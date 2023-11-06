package com.winfo.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
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
import javax.validation.ConstraintValidatorContext;
import reactor.core.publisher.Mono;

@Service
public class ScheduleTestRunServiceImpl implements ScheduleTestRunService {

	public static final Logger logger = Logger.getLogger(ScheduleTestRunServiceImpl.class);

	@Autowired
	ThreadPoolTaskExecutor customTaskExecutor;
	
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
			validateScheduleTestRuns(scheduleJobVO.getSchedulerName(), scheduleJobVO.getTestRuns());
			AtomicInteger count = new AtomicInteger(0);
			Scheduler scheduler = schedulerRepository.findByJobName(scheduleJobVO.getSchedulerName());
			if (scheduler == null) {
				logger.info("Create Schedule object ::" + scheduleJobVO.toString());
				scheduler = new Scheduler();
				if(scheduleJobVO.isTemplate())scheduler.setTemplate(String.valueOf(scheduleJobVO.isTemplate()));
				scheduler.setConfigurationId(scheduleJobVO.getConfigurationId());
				scheduler.setProjectId(scheduleJobVO.getProjectId());
				scheduler.setCreatedBy(scheduleJobVO.getSchedulerEmail());
				scheduler.setCreationDate(new Date());
				scheduler.setEmail(scheduleJobVO.getSchedulerEmail());
				scheduler.setJobName(scheduleJobVO.getSchedulerName().replaceAll("\\s+", " "));
				scheduler.setStatus(Constants.YET_TO_START);
				scheduler = schedulerRepository.save(scheduler);
			}
			int jobId = createSchedule(scheduleJobVO, scheduleJobVO.getTestRuns(), count, scheduler, false);
			return new ResponseDto(HttpStatus.OK.value(), Constants.SUCCESS,
					jobId + ":Successfully created new " + scheduleJobVO.getSchedulerName() + " job");
		} catch (WatsEBSException e) {
			logger.error(Constants.INTERNAL_SERVER_ERROR + " - " + e.getMessage() + " - "
					+ scheduleJobVO.getSchedulerName());
		        String errorMessage = e.getMessage();
		        if (e.getMessage() != null && e.getMessage().contains(": ")) {
		        	errorMessage = e.getMessage().substring(e.getMessage().lastIndexOf(": ") + 2);
		        }
			return new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.ERROR,
					scheduleJobVO.getSchedulerName() + " is not created successfully - " + errorMessage);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.ERROR, e.getMessage());
		}
	}

	@Transactional
	public ResponseDto editScheduledJob(ScheduleJobVO scheduleJobVO) {
		try {
			validateScheduleTestRuns(scheduleJobVO.getSchedulerName(), scheduleJobVO.getTestRuns());

			Scheduler scheduler = schedulerRepository.findByJobName(scheduleJobVO.getSchedulerName());
			logger.info(String
					.format("Schedule job name:" + scheduler.getJobName() + "Schedule job id:" + scheduler.getJobId()));
			Optional<List<UserSchedulerJob>> listOfSubJob = userSchedulerJobRepository
					.findByJobId(scheduler.getJobId());

			deleteTestRunFromSchedule(scheduleJobVO, listOfSubJob);

			addTestRunInSchedule(scheduleJobVO, listOfSubJob, scheduler);

			editTestRunInSchedule(scheduleJobVO, listOfSubJob);

			scheduler.setStatus(Constants.YET_TO_START);
			scheduler.setUpdatedBy(scheduleJobVO.getSchedulerEmail());
			scheduler.setEmail(scheduleJobVO.getSchedulerEmail());
			scheduler.setUpdatedDate(new Date());
			schedulerRepository.save(scheduler);
			return new ResponseDto(HttpStatus.OK.value(), Constants.SUCCESS,
					scheduler.getJobId() + ":Successfully updated the " + scheduler.getJobName() + " job");
		} catch (WatsEBSException e) {
			logger.error(Constants.INTERNAL_SERVER_ERROR + " - " + e.getMessage() + " - "
					+ scheduleJobVO.getSchedulerName());
			return new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.ERROR, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.ERROR, e.getMessage());
		}
	}

	public int createSchedule(ScheduleJobVO scheduleJobVO, List<ScheduleTestRunVO> listOfTestRunInJob,
			AtomicInteger count, Scheduler scheduler, boolean isValidated) {
		String jobName = scheduler.getJobName().replaceAll("\\s", "").toUpperCase();
		int jobId = scheduler.getJobId();
		logger.info(String.format("Schedule job name:" + jobName + " Schedule job id:" + jobId));
		List<CompletableFuture<Void>> futures = listOfTestRunInJob.stream()
				.map(testRunVO -> CompletableFuture.supplyAsync(() -> testRunVO))
				.map(future -> future.thenAcceptAsync(testRunVO -> {
					if (testRunVO.getTestRunName() != null && !"".equals(testRunVO.getTestRunName())) {
						TestSet testRun = testSetRepository.findByTestRunName(testRunVO.getTemplateTestRun());
						logger.info(String.format("TestRun Id : %s, TestRun Name : %s, Project Id : %s",
								testRun.getTestRunId(), testRunVO.getTestRunName(), scheduleJobVO.getProjectId()));
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
					}
					TestSet testRun = testSetRepository.findByTestRunName(testRunVO.getTemplateTestRun());
					testRun.setCopyIncreamentFlag(testRunVO.getAutoIncrement());
					testSetRepository.save(testRun);
					String newSubSchedularName = Constants.PREFIX_SCHEDULE_NAME + jobName + Constants.ADDEDNUM
							+ count.incrementAndGet();
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
					if (scheduleJobVO.isTemplate()) {
						testRun.setTemplate(String.valueOf(scheduleJobVO.isTemplate()));
						testSetRepository.save(testRun);
						scheduleSubJobVO.setType(Constants.TEMPLATE);
					}
					logger.info(String.format("TestRun Id : %s, TestRun Name : %s, Project Id : %s",
							testRun.getTestRunId(), testRun.getTestRunName(), scheduleJobVO.getProjectId()));
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
				}, customTaskExecutor)).collect(Collectors.toList());
		CompletableFuture<Void> allOfFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

		List<String> exceptionMessages = new ArrayList<>();
		allOfFutures.exceptionally(ex -> {
			futures.forEach(future -> {
				if (future.isCompletedExceptionally()) {
					future.whenComplete((result, throwable) -> {
						if (throwable != null) {
							exceptionMessages.add(throwable.getMessage());
						}
					});
				}
			});
			return null;
		});
		try {
			allOfFutures.get();
		} catch (Exception e) {
			logger.error("Error waiting for CompletableFuture: " + e.getMessage());
		}
		if (!exceptionMessages.isEmpty()) {
			String jsonErrorMessage = exceptionMessages.stream().map(message -> message)
					.collect(Collectors.joining(","));
			String jsonResponse = String.format(jsonErrorMessage);
			throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), jsonResponse);
		}
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

	private void validateScheduleTestRuns(String scheduleName, List<ScheduleTestRunVO> listOfTestRunInJob)
			throws Exception {
		List<ResponseEntity<ResponseDto>> result = new ArrayList<>();
		List<String> uniqueTemplateTestRuns = listOfTestRunInJob.parallelStream()
				.map(ScheduleTestRunVO::getTemplateTestRun).distinct().collect(Collectors.toList());
		List<CompletableFuture<Void>> futures = uniqueTemplateTestRuns.stream()
				.map(templateTestRun -> CompletableFuture.supplyAsync(() -> templateTestRun))
				.map(future -> future.thenAcceptAsync(templateTestRun -> {
					TestSet testSet = testSetRepository.findByTestRunName(templateTestRun);
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
								+ templateTestRun);
						throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
								Constants.INTERNAL_SERVER_ERROR + " - " + Constants.INVALID_TEST_SET_ID + " - "
										+ templateTestRun);
					}
				}, customTaskExecutor)).collect(Collectors.toList());
		CompletableFuture<Void> allOfFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

		List<String> exceptionMessages = new ArrayList<>();
		allOfFutures.exceptionally(ex -> {
			futures.forEach(future -> {
				if (future.isCompletedExceptionally()) {
					future.whenComplete((response, throwable) -> {
						if (throwable != null) {
							exceptionMessages.add(throwable.getMessage());
						}
					});
				}
			});
			return null;
		});
		try {
			allOfFutures.get();
		} catch (Exception e) {
			logger.error("Error waiting for CompletableFuture: " + e.getMessage());
		}
		if (!exceptionMessages.isEmpty()) {
			String jsonErrorMessage = exceptionMessages.stream().map(message -> message)
					.collect(Collectors.joining(","));
			String jsonResponse = String.format(jsonErrorMessage);
			throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), jsonResponse);
		}

		Map<Boolean, List<ResponseEntity<ResponseDto>>> partitionedMap = result.parallelStream().collect(Collectors
				.partitioningBy(responseEntity -> responseEntity.getStatusCode().value() == Constants.SUCCESS_STATUS));
		result.removeAll(partitionedMap.get(true));
		logger.info(result.toString());
		if (result.size() > 0) {
			logger.error(Constants.INTERNAL_SERVER_ERROR + " - " + scheduleName);
			throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					scheduleName + " is not " + Constants.VALIDATED_SUCCESSFULLY);
		}
	}

	private void editTestRunInSchedule(ScheduleJobVO scheduleJobVO, Optional<List<UserSchedulerJob>> listOfSubJob) {
		// Edit schedule details and test run time and email notification
		if (listOfSubJob.isPresent()) {
			scheduleJobVO.getTestRuns().parallelStream().forEach(testRunVO -> {
				listOfSubJob.get().parallelStream().forEach(subScheduleJob -> {
					if (testRunVO.getTemplateTestRun().equalsIgnoreCase(subScheduleJob.getComments())) {
						TestSet testRun = testSetRepository.findByTestRunName(testRunVO.getTemplateTestRun());
						testRun.setCopyIncreamentFlag(testRunVO.getAutoIncrement());
						testSetRepository.save(testRun);
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
							logger.info("result=" + result.toString());
						} catch (Exception e) {
							logger.error(e.getMessage());
							throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
									"Error occurred from APEX web service of edit scheduled job", e);
						}
					}
				});
			});
		}
	}

	private void addTestRunInSchedule(ScheduleJobVO scheduleJobVO, Optional<List<UserSchedulerJob>> listOfSubJob,
			Scheduler scheduler) {
		// add test runs in a schedule
		if ((scheduleJobVO.getTestRuns().size() > 0 && !listOfSubJob.isPresent())
				|| (scheduleJobVO.getTestRuns().size() > listOfSubJob.get().size())) {
			List<String> listOfSubJobFromDB = listOfSubJob.isPresent()
					? listOfSubJob.get().parallelStream().filter(Objects::nonNull).map(UserSchedulerJob::getComments)
							.collect(Collectors.toList())
					: new ArrayList<>();
			logger.info(String.format("list of schedule TestRuns from DB :"
					+ listOfSubJobFromDB.parallelStream().collect(Collectors.joining(","))));
			List<String> listOfSubJobFromVO = scheduleJobVO.getTestRuns().parallelStream().filter(Objects::nonNull)
					.map(ScheduleTestRunVO::getTemplateTestRun).collect(Collectors.toList());
			logger.info(String.format("list of schedule TestRuns from edit :"
					+ listOfSubJobFromVO.parallelStream().collect(Collectors.joining(","))));
			List<String> newAddedSubJobNames = listOfSubJobFromVO.parallelStream().filter(Objects::nonNull)
					.filter(subJobName -> !listOfSubJobFromDB.contains(subJobName)).collect(Collectors.toList());
			logger.info(String.format("list of newly added schedule TestRuns from edit :"
					+ newAddedSubJobNames.parallelStream().collect(Collectors.joining(","))));
			List<ScheduleTestRunVO> newAddedSubJobs = scheduleJobVO.getTestRuns().parallelStream()
					.filter(Objects::nonNull)
					.filter(subJob -> newAddedSubJobNames.contains(subJob.getTemplateTestRun()))
					.collect(Collectors.toList());
			logger.info("newAddedSubJobs=" + newAddedSubJobs.toString());
			if (newAddedSubJobs.size() > 0) {
				AtomicInteger count = new AtomicInteger(listOfSubJob.isPresent() ? listOfSubJob.get().size() : 0);
				createSchedule(scheduleJobVO, newAddedSubJobs, count, scheduler, true);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void deleteTestRunFromSchedule(ScheduleJobVO scheduleJobVO, Optional<List<UserSchedulerJob>> listOfSubJob) {
		// delete test runs from a schedule
		if (listOfSubJob.isPresent()) {
			// get list of all test run name from request body(JSON VO)
			List<String> listOfAllSubJobFromVO = scheduleJobVO.getTestRuns().parallelStream().filter(Objects::nonNull)
					.map(ScheduleTestRunVO::getTemplateTestRun).collect(Collectors.toList());
			logger.info("listOfAllSubJobFromVO=" + listOfAllSubJobFromVO.toString());
			// get list of test run name which are present in database(Except newly added
			// test runs) from request body(JSON VO)
			List<String> listOfExistingSubJobInDBFromVO = listOfSubJob.get().parallelStream().filter(Objects::nonNull)
					.filter(subScheduleJob -> listOfAllSubJobFromVO.contains(subScheduleJob.getComments()))
					.map(UserSchedulerJob::getComments).collect(Collectors.toList());
			logger.info("listOfExistingSubJobInDBFromVO=" + listOfExistingSubJobInDBFromVO.toString());
			// delete test run from APEX RestAPI and return the list of deleted test runs
			List<UserSchedulerJob> listOfDeletedSubJob = listOfSubJob.get().parallelStream().filter(Objects::nonNull)
					.filter(subScheduleJob -> {
						if (!listOfExistingSubJobInDBFromVO.contains(subScheduleJob.getComments())) {
							ScheduleSubJobVO scheduleSubJobVO = new ScheduleSubJobVO();
							logger.info("scheduleSubJobVO=" + scheduleSubJobVO.toString());
							scheduleSubJobVO.setSubJobName(subScheduleJob.getJobName());
							try {
								WebClient webClient = WebClient.create(basePath + Constants.FORWARD_SLASH
										+ Constants.WATS_SERVICE + Constants.FORWARD_SLASH
										+ Constants.DELETE_SCHEDULE_TEST_RUN_ENDPOINT);
								String result = webClient.post().syncBody(scheduleSubJobVO).retrieve()
										.bodyToMono(String.class).block();
								if (result != null) {
									logger.info("result=" + result.toString());
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
			logger.info("listOfSubJob=" + listOfSubJob.toString());
		}
	}
}
