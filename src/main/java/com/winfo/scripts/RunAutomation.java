package com.winfo.scripts;

import java.io.IOException;
import com.winfo.interface1.SeleniumKeyWordsInterface;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.lowagie.text.DocumentException;
import com.winfo.Factory.SeleniumKeywordsFactory;
import com.winfo.config.DriverConfiguration;
import com.winfo.services.FetchConfigVO;
import com.winfo.services.FetchMetadataVO;
import com.winfo.services.FetchScriptVO;
import com.winfo.services.TestCaseDataService;
import com.winfo.scripts.DataBaseEntry;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;
import java.util.Date;

@Service
@RefreshScope
public class RunAutomation {
	@Autowired
	SeleniumKeywordsFactory seleniumFactory;

	@Autowired
	TestCaseDataService dataService;
	@Autowired
	DataBaseEntry dataBaseEntry;
	public String c_url = null;
	Logger log = Logger.getLogger("Logger");
	
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
		
//		createFailedPdf(fetchMetadataListVO, fetchConfigVO, "Passed_Report.pdf");
//
	//	createFailedPdf(fetchMetadataListVO, fetchConfigVO, "Failed_Report.pdf");
//		createFailedPdf(fetchMetadataListVO, fetchConfigVO, "14_OTC.AR.224.pdf");
//
		//createFailedPdf(fetchMetadataListVO, fetchConfigVO,fetchMetadataVO, "Detailed_Report.pdf");
//		createPdf(fetchMetadataListVO, fetchConfigVO, "Passed_Report.pdf",passcount,failcount,null);
//		createPdf(fetchMetadataListVO, fetchConfigVO, "1_10_Create Manual Invoice Transaction_OTC.AR.203.pdf",passcount,failcount);
//		createPdf(fetchMetadataListVO, fetchConfigVO,"Failed_Report.pdf",passcount,failcount);
//		createPdf(fetchMetadataListVO, fetchConfigVO, "55_OTC.AR.218.pdf",passcount,failcount);
		

	}
	long increment=0;
	public void run(String args) throws MalformedURLException {
		System.out.println(args);
		try {
			// Config Webservice
//			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");  
//	        LocalDateTime now = LocalDateTime.now();
//	        String start_time=dtf.format(now);
//			System.out.println("instanceName"+instanceName);
			FetchConfigVO fetchConfigVO = dataService.getFetchConfigVO(args);
			//FetchMetadataVO fetchMetadataVO = (FetchMetadataVO) dataService.getFetchMetaData(args, uri);
			final String uri = fetchConfigVO.getUri_test_scripts() + args;
			List<FetchMetadataVO> fetchMetadataListVO = dataService.getFetchMetaData(args, uri);
			System.out.println(fetchMetadataListVO.size());
			LinkedHashMap<String, List<FetchMetadataVO>> metaDataMap = dataService
					.prepareTestcasedata(fetchMetadataListVO);
			Date date = new Date();
			 fetchConfigVO.setStarttime1(date);
			System.out.println(metaDataMap.toString());
			ExecutorService executor = Executors.newFixedThreadPool(fetchConfigVO.getParallel_independent());
			for (Entry<String, List<FetchMetadataVO>> metaData : metaDataMap.entrySet()) {
				executor.execute(() -> {
					try {
						long starttimeIntermediate = System.currentTimeMillis();	
						String flag=dataBaseEntry.getTrMode(args,fetchConfigVO);
					              if(flag.equalsIgnoreCase("STOPPED")) {
										metaData.getValue().clear();
										executor.shutdown();
										System.out.println("treminattion is succeed");
									}else {
										executorMethod(args, fetchConfigVO, fetchMetadataListVO, metaData);
									}
									long i=System.currentTimeMillis()-starttimeIntermediate;
									increment=increment+i; 
									System.out.println("time"+increment/ 1000 % 60);					} catch (IOException | DocumentException | com.itextpdf.text.DocumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
			}
			executor.shutdown();
			try {
				executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
				if (executor.isTerminated()) {
					ExecutorService executordependent = Executors
							.newFixedThreadPool(fetchConfigVO.getParallel_dependent());
					LinkedHashMap<String, List<FetchMetadataVO>> dependantmetaDataMap = dataService
							.getDependentScriptMap();
					System.out.println(dependantmetaDataMap.toString());
					for (Entry<String, List<FetchMetadataVO>> metaData : dependantmetaDataMap.entrySet()) {
						executordependent.execute(() -> {
							try {
								String flag=dataBaseEntry.getTrMode(args,fetchConfigVO);
					              if(flag.equalsIgnoreCase("STOPPED")) {
										metaData.getValue().clear();
										executor.shutdown();
										System.out.println("treminattion is succeed");
									}else {
										executorMethod(args, fetchConfigVO, fetchMetadataListVO, metaData);
									}
							} catch (IOException | DocumentException | com.itextpdf.text.DocumentException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						});
					}
					executordependent.shutdown();
				}
				seleniumFactory.getInstanceObj(fetchConfigVO.getInstance_name()).createPdf(fetchMetadataListVO, fetchConfigVO, "Passed_Report.pdf", null,null);
				seleniumFactory.getInstanceObj(fetchConfigVO.getInstance_name()).createPdf(fetchMetadataListVO, fetchConfigVO, "Failed_Report.pdf", null,null);
				seleniumFactory.getInstanceObj(fetchConfigVO.getInstance_name()).createPdf(fetchMetadataListVO, fetchConfigVO, "Detailed_Report.pdf", null,null);
				increment=0;
//				uploadPDF(fetchMetadataListVO, fetchConfigVO);
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
	}

	public void executorMethod(String args, FetchConfigVO fetchConfigVO, List<FetchMetadataVO> fetchMetadataListVO,
			Entry<String, List<FetchMetadataVO>> metaData) throws Exception {
		List<String> failList = new ArrayList<String>();
		WebDriver driver = null;
		ConnectToSQL sql = null;
//		//String start_time=null;
//		String end_time=null;
//		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");  
//        LocalDateTime now = LocalDateTime.now();
		
        String test_set_id = fetchMetadataListVO.get(0).getTest_set_id();
		String script_id = fetchMetadataListVO.get(0).getScript_id();
		String test_set_line_id = fetchMetadataListVO.get(0).getTest_set_line_id();
		//start_time=dtf.format(now);
		
		String passurl = fetchConfigVO.getImg_url() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
				+ fetchMetadataListVO.get(0).getTest_run_name() + "/" + "Passed_Report.pdf";
		String failurl = fetchConfigVO.getImg_url() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
				+ fetchMetadataListVO.get(0).getTest_run_name() + "/" + "Failed_Report.pdf";
		String detailurl = fetchConfigVO.getImg_url() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
				+ fetchMetadataListVO.get(0).getTest_run_name() + "/" + "Detailed_Report.pdf";
		String scripturl = fetchConfigVO.getImg_url() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
				+ fetchMetadataListVO.get(0).getTest_run_name() + "/" + fetchMetadataListVO.get(0).getSeq_num() + "_"
				+ fetchMetadataListVO.get(0).getScript_number() + ".pdf";
		System.out.println(passurl);
		System.out.println(failurl);
		System.out.println(detailurl);
		boolean isDriverError = true;
		try {
			
			driver = DriverConfiguration.getWebDriver(fetchConfigVO);
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
			driver.quit();
		}
	}

	int passcount = 0;
	int failcount = 0;

	public void switchActions(String param, WebDriver driver, List<FetchMetadataVO> fetchMetadataListVO,
			FetchConfigVO fetchConfigVO) throws Exception {
		
		String log4jConfPath="log4j.properties";
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
		String script_Number = null;
		String line_number = null;
		String seq_num = null;
		String step_description=null;
		String test_script_param_id=null;

		//String start_time=null;
		//String end_time=null;

		try {
			script_id = fetchMetadataListVO.get(0).getScript_id();
			passurl = fetchConfigVO.getImg_url() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
					+ fetchMetadataListVO.get(0).getTest_run_name() + "/" + "Passed_Report.pdf";
			failurl = fetchConfigVO.getImg_url() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
					+ fetchMetadataListVO.get(0).getTest_run_name() + "/" + "Failed_Report.pdf";
			detailurl = fetchConfigVO.getImg_url() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
					+ fetchMetadataListVO.get(0).getTest_run_name() + "/" + "Detailed_Report.pdf";
			scripturl = fetchConfigVO.getImg_url() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
					+ fetchMetadataListVO.get(0).getTest_run_name() + "/" + fetchMetadataListVO.get(0).getSeq_num()
					+ "_" + fetchMetadataListVO.get(0).getScript_number() + ".pdf";
			
			String userName = null;
			ConnectToSQL dataSource = null;
			String globalValueForSteps = null;
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");  
			Date startdate = new Date();
			 fetchConfigVO.setStarttime(startdate);
			 String instanceName=fetchConfigVO.getInstance_name();
			 seleniumFactory.getInstanceObj(instanceName).DelatedScreenshoots(fetchMetadataListVO,fetchConfigVO);

			for (FetchMetadataVO fetchMetadataVO : fetchMetadataListVO) {
				String url = fetchConfigVO.getApplication_url();
				actionName = fetchMetadataVO.getAction();
				test_set_id = fetchMetadataVO.getTest_set_id();
				test_set_line_id = fetchMetadataVO.getTest_set_line_id();
				script_Number = fetchMetadataVO.getScript_number();
				line_number = fetchMetadataVO.getLine_number();
				seq_num = fetchMetadataVO.getSeq_num();
				dataBaseEntry.updateInProgressScriptStatus(fetchConfigVO,test_set_id,test_set_line_id);
				dataBaseEntry.updateStartTime(fetchConfigVO,test_set_line_id,test_set_id,startdate);
				step_description=fetchMetadataVO.getStep_description();
				String screenParameter = fetchMetadataVO.getInput_parameter();
				test_script_param_id=fetchMetadataVO.getTest_script_param_id();
				dataBaseEntry.updateInProgressScriptLineStatus(fetchMetadataVO,fetchConfigVO,test_script_param_id,"In-Progress");
				String param1 = null;
				String param2 = null;
				String param3 = null;
				String type1 = null;
				String type2 = null;
				String type3 = null;
				String message=null;
				String value1=null;
				String value2=null;
				int count=0;
				if (screenParameter != null) {
					param1 = screenParameter.split(">").length > 0 ? screenParameter.split(">")[0] : "";
					param2 = screenParameter.split(">").length > 1 ? screenParameter.split(">")[1] : "";
					param3 = screenParameter.split(">").length > 2 ? screenParameter.split(">")[2] : "";
					String actionType = fetchMetadataVO.getField_type();
					type1 = actionType != null ? actionType.split(">").length > 0 ? actionType.split(">")[0] : "" : "";
					type2 = actionType != null ? actionType.split(">").length > 1 ? actionType.split(">")[1] : "" : "";
					type3 = actionType != null ? actionType.split(">").length > 2 ? actionType.split(">")[2] : "" : "";
					String getValue = fetchMetadataVO.getInput_value();
	                value1 = getValue!=null ? getValue.split(">").length > 0 ? getValue.split(">")[0] : "" : "";
	                value2 = getValue!=null ? getValue.split(">").length > 1 ? getValue.split(">")[1] : "" : "";
				
				}
				try {
					switch (actionName) {

					case "Login into Application":
						userName = fetchMetadataVO.getInput_value();
						if (dataSource == null)
							dataSource = new ConnectToSQL();
						log.info("Navigating to Login into Application Action");
						if(fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
							seleniumFactory.getInstanceObj(instanceName).loginApplication(driver, fetchConfigVO, fetchMetadataVO, type1, type2, type3, param1, param2,
								param3, fetchMetadataVO.getInput_value(),
								dataSource.getPassword(param, userName, fetchConfigVO));
						userName = null;
						break;
						}
						else {
							break;
						}

					case "Navigate":
						log.info("Navigating to Navigate Action");
						seleniumFactory.getInstanceObj(instanceName).navigate(driver, fetchConfigVO, fetchMetadataVO, type1, type2, param1, param2,count);
						break;

					case "openTask":
						log.info("Navigating to openTask Action");
						seleniumFactory.getInstanceObj(instanceName).openTask(driver, fetchConfigVO, fetchMetadataVO, type1, type2, param1, param2,count);
						break;

					case "Logout":
						seleniumFactory.getInstanceObj(instanceName).logout(driver, fetchConfigVO, fetchMetadataVO, type1, type2, type3, param1, param2, param3);
						break;
					case "SendKeys":
						if(fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
							seleniumFactory.getInstanceObj(instanceName).sendValue(driver, param1, param2, fetchMetadataVO.getInput_value(), fetchMetadataVO,
								fetchConfigVO);
						break;
						}
						else {
							break;
						}
					case "textarea":
						if(fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") 
						{
							seleniumFactory.getInstanceObj(instanceName).textarea(driver, param1, param2, fetchMetadataVO.getInput_value(), fetchMetadataVO,
								fetchConfigVO);
						break;
						}
						else {
							break;
						}
					case "Dropdown Values":
						if(fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
							seleniumFactory.getInstanceObj(instanceName).dropdownValues(driver, param1, param2, param3, fetchMetadataVO.getInput_value(),
								fetchMetadataVO, fetchConfigVO);
						break;
						}
						else {
							break;
						}
					case "Table SendKeys":
						if(fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
							seleniumFactory.getInstanceObj(instanceName).tableSendKeys(driver, param1, param2, param3, fetchMetadataVO.getInput_value(), fetchMetadataVO,
								fetchConfigVO);
						break;
						}
						else {
							break;
						}
					case "multiplelinestableSendKeys":
						if(fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
							seleniumFactory.getInstanceObj(instanceName).multiplelinestableSendKeys(driver, param1, param2, param3, fetchMetadataVO.getInput_value(), fetchMetadataVO,
								fetchConfigVO);
						break;
						}
						else {
							break;
						}
						
					case "Table Dropdown Values":
						if(fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
							seleniumFactory.getInstanceObj(instanceName).tableDropdownValues(driver, param1, param2, fetchMetadataVO.getInput_value(), fetchMetadataVO,
								fetchConfigVO);
						break;
						}
						else {
							break;
						}
					case "clickLinkAction":
						if(fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
							seleniumFactory.getInstanceObj(instanceName).clickLinkAction(driver, param1, param2, fetchMetadataVO.getInput_value(), fetchMetadataVO,
								fetchConfigVO);
						break;
						}
						else {
							break;
						}
					case "clickCheckbox":
						if(fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
							seleniumFactory.getInstanceObj(instanceName).clickCheckbox(driver, param1, fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
						break;
						}
						else {
							break;
						}
					case "clickRadiobutton":
						if(fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
							seleniumFactory.getInstanceObj(instanceName).clickRadiobutton(driver, param1, param2, fetchMetadataVO.getInput_value(), fetchMetadataVO,
								fetchConfigVO);
						break;
						}
						else {
							break;
						}
					case "selectAValue":
						if(fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
							seleniumFactory.getInstanceObj(instanceName).selectAValue(driver, param1, param2, fetchMetadataVO.getInput_value(), fetchMetadataVO,
								fetchConfigVO);
						break;
						}
						else {
							break;
						}
					case "clickTableLink":
						seleniumFactory.getInstanceObj(instanceName).clickTableLink(driver, param1, param2, fetchMetadataVO, fetchConfigVO);
						break;
					case "clickLink":
						seleniumFactory.getInstanceObj(instanceName).clickLink(driver, param1, param2, fetchMetadataVO, fetchConfigVO);
						break;
					case "clickNotificationLink":
						seleniumFactory.getInstanceObj(instanceName).clickNotificationLink(driver, param1, fetchMetadataVO, fetchConfigVO);
						break;
					case "clickMenu":
						seleniumFactory.getInstanceObj(instanceName).clickMenu(driver, param1, param2, fetchMetadataVO, fetchConfigVO);
						break;
					case "clickImage":
						seleniumFactory.getInstanceObj(instanceName).clickImage(driver, param1, param2, fetchMetadataVO, fetchConfigVO);
						break;
					case "clickTableImage":
						seleniumFactory.getInstanceObj(instanceName).clickTableImage(driver, param1, param2, fetchMetadataVO.getInput_value(), fetchMetadataVO,
								fetchConfigVO);
						break;
					case "clickExpandorcollapse":
						seleniumFactory.getInstanceObj(instanceName).clickExpandorcollapse(driver, param1, param2, fetchMetadataVO, fetchConfigVO);
						break;
					case "clickButton":		  
						seleniumFactory.getInstanceObj(instanceName).clickButton(driver, param1, param2, fetchMetadataVO, fetchConfigVO);
						    message=seleniumFactory.getInstanceObj(instanceName).getErrorMessages(driver);
						    String message1=seleniumFactory.getInstanceObj(instanceName).getErrorMessages(driver);
	                     if(message != null&& !message.startsWith("Example")) {
	                           fetchConfigVO.setErrormessage(message);
	                           seleniumFactory.getInstanceObj(instanceName).screenshotFail(driver, "", fetchMetadataVO, fetchConfigVO);
	                          throw new IllegalArgumentException("Erroe occured");
	                        }
	                     seleniumFactory.getInstanceObj(instanceName).screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
						break;
					case "tableRowSelect":
						seleniumFactory.getInstanceObj(instanceName).tableRowSelect(driver, param1, param2, fetchMetadataVO, fetchConfigVO);
						break;
					case "clickButton Dropdown":
						if(fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
							seleniumFactory.getInstanceObj(instanceName).clickButtonDropdown(driver, param1, param2, fetchMetadataVO.getInput_value(), fetchMetadataVO,
								fetchConfigVO);https://watshubd01.winfosolutions.com:4443/wats/wats_workspace_prod/taconfig/data/
						break;
						}
						else {
							break;
						}
					case "mousehover":
						seleniumFactory.getInstanceObj(instanceName).mousehover(driver, param1, param2, fetchMetadataVO, fetchConfigVO);
						break;
					case "scrollUsingElement":
						seleniumFactory.getInstanceObj(instanceName).scrollUsingElement(driver, fetchMetadataVO.getInput_parameter(), fetchMetadataVO,
								fetchConfigVO);
						break;
					case "moveToElement":
						seleniumFactory.getInstanceObj(instanceName).moveToElement(driver, fetchMetadataVO.getInput_parameter(), fetchMetadataVO, fetchConfigVO);
						break;
					case "switchToDefaultFrame":
						seleniumFactory.getInstanceObj(instanceName).switchToDefaultFrame(driver);
						break;
					case "switchToFrame":
						seleniumFactory.getInstanceObj(instanceName).switchToFrame(driver, fetchMetadataVO.getInput_parameter(), fetchMetadataVO, fetchConfigVO);
						break;
					case "windowhandle":
						seleniumFactory.getInstanceObj(instanceName).windowhandle(driver, fetchMetadataVO, fetchConfigVO);
						break;
					case "dragAnddrop":
						seleniumFactory.getInstanceObj(instanceName).dragAnddrop(driver, fetchMetadataVO.getXpath_location(), fetchMetadataVO.getXpath_location1(),
								fetchMetadataVO, fetchConfigVO);
						break;
					case "clickFilter":
						seleniumFactory.getInstanceObj(instanceName).clickFilter(driver, fetchMetadataVO.getXpath_location(), fetchMetadataVO.getXpath_location1(),
								fetchMetadataVO, fetchConfigVO);
						break;
					case "selectByText":
						if(fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
							seleniumFactory.getInstanceObj(instanceName).selectByText(driver, param1, param2, fetchMetadataVO.getInput_value(), fetchMetadataVO,
								fetchConfigVO);
						break;
						}
						else {
							break;
						}
					case "copy":
						seleniumFactory.getInstanceObj(instanceName).copy(driver, fetchMetadataVO, fetchConfigVO);
						break;
					case "copynumber":
						globalValueForSteps = seleniumFactory.getInstanceObj(instanceName).copynumber(driver, param1, param2, fetchMetadataVO, fetchConfigVO);
						break;
					case "copyy":
						seleniumFactory.getInstanceObj(instanceName).copyy(driver, fetchMetadataVO.getXpath_location(), fetchMetadataVO, fetchConfigVO);
						break;
					case "copytext":
						seleniumFactory.getInstanceObj(instanceName).copytext(driver, fetchMetadataVO.getXpath_location(), fetchMetadataVO, fetchConfigVO);
						break;
					case "clear":
						seleniumFactory.getInstanceObj(instanceName).clear(driver, param1, param2, fetchMetadataVO, fetchConfigVO);
						break;
					case "enter":
						seleniumFactory.getInstanceObj(instanceName).enter(driver, fetchMetadataVO, fetchConfigVO);
						break;
					case "tab":
						seleniumFactory.getInstanceObj(instanceName).tab(driver, fetchMetadataVO, fetchConfigVO);
						break;
					case "paste":
						seleniumFactory.getInstanceObj(instanceName).paste(driver, fetchMetadataVO.getInput_parameter(), fetchMetadataVO, fetchConfigVO,
								globalValueForSteps);
						break;
					case "uploadFileAutoIT":
						seleniumFactory.getInstanceObj(instanceName).uploadFileAutoIT(fetchMetadataVO.getField_type(), fetchMetadataVO);
						break;
					case "windowclose":
						seleniumFactory.getInstanceObj(instanceName).windowclose(driver, fetchMetadataVO, fetchConfigVO);
						break;
					case "switchDefaultContent":
						seleniumFactory.getInstanceObj(instanceName).switchDefaultContent(driver, fetchMetadataVO, fetchConfigVO);
						break;
					case "switchParentWindow":
						seleniumFactory.getInstanceObj(instanceName).switchToParentWindow(driver, fetchMetadataVO, fetchConfigVO);
						break;
					case "switchToParentWindow":
						seleniumFactory.getInstanceObj(instanceName).switchToParentWindow(driver, fetchMetadataVO, fetchConfigVO);
						break;
					case "DatePicker":
						if(fetchMetadataVO.getInput_value() != null || fetchMetadataVO.getInput_value() == "") {
							seleniumFactory.getInstanceObj(instanceName).datePicker(driver, param1, param2, fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
						break;
						}
						else {
							break;
						}
					case "refreshPage":
						seleniumFactory.getInstanceObj(instanceName).refreshPage(driver, fetchMetadataVO, fetchConfigVO);
						break;
					case "navigateToBackPage":
						seleniumFactory.getInstanceObj(instanceName).navigateToBackPage(driver, fetchMetadataVO, fetchConfigVO);
						break;
					case "openPdf":
						seleniumFactory.getInstanceObj(instanceName).openPdf(driver, fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
						break;
					case "openFile":
						seleniumFactory.getInstanceObj(instanceName).openFile(driver, fetchMetadataVO, fetchConfigVO);
						break;
					case "actionApprove":
						seleniumFactory.getInstanceObj(instanceName).actionApprove(driver, param1, param2, fetchMetadataVO, fetchConfigVO);
						break;
					case "multipleSendKeys" :
						seleniumFactory.getInstanceObj(instanceName).multipleSendKeys(driver, param1, param2, value1, value2,fetchMetadataVO, fetchConfigVO);
						break;
					default:
						System.out.println("Action Name is not matched with" + "" + actionName);
						// screenshotException(driver, "Test Action Name Not Exists_",
						// fetchMetadataListVO, fetchConfigVO);
						break;
					}
					i++;
					
					// MetaData Webservice
					if (fetchMetadataListVO.size() == i) {
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
						 seleniumFactory.getInstanceObj(instanceName).createPdf(fetchMetadataListVO, fetchConfigVO, seq_num + "_" + script_Number + ".pdf",startdate,enddate);
						try {
						dataService.updateTestCaseStatus(post, param, fetchConfigVO);
						 dataBaseEntry.updateEndTime(fetchConfigVO,test_set_line_id,test_set_id,enddate);
						}catch (Exception e) {
                        System.out.println("e");				
                        }
//						uploadPDF(fetchMetadataListVO, fetchConfigVO);
					}
					System.out.println("Successfully Executed the" + "" + actionName);
					try {
					dataBaseEntry.updatePassedScriptLineStatus(fetchMetadataVO,fetchConfigVO,test_script_param_id,"Pass");
					}catch (Exception e) {
                        System.out.println("e");				
                        }
					} catch (Exception e) {
					System.out.println("Failed to Execute the " + "" + actionName);
					System.out.println(
							"Error occurred in TestCaseName=" + actionName + "" + "Exception=" + "" + e.getMessage());
					 String error_message="Took more than 10 seconds to load the page";
						if(actionName.equalsIgnoreCase("clickButton") && message != null) {
	                        //String error_message="Took more than 10 seconds to load the page";
	                        dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO,fetchConfigVO,test_script_param_id,"Fail",message);
	                        }
						else if (actionName.equalsIgnoreCase("SendKeys")||actionName.equalsIgnoreCase("textarea")||actionName.equalsIgnoreCase("Table SendKeys")||
								actionName.equalsIgnoreCase("multiplelinestableSendKeys")) {
							error_message="Failed at "+actionName+" => Not able to enter the value in "+param1+" and "+param2;
							 fetchConfigVO.setErrormessage(error_message);
							dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO,fetchConfigVO,test_script_param_id,"Fail",error_message);
						}
						else if (actionName.equalsIgnoreCase("Dropdown Values")||actionName.equalsIgnoreCase("Table Dropdown Values")||actionName.equalsIgnoreCase("selectByText")) {
							error_message="Failed at "+actionName+" =>Not able to Select the Value from "+param2+" dropdownlist";
							 fetchConfigVO.setErrormessage(error_message);
							dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO,fetchConfigVO,test_script_param_id,"Fail",error_message);
						}
	                    else
	                    {
	                        dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO,fetchConfigVO,test_script_param_id,"Fail",error_message);
	                        //new changes-error_message added to else block
							fetchConfigVO.setErrormessage(error_message);
	                    }

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
					 dataBaseEntry.updateEndTime(fetchConfigVO,test_set_line_id,test_set_id,enddate);
					 seleniumFactory.getInstanceObj(instanceName).createFailedPdf(fetchMetadataListVO, fetchConfigVO, seq_num + "_" + script_Number + ".pdf",startdate,enddate);
					dataService.updateTestCaseStatus(post, param, fetchConfigVO);
	//				uploadPDF(fetchMetadataListVO, fetchConfigVO);
					
					
					throw e;
				}
			}
		} catch (Exception e) {
			System.out.println(e);
//			screenshotException(driver, "Test Action Name Not Exists_", fetchMetadataListVO, fetchConfigVO);
			throw e;
		}
	}
}
