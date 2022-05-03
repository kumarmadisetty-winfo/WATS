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
import java.util.TimeZone;
import java.util.TreeMap;
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
import com.winfo.model.PyJabActions;
import com.winfo.model.TestSetScriptParam;
import com.winfo.scripts.EBSSeleniumKeyWords;
import com.winfo.utils.Constants;
import com.winfo.utils.Constants.BOOLEAN_STATUS;
import com.winfo.utils.Constants.SCRIPT_PARAM_STATUS;
import com.winfo.utils.DateUtils;
import com.winfo.vo.PyJabKafkaDto;
import com.winfo.vo.PyJabScriptDto;
import com.winfo.vo.ResponseDto;
import com.winfo.vo.UpdateScriptParamStatus;

@Service
public class TestScriptExecService {

	public final Logger logger = LogManager.getLogger(TestScriptExecService.class);
	private static final String PY_EXTN = ".py";
	public static final String topic = "test-script-run";
	public static final String FORWARD_SLASH = "/";
	public static final String SPLIT = "@";

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
	private KafkaTemplate<String, PyJabKafkaDto> kafkaTemp;

	@Autowired
	PyJabActionRepo actionRepo;

	@Autowired
	EBSSeleniumKeyWords eBSSeleniumKeyWords;

	@Autowired
	Environment environment;

	public String getTestSetMode(Long testSetId) {
		return dataBaseEntry.getTestSetMode(testSetId);

	}

	public ResponseDto run(String testSetId) throws MalformedURLException {
		ResponseDto executeTestrunVo = new ResponseDto();

		try {
			dataBaseEntry.updatePdfGenerationEnableStatus(testSetId, BOOLEAN_STATUS.TRUE.getLabel());
			FetchConfigVO fetchConfigVO = dataService.getFetchConfigVO(testSetId);

			final String uri = fetchConfigVO.getMETADATA_URL() + testSetId;
			List<FetchMetadataVO> fetchMetadataListVO = dataService.getFetchMetaData(testSetId, uri);
			SortedMap<Integer, List<FetchMetadataVO>> dependentScriptMap = new TreeMap<Integer, List<FetchMetadataVO>>();
			SortedMap<Integer, List<FetchMetadataVO>> metaDataMap = dataService.prepareTestcasedata(fetchMetadataListVO,
					dependentScriptMap);

			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

			fetchConfigVO.setStarttime1(date);

			// Independent
			for (Entry<Integer, List<FetchMetadataVO>> metaData : metaDataMap.entrySet()) {
				logger.info(" Running Independent - " + metaData.getKey());
				executorMethodPyJab(testSetId, fetchConfigVO, metaData);
			}

			ExecutorService executordependent = Executors.newFixedThreadPool(fetchConfigVO.getParallel_dependent());
			for (Entry<Integer, List<FetchMetadataVO>> metaData : dependentScriptMap.entrySet()) {
				logger.info(" Running Dependent - " + metaData.getKey());
				executordependent.execute(() -> {
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
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			}
			executordependent.shutdown();

			executeTestrunVo.setStatusCode(200);
			executeTestrunVo.setStatusMessage("SUCCESS");
			executeTestrunVo.setStatusDescr("SUCCESS");
		} catch (Exception e) {
			e.printStackTrace();
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

		String passurl = null;
		String failurl = null;

		String detailurl = null;
		String scripturl = null;
		String test_set_id = null;
		String test_set_line_id = null;
		String script_id = null;
		String script_id1 = null;
		String script_Number = null;
		String seq_num = null;
		String step_description = null;
		String line_number = null;
		String testScriptParamId = null;
		String methodCall;
		ArrayList<String> methods = new ArrayList<>();
		PyJabScriptDto dto = new PyJabScriptDto();

		System.out
				.println("Create script methods for  ---------   " + fetchMetadataListVO.get(0).getTest_set_line_id());

		try {

			String userName = null;
			Date startdate = new Date();
			fetchConfigVO.setStarttime(startdate);
			String instanceName = fetchConfigVO.getInstance_name();
			String screenShotFolderPath = fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION()
					+ fetchMetadataListVO.get(0).getCustomer_name() + "\\\\"
					+ fetchMetadataListVO.get(0).getTest_run_name() + "\\\\";

			String objectStoreScreenShotPath = fetchConfigVO.getScreenshot_path()
					+ fetchMetadataListVO.get(0).getCustomer_name() + "/"
					+ fetchMetadataListVO.get(0).getTest_run_name() + "/";

			for (FetchMetadataVO fetchMetadataVO : fetchMetadataListVO) {
				String url = fetchConfigVO.getApplication_url();
				actionName = fetchMetadataVO.getAction();
				test_set_id = fetchMetadataVO.getTest_set_id();
				test_set_line_id = fetchMetadataVO.getTest_set_line_id();
				script_id = fetchMetadataVO.getScript_id();
				script_Number = fetchMetadataVO.getScript_number();
				line_number = fetchMetadataVO.getLine_number();
				seq_num = fetchMetadataVO.getSeq_num();

				step_description = fetchMetadataVO.getStep_description();
				String screenParameter = fetchMetadataVO.getInput_parameter();
				testScriptParamId = fetchMetadataVO.getTest_script_param_id();

				String screenshotPath = screenShotFolderPath + fetchMetadataVO.getSeq_num() + "_"

						+ fetchMetadataVO.getLine_number() + "_" + fetchMetadataVO.getScenario_name() + "_"

						+ fetchMetadataVO.getScript_number() + "_" + fetchMetadataVO.getTest_run_name() + "_"

						+ fetchMetadataVO.getLine_number();

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
				}

//				if(test_set_line_id.equalsIgnoreCase(""+17001)) {
				methodCall = ebsActions(fetchMetadataVO, fetchMetadataVO.getTest_set_id(), actionName, screenshotPath,
						testScriptParamId);
				methods.add(methodCall);
//				}
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

			final Context ctx = new Context();
			ctx.setVariable("dto", dto);
			final String scriptContent = this.templateEngine.process(templateName, ctx);

			String scriptPathForPyJabScript = fetchMetadataListVO.get(0).getCustomer_name() + FORWARD_SLASH
					+ fetchMetadataListVO.get(0).getTest_run_name() + FORWARD_SLASH
					+ fetchMetadataListVO.get(0).getTest_set_line_id() + FORWARD_SLASH
					+ fetchMetadataListVO.get(0).getTest_set_line_id() + PY_EXTN;
			uploadObjectToObjectStoreWithInputContent(scriptContent, scriptPathForPyJabScript);
			logger.info(
					"Publishing with details test_set_id, test_set_line_id, scriptPathForPyJabScript, screenShotFolderPath,objectStoreScreenShotPath ---- "
							+ test_set_id + " - " + test_set_line_id + " - " + scriptPathForPyJabScript + " - "
							+ screenShotFolderPath + " - " + objectStoreScreenShotPath);
			this.kafkaTemp.send(topic, new PyJabKafkaDto(test_set_id, test_set_line_id, scriptPathForPyJabScript,
					screenShotFolderPath, objectStoreScreenShotPath));
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
//						if (actionName.equalsIgnoreCase("ebsPasteValue")) {
//							String copynumberValue;
//							dbValue = codeLineRepo.findByTestRunScriptId(
//									Integer.parseInt(fetchMetadataVO.getTest_script_param_id()), key);
//							String[] arrOfStr = dbValue.split(">", 5);
//							if (arrOfStr.length < 2) {
//								copynumberValue = dbValue;
//							} else {
//								String Testrun_name = arrOfStr[0];
//								String seq = arrOfStr[1];
//								String line_number = arrOfStr[2];
//								if (Testrun_name.equalsIgnoreCase(fetchMetadataVO.getTest_run_name())
//										&& seq.equalsIgnoreCase(fetchMetadataVO.getSeq_num())) {
//									copynumberValue = dynamicnumber.getCopynumberInputParameter(Testrun_name, seq,
//											line_number, null, null);
//								} else {
//									copynumberValue = dynamicnumber.getCopynumber(Testrun_name, seq, line_number, null,
//											null);
//								}
//							}
//							listArgs.add(index + SPLIT + addQuotes(copynumberValue));
//						} else {
						String image_dest = "C:\\\\EBS-Automation\\\\WATS_Files\\\\screenshot\\\\ebs\\\\"
								+ fetchMetadataVO.getCustomer_name() + "\\\\" + fetchMetadataVO.getTest_run_name();

						dbValue = image_dest;
						listArgs.add(index + SPLIT + addQuotes(dbValue));
//						}
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
				e.printStackTrace();
			}

		}
		Collections.sort(listArgs);
		listArgs.stream().map(val -> val.split(SPLIT)[1]).forEach(methodCall::add);
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
		try {
			String path = "C:\\wats\\java-generated-scripts\\" + destinationFilePath.split(FORWARD_SLASH)[3];
			logger.info("%%%%%%%%%%");

			logger.info(path);

			Files.write(Paths.get(path), sourceFileContent.getBytes());
		} catch (IOException e1) {

			e1.printStackTrace();
		}

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
			ObjectStorageClient client = new ObjectStorageClient(provider);

			/* Create a request and dependent object(s). */

			PutObjectRequest putObjectRequest = PutObjectRequest.builder().namespaceName(ociNamespace)
					.bucketName(ociBucketName)
					// .objectName("ebs/Detailed_Report.pdf")
					.objectName(destinationFilePath).contentLength(fileSize)// Create a Stream, for example, by calling
																			// a helper function like below.

					.putObjectBody(is)

					.build();

			/* Send request to the Client */
			response = client.putObject(putObjectRequest);

			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response.toString();
	}

	public void downloadScreenshotsFromObjectStore(String screenshotPath, String customerName, String TestRunName,
			String objectStoreScreenShotPath, String seqNum) {
		String configurationFilePath = "~/.oci/config";
		String profile = "DEFAULT";

		// Configuring the AuthenticationDetailsProvider. It's assuming there is a
		// default OCI config file
		// "~/.oci/config", and a profile in that config with the name "DEFAULT". Make
		// changes to the following
		// line if needed and use ConfigFileReader.parse(configurationFilePath,
		// profile);

		// Configuring the AuthenticationDetailsProvider. It's assuming there is a
		// default OCI config file
		// "~/.oci/config", and a profile in that config with the name "DEFAULT". Make
		// changes to the following
		// line if needed and use ConfigFileReader.parse(configurationFilePath,
		// profile);

		ConfigFileReader.ConfigFile configFile = null;
		;
		List<String> objNames = null;
		try {
			configFile = ConfigFileReader.parse(new ClassPathResource("oci/config").getInputStream(), ociConfigName);
		} catch (IOException e) {
			e.printStackTrace();
		}

		final AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);

		ObjectStorage client = new ObjectStorageClient(provider);

		String objectStoreScreenshotPath = objectStoreScreenShotPath + customerName + "/" + TestRunName + "/" + seqNum;

		ListObjectsRequest listObjectsRequest = ListObjectsRequest.builder().namespaceName(ociNamespace)
				.bucketName(ociBucketName)
				// .startAfter(objectStoreScreenShotPath)
				.prefix(objectStoreScreenshotPath).delimiter("/").build();

		/* Send request to the Client */
		ListObjectsResponse response = client.listObjects(listObjectsRequest);

		objNames = response.getListObjects().getObjects().stream()
				// .filter(objSummary->objSummary.getName().startsWith("/objstore/watsdev01/ebs/WATS"))
				.map((objSummary) -> objSummary.getName()).collect(Collectors.toList());
		logger.info(objNames.size());
		ListIterator<String> listIt = objNames.listIterator();
		String imagePath = screenshotPath;
		while (listIt.hasNext()) {
			String objectName = listIt.next();
			GetObjectResponse getResponse = client.getObject(GetObjectRequest.builder().namespaceName(ociNamespace)
					.bucketName(ociBucketName).objectName(objectName).build());

			String imageName = objectName.substring(objectName.lastIndexOf("/") + 1, objectName.length());
			File file = new File(imagePath + imageName);
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
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		try {

			client.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void deleteScreenshotsFromWindows(FetchConfigVO fetchConfigVO, List<FetchMetadataVO> fetchMetadataListVO) {
		File folder1 = new File(fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION()
				+ fetchMetadataListVO.get(0).getCustomer_name() + "\\" + fetchMetadataListVO.get(0).getTest_run_name());
		if (!folder1.exists()) {
			// boolean result = false;
			try {
				folder1.mkdirs();
				// result = true;
			} catch (SecurityException se) {
				// handle it
				logger.info(se.getMessage());
			}
		} else {

			File folder = new File(
					fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + fetchMetadataListVO.get(0).getCustomer_name()
							+ "\\" + fetchMetadataListVO.get(0).getTest_run_name() + "\\");
			if (folder.exists()) {
				File[] listOfFiles = folder.listFiles();

				for (File file : Arrays.asList(listOfFiles)) {

					String seqNum = String.valueOf(file.getName().substring(0, file.getName().indexOf('_')));

					String seqnum1 = fetchMetadataListVO.get(0).getSeq_num();
					if (seqNum.equalsIgnoreCase(seqnum1)) {
						Path imagesPath = Paths.get(file.getPath());
						try {
							Files.delete(imagesPath);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	public ResponseDto generateTestScriptLineIdReports(PyJabKafkaDto args) {
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

			args.setStartDate(dataBaseEntry.getExecStartDateOfScript(args.getTestSetId(), args.getTestSetLineId()));
			FetchConfigVO fetchConfigVO = dataService.getFetchConfigVO(args.getTestSetId());

			List<FetchMetadataVO> fetchMetadataListVO = dataBaseEntry.getMetaDataVOList(args.getTestSetId(),
					args.getTestSetLineId(), false);

			String screenShotFolderPath = (fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION()
					+ fetchMetadataListVO.get(0).getCustomer_name() + "\\"
					+ fetchMetadataListVO.get(0).getTest_run_name() + "\\");
			String objectStore = fetchConfigVO.getScreenshot_path();
			String[] arrOfStr = objectStore.split("/", 5);
			String objectStoreScreenShotPath = fetchConfigVO.getScreenshot_path() + "/"
					+ fetchMetadataListVO.get(0).getCustomer_name() + "/"
					+ fetchMetadataListVO.get(0).getTest_run_name();
			objectStoreScreenShotPath = arrOfStr[3];
			for (int i = 4; i < arrOfStr.length; i++) {
				objectStoreScreenShotPath = objectStoreScreenShotPath + "/" + arrOfStr[i];
			}

			String script_id = fetchMetadataListVO.get(0).getScript_id();
			String passurl = fetchConfigVO.getImg_url() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
					+ fetchMetadataListVO.get(0).getTest_run_name() + "/" + "Passed_Report.pdf" + "AAAparent="
					+ fetchConfigVO.getImg_url();
			String failurl = fetchConfigVO.getImg_url() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
					+ fetchMetadataListVO.get(0).getTest_run_name() + "/" + "Failed_Report.pdf" + "AAAparent="
					+ fetchConfigVO.getImg_url();
			String detailurl = fetchConfigVO.getImg_url() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
					+ fetchMetadataListVO.get(0).getTest_run_name() + "/" + "Detailed_Report.pdf" + "AAAparent="
					+ fetchConfigVO.getImg_url();
			String scripturl = fetchConfigVO.getImg_url() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
					+ fetchMetadataListVO.get(0).getTest_run_name() + "/" + fetchMetadataListVO.get(0).getSeq_num()
					+ "_" + fetchMetadataListVO.get(0).getScript_number() + ".pdf" + "AAAparent="
					+ fetchConfigVO.getImg_url();

			fetchConfigVO.setStarttime(args.getStartDate());
			fetchConfigVO.setStarttime1(args.getStartDate());

			if (args.isSuccess()) {

				FetchScriptVO post = new FetchScriptVO();
				post.setP_test_set_id(args.getTestSetId());
				post.setP_status("Pass");
				post.setP_script_id(script_id);
				post.setP_test_set_line_id(args.getTestSetLineId());
				post.setP_pass_path(passurl);
				post.setP_fail_path(failurl);
				post.setP_exception_path(detailurl);
				post.setP_test_set_line_path(scripturl);
				Date enddate = new Date();
				fetchConfigVO.setEndtime(enddate);

				try {
					dataService.updateTestCaseStatus(post, args.getTestSetId(), fetchConfigVO);
					dataBaseEntry.updateEndTime(fetchConfigVO, args.getTestSetLineId(), args.getTestSetId(), enddate);
				} catch (Exception e) {
					logger.info("e");
				}
				deleteScreenshotsFromWindows(fetchConfigVO, fetchMetadataListVO);
				downloadScreenshotsFromObjectStore(screenShotFolderPath, fetchMetadataListVO.get(0).getCustomer_name(),
						fetchMetadataListVO.get(0).getTest_run_name(), objectStoreScreenShotPath,
						fetchMetadataListVO.get(0).getSeq_num() + "_");
				createPdf(fetchMetadataListVO, fetchConfigVO,

						fetchMetadataListVO.get(0).getSeq_num() + "_" + fetchMetadataListVO.get(0).getScript_number()
								+ ".pdf",
						args.getStartDate(), enddate);

				if ("OBJECT_STORE".equalsIgnoreCase(fetchConfigVO.getPDF_LOCATION())) {
					eBSSeleniumKeyWords.uploadPDF(fetchMetadataListVO, fetchConfigVO);
				}

				limitScriptExecutionService.insertTestRunScriptData(fetchConfigVO, fetchMetadataListVO,
						fetchMetadataListVO.get(0).getScript_id(), fetchMetadataListVO.get(0).getScript_number(),
						"pass", new Date(), enddate);
				limitScriptExecutionService.updateFaileScriptscount(args.getTestSetLineId(), args.getTestSetId());

			} else {

				String message = "EBS Execution Failed";

				fetchConfigVO.setErrormessage(message);
				FetchScriptVO post = new FetchScriptVO();
				post.setP_test_set_id(args.getTestSetId());
				post.setP_status("Fail");
				post.setP_script_id(script_id);
				post.setP_test_set_line_id(args.getTestSetLineId());
				post.setP_pass_path(passurl);
				post.setP_fail_path(failurl);
				post.setP_exception_path(detailurl);
				post.setP_test_set_line_path(scripturl);
				Date enddate = new Date();
				fetchConfigVO.setEndtime(enddate);
				dataService.updateTestCaseStatus(post, args.getTestSetId(), fetchConfigVO);
				dataBaseEntry.updateEndTime(fetchConfigVO, args.getTestSetLineId(), args.getTestSetId(), enddate);
				deleteScreenshotsFromWindows(fetchConfigVO, fetchMetadataListVO);
				downloadScreenshotsFromObjectStore(screenShotFolderPath, fetchMetadataListVO.get(0).getCustomer_name(),
						fetchMetadataListVO.get(0).getTest_run_name(), objectStoreScreenShotPath,
						fetchMetadataListVO.get(0).getSeq_num() + "_");
				int failedScriptRunCount = limitScriptExecutionService.getFailedScriptRunCount(args.getTestSetLineId(),
						args.getTestSetId());
				if (failedScriptRunCount == 1) {
					eBSSeleniumKeyWords.createFailedPdf(

							fetchMetadataListVO, fetchConfigVO,
							fetchMetadataListVO.get(0).getSeq_num() + "_"
									+ fetchMetadataListVO.get(0).getScript_number() + ".pdf",
							args.getStartDate(), enddate);

				} else if (failedScriptRunCount == 2) {
					limitScriptExecutionService
							.renameFailedFile(fetchMetadataListVO, fetchConfigVO,
									fetchMetadataListVO.get(0).getSeq_num() + "_"
											+ fetchMetadataListVO.get(0).getScript_number() + ".pdf",
									failedScriptRunCount);
					eBSSeleniumKeyWords.createFailedPdf(fetchMetadataListVO, fetchConfigVO,
							fetchMetadataListVO.get(0).getSeq_num() + "_"
									+ fetchMetadataListVO.get(0).getScript_number() + "_RUN" + failedScriptRunCount
									+ ".pdf",
							args.getStartDate(), enddate);

				} else {
					eBSSeleniumKeyWords.createFailedPdf(fetchMetadataListVO, fetchConfigVO,
							fetchMetadataListVO.get(0).getSeq_num() + "_"
									+ fetchMetadataListVO.get(0).getScript_number() + "_RUN" + failedScriptRunCount
									+ ".pdf",
							args.getStartDate(), enddate);
				}
				if ("OBJECT_STORE".equalsIgnoreCase(fetchConfigVO.getPDF_LOCATION())) {
					seleniumFactory.getInstanceObj(fetchConfigVO.getInstance_name()).uploadPDF(fetchMetadataListVO,
							fetchConfigVO);

					limitScriptExecutionService.insertTestRunScriptData(fetchConfigVO, fetchMetadataListVO,
							fetchMetadataListVO.get(0).getScript_id(), fetchMetadataListVO.get(0).getScript_number(),
							"Fail", new Date(), enddate);
					// break;
				}
			}

			// final reports generation
			if (!args.isManualTrigger()) {
				String pdfGenerationEnabled = dataBaseEntry.pdfGenerationEnabled(Long.valueOf(args.getTestSetId()));
				if (BOOLEAN_STATUS.TRUE.getLabel().equalsIgnoreCase(pdfGenerationEnabled)) {
					boolean runFinalPdf = dataBaseEntry
							.checkIfAllTestSetLinesCompleted(Long.valueOf(args.getTestSetId()), true);
					if (runFinalPdf) {
						Date date1 = new Date();
						fetchConfigVO.setEndtime(date1);
						dataBaseEntry.updatePdfGenerationEnableStatus(args.getTestSetId(),
								BOOLEAN_STATUS.FALSE.getLabel());
						testRunPdfGeneration(args.getTestSetId(), fetchConfigVO, date1);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseDto(200, Constants.SUCCESS, null);
	}

	private void testRunPdfGeneration(String testSetId, FetchConfigVO fetchConfigVO, Date endDate) {
		List<FetchMetadataVO> fetchMetadataListVOFinal = dataBaseEntry.getMetaDataVOList(testSetId, null, true);
		dataBaseEntry.setPassAndFailScriptCount(testSetId, fetchConfigVO);
		fetchConfigVO.setEndtime(endDate);
		try {
			createPdf(fetchMetadataListVOFinal, fetchConfigVO, "Passed_Report.pdf", null, null);
			createPdf(fetchMetadataListVOFinal, fetchConfigVO, "Failed_Report.pdf", null, null);
			createPdf(fetchMetadataListVOFinal, fetchConfigVO, "Detailed_Report.pdf", null, null);
		} catch (IOException | DocumentException | com.itextpdf.text.DocumentException e) {
			e.printStackTrace();
		}
	}

	private void createPdf(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO, String pdffileName,
			Date Starttime, Date endtime) throws IOException, DocumentException, com.itextpdf.text.DocumentException {
		try {
			logger.info("Start of create Pdf for -- " + pdffileName);
			String Date = DateUtils.getSysdate();

			String Folder = (fetchConfigVO.getWINDOWS_PDF_LOCATION() + fetchMetadataListVO.get(0).getCustomer_name()
					+ "\\" + fetchMetadataListVO.get(0).getTest_run_name() + "\\");

			String FILE = (Folder + pdffileName);
			logger.info("Path of Pdf -- " + FILE);

			List<String> fileNameList = null;
			if ("Passed_Report.pdf".equalsIgnoreCase(pdffileName)) {
				fileNameList = eBSSeleniumKeyWords.getPassedPdfNew(fetchMetadataListVO, fetchConfigVO);
			} else if ("Failed_Report.pdf".equalsIgnoreCase(pdffileName)) {
				fileNameList = eBSSeleniumKeyWords.getFailedPdfNew(fetchMetadataListVO, fetchConfigVO);
			} else if ("Detailed_Report.pdf".equalsIgnoreCase(pdffileName)) {
				fileNameList = eBSSeleniumKeyWords.getDetailPdfNew(fetchMetadataListVO, fetchConfigVO);
			} else {
				fileNameList = eBSSeleniumKeyWords.getFileNameListNew(fetchMetadataListVO, fetchConfigVO);
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
				logger.info("Creating directory: " + theDir.getName());
				boolean result = false;
				try {
					theDir.mkdirs();
					result = true;
				} catch (SecurityException se) {
					// handle it
					logger.info(se.getMessage());
				}
			} else {
				logger.info("Folder exist");
			}
			int passcount = fetchConfigVO.getPasscount();
			int failcount = fetchConfigVO.getFailcount();
			Date Tendtime = fetchConfigVO.getEndtime();
			Date TStarttime = fetchConfigVO.getStarttime1();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:aa");

			String TStarttime1 = dateFormat.format(TStarttime);
			String Tendtime1 = dateFormat.format(Tendtime);
			long Tdiff = Tendtime.getTime() - TStarttime.getTime();

			Document document = new Document();
			String start = "Execution Summary";
			String pichart = "Pie-Chart";
			String Report = "Execution Report";
			Font bfBold12 = FontFactory.getFont("Arial", 23);
			Font fnt = FontFactory.getFont("Arial", 12);
			Font bf12 = FontFactory.getFont("Arial", 23);
			Font bf15 = FontFactory.getFont("Arial", 23, Font.UNDERLINE);
			Font bf16 = FontFactory.getFont("Arial", 12, Font.UNDERLINE, new BaseColor(66, 245, 236));
			Font bf13 = FontFactory.getFont("Arial", 23, Font.UNDERLINE, BaseColor.GREEN);
			Font bf14 = FontFactory.getFont("Arial", 23, Font.UNDERLINE, BaseColor.RED);
			Font bfBold = FontFactory.getFont("Arial", 23, BaseColor.WHITE);
			DefaultPieDataset dataSet = new DefaultPieDataset();
			PdfWriter writer = null;
			writer = PdfWriter.getInstance(document, new FileOutputStream(FILE));
			Rectangle one = new Rectangle(1360, 800);
			document.setPageSize(one);
			document.open();
			logger.info("before enter Images/wats_icon.png1");
			Image img1 = Image.getInstance(watslogo);
			logger.info("after enter Images/wats_icon.png1");

			img1.scalePercent(65, 68);
			img1.setAlignment(Image.ALIGN_RIGHT);
			if ((passcount != 0 || failcount != 0) & ("Passed_Report.pdf".equalsIgnoreCase(pdffileName)
					|| "Failed_Report.pdf".equalsIgnoreCase(pdffileName)
					|| "Detailed_Report.pdf".equalsIgnoreCase(pdffileName))) {
				String TestRun = TestRun = test_Run_Name;

				String StartTime = null;
				String EndTime = Tendtime1;
				String ExecutionTime = null;
				Date date = new Date();
				Timestamp startTimestamp = new Timestamp(TStarttime.getTime());
				Timestamp endTimestamp = new Timestamp(Tendtime.getTime());
				Map<String, Map<String, TestSetScriptParam>> descriptionList = dataBaseEntry
						.getTestRunMap(fetchMetadataListVO.get(0).getTest_set_id());
				Map<Date, Long> timeslist = limitScriptExecutionService
						.getStarttimeandExecutiontime(fetchMetadataListVO.get(0).getTest_set_id());
				if (timeslist.size() == 0) {
					StartTime = TStarttime1;
					long TdiffSeconds = Tdiff / 1000 % 60;
					long TdiffMinutes = Tdiff / (60 * 1000) % 60;
					long TdiffHours = Tdiff / (60 * 60 * 1000);
					ExecutionTime = TdiffHours + ":" + TdiffMinutes + ":" + TdiffSeconds;
					if ("Detailed_Report.pdf".equalsIgnoreCase(pdffileName)) {
						limitScriptExecutionService.updateTestrunTimes(startTimestamp, endTimestamp, Tdiff,
								fetchMetadataListVO.get(0).getTest_set_id());
					}
				} else {
					for (Entry<Date, Long> entryMap : timeslist.entrySet()) {
						StartTime = dateFormat.format(entryMap.getKey());
						long totalTime = Tdiff + entryMap.getValue();
						long TdiffSeconds = totalTime / 1000 % 60;
						long TdiffMinutes = totalTime / (60 * 1000) % 60;
						long TdiffHours = totalTime / (60 * 60 * 1000);
						ExecutionTime = TdiffHours + ":" + TdiffMinutes + ":" + TdiffSeconds;
						if ("Detailed_Report.pdf".equalsIgnoreCase(pdffileName)) {

							limitScriptExecutionService.updateTestrunTimes1(endTimestamp, totalTime,
									fetchMetadataListVO.get(0).getTest_set_id());
						}
					}
				}
				String TR = "Test Run Name";
				String SN = "Executed By";
				String SN1 = "Start Time";
				String S1 = "End Time";
				String Scenarios1 = "Execution Time";

				document.add(img1);
				document.add(new Paragraph(Report, bfBold12));
				document.add(Chunk.NEWLINE);
				PdfPTable table1 = new PdfPTable(2);
				table1.setWidths(new int[] { 1, 1 });
				table1.setWidthPercentage(100f);
				eBSSeleniumKeyWords.insertCell(table1, TR, Element.ALIGN_LEFT, 1, bf12);
				eBSSeleniumKeyWords.insertCell(table1, TestRun, Element.ALIGN_LEFT, 1, bf12);
				eBSSeleniumKeyWords.insertCell(table1, SN, Element.ALIGN_LEFT, 1, bf12);
				eBSSeleniumKeyWords.insertCell(table1, ExecutedBy, Element.ALIGN_LEFT, 1, bf12);
				eBSSeleniumKeyWords.insertCell(table1, SN1, Element.ALIGN_LEFT, 1, bf12);
				eBSSeleniumKeyWords.insertCell(table1, StartTime, Element.ALIGN_LEFT, 1, bf12);
				eBSSeleniumKeyWords.insertCell(table1, S1, Element.ALIGN_LEFT, 1, bf12);
				eBSSeleniumKeyWords.insertCell(table1, EndTime, Element.ALIGN_LEFT, 1, bf12);
				eBSSeleniumKeyWords.insertCell(table1, Scenarios1, Element.ALIGN_LEFT, 1, bf12);
				eBSSeleniumKeyWords.insertCell(table1, ExecutionTime, Element.ALIGN_LEFT, 1, bf12);
				document.add(table1);

				if (passcount == 0) {

					dataSet.setValue("Fail", failcount);
				} else if (failcount == 0) {
					dataSet.setValue("Pass", passcount);
				} else {
					dataSet.setValue("Pass", passcount);
					dataSet.setValue("Fail", failcount);
				}
				double pass = Math.round((passcount * 100.0) / (passcount + failcount));
				double fail = Math.round((failcount * 100.0) / (passcount + failcount));
				Rectangle one1 = new Rectangle(1360, 1000);
				if ("Detailed_Report.pdf".equalsIgnoreCase(pdffileName)) {

					document.setPageSize(one1);

					document.newPage();
					document.add(img1);
					Paragraph executionSummery = new Paragraph(start, bfBold12);
					document.add(executionSummery);
					document.add(Chunk.NEWLINE);
					DecimalFormat df1 = new DecimalFormat("0");
					DecimalFormat df2 = new DecimalFormat("0");
					PdfPTable table = new PdfPTable(3);
					table.setWidths(new int[] { 1, 1, 1 });
					table.setWidthPercentage(100f);
					eBSSeleniumKeyWords.insertCell(table, "Status", Element.ALIGN_CENTER, 1, bfBold12);
					eBSSeleniumKeyWords.insertCell(table, "Total", Element.ALIGN_CENTER, 1, bfBold12);
					eBSSeleniumKeyWords.insertCell(table, "Percentage", Element.ALIGN_CENTER, 1, bfBold12);
					PdfPCell[] cells1 = table.getRow(0).getCells();
					for (int k = 0; k < cells1.length; k++) {
						cells1[k].setBackgroundColor(new BaseColor(161, 190, 212));
					}
					eBSSeleniumKeyWords.insertCell(table, "Passed", Element.ALIGN_CENTER, 1, bf12);
					eBSSeleniumKeyWords.insertCell(table, df1.format(passcount), Element.ALIGN_CENTER, 1, bf12);
					eBSSeleniumKeyWords.insertCell(table, df2.format(pass) + "%", Element.ALIGN_CENTER, 1, bf12);
					eBSSeleniumKeyWords.insertCell(table, "Failed", Element.ALIGN_CENTER, 1, bf12);
					eBSSeleniumKeyWords.insertCell(table, df1.format(failcount), Element.ALIGN_CENTER, 1, bf12);
					eBSSeleniumKeyWords.insertCell(table, df2.format(fail) + "%", Element.ALIGN_CENTER, 1, bf12);
					document.setMargins(20, 20, 20, 20);
					document.add(table);
				} else if ("Passed_Report.pdf".equalsIgnoreCase(pdffileName)) {
					document.add(Chunk.NEWLINE);
					Paragraph executionSummery = new Paragraph(start, bfBold12);
					document.add(executionSummery);
					document.add(Chunk.NEWLINE);
					DecimalFormat df1 = new DecimalFormat("0");
					DecimalFormat df2 = new DecimalFormat("0");
					PdfPTable table = new PdfPTable(3);
					table.setWidths(new int[] { 1, 1, 1 });
					table.setWidthPercentage(100f);
					eBSSeleniumKeyWords.insertCell(table, "Status", Element.ALIGN_CENTER, 1, bfBold12);
					eBSSeleniumKeyWords.insertCell(table, "Total", Element.ALIGN_CENTER, 1, bfBold12);
					eBSSeleniumKeyWords.insertCell(table, "Percentage", Element.ALIGN_CENTER, 1, bfBold12);
					PdfPCell[] cells1 = table.getRow(0).getCells();
					for (int k = 0; k < cells1.length; k++) {
						cells1[k].setBackgroundColor(new BaseColor(161, 190, 212));
					}

					eBSSeleniumKeyWords.insertCell(table, "Passed", Element.ALIGN_CENTER, 1, bf12);
					eBSSeleniumKeyWords.insertCell(table, df1.format(passcount), Element.ALIGN_CENTER, 1, bf12);
					eBSSeleniumKeyWords.insertCell(table, df2.format(pass) + "%", Element.ALIGN_CENTER, 1, bf12);
					document.setMargins(20, 20, 20, 20);
					document.add(table);

				} else {
					document.add(Chunk.NEWLINE);
					Paragraph executionSummery = new Paragraph(start, bfBold12);
					document.add(executionSummery);
					document.add(Chunk.NEWLINE);
					DecimalFormat df1 = new DecimalFormat("0");
					DecimalFormat df2 = new DecimalFormat("0");
					PdfPTable table = new PdfPTable(3);
					table.setWidths(new int[] { 1, 1, 1 });
					table.setWidthPercentage(100f);
					eBSSeleniumKeyWords.insertCell(table, "Status", Element.ALIGN_CENTER, 1, bfBold12);
					eBSSeleniumKeyWords.insertCell(table, "Total", Element.ALIGN_CENTER, 1, bfBold12);
					eBSSeleniumKeyWords.insertCell(table, "Percentage", Element.ALIGN_CENTER, 1, bfBold12);
					PdfPCell[] cells1 = table.getRow(0).getCells();
					for (int k = 0; k < cells1.length; k++) {
						cells1[k].setBackgroundColor(new BaseColor(161, 190, 212));
					}

					eBSSeleniumKeyWords.insertCell(table, "Failed", Element.ALIGN_CENTER, 1, bf12);
					eBSSeleniumKeyWords.insertCell(table, df1.format(failcount), Element.ALIGN_CENTER, 1, bf12);
					eBSSeleniumKeyWords.insertCell(table, df2.format(fail) + "%", Element.ALIGN_CENTER, 1, bf12);
					document.setMargins(20, 20, 20, 20);
					document.add(table);
				}
				if ("Detailed_Report.pdf".equalsIgnoreCase(pdffileName)) {
					Chunk ch = new Chunk(pichart, bfBold);
					ch.setTextRise(-18);
					ch.setBackground(new BaseColor(38, 99, 175), 0f, 10f, 1730f, 15f);

					Paragraph p1 = new Paragraph(ch);
					p1.setSpacingBefore(50);
					document.add(p1);

					JFreeChart chart = ChartFactory.createPieChart(" ", dataSet, true, true, false);
					Color c1 = new Color(102, 255, 102);
					Color c = new Color(253, 32, 32);

					LegendTitle legend = chart.getLegend();
					PiePlot piePlot = (PiePlot) chart.getPlot();
					piePlot.setSectionPaint("Pass", c1);
					piePlot.setSectionPaint("Fail", c);
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
					legend.setFrame(
							new LineBorder(Color.white, new BasicStroke(20f), new RectangleInsets(1.0, 1.0, 1.0, 1.0)));

					java.awt.Font pass1 = new java.awt.Font("", Font.NORMAL, 22);
					legend.setItemFont(pass1);
					PdfContentByte contentByte = writer.getDirectContent();
					PdfTemplate template = contentByte.createTemplate(1000, 900);
					Graphics2D graphics2d = template.createGraphics(700, 400, new DefaultFontMapper());
					Rectangle2D rectangle2d = new Rectangle2D.Double(0, 0, 600, 400);
					chart.draw(graphics2d, rectangle2d);
					graphics2d.dispose();
					contentByte.addTemplate(template, 400, 100);
				}
				int k = 0, l = 0;
				String sno1 = "";
				Map<Integer, Map<String, String>> toc = new TreeMap<>();

				Map<String, String> toc2 = new TreeMap<>();
				for (String image : fileNameList) {
					k++;
					String sndo = image.split("_")[0];
					String name = image.split("_")[3];

					if (!sndo.equalsIgnoreCase(sno1)) {
						Map<String, String> toc1 = new TreeMap<>();
						for (String image1 : fileNameList) {
							String Status = image1.split("_")[6];
							String status = Status.split("\\.")[0];

							if (image1.startsWith(sndo + "_") && image1.contains("Failed")) {

								toc2.put(sndo, "Failed" + l);
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
				document.add(img1);
				Anchor target2 = new Anchor(String.valueOf("Page Numbers"), bfBold);
				target2.setName(String.valueOf("details"));
				Chunk ch1 = new Chunk(String.format("Script Numbers"), bfBold);
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
						click.setReference("#" + String.valueOf(entry1.getKey()));
						Anchor click1 = new Anchor(String.valueOf("(Failed)"), bf14);
						click1.setReference("#" + String.valueOf(entry1.getValue()));
						Paragraph pr = new Paragraph();
						int value = entry.getKey();
						Anchor ca1 = new Anchor(String.valueOf(entry1.getKey()), bf15);
						ca1.setReference("#" + String.valueOf(entry1.getKey()));
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
							click2.setReference("#" + String.valueOf(entry1.getKey()));
							pr.add(ca1);
							pr.add(click2);
							pr.add(dottedLine);
							pr.add(click);
							document.add(Chunk.NEWLINE);
							document.add(pr);
						}
					}
				}

				int i = 0, j = 0;
				for (String image : fileNameList) {
					i++;
					Image img = Image.getInstance(fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + customer_Name + "/"
							+ test_Run_Name + "/" + image);
					String sno = image.split("_")[0];
					String SNO = "Script Number";
					String ScriptNumber = image.split("_")[3];
					String SNM = "Scenario Name";
					String ScriptName = image.split("_")[2];
					String testRunName = image.split("_")[4];
					if (!sno.equalsIgnoreCase(sno1)) {
						document.setPageSize(img);
						document.newPage();
						document.add(img1);
						Anchor target3 = new Anchor("Script Details", bf12);
						target3.setName(sno + "_" + ScriptNumber);
						Paragraph pa = new Paragraph();
						pa.add(target3);
						document.add(pa);
						document.add(Chunk.NEWLINE);
						PdfPTable table2 = new PdfPTable(2);
						table2.setWidths(new int[] { 1, 1 });
						table2.setWidthPercentage(100f);
						eBSSeleniumKeyWords.insertCell(table2, SNO, Element.ALIGN_LEFT, 1, bf12);
						eBSSeleniumKeyWords.insertCell(table2, ScriptNumber, Element.ALIGN_LEFT, 1, bf12);
						eBSSeleniumKeyWords.insertCell(table2, SNM, Element.ALIGN_LEFT, 1, bf12);
						eBSSeleniumKeyWords.insertCell(table2, ScriptName, Element.ALIGN_LEFT, 1, bf12);

						for (Entry<String, String> entry1 : toc.get(i).entrySet()) {
							String str = entry1.getValue();
							if (!str.equals("null")) {
								eBSSeleniumKeyWords.insertCell(table2, "Status", Element.ALIGN_LEFT, 1, bf12);
								eBSSeleniumKeyWords.insertCell(table2, "Failed", Element.ALIGN_LEFT, 1, bf12);
							} else {
								eBSSeleniumKeyWords.insertCell(table2, "Status", Element.ALIGN_LEFT, 1, bf12);
								eBSSeleniumKeyWords.insertCell(table2, "Passed", Element.ALIGN_LEFT, 1, bf12);
							}
						}

						document.add(table2);

					}
					if (sno != null) {
						sno1 = sno;
					}
					String Status = image.split("_")[6];
					String status = Status.split("\\.")[0];
					String Scenario = image.split("_")[2];

					String Scenarios = "Scenario Name :" + "" + Scenario;

					String sndo = image.split("_")[0];
					img1.scalePercent(65, 68);

					img1.setAlignment(Image.ALIGN_RIGHT);
					if (image.startsWith(sndo + "_") && image.contains("Failed")) {
						document.setPageSize(one1);
						document.newPage();
					} else {

						document.setPageSize(img);
						document.newPage();
					}
					document.add(img1);
					document.add(new Paragraph(Scenarios, fnt));
					String Reason = image.split("_")[5];
					String step = "Step No :" + "" + Reason;
					String Message = "Failed at Line Number:" + "" + Reason;
					// new change-database to get error message
					String error = dataBaseEntry.getErrorMessage(sndo, ScriptNumber, testRunName, fetchConfigVO);
					String errorMessage = "Failed Message:" + "" + error;

					String stepDescription = descriptionList.get(sno).get(Reason).getTest_run_param_desc();

					String inputParam = descriptionList.get(sno).get(Reason).getInput_parameter();

					String inputValue = descriptionList.get(sno).get(Reason).getInput_value();

					Paragraph pr1 = new Paragraph();
					pr1.add("Status:");

					if (image.startsWith(sndo + "_") && image.contains("Failed")) {
						Anchor target1 = new Anchor(status);
						target1.setName(String.valueOf(status + j));
						j++;
						pr1.add(target1);
						document.add(pr1);
						document.add(new Paragraph(Message, fnt));
						if (error != null) {
							document.add(new Paragraph(errorMessage, fnt));
						}
						if (stepDescription != null) {
							document.add(new Paragraph("Step Description :" + stepDescription, fnt));
						}
						if (inputParam != null) {
							document.add(new Paragraph("Test Parameter :" + inputParam, fnt));
							if (inputValue != null) {
								document.add(new Paragraph("Test Value :" + inputValue, fnt));
							}
						}
						document.add(Chunk.NEWLINE);
						img.setAlignment(Image.ALIGN_CENTER);
						img.isScaleToFitHeight();
						// new change-change page size
						img.scalePercent(60, 60);
						document.add(img);

					} else {
						document.add(new Paragraph(step, fnt));
						Anchor target1 = new Anchor(status);
						target1.setName(String.valueOf(status));
						pr1.add(target1);
						document.add(pr1);

						if (stepDescription != null) {
							document.add(new Paragraph("Step Description: " + stepDescription, fnt));
						}
						if (inputParam != null) {
							document.add(new Paragraph("Test Parameter: " + inputParam, fnt));
							if (inputValue != null) {
								document.add(new Paragraph("Test Value: " + inputValue, fnt));
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
					target1.setReference("#" + String.valueOf("details"));
					Paragraph p = new Paragraph();
					p.add(target1);
					p.add(new Chunk(new VerticalPositionMark()));
					p.add(" page ");
					p.add(target);
					p.add(" of " + fileNameList.size());
					document.add(p);
				}
			} else {
				if (!("Passed_Report.pdf".equalsIgnoreCase(pdffileName)
						|| "Failed_Report.pdf".equalsIgnoreCase(pdffileName)
						|| "Detailed_Report.pdf".equalsIgnoreCase(pdffileName))) {
					String Starttime1 = dateFormat.format(Starttime);
					String endtime1 = dateFormat.format(endtime);
					long diff = endtime.getTime() - Starttime.getTime();
					long diffSeconds = diff / 1000 % 60;
					long diffMinutes = diff / (60 * 1000) % 60;
					long diffHours = diff / (60 * 60 * 1000);
					String TestRun = test_Run_Name;
					String ScriptNumber = Script_Number;
					String ScriptNumber1 = Scenario_Name;
					String Scenario1 = fetchConfigVO.getStatus1();
//				String ExecutedBy=fetchConfigVO.getApplication_user_name();
					String StartTime = Starttime1;
					String EndTime = endtime1;
					String ExecutionTime = diffHours + ":" + diffMinutes + ":" + diffSeconds;

					String TR = "Test Run Name";
					String SN = "Script Number";
					String SN1 = "Scenario name";
					String Scenarios1 = "Status ";
					String EB = "Executed By";
					String ST = "Start Time";
					String ET = "End Time";
					String EX = "Execution Time";
					document.add(img1);
					// added step DEsc, Input PAram ,Input val in pdf
					Map<String, TestSetScriptParam> map = dataBaseEntry
							.getTestScriptMap(fetchMetadataListVO.get(0).getTest_set_line_id());

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
					int i = 0;
					for (String image : fileNameList) {
						i++;
						Image img = Image.getInstance(fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + customer_Name
								+ "/" + test_Run_Name + "/" + image);

						String Status = image.split("_")[6];
						String status = Status.split("\\.")[0];
						String Scenario = image.split("_")[2];
						String steps = image.split("_")[5];

						String stepDescription = map.get(steps).getTest_run_param_desc();
						String inputParam = map.get(steps).getInput_parameter();
						String inputValue = map.get(steps).getInput_value();
						document.setPageSize(img);
						document.newPage();

						String S = "Status:" + " " + status;
						String Scenarios = "Scenario Name :" + "" + Scenario;
						String step = "Step No :" + "" + steps;
						img1.scalePercent(65, 65);
						img1.setAlignment(Image.ALIGN_RIGHT);
						document.add(img1);
						document.add(new Paragraph(S, fnt));
						document.add(new Paragraph(Scenarios, fnt));
						document.add(new Paragraph(step, fnt));
						if (stepDescription != null) {
							document.add(new Paragraph("Step Description: " + stepDescription, fnt));
						}
						if (inputParam != null) {
							document.add(new Paragraph("Test Parameter: " + inputParam, fnt));
							if (inputValue != null) {
								document.add(new Paragraph("Test Value: " + inputValue, fnt));
							}
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
			document.close();

		} catch (Exception e) {
			logger.info("Not able to Create pdf {}", e);
		}
		try {
			String destinationFilePath = (fetchMetadataListVO.get(0).getCustomer_name() + "/"
					+ fetchMetadataListVO.get(0).getTest_run_name() + "/") + pdffileName;

			String sourceFilePath = (fetchConfigVO.getWINDOWS_PDF_LOCATION()
					+ fetchMetadataListVO.get(0).getCustomer_name() + "\\"
					+ fetchMetadataListVO.get(0).getTest_run_name() + "\\") + pdffileName;

			uploadObjectToObjectStore(sourceFilePath, destinationFilePath);
		} catch (Exception e) {
			logger.info(e);
		}
	}

	public void updateStartStatus(PyJabKafkaDto args) throws ClassNotFoundException, SQLException {
		dataBaseEntry.updateInProgressScriptStatus(null, null, args.getTestSetLineId());
		dataBaseEntry.updateStartTime(null, args.getTestSetLineId(), args.getTestSetId(), args.getStartDate());
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
		ArrayList<Object[]> configurations = dataBaseEntry.getConfigurationDetails(testSetId);
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
				testRunPdfGeneration(testSetId, fetchConfigVO, endDate);
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
		String actionsFilePath = environment.getProperty("pyjab.template.path")
				+ environment.getProperty("pyjab.actions.script.name");
		String customerSpecificScriptPath = environment.getProperty("pyjab.template.path")
				+ environment.getProperty("pyjab.customer.specific.name");
		uploadObjectToObjectStore(actionsFilePath, environment.getProperty("pyjab.script.path.in.oci")
				+ environment.getProperty("pyjab.actions.script.name"));
		uploadObjectToObjectStore(customerSpecificScriptPath, environment.getProperty("pyjab.script.path.in.oci")
				+ environment.getProperty("pyjab.customer.specific.name"));
	}

}
