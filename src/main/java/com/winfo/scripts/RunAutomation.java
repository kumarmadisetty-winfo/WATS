package com.winfo.scripts;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.lowagie.text.DocumentException;
import com.winfo.Factory.SeleniumKeywordsFactory;
import com.winfo.config.DriverConfiguration;
import com.winfo.dao.CodeLinesRepository;
import com.winfo.dao.DataBaseEntryDao;
import com.winfo.dao.PyJabActionRepo;
import com.winfo.exception.WatsEBSException;
import com.winfo.model.AuditScriptExecTrail;
import com.winfo.model.Scheduler;
import com.winfo.model.TestSet;
import com.winfo.model.UserSchedulerJob;
import com.winfo.repository.SchedulerRepository;
import com.winfo.repository.TestSetLinesRepository;
import com.winfo.repository.TestSetRepository;
import com.winfo.repository.UserSchedulerJobRepository;
import com.winfo.service.SFInterface;
import com.winfo.service.WoodInterface;
import com.winfo.serviceImpl.DataBaseEntry;
import com.winfo.serviceImpl.ErrorMessagesHandler;
import com.winfo.serviceImpl.GraphQLService;
import com.winfo.serviceImpl.JiraTicketBugService;
import com.winfo.serviceImpl.LimitScriptExecutionService;
import com.winfo.serviceImpl.ScriptXpathService;
import com.winfo.serviceImpl.SendMailServiceImpl;
import com.winfo.serviceImpl.SmartBearService;
import com.winfo.serviceImpl.TestCaseDataService;
import com.winfo.serviceImpl.TestScriptExecService;
import com.winfo.utils.Constants;
import com.winfo.utils.Constants.AUDIT_TRAIL_STAGES;
import com.winfo.utils.Constants.BOOLEAN_STATUS;
import com.winfo.utils.FileUtil;
import com.winfo.vo.ApiValidationVO;
import com.winfo.vo.CustomerProjectDto;
import com.winfo.vo.FetchConfigVO;
import com.winfo.vo.FetchMetadataVO;
import com.winfo.vo.FetchScriptVO;
import com.winfo.vo.ResponseDto;
import com.winfo.vo.ScriptDetailsDto;
import com.winfo.vo.Status;
import com.winfo.vo.TestScriptDto;

@Service

public class RunAutomation {
	
	public static final Logger logger = Logger.getLogger(RunAutomation.class);
	
	@Autowired
	SeleniumKeywordsFactory seleniumFactory;
	@Autowired
	ErrorMessagesHandler errorMessagesHandler;
	@Autowired
	DriverConfiguration driverConfiguration;
	@Autowired
	JiraTicketBugService jiraTicketBugService;
	@Value("${configvO.watsvediologo}")
	private String watsvediologo;

	@Value("${configvO.whiteimage}")
	private String whiteimage;

	@Autowired
	TestCaseDataService dataService;
	@Autowired
	DataBaseEntry dataBaseEntry;
	public String c_url = null;

	@Autowired
	LimitScriptExecutionService limitScriptExecutionService;
	@Autowired
	TestScriptExecService testScriptExecService;
	@Autowired
	XpathPerformance xpathPerformance;
	@Autowired
	ScriptXpathService xpathService;
	@Autowired
	PyJabActionRepo actionRepo;
	@Autowired
	CodeLinesRepository codeLineRepo;
	@Autowired
	GraphQLService graphQLService;
	@Autowired
	SmartBearService smartBearService;
	@Autowired
	UserSchedulerJobRepository userSchedulerJobRepository;
	@Autowired
	SendMailServiceImpl sendMailServiceImpl;
	@Autowired
	TestSetRepository testSetRepository;
	@Autowired
	TestSetLinesRepository testSetLinesRepository;
	@Autowired
	DataBaseEntryDao dao;
	@Autowired
	SchedulerRepository schedulerRepository;


	@Autowired
	WoodInterface woodInterface;
	
	@Autowired
	SFInterface sfInterface;

	public void report() throws IOException, DocumentException, com.itextpdf.text.DocumentException {

		FetchMetadataVO fetchMetadataVO = new FetchMetadataVO();
		List<FetchMetadataVO> fetchMetadataListVO = new ArrayList<FetchMetadataVO>();
		fetchMetadataListVO.add(fetchMetadataVO);

//		seleniumFactory.getInstanceObj("UDG").createFailedPdf(fetchMetadataListVO, fetchConfigVO, "14_OTC.AR.224.pdf");
//
		// createFailedPdf(fetchMetadataListVO, fetchConfigVO, "Failed_Report.pdf");
//		createFailedPdf(fetchMetadataListVO, fetchConfigVO, "14_OTC.AR.224.pdf");
//
		// createFailedPdf(fetchMetadataListVO, fetchConfigVO,fetchMetadataVO,
		// "Detailed_Report.pdf");
//		createPdf(fetchMetadataListVO, fetchConfigVO, "Passed_Report.pdf",passcount,failcount,null);
//		createPdf(fetchMetadataListVO, fetchConfigVO, "1_10_Create Manual Invoice Transaction_OTC.AR.203.pdf",passcount,failcount);
//		createPdf(fetchMetadataListVO, fetchConfigVO,"Failed_Report.pdf",passcount,failcount);
//		createPdf(fetchMetadataListVO, fetchConfigVO, "55_OTC.AR.218.pdf",passcount,failcount);

	}

	long increment = 0;

	@Async
	public ResponseDto run(TestScriptDto testScriptDto) throws MalformedURLException {
		logger.info("Test Run Id : " + testScriptDto.getTestScriptNo());

		ResponseDto executeTestrunVo;
		String checkPackage = dataBaseEntry.getPackage(testScriptDto.getTestScriptNo());
		if (checkPackage != null && checkPackage.toLowerCase().contains(Constants.EBS)) {
			executeTestrunVo = ebsRun(testScriptDto.getTestScriptNo());
		} else {
			executeTestrunVo = cloudRun(testScriptDto);
		}
		logger.info("End of Test Script Run : " + testScriptDto.getTestScriptNo());

		return executeTestrunVo;
	}

	public ResponseDto ebsRun(String testSetId) throws MalformedURLException {
		ResponseDto executeTestrunVo = new ResponseDto();
		try {
			dataBaseEntry.updatePdfGenerationEnableStatus(testSetId, BOOLEAN_STATUS.TRUE.getLabel());
			FetchConfigVO fetchConfigVO = testScriptExecService.fetchConfigVO(testSetId);
			CustomerProjectDto customerDetails = dataBaseEntry.getCustomerDetails(testSetId);
			List<ScriptDetailsDto> testLinesDetails = dataBaseEntry.getScriptDetailsListVO(testSetId, null, false,
					true);
//			List<FetchMetadataVO> fetchMetadataListVO = dataBaseEntry.getMetaDataVOList(testSetId, null, false, true);

			SortedMap<Integer, List<ScriptDetailsDto>> dependentScriptMap = new TreeMap<Integer, List<ScriptDetailsDto>>();
			SortedMap<Integer, List<ScriptDetailsDto>> metaDataMap = dataService.prepareTestcasedata(testLinesDetails,
					dependentScriptMap);
			// Independent
			for (Entry<Integer, List<ScriptDetailsDto>> metaData : metaDataMap.entrySet()) {
				logger.info("Running Independent - " + metaData.getKey());
				testScriptExecService.executorMethodPyJab(testSetId, fetchConfigVO, metaData, true, customerDetails);
			}

			ExecutorService executordependent = Executors.newFixedThreadPool(fetchConfigVO.getPARALLEL_DEPENDENT());
			for (Entry<Integer, List<ScriptDetailsDto>> metaData : dependentScriptMap.entrySet()) {
				logger.info("Running Dependent - " + metaData.getKey());
				executordependent.execute(() -> {
					logger.info("Running Dependent in executor - " + metaData.getKey());
					boolean run = dataBaseEntry.checkRunStatusOfTestRunLevelDependantScript(
							metaData.getValue().get(0).getDependencyScriptNumber());
					logger.info("Dependant Script run status " + metaData.getValue().get(0).getScriptId() + " " + run);
					testScriptExecService.executorMethodPyJab(testSetId, fetchConfigVO, metaData, run, customerDetails);
				});
			}
			executordependent.shutdown();

			executeTestrunVo.setStatusCode(200);
			executeTestrunVo.setStatusMessage("SUCCESS");
			executeTestrunVo.setStatusDescr("SUCCESS");
		} catch (Exception e) {
			dataBaseEntry.updateExecStatusIfTestRunIsCompleted(testSetId);
			if (e instanceof WatsEBSException)
				throw e;
			throw new WatsEBSException(500, "Exception Occured while creating script for Test Run", e);
		}
		return executeTestrunVo;
	}

	public ResponseDto cloudRun(TestScriptDto testScriptDto) throws MalformedURLException {
		ResponseDto executeTestrunVo = new ResponseDto();
		String testSetId = testScriptDto.getTestScriptNo();
		try {
			FetchConfigVO fetchConfigVO = testScriptExecService.fetchConfigVO(testSetId);
//			List<FetchMetadataVO> fetchMetadataListVO = dataBaseEntry.getMetaDataVOList(testSetId, null, false, true);
			CustomerProjectDto customerDetails = dataBaseEntry.getCustomerDetails(testSetId);
			logger.info(String.format("Customer Id : %s, Customer Name : %s, Project Name : %s  " , customerDetails.getCustomerId(), customerDetails.getCustomerName(), customerDetails.getProjectName()));
			logger.debug(String.format("Management Tool Enabled value : %s ", fetchConfigVO.getMANAGEMENT_TOOL_ENABLED()));
			if("YES".equalsIgnoreCase(fetchConfigVO.getMANAGEMENT_TOOL_ENABLED())){
				String key = graphQLService.createTestRunInJiraXrayCloud(customerDetails);
				fetchConfigVO.setTestRunIssueId(key);
			}
			List<ScriptDetailsDto> testLinesDetails = dataBaseEntry.getScriptDetailsListVO(testSetId, null, false,
					true);

			SortedMap<Integer, List<ScriptDetailsDto>> dependentScriptMap = new TreeMap<Integer, List<ScriptDetailsDto>>();
			SortedMap<Integer, List<ScriptDetailsDto>> metaDataMap = dataService.prepareTestcasedata(testLinesDetails,
					dependentScriptMap);
			Map<Integer, Status> scriptStatus = new HashMap<>();

			Date date = new Date();
			fetchConfigVO.setStarttime1(date);
			ExecutorService executor = Executors.newFixedThreadPool(fetchConfigVO.getPARALLEL_INDEPENDENT());
			try {
				for (Entry<Integer, List<ScriptDetailsDto>> metaData : metaDataMap.entrySet()) {

					String scriptNumber = metaData.getValue().get(0).getScriptNumber();

					executor.execute(() -> {
						logger.info("Start of Independent Script Execution of " + scriptNumber);
						try {
							long starttimeIntermediate = System.currentTimeMillis();
							String flag = dataBaseEntry.getTrMode(testSetId, fetchConfigVO);
							if (flag.equalsIgnoreCase("STOPPED")) {
								metaData.getValue().clear();
								executor.shutdown();
								logger.info("Test run is STOPPED - Scripts will only run when Test Run status is ACTIVE");
							} else {
								executorMethod(testSetId, fetchConfigVO, testLinesDetails, metaData, scriptStatus,
										customerDetails);
							}
							long i = System.currentTimeMillis() - starttimeIntermediate;
							increment = increment + i;
							logger.info("Time  " + increment / 1000 % 60);
						} catch (Exception e) {
							logger.info("Exception occurred while executing Independent Script of " + scriptNumber);
							e.printStackTrace();
						}
						logger.info("End of Independent Script Execution of "  + scriptNumber);
					});
				}
				executor.shutdown();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
				if (executor.isTerminated() && dependentScriptMap.size() > 0) {
					ExecutorService executordependent = Executors
							.newFixedThreadPool(fetchConfigVO.getPARALLEL_DEPENDENT());

					for (Entry<Integer, List<ScriptDetailsDto>> metaData : dependentScriptMap.entrySet()) {
						logger.info("Running Dependent - " + metaData.getKey());
						executordependent.execute(() -> {
							logger.info("Running Dependent in executor - " + metaData.getKey());
							boolean run;
							run = dataBaseEntry.checkRunStatusOfTestRunLevelDependantScript(
									metaData.getValue().get(0).getDependencyScriptNumber());
							logger.info("Dependant Script run status " + metaData.getValue().get(0).getScriptId() + " "
									+ run);

							try {
								String flag = dataBaseEntry.getTrMode(testSetId, fetchConfigVO);
								if (flag.equalsIgnoreCase("STOPPED")) {
									metaData.getValue().clear();
									executor.shutdown();
									logger.info("Script Termination is Succeed");
								} else {
									if (run) {
										executorMethod(testSetId, fetchConfigVO, testLinesDetails, metaData,
												scriptStatus, customerDetails);
									} else {
										String passurl = fetchConfigVO.getIMG_URL() + customerDetails.getCustomerName()
												+ "/" + customerDetails.getProjectName() + "/"
												+ customerDetails.getTestSetName() + "/" + "Passed_Report.pdf";
										String failurl = fetchConfigVO.getIMG_URL() + customerDetails.getCustomerName()
												+ "/" + customerDetails.getProjectName() + "/"
												+ customerDetails.getTestSetName() + "/" + "Failed_Report.pdf";
										String detailurl = fetchConfigVO.getIMG_URL()
												+ customerDetails.getCustomerName() + "/"
												+ customerDetails.getProjectName() + "/"
												+ customerDetails.getTestSetName() + "/" + "Detailed_Report.pdf";
										String scripturl = fetchConfigVO.getIMG_URL()
												+ customerDetails.getCustomerName() + "/"
												+ customerDetails.getProjectName() + "/"
												+ customerDetails.getTestSetName() + "/"
												+ testLinesDetails.get(0).getSeqNum() + "_"
												+ testLinesDetails.get(0).getScriptNumber() + ".pdf";

										ScriptDetailsDto fd = metaData.getValue().get(0);
										FetchScriptVO post = new FetchScriptVO();
										post.setP_test_set_id(customerDetails.getTestSetId());
										post.setP_status("Fail");
										post.setP_script_id(fd.getScriptId());
										post.setP_test_set_line_id(fd.getTestSetLineId());
										post.setP_pass_path(passurl);
										post.setP_fail_path(failurl);
										post.setP_exception_path(detailurl);
										post.setP_test_set_line_path(scripturl);
										failcount = failcount + 1;
										logger.info("Checking fail count : " + failcount);

//										dataService.updateTestCaseStatus(post, testSetId, fetchConfigVO);

										dataBaseEntry.updateTestCaseEndDate(post, fetchConfigVO.getEndtime(),
												post.getP_status());
										dataBaseEntry.updateTestCaseStatus(post, fetchConfigVO, testLinesDetails,
												fetchConfigVO.getStarttime(), customerDetails.getTestSetName(),true);

										// dataBaseEntry.updateEndTime(fetchConfigVO,fd.getTest_set_line_id(),fd.getTest_set_id(),
										// enddate);
										int failedScriptRunCount = limitScriptExecutionService.getFailedScriptRunCount(
												fd.getTestSetLineId(), customerDetails.getTestSetId());

										errorMessagesHandler.getError("Dependency Fail", fd, fetchConfigVO,
												fd.getTestScriptParamId(), null, null, null, null);

									}
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();

							}
						});
					}

					executordependent.shutdown();
					executordependent.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

				}
				downloadScreenShot(fetchConfigVO, testLinesDetails.get(0), customerDetails, true);
				List<ScriptDetailsDto> fetchMetadataListVOforEvidence = dataBaseEntry.getScriptDetailsListVO(testSetId,
						null, true, false);

				dataBaseEntry.updateStatusOfPdfGeneration(testSetId,Constants.GENERATING);
				CompletableFuture<String> completableFuture1 = seleniumFactory.getInstanceObjFromAbstractClass(fetchConfigVO.getINSTANCE_NAME())
						.createPdf(fetchMetadataListVOforEvidence, fetchConfigVO, "Passed_Report.pdf", customerDetails);	
				CompletableFuture<String> completableFuture2 = seleniumFactory.getInstanceObjFromAbstractClass(fetchConfigVO.getINSTANCE_NAME())
						.createPdf(fetchMetadataListVOforEvidence, fetchConfigVO, "Failed_Report.pdf", customerDetails);
				CompletableFuture<String> completableFuture3 = seleniumFactory.getInstanceObjFromAbstractClass(fetchConfigVO.getINSTANCE_NAME()).createPdf(
						fetchMetadataListVOforEvidence, fetchConfigVO, "Detailed_Report.pdf", customerDetails);
				List<CompletableFuture<String>> completableFutures = Arrays.asList(completableFuture1, completableFuture2, completableFuture3);
				CompletableFuture<Void> resultantCf = CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[completableFutures.size()]));
				CompletableFuture<List<String>> allFutureResults = resultantCf.thenApply(t ->{
					FileUtil.deleteScreenshotAndPdfDirectoryFromTemp(fetchConfigVO, customerDetails);
					dataBaseEntry.updateStatusOfPdfGeneration(testSetId,Constants.PASSED);
					return completableFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
				}).exceptionally((e)->{
					dataBaseEntry.updateStatusOfPdfGeneration(testSetId,Constants.PASSED);
					logger.info("Exception occurred while generating PDFs");
					return null;
				});
				logger.info("Successfully created PDFs - " + allFutureResults.get());
				
				dataBaseEntry.updateStartAndEndTimeForTestSetTable(customerDetails.getTestSetId(), fetchConfigVO.getStarttime(), fetchConfigVO.getEndtime());
				if(testScriptDto.getJobId()!=null) {
					dataBaseEntry.testRunsNotificationEmail(customerDetails.getTestSetName(),testLinesDetails,testScriptDto.getJobId(),customerDetails.getTestSetId());
					//String FORMAT = "dd-MMM-yyyy HH:mm:ss.SSS";
					LocalDateTime localDate = LocalDateTime.now(ZoneId.of("GMT+05:30"));
					userSchedulerJobRepository.updateEndDateInUserSchedulerJob(localDate,customerDetails.getTestSetName(),testScriptDto.getJobId());
				}
				
				if ("SHAREPOINT".equalsIgnoreCase(fetchConfigVO.getPDF_LOCATION())) {
					seleniumFactory.getInstanceObj(fetchConfigVO.getINSTANCE_NAME())
							.uploadPdfToSharepoint(fetchMetadataListVOforEvidence, fetchConfigVO, customerDetails);
				}
				// check dependency and return test run id, if any dependency then call cloudRun method
				if(testScriptDto.getJobId()!=null) {
					int isTestRunPassed=testSetLinesRepository.checkIsTestRunPassed(testScriptDto.getTestScriptNo());
					if(isTestRunPassed==0){
						Optional<UserSchedulerJob> dependencyTestRun = userSchedulerJobRepository
								.findByJobIdAndDependency(testScriptDto.getJobId(), Integer.parseInt(testScriptDto.getTestScriptNo()));
						if (dependencyTestRun.isPresent() && StringUtils.isNotBlank(dependencyTestRun.get().getComments())) {
							TestScriptDto dependencyTestScriptDto = new TestScriptDto();
							dependencyTestScriptDto.setJobId(testScriptDto.getJobId());
							dependencyTestScriptDto
							.setTestScriptNo(String.valueOf(testSetRepository.findByTestRunName(dependencyTestRun.get().getComments()).getTestRunId()));
							cloudRun(dependencyTestScriptDto);
						}						
					}
				}
				
				// send scheduler level notification email if jobId is present
				if(testScriptDto.getJobId()!=null) {
					Optional<List<UserSchedulerJob>> listOfUserSchedulerJob = userSchedulerJobRepository
							.findByJobId(testScriptDto.getJobId());
					// check the size of the user scheduler job list
					//if (listOfUserSchedulerJob.isPresent() && listOfUserSchedulerJob.get().size() > 0) {
						List<UserSchedulerJob> listOfUserSchedulerJobWithEndDate = listOfUserSchedulerJob.get().stream()
								.filter(Objects::nonNull)
								.map(job -> {
									if (job.getEndDate() != null) {
										return job;
									}
									return null;
								}).filter(Objects::nonNull).collect(Collectors.toList());

						if (listOfUserSchedulerJob.isPresent()) {
							if (listOfUserSchedulerJobWithEndDate.size() == listOfUserSchedulerJob.get().size()) {
								// send notifications to users
								List<TestSet> testSetIds = userSchedulerJobRepository
										.findByTestRuns(testScriptDto.getJobId());

								List<Integer> testRunIds = testSetIds.stream().map(testSet -> testSet.getTestRunId())
										.collect(Collectors.toList());
								String listTestSetIds = testRunIds.stream().map(String::valueOf)
										.collect(Collectors.joining(","));
								String testRunNames = testSetIds.stream().map(testSet -> testSet.getTestRunName())
										.collect(Collectors.joining(","));

								Scheduler scheduler = schedulerRepository.findByJobId(testScriptDto.getJobId());
								dataBaseEntry.schedulerNotificationEmail(scheduler.getJobName(), testLinesDetails, listTestSetIds,
										testScriptDto.getJobId(), testRunNames);
							}
						}
				//	}
				}
				
				increment = 0;

				
				executeTestrunVo.setStatusCode(200);
				executeTestrunVo.setStatusMessage("SUCCESS");
				executeTestrunVo.setStatusDescr("SUCCESS");
			} catch (InterruptedException e) {
				e.printStackTrace();
				logger.error("Exception in dependent block of code");
				Thread.currentThread().interrupt();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error in Cloud test run method " + e.getMessage());
			dataBaseEntry.updateEnabledStatusForTestSetLine(testSetId, "Y");
		}
		return executeTestrunVo;
	}
	


	public void executorMethod(String testRunId, FetchConfigVO fetchConfigVO, List<ScriptDetailsDto> testLinesDetails,
			Entry<Integer, List<ScriptDetailsDto>> metaData, Map<Integer, Status> scriptStatus,
			CustomerProjectDto customerDetails) throws Exception {
		List<String> failList = new ArrayList<>();
		WebDriver driver = null;
		List<ScriptDetailsDto> fetchMetadataListsVO = metaData.getValue();
		String testSetId = customerDetails.getTestSetId();
		String testSetLineId = fetchMetadataListsVO.get(0).getTestSetLineId();

		String scriptId = fetchMetadataListsVO.get(0).getScriptId();
		String passUrl = fetchConfigVO.getIMG_URL() + customerDetails.getCustomerName() + "/"
				+ customerDetails.getProjectName() + "/" + customerDetails.getTestSetName() + "/" + "Passed_Report.pdf";
		String failUrl = fetchConfigVO.getIMG_URL() + customerDetails.getCustomerName() + "/"
				+ customerDetails.getProjectName() + "/" + customerDetails.getTestSetName() + "/" + "Failed_Report.pdf";
		String detailUrl = fetchConfigVO.getIMG_URL() + customerDetails.getCustomerName() + "/"
				+ customerDetails.getProjectName() + "/" + customerDetails.getTestSetName() + "/"
				+ "Detailed_Report.pdf";
		String scriptUrl = fetchConfigVO.getIMG_URL() + customerDetails.getCustomerName() + "/"
				+ customerDetails.getProjectName() + "/" + customerDetails.getTestSetName() + "/"
				+ fetchMetadataListsVO.get(0).getSeqNum() + "_" + fetchMetadataListsVO.get(0).getScriptNumber()
				+ ".pdf";
		logger.info(String.format("Pass Url : %s , Fail Url : %s , Detailed Url : %s , Script Url : %s " , passUrl, failUrl, detailUrl, scriptUrl));
		boolean isDriverError = true;
		AuditScriptExecTrail auditTrial = dataBaseEntry.insertScriptExecAuditRecord(AuditScriptExecTrail.builder()
				.testSetLineId(Integer.valueOf(testSetLineId)).triggeredBy(fetchMetadataListsVO.get(0).getExecutedBy())
				.correlationId(UUID.randomUUID().toString()).build(), AUDIT_TRAIL_STAGES.RR, null);
		try {
			boolean actionContainsExcel = dataBaseEntry.doesActionContainsExcel(fetchMetadataListsVO.get(0).getScriptId());
			String operatingSystem = actionContainsExcel ? "windows" : null;
			driver = driverConfiguration.getWebDriver(fetchConfigVO, operatingSystem);
			isDriverError = false;
			switchActions(testRunId, driver, fetchMetadataListsVO, fetchConfigVO, scriptStatus, customerDetails,auditTrial, scriptId, passUrl, failUrl, detailUrl, scriptUrl);

		}
		catch (WebDriverException e) {
			if(driver == null)
			{
					String enableStatus=dataBaseEntry.getEnabledStatusByTestSetLineID(testSetLineId);	
					dataBaseEntry.updateEnabledStatusForTestSetLine(testSetId,enableStatus);
			}
		}
		catch (Exception e) {
			logger.info("Exception occured while running script " + fetchMetadataListsVO.get(0).getScriptNumber());
			e.printStackTrace();
			if (isDriverError) {
				FetchScriptVO post = new FetchScriptVO();
				post.setP_test_set_id(testSetId);
				post.setP_status("Fail");
				post.setP_script_id(scriptId);
				post.setP_test_set_line_id(testSetLineId);
				post.setP_pass_path(passUrl);
				post.setP_fail_path(failUrl);
				post.setP_exception_path(detailUrl);
				post.setP_test_set_line_path(scriptUrl);

//				dataService.updateTestCaseStatus(post, testSetId, fetchConfigVO);
				dataBaseEntry.insertScriptExecAuditRecord(auditTrial, AUDIT_TRAIL_STAGES.DF, e.getMessage());
				dataBaseEntry.updateTestCaseEndDate(post, fetchConfigVO.getEndtime(), post.getP_status());
				dataBaseEntry.updateTestCaseStatus(post, fetchConfigVO, testLinesDetails, fetchConfigVO.getStarttime(),
						customerDetails.getTestSetName(),false);

				failList.add(scriptId);
			}
		} finally {
			dataBaseEntry.updateEnableFlagForSanity(testSetId);
			logger.info("Execution is completed for script " + fetchMetadataListsVO.get(0).getScriptNumber());
			if (driver != null) {
				driver.close();
				driver.quit();
				driver=null;
			}
		}
	}

	int passcount = 0;
	int failcount = 0;

	public void switchActions(String param, WebDriver driver, List<ScriptDetailsDto> fetchMetadataListVO,
			FetchConfigVO fetchConfigVO, Map<Integer, Status> scriptStatus, CustomerProjectDto customerDetails,AuditScriptExecTrail auditTrial, String scriptId, String passurl, String failurl, String detailurl, String scripturl)
			throws Exception {

		String log4jConfPath = "log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);

		// XpathPerformance code for cases added
		long startTime = System.currentTimeMillis();
		int i = 0;
		String actionName = null;
		String testSetId = null;
		String testSetLineId = null;
		String scriptId1 = null;
		String scriptNumber = null;
		String seqNum = null;
		String testScriptParamId = null;
		boolean startExcelAction = false;
		boolean isError = false;
		List<ScriptDetailsDto> excelMetadataListVO = new ArrayList<>();
		dataBaseEntry.insertScriptExecAuditRecord(auditTrial, AUDIT_TRAIL_STAGES.SES, null);
		
		try {
			String userName = null;
			String globalValueForSteps = null;
			Date startdate = new Date();
			fetchConfigVO.setStarttime(startdate);
			String instanceName = fetchConfigVO.getINSTANCE_NAME();
			logger.info("Instance Name " + instanceName);
			seleniumFactory.getInstanceObjFromAbstractClass(instanceName)
					.deleteOldScreenshotForScriptFrmObjStore(fetchMetadataListVO.get(0), customerDetails);

			// XpathPerformance code for cases added
			String scriptID = fetchMetadataListVO.get(0).getScriptId();
			String checkValidScript = "Yes";
			logger.info("Valid script check " + checkValidScript);
			Boolean validationFlag = null;
			Map<String, String> accessTokenStorage = new HashMap<>();
			ApiValidationVO api = new ApiValidationVO();
			String scriptIssueId = null;
			if("YES".equalsIgnoreCase(fetchConfigVO.getMANAGEMENT_TOOL_ENABLED())){
				scriptIssueId = graphQLService.createScriptInJiraXrayCloud(fetchMetadataListVO);
//				fetchConfigVO.setScriptIssueId(key);
				graphQLService.associateScriptToTestRun(fetchConfigVO,scriptIssueId);
			}
			
			for (ScriptDetailsDto fetchMetadataVO : fetchMetadataListVO) {

				actionName = fetchMetadataVO.getAction();
				testSetId = customerDetails.getTestSetId();
				testSetLineId = fetchMetadataVO.getTestSetLineId();
				scriptId1 = fetchMetadataVO.getScriptId();
				scriptNumber = fetchMetadataVO.getScriptNumber();
				seqNum = fetchMetadataVO.getSeqNum();
				String screenParameter = fetchMetadataVO.getInputParameter();
				testScriptParamId = fetchMetadataVO.getTestScriptParamId();
				logger.debug(String.format("actionName: %s, testSetId : %s, testSetLineId : %s , scriptId : %s , scriptNumber : %s, seqNum : %s, screenParameter : %s , testScriptParamId : %s" , actionName, testSetId, testSetLineId, scriptId1 , scriptNumber, seqNum , screenParameter, testScriptParamId));
				if (i == 0) {
					dataBaseEntry.updateInProgressScriptStatus(testSetLineId, startdate);
				}

				String param1 = null;
				String param2 = null;
				String param3 = null;
				String type1 = null;
				String type2 = null;
				String type3 = null;
				String message = null;
				String value1 = null;
				String value2 = null;
				int count = 0;
				if (screenParameter != null) {
					param1 = screenParameter.split(">").length > 0 ? screenParameter.split(">")[0] : "";
					param2 = screenParameter.split(">").length > 1 ? screenParameter.split(">")[1] : "";
					param3 = screenParameter.split(">").length > 2 ? screenParameter.split(">")[2] : "";
					String actionType = fetchMetadataVO.getFieldType();
					type1 = actionType != null ? actionType.split(">").length > 0 ? actionType.split(">")[0] : "" : "";
					type2 = actionType != null ? actionType.split(">").length > 1 ? actionType.split(">")[1] : "" : "";
					type3 = actionType != null ? actionType.split(">").length > 2 ? actionType.split(">")[2] : "" : "";
					String getValue = fetchMetadataVO.getInputValue();
					value1 = getValue != null ? getValue.split(">").length > 0 ? getValue.split(">")[0] : "" : "";
					value2 = getValue != null ? getValue.split(">").length > 1 ? getValue.split(">")[1] : "" : "";

				}
				try {
					i++;
					if (startExcelAction
							&& (!actionName.toLowerCase().contains("excel") || i == fetchMetadataListVO.size())) {
						logger.info("Start of Excel Operation");
						// run excel code
						if (actionName.toLowerCase().contains("excel")) {
							excelMetadataListVO.add(fetchMetadataVO);
						}
						startExcelAction = false;
						testScriptExecService.runExcelSteps(param, excelMetadataListVO, fetchConfigVO, true,
								customerDetails,auditTrial);
						logger.info("Size of Excel MetadataList " + excelMetadataListVO.size());
						List<Integer> stepIdList = excelMetadataListVO.stream().map(e -> e.getTestScriptParamId())
								.map(Integer::valueOf).collect(Collectors.toList());
						boolean stepPassed = dataBaseEntry.checkScriptStatusForSteps(stepIdList);
						if (!stepPassed) {
							logger.info("Excel Actions failed");
							isError = true;
						}
					}
					if (actionName.toLowerCase().contains("excel")) {
						startExcelAction = true;
						logger.info("Adding record to excel list");
						excelMetadataListVO.add(fetchMetadataVO);
					} else if (!isError) {
						dataBaseEntry.updateInProgressScriptLineStatus(testScriptParamId, "In-Progress");
						switch (actionName) {

						case "Login into Application":
							userName = fetchMetadataVO.getInputValue();
							logger.info("Navigating to Login into Application Action");
							if (fetchMetadataVO.getInputValue() != null || "".equals(fetchMetadataVO.getInputValue())) {
								try {
									if ("Yes".equalsIgnoreCase(checkValidScript)) {
										xpathPerformance.loginApplication(driver, fetchConfigVO,
												fetchMetadataVO, type1, type2, type3, param1, param2, param3,
												fetchMetadataVO.getInputValue(),
												dataBaseEntry.getPassword(param, userName, fetchConfigVO), customerDetails,count);
										break;
									} else {
										throw new Exception("ScriptNotValid");
									}
								} catch (Exception e) {
									seleniumFactory.getInstanceObj(instanceName).loginApplication(driver, fetchConfigVO,
											fetchMetadataVO, type1, type2, type3, param1, param2, param3,
											fetchMetadataVO.getInputValue(),
											dataBaseEntry.getPassword(param, userName, fetchConfigVO), customerDetails);
									userName = null;
									break;
								}
							} else {
								seleniumFactory.getInstanceObj(instanceName).fullPageFailedScreenshot(driver, fetchMetadataVO,
										customerDetails);
								logger.error("Failed during " + instanceName + " login because input value is null");
								throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), 
										"Failed during " + instanceName + " login because input value is null");
							}
						case "Login into SFApplication":
							userName = fetchMetadataVO.getInputValue();
							logger.info("Navigating to Login into SFApplication Action");
							if (fetchMetadataVO.getInputValue() != null || fetchMetadataVO.getInputValue() == "") {
								seleniumFactory.getInstanceObj(instanceName).loginSFApplication(driver, fetchConfigVO,
										fetchMetadataVO, type1, type2, type3, param1, param2, param3,
										fetchMetadataVO.getInputValue(),
										dataBaseEntry.getPassword(param, userName, fetchConfigVO),customerDetails);
								userName = null;
								break;
							} else {
								break;
							}
						case "Login into DLApplication":
							
                            sfInterface.loginDLApplication(driver, param1, param2, fetchMetadataVO, fetchConfigVO, customerDetails);
                            break;
							
						case "Login into SSOApplication":
							userName = fetchMetadataVO.getInputValue();
							logger.info("Navigating to Login into Application Action");
							if (fetchMetadataVO.getInputValue() != null || fetchMetadataVO.getInputValue() == "") {
								seleniumFactory.getInstanceObj(instanceName).loginSSOApplication(driver, fetchConfigVO,
										fetchMetadataVO, type1, type2, type3, param1, param2, param3,
										fetchMetadataVO.getInputValue(),
										dataBaseEntry.getPassword(param, userName, fetchConfigVO), customerDetails);
								userName = null;
								break;
							} else {
								break;
							}
						case "Login into Application(OIC)":
							userName = fetchMetadataVO.getInputValue();
							logger.info("Navigating to Login into (OIC)Application Action");
							if (fetchMetadataVO.getInputValue() != null || fetchMetadataVO.getInputValue() == "") {
								seleniumFactory.getInstanceObj(instanceName).loginOicApplication(driver, fetchConfigVO,
										fetchMetadataVO, type1, type2, type3, param1, param2, param3,
										fetchMetadataVO.getInputValue(),
										dataBaseEntry.getPassword(param, userName, fetchConfigVO), customerDetails);
								userName = null;
								break;
							} else {
								break;
							}

						case "Login into Application(jobscheduler)":
							userName = fetchMetadataVO.getInputValue();
							logger.info("Navigating to Login into Application Action");
							if (fetchMetadataVO.getInputValue() != null || fetchMetadataVO.getInputValue() == "") {
								seleniumFactory.getInstanceObj(instanceName).loginOicJob(driver, fetchConfigVO,
										fetchMetadataVO, type1, type2, type3, param1, param2, param3,
										fetchMetadataVO.getInputValue(),
										dataBaseEntry.getPassword(param, userName, fetchConfigVO), customerDetails);
								userName = null;
								break;
							} else {
								break;
							}

						case "Navigate":
								try {
									if ("Yes".equalsIgnoreCase(checkValidScript)) {
										String xpathlocation=null;
										int totalXpaths = 0;
										xpathPerformance.navigate(driver, fetchConfigVO,
												fetchMetadataVO, type1, type2, param1, param2, count, customerDetails,xpathlocation,totalXpaths);
										break;
									} else {
										throw new Exception("ScriptNotValid");
									}
								} catch (Exception e) {
									logger.info("Navigating to Navigate Action");
									seleniumFactory.getInstanceObj(instanceName).navigate(driver, fetchConfigVO,
											fetchMetadataVO, type1, type2, param1, param2, null, count, customerDetails);
									break;
								}
						case "Click Menu(OIC)":
							seleniumFactory.getInstanceObj(instanceName).oicClickMenu(driver, param1, param2,
									fetchMetadataVO, fetchConfigVO, customerDetails);
							break;
						case "Navigate(OIC)":
							logger.info("Navigating to Navigate Action");
							seleniumFactory.getInstanceObj(instanceName).oicNavigate(driver, fetchConfigVO,
									fetchMetadataVO, type1, type2, param1, param2, count, customerDetails);
							break;

						case "Logout(OIC)":
							seleniumFactory.getInstanceObj(instanceName).oicLogout(driver, fetchConfigVO,
									fetchMetadataVO, type1, type2, type3, param1, param2, param3, customerDetails);
							break;

						case "openTask":
							try {
								if ("Yes".equalsIgnoreCase(checkValidScript)) {
									String xpathlocation=null;
									xpathPerformance.openTask(driver, fetchConfigVO,
											fetchMetadataVO, type1, type2, param1, param2, count, customerDetails,xpathlocation);
									break;
								} else {
									throw new Exception("ScriptNotValid");
								}
							} catch (Exception e) {
								logger.info("Navigating to openTask Action");
								seleniumFactory.getInstanceObj(instanceName).openTask(driver, fetchConfigVO,
										fetchMetadataVO, type1, type2, param1, param2, count, customerDetails);
								break;
							}

						case "Logout":
							try {
								if ("Yes".equalsIgnoreCase(checkValidScript)) {
									String xpathlocation=null;
									int totalXpaths = 0;
									xpathPerformance.logout(driver, fetchConfigVO, fetchMetadataVO,
											type1, type2, type3, param1, param2, param3, customerDetails,count,xpathlocation,totalXpaths);
									break;
								} else {
									throw new Exception("ScriptNotValid");
								}
							} catch (Exception e) {
								seleniumFactory.getInstanceObj(instanceName).logout(driver, fetchConfigVO, fetchMetadataVO,
										type1, type2, type3, param1, param2, param3, customerDetails);
								break;
							}

						// XpathPerformance code for cases added

						case "SendKeys":
							if (fetchMetadataVO.getInputValue() != null || fetchMetadataVO.getInputValue() == "") {
								try {
									if ("Yes".equalsIgnoreCase(checkValidScript)) {

										xpathPerformance.sendValue(driver, param1, param2,
												fetchMetadataVO.getInputValue(), fetchMetadataVO, fetchConfigVO, count,
												customerDetails);
										break;
									} else {
										throw new Exception("ScriptNotValid");
									}
								} catch (Exception e) {
									seleniumFactory.getInstanceObj(instanceName).sendValue(driver, param1, param2,
											fetchMetadataVO.getInputValue(), fetchMetadataVO, fetchConfigVO,
											customerDetails);
									break;
								}

							}else {
								break;
							}

						case "sendvalues(OIC)":
							if (fetchMetadataVO.getInputValue() != null || fetchMetadataVO.getInputValue() == "") {
								seleniumFactory.getInstanceObj(instanceName).oicSendValue(driver, param1, param2,
										fetchMetadataVO.getInputValue(), fetchMetadataVO, fetchConfigVO,
										customerDetails);
								break;
							} else {
								break;
							}

						case "clickExpandorcollapse":
							try {
								if ("Yes".equalsIgnoreCase(checkValidScript)) {
									xpathPerformance.clickExpandorcollapse(driver, param1, param2,
											fetchMetadataVO, fetchConfigVO, customerDetails,count);
									break;
								} else {
									throw new Exception("ScriptNotValid");
								}
							} catch (Exception e) {
								seleniumFactory.getInstanceObj(instanceName).clickExpandorcollapse(driver, param1, param2,
										fetchMetadataVO, fetchConfigVO, customerDetails);
								break;
							}
						case "clickButton(OIC)":
							seleniumFactory.getInstanceObj(instanceName).oicClickButton(driver, param1, param2,
									fetchMetadataVO, fetchConfigVO, customerDetails);
							break;
						case "Mouse Hover(OIC)":
							seleniumFactory.getInstanceObj(instanceName).oicMouseHover(driver, param1, param2,
									fetchMetadataVO, fetchConfigVO, customerDetails);
							break;

						case "textarea":
							if (fetchMetadataVO.getInputValue() != null || fetchMetadataVO.getInputValue() == "") {
								try {
									if ("Yes".equalsIgnoreCase(checkValidScript)) {

										xpathPerformance.textarea(driver, param1, param2,
												fetchMetadataVO.getInputValue(), fetchMetadataVO, fetchConfigVO, count,customerDetails);
										break;
									} else {
										throw new Exception("ScriptNotValid");
									}
								} catch (Exception e) {
									seleniumFactory.getInstanceObj(instanceName).textarea(driver, param1, param2,
											fetchMetadataVO.getInputValue(), fetchMetadataVO, fetchConfigVO,
											customerDetails);
									break;
								}

							}else {
								break;
							}

						case "Dropdown Values":
							if (fetchMetadataVO.getInputValue() != null || fetchMetadataVO.getInputValue() == "") {
								try {
									if ("Yes".equalsIgnoreCase(checkValidScript)) {
										String xpathlocation = null;
										xpathPerformance.dropdownValues(driver, param1, param2, param3,
												fetchMetadataVO.getInputValue(), fetchMetadataVO, fetchConfigVO, count,
												customerDetails, xpathlocation);
										break;
									} else {
										throw new Exception("ScriptNotValid");
									}
								} catch (Exception e) {
									seleniumFactory.getInstanceObj(instanceName).dropdownValues(driver, param1, param2,
											param3, fetchMetadataVO.getInputValue(), fetchMetadataVO, fetchConfigVO,
											customerDetails);
									break;
								}

							}
							break;
						case "Table SendKeys":
							if (fetchMetadataVO.getInputValue() != null || fetchMetadataVO.getInputValue() == "") {
								try {
									if ("Yes".equalsIgnoreCase(checkValidScript)) {

										xpathPerformance.tableSendKeys(driver, param1, param2, param3,
												fetchMetadataVO.getInputValue(), fetchMetadataVO, fetchConfigVO, count,customerDetails);
										break;
									} else {
										throw new Exception("ScriptNotValid");
									}
								} catch (Exception e) {
									seleniumFactory.getInstanceObj(instanceName).tableSendKeys(driver, param1, param2,
											param3, fetchMetadataVO.getInputValue(), fetchMetadataVO, fetchConfigVO,
											customerDetails);
									break;
								}

							}else {
								break;
							}

						case "multiplelinestableSendKeys":
							if (fetchMetadataVO.getInputValue() != null || fetchMetadataVO.getInputValue() == "") {
								try {
									if ("Yes".equalsIgnoreCase(checkValidScript)) {

										xpathPerformance.multiplelinestableSendKeys(driver, param1, param2, param3,
												fetchMetadataVO.getInputValue(), fetchMetadataVO, fetchConfigVO, count,
												customerDetails);
										break;
									} else {
										throw new Exception("ScriptNotValid");
									}
								} catch (Exception e) {
									seleniumFactory.getInstanceObj(instanceName).multiplelinestableSendKeys(driver,
											param1, param2, param3, fetchMetadataVO.getInputValue(), fetchMetadataVO,
											fetchConfigVO, customerDetails);
									break;
								}

							}else {
								break;
							}
						case "Table Dropdown Values":
							if (fetchMetadataVO.getInputValue() != null || fetchMetadataVO.getInputValue() == "") {
								try {
									if ("Yes".equalsIgnoreCase(checkValidScript)) {

										xpathPerformance.tableDropdownValues(driver, param1, param2,
												fetchMetadataVO.getInputValue(), fetchMetadataVO, fetchConfigVO, count,
												customerDetails);
										break;
									} else {
										throw new Exception("ScriptNotValid");
									}
								} catch (Exception e) {
									seleniumFactory.getInstanceObj(instanceName).tableDropdownValues(driver, param1,
											param2, fetchMetadataVO.getInputValue(), fetchMetadataVO, fetchConfigVO,
											customerDetails);
									break;
								}
							}else {
								break;
							}

						case "clickLinkAction":
							if (fetchMetadataVO.getInputValue() != null || fetchMetadataVO.getInputValue() == "") {
								try {
									if ("Yes".equalsIgnoreCase(checkValidScript)) {
										xpathPerformance.clickLinkAction(driver, param1, param2,
												fetchMetadataVO.getInputValue(), fetchMetadataVO, fetchConfigVO, count,
												customerDetails);
										break;
									} else {
										throw new Exception("ScriptNotValid");
									}
								} catch (Exception e) {
									seleniumFactory.getInstanceObj(instanceName).clickLinkAction(driver, param1, param2,
											fetchMetadataVO.getInputValue(), fetchMetadataVO, fetchConfigVO,
											customerDetails);
									break;
								}

							}else {
								break;
							}
							


						case "clickCheckbox":
							if (fetchMetadataVO.getInputValue() != null || fetchMetadataVO.getInputValue() == "") {
								try {
									if ("Yes".equalsIgnoreCase(checkValidScript)) {
										xpathPerformance.clickCheckbox(driver, param1, fetchMetadataVO.getInputValue(),
												fetchMetadataVO, fetchConfigVO, count, customerDetails);
										break;
									} else {
										throw new Exception("ScriptNotValid");
									}
								} catch (Exception e) {
									seleniumFactory.getInstanceObj(instanceName).clickCheckbox(driver, param1,
											fetchMetadataVO.getInputValue(), fetchMetadataVO, fetchConfigVO,
											customerDetails);
									break;
								}
							}
							else
							{
								break;
							}

						case "clickRadiobutton":
							if (fetchMetadataVO.getInputValue() != null || fetchMetadataVO.getInputValue() == "") {
								try {
									if ("Yes".equalsIgnoreCase(checkValidScript)) {
										xpathPerformance.clickRadiobutton(driver, param1, param2,
												fetchMetadataVO.getInputValue(), fetchMetadataVO, fetchConfigVO, count,
												customerDetails);
										break;
									} else {
										throw new Exception("ScriptNotValid");
									}
								} catch (Exception e) {
									seleniumFactory.getInstanceObj(instanceName).clickRadiobutton(driver, param1,
											param2, fetchMetadataVO.getInputValue(), fetchMetadataVO, fetchConfigVO,
											customerDetails);
									break;
								}

							}
							else
							{
								break;
							}
						case "selectAValue":
							if (fetchMetadataVO.getInputValue() != null || fetchMetadataVO.getInputValue() == "") {
								try {
									if ("Yes".equalsIgnoreCase(checkValidScript)) {

										xpathPerformance.selectAValue(driver, param1, param2,
												fetchMetadataVO.getInputValue(), fetchMetadataVO, fetchConfigVO, count,
												customerDetails);
										break;
									} else {
										throw new Exception("ScriptNotValid");
									}
								} catch (Exception e) {
									seleniumFactory.getInstanceObj(instanceName).selectAValue(driver, param1, param2,
											fetchMetadataVO.getInputValue(), fetchMetadataVO, fetchConfigVO,
											customerDetails);
									break;
								}

							}
							else
							{
								break;
							}

						case "clickTableLink":
							try {
								if ("Yes".equalsIgnoreCase(checkValidScript)) {

									xpathPerformance.clickTableLink(driver, param1, param2, fetchMetadataVO, fetchConfigVO,
											count, customerDetails);
									break;
								} else {
									throw new Exception("ScriptNotValid");
								}
							} catch (Exception e) {
								seleniumFactory.getInstanceObj(instanceName).clickTableLink(driver, param1, param2,
										fetchMetadataVO, fetchConfigVO, customerDetails);
								break;
							}
              
						case "clickLink":
							try {
								if ("Yes".equalsIgnoreCase(checkValidScript)) {

									xpathPerformance.clickLink(driver, param1, param2, fetchMetadataVO, fetchConfigVO,
											count, customerDetails);
									break;
								} else {
									throw new Exception("ScriptNotValid");
								}
							} catch (Exception e) {
								seleniumFactory.getInstanceObj(instanceName).clickLink(driver, param1, param2,
										fetchMetadataVO, fetchConfigVO,customerDetails);
								break;
							}
						case "clickNotificationLink":
							try {
								if ("Yes".equalsIgnoreCase(checkValidScript)) {

									xpathPerformance.clickNotificationLink(driver, param1, param2, fetchMetadataVO, fetchConfigVO,
											customerDetails,count);
									break;
								} else {
									throw new Exception("ScriptNotValid");
								}
							} catch (Exception e) {
								seleniumFactory.getInstanceObj(instanceName).clickNotificationLink(driver, param1, param2,
										fetchMetadataVO, fetchConfigVO, customerDetails);
								break;
							}

						case "clickMenu":
							try {
								if ("Yes".equalsIgnoreCase(checkValidScript)) {
									xpathPerformance.clickMenu(driver, param1, param2, fetchMetadataVO, fetchConfigVO,
											count, customerDetails);
									break;
								} else {
									throw new Exception("ScriptNotValid");
								}
							} catch (Exception e) {

								seleniumFactory.getInstanceObj(instanceName).clickMenu(driver, param1, param2,
										fetchMetadataVO, fetchConfigVO, customerDetails);
								break;
							}

						case "clickImage":
							try {
								if ("Yes".equalsIgnoreCase(checkValidScript)) {

									xpathPerformance.clickImage(driver, param1, param2, fetchMetadataVO, fetchConfigVO,
											count, customerDetails);
									break;
								} else {
									throw new Exception("ScriptNotValid");
								}
							} catch (Exception e) {
								seleniumFactory.getInstanceObj(instanceName).clickImage(driver, param1, param2,
										fetchMetadataVO, fetchConfigVO, customerDetails);
								break;
							}

						case "clickTableImage":
							try {
								if ("Yes".equalsIgnoreCase(checkValidScript)) {

									xpathPerformance.clickTableImage(driver, param1, param2,
											fetchMetadataVO.getInputValue(), fetchMetadataVO, fetchConfigVO, count,
											customerDetails);
									break;
								} else {
									throw new Exception("ScriptNotValid");
								}
							} catch (Exception e) {
								seleniumFactory.getInstanceObj(instanceName).clickTableImage(driver, param1, param2,
										fetchMetadataVO.getInputValue(), fetchMetadataVO, fetchConfigVO,
										customerDetails);
								break;
							}

						case "clickButton":
							try {
								if ("Yes".equalsIgnoreCase(checkValidScript)) {

									xpathPerformance.clickButton(driver, param1, param2, fetchMetadataVO, fetchConfigVO,
											count, customerDetails);
									break;
								} else {
									throw new Exception("ScriptNotValid");
								}
							} catch (Exception e) {
								seleniumFactory.getInstanceObj(instanceName).fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
								seleniumFactory.getInstanceObj(instanceName).clickButton(driver, param1, param2,
										fetchMetadataVO, fetchConfigVO, customerDetails);
								message = seleniumFactory.getInstanceObj(instanceName).getErrorMessages(driver);
								if (message != null && !message.startsWith("Example")
										&& !message.startsWith("Offer Comments") && !message.startsWith("Context Value")
										&& !message.startsWith("Select Book")
										&& !message.startsWith("Enter a date between")
										&& !message.startsWith("Accounting Period") && !message.startsWith("Source")
										&& !message.startsWith("Add Collaborator Type") && !message.startsWith("Batch")
										&& !message.startsWith("Added to Cart") && !message.startsWith("Journal")
										&& !message.startsWith("Project Number")
										&& !message.startsWith("Regional Information")
										&& !message.startsWith("Distribution") && !message.startsWith("Salary Basis")
										&& !message.startsWith("Enter a date on or after")
										&& !message.startsWith("Legislative Data Group") && !message.startsWith("item")
										&& !message.startsWith("Select Subinventories")
										&& !message.startsWith("Comments") && !message.startsWith("Employee Name")
										&& !message.startsWith("All higher-level managers can see comments")
										&& !message.startsWith("Shift")
										&& !message.startsWith("Copy tasks and selected attributes to the new project")
										&& !message.startsWith("Course Title")) {

									fetchConfigVO.setErrormessage(message);
									seleniumFactory.getInstanceObj(instanceName).fullPageFailedScreenshot(driver, fetchMetadataVO,
											customerDetails);
									throw new IllegalArgumentException("Error occured");
								}
								break;
							}

						case "tableRowSelect":
							try {
								if ("Yes".equalsIgnoreCase(checkValidScript)) {

									xpathPerformance.tableRowSelect(driver, param1, param2, fetchMetadataVO,
											fetchConfigVO, count, customerDetails);
									break;
								} else {
									throw new Exception("ScriptNotValid");
								}
							} catch (Exception e) {
								seleniumFactory.getInstanceObj(instanceName).tableRowSelect(driver, param1, param2,
										fetchMetadataVO, fetchConfigVO, customerDetails);
								break;
							}

						case "clickButton Dropdown":
							if (fetchMetadataVO.getInputValue() != null || fetchMetadataVO.getInputValue() == "") {
								try {
									if ("Yes".equalsIgnoreCase(checkValidScript)) {

										xpathPerformance.clickButtonDropdown(driver, param1, param2,
												fetchMetadataVO.getInputValue(), fetchMetadataVO, fetchConfigVO, count,
												customerDetails);
										break;
									} else {
										throw new Exception("ScriptNotValid");
									}
								} catch (Exception e) {
									seleniumFactory.getInstanceObj(instanceName).clickButtonDropdown(driver, param1,
											param2, fetchMetadataVO.getInputValue(), fetchMetadataVO, fetchConfigVO,
											customerDetails);
									break;
								}
							}
							else
							{
								break;
							}

						case "mousehover":
							try {
								if ("Yes".equalsIgnoreCase(checkValidScript)) {
									xpathPerformance.mousehover(driver, param1, param2, fetchMetadataVO, fetchConfigVO,
											customerDetails);
									break;
								} else {
									throw new Exception("ScriptNotValid");
								}
							} catch (Exception e) {
								seleniumFactory.getInstanceObj(instanceName).mousehover(driver, param1, param2,
										fetchMetadataVO, fetchConfigVO, customerDetails);
								break;
							}

						case "scrollUsingElement":
							try {
								if ("Yes".equalsIgnoreCase(checkValidScript)) {
									xpathPerformance.scrollUsingElement(driver, fetchMetadataVO.getInputParameter(),
											fetchMetadataVO, fetchConfigVO, count, customerDetails);
									break;
								} else {
									throw new Exception("ScriptNotValid");
								}
							} catch (Exception e) {
								seleniumFactory.getInstanceObj(instanceName).scrollUsingElement(driver,
										fetchMetadataVO.getInputParameter(), fetchMetadataVO, fetchConfigVO,
										customerDetails);
								break;
							}

						case "moveToElement":
							try {
								if ("Yes".equalsIgnoreCase(checkValidScript)) {
									xpathPerformance.moveToElement(driver,
											fetchMetadataVO.getInputParameter(), fetchMetadataVO, fetchConfigVO,customerDetails,count);
									break;
								} else {
									throw new Exception("ScriptNotValid");
								}
							} catch (Exception e) {
								seleniumFactory.getInstanceObj(instanceName).moveToElement(driver,
										fetchMetadataVO.getInputParameter(), fetchMetadataVO, fetchConfigVO);
								break;
							}

						case "switchToDefaultFrame":
							seleniumFactory.getInstanceObj(instanceName).switchToDefaultFrame(driver);
							break;

						case "windowhandle":
							seleniumFactory.getInstanceObj(instanceName).windowhandle(driver, fetchMetadataVO,
									fetchConfigVO, customerDetails);
							break;
						case "dragAnddrop":
							seleniumFactory.getInstanceObj(instanceName).dragAnddrop(driver,
									fetchMetadataVO.getXpathLocation(), fetchMetadataVO.getXpathLocation1(),
									fetchMetadataVO, fetchConfigVO, customerDetails);
							break;
						case "clickFilter":
							try {
								if ("Yes".equalsIgnoreCase(checkValidScript)) {

									xpathPerformance.clickFilter(driver, param1, param2,
											fetchMetadataVO, fetchConfigVO, customerDetails,count);
									break;
								} else {
									throw new Exception("ScriptNotValid");
								}
							} catch (Exception e) {

								seleniumFactory.getInstanceObj(instanceName).clickFilter(driver, param1, param2,
										fetchMetadataVO, fetchConfigVO, customerDetails);
								break;

							}

						case "switchToFrame":
							try {
								if ("Yes".equalsIgnoreCase(checkValidScript)) {

									xpathPerformance.switchToFrame(driver, fetchMetadataVO.getInputParameter(),
											fetchMetadataVO, fetchConfigVO, count,customerDetails);
									break;
								} else {
									throw new Exception("ScriptNotValid");
								}
							} catch (Exception e) {

								seleniumFactory.getInstanceObj(instanceName).switchToFrame(driver,
										fetchMetadataVO.getInputParameter(), fetchMetadataVO, fetchConfigVO,
										customerDetails);
								break;
							}

						case "selectByText":
							if (fetchMetadataVO.getInputValue() != null || fetchMetadataVO.getInputValue() == "") {
								try {
									if ("Yes".equalsIgnoreCase(checkValidScript)) {

										xpathPerformance.selectByText(driver, param1, param2,
												fetchMetadataVO.getInputValue(), fetchMetadataVO, fetchConfigVO, count,
												customerDetails);
										break;
									} else {
										throw new Exception("ScriptNotValid");
									}
								} catch (Exception e) {
									seleniumFactory.getInstanceObj(instanceName).selectByText(driver, param1, param2,
											fetchMetadataVO.getInputValue(), fetchMetadataVO, fetchConfigVO,
											customerDetails);
									break;
								}
							}
							else
							{
								break;
							}

						case "copy":
							seleniumFactory.getInstanceObj(instanceName).copy(driver, fetchMetadataVO, fetchConfigVO,
									customerDetails);
							break;
						case "copynumber":
							try {
								if ("Yes".equalsIgnoreCase(checkValidScript)) {

									globalValueForSteps = xpathPerformance.copynumber(driver,
											param1, param2, fetchMetadataVO, fetchConfigVO, customerDetails,count);
									break;
								} else {
									throw new Exception("ScriptNotValid");
								}
							} catch (Exception e) {

								globalValueForSteps = seleniumFactory.getInstanceObj(instanceName).copynumber(driver,
										param1, param2, fetchMetadataVO, fetchConfigVO, customerDetails);
								break;
							}
							
						case "copyy":
							seleniumFactory.getInstanceObj(instanceName).copyy(driver,
									fetchMetadataVO.getXpathLocation(), fetchMetadataVO, fetchConfigVO,
									customerDetails);
							break;
						case "copytext":
							seleniumFactory.getInstanceObj(instanceName).copytext(driver,
									fetchMetadataVO.getXpathLocation(), fetchMetadataVO, fetchConfigVO,
									customerDetails);
							break;
						case "clear":
							try {
								if ("Yes".equalsIgnoreCase(checkValidScript)) {

									xpathPerformance.clear(driver, param1, param2, fetchMetadataVO, fetchConfigVO,
											count,customerDetails);
									break;
								} else {
									throw new Exception("ScriptNotValid");
								}
							} catch (Exception e) {

								seleniumFactory.getInstanceObj(instanceName).clear(driver, param1, param2,
										fetchMetadataVO, fetchConfigVO, customerDetails);
								break;
							}
							// XpathPerformance code for cases ended
						case "enter":
							seleniumFactory.getInstanceObj(instanceName).enter(driver, fetchMetadataVO, fetchConfigVO,
									customerDetails);
							break;
						case "tab":
							seleniumFactory.getInstanceObj(instanceName).tab(driver, fetchMetadataVO, fetchConfigVO,
									customerDetails);
							break;
						case "paste":
							seleniumFactory.getInstanceObj(instanceName).paste(driver,
									fetchMetadataVO.getInputParameter(), fetchMetadataVO, fetchConfigVO,
									globalValueForSteps, customerDetails);
							break;
						case "uploadFileAutoIT":
								seleniumFactory.getInstanceObj(instanceName).uploadFileAutoIT(driver,fetchConfigVO.getUPLOAD_FILE_PATH(), param1, param2, param3, fetchMetadataVO, customerDetails);
							break;
						case "windowclose":
							seleniumFactory.getInstanceObj(instanceName).windowclose(driver, fetchMetadataVO,
									fetchConfigVO, customerDetails);
							break;
						case "switchDefaultContent":
							seleniumFactory.getInstanceObj(instanceName).switchDefaultContent(driver, fetchMetadataVO,
									fetchConfigVO, customerDetails);
							break;
						case "switchParentWindow":
							seleniumFactory.getInstanceObj(instanceName).switchParentWindow(driver, fetchMetadataVO,
									fetchConfigVO, customerDetails);
							break;
						case "switchToParentWindow":
							seleniumFactory.getInstanceObj(instanceName).switchToParentWindow(driver, fetchMetadataVO,
									fetchConfigVO, customerDetails);
							break;
						case "DatePicker":
							if (fetchMetadataVO.getInputValue() != null || fetchMetadataVO.getInputValue() == "") {
								try {
									if ("Yes".equalsIgnoreCase(checkValidScript)) {

										xpathPerformance.datePicker(driver, param1, param2,
												fetchMetadataVO.getInputValue(), fetchMetadataVO, fetchConfigVO,
												customerDetails,count);
										break;
									} else {
										throw new Exception("ScriptNotValid");
									}
								} catch (Exception e) {
									seleniumFactory.getInstanceObj(instanceName).datePicker(driver, param1, param2,
											fetchMetadataVO.getInputValue(), fetchMetadataVO, fetchConfigVO,
											customerDetails);
									break;
								}
							} else {
								break;
							}
						case "refreshPage":
							seleniumFactory.getInstanceObj(instanceName).refreshPage(driver, fetchMetadataVO,
									fetchConfigVO, customerDetails);
							break;
						case "navigateToBackPage":
							seleniumFactory.getInstanceObj(instanceName).navigateToBackPage(driver, fetchMetadataVO,
									fetchConfigVO, customerDetails);
							break;
						case "openPdf":
							seleniumFactory.getInstanceObj(instanceName).openPdf(driver,
									fetchMetadataVO.getInputValue(), fetchMetadataVO, fetchConfigVO, customerDetails);
							break;
						case "openFile":
							seleniumFactory.getInstanceObj(instanceName).openFile(driver, fetchMetadataVO,
									fetchConfigVO, customerDetails);
							break;
						case "actionApprove":
							seleniumFactory.getInstanceObj(instanceName).actionApprove(driver, param1, param2,
									fetchMetadataVO, fetchConfigVO, customerDetails);
							break;
						case "multipleSendKeys":
							if (fetchMetadataVO.getInputValue() != null || fetchMetadataVO.getInputValue() == "") {
								try {
									if ("Yes".equalsIgnoreCase(checkValidScript)) {

										xpathPerformance.multipleSendKeys(driver, param1, param2,
												value1, value2, fetchMetadataVO, fetchConfigVO, customerDetails,count);
										break;
									} else {
										throw new Exception("ScriptNotValid");
									}
								} catch (Exception e) {
									seleniumFactory.getInstanceObj(instanceName).multipleSendKeys(driver, param1, param2,
											value1, value2, fetchMetadataVO, fetchConfigVO, customerDetails);
									break;
								}								
							}else {
								break;
							}

						case "Login into Application(Informatica)":
							userName = fetchMetadataVO.getInputValue();
							logger.info("Navigating to Login into Application Action");
							if (fetchMetadataVO.getInputValue() != null || fetchMetadataVO.getInputValue() == "") {
								seleniumFactory.getInstanceObj(instanceName).loginInformaticaApplication(driver,
										fetchConfigVO, fetchMetadataVO, type1, type2, type3, param1, param2, param3,
										fetchMetadataVO.getInputValue(),
										dataBaseEntry.getPassword(param, userName, fetchConfigVO), customerDetails);
								userName = null;
							}
							break;
						case "Logout(Informatica)":
							seleniumFactory.getInstanceObj(instanceName).InformaticaLogout(driver, fetchConfigVO,
									fetchMetadataVO, type1, type2, type3, param1, param2, param3, customerDetails);
							break;

						case "sendvalues(Informatica)":
							if (fetchMetadataVO.getInputValue() != null || fetchMetadataVO.getInputValue() == "") {
								seleniumFactory.getInstanceObj(instanceName).InformaticaSendValue(driver, param1,
										param2, fetchMetadataVO.getInputValue(), fetchMetadataVO, fetchConfigVO,
										customerDetails);
								break;
							} else {
								break;
							}

						case "selectAValue(Informatica)":
							if (fetchMetadataVO.getInputValue() != null || "".equals(fetchMetadataVO.getInputValue())) {
								seleniumFactory.getInstanceObj(instanceName).InformaticaSelectAValue(driver, param1,
										param2, fetchMetadataVO.getInputValue(), fetchMetadataVO, fetchConfigVO,
										customerDetails);
							}
							break;

						case "clickLink(Informatica)":
							seleniumFactory.getInstanceObj(instanceName).InformaticaclickLink(driver, param1, param2,
									fetchMetadataVO, fetchConfigVO, customerDetails);
							break;
						case "clickImage(Informatica)":
							seleniumFactory.getInstanceObj(instanceName).InformaticaClickImage(driver, param1, param2,
									fetchMetadataVO, fetchConfigVO, customerDetails);
							break;
						case "clickButton(Informatica)":
							seleniumFactory.getInstanceObj(instanceName).InformaticaClickButton(driver, param1, param2,
									fetchMetadataVO, fetchConfigVO, customerDetails);
							break;
						case "waitTillLoad":
							Integer valueInMS = 1000 * Integer.parseInt(fetchMetadataVO.getInputValue());
							fetchMetadataVO.setInputValue(valueInMS.toString());
							seleniumFactory.getInstanceObj(instanceName).waitTillLoad(driver, param1, param2,
									fetchMetadataVO, fetchConfigVO);
							break;

						case "compareValue":
							try {
								seleniumFactory.getInstanceObj(instanceName).compareValue(driver,
										fetchMetadataVO.getInputParameter(), fetchMetadataVO, fetchConfigVO,
										globalValueForSteps, customerDetails);
								seleniumFactory.getInstanceObj(instanceName).createScreenShot(
										fetchMetadataVO, fetchConfigVO, "Matched", customerDetails,true);
								break;
							} catch (Exception e) {
								seleniumFactory.getInstanceObj(instanceName).createScreenShot(
										fetchMetadataVO, fetchConfigVO, "Unmatched", customerDetails,false);
								throw new WatsEBSException(500,"Failed at campare Value");
							}
							
						case "apiAccessToken":
							seleniumFactory.getInstanceObjFromAbstractClass(fetchConfigVO.getINSTANCE_NAME()).apiAccessToken(fetchMetadataVO,
									accessTokenStorage, customerDetails);
							break;

						case "apiValidationResponse":
							seleniumFactory.getInstanceObjFromAbstractClass(fetchConfigVO.getINSTANCE_NAME()).apiValidationResponse(fetchMetadataVO,
									accessTokenStorage, api,customerDetails,fetchConfigVO);
							break;

						case "validation":
							seleniumFactory.getInstanceObjFromAbstractClass(fetchConfigVO.getINSTANCE_NAME()).validation(fetchMetadataVO, api);
							break;
							
		
						case "Enter Multiple Transactions":
							woodInterface.enterMultipleTransaction(driver, fetchConfigVO,
									fetchMetadataVO, customerDetails);
							break;
						default:
							logger.info("Action Name is not matched with " + "" + actionName);
							break;

						}
						fetchConfigVO.setStatus1("Pass");
						logger.info("Successfully Executed the" + "" + actionName);
						try {
							dataBaseEntry.updatePassedScriptLineStatus(fetchMetadataVO, fetchConfigVO,
									testScriptParamId, "Pass");
							Optional<String> testSetlineWarningMsgOptional = Optional.ofNullable(fetchMetadataVO)
									.map(ScriptDetailsDto::getLineErrorMsg)
									.filter(testSetlineWarningMsg -> !testSetlineWarningMsg.isEmpty());

							testSetlineWarningMsgOptional
									.filter(testSetlineWarningMsg -> testSetlineWarningMsg.startsWith("Warning"))
									.ifPresent(testSetlineWarningMsg -> {
										try {
											dataBaseEntry.updateTestSetLinesWarningMessage(
													fetchMetadataVO.getTestScriptParamId(), testSetlineWarningMsg);
										} catch (ClassNotFoundException | SQLException e) {
											logger.error("Not able to update warning message: {}" + e.getMessage());
										}
									});
							fetchMetadataVO.setStatus("Pass");
						} catch (Exception e) {
							logger.error(e.getMessage());
						}
					}

					if (fetchMetadataListVO.size() == i && !isError) {
						String passScriptKey = null;
						if("YES".equalsIgnoreCase(fetchConfigVO.getMANAGEMENT_TOOL_ENABLED())){
							passScriptKey = graphQLService.getScriptId(fetchConfigVO,scriptIssueId);
							graphQLService.changeStatusOfScriptInJiraXrayCloud(passScriptKey, "PASSED");
						}
							FetchScriptVO post = new FetchScriptVO();
							post.setP_test_set_id(testSetId);
							post.setP_status("Pass");
							post.setP_script_id(scriptId);
							post.setP_test_set_line_id(testSetLineId);
							post.setP_pass_path(passurl);
							post.setP_fail_path(failurl);
							post.setP_exception_path(detailurl);
							post.setP_test_set_line_path(scripturl);
							long endTime = System.currentTimeMillis();
							logger.info("End Time " + endTime);

							long gap = startTime - endTime;
							logger.info("Time difference " + gap);

							Date enddate = new Date();
							fetchConfigVO.setEndtime(enddate);
							fetchConfigVO.setStatus1("Pass");
							try {
								// dataService.updateTestCaseStatus(post, param, fetchConfigVO);
								if ("Y".equalsIgnoreCase(fetchMetadataVO.getDependency())) {
									if (scriptStatus.containsKey(Integer.parseInt(fetchMetadataVO.getScriptId()))) {
										Status s = scriptStatus.get(Integer.parseInt(fetchMetadataVO.getScriptId()));
										if (!"Fail".equalsIgnoreCase(s.getStatusMsg())) {
											int awaitCounter = s.getInExecutionCount();
											s.setInExecutionCount(--awaitCounter);
											if (awaitCounter <= 0) {
												s.setStatusMsg("Pass");
											}
										}
									}
								}

								dataBaseEntry.updateTestCaseEndDate(post, enddate, post.getP_status());
								dataBaseEntry.updateTestCaseStatus(post, fetchConfigVO, fetchMetadataListVO,
										fetchConfigVO.getStarttime(), customerDetails.getTestSetName(),false);

								dataBaseEntry.updateEndTime(fetchConfigVO, testSetLineId, testSetId, enddate);
							} catch (Exception e) {
								e.printStackTrace();
							}

							limitScriptExecutionService.insertTestRunScriptData(fetchConfigVO, fetchMetadataListVO,
									scriptId1, scriptNumber, "pass", startdate, enddate, customerDetails);
							limitScriptExecutionService.updateFaileScriptscount(testSetLineId, testSetId);
							downloadScreenShot(fetchConfigVO, fetchMetadataVO, customerDetails, false);
							fetchMetadataVO.setStatus("Pass");
							seleniumFactory.getInstanceObjFromAbstractClass(fetchConfigVO.getINSTANCE_NAME()).createPdf(
									fetchMetadataListVO, fetchConfigVO, seqNum + "_" + scriptNumber + ".pdf",
									customerDetails);
							if("YES".equalsIgnoreCase(fetchConfigVO.getMANAGEMENT_TOOL_ENABLED())){
								String sourceFilePath = (fetchConfigVO.getWINDOWS_PDF_LOCATION() + customerDetails.getCustomerName()
								+ File.separator + customerDetails.getTestSetName() + File.separator) + seqNum + "_" + scriptNumber + ".pdf";
								File file = new File(sourceFilePath);
								byte [] bytes = Files.readAllBytes(file.toPath());
							     String b64 = Base64.getEncoder().encodeToString(bytes);
								graphQLService.addAttachmentToScript(passScriptKey,b64,file.getName());
							}
							if ("SHAREPOINT".equalsIgnoreCase(fetchConfigVO.getPDF_LOCATION())) {
								seleniumFactory.getInstanceObjFromAbstractClass(fetchConfigVO.getINSTANCE_NAME())
										.uploadPdfToSharepoint(fetchMetadataListVO, fetchConfigVO, customerDetails);
							}
							if(StringUtils.isNotBlank(fetchMetadataVO.getIssueKey())){ 
								fetchConfigVO.getJIRA_ISSUE_URL();
								jiraTicketBugService.jiraIssueFixed(fetchMetadataVO.getIssueKey(),fetchConfigVO.getJIRA_ISSUE_UPDATE_STATUS_URL(),fetchConfigVO.getJIRA_ISSUE_UPDATE_TRANSITIONS());
							}
							if (Constants.smartBear.YES.toString().equalsIgnoreCase(fetchConfigVO.getSMARTBEAR_ENABLED())
									&& Constants.smartBear.WOOD.toString().equalsIgnoreCase(fetchConfigVO.getINSTANCE_NAME())) {
								String sourceFilePath = (fetchConfigVO.getWINDOWS_PDF_LOCATION().replace("/",
										File.separator) + customerDetails.getCustomerName() + File.separator
										+ customerDetails.getTestSetName() + File.separator) + seqNum + "_"
										+ scriptNumber + ".pdf";
								smartBearService.smartBearIntegrate(fetchMetadataListVO, "Passed", sourceFilePath,
										fetchConfigVO.getSMARTBEAR_PROJECT_NAME(),
										fetchConfigVO.getSMARTBEAR_CUSTOM_COLUMN_NAME());
							}
					}

				} catch (Exception e) {
					e.printStackTrace();
					if (scriptStatus.containsKey(Integer.parseInt(fetchMetadataVO.getScriptId()))) {
						Status s = scriptStatus.get(Integer.parseInt(fetchMetadataVO.getScriptId()));
						s.setStatusMsg("Fail");
					}
					logger.error("Failed to Execute the " + "" + actionName);
					logger.error(e.getMessage());
					errorMessagesHandler.getError(actionName, fetchMetadataVO, fetchConfigVO, testScriptParamId,
							message, param1, param2, dataBaseEntry.getPassword(param, userName, fetchConfigVO));
					isError = true;
				}
				if (isError) {
					String failScriptKey = null;
					if("YES".equalsIgnoreCase(fetchConfigVO.getMANAGEMENT_TOOL_ENABLED())){
						failScriptKey = graphQLService.getScriptId(fetchConfigVO,scriptIssueId);
//						fetchConfigVO.setSctiptId(key);
						graphQLService.changeStatusOfScriptInJiraXrayCloud(failScriptKey, "FAILED");
					}
						FetchScriptVO post = new FetchScriptVO();
						post.setP_test_set_id(testSetId);
						post.setP_status("Fail");
						post.setP_script_id(scriptId);
						post.setP_test_set_line_id(testSetLineId);
						post.setP_pass_path(passurl);
						post.setP_fail_path(failurl);
						post.setP_exception_path(detailurl);
						post.setP_test_set_line_path(scripturl);
						failcount = failcount + 1;
						Date enddate = new Date();
						fetchConfigVO.setEndtime(enddate);

//						dataService.updateTestCaseStatus(post, testSetId, fetchConfigVO);

						dataBaseEntry.updateTestCaseEndDate(post, enddate, post.getP_status());
						dataBaseEntry.updateTestCaseStatus(post, fetchConfigVO, fetchMetadataListVO,
								fetchConfigVO.getStarttime(), customerDetails.getTestSetName(),false);

						dataBaseEntry.updateEndTime(fetchConfigVO, testSetLineId, testSetId, enddate);

						limitScriptExecutionService.insertTestRunScriptData(fetchConfigVO, fetchMetadataListVO,
								scriptId1, scriptNumber, "Fail", startdate, enddate, customerDetails);

						int failedScriptRunCount = limitScriptExecutionService.getFailedScriptRunCount(testSetLineId,
								testSetId);

						fetchConfigVO.setStatus1("Fail");
						downloadScreenShot(fetchConfigVO, fetchMetadataVO, customerDetails, false);
						seleniumFactory.getInstanceObjFromAbstractClass(fetchConfigVO.getINSTANCE_NAME()).createPdf(
								fetchMetadataListVO, fetchConfigVO,
								seqNum + "_" + scriptNumber + "_RUN" + failedScriptRunCount + ".pdf",
								customerDetails);
						if("YES".equalsIgnoreCase(fetchConfigVO.getMANAGEMENT_TOOL_ENABLED())){
							String sourceFilePath = (fetchConfigVO.getWINDOWS_PDF_LOCATION() + customerDetails.getCustomerName()
							+ File.separator + customerDetails.getTestSetName() + File.separator) + seqNum + "_" + scriptNumber + "_RUN" + failedScriptRunCount + ".pdf";
							File file = new File(sourceFilePath);
							byte [] bytes = Files.readAllBytes(file.toPath());
						     String b64 = Base64.getEncoder().encodeToString(bytes);
							graphQLService.addAttachmentToScript(failScriptKey,b64,file.getName());
						}
						if ("SHAREPOINT".equalsIgnoreCase(fetchConfigVO.getPDF_LOCATION())) {
							seleniumFactory.getInstanceObjFromAbstractClass(fetchConfigVO.getINSTANCE_NAME())
									.uploadPdfToSharepoint(fetchMetadataListVO, fetchConfigVO, customerDetails);
						}
						if (Constants.smartBear.YES.toString().equalsIgnoreCase(fetchConfigVO.getSMARTBEAR_ENABLED())
								&& Constants.smartBear.WOOD.toString().equalsIgnoreCase(fetchConfigVO.getINSTANCE_NAME())) {
							String sourceFilePath = (fetchConfigVO.getWINDOWS_PDF_LOCATION().replace("/",
									File.separator) + customerDetails.getCustomerName() + File.separator
									+ customerDetails.getTestSetName() + File.separator) + seqNum + "_" + scriptNumber
									+ "_RUN" + failedScriptRunCount + ".pdf";
							smartBearService.smartBearIntegrate(fetchMetadataListVO, "Failed", sourceFilePath,
									fetchConfigVO.getSMARTBEAR_PROJECT_NAME(),
									fetchConfigVO.getSMARTBEAR_CUSTOM_COLUMN_NAME());
						}
					return;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			dataBaseEntry.insertScriptExecAuditRecord(auditTrial, AUDIT_TRAIL_STAGES.SEF, e.getMessage());
			throw e;
		}
	}

	private void downloadScreenShot(FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			CustomerProjectDto customerDetails, boolean evidenceReport) {
		String seqNumber = evidenceReport ? null : fetchMetadataVO.getSeqNum();
		String screenShotFolder = fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + customerDetails.getCustomerName()
				+ File.separator + customerDetails.getTestSetName() + File.separator;
		seleniumFactory.getInstanceObjFromAbstractClass(fetchConfigVO.getINSTANCE_NAME())
				.downloadScreenshotsFromObjectStore(screenShotFolder, customerDetails.getCustomerName(),
						customerDetails.getTestSetName(), seqNumber);
		logger.info("Successfully downloaded ScreenShots");
	}
}
