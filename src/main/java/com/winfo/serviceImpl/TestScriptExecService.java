package com.winfo.serviceImpl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.objectstorage.responses.PutObjectResponse;
import com.winfo.Factory.SeleniumKeywordsFactory;
import com.winfo.config.DriverConfiguration;
import com.winfo.constants.TestScriptExecServiceEnum;
import com.winfo.dao.CodeLinesRepository;
import com.winfo.dao.PyJabActionRepo;
import com.winfo.exception.WatsEBSException;
import com.winfo.model.AuditScriptExecTrail;
import com.winfo.model.PyJabActions;
import com.winfo.model.TestSetLine;
import com.winfo.model.TestSetScriptParam;
import com.winfo.repository.TestSetScriptParamRepository;
import com.winfo.service.ExecutionHistoryService;
import com.winfo.utils.Constants;
import com.winfo.utils.Constants.AUDIT_TRAIL_STAGES;
import com.winfo.utils.Constants.BOOLEAN_STATUS;
import com.winfo.utils.Constants.SCRIPT_PARAM_STATUS;
import com.winfo.utils.Constants.UPDATE_STATUS;
import com.winfo.utils.DateUtils;
import com.winfo.utils.FileUtil;
import com.winfo.vo.CustomerProjectDto;
import com.winfo.vo.FetchConfigVO;
import com.winfo.vo.FetchMetadataVO;
import com.winfo.vo.FetchScriptVO;
import com.winfo.vo.MessageQueueDto;
import com.winfo.vo.PyJabScriptDto;
import com.winfo.vo.ResponseDto;
import com.winfo.vo.ScriptDetailsDto;
import com.winfo.vo.TestScriptDto;
import com.winfo.vo.UpdateScriptParamStatus;
import com.winfo.vo.UpdateScriptStepStatus;

@Service
public class TestScriptExecService extends AbstractSeleniumKeywords {

	public static final Logger logger = Logger.getLogger(TestScriptExecService.class);;

	@Value("${configvO.watslogo}")
	private String watslogo;

	@Value("${oci.config.path}")
	private String ociConfigPath;
	@Value("${chrome.driver.path}")
	private String chromeDriverPath;
	@Value("${dll.path}")
	private String dllPath;
	@Value("${oci.config.name}")
	private String ociConfigName;
	@Value("${oci.bucket.name}")
	private String ociBucketName;
	@Value("${oci.namespace}")
	private String ociNamespace;


	@Value("${url.update.script.step.status}")
	private String scriptParamStatusUpdateUrl;

	@Value("${url.get.copied.value}")
	private String copiedValueUrl;

	@Value("${pyjab.template.name}")
	private String templateName;

	@Value("${kafka.topic.name.test.run}")
	private String testScriptRunTopicName;
	
	@Value("${kafka.topic.name.excel.test.run}")
	private String excelTestRunTopicName;

	@Autowired
	TemplateEngine templateEngine;
	@Autowired
	LimitScriptExecutionService limitScriptExecutionService;
	@Autowired
	DriverConfiguration deriverConfiguration;


	@Autowired
	DataBaseEntry dataBaseEntry;
	@Autowired
	SeleniumKeywordsFactory seleniumFactory;
	@Autowired
	CodeLinesRepository codeLineRepo;
	@Autowired
	DynamicRequisitionNumber dynamicnumber;
	@Autowired
	GenerateTestRunPDFService generateTestRunPDFService;
	
	@Autowired
	ExecutionHistoryService executionHistory;

	@Autowired
	private KafkaTemplate<String, MessageQueueDto> kafkaTemp;

	@Autowired
	PyJabActionRepo actionRepo;
	
	@Autowired
	SmartBearService smartBearService;
	
	@Autowired
	TestSetScriptParamRepository testSetScriptParamRepository;

	public String getTestSetMode(Long testSetId) {
		return dataBaseEntry.getTestSetMode(testSetId);
	}

	public void executorMethodPyJab(TestScriptDto testScriptDto, FetchConfigVO fetchConfigVO,
			Entry<Integer, List<ScriptDetailsDto>> metaData, boolean run, CustomerProjectDto customerDetails) {
		List<ScriptDetailsDto> fetchMetadataListsVO = metaData.getValue();
		switchActions(testScriptDto, fetchMetadataListsVO, fetchConfigVO, run, customerDetails);

	}

	public void switchActions(TestScriptDto testScriptDto, List<ScriptDetailsDto> fetchMetadataListVO, FetchConfigVO fetchConfigVO,
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

				String screenShotFolderPath = TestScriptExecServiceEnum.SCREENSHOT.getValue() + TestScriptExecServiceEnum.BACK_SLASH.getValue() + customerDetails.getCustomerName() + TestScriptExecServiceEnum.BACK_SLASH.getValue()
						+ customerDetails.getTestSetName() + TestScriptExecServiceEnum.BACK_SLASH.getValue();

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
					dto.setEbsApplicationUrl(fetchConfigVO.getAPPLICATION_URL());
				}
				dto.setScriptFileName(
						fetchMetadataListVO.get(0).getTargetApplicationName().replaceAll("\\s+", "_").toLowerCase()
								+ "_" + customerDetails.getCustomerName().replaceAll("\\s+", "_").toLowerCase());

				final Context ctx = new Context();
				ctx.setVariable("dto", dto);
				final String scriptContent = this.templateEngine.process(templateName, ctx);

				String scriptPathForPyJabScript = customerDetails.getCustomerName() + TestScriptExecServiceEnum.FORWARD_SLASH.getValue()
						+ customerDetails.getTestSetName() + TestScriptExecServiceEnum.FORWARD_SLASH.getValue()
						+ fetchMetadataListVO.get(0).getTestSetLineId() + TestScriptExecServiceEnum.FORWARD_SLASH.getValue()
						+ fetchMetadataListVO.get(0).getTestSetLineId() + TestScriptExecServiceEnum.PY_EXTN.getValue();
				uploadObjectToObjectStoreWithInputContent(scriptContent, scriptPathForPyJabScript);
				dataBaseEntry.insertScriptExecAuditRecord(auditTrial, AUDIT_TRAIL_STAGES.SGC, null);

				logger.info(
						"Publishing with details test_set_id, test_set_line_id, scriptPathForPyJabScript, screenShotFolderPath,objectStoreScreenShotPath ---- "
								+ testSetId + " - " + testSetLineId + " - " + scriptPathForPyJabScript + " - "
								+ screenShotFolderPath);
				this.kafkaTemp.send(testScriptRunTopicName,
						new MessageQueueDto(testSetId, testSetLineId, scriptPathForPyJabScript, auditTrial,testScriptDto.getExecutedBy()));
				
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
			dataBaseEntry.updateStatusOfScript(testSetLineId, Constants.TEST_SET_LINE_ID_STATUS.FAIL.getLabel());
			dataBaseEntry.updateExecStatusIfTestRunIsCompleted(testScriptDto);
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

			seleniumFactory.getInstanceObj(fetchConfigVO.getINSTANCE_NAME()).uploadObjectToObjectStore(sourceFilePath,
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
					index = keyWithIndex.split(Constants.SPLIT)[0];
					key = keyWithIndex.split(Constants.SPLIT)[1];
					value = (String) entry.getValue();

					if (value.equalsIgnoreCase("<Pick from Config Table>")) {
						dbValue = codeLineRepo.findByConfigurationId(Integer.parseInt(testrunId), key);
						listArgs.add(index + Constants.SPLIT + Constants.addQuotes(dbValue));
					}
					if (value.equalsIgnoreCase("<Pick from Input Value>")) {
						dbValue = inputValue;
						if (actionName.equalsIgnoreCase("ebsSelectMenu")) {
							if (dbValue.contains(">")) {
								String[] arrOfStr = dbValue.split(">", 5);
								if (arrOfStr.length < 2) {
									listArgs.add(index + Constants.SPLIT + Constants.addQuotes(dbValue));
								} else {
									String menu = arrOfStr[0];
									String subMenu = arrOfStr[1];
									String menuLink = menu + "    " + subMenu;
									listArgs.add(index + Constants.SPLIT + Constants.addQuotes(menuLink));
								}
							} else {
								listArgs.add(index + Constants.SPLIT + Constants.addQuotes(dbValue));
							}
						} else {
							listArgs.add(index + Constants.SPLIT + Constants.addQuotes(dbValue));
						}

					}

					if (value.equalsIgnoreCase("<Pick from Java>")) {
						String image_dest = "C:\\\\EBS-Automation\\\\WATS_Files\\\\screenshot\\\\ebs\\\\"
								+ customerDetails.getCustomerName() + "\\\\" + customerDetails.getTestSetName();

						dbValue = image_dest;
						listArgs.add(index + Constants.SPLIT + Constants.addQuotes(dbValue));
					}
					if (value.equalsIgnoreCase("<Pick from Input Parameter>")) {
						dbValue = codeLineRepo.findByTestRunScriptIdInputParam(
								Integer.parseInt(fetchMetadataVO.getTestScriptParamId()), key);
						listArgs.add(index + Constants.SPLIT + Constants.addQuotes(dbValue));

					}
					if (value.equalsIgnoreCase("<Password>")) {
						String userName = fetchMetadataVO.getInputValue();

						dbValue = dataBaseEntry.getPassword(customerDetails.getTestSetId(), userName, null);
						dbValue = dbValue != null ? dbValue : "welcome123";
						listArgs.add(index + Constants.SPLIT + Constants.addQuotes(dbValue));
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
			int indexVal = val.indexOf(Constants.SPLIT);
			val = val.substring(indexVal + 1);
			return val;
		}).forEach(methodCall::add);
		methodCall.add(Constants.addQuotes(screenshotPath));
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

	public String uploadObjectToObjectStoreWithInputContent(String sourceFileContent, String destinationFilePath)
	{

    	PutObjectResponse response = null;

		byte[] bytes = sourceFileContent.getBytes(StandardCharsets.UTF_8);

		try (InputStream in = new ByteArrayInputStream(bytes);) {

			final ConfigFileReader.ConfigFile configFile = ConfigFileReader

					.parse(new FileInputStream(new File(ociConfigPath)), ociConfigName);

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

			throw new WatsEBSException(500, "Exception occurred while uploading generated script to object store",

					e);
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

//			fetchConfigVO.setWINDOWS_SCREENSHOT_LOCATION(
//					System.getProperty(Constants.SYS_USER_HOME_PATH) + Constants.SCREENSHOT);
//			fetchConfigVO.setWINDOWS_PDF_LOCATION(System.getProperty(Constants.SYS_USER_HOME_PATH) + Constants.PDF);

			CustomerProjectDto customerDetails = dataBaseEntry.getCustomerDetails(args.getTestSetId());

			List<ScriptDetailsDto> testLinesDetails = dataBaseEntry.getScriptDetailsListVO(args.getTestSetId(),
					args.getTestSetLineId(), false, false);

			String screenShotFolderPath = (fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION()
					+ customerDetails.getCustomerName() + File.separator + customerDetails.getTestSetName());

			String scriptId = testLinesDetails.get(0).getScriptId();
			Map<String, String> urls = FileUtil.generateUrls(fetchConfigVO, customerDetails, testLinesDetails);

			//Map<String, String> mapOfUrl=runAutomation.generateUrls( fetchConfigVO,  customerDetails, testLinesDetails);
			//String passUrl = urls.get("PassUrl");
			//String failUrl = urls.get("FailUrl");
			//String detailUrl = urls.get("DetailUrl");
			//String scriptUrl = urls.get("ScriptUrl");
			
			fetchConfigVO.setStarttime(testSetLine.getExecutionStartTime());
			deleteScreenshotsFromWindows(screenShotFolderPath, testLinesDetails.get(0).getSeqNum());
			downloadScreenshotsFromObjectStore(screenShotFolderPath, customerDetails.getCustomerName(),
					customerDetails.getTestSetName(), testLinesDetails.get(0).getSeqNum() + "_");

			FetchScriptVO post = new FetchScriptVO(args.getTestSetId(), scriptId, args.getTestSetLineId(), urls.get("PassUrl"),
					urls.get("FailUrl"), urls.get("DetailUrl"),  urls.get("ScriptUrl"));
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
						+ TestScriptExecServiceEnum.PDF_EXTENSION.getValue();
				fetchConfigVO.setStatus1("Pass");
				limitScriptExecutionService.updateFaileScriptscount(args.getTestSetLineId(), args.getTestSetId());
				dataBaseEntry.updateTestCaseEndDate(post, enddate, fetchConfigVO.getStatus1());
			} 
			else {
				fetchConfigVO.setErrormessage("Execution Failed");
				fetchConfigVO.setStatus1(TestScriptExecServiceEnum.FAIL.getValue());
				failedScriptRunCount = limitScriptExecutionService.getFailScriptRunCount(args.getTestSetLineId(),
						args.getTestSetId());
				pdfName = testLinesDetails.get(0).getSeqNum() + "_" + testLinesDetails.get(0).getScriptNumber() + "_RUN"
						+ failedScriptRunCount + TestScriptExecServiceEnum.PDF_EXTENSION.getValue();
				
			String scriptUrl = fetchConfigVO.getIMG_URL() + customerDetails.getCustomerName() +"/"+ customerDetails.getProjectName() + "/"
							+ customerDetails.getTestSetName() + "/" + pdfName;
				post.setP_test_set_line_path(scriptUrl);
				dataBaseEntry.updateTestCaseEndDate(post, enddate, fetchConfigVO.getStatus1());
			}
//			dataBaseEntry.updateTestCaseEndDate(post, enddate, fetchConfigVO.getStatus1());
//			dataService.updateTestCaseStatus(post, args.getTestSetId(), fetchConfigVO);

			/* Email processing Updating subscription table code */
			if (updateStatus) {
				try{
					int exeId = executionHistory.getMaxExecutionIdForTestSetLine(Integer.parseInt(args.getTestSetLineId()));
					dataBaseEntry.updateTestCaseStatus(post, fetchConfigVO, testLinesDetails,
							testSetLine.getExecutionStartTime(), customerDetails.getTestSetName(),false,args.getExecutedBy(),exeId,Optional.of(Constants.TEST_RUN));

				} catch (Exception e){
					logger.error("Failed to update the execution history");
				}
				if (fetchConfigVO.getStatus1().equals(TestScriptExecServiceEnum.FAIL.getValue())) {
					failedScriptRunCount = failedScriptRunCount + 1;
					limitScriptExecutionService.updateFailScriptRunCount(failedScriptRunCount, args.getTestSetLineId(),
							args.getTestSetId());
					pdfName = testLinesDetails.get(0).getSeqNum() + "_" + testLinesDetails.get(0).getScriptNumber()
							+ "_RUN" + failedScriptRunCount + TestScriptExecServiceEnum.PDF_EXTENSION.getValue();
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
						generateTestRunPDFService.testRunPdfGeneration(args.getTestSetId(), fetchConfigVO);
					}
				}
			}
			if ("SHAREPOINT".equalsIgnoreCase(fetchConfigVO.getPDF_LOCATION())) {
				List<ScriptDetailsDto> fetchMetadataListVOforEvidence = dataBaseEntry.getScriptDetailsListVO(args.getTestSetId(),
						null, true, false);
				seleniumFactory.getInstanceObjFromAbstractClass(fetchConfigVO.getINSTANCE_NAME())
						.uploadPdfToSharepoint(fetchMetadataListVOforEvidence, fetchConfigVO, customerDetails);
			}
			if (Constants.smartBear.YES.toString().equalsIgnoreCase(fetchConfigVO.getSMARTBEAR_ENABLED())
					&& Constants.smartBear.WOOD.toString().equalsIgnoreCase(fetchConfigVO.getINSTANCE_NAME())) {
				String sourceFilePath = (fetchConfigVO.getWINDOWS_PDF_LOCATION().replace("/", File.separator)
						+ customerDetails.getCustomerName() + File.separator + customerDetails.getTestSetName()
						+ File.separator) + pdfName;
				smartBearService.smartBearRegenerateAttachment(testLinesDetails, sourceFilePath,
						fetchConfigVO.getSMARTBEAR_PROJECT_NAME(), fetchConfigVO.getSMARTBEAR_CUSTOM_COLUMN_NAME());
			}
			FileUtil.deleteScreenshotsForSpecificSeqFromDir(
					fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + customerDetails.getCustomerName()
							+ File.separator + customerDetails.getTestSetName() + File.separator, testSetLine.getSeqNum());
			FileUtil.deletePdfsForSpecificSeqFromDir(
					fetchConfigVO.getWINDOWS_PDF_LOCATION() + customerDetails.getCustomerName()
					+ File.separator + customerDetails.getTestSetName() + File.separator, testSetLine.getSeqNum());
		} catch (Exception e) {
			if (args.getAutditTrial() != null) {
				dataBaseEntry.insertScriptExecAuditRecord(args.getAutditTrial(), AUDIT_TRAIL_STAGES.EISU,
						e.getMessage());
			}
			if (scriptStatus != null) {
				dataBaseEntry.updateStatusOfScript(args.getTestSetLineId(), scriptStatus);
			}
			TestScriptDto testScriptDto = new TestScriptDto();
			dataBaseEntry.updateExecStatusIfTestRunIsCompleted(testScriptDto);
			if (e instanceof WatsEBSException) {
				throw e;
			}
			throw new WatsEBSException(500, "Exception occurred while generating the pdf", e);
		}
		return new ResponseDto(200, Constants.SUCCESS, null);
	}


	public Map<String, String> findExecutionTimeForTestRun(String testSetId, String pdffileName) {

		String scriptStatus = null;
		Map<String, String> totalExecutedTime = new HashMap<>();
		if (pdffileName.equalsIgnoreCase("Passed_Report.pdf")) {
			scriptStatus = "Pass";
		} else if (pdffileName.equalsIgnoreCase("Failed_Report.pdf")) {
			scriptStatus = "Fail";
		} else {
			scriptStatus = null;
		}

		List<Object[]> startAndEndDates = dataBaseEntry.findStartAndEndTimeForTestRun(testSetId, scriptStatus);
		long totalDiff = 0;
		Date startDate = null;
		Date finishDate = null;
		for (Object[] date : startAndEndDates) {
			if (date[0] != null && date[1] != null) {
				if (startDate == null || startDate.after((Date) date[0])) {
					startDate = (Date) date[0];
				}
				if (finishDate == null || finishDate.before((Date) date[1])) {
					finishDate = (Date) date[1];
				}
				totalDiff += DateUtils.findTimeDifference(date[0].toString(), date[1].toString());
			}
		}
		if (startDate != null && finishDate != null) {
			totalExecutedTime.put("totalElapsedTime", DateUtils.convertMiliSecToDayFormat(
					DateUtils.findTimeDifference(startDate.toString(), finishDate.toString())));
		} else {
			totalExecutedTime.put("totalElapsedTime", "Not Available");
		}
		totalExecutedTime.put("totalExecutedTime", DateUtils.convertMiliSecToDayFormat(totalDiff));

		return totalExecutedTime;
	}


	public void updateStartStatus(MessageQueueDto args) throws ClassNotFoundException, SQLException {
		dataBaseEntry.updateInProgressScriptStatus(args.getTestSetLineId(), new Date());
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

	public void updateScriptStepStatus(UpdateScriptStepStatus scriptParamDetails)
			throws ClassNotFoundException, SQLException {
		String status = SCRIPT_PARAM_STATUS.FAIL.getLabel();
		if (scriptParamDetails.getStatus().equalsIgnoreCase(SCRIPT_PARAM_STATUS.PASS.getLabel())) {
			status = SCRIPT_PARAM_STATUS.PASS.getLabel();
		} else if (scriptParamDetails.getStatus().equalsIgnoreCase(SCRIPT_PARAM_STATUS.IN_PROGRESS.getLabel())) {
			status = SCRIPT_PARAM_STATUS.IN_PROGRESS.getLabel();
		}
		if (StringUtils.isBlank(scriptParamDetails.getResult())) {
			testSetScriptParamRepository.updateTestSetScriptParamStatusAndStartAndEndTimeWithoutCopyvalue(status,
					scriptParamDetails.getStartTime(), scriptParamDetails.getEndTime(), new Date(),
					scriptParamDetails.getMessage(), Integer.parseInt(scriptParamDetails.getScriptParamId()));
		} else {
			testSetScriptParamRepository.updateTestSetScriptParamStatusAndStartAndEndTime(status,
					scriptParamDetails.getStartTime(), scriptParamDetails.getEndTime(), new Date(), null,
					scriptParamDetails.getResult(), Integer.parseInt(scriptParamDetails.getScriptParamId()));

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
		dataBaseEntry.updateStatusOfPdfGeneration(testSetId,Constants.GENERATING);
		ResponseDto response;
		boolean runFinalPdf = dataBaseEntry.checkIfAllTestSetLinesCompleted(Long.valueOf(testSetId), null);
		if (runFinalPdf) {
			try {
				FetchConfigVO fetchConfigVO = fetchConfigVO(testSetId);
				Date startDate = dataBaseEntry.findMinExecutionStartDate(Long.valueOf(testSetId));
				Date endDate = dataBaseEntry.findMaxExecutionEndDate(Long.valueOf(testSetId));
				fetchConfigVO.setStarttime(startDate);
				fetchConfigVO.setEndtime(endDate);
				generateTestRunPDFService.testRunPdfGeneration(testSetId, fetchConfigVO);
				response = new ResponseDto(200, Constants.SUCCESS, "The process of generating the PDF has started, please check back after some time.");
			} catch (Exception e) {
				dataBaseEntry.updateStatusOfPdfGeneration(testSetId,Constants.PASSED);
				response = new ResponseDto(500, Constants.ERROR, e.getMessage());
			}
		} else {
			dataBaseEntry.updateStatusOfPdfGeneration(testSetId,Constants.PASSED);
			response = new ResponseDto(200, Constants.WARNING, "Cannot generate PDF. Scripts are In-Progress or In-Queue");
		}
		return response;
	}


	@KafkaListener(topics = "#{'${kafka.topic.name.update.audit.logs}'.split(',')}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
	public void updateAuditLogs(MessageQueueDto event) {
		dataBaseEntry.insertScriptExecAuditRecord(event.getAutditTrial(), event.getStage(), null);
	}

	public void runExcelSteps(TestScriptDto testScriptDto, List<ScriptDetailsDto> fetchMetadataListVO, FetchConfigVO fetchConfigVO,
			boolean run, CustomerProjectDto customerDetails,AuditScriptExecTrail auditTrial) {

		String log4jConfPath = "log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
		String testSetId = customerDetails.getTestSetId();
		String testSetLineId = fetchMetadataListVO.get(0).getTestSetLineId();
		String testScriptParamId = null;
		String methodCall;
		ArrayList<String> methods = new ArrayList<>();
		PyJabScriptDto dto = new PyJabScriptDto();
		dataBaseEntry.insertScriptExecAuditRecord(auditTrial, AUDIT_TRAIL_STAGES.EES, null);
		System.out
				.println("Create script methods for  ---------   " + fetchMetadataListVO.get(0).getTestSetLineId());

		String screenShotFolderPath = TestScriptExecServiceEnum.SCREENSHOT.getValue() + TestScriptExecServiceEnum.BACK_SLASH.getValue() + customerDetails.getCustomerName()
				+ TestScriptExecServiceEnum.BACK_SLASH.getValue() + customerDetails.getTestSetName() + TestScriptExecServiceEnum.BACK_SLASH.getValue();
		logger.info(" ScreenShot Folder Path " + screenShotFolderPath);
		for (ScriptDetailsDto fetchMetadataVO : fetchMetadataListVO) {

			testScriptParamId = fetchMetadataVO.getTestScriptParamId();

			String screenshotPath = screenShotFolderPath + fetchMetadataVO.getSeqNum() + "_"
					+ fetchMetadataVO.getLineNumber() + "_" + fetchMetadataVO.getScenarioName() + "_"
					+ fetchMetadataVO.getScriptNumber() + "_" + customerDetails.getTestSetName() + "_"
					+ fetchMetadataVO.getLineNumber();

			methodCall = ebsActions(fetchMetadataVO, customerDetails.getTestSetId(), fetchMetadataVO.getAction(),
					fetchMetadataVO.getInputValue(), screenshotPath, testScriptParamId, customerDetails);
			methods.add(methodCall);
		}
		dto.setActions(methods);
		dto.setScriptStatusUpdateUrl(scriptParamStatusUpdateUrl);
		dto.setCopiedValueUrl(copiedValueUrl);
		dto.setOciConfigPath(ociConfigPath);
		dto.setOciConfigName(ociConfigName);
		dto.setBuckerName(ociBucketName);
		dto.setDownloadPath(fetchConfigVO.getDOWNLOD_FILE_PATH().replace("\\", "\\\\"));
		dto.setExcelDownloadFilePath(fetchConfigVO.getEXCEL_DOWNLOAD_FILE_PATH().replace("\\", "\\\\"));
		
		final Context ctx = new Context();
		ctx.setVariable("dto", dto);
		final String scriptContent = this.templateEngine.process("excel-automation-template.txt", ctx);
		System.out.println(scriptContent);
		String scriptPathForPyJabScript = customerDetails.getCustomerName() + TestScriptExecServiceEnum.FORWARD_SLASH.getValue()
				+ customerDetails.getTestSetName() + TestScriptExecServiceEnum.FORWARD_SLASH.getValue()
				+ fetchMetadataListVO.get(0).getTestSetLineId() + TestScriptExecServiceEnum.FORWARD_SLASH.getValue()
				+ fetchMetadataListVO.get(0).getTestSetLineId() + TestScriptExecServiceEnum.PY_EXTN.getValue();
		uploadObjectToObjectStoreWithInputContent(scriptContent, scriptPathForPyJabScript);
		dataBaseEntry.insertScriptExecAuditRecord(auditTrial, AUDIT_TRAIL_STAGES.SGC, null);

		logger.info(
				"Publishing with details test_set_id, test_set_line_id, scriptPathForPyJabScript, screenShotFolderPath,objectStoreScreenShotPath ---- "
						+ testSetId + " - " + testSetLineId + " - " + scriptPathForPyJabScript + " - "
						+ screenShotFolderPath);
		this.kafkaTemp.send(excelTestRunTopicName,
				new MessageQueueDto(testSetId, testSetLineId, scriptPathForPyJabScript, auditTrial,testScriptDto.getExecutedBy()));
		dataBaseEntry.insertScriptExecAuditRecord(auditTrial, AUDIT_TRAIL_STAGES.SQ, null);

	}
	
	public ResponseDto excelStatusCheck(Integer testsetlineid) {
		ResponseDto response = new ResponseDto();

		List<TestSetScriptParam> testsetscripts = dataBaseEntry.getTestSetScriptParamContainsExcel(testsetlineid);
		for (TestSetScriptParam testscript : testsetscripts) {
			if (testscript.getLineExecutionStatus().equalsIgnoreCase(SCRIPT_PARAM_STATUS.PASS.getLabel())) {
				continue;
			} else if (testscript.getLineExecutionStatus().equalsIgnoreCase(SCRIPT_PARAM_STATUS.FAIL.getLabel())) {
				break;
			} else if (testscript.getLineExecutionStatus().equalsIgnoreCase(SCRIPT_PARAM_STATUS.IN_PROGRESS.getLabel())) {
				dataBaseEntry.UpdateTestSetScriptParamContainsExcel(testscript.getTestRunScriptParamId());
				break;
			} else if (testscript.getLineExecutionStatus().equalsIgnoreCase(SCRIPT_PARAM_STATUS.NEW.getLabel())) {
				dataBaseEntry.UpdateTestSetScriptParamContainsExcel(testscript.getTestRunScriptParamId());
				break;
			}
		}
		response.setStatusCode(200);
		response.setStatusDescription("Updated Successfully");
		response.setStatusMessage(Constants.SUCCESS);
		return response;
	}

}
