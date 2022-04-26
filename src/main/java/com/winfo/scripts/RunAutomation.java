package com.winfo.scripts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.log.SysoCounter;
import com.lowagie.text.DocumentException;
import com.winfo.Factory.SeleniumKeywordsFactory;
import com.winfo.config.DriverConfiguration;
import com.winfo.dao.ActionsRepository;
import com.winfo.dao.CodeLinesRepository;
import com.winfo.model.Actions;
import com.winfo.model.CodeLines;
import com.winfo.services.DataBaseEntry;
import com.winfo.services.ErrorMessagesHandler;
import com.winfo.services.FetchConfigVO;
import com.winfo.services.FetchMetadataVO;
import com.winfo.services.FetchScriptVO;
import com.winfo.services.LimitScriptExecutionService;
import com.winfo.services.TestCaseDataService;
import com.winfo.vo.ExecuteTestrunVo;

@Service

public class RunAutomation {
	Logger log = Logger.getLogger("Logger");

	@Autowired
	SeleniumKeywordsFactory seleniumFactory;
	@Autowired
	ErrorMessagesHandler errorMessagesHandler;
	@Autowired
	DriverConfiguration deriverConfiguration;

	@Autowired
	TestCaseDataService dataService;
	@Autowired
	DataBaseEntry dataBaseEntry;
	public String c_url = null;
	@Value("${configvO.watslogo}")
	private String watslogo;

	@Value("${configvO.watsvediologo}")
	private String watsvediologo;

	@Value("${configvO.whiteimage}")
	private String whiteimage;
	@Autowired
	LimitScriptExecutionService limitScriptExecutionService;
	
	@Autowired
	CodeLinesRepository codeLineRepo;

	@Autowired
	ActionsRepository actionRepo;
	/*
	 * public void report() throws IOException, DocumentException,
	 * com.itextpdf.text.DocumentException {
	 * 
	 * FetchMetadataVO fetchMetadataVO = new FetchMetadataVO();
	 * List<FetchMetadataVO> fetchMetadataListVO = new ArrayList<FetchMetadataVO>();
	 * fetchMetadataListVO.add(fetchMetadataVO); FetchConfigVO fetchConfigVO = new
	 * FetchConfigVO();
	 * 
	 * createPdf(fetchMetadataListVO, fetchConfigVO, "Passed_Report.pdf", passcount,
	 * failcount);
	 * 
	 * createPdf(fetchMetadataListVO, fetchConfigVO, "Failed_Report.pdf", passcount,
	 * failcount); createPdf(fetchMetadataListVO, fetchConfigVO,
	 * "Detailed_Report.pdf", passcount, failcount);
	 * 
	 * 
	 * }
	 */
	public void report() throws IOException, DocumentException, com.itextpdf.text.DocumentException {

		FetchMetadataVO fetchMetadataVO = new FetchMetadataVO();
		List<FetchMetadataVO> fetchMetadataListVO = new ArrayList<FetchMetadataVO>();
		fetchMetadataListVO.add(fetchMetadataVO);
		FetchConfigVO fetchConfigVO = new FetchConfigVO();

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

	public ExecuteTestrunVo run(String args) throws MalformedURLException {
		System.out.println(args);
		ExecuteTestrunVo executeTestrunVo = new ExecuteTestrunVo();

		try {
			// Config Webservice
//			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");  
//	        LocalDateTime now = LocalDateTime.now();
//	        String start_time=dtf.format(now);
//			System.out.println("instanceName"+instanceName);
			FetchConfigVO fetchConfigVO = dataService.getFetchConfigVO(args);
			// FetchMetadataVO fetchMetadataVO = (FetchMetadataVO)
			// dataService.getFetchMetaData(args, uri);
//			fetchConfigVO.setChrome_driver_path("E:\\downloads-chakradhar\\chromedriver.exe");
//			fetchConfigVO.setPdf_path("E:\\wats-chakradhar\\pdfpatrh\\");
//			fetchConfigVO.setScreenshot_path("E:\\wats-chakradhar\\Scroonshootpath\\");
		//	fetchConfigVO.setChrome_driver_path("C:\\Users\\Winfo solutions\\Priya\\cromeDriver\\chromedriver_win32\\chromedriver.exe");
		//	fetchConfigVO.setPdf_path("C:\\\\Users\\\\Winfo solutions\\\\Priya\\\\softwares\\\\wats\\\\jars\\\\padf\\\\");
		//	fetchConfigVO.setScreenshot_path("C:\\\\Users\\\\Winfo solutions\\\\Priya\\\\softwares\\\\wats\\\\jars\\\\screen\\\\");
	//		fetchConfigVO.setChrome_driver_path("C:\\EBS-Automation\\EBS Automation-POC\\Driver\\chromedriver.exe");
			//fetchConfigVO.setPdf_path("C:\\EBS-Automation\\WATS_Files\\pdf\\");
			//fetchConfigVO.setScreenshot_path("C:\\\\EBS-Automation\\\\WATS_Files\\\\screenshot\\\\");

			final String uri = fetchConfigVO.getMETADATA_URL()+ args;
			System.out.println("fetchConfigVO.getDownlod_file_path()"+fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION()+fetchConfigVO.getUri_config()+fetchConfigVO.getWINDOWS_PDF_LOCATION() );
		 	List<FetchMetadataVO> fetchMetadataListVO = dataService.getFetchMetaData(args, uri);
			System.out.println(fetchMetadataListVO.size());
//			LinkedHashMap<String, List<FetchMetadataVO>> dependentScriptMap=new LinkedHashMap<String, List<FetchMetadataVO>>();
//			LinkedHashMap<String, List<FetchMetadataVO>> metaDataMap = dataService
//					.prepareTestcasedata(fetchMetadataListVO,dependentScriptMap);
			SortedMap<Integer, List<FetchMetadataVO>> dependentScriptMap=new TreeMap<Integer, List<FetchMetadataVO>>();
			SortedMap<Integer, List<FetchMetadataVO>> metaDataMap = dataService.prepareTestcasedata(fetchMetadataListVO,dependentScriptMap);
			
//			Map<Integer, Boolean> mutableMap = limitScriptExecutionService.getLimitedCoundiationExaption(fetchConfigVO,
//					fetchMetadataListVO, metaDataMap, args);

			Date date = new Date();
			  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			  sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
			//  Date startDate=sdf.parse(fetchConfigVO.getStart_date());
		//	Date endDate=sdf.parse(fetchConfigVO.getEnd_date());
//	
//						for(Entry<Integer, Boolean> entryMap:mutableMap.entrySet()) {
//				if (entryMap.getValue()||date.after(endDate)||date.before(startDate)) {
//					executeTestrunVo.setStatusCode(404);
//					executeTestrunVo.setStatusMessage("ERROR");
//					if(entryMap.getKey()>0) {
//					executeTestrunVo.setStatusDescr("Your request could not be processed as you have reached the scripts execution threshold. You can run only run "+entryMap.getKey()+" more scripts. Reach out to the WATS support team to enhance the limit..");
//					}else if(date.after(endDate)||date.before(startDate)){
//						executeTestrunVo.setStatusDescr("Your request could not be processed the Testrun, please check with the Start and End Date");
//	
//					}else{
//						executeTestrunVo.setStatusDescr("Your request could not be processed as you have reached the scripts execution threshold. Reach out to the WATS support team to enhance the limit..");
//	
//					}
//					return executeTestrunVo;
//
//				}
//			}


			fetchConfigVO.setStarttime1(date);
			
			//System.out.println(metaDataMap.toString());
	
			ExecutorService executor = Executors.newFixedThreadPool(fetchConfigVO.getParallel_independent());
		   try {
			for (Entry<Integer, List<FetchMetadataVO>> metaData : metaDataMap.entrySet()) {
				executor.execute(() -> {
					try {
						long starttimeIntermediate = System.currentTimeMillis();
						String flag = dataBaseEntry.getTrMode(args, fetchConfigVO);
						if (flag.equalsIgnoreCase("STOPPED")) {
							metaData.getValue().clear();
							executor.shutdown();
							System.out.println("treminattion is succeed");
						} else {
							//System.out.println(metaData.getKey());
							executorMethod(args, fetchConfigVO, fetchMetadataListVO, metaData);
						}
						long i = System.currentTimeMillis() - starttimeIntermediate;
						increment = increment + i;
						System.out.println("time" + increment / 1000 % 60);
					} catch (IOException | DocumentException | com.itextpdf.text.DocumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
			}
			executor.shutdown();
		   } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
				if (executor.isTerminated()) {
					ExecutorService executordependent = Executors
							.newFixedThreadPool(fetchConfigVO.getParallel_dependent());
					/*
					 * LinkedHashMap<String, List<FetchMetadataVO>> dependantmetaDataMap =
					 * dataService .getDependentScriptMap();
					 */
				//	System.out.println(dependentScriptMap.toString());
					for (Entry<Integer, List<FetchMetadataVO>> metaData : dependentScriptMap.entrySet()) {
						executordependent.execute(() -> {
							try {
								String flag = dataBaseEntry.getTrMode(args, fetchConfigVO);
								if (flag.equalsIgnoreCase("STOPPED")) {
									metaData.getValue().clear();
									executor.shutdown();
									System.out.println("treminattion is succeed");
								} else {
									executorMethod(args, fetchConfigVO, fetchMetadataListVO, metaData);
								}
							}  catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								
							}finally {
								try {
								seleniumFactory.getInstanceObj(fetchConfigVO.getInstance_name()).createPdf(fetchMetadataListVO,
										fetchConfigVO, "Passed_Report.pdf", null, null);
								seleniumFactory.getInstanceObj(fetchConfigVO.getInstance_name()).createPdf(fetchMetadataListVO,
										fetchConfigVO, "Failed_Report.pdf", null, null);
								seleniumFactory.getInstanceObj(fetchConfigVO.getInstance_name()).createPdf(fetchMetadataListVO,
										fetchConfigVO, "Detailed_Report.pdf", null, null);
								increment = 0;
								if ("ARLO".equalsIgnoreCase(fetchConfigVO.getInstance_name())) {
									seleniumFactory.getInstanceObj(fetchConfigVO.getInstance_name()).uploadPDF(fetchMetadataListVO,
											fetchConfigVO);
								}
							}  catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								
							}
							}
						});
					}
					executordependent.shutdown();
				}
				seleniumFactory.getInstanceObj(fetchConfigVO.getInstance_name()).createPdf(fetchMetadataListVO,
						fetchConfigVO, "Passed_Report.pdf", null, null);
				seleniumFactory.getInstanceObj(fetchConfigVO.getInstance_name()).createPdf(fetchMetadataListVO,
						fetchConfigVO, "Failed_Report.pdf", null, null);
				seleniumFactory.getInstanceObj(fetchConfigVO.getInstance_name()).createPdf(fetchMetadataListVO,
						fetchConfigVO, "Detailed_Report.pdf", null, null);
				increment = 0;
				if ("OBJECT_STORE".equalsIgnoreCase(fetchConfigVO.getPDF_LOCATION())) {
					seleniumFactory.getInstanceObj(fetchConfigVO.getInstance_name()).uploadPDF(fetchMetadataListVO,
							fetchConfigVO);
				}
				
				uploadScreenshotsToObjectStore(fetchConfigVO,fetchMetadataListVO);
				executeTestrunVo.setStatusCode(200);
				executeTestrunVo.setStatusMessage("SUCCESS");
				executeTestrunVo.setStatusDescr("SUCCESS");
			} catch (InterruptedException e) {
				e.printStackTrace();
				// Restore interrupted state...
				Thread.currentThread().interrupt();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error in Block 1");
			FetchScriptVO post = new FetchScriptVO();
			post.setP_status("Fail");
			dataService.updateTestCaseStatus(post, args, null);
		}
		return executeTestrunVo;
	}

	public void executorMethod(String args, FetchConfigVO fetchConfigVO, List<FetchMetadataVO> fetchMetadataListVO,
			Entry<Integer, List<FetchMetadataVO>> metaData) throws Exception {
		List<String> failList = new ArrayList<String>();
		WebDriver driver = null;
//		//String start_time=null;
//		String end_time=null;
//		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");  
//        LocalDateTime now = LocalDateTime.now();

		String test_set_id = fetchMetadataListVO.get(0).getTest_set_id();
		String test_set_line_id = fetchMetadataListVO.get(0).getTest_set_line_id();
		// start_time=dtf.format(now);

		String script_id = fetchMetadataListVO.get(0).getScript_id();
		String passurl = fetchConfigVO.getImg_url() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
                + fetchMetadataListVO.get(0).getTest_run_name() + "/" + "Passed_Report.pdf" + "AAAparent="+fetchConfigVO.getImg_url();
		String failurl = fetchConfigVO.getImg_url() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
                + fetchMetadataListVO.get(0).getTest_run_name() + "/" + "Failed_Report.pdf" + "AAAparent="+fetchConfigVO.getImg_url();
		String detailurl = fetchConfigVO.getImg_url() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
                + fetchMetadataListVO.get(0).getTest_run_name() + "/" + "Detailed_Report.pdf" + "AAAparent="+fetchConfigVO.getImg_url();
		String scripturl = fetchConfigVO.getImg_url() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
                + fetchMetadataListVO.get(0).getTest_run_name() + "/" + fetchMetadataListVO.get(0).getSeq_num()
                + "_" + fetchMetadataListVO.get(0).getScript_number() + ".pdf" + "AAAparent="+fetchConfigVO.getImg_url();
//		System.out.println(passurl);
//		System.out.println(failurl);
//		System.out.println(detailurl);
		boolean isDriverError = true;
		try {
			if(fetchConfigVO.getInstance_name().equalsIgnoreCase("EBS"))
			{
				if(fetchMetadataListVO.get(0).getScenario_name().equalsIgnoreCase("Requisition Creation")||fetchMetadataListVO.get(0).getScenario_name().equalsIgnoreCase("Receivables"))
				{
					driver = deriverConfiguration.getWebDriver(fetchConfigVO);
				}
			}
			else
			{
				driver = deriverConfiguration.getWebDriver(fetchConfigVO);
			}
		//	driver = deriverConfiguration.getWebDriver(fetchConfigVO);
			isDriverError = false;
			List<FetchMetadataVO> fetchMetadataListsVO = metaData.getValue();
			switchActions(args, driver, fetchMetadataListsVO, fetchConfigVO);

		} catch (Exception e) {
//			screenshotException(driver, "Test Action Name Not Exists_", fetchMetadataListVO, fetchConfigVO, "0", inputParam);
			System.out.println(e);
//			uploadPDF(fetchMetadataListVO, fetchConfigVO);
			if (isDriverError) {
				FetchScriptVO post = new FetchScriptVO();
				post.setP_test_set_id(test_set_id);
				post.setP_status("Fail");
				post.setP_script_id(script_id);
				post.setP_test_set_line_id(test_set_line_id);
				post.setP_pass_path(passurl);
				post.setP_fail_path(failurl);
				post.setP_exception_path(detailurl);
				post.setP_test_set_line_path(scripturl);
				dataService.updateTestCaseStatus(post, args, fetchConfigVO);
				failList.add(script_id);
			} else {
				throw e;
			}
		} finally {
			System.out.println("Execution is completed with" + "" + fetchMetadataListVO.get(0).getScript_id());
			if(fetchConfigVO.getInstance_name().equalsIgnoreCase("EBS"))
			{
				if(fetchMetadataListVO.get(0).getScenario_name().equalsIgnoreCase("Requisition Creation")||fetchMetadataListVO.get(0).getScenario_name().equalsIgnoreCase("Receivables"))
				{
					driver.quit();
				}
			}
			else
			{
				driver.quit();
			}
		}
	}

	int passcount = 0;
	int failcount = 0;

	public void switchActions(String param, WebDriver driver, List<FetchMetadataVO> fetchMetadataListVO,
			FetchConfigVO fetchConfigVO) throws Exception {

		String log4jConfPath = "log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);

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
		String line_number = null;
		String seq_num = null;
		String step_description = null;
		String test_script_param_id = null;
		ArrayList<String> robotCodeLines;
		ArrayList<String> listOfRobotCodeLines= new ArrayList<>();
		// String start_time=null;
		// String end_time=null;
	
		try {
			script_id = fetchMetadataListVO.get(0).getScript_id();
            passurl = fetchConfigVO.getImg_url() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
                    + fetchMetadataListVO.get(0).getTest_run_name() + "/" + "Passed_Report.pdf" + "AAAparent="+fetchConfigVO.getImg_url();
            failurl = fetchConfigVO.getImg_url() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
                    + fetchMetadataListVO.get(0).getTest_run_name() + "/" + "Failed_Report.pdf" + "AAAparent="+fetchConfigVO.getImg_url();
            detailurl = fetchConfigVO.getImg_url() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
                    + fetchMetadataListVO.get(0).getTest_run_name() + "/" + "Detailed_Report.pdf" + "AAAparent="+fetchConfigVO.getImg_url();
            scripturl = fetchConfigVO.getImg_url() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
                    + fetchMetadataListVO.get(0).getTest_run_name() + "/" + fetchMetadataListVO.get(0).getSeq_num()
                    + "_" + fetchMetadataListVO.get(0).getScript_number() + ".pdf" + "AAAparent="+fetchConfigVO.getImg_url();

			String userName = null;
			String globalValueForSteps = null;
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
			Date startdate = new Date();
			fetchConfigVO.setStarttime(startdate);
			String instanceName = fetchConfigVO.getInstance_name();
			seleniumFactory.getInstanceObj(instanceName).DelatedScreenshoots(fetchMetadataListVO, fetchConfigVO);
			if(instanceName.equalsIgnoreCase("EBS")&&(!fetchMetadataListVO.get(0).getScenario_name().equalsIgnoreCase("Requisition Creation")&&(!fetchMetadataListVO.get(0).getScenario_name().equalsIgnoreCase("Receivables"))))
			{
			List<Actions> listOfActions=actionRepo.findByActionTypeOrderById("default");
			List<CodeLines>listOfCodeLines=codeLineRepo.findByActionIdInOrderByActionIdAscCodeLineIdAsc(listOfActions);
			for(CodeLines codeLine:listOfCodeLines)
			{ 
				if(codeLine.getParam_values()==null)
				{
					listOfRobotCodeLines.add(codeLine.getRobot_line());
				}
				if(codeLine.getParam_values()!=null)
				{
					String param_value=codeLine.getParam_values();
					HashMap<String, Object> result=null;
					String dbValue="";
					String key ;
					String value;
					String defaultCodeLine=codeLine.getRobot_line();
					try {
						result = new ObjectMapper().readValue(param_value, HashMap.class);
						
						for (Map.Entry<String,Object> entry : result.entrySet())
						{
				            System.out.println("Key = " + entry.getKey() +
				                             ", Value = " + entry.getValue());
				             key =entry.getKey();
							 value=(String)entry.getValue();
							if(value.equalsIgnoreCase("<Pick from Config Table>"))
							{
								dbValue=	codeLineRepo.findByConfigurationId(Integer.parseInt(fetchMetadataListVO.get(0).getTest_set_id()),key);
							}
							if(value.equalsIgnoreCase("<Pick from Java>"))
							{String	image_dest="";
								if(defaultCodeLine.contains("[TearDown]"))
								{
										image_dest = "C:\\\\EBS-Automation\\\\WATS_Files\\\\screenshot\\\\ebs\\\\" + fetchMetadataListVO.get(0).getCustomer_name() + "\\\\"+ fetchMetadataListVO.get(0).getTest_run_name()+"	"+fetchMetadataListVO.get(0).getSeq_num();
								}
								else
								{
										image_dest = "C:\\\\EBS-Automation\\\\WATS_Files\\\\screenshot\\\\ebs\\\\" + fetchMetadataListVO.get(0).getCustomer_name() + "\\\\"+ fetchMetadataListVO.get(0).getTest_run_name();

								}

							//	String path=fetchMetadataVO.getCustomer_name() + "/"+ fetchMetadataVO.getTest_run_name() + "/";
								dbValue=image_dest;
								
							}
							
							defaultCodeLine= defaultCodeLine.replace("${"+key+"}", dbValue);
							//System.out.println(defaultCodeLine);
						}
						
					} catch (JsonMappingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//String defaultCodeLine=codeLine.getRobot_line();
					
				//	defaultCodeLine= defaultCodeLine.replace("${"+(String) result.keySet().toArray()[0]+"}", dbValue);
				//	System.out.println(defaultCodeLine);
					listOfRobotCodeLines.add(defaultCodeLine);
				}
			}
				
			}
			String parentName="";
			for (FetchMetadataVO fetchMetadataVO : fetchMetadataListVO) {
				String url = fetchConfigVO.getApplication_url();
				actionName = fetchMetadataVO.getAction();
				test_set_id = fetchMetadataVO.getTest_set_id();
				test_set_line_id = fetchMetadataVO.getTest_set_line_id();
				script_id1 = fetchMetadataVO.getScript_id();
				script_Number = fetchMetadataVO.getScript_number();
				line_number = fetchMetadataVO.getLine_number();
				seq_num = fetchMetadataVO.getSeq_num();
			
				dataBaseEntry.updateInProgressScriptStatus(fetchConfigVO, test_set_id, test_set_line_id);
				dataBaseEntry.updateStartTime(fetchConfigVO, test_set_line_id, test_set_id, startdate);
				step_description = fetchMetadataVO.getStep_description();
				String screenParameter = fetchMetadataVO.getInput_parameter();
				test_script_param_id = fetchMetadataVO.getTest_script_param_id();

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
					if(instanceName.equalsIgnoreCase("EBS")&&(!fetchMetadataListVO.get(0).getScenario_name().equalsIgnoreCase("Requisition Creation")&&(!fetchMetadataListVO.get(0).getScenario_name().equalsIgnoreCase("Receivables"))))
					{
						robotCodeLines=seleniumFactory.getInstanceObj(instanceName).ebsActions(fetchMetadataVO,fetchMetadataVO.getTest_set_id(),actionName);
						listOfRobotCodeLines.addAll(robotCodeLines);
						
						String	image_dest = ("C:\\\\EBS-Automation\\\\WATS_Files\\\\screenshot\\\\ebs\\\\" + fetchMetadataVO.getCustomer_name() + "\\\\"+ fetchMetadataVO.getTest_run_name() + "\\\\" + fetchMetadataVO.getSeq_num() + "_"+ fetchMetadataVO.getLine_number() + "_" + fetchMetadataVO.getScenario_name() + "_"+ fetchMetadataVO.getScript_number() + "_" + fetchMetadataVO.getTest_run_name() + "_"+ fetchMetadataVO.getLine_number() + "_Passed").concat(".jpg");
						String screenshotStep="    Screenshot.Take Screenshot    "+image_dest;
						listOfRobotCodeLines.add(screenshotStep);
					}
					else
					{
						dataBaseEntry.updateInProgressScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id,"In-Progress");
					switch (actionName) {

					case "Login into Application":
						
						userName = fetchMetadataVO.getInput_value();
						log.info("Navigating to Login into Application Action");
						if (fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
							seleniumFactory.getInstanceObj(instanceName).loginApplication(driver, fetchConfigVO,
									fetchMetadataVO, type1, type2, type3, param1, param2, param3,
									fetchMetadataVO.getInput_value(),"welcome123");
									//dataBaseEntry.getPassword(param, userName, fetchConfigVO));
							userName = null;
							break;
						} else {
							break;
						}
						

					case "Navigate":
						log.info("Navigating to Navigate Action");
						
						seleniumFactory.getInstanceObj(instanceName).navigate(driver, fetchConfigVO, fetchMetadataVO,
								type1, type2, param1, param2, count);
						break;
						

					case "openTask":
						log.info("Navigating to openTask Action");
						
						seleniumFactory.getInstanceObj(instanceName).openTask(driver, fetchConfigVO, fetchMetadataVO,
								type1, type2, param1, param2, count);
						break;
						
					case "Logout":
						seleniumFactory.getInstanceObj(instanceName).logout(driver, fetchConfigVO, fetchMetadataVO,
								type1, type2, type3, param1, param2, param3);
						break;
					case "SendKeys":
						
						
						if (fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
							seleniumFactory.getInstanceObj(instanceName).sendValue(driver, param1, param2,
									fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
							break;
						} else {
							break;
						}
						
					case "textarea":
						
						if (fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
							seleniumFactory.getInstanceObj(instanceName).textarea(driver, param1, param2,
									fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
							break;
						} else {
							break;
						}
						
					case "Dropdown Values":
						if (fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
							seleniumFactory.getInstanceObj(instanceName).dropdownValues(driver, param1, param2, param3,
									fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
							break;
						} else {
							break;
						}
					case "Table SendKeys":
						
						if (fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
							seleniumFactory.getInstanceObj(instanceName).tableSendKeys(driver, param1, param2, param3,
									fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
							break;
						} else {
							break;
						}
						
					case "multiplelinestableSendKeys":
						if (fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
							seleniumFactory.getInstanceObj(instanceName).multiplelinestableSendKeys(driver, param1,
									param2, param3, fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
							break;
						} else {
							break;
						}

					case "Table Dropdown Values":
						if (fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
							seleniumFactory.getInstanceObj(instanceName).tableDropdownValues(driver, param1, param2,
									fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
							break;
						} else {
							break;
						}
					case "clickLinkAction":
												if (fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
							seleniumFactory.getInstanceObj(instanceName).clickLinkAction(driver, param1, param2,
									fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
							break;
						} else {
							break;
						}
						
					case "clickCheckbox":
						if (fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
							seleniumFactory.getInstanceObj(instanceName).clickCheckbox(driver, param1,
									fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
							break;
						} else {
							break;
						}
					case "clickRadiobutton":
						if (fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
							seleniumFactory.getInstanceObj(instanceName).clickRadiobutton(driver, param1, param2,
									fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
							break;
						} else {
							break;
						}
					case "selectAValue":
						if (fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
							seleniumFactory.getInstanceObj(instanceName).selectAValue(driver, param1, param2,
									fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
							break;
						} else {
							break;
						}
					case "clickTableLink":
						
						seleniumFactory.getInstanceObj(instanceName).clickTableLink(driver, param1, param2,
								fetchMetadataVO, fetchConfigVO);
						break;
						
					case "clickLink":
					
						seleniumFactory.getInstanceObj(instanceName).clickLink(driver, param1, param2, fetchMetadataVO,
								fetchConfigVO);
						break;
						
					case "clickNotificationLink":
						seleniumFactory.getInstanceObj(instanceName).clickNotificationLink(driver, param1, param2,
								fetchMetadataVO, fetchConfigVO);
						break;
					case "clickMenu":
						seleniumFactory.getInstanceObj(instanceName).clickMenu(driver, param1, param2, fetchMetadataVO,
								fetchConfigVO);
						break;
					case "clickImage":
						seleniumFactory.getInstanceObj(instanceName).clickImage(driver, param1, param2, fetchMetadataVO,
								fetchConfigVO);
						break;
					case "clickTableImage":
						seleniumFactory.getInstanceObj(instanceName).clickTableImage(driver, param1, param2,
								fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
						break;
					case "clickExpandorcollapse":
						seleniumFactory.getInstanceObj(instanceName).clickExpandorcollapse(driver, param1, param2,
								fetchMetadataVO, fetchConfigVO);
						break;
						
					case "clickButton":
						
						seleniumFactory.getInstanceObj(instanceName).clickButton(driver, param1, param2,
								fetchMetadataVO, fetchConfigVO);
						message = seleniumFactory.getInstanceObj(instanceName).getErrorMessages(driver);
						String message1 = seleniumFactory.getInstanceObj(instanceName).getErrorMessages(driver);

						seleniumFactory.getInstanceObj(instanceName).clickButtonCheckPopup(driver, param1, param2,
								fetchMetadataVO, fetchConfigVO);

						if (message != null && !message.startsWith("Example") && !message.startsWith("Batch")&&!message.startsWith("Added to Cart")&& !message.startsWith("Journal") ) {
							fetchConfigVO.setErrormessage(message);
							seleniumFactory.getInstanceObj(instanceName).screenshotFail(driver, "", fetchMetadataVO,
									fetchConfigVO);
							throw new IllegalArgumentException("Erroe occured");
						}
						seleniumFactory.getInstanceObj(instanceName).screenshot(driver, "", fetchMetadataVO,
								fetchConfigVO);
						break;
						
					case "tableRowSelect":
						seleniumFactory.getInstanceObj(instanceName).tableRowSelect(driver, param1, param2,
								fetchMetadataVO, fetchConfigVO);
						break;
					case "clickButton Dropdown":
						if (fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
							seleniumFactory.getInstanceObj(instanceName).clickButtonDropdown(driver, param1, param2,
									fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
							https: // watshubd01.winfosolutions.com:4443/wats/wats_workspace_prod/taconfig/data/
							break;
						} else {
							break;
						}
					case "mousehover":
						seleniumFactory.getInstanceObj(instanceName).mousehover(driver, param1, param2, fetchMetadataVO,
								fetchConfigVO);
						break;
					case "scrollUsingElement":
						seleniumFactory.getInstanceObj(instanceName).scrollUsingElement(driver,
								fetchMetadataVO.getInput_parameter(), fetchMetadataVO, fetchConfigVO);
						break;
					case "moveToElement":
						seleniumFactory.getInstanceObj(instanceName).moveToElement(driver,
								fetchMetadataVO.getInput_parameter(), fetchMetadataVO, fetchConfigVO);
						break;
					case "switchToDefaultFrame":
						seleniumFactory.getInstanceObj(instanceName).switchToDefaultFrame(driver);
						break;
					case "switchToFrame":
						
						seleniumFactory.getInstanceObj(instanceName).switchToFrame(driver,
								fetchMetadataVO.getInput_parameter(), fetchMetadataVO, fetchConfigVO);
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
						seleniumFactory.getInstanceObj(instanceName).clickFilter(driver,
								fetchMetadataVO.getXpath_location(), fetchMetadataVO.getXpath_location1(),
								fetchMetadataVO, fetchConfigVO);
						break;
					case "selectByText":
						if (fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
							seleniumFactory.getInstanceObj(instanceName).selectByText(driver, param1, param2,
									fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
							break;
						} else {
							break;
						}
					case "copy":
						seleniumFactory.getInstanceObj(instanceName).copy(driver, fetchMetadataVO, fetchConfigVO);
						break;
					case "copynumber":
						globalValueForSteps = seleniumFactory.getInstanceObj(instanceName).copynumber(driver, param1,
								param2, fetchMetadataVO, fetchConfigVO);
						break;
					case "ebsCopyValue":
						globalValueForSteps = seleniumFactory.getInstanceObj(instanceName).copynumber(driver, param1,
								param2, fetchMetadataVO, fetchConfigVO);
						break;
					case "copyy":
						seleniumFactory.getInstanceObj(instanceName).copyy(driver, fetchMetadataVO.getXpath_location(),
								fetchMetadataVO, fetchConfigVO);
						break;
					case "copytext":
						seleniumFactory.getInstanceObj(instanceName).copytext(driver,
								fetchMetadataVO.getXpath_location(), fetchMetadataVO, fetchConfigVO);
						break;
					case "clear":
						seleniumFactory.getInstanceObj(instanceName).clear(driver, param1, param2, fetchMetadataVO,
								fetchConfigVO);
						break;
					case "enter":
						
						seleniumFactory.getInstanceObj(instanceName).enter(driver, fetchMetadataVO, fetchConfigVO);
						break;
						
					case "tab":
						
						seleniumFactory.getInstanceObj(instanceName).tab(driver, fetchMetadataVO, fetchConfigVO);
						break;
					case "ebsTabKey":
						
						seleniumFactory.getInstanceObj(instanceName).ebsTabKey(driver, fetchMetadataVO, fetchConfigVO);
						break;
						
					case "paste":
						seleniumFactory.getInstanceObj(instanceName).paste(driver, fetchMetadataVO.getInput_parameter(),
								fetchMetadataVO, fetchConfigVO, globalValueForSteps);
						break;
					case "ebsPasteValue":
						seleniumFactory.getInstanceObj(instanceName).paste(driver, fetchMetadataVO.getInput_parameter(),
								fetchMetadataVO, fetchConfigVO, globalValueForSteps);
						break;
						
					case "uploadFileAutoIT":
						seleniumFactory.getInstanceObj(instanceName).uploadFileAutoIT(fetchMetadataVO.getField_type(),
								fetchMetadataVO);
						break;
					case "windowclose":
						
						seleniumFactory.getInstanceObj(instanceName).windowclose(driver, fetchMetadataVO,
								fetchConfigVO);
						break;
					case "selectByIndex":
						seleniumFactory.getInstanceObj(instanceName).selectByIndex( driver,  param1,  param2, 
								 fetchMetadataVO,  fetchConfigVO);
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
						
					case "wait":
						
					case "navigateToBackPage":
						seleniumFactory.getInstanceObj(instanceName).navigateToBackPage(driver, fetchMetadataVO,
								fetchConfigVO);
						break;
					case "openPdf":
						seleniumFactory.getInstanceObj(instanceName).openPdf(driver, fetchMetadataVO.getInput_value(),
								fetchMetadataVO, fetchConfigVO);
						break;
					case "openFile":
						seleniumFactory.getInstanceObj(instanceName).openFile(driver, fetchMetadataVO, fetchConfigVO);
						break;
					case "actionApprove":
						seleniumFactory.getInstanceObj(instanceName).actionApprove(driver, param1, param2,
								fetchMetadataVO, fetchConfigVO);
						break;
					case "multipleSendKeys":
						seleniumFactory.getInstanceObj(instanceName).multipleSendKeys(driver, param1, param2, value1,
								value2, fetchMetadataVO, fetchConfigVO);
						break;
					case "selectRadioButton":
						seleniumFactory.getInstanceObj(instanceName).selectRadioButton(driver, param1,  fetchMetadataVO, fetchConfigVO);
						break;
					default:
						System.out.println("Action Name is not matched with" + "" + actionName);
						// screenshotException(driver, "Test Action Name Not Exists_",
						// fetchMetadataListVO, fetchConfigVO);
						break;
					}
					}
					i++;

					// MetaData Webservice
					if (fetchMetadataListVO.size() == i) {
						if(instanceName.equalsIgnoreCase("EBS")&&(!fetchMetadataListVO.get(0).getScenario_name().equalsIgnoreCase("Requisition Creation")&&(!fetchMetadataListVO.get(0).getScenario_name().equalsIgnoreCase("Receivables"))))
						{
							String Folder = (fetchConfigVO.getWINDOWS_PDF_LOCATION()  + fetchMetadataListVO.get(0).getCustomer_name() + "/"
									+ fetchMetadataListVO.get(0).getTest_run_name()+"/"+"robot" + "/");
							
						//	String Folder = "C:\\EBS-Automation\\EBS Automation-POC\\robot files\\";
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
						FileWriter writer;
						try {
							String robotFileName=seq_num+"_"+fetchMetadataListVO.get(0).getScript_number()+".robot";
							String completePath=Folder+robotFileName;
						//	writer = new FileWriter("C:\\Users\\Winfo solutions\\Priya\\Documents\\createInvoice.robot");
							writer = new FileWriter(completePath);
							
							for(String codeline:listOfRobotCodeLines)
					    	  {
					    		//String code=codeline.getRobot_line();  
								
					    		writer.write(codeline + System.lineSeparator());
					    	  }
						//	writer.write(robotCodeLine + System.lineSeparator());
					      writer.close();
					      String screenshotPath="C:\\\\\\\\EBS-Automation\\\\\\\\WATS_Files\\\\\\\\screenshot\\\\\\\\ebs\\\\\\\\" + fetchMetadataVO.getCustomer_name() + "\\\\\\\\"+ fetchMetadataVO.getTest_run_name();
					      robotFileTransfer( completePath, robotFileName);
					     String response= callLocalRobot(seq_num+"_"+fetchMetadataListVO.get(0).getScript_number(),screenshotPath);
							JSONObject jsonobj= new JSONObject(response);
						String jsonResponse="";
							for(Iterator iterator = jsonobj.keySet().iterator(); iterator.hasNext();) {
							    String key = (String) iterator.next();
							    System.out.println(jsonobj.get(key));
							  //  System.out.println();
							    if(!(key.equalsIgnoreCase("status")))
							    {
							    	String value=(String) jsonobj.get(key);
							    	seleniumFactory.getInstanceObj(instanceName).updateCopyValue(key,value,test_set_line_id,test_set_id);
							    }
							    if ((key.equalsIgnoreCase("status")))
							     {
							    	String value=(String) jsonobj.get(key);
							    	jsonResponse=value;
							    	 
							 
							     }
							
				if(jsonResponse.equalsIgnoreCase("pass"))
				{
					for(FetchMetadataVO fetchMetadataVO1 : fetchMetadataListVO)
					{
						dataBaseEntry.updatePassedScriptLineStatus(fetchMetadataVO1, fetchConfigVO, fetchMetadataVO1.getTest_script_param_id(),"Pass",null);

					}
					FetchScriptVO post = new FetchScriptVO();
					post.setP_test_set_id(test_set_id);
					post.setP_status("Pass");
					post.setP_script_id(script_id);
					post.setP_test_set_line_id(test_set_line_id);
					post.setP_pass_path(passurl);
					post.setP_fail_path(failurl);
					post.setP_exception_path(detailurl);
					post.setP_test_set_line_path(scripturl);
					// passcount = passcount+1;
					Date enddate = new Date();
					fetchConfigVO.setEndtime(enddate);
					try {
						dataService.updateTestCaseStatus(post, param, fetchConfigVO);
						dataBaseEntry.updateEndTime(fetchConfigVO, test_set_line_id, test_set_id, enddate);
					} catch (Exception e) {
						System.out.println("e");
					}
					seleniumFactory.getInstanceObj(instanceName).createPdf(fetchMetadataListVO, fetchConfigVO,
							seq_num + "_" + script_Number + ".pdf", startdate, enddate);
//					if ("OBJECT_STORE".equalsIgnoreCase(fetchConfigVO.getPDF_LOCATION())) {
//						seleniumFactory.getInstanceObj(fetchConfigVO.getInstance_name()).uploadPDF(fetchMetadataListVO,
//								fetchConfigVO);
//					}
					limitScriptExecutionService.insertTestRunScriptData(fetchConfigVO, fetchMetadataListVO,
							script_id1, script_Number, "pass", startdate, enddate);
//					limitScriptExecutionService.updateFaileScriptscount(test_set_line_id,
//							test_set_id);
				}
				if(jsonResponse.equalsIgnoreCase("fail"))
				{
					System.out.println("inside fail :");
				    List<String> fileNameList = new ArrayList<String>();
//				    File folder = new File(fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
//							+ fetchMetadataListVO.get(0).getTest_run_name() + "/");
			    File folder = new File(fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + fetchMetadataListVO.get(0).getCustomer_name() + "\\"
							+ fetchMetadataListVO.get(0).getTest_run_name() + "\\");

					File[] listOfFiles = folder.listFiles();
					List<File> allFileList = Arrays.asList(listOfFiles);
					List<File> fileList = new ArrayList<>();
					String seqNumber = fetchMetadataListVO.get(0).getSeq_num();
					// String seqNumber = "1";
					for (File file : allFileList) {
						if (file.getName().startsWith(seqNumber + "_")) {
							fileList.add(file);
						}
					}

					Collections.sort(fileList, new Comparator<File>() {

						public int compare(File f1, File f2) {

							return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified()) * -1;

						}

					});
					for(File f:fileList) {
					fileNameList.add(f.getName());
					}
//				    fileNameList = seleniumFactory.getInstanceObj(instanceName).getFileNameListNew(fetchMetadataListVO, fetchConfigVO);
		System.out.println("screenshot list first entry :");
					String name=fileNameList.get(0);
					System.out.println(name);
		String arr[]=name.split("_", 0);
	String lastScreenshotSeqNum=arr[1];
	int lastPassedSeq=Integer.parseInt (lastScreenshotSeqNum);
	System.out.println("line num:");
		System.out.println(lastScreenshotSeqNum);
	String lastSeqNum=fetchMetadataListVO.get(fetchMetadataListVO.size()-1).getSeq_num();
	System.out.println("fetch metadatavo last eleemnt line number:");
	System.out.println(lastSeqNum);
					for(FetchMetadataVO fetchMetadataVO11 : fetchMetadataListVO)
					{
						//int lastPassedSeq=Integer.parseInt (lastScreenshotSeqNum);
						int currentSeqNum =Integer.parseInt(fetchMetadataVO11.getLine_number());
						System.out.println("currentSeqNum: "+currentSeqNum);
						System.out.println("lastPassedSeq: "+lastPassedSeq);
						if(currentSeqNum<=lastPassedSeq)
						{
						dataBaseEntry.updatePassedScriptLineStatus(fetchMetadataVO11, fetchConfigVO, fetchMetadataVO11.getTest_script_param_id(),"Pass",null);
						}
						else
						{
							message="EBS Execution Failed";
							dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO11, fetchConfigVO, fetchMetadataVO11.getTest_script_param_id(), "Fail",
									message);
							fetchConfigVO.setErrormessage(message);
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
							
//							File source = new File(fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + fetchMetadataListVO.get(0).getCustomer_name() + "/Images/last.jpg");
					//	File dest = new File(fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
				//					+ fetchMetadataListVO.get(0).getTest_run_name() + "/"+fetchMetadataVO.getSeq_num() + "_"+ currentSeqNum + "_" + fetchMetadataVO.getScenario_name() + "_"+ fetchMetadataVO.getScript_number() + "_" + fetchMetadataVO.getTest_run_name() + "_"+ currentSeqNum + "_Failed.jpg");
//							try {
//							    FileUtils.copyFile(source, dest);
//							} catch (IOException e) {
//							    e.printStackTrace();
//							}
							File file = new ClassPathResource(whiteimage).getFile();
							// File file = new File("C:\\Users\\Winfo
							// Solutions\\Desktop\\Add_On\\white.jpg");
							File file1 = new ClassPathResource(watsvediologo).getFile();
							// File file1=new File("C:\\Users\\Winfo
							// Solutions\\Desktop\\Add_On\\WATS_LOGO.JPG");

							BufferedImage image = null;
							image = ImageIO.read(file);
							BufferedImage logo = null;
							logo = ImageIO.read(file1);
							BufferedImage image1 = null;
							image1 = ImageIO.read(file);
							Graphics g1 = image1.getGraphics();
							g1.setColor(Color.red);
							java.awt.Font font1 = new java.awt.Font("Calibir", java.awt.Font.PLAIN, 36);
							g1.setFont(font1);
							g1.drawString("FAILED IN THIS STEP!!", 400, 300);
							g1.drawImage(logo, 1012, 15, null);
							g1.dispose();

							//ImageIO.write(image1, "jpg",  new File(fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
								//	+ fetchMetadataListVO.get(0).getTest_run_name() + "/"+fetchMetadataVO.getSeq_num() + "_"+ currentSeqNum + "_" + fetchMetadataVO.getScenario_name() + "_"+ fetchMetadataVO.getScript_number() + "_" + fetchMetadataVO.getTest_run_name() + "_"+ currentSeqNum + "_Failed.jpg"));
					
							ImageIO.write(image1, "jpg",  new File(fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + fetchMetadataListVO.get(0).getCustomer_name() + "\\"
									+ fetchMetadataListVO.get(0).getTest_run_name() + "\\"+fetchMetadataVO.getSeq_num() + "_"+ currentSeqNum + "_" + fetchMetadataVO.getScenario_name() + "_"+ fetchMetadataVO.getScript_number() + "_" + fetchMetadataVO.getTest_run_name() + "_"+ currentSeqNum + "_Failed.jpg"));
							
							seleniumFactory.getInstanceObj(instanceName).createFailedPdf(fetchMetadataListVO, fetchConfigVO,
								seq_num + "_" + script_Number +".pdf", startdate, enddate);

								
									
							limitScriptExecutionService.insertTestRunScriptData(fetchConfigVO, fetchMetadataListVO, script_id1,
									script_Number, "Fail", startdate, enddate);
							break;
						}
						
					}
				}
//							    for(String name:fileNameList)
//						{
//							System.out.println(name);
//							String arr[]=name.split("_", 1);
//							String lineNum=arr[1];
//							System.out.println(lineNum);
//						String lastLineNumber=fetchMetadataListVO.get(fetchMetadataListVO.size()-1).getLine_number();
//						
//						}
						
							}
					       System.out.println(response);
					     
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						//	throw new Exception("Script failed");
						}
						
}
						else
						{
						FetchScriptVO post = new FetchScriptVO();
						post.setP_test_set_id(test_set_id);
						post.setP_status("Pass");
						post.setP_script_id(script_id);
						post.setP_test_set_line_id(test_set_line_id);
						post.setP_pass_path(passurl);
						post.setP_fail_path(failurl);
						post.setP_exception_path(detailurl);
						post.setP_test_set_line_path(scripturl);
						// passcount = passcount+1;
						Date enddate = new Date();
						fetchConfigVO.setEndtime(enddate);
						try {
							dataService.updateTestCaseStatus(post, param, fetchConfigVO);
							dataBaseEntry.updateEndTime(fetchConfigVO, test_set_line_id, test_set_id, enddate);
						} catch (Exception e) {
							System.out.println("e");
						}
						seleniumFactory.getInstanceObj(instanceName).createPdf(fetchMetadataListVO, fetchConfigVO,
								seq_num + "_" + script_Number + ".pdf", startdate, enddate);
						if ("OBJECT_STORE".equalsIgnoreCase(fetchConfigVO.getPDF_LOCATION())) {
							seleniumFactory.getInstanceObj(fetchConfigVO.getInstance_name()).uploadPDF(fetchMetadataListVO,
									fetchConfigVO);
						}
						limitScriptExecutionService.insertTestRunScriptData(fetchConfigVO, fetchMetadataListVO,
								script_id1, script_Number, "pass", startdate, enddate);
						limitScriptExecutionService.updateFaileScriptscount(test_set_line_id,
								test_set_id);
//						uploadPDF(fetchMetadataListVO, fetchConfigVO);
					}
					}
				//	System.out.println("Successfully Executed the" + "" + actionName);
					try {
						if(instanceName.equalsIgnoreCase("EBS")&&(!fetchMetadataListVO.get(0).getScenario_name().equalsIgnoreCase("Requisition Creation")&&(!fetchMetadataListVO.get(0).getScenario_name().equalsIgnoreCase("Receivables"))))
						{	}
						else
						{
							dataBaseEntry.updatePassedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id,"Pass",null);
						dataBaseEntry.updateFailedImages(fetchMetadataVO, fetchConfigVO, test_script_param_id);
						}
						
					} catch (Exception e) {
						System.out.println("e");
					}
				} catch (Exception e) {
					
					System.out.println("Failed to Execute the " + "" + actionName);
					System.out.println(
							"Error occurred in TestCaseName=" + actionName + "" + "Exception=" + "" + e.getMessage());
					errorMessagesHandler.getError(actionName,fetchMetadataVO, fetchConfigVO, test_script_param_id,message,param1,param2,dataBaseEntry.getPassword(param, userName, fetchConfigVO));


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
					int failedScriptRunCount = limitScriptExecutionService.getFailedScriptRunCount(test_set_line_id,
							test_set_id);					if(failedScriptRunCount==1) {
								seleniumFactory.getInstanceObj(instanceName).createFailedPdf(fetchMetadataListVO, fetchConfigVO,
										seq_num + "_" + script_Number +".pdf", startdate, enddate);

							}else if(failedScriptRunCount==2) {
								limitScriptExecutionService.renameFailedFile(fetchMetadataListVO, fetchConfigVO,
										seq_num + "_" + script_Number +".pdf",failedScriptRunCount);
								seleniumFactory.getInstanceObj(instanceName).createFailedPdf(fetchMetadataListVO, fetchConfigVO,
										seq_num + "_" + script_Number + "_RUN" + failedScriptRunCount + ".pdf", startdate, enddate);

							}else {
							seleniumFactory.getInstanceObj(instanceName).createFailedPdf(fetchMetadataListVO, fetchConfigVO,
									seq_num + "_" + script_Number + "_RUN" + failedScriptRunCount + ".pdf", startdate, enddate);
							}
							if ("OBJECT_STORE".equalsIgnoreCase(fetchConfigVO.getPDF_LOCATION())) {
								seleniumFactory.getInstanceObj(fetchConfigVO.getInstance_name()).uploadPDF(fetchMetadataListVO,
										fetchConfigVO);
							}
					// uploadPDF(fetchMetadataListVO, fetchConfigVO);
					limitScriptExecutionService.insertTestRunScriptData(fetchConfigVO, fetchMetadataListVO, script_id1,
							script_Number, "Fail", startdate, enddate);
					throw e;
				}
			}
		} catch (Exception e) {
			System.out.println(e);
//			screenshotException(driver, "Test Action Name Not Exists_", fetchMetadataListVO, fetchConfigVO);
			throw e;
		}
	}
	public String callLocalRobot(String fileName,String path)
	{
		HttpURLConnection conn = null;
    DataOutputStream os = null;
    String output="";
    String response="";
    try{
        URL url = new URL("http://192.168.1.203:8080/EBSAutomation/api/v3/localRobotJob"); //important to add the trailing slash after add
    //   URL url = new URL("http://localhost:8080/api/v3/localRobotJob"); //important to add the trailing slash after add

   //  String input1="{\"Data\":  \"";
    // String finalInput=input1+fileName+"\"}";
     //   String[] inputData = {"{\"Data\": [{ \"SUBJECT\": \"invoiceDesktop\"}]}"};
        String input1="{\"Data\": [{ \"SUBJECT\": \"";
        String finalInput=input1+fileName+"\",\"PATH\":\"";
        String finalInput2=finalInput+path+"\"}]}";
        String[] inputData= {finalInput2};
        for(String input: inputData){
            byte[] postData = input.getBytes(StandardCharsets.UTF_8);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
           // conn.setRequestProperty( "charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(input.length()));
            os = new DataOutputStream(conn.getOutputStream());
            os.write(postData);
            os.flush();

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

           System.out.println("response message");
//           System.out.println(conn.getResponseMessage());
//           System.out.println(conn.getResponseMessage());           
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
                response=response+output;

            }
            conn.disconnect();
        }
} catch (MalformedURLException e) {
    e.printStackTrace();
}catch (IOException e){
    e.printStackTrace();
}finally
    {
        if(conn != null)
        {
            conn.disconnect();
        }
    }
    System.out.println("returned Val");
    System.out.println(response);

    try {
		JSONObject jsonobj= new JSONObject(response);
		String val= (String) jsonobj.get("status");
		System.out.println("json Object Val "+val);
		
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
    return response; 
}
	public void robotFileTransfer(String filePath,String robotFileName)
	{
		 String server = "192.168.1.203";
	        int port = 21;
	        String user = "wats_ebs";
	        String pass = "2020@Winfo";

	        FTPClient ftpClient = new FTPClient();
	        try {

	            ftpClient.connect(server, port);
	            ftpClient.login(user, pass);
	            ftpClient.enterLocalPassiveMode();

	        
	            File firstLocalFile = new File(filePath);

	            String firstRemoteFile = robotFileName;
	            InputStream inputStream = new FileInputStream(firstLocalFile);

	            System.out.println("Start uploading first file");
	            boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
	            inputStream.close();
	            if (done) {
	                System.out.println("The first file is uploaded successfully.");
	            }
	        }catch (IOException ex) {
	            System.out.println("Error: " + ex.getMessage());
	            ex.printStackTrace();
	        } finally {
	            try {
	                if (ftpClient.isConnected()) {
	                    ftpClient.logout();
	                    ftpClient.disconnect();
	                }
	            } catch (IOException ex) {
	                ex.printStackTrace();
	            }
	        }
	}
	public void uploadScreenshotsToObjectStore(FetchConfigVO fetchConfigVO,List<FetchMetadataVO> fetchMetadataListVO)
	{
		String sourceFilePath;
		String destinationFilePath;
		
		 File sourceFolderDirectory = new File(fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + fetchMetadataListVO.get(0).getCustomer_name() + "\\"
					+ fetchMetadataListVO.get(0).getTest_run_name() + "\\");
		 
		 String sourceFolder = (fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + fetchMetadataListVO.get(0).getCustomer_name() + "\\"
					+ fetchMetadataListVO.get(0).getTest_run_name() + "\\");
		 
		 String destinationFolder = ("Screenshot/" + fetchMetadataListVO.get(0).getCustomer_name() + "/"
					+ fetchMetadataListVO.get(0).getTest_run_name() + "/");

			File[] listOfFiles = sourceFolderDirectory.listFiles();
			List<File> allFileList = Arrays.asList(listOfFiles);
			for(File f:allFileList)
			{
				String fileName=f.getName();
				sourceFilePath=sourceFolder+fileName;
				destinationFilePath=destinationFolder+fileName;
				
				
				seleniumFactory.getInstanceObj(fetchConfigVO.getInstance_name()).uploadObjectToObjectStore(sourceFilePath,destinationFilePath);
			}
	
	}
}