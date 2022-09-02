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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
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
import com.winfo.utils.Constants.BOOLEAN_STATUS;
import com.winfo.vo.ApiValidationVO;
import com.winfo.vo.ResponseDto;
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
			List<FetchMetadataVO> fetchMetadataListVO = dataBaseEntry.getMetaDataVOList(testSetId, null, false, true);
			SortedMap<Integer, List<FetchMetadataVO>> dependentScriptMap = new TreeMap<Integer, List<FetchMetadataVO>>();
			SortedMap<Integer, List<FetchMetadataVO>> metaDataMap = dataService.prepareTestcasedata(fetchMetadataListVO,
					dependentScriptMap);
			// Independent
			for (Entry<Integer, List<FetchMetadataVO>> metaData : metaDataMap.entrySet()) {
				log.info(" Running Independent - " + metaData.getKey());
				testScriptExecService.executorMethodPyJab(testSetId, fetchConfigVO, metaData, true);
			}

			ExecutorService executordependent = Executors.newFixedThreadPool(fetchConfigVO.getParallel_dependent());
			for (Entry<Integer, List<FetchMetadataVO>> metaData : dependentScriptMap.entrySet()) {
				log.info(" Running Dependent - " + metaData.getKey());
				executordependent.execute(() -> {
					log.info(" Running Dependent in executor - " + metaData.getKey());
					boolean run = dataBaseEntry.checkRunStatusOfTestRunLevelDependantScript(
							metaData.getValue().get(0).getDependencyScriptNumber());
					log.info(" Dependant Script run status" + metaData.getValue().get(0).getScript_id() + " " + run);
					testScriptExecService.executorMethodPyJab(testSetId, fetchConfigVO, metaData, run);
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

	public ResponseDto cloudRun(String args) throws MalformedURLException {
		ResponseDto executeTestrunVo = new ResponseDto();
		log.info("Start of cloud run method");
		try {
			FetchConfigVO fetchConfigVO = testScriptExecService.fetchConfigVO(args);
			List<FetchMetadataVO> fetchMetadataListVO = dataBaseEntry.getMetaDataVOList(args, null, false, true);
			SortedMap<Integer, List<FetchMetadataVO>> dependentScriptMap = new TreeMap<Integer, List<FetchMetadataVO>>();
			SortedMap<Integer, List<FetchMetadataVO>> metaDataMap = dataService.prepareTestcasedata(fetchMetadataListVO,
					dependentScriptMap);
			Map<Integer, Status> scriptStatus = new HashMap<>();

			Map<Integer, Boolean> mutableMap = limitScriptExecutionService.getLimitedConditionException(fetchConfigVO,
					fetchMetadataListVO, metaDataMap, args);

			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
			Date startDate = sdf.parse(fetchConfigVO.getStart_date());
			Date endDate = sdf.parse(fetchConfigVO.getEnd_date());

			for (Entry<Integer, Boolean> entryMap : mutableMap.entrySet()) {
				if (entryMap.getValue() || date.after(endDate) || date.before(startDate)) {
					executeTestrunVo.setStatusCode(404);
					executeTestrunVo.setStatusMessage("ERROR");
					if (entryMap.getKey() > 0) {
						executeTestrunVo.setStatusDescr(
								"Your request could not be processed as you have reached the scripts execution threshold. You can run only run "
										+ entryMap.getKey()
										+ " more scripts. Reach out to the WATS support team to enhance the limit..");
					} else if (date.after(endDate) || date.before(startDate)) {
						executeTestrunVo.setStatusDescr(
								"Your request could not be processed the Testrun, please check with the Start and End Date");

					} else {
						executeTestrunVo.setStatusDescr(
								"Your request could not be processed as you have reached the scripts execution threshold. Reach out to the WATS support team to enhance the limit..");

					}
					return executeTestrunVo;

				}
			}

			fetchConfigVO.setStarttime1(date);

			log.info("Independent scripts # - {} ", metaDataMap.toString());
			ExecutorService executor = Executors.newFixedThreadPool(fetchConfigVO.getParallel_independent());
			try {
				for (Entry<Integer, List<FetchMetadataVO>> metaData : metaDataMap.entrySet()) {

					String scriptNumber = metaData.getValue().get(0).getScript_number();

					executor.execute(() -> {
						log.info("Start of Independent Script Execution of {}", scriptNumber);
						try {
							long starttimeIntermediate = System.currentTimeMillis();
							String flag = dataBaseEntry.getTrMode(args, fetchConfigVO);
							if (flag.equalsIgnoreCase("STOPPED")) {
								metaData.getValue().clear();
								executor.shutdown();
								log.info("Test run is STOPPED - Scripts will only run when Test Run status is ACTIVE");
							} else {
								executorMethod(args, fetchConfigVO, fetchMetadataListVO, metaData, scriptStatus);
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

					for (Entry<Integer, List<FetchMetadataVO>> metaData : dependentScriptMap.entrySet()) {
						log.info(" Running Dependent - " + metaData.getKey());
						executordependent.execute(() -> {
							log.info(" Running Dependent in executor - " + metaData.getKey());
							boolean run;
							run = dataBaseEntry.checkRunStatusOfTestRunLevelDependantScript(
									metaData.getValue().get(0).getDependencyScriptNumber());
							log.info(" Dependant Script run status" + metaData.getValue().get(0).getScript_id() + " "
									+ run);

							try {
								String flag = dataBaseEntry.getTrMode(args, fetchConfigVO);
								if (flag.equalsIgnoreCase("STOPPED")) {
									metaData.getValue().clear();
									executor.shutdown();
									System.out.println("treminattion is succeed");
								} else {
									if (run) {
										executorMethod(args, fetchConfigVO, fetchMetadataListVO, metaData,
												scriptStatus);
									} else {
										String passurl = fetchConfigVO.getImg_url()
												+ fetchMetadataListVO.get(0).getCustomer_name() + "/"
												+ fetchMetadataListVO.get(0).getProject_name() + "/"
												+ fetchMetadataListVO.get(0).getTest_run_name() + "/"
												+ "Passed_Report.pdf";
										String failurl = fetchConfigVO.getImg_url()
												+ fetchMetadataListVO.get(0).getCustomer_name() + "/"
												+ fetchMetadataListVO.get(0).getProject_name() + "/"
												+ fetchMetadataListVO.get(0).getTest_run_name() + "/"
												+ "Failed_Report.pdf";
										String detailurl = fetchConfigVO.getImg_url()
												+ fetchMetadataListVO.get(0).getCustomer_name() + "/"
												+ fetchMetadataListVO.get(0).getProject_name() + "/"
												+ fetchMetadataListVO.get(0).getTest_run_name() + "/"
												+ "Detailed_Report.pdf";
										String scripturl = fetchConfigVO.getImg_url()
												+ fetchMetadataListVO.get(0).getCustomer_name() + "/"
												+ fetchMetadataListVO.get(0).getProject_name() + "/"
												+ fetchMetadataListVO.get(0).getTest_run_name() + "/"
												+ fetchMetadataListVO.get(0).getSeq_num() + "_"
												+ fetchMetadataListVO.get(0).getScript_number() + ".pdf";

										FetchMetadataVO fd = metaData.getValue().get(0);
										FetchScriptVO post = new FetchScriptVO();
										post.setP_test_set_id(fd.getTest_set_id());
										post.setP_status("Fail");
										post.setP_script_id(fd.getScript_id());
										post.setP_test_set_line_id(fd.getTest_set_line_id());
										post.setP_pass_path(passurl);
										post.setP_fail_path(failurl);
										post.setP_exception_path(detailurl);
										post.setP_test_set_line_path(scripturl);
										failcount = failcount + 1;
										System.out.println("Came here to check fail condition");
										dataService.updateTestCaseStatus(post, args, fetchConfigVO);
										// dataBaseEntry.updateEndTime(fetchConfigVO,fd.getTest_set_line_id(),fd.getTest_set_id(),
										// enddate);
										int failedScriptRunCount = limitScriptExecutionService
												.getFailedScriptRunCount(fd.getTest_set_line_id(), fd.getTest_set_id());

										errorMessagesHandler.getError("Dependency Fail", fd, fetchConfigVO,
												fd.getTest_script_param_id(), null, null, null, null);

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
				downloadScreenShot(fetchConfigVO, fetchMetadataListVO.get(0), true);
				List<FetchMetadataVO> fetchMetadataListVOforEvidence = dataBaseEntry.getMetaDataVOList(args, null, true,
						false);

				seleniumFactory.getInstanceObjFromAbstractClass(fetchConfigVO.getInstance_name())
						.createPdf(fetchMetadataListVOforEvidence, fetchConfigVO, "Passed_Report.pdf", null, null);
				seleniumFactory.getInstanceObjFromAbstractClass(fetchConfigVO.getInstance_name())
						.createPdf(fetchMetadataListVOforEvidence, fetchConfigVO, "Failed_Report.pdf", null, null);
				seleniumFactory.getInstanceObjFromAbstractClass(fetchConfigVO.getInstance_name())
						.createPdf(fetchMetadataListVOforEvidence, fetchConfigVO, "Detailed_Report.pdf", null, null);
				increment = 0;

				if ("SHAREPOINT".equalsIgnoreCase(fetchConfigVO.getPDF_LOCATION())) {
					seleniumFactory.getInstanceObj(fetchConfigVO.getInstance_name()).uploadPDF(fetchMetadataListVO,
							fetchConfigVO);
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
			e.printStackTrace();
			System.out.println("Error in Cloud test run method");
			FetchScriptVO post = new FetchScriptVO();
			post.setP_status("Fail");
			dataService.updateTestCaseStatus(post, args, null);
		}
		return executeTestrunVo;
	}

	public void executorMethod(String args, FetchConfigVO fetchConfigVO, List<FetchMetadataVO> fetchMetadataListVO,
			Entry<Integer, List<FetchMetadataVO>> metaData, Map<Integer, Status> scriptStatus) throws Exception {
		List<String> failList = new ArrayList<>();
		WebDriver driver = null;
		List<FetchMetadataVO> fetchMetadataListsVO = metaData.getValue();
		String testSetId = fetchMetadataListsVO.get(0).getTest_set_id();
		String testSetLineId = fetchMetadataListsVO.get(0).getTest_set_line_id();

		String scriptId = fetchMetadataListsVO.get(0).getScript_id();
		String passurl = fetchConfigVO.getImg_url() + fetchMetadataListsVO.get(0).getCustomer_name() + "/"
				+ fetchMetadataListsVO.get(0).getProject_name() + "/" + fetchMetadataListsVO.get(0).getTest_run_name()
				+ "/" + "Passed_Report.pdf";
		String failurl = fetchConfigVO.getImg_url() + fetchMetadataListsVO.get(0).getCustomer_name() + "/"
				+ fetchMetadataListsVO.get(0).getProject_name() + "/" + fetchMetadataListsVO.get(0).getTest_run_name()
				+ "/" + "Failed_Report.pdf";
		String detailurl = fetchConfigVO.getImg_url() + fetchMetadataListsVO.get(0).getCustomer_name() + "/"
				+ fetchMetadataListsVO.get(0).getProject_name() + "/" + fetchMetadataListsVO.get(0).getTest_run_name()
				+ "/" + "Detailed_Report.pdf";
		String scripturl = fetchConfigVO.getImg_url() + fetchMetadataListsVO.get(0).getCustomer_name() + "/"
				+ fetchMetadataListsVO.get(0).getProject_name() + "/" + fetchMetadataListsVO.get(0).getTest_run_name()
				+ "/" + fetchMetadataListsVO.get(0).getSeq_num() + "_" + fetchMetadataListsVO.get(0).getScript_number()
				+ ".pdf";
		log.info("Pass Url - {}", passurl);
		log.info("Fail Url - {}", failurl);
		log.info("Detailed Url - {}", detailurl);
		boolean isDriverError = true;
		try {
			boolean actionContainsExcel = dataBaseEntry
					.checkActionContainsExcel(fetchMetadataListsVO.get(0).getScript_id());
			String operatingSystem = actionContainsExcel ? "windows" : null;
			driver = driverConfiguration.getWebDriver(fetchConfigVO, operatingSystem);
			isDriverError = false;
			switchActions(args, driver, fetchMetadataListsVO, fetchConfigVO, scriptStatus);
		} catch (Exception e) {
			log.info("Exception occured while running script - {} ", fetchMetadataListsVO.get(0).getScript_number());
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
				dataService.updateTestCaseStatus(post, args, fetchConfigVO);
				failList.add(scriptId);
			}
		} finally {
			log.info("Execution is completed for script  - {}", fetchMetadataListsVO.get(0).getScript_number());
			if (driver != null) {
				driver.quit();
			}
		}
	}

	int passcount = 0;
	int failcount = 0;

	public void switchActions(String param, WebDriver driver, List<FetchMetadataVO> fetchMetadataListVO,
			FetchConfigVO fetchConfigVO, Map<Integer, Status> scriptStatus) throws Exception {

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
		String test_set_id = null;
		String test_set_line_id = null;
		String script_id = null;
		String script_id1 = null;
		String script_Number = null;
		String seq_num = null;
		String test_script_param_id = null;
		boolean startExcelAction = false;
		boolean isError = false;
		// String start_time=null;
		// String end_time=null;
		List<FetchMetadataVO> excelMetadataListVO = new ArrayList<>();

		try {
			script_id = fetchMetadataListVO.get(0).getScript_id();
			passurl = fetchConfigVO.getImg_url() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
					+ fetchMetadataListVO.get(0).getProject_name() + "/" + fetchMetadataListVO.get(0).getTest_run_name()
					+ "/" + "Passed_Report.pdf";
			failurl = fetchConfigVO.getImg_url() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
					+ fetchMetadataListVO.get(0).getProject_name() + "/" + fetchMetadataListVO.get(0).getTest_run_name()
					+ "/" + "Failed_Report.pdf";
			detailurl = fetchConfigVO.getImg_url() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
					+ fetchMetadataListVO.get(0).getProject_name() + "/" + fetchMetadataListVO.get(0).getTest_run_name()
					+ "/" + "Detailed_Report.pdf";
			scripturl = fetchConfigVO.getImg_url() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
					+ fetchMetadataListVO.get(0).getProject_name() + "/" + fetchMetadataListVO.get(0).getTest_run_name()
					+ "/" + fetchMetadataListVO.get(0).getSeq_num() + "_"
					+ fetchMetadataListVO.get(0).getScript_number() + ".pdf";

			String userName = null;
			String globalValueForSteps = null;
			Date startdate = new Date();
			fetchConfigVO.setStarttime(startdate);
			String instanceName = fetchConfigVO.getInstance_name();
			seleniumFactory.getInstanceObj(instanceName).DelatedScreenshoots(fetchMetadataListVO, fetchConfigVO);

			// XpathPerformance code for cases added
			String scriptID = fetchMetadataListVO.get(0).getScript_id();
			String checkValidScript = xpathService.checkValidScript(scriptID);
			log.info("Valid script check.......::" + checkValidScript);
			
			Boolean validationFlag = null;
			Map<String,String> accessTokenStorage = new HashMap<>();
			ApiValidationVO api = new ApiValidationVO();
			for (FetchMetadataVO fetchMetadataVO : fetchMetadataListVO) {

				actionName = fetchMetadataVO.getAction();
				test_set_id = fetchMetadataVO.getTest_set_id();
				test_set_line_id = fetchMetadataVO.getTest_set_line_id();
				script_id1 = fetchMetadataVO.getScript_id();
				script_Number = fetchMetadataVO.getScript_number();
				seq_num = fetchMetadataVO.getSeq_num();

				String screenParameter = fetchMetadataVO.getInput_parameter();
				test_script_param_id = fetchMetadataVO.getTest_script_param_id();
				if (i == 0) {
					dataBaseEntry.updateInProgressScriptStatus(fetchConfigVO, test_set_id, test_set_line_id);
					dataBaseEntry.updateStartTime(fetchConfigVO, test_set_line_id, test_set_id, startdate);
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
					String actionType = fetchMetadataVO.getField_type();
					type1 = actionType != null ? actionType.split(">").length > 0 ? actionType.split(">")[0] : "" : "";
					type2 = actionType != null ? actionType.split(">").length > 1 ? actionType.split(">")[1] : "" : "";
					type3 = actionType != null ? actionType.split(">").length > 2 ? actionType.split(">")[2] : "" : "";
					String getValue = fetchMetadataVO.getInput_value();
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
						testScriptExecService.runExcelSteps(param, excelMetadataListVO, fetchConfigVO, true);
						log.info("In final step of excel end-- " + excelMetadataListVO.size());
						List<Integer> stepIdList = excelMetadataListVO.stream().map(e -> e.getTest_script_param_id())
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
						dataBaseEntry.updateInProgressScriptLineStatus(fetchMetadataVO, fetchConfigVO,
								test_script_param_id, "In-Progress");
						switch (actionName) {

						case "Login into Application":
							userName = fetchMetadataVO.getInput_value();
							log.info("Navigating to Login into Application Action");
							if (fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
								seleniumFactory.getInstanceObj(instanceName).loginApplication(driver, fetchConfigVO,
										fetchMetadataVO, type1, type2, type3, param1, param2, param3,
										fetchMetadataVO.getInput_value(),
										dataBaseEntry.getPassword(param, userName, fetchConfigVO));
								userName = null;
								break;
							} else {
								break;
							}
						case "Login into SSOApplication":
							userName = fetchMetadataVO.getInput_value();
							log.info("Navigating to Login into Application Action");
							if (fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
								seleniumFactory.getInstanceObj(instanceName).loginSSOApplication(driver, fetchConfigVO,
										fetchMetadataVO, type1, type2, type3, param1, param2, param3,
										fetchMetadataVO.getInput_value(),
										dataBaseEntry.getPassword(param, userName, fetchConfigVO));
								userName = null;
								break;
							} else {
								break;
							}
						case "Login into Application(OIC)":
							userName = fetchMetadataVO.getInput_value();
							log.info("Navigating to Login into Application Action");
							if (fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
								seleniumFactory.getInstanceObj(instanceName).loginOicApplication(driver, fetchConfigVO,
										fetchMetadataVO, type1, type2, type3, param1, param2, param3,
										fetchMetadataVO.getInput_value(),
										dataBaseEntry.getPassword(param, userName, fetchConfigVO));
								userName = null;
								break;
							} else {
								break;
							}

						case "Login into Application(jobscheduler)":
							userName = fetchMetadataVO.getInput_value();
							log.info("Navigating to Login into Application Action");
							if (fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
								seleniumFactory.getInstanceObj(instanceName).loginOicJob(driver, fetchConfigVO,
										fetchMetadataVO, type1, type2, type3, param1, param2, param3,
										fetchMetadataVO.getInput_value(),
										dataBaseEntry.getPassword(param, userName, fetchConfigVO));
								userName = null;
								break;
							} else {
								break;
							}

						case "Navigate":
							log.info("Navigating to Navigate Action");
							seleniumFactory.getInstanceObj(instanceName).navigate(driver, fetchConfigVO,
									fetchMetadataVO, type1, type2, param1, param2, count);
							break;

						case "Click Menu(OIC)":
							seleniumFactory.getInstanceObj(instanceName).oicClickMenu(driver, param1, param2,
									fetchMetadataVO, fetchConfigVO);
							break;
						case "Navigate(OIC)":
							log.info("Navigating to Navigate Action");
							seleniumFactory.getInstanceObj(instanceName).oicNavigate(driver, fetchConfigVO,
									fetchMetadataVO, type1, type2, param1, param2, count);
							break;

						case "Logout(OIC)":
							seleniumFactory.getInstanceObj(instanceName).oicLogout(driver, fetchConfigVO,
									fetchMetadataVO, type1, type2, type3, param1, param2, param3);
							break;

						case "openTask":
							log.info("Navigating to openTask Action");
							seleniumFactory.getInstanceObj(instanceName).openTask(driver, fetchConfigVO,
									fetchMetadataVO, type1, type2, param1, param2, count);
							break;

						case "Logout":
							seleniumFactory.getInstanceObj(instanceName).logout(driver, fetchConfigVO, fetchMetadataVO,
									type1, type2, type3, param1, param2, param3);
							break;

						// XpathPerformance code for cases added

						case "SendKeys":
							if (fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
								try {
									if (checkValidScript.equalsIgnoreCase("Yes")) {

										xpathPerformance.sendValue(driver, param1, param2,
												fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO,
												count);
										break;
									} else {

										throw new Exception("ScriptNotValid");
									}
								} catch (Exception e) {
									seleniumFactory.getInstanceObj(instanceName).sendValue(driver, param1, param2,
											fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
									break;
								}

							}

						case "sendvalues(OIC)":
							if (fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
								seleniumFactory.getInstanceObj(instanceName).oicSendValue(driver, param1, param2,
										fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
								break;
							} else {
								break;
							}

						case "clickExpandorcollapse":
							seleniumFactory.getInstanceObj(instanceName).clickExpandorcollapse(driver, param1, param2,
									fetchMetadataVO, fetchConfigVO);
							break;
						case "clickButton(OIC)":
							seleniumFactory.getInstanceObj(instanceName).oicClickButton(driver, param1, param2,
									fetchMetadataVO, fetchConfigVO);
							break;
						case "Mouse Hover(OIC)":
							seleniumFactory.getInstanceObj(instanceName).oicMouseHover(driver, param1, param2,
									fetchMetadataVO, fetchConfigVO);
							break;

						case "textarea":
							if (fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
								try {
									if (checkValidScript.equalsIgnoreCase("Yes")) {

										xpathPerformance.textarea(driver, param1, param2,
												fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO,
												count);
										break;
									} else {

										throw new Exception("ScriptNotValid");
									}
								} catch (Exception e) {
									seleniumFactory.getInstanceObj(instanceName).textarea(driver, param1, param2,
											fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
									break;
								}

							}
						case "Dropdown Values":
							seleniumFactory.getInstanceObj(instanceName).dropdownValues(driver, param1, param2, param3,
									fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
							break;

						case "Table SendKeys":
							if (fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
								try {
									if (checkValidScript.equalsIgnoreCase("Yes")) {

										xpathPerformance.tableSendKeys(driver, param1, param2, param3,
												fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO,
												count);
										break;
									} else {

										throw new Exception("ScriptNotValid");
									}
								} catch (Exception e) {
									seleniumFactory.getInstanceObj(instanceName).tableSendKeys(driver, param1, param2,
											param3, fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
									break;
								}

							}

						case "multiplelinestableSendKeys":
							if (fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
								try {
									if (checkValidScript.equalsIgnoreCase("Yes")) {

										xpathPerformance.multiplelinestableSendKeys(driver, param1, param2, param3,
												fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO,
												count);
										break;
									} else {

										throw new Exception("ScriptNotValid");
									}
								} catch (Exception e) {
									seleniumFactory.getInstanceObj(instanceName).multiplelinestableSendKeys(driver,
											param1, param2, param3, fetchMetadataVO.getInput_value(), fetchMetadataVO,
											fetchConfigVO);
									break;
								}
							}
						case "Table Dropdown Values":
							if (fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
								try {
									if (checkValidScript.equalsIgnoreCase("Yes")) {

										xpathPerformance.tableDropdownValues(driver, param1, param2,
												fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO,
												count);
										break;
									} else {

										throw new Exception("ScriptNotValid");
									}
								} catch (Exception e) {
									seleniumFactory.getInstanceObj(instanceName).tableDropdownValues(driver, param1,
											param2, fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
									break;
								}

							}

						case "clickLinkAction":
							if (fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
								try {
									if (checkValidScript.equalsIgnoreCase("Yes")) {
										xpathPerformance.clickLinkAction(driver, param1, param2,
												fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO,
												count);
										break;
									} else {

										throw new Exception("ScriptNotValid");
									}
								} catch (Exception e) {
									seleniumFactory.getInstanceObj(instanceName).clickLinkAction(driver, param1, param2,
											fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
									break;
								}

							}

						case "clickCheckbox":
							if (fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
								try {
									if (checkValidScript.equalsIgnoreCase("Yes")) {
										xpathPerformance.clickCheckbox(driver, param1, fetchMetadataVO.getInput_value(),
												fetchMetadataVO, fetchConfigVO, count);
										break;
									} else {

										throw new Exception("ScriptNotValid");
									}
								} catch (Exception e) {
									seleniumFactory.getInstanceObj(instanceName).clickCheckbox(driver, param1,
											fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
									break;
								}

							}

						case "clickRadiobutton":
							if (fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
								try {
									if (checkValidScript.equalsIgnoreCase("Yes")) {
										xpathPerformance.clickRadiobutton(driver, param1, param2,
												fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO,
												count);
										break;
									} else {

										throw new Exception("ScriptNotValid");
									}
								} catch (Exception e) {
									seleniumFactory.getInstanceObj(instanceName).clickRadiobutton(driver, param1,
											param2, fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
									break;
								}

							}
						case "selectAValue":
							if (fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
								try {
									if (checkValidScript.equalsIgnoreCase("Yes")) {

										xpathPerformance.selectAValue(driver, param1, param2,
												fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO,
												count);
										break;
									} else {

										throw new Exception("ScriptNotValid");
									}
								} catch (Exception e) {
									seleniumFactory.getInstanceObj(instanceName).selectAValue(driver, param1, param2,
											fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
									break;
								}

							}

						case "clickTableLink":
							try {
								if (checkValidScript.equalsIgnoreCase("Yes")) {

									xpathPerformance.clickTableLink(driver, param1, param2, fetchMetadataVO,
											fetchConfigVO, count);
									break;
								} else {

									throw new Exception("ScriptNotValid");
								}
							} catch (Exception e) {
								seleniumFactory.getInstanceObj(instanceName).clickTableLink(driver, param1, param2,
										fetchMetadataVO, fetchConfigVO);
								break;
							}

						case "clickLink":
							try {
								if (checkValidScript.equalsIgnoreCase("Yes")) {

									xpathPerformance.clickLink(driver, param1, param2, fetchMetadataVO, fetchConfigVO,
											count);
									break;
								} else {

									throw new Exception("ScriptNotValid");
								}
							} catch (Exception e) {
								seleniumFactory.getInstanceObj(instanceName).clickLink(driver, param1, param2,
										fetchMetadataVO, fetchConfigVO);
								break;
							}

						case "clickNotificationLink":
							seleniumFactory.getInstanceObj(instanceName).clickNotificationLink(driver, param1, param2,
									fetchMetadataVO, fetchConfigVO);
							break;

						case "clickMenu":
							try {
								if (checkValidScript.equalsIgnoreCase("Yes")) {
									xpathPerformance.clickMenu(driver, param1, param2, fetchMetadataVO, fetchConfigVO,
											count);
									break;
								} else {

									throw new Exception("ScriptNotValid");
								}
							} catch (Exception e) {

								seleniumFactory.getInstanceObj(instanceName).clickMenu(driver, param1, param2,
										fetchMetadataVO, fetchConfigVO);
								break;
							}

						case "clickImage":
							try {
								if (checkValidScript.equalsIgnoreCase("Yes")) {

									xpathPerformance.clickImage(driver, param1, param2, fetchMetadataVO, fetchConfigVO,
											count);
									break;
								} else {

									throw new Exception("ScriptNotValid");
								}
							} catch (Exception e) {
								seleniumFactory.getInstanceObj(instanceName).clickImage(driver, param1, param2,
										fetchMetadataVO, fetchConfigVO);
								break;
							}

						case "clickTableImage":
							try {
								if (checkValidScript.equalsIgnoreCase("Yes")) {

									xpathPerformance.clickTableImage(driver, param1, param2,
											fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO, count);
									break;
								} else {

									throw new Exception("ScriptNotValid");
								}
							} catch (Exception e) {
								seleniumFactory.getInstanceObj(instanceName).clickTableImage(driver, param1, param2,
										fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
								break;
							}

						case "clickButton":
							try {
								if (checkValidScript.equalsIgnoreCase("Yes")) {

									xpathPerformance.clickButton(driver, param1, param2, fetchMetadataVO, fetchConfigVO,
											count);
									break;
								} else {

									throw new Exception("ScriptNotValid");
								}
							} catch (Exception e) {
								seleniumFactory.getInstanceObj(instanceName).clickButton(driver, param1, param2,
										fetchMetadataVO, fetchConfigVO);
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
									seleniumFactory.getInstanceObj(instanceName).screenshotFail(driver, "",
											fetchMetadataVO, fetchConfigVO);
									throw new IllegalArgumentException("Error occured");
								}
								seleniumFactory.getInstanceObj(instanceName).screenshot(driver, "", fetchMetadataVO,
										fetchConfigVO);
								break;
							}

						case "tableRowSelect":
							try {
								if (checkValidScript.equalsIgnoreCase("Yes")) {

									xpathPerformance.tableRowSelect(driver, param1, param2, fetchMetadataVO,
											fetchConfigVO, count);
									break;
								} else {

									throw new Exception("ScriptNotValid");
								}
							} catch (Exception e) {

								seleniumFactory.getInstanceObj(instanceName).tableRowSelect(driver, param1, param2,
										fetchMetadataVO, fetchConfigVO);
								break;
							}

						case "clickButton Dropdown":
							if (fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
								try {
									if (checkValidScript.equalsIgnoreCase("Yes")) {

										xpathPerformance.clickButtonDropdown(driver, param1, param2,
												fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO,
												count);
										break;
									} else {

										throw new Exception("ScriptNotValid");
									}
								} catch (Exception e) {
									seleniumFactory.getInstanceObj(instanceName).clickButtonDropdown(driver, param1,
											param2, fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
									break;
								}

							}

						case "mousehover":
							try {
								if (checkValidScript.equalsIgnoreCase("Yes")) {
									xpathPerformance.mousehover(driver, param1, param2, fetchMetadataVO, fetchConfigVO);
									break;
								} else {

									throw new Exception("ScriptNotValid");
								}
							} catch (Exception e) {

								seleniumFactory.getInstanceObj(instanceName).mousehover(driver, param1, param2,
										fetchMetadataVO, fetchConfigVO);
								break;
							}

						case "scrollUsingElement":
							try {
								if (checkValidScript.equalsIgnoreCase("Yes")) {
									xpathPerformance.scrollUsingElement(driver, fetchMetadataVO.getInput_parameter(),
											fetchMetadataVO, fetchConfigVO, count);
									break;
								} else {

									throw new Exception("ScriptNotValid");
								}
							} catch (Exception e) {
								seleniumFactory.getInstanceObj(instanceName).scrollUsingElement(driver,
										fetchMetadataVO.getInput_parameter(), fetchMetadataVO, fetchConfigVO);
								break;
							}

						case "moveToElement":
							seleniumFactory.getInstanceObj(instanceName).moveToElement(driver,
									fetchMetadataVO.getInput_parameter(), fetchMetadataVO, fetchConfigVO);
							break;
						case "switchToDefaultFrame":
							seleniumFactory.getInstanceObj(instanceName).switchToDefaultFrame(driver);
							break;

						case "windowhandle":
							seleniumFactory.getInstanceObj(instanceName).windowhandle(driver, fetchMetadataVO,
									fetchConfigVO);
							break;
						case "dragAnddrop":
							seleniumFactory.getInstanceObj(instanceName).dragAnddrop(driver,
									fetchMetadataVO.getXpath_location(), fetchMetadataVO.getXpath_location1(),
									fetchMetadataVO, fetchConfigVO);
							break;
						case "clickFilter":
							seleniumFactory.getInstanceObj(instanceName).clickFilter(driver, param1, param2,
									fetchMetadataVO, fetchConfigVO);
							break;

						case "switchToFrame":
							try {
								if (checkValidScript.equalsIgnoreCase("Yes")) {

									xpathPerformance.switchToFrame(driver, fetchMetadataVO.getInput_parameter(),
											fetchMetadataVO, fetchConfigVO, count);
									break;
								} else {

									throw new Exception("ScriptNotValid");
								}
							} catch (Exception e) {

								seleniumFactory.getInstanceObj(instanceName).switchToFrame(driver,
										fetchMetadataVO.getInput_parameter(), fetchMetadataVO, fetchConfigVO);
								break;
							}

						case "selectByText":
							if (fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
								try {
									if (checkValidScript.equalsIgnoreCase("Yes")) {

										xpathPerformance.selectByText(driver, param1, param2,
												fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO,
												count);
										break;
									} else {

										throw new Exception("ScriptNotValid");
									}
								} catch (Exception e) {
									seleniumFactory.getInstanceObj(instanceName).selectByText(driver, param1, param2,
											fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
									break;
								}

							}

						case "copy":
							seleniumFactory.getInstanceObj(instanceName).copy(driver, fetchMetadataVO, fetchConfigVO);
							break;
						case "copynumber":
							globalValueForSteps = seleniumFactory.getInstanceObj(instanceName).copynumber(driver,
									param1, param2, fetchMetadataVO, fetchConfigVO);
							break;
						case "copyy":
							seleniumFactory.getInstanceObj(instanceName).copyy(driver,
									fetchMetadataVO.getXpath_location(), fetchMetadataVO, fetchConfigVO);
							break;
						case "copytext":
							seleniumFactory.getInstanceObj(instanceName).copytext(driver,
									fetchMetadataVO.getXpath_location(), fetchMetadataVO, fetchConfigVO);
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
										fetchMetadataVO, fetchConfigVO);
								break;
							}
							// XpathPerformance code for cases ended
						case "enter":
							seleniumFactory.getInstanceObj(instanceName).enter(driver, fetchMetadataVO, fetchConfigVO);
							break;
						case "tab":
							seleniumFactory.getInstanceObj(instanceName).tab(driver, fetchMetadataVO, fetchConfigVO);
							break;
						case "paste":
							seleniumFactory.getInstanceObj(instanceName).paste(driver,
									fetchMetadataVO.getInput_parameter(), fetchMetadataVO, fetchConfigVO,
									globalValueForSteps);
							break;
						case "uploadFileAutoIT":
							seleniumFactory.getInstanceObj(instanceName)
									.uploadFileAutoIT(fetchMetadataVO.getField_type(), fetchMetadataVO);
							break;
						case "windowclose":
							seleniumFactory.getInstanceObj(instanceName).windowclose(driver, fetchMetadataVO,
									fetchConfigVO);
							break;
						case "switchDefaultContent":
							seleniumFactory.getInstanceObj(instanceName).switchDefaultContent(driver, fetchMetadataVO,
									fetchConfigVO);
							break;
						case "switchParentWindow":
							seleniumFactory.getInstanceObj(instanceName).switchParentWindow(driver, fetchMetadataVO,
									fetchConfigVO);
							break;
						case "switchToParentWindow":
							seleniumFactory.getInstanceObj(instanceName).switchToParentWindow(driver, fetchMetadataVO,
									fetchConfigVO);
							break;
						case "DatePicker":
							if (fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
								seleniumFactory.getInstanceObj(instanceName).datePicker(driver, param1, param2,
										fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
								break;
							} else {
								break;
							}
						case "refreshPage":
							seleniumFactory.getInstanceObj(instanceName).refreshPage(driver, fetchMetadataVO,
									fetchConfigVO);
							break;
						case "navigateToBackPage":
							seleniumFactory.getInstanceObj(instanceName).navigateToBackPage(driver, fetchMetadataVO,
									fetchConfigVO);
							break;
						case "openPdf":
							seleniumFactory.getInstanceObj(instanceName).openPdf(driver,
									fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
							break;
						case "openFile":
							seleniumFactory.getInstanceObj(instanceName).openFile(driver, fetchMetadataVO,
									fetchConfigVO);
							break;
						case "actionApprove":
							seleniumFactory.getInstanceObj(instanceName).actionApprove(driver, param1, param2,
									fetchMetadataVO, fetchConfigVO);
							break;
						case "multipleSendKeys":
							seleniumFactory.getInstanceObj(instanceName).multipleSendKeys(driver, param1, param2,
									value1, value2, fetchMetadataVO, fetchConfigVO);
							break;

						case "Login into Application(Informatica)":
							userName = fetchMetadataVO.getInput_value();
							log.info("Navigating to Login into Application Action");
							if (fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
								seleniumFactory.getInstanceObj(instanceName).loginInformaticaApplication(driver,
										fetchConfigVO, fetchMetadataVO, type1, type2, type3, param1, param2, param3,
										fetchMetadataVO.getInput_value(),
										dataBaseEntry.getPassword(param, userName, fetchConfigVO));
								userName = null;
							}
							break;
						case "Logout(Informatica)":
							seleniumFactory.getInstanceObj(instanceName).InformaticaLogout(driver, fetchConfigVO,
									fetchMetadataVO, type1, type2, type3, param1, param2, param3);
							break;

						case "sendvalues(Informatica)":
							if (fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
								seleniumFactory.getInstanceObj(instanceName).InformaticaSendValue(driver, param1,
										param2, fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
								break;
							} else {
								break;
							}

						case "selectAValue(Informatica)":
							if (fetchMetadataVO.getInput_value() != null
									|| "".equals(fetchMetadataVO.getInput_value())) {
								seleniumFactory.getInstanceObj(instanceName).InformaticaSelectAValue(driver, param1,
										param2, fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
							}
							break;

						case "clickLink(Informatica)":
							seleniumFactory.getInstanceObj(instanceName).InformaticaclickLink(driver, param1, param2,
									fetchMetadataVO, fetchConfigVO);
							break;
						case "clickImage(Informatica)":
							seleniumFactory.getInstanceObj(instanceName).InformaticaClickImage(driver, param1, param2,
									fetchMetadataVO, fetchConfigVO);
							break;
						case "clickButton(Informatica)":
							seleniumFactory.getInstanceObj(instanceName).InformaticaClickButton(driver, param1, param2,
									fetchMetadataVO, fetchConfigVO);
							break;
						case "waitTillLoad":
							seleniumFactory.getInstanceObj(instanceName).waitTillLoad(driver, param1, param2,
									fetchMetadataVO, fetchConfigVO);
							break;

						case "compareValue":
							seleniumFactory.getInstanceObj(instanceName).compareValue(driver,
									fetchMetadataVO.getInput_parameter(), fetchMetadataVO, fetchConfigVO,
									globalValueForSteps);
							break;
							
						case "apiAccessToken":
							seleniumFactory.getInstanceObj(instanceName).apiAccessToken(fetchMetadataVO,accessTokenStorage);
							validationFlag = true;
							break;
							
						case "apiValidationResponse":
							seleniumFactory.getInstanceObj(instanceName).apiValidationResponse(fetchMetadataVO,accessTokenStorage,api);
							break;
							
						case "validation":
							validationFlag = seleniumFactory.getInstanceObj(instanceName).validation(fetchMetadataVO,api);
							break;

						default:
							System.out.println("Action Name is not matched with" + "" + actionName);
							break;

						}
						fetchConfigVO.setStatus1("Pass");
						System.out.println("Successfully Executed the" + "" + actionName);
						try {
							dataBaseEntry.updatePassedScriptLineStatus(fetchMetadataVO, fetchConfigVO,
									test_script_param_id, "Pass");
							fetchMetadataVO.setStatus("Pass");
							
							if(validationFlag!=null && !validationFlag) {
								dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id,
										"Fail","");
							}
//							dataBaseEntry.updateFailedImages(fetchMetadataVO, fetchConfigVO, test_script_param_id);
						} catch (Exception e) {
							System.out.println("e");
						}
					}

					if (fetchMetadataListVO.size() == i && !isError) {
						String checkPackage = dataBaseEntry.getPackage(test_set_id);
						if (!"API_TESTING".equalsIgnoreCase(checkPackage)) {
							FetchScriptVO post = new FetchScriptVO();
							post.setP_test_set_id(test_set_id);
							post.setP_status("Pass");
							post.setP_script_id(script_id);
							post.setP_test_set_line_id(test_set_line_id);
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
									if (scriptStatus.containsKey(Integer.parseInt(fetchMetadataVO.getScript_id()))) {
										Status s = scriptStatus.get(Integer.parseInt(fetchMetadataVO.getScript_id()));
										if (!"Fail".equalsIgnoreCase(s.getStatus())) {
											int awaitCounter = s.getInExecutionCount();
											s.setInExecutionCount(--awaitCounter);
											if (awaitCounter <= 0) {
												s.setStatus("Pass");
											}

										}
									}
								}

								dataService.updateTestCaseStatus(post, param, fetchConfigVO);
								dataBaseEntry.updateEndTime(fetchConfigVO, test_set_line_id, test_set_id, enddate);
							} catch (Exception e) {
								e.printStackTrace();
							}

							limitScriptExecutionService.insertTestRunScriptData(fetchConfigVO, fetchMetadataListVO,
									script_id1, script_Number, "pass", startdate, enddate);
							limitScriptExecutionService.updateFaileScriptscount(test_set_line_id, test_set_id);
							downloadScreenShot(fetchConfigVO, fetchMetadataVO, false);
							fetchMetadataVO.setStatus("Pass");
							seleniumFactory.getInstanceObjFromAbstractClass(fetchConfigVO.getInstance_name()).createPdf(
									fetchMetadataListVO, fetchConfigVO, seq_num + "_" + script_Number + ".pdf",
									startdate, fetchConfigVO.getEndtime());

							if ("SHAREPOINT".equalsIgnoreCase(fetchConfigVO.getPDF_LOCATION())) {
								seleniumFactory.getInstanceObj(fetchConfigVO.getInstance_name())
										.uploadPDF(fetchMetadataListVO, fetchConfigVO);
							}
						}
						else {
							Date enddate = new Date();
							fetchConfigVO.setEndtime(enddate);
							FetchScriptVO post = new FetchScriptVO();
							post.setP_test_set_id(test_set_id);
							if (validationFlag) {
								post.setP_status("Pass");
							} else {
								post.setP_status("Fail");
							}
							post.setP_script_id(script_id);
							post.setP_test_set_line_id(test_set_line_id);
							post.setP_pass_path(passurl);
							post.setP_fail_path(failurl);
							post.setP_exception_path(detailurl);
							post.setP_test_set_line_path(scripturl);
							dataService.updateTestCaseStatus(post, param, fetchConfigVO);
							dataBaseEntry.updateEndTime(fetchConfigVO, test_set_line_id, test_set_id, enddate);
							if(!validationFlag) {
								int failedScriptRunCount = limitScriptExecutionService.getFailedScriptRunCount(test_set_line_id,
										test_set_id);
								seleniumFactory.getInstanceObj(instanceName).createDriverFailedPdf(fetchMetadataListVO, fetchConfigVO,seq_num + "_" + script_Number +"_RUN"+failedScriptRunCount+ ".pdf",api,validationFlag);
							}else {
								seleniumFactory.getInstanceObj(instanceName).createDriverFailedPdf(fetchMetadataListVO, fetchConfigVO,seq_num + "_" + script_Number + ".pdf",api,validationFlag);
							}
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
					if (scriptStatus.containsKey(Integer.parseInt(fetchMetadataVO.getScript_id()))) {
						Status s = scriptStatus.get(Integer.parseInt(fetchMetadataVO.getScript_id()));
						s.setStatus("Fail");
					}
					System.out.println("Failed to Execute the " + "" + actionName);
					System.out.println(
							"Error occurred in TestCaseName=" + actionName + "" + "Exception=" + "" + e.getMessage());
					errorMessagesHandler.getError(actionName, fetchMetadataVO, fetchConfigVO, test_script_param_id,
							message, param1, param2, dataBaseEntry.getPassword(param, userName, fetchConfigVO));
					isError = true;
				}
				if (isError) {
					String checkPackage = dataBaseEntry.getPackage(test_set_id);
					if (!"API_TESTING".equalsIgnoreCase(checkPackage)) {
						FetchScriptVO post = new FetchScriptVO();
						post.setP_test_set_id(test_set_id);
						post.setP_status("Fail");
						post.setP_script_id(script_id);
						post.setP_test_set_line_id(test_set_line_id);
						post.setP_pass_path(passurl);
						post.setP_fail_path(failurl);
						post.setP_exception_path(detailurl);
						post.setP_test_set_line_path(scripturl);
						failcount = failcount + 1;
						Date enddate = new Date();
						fetchConfigVO.setEndtime(enddate);
						dataService.updateTestCaseStatus(post, param, fetchConfigVO);
						dataBaseEntry.updateEndTime(fetchConfigVO, test_set_line_id, test_set_id, enddate);
						limitScriptExecutionService.insertTestRunScriptData(fetchConfigVO, fetchMetadataListVO,
								script_id1, script_Number, "Fail", startdate, enddate);
						int failedScriptRunCount = limitScriptExecutionService.getFailedScriptRunCount(test_set_line_id,
								test_set_id);

						fetchConfigVO.setStatus1("Fail");
						downloadScreenShot(fetchConfigVO, fetchMetadataVO, false);
						seleniumFactory.getInstanceObjFromAbstractClass(fetchConfigVO.getInstance_name()).createPdf(
								fetchMetadataListVO, fetchConfigVO,
								seq_num + "_" + script_Number + "_RUN" + failedScriptRunCount + ".pdf", startdate,
								enddate);
						if ("SHAREPOINT".equalsIgnoreCase(fetchConfigVO.getPDF_LOCATION())) {
							seleniumFactory.getInstanceObj(fetchConfigVO.getInstance_name())
									.uploadPDF(fetchMetadataListVO, fetchConfigVO);
						}
					} else {
						Date enddate = new Date();
						fetchConfigVO.setEndtime(enddate);
						FetchScriptVO post = new FetchScriptVO();
						post.setP_test_set_id(test_set_id);
						if (validationFlag) {
							post.setP_status("Pass");
						} else {
							post.setP_status("Fail");
						}
						post.setP_script_id(script_id);
						post.setP_test_set_line_id(test_set_line_id);
						post.setP_pass_path(passurl);
						post.setP_fail_path(failurl);
						post.setP_exception_path(detailurl);
						post.setP_test_set_line_path(scripturl);
						dataService.updateTestCaseStatus(post, param, fetchConfigVO);
						dataBaseEntry.updateEndTime(fetchConfigVO, test_set_line_id, test_set_id, enddate);

						int failedScriptRunCount = limitScriptExecutionService.getFailedScriptRunCount(test_set_line_id,
								test_set_id);
						seleniumFactory.getInstanceObj(instanceName).createDriverFailedPdf(fetchMetadataListVO,
								fetchConfigVO, seq_num + "_" + script_Number + "_RUN" + failedScriptRunCount + ".pdf",
								api, validationFlag);
						dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id,
								"Fail", "");
					}
					return;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	private void downloadScreenShot(FetchConfigVO fetchConfigVO, FetchMetadataVO fetchMetadataVO,
			boolean evidenceReport) {
		String seqNumber = evidenceReport ? null : fetchMetadataVO.getSeq_num();
		String screenShotFolder = fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + fetchMetadataVO.getCustomer_name()
				+ File.separator + fetchMetadataVO.getTest_run_name() + File.separator;
		seleniumFactory.getInstanceObjFromAbstractClass(fetchConfigVO.getInstance_name())
				.downloadScreenshotsFromObjectStore(screenShotFolder, fetchMetadataVO.getCustomer_name(),
						fetchMetadataVO.getTest_run_name(), seqNumber);
	}

}
