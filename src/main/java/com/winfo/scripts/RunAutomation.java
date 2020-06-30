package com.winfo.scripts;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testng.annotations.Test;

import com.lowagie.text.DocumentException;
import com.winfo.config.DriverConfiguration;
import com.winfo.services.FetchConfigVO;
import com.winfo.services.FetchMetadataVO;
import com.winfo.services.FetchScriptVO;
import com.winfo.services.TestCaseDataService;

@Service
public class RunAutomation extends SeleniumKeyWords 
{
	private static final Logger logger = Logger.getLogger(RunAutomation.class);
	
	@Autowired
	TestCaseDataService dataService;
	public String c_url = null;
	TestCaseDataService DataService = null;
	public void run(String args) throws MalformedURLException 
	{
		DataService = new TestCaseDataService();
		System.out.println(args);
		try 
		{
			// Config Webservice
			FetchConfigVO fetchConfigVO = DataService.getFetchConfigVO(args);
			final String uri =  fetchConfigVO.getUri_test_scripts()+ args;
			List<FetchMetadataVO> fetchMetadataListVO = DataService.getFetchMetaData(args, uri);
//			createPdf(fetchMetadataListVO, fetchConfigVO, "Passed_Report.pdf");
//			createLowPdf(fetchMetadataListVO, fetchConfigVO, "Passed_Report.pdf");
			System.out.println(fetchMetadataListVO.size());
			LinkedHashMap<String, List<FetchMetadataVO>> metaDataMap = DataService.prepareTestcasedata(fetchMetadataListVO);
			System.out.println(metaDataMap.toString());
			ExecutorService executor = Executors.newFixedThreadPool(fetchConfigVO.getParallel_independent());
			for (Entry<String, List<FetchMetadataVO>> metaData : metaDataMap.entrySet()) 
			{
				executor.execute(() -> {
					try {
						executorMethod(args, fetchConfigVO, fetchMetadataListVO, metaData);
					} catch (IOException | DocumentException | com.itextpdf.text.DocumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
			}executor.shutdown();
			try 
			{  
				executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
					if(executor.isTerminated()) 
					{
						ExecutorService executordependent = Executors.newFixedThreadPool(fetchConfigVO.getParallel_dependent());
						LinkedHashMap<String, List<FetchMetadataVO>> dependantmetaDataMap = DataService.getDependentScriptMap();
						System.out.println(dependantmetaDataMap.toString());
						for (Entry<String, List<FetchMetadataVO>> metaData : dependantmetaDataMap.entrySet()) 
						{
							executordependent.execute(() -> {
								try {
									executorMethod(args, fetchConfigVO, fetchMetadataListVO, metaData);
								} catch (IOException | DocumentException | com.itextpdf.text.DocumentException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							});
						}executordependent.shutdown();
					}
		//			uploadImage(fetchMetadataListVO, fetchConfigVO);
					createPdf(fetchMetadataListVO, fetchConfigVO, "Passed_Report.pdf");
					createPdf(fetchMetadataListVO, fetchConfigVO, "Failed_Report.pdf");
					createPdf(fetchMetadataListVO, fetchConfigVO, "Detailed_Report.pdf");
					uploadPDF(fetchMetadataListVO, fetchConfigVO);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			System.out.println("Error in Block 1");
		}
	}

	public void executorMethod(String args, FetchConfigVO fetchConfigVO,
	List<FetchMetadataVO> fetchMetadataListVO, Entry<String, List<FetchMetadataVO>> metaData) throws Exception {
		List<String> failList = new ArrayList<String>();
		WebDriver driver = null;
		TestCaseDataService	DataService = null;
		ConnectToSQL sql = null;
		String test_set_id = fetchMetadataListVO.get(0).getTest_set_id();
		String script_id = fetchMetadataListVO.get(0).getScript_id();
		String test_set_line_id = fetchMetadataListVO.get(0).getTest_set_line_id();
		String passurl = fetchConfigVO.getImg_url() + fetchMetadataListVO.get(0).getCustomer_name() +"/" + fetchMetadataListVO.get(0).getTest_run_name()+"/" +"Passed_Report.pdf";
		String failurl = fetchConfigVO.getImg_url() + fetchMetadataListVO.get(0).getCustomer_name()+"/" + fetchMetadataListVO.get(0).getTest_run_name()+"/" +"Failed_Report.pdf";
		String detailurl = fetchConfigVO.getImg_url() + fetchMetadataListVO.get(0).getCustomer_name()+"/" + fetchMetadataListVO.get(0).getTest_run_name()+"/" +"Detailed_Report.pdf";
		String scripturl = fetchConfigVO.getImg_url() + fetchMetadataListVO.get(0).getCustomer_name() +"/" + fetchMetadataListVO.get(0).getTest_run_name()+"/" +fetchMetadataListVO.get(0).getScript_number() + ".pdf";
		System.out.println(passurl);
		System.out.println(failurl);
		System.out.println(detailurl);
		boolean isDriverError = true;
		try {
			DataService = new TestCaseDataService();
			driver = DriverConfiguration.getWebDriver(fetchConfigVO);
			isDriverError = false;
			List<FetchMetadataVO> fetchMetadataListsVO = metaData.getValue();
			switchActions(args, driver, fetchMetadataListsVO, fetchConfigVO, DataService);
			
		} catch (Exception e) {
	//		screenshotException(driver, "Test Action Name Not Exists_", fetchMetadataListVO, fetchConfigVO);
			createPdf(fetchMetadataListVO, fetchConfigVO, "Passed_Report.pdf");
			createPdf(fetchMetadataListVO, fetchConfigVO, "Failed_Report.pdf");
			createPdf(fetchMetadataListVO, fetchConfigVO, "Detailed_Report.pdf");
//			uploadPDF(fetchMetadataListVO, fetchConfigVO);
			if(isDriverError) 
			{
				FetchScriptVO post = new FetchScriptVO();
				post.setP_test_set_id(test_set_id);
				post.setP_status("Fail");
				post.setP_script_id(script_id);
				post.setP_test_set_line_id(test_set_line_id);
				post.setP_pass_path(passurl);
				post.setP_fail_path(failurl);
				post.setP_exception_path(detailurl);
				post.setP_test_set_line_path(scripturl);
				DataService.updateTestCaseStatus(post, args, fetchConfigVO);
				failList.add(script_id);
		//		uploadImage(fetchMetadataListVO, fetchConfigVO);
				createPdf(fetchMetadataListVO, fetchConfigVO, "Passed_Report.pdf");
				createPdf(fetchMetadataListVO, fetchConfigVO, "Failed_Report.pdf");
				createPdf(fetchMetadataListVO, fetchConfigVO, "Detailed_Report.pdf");
//				uploadPDF(fetchMetadataListVO, fetchConfigVO);
			}else {
				throw e;
			}
		} finally 
		{
			System.out.println("Execution is completed with" + "" + fetchMetadataListVO.get(0).getScript_id());
			driver.quit();
		}
	}

	public void switchActions(String param, WebDriver driver,
			List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO, TestCaseDataService DataService)
			throws Exception {
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
		try {
			script_id = fetchMetadataListVO.get(0).getScript_id();
			passurl = fetchConfigVO.getImg_url() + fetchMetadataListVO.get(0).getCustomer_name() +"/" + fetchMetadataListVO.get(0).getTest_run_name()+"/" +"Passed_Report.pdf";
			failurl = fetchConfigVO.getImg_url() + fetchMetadataListVO.get(0).getCustomer_name()+"/" + fetchMetadataListVO.get(0).getTest_run_name()+"/" +"Failed_Report.pdf";
			detailurl = fetchConfigVO.getImg_url() + fetchMetadataListVO.get(0).getCustomer_name()+"/" + fetchMetadataListVO.get(0).getTest_run_name()+"/" +"Detailed_Report.pdf";
			scripturl = fetchConfigVO.getImg_url() + fetchMetadataListVO.get(0).getCustomer_name() +"/" + fetchMetadataListVO.get(0).getTest_run_name()+"/" +fetchMetadataListVO.get(0).getScript_number() + ".pdf";
			String userName = null;
			ConnectToSQL dataSource = null;
		for (FetchMetadataVO fetchMetadataVO : fetchMetadataListVO) {
			String url = fetchConfigVO.getApplication_url();
			actionName = fetchMetadataVO.getAction();
			test_set_id = fetchMetadataVO.getTest_set_id();
			test_set_line_id = fetchMetadataVO.getTest_set_line_id();
			script_Number = fetchMetadataVO.getScript_number();
			String screenParameter = fetchMetadataVO.getInput_parameter(); 
			String param1 = null;  
			String param2 = null; 
			String param3 = null;
			String type1 = null; 
			String type2 = null; 
			String type3 = null; 
			if(screenParameter!=null) {
				param1 = screenParameter.split(">").length > 0 ?  screenParameter.split(">")[0] : "";  
				param2 = screenParameter.split(">").length > 1 ?  screenParameter.split(">")[1] : ""; 
				param3 = screenParameter.split(">").length > 2 ? screenParameter.split(">")[2] : "";
				String actionType = fetchMetadataVO.getField_type();
				type1 = actionType!=null ? actionType.split(">").length > 0 ? actionType.split(">")[0] : "" : ""; 
				type2 = actionType!=null ? actionType.split(">").length > 1 ? actionType.split(">")[1] : "" : ""; 
				type3 = actionType!=null ? actionType.split(">").length > 2 ? actionType.split(">")[2] : "" : ""; 
					}
				try {
					switch (actionName) {
					
					case "Login into Application": 
						userName = fetchMetadataVO.getInput_value();
						if(dataSource == null) dataSource = new ConnectToSQL();
						loginApplication(driver, fetchConfigVO, fetchMetadataVO, type1, type2, type3,param1, param2, param3, fetchMetadataVO.getInput_value(),dataSource.getPassword(param, userName, fetchConfigVO)); 
						userName = null; 
						break;
						
					case "Navigate": 
						navigate(driver, fetchConfigVO, fetchMetadataVO, type1, type2, param1, param2);
						break;
						
					case "openTask": 
						openTask(driver, fetchConfigVO, fetchMetadataVO, type1, type2, param1, param2);
						break;
						
					case "Logout": 
						logout(driver, fetchConfigVO, fetchMetadataVO, type1, type2, type3, param1, param2, param3);
						break;
					case "SendKeys":
						sendValue(driver, param1, param2, fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
						break;
					case "textarea":
						textarea(driver, param1, param2, fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
						break;
					case "Dropdown Values" :
						dropdownValues(driver, param1, param2,param3, fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
						break;
					case "Table SendKeys" :
						tableSendKeys(driver, param1, param2,param3, fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
						break;
					case "Table Dropdown Values" :
						tableDropdownValues(driver, param1, param2, fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
						break;
					case "clickLinkAction" :
						clickLinkAction(driver, param1,param2, fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
						break;
					case "clickCheckbox" :
						clickCheckbox(driver, param1, fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
						break;
					case "clickRadiobutton" :
						clickRadiobutton(driver, param1, param2, fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
						break;
					case "selectAValue" :
						selectAValue(driver, param1, param2, fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
						break;
					case "clickTableLink":
						clickTableLink(driver, param1, param2, fetchMetadataVO, fetchConfigVO);
						break;
					case "clickLink":
						clickLink(driver, param1, param2, fetchMetadataVO, fetchConfigVO);
						break;
					case "clickMenu":
						clickMenu(driver, param1, param2, fetchMetadataVO, fetchConfigVO);
						break;
					case "clickImage":
						clickImage(driver, param1, param2, fetchMetadataVO, fetchConfigVO);
						break;
					case "clickTableImage":
						clickTableImage(driver, param1, param2, fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
						break;
					case "clickExpandorcollapse":
						clickExpandorcollapse(driver, param1, param2, fetchMetadataVO, fetchConfigVO);
						break;
					case "clickButton":
						clickButton(driver, param1, param2, fetchMetadataVO, fetchConfigVO);
						break;
					case "tableRowSelect":
						tableRowSelect(driver, param1, param2, fetchMetadataVO, fetchConfigVO);
						break;
					case "clickButton Dropdown":
						clickButtonDropdown(driver, param1,param2, fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
						break;
					case "mousehover":
						mousehover(driver, param1, param2, fetchMetadataVO, fetchConfigVO);
						break;
					case "scrollUsingElement":
						scrollUsingElement(driver, fetchMetadataVO.getInput_parameter(), fetchMetadataVO,
								fetchConfigVO);
						break;
					case "switchToDefaultFrame":
						switchToDefaultFrame(driver);
						break;
					case "switchToFrame":
						switchToFrame(driver, fetchMetadataVO.getInput_parameter(), fetchMetadataVO, fetchConfigVO);
						break;
					case "windowhandle":
						windowhandle(driver, fetchMetadataVO, fetchConfigVO);
						break;
					case "dragAnddrop":
						dragAnddrop(driver, fetchMetadataVO.getXpath_location(),
								fetchMetadataVO.getXpath_location1(), fetchMetadataVO, fetchConfigVO);
						break;
					case "clickFilter":
						clickFilter(driver, fetchMetadataVO.getXpath_location(),
								fetchMetadataVO.getXpath_location1(), fetchMetadataVO, fetchConfigVO);
						break;
					case "selectByText":
						selectByText(driver, param1, param2,fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
						break;
					case "copy":
						copy(driver, fetchMetadataVO, fetchConfigVO);
						break;
					case "copynumber":
						copynumber(driver, fetchMetadataVO.getInput_parameter(), fetchMetadataVO, fetchConfigVO);
						break; 
					case "copyy":
						copyy(driver, fetchMetadataVO.getXpath_location(), fetchMetadataVO, fetchConfigVO);
						break;
					case "copytext":
						copytext(driver, fetchMetadataVO.getXpath_location(), fetchMetadataVO, fetchConfigVO);
						break;
					case "clear":
						clear(driver, fetchMetadataVO.getInput_parameter(), fetchMetadataVO, fetchConfigVO);
						break;
					case "enter":
						enter(driver, fetchMetadataVO, fetchConfigVO);
						break;
					case "tab":
						tab(driver, fetchMetadataVO, fetchConfigVO);
						break;
					case "paste":
						paste(driver, fetchMetadataVO, fetchConfigVO);
						break;
					case "uploadFileAutoIT":
						uploadFileAutoIT(fetchMetadataVO.getField_type(), fetchMetadataVO);
						break;
					case "windowclose":
						windowclose(driver, fetchMetadataVO, fetchConfigVO);
						break;
					default:
						System.out.println("TestCaseName is not matched" + "" + actionName);
	//					screenshotException(driver, "Test Action Name Not Exists_", fetchMetadataListVO, fetchConfigVO);
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
						createPdf(fetchMetadataListVO, fetchConfigVO, script_Number+".pdf");
						DataService.updateTestCaseStatus(post, param, fetchConfigVO);
					}
					System.out.println("Successfully Executed the" + "" + actionName);
				} 
				catch (Exception e) {
					System.out.println("Failed to Execute the " + "" + actionName);
					System.out.println("Error occurred in TestCaseName=" + actionName + "" + "Exception="
							+ "" + e.getMessage());
//					screenshotException(driver, "Test Action Name Not Exists_", fetchMetadataListVO, fetchConfigVO);
					FetchScriptVO post = new FetchScriptVO();
					post.setP_test_set_id(test_set_id);
					post.setP_status("Fail");
					post.setP_script_id(script_id);
					post.setP_test_set_line_id(test_set_line_id);
					post.setP_pass_path(passurl);
					post.setP_fail_path(failurl);
					post.setP_exception_path(detailurl);
					post.setP_test_set_line_path(scripturl);
					createPdf(fetchMetadataListVO, fetchConfigVO, script_Number+".pdf");
					DataService.updateTestCaseStatus(post, param, fetchConfigVO);
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