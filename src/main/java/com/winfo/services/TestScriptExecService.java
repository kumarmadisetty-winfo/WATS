package com.winfo.services;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.GetObjectRequest;
import com.oracle.bmc.objectstorage.requests.ListObjectsRequest;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.objectstorage.responses.GetObjectResponse;
import com.oracle.bmc.objectstorage.responses.ListObjectsResponse;
import com.oracle.bmc.objectstorage.responses.PutObjectResponse;
import com.winfo.Factory.SeleniumKeywordsFactory;
import com.winfo.config.DriverConfiguration;
import com.winfo.dao.CodeLinesRepository;
import com.winfo.dao.PyJabActionRepo;
import com.winfo.exception.WatsEBSCustomException;
import com.winfo.interface1.AbstractSeleniumKeywords;
import com.winfo.model.AuditScriptExecTrail;
import com.winfo.model.PyJabActions;
import com.winfo.model.TestSetLine;
import com.winfo.utils.Constants;
import com.winfo.utils.Constants.AUDIT_TRAIL_STAGES;
import com.winfo.utils.Constants.BOOLEAN_STATUS;
import com.winfo.utils.Constants.SCRIPT_PARAM_STATUS;
import com.winfo.utils.Constants.TEST_SET_LINE_ID_STATUS;
import com.winfo.utils.Constants.UPDATE_STATUS;
import com.winfo.utils.DateUtils;
import com.winfo.vo.CustomerProjectDto;
import com.winfo.vo.MessageQueueDto;
import com.winfo.vo.PyJabScriptDto;
import com.winfo.vo.ResponseDto;
import com.winfo.vo.ScriptDetailsDto;
import com.winfo.vo.UpdateScriptParamStatus;
import com.winfo.vo.UpdateScriptStepStatus;

@Service
public class TestScriptExecService extends AbstractSeleniumKeywords {

	public final Logger logger = LogManager.getLogger(TestScriptExecService.class);

	public static final String BACK_SLASH = "\\\\";
	public static final String topic = "test-script-run";
	private static final String PY_EXTN = ".py";
	private static final String PNG_EXTENSION = ".png";
	private static final String JPG_EXTENSION = ".jpg";
	public static final String FORWARD_SLASH = "/";
	public static final String SPLIT = "@";
	private static final String[] CONST = { "Status", "Total", "Percentage" };
	private static final String PASSED = "Passed";
	private static final String FAILED = "Failed";
	private static final String ARIAL = "Arial";
	private static final String PASS = "Pass";
	private static final String FAIL = "Fail";
	private static final String IN_COMPLETE = "In Complete";
	private static final String PDF_EXTENSION = ".pdf";
	private static final String EXECUTION_REPORT = "Execution Report";
	private static final String TEST_RUN_NAME = "Test Run Name";
	private static final String EXECUTED_BY = "Executed By";
	private static final String START_TIME = "Start Time";
	private static final String END_TIME = "End Time";
	private static final String EXECUTION_TIME = "Execution Time";
	private static final String EXECUTION_SUMMARY = "Execution Summary";
	private static final String SCRIPT_NUMBER = "Script Number";
	private static final String STEP_DESC = "Step Description : ";
	private static final String TEST_PARAM = "Test Parameter : ";
	private static final String TEST_VALUE = "Test Value : ";
	private static final String SCENARIO_NAME = "Test Case Name";
	private static final String STEP_NO = "Step No : ";
	private static final String SCREENSHOT = "Screenshot";

	@Value("${configvO.watslogo}")
	private String watslogo;

	@Value("${chrome.driver.path}")
	private String chromeDriverPath;
	@Value("${dll.path}")
	private String dllPath;
	@Value("${oci.config.path}")
	private String ociConfigPath;
	@Value("${oci.config.name}")
	private String ociConfigName;
	@Value("${oci.bucket.name}")
	private String ociBucketName;
	@Value("${oci.namespace}")
	private String ociNamespace;

	@Autowired
	ErrorMessagesHandler errorMessagesHandler;
	@Value("${configvO.watsvediologo}")
	private String watsvediologo;
	@Value("${configvO.whiteimage}")
	private String whiteimage;

	@Value("${url.update.script.step.status}")
	private String scriptParamStatusUpdateUrl;

	@Value("${url.get.copied.value}")
	private String copiedValueUrl;

	@Value("${pyjab.template.name}")
	private String templateName;

	@Value("${kafka.topic.name.test.run}")
	private String testScriptRunTopicName;

	@Autowired
	TemplateEngine templateEngine;
	@Autowired
	LimitScriptExecutionService limitScriptExecutionService;
	@Autowired
	DriverConfiguration deriverConfiguration;

	@Autowired
	TestCaseDataService dataService;
	@Autowired
	DataBaseEntry dataBaseEntry;
	@Autowired
	SeleniumKeywordsFactory seleniumFactory;
	@Autowired
	CodeLinesRepository codeLineRepo;
	@Autowired
	DynamicRequisitionNumber dynamicnumber;

	@Autowired
	private KafkaTemplate<String, MessageQueueDto> kafkaTemp;

	@Autowired
	PyJabActionRepo actionRepo;

	@Autowired
	Environment environment;

	public String getTestSetMode(Long testSetId) {
		return dataBaseEntry.getTestSetMode(testSetId);

	}

	public void executorMethodPyJab(String args, FetchConfigVO fetchConfigVO,
			Entry<Integer, List<ScriptDetailsDto>> metaData, boolean run, CustomerProjectDto customerDetails) {
		List<ScriptDetailsDto> fetchMetadataListsVO = metaData.getValue();
		switchActions(args, fetchMetadataListsVO, fetchConfigVO, run, customerDetails);

	}

	public void switchActions(String param, List<ScriptDetailsDto> fetchMetadataListVO, FetchConfigVO fetchConfigVO,
			boolean run, CustomerProjectDto customerDetails) {
		String log4jConfPath = "log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
		String errorMessage = null;
		String testSetId = customerDetails.getTestSetId();
		String testSetLineId = fetchMetadataListVO.get(0).getTestSetLineId();
		String testScriptParamId = null;
		String methodCall;
		String targetApplication = fetchMetadataListVO.get(0).getTargetApplicationName();
		ArrayList<String> methods = new ArrayList<>();
		PyJabScriptDto dto = new PyJabScriptDto();
		AuditScriptExecTrail auditTrial = dataBaseEntry.insertScriptExecAuditRecord(AuditScriptExecTrail.builder()
				.testSetLineId(Integer.valueOf(testSetLineId)).triggeredBy(fetchMetadataListVO.get(0).getExecutedBy())
				.correlationId(UUID.randomUUID().toString()).build(), AUDIT_TRAIL_STAGES.RR, null);
		if (run) {
			try {
				System.out.println(
						"Create script methods for  ---------   " + fetchMetadataListVO.get(0).getTestSetLineId());

				String screenShotFolderPath = SCREENSHOT + BACK_SLASH + customerDetails.getCustomerName() + BACK_SLASH
						+ customerDetails.getTestSetName() + BACK_SLASH;

				for (ScriptDetailsDto fetchMetadataVO : fetchMetadataListVO) {

					testScriptParamId = fetchMetadataVO.getTestScriptParamId();

					String screenshotPath = screenShotFolderPath + fetchMetadataVO.getSeqNum() + "_"
							+ fetchMetadataVO.getLineNumber() + "_" + fetchMetadataVO.getScenarioName() + "_"
							+ fetchMetadataVO.getScriptNumber() + "_" + customerDetails.getTestSetName() + "_"
							+ fetchMetadataVO.getLineNumber();

					methodCall = ebsActions(fetchMetadataVO, customerDetails.getTestSetId(),
							fetchMetadataVO.getAction(), fetchMetadataVO.getInputValue(), screenshotPath,
							testScriptParamId, customerDetails);
					methods.add(methodCall);
				}
				dto.setActions(methods);
				dto.setScriptStatusUpdateUrl(scriptParamStatusUpdateUrl);
				dto.setCopiedValueUrl(copiedValueUrl);
				dto.setChromeDriverPath(chromeDriverPath);
				dto.setApplicationName(fetchConfigVO.getEBS_APPLICATION_NAME());
				dto.setDllPath(dllPath);
				dto.setOciConfigPath(ociConfigPath);
				dto.setOciConfigName(ociConfigName);
				dto.setBuckerName(ociBucketName);
				dto.setOciNameSpace(ociNamespace);
				if (targetApplication.contains(Constants.EBS)) {
					dto.setEbsApplicationUrl(fetchConfigVO.getEBS_URL());
				} else if (targetApplication.contains(Constants.SAP_CONCUR)) {
					dto.setEbsApplicationUrl(fetchConfigVO.getSAP_CONCUR_URL());
				} else {
					dto.setEbsApplicationUrl(fetchConfigVO.getApplication_url());
				}
				dto.setScriptFileName(
						fetchMetadataListVO.get(0).getTargetApplicationName().replaceAll("\\s+", "_").toLowerCase()
								+ "_" + customerDetails.getCustomerName().replaceAll("\\s+", "_").toLowerCase());

				final Context ctx = new Context();
				ctx.setVariable("dto", dto);
				final String scriptContent = this.templateEngine.process(templateName, ctx);

				String scriptPathForPyJabScript = customerDetails.getCustomerName() + FORWARD_SLASH
						+ customerDetails.getTestSetName() + FORWARD_SLASH
						+ fetchMetadataListVO.get(0).getTestSetLineId() + FORWARD_SLASH
						+ fetchMetadataListVO.get(0).getTestSetLineId() + PY_EXTN;
				uploadObjectToObjectStoreWithInputContent(scriptContent, scriptPathForPyJabScript);
				dataBaseEntry.insertScriptExecAuditRecord(auditTrial, AUDIT_TRAIL_STAGES.SGC, null);

				logger.info(
						"Publishing with details test_set_id, test_set_line_id, scriptPathForPyJabScript, screenShotFolderPath,objectStoreScreenShotPath ---- "
								+ testSetId + " - " + testSetLineId + " - " + scriptPathForPyJabScript + " - "
								+ screenShotFolderPath);
				this.kafkaTemp.send(testScriptRunTopicName,
						new MessageQueueDto(testSetId, testSetLineId, scriptPathForPyJabScript, auditTrial));
				dataBaseEntry.insertScriptExecAuditRecord(auditTrial, AUDIT_TRAIL_STAGES.SQ, null);
			} catch (Exception e) {
				// suppressing error so that other scripts run if there data has no issue
				run = false;
				errorMessage = e.getMessage();
				e.printStackTrace();
			}
		}
		if (!run) {
			if (errorMessage == null) {
				dataBaseEntry.insertScriptExecAuditRecord(auditTrial, AUDIT_TRAIL_STAGES.DSF, errorMessage);
				dataBaseEntry.updateDefaultMessageForFailedScriptInFirstStep(testSetLineId,
						Constants.ERR_MSG_FOR_DEP_FAILED);
			} else {
				dataBaseEntry.insertScriptExecAuditRecord(auditTrial, AUDIT_TRAIL_STAGES.EIP, errorMessage);
				dataBaseEntry.updateDefaultMessageForFailedScriptInFirstStep(testSetLineId,
						Constants.ERR_MSG_FOR_SCRIPT_RUN);
			}
			dataBaseEntry.updateStatusOfScript(testSetLineId, Constants.TEST_SET_LINE_ID_STATUS.Fail.getLabel());
			dataBaseEntry.updateExecStatusIfTestRunIsCompleted(testSetId);
		}

	}

	public void uploadScreenshotsToObjectStore(FetchConfigVO fetchConfigVO, List<FetchMetadataVO> fetchMetadataListVO) {
		String sourceFilePath;
		String destinationFilePath;

		File sourceFolderDirectory = new File(
				fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + fetchMetadataListVO.get(0).getCustomer_name() + "\\"
						+ fetchMetadataListVO.get(0).getTest_run_name() + "\\");

		String sourceFolder = (fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION()
				+ fetchMetadataListVO.get(0).getCustomer_name() + "\\" + fetchMetadataListVO.get(0).getTest_run_name()
				+ "\\");

		String destinationFolder = ("Screenshot/" + fetchMetadataListVO.get(0).getCustomer_name() + "/"
				+ fetchMetadataListVO.get(0).getTest_run_name() + "/");

		File[] listOfFiles = sourceFolderDirectory.listFiles();
		List<File> allFileList = Arrays.asList(listOfFiles);
		for (File f : allFileList) {
			String fileName = f.getName();
			sourceFilePath = sourceFolder + fileName;
			destinationFilePath = destinationFolder + fileName;

			seleniumFactory.getInstanceObj(fetchConfigVO.getInstance_name()).uploadObjectToObjectStore(sourceFilePath,
					destinationFilePath);
		}

	}

	public String ebsActions(ScriptDetailsDto fetchMetadataVO, String testrunId, String actionName, String inputValue,
			String screenshotPath, String testScriptParamId, CustomerProjectDto customerDetails) {
		logger.info(actionName);

		PyJabActions action = actionRepo.findByActionName(actionName);
		String paramValue = action.getParamValues();
		StringJoiner methodCall = new StringJoiner(",", action.getMethodName() + "(", ")");
		String dbValue = "";
		String key = "";
		String keyWithIndex = "";
		String value;
		String index = "";
		List<String> listArgs = new ArrayList<>();

		if (paramValue != null) {
			HashMap<String, Object> result;
			try {
				result = new ObjectMapper().readValue(paramValue, HashMap.class);
				for (Map.Entry<String, Object> entry : result.entrySet()) {
					keyWithIndex = entry.getKey();
					index = keyWithIndex.split(SPLIT)[0];
					key = keyWithIndex.split(SPLIT)[1];
					value = (String) entry.getValue();

					if (value.equalsIgnoreCase("<Pick from Config Table>")) {
						dbValue = codeLineRepo.findByConfigurationId(Integer.parseInt(testrunId), key);
						listArgs.add(index + SPLIT + addQuotes(dbValue));
					}
					if (value.equalsIgnoreCase("<Pick from Input Value>")) {
						dbValue = inputValue;
						if (actionName.equalsIgnoreCase("ebsSelectMenu")) {
							if (dbValue.contains(">")) {
								String[] arrOfStr = dbValue.split(">", 5);
								if (arrOfStr.length < 2) {
									listArgs.add(index + SPLIT + addQuotes(dbValue));
								} else {
									String menu = arrOfStr[0];
									String subMenu = arrOfStr[1];
									String menuLink = menu + "    " + subMenu;
									listArgs.add(index + SPLIT + addQuotes(menuLink));
								}
							} else {
								listArgs.add(index + SPLIT + addQuotes(dbValue));
							}
						} else {
							listArgs.add(index + SPLIT + addQuotes(dbValue));
						}

					}

					if (value.equalsIgnoreCase("<Pick from Java>")) {
						String image_dest = "C:\\\\EBS-Automation\\\\WATS_Files\\\\screenshot\\\\ebs\\\\"
								+ customerDetails.getCustomerName() + "\\\\" + customerDetails.getTestSetName();

						dbValue = image_dest;
						listArgs.add(index + SPLIT + addQuotes(dbValue));
					}
					if (value.equalsIgnoreCase("<Pick from Input Parameter>")) {
						dbValue = codeLineRepo.findByTestRunScriptIdInputParam(
								Integer.parseInt(fetchMetadataVO.getTestScriptParamId()), key);
						listArgs.add(index + SPLIT + addQuotes(dbValue));
					}
					if (value.equalsIgnoreCase("<Password>")) {
						String userName = fetchMetadataVO.getInputValue();

						dbValue = dataBaseEntry.getPassword(customerDetails.getTestSetId(), userName, null);
						dbValue = dbValue != null ? dbValue : "welcome123";
						listArgs.add(index + SPLIT + addQuotes(dbValue));
					}
				}

			} catch (Exception e) {
				// suppressing error.. because the errors might happen because of user entered
				// data as well which need not be handled
				e.printStackTrace();
			}

		}
		Collections.sort(listArgs);
		listArgs.stream().map(val -> {
			int indexVal = val.indexOf(SPLIT);
			val = val.substring(indexVal + 1);
			return val;
		}).forEach(methodCall::add);
		methodCall.add(addQuotes(screenshotPath));
		methodCall.add(testScriptParamId);
		return methodCall.toString();
	}

	public void deleteScreenshoots(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO)
			throws IOException {
		File folder = new File(
				fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
						+ fetchMetadataListVO.get(0).getTest_run_name() + "/");
		if (folder.exists()) {
			File[] listOfFiles = folder.listFiles();

			for (File file : Arrays.asList(listOfFiles)) {

				String seqNum = String.valueOf(file.getName().substring(0, file.getName().indexOf('_')));

				String seqnum1 = fetchMetadataListVO.get(0).getSeq_num();
				if (seqNum.equalsIgnoreCase(seqnum1)) {
					Path imagesPath = Paths.get(file.getPath());
					Files.delete(imagesPath);
				}
			}
		}
	}

	private String addQuotes(String string) {
		return "'" + string + "'";
	}

	public String uploadObjectToObjectStoreWithInputContent(String sourceFileContent, String destinationFilePath) {
//		try {
//			String path = "C:\\wats\\java-generated-scripts\\" + destinationFilePath.split(FORWARD_SLASH)[3];
//			logger.info("%%%%%%%%%%");
//
//			logger.info(path);
//
//			Files.write(Paths.get(path), sourceFileContent.getBytes());
//		} catch (IOException e1) {
//
//			e1.printStackTrace();
//		}

		PutObjectResponse response = null;
		byte[] bytes = sourceFileContent.getBytes(StandardCharsets.UTF_8);
		try (InputStream in = new ByteArrayInputStream(bytes);) {
			final ConfigFileReader.ConfigFile configFile = ConfigFileReader
					.parse(new ClassPathResource("oci/config").getInputStream(), ociConfigName);
			final AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);

			/* Create a service client */
			ObjectStorageClient client = new ObjectStorageClient(provider);

			/* Create a request and dependent object(s). */
			PutObjectRequest putObjectRequest = PutObjectRequest.builder().namespaceName(ociNamespace)
					.bucketName(ociBucketName).objectName(destinationFilePath).putObjectBody(in).build();

			/* Send request to the Client */
			response = client.putObject(putObjectRequest);
			logger.info("Uploaded to -------- " + destinationFilePath);

			return response.toString();
		} catch (Exception e) {
			throw new WatsEBSCustomException(500, "Exception Occured while uploading generated script to object store",
					e);
		}
	}

	public String uploadObjectToObjectStore(String sourceFile, String destinationFilePath) {

		PutObjectResponse response = null;
		try {
			/**
			 * Create a default authentication provider that uses the DEFAULT profile in the
			 * configuration file. Refer to <see
			 * href="https://docs.cloud.oracle.com/en-us/iaas/Content/API/Concepts/sdkconfig.htm#SDK_and_CLI_Configuration_File>the
			 * public documentation</see> on how to prepare a configuration file.
			 */
			final ConfigFileReader.ConfigFile configFile = ConfigFileReader
					.parse(new ClassPathResource("oci/config").getInputStream(), ociConfigName);
			final AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);
			final String FILE_NAME = sourceFile;
			File file = new File(FILE_NAME);
			long fileSize = FileUtils.sizeOf(file);
			InputStream is = new FileInputStream(file);

			/* Create a service client */
			try (ObjectStorageClient client = new ObjectStorageClient(provider);) {

				/* Create a request and dependent object(s). */

				PutObjectRequest putObjectRequest = PutObjectRequest.builder().namespaceName(ociNamespace)
						.bucketName(ociBucketName).objectName(destinationFilePath).contentLength(fileSize)
						.putObjectBody(is).build();

				/* Send request to the Client */
				response = client.putObject(putObjectRequest);
			}
			return response.toString();
		} catch (WatsEBSCustomException e) {
			throw e;
		} catch (Exception e) {
			throw new WatsEBSCustomException(500, "Exception occured while uploading pdf in Object Storage", e);
		}
	}

	public void deleteScreenshotsFromWindows(String screenShotFolderPath, String seqNum) {
		File folder1 = new File(screenShotFolderPath);
		if (folder1.exists()) {
			File folder = new File(screenShotFolderPath + File.separator);
			if (folder.exists()) {
				File[] listOfFiles = folder.listFiles();
				for (File file : Arrays.asList(listOfFiles)) {
					String seqNumFromScreenshot = String
							.valueOf(file.getName().substring(0, file.getName().indexOf('_')));
					if (seqNum.equalsIgnoreCase(seqNumFromScreenshot)) {
						Path imagesPath = Paths.get(file.getPath());
						try {
							Files.delete(imagesPath);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	public ResponseDto generateTestScriptLineIdReports(MessageQueueDto args) throws Exception {
		String scriptStatus = null;
		try {
			scriptStatus = dataBaseEntry.getScriptStatus(args.getTestSetLineId());
			TestSetLine testSetLine = dataBaseEntry.getTestSetLinesRecord(args.getTestSetId(), args.getTestSetLineId());
			if (args.isManualTrigger() && testSetLine.getExecutionStartTime() == null) {
				return new ResponseDto(500, Constants.ERROR,
						"Script didn't ran atleast once. So Pdf can't be generated");
			}
			FetchConfigVO fetchConfigVO = fetchConfigVO(args.getTestSetId());

			fetchConfigVO.setWINDOWS_SCREENSHOT_LOCATION(
					System.getProperty(Constants.SYS_USER_HOME_PATH) + Constants.SCREENSHOT);
			fetchConfigVO.setWINDOWS_PDF_LOCATION(System.getProperty(Constants.SYS_USER_HOME_PATH) + Constants.PDF);

			CustomerProjectDto customerDetails = dataBaseEntry.getCustomerDetails(args.getTestSetId());

			List<ScriptDetailsDto> testLinesDetails = dataBaseEntry.getScriptDetailsListVO(args.getTestSetId(),
					args.getTestSetLineId(), false, false);

			String screenShotFolderPath = (fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION()
					+ customerDetails.getCustomerName() + File.separator + customerDetails.getTestSetName());

			String scriptId = testLinesDetails.get(0).getScriptId();
			String passurl = fetchConfigVO.getImg_url() + customerDetails.getCustomerName() + File.separator
					+ customerDetails.getTestSetName() + File.separator + "Passed_Report.pdf" + "AAAparent="
					+ fetchConfigVO.getImg_url();
			String failurl = fetchConfigVO.getImg_url() + customerDetails.getCustomerName() + "b/"
					+ customerDetails.getTestSetName() + File.separator + "Failed_Report.pdf" + "AAAparent="
					+ fetchConfigVO.getImg_url();
			String detailurl = fetchConfigVO.getImg_url() + customerDetails.getCustomerName() + File.separator
					+ customerDetails.getTestSetName() + File.separator + "Detailed_Report.pdf" + "AAAparent="
					+ fetchConfigVO.getImg_url();
			String scripturl = fetchConfigVO.getImg_url() + customerDetails.getCustomerName() + File.separator
					+ customerDetails.getTestSetName() + File.separator + testLinesDetails.get(0).getSeqNum() + "_"
					+ testLinesDetails.get(0).getScriptNumber() + PDF_EXTENSION + "AAAparent="
					+ fetchConfigVO.getImg_url();

			fetchConfigVO.setStarttime(testSetLine.getExecutionStartTime());
			deleteScreenshotsFromWindows(screenShotFolderPath, testLinesDetails.get(0).getSeqNum());
			downloadScreenshotsFromObjectStore(screenShotFolderPath, customerDetails.getCustomerName(),
					customerDetails.getTestSetName(), testLinesDetails.get(0).getSeqNum() + "_");

			FetchScriptVO post = new FetchScriptVO(args.getTestSetId(), scriptId, args.getTestSetLineId(), passurl,
					failurl, detailurl, scripturl);
			Date enddate = null;
			boolean updateStatus = limitScriptExecutionService.updateStatusCheck(fetchConfigVO,
					customerDetails.getTestSetId(), testLinesDetails.get(0).getScriptId(),
					testLinesDetails.get(0).getScriptNumber(), fetchConfigVO.getStatus1());
			if (!updateStatus) {
				enddate = testSetLine.getExecutionEndTime();
			} else {
				enddate = dataBaseEntry.findStepMaxUpdatedDate(args.getTestSetLineId(),
						testSetLine.getExecutionStartTime());
			}
			String pdfName = null;
			fetchConfigVO.setEndtime(enddate);
			int failedScriptRunCount = 0;
			if (scriptStatus != null && scriptStatus.equalsIgnoreCase(UPDATE_STATUS.PASS.getLabel())) {
				pdfName = testLinesDetails.get(0).getSeqNum() + "_" + testLinesDetails.get(0).getScriptNumber()
						+ PDF_EXTENSION;
				fetchConfigVO.setStatus1("Pass");
				limitScriptExecutionService.updateFaileScriptscount(args.getTestSetLineId(), args.getTestSetId());
			} else {
				fetchConfigVO.setErrormessage("EBS Execution Failed");
				fetchConfigVO.setStatus1(FAIL);
				failedScriptRunCount = limitScriptExecutionService.getFailScriptRunCount(args.getTestSetLineId(),
						args.getTestSetId());
				pdfName = testLinesDetails.get(0).getSeqNum() + "_" + testLinesDetails.get(0).getScriptNumber() + "_RUN"
						+ failedScriptRunCount + PDF_EXTENSION;
			}
			dataBaseEntry.updateTestCaseEndDate(post, enddate, fetchConfigVO.getStatus1());
//			dataService.updateTestCaseStatus(post, args.getTestSetId(), fetchConfigVO);

			/* Email processing Updating subscription table code */
			if (updateStatus) {
				dataBaseEntry.updateTestCaseStatus(post, fetchConfigVO, testLinesDetails,
						testSetLine.getExecutionStartTime(), customerDetails.getTestSetName());
				if (fetchConfigVO.getStatus1().equals(FAIL)) {
					failedScriptRunCount = failedScriptRunCount + 1;
					limitScriptExecutionService.updateFailScriptRunCount(failedScriptRunCount, args.getTestSetLineId(),
							args.getTestSetId());
					pdfName = testLinesDetails.get(0).getSeqNum() + "_" + testLinesDetails.get(0).getScriptNumber()
							+ "_RUN" + failedScriptRunCount + PDF_EXTENSION;
				}
			}
			createPdf(testLinesDetails, fetchConfigVO, pdfName, customerDetails);
			// final reports generation
			if (!args.isManualTrigger()) {
				dataBaseEntry.insertScriptExecAuditRecord(args.getAutditTrial(), AUDIT_TRAIL_STAGES.ERG, null);

				String pdfGenerationEnabled = dataBaseEntry.pdfGenerationEnabled(Long.valueOf(args.getTestSetId()));
				if (BOOLEAN_STATUS.TRUE.getLabel().equalsIgnoreCase(pdfGenerationEnabled)) {
					boolean runFinalPdf = dataBaseEntry
							.checkIfAllTestSetLinesCompleted(Long.valueOf(args.getTestSetId()), true);
					if (runFinalPdf) {
						dataBaseEntry.updatePdfGenerationEnableStatus(args.getTestSetId(),
								BOOLEAN_STATUS.FALSE.getLabel());
						testRunPdfGeneration(args.getTestSetId(), fetchConfigVO);
					}
				}
			}
		} catch (Exception e) {
			if (args.getAutditTrial() != null) {
				dataBaseEntry.insertScriptExecAuditRecord(args.getAutditTrial(), AUDIT_TRAIL_STAGES.EISU,
						e.getMessage());
			}
			if (scriptStatus != null) {
				dataBaseEntry.updateStatusOfScript(args.getTestSetLineId(), scriptStatus);
			}
			dataBaseEntry.updateExecStatusIfTestRunIsCompleted(args.getTestSetId());
			if (e instanceof WatsEBSCustomException) {
				throw e;
			}
			throw new WatsEBSCustomException(500, "Exception occured while generating the pdf", e);
		}
		return new ResponseDto(200, Constants.SUCCESS, null);
	}

	private void testRunPdfGeneration(String testSetId, FetchConfigVO fetchConfigVO) throws Exception {
		CustomerProjectDto customerDetails = dataBaseEntry.getCustomerDetails(testSetId);
		List<ScriptDetailsDto> fetchMetadataListVOFinal = dataBaseEntry.getScriptDetailsListVO(testSetId, null, true,
				false);
		dataBaseEntry.setPassAndFailScriptCount(testSetId, fetchConfigVO);
		String screenShotFolderPath = (fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION()
				+ customerDetails.getCustomerName() + File.separator + customerDetails.getTestSetName());
		createDir(screenShotFolderPath);
		File folder = new File(screenShotFolderPath);
		Map<String, String> screenShotsMap = new HashMap<>();
		for (ScriptDetailsDto scriptDetailsData : fetchMetadataListVOFinal) {
			String seqNum = scriptDetailsData.getSeqNum();
			if (!screenShotsMap.containsKey(seqNum)) {
				String screenShot = scriptDetailsData.getSeqNum() + "_" + scriptDetailsData.getLineNumber() + "_"
						+ scriptDetailsData.getScenarioName() + "_" + scriptDetailsData.getScriptNumber() + "_"
						+ customerDetails.getTestSetName() + "_" + scriptDetailsData.getLineNumber();
				screenShotsMap.put(seqNum, screenShot);
			}
		}
		List<String> files = new ArrayList<>();
		for (File fileName : Arrays.asList(folder.listFiles())) {
			files.add(fileName.getName());
		}
		if (folder.exists()) {
			for (Map.Entry<String, String> entry : screenShotsMap.entrySet()) {
				String seqNum = entry.getKey();
				String value = entry.getValue();
				String screenShotName = null;
				if (files.contains(value + "_" + PASSED + PNG_EXTENSION)) {
					screenShotName = value + "_" + PASSED + PNG_EXTENSION;
				} else if (files.contains(value + "_" + PASSED + JPG_EXTENSION)) {
					screenShotName = value + "_" + PASSED + JPG_EXTENSION;
				} else if (files.contains(value + "_" + FAILED + PNG_EXTENSION)) {
					screenShotName = value + "_" + FAILED + PNG_EXTENSION;
				} else if (files.contains(value + "_" + FAILED + JPG_EXTENSION)) {
					screenShotName = value + "_" + FAILED + JPG_EXTENSION;
				}
				if (screenShotName == null) {
					downloadScreenshotsFromObjectStore(screenShotFolderPath, customerDetails.getCustomerName(),
							customerDetails.getTestSetName(), seqNum);
				}
			}
		}
		createPdf(fetchMetadataListVOFinal, fetchConfigVO, "Passed_Report.pdf", customerDetails);
		createPdf(fetchMetadataListVOFinal, fetchConfigVO, "Failed_Report.pdf", customerDetails);
		createPdf(fetchMetadataListVOFinal, fetchConfigVO, "Detailed_Report.pdf", customerDetails);

	}

	private void createDir(String path) {
		File folder1 = new File(path);
		if (!folder1.exists()) {
			logger.info("creating directory: " + folder1.getName());
			try {
				folder1.mkdirs();
			} catch (SecurityException se) {
				se.printStackTrace();
			}
		} else {
			logger.info("Folder exist");
		}
	}

	public String findExecutionTimeForScript(String testSetId, String pdffileName) {

		String scriptStatus = null;

		if (pdffileName.equalsIgnoreCase("Passed_Report.pdf")) {
			scriptStatus = "Pass";
		} else if (pdffileName.equalsIgnoreCase("Failed_Report.pdf")) {
			scriptStatus = "Fail";
		} else {
			scriptStatus = null;
		}

		List<Object[]> startAndEndDates = dataBaseEntry.findStartAndEndTimeForTestRun(testSetId, scriptStatus);
		long totalDiff = 0;
		for (Object[] date : startAndEndDates) {
			if (date[0] != null && date[1] != null) {
				totalDiff += DateUtils.findTimeDifference(date[0].toString(), date[1].toString());
			}
		}

		return DateUtils.convertMiliSecToDayFormat(totalDiff);
	}


	public void updateStartStatus(MessageQueueDto args) throws ClassNotFoundException, SQLException {
		dataBaseEntry.updateInProgressScriptStatus(null, args.getTestSetLineId(), new Date());
	}

	public void updateScriptParamStatus(UpdateScriptParamStatus args) throws ClassNotFoundException, SQLException {
		String status = args.isSuccess() ? SCRIPT_PARAM_STATUS.PASS.getLabel() : SCRIPT_PARAM_STATUS.FAIL.getLabel();
		if (StringUtils.isBlank(args.getResult())) {
			dataBaseEntry.updatePassedScriptLineStatus(null, null, args.getScriptParamId(), status, args.getMessage());
		} else {
			dataBaseEntry.updatePassedScriptLineStatus(null, null, args.getScriptParamId(), status, args.getResult(),
					args.getMessage());
		}
	}

	public void updateScriptStepStatus(UpdateScriptStepStatus args) throws ClassNotFoundException, SQLException {
		String status = SCRIPT_PARAM_STATUS.FAIL.getLabel();
		if (args.getStatus().equalsIgnoreCase(SCRIPT_PARAM_STATUS.PASS.getLabel())) {
			status = SCRIPT_PARAM_STATUS.PASS.getLabel();
		} else if (args.getStatus().equalsIgnoreCase(SCRIPT_PARAM_STATUS.IN_PROGRESS.getLabel())) {
			status = SCRIPT_PARAM_STATUS.IN_PROGRESS.getLabel();
		}
		if (StringUtils.isBlank(args.getResult())) {
			dataBaseEntry.updatePassedScriptLineStatus(null, null, args.getScriptParamId(), status, args.getMessage());
		} else {
			dataBaseEntry.updatePassedScriptLineStatus(null, null, args.getScriptParamId(), status, args.getResult(),
					args.getMessage());
		}
	}

	public String getCopiedValue(String copyPath) {
		String copynumberValue;
		String[] arrOfStr = copyPath.split(">", 5);
		if (arrOfStr.length < 2) {
			copynumberValue = copyPath;
		} else {
			String Testrun_name = arrOfStr[0];
			String seq = arrOfStr[1];
			String line_number = arrOfStr[2];
			copynumberValue = dynamicnumber.getCopynumber(Testrun_name, seq, line_number, null, null);
		}
		return copynumberValue;
	}

	public FetchConfigVO fetchConfigVO(String testSetId) {
		List<Object[]> configurations = dataBaseEntry.getConfigurationDetails(testSetId);
		Map<String, String> mapConfig = new HashMap<>();
		String value = null;
		for (Object[] e : configurations) {
			if (e[1] != null && StringUtils.isNotBlank(e[1].toString())) {
				value = e[1].toString();
			} else if (e[2] != null && StringUtils.isNotBlank(e[2].toString())) {
				value = e[2].toString();
			} else {
				value = null;
			}
			mapConfig.put(e[0].toString(), value);
		}

		JSONObject jsno = new JSONObject(mapConfig);
		Gson g = new Gson();
		return g.fromJson(jsno.toString(), FetchConfigVO.class);
	}

	public ResponseDto generateTestRunPdf(String testSetId) {
		boolean runFinalPdf = dataBaseEntry.checkIfAllTestSetLinesCompleted(Long.valueOf(testSetId), null);
		if (runFinalPdf) {
			try {
				FetchConfigVO fetchConfigVO = fetchConfigVO(testSetId);
				Date startDate = dataBaseEntry.findMinExecutionStartDate(Long.valueOf(testSetId));
				Date endDate = dataBaseEntry.findMaxExecutionEndDate(Long.valueOf(testSetId));
				fetchConfigVO.setStarttime(startDate);
				fetchConfigVO.setEndtime(endDate);
				fetchConfigVO.setWINDOWS_SCREENSHOT_LOCATION(
						System.getProperty(Constants.SYS_USER_HOME_PATH) + Constants.SCREENSHOT);
				fetchConfigVO.setWINDOWS_PDF_LOCATION(System.getProperty(Constants.SYS_USER_HOME_PATH) + Constants.PDF);
				testRunPdfGeneration(testSetId, fetchConfigVO);
				return new ResponseDto(200, Constants.SUCCESS, null);
			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseDto(500, Constants.ERROR, e.getMessage());
			}
		} else {
			return new ResponseDto(200, Constants.WARNING, "Cannot generate PDF. Scripts are In-Progress or In-Queue");
		}
	}

	@KafkaListener(topics = "#{'${kafka.topic.name.update.audit.logs}'.split(',')}", groupId = "wats-group")
	public void updateAuditLogs(MessageQueueDto event) {
		dataBaseEntry.insertScriptExecAuditRecord(event.getAutditTrial(), event.getStage(), null);
	}

}
