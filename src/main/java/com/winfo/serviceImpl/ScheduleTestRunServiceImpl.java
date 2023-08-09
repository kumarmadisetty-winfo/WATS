
package com.winfo.serviceImpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.winfo.exception.WatsEBSException;
import com.winfo.model.TestSet;
import com.winfo.repository.TestSetRepository;
import com.winfo.repository.UserSchedulerJobRepository;
import com.winfo.service.ScheduleTestRunService;
import com.winfo.utils.Constants;
import com.winfo.vo.CopytestrunVo;
import com.winfo.vo.ResponseDto;
import com.winfo.vo.ScheduleJobVO;
import com.winfo.vo.ScheduleSubJobVO;

import reactor.core.publisher.Mono;

@Service
public class ScheduleTestRunServiceImpl implements ScheduleTestRunService {

	public static final Logger logger = Logger.getLogger(ScheduleTestRunServiceImpl.class);
	
	@Value("${hubUrl}")
	private String basePath;
	
	@Autowired
	TestSetRepository testSetRepository;

	@Autowired
	UserSchedulerJobRepository userSchedulerJobRepository;
	
	@Autowired
	CopyTestRunService copyTestRunService;
	
	public  ResponseDto createNewScheduledJob(ScheduleJobVO scheduleJobVO) {
		try {	
			AtomicInteger count=new AtomicInteger(1);
			int jobId=userSchedulerJobRepository.getNewJobIdFromSequence();
			scheduleJobVO.getTestRuns().parallelStream().forEach(testRunVO->{
				if(testRunVO.getNewTestRunName()!=null || !"".equals(testRunVO.getNewTestRunName())) {
					TestSet testRun=testSetRepository.findByTestRunName(testRunVO.getTemplateTestRun());
					CopytestrunVo copyTestrunvo =new CopytestrunVo();
					copyTestrunvo.setConfiguration(scheduleJobVO.getConfigurationId());
					copyTestrunvo.setCreatedBy(scheduleJobVO.getSchedulerEmail());
					copyTestrunvo.setCreationDate(new Date());
					copyTestrunvo.setIncrementValue(testRunVO.getAutoIncrement());
					copyTestrunvo.setNewtestrunname(testRunVO.getNewTestRunName());
					copyTestrunvo.setProject(scheduleJobVO.getProjectId());
					copyTestrunvo.setRequestType("copyTestRun");
					copyTestrunvo.setTestScriptNo(testRun.getTestRunId());
					try {
						testRunVO.setTemplateTestRun(String.valueOf(copyTestRunService.copyTestrun(copyTestrunvo)));
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
				TestSet testRun=testSetRepository.findByTestRunName(testRunVO.getTemplateTestRun());
				String newSubSchedularName=scheduleJobVO.getSchedulerName()+"ADDEDNUM"+count;
				ScheduleSubJobVO scheduleSubJobVO=new ScheduleSubJobVO();
				scheduleSubJobVO.setEmail(testRunVO.getNotification());
				scheduleSubJobVO.setJobId(jobId);
				scheduleSubJobVO.setStartDate(testRunVO.getStartDate().toString());
				scheduleSubJobVO.setSubJobName(newSubSchedularName);
				scheduleSubJobVO.setTestRunName(testRun.getTestRunName());
				scheduleSubJobVO.setTestSetId(testRun.getTestRunId());
				scheduleSubJobVO.setProjectId(scheduleJobVO.getProjectId());
				scheduleSubJobVO.setConfigurationId(scheduleJobVO.getConfigurationId());
				scheduleSubJobVO.setUserName(scheduleJobVO.getSchedulerEmail());
				WebClient webClient = WebClient.create(basePath.substring(0, basePath.length()-1)+"3/wats/wats_workspace_prod/WATSservice/scheduleTestRun");
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
