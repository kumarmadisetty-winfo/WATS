package com.winfo.scripts;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.lowagie.text.DocumentException;
import com.winfo.Factory.SeleniumKeywordsFactory;
import com.winfo.config.DriverConfiguration;
import com.winfo.dao.CodeLinesRepository;
import com.winfo.dao.PyJabActionRepo;
import com.winfo.exception.WatsEBSCustomException;
import com.winfo.model.AuditScriptExecTrail;
import com.winfo.services.DataBaseEntry;
import com.winfo.services.ErrorMessagesHandler;
import com.winfo.services.FetchConfigVO;
import com.winfo.services.FetchMetadataVO;
import com.winfo.services.FetchScriptVO;
import com.winfo.services.LimitScriptExecutionService;
import com.winfo.services.ScriptXpathService;
import com.winfo.services.TestCaseDataService;
import com.winfo.services.TestScriptExecService;
import com.winfo.utils.Constants;
import com.winfo.utils.Constants.AUDIT_TRAIL_STAGES;
import com.winfo.utils.Constants.BOOLEAN_STATUS;
import com.winfo.vo.ApiValidationVO;
import com.winfo.vo.CustomerProjectDto;
import com.winfo.vo.ResponseDto;
import com.winfo.vo.ScriptDetailsDto;
import com.winfo.vo.Status;

@Service

public class RunAutomation {
	public final Logger log = LogManager.getLogger(RunAutomation.class);

	@Autowired
	SeleniumKeywordsFactory seleniumFactory;
	@Autowired
	ErrorMessagesHandler errorMessagesHandler;
	@Autowired
	DriverConfiguration driverConfiguration;

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
	public ResponseDto run(String args) throws MalformedURLException {
		log.info("TestRunId ***" + args);

		ResponseDto executeTestrunVo;
		String checkPackage = dataBaseEntry.getPackage(args);
		if (checkPackage != null && checkPackage.toLowerCase().contains(Constants.EBS)) {
			executeTestrunVo = ebsRun(args);
		} else {
			executeTestrunVo = cloudRun(args);
		}
		log.info("End of Test Script Run # : " + args);

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
				log.info(" Running Independent - " + metaData.getKey());
				testScriptExecService.executorMethodPyJab(testSetId, fetchConfigVO, metaData, true, customerDetails);
			}

			ExecutorService executordependent = Executors.newFixedThreadPool(fetchConfigVO.getParallel_dependent());
			for (Entry<Integer, List<ScriptDetailsDto>> metaData : dependentScriptMap.entrySet()) {
				log.info(" Running Dependent - " + metaData.getKey());
				executordependent.execute(() -> {
					log.info(" Running Dependent in executor - " + metaData.getKey());
					boolean run = dataBaseEntry.checkRunStatusOfTestRunLevelDependantScript(
							metaData.getValue().get(0).getDependencyScriptNumber());
					log.info(" Dependant Script run status" + metaData.getValue().get(0).getScriptId() + " " + run);
					testScriptExecService.executorMethodPyJab(testSetId, fetchConfigVO, metaData, run, customerDetails);
				});
			}
			executordependent.shutdown();

			executeTestrunVo.setStatusCode(200);
			executeTestrunVo.setStatusMessage("SUCCESS");
			executeTestrunVo.setStatusDescr("SUCCESS");
		} catch (Exception e) {
			dataBaseEntry.updateExecStatusIfTestRunIsCompleted(testSetId);
			if (e instanceof WatsEBSCustomException)
				throw e;
			throw new WatsEBSCustomException(500, "Exception Occured while creating script for Test Run", e);
		}
		return executeTestrunVo;
	}

	public ResponseDto cloudRun(String testSetId) throws MalformedURLException {
		ResponseDto executeTestrunVo = new ResponseDto();
		log.info("Start of cloud run method");
		try {
			FetchConfigVO fetchConfigVO = testScriptExecService.fetchConfigVO(testSetId);
//			List<FetchMetadataVO> fetchMetadataListVO = dataBaseEntry.getMetaDataVOList(testSetId, null, false, true);

			CustomerProjectDto customerDetails = dataBaseEntry.getCustomerDetails(testSetId);

			List<ScriptDetailsDto> testLinesDetails = dataBaseEntry.getScriptDetailsListVO(testSetId, null, false,
					true);

			SortedMap<Integer, List<ScriptDetailsDto>> dependentScriptMap = new TreeMap<Integer, List<ScriptDetailsDto>>();
			SortedMap<Integer, List<ScriptDetailsDto>> metaDataMap = dataService.prepareTestcasedata(testLinesDetails,
					dependentScriptMap);
			Map<Integer, Status> scriptStatus = new HashMap<>();

			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
			Date startDate = sdf.parse(fetchConfigVO.getStart_date());
			Date endDate = sdf.parse(fetchConfigVO.getEnd_date());
				if (date.after(endDate) || date.before(startDate)) {
					executeTestrunVo.setStatusCode(404);
					executeTestrunVo.setStatusMessage("ERROR");
					if (date.after(endDate) || date.before(startDate)) {
						executeTestrunVo.setStatusDescr(
								"Your request could not be processed the Testrun, please check with the Start and End Date");

					} else {
						executeTestrunVo.setStatusDescr(
								"Your request could not be processed as you have reached the scripts execution threshold. Reach out to the WATS support team to enhance the limit..");

					}
					return executeTestrunVo;

				}
			

			fetchConfigVO.setStarttime1(date);

			log.info("Independent scripts # - {} ", metaDataMap.toString());
			ExecutorService executor = Executors.newFixedThreadPool(fetchConfigVO.getParallel_independent());
			try {
				for (Entry<Integer, List<ScriptDetailsDto>> metaData : metaDataMap.entrySet()) {

					String scriptNumber = metaData.getValue().get(0).getScriptNumber();

					executor.execute(() -> {
						log.info("Start of Independent Script Execution of {}", scriptNumber);
						try {
							long starttimeIntermediate = System.currentTimeMillis();
							String flag = dataBaseEntry.getTrMode(testSetId, fetchConfigVO);
							if (flag.equalsIgnoreCase("STOPPED")) {
								metaData.getValue().clear();
								executor.shutdown();
								log.info("Test run is STOPPED - Scripts will only run when Test Run status is ACTIVE");
							} else {
								executorMethod(testSetId, fetchConfigVO, testLinesDetails, metaData, scriptStatus,
										customerDetails);
							}
							long i = System.currentTimeMillis() - starttimeIntermediate;
							increment = increment + i;
							log.info("time , {} ", increment / 1000 % 60);
						} catch (Exception e) {
							log.info("Exception occured while executing Independent Script of {}", scriptNumber);
							e.printStackTrace();
						}
						log.info("End of Independent Script Execution of {}", scriptNumber);
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
							.newFixedThreadPool(fetchConfigVO.getParallel_dependent());

					for (Entry<Integer, List<ScriptDetailsDto>> metaData : dependentScriptMap.entrySet()) {
						log.info(" Running Dependent - " + metaData.getKey());
						executordependent.execute(() -> {
							log.info(" Running Dependent in executor - " + metaData.getKey());
							boolean run;
							run = dataBaseEntry.checkRunStatusOfTestRunLevelDependantScript(
									metaData.getValue().get(0).getDependencyScriptNumber());
							log.info(" Dependant Script run status" + metaData.getValue().get(0).getScriptId() + " "
									+ run);

							try {
								String flag = dataBaseEntry.getTrMode(testSetId, fetchConfigVO);
								if (flag.equalsIgnoreCase("STOPPED")) {
									metaData.getValue().clear();
									executor.shutdown();
									System.out.println("treminattion is succeed");
								} else {
									if (run) {
										executorMethod(testSetId, fetchConfigVO, testLinesDetails, metaData,
												scriptStatus, customerDetails);
									} else {
										String passurl = fetchConfigVO.getImg_url() + customerDetails.getCustomerName()
												+ "/" + customerDetails.getProjectName() + "/"
												+ customerDetails.getTestSetName() + "/" + "Passed_Report.pdf";
										String failurl = fetchConfigVO.getImg_url() + customerDetails.getCustomerName()
												+ "/" + customerDetails.getProjectName() + "/"
												+ customerDetails.getTestSetName() + "/" + "Failed_Report.pdf";
										String detailurl = fetchConfigVO.getImg_url()
												+ customerDetails.getCustomerName() + "/"
												+ customerDetails.getProjectName() + "/"
												+ customerDetails.getTestSetName() + "/" + "Detailed_Report.pdf";
										String scripturl = fetchConfigVO.getImg_url()
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
										System.out.println("Came here to check fail condition");

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

				seleniumFactory.getInstanceObjFromAbstractClass(fetchConfigVO.getInstance_name())
						.createPdf(fetchMetadataListVOforEvidence, fetchConfigVO, "Passed_Report.pdf", customerDetails);
				seleniumFactory.getInstanceObjFromAbstractClass(fetchConfigVO.getInstance_name())
						.createPdf(fetchMetadataListVOforEvidence, fetchConfigVO, "Failed_Report.pdf", customerDetails);
				seleniumFactory.getInstanceObjFromAbstractClass(fetchConfigVO.getInstance_name()).createPdf(
						fetchMetadataListVOforEvidence, fetchConfigVO, "Detailed_Report.pdf", customerDetails);

				dataBaseEntry.updateStartAndEndTimeForTestSetTable(customerDetails.getTestSetId(), fetchConfigVO.getStarttime(), fetchConfigVO.getEndtime());
				
				increment = 0;

				if ("SHAREPOINT".equalsIgnoreCase(fetchConfigVO.getPDF_LOCATION())) {
					seleniumFactory.getInstanceObj(fetchConfigVO.getInstance_name())
							.uploadPDF(fetchMetadataListVOforEvidence, fetchConfigVO, customerDetails);
				}
				executeTestrunVo.setStatusCode(200);
				executeTestrunVo.setStatusMessage("SUCCESS");
				executeTestrunVo.setStatusDescr("SUCCESS");
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Exception in dependant block of code");
				Thread.currentThread().interrupt();
			}
		} catch (Exception e) {
			log.error("Error in Cloud test run method " + e.getMessage());
			dataBaseEntry.updateEnabledStatusForTestSetLine(testSetId, "Y");
		}
		return executeTestrunVo;
	}

	public void executorMethod(String args, FetchConfigVO fetchConfigVO, List<ScriptDetailsDto> testLinesDetails,
			Entry<Integer, List<ScriptDetailsDto>> metaData, Map<Integer, Status> scriptStatus,
			CustomerProjectDto customerDetails) throws Exception {
		List<String> failList = new ArrayList<>();
		WebDriver driver = null;
		List<ScriptDetailsDto> fetchMetadataListsVO = metaData.getValue();
		String testSetId = customerDetails.getTestSetId();
		String testSetLineId = fetchMetadataListsVO.get(0).getTestSetLineId();

		String scriptId = fetchMetadataListsVO.get(0).getScriptId();
		String passurl = fetchConfigVO.getImg_url() + customerDetails.getCustomerName() + "/"
				+ customerDetails.getProjectName() + "/" + customerDetails.getTestSetName() + "/" + "Passed_Report.pdf";
		String failurl = fetchConfigVO.getImg_url() + customerDetails.getCustomerName() + "/"
				+ customerDetails.getProjectName() + "/" + customerDetails.getTestSetName() + "/" + "Failed_Report.pdf";
		String detailurl = fetchConfigVO.getImg_url() + customerDetails.getCustomerName() + "/"
				+ customerDetails.getProjectName() + "/" + customerDetails.getTestSetName() + "/"
				+ "Detailed_Report.pdf";
		String scripturl = fetchConfigVO.getImg_url() + customerDetails.getCustomerName() + "/"
				+ customerDetails.getProjectName() + "/" + customerDetails.getTestSetName() + "/"
				+ fetchMetadataListsVO.get(0).getSeqNum() + "_" + fetchMetadataListsVO.get(0).getScriptNumber()
				+ ".pdf";
		log.info("Pass Url - {}", passurl);
		log.info("Fail Url - {}", failurl);
		log.info("Detailed Url - {}", detailurl);
		boolean isDriverError = true;
		AuditScriptExecTrail auditTrial = dataBaseEntry.insertScriptExecAuditRecord(AuditScriptExecTrail.builder()
				.testSetLineId(Integer.valueOf(testSetLineId)).triggeredBy(fetchMetadataListsVO.get(0).getExecutedBy())
				.correlationId(UUID.randomUUID().toString()).build(), AUDIT_TRAIL_STAGES.RR, null);
		try {
			boolean actionContainsExcel = dataBaseEntry.doesActionContainsExcel(fetchMetadataListsVO.get(0).getScriptId());
			String operatingSystem = actionContainsExcel ? "windows" : null;
			driver = driverConfiguration.getWebDriver(fetchConfigVO, operatingSystem);
			isDriverError = false;
			switchActions(args, driver, fetchMetadataListsVO, fetchConfigVO, scriptStatus, customerDetails,auditTrial);
		}
		catch (WebDriverException e) {
			if(driver == null)
			{
					String enableStatus=dataBaseEntry.getEnabledStatusByTestSetLineID(testSetLineId);	
					dataBaseEntry.updateEnabledStatusForTestSetLine(testSetId,enableStatus);
			}
		}
		catch (Exception e) {
			log.info("Exception occured while running script - {} ", fetchMetadataListsVO.get(0).getScriptNumber());
			e.printStackTrace();
			if (isDriverError) {
				FetchScriptVO post = new FetchScriptVO();
				post.setP_test_set_id(testSetId);
				post.setP_status("Fail");
				post.setP_script_id(scriptId);
				post.setP_test_set_line_id(testSetLineId);
				post.setP_pass_path(passurl);
				post.setP_fail_path(failurl);
				post.setP_exception_path(detailurl);
				post.setP_test_set_line_path(scripturl);

//				dataService.updateTestCaseStatus(post, testSetId, fetchConfigVO);
				dataBaseEntry.insertScriptExecAuditRecord(auditTrial, AUDIT_TRAIL_STAGES.DF, e.getMessage());
				dataBaseEntry.updateTestCaseEndDate(post, fetchConfigVO.getEndtime(), post.getP_status());
				dataBaseEntry.updateTestCaseStatus(post, fetchConfigVO, testLinesDetails, fetchConfigVO.getStarttime(),
						customerDetails.getTestSetName(),false);

				failList.add(scriptId);
			}
		} finally {
			dataBaseEntry.updateEnableFlagForSanity(testSetId);
			log.info("Execution is completed for script  - {}", fetchMetadataListsVO.get(0).getScriptNumber());
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
			FetchConfigVO fetchConfigVO, Map<Integer, Status> scriptStatus, CustomerProjectDto customerDetails,AuditScriptExecTrail auditTrial)
			throws Exception {

		String log4jConfPath = "log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);

		// XpathPerformance code for cases added
		long startTime = System.currentTimeMillis();
		log.info("Run started in swithch actions at ::::::::::::: {}", startTime);
		int i = 0;
		String passurl = null;
		String failurl = null;
		String actionName = null;
		String detailurl = null;
		String scripturl = null;
		String testSetId = null;
		String testSetLineId = null;
		String scriptId = null;
		String scriptId1 = null;
		String scriptNumber = null;
		String seqNum = null;
		String testScriptParamId = null;
		boolean startExcelAction = false;
		boolean isError = false;
		List<ScriptDetailsDto> excelMetadataListVO = new ArrayList<>();
		dataBaseEntry.insertScriptExecAuditRecord(auditTrial, AUDIT_TRAIL_STAGES.SES, null);
		
		try {
			scriptId = fetchMetadataListVO.get(0).getScriptId();
			passurl = fetchConfigVO.getImg_url() + customerDetails.getCustomerName() + "/"
					+ customerDetails.getProjectName() + "/" + customerDetails.getTestSetName() + "/"
					+ "Passed_Report.pdf";
			failurl = fetchConfigVO.getImg_url() + customerDetails.getCustomerName() + "/"
					+ customerDetails.getProjectName() + "/" + customerDetails.getTestSetName() + "/"
					+ "Failed_Report.pdf";
			detailurl = fetchConfigVO.getImg_url() + customerDetails.getCustomerName() + "/"
					+ customerDetails.getProjectName() + "/" + customerDetails.getTestSetName() + "/"
					+ "Detailed_Report.pdf";
			scripturl = fetchConfigVO.getImg_url() + customerDetails.getCustomerName() + "/"
					+ customerDetails.getProjectName() + "/" + customerDetails.getTestSetName() + "/"
					+ fetchMetadataListVO.get(0).getSeqNum() + "_" + fetchMetadataListVO.get(0).getScriptNumber()
					+ ".pdf";

			String userName = null;
			String globalValueForSteps = null;
			Date startdate = new Date();
			fetchConfigVO.setStarttime(startdate);
			String instanceName = fetchConfigVO.getInstance_name();
			seleniumFactory.getInstanceObjFromAbstractClass(instanceName)
					.deleteOldScreenshotForScriptFrmObjStore(fetchMetadataListVO.get(0), customerDetails);

			// XpathPerformance code for cases added
			String scriptID = fetchMetadataListVO.get(0).getScriptId();
			String checkValidScript = "Yes";

			//String checkValidScript = xpathService.checkValidScript(scriptID);

			log.info("Valid script check.......::" + checkValidScript);

			Boolean validationFlag = null;
			Map<String, String> accessTokenStorage = new HashMap<>();
			ApiValidationVO api = new ApiValidationVO();
			for (ScriptDetailsDto fetchMetadataVO : fetchMetadataListVO) {

				actionName = fetchMetadataVO.getAction();
				testSetId = customerDetails.getTestSetId();
				testSetLineId = fetchMetadataVO.getTestSetLineId();
				scriptId1 = fetchMetadataVO.getScriptId();
				scriptNumber = fetchMetadataVO.getScriptNumber();
				seqNum = fetchMetadataVO.getSeqNum();

				String screenParameter = fetchMetadataVO.getInputParameter();
				testScriptParamId = fetchMetadataVO.getTestScriptParamId();
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
						log.info("In final step of excel");
						// run excel code
						if (actionName.toLowerCase().contains("excel")) {
							excelMetadataListVO.add(fetchMetadataVO);
						}
						startExcelAction = false;
						testScriptExecService.runExcelSteps(param, excelMetadataListVO, fetchConfigVO, true,
								customerDetails,auditTrial);
						log.info("In final step of excel end-- " + excelMetadataListVO.size());
						List<Integer> stepIdList = excelMetadataListVO.stream().map(e -> e.getTestScriptParamId())
								.map(Integer::valueOf).collect(Collectors.toList());
						boolean stepPassed = dataBaseEntry.checkScriptStatusForSteps(stepIdList);
						if (!stepPassed) {
							log.info("Excel Actions failed");
							isError = true;
						}
					}
					if (actionName.toLowerCase().contains("excel")) {
						startExcelAction = true;
						log.info("Adding record to excel list");
						excelMetadataListVO.add(fetchMetadataVO);
					} else if (!isError) {
						dataBaseEntry.updateInProgressScriptLineStatus(testScriptParamId, "In-Progress");
						switch (actionName) {

						case "Login into Application":
							userName = fetchMetadataVO.getInputValue();
							log.info("Navigating to Login into Application Action");
							if (fetchMetadataVO.getInputValue() != null || fetchMetadataVO.getInputValue().equals("")) {
								seleniumFactory.getInstanceObj(instanceName).loginApplication(driver, fetchConfigVO,
										fetchMetadataVO, type1, type2, type3, param1, param2, param3,
										fetchMetadataVO.getInputValue(),
										dataBaseEntry.getPassword(param, userName, fetchConfigVO), customerDetails);
								userName = null;
								break;
							} else {
								break;
							}
						case "Login into SFApplication":
							userName = fetchMetadataVO.getInputValue();
							log.info("Navigating to Login into SFApplication Action");
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
						case "Login into SSOApplication":
							userName = fetchMetadataVO.getInputValue();
							log.info("Navigating to Login into Application Action");
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
							log.info("Navigating to Login into (OIC)Application Action");
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
							log.info("Navigating to Login into Application Action");
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
							log.info("Navigating to Navigate Action");
							seleniumFactory.getInstanceObj(instanceName).navigate(driver, fetchConfigVO,
									fetchMetadataVO, type1, type2, param1, param2, null, count, customerDetails);
							break;

						case "Click Menu(OIC)":
							seleniumFactory.getInstanceObj(instanceName).oicClickMenu(driver, param1, param2,
									fetchMetadataVO, fetchConfigVO, customerDetails);
							break;
						case "Navigate(OIC)":
							log.info("Navigating to Navigate Action");
							seleniumFactory.getInstanceObj(instanceName).oicNavigate(driver, fetchConfigVO,
									fetchMetadataVO, type1, type2, param1, param2, count, customerDetails);
							break;

						case "Logout(OIC)":
							seleniumFactory.getInstanceObj(instanceName).oicLogout(driver, fetchConfigVO,
									fetchMetadataVO, type1, type2, type3, param1, param2, param3, customerDetails);
							break;

						case "openTask":
							log.info("Navigating to openTask Action");
							seleniumFactory.getInstanceObj(instanceName).openTask(driver, fetchConfigVO,
									fetchMetadataVO, type1, type2, param1, param2, count, customerDetails);
							break;

						case "Logout":
							seleniumFactory.getInstanceObj(instanceName).logout(driver, fetchConfigVO, fetchMetadataVO,
									type1, type2, type3, param1, param2, param3, customerDetails);
							break;

						// XpathPerformance code for cases added

						case "SendKeys":
							if (fetchMetadataVO.getInputValue() != null || fetchMetadataVO.getInputValue() == "") {
								try {
									if (checkValidScript.equalsIgnoreCase("Yes")) {

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
							seleniumFactory.getInstanceObj(instanceName).clickExpandorcollapse(driver, param1, param2,
									fetchMetadataVO, fetchConfigVO, customerDetails);
							break;
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
									if (checkValidScript.equalsIgnoreCase("Yes")) {

										xpathPerformance.textarea(driver, param1, param2,
												fetchMetadataVO.getInputValue(), fetchMetadataVO, fetchConfigVO, count);
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

							}

									case "Dropdown Values":
							seleniumFactory.getInstanceObj(instanceName).dropdownValues(driver, param1, param2, param3,
									fetchMetadataVO.getInputValue(), fetchMetadataVO, fetchConfigVO, customerDetails);
							break;
						case "Table SendKeys":
							if (fetchMetadataVO.getInputValue() != null || fetchMetadataVO.getInputValue() == "") {
								try {
									if (checkValidScript.equalsIgnoreCase("Yes")) {

										xpathPerformance.tableSendKeys(driver, param1, param2, param3,
												fetchMetadataVO.getInputValue(), fetchMetadataVO, fetchConfigVO, count);
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

							}

						case "multiplelinestableSendKeys":
							if (fetchMetadataVO.getInputValue() != null || fetchMetadataVO.getInputValue() == "") {
								try {
									if (checkValidScript.equalsIgnoreCase("Yes")) {

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

							}
						case "Table Dropdown Values":
							if (fetchMetadataVO.getInputValue() != null || fetchMetadataVO.getInputValue() == "") {
								try {
									if (checkValidScript.equalsIgnoreCase("Yes")) {

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
							}

						case "clickLinkAction":
							if (fetchMetadataVO.getInputValue() != null || fetchMetadataVO.getInputValue() == "") {
								try {
									if (checkValidScript.equalsIgnoreCase("Yes")) {
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

							}
							


						case "clickCheckbox":
							if (fetchMetadataVO.getInputValue() != null || fetchMetadataVO.getInputValue() == "") {
								try {
									if (checkValidScript.equalsIgnoreCase("Yes")) {
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
									if (checkValidScript.equalsIgnoreCase("Yes")) {
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
									if (checkValidScript.equalsIgnoreCase("Yes")) {

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
								if (checkValidScript.equalsIgnoreCase("Yes")) {

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
								if (checkValidScript.equalsIgnoreCase("Yes")) {

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
							seleniumFactory.getInstanceObj(instanceName).clickNotificationLink(driver, param1, param2,
									fetchMetadataVO, fetchConfigVO, customerDetails);
							break;

						case "clickMenu":
							try {
								if (checkValidScript.equalsIgnoreCase("Yes")) {
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
								if (checkValidScript.equalsIgnoreCase("Yes")) {

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
								if (checkValidScript.equalsIgnoreCase("Yes")) {

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
								if (checkValidScript.equalsIgnoreCase("Yes")) {

									xpathPerformance.clickButton(driver, param1, param2, fetchMetadataVO, fetchConfigVO,
											count, customerDetails);
									break;
								} else {

									throw new Exception("ScriptNotValid");
								}
							} catch (Exception e) {
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
								seleniumFactory.getInstanceObj(instanceName).fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
								break;
							}

						case "tableRowSelect":
							try {
								if (checkValidScript.equalsIgnoreCase("Yes")) {

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
									if (checkValidScript.equalsIgnoreCase("Yes")) {

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
								if (checkValidScript.equalsIgnoreCase("Yes")) {
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
								if (checkValidScript.equalsIgnoreCase("Yes")) {
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
							seleniumFactory.getInstanceObj(instanceName).moveToElement(driver,
									fetchMetadataVO.getInputParameter(), fetchMetadataVO, fetchConfigVO);
							break;
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
							seleniumFactory.getInstanceObj(instanceName).clickFilter(driver, param1, param2,
									fetchMetadataVO, fetchConfigVO, customerDetails);
							break;

						case "switchToFrame":
							try {
								if (checkValidScript.equalsIgnoreCase("Yes")) {

									xpathPerformance.switchToFrame(driver, fetchMetadataVO.getInputParameter(),
											fetchMetadataVO, fetchConfigVO, count);
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
									if (checkValidScript.equalsIgnoreCase("Yes")) {

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
							globalValueForSteps = seleniumFactory.getInstanceObj(instanceName).copynumber(driver,
									param1, param2, fetchMetadataVO, fetchConfigVO, customerDetails);
							break;
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
								if (checkValidScript.equalsIgnoreCase("Yes")) {

									xpathPerformance.clear(driver, param1, param2, fetchMetadataVO, fetchConfigVO,
											count);
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
							try {
								seleniumFactory.getInstanceObj(instanceName).uploadFileAutoIT(driver,
										fetchConfigVO.getUpload_file_path(), param1, param2, param3);
								seleniumFactory.getInstanceObj(instanceName).screenshot(driver, fetchMetadataVO,
										customerDetails);
								break;
							} catch (Exception e) {
								seleniumFactory.getInstanceObj(instanceName).screenshotFail(driver, fetchMetadataVO,
										customerDetails);
								throw e;
							}
							
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
								seleniumFactory.getInstanceObj(instanceName).datePicker(driver, param1, param2,
										fetchMetadataVO.getInputValue(), fetchMetadataVO, fetchConfigVO,
										customerDetails);
								break;
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
							seleniumFactory.getInstanceObj(instanceName).multipleSendKeys(driver, param1, param2,
									value1, value2, fetchMetadataVO, fetchConfigVO, customerDetails);
							break;

						case "Login into Application(Informatica)":
							userName = fetchMetadataVO.getInputValue();
							log.info("Navigating to Login into Application Action");
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
							seleniumFactory.getInstanceObj(instanceName).waitTillLoad(driver, param1, param2,
									fetchMetadataVO, fetchConfigVO);
							break;

						case "compareValue":
							seleniumFactory.getInstanceObj(instanceName).compareValue(driver,
									fetchMetadataVO.getInputParameter(), fetchMetadataVO, fetchConfigVO,
									globalValueForSteps, customerDetails);
							break;

						case "apiAccessToken":
							seleniumFactory.getInstanceObjFromAbstractClass(fetchConfigVO.getInstance_name()).apiAccessToken(fetchMetadataVO,
									accessTokenStorage, customerDetails);
							break;

						case "apiValidationResponse":
							seleniumFactory.getInstanceObjFromAbstractClass(fetchConfigVO.getInstance_name()).apiValidationResponse(fetchMetadataVO,
									accessTokenStorage, api,customerDetails,fetchConfigVO);
							break;

						case "validation":
							seleniumFactory.getInstanceObjFromAbstractClass(fetchConfigVO.getInstance_name()).validation(fetchMetadataVO, api);
							break;

						default:
							System.out.println("Action Name is not matched with" + "" + actionName);
							break;

						}
						fetchConfigVO.setStatus1("Pass");
						System.out.println("Successfully Executed the" + "" + actionName);
						try {
							dataBaseEntry.updatePassedScriptLineStatus(fetchMetadataVO, fetchConfigVO,
									testScriptParamId, "Pass");
							fetchMetadataVO.setStatus("Pass");
						} catch (Exception e) {
							System.out.println("e");
						}
					}

					if (fetchMetadataListVO.size() == i && !isError) {
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
							System.out.println("endTime:::::::::::::" + endTime);

							long gap = startTime - endTime;
							System.out.println("gap:::::::::::::" + gap);

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
							seleniumFactory.getInstanceObjFromAbstractClass(fetchConfigVO.getInstance_name()).createPdf(
									fetchMetadataListVO, fetchConfigVO, seqNum + "_" + scriptNumber + ".pdf",
									customerDetails);

							if ("SHAREPOINT".equalsIgnoreCase(fetchConfigVO.getPDF_LOCATION())) {
								seleniumFactory.getInstanceObj(fetchConfigVO.getInstance_name())
										.uploadPDF(fetchMetadataListVO, fetchConfigVO, customerDetails);
							}
					}

				} catch (Exception e) {
					e.printStackTrace();
					if (scriptStatus.containsKey(Integer.parseInt(fetchMetadataVO.getScriptId()))) {
						Status s = scriptStatus.get(Integer.parseInt(fetchMetadataVO.getScriptId()));
						s.setStatusMsg("Fail");
					}
					System.out.println("Failed to Execute the " + "" + actionName);
					System.out.println(
							"Error occurred in TestCaseName=" + actionName + "" + "Exception=" + "" + e.getMessage());
					errorMessagesHandler.getError(actionName, fetchMetadataVO, fetchConfigVO, testScriptParamId,
							message, param1, param2, dataBaseEntry.getPassword(param, userName, fetchConfigVO));
					isError = true;
				}
				if (isError) {
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
						seleniumFactory.getInstanceObjFromAbstractClass(fetchConfigVO.getInstance_name()).createPdf(
								fetchMetadataListVO, fetchConfigVO,
								seqNum + "_" + scriptNumber + "_RUN" + failedScriptRunCount + ".pdf",
								customerDetails);
						if ("SHAREPOINT".equalsIgnoreCase(fetchConfigVO.getPDF_LOCATION())) {
							seleniumFactory.getInstanceObj(fetchConfigVO.getInstance_name())
									.uploadPDF(fetchMetadataListVO, fetchConfigVO, customerDetails);
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
		seleniumFactory.getInstanceObjFromAbstractClass(fetchConfigVO.getInstance_name())
				.downloadScreenshotsFromObjectStore(screenShotFolder, customerDetails.getCustomerName(),
						customerDetails.getTestSetName(), seqNumber);
	}
	

}
