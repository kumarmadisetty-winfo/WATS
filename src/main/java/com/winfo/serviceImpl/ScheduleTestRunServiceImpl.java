
package com.winfo.serviceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
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
import com.winfo.model.UserSchedulerJob;
import com.winfo.repository.SchedulerRepository;
import com.winfo.repository.TestSetRepository;
import com.winfo.repository.UserSchedulerJobRepository;
import com.winfo.service.ScheduleTestRunService;
import com.winfo.utils.Constants;
import com.winfo.vo.CopytestrunVo;
import com.winfo.vo.ResponseDto;
import com.winfo.vo.ScheduleJobVO;
import com.winfo.vo.ScheduleSubJobVO;
import com.winfo.vo.ScheduleTestRunVO;
import java.time.*;
import java.time.format.DateTimeFormatter;
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
	UserSchedulerJobRepository userSchedulerJobRepository;
	
	@Autowired
	CopyTestRunService copyTestRunService;
	
	@Transactional
	public  ResponseDto createNewScheduledJob(ScheduleJobVO scheduleJobVO) {
		try {	
			AtomicInteger count=new AtomicInteger(0);
			createSchedule(scheduleJobVO,scheduleJobVO.getTestRuns(),count);
			return new ResponseDto(HttpStatus.OK.value(), Constants.SUCCESS, "Successfully created new job");
		}catch(Exception e) {
			logger.error(e.getMessage());
			return new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.ERROR, e.getMessage());
		}
	}
	
	public ResponseDto editScheduledJob(ScheduleJobVO scheduleJobVO) {
		try {
			Scheduler scheduler = schedulerRepository.findByJobName(scheduleJobVO.getSchedulerName().toUpperCase());
			List<UserSchedulerJob> listOfSubJob = userSchedulerJobRepository.findByJobId(scheduler.getJobId());
			if (scheduleJobVO.getTestRuns().size() > listOfSubJob.size()) {
				List<String> listOfSubJobFromDB = listOfSubJob.parallelStream().map(UserSchedulerJob::getComments)
						.collect(Collectors.toList());
				List<String> listOfSubJobFromVO = scheduleJobVO.getTestRuns().parallelStream()
						.map(ScheduleTestRunVO::getTemplateTestRun).collect(Collectors.toList());
				List<String> newAddedSubJobNames = listOfSubJobFromVO.parallelStream()
						.filter(subJobName -> !listOfSubJobFromDB.contains(subJobName)).collect(Collectors.toList());
				List<ScheduleTestRunVO> newAddedSubJobs = scheduleJobVO.getTestRuns().parallelStream()
						.filter(subJob -> newAddedSubJobNames.contains(subJob.getTemplateTestRun()))
						.collect(Collectors.toList());
				if (newAddedSubJobs.size() > 0) {
					AtomicInteger count = new AtomicInteger(listOfSubJob.size());
					createSchedule(scheduleJobVO, newAddedSubJobs, count);
				}
			}

			scheduleJobVO.getTestRuns().parallelStream().forEach(testRunVO->{
				listOfSubJob.parallelStream().forEach(subScheduleJob -> {
					if (testRunVO.getTemplateTestRun().equalsIgnoreCase(subScheduleJob.getComments())) {
						System.out.println(subScheduleJob.getStartDate());
						System.out.println(testRunVO.getStartDate());
						String convertedTime = convertTimeFormat(subScheduleJob.getStartDate());
						System.out.println(convertedTime);
						if (!testRunVO.getStartDate().equalsIgnoreCase(convertedTime)) {
							ScheduleSubJobVO scheduleSubJobVO = new ScheduleSubJobVO();
							scheduleSubJobVO.setSubJobName(subScheduleJob.getJobName());
							scheduleSubJobVO.setJobId(subScheduleJob.getJobId());
							scheduleSubJobVO.setStartDate(testRunVO.getStartDate());
							scheduleSubJobVO.setUserName(scheduleJobVO.getSchedulerEmail());
							WebClient webClient = WebClient.create(basePath + "/WATSservice/editScheduleTestRun");
							Mono<String> result = webClient.post().syncBody(scheduleSubJobVO).retrieve()
									.bodyToMono(String.class);
							result.block();
						}
					}
				});
			});
			return new ResponseDto(HttpStatus.OK.value(), Constants.SUCCESS, "Successfully updated the job");
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.ERROR, e.getMessage());
		}
	}
	
	public void createSchedule(ScheduleJobVO scheduleJobVO, List<ScheduleTestRunVO> listOfTestRunInJob,AtomicInteger count) {
		listOfTestRunInJob.parallelStream().forEach(testRunVO->{
			count.incrementAndGet();
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
			Scheduler scheduler =schedulerRepository.findByJobName(scheduleJobVO.getSchedulerName().toUpperCase());
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
		});
	}
	
    public String convertTimeFormat(String inputTime) {
        try {
            String[] parts = inputTime.split(" ");
            String dateTimePart = parts[0] + "T" + parts[1];
            String offsetPart = parts[2];

            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.S");
            LocalDateTime localDateTime = LocalDateTime.parse(dateTimePart, inputFormatter);

            String sign = offsetPart.startsWith("+") ? "+" : "-";
            int offsetHours = Integer.parseInt(offsetPart.substring(1, offsetPart.indexOf(":")));
            int offsetMinutes = Integer.parseInt(offsetPart.substring(offsetPart.indexOf(":") + 1));

            ZoneOffset zoneOffset = ZoneOffset.ofHoursMinutes(Integer.parseInt(sign + offsetHours), offsetMinutes);
            OffsetDateTime offsetDateTime = localDateTime.atOffset(zoneOffset);

            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MMM-yy hh.mm.ss.SSSSSSSSS a XXX");
            String formattedDate = offsetDateTime.format(outputFormatter);
            
            return StringUtils.countMatches(formattedDate,'-')>2?formattedDate.replace("+", "").replaceFirst("(?s)-(?!.*-)", ""):formattedDate.replace("+", "");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
