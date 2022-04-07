package com.winfo.services;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.StringJoiner;
import java.util.TimeZone;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.objectstorage.responses.PutObjectResponse;
import com.winfo.Factory.SeleniumKeywordsFactory;
import com.winfo.config.DriverConfiguration;
import com.winfo.dao.CodeLinesRepository;
import com.winfo.dao.PyJabActionRepo;
import com.winfo.model.PyJabActions;
import com.winfo.vo.ExecuteTestrunVo;
import com.winfo.vo.PyJabKafkaDto;
import com.winfo.vo.PyJabScriptDto;

@Service
public class TestScriptExecService {

	Logger log = Logger.getLogger("Logger");

	private static final String JPG = ".jpg";
	private static final String PY_EXTN = ".py";
	public static final String topic = "test-script-run";
	public static final String FORWARD_SLASH = "/";

	@Autowired
	ErrorMessagesHandler errorMessagesHandler;
	@Value("${configvO.watsvediologo}")
	private String watsvediologo;
	@Value("${configvO.whiteimage}")
	private String whiteimage;
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

	public ExecuteTestrunVo run(String args) throws MalformedURLException {
		ExecuteTestrunVo executeTestrunVo = new ExecuteTestrunVo();

		try {
			FetchConfigVO fetchConfigVO = dataService.getFetchConfigVO(args);

			final String uri = fetchConfigVO.getMETADATA_URL() + args;
			List<FetchMetadataVO> fetchMetadataListVO = dataService.getFetchMetaData(args, uri);
			SortedMap<Integer, List<FetchMetadataVO>> dependentScriptMap = new TreeMap<Integer, List<FetchMetadataVO>>();
			SortedMap<Integer, List<FetchMetadataVO>> metaDataMap = dataService.prepareTestcasedata(fetchMetadataListVO,
					dependentScriptMap);

			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

			fetchConfigVO.setStarttime1(date);

			// Independent
			for (Entry<Integer, List<FetchMetadataVO>> metaData : metaDataMap.entrySet()) {
				executorMethodPyJab(args, fetchConfigVO, fetchMetadataListVO, metaData);
			}
			// Dependent
			for (Entry<Integer, List<FetchMetadataVO>> metaData : dependentScriptMap.entrySet()) {
//				executorMethodPyJab(args, fetchConfigVO, fetchMetadataListVO, metaData);
			}
//			seleniumFactory.getInstanceObj(fetchConfigVO.getInstance_name()).createPdf(fetchMetadataListVO,
//					fetchConfigVO, "Passed_Report.pdf", null, null);
//			seleniumFactory.getInstanceObj(fetchConfigVO.getInstance_name()).createPdf(fetchMetadataListVO,
//					fetchConfigVO, "Failed_Report.pdf", null, null);
//			seleniumFactory.getInstanceObj(fetchConfigVO.getInstance_name()).createPdf(fetchMetadataListVO,
//					fetchConfigVO, "Detailed_Report.pdf", null, null);
//			increment = 0;
//			if ("OBJECT_STORE".equalsIgnoreCase(fetchConfigVO.getPDF_LOCATION())) {
//				seleniumFactory.getInstanceObj(fetchConfigVO.getInstance_name()).uploadPDF(fetchMetadataListVO,
//						fetchConfigVO);
//			}
//
//			uploadScreenshotsToObjectStore(fetchConfigVO, fetchMetadataListVO);
			executeTestrunVo.setStatusCode(200);
			executeTestrunVo.setStatusMessage("SUCCESS");
			executeTestrunVo.setStatusDescr("SUCCESS");
		} catch (Exception e) {
			e.printStackTrace();
			FetchScriptVO post = new FetchScriptVO();
			post.setP_status("Fail");
//			dataService.updateTestCaseStatus(post, args, null);
		}
		return executeTestrunVo;
	}

	public void executorMethodPyJab(String args, FetchConfigVO fetchConfigVO, List<FetchMetadataVO> fetchMetadataListVO,
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

		try {

			String userName = null;
			Date startdate = new Date();
			fetchConfigVO.setStarttime(startdate);
			String instanceName = fetchConfigVO.getInstance_name();
//			deleteScreenshoots(fetchMetadataListVO, fetchConfigVO);

			for (FetchMetadataVO fetchMetadataVO : fetchMetadataListVO) {
				String url = fetchConfigVO.getApplication_url();
				actionName = fetchMetadataVO.getAction();
				test_set_id = fetchMetadataVO.getTest_set_id();
				test_set_line_id = fetchMetadataVO.getTest_set_line_id();
				script_id = fetchMetadataVO.getScript_id();
				script_Number = fetchMetadataVO.getScript_number();
				line_number = fetchMetadataVO.getLine_number();
				seq_num = fetchMetadataVO.getSeq_num();

//				dataBaseEntry.updateInProgressScriptStatus(fetchConfigVO, test_set_id, test_set_line_id);
//				dataBaseEntry.updateStartTime(fetchConfigVO, test_set_line_id, test_set_id, startdate);
				step_description = fetchMetadataVO.getStep_description();
				String screenParameter = fetchMetadataVO.getInput_parameter();
				testScriptParamId = fetchMetadataVO.getTest_script_param_id();

				String screenshotName = testScriptParamId + JPG;
				String screenshotPath = fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION()
						+ fetchMetadataVO.getCustomer_name() + File.separator + fetchMetadataVO.getTest_run_name()
						+ File.separator;

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

				if (instanceName.equalsIgnoreCase("EBS")
						&& (!fetchMetadataListVO.get(0).getScenario_name().equalsIgnoreCase("Requisition Creation")
								&& (!fetchMetadataListVO.get(0).getScenario_name().equalsIgnoreCase("Receivables")))) {
					methodCall = ebsActions(fetchMetadataVO, fetchMetadataVO.getTest_set_id(), actionName,
							screenshotPath, screenshotName);
					methods.add(methodCall);
					System.out.println("actionName --- " + actionName);
					System.out.println("methodCall --- " + methodCall);
				}
			}
			dto.setActions(methods);
			final Context ctx = new Context();
			ctx.setVariable("dto", dto);
			final String scriptContent = this.templateEngine.process("pyjab-script.txt", ctx);

			String destinationFilePath = fetchMetadataListVO.get(0).getCustomer_name() + FORWARD_SLASH
					+ fetchMetadataListVO.get(0).getTest_run_name() + FORWARD_SLASH + script_id + FORWARD_SLASH
					+ script_id + PY_EXTN;
			uploadObjectToObjectStore(scriptContent, destinationFilePath);
			this.kafkaTemp.send(topic, new PyJabKafkaDto(test_set_id, test_set_line_id, destinationFilePath));
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
			String screenshotPath, String screenshotname) throws Exception {
		PyJabActions action = actionRepo.findByActionName(actionName);
		String paramValue = action.getParamValues();
		StringJoiner methodCall = new StringJoiner(",", action.getMethodName() + "(", ");");
		String dbValue = "";
		String key = "";
		String value;

		if (paramValue != null) {
			HashMap<String, Object> result = new ObjectMapper().readValue(paramValue, HashMap.class);

			try {
				for (Map.Entry<String, Object> entry : result.entrySet()) {
					key = entry.getKey();
					value = (String) entry.getValue();

					if (value.equalsIgnoreCase("<Pick from Config Table>")) {
						dbValue = codeLineRepo.findByConfigurationId(Integer.parseInt(testrunId), key);
						methodCall.add(addQuotes(dbValue));
					}
					if (value.equalsIgnoreCase("<Pick from Input Value>")) {
						if (actionName.equalsIgnoreCase("ebsSelectMenu")) {
							dbValue = codeLineRepo.findByTestRunScriptId(
									Integer.parseInt(fetchMetadataVO.getTest_script_param_id()), key);

							if (dbValue.contains(">")) {
								String[] arrOfStr = dbValue.split(">", 5);
								if (arrOfStr.length < 2) {
									// copynumberValue = dbValue;
								} else {
									String menu = arrOfStr[0];
									String subMenu = arrOfStr[1];
									String menu_link = menu + "    " + subMenu;
									methodCall.add(addQuotes(menu_link));
								}
							}
						} else {
							dbValue = codeLineRepo.findByTestRunScriptId(
									Integer.parseInt(fetchMetadataVO.getTest_script_param_id()), key);
							methodCall.add(addQuotes(dbValue));
						}

					}

					if (value.equalsIgnoreCase("<Pick from Java>")) {
						if (actionName.equalsIgnoreCase("ebsPasteValue")) {
							String copynumberValue;
							dbValue = codeLineRepo.findByTestRunScriptId(
									Integer.parseInt(fetchMetadataVO.getTest_script_param_id()), key);
							String[] arrOfStr = dbValue.split(">", 5);
							if (arrOfStr.length < 2) {
								copynumberValue = dbValue;
							} else {
								String Testrun_name = arrOfStr[0];
								String seq = arrOfStr[1];
								String line_number = arrOfStr[2];
								if (Testrun_name.equalsIgnoreCase(fetchMetadataVO.getTest_run_name())
										&& seq.equalsIgnoreCase(fetchMetadataVO.getSeq_num())) {
									copynumberValue = dynamicnumber.getCopynumberInputParameter(Testrun_name, seq,
											line_number, null, null);
								} else {
									copynumberValue = dynamicnumber.getCopynumber(Testrun_name, seq, line_number, null,
											null);
								}
							}
							methodCall.add(addQuotes(copynumberValue));
						} else {
							String image_dest = "C:\\\\EBS-Automation\\\\WATS_Files\\\\screenshot\\\\ebs\\\\"
									+ fetchMetadataVO.getCustomer_name() + "\\\\" + fetchMetadataVO.getTest_run_name();

							dbValue = image_dest;
							methodCall.add(addQuotes(dbValue));
						}
					}
					if (value.equalsIgnoreCase("<Pick from Input Parameter>")) {
						dbValue = codeLineRepo.findByTestRunScriptIdInputParam(
								Integer.parseInt(fetchMetadataVO.getTest_script_param_id()), key);
						methodCall.add(addQuotes(dbValue));
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		methodCall.add(addQuotes(screenshotPath));
		methodCall.add(addQuotes(screenshotname));
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

	public String uploadObjectToObjectStore(String sourceFile, String destinationFilePath) {
		PutObjectResponse response = null;
		byte[] bytes = sourceFile.getBytes(StandardCharsets.UTF_8);
//
//		try {
//			
//			File f = new File("C:\\" + destinationFilePath);
////			f.mkdirs();
//			Path path = Paths.get("C:\\" + destinationFilePath);
//			
//			Files.writeString(path, sourceFile);
//			log.info("Uploaded to " + path);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		try (InputStream in = new ByteArrayInputStream(bytes);) {
			final ConfigFileReader.ConfigFile configFile = ConfigFileReader
					.parse(new ClassPathResource("oci/config").getInputStream(), "WATS_WINFOERP");
			final AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);

			/* Create a service client */
			ObjectStorageClient client = new ObjectStorageClient(provider);

			/* Create a request and dependent object(s). */
			PutObjectRequest putObjectRequest = PutObjectRequest.builder().namespaceName("nrch2emfoqis")
					.bucketName("obj-watsdev01-standard").objectName(destinationFilePath).putObjectBody(in).build();

			/* Send request to the Client */
			response = client.putObject(putObjectRequest);

			log.info("Uploaded to " + destinationFilePath);
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response.toString();
//		return null;
	}
	
	public void generateTestScriptLineIdReports() {
//		script_id = fetchMetadataListVO.get(0).getScript_id();
//		passurl = fetchConfigVO.getImg_url() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
//				+ fetchMetadataListVO.get(0).getTest_run_name() + "/" + "Passed_Report.pdf" + "AAAparent="
//				+ fetchConfigVO.getImg_url();
//		failurl = fetchConfigVO.getImg_url() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
//				+ fetchMetadataListVO.get(0).getTest_run_name() + "/" + "Failed_Report.pdf" + "AAAparent="
//				+ fetchConfigVO.getImg_url();
//		detailurl = fetchConfigVO.getImg_url() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
//				+ fetchMetadataListVO.get(0).getTest_run_name() + "/" + "Detailed_Report.pdf" + "AAAparent="
//				+ fetchConfigVO.getImg_url();
//		scripturl = fetchConfigVO.getImg_url() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
//				+ fetchMetadataListVO.get(0).getTest_run_name() + "/" + fetchMetadataListVO.get(0).getSeq_num()
//				+ "_" + fetchMetadataListVO.get(0).getScript_number() + ".pdf" + "AAAparent="
//				+ fetchConfigVO.getImg_url();

//					// MetaData Webservice
//					if (fetchMetadataListVO.size() == i) {
//						if (instanceName.equalsIgnoreCase("EBS") && (!fetchMetadataListVO.get(0).getScenario_name()
//								.equalsIgnoreCase("Requisition Creation")
//								&& (!fetchMetadataListVO.get(0).getScenario_name().equalsIgnoreCase("Receivables")))) {
//							String Folder = (fetchConfigVO.getWINDOWS_PDF_LOCATION()
//									+ fetchMetadataListVO.get(0).getCustomer_name() + "/"
//									+ fetchMetadataListVO.get(0).getTest_run_name() + "/" + "robot" + "/");
//
//							File theDir = new File(Folder);
//							if (!theDir.exists()) {
//								boolean result = false;
//								try {
//									theDir.mkdirs();
//									result = true;
//								} catch (SecurityException se) {
//									// handle it
//									System.out.println(se.getMessage());
//								}
//							} else {
//								System.out.println("Folder exist");
//							}
//							FileWriter writer;
//							try {
//								String pythonFileName = seq_num + "_" + fetchMetadataListVO.get(0).getScript_number()
//										+ ".py";
//								String completePath = Folder + pythonFileName;
//								writer = new FileWriter(completePath);
//
////								writer.write(codeline + System.lineSeparator());
//								writer.close();
//								robotFileTransfer(completePath, pythonFileName);
//								String response = callLocalRobot(
//										seq_num + "_" + fetchMetadataListVO.get(0).getScript_number(), screenshotPath);
//								JSONObject jsonobj = new JSONObject(response);
//								String jsonResponse = "";
//								for (Iterator iterator = jsonobj.keySet().iterator(); iterator.hasNext();) {
//									String key = (String) iterator.next();
//									if (!(key.equalsIgnoreCase("status"))) {
//										String value = (String) jsonobj.get(key);
//										seleniumFactory.getInstanceObj(instanceName).updateCopyValue(key, value,
//												test_set_line_id, test_set_id);
//									}
//									if ((key.equalsIgnoreCase("status"))) {
//										String value = (String) jsonobj.get(key);
//										jsonResponse = value;
//
//									}
//
//									if (jsonResponse.equalsIgnoreCase("pass")) {
//										for (FetchMetadataVO fetchMetadataVO1 : fetchMetadataListVO) {
//											dataBaseEntry.updatePassedScriptLineStatus(fetchMetadataVO1, fetchConfigVO,
//													fetchMetadataVO1.getTest_script_param_id(), "Pass");
//
//										}
//										FetchScriptVO post = new FetchScriptVO();
//										post.setP_test_set_id(test_set_id);
//										post.setP_status("Pass");
//										post.setP_script_id(script_id);
//										post.setP_test_set_line_id(test_set_line_id);
//										post.setP_pass_path(passurl);
//										post.setP_fail_path(failurl);
//										post.setP_exception_path(detailurl);
//										post.setP_test_set_line_path(scripturl);
//										// passcount = passcount+1;
//										Date enddate = new Date();
//										fetchConfigVO.setEndtime(enddate);
//										try {
//											dataService.updateTestCaseStatus(post, param, fetchConfigVO);
//											dataBaseEntry.updateEndTime(fetchConfigVO, test_set_line_id, test_set_id,
//													enddate);
//										} catch (Exception e) {
//											System.out.println("e");
//										}
//										seleniumFactory.getInstanceObj(instanceName).createPdf(fetchMetadataListVO,
//												fetchConfigVO, seq_num + "_" + script_Number + ".pdf", startdate,
//												enddate);
//										limitScriptExecutionService.insertTestRunScriptData(fetchConfigVO,
//												fetchMetadataListVO, script_id1, script_Number, "pass", startdate,
//												enddate);
//									}
//									if (jsonResponse.equalsIgnoreCase("fail")) {
//										System.out.println("inside fail :");
//										List<String> fileNameList = new ArrayList<String>();
//										File folder = new File(fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION()
//												+ fetchMetadataListVO.get(0).getCustomer_name() + "\\"
//												+ fetchMetadataListVO.get(0).getTest_run_name() + "\\");
//
//										File[] listOfFiles = folder.listFiles();
//										List<File> allFileList = Arrays.asList(listOfFiles);
//										List<File> fileList = new ArrayList<>();
//										String seqNumber = fetchMetadataListVO.get(0).getSeq_num();
//										for (File file : allFileList) {
//											if (file.getName().startsWith(seqNumber + "_")) {
//												fileList.add(file);
//											}
//										}
//
//										Collections.sort(fileList, new Comparator<File>() {
//
//											public int compare(File f1, File f2) {
//
//												return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified())
//														* -1;
//
//											}
//
//										});
//										for (File f : fileList) {
//											fileNameList.add(f.getName());
//										}
//										String name = fileNameList.get(0);
//										String arr[] = name.split("_", 0);
//										String lastScreenshotSeqNum = arr[1];
//										int lastPassedSeq = Integer.parseInt(lastScreenshotSeqNum);
//										String lastSeqNum = fetchMetadataListVO.get(fetchMetadataListVO.size() - 1)
//												.getSeq_num();
//										for (FetchMetadataVO fetchMetadataVO11 : fetchMetadataListVO) {
//											// int lastPassedSeq=Integer.parseInt (lastScreenshotSeqNum);
//											int currentSeqNum = Integer.parseInt(fetchMetadataVO11.getLine_number());
//											if (currentSeqNum <= lastPassedSeq) {
//												dataBaseEntry.updatePassedScriptLineStatus(fetchMetadataVO11,
//														fetchConfigVO, fetchMetadataVO11.getTest_script_param_id(),
//														"Pass");
//											} else {
//												message = "EBS Execution Failed";
//												dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO11,
//														fetchConfigVO, fetchMetadataVO11.getTest_script_param_id(),
//														"Fail", message);
//												fetchConfigVO.setErrormessage(message);
//												FetchScriptVO post = new FetchScriptVO();
//												post.setP_test_set_id(test_set_id);
//												post.setP_status("Fail");
//												post.setP_script_id(script_id);
//												post.setP_test_set_line_id(test_set_line_id);
//												post.setP_pass_path(passurl);
//												post.setP_fail_path(failurl);
//												post.setP_exception_path(detailurl);
//												post.setP_test_set_line_path(scripturl);
//												failcount = failcount + 1;
//												Date enddate = new Date();
//												fetchConfigVO.setEndtime(enddate);
//												dataService.updateTestCaseStatus(post, param, fetchConfigVO);
//												dataBaseEntry.updateEndTime(fetchConfigVO, test_set_line_id,
//														test_set_id, enddate);
//
//												File file = new ClassPathResource(whiteimage).getFile();
//												File file1 = new ClassPathResource(watsvediologo).getFile();
//
//												BufferedImage image = null;
//												image = ImageIO.read(file);
//												BufferedImage logo = null;
//												logo = ImageIO.read(file1);
//												BufferedImage image1 = null;
//												image1 = ImageIO.read(file);
//												Graphics g1 = image1.getGraphics();
//												g1.setColor(Color.red);
//												java.awt.Font font1 = new java.awt.Font("Calibir", java.awt.Font.PLAIN,
//														36);
//												g1.setFont(font1);
//												g1.drawString("FAILED IN THIS STEP!!", 400, 300);
//												g1.drawImage(logo, 1012, 15, null);
//												g1.dispose();
//
//												ImageIO.write(image1, "jpg",
//														new File(fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION()
//																+ fetchMetadataListVO.get(0).getCustomer_name() + "\\"
//																+ fetchMetadataListVO.get(0).getTest_run_name() + "\\"
//																+ fetchMetadataVO.getSeq_num() + "_" + currentSeqNum
//																+ "_" + fetchMetadataVO.getScenario_name() + "_"
//																+ fetchMetadataVO.getScript_number() + "_"
//																+ fetchMetadataVO.getTest_run_name() + "_"
//																+ currentSeqNum + "_Failed.jpg"));
//
//												seleniumFactory.getInstanceObj(instanceName).createFailedPdf(
//														fetchMetadataListVO, fetchConfigVO,
//														seq_num + "_" + script_Number + ".pdf", startdate, enddate);
//
//												limitScriptExecutionService.insertTestRunScriptData(fetchConfigVO,
//														fetchMetadataListVO, script_id1, script_Number, "Fail",
//														startdate, enddate);
//												break;
//											}
//
//										}
//									}
//
//								}
//								System.out.println(response);
//
//							} catch (IOException e) {
//								e.printStackTrace();
//							}
//
//						} else {
//							FetchScriptVO post = new FetchScriptVO();
//							post.setP_test_set_id(test_set_id);
//							post.setP_status("Pass");
//							post.setP_script_id(script_id);
//							post.setP_test_set_line_id(test_set_line_id);
//							post.setP_pass_path(passurl);
//							post.setP_fail_path(failurl);
//							post.setP_exception_path(detailurl);
//							post.setP_test_set_line_path(scripturl);
//							// passcount = passcount+1;
//							Date enddate = new Date();
//							fetchConfigVO.setEndtime(enddate);
//							try {
//								dataService.updateTestCaseStatus(post, param, fetchConfigVO);
//								dataBaseEntry.updateEndTime(fetchConfigVO, test_set_line_id, test_set_id, enddate);
//							} catch (Exception e) {
//								System.out.println("e");
//							}
//							seleniumFactory.getInstanceObj(instanceName).createPdf(fetchMetadataListVO, fetchConfigVO,
//									seq_num + "_" + script_Number + ".pdf", startdate, enddate);
//							if ("OBJECT_STORE".equalsIgnoreCase(fetchConfigVO.getPDF_LOCATION())) {
//								seleniumFactory.getInstanceObj(fetchConfigVO.getInstance_name())
//										.uploadPDF(fetchMetadataListVO, fetchConfigVO);
//							}
//							limitScriptExecutionService.insertTestRunScriptData(fetchConfigVO, fetchMetadataListVO,
//									script_id1, script_Number, "pass", startdate, enddate);
//							limitScriptExecutionService.updateFaileScriptscount(test_set_line_id, test_set_id);
//						}
//					}
//					try {
//						if (instanceName.equalsIgnoreCase("EBS") && (!fetchMetadataListVO.get(0).getScenario_name()
//								.equalsIgnoreCase("Requisition Creation")
//								&& (!fetchMetadataListVO.get(0).getScenario_name().equalsIgnoreCase("Receivables")))) {
//						} else {
//							dataBaseEntry.updatePassedScriptLineStatus(fetchMetadataVO, fetchConfigVO,
//									test_script_param_id, "Pass");
//							dataBaseEntry.updateFailedImages(fetchMetadataVO, fetchConfigVO, test_script_param_id);
//						}
//
//					} catch (Exception e) {
//						System.out.println("e");
//					}
//				} catch (Exception e) {
//
//					System.out.println("Failed to Execute the " + "" + actionName);
//					System.out.println(
//							"Error occurred in TestCaseName=" + actionName + "" + "Exception=" + "" + e.getMessage());
//					errorMessagesHandler.getError(actionName, fetchMetadataVO, fetchConfigVO, test_script_param_id,
//							message, param1, param2, dataBaseEntry.getPassword(param, userName, fetchConfigVO));
//
//					FetchScriptVO post = new FetchScriptVO();
//					post.setP_test_set_id(test_set_id);
//					post.setP_status("Fail");
//					post.setP_script_id(script_id);
//					post.setP_test_set_line_id(test_set_line_id);
//					post.setP_pass_path(passurl);
//					post.setP_fail_path(failurl);
//					post.setP_exception_path(detailurl);
//					post.setP_test_set_line_path(scripturl);
//					failcount = failcount + 1;
//					Date enddate = new Date();
//					fetchConfigVO.setEndtime(enddate);
//					dataService.updateTestCaseStatus(post, param, fetchConfigVO);
//					dataBaseEntry.updateEndTime(fetchConfigVO, test_set_line_id, test_set_id, enddate);
//					int failedScriptRunCount = limitScriptExecutionService.getFailedScriptRunCount(test_set_line_id,
//							test_set_id);
//					if (failedScriptRunCount == 1) {
//						seleniumFactory.getInstanceObj(instanceName).createFailedPdf(fetchMetadataListVO, fetchConfigVO,
//								seq_num + "_" + script_Number + ".pdf", startdate, enddate);
//
//					} else if (failedScriptRunCount == 2) {
//						limitScriptExecutionService.renameFailedFile(fetchMetadataListVO, fetchConfigVO,
//								seq_num + "_" + script_Number + ".pdf", failedScriptRunCount);
//						seleniumFactory.getInstanceObj(instanceName).createFailedPdf(fetchMetadataListVO, fetchConfigVO,
//								seq_num + "_" + script_Number + "_RUN" + failedScriptRunCount + ".pdf", startdate,
//								enddate);
//
//					} else {
//						seleniumFactory.getInstanceObj(instanceName).createFailedPdf(fetchMetadataListVO, fetchConfigVO,
//								seq_num + "_" + script_Number + "_RUN" + failedScriptRunCount + ".pdf", startdate,
//								enddate);
//					}
//					if ("OBJECT_STORE".equalsIgnoreCase(fetchConfigVO.getPDF_LOCATION())) {
//						seleniumFactory.getInstanceObj(fetchConfigVO.getInstance_name()).uploadPDF(fetchMetadataListVO,
//								fetchConfigVO);
//					}
//					limitScriptExecutionService.insertTestRunScriptData(fetchConfigVO, fetchMetadataListVO, script_id1,
//							script_Number, "Fail", startdate, enddate);
//					throw e;
//				}
//			}
	}

}
