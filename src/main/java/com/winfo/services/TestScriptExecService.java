package com.winfo.services;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.block.LineBorder;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.VerticalAlignment;
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
import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.lowagie.text.DocumentException;
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
import com.winfo.model.AuditScriptExecTrail;
import com.winfo.model.PyJabActions;
import com.winfo.model.TestSetLine;
import com.winfo.scripts.DHSeleniumKeyWords;
import com.winfo.utils.Constants;
import com.winfo.utils.Constants.AUDIT_TRAIL_STAGES;
import com.winfo.utils.Constants.BOOLEAN_STATUS;
import com.winfo.utils.Constants.SCRIPT_PARAM_STATUS;
import com.winfo.utils.Constants.TEST_SET_LINE_ID_STATUS;
import com.winfo.utils.DateUtils;
import com.winfo.vo.CustomerProjectDto;
import com.winfo.vo.MessageQueueDto;
import com.winfo.vo.PyJabScriptDto;
import com.winfo.vo.ResponseDto;
import com.winfo.vo.ScriptDetailsDto;
import com.winfo.vo.UpdateScriptParamStatus;

@Service
public class TestScriptExecService {

	public final Logger logger = LogManager.getLogger(TestScriptExecService.class);
	public static final String topic = "test-script-run";
	private static final String PY_EXTN = ".py";
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
	private static final String SCENARIO_NAME = "Scenario Name";
	private static final String STEP_NO = "Step No : ";

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

	@Value("${url.update.script.param}")
	private String scriptParamStatusUpdateUrl;

	@Value("${url.get.copied.value}")
	private String copiedValueUrl;

	@Value("${pyjab.template.name}")
	private String templateName;

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
	DHSeleniumKeyWords eBSSeleniumKeyWords;

	@Autowired
	Environment environment;

	public String getTestSetMode(Long testSetId) {
		return dataBaseEntry.getTestSetMode(testSetId);

	}

	public ResponseDto run(String testSetId) throws MalformedURLException {
		ResponseDto executeTestrunVo = new ResponseDto();
		try {
			dataBaseEntry.updatePdfGenerationEnableStatus(testSetId, BOOLEAN_STATUS.TRUE.getLabel());
			FetchConfigVO fetchConfigVO = fetchConfigVO(testSetId);
			List<FetchMetadataVO> fetchMetadataListVO = dataBaseEntry.getMetaDataVOList(testSetId, null, false, true);
			SortedMap<Integer, List<FetchMetadataVO>> dependentScriptMap = new TreeMap<Integer, List<FetchMetadataVO>>();
			SortedMap<Integer, List<FetchMetadataVO>> metaDataMap = dataService.prepareTestcasedata(fetchMetadataListVO,
					dependentScriptMap);

			// Independent
			for (Entry<Integer, List<FetchMetadataVO>> metaData : metaDataMap.entrySet()) {
				logger.info(" Running Independent - " + metaData.getKey());
				executorMethodPyJab(testSetId, fetchConfigVO, metaData);
			}

			ExecutorService executordependent = Executors.newFixedThreadPool(fetchConfigVO.getParallel_dependent());
			for (Entry<Integer, List<FetchMetadataVO>> metaData : dependentScriptMap.entrySet()) {
				logger.info(" Running Dependent - " + metaData.getKey());
				executordependent.execute(() -> {
					logger.info(" Running Dependent in executor - " + metaData.getKey());
					try {
						boolean run = dataBaseEntry.checkRunStatusOfDependantScript(testSetId,
								metaData.getValue().get(0).getScript_id());
						logger.info(
								" Dependant Script run status" + metaData.getValue().get(0).getScript_id() + " " + run);
						if (run) {
							executorMethodPyJab(testSetId, fetchConfigVO, metaData);
						} else {
							dataBaseEntry.updateStatusOfScript(testSetId,
									metaData.getValue().get(0).getTest_set_line_id(),
									Constants.TEST_SET_LINE_ID_STATUS.Fail.getLabel());
							Integer inProgressCount = dataBaseEntry.getCountOfInProgressScript(testSetId);
							if (inProgressCount.equals(0)) {
								dataBaseEntry.updateExecStatusFlag(testSetId);
							}
						}
					} catch (Exception e) {
						// suppressing error as dependant scripts are running in different thread
						e.printStackTrace();
					}
				});
			}
			executordependent.shutdown();

			executeTestrunVo.setStatusCode(200);
			executeTestrunVo.setStatusMessage("SUCCESS");
			executeTestrunVo.setStatusDescr("SUCCESS");
		} catch (Exception e) {
			throw new WatsEBSCustomException(500, "Exception Occured while creating script for Test Run", e);
		}
		return executeTestrunVo;
	}

	public void executorMethodPyJab(String args, FetchConfigVO fetchConfigVO,
			Entry<Integer, List<FetchMetadataVO>> metaData) throws Exception {

		try {

			List<FetchMetadataVO> fetchMetadataListsVO = metaData.getValue();
			switchActions(args, fetchMetadataListsVO, fetchConfigVO);

		} catch (Exception e) {
			throw e;
		}
	}

	public void switchActions(String param, List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO)
			throws Exception {

		String log4jConfPath = "log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
		String actionName = null;

		String testSetId = null;
		String testSetLineId = null;
		String testScriptParamId = null;
		String methodCall;
		ArrayList<String> methods = new ArrayList<>();
		PyJabScriptDto dto = new PyJabScriptDto();

		System.out
				.println("Create script methods for  ---------   " + fetchMetadataListVO.get(0).getTest_set_line_id());
		AuditScriptExecTrail auditTrial = dataBaseEntry.insertScriptExecAuditRecord(AuditScriptExecTrail.builder()
				.testSetLineId(Integer.valueOf(fetchMetadataListVO.get(0).getTest_set_line_id()))
				.triggeredBy(fetchMetadataListVO.get(0).getExecuted_by()).correlationId(UUID.randomUUID().toString())
				.build(), AUDIT_TRAIL_STAGES.RR);
		try {

			String screenShotFolderPath = fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION()
					+ fetchMetadataListVO.get(0).getCustomer_name() + "\\\\"
					+ fetchMetadataListVO.get(0).getTest_run_name() + "\\\\";

			for (FetchMetadataVO fetchMetadataVO : fetchMetadataListVO) {
				actionName = fetchMetadataVO.getAction();
				testSetId = fetchMetadataVO.getTest_set_id();
				testSetLineId = fetchMetadataVO.getTest_set_line_id();
				testScriptParamId = fetchMetadataVO.getTest_script_param_id();

				String screenshotPath = screenShotFolderPath + fetchMetadataVO.getSeq_num() + "_"
						+ fetchMetadataVO.getLine_number() + "_" + fetchMetadataVO.getScenario_name() + "_"
						+ fetchMetadataVO.getScript_number() + "_" + fetchMetadataVO.getTest_run_name() + "_"
						+ fetchMetadataVO.getLine_number();

				methodCall = ebsActions(fetchMetadataVO, fetchMetadataVO.getTest_set_id(), actionName, screenshotPath,
						testScriptParamId);
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
			dto.setEbsApplicationUrl(fetchConfigVO.getApplication_url());
			dto.setScriptFileName(
					fetchMetadataListVO.get(0).getTargetApplicationName().replaceAll("\\s+", "_").toLowerCase() + "_"
							+ fetchMetadataListVO.get(0).getCustomer_name().toLowerCase());

			final Context ctx = new Context();
			ctx.setVariable("dto", dto);
			final String scriptContent = this.templateEngine.process(templateName, ctx);

			String scriptPathForPyJabScript = fetchMetadataListVO.get(0).getCustomer_name() + FORWARD_SLASH
					+ fetchMetadataListVO.get(0).getTest_run_name() + FORWARD_SLASH
					+ fetchMetadataListVO.get(0).getTest_set_line_id() + FORWARD_SLASH
					+ fetchMetadataListVO.get(0).getTest_set_line_id() + PY_EXTN;
			uploadObjectToObjectStoreWithInputContent(scriptContent, scriptPathForPyJabScript);
			dataBaseEntry.insertScriptExecAuditRecord(auditTrial, AUDIT_TRAIL_STAGES.SGC);

			logger.info(
					"Publishing with details test_set_id, test_set_line_id, scriptPathForPyJabScript, screenShotFolderPath,objectStoreScreenShotPath ---- "
							+ testSetId + " - " + testSetLineId + " - " + scriptPathForPyJabScript + " - "
							+ screenShotFolderPath);
			this.kafkaTemp.send(topic,
					new MessageQueueDto(testSetId, testSetLineId, scriptPathForPyJabScript, auditTrial));
			dataBaseEntry.insertScriptExecAuditRecord(auditTrial, AUDIT_TRAIL_STAGES.SQ);
		} catch (Exception e) {
			throw e;
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

	public String ebsActions(FetchMetadataVO fetchMetadataVO, String testrunId, String actionName,
			String screenshotPath, String testScriptParamId) throws Exception {
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
			HashMap<String, Object> result = new ObjectMapper().readValue(paramValue, HashMap.class);
			try {
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
						if (actionName.equalsIgnoreCase("ebsSelectMenu")) {
							dbValue = codeLineRepo.findByTestRunScriptId(
									Integer.parseInt(fetchMetadataVO.getTest_script_param_id()), key);
							if (dbValue.contains(">")) {
								String[] arrOfStr = dbValue.split(">", 5);
								if (arrOfStr.length < 2) {
									listArgs.add(index + SPLIT + addQuotes(dbValue));
								} else {
									String menu = arrOfStr[0];
									String subMenu = arrOfStr[1];
									String menu_link = menu + "    " + subMenu;
									listArgs.add(index + SPLIT + addQuotes(menu_link));
								}
							} else {
								dbValue = codeLineRepo.findByTestRunScriptId(
										Integer.parseInt(fetchMetadataVO.getTest_script_param_id()), key);
								listArgs.add(index + SPLIT + addQuotes(dbValue));
							}
						} else {
							dbValue = codeLineRepo.findByTestRunScriptId(
									Integer.parseInt(fetchMetadataVO.getTest_script_param_id()), key);
							listArgs.add(index + SPLIT + addQuotes(dbValue));
						}

					}

					if (value.equalsIgnoreCase("<Pick from Java>")) {
						String image_dest = "C:\\\\EBS-Automation\\\\WATS_Files\\\\screenshot\\\\ebs\\\\"
								+ fetchMetadataVO.getCustomer_name() + "\\\\" + fetchMetadataVO.getTest_run_name();

						dbValue = image_dest;
						listArgs.add(index + SPLIT + addQuotes(dbValue));
					}
					if (value.equalsIgnoreCase("<Pick from Input Parameter>")) {
						dbValue = codeLineRepo.findByTestRunScriptIdInputParam(
								Integer.parseInt(fetchMetadataVO.getTest_script_param_id()), key);
						listArgs.add(index + SPLIT + addQuotes(dbValue));
					}
					if (value.equalsIgnoreCase("<Password>")) {
						String userName = fetchMetadataVO.getInput_value();

						dbValue = dataBaseEntry.getPassword(fetchMetadataVO.getTest_set_id(), userName, null);
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
			e.printStackTrace();
		}
		return response.toString();
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
		} catch (Exception e) {
			throw new WatsEBSCustomException(500, "Exception occured while uploading pdf in Object Storage", e);
		}
	}

	public void downloadScreenshotsFromObjectStore(String screenshotPath, String customerName, String TestRunName,
			String objectStoreScreenShotPath, String seqNum) {
		ConfigFileReader.ConfigFile configFile = null;
		List<String> objNames = null;
		try {
			configFile = ConfigFileReader.parse(new ClassPathResource("oci/config").getInputStream(), ociConfigName);
		} catch (IOException e) {
			throw new WatsEBSCustomException(500, "Exception occured while connecting to oci/config path", e);
		}

		final AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);

		try (ObjectStorage client = new ObjectStorageClient(provider);) {

//		String objectStoreScreenshotPath = objectStoreScreenShotPath + customerName + FORWARD_SLASH + TestRunName
//				+ FORWARD_SLASH + seqNum;

			String objectStoreScreenshotPath = objectStoreScreenShotPath + customerName + FORWARD_SLASH + TestRunName
					+ FORWARD_SLASH;

			ListObjectsRequest listObjectsRequest = ListObjectsRequest.builder().namespaceName(ociNamespace)
					.bucketName(ociBucketName).prefix(objectStoreScreenshotPath).delimiter("/").build();

			/* Send request to the Client */
			ListObjectsResponse response = client.listObjects(listObjectsRequest);

			objNames = response.getListObjects().getObjects().stream().map((objSummary) -> objSummary.getName())
					.collect(Collectors.toList());
			logger.info(objNames.size());
			ListIterator<String> listIt = objNames.listIterator();
			createDir(screenshotPath);
			while (listIt.hasNext()) {
				String objectName = listIt.next();
				GetObjectResponse getResponse = client.getObject(GetObjectRequest.builder().namespaceName(ociNamespace)
						.bucketName(ociBucketName).objectName(objectName).build());

				String imageName = objectName.substring(objectName.lastIndexOf("/") + 1, objectName.length());
				File file = new File(screenshotPath + File.separator + imageName);
				if (!file.exists()) {
					try (final InputStream stream = getResponse.getInputStream();
							// final OutputStream outputStream = new FileOutputStream(imagePath + imageName)

							final OutputStream outputStream = Files.newOutputStream(file.toPath(), CREATE_NEW)) {
						// use fileStream
						byte[] buf = new byte[8192];
						int bytesRead;
						while ((bytesRead = stream.read(buf)) > 0) {
							outputStream.write(buf, 0, bytesRead);
						}
					} catch (IOException e1) {
						throw new WatsEBSCustomException(500,
								"Exception occured while read or write screenshot from Object Storage", e1);
					}
				}
			}
		} catch (WatsEBSCustomException e) {
			throw e;
		} catch (Exception e) {
			throw new WatsEBSCustomException(500, "Exception occured while closing Object stroage path", e);
		}

	}

	public void deleteScreenshotsFromWindows(String screenShotFolderPath) {
		File folder1 = new File(screenShotFolderPath);
		if (folder1.exists()) {
			File folder = new File(screenShotFolderPath + File.separator);
			if (folder.exists()) {
				File[] listOfFiles = folder.listFiles();
				for (File file : Arrays.asList(listOfFiles)) {
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

	public ResponseDto generateTestScriptLineIdReports(MessageQueueDto args) {
		try {
			Boolean scriptStatus = dataBaseEntry.checkAllStepsStatusForAScript(args.getTestSetLineId());
			if (scriptStatus == null) {
				if (args.isManualTrigger()) {
					return new ResponseDto(200, Constants.WARNING, "Script Run In Progress");
				} else {
					scriptStatus = false;
				}
			}
			args.setSuccess(scriptStatus);
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

			List<ScriptDetailsDto> fetchMetadataListVO = dataBaseEntry.getScriptDetailsListVO(args.getTestSetId(),
					args.getTestSetLineId(), false, false);
			args.setSuccess(scriptStatus);

			String screenShotFolderPath = (fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION()
					+ customerDetails.getCustomerName() + File.separator + customerDetails.getTestSetName());
			String objectStore = fetchConfigVO.getScreenshot_path();
			String[] arrOfStr = objectStore.split(FORWARD_SLASH, 5);
			StringBuilder objectStoreScreenShotPath = new StringBuilder(arrOfStr[3]);
			for (int i = 4; i < arrOfStr.length; i++) {
				objectStoreScreenShotPath.append(FORWARD_SLASH + arrOfStr[i]);
			}

			String scriptId = fetchMetadataListVO.get(0).getScriptId();
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
					+ customerDetails.getTestSetName() + File.separator + fetchMetadataListVO.get(0).getSeqNum() + "_"
					+ fetchMetadataListVO.get(0).getScriptNumber() + PDF_EXTENSION + "AAAparent="
					+ fetchConfigVO.getImg_url();

			fetchConfigVO.setStarttime(testSetLine.getExecutionStartTime());
			fetchConfigVO.setStarttime1(testSetLine.getExecutionStartTime());
			deleteScreenshotsFromWindows(screenShotFolderPath);
			downloadScreenshotsFromObjectStore(screenShotFolderPath, customerDetails.getCustomerName(),
					customerDetails.getTestSetName(), objectStoreScreenShotPath.toString(),
					fetchMetadataListVO.get(0).getSeqNum() + "_");
			FetchScriptVO post = new FetchScriptVO(args.getTestSetId(), scriptId, args.getTestSetLineId(), passurl,
					failurl, detailurl, scripturl);
			Date enddate = null;
			boolean updateStatus = limitScriptExecutionService.updateStatusCheck(fetchConfigVO,
					customerDetails.getTestSetId(), fetchMetadataListVO.get(0).getScriptId(),
					fetchMetadataListVO.get(0).getScriptNumber(), fetchConfigVO.getStatus1());
			if (!updateStatus) {
				enddate = testSetLine.getExecutionEndTime();
			} else {
				enddate = dataBaseEntry.findStepMaxUpdatedDate(args.getTestSetLineId(),
						testSetLine.getExecutionStartTime());
			}
			String pdfName = null;
			fetchConfigVO.setEndtime(enddate);
			int failedScriptRunCount = 0;
			if (args.isSuccess()) {
				pdfName = fetchMetadataListVO.get(0).getSeqNum() + "_" + fetchMetadataListVO.get(0).getScriptNumber()
						+ PDF_EXTENSION;
				fetchConfigVO.setStatus1("Pass");
				limitScriptExecutionService.updateFaileScriptscount(args.getTestSetLineId(), args.getTestSetId());
			} else {
				fetchConfigVO.setErrormessage("EBS Execution Failed");
				fetchConfigVO.setStatus1(FAIL);
				failedScriptRunCount = limitScriptExecutionService.getFailScriptRunCount(args.getTestSetLineId(),
						args.getTestSetId());
				pdfName = fetchMetadataListVO.get(0).getSeqNum() + "_" + fetchMetadataListVO.get(0).getScriptNumber()
						+ "_RUN" + failedScriptRunCount + PDF_EXTENSION;
			}
			dataBaseEntry.updateTestCaseEndDate(post, enddate, fetchConfigVO.getStatus1());
//			dataService.updateTestCaseStatus(post, args.getTestSetId(), fetchConfigVO);

			/* Email processing Updating subscription table code */
			if (updateStatus) {
				dataBaseEntry.updateTestCaseStatus(post, fetchConfigVO, fetchMetadataListVO,
						testSetLine.getExecutionStartTime(), customerDetails.getTestSetName());
				if (fetchConfigVO.getStatus1().equals(FAIL)) {
					failedScriptRunCount = failedScriptRunCount + 1;
					limitScriptExecutionService.updateFailScriptRunCount(failedScriptRunCount, args.getTestSetLineId(),
							args.getTestSetId());
					pdfName = fetchMetadataListVO.get(0).getSeqNum() + "_"
							+ fetchMetadataListVO.get(0).getScriptNumber() + "_RUN" + failedScriptRunCount
							+ PDF_EXTENSION;
				}
			}
			createPdf(fetchMetadataListVO, fetchConfigVO, pdfName, customerDetails);
			// final reports generation
			if (!args.isManualTrigger()) {
				dataBaseEntry.insertScriptExecAuditRecord(args.getAutditTrial(), AUDIT_TRAIL_STAGES.ERG);

				String pdfGenerationEnabled = dataBaseEntry.pdfGenerationEnabled(Long.valueOf(args.getTestSetId()));
				if (BOOLEAN_STATUS.TRUE.getLabel().equalsIgnoreCase(pdfGenerationEnabled)) {
					boolean runFinalPdf = dataBaseEntry
							.checkIfAllTestSetLinesCompleted(Long.valueOf(args.getTestSetId()), true);
					if (runFinalPdf) {
						Date endDate = dataBaseEntry.findMaxExecutionEndDate(Long.valueOf(args.getTestSetId()));
						fetchConfigVO.setEndtime(endDate);
						dataBaseEntry.updatePdfGenerationEnableStatus(args.getTestSetId(),
								BOOLEAN_STATUS.FALSE.getLabel());
						testRunPdfGeneration(args.getTestSetId(), fetchConfigVO);
					}
				}
			}

		} catch (Exception e) {
			throw new WatsEBSCustomException(500, "Exception occured while generating the pdf", e);
		}
		return new ResponseDto(200, Constants.SUCCESS, null);
	}

	public void findPassAndFailCount(FetchConfigVO fetchConfigVO, String testSetId) {

		List<String> testLineStatusList = dataBaseEntry.getStatusByTestSetId(testSetId);
		fetchConfigVO.setSeqNumAndStatus(dataBaseEntry.getStatusAndSeqNum(testSetId));
		int passCount = 0;
		int failCount = 0;
		int other = 0;
		for (String testLinesStatus : testLineStatusList) {
			if (testLinesStatus.equalsIgnoreCase(TEST_SET_LINE_ID_STATUS.Pass.getLabel())) {
				passCount++;
			} else if (testLinesStatus.equalsIgnoreCase(TEST_SET_LINE_ID_STATUS.Fail.getLabel())) {
				failCount++;
			} else {
				other++;
			}
		}
		fetchConfigVO.setPasscount(passCount);
		fetchConfigVO.setFailcount(failCount);
		fetchConfigVO.setOtherCount(other);
	}

	private void testRunPdfGeneration(String testSetId, FetchConfigVO fetchConfigVO) {
		CustomerProjectDto customerDetails = dataBaseEntry.getCustomerDetails(testSetId);
		List<ScriptDetailsDto> fetchMetadataListVOFinal = dataBaseEntry.getScriptDetailsListVO(testSetId, null, true,
				false);
		dataBaseEntry.setPassAndFailScriptCount(testSetId, fetchConfigVO);
		try {
			createPdf(fetchMetadataListVOFinal, fetchConfigVO, "Passed_Report.pdf", customerDetails);
			createPdf(fetchMetadataListVOFinal, fetchConfigVO, "Failed_Report.pdf", customerDetails);
			createPdf(fetchMetadataListVOFinal, fetchConfigVO, "Detailed_Report.pdf", customerDetails);
		} catch (com.itextpdf.text.DocumentException e) {
			logger.error("Exception occured while creating TestLvlPDF" + e);
		}
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

	public String findExecutionTimeForScript(String testSetId, String pdffileName, Date tStarttime, Date tendTime,
			long tdiff) {

		Map<Date, Long> timeslist = limitScriptExecutionService.getStarttimeandExecutiontime(testSetId);
		String startTime = null;
		String executionTime = null;
		Timestamp startTimestamp = new Timestamp(tStarttime.getTime());
		Timestamp endTimestamp = new Timestamp(tendTime.getTime());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:aa");
		if (timeslist.size() == 0) {
			startTime = dateFormat.format(tStarttime);
			long tDiffSeconds = tdiff / 1000 % 60;
			long tDiffMinutes = tdiff / (60 * 1000) % 60;
			long tDiffHours = tdiff / (60 * 60 * 1000);
			String hr = tDiffHours > 0 ? tDiffHours + "hr " : "";
			String min = (tDiffMinutes > 0 && !hr.equals("")) ? tDiffMinutes + "min " : "";
			String sec = tDiffSeconds > 0 ? tDiffSeconds + "sec" : "";
			executionTime = hr + min + sec;
			if ("Detailed_Report.pdf".equalsIgnoreCase(pdffileName)) {
				limitScriptExecutionService.updateTestrunTimes(startTimestamp, endTimestamp, tdiff, testSetId);
			}
		} else {
			for (Entry<Date, Long> entryMap : timeslist.entrySet()) {
				startTime = dateFormat.format(entryMap.getKey());
				long totalTime = tdiff + entryMap.getValue();
				long tDiffSeconds = totalTime / 1000 % 60;
				long tDiffMinutes = totalTime / (60 * 1000) % 60;
				long tDiffHours = totalTime / (60 * 60 * 1000);
				String hr = tDiffHours > 0 ? tDiffHours + "hr " : "";
				String min = (tDiffMinutes > 0 && !hr.equals("")) ? tDiffMinutes + "min " : "";
				String sec = tDiffSeconds > 0 ? tDiffSeconds + "sec" : "";
				executionTime = hr + min + sec;
				if ("Detailed_Report.pdf".equalsIgnoreCase(pdffileName)) {
					limitScriptExecutionService.updateTestrunTimes1(endTimestamp, totalTime, testSetId);
				}
			}
		}
		return startTime + "_" + executionTime;
	}

	public List<String> getPassedPdfNew(List<ScriptDetailsDto> fetchMetadataListVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) throws IOException {

		String folder = fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + customerDetails.getCustomerName() + "\\"
				+ customerDetails.getTestSetName() + "\\";

		/* FOR MP4 FUNCTIONALITY */
		String videoRec = "no";

		Map<Integer, List<File>> filesMap = new TreeMap<>();
		List<String> targetPassedPdf = new ArrayList<>();
		Map<String, String> seqNumMap = new HashMap<>();
		for (Object[] obj : fetchConfigVO.getSeqNumAndStatus()) {
			seqNumMap.put(obj[0].toString(), obj[1].toString());
		}
		List<String> fileSeqList = fileSeqContainer(fetchMetadataListVO, customerDetails.getTestSetName());
		for (String fileNames : fileSeqList) {
			if (fileNames.endsWith(PASSED)) {
				fileNames = new File(folder + fileNames + ".png").exists() ? fileNames + ".png" : fileNames;
				fileNames = (!(fileNames.endsWith(".png")) && (new File(folder + fileNames + ".jpg").exists()))
						? fileNames + ".jpg"
						: fileNames;
				File newFile = new File(folder + fileNames);
				if (newFile.exists()) {
					Integer seqNum = Integer.valueOf(newFile.getName().substring(0, newFile.getName().indexOf('_')));
					if (seqNumMap.get(seqNum.toString()).equals("Pass")) {
						filesMap.putIfAbsent(seqNum, new ArrayList<File>());
						filesMap.get(seqNum).add(newFile);
						targetPassedPdf.add(newFile.getName());
					}
				}
			}
		}

		return targetPassedPdf;
	}

	public List<String> getFailedPdfNew(List<ScriptDetailsDto> fetchMetadataListVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) throws IOException {

		String folder = fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + customerDetails.getCustomerName() + "/"
				+ customerDetails.getTestSetName() + "/";
		String videoRec = "no";
		Map<String, String> seqNumMap = new HashMap<>();
		for (Object[] obj : fetchConfigVO.getSeqNumAndStatus()) {
			seqNumMap.put(obj[0].toString(), obj[1].toString());
		}
		List<String> targetFailedPdf = new ArrayList<>();
		List<String> fileSeqList = fileSeqContainer(fetchMetadataListVO, customerDetails.getTestSetName());
		Map<Integer, List<File>> filesMap = new TreeMap<>();
		for (String fileNames : fileSeqList) {
			fileNames = new File(folder + fileNames + ".png").exists() ? fileNames + ".png" : fileNames;
			fileNames = (!(fileNames.endsWith(".png")) && (new File(folder + fileNames + ".jpg").exists()))
					? fileNames + ".jpg"
					: fileNames;
			File newFile = new File(folder + fileNames);
			if (newFile.exists()) {
				Integer seqNum = Integer.valueOf(newFile.getName().substring(0, newFile.getName().indexOf('_')));
				if (seqNumMap.get(seqNum.toString()).equals("Fail")) {
					if (!filesMap.containsKey(seqNum)) {
						filesMap.put(seqNum, new ArrayList<File>());
					}
					filesMap.get(seqNum).add(newFile);
					targetFailedPdf.add(newFile.getName());
				}
			}
		}

		return targetFailedPdf;
	}

	public List<String> getDetailPdfNew(List<ScriptDetailsDto> fetchMetadataListVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) throws IOException {

		String folder = fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + customerDetails.getCustomerName() + "/"
				+ customerDetails.getTestSetName() + "/";
		Map<Integer, List<File>> filesMap = new TreeMap<>();
		List<String> fileSeqList = fileSeqContainer(fetchMetadataListVO, customerDetails.getTestSetName());
		List<String> detailsFileName = new ArrayList<>();
		for (String fileNames : fileSeqList) {
			fileNames = new File(folder + fileNames + ".png").exists() ? fileNames + ".png" : fileNames;
			fileNames = (!(fileNames.endsWith(".png")) && (new File(folder + fileNames + ".jpg").exists()))
					? fileNames + ".jpg"
					: fileNames;
			File file = new File(folder + fileNames);
			if (file.exists()) {
				Integer seqNum = Integer.valueOf(file.getName().substring(0, file.getName().indexOf('_')));
				if (!filesMap.containsKey(seqNum)) {
					filesMap.put(seqNum, new ArrayList<File>());
				}
				filesMap.get(seqNum).add(file);
				detailsFileName.add(fileNames);
			}
		}
		return detailsFileName;
	}

	public List<String> getFileNameListNew(List<ScriptDetailsDto> fetchMetadataListVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) throws IOException {

		String folder = fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + customerDetails.getCustomerName() + "/"
				+ customerDetails.getTestSetName() + "/";
		String videoRec = "no";
		List<File> fileList = new ArrayList<>();
		List<String> fileSeqList = fileSeqContainer(fetchMetadataListVO, customerDetails.getTestSetName());
		for (String newFile : fileSeqList) {
			newFile = new File(folder + newFile + ".png").exists() ? newFile + ".png" : newFile;
			newFile = (!(newFile.endsWith(".png")) && (new File(folder + newFile + ".jpg").exists())) ? newFile + ".jpg"
					: newFile;
			File file = new File(folder + newFile);
			if (file.exists()) {
				fileList.add(file);
			}
		}
		List<String> fileNameList = new ArrayList<>();
		for (int i = 0; i < fileList.size(); i++) {
			fileNameList.add(fileList.get(i).getName());
		}
		return fileNameList;
	}

	public List<String> fileSeqContainer(List<ScriptDetailsDto> fetchMetadataListVO, String testRunName) {
		List<String> fetchConfigVODtl = new ArrayList<>();
		for (ScriptDetailsDto fetchMetaData : fetchMetadataListVO) {
			if (fetchMetaData.getStatus().equals("Pass")) {
				fetchConfigVODtl.add(fetchMetaData.getSeqNum() + "_"

						+ fetchMetaData.getLineNumber() + "_" + fetchMetaData.getScenarioName() + "_"

						+ fetchMetaData.getScriptNumber() + "_" + testRunName + "_"

						+ fetchMetaData.getLineNumber() + "_Passed");
			} else if (fetchMetaData.getStatus().equals("Fail")) {
				fetchConfigVODtl.add(fetchMetaData.getSeqNum() + "_"

						+ fetchMetaData.getLineNumber() + "_" + fetchMetaData.getScenarioName() + "_"

						+ fetchMetaData.getScriptNumber() + "_" + testRunName + "_"

						+ fetchMetaData.getLineNumber() + "_Failed");
			}
		}
		return fetchConfigVODtl;
	}

	private void createPdf(List<ScriptDetailsDto> fetchMetadataListVO, FetchConfigVO fetchConfigVO, String pdffileName,
			CustomerProjectDto customerDetails) throws com.itextpdf.text.DocumentException {
		try {
			String folder = (fetchConfigVO.getWINDOWS_PDF_LOCATION() + customerDetails.getCustomerName()
					+ File.separator + customerDetails.getTestSetName() + File.separator);
			String file = (folder + pdffileName);
			findPassAndFailCount(fetchConfigVO, customerDetails.getTestSetId());

			List<String> fileNameList = null;
			if ("Passed_Report.pdf".equalsIgnoreCase(pdffileName)) {
				fileNameList = getPassedPdfNew(fetchMetadataListVO, fetchConfigVO, customerDetails);
			} else if ("Failed_Report.pdf".equalsIgnoreCase(pdffileName)) {
				fileNameList = getFailedPdfNew(fetchMetadataListVO, fetchConfigVO, customerDetails);
			} else if ("Detailed_Report.pdf".equalsIgnoreCase(pdffileName)) {
				fileNameList = getDetailPdfNew(fetchMetadataListVO, fetchConfigVO, customerDetails);
			} else {
				fileNameList = getFileNameListNew(fetchMetadataListVO, fetchConfigVO, customerDetails);
			}
			String executedBy = fetchMetadataListVO.get(0).getExecutedBy();
			createDir(folder);
			Document document = new Document();
			String report = EXECUTION_REPORT;
			Font font23 = FontFactory.getFont(ARIAL, 23);
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
			Rectangle pageSize = new Rectangle(1360, 800);
			document.setPageSize(pageSize);
			document.open();
			logger.info("before enter Images/wats_icon.png1");
			Image watsLogo = Image.getInstance(watslogo);
			logger.info("after enter Images/wats_icon.png1");
			watsLogo.scalePercent(65, 68);
			watsLogo.setAlignment(Image.ALIGN_RIGHT);
			Date tendTime = fetchConfigVO.getEndtime();
			fetchConfigVO.setStarttime1(new Date());
			Date tStarttime = fetchConfigVO.getStarttime1();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:aa");
			String tendtime1 = dateFormat.format(tendTime);
			long tdiff = tendTime.getTime() - tStarttime.getTime();
			String testRunName1 = customerDetails.getTestSetName();

			if ((!fileNameList.isEmpty()) && ("Passed_Report.pdf".equalsIgnoreCase(pdffileName)
					|| "Failed_Report.pdf".equalsIgnoreCase(pdffileName)
					|| "Detailed_Report.pdf".equalsIgnoreCase(pdffileName))) {
				int passcount = fetchConfigVO.getPasscount();
				int failcount = fetchConfigVO.getFailcount();
				int others = fetchConfigVO.getOtherCount();

				String[] startAndExecTime = findExecutionTimeForScript(customerDetails.getTestSetId(), pdffileName,
						tStarttime, tendTime, tdiff).split("_");
				String startTime = startAndExecTime[0];
				String executionTime = startAndExecTime[1];
				String endTime = tendtime1;
				String tr = TEST_RUN_NAME;
				String sn = EXECUTED_BY;
				String sn1 = START_TIME;
				String s1 = END_TIME;
				String scenarios1 = EXECUTION_TIME;
				String[] testArr = { tr, testRunName1, sn, executedBy, sn1, startTime, s1, endTime, scenarios1,
						executionTime };
				document.add(watsLogo);
				document.add(new Paragraph(report, font23));
				document.add(Chunk.NEWLINE);
				PdfPTable table1 = new PdfPTable(2);
				table1.setWidths(new int[] { 1, 1 });
				table1.setWidthPercentage(100f);
				for (String text : testArr) {
					eBSSeleniumKeyWords.insertCell(table1, text, Element.ALIGN_LEFT, 1, font23);
				}
				document.add(table1);

				if ("Detailed_Report.pdf".equalsIgnoreCase(pdffileName)) {
					generateDetailsPDF(document, watsLogo, passcount, failcount, others, writer);
				} else if ("Passed_Report.pdf".equalsIgnoreCase(pdffileName)) {
					generatePassPDF(document, passcount, failcount);
				} else {
					generateFailedPDF(document, passcount, failcount);
				}
				addRestOfPagesToPDF(document, fileNameList, watsLogo, fetchConfigVO, fetchMetadataListVO,
						customerDetails);
			} else if (!("Passed_Report.pdf".equalsIgnoreCase(pdffileName)
					|| "Failed_Report.pdf".equalsIgnoreCase(pdffileName)
					|| "Detailed_Report.pdf".equalsIgnoreCase(pdffileName))) {
				generateScriptLvlPDF(document, fetchConfigVO.getStarttime(), fetchConfigVO.getEndtime(), watsLogo,
						fetchMetadataListVO, fetchConfigVO, fileNameList, customerDetails);
			}
			document.close();

		} catch (Exception e) {
			logger.info("Not able to Create pdf {}", e);
		}
		try {
			String destinationFilePath = (customerDetails.getCustomerName() + FORWARD_SLASH
					+ customerDetails.getTestSetName() + FORWARD_SLASH) + pdffileName;

			String sourceFilePath = (fetchConfigVO.getWINDOWS_PDF_LOCATION() + customerDetails.getCustomerName()
					+ File.separator + customerDetails.getTestSetName() + File.separator) + pdffileName;
//			uploadObjectToObjectStore(sourceFilePath, destinationFilePath);
		} catch (Exception e) {
			logger.info(e);
		}
	}

	public void generateScriptLvlPDF(Document document, Date startTime, Date endTime, Image watsLogo,
			List<ScriptDetailsDto> fetchMetadataListVO, FetchConfigVO fetchConfigVO, List<String> fileNameList,
			CustomerProjectDto customerDetails) throws IOException, com.itextpdf.text.DocumentException {

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:aa");
		Font font23 = FontFactory.getFont(ARIAL, 23);
		Font fnt12 = FontFactory.getFont(ARIAL, 12);
		String report = EXECUTION_REPORT;
		String starttime1 = dateFormat.format(startTime);
		String endtime1 = dateFormat.format(endTime);
		long diff = endTime.getTime() - startTime.getTime();
		long time = 0;
		String sec = (time = diff / 1000 % 60) > 0 ? time + "sec" : "";
		String min = (time = diff / (60 * 1000) % 60) > 0 ? time + "min " : "";
		String hr = (time = diff / (60 * 60 * 1000)) > 0 ? time + "hr " : "";

		String scriptNumber2 = fetchMetadataListVO.get(0).getScenarioName();
		String scenario1 = fetchConfigVO.getStatus1();
		String executionTime = hr + min + sec;
		String tr = TEST_RUN_NAME;
		String sn = SCRIPT_NUMBER;
		String sn1 = SCENARIO_NAME;
		String scenarios1 = "Status ";
		String errorMsg = "ErrorMessage";
		String eb = EXECUTED_BY;
		String st = START_TIME;
		String et = END_TIME;
		String ex = EXECUTION_TIME;
		String testRunName1 = customerDetails.getTestSetName();
		String scriptNumber = fetchMetadataListVO.get(0).getScriptNumber();
		String executedBy = fetchMetadataListVO.get(0).getExecutedBy();
		String customerName = customerDetails.getCustomerName();
		String errorMsgs = fetchConfigVO.getErrormessage();
		document.add(watsLogo);

		document.add(new Paragraph(report, font23));
		document.add(Chunk.NEWLINE);
		PdfPTable table1 = new PdfPTable(2);
		table1.setWidths(new int[] { 1, 1 });
		table1.setWidthPercentage(100f);
		String[] strArr1 = { tr, testRunName1, sn, scriptNumber, sn1, scriptNumber2, scenarios1, scenario1 };
		String[] strArr2 = { eb, executedBy, st, starttime1, et, endtime1, ex, executionTime };
		for (String str : strArr1) {
			eBSSeleniumKeyWords.insertCell(table1, str, Element.ALIGN_LEFT, 1, font23);
		}
		if (errorMsgs != null) {
			eBSSeleniumKeyWords.insertCell(table1, errorMsg, Element.ALIGN_LEFT, 1, font23);
			eBSSeleniumKeyWords.insertCell(table1, errorMsgs, Element.ALIGN_LEFT, 1, font23);
		}
		for (String str : strArr2) {
			eBSSeleniumKeyWords.insertCell(table1, str, Element.ALIGN_LEFT, 1, font23);
		}

		document.add(table1);
		document.newPage();

		int i = 0;
		for (ScriptDetailsDto metaDataVO : fetchMetadataListVO) {
			String fileName = metaDataVO.getSeqNum() + "_" + metaDataVO.getLineNumber() + "_"
					+ metaDataVO.getScenarioName() + "_" + metaDataVO.getScriptNumber() + "_"
					+ customerDetails.getTestSetName() + "_" + metaDataVO.getLineNumber();
			String image = null;
			if (fileNameList.contains(fileName + "_Passed.png")) {
				image = fileName + "_Passed.png";
			} else if (fileNameList.contains(fileName + "_Passed.jpg")) {
				image = fileName + "_Passed.jpg";
			} else if (fileNameList.contains(fileName + "_Failed.png")) {
				image = fileName + "_Failed.png";
			} else if (fileNameList.contains(fileName + "_Failed.jpg")) {
				image = fileName + "_Failed.jpg";
			}

			if (image != null) {
				i++;
				Image img = Image.getInstance(fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + customerName + "/"
						+ testRunName1 + "/" + image);

				Rectangle pageSize = new Rectangle(img.getPlainWidth(), img.getPlainHeight() + 100);
				String status = image.split("_")[6].split("\\.")[0];
				String scenario = image.split("_")[2];
				String steps = image.split("_")[5];

				String stepDescription = metaDataVO.getTestRunParamDesc();
				String inputParam = metaDataVO.getInputParameter();
				String inputValue = metaDataVO.getInputValue();
				document.setPageSize(pageSize);
				document.newPage();
				String s = "Status: " + status;
				String scenarios = SCENARIO_NAME + " :" + scenario;
				watsLogo.scalePercent(65, 65);
				watsLogo.setAlignment(Image.ALIGN_RIGHT);
				document.add(watsLogo);
				document.add(new Paragraph(s, fnt12));
				document.add(new Paragraph(scenarios, fnt12));
				String step = status.equals(FAILED) ? "Failed at Line Number:" + "" + steps : "Step No :" + "" + steps;
				String failMsg = status.equals(FAILED) ? "Failed Message:" + "" + metaDataVO.getLineErrorMsg() : null;
				document.add(new Paragraph(step, fnt12));
				if (failMsg != null) {
					document.add(new Paragraph(failMsg, fnt12));
				}
				if (stepDescription != null) {
					document.add(new Paragraph(STEP_DESC + stepDescription, fnt12));
				}
				if (inputParam != null && inputValue != null) {
					document.add(new Paragraph(TEST_PARAM + inputParam, fnt12));
					document.add(new Paragraph(TEST_VALUE + inputValue, fnt12));
				}
				document.add(Chunk.NEWLINE);

				Paragraph p = new Paragraph(String.format("page %s of %s", i, fileNameList.size()));
				p.setAlignment(Element.ALIGN_RIGHT);
				img.setAlignment(Image.ALIGN_CENTER);
				img.isScaleToFitHeight();
				img.scalePercent(60, 62);
				document.add(img);
				document.add(p);
			}
		}

	}

	public void addRestOfPagesToPDF(Document document, List<String> fileNameList, Image watsLogo,
			FetchConfigVO fetchConfigVO, List<ScriptDetailsDto> fetchMetadataListVO, CustomerProjectDto customerDetails)
			throws IOException, com.itextpdf.text.DocumentException {
		int k = 0;
		int l = 0;
		String sno1 = "";
		Map<Integer, Map<String, String>> toc = new TreeMap<>();
		String customerName = customerDetails.getCustomerName();
		String testRunName1 = customerDetails.getTestSetName();
		Font font23 = FontFactory.getFont(ARIAL, 23);
		Font fnt12 = FontFactory.getFont(ARIAL, 12);
		Font bf15 = FontFactory.getFont(ARIAL, 23, Font.UNDERLINE);
		Font bf16 = FontFactory.getFont(ARIAL, 12, Font.UNDERLINE, new BaseColor(66, 245, 236));
		Font bf13 = FontFactory.getFont(ARIAL, 23, Font.UNDERLINE, BaseColor.GREEN);
		Font bf14 = FontFactory.getFont(ARIAL, 23, Font.UNDERLINE, BaseColor.RED);
		Font bfBold = FontFactory.getFont(ARIAL, 23, BaseColor.WHITE);
		Map<String, String> seqNumMap = new HashMap<>();
		for (Object[] obj : fetchConfigVO.getSeqNumAndStatus()) {
			seqNumMap.put(obj[0].toString(), obj[1].toString());
		}
		for (String image : fileNameList) {
			k++;
			String sndo = image.split("_")[0]; // SEQ NUM
			String name = image.split("_")[3];// SCRIPT NUM
			Map<String, String> toc2 = new TreeMap<>();

			/* JUST CHECK THE CODE IS IT WORKJIng OR NOT */
			if (!sndo.equalsIgnoreCase(sno1)) {
				Map<String, String> toc1 = new TreeMap<>();
				for (String image1 : fileNameList) {
					if (image1.startsWith(sndo + "_") && seqNumMap.get(sndo).equals("Fail")) {
						toc2.put(sndo, FAILED + l);
						l++;
					}
				}
				String str = String.valueOf(toc2.get(sndo));
				toc1.put(sndo + "_" + name, str);
				toc.put(k, toc1);

			}
			if (sndo != null) {
				sno1 = sndo;
			}
		}
		sno1 = "";
		document.newPage();
		document.add(watsLogo);
		Anchor target2 = new Anchor(String.valueOf("Page Numbers"), bfBold);
		target2.setName(String.valueOf("details"));
		Chunk ch1 = new Chunk("Script Numbers", bfBold);
		ch1.setBackground(new BaseColor(38, 99, 175), 0f, 10f, 1730f, 15f);
		Paragraph p2 = new Paragraph();
		p2.add(ch1);
		p2.add(new Chunk(new VerticalPositionMark()));
		p2.add(target2);
		document.add(p2);
		document.add(Chunk.NEWLINE);

		Chunk dottedLine = new Chunk(new DottedLineSeparator());
		for (Entry<Integer, Map<String, String>> entry : toc.entrySet()) {
			Map<String, String> str1 = entry.getValue();
			for (Entry<String, String> entry1 : str1.entrySet()) {
				Anchor click = new Anchor(String.valueOf(entry.getKey()), bf15);
				click.setReference("#" + entry1.getKey());
				Anchor click1 = new Anchor(String.valueOf("(Failed)"), bf14);
				click1.setReference("#" + entry1.getValue());
				Paragraph pr = new Paragraph();
				Anchor ca1 = new Anchor(entry1.getKey(), bf15);
				ca1.setReference("#" + entry1.getKey());
				String compare = entry1.getValue();
				if (!compare.equals("null")) {
					pr.add(ca1);

					pr.add(click1);
					pr.add(dottedLine);
					pr.add(click);
					document.add(Chunk.NEWLINE);
					document.add(pr);
				} else {
					Anchor click2 = new Anchor(String.valueOf("(Passed)"), bf13);
					click2.setReference("#" + entry1.getKey());
					pr.add(ca1);
					pr.add(click2);
					pr.add(dottedLine);
					pr.add(click);
					document.add(Chunk.NEWLINE);
					document.add(pr);
				}
			}
		}

		int i = 0;
		int j = 0;
		for (ScriptDetailsDto metaDataVO : fetchMetadataListVO) {
			String fileName = metaDataVO.getSeqNum() + "_" + metaDataVO.getLineNumber() + "_"
					+ metaDataVO.getScenarioName() + "_" + metaDataVO.getScriptNumber() + "_"
					+ customerDetails.getTestSetName() + "_" + metaDataVO.getLineNumber();
			String image = null;
			if (fileNameList.contains(fileName + "_Passed.png")) {
				image = fileName + "_Passed.png";
			} else if (fileNameList.contains(fileName + "_Passed.jpg")) {
				image = fileName + "_Passed.jpg";
			} else if (fileNameList.contains(fileName + "_Failed.png")) {
				image = fileName + "_Failed.png";
			} else if (fileNameList.contains(fileName + "_Failed.jpg")) {
				image = fileName + "_Failed.jpg";
			}

			if (image != null) {
				i++;
				Image img = Image.getInstance(fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + customerName + "/"
						+ testRunName1 + "/" + image);
				Rectangle pageSize = new Rectangle(img.getPlainWidth(), img.getPlainHeight() + 100);
				String sno = image.split("_")[0];
				String sNo = SCRIPT_NUMBER;
				String scriptNumber1 = image.split("_")[3];
				String snm = SCENARIO_NAME;
				String scriptName = image.split("_")[2];

				if (!sno.equalsIgnoreCase(sno1)) {
					document.setPageSize(pageSize);
					document.newPage();
					document.add(watsLogo);
					Anchor target3 = new Anchor("Script Details", font23);
					target3.setName(sno + "_" + scriptNumber1);
					Paragraph pa = new Paragraph();
					pa.add(target3);
					document.add(pa);
					document.add(Chunk.NEWLINE);
					PdfPTable table2 = new PdfPTable(2);
					table2.setWidths(new int[] { 1, 1 });
					table2.setWidthPercentage(100f);
					String[] strArr = { sNo, scriptNumber1, snm, scriptName };
					for (String str : strArr) {
						eBSSeleniumKeyWords.insertCell(table2, str, Element.ALIGN_LEFT, 1, font23);
					}

					for (Entry<String, String> entry1 : toc.get(i).entrySet()) {
						String str = entry1.getValue();
						if (!str.equals("null")) {
							eBSSeleniumKeyWords.insertCell(table2, CONST[0], Element.ALIGN_LEFT, 1, font23);
							eBSSeleniumKeyWords.insertCell(table2, FAILED, Element.ALIGN_LEFT, 1, font23);
						} else {
							eBSSeleniumKeyWords.insertCell(table2, CONST[0], Element.ALIGN_LEFT, 1, font23);
							eBSSeleniumKeyWords.insertCell(table2, PASSED, Element.ALIGN_LEFT, 1, font23);
						}
					}

					document.add(table2);
				}
				if (sno != null) {
					sno1 = sno;
				}
				String status = image.split("_")[6].split("\\.")[0];
				String scenario = image.split("_")[2];

				String scenarios = SCENARIO_NAME + " :" + scenario;

				String sndo = image.split("_")[0];
				watsLogo.scalePercent(65, 68);
				Rectangle one1 = new Rectangle(1360, 1000);
				watsLogo.setAlignment(Image.ALIGN_RIGHT);
				if (image.startsWith(sndo + "_") && image.contains(FAILED)) {
					document.setPageSize(one1);
					document.newPage();
				} else {

					document.setPageSize(pageSize);
					document.newPage();
				}
				document.add(watsLogo);
				document.add(new Paragraph(scenarios, fnt12));
				String reason = image.split("_")[5];
				String step = STEP_NO + "" + reason;

				String stepDescription = metaDataVO.getTestRunParamDesc();

				String inputParam = metaDataVO.getInputParameter();

				String inputValue = metaDataVO.getInputValue();

				Paragraph pr1 = new Paragraph();
				pr1.add("Status:");

				if (image.startsWith(sndo + "_") && image.contains(FAILED)) {
					String message = "Failed at Line Number:" + "" + reason;
					String error = metaDataVO.getLineErrorMsg();
					String errorMessage = "Failed Message:" + "" + error;
					Anchor target1 = new Anchor(status);
					target1.setName(String.valueOf(status + j));
					j++;
					pr1.add(target1);
					document.add(pr1);
					document.add(new Paragraph(message, fnt12));
					if (error != null) {
						document.add(new Paragraph(errorMessage, fnt12));
					}
					if (stepDescription != null) {
						document.add(new Paragraph(STEP_DESC + stepDescription, fnt12));
					}
					if (inputParam != null) {
						document.add(new Paragraph(TEST_PARAM + inputParam, fnt12));
						if (inputValue != null) {
							document.add(new Paragraph(TEST_VALUE + inputValue, fnt12));
						}
					}
					document.add(Chunk.NEWLINE);
					img.setAlignment(Image.ALIGN_CENTER);
					img.isScaleToFitHeight();
					// new change-change page size
					img.scalePercent(60, 60);
					document.add(img);

				} else {
					document.add(new Paragraph(step, fnt12));
					Anchor target1 = new Anchor(status);
					target1.setName(String.valueOf(status));
					pr1.add(target1);
					document.add(pr1);

					if (stepDescription != null) {
						document.add(new Paragraph(STEP_DESC + stepDescription, fnt12));
					}
					if (inputParam != null) {
						document.add(new Paragraph(TEST_PARAM + inputParam, fnt12));
						if (inputValue != null) {
							document.add(new Paragraph(TEST_VALUE + inputValue, fnt12));
						}
					}
					img.setAlignment(Image.ALIGN_CENTER);
					img.isScaleToFitHeight();
					img.scalePercent(60, 68);
					document.add(img);
				}

				Anchor target = new Anchor(String.valueOf(i));
				target.setName(String.valueOf(i));
				Anchor target1 = new Anchor(String.valueOf("Back to Index"), bf16);
				target1.setReference("#" + "details");
				Paragraph p = new Paragraph();
				p.add(target1);
				p.add(new Chunk(new VerticalPositionMark()));
				p.add(" page ");
				p.add(target);
				p.add(" of " + fileNameList.size());
				document.add(p);
			}

		}

	}

	public void generateFailedPDF(Document document, int passcount, int failcount)
			throws DocumentException, com.itextpdf.text.DocumentException {
		Font font23 = FontFactory.getFont(ARIAL, 23);
		String start = EXECUTION_SUMMARY;
		document.add(Chunk.NEWLINE);
		Paragraph executionSummery = new Paragraph(start, font23);
		document.add(executionSummery);
		document.add(Chunk.NEWLINE);
		DecimalFormat df1 = new DecimalFormat("0");
		DecimalFormat df2 = new DecimalFormat("0");
		double fail = Math.round((failcount * 100.0) / (passcount + failcount));
		PdfPTable table = new PdfPTable(3);
		table.setWidths(new int[] { 1, 1, 1 });
		table.setWidthPercentage(100f);
		for (String str : CONST) {
			eBSSeleniumKeyWords.insertCell(table, str, Element.ALIGN_CENTER, 1, font23);
		}
		PdfPCell[] cells1 = table.getRow(0).getCells();
		for (int k = 0; k < cells1.length; k++) {
			cells1[k].setBackgroundColor(new BaseColor(161, 190, 212));
		}
		String[] strArr = { FAILED, df1.format(failcount), df2.format(fail) + "%" };
		for (String str : strArr) {
			eBSSeleniumKeyWords.insertCell(table, str, Element.ALIGN_CENTER, 1, font23);
		}
		document.setMargins(20, 20, 20, 20);
		document.add(table);
	}

	public void generatePassPDF(Document document, int passCount, int failCount)
			throws DocumentException, com.itextpdf.text.DocumentException {
		Font font23 = FontFactory.getFont(ARIAL, 23);
		String start = EXECUTION_SUMMARY;
		document.add(Chunk.NEWLINE);
		Paragraph executionSummery = new Paragraph(start, font23);
		document.add(executionSummery);
		document.add(Chunk.NEWLINE);
		DecimalFormat df1 = new DecimalFormat("0");
		DecimalFormat df2 = new DecimalFormat("0");
		double pass = Math.round((passCount * 100.0) / (passCount + failCount));
		PdfPTable table = new PdfPTable(3);
		table.setWidths(new int[] { 1, 1, 1 });
		table.setWidthPercentage(100f);
		for (String consts : CONST) {
			eBSSeleniumKeyWords.insertCell(table, consts, Element.ALIGN_CENTER, 1, font23);
		}
		PdfPCell[] cells1 = table.getRow(0).getCells();
		for (int k = 0; k < cells1.length; k++) {
			cells1[k].setBackgroundColor(new BaseColor(161, 190, 212));
		}
		String[] strArr = { "Status", df1.format(passCount), df2.format(pass) + "%" };
		for (String str : strArr) {
			eBSSeleniumKeyWords.insertCell(table, str, Element.ALIGN_CENTER, 1, font23);
		}
		document.setMargins(20, 20, 20, 20);
		document.add(table);

	}

	public void generateDetailsPDF(Document document, Image watsLogo, int passCount, int failCount, int others,
			PdfWriter writer) throws DocumentException, com.itextpdf.text.DocumentException {
		String start = EXECUTION_SUMMARY;
		String pichart = "Pie-Chart";
		Font font23 = FontFactory.getFont(ARIAL, 23);
		Font fontWhite23 = FontFactory.getFont(ARIAL, 23, BaseColor.WHITE);
		document.add(Chunk.NEWLINE);
		Paragraph executionSummery = new Paragraph(start, font23);
		document.add(executionSummery);
		document.add(Chunk.NEWLINE);
		DecimalFormat df1 = new DecimalFormat("0");
		DecimalFormat df2 = new DecimalFormat("0");
		double pass = Math.round((passCount * 100.0) / (passCount + failCount + others));
		double fail = Math.round((failCount * 100.0) / (passCount + failCount + others));
		double other = Math.round(others * 100.0) / (passCount + failCount + others);
		DefaultPieDataset dataSet = new DefaultPieDataset();
		if (passCount == 0 && others == 0) {
			dataSet.setValue(FAIL, fail);
		} else if (failCount == 0 && others == 0) {
			dataSet.setValue(PASS, pass);
		} else if (passCount == 0 && failCount == 0) {
			dataSet.setValue(IN_COMPLETE, other);
		} else if (passCount != 0 && others != 0 && failCount == 0) {
			dataSet.setValue(PASS, pass);
			dataSet.setValue(IN_COMPLETE, other);
		} else if (passCount == 0 && others != 0 && failCount != 0) {
			dataSet.setValue(FAIL, fail);
			dataSet.setValue(IN_COMPLETE, other);
		} else if (passCount != 0 && others == 0 && failCount != 0) {
			dataSet.setValue(PASS, pass);
			dataSet.setValue(FAIL, fail);
		} else if (passCount != 0 && others != 0 && failCount != 0) {
			dataSet.setValue(PASS, pass);
			dataSet.setValue(FAIL, fail);
			dataSet.setValue(IN_COMPLETE, other);
		}
		PdfPTable table = new PdfPTable(3);
		table.setWidths(new int[] { 1, 1, 1 });
		table.setWidthPercentage(100f);
		for (String consts : CONST) {
			eBSSeleniumKeyWords.insertCell(table, consts, Element.ALIGN_CENTER, 1, font23);
		}
		PdfPCell[] cells1 = table.getRow(0).getCells();
		for (int k = 0; k < cells1.length; k++) {
			cells1[k].setBackgroundColor(new BaseColor(161, 190, 212));
		}
		String[] strArr = { PASSED, df1.format(passCount), df2.format(pass) + "%", FAILED, df1.format(failCount),
				df2.format(fail) + "%" };

		for (String str : strArr) {
			eBSSeleniumKeyWords.insertCell(table, str, Element.ALIGN_CENTER, 1, font23);
		}
		document.setMargins(20, 20, 20, 20);
		document.add(table);

		Chunk ch = new Chunk(pichart, fontWhite23);
		ch.setTextRise(-18);
		ch.setBackground(new BaseColor(38, 99, 175), 0f, 10f, 1730f, 15f);
		document.newPage();
		document.add(watsLogo);
		Paragraph p1 = new Paragraph(ch);
		p1.setSpacingBefore(50);
		document.add(p1);

		JFreeChart chart = ChartFactory.createPieChart("", dataSet, true, true, false);
		Color c1 = Color.GREEN;
		Color c = Color.RED;
		Color gray = Color.GRAY;

		LegendTitle legend = chart.getLegend();
		PiePlot piePlot = (PiePlot) chart.getPlot();
		piePlot.setSectionPaint("Pass", c1);
		piePlot.setSectionPaint("Fail", c);
		piePlot.setSectionPaint("In Complete", gray);

		piePlot.setBackgroundPaint(Color.WHITE);
		piePlot.setOutlinePaint(null);
		piePlot.setLabelBackgroundPaint(null);
		piePlot.setLabelOutlinePaint(null);
		piePlot.setLabelGenerator(new StandardPieSectionLabelGenerator());
		piePlot.setInsets(new RectangleInsets(10, 5.0, 5.0, 5.0));
		piePlot.setLabelShadowPaint(null);
		piePlot.setShadowXOffset(0.0D);
		piePlot.setShadowYOffset(0.0D);
		piePlot.setLabelGenerator(null);
		piePlot.setBackgroundAlpha(0.4f);
		piePlot.setExplodePercent("Pass", 0.05);
		piePlot.setSimpleLabels(true);
		piePlot.setSectionOutlinesVisible(false);
		java.awt.Font f2 = new java.awt.Font("", java.awt.Font.PLAIN, 22);
		piePlot.setLabelFont(f2);
		PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator("{2}", new DecimalFormat("0"),
				new DecimalFormat("0%"));
		piePlot.setLabelGenerator(gen);
		legend.setPosition(RectangleEdge.RIGHT);
		legend.setVerticalAlignment(VerticalAlignment.CENTER);
		piePlot.setInsets(new RectangleInsets(0.0, 5.0, 5.0, 5.0));
		legend.setFrame(BlockBorder.NONE);
		legend.setFrame(new LineBorder(Color.white, new BasicStroke(20f), new RectangleInsets(1.0, 1.0, 1.0, 1.0)));

		java.awt.Font pass1 = new java.awt.Font("", Font.NORMAL, 22);
		legend.setItemFont(pass1);
		PdfContentByte contentByte = writer.getDirectContent();
		PdfTemplate template = contentByte.createTemplate(1000, 900);
		@SuppressWarnings("deprecation")
		Graphics2D graphics2d = template.createGraphics(700, 400, new DefaultFontMapper());
		Rectangle2D rectangle2d = new Rectangle2D.Double(0, 0, 600, 400);
		chart.draw(graphics2d, rectangle2d);
		graphics2d.dispose();
		contentByte.addTemplate(template, 400, 100);
	}

	public void updateStartStatus(MessageQueueDto args) throws ClassNotFoundException, SQLException {
		dataBaseEntry.updateInProgressScriptStatus(null, args.getTestSetLineId(), new Date());
	}

	public void updateScriptParamStatus(UpdateScriptParamStatus args) throws ClassNotFoundException, SQLException {
		String status = args.isSuccess() ? SCRIPT_PARAM_STATUS.PASS.getLabel() : SCRIPT_PARAM_STATUS.FAIL.getLabel();
		if (args.getResult() == null) {
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
				fetchConfigVO.setStarttime1(startDate);
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

	public void movePyjabScriptFilesToObjectStore() {
		String actionsFilePath = environment.getProperty("pyjab.scripts.git.path")
				+ environment.getProperty("pyjab.actions.script.name");
		String customerSpecificScriptPath = environment.getProperty("pyjab.scripts.git.path")
				+ environment.getProperty("pyjab.customer.specific.name");
		uploadObjectToObjectStore(actionsFilePath, environment.getProperty("pyjab.script.path.in.oci")
				+ environment.getProperty("pyjab.actions.script.name"));
		uploadObjectToObjectStore(customerSpecificScriptPath, environment.getProperty("pyjab.script.path.in.oci")
				+ environment.getProperty("pyjab.customer.specific.name"));
	}

	public void createFailedPdf(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO,
			String pdffileName, Date Starttime, Date endtime)
			throws IOException, DocumentException, com.itextpdf.text.DocumentException {
		try {
			String Date = DateUtils.getSysdate();
			String Folder = (fetchConfigVO.getWINDOWS_PDF_LOCATION() + fetchMetadataListVO.get(0).getCustomer_name()
					+ "/" + fetchMetadataListVO.get(0).getTest_run_name() + "/");
			String FILE = (Folder + pdffileName);
			List<String> fileNameList = null;
			if ("Passed_Report.pdf".equalsIgnoreCase(pdffileName)) {
//				fileNameList = getPassedPdfNew(fetchMetadataListVO, fetchConfigVO);
			} else if ("Failed_Report.pdf".equalsIgnoreCase(pdffileName)) {
//				fileNameList = getFailedPdfNew(fetchMetadataListVO, fetchConfigVO);
			}
			if ("Detailed_Report.pdf".equalsIgnoreCase(pdffileName)) {
				fileNameList = eBSSeleniumKeyWords.getDetailPdfNew(fetchMetadataListVO, fetchConfigVO);
			} else {
				fileNameList = eBSSeleniumKeyWords.getFailFileNameListNew(fetchMetadataListVO, fetchConfigVO);
			}

			String Script_Number = fetchMetadataListVO.get(0).getScript_number();
			String customer_Name = fetchMetadataListVO.get(0).getCustomer_name();
			String test_Run_Name = fetchMetadataListVO.get(0).getTest_run_name();
			String Scenario_Name = fetchMetadataListVO.get(0).getScenario_name();
			// new change add ExecutedBy field
			String ExecutedBy = fetchMetadataListVO.get(0).getExecuted_by();
			String ScriptDescription1 = fetchMetadataListVO.get(0).getScenario_name();
			File theDir = new File(Folder);
			if (!theDir.exists()) {
				System.out.println("creating directory: " + theDir.getName());
				boolean result = false;
				try {
					theDir.mkdirs();
					result = true;
				} catch (SecurityException se) {
					// handle it
					System.out.println(se.getMessage());
				}
			} else {
				System.out.println("Folder exist");
			}
			Font bf12 = FontFactory.getFont("Arial", 23);
			System.out.println("before enter Images/wats_icon.png");
			Image img1 = Image.getInstance(watslogo);
			System.out.println("after enter Images/wats_icon.png");
			img1.scalePercent(65, 68);
			img1.setAlignment(Image.ALIGN_RIGHT);
			Font bfBold12 = FontFactory.getFont("Arial", 23);
			String Report = EXECUTION_REPORT;
			Font fnt = FontFactory.getFont("Arial", 12);
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:aa");
			String Starttime1 = dateFormat.format(Starttime);
			String endtime1 = dateFormat.format(endtime);
			long diff = endtime.getTime() - Starttime.getTime();
			long diffSeconds = diff / 1000 % 60;
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000);
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(FILE));
			Rectangle one = new Rectangle(1360, 800);
			document.setPageSize(one);
			document.open();
			String TestRun = test_Run_Name;
			String ScriptNumber = Script_Number;
			String error = fetchConfigVO.getErrormessage();
			String ScriptNumber1 = Scenario_Name;
			String Scenario1 = fetchConfigVO.getStatus1();
//			String ExecutedBy=fetchConfigVO.getApplication_user_name();
			String StartTime = Starttime1;
			String EndTime = endtime1;
			String ExecutionTime = diffHours + ":" + diffMinutes + ":" + diffSeconds;

			String TR = "Test Run Name";
			String SN = "Script Number";
			String SN1 = "Scenario Name";
			String Scenarios1 = "Status ";
			String showErrorMessage = "	ErrorMessage ";
			String EB = "Executed By";
			String ST = "Start Time";
			String ET = "End Time";
			String EX = "Execution Time";

			document.add(img1);

			document.add(new Paragraph(Report, bfBold12));
			document.add(Chunk.NEWLINE);
			PdfPTable table1 = new PdfPTable(2);
			table1.setWidths(new int[] { 1, 1 });
			table1.setWidthPercentage(100f);

			eBSSeleniumKeyWords.insertCell(table1, TR, Element.ALIGN_LEFT, 1, bf12);
			eBSSeleniumKeyWords.insertCell(table1, TestRun, Element.ALIGN_LEFT, 1, bf12);
			eBSSeleniumKeyWords.insertCell(table1, SN, Element.ALIGN_LEFT, 1, bf12);
			eBSSeleniumKeyWords.insertCell(table1, ScriptNumber, Element.ALIGN_LEFT, 1, bf12);
			eBSSeleniumKeyWords.insertCell(table1, SN1, Element.ALIGN_LEFT, 1, bf12);
			eBSSeleniumKeyWords.insertCell(table1, ScriptNumber1, Element.ALIGN_LEFT, 1, bf12);
			eBSSeleniumKeyWords.insertCell(table1, Scenarios1, Element.ALIGN_LEFT, 1, bf12);
			eBSSeleniumKeyWords.insertCell(table1, Scenario1, Element.ALIGN_LEFT, 1, bf12);
			eBSSeleniumKeyWords.insertCell(table1, showErrorMessage, Element.ALIGN_LEFT, 1, bf12);
			eBSSeleniumKeyWords.insertCell(table1, error, Element.ALIGN_LEFT, 1, bf12);
			eBSSeleniumKeyWords.insertCell(table1, EB, Element.ALIGN_LEFT, 1, bf12);
			eBSSeleniumKeyWords.insertCell(table1, ExecutedBy, Element.ALIGN_LEFT, 1, bf12);
			eBSSeleniumKeyWords.insertCell(table1, ST, Element.ALIGN_LEFT, 1, bf12);
			eBSSeleniumKeyWords.insertCell(table1, StartTime, Element.ALIGN_LEFT, 1, bf12);
			eBSSeleniumKeyWords.insertCell(table1, ET, Element.ALIGN_LEFT, 1, bf12);
			eBSSeleniumKeyWords.insertCell(table1, EndTime, Element.ALIGN_LEFT, 1, bf12);
			eBSSeleniumKeyWords.insertCell(table1, EX, Element.ALIGN_LEFT, 1, bf12);
			eBSSeleniumKeyWords.insertCell(table1, ExecutionTime, Element.ALIGN_LEFT, 1, bf12);
			document.add(table1);
			document.newPage();
//End to add Script level details
//				Start to add screenshoots and pagenumbers and wats icon		 		
			int i = 0;
			for (String image : fileNameList) {
				i++;
				Image img = Image.getInstance(fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + customer_Name + "/"
						+ test_Run_Name + "/" + image);

//						String ScriptNumber = image.split("_")[3];
//						String TestRun = image.split("_")[4];
				String Status = image.split("_")[6];
				String status = Status.split("\\.")[0];
				String Scenario = image.split("_")[2];

				if (status.equalsIgnoreCase("Failed")) {// Rectangle one2 = new Rectangle(1360,1000);
					document.setPageSize(one);
					document.newPage();
				} else {

					document.setPageSize(img);
					document.newPage();
				}

				document.add(img1);
				String Reason = image.split("_")[5];
				// String TR = "Test Run Name:" + " " + TestRun;
//						String SN = "Script Number:" + " " + ScriptNumber;
				String S = "Status:" + " " + status;
				String step = "Step No :" + "" + Reason;
				String Scenarios = "Scenario Name :" + "" + Scenario;
				String Message = "Failed at Line Number:" + "" + Reason;
				String errorMessage = "Failed Message:" + "" + fetchConfigVO.getErrormessage();
				// String message = "Failed at
				// :"+fetchMetadataListVO.get(0).getInput_parameter();
//						document.add(new Paragraph(TR, fnt));
//						document.add(new Paragraph(SN, fnt));
				document.add(new Paragraph(S, fnt));
				document.add(new Paragraph(Scenarios, fnt));
//new change-failed pdf to add pagesize
				if (status.equalsIgnoreCase("Failed")) {
					document.add(new Paragraph(Message, fnt));
					if (fetchConfigVO.getErrormessage() != null) {
						document.add(new Paragraph(errorMessage, fnt));
					}
					document.add(Chunk.NEWLINE);
					img.setAlignment(Image.ALIGN_CENTER);
					img.isScaleToFitHeight();
					// new change-change page size
					img.scalePercent(60, 58);
					document.add(img);
				} else {
					document.add(new Paragraph(step, fnt));
					document.add(Chunk.NEWLINE);
					img.setAlignment(Image.ALIGN_CENTER);
					img.isScaleToFitHeight();
					// new change-change page size
					img.scalePercent(60, 62);
					document.add(img);
				}

				Paragraph p = new Paragraph(String.format("page %s of %s", i, fileNameList.size()));
				p.setAlignment(Element.ALIGN_RIGHT);

				document.add(p);
				System.out.println("This Image " + "" + image + "" + "was added to the report");
//				End to add screenshoots and pagenumbers and wats icon
				// End to create Script level passed reports

			}
			document.close();
//			compress(fetchMetadataListVO, fetchConfigVO, pdffileName);
			try {
				System.out.println(" %%%%%%%%% ");
				String destinationFilePath = (fetchMetadataListVO.get(0).getCustomer_name() + "/"
						+ fetchMetadataListVO.get(0).getTest_run_name() + "/") + pdffileName;

				String sourceFilePath = (fetchConfigVO.getWINDOWS_PDF_LOCATION()
						+ fetchMetadataListVO.get(0).getCustomer_name() + "\\"
						+ fetchMetadataListVO.get(0).getTest_run_name() + "\\") + pdffileName;

				uploadObjectToObjectStore(sourceFilePath, destinationFilePath);
			} catch (Exception e) {
				System.out.println(e);
			}

		} catch (Exception e) {
			System.out.println("Not able to upload the pdf");
			e.printStackTrace();
		}
	}

	@KafkaListener(topics = "update-audit-logs", groupId = "wats-group")
	public void updateAuditLogs(MessageQueueDto event) {
		dataBaseEntry.insertScriptExecAuditRecord(event.getAutditTrial(), event.getStage());
	}

}
