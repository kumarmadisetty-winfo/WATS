package com.winfo.serviceImpl;
//package com.winfo.wats.service.impl;
//import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
//
//import java.awt.AlphaComposite;
//import java.awt.BasicStroke;
//import java.awt.Color;
//import java.awt.Graphics;
//import java.awt.Graphics2D;
//import java.awt.RenderingHints;
//import java.awt.Robot;
//import java.awt.Toolkit;
//import java.awt.datatransfer.StringSelection;
//import java.awt.event.KeyEvent;
//import java.awt.geom.Rectangle2D;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.nio.file.FileSystems;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//import java.sql.Timestamp;
//import java.text.DateFormat;
//import java.text.DecimalFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.Set;
//import java.util.TreeMap;
//import java.util.concurrent.TimeUnit;
//
//import javax.imageio.IIOImage;
//import javax.imageio.ImageIO;
//import javax.imageio.ImageWriteParam;
//import javax.imageio.ImageWriter;
//import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
//import javax.imageio.stream.FileImageOutputStream;
//
////import blank.FFmpegFrameRecorder;
////import blank.IplImage;
////import blank.OpenCVFrameConverter;
//
////import org.apache.log4j.Logger;
//import org.bytedeco.javacpp.avcodec;
//import org.bytedeco.javacpp.opencv_core.IplImage;
//import org.bytedeco.javacv.FFmpegFrameRecorder;
//import org.bytedeco.javacv.OpenCVFrameConverter;
//import org.jfree.chart.ChartFactory;
//import org.jfree.chart.JFreeChart;
//import org.jfree.chart.block.BlockBorder;
//import org.jfree.chart.block.LineBorder;
//import org.jfree.chart.labels.PieSectionLabelGenerator;
//import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
//import org.jfree.chart.plot.PiePlot;
//import org.jfree.chart.title.LegendTitle;
//import org.jfree.data.general.DefaultPieDataset;
//import org.jfree.ui.RectangleEdge;
//import org.jfree.ui.RectangleInsets;
//import org.jfree.ui.VerticalAlignment;
//import org.json.JSONObject;
//import org.openqa.selenium.By;
////import org.openqa.selenium.ElementNotVisibleException;
//import org.openqa.selenium.JavascriptExecutor;
//import org.openqa.selenium.Keys;
//import org.openqa.selenium.OutputType;
//import org.openqa.selenium.StaleElementReferenceException;
////import org.openqa.selenium.StaleElementReferenceException;
//import org.openqa.selenium.TakesScreenshot;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.interactions.Actions;
//import org.openqa.selenium.support.ui.ExpectedConditions;
////import org.openqa.selenium.support.ui.FluentWait;
//import org.openqa.selenium.support.ui.Select;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.context.config.annotation.RefreshScope;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.itextpdf.awt.DefaultFontMapper;
//import com.itextpdf.text.Anchor;
//import com.itextpdf.text.BaseColor;
//import com.itextpdf.text.Chunk;
//import com.itextpdf.text.Document;
//import com.itextpdf.text.Element;
//import com.itextpdf.text.Font;
//import com.itextpdf.text.FontFactory;
//import com.itextpdf.text.Image;
//import com.itextpdf.text.Paragraph;
//import com.itextpdf.text.Rectangle;
//import com.itextpdf.text.pdf.PdfContentByte;
//import com.itextpdf.text.pdf.PdfPCell;
//import com.itextpdf.text.pdf.PdfPTable;
//import com.itextpdf.text.pdf.PdfTemplate;
//import com.itextpdf.text.pdf.PdfWriter;
//import com.itextpdf.text.pdf.draw.DottedLineSeparator;
//import com.itextpdf.text.pdf.draw.VerticalPositionMark;
//import com.lowagie.text.DocumentException;
//import com.winfo.wats.dao.impl.ActionsRepository;
//import com.winfo.wats.dao.impl.CodeLinesRepository;
//import com.winfo.wats.dao.impl.EBSMappingRepository;
//import com.winfo.wats.model.CodeLines;
//import com.winfo.wats.model.SeleniumEbsMapping;
//import com.winfo.wats.scripts.EvidenceReportConfiguration;
//import com.winfo.wats.service.OracleThemeInterface;
//import com.winfo.wats.service.TestRunScriptExecutionService;
//import com.winfo.wats.utils.DateUtils;
//import com.winfo.wats.utils.StringUtils;
//import com.winfo.wats.vo.ConfigVO;
//import com.winfo.wats.vo.TestRunExecutionVO;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import com.winfo.wats.model.CodeLines;
//import org.springframework.beans.factory.annotation.Value;
//@Service("UDG")
//@RefreshScope
//public class EBSExecution implements OracleThemeInterface{
//	
//	@Autowired
//	TestRunScriptExecutionService testRunScriptExecService;
//
//@Value("${configvO.watslogo}")
//private String watslogo;
//
//@Value("${configvO.watsvediologo}")
//private String watsvediologo;
//
//@Value("${configvO.whiteimage}")
//private String whiteimage;
//
//@Value("${evidenceReportFormat}")
//private String reportFormat;
//
//@Value("${evidenceReportLocation}")
//private String reportLocation;

//@Value("${microsoft.graph.base-drives-url}")
//private String microsoftGraphBaseDrivesUrl;

//@Value("${microsoft.graph.base-sites-url}")
//private String microsoftGraphBaseSitesUrl;

//@Autowired
//private EvidenceReportConfiguration evidenceReportConfiguration;
//
//@Autowired
//CodeLinesRepository codeLineRepo;
//
//@Autowired
//ActionsRepository actionRepo;
//
//@Autowired
//EBSMappingRepository ebsMappingRepo;
//
//private  final Logger log = LogManager.getLogger(EBSExecution.class);
//	//Logger log = Logger.getLogger("Logger");
//	
//	public String Main_Window = "";
//	public WebElement fromElement;
//	public WebElement toElement;
//	
//
//
////	public void loginApplication(WebDriver driver, ConfigVO configVO, TestRunExecutionVO fetchMetadataVO,
////			String type1, String type2, String type3, String param1, String param2, String param3, String keysToSend,
////			String value) throws Exception {
////		String param5 = "password";
////		String param6 = "Sign In";
////		navigateUrl(driver, configVO, fetchMetadataVO);
////		String xpath1 = loginPage(driver, param1, keysToSend, fetchMetadataVO, configVO);
////		String xpath2 = loginPage(driver, param5, value, fetchMetadataVO, configVO);
////		String scripNumber = fetchMetadataVO.getScriptNumber();
////		String xpath = xpath1 + ";" + xpath2;
////		testRunScriptExecService.saveXpathParams("User ID", "", scripNumber, xpath);
//////		sendValue(driver, param1, param3, keysToSend, fetchMetadataVO, configVO);
//////		sendValue(driver, param5, param2, value, fetchMetadataVO, configVO);
//////		clickSignInSignOut(driver, param6, fetchMetadataVO, configVO);
//////		clickButton(driver, param6, param2, fetchMetadataVO, configVO);
////	}
//	
//	public ArrayList<String> loginApplication(WebDriver driver, ConfigVO configVO, TestRunExecutionVO fetchMetadataVO,
//			String type1, String type2, String type3, String param1, String param2, String param3, String keysToSend,
//			String value,String actionName) throws Exception {
//		ArrayList<String> listOfCodeLines= new ArrayList<>();
//		
////		String param5 = "password";
////		String param6 = "Sign In";
//		 listOfCodeLines=	navigateUrl(driver, configVO, fetchMetadataVO,actionName);
//		//String xpath1 = loginPage(driver, param1, keysToSend, fetchMetadataVO, configVO);
////		String xpath2 = loginPage(driver, param5, value, fetchMetadataVO, configVO);
////		String scripNumber = fetchMetadataVO.getScriptNumber();
////		String xpath = xpath1 + ";" + xpath2;
////		testRunScriptExecService.saveXpathParams("User ID", "", scripNumber, xpath);
//		
////		long actionId=25;
////		List<CodeLines>listOfCodeLines=ebsRepo.findByAction_Id(actionId);
////		CodeLines robotCodeLine=listOfCodeLines.get(0);
////		if(robotCodeLine.getParam_values()!=null)
////		{
////			String param_value=robotCodeLine.getParam_values();
////			HashMap<String,Object> result =  new ObjectMapper().readValue(param_value, HashMap.class);
////			String codeLine=robotCodeLine.getRobot_line();
////			codeLine.replaceAll("${"+(String) result.keySet().toArray()[0]+"}", configVO.getApplicationUrl());
////		}
//		
//		return listOfCodeLines;
//		
//	}
//
//	public synchronized ArrayList<String> navigate(WebDriver driver, ConfigVO configVO, TestRunExecutionVO fetchMetadataVO,
//			String type1, String type2, String param1, String param2, int count,String actionName) throws Exception {
////		String param3 = "Navigator";
////		String xpath = navigator(driver, param3, fetchMetadataVO, configVO);
////		String xpath1 = menuNavigation(driver, param1, fetchMetadataVO, configVO);
////
////		String xpath2 = menuNavigationButton(driver, fetchMetadataVO, configVO, type1, type2, param1, param2,
////				count);
////		String scripNumber = fetchMetadataVO.getScriptNumber();
////		String xpaths = xpath + ">" + xpath1 + ">" + xpath2;
////		testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpaths);
//////		clickLink(driver, param3, param2, fetchMetadataVO, configVO);
//////		clickMenu(driver, param1, param2, fetchMetadataVO, configVO);
//////		clickButton(driver, param2, param2, fetchMetadataVO, configVO);
//		
//		SeleniumEbsMapping selEbsMapping=ebsMappingRepo.findBySeleniumActionName(actionName);
//		com.winfo.wats.model.Actions action=actionRepo.findByActionName(selEbsMapping.getEbsActionName());
//		
//			String codeLine="";
//			List<CodeLines>listOfCodeLines=codeLineRepo.findByActionIdOrderByCodeLineId(action);
//			HashMap<String, Object> result = null;
//			String dbValue="";
//			String key="" ;
//			String value;
//			ArrayList<String> listOfRobotCodeLines= new ArrayList<>();
//			//CodeLines robotCodeLine=listOfCodeLines.get(0);
//			for(CodeLines robotCodeLine:listOfCodeLines) {
//				codeLine=robotCodeLine.getRobot_line();
//			if(robotCodeLine.getParam_values()!=null)
//			{
//				
//				
//				String param_value=robotCodeLine.getParam_values();
//				try {
//					result = new ObjectMapper().readValue(param_value, HashMap.class);
//					for (Map.Entry<String,Object> entry : result.entrySet())
//					{
//			             key =entry.getKey();
//						 value=(String)entry.getValue();
//					
//					if(value.equalsIgnoreCase("<Pick from Config Table>"))
//					{
//						dbValue=	codeLineRepo.findByConfigurationId(configVO.getConfigurationId(),key);
//					}
//					if(value.equalsIgnoreCase("<Pick from Input Value>"))
//					{
//						dbValue=	codeLineRepo.findByTestRunScriptId(fetchMetadataVO.getTestRunScriptId(), key);
//					}
//					if(value.equalsIgnoreCase("<Pick from Input Parameter>"))
//					{
//						dbValue=	codeLineRepo.findByTestRunScriptIdInputParam(fetchMetadataVO.getTestRunScriptParamId(), key);
//					}
//					codeLine= codeLine.replace("${"+key+"}", dbValue);
//					
//					}
//					listOfRobotCodeLines.add(codeLine);
//
//				} catch (JsonMappingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (JsonProcessingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//			
//				System.out.println(codeLine);
//			}
//			else
//			{
//				listOfRobotCodeLines.add(codeLine);	
//			}
//		}
//			return listOfRobotCodeLines;
//		
//	}
//
//	public synchronized void openTask(WebDriver driver, ConfigVO configVO, TestRunExecutionVO fetchMetadataVO,
//			String type1, String type2, String param1, String param2, int count) throws Exception {
//		String param3 = "Tasks";
////		clickImage(driver, param3, param2, fetchMetadataVO, configVO);
////		clickLink(driver, param1, param2, fetchMetadataVO, configVO);
//		String xpath = task(driver, param3, fetchMetadataVO, configVO);
//		String xpath1 = taskMenu(driver, fetchMetadataVO, configVO, type1, type2, param1, param2, count);
//		String xpaths = xpath + ";" + xpath1;
//		String scripNumber = fetchMetadataVO.getScriptNumber();
//		testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpaths);
//	}
//
//	public void logout(WebDriver driver, ConfigVO configVO, TestRunExecutionVO fetchMetadataVO, String type1,
//			String type2, String type3, String param1, String param2, String param3) throws Exception {
//
//		String param4 = "UIScmil1u";
//		String param5 = "Sign Out";
//		String param6 = " Confirm";
//		logoutDropdown(driver, configVO, fetchMetadataVO, param1);
//		clickSignInSignOut(driver, param6, fetchMetadataVO, configVO);
//	}
//
//	public void logoutDropdown(WebDriver driver, ConfigVO configVO, TestRunExecutionVO fetchMetadataVO,
//			String param1) throws Exception {
//
//		try {
//			Thread.sleep(4000);
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@id,'UISpb2')]")));
//			WebElement waittext = driver.findElement(By.xpath("//div[contains(@id,'UISpb2')]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			Thread.sleep(4000);
//			try {
//				if (driver.findElement(By.xpath("//div[contains(@id,'popup-container')]//a[text()='Sign Out']"))
//						.isDisplayed()) {
//					WebElement signout = driver
//							.findElement(By.xpath("//div[contains(@id,'popup-container')]//a[text()='Sign Out']"));
//					signout.click();
//					Thread.sleep(4000);
//				}
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Successfully Logout is done " + scripNumber);
//			} catch (Exception e) {
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(4000);
//				WebElement signout = driver
//						.findElement(By.xpath("//div[contains(@id,'popup-container')]//a[text()='Sign Out']"));
//				signout.click();
//				Thread.sleep(4000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.error("Successfully Logout is done " + scripNumber);
//			}
//			return;
//		} catch (Exception e) {
//			System.out.println(e);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed to logout " + scripNumber);
//			screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, configVO);
//			throw e;
//		}
//	}
//
//	public void datePicker(WebDriver driver, String param1, String param2, String keysToSend,
//			TestRunExecutionVO fetchMetadataVO, ConfigVO configVO) throws Exception {
//
//		String[] fullDate = keysToSend.split(">");
//		String date = fullDate[0];
//		String month = fullDate[1];
//		String year = fullDate[2];
//
//		selectYear(driver, year, fetchMetadataVO, configVO);
//		selectMonth(driver, month, fetchMetadataVO, configVO);
//		selectDate(driver, date, fetchMetadataVO, configVO);
//
//	}
//
//	public void selectDate(WebDriver driver, String param1, TestRunExecutionVO fetchMetadataVO,
//			ConfigVO configVO) throws Exception {
//		try {
//			Thread.sleep(4000);
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions
//					.presenceOfElementLocated(By.xpath("//td[@data-afr-adfday='cm' and text()='" + param1 + "']")));
//			wait.until(ExpectedConditions
//					.elementToBeClickable(By.xpath("//td[@data-afr-adfday='cm' and text()='" + param1 + "']")));
//			WebElement waittext = driver
//					.findElement(By.xpath("//td[@data-afr-adfday='cm' and text()='" + param1 + "']"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			actions.moveToElement(waittext).click().build().perform();
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			return;
//		} catch (Exception e) {
//			log.error("Failed During Navigation");
//			screenshotFail(driver, "Failed during navigateUrl Method", fetchMetadataVO, configVO);
//			System.out.println("Not able to navitage to the Url");
//			log.error("Not able to navitage to the Url");
//			throw e;
//		}
//	}
//
//	public void selectMonth(WebDriver driver, String param1, TestRunExecutionVO fetchMetadataVO,
//			ConfigVO configVO) throws Exception {
//		try {
//			WebElement waittext = driver.findElement(By.xpath("//*[text()='Create Time Card']/following::select[1]"));
//			selectMethod(driver, param1, fetchMetadataVO, waittext, configVO);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			return;
//		} catch (Exception e) {
//			log.error("Failed During Navigation");
//			screenshotFail(driver, "Failed during Navigation Method", fetchMetadataVO, configVO);
//			System.out.println("Not able to navitage to the :" + "" + param1);
//			log.error("Not able to navitage to the :" + "" + param1);
//			throw e;
//		}
//	}
//
//	public void selectYear(WebDriver driver, String param1, TestRunExecutionVO fetchMetadataVO,
//			ConfigVO configVO) throws Exception {
//		try {
//			WebElement wait = driver.findElement(By.xpath("//*[text()='Select Year']/preceding-sibling::input[1]"));
//			String yearValue = wait.getAttribute("title");
//			int year = Integer.parseInt(yearValue);
//			System.out.println("The value of the year is: " + year);
//			log.info("The value of the year is: " + year);
//			if (year < Integer.parseInt(param1)) {
//				while (year != Integer.parseInt(param1)) {
//					WebElement increment = driver.findElement(By.xpath("//a[@title='increment']"));
//					increment.click();
//					year = year + 1;
//					System.out.println(year);
//					log.info(year);
//				}
//			} else if (year > Integer.parseInt(param1)) {
//				while (year != Integer.parseInt(param1)) {
//					WebElement decrement = driver.findElement(By.xpath("//a[@title='decrement']"));
//					decrement.click();
//					year = year - 1;
//					System.out.println(year);
//					log.info(year);
//				}
//			} else {
//				System.out.println("The given year is matched with the Oracle year");
//				log.info("The given year is matched with the Oracle year");
//			}
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			return;
//		} catch (Exception e) {
//			log.error("Failed During Navigation");
//			screenshotFail(driver, "Failed during Navigation Method", fetchMetadataVO, configVO);
//			System.out.println("Not able to navitage to the :" + "" + param1);
//			log.error("Not able to navitage to the :" + "" + param1);
//			throw e;
//		}
//	}
//
//	public void openSettings(WebDriver driver, ConfigVO configVO, TestRunExecutionVO fetchMetadataVO,
//			String type1, String type2, String type3, String param1, String param2, String param3) throws Exception {
//		String param4 = "UIScmil1u";
//		//clickLink(driver, param4, param3, fetchMetadataVO, configVO);
//		//clickLink(driver, param1, param2, fetchMetadataVO, configVO);
//		// clickSignInSignOut(driver, param6, fetchMetadataVO, configVO);
////		clickButton(driver, param6, param2, fetchMetadataVO, configVO);
//	}
//
//	public ArrayList<String> navigateUrl(WebDriver driver, ConfigVO configVO, TestRunExecutionVO fetchMetadataVO,String actionName) {
////		try {
////			driver.navigate().to(configVO.getApplicationUrl());
////			driver.manage().window().maximize();
////			deleteAllCookies(driver, fetchMetadataVO, configVO);
////			refreshPage(driver, fetchMetadataVO, configVO);
////			switchToActiveElement(driver, fetchMetadataVO, configVO);
////			String scripNumber = fetchMetadataVO.getScriptNumber();
////			log.error("Failed to logout " + scripNumber);
////		} catch (Exception e) {
////			String scripNumber = fetchMetadataVO.getScriptNumber();
////			log.error("failed to do navigate URl " + scripNumber);
////			screenshotFail(driver, "Failed during navigateUrl Method", fetchMetadataVO, configVO);
////			System.out.println("Not able to navitage to the Url");
////			log.error("Not able to navitage to the Url");
////		}
//		System.out.println("hi");
//	//	String actionName="GO_TO";
//		SeleniumEbsMapping selEbsMapping=ebsMappingRepo.findBySeleniumActionName(actionName);
//		com.winfo.wats.model.Actions action=actionRepo.findByActionName(selEbsMapping.getEbsActionName());
//	//	long actionId=25;
//		String codeLine="";
//		List<CodeLines>listOfCodeLines=codeLineRepo.findByActionIdOrderByCodeLineId(action);
//		HashMap<String, Object> result = null;
//		String dbValue="";
//		String key="" ;
//		String value;
//		ArrayList<String> listOfRobotCodeLines= new ArrayList<>();
//		//CodeLines robotCodeLine=listOfCodeLines.get(0);
//		for(CodeLines robotCodeLine:listOfCodeLines) {
//			codeLine=robotCodeLine.getRobot_line();
//		if(robotCodeLine.getParam_values()!=null)
//		{
//			
//			
//			String param_value=robotCodeLine.getParam_values();
//			try {
//				result = new ObjectMapper().readValue(param_value, HashMap.class);
//				for (Map.Entry<String,Object> entry : result.entrySet())
//				{
//		             key =entry.getKey();
//					 value=(String)entry.getValue();
//				
//				if(value.equalsIgnoreCase("<Pick from Config Table>"))
//				{
//					dbValue=	codeLineRepo.findByConfigurationId(configVO.getConfigurationId(),key);
//				}
//				if(value.equalsIgnoreCase("<Pick from Input Value>"))
//				{
//					dbValue=	codeLineRepo.findByTestRunScriptId(fetchMetadataVO.getTestRunScriptId(), key);
//				}
//				codeLine= codeLine.replace("${"+key+"}", dbValue);
//				
//				}
//				listOfRobotCodeLines.add(codeLine);
//
//			} catch (JsonMappingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (JsonProcessingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		
//			System.out.println(codeLine);
//		}
//		else
//		{
//			listOfRobotCodeLines.add(codeLine);	
//		}
//	}
//		return listOfRobotCodeLines;
//	}
//
//	public String loginPage(WebDriver driver, String param1, String keysToSend, TestRunExecutionVO fetchMetadataVO,
//			ConfigVO configVO) throws JsonMappingException, JsonProcessingException {
//		String xpath = null;
//		try {
//			if (param1.equalsIgnoreCase("password")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='" + param1 + "']")));
//				JavascriptExecutor jse = (JavascriptExecutor) driver;
//				jse.executeScript("document.getElementById('password').value = '" + keysToSend + "';");
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				Thread.sleep(1000);
//				//enter(driver, fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Succesfully password is entered " + scripNumber);
//				xpath = "//input[@type='param1']";
//				return xpath;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed to enter password " + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions
//					.presenceOfElementLocated(By.xpath("//*[contains(@placeholder,'" + param1 + "')]")));
//			WebElement waittill = driver.findElement(By.xpath("//*[contains(@placeholder,'" + param1 + "')]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittill).build().perform();
//			typeIntoValidxpath(driver, keysToSend, waittill, configVO, fetchMetadataVO);
//			JavascriptExecutor jse = (JavascriptExecutor) driver;
//			jse.executeScript("arguments[0].value='" + keysToSend + "';", waittill);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			Thread.sleep(1000);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			xpath = "//*[contains(@placeholder,'param1')]";
//			log.info("Successfully entered data " + scripNumber);
//			return xpath;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Failed during login page " + scripNumber);
//			screenshotFail(driver, "Failed During Login page", fetchMetadataVO, configVO);
//			System.out.println("Failed During Login page");
//		}
//		return xpath;
////		ArrayList<String> listOfCodeLines= new ArrayList<>()
////		long actionId=26;
////		String codeLine="";
////		List<CodeLines>listOfCodeLines=ebsRepo.findByAction_Id(actionId);
////		CodeLines robotCodeLine=listOfCodeLines.get(0);
////		codeLine=robotCodeLine.getRobot_line();
////		
////		 actionId=27;
////		String codeLine1="";
////		List<CodeLines>listOfCodeLines1=ebsRepo.findByAction_Id(actionId);
////		HashMap<String, Object> result = null;
////		for(CodeLines robotCodeLine1: listOfCodeLines1)
////		{
////			codeLine1=robotCodeLine1.getRobot_line();
////			String paramVal=robotCodeLine1.getParam_values();
////			result = new ObjectMapper().readValue(paramVal, HashMap.class);
////			 for (Map.Entry<String, Object> entry : result.entrySet())
////			 {
////				 String val=(String) entry.getValue();
////				 if(val.contains("Pick from YAML"))
////				 {}
////				 else
////				 {
////					  codeLine1=robotCodeLine.getRobot_line();
////						codeLine1.replaceAll("${"+(String) entry.getKey()+"}", keysToSend);
////				 }
////			 }
////		}
////		
////		
////		
////		return codeLine;
//	}
//
//	public String navigator(WebDriver driver, String param1, TestRunExecutionVO fetchMetadataVO,
//			ConfigVO configVO) throws Exception {
//		try {
//			Thread.sleep(4000);
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@title='" + param1 + "']")));
//			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@title='" + param1 + "']")));
//			WebElement waittext = driver.findElement(By.xpath("//a[@title='" + param1 + "']"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			actions.moveToElement(waittext).click().build().perform();
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Successfully navigator is done " + scripNumber);
//			String xpath = "//a[@title='param1']";
//			return xpath;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during navigator " + scripNumber);
//			screenshotFail(driver, "Failed during navigateUrl Method", fetchMetadataVO, configVO);
//			System.out.println("Not able to navitage to the Url");
//			log.error("Not able to navitage to the Url");
//			throw e;
//		}
//	}
//
//	public String menuNavigation(WebDriver driver, String param1, TestRunExecutionVO fetchMetadataVO,
//			ConfigVO configVO) throws Exception {
//		try {
//			if (param1.equalsIgnoreCase("Expenses")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(
//						By.xpath("(//*[contains(@id,'popup-container')]//*[@title='" + param1 + "'])[2]")));
//				wait.until(ExpectedConditions.elementToBeClickable(
//						By.xpath("(//*[contains(@id,'popup-container')]//*[@title='" + param1 + "'])[2]")));
//				WebElement waittext = driver
//						.findElement(By.xpath("(//*[contains(@id,'popup-container')]//*[@title='" + param1 + "'])[2]"));
//				Actions actions = new Actions(driver);
//				Thread.sleep(3000);
//				actions.moveToElement(waittext).build().perform();
//				actions.moveToElement(waittext).click().build().perform();
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Successfully MenuNavigation is done " + scripNumber);
//				String xpath = "(//*[contains(@id,'popup-container')]//*[@title='param1'])[2]";
//
//				return xpath;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during MenuNavigation " + scripNumber);
//			// TODO: handle exception
//		}
//
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(
//					By.xpath("//*[contains(@id,'popup-container')]//*[@title='" + param1 + "']")));
//			wait.until(ExpectedConditions.elementToBeClickable(
//					By.xpath("//*[contains(@id,'popup-container')]//*[@title='" + param1 + "']")));
//			WebElement waittext = driver
//					.findElement(By.xpath("//*[contains(@id,'popup-container')]//*[@title='" + param1 + "']"));
//
//			WebElement showmore = driver
//					.findElement(By.xpath("//*[contains(@id,'popup-container')]//a[text()='Show More']"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(showmore).build().perform();
//			actions.moveToElement(showmore).click().build().perform();
//			Thread.sleep(3000);
//			WebElement showless = driver
//					.findElement(By.xpath("//*[contains(@id,'popup-container')]//a[text()='Show Less']"));
//			actions.moveToElement(showless).build().perform();
//			actions.moveToElement(showless).click().build().perform();
//			Thread.sleep(3000);
//			actions.moveToElement(waittext).build().perform();
//			actions.moveToElement(waittext).click().build().perform();
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Successfully menunavigation is clicked " + scripNumber);
//			String xpath = "//*[contains(@id,'popup-container')]//a[text()='Show More']" + ">"
//					+ "//*[contains(@id,'popup-container')]//a[text()='Show Less']";
//			log.info("Successfully menunavigation is clicked " + scripNumber);
//			return xpath;
//
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Menunavigation " + scripNumber);
//
//			screenshotFail(driver, "Failed during Navigation Method", fetchMetadataVO, configVO);
//			System.out.println("Not able to navitage to the :" + "" + param1);
//			log.error("Not able to navitage to the :" + "" + param1);
//			throw e;
//		}
//	}
//
//	public String menuNavigationButton(WebDriver driver, TestRunExecutionVO fetchMetadataVO, ConfigVO configVO,
//			String type1, String type2, String param1, String param2, int count) throws Exception {
//		String xpath = null;
//		try {
//			Thread.sleep(3000);
//			if (param2.equalsIgnoreCase("Assets")) {
//				WebElement asset = driver.findElement(By.xpath(
//						"//span[normalize-space(text())='Fixed Assets']/following::span[normalize-space(text())='"
//								+ param2 + "']"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(asset).build().perform();
//				actions.moveToElement(asset).click().build().perform();
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Successfully menuNavigationButton is done " + scripNumber);
//				xpath = "//span[normalize-space(text())='Fixed Assets']/following::span[normalize-space(text())='param2']";
//				log.info("Successfully menuNavigationButton is done " + scripNumber);
//				return xpath;
//
//			} else {
//				// try {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
//						"//div[@style='visibility: visible;']//span[normalize-space(text())='" + param2 + "']")));
//				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
//						"//div[@style='visibility: visible;']//span[normalize-space(text())='" + param2 + "']")));
//				WebElement waittext = driver.findElement(By
//						.xpath("//div[@style='visibility: visible;']//span[normalize-space(text())='" + param2 + "']"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				actions.moveToElement(waittext).click().build().perform();
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Successfully menuNavigationButton is done " + scripNumber);
//				xpath = "//div[@style='visibility: visible;']//span[normalize-space(text())='" + param1 + "']";
//				log.info("Successfully menuNavigationButton is done " + scripNumber);
//				return xpath;
//			}
//		} catch (Exception e) {
//			if (count == 0) {
//				count = 1;
//				System.out.println(" The Count Value is : " + count);
//				log.info(" The Count Value is : " + count);
//			//	navigate(driver, configVO, fetchMetadataVO, type1, type2, param1, param2, count);
//			} else if (count <= 10) {
//				count = count + 1;
//				System.out.println(" The Count Value is : " + count);
//				log.info(" The Count Value is : " + count);
//				//navigate(driver, configVO, fetchMetadataVO, type1, type2, param1, param2, count);
//			} else {
//				System.out.println("Count value exceeds the limit");
//				log.error("Failed During Navigation");
//				screenshotFail(driver, "Failed during Navigation Method", fetchMetadataVO, configVO);
//				System.out.println("Not able to navitage to the :" + "" + param1);
//				log.error("Not able to navitage to the :" + "" + param1);
//				throw e;
//			}
//
//		}
//		return xpath;
//	}
//
//	public String task(WebDriver driver, String param1, TestRunExecutionVO fetchMetadataVO, ConfigVO configVO)
//			throws Exception {
//		try {
//			Thread.sleep(7000);
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@title='" + param1 + "']")));
//			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@title='" + param1 + "']")));
//			WebElement waittext = driver.findElement(By.xpath("//img[@title='" + param1 + "']"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Successfully task is open " + scripNumber);
//			String xpath = "//img[@title='param1']";
//			log.info("Successfully task is open " + scripNumber);
//			return xpath;
//
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Failed During Task " + scripNumber);
//			screenshotFail(driver, "Failed to Open Task Menu", fetchMetadataVO, configVO);
//			System.out.println("Failed to Open Task Menu");
//			log.info("Failed to Open Task Menu");
//			throw e;
//		}
//	}
//
//	public String taskMenu(WebDriver driver, TestRunExecutionVO fetchMetadataVO, ConfigVO configVO, String type1,
//			String type2, String param1, String param2, int count) throws Exception {
//		String xpath = null;
//		try {
//			Thread.sleep(2000);
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(
//					By.xpath("//div[contains(@class,'AFVertical')]//a[normalize-space(text())='" + param1 + "']")));
//			wait.until(ExpectedConditions.elementToBeClickable(
//					By.xpath("//div[contains(@class,'AFVertical')]//a[normalize-space(text())='" + param1 + "']")));
//			WebElement waittext = driver.findElement(
//					By.xpath("//div[contains(@class,'AFVertical')]//a[normalize-space(text())='" + param1 + "']"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			Thread.sleep(5000);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Successfully open Task " + scripNumber);
//			xpath = "//div[contains(@class,'AFVertical')]//a[normalize-space(text())='param1']";
//
//			log.info("Successfully open Task " + scripNumber);
//			return xpath;
//
//		} catch (Exception e) {
//			if (count == 0) {
//				count = 1;
//				System.out.println(" The Count Value is : " + count);
//				openTask(driver, configVO, fetchMetadataVO, type1, type2, param1, param2, count);
//			} else if (count <= 10) {
//				count = count + 1;
//				System.out.println(" The Count Value is : " + count);
//				openTask(driver, configVO, fetchMetadataVO, type1, type2, param1, param2, count);
//			} else {
//				System.out.println("Count value exceeds the limit");
//				log.error("Failed to Open Task Menu");
//				screenshotFail(driver, "Failed to Open Task Menu", fetchMetadataVO, configVO);
//				System.out.println("Failed to Open Task Menu");
//				throw e;
//
//			}
//		}
//		return xpath;
//	}
//
//	public void mediumWait(WebDriver driver, String inputData, TestRunExecutionVO fetchMetadataVO,
//			ConfigVO configVO) {
//		try {
//			int time = StringUtils.convertStringToInteger(inputData, 4);
//			int seconds = time * 1000;
//			Thread.sleep(seconds);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Successfully waited for 4 seconds " + scripNumber);
//		} catch (InterruptedException e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed During meduim wait" + scripNumber);
//			e.printStackTrace();
//			// Restore interrupted state...
//			Thread.currentThread().interrupt();
//		}
//	}
//
//	public void shortwait(WebDriver driver, String inputData) {
//		try {
//			int time = StringUtils.convertStringToInteger(inputData, 2);
//			int seconds = time * 1000;
//			log.info("Successfully shortwait");
//			Thread.sleep(seconds);
//		} catch (InterruptedException e) {
//			log.error("Failed During shortwait");
//			e.printStackTrace();
//			// Restore interrupted state...
//			Thread.currentThread().interrupt();
//		}
//	}
//
//	public void wait(WebDriver driver, String inputData) {
//		try {
//			int time = StringUtils.convertStringToInteger(inputData, 8);
//			int seconds = time * 1000;
//			log.info("Successfully wait");
//			Thread.sleep(seconds);
//		} catch (InterruptedException e) {
//			log.error("Failed During wait");
//			e.printStackTrace();
//			// Restore interrupted state...
//			Thread.currentThread().interrupt();
//		}
//	}
//
//	public List<String> getFailFileNameListNew(List<TestRunExecutionVO> fetchMetadataListVO, ConfigVO configVO)
//			throws IOException {
//		System.out.println("entered to getFailFileNameListNew");
//		File folder = new File(configVO.getScreenshotPath() + fetchMetadataListVO.get(0).getCustomerName() + "/"
//				+ fetchMetadataListVO.get(0).getTestRunName() + "/");
//		// File folder = new File("C:\\\\Users\\\\Winfo Solutions\\\\Desktop\\\\test");
//		File[] listOfFiles = folder.listFiles();
//		String video_rec = "no";
//		// String video_rec="yes";
////		List<File> fileList = Arrays.asList(listOfFiles);
//		List<File> allFileList = Arrays.asList(listOfFiles);
//		List<File> fileList = new ArrayList<>();
//		String seqNumber = String.valueOf( fetchMetadataListVO.get(0).getSeqNum());
//		// String seqNumber = "1";
//		for (File file : allFileList) {
//			if (file.getName().startsWith(seqNumber + "_")) {
//				fileList.add(file);
//			}
//		}
//		System.out.println("before Collections.sort completed");
//		Collections.sort(fileList, new Comparator<File>() {
//
//			public int compare(File f1, File f2) {
//
//				return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified()) * -1;
//
//			}
//
//		});
//		System.out.println("after Collections.sort completed");
//		List<String> fileNameList = new ArrayList<String>();
//		ArrayList<String> linksall = new ArrayList<String>();
//		ArrayList<String> links1 = new ArrayList<String>();
//		File file = new ClassPathResource(whiteimage).getFile();
//		// File file = new File("C:\\Users\\Winfo
//		// Solutions\\Desktop\\Add_On\\white.jpg");
//		File file1 = new ClassPathResource(watsvediologo).getFile();
//		// File file1=new File("C:\\Users\\Winfo
//		// Solutions\\Desktop\\Add_On\\WATS_LOGO.JPG");
//
//		BufferedImage image = null;
//		image = ImageIO.read(file);
//		BufferedImage logo = null;
//		logo = ImageIO.read(file1);
//		Graphics g = image.getGraphics();
//		g.setColor(Color.black);
//		java.awt.Font font = new java.awt.Font("Calibri", java.awt.Font.PLAIN, 36);
//		g.setFont(font);
//		String details = fileList.get(0).getName();
//		// String details= seqList.get(0).getName();
//		String ScriptNumber = details.split("_")[3];
//		String TestRun = details.split("_")[4];
//		String Status = details.split("_")[6];
//		String status = Status.split("\\.")[0];
//		String Scenario = details.split("_")[2];
//		String imagename = TestRun + ScriptNumber;
//		String TName = fetchMetadataListVO.get(0).getTestRunName();
//		String no = details.split("_")[0];
//		Date Starttime = configVO.getStarttime();
//		Date endtime = configVO.getEndtime();
//		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//		String Starttime1 = dateFormat.format(Starttime);
////Changed the executed by variable
//		String ExeBy = fetchMetadataListVO.get(0).getExecutedBy();
//		String endtime1 = dateFormat.format(endtime);
//		long diff = endtime.getTime() - Starttime.getTime();
//		long diffSeconds = diff / 1000 % 60;
//		long diffMinutes = diff / (60 * 1000) % 60;
//		long diffHours = diff / (60 * 60 * 1000);
//		String ExecutionTime = diffHours + ":" + diffMinutes + ":" + diffSeconds;
//		g.drawString("TEST SCRIPT DETAILS", 450, 50);
//		g.drawString("Test Run Name : " + TName, 50, 100);
//		g.drawString("Script Number : " + ScriptNumber, 50, 150);
//		g.drawString("Test Case Name :" + Scenario, 50, 200);
//		g.drawString("Status : " + status, 50, 250);
//		g.drawString("Executed By :" + ExeBy, 50, 300);
//		g.drawString("Start Time :" + Starttime1, 50, 350);
//		g.drawString("End Time :" + endtime1, 50, 400);
//		g.drawString("Execution Time : " + ExecutionTime, 50, 450);
//		g.drawImage(logo, 1012, 15, null);
//		g.dispose();
//		System.out.println("before ImageIO.write");
//		File folder1 = new File(
//				configVO.getScreenshotPath() + fetchMetadataListVO.get(0).getCustomerName() + "/Images");
//		if (!folder1.exists()) {
//			System.out.println("creating directory: " + folder1.getName());
//			boolean result = false;
//			try {
//				folder1.mkdirs();
//				result = true;
//			} catch (SecurityException se) {
//				// handle it
//				System.out.println(se.getMessage());
//			}
//		} else {
//			System.out.println("Folder exist");
//		}
//		ImageIO.write(image, "jpg", new File(folder1 + "/first.jpg"));
//		// ImageIO.write(image, "jpg", new File("C:\\Users\\Winfo
//		// Solutions\\Desktop\\Add_On\\first.jpg"));
//
//		BufferedImage image1 = null;
//		image1 = ImageIO.read(file);
//		Graphics g1 = image1.getGraphics();
//		g1.setColor(Color.red);
//		java.awt.Font font1 = new java.awt.Font("Calibir", java.awt.Font.PLAIN, 36);
//		g1.setFont(font1);
//		g1.drawString("FAILED IN THE NEXT STEP!!", 400, 300);
//		g1.drawImage(logo, 1012, 15, null);
//		g1.dispose();
//
//		ImageIO.write(image1, "jpg", new File(folder1 + "/last.jpg"));
//		String imgpath2 = folder1 + "/";
//		// ImageIO.write(image1, "jpg", new File("C:\\Users\\Winfo
//		// Solutions\\Desktop\\Add_On\\last.jpg"));
//		// String imgpath2 ="C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\";
//		File f11 = new File(imgpath2);
//		File[] f22 = f11.listFiles();
//		System.out.println("before Failed.jpg");
//		if (fileList.get(0).getName().endsWith("Failed.jpg")) {
//			for (File f33 : f22) {
//				if (f33.getAbsolutePath().contains("first")) {
//					linksall.add(f33.getAbsolutePath());
//					linksall.set(0, f33.getAbsolutePath());
//				}
//				if (f33.getAbsolutePath().contains("last")) {
//					linksall.add(f33.getAbsolutePath());
//					linksall.add(f33.getAbsolutePath());
//					linksall.set(1, f33.getAbsolutePath());
//
//				}
//			}
//			configVO.setStatus1("Fail");
//			fileNameList.add(fileList.get(0).getName());
//			links1.add(fileList.get(0).getAbsolutePath());
//			links1.add(linksall.get(1));
//			System.out.println("added links1 list");
//			for (int i = 1; i < fileList.size(); i++) {
//
//				if (!fileList.get(i).getName().endsWith("Failed.jpg")) {
//					links1.add(fileList.get(i).getAbsolutePath());
//					fileNameList.add(fileList.get(i).getName());
//
//				} else {
//
//				}
//
//			}
//
//			links1.add(linksall.get(0));
//			Collections.reverse(links1);
//			Collections.reverse(fileNameList);
//			System.out.println("Collections.reverse");
//		}
//
//		// targetFileList.addAll(seqList);
//
//		/*
//		 * for (String fileName : fileNameList) {
//		 * 
//		 * System.out.println("Target File : " + fileName);
//		 * 
//		 * }
//		 */
//		if (video_rec.equalsIgnoreCase("Y")) {
//			String name = no + "_" + ScriptNumber + ".mp4";
//			convertJPGtoMovie(null, links1, fetchMetadataListVO, configVO, name);
//		}
//		return fileNameList;
//	}
//
//	public List<String> getFileNameListNew(List<TestRunExecutionVO> fetchMetadataListVO, ConfigVO configVO)
//			throws IOException {
//
//		File folder = new File(configVO.getScreenshotPath() + fetchMetadataListVO.get(0).getCustomerName() + "/"
//				+ fetchMetadataListVO.get(0).getTestRunName() + "/");
//		// File folder = new File("C:\\\\Users\\\\Winfo Solutions\\\\Desktop\\\\test");
//
//		File[] listOfFiles = folder.listFiles();
////		String video_rec=configVO.getEnable_video();
//		String video_rec = "no";
////		List<File> fileList = Arrays.asList(listOfFiles);
//		List<File> allFileList = Arrays.asList(listOfFiles);
//		List<File> fileList = new ArrayList<>();
//		String seqNumber = String.valueOf( fetchMetadataListVO.get(0).getSeqNum());
//		// String seqNumber = "1";
//		for (File file : allFileList) {
//			if (file.getName().startsWith(seqNumber + "_")) {
//				fileList.add(file);
//			}
//		}
//
//		Collections.sort(fileList, new Comparator<File>() {
//
//			public int compare(File f1, File f2) {
//
//				return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified()) * -1;
//
//			}
//
//		});
//
//		List<String> fileNameList = new ArrayList<String>();
//		ArrayList<String> linksall = new ArrayList<String>();
//		ArrayList<String> links1 = new ArrayList<String>();
//		File file = new ClassPathResource(whiteimage).getFile();
//		// File file = new File("C:\\Users\\Winfo
//		// Solutions\\Desktop\\Add_On\\white.jpg");
//		File file1 = new ClassPathResource(watsvediologo).getFile();
//		// File file1=new File("C:\\Users\\Winfo
//		// Solutions\\Desktop\\Add_On\\WATS_LOGO.JPG");
//
//		BufferedImage image = null;
//		image = ImageIO.read(file);
//		BufferedImage logo = null;
//		logo = ImageIO.read(file1);
//		Graphics g = image.getGraphics();
//		g.setColor(Color.black);
//		java.awt.Font font = new java.awt.Font("Calibri", java.awt.Font.PLAIN, 36);
//		g.setFont(font);
//		String details = fileList.get(0).getName();
//		// String details= seqList.get(0).getName();
//		String ScriptNumber = details.split("_")[3];
//		String TestRun = details.split("_")[4];
//		String Status = details.split("_")[6];
//		String status = Status.split("\\.")[0];
//		String Scenario = details.split("_")[2];
//		String imagename = TestRun + ScriptNumber;
//		String TName = fetchMetadataListVO.get(0).getTestRunName();
//		String no = details.split("_")[0];
//		Date Starttime = configVO.getStarttime();
//		Date endtime = configVO.getEndtime();
//		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//		String Starttime1 = dateFormat.format(Starttime);
////Changed the executed by variable
//		String ExeBy = fetchMetadataListVO.get(0).getExecutedBy();
//		String endtime1 = dateFormat.format(endtime);
//		long diff = endtime.getTime() - Starttime.getTime();
//		long diffSeconds = diff / 1000 % 60;
//		long diffMinutes = diff / (60 * 1000) % 60;
//		long diffHours = diff / (60 * 60 * 1000);
//		String ExecutionTime = diffHours + ":" + diffMinutes + ":" + diffSeconds;
//		g.drawString("TEST SCRIPT DETAILS", 450, 50);
//		g.drawString("Test Run Name : " + TName, 50, 100);
//		g.drawString("Script Number : " + ScriptNumber, 50, 150);
//		g.drawString("Test Case Name :" + Scenario, 50, 200);
//		g.drawString("Status : " + status, 50, 250);
//		g.drawString("Executed By :" + ExeBy, 50, 300);
//		g.drawString("Start Time :" + Starttime1, 50, 350);
//		g.drawString("End Time :" + endtime1, 50, 400);
//		g.drawString("Execution Time : " + ExecutionTime, 50, 450);
//		g.drawImage(logo, 1012, 15, null);
//		g.dispose();
//		File folder1 = new File(
//				configVO.getScreenshotPath() + fetchMetadataListVO.get(0).getCustomerName() + "/Images");
//		if (!folder1.exists()) {
//			System.out.println("creating directory: " + folder1.getName());
//			boolean result = false;
//			try {
//				folder1.mkdirs();
//				result = true;
//			} catch (SecurityException se) {
//				// handle it
//				System.out.println(se.getMessage());
//			}
//		} else {
//			System.out.println("Folder exist");
//		}
//
//		ImageIO.write(image, "jpg", new File(folder1 + "/first.jpg"));
//		// ImageIO.write(image, "jpg", new File("C:\\Users\\Winfo
//		// Solutions\\Desktop\\Add_On\\first.jpg"));
//
//		BufferedImage image1 = null;
//		image1 = ImageIO.read(file);
//		Graphics g1 = image1.getGraphics();
//		g1.setColor(Color.red);
//		java.awt.Font font1 = new java.awt.Font("Calibir", java.awt.Font.PLAIN, 36);
//		g1.setFont(font1);
//		g1.drawString("FAILED IN THE NEXT STEP!!", 400, 300);
//		g1.drawImage(logo, 1150, 15, null);
//		g1.dispose();
//		ImageIO.write(image1, "jpg", new File(folder1 + "/last.jpg"));
//		// ImageIO.write(image1, "jpg", new File("C:\\Users\\Winfo
//		// Solutions\\Desktop\\Add_On\\last.jpg"));
//		String imgpath2 = folder1 + "/";
//		// String imgpath2 ="C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\";
//		File f11 = new File(imgpath2);
//		File[] f22 = f11.listFiles();
//
//		if (!fileList.get(0).getName().endsWith("Failed.jpg")) {
//			for (File f33 : f22) {
//				if (f33.getAbsolutePath().contains("first")) {
//					linksall.add(f33.getAbsolutePath());
//				}
//			}
//			configVO.setStatus1("Pass");
//			fileNameList.add(fileList.get(0).getName());
//			links1.add(fileList.get(0).getAbsolutePath());
//			for (int i = 1; i < fileList.size(); i++) {
//
//				if (!fileList.get(i).getName().endsWith("Failed.jpg")) {
//					links1.add(fileList.get(i).getAbsolutePath());
//
//					fileNameList.add(fileList.get(i).getName());
//
//				} else {
//
//				}
//
//			}
//
//			links1.add(linksall.get(0));
//			Collections.reverse(links1);
//			Collections.reverse(fileNameList);
//
//		}
//
//		// targetFileList.addAll(seqList);
//
//		/*
//		 * for (String fileName : fileNameList) {
//		 * 
//		 * System.out.println("Target File : " + fileName);
//		 * 
//		 * }
//		 */
//		if (video_rec.equalsIgnoreCase("Y")) {
//			String name = no + "_" + ScriptNumber + ".mp4";
//			convertJPGtoMovie(null, links1, fetchMetadataListVO, configVO, name);
//		}
//		return fileNameList;
//
//	}
//
//	public List<String> getPassedPdfNew(List<TestRunExecutionVO> fetchMetadataListVO, ConfigVO configVO)
//			throws IOException {
//
//		File folder = new File(configVO.getScreenshotPath() + fetchMetadataListVO.get(0).getCustomerName() + "/"
//				+ fetchMetadataListVO.get(0).getTestRunName() + "/");
//		// File folder=new File("C:\\Users\\Winfo Solutions\\Desktop\\test");
//		File[] listOfFiles = folder.listFiles();
//		// String video_rec=configVO.getVideo_rec();
//		String video_rec = "no";
//		Map<Integer, List<File>> filesMap = new TreeMap<>();
//		int passcount = 0;
//		int failcount = 0;
//		for (File file : Arrays.asList(listOfFiles)) {
//
//			Integer seqNum = Integer.valueOf(file.getName().substring(0, file.getName().indexOf('_')));
//
//			if (!filesMap.containsKey(seqNum)) {
//
//				filesMap.put(seqNum, new ArrayList<File>());
//
//			}
//
//			filesMap.get(seqNum).add(file);
//
//		}
//
//		List<String> targetFileList = new ArrayList<>();
//		ArrayList<String> links = new ArrayList<String>();
//		String firstimagelink = null;
//		for (Entry<Integer, List<File>> seqEntry : filesMap.entrySet()) {
//
//			List<File> seqList = seqEntry.getValue();
//
//			Collections.sort(seqList, new Comparator<File>() {
//
//				public int compare(File f1, File f2) {
//
//					return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified()) * -1;
//
//				}
//
//			});
//			List<String> seqFileNameList = new ArrayList<String>();
//			ArrayList<String> links1 = new ArrayList<String>();
//			ArrayList<String> linksall = new ArrayList<String>();
//
//			File file = new ClassPathResource(whiteimage).getFile();
//			// File file = new File("C:\\Users\\Winfo
//			// Solutions\\Desktop\\Add_On\\white.jpg");
//			File file1 = new ClassPathResource(watsvediologo).getFile();
//			// File file1=new File("C:\\Users\\Winfo
//			// Solutions\\Desktop\\Add_On\\WATS_LOGO.JPG");
//
//			BufferedImage image = null;
//			image = ImageIO.read(file);
//			BufferedImage logo = null;
//			logo = ImageIO.read(file1);
//			Graphics g = image.getGraphics();
//			g.setColor(Color.black);
//			java.awt.Font font = new java.awt.Font("Calibri", java.awt.Font.PLAIN, 36);
//			g.setFont(font);
//
//			String details = seqList.get(0).getName();
//			String ScriptNumber = details.split("_")[3];
//			String TestRun = details.split("_")[4];
//			String Status = details.split("_")[6];
//			String status = Status.split("\\.")[0];
//			String Scenario = details.split("_")[2];
//			String imagename = TestRun + ScriptNumber;
//			String TName = fetchMetadataListVO.get(0).getTestRunName();
//			Date endtime = configVO.getEndtime();
//			Date TStarttime = configVO.getStarttime1();
//			DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//			String TStarttime1 = dateFormat.format(TStarttime);
////Changed the executed by variable
//			String ExeBy = fetchMetadataListVO.get(0).getExecutedBy();
//			String endtime1 = dateFormat.format(endtime);
//			long Tdiff = endtime.getTime() - TStarttime.getTime();
//			long TdiffSeconds = Tdiff / 1000 % 60;
//			long TdiffMinutes = Tdiff / (60 * 1000) % 60;
//			long TdiffHours = Tdiff / (60 * 60 * 1000);
//			String ExecutionTime = TdiffHours + ":" + TdiffMinutes + ":" + TdiffSeconds;
//			g.drawString("TEST SCRIPT DETAILS", 450, 50);
//			g.drawString("Test Run Name : " + TName, 50, 125);
//			g.drawString("Script Number : " + ScriptNumber, 50, 200);
//			g.drawString("Test Case Name :" + Scenario, 50, 275);
//			g.drawString("Status : " + status, 50, 350);
//			g.drawString("Executed By :" + ExeBy, 50, 425);
//			g.drawImage(logo, 1012, 15, null);
//////		 g.drawString("Start Time :"+TStarttime1, 50, 425);
//////		 g.drawString("End Time :"+endtime1, 50, 500);
//////		 g.drawString("Execution Time : "+ExecutionTime, 50, 575);
//			g.dispose();
//
//			BufferedImage image2 = null;
//			image2 = ImageIO.read(file);
//			Graphics g2 = image2.getGraphics();
//			g2.setColor(Color.black);
//			g2.setFont(font);
//			g2.drawString("TEST RUN SUMMARY", 450, 50);
//			g2.drawString("Test Run Name : " + TName, 50, 125);
//			g2.drawString("Executed By :" + ExeBy, 50, 200);
//			g2.drawString("Start Time :" + TStarttime1, 50, 275);
//			g2.drawString("End Time :" + endtime1, 50, 350);
//			g2.drawString("Execution Time : " + ExecutionTime, 50, 425);
//			g2.drawImage(logo, 1012, 15, null);
//			g2.dispose();
//			File folder1 = new File(
//					configVO.getScreenshotPath() + fetchMetadataListVO.get(0).getCustomerName() + "/Images");
//			if (!folder1.exists()) {
//				System.out.println("creating directory: " + folder1.getName());
//				boolean result = false;
//				try {
//					folder1.mkdirs();
//					result = true;
//				} catch (SecurityException se) {
//					// handle it
//					System.out.println(se.getMessage());
//				}
//			} else {
//				System.out.println("Folder exist");
//			}
//
//			ImageIO.write(image2, "jpg", new File(folder1 + "/first.jpg"));
//			// ImageIO.write(image2, "jpg", new File("C:\\Users\\Winfo
//			// Solutions\\Desktop\\Add_On\\first.jpg"));
//			String imgpath3 = folder1 + "/first.jpg";
//			String imgpath2 = folder1 + "/";
//			ImageIO.write(image, "jpg", new File(folder1 + "/" + imagename + ".jpg"));
//			// String imgpath3 ="C\\Users\\Winfo Solutions\\Desktop\\Add_On\\first.jpg";
//			// String imgpath2 ="C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\";
//			// ImageIO.write(image, "jpg", new File("C:\\Users\\Winfo
//			// Solutions\\Desktop\\Add_On\\"+imagename+".jpg"));
//
//			File f11 = new File(imgpath2);
//			File[] f22 = f11.listFiles();
//			File f44 = new File(imgpath3);
//			firstimagelink = f44.getAbsolutePath();
//
//			if (!seqList.get(0).getName().endsWith("Failed.jpg")) {
//				passcount++;
//				for (File f33 : f22) {
//					if (f33.getAbsolutePath().contains(imagename)) {
//						linksall.add(f33.getAbsolutePath());
//					}
//				}
//				links1.add(seqList.get(0).getAbsolutePath());
//				seqFileNameList.add(seqList.get(0).getName());
//
//				for (int i = 1; i < seqList.size(); i++) {
//
//					if (!seqList.get(i).getName().endsWith("Failed.jpg")) {
//						links1.add(seqList.get(i).getAbsolutePath());
//						seqFileNameList.add(seqList.get(i).getName());
//
//					} else {
//
//					}
//
//				}
//
//				links1.add(linksall.get(0));
//				Collections.reverse(links1);
//				Collections.reverse(seqFileNameList);
//				links.addAll(links1);
//				targetFileList.addAll(seqFileNameList);
//
//			}
//
//////                    targetFileList.addAll(seqList);
//
//		}
//
//		/*
//		 * for (String fileName : targetFileList) {
//		 * 
//		 * System.out.println("Target File : " + fileName);
//		 * 
//		 * }
//		 */
//
//		configVO.setPasscount(passcount);
//		configVO.setFailcount(failcount);
//		if (video_rec.equalsIgnoreCase("yes")) {
//			convertJPGtoMovie(firstimagelink, links, fetchMetadataListVO, configVO, "Passed_Video.mp4");
//		}
//		System.out.println(targetFileList.size());
//		return targetFileList;
//	}
//
//	public List<String> getFailedPdfNew(List<TestRunExecutionVO> fetchMetadataListVO, ConfigVO configVO)
//			throws IOException {
//
//		File folder = new File(configVO.getScreenshotPath() + fetchMetadataListVO.get(0).getCustomerName() + "/"
//				+ fetchMetadataListVO.get(0).getTestRunName() + "/");
//		// File folder=new File("C:\\Users\\Winfo Solutions\\Desktop\\test");
//		File[] listOfFiles = folder.listFiles();
//		// String video_rec=configVO.getVideo_rec();
//		String video_rec = "no";
//		Map<Integer, List<File>> filesMap = new TreeMap<>();
//		int failcount = 0;
//		int passcount = 0;
//		for (File file : Arrays.asList(listOfFiles)) {
//
//			Integer seqNum = Integer.valueOf(file.getName().substring(0, file.getName().indexOf('_')));
//
//			if (!filesMap.containsKey(seqNum)) {
//
//				filesMap.put(seqNum, new ArrayList<File>());
//
//			}
//
//			filesMap.get(seqNum).add(file);
//
//		}
//
//		List<String> targetFileList = new ArrayList<>();
//		ArrayList<String> links = new ArrayList<String>();
//		String firstimagelink = null;
//		for (Entry<Integer, List<File>> seqEntry : filesMap.entrySet()) {
//
//			List<File> seqList = seqEntry.getValue();
//
//			Collections.sort(seqList, new Comparator<File>() {
//
//				public int compare(File f1, File f2) {
//
//					return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified()) * -1;
//
//				}
//
//			});
//
//			List<String> seqFileNameList = new ArrayList<String>();
//			ArrayList<String> links1 = new ArrayList<String>();
//			ArrayList<String> linksall = new ArrayList<String>();
//
//			File file = new ClassPathResource(whiteimage).getFile();
//			// File file = new File("C:\\Users\\Winfo
//			// Solutions\\Desktop\\Add_On\\white.jpg");
//			File file1 = new ClassPathResource(watsvediologo).getFile();
//			// File file1=new File("C:\\Users\\Winfo
//			// Solutions\\Desktop\\Add_On\\WATS_LOGO.JPG");
//
//			BufferedImage image = null;
//			image = ImageIO.read(file);
//			BufferedImage logo = null;
//			logo = ImageIO.read(file1);
//			Graphics g = image.getGraphics();
//			g.setColor(Color.black);
//			java.awt.Font font = new java.awt.Font("Calibri", java.awt.Font.PLAIN, 36);
//			g.setFont(font);
//			String details = seqList.get(0).getName();
//			String ScriptNumber = details.split("_")[3];
//			String TestRun = details.split("_")[4];
//			String Status = details.split("_")[6];
//			String status = Status.split("\\.")[0];
//			String Scenario = details.split("_")[2];
//			String imagename = TestRun + ScriptNumber;
//			String TName = fetchMetadataListVO.get(0).getTestRunName();
//			Date endtime = configVO.getEndtime();
//			Date TStarttime = configVO.getStarttime1();
//			DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//			String TStarttime1 = dateFormat.format(TStarttime);
////Changed the executed by variable
//			String ExeBy = fetchMetadataListVO.get(0).getExecutedBy();
//			String endtime1 = dateFormat.format(endtime);
//			long Tdiff = endtime.getTime() - TStarttime.getTime();
//			long TdiffSeconds = Tdiff / 1000 % 60;
//			long TdiffMinutes = Tdiff / (60 * 1000) % 60;
//			long TdiffHours = Tdiff / (60 * 60 * 1000);
//			String ExecutionTime = TdiffHours + ":" + TdiffMinutes + ":" + TdiffSeconds;
//
//			g.drawString("TEST SCRIPT DETAILS", 450, 50);
//			g.drawString("Test Run Name : " + TName, 50, 125);
//			g.drawString("Script Number : " + ScriptNumber, 50, 200);
//			g.drawString("Test Case Name :" + Scenario, 50, 275);
//			g.drawString("Status : " + status, 50, 350);
//			g.drawString("Executed By :" + ExeBy, 50, 425);
//			g.drawImage(logo, 1150, 15, null);
//////	    g.drawString("Start Time :"+TStarttime1, 50, 425);
//////	    g.drawString("End Time :"+endtime1, 50, 500);
//////	    g.drawString("Execution Time : "+ExecutionTime, 50, 575);
//			g.dispose();
//			File folder1 = new File(
//					configVO.getScreenshotPath() + fetchMetadataListVO.get(0).getCustomerName() + "/Images");
//			if (!folder1.exists()) {
//				System.out.println("creating directory: " + folder1.getName());
//				boolean result = false;
//				try {
//					folder1.mkdirs();
//					result = true;
//				} catch (SecurityException se) {
//					// handle it
//					System.out.println(se.getMessage());
//				}
//			} else {
//				System.out.println("Folder exist");
//			}
//
//			ImageIO.write(image, "jpg", new File(folder1 + "/" + imagename + ".jpg"));
//			// ImageIO.write(image, "jpg", new File("C:\\Users\\Winfo
//			// Solutions\\Desktop\\Add_On\\"+imagename+".jpg"));
//
//			BufferedImage image1 = null;
//			image1 = ImageIO.read(file);
//			Graphics g1 = image1.getGraphics();
//			g1.setColor(Color.red);
//			java.awt.Font font1 = new java.awt.Font("Calibri", java.awt.Font.PLAIN, 36);
//			g1.setFont(font1);
//			g1.drawImage(logo, 1012, 14, null);
//			g1.drawString("FAILED IN THE NEXT STEP!!", 400, 300);
//			g1.dispose();
//			ImageIO.write(image1, "jpg", new File(folder1 + "/last.jpg"));
//			// ImageIO.write(image1, "jpg", new File("C:\\Users\\Winfo
//			// Solutions\\Desktop\\Add_On\\last.jpg"));
//
//			BufferedImage image2 = null;
//			image2 = ImageIO.read(file);
//			Graphics g2 = image2.getGraphics();
//			g2.setColor(Color.black);
//			g2.setFont(font);
//			g2.drawString("TEST RUN SUMMARY", 50, 50);
//			g2.drawString("Test Run Name : " + TName, 50, 125);
//			g2.drawString("Executed By :" + ExeBy, 50, 200);
//			g2.drawString("Start Time :" + TStarttime1, 50, 275);
//			g2.drawString("End Time :" + endtime1, 50, 350);
//			g2.drawString("Execution Time : " + ExecutionTime, 50, 425);
//			g2.drawImage(logo, 1012, 15, null);
//			g2.dispose();
//			ImageIO.write(image2, "jpg", new File(folder1 + "/first.jpg"));
//			String imgpath3 = folder1 + "/first.jpg";
//			String imgpath2 = folder1 + "/";
//
//			// ImageIO.write(image2, "jpg", new File("C:\\Users\\Winfo
//			// Solutions\\Desktop\\Add_On\\first.jpg"));
//			// String imgpath3 ="C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\first.jpg";
//			// String imgpath2 ="C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\";
//			File f11 = new File(imgpath2);
//			File[] f22 = f11.listFiles();
//			File f44 = new File(imgpath3);
//			firstimagelink = f44.getAbsolutePath();
//
//			if (seqList.get(0).getName().endsWith("Failed.jpg")) {
//				failcount++;
//				for (File f33 : f22) {
//					if (f33.getAbsolutePath().contains(imagename)) {
//						linksall.add(f33.getAbsolutePath());
//						linksall.set(0, f33.getAbsolutePath());
//					}
//					if (f33.getAbsolutePath().contains("last")) {
//						linksall.add(f33.getAbsolutePath());
//						linksall.add(f33.getAbsolutePath());
//						linksall.set(1, f33.getAbsolutePath());
//
//					}
//				}
////                                   System.out.println("SEQ : "+seqEntry.getKey());
//				links1.add(seqList.get(0).getAbsolutePath());
//				links1.add(linksall.get(1));
//				seqFileNameList.add(seqList.get(0).getName());
//
//				for (int i = 1; i < seqList.size(); i++) {
//
//					if (!seqList.get(i).getName().endsWith("Failed.jpg")) {
//						links1.add(seqList.get(i).getAbsolutePath());
//						seqFileNameList.add(seqList.get(i).getName());
//
//					} else {
//
//					}
//
//				}
//				links1.add(linksall.get(0));
//				Collections.reverse(links1);
//				Collections.reverse(seqFileNameList);
//				links.addAll(links1);
//				targetFileList.addAll(seqFileNameList);
//
//			}
//
////                    targetFileList.addAll(seqList);
//
//		}
////
////		/*
////		 * for (String fileName : targetFileList) {
////		 * 
////		 * System.out.println("Target File : " + fileName);
////		 * 
////		 * }
////		 */
//		configVO.setPasscount(passcount);
//		configVO.setFailcount(failcount);
//		if (video_rec.equalsIgnoreCase("yes")) {
//			convertJPGtoMovie(firstimagelink, links, fetchMetadataListVO, configVO, "Failed_Video.mp4");
//		}
//		return targetFileList;
//
//	}
//
//	public List<String> getDetailPdfNew(List<TestRunExecutionVO> fetchMetadataListVO, ConfigVO configVO)
//			throws IOException {
//
//		File folder = new File(configVO.getScreenshotPath() + fetchMetadataListVO.get(0).getCustomerName() + "/"
//				+ fetchMetadataListVO.get(0).getTestRunName() + "/");
//		// File folder=new File("C:\\Users\\Winfo Solutions\\Desktop\\test");
//		File[] listOfFiles = folder.listFiles();
////		String video_rec=configVO.getEnable_video();
//		String video_rec = "no";
//		Map<Integer, List<File>> filesMap = new TreeMap<>();
//		int failcount = 0;
//		int passcount = 0;
//		for (File file : Arrays.asList(listOfFiles)) {
//
//			Integer seqNum = Integer.valueOf(file.getName().substring(0, file.getName().indexOf('_')));
//
//			if (!filesMap.containsKey(seqNum)) {
//
//				filesMap.put(seqNum, new ArrayList<File>());
//
//			}
//
//			filesMap.get(seqNum).add(file);
//
//		}
//
//		List<String> targetFileList = new ArrayList<>();
//		ArrayList<String> finalLinks = new ArrayList<String>();
//		List<String> targetSuccessFileList = new ArrayList<>();
//		ArrayList<String> links = new ArrayList<String>();
//		ArrayList<String> links2 = new ArrayList<String>();
//		List<String> targetFailedFileList = new ArrayList<>();
//		String firstimagelink = null;
//		String TName = fetchMetadataListVO.get(0).getTestRunName();
//		for (Entry<Integer, List<File>> seqEntry : filesMap.entrySet()) {
//
//			List<File> seqList = seqEntry.getValue();
//
//			Collections.sort(seqList, new Comparator<File>() {
//
//				public int compare(File f1, File f2) {
//
//					return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified()) * -1;
//
//				}
//
//			});
//
//			List<String> seqFileNameList = new ArrayList<String>();
//			ArrayList<String> links1 = new ArrayList<String>();
//
//			ArrayList<String> linksall = new ArrayList<String>();
//
//			File file = new ClassPathResource(whiteimage).getFile();
//			File file1 = new ClassPathResource(watsvediologo).getFile();
//			// File file = new File("C:\\Users\\Winfo
//			// Solutions\\Desktop\\Add_On\\white.jpg");
//			// File file1=new File("C:\\Users\\Winfo
//			// Solutions\\Desktop\\Add_On\\WATS_LOGO.JPG");
//			BufferedImage image = null;
//			image = ImageIO.read(file);
//			BufferedImage logo = null;
//			logo = ImageIO.read(file1);
//			Graphics g = image.getGraphics();
//			g.setColor(Color.black);
//			java.awt.Font font = new java.awt.Font("Calibri", java.awt.Font.PLAIN, 36);
//			g.setFont(font);
//
//			String details = seqList.get(0).getName();
//			String ScriptNumber = details.split("_")[3];
//			String TestRun = details.split("_")[4];
//			String Status = details.split("_")[6];
//			String status = Status.split("\\.")[0];
//			String Scenario = details.split("_")[2];
//			String imagename = TestRun + ScriptNumber;
//			// String TName = fetchMetadataListVO.get(0).getTestRunName();
//			Date endtime = configVO.getEndtime();
//			Date TStarttime = configVO.getStarttime1();
//			DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//			String TStarttime1 = dateFormat.format(TStarttime);
////Changed the executed by variable
//			String ExeBy = fetchMetadataListVO.get(0).getExecutedBy();
//			String endtime1 = dateFormat.format(endtime);
//			long Tdiff = endtime.getTime() - TStarttime.getTime();
//			long TdiffSeconds = Tdiff / 1000 % 60;
//			long TdiffMinutes = Tdiff / (60 * 1000) % 60;
//			long TdiffHours = Tdiff / (60 * 60 * 1000);
//			String ExecutionTime = TdiffHours + ":" + TdiffMinutes + ":" + TdiffSeconds;
//			g.drawString("TEST SCRIPT DETAILS", 450, 50);
//			g.drawString("Test Run Name : " + TName, 50, 125);
//			g.drawString("Script Number : " + ScriptNumber, 50, 200);
//			g.drawString("Test Case Name :" + Scenario, 50, 275);
//			g.drawString("Status : " + status, 50, 350);
//			g.drawString("Executed By :" + ExeBy, 50, 425);
//			g.drawImage(logo, 1012, 15, null);
//			// g.drawString("Start Time :"+TStarttime1, 50, 425);
//			// g.drawString("End Time :"+endtime1, 50, 500);
//			// g.drawString("Execution Time : "+ExecutionTime, 50, 575);
//			g.dispose();
//			File folder1 = new File(
//					configVO.getScreenshotPath() + fetchMetadataListVO.get(0).getCustomerName() + "/Images");
//			if (!folder1.exists()) {
//				System.out.println("creating directory: " + folder1.getName());
//				boolean result = false;
//				try {
//					folder1.mkdirs();
//					result = true;
//				} catch (SecurityException se) {
//					// handle it
//					System.out.println(se.getMessage());
//				}
//			} else {
//				System.out.println("Folder exist");
//			}
//
//			ImageIO.write(image, "jpg", new File(folder1 + "/" + imagename + ".jpg"));
//			// ImageIO.write(image, "jpg", new File("C:\\Users\\Winfo
//			// Solutions\\Desktop\\Add_On\\"+imagename+".jpg"));
//
//			BufferedImage image1 = null;
//			image1 = ImageIO.read(file);
//			Graphics g1 = image1.getGraphics();
//			g1.setColor(Color.red);
//			java.awt.Font font1 = new java.awt.Font("Calibri", java.awt.Font.PLAIN, 36);
//			g1.setFont(font1);
//			g1.drawString("FAILED IN THE NEXT STEP!!", 400, 300);
//			g1.drawImage(logo, 1012, 14, null);
//			g1.dispose();
//
//			BufferedImage image2 = null;
//			image2 = ImageIO.read(file);
//			Graphics g2 = image2.getGraphics();
//			g2.setColor(Color.black);
//			g2.setFont(font);
//			g2.drawString("TEST RUN SUMMARY", 450, 50);
//			g2.drawString("Test Run Name : " + TName, 50, 125);
//			g2.drawString("Executed By :" + ExeBy, 50, 200);
//			g2.drawString("Start Time :" + TStarttime1, 50, 275);
//			g2.drawString("End Time :" + endtime1, 50, 350);
//			g2.drawString("Execution Time : " + ExecutionTime, 50, 425);
//			g2.drawImage(logo, 1012, 15, null);
//			g2.dispose();
//			ImageIO.write(image2, "jpg", new File(folder1 + "/first.jpg"));
//			// ImageIO.write(image2, "jpg", new File("C:\\Users\\Winfo
//			// Solutions\\Desktop\\Add_On\\first.jpg"));
//			String imgpath2 = folder1 + "/";
//			// String imgpath2 ="C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\";
//			String imgpath3 = folder1 + "/first.jpg";
//			// String imgpath3 ="C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\first.jpg";
//			File f11 = new File(imgpath2);
//			File[] f22 = f11.listFiles();
//			File f44 = new File(imgpath3);
//			firstimagelink = f44.getAbsolutePath();
//
//			if (!seqList.get(0).getName().endsWith("Failed.jpg")) {
//				passcount++;
//				for (File f33 : f22) {
//					if (f33.getAbsolutePath().contains(imagename)) {
//						linksall.add(f33.getAbsolutePath());
//					}
//				}
//				links1.add(seqList.get(0).getAbsolutePath());
//
//				seqFileNameList.add(seqList.get(0).getName());
//
////			             	                      System.out.println("FIRST S STEP: "+seqList.get(0).getName());
//
//				for (int i = 1; i < seqList.size(); i++) {
//
//					if (!seqList.get(i).getName().endsWith("Failed.jpg")) {
//						links1.add(seqList.get(i).getAbsolutePath());
//
//						seqFileNameList.add(seqList.get(i).getName());
//
////			                                                                 System.out.println("S STEP: "+seqList.get(i).getName());
//
//					} else {
//
//					}
//
//				}
//				links1.add(linksall.get(0));
//				Collections.reverse(links1);
//				Collections.reverse(seqFileNameList);
//				links.addAll(links1);
//				targetSuccessFileList.addAll(seqFileNameList);
//
//			} else {
//				failcount++;
//				for (File f33 : f22) {
//					if (f33.getAbsolutePath().contains(imagename)) {
//						linksall.add(f33.getAbsolutePath());
//						linksall.set(0, f33.getAbsolutePath());
//					}
//					if (f33.getAbsolutePath().contains("last")) {
//						linksall.add(f33.getAbsolutePath());
//						linksall.add(f33.getAbsolutePath());
//						linksall.set(1, f33.getAbsolutePath());
//
//					}
//				}
////			                                   System.out.println("SEQ : "+seqEntry.getKey());
//				links1.add(seqList.get(0).getAbsolutePath());
//				links1.add(linksall.get(1));
////			                                   System.out.println("SEQ : "+seqEntry.getKey());
//
//				seqFileNameList.add(seqList.get(0).getName());
//
////			                                   System.out.println("FIRST F STEP: "+seqList.get(0).getName());
//
//				for (int i = 1; i < seqList.size(); i++) {
//
//					if (!seqList.get(i).getName().endsWith("Failed.jpg")) {
//						links1.add(seqList.get(i).getAbsolutePath());
//
//						seqFileNameList.add(seqList.get(i).getName());
//
////			                                                                 System.out.println("F STEP: "+seqList.get(i).getName());
//
//					} else {
//
//					}
//
//				}
//
//				links1.add(linksall.get(0));
//				Collections.reverse(links1);
//				Collections.reverse(seqFileNameList);
//				links2.addAll(links1);
//				targetFailedFileList.addAll(seqFileNameList);
//
//			}
//
////			                    targetFileList.addAll(seqList);
//
//		}
//
//		finalLinks.addAll(links);
//		finalLinks.addAll(links2);
//		targetFileList.addAll(targetSuccessFileList);
//
//		targetFileList.addAll(targetFailedFileList);
//
//		/*
//		 * for (String fileName : targetFileList) {
//		 * 
//		 * System.out.println("Target File : " + fileName);
//		 * 
//		 * }
//		 */
//		configVO.setPasscount(passcount);
//		configVO.setFailcount(failcount);
//		if (video_rec.equalsIgnoreCase("Y")) {
//
//			convertJPGtoMovie(firstimagelink, finalLinks, fetchMetadataListVO, configVO, TName + ".mp4");
//		}
//		return targetFileList;
//	}
//
//	public void convertJPGtoMovie(String targetFile1, List<String> targetFileList,
//			List<TestRunExecutionVO> fetchMetadataListVO, ConfigVO configVO, String name) {
//		String vidPath = (configVO.getPdfPath() + fetchMetadataListVO.get(0).getCustomerName() + "/"
//				+ fetchMetadataListVO.get(0).getTestRunName() + "/" + name);
//		// String vidPath="C:\\Testing\\ReportWinfo\\"+name;
//		String Folder = (configVO.getPdfPath() + fetchMetadataListVO.get(0).getCustomerName() + "/"
//				+ fetchMetadataListVO.get(0).getTestRunName() + "/");
//		File theDir = new File(Folder);
//		if (!theDir.exists()) {
//			System.out.println("creating directory: " + theDir.getName());
//			boolean result = false;
//			try {
//				theDir.mkdirs();
//				result = true;
//			} catch (SecurityException se) {
//				// handle it
//				System.out.println(se.getMessage());
//			}
//		} else {
//			System.out.println("Folder exist");
//		}
//		// String vidPath = "C:\\Users\\Winfo Solutions\\Desktop\\"+name;
//		OpenCVFrameConverter.ToIplImage grabberConverter = new OpenCVFrameConverter.ToIplImage();
//		FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(vidPath, 1366, 614);
//		String str = null;
//		IplImage ipl = cvLoadImage(str);
//		try {
//			recorder.setFrameRate(0.33);
//			recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
//			recorder.setVideoBitrate(9000);
//			recorder.setFormat("mp4");
//			recorder.setVideoQuality(0); // maximum quality
//			recorder.start();
////             for (int i=0;i<targetFileList.size();i++)
////             {
////            	 System.out.println(targetFileList.get(i));
////             }
//			if (targetFile1 != null) {
//				System.out.println(targetFile1);
//				str = targetFile1;
//				ipl = cvLoadImage(str);
//				recorder.record(grabberConverter.convert(ipl));
//			}
//			for (String image : targetFileList) {
//				System.out.println(image);
//				str = image;
//				ipl = cvLoadImage(str);
//				recorder.record(grabberConverter.convert(ipl));
//			}
//			recorder.stop();
//			System.out.println("ok");
//		} catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public List<String> getFailFileNameList(List<TestRunExecutionVO> fetchMetadataListVO, ConfigVO configVO) {
//		List<String> fileNameList = new ArrayList<String>();
//		File folder = new File(configVO.getScreenshotPath() + fetchMetadataListVO.get(0).getCustomerName() + "/"
//				+ fetchMetadataListVO.get(0).getTestRunName() + "/");
//		File[] listOfFiles = folder.listFiles();
//		Arrays.sort(listOfFiles, new Comparator<File>() {
//			public int compare(File f1, File f2) {
//				return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
//
//			}
//		});
//		String seqNumber = String.valueOf( fetchMetadataListVO.get(0).getSeqNum());
//		String scripNumber = fetchMetadataListVO.get(0).getScriptNumber();
//		String Number =  String.valueOf( fetchMetadataListVO.get(0).getLineNumber());
//		for (int i = 0; i < listOfFiles.length; i++) {
//			if (listOfFiles[i].isFile()) {
//				System.out.println("File " + listOfFiles[i].getName());
//				String fileName = listOfFiles[i].getName();
//				String[] fileNameArr = fileName.split("\\.");
//				String fileExt = fileNameArr[fileNameArr.length - 1];
//				String[] _arr = fileName.split("_");
//				String currentScriptNumber = _arr[3];
//				String seq = _arr[0];
//				String Status = _arr[6];
//				String status = Status.split("\\.")[0];
//				if ("jpg".equalsIgnoreCase(fileExt) && scripNumber.equalsIgnoreCase(currentScriptNumber)
//						&& seqNumber.equalsIgnoreCase(seq)) {
//					fileNameList.add(fileName);
//				}
//			}
//		}
//		return fileNameList;
//	}
//
//	public List<String> getFileNameList(List<TestRunExecutionVO> fetchMetadataListVO, ConfigVO configVO) {
//		List<String> fileNameList = new ArrayList<String>();
//		File folder = new File(configVO.getScreenshotPath() + fetchMetadataListVO.get(0).getCustomerName() + "/"
//				+ fetchMetadataListVO.get(0).getTestRunName() + "/");
//		File[] listOfFiles = folder.listFiles();
//		Arrays.sort(listOfFiles, new Comparator<File>() {
//			public int compare(File f1, File f2) {
//				return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
//
//			}
//		});
//		String seqNumber = String.valueOf( fetchMetadataListVO.get(0).getSeqNum());
//		String scripNumber = fetchMetadataListVO.get(0).getScriptNumber();
//		String Number = String.valueOf(fetchMetadataListVO.get(0).getLineNumber());
//		for (int i = 0; i < listOfFiles.length; i++) {
//			if (listOfFiles[i].isFile()) {
//				System.out.println("File " + listOfFiles[i].getName());
//				String fileName = listOfFiles[i].getName();
//				String[] fileNameArr = fileName.split("\\.");
//				String fileExt = fileNameArr[fileNameArr.length - 1];
//				String[] _arr = fileName.split("_");
//				String currentScriptNumber = _arr[3];
//				String seq = _arr[0];
//				String Status = _arr[6];
//				String status = Status.split("\\.")[0];
//				if ("jpg".equalsIgnoreCase(fileExt) && scripNumber.equalsIgnoreCase(currentScriptNumber)
//						&& "Passed".equalsIgnoreCase(status) && seqNumber.equalsIgnoreCase(seq)) {
//					fileNameList.add(fileName);
//				}
//			}
//		}
//		return fileNameList;
//	}
//
//	public List<String> getPassedPdf(List<TestRunExecutionVO> fetchMetadataListVO, ConfigVO configVO) {
//		List<String> fileNameList = new ArrayList<String>();
//		File folder = new File(configVO.getScreenshotPath() + fetchMetadataListVO.get(0).getCustomerName() + "/"
//				+ fetchMetadataListVO.get(0).getTestRunName() + "/");
//		File[] listOfFiles = folder.listFiles();
//		Arrays.sort(listOfFiles, new Comparator<File>() {
//			public int compare(File f1, File f2) {
//				return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
//
//			}
//		});
//		String seqNumber = String.valueOf( fetchMetadataListVO.get(0).getSeqNum());
//		String scripNumber = fetchMetadataListVO.get(0).getScriptNumber();
//		String STATUS = fetchMetadataListVO.get(0).getStatus();
//		for (int i = 0; i < listOfFiles.length; i++) {
//			if (listOfFiles[i].isFile()) {
//				System.out.println("File " + listOfFiles[i].getName());
//				String fileName = listOfFiles[i].getName();
//				String[] fileNameArr = fileName.split("\\.");
//				String fileExt = fileNameArr[fileNameArr.length - 1];
//				String[] _arr = fileName.split("_");
//				String currentScriptNumber = _arr[3];
//				String seq = _arr[0];
//				String Status = _arr[6];
//				String status = Status.split("\\.")[0];
//				if ("jpg".equalsIgnoreCase(fileExt) && "Passed".equalsIgnoreCase(status)) {
//					fileNameList.add(fileName);
//				}
//			}
//		}
//		return fileNameList;
//	}
//
//	public List<String> getFailedPdf(List<TestRunExecutionVO> fetchMetadataListVO, ConfigVO configVO) {
//
//		List<String> fileNameList = new ArrayList<String>();
//		File folder = new File(configVO.getScreenshotPath() + fetchMetadataListVO.get(0).getCustomerName() + "/"
//				+ fetchMetadataListVO.get(0).getTestRunName() + "/");
//		File[] listOfFiles = folder.listFiles();
//		Arrays.sort(listOfFiles, new Comparator<File>() {
//			public int compare(File f1, File f2) {
//				return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
//
//			}
//		});
//		String scripNumber = fetchMetadataListVO.get(0).getScriptNumber();
//		for (int i = 0; i < listOfFiles.length; i++) {
//			if (listOfFiles[i].isFile()) {
//				System.out.println("File " + listOfFiles[i].getName());
//				String fileName = listOfFiles[i].getName();
//				String[] fileNameArr = fileName.split("\\.");
//				String fileExt = fileNameArr[fileNameArr.length - 1];
//				String[] _arr = fileName.split("_");
//				String currentScriptNumber = _arr[3];
//				String Status = _arr[6];
//				String status = Status.split("\\.")[0];
//				if ("jpg".equalsIgnoreCase(fileExt) && ("Failed".equalsIgnoreCase(status))) {
//					fileNameList.add(fileName);
//				}
//			}
//		}
//		return fileNameList;
//	}
//
//	public List<String> getDetailPdf(List<TestRunExecutionVO> fetchMetadataListVO, ConfigVO configVO) {
//		List<String> fileNameList = new ArrayList<String>();
//		File folder = new File(configVO.getScreenshotPath() + fetchMetadataListVO.get(0).getCustomerName() + "/"
//				+ fetchMetadataListVO.get(0).getTestRunName() + "/");
//		File[] listOfFiles = folder.listFiles();
//		Arrays.sort(listOfFiles, new Comparator<File>() {
//			public int compare(File f1, File f2) {
//				return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
//
//			}
//		});
//		String scripNumber = fetchMetadataListVO.get(0).getScriptNumber();
//		for (int i = 0; i < listOfFiles.length; i++) {
//			if (listOfFiles[i].isFile()) {
//				System.out.println("File " + listOfFiles[i].getName());
//				String fileName = listOfFiles[i].getName();
//				String[] fileNameArr = fileName.split("\\.");
//				String fileExt = fileNameArr[fileNameArr.length - 1];
//				String[] _arr = fileName.split("_");
//				String currentScriptNumber = _arr[3];
//				String Status = _arr[6];
//				String status = Status.split("\\.")[0];
//				if ("jpg".equalsIgnoreCase(fileExt)) {
//					fileNameList.add(fileName);
//				}
//			}
//		}
//		return fileNameList;
//	}
//
//	public void createPdf(List<TestRunExecutionVO> fetchMetadataListVO, ConfigVO configVO, String pdffileName,
//			Date Starttime, Date endtime) throws IOException, DocumentException, com.itextpdf.text.DocumentException {
//		try {
//			String Date = DateUtils.getSysdate();
//			String Folder = (configVO.getPdfPath() + fetchMetadataListVO.get(0).getCustomerName() + "/"
//					+ fetchMetadataListVO.get(0).getTestRunName() + "/");
//			// String Folder="C:\\Users\\Winfo Solutions\\Desktop\\new\\";
////			String Folder = "/objstore/udgsup/UDG SUPPORT/UDG - PPM  (copy)/";
//			String FILE = (Folder + pdffileName);
//			System.out.println(FILE);
//			List<String> fileNameList = null;
//			if ("Passed_Report.pdf".equalsIgnoreCase(pdffileName)) {
//				fileNameList = getPassedPdfNew(fetchMetadataListVO, configVO);
//			} else if ("Failed_Report.pdf".equalsIgnoreCase(pdffileName)) {
//				fileNameList = getFailedPdfNew(fetchMetadataListVO, configVO);
//			} else if ("Detailed_Report.pdf".equalsIgnoreCase(pdffileName)) {
//				fileNameList = getDetailPdfNew(fetchMetadataListVO, configVO);
//			} else {
//				fileNameList = getFileNameListNew(fetchMetadataListVO, configVO);
//			}
//			String Script_Number = fetchMetadataListVO.get(0).getScriptNumber();
//			String customer_Name = fetchMetadataListVO.get(0).getCustomerName();
//			String test_Run_Name = fetchMetadataListVO.get(0).getTestRunName();
//			String Scenario_Name = fetchMetadataListVO.get(0).getScenarioName();
//			int testRunId=fetchMetadataListVO.get(0).getTestRunId();
//			// new change add ExecutedBy field
//			String ExecutedBy = fetchMetadataListVO.get(0).getExecutedBy();
//			String ScriptDescription1 = fetchMetadataListVO.get(0).getScenarioName();
//			File theDir = new File(Folder);
//			if (!theDir.exists()) {
//				System.out.println("creating directory: " + theDir.getName());
//				boolean result = false;
//				try {
//					theDir.mkdirs();
//					result = true;
//				} catch (SecurityException se) {
//					// handle it
//					System.out.println(se.getMessage());
//				}
//			} else {
//				System.out.println("Folder exist");
//			}
//			int passcount = configVO.getPasscount();
//			int failcount = configVO.getFailcount();
////			Date Starttime = configVO.getStarttime();
//			Date Tendtime = configVO.getEndtime();
//			Date TStarttime = configVO.getStarttime1();
//			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:aa");
//
//			String TStarttime1 = dateFormat.format(TStarttime);
//			String Tendtime1 = dateFormat.format(Tendtime);
//			long Tdiff = Tendtime.getTime() - TStarttime.getTime();
//
//			Document document = new Document();
//			String start = "Execution Summary";
//			String pichart = "Pie-Chart";
//			String Report = "Execution Report";
//			Font bfBold12 = FontFactory.getFont("Arial", 23);
//			Font fnt = FontFactory.getFont("Arial", 12);
//			Font bf12 = FontFactory.getFont("Arial", 23);
//			Font bf15 = FontFactory.getFont("Arial", 23, Font.UNDERLINE);
//			Font bf16 = FontFactory.getFont("Arial", 12, Font.UNDERLINE);
//			Font bf13 = FontFactory.getFont("Arial", 23, Font.UNDERLINE, BaseColor.GREEN);
//			Font bf14 = FontFactory.getFont("Arial", 23, Font.UNDERLINE, BaseColor.RED);
//			Font bfBold = FontFactory.getFont("Arial", 23, BaseColor.WHITE);
//			DefaultPieDataset dataSet = new DefaultPieDataset();
//			PdfWriter writer = null;
//			writer = PdfWriter.getInstance(document, new FileOutputStream(FILE));
//			Rectangle one = new Rectangle(1360, 800);
//			document.setPageSize(one);
//			document.open();
//			System.out.println("before enter Images/wats_icon.png1");
//			Image img1 = Image.getInstance(watslogo);
//			System.out.println("after enter Images/wats_icon.png1");
//
//			img1.scalePercent(65, 68);
//			img1.setAlignment(Image.ALIGN_RIGHT);
////		start to create testrun level reports	
//			if ((passcount != 0 || failcount != 0) & ("Passed_Report.pdf".equalsIgnoreCase(pdffileName)
//					|| "Failed_Report.pdf".equalsIgnoreCase(pdffileName)
//					|| "Detailed_Report.pdf".equalsIgnoreCase(pdffileName))) {
////	     Start testrun to add details like start and end time,testrun name
//				String TestRun = TestRun = test_Run_Name;
//				;
//				String StartTime = null;
//				String EndTime = Tendtime1;
//				String ExecutionTime = null;
//				Date date = new Date();
//				Timestamp startTimestamp = new Timestamp(TStarttime.getTime());
//				Timestamp endTimestamp = new Timestamp(Tendtime.getTime());
//
//				Map<Date, Long> timeslist =  testRunScriptExecService.getTestRunStartAndExecutionTime(fetchMetadataListVO.get(0).getTestRunId());
//				if (timeslist.size() == 0) {
//					StartTime = TStarttime1;
//					long TdiffSeconds = Tdiff / 1000 % 60;
//					long TdiffMinutes = Tdiff / (60 * 1000) % 60;
//					long TdiffHours = Tdiff / (60 * 60 * 1000);
//					ExecutionTime = TdiffHours + ":" + TdiffMinutes + ":" + TdiffSeconds;
//					if ("Detailed_Report.pdf".equalsIgnoreCase(pdffileName)) {
//						testRunScriptExecService.updateTestRunStartAndEndTime(startTimestamp, endTimestamp, Tdiff,fetchMetadataListVO.get(0).getTestRunId());
//
//					}
//				} else {
//					for (Entry<Date, Long> entryMap : timeslist.entrySet()) {
//						StartTime = dateFormat.format(entryMap.getKey());
//						long totalTime = Tdiff + entryMap.getValue();
//						long TdiffSeconds = totalTime / 1000 % 60;
//						long TdiffMinutes = totalTime / (60 * 1000) % 60;
//						long TdiffHours = totalTime / (60 * 60 * 1000);
//						ExecutionTime = TdiffHours + ":" + TdiffMinutes + ":" + TdiffSeconds;
//						if ("Detailed_Report.pdf".equalsIgnoreCase(pdffileName)) {
//
//							testRunScriptExecService.updateTestRunEndTime(endTimestamp, totalTime,fetchMetadataListVO.get(0).getTestRunId());
//
//						}
//					}
//				}
//				String TR = "Test Run Name";
//				String SN = "Executed By";
//				String SN1 = "Start Time";
//				String S1 = "End Time";
//				String Scenarios1 = "Execution Time";
//
//				document.add(img1);
//				document.add(new Paragraph(Report, bfBold12));
//				document.add(Chunk.NEWLINE);
//				PdfPTable table1 = new PdfPTable(2);
//				table1.setWidths(new int[] { 1, 1 });
//				table1.setWidthPercentage(100f);
//				insertCell(table1, TR, Element.ALIGN_LEFT, 1, bf12);
//				insertCell(table1, TestRun, Element.ALIGN_LEFT, 1, bf12);
//				insertCell(table1, SN, Element.ALIGN_LEFT, 1, bf12);
//				insertCell(table1, ExecutedBy, Element.ALIGN_LEFT, 1, bf12);
//				insertCell(table1, SN1, Element.ALIGN_LEFT, 1, bf12);
//				insertCell(table1, StartTime, Element.ALIGN_LEFT, 1, bf12);
//				insertCell(table1, S1, Element.ALIGN_LEFT, 1, bf12);
//				insertCell(table1, EndTime, Element.ALIGN_LEFT, 1, bf12);
//				insertCell(table1, Scenarios1, Element.ALIGN_LEFT, 1, bf12);
//				insertCell(table1, ExecutionTime, Element.ALIGN_LEFT, 1, bf12);
//				document.add(table1);
////	   End testrun to add details like start and end time,testrun name 	
//
////					Start Testrun to add Table and piechart 		 
//				if (passcount == 0) {
//
//					dataSet.setValue("Fail", failcount);
//				} else if (failcount == 0) {
//					dataSet.setValue("Pass", passcount);
//				} else {
//					dataSet.setValue("Pass", passcount);
//					dataSet.setValue("Fail", failcount);
//				}
//				double pass = Math.round((passcount * 100.0) / (passcount + failcount));
//				double fail = Math.round((failcount * 100.0) / (passcount + failcount));
//				Rectangle one1 = new Rectangle(1360, 1000);
//				if ("Detailed_Report.pdf".equalsIgnoreCase(pdffileName)) {
//					
//					document.setPageSize(one1);
//
//					document.newPage();
//					document.add(img1);
//					Paragraph executionSummery=new Paragraph(start, bfBold12);
//					executionSummery.setAlignment(Element.ALIGN_CENTER);
//					document.add(executionSummery);
//					document.add(Chunk.NEWLINE);
//					DecimalFormat df1 = new DecimalFormat("0");
//					DecimalFormat df2 = new DecimalFormat("0");
////			Start Testrun to add Table   	 
//					PdfPTable table = new PdfPTable(3);
//					table.setWidths(new int[] { 1, 1, 1 });
//					table.setWidthPercentage(100f);
//					insertCell(table, "Status", Element.ALIGN_CENTER, 1, bfBold12);
//					insertCell(table, "Total", Element.ALIGN_CENTER, 1, bfBold12);
//					insertCell(table, "Percentage", Element.ALIGN_CENTER, 1, bfBold12);
//					PdfPCell[] cells1 = table.getRow(0).getCells();
//					for (int k = 0; k < cells1.length; k++) {
//						cells1[k].setBackgroundColor(new BaseColor(161, 190, 212));
//					}
//					insertCell(table, "Passed", Element.ALIGN_CENTER, 1, bf12);
//					insertCell(table, df1.format(passcount), Element.ALIGN_CENTER, 1, bf12);
//					insertCell(table, df2.format(pass) + "%", Element.ALIGN_CENTER, 1, bf12);
//					insertCell(table, "Failed", Element.ALIGN_CENTER, 1, bf12);
//					insertCell(table, df1.format(failcount), Element.ALIGN_CENTER, 1, bf12);
//					insertCell(table, df2.format(fail) + "%", Element.ALIGN_CENTER, 1, bf12);
//					document.setMargins(20, 20, 20, 20);
//					document.add(table);
//				} else if ("Passed_Report.pdf".equalsIgnoreCase(pdffileName)) {
//					document.add(Chunk.NEWLINE);
//					Paragraph executionSummery=new Paragraph(start, bfBold12);
//					executionSummery.setAlignment(Element.ALIGN_CENTER);
//					document.add(executionSummery);
//					document.add(Chunk.NEWLINE);
//					DecimalFormat df1 = new DecimalFormat("0");
//					DecimalFormat df2 = new DecimalFormat("0");
////					Start Testrun to add Table   	 
//					PdfPTable table = new PdfPTable(3);
//					table.setWidths(new int[] { 1, 1, 1 });
//					table.setWidthPercentage(100f);
//					insertCell(table, "Status", Element.ALIGN_CENTER, 1, bfBold12);
//					insertCell(table, "Total", Element.ALIGN_CENTER, 1, bfBold12);
//					insertCell(table, "Percentage", Element.ALIGN_CENTER, 1, bfBold12);
//					PdfPCell[] cells1 = table.getRow(0).getCells();
//					for (int k = 0; k < cells1.length; k++) {
//						cells1[k].setBackgroundColor(new BaseColor(161, 190, 212));
//					}
//
//					insertCell(table, "Passed", Element.ALIGN_CENTER, 1, bf12);
//					insertCell(table, df1.format(passcount), Element.ALIGN_CENTER, 1, bf12);
//					insertCell(table, df2.format(pass) + "%", Element.ALIGN_CENTER, 1, bf12);
//					document.setMargins(20, 20, 20, 20);
//					document.add(table);
//
//				} else {
//					document.add(Chunk.NEWLINE);
//					Paragraph executionSummery=new Paragraph(start, bfBold12);
//					executionSummery.setAlignment(Element.ALIGN_CENTER);
//					document.add(executionSummery);
//					document.add(Chunk.NEWLINE);
//					DecimalFormat df1 = new DecimalFormat("0");
//					DecimalFormat df2 = new DecimalFormat("0");
////							Start Testrun to add Table   	 
//					PdfPTable table = new PdfPTable(3);
//					table.setWidths(new int[] { 1, 1, 1 });
//					table.setWidthPercentage(100f);
//					insertCell(table, "Status", Element.ALIGN_CENTER, 1, bfBold12);
//					insertCell(table, "Total", Element.ALIGN_CENTER, 1, bfBold12);
//					insertCell(table, "Percentage", Element.ALIGN_CENTER, 1, bfBold12);
//					PdfPCell[] cells1 = table.getRow(0).getCells();
//					for (int k = 0; k < cells1.length; k++) {
//						cells1[k].setBackgroundColor(new BaseColor(161, 190, 212));
//					}
//
//					insertCell(table, "Failed", Element.ALIGN_CENTER, 1, bf12);
//					insertCell(table, df1.format(failcount), Element.ALIGN_CENTER, 1, bf12);
//					insertCell(table, df2.format(fail) + "%", Element.ALIGN_CENTER, 1, bf12);
//					document.setMargins(20, 20, 20, 20);
//					document.add(table);
//				}
////			End Testrun to add Table
////			Start Testrun to add piechart 
//				if ("Detailed_Report.pdf".equalsIgnoreCase(pdffileName)) {
//					Chunk ch = new Chunk(pichart, bfBold);
//					ch.setTextRise(-18);
//					ch.setBackground(new BaseColor(38, 99, 175), 0f, 10f, 1730f, 15f);
//
//					Paragraph p1 = new Paragraph(ch);
//					p1.setSpacingBefore(50);
//					document.add(p1);
//
//					JFreeChart chart = ChartFactory.createPieChart(" ", dataSet, true, true, false);
//					Color c1 = new Color(102, 255, 102);
//					Color c = new Color(253, 32, 32);
//
//					LegendTitle legend = chart.getLegend();
//					PiePlot piePlot = (PiePlot) chart.getPlot();
//					piePlot.setSectionPaint("Pass", c1);
//					piePlot.setSectionPaint("Fail", c);
//					piePlot.setBackgroundPaint(Color.WHITE);
//					piePlot.setOutlinePaint(null);
//					piePlot.setLabelBackgroundPaint(null);
//					piePlot.setLabelOutlinePaint(null);
//					piePlot.setLabelGenerator(new StandardPieSectionLabelGenerator());
//					piePlot.setInsets(new RectangleInsets(10, 5.0, 5.0, 5.0));
//					piePlot.setLabelShadowPaint(null);
//					piePlot.setShadowXOffset(0.0D);
//					piePlot.setShadowYOffset(0.0D);
//					piePlot.setLabelGenerator(null);
//					piePlot.setBackgroundAlpha(0.4f);
//					piePlot.setExplodePercent("Pass", 0.05);
//					piePlot.setSimpleLabels(true);
//					piePlot.setSectionOutlinesVisible(false);
//					java.awt.Font f2 = new java.awt.Font("", java.awt.Font.PLAIN, 22);
//					piePlot.setLabelFont(f2);
//
//					PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator("{2}", new DecimalFormat("0"),
//							new DecimalFormat("0%"));
//					piePlot.setLabelGenerator(gen);
//					legend.setPosition(RectangleEdge.RIGHT);
//					legend.setVerticalAlignment(VerticalAlignment.CENTER);
//					piePlot.setInsets(new RectangleInsets(0.0, 5.0, 5.0, 5.0));
//					legend.setFrame(BlockBorder.NONE);
//					legend.setFrame(
//							new LineBorder(Color.white, new BasicStroke(20f), new RectangleInsets(1.0, 1.0, 1.0, 1.0)));
//
//					java.awt.Font pass1 = new java.awt.Font("", Font.NORMAL, 22);
//					legend.setItemFont(pass1);
//					PdfContentByte contentByte = writer.getDirectContent();
//					PdfTemplate template = contentByte.createTemplate(1000, 900);
//					Graphics2D graphics2d = template.createGraphics(700, 400, new DefaultFontMapper());
//					Rectangle2D rectangle2d = new Rectangle2D.Double(0, 0, 600, 400);
//					chart.draw(graphics2d, rectangle2d);
//					graphics2d.dispose();
//					contentByte.addTemplate(template, 400, 100);
//				}
////			 End Testrun to add piechart 
//// End Testrun to add Table and piechart 
////					 		Start to add page heading,all testrun names and states and page numbers	 		
//				int k = 0, l = 0;
//				String sno1 = "";
//				Map<Integer, Map<String, String>> toc = new TreeMap<>();
//
//				Map<String, String> toc2 = new TreeMap<>();
//				for (String image : fileNameList) {
//					k++;
//					String sndo = image.split("_")[0];
//					String name = image.split("_")[3];
//
//					if (!sndo.equalsIgnoreCase(sno1)) {
//						Map<String, String> toc1 = new TreeMap<>();
////					 				l=0;
//						for (String image1 : fileNameList) {
//							String Status = image1.split("_")[6];
//							String status = Status.split("\\.")[0];
//
////					 					l++;
//							if (image1.startsWith(sndo + "_") && image1.contains("Failed")) {
//
////					 						toc2.put(sndo,String.valueOf(l-2));	
//								toc2.put(sndo, "Failed" + l);
//								l++;
//							}
//						}
//
//						String str = String.valueOf(toc2.get(sndo));
//						toc1.put(name, str);
//						toc.put(k, toc1);
//
//					}
//					if (sndo != null) {
//						sno1 = sndo;
//					}
//				}
//				sno1 = "";
//				document.newPage();
//				document.add(img1);
////				Start to add page heading 
//				Anchor target2 = new Anchor(String.valueOf("Page Numbers"), bfBold);
//				target2.setName(String.valueOf("details"));
//				Chunk ch1 = new Chunk(String.format("Script Numbers"), bfBold);
//				ch1.setBackground(new BaseColor(38, 99, 175), 0f, 10f, 1730f, 15f);
//				Paragraph p2 = new Paragraph();
//				p2.add(ch1);
//				p2.add(new Chunk(new VerticalPositionMark()));
//				p2.add(target2);
//				document.add(p2);
//				document.add(Chunk.NEWLINE);
////				End to add page heading 
//
////			 Start to add all testrun names and states and page numbers	
//				Chunk dottedLine = new Chunk(new DottedLineSeparator());
//				for (Entry<Integer, Map<String, String>> entry : toc.entrySet()) {
//					Map<String, String> str1 = entry.getValue();
//					for (Entry<String, String> entry1 : str1.entrySet()) {
//						Anchor click = new Anchor(String.valueOf(entry.getKey()), bf15);
//						click.setReference("#" + String.valueOf(entry1.getKey()));
//						Anchor click1 = new Anchor(String.valueOf("(Failed)"), bf14);
//						click1.setReference("#" + String.valueOf(entry1.getValue()));
//						Paragraph pr = new Paragraph();
//						int value = entry.getKey();
//						Anchor ca1 = new Anchor(String.valueOf(entry1.getKey()), bf15);
//						ca1.setReference("#" + String.valueOf(entry1.getKey()));
//						String compare = entry1.getValue();
//						if (!compare.equals("null")) {
//							pr.add(ca1);
//
//							pr.add(click1);
//							pr.add(dottedLine);
//							pr.add(click);
//							document.add(Chunk.NEWLINE);
//							document.add(pr);
//						} else {
//							Anchor click2 = new Anchor(String.valueOf("(Passed)"), bf13);
//							click2.setReference("#" + String.valueOf(entry1.getKey()));
//							pr.add(ca1);
//							pr.add(click2);
//							pr.add(dottedLine);
//							pr.add(click);
//							document.add(Chunk.NEWLINE);
//							document.add(pr);
//						}
//					}
//				}
////			 End to add all testrun names and states and page numbers
////			 End to add page heading,add all testrun names and states and page numbers	
//
////	Start to add script details, screenshoots and pagenumbers and wats icon	
//				int i = 0, j = 0;
//				for (String image : fileNameList) {
//					i++;
//					Image img = Image.getInstance(
//							configVO.getScreenshotPath() + customer_Name + "/" + test_Run_Name + "/" + image);
////	Start to add script details 
//					String sno = image.split("_")[0];
//					String SNO = "Script Number";
//					String ScriptNumber = image.split("_")[3];
//					String SNM = "Test Case Name";
//					String ScriptName = image.split("_")[2];
//					String testRunName = image.split("_")[4];
////				String scrtipt=;
//					if (!sno.equalsIgnoreCase(sno1)) {
//						document.setPageSize(img);
//						document.newPage();
//						document.add(img1);
//						Anchor target3 = new Anchor("Script Details", bf12);
//						target3.setName(ScriptNumber);
//						Paragraph pa = new Paragraph();
//						pa.add(target3);
//						pa.setAlignment(Element.ALIGN_CENTER);
//						document.add(pa);
//						document.add(Chunk.NEWLINE);
//						PdfPTable table2 = new PdfPTable(2);
//						table2.setWidths(new int[] { 1, 1 });
//						table2.setWidthPercentage(100f);
//						insertCell(table2, SNO, Element.ALIGN_LEFT, 1, bf12);
//						insertCell(table2, ScriptNumber, Element.ALIGN_LEFT, 1, bf12);
//						insertCell(table2, SNM, Element.ALIGN_LEFT, 1, bf12);
//						insertCell(table2, ScriptName, Element.ALIGN_LEFT, 1, bf12);
//
//						for (Entry<String, String> entry1 : toc.get(i).entrySet()) {
//							String str = entry1.getValue();
//							if (!str.equals("null")) {
//								insertCell(table2, "Status", Element.ALIGN_LEFT, 1, bf12);
//								insertCell(table2, "Failed", Element.ALIGN_LEFT, 1, bf12);
//							} else {
//								insertCell(table2, "Status", Element.ALIGN_LEFT, 1, bf12);
//								insertCell(table2, "Passed", Element.ALIGN_LEFT, 1, bf12);
//							}
//						}
//
//						document.add(table2);
//
//					}
//					if (sno != null) {
//						sno1 = sno;
//					}
////	End to add script details 
//
////	Start to add  screenshoots and pagenumbers and wats icon		 		
////				String TestRun = image.split("_")[4];
//					String Status = image.split("_")[6];
//					String status = Status.split("\\.")[0];
//					String Scenario = image.split("_")[2];
//
////				String TR = "Test Run Name:" + " " + TestRun;
////				String SN = "Script Number:" + " " + ScriptNumber;
//
//					String Scenarios = "Test Case Name :" + "" + Scenario;
//
//					String sndo = image.split("_")[0];
//					img1.scalePercent(65, 68);
//
//					img1.setAlignment(Image.ALIGN_RIGHT);
//					// new change-failed pdf to set pagesize
//					if (image.startsWith(sndo + "_") && image.contains("Failed")) {
////					Rectangle one2 = new Rectangle(1360,1000);
//						document.setPageSize(one1);
//						document.newPage();
//					} else {
//
//						document.setPageSize(img);
//						document.newPage();
//					}
//					document.add(img1);
//					document.add(new Paragraph(Scenarios, fnt));
//					String Reason = image.split("_")[5];
//					String step = "Step No :" + "" + Reason;
//					String Message = "Failed at Line Number:" + "" + Reason;
//					// new change-database to get error message
//					//String error = databaseentry.getErrorMessage(sndo, ScriptNumber, testRunName, configVO);
//					 String error=testRunScriptExecService.getErrorMessage(sndo,ScriptNumber,testRunName,configVO,Reason,testRunId);
//					String errorMessage = "Failed Message:" + "" + error;
//					Paragraph pr1 = new Paragraph();
//					pr1.add("Status:");
//
//					if (image.startsWith(sndo + "_") && image.contains("Failed")) {
//						Anchor target1 = new Anchor(status);
//						target1.setName(String.valueOf(status + j));
//						j++;
//						pr1.add(target1);
//						document.add(pr1);
//						document.add(new Paragraph(Message, fnt));
//						if (error != null) {
//							document.add(new Paragraph(errorMessage, fnt));
//						}
//						document.add(Chunk.NEWLINE);
//						img.setAlignment(Image.ALIGN_CENTER);
//						img.isScaleToFitHeight();
//						// new change-change page size
//						img.scalePercent(60, 60);
//						document.add(img);
//
//					} else {
//						document.add(new Paragraph(step, fnt));
//						Anchor target1 = new Anchor(status);
//						target1.setName(String.valueOf(status));
//						pr1.add(target1);
//						document.add(pr1);
//						img.setAlignment(Image.ALIGN_CENTER);
//						img.isScaleToFitHeight();
//						// new change-change page size
//						img.scalePercent(60, 68);
//						document.add(img);
//					}
//
//					Anchor target = new Anchor(String.valueOf(i));
//					target.setName(String.valueOf(i));
//					Anchor target1 = new Anchor(String.valueOf("Back to Index"), bf16);
//					target1.setReference("#" + String.valueOf("details"));
//					Paragraph p = new Paragraph();
//					p.add(target1);
//					p.add(new Chunk(new VerticalPositionMark()));
//					p.add(" page ");
//					p.add(target);
//					p.add(" of " + fileNameList.size());
////				img.setAlignment(Image.ALIGN_CENTER);
////				img.isScaleToFitHeight();
////				img.scalePercent(60, 71);
////				document.add(img);
//					document.add(p);
//					System.out.println("This Image " + "" + image + "" + "was added to the report");
////	End to add  screenshots and pagenumbers and wats icon		 		
////	End to add script details, screenshoots and pagenumbers and wats icon		 		
////  End to create testrun level reports	
//				}
//			} else {
////  Start to create Script level passed reports		
////  Start to add Script level details		
//				if (!("Passed_Report.pdf".equalsIgnoreCase(pdffileName)
//						|| "Failed_Report.pdf".equalsIgnoreCase(pdffileName)
//						|| "Detailed_Report.pdf".equalsIgnoreCase(pdffileName))) {
//					String Starttime1 = dateFormat.format(Starttime);
//					String endtime1 = dateFormat.format(endtime);
//					long diff = endtime.getTime() - Starttime.getTime();
//					long diffSeconds = diff / 1000 % 60;
//					long diffMinutes = diff / (60 * 1000) % 60;
//					long diffHours = diff / (60 * 60 * 1000);
//					String TestRun = test_Run_Name;
//					String ScriptNumber = Script_Number;
//					String ScriptNumber1 = Scenario_Name;
//					String Scenario1 = configVO.getStatus1();
////					String ExecutedBy=configVO.getApplication_user_name();
//					String StartTime = Starttime1;
//					String EndTime = endtime1;
//					String ExecutionTime = diffHours + ":" + diffMinutes + ":" + diffSeconds;
//
//					String TR = "Test Run Name";
//					String SN = "Script Number";
//					String SN1 = "Scenario name";
//					String Scenarios1 = "Status ";
//					String EB = "Executed By";
//					String ST = "Start Time";
//					String ET = "End Time";
//					String EX = "Execution Time";
//					document.add(img1);
//
//					document.add(new Paragraph(Report, bfBold12));
//					document.add(Chunk.NEWLINE);
//					PdfPTable table1 = new PdfPTable(2);
//					table1.setWidths(new int[] { 1, 1 });
//					table1.setWidthPercentage(100f);
//
//					insertCell(table1, TR, Element.ALIGN_LEFT, 1, bf12);
//					insertCell(table1, TestRun, Element.ALIGN_LEFT, 1, bf12);
//					insertCell(table1, SN, Element.ALIGN_LEFT, 1, bf12);
//					insertCell(table1, ScriptNumber, Element.ALIGN_LEFT, 1, bf12);
//					insertCell(table1, SN1, Element.ALIGN_LEFT, 1, bf12);
//					insertCell(table1, ScriptNumber1, Element.ALIGN_LEFT, 1, bf12);
//					insertCell(table1, Scenarios1, Element.ALIGN_LEFT, 1, bf12);
//					insertCell(table1, Scenario1, Element.ALIGN_LEFT, 1, bf12);
//					insertCell(table1, EB, Element.ALIGN_LEFT, 1, bf12);
//					insertCell(table1, ExecutedBy, Element.ALIGN_LEFT, 1, bf12);
//					insertCell(table1, ST, Element.ALIGN_LEFT, 1, bf12);
//					insertCell(table1, StartTime, Element.ALIGN_LEFT, 1, bf12);
//					insertCell(table1, ET, Element.ALIGN_LEFT, 1, bf12);
//					insertCell(table1, EndTime, Element.ALIGN_LEFT, 1, bf12);
//					insertCell(table1, EX, Element.ALIGN_LEFT, 1, bf12);
//					insertCell(table1, ExecutionTime, Element.ALIGN_LEFT, 1, bf12);
//					document.add(table1);
//					document.newPage();
////  End to add Script level details
//
////	Start to add screenshoots and pagenumbers and wats icon		 		
//					int i = 0;
//					for (String image : fileNameList) {
////				 Image img = Image.getInstance(
////				 configVO.getScreenshotPath() + customer_Name + "\\" + test_Run_Name +
////				 "\\" + image);
//						i++;
//						Image img = Image.getInstance(
//								configVO.getScreenshotPath() + customer_Name + "/" + test_Run_Name + "/" + image);
//
//						String Status = image.split("_")[6];
//						String status = Status.split("\\.")[0];
//						String Scenario = image.split("_")[2];
//						String steps = image.split("_")[5];
//						document.setPageSize(img);
//						document.newPage();
//
//						String S = "Status:" + " " + status;
//						String Scenarios = "Test Case Name :" + "" + Scenario;
//						String step = "Step No :" + "" + steps;
//						img1.scalePercent(65, 65);
//						img1.setAlignment(Image.ALIGN_RIGHT);
//						document.add(img1);
//						document.add(new Paragraph(S, fnt));
//						document.add(new Paragraph(Scenarios, fnt));
//						document.add(new Paragraph(step, fnt));
//						document.add(Chunk.NEWLINE);
//
//						Paragraph p = new Paragraph(String.format("page %s of %s", i, fileNameList.size()));
//						p.setAlignment(Element.ALIGN_RIGHT);
//						img.setAlignment(Image.ALIGN_CENTER);
//						img.isScaleToFitHeight();
//						// new change-change page size
//						img.scalePercent(60, 62);
//						document.add(img);
//						document.add(p);
//						System.out.println("This Image " + "" + image + "" + "was added to the report");
////		End to add screenshoots and pagenumbers and wats icon
////  End to create Script level passed reports		
//
//					}
//				}
//			}
//			document.close();
////			compress(fetchMetadataListVO, configVO, pdffileName);
//
//		} catch (Exception e) {
//			System.out.println("Not able to Create pdf" + e);
//		}
//	}
//
//	public void insertCell(PdfPTable table, String text, int align, int colspan, Font font) {
//
//		// create a new cell with the specified Text and Font
//		PdfPCell cell = new PdfPCell(new Paragraph(text.trim(), font));
//		cell.setBorder(PdfPCell.NO_BORDER);
//		// set the cell alignment
//
//		cell.setUseVariableBorders(true);
//		if (text.equalsIgnoreCase("Status")) {
//			cell.setBorderWidthLeft(0.3f);
//			cell.setBorderColorLeft(new BaseColor(230, 225, 225));
//			cell.setBorderWidthTop(0.3f);
//			cell.setBorderColorTop(new BaseColor(230, 225, 225));
//			cell.setBorderWidthRight(0.3f);
//			cell.setBorderColorRight(new BaseColor(230, 225, 225));
//			cell.setBorderWidthBottom(0.3f);
//			cell.setBorderColorBottom(new BaseColor(230, 225, 225));
//		} else if (text.equalsIgnoreCase("Total")) {
//			cell.setBorderWidthTop(0.3f);
//			cell.setBorderColorTop(new BaseColor(230, 225, 225));
//			cell.setBorderWidthRight(0.3f);
//			cell.setBorderColorRight(new BaseColor(230, 225, 225));
//			cell.setBorderWidthBottom(0.3f);
//			cell.setBorderColorBottom(new BaseColor(230, 225, 225));
//		} else if (text.equalsIgnoreCase("Percentage")) {
//			cell.setBorderWidthTop(0.3f);
//			cell.setBorderColorTop(new BaseColor(230, 225, 225));
//			cell.setBorderWidthRight(0.3f);
//			cell.setBorderColorRight(new BaseColor(230, 225, 225));
//			cell.setBorderWidthBottom(0.3f);
//			cell.setBorderColorBottom(new BaseColor(230, 225, 225));
//		} else if (text.equalsIgnoreCase("Passed") || text.equalsIgnoreCase("Failed")) {
//			cell.setBorderWidthLeft(0.3f);
//			cell.setBorderColorLeft(new BaseColor(230, 225, 225));
//			cell.setBorderWidthRight(0.3f);
//			cell.setBorderColorRight(new BaseColor(230, 225, 225));
//			cell.setBorderWidthBottom(0.3f);
//			cell.setBorderColorBottom(new BaseColor(230, 225, 225));
//		} else if (text.contains("%")) {
//			cell.setBorderWidthRight(0.3f);
//			cell.setBorderColorRight(new BaseColor(230, 225, 225));
//			cell.setBorderWidthBottom(0.3f);
//			cell.setBorderColorBottom(new BaseColor(230, 225, 225));
//		}
////	  	else if() {
////	  	 cell.setBorderWidthRight(0.3f);
////	  	cell.setBorderColorRight(new BaseColor(230, 225, 225));
////	  		cell.setBorderWidthBottom(0.3f);
////	  		cell.setBorderColorBottom(new BaseColor(230, 225, 225));
////	  	}
//		else {
//			cell.setBorderWidthLeft(0.3f);
//			cell.setBorderColorLeft(new BaseColor(230, 225, 225));
//			cell.setBorderWidthTop(0.3f);
//			cell.setBorderColorTop(new BaseColor(230, 225, 225));
//			cell.setBorderWidthRight(0.3f);
//			cell.setBorderColorRight(new BaseColor(230, 225, 225));
//			cell.setBorderWidthBottom(0.3f);
//			cell.setBorderColorBottom(new BaseColor(230, 225, 225));
//		}
//
//		cell.setHorizontalAlignment(align);
//
//		cell.setColspan(colspan);
//		// in case there is no text and you wan to create an empty row
//		if (text.trim().equalsIgnoreCase("")) {
//			cell.setMinimumHeight(20f);
//		}
//		if (text.length() > 103) {
//			cell.setFixedHeight(80f);
//		} else if (text.length() > 53) {
//			cell.setFixedHeight(60f);
//		} else {
//			cell.setFixedHeight(40f);
//		}
//		// add the call to the table
//		table.addCell(cell);
//
//	}
//
//	public List<String> getImages(List<TestRunExecutionVO> fetchMetadataListVO, ConfigVO configVO) {
//		List<String> fileNameList = new ArrayList<String>();
//		File folder = new File(configVO.getScreenshotPath() + "\\" + fetchMetadataListVO.get(0).getCustomerName()
//				+ "\\" + fetchMetadataListVO.get(0).getTestRunName() + "\\");
//		File[] listOfFiles = folder.listFiles();
//		for (int i = 0; i < listOfFiles.length; i++) {
//			if (listOfFiles[i].isFile()) {
//				System.out.println("File " + listOfFiles[i].getName());
//				String fileName = listOfFiles[i].getName();
//				String[] fileNameArr = fileName.split("\\.");
//				String fileExt = fileNameArr[fileNameArr.length - 1];
//				String[] _arr = fileName.split("_");
//				String currentScriptNumber = _arr[2];
//				String Status = _arr[6];
//				String status = Status.split("\\.")[0];
//				if ("jpg".equalsIgnoreCase(fileExt) && "Passed".equalsIgnoreCase(status)) {
//					fileNameList.add(fileName);
//				}
//			}
//		}
//		return fileNameList;
//	}
//
//	public void compress(List<TestRunExecutionVO> fetchMetadataListVO, ConfigVO configVO, String pdffileName)
//			throws IOException {
//		String Folder = (configVO.getScreenshotPath() + "\\" + fetchMetadataListVO.get(0).getCustomerName()
//				+ "\\" + fetchMetadataListVO.get(0).getTestRunName() + "\\");
//		List<String> fileNameList = null;
//		String customer_Name = fetchMetadataListVO.get(0).getCustomerName();
//		String test_Run_Name = fetchMetadataListVO.get(0).getTestRunName();
//		fileNameList = getImages(fetchMetadataListVO, configVO);
//
//		for (String image : fileNameList) {
//
//			FileInputStream inputStream = new FileInputStream(
//					configVO.getScreenshotPath() + "\\" + customer_Name + "\\" + test_Run_Name + "\\" + image);
//			BufferedImage inputImage = ImageIO.read(inputStream);
//
//			JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
//			jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
//			jpegParams.setCompressionQuality(.4f);
//
//			final ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
//			// specifies where the jpg image has to be written
//			writer.setOutput(new FileImageOutputStream(new File("C:\\Kaushik" + "\\" + image)));
//
//			BufferedImage convertedImg = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(),
//					BufferedImage.TYPE_INT_RGB);
//			convertedImg.getGraphics().drawImage(inputImage, 0, 0, null);
//
//			// writes the file with given compression level
//			// from your JPEGImageWriteParam instance
//			writer.write(null, new IIOImage(convertedImg, null, null), jpegParams);
//
////		BufferedImage originalImage = ImageIO.read(new File(Folder+image));
////		int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
//
////		BufferedImage resizeImageGif = resizeImage(originalImage, type);
////		ImageIO.write(resizeImageGif, "jpg", new File("C:\\Kaushik"+"\\"+image));
//
//			/*
//			 * BufferedImage resizeImagePng = resizeImage(originalImage, type);
//			 * ImageIO.write(resizeImagePng, "png", new File("c:\\image\\mkyong_png.jpg"));
//			 * 
//			 * BufferedImage resizeImageHintJpg = resizeImageWithHint(originalImage, type);
//			 * ImageIO.write(resizeImageHintJpg, "jpg", new
//			 * File("c:\\image\\mkyong_hint_jpg.jpg"));
//			 * 
//			 * BufferedImage resizeImageHintPng = resizeImageWithHint(originalImage, type);
//			 * ImageIO.write(resizeImageHintPng, "png", new
//			 * File("c:\\image\\mkyong_hint_png.jpg"));
//			 */
//		}
//
//	}
//
//	private static BufferedImage resizeImage(BufferedImage originalImage, int type) {
//		final int IMG_WIDTH = 1280;
//		final int IMG_HEIGHT = 960;
//		BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
//		Graphics2D g = resizedImage.createGraphics();
//		g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
//		g.dispose();
//
//		return resizedImage;
//	}
//
//	private static BufferedImage resizeImageWithHint(BufferedImage originalImage, int type) {
//		final int IMG_WIDTH = 1280;
//		final int IMG_HEIGHT = 768;
//		BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
//		Graphics2D g = resizedImage.createGraphics();
//		g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
//		g.dispose();
//		g.setComposite(AlphaComposite.Src);
//
//		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		return resizedImage;
//	}
//
//	public List<String> getLowFileNameList(List<TestRunExecutionVO> fetchMetadataListVO) {
//		List<String> fileNameList = new ArrayList<String>();
//		File folder = new File("C:\\Kaushik\\");
//		File[] listOfFiles = folder.listFiles();
//		Arrays.sort(listOfFiles, new Comparator<File>() {
//			public int compare(File f1, File f2) {
//				return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
//
//			}
//		});
//		String scripNumber = fetchMetadataListVO.get(0).getScriptNumber();
//		String Number =  String.valueOf(fetchMetadataListVO.get(0).getLineNumber());
//		for (int i = 0; i < listOfFiles.length; i++) {
//			if (listOfFiles[i].isFile()) {
//				System.out.println("File " + listOfFiles[i].getName());
//				String fileName = listOfFiles[i].getName();
//				String[] fileNameArr = fileName.split("\\.");
//				String fileExt = fileNameArr[fileNameArr.length - 1];
//				String[] _arr = fileName.split("_");
//				String currentScriptNumber = _arr[2];
//				String Status = _arr[6];
//				String status = Status.split("\\.")[0];
//				if ("jpg".equalsIgnoreCase(fileExt) && scripNumber.equalsIgnoreCase(currentScriptNumber)
//						&& "Passed".equalsIgnoreCase(status)) {
//					fileNameList.add(fileName);
//				}
//			}
//		}
//		return fileNameList;
//	}
//
//	public List<String> getLowPassedPdf(List<TestRunExecutionVO> fetchMetadataListVO) {
//		List<String> fileNameList = new ArrayList<String>();
//		File folder = new File("C:\\Kaushik\\");
//		File[] listOfFiles = folder.listFiles();
//		Arrays.sort(listOfFiles, new Comparator<File>() {
//			public int compare(File f1, File f2) {
//				return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
//
//			}
//		});
//		String scripNumber = fetchMetadataListVO.get(0).getScriptNumber();
//		String STATUS = fetchMetadataListVO.get(0).getStatus();
//		for (int i = 0; i < listOfFiles.length; i++) {
//			if (listOfFiles[i].isFile()) {
//				System.out.println("File " + listOfFiles[i].getName());
//				String fileName = listOfFiles[i].getName();
//				String[] fileNameArr = fileName.split("\\.");
//				String fileExt = fileNameArr[fileNameArr.length - 1];
//				String[] _arr = fileName.split("_");
//				String currentScriptNumber = _arr[2];
//				String Status = _arr[6];
//				String status = Status.split("\\.")[0];
//				if ("jpg".equalsIgnoreCase(fileExt) && "Passed".equalsIgnoreCase(status)) {
//					fileNameList.add(fileName);
//				}
//			}
//		}
//		return fileNameList;
//	}
//
//	public List<String> getLowFailedPdf(List<TestRunExecutionVO> fetchMetadataListVO) {
//
//		List<String> fileNameList = new ArrayList<String>();
//		File folder = new File("C:\\Kaushik\\");
//		File[] listOfFiles = folder.listFiles();
//		Arrays.sort(listOfFiles, new Comparator<File>() {
//			public int compare(File f1, File f2) {
//				return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
//
//			}
//		});
//		String scripNumber = fetchMetadataListVO.get(0).getScriptNumber();
//		for (int i = 0; i < listOfFiles.length; i++) {
//			if (listOfFiles[i].isFile()) {
//				System.out.println("File " + listOfFiles[i].getName());
//				String fileName = listOfFiles[i].getName();
//				String[] fileNameArr = fileName.split("\\.");
//				String fileExt = fileNameArr[fileNameArr.length - 1];
//				String[] _arr = fileName.split("_");
//				String currentScriptNumber = _arr[2];
//				String Status = _arr[6];
//				String status = Status.split("\\.")[0];
//				if ("jpg".equalsIgnoreCase(fileExt) && ("Failed".equalsIgnoreCase(status))) {
//					fileNameList.add(fileName);
//				}
//			}
//		}
//		return fileNameList;
//	}
//
//	public List<String> getLowDetailPdf(List<TestRunExecutionVO> fetchMetadataListVO) {
//		List<String> fileNameList = new ArrayList<String>();
//		File folder = new File("C:\\Kaushik\\");
//		File[] listOfFiles = folder.listFiles();
//		Arrays.sort(listOfFiles, new Comparator<File>() {
//			public int compare(File f1, File f2) {
//				return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
//
//			}
//		});
//		String scripNumber = fetchMetadataListVO.get(0).getScriptNumber();
//		for (int i = 0; i < listOfFiles.length; i++) {
//			if (listOfFiles[i].isFile()) {
//				System.out.println("File " + listOfFiles[i].getName());
//				String fileName = listOfFiles[i].getName();
//				String[] fileNameArr = fileName.split("\\.");
//				String fileExt = fileNameArr[fileNameArr.length - 1];
//				String[] _arr = fileName.split("_");
//				String currentScriptNumber = _arr[2];
//				String Status = _arr[6];
//				String status = Status.split("\\.")[0];
//				if ("jpg".equalsIgnoreCase(fileExt)) {
//					fileNameList.add(fileName);
//				}
//			}
//		}
//		return fileNameList;
//	}
//
//	public void createLowPdf(List<TestRunExecutionVO> fetchMetadataListVO, ConfigVO configVO, String pdffileName)
//			throws IOException, DocumentException, com.itextpdf.text.DocumentException {
//		try {
//			String Date = DateUtils.getSysdate();
//			String Folder = ("C:\\Kaushik\\PDF\\");
//			String FILE = (Folder + pdffileName);
//			System.out.println(FILE);
//			List<String> fileNameList = null;
//			if ("Passed_Report.pdf".equalsIgnoreCase(pdffileName)) {
//				fileNameList = getLowPassedPdf(fetchMetadataListVO);
//			} else if ("Failed_Report.pdf".equalsIgnoreCase(pdffileName)) {
//				fileNameList = getLowFailedPdf(fetchMetadataListVO);
//			} else if ("Detailed_Report.pdf".equalsIgnoreCase(pdffileName)) {
//				fileNameList = getLowDetailPdf(fetchMetadataListVO);
//			} else {
//				fileNameList = getLowFileNameList(fetchMetadataListVO);
//			}
//			String Script_Number = fetchMetadataListVO.get(0).getScriptNumber();
//			String customer_Name = fetchMetadataListVO.get(0).getCustomerName();
//			String test_Run_Name = fetchMetadataListVO.get(0).getTestRunName();
//			String Scenario_Name = fetchMetadataListVO.get(0).getScenarioName();
//			File theDir = new File(Folder);
//			if (!theDir.exists()) {
//				System.out.println("creating directory: " + theDir.getName());
//				boolean result = false;
//				try {
//					theDir.mkdirs();
//					result = true;
//				} catch (SecurityException se) {
//					// handle it
//					System.out.println(se.getMessage());
//				}
//			} else {
//				System.out.println("Folder exist");
//			}
//			Document document = new Document();
//			PdfWriter.getInstance(document, new FileOutputStream(FILE));
//			document.open();
//			for (String image : fileNameList) {
//				Image img = Image.getInstance("C:\\Kaushik\\" + image);
//				String ScriptNumber = image.split("_")[2];
//				String TestRun = image.split("_")[3];
//				String Status = image.split("_")[6];
//				String status = Status.split("\\.")[0];
//				String Scenario = image.split("_")[1];
//				document.setPageSize(img);
//				document.newPage();
//				Font fnt = FontFactory.getFont("Arial", 12);
//				String TR = "Test Run Name:" + " " + TestRun;
//				String SN = "Script Number:" + " " + ScriptNumber;
//				String S = "Status:" + " " + status;
//				String Scenarios = "Test Case Name :" + "" + Scenario;
//				document.add(new Paragraph(TR, fnt));
//				document.add(new Paragraph(SN, fnt));
//				document.add(new Paragraph(S, fnt));
//				document.add(new Paragraph(Scenarios, fnt));
//				document.add(Chunk.NEWLINE);
//				img.setAlignment(Image.ALIGN_CENTER);
//				img.isScaleToFitHeight();
//				img.scalePercent(60, 60);
//				document.add(img);
//			}
//			document.close();
//		} catch (Exception e) {
//			System.out.println("Not able to upload the pdf");
//		}
//	}
//
//	public void createFailedPdf(List<TestRunExecutionVO> fetchMetadataListVO, ConfigVO configVO,
//			String pdffileName, Date Starttime, Date endtime)
//			throws IOException, DocumentException, com.itextpdf.text.DocumentException {
//		try {
//			String Date = DateUtils.getSysdate();
//			String Folder = (configVO.getPdfPath() + fetchMetadataListVO.get(0).getCustomerName() + "/"
//					+ fetchMetadataListVO.get(0).getTestRunName() + "/");
//			String FILE = (Folder + pdffileName);
//			System.out.println(FILE);
//			List<String> fileNameList = null;
//			if ("Passed_Report.pdf".equalsIgnoreCase(pdffileName)) {
////				fileNameList = getPassedPdfNew(fetchMetadataListVO, configVO);
//			} else if ("Failed_Report.pdf".equalsIgnoreCase(pdffileName)) {
////				fileNameList = getFailedPdfNew(fetchMetadataListVO, configVO);
//			}
//			if ("Detailed_Report.pdf".equalsIgnoreCase(pdffileName)) {
//				fileNameList = getDetailPdfNew(fetchMetadataListVO, configVO);
//			} else {
//				fileNameList = getFailFileNameListNew(fetchMetadataListVO, configVO);
//			}
//
//			String Script_Number = fetchMetadataListVO.get(0).getScriptNumber();
//			String customer_Name = fetchMetadataListVO.get(0).getCustomerName();
//			String test_Run_Name = fetchMetadataListVO.get(0).getTestRunName();
//			String Scenario_Name = fetchMetadataListVO.get(0).getScenarioName();
//			// new change add ExecutedBy field
//			String ExecutedBy = fetchMetadataListVO.get(0).getExecutedBy();
//			String ScriptDescription1 = fetchMetadataListVO.get(0).getScenarioName();
//			File theDir = new File(Folder);
//			if (!theDir.exists()) {
//				System.out.println("creating directory: " + theDir.getName());
//				boolean result = false;
//				try {
//					theDir.mkdirs();
//					result = true;
//				} catch (SecurityException se) {
//					// handle it
//					System.out.println(se.getMessage());
//				}
//			} else {
//				System.out.println("Folder exist");
//			}
//			Font bf12 = FontFactory.getFont("Arial", 23);
//			System.out.println("before enter Images/wats_icon.png");
//			Image img1 = Image.getInstance(watslogo);
//			System.out.println("after enter Images/wats_icon.png");
//			img1.scalePercent(65, 68);
//			img1.setAlignment(Image.ALIGN_RIGHT);
//			Font bfBold12 = FontFactory.getFont("Arial", 23);
//			String Report = "Execution Report";
//			Font fnt = FontFactory.getFont("Arial", 12);
//			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:aa");
//			String Starttime1 = dateFormat.format(Starttime);
//			String endtime1 = dateFormat.format(endtime);
//			long diff = endtime.getTime() - Starttime.getTime();
//			long diffSeconds = diff / 1000 % 60;
//			long diffMinutes = diff / (60 * 1000) % 60;
//			long diffHours = diff / (60 * 60 * 1000);
//			Document document = new Document();
//			PdfWriter.getInstance(document, new FileOutputStream(FILE));
//			Rectangle one = new Rectangle(1360, 800);
//			document.setPageSize(one);
//			document.open();
//			String TestRun = test_Run_Name;
//			String ScriptNumber = Script_Number;
//			String error = configVO.getErrormessage();
//			String ScriptNumber1 = Scenario_Name;
//			String Scenario1 = configVO.getStatus1();
////			String ExecutedBy=configVO.getApplication_user_name();
//			String StartTime = Starttime1;
//			String EndTime = endtime1;
//			String ExecutionTime = diffHours + ":" + diffMinutes + ":" + diffSeconds;
//
//			String TR = "Test Run Name";
//			String SN = "Script Number";
//			String SN1 = "Test Case Name";
//			String Scenarios1 = "Status ";
//			String showErrorMessage = "	ErrorMessage ";
//			String EB = "Executed By";
//			String ST = "Start Time";
//			String ET = "End Time";
//			String EX = "Execution Time";
//
//			document.add(img1);
//
//			document.add(new Paragraph(Report, bfBold12));
//			document.add(Chunk.NEWLINE);
//			PdfPTable table1 = new PdfPTable(2);
//			table1.setWidths(new int[] { 1, 1 });
//			table1.setWidthPercentage(100f);
//
//			insertCell(table1, TR, Element.ALIGN_LEFT, 1, bf12);
//			insertCell(table1, TestRun, Element.ALIGN_LEFT, 1, bf12);
//			insertCell(table1, SN, Element.ALIGN_LEFT, 1, bf12);
//			insertCell(table1, ScriptNumber, Element.ALIGN_LEFT, 1, bf12);
//			insertCell(table1, SN1, Element.ALIGN_LEFT, 1, bf12);
//			insertCell(table1, ScriptNumber1, Element.ALIGN_LEFT, 1, bf12);
//			insertCell(table1, Scenarios1, Element.ALIGN_LEFT, 1, bf12);
//			insertCell(table1, Scenario1, Element.ALIGN_LEFT, 1, bf12);
//			insertCell(table1, showErrorMessage, Element.ALIGN_LEFT, 1, bf12);
//			insertCell(table1, error, Element.ALIGN_LEFT, 1, bf12);
//			insertCell(table1, EB, Element.ALIGN_LEFT, 1, bf12);
//			insertCell(table1, ExecutedBy, Element.ALIGN_LEFT, 1, bf12);
//			insertCell(table1, ST, Element.ALIGN_LEFT, 1, bf12);
//			insertCell(table1, StartTime, Element.ALIGN_LEFT, 1, bf12);
//			insertCell(table1, ET, Element.ALIGN_LEFT, 1, bf12);
//			insertCell(table1, EndTime, Element.ALIGN_LEFT, 1, bf12);
//			insertCell(table1, EX, Element.ALIGN_LEFT, 1, bf12);
//			insertCell(table1, ExecutionTime, Element.ALIGN_LEFT, 1, bf12);
//			document.add(table1);
//			document.newPage();
////End to add Script level details
////				Start to add screenshoots and pagenumbers and wats icon		 		
//			int i = 0;
//			for (String image : fileNameList) {
//				i++;
//				Image img = Image.getInstance(
//						configVO.getScreenshotPath() + customer_Name + "/" + test_Run_Name + "/" + image);
//
////						String ScriptNumber = image.split("_")[3];
////						String TestRun = image.split("_")[4];
//				String Status = image.split("_")[6];
//				String status = Status.split("\\.")[0];
//				String Scenario = image.split("_")[2];
//
//				if (status.equalsIgnoreCase("Failed")) {// Rectangle one2 = new Rectangle(1360,1000);
//					document.setPageSize(one);
//					document.newPage();
//				} else {
//
//					document.setPageSize(img);
//					document.newPage();
//				}
//
//				document.add(img1);
//				String Reason = image.split("_")[5];
//				// String TR = "Test Run Name:" + " " + TestRun;
////						String SN = "Script Number:" + " " + ScriptNumber;
//				String S = "Status:" + " " + status;
//				String step = "Step No :" + "" + Reason;
//				String Scenarios = "Test Case Name :" + "" + Scenario;
//				String Message = "Failed at Line Number:" + "" + Reason;
//				String errorMessage = "Failed Message:" + "" + configVO.getErrormessage();
//				// String message = "Failed at
//				// :"+fetchMetadataListVO.get(0).getInput_parameter();
////						document.add(new Paragraph(TR, fnt));
////						document.add(new Paragraph(SN, fnt));
//				document.add(new Paragraph(S, fnt));
//				document.add(new Paragraph(Scenarios, fnt));
////new change-failed pdf to add pagesize
//				if (status.equalsIgnoreCase("Failed")) {
//					document.add(new Paragraph(Message, fnt));
//					if (configVO.getErrormessage() != null) {
//						document.add(new Paragraph(errorMessage, fnt));
//					}
//					document.add(Chunk.NEWLINE);
//					img.setAlignment(Image.ALIGN_CENTER);
//					img.isScaleToFitHeight();
//					// new change-change page size
//					img.scalePercent(60, 58);
//					document.add(img);
//				} else {
//					document.add(new Paragraph(step, fnt));
//					document.add(Chunk.NEWLINE);
//					img.setAlignment(Image.ALIGN_CENTER);
//					img.isScaleToFitHeight();
//					// new change-change page size
//					img.scalePercent(60, 62);
//					document.add(img);
//				}
//
//				Paragraph p = new Paragraph(String.format("page %s of %s", i, fileNameList.size()));
//				p.setAlignment(Element.ALIGN_RIGHT);
//
//				document.add(p);
//				System.out.println("This Image " + "" + image + "" + "was added to the report");
////				End to add screenshoots and pagenumbers and wats icon
//				// End to create Script level passed reports
//
//			}
//			document.close();
//			compress(fetchMetadataListVO, configVO, pdffileName);
//		} catch (Exception e) {
//			System.out.println("Not able to upload the pdf");
//			e.printStackTrace();
//		}
//	}
//
//	public void uploadPDF(List<TestRunExecutionVO> fetchMetadataListVO, ConfigVO configVO) {
//		try {
//			String accessToken = getAccessTokenPdf(configVO);
//			List imageUrlList = new ArrayList();
//			File imageDir = new File(configVO.getPdfPath() + fetchMetadataListVO.get(0).getCustomerName() + "/"
//					+ fetchMetadataListVO.get(0).getTestRunName() + "/");
//			System.out.println(imageDir);
//			for (File imageFile : imageDir.listFiles()) {
//				String imageFileName = imageFile.getName();
//				System.out.println(imageFileName);
//				imageUrlList.add(imageFileName);
//				File pdfFile = new File(imageDir + "/" + imageFileName);
//				System.out.println(pdfFile);
//				FileInputStream input = new FileInputStream(pdfFile);
//				ByteArrayOutputStream bos = new ByteArrayOutputStream();
//				byte[] buffer = new byte[99999999];
//				int l;
//				while ((l = input.read(buffer)) > 0) {
//					bos.write(buffer, 0, l);
//				}
//				input.close();
//				byte[] data = bos.toByteArray();
//				RestTemplate restTemplate = new RestTemplate();
//				MultiValueMap<String, byte[]> bodyMap = new LinkedMultiValueMap<>();
//				bodyMap.add("user-file", data);
//				// Outer header
//				HttpHeaders uploadSessionHeader = new HttpHeaders();
//				// uploadSessionHeader.setContentType(MediaType.APPLICATION_JSON);
//				uploadSessionHeader.add("Authorization", "Bearer " + accessToken);
//				System.out.println(configVO.getSharepointDriveId());
//				System.out.println(configVO.getSharepointItemId());
//				HttpEntity<byte[]> uploadSessionRequest = new HttpEntity<>(null, uploadSessionHeader);
//				ResponseEntity<Object> response = restTemplate.exchange(microsoftGraphBaseDrivesUrl
//						+ configVO.getSharepointDriveId() + "/items/" + configVO.getSharepointItemId()
//						+ ":/Screenshot/" + fetchMetadataListVO.get(0).getCustomerName() + "/"
//						+ fetchMetadataListVO.get(0).getTestRunName() + "/" + imageFileName + ":/createUploadSession",
//						HttpMethod.POST, uploadSessionRequest, Object.class);
//				System.out.println(response);
//				Map<String, Object> linkedMap = response.getBody() != null
//						? (LinkedHashMap<String, Object>) response.getBody()
//						: null;
//				String uploadUrl = linkedMap != null ? StringUtils.convertToString(linkedMap.get("uploadUrl")) : null;
//
//				HttpHeaders uploadingFileHeader = new HttpHeaders();
//				uploadingFileHeader.setContentLength(data.length);
//				uploadingFileHeader.add("Content-Range", "bytes " + 0 + "-" + (data.length - 1) + "/" + data.length);
//				uploadingFileHeader.setContentType(MediaType.parseMediaType("application/pdf"));
//
//				HttpEntity<byte[]> uploadingFileRequest = new HttpEntity<>(data, uploadingFileHeader);
//				ResponseEntity<byte[]> putResponse = restTemplate.exchange(uploadUrl, HttpMethod.PUT,
//						uploadingFileRequest, byte[].class);
//
//				System.out.println(putResponse);
//				System.out.println("response status: " + response.getStatusCode());
//				System.out.println("response body: " + response.getBody());
//				System.out.println("response : " + response);
//			}
//		} catch (Exception e) {
//			System.out.println(e);
//		}
//	}
//
//	public String getAccessTokenPdf(ConfigVO configVO) {
//		String acessToken = null;
//		try {
//			RestTemplate restTemplate = new RestTemplate();
//			HttpHeaders headers = new HttpHeaders();
//			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//			MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//			map.add("grant_type", "client_credentials");
//			map.add("client_id", configVO.getClientId());
//			map.add("client_secret", configVO.getClientSecret());
//			map.add("scope", "https://graph.microsoft.com/.default");
//
//			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
//			ResponseEntity<Object> response = restTemplate.exchange(
//					"https://login.microsoftonline.com/" + configVO.getTenantId() + "/oauth2/v2.0/token",
//					HttpMethod.POST, entity, Object.class);
//			System.out.println(response);
//
//			@SuppressWarnings("unchecked")
//			Map<String, Object> linkedMap = response.getBody() != null ? (Map<String, Object>) response.getBody()
//					: null;
//			acessToken = linkedMap != null ? StringUtils.convertToString(linkedMap.get("access_token")) : null;
//		} catch (Exception e) {
//			System.out.println(e);
//		}
//		System.out.println(acessToken);
//		return acessToken;
//	}
//
//	public void copy(WebDriver driver, TestRunExecutionVO fetchMetadataVO, ConfigVO configVO) throws Exception {
//		try {
//			Robot r = new Robot();
//			r.keyPress(KeyEvent.VK_CONTROL);
//			r.keyPress(KeyEvent.VK_C);
//			r.keyRelease(KeyEvent.VK_C);
//			r.keyRelease(KeyEvent.VK_CONTROL);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Successfully Copy is done " + scripNumber);
//
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Copy " + scripNumber);
//			screenshotFail(driver, "Failed during Copy Method", fetchMetadataVO, configVO);
//			e.printStackTrace();
//			throw e;
//		}
//	}// input[@placeholder='Enter search terms']
//
//	public void paste(WebDriver driver, String inputParam, TestRunExecutionVO fetchMetadataVO, ConfigVO configVO,
//			String globalValueForSteps)
//
//			throws Exception {
//
//		try {
//			if (inputParam.equalsIgnoreCase("Notifications")) {
//				WebElement waittill = driver.findElement(
//						By.xpath("//h1[text()='" + inputParam + "']/following::input[@placeholder='Search']"));
//				// to get Dynamic copynumber
//				String testParamId = String.valueOf(fetchMetadataVO.getTestRunScriptParamId());
//				String testSetId = String.valueOf(fetchMetadataVO.getTestRunScriptId());
//				String copynumberValue;
//				String inputValue = fetchMetadataVO.getInputValue();
//
//				String[] arrOfStr = inputValue.split(">", 5);
//				if (arrOfStr.length < 2) {
//					copynumberValue = inputValue;
//				} else {
//					String Testrun_name = arrOfStr[0];
//					String seq = arrOfStr[1];
//					// String Script_num=arrOfStr[2];
//					String line_number = arrOfStr[2];
//					copynumberValue = testRunScriptExecService.getCopynumber(Testrun_name, seq, line_number, testParamId,
//							testSetId);
//				}
//				System.out.println("copynumberValue:::" + copynumberValue);
//
//				String value = globalValueForSteps;
//				waittill.click();
//				JavascriptExecutor jse = (JavascriptExecutor) driver;
//				jse.executeScript("arguments[0].value='" + copynumberValue + "';", waittill);
//				Thread.sleep(3000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Successfully paste is done " + scripNumber);
//				String xpath = "//input[@placeholder='inputParam']";
//				String action = fetchMetadataVO.getAction();
//				String lineNumber =String.valueOf( fetchMetadataVO.getLineNumber());
//				testRunScriptExecService.saveXpathParams(inputParam, "", scripNumber, xpath);
//
//				// testRunScriptExecService.saveXpathParams(inputParam,"",scripNumber,xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Paste Method");
//			screenshotFail(driver, "Failed during paste Method", fetchMetadataVO, configVO);
//			throw e;
//		}
//		try {
//
//			WebElement waittill = driver
//
//					.findElement(By.xpath("//label[text()='" + inputParam + "']/following::input[1]"));
//
//			// to get Dynamic copynumber
//			String testParamId =String.valueOf( fetchMetadataVO.getTestRunScriptParamId());
//			String testSetId = String.valueOf(fetchMetadataVO.getTestRunScriptId());
//			String copynumberValue;
//			String inputValue = fetchMetadataVO.getInputValue();
//
//			String[] arrOfStr = inputValue.split(">", 5);
//			if (arrOfStr.length < 2) {
//				copynumberValue = inputValue;
//			} else {
//				String Testrun_name = arrOfStr[0];
//				String seq = arrOfStr[1];
//				// String Script_num=arrOfStr[2];
//				String line_number = arrOfStr[2];
//				copynumberValue = testRunScriptExecService.getCopynumber(Testrun_name, seq, line_number, testParamId, testSetId);
//			}
//
//			System.out.println("copynumberValue:::" + copynumberValue);
//
//			String value = globalValueForSteps;
//
////	          String value = copynumber(driver, inputParam1, inputParam2, fetchMetadataVO, configVO)
//
//			waittill.click();
//
//			JavascriptExecutor jse = (JavascriptExecutor) driver;
//
//			jse.executeScript("arguments[0].value='" + copynumberValue + "';", waittill);
//
//			/*
//			 * 
//			 * Actions action = new Actions(driver);
//			 * 
//			 * action.click(waittill).build().perform();
//			 * 
//			 * action.doubleClick(waittill).build().perform();
//			 * 
//			 * action.sendKeys(value).build().perform();
//			 * 
//			 */
//
//			Thread.sleep(3000);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			String xpath = "//label[text()='inputParam']/following::input[1]";
//			String action = fetchMetadataVO.getAction();
//			String lineNumber = String.valueOf(fetchMetadataVO.getLineNumber());
//			testRunScriptExecService.saveXpathParams(inputParam, "", scripNumber, xpath);
//
////				testRunScriptExecService.saveXpathParams(inputParam,"",scripNumber,xpath);
//
//			return;
//
//		} catch (Exception e) {
//
//			System.out.println(e);
//
//		}
//
//		try {
//
//			WebElement waittill = driver.findElement(By.xpath("//input[@placeholder='" + inputParam + "']"));
//
//			// to get Dynamic copynumber
//			String testParamId = String.valueOf(fetchMetadataVO.getTestRunScriptParamId());
//			String testSetId = String.valueOf(fetchMetadataVO.getTestRunScriptId());
//			String copynumberValue;
//			String inputValue = fetchMetadataVO.getInputValue();
//
//			String[] arrOfStr = inputValue.split(">", 5);
//			if (arrOfStr.length < 2) {
//				copynumberValue = inputValue;
//			} else {
//				String Testrun_name = arrOfStr[0];
//				String seq = arrOfStr[1];
//				// String Script_num=arrOfStr[2];
//				String line_number = arrOfStr[2];
//				copynumberValue = testRunScriptExecService.getCopynumber(Testrun_name, seq, line_number, testParamId, testSetId);
//			}
//			String value = globalValueForSteps;
//			System.out.println("copynumberValue:::" + copynumberValue);
//			waittill.click();
//
//			JavascriptExecutor jse = (JavascriptExecutor) driver;
//
//			jse.executeScript("arguments[0].value='" + copynumberValue + "';", waittill);
//
//			Thread.sleep(3000);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Successfully paste is done " + scripNumber);
//			String xpath = "//input[@placeholder='inputParam']";
//			String action = fetchMetadataVO.getAction();
//			String lineNumber = String.valueOf(fetchMetadataVO.getLineNumber());
//			testRunScriptExecService.saveXpathParams(inputParam, "", scripNumber, xpath);
//
////		testRunScriptExecService.saveXpathParams(inputParam,"",scripNumber,xpath);
//
//			return;
//
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Paste Method");
//			screenshotFail(driver, "Failed during paste Method", fetchMetadataVO, configVO);
//			throw e;
//
//		}
//
//	}
//
//	public void clear(WebDriver driver, String inputParam1, String inputParam2, TestRunExecutionVO fetchMetadataVO,
//			ConfigVO configVO) {
//
//		try {
//			if (inputParam1.equalsIgnoreCase("Lines")) {
//				WebElement waittill = driver.findElement(By.xpath("(//*[normalize-space(text())=\"" + inputParam1
//						+ "\"]/following::label[normalize-space(text())='" + inputParam2
//						+ "']/preceding-sibling::input)[1]"));
//				clearMethod(driver, waittill);
//				Thread.sleep(4000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				String xpath = "(//*[normalize-space(text())='inputParam1']/following::label[normalize-space(text())='inputParam2']/preceding-sibling::input)[1]";
//				testRunScriptExecService.saveXpathParams(inputParam1, inputParam2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			System.out.println(e);
//		}
//		try {
//			if (inputParam2.equals("Accounting Period")) {
//				Thread.sleep(4000);
//				WebElement waittill = driver.findElement(
//						By.xpath("//label[normalize-space(text())='" + inputParam2 + "']/preceding-sibling::input[1]"));
//				clearMethod(driver, waittill);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Successfully Accounting Period Cleared" + scripNumber);
//				String xpath = "//label[normalize-space(text())='inputParam2']/preceding-sibling::input[1]";
//				testRunScriptExecService.saveXpathParams(inputParam1, inputParam2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed During Accounting Period Clear" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebElement waittill = driver.findElement(
//					By.xpath("(//label[contains(text(),'" + inputParam1 + "')]/preceding-sibling::input)[1]"));
//			clearMethod(driver, waittill);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Successfully Cleared" + scripNumber);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed During Clear" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			Thread.sleep(4000);
//			WebElement waittill = driver
//					.findElement(By.xpath("(//*[normalize-space(text())='" + inputParam1 + "']/following::input)[1]"));
//			clearMethod(driver, waittill);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Successfully Cleared" + scripNumber);
//			String xpath = "(//*[normalize-space(text())='inputParam1']/following::input)[1]";
//			testRunScriptExecService.saveXpathParams(inputParam1, inputParam2, scripNumber, xpath);
//			return;
//		} catch (Exception e) {
//			System.out.println(e);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed During Clear" + scripNumber);
//		}
//		try {
//			WebElement waittill = driver.findElement(By.xpath("//*[contains(@placeholder,'" + inputParam1 + "')]"));
//			clearMethod(driver, waittill);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Successfully Cleared" + scripNumber);
//			String xpath = "//*[contains(@placeholder,'inputParam1')]";
//			testRunScriptExecService.saveXpathParams(inputParam1, inputParam2, scripNumber, xpath);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed During Clear" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebElement waittill = driver
//					.findElement(By.xpath("//*[normalize-space(text())='" + inputParam1 + "']/following::textarea[1]"));
//			clearMethod(driver, waittill);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Successfully Cleared" + scripNumber);
//			String xpath = "//*[normalize-space(text())='inputParam1']/following::textarea[1]";
//			testRunScriptExecService.saveXpathParams(inputParam1, inputParam2, scripNumber, xpath);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed During Clear" + scripNumber);
//			screenshotFail(driver, "Failed during clearAndType Method", fetchMetadataVO, configVO);
//			System.out.println(e);
//			throw e;
//		}
//	}
//
//	public void windowclose(WebDriver driver, TestRunExecutionVO fetchMetadataVO, ConfigVO configVO) {
//		try {
//			String mainWindow = driver.getWindowHandle();
//			// It returns no. of windows opened by WebDriver and will return Set of Strings
//			Set<String> set = driver.getWindowHandles();
//			// Using Iterator to iterate with in windows
//			Iterator<String> itr = set.iterator();
//			while (itr.hasNext()) {
//				String childWindow = itr.next();
//				// Compare whether the main windows is not equal to child window. If not
//				// equal,we will close.
//				if (!mainWindow.equals(childWindow)) {
//					driver.switchTo().window(childWindow);
//					System.out.println(driver.switchTo().window(childWindow).getTitle());
//					driver.manage().window().maximize();
//					driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//					driver.switchTo().window(childWindow);
//					driver.close();
//					driver.switchTo().window(mainWindow);
//					String scripNumber = fetchMetadataVO.getScriptNumber();
//					log.info("Successfully Windowclosed" + scripNumber);
//				}
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed During WindowClose Acion." + scripNumber);
//			screenshot(driver, "Failed during windowhandle Method", fetchMetadataVO, configVO);
//			throw e;
//		}
//	}
//
//	public void switchToActiveElement(WebDriver driver, TestRunExecutionVO fetchMetadataVO, ConfigVO configVO) {
//		try {
//			driver.switchTo().activeElement();
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Switched to Element Successfully" + scripNumber);
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed During switchToActiveElement Action." + scripNumber);
//			screenshotFail(driver, "Failed during switchToActiveElement Method", fetchMetadataVO, configVO);
//			System.out.println(e.getMessage());
//			throw e;
//		}
//	}
//
//	public void clickMenu(WebDriver driver, String param1, String param2, TestRunExecutionVO fetchMetadataVO,
//			ConfigVO configVO) throws Exception {
//		try {
//			if (param1.equalsIgnoreCase("PDF")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions
//						.presenceOfElementLocated(By.xpath(("(//div[normalize-space(text())='" + param1 + "'])[2]"))));
//				// wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(("(//div[text()='"
//				// + param1 + "'])[1]")), param1));
//				WebElement waittext = driver
//						.findElement(By.xpath(("(//div[normalize-space(text())='" + param1 + "'])[2]")));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(80000);
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				String params = param1;
//				String xpath = "(//div[normalize-space(text())='param1'])[2]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				log.info("Sucessfully clicked Element in clickmenu " + scripNumber);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("failed during ClickMenu " + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@title='" + param1 + "']")));
//			Thread.sleep(4000);
//			WebElement waittext = driver.findElement(By.xpath("//div[@title='" + param1 + "']"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			String params = param1;
//			String xpath = "//div[@title='param1']";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			log.info("Sucessfully clicked Element in clickmenu " + scripNumber);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("failed during ClickMenu " + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions
//					.presenceOfElementLocated(By.xpath(("//a[normalize-space(text())='" + param1 + "']"))));
//			wait.until(ExpectedConditions.textToBePresentInElementLocated(
//					By.xpath(("//a[normalize-space(text())='" + param1 + "']")), param1));
//			WebElement waittext = driver.findElement(By.xpath(("//a[normalize-space(text())='" + param1 + "']")));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//
//			String xpath = "//a[normalize-space(text())='param1']";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			log.info("Sucessfully clicked Element in clickmenu " + scripNumber);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("failed during ClickMenu " + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
//					("//div[contains(@style,'display: block')]//div[normalize-space(text())='" + param1 + "']"))));
//			WebElement waittext = driver.findElement(By.xpath(
//					("//div[contains(@style,'display: block')]//div[normalize-space(text())='" + param1 + "']")));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//
//			String xpath = "//div[contains(@style,'display: block')]//div[normalize-space(text())='param1']";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			log.info("Sucessfully clicked Element in clickmenu " + scripNumber);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//
//			log.error("failed during ClickMenu " + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions
//					.presenceOfElementLocated(By.xpath(("//div[normalize-space(text())='" + param1 + "']"))));
//			wait.until(ExpectedConditions.textToBePresentInElementLocated(
//					By.xpath(("//div[normalize-space(text())='" + param1 + "']")), param1));
//			WebElement waittext = driver.findElement(By.xpath(("//div[normalize-space(text())='" + param1 + "']")));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			String xpath = "//div[normalize-space(text())='param1']";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			log.info("Sucessfully clicked Element in clickmenu " + scripNumber);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("failed during ClickMenu " + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions
//					.presenceOfElementLocated(By.xpath("(//div[contains(@id,'" + param1 + "')])[1]")));
//			WebElement waittext = driver.findElement(By.xpath("(//div[contains(@id,'" + param1 + "')])[1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			String params = param1;
//			String xpath = "(//div[contains(@id,'param1')])[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			log.info("Sucessfully clicked Element in clickmenu " + scripNumber);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			System.out.println(e);
//			log.error("failed during ClickMenu " + scripNumber);
//			screenshotFail(driver, "Failed during clickLink Method", fetchMetadataVO, configVO);
//			throw e;
//		}
//	}
//
//	public void clickSignInSignOut(WebDriver driver, String param1, TestRunExecutionVO fetchMetadataVO,
//			ConfigVO configVO) throws Exception {
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(
//					By.xpath(("//button[normalize-space(normalize-space(text())='" + param1 + "')]"))));
//			WebElement waittext = driver
//					.findElement(By.xpath(("//button[normalize-space(normalize-space(text())='" + param1 + "')]")));
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully clicked SingnInSignOut" + scripNumber);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			System.out.println(e);
//			log.error("Failed during SingnInSignOut " + scripNumber);
//			screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, configVO);
//			throw e;
//		}
//	}// *[text()='Action Required']/following::a[1]
//
//	public void clickNotificationLink(WebDriver driver, String param1, String param2, TestRunExecutionVO fetchMetadataVO,
//			ConfigVO configVO) throws Exception {
//
//		// placeholder changed from 'Enter Search Terms to Search' in Fusion Instance
//		try {
//			if (param1.equalsIgnoreCase("Notifications") && param2.equalsIgnoreCase("Search")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1
//						+ "']/following::input[@placeholder='" + param2 + "']/following::a[1]")));
//				Thread.sleep(4000);
//				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
//						+ "']/following::input[@placeholder='" + param2 + "']/following::a[1]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(2000);
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Successfully Clicked NotificationLink" + scripNumber);
//				String params = param1;
//				String xpath = "//*[normalize-space(text())='param1']/following::a[1]";
//				testRunScriptExecService.saveXpathParams(param1, "", scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed During NotificationLink" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(
//					By.xpath("//*[normalize-space(text())='" + param1 + "']/following::a[1]")));
//			Thread.sleep(4000);
//			WebElement waittext = driver
//					.findElement(By.xpath("//*[normalize-space(text())='" + param1 + "']/following::a[1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			Thread.sleep(2000);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Successfully Clicked NotificationLink" + scripNumber);
//			String params = param1;
//			String xpath = "//*[normalize-space(text())='param1']/following::a[1]";
//			testRunScriptExecService.saveXpathParams(param1, "", scripNumber, xpath);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed During NotificationLink" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions
//					.presenceOfElementLocated(By.xpath(("//*[@placeholder='" + param1 + "']/following::a[1]"))));
//			Thread.sleep(4000);
//			WebElement waittext = driver.findElement(By.xpath("//*[@placeholder='" + param1 + "']/following::a[1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			Thread.sleep(2000);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Successfully Clicked NotificationLink" + scripNumber);
//			String params = param1;
//			String xpath = "//*[@placeholder='param1']/following::a[1]";
//			testRunScriptExecService.saveXpathParams(param1, "", scripNumber, xpath);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			System.out.println(e);
//			log.error("Failed during NotificationLink" + scripNumber);
//			screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, configVO);
//			throw e;
//		}
//	}
//
//	public void clickButtonDropdown(WebDriver driver, String param1, String param2, String keysToSend,
//			TestRunExecutionVO fetchMetadataVO, ConfigVO configVO) throws Exception {
//		try {
//			// Changed == to equals method
//			if (param2.equals("")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@title='" + param1 + "']")));
//				WebElement waittext = driver.findElement(By.xpath("//a[@title='" + param1 + "']"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				clickButtonDropdownText(driver, param1, keysToSend, fetchMetadataVO, configVO);
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				String xpath = "//a[@title='" + param1 + "']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				log.info("Successfully Clicked ClickButtonDropdown" + scripNumber);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed During ClickButtonDropdown " + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			// Changed == to equals method
//			if (param2.equals("")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions
//						.presenceOfElementLocated(By.xpath("//h1[contains(text(),'" + param1 + "')]/following::a[1]")));
//				WebElement waittext = driver
//						.findElement(By.xpath("//h1[contains(text(),'" + param1 + "')]/following::a[1]"));
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				clickButtonDropdownText(driver, param1, keysToSend, fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				String xpath = "//h1[contains(text(),'" + param1 + "')]/following::a[1]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				log.info("Successfully Clicked ClickButtonDropdown" + scripNumber);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed During clickButtonDropdown " + scripNumber);
//			System.out.println(e);
//
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
//					"(//h1[normalize-space(text())='" + param1 + "']/following::a[@title='" + param2 + "'])[1]")));
//			wait.until(ExpectedConditions.textToBePresentInElementLocated(
//					By.xpath(("//h1[normalize-space(text())='" + param1 + "']")), param1));
//			Thread.sleep(6000);
//			WebElement waittext = driver.findElement(By.xpath(
//					"(//h1[normalize-space(text())='" + param1 + "']/following::a[@title='" + param2 + "'])[1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			clickButtonDropdownText(driver, param1, keysToSend, fetchMetadataVO, configVO);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			String xpath = "(//h1[normalize-space(text())='" + param1 + "']/following::a[@title='" + param2 + "'])[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			log.info("Successfully Clicked ClickButtonDropdown" + scripNumber);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed During clickButtonDropdown " + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param2.equalsIgnoreCase("Publish to Managers")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[normalize-space(text())='"
//						+ param1 + "']/following::a[normalize-space(text())='" + param2 + "'])[2]")));
//				WebElement waittext = driver.findElement(By.xpath("(//h1[normalize-space(text())='" + param1
//						+ "']/following::a[normalize-space(text())='" + param2 + "'])[2]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				clickButtonDropdownText(driver, param1, keysToSend, fetchMetadataVO, configVO);
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				String xpath = "(//h1[normalize-space(text())='param1']/following::a[normalize-space(text())='param2'])[2]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				log.info("Successfully Clicked ClickButtonDropdown" + scripNumber);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed During clickButtonDropdown " + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[normalize-space(text())='" + param1
//					+ "']/following::a[normalize-space(text())='" + param2 + "'])[1]")));
//			WebElement waittext = driver.findElement(By.xpath("(//h1[normalize-space(text())='" + param1
//					+ "']/following::a[normalize-space(text())='" + param2 + "'])[1]"));
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			clickButtonDropdownText(driver, param1, keysToSend, fetchMetadataVO, configVO);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			String xpath = "(//h1[normalize-space(text())='param1']/following::a[normalize-space(text())='param2'])[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			log.info("Successfully Clicked ClickButtonDropdown" + scripNumber);
//			return;
//		} catch (Exception e) {
//			System.out.println(e);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed During clickButtonDropdown " + scripNumber);
//			screenshotFail(driver, "Failed during clickLink Method", fetchMetadataVO, configVO);
//			throw e;
//		}
//	}
//
//	public void clickButtonDropdownText(WebDriver driver, String param1, String keysToSend,
//			TestRunExecutionVO fetchMetadataVO, ConfigVO configVO) throws Exception {
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions
//					.presenceOfElementLocated(By.xpath("//li[normalize-space(text())='" + keysToSend + "']")));
//			wait.until(ExpectedConditions
//					.presenceOfElementLocated(By.xpath("//li[normalize-space(text())='" + keysToSend + "']")));
//			wait.until(ExpectedConditions.textToBePresentInElementLocated(
//					By.xpath(("//li[normalize-space(text())='" + keysToSend + "']")), keysToSend));
//			Thread.sleep(5000);
//			WebElement waittext = driver.findElement(By.xpath("//li[normalize-space(text())='" + keysToSend + "']"));
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Successfully Clicked ClickButtonDropdownText" + scripNumber);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed During clickButtonDropdownText " + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
//					"//div[contains(@class,'PopupMenuContent')]//td[normalize-space(text())='" + keysToSend + "']")));
//			WebElement waittext = driver.findElement(By.xpath(
//					"//div[contains(@class,'PopupMenuContent')]//td[normalize-space(text())='" + keysToSend + "']"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Successfully Clicked ClickButtonDropdownText" + scripNumber);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed During clickButtonDropdownText " + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions
//					.presenceOfElementLocated(By.xpath("//td[normalize-space(text())='" + keysToSend + "']")));
//			WebElement waittext = driver.findElement(By.xpath("//td[normalize-space(text())='" + keysToSend + "']"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Successfully Clicked ClickButtonDropdownText" + scripNumber);
//			return;
//		} catch (Exception e) {
//			System.out.println(e);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed During clickButtonDropdownText " + scripNumber);
//			screenshotFail(driver, "Failed during clickLink Method", fetchMetadataVO, configVO);
//			throw e;
//		}
//	}
//
//	public void clickExpandorcollapse(WebDriver driver, String param1, String param2, TestRunExecutionVO fetchMetadataVO,
//			ConfigVO configVO) throws Exception {
//		try {
//			if (param1.equalsIgnoreCase("Process Monitor")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
//						"(//h2[normalize-space(text())='" + param1 + "']/preceding::*[@title='" + param2 + "'])[1]")));
//				wait.until(ExpectedConditions.textToBePresentInElementLocated(
//						By.xpath("//h2[normalize-space(text())='" + param1 + "']"), param1));
//				WebElement waittext = driver.findElement(By.xpath(
//						"(//h2[normalize-space(text())='" + param1 + "']/preceding::*[@title='" + param2 + "'])[1]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(4000);
//				try {
//					WebElement Expand = driver.findElement(By.xpath("(//h2[normalize-space(text())='" + param1
//							+ "']/following::*[@title='" + param2 + "'])[1]"));
//					Expand.click();
//					String scripNumber = fetchMetadataVO.getScriptNumber();
//					log.info("Sucessfully Clicked Expanded or Collapsed" + scripNumber);
//
//				} catch (Exception e) {
//
//					String scripNumber = fetchMetadataVO.getScriptNumber();
//					log.error("Failed During ClickExpand or Collapse" + scripNumber);
//				}
//
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				String xpath = "(//h1[normalize-space(text())='param1']/preceding::*[@title='param2'])[1]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				log.info("Sucessfully Clicked Process Monitor ClickExpand or Collapse" + scripNumber);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed During Process Monitor ClickExpand or Collapse" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
//					"(//h2[normalize-space(text())='" + param1 + "']/following::*[@title='" + param2 + "'])[1]")));
//			wait.until(ExpectedConditions.textToBePresentInElementLocated(
//					By.xpath("//h2[normalize-space(text())='" + param1 + "']"), param1));
//			WebElement waittext = driver.findElement(By.xpath(
//					"(//h2[normalize-space(text())='" + param1 + "']/following::*[@title='" + param2 + "'])[1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			Thread.sleep(4000);
//			try {
//				WebElement Expand = driver.findElement(By.xpath(
//						"(//h2[normalize-space(text())='" + param1 + "']/following::*[@title='" + param2 + "'])[1]"));
//				Expand.click();
//			} catch (Exception e) {
//
//			}
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked ClickExpand or Collapse" + scripNumber);
//			String xpath = "(//h2[normalize-space(text())='param1']/following::*[@title='param2'])[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed During ClickExpand or Collapse" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
//					"(//h1[normalize-space(text())='" + param1 + "']/preceding::*[@title='" + param2 + "'])[1]")));
//			wait.until(ExpectedConditions.textToBePresentInElementLocated(
//					By.xpath("//h1[normalize-space(text())='" + param1 + "']"), param1));
//			WebElement waittext = driver.findElement(By.xpath(
//					"(//h1[normalize-space(text())='" + param1 + "']/preceding::*[@title='" + param2 + "'])[1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			Thread.sleep(4000);
//			try {
//				WebElement Expand = driver.findElement(By.xpath(
//						"(//h1[normalize-space(text())='" + param1 + "']/preceding::*[@title='" + param2 + "'])[1]"));
//				Expand.click();
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked ClickExpand or Collapse" + scripNumber);
//
//			} catch (Exception e) {
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.error("Failed During ClickExpand or Collapse" + scripNumber);
//
//			}
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked ClickExpand or Collapse" + scripNumber);
//			String xpath = "(//h1[normalize-space(text())='param1']/preceding::*[@title='param2'])[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			return;
//
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed During ClickExpand or Collapse" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			Thread.sleep(4000);
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
//					"(//span[contains(text(),'" + param1 + "')])[1]/preceding::a[3][@title='" + param2 + "'][1]")));
//			WebElement waittext = driver.findElement(By.xpath(
//					"(//span[contains(text(),'" + param1 + "')])[1]/preceding::a[3][@title='" + param2 + "'][1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			Thread.sleep(1000);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked ClickExpand or Collapse" + scripNumber);
//			String xpath = "(//span[contains(text(),'param1')])[1]/preceding::a[3][@title='param2'][1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed During ClickExpand or Collapse" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
//					"(//*[normalize-space(text())='" + param1 + "']/following::*[@title='" + param2 + "'])[1]")));
//			WebElement waittext = driver.findElement(By
//					.xpath("(//*[normalize-space(text())='" + param1 + "']/following::*[@title='" + param2 + "'])[1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			Thread.sleep(4000);
//			try {
//				WebElement Expand = driver.findElement(By.xpath(
//						"(//*[normalize-space(text())='" + param1 + "']/following::*[@title='" + param2 + "'])[1]"));
//				Expand.click();
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked ClickExpand or Collapse" + scripNumber);
//			} catch (Exception e) {
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.error("Failed During ClickExpand or Collapse" + scripNumber);
//			}
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked ClickExpand or Collapse" + scripNumber);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed During ClickExpand or Collapse" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
//					"(//*[normalize-space(text())='" + param1 + "']/preceding::*[@title='" + param2 + "'])[1]")));
//			WebElement waittext = driver.findElement(By
//					.xpath("(//*[normalize-space(text())='" + param1 + "']/preceding::*[@title='" + param2 + "'])[1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			Thread.sleep(4000);
//			try {
//				WebElement Expand = driver.findElement(By.xpath(
//						"(//*[normalize-space(text())='" + param1 + "']/preceding::*[@title='" + param2 + "'])[1]"));
//				Expand.click();
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked ClickExpand or Collapse" + scripNumber);
//			} catch (Exception e) {
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.error("Failed During ClickExpand or Collapse" + scripNumber);
//
//			}
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked ClickExpand or Collapse" + scripNumber);
//			String xpath = "(//*[normalize-space(text())='param1']/preceding::*[@title='param2'])[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed During ClickExpand or Collapse" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
//					"//*[normalize-space(text())='" + param1 + "']/following::*[normalize-space(text())='" + param2
//							+ "']/preceding::*[@title='Expand' and @href and not(@style='display:none')][1]")));
//			WebElement waittext = driver.findElement(
//					By.xpath("//*[normalize-space(text())='" + param1 + "']/following::*[normalize-space(text())='"
//							+ param2 + "']/preceding::*[@title='Expand' and @href and not(@style='display:none')][1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			Thread.sleep(4000);
//			try {
//				WebElement Expand = driver.findElement(By.xpath(
//						"//*[normalize-space(text())='" + param1 + "']/following::*[normalize-space(text())='" + param2
//								+ "']/preceding::*[@title='Expand' and @href and not(@style='display:none')][1]"));
//				Expand.click();
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked ClickExpand or Collapse" + scripNumber);
//			} catch (Exception e) {
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.error("Failed During ClickExpand or Collapse" + scripNumber);
//
//			}
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked ClickExpand or Collapse" + scripNumber);
//			String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']/preceding::*[@title='Expand' and @href and not(@style='display:none')][1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed During ClickExpand or Collapse" + scripNumber);
//			System.out.println(e);
//			screenshotFail(driver, "Failed during clickExpandorcollapse", fetchMetadataVO, configVO);
//			throw e;
//		}
//	}
//
//	public void selectAValue(WebDriver driver, String param1, String param2, String keysToSend,
//			TestRunExecutionVO fetchMetadataVO, ConfigVO configVO) throws Exception {
//		try {
//			if (param1.equalsIgnoreCase("Review installments") || param1.equalsIgnoreCase("Review proposed payments")
//					|| param1.equalsIgnoreCase("Record print status")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
//						"//*[normalize-space(text())='" + keysToSend + "']/following::img[@title='" + param1 + "']")));
//				WebElement waittext = driver.findElement(By.xpath(
//						"//*[normalize-space(text())='" + keysToSend + "']/following::img[@title='" + param1 + "']"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(2000);
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Review installments selectAValue" + scripNumber);
//				String xpath = "//*[normalize-space(text())='keysToSend']/following::img[@title='param1']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Review installments selectAValue" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Payment Process Requests") && param1.equalsIgnoreCase("Name")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
//						"(//*[contains(text(),'" + param2 + "')]/following::*[normalize-space(text())='" + keysToSend
//								+ "'][1]/following::img/following::a[contains(@id,'RecentlyCompletedPpr')])[2]")));
//				WebElement waittext = driver.findElement(By.xpath(
//						"(//*[contains(text(),'\" + param2 + \"')]/following::*[normalize-space(text())='\" + keysToSend + \"'][1]/following::img/following::a[contains(@id,'RecentlyCompletedPpr')])[2]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(2000);
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Review installments selectAValue" + scripNumber);
//				String xpath = "//*[normalize-space(text())='keysToSend']/following::img[@title='param1']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Review installments selectAValue" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			Thread.sleep(5000);
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(),'" + param1
//					+ "')]/following::*[normalize-space(text())='" + keysToSend + "'][1]")));
//			WebElement waittext = driver.findElement(By.xpath("//*[contains(text(),'" + param1
//					+ "')]/following::*[normalize-space(text())='" + keysToSend + "'][1]"));
//			Thread.sleep(2000);
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			Thread.sleep(5000);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked selectAValue" + scripNumber);
//			String xpath = "//*[contains(text(),'param1')]/following::*[normalize-space(text())='keysToSend'][1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during selectAValue" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1
//					+ "']/following::label[normalize-space(text())='" + param2 + "']")));
//			WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
//					+ "']/following::label[normalize-space(text())='" + param2 + "']"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			Thread.sleep(2000);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked selectAValue" + scripNumber);
//			String xpath = "//*[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during selectAValue" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + keysToSend
//					+ "']/following::*[normalize-space(text())='" + param1 + "']")));
//			WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + keysToSend
//					+ "']/following::*[normalize-space(text())='" + param1 + "']"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			Thread.sleep(2000);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked selectAValue" + scripNumber);
//			String xpath = "//*[normalize-space(text())='keysToSend']/following::*[normalize-space(text())='param1']";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			return;
//		} catch (Exception e) {
//			System.out.println(e);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during selectAValue" + scripNumber);
//			screenshotFail(driver, "Failed during clickExpandorcollapse", fetchMetadataVO, configVO);
//			throw e;
//		}
//	}
//
//	public String clickTableImage(WebDriver driver, String param1, String param2, String keysToSend,
//			TestRunExecutionVO fetchMetadataVO, ConfigVO configVO) throws Exception {
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(
//					By.xpath("//*[normalize-space(text())='" + param1 + "']/following::*[normalize-space(text())='"
//							+ keysToSend + "']/following::img[contains(@id,'" + param2 + "')][1]")));
//			Thread.sleep(4000);
//			WebElement waittill = driver.findElement(
//					By.xpath("//*[normalize-space(text())='" + param1 + "']/following::*[normalize-space(text())='"
//							+ keysToSend + "']/following::img[contains(@id,'" + param2 + "')][1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittill).build().perform();
//			Thread.sleep(2000);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			clickValidateXpath(driver, fetchMetadataVO, waittill, configVO);
//			Thread.sleep(500);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='keysToSend']/following::img[contains(@id,'param2')][1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			log.info("Sucessfully Clicked clickTableImage" + scripNumber);
//			return keysToSend;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickTableImage" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(
//					By.xpath("//*[normalize-space(text())='" + param1 + "']/following::*[normalize-space(text())='"
//							+ keysToSend + "']/following::img[@title='" + param2 + "'][1]")));
//			WebElement waittill = driver.findElement(
//					By.xpath("//*[normalize-space(text())='" + param1 + "']/following::*[normalize-space(text())='"
//							+ keysToSend + "']/following::img[@title='" + param2 + "'][1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittill).build().perform();
//			Thread.sleep(2000);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			clickValidateXpath(driver, fetchMetadataVO, waittill, configVO);
//			Thread.sleep(500);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='keysToSend']/following::img[@title='param2'][1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			log.info("Sucessfully Clicked clickTableImage" + scripNumber);
//			return keysToSend;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickTableImage" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1
//					+ "']/following::*[@value='" + keysToSend + "']/following::img[@title='" + param2 + "'][1]")));
//			WebElement waittill = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
//					+ "']/following::*[@value='" + keysToSend + "']/following::img[@title='" + param2 + "'][1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittill).build().perform();
//			Thread.sleep(2000);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			clickValidateXpath(driver, fetchMetadataVO, waittill, configVO);
//			Thread.sleep(500);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			String xpath = "//*[normalize-space(text())='param1']/following::*[@value='keysToSend']/following::img[@title='param2'][1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			log.info("Sucessfully Clicked clickTableImage" + scripNumber);
//			return keysToSend;
//		} catch (Exception e) {
//			System.out.println(e);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickTableImage" + scripNumber);
//			screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, configVO);
//			throw e;
//		}
//	}
//
//	public void clickImage(WebDriver driver, String param1, String param2, TestRunExecutionVO fetchMetadataVO,
//			ConfigVO configVO) throws Exception {
//		try {
//			if (param2.equalsIgnoreCase("Add Row")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(
//						By.xpath("//*[contains(text(),'" + param1 + "')]/following::img[@title='" + param2 + "'][1]")));
//				WebElement waittext = driver.findElement(
//						By.xpath("//*[contains(text(),'" + param1 + "')]/following::img[@title='" + param2 + "'][1]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				waittext.click();
//				// clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked clickImage" + scripNumber);
//				String xpath = "//*[contains(text(),'param1')]/following::img[@title='param2'][1]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickImag" + scripNumber);
//			System.out.println(e);
//		}
//		// label[contains(text(),'Enter Cost Centre')]/following::input[1]
//		try {
//			if (param1.equalsIgnoreCase("Report")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(
//						By.xpath("//*[contains(text(),'" + param2 + "')/following::input[1]]")));
//				WebElement waittext = driver
//						.findElement(By.xpath("//*[contains(text(),'" + param2 + "')/following::input[1]]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Report clickImage" + scripNumber);
//				String xpath = "//*[contains(text(),'param2')/following::input[1]]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Report clickImag" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			// Changed == to equals method
//			if (param2.equals("")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@title='" + param1 + "']")));
//				WebElement waittext = driver.findElement(By.xpath("//img[@title='" + param1 + "']"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(8000);
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked clickImage" + scripNumber);
//				String params = param1;
//				String xpath = "//img[@title='param1']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickImag" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Customer")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By
//						.xpath("//*[normalize-space(text())='" + param1 + "']/following::a[@title='" + param2 + "']")));
//				WebElement waittext = driver.findElement(By
//						.xpath("//*[normalize-space(text())='" + param1 + "']/following::a[@title='" + param2 + "']"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Customer clickImage" + scripNumber);
//				String xpath = "//*[normalize-space(text())='param1']/following::a[@title='param2']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Clicked clickImag" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Add to Selected") || param1.equalsIgnoreCase("Remove from Selected")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(
//						ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@title='" + param1 + "']//img[1]")));
//				WebElement waittext = driver.findElement(By.xpath("//a[@title='" + param1 + "']//img[1]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Add to Selected clickImage" + scripNumber);
//				String xpath = "//a[@title='param1']//img[1]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Add to Selected clickImag" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param2.equalsIgnoreCase("Go to Member Selection")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By
//						.xpath("//*[contains(text(),'" + param1 + "')]/following::input[@title='" + param2 + "'][1]")));
//				WebElement waittext = driver.findElement(By
//						.xpath("//*[contains(text(),'" + param1 + "')]/following::input[@title='" + param2 + "'][1]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Go to Member Selection clickImage" + scripNumber);
//				String xpath = "//*[contains(text(),'param1')]/following::input[@title='param2'][1]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Go to Member Selection clickImag" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Provider") || param1.equalsIgnoreCase("Receiver")) {
//				Thread.sleep(4000);
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
//						"//*[normalize-space(text())='" + param1 + "']/following::img[@title='" + param2 + "'][2]")));
//				WebElement waittext = driver.findElement(By.xpath(
//						"//*[normalize-space(text())='" + param1 + "']/following::img[@title='" + param2 + "'][2]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Provider or Receiver clickImage" + scripNumber);
//				String xpath = "//*[normalize-space(text())='param1']/following::img[@title='param2'][2]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Provider or Receiver clickImag" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			// Changed == to equals method
//			if (param2.equals("")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(
//						ExpectedConditions.presenceOfElementLocated(By.xpath("//img[contains(@id,'" + param1 + "')]")));
//				WebElement waittext = driver.findElement(By.xpath("//img[contains(@id,'" + param1 + "')]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked clickImage" + scripNumber);
//				String params = param1;
//				String xpath = "//img[contains(@id,'param1')]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickImag" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			// Changed == to equals method
//			if (param2.equals("")) {
//				Thread.sleep(3000);
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@title='" + param1 + "']")));
//				WebElement waittext = driver.findElement(By.xpath("//a[@title='" + param1 + "']"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked clickImage" + scripNumber);
//				String params = param1;
//				String xpath = "//a[@title='param1']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickImag" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param2.equalsIgnoreCase("Back")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(
//						By.xpath("//h1[normalize-space(text())='" + param1 + "']/preceding::a[1]")));
//				WebElement waittext = driver
//						.findElement(By.xpath("//h1[normalize-space(text())='" + param1 + "']/preceding::a[1]"));
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Back clickImage" + scripNumber);
//				String xpath = "//h1[normalize-space(text())='param1']/preceding::a[1]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Back clickImag" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(
//					By.xpath("(//h1[normalize-space(text())='" + param1 + "']/following::div[@role='button'])[1]")));
//			Thread.sleep(2000);
//			WebElement waittext = driver.findElement(
//					By.xpath("(//h1[normalize-space(text())='" + param1 + "']/following::div[@role='button'])[1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			Thread.sleep(3000);
//			WebElement add = driver.findElement(
//					By.xpath("//h1[normalize-space(text())='" + param1 + "']/following::img[@title='" + param2 + "']"));
//			clickValidateXpath(driver, fetchMetadataVO, add, configVO);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked clickImage" + scripNumber);
//			String xpath = "(//h1[normalize-space(text())='param1']/following::div[@role='button'])[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickImag" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
//					"(//h1[normalize-space(text())='" + param1 + "']/following::img[@title='" + param2 + "'])[1]")));
//			WebElement waittext = driver.findElement(By.xpath(
//					"(//h1[normalize-space(text())='" + param1 + "']/following::img[@title='" + param2 + "'])[1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked clickImage" + scripNumber);
//			String xpath = "(//h1[normalize-space(text())='param1']/following::img[@title='param2'])[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickImag" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
//					"//*[normalize-space(text())='" + param1 + "']/following::img[@title='" + param2 + "'][1]")));
//			WebElement waittext = driver.findElement(By
//					.xpath("//*[normalize-space(text())='" + param1 + "']/following::img[@title='" + param2 + "'][1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			Thread.sleep(8000);
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked clickImage" + scripNumber);
//			String xpath = "//*[normalize-space(text())='param1']/following::img[@title='param2'][1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickImag" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(
//					By.xpath("(//*[normalize-space(text())='" + param1 + "']/following::div[@role='button'])[1]")));
//			WebElement waittext = driver.findElement(
//					By.xpath("(//*[normalize-space(text())='" + param1 + "']/following::div[@role='button'])[1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			Thread.sleep(3000);
//			WebElement add = driver.findElement(
//					By.xpath("//*[normalize-space(text())='" + param1 + "']/following::img[@title='" + param2 + "']"));
//			clickValidateXpath(driver, fetchMetadataVO, add, configVO);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked clickImage" + scripNumber);
//			String xpath = "(//*[normalize-space(text())='param1']/following::div[@role='button'])[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickImag" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
//					"//*[normalize-space(text())='" + param1 + "']/following::img[contains(@id,'" + param2 + "')]")));
//			WebElement waittext = driver.findElement(By.xpath(
//					"//*[normalize-space(text())='" + param1 + "']/following::img[contains(@id,'" + param2 + "')]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			waittext.click();
////			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			Thread.sleep(2000);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked clickImage" + scripNumber);
//			String xpath = "//*[normalize-space(text())='param1']/following::img[contains(@id,'param2')]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickImag" + scripNumber);
//			System.out.println(e);
//		}
//
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1
//					+ "']/following::*[normalize-space(text())='" + param2 + "']/following::img[1]")));
//			WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
//					+ "']/following::*[normalize-space(text())='" + param2 + "']/following::img[1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			waittext.click();
//			// clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked clickImage" + scripNumber);
//			String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']/following::img[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickImag" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions
//					.presenceOfElementLocated(By.xpath("//*[contains(@aria-label,'" + param1 + "')]")));
//			WebElement waittext = driver.findElement(By.xpath("//*[contains(@aria-label,'" + param1 + "')]"));
//			Actions actions = new Actions(driver);
//			waittext.click();
//			actions.moveToElement(waittext).build().perform();
//			// clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked clickImage" + scripNumber);
//			String xpath = "//*[contains(@aria-label,'param1')]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickImag" + scripNumber);
//			screenshotFail(driver, "Failed during click Image Method", fetchMetadataVO, configVO);
//			throw e;
//		}
//	}
//
//	public void clickButton(WebDriver driver, String param1, String param2, TestRunExecutionVO fetchMetadataVO,
//			ConfigVO configVO) throws Exception {
//		try {
//			if (param1.equalsIgnoreCase("Applied Receipts Register") && param2.equalsIgnoreCase("Apply")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				WebElement waittext = driver.findElement(By.xpath("//input[@value='Apply']"));
////                screenshot(driver, "", fetchMetadataVO, configVO);
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(15000);
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Create Time Card clickButton" + scripNumber);
//				String xpath = "//input[@value='Apply']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//				return;
//			}
//
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed clickButton" + scripNumber);
//
//			System.out.println(e);
//		}
//		try {
//			if (param2.equalsIgnoreCase("Save and Close") && (param1.equalsIgnoreCase("Manage Expenditure Types")
//					|| param1.equalsIgnoreCase("Manage Rate Schedules"))) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				WebElement waittext = driver.findElement(By.xpath(("(//span[text()='S'])[2]")));
////                screenshot(driver, "", fetchMetadataVO, configVO);
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(15000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Create Time Card clickButton" + scripNumber);
//				String xpath = "(//span[text()='S'])[2]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//				return;
//			}
//
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Create Time Card clickButton" + scripNumber);
//
//			System.out.println(e);
//		}
//		// for "PTP.PO.212 Split requisition lines" when exectuing in Fusion instance
//		try {
//			if (param1.equalsIgnoreCase("Create Address") && param2.equalsIgnoreCase("OK")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				Thread.sleep(8000);
//				wait.until(ExpectedConditions.presenceOfElementLocated(
//						By.xpath(("//*[normalize-space(text())='Create Address']/following::*[text()='K'][1]"))));
//				WebElement waittext = driver.findElement(
//						By.xpath(("//*[normalize-space(text())='Create Address']/following::*[text()='K'][1]")));
////                screenshot(driver, "", fetchMetadataVO, configVO);
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				Thread.sleep(4000);
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(4000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Ok clickButton" + scripNumber);
//				String xpath = "//*[normalize-space(text())='Create Address']/following::*[text()='K'][1]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			System.out.println(e);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Ok clickButton" + scripNumber);
//		}
//
//		try {
//			if (param1.equalsIgnoreCase("Submit")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				WebElement waittext = driver.findElement(By.xpath(("//*[text()='Submit' or text()='S']")));// screenshot(driver,
//																											// "",
//																											// fetchMetadataVO,
//																											// configVO);
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(15000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Create Time Card clickButton" + scripNumber);
//				String xpath = "//*[text()='Submit or text()='S']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//				return;
//			}
//
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickButton" + scripNumber);
//
//			System.out.println(e);
//		}
////for "PTP.AP.327 Applying a prepayment to Invoice" when exectuing in Fusion instance
//		try {
//			if (param2.equalsIgnoreCase("Save and Close") && !param1.equalsIgnoreCase("Unapply Application")
//					&& !param1.equalsIgnoreCase("Manage Distributions") && !param1.equalsIgnoreCase("Manage Holds")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				WebElement waittext = driver.findElement(By.xpath(("//span[text()='S']")));
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(15000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Create Time Card clickButton" + scripNumber);
//				String xpath = "//span[text()='S']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Create Time Card clickButton" + scripNumber);
//		}
//		try {
//			if ((param1.equalsIgnoreCase("Manage Organization Trees")
//					|| param1.equalsIgnoreCase("Edit Project Template")
//					|| param1.equalsIgnoreCase("Manage Project Templates")) && param2.equalsIgnoreCase("Done")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("(//span[text()='o'])[2]"))));
//
//				WebElement waittext = driver.findElement(By.xpath(("(//span[text()='o'])[2]")));
////                screenshot(driver, "", fetchMetadataVO, configVO);
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Done clickButton" + scripNumber);
//				log.info("Sucessfully Clicked Done clickButton" + scripNumber);
//				String xpath = "(//span[text()='o'])[2]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickButton Done" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Done")) {
//				Thread.sleep(3000);
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				Thread.sleep(5000);
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[text()='o']"))));
//				WebElement waittext = driver.findElement(By.xpath(("//span[text()='o']")));
////                screenshot(driver, "", fetchMetadataVO, configVO);
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Done clickButton" + scripNumber);
//				log.info("Sucessfully Clicked Done clickButton" + scripNumber);
//				String xpath = "//span[text()='o']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickButton Done" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Notifications")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(
//						By.xpath(("//h1[normalize-space(text())='Notifications']/following::button[text()='" + param2
//								+ "'][1]"))));
//				WebElement waittext = driver.findElement(
//						By.xpath(("//h1[normalize-space(text())='Notifications']/following::button[text()='" + param2
//								+ "'][1]")));
//				// screenshot(driver, "", fetchMetadataVO, configVO);
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(15000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Members clickButton" + scripNumber);
//				String xpath = "//button[@title='param2']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Members clickButton" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Expend")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				WebElement waittext = driver
//						.findElement(By.xpath(("//div[contains(@class,'Overflow')]//div[@role='button']")));
////				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(15000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Create Time Card clickButton" + scripNumber);
//				String xpath = "//div[contains(@class,'Overflow')]//div[@role='button']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//				return;
//			}
//
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Create Time Card clickButton" + scripNumber);
//
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Create Time Card") && param2.equalsIgnoreCase("OK")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(
//						By.xpath(("//*[text()='Create Time Card']/following::span[text()='K']"))));
//				WebElement waittext = driver
//						.findElement(By.xpath(("//*[text()='Create Time Card']/following::span[text()='K']")));
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(15000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Create Time Card clickButton" + scripNumber);
//				String xpath = "//*[text()='Create Time Card']/following::span[text()='K']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Create Time Card clickButton" + scripNumber);
//
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Edit Line")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				Thread.sleep(8000);
//				System.out.println("here1234");
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
//						("//*[contains(text(),'" + param1 + "')]/following::span[normalize-space(text())='K']"))));
//				WebElement waittext = driver.findElement(By.xpath(
//						("//*[contains(text(),'" + param1 + "')]/following::span[normalize-space(text())='K']")));
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				Thread.sleep(4000);
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(4000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Edit Line clickButton" + scripNumber);
//				String xpath = "//*[contains(text(),'param1')]/following::span[normalize-space(text())='K']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Edit Line clickButton" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Members") || param1.equalsIgnoreCase("Complete Report")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(
//						ExpectedConditions.presenceOfElementLocated(By.xpath(("//button[@title='" + param2 + "']"))));
//				WebElement waittext = driver.findElement(By.xpath(("//button[@title='" + param2 + "']")));
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(15000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Members clickButton" + scripNumber);
//				String xpath = "//button[@title='param2']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Members clickButton" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Address Contacts") && param2.equalsIgnoreCase("OK")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
//						"//*[normalize-space(text())='" + param1 + "']/following::button[@title='" + param2 + "']")));
//				WebElement waittext = driver.findElement(By.xpath(
//						"//*[normalize-space(text())='" + param1 + "']/following::button[@title='" + param2 + "']"));
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(5000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Address Contacts clickButton" + scripNumber);
//				String xpath = "//*[normalize-space(text())='" + param1 + "']/following::button[@title='param2']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Address Contacts clickButton" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			Thread.sleep(2000);
//			if (param2.equalsIgnoreCase("OK")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				Thread.sleep(8000);
//				wait.until(ExpectedConditions.presenceOfElementLocated(
//						By.xpath(("//div[contains(@id,'RejectPopup::content')]//span[text()='K']"))));
//				// wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[text()='ne']"),
//				// "ne"));
//				WebElement waittext = driver
//						.findElement(By.xpath(("//div[contains(@id,'RejectPopup::content')]//span[text()='K']")));
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(4000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Ok clickButton" + scripNumber);
//				String xpath = "//div[contains(@id,'RejectPopup::content')]//span[text()='K']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Ok clickButton" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param2.equalsIgnoreCase("OK")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				Thread.sleep(8000);
//				wait.until(ExpectedConditions.presenceOfElementLocated(
//						By.xpath(("//*[normalize-space(text())='Search']/following::*[normalize-space(text())='"
//								+ param1 + "']/following::*[not (@aria-disabled) and text()='OK'][1]"))));
//				WebElement waittext = driver.findElement(
//						By.xpath(("//*[normalize-space(text())='Search']/following::*[normalize-space(text())='"
//								+ param1 + "']/following::*[not (@aria-disabled) and text()='OK'][1]")));
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				Thread.sleep(4000);
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(4000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Ok clickButton" + scripNumber);
//				String xpath = "//*[normalize-space(text())='Search']/following::*[normalize-space(text())='param1']/following::*[not (@aria-disabled) and text()='OK'][1]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			System.out.println(e);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Ok clickButton" + scripNumber);
//		}
//		try {
//			if (param2.equalsIgnoreCase("OK")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				Thread.sleep(8000);
//				wait.until(ExpectedConditions.presenceOfElementLocated(
//						By.xpath(("//[contains(text(),'" + param1 + "')]/following::span[text()='K']"))));
//				WebElement waittext = driver
//						.findElement(By.xpath(("//[contains(text(),'" + param1 + "')]/following::span[text()='K']")));
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(4000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Ok clickButton" + scripNumber);
//				String xpath = "//[contains(text(),'param1')]/following::span[text()='K']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			System.out.println(e);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Ok clickButton" + scripNumber);
//		}
//		try {
//
//			if (param2.equalsIgnoreCase("Select")) {
//				Thread.sleep(2000);
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("(//input[contains(@value,'" + param1
//						+ "') and (@type)]/following::button[contains(text(),'" + param2 + "')])[1]"))));
//				WebElement waittext = driver.findElement(By.xpath(("(//input[contains(@value,'" + param1
//						+ "') and (@type)]/following::button[contains(text(),'" + param2 + "')])[1]")));
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(4000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Select clickButton" + scripNumber);
//				String xpath = "(//input[contains(@value,'param1') and (@type)]/following::button[contains(text(),'param2')])[1]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			System.out.println(e);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Select clickButton" + scripNumber);
//		}
//		try {
//			if (param2.equalsIgnoreCase("Done")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				Thread.sleep(8000);
//				wait.until(ExpectedConditions.presenceOfElementLocated(
//						By.xpath(("//*[contains(@id,'tAccountPopup::content')]//*[text()='o']"))));
//				WebElement waittext = driver
//						.findElement(By.xpath(("//*[contains(@id,'tAccountPopup::content')]//*[text()='o']")));
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Done clickButton" + scripNumber);
//				String xpath = "//*[contains(@id,'tAccountPopup::content')]//*[text()='o']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Done clickButton" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Apply")) {
//				Thread.sleep(8000);
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@value='" + param1 + "']")));
//				// wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()='l']"),
//				// "l"));
//				Thread.sleep(4000);
//				WebElement waittext = driver.findElement(By.xpath("//input[@value='" + param1 + "']"));
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				// clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				waittext.click();
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Apply clickButton" + scripNumber);
//				String xpath = "//input[@value='param1']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Apply clickButton" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Done")) {
//				Thread.sleep(3000);
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				Thread.sleep(5000);
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[text()='ne']"))));
//				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[text()='ne']"), "ne"));
//				WebElement waittext = driver.findElement(By.xpath(("//*[text()='ne']")));
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Done clickButton" + scripNumber);
//				log.info("Sucessfully Clicked Done clickButton" + scripNumber);
//				String xpath = "//*[text()='ne']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			} else if (param1.equalsIgnoreCase("Approval and Notification History")
//					&& param2.equalsIgnoreCase("Done")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				Thread.sleep(5000);
//				wait.until(ExpectedConditions.presenceOfElementLocated(
//						By.xpath(("//div[contains(text(),'" + param1 + "')]/following::span[text()='o']"))));
//				wait.until(ExpectedConditions.textToBePresentInElementLocated(
//						By.xpath("//div[contains(text(),'" + param1 + "')]/following::span[text()='o']"), "o"));
//				WebElement waittext = driver.findElement(
//						By.xpath(("//div[contains(text(),'" + param1 + "')]/following::span[text()='o']")));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Approval and Notification History or Done clickButton" + scripNumber);
//				String xpath = "//div[contains(text(),'param1')]/following::span[text()='o']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			} else if (param2.equalsIgnoreCase("Done")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				Thread.sleep(5000);
//				wait.until(ExpectedConditions.presenceOfElementLocated(
//						By.xpath(("//*[contains(text(),'" + param1 + "')]/following::span[text()='o']"))));
//				wait.until(ExpectedConditions.textToBePresentInElementLocated(
//						By.xpath("//*[contains(text(),'" + param1 + "')]/following::span[text()='o']"), "o"));
//				WebElement waittext = driver
//						.findElement(By.xpath(("//*[contains(text(),'" + param1 + "')]/following::span[text()='o']")));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Done clickButton" + scripNumber);
//				String xpath = "//*[contains(text(),'param1')]/following::span[text()='o']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			} else if (param1.equalsIgnoreCase("Submit")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[text()='m']"))));
//				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()='m']"), "m"));
//				Thread.sleep(20000);
//				WebElement waittext = driver.findElement(By.xpath(("//span[text()='m']")));
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(3000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Submit clickButton" + scripNumber);
//				String xpath = "//span[text()='m']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			} else if (param2.equalsIgnoreCase("Submit")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(
//						By.xpath(("//*[normalize-space(text())='" + param1 + "']/following::span[text()='m']"))));
//				wait.until(ExpectedConditions.textToBePresentInElementLocated(
//						By.xpath("//*[normalize-space(text())='" + param1 + "']/following::span[text()='m']"), "m"));
//				Thread.sleep(4000);
//				WebElement waittext = driver.findElement(
//						By.xpath(("//*[normalize-space(text())='" + param1 + "']/following::span[text()='m']")));
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(3000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Submit clickButton" + scripNumber);
//				String xpath = "//*[normalize-space(text())='param1']/following::span[text()='m']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			} else if (param1.equalsIgnoreCase("Distributions")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='istributions']")));
//				WebElement waittext = driver.findElement(By.xpath("//span[text()='istributions']"));
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(3000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Distributions clickButton" + scripNumber);
//				String xpath = "//span[text()='istributions']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			} else if ((param1.equalsIgnoreCase("Manage Distributions") || param1.equalsIgnoreCase("Manage Holds"))
//					&& param2.equalsIgnoreCase("Save and Close")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
//						("//*[normalize-space(text())='" + param1 + "']/following::button[text()='Save and Close']"))));
//				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(
//						"//*[normalize-space(text())='" + param1 + "']/following::button[text()='Save and Close']"),
//						"Save and Close"));
//				Thread.sleep(4000);
//				WebElement waittext = driver.findElement(By.xpath(
//						("//*[normalize-space(text())='" + param1 + "']/following::button[text()='Save and Close']")));
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(3000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Manage Holds or Save and Close clickButton" + scripNumber);
//				String xpath = "//*[normalize-space(text())='param1']/following::button[text()='Save and Close']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			} else if (param2.equalsIgnoreCase("Save and Close")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(
//						By.xpath(("//*[normalize-space(text())='" + param1 + "']/following::span[text()='S']"))));
//				wait.until(ExpectedConditions.textToBePresentInElementLocated(
//						By.xpath("//*[normalize-space(text())='" + param1 + "']/following::span[text()='S']"), "S"));
//				Thread.sleep(4000);
//				WebElement waittext = driver.findElement(
//						By.xpath(("//*[normalize-space(text())='" + param1 + "']/following::span[text()='S']")));
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(3000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
//				String xpath = "//*[normalize-space(text())='param1']/following::span[text()='S']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			} else if (param1.equalsIgnoreCase("Next")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[text()='x']"))));
//				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()='x']"), "x"));
//				Thread.sleep(4000);
//				WebElement waittext = driver.findElement(By.xpath(("//span[text()='x']")));
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(8000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Next clickButton" + scripNumber);
//				String xpath = "//span[text()='x']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			} else if (param2.equalsIgnoreCase("Next")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1
//						+ "']/following::*[normalize-space(text())='" + param2 + "']")));
//				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[normalize-space(text())='"
//						+ param1 + "']/following::*[normalize-space(text())='" + param2 + "']"), "x"));
//				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
//						+ "']/following::*[normalize-space(text())='" + param2 + "']"));
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(8000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Next clickButton" + scripNumber);
//				String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			} else if (param2.equalsIgnoreCase("Yes")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions
//						.presenceOfElementLocated(By.xpath(("//div[@class='AFDetectExpansion']/following::*[text()='"
//								+ param1 + "']/following::span[text()='Y']"))));
//				wait.until(ExpectedConditions.textToBePresentInElementLocated(
//						By.xpath("//div[@class='AFDetectExpansion']/following::*[text()='" + param1
//								+ "']/following::span[text()='Y']"),
//						"Y"));
//				Thread.sleep(6000);
//				WebElement waittext = driver
//						.findElement(By.xpath(("//div[@class='AFDetectExpansion']/following::*[text()='" + param1
//								+ "']/following::span[text()='Y']")));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(6000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Yes clickButton" + scripNumber);
//				String xpath = "//div[@class='AFDetectExpansion']/following::*[text()='param1']/following::span[text()='Y']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			} else if (param2.equalsIgnoreCase("OK")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions
//						.presenceOfElementLocated(By.xpath("//button[@_afrpdo='ok' and @accesskey='K']")));
//				wait.until(ExpectedConditions
//						.textToBePresentInElementLocated(By.xpath("//button[@_afrpdo='ok' and @accesskey='K']"), "K"));
//				Thread.sleep(4000);
//				WebElement waittext = driver.findElement(By.xpath("//button[@_afrpdo='ok' and @accesskey='K']"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(4000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked OK clickButton" + scripNumber);
//				String xpath = "//button[@_afrpdo='ok' and @accesskey='K']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			} // Here adding code for Advanced Button AP.452
//
//			else if (param1.equalsIgnoreCase("Advanced")) {
//
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//
//				wait.until(ExpectedConditions
//
//						.presenceOfElementLocated(By.xpath("//button[@accesskey='d']")));
//
//				Thread.sleep(4000);
//
//				WebElement waittext = driver.findElement(By.xpath("//button[@accesskey='d']"));
//
//				Actions actions = new Actions(driver);
//
//				actions.moveToElement(waittext).build().perform();
//
////				screenshot(driver, "", fetchMetadataVO, configVO); 
//
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//
//				Thread.sleep(4000);
//
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//
//				log.info("Sucessfully Clicked OK clickButton" + scripNumber);
//
//				String xpath = "//button[@accesskey='d']";
//
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//				return;
//			} else if (param1.equalsIgnoreCase("Save and Close")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[text()='S']"))));
//				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()='S']"), "S"));
//				Thread.sleep(4000);
//				WebElement waittext = driver.findElement(By.xpath(("//span[text()='S']")));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
//				String xpath = "//span[text()='S']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			} else if (param1.equalsIgnoreCase("Continue")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[text()='u']"))));
//				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()='u']"), "u"));
//				Thread.sleep(4000);
//				WebElement waittext = driver.findElement(By.xpath(("//span[text()='u']")));
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Continue clickButton" + scripNumber);
//				String xpath = "//span[text()='u']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			} else if (param2.equalsIgnoreCase("Continue")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//button[text()='Contin']"))));
//				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//button[text()='Contin']"),
//						"Contin"));
//				WebElement waittext = driver.findElement(By.xpath(("//button[text()='Contin']")));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Continue clickButton" + scripNumber);
//				String xpath = "//button[text()='Contin']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			} else if (param1.equalsIgnoreCase("Close")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//button[text()='Cl']"))));
//				Thread.sleep(5000);
//				WebElement waittext = driver.findElement(By.xpath(("//button[text()='Cl']")));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Close clickButton" + scripNumber);
//				String xpath = "//button[text()='Cl']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			} else if (param1.equalsIgnoreCase("Adjustment")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				Thread.sleep(3000);
//				wait.until(ExpectedConditions
//						.presenceOfElementLocated(By.xpath(("(//span[text()='" + param1 + "'])[1]"))));
//				WebElement waittext = driver.findElement(By.xpath(("(//span[text()='" + param1 + "'])[1]")));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(5000);
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Adjustment clickButton" + scripNumber);
//				String xpath = "(//span[text()='param1'])[1]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			} else if (param1.equalsIgnoreCase("Cancel")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='C']")));
//				Thread.sleep(5000);
//				WebElement waittext = driver.findElement(By.xpath("//span[text()='C']"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Cancel clickButton" + scripNumber);
//				String xpath = "//span[text()='C']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			} else if (param1.equalsIgnoreCase("Save")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[text()='ave']"))));
//				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()='ave']"), "ave"));
//				Thread.sleep(4000);
//				WebElement waittext = driver.findElement(By.xpath(("//span[text()='ave']")));
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Save clickButton" + scripNumber);
//				String xpath = "//span[text()='ave']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			} else if (param1.equalsIgnoreCase("Apply")) {
//				Thread.sleep(8000);
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[text()='l']"))));
//				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()='l']"), "l"));
//				Thread.sleep(4000);
//				WebElement waittext = driver.findElement(By.xpath(("//span[text()='l']")));
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Apply clickButton" + scripNumber);
//				String xpath = "//span[text()='l']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			} else if (param2.equalsIgnoreCase("Apply")) {
//				Thread.sleep(4000);
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(
//						By.xpath(("//*[normalize-space(text())='" + param1 + "']/following::span[text()='l']"))));
//				wait.until(ExpectedConditions.textToBePresentInElementLocated(
//						By.xpath("//*[normalize-space(text())='" + param1 + "']/following::span[text()='l']"), "l"));
//				Thread.sleep(4000);
//				WebElement waittext = driver.findElement(
//						By.xpath(("//*[normalize-space(text())='" + param1 + "']/following::span[text()='l']")));
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				Thread.sleep(2000);
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Apply clickButton" + scripNumber);
//				String xpath = "//*[normalize-space(text())='param1']/following::span[text()='l']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			} else if (param2.equalsIgnoreCase("Accept")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(
//						By.xpath(("//*[normalize-space(text())='" + param1 + "']/following::span[text()='p']"))));
//				wait.until(ExpectedConditions.textToBePresentInElementLocated(
//						By.xpath("//*[normalize-space(text())='" + param1 + "']/following::span[text()='p']"), "p"));
//				Thread.sleep(4000);
//				WebElement waittext = driver.findElement(
//						By.xpath(("//*[normalize-space(text())='" + param1 + "']/following::span[text()='p']")));
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Accept clickButton" + scripNumber);
//				String xpath = "//*[normalize-space(text())='param1']/following::span[text()='p']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Apply clickButton" + scripNumber);
//			System.out.println(e);
//
//		}
//		try {
//			if (param2.equalsIgnoreCase("OK")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(
//						By.xpath(("//*[normalize-space(text())=\"" + param1 + "\"]/following::span[text()='K']"))));
//				wait.until(ExpectedConditions.textToBePresentInElementLocated(
//						By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::span[text()='K']"), "K"));
//				WebElement waittext = driver.findElement(
//						By.xpath(("//*[normalize-space(text())=\"" + param1 + "\"]/following::span[text()='K']")));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(4000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked OK clickButton" + scripNumber);
//				String xpath = "//*[normalize-space(text())='param1']/following::span[text()='K']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			System.out.println(e);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during OK clickButton" + scripNumber);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Add Application")) {
//				try {
//					WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//					wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[text()='A']"))));
//					wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()='A']"), "A"));
//					Thread.sleep(4000);
//					WebElement waittext = driver.findElement(By.xpath(("//span[text()='A']")));
////					screenshot(driver, "", fetchMetadataVO, configVO);
//					Actions actions = new Actions(driver);
//					actions.moveToElement(waittext).build().perform();
//					clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//					String scripNumber = fetchMetadataVO.getScriptNumber();
//					log.info("Sucessfully Clicked add Application clickButton" + scripNumber);
//					String xpath = "//span[text()='A']";
//					testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				} catch (Exception e) {
//					WebElement expand = driver
//							.findElement(By.xpath(("//a[text()='Application']/following::div[@role='button'][2]")));
//					expand.click();
//					Thread.sleep(2000);
//					WebElement waittext = driver.findElement(By.xpath(("//span[text()='A']")));
////					screenshot(driver, "", fetchMetadataVO, configVO);
//					Actions actions = new Actions(driver);
//					actions.moveToElement(waittext).build().perform();
//					clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//					String scripNumber = fetchMetadataVO.getScriptNumber();
//					log.error("Failed during add Application clickButton" + scripNumber);
//					String xpath = "//a[text()='Application']/following::div[@role='button'][2]" + ">"
//							+ "//span[text()='A']";
//					testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				}
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during add Application clickButton" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Unapply Application")) {
//				try {
//					WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//					wait.until(ExpectedConditions
//							.presenceOfElementLocated(By.xpath(("//button[text()='" + param1 + "']"))));
//					wait.until(ExpectedConditions
//							.textToBePresentInElementLocated(By.xpath("//button[text()='" + param1 + "']"), "param1"));
//					Thread.sleep(4000);
//					WebElement waittext = driver.findElement(By.xpath(("//button[text()='" + param1 + "']")));
////					screenshot(driver, "", fetchMetadataVO, configVO);
//					Actions actions = new Actions(driver);
//					actions.moveToElement(waittext).build().perform();
//					clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//					Thread.sleep(4000);
//					String scripNumber = fetchMetadataVO.getScriptNumber();
//					log.info("Sucessfully Clicked Unapply Application clickButton" + scripNumber);
//					String xpath = "//button[text()='param1']";
//					testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				} catch (Exception e) {
//					WebElement expand = driver
//							.findElement(By.xpath(("//a[text()='Application']/following::div[@role='button'][2]")));
//					expand.click();
//					Thread.sleep(2000);
//					WebElement waittext = driver.findElement(By.xpath(("//button[text()='" + param1 + "']")));
////					screenshot(driver, "", fetchMetadataVO, configVO);
//					Actions actions = new Actions(driver);
//					actions.moveToElement(waittext).build().perform();
//					clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//					String scripNumber = fetchMetadataVO.getScriptNumber();
//					log.error("Failed during Unapply Application clickButton" + scripNumber);
//					String xpath = "//a[text()='Application']/following::div[@role='button'][2]";
//					testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				}
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Unapply Application clickButton" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param2.equalsIgnoreCase("Submit")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				Thread.sleep(3000);
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[normalize-space(text())='"
//						+ param1 + "']/following::span[normalize-space(text())='" + param2 + "']"))));
//				WebElement waittext = driver.findElement(By.xpath(("//*[normalize-space(text())='" + param1
//						+ "']/following::span[normalize-space(text())='" + param2 + "']")));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(3000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Submit clickButton" + scripNumber);
//				return;
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Submit clickButton" + scripNumber);
//		}
//		try {
//			// Changed == to equals method
//			if (!param2.equals("")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())='"
//						+ param1 + "']/following::*[normalize-space(text())='" + param2 + "'])[1]")));
//				WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())='" + param1
//						+ "']/following::*[normalize-space(text())='" + param2 + "'])[1]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				Thread.sleep(1000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked clickButton" + scripNumber);
//				String xpath = "(//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2'])[1]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickButton" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Columns") || param1.equalsIgnoreCase("Show All")) {
//				Thread.sleep(4000);
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions
//						.presenceOfElementLocated(By.xpath("(//td[normalize-space(text())='" + param1 + "'])[2]")));
//				// wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()='l']"),
//				// "l"));
//				Thread.sleep(4000);
//				WebElement waittext = driver
//						.findElement(By.xpath("(//td[normalize-space(text())='" + param1 + "'])[2]"));
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				// clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				waittext.click();
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked  Columns or Show All clickButton" + scripNumber);
//				String xpath = "(//td[normalize-space(text())='param1'])[2]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Columns or Show All clickButton" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Add to Document Builder")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				Thread.sleep(3000);
//				wait.until(
//						ExpectedConditions.presenceOfElementLocated(By.xpath(("//button[text()='" + param1 + "']"))));
//				WebElement waittext = driver.findElement(By.xpath(("//button[text()='" + param1 + "']")));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(3000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked  Add to Document Builder clickButton" + scripNumber);
//				String xpath = "//button[text()='param1']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Add to Document Builder clickButton" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Freeze")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				Thread.sleep(3000);
//				wait.until(ExpectedConditions.presenceOfElementLocated(
//						By.xpath(("//tr[contains(@id,'HEADER_FREEZE')]//td[text()='" + param1 + "']"))));
//				WebElement waittext = driver
//						.findElement(By.xpath(("//tr[contains(@id,'HEADER_FREEZE')]//td[text()='" + param1 + "']")));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(3000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked  Freeze clickButton" + scripNumber);
//				String xpath = "//tr[contains(@id,'HEADER_FREEZE')]//td[text()='param1']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			} else if (param1.equalsIgnoreCase("Unfreeze")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				Thread.sleep(3000);
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
//						("//tr[contains(@id,'HEADER_UNFREEZE')]//td[normalize-space(text())='" + param1 + "']"))));
//				WebElement waittext = driver.findElement(By.xpath(
//						("//tr[contains(@id,'HEADER_UNFREEZE')]//td[normalize-space(text())='" + param1 + "']")));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(3000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked  Unfreeze clickButton" + scripNumber);
//				String xpath = "//tr[contains(@id,'HEADER_UNFREEZE')]//td[normalize-space(text())='param1']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			} else if (param1.equalsIgnoreCase("Close")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				Thread.sleep(3000);
//				wait.until(ExpectedConditions.presenceOfElementLocated(By
//						.xpath(("//tr[contains(@id,'HEADER_CLOSE')]//td[normalize-space(text())='" + param1 + "']"))));
//				WebElement waittext = driver.findElement(
//						By.xpath(("//tr[contains(@id,'HEADER_CLOSE')]//td[normalize-space(text())='" + param1 + "']")));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(3000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Close clickButton" + scripNumber);
//				String xpath = "//tr[contains(@id,'HEADER_CLOSE')]//td[normalize-space(text())='param1']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			} else if (param1.equalsIgnoreCase("Reopen")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				Thread.sleep(3000);
//				wait.until(ExpectedConditions.presenceOfElementLocated(By
//						.xpath(("//tr[contains(@id,'HEADER_REOPEN')]//td[normalize-space(text())='" + param1 + "']"))));
//				WebElement waittext = driver.findElement(By
//						.xpath(("//tr[contains(@id,'HEADER_REOPEN')]//td[normalize-space(text())='" + param1 + "']")));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(3000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Reopen clickButton" + scripNumber);
//				String xpath = "//tr[contains(@id,'HEADER_REOPEN')]//td[normalize-space(text())='param1']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			} else if (param1.equalsIgnoreCase("Edit")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				Thread.sleep(3000);
//				wait.until(ExpectedConditions.presenceOfElementLocated(
//						By.xpath(("//tr[contains(@id,'HEADER_EDIT')]//td[normalize-space(text())='" + param1 + "']"))));
//				WebElement waittext = driver.findElement(
//						By.xpath(("//tr[contains(@id,'HEADER_EDIT')]//td[normalize-space(text())='" + param1 + "']")));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(3000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Edit clickButton" + scripNumber);
//				String xpath = "//tr[contains(@id,'HEADER_EDIT')]//td[normalize-space(text())='param1']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickButton" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Edit")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				Thread.sleep(3000);
//				wait.until(ExpectedConditions.presenceOfElementLocated(
//						By.xpath(("//tr[contains(@id,'commandMenuItem')]//td[text()='" + param1 + "']"))));
//				WebElement waittext = driver
//						.findElement(By.xpath(("//tr[contains(@id,'commandMenuItem')]//td[text()='" + param1 + "']")));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(3000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Edit clickButton" + scripNumber);
//				String xpath = "//tr[contains(@id,'commandMenuItem')]//td[text()='param1']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Edit clickButton" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Reverse")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By
//						.xpath(("//div[@class='AFPopupMenuPopup']//td[(normalize-space(text())='" + param1 + "')]"))));
//				WebElement waittext = driver.findElement(
//						By.xpath(("//div[@class='AFPopupMenuPopup']//td[(normalize-space(text())='" + param1 + "')]")));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(2000);
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Reverse clickButton" + scripNumber);
//				String xpath = "//div[@class='AFPopupMenuPopup']//td[(normalize-space(text())='param1')]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Reverse clickButton" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("PDF")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions
//						.presenceOfElementLocated(By.xpath("//td[normalize-space(text())='" + param1 + "']")));
//				WebElement waittext = driver.findElement(By.xpath("//td[normalize-space(text())='" + param1 + "']"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(60000);
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked  Columns or Show All clickButton" + scripNumber);
//				String xpath = "//td[normalize-space(text())='param1']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Apply clickButton" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Republish")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions
//						.presenceOfElementLocated(By.xpath("//button[normalize-space(text())='" + param1 + "']")));
//				WebElement waittext = driver
//						.findElement(By.xpath("//button[normalize-space(text())='" + param1 + "']"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(6000);
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Republish clickButton" + scripNumber);
//				String xpath = "//button[normalize-space(text())='param1']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Republish clickButton" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Match Invoice Lines") && param2.equalsIgnoreCase("OK")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(
//						By.xpath("//*[text()='" + param1 + "']/following::*[text()='K'][2]")));
//				WebElement waittext = driver
//						.findElement(By.xpath("//*[text()='" + param1 + "']/following::*[text()='K'][2]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(6000);
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Republish clickButton" + scripNumber);
//				String xpath = "//*[text()='" + param1 + "']/following::*[text()='K'][2]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Republish clickButton" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			// Changed == to equals method
//			if (param2.equals("")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				Thread.sleep(3000);
//				wait.until(ExpectedConditions
//						.presenceOfElementLocated(By.xpath(("//span[normalize-space(text())='" + param1 + "']"))));
//				WebElement waittext = driver
//						.findElement(By.xpath(("//span[normalize-space(text())='" + param1 + "']")));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(5000);
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked  clickButton" + scripNumber);
//				String xpath = "//span[normalize-space(text())='param1']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickButton" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			// Changed == to equals method
//			if (param2.equals("")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions
//						.presenceOfElementLocated(By.xpath(("//td[(normalize-space(text())='" + param1 + "')]"))));
//				WebElement waittext = driver
//						.findElement(By.xpath(("//td[(normalize-space(text())='" + param1 + "')]")));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(2000);
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked  clickButton" + scripNumber);
//				String xpath = "//td[(normalize-space(text())='param1')]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickButton" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			// Changed == to equals method
//			if (param2.equals("")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(
//						By.xpath(("//button[text()='" + param1 + "'and not(@style='display:none')]"))));
//				WebElement waittext = driver
//						.findElement(By.xpath(("//button[text()='" + param1 + "'and not(@style='display:none')]")));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(2000);
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				Thread.sleep(5000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked clickButton" + scripNumber);
//				String xpath = "//button[text()='param1'and not(@style='display:none')]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickButton" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			// Changed == to equals method
//			if (param2.equals("")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				Thread.sleep(3000);
//				wait.until(ExpectedConditions
//						.presenceOfElementLocated(By.xpath(("//div[normalize-space(text())='" + param1 + "']"))));
//				WebElement waittext = driver.findElement(By.xpath(("//div[normalize-space(text())='" + param1 + "']")));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(5000);
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked clickButton" + scripNumber);
//				String xpath = "//div[normalize-space(text())='param1']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickButton" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			// Changed == to equals method
//			if (param2.equals("")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(
//						By.xpath(("//div[contains(@class,'PopupMenu')]/following::*[text()='" + param1 + "']"))));
//				WebElement waittext = driver.findElement(
//						By.xpath(("//div[contains(@class,'PopupMenu')]/following::*[text()='" + param1 + "']")));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(2000);
////				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked  clickButton" + scripNumber);
//				String xpath = "//div[contains(@class,'PopupMenu')]/following::*[text()='" + param1 + "']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickButton" + scripNumber);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())='" + param1
//					+ "']/following::*[normalize-space(text())='" + param2 + "' and not(@_afrpdo)])[1]")));
//			WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())='" + param1
//					+ "']/following::*[normalize-space(text())='" + param2 + "' and not(@_afrpdo)])[1]"));
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			Thread.sleep(1000);
////			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked clickButton" + scripNumber);
//			String xpath = "(//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2' and not(@_afrpdo)])[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickButton" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())=\"" + param1
//					+ "\"]/following::*[normalize-space(text())='" + param2 + "'])[1]")));
//			WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())=\"" + param1
//					+ "\"]/following::*[normalize-space(text())='" + param2 + "'])[1]"));
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			Thread.sleep(3000);
////			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked clickButton" + scripNumber);
//			String xpath = "(//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2'])[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickButton" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
//					"(//*[normalize-space(text())=\"" + param1 + "\"]/following::*[@title='" + param2 + "'])[1]")));
//			WebElement waittext = driver.findElement(By.xpath(
//					"(//*[normalize-space(text())=\"" + param1 + "\"]/following::*[@title='" + param2 + "'])[1]"));
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			Thread.sleep(3000);
////			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked clickButton" + scripNumber);
//			String xpath = "(//*[normalize-space(text())='param1']/following::*[@title='param2'])[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickButton" + scripNumber);
//			System.out.println(e);
//		}
//
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			Thread.sleep(3000);
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[contains(text(),'" + param1
//					+ "')]/following::*[normalize-space(text())='" + param2 + "'][1]"))));
//			WebElement waittext = driver.findElement(By.xpath(("//*[contains(text(),'" + param1
//					+ "')]/following::*[normalize-space(text())='" + param2 + "'][1]")));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			Thread.sleep(5000);
////			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked clickButton" + scripNumber);
//			String xpath = "//*[contains(text(),'param1')]/following::*[normalize-space(text())='param2'][1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickButton" + scripNumber);
//		}
////		   try {
////	              String text = driver.findElement(By.xpath("//td[@class='AFNoteWindow']")).getText();
////	              configVO.setErrormessage(text);
////	  			return;
////	        } catch (Exception e) {
////	            System.out.println(e);
////	        }try {
////	              String text = driver.findElement(By.xpath("//div[contains(@class,'Error')]")).getText();
////	              configVO.setErrormessage(text);
////	  			return;
////	        } catch (Exception e) {
////	            System.out.println(e);
////	        }
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())='" + param1
//					+ "']/following::*[normalize-space(text())='" + param2 + "'])[1]")));
//			WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())='" + param1
//					+ "']/following::*[normalize-space(text())='" + param2 + "'])[1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
////			screenshot(driver, "", fetchMetadataVO, configVO);
//			Thread.sleep(1000);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked clickButton" + scripNumber);
//			String xpath = "(//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2'])[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickButton" + scripNumber);
//			System.out.println(e);
//			screenshotFail(driver, "Failed during clickLink Method", fetchMetadataVO, configVO);
//			throw e;
//		}
//
//	}
//
//	public void clickTableLink(WebDriver driver, String param1, String param2, TestRunExecutionVO fetchMetadataVO,
//			ConfigVO configVO) throws Exception {
//		try {
//			if ("Manage Receipts".equalsIgnoreCase(param1)) {
//				Thread.sleep(3000);
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[normalize-space(text())='"
//						+ param1 + "']/following::table[@summary='" + param2 + "']//a)[2]/parent::span")));
//				Thread.sleep(4000);
//				WebElement waittext = driver.findElement(By.xpath("(//h1[normalize-space(text())='" + param1
//						+ "']/following::table[@summary='" + param2 + "']//a)[2]/parent::span"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				actions.click(waittext).build().perform();
//				Thread.sleep(8000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked clickTableLink" + scripNumber);
//				String xpath = "(//h1[normalize-space(text())='param1']/following::table[@summary='param2']//a)[2]/parent::span";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//				return;
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		try {
//			if (param1.equalsIgnoreCase("Manage Journals") || param1.equalsIgnoreCase("Journal Lines")
//					|| param1.equalsIgnoreCase("Manage Transactions") || param1.equalsIgnoreCase("Prepare Source lines")
//					|| param1.equalsIgnoreCase("Contracts")) {
//				Thread.sleep(3000);
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[normalize-space(text())='"
//						+ param1 + "']/following::table[@summary='" + param2 + "']//a)[2]")));
//				Thread.sleep(4000);
//				WebElement waittext = driver.findElement(By.xpath("(//h1[normalize-space(text())='" + param1
//						+ "']/following::table[@summary='" + param2 + "']//a)[2]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				actions.click(waittext).build().perform();
//				Thread.sleep(8000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked clickTableLink" + scripNumber);
//				String xpath = "(//h1[normalize-space(text())='param1']/following::table[@summary='param2']//a)[2]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickTableLink" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Addresses")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions
//						.presenceOfElementLocated(By.xpath("(//table[@summary='" + param1 + "']//a)[2]")));
//				Thread.sleep(4000);
//				WebElement waittext = driver.findElement(By.xpath("(//table[@summary='" + param1 + "']//a)[2]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				actions.click(waittext).build().perform();
//				Thread.sleep(8000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Addresses clickTableLink" + scripNumber);
//				String xpath = "(//table[@summary='param1']//a)[2]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Addresses clickTableLink" + scripNumber);
//			System.out.println(e);
//		}
//
//		try {
//			if (param1.equalsIgnoreCase("Source Lines")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
//						"(//table[@summary='" + param1 + "']//a[not (contains(@title,'Required information'))])[1]")));
//				Thread.sleep(4000);
//				WebElement waittext = driver.findElement(By.xpath(
//						"(//table[@summary='" + param1 + "']//a[not (contains(@title,'Required information'))])[1]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				actions.click(waittext).build().perform();
//				Thread.sleep(8000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Addresses clickTableLink" + scripNumber);
//				String xpath = "(//table[@summary='\" +param1+ \"']//a[not (contains(@title,'Required information'))])[1]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Addresses clickTableLink" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param2.equalsIgnoreCase("Approved")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//table[@summary='" + param1
//						+ "']//*[normalize-space(text())='" + param2 + "']/following::a)[1]")));
//				Thread.sleep(4000);
//				WebElement waittext = driver.findElement(By.xpath("(//table[@summary='" + param1
//						+ "']//*[normalize-space(text())='" + param2 + "']/following::a)[1]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				actions.click(waittext).build().perform();
//				Thread.sleep(8000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Approved clickTableLink" + scripNumber);
//				String xpath = "(//table[@summary='param1']//*[normalize-space(text())='param2']/following::a)[1]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Approved clickTableLink" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Manage Orders")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(
//						By.xpath("//h1[normalize-space(text())='" + param1 + "']/following::table[@summary='" + param2
//								+ "']//a[contains(@title,'Purchase Order')]")));
//				Thread.sleep(4000);
//				WebElement waittext = driver.findElement(By.xpath("//h1[normalize-space(text())='" + param1
//						+ "']/following::table[@summary='" + param2 + "']//a[contains(@title,'Purchase Order')]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(2000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Manage Orders clickTableLink" + scripNumber);
//				String xpath = "//h1[normalize-space(text())='param1']/following::table[@summary='param2']//a[contains(@title,'Purchase Order')]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Manage Orders clickTableLink" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Manage Receipts")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions
//						.presenceOfElementLocated(By.xpath("(//table[@summary='" + param2 + "']//td)[1]")));
//				WebElement waittext = driver.findElement(By.xpath("(//table[@summary='" + param2 + "']//td)[1]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				//tab(driver, fetchMetadataVO, configVO);
//				//enter(driver, fetchMetadataVO, configVO);
//				Thread.sleep(2000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Manage Receipts clickTableLink" + scripNumber);
//				String xpath = "(//table[@summary='param2']//td)[1]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Manage Receipts clickTableLink" + scripNumber);
//			System.out.println(e);
//		}
//		// Adding Xpath for 'Checking the dashboard for unposted & journals in error for
//		// all journals(GL.125)’
//		try {
//
//			if (param1.equalsIgnoreCase("Journals") && param2.equalsIgnoreCase("Requiring Attention")) {
//
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//
//				wait.until(ExpectedConditions
//
//						.presenceOfElementLocated(By.xpath(
//								"//h2[text()='" + param1 + "']//following::table[@summary='" + param2 + "']//a[1]")));
//
//				WebElement waittext = driver.findElement(
//						By.xpath("//h2[text()='" + param1 + "']//following::table[@summary='" + param2 + "']//a[1]"));
//
//				Actions actions = new Actions(driver);
//
//				actions.moveToElement(waittext).build().perform();
//
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//
//				//tab(driver, fetchMetadataVO, configVO);
//
//				//enter(driver, fetchMetadataVO, configVO);
//
//				Thread.sleep(2000);
//
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//
//				log.info("Sucessfully Clicked Journals in Requiring Attention clickTableLink" + scripNumber);
//
//				String xpath = "//h2[text()='param1']//following::table[@summary='param2']//a[1]";
//
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//				return;
//
//			}
//
//		} catch (Exception e) {
//
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//
//			log.error("Failed during Journals in Requiring Attention clickTableLink" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("List of Processes Meeting Search Criteria")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions
//						.presenceOfElementLocated(By.xpath("(//table[@summary='" + param1 + "']//td[2]//span)[1]")));
//				Thread.sleep(4000);
//				WebElement waittext = driver
//						.findElement(By.xpath("(//table[@summary='" + param1 + "']//td[2]//span)[1]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(2000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked List of Processes Meeting Search Criteria clickTableLink" + scripNumber);
//				String xpath = "(//table[@summary='param1']//td[2]//span)[1]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during List of Processes Meeting Search Criteria clickTableLink" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			// Changed == to equals method
//			if (param2.equals("")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions
//						.presenceOfElementLocated(By.xpath("(//table[@summary='" + param1 + "']//a)[1]")));
//				Thread.sleep(4000);
//				WebElement waittext = driver.findElement(By.xpath("(//table[@summary='" + param1 + "']//a)[1]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(2000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked  clickTableLink" + scripNumber);
//				String params = param1;
//				String xpath = "(//table[@summary='param1']//a)[1]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickTableLink" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[normalize-space(text())='" + param1
//					+ "']/following::img[@title='" + param2 + "']/following-sibling::a[1]")));
//			Thread.sleep(4000);
//			WebElement waittext = driver.findElement(By.xpath("//h1[normalize-space(text())='" + param1
//					+ "']/following::img[@title='" + param2 + "']/following-sibling::a[1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			Thread.sleep(2000);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked clickTableLink" + scripNumber);
//			String xpath = "//h1[normalize-space(text())='param1']/following::img[@title='param2']/following-sibling::a[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickTableLink" + scripNumber);
//			screenshotFail(driver, "Failed during clickLink Method", fetchMetadataVO, configVO);
//			throw e;
//		}
//
//	}
//
//	public void tableRowSelect(WebDriver driver, String param1, String param2, TestRunExecutionVO fetchMetadataVO,
//			ConfigVO configVO) throws Exception {
//		try {
//
//			if (param1.equalsIgnoreCase("Value") || param1.equalsIgnoreCase("Transaction Number")
//					|| param1.equalsIgnoreCase("Name")) {
//				Thread.sleep(5000);
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(
//						By.xpath("(//div[@class='AFDetectExpansion']/following::span[normalize-space(text())='" + param1
//								+ "']/following::table//span[text()])[1]")));
//				WebElement waittext = driver.findElement(
//						By.xpath("(//div[@class='AFDetectExpansion']/following::span[normalize-space(text())='" + param1
//								+ "']/following::table//span[text()])[1]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(4000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked tableRowSelect" + scripNumber);
//				String xpath = "(//div[@class='AFDetectExpansion']/following::span[normalize-space(text())='param1']/following::table//span[text()])[1]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during tableRowSelect" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("SecondLine")) {
//				Thread.sleep(4000);
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions
//						.presenceOfElementLocated(By.xpath("(//table[@summary='" + param2 + "']//tr[2]//td)[1]")));
//				WebElement waittext = driver
//						.findElement(By.xpath("(//table[@summary='" + param2 + "']//tr[2]//td)[1]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				waittext.click();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(10000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked SecondLine tableRowSelect" + scripNumber);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during SecondLine tableRowSelect" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			Thread.sleep(6000);
//			System.out.println("Here1 came");
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions
//					.presenceOfElementLocated(By.xpath("(//table[@summary='" + param1 + "']//td)[1]")));
//			WebElement waittext = driver.findElement(By.xpath("(//table[@summary='" + param1 + "']//td)[1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			waittext.click();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			Thread.sleep(10000);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked Here1 came tableRowSelect" + scripNumber);
//			String xpath = "(//table[@summary='param1']//td)[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Here1 came tableRowSelect" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
//					"//h1[normalize-space(text())='" + param1 + "']/following::table[@summary='" + param1 + "'][1]")));
//			WebElement waittext = driver.findElement(By.xpath(
//					"//h1[normalize-space(text())='" + param1 + "']/following::table[@summary='" + param1 + "'][1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			Thread.sleep(4000);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked tableRowSelect" + scripNumber);
//			String xpath = "//h1[normalize-space(text())='param1']/following::table[@summary='param1'][1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during tableRowSelect" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1
//					+ "']/following::*[normalize-space(text())='" + param2 + "']/following::tr[1]/td[1]")));
//			WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
//					+ "']/following::*[normalize-space(text())='" + param2 + "']/following::tr[1]/td[1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			Thread.sleep(2000);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked tableRowSelect" + scripNumber);
//			String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']/following::tr[1]/td[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during tableRowSelect" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("")));
//			WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
//					+ "']/following::*[normalize-space(text())='" + param2 + "']/following::tr[1]/td[1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			Thread.sleep(2000);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked tableRowSelect" + scripNumber);
//			String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']/following::tr[1]/td[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//			return;
//		} catch (Exception e) {
//			System.out.println(e);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during tableRowSelect" + scripNumber);
//			screenshotFail(driver, "Failed during clickLink Method", fetchMetadataVO, configVO);
//			throw e;
//		}
//	}
//
//	public ArrayList<String> clickLink(WebDriver driver, String param1, String param2, TestRunExecutionVO fetchMetadataVO,
//			ConfigVO configVO,String actionName) throws Exception {
//		
//		SeleniumEbsMapping selEbsMapping=ebsMappingRepo.findBySeleniumActionName(actionName);
//		com.winfo.wats.model.Actions action=actionRepo.findByActionName(selEbsMapping.getEbsActionName());
//		
//		String codeLine="";
//		List<CodeLines>listOfCodeLines=codeLineRepo.findByActionIdOrderByCodeLineId(action);
//		HashMap<String, Object> result = null;
//		String dbValue="";
//		String key="" ;
//		String value;
//		ArrayList<String> listOfRobotCodeLines= new ArrayList<>();
//		//CodeLines robotCodeLine=listOfCodeLines.get(0);
//		for(CodeLines robotCodeLine:listOfCodeLines) {
//			codeLine=robotCodeLine.getRobot_line();
//		if(robotCodeLine.getParam_values()!=null)
//		{
//			
//			
//			String param_value=robotCodeLine.getParam_values();
//			try {
//				result = new ObjectMapper().readValue(param_value, HashMap.class);
//				for (Map.Entry<String,Object> entry : result.entrySet())
//				{
//		             key =entry.getKey();
//					 value=(String)entry.getValue();
//				
//				if(value.equalsIgnoreCase("<Pick from Config Table>"))
//				{
//					dbValue=	codeLineRepo.findByConfigurationId(configVO.getConfigurationId(),key);
//				}
//				if(value.equalsIgnoreCase("<Pick from Input Value>"))
//				{
//					dbValue=	codeLineRepo.findByTestRunScriptId(fetchMetadataVO.getTestRunScriptId(), key);
//				}
//				if(value.equalsIgnoreCase("<Pick from Input Parameter>"))
//				{
//					dbValue=	codeLineRepo.findByTestRunScriptIdInputParam(fetchMetadataVO.getTestRunScriptParamId(), key);
//				}
//				codeLine= codeLine.replace("${"+key+"}", dbValue);
//				
//				}
//				listOfRobotCodeLines.add(codeLine);
//
//			} catch (JsonMappingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (JsonProcessingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		
//			System.out.println(codeLine);
//		}
//		else
//		{
//			listOfRobotCodeLines.add(codeLine);	
//		}
//	}
//		return listOfRobotCodeLines;
//	}
//
//	public void clickRadiobutton(WebDriver driver, String param1, String param2, String keysToSend,
//			TestRunExecutionVO fetchMetadataVO, ConfigVO configVO) throws Exception {
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(
//					By.xpath(("(//*[normalize-space(text())='" + param1 + "']/following::label[text()='" + param2
//							+ "']/following::label[normalize-space(text())='" + keysToSend + "'])[1]"))));
//			wait.until(
//					ExpectedConditions
//							.textToBePresentInElementLocated(
//									By.xpath("//*[normalize-space(text())='" + param1
//											+ "']/following::label[normalize-space(text()))='" + param2 + "']"),
//									param2));
//			WebElement waittext = driver.findElement(
//					By.xpath("(//*[normalize-space(text())='" + param1 + "']/following::label[normalize-space(text())='"
//							+ param2 + "']/following::label[normalize-space(text())='" + keysToSend + "'])[1]"));
//			Thread.sleep(1000);
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			//tab(driver, fetchMetadataVO, configVO);
//			Thread.sleep(500);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked clickRadiobutton" + scripNumber);
//			String xpath = "(//*[normalize-space(text())='param1']/following::label[text()='param2']/following::label[normalize-space(text())='keysToSend'])[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickRadiobutton" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("(//*[normalize-space(text())='" + param1
//					+ "']/following::label[normalize-space(text())='" + keysToSend + "'])[1]"))));
//			WebElement waittext = driver.findElement(By.xpath(("(//*[normalize-space(text())='" + param1
//					+ "']/following::label[normalize-space(text())='" + keysToSend + "'])[1]")));
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			//tab(driver, fetchMetadataVO, configVO);
//			Thread.sleep(500);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked clickRadiobutton" + scripNumber);
//			String xpath = "(//*[normalize-space(text())='param1']/following::label[normalize-space(text())='keysToSend'])[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickRadiobutton" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			WebElement waittext = driver.findElement(By.xpath(("//*[contains(text(),'" + param1
//					+ "')]/following::*[normalize-space(text())='" + keysToSend + "']/preceding-sibling::input[1]")));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			Thread.sleep(500);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked clickRadiobutton" + scripNumber);
//			String xpath = "//*[contains(text(),'param1')]/following::*[normalize-space(text())='keysToSend']/preceding-sibling::input[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//			return;
//		} catch (Exception e) {
//			System.out.println(e);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickRadiobutton" + scripNumber);
//			screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, configVO);
//			throw e;
//		}
//	}
//
//	public void clickCheckbox(WebDriver driver, String param1, String keysToSend, TestRunExecutionVO fetchMetadataVO,
//			ConfigVO configVO) throws Exception {
//		try {
//			if (param1.equalsIgnoreCase("Item Description")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(
//						By.xpath("//*[normalize-space(text())='" + param1 + "']/following::*[normalize-space(text())='"
//								+ keysToSend + "']/preceding::label[contains(@id,'Label')][1]")));
//				wait.until(
//						ExpectedConditions.textToBePresentInElementLocated(
//								By.xpath("//*[normalize-space(text())='" + param1
//										+ "']/following::*[normalize-space(text())='" + keysToSend + "']"),
//								keysToSend));
//				WebElement waittext = driver.findElement(
//						By.xpath("//*[normalize-space(text())='" + param1 + "']/following::*[normalize-space(text())='"
//								+ keysToSend + "']/preceding::label[contains(@id,'Label')][1]"));
//				Thread.sleep(1000);
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				//tab(driver, fetchMetadataVO, configVO);
//				Thread.sleep(500);
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Item Description clickCheckbox" + scripNumber);
//				String params = param1;
//				String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='keysToSend']/preceding::label[contains(@id,'Label')][1]";
//				testRunScriptExecService.saveXpathParams(param1, "", scripNumber, xpath);
//
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Item Description clickCheckbox" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Address Purpose")) {
//				Thread.sleep(2000);
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1
//						+ "']/following::*[normalize-space(text())='" + keysToSend + "']")));
//				wait.until(
//						ExpectedConditions.textToBePresentInElementLocated(
//								By.xpath("//*[normalize-space(text())='" + param1
//										+ "']/following::*[normalize-space(text())='" + keysToSend + "']"),
//								keysToSend));
//				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
//						+ "']/following::*[normalize-space(text())='" + keysToSend + "']"));
//				Thread.sleep(1000);
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				////tab(driver, fetchMetadataVO, configVO);
//				Thread.sleep(500);
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Address Purpose clickCheckbox" + scripNumber);
//				String params = param1;
//				String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='keysToSend']";
//				testRunScriptExecService.saveXpathParams(param1, "", scripNumber, xpath);
//
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Address Purpose clickCheckbox" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Scenario")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1
//						+ "']/following::span[normalize-space(text())='" + keysToSend + "']/preceding::input[1]")));
//				wait.until(
//						ExpectedConditions.textToBePresentInElementLocated(
//								By.xpath("//*[normalize-space(text())='" + param1
//										+ "']/following::span[normalize-space(text())='" + keysToSend + "']"),
//								keysToSend));
//				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
//						+ "']/following::span[normalize-space(text())='" + keysToSend + "']/preceding::input[1]"));
//				Thread.sleep(1000);
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				// //tab(driver, fetchMetadataVO, configVO);
//				Thread.sleep(500);
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Scenario clickCheckbox" + scripNumber);
//				String params = param1;
//				String xpath = "//*[normalize-space(text())='param1']/following::span[normalize-space(text())='keysToSend']/preceding::input[1]";
//				testRunScriptExecService.saveXpathParams(param1, "", scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Scenario clickCheckbox" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Address Purpose")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1
//						+ "']/following::label[normalize-space(text())='" + keysToSend + "']/preceding::input[1]")));
//				wait.until(
//						ExpectedConditions.textToBePresentInElementLocated(
//								By.xpath("//*[normalize-space(text())='" + param1
//										+ "']/following::label[normalize-space(text())='" + keysToSend + "']"),
//								keysToSend));
//				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
//						+ "']/following::label[normalize-space(text())='" + keysToSend + "']/preceding::input[1]"));
//				Thread.sleep(1000);
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				// //tab(driver, fetchMetadataVO, configVO);
//				Thread.sleep(500);
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Address Purpose clickCheckbox" + scripNumber);
//				String params = param1;
//				String xpath = "//*[normalize-space(text())='param1']/following::label[normalize-space(text())='keysToSend']/preceding::input[1]";
//				testRunScriptExecService.saveXpathParams(param1, "", scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Address Purpose clickCheckbox" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Name")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(
//						By.xpath("//a[normalize-space(text())='" + param1 + "']/following::input[1]")));
//				WebElement waittext = driver
//						.findElement(By.xpath("//a[normalize-space(text())='" + param1 + "']/following::input[1]"));
//				Thread.sleep(1000);
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				// tab(driver, fetchMetadataVO, configVO);
//				Thread.sleep(500);
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Name clickCheckbox" + scripNumber);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Name clickCheckbox" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Match Invoice Lines")
//					|| param1.equalsIgnoreCase("Correct Unmatched Invoices")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(
//						By.xpath("//*[normalize-space(text())='" + param1 + "']/following::*[normalize-space(text())='"
//								+ keysToSend + "']/following::label[contains(@id,'Label')][1]")));
//				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[text()='" + param1 + "']"),
//						param1));
//				WebElement waittext = driver.findElement(By.xpath(
//						"//*[normalize-space(text())='Match Invoice Lines']/following::*[normalize-space(text())='Match']/following::label[contains(@id,'Label')][1]"));
//				Thread.sleep(1000);
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				//tab(driver, fetchMetadataVO, configVO);
//				Thread.sleep(500);
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Match Invoice Lines clickCheckbox" + scripNumber);
//				String params = param1;
//				String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='keysToSend']/following::label[contains(@id,'Label')][1]";
//				testRunScriptExecService.saveXpathParams(param1, "", scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Match Invoice Lines clickCheckbox" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//label[text()='" + param1
//					+ "']/following::span[text()='" + keysToSend + "']/preceding::label[1]"))));
//			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//label[text()='" + param1 + "']"),
//					param1));
//			WebElement waittext = driver.findElement(By.xpath("//label[normalize-space(text())='" + param1
//					+ "']/following::span[normalize-space(text())='" + keysToSend + "']/preceding::label[1]"));
//			Thread.sleep(1000);
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			//tab(driver, fetchMetadataVO, configVO);
//			Thread.sleep(500);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked clickCheckbox" + scripNumber);
//			String params = param1;
//			String xpath = "//label[text()='param1']/following::span[text()='keysToSend']/preceding::label[1]";
//			testRunScriptExecService.saveXpathParams(param1, "", scripNumber, xpath);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickCheckbox" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[normalize-space(text())='" + param1
//					+ "']/following::span[normalize-space(text())='" + keysToSend + "']/preceding::label[1]"))));
//			wait.until(
//					ExpectedConditions
//							.textToBePresentInElementLocated(
//									By.xpath("//*[normalize-space(text())='" + param1
//											+ "']/following::span[normalize-space(text())='" + keysToSend + "']"),
//									keysToSend));
//			WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
//					+ "']/following::span[normalize-space(text())='" + keysToSend + "']/preceding::label[1]"));
//			Thread.sleep(1000);
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			//tab(driver, fetchMetadataVO, configVO);
//			Thread.sleep(500);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked clickCheckbox" + scripNumber);
//			String params = param1;
//			String xpath = "//*[normalize-space(text())='param1']/following::span[normalize-space(text())='keysToSend']/preceding::label[1]";
//			testRunScriptExecService.saveXpathParams(param1, "", scripNumber, xpath);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickCheckbox" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//label[normalize-space(text())='"
//					+ param1 + "']/following::label[normalize-space(text())='" + keysToSend + "']"))));
//			wait.until(
//					ExpectedConditions.textToBePresentInElementLocated(
//							By.xpath("//label[normalize-space(text())='" + param1
//									+ "']/following::label[normalize-space(text())='" + keysToSend + "']"),
//							keysToSend));
//			WebElement waittext = driver.findElement(By.xpath("//label[normalize-space(text())='" + param1
//					+ "']/following::label[normalize-space(text())='" + keysToSend + "']"));
//			Thread.sleep(1000);
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			//tab(driver, fetchMetadataVO, configVO);
//			Thread.sleep(500);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked clickCheckbox" + scripNumber);
//			String params = param1;
//			String xpath = "//label[normalize-space(text())='param1']/following::label[normalize-space(text())='keysToSend']";
//			testRunScriptExecService.saveXpathParams(param1, "", scripNumber, xpath);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickCheckbox" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[normalize-space(text())='" + param1
//					+ "']/following::*[normalize-space(text())='" + keysToSend + "']"))));
//			wait.until(
//					ExpectedConditions
//							.textToBePresentInElementLocated(
//									By.xpath("//*[normalize-space(text())='" + param1
//											+ "']/following::*[normalize-space(text())='" + keysToSend + "']"),
//									keysToSend));
//			WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
//					+ "']/following::*[normalize-space(text())='" + keysToSend + "']"));
//			Thread.sleep(1000);
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			Thread.sleep(3000);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked clickCheckbox" + scripNumber);
//			String params = param1;
//			String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='keysToSend']";
//			testRunScriptExecService.saveXpathParams(param1, "", scripNumber, xpath);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickCheckbox" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//
//			wait.until(ExpectedConditions
//					.presenceOfElementLocated(By.xpath(("//label[normalize-space(text())='" + keysToSend + "']"))));
//			wait.until(ExpectedConditions.textToBePresentInElementLocated(
//					By.xpath("//label[normalize-space(text())='" + keysToSend + "']"), keysToSend));
//			WebElement waittext = driver.findElement(By.xpath("//label[normalize-space(text())='" + keysToSend + "']"));
//			Thread.sleep(1000);
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			//tab(driver, fetchMetadataVO, configVO);
//			Thread.sleep(500);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked clickCheckbox" + scripNumber);
//			String params = param1;
//			String xpath = "//label[normalize-space(text())='keysToSend']";
//			testRunScriptExecService.saveXpathParams(param1, "", scripNumber, xpath);
//
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickCheckbox" + scripNumber);
//			System.out.println(e);
//			screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, configVO);
//			throw e;
//		}
//	}
//
//	public void clickLinkAction(WebDriver driver, String param1, String param2, String keysToSend,
//			TestRunExecutionVO fetchMetadataVO, ConfigVO configVO) throws Exception {
//
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(
//					By.xpath(("//*[normalize-space(text())='" + param1 + "']/following::a[normalize-space(text())='"
//							+ keysToSend + "']/following::img[contains(@title,'" + param2 + "')][1]"))));
//			Thread.sleep(2000);
//			wait.until(
//					ExpectedConditions
//							.textToBePresentInElementLocated(
//									By.xpath("//*[normalize-space(text())='" + param1
//											+ "']/following::a[normalize-space(text())='" + keysToSend + "']"),
//									keysToSend));
//			WebElement waittext = driver.findElement(
//					By.xpath("//*[normalize-space(text())='" + param1 + "']/following::a[normalize-space(text())='"
//							+ keysToSend + "']/following::img[contains(@title,'" + param2 + "')][1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			Thread.sleep(2000);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			String xpath = "//*[normalize-space(text())='param1']/following::a[normalize-space(text())='keysToSend']/following::img[contains(@title,'param2')][1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			log.info("Sucessfully Clicked clickLinkAction" + scripNumber);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickLinkAction" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			// Changed == to equals method
//			if (param2.equals("")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1
//						+ "']/following::a[normalize-space(text())='" + keysToSend + "']")));
//				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
//						+ "']/following::a[normalize-space(text())='" + keysToSend + "']"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				String xpath = "//*[normalize-space(text())='param1']/following::a[normalize-space(text())='keysToSend']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				log.info("Sucessfully Clicked clickLinkAction" + scripNumber);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickLinkAction" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			// Changed == to equals method
//			if (param2.equals("")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
//						"(//*[normalize-space(text())='" + keysToSend + "']/following::td[normalize-space(text())='"
//								+ param1 + "']/following::table[1]//div)[1]")));
//				WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())='" + keysToSend
//						+ "']/following::td[normalize-space(text())='" + param1 + "']/following::table[1]//div)[1]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(1000);
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				String xpath = "(//*[normalize-space(text())='keysToSend']/following::td[normalize-space(text())='param1']/following::table[1]//div)[1]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				log.info("Sucessfully Clicked clickLinkAction" + scripNumber);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickLinkAction" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(
//					By.xpath(("(//*[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
//							+ keysToSend + "\"]/following::img[contains(@title,\"" + param2 + "\")])[1]"))));
//			WebElement waittext = driver.findElement(
//					By.xpath(("(//*[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
//							+ keysToSend + "\"]/following::img[contains(@title,\"" + param2 + "\")])[1]")));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			Thread.sleep(2000);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			String xpath = "(//*[normalize-space(text())='param1']/following::*[normalize-space(text())='keysToSend']/following::img[contains(@title,'param2')])[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			log.info("Sucessfully Clicked clickLinkAction" + scripNumber);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickLinkAction" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(
//					By.xpath(("(//*[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
//							+ keysToSend + "\"]/following::img[contains(@title,\"" + param2 + "\")])[1]"))));
//			WebElement waittext = driver.findElement(
//					By.xpath("(//*[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
//							+ keysToSend + "\"]/following::img[contains(@title,\"" + param2 + "\")])[1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			Thread.sleep(2000);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			String xpath = "(//*[normalize-space(text())='param1']/following::*[normalize-space(text())='keysToSend']/following::img[contains(@title,'param2')])[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			log.info("Sucessfully Clicked clickLinkAction" + scripNumber);
//			return;
//		} catch (Exception e) {
//			System.out.println(e);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during clickLinkAction" + scripNumber);
//			screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, configVO);
//			throw e;
//		}
//	}
//
//	public String textarea(WebDriver driver, String param1, String param2, String keysToSend,
//			TestRunExecutionVO fetchMetadataVO, ConfigVO configVO) throws Exception {
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())='" + param1
//					+ "']/following::label[normalize-space(text())='" + param2 + "']/following::textarea)[1]")));
//			Thread.sleep(1000);
//			wait.until(
//					ExpectedConditions
//							.textToBePresentInElementLocated(
//									By.xpath("//*[normalize-space(text())='" + param1
//											+ "']/following::label[normalize-space(text())='" + param2 + "']"),
//									param2));
//			WebElement waittill = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
//					+ "']/following::label[normalize-space(text())='" + param2 + "']/following::textarea[1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittill).build().perform();
//			typeIntoValidxpath(driver, keysToSend, waittill, configVO, fetchMetadataVO);
//			Thread.sleep(500);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked textarea" + scripNumber);
//			String xpath = "//*[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/following::textarea[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			return keysToSend;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during textarea" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[contains(text(),'" + param1
//					+ "')]/following::label[normalize-space(text())='" + param2 + "']/following::textarea)[1]")));
//			Thread.sleep(1000);
//			wait.until(
//					ExpectedConditions
//							.textToBePresentInElementLocated(
//									By.xpath("//*[contains(text(),'" + param1
//											+ "')]/following::label[normalize-space(text())='" + param2 + "']"),
//									param2));
//			WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),'" + param1
//					+ "')]/following::label[normalize-space(text())='" + param2 + "']/following::textarea)[1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittill).build().perform();
//			typeIntoValidxpath(driver, keysToSend, waittill, configVO, fetchMetadataVO);
//			Thread.sleep(500);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked textarea" + scripNumber);
//			String xpath = "(//*[contains(text(),'param1')]/following::label[normalize-space(text())='param2']/following::textarea)[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			return keysToSend;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during textarea" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//body[@dir='ltr']")));
//			Thread.sleep(1000);
//			WebElement waittill = driver.findElement(By.xpath("//body[@dir='ltr']"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittill).build().perform();
//			typeIntoValidxpath(driver, keysToSend, waittill, configVO, fetchMetadataVO);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			Thread.sleep(500);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked textarea" + scripNumber);
//			String xpath = "//body[@dir='ltr']";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			return keysToSend;
//		} catch (Exception e) {
//			System.out.println(e);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during textarea" + scripNumber);
//			screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, configVO);
//			throw e;
//		}
//	}
//
//	public ArrayList<String> sendValue(WebDriver driver, String param1, String param2, String keysToSend,
//			TestRunExecutionVO fetchMetadataVO, ConfigVO configVO,String actionName) throws Exception {
//		
//		SeleniumEbsMapping selEbsMapping=ebsMappingRepo.findBySeleniumActionName(actionName);
//		com.winfo.wats.model.Actions action=actionRepo.findByActionName(selEbsMapping.getEbsActionName());
//		
//			String codeLine="";
//			List<CodeLines>listOfCodeLines=codeLineRepo.findByActionIdOrderByCodeLineId(action);
//			HashMap<String, Object> result = null;
//			String dbValue="";
//			String key="" ;
//			String value;
//			ArrayList<String> listOfRobotCodeLines= new ArrayList<>();
//			//CodeLines robotCodeLine=listOfCodeLines.get(0);
//			for(CodeLines robotCodeLine:listOfCodeLines) {
//				codeLine=robotCodeLine.getRobot_line();
//			if(robotCodeLine.getParam_values()!=null)
//			{
//				
//				
//				String param_value=robotCodeLine.getParam_values();
//				try {
//					result = new ObjectMapper().readValue(param_value, HashMap.class);
//					for (Map.Entry<String,Object> entry : result.entrySet())
//					{
//			             key =entry.getKey();
//						 value=(String)entry.getValue();
//					
//					if(value.equalsIgnoreCase("<Pick from Config Table>"))
//					{
//						dbValue=	codeLineRepo.findByConfigurationId(configVO.getConfigurationId(),key);
//					}
//					if(value.equalsIgnoreCase("<Pick from Input Value>"))
//					{
//						dbValue=	codeLineRepo.findByTestRunScriptId(fetchMetadataVO.getTestRunScriptParamId(), key);
//					}
//					if(value.equalsIgnoreCase("<Pick from Input Parameter>"))
//					{
//						dbValue=	codeLineRepo.findByTestRunScriptIdInputParam(fetchMetadataVO.getTestRunScriptParamId(), key);
//					}
//					codeLine= codeLine.replace("${"+key+"}", dbValue);
//					
//					}
//					listOfRobotCodeLines.add(codeLine);
//
//				} catch (JsonMappingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (JsonProcessingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//			
//				System.out.println(codeLine);
//			}
//			else
//			{
//				listOfRobotCodeLines.add(codeLine);	
//			}
//		}
//			return listOfRobotCodeLines;
//				
//	}
//
//	public void tableSendKeys(WebDriver driver, String param1, String param2, String param3, String keysToSend,
//			TestRunExecutionVO fetchMetadataVO, ConfigVO configVO) throws Exception {
//		try {
//			if (param1.equalsIgnoreCase("Assigned Sets")) {
//				WebElement waittill = driver
//						.findElement(By.xpath("//span[text()='" + param2 + "']/following::input[1]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittill).build().perform();
////        waittill.sendKeys(keysToSend);
//				Thread.sleep(1000);
//				typeIntoValidxpath(driver, keysToSend, waittill, configVO, fetchMetadataVO);
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked tableSendKeys" + scripNumber);
//				String xpath = "//span[text()='" + param2 + "']/following::input[1]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  tableSendKeys" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Add Project Customer")) {
//				WebElement waittill = driver
//						.findElement(By.xpath("//*[text()='" + param1 + "']/following::label[text()='" + param2
//								+ "']/preceding-sibling::input[not(@type='hidden')]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittill).build().perform();
////        waittill.sendKeys(keysToSend);
//				typeIntoValidxpath(driver, keysToSend, waittill, configVO, fetchMetadataVO);
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked tableSendKeys" + scripNumber);
//				String xpath = "//*[text()='" + param1 + "']/following::label[text()='" + param2
//						+ "']/preceding-sibling::input[not(@type='hidden')]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  tableSendKeys" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//
//			if ((param1.equalsIgnoreCase("Associated Projects") && param2.equalsIgnoreCase("Funded Amount"))
//					|| param2.equalsIgnoreCase("Amount")) {
//				WebElement waittill = driver.findElement(By.xpath("//*[text()=\"" + param1
//
//						+ "\"]/following::label[text()='" + param2 + "']/preceding-sibling::input[1]"));
//
//				Actions actions = new Actions(driver);
//
//				actions.moveToElement(waittill).build().perform();
//
//				typeIntoValidxpath(driver, keysToSend, waittill, configVO, fetchMetadataVO);
//
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Associated Projects or Funded Amount tableSendKeys" + scripNumber);
//				String xpath = "//*[text()='param1']/following::label[text()='param2']/preceding-sibling::input[1]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//				return;
//			}
//
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  Associated Projects or Funded Amount tableSendKeys" + scripNumber);
//			System.out.println(e);
//
//		}
//
//		try {
//
//			if (param1.equalsIgnoreCase("Associated Projects") && param2.equalsIgnoreCase("Project Number")) {
//
//				WebElement waittill = driver.findElement(By.xpath("//*[text()=\"" + param1
//
//						+ "\"]/following::label[text()='" + param2 + "']/preceding::span[1]/input"));
//
//				Actions actions = new Actions(driver);
//
//				actions.moveToElement(waittill).build().perform();
//
//				typeIntoValidxpath(driver, keysToSend, waittill, configVO, fetchMetadataVO);
//
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Associated Projects or FProject Number tableSendKeys" + scripNumber);
//				String xpath = "//*[text()='param1']/following::label[text()='param2']/preceding::span[1]/input";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//				return;
//			}
//
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Associated Projects or FProject Number tableSendKeys" + scripNumber);
//			System.out.println(e);
//
//		}
//
//		try {
//
//			if (param1.equalsIgnoreCase("Associated Projects") && param2.equalsIgnoreCase("Task Number")) {
//
//				WebElement waittill = driver.findElement(By.xpath("(//*[text()=\"" + param1
//
//						+ "\"]/following::div[text()='Autocompletes on TAB']/preceding::input[1])[4]"));
//
//				Actions actions = new Actions(driver);
//
//				actions.moveToElement(waittill).build().perform();
//
//				typeIntoValidxpath(driver, keysToSend, waittill, configVO, fetchMetadataVO);
//
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Associated Projects or Task Number tableSendKeys" + scripNumber);
//				String xpath = "(//*[text()='param1']/following::div[text()='Autocompletes on TAB']/preceding::input[1])[4]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//				return;
//			}
//
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Associated Projects or Task Number tableSendKeys" + scripNumber);
//			System.out.println(e);
//
//		}
//		try {
//
//			if (param1.equalsIgnoreCase("Time Entry") || param2.equalsIgnoreCase("Unit Price")) {
//
//				WebElement waittill = driver.findElement(By.xpath("(//*[text()=\"" + param1
//
//						+ "\"]/following::label[text()='" + param2 + "']/preceding-sibling::input)[1]"));
//
//				Actions actions = new Actions(driver);
//
//				actions.moveToElement(waittill).build().perform();
//
//				typeIntoValidxpath(driver, keysToSend, waittill, configVO, fetchMetadataVO);
//
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Time Entry tableSendKeys" + scripNumber);
//				String xpath = "(//*[text()='param1']/following::label[text()='param2']/preceding-sibling::input)[1]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//				return;
//			}
//
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Time Entry tableSendKeys" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Mon")) {
//
//				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1
//
//						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[1]"));
//
//				Actions actions = new Actions(driver);
//
//				actions.moveToElement(waittill).build().perform();
//
//				typeIntoValidxpath(driver, keysToSend, waittill, configVO, fetchMetadataVO);
//
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Mon tableSendKeys" + scripNumber);
//				String xpath = "(//*[contains(text(),'param1')]/following::label[text()='param2']/preceding-sibling::input[1])[2]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//				return;
//			}
//			if (param1.equalsIgnoreCase("Tue")) {
//
//				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1
//
//						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[2]"));
//
//				Actions actions = new Actions(driver);
//
//				actions.moveToElement(waittill).build().perform();
//
//				typeIntoValidxpath(driver, keysToSend, waittill, configVO, fetchMetadataVO);
//
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Tue tableSendKeys" + scripNumber);
//				String xpath = "(//*[contains(text(),'param1')]/following::label[text()='param2']/preceding-sibling::input[1])[2]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//				return;
//			}
//			if (param1.equalsIgnoreCase("Wed")) {
//				Thread.sleep(2000);
//
//				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1
//
//						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[3]"));
//
//				Actions actions = new Actions(driver);
//
//				actions.moveToElement(waittill).build().perform();
//
//				typeIntoValidxpath(driver, keysToSend, waittill, configVO, fetchMetadataVO);
//
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Wed tableSendKeys" + scripNumber);
//				String xpath = "(//*[contains(text(),'param1')]/following::label[text()='param2']/preceding-sibling::input[1])[3]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//				return;
//			}
//			if (param1.equalsIgnoreCase("Thu")) {
//				Thread.sleep(2000);
//
//				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1
//
//						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[4]"));
//
//				Actions actions = new Actions(driver);
//
//				actions.moveToElement(waittill).build().perform();
//
//				typeIntoValidxpath(driver, keysToSend, waittill, configVO, fetchMetadataVO);
//
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Thu tableSendKeys" + scripNumber);
//				String xpath = "(//*[contains(text(),'param1')]/following::label[text()='param2']/preceding-sibling::input[1])[4]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//				return;
//			}
//			if (param1.equalsIgnoreCase("Fri")) {
//				Thread.sleep(2000);
//
//				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1
//
//						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[5]"));
//
//				Actions actions = new Actions(driver);
//
//				actions.moveToElement(waittill).build().perform();
//
//				typeIntoValidxpath(driver, keysToSend, waittill, configVO, fetchMetadataVO);
//
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Fri tableSendKeys" + scripNumber);
//				String xpath = "(//*[contains(text(),'param1')]/following::label[text()='param2']/preceding-sibling::input[1])[5]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//				return;
//			}
//			if (param1.equalsIgnoreCase("Sat")) {
//
//				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1
//
//						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[6]"));
//
//				Actions actions = new Actions(driver);
//
//				actions.moveToElement(waittill).build().perform();
//
//				typeIntoValidxpath(driver, keysToSend, waittill, configVO, fetchMetadataVO);
//
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Sat tableSendKeys" + scripNumber);
//				String xpath = "(//*[contains(text(),'param1')]/following::label[text()='param2']/preceding-sibling::input[1])[6]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//				return;
//			}
//			if (param1.equalsIgnoreCase("Sunday")) {
//
//				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1
//
//						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[7]"));
//
//				Actions actions = new Actions(driver);
//
//				actions.moveToElement(waittill).build().perform();
//
//				typeIntoValidxpath(driver, keysToSend, waittill, configVO, fetchMetadataVO);
//
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Sunday tableSendKeys" + scripNumber);
//				String xpath = "(//*[contains(text(),'param1')]/following::label[text()='param2']/preceding-sibling::input[1])[7]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  tableSendKeys" + scripNumber);
//			System.out.println(e);
//
//		}
//
//		try {
//			if (param1.equalsIgnoreCase("Quantity")) {
//				Thread.sleep(5000);
//				try {
//					WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//					WebElement waittill = driver.findElement(
//							By.xpath("(//text()='" + param1 + "']/preceding-sibling::input[ not (@value)])[1]"));
//					Actions actions = new Actions(driver);
//					actions.moveToElement(waittill).build().perform();
//					typeIntoValidxpath(driver, keysToSend, waittill, configVO, fetchMetadataVO);
//					screenshot(driver, "", fetchMetadataVO, configVO);
//					String scripNumber = fetchMetadataVO.getScriptNumber();
//					log.info("Sucessfully Clicked Quantity tableSendKeys" + scripNumber);
//					String xpath = "(//text()='param1']/preceding-sibling::input[ not (@value)])[1]";
//					testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//				} catch (Exception e) {
//					WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//					WebElement waittill = driver
//							.findElement(By.xpath("//label[text()='" + param1 + "']/preceding-sibling::input[1]"));
//					Actions actions = new Actions(driver);
//					actions.moveToElement(waittill).build().perform();
//					typeIntoValidxpath(driver, keysToSend, waittill, configVO, fetchMetadataVO);
//					screenshot(driver, "", fetchMetadataVO, configVO);
//					String scripNumber = fetchMetadataVO.getScriptNumber();
//					log.error("Failed during Quantity tableSendKeys" + scripNumber);
//
//				}
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  tableSendKeys" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Lines") && param2.equalsIgnoreCase("Price")) {
//				Thread.sleep(10000);
//				WebElement waittill = driver
//						.findElement(By.xpath("//*[text()=\"" + param1 + "\"]/following::label[text()='" + param2
//								+ "']/preceding-sibling::input[contains(@name,'AmountAsPrice')]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittill).build().perform();
//				typeIntoValidxpath(driver, keysToSend, waittill, configVO, fetchMetadataVO);
//				Thread.sleep(4000);
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Lines or Price tableSendKeys" + scripNumber);
//				String xpath = "//*[text()='param1']/following::label[text()='param2']/preceding-sibling::input[contains(@name,'AmountAsPrice')]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Clicked Lines or Price  tableSendKeys" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Lines") && param2.equalsIgnoreCase("Expenditure Item Date")) {
//				Thread.sleep(10000);
//				WebElement waittill = driver.findElement(By.xpath("(//*[text()=\"" + param1
//						+ "\"]/following::label[text()='" + param2 + "']/preceding-sibling::input)[1]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittill).build().perform();
//				typeIntoValidxpath(driver, keysToSend, waittill, configVO, fetchMetadataVO);
//				Thread.sleep(4000);
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Lines or Expenditure Item Date tableSendKeys" + scripNumber);
//				String xpath = "(//*[text()='param1']/following::label[text()='param2']/preceding-sibling::input)[1]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Lines or Expenditure Item Date tableSendKeys" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Lines") || param2.equalsIgnoreCase("Item")) {
//				Thread.sleep(10000);
//				WebElement waittill = driver.findElement(By.xpath("//*[text()=\"" + param1
//						+ "\"]/following::label[text()='" + param2 + "']/preceding::input[1]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittill).build().perform();
//				typeIntoValidxpath(driver, keysToSend, waittill, configVO, fetchMetadataVO);
//				Thread.sleep(4000);
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Lines or Item tableSendKeys" + scripNumber);
//				String xpath = "//*[text()='param1']/following::label[text()='param2']/preceding::input[1]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Clicked Lines or Item  tableSendKeys" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param2.equalsIgnoreCase("Application Reference")) {
//				Thread.sleep(4000);
//				WebElement waittill = driver.findElement(By.xpath(
//						"(//h1[text()=\"" + param1 + "\"]/following::*[text()='" + param2 + "']/following::input)[1]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittill).build().perform();
//				typeIntoValidxpath(driver, keysToSend, waittill, configVO, fetchMetadataVO);
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Application Reference tableSendKeys" + scripNumber);
//				String xpath = "(//h1[text()='param1']/following::*[text()='param2']/following::input)[1]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Application Reference tableSendKeys" + scripNumber);
//			System.out.println(e);
//		}
//		// Add Xpath for "Register a mass promise to pay(AR.249)
//
//		try {
//
//			if (param1.equalsIgnoreCase("Transaction")) {
//
//				WebElement waittill = driver.findElement(By.xpath(
//
//						"(//span[text()='" + param1 + "']/preceding::input[@type='text'])[2]"));
//
//				Actions actions = new Actions(driver);
//
//				actions.moveToElement(waittill).build().perform();
//
//				typeIntoValidxpath(driver, keysToSend, waittill, configVO, fetchMetadataVO);
//
//				screenshot(driver, "", fetchMetadataVO, configVO);
//
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//
//				log.info("Sucessfully Clicked tableSendKeys" + scripNumber);
//
//				String xpath = "(//span[text()='param1']/preceding::input[@type='text'])[2]";
//
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//				return;
//
//			}
//
//		} catch (Exception e) {
//
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//
//			log.error("Failed during tableSendKeys" + scripNumber);
//
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Provider") || param1.equalsIgnoreCase("Receiver")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				WebElement waittill = driver.findElement(By.xpath("(//*[text()='" + param1 + "']/following::*[text()='"
//						+ param2 + "']/preceding-sibling::input)[2]"));
//				Thread.sleep(1000);
//				// wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h1[normalize-space(text())='"+param1+"']/following::label[text()='"+param2+"']"),
//				// param2));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittill).build().perform();
//				typeIntoValidxpath(driver, keysToSend, waittill, configVO, fetchMetadataVO);
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked Provider or Receiver tableSendKeys" + scripNumber);
//				String xpath = "(//*[text()='param1']/following::*[text()='param2']/preceding-sibling::input)[2]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  tableSendKeys" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			Thread.sleep(6000);
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			WebElement waittill = driver.findElement(By.xpath("(//h1[text()='" + param1 + "']/following::label[text()='"
//					+ param2 + "']/preceding-sibling::input[not(@type='hidden')])[1]"));
//			Thread.sleep(1000);
//			// wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h1[normalize-space(text())='"+param1+"']/following::label[text()='"+param2+"']"),
//			// param2));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittill).build().perform();
//			Thread.sleep(6000);
//			typeIntoValidxpath(driver, keysToSend, waittill, configVO, fetchMetadataVO);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked tableSendKeys" + scripNumber);
//			String xpath = "(//h1[text()='param1']/following::label[text()='param2']/preceding-sibling::input[not(@type='hidden')])[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  tableSendKeys" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebElement waittill = driver.findElement(By.xpath(
//					"//h1[text()='" + param1 + "']/following::*[text()='" + param2 + "']/preceding-sibling::input[1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittill).build().perform();
//			typeIntoValidxpath(driver, keysToSend, waittill, configVO, fetchMetadataVO);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked tableSendKeys" + scripNumber);
//			String xpath = "//h1[text()='param1']/following::*[text()='param2']/preceding-sibling::input[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  tableSendKeys" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebElement waittill = driver.findElement(By.xpath("//h1[text()='" + param1 + "']/following::span[text()='"
//					+ param2 + "']/preceding::input[contains(@id,'descColumn::content')]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittill).build().perform();
//			typeIntoValidxpath(driver, keysToSend, waittill, configVO, fetchMetadataVO);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked tableSendKeys" + scripNumber);
//			String xpath = "//h1[text()='Assets']/following::span[text()='Description']/preceding::input[contains(@id,'descColumn::content')]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  tableSendKeys" + scripNumber);
//			System.out.println(e);
//		}
//
//		// Add Xpath for "Journal submitted but Approver on vacation(RTR.GL.115)"
//
//		try {
//			WebElement waittill = driver.findElement(By.xpath("//*[text()='" + param1 + "']/following::span[text()='"
//					+ param2 + "']/preceding::input[contains(@id,'journalBatch::content')]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittill).build().perform();
//			typeIntoValidxpath(driver, keysToSend, waittill, configVO, fetchMetadataVO);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked tableSendKeys" + scripNumber);
//			String xpath = "//*[text()='param1']/following::span[text()='param2']/preceding::input[contains(@id,'journalBatch::content')]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during tableSendKeys" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebElement waittill = driver.findElement(By.xpath(
//					"//h1[text()='" + param1 + "']/following::label[text()='" + param2 + "']/preceding::input[1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittill).build().perform();
//			typeIntoValidxpath(driver, keysToSend, waittill, configVO, fetchMetadataVO);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			Thread.sleep(5000);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked tableSendKeys" + scripNumber);
//			String xpath = "//h1[text()='param1']/following::label[text()='param2']/preceding::input[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  tableSendKeys" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebElement waittill = driver.findElement(By.xpath("(//*[text()=\"" + param1
//					+ "\"]/following::label[text()='" + param2 + "']/preceding-sibling::input)[1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittill).build().perform();
//			typeIntoValidxpath(driver, keysToSend, waittill, configVO, fetchMetadataVO);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked tableSendKeys" + scripNumber);
//			String xpath = "(//*[text()='param1']/following::label[text()='param2']/preceding-sibling::input)[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  tableSendKeys" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebElement waittill = driver.findElement(By.xpath("(//*[text()=\"" + param1
//					+ "\"]/following::label[text()='" + param2 + "']/preceding-sibling::textarea)[1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittill).build().perform();
//			typeIntoValidxpath(driver, keysToSend, waittill, configVO, fetchMetadataVO);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked tableSendKeys" + scripNumber);
//			String xpath = "(//*[text()='param1']/following::label[text()='param2']/preceding-sibling::textarea)[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  tableSendKeys" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebElement waittill = driver
//					.findElement(By.xpath("//*[text()=\"" + param1 + "\"]/following::table[@summary='" + param2
//							+ "']//*[text()='" + param3 + "']/following::input[contains(@id,'NewBdgtPctLst')][1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittill).build().perform();
//			typeIntoValidxpath(driver, keysToSend, waittill, configVO, fetchMetadataVO);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked tableSendKeys" + scripNumber);
//			String xpath = "//*[text()='param1']/following::table[@summary='param2']//*[text()='param3']/following::input[contains(@id,'NewBdgtPctLst')][1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  tableSendKeys" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			// tab(driver, fetchMetadataVO, configVO);
//			// Thread.sleep(1000);
//			// enter(driver, fetchMetadataVO, configVO);
//			WebElement waittill = driver.findElement(By.xpath("(//table[@summary='" + param1 + "']//label[text()='"
//					+ param2 + "']/preceding-sibling::input)[1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittill).build().perform();
//			typeIntoValidxpath(driver, keysToSend, waittill, configVO, fetchMetadataVO);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked tableSendKeys" + scripNumber);
//			String xpath = "(//table[@summary='param1']//label[text()='param2']/preceding-sibling::input)[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			return;
//		} catch (Exception e) {
//			System.out.println(e);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  tableSendKeys" + scripNumber);
//			screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, configVO);
//			throw e;
//		}
//	}
//
//	public void tableDropdownTexts(WebDriver driver, String param1, String param2, String keysToSend,
//			TestRunExecutionVO fetchMetadataVO, ConfigVO configVO) throws Exception {
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.textToBePresentInElementLocated(
//					By.xpath("//div[@class='AFDetectExpansion']/following::*[text()='" + keysToSend + "']"),
//					keysToSend));
//			WebElement waittext = driver.findElement(
//					By.xpath("//div[@class='AFDetectExpansion']/following::*[text()='" + keysToSend + "']"));
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked tableDropdownTexts" + scripNumber);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  tableDropdownTexts" + scripNumber);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(
//					By.xpath("//table[@summary='" + param1 + "']/following::li[text()='" + keysToSend + "']")));
//			wait.until(ExpectedConditions.textToBePresentInElementLocated(
//					By.xpath("//table[@summary='" + param1 + "']/following::li[text()='" + keysToSend + "']"),
//					keysToSend));
//			WebElement waittext = driver.findElement(
//					By.xpath("//table[@summary='" + param1 + "']/following::li[text()='" + keysToSend + "']"));
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked tableDropdownTexts" + scripNumber);
//			return;
//		} catch (Exception e) {
//			System.out.println(e);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  tableDropdownTexts" + scripNumber);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(
//					By.xpath("//*[text()='" + param1 + "']/following::li[text()='" + keysToSend + "']")));
//			wait.until(ExpectedConditions.textToBePresentInElementLocated(
//					By.xpath("//*[text()='" + param1 + "']/following::li[text()='" + keysToSend + "']"), keysToSend));
//			WebElement waittext = driver
//					.findElement(By.xpath("//*[text()='" + param1 + "']/following::li[text()='" + keysToSend + "']"));
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked tableDropdownTexts" + scripNumber);
//			return;
//		} catch (Exception e) {
//			System.out.println(e);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  tableDropdownTexts" + scripNumber);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(
//					By.xpath("//*[text()='" + param1 + "']/following::td[text()='" + keysToSend + "']")));
//			wait.until(ExpectedConditions.textToBePresentInElementLocated(
//					By.xpath("//*[text()='" + param1 + "']/following::td[text()='" + keysToSend + "']"), keysToSend));
//			WebElement waittext = driver
//					.findElement(By.xpath("//*[text()='" + param1 + "']/following::td[text()='" + keysToSend + "']"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked tableDropdownTexts" + scripNumber);
//			return;
//		} catch (Exception e) {
//			System.out.println(e);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  tableDropdownTexts" + scripNumber);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
//					"//div[contains(@id,'dropdownPopup::content')]/following::a[contains(text(),'Search')][1]")));
////		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//a[contains(text(),'Search')]"), "Search"));
//			WebElement search = driver.findElement(By
//					.xpath("//div[contains(@id,'dropdownPopup::content')]/following::a[contains(text(),'Search')][1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(search).build().perform();
//			search.click();
//			Thread.sleep(10000);
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
//					"//div[contains(@id,'PopupId::content')]//*[text()='Search']/following::*[normalize-space(text())='"
//							+ param2 + "']/following::input[1]")));
//			WebElement searchResult = driver.findElement(By.xpath(
//					"//div[contains(@id,'PopupId::content')]//*[text()='Search']/following::*[normalize-space(text())='"
//							+ param2 + "']/following::input[1]"));
//			typeIntoValidxpath(driver, keysToSend, searchResult, configVO, fetchMetadataVO);
//			if (keysToSend != null) {
//				//enter(driver, fetchMetadataVO, configVO);
//				Thread.sleep(5000);
//				WebElement text = driver.findElement(By.xpath("(//span[contains(text(),'" + keysToSend + "')])[1]"));
//				text.click();
//			}
//			try {
//				WebElement button = driver.findElement(By.xpath(
//						"//*[text()='Search']/following::*[text()='" + param2 + "']/following::*[text()='K'][1]"));
//				button.click();
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked tableDropdownTexts" + scripNumber);
//			} catch (Exception e) {
//				WebElement button = driver.findElement(By.xpath(
//						"//*[text()='Search']/following::*[text()='" + param2 + "']/following::*[text()='OK'][1]"));
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.error("Failed during  tableDropdownTexts" + scripNumber);
//				button.click();
//			}
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  tableDropdownTexts" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			try {
//				WebElement searchResult = driver
//						.findElement(By.xpath("//*[text()='Search']/following::*[text()='Name']/following::input[1]"));
//				typeIntoValidxpath(driver, keysToSend, searchResult, configVO, fetchMetadataVO);
//				//enter(driver, fetchMetadataVO, configVO);
//				Thread.sleep(5000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked tableDropdownTexts" + scripNumber);
//			} catch (Exception e) {
//				WebElement searchResult = driver
//						.findElement(By.xpath("//*[text()='Search']/following::*[text()='Value']/following::input[1]"));
//				typeIntoValidxpath(driver, keysToSend, searchResult, configVO, fetchMetadataVO);
//				//enter(driver, fetchMetadataVO, configVO);
//				Thread.sleep(5000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.error("Failed during  tableDropdownTexts" + scripNumber);
//			}
//
//			WebElement text = driver.findElement(By.xpath("(//span[contains(text(),'" + keysToSend + "')])[1]"));
//			text.click();
//			Thread.sleep(1000);
//			try {
//				WebElement button = driver.findElement(
//						By.xpath("//*[text()='Search']/following::*[text()='Name']/following::*[text()='OK'][1]"));
//				button.click();
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.info("Sucessfully Clicked tableDropdownTexts" + scripNumber);
//			} catch (Exception e) {
//				WebElement button = driver.findElement(
//						By.xpath("//*[text()='Search']/following::*[text()='Value']/following::*[text()='OK'][1]"));
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.error("Failed during  tableDropdownTexts" + scripNumber);
//				button.click();
//			}
//
//			return;
//		} catch (Exception e) {
//			System.out.println(e);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  tableDropdownTexts" + scripNumber);
//		}
//		try {
//			WebElement button = driver
//					.findElement(By.xpath("//*[text()='Search']/following::*[normalize-space(text())='" + param2
//							+ "']/following::*[text()='OK'][1]"));
//			button.click();
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked tableDropdownTexts" + scripNumber);
//			return;
//		} catch (Exception e) {
//			System.out.println(e);
//			screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  tableDropdownTexts" + scripNumber);
//			throw e;
//		}
//	}
//
//	public void tableDropdownValues(WebDriver driver, String param1, String param2, String keysToSend,
//			TestRunExecutionVO fetchMetadataVO, ConfigVO configVO) throws Exception {
//		try {
//			if (param1.equalsIgnoreCase("Add Project Customer")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(@id,'projectRole')]")));
//				WebElement waittext = driver.findElement(By.xpath("//a[contains(@id,'projectRole')]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				tableDropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, configVO);
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//
//				String xpath = "(//a[contains(@id,'projectRole')]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				log.info("Sucessfully Clicked tableDropdownValues" + scripNumber);
//				return;
//			}
//		} catch (Exception e) {
//			System.out.println(e);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  tableDropdownValues" + scripNumber);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(
//					By.xpath("(//*[text()='" + param1 + "']/following::a[contains(@id,'" + param2 + "')])[1]")));
//			WebElement waittext = driver.findElement(
//					By.xpath("(//*[text()='" + param1 + "']/following::a[contains(@id,'" + param2 + "')])[1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			tableDropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, configVO);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//
//			String xpath = "(//*[text()='param1']/following::a[contains(@id,'param2')])[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			log.info("Sucessfully Clicked tableDropdownValues" + scripNumber);
//			return;
//		} catch (Exception e) {
//			System.out.println(e);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  tableDropdownValues" + scripNumber);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Billing")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
//						"//*[text()='" + param1 + "']/following::label[text()='" + param2 + "']/following::a[1]")));
//				WebElement waittext = driver.findElement(By.xpath(
//						"//*[text()='" + param1 + "']/following::label[text()='" + param2 + "']/following::a[1]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				Thread.sleep(2000);
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				tableDropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, configVO);
//				Thread.sleep(3000);
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//
//				String xpath = "//*[text()='param1']/following::label[text()='param2']/following::a[1]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				log.info("Sucessfully Clicked Billing tableDropdownValues" + scripNumber);
//				return;
//			}
//		} catch (Exception e) {
//			System.out.println(e);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Billing tableDropdownValues" + scripNumber);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By
//					.xpath("//*[text()='" + param1 + "']/following::label[text()='" + param2 + "']/preceding::a[1]")));
//			WebElement waittext = driver.findElement(
//					By.xpath("//*[text()='" + param1 + "']/following::label[text()='" + param2 + "']/preceding::a[1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			tableDropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, configVO);
//			Thread.sleep(3000);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//
//			String xpath = "//*[text()='param1']/following::label[text()='param2']/preceding::a[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			log.info("Sucessfully Clicked tableDropdownValues" + scripNumber);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during tableDropdownValues" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(
//					By.xpath("//table[@summary='" + param1 + "']//input/following-sibling::a[1]")));
//			WebElement waittext = driver
//					.findElement(By.xpath("//table[@summary='" + param1 + "']//input/following-sibling::a[1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			tableDropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, configVO);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//
//			String xpath = "//table[@summary='param1']//input/following-sibling::a[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			log.info("Sucessfully Clicked tableDropdownValues" + scripNumber);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  tableDropdownValues" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()='" + param1
//					+ "']/following::input[contains(@id,'" + param2 + "')][1]/following::a[1]")));
//			WebElement waittext = driver.findElement(By.xpath("//*[text()='" + param1
//					+ "']/following::input[contains(@id,'" + param2 + "')][1]/following::a[1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			tableDropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, configVO);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//
//			String xpath = "//*[text()='param1']/following::input[contains(@id,'param2')][1]/following::a[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			log.info("Sucessfully Clicked tableDropdownValues" + scripNumber);
//			return;
//		} catch (Exception e) {
//			System.out.println(e);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  tableDropdownValues" + scripNumber);
//			screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, configVO);
//			throw e;
//		}
//	}
//
//	public void dropdownValues(WebDriver driver, String param1, String param2, String param3, String keysToSend,
//			TestRunExecutionVO fetchMetadataVO, ConfigVO configVO) throws Exception {
//		try {
//			if (param1.equalsIgnoreCase("Schedule New Process") && param2.equalsIgnoreCase("Name")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(
//						By.xpath("//div[@class='AFDetectExpansion']/following::*[text()='" + param1
//								+ "']/following::*[normalize-space(text())='" + param2 + "']/following::a[1]")));
//				WebElement waittext = driver
//						.findElement(By.xpath("//div[@class='AFDetectExpansion']/following::*[text()='" + param1
//								+ "']/following::*[normalize-space(text())='" + param2 + "']/following::a[1]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(2000);
//				WebElement search = driver.findElement(By.xpath("//a[contains(text(),'Search')]"));
//				clickValidateXpath(driver, fetchMetadataVO, search, configVO);
//				Thread.sleep(5000);
//				WebElement values = driver.findElement(By.xpath(
//						"//div[@class='AFDetectExpansion']/following::*[text()='Search']/following::*[normalize-space(text())='"
//								+ param2 + "']/following::input[1]"));
//				typeIntoValidxpath(driver, keysToSend, values, configVO, fetchMetadataVO);
//				//enter(driver, fetchMetadataVO, configVO);
//				WebElement select = driver
//						.findElement(By.xpath("//div[@class='AFDetectExpansion']/following::span[starts-with(text(),'"
//								+ keysToSend + "')][1]"));
//				clickValidateXpath(driver, fetchMetadataVO, select, configVO);
//				WebElement searchok = driver
//						.findElement(By.xpath("//div[@class='AFDetectExpansion']/following::span[contains(text(),'"
//								+ keysToSend + "')][1]/following::button[text()='OK'][1]"));
//				clickValidateXpath(driver, fetchMetadataVO, searchok, configVO);
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				Thread.sleep(5000);
//				WebElement ok = driver.findElement(By.xpath("//div[@class='AFDetectExpansion']/following::*[text()='"
//						+ param1 + "']/following::*[normalize-space(text())='" + param2
//						+ "']/following::a[1]/following::button[text()='OK']"));
//				ok.click();
//				Thread.sleep(6000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//
//				String xpath = "//div[@class='AFDetectExpansion']/following::*[text()='param1']/following::*[normalize-space(text())='param2 ']/following::a[1]"
//						+ ";" + "//a[contains(text(),'Search')]" + ";"
//						+ "//div[@class='AFDetectExpansion']/following::*[text()='Search']/following::*[normalize-space(text())=' param2 ']/following::input[1]"
//						+ ";"
//						+ "//div[@class='AFDetectExpansion']/following::span[contains(text(),'keysToSend ')][1]/following::button[text()='OK'][1]"
//						+ ";"
//						+ "//div[@class='AFDetectExpansion']/following::*[text()=' param1 ']/following::*[normalize-space(text())='param2']/following::a[1]/following::button[text()='OK']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				log.info("Sucessfully Clicked Schedule New Process or Name dropdownValues" + scripNumber);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Schedule New Process or Name dropdownValues" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Invoice Header") && param2.equalsIgnoreCase("Business Unit")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1
//						+ "']/following::label[text()='" + param2 + "']/following::a[1]")));
//				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
//						+ "']/following::label[text()='" + param2 + "']/following::a[1]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//
//				String xpath = "//*[normalize-space(text())='param1']/following::label[text()='param2']/following::a[1]"
//						+ ";" + "//a[contains(text(),'Search')][1]" + ";"
//						+ "//*[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::input[1]"
//						+ ";" + "//span[text()='Name']/following::span[normalize-space(text())='keysToSend']" + ";"
//						+ "//*[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::*[text()='OK'][1]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				try {
//					actions.click(waittext).build().perform();
//					Thread.sleep(10000);
//					// WebElement popup1 =
//					// driver.findElement(By.xpath("//div[contains(@id,'suggestions-popup')]"));
//					WebElement search = driver.findElement(By.xpath("//a[contains(text(),'Search')][1]"));
//					search.click();
//					Thread.sleep(3000);
//					wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
//							"//*[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::input[1]")));
//					WebElement searchResult = driver.findElement(By.xpath(
//							"//*[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::input[1]"));
//					typeIntoValidxpath(driver, keysToSend, searchResult, configVO, fetchMetadataVO);
//					//enter(driver, fetchMetadataVO, configVO);
//					Thread.sleep(5000);
//					WebElement text = driver.findElement(By.xpath(
//							"//span[text()='Name']/following::span[normalize-space(text())='" + keysToSend + "']"));
//					text.click();
//					Thread.sleep(1000);
//					WebElement button = driver.findElement(By.xpath(
//							"//*[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::*[text()='OK'][1]"));
//					button.click();
//
//					log.info("Sucessfully Clicked Invoice Header or Business Unit dropdownValues" + scripNumber);
//
//				} catch (Exception e) {
//					// String scripNumber = fetchMetadataVO.getScriptNumber();
//					log.error("Failed during Invoice Header or Business Unit  dropdownValues" + scripNumber);
//					System.out.println(e);
//				}
//				return;
//			}
//		} catch (Exception ex) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  dropdownValues" + scripNumber);
//			System.out.println(ex);
//		}
//		// This is to select the dropdown and select 'All' and deselect All then
//		// Selecting Draft
//		try {
//			if (param2.equalsIgnoreCase("Project Status") && keysToSend.equalsIgnoreCase("Draft")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1
//						+ "']/following::label[text()='" + param2 + "']/following::a[1]")));
//				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
//						+ "']/following::label[text()='" + param2 + "']/following::a[1]"));
//				Actions actions = new Actions(driver);
//				actions.click(waittext).build().perform();
//				Thread.sleep(4000);
//				WebElement checkbox = driver.findElement(By.xpath("//label[text()='All']"));
//				checkbox.click();
//				Thread.sleep(3000);
//				checkbox.click();
//				WebElement text = driver.findElement(By.xpath("//label[text()='" + param2
//						+ "']/following::label[normalize-space(text())='" + keysToSend + "']"));
//				text.click();
//				Thread.sleep(1000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//
//				String xpath = "//*[normalize-space(text())='param1']/following::label[text()='param2']/following::a[1]"
//						+ ";" + "//label[text()='All']" + ";"
//						+ "//label[text()='param2']/following::label[normalize-space(text())='keysToSend']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				log.info("Sucessfully Clicked Project Status or Draft dropdownValues" + scripNumber);
//				return;
//			}
//		} catch (Exception ex) {
//			System.out.println(ex);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Create Contract in Wizard") && param2.equalsIgnoreCase("Primary Party")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1
//						+ "']/following::label[text()='" + param2 + "']/following::a[1]")));
//				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
//						+ "']/following::label[text()='" + param2 + "']/following::a[1]"));
//				Actions actions = new Actions(driver);
//				actions.click(waittext).build().perform();
//				Thread.sleep(4000);
//				WebElement Search = driver.findElement(By.xpath("(//a[text()='Search...'])[3]"));
//				Search.click();
//				WebElement Name = driver.findElement(By.xpath(
//						"//h2[normalize-space(text())='Search']/following::label[text()='Name']/following::input[1]"));
//				typeIntoValidxpath(driver, keysToSend, Name, configVO, fetchMetadataVO);
//				//enter(driver, fetchMetadataVO, configVO);
//				Thread.sleep(5000);
//				WebElement text = driver.findElement(By
//						.xpath("//span[text()='Name']/following::span[normalize-space(text())='" + keysToSend + "']"));
//				text.click();
//				Thread.sleep(1000);
//				WebElement button = driver.findElement(By.xpath(
//						"//*[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::*[text()='OK'][1]"));
//				button.click();
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//
//				String xpath = "//*[normalize-space(text())='param1']/following::label[text()='param2']/following::a[1]"
//						+ ";" + "(//a[text()='Search...'])[3]" + ";"
//						+ "//h2[normalize-space(text())='Search']/following::label[text()='Name']/following::input[1]"
//						+ ";" + "//span[text()='Name']/following::span[normalize-space(text())='keysToSend']" + ";"
//						+ "//*[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::*[text()='OK'][1]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				return;
//			}
//		} catch (Exception ex) {
//			System.out.println(ex);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Create Bank Account") && param2.equalsIgnoreCase("Country")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1
//						+ "']/following::label[text()='" + param2 + "']/following::a[1]")));
//				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
//						+ "']/following::label[text()='" + param2 + "']/following::a[1]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				actions.click(waittext).build().perform();
//				Thread.sleep(10000);
//				WebElement search = driver.findElement(By.xpath(
//						"//*[contains(@id,'territoryShortNameId')]/following-sibling::a[contains(text(),'Search')]"));
//				search.click();
//				Thread.sleep(3000);
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
//						"(//h2[normalize-space(text())='Search']/following::label[normalize-space(text())='Name']/following::input)[1]")));
//				WebElement searchResult = driver.findElement(By.xpath(
//						"(//h2[normalize-space(text())='Search']/following::label[normalize-space(text())='Name']/following::input)[1]"));
//				typeIntoValidxpath(driver, keysToSend, searchResult, configVO, fetchMetadataVO);
//				//enter(driver, fetchMetadataVO, configVO);
//				Thread.sleep(5000);
//				WebElement text = driver.findElement(By
//						.xpath("//span[text()='Name']/following::span[normalize-space(text())='" + keysToSend + "']"));
//				text.click();
//				Thread.sleep(1000);
//				WebElement button = driver.findElement(By.xpath(
//						"//*[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::*[text()='OK'][1]"));
//				button.click();
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//
//				String xpath = "//*[normalize-space(text())='param1']/following::label[text()='param2']/following::a[1]"
//						+ ";"
//						+ "//*[contains(@id,'territoryShortNameId')]/following-sibling::a[contains(text(),'Search')]"
//						+ ";"
//						+ "(//h2[normalize-space(text())='Search']/following::label[normalize-space(text())='Name']/following::input)[1]"
//						+ ";" + "//span[text()='Name']/following::span[normalize-space(text())='keysToSend']" + ";"
//						+ "//*[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::*[text()='OK'][1]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				log.info("Sucessfully Clicked Create Bank Account or Country dropdownValues" + scripNumber);
//				return;
//			}
//		} catch (Exception ex) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Create Bank Account or Country dropdownValues" + scripNumber);
//			System.out.println(ex);
//		}
//		// for "PTP.PO.301 Request New Supplier" when exectuing in Fusion instance
//		try {
//			if (param1.equalsIgnoreCase("Company Details") && param2.equalsIgnoreCase("Tax Country")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1
//						+ "']/following::label[text()='" + param2 + "']/following::a[@title='Search: Tax Country']")));
//				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
//						+ "']/following::label[text()='" + param2 + "']/following::a[@title='Search: Tax Country']"));
//				Actions actions = new Actions(driver);
//				actions.click(waittext).build().perform();
//				Thread.sleep(4000);
//				WebElement Search = driver.findElement(By.xpath("(//a[text()='Search...'])"));
//				Search.click();
//				WebElement Name = driver.findElement(By.xpath(
//						"//div[normalize-space(text())='Search and Select: Tax Country']/following::label[text()='Name']/following::input[1]"));
//				typeIntoValidxpath(driver, keysToSend, Name, configVO, fetchMetadataVO);
//				//enter(driver, fetchMetadataVO, configVO);
//				Thread.sleep(5000);
//				WebElement text = driver.findElement(By
//						.xpath("//span[text()='Name']/following::span[normalize-space(text())='" + keysToSend + "']"));
//				text.click();
//				Thread.sleep(1000);
//				WebElement button = driver.findElement(By.xpath(
//						"//*[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::*[text()='OK'][1]"));
//				button.click();
//				return;
//			}
//		} catch (Exception ex) {
//			System.out.println(ex);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Create Address") && param2.equalsIgnoreCase("Country")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1
//						+ "']/following::label[text()='" + param2 + "']/following::a[@title='Search: Country']")));
//				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
//						+ "']/following::label[text()='" + param2 + "']/following::a[@title='Search: Country']"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				actions.click(waittext).build().perform();
//				Thread.sleep(10000);
//				WebElement search = driver.findElement(By.xpath(
//						"//*[contains(@id,'inputComboboxListOfValues1')]/following-sibling::a[contains(text(),'Search')]"));
//				search.click();
//				Thread.sleep(3000);
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
//						"(//h2[normalize-space(text())='Search']/following::label[normalize-space(text())='Name']/following::input)[1]")));
//				WebElement searchResult = driver.findElement(By.xpath(
//						"(//h2[normalize-space(text())='Search']/following::label[normalize-space(text())='Name']/following::input)[1]"));
//				typeIntoValidxpath(driver, keysToSend, searchResult, configVO, fetchMetadataVO);
//				//enter(driver, fetchMetadataVO, configVO);
//				Thread.sleep(5000);
//				WebElement text = driver.findElement(By
//						.xpath("//span[text()='Name']/following::span[normalize-space(text())='" + keysToSend + "']"));
//				text.click();
//				Thread.sleep(5000);
//				WebElement button = driver.findElement(By.xpath(
//						"//button[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::button[text()='OK'][1]"));
//				button.click();
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//
//				String xpath = "//*[normalize-space(text())='param1']/following::label[text()='param2']/following::a[@title='Search: Country']"
//						+ ";"
//						+ "//*[contains(@id,'inputComboboxListOfValues1')]/following-sibling::a[contains(text(),'Search')]"
//						+ ";"
//						+ "(//h2[normalize-space(text())='Search']/following::label[normalize-space(text())='Name']/following::input)[1]"
//						+ ";" + "//span[text()='Name']/following::span[normalize-space(text())='keysToSend']" + ";"
//						+ "//button[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::button[text()='OK'][1]";
//
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				log.info("Sucessfully Clicked Create Address or Country dropdownValues" + scripNumber);
//				return;
//			}
//		} catch (Exception ex) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Create Address or Country  dropdownValues" + scripNumber);
//			System.out.println(ex);
//		}
//		try {
//
//			if (param1.equalsIgnoreCase("Assets")) {
//
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//
//				wait.until(ExpectedConditions.presenceOfElementLocated(
//						By.xpath("//*[text()='" + param1 + "']/following::a[@role='button'][1]")));
//
//				WebElement waittext = driver
//						.findElement(By.xpath("//*[text()='" + param1 + "']/following::a[@role='button'][1]"));
//
//				Actions actions = new Actions(driver);
//
//				actions.moveToElement(waittext).build().perform();
//
//				// clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//
//				waittext.click();
//
//			///	dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, configVO);
//				Thread.sleep(30000);
//
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//
//				String xpath = "//*[text()='param1']/following::a[@role='button'][1]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				log.info("Sucessfully Clicked Assets dropdownValues" + scripNumber);
//				return;
//
//			}
//
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Assets dropdownValues" + scripNumber);
//			System.out.println(e);
//
//		}
//		try {
//			if (param1.equalsIgnoreCase("Create Request") || param2.equalsIgnoreCase("CIP Budget Code")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[contains(text(),'" + param1
//						+ "')]/following::label[normalize-space(text())='" + param2 + "']/following::a)[1]")));
//				WebElement waittext = driver.findElement(By.xpath("(//*[contains(text(),'" + param1
//						+ "')]/following::label[normalize-space(text())='" + param2 + "']/following::a)[1]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			//	dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, configVO);
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//
//				String xpath = "(//*[contains(text(),'param1')]/following::label[normalize-space(text())='param2']/following::a)[1]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				log.info("Sucessfully Clicked Create Request dropdownValues" + scripNumber);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Create Request dropdownValues" + scripNumber);
//			System.out.println(e);
//		}
//
//		try {
//			if (param1.equalsIgnoreCase("Payables to Ledger Reconciliation Report")) {
//
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				Thread.sleep(10000);
//				wait.until(ExpectedConditions.presenceOfElementLocated(
//						By.xpath("(//span[contains(text(),'" + param2 + "')]/following::img)[1]")));
//				WebElement waittext = driver
//						.findElement(By.xpath("(//span[contains(text(),'" + param2 + "')]/following::img)[1]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				// clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				waittext.click();
//				Thread.sleep(4000);
//				WebElement dropdown = driver.findElement(By.xpath("//span[text()='" + keysToSend + "']"));
//				actions.moveToElement(dropdown).build().perform();
//				dropdown.click();
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//
//				String xpath = "(//span[contains(text(),'param2')]/following::img)[1]" + ";"
//						+ "//span[text()='keysToSend']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				log.info("Sucessfully Clicked Payables to Ledger Reconciliation Report dropdownValues" + scripNumber);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Payables to Ledger Reconciliation Report dropdownValues" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//
//			if (param1.equalsIgnoreCase("P2P-3031-Spend Detail by Invoice Number")
//					|| param1.equalsIgnoreCase("P2P-3026-Payment Terms by Supplier vs Actual Days Paid")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				Thread.sleep(10000);
//				wait.until(ExpectedConditions.presenceOfElementLocated(
//						By.xpath("//*[normalize-space(text())='" + param2 + "']/following::input[1]")));
//				WebElement waittext = driver
//						.findElement(By.xpath("//*[normalize-space(text())='" + param2 + "']/following::input[1]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//
//				Thread.sleep(2000);
//				if (param2.equalsIgnoreCase("Procurement BU") || param2.equalsIgnoreCase("Business Unit")) {
//					WebElement search = driver
//							.findElement(By.xpath("//div[@class='listbox']//span[contains(text(),'Search')]"));
//					// clickValidateXpath(driver, fetchMetadataVO, search, configVO);
//					search.click();
//					Thread.sleep(1000);
//					WebElement values = driver.findElement(By.xpath("(//span[text()='Name']/following::input)[1]"));
//					typeIntoValidxpath(driver, keysToSend, values, configVO, fetchMetadataVO);
//					//enter(driver, fetchMetadataVO, configVO);
//					WebElement select = driver.findElement(By
//							.xpath("//*[text()='Name']/following::div[normalize-space(text())='" + keysToSend + "']"));
//					clickValidateXpath(driver, fetchMetadataVO, select, configVO);
//					WebElement searchok = driver
//							.findElement(By.xpath("//span[text()='Name']/following::button[text()='OK']"));
//					clickValidateXpath(driver, fetchMetadataVO, searchok, configVO);
//					screenshot(driver, "", fetchMetadataVO, configVO);
//					String scripNumber = fetchMetadataVO.getScriptNumber();
//
//					String xpath = "//*[normalize-space(text())='param2']/following::input[1]" + ";"
//							+ "//div[@class='listbox']//span[contains(text(),'Search')]" + ";"
//							+ "(//span[text()='Name']/following::input)[1]" + ";"
//							+ "//span[text()='Name']/following::button[text()='OK']";
//					testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//					log.info("Sucessfully Clicked Procurement BU or Business Unit dropdownValues" + scripNumber);
//					return;
//				} else if (param2.equalsIgnoreCase("Requisition BU")) {
//					Thread.sleep(2000);
//					WebElement search = driver
//							.findElement(By.xpath("(//div[@class='listbox']//span[contains(text(),'Search')])[1]"));
//					// clickValidateXpath(driver, fetchMetadataVO, search, configVO);
//					search.click();
//					Thread.sleep(1000);
//					WebElement values = driver
//							.findElement(By.xpath("(//span[text()='Name']/following::input[@type='text'])[2]"));
//					typeIntoValidxpath(driver, keysToSend, values, configVO, fetchMetadataVO);
//					//enter(driver, fetchMetadataVO, configVO);
//					WebElement select = driver
//							.findElement(By.xpath("//*[text()='Name']/following::div[text()='" + keysToSend + "']"));
//					clickValidateXpath(driver, fetchMetadataVO, select, configVO);
//					WebElement searchok = driver
//							.findElement(By.xpath("//span[text()='Name']/following::button[text()='OK'][2]"));
//					clickValidateXpath(driver, fetchMetadataVO, searchok, configVO);
//					screenshot(driver, "", fetchMetadataVO, configVO);
//					String scripNumber = fetchMetadataVO.getScriptNumber();
//
//					String xpath = "//*[normalize-space(text())='param2']/following::input[1]" + ";"
//							+ "(//div[@class='listbox']//span[contains(text(),'Search')])[1]" + ";"
//							+ "(//span[text()='Name']/following::input[@type='text'])[2]" + ";"
//							+ "//*[text()='Name']/following::div[text()='" + keysToSend + "']" + ";"
//							+ "//span[text()='Name']/following::button[text()='OK'][2]";
//					testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//					log.info("Sucessfully Clicked Requisition BU dropdownValues" + scripNumber);
//					return;
//				} else if (param2.equalsIgnoreCase("Supplier Name")) {
//					if (keysToSend.equalsIgnoreCase("All")) {
//						WebElement select = driver.findElement(By.xpath("//span[text()='" + param2
//								+ "']/following::div[normalize-space(text())='" + keysToSend + "'][1]"));
//						clickValidateXpath(driver, fetchMetadataVO, select, configVO);
//						return;
//					} else {
//						WebElement search = driver
//								.findElement(By.xpath("(//div[@class='listbox']//span[contains(text(),'Search')])[3]"));
//						clickValidateXpath(driver, fetchMetadataVO, search, configVO);
//						Thread.sleep(1000);
//						WebElement values = driver
//								.findElement(By.xpath("(//span[text()='Name']/following::input[@type='text'])[3]"));
//						typeIntoValidxpath(driver, keysToSend, values, configVO, fetchMetadataVO);
//					//	//enter(driver, fetchMetadataVO, configVO);
//						WebElement select = driver.findElement(By.xpath(
//								"//*[text()='Name']/following::div[normalize-space(text())='" + keysToSend + "']"));
//						clickValidateXpath(driver, fetchMetadataVO, select, configVO);
//						WebElement searchok = driver
//								.findElement(By.xpath("//span[text()='Name']/following::button[text()='OK'][3]"));
//						clickValidateXpath(driver, fetchMetadataVO, searchok, configVO);
//						screenshot(driver, "", fetchMetadataVO, configVO);
//						String scripNumber = fetchMetadataVO.getScriptNumber();
//
//						String xpath = "//*[normalize-space(text())='param2']/following::input[1]" + ";"
//								+ "(//div[@class='listbox']//span[contains(text(),'Search')])[3]" + ";"
//								+ "(//span[text()='Name']/following::input[@type='text'])[3]" + ";"
//								+ "//*[text()='Name']/following::div[normalize-space(text())='" + keysToSend + "']"
//								+ ";" + "//span[text()='Name']/following::button[text()='OK'][3]";
//						testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//						log.info("Sucessfully  Supplier Name Clicked dropdownValues" + scripNumber);
//
//						return;
//					}
//				}
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  dropdownValues" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("FIN-7073-UDG Cognos Extract")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				Thread.sleep(10000);
//				wait.until(ExpectedConditions.presenceOfElementLocated(
//						By.xpath("//*[normalize-space(text())='" + param2 + "']/following::input[1]")));
//				WebElement waittext = driver
//						.findElement(By.xpath("//*[normalize-space(text())='" + param2 + "']/following::input[1]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(2000);
//				if (param2.equalsIgnoreCase("Period Name")) {
//					WebElement search = driver
//							.findElement(By.xpath("//div[@class='listbox']//span[contains(text(),'Search')]"));
//					clickValidateXpath(driver, fetchMetadataVO, search, configVO);
//					Thread.sleep(1000);
//					WebElement values = driver.findElement(By.xpath("(//span[text()='Name']/following::input)[1]"));
//					typeIntoValidxpath(driver, keysToSend, values, configVO, fetchMetadataVO);
//					//enter(driver, fetchMetadataVO, configVO);
//					WebElement select = driver.findElement(By
//							.xpath("//*[text()='Name']/following::div[normalize-space(text())='" + keysToSend + "']"));
//					clickValidateXpath(driver, fetchMetadataVO, select, configVO);
//					WebElement searchok = driver
//							.findElement(By.xpath("//span[text()='Name']/following::button[text()='OK']"));
//					clickValidateXpath(driver, fetchMetadataVO, searchok, configVO);
//					screenshot(driver, "", fetchMetadataVO, configVO);
//					String scripNumber = fetchMetadataVO.getScriptNumber();
//
//					String xpath = "//*[normalize-space(text())='param2']/following::input[1]" + ";"
//							+ "//div[@class='listbox']//span[contains(text(),'Search')]" + ";"
//							+ "(//span[text()='Name']/following::input)[1]" + ";"
//							+ "//*[text()='Name']/following::div[normalize-space(text())='keysToSend']" + ";"
//							+ "//span[text()='Name']/following::button[text()='OK']";
//					testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//					log.info("Sucessfully Clicked Period Name dropdownValues" + scripNumber);
//					return;
//				} else if (param2.equalsIgnoreCase("Legal Entity")) {
//					WebElement search = driver
//							.findElement(By.xpath("//div[@class='listbox']//span[contains(text(),'Search')]"));
//					clickValidateXpath(driver, fetchMetadataVO, search, configVO);
//					Thread.sleep(1000);
//					WebElement values = driver.findElement(By.xpath(
//							"//div[@class='masterDialog modalDialog']/following::span[text()='Name']/following::input[1]"));
//					typeIntoValidxpath(driver, keysToSend, values, configVO, fetchMetadataVO);
//					//enter(driver, fetchMetadataVO, configVO);
//					Thread.sleep(6000);
//					WebElement select = driver.findElement(By
//							.xpath("//*[text()='Name']/following::div[normalize-space(text())='" + keysToSend + "']"));
//					clickValidateXpath(driver, fetchMetadataVO, select, configVO);
//					WebElement searchok = driver.findElement(By.xpath(
//							"//div[@class='masterDialog modalDialog']/following::span[text()='Name']/following::button[text()='OK']"));
//					clickValidateXpath(driver, fetchMetadataVO, searchok, configVO);
//					screenshot(driver, "", fetchMetadataVO, configVO);
//					String scripNumber = fetchMetadataVO.getScriptNumber();
//
//					String xpath = "//*[normalize-space(text())='param2']/following::input[1]" + ";"
//							+ "//div[@class='listbox']//span[contains(text(),'Search')]";
//					testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//					log.info("Sucessfully Clicked Legal Entity dropdownValues" + scripNumber);
//					return;
//				}
//			}
//
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  dropdownValues" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("FIN-7056-Generate Customer Statements")
//					&& (param2.equalsIgnoreCase("Legal Entity") || param2.equalsIgnoreCase("Customer Name"))) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				Thread.sleep(10000);
//				wait.until(ExpectedConditions.presenceOfElementLocated(
//						By.xpath("//*[normalize-space(text())='" + param2 + "']/following::input[1]")));
//				WebElement waittext = driver
//						.findElement(By.xpath("//*[normalize-space(text())='" + param2 + "']/following::input[1]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(2000);
//				if (param2.equalsIgnoreCase("Legal Entity")) {
//					WebElement search = driver.findElement(
//							By.xpath("//a[contains(@id,'LEGAL_ENTITY')][1]//span/span[contains(text(),'Search')]"));
//					clickValidateXpath(driver, fetchMetadataVO, search, configVO);
//					Thread.sleep(1000);
//					WebElement values = driver
//							.findElement(By.xpath("(//span[text()='Name']/following::input[@type='text'])[1]"));
//					typeIntoValidxpath(driver, keysToSend, values, configVO, fetchMetadataVO);
//					//enter(driver, fetchMetadataVO, configVO);
//					WebElement select = driver.findElement(By
//							.xpath("//*[text()='Value']/following::div[normalize-space(text())='" + keysToSend + "']"));
//					clickValidateXpath(driver, fetchMetadataVO, select, configVO);
//					WebElement searchok = driver
//							.findElement(By.xpath("//div[text()='Search']/following::button[text()='OK']"));
//					actions.moveToElement(searchok).build().perform();
//					clickValidateXpath(driver, fetchMetadataVO, searchok, configVO);
//					screenshot(driver, "", fetchMetadataVO, configVO);
//					String scripNumber = fetchMetadataVO.getScriptNumber();
//
//					String xpath = "//*[normalize-space(text())='param2']/following::input[1]" + ";"
//							+ "//a[contains(@id,'LEGAL_ENTITY')][1]//span/span[contains(text(),'Search')]" + ";"
//							+ "(//span[text()='Name']/following::input[@type='text'])[1]" + ";"
//							+ "//*[text()='Value']/following::div[normalize-space(text())='keysToSend']" + ";"
//							+ "//div[text()='Search']/following::button[text()='OK']";
//					testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//					log.info("Sucessfully Clicked Legal Entity dropdownValues" + scripNumber);
//					return;
//				} else if (param2.equalsIgnoreCase("Customer Name")) {
//					WebElement search1 = driver.findElement(
//							By.xpath("//a[contains(@id,'CUSTOMER_NAME')][1]//span/span[contains(text(),'Search')]"));
//					clickValidateXpath(driver, fetchMetadataVO, search1, configVO);
//					Thread.sleep(1000);
//					WebElement values = driver.findElement(By.xpath(
//							"//div[@class='masterDialog modalDialog']/following::span[text()='Name']/following::input[@type='text'][1]"));
//					typeIntoValidxpath(driver, keysToSend, values, configVO, fetchMetadataVO);
//					//enter(driver, fetchMetadataVO, configVO);
//					WebElement select = driver.findElement(
//							By.xpath("//*[text()='Value']/following::div[contains(text(),'" + keysToSend + "')]"));
//					clickValidateXpath(driver, fetchMetadataVO, select, configVO);
//					Thread.sleep(1000);
//					WebElement searchok = driver
//							.findElement(By.xpath("//div[text()='Search']/following::button[text()='OK'][2]"));
//					actions.moveToElement(searchok).build().perform();
//					clickValidateXpath(driver, fetchMetadataVO, searchok, configVO);
//					screenshot(driver, "", fetchMetadataVO, configVO);
//					String scripNumber = fetchMetadataVO.getScriptNumber();
//
//					String xpath = "//*[normalize-space(text())='param2']/following::input[1]" + ";"
//							+ "//a[contains(@id,'CUSTOMER_NAME')][1]//span/span[contains(text(),'Search')]" + ";"
//							+ "//div[@class='masterDialog modalDialog']/following::span[text()='Name']/following::input[@type='text'][1]"
//							+ ";" + "//*[text()='Value']/following::div[contains(text(),'" + keysToSend + "')]" + ";"
//							+ "//div[text()='Search']/following::button[text()='OK'][2]";
//					testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//					log.info("Sucessfully Clicked Customer Name dropdownValues" + scripNumber);
//					return;
//				}
//
//			}
//
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  dropdownValues" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//
//			if (param1.equalsIgnoreCase("Applied Receipts Register")
//					|| param1.equalsIgnoreCase("General Journals Report")
//					|| param1.equalsIgnoreCase("Unapplied Receipt Register") || param2.equalsIgnoreCase("Request Name")
//					|| (param1.equalsIgnoreCase("Receivables to Ledger Reconciliation")
//
//							&& param2.equalsIgnoreCase("Ledger"))) {
//
//				if (param1.equalsIgnoreCase("General Journals Report") && (param2.equalsIgnoreCase("Ledger"))) {
//
//					WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//
//					Thread.sleep(5000);
//
//					wait.until(ExpectedConditions.presenceOfElementLocated(
//
//							By.xpath("(//span[text()='" + param2 + "']/following::img)[1]")));
//
//					WebElement waittext = driver
//
//							.findElement(By.xpath("(//span[text()='" + param2 + "']/following::img)[1]"));
//
//					Actions actions = new Actions(driver);
//
//					actions.moveToElement(waittext).build().perform();
//
//					// clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//
//					waittext.click();
//
//					Thread.sleep(2000);
//
//				}
//
//				else {
//
//					WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//
//					Thread.sleep(10000);
//
//					wait.until(ExpectedConditions.presenceOfElementLocated(
//
//							By.xpath("(//span[contains(text(),'" + param2 + "')]/following::img)[1]")));
//
//					WebElement waittext = driver
//
//							.findElement(By.xpath("(//span[contains(text(),'" + param2 + "')]/following::img)[1]"));
//
//					Actions actions = new Actions(driver);
//
//					actions.moveToElement(waittext).build().perform();
//
//					// clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//
//					waittext.click();
//
//					Thread.sleep(2000);
//
//				}
//
//				if (param2.equalsIgnoreCase("Ledger") || param2.equalsIgnoreCase("Ledger Set")
//						|| param2.equalsIgnoreCase("Entered Currency")
//
//						|| param2.equalsIgnoreCase("Approval Status") || param2.equalsIgnoreCase("Batch Status")) {
//
//					WebElement search = driver.findElement(
//
//							By.xpath("//div[@class='floatingWindowDiv']//span[contains(text(),'Search')]"));
//
//					// clickValidateXpath(driver, fetchMetadataVO, search, configVO);
//
//					search.click();
//
//					Thread.sleep(1000);
//
//					WebElement dropdown = driver.findElement(By.xpath("//span[text()='Name']/following::select[1]"));
//
//					dropdown.sendKeys("Starts");
//
//					WebElement values = driver.findElement(By.xpath("(//span[text()='Name']/following::input)[1]"));
//
//					typeIntoValidxpath(driver, keysToSend, values, configVO, fetchMetadataVO);
//
//					//enter(driver, fetchMetadataVO, configVO);
//
//					Thread.sleep(10000);
//
//					WebElement select = driver.findElement(By
//
//							.xpath("//*[text()='Name']/following::span[normalize-space(text())='" + keysToSend + "']"));
//
////								clickValidateXpath(driver, fetchMetadataVO, select, configVO); 
//
//					select.click();
//
//					WebElement searchok = driver
//
//							.findElement(By.xpath("//span[text()='Name']/following::a[text()='OK']"));
//
//					clickValidateXpath(driver, fetchMetadataVO, searchok, configVO);
//
//					// searchok.click();
//
//					screenshot(driver, "", fetchMetadataVO, configVO);
//
//					String scripNumber = fetchMetadataVO.getScriptNumber();
//
//					String xpath = "(//span[contains(text(),'param2')]/following::img)[1]" + ";"
//
//							+ "//div[@class='floatingWindowDiv']//span[contains(text(),'Search')]" + ";"
//
//							+ "(//span[text()='Name']/following::input)[1]" + ";"
//
//							+ "//*[text()='Name']/following::span[normalize-space(text())='keysToSend']" + ";"
//
//							+ "//span[text()='Name']/following::a[text()='OK']";
//
//					testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//					log.info("Sucessfully Clicked Ledger dropdownValues" + scripNumber);
//
//					return;
//
//				} else if (param2.equalsIgnoreCase("Request Name") || param2.equalsIgnoreCase("Source")
//						|| param2.equalsIgnoreCase("Category")) {
//
//					WebElement search = driver.findElement(
//
//							By.xpath("//div[@class='floatingWindowDiv']//span[contains(text(),'Search')]"));
//
//					// clickValidateXpath(driver, fetchMetadataVO, search, configVO);
//
//					search.click();
//
//					Thread.sleep(1000);
//
//					WebElement values = driver.findElement(By.xpath("(//span[text()='Name']/following::input)[1]"));
//
//					typeIntoValidxpath(driver, keysToSend, values, configVO, fetchMetadataVO);
//
//					//enter(driver, fetchMetadataVO, configVO);
//
//					Thread.sleep(6000);
//
//					WebElement select = driver.findElement(By
//
//							.xpath("//*[text()='Name']/following::span[normalize-space(text())='" + keysToSend + "']"));
//
//					// clickValidateXpath(driver, fetchMetadataVO, select, configVO);
//
//					select.click();
//
//					WebElement searchok = driver
//
//							.findElement(By.xpath("//span[text()='Name']/following::a[text()='OK']"));
//
//					clickValidateXpath(driver, fetchMetadataVO, searchok, configVO);
//
//					screenshot(driver, "", fetchMetadataVO, configVO);
//
//					String scripNumber = fetchMetadataVO.getScriptNumber();
//
//					String xpath = "(//span[contains(text(),'param2')]/following::img)[1]" + ";"
//
//							+ "//div[@class='floatingWindowDiv']//span[contains(text(),'Search')]" + ""
//
//							+ "(//span[text()='Name']/following::input)[1]" + ";"
//
//							+ "//*[text()='Name']/following::span[normalize-space(text())='keysToSend']" + ";"
//
//							+ "//span[text()='Name']/following::a[text()='OK']";
//
//					testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//					log.info("Sucessfully Clicked Request Name dropdownValues" + scripNumber);
//
//					return;
//
//				}
//
//				else if (param2.equalsIgnoreCase("Business Unit") || param2.equalsIgnoreCase("Accounting Period")) {
//
//					WebElement search = driver.findElement(
//
//							By.xpath("//div[@class='floatingWindowDiv']//span[contains(text(),'Search')]"));
//
//					// clickValidateXpath(driver, fetchMetadataVO, search, configVO);
//
//					search.click();
//
//					Thread.sleep(1000);
//
//					WebElement values = driver.findElement(By.xpath("(//span[text()='Name']/following::input)[1]"));
//
//					typeIntoValidxpath(driver, keysToSend, values, configVO, fetchMetadataVO);
//
//					//enter(driver, fetchMetadataVO, configVO);
//
//					WebElement select = driver.findElement(By
//
//							.xpath("//*[text()='Name']/following::span[normalize-space(text())='" + keysToSend + "']"));
//
//					// clickValidateXpath(driver, fetchMetadataVO, select, configVO);
//
//					select.click();
//
//					WebElement move = driver.findElement(By
//
//							.xpath("//td[@title='Move']"));
//
//					// clickValidateXpath(driver, fetchMetadataVO, move, configVO);
//
//					move.click();
//
//					WebElement searchok = driver
//
//							.findElement(By.xpath("//span[text()='Name']/following::a[text()='OK']"));
//
//					clickValidateXpath(driver, fetchMetadataVO, searchok, configVO);
//
//					// searchok.click();
//
//					screenshot(driver, "", fetchMetadataVO, configVO);
//
//					String scripNumber = fetchMetadataVO.getScriptNumber();
//
//					String xpath = "(//span[contains(text(),'param2')]/following::img)[1]" + ";"
//
//							+ "//div[@class='floatingWindowDiv']//span[contains(text(),'Search')]" + ";"
//
//							+ "(//span[text()='Name']/following::input)[1]" + ";"
//
//							+ "//*[text()='Name']/following::span[normalize-space(text())='keysToSend']" + ";"
//
//							+ "//span[text()='Name']/following::a[text()='OK']";
//
//					testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//					log.info("Sucessfully Clicked Ledger dropdownValues" + scripNumber);
//
//					return;
//
//				}
//
//			}
//
//		} catch (Exception e) {
//
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//
//			log.error("Failed during  dropdownValues" + scripNumber);
//
//			System.out.println(e);
//
//		}
//		try {
//			if (param1.equalsIgnoreCase("FIN-7064-AP Invoice Summary")
//					|| param1.equalsIgnoreCase("P2P-3000-AP Hold Detailed Report")
//					|| param1.equalsIgnoreCase("FIN-7073-UDG Cognos Extract")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				Thread.sleep(15000);
//				wait.until(ExpectedConditions.presenceOfElementLocated(
//						By.xpath("//*[normalize-space(text())='" + param2 + "']/following::input[1]")));
//				WebElement waittext = driver
//						.findElement(By.xpath("//*[normalize-space(text())='" + param2 + "']/following::input[1]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(2000);
//				WebElement search = driver.findElement(
//						By.xpath("(//a[contains(@id,'legal')or 'LE'][1]//span/span[contains(text(),'Search')])[2]"));
//				clickValidateXpath(driver, fetchMetadataVO, search, configVO);
//				WebElement values = driver
//						.findElement(By.xpath("(//span[text()='Name']/following::input[@type='text'])"));
//				typeIntoValidxpath(driver, keysToSend, values, configVO, fetchMetadataVO);
//				//enter(driver, fetchMetadataVO, configVO);
//				WebElement select = driver.findElement(
//						By.xpath("//b[text()='Value']/following::div[normalize-space(text())='" + keysToSend + "']"));
//				clickValidateXpath(driver, fetchMetadataVO, select, configVO);
//				WebElement searchok = driver
//						.findElement(By.xpath("//div[text()='Search']/following::button[text()='OK']"));
//				clickValidateXpath(driver, fetchMetadataVO, searchok, configVO);
//				Thread.sleep(10000);
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				Thread.sleep(15000);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//
//				String xpath = "//*[normalize-space(text())='param2']/following::input[1]" + ";"
//						+ "(//a[contains(@id,'legal')or 'LE'][1]//span/span[contains(text(),'Search')])[2]" + ";"
//						+ "(//span[text()='Name']/following::input[@type='text'])" + ";"
//						+ "//b[text()='Value']/following::div[normalize-space(text())='keysToSend']" + ";"
//						+ "//div[text()='Search']/following::button[text()='OK']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				log.info("Sucessfully Clicked dropdownValues" + scripNumber);
//				return;
//			}
//
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  dropdownValues" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Report")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				Thread.sleep(5000);
//				wait.until(ExpectedConditions.presenceOfElementLocated(
//						By.xpath("//*[normalize-space(text())='" + param2 + "']/following::input[1]")));
//				WebElement waittext = driver
//						.findElement(By.xpath("//*[normalize-space(text())='" + param2 + "']/following::input[1]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//				Thread.sleep(2000);
//				if (param2.equalsIgnoreCase("Procurement Business Unit")) {
//					WebElement search = driver.findElement(
//							By.xpath("//a[contains(@id,'PROCUREMENT')][1]//span/span[contains(text(),'Search')]"));
//					clickValidateXpath(driver, fetchMetadataVO, search, configVO);
//					WebElement values = driver
//							.findElement(By.xpath("(//span[text()='Name']/following::input[@type='text'])[1]"));
//					typeIntoValidxpath(driver, keysToSend, values, configVO, fetchMetadataVO);
//					//enter(driver, fetchMetadataVO, configVO);
//					WebElement select = driver.findElement(By
//							.xpath("//*[text()='Value']/following::div[normalize-space(text())='" + keysToSend + "']"));
//					clickValidateXpath(driver, fetchMetadataVO, select, configVO);
//					WebElement searchok = driver
//							.findElement(By.xpath("//div[text()='Search']/following::button[text()='OK']"));
//					clickValidateXpath(driver, fetchMetadataVO, searchok, configVO);
//					screenshot(driver, "", fetchMetadataVO, configVO);
//					String scripNumber = fetchMetadataVO.getScriptNumber();
//
//					String xpath = "//*[normalize-space(text())='param2']/following::input[1]" + ";"
//							+ "//a[contains(@id,'PROCUREMENT')][1]//span/span[contains(text(),'Search')]" + ";"
//							+ "(//span[text()='Name']/following::input[@type='text'])[1]" + ";"
//							+ "//*[text()='Value']/following::div[normalize-space(text())='keysToSend']" + ";"
//							+ "//div[text()='Search']/following::button[text()='OK']";
//					testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//					log.info("Sucessfully Clicked Report dropdownValues" + scripNumber);
//					return;
//				} else if (param2.equalsIgnoreCase("Ledger")) {
//					Thread.sleep(1000);
//					WebElement search = driver
//							.findElement(By.xpath("//a[contains(@id,'REQ')][1]//span/span[contains(text(),'Search')]"));
//					clickValidateXpath(driver, fetchMetadataVO, search, configVO);
//					WebElement values = driver
//							.findElement(By.xpath("(//span[text()='Name']/following::input[@type='text'])[2]"));
//					typeIntoValidxpath(driver, keysToSend, values, configVO, fetchMetadataVO);
//					//enter(driver, fetchMetadataVO, configVO);
//					WebElement select = driver.findElement(By.xpath(
//							"//*[text()='Value']/following::div[normalize-space(text())='" + keysToSend + "'][2]"));
//					clickValidateXpath(driver, fetchMetadataVO, select, configVO);
//					WebElement searchok = driver
//							.findElement(By.xpath("//div[text()='Search']/following::button[text()='OK'][2]"));
//					clickValidateXpath(driver, fetchMetadataVO, searchok, configVO);
//					screenshot(driver, "", fetchMetadataVO, configVO);
//					String scripNumber = fetchMetadataVO.getScriptNumber();
//
//					String xpath = "//*[normalize-space(text())='param2']/following::input[1]" + ";"
//							+ "//a[contains(@id,'REQ')][1]//span/span[contains(text(),'Search')]" + ";"
//							+ "(//span[text()='Name']/following::input[@type='text'])[2]" + ";"
//							+ "//*[text()='Value']/following::div[normalize-space(text())='keysToSend'][2]" + ";"
//							+ "//div[text()='Search']/following::button[text()='OK'][2]";
//					testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//					log.info("Sucessfully Clicked dropdownValues" + scripNumber);
//					return;
//				} else if (param2.equalsIgnoreCase("Request Name")) {
//					Thread.sleep(1000);
//					WebElement search = driver
//							.findElement(By.xpath("//a[contains(@id,'REQ')][1]//span/span[contains(text(),'Search')]"));
//					clickValidateXpath(driver, fetchMetadataVO, search, configVO);
//					WebElement values = driver
//							.findElement(By.xpath("(//span[text()='Name']/following::input[@type='text'])[2]"));
//					typeIntoValidxpath(driver, keysToSend, values, configVO, fetchMetadataVO);
//					//enter(driver, fetchMetadataVO, configVO);
//					WebElement select = driver.findElement(By.xpath(
//							"//*[text()='Value']/following::div[normalize-space(text())='" + keysToSend + "'][2]"));
//					clickValidateXpath(driver, fetchMetadataVO, select, configVO);
//					WebElement searchok = driver
//							.findElement(By.xpath("//div[text()='Search']/following::button[text()='OK'][2]"));
//					clickValidateXpath(driver, fetchMetadataVO, searchok, configVO);
//					screenshot(driver, "", fetchMetadataVO, configVO);
//					String scripNumber = fetchMetadataVO.getScriptNumber();
//
//					String xpath = "//*[normalize-space(text())='param2']/following::input[1]" + ";"
//							+ "//a[contains(@id,'REQ')][1]//span/span[contains(text(),'Search')]" + ";"
//							+ "(//span[text()='Name']/following::input[@type='text'])[2]" + ";"
//							+ "//*[text()='Value']/following::div[normalize-space(text())='keysToSend'][2]" + ";"
//							+ "//div[text()='Search']/following::button[text()='OK'][2]";
//					testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//					log.info("Sucessfully Clicked dropdownValues" + scripNumber);
//					return;
//				} else if (param2.equalsIgnoreCase("Requistion Business Unit")) {
//					Thread.sleep(1000);
//					WebElement search = driver
//							.findElement(By.xpath("//a[contains(@id,'REQ')][1]//span/span[contains(text(),'Search')]"));
//					clickValidateXpath(driver, fetchMetadataVO, search, configVO);
//					WebElement values = driver
//							.findElement(By.xpath("(//span[text()='Name']/following::input[@type='text'])[2]"));
//					typeIntoValidxpath(driver, keysToSend, values, configVO, fetchMetadataVO);
//					//enter(driver, fetchMetadataVO, configVO);
//					WebElement select = driver.findElement(By.xpath(
//							"//*[text()='Value']/following::div[normalize-space(text())='" + keysToSend + "'][2]"));
//					clickValidateXpath(driver, fetchMetadataVO, select, configVO);
//					WebElement searchok = driver
//							.findElement(By.xpath("//div[text()='Search']/following::button[text()='OK'][2]"));
//					clickValidateXpath(driver, fetchMetadataVO, searchok, configVO);
//					screenshot(driver, "", fetchMetadataVO, configVO);
//					String scripNumber = fetchMetadataVO.getScriptNumber();
//
//					String xpath = "//*[normalize-space(text())='param2']/following::input[1]" + ";"
//							+ "//a[contains(@id,'REQ')][1]//span/span[contains(text(),'Search')]" + ";"
//							+ "(//span[text()='Name']/following::input[@type='text'])[2]" + ";"
//							+ "//*[text()='Value']/following::div[normalize-space(text())='keysToSend'][2]" + ";"
//							+ "//div[text()='Search']/following::button[text()='OK'][2]";
//					testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//					log.info("Sucessfully Clicked Ledger dropdownValues" + scripNumber);
//					return;
//				} else if (param2.equalsIgnoreCase("Status")) {
//					WebElement search = driver.findElement(
//							By.xpath("//a[contains(@id,'STATUS')][1]//span/span[contains(text(),'Search')]"));
//					clickValidateXpath(driver, fetchMetadataVO, search, configVO);
//					WebElement values = driver
//							.findElement(By.xpath("(//span[text()='Name']/following::input[@type='text'])[3]"));
//					typeIntoValidxpath(driver, keysToSend, values, configVO, fetchMetadataVO);
//					//enter(driver, fetchMetadataVO, configVO);
//					WebElement select = driver.findElement(By.xpath(
//							"//*[text()='Value']/following::div[normalize-space(text())='" + keysToSend + "'][1]"));
//					clickValidateXpath(driver, fetchMetadataVO, select, configVO);
//					WebElement searchok = driver
//							.findElement(By.xpath("//div[text()='Search']/following::button[text()='OK'][3]"));
//					clickValidateXpath(driver, fetchMetadataVO, searchok, configVO);
//					screenshot(driver, "", fetchMetadataVO, configVO);
//					String scripNumber = fetchMetadataVO.getScriptNumber();
//
//					String xpath = "//*[normalize-space(text())='param2']/following::input[1]" + ";"
//							+ "//a[contains(@id,'STATUS')][1]//span/span[contains(text(),'Search')]" + ";"
//							+ "(//span[text()='Name']/following::input[@type='text'])[3]" + ";"
//							+ "//*[text()='Value']/following::div[normalize-space(text())='keysToSend'][1]" + ";"
//							+ "//div[text()='Search']/following::button[text()='OK'][3]";
//					testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//					log.info("Sucessfully Clicked Status dropdownValues" + scripNumber);
//					return;
//				} else if (param2.equalsIgnoreCase("Req. Business Unit") || param2.equalsIgnoreCase("Client BU")) {
//					WebElement search = driver.findElement(
//							By.xpath("//a[contains(@id,'paramsp')][1]//span/span[contains(text(),'Search')]"));
//					clickValidateXpath(driver, fetchMetadataVO, search, configVO);
//					WebElement values = driver
//							.findElement(By.xpath("(//span[text()='Name']/following::input[@type='text'])[1]"));
//					typeIntoValidxpath(driver, keysToSend, values, configVO, fetchMetadataVO);
//					//enter(driver, fetchMetadataVO, configVO);
//					WebElement select = driver.findElement(By
//							.xpath("//*[text()='Value']/following::div[normalize-space(text())='" + keysToSend + "']"));
//					clickValidateXpath(driver, fetchMetadataVO, select, configVO);
//					WebElement searchok = driver
//							.findElement(By.xpath("//div[text()='Search']/following::button[text()='OK']"));
//					clickValidateXpath(driver, fetchMetadataVO, searchok, configVO);
//					screenshot(driver, "", fetchMetadataVO, configVO);
//					String scripNumber = fetchMetadataVO.getScriptNumber();
//
//					String xpath = "//*[normalize-space(text())='param2']/following::input[1]" + ";"
//							+ "//a[contains(@id,'paramsp')][1]//span/span[contains(text(),'Search')]" + ";"
//							+ "(//span[text()='Name']/following::input[@type='text'])[1]" + ";"
//							+ "//*[text()='Value']/following::div[normalize-space(text())='keysToSend']" + ";"
//							+ "//div[text()='Search']/following::button[text()='OK']";
//					testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//					log.info("Sucessfully Clicked Req. Business Unit dropdownValues" + scripNumber);
//					return;
//				} else if (param2.equalsIgnoreCase("Legal Entity")) {
//					Thread.sleep(2000);
//					WebElement search = driver.findElement(
//							By.xpath("(//a[contains(@id,'paramsp')][1]//span/span[contains(text(),'Search')])[1]"));
//					clickValidateXpath(driver, fetchMetadataVO, search, configVO);
//					WebElement values = driver
//							.findElement(By.xpath("(//span[text()='Name']/following::input[@type='text'])[1]"));
//					typeIntoValidxpath(driver, keysToSend, values, configVO, fetchMetadataVO);
//					//enter(driver, fetchMetadataVO, configVO);
//					WebElement select = driver.findElement(By
//							.xpath("//*[text()='Value']/following::div[normalize-space(text())='" + keysToSend + "']"));
//					clickValidateXpath(driver, fetchMetadataVO, select, configVO);
//					WebElement searchok = driver
//							.findElement(By.xpath("//div[text()='Search']/following::button[text()='OK']"));
//					clickValidateXpath(driver, fetchMetadataVO, searchok, configVO);
//					screenshot(driver, "", fetchMetadataVO, configVO);
//					String scripNumber = fetchMetadataVO.getScriptNumber();
//
//					String xpath = "//*[normalize-space(text())='param2']/following::input[1]" + ";"
//							+ "(//a[contains(@id,'paramsp')][1]//span/span[contains(text(),'Search')])[1]" + ";"
//							+ "(//span[text()='Name']/following::input[@type='text'])[1]" + ";"
//							+ "//*[text()='Value']/following::div[normalize-space(text())='keysToSend']" + ";"
//							+ "//div[text()='Search']/following::button[text()='OK']";
//					testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//					log.info("Sucessfully Clicked Legal Entity dropdownValues" + scripNumber);
//					return;
//				} else if (param2.equalsIgnoreCase("Customer Name")) {
//					Thread.sleep(1000);
//					WebElement search = driver.findElement(
//							By.xpath("(//a[contains(@id,'CUSTOMER')][1]//span/span[contains(text(),'Search')])[1]"));
//					clickValidateXpath(driver, fetchMetadataVO, search, configVO);
//					WebElement values = driver
//							.findElement(By.xpath("(//span[text()='Name']/following::input[@type='text'])[2]"));
//					typeIntoValidxpath(driver, keysToSend, values, configVO, fetchMetadataVO);
//					//enter(driver, fetchMetadataVO, configVO);
//					WebElement select = driver.findElement(By.xpath(
//							"//*[text()='Value']/following::div[normalize-space(text())='" + keysToSend + "'][2]"));
//					clickValidateXpath(driver, fetchMetadataVO, select, configVO);
//					WebElement searchok = driver
//							.findElement(By.xpath("//div[text()='Search']/following::button[text()='OK'][2]"));
//					clickValidateXpath(driver, fetchMetadataVO, searchok, configVO);
//					screenshot(driver, "", fetchMetadataVO, configVO);
//					String scripNumber = fetchMetadataVO.getScriptNumber();
//
//					String xpath = "//*[normalize-space(text())='param2']/following::input[1]" + ";"
//							+ "(//a[contains(@id,'CUSTOMER')][1]//span/span[contains(text(),'Search')])[1]" + ";"
//							+ "(//span[text()='Name']/following::input[@type='text'])[2]" + ";"
//							+ "//*[text()='Value']/following::div[normalize-space(text())='keysToSend'][2]" + ";"
//							+ "//div[text()='Search']/following::button[text()='OK'][2]";
//					testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//					log.info("Sucessfully Clicked Customer Name dropdownValues" + scripNumber);
//					return;
//				} else if (param2.equalsIgnoreCase("Business Unit")) {
//					if (keysToSend.equalsIgnoreCase("All")) {
//						WebElement select = driver.findElement(By.xpath("//span[text()='" + param2
//								+ "']/following::div[normalize-space(text())='" + keysToSend + "'][1]"));
//						clickValidateXpath(driver, fetchMetadataVO, select, configVO);
//						String scripNumber = fetchMetadataVO.getScriptNumber();
//
//						String xpath = "//*[normalize-space(text())='param2']/following::input[1]" + ";"
//								+ "//span[text()='" + param2
//								+ "']/following::div[normalize-space(text())='keysToSend'][1]";
//						testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//						log.info("Sucessfully Clicked Business Unit dropdownValues" + scripNumber);
//						return;
//					} else {
//						WebElement search = driver.findElement(
//								By.xpath("//a[contains(@id,'BU')][1]//span/span[contains(text(),'Search')]"));
//						clickValidateXpath(driver, fetchMetadataVO, search, configVO);
//						WebElement values = driver
//								.findElement(By.xpath("(//span[text()='Name']/following::input[@type='text'])[1]"));
//						typeIntoValidxpath(driver, keysToSend, values, configVO, fetchMetadataVO);
//						//enter(driver, fetchMetadataVO, configVO);
//						WebElement select = driver.findElement(By.xpath(
//								"//*[text()='Value']/following::div[normalize-space(text())='" + keysToSend + "']"));
//						clickValidateXpath(driver, fetchMetadataVO, select, configVO);
//						WebElement searchok = driver
//								.findElement(By.xpath("//div[text()='Search']/following::button[text()='OK']"));
//						clickValidateXpath(driver, fetchMetadataVO, searchok, configVO);
//						Thread.sleep(10000);
//						screenshot(driver, "", fetchMetadataVO, configVO);
//						Thread.sleep(3000);
//						String scripNumber = fetchMetadataVO.getScriptNumber();
//
//						String xpath = "//*[normalize-space(text())='param2']/following::input[1]" + ";"
//								+ "//a[contains(@id,'BU')][1]//span/span[contains(text(),'Search')]" + ";"
//								+ "(//span[text()='Name']/following::input[@type='text'])[1]" + ";"
//								+ "//*[text()='Value']/following::div[normalize-space(text())='keysToSend']" + ";"
//								+ "//div[text()='Search']/following::button[text()='OK']";
//						testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//						log.info("Sucessfully Clicked dropdownValues" + scripNumber);
//						return;
//					}
//				} else if (param2.equalsIgnoreCase("Supplier Name")) {
//					if (keysToSend.equalsIgnoreCase("All")) {
//						WebElement select = driver.findElement(By.xpath("//span[text()='" + param2
//								+ "']/following::div[normalize-space(text())='" + keysToSend + "'][1]"));
//						clickValidateXpath(driver, fetchMetadataVO, select, configVO);
//						String scripNumber = fetchMetadataVO.getScriptNumber();
//
//						String xpath = "//*[normalize-space(text())='param2']/following::input[1]" + ";"
//								+ "//span[text()='param2']/following::div[normalize-space(text())='keysToSend'][1]";
//						testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//						log.info("Sucessfully Clicked Supplier Name dropdownValues" + scripNumber);
//						return;
//					} else {
//						WebElement search = driver.findElement(
//								By.xpath("//a[contains(@id,'SUPPLIER')][1]//span/span[contains(text(),'Search')]"));
//						clickValidateXpath(driver, fetchMetadataVO, search, configVO);
//						WebElement values = driver
//								.findElement(By.xpath("(//span[text()='Name']/following::input[@type='text'])[1]"));
//						typeIntoValidxpath(driver, keysToSend, values, configVO, fetchMetadataVO);
//						//enter(driver, fetchMetadataVO, configVO);
//						WebElement select = driver.findElement(
//								By.xpath("//*[normalize-space(text())='Value']/following::div[normalize-space(text())='"
//										+ keysToSend + "']"));
//						clickValidateXpath(driver, fetchMetadataVO, select, configVO);
//						WebElement searchok = driver
//								.findElement(By.xpath("//div[text()='Search']/following::button[text()='OK']"));
//						clickValidateXpath(driver, fetchMetadataVO, searchok, configVO);
//						Thread.sleep(10000);
//						screenshot(driver, "", fetchMetadataVO, configVO);
//						Thread.sleep(3000);
//						String scripNumber = fetchMetadataVO.getScriptNumber();
//
//						String xpath = "//*[normalize-space(text())='param2']/following::input[1]" + ";"
//								+ "//a[contains(@id,'SUPPLIER')][1]//span/span[contains(text(),'Search')]" + ";"
//								+ "(//span[text()='Name']/following::input[@type='text'])[1]" + ";"
//								+ "//*[normalize-space(text())='Value']/following::div[normalize-space(text())='keysToSend']"
//								+ ";" + "//div[text()='Search']/following::button[text()='OK']";
//						testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//						log.info("Sucessfully Clicked dropdownValues" + scripNumber);
//						return;
//					}
//				}
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  dropdownValues" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Basic Options") && param2.equalsIgnoreCase("Ledger")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
//						"//*[normalize-space(text())='" + param1 + "']/following::label[normalize-space(text())='"
//								+ param2 + "']/following::a[contains(@title,'" + param2 + "')]")));
//				WebElement waittext = driver
//						.findElement(By.xpath("//*[normalize-space(text())='" + param1 + "']/following::label[text()='"
//								+ param2 + "']/following::a[contains(@title,'" + param2 + "')]"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittext).build().perform();
//				try {
//					actions.click(waittext).build().perform();
//					Thread.sleep(6000);
//					WebElement popup1 = driver.findElement(By.xpath("//div[@class='AFDetectExpansion']"));
//					WebElement search = driver.findElement(
//							By.xpath("//div[@class='AFDetectExpansion']/following::a[contains(text(),'Search')][1]"));
//					actions.moveToElement(search).build().perform();
//					search.click();
//					Thread.sleep(10000);
//					wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
//							"//div[contains(@id,'PopupId::content')]//*[text()='Search']/following::*[normalize-space(text())='"
//									+ param2 + "']/following::input[1]")));
//					WebElement searchResult = driver.findElement(By.xpath(
//							"//div[contains(@id,'PopupId::content')]//*[text()='Search']/following::*[normalize-space(text())='"
//									+ param2 + "']/following::input[1]"));
//					typeIntoValidxpath(driver, keysToSend, searchResult, configVO, fetchMetadataVO);
//					//enter(driver, fetchMetadataVO, configVO);
//					Thread.sleep(5000);
//					WebElement text = driver
//							.findElement(By.xpath("(//span[contains(text(),'" + keysToSend + "')])[1]"));
//					text.click();
//					screenshot(driver, "", fetchMetadataVO, configVO);
//					WebElement button = driver
//							.findElement(By.xpath("//*[text()='Search']/following::*[normalize-space(text())='" + param2
//									+ "']/following::*[text()='OK'][1]"));
//					button.click();
//					String scripNumber = fetchMetadataVO.getScriptNumber();
//
//					String xpath = "//*[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/following::a[contains(@title,'param2')]"
//							+ ";" + "//div[@class='AFDetectExpansion']" + ";"
//							+ "//div[@class='AFDetectExpansion']/following::a[contains(text(),'Search')][1]" + ";"
//							+ "//div[contains(@id,'PopupId::content')]//*[text()='Search']/following::*[normalize-space(text())='param2']/following::input[1]"
//							+ ";" + "(//span[contains(text(),'keysToSend')])[1]" + ";"
//							+ "//*[text()='Search']/following::*[normalize-space(text())='param2']/following::*[text()='OK'][1]";
//					testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//					log.info("Sucessfully Clicked Basic Options or Ledger dropdownValues" + scripNumber);
//				} catch (Exception e) {
//					String scripNumber = fetchMetadataVO.getScriptNumber();
//					log.error("Failed during Basic Options or Ledger dropdownValues" + scripNumber);
//
//					for (int i = 0; i <= 2; i++) {
//						try {
//							actions.click(waittext).build().perform();
//							break;
//						} finally {
//
//						}
//					}
//				}
//				try {
//					Thread.sleep(6000);
//					WebElement popup1 = driver.findElement(By.xpath("//div[@class='AFDetectExpansion']"));
//					WebElement search = driver.findElement(
//							By.xpath("//div[@class='AFDetectExpansion']/following::a[contains(text(),'Search')][1]"));
//					actions.moveToElement(search).build().perform();
//					search.click();
//					Thread.sleep(10000);
//					wait.until(ExpectedConditions.presenceOfElementLocated(
//							By.xpath("//div[contains(@id,'PopupId::content')]//*[text()='Search']/following::*[text()='"
//									+ param2 + "']/following::input[1]")));
//					WebElement searchResult = driver.findElement(
//							By.xpath("//div[contains(@id,'PopupId::content')]//*[text()='Search']/following::*[text()='"
//									+ param2 + "']/following::input[1]"));
//					typeIntoValidxpath(driver, keysToSend, searchResult, configVO, fetchMetadataVO);
//					//enter(driver, fetchMetadataVO, configVO);
//					Thread.sleep(5000);
//					WebElement text = driver.findElement(By.xpath("//span[text()='" + param2
//							+ "']/following::span[contains(text(),'" + keysToSend + "')][1]"));
//					text.click();
//					screenshot(driver, "", fetchMetadataVO, configVO);
//					WebElement button = driver
//							.findElement(By.xpath("//*[text()='Search']/following::*[normalize-space(text())='" + param2
//									+ "']/following::*[text()='OK'][1]"));
//					button.click();
//					String scripNumber = fetchMetadataVO.getScriptNumber();
//					String xpath = "//*[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/following::a[contains(@title,'param2')]"
//							+ ";" + "//div[@class='AFDetectExpansion']/following::a[contains(text(),'Search')][1]" + ";"
//							+ "//div[contains(@id,'PopupId::content')]//*[text()='Search']/following::*[text()='param2']/following::input[1]"
//							+ ";" + "//span[text()='param2']/following::span[contains(text(),'keysToSend')][1]" + ";"
//							+ "//*[text()='Search']/following::*[normalize-space(text())='param2']/following::*[text()='OK'][1]";
//
//					testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//					log.info("Sucessfully Clicked dropdownValues" + scripNumber);
//				} catch (Exception e) {
//					String scripNumber = fetchMetadataVO.getScriptNumber();
//					log.error("Failed during  dropdownValues" + scripNumber);
//
//					for (int i = 0; i <= 2; i++) {
//						try {
//							actions.click(waittext).build().perform();
//							break;
//						} finally {
//							Thread.sleep(4000);
//							WebElement popup1 = driver.findElement(By.xpath("//div[@class='AFDetectExpansion']"));
//							WebElement search = driver.findElement(By.xpath(
//									"//div[@class='AFDetectExpansion']/following::a[contains(text(),'Search')][1]"));
//							actions.moveToElement(search).build().perform();
//							search.click();
//							Thread.sleep(10000);
//							wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
//									"//div[contains(@id,'PopupId::content')]//*[text()='Search']/following::*[normalize-space(text())='"
//											+ param2 + "']/following::input[1]")));
//							WebElement searchResult = driver.findElement(By.xpath(
//									"//div[contains(@id,'PopupId::content')]//*[text()='Search']/following::*[normalize-space(text())='"
//											+ param2 + "']/following::input[1]"));
//							typeIntoValidxpath(driver, keysToSend, searchResult, configVO, fetchMetadataVO);
//							//enter(driver, fetchMetadataVO, configVO);
//							Thread.sleep(5000);
//							WebElement text = driver
//									.findElement(By.xpath("(//span[contains(text(),'" + keysToSend + "')])[1]"));
//							text.click();
//							screenshot(driver, "", fetchMetadataVO, configVO);
//							WebElement button = driver.findElement(By.xpath("//*[text()='Search']/following::*[text()='"
//									+ param2 + "']/following::*[text()='OK'][1]"));
//							button.click();
//
//							String xpath = "//div[@class='AFDetectExpansion']/following::a[contains(text(),'Search')][1]"
//									+ ";" + "(//span[contains(text(),'keysToSend')])[1]" + ";"
//									+ "//*[text()='Search']/following::*[text()='param2']/following::*[text()='OK'][1]";
//							testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//
//						}
//					}
//
//				}
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  dropdownValues" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1
//					+ "']/following::label[normalize-space(text())='" + param2 + "']/following::a[1]")));
//			WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
//					+ "']/following::label[normalize-space(text())='" + param2 + "']/following::a[1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			try {
//				actions.click(waittext).build().perform();
//				Thread.sleep(10000);
//				WebElement popup1 = driver.findElement(By.xpath("//div[contains(@id,'suggestions-popup')]"));
//		//		dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, configVO);
//				actions.release();
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//
//				String xpath = "//*[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/following::a[1]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				log.info("Sucessfully Clicked dropdownValues" + scripNumber);
//			} catch (Exception ex) {
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.error("Failed during  dropdownValues" + scripNumber);
//
//				try {
//					try {
//						WebElement popup1 = driver.findElement(By.xpath("//div[@class='AFDetectExpansion']"));
//				//		dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, configVO);
//						actions.release();
//						screenshot(driver, "", fetchMetadataVO, configVO);
//						// String xpath="//div[@class='AFDetectExpansion']";
//						String xpath = "//*[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/following::a[1]";
//						testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//					} catch (Exception ex1) {
//						for (int i = 0; i <= 2; i++) {
//							actions.click(waittext).build().perform();
//							break;
//						}
//						Thread.sleep(3000);
//						WebElement popup1 = driver.findElement(By.xpath("//div[contains(@id,'suggestions-popup')]"));
//					//	dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, configVO);
//						actions.release();
//						screenshot(driver, "", fetchMetadataVO, configVO);
//						String xpath = "//*[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/following::a[1]";
//						testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//					}
//				} catch (Exception ex2) {
//					WebElement popup1 = driver.findElement(By.xpath("//div[@class='AFDetectExpansion']"));
//				//	dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, configVO);
//					actions.release();
//					screenshot(driver, "", fetchMetadataVO, configVO);
//					String xpath = "//*[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/following::a[1]";
//					testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				}
//			}
//			return;
//		} catch (Exception exe) {
//			System.out.println(exe);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  dropdownValues" + scripNumber);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1
//					+ "']/following::label[normalize-space(text())='" + param2 + "']/following::a[1]")));
//			WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
//					+ "']/following::label[text()='" + param2 + "']/following::a[1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			try {
//				actions.clickAndHold(waittext).build().perform();
//				Thread.sleep(6000);
//				WebElement popup1 = driver.findElement(By.xpath("//div[contains(@id,'dropdownPopup::content')]"));
//
//			//	dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, configVO);
//				actions.release();
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//
//				String xpath = "//*[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/following::a[1]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				log.info("Sucessfully Clicked dropdownValues" + scripNumber);
//			} catch (Exception e) {
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.error("Failed during  dropdownValues" + scripNumber);
//				for (int i = 0; i <= 2; i++) {
//					try {
//						actions.click(waittext).build().perform();
//						break;
//					} finally {
//					//	dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, configVO);
//						actions.release();
//						screenshot(driver, "", fetchMetadataVO, configVO);
//						String xpath = "//*[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/following::a[1]";
//						testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//					}
//				}
//
//			}
//			try {
//				actions.click(waittext).build().perform();
//				Thread.sleep(6000);
//				WebElement popup1 = driver.findElement(By.xpath("//div[contains(@id,'dropdownPopup::content')][1]"));
//			//	dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, configVO);
//				actions.release();
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//
//				String xpath = "//div[contains(@id,'dropdownPopup::content')][1]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				log.info("Sucessfully Clicked dropdownValues" + scripNumber);
//			} catch (Exception e) {
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				log.error("Failed during  dropdownValues" + scripNumber);
//				for (int i = 0; i <= 2; i++) {
//					try {
//						actions.click(waittext).build().perform();
//						break;
//					} finally {
//					///	dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, configVO);
//						screenshot(driver, "", fetchMetadataVO, configVO);
//						String xpath = "//div[contains(@id,'dropdownPopup::content')][1]";
//						testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//					}
//				}
//
//			}
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  dropdownValues" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//label[normalize-space(text())='" + param1
//					+ "']/following::*[normalize-space(text())='" + param2 + "']/following::a[1]")));
//			WebElement waittext = driver.findElement(By.xpath("//label[normalize-space(text())='" + param1
//					+ "']/following::*[normalize-space(text())='" + param2 + "']/following::a[1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			//dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, configVO);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//
//			String xpath = "//label[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']/following::a[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			log.info("Sucessfully Clicked dropdownValues" + scripNumber);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  dropdownValues" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(
//					By.xpath("(//div[contains(@id,'popup-container')]//*[normalize-space(text())='" + param1
//							+ "']/following::*[normalize-space(text())=\"" + param2 + "\"]/following::a)[1]")));
//			WebElement waittext = driver.findElement(By.xpath("(//div[contains(@id,'popup-container')]//*[text()='"
//					+ param1 + "']/following::*[normalize-space(text())=\"" + param2 + "\"]/following::a)[1]"));
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//		//	dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, configVO);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//
//			String xpath = "(//div[contains(@id,'popup-container')]//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']/following::a)[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			log.info("Sucessfully Clicked dropdownValues" + scripNumber);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  dropdownValues" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())='" + param1
//					+ "']/following::*[normalize-space(text())=\"" + param2 + "\"]/following::a)[1]")));
//			WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())='" + param1
//					+ "']/following::*[normalize-space(text())=\"" + param2 + "\"]/following::a)[1]"));
//			clickValidateXpath(driver, fetchMetadataVO, waittext, configVO);
//			//dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, configVO);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//
//			String xpath = "(//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']/following::a)[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			log.info("Sucessfully Clicked dropdownValues" + scripNumber);
//			return;
//		} catch (Exception e) {
//			System.out.println(e);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked dropdownValues" + scripNumber);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(text(),'Search')]")));
//			WebElement search = driver.findElement(By.xpath("//a[contains(text(),'Search')]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(search).build().perform();
//			search.click();
//			Thread.sleep(2000);
//			wait.until(ExpectedConditions.presenceOfElementLocated(
//					By.xpath("//*[normalize-space(text())='Search']/following::*[normalize-space(text())='" + param2
//							+ "']/following::input[1]")));
//			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(
//					"//*[normalize-space(text())='Search']/following::*[normalize-space(text())='" + param2 + "']"),
//					param2));
//			WebElement searchResult = driver
//					.findElement(By.xpath("//*[normalize-space(text())='Search']/following::*[normalize-space(text())='"
//							+ param2 + "']/following::input[1]"));
//			typeIntoValidxpath(driver, keysToSend, searchResult, configVO, fetchMetadataVO);
//			if (keysToSend != null) {
//				//enter(driver, fetchMetadataVO, configVO);
//				Thread.sleep(5000);
//				WebElement text = driver
//						.findElement(By.xpath("(//span[normalize-space(text())='" + keysToSend + "'])[1]"));
//				text.click();
//			}
//			WebElement button = driver
//					.findElement(By.xpath("//*[normalize-space(text())='Search']/following::*[normalize-space(text())='"
//							+ param2 + "']/following::*[text()='K'][1]"));
//			button.click();
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//
//			String xpath = "//a[contains(text(),'Search')]" + ";"
//					+ "//*[normalize-space(text())='Search']/following::*[normalize-space(text())='param2']/following::input[1]"
//					+ ";" + "(//span[normalize-space(text())='keysToSend'])[1]" + ";"
//					+ "//*[normalize-space(text())='Search']/following::*[normalize-space(text())='param2']/following::*[text()='K'][1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			log.info("Sucessfully Clicked dropdownValues" + scripNumber);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  dropdownValues" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
//					"//*[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::input[1]")));
//			WebElement searchResult = driver.findElement(By.xpath(
//					"//*[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::input[1]"));
//			typeIntoValidxpath(driver, keysToSend, searchResult, configVO, fetchMetadataVO);
//			//enter(driver, fetchMetadataVO, configVO);
//			Thread.sleep(5000);
//			WebElement text = driver.findElement(By.xpath("//span[normalize-space(text())='" + keysToSend + "']"));
//			text.click();
//			Thread.sleep(1000);
//			WebElement button = driver.findElement(By.xpath(
//					"//*[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::*[text()='OK'][1]"));
//			button.click();
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//
//			String xpath = "//*[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::input[1]"
//					+ ";" + "//span[normalize-space(text())='keysToSend']" + ";"
//					+ "//*[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::*[text()='OK'][1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			log.info("Sucessfully Clicked dropdownValues" + scripNumber);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  dropdownValues" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebElement button = driver
//					.findElement(By.xpath("//*[normalize-space(text())='Search']/following::*[normalize-space(text())='"
//							+ param2 + "']/following::*[normalize-space(text())='OK'][1]"));
//			button.click();
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//
//			String xpath = "//*[normalize-space(text())='Search']/following::*[normalize-space(text())='param2']/following::*[normalize-space(text())='OK'][1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			log.info("Sucessfully Clicked dropdownValues" + scripNumber);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//
//			log.error("Failed during  dropdownValues" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[contains(text(),'" + param1
//					+ "')]/following::label[normalize-space(text())='" + keysToSend + "']/following::input)[1]")));
//			Thread.sleep(1000);
//			wait.until(
//					ExpectedConditions.textToBePresentInElementLocated(
//							By.xpath("//h1[contains(text(),'" + param1
//									+ "')]/following::label[normalize-space(text())='" + keysToSend + "']"),
//							keysToSend));
//			WebElement waittill = driver.findElement(By.xpath("//h1[contains(text(),'" + param1
//					+ "')]/following::label[normalize-space(text())='" + keysToSend + "']/following::input[1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittill).build().perform();
//			typeIntoValidxpath(driver, keysToSend, waittill, configVO, fetchMetadataVO);
//			Thread.sleep(500);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//
//			String xpath = "(//h1[contains(text(),'param1')]/following::label[normalize-space(text())='keysToSend']/following::input)[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			log.info("Sucessfully Clicked dropdownValues" + scripNumber);
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  dropdownValues" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(@id,'" + param1 + "')]")));
//			Thread.sleep(1000);
////wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//a[contains(@id,'drop')]"), keysToSend));
//			WebElement waittill = driver.findElement(By.xpath("//a[contains(@id,'" + param1 + "')]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittill).build().perform();
//			clickValidateXpath(driver, fetchMetadataVO, waittill, configVO);
//		//	dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, configVO);
//			Thread.sleep(500);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//
//			String xpath = "//a[contains(@id,'param1')]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			log.info("Sucessfully Clicked dropdownValues" + scripNumber);
//		} catch (Exception e) {
//			System.out.println(e);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  dropdownValues" + scripNumber);
//			screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, configVO);
//			throw e;
//		}
//	}
//
//	private void clickValidateXpath(WebDriver driver, TestRunExecutionVO fetchMetadataVO, WebElement waittext,
//			ConfigVO configVO) {
//		try {
//			JavascriptExecutor js = (JavascriptExecutor) driver;
//			js.executeScript("arguments[0].click();", waittext);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked clickValidateXpath" + scripNumber);
//			// waittext.click();
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  clickValidateXpath" + scripNumber);
//			e.printStackTrace();
//		}
//	}
//
//	public void clickFilter(WebDriver driver, String param1, String param2, TestRunExecutionVO fetchMetadataVO,
//			ConfigVO configVO) throws Exception {
//		try {
//			WebElement waittill = driver.findElement(By.xpath("//img[@title='" + param1 + "']/following::*[text()='"
//					+ param2 + "']/preceding::input[@type='text'])[3]"));
//
//			waittill.click();
//			screenshot(driver, "", fetchMetadataVO, configVO);
//
//			return;
//
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  clickFilter" + scripNumber);
//			screenshotFail(driver, "Failed during clickExpand Method", fetchMetadataVO, configVO);
//			System.out.println("(//img[@title='" + param1 + "']/following::*[text()='" + param2
//					+ "']/preceding::input[@type='text'])[3]");
//		}
//		try {
//			WebElement waittill = driver.findElement(By.xpath("//img[@title='" + param1 + "'][1]"));
//
//			waittill.click();
//			Thread.sleep(2000);
//			WebElement waittill1 = driver.findElement(By.xpath("(//img[@title='" + param1 + "']/following::*[text()='"
//					+ param2 + "']/preceding::input[@type='text'])[3]"));
//			waittill1.click();
//			Thread.sleep(2000);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  clickFilter" + scripNumber);
//			screenshotFail(driver, "Failed during clickExpand Method", fetchMetadataVO, configVO);
//			System.out.println("//img[@title='" + param1 + "'][1]");
//			throw e;
//		}
//	}
//
//	public String password(WebDriver driver, String inputParam, String keysToSend, ConfigVO configVO,
//			TestRunExecutionVO fetchMetadataVO) {
//		try {
//			WebElement waittill = driver.findElement(By.xpath("//*[contains(@placeholder,'" + inputParam + "')]"));
//			typeIntoValidxpath(driver, keysToSend, waittill, configVO, fetchMetadataVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked password" + scripNumber);
//			return keysToSend;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  password" + scripNumber);
//			screenshotFail(driver, "Failed during clearAndType Method", fetchMetadataVO, configVO);
//			System.out.println(e);
//			throw e;
//		}
//	}
//
//	public void typeIntoValidxpath(WebDriver driver, String keysToSend, WebElement waittill,
//			ConfigVO configVO, TestRunExecutionVO fetchMetadataVO) {
//		try {
//			waittill.clear();
//			waittill.click();
//			JavascriptExecutor jse = (JavascriptExecutor) driver;
//			jse.executeScript("arguments[0].value='" + keysToSend + "';", waittill);
//			log.info("clear and typed the given Data");
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked typeIntoValidxpath" + scripNumber);
//
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  typeIntoValidxpath" + scripNumber);
//			e.printStackTrace();
//		}
//	}
//
//	public void clearMethod(WebDriver driver, WebElement waittill) {
//		WebDriverWait wait = new WebDriverWait(driver, 60);
//		wait.until(ExpectedConditions.elementToBeClickable(waittill));
//		waittill.click();
//		waittill.clear();
//		log.info("clear and typed the given Data");
//	}
//
//	public void moveToElement(WebDriver driver, String inputParam, TestRunExecutionVO fetchMetadataVO,
//			ConfigVO configVO) {
//		WebElement waittill = driver.findElement(By.xpath("//*[normalize-space(text())='" + inputParam + "'][1]"));
//		Actions actions = new Actions(driver);
//		actions.moveToElement(waittill).build().perform();
//		String scripNumber = fetchMetadataVO.getScriptNumber();
//		log.info("Sucessfully Clicked moveToElement" + scripNumber);
//	}
//
//	public void scrollUsingElement(WebDriver driver, String inputParam, TestRunExecutionVO fetchMetadataVO,
//			ConfigVO configVO) {
//		try {
//			Thread.sleep(2000);
//			WebElement waittill = driver
//					.findElement(By.xpath("//span[normalize-space(text())='" + inputParam + "'][1]"));
//			// ((JavascriptExecutor)driver).executeScript("document.body.style.zoom='50%';");
//			scrollMethod(driver, configVO, waittill, fetchMetadataVO);
//			// ((JavascriptExecutor)driver).executeScript("document.body.style.zoom='100%';");
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			String xpath = "//span[normalize-space(text())='inputParam'][1]";
//			testRunScriptExecService.saveXpathParams(inputParam, "", scripNumber, xpath);
//			log.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//
//			log.error("Failed during  scrollUsingElement" + scripNumber);
//			System.out.println(inputParam);
//		}
//		try {
//			WebElement waittill = driver.findElement(By.xpath("//a[normalize-space(text())='" + inputParam + "']"));
//			scrollMethod(driver, configVO, waittill, fetchMetadataVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			String xpath = "//a[normalize-space(text())='inputParam']";
//			testRunScriptExecService.saveXpathParams(inputParam, "", scripNumber, xpath);
//			log.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  scrollUsingElement" + scripNumber);
//			System.out.println(inputParam);
//		}
//		try {
//			WebElement waittill = driver.findElement(By.xpath("//h1[normalize-space(text())='" + inputParam + "']"));
//			scrollMethod(driver, configVO, waittill, fetchMetadataVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			String xpath = "//h1[normalize-space(text())='inputParam']";
//			testRunScriptExecService.saveXpathParams(inputParam, "", scripNumber, xpath);
//			log.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Field during scrollUsingElement" + scripNumber);
//			System.out.println(inputParam);
//		}
//		try {
//			WebElement waittill = driver.findElement(By.xpath("(//h2[normalize-space(text())='" + inputParam + "'])"));
//			scrollMethod(driver, configVO, waittill, fetchMetadataVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			String xpath = "(//h2[normalize-space(text())='inputParam'])";
//			testRunScriptExecService.saveXpathParams(inputParam, "", scripNumber, xpath);
//			log.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  scrollUsingElement" + scripNumber);
//			System.out.println(inputParam);
//		}
//		try {
//			WebElement waittill = driver
//					.findElement(By.xpath("(//h3[normalize-space(text())='" + inputParam + "'])[2]"));
//			scrollMethod(driver, configVO, waittill, fetchMetadataVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			String xpath = "(//h3[normalize-space(text())='inputParam'])[2]";
//			testRunScriptExecService.saveXpathParams(inputParam, "", scripNumber, xpath);
//			log.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
//			return;
//		} catch (Exception e) {
//			System.out.println(inputParam);
//		}
//		try {
//			WebElement waittill = driver.findElement(By.xpath("//td[normalize-space(text())='" + inputParam + "']"));
//			scrollMethod(driver, configVO, waittill, fetchMetadataVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			String xpath = "//td[normalize-space(text())='inputParam']";
//			testRunScriptExecService.saveXpathParams(inputParam, "", scripNumber, xpath);
//			log.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
//			return;
//		} catch (Exception e) {
//			System.out.println(inputParam);
//		}
//		try {
//			WebElement waittill = driver.findElement(By.xpath("//div[contains(text(),'" + inputParam + "')]"));
//			scrollMethod(driver, configVO, waittill, fetchMetadataVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			String xpath = "//div[contains(text(),'inputParam')]";
//			testRunScriptExecService.saveXpathParams(inputParam, "", scripNumber, xpath);
//			log.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  scrollUsingElement" + scripNumber);
//			System.out.println(inputParam);
//		}
//		try {
//			WebElement waittill = driver.findElement(By.xpath("(//table[@summary='" + inputParam + "']//td//a)[1]"));
//			scrollMethod(driver, configVO, waittill, fetchMetadataVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			String xpath = "(//table[@summary='inputParam']//td//a)[1]";
//			testRunScriptExecService.saveXpathParams(inputParam, "", scripNumber, xpath);
//			log.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  scrollUsingElement" + scripNumber);
//			System.out.println(inputParam);
//		}
//		try {
//			WebElement waittill = driver.findElement(
//					By.xpath("(//label[normalize-space(text())=\"" + inputParam + "\"]/following::input)[1]"));
//			scrollMethod(driver, configVO, waittill, fetchMetadataVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			String xpath = "(//label[normalize-space(text())='inputParam']/following::input)[1]";
//			testRunScriptExecService.saveXpathParams(inputParam, "", scripNumber, xpath);
//			log.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
//			return;
//		} catch (Exception e) {
//			System.out.println(inputParam);
//		}
//		try {
//			WebElement waittill = driver.findElement(By.xpath("//a[contains(@id,'" + inputParam + "')]"));
//			scrollMethod(driver, configVO, waittill, fetchMetadataVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			String xpath = "//a[contains(@id,'inputParam')]";
//			testRunScriptExecService.saveXpathParams(inputParam, "", scripNumber, xpath);
//			log.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  scrollUsingElement" + scripNumber);
//			System.out.println(inputParam);
//		}
//		try {
//			WebElement waittill = driver.findElement(By.xpath("//li[normalize-space(text())='" + inputParam + "']"));
//			scrollMethod(driver, configVO, waittill, fetchMetadataVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			String xpath = "//li[normalize-space(text())='inputParam']";
//			testRunScriptExecService.saveXpathParams(inputParam, "", scripNumber, xpath);
//			log.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  scrollUsingElement" + scripNumber);
//			System.out.println(inputParam);
//		}
//		try {
//			WebElement waittill = driver
//					.findElement(By.xpath("//label[normalize-space(text())=\"" + inputParam + "\"]"));
//			scrollMethod(driver, configVO, waittill, fetchMetadataVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			String xpath = "//label[normalize-space(text())='inputParam']";
//			testRunScriptExecService.saveXpathParams(inputParam, "", scripNumber, xpath);
//			log.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  scrollUsingElement" + scripNumber);
//			System.out.println(inputParam);
//		}
//		try {
//			WebElement waittill = driver
//					.findElement(By.xpath("//button[normalize-space(text())='" + inputParam + "']"));
//			scrollMethod(driver, configVO, waittill, fetchMetadataVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			String xpath = "//button[normalize-space(text())='inputParam']";
//			testRunScriptExecService.saveXpathParams(inputParam, "", scripNumber, xpath);
//			log.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  scrollUsingElement" + scripNumber);
//			System.out.println(inputParam);
//		}
//		try {
//			WebElement waittill = driver.findElement(By.xpath("//img[@title='" + inputParam + "']"));
//			scrollMethod(driver, configVO, waittill, fetchMetadataVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			String xpath = "//img[@title='inputParam']";
//			testRunScriptExecService.saveXpathParams(inputParam, "", scripNumber, xpath);
//			log.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  scrollUsingElement" + scripNumber);
//			System.out.println(inputParam);
//		}
//		try {
//			WebElement waittill = driver.findElement(By.xpath("(//*[@title='" + inputParam + "'])[1]"));
//			scrollMethod(driver, configVO, waittill, fetchMetadataVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			String xpath = "(//*[@title='inputParam'])[1]";
//			testRunScriptExecService.saveXpathParams(inputParam, "", scripNumber, xpath);
//			log.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  scrollUsingElement" + scripNumber);
//			screenshotFail(driver, "Failed during scrollUsingElement Method", fetchMetadataVO, configVO);
//			System.out.println(inputParam);
//			e.printStackTrace();
//			throw e;
//		}
//	}
//
//	private void scrollMethod(WebDriver driver, ConfigVO configVO, WebElement waittill,
//			TestRunExecutionVO fetchMetadataVO) {
//		configVO.getWaitTime();
//		WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//		// WebElement elements =
//		// wait.until(ExpectedConditions.elementToBeClickable(waittill));
//		WebElement element = waittill;
//		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
//		screenshot(driver, "", fetchMetadataVO, configVO);
//		String scripNumber = fetchMetadataVO.getScriptNumber();
//		log.info("Sucessfully Clicked scrollMethod" + scripNumber);
//	}
//
//	public ArrayList<String> tab(WebDriver driver, TestRunExecutionVO fetchMetadataVO, ConfigVO configVO,String actionName) throws Exception {
//		SeleniumEbsMapping selEbsMapping=ebsMappingRepo.findBySeleniumActionName(actionName);
//		com.winfo.wats.model.Actions action=actionRepo.findByActionName(selEbsMapping.getEbsActionName());
//		
//			String codeLine="";
//			List<CodeLines>listOfCodeLines=codeLineRepo.findByActionIdOrderByCodeLineId(action);
//			HashMap<String, Object> result = null;
//			String dbValue="";
//			String key="" ;
//			String value;
//			ArrayList<String> listOfRobotCodeLines= new ArrayList<>();
//			//CodeLines robotCodeLine=listOfCodeLines.get(0);
//			for(CodeLines robotCodeLine:listOfCodeLines) {
//				codeLine=robotCodeLine.getRobot_line();
//			if(robotCodeLine.getParam_values()!=null)
//			{
//				
//				
//				String param_value=robotCodeLine.getParam_values();
//				try {
//					result = new ObjectMapper().readValue(param_value, HashMap.class);
//					for (Map.Entry<String,Object> entry : result.entrySet())
//					{
//			             key =entry.getKey();
//						 value=(String)entry.getValue();
//					
//					if(value.equalsIgnoreCase("<Pick from Config Table>"))
//					{
//						dbValue=	codeLineRepo.findByConfigurationId(configVO.getConfigurationId(),key);
//					}
//					if(value.equalsIgnoreCase("<Pick from Input Value>"))
//					{
//						dbValue=	codeLineRepo.findByTestRunScriptId(fetchMetadataVO.getTestRunScriptParamId(), key);
//					}
//					if(value.equalsIgnoreCase("<Pick from Input Parameter>"))
//					{
//						dbValue=	codeLineRepo.findByTestRunScriptIdInputParam(fetchMetadataVO.getTestRunScriptParamId(), key);
//					}
//					codeLine= codeLine.replace("${"+key+"}", dbValue);
//					
//					}
//					listOfRobotCodeLines.add(codeLine);
//
//				} catch (JsonMappingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (JsonProcessingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//			
//				System.out.println(codeLine);
//			}
//			else
//			{
//				listOfRobotCodeLines.add(codeLine);	
//			}
//		}
//			return listOfRobotCodeLines;
//	}
//
//	public void mousehover(WebDriver driver, String param1, String param2, TestRunExecutionVO fetchMetadataVO,
//			ConfigVO configVO) throws Exception {
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//			Actions actions = new Actions(driver);
//			wait.until(ExpectedConditions
//					.presenceOfElementLocated(By.xpath("(//table[@summary='" + param1 + "']//tr[1]/following::a)[2]")));
//			scrollUsingElement(driver, param1, fetchMetadataVO, configVO);
//			Thread.sleep(6000);
//			WebElement waittext = driver
//					.findElement(By.xpath("(//table[@summary='" + param1 + "']//tr[1]/following::a)[2]"));
//			actions.moveToElement(waittext).build().perform();
//			clickImage(driver, param2, param1, fetchMetadataVO, configVO);
//			screenshot(driver, "", fetchMetadataVO, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			String xpath = "(//table[@summary='" + param1 + "']//tr[1]/following::a)[2]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			log.info("Sucessfully Clicked mousehover" + scripNumber);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  mousehover" + scripNumber);
//			System.out.println(e);
//		}
//		try {
//			Actions actions = new Actions(driver);
//			WebElement waittill = driver.findElement(By.xpath(
//					"(//table[@role='presentation']/following::a[normalize-space(text())='" + param1 + "'])[1]"));
//			actions.moveToElement(waittill).build().perform();
//			Thread.sleep(5000);
//			System.out.print("Successfully executed Mousehover");
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			String xpath = "(//table[@role='presentation']/following::a[normalize-space(text())='" + param1 + "'])[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			log.info("Sucessfully Clicked mousehover" + scripNumber);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  mousehover" + scripNumber);
//			System.out.println(e);
//			screenshotFail(driver, "Failed during MouseHover Method", fetchMetadataVO, configVO);
//			throw e;
//		}
//	}
//
//	public ArrayList<String> enter(WebDriver driver, TestRunExecutionVO fetchMetadataVO, ConfigVO configVO,String actionName) throws Exception {
//		SeleniumEbsMapping selEbsMapping=ebsMappingRepo.findBySeleniumActionName(actionName);
//		com.winfo.wats.model.Actions action=actionRepo.findByActionName(selEbsMapping.getEbsActionName());
//		
//			String codeLine="";
//			List<CodeLines>listOfCodeLines=codeLineRepo.findByActionIdOrderByCodeLineId(action);
//			HashMap<String, Object> result = null;
//			String dbValue="";
//			String key="" ;
//			String value;
//			ArrayList<String> listOfRobotCodeLines= new ArrayList<>();
//			//CodeLines robotCodeLine=listOfCodeLines.get(0);
//			for(CodeLines robotCodeLine:listOfCodeLines) {
//				codeLine=robotCodeLine.getRobot_line();
//			if(robotCodeLine.getParam_values()!=null)
//			{
//				
//				
//				String param_value=robotCodeLine.getParam_values();
//				try {
//					result = new ObjectMapper().readValue(param_value, HashMap.class);
//					for (Map.Entry<String,Object> entry : result.entrySet())
//					{
//			             key =entry.getKey();
//						 value=(String)entry.getValue();
//					
//					if(value.equalsIgnoreCase("<Pick from Config Table>"))
//					{
//						dbValue=	codeLineRepo.findByConfigurationId(configVO.getConfigurationId(),key);
//					}
//					if(value.equalsIgnoreCase("<Pick from Input Value>"))
//					{
//						dbValue=	codeLineRepo.findByTestRunScriptId(fetchMetadataVO.getTestRunScriptParamId(), key);
//					}
//					if(value.equalsIgnoreCase("<Pick from Input Parameter>"))
//					{
//						dbValue=	codeLineRepo.findByTestRunScriptIdInputParam(fetchMetadataVO.getTestRunScriptParamId(), key);
//					}
//					codeLine= codeLine.replace("${"+key+"}", dbValue);
//					
//					}
//					listOfRobotCodeLines.add(codeLine);
//
//				} catch (JsonMappingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (JsonProcessingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//			
//				System.out.println(codeLine);
//			}
//			else
//			{
//				listOfRobotCodeLines.add(codeLine);	
//			}
//		}
//			return listOfRobotCodeLines;
//	}
//
//	public String screenshot(WebDriver driver, String screenshotName, TestRunExecutionVO fetchMetadataVO,
//
//			ConfigVO configVO) {
//
//		String image_dest = null;
//
//		try {
//
//			TakesScreenshot ts = (TakesScreenshot) driver;
//
//			File source = ts.getScreenshotAs(OutputType.FILE);
//
//			image_dest = (configVO.getScreenshotPath() + fetchMetadataVO.getCustomerName() + "/"
//
//					+ fetchMetadataVO.getTestRunName() + "/" + fetchMetadataVO.getSeqNum() + "_"
//
//					+ fetchMetadataVO.getLineNumber() + "_" + fetchMetadataVO.getScenarioName() + "_"
//
//					+ fetchMetadataVO.getScriptNumber() + "_" + fetchMetadataVO.getTestRunName() + "_"
//
//					+ fetchMetadataVO.getLineNumber() + "_Passed").concat(".jpg");
//
//			System.out.println(image_dest);
//
//			File destination = new File(image_dest);
//
//			if (!destination.exists()) {
//
//				System.out.println("creating directory: " + destination.getName());
//
//				boolean result = false;
//
//				try {
//
//					destination.mkdirs();
//
//					result = true;
//
//				} catch (SecurityException se) {
//
//					// handle it
//
//					System.out.println(se.getMessage());
//
//				}
//
//			} else {
//
//				System.out.println("Folder exist");
//
//			}
//
//			// FileUtils.copyFile(source, destination);
//
////			Files.copy(FileSystems.getDefault().getPath(source.getPath()), FileSystems.getDefault().getPath(destination.getPath()), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
//
//			Files.copy(source.toPath(),
//
//					destination.toPath(), StandardCopyOption.COPY_ATTRIBUTES,
//
//					StandardCopyOption.REPLACE_EXISTING);
//
//			log.info("Successfully Screenshot is taken");
//
//			return image_dest;
//
//		} catch (Exception e) {
//
//			log.error("Failed During Taking screenshot");
//
//			System.out.println("Exception while taking Screenshot" + e.getMessage());
//
//			return e.getMessage();
//
//		}
//
//	}
//
//	public String screenshotFail(WebDriver driver, String screenshotName, TestRunExecutionVO fetchMetadataVO,
//			ConfigVO configVO) {
//		String image_dest = null;
//		try {
//			TakesScreenshot ts = (TakesScreenshot) driver;
//			File source = ts.getScreenshotAs(OutputType.FILE);
//			String currenttime = new SimpleDateFormat("MM-dd-yyyy HH-mm-ss").format(Calendar.getInstance().getTime());
//			image_dest = (configVO.getScreenshotPath() + fetchMetadataVO.getCustomerName() + "/"
//					+ fetchMetadataVO.getTestRunName() + "/" + fetchMetadataVO.getSeqNum() + "_"
//					+ fetchMetadataVO.getLineNumber() + "_" + fetchMetadataVO.getScenarioName() + "_"
//					+ fetchMetadataVO.getScriptNumber() + "_" + fetchMetadataVO.getTestRunName() + "_"
//					+ fetchMetadataVO.getLineNumber() + "_Failed").concat(".jpg");
//			File destination = new File(image_dest);
//
//			if (!destination.exists()) {
//
//				System.out.println("creating directory: " + destination.getName());
//
//				boolean result = false;
//
//				try {
//
//					destination.mkdirs();
//
//					result = true;
//
//				} catch (SecurityException se) {
//
//					// handle it
//
//					System.out.println(se.getMessage());
//
//				}
//
//			} else {
//
//				System.out.println("Folder exist");
//
//			}
//
//			// FileUtils.copyFile(source, destination);
//
////			Files.copy(FileSystems.getDefault().getPath(source.getPath()), FileSystems.getDefault().getPath(destination.getPath()), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
//
//			Files.copy(source.toPath(),
//
//					destination.toPath(), StandardCopyOption.COPY_ATTRIBUTES,
//
//					StandardCopyOption.REPLACE_EXISTING);
//
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Successfully Failed Screenshot is Taken " + scripNumber);
//			return image_dest;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during screenshotFail Action. " + scripNumber);
//			System.out.println("Exception while taking Screenshot" + e.getMessage());
//			return e.getMessage();
//		}
//	}
//
//	public String screenshotException(WebDriver driver, String screenshotName,
//
//			List<TestRunExecutionVO> fetchMetadataListVO, ConfigVO configVO, String line_number, String param) {
//
//		String image_dest = null;
//
//		try {
//
//			TakesScreenshot ts = (TakesScreenshot) driver;
//
//			File source = ts.getScreenshotAs(OutputType.FILE);
//
//			image_dest = (configVO.getScreenshotPath() + fetchMetadataListVO.get(0).getCustomerName() + "/"
//
//					+ fetchMetadataListVO.get(0).getTestRunName() + "/" + line_number + "_"
//
//					+ fetchMetadataListVO.get(0).getScenarioName() + "_"
//
//					+ fetchMetadataListVO.get(0).getScriptNumber() + "_"
//
//					+ fetchMetadataListVO.get(0).getTestRunName() + "_" + fetchMetadataListVO.get(0).getScriptId()
//
//					+ "_" + param + "_Failed").concat(".jpg");
//
//			File destination = new File(image_dest);
//
//			if (!destination.exists()) {
//
//				System.out.println("creating directory: " + destination.getName());
//
//				boolean result = false;
//
//				try {
//
//					destination.mkdirs();
//
//					result = true;
//
//				} catch (SecurityException se) {
//
//					// handle it
//
//					System.out.println(se.getMessage());
//
//				}
//
//			} else {
//
//				System.out.println("Folder exist");
//
//			}
//
//			// FileUtils.copyFile(source, destination);
//
//			Files.copy(FileSystems.getDefault().getPath(source.getPath()),
//					FileSystems.getDefault().getPath(destination.getPath()), StandardCopyOption.COPY_ATTRIBUTES,
//					StandardCopyOption.REPLACE_EXISTING);
//
//			log.info("Successfully Failed Screenshot is Taken ");
//
//			return image_dest;
//
//		} catch (Exception e) {
//
//			log.error("Failed during screenshotFail Action. ");
//
//			System.out.println("Exception while taking Screenshot" + e.getMessage());
//
//			return e.getMessage();
//
//		}
//
//	}
//
//	public void deleteAllCookies(WebDriver driver, TestRunExecutionVO fetchMetadataVO, ConfigVO configVO) {
//		try {
//			driver.manage().deleteAllCookies();
//			log.info("Successfully Deleted All The Cookies.");
//		} catch (Exception e) {
//			log.error("Failed To Delete All The Cookies.");
//			screenshotFail(driver, "Failed during deleteAllCookies Method", fetchMetadataVO, configVO);
//			System.out.println("cookies not deleted");
//			throw e;
//		}
//	}
//
//	public void selectCheckBox(WebDriver driver, String xpath, TestRunExecutionVO fetchMetadataVO) {
//		try {
//			WebElement element = driver.findElement(By.xpath(xpath));
//			if (element.isSelected()) {
//			} else {
//				element.click();
//			}
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked selectCheckBox" + scripNumber);
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during selectCheckBox" + scripNumber);
//			screenshotFail(driver, "Failed during selectCheckBox Method", fetchMetadataVO, null);
//			e.printStackTrace();
//			System.out.println(xpath);
//			throw e;
//		}
//	}
//
//	public void selectByText(WebDriver driver, String param1, String param2, String inputData,
//			TestRunExecutionVO fetchMetadataVO, ConfigVO configVO) {
//		try {
//			if (param1.equalsIgnoreCase("Address Purposes")) {
//				Thread.sleep(2000);
//				WebElement waittext = driver.findElement(
//						By.xpath(("//*[normalize-space(text())='" + param1 + "']/following::*[normalize-space(text())='"
//								+ param2 + "']/following::select[not (@title)]")));
//				selectMethod(driver, inputData, fetchMetadataVO, waittext, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//
//				String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']/following::select[not (@title)]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				log.info("Sucessfully Clicked selectByText" + scripNumber);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during selectByText" + scripNumber);
//			System.out.println(param2);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Holds")) {
//				Thread.sleep(2000);
//				WebElement waittext = driver.findElement(
//						By.xpath("//*[normalize-space(text())='" + param1 + "']/following::*[normalize-space(text())='"
//								+ param2 + "']/preceding-sibling::select[@title='']"));
//				selectMethod(driver, inputData, fetchMetadataVO, waittext, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//
//				String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']/preceding-sibling::select[@title='']";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				log.info("Sucessfully Clicked Holds selectByText" + scripNumber);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Holds selectByText" + scripNumber);
//			System.out.println(param2);
//		}
//		try {
//			if (param2.equalsIgnoreCase("Batch Status")) {
//				Thread.sleep(2000);
//				WebElement waittext = driver.findElement(By.xpath(
//						("//*[normalize-space(text())='" + param1 + "']/following::label[normalize-space(text())='"
//								+ param2 + "']/preceding-sibling::select[1]")));
//				selectMethod(driver, inputData, fetchMetadataVO, waittext, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				String xpath = "//*[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/preceding-sibling::select[1]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				log.info("Sucessfully Clicked Batch Status selectByText" + scripNumber);
//				return;
//			}
//
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Batch Status selectByText" + scripNumber);
//			System.out.println(param2);
//		}
//		try {
//			if (param1.equalsIgnoreCase("Release") && param2.equalsIgnoreCase("Name")) {
//				Thread.sleep(5000);
//				WebElement waittext = driver.findElement(By.xpath(
//						("(//*[normalize-space(text())='" + param1 + "']/following::label[normalize-space(text())='"
//								+ param2 + "']/preceding-sibling::select)[2]")));
//				selectMethod(driver, inputData, fetchMetadataVO, waittext, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//
//				String xpath = "(//*[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/preceding-sibling::select)[2]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				log.info("Sucessfully Clicked Release selectByText" + scripNumber);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Release selectByText" + scripNumber);
//			System.out.println(param2);
//		}
//		try {
//			Thread.sleep(2000);
//			WebElement waittext = driver.findElement(By.xpath(("//*[normalize-space(text())='" + param1
//					+ "']/following::label[normalize-space(text())='" + param2 + "']/following::select[1]")));
//			selectMethod(driver, inputData, fetchMetadataVO, waittext, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked selectByText" + scripNumber);
//
//			String xpath = "//*[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/following::select[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during selectByText" + scripNumber);
//			System.out.println(param2);
//		}
//		try {
//			Thread.sleep(2000);
//			WebElement waittext = driver.findElement(By.xpath(("//*[contains(text(),'" + param1
//					+ "')]/following::label[normalize-space(text())='" + param2 + "']/preceding::select[1]")));
//			selectMethod(driver, inputData, fetchMetadataVO, waittext, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//
//			String xpath = "//*[contains(text(),'param1')]/following::label[normalize-space(text())='param2']/preceding::select[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			log.info("Sucessfully Clicked selectByText" + scripNumber);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during selectByText" + scripNumber);
//			System.out.println(param2);
//		}
//		try {
//			Thread.sleep(2000);
//			WebElement waittext = driver.findElement(By.xpath(("//*[normalize-space(text())='" + param1
//					+ "']/following::label[normalize-space(text())='" + param2 + "']/preceding::select[1]")));
//			selectMethod(driver, inputData, fetchMetadataVO, waittext, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//
//			String xpath = "//*[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/preceding::select[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			log.info("Sucessfully Clicked selectByText" + scripNumber);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during selectByText" + scripNumber);
//			System.out.println(param2);
//		}
//		try {
//			Thread.sleep(2000);
//			WebElement waittext = driver.findElement(By.xpath(("//*[contains(text(),'" + param1
//					+ "')]/following::label[normalize-space(text())='" + param2 + "']/following::select[1]")));
//			selectMethod(driver, inputData, fetchMetadataVO, waittext, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//
//			String xpath = "//*[contains(text(),'param1')]/following::label[normalize-space(text())='param2']/following::select[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			log.info("Sucessfully Clicked selectByText" + scripNumber);
//			return;
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during selectByText" + scripNumber);
//			System.out.println(param2);
//		}
//		try {
//			if (param2 == "") {
//				WebElement waittext = driver
//						.findElement(By.xpath(("//*[contains(text(),'" + param1 + "')]/following::select[1]")));
//				selectMethod(driver, inputData, fetchMetadataVO, waittext, configVO);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//
//				String xpath = "//*[contains(text(),'param1')]/following::select[1]";
//				testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//				log.info("Sucessfully Clicked selectCheckBox" + scripNumber);
//				return;
//			}
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during selectByText" + scripNumber);
//			System.out.println(param2);
//		}
//		try {
//			WebElement waittext = driver
//					.findElement(By.xpath(("//*[contains(text(),'" + param1 + "')]/preceding-sibling::select[1]")));
//			selectMethod(driver, inputData, fetchMetadataVO, waittext, configVO);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//
//			String xpath = "//*[contains(text(),'param1')]/preceding-sibling::select[1]";
//			testRunScriptExecService.saveXpathParams(param1, param2, scripNumber, xpath);
//			log.info("Sucessfully Clicked selectByText" + scripNumber);
//			return;
//		} catch (Exception e) {
//			System.out.println(param2);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during selectByText" + scripNumber);
//			screenshotFail(driver, "Failed during selectByValue Method", fetchMetadataVO, configVO);
//			throw e;
//		}
//	}
//
//	private void selectMethod(WebDriver driver, String inputData, TestRunExecutionVO fetchMetadataVO, WebElement waittext,
//			ConfigVO configVO) {
//		Actions actions = new Actions(driver);
//		actions.moveToElement(waittext).build().perform();
//		Select selectBox = new Select(waittext);
//		selectBox.selectByVisibleText(inputData);
//		String scripNumber = fetchMetadataVO.getScriptNumber();
//		log.info("Sucessfully Clicked selectMethod" + scripNumber);
//		screenshot(driver, "", fetchMetadataVO, configVO);
//		return;
//	}
//
//	public void selectByValue(WebDriver driver, String xpath, String inputData, TestRunExecutionVO fetchMetadataVO,
//			ConfigVO configVO) {
//		try {
//			WebElement webElement = driver.findElement(By.xpath(xpath));
//			Select selectBox = new Select(webElement);
//			selectBox.selectByValue(inputData);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked selectByValue" + scripNumber);
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during selectByValue" + scripNumber);
//			screenshotFail(driver, "Failed during selectByValue Method", fetchMetadataVO, configVO);
//			System.out.println(xpath);
//			e.printStackTrace();
//			throw e;
//		}
//	}
//
//	public void switchToDefaultFrame(WebDriver driver) {
//		try {
//			driver.switchTo().defaultContent();
//			log.info("Successfully switched to default Frame");
//		} catch (Exception e) {
//			log.error("Failed During Switching to default Action");
//			throw e;
//		}
//	}
//
//	public String copynumber(WebDriver driver, String inputParam1, String inputParam2, TestRunExecutionVO fetchMetadataVO,
//
//			ConfigVO configVO) {
//
//		String value = null;
//
//		try {
//
//			if (inputParam1.equalsIgnoreCase("Totals") && inputParam2.equalsIgnoreCase("Total")) {
//
//				Thread.sleep(5000);
//
//				WebElement webElement = driver.findElement(By.xpath("//*[normalize-space(text())='" + inputParam1
//
//						+ "']/following::*[normalize-space(text())='" + inputParam2 + "']/following::span[1]"));
//
//				Actions actions = new Actions(driver);
//
//				actions.moveToElement(webElement).build().perform();
//				value = copyNegative(webElement);
//
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				String xpath = "//*[normalize-space(text())='inputParam1']/following::*[normalize-space(text())='inputParam2']/following::span[1]";
//				testRunScriptExecService.saveXpathParams(inputParam1, inputParam2, scripNumber, xpath);
//				String testParamId = String.valueOf(fetchMetadataVO.getTestRunScriptParamId());
//				String testSetId = String.valueOf(fetchMetadataVO.getTestRunScriptId());
//				testRunScriptExecService.saveCopyNumber(value, testParamId, testSetId);
//				log.info("Sucessfully Clicked Totals or Total copynumber" + scripNumber);
//				return value;
//
//			}
//
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during Totals or Total copynumber" + scripNumber);
//
//			System.out.println(inputParam2);
//
//		}
//		try {
//
//			if (inputParam1.equalsIgnoreCase("Totals") || inputParam2.equalsIgnoreCase("Transaction Number")
//					|| inputParam2.equalsIgnoreCase("Batch Number") || inputParam1.equalsIgnoreCase("Overview")) {
//
//				Thread.sleep(5000);
//
//				WebElement webElement = driver.findElement(By.xpath("//*[normalize-space(text())='" + inputParam1
//
//						+ "']/following::*[normalize-space(text())='" + inputParam2 + "']/following::span[1]"));
//
//				Actions actions = new Actions(driver);
//
//				actions.moveToElement(webElement).build().perform();
//
//				value = copyInt(webElement);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				String xpath = "//*[normalize-space(text())='inputParam1']/following::*[normalize-space(text())='inputParam2']/following::span[1]";
//				testRunScriptExecService.saveXpathParams(inputParam1, inputParam2, scripNumber, xpath);
//				String testParamId = String.valueOf(fetchMetadataVO.getTestRunScriptParamId());
//				String testSetId = String.valueOf(fetchMetadataVO.getTestRunScriptId());
//				testRunScriptExecService.saveCopyNumber(value, testParamId, testSetId);
//				log.info("Sucessfully Clicked  copynumber" + scripNumber);
//
//				return value;
//
//			}
//
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during  copynumber" + scripNumber);
//
//			System.out.println(inputParam2);
//
//		}
//
//		try {
//
//			if (inputParam1.equalsIgnoreCase("Confirmation") || inputParam2.equalsIgnoreCase("adjustment")
//					|| inputParam1.equalsIgnoreCase("Information")) {
//
//				Thread.sleep(5000);
//
//				WebElement webElement = driver.findElement(By.xpath("//div[normalize-space(text())='" + inputParam1
//
//						+ "']/following::*[contains(text(),'" + inputParam2 + "')]"));
//
//				Actions actions = new Actions(driver);
//
//				actions.moveToElement(webElement).build().perform();
//
//				value = copyInt(webElement);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				String xpath = "//div[normalize-space(text())='inputParam1']/following::*[contains(text(),'inputParam2')]";
//				testRunScriptExecService.saveXpathParams(inputParam1, inputParam2, scripNumber, xpath);
//				String testParamId = String.valueOf(fetchMetadataVO.getTestRunScriptParamId());
//				String testSetId =String.valueOf(fetchMetadataVO.getTestRunScriptId());
//				testRunScriptExecService.saveCopyNumber(value, testParamId, testSetId);
//				log.info("Sucessfully Clicked  copynumber" + scripNumber);
//
//				return value;
//
//			}
//
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during copynumber" + scripNumber);
//			System.out.println(inputParam2);
//
//		}
//
//		try {
//
//			WebElement webElement = driver.findElement(By.xpath("(//div[contains(@title,'" + inputParam1 + "')])[1]"));
//
//			Actions actions = new Actions(driver);
//
//			actions.moveToElement(webElement).build().perform();
//
//			Thread.sleep(5000);
//
//			if (webElement.isDisplayed() == true) {
//
//				value = copyMethod(webElement, value);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				String xpath = "(//div[contains(@title,'inputParam1')])[1]";
//				testRunScriptExecService.saveXpathParams(inputParam1, inputParam2, scripNumber, xpath);
//
//				String testParamId = String.valueOf(fetchMetadataVO.getTestRunScriptParamId());
//				String testSetId = String.valueOf(fetchMetadataVO.getTestRunScriptId());
//				testRunScriptExecService.saveCopyNumber(value, testParamId, testSetId);
//				log.info("Sucessfully Clicked copynumber" + scripNumber);
//
//				return value;
//
//			}
//
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during copynumber" + scripNumber);
//
//			System.out.println(inputParam1);
//
//		}
//
//		try {
//			WebElement webElement = driver.findElement(By.xpath("(//div[contains(text(),'" + inputParam1 + "')])[1]"));
//
//			Actions actions = new Actions(driver);
//
//			actions.moveToElement(webElement).build().perform();
//
//			Thread.sleep(5000);
//
//			if (webElement.isDisplayed() == true) {
//
//				value = copyMethod(webElement, value);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				String xpath = "(//div[contains(text(),'inputParam1')])[1]";
//				testRunScriptExecService.saveXpathParams(inputParam1, inputParam2, scripNumber, xpath);
//
//				String testParamId = String.valueOf(fetchMetadataVO.getTestRunScriptParamId());
//				String testSetId = String.valueOf(fetchMetadataVO.getTestRunScriptId());
//				testRunScriptExecService.saveCopyNumber(value, testParamId, testSetId);
//				log.info("Sucessfully Clicked copynumber" + scripNumber);
//
//				return value;
//
//			}
//
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during copynumber" + scripNumber);
//
//			System.out.println(inputParam1);
//
//		}
//		try {
//
//			WebElement webElement = driver
//					.findElement(By.xpath("//label[text()='" + inputParam1 + "']/following::td[1]"));
//
//			Actions actions = new Actions(driver);
//
//			actions.moveToElement(webElement).build().perform();
//
//			Thread.sleep(5000);
//
//			if (webElement.isDisplayed() == true) {
//
//				value = copyMethod(webElement, value);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				String xpath = "//label[text()='inputParam1']/following::td[1]";
//				testRunScriptExecService.saveXpathParams(inputParam1, inputParam2, scripNumber, xpath);
//
//				String testParamId = String.valueOf(fetchMetadataVO.getTestRunScriptParamId());
//				String testSetId = String.valueOf(fetchMetadataVO.getTestRunScriptId());
//				testRunScriptExecService.saveCopyNumber(value, testParamId, testSetId);
//				log.info("Sucessfully Clicked copynumber" + scripNumber);
//
//				return value;
//
//			}
//
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during copynumber" + scripNumber);
//
//			System.out.println(inputParam1);
//
//		}
//
//		try {
//			WebElement webElement = driver.findElement(By.xpath("//img[@title='In Balance ']/following::td[1]"));
//
//			Actions actions = new Actions(driver);
//
//			actions.moveToElement(webElement).build().perform();
//
//			if (webElement.isDisplayed() == true) {
//
//				value = copyMethod(webElement, value);
//				String scripNumber = fetchMetadataVO.getScriptNumber();
//				String xpath = "//img[@title='In Balance ']/following::td[1]";
//				testRunScriptExecService.saveXpathParams(inputParam1, inputParam2, scripNumber, xpath);
//				String testParamId = String.valueOf(fetchMetadataVO.getTestRunScriptParamId());
//				String testSetId = String.valueOf(fetchMetadataVO.getTestRunScriptId());
//				testRunScriptExecService.saveCopyNumber(value, testParamId, testSetId);
//				log.info("Sucessfully Clicked copynumber" + scripNumber);
//
//				return value;
//
//			}
//
//		} catch (Exception e) {
//
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during copynumber" + scripNumber);
//
//			screenshotFail(driver, "Failed during CopyNumber Method", fetchMetadataVO, configVO);
//
//			System.out.println(inputParam1);
//
//			throw e;
//
//		}
//
//		return value;
//
//	}
//
//	private String copyMethod(WebElement webElement, String value) {
//
//		String num = null;
//
//		try {
//
////          System.out.println(value);
//
//			String number = webElement.getText();
//
//			System.out.println(number);
//
//			num = number.replaceAll("[^\\d.]+|\\.(?!\\d)", "");
//
//			System.out.println(num);
//
//			log.info("Successfully Copied the Number");
//
//		} catch (Exception e) {
//			log.error("Sucessfully Clicked copynumber");
//
//			System.out.println(e);
//
//		}
//
//		return num;
//
//	}
//
//	private String copyInt(WebElement webElement) {
//
//		String num = null;
//
//		try {
//
////         System.out.println(value);
//
//			String number = webElement.getText().toString();
//
//			System.out.println(number);
//
//			num = number.replaceAll("[^\\d.]+|\\.(?!\\d)", "");
//
//			System.out.println(num);
//
//			log.info("Successfully Copied the Number");
//
//		} catch (Exception e) {
//
//			System.out.println(e);
//
//		}
//
//		return num;
//
//	}
//
//	private String copyNegative(WebElement webElement) {
//
//		String num = null;
//
//		try {
//
////          System.out.println(value);
//
//			String number = webElement.getText().toString();
//
//			System.out.println(number);
//
//			num = number.replaceAll("[^\\-\\,\\d.]+|\\.(?!\\d)", number);
//			System.out.println(num);
//			log.info("Successfully Copied the Number");
//
//		} catch (Exception e) {
//
//			System.out.println(e);
//
//		}
//
//		return num;
//
//	}
//
//	public String copyy(WebDriver driver, String xpath, TestRunExecutionVO fetchMetadataVO, ConfigVO configVO) {
//		try {
//			WebElement webElement = driver.findElement(By.xpath(xpath));
//			String number = webElement.getText();
//			System.out.println(number);
//			String num = number.replaceAll("[^\\d.]+|\\.(?!\\d)", "");
//			StringSelection stringSelection = new StringSelection(num);
//			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked copyy" + scripNumber);
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during copyy" + scripNumber);
//			screenshotFail(driver, "Failed during copyy Method", fetchMetadataVO, configVO);
//			System.out.println(xpath);
//			throw e;
//		}
//		return xpath;
//	}
//
//	public String copytext(WebDriver driver, String xpath, TestRunExecutionVO fetchMetadataVO,
//			ConfigVO configVO) {
//		try {
//			java.util.List<WebElement> webElement = driver.findElements(By.xpath(xpath));
//			ArrayList<String> texts = new ArrayList<String>();
//			for (WebElement element : webElement) {
//				String[] text = element.getText().split(":");
//				if (text.length == 2) {
//					texts.add(text[1].trim().toString());
//					System.out.println(texts.get(0));
//					StringSelection stringSelection = new StringSelection(texts.get(0));
//					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
//				}
//			}
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked copytext" + scripNumber);
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during copytext" + scripNumber);
//			screenshotFail(driver, "Failed during copytext Method", fetchMetadataVO, configVO);
//			System.out.println(xpath);
//			throw e;
//		}
//		return xpath;
//
//	}
//
//	public void maximize(WebDriver driver, TestRunExecutionVO fetchMetadataVO, ConfigVO configVO) {
//		try {
//			driver.manage().window().maximize();
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked maximize" + scripNumber);
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during maximize" + scripNumber);
//			screenshotFail(driver, "Failed during maximize Method", fetchMetadataVO, configVO);
//			System.out.println("can not maximize");
//			e.printStackTrace();
//			throw e;
//
//		}
//	}
//
//	public void switchWindow(WebDriver driver, TestRunExecutionVO fetchMetadataVO, ConfigVO configVO) {
//		try {
//			driver.switchTo().window(Main_Window);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked switchWindow" + scripNumber);
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during switchWindow" + scripNumber);
//			screenshotFail(driver, "Failed during switchWindow Method", fetchMetadataVO, configVO);
//			System.out.println("can not switch to window");
//			e.printStackTrace();
//
//			throw e;
//		}
//	}
//
//	public void switchDefaultContent(WebDriver driver, TestRunExecutionVO fetchMetadataVO, ConfigVO configVO)
//			throws Exception {
//		try {
//			Thread.sleep(8000);
//			Set<String> set = driver.getWindowHandles();
//			Iterator<String> itr = set.iterator();
//			while (itr.hasNext()) {
//				String childWindow = itr.next();
//				driver.switchTo().window(childWindow);
//				System.out.println(driver.switchTo().window(childWindow).getTitle());
//				Thread.sleep(4000);
//			}
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked switchDefaultContent" + scripNumber);
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during switchDefaultContent" + scripNumber);
//			screenshotFail(driver, "Failed during windowhandle Method", fetchMetadataVO, configVO);
//			System.out.println("failed while hadling window");
//			e.printStackTrace();
//			throw e;
//		}
//	}
//
//	public void switchParentWindow(WebDriver driver, TestRunExecutionVO fetchMetadataVO, ConfigVO configVO)
//			throws Exception {
//		try {
//			Thread.sleep(8000);
//			Set<String> set = driver.getWindowHandles();
//			Iterator<String> itr = set.iterator();
//			while (itr.hasNext()) {
//				String childWindow = itr.next();
//				driver.switchTo().window(childWindow);
//				System.out.println(driver.switchTo().window(childWindow).getTitle());
//			}
//			driver.close();
//			Thread.sleep(2000);
//			Set<String> set1 = driver.getWindowHandles();
//			Iterator<String> itr1 = set1.iterator();
//			while (itr1.hasNext()) {
//				String childWindow = itr1.next();
//				driver.switchTo().window(childWindow);
//				System.out.println(driver.switchTo().window(childWindow).getTitle());
//			}
//			driver.close();
//			Set<String> set2 = driver.getWindowHandles();
//			Iterator<String> itr2 = set2.iterator();
//			while (itr2.hasNext()) {
//				String childWindow = itr2.next();
//				driver.switchTo().window(childWindow);
//				System.out.println(driver.switchTo().window(childWindow).getTitle());
//			}
//		} catch (Exception e) {
//			log.error("Failed to Handle the window");
//			screenshotFail(driver, "Failed during windowhandle Method", fetchMetadataVO, configVO);
//			System.out.println("failed while hadling window");
//			e.printStackTrace();
//			throw e;
//		}
//	}
//
//	public void switchToParentWindow(WebDriver driver, TestRunExecutionVO fetchMetadataVO, ConfigVO configVO)
//			throws Exception {
//		try {
//			Thread.sleep(8000);
//			Set<String> set = driver.getWindowHandles();
//			Iterator<String> itr = set.iterator();
//			while (itr.hasNext()) {
//				String childWindow = itr.next();
//				driver.switchTo().window(childWindow);
//				System.out.println(driver.switchTo().window(childWindow).getTitle());
//			}
//			driver.close();
//			Set<String> set1 = driver.getWindowHandles();
//			Iterator<String> itr1 = set1.iterator();
//			while (itr1.hasNext()) {
//				String childWindow = itr1.next();
//				driver.switchTo().window(childWindow);
//				System.out.println(driver.switchTo().window(childWindow).getTitle());
//			}
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked switchToParentWindow" + scripNumber);
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during switchToParentWindow" + scripNumber);
//			screenshotFail(driver, "Failed during windowhandle Method", fetchMetadataVO, configVO);
//			System.out.println("failed while hadling window");
//			e.printStackTrace();
//			throw e;
//		}
//	}
//
//	public void dragAnddrop(WebDriver driver, String xpath, String xpath1, TestRunExecutionVO fetchMetadataVO,
//			ConfigVO configVO) {
//		try {
//			WebElement dragElement = driver.findElement(By.xpath(xpath));
//			fromElement = dragElement;
//			WebElement dropElement = driver.findElement(By.xpath(xpath1));
//			toElement = dropElement;
//			Actions action = new Actions(driver);
//			// Action dragDrop = action.dragAndDrop(fromElement, webElement).build();
//			action.dragAndDrop(fromElement, toElement).build().perform();
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Successfully Drag and drop the values" + scripNumber);
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed During dragAnddrop Action." + scripNumber);
//			screenshotFail(driver, "Failed during dragAnddrop Method", fetchMetadataVO, configVO);
//			System.out.println(xpath);
//			e.printStackTrace();
//			throw e;
//		}
//	}
//
//	public void windowhandle(WebDriver driver, TestRunExecutionVO fetchMetadataVO, ConfigVO configVO)
//			throws Exception {
//		try {
//			Thread.sleep(20000);
//			String mainWindow = driver.getWindowHandle();
//			Set<String> set = driver.getWindowHandles();
//			Iterator<String> itr = set.iterator();
//			while (itr.hasNext()) {
//				String childWindow = itr.next();
//				if (!mainWindow.equals(childWindow)) {
//					driver.switchTo().window(childWindow);
//					System.out.println(driver.switchTo().window(childWindow).getTitle());
//					driver.manage().window().maximize();
//					Thread.sleep(2000);
//					screenshot(driver, "", fetchMetadataVO, configVO);
//					driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//					driver.switchTo().window(childWindow);
//				}
//			}
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Successfully Handeled the window" + scripNumber);
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed to Handle the window" + scripNumber);
//			screenshotFail(driver, "Failed during windowhandle Method", fetchMetadataVO, configVO);
//			System.out.println("failed while hadling window");
//			e.printStackTrace();
//			throw e;
//		}
//	}
//
//	public void switchToFrame(WebDriver driver, String inputParam, TestRunExecutionVO fetchMetadataVO,
//			ConfigVO configVO) throws Exception {
//		try {
//			Thread.sleep(5000);
//			WebElement waittext = driver
//					.findElement(By.xpath("//h1[normalize-space(text())='" + inputParam + "']/following::iframe[1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			driver.switchTo().frame(waittext);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			String xpath = "//h1[normalize-space(text())='inputParam']/following::iframe[1]";
//			testRunScriptExecService.saveXpathParams(inputParam, "", scripNumber, xpath);
//
//			return;
//		} catch (Exception e) {
//			System.out.println(e);
//		}
//		try {
//			WebElement waittext = driver.findElement(By.xpath("//iframe[contains(@id,'" + inputParam + "')]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			driver.switchTo().frame(waittext);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			String xpath = "//iframe[contains(@id,'inputParam')]";
//			testRunScriptExecService.saveXpathParams(inputParam, "", scripNumber, xpath);
//			return;
//		} catch (Exception e) {
//			System.out.println(e);
//		}
//		try {
//			Thread.sleep(5000);
//			WebElement waittext = driver.findElement(By.xpath("//iframe[@title='" + inputParam + "']"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			driver.switchTo().frame(waittext);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			String xpath = "//iframe[@title='inputParam']";
//			testRunScriptExecService.saveXpathParams(inputParam, "", scripNumber, xpath);
//			return;
//		} catch (Exception e) {
//			System.out.println(e);
//		}
//		try {
//			Thread.sleep(10000);
//			WebElement waittext = driver
//					.findElement(By.xpath("//*[normalize-space(text())='" + inputParam + "']/following::iframe[1]"));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(waittext).build().perform();
//			driver.switchTo().frame(waittext);
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			String xpath = "//*[normalize-space(text())='inputParam']/following::iframe[1]";
//			testRunScriptExecService.saveXpathParams(inputParam, "", scripNumber, xpath);
//			return;
//		} catch (Exception e) {
//			System.out.println(e);
//			log.error("Failed During switchToFrame Action");
//			screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, configVO);
//			throw e;
//		}
//	}
//
//	public void uploadFileAutoIT(String filelocation, TestRunExecutionVO fetchMetadataVO) throws Exception {
//		try {
//			String autoitscriptpath = System.getProperty("user.dir") + "/" + "File_upload_selenium_webdriver.au3";
//
//			Runtime.getRuntime().exec("cmd.exe /c Start AutoIt3.exe " + autoitscriptpath + " \"" + filelocation + "\"");
//			log.info("Successfully Uploaded The File");
//		} catch (Exception e) {
//			log.error("Failed During uploadFileAutoIT Action.");
////			screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, configVO);
//			System.out.println(filelocation);
//			e.printStackTrace();
//			throw e;
//
//		}
//	}
//
//	public void refreshPage(WebDriver driver, TestRunExecutionVO fetchMetadataVO, ConfigVO configVO) {
//		try {
//			driver.navigate().refresh();
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.info("Sucessfully Clicked refreshPage" + scripNumber);
//		} catch (Exception e) {
//			String scripNumber = fetchMetadataVO.getScriptNumber();
//			log.error("Failed during refreshPage" + scripNumber);
//			screenshotFail(driver, "Failed during refreshPage Method", fetchMetadataVO, configVO);
//			System.out.println("can not refresh the page");
//			e.printStackTrace();
//			throw e;
//
//		}
//	}
//
//	public void deleteScreenshots(List<TestRunExecutionVO> fetchMetadataListVO, ConfigVO configVO)
//			throws IOException {
//		File folder = new File(configVO.getScreenshotPath() + fetchMetadataListVO.get(0).getCustomerName() + "/"
//				+ fetchMetadataListVO.get(0).getTestRunName() + "/");
//		if (folder.exists()) {
//			File[] listOfFiles = folder.listFiles();
//
////		String image=configVO.getScreenshotPath() + fetchMetadataVO.getCustomerName() + "/"
////				+ fetchMetadataVO.getTestRunName() + "/" + fetchMetadataVO.getSeq_num() + "_"
////				+ fetchMetadataVO.getLine_number() + "_" + fetchMetadataVO.getScenario_name() + "_"
////				+ fetchMetadataVO.getScriptNumber() + "_" + fetchMetadataVO.getTestRunName() + "_"
////				+ fetchMetadataVO.getLine_number();
//			for (File file : Arrays.asList(listOfFiles)) {
//
//				String seqNum = String.valueOf(file.getName().substring(0, file.getName().indexOf('_')));
//
//				String seqnum1 = String.valueOf( fetchMetadataListVO.get(0).getSeqNum());
//				if (seqNum.equalsIgnoreCase(seqnum1)) {
//					Path imagesPath = Paths.get(file.getPath());
//					Files.delete(imagesPath);
//				}
//			}
//		}
//	}
//
//	public String getErrorMessages(WebDriver driver) {
//		try {
//			String text = driver.findElement(By.xpath("//td[@class='AFNoteWindow']")).getText();
//			return text;
//		} catch (Exception e) {
//			System.out.println(e);
//		}
//		try {
//			String text = driver.findElement(By.xpath("//div[contains(@class,'Error')]")).getText();
//			return text;
//		} catch (Exception e) {
//			System.out.println(e);
//		}
//		return null;
//	}
//
//	@Override
//	public void navigateToBackPage(WebDriver driver, TestRunExecutionVO fetchMetadataVO, ConfigVO configVO) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void openPdf(WebDriver driver, String input_value, TestRunExecutionVO fetchMetadataVO,
//			ConfigVO configVO) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void openFile(WebDriver driver, TestRunExecutionVO fetchMetadataVO, ConfigVO configVO) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void actionApprove(WebDriver driver, String param1, String param2, TestRunExecutionVO fetchMetadataVO,
//			ConfigVO configVO) throws Exception {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void multipleSendKeys(WebDriver driver, String param1, String param2, String value1, String value2,
//			TestRunExecutionVO fetchMetadataVO, ConfigVO configVO) throws Exception {
//		try {
//			if (param1.equalsIgnoreCase("Requisition Lines")) {
//				WebDriverWait wait = new WebDriverWait(driver, configVO.getWaitTime());
//				WebElement waittill = driver.findElement(By.xpath(
//						"//*[text()='" + param1 + "']/following::*[text()='" + value1 + "']/following::input[1]"));
//				Thread.sleep(1000);
//				// wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[text()='"+param1+"']/following::*[text()='"+value1+"']/following::input[1]"),
//				// value1));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(waittill).build().perform();
//				typeIntoValidxpath(driver, value2, waittill, configVO, fetchMetadataVO);
//				screenshot(driver, "", fetchMetadataVO, configVO);
//				return;
//			}
//		} catch (Exception e) {
//			System.out.println(e);
//			log.error("Failed during Click action.");
//			screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, configVO);
//		}
//	}
//	
//	public void createEvidenceReport(List<TestRunExecutionVO> fetchMetadataListVO, ConfigVO fetchConfigVO, String pdffileName,Date Starttime, Date endtime) 
//	{
//			evidenceReportConfiguration.getEvidenceReportFormat(reportFormat).createEvidenceReport( fetchMetadataListVO,  fetchConfigVO,  pdffileName, Starttime,  endtime);
//	}
//	public void uploadEvidenceReport(List<TestRunExecutionVO> fetchMetadataListVO, ConfigVO fetchConfigVO)
//	{
//		evidenceReportConfiguration.getEvidenceReportUploadLocation(reportLocation).uploadEvidenceReport(fetchMetadataListVO, fetchConfigVO);
//	}
//	public void createScriptLevelEvidenceReport(TestRunExecutionVO fetchMetadataListVO, ConfigVO fetchConfigVO,String string, Date startdate, Date enddate,String scriptStatus)
//	{
//		evidenceReportConfiguration.getEvidenceReportFormat(reportFormat).createScriptLevelEvidenceReport(fetchMetadataListVO, fetchConfigVO, string, startdate, enddate, scriptStatus);
//	}
//	public  ArrayList<String> ebsActions(WebDriver driver, TestRunExecutionVO fetchMetadataVO, ConfigVO configVO,String actionName) throws Exception {
//		SeleniumEbsMapping selEbsMapping=ebsMappingRepo.findBySeleniumActionName(actionName);
//		com.winfo.wats.model.Actions action=actionRepo.findByActionName(selEbsMapping.getEbsActionName());
//		
//			String codeLine="";
//			List<CodeLines>listOfCodeLines=codeLineRepo.findByActionIdOrderByCodeLineId(action);
//			HashMap<String, Object> result = null;
//			String dbValue="";
//			String key="" ;
//			String value;
//			ArrayList<String> listOfRobotCodeLines= new ArrayList<>();
//			//CodeLines robotCodeLine=listOfCodeLines.get(0);
//			for(CodeLines robotCodeLine:listOfCodeLines) {
//				codeLine=robotCodeLine.getRobot_line();
//			if(robotCodeLine.getParam_values()!=null)
//			{
//				
//				
//				String param_value=robotCodeLine.getParam_values();
//				try {
//					result = new ObjectMapper().readValue(param_value, HashMap.class);
//					for (Map.Entry<String,Object> entry : result.entrySet())
//					{
//			             key =entry.getKey();
//						 value=(String)entry.getValue();
//					
//					if(value.equalsIgnoreCase("<Pick from Config Table>"))
//					{
//						dbValue=	codeLineRepo.findByConfigurationId(configVO.getConfigurationId(),key);
//					}
//					if(value.equalsIgnoreCase("<Pick from Input Value>"))
//					{
//						dbValue=	codeLineRepo.findByTestRunScriptId(fetchMetadataVO.getTestRunScriptParamId(), key);
//					}
//					if(value.equalsIgnoreCase("<Pick from Input Parameter>"))
//					{
//						dbValue=	codeLineRepo.findByTestRunScriptIdInputParam(fetchMetadataVO.getTestRunScriptParamId(), key);
//					}
//					codeLine= codeLine.replace("${"+key+"}", dbValue);
//					
//					}
//					listOfRobotCodeLines.add(codeLine);
//
//				} catch (JsonMappingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (JsonProcessingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//			
//				System.out.println(codeLine);
//			}
//			else
//			{
//				listOfRobotCodeLines.add(codeLine);	
//			}
//		}
//			return listOfRobotCodeLines;
//	}
//}
