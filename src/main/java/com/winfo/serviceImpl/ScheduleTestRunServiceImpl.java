package com.winfo.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winfo.exception.WatsEBSException;
import com.winfo.model.Scheduler;
import com.winfo.model.TestSet;
import com.winfo.model.UserSchedulerJob;
import com.winfo.reports.PDFGenerator;
import com.winfo.repository.ConfigLinesRepository;
import com.winfo.repository.ConfigurationRepository;
import com.winfo.repository.CustomerRepository;
import com.winfo.repository.SchedulerRepository;
import com.winfo.repository.TestSetRepository;
import com.winfo.repository.UserSchedulerJobRepository;
import com.winfo.service.ScheduleTestRunService;
import com.winfo.service.ValidationService;
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
	UserSchedulerJobRepository userSchedulerJobRepository;

	@Autowired
	CopyTestRunService copyTestRunService;

	@Autowired
	ConfigurationRepository configurationRepository;

	@Autowired
	ConfigLinesRepository configLinesRepository;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	ValidationService validationService;

	@Transactional
	public ResponseDto createNewScheduledJob(ScheduleJobVO scheduleJobVO) {
		try {
			AtomicInteger count = new AtomicInteger(0);
			Scheduler scheduler = schedulerRepository.findByJobName(scheduleJobVO.getSchedulerName());
			if (scheduler == null) {
				logger.info("Create Schedule object ::" + scheduleJobVO.toString());
				scheduler = new Scheduler();
				scheduler.setConfigurationId(scheduleJobVO.getConfigurationId());
				scheduler.setProjectId(scheduleJobVO.getProjectId());
				scheduler.setCreatedBy(scheduleJobVO.getSchedulerEmail());
				scheduler.setCreationDate(new Date());
				scheduler.setEmail(scheduleJobVO.getSchedulerEmail());
				scheduler.setJobName(scheduleJobVO.getSchedulerName().replaceAll("\\s+", " "));
				scheduler.setStatus(Constants.YET_TO_START);
				scheduler = schedulerRepository.save(scheduler);
			}
			int jobId = createSchedule(scheduleJobVO, scheduleJobVO.getTestRuns(), count, scheduler,false);
			return new ResponseDto(HttpStatus.OK.value(), Constants.SUCCESS,
					jobId + ":Successfully created new " + scheduleJobVO.getSchedulerName() + " job");
		} catch (WatsEBSException e) {
			logger.error(Constants.INTERNAL_SERVER_ERROR + " - " + e.getMessage() + " - "
					+ scheduleJobVO.getSchedulerName());
			return new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.ERROR,
					scheduleJobVO.getSchedulerName() + " is not created successfully - " + e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.ERROR, e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public ResponseDto editScheduledJob(ScheduleJobVO scheduleJobVO) {
		try {
			Scheduler scheduler = schedulerRepository.findByJobName(scheduleJobVO.getSchedulerName());
			logger.info(String
					.format("Schedule job name:" + scheduler.getJobName() + "Schedule job id:" + scheduler.getJobId()));
			Optional<List<UserSchedulerJob>> listOfSubJob = userSchedulerJobRepository
					.findByJobId(scheduler.getJobId());

			validateScheduleTestRuns(scheduler,scheduleJobVO.getTestRuns(),false);
			
			// delete test runs from a schedule
			if (listOfSubJob.isPresent()) {
				// get list of all test run name from request body(JSON VO)
				List<String> listOfAllSubJobFromVO = scheduleJobVO.getTestRuns().parallelStream()
						.filter(Objects::nonNull).map(ScheduleTestRunVO::getTemplateTestRun)
						.collect(Collectors.toList());
				// get list of test run name which are present in database(Except newly added
				// test runs) from request body(JSON VO)
				List<String> listOfExistingSubJobInDBFromVO = listOfSubJob.get().parallelStream()
						.filter(Objects::nonNull)
						.filter(subScheduleJob -> listOfAllSubJobFromVO.contains(subScheduleJob.getComments()))
						.map(UserSchedulerJob::getComments).collect(Collectors.toList());
				// delete test run from APEX RestAPI and return the list of deleted test runs
				List<UserSchedulerJob> listOfDeletedSubJob = listOfSubJob.get().parallelStream()
						.filter(Objects::nonNull).filter(subScheduleJob -> {
							if (!listOfExistingSubJobInDBFromVO.contains(subScheduleJob.getComments())) {
								ScheduleSubJobVO scheduleSubJobVO = new ScheduleSubJobVO();
								scheduleSubJobVO.setSubJobName(subScheduleJob.getJobName());
								try {
									WebClient webClient = WebClient.create(basePath + Constants.FORWARD_SLASH
											+ Constants.WATS_SERVICE + Constants.FORWARD_SLASH
											+ Constants.DELETE_SCHEDULE_TEST_RUN_ENDPOINT);
									String result = webClient.post().syncBody(scheduleSubJobVO).retrieve()
											.bodyToMono(String.class).block();
									if (result != null) {
										ObjectMapper objectMapper = new ObjectMapper();
										Map<String, Object> jsonMap = null;
										jsonMap = objectMapper.readValue(result, Map.class);
										Integer itemsObject = Integer.parseInt((String) jsonMap.get("status"));
										if (itemsObject != HttpStatus.OK.value()) {
											logger.error(Constants.INVALID_RESPOSNE_APEX_API + " - "
													+ Constants.DELETE_SCHEDULE_TEST_RUN_ENDPOINT);
											throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
													Constants.INVALID_RESPOSNE_APEX_API);
										}
									} else {
										logger.error(Constants.NO_RESPOSNE_APEX_API + " - "
												+ Constants.DELETE_SCHEDULE_TEST_RUN_ENDPOINT);
										throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
												Constants.NO_RESPOSNE_APEX_API);
									}
									return true;
								} catch (JsonProcessingException e) {
									logger.error(Constants.JOSN_PARSING_ERROR + " - " + e.getMessage() + " - "
											+ Constants.DELETE_SCHEDULE_TEST_RUN_ENDPOINT);
									throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
											Constants.INTERNAL_SERVER_ERROR, e);
								} catch (Exception e) {
									logger.error(Constants.INTERNAL_SERVER_ERROR + " - " + e.getMessage() + " - "
											+ Constants.DELETE_SCHEDULE_TEST_RUN_ENDPOINT);
									throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
											Constants.INTERNAL_SERVER_ERROR, e);
								}
							}
							return false;
						}).collect(Collectors.toList());
				// remove deleted test runs from original list of DB
				if (listOfDeletedSubJob.size() > 0)
					listOfSubJob.get().removeAll(listOfDeletedSubJob);
			}

			// add test runs in a schedule
			if ((scheduleJobVO.getTestRuns().size() > 0 && !listOfSubJob.isPresent())
					|| (scheduleJobVO.getTestRuns().size() > listOfSubJob.get().size())) {
				List<String> listOfSubJobFromDB = listOfSubJob.isPresent()
						? listOfSubJob.get().parallelStream().filter(Objects::nonNull)
								.map(UserSchedulerJob::getComments).collect(Collectors.toList())
						: new ArrayList<>();
				logger.info(String.format("list of schedule TestRuns from DB :"
						+ listOfSubJobFromDB.stream().collect(Collectors.joining(","))));
				List<String> listOfSubJobFromVO = scheduleJobVO.getTestRuns().parallelStream().filter(Objects::nonNull)
						.map(ScheduleTestRunVO::getTemplateTestRun).collect(Collectors.toList());
				logger.info(String.format("list of schedule TestRuns from edit :"
						+ listOfSubJobFromVO.stream().collect(Collectors.joining(","))));
				List<String> newAddedSubJobNames = listOfSubJobFromVO.parallelStream().filter(Objects::nonNull)
						.filter(subJobName -> !listOfSubJobFromDB.contains(subJobName)).collect(Collectors.toList());
				List<ScheduleTestRunVO> newAddedSubJobs = scheduleJobVO.getTestRuns().parallelStream()
						.filter(Objects::nonNull)
						.filter(subJob -> newAddedSubJobNames.contains(subJob.getTemplateTestRun()))
						.collect(Collectors.toList());
				if (newAddedSubJobs.size() > 0) {
					AtomicInteger count = new AtomicInteger(listOfSubJob.isPresent() ? listOfSubJob.get().size() : 0);
					createSchedule(scheduleJobVO, newAddedSubJobs, count, scheduler,true);
				}
			}
			// Edit schedule details and test run time and email notification
			if (listOfSubJob.isPresent()) {
				scheduleJobVO.getTestRuns().parallelStream().forEach(testRunVO -> {
					listOfSubJob.get().parallelStream().forEach(subScheduleJob -> {
						if (testRunVO.getTemplateTestRun().equalsIgnoreCase(subScheduleJob.getComments())) {
							ScheduleSubJobVO scheduleSubJobVO = new ScheduleSubJobVO();
							scheduleSubJobVO.setSubJobName(subScheduleJob.getJobName());
							scheduleSubJobVO.setJobId(subScheduleJob.getJobId());
							scheduleSubJobVO.setStartDate(testRunVO.getStartDate());
							scheduleSubJobVO.setUserName(scheduleJobVO.getSchedulerEmail());
							scheduleSubJobVO.setEmail(testRunVO.getNotification());
							scheduleSubJobVO.setTestRunName(subScheduleJob.getComments());
							scheduleSubJobVO.setTestSetId(
									testSetRepository.findByTestRunName(subScheduleJob.getComments()).getTestRunId());
							scheduleSubJobVO.setSequenceNumber(testRunVO.getSequenceNumber());
							scheduleSubJobVO.setType(testRunVO.getType());
							try {
								WebClient webClient = WebClient.create(basePath + "/WATSservice/editScheduleTestRun");
								Mono<String> result = webClient.post().syncBody(scheduleSubJobVO).retrieve()
										.bodyToMono(String.class);
								result.block();
							} catch (Exception e) {
								logger.error(e.getMessage());
								throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
										"Error occurred from APEX web service of edit scheduled job", e);
							}
						}
					});
				});
			}
			scheduler.setStatus(Constants.YET_TO_START);
			scheduler.setUpdatedBy(scheduleJobVO.getSchedulerEmail());
			scheduler.setEmail(scheduleJobVO.getSchedulerEmail());
			scheduler.setUpdatedDate(new Date());
			schedulerRepository.save(scheduler);
			return new ResponseDto(HttpStatus.OK.value(), Constants.SUCCESS,
					scheduler.getJobId() + ":Successfully updated the " + scheduler.getJobName() + " job");
		}catch (WatsEBSException e) {
			logger.error(Constants.INTERNAL_SERVER_ERROR + " - " + e.getMessage() + " - "
					+ scheduleJobVO.getSchedulerName());
			return new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.ERROR,
					scheduleJobVO.getSchedulerName() + " is not created successfully - " + e.getMessage());
		}  catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.ERROR, e.getMessage());
		}
	}

	public int createSchedule(ScheduleJobVO scheduleJobVO, List<ScheduleTestRunVO> listOfTestRunInJob,
			AtomicInteger count, Scheduler scheduler, boolean isValidated) {
		String jobName = scheduler.getJobName().replaceAll("\\s", "").toUpperCase();
		int jobId = scheduler.getJobId();
		logger.info(String.format("Schedule job name:" + jobName + " Schedule job id:" + jobId));
		validateScheduleTestRuns(scheduler,listOfTestRunInJob,isValidated);

		listOfTestRunInJob.parallelStream().filter(Objects::nonNull).forEach(testRunVO -> {
			if (testRunVO.getTestRunName() != null && !"".equals(testRunVO.getTestRunName())) {
				TestSet testRun = testSetRepository.findByTestRunName(testRunVO.getTemplateTestRun());
				logger.info(String.format("TestRun Id : %s, TestRun Name : %s, Project Id : %s", testRun.getTestRunId(),
						testRunVO.getTestRunName(), scheduleJobVO.getProjectId()));
				CopytestrunVo copyTestrunvo = new CopytestrunVo();
				copyTestrunvo.setConfiguration(scheduleJobVO.getConfigurationId());
				copyTestrunvo.setCreatedBy(scheduleJobVO.getSchedulerEmail());
				copyTestrunvo.setCreationDate(new Date());
				copyTestrunvo.setIncrementValue(testRunVO.getAutoIncrement());
				copyTestrunvo.setNewtestrunname(testRunVO.getTestRunName());
				copyTestrunvo.setProject(scheduleJobVO.getProjectId());
				copyTestrunvo.setRequestType("copyTestRun");
				copyTestrunvo.setTestScriptNo(testRun.getTestRunId());
				try {
					testRunVO.setTemplateTestRun(testSetRepository
							.findByTestRunId(copyTestRunService.copyTestrun(copyTestrunvo)).getTestRunName());
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

				String newSubSchedularName = jobName + Constants.ADDEDNUM + count.incrementAndGet();
				ScheduleSubJobVO scheduleSubJobVO = new ScheduleSubJobVO();
				scheduleSubJobVO.setEmail(testRunVO.getNotification());
				scheduleSubJobVO.setJobId(jobId);
				scheduleSubJobVO.setStartDate(testRunVO.getStartDate());
				scheduleSubJobVO.setSubJobName(newSubSchedularName);
				scheduleSubJobVO.setTestRunName(testRun.getTestRunName());
				scheduleSubJobVO.setTestSetId(testRun.getTestRunId());
				scheduleSubJobVO.setUserName(scheduleJobVO.getSchedulerEmail());
				scheduleSubJobVO.setType(testRunVO.getType());
				scheduleSubJobVO.setSequenceNumber(testRunVO.getSequenceNumber());
				logger.info(String.format("TestRun Id : %s, TestRun Name : %s, Project Id : %s", testRun.getTestRunId(),
						testRun.getTestRunName(), scheduleJobVO.getProjectId()));
				logger.info("WebClient URL:" + (basePath + "/WATSservice/scheduleTestRun"));
				try {
					WebClient webClient = WebClient.create(basePath + "/WATSservice/scheduleTestRun");
					Mono<String> response = webClient.post().syncBody(scheduleSubJobVO).retrieve()
							.bodyToMono(String.class);
					response.block();
				} catch (Exception e) {
					logger.error(e.getMessage());
					throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Error occurred from APEX web service of create scheduled job", e);
				}
			}
		});
		return jobId;
	}

	public ResponseDto generateScheduleSummaryTestRunReport(int jobId) {

		ResponseDto response = null;
		try {
			logger.info("JobId " + jobId);
			int configId = schedulerRepository.findByJobId(jobId).getConfigurationId();
			String pdfPath = configLinesRepository.getValueFromKeyNameAndConfigurationId(Constants.PDF_PATH, configId);
			int customerId = configurationRepository.getCustomerIdUsingconfigurationId(configId);
			String customerName = customerRepository.findByCustomerId(customerId).getCustomerName();
			logger.info("PdfPath : " + pdfPath + "," + " customerName : " + customerName);
			if (StringUtils.isNotBlank(pdfPath) && StringUtils.isNotBlank(customerName)) {
				response = PDFGenerator.createPDF(jobId, pdfPath, customerName);
				if (response.getStatusCode() == HttpStatus.OK.value()) {
					logger.info("Schedule summary report regeneration has done successfully : " + jobId);
				}
			} else {
				logger.info("Pdf path and customerName should not be null");
				response = new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.ERROR,
						"Exception occurred while regenerating the schedule summary report");
			}
		} catch (Exception e) {
			logger.error("Exception occurred while regenerating the schedule summary report : " + jobId);
		}
		return response;
	}
	
	private void validateScheduleTestRuns( Scheduler scheduler,List<ScheduleTestRunVO> listOfTestRunInJob,boolean isValidated) {
		if(!isValidated){
			List<ResponseEntity<ResponseDto>> result = new ArrayList<>();
			listOfTestRunInJob.parallelStream().filter(Objects::nonNull).forEach(testRunVO -> {
				TestSet testSet = testSetRepository.findByTestRunName(testRunVO.getTemplateTestRun());
				if (testSet != null) {
					try {
						result.add(validationService.validateTestRun(testSet.getTestRunId(), true));
					} catch (ConstraintViolationException e) {
						logger.error(Constants.INTERNAL_SERVER_ERROR + " - " + e.getMessage() + " - "
								+ testSet.getTestRunId() + " - " + testSet.getTestRunName());
						throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
								Constants.INVALID_CREDENTIALS_CONFIG_MESSAGE + " of " + testSet.getTestRunName());
						
					} catch (Exception e) {
						logger.error(Constants.INTERNAL_SERVER_ERROR + " - " + e.getMessage() + " - "
								+ testSet.getTestRunId() + " - " + testSet.getTestRunName());
						throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
					}
				} else {
					logger.error(Constants.INTERNAL_SERVER_ERROR + " - " + Constants.INVALID_TEST_SET_ID + " - "
							+ testRunVO.getTemplateTestRun());
					throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.INTERNAL_SERVER_ERROR
							+ " - " + Constants.INVALID_TEST_SET_ID + " - " + testRunVO.getTemplateTestRun());
				}
			});
			result.parallelStream().forEach(responseEntity->{
				if(Integer.parseInt(responseEntity.getStatusCode().toString())!=HttpStatus.OK.value())result.remove(responseEntity);
			});
			if (result.size()>0) {
				logger.error(Constants.INTERNAL_SERVER_ERROR +" - "+scheduler.getJobName()+" - "+scheduler.getJobId());
				throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(),scheduler.getJobName() + " is not " + Constants.VALIDATED_SUCCESSFULLY);
			}
		}
	}
}
