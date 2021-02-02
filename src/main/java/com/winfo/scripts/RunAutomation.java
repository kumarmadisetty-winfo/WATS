package com.winfo.scripts;

import java.io.IOException;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lowagie.text.DocumentException;
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
public class RunAutomation extends SeleniumKeyWords {

	@Autowired
	TestCaseDataService dataService;
	@Autowired
	DataBaseEntry dataBaseEntry;
	public String c_url = null;

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
		createPdf(fetchMetadataListVO, fetchConfigVO, "Passed_Report.pdf",passcount,failcount);
		createPdf(fetchMetadataListVO, fetchConfigVO, "1_10_Create Manual Invoice Transaction_OTC.AR.203.pdf",passcount,failcount);
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
					              if(flag.equalsIgnoreCase("STOP")) {
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
					              if(flag.equalsIgnoreCase("STOP")) {
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
				createPdf(fetchMetadataListVO, fetchConfigVO, "Passed_Report.pdf", passcount, failcount);
				createPdf(fetchMetadataListVO, fetchConfigVO, "Failed_Report.pdf", passcount, failcount);
				createPdf(fetchMetadataListVO, fetchConfigVO, "Detailed_Report.pdf", passcount, failcount);
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
			Date date = new Date();
			 fetchConfigVO.setStarttime(date);
			DelatedScreenshoots(fetchMetadataListVO,fetchConfigVO);

			for (FetchMetadataVO fetchMetadataVO : fetchMetadataListVO) {
				String url = fetchConfigVO.getApplication_url();
				actionName = fetchMetadataVO.getAction();
				test_set_id = fetchMetadataVO.getTest_set_id();
				test_set_line_id = fetchMetadataVO.getTest_set_line_id();
				script_Number = fetchMetadataVO.getScript_number();
				line_number = fetchMetadataVO.getLine_number();
				seq_num = fetchMetadataVO.getSeq_num();
				dataBaseEntry.updateInProgressScriptStatus(fetchConfigVO,test_set_id,test_set_line_id);
				dataBaseEntry.updateStartTime(fetchConfigVO,test_set_line_id,test_set_id);
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
				int count=0;
				if (screenParameter != null) {
					param1 = screenParameter.split(">").length > 0 ? screenParameter.split(">")[0] : "";
					param2 = screenParameter.split(">").length > 1 ? screenParameter.split(">")[1] : "";
					param3 = screenParameter.split(">").length > 2 ? screenParameter.split(">")[2] : "";
					String actionType = fetchMetadataVO.getField_type();
					type1 = actionType != null ? actionType.split(">").length > 0 ? actionType.split(">")[0] : "" : "";
					type2 = actionType != null ? actionType.split(">").length > 1 ? actionType.split(">")[1] : "" : "";
					type3 = actionType != null ? actionType.split(">").length > 2 ? actionType.split(">")[2] : "" : "";
				
				}
				try {
					switch (actionName) {

					case "Login into Application":
						userName = fetchMetadataVO.getInput_value();
						if (dataSource == null)
							dataSource = new ConnectToSQL();
						log.info("Navigating to Login into Application Action");
						loginApplication(driver, fetchConfigVO, fetchMetadataVO, type1, type2, type3, param1, param2,
								param3, fetchMetadataVO.getInput_value(),
								dataSource.getPassword(param, userName, fetchConfigVO));
						userName = null;
						break;

					case "Navigate":
						log.info("Navigating to Navigate Action");
						navigate(driver, fetchConfigVO, fetchMetadataVO, type1, type2, param1, param2,count);
						break;

					case "openTask":
						log.info("Navigating to openTask Action");
						openTask(driver, fetchConfigVO, fetchMetadataVO, type1, type2, param1, param2,count);
						break;

					case "Logout":
						logout(driver, fetchConfigVO, fetchMetadataVO, type1, type2, type3, param1, param2, param3);
						break;
					case "SendKeys":
						sendValue(driver, param1, param2, fetchMetadataVO.getInput_value(), fetchMetadataVO,
								fetchConfigVO);
						break;
					case "textarea":
						textarea(driver, param1, param2, fetchMetadataVO.getInput_value(), fetchMetadataVO,
								fetchConfigVO);
						break;
					case "Dropdown Values":
						dropdownValues(driver, param1, param2, param3, fetchMetadataVO.getInput_value(),
								fetchMetadataVO, fetchConfigVO);
						break;
					case "Table SendKeys":
						tableSendKeys(driver, param1, param2, param3, fetchMetadataVO.getInput_value(), fetchMetadataVO,
								fetchConfigVO);
						break;
					case "multiplelinestableSendKeys":
						multiplelinestableSendKeys(driver, param1, param2, param3, fetchMetadataVO.getInput_value(), fetchMetadataVO,
								fetchConfigVO);
						break;
					case "Table Dropdown Values":
						tableDropdownValues(driver, param1, param2, fetchMetadataVO.getInput_value(), fetchMetadataVO,
								fetchConfigVO);
						break;
					case "clickLinkAction":
						clickLinkAction(driver, param1, param2, fetchMetadataVO.getInput_value(), fetchMetadataVO,
								fetchConfigVO);
						break;
					case "clickCheckbox":
						clickCheckbox(driver, param1, fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
						break;
					case "clickRadiobutton":
						clickRadiobutton(driver, param1, param2, fetchMetadataVO.getInput_value(), fetchMetadataVO,
								fetchConfigVO);
						break;
					case "selectAValue":
						selectAValue(driver, param1, param2, fetchMetadataVO.getInput_value(), fetchMetadataVO,
								fetchConfigVO);
						break;
					case "clickTableLink":
						clickTableLink(driver, param1, param2, fetchMetadataVO, fetchConfigVO);
						break;
					case "clickLink":
						clickLink(driver, param1, param2, fetchMetadataVO, fetchConfigVO);
						break;
					case "clickNotificationLink":
						clickNotificationLink(driver, param1, fetchMetadataVO, fetchConfigVO);
						break;
					case "clickMenu":
						clickMenu(driver, param1, param2, fetchMetadataVO, fetchConfigVO);
						break;
					case "clickImage":
						clickImage(driver, param1, param2, fetchMetadataVO, fetchConfigVO);
						break;
					case "clickTableImage":
						clickTableImage(driver, param1, param2, fetchMetadataVO.getInput_value(), fetchMetadataVO,
								fetchConfigVO);
						break;
					case "clickExpandorcollapse":
						clickExpandorcollapse(driver, param1, param2, fetchMetadataVO, fetchConfigVO);
						break;
					case "clickButton":		  
						  clickButton(driver, param1, param2, fetchMetadataVO, fetchConfigVO);
						    message=getErrorMessages(driver);
						    String message1=getErrorMessages(driver);
	                     if(message != null) {
	                           fetchConfigVO.setErrormessage(message);
	                           screenshotFail(driver, "", fetchMetadataVO, fetchConfigVO);
	                          throw new IllegalArgumentException("Erroe occured");
	                        }
	                     screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
						break;
					case "tableRowSelect":
						tableRowSelect(driver, param1, param2, fetchMetadataVO, fetchConfigVO);
						break;
					case "clickButton Dropdown":
						clickButtonDropdown(driver, param1, param2, fetchMetadataVO.getInput_value(), fetchMetadataVO,
								fetchConfigVO);https://watshubd01.winfosolutions.com:4443/wats/wats_workspace_prod/taconfig/data/
						break;
					case "mousehover":
						mousehover(driver, param1, param2, fetchMetadataVO, fetchConfigVO);
						break;
					case "scrollUsingElement":
						scrollUsingElement(driver, fetchMetadataVO.getInput_parameter(), fetchMetadataVO,
								fetchConfigVO);
						break;
					case "moveToElement":
						moveToElement(driver, fetchMetadataVO.getInput_parameter(), fetchMetadataVO, fetchConfigVO);
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
						dragAnddrop(driver, fetchMetadataVO.getXpath_location(), fetchMetadataVO.getXpath_location1(),
								fetchMetadataVO, fetchConfigVO);
						break;
					case "clickFilter":
						clickFilter(driver, fetchMetadataVO.getXpath_location(), fetchMetadataVO.getXpath_location1(),
								fetchMetadataVO, fetchConfigVO);
						break;
					case "selectByText":
						selectByText(driver, param1, param2, fetchMetadataVO.getInput_value(), fetchMetadataVO,
								fetchConfigVO);
						break;
					case "copy":
						copy(driver, fetchMetadataVO, fetchConfigVO);
						break;
					case "copynumber":
						globalValueForSteps = copynumber(driver, param1, param2, fetchMetadataVO, fetchConfigVO);
						break;
					case "copyy":
						copyy(driver, fetchMetadataVO.getXpath_location(), fetchMetadataVO, fetchConfigVO);
						break;
					case "copytext":
						copytext(driver, fetchMetadataVO.getXpath_location(), fetchMetadataVO, fetchConfigVO);
						break;
					case "clear":
						clear(driver, param1, param2, fetchMetadataVO, fetchConfigVO);
						break;
					case "enter":
						enter(driver, fetchMetadataVO, fetchConfigVO);
						break;
					case "tab":
						tab(driver, fetchMetadataVO, fetchConfigVO);
						break;
					case "paste":
						paste(driver, fetchMetadataVO.getInput_parameter(), fetchMetadataVO, fetchConfigVO,
								globalValueForSteps);
						break;
					case "uploadFileAutoIT":
						uploadFileAutoIT(fetchMetadataVO.getField_type(), fetchMetadataVO);
						break;
					case "windowclose":
						windowclose(driver, fetchMetadataVO, fetchConfigVO);
						break;
					case "switchDefaultContent":
						switchDefaultContent(driver, fetchMetadataVO, fetchConfigVO);
						break;
					case "switchParentWindow":
						switchParentWindow(driver, fetchMetadataVO, fetchConfigVO);
						break;
					case "switchToParentWindow":
						switchToParentWindow(driver, fetchMetadataVO, fetchConfigVO);
						break;
					case "DatePicker":
						datePicker(driver, param1, param2, fetchMetadataVO.getInput_value(), fetchMetadataVO, fetchConfigVO);
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
						 date = new Date();
						 fetchConfigVO.setEndtime(date);
						createPdf(fetchMetadataListVO, fetchConfigVO, seq_num + "_" + script_Number + ".pdf", passcount,
								failcount);
						try {
						dataService.updateTestCaseStatus(post, param, fetchConfigVO);
						 dataBaseEntry.updateEndTime(fetchConfigVO,test_set_line_id,test_set_id);
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
					 date = new Date();
					 fetchConfigVO.setEndtime(date);
					 dataBaseEntry.updateEndTime(fetchConfigVO,test_set_line_id,test_set_id);
					createFailedPdf(fetchMetadataListVO, fetchConfigVO, seq_num + "_" + script_Number + ".pdf");
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
