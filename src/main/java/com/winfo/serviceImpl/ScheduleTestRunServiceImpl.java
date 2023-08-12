
package com.winfo.serviceImpl;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.winfo.exception.WatsEBSException;
import com.winfo.model.Scheduler;
import com.winfo.model.TestSet;
import com.winfo.repository.SchedulerRepository;
import com.winfo.repository.TestSetRepository;
import com.winfo.service.ScheduleTestRunService;
import com.winfo.utils.Constants;
import com.winfo.vo.CopytestrunVo;
import com.winfo.vo.ResponseDto;
import com.winfo.vo.ScheduleJobVO;
import com.winfo.vo.ScheduleSubJobVO;
import com.winfo.vo.ScheduleTestRunVO;

import reactor.core.publisher.Mono;

@Service
public class ScheduleTestRunServiceImpl implements ScheduleTestRunService {

	public static final Logger logger = Logger.getLogger(ScheduleTestRunServiceImpl.class);
	
	@Value("${apex.webservice.basePath}")
	private String basePath;
	
	@Autowired
	TestSetRepository testSetRepository;

	@Autowired
	SchedulerRepository schedulerRepository;
	
	@Autowired
	CopyTestRunService copyTestRunService;
	
	@Transactional
	public  ResponseDto createNewScheduledJob(ScheduleJobVO scheduleJobVO) {
		try {	
			AtomicInteger count=new AtomicInteger(1);
			scheduleJobVO.getTestRuns().parallelStream().forEach(testRunVO->{
				if(testRunVO.getTestRunName()!=null && !"".equals(testRunVO.getTestRunName())) {
					TestSet testRun=testSetRepository.findByTestRunName(testRunVO.getTemplateTestRun());
					CopytestrunVo copyTestrunvo =new CopytestrunVo();
					copyTestrunvo.setConfiguration(scheduleJobVO.getConfigurationId());
					copyTestrunvo.setCreatedBy(scheduleJobVO.getSchedulerEmail());
					copyTestrunvo.setCreationDate(new Date());
					copyTestrunvo.setIncrementValue(testRunVO.getAutoIncrement());
					copyTestrunvo.setNewtestrunname(testRunVO.getTestRunName());
					copyTestrunvo.setProject(scheduleJobVO.getProjectId());
					copyTestrunvo.setRequestType("copyTestRun");
					copyTestrunvo.setTestScriptNo(testRun.getTestRunId());
					try {
						testRunVO.setTemplateTestRun(testSetRepository.findByTestRunId(copyTestRunService.copyTestrun(copyTestrunvo)).getTestRunName());
					} catch (JsonMappingException e) {
						logger.error(e.getMessage());
						new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
					} catch (JsonProcessingException e) {
						logger.error(e.getMessage());
						new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
					} catch (InterruptedException e) {
						logger.error(e.getMessage());
						new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
					}
				}
				
				Scheduler scheduler =schedulerRepository.findByJobName(scheduleJobVO.getSchedulerName());
				if(scheduler==null) {
					scheduler=new Scheduler();
					scheduler.setConfigurationId(scheduleJobVO.getConfigurationId());
					scheduler.setProjectId(scheduleJobVO.getProjectId());
					scheduler.setCreatedBy(scheduleJobVO.getSchedulerEmail());
					scheduler.setCreationDate(new Date());
					scheduler.setEmail(scheduleJobVO.getSchedulerEmail());
					scheduler.setJobName(scheduleJobVO.getSchedulerName().toUpperCase());
					scheduler=schedulerRepository.save(scheduler);
				}
				
				TestSet testRun=testSetRepository.findByTestRunName(testRunVO.getTemplateTestRun());
				String newSubSchedularName=scheduler.getJobName()+"ADDEDNUM"+count;
				ScheduleSubJobVO scheduleSubJobVO=new ScheduleSubJobVO();
				scheduleSubJobVO.setEmail(testRunVO.getNotification());
				scheduleSubJobVO.setJobId(scheduler.getJobId());
				scheduleSubJobVO.setStartDate(testRunVO.getStartDate());
				scheduleSubJobVO.setSubJobName(newSubSchedularName);
				scheduleSubJobVO.setTestRunName(testRun.getTestRunName());
				scheduleSubJobVO.setTestSetId(testRun.getTestRunId());
				scheduleSubJobVO.setUserName(scheduleJobVO.getSchedulerEmail());
				WebClient webClient = WebClient.create(basePath+"/WATSservice/scheduleTestRun");
				Mono<String> result = webClient.post().syncBody(scheduleSubJobVO).retrieve().bodyToMono(String.class);
				result.block();
				count.incrementAndGet();
			});
			return new ResponseDto(HttpStatus.OK.value(), Constants.SUCCESS, "Successfully created new job");
		}catch(Exception e) {
			logger.error(e.getMessage());
			return new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.ERROR, e.getMessage());
		}
	}
}
