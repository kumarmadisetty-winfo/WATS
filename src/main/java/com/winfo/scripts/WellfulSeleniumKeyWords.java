package com.winfo.scripts;

import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.FileImageOutputStream;

//import blank.FFmpegFrameRecorder;
//import blank.IplImage;
//import blank.OpenCVFrameConverter;

import org.apache.log4j.Logger;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
//import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import com.itextpdf.text.DocumentException;
import com.winfo.service.SeleniumKeyWordsInterface;
import com.winfo.serviceImpl.AbstractSeleniumKeywords;
import com.winfo.serviceImpl.DataBaseEntry;
import com.winfo.serviceImpl.DynamicRequisitionNumber;
import com.winfo.serviceImpl.LimitScriptExecutionService;
import com.winfo.serviceImpl.ScriptXpathService;
import com.winfo.utils.StringUtils;
import com.winfo.vo.ApiValidationVO;
import com.winfo.vo.CustomerProjectDto;
import com.winfo.vo.FetchConfigVO;
import com.winfo.vo.ScriptDetailsDto;

@Service("WELLFUL")
//@Service("WATS")
@RefreshScope
public class WellfulSeleniumKeyWords extends AbstractSeleniumKeywords implements SeleniumKeyWordsInterface {
//New-changes - added annotation for DatabaseEntry

	@Autowired
	private DataBaseEntry databaseentry;
	@Autowired
	ScriptXpathService service;
	@Autowired
	DynamicRequisitionNumber dynamicnumber;
	@Autowired
	LimitScriptExecutionService limitScriptExecutionService;

	@Value("${configvO.watsdhlogo}")
	private String watslogo;

	@Value("${configvO.watsvediologo}")
	private String watsvediologo;

	@Value("${configvO.whiteimage}")
	private String whiteimage;

//	public static log log = LogManager.getlog(SeleniumKeyWords.class);
	/*
	 * private Integer ElementWait = Integer
	 * .valueOf(PropertyReader.getPropertyValue(PropertyConstants.EXECUTION_TIME.
	 * value)); public int WaitElementSeconds = new Integer(ElementWait);
	 */
	public static final Logger logger = Logger.getLogger(WellfulSeleniumKeyWords.class);

	public String Main_Window = "";
	public WebElement fromElement;
	public WebElement toElement;

	private static final DecimalFormat df = new DecimalFormat("00");

	public void loginApplication(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			String type1, String type2, String type3, String param1, String param2, String param3, String keysToSend,
			String value, CustomerProjectDto customerDetails) throws Exception {
		String param5 = "password";
		String param6 = "Sign In";
		navigateUrl(driver, fetchConfigVO, fetchMetadataVO, customerDetails);
		String xpath1 = loginPage(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
		String xpath2 = loginPage(driver, param5, value, fetchMetadataVO, fetchConfigVO, customerDetails);
		if (xpath2.equalsIgnoreCase(null)) {
			throw new IOException("Failed during login page");
		}
		String scripNumber = fetchMetadataVO.getScriptNumber();
		String scriptID = fetchMetadataVO.getScriptId();
		String xpath = xpath1 + ";" + xpath2;
		String lineNumber = fetchMetadataVO.getLineNumber();
		service.saveXpathParams(scriptID, lineNumber, xpath);
//		sendValue(driver, param1, param3, keysToSend, fetchMetadataVO, fetchConfigVO);
//		sendValue(driver, param5, param2, value, fetchMetadataVO, fetchConfigVO);
//		clickSignInSignOut(driver, param6, fetchMetadataVO, fetchConfigVO);
//		clickButton(driver, param6, param2, fetchMetadataVO, fetchConfigVO);
	}
	
	public void loginSSOApplication(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			String type1, String type2, String type3, String param1, String param2, String param3, String keysToSend,
			String value, CustomerProjectDto customerDetails) throws Exception {

		navigateUrl(driver, fetchConfigVO, fetchMetadataVO, customerDetails);
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[text()=\"Company Single Sign-On\"]")));
		WebElement waittill = driver.findElement(By.xpath("//button[text()=\"Company Single Sign-On\"]"));
		waittill.click();
		Thread.sleep(7000);

		String param4 = "User name or email";
		String param5 = "password";
		// String param6 = "Sign In";
		WebElement iframe = driver.findElement(By.xpath("//iframe[@title=\"TrustArc Cookie Consent Manager\"]"));
		Actions actions = new Actions(driver);
		actions.moveToElement(iframe).build().perform();
		driver.switchTo().frame(iframe);
		WebElement Acceptall = driver.findElement(By.xpath("//a[text()=\"Accept all\"]"));
		Acceptall.click();
		Thread.sleep(2000);
		String xpath1 = oicLoginPage(driver, param4, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
		String xpath2 = oicLoginPage(driver, param5, value, fetchMetadataVO, fetchConfigVO, customerDetails);
		Thread.sleep(10000);
		if (xpath2.equalsIgnoreCase(null)) {
			throw new IOException("Failed during login page");
		}
		String scripNumber = fetchMetadataVO.getScriptNumber();
		String xpath = xpath1 + ";" + xpath2;
		String scriptID = fetchMetadataVO.getScriptId();
		String lineNumber = fetchMetadataVO.getLineNumber();
		service.saveXpathParams(scriptID, lineNumber, xpath);
	}

	public synchronized void navigate(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			String type1, String type2, String param1, String param2, String param3, int count, CustomerProjectDto customerDetails)
			throws Exception {
		String param = "Navigator";
		String xpath = navigator(driver, param, fetchMetadataVO, fetchConfigVO, customerDetails);
		String xpath1 = menuNavigation(driver, param1, param2, param3, fetchMetadataVO, fetchConfigVO, customerDetails);

//		String xpath2 = menuNavigationButton(driver, fetchMetadataVO, fetchConfigVO, type1, type2, param1, param2,
//				count, customerDetails);
		String scripNumber = fetchMetadataVO.getScriptNumber();
//		String xpaths = xpath + ";" + xpath1 + ";" + xpath2;
		String xpaths = xpath + ";" + xpath1;
		String scriptID = fetchMetadataVO.getScriptId();
		String lineNumber = fetchMetadataVO.getLineNumber();
		service.saveXpathParams(scriptID, lineNumber, xpaths);
//		clickLink(driver, param3, param2, fetchMetadataVO, fetchConfigVO);
//		clickMenu(driver, param1, param2, fetchMetadataVO, fetchConfigVO);
//		clickButton(driver, param2, param2, fetchMetadataVO, fetchConfigVO);
	}

	public synchronized void openTask(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			String type1, String type2, String param1, String param2, int count, CustomerProjectDto customerDetails)
			throws Exception {
		String param3 = "Tasks";
//		clickImage(driver, param3, param2, fetchMetadataVO, fetchConfigVO);
//		clickLink(driver, param1, param2, fetchMetadataVO, fetchConfigVO);
		String xpath = task(driver, param3, fetchMetadataVO, fetchConfigVO, customerDetails);
		String xpath1 = taskMenu(driver, fetchMetadataVO, fetchConfigVO, type1, type2, param1, param2, count,
				customerDetails);
		String xpaths = xpath + ";" + xpath1;
		String scripNumber = fetchMetadataVO.getScriptNumber();
		String scriptID = fetchMetadataVO.getScriptId();
		String lineNumber = fetchMetadataVO.getLineNumber();
		service.saveXpathParams(scriptID, lineNumber, xpaths);

	}

	public void logout(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO, String type1,
			String type2, String type3, String param1, String param2, String param3, CustomerProjectDto customerDetails)
			throws Exception {

		String param4 = "UIScmil1u";
		String param5 = "Sign Out";
		String param6 = " Confirm";
		logoutDropdown(driver, fetchConfigVO, fetchMetadataVO, param1, customerDetails);
		clickSignInSignOut(driver, param6, fetchMetadataVO, fetchConfigVO, customerDetails);
	}
	
	private void clickValidateXpath(WebDriver driver, ScriptDetailsDto fetchMetadataVO, WebElement waittext,
			FetchConfigVO fetchConfigVO) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click();", waittext);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked clickValidateXpath" + scripNumber);
			// waittext.click();
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  clickValidateXpath" + scripNumber);
			e.printStackTrace();
		}
	}

	public void logoutDropdown(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			String param1, CustomerProjectDto customerDetails) throws Exception {

		try {
			Thread.sleep(4000);
			alertPopupCheck(driver);
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@id,\"UISpb1\")]")));
			WebElement waittext = driver.findElement(By.xpath("//div[contains(@id,\"UISpb1\")]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(4000);
			try {
				if (driver.findElement(By.xpath("//div[contains(@id,\"popup-container\")]//a[text()=\"Sign Out\"]"))
						.isDisplayed()) {
					WebElement signout = driver
							.findElement(By.xpath("//div[contains(@id,\"popup-container\")]//a[text()=\"Sign Out\"]"));
					signout.click();
					Thread.sleep(4000);
				}
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Successfully Logout is done " + scripNumber);
			} catch (Exception e) {
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(4000);
				WebElement signout = driver
						.findElement(By.xpath("//div[contains(@id,\"popup-container\")]//a[text()=\"Sign Out\"]"));
				signout.click();
				Thread.sleep(4000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.error("Successfully Logout is done " + scripNumber);
			}
			return;
		} catch (Exception e) {
			logger.error(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed to logout " + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void datePicker(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {

		String[] fullDate = keysToSend.split(">");
		String date = fullDate[0];
		String month = fullDate[1];
		String year = fullDate[2];

		selectYear(driver, year, fetchMetadataVO, fetchConfigVO, customerDetails);
		selectMonth(driver, month, fetchMetadataVO, fetchConfigVO, customerDetails);
		selectDate(driver, date, fetchMetadataVO, fetchConfigVO, customerDetails);

	}

	public void selectDate(WebDriver driver, String param1, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			Thread.sleep(4000);
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("//td[@data-afr-adfday=\"cm\" and text()=\"" + param1 + "\"]")));
			wait.until(ExpectedConditions
					.elementToBeClickable(By.xpath("//td[@data-afr-adfday=\"cm\" and text()=\"" + param1 + "\"]")));
			WebElement waittext = driver
					.findElement(By.xpath("//td[@data-afr-adfday=\"cm\" and text()=\"" + param1 + "\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			actions.moveToElement(waittext).click().build().perform();
			screenshot(driver, fetchMetadataVO, customerDetails);
			String xpath = "//td[@data-afr-adfday=\"cm\" and text()=\"param1\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed During Navigation");
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void selectMonth(WebDriver driver, String param1, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			WebElement waittext = driver.findElement(By.xpath("//*[text()=\"Create Time Card\"]/following::select[1]"));
			selectMethod(driver, param1, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String xpath = "//*[text()=\"Create Time Card\"]/following::select[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed During Navigation");
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void selectYear(WebDriver driver, String param1, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			WebElement wait = driver.findElement(By.xpath("//*[text()=\"Select Year\"]/preceding-sibling::input[1]"));
			String yearValue = wait.getAttribute("title");
			int year = Integer.parseInt(yearValue);
			String xpath = "//*[text()=\"Select Year\"]/preceding-sibling::input[1]";
			if (year < Integer.parseInt(param1)) {
				while (year != Integer.parseInt(param1)) {
					WebElement increment = driver.findElement(By.xpath("//a[@title=\"increment\"]"));
					increment.click();
					year = year + 1;
				}
				xpath = xpath + ";" + "//a[@title=\"increment\"]";
			} else if (year > Integer.parseInt(param1)) {
				while (year != Integer.parseInt(param1)) {
					WebElement decrement = driver.findElement(By.xpath("//a[@title=\"decrement\"]"));
					decrement.click();
					year = year - 1;
				}
				xpath = xpath + ";" + "//a[@title=\"decrement\"]";
			} else {
				logger.info("The given year is matched with the Oracle year");
			}
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed During Navigation");
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void openSettings(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			String type1, String type2, String type3, String param1, String param2, String param3, CustomerProjectDto customerDetails) throws Exception {
		String param4 = "UIScmil1u";
		clickLink(driver, param4, param3, fetchMetadataVO, fetchConfigVO, customerDetails);
		clickLink(driver, param1, param2, fetchMetadataVO, fetchConfigVO, customerDetails);
		// clickSignInSignOut(driver, param6, fetchMetadataVO, fetchConfigVO);
//		clickButton(driver, param6, param2, fetchMetadataVO, fetchConfigVO);
	}

	public void navigateUrl(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			CustomerProjectDto customerDetails) {
		try {
			driver.navigate().to(fetchConfigVO.getAPPLICATION_URL());
			driver.manage().window().maximize();
			deleteAllCookies(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			refreshPage(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			switchToActiveElement(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Successfully Navigate to the Navigate URL " + scripNumber);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("failed to do navigate URl " + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
		}
	}

	public String loginPage(WebDriver driver, String param1, String keysToSend, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {
		String xpath = null;
		try {
			if (param1.equalsIgnoreCase("password")) {
				String title1 = driver.getTitle();
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type=\"" + param1 + "\"]")));
				JavascriptExecutor jse = (JavascriptExecutor) driver;
				jse.executeScript("document.getElementById(\"password\").value = \"" + keysToSend + "\";");
				// if("password".equalsIgnoreCase(param1))
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
				String title2 = driver.getTitle();
				if (title1.equalsIgnoreCase(title2)) {
					screenshotFail(driver, fetchMetadataVO, customerDetails);
					throw new IOException("Failed during login page");
				}
				// screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Succesfully password is entered " + scripNumber);
				xpath = "//input[@type=\"param1\"]";
				return xpath;
			}
		} catch (Exception e) {
			screenshotFail(driver, fetchMetadataVO, customerDetails);

			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed to enter password " + scripNumber);
			logger.error(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("//*[contains(@placeholder,\"" + param1 + "\")]")));
			WebElement waittill = driver.findElement(By.xpath("//*[contains(@placeholder,\"" + param1 + "\")]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("arguments[0].value=\"" + keysToSend + "\";", waittill);
			// if("password".equalsIgnoreCase(param1))
			// screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			Thread.sleep(1000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			xpath = "//*[contains(@placeholder,\"param1\")]";
			logger.info("Successfully entered data " + scripNumber);
			return xpath;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Failed during login page " + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			logger.info("Failed During Login page");
		}
		return xpath;
	}

	public String navigator(WebDriver driver, String param1, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			Thread.sleep(4000);
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@title=\"" + param1 + "\"]")));
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@title=\"" + param1 + "\"]")));
			WebElement waittext = driver.findElement(By.xpath("//a[@title=\"" + param1 + "\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			actions.moveToElement(waittext).click().build().perform();
			takeScreenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Successfully navigator is done " + scripNumber);
			String xpath = "//a[@title=\"param1\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return xpath;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during navigator " + scripNumber);
			takeScreenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public String menuNavigation(WebDriver driver, String param1,String param2, String param3, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			if (param1.equalsIgnoreCase("More")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//div[contains(@id,\"popup-container\")]//a[text()=\"More...\"]")));
				wait.until(ExpectedConditions.elementToBeClickable(
						By.xpath("//div[contains(@id,\"popup-container\")]//a[text()=\"More...\"]")));
				WebElement waittext = driver
						.findElement(By.xpath("//div[contains(@id,\"popup-container\")]//a[text()=\"More...\"]"));
				Actions actions = new Actions(driver);
				Thread.sleep(3000);
//				actions.moveToElement(waittext).build().perform();
				actions.moveToElement(waittext).click().build().perform();
				Thread.sleep(4000);
				takeScreenshot(driver, fetchMetadataVO, customerDetails);
				WebElement navigate = driver
						.findElement(By.xpath("//div[contains(@id,\"popup-container\")]//span[text()=\""+param2+"\"]/following::a[text()=\""+param3+"\"]"));
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Successfully MenuNavigation is done " + scripNumber);
				String xpath = "(//*[contains(@id,\"popup-container\")]//*[@title=\""+param1+"\"])[2]";

				return xpath;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during MenuNavigation " + scripNumber);
			Thread.currentThread().interrupt();
		}

		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			
			// ------------------------(New Change)-----------------------
			
			WebElement showmore = driver
					.findElement(By.xpath("//*[contains(@id,\"popup-container\")]//a[text()=\"Show More\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(showmore).build().perform();
			actions.moveToElement(showmore).click().build().perform();
			Thread.sleep(15000);
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//*[contains(@id,\"popup-container\")]//*[@title=\"" + param1 + "\"]")));
			wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("//*[contains(@id,\"popup-container\")]//*[@title=\"" + param1 + "\"]")));
		
			WebElement waittext = driver.findElement(By.xpath(
					"//*[contains(@id,\"popup-container\")]//*[@title=\"" + param1 + "\"]//div[2]/a/*[name()=\"svg\"][1]"));

			WebElement showless = driver
					.findElement(By.xpath("//*[contains(@id,\"popup-container\")]//a[text()=\"Show Less\"]"));
			actions.moveToElement(showless).build().perform();
			actions.moveToElement(showless).click().build().perform();
			Thread.sleep(15000);
			actions.moveToElement(waittext).build().perform();
			actions.moveToElement(waittext).click().build().perform();
			Thread.sleep(15000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Successfully menunavigation is clicked " + scripNumber);
			String xpath = "//*[contains(@id,\"popup-container\")]//a[text()=\"Show More\"]" + ">"
					+ "//*[contains(@id,\"popup-container\")]//a[text()=\"Show Less\"]";
			logger.info("Successfully menunavigation is clicked " + scripNumber);
			return xpath;

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Menunavigation " + scripNumber);

			takeScreenshotFail(driver, fetchMetadataVO, customerDetails);
			logger.info("Not able to navitage to the :" + "" + param1);
			throw e;
		}
	}
	
	private void alertPopupCheck(WebDriver driver) {
		try {
	        Alert alert = driver.switchTo().alert();
	        String alertText = alert.getText();
	        logger.info("Alert data: " + alertText);
	        alert.accept();
	    } catch (NoAlertPresentException ex) {
	        ex.printStackTrace();
	    }
	}

	public String menuNavigationButton(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			String type1, String type2, String param1, String param2, int count, CustomerProjectDto customerDetails)
			throws Exception {
		String xpath = null;
		try {
			Thread.sleep(5000);
			if (param1.equalsIgnoreCase("Fixed Assets") && param2.equalsIgnoreCase("Assets")) {
				WebElement asset = driver.findElement(By.xpath(
						"//span[normalize-space(text())=\"Fixed Assets\"]/following::span[normalize-space(text())=\""
								+ param2 + "\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(asset).build().perform();
				actions.moveToElement(asset).click().build().perform();
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Successfully menuNavigationButton is done " + scripNumber);
				xpath = "//span[normalize-space(text())=\"Fixed Assets\"]/following::span[normalize-space(text())=\"param2\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Successfully menuNavigationButton is done " + scripNumber);
				return xpath;

			} else {
				// try {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"//div[@style=\"visibility: visible;\"]//span[normalize-space(text())=\"" + param2 + "\"]")));
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
						"//div[@style=\"visibility: visible;\"]//span[normalize-space(text())=\"" + param2 + "\"]")));
				WebElement waittext = driver.findElement(By
						.xpath("//div[@style=\"visibility: visible;\"]//span[normalize-space(text())=\"" + param2 + "\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				actions.moveToElement(waittext).click().build().perform();
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Successfully menuNavigationButton is done " + scripNumber);
				xpath = "//div[@style=\"visibility: visible;\"]//span[normalize-space(text())=\"param1\"]";
				logger.info("Successfully menuNavigationButton is done " + scripNumber);
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return xpath;
			}
		} catch (Exception e) {
			if (count == 0) {
				count = 1;
				logger.error(" The Count Value is : " + count);
				navigate(driver, fetchConfigVO, fetchMetadataVO, type1, type2, param1, param2, null, count, customerDetails);
			} else if (count <= 10) {
				count = count + 1;
				logger.error(" The Count Value is : " + count);
				navigate(driver, fetchConfigVO, fetchMetadataVO, type1, type2, param1, param2, null, count, customerDetails);
			} else {
				logger.error("Count value exceeds the limit " + count);
				logger.error("Failed During Navigation");
				screenshotFail(driver, fetchMetadataVO, customerDetails);
				throw e;
			}

		}
		return xpath;
	}

	public String task(WebDriver driver, String param1, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) throws Exception {
		try {
			Thread.sleep(7000);
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@title=\"" + param1 + "\"]")));
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@title=\"" + param1 + "\"]")));
			WebElement waittext = driver.findElement(By.xpath("//img[@title=\"" + param1 + "\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Successfully task is open " + scripNumber);
			String xpath = "//img[@title=\"param1\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Successfully task is open " + scripNumber);
			return xpath;

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Failed During Task " + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public String taskMenu(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			String type1, String type2, String param1, String param2, int count, CustomerProjectDto customerDetails)
			throws Exception {
		String xpath = null;
		try {
			Thread.sleep(2000);
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//div[contains(@class,\"AFVertical\")]//a[normalize-space(text())=\"" + param1 + "\"]")));
			wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("//div[contains(@class,\"AFVertical\")]//a[normalize-space(text())=\"" + param1 + "\"]")));
			WebElement waittext = driver.findElement(
					By.xpath("//div[contains(@class,\"AFVertical\")]//a[normalize-space(text())=\"" + param1 + "\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			Thread.sleep(5000);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Successfully open Task " + scripNumber);
			xpath = "//div[contains(@class,\"AFVertical\")]//a[normalize-space(text())=\"param1\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Successfully open Task " + scripNumber);
			return xpath;

		} catch (Exception e) {
			if (count == 0) {
				count = 1;
				logger.error(" The Count Value is : " + count);
				openTask(driver, fetchConfigVO, fetchMetadataVO, type1, type2, param1, param2, count, customerDetails);
			} else if (count <= 10) {
				count = count + 1;
				logger.error(" The Count Value is : " + count);
				openTask(driver, fetchConfigVO, fetchMetadataVO, type1, type2, param1, param2, count, customerDetails);
			} else {
				logger.error("Count value exceeds the limit " + count);
				logger.error("Failed to Open Task Menu");
				screenshotFail(driver, fetchMetadataVO, customerDetails);
				throw e;

			}
		}
		return xpath;
	}

	public void mediumWait(WebDriver driver, String inputData, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {
		try {
			int time = StringUtils.convertStringToInteger(inputData, 4);
			int seconds = time * 1000;
			Thread.sleep(seconds);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Successfully waited for 4 seconds " + scripNumber);
		} catch (InterruptedException e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed During meduim wait" + scripNumber);
			e.printStackTrace();
			// Restore interrupted state...
			Thread.currentThread().interrupt();
		}
	}

	public void shortwait(WebDriver driver, String inputData) {
		try {
			int time = StringUtils.convertStringToInteger(inputData, 2);
			int seconds = time * 1000;
			logger.info("Successfully shortwait");
			Thread.sleep(seconds);
		} catch (InterruptedException e) {
			logger.error("Failed During shortwait");
			e.printStackTrace();
			// Restore interrupted state...
			Thread.currentThread().interrupt();
		}
	}

	public void wait(WebDriver driver, String inputData) {
		try {
			int time = StringUtils.convertStringToInteger(inputData, 8);
			int seconds = time * 1000;
			logger.info("Successfully wait");
			Thread.sleep(seconds);
		} catch (InterruptedException e) {
			logger.error("Failed During wait");
			e.printStackTrace();
			// Restore interrupted state...
			Thread.currentThread().interrupt();
		}
	}

	public void convertJPGtoMovie(String targetFile1, List<String> targetFileList,
			List<ScriptDetailsDto> fetchMetadataListVO, FetchConfigVO fetchConfigVO, String name,
			CustomerProjectDto customerDetails) {
		String vidPath = (fetchConfigVO.getPDF_PATH() + customerDetails.getCustomerName() + "/"
				+ customerDetails.getTestSetName() + "/" + name);
		// String vidPath="C:\\Testing\\ReportWinfo\\"+name;
		String Folder = (fetchConfigVO.getPDF_PATH() + customerDetails.getCustomerName() + "/"
				+ customerDetails.getTestSetName() + "/");
		File theDir = new File(Folder);
		if (!theDir.exists()) {
			logger.info("creating directory: " + theDir.getName());
			boolean result = false;
			try {
				theDir.mkdirs();
				result = true;
			} catch (SecurityException se) {
				// handle it
				logger.error("Failed during conver JPG to Movie " + se.getMessage());
			}
		} else {
			logger.info("Folder exist");
		}
		// String vidPath = "C:\\Users\\Winfo Solutions\\Desktop\\"+name;
		OpenCVFrameConverter.ToIplImage grabberConverter = new OpenCVFrameConverter.ToIplImage();
		FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(vidPath, 1366, 614);
		String str = null;
		IplImage ipl = cvLoadImage(str);
		try {
			recorder.setFrameRate(0.33);
			recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
			recorder.setVideoBitrate(9000);
			recorder.setFormat("mp4");
			recorder.setVideoQuality(0); // maximum quality
			recorder.start();
//             for (int i=0;i<targetFileList.size();i++)
//             {
//            	 System.out.println(targetFileList.get(i));
//             }
			if (targetFile1 != null) {
				logger.info("Target File " + targetFile1);
				str = targetFile1;
				ipl = cvLoadImage(str);
				recorder.record(grabberConverter.convert(ipl));
			}
			for (String image : targetFileList) {
				str = image;
				ipl = cvLoadImage(str);
				recorder.record(grabberConverter.convert(ipl));
			}
			recorder.stop();
		} catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
			e.printStackTrace();
		}
	}

	public List<String> getImages(List<ScriptDetailsDto> fetchMetadataListVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) {
		List<String> fileNameList = new ArrayList<String>();
		File folder = new File(fetchConfigVO.getSCREENSHOT_PATH() + "\\" + customerDetails.getCustomerName() + "\\"
				+ customerDetails.getTestSetName() + "\\");
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				String fileName = listOfFiles[i].getName();
				String[] fileNameArr = fileName.split("\\.");
				String fileExt = fileNameArr[fileNameArr.length - 1];
				String[] _arr = fileName.split("_");
				String currentScriptNumber = _arr[2];
				String Status = _arr[6];
				String status = Status.split("\\.")[0];
				if ("jpg".equalsIgnoreCase(fileExt) && "Passed".equalsIgnoreCase(status)) {
					fileNameList.add(fileName);
				}
			}
		}
		return fileNameList;
	}

	public void compress(List<ScriptDetailsDto> fetchMetadataListVO, FetchConfigVO fetchConfigVO, String pdffileName,
			CustomerProjectDto customerDetails) throws IOException {
		String Folder = (fetchConfigVO.getSCREENSHOT_PATH() + "\\" + customerDetails.getCustomerName() + "\\"
				+ customerDetails.getTestSetName() + "\\");
		List<String> fileNameList = null;
		String customer_Name = customerDetails.getCustomerName();
		String test_Run_Name = customerDetails.getTestSetName();
		fileNameList = getImages(fetchMetadataListVO, fetchConfigVO, customerDetails);

		for (String image : fileNameList) {

			FileInputStream inputStream = new FileInputStream(
					fetchConfigVO.getSCREENSHOT_PATH() + "\\" + customer_Name + "\\" + test_Run_Name + "\\" + image);
			BufferedImage inputImage = ImageIO.read(inputStream);

			JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
			jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			jpegParams.setCompressionQuality(.4f);

			final ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
			// specifies where the jpg image has to be written
			writer.setOutput(new FileImageOutputStream(new File("C:\\Kaushik" + "\\" + image)));

			BufferedImage convertedImg = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(),
					BufferedImage.TYPE_INT_RGB);
			convertedImg.getGraphics().drawImage(inputImage, 0, 0, null);

			// writes the file with given compression level
			// from your JPEGImageWriteParam instance
			writer.write(null, new IIOImage(convertedImg, null, null), jpegParams);

		}

	}

	private static BufferedImage resizeImage(BufferedImage originalImage, int type) {
		final int IMG_WIDTH = 1280;
		final int IMG_HEIGHT = 960;
		BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
		g.dispose();

		return resizedImage;
	}

	private static BufferedImage resizeImageWithHint(BufferedImage originalImage, int type) {
		final int IMG_WIDTH = 1280;
		final int IMG_HEIGHT = 768;
		BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
		g.dispose();
		g.setComposite(AlphaComposite.Src);

		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		return resizedImage;
	}

	public void copy(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) throws Exception {
		try {
			Robot r = new Robot();
			r.keyPress(KeyEvent.VK_CONTROL);
			r.keyPress(KeyEvent.VK_C);
			r.keyRelease(KeyEvent.VK_C);
			r.keyRelease(KeyEvent.VK_CONTROL);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Successfully Copy is done " + scripNumber);

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Copy " + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			e.printStackTrace();
			throw e;
		}
	}// input[@placeholder=\"Enter search terms\"]

	public void paste(WebDriver driver, String inputParam, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, String globalValueForSteps, CustomerProjectDto customerDetails)
			throws Exception {
		try {
			if (inputParam.equalsIgnoreCase("Transaction")) {
				WebElement waittill = driver
						.findElement(By.xpath("//span[text()=\"" + inputParam + "\"]/following::input[1]"));
				String testParamId = fetchMetadataVO.getTestScriptParamId();
				String testSetId = fetchMetadataVO.getTestSetLineId();
				String copynumberValue;
				String inputValue = fetchMetadataVO.getInputValue();

				String[] arrOfStr = inputValue.split(">", 5);
				if (arrOfStr.length < 2) {
					copynumberValue = inputValue;
				} else {
					String Testrun_name = arrOfStr[0];
					String seq = arrOfStr[1];
					// String Script_num=arrOfStr[2];
					String line_number = arrOfStr[2];
					copynumberValue = dynamicnumber.getCopynumber(Testrun_name, seq, line_number, testParamId,
							testSetId);
				}

				String value = globalValueForSteps;

				// String value = copynumber(driver, inputParam1, inputParam2, fetchMetadataVO,
				// fetchConfigVO)

				waittill.click();

				JavascriptExecutor jse = (JavascriptExecutor) driver;

				jse.executeScript("arguments[0].value=\"" + copynumberValue + "\";", waittill);

				Thread.sleep(3000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//span[text()=\"inputParam\"]/following::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;

			}

		} catch (Exception e) {

			logger.error("Failed during Paste value " + e.getMessage());

		}

		// DH 39
		try {
			if (inputParam.equalsIgnoreCase("Query By Example")) {
				// WebElement waittill = driver.findElement(
				// By.xpath("//h1[text()=\"" + inputParam +
				// "\"]/following::input[@placeholder=\"Search\"]"));
				// to get Dynamic copynumber
				Thread.sleep(5000);
				WebElement waittill = driver
						.findElement(By.xpath("//*[@title=\"" + inputParam + "\"]/following::input[1]"));

				String testParamId = fetchMetadataVO.getTestScriptParamId();
				String testSetId = fetchMetadataVO.getTestSetLineId();
				String copynumberValue;
				String inputValue = fetchMetadataVO.getInputValue();

				String[] arrOfStr = inputValue.split(">", 5);
				if (arrOfStr.length < 2) {
					copynumberValue = inputValue;
				} else {
					String Testrun_name = arrOfStr[0];
					String seq = arrOfStr[1];
					// String Script_num=arrOfStr[2];
					String line_number = arrOfStr[2];
					copynumberValue = dynamicnumber.getCopynumber(Testrun_name, seq, line_number, testParamId,
							testSetId);
				}

				String value = globalValueForSteps;
				Thread.sleep(2000);
				waittill.click();
				JavascriptExecutor jse = (JavascriptExecutor) driver;
				jse.executeScript("arguments[0].value=\"" + copynumberValue + "\";", waittill);
				Thread.sleep(3000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Successfully paste is done " + scripNumber);
				String xpath = "//*[@title=\"inputParam\"]/following::input[1]";

				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				// service.saveXpathParams(inputParam,"",scripNumber,xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Paste Method");
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}

		try {
			if (inputParam.equalsIgnoreCase("Notifications")) {
				WebElement waittill = driver.findElement(
						By.xpath("//h1[text()=\"" + inputParam + "\"]/following::input[@placeholder=\"Search\"]"));
				// to get Dynamic copynumber
				String testParamId = fetchMetadataVO.getTestScriptParamId();
				String testSetId = fetchMetadataVO.getTestSetLineId();
				String copynumberValue;
				String inputValue = fetchMetadataVO.getInputValue();

				String[] arrOfStr = inputValue.split(">", 5);
				if (arrOfStr.length < 2) {
					copynumberValue = inputValue;
				} else {
					String Testrun_name = arrOfStr[0];
					String seq = arrOfStr[1];
					// String Script_num=arrOfStr[2];
					String line_number = arrOfStr[2];
					copynumberValue = dynamicnumber.getCopynumber(Testrun_name, seq, line_number, testParamId,
							testSetId);
				}

				String value = globalValueForSteps;
				// Thread.sleep(2000);

				waittill.click();
				JavascriptExecutor jse = (JavascriptExecutor) driver;
				jse.executeScript("arguments[0].value=\"" + copynumberValue + "\";", waittill);
				Thread.sleep(3000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Successfully paste is done " + scripNumber);
				String xpath = "//h1[text()=\"inputParam\"]/following::input[@placeholder=\"Search\"]";

				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				// service.saveXpathParams(inputParam,"",scripNumber,xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Paste Method");
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
		try {

			WebElement waittill = driver

					.findElement(By.xpath("//label[text()=\"" + inputParam + "\"]/following::input[1]"));

			// to get Dynamic copynumber
			String testParamId = fetchMetadataVO.getTestScriptParamId();
			String testSetId = fetchMetadataVO.getTestSetLineId();
			String copynumberValue;
			String inputValue = fetchMetadataVO.getInputValue();

			String[] arrOfStr = inputValue.split(">", 5);
			if (arrOfStr.length < 2) {
				copynumberValue = inputValue;
			} else {
				String Testrun_name = arrOfStr[0];
				String seq = arrOfStr[1];
				// String Script_num=arrOfStr[2];
				String line_number = arrOfStr[2];
				copynumberValue = dynamicnumber.getCopynumber(Testrun_name, seq, line_number, testParamId, testSetId);
			}

			String value = globalValueForSteps;

//	          String value = copynumber(driver, inputParam1, inputParam2, fetchMetadataVO, fetchConfigVO)

			waittill.click();

			JavascriptExecutor jse = (JavascriptExecutor) driver;

			jse.executeScript("arguments[0].value=\"" + copynumberValue + "\";", waittill);

			/*
			 * 
			 * Actions action = new Actions(driver);
			 * 
			 * action.click(waittill).build().perform();
			 * 
			 * action.doubleClick(waittill).build().perform();
			 * 
			 * action.sendKeys(value).build().perform();
			 * 
			 */

			Thread.sleep(3000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//label[text()=\"inputParam\"]/following::input[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

//				service.saveXpathParams(inputParam,"",scripNumber,xpath);

			return;

		} catch (Exception e) {

			logger.error("Failed during Paste value " + e.getMessage());

		}

		try {

			WebElement waittill = driver.findElement(By.xpath("//input[@placeholder=\"" + inputParam + "\"]"));

			// to get Dynamic copynumber
			String testParamId = fetchMetadataVO.getTestScriptParamId();
			String testSetId = fetchMetadataVO.getTestSetLineId();
			String copynumberValue;
			String inputValue = fetchMetadataVO.getInputValue();

			String[] arrOfStr = inputValue.split(">", 5);
			if (arrOfStr.length < 2) {
				copynumberValue = inputValue;
			} else {
				String Testrun_name = arrOfStr[0];
				String seq = arrOfStr[1];
				// String Script_num=arrOfStr[2];
				String line_number = arrOfStr[2];
				copynumberValue = dynamicnumber.getCopynumber(Testrun_name, seq, line_number, testParamId, testSetId);
			}
			String value = globalValueForSteps;
			waittill.click();

			JavascriptExecutor jse = (JavascriptExecutor) driver;

			jse.executeScript("arguments[0].value=\"" + copynumberValue + "\";", waittill);

			Thread.sleep(3000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Successfully paste is done " + scripNumber);
			String xpath = "//input[@placeholder=\"inputParam\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

//		service.saveXpathParams(inputParam,"",scripNumber,xpath);

			return;

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Paste Method");
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;

		}

	}

	public void clear(WebDriver driver, String inputParam1, String inputParam2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {

		try {
			if (inputParam1.equalsIgnoreCase("Lines")) {
				WebElement waittill = driver.findElement(By.xpath("(//*[normalize-space(text())=\"" + inputParam1
						+ "\"]/following::label[normalize-space(text())=\"" + inputParam2
						+ "\"]/preceding-sibling::input)[1]"));
				clearMethod(driver, waittill);
				Thread.sleep(4000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//*[normalize-space(text())=\"inputParam1\"]/following::label[normalize-space(text())=\"inputParam2\"]/preceding-sibling::input)[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during clear " + e.getMessage());
		}
		try {
			if (inputParam2.equals("Accounting Period")) {
				Thread.sleep(4000);
				WebElement waittill = driver.findElement(
						By.xpath("//label[normalize-space(text())=\"" + inputParam2 + "\"]/preceding-sibling::input[1]"));
				clearMethod(driver, waittill);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Successfully Accounting Period Cleared" + scripNumber);
				String xpath = "//label[normalize-space(text())=\"inputParam2\"]/preceding-sibling::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed During Accounting Period Clear" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebElement waittill = driver.findElement(
					By.xpath("(//label[contains(text(),\"" + inputParam1 + "\")]/preceding-sibling::input)[1]"));
			clearMethod(driver, waittill);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Successfully Cleared" + scripNumber);
			String xpath = "(//label[contains(text(),\"inputParam1\")]/preceding-sibling::input)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed During Clear" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			Thread.sleep(4000);
			WebElement waittill = driver
					.findElement(By.xpath("(//*[normalize-space(text())=\"" + inputParam1 + "\"]/following::input)[1]"));
			clearMethod(driver, waittill);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Successfully Cleared" + scripNumber);
			String xpath = "(//*[normalize-space(text())=\"inputParam1\"]/following::input)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed During Clear" + scripNumber);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("//*[contains(@placeholder,\"" + inputParam1 + "\")]"));
			clearMethod(driver, waittill);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Successfully Cleared" + scripNumber);
			String xpath = "//*[contains(@placeholder,\"inputParam1\")]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed During Clear" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebElement waittill = driver
					.findElement(By.xpath("//*[normalize-space(text())=\"" + inputParam1 + "\"]/following::textarea[1]"));
			clearMethod(driver, waittill);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Successfully Cleared" + scripNumber);
			String xpath = "//*[normalize-space(text())=\"inputParam1\"]/following::textarea[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed During Clear" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			logger.error(e.getMessage());
			throw e;
		}
	}

	public void windowclose(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) {
		try {
			String mainWindow = driver.getWindowHandle();
			// It returns no. of windows opened by WebDriver and will return Set of Strings
			Set<String> set = driver.getWindowHandles();
			// Using Iterator to iterate with in windows
			Iterator<String> itr = set.iterator();
			while (itr.hasNext()) {
				String childWindow = itr.next();
				// Compare whether the main windows is not equal to child window. If not
				// equal,we will close.
				if (!mainWindow.equals(childWindow)) {
					driver.switchTo().window(childWindow);
					driver.manage().window().maximize();
					driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
					driver.switchTo().window(childWindow);
					driver.close();
					driver.switchTo().window(mainWindow);
					String scripNumber = fetchMetadataVO.getScriptNumber();
					logger.info("Successfully Windowclosed" + scripNumber);
				}
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed During WindowClose Acion." + scripNumber);
			screenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void switchToActiveElement(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) {
		try {
			driver.switchTo().activeElement();
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Switched to Element Successfully" + scripNumber);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed During switchToActiveElement Action." + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void clickMenu(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			if (param1.equalsIgnoreCase("PDF")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath(("(//div[normalize-space(text())=\"" + param1 + "\"])[2]"))));
				// wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(("(//div[text()=\""
				// + param1 + "\"])[1]")), param1));
				WebElement waittext = driver
						.findElement(By.xpath(("(//div[normalize-space(text())=\"" + param1 + "\"])[2]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(80000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String params = param1;
				String xpath = "(//div[normalize-space(text())=\"param1\"])[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully clicked Element in clickmenu " + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("failed during ClickMenu " + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@title=\"" + param1 + "\"]")));
			Thread.sleep(4000);
			WebElement waittext = driver.findElement(By.xpath("//div[@title=\"" + param1 + "\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String params = param1;
			String xpath = "//div[@title=\"param1\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully clicked Element in clickmenu " + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("failed during ClickMenu " + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath(("//a[normalize-space(text())=\"" + param1 + "\"]"))));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath(("//a[normalize-space(text())=\"" + param1 + "\"]")), param1));
			WebElement waittext = driver.findElement(By.xpath(("//a[normalize-space(text())=\"" + param1 + "\"]")));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();

			String xpath = "//a[normalize-space(text())=\"param1\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully clicked Element in clickmenu " + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("failed during ClickMenu " + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					("//div[contains(@style,\"display: block\")]//div[normalize-space(text())=\"" + param1 + "\"]"))));
			WebElement waittext = driver.findElement(By.xpath(
					("//div[contains(@style,\"display: block\")]//div[normalize-space(text())=\"" + param1 + "\"]")));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();

			String xpath = "//div[contains(@style,\"display: block\")]//div[normalize-space(text())=\"param1\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully clicked Element in clickmenu " + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();

			logger.error("failed during ClickMenu " + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath(("//div[normalize-space(text())=\"" + param1 + "\"]"))));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath(("//div[normalize-space(text())=\"" + param1 + "\"]")), param1));
			WebElement waittext = driver.findElement(By.xpath(("//div[normalize-space(text())=\"" + param1 + "\"]")));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//div[normalize-space(text())=\"param1\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully clicked Element in clickmenu " + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("failed during ClickMenu " + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("(//div[contains(@id,\"" + param1 + "\")])[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//div[contains(@id,\"" + param1 + "\")])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String params = param1;
			String xpath = "(//div[contains(@id,\"param1\")])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully clicked Element in clickmenu " + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error(e.getMessage());
			logger.error("failed during ClickMenu " + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void clickSignInSignOut(WebDriver driver, String param1, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath(("//button[normalize-space(normalize-space(text())=\"" + param1 + "\")]"))));
			WebElement waittext = driver
					.findElement(By.xpath(("//button[normalize-space(normalize-space(text())=\"" + param1 + "\")]")));
			screenshot(driver, fetchMetadataVO, customerDetails);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully clicked SingnInSignOut" + scripNumber);
			String xpath = "//button[normalize-space(normalize-space(text())=\"param1\")]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error(e.getMessage());
			logger.error("Failed during SingnInSignOut " + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}// *[text()=\"Action Required\"]/following::a[1]

	public void clickNotificationLink(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {

		// placeholder changed from \"Enter Search Terms to Search\" in Fusion Instance

		
		try {
			if (param1.equalsIgnoreCase("Action Required:")) {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//a[contains(@title,\"" + param1 + "\")]")));
			Thread.sleep(4000);
			WebElement waittext = driver
					.findElement(By.xpath("//a[contains(@title,\"" + param1 + "\")]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			Thread.sleep(2000);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Successfully Clicked NotificationLink" + scripNumber);
			String params = param1;
			String xpath = "//a[contains(@title,'Action Required:')]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed During NotificationLink" + scripNumber);
			logger.error(e.getMessage());
		}
		
		try {
			if (param1.equalsIgnoreCase("Notifications") && param2.equalsIgnoreCase("Search")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::input[@placeholder=\"" + param2 + "\"]/following::a[1]")));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::input[@placeholder=\"" + param2 + "\"]/following::a[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(2000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Successfully Clicked NotificationLink" + scripNumber);
				String params = param1;
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::input[@placeholder=\"param2\"]/following::a[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed During NotificationLink" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::a[1]")));
			Thread.sleep(4000);
			WebElement waittext = driver
					.findElement(By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::a[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			Thread.sleep(2000);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Successfully Clicked NotificationLink" + scripNumber);
			String params = param1;
			String xpath = "//*[normalize-space(text())=\"param1\"]/following::a[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed During NotificationLink" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath(("//*[@placeholder=\"" + param1 + "\"]/following::a[1]"))));
			Thread.sleep(4000);
			WebElement waittext = driver.findElement(By.xpath("//*[@placeholder=\"" + param1 + "\"]/following::a[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			Thread.sleep(2000);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Successfully Clicked NotificationLink" + scripNumber);
			String params = param1;
			String xpath = "//*[@placeholder=\"param1\"]/following::a[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error(e.getMessage());
			logger.error("Failed during NotificationLink" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void clickButtonDropdown(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@title=\"" + param1 + "\"]")));
				WebElement waittext = driver.findElement(By.xpath("//a[@title=\"" + param1 + "\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				clickButtonDropdownText(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//a[@title=\"param1\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Successfully Clicked ClickButtonDropdown" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed During ClickButtonDropdown " + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("//h1[contains(text(),\"" + param1 + "\")]/following::a[1]")));
				WebElement waittext = driver
						.findElement(By.xpath("//h1[contains(text(),\"" + param1 + "\")]/following::a[1]"));
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				screenshot(driver, fetchMetadataVO, customerDetails);
				clickButtonDropdownText(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//h1[contains(text(),\"param1\")]/following::a[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Successfully Clicked ClickButtonDropdown" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed During clickButtonDropdown " + scripNumber);
			logger.error(e.getMessage());

		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"(//h1[normalize-space(text())=\"" + param1 + "\"]/following::a[@title=\"" + param2 + "\"])[1]")));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath(("//h1[normalize-space(text())=\"" + param1 + "\"]")), param1));
			Thread.sleep(6000);
			WebElement waittext = driver.findElement(By.xpath(
					"(//h1[normalize-space(text())=\"" + param1 + "\"]/following::a[@title=\"" + param2 + "\"])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			clickButtonDropdownText(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//h1[normalize-space(text())=\"param1\"]/following::a[@title=\"param2\"])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Successfully Clicked ClickButtonDropdown" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed During clickButtonDropdown " + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param2.equalsIgnoreCase("Publish to Managers")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[normalize-space(text())=\""
						+ param1 + "\"]/following::a[normalize-space(text())=\"" + param2 + "\"])[2]")));
				WebElement waittext = driver.findElement(By.xpath("(//h1[normalize-space(text())=\"" + param1
						+ "\"]/following::a[normalize-space(text())=\"" + param2 + "\"])[2]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				clickButtonDropdownText(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//h1[normalize-space(text())=\"param1\"]/following::a[normalize-space(text())=\"param2\"])[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Successfully Clicked ClickButtonDropdown" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed During clickButtonDropdown " + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[normalize-space(text())=\"" + param1
					+ "\"]/following::a[normalize-space(text())=\"" + param2 + "\"])[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//h1[normalize-space(text())=\"" + param1
					+ "\"]/following::a[normalize-space(text())=\"" + param2 + "\"])[1]"));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			clickButtonDropdownText(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//h1[normalize-space(text())=\"param1\"]/following::a[normalize-space(text())=\"param2\"])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Successfully Clicked ClickButtonDropdown" + scripNumber);
			return;
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed During clickButtonDropdown " + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void clickButtonDropdownText(WebDriver driver, String param1, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("//li[normalize-space(text())=\"" + keysToSend + "\"]")));
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("//li[normalize-space(text())=\"" + keysToSend + "\"]")));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath(("//li[normalize-space(text())=\"" + keysToSend + "\"]")), keysToSend));
			Thread.sleep(5000);
			WebElement waittext = driver.findElement(By.xpath("//li[normalize-space(text())=\"" + keysToSend + "\"]"));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Successfully Clicked ClickButtonDropdownText" + scripNumber);
			String xpath = "//li[normalize-space(text())=\"keysToSend\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed During clickButtonDropdownText " + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"//div[contains(@class,\"PopupMenuContent\")]//td[normalize-space(text())=\"" + keysToSend + "\"]")));
			WebElement waittext = driver.findElement(By.xpath(
					"//div[contains(@class,\"PopupMenuContent\")]//td[normalize-space(text())=\"" + keysToSend + "\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Successfully Clicked ClickButtonDropdownText" + scripNumber);
			String xpath = "//div[contains(@class,\"PopupMenuContent\")]//td[normalize-space(text())=\"keysToSend\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed During clickButtonDropdownText " + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("//td[normalize-space(text())=\"" + keysToSend + "\"]")));
			WebElement waittext = driver.findElement(By.xpath("//td[normalize-space(text())=\"" + keysToSend + "\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Successfully Clicked ClickButtonDropdownText" + scripNumber);
			String xpath = "//td[normalize-space(text())=\"keysToSend\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed During clickButtonDropdownText " + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void clickExpandorcollapse(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			if (param1.equalsIgnoreCase("Process Monitor")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"(//h2[normalize-space(text())=\"" + param1 + "\"]/preceding::*[@title=\"" + param2 + "\"])[1]")));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(
						By.xpath("//h2[normalize-space(text())=\"" + param1 + "\"]"), param1));
				WebElement waittext = driver.findElement(By.xpath(
						"(//h2[normalize-space(text())=\"" + param1 + "\"]/preceding::*[@title=\"" + param2 + "\"])[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(4000);
				try {
					WebElement Expand = driver.findElement(By.xpath("(//h2[normalize-space(text())=\"" + param1
							+ "\"]/following::*[@title=\"" + param2 + "\"])[1]"));
					Expand.click();
					String scripNumber = fetchMetadataVO.getScriptNumber();
					logger.info("Sucessfully Clicked Expanded or Collapsed" + scripNumber);

				} catch (Exception e) {

					String scripNumber = fetchMetadataVO.getScriptNumber();
					logger.error("Failed During ClickExpand or Collapse" + scripNumber);
				}

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//h1[normalize-space(text())=\"param1\"]/preceding::*[@title=\"param2\"])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked Process Monitor ClickExpand or Collapse" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed During Process Monitor ClickExpand or Collapse" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"(//h2[normalize-space(text())=\"" + param1 + "\"]/following::*[@title=\"" + param2 + "\"])[1]")));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath("//h2[normalize-space(text())=\"" + param1 + "\"]"), param1));
			WebElement waittext = driver.findElement(By.xpath(
					"(//h2[normalize-space(text())=\"" + param1 + "\"]/following::*[@title=\"" + param2 + "\"])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			Thread.sleep(4000);
			try {
				WebElement Expand = driver.findElement(By.xpath(
						"(//h2[normalize-space(text())=\"" + param1 + "\"]/following::*[@title=\"" + param2 + "\"])[1]"));
				Expand.click();
			} catch (Exception e) {

			}
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked ClickExpand or Collapse" + scripNumber);
			String xpath = "(//h2[normalize-space(text())=\"param1\"]/following::*[@title=\"param2\"])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			screenshot(driver, fetchMetadataVO, customerDetails);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed During ClickExpand or Collapse" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"(//h1[normalize-space(text())=\"" + param1 + "\"]/preceding::*[@title=\"" + param2 + "\"])[1]")));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath("//h1[normalize-space(text())=\"" + param1 + "\"]"), param1));
			WebElement waittext = driver.findElement(By.xpath(
					"(//h1[normalize-space(text())=\"" + param1 + "\"]/preceding::*[@title=\"" + param2 + "\"])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			Thread.sleep(4000);
			try {
				WebElement Expand = driver.findElement(By.xpath(
						"(//h1[normalize-space(text())=\"" + param1 + "\"]/preceding::*[@title=\"" + param2 + "\"])[1]"));
				Expand.click();
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked ClickExpand or Collapse" + scripNumber);

			} catch (Exception e) {
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.error("Failed During ClickExpand or Collapse" + scripNumber);

			}
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked ClickExpand or Collapse" + scripNumber);
			String xpath = "(//h1[normalize-space(text())=\"param1\"]/preceding::*[@title=\"param2\"])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed During ClickExpand or Collapse" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			Thread.sleep(4000);
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"(//span[contains(text(),\"" + param1 + "\")])[1]/preceding::a[3][@title=\"" + param2 + "\"][1]")));
			WebElement waittext = driver.findElement(By.xpath(
					"(//span[contains(text(),\"" + param1 + "\")])[1]/preceding::a[3][@title=\"" + param2 + "\"][1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			Thread.sleep(1000);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked ClickExpand or Collapse" + scripNumber);
			String xpath = "(//span[contains(text(),\"param1\")])[1]/preceding::a[3][@title=\"param2\"][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed During ClickExpand or Collapse" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"(//*[normalize-space(text())=\"" + param1 + "\"]/following::*[@title=\"" + param2 + "\"])[1]")));
			WebElement waittext = driver.findElement(By
					.xpath("(//*[normalize-space(text())=\"" + param1 + "\"]/following::*[@title=\"" + param2 + "\"])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			String xpath = "(//*[normalize-space(text())=\"param1\"]/following::*[@title=\"param2\"])[1]";
			Thread.sleep(4000);
			try {
				WebElement Expand = driver.findElement(By.xpath(
						"(//*[normalize-space(text())=\"" + param1 + "\"]/following::*[@title=\"" + param2 + "\"])[1]"));
				Expand.click();
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked ClickExpand or Collapse" + scripNumber);
				xpath = xpath + ";" + "(//*[normalize-space(text())=\"param1\"]/following::*[@title=\"param2\"])[1]";
			} catch (Exception e) {
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.error("Failed During ClickExpand or Collapse" + scripNumber);
			}
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked ClickExpand or Collapse" + scripNumber);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed During ClickExpand or Collapse" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"(//*[normalize-space(text())=\"" + param1 + "\"]/preceding::*[@title=\"" + param2 + "\"])[1]")));
			WebElement waittext = driver.findElement(By
					.xpath("(//*[normalize-space(text())=\"" + param1 + "\"]/preceding::*[@title=\"" + param2 + "\"])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			String xpath = "(//*[normalize-space(text())=\"param1\"]/preceding::*[@title=\"param2\"])[1]";
			Thread.sleep(4000);
			try {
				WebElement Expand = driver.findElement(By.xpath(
						"(//*[normalize-space(text())=\"" + param1 + "\"]/preceding::*[@title=\"" + param2 + "\"])[1]"));
				Expand.click();
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked ClickExpand or Collapse" + scripNumber);
				xpath = xpath + ";" + "(//*[normalize-space(text())=\"param1\"]/preceding::*[@title=\"param2\"])[1]";
			} catch (Exception e) {
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.error("Failed During ClickExpand or Collapse" + scripNumber);

			}
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked ClickExpand or Collapse" + scripNumber);
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			screenshot(driver, fetchMetadataVO, customerDetails);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed During ClickExpand or Collapse" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//*[text()=\"" + param1 + "\"]/preceding::a[@title=\"" + param2 + "\"]")));
			WebElement waittext = driver
					.findElement(By.xpath("//*[text()=\"" + param1 + "\"]/preceding::a[@title=\"" + param2 + "\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			Thread.sleep(4000);
			try {
				WebElement Expand = driver
						.findElement(By.xpath("//*[text()=\"" + param1 + "\"]/preceding::a[@title=\"" + param2 + "\"]"));
				Expand.click();
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked ClickExpand or Collapse" + scripNumber);
			} catch (Exception e) {
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.error("Failed During ClickExpand or Collapse" + scripNumber);

			}
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked ClickExpand or Collapse" + scripNumber);
			String xpath = "(//*[normalize-space(text())=\"param1\"]/preceding::*[@title=\"param2\"])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			screenshot(driver, fetchMetadataVO, customerDetails);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed During ClickExpand or Collapse" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"//*[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\"" + param2
							+ "\"]/preceding::*[@title=\"Expand\" and @href and not(@style=\"display:none\")][1]")));
			WebElement waittext = driver.findElement(
					By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
							+ param2 + "\"]/preceding::*[@title=\"Expand\" and @href and not(@style=\"display:none\")][1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			String xpath = "//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]/preceding::*[@title=\"Expand\" and @href and not(@style=\"display:none\")][1]";
			Thread.sleep(4000);
			try {
				WebElement Expand = driver.findElement(By.xpath(
						"//*[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\"" + param2
								+ "\"]/preceding::*[@title=\"Expand\" and @href and not(@style=\"display:none\")][1]"));
				Expand.click();
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked ClickExpand or Collapse" + scripNumber);
				xpath = xpath + ";"
						+ "//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]/preceding::*[@title=\"Expand\" and @href and not(@style=\"display:none\")][1]";
			} catch (Exception e) {
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.error("Failed During ClickExpand or Collapse" + scripNumber);

			}
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked ClickExpand or Collapse" + scripNumber);
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			screenshot(driver, fetchMetadataVO, customerDetails);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed During ClickExpand or Collapse" + scripNumber);
			logger.error(e.getMessage());
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void selectAValue(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {
		try {

			if (param1.equalsIgnoreCase("Locations")) {
				Thread.sleep(5000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(),\"" + param1
						+ "\")]/following::*[normalize-space(text())=\"" + keysToSend + "\"][1]")));
				WebElement waittext = driver.findElement(By.xpath("//*[contains(text(),\"" + param1
						+ "\")]/following::*[normalize-space(text())=\"" + keysToSend + "\"][1]"));
				Thread.sleep(2000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked selectAValue" + scripNumber);
				String xpath = "//*[contains(text(),\"param1\")]/following::*[normalize-space(text())=\"keysToSend\"][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;

			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during selectAValue" + scripNumber);
			logger.error(e.getMessage());
		}
		// DH 46
		try {
			if (param1.equalsIgnoreCase("shopByCategoryPopup")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@data-afr-popupid,\""
						+ param1 + "\")]//*[contains(normalize-space(text()),\"" + keysToSend + "\")][1]")));
				WebElement waittext = driver.findElement(By.xpath("//*[contains(@data-afr-popupid,\"" + param1
						+ "\")]//*[contains(normalize-space(text()),\"" + keysToSend + "\")][1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(2000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked selectAValue" + scripNumber);
				String xpath = "//*[contains(@data-afr-popupid,\"param1\")]//*[contains(normalize-space(text()),\"keysToSend\")][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during selectAValue" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
		}

		// DH 39
		try {
			if (param1.equalsIgnoreCase("Assignment Number")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(),\"" + param1
						+ "\")]/following::a[text()=\"" + keysToSend + "\" and not(@style)]")));
				WebElement waittext = driver.findElement(By.xpath("//*[contains(text(),\"" + param1
						+ "\")]/following::a[text()=\"" + keysToSend + "\" and not(@style)]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				Thread.sleep(2000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Review installments selectAValue" + scripNumber);
				String xpath = "//*[contains(text(),\"param1\")]/following::a[text()=\"keysToSend\" and not(@style)]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Review installments selectAValue" + scripNumber);
			logger.error(e.getMessage());
		}

		// DH 32
		try {
			if (param1.equalsIgnoreCase("Plan")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(),\"" + param1
						+ "\")]/following::*[normalize-space(text())=\"" + keysToSend + "\"]//following::span[1]")));
				WebElement waittext = driver.findElement(By.xpath("//*[contains(text(),\"" + param1
						+ "\")]/following::*[normalize-space(text())=\"" + keysToSend + "\"]//following::span[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(2000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Review installments selectAValue" + scripNumber);
				String xpath = "//*[contains(text(),\"param1\")]/following::*[normalize-space(text())=\"keysToSend\"]//following::span[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Review installments selectAValue" + scripNumber);
			logger.error(e.getMessage());
		}

		try {
			if (param1.equalsIgnoreCase("Review installments") || param1.equalsIgnoreCase("Review proposed payments")
					|| param1.equalsIgnoreCase("Record print status")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"//*[normalize-space(text())=\"" + keysToSend + "\"]/following::img[@title=\"" + param1 + "\"]")));
				WebElement waittext = driver.findElement(By.xpath(
						"//*[normalize-space(text())=\"" + keysToSend + "\"]/following::img[@title=\"" + param1 + "\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(2000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Review installments selectAValue" + scripNumber);
				String xpath = "//*[normalize-space(text())=\"keysToSend\"]/following::img[@title=\"param1\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Review installments selectAValue" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Payment Process Requests") && param1.equalsIgnoreCase("Name")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"(//*[contains(text(),\"" + param2 + "\")]/following::*[normalize-space(text())=\"" + keysToSend
								+ "\"][1]/following::img/following::a[contains(@id,\"RecentlyCompletedPpr\")])[2]")));
				WebElement waittext = driver.findElement(By.xpath(
						"(//*[contains(text(),\"\" + param2 + \"\")]/following::*[normalize-space(text())=\"\" + keysToSend + \"\"][1]/following::img/following::a[contains(@id,\"RecentlyCompletedPpr\")])[2]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(2000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Review installments selectAValue" + scripNumber);
				String xpath = "(//*[contains(text(),\"param2\")]/following::*[normalize-space(text())=\"keysToSend\"][1]/following::img/following::a[contains(@id,\"RecentlyCompletedPpr\")])[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Review installments selectAValue" + scripNumber);
			logger.error(e.getMessage());
		}
		// DH 15
		try {
			if (param1.equalsIgnoreCase("Absences") && param2.equalsIgnoreCase("Absence Type")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::span[text()=\"" + keysToSend
								+ "\"]/following::img[contains(@title,\"" + param2 + "\")][1]")));
				WebElement waittext = driver
						.findElement(By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::span[text()=\""
								+ keysToSend + "\"]/following::img[contains(@title,\"" + param2 + "\")][1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(2000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked selectAValue" + scripNumber);
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::span[text()=\"keysToSend\"]/following::img[contains(@title,\"param2\")][1]";
				// service.saveXpathParams(param1, param2, scripNumber, xpath);
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during selectAValue" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			// throw e;
		}
		try {
			if (param1.equalsIgnoreCase("Existing Absences") && param2.equalsIgnoreCase("Action")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By
						.xpath("//*[contains(text(),\"" + keysToSend + "\")]/following::*[@title=\"" + param2 + "\"][1]")));
				WebElement waittext = driver.findElement(By
						.xpath("//*[contains(text(),\"" + keysToSend + "\")]/following::*[@title=\"" + param2 + "\"][1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(2000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked selectAValue" + scripNumber);
				String xpath = "//*[contains(text(),\"keysToSend\")]/following::*[@title=\"param2\"][1]";
				// service.saveXpathParams(param1, param2, scripNumber, xpath);
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during selectAValue" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			// throw e;
		}
		// DH
		try {
			Thread.sleep(5000);
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(),\"" + param1
					+ "\")]/following::span[normalize-space(text())=\"" + keysToSend + "\"][1]")));
			WebElement waittext = driver.findElement(By.xpath("//*[contains(text(),\"" + param1
					+ "\")]/following::span[normalize-space(text())=\"" + keysToSend + "\"][1]"));
			Thread.sleep(2000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			Thread.sleep(5000);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked selectAValue" + scripNumber);
			String xpath = "//*[contains(text(),\"param1\")]/following::span[normalize-space(text())=\"keysToSend\"][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during selectAValue" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			Thread.sleep(5000);
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(),\"" + param1
					+ "\")]/following::*[normalize-space(text())=\"" + keysToSend + "\"][1]")));
			WebElement waittext = driver.findElement(By.xpath("//*[contains(text(),\"" + param1
					+ "\")]/following::*[normalize-space(text())=\"" + keysToSend + "\"][1]"));
			Thread.sleep(2000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			Thread.sleep(5000);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked selectAValue" + scripNumber);
			String xpath = "//*[contains(text(),\"param1\")]/following::*[normalize-space(text())=\"keysToSend\"][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during selectAValue" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]")));
			WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			Thread.sleep(2000);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked selectAValue" + scripNumber);
			String xpath = "//*[normalize-space(text())=\"param1\"]/following::label[normalize-space(text())=\"param2\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during selectAValue" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())=\"" + keysToSend
					+ "\"]/following::*[normalize-space(text())=\"" + param1 + "\"]")));
			WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + keysToSend
					+ "\"]/following::*[normalize-space(text())=\"" + param1 + "\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			Thread.sleep(2000);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked selectAValue" + scripNumber);
			String xpath = "//*[normalize-space(text())=\"keysToSend\"]/following::*[normalize-space(text())=\"param1\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during selectAValue" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}

	}

	public void oicClickMenu(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {

		try {
			if (param1.equalsIgnoreCase("Monitoring") && param2.equalsIgnoreCase("Integrations")) {
				Thread.sleep(3000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver.findElement(By.xpath(("(//span[text()=\"" + param2 + "\"])[2]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).perform();
				screenshot(driver, fetchMetadataVO, customerDetails);
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("arguments[0].click();", waittext);
				Thread.sleep(15000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath = "(//span[text()=\"param2\"])[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			Thread.sleep(3000);
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			WebElement waittext = driver.findElement(By.xpath(("//span[text()=\"" + param1 + "\"]")));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).perform();
			screenshot(driver, fetchMetadataVO, customerDetails);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click();", waittext);
			Thread.sleep(15000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
			String xpath = "//span[text()=\"param1\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public String clickTableImage(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
							+ keysToSend + "\"]/following::img[contains(@id,\"" + param2 + "\")][1]")));
			Thread.sleep(4000);
			WebElement waittill = driver.findElement(
					By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
							+ keysToSend + "\"]/following::img[contains(@id,\"" + param2 + "\")][1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			Thread.sleep(2000);
			screenshot(driver, fetchMetadataVO, customerDetails);
			clickValidateXpath(driver, fetchMetadataVO, waittill, fetchConfigVO, customerDetails);
			Thread.sleep(500);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"keysToSend\"]/following::img[contains(@id,\"param2\")][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked clickTableImage" + scripNumber);
			return keysToSend;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickTableImage" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
							+ keysToSend + "\"]/following::img[@title=\"" + param2 + "\"][1]")));
			WebElement waittill = driver.findElement(
					By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
							+ keysToSend + "\"]/following::img[@title=\"" + param2 + "\"][1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			Thread.sleep(2000);
			screenshot(driver, fetchMetadataVO, customerDetails);
			clickValidateXpath(driver, fetchMetadataVO, waittill, fetchConfigVO, customerDetails);
			Thread.sleep(500);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"keysToSend\"]/following::img[@title=\"param2\"][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked clickTableImage" + scripNumber);
			return keysToSend;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickTableImage" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::*[@value=\"" + keysToSend + "\"]/following::img[@title=\"" + param2 + "\"][1]")));
			WebElement waittill = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::*[@value=\"" + keysToSend + "\"]/following::img[@title=\"" + param2 + "\"][1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			Thread.sleep(2000);
			screenshot(driver, fetchMetadataVO, customerDetails);
			clickValidateXpath(driver, fetchMetadataVO, waittill, fetchConfigVO, customerDetails);
			Thread.sleep(500);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[normalize-space(text())=\"param1\"]/following::*[@value=\"keysToSend\"]/following::img[@title=\"param2\"][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked clickTableImage" + scripNumber);
			return keysToSend;
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickTableImage" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void clickImage(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {

		try {
			if (param1.equalsIgnoreCase("Life Cycle")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[@title=\"" + param1 + "\"])[1]")));
				WebElement waittext = driver.findElement(By.xpath("(//*[@title=\"" + param1 + "\"])[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				logger.info("Successfully clicked the Image " + param1);
				String xpath = "(//*[@title=\"" + param1 + "\"])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during click Image " + e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Create")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@title=\"" + param1 + "\"]")));
				WebElement waittext = driver.findElement(By.xpath("//img[@title=\"" + param1 + "\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				logger.info("Successfully clicked the Image " + param1);
				String xpath = "//img[@title='Create']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during click Image " + e.getMessage());
		}
		// prod
		try {
			if (param2.equalsIgnoreCase("General Journals Report")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[contains(text(),\"" + param1
						+ "\")]/following::img[@class=\"promptComboBoxButtonMoz\"])[1]")));
				WebElement waittext = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1
						+ "\")]/following::img[@class=\"promptComboBoxButtonMoz\"])[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				String xpath = "(//*[contains(text(),\"param1\")]/following::img[@class=\"promptComboBoxButtonMoz\"])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during click Image " + e.getMessage());
		}
		// prod
		try {
			if (param2.equalsIgnoreCase("General Journals Report")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By
						.xpath("(//*[text()=\"" + param1 + "\"]/following::img[@class=\"promptComboBoxButtonMoz\"])[1]")));
				WebElement waittext = driver.findElement(
						By.xpath("(//*[text()=\"" + param1 + "\"]/following::img[@class=\"promptComboBoxButtonMoz\"])[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				String xpath = "(//*[text()=\"param1\"]/following::img[@class=\"promptComboBoxButtonMoz\"])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during click Image " + e.getMessage());
		}
		// DH 43
		try {
			if (param1.equalsIgnoreCase("Plan Balances") || param1.equalsIgnoreCase("Existing Absences")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[text()=\"" + param1 + "\"]/following::div[@role=\"button\"][1]")));
				WebElement waittext = driver
						.findElement(By.xpath("//*[text()=\"" + param1 + "\"]/following::div[@role=\"button\"][1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				String xpath = "//*[text()=\"param1\"]/following::div[@role=\"button\"][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during click Image " + e.getMessage());
		}

		// DH 15
		try {
			if (param2.equalsIgnoreCase("Back")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//h1[normalize-space(text())=\"" + param1 + "\"]/preceding::a[1]")));
				WebElement waittext = driver
						.findElement(By.xpath("//h1[normalize-space(text())=\"" + param1 + "\"]/preceding::a[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				String xpath = "//h1[normalize-space(text())=\"param1\"]/preceding::a[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during click Image " + e.getMessage());
		}

		// Dh 9
		try {

			if (param1.equalsIgnoreCase("move")) {

				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				wait.until(ExpectedConditions.presenceOfElementLocated(

						By.xpath("(//a[contains(@id,\"" + param1 + "\")])[1]")));

				WebElement waittext = driver.findElement(

						By.xpath("(//a[contains(@id,\"" + param1 + "\")])[1]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittext).build().perform();

				waittext.click();

				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);

				String scripNumber = fetchMetadataVO.getScriptNumber();

				logger.info("Sucessfully Clicked clickImage" + scripNumber);

				String xpath = "(//a[contains(@id,\"param1\")])[1]";

				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;

			}

		} catch (Exception e) {

			String scripNumber = fetchMetadataVO.getScriptNumber();

			logger.error("Failed during clickImag" + scripNumber);

			logger.error(e.getMessage());

		}

		// DH 19
		try {
			if (param1.equalsIgnoreCase("Existing Absences") && param2.equalsIgnoreCase("Add")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By
						.xpath("(//h1[normalize-space(text())=\"" + param1 + "\"]/following::div[@role=\"button\"])[1]")));
				Thread.sleep(2000);
				WebElement waittext = driver.findElement(
						By.xpath("(//h1[normalize-space(text())=\"" + param1 + "\"]/following::div[@role=\"button\"])[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(3000);
				WebElement add = driver.findElement(By.xpath(
						"//h1[normalize-space(text())=\"" + param1 + "\"]/following::span[text()=\"" + param2 + "\"]"));
				clickValidateXpath(driver, fetchMetadataVO, add, fetchConfigVO, customerDetails);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked clickImage" + scripNumber);
				String xpath1 = "(//h1[normalize-space(text())=\"param1\"]/following::div[@role=\"button\"])[1]";
				String xpath2 = "//h1[normalize-space(text())=\"param1\"]/following::span[text()=\"param2\"]";
				String xpath = xpath1 + ";" + xpath2;
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				// service.saveXpathParams(param1, param2, scripNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickImag" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equals("Republish")) {
				Thread.sleep(3000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver.findElement(By.xpath("//img[contains(@title,\"" + param1 + "\")]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked clickImage" + scripNumber);
				String params = param1;
				String xpath = "//img[contains(@title,\"param1\")]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickImag" + scripNumber);
			logger.error(e.getMessage());
		}

		try {
			if (param1.equalsIgnoreCase("Provider") || param1.equalsIgnoreCase("Receiver")) {
				Thread.sleep(4000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"//*[normalize-space(text())=\"" + param1 + "\"]/following::img[@title=\"" + param2 + "\"][2]")));
				WebElement waittext = driver.findElement(By.xpath(
						"//*[normalize-space(text())=\"" + param1 + "\"]/following::img[@title=\"" + param2 + "\"][2]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Provider or Receiver clickImage" + scripNumber);
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::img[@title=\"param2\"][2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Provider or Receiver clickImag" + scripNumber);
			logger.error(e.getMessage());
		}

		try {
			if (param2.equalsIgnoreCase("Add Row")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[contains(text(),\"" + param1 + "\")]/following::img[@title=\"" + param2 + "\"][1]")));
				WebElement waittext = driver.findElement(
						By.xpath("//*[contains(text(),\"" + param1 + "\")]/following::img[@title=\"" + param2 + "\"][1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked clickImage" + scripNumber);
				String xpath = "//*[contains(text(),\"param1\")]/following::img[@title=\"param2\"][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickImag" + scripNumber);
			logger.error(e.getMessage());
		}
		// label[contains(text(),\"Enter Cost Centre\")]/following::input[1]
		try {
			if (param1.equalsIgnoreCase("Report")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[contains(text(),\"" + param2 + "\")/following::input[1]]")));
				WebElement waittext = driver
						.findElement(By.xpath("//*[contains(text(),\"" + param2 + "\")/following::input[1]]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Report clickImage" + scripNumber);
				String xpath = "//*[contains(text(),\"param2\")/following::input[1]]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Report clickImag" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@title=\"" + param1 + "\"]")));
				WebElement waittext = driver.findElement(By.xpath("//img[@title=\"" + param1 + "\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(8000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked clickImage" + scripNumber);
				String params = param1;
				String xpath = "//img[@title=\"param1\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickImag" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Customer")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By
						.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::a[@title=\"" + param2 + "\"]")));
				WebElement waittext = driver.findElement(By
						.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::a[@title=\"" + param2 + "\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Customer clickImage" + scripNumber);
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::a[@title=\"param2\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Clicked clickImag" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Add to Selected") || param1.equalsIgnoreCase("Remove from Selected")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(
						ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@title=\"" + param1 + "\"]//img[1]")));
				WebElement waittext = driver.findElement(By.xpath("//a[@title=\"" + param1 + "\"]//img[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Add to Selected clickImage" + scripNumber);
				String xpath = "//a[@title=\"param1\"]//img[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Add to Selected clickImag" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param2.equalsIgnoreCase("Go to Member Selection")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By
						.xpath("//*[contains(text(),\"" + param1 + "\")]/following::input[@title=\"" + param2 + "\"][1]")));
				WebElement waittext = driver.findElement(By
						.xpath("//*[contains(text(),\"" + param1 + "\")]/following::input[@title=\"" + param2 + "\"][1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Go to Member Selection clickImage" + scripNumber);
				String xpath = "//*[contains(text(),\"param1\")]/following::input[@title=\"param2\"][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Go to Member Selection clickImag" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(
						ExpectedConditions.presenceOfElementLocated(By.xpath("//img[contains(@id,\"" + param1 + "\")]")));
				WebElement waittext = driver.findElement(By.xpath("//img[contains(@id,\"" + param1 + "\")]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked clickImage" + scripNumber);
				String params = param1;
				String xpath = "//img[contains(@id,\"param1\")]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickImag" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				Thread.sleep(3000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@title=\"" + param1 + "\"]")));
				WebElement waittext = driver.findElement(By.xpath("//a[@title=\"" + param1 + "\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked clickImage" + scripNumber);
				String params = param1;
				String xpath = "//a[@title=\"param1\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickImag" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param2.equalsIgnoreCase("Back")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//h1[normalize-space(text())=\"" + param1 + "\"]/preceding::a[1]")));
				WebElement waittext = driver
						.findElement(By.xpath("//h1[normalize-space(text())=\"" + param1 + "\"]/preceding::a[1]"));
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Back clickImage" + scripNumber);
				String xpath = "//h1[normalize-space(text())=\"param1\"]/preceding::a[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Back clickImag" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("(//h1[normalize-space(text())=\"" + param1 + "\"]/following::div[@role=\"button\"])[1]")));
			Thread.sleep(2000);
			WebElement waittext = driver.findElement(
					By.xpath("(//h1[normalize-space(text())=\"" + param1 + "\"]/following::div[@role=\"button\"])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			Thread.sleep(3000);
			WebElement add = driver.findElement(
					By.xpath("//h1[normalize-space(text())=\"" + param1 + "\"]/following::img[@title=\"" + param2 + "\"]"));
			clickValidateXpath(driver, fetchMetadataVO, add, fetchConfigVO, customerDetails);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked clickImage" + scripNumber);
			String xpath1 = "(//h1[normalize-space(text())=\"param1\"]/following::div[@role=\"button\"])[1]";
			String xpath2 = "//h1[normalize-space(text())=\"param1\"]/following::img[@title=\"param2\"]";
			String xpath = xpath1 + ";" + xpath2;
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickImag" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"(//h1[normalize-space(text())=\"" + param1 + "\"]/following::img[@title=\"" + param2 + "\"])[1]")));
			WebElement waittext = driver.findElement(By.xpath(
					"(//h1[normalize-space(text())=\"" + param1 + "\"]/following::img[@title=\"" + param2 + "\"])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked clickImage" + scripNumber);
			String xpath = "(//h1[normalize-space(text())=\"param1\"]/following::img[@title=\"param2\"])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickImag" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"//*[normalize-space(text())=\"" + param1 + "\"]/following::img[@title=\"" + param2 + "\"][1]")));
			WebElement waittext = driver.findElement(By.xpath(
					"//*[normalize-space(text())=\"" + param1 + "\"]/following::img[@title=\"" + param2 + "\"][1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			Thread.sleep(8000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked clickImage" + scripNumber);
			String xpath = "//*[normalize-space(text())=\"param1\"]/following::img[@title=\"param2\"][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickImag" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("(//*[normalize-space(text())=\"" + param1 + "\"]/following::div[@role=\"button\"])[1]")));
			WebElement waittext = driver.findElement(
					By.xpath("(//*[normalize-space(text())=\"" + param1 + "\"]/following::div[@role=\"button\"])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			Thread.sleep(3000);
			WebElement add = driver.findElement(
					By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::img[@title=\"" + param2 + "\"]"));
			clickValidateXpath(driver, fetchMetadataVO, add, fetchConfigVO, customerDetails);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked clickImage" + scripNumber);
			String xpath1 = "(//*[normalize-space(text())=\"param1\"]/following::div[@role=\"button\"])[1]";
			String xpath2 = "//*[normalize-space(text())=\"param1\"]/following::img[@title=\"param2\"]";
			String xpath = xpath1 + ";" + xpath2;
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickImag" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"//*[normalize-space(text())=\"" + param1 + "\"]/following::img[contains(@id,\"" + param2 + "\")]")));
			WebElement waittext = driver.findElement(By.xpath(
					"//*[normalize-space(text())=\"" + param1 + "\"]/following::img[contains(@id,\"" + param2 + "\")]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			waittext.click();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(2000);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked clickImage" + scripNumber);
			String xpath = "//*[normalize-space(text())=\"param1\"]/following::img[contains(@id,\"param2\")]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickImag" + scripNumber);
			logger.error(e.getMessage());
		}

		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::img[1]")));
			WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::img[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			waittext.click();
			// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked clickImage" + scripNumber);
			String xpath = "//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]/following::img[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickImag" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("//*[contains(@aria-label,\"" + param1 + "\")]")));
			WebElement waittext = driver.findElement(By.xpath("//*[contains(@aria-label,\"" + param1 + "\")]"));
			Actions actions = new Actions(driver);
			waittext.click();
			actions.moveToElement(waittext).build().perform();
			// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked clickImage" + scripNumber);
			String xpath = "//*[contains(@aria-label,\"param1\")]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickImag" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void clickButtonCheckPopup(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			if (param1.equalsIgnoreCase("Manage") || (param1.equalsIgnoreCase("Award"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver
						.findElement(By.xpath(("//div[contains(@class,\"PopupMenu\")]//*[text()=\"" + param1 + "\"]")));// screenshot(driver,
				// "",
				// fetchMetadataVO,
				// fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				Thread.sleep(15000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath = "//a[@accessKey=\"m\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if ("yes".equalsIgnoreCase(fetchMetadataVO.getConditionalPopup())) {
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				logger.info("alertText clicked ");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clickButton(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {

		
		try {
			if (param1.equalsIgnoreCase("D")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver.findElement(By.xpath(("//span[text()=\"" + param1 + "\"]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				Thread.sleep(15000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked  clickButton" + scripNumber);
				String xpath = "//span[text()='D']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);
			logger.error(e.getMessage());
		}
		
		try {
			if (param1.equalsIgnoreCase("Primary Mailing") && param2.equalsIgnoreCase("Edit")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"(//*[text()=\"" + param1 + "\"]/following::*[text()=\"" + param2 + "\"]/following::a)[1]")));
				WebElement waittext = driver.findElement(By
						.xpath("(//*[text()=\"" + param1 + "\"]/following::*[text()=\"" + param2 + "\"]/following::a)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(6000);
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Republish clickButton" + scripNumber);
				String xpath = "(//*[text()=\"param1\"]/following::*[text()=\"param2\"]/following::a)[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Republish clickButton" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Search...")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver.findElement(
						By.xpath(("//div[@class=\"masterMenu DropDownSearch\" and @style=\"display: block;\"]/span[text()=\""
								+ param1 + "\"]")));// screenshot(driver,
				// "",
				// fetchMetadataVO,
				// fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				Thread.sleep(15000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath = "//div[@class=\"masterMenu DropDownSearch\" and @style=\"display: block;\"]/span[text()=\"param1\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);
			logger.error(e.getMessage());
		}

		// HCM.ADM.1132 HS2 (click button)
		try {
			if (param1.equalsIgnoreCase("Send")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver.findElement(By.xpath(("//button[text()=\"Sen\"]")));// screenshot(driver,
				// "",
				// fetchMetadataVO,
				// fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				Thread.sleep(15000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath = "//button[text()=\"Sen\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);
			logger.error(e.getMessage());
		}

		// DH 57

		try {
			if (param1.equalsIgnoreCase("Warning") && param2.equalsIgnoreCase("Yes")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath(("//table[contains(@id,\"warningPopup\")]//*[text()=\"" + param1
								+ "\"]/following::*[text()=\"" + param2 + "\"]"))));
				WebElement waittext = driver.findElement(By.xpath(("//table[contains(@id,\"warningPopup\")]//*[text()=\""
						+ param1 + "\"]/following::*[text()=\"" + param2 + "\"]")));
				// screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(15000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Create Time Card clickButton" + scripNumber);
				String xpath = "//table[contains(@id,\"warningPopup\")]//*[text()=\"param1\"]/following::*[text()=\"param2\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Create Time Card clickButton" + scripNumber);
			logger.error(e.getMessage());
		}

		// Dh 39
		try {
			if (param1.equalsIgnoreCase("Generate Schedules")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver.findElement(By.xpath("//span[text()=\"enerate Schedules\"]"));// screenshot(driver,
				// "",
				// fetchMetadataVO,
				// fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				Thread.sleep(2000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath = "//span[text()=\"enerate Schedules\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);

			logger.error(e.getMessage());
		}

		// Dh 39
		try {
			if (param1.equalsIgnoreCase("Warning") && param2.equalsIgnoreCase("Yes")) {
				Thread.sleep(3000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				WebElement waittext = driver.findElement(By.xpath(("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\" and @type=\"button\"][1]")));// screenshot(driver,
				// "",
				// fetchMetadataVO,
				// fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				// Thread.sleep(15000);
				Thread.sleep(2000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\" and @type=\"button\"][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);

			logger.error(e.getMessage());
		}

		// Dh 39
		try {
			if (param1.equalsIgnoreCase("Addresses") && param2.equalsIgnoreCase("Edit")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver.findElement(
						By.xpath(("//h2[text()=\"" + param1 + "\"]/following::*[text()=\"" + param2 + "\"][1]")));// screenshot(driver,
				// "",
				// fetchMetadataVO,
				// fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				Thread.sleep(15000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath = "//h2[text()=\"param1\"]/following::*[text()=\"param2\"][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);

			logger.error(e.getMessage());
		}

		// DH 31
		try {

			if (param1.equalsIgnoreCase("Create Baseline")) {

				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				WebElement waittext = driver.findElement(By.xpath(("//*[text()=\"aseline\"]")));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittext).build().perform();

				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);

				Thread.sleep(15000);

				String scripNumber = fetchMetadataVO.getScriptNumber();

				logger.info("Sucessfully Clicked Create Baseline clickButton" + scripNumber);

				String xpath = "//*[text()=\"aseline\"]";

				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;

			}

		} catch (Exception e) {

			String scripNumber = fetchMetadataVO.getScriptNumber();

			logger.error("Failed during clickButton" + scripNumber);

			logger.error(e.getMessage());

		}

		// DH 31

		try {

			if (param1.equalsIgnoreCase("Search")) {

				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				WebElement waittext = driver.findElement(By.xpath(("//*[text()=\"Sea\"]")));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittext).build().perform();

				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);

				Thread.sleep(15000);

				String scripNumber = fetchMetadataVO.getScriptNumber();

				logger.info("Sucessfully Clicked Search clickButton" + scripNumber);

				String xpath = "//*[text()=\"Sea\"]";

				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;

			}

		} catch (Exception e) {

			String scripNumber = fetchMetadataVO.getScriptNumber();

			logger.error("Failed during clickButton" + scripNumber);

			logger.error(e.getMessage());

		}

		// New code for PTP.PO.511
		// DH 29
		try {
			if (param1.equalsIgnoreCase("Submit")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver.findElement(By.xpath(("//a[@accessKey=\"m\"]")));// screenshot(driver,
				// "",
				// fetchMetadataVO,
				// fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				Thread.sleep(15000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath = "//a[@accessKey=\"m\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);
			logger.error(e.getMessage());
		}

		// DH 29
		try {
			if (param1.equalsIgnoreCase("Approval") || param1.equalsIgnoreCase("Respond to Questionnaire")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver.findElement(By.xpath(("//div[contains(@id,\"popup-container\")]//td[text()=\""
						+ param1 + "\"]//preceding-sibling::td[1]")));// screenshot(driver,
				// "",
				// fetchMetadataVO,
				// fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				Thread.sleep(15000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath = "//div[contains(@id,\"popup-container\")]//td[text()=\"param1\"]//preceding-sibling::td[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);

			logger.error(e.getMessage());
		}

		// DH 29
		try {
			if (param1.equalsIgnoreCase("Initiate") || param1.equalsIgnoreCase("Supplier")
					|| param1.equalsIgnoreCase("Internal")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver.findElement(
						By.xpath(("(//div[contains(@class,\"PopupMenuContent\")])[2]//td[text()=\"" + param1 + "\"]")));// screenshot(driver,
				// "",
				// fetchMetadataVO,
				// fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(15000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath = "(//div[contains(@class,\"PopupMenuContent\")])[2]//td[text()=\"param1\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);

			logger.error(e.getMessage());
		}

		// DH 10
		try {

			if (param2.equalsIgnoreCase("Done")) {

				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				WebElement waittext = driver

						.findElement(By.xpath("//div[contains(@id,\"popup-container\")]//*[text()=\"" + param1
								+ "\"]/following::button[text()=\"" + param2 + "\"])[1]"));

				// //screenshot(driver, "", fetchMetadataVO, fetchConfigVO);

				Actions actions = new Actions(driver);

				actions.moveToElement(waittext).build().perform();

				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);

				Thread.sleep(1000);

				String scripNumber = fetchMetadataVO.getScriptNumber();

				logger.info("Sucessfully Clicked Done Button" + scripNumber);

				String xpath = "//div[contains(@id,\"popup-container\")]//*[text()=\"param1\"]/following::button[text()=\"param2\"])[1]";

				// service.saveXpathParams(param1, param2, scripNumber, xpath);
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;

			}

		} catch (Exception e) {

			String scripNumber = fetchMetadataVO.getScriptNumber();

			logger.error("Failed during clicking Done Button" + scripNumber);

			logger.error(e.getMessage());

		}

		// DH 9
		try {

			if (param1.equalsIgnoreCase("Save and Close")) {

				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				WebElement waittext = driver.findElement(By.xpath(("//button[text()=\"ave and Close\"]")));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittext).build().perform();

				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);

				Thread.sleep(15000);

				String scripNumber = fetchMetadataVO.getScriptNumber();

				logger.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);

				String xpath = "//button[text()=\"ave and Close\"]";

				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;

			}

		} catch (Exception e) {

			String scripNumber = fetchMetadataVO.getScriptNumber();

			logger.error("Failed during clickButton" + scripNumber);

			logger.error(e.getMessage());

		}

		// Action: Click Button
		// DH 22
		try {

			if (param1.equalsIgnoreCase("Update Address") && param2.equalsIgnoreCase("OK")) {

				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				WebElement waittext = driver
						.findElement(By.xpath(("(//div[contains(@id,\"popup-container\")]//button[@accesskey=\"K\"])[2]")));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittext).build().perform();

				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);

				Thread.sleep(15000);

				String scripNumber = fetchMetadataVO.getScriptNumber();

				logger.info("Sucessfully Clicked OK clickButton" + scripNumber);

				String xpath = "(//div[contains(@id,\"popup-container\")]//button[@accesskey=\"K\"])[2]";

				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;

			}

		} catch (Exception e) {

			String scripNumber = fetchMetadataVO.getScriptNumber();

			logger.error("Failed during clickButton" + scripNumber);

			logger.error(e.getMessage());

		}
		try {

			if (param2.equalsIgnoreCase("OK")) {

				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				WebElement waittext = driver
						.findElement(By.xpath(("(//div[contains(@id,\"popup-container\")]//button[@accesskey=\"O\"])[1]")));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittext).build().perform();

				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);

				Thread.sleep(15000);

				String scripNumber = fetchMetadataVO.getScriptNumber();

				logger.info("Sucessfully Clicked OK clickButton" + scripNumber);

				String xpath = "(//div[contains(@id,\"popup-container\")]//button[@accesskey=\"O\"])[1]";

				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;

			}

		} catch (Exception e) {

			String scripNumber = fetchMetadataVO.getScriptNumber();

			logger.error("Failed during clickButton" + scripNumber);

			logger.error(e.getMessage());

		}

		// Action: Click Button

		try {

			if (param2.equalsIgnoreCase("OK")) {

				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				WebElement waittext = driver
						.findElement(By.xpath(("(//div[contains(@id,\"popup-container\")]//button[@accesskey=\"K\"])[1]")));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittext).build().perform();

				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);

				Thread.sleep(15000);

				String scripNumber = fetchMetadataVO.getScriptNumber();

				logger.info("Sucessfully Clicked OK clickButton" + scripNumber);

				String xpath = "(//div[contains(@id,\"popup-container\")]//button[@accesskey=\"K\"])[1]";

				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;

			}

		} catch (Exception e) {

			String scripNumber = fetchMetadataVO.getScriptNumber();

			logger.error("Failed during clickButton" + scripNumber);

			logger.error(e.getMessage());

		}

		// Action: Click Button

		try {

			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

			WebElement waittext = driver
					.findElement(By.xpath(("//div[contains(@class,\"PopupMenu\")]//*[text()=\"" + param1 + "\"]")));

			Actions actions = new Actions(driver);

			actions.moveToElement(waittext).build().perform();

			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);

			Thread.sleep(15000);

			String scripNumber = fetchMetadataVO.getScriptNumber();

			logger.info("Sucessfully Clicked OK clickButton" + scripNumber);

			String xpath = "//div[contains(@class,\"PopupMenu\")]//*[text()=\"param1\"]";

			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;

		} catch (Exception e) {

			String scripNumber = fetchMetadataVO.getScriptNumber();

			logger.error("Failed during clickButton" + scripNumber);

			logger.error(e.getMessage());

		}

		// Dh changes 7

		try {
			if (param1.equalsIgnoreCase("Finish")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver.findElement(By.xpath(("//span[text()=\"i\"][1]")));
				// screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(3000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Create Time Card clickButton" + scripNumber);
				String xpath = "//span[text()=\"i\"][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Create Time Card clickButton" + scripNumber);
		}

		try {

			if (param1.equalsIgnoreCase("Applied Receipts Register") && param2.equalsIgnoreCase("Apply")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver.findElement(By.xpath("//input[@value=\"Apply\"]"));
//                screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(15000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Create Time Card clickButton" + scripNumber);
				String xpath = "//input[@value=\"Apply\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed clickButton" + scripNumber);

			logger.error(e.getMessage());
		}
		// DH changes 6
		try {
			if (param1.equalsIgnoreCase("Issue Refund")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver
						.findElement(By.xpath("//div[text()=\"" + param1 + "\"]/following::*[text()=\"K\"]"));
				// //screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Create Time Card clickButton" + scripNumber);
				String xpath = "//div[text()=\"param1\"]/following::*[text()=\"K\"]";
				// service.saveXpathParams(param1, param2, scripNumber, xpath);
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Issue Refund ok clickButton" + scripNumber);

			logger.error(e.getMessage());
		}
		// DH changes 6
		try {
			if (param1.equalsIgnoreCase("Create Bank Account")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver
						.findElement(By.xpath("//*[text()=\"" + param1 + "\"]/following::*[text()=\"S\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Create Time Card clickButton" + scripNumber);
				String xpath = "//*[text()=\"param1\"]/following::*[text()=\"S\"]";
				// service.saveXpathParams(param1, param2, scripNumber, xpath);
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Issue Refund ok clickButton" + scripNumber);

			logger.error(e.getMessage());
		}
		try {
			if ((param2.equalsIgnoreCase("Save and Close") && param1.equalsIgnoreCase("Manage Expenditure Types"))
					|| param1.equalsIgnoreCase("Manage Rate Schedules") || param2.equalsIgnoreCase("Save and Close")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver.findElement(By.xpath(("(//span[text()=\"S\"])[2]")));
//                screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(15000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Create Time Card clickButton" + scripNumber);
				String xpath = "(//span[text()=\"S\"])[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Create Time Card clickButton" + scripNumber);

			logger.error(e.getMessage());
		}
		// DH fix 4
		try {
			// Changed == to equals method
			if (param2.equalsIgnoreCase("OK")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(3000);
				// wait.until(ExpectedConditions
				// .presenceOfElementLocated(By.xpath(("//div[text()=\"" + param1 +
				// "\"]/following::button[text()=\"" + param2 + "\"][1]"))));
				WebElement waittext = driver.findElement(
						By.xpath(("//div[text()=\"" + param1 + "\"]/following::button[text()=\"" + param2 + "\"][1]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
				// screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked clickButton" + scripNumber);
				String xpath = "//div[text()=\"param1\"]/following::button[text()=\"param2\"][1]";
				// service.saveXpathParams(param1, param2, scripNumber, xpath);
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);
			logger.error(e.getMessage());
		}

		// DH fix 4
		try {
			if (param1.equalsIgnoreCase("Transactions")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver
						.findElement(By.xpath(("//*[text()=\"" + param1 + "\"]/following::span[text()=\"o\"]")));
				// //screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Create Time Card clickButton" + scripNumber);
				String xpath = "//*[text()=\"param1\"]/following::span[text()=\"o\"]";
				// service.saveXpathParams(param1, param2, scripNumber, xpath); return;
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Create Time Card clickButton" + scripNumber);
			logger.error(e.getMessage());
		}

		// DH fix4
		try {
			if (param1.equalsIgnoreCase("Accounts")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver.findElement(
						By.xpath("//*[contains(text(),\"" + param1 + "\")]/following::td[text()=\"" + param2 + "\"][2]"));
				// //screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Create Time Card clickButton" + scripNumber);
				String xpath = "//*[contains(text(),\"param1\")]/following::td[text()=\"param2\"][2]";
				// service.saveXpathParams(param1, param2, scripNumber, xpath);
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Create Time Card clickButton" + scripNumber);

			logger.error(e.getMessage());
		}
		// for "PTP.PO.212 Split requisition lines" when exectuing in Fusion instance
		try {
			if (param1.equalsIgnoreCase("Create Address") && param2.equalsIgnoreCase("OK")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(8000);
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//*[normalize-space(text())=\"Create Address\"]/following::*[text()=\"K\"][1]"))));
				WebElement waittext = driver.findElement(
						By.xpath(("//*[normalize-space(text())=\"Create Address\"]/following::*[text()=\"K\"][1]")));
//                screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				Thread.sleep(4000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(4000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Ok clickButton" + scripNumber);
				String xpath = "//*[normalize-space(text())=\"Create Address\"]/following::*[text()=\"K\"][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Ok clickButton" + scripNumber);
		}

		try {
			if (param1.equalsIgnoreCase("Submit")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver.findElement(By.xpath(("//*[text()=\"Submit\" or text()=\"S\"]")));// screenshot(driver,
																											// "",
																											// fetchMetadataVO,
																											// fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(15000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Create Time Card clickButton" + scripNumber);
				String xpath = "//*[text()=\"Submit or text()=\"S\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);

			logger.error(e.getMessage());
		}
//for "PTP.AP.327 Applying a prepayment to Invoice" when exectuing in Fusion instance
		try {
			if (param2.equalsIgnoreCase("Save and Close") && !param1.equalsIgnoreCase("Unapply Application")
					&& !param1.equalsIgnoreCase("Manage Distributions") && !param1.equalsIgnoreCase("Manage Holds")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver.findElement(By.xpath(("//span[text()=\"S\"]")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(15000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Create Time Card clickButton" + scripNumber);
				String xpath = "//span[text()=\"S\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Create Time Card clickButton" + scripNumber);
		}
		try {
			if ((param1.equalsIgnoreCase("Manage Organization Trees")
					|| param1.equalsIgnoreCase("Edit Project Template")
					|| param1.equalsIgnoreCase("Manage Project Templates")) && param2.equalsIgnoreCase("Done")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("(//span[text()=\"o\"])[2]"))));

				WebElement waittext = driver.findElement(By.xpath(("(//span[text()=\"o\"])[2]")));
//                screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);

				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Done clickButton" + scripNumber);
				logger.info("Sucessfully Clicked Done clickButton" + scripNumber);
				String xpath = "(//span[text()=\"o\"])[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton Done" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Done")) {
				Thread.sleep(3000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(5000);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[text()=\"o\"]"))));
				WebElement waittext = driver.findElement(By.xpath(("//span[text()=\"o\"]")));
//                screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Done clickButton" + scripNumber);
				logger.info("Sucessfully Clicked Done clickButton" + scripNumber);
				String xpath = "//span[text()=\"o\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton Done" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Notifications")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//h1[normalize-space(text())=\"Notifications\"]/following::button[text()=\"" + param2
								+ "\"][1]"))));
				WebElement waittext = driver.findElement(
						By.xpath(("//h1[normalize-space(text())=\"Notifications\"]/following::button[text()=\"" + param2
								+ "\"][1]")));
				// screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(15000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Members clickButton" + scripNumber);
				String xpath = "//h1[normalize-space(text())=\"Notifications\"]/following::button[text()=\"param2\"][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Members clickButton" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Expend")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver
						.findElement(By.xpath(("//div[contains(@class,\"Overflow\")]//div[@role=\"button\"]")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(15000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Create Time Card clickButton" + scripNumber);
				String xpath = "//div[contains(@class,\"Overflow\")]//div[@role=\"button\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Create Time Card clickButton" + scripNumber);

			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Create Time Card") && param2.equalsIgnoreCase("OK")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//*[text()=\"Create Time Card\"]/following::span[text()=\"K\"]"))));
				WebElement waittext = driver
						.findElement(By.xpath(("//*[text()=\"Create Time Card\"]/following::span[text()=\"K\"]")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(15000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Create Time Card clickButton" + scripNumber);
				String xpath = "//*[text()=\"Create Time Card\"]/following::span[text()=\"K\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Create Time Card clickButton" + scripNumber);

			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Edit Line")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(8000);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						("//*[contains(text(),\"" + param1 + "\")]/following::span[normalize-space(text())=\"K\"]"))));
				WebElement waittext = driver.findElement(By.xpath(
						("//*[contains(text(),\"" + param1 + "\")]/following::span[normalize-space(text())=\"K\"]")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				Thread.sleep(4000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(4000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Edit Line clickButton" + scripNumber);
				String xpath = "//*[contains(text(),\"param1\")]/following::span[normalize-space(text())=\"K\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Edit Line clickButton" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Members") || param1.equalsIgnoreCase("Complete Report")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(
						ExpectedConditions.presenceOfElementLocated(By.xpath(("//button[@title=\"" + param2 + "\"]"))));
				WebElement waittext = driver.findElement(By.xpath(("//button[@title=\"" + param2 + "\"]")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(15000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Members clickButton" + scripNumber);
				String xpath = "//button[@title=\"param2\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Members clickButton" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Address Contacts") && param2.equalsIgnoreCase("OK")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"//*[normalize-space(text())=\"" + param1 + "\"]/following::button[@title=\"" + param2 + "\"]")));
				WebElement waittext = driver.findElement(By.xpath(
						"//*[normalize-space(text())=\"" + param1 + "\"]/following::button[@title=\"" + param2 + "\"]"));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Address Contacts clickButton" + scripNumber);
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::button[@title=\"param2\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Address Contacts clickButton" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			Thread.sleep(2000);
			if (param2.equalsIgnoreCase("OK")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(8000);
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//div[contains(@id,\"RejectPopup::content\")]//span[text()=\"K\"]"))));
				// wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[text()=\"ne\"]"),
				// "ne"));
				WebElement waittext = driver
						.findElement(By.xpath(("//div[contains(@id,\"RejectPopup::content\")]//span[text()=\"K\"]")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(4000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Ok clickButton" + scripNumber);
				String xpath = "//div[contains(@id,\"RejectPopup::content\")]//span[text()=\"K\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Ok clickButton" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param2.equalsIgnoreCase("OK")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(8000);
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\""
								+ param1 + "\"]/following::*[not (@aria-disabled) and text()=\"OK\"][1]"))));
				WebElement waittext = driver.findElement(
						By.xpath(("//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\""
								+ param1 + "\"]/following::*[not (@aria-disabled) and text()=\"OK\"][1]")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				Thread.sleep(4000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(4000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Ok clickButton" + scripNumber);
				String xpath = "//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\"param1\"]/following::*[not (@aria-disabled) and text()=\"OK\"][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Ok clickButton" + scripNumber);
		}
		try {
			if (param2.equalsIgnoreCase("OK")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(8000);
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//[contains(text(),\"" + param1 + "\")]/following::span[text()=\"K\"]"))));
				WebElement waittext = driver
						.findElement(By.xpath(("//[contains(text(),\"" + param1 + "\")]/following::span[text()=\"K\"]")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(4000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Ok clickButton" + scripNumber);
				String xpath = "//[contains(text(),\"param1\")]/following::span[text()=\"K\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Ok clickButton" + scripNumber);
		}
		try {

			if (param2.equalsIgnoreCase("Select")) {
				Thread.sleep(2000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("(//input[contains(@value,\"" + param1
						+ "\") and (@type)]/following::button[contains(text(),\"" + param2 + "\")])[1]"))));
				WebElement waittext = driver.findElement(By.xpath(("(//input[contains(@value,\"" + param1
						+ "\") and (@type)]/following::button[contains(text(),\"" + param2 + "\")])[1]")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(4000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Select clickButton" + scripNumber);
				String xpath = "(//input[contains(@value,\"param1\") and (@type)]/following::button[contains(text(),\"param2\")])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Select clickButton" + scripNumber);
		}
		try {
			if (param2.equalsIgnoreCase("Done")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(8000);
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//*[contains(@id,\"tAccountPopup::content\")]//*[text()=\"o\"]"))));
				WebElement waittext = driver
						.findElement(By.xpath(("//*[contains(@id,\"tAccountPopup::content\")]//*[text()=\"o\"]")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Done clickButton" + scripNumber);
				String xpath = "//*[contains(@id,\"tAccountPopup::content\")]//*[text()=\"o\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Done clickButton" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Apply")) {
				Thread.sleep(8000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@value=\"" + param1 + "\"]")));
				// wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()=\"l\"]"),
				// "l"));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath("//input[@value=\"" + param1 + "\"]"));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Apply clickButton" + scripNumber);
				String xpath = "//input[@value=\"param1\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Apply clickButton" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Done")) {
				Thread.sleep(3000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(5000);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[text()=\"ne\"]"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[text()=\"ne\"]"), "ne"));
				WebElement waittext = driver.findElement(By.xpath(("//*[text()=\"ne\"]")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Done clickButton" + scripNumber);
				logger.info("Sucessfully Clicked Done clickButton" + scripNumber);
				String xpath = "//*[text()=\"ne\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param1.equalsIgnoreCase("Approval and Notification History")
					&& param2.equalsIgnoreCase("Done")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(5000);
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//div[contains(text(),\"" + param1 + "\")]/following::span[text()=\"o\"]"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(
						By.xpath("//div[contains(text(),\"" + param1 + "\")]/following::span[text()=\"o\"]"), "o"));
				WebElement waittext = driver.findElement(
						By.xpath(("//div[contains(text(),\"" + param1 + "\")]/following::span[text()=\"o\"]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Approval and Notification History or Done clickButton" + scripNumber);
				String xpath = "//div[contains(text(),\"param1\")]/following::span[text()=\"o\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param2.equalsIgnoreCase("Done")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(5000);
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//*[contains(text(),\"" + param1 + "\")]/following::span[text()=\"o\"]"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(
						By.xpath("//*[contains(text(),\"" + param1 + "\")]/following::span[text()=\"o\"]"), "o"));
				WebElement waittext = driver
						.findElement(By.xpath(("//*[contains(text(),\"" + param1 + "\")]/following::span[text()=\"o\"]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Done clickButton" + scripNumber);
				String xpath = "//*[contains(text(),\"param1\")]/following::span[text()=\"o\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param1.equalsIgnoreCase("Submit")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[text()=\"m\"]"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()=\"m\"]"), "m"));
				Thread.sleep(20000);
				WebElement waittext = driver.findElement(By.xpath(("//span[text()=\"m\"]")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(3000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Submit clickButton" + scripNumber);
				String xpath = "//span[text()=\"m\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param2.equalsIgnoreCase("Submit")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//*[normalize-space(text())=\"" + param1 + "\"]/following::span[text()=\"m\"]"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(
						By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::span[text()=\"m\"]"), "m"));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(
						By.xpath(("//*[normalize-space(text())=\"" + param1 + "\"]/following::span[text()=\"m\"]")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(3000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Submit clickButton" + scripNumber);
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::span[text()=\"m\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param1.equalsIgnoreCase("Distributions")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()=\"istributions\"]")));
				WebElement waittext = driver.findElement(By.xpath("//span[text()=\"istributions\"]"));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(3000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Distributions clickButton" + scripNumber);
				String xpath = "//span[text()=\"istributions\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if ((param1.equalsIgnoreCase("Manage Distributions") || param1.equalsIgnoreCase("Manage Holds"))
					&& param2.equalsIgnoreCase("Save and Close")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						("//*[normalize-space(text())=\"" + param1 + "\"]/following::button[text()=\"Save and Close\"]"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(
						"//*[normalize-space(text())=\"" + param1 + "\"]/following::button[text()=\"Save and Close\"]"),
						"Save and Close"));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath(
						("//*[normalize-space(text())=\"" + param1 + "\"]/following::button[text()=\"Save and Close\"]")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(3000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Manage Holds or Save and Close clickButton" + scripNumber);
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::button[text()=\"Save and Close\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param2.equalsIgnoreCase("Save and Close")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//*[normalize-space(text())=\"" + param1 + "\"]/following::span[text()=\"S\"]"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(
						By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::span[text()=\"S\"]"), "S"));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(
						By.xpath(("//*[normalize-space(text())=\"" + param1 + "\"]/following::span[text()=\"S\"]")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(3000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::span[text()=\"S\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param1.equalsIgnoreCase("Next")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[text()=\"x\"]"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()=\"x\"]"), "x"));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath(("//span[text()=\"x\"]")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(8000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Next clickButton" + scripNumber);
				String xpath = "//span[text()=\"x\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param2.equalsIgnoreCase("Next")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]")));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[normalize-space(text())=\""
						+ param1 + "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]"), "x"));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]"));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(8000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Next clickButton" + scripNumber);
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param2.equalsIgnoreCase("Yes")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath(("//div[@class=\"AFDetectExpansion\"]/following::*[text()=\""
								+ param1 + "\"]/following::span[text()=\"Y\"]"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(
						By.xpath("//div[@class=\"AFDetectExpansion\"]/following::*[text()=\"" + param1
								+ "\"]/following::span[text()=\"Y\"]"),
						"Y"));
				Thread.sleep(6000);
				WebElement waittext = driver
						.findElement(By.xpath(("//div[@class=\"AFDetectExpansion\"]/following::*[text()=\"" + param1
								+ "\"]/following::span[text()=\"Y\"]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(6000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Yes clickButton" + scripNumber);
				String xpath = "//div[@class=\"AFDetectExpansion\"]/following::*[text()=\"param1\"]/following::span[text()=\"Y\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param2.equalsIgnoreCase("OK")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("//button[@_afrpdo=\"ok\" and @accesskey=\"K\"]")));
				wait.until(ExpectedConditions
						.textToBePresentInElementLocated(By.xpath("//button[@_afrpdo=\"ok\" and @accesskey=\"K\"]"), "K"));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath("//button[@_afrpdo=\"ok\" and @accesskey=\"K\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(4000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked OK clickButton" + scripNumber);
				String xpath = "//button[@_afrpdo=\"ok\" and @accesskey=\"K\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} // Here adding code for Advanced Button AP.452

			else if (param1.equalsIgnoreCase("Advanced")) {

				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				wait.until(ExpectedConditions

						.presenceOfElementLocated(By.xpath("//button[@accesskey=\"d\"]")));

				Thread.sleep(4000);

				WebElement waittext = driver.findElement(By.xpath("//button[@accesskey=\"d\"]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittext).build().perform();

//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO); 

				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);

				Thread.sleep(4000);

				String scripNumber = fetchMetadataVO.getScriptNumber();

				logger.info("Sucessfully Clicked OK clickButton" + scripNumber);

				String xpath = "//button[@accesskey=\"d\"]";

				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			} else if (param1.equalsIgnoreCase("Save and Close")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[text()=\"S\"]"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()=\"S\"]"), "S"));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath(("//span[text()=\"S\"]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath = "//span[text()=\"S\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param1.equalsIgnoreCase("Continue")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[text()=\"u\"]"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()=\"u\"]"), "u"));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath(("//span[text()=\"u\"]")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Continue clickButton" + scripNumber);
				String xpath = "//span[text()=\"u\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param2.equalsIgnoreCase("Continue")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//button[text()=\"Contin\"]"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//button[text()=\"Contin\"]"),
						"Contin"));
				WebElement waittext = driver.findElement(By.xpath(("//button[text()=\"Contin\"]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Continue clickButton" + scripNumber);
				String xpath = "//button[text()=\"Contin\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param1.equalsIgnoreCase("Close")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//button[text()=\"Cl\"]"))));
				Thread.sleep(5000);
				WebElement waittext = driver.findElement(By.xpath(("//button[text()=\"Cl\"]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Close clickButton" + scripNumber);
				String xpath = "//button[text()=\"Cl\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param1.equalsIgnoreCase("Adjustment")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(3000);
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath(("(//span[text()=\"" + param1 + "\"])[1]"))));
				WebElement waittext = driver.findElement(By.xpath(("(//span[text()=\"" + param1 + "\"])[1]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Adjustment clickButton" + scripNumber);
				String xpath = "(//span[text()=\"param1\"])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param1.equalsIgnoreCase("Cancel")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()=\"C\"]")));
				Thread.sleep(5000);
				WebElement waittext = driver.findElement(By.xpath("//span[text()=\"C\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Cancel clickButton" + scripNumber);
				String xpath = "//span[text()=\"C\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param1.equalsIgnoreCase("Save")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[text()=\"ave\"]"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()=\"ave\"]"), "ave"));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath(("//span[text()=\"ave\"]")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Save clickButton" + scripNumber);
				String xpath = "//span[text()=\"ave\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param1.equalsIgnoreCase("Apply")) {
				Thread.sleep(8000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[text()=\"l\"]"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()=\"l\"]"), "l"));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath(("//span[text()=\"l\"]")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Apply clickButton" + scripNumber);
				String xpath = "//span[text()=\"l\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param2.equalsIgnoreCase("Apply")) {
				Thread.sleep(4000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//*[normalize-space(text())=\"" + param1 + "\"]/following::span[text()=\"l\"]"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(
						By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::span[text()=\"l\"]"), "l"));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(
						By.xpath(("//*[normalize-space(text())=\"" + param1 + "\"]/following::span[text()=\"l\"]")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				Thread.sleep(2000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Apply clickButton" + scripNumber);
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::span[text()=\"l\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param2.equalsIgnoreCase("Accept")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//*[normalize-space(text())=\"" + param1 + "\"]/following::span[text()=\"p\"]"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(
						By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::span[text()=\"p\"]"), "p"));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(
						By.xpath(("//*[normalize-space(text())=\"" + param1 + "\"]/following::span[text()=\"p\"]")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Accept clickButton" + scripNumber);
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::span[text()=\"p\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Apply clickButton" + scripNumber);
			logger.error(e.getMessage());

		}
		try {
			if (param2.equalsIgnoreCase("OK")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//*[normalize-space(text())=\"" + param1 + "\"]/following::span[text()=\"K\"]"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(
						By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::span[text()=\"K\"]"), "K"));
				WebElement waittext = driver.findElement(
						By.xpath(("//*[normalize-space(text())=\"" + param1 + "\"]/following::span[text()=\"K\"]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(4000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked OK clickButton" + scripNumber);
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::span[text()=\"K\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during OK clickButton" + scripNumber);
		}
		try {
			if (param1.equalsIgnoreCase("Add Application")) {
				try {
					WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
					wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[text()=\"A\"]"))));
					wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()=\"A\"]"), "A"));
					Thread.sleep(4000);
					WebElement waittext = driver.findElement(By.xpath(("//span[text()=\"A\"]")));
//					screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
					Actions actions = new Actions(driver);
					actions.moveToElement(waittext).build().perform();
					clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
					String scripNumber = fetchMetadataVO.getScriptNumber();
					logger.info("Sucessfully Clicked add Application clickButton" + scripNumber);
					String xpath = "//span[text()=\"A\"]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
				} catch (Exception e) {
					WebElement expand = driver
							.findElement(By.xpath(("//a[text()=\"Application\"]/following::div[@role=\"button\"][2]")));
					expand.click();
					Thread.sleep(2000);
					WebElement waittext = driver.findElement(By.xpath(("//span[text()=\"A\"]")));
//					screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
					Actions actions = new Actions(driver);
					actions.moveToElement(waittext).build().perform();
					clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
					String scripNumber = fetchMetadataVO.getScriptNumber();
					logger.error("Failed during add Application clickButton" + scripNumber);
					String xpath = "//a[text()=\"Application\"]/following::div[@role=\"button\"][2]" + ";"
							+ "//span[text()=\"A\"]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
				}
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during add Application clickButton" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Unapply Application")) {
				try {
					WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
					wait.until(ExpectedConditions
							.presenceOfElementLocated(By.xpath(("//button[text()=\"" + param1 + "\"]"))));
					wait.until(ExpectedConditions
							.textToBePresentInElementLocated(By.xpath("//button[text()=\"" + param1 + "\"]"), "param1"));
					Thread.sleep(4000);
					WebElement waittext = driver.findElement(By.xpath(("//button[text()=\"" + param1 + "\"]")));
//					screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
					Actions actions = new Actions(driver);
					actions.moveToElement(waittext).build().perform();
					clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
					Thread.sleep(4000);
					String scripNumber = fetchMetadataVO.getScriptNumber();
					logger.info("Sucessfully Clicked Unapply Application clickButton" + scripNumber);
					String xpath = "//button[text()=\"param1\"]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
				} catch (Exception e) {
					WebElement expand = driver
							.findElement(By.xpath(("//a[text()=\"Application\"]/following::div[@role=\"button\"][2]")));
					expand.click();
					Thread.sleep(2000);
					WebElement waittext = driver.findElement(By.xpath(("//button[text()=\"" + param1 + "\"]")));
//					screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
					Actions actions = new Actions(driver);
					actions.moveToElement(waittext).build().perform();
					clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
					String scripNumber = fetchMetadataVO.getScriptNumber();
					logger.error("Failed during Unapply Application clickButton" + scripNumber);
					String xpath = "//a[text()=\"Application\"]/following::div[@role=\"button\"][2]" + ";"
							+ "//button[text()=\"param1\"]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
				}
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Unapply Application clickButton" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param2.equalsIgnoreCase("Submit")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(3000);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[normalize-space(text())=\""
						+ param1 + "\"]/following::span[normalize-space(text())=\"" + param2 + "\"]"))));
				WebElement waittext = driver.findElement(By.xpath(("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::span[normalize-space(text())=\"" + param2 + "\"]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(3000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Submit clickButton" + scripNumber);
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::span[normalize-space(text())=\"param2\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			// TODO: handle exception
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Submit clickButton" + scripNumber);
		}
		try {
			// Changed == to equals method
			if (!param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())=\""
						+ param1 + "\"]/following::*[normalize-space(text())=\"" + param2 + "\"])[1]")));
				WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())=\"" + param1
						+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"])[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked clickButton" + scripNumber);
				String xpath = "(//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Columns") || param1.equalsIgnoreCase("Show All")) {
				Thread.sleep(4000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("(//td[normalize-space(text())=\"" + param1 + "\"])[2]")));
				// wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()=\"l\"]"),
				// "l"));
				Thread.sleep(4000);
				WebElement waittext = driver
						.findElement(By.xpath("(//td[normalize-space(text())=\"" + param1 + "\"])[2]"));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked  Columns or Show All clickButton" + scripNumber);
				String xpath = "(//td[normalize-space(text())=\"param1\"])[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Columns or Show All clickButton" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Add to Document Builder")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(3000);
				wait.until(
						ExpectedConditions.presenceOfElementLocated(By.xpath(("//button[text()=\"" + param1 + "\"]"))));
				WebElement waittext = driver.findElement(By.xpath(("//button[text()=\"" + param1 + "\"]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(3000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked  Add to Document Builder clickButton" + scripNumber);
				String xpath = "//button[text()=\"param1\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Add to Document Builder clickButton" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Freeze")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(3000);
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//tr[contains(@id,\"HEADER_FREEZE\")]//td[text()=\"" + param1 + "\"]"))));
				WebElement waittext = driver
						.findElement(By.xpath(("//tr[contains(@id,\"HEADER_FREEZE\")]//td[text()=\"" + param1 + "\"]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(3000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked  Freeze clickButton" + scripNumber);
				String xpath = "//tr[contains(@id,\"HEADER_FREEZE\")]//td[text()=\"param1\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param1.equalsIgnoreCase("Unfreeze")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(3000);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						("//tr[contains(@id,\"HEADER_UNFREEZE\")]//td[normalize-space(text())=\"" + param1 + "\"]"))));
				WebElement waittext = driver.findElement(By.xpath(
						("//tr[contains(@id,\"HEADER_UNFREEZE\")]//td[normalize-space(text())=\"" + param1 + "\"]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(3000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked  Unfreeze clickButton" + scripNumber);
				String xpath = "//tr[contains(@id,\"HEADER_UNFREEZE\")]//td[normalize-space(text())=\"param1\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param1.equalsIgnoreCase("Close")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(3000);
				wait.until(ExpectedConditions.presenceOfElementLocated(By
						.xpath(("//tr[contains(@id,\"HEADER_CLOSE\")]//td[normalize-space(text())=\"" + param1 + "\"]"))));
				WebElement waittext = driver.findElement(
						By.xpath(("//tr[contains(@id,\"HEADER_CLOSE\")]//td[normalize-space(text())=\"" + param1 + "\"]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(3000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Close clickButton" + scripNumber);
				String xpath = "//tr[contains(@id,\"HEADER_CLOSE\")]//td[normalize-space(text())=\"param1\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param1.equalsIgnoreCase("Reopen")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(3000);
				wait.until(ExpectedConditions.presenceOfElementLocated(By
						.xpath(("//tr[contains(@id,\"HEADER_REOPEN\")]//td[normalize-space(text())=\"" + param1 + "\"]"))));
				WebElement waittext = driver.findElement(By
						.xpath(("//tr[contains(@id,\"HEADER_REOPEN\")]//td[normalize-space(text())=\"" + param1 + "\"]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(3000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Reopen clickButton" + scripNumber);
				String xpath = "//tr[contains(@id,\"HEADER_REOPEN\")]//td[normalize-space(text())=\"param1\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param1.equalsIgnoreCase("Edit")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(3000);
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//tr[contains(@id,\"HEADER_EDIT\")]//td[normalize-space(text())=\"" + param1 + "\"]"))));
				WebElement waittext = driver.findElement(
						By.xpath(("//tr[contains(@id,\"HEADER_EDIT\")]//td[normalize-space(text())=\"" + param1 + "\"]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(3000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Edit clickButton" + scripNumber);
				String xpath = "//tr[contains(@id,\"HEADER_EDIT\")]//td[normalize-space(text())=\"param1\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Edit")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(3000);
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//tr[contains(@id,\"commandMenuItem\")]//td[text()=\"" + param1 + "\"]"))));
				WebElement waittext = driver
						.findElement(By.xpath(("//tr[contains(@id,\"commandMenuItem\")]//td[text()=\"" + param1 + "\"]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(3000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Edit clickButton" + scripNumber);
				String xpath = "//tr[contains(@id,\"commandMenuItem\")]//td[text()=\"param1\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Edit clickButton" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Reverse")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By
						.xpath(("//div[@class=\"AFPopupMenuPopup\"]//td[(normalize-space(text())=\"" + param1 + "\")]"))));
				WebElement waittext = driver.findElement(
						By.xpath(("//div[@class=\"AFPopupMenuPopup\"]//td[(normalize-space(text())=\"" + param1 + "\")]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(2000);
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Reverse clickButton" + scripNumber);
				String xpath = "//div[@class=\"AFPopupMenuPopup\"]//td[(normalize-space(text())=\"param1\")]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Reverse clickButton" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("PDF")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("//td[normalize-space(text())=\"" + param1 + "\"]")));
				WebElement waittext = driver.findElement(By.xpath("//td[normalize-space(text())=\"" + param1 + "\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(60000);
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked  Columns or Show All clickButton" + scripNumber);
				String xpath = "//td[normalize-space(text())=\"param1\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Apply clickButton" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Republish")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("//button[normalize-space(text())=\"" + param1 + "\"]")));
				WebElement waittext = driver
						.findElement(By.xpath("//button[normalize-space(text())=\"" + param1 + "\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(6000);
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Republish clickButton" + scripNumber);
				String xpath = "//button[normalize-space(text())=\"param1\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Republish clickButton" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Match Invoice Lines") && param2.equalsIgnoreCase("OK")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[text()=\"" + param1 + "\"]/following::*[text()=\"K\"][2]")));
				WebElement waittext = driver
						.findElement(By.xpath("//*[text()=\"" + param1 + "\"]/following::*[text()=\"K\"][2]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(6000);
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Republish clickButton" + scripNumber);
				String xpath = "//*[text()=\"param1\"]/following::*[text()=\"K\"][2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Republish clickButton" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(3000);
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath(("//span[normalize-space(text())=\"" + param1 + "\"]"))));
				WebElement waittext = driver
						.findElement(By.xpath(("//span[normalize-space(text())=\"" + param1 + "\"]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked  clickButton" + scripNumber);
				String xpath = "//span[normalize-space(text())=\"param1\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath(("//td[(normalize-space(text())=\"" + param1 + "\")]"))));
				WebElement waittext = driver
						.findElement(By.xpath(("//td[(normalize-space(text())=\"" + param1 + "\")]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(2000);
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked  clickButton" + scripNumber);
				String xpath = "//td[(normalize-space(text())=\"param1\")]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//button[text()=\"" + param1 + "\"and not(@style=\"display:none\")]"))));
				WebElement waittext = driver
						.findElement(By.xpath(("//button[text()=\"" + param1 + "\"and not(@style=\"display:none\")]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(2000);
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked clickButton" + scripNumber);
				String xpath = "//button[text()=\"param1\"and not(@style=\"display:none\")]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(3000);
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath(("//div[normalize-space(text())=\"" + param1 + "\"]"))));
				WebElement waittext = driver.findElement(By.xpath(("//div[normalize-space(text())=\"" + param1 + "\"]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked clickButton" + scripNumber);
				String xpath = "//div[normalize-space(text())=\"param1\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//div[contains(@class,\"PopupMenu\")]/following::*[text()=\"" + param1 + "\"]"))));
				WebElement waittext = driver.findElement(
						By.xpath(("//div[contains(@class,\"PopupMenu\")]/following::*[text()=\"" + param1 + "\"]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(2000);
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked  clickButton" + scripNumber);
				String xpath = "//div[contains(@class,\"PopupMenu\")]/following::*[text()=\"param1\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\" and not(@_afrpdo)])[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\" and not(@_afrpdo)])[1]"));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			Thread.sleep(1000);
//			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked clickButton" + scripNumber);
			String xpath = "(//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\" and not(@_afrpdo)])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"])[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"])[1]"));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			Thread.sleep(3000);
//			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked clickButton" + scripNumber);
			String xpath = "(//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"(//*[normalize-space(text())=\"" + param1 + "\"]/following::*[@title=\"" + param2 + "\"])[1]")));
			WebElement waittext = driver.findElement(By.xpath(
					"(//*[normalize-space(text())=\"" + param1 + "\"]/following::*[@title=\"" + param2 + "\"])[1]"));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			Thread.sleep(3000);
//			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked clickButton" + scripNumber);
			String xpath = "(//*[normalize-space(text())=\"param1\"]/following::*[@title=\"param2\"])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);
			logger.error(e.getMessage());
		}

		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			Thread.sleep(3000);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[contains(text(),\"" + param1
					+ "\")]/following::*[normalize-space(text())=\"" + param2 + "\"][1]"))));
			WebElement waittext = driver.findElement(By.xpath(("//*[contains(text(),\"" + param1
					+ "\")]/following::*[normalize-space(text())=\"" + param2 + "\"][1]")));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			Thread.sleep(5000);
//			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked clickButton" + scripNumber);
			String xpath = "//*[contains(text(),\"param1\")]/following::*[normalize-space(text())=\"param2\"][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);
		}
//		   try {
//	              String text = driver.findElement(By.xpath("//td[@class=\"AFNoteWindow\"]")).getText();
//	              fetchConfigVO.setErrormessage(text);
//	  			return;
//	        } catch (Exception e) {
//	            logger.error(e.getMessage());
//	        }try {
//	              String text = driver.findElement(By.xpath("//div[contains(@class,\"Error\")]")).getText();
//	              fetchConfigVO.setErrormessage(text);
//	  			return;
//	        } catch (Exception e) {
//	            logger.error(e.getMessage());
//	        }
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"])[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
//			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			Thread.sleep(1000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked clickButton" + scripNumber);
			String xpath = "(//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);
			logger.error(e.getMessage());
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}

	}

	public void clickTableLink(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			if (param1.equalsIgnoreCase("Manage Negotiations")||param2.equalsIgnoreCase("Search Results")) {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
			.presenceOfElementLocated(By.xpath("(//table[@summary=\"" +param2+ "\"]//table[1]//a)[1]")));
			Thread.sleep(4000);
			WebElement waittext = driver
			.findElement(By.xpath("(//table[@summary=\"" +param2+ "\"]//table[1]//a)[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			Thread.sleep(2000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked List of Processes Meeting Search Criteria clickTableLink" + scripNumber);
			String xpath = "(//table[@summary=\"param2\"]//table[1]//a)[1]";
			//service.saveXpathParams(param1, param2, scripNumber, xpath);
			return;
			}
			} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during List of Processes Meeting Search Criteria clickTableLink" + scripNumber);
			logger.error(e.getMessage());
			}
		try {
			if (param1.equalsIgnoreCase("Manage Agreements") && (param2.equalsIgnoreCase("Headers: Search Results"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[normalize-space(text())=\""
						+ param1 + "\"]/following::table[@summary=\"" + param2
						+ "\"]//a[contains(@title,\"Blanket Purchase Agreement\") or contains(@title,\"Contract Purchase Agreement\")])[1]")));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath("(//h1[normalize-space(text())=\"" + param1
						+ "\"]/following::table[@summary=\"" + param2
						+ "\"]//a[contains(@title,\"Blanket Purchase Agreement\") or contains(@title,\"Contract Purchase Agreement\")])[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				actions.click(waittext).build().perform();
				Thread.sleep(8000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Addresses clickTableLink" + scripNumber);
				String xpath = "(//h1[normalize-space(text())=\"param1\"]/following::table[@summary=\"param2\"]//a[contains(@title,\"Blanket Purchase Agreement\") or contains(@title,\"Contract Purchase Agreement\")])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Addresses clickTableLink" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Manage Agreements") || param2.equalsIgnoreCase("Search Results")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("(//table[@summary=\"" + param1 + "\"]//table[1]//a)[1]")));
				Thread.sleep(4000);
				WebElement waittext = driver
						.findElement(By.xpath("(//table[@summary=\"" + param1 + "\"]//table[1]//a)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(2000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked List of Processes Meeting Search Criteria clickTableLink" + scripNumber);
				String xpath = "(//table[@summary=\"param1\"]//table[1]//a)[1]";
				// service.saveXpathParams(param1, param2, scripNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during List of Processes Meeting Search Criteria clickTableLink" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Manage Receipts") || param1.equalsIgnoreCase("Manage Customers")) {
				Thread.sleep(3000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[normalize-space(text())=\""
						+ param1 + "\"]/following::table[@summary=\"" + param2 + "\"]//a)[2]/parent::span")));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath("(//h1[normalize-space(text())=\"" + param1
						+ "\"]/following::table[@summary=\"" + param2 + "\"]//a)[2]/parent::span"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				actions.click(waittext).build().perform();
				Thread.sleep(8000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked clickTableLink" + scripNumber);
				String xpath = "(//h1[normalize-space(text())=\"param1\"]/following::table[@summary=\"param2\"]//a)[2]/parent::span";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		// new DH Script 4
		try {
			if (param1.equalsIgnoreCase("Accounts")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(),\"" + param1
						+ "\")]/following::table[@summary=\"" + param2 + "\"]//span[@title]")));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath("//*[contains(text(),\"" + param1
						+ "\")]/following::table[@summary=\"" + param2 + "\"]//span[@title]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(2000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked List of Processes Meeting Search Criteria clickTableLink" + scripNumber);
				String xpath = "//*[contains(text(),\"param1\")]/following::table[@summary=\"param2\"]//span[@title]";
				// service.saveXpathParams(param1, param2, scripNumber, xpath);
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during List of Processes Meeting Search Criteria clickTableLink" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Manage Journals") || param1.equalsIgnoreCase("Journal Lines")
					|| param1.equalsIgnoreCase("Manage Transactions") || param1.equalsIgnoreCase("Prepare Source lines")
					|| param1.equalsIgnoreCase("Contracts")) {
				Thread.sleep(3000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[normalize-space(text())=\""
						+ param1 + "\"]/following::table[@summary=\"" + param2 + "\"]//a)[2]")));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath("(//h1[normalize-space(text())=\"" + param1
						+ "\"]/following::table[@summary=\"" + param2 + "\"]//a)[2]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				actions.click(waittext).build().perform();
				Thread.sleep(8000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked clickTableLink" + scripNumber);
				String xpath = "(//h1[normalize-space(text())=\"param1\"]/following::table[@summary=\"param2\"]//a)[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickTableLink" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Addresses")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("(//table[@summary=\"" + param1 + "\"]//a)[2]")));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath("(//table[@summary=\"" + param1 + "\"]//a)[2]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				actions.click(waittext).build().perform();
				Thread.sleep(8000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Addresses clickTableLink" + scripNumber);
				String xpath = "(//table[@summary=\"param1\"]//a)[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Addresses clickTableLink" + scripNumber);
			logger.error(e.getMessage());
		}

		try {
			if (param1.equalsIgnoreCase("Source Lines")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"(//table[@summary=\"" + param1 + "\"]//a[not (contains(@title,\"Required information\"))])[1]")));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath(
						"(//table[@summary=\"" + param1 + "\"]//a[not (contains(@title,\"Required information\"))])[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				actions.click(waittext).build().perform();
				Thread.sleep(8000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Addresses clickTableLink" + scripNumber);
				String xpath = "(//table[@summary=\"param1\"]//a[not (contains(@title,\"Required information\"))])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Addresses clickTableLink" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param2.equalsIgnoreCase("Approved")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//table[@summary=\"" + param1
						+ "\"]//*[normalize-space(text())=\"" + param2 + "\"]/following::a)[1]")));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath("(//table[@summary=\"" + param1
						+ "\"]//*[normalize-space(text())=\"" + param2 + "\"]/following::a)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				actions.click(waittext).build().perform();
				Thread.sleep(8000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Approved clickTableLink" + scripNumber);
				String xpath = "(//table[@summary=\"param1\"]//*[normalize-space(text())=\"param2\"]/following::a)[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Approved clickTableLink" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Manage Orders")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//h1[normalize-space(text())=\"" + param1 + "\"]/following::table[@summary=\"" + param2
								+ "\"]//a[contains(@title,\"Purchase Order\")]")));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath("//h1[normalize-space(text())=\"" + param1
						+ "\"]/following::table[@summary=\"" + param2 + "\"]//a[contains(@title,\"Purchase Order\")]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(2000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Manage Orders clickTableLink" + scripNumber);
				String xpath = "//h1[normalize-space(text())=\"param1\"]/following::table[@summary=\"param2\"]//a[contains(@title,\"Purchase Order\")]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Manage Orders clickTableLink" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Manage Receipts")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("(//table[@summary=\"" + param2 + "\"]//td)[1]")));
				WebElement waittext = driver.findElement(By.xpath("(//table[@summary=\"" + param2 + "\"]//td)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(2000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Manage Receipts clickTableLink" + scripNumber);
				String xpath = "(//table[@summary=\"param2\"]//td)[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Manage Receipts clickTableLink" + scripNumber);
			logger.error(e.getMessage());
		}
		// Adding Xpath for \"Checking the dashboard for unposted & journals in error for
		// all journals(GL.125)’
		try {

			if (param1.equalsIgnoreCase("Journals") && param2.equalsIgnoreCase("Requiring Attention")) {

				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				wait.until(ExpectedConditions

						.presenceOfElementLocated(By.xpath(
								"//h2[text()=\"" + param1 + "\"]//following::table[@summary=\"" + param2 + "\"]//a[1]")));

				WebElement waittext = driver.findElement(
						By.xpath("//h2[text()=\"" + param1 + "\"]//following::table[@summary=\"" + param2 + "\"]//a[1]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittext).build().perform();

				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);

				tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);

				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);

				Thread.sleep(2000);

				String scripNumber = fetchMetadataVO.getScriptNumber();

				logger.info("Sucessfully Clicked Journals in Requiring Attention clickTableLink" + scripNumber);

				String xpath = "//h2[text()=\"param1\"]//following::table[@summary=\"param2\"]//a[1]";

				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;

			}

		} catch (Exception e) {

			String scripNumber = fetchMetadataVO.getScriptNumber();

			logger.error("Failed during Journals in Requiring Attention clickTableLink" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("List of Processes Meeting Search Criteria")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("(//table[@summary=\"" + param1 + "\"]//td[2]//span)[1]")));
				Thread.sleep(4000);
				WebElement waittext = driver
						.findElement(By.xpath("(//table[@summary=\"" + param1 + "\"]//td[2]//span)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(2000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked List of Processes Meeting Search Criteria clickTableLink" + scripNumber);
				String xpath = "(//table[@summary=\"param1\"]//td[2]//span)[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during List of Processes Meeting Search Criteria clickTableLink" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("(//table[@summary=\"" + param1 + "\"]//a[not (@title)])[1]")));
				Thread.sleep(4000);
				WebElement waittext = driver
						.findElement(By.xpath("(//table[@summary=\"" + param1 + "\"]//a[not (@title)])[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(2000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked  clickTableLink" + scripNumber);
				String params = param1;
				String xpath = "(//table[@summary=\"param1\"]//a[not (@title)])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickTableLink" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[normalize-space(text())=\"" + param1
					+ "\"]/following::img[@title=\"" + param2 + "\"]/following-sibling::a[1]")));
			Thread.sleep(4000);
			WebElement waittext = driver.findElement(By.xpath("//h1[normalize-space(text())=\"" + param1
					+ "\"]/following::img[@title=\"" + param2 + "\"]/following-sibling::a[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			Thread.sleep(2000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked clickTableLink" + scripNumber);
			String xpath = "//h1[normalize-space(text())=\"param1\"]/following::img[@title=\"param2\"]/following-sibling::a[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickTableLink" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}

	}

	public void tableRowSelect(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {

			if (param1.equalsIgnoreCase("Value") || param1.equalsIgnoreCase("Transaction Number")
					|| param1.equalsIgnoreCase("Name")) {
				Thread.sleep(5000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("(//div[@class=\"AFDetectExpansion\"]/following::span[normalize-space(text())=\"" + param1
								+ "\"]/following::table//span[text()])[1]")));
				WebElement waittext = driver.findElement(
						By.xpath("(//div[@class=\"AFDetectExpansion\"]/following::span[normalize-space(text())=\"" + param1
								+ "\"]/following::table//span[text()])[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(4000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked tableRowSelect" + scripNumber);
				String xpath = "(//div[@class=\"AFDetectExpansion\"]/following::span[normalize-space(text())=\"param1\"]/following::table//span[text()])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during tableRowSelect" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("SecondLine")) {
				Thread.sleep(4000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("(//table[@summary=\"" + param2 + "\"]//tr[2]//td)[1]")));
				WebElement waittext = driver
						.findElement(By.xpath("(//table[@summary=\"" + param2 + "\"]//tr[2]//td)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(10000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked SecondLine tableRowSelect" + scripNumber);
				String xpath = "(//table[@summary=\"param2\"]//tr[2]//td)[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during SecondLine tableRowSelect" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			Thread.sleep(6000);
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("(//table[@summary=\"" + param1 + "\"]//td)[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//table[@summary=\"" + param1 + "\"]//td)[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			waittext.click();
			// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(10000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked Here1 came tableRowSelect" + scripNumber);
			String xpath = "(//table[@summary=\"param1\"]//td)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Here1 came tableRowSelect" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"//h1[normalize-space(text())=\"" + param1 + "\"]/following::table[@summary=\"" + param1 + "\"][1]")));
			WebElement waittext = driver.findElement(By.xpath(
					"//h1[normalize-space(text())=\"" + param1 + "\"]/following::table[@summary=\"" + param1 + "\"][1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			Thread.sleep(4000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked tableRowSelect" + scripNumber);
			String xpath = "//h1[normalize-space(text())=\"param1\"]/following::table[@summary=\"param1\"][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during tableRowSelect" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::tr[1]/td[1]")));
			WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::tr[1]/td[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			Thread.sleep(2000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked tableRowSelect" + scripNumber);
			String xpath = "//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]/following::tr[1]/td[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during tableRowSelect" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("")));
			WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::tr[1]/td[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			Thread.sleep(2000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked tableRowSelect" + scripNumber);
			String xpath = "//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]/following::tr[1]/td[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during tableRowSelect" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			// throw e;
		}
		// New Code for NTA.SO.4
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(
					ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@summary,\"" + param1 + "\")]")));
			WebElement waittext = driver.findElement(By.xpath("//*[contains(@summary,\"" + param1 + "\")]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			Thread.sleep(2000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked tableRowSelect" + scripNumber);
			String xpath = "//*[contains(@summary,\"param1\")]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during tableRowSelect" + scripNumber);
			logger.error(e.getMessage());
			throw e;
		}
	}

	public void clickLink(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			try {

				if (param1.equalsIgnoreCase("drop")) {

					WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

					wait.until(ExpectedConditions

							.presenceOfElementLocated(By.xpath("//a[contains(@id,\"" + param1 + "\")]")));

					WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]"));

					Actions actions = new Actions(driver);

					actions.moveToElement(waittext).build().perform();

					waittext.click();

					screenshot(driver, fetchMetadataVO, customerDetails);

					refreshPage(driver, fetchMetadataVO, fetchConfigVO, customerDetails);

					Thread.sleep(5000);

					String scripNumber = fetchMetadataVO.getScriptNumber();

					logger.info("Sucessfully Clicked Approve clickLink" + scripNumber);

					String xpath = "//*[normalize-space(text())=\"param1\"]";

					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);

					return;

				}

			} catch (Exception e) {

				logger.error(e.getMessage());

				String scripNumber = fetchMetadataVO.getScriptNumber();

				logger.error("Failed during Approve clickLink" + scripNumber);

			}
			// Here adding code for Scanned invoices in AP.453

			try {

				if (param1.equalsIgnoreCase("Invoices")) {

					WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

					wait.until(ExpectedConditions.presenceOfElementLocated(
							By.xpath("//span[text()=\"" + param2 + "\"]/following::span[text() > \"0\"][1]")));

					WebElement waittext = driver
							.findElement(By.xpath("//span[text()=\"" + param2 + "\"]/following::span[text() > \"0\"][1]"));

					Actions actions = new Actions(driver);

					actions.moveToElement(waittext).build().perform();

					waittext.click();

					Thread.sleep(6000);

					screenshot(driver, fetchMetadataVO, customerDetails);

					String scripNumber = fetchMetadataVO.getScriptNumber();

					logger.info("Sucessfully Clicked Scanned clickLink" + scripNumber);

					String xpath = "//span[text()=\"param2\"]/following::span[text() > \"0\"][1]";

					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
					return;

				}

			} catch (Exception e) {

				String scripNumber = fetchMetadataVO.getScriptNumber();

				logger.error("Failed during Scanned clickLink" + scripNumber);

				logger.error(e.getMessage());

			}
			// DH fix
			try {
				if (param1.equalsIgnoreCase("Accounts")) {
					Thread.sleep(70000);
					WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
					// wait.until(ExpectedConditions
					// .presenceOfElementLocated(By.xpath("//a[normalize-space(text())=\"" + param1 +
					// "\"]")));
					WebElement waittext = driver.findElement(By
							.xpath("//*[contains(text(),\"" + param1 + "\")]/following::*[text()=\"" + param2 + "\"][1]"));
					Actions actions = new Actions(driver);
					actions.moveToElement(waittext).build().perform();
					// actions.moveToElement(Keys.PAGE_DOWN).build().perform();
					waittext.click();
					screenshot(driver, fetchMetadataVO, customerDetails);
					String scripNumber = fetchMetadataVO.getScriptNumber();
					logger.info("Sucessfully Clicked Export clickLink" + scripNumber);
					String xpath = "//*[contains(text(),\"param1\")]/following::*[text()=\"param2\"][1]";
					// service.saveXpathParams(param1, param2, scripNumber, xpath);
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);

					Thread.sleep(5000);
					return;
				}
			} catch (Exception e) {
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.error("Failed during Export clickLink" + scripNumber);
				logger.error(e.getMessage());
			}
			if (param1.equalsIgnoreCase("Report") && param2.equalsIgnoreCase("Apply")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@value=\"" + param2 + "\"]")));
				WebElement waittext = driver.findElement(By.xpath("//input[@value=\"" + param2 + "\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				Thread.sleep(6000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked clickLink" + scripNumber);
				String xpath = "//input[@value=\"param2\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickLink" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Home")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//a[contains(@id,\"UIShome\")])[1]")));
				WebElement waittext = driver.findElement(By.xpath("(//a[contains(@id,\"UIShome\")])[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				refreshPage(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Home clickLink" + scripNumber);
				String xpath = "(//a[contains(@id,\"UIShome\")])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Home clickLink" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Financials Details") && param2.equalsIgnoreCase("Invoices")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[contains(text(),\"" + param1 + "\")]/following::*[text()=\"" + param1 + "\")[1]")));
				WebElement waittext = driver.findElement(
						By.xpath("//*[contains(text(),\"" + param1 + "\")]/following::*[text()=\"" + param1 + "\")[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				refreshPage(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Financials Details or Invoices clickLink" + scripNumber);
				String xpath = "//*[contains(text(),\"param1\")]/following::*[text()=\"param1\")[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Financials Details or Invoices  clickLink" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Approve")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]")));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				refreshPage(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Approve clickLink" + scripNumber);
				String xpath = "//*[normalize-space(text())=\"param1\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Approve clickLink" + scripNumber);
		}
		try {
			if (param1.equalsIgnoreCase("Payables to Ledger Reconciliation Summary")
					&& param2.equalsIgnoreCase("Export")) {
				Thread.sleep(8000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//label[normalize-space(text())=\""
						+ param1 + "\"]/following::a[normalize-space(text())=\"" + param2 + "\"]")));
				WebElement waittext = driver.findElement(By.xpath("//label[normalize-space(text())=\"" + param1
						+ "\"]/following::a[normalize-space(text())=\"" + param2 + "\"]"));
				Actions actions = new Actions(driver);
				// actions.moveToElement(waittext).build().perform();
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
				js.executeScript("window.scrollBy(0,1000)");
				waittext.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Payables to Ledger Reconciliation Summary clickLink" + scripNumber);
				String xpath = "//label[normalize-space(text())=\"param1\"]/following::a[normalize-space(text())=\"param2\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Payables to Ledger Reconciliation Summary clickLink" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Export")) {
				Thread.sleep(70000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("//a[normalize-space(text())=\"" + param1 + "\"]")));
				WebElement waittext = driver.findElement(By.xpath("//a[normalize-space(text())=\"" + param1 + "\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				refreshPage(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Export clickLink" + scripNumber);
				String xpath = "//a[normalize-space(text())=\"param1\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Export clickLink" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Export")) {
				Thread.sleep(70000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("//a[normalize-space(text())=\"" + param1 + "\"]")));
				WebElement waittext = driver.findElement(By.xpath("//a[normalize-space(text())=\"" + param1 + "\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// actions.moveToElement(Keys.PAGE_DOWN).build().perform();
				waittext.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Export clickLink" + scripNumber);
				String xpath = "//a[normalize-space(text())=\"param1\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				Thread.sleep(5000);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Export clickLink" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Project")) {
				try {
					Thread.sleep(70000);
					WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
					wait.until(ExpectedConditions
							.presenceOfElementLocated(By.xpath("//a[normalize-space(text())=\"" + param1 + "\"]")));
					WebElement waittext = driver.findElement(By.xpath("//a[normalize-space(text())=\"" + param1 + "\"]"));
					Actions actions = new Actions(driver);
					actions.moveToElement(waittext).build().perform();
					waittext.click();
					screenshot(driver, fetchMetadataVO, customerDetails);
					// refreshPage(driver, fetchMetadataVO, fetchConfigVO);
					String scripNumber = fetchMetadataVO.getScriptNumber();
					logger.info("Sucessfully Clicked Project clickLink" + scripNumber);
					String xpath = "//a[normalize-space(text())=\"param1\"]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);

					Thread.sleep(5000);
				} catch (Exception e) {
					WebElement expand = driver
							.findElement(By.xpath(("//span[text()=\"Allocate\"]/following::div[@role=\"button\"][2]")));
					expand.click();
					Thread.sleep(2000);
					WebElement waittext = driver
							.findElement(By.xpath(("//a[normalize-space(text())=\"" + param1 + "\"]")));
					screenshot(driver, fetchMetadataVO, customerDetails);
					Actions actions = new Actions(driver);
					actions.moveToElement(waittext).build().perform();
					waittext.click();
					String scripNumber = fetchMetadataVO.getScriptNumber();
					logger.error("Failed during Project clickLink" + scripNumber);
					String xpath = "//span[text()=\"Allocate\"]/following::div[@role=\"button\"][2]" + ";"
							+ "//a[normalize-space(text())=\"param1\"]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
				}
				return;
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickLink" + scripNumber);
		}
		try {
			if (param1.equalsIgnoreCase("Financial Reporting Center")) {
				Thread.sleep(2000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath(("//a[normalize-space(text())=\"" + param2 + "\"][1]"))));
				WebElement waittext = driver
						.findElement(By.xpath(("//a[normalize-space(text())=\"" + param2 + "\"][1]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// Robot robot = new Robot();
				// robot.keyPress(KeyEvent.VK_PAGE_DOWN);
				// robot.keyRelease(KeyEvent.VK_PAGE_DOWN);
				// JavascriptExecutor jse = (JavascriptExecutor)driver;
				// jse.executeScript("window.scrollBy(0,1000)");
				waittext.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(30000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Financial Reporting Center clickLink" + scripNumber);
				String xpath = "//a[normalize-space(text())=\"param2\"][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Financial Reporting Center clickLink" + scripNumber);
		}
		try {
			if (param1.equalsIgnoreCase("Receivables")) {
				Thread.sleep(2000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath(("//a[normalize-space(text())=\"" + param1 + "\"][1]"))));
				WebElement waittext = driver
						.findElement(By.xpath(("//a[normalize-space(text())=\"" + param1 + "\"][1]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// Robot robot = new Robot();
				// robot.keyPress(KeyEvent.VK_PAGE_DOWN);
				// robot.keyRelease(KeyEvent.VK_PAGE_DOWN);
				// JavascriptExecutor jse = (JavascriptExecutor)driver;
				// jse.executeScript("window.scrollBy(0,1000)");
				waittext.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(30000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Receivables clickLink" + scripNumber);
				String xpath = "//a[normalize-space(text())=\"param1\"][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Receivables clickLink" + scripNumber);
			logger.error(e.getMessage());
		}
		// for "PTP.PO.212 Split requisition lines" when exectuing in Fusion instance
		try {
			if (param1.equalsIgnoreCase("Requisition Lines") && param2.equalsIgnoreCase("Actions")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//h2[text()=\"" + param1 + "\"]/following::a[text()=\"Actions\"]")));
				WebElement waittext = driver
						.findElement(By.xpath("//h2[text()=\"" + param1 + "\"]/following::a[text()=\"Actions\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Requisition Lines clickLink" + scripNumber);
				String xpath = "//h2[text()=\"param1\"]/following::a[text()=\"Actions\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Requisition Lines clickLink" + scripNumber);
			logger.error(e.getMessage());
		}

		try {
			if (param1.equalsIgnoreCase("Requisition Lines")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//table[@summary=\"" + param1 + "\"]//span[text()=\"Approved\"]/following::a[1]")));
				WebElement waittext = driver.findElement(
						By.xpath("//table[@summary=\"" + param1 + "\"]//span[text()=\"Approved\"]/following::a[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Requisition Lines clickLink" + scripNumber);
				String xpath = "//table[@summary=\"param1\"]//span[text()=\"Approved\"]/following::a[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Requisition Lines clickLink" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Details")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("(//*[contains(text(),\"" + param1 + "\")]/following::*[text()=\"" + param2 + "\"])[1]")));
				WebElement waittext = driver.findElement(
						By.xpath("(//*[contains(text(),\"" + param1 + "\")]/following::*[text()=\"" + param2 + "\"])[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Details" + scripNumber);

				String xpath = "(//*[contains(text(),\"param1\")]/following::*[text()=\"param2\"])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Details" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			// Changed == to equals method
			if (!param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[normalize-space(text())=\""
						+ param1 + "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]/following::a[1]")));
				Thread.sleep(4000);
				wait.until(
						ExpectedConditions
								.textToBePresentInElementLocated(
										By.xpath("//h1[normalize-space(text())=\"" + param1
												+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]"),
										param2));
				WebElement waittext = driver.findElement(By.xpath("//h1[normalize-space(text())=\"" + param1
						+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]/following::a[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked clickLink" + scripNumber);
				String xpath = "//h1[normalize-space(text())=\"param1\"]/following::label[normalize-space(text())=\"param2\"]/following::a[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickLink" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Journal")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[normalize-space(text())=\""
						+ param1 + "\"]/following::a[normalize-space(text())=\"" + param2 + "\"]")));
				Thread.sleep(4000);
				wait.until(
						ExpectedConditions
								.textToBePresentInElementLocated(
										By.xpath("//h1[normalize-space(text())=\"" + param1
												+ "\"]/following::a[normalize-space(text())=\"" + param2 + "\"]"),
										param2));
				WebElement waittext = driver.findElement(By.xpath("//h1[normalize-space(text())=\"" + param1
						+ "\"]/following::a[normalize-space(text())=\"" + param2 + "\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Journal clickLink" + scripNumber);
				String xpath = "//h1[normalize-space(text())=\"param1\"]/following::a[normalize-space(text())=\"param2\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Journal clickLink" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Receipt Details") || param1.equalsIgnoreCase("General Information")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"][1]")));
				Thread.sleep(4000);
				wait.until(
						ExpectedConditions
								.textToBePresentInElementLocated(
										By.xpath("//*[normalize-space(text())=\"" + param1
												+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]"),
										param2));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"][1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Receipt Details clickLink" + scripNumber);
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Receipt Details clickLink" + scripNumber);
		}
		try {
			if (param1.equalsIgnoreCase("View")) {
				Thread.sleep(3000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//a[text()=\"" + param1 + "\"][1]"))));
				WebElement waittext = driver.findElement(By.xpath("//a[text()=\"" + param1 + "\"][1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				Thread.sleep(5000);
				waittext.click();
				Thread.sleep(2000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked View clickLink" + scripNumber);
				String xpath = "//a[text()=\"param1\"][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during View clickLink" + scripNumber);
		}
		try {
			if (param1.equalsIgnoreCase("Invoice Actions")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(4000);
				WebElement Continue = driver
						.findElement(By.xpath("//div[text()=\"Warning\"]/following::button[text()=\"Continue\"]"));
				Continue.click();
				Thread.sleep(10000);
				WebElement waittext = driver.findElement(By.xpath("//a[normalize-space(text())=\"" + param1 + "\"][1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				Thread.sleep(2000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Invoice Actions clickLink" + scripNumber);
				String xpath = "//div[text()=\"Warning\"]/following::button[text()=\"Continue\"]" + ";"
						+ "//a[normalize-space(text())=\"param1\"][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
			try {
				// Changed == to equals method
				if (param2.equals("")) {
					Thread.sleep(3000);
					WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
					wait.until(
							ExpectedConditions.presenceOfElementLocated(By.xpath(("//a[text()=\"" + param1 + "\"][1]"))));
					WebElement waittext = driver.findElement(By.xpath("//a[text()=\"" + param1 + "\"][1]"));
					Actions actions = new Actions(driver);
					actions.moveToElement(waittext).build().perform();
					Thread.sleep(5000);
					waittext.click();
					Thread.sleep(2000);
					screenshot(driver, fetchMetadataVO, customerDetails);
					String scripNumber = fetchMetadataVO.getScriptNumber();
					logger.info("Sucessfully Clicked clickLink" + scripNumber);
					String params = param1;
					String xpath = "//a[text()=\"param1\"][1]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
					return;
				}
			} catch (Exception e) {
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.error("Failed during clickLink" + scripNumber);
				logger.error(e.getMessage());
			}
			try {
				// Changed == to equals method
				if (param2.equals("")) {
					Thread.sleep(3000);
					WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
					wait.until(ExpectedConditions
							.presenceOfElementLocated(By.xpath(("//a[contains(text(),\"" + param1 + "\")][1]"))));
					WebElement waittext = driver.findElement(By.xpath("//a[contains(text(),\"" + param1 + "\")][1]"));
					Actions actions = new Actions(driver);
					actions.moveToElement(waittext).build().perform();
					Thread.sleep(5000);
					waittext.click();
					Thread.sleep(2000);
					screenshot(driver, fetchMetadataVO, customerDetails);
					String scripNumber = fetchMetadataVO.getScriptNumber();
					logger.info("Sucessfully Clicked clickLink" + scripNumber);
					String params = param1;
					String xpath = "//a[contains(text()=\"param1\")][1]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
					return;
				}
			} catch (Exception e) {
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.error("Failed during clickLink" + scripNumber);
				logger.error(e.getMessage());
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Invoice Actions clickLink" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				Thread.sleep(3000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath(("//a[normalize-space(text())=\"" + param1 + "\"][1]"))));
				WebElement waittext = driver.findElement(By.xpath("//a[normalize-space(text())=\"" + param1 + "\"][1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				Thread.sleep(5000);
				waittext.click();
				Thread.sleep(2000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked clickLink" + scripNumber);
				String params = param1;
				String xpath = "//a[normalize-space(text())=\"param1\"][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickLink" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Reports and Analytics")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//h1[normalize-space(text())=\""
						+ param1 + "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]"))));
				WebElement waittext = driver.findElement(By.xpath(("//h1[normalize-space(text())=\"" + param1
						+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]")));
				Thread.sleep(5000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				Thread.sleep(2000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Reports and Analytics clickLink" + scripNumber);
				String xpath = "//h1[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Reports and Analytics clickLink" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Attachment") || param1.equalsIgnoreCase("Invoice Summary")
					|| param1.equalsIgnoreCase("Attachments")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//*[normalize-space(text())=\"" + param1 + "\"]/following::a[1]"))));
				WebElement waittext = driver
						.findElement(By.xpath(("//*[normalize-space(text())=\"" + param1 + "\"]/following::a[1]")));
				Thread.sleep(5000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				Thread.sleep(2000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Attachment or Invoice Summary clickLink" + scripNumber);
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::a[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Attachment or Invoice Summary clickLink" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::span)[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//h1[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::span)[1]"));
			Thread.sleep(5000);
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			waittext.click();
			Thread.sleep(2000);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked clickLink" + scripNumber);
			String xpath = "(//h1[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]/following::span)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickLink" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("//a[contains(text(),\"" + param1 + "\")]")));
				WebElement waittext = driver.findElement(By.xpath("//a[contains(text(),\"" + param1 + "\")]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked clickLink" + scripNumber);
				String params = param1;
				String xpath = "//a[contains(text(),\"param1\")]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickLink" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("(//a[contains(@id,\"" + param1 + "\")])[1]")));
				WebElement waittext = driver.findElement(By.xpath("(//a[contains(@id,\"" + param1 + "\")])[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked clickLink" + scripNumber);
				String params = param1;
				String xpath = "(//a[contains(@id,\"param1\")])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickLink" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@title=\"" + param1 + "\"]")));
				WebElement waittext = driver.findElement(By.xpath("//div[@title=\"" + param1 + "\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked clickLink" + scripNumber);
				String params = param1;
				String xpath = "//div[@title=\"param1\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickLink" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@title=\"" + param1 + "\"]")));
				WebElement waittext = driver.findElement(By.xpath("//a[@title=\"" + param1 + "\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked clickLink" + scripNumber);

				String params = param1;
				String xpath = "//a[@title=\"param1\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickLink" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("//*[contains(@title,\"" + param1 + "\")]")));
				WebElement waittext = driver.findElement(By.xpath("//*[contains(@title,\"" + param1 + "\")]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked clickLink" + scripNumber);
				String params = param1;
				String xpath = "//*[contains(@title,\"param1\")]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickLink" + scripNumber);
			logger.error(e.getMessage());
		}
		// Need to check for what purpose
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("(//div[contains(text(),\"" + param1 + "\")])[2]")));
				WebElement waittext = driver.findElement(By.xpath("(//div[contains(text(),\"" + param1 + "\")])[2]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked clickLink" + scripNumber);
				String params = param1;
				String xpath = "(//div[contains(text(),\"param1\")])[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickLink" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//a[@role=\"" + param1 + "\"]"))));
				WebElement waittext = driver.findElement(By.xpath(("//a[@role=\"" + param1 + "\"]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked clickLink" + scripNumber);
				String params = param1;
				String xpath = "//a[@role=\"param1\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickLink" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::a[normalize-space(text())=\"" + param2 + "\"][1]"))));
			WebElement waittext = driver.findElement(By.xpath(("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::a[normalize-space(text())=\"" + param2 + "\"][1]")));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			waittext.click();
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked clickLink" + scripNumber);
			String xpath = "//*[normalize-space(text())=\"param1\"]/following::a[normalize-space(text())=\"param2\"][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickLink" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			// throw e;
		}

		// DH 29
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath(("//h1[contains(text(),\"" + param1 + "\")]/following::*[text()=\"" + param2 + "\"][1]"))));
			WebElement waittext = driver.findElement(
					By.xpath(("//h1[contains(text(),\"" + param1 + "\")]/following::*[text()=\"" + param2 + "\"][1]")));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			waittext.click();
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked clickLink" + scripNumber);
			String xpath = "//h1[contains(text(),\"param1\")]/following::*[text()=\"param2\"][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickLink" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void clickRadiobutton(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {

		// DH 32
		try {
			if (param1.equalsIgnoreCase("Select Learning Item")) {

				Thread.sleep(5000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//*[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
								+ keysToSend + "\"]/preceding::input[@type=\"radio\"]"))));
				WebElement waittext = driver.findElement(
						By.xpath(("//*[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
								+ keysToSend + "\"]/preceding::input[@type=\"radio\"]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(500);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked clickRadiobutton" + scripNumber);
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"keysToSend\"]/preceding::input[@type=\"radio\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;

			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickRadiobutton" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
		}

		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath(("(//*[normalize-space(text())=\"" + param1 + "\"]/following::label[text()=\"" + param2
							+ "\"]/following::label[normalize-space(text())=\"" + keysToSend + "\"])[1]"))));
			wait.until(
					ExpectedConditions
							.textToBePresentInElementLocated(
									By.xpath("//*[normalize-space(text())=\"" + param1
											+ "\"]/following::label[normalize-space(text()))=\"" + param2 + "\"]"),
									param2));
			WebElement waittext = driver.findElement(
					By.xpath("(//*[normalize-space(text())=\"" + param1 + "\"]/following::label[normalize-space(text())=\""
							+ param2 + "\"]/following::label[normalize-space(text())=\"" + keysToSend + "\"])[1]"));
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(500);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked clickRadiobutton" + scripNumber);
			String xpath = "(//*[normalize-space(text())=\"param1\"]/following::label[text()=\"param2\"]/following::label[normalize-space(text())=\"keysToSend\"])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickRadiobutton" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("(//*[normalize-space(text())=\"" + param1
					+ "\"]/following::label[normalize-space(text())=\"" + keysToSend + "\"])[1]"))));
			WebElement waittext = driver.findElement(By.xpath(("(//*[normalize-space(text())=\"" + param1
					+ "\"]/following::label[normalize-space(text())=\"" + keysToSend + "\"])[1]")));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(500);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked clickRadiobutton" + scripNumber);
			String xpath = "(//*[normalize-space(text())=\"param1\"]/following::label[normalize-space(text())=\"keysToSend\"])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickRadiobutton" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					("//*[text()=\"" + param1 + "\"]/following::*[text()=\"" + keysToSend + "\"]/following::label"))));
			WebElement waittext = driver.findElement(By.xpath(
					("//*[text()=\"" + param1 + "\"]/following::*[text()=\"" + keysToSend + "\"]/following::label")));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(500);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked clickRadiobutton" + scripNumber);
			String xpath = "//*[text()=\"param1\"]/following::*[text()=\"keysToSend\"]/following::label";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickRadiobutton" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			WebElement waittext = driver.findElement(By.xpath(("//*[contains(text(),\"" + param1
					+ "\")]/following::*[normalize-space(text())=\"" + keysToSend + "\"]/preceding-sibling::input[1]")));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			Thread.sleep(500);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked clickRadiobutton" + scripNumber);
			String xpath = "//*[contains(text(),\"param1\")]/following::*[normalize-space(text())=\"keysToSend\"]/preceding-sibling::input[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickRadiobutton" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void clickCheckbox(WebDriver driver, String param1, String keysToSend, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			if (param1.equalsIgnoreCase("Compare and Award")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(
						ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[normalize-space(text())=\"" + param1
								+ "\"]/following::*[normalize-space(text())=\"" + keysToSend + "\"]/following::div[1]"))));
				// wait.until(
				// ExpectedConditions
				// .textToBePresentInElementLocated(
				// By.xpath("//*[normalize-space(text())=\"" +param1+
				// "\"]/following::*[normalize-space(text())=\"" +keysToSend+
				// "\"]/following::div[1]"),
				// keysToSend));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::*[normalize-space(text())=\"" + keysToSend + "\"]/following::div[1]"));
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(3000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked clickCheckbox" + scripNumber);
				String params = param1;
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"keysToSend\"]/following::label[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickCheckbox" + scripNumber);
			logger.error(e.getMessage());
		}
		// DH 31
		try {
			if (param1.equalsIgnoreCase("Bank Statement Lines")) {
				Thread.sleep(2000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[text()=\"" + param1
						+ "\"]/following::span[text()=\"" + keysToSend + "\"])[1]/preceding::label[1]")));

				WebElement waittext = driver.findElement(By.xpath("(//*[text()=\"" + param1
						+ "\"]/following::span[text()=\"" + keysToSend + "\"])[1]/preceding::label[1]"));
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(500);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Address Purpose clickCheckbox" + scripNumber);
				String params = param1;
				String xpath = "(//*[text()=\"param1\"]/following::span[text()=\"keysToSend\"])[1]/preceding::label[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Address Purpose clickCheckbox" + scripNumber);
			logger.error(e.getMessage());
		}

		// DH 50
		try {
			if (param1.equalsIgnoreCase("Create Expense Item") && keysToSend.equalsIgnoreCase("Receipt missing")) {
				Thread.sleep(2000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[normalize-space(text())=\""
						+ param1 + "\"]/following::label[normalize-space(text())=\"" + keysToSend + "\"]")));

				WebElement waittext = driver.findElement(By.xpath("//h1[normalize-space(text())=\"" + param1
						+ "\"]/following::label[normalize-space(text())=\"" + keysToSend + "\"]"));
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(500);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Address Purpose clickCheckbox" + scripNumber);
				String params = param1;
				String xpath = "//h1[normalize-space(text())=\"param1\"]/following::label[normalize-space(text())=\"keysToSend\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Address Purpose clickCheckbox" + scripNumber);
			logger.error(e.getMessage());
		}

		// DH 50
		try {
			if (param1.equalsIgnoreCase("Receipt Verification") && keysToSend.equalsIgnoreCase("Receipt Verified")) {
				Thread.sleep(2000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::label[normalize-space(text())=\"" + keysToSend + "\"]")));

				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::label[normalize-space(text())=\"" + keysToSend + "\"]"));
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(500);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Address Purpose clickCheckbox" + scripNumber);
				String params = param1;
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::label[normalize-space(text())=\"keysToSend\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Address Purpose clickCheckbox" + scripNumber);
			logger.error(e.getMessage());
		}

		// DH 31
		try {
			if (param1.equalsIgnoreCase("System Transactions")) {
				Thread.sleep(2000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[text()=\"" + param1
						+ "\"]/following::span[text()=\"" + keysToSend + "\"])[2]/preceding::label[1]")));

				WebElement waittext = driver.findElement(By.xpath("(//*[text()=\"" + param1
						+ "\"]/following::span[text()=\"" + keysToSend + "\"])[2]/preceding::label[1]"));
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(500);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Address Purpose clickCheckbox" + scripNumber);
				String params = param1;
				String xpath = "(//*[text()=\"param1\"]/following::span[text()=\"keysToSend\"])[2]/preceding::label[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Address Purpose clickCheckbox" + scripNumber);
			logger.error(e.getMessage());
		}

		// DH 25
		try {
			if (param1.equalsIgnoreCase("Supplier Contact")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						("(//*[normalize-space(text())=\"" + param1 + "\"]/following::input[@type=\"checkbox\"])[1]"))));
				WebElement waittext = driver.findElement(By.xpath(
						"(//*[normalize-space(text())=\"" + param1 + "\"]/following::input[@type=\"checkbox\"])[1]"));
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(500);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked clickCheckbox" + scripNumber);
				String params = param1;
				String xpath = "(//*[normalize-space(text())=\"param1\"]/following::input[@type=\"checkbox\"])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickCheckbox" + scripNumber);
			logger.error(e.getMessage());
			screenshotFail(driver, fetchMetadataVO, customerDetails);
		}

		// po.511
		try {
			if (param1.equalsIgnoreCase("Internal Responder")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						("(//*[normalize-space(text())=\"" + param1 + "\"]/following::input[@type=\"checkbox\"])[2]"))));
				WebElement waittext = driver.findElement(By.xpath(
						"(//*[normalize-space(text())=\"" + param1 + "\"]/following::input[@type=\"checkbox\"])[2]"));
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(500);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked clickCheckbox" + scripNumber);
				String params = param1;
				String xpath = "(//*[normalize-space(text())=\"param1\"]/following::input[@type=\"checkbox\"])[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickCheckbox" + scripNumber);
			logger.error(e.getMessage());
			screenshotFail(driver, fetchMetadataVO, customerDetails);
		}

		// DH 23
		try {
			if (param1.equalsIgnoreCase("Correct Unmatched Invoices")) {
				Thread.sleep(2000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()=\"" + param1
						+ "\"]/following::span[text()=\"" + keysToSend + "\"]/following::label[1]")));

				WebElement waittext = driver.findElement(By.xpath("//*[text()=\"" + param1
						+ "\"]/following::span[text()=\"" + keysToSend + "\"]/following::label[1]"));
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(500);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Address Purpose clickCheckbox" + scripNumber);
				String params = param1;
				String xpath = "//*[text()=\"param1\"]/following::span[text()=\"keysToSend\"]/following::label[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Address Purpose clickCheckbox" + scripNumber);
			logger.error(e.getMessage());
		}

		try {
			if (param1.equalsIgnoreCase("Item Description")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
								+ keysToSend + "\"]/preceding::label[contains(@id,\"Label\")][1]")));
				wait.until(
						ExpectedConditions.textToBePresentInElementLocated(
								By.xpath("//*[normalize-space(text())=\"" + param1
										+ "\"]/following::*[normalize-space(text())=\"" + keysToSend + "\"]"),
								keysToSend));
				WebElement waittext = driver.findElement(
						By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
								+ keysToSend + "\"]/preceding::label[contains(@id,\"Label\")][1]"));
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(500);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Item Description clickCheckbox" + scripNumber);
				String params = param1;
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"keysToSend\"]/preceding::label[contains(@id,\"Label\")][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Item Description clickCheckbox" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Address Purpose")) {
				Thread.sleep(2000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::*[normalize-space(text())=\"" + keysToSend + "\"]")));
				wait.until(
						ExpectedConditions.textToBePresentInElementLocated(
								By.xpath("//*[normalize-space(text())=\"" + param1
										+ "\"]/following::*[normalize-space(text())=\"" + keysToSend + "\"]"),
								keysToSend));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::*[normalize-space(text())=\"" + keysToSend + "\"]"));
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(500);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Address Purpose clickCheckbox" + scripNumber);
				String params = param1;
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"keysToSend\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Address Purpose clickCheckbox" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Scenario")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::span[normalize-space(text())=\"" + keysToSend + "\"]/preceding::input[1]")));
				wait.until(
						ExpectedConditions.textToBePresentInElementLocated(
								By.xpath("//*[normalize-space(text())=\"" + param1
										+ "\"]/following::span[normalize-space(text())=\"" + keysToSend + "\"]"),
								keysToSend));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::span[normalize-space(text())=\"" + keysToSend + "\"]/preceding::input[1]"));
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				// tab(driver, fetchMetadataVO, fetchConfigVO);
				Thread.sleep(500);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Scenario clickCheckbox" + scripNumber);
				String params = param1;
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::span[normalize-space(text())=\"keysToSend\"]/preceding::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Scenario clickCheckbox" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Address Purpose")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::label[normalize-space(text())=\"" + keysToSend + "\"]/preceding::input[1]")));
				wait.until(
						ExpectedConditions.textToBePresentInElementLocated(
								By.xpath("//*[normalize-space(text())=\"" + param1
										+ "\"]/following::label[normalize-space(text())=\"" + keysToSend + "\"]"),
								keysToSend));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::label[normalize-space(text())=\"" + keysToSend + "\"]/preceding::input[1]"));
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				// tab(driver, fetchMetadataVO, fetchConfigVO);
				Thread.sleep(500);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Address Purpose clickCheckbox" + scripNumber);
				String params = param1;
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::label[normalize-space(text())=\"keysToSend\"]/preceding::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Address Purpose clickCheckbox" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Name")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//a[normalize-space(text())=\"" + param1 + "\"]/following::input[1]")));
				WebElement waittext = driver
						.findElement(By.xpath("//a[normalize-space(text())=\"" + param1 + "\"]/following::input[1]"));
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				// tab(driver, fetchMetadataVO, fetchConfigVO);
				Thread.sleep(500);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Name clickCheckbox" + scripNumber);
				String xpath = "//a[normalize-space(text())=\"param1\"]/following::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Name clickCheckbox" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Match Invoice Lines")
					|| param1.equalsIgnoreCase("Correct Unmatched Invoices")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
								+ keysToSend + "\"]/following::label[contains(@id,\"Label\")][1]")));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[text()=\"" + param1 + "\"]"),
						param1));
				WebElement waittext = driver.findElement(By.xpath(
						"//*[normalize-space(text())=\"Match Invoice Lines\"]/following::*[normalize-space(text())=\"Match\"]/following::label[contains(@id,\"Label\")][1]"));
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(500);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Match Invoice Lines clickCheckbox" + scripNumber);
				String params = param1;
				String xpath = "//*[normalize-space(text())=\"Match Invoice Lines\"]/following::*[normalize-space(text())=\"Match\"]/following::label[contains(@id,\"Label\")][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Match Invoice Lines clickCheckbox" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//label[text()=\"" + param1
					+ "\"]/following::span[text()=\"" + keysToSend + "\"]/preceding::label[1]"))));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//label[text()=\"" + param1 + "\"]"),
					param1));
			WebElement waittext = driver.findElement(By.xpath("//label[normalize-space(text())=\"" + param1
					+ "\"]/following::span[normalize-space(text())=\"" + keysToSend + "\"]/preceding::label[1]"));
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(500);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked clickCheckbox" + scripNumber);
			String params = param1;
			String xpath = "//label[normalize-space(text())=\"param1\"]/following::span[normalize-space(text())=\"keysToSend\"]/preceding::label[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickCheckbox" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::span[normalize-space(text())=\"" + keysToSend + "\"]/preceding::label[1]"))));
			wait.until(
					ExpectedConditions
							.textToBePresentInElementLocated(
									By.xpath("//*[normalize-space(text())=\"" + param1
											+ "\"]/following::span[normalize-space(text())=\"" + keysToSend + "\"]"),
									keysToSend));
			WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::span[normalize-space(text())=\"" + keysToSend + "\"]/preceding::label[1]"));
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(500);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked clickCheckbox" + scripNumber);
			String params = param1;
			String xpath = "//*[normalize-space(text())=\"param1\"]/following::span[normalize-space(text())=\"keysToSend\"]/preceding::label[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickCheckbox" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//label[normalize-space(text())=\""
					+ param1 + "\"]/following::label[normalize-space(text())=\"" + keysToSend + "\"]"))));
			wait.until(
					ExpectedConditions.textToBePresentInElementLocated(
							By.xpath("//label[normalize-space(text())=\"" + param1
									+ "\"]/following::label[normalize-space(text())=\"" + keysToSend + "\"]"),
							keysToSend));
			WebElement waittext = driver.findElement(By.xpath("//label[normalize-space(text())=\"" + param1
					+ "\"]/following::label[normalize-space(text())=\"" + keysToSend + "\"]"));
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(500);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked clickCheckbox" + scripNumber);
			String params = param1;
			String xpath = "//label[normalize-space(text())=\"param1\"]/following::label[normalize-space(text())=\"keysToSend\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickCheckbox" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + keysToSend + "\"]"))));
			wait.until(
					ExpectedConditions
							.textToBePresentInElementLocated(
									By.xpath("//*[normalize-space(text())=\"" + param1
											+ "\"]/following::*[normalize-space(text())=\"" + keysToSend + "\"]"),
									keysToSend));
			WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + keysToSend + "\"]"));
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			Thread.sleep(3000);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked clickCheckbox" + scripNumber);
			String params = param1;
			String xpath = "//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"keysToSend\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickCheckbox" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath(("//label[normalize-space(text())=\"" + keysToSend + "\"]"))));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath("//label[normalize-space(text())=\"" + keysToSend + "\"]"), keysToSend));
			WebElement waittext = driver.findElement(By.xpath("//label[normalize-space(text())=\"" + keysToSend + "\"]"));
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(500);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked clickCheckbox" + scripNumber);
			String params = param1;
			String xpath = "//label[normalize-space(text())=\"keysToSend\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickCheckbox" + scripNumber);
			logger.error(e.getMessage());
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			// throw e;
		}
		// New code for PTP.Ex.111
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath(("(//*[contains(text(),\"" + param1 + "\")]/following::input[@type=\"checkbox\"])[1]"))));

			WebElement waittext = driver.findElement(
					By.xpath("(//*[contains(text(),\"" + param1 + "\")]/following::input[@type=\"checkbox\"])[1]"));
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(500);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked clickCheckbox" + scripNumber);
			String params = param1;
			String xpath = "(//*[contains(text(),\"param1\")]/following::input[@type=\"checkbox\"])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickCheckbox" + scripNumber);
			logger.error(e.getMessage());
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void clickLinkAction(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {

		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath(("//*[normalize-space(text())=\"" + param1 + "\"]/following::a[normalize-space(text())=\""
							+ keysToSend + "\"]/following::img[contains(@title,\"" + param2 + "\")][1]"))));
			Thread.sleep(2000);
			wait.until(
					ExpectedConditions
							.textToBePresentInElementLocated(
									By.xpath("//*[normalize-space(text())=\"" + param1
											+ "\"]/following::a[normalize-space(text())=\"" + keysToSend + "\"]"),
									keysToSend));
			WebElement waittext = driver.findElement(
					By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::a[normalize-space(text())=\""
							+ keysToSend + "\"]/following::img[contains(@title,\"" + param2 + "\")][1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			Thread.sleep(2000);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[normalize-space(text())=\"param1\"]/following::a[normalize-space(text())=\"keysToSend\"]/following::img[contains(@title,\"param2\")][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked clickLinkAction" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickLinkAction" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::a[normalize-space(text())=\"" + keysToSend + "\"]")));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::a[normalize-space(text())=\"" + keysToSend + "\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::a[normalize-space(text())=\"keysToSend\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked clickLinkAction" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickLinkAction" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"(//*[normalize-space(text())=\"" + keysToSend + "\"]/following::td[normalize-space(text())=\""
								+ param1 + "\"]/following::table[1]//div)[1]")));
				WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())=\"" + keysToSend
						+ "\"]/following::td[normalize-space(text())=\"" + param1 + "\"]/following::table[1]//div)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(1000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//*[normalize-space(text())=\"keysToSend\"]/following::td[normalize-space(text())=\"param1\"]/following::table[1]//div)[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked clickLinkAction" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickLinkAction" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath(("(//*[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
							+ keysToSend + "\"]/following::img[contains(@title,\"" + param2 + "\")])[1]"))));
			WebElement waittext = driver.findElement(
					By.xpath(("(//*[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
							+ keysToSend + "\"]/following::img[contains(@title,\"" + param2 + "\")])[1]")));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			Thread.sleep(2000);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"keysToSend\"]/following::img[contains(@title,\"param2\")])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked clickLinkAction" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickLinkAction" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath(("(//*[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
							+ keysToSend + "\"]/following::img[contains(@title,\"" + param2 + "\")])[1]"))));
			WebElement waittext = driver.findElement(
					By.xpath("(//*[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
							+ keysToSend + "\"]/following::img[contains(@title,\"" + param2 + "\")])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			Thread.sleep(2000);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"keysToSend\"]/following::img[contains(@title,\"param2\")])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked clickLinkAction" + scripNumber);
			return;
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickLinkAction" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public String textarea(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {
		try {
			if (param1.equalsIgnoreCase("Requirements")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("//*[text()=\""+param1+"\"]/following::div[contains(text(),\""+param2+"\")]/following::textarea[1]")));
				Thread.sleep(1000);
				WebElement waittill = driver.findElement(By.xpath("//*[text()=\""+param1+"\"]/following::div[contains(text(),\""+param2+"\")]/following::textarea[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				waittill.sendKeys(keysToSend);
				// typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO,
				// fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(500);
				return keysToSend;
			}
		} catch (Exception e) {
			logger.error("Failed during text area " + e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Text")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("//body[@contenteditable=\"true\"]")));
				Thread.sleep(1000);
				WebElement waittill = driver.findElement(By.xpath("//body[@contenteditable=\"true\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				waittill.sendKeys(keysToSend);
				// typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO,
				// fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(500);
				return keysToSend;
			}
		} catch (Exception e) {
			logger.error("Failed during text area " + e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Text")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("//body[@dir=\"ltr\"]")));
				Thread.sleep(1000);
				WebElement waittill = driver.findElement(By.xpath("//body[@dir=\"ltr\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				waittill.sendKeys(keysToSend);
				// typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO,
				// fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(500);
				return keysToSend;
			}
		} catch (Exception e) {
			logger.error("Failed during text area " + e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Description for Internal Candidates")||(param1.equalsIgnoreCase("Qualifications for Internal Candidates"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("//body[contains(@class,\"cke_editable\")][1]")));
				Thread.sleep(1000);
				WebElement waittill = driver.findElement(By.xpath("//body[contains(@class,\"cke_editable\")][1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				waittill.sendKeys(keysToSend);
				// typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO,
				// fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(500);
				return keysToSend;
			}
		} catch (Exception e) {
			logger.error("Failed during text area " + e.getMessage());
		}
		// HCM.ADM.1141 HCM.ADM.1142 HCM.ADM.1144 HS2 (textarea)
		try {
			if (param1.equalsIgnoreCase("Compose")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("//body[contains(@id,\"MessageContent\")]")));
				Thread.sleep(1000);
				WebElement waittill = driver.findElement(By.xpath("//body[contains(@id,\"MessageContent\")]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				waittill.sendKeys(keysToSend);
				// typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO,
				// fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(500);
				return keysToSend;
			}
		} catch (Exception e) {
			logger.error("Failed during text area " + e.getMessage());
		}

		// DH 20
		try {
			if (param1.equalsIgnoreCase("Create Note")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//body[@dir=\"ltr\"]")));
				Thread.sleep(1000);
				WebElement waittill = driver.findElement(By.xpath("//body[@dir=\"ltr\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				waittill.sendKeys(keysToSend);
				// typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO,
				// fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(500);
				String xpath = "//body[@dir=\"ltr\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return keysToSend;
			}
		} catch (Exception e) {
			logger.error("Failed during text area " + e.getMessage());
		}

		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())=\"" + param1
					+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]/following::textarea)[1]")));
			Thread.sleep(1000);
			wait.until(
					ExpectedConditions
							.textToBePresentInElementLocated(
									By.xpath("//*[normalize-space(text())=\"" + param1
											+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]"),
									param2));
			WebElement waittill = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]/following::textarea[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			Thread.sleep(500);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked textarea" + scripNumber);
			String xpath = "//*[normalize-space(text())=\"param1\"]/following::label[normalize-space(text())=\"param2\"]/following::textarea[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return keysToSend;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during textarea" + scripNumber);
			logger.error(e.getMessage());
		}
		// PROD
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[contains(text(),\"" + param1
					+ "\")]/following::label[normalize-space(text())=\"" + param2 + "\"]/following::textarea)[1]")));
			Thread.sleep(1000);
//			wait.until(
//			ExspectedConditions.textToBePresentInElementLocated(By.xpath("(//*[contains(text(),\"" + param1+ "\")]/following::label[normalize-space(text())=\"" + param2 + "\"]/following::textarea)[1]"), param2));
			WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1
					+ "\")]/following::label[normalize-space(text())=\"" + param2 + "\"]/following::textarea)[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			Thread.sleep(500);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked textarea" + scripNumber);
			String xpath = "(//*[contains(text(),\"param1\")]/following::label[normalize-space(text())=\"param2\"]/following::textarea)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return keysToSend;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during textarea" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"//*[text()=\"" + param1 + "\"]/following::*[text()=\"" + param2 + "\"]/following::textarea[1]")));
			Thread.sleep(1000);
//			wait.until(
//			ExspectedConditions.textToBePresentInElementLocated(By.xpath("(//*[contains(text(),\"" + param1+ "\")]/following::label[normalize-space(text())=\"" + param2 + "\"]/following::textarea)[1]"), param2));
			WebElement waittill = driver.findElement(By.xpath(
					"//*[text()=\"" + param1 + "\"]/following::*[text()=\"" + param2 + "\"]/following::textarea[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			Thread.sleep(500);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked textarea" + scripNumber);
			String xpath = "//*[text()=\"param1\"]/following::*[text()=\"param2\"]/following::textarea[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return keysToSend;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during textarea" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			Thread.sleep(5000);
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[text()=\"" + param1
					+ "\"]/following::label[text()=\"" + param2 + "\"])[2]/following::input[1]")));
			// wait.until(ExpectedConditions.textToBePresentInElementLocated(
			// By.xpath(""),
			// param2));
			WebElement waittill = driver.findElement(By.xpath("(//h1[text()=\"" + param1 + "\"]/following::label[text()=\""
					+ param2 + "\"])[2]/following::input[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			Thread.sleep(2000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked sendValue" + scripNumber);
			String xpath = "(//h1[text()=\"param1\"]/following::label[text()=\"param2\"])[2]/following::input[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return keysToSend;
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during sendValue" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			// throw e;
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//body[@dir=\"ltr\"]")));
			Thread.sleep(1000);
			WebElement waittill = driver.findElement(By.xpath("//body[@dir=\"ltr\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			screenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(500);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked textarea" + scripNumber);
			String xpath = "//body[@dir=\"ltr\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return keysToSend;
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during textarea" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public String sendValue(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {
		try {
			if (param1.equalsIgnoreCase("m/d/yy")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[@placeholder=\"" + param1 + "\"])[1]")));
				WebElement waittill = driver.findElement(By.xpath("(//*[@placeholder=\"" + param1 + "\"])[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Close Date sendValue" + scripNumber);
				String xpath = "(//*[@placeholder='m/d/yy'])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Date sendValue" + scripNumber);
		}
    
		try {
			if (param1.equalsIgnoreCase("Create Interview") && param2.equalsIgnoreCase("Interviewers")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[text()=\"" + param1
						+ "\"]/following::label[text()=\"" + param2 + "\"]/following::input)[1]")));
				WebElement waittill = driver.findElement(By.xpath("(//h1[text()=\"" + param1
						+ "\"]/following::label[text()=\"" + param2 + "\"]/following::input)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				waittill.sendKeys(keysToSend);
				// typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO,
				// fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(5000);

				WebElement selectvalue = driver.findElement(By.xpath("//*[text()=\"" + keysToSend + "\"]"));
				clickValidateXpath(driver, fetchMetadataVO, selectvalue, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Delegate to sendValue" + scripNumber);
				String xpath = "(//h1[text()=\"param1\"]/following::label[text()=\"param2\"]/following::input)[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Delegate to sendValue" + scripNumber);
			logger.error(e.getMessage());
		}
		try {

			if (param1.equalsIgnoreCase("Create Surrogate Response") || (param2.equalsIgnoreCase("Supplier Contact"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("//div[@class=\"AFDetectExpansion\"]/following::div[text()=\""
								+ param1 + "\"]/following::label[text()=\"" + param2 + "\"][2]/following::input[1]")));
				WebElement waittill = driver
						.findElement(By.xpath("//div[@class=\"AFDetectExpansion\"]/following::div[text()=\"" + param1
								+ "\"]/following::label[text()=\"" + param2 + "\"][2]/following::input[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				waittill.sendKeys(keysToSend);
				// typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO,
				// fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(2000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Close Date sendValue" + scripNumber);
				String xpath = "//div[@class=\"AFDetectExpansion\"]/following::div[text()=\"param1\"]/following::label[text()=\"param2\"][2]/following::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Close Date sendValue" + scripNumber);
			logger.error(e.getMessage());
		}
		// DH
		try {

			if (param1.equalsIgnoreCase("Search for proposed manager")
					|| (param1.equalsIgnoreCase("Select a value") || (param1.equalsIgnoreCase("Search for a Person")
							|| (param1.equalsIgnoreCase("Search for a learning item"))))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(
						ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@placeholder=\"" + param1 + "\"][1]")));
				WebElement waittill = driver.findElement(By.xpath("//*[@placeholder=\"" + param1 + "\"][1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				waittill.sendKeys(keysToSend);
				// typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO,
				// fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(2000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Close Date sendValue" + scripNumber);
				String xpath = "//*[@placeholder=\"param1\"][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Close Date sendValue" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("When") && param2.equalsIgnoreCase("End Date")
					|| param2.equalsIgnoreCase("End Date and Time")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()=\"" + param1
						+ "\"]/following::label[text()=\"" + param2 + "\"]/following::input[contains(@id,\"Ed\")][1]")));
				Thread.sleep(1000);
				WebElement waittill = driver.findElement(By.xpath("//*[text()=\"" + param1
						+ "\"]/following::label[text()=\"" + param2 + "\"]/following::input[contains(@id,\"Ed\")][1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				Thread.sleep(500);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked sendValue" + scripNumber);
				String xpath = "(//label[normalize-space(text())=\"param1\"]/following::label[normalize-space(text())=\"param2\"]/following::input)[1]";
				// service.saveXpathParams(param1, param2, scripNumber, xpath);
				return keysToSend;
			}
			// return keysToSend;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during sendValue" + scripNumber);
			logger.error(e.getMessage());
			// throw e;
		}
		// HS2
		try {
			if (param1.equalsIgnoreCase("job Details") && (param2.equalsIgnoreCase("Start Time"))
					|| (param1.equalsIgnoreCase("job Details") && (param2.equalsIgnoreCase("Start Time")))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h2[text()=\"" + param1
						+ "\"]/following::label[text()=\"" + param2 + "\"]/following::input)[1]")));
				WebElement waittill = driver.findElement(By.xpath("(//h2[text()=\"" + param1
						+ "\"]/following::label[text()=\"" + param2 + "\"]/following::input)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Close Date sendValue" + scripNumber);
				String xpath = "(//h2[text()=\"param1\"]/following::label[text()=\"param2\"]/following::input)[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Close Date sendValue" + scripNumber);
			logger.error(e.getMessage());
		}
		// prod
		try {
			if (param1.equalsIgnoreCase("Maintain Managers") || (param1.equalsIgnoreCase("Position Details"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"(//*[text()=\"" + param1 + "\"]/following::*[text()=\"" + param2 + "\"]/following::input)[1]")));
				WebElement waittill = driver.findElement(By.xpath(
						"(//*[text()=\"" + param1 + "\"]/following::*[text()=\"" + param2 + "\"]/following::input)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				waittill.sendKeys(keysToSend);
				// typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO,
				// fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(2000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Close Date sendValue" + scripNumber);
				String xpath = "(//*[text()=\"param1\"]/following::*[text()=\"param2\"]/following::input)[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Close Date sendValue" + scripNumber);
			logger.error(e.getMessage());
		}
		// prod
		try {
			if (param1.equalsIgnoreCase("Add Activities")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[contains(text(),\"" + param1 + "\")]/following::input[1]")));
				WebElement waittill = driver
						.findElement(By.xpath("//*[contains(text(),\"" + param1 + "\")]/following::input[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Close Date sendValue" + scripNumber);
				String xpath = "//*[contains(text(),\"param1\")]/following::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Close Date sendValue" + scripNumber);
			logger.error(e.getMessage());
		}
		// prod
		try {
			if (param1.equalsIgnoreCase("Create Line") && (param2.equalsIgnoreCase("Name"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[text()=\"" + param1
						+ "\"]/following::label[text()=\"" + param2 + "\"]/following::input[@aria-live=\"off\"][1]")));
				WebElement waittill = driver.findElement(By.xpath("//div[text()=\"" + param1
						+ "\"]/following::label[text()=\"" + param2 + "\"]/following::input[@aria-live=\"off\"][1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Close Date sendValue" + scripNumber);
				String xpath = "//div[text()=\"param1\"]/following::label[text()=\"param2\"]/following::input[@aria-live=\"off\"][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Close Date sendValue" + scripNumber);
			logger.error(e.getMessage());
		}

		// DH 65
		try {

			if (param1.equalsIgnoreCase("Initial Due Date Option")) {

				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[text()=\"" + param1 + "\"]/following::*[@placeholder=\"dd-mmm-yyyy\"][1]")));

				WebElement waittill = driver.findElement(
						By.xpath("//*[text()=\"" + param1 + "\"]/following::*[@placeholder=\"dd-mmm-yyyy\"][1]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);

				Thread.sleep(1000);

				String scripNumber = fetchMetadataVO.getScriptNumber();

				logger.info("Sucessfully Clicked Invoice Dates sendValue" + scripNumber);

				String xpath = "//*[text()=\"param1\"]/following::*[@placeholder=\"dd-mmm-yyyy\"][1]";

				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return keysToSend;

			}

		} catch (Exception e) {

			String scripNumber = fetchMetadataVO.getScriptNumber();

			logger.error("Failed during Invoice Dates sendValue" + scripNumber);

			logger.error(e.getMessage());

		}

		// DH 55
		try {
			if (param1.equalsIgnoreCase("DH Transaction Detail Report")
					&& (param2.equalsIgnoreCase("Business Unit") || param2.equalsIgnoreCase("Customer Name"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("//*[text()=\"" + param1 + "\"]//following::input[1]")));
				WebElement waittill = driver.findElement(By.xpath("//*[text()=\"" + param1 + "\"]//following::input[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Close Date sendValue" + scripNumber);
				String xpath = "//*[text()=\"param1\"]//following::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Close Date sendValue" + scripNumber);
			logger.error(e.getMessage());
		}

		// DH 39 OTC.AR.236
		try {
			if (param1.equalsIgnoreCase("Create Contact Point") && param2.equalsIgnoreCase("Phone")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[text()=\"" + param1
						+ "\"]/following::label[text()=\"" + param2 + "\"]/following::input[1]")));
				WebElement waittill = driver.findElement(By.xpath("//div[text()=\"" + param1
						+ "\"]/following::label[text()=\"" + param2 + "\"]/following::input[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Close Date sendValue" + scripNumber);
				String xpath = "//div[text()=\"param1\"]/following::label[text()=\"param2\"]/following::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Close Date sendValue" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param2.equalsIgnoreCase("Delegate To") && param1.equalsIgnoreCase("Approval Delegations")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h2[text()=\"" + param1
						+ "\"]//following::label[text()=\"" + param2 + "\"]//following::input)[1]")));
				WebElement waittill = driver.findElement(By.xpath("(//h2[text()=\"" + param1
						+ "\"]//following::label[text()=\"" + param2 + "\"]//following::input)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				waittill.sendKeys(keysToSend);
				// typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO,
				// fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(10000);

				WebElement selectvalue = driver.findElement(By.xpath("//*[text()=\"" + keysToSend + "\"]"));
				clickValidateXpath(driver, fetchMetadataVO, selectvalue, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Delegate to sendValue" + scripNumber);
				String xpath = "(//h2[text()=\"param1\"]//following::label[text()=\"param2\"]//following::input)[1]" + ";"
						+ "//*[text()=\"keysToSend\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Delegate to sendValue" + scripNumber);
			logger.error(e.getMessage());
		}

		// DH 39 OTC.AR.236
		try {
			if (param1.equalsIgnoreCase("Create Address")
					&& (param2.equalsIgnoreCase("Phone Country Code") || param2.equalsIgnoreCase("Phone Area Code")
							|| param2.equalsIgnoreCase("Phone") || param2.equalsIgnoreCase("Phone Extension"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"//div[text()=\"" + param1 + "\"]/following::label[text()=\"Phone\"]/following::label[text()=\""
								+ param2 + "\"]/preceding::input[1]")));
				WebElement waittill = driver.findElement(By.xpath(
						"//div[text()=\"" + param1 + "\"]/following::label[text()=\"Phone\"]/following::label[text()=\""
								+ param2 + "\"]/preceding::input[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Close Date sendValue" + scripNumber);
				String xpath = "//div[text()=\"param1\"]/following::label[text()=\"Phone\"]/following::label[text()=\"param2\"]/preceding::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Close Date sendValue" + scripNumber);
			logger.error(e.getMessage());
		}

		// DH 32
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("//*[contains(@id,\"popup-container\")]//*[text()=\"" + param1
							+ "\"]/following::*[text()=\"" + param2 + "\"]/following::input[not (@type=\"hidden\")][1]")));
			WebElement waittill = driver.findElement(By.xpath("//*[contains(@id,\"popup-container\")]//*[text()=\""
					+ param1 + "\"]/following::*[text()=\"" + param2 + "\"]/following::input[not (@type=\"hidden\")][1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			screenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked Close Date sendValue" + scripNumber);
			String xpath = "//*[contains(@id,\"popup-container\")]//*[text()=\"param1\"]/following::*[text()=\"param2\"]/following::input[not (@type=\"hidden\")][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return keysToSend;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Close Date sendValue" + scripNumber);
			logger.error(e.getMessage());
		}
		// DH 39 SCM.PM.509

		try {
			if (param1.equalsIgnoreCase("Security")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"//*[text()=\"" + param1 + "\"]/following::span[text()=\"" + param2 + "\"]/following::input[1]")));
				WebElement waittill = driver.findElement(By.xpath(
						"//*[text()=\"" + param1 + "\"]/following::span[text()=\"" + param2 + "\"]/following::input[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Close Date sendValue" + scripNumber);
				String xpath = "//*[text()=\"param1\"]/following::span[text()=\"param2\"]/following::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Close Date sendValue" + scripNumber);
			logger.error(e.getMessage());
		}

		// DH 12
		try {
			if (param2.equalsIgnoreCase("Close Date: Fixed")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("(//*[text()=\"" + param1 + "\"]/following::label[text()=\""
								+ param2 + "\"]/preceding-sibling::input[not(@type=\"hidden\")])[1]")));
				WebElement waittill = driver
						.findElement(By.xpath("(//*[text()=\"" + param1 + "\"]/following::label[text()=\"" + param2
								+ "\"]/preceding-sibling::input[not(@type=\"hidden\")])[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Close Date sendValue" + scripNumber);
				String xpath = "(//*[text()=\"param1\"]/following::label[text()=\"param2\"]/preceding-sibling::input[not(@type=\"hidden\")])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Close Date sendValue" + scripNumber);
			logger.error(e.getMessage());
		}

		try {
			if (param1.equalsIgnoreCase("Password")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type=\"" + param1 + "\"]")));
				WebElement waittill = driver.findElement(By.xpath("//input[@type=\"" + param1 + "\"]"));
				JavascriptExecutor jse = (JavascriptExecutor) driver;
				jse.executeScript("document.getElementById(\"password\").value = \"" + keysToSend + "\";");
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Password sendValue" + scripNumber);
				String xpath = "//input[@type=\"param1\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
//                                       String scriptID=fetchMetadataVO.getScriptId();String lineNumber=fetchMetadataVO.getLineNumber();service.saveXpathParams(scriptID, lineNumber, xpath);
//                String lineNumber=fetchMetadataVO.getLineNumber();
//                service.saveXpathParams(scriptID,metadataID);
				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Password sendValue" + scripNumber);
			logger.error(e.getMessage());
		}
		// Here Adding xpath for invoice dates AP.452

		try {

			if (param1.equalsIgnoreCase("Invoice Date")) {

				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()=\"" + param1
						+ "\"]//following::*[contains(text(),\"" + param2 + "\")]/preceding::input[2]")));

				WebElement waittill = driver.findElement(By.xpath("//*[text()=\"" + param1
						+ "\"]//following::*[contains(text(),\"" + param2 + "\")]/preceding::input[2]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);

				Thread.sleep(1000);

				String scripNumber = fetchMetadataVO.getScriptNumber();

				logger.info("Sucessfully Clicked Invoice Dates sendValue" + scripNumber);

				String xpath = "//*[text()=\"param1\"]//following::*[contains(text(),\"param2\")]/preceding::input[2]";

				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return keysToSend;

			}

		} catch (Exception e) {

			String scripNumber = fetchMetadataVO.getScriptNumber();

			logger.error("Failed during Invoice Dates  sendValue" + scripNumber);

			logger.error(e.getMessage());

		}
		try {

			if ((param1.equalsIgnoreCase("Application Accounting Date") || param1.equalsIgnoreCase("Accounting Date"))
					&& param2.equalsIgnoreCase("Start Date")) {

				WebElement waittill = driver
						.findElement(By.xpath(" //label[@title=\"" + param1 + "\"]/following::input[1]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				Thread.sleep(500);

				String scripNumber = fetchMetadataVO.getScriptNumber();

				logger.info("Sucessfully Clicked Payables to Ledger Reconciliation Report sendValue" + scripNumber);

				String xpath = " //label[@title=\"param1\"]/following::input[1]";

				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return keysToSend;

			}

		} catch (Exception e) {

			String scripNumber = fetchMetadataVO.getScriptNumber();

			logger.error("Failed during Payables to Ledger Reconciliation Report sendValue" + scripNumber);

			logger.error(e.getMessage());

		}

		try {
			if (param2.equalsIgnoreCase("Delegate to: ") || param1.equalsIgnoreCase("Search: Invoice")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[text()=\"" + param1
						+ "\"]//following::label[text()=\"" + param2 + "\"]//following::input[1]")));
				WebElement waittill = driver.findElement(By.xpath("//h1[text()=\"" + param1
						+ "\"]//following::label[text()=\"" + param2 + "\"]//following::input[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Delegate to sendValue" + scripNumber);
				String xpath = "//h1[text()=\"param1\"]//following::label[text()=\"param2\"]//following::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Delegate to  sendValue" + scripNumber);
			logger.error(e.getMessage());
		}

		try {

			if ((param1.equalsIgnoreCase("Application Accounting Date") || param1.equalsIgnoreCase("Accounting Date"))
					&& param2.equalsIgnoreCase("End Date")) {

				WebElement waittill = driver
						.findElement(By.xpath(" //label[@title=\"" + param1 + "\"]/following::input[2]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				Thread.sleep(500);

				String scripNumber = fetchMetadataVO.getScriptNumber();

				logger.info("Sucessfully Clicked Payables to Ledger Reconciliation Report sendValue" + scripNumber);

				String xpath = " //label[@title=\"param1\"]/following::input[2]";

				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return keysToSend;

			}

		} catch (Exception e) {

			String scripNumber = fetchMetadataVO.getScriptNumber();

			logger.error("Failed during Payables to Ledger Reconciliation Report sendValue" + scripNumber);

			logger.error(e.getMessage());

		}
		try {
			if (param1.equalsIgnoreCase("Reports and Analytics")
					|| param1.equalsIgnoreCase("Notifications") && param2.equalsIgnoreCase("Search")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::input[@placeholder=\"" + param2 + "\"][1]")));
				WebElement waittill = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::input[@placeholder=\"" + param2 + "\"][1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Reports and Analytics or Search sendValue" + scripNumber);
				String xpath = "//*[normalize-space(text())=\" param1 \"]/following::input[@placeholder=\" param2 \"][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Reports and Analytics or Search  sendValue" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Report")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[contains(text(),\"" + param2 + "\")]/following::input[1]")));
				WebElement waittill = driver
						.findElement(By.xpath("//*[contains(text(),\"" + param2 + "\")]/following::input[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				Thread.sleep(8000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Report sendValue" + scripNumber);
				String xpath = "//*[contains(text(),\"param2\")]/following::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Report sendValue" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Create Bank Account")
					&& (param2.equalsIgnoreCase("Account Number") || param2.equalsIgnoreCase("IBAN"))
					|| param2.equalsIgnoreCase("Delegate to")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[contains(text(),\"" + param1
						+ "\")]/following::label[contains(text(),\"" + param2 + "\")]/following::input)[1]")));
				Thread.sleep(1000);
				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1
						+ "\")]/following::label[contains(text(),\"" + param2 + "\")]/following::input)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				Thread.sleep(500);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Create Expense Item sendValue" + scripNumber);
				String xpath = "(//*[contains(text(),\"param1\")]/following::label[contains(text(),\"param2\")]/following::input)[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked Create Expense Item sendValue" + scripNumber);

				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Create Expense Item sendValue" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Create Bank Account")
					&& (param2.equalsIgnoreCase("Bank") || param2.equalsIgnoreCase("Bank Branch"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[text()=\"" + param1
						+ "\"]/following::label[text()=\"" + param2 + "\"]/following::input)[1]")));
				Thread.sleep(1000);
				WebElement waittill = driver.findElement(By.xpath("(//*[text()=\"" + param1
						+ "\"]/following::label[text()=\"" + param2 + "\"]/following::input)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				Thread.sleep(500);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Create Expense Item sendValue" + scripNumber);

				String xpath = "(//*[text()=\"param1\"]/following::label[text()=\"param2\"]/following::input)[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				logger.info("Sucessfully Clicked Create Expense Item sendValue" + scripNumber);

				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Create Expense Item sendValue" + scripNumber);
			logger.error(e.getMessage());
		}

		try {
			if (param2.equalsIgnoreCase("Phone") || param2.equalsIgnoreCase("Mobile")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())=\""
						+ param1 + "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::input)[3]")));
				WebElement waittill = driver.findElement(By.xpath("(//*[normalize-space(text())=\"" + param1
						+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::input)[3]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				Thread.sleep(8000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Phone or Mobile sendValue" + scripNumber);
				String xpath = "(//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]/following::input)[3]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Clicked Phone or Mobile sendValue" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Create Line") && param2.equalsIgnoreCase("Name")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"(//div[normalize-space(text())=\"" + param1 + "\"]/following::label[normalize-space(text())=\""
								+ param2 + "\"]/following::input)[2]")));
				WebElement waittill = driver.findElement(By.xpath("(//div[normalize-space(text())=\"" + param1
						+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]/following::input)[2]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				Thread.sleep(8000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Create Line or Name sendValue" + scripNumber);
				String xpath = "(//div[normalize-space(text())=\" param1 \"]/following::label[normalize-space(text())=\" param2 \"]/following::input)[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Create Line or Name  sendValue" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Create Time Card") && param2.equalsIgnoreCase("Person Name")) {
				Thread.sleep(4000);

				WebElement waittill = driver.findElement(By.xpath("//div[text()=\"" + param1

						+ "\"]/following::span[text()=\"" + param2 + "\"]//input[1]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Create Time Card or Person Name sendValue" + scripNumber);
				String xpath = "//div[text()=\"param1\"]/following::span[text()=\"param2\"]//input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return keysToSend;

			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Create Time Card or Person Name sendValue" + scripNumber);
			logger.error(e.getMessage());

		}

		try {
			if (param1.equalsIgnoreCase("Lines") && param2.equalsIgnoreCase("Query By Example")) {
				Thread.sleep(8000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[normalize-space(text())=\""
						+ param1 + "\"]/following::*[@title=\"" + param2 + "\"]/following::input)[1]")));
				WebElement waittill = driver.findElement(By.xpath("(//h1[normalize-space(text())=\"" + param1
						+ "\"]/following::*[@title=\"" + param2 + "\"]/following::input)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				Thread.sleep(3000);
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(10000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Lines or Query By Example sendValue" + scripNumber);
				String xpath = "(//h1[normalize-space(text())=\"param1\"]/following::*[@title=\"param2\"]/following::input)[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Lines or Query By Example  sendValue" + scripNumber);
			logger.error(e.getMessage());
		}

		try {
			if (param1.equalsIgnoreCase("Driver\"s Licenses") || param2.equalsIgnoreCase("Unapply Accounting Date")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"(//*[normalize-space(text())=\"" + param1 + "\"]/following::label[normalize-space(text())=\""
								+ param2 + "\"]/following::input)[1]")));
				Thread.sleep(5000);
				wait.until(
						ExpectedConditions
								.textToBePresentInElementLocated(
										By.xpath("//*[normalize-space(text())=\"" + param1
												+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]"),
										param2));
				WebElement waittill = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]/following::input[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				Thread.sleep(8000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Unapply Accounting Date sendValue" + scripNumber);
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::label[normalize-space(text())=\"param2\"]/following::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Unapply Accounting Date sendValue" + scripNumber);
			logger.error(e.getMessage());
		}
		try {

			if (param1.equalsIgnoreCase("Accounting Period-Filter")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				try {
					WebElement waittill = driver.findElement(By.xpath("//*[contains(@id,\"PeriodName::content\")]"));
					Actions actions = new Actions(driver);
					actions.moveToElement(waittill).build().perform();
					typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
					String scripNumber = fetchMetadataVO.getScriptNumber();

					String xpath = "//*[contains(@id,\"PeriodName::content\")]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);

					Thread.sleep(2000);
				} catch (Exception e) {
					WebElement filter = driver.findElement(By.xpath("//img[@title=\"Query By Example\"]"));
					Actions actions = new Actions(driver);
					actions.moveToElement(filter).build().perform();
					filter.click();
					Thread.sleep(5000);
					WebElement waittill = driver.findElement(By.xpath("//*[contains(@id,\"PeriodName::content\")]"));
					actions.moveToElement(waittill).build().perform();
					typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
					Thread.sleep(2000);
					String scripNumber = fetchMetadataVO.getScriptNumber();
					logger.error("Failed during Accounting Period-Filter sendValue" + scripNumber);
					String xpath = "//img[@title=\"Query By Example\"]" + ";"
							+ "//*[contains(@id,\"PeriodName::content\")]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
				}

				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Accounting Period-Filter sendValue" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Manage Accounting Periods")
					|| param1.equalsIgnoreCase("Edit Accounting Period Statuses")
					|| param2.equalsIgnoreCase("Query By Example")) {

				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[contains(text(),\"" + param1
						+ "\")]/following::*[@title=\"" + param2 + "\"]/following::input)[1]")));
				// wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[contains(text(),\""+param1+"\")]/following::*[@title=\""+param2+"\"]"),
				// param2));
				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1
						+ "\")]/following::*[@title=\"" + param2 + "\"]/following::input)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				Thread.sleep(2000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Manage Accounting Periods sendValue" + scripNumber);
				String xpath = "(//*[contains(text(),\"param1\")]/following::*[@title=\"param2\"]/following::input)[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Manage Accounting Periods sendValue" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Reports and Analytics") && param2.equalsIgnoreCase("Search")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::input[@placeholder=\"" + param2 + "\"][1]")));
				WebElement waittill = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::input[@placeholder=\"" + param2 + "\"][1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Reports and Analytics or Search sendValue" + scripNumber);
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::input[@placeholder=\"param2\"][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Reports and Analytics or Search  sendValue" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Payables to Ledger Reconciliation Report")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("(//*[contains(text(),\"" + param2 + "\")]/following::input)[2]")));
				Thread.sleep(1000);
				WebElement waittill = driver
						.findElement(By.xpath("(//*[contains(text(),\"" + param2 + "\")]/following::input)[2]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				Thread.sleep(500);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				logger.info("Sucessfully Clicked Payables to Ledger Reconciliation Report sendValue" + scripNumber);

				String xpath = "(//*[contains(text(),\"param2\")]/following::input)[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Payables to Ledger Reconciliation Report sendValue" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Daily Rates")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"//a[normalize-space(text())=\"" + param1 + "\"]/following::label[normalize-space(text())=\""
								+ param2 + "\"]/preceding::input[not (@type=\"hidden\")][1]")));
				WebElement waittill = driver.findElement(By.xpath(
						"//a[normalize-space(text())=\"" + param1 + "\"]/following::label[normalize-space(text())=\""
								+ param2 + "\"]/preceding::input[not (@type=\"hidden\")][1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Daily Rates  sendValue" + scripNumber);
				String xpath = "//a[normalize-space(text())=\"param1\"]/following::label[normalize-space(text())=\"param2\"]/preceding::input[not (@type=\"hidden\")][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  Daily Rates sendValue" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Edit Line") && param2.equalsIgnoreCase("Category Name")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[contains(text(),\"" + param1
						+ "\")]/following::label[normalize-space(text())=\"" + param2 + "\"]/following::input)[1]")));
				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1
						+ "\")]/following::label[normalize-space(text())=\"" + param2 + "\"]/following::input)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked sendValue" + scripNumber);
				String xpath = "(//*[contains(text(),\"param1\")]/following::label[normalize-space(text())=\"param2\"]/following::input)[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return keysToSend;
			}
		} catch (Exception e) {
			// TODO: handle exception
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during sendValue" + scripNumber);
		}
		// DH 15
		try {
			if (param1.equalsIgnoreCase("Manage Divisions")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("(//*[text()=\"" + param1 + "\"]/following::label[text()=\""
								+ param2 + "\"]/following::input[contains(@id,\"qry\") and not (@role)])[1]")));
				Thread.sleep(1000);
				WebElement waittill = driver
						.findElement(By.xpath("(//*[text()=\"" + param1 + "\"]/following::label[text()=\"" + param2
								+ "\"]/following::input[contains(@id,\"qry\") and not (@role)])[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				Thread.sleep(500);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked sendValue" + scripNumber);
				String xpath = "(//*[text()=\"param1\"]/following::label[text()=\"param2\"]/following::input[contains(@id,\"qry\") and not (@role)])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				// service.saveXpathParams(param1, param2, scripNumber, xpath);
				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during sendValue" + scripNumber);
			logger.error(e.getMessage());
		}
		// DH 35
		try {

			if (param1.equalsIgnoreCase("When") && param2.equalsIgnoreCase("End Date")
					|| param2.equalsIgnoreCase("End Date and Time")) {

				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()=\"" + param1
						+ "\"]/following::label[text()=\"" + param2 + "\"]/following::input[contains(@id,\"Ed\")][1]")));

				Thread.sleep(1000);

				WebElement waittill = driver.findElement(By.xpath("//*[text()=\"" + param1
						+ "\"]/following::label[text()=\"" + param2 + "\"]/following::input[contains(@id,\"Ed\")][1]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				Thread.sleep(500);

				String scripNumber = fetchMetadataVO.getScriptNumber();

				logger.info("Sucessfully Clicked sendValue" + scripNumber);

				// service.saveXpathParams(param1, param2, scripNumber, xpath);

				String xpath = "//*[text()=\"param1\"]/following::label[text()=\"param2\"]/following::input[contains(@id,\"Ed\")][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				// service.saveXpathParams(param1, param2, scripNumber, xpath);

				return keysToSend;

			}

			// return keysToSend;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during sendValue" + scripNumber);
			logger.error(e.getMessage());
			// throw e;
		}
		try {
			if (param1.equalsIgnoreCase("Create Expense Item") && param2.equalsIgnoreCase("Amount")) {
				Thread.sleep(10000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("(//h1[contains(text(),\"" + param1 + "\")]/following::label[normalize-space(text())=\""
								+ param2 + "\"]/following::input[@type=\"text\"])[2]")));
				Thread.sleep(1000);
				WebElement waittill = driver.findElement(
						By.xpath("(//h1[contains(text(),\"" + param1 + "\")]/following::label[normalize-space(text())=\""
								+ param2 + "\"]/following::input[@type=\"text\"])[2]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				Thread.sleep(500);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Create Expense Item sendValue" + scripNumber);
				String xpath = "(//h1[contains(text(),\"param1\")]/following::label[normalize-space(text())=\"param2\"]/following::input[@type=\"text\"])[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked Create Expense Item sendValue" + scripNumber);

				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Create Expense Item sendValue" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			Thread.sleep(10000);
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h2[contains(text(),\"" + param1
					+ "\")]/following::label[normalize-space(text())=\"" + param2 + "\"]/following::input)[1]")));
			Thread.sleep(1000);
			wait.until(
					ExpectedConditions
							.textToBePresentInElementLocated(
									By.xpath("//h2[contains(text(),\"" + param1
											+ "\")]/following::label[normalize-space(text())=\"" + param2 + "\"]"),
									param2));
			WebElement waittill = driver.findElement(By.xpath("//h2[contains(text(),\"" + param1
					+ "\")]/following::label[normalize-space(text())=\"" + param2 + "\"]/following::input[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			Thread.sleep(500);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked sendValue" + scripNumber);
			String xpath = "(//h2[contains(text(),\"param1\")]/following::label[normalize-space(text())=\"param2\"]/following::input)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return keysToSend;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during sendValue" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[contains(text(),\"" + param1
					+ "\")]/following::label[normalize-space(text())=\"" + param2 + "\"]/following::input)[1]")));
			Thread.sleep(1000);
			wait.until(
					ExpectedConditions
							.textToBePresentInElementLocated(
									By.xpath("//*[contains(text(),\"" + param1
											+ "\")]/following::label[normalize-space(text())=\"" + param2 + "\"]"),
									param2));

			WebElement waittill = driver.findElement(By.xpath("//*[contains(text(),\"" + param1
					+ "\")]/following::label[normalize-space(text())=\"" + param2 + "\"]/following::input[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			Thread.sleep(500);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//*[contains(text(),\"param1\")]/following::label[normalize-space(text())=\"param2\"]/following::input)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked sendValue" + scripNumber);
			return keysToSend;
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during sendValue" + scripNumber);
		}
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("//*[contains(@placeholder,\"" + param1 + "\")]")));
				WebElement waittill = driver.findElement(By.xpath("//*[contains(@placeholder,\"" + param1 + "\")]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				// waittill.sendKeys(keysToSend);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked sendValue" + scripNumber);
				String xpath = "//*[contains(@placeholder,\"param1\")]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during sendValue" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[contains(text(),\"" + param1
					+ "\")]/following::label[normalize-space(text())=\"" + param2 + "\"]/following::input)[1]")));
			Thread.sleep(1000);
			wait.until(
					ExpectedConditions
							.textToBePresentInElementLocated(
									By.xpath("//h1[contains(text(),\"" + param1
											+ "\")]/following::label[normalize-space(text())=\"" + param2 + "\"]"),
									param2));
			WebElement waittill = driver.findElement(By.xpath("//h1[contains(text(),\"" + param1
					+ "\")]/following::label[normalize-space(text())=\"" + param2 + "\"]/following::input[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			String scripNumber = fetchMetadataVO.getScriptNumber();

			String xpath = "(//h1[contains(text(),\"param1\")]/following::label[normalize-space(text())=\"param2\"]/following::input)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			Thread.sleep(5000);
			return keysToSend;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during sendValue" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//label[normalize-space(text())=\""
					+ param1 + "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]/following::input)[1]")));
			Thread.sleep(1000);
			wait.until(
					ExpectedConditions
							.textToBePresentInElementLocated(
									By.xpath("//label[normalize-space(text())=\"" + param1
											+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]"),
									param2));
			WebElement waittill = driver.findElement(By.xpath("//label[normalize-space(text())=\"" + param1
					+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]/following::input[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			Thread.sleep(500);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked sendValue" + scripNumber);
			String xpath = "(//label[normalize-space(text())=\"param1\"]/following::label[normalize-space(text())=\"param2\"]/following::input)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return keysToSend;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during sendValue" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())=\"" + param1
					+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]/following::input)[1]")));
			Thread.sleep(5000);
			wait.until(
					ExpectedConditions
							.textToBePresentInElementLocated(
									By.xpath("//*[normalize-space(text())=\"" + param1
											+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]"),
									param2));
			WebElement waittill = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]/following::input[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			Thread.sleep(8000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked sendValue" + scripNumber);
			String xpath = "(//*[normalize-space(text())=\"param1\"]/following::label[normalize-space(text())=\"param2\"]/following::input)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return keysToSend;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during sendValue" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::input)[1]")));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[normalize-space(text())=\""
					+ param1 + "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]"), param2));
			WebElement waittill = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::input[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			Thread.sleep(2000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked sendValue" + scripNumber);
			String xpath = "(//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]/following::input)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return keysToSend;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during sendValue" + scripNumber);
			logger.error(e.getMessage());
		}

		try {
			Thread.sleep(5000);
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())=\"" + param1
					+ "\"]/following::img[@title=\"" + param2 + "\"]/following::input)[1]")));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::img[@title=\"" + param2 + "\"]"),
					param2));
			WebElement waittill = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::img[@title=\"" + param2 + "\"]/following::input[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			Thread.sleep(2000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked sendValue" + scripNumber);
			String xpath = "(//*[normalize-space(text())=\"param1\"]/following::img[@title=\"param2\"]/following::input)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return keysToSend;
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during sendValue" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			// throw e;
		}
		try {
			Thread.sleep(5000);
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By
					.xpath("//*[text()=\"" + param1 + "\"]/following::*[text()=\"" + param2 + "\"]/following::input[1]")));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath("//*[text()=\"" + param1 + "\"]/following::*[text()=\"" + param2 + "\"]/following::input[1]"),
					param2));
			WebElement waittill = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::img[@title=\"" + param2 + "\"]/following::input[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			Thread.sleep(2000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked sendValue" + scripNumber);
			String xpath = "//*[normalize-space(text())=\"param1\"]/following::img[@title=\"param2\"]/following::input[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return keysToSend;
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during sendValue" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			// throw e;
		}
		// PPM.PA.002 DH
		try {
			Thread.sleep(10000);
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[contains(text(),\"" + param1
					+ "\")]/following::label[text()=\"" + param2 + "\"]/following::input)[1]")));
			Thread.sleep(1000);
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("(//h1[contains(text(),\"" + param1
					+ "\")]/following::label[text()=\"" + param2 + "\"]/following::input)[1]"), param2));
			WebElement waittill = driver.findElement(By.xpath("(//h1[contains(text(),\"" + param1
					+ "\")]/following::label[text()=\"" + param2 + "\"]/following::input)[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			Thread.sleep(500);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked sendValue" + scripNumber);
			String xpath = "(//h1[contains(text(),\"param1\")]/following::label[text()=\"param2\"]/following::input)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return keysToSend;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during sendValue" + scripNumber);
			logger.error(e.getMessage());
			throw e;
		}

	}

	public void dropdownTexts(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {
		try {

			if (param2.equalsIgnoreCase("Postal Code") || param2.equalsIgnoreCase("Legal Entity")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"//div[contains(@id,\"dropdownPopup::popup-container\")]//a[contains(text(),\"Search\")][1]")));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath(
						"//div[contains(@id,\"dropdownPopup::popup-container\")]//a[contains(text(),\"Search\")][1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"//div[contains(@id,\"PopupId::content\")]//*[text()=\"Search\"]/following::*[normalize-space(text())=\""
								+ param2 + "\"]/following::input[1]")));
				WebElement searchResult = driver.findElement(By.xpath(
						"//div[contains(@id,\"PopupId::content\")]//*[text()=\"Search\"]/following::*[normalize-space(text())=\""
								+ param2 + "\"]/following::input[1]"));
				typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
				String xpath = "//div[contains(@id,\"dropdownPopup::popup-container\")]//a[contains(text(),\"Search\")][1]"
						+ ";"
						+ "//div[contains(@id,\"PopupId::content\")]//*[text()=\"Search\"]/following::*[normalize-space(text())=\"param2\"]/following::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				if (keysToSend != null) {

					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);

					Thread.sleep(5000);

					WebElement text = driver.findElement(
							By.xpath("(//div[@class=\"AFDetectExpansion\"]/following::span[normalize-space(text())=\""
									+ param2 + "\"]/following::table//span[text()])[1]"));

					text.click();

					Thread.sleep(1000);

					WebElement button = driver
							.findElement(By.xpath("//*[text()=\"Search\"]/following::*[normalize-space(text())=\"" + param2
									+ "\"]/following::*[text()=\"OK\"][1]"));

					button.click();
					String scripNumber = fetchMetadataVO.getScriptNumber();
					logger.info("Sucessfully Clicked Postal Code Legal Entity dropdownTexts" + scripNumber);
					String xpath1 = "(//div[@class=\"AFDetectExpansion\"]/following::span[normalize-space(text())=\"param2\"]/following::table//span[text()])[1]"
							+ ";"
							+ "//*[text()=\"Search\"]/following::*[normalize-space(text())=\"param2\"]/following::*[text()=\"OK\"][1]";
					String scriptID1 = fetchMetadataVO.getScriptId();
					String metadataID1 = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID1, metadataID1, xpath1);
				}

				return;

			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Postal Code Legal Entity  dropdownTexts" + scripNumber);

			logger.error(e.getMessage());

		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"(//div[contains(@id,\"popup-container\")]//*[normalize-space(text())=\"" + keysToSend + "\"])[1]")));
			Thread.sleep(4000);
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(
					"(//div[contains(@id,\"popup-container\")]//*[normalize-space(text())=\"" + keysToSend + "\"])[1]"),
					keysToSend));
			WebElement waittext = driver.findElement(By.xpath(
					"(//div[contains(@id,\"popup-container\")]//*[normalize-space(text())=\"" + keysToSend + "\"])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			Thread.sleep(2000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked dropdownTexts" + scripNumber);
			String xpath = "(//div[contains(@id,\"popup-container\")]//*[normalize-space(text())=\"keysToSend\"])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during dropdownTexts" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("(//div[contains(@id,\"dropdownPopup::dropDownContent\")]//*[normalize-space(text())=\""
							+ keysToSend + "\"])[1]")));
			Thread.sleep(4000);
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath("(//div[contains(@id,\"dropdownPopup::dropDownContent\")]//*[normalize-space(text())=\""
							+ keysToSend + "\"])[1]"),
					keysToSend));
			WebElement waittext = driver.findElement(
					By.xpath("(//div[contains(@id,\"dropdownPopup::dropDownContent\")]//*[normalize-space(text())=\""
							+ keysToSend + "\"])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			Thread.sleep(2000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked dropdownTexts" + scripNumber);
			String xpath = "(//div[contains(@id,\"dropdownPopup::dropDownContent\")]//*[normalize-space(text())=\"keysToSend\"])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during dropdownTexts" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"//div[contains(@id,\"dropdownPopup::dropDownContent\")]/following::a[contains(text(),\"Search\")][1]")));
//		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//a[contains(text(),\"Search\")]"), "Search"));
			WebElement search = driver.findElement(By.xpath(
					"//div[contains(@id,\"dropdownPopup::dropDownContent\")]/following::a[contains(text(),\"Search\")][1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(search).build().perform();
			search.click();
			Thread.sleep(10000);
			// wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@id,\"PopupId::content\")]//*[text()=\"Search\"]/following::*[text()=\""+param2+"\"]/following::input[1]")));
			WebElement searchResult = driver.findElement(By.xpath(
					"//div[contains(@id,\"PopupId::content\")]//*[text()=\"Search\"]/following::*[normalize-space(text())=\""
							+ param2 + "\"]/following::input[1]"));
			typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
			String xpath1 = "//div[contains(@id,\"dropdownPopup::dropDownContent\")]/following::a[contains(text(),\"Search\")][1]";
			String xpath2 = "//div[contains(@id,\"PopupId::content\")]//*[text()=\"Search\"]/following::*[normalize-space(text())=\"param2\"]/following::input[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			String xpath = xpath1 + ";" + xpath2;
			if (keysToSend != null) {
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
				try {
					WebElement text = driver.findElement(By.xpath(
							"//div[@class=\"AFDetectExpansion\"]/following::span[text()=\"Name\"]/following::span[normalize-space(text())=\""
									+ keysToSend + "\"]"));
					String scripNumber = fetchMetadataVO.getScriptNumber();
					logger.info("Sucessfully Clicked dropdownTexts" + scripNumber);
					text.click();
					xpath = xpath + ";"
							+ "//div[@class=\"AFDetectExpansion\"]/following::span[text()=\"Name\"]/following::span[normalize-space(text())=\"keysToSend\"]";

				} catch (Exception e) {
					String scripNumber = fetchMetadataVO.getScriptNumber();
					logger.error("Failed during dropdownTexts" + scripNumber);
					WebElement text = driver
							.findElement(By.xpath("(//span[contains(text(),\"" + keysToSend + "\")])[1]"));
					text.click();
					xpath = xpath + ";" + "(//span[contains(text(),\"keysToSend\")])[1]";
				}
			}
			try {
				WebElement button = driver
						.findElement(By.xpath("//*[text()=\"Search\"]/following::*[normalize-space(text())=\"" + param2
								+ "\"]/following::*[not (@aria-disabled) and text()=\"K\"][1]"));
				button.click();
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked dropdownTexts" + scripNumber);
				xpath = xpath + ";"
						+ "//*[text()=\"Search\"]/following::*[normalize-space(text())=\"param2\"]/following::*[not (@aria-disabled) and text()=\"K\"][1]";
			} catch (Exception e) {
				WebElement button = driver
						.findElement(By.xpath("//*[text()=\"Search\"]/following::*[normalize-space(text())=\"" + param2
								+ "\"]/following::*[not (@aria-disabled) and text()=\"OK\"][1]"));
				button.click();
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.error("Failed during dropdownTexts" + scripNumber);
				xpath = xpath + ";"
						+ "//*[text()=\"Search\"]/following::*[normalize-space(text())=\"param2\"]/following::*[not (@aria-disabled) and text()=\"OK\"][1]";
			}
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during dropdownTexts" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"//div[contains(@id,\"PopupId::content\")]/following::*[normalize-space(text())=\"Search\"]/following::*[text()=\"Name\"]/following::input[@type=\"text\"][1]")));
			WebElement searchResult = driver.findElement(By.xpath(
					"//div[contains(@id,\"PopupId::content\")]/following::*[normalize-space(text())=\"Search\"]/following::*[text()=\"Name\"]/following::input[@type=\"text\"][1]"));
			typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
			enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(5000);
			WebElement text = driver.findElement(By.xpath("(//span[contains(text(),\"" + keysToSend + "\")])[1]"));
			text.click();
			Thread.sleep(1000);
			WebElement button = driver.findElement(
					By.xpath("//*[text()=\"Search\"]/following::*[text()=\"Name\"]/following::*[text()=\"OK\"][1]"));
			button.click();
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked dropdownTexts" + scripNumber);
			String xpath1 = "//div[contains(@id,\"PopupId::content\")]/following::*[normalize-space(text())=\"Search\"]/following::*[text()=\"Name\"]/following::input[@type=\"text\"][1]";
			String xpath2 = "(//span[contains(text(),\"keysToSend\")])[1]";
			String xpath3 = "//*[text()=\"Search\"]/following::*[text()=\"Name\"]/following::*[text()=\"OK\"][1]";
			String xpath = xpath1 + ";" + xpath2 + ";" + xpath3;
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during dropdownTexts" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[contains(text(),\"" + param1
					+ "\")]/following::label[text()=\"" + keysToSend + "\"]/following::input)[1]")));
			Thread.sleep(1000);
			wait.until(
					ExpectedConditions.textToBePresentInElementLocated(
							By.xpath("//h1[contains(text(),\"" + param1
									+ "\")]/following::label[normalize-space(text())=\"" + keysToSend + "\"]"),
							keysToSend));
			WebElement waittill = driver.findElement(By.xpath("//h1[contains(text(),\"" + param1
					+ "\")]/following::label[normalize-space(text())=\"" + keysToSend + "\"]/following::input[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			Thread.sleep(500);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked dropdownTexts" + scripNumber);
			String xpath = "//h1[contains(text(),\"param1\")]/following::label[normalize-space(text())=\"keysToSend\"]/following::input[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during dropdownTexts" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//div[@class=\"AFDetectExpansion\"]/following::a[contains(text(),\"Search\")][1]")));
//			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//a[contains(text(),\"Search\")]"), "Search"));
			WebElement search = driver.findElement(
					By.xpath("//div[@class=\"AFDetectExpansion\"]/following::a[contains(text(),\"Search\")][1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(search).build().perform();
			search.click();
			String xpath = "//div[@class=\"AFDetectExpansion\"]/following::a[contains(text(),\"Search\")][1]";
			Thread.sleep(10000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked dropdownTexts" + scripNumber);
			try {
				WebElement searchResult = driver.findElement(
						By.xpath("//div[contains(@id,\"PopupId::content\")]//*[text()=\"Search\"]/following::*[text()=\""
								+ param2 + "\"]/following::input[1]"));
				typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
				String scripNumber1 = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked dropdownTexts" + scripNumber1);
				xpath = xpath + ";"
						+ "//div[contains(@id,\"PopupId::content\")]//*[text()=\"Search\"]/following::*[text()=\"param2\"]/following::input[1]";
			} catch (Exception e) {
				WebElement searchResult = driver.findElement(By.xpath(
						"//div[contains(@id,\"PopupId::content\")]//*[text()=\"Search\"]/following::*[text()=\"Name\"]/following::input[1]"));
				typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
				String scripNumber1 = fetchMetadataVO.getScriptNumber();
				logger.error("Failed during dropdownTexts" + scripNumber1);
				xpath = xpath + ";"
						+ "//div[contains(@id,\"PopupId::content\")]//*[text()=\"Search\"]/following::*[text()=\"Name\"]/following::input[1]";
			}
			if (keysToSend != null) {
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
				try {
					WebElement text = driver.findElement(By.xpath(
							"//div[@class=\"AFDetectExpansion\"]/following::span[text()=\"Name\"]/following::span[text()=\""
									+ keysToSend + "\"]"));
					text.click();
					String scripNumber1 = fetchMetadataVO.getScriptNumber();
					logger.info("Sucessfully Clicked dropdownTexts" + scripNumber1);
					xpath = xpath + ";"
							+ "//div[@class=\"AFDetectExpansion\"]/following::span[text()=\"Name\"]/following::span[text()=\"keysToSend\"]";
				} catch (Exception e) {
					WebElement text = driver
							.findElement(By.xpath("(//span[contains(text(),\"" + keysToSend + "\")])[1]"));
					text.click();
					String scripNumber1 = fetchMetadataVO.getScriptNumber();
					logger.error("Failed during dropdownTexts" + scripNumber1);
					xpath = xpath + ";" + "(//span[contains(text(),\"keysToSend\")])[1]";
				}
			}
			try {
				WebElement button = driver
						.findElement(By.xpath("//*[text()=\"Search\"]/following::*[normalize-space(text())=\"" + param2
								+ "\"]/following::*[not (@aria-disabled) and text()=\"K\"][1]"));
				String scripNumber1 = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked dropdownTexts" + scripNumber1);
				button.click();
				xpath = xpath + ";"
						+ "//*[text()=\"Search\"]/following::*[normalize-space(text())=\"param2\"]/following::*[not (@aria-disabled) and text()=\"K\"][1]";
			} catch (Exception e) {
				WebElement button = driver
						.findElement(By.xpath("//*[text()=\"Search\"]/following::*[normalize-space(text())=\"" + param2
								+ "\"]/following::*[not (@aria-disabled) and text()=\"OK\"][1]"));
				button.click();
				String scripNumber1 = fetchMetadataVO.getScriptNumber();
				logger.error("Failed during dropdownTexts" + scripNumber1);
				xpath = xpath + ";"
						+ "//*[text()=\"Search\"]/following::*[normalize-space(text())=\"param2\"]/following::*[not (@aria-disabled) and text()=\"OK\"][1]";
			}
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during dropdownTexts" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void multiplelinestableSendKeys(WebDriver driver, String param1, String param2, String param3,
			String keysToSend, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) throws Exception {
		try {

			if (param1.equalsIgnoreCase("Time Entry")) {
				Thread.sleep(4000);

				WebElement waittill = driver.findElement(By.xpath("(//*[text()=\"" + param1

						+ "\"]/following::label[text()=\"" + param2 + "\"]/preceding-sibling::input)[2]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//*[text()=\"param1\"]/following::label[text()=\"param2\"]/preceding-sibling::input)[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				logger.info("Sucessfully Clicked Time Entry multiplelinestableSendKeys" + scripNumber);

				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Time Entry multiplelinestableSendKeys" + scripNumber);
			logger.error(e.getMessage());

		}
		try {

			if (param1.equalsIgnoreCase("Mon")) {
				Thread.sleep(1000);
				WebElement saturday = driver.findElement(By.xpath("(//td[contains(text(),\"Saturday\")])[1]"));

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()=\"" + param2 + "\"]/preceding-sibling::input[1])[8]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "(//td[contains(text(),\"Saturday\")])[1]" + ";"
						+ "(//*[contains(text(),\"param1\")]/following::label[text()=\"param2\"]/preceding-sibling::input[1])[8]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				screenshot(driver, fetchMetadataVO, customerDetails);

				return;
			}
			if (param1.equalsIgnoreCase("Tue")) {

				Thread.sleep(1000);
				WebElement saturday = driver.findElement(By.xpath("(//td[contains(text(),\"Saturday\")])[1]"));
				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()=\"" + param2 + "\"]/preceding-sibling::input[1])[9]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				String xpath = "(//td[contains(text(),\"Saturday\")])[1]" + ";"
						+ "(//*[contains(text(),\"param1\")]/following::label[text()=\"param2\"]/preceding-sibling::input[1])[9]";
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				screenshot(driver, fetchMetadataVO, customerDetails);

				return;
			}
			if (param1.equalsIgnoreCase("Wed")) {
				WebElement saturday = driver.findElement(By.xpath("(//td[contains(text(),\"Saturday\")])[1]"));

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()=\"" + param2 + "\"]/preceding-sibling::input[1])[10]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "(//td[contains(text(),\"Saturday\")])[1]" + ";"
						+ "(//*[contains(text(),\"param1\")]/following::label[text()=\"param2\"]/preceding-sibling::input[1])[10]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				screenshot(driver, fetchMetadataVO, customerDetails);

				return;
			}
			if (param1.equalsIgnoreCase("Thu")) {

				WebElement saturday = driver.findElement(By.xpath("(//td[contains(text(),\"Saturday\")])[1]"));
				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()=\"" + param2 + "\"]/preceding-sibling::input[1])[11]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//td[contains(text(),\"Saturday\")])[1]" + ";"
						+ "(//*[contains(text(),\"param1\")]/following::label[text()=\"param2\"]/preceding-sibling::input[1])[11]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				screenshot(driver, fetchMetadataVO, customerDetails);

				return;
			}
			if (param1.equalsIgnoreCase("Fri")) {
				WebElement saturday = driver.findElement(By.xpath("(//td[contains(text(),\"Saturday\")])[1]"));

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()=\"" + param2 + "\"]/preceding-sibling::input[1])[12]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//td[contains(text(),\"Saturday\")])[1]" + ";"
						+ "(//*[contains(text(),\"param1\")]/following::label[text()=\"param2\"]/preceding-sibling::input[1])[12]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				screenshot(driver, fetchMetadataVO, customerDetails);

				return;
			}
			if (param1.equalsIgnoreCase("Sat")) {

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()=\"" + param2 + "\"]/preceding-sibling::input[1])[13]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//*[contains(text(),\"param1\")]/following::label[text()=\"param2\"]/preceding-sibling::input[1])[13]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				screenshot(driver, fetchMetadataVO, customerDetails);

				return;
			}
			if (param1.equalsIgnoreCase("Sunday")) {

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()=\"" + param2 + "\"]/preceding-sibling::input[1])[14]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//*[contains(text(),\"param1\")]/following::label[text()=\"param2\"]/preceding-sibling::input[1])[14]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				screenshot(driver, fetchMetadataVO, customerDetails);

				return;
			}
		} catch (Exception e) {

			logger.error("Failed during multiple lines table SendKeys " + e.getMessage());

		}

		try {

			if (param1.equalsIgnoreCase("Mon")) {
				Thread.sleep(1000);
				WebElement saturday = driver.findElement(By.xpath("(//td[contains(text(),\"Sat\")])[3]"));

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()=\"" + param2 + "\"]/preceding-sibling::input[1])[8]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "(//td[contains(text(),\"Sat\")])[3]" + ";"
						+ "(//*[contains(text(),\"param1\")]/following::label[text()=\"param2\"]/preceding-sibling::input[1])[8]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked Mon multiplelinestableSendKeys" + scripNumber);

				return;
			}
			if (param1.equalsIgnoreCase("Tue")) {

				Thread.sleep(1000);
				WebElement saturday = driver.findElement(By.xpath("(//td[contains(text(),\"Sat\")])[3]"));
				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()=\"" + param2 + "\"]/preceding-sibling::input[1])[9]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//td[contains(text(),\"Sat\")])[3]" + ";"
						+ "(//*[contains(text(),\"param1\")]/following::label[text()=\"param2\"]/preceding-sibling::input[1])[9]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked Mon multiplelinestableSendKeys" + scripNumber);

				return;
			}
			if (param1.equalsIgnoreCase("Wed")) {
				WebElement saturday = driver.findElement(By.xpath("(//td[contains(text(),\"Sat\")])[3]"));

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()=\"" + param2 + "\"]/preceding-sibling::input[1])[10]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "(//td[contains(text(),\"Sat\")])[3]" + ";"
						+ "(//*[contains(text(),\"param1\")]/following::label[text()=\"param2\"]/preceding-sibling::input[1])[10]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				screenshot(driver, fetchMetadataVO, customerDetails);

				return;
			}
			if (param1.equalsIgnoreCase("Thu")) {

				WebElement saturday = driver.findElement(By.xpath("(//td[contains(text(),\"Sat\")])[3]"));
				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()=\"" + param2 + "\"]/preceding-sibling::input[1])[11]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();
				String scripNumber = fetchMetadataVO.getScriptNumber();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				String xpath = "(//td[contains(text(),\"Sat\")])[3]" + ";"
						+ "(//*[contains(text(),\"param1\")]/following::label[text()=\"param2\"]/preceding-sibling::input[1])[11]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				screenshot(driver, fetchMetadataVO, customerDetails);

				return;
			}
			if (param1.equalsIgnoreCase("Fri")) {
				WebElement saturday = driver.findElement(By.xpath("(//td[contains(text(),\"Sat\")])[3]"));

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()=\"" + param2 + "\"]/preceding-sibling::input[1])[12]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "(//td[contains(text(),\"Sat\")])[3]" + ";"
						+ "(//*[contains(text(),\"param1\")]/following::label[text()=\"param2\"]/preceding-sibling::input[1])[12]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				screenshot(driver, fetchMetadataVO, customerDetails);

				return;
			}
			if (param1.equalsIgnoreCase("Sat")) {

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()=\"" + param2 + "\"]/preceding-sibling::input[1])[13]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "(//*[contains(text(),\"param1\")]/following::label[text()=\"param2\"]/preceding-sibling::input[1])[13]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				screenshot(driver, fetchMetadataVO, customerDetails);

				return;
			}
			if (param1.equalsIgnoreCase("Sunday")) {

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()=\"" + param2 + "\"]/preceding-sibling::input[1])[14]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();
				String scripNumber = fetchMetadataVO.getScriptNumber();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				String xpath = "(//*[contains(text(),\"param1\")]/following::label[text()=\"param2\"]/preceding-sibling::input[1])[14]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				screenshot(driver, fetchMetadataVO, customerDetails);

				return;
			}
		} catch (Exception e) {

			logger.error("Failed during multiple lines table SendKeys " + e.getMessage());

		}

		try {

			if (param1.equalsIgnoreCase("Mon")) {
				Thread.sleep(1000);

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()=\"" + param2 + "\"]/preceding-sibling::input[1])[6]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "(//*[contains(text(),\"param1\")]/following::label[text()=\"param2\"]/preceding-sibling::input[1])[6]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				screenshot(driver, fetchMetadataVO, customerDetails);

				return;
			}
			if (param1.equalsIgnoreCase("Tue")) {

				Thread.sleep(1000);
				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()=\"" + param2 + "\"]/preceding-sibling::input[1])[7]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "(//*[contains(text(),\"param1\")]/following::label[text()=\"param2\"]/preceding-sibling::input[1])[7]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				screenshot(driver, fetchMetadataVO, customerDetails);

				return;
			}
			if (param1.equalsIgnoreCase("Wed")) {
				Thread.sleep(1000);

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()=\"" + param2 + "\"]/preceding-sibling::input[1])[8]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Wed multiplelinestableSendKeys" + scripNumber);
				String xpath = "(//*[contains(text(),\"param1\")]/following::label[text()=\"param2\"]/preceding-sibling::input[1])[8]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
			if (param1.equalsIgnoreCase("Thu")) {

				Thread.sleep(1000);
				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()=\"" + param2 + "\"]/preceding-sibling::input[1])[9]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//*[contains(text(),\"param1\")]/following::label[text()=\"param2\"]/preceding-sibling::input[1])[9]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked Thu multiplelinestableSendKeys" + scripNumber);
				return;
			}
			if (param1.equalsIgnoreCase("Fri")) {
				Thread.sleep(1000);

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()=\"" + param2 + "\"]/preceding-sibling::input[1])[10]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//*[contains(text(),\"param1\")]/following::label[text()=\"param2\"]/preceding-sibling::input[1])[10]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked Fri multiplelinestableSendKeys" + scripNumber);
				return;
			}
			if (param1.equalsIgnoreCase("Sat")) {

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()=\"" + param2 + "\"]/preceding-sibling::input[1])[13]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//*[contains(text(),\"param1\")]/following::label[text()=\"param2\"]/preceding-sibling::input[1])[13]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked Sat multiplelinestableSendKeys" + scripNumber);
				return;
			}
			if (param1.equalsIgnoreCase("Sunday")) {

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()=\"" + param2 + "\"]/preceding-sibling::input[1])[14]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//*[contains(text(),\"param1\")]/following::label[text()=\"param2\"]/preceding-sibling::input[1])[14]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked Sunday multiplelinestableSendKeys" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  multiplelinestableSendKeys" + scripNumber);
			logger.error(e.getMessage());

		}

	}

	public void tableSendKeys(WebDriver driver, String param1, String param2, String param3, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {
		try {
			if (param1.equalsIgnoreCase("ABC Classes") && param2.equalsIgnoreCase("Sequence")) {
				WebElement waittill = driver
						.findElement(By.xpath("//*[text()=\"" + param1 + "\"]/following::label[text()= \"" + param2 + "\"]/preceding::input[1]"));
				Thread.sleep(1000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked tableSendKeys" + scripNumber);
				String xpath = "//*[text()='ABC Classes']/following::label[text()='Sequence']/preceding::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  tableSendKeys" + scripNumber);
			logger.error(e.getMessage());
		}
	
		try {
			if ((param1.equalsIgnoreCase("Create Cost Scenario ") && (param2.equalsIgnoreCase("Cost Book")
					|| param2.equalsIgnoreCase("Effective Date") || param2.equalsIgnoreCase("Cost Organization")))) {
				WebElement waittill = driver
						.findElement(By.xpath("//*[text()=\"" + param1 + "\"]//following::*[text()=\"" + param2 + "\"]//following::input[1]"));
				Thread.sleep(1000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked tableSendKeys" + scripNumber);
				String xpath = "//*[text()=\"" + param1 + "\"]//following::*[text()=\"" + param2 + "\"]//following::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  tableSendKeys" + scripNumber);
			logger.error(e.getMessage());
		}
		
		try {
			if (param1.equalsIgnoreCase("Standard Cost Details") && param2.equalsIgnoreCase("Cost Element")) {
				WebElement waittill = driver.findElement(By.xpath("//*[text()=\"" + param1 + "\"]/following::*[text()=\"" + param2 + "\"][1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked tableSendKeys" + scripNumber);
				String xpath = "//*[text()=\"Standard Cost Details\"]/following::*[text()=\"Cost Element\"][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during tableSendKeys" + scripNumber);
			logger.error(e.getMessage());
		}
		
		try {
			if (param1.equalsIgnoreCase("Cost Element")) {
				WebElement waittill = driver.findElement(By.xpath("(//*[text()=\"" + param1 + "\"]//following::input)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked tableSendKeys" + scripNumber);
				String xpath = "(//*[text()='Cost Element']//following::input)[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during tableSendKeys" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if ((param1.equalsIgnoreCase("Create Job Requisition") && param2.equalsIgnoreCase("Recruiter"))) {
				Thread.sleep(6000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(6000);

				WebElement waittill = driver
						.findElement(By.xpath("(//h1[text()=\"" + param1 + "\"]/following::label[text()=\"" + param2
								+ "\"]/preceding-sibling::input[not(@type=\"hidden\")])[1]"));
				Thread.sleep(1000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				Thread.sleep(6000);
				// values.sendKeys(keysToSend);
				// typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO,
				// fetchMetadataVO);
				waittill.sendKeys(keysToSend);
				Thread.sleep(1000);
				WebElement select = driver.findElement(By.xpath("//*[text()=\"" + keysToSend + "\"]"));
				clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO, customerDetails);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked tableSendKeys" + scripNumber);
				String xpath = "(//h1[text()=\"param1\"]/following::label[text()=\"param2\"]/preceding-sibling::input[not(@type=\"hidden\")])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  tableSendKeys" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if ((param1.equalsIgnoreCase("Inspection Results") && param2.equalsIgnoreCase("Characteristic")
					|| param2.equalsIgnoreCase("Result"))) {
				WebElement waittill = driver
						.findElement(By.xpath("(//h1[text()=\"" + param1 + "\"]/following::label[text()=\"" + param2
								+ "\"]/preceding-sibling::input[not(@type=\"hidden\")])[1]"));
				Thread.sleep(1000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				Thread.sleep(6000);
				// values.sendKeys(keysToSend);
				// typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO,
				// fetchMetadataVO);
				waittill.sendKeys(keysToSend);
				Thread.sleep(1000);
				WebElement select = driver.findElement(By.xpath("//*[text()=\"" + keysToSend + "\"]"));
				clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO, customerDetails);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked tableSendKeys" + scripNumber);
				String xpath = "(//h1[text()=\"param1\"]/following::label[text()=\"param2\"]/preceding-sibling::input[not(@type=\"hidden\")])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  tableSendKeys" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if ((param1.equalsIgnoreCase("Inspection Results") && param2.equalsIgnoreCase("Characteristic")
					|| param2.equalsIgnoreCase("Result"))) {
				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1
						+ "\")]/following::label[text()=\"" + param2 + "\"]/preceding::input[1][not(@value)])[1]"));
				Thread.sleep(1000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked tableSendKeys" + scripNumber);
				String xpath = "(//*[contains(text(),\"param1\")]/following::label[text()=\"param2\"]/preceding::input[1][not(@value)])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during tableSendKeys" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if ((param1.equalsIgnoreCase("Inspection Results") && param2.equalsIgnoreCase("Expiry Date")
					|| param2.equalsIgnoreCase("Receiving Time")
					|| param2.equalsIgnoreCase("Received on Condition "))) {
				WebElement waittill = driver.findElement(By.xpath("//*[contains(text(),\"" + param1
						+ "\")]/following::*[text()=\"" + param2 + "\"]/following::input[not(@value)][2]"));
				Thread.sleep(1000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked tableSendKeys" + scripNumber);
				String xpath = "//*[contains(text(),\"param1\")]/following::*[text()=\"param2\"]/following::input[not(@value)][2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during tableSendKeys" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if ((param1.equalsIgnoreCase("Acceptable Values") && param2.equalsIgnoreCase("Acceptable Value"))) {
				WebElement waittill = driver.findElement(By.xpath("(//*[text()=\"" + param1
						+ "\"]/following::label[text()=\"" + param2 + "\"]/preceding-sibling::input[not(@title)])[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked tableSendKeys" + scripNumber);
				String xpath = "(//*[text()=\"param1\"]/following::label[text()=\"param2\"]/preceding-sibling::input[not(@title)])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during tableSendKeys" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if ((param1.equalsIgnoreCase("Manage Financial Project Plan")
					&& param2.equalsIgnoreCase("Planned Finish Date"))) {
				WebElement waittill = driver
						.findElement(By.xpath("//*[text()=\"" + param1 + "\"]/following::span[text()=\"" + param2
								+ "\"]/following::a[@title=\"Select Date\"][2]/preceding-sibling::input"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked tableSendKeys" + scripNumber);
				String xpath = "//*[text()=\"param1\"]/following::span[text()=\"param2\"]/following::a[@title=\"Select Date\"][2]/preceding-sibling::input";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during tableSendKeys" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if ((param1.equalsIgnoreCase("Manage Financial Project Plan")
					&& param2.equalsIgnoreCase("Planned Start Date"))) {
				WebElement waittill = driver
						.findElement(By.xpath("//*[text()=\"" + param1 + "\"]/following::span[text()=\"" + param2
								+ "\"]/following::a[@title=\"Select Date\"][1]/preceding-sibling::input"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked tableSendKeys" + scripNumber);
				String xpath = "//*[text()=\"param1\"]/following::span[text()=\"param2\"]/following::a[@title=\"Select Date\"][1]/preceding-sibling::input";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during tableSendKeys" + scripNumber);
			logger.error(e.getMessage());
		}
		// DH 40
		try {
			if ((param1.equalsIgnoreCase("Suppliers") && param2.equalsIgnoreCase("Supplier Contact"))
					|| param1.equalsIgnoreCase("Security")) {
				WebElement waittill = driver.findElement(By.xpath(
						"//*[text()=\"" + param1 + "\"]/following::label[text()=\"" + param2 + "\"]/preceding::input[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked tableSendKeys" + scripNumber);
				String xpath = "//*[text()=\"param1\"]/following::label[text()=\"param2\"]/preceding::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during tableSendKeys" + scripNumber);
			logger.error(e.getMessage());
		}

		// DH 34
		try {
			if (param1.equalsIgnoreCase("Budget Lines") && param2.equalsIgnoreCase("Total")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				// WebElement waittill = driver.findElement(By.xpath("(//*[text()=\"" + param1 +
				// "\"])[1]/following::label[text()=\"" + param2 + "\"]/preceding::input[1]"));

				WebElement waittill = driver.findElement(By.xpath("//table[@summary=\"" + param1
						+ "\"]//label[text()=\"Total\"]/preceding-sibling::input[contains(@id,\"tRCIN\")][1]"));

				Thread.sleep(1000);
				// wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h1[normalize-space(text())=\""+param1+"\"]/following::label[text()=\""+param2+"\"]"),
				// param2));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Provider or Receiver tableSendKeys" + scripNumber);
				String xpath = "//table[@summary=\"param1\"]//label[text()=\"Total\"]/preceding-sibling::input[contains(@id,\"tRCIN\")][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during tableSendKeys" + scripNumber);
			logger.error(e.getMessage());
		}

		try {
			if (param1.equalsIgnoreCase("Budget Lines") && param2.equalsIgnoreCase("Revenue")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				// WebElement waittill = driver.findElement(By.xpath("(//*[text()=\"" + param1 +
				// "\"])[1]/following::label[text()=\"" + param2 + "\"]/preceding::input[1]"));

				WebElement waittill = driver.findElement(By.xpath("//table[@summary=\"" + param1
						+ "\"]//label[text()=\"Total\"]/preceding-sibling::input[contains(@id,\"tRevIN\")][1]"));

				Thread.sleep(1000);
				// wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h1[normalize-space(text())=\""+param1+"\"]/following::label[text()=\""+param2+"\"]"),
				// param2));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Provider or Receiver tableSendKeys" + scripNumber);
				String xpath = "//table[@summary=\"param1\"]//label[text()=\"Total\"]/preceding-sibling::input[contains(@id,\"tRevIN\")][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during tableSendKeys" + scripNumber);
			logger.error(e.getMessage());
		}

		// DH 29
		try {
			if (param1.equalsIgnoreCase("Associated Projects")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittill = driver.findElement(By.xpath("(//*[text()=\"" + param1
						+ "\"])[1]/following::label[text()=\"" + param2 + "\"]/preceding::input[1]"));
				Thread.sleep(1000);
				// wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h1[normalize-space(text())=\""+param1+"\"]/following::label[text()=\""+param2+"\"]"),
				// param2));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Provider or Receiver tableSendKeys" + scripNumber);
				String xpath = "(//*[text()=\"param1\"])[1]/following::label[text()=\"param2\"]/preceding::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during tableSendKeys" + scripNumber);
			logger.error(e.getMessage());
		}

		// New Code for PTP.PO.511
		try {
			if (param1.equalsIgnoreCase("Suppliers") && param2.equalsIgnoreCase("Internal Responder")) {
				WebElement waittill = driver.findElement(By
						.xpath("//*[text()=\"" + param1 + "\"]/following::input[contains(@id,\"internalResponder\")][1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				// waittill.sendKeys(keysToSend);
				Thread.sleep(1000);
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked tableSendKeys" + scripNumber);
				String xpath = "//*[text()=\"param1\"]/following::input[contains(@id,\"internalResponder\")][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during tableSendKeys" + scripNumber);
			logger.error(e.getMessage());
		}

		try {
			if (param1.equalsIgnoreCase("Assigned Sets")) {
				WebElement waittill = driver
						.findElement(By.xpath("//span[text()=\"" + param2 + "\"]/following::input[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
//        waittill.sendKeys(keysToSend);
				Thread.sleep(1000);
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked tableSendKeys" + scripNumber);
				String xpath = "//span[text()=\"param2\"]/following::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  tableSendKeys" + scripNumber);
			logger.error(e.getMessage());
		}
		// Dh changes 8
		try {
			if (param1.equalsIgnoreCase("Journals") && param2.equalsIgnoreCase("journalBatch")) {
				WebElement waittill = driver.findElement(
						By.xpath("//*[text()=\"" + param1 + "\"]/following::input[contains(@id,\"" + param2 + "\")][1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
//		        waittill.sendKeys(keysToSend);
				Thread.sleep(1000);
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked tableSendKeys" + scripNumber);
				String xpath = "//*[text()=\"param1\"]/following::input[contains(@id,\"param2\")][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  tableSendKeys" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Add Project Customer")) {
				WebElement waittill = driver
						.findElement(By.xpath("//*[text()=\"" + param1 + "\"]/following::label[text()=\"" + param2
								+ "\"]/preceding-sibling::input[not(@type=\"hidden\")]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
//        waittill.sendKeys(keysToSend);
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked tableSendKeys" + scripNumber);
				String xpath = "//*[text()=\"param1\"]/following::label[text()=\"param2\"]/preceding-sibling::input[not(@type=\"hidden\")]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  tableSendKeys" + scripNumber);
			logger.error(e.getMessage());
		}
		// DH changes
		try {
			if (param1.equalsIgnoreCase("Review Distributions") && param2.equalsIgnoreCase("Account Class")) {
				Thread.sleep(4000);
				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1
						+ "\")]/following::*[text()=\"" + param2 + "\"]//preceding::input[contains(@id,\"Filter\")])[3]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				// waittill.sendKeys(keysToSend);
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked tableSendKeys" + scripNumber);
				String xpath = "(//*[contains(text(),\"param1\")]/following::*[text()=\"param2\"]//preceding::input[contains(@id,\"Filter\")])[3]";
				// service.saveXpathParams(param1, param2, scripNumber, xpath);
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during tableSendKeys" + scripNumber);
			logger.error(e.getMessage());
		}
		// DH changes
		try {

			if ((param1.equalsIgnoreCase("Receipt Details") && param2.equalsIgnoreCase("Application Reference"))) {
				WebElement waittill = driver.findElement(By.xpath("//h1[text()=\"" + param1
						+ "\"]/following::input[contains(@id,\"trxNumberList\")]/following::a[@title=\"Search\"]/preceding-sibling::input[1]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Associated Projects or Funded Amount tableSendKeys" + scripNumber);
				String xpath = "//h1[text()=\"param1\"]/following::input[contains(@id,\"trxNumberList\")]/following::a[@title=\"Search\"]/preceding-sibling::input[1]";
				// service.saveXpathParams(param1, param2, scripNumber, xpath);
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Associated Projects or Funded Amount tableSendKeys" + scripNumber);
			logger.error(e.getMessage());

		}
		try {

			if ((param1.equalsIgnoreCase("Associated Projects") && param2.equalsIgnoreCase("Funded Amount"))
					|| param2.equalsIgnoreCase("Amount")
					|| param1.equalsIgnoreCase("Review Distributions") && param2.equalsIgnoreCase("Distribution")) {
				WebElement waittill = driver.findElement(By.xpath("//*[text()=\"" + param1

						+ "\"]/following::label[text()=\"" + param2 + "\"]/preceding-sibling::input[1]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Associated Projects or Funded Amount tableSendKeys" + scripNumber);
				String xpath = "//*[text()=\"param1\"]/following::label[text()=\"param2\"]/preceding-sibling::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  Associated Projects or Funded Amount tableSendKeys" + scripNumber);
			logger.error(e.getMessage());

		}

		try {

			if (param1.equalsIgnoreCase("Associated Projects") && param2.equalsIgnoreCase("Project Number")) {

				WebElement waittill = driver.findElement(By.xpath("//*[text()=\"" + param1

						+ "\"]/following::label[text()=\"" + param2 + "\"]/preceding::span[1]/input"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Associated Projects or FProject Number tableSendKeys" + scripNumber);
				String xpath = "//*[text()=\"param1\"]/following::label[text()=\"param2\"]/preceding::span[1]/input";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Associated Projects or FProject Number tableSendKeys" + scripNumber);
			logger.error(e.getMessage());

		}

		try {

			if (param1.equalsIgnoreCase("Associated Projects") && param2.equalsIgnoreCase("Task Number")) {

				WebElement waittill = driver.findElement(By.xpath("(//*[text()=\"" + param1

						+ "\"]/following::div[text()=\"Autocompletes on TAB\"]/preceding::input[1])[4]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Associated Projects or Task Number tableSendKeys" + scripNumber);
				String xpath = "(//*[text()=\"param1\"]/following::div[text()=\"Autocompletes on TAB\"]/preceding::input[1])[4]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Associated Projects or Task Number tableSendKeys" + scripNumber);
			logger.error(e.getMessage());

		}
		try {

			if (param1.equalsIgnoreCase("Time Entry") || param2.equalsIgnoreCase("Unit Price")) {

				WebElement waittill = driver.findElement(By.xpath("(//*[text()=\"" + param1

						+ "\"]/following::label[text()=\"" + param2 + "\"]/preceding-sibling::input)[1]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Time Entry tableSendKeys" + scripNumber);
				String xpath = "(//*[text()=\"param1\"]/following::label[text()=\"param2\"]/preceding-sibling::input)[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Time Entry tableSendKeys" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Mon")) {

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()=\"" + param2 + "\"]/preceding-sibling::input[1])[1]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Mon tableSendKeys" + scripNumber);
				String xpath = "(//*[contains(text(),\"param1\")]/following::label[text()=\"param2\"]/preceding-sibling::input[1])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
			if (param1.equalsIgnoreCase("Tue")) {

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()=\"" + param2 + "\"]/preceding-sibling::input[1])[2]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Tue tableSendKeys" + scripNumber);
				String xpath = "(//*[contains(text(),\"param1\")]/following::label[text()=\"param2\"]/preceding-sibling::input[1])[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
			if (param1.equalsIgnoreCase("Wed")) {
				Thread.sleep(2000);

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()=\"" + param2 + "\"]/preceding-sibling::input[1])[3]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Wed tableSendKeys" + scripNumber);
				String xpath = "(//*[contains(text(),\"param1\")]/following::label[text()=\"param2\"]/preceding-sibling::input[1])[3]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
			if (param1.equalsIgnoreCase("Thu")) {
				Thread.sleep(2000);

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()=\"" + param2 + "\"]/preceding-sibling::input[1])[4]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Thu tableSendKeys" + scripNumber);
				String xpath = "(//*[contains(text(),\"param1\")]/following::label[text()=\"param2\"]/preceding-sibling::input[1])[4]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
			if (param1.equalsIgnoreCase("Fri")) {
				Thread.sleep(2000);

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()=\"" + param2 + "\"]/preceding-sibling::input[1])[5]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Fri tableSendKeys" + scripNumber);
				String xpath = "(//*[contains(text(),\"param1\")]/following::label[text()=\"param2\"]/preceding-sibling::input[1])[5]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
			if (param1.equalsIgnoreCase("Sat")) {

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()=\"" + param2 + "\"]/preceding-sibling::input[1])[6]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Sat tableSendKeys" + scripNumber);
				String xpath = "(//*[contains(text(),\"param1\")]/following::label[text()=\"param2\"]/preceding-sibling::input[1])[6]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
			if (param1.equalsIgnoreCase("Sunday")) {

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()=\"" + param2 + "\"]/preceding-sibling::input[1])[7]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Sunday tableSendKeys" + scripNumber);
				String xpath = "(//*[contains(text(),\"param1\")]/following::label[text()=\"param2\"]/preceding-sibling::input[1])[7]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  tableSendKeys" + scripNumber);
			logger.error(e.getMessage());

		}

		try {
			if (param1.equalsIgnoreCase("Quantity")) {
				Thread.sleep(5000);
				try {
					WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
					WebElement waittill = driver.findElement(
							By.xpath("(//text()=\"" + param1 + "\"]/preceding-sibling::input[ not (@value)])[1]"));
					Actions actions = new Actions(driver);
					actions.moveToElement(waittill).build().perform();
					typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
					screenshot(driver, fetchMetadataVO, customerDetails);
					String scripNumber = fetchMetadataVO.getScriptNumber();
					logger.info("Sucessfully Clicked Quantity tableSendKeys" + scripNumber);
					String xpath = "(//text()=\"param1\"]/preceding-sibling::input[ not (@value)])[1]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);

				} catch (Exception e) {
					WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
					WebElement waittill = driver
							.findElement(By.xpath("//label[text()=\"" + param1 + "\"]/preceding-sibling::input[1]"));
					Actions actions = new Actions(driver);
					actions.moveToElement(waittill).build().perform();
					typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
					screenshot(driver, fetchMetadataVO, customerDetails);
					String scripNumber = fetchMetadataVO.getScriptNumber();
					logger.error("Failed during Quantity tableSendKeys" + scripNumber);
					String xpath = "//label[text()=\"param1\"]/preceding-sibling::input[1]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
				}
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  tableSendKeys" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Lines") && param2.equalsIgnoreCase("Price")) {
				Thread.sleep(10000);
				WebElement waittill = driver
						.findElement(By.xpath("//*[text()=\"" + param1 + "\"]/following::label[text()=\"" + param2
								+ "\"]/preceding-sibling::input[contains(@name,\"AmountAsPrice\")]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				Thread.sleep(4000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Lines or Price tableSendKeys" + scripNumber);
				String xpath = "//*[text()=\"param1\"]/following::label[text()=\"param2\"]/preceding-sibling::input[contains(@name,\"AmountAsPrice\")]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Clicked Lines or Price  tableSendKeys" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Lines") && param2.equalsIgnoreCase("Expenditure Item Date")) {
				Thread.sleep(10000);
				WebElement waittill = driver.findElement(By.xpath("(//*[text()=\"" + param1
						+ "\"]/following::label[text()=\"" + param2 + "\"]/preceding-sibling::input)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				Thread.sleep(4000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Lines or Expenditure Item Date tableSendKeys" + scripNumber);
				String xpath = "(//*[text()=\"param1\"]/following::label[text()=\"param2\"]/preceding-sibling::input)[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Lines or Expenditure Item Date tableSendKeys" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Lines") || param2.equalsIgnoreCase("Item")) {
				Thread.sleep(10000);
				WebElement waittill = driver.findElement(By.xpath("//*[text()=\"" + param1
						+ "\"]/following::label[text()=\"" + param2 + "\"]/preceding::input[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				Thread.sleep(4000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Lines or Item tableSendKeys" + scripNumber);
				String xpath = "//*[text()=\"param1\"]/following::label[text()=\"param2\"]/preceding::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Clicked Lines or Item  tableSendKeys" + scripNumber);
			logger.error(e.getMessage());
		}
		// DH 12
		try {

			if (param1.equalsIgnoreCase("Query By Example")) {
				Thread.sleep(4000);
				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1
						+ "\")]/following::th[@_d_index=\"" + param2 + "\"][1]//input)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				Thread.sleep(4000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Lines or Item tableSendKeys" + scripNumber);
				String xpath = "//*[contains(text(),\"param1\")]/following::th[@_d_index=\"param2\"][1]//input[1]";
				// service.saveXpathParams(param1, param2, scripNumber, xpath);
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Clicked Lines or Item tableSendKeys" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param2.equalsIgnoreCase("Application Reference")) {
				Thread.sleep(4000);
				WebElement waittill = driver.findElement(By.xpath(
						"(//h1[text()=\"" + param1 + "\"]/following::*[text()=\"" + param2 + "\"]/following::input)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Application Reference tableSendKeys" + scripNumber);
				String xpath = "(//h1[text()=\"param1\"]/following::*[text()=\"param2\"]/following::input)[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Application Reference tableSendKeys" + scripNumber);
			logger.error(e.getMessage());
		}
		// Add Xpath for "Register a mass promise to pay(AR.249)

		try {

			if (param1.equalsIgnoreCase("Transaction")) {

				WebElement waittill = driver.findElement(By.xpath(

						"(//span[text()=\"" + param1 + "\"]/preceding::input[@type=\"text\"])[2]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);

				String scripNumber = fetchMetadataVO.getScriptNumber();

				logger.info("Sucessfully Clicked tableSendKeys" + scripNumber);

				String xpath = "(//span[text()=\"param1\"]/preceding::input[@type=\"text\"])[2]";

				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;

			}

		} catch (Exception e) {

			String scripNumber = fetchMetadataVO.getScriptNumber();

			logger.error("Failed during tableSendKeys" + scripNumber);

			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Provider") || param1.equalsIgnoreCase("Receiver")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittill = driver.findElement(By.xpath("(//*[text()=\"" + param1 + "\"]/following::*[text()=\""
						+ param2 + "\"]/preceding-sibling::input)[2]"));
				Thread.sleep(1000);
				// wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h1[normalize-space(text())=\""+param1+"\"]/following::label[text()=\""+param2+"\"]"),
				// param2));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Provider or Receiver tableSendKeys" + scripNumber);
				String xpath = "(//*[text()=\"param1\"]/following::*[text()=\"param2\"]/preceding-sibling::input)[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  tableSendKeys" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			Thread.sleep(6000);
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			WebElement waittill = driver.findElement(By.xpath("(//h1[text()=\"" + param1 + "\"]/following::label[text()=\""
					+ param2 + "\"]/preceding-sibling::input[not(@type=\"hidden\")])[1]"));
			Thread.sleep(1000);
			// wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h1[normalize-space(text())=\""+param1+"\"]/following::label[text()=\""+param2+"\"]"),
			// param2));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			Thread.sleep(6000);
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked tableSendKeys" + scripNumber);
			String xpath = "(//h1[text()=\"param1\"]/following::label[text()=\"param2\"]/preceding-sibling::input[not(@type=\"hidden\")])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  tableSendKeys" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebElement waittill = driver.findElement(By.xpath(
					"//h1[text()=\"" + param1 + "\"]/following::*[text()=\"" + param2 + "\"]/preceding-sibling::input[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked tableSendKeys" + scripNumber);
			String xpath = "//h1[text()=\"param1\"]/following::*[text()=\"param2\"]/preceding-sibling::input[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  tableSendKeys" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("//h1[text()=\"" + param1 + "\"]/following::span[text()=\""
					+ param2 + "\"]/preceding::input[contains(@id,\"descColumn::content\")]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked tableSendKeys" + scripNumber);
			String xpath = "//h1[text()=\"param1\"]/following::span[text()=\"param2\"]/preceding::input[contains(@id,\"descColumn::content\")]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  tableSendKeys" + scripNumber);
			logger.error(e.getMessage());
		}

		// Add Xpath for "Journal submitted but Approver on vacation(RTR.GL.115)"

		try {
			WebElement waittill = driver.findElement(By.xpath("//*[text()=\"" + param1 + "\"]/following::span[text()=\""
					+ param2 + "\"]/preceding::input[contains(@id,\"journalBatch::content\")]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked tableSendKeys" + scripNumber);
			String xpath = "//*[text()=\"param1\"]/following::span[text()=\"param2\"]/preceding::input[contains(@id,\"journalBatch::content\")]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during tableSendKeys" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebElement waittill = driver.findElement(By.xpath(
					"//h1[text()=\"" + param1 + "\"]/following::label[text()=\"" + param2 + "\"]/preceding::input[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			screenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(5000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked tableSendKeys" + scripNumber);
			String xpath = "//h1[text()=\"param1\"]/following::label[text()=\"param2\"]/preceding::input[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  tableSendKeys" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("(//*[text()=\"" + param1
					+ "\"]/following::label[text()=\"" + param2 + "\"]/preceding-sibling::input)[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked tableSendKeys" + scripNumber);
			String xpath = "(//*[text()=\"param1\"]/following::label[text()=\"param2\"]/preceding-sibling::input)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  tableSendKeys" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("(//*[text()=\"" + param1
					+ "\"]/following::label[text()=\"" + param2 + "\"]/preceding-sibling::textarea)[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked tableSendKeys" + scripNumber);
			String xpath = "(//*[text()=\"param1\"]/following::label[text()=\"param2\"]/preceding-sibling::textarea)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  tableSendKeys" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebElement waittill = driver
					.findElement(By.xpath("//*[text()=\"" + param1 + "\"]/following::table[@summary=\"" + param2
							+ "\"]//*[text()=\"" + param3 + "\"]/following::input[contains(@id,\"NewBdgtPctLst\")][1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked tableSendKeys" + scripNumber);
			String xpath = "//*[text()=\"param1\"]/following::table[@summary=\"param2\"]//*[text()=\"param3\"]/following::input[contains(@id,\"NewBdgtPctLst\")][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  tableSendKeys" + scripNumber);
			logger.error(e.getMessage());
		}
		// DH
		try {
			Thread.sleep(6000);
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			WebElement waittill = driver.findElement(By.xpath("//*[contains(text(),\"" + param1
					+ "\")]/following::label[text()=\"" + param2 + "\"]/preceding::input[1]"));
			Thread.sleep(1000);
			// wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h1[normalize-space(text())=\""+param1+"\"]/following::label[text()=\""+param2+"\"]"),
			// param2));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			Thread.sleep(6000);
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked tableSendKeys" + scripNumber);
			String xpath = "//*[contains(text(),\"param1\")]/following::label[text()=\"param2\"]/preceding::input[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  tableSendKeys" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			Thread.sleep(6000);
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			WebElement waittill = driver.findElement(By.xpath("//*[text()=\"" + param1
					+ "\"]/following::label[contains(text(),\"" + param2 + "\")]/preceding::input[1]"));
			Thread.sleep(1000);
			// wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h1[normalize-space(text())=\""+param1+"\"]/following::label[text()=\""+param2+"\"]"),
			// param2));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			Thread.sleep(6000);
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked tableSendKeys" + scripNumber);
			String xpath = "//*[contains(text(),\"param1\")]/following::label[text()=\"param2\"]/preceding::input[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  tableSendKeys" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			// tab(driver, fetchMetadataVO, fetchConfigVO);
			// Thread.sleep(1000);
			// enter(driver, fetchMetadataVO, fetchConfigVO);
			WebElement waittill = driver.findElement(By.xpath("(//table[@summary=\"" + param1 + "\"]//label[text()=\""
					+ param2 + "\"]/preceding-sibling::input)[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked tableSendKeys" + scripNumber);
			String xpath = "(//table[@summary=\"param1\"]//label[text()=\"param2\"]/preceding-sibling::input)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  tableSendKeys" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void tableDropdownTexts(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath("//div[@class=\"AFDetectExpansion\"]/following::*[text()=\"" + keysToSend + "\"]"),
					keysToSend));
			WebElement waittext = driver.findElement(
					By.xpath("//div[@class=\"AFDetectExpansion\"]/following::*[text()=\"" + keysToSend + "\"]"));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked tableDropdownTexts" + scripNumber);
			String xpath = "//div[@class=\"AFDetectExpansion\"]/following::*[text()=\"keysToSend\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  tableDropdownTexts" + scripNumber);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//table[@summary=\"" + param1 + "\"]/following::li[text()=\"" + keysToSend + "\"]")));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath("//table[@summary=\"" + param1 + "\"]/following::li[text()=\"" + keysToSend + "\"]"),
					keysToSend));
			WebElement waittext = driver.findElement(
					By.xpath("//table[@summary=\"" + param1 + "\"]/following::li[text()=\"" + keysToSend + "\"]"));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked tableDropdownTexts" + scripNumber);
			String xpath = "//table[@summary=\"param1\"]/following::li[text()=\"keysToSend\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  tableDropdownTexts" + scripNumber);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//*[text()=\"" + param1 + "\"]/following::li[text()=\"" + keysToSend + "\"]")));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath("//*[text()=\"" + param1 + "\"]/following::li[text()=\"" + keysToSend + "\"]"), keysToSend));
			WebElement waittext = driver
					.findElement(By.xpath("//*[text()=\"" + param1 + "\"]/following::li[text()=\"" + keysToSend + "\"]"));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked tableDropdownTexts" + scripNumber);
			String xpath = "//*[text()=\"param1\"]/following::li[text()=\"keysToSend\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  tableDropdownTexts" + scripNumber);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//*[text()=\"" + param1 + "\"]/following::td[text()=\"" + keysToSend + "\"]")));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath("//*[text()=\"" + param1 + "\"]/following::td[text()=\"" + keysToSend + "\"]"), keysToSend));
			WebElement waittext = driver
					.findElement(By.xpath("//*[text()=\"" + param1 + "\"]/following::td[text()=\"" + keysToSend + "\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked tableDropdownTexts" + scripNumber);
			String xpath = "//*[text()=\"param1\"]/following::td[text()=\"keysToSend\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  tableDropdownTexts" + scripNumber);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"//div[contains(@id,\"dropdownPopup::content\")]/following::a[contains(text(),\"Search\")][1]")));
//		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//a[contains(text(),\"Search\")]"), "Search"));
			WebElement search = driver.findElement(By
					.xpath("//div[contains(@id,\"dropdownPopup::content\")]/following::a[contains(text(),\"Search\")][1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(search).build().perform();
			search.click();
			Thread.sleep(10000);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"//div[contains(@id,\"PopupId::content\")]//*[text()=\"Search\"]/following::*[normalize-space(text())=\""
							+ param2 + "\"]/following::input[1]")));
			WebElement searchResult = driver.findElement(By.xpath(
					"//div[contains(@id,\"PopupId::content\")]//*[text()=\"Search\"]/following::*[normalize-space(text())=\""
							+ param2 + "\"]/following::input[1]"));
			String xpath1 = "//div[contains(@id,\"dropdownPopup::content\")]/following::a[contains(text(),\"Search\")][1]";
			String xpath2 = "//div[contains(@id,\"PopupId::content\")]//*[text()=\"Search\"]/following::*[normalize-space(text())=\"param2\"]/following::input[1]";
			String xpath = xpath1 + ";" + xpath2;
			typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
			if (keysToSend != null) {
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
				WebElement text = driver.findElement(By.xpath("(//span[contains(text(),\"" + keysToSend + "\")])[1]"));
				text.click();
				xpath = xpath + ";" + "(//span[contains(text(),\"keysToSend\")])[1]";
			}
			try {
				WebElement button = driver.findElement(By.xpath(
						"//*[text()=\"Search\"]/following::*[text()=\"" + param2 + "\"]/following::*[text()=\"K\"][1]"));
				button.click();
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked tableDropdownTexts" + scripNumber);
				xpath = xpath + ";" + "//*[text()=\"Search\"]/following::*[text()=\"param2\"]/following::*[text()=\"K\"][1]";
			} catch (Exception e) {
				WebElement button = driver.findElement(By.xpath(
						"//*[text()=\"Search\"]/following::*[text()=\"" + param2 + "\"]/following::*[text()=\"OK\"][1]"));
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.error("Failed during  tableDropdownTexts" + scripNumber);
				button.click();
				xpath = xpath + ";" + "//*[text()=\"Search\"]/following::*[text()=\"param2\"]/following::*[text()=\"OK\"][1]";
			}
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  tableDropdownTexts" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			String xpath;
			try {
				WebElement searchResult = driver
						.findElement(By.xpath("//*[text()=\"Search\"]/following::*[text()=\"Name\"]/following::input[1]"));
				typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked tableDropdownTexts" + scripNumber);
				xpath = "//*[text()=\"Search\"]/following::*[text()=\"Name\"]/following::input[1]";
			} catch (Exception e) {
				WebElement searchResult = driver
						.findElement(By.xpath("//*[text()=\"Search\"]/following::*[text()=\"Value\"]/following::input[1]"));
				typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.error("Failed during  tableDropdownTexts" + scripNumber);
				xpath = "//*[text()=\"Search\"]/following::*[text()=\"Value\"]/following::input[1]";
			}

			WebElement text = driver.findElement(By.xpath("(//span[contains(text(),\"" + keysToSend + "\")])[1]"));
			text.click();
			xpath = xpath + ";" + "(//span[contains(text(),\"keysToSend\")])[1]";
			Thread.sleep(1000);
			try {
				WebElement button = driver.findElement(
						By.xpath("//*[text()=\"Search\"]/following::*[text()=\"Name\"]/following::*[text()=\"OK\"][1]"));
				button.click();
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked tableDropdownTexts" + scripNumber);
				xpath = xpath + ";" + "//*[text()=\"Search\"]/following::*[text()=\"Name\"]/following::*[text()=\"OK\"][1]";
			} catch (Exception e) {
				WebElement button = driver.findElement(
						By.xpath("//*[text()=\"Search\"]/following::*[text()=\"Value\"]/following::*[text()=\"OK\"][1]"));
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.error("Failed during  tableDropdownTexts" + scripNumber);
				button.click();
				xpath = xpath + ";" + "//*[text()=\"Search\"]/following::*[text()=\"Value\"]/following::*[text()=\"OK\"][1]";
			}
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  tableDropdownTexts" + scripNumber);
		}
		try {
			WebElement button = driver
					.findElement(By.xpath("//*[text()=\"Search\"]/following::*[normalize-space(text())=\"" + param2
							+ "\"]/following::*[text()=\"OK\"][1]"));
			button.click();
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked tableDropdownTexts" + scripNumber);
			String xpath = "//*[text()=\"Search\"]/following::*[normalize-space(text())=\"param2\"]/following::*[text()=\"OK\"][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error(e.getMessage());
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  tableDropdownTexts" + scripNumber);
			throw e;
		}
	}

	public void tableDropdownValues(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {
		// HS2
		try {

			if (param1.equalsIgnoreCase("Edit Citizenships")
					&& ((param2.equalsIgnoreCase("Citizenship Status") || (param2.equalsIgnoreCase("Citizenship"))))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//div[text()=\"" + param1
						+ "\"]/following::label[text()=\"" + param2 + "\"]/parent::span//a)[1]")));
				WebElement waittext = driver.findElement(By.xpath("(//div[text()=\"" + param1
						+ "\"]/following::label[text()=\"" + param2 + "\"]/parent::span//a)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				Thread.sleep(3000);

				WebElement select = driver.findElement(By.xpath("//*[text()=\"" + keysToSend + "\"]"));
				clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//div[text()=\"param1\"]/following::*[text()=\"param2\"]/following::a[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked Schedule New Process or Name dropdownValues" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Schedule New Process or Name dropdownValues" + scripNumber);
			logger.error(e.getMessage());
		}
		// OB.9 DH
		try {

			if (param1.equalsIgnoreCase("Schedules") && (param2.equalsIgnoreCase("Primary"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[text()=\"" + param1
						+ "\"]/following::label[text()=\"" + param2 + "\"]/ancestor::span//input)[1]")));
				WebElement waittext = driver.findElement(By.xpath("(//h1[text()=\"" + param1
						+ "\"]/following::label[text()=\"" + param2 + "\"]/ancestor::span//input)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				Thread.sleep(3000);

				WebElement select = driver.findElement(By.xpath("//*[text()=\"" + keysToSend + "\"]"));
				clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath1 = "(//h1[text()=\"param1\"]/following::label[text()=\"param2\"]/ancestor::span//input)[1]";
				String xpath2 = "//*[text()=\"keysToSend\"]";
				String xpath = xpath1 + ";" + xpath2;
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked Schedule New Process or Name dropdownValues" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Schedule New Process or Name dropdownValues" + scripNumber);
			logger.error(e.getMessage());
		}
		// dh 8
		try {
			if (param2.equalsIgnoreCase("Type")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]/preceding::input[2]")));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]/preceding::input[2]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// clickTableDropdown(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				tableDropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(3000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::label[normalize-space(text())=\"param2\"]/preceding::input[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		try {
			if (param1.equalsIgnoreCase("Add Project Customer")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(@id,\"projectRole\")]")));
				WebElement waittext = driver.findElement(By.xpath("//a[contains(@id,\"projectRole\")]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				tableDropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "(//a[contains(@id,\"projectRole\")]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked tableDropdownValues" + scripNumber);
				return;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  tableDropdownValues" + scripNumber);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("(//*[text()=\"" + param1 + "\"]/following::a[contains(@id,\"" + param2 + "\")])[1]")));
			WebElement waittext = driver.findElement(
					By.xpath("(//*[text()=\"" + param1 + "\"]/following::a[contains(@id,\"" + param2 + "\")])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			tableDropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();

			String xpath = "(//*[text()=\"param1\"]/following::a[contains(@id,\"param2\")])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked tableDropdownValues" + scripNumber);
			return;
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  tableDropdownValues" + scripNumber);
		}
		try {
			if (param1.equalsIgnoreCase("Billing")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"//*[text()=\"" + param1 + "\"]/following::label[text()=\"" + param2 + "\"]/following::a[1]")));
				WebElement waittext = driver.findElement(By.xpath(
						"//*[text()=\"" + param1 + "\"]/following::label[text()=\"" + param2 + "\"]/following::a[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				Thread.sleep(2000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				tableDropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(3000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "//*[text()=\"param1\"]/following::label[text()=\"param2\"]/following::a[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked Billing tableDropdownValues" + scripNumber);
				return;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Billing tableDropdownValues" + scripNumber);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By
					.xpath("//*[text()=\"" + param1 + "\"]/following::label[text()=\"" + param2 + "\"]/preceding::a[1]")));
			WebElement waittext = driver.findElement(
					By.xpath("//*[text()=\"" + param1 + "\"]/following::label[text()=\"" + param2 + "\"]/preceding::a[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			tableDropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(3000);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();

			String xpath = "//*[text()=\"param1\"]/following::label[text()=\"param2\"]/preceding::a[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked tableDropdownValues" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during tableDropdownValues" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//table[@summary=\"" + param1 + "\"]//input/following-sibling::a[1]")));
			WebElement waittext = driver
					.findElement(By.xpath("//table[@summary=\"" + param1 + "\"]//input/following-sibling::a[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			tableDropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();

			String xpath = "//table[@summary=\"param1\"]//input/following-sibling::a[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked tableDropdownValues" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  tableDropdownValues" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()=\"" + param1
					+ "\"]/following::input[contains(@id,\"" + param2 + "\")][1]/following::a[1]")));
			WebElement waittext = driver.findElement(By.xpath("//*[text()=\"" + param1
					+ "\"]/following::input[contains(@id,\"" + param2 + "\")][1]/following::a[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			tableDropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();

			String xpath = "//*[text()=\"param1\"]/following::input[contains(@id,\"param2\")][1]/following::a[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked tableDropdownValues" + scripNumber);
			return;
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  tableDropdownValues" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void dropdownValues(WebDriver driver, String param1, String param2, String param3, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {
		try {
            if ((param1.equalsIgnoreCase("Offer Team")
                    && param2.equalsIgnoreCase("Recruiter"))) {
            Thread.sleep(6000);
            WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
            WebElement waittill = driver.findElement(By.xpath("(//h1[text()=\"" + param1 + "\"]/following::label[text()=\""
                    + param2 + "\"]/preceding-sibling::input[not(@type=\"hidden\")])[1]"));
            Thread.sleep(1000);
            Actions actions = new Actions(driver);
            actions.moveToElement(waittill).build().perform();
            Thread.sleep(6000);
            //values.sendKeys(keysToSend);
        //    typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
            waittill.sendKeys(keysToSend);
            Thread.sleep(1000);
            WebElement select = driver.findElement(
                    By.xpath("//*[text()=\""+keysToSend+"\"]"));
            clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO, customerDetails);
            screenshot(driver, fetchMetadataVO, customerDetails);
            String scripNumber = fetchMetadataVO.getScriptNumber();
            logger.info("Sucessfully Clicked tableSendKeys" + scripNumber);
            String xpath = "(//h1[text()=\"param1\"]/following::label[text()=\"param2\"]/preceding-sibling::input[not(@type=\"hidden\")])[1]";
            String scriptID = fetchMetadataVO.getScriptId();
            String lineNumber = fetchMetadataVO.getLineNumber();
            service.saveXpathParams(scriptID, lineNumber, xpath);

            return;
        } }catch (Exception e) {
            String scripNumber = fetchMetadataVO.getScriptNumber();
            logger.error("Failed during  tableSendKeys" + scripNumber);
            logger.error(e.getMessage());
        }
		try {
			if (param1.equalsIgnoreCase("Interviewer Responses")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"(//*[text()=\"" + param1 + "\"]/following::*[text()=\"" + param2 + "\"]/following::a)[1]")));
				WebElement waittext = driver.findElement(By
						.xpath("(//*[text()=\"" + param1 + "\"]/following::*[text()=\"" + param2 + "\"]/following::a)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				Thread.sleep(3000);

				WebElement select = driver.findElement(By.xpath(
						"//*[@class=\"AFDetectExpansion\"]/following::li[contains(text(), \"" + keysToSend + "\")]"));
				clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//div[text()=\"param1\"]/following::*[text()=\"param2\"]/following::a[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked Schedule New Process or Name dropdownValues" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Schedule New Process or Name dropdownValues" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Add Enrollment") && (param2.equalsIgnoreCase("Select Plan"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"(//*[text()=\"" + param1 + "\"]/following::*[text()=\"" + param2 + "\"]/following::a)[1]")));
				WebElement waittext = driver.findElement(By
						.xpath("(//*[text()=\"" + param1 + "\"]/following::*[text()=\"" + param2 + "\"]/following::a)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).perform();

				WebElement select = driver
						.findElement(By.xpath("(//div[contains(@id,\"popup-container\")]//*[normalize-space(text())=\""
								+ keysToSend + "\"])[1]"));
				// select.click();
				clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//*[text()=\"param1\"]/following::*[text()=\"param2\"]/following::a)[1]" + ";"
						+ "(//div[contains(@id,\"popup-container\")]//*[normalize-space(text())=\"keysToSend\"])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked Schedule New Process or Name dropdownValues" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Schedule New Process or Name dropdownValues" + scripNumber);
			logger.error(e.getMessage());
		}
		// DH
		try {
			if (param1.equalsIgnoreCase("Create Contract") && param2.equalsIgnoreCase("Primary Party")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[text()=\"" + param1
						+ "\"]/following::label[text()=\"" + param2 + "\"][1]/following::a[@title=\"Name\"]")));

				WebElement waittext = driver.findElement(By.xpath("//div[text()=\"" + param1
						+ "\"]/following::label[text()=\"" + param2 + "\"][1]/following::a[@title=\"Name\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(2000);

				WebElement search = driver
						.findElement(By.xpath("//table[contains(@id,\"dropdownPopup\")]//*[text()=\"Search...\"]"));
				clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO, customerDetails);
				Thread.sleep(5000);

				WebElement values = driver
						.findElement(By.xpath("//div[contains(text(),\"" + param2 + "\")]/following::input[1]"));
				typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);

				WebElement select = driver
						.findElement(By.xpath("//div[@class=\"AFDetectExpansion\"]/following::span[starts-with(text(),\""
								+ keysToSend + "\")][1]"));
				clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO, customerDetails);

				WebElement searchok = driver
						.findElement(By.xpath("//div[@class=\"AFDetectExpansion\"]/following::span[contains(text(),\""
								+ keysToSend + "\")][1]/following::button[text()=\"OK\"][1]"));
				clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO, customerDetails);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//div[text()=\"param1\"]/following::label[text()=\"param2\"][1]/following::a[@title=\"Name\"]"
						+ ";" + "//table[contains(@id,\"dropdownPopup\")]//*[text()=\"Search...\"]" + ";"
						+ "//div[contains(text(),\"param2\")]/following::input[1]" + ";"
						+ "//div[@class=\"AFDetectExpansion\"]/following::span[starts-with(text(),\"keysToSend\")][1]" + ";"
						+ "//div[@class=\"AFDetectExpansion\"]/following::span[contains(text(),\"keysToSend\")][1]/following::button[text()=\"OK\"][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked Schedule New Process or Name dropdownValues" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Schedule New Process or Name dropdownValues" + scripNumber);
			logger.error(e.getMessage());
		}
		// HS2
		try {
			if (param1.equalsIgnoreCase("job Details") && (param2.equalsIgnoreCase("Start Time"))
					|| (param1.equalsIgnoreCase("job Details") && (param2.equalsIgnoreCase("End Time")))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"(//h2[text()=\"" + param1 + "\"]/following::label[text()=\"" + param2 + "\"]/following::a)[1]")));
				WebElement waittext = driver.findElement(By.xpath(
						"(//h2[text()=\"" + param1 + "\"]/following::label[text()=\"" + param2 + "\"]/following::a)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).perform();

				WebElement select = driver
						.findElement(By.xpath("(//div[contains(@id,\"popup-container\")]//*[normalize-space(text())=\""
								+ keysToSend + "\"])[1]"));
				// select.click();
				clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//h2[text()=\"param1\"]/following::label[text()=\"param2\"]/following::a)[1]" + ";"
						+ "(//div[contains(@id,\"popup-container\")]//*[normalize-space(text())=\"keysToSend\"])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked Schedule New Process or Name dropdownValues" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Schedule New Process or Name dropdownValues" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Create Element Entry") && param2.equalsIgnoreCase("Element Name")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::label[text()=\"" + param2 + "\"]/following::a[@title=\"" + param2 + "\"]")));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::label[text()=\"" + param2 + "\"]/following::a[@title=\"" + param2 + "\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				actions.click(waittext).build().perform();
				try {
					Thread.sleep(1000);
					WebElement search = driver.findElement(By.xpath("//a[contains(text(),\"Search\")][1]"));
					search.click();
					Thread.sleep(3000);
					wait.until(ExpectedConditions.presenceOfElementLocated(
							By.xpath("(//*[contains(text(),\"Search and Select\")]/following::*[normalize-space(text())=\""
									+ param2 + "\"]/following::input)[1]")));
					WebElement searchResult = driver.findElement(
							By.xpath("(//*[contains(text(),\"Search and Select\")]/following::*[normalize-space(text())=\""
									+ param2 + "\"]/following::input[1])"));
					typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
					Thread.sleep(5000);
					WebElement text = driver
							.findElement(By.xpath("(//span[normalize-space(text())=\"" + keysToSend + "\"])[2]"));
					text.click();
					Thread.sleep(1000);
					WebElement button = driver.findElement(
							By.xpath("//*[contains(text(),\"Search and Select\")]/following::*[text()=\"OK\"][1]"));
					button.click();

				} catch (Exception e) {
					logger.error("Failed during dropdownValues" + e.getMessage());
				}
				return;
			}
		} catch (Exception ex) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during dropdownValues" + scripNumber);
			logger.error(ex.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Relationship Information") && (param2.equalsIgnoreCase("Contact Type"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[normalize-space(text())=\""
						+ param1 + "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::a[1]")));
				WebElement waittext = driver.findElement(By.xpath("//h1[normalize-space(text())=\"" + param1
						+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::a[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).perform();

				WebElement select = driver
						.findElement(By.xpath("(//div[contains(@id,\"popup-container\")]//*[normalize-space(text())=\""
								+ keysToSend + "\"])[1]"));
				// select.click();
				clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[text()=\"param1\"]//following::label[text()=\"param2\"]//following::a[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked Schedule New Process or Name dropdownValues" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Schedule New Process or Name dropdownValues" + scripNumber);
			logger.error(e.getMessage());
		}
		// OTL.004 DH
		try {

			if (param1.equalsIgnoreCase("Create Shift") && (param2.equalsIgnoreCase("Resource"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"(//*[text()=\"" + param1 + "\"]/following::*[text()=\"" + param2 + "\"]/following::a)[1]")));
				WebElement waittext = driver.findElement(By
						.xpath("(//*[text()=\"" + param1 + "\"]/following::*[text()=\"" + param2 + "\"]/following::a)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				Thread.sleep(3000);

				WebElement select = driver.findElement(By.xpath("//li[contains(text(), \"" + keysToSend + "\")]"));
				clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath1 = "(//*[text()=\"param1\"]/following::*[text()=\"param2\"]/following::a)[1]";
				String xpath2 = "//li[contains(text(), \"keysToSend\")]";
				String xpath = xpath1 + ";" + xpath2;
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked Schedule New Process or Name dropdownValues" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Schedule New Process or Name dropdownValues" + scripNumber);
			logger.error(e.getMessage());
		}

		// DH 32
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("//*[contains(@id,\"popup-container\")]//*[text()=\"" + param1
							+ "\"]/following::*[text()=\"" + param2 + "\"]/following::input[not (@type=\"hidden\")][1]")));
			WebElement waittext = driver.findElement(By.xpath("//*[contains(@id,\"popup-container\")]//*[text()=\""
					+ param1 + "\"]/following::*[text()=\"" + param2 + "\"]/following::input[not (@type=\"hidden\")][1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			waittext.click();
			Thread.sleep(4000);
			WebElement selectvalue = driver.findElement(By.xpath("//*[text()=\"" + keysToSend + "\"][1]"));
			actions.moveToElement(selectvalue).build().perform();
			selectvalue.click();
			Thread.sleep(500);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[contains(@id,\"popup-container\")]//*[text()=\"param1\"]/following::*[text()=\"param2\"]/following::input[not (@type=\"hidden\")][1]"
					+ ";" + "//*[text()=\"keysToSend\"][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked Schedule New Process or Name dropdownValues" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Event Type dropdownValues" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Emirate") && param2.equalsIgnoreCase("Emirate")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[text()=\"" + param1 + "\"]/following::a[@title=\"" + param2 + "\"]")));

				WebElement waittext = driver
						.findElement(By.xpath("//*[text()=\"" + param1 + "\"]/following::a[@title=\"" + param2 + "\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(2000);

				WebElement search = driver
						.findElement(By.xpath("//table[contains(@id,\"dropdownPopup\")]//*[text()=\"Search...\"]"));
				clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO, customerDetails);
				Thread.sleep(5000);

				WebElement values = driver.findElement(By.xpath(
						"//div[@class=\"AFDetectExpansion\"]/following::*[text()=\"Search\"]/following::*[normalize-space(text())=\""
								+ param2 + "\"]/following::input[1]"));
				typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);

				WebElement select = driver
						.findElement(By.xpath("//div[@class=\"AFDetectExpansion\"]/following::span[starts-with(text(),\""
								+ keysToSend + "\")][1]"));
				clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO, customerDetails);

				WebElement searchok = driver
						.findElement(By.xpath("//div[@class=\"AFDetectExpansion\"]/following::span[contains(text(),\""
								+ keysToSend + "\")][1]/following::button[text()=\"OK\"][1]"));
				clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO, customerDetails);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[text()=\"param1\"]/following::a[@title=\" param2\"]" + ";"
						+ "//table[contains(@id,\"dropdownPopup\")]//*[text()=\"Search...\"]" + ";"
						+ "//div[@class=\"AFDetectExpansion\"]/following::*[text()=\"Search\"]/following::*[normalize-space(text())=\""
						+ ";" + "//div[@class=\"AFDetectExpansion\"]/following::span[starts-with(text(),\"keysToSend\")][1]"
						+ ";"
						+ "//div[@class=\"AFDetectExpansion\"]/following::span[contains(text(),\"keysToSend\")][1]/following::button[text()=\"OK\"][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked Schedule New Process or Name dropdownValues" + scripNumber);
				return;

			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Schedule New Process or Name dropdownValues" + scripNumber);
			logger.error(e.getMessage());
		}

		// DH 55
		try {
			if (param1.equalsIgnoreCase("DH Account Analysis Report") && param2.equalsIgnoreCase("Ledger Name")) {
				Thread.sleep(5000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver.findElement(By.xpath("//*[text()=\"" + param2 + "\"]//following::a[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				Thread.sleep(3000);
				WebElement selectvalue = driver
						.findElement(By.xpath("//div[@class=\"listbox\"]//div[text()=\"" + keysToSend + "\"]"));
				// clickValidateXpath(driver, fetchMetadataVO, selectvalue, fetchConfigVO);
				selectvalue.click();
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath1 = "//*[text()=\"param2\"]//following::a[1]";
				String xpath2 = "//div[@class=\"listbox\"]//div[text()=\"keysToSend\"]";
				String xpath = xpath1 + ";" + xpath2;
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked Schedule New Process or Name dropdownValues" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Schedule New Process or Name dropdownValues" + scripNumber);
			logger.error(e.getMessage());
		}

		try {
			if (param1.equalsIgnoreCase("DH Account Analysis Report")
					&& param2.equalsIgnoreCase("From Accounting Period")) {
				Thread.sleep(5000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver.findElement(By.xpath("//*[text()=\"" + param2 + "\"]//following::a[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				Thread.sleep(3000);
				WebElement selectvalue = driver.findElement(
						By.xpath("//div[contains(@id,\"FROM_PERIOD_DT\")]//div[text()=\"" + keysToSend + "\"]"));
				// clickValidateXpath(driver, fetchMetadataVO, selectvalue, fetchConfigVO);
				selectvalue.click();
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath1 = "//*[text()=\"param2\"]//following::a[1]";
				String xpath2 = "//div[contains(@id,\"FROM_PERIOD_DT\")]//div[text()=\"keysToSend\"]";
				String xpath = xpath1 + ";" + xpath2;
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked Schedule New Process or Name dropdownValues" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Schedule New Process or Name dropdownValues" + scripNumber);
			logger.error(e.getMessage());
		}

		try {
			if (param1.equalsIgnoreCase("DH Account Analysis Report")
					&& param2.equalsIgnoreCase("To Accounting Period")) {
				Thread.sleep(5000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver.findElement(By.xpath("//*[text()=\"" + param2 + "\"]//following::a[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				Thread.sleep(3000);
				WebElement selectvalue = driver
						.findElement(By.xpath("//div[contains(@id,\"TO_PERIOD_DT\")]//div[text()=\"" + keysToSend + "\"]"));
				// clickValidateXpath(driver, fetchMetadataVO, selectvalue, fetchConfigVO);
				selectvalue.click();
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath1 = "//*[text()=\"param2\"]//following::a[1]";
				String xpath2 = "//div[contains(@id,\"TO_PERIOD_DT\")]//div[text()=\"keysToSend\"]";
				String xpath = xpath1 + ";" + xpath2;
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked Schedule New Process or Name dropdownValues" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Schedule New Process or Name dropdownValues" + scripNumber);
			logger.error(e.getMessage());
		}

		try {
			if (param1.equalsIgnoreCase("DH Account Analysis Report") && param2.equalsIgnoreCase("Account")) {
				Thread.sleep(5000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver.findElement(By.xpath("//*[text()=\"" + param2 + "\"]//following::a[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				Thread.sleep(3000);
				WebElement selectvalue = driver
						.findElement(By.xpath("//div[contains(@id,\"P_ACCOUNTS\")]//div[text()=\"" + keysToSend + "\"]"));
				// clickValidateXpath(driver, fetchMetadataVO, selectvalue, fetchConfigVO);
				selectvalue.click();
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath1 = "//*[text()=\"param2\"]//following::a[1]";
				String xpath2 = "//div[contains(@id,\"P_ACCOUNTS\")]//div[text()=\"keysToSend\"]";
				String xpath = xpath1 + ";" + xpath2;
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked Schedule New Process or Name dropdownValues" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Schedule New Process or Name dropdownValues" + scripNumber);
			logger.error(e.getMessage());
		}

		try {
			if (param1.equalsIgnoreCase("DH Account Analysis Report")
					&& param2.equalsIgnoreCase("Sub Ledger Application")) {
				Thread.sleep(5000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver.findElement(By.xpath("//*[text()=\"" + param2 + "\"]//following::a[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				Thread.sleep(3000);
				WebElement selectvalue = driver.findElement(
						By.xpath("//div[contains(@id,\"SUB_LEDGER_APPL\")]//div[text()=\"" + keysToSend + "\"]"));
				// clickValidateXpath(driver, fetchMetadataVO, selectvalue, fetchConfigVO);
				selectvalue.click();
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath1 = "//*[text()=\"" + param2 + "\"]//following::a[1]";
				String xpath2 = "//div[contains(@id,\"SUB_LEDGER_APPL\")]//div[text()=\"keysToSend\"]";
				String xpath = xpath1 + ";" + xpath2;
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked Schedule New Process or Name dropdownValues" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Schedule New Process or Name dropdownValues" + scripNumber);
			logger.error(e.getMessage());
		}

		// DH 48
		try {
			if ((param1.equalsIgnoreCase("Adjust Balance") && param2.equalsIgnoreCase("Reason"))
					|| (param1.equalsIgnoreCase("Update Employment") && param2.equalsIgnoreCase("Action"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"//div[text()=\"" + param1 + "\"]/following::*[text()=\"" + param2 + "\"]/following::a[1]")));

				WebElement waittext = driver.findElement(By
						.xpath("//div[text()=\"" + param1 + "\"]/following::*[text()=\"" + param2 + "\"]/following::a[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				Thread.sleep(3000);

				WebElement selectvalue = driver.findElement(By.xpath("//li[text()=\"" + keysToSend + "\"]"));
				// clickValidateXpath(driver, fetchMetadataVO, selectvalue, fetchConfigVO);
				selectvalue.click();

				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath1 = "//div[text()=\"param1\"]/following::*[text()=\"param2\"]/following::a[1]";
				String xpath2 = "//li[text()=\"keysToSend\"]";
				String xpath = xpath1 + ";" + xpath2;
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked Schedule New Process or Name dropdownValues" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Schedule New Process or Name dropdownValues" + scripNumber);
			logger.error(e.getMessage());
		}

		// DH 32
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("//*[contains(@id,\"popup-container\")]//*[text()=\"" + param1
							+ "\"]/following::*[text()=\"" + param2 + "\"]/following::input[not (@type=\"hidden\")][1]")));
			WebElement waittext = driver.findElement(By.xpath("//*[contains(@id,\"popup-container\")]//*[text()=\""
					+ param1 + "\"]/following::*[text()=\"" + param2 + "\"]/following::input[not (@type=\"hidden\")][1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			waittext.click();
			Thread.sleep(4000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[contains(@id,\"popup-container\")]//*[text()=\"param1\"]/following::*[text()=\"param2\"]/following::input[not (@type=\"hidden\")][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked Schedule New Process or Name dropdownValues" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Event Type dropdownValues" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Emirate") && param2.equalsIgnoreCase("Emirate")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[text()=\"" + param1 + "\"]/following::a[@title=\"" + param2 + "\"]")));

				WebElement waittext = driver
						.findElement(By.xpath("//*[text()=\"" + param1 + "\"]/following::a[@title=\"" + param2 + "\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(2000);

				WebElement search = driver
						.findElement(By.xpath("//table[contains(@id,\"dropdownPopup\")]//*[text()=\"Search...\"]"));
				clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO, customerDetails);
				Thread.sleep(5000);

				WebElement values = driver.findElement(By.xpath(
						"//div[@class=\"AFDetectExpansion\"]/following::*[text()=\"Search\"]/following::*[normalize-space(text())=\""
								+ param2 + "\"]/following::input[1]"));
				typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);

				WebElement select = driver
						.findElement(By.xpath("//div[@class=\"AFDetectExpansion\"]/following::span[starts-with(text(),\""
								+ keysToSend + "\")][1]"));
				clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO, customerDetails);

				WebElement searchok = driver
						.findElement(By.xpath("//div[@class=\"AFDetectExpansion\"]/following::span[contains(text(),\""
								+ keysToSend + "\")][1]/following::button[text()=\"OK\"][1]"));

				clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO, customerDetails);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[text()=\"param1\"]/following::*[@title=\"param2\"]" + ";"
						+ "//table[contains(@id,\"dropdownPopup\")]//*[text()=\"Search...\"]" + ";"
						+ "//div[@class=\"AFDetectExpansion\"]/following::*[text()=\"Search\"]/following::*[normalize-space(text())=\"param2\"]/following::input[1]"
						+ ";" + "//div[@class=\"AFDetectExpansion\"]/following::span[starts-with(text(),\"keysToSend\")][1]"
						+ ";"
						+ "//div[@class=\"AFDetectExpansion\"]/following::span[contains(text(),\"keysToSend\")][1]/following::button[text()=\"OK\"][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked Schedule New Process or Name dropdownValues" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Schedule New Process or Name dropdownValues" + scripNumber);
			logger.error(e.getMessage());
		}

		// DH 29
		try {
			if (param1.equalsIgnoreCase("Create Event") && (param2.equalsIgnoreCase("Event Type"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//h1[normalize-space(text())=\"" + param1 + "\"]/following::label[text()=\"" + param2
								+ "\"]/following::a[contains(@id,\"eventType\")]")));
				WebElement waittext = driver.findElement(By.xpath("//h1[normalize-space(text())=\"" + param1
						+ "\"]/following::label[text()=\"" + param2 + "\"]/following::a[contains(@id,\"eventType\")]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				WebElement text = driver.findElement(
						By.xpath("//div[contains(@id,\"popup-container\")]//td[text()=\"" + keysToSend + "\"][1]"));
				text.click();
				Thread.sleep(4000);

				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//h1[normalize-space(text())=\"param1\"]/following::label[text()=\"param2\"]/following::a[contains(@id,\"eventType\")]"
						+ ";" + "//div[contains(@id,\"popup-container\")]//td[text()=\"keysToSend\"][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked Schedule New Process or Name dropdownValues" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Event Type dropdownValues" + scripNumber);
			logger.error(e.getMessage());
		}

		// DH 14 SCP.512
		try {
			if (param1.equalsIgnoreCase("Search") && param2.equalsIgnoreCase("Order Type")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())=\""
						+ param1 + "\"]/following::label[text()=\"" + param2 + "\"]/following::input)[1]")));

				WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())=\"" + param1
						+ "\"]/following::label[text()=\"" + param2 + "\"]/following::input)[1]"));

				Actions actions = new Actions(driver);

				waittext.click();

				Thread.sleep(10000);

				WebElement search = driver
						.findElement(By.xpath("//div[contains(@id,\"popup-container\")]//label[text()=\"All\"]/input[1]"));

				search.click();

				Thread.sleep(4000);

				search.click();

				WebElement Value = driver.findElement(By
						.xpath("//div[contains(@id,\"popup-container\")]//label[text()=\"" + keysToSend + "\"]/input[1]"));

				Value.click();
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//*[normalize-space(text())=\"param1\"]/following::label[text()=\"param2\"]/following::input)[1]"
						+ ";" + "//div[contains(@id,\"popup-container\")]//label[text()=\"All\"]/input[1]" + ";"
						+ "//div[contains(@id,\"popup-container\")]//label[text()=\"keysToSend\"]/input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked Schedule New Process or Name dropdownValues" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Schedule New Process or Name dropdownValues" + scripNumber);
			logger.error(e.getMessage());
		}

		// DH 24
		try {
			if (param1.equalsIgnoreCase("Pay Groups") || param1.equalsIgnoreCase("Sources")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[text()=\"" + param1 + "\"]/following::*[@title=\"" + param2 + "\"]")));
				WebElement waittext = driver
						.findElement(By.xpath("//*[text()=\"" + param1 + "\"]/following::*[@title=\"" + param2 + "\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(2000);
				WebElement search = driver.findElement(By.xpath("//a[contains(text(),\"Search\")]"));
				clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
				WebElement values = driver.findElement(By.xpath(
						"//div[@class=\"AFDetectExpansion\"]/following::*[text()=\"Search\"]/following::*[normalize-space(text())=\""
								+ param2 + "\"]/following::input[1]"));
				typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				WebElement select = driver
						.findElement(By.xpath("//div[@class=\"AFDetectExpansion\"]/following::span[starts-with(text(),\""
								+ keysToSend + "\")][1]"));
				clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO, customerDetails);
				WebElement searchok = driver
						.findElement(By.xpath("//div[@class=\"AFDetectExpansion\"]/following::span[contains(text(),\""
								+ keysToSend + "\")][1]/following::button[text()=\"OK\"][1]"));
				clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO, customerDetails);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[text()=\"param1\"]/following::*[@title=\"param2\"]" + ";"
						+ "//a[contains(text(),\"Search\")]" + ";"
						+ "//div[@class=\"AFDetectExpansion\"]/following::*[text()=\"Search\"]/following::*[normalize-space(text())=\"param2\"]/following::input[1]"
						+ ";" + "//div[@class=\"AFDetectExpansion\"]/following::span[starts-with(text(),\"keysToSend\")][1]"
						+ ";"
						+ "//div[@class=\"AFDetectExpansion\"]/following::span[contains(text(),\"keysToSend\")][1]/following::button[text()=\"OK\"][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked Schedule New Process or Name dropdownValues" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Schedule New Process or Name dropdownValues" + scripNumber);
			logger.error(e.getMessage());
		}

		try {
			if (param1.equalsIgnoreCase("Schedule New Process") && param2.equalsIgnoreCase("Name")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//div[@class=\"AFDetectExpansion\"]/following::*[text()=\"" + param1
								+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::a[1]")));
				WebElement waittext = driver
						.findElement(By.xpath("//div[@class=\"AFDetectExpansion\"]/following::*[text()=\"" + param1
								+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::a[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(2000);
				WebElement search = driver.findElement(By.xpath("//a[contains(text(),\"Search\")]"));
				clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
				WebElement values = driver.findElement(By.xpath(
						"//div[@class=\"AFDetectExpansion\"]/following::*[text()=\"Search\"]/following::*[normalize-space(text())=\""
								+ param2 + "\"]/following::input[1]"));
				typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				WebElement select = driver
						.findElement(By.xpath("//div[@class=\"AFDetectExpansion\"]/following::span[starts-with(text(),\""
								+ keysToSend + "\")][1]"));
				clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO, customerDetails);
				WebElement searchok = driver
						.findElement(By.xpath("//div[@class=\"AFDetectExpansion\"]/following::span[contains(text(),\""
								+ keysToSend + "\")][1]/following::button[text()=\"OK\"][1]"));
				clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO, customerDetails);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(5000);
				WebElement ok = driver.findElement(By.xpath("//div[@class=\"AFDetectExpansion\"]/following::*[text()=\""
						+ param1 + "\"]/following::*[normalize-space(text())=\"" + param2
						+ "\"]/following::a[1]/following::button[text()=\"OK\"]"));
				ok.click();
				Thread.sleep(6000);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "//div[@class=\"AFDetectExpansion\"]/following::*[text()=\"param1\"]/following::*[normalize-space(text())=\"param2 \"]/following::a[1]"
						+ ";" + "//a[contains(text(),\"Search\")]" + ";"
						+ "//div[@class=\"AFDetectExpansion\"]/following::*[text()=\"Search\"]/following::*[normalize-space(text())=\"param2\"]/following::input[1]"
						+ ";" + "//div[@class=\"AFDetectExpansion\"]/following::span[starts-with(text(),\"keysToSend\")][1]"
						+ ";"
						+ "//div[@class=\"AFDetectExpansion\"]/following::span[contains(text(),\"keysToSend\")][1]/following::button[text()=\"OK\"][1]"
						+ ";"
						+ "//div[@class=\"AFDetectExpansion\"]/following::*[text()=\"param1\"]/following::*[normalize-space(text())=\"param2\"]/following::a[1]/following::button[text()=\"OK\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked Schedule New Process or Name dropdownValues" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Schedule New Process or Name dropdownValues" + scripNumber);
			logger.error(e.getMessage());
		}

		try {
			if (param1.equalsIgnoreCase("Invoice Header") && param2.equalsIgnoreCase("Business Unit")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::label[text()=\"" + param2 + "\"]/following::a[1]")));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::label[text()=\"" + param2 + "\"]/following::a[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "//*[normalize-space(text())=\"param1\"]/following::label[text()=\"param2\"]/following::a[1]";

				try {
					actions.click(waittext).build().perform();
					Thread.sleep(10000);
					// WebElement popup1 =
					// driver.findElement(By.xpath("//div[contains(@id,\"suggestions-popup\")]"));
					WebElement search = driver.findElement(By.xpath("//a[contains(text(),\"Search\")][1]"));
					search.click();
					Thread.sleep(3000);
					wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
							"//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\"Name\"]/following::input[1]")));
					WebElement searchResult = driver.findElement(By.xpath(
							"//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\"Name\"]/following::input[1]"));
					typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
					Thread.sleep(5000);
					WebElement text = driver.findElement(By.xpath(
							"//span[text()=\"Name\"]/following::span[normalize-space(text())=\"" + keysToSend + "\"]"));
					text.click();
					Thread.sleep(1000);
					WebElement button = driver.findElement(By.xpath(
							"//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\"Name\"]/following::*[text()=\"OK\"][1]"));
					button.click();

					logger.info("Sucessfully Clicked Invoice Header or Business Unit dropdownValues" + scripNumber);

					xpath = xpath + ";" + "//a[contains(text(),\"Search\")][1]" + ";"
							+ "//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\"Name\"]/following::input[1]"
							+ ";" + "//span[text()=\"Name\"]/following::span[normalize-space(text())=\"keysToSend\"]" + ";"
							+ "//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\"Name\"]/following::*[text()=\"OK\"][1]";

				} catch (Exception e) {
					// String scripNumber = fetchMetadataVO.getScriptNumber();
					logger.error("Failed during Invoice Header or Business Unit  dropdownValues" + scripNumber);
					logger.error(e.getMessage());
				}
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception ex) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  dropdownValues" + scripNumber);
			logger.error(ex.getMessage());
		}
		// This is to select the dropdown and select \"All\" and deselect All then
		// Selecting Draft
		// DH_50
		try {
			if (((param2.equalsIgnoreCase("Project Status") && keysToSend.equalsIgnoreCase("Draft"))
					|| (param1.equalsIgnoreCase("Basic Options") && param2.equalsIgnoreCase("Template"))
					|| param2.equalsIgnoreCase("Campaign Purpose"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::label[text()=\"" + param2 + "\"]/following::a[1]")));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::label[text()=\"" + param2 + "\"]/following::a[1]"));
				Actions actions = new Actions(driver);
				actions.click(waittext).build().perform();
				Thread.sleep(4000);
				// WebElement checkbox = driver.findElement(By.xpath("//label[text()=\"All\"]"));
				// checkbox.click();
				// Thread.sleep(3000);
				// checkbox.click();
				WebElement text = driver.findElement(By.xpath("//label[text()=\"" + param2
						+ "\"]/following::label[normalize-space(text())=\"" + keysToSend + "\"]"));
				text.click();
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "//*[normalize-space(text())=\"param1\"]/following::label[text()=\"param2\"]/following::a[1]"
						+ ";" + "//label[text()=\"param2\"]/following::label[normalize-space(text())=\"keysToSend\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked Project Status or Draft dropdownValues" + scripNumber);
				return;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		// --------------------------(including new change
		// here)<------------------------------
		try {
			if (param1.equalsIgnoreCase("Create Order") && param2.equalsIgnoreCase("Search: Bill-to Account")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[contains(text(),\"" + param1 + "\")]/following::a[@title=\"" + param2 + "\"][1]")));
				WebElement waittext = driver.findElement(
						By.xpath("//*[contains(text(),\"" + param1 + "\")]/following::a[@title=\"" + param2 + "\"][1]"));
				Actions actions = new Actions(driver);
				actions.click(waittext).build().perform();
				Thread.sleep(4000);
				WebElement search = driver.findElement(By.xpath("//a[text()=\"Search...\"]"));
				search.click();
				Thread.sleep(2000);
				WebElement Value = driver.findElement(By.xpath(
						"//div[text()=\"Search and Select: Bill-to Account\"]/following::label[text()=\"Account Number\"]/following::input[1]"));
				typeIntoValidxpath(driver, keysToSend, Value, fetchConfigVO, fetchMetadataVO);
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(1000);
				WebElement clickok = driver
						.findElement(By.xpath("//h1[text()=\"Search\"]/following::button[text()=\"OK\"][1]"));
				clickok.click();
				Thread.sleep(2000);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "//*[contains(text(),\"param1\")]/following::a[@title=\"param2\"][1]" + ";"
						+ "//a[text()=\"Search...\"]" + ";"
						+ "//div[text()=\"Search and Select: Bill-to Account\"]/following::label[text()=\"Account Number\"]/following::input[1]"
						+ ";" + "//h1[text()=\"Search\"]/following::button[text()=\"OK\"][1]";
				// service.saveXpathParams(param1, param2, scripNumber, xpath);
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				logger.info("Sucessfully Clicked Project Status or Draft dropdownValues" + scripNumber);
				return;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}

		// --------------------------------------------(ends
		// here)-------------------------------------

		try {
			if (param1.equalsIgnoreCase("Create Contract in Wizard") && param2.equalsIgnoreCase("Primary Party")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::label[text()=\"" + param2 + "\"]/following::a[1]")));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::label[text()=\"" + param2 + "\"]/following::a[1]"));
				Actions actions = new Actions(driver);
				actions.click(waittext).build().perform();
				Thread.sleep(4000);
				WebElement Search = driver.findElement(By.xpath("(//a[text()=\"Search...\"])[3]"));
				Search.click();
				WebElement Name = driver.findElement(By.xpath(
						"//h2[normalize-space(text())=\"Search\"]/following::label[text()=\"Name\"]/following::input[1]"));
				typeIntoValidxpath(driver, keysToSend, Name, fetchConfigVO, fetchMetadataVO);
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
				WebElement text = driver.findElement(By
						.xpath("//span[text()=\"Name\"]/following::span[normalize-space(text())=\"" + keysToSend + "\"]"));
				text.click();
				Thread.sleep(1000);
				WebElement button = driver.findElement(By.xpath(
						"//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\"Name\"]/following::*[text()=\"OK\"][1]"));
				button.click();
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "//*[normalize-space(text())=\"param1\"]/following::label[text()=\"param2\"]/following::a[1]"
						+ ";" + "(//a[text()=\"Search...\"])[3]" + ";"
						+ "//h2[normalize-space(text())=\"Search\"]/following::label[text()=\"Name\"]/following::input[1]"
						+ ";" + "//span[text()=\"Name\"]/following::span[normalize-space(text())=\"keysToSend\"]" + ";"
						+ "//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\"Name\"]/following::*[text()=\"OK\"][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Create Bank Account") && param2.equalsIgnoreCase("Country")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::label[text()=\"" + param2 + "\"]/following::a[1]")));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::label[text()=\"" + param2 + "\"]/following::a[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				actions.click(waittext).build().perform();
				Thread.sleep(10000);
				WebElement search = driver.findElement(By.xpath(
						"//*[contains(@id,\"territoryShortNameId\")]/following-sibling::a[contains(text(),\"Search\")]"));
				search.click();
				Thread.sleep(3000);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"(//h2[normalize-space(text())=\"Search\"]/following::label[normalize-space(text())=\"Name\"]/following::input)[1]")));
				WebElement searchResult = driver.findElement(By.xpath(
						"(//h2[normalize-space(text())=\"Search\"]/following::label[normalize-space(text())=\"Name\"]/following::input)[1]"));
				typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
				WebElement text = driver.findElement(By
						.xpath("//span[text()=\"Name\"]/following::span[normalize-space(text())=\"" + keysToSend + "\"]"));
				text.click();
				Thread.sleep(1000);
				WebElement button = driver.findElement(By.xpath(
						"//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\"Name\"]/following::*[text()=\"OK\"][1]"));
				button.click();
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "//*[normalize-space(text())=\"param1\"]/following::label[text()=\"param2\"]/following::a[1]"
						+ ";"
						+ "//*[contains(@id,\"territoryShortNameId\")]/following-sibling::a[contains(text(),\"Search\")]"
						+ ";"
						+ "(//h2[normalize-space(text())=\"Search\"]/following::label[normalize-space(text())=\"Name\"]/following::input)[1]"
						+ ";" + "//span[text()=\"Name\"]/following::span[normalize-space(text())=\"keysToSend\"]" + ";"
						+ "//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\"Name\"]/following::*[text()=\"OK\"][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked Create Bank Account or Country dropdownValues" + scripNumber);
				return;
			}
		} catch (Exception ex) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Create Bank Account or Country dropdownValues" + scripNumber);
			logger.error(ex.getMessage());
		}
		// for "PTP.PO.301 Request New Supplier" when exectuing in Fusion instance
		try {
			if (param1.equalsIgnoreCase("Company Details") && param2.equalsIgnoreCase("Tax Country")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::label[text()=\"" + param2 + "\"]/following::a[@title=\"Search: Tax Country\"]")));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::label[text()=\"" + param2 + "\"]/following::a[@title=\"Search: Tax Country\"]"));
				Actions actions = new Actions(driver);
				actions.click(waittext).build().perform();
				Thread.sleep(4000);
				WebElement Search = driver.findElement(By.xpath("(//a[text()=\"Search...\"])"));
				Search.click();
				WebElement Name = driver.findElement(By.xpath(
						"//div[normalize-space(text())=\"Search and Select: Tax Country\"]/following::label[text()=\"Name\"]/following::input[1]"));
				typeIntoValidxpath(driver, keysToSend, Name, fetchConfigVO, fetchMetadataVO);
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
				WebElement text = driver.findElement(By
						.xpath("//span[text()=\"Name\"]/following::span[normalize-space(text())=\"" + keysToSend + "\"]"));
				text.click();
				Thread.sleep(1000);
				WebElement button = driver.findElement(By.xpath(
						"//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\"Name\"]/following::*[text()=\"OK\"][1]"));
				button.click();
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::label[text()=\"param2\"]/following::a[@title=\"Search: Tax Country\"]"
						+ ";" + "(//a[text()=\"Search...\"])" + ";"
						+ "//div[normalize-space(text())=\"Search and Select: Tax Country\"]/following::label[text()=\"Name\"]/following::input[1]"
						+ ";" + "//span[text()=\"Name\"]/following::span[normalize-space(text())=\"keysToSend\"]" + ";"
						+ "//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\"Name\"]/following::*[text()=\"OK\"][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Create Address") && param2.equalsIgnoreCase("Country")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::label[text()=\"" + param2 + "\"]/following::a[@title=\"Search: Country\"]")));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::label[text()=\"" + param2 + "\"]/following::a[@title=\"Search: Country\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				actions.click(waittext).build().perform();
				Thread.sleep(10000);
				WebElement search = driver.findElement(By.xpath(
						"//*[contains(@id,\"inputComboboxListOfValues1\")]/following-sibling::a[contains(text(),\"Search\")]"));
				search.click();
				Thread.sleep(3000);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"(//h2[normalize-space(text())=\"Search\"]/following::label[normalize-space(text())=\"Name\"]/following::input)[1]")));
				WebElement searchResult = driver.findElement(By.xpath(
						"(//h2[normalize-space(text())=\"Search\"]/following::label[normalize-space(text())=\"Name\"]/following::input)[1]"));
				typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
				WebElement text = driver.findElement(By
						.xpath("//span[text()=\"Name\"]/following::span[normalize-space(text())=\"" + keysToSend + "\"]"));
				text.click();
				Thread.sleep(5000);
				WebElement button = driver.findElement(By.xpath(
						"//button[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\"Name\"]/following::button[text()=\"OK\"][1]"));
				button.click();
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "//*[normalize-space(text())=\"param1\"]/following::label[text()=\"param2\"]/following::a[@title=\"Search: Country\"]"
						+ ";"
						+ "//*[contains(@id,\"inputComboboxListOfValues1\")]/following-sibling::a[contains(text(),\"Search\")]"
						+ ";"
						+ "(//h2[normalize-space(text())=\"Search\"]/following::label[normalize-space(text())=\"Name\"]/following::input)[1]"
						+ ";" + "//span[text()=\"Name\"]/following::span[normalize-space(text())=\"keysToSend\"]" + ";"
						+ "//button[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\"Name\"]/following::button[text()=\"OK\"][1]";

				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked Create Address or Country dropdownValues" + scripNumber);
				return;
			}
		} catch (Exception ex) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Create Address or Country  dropdownValues" + scripNumber);
			logger.error(ex.getMessage());
		}
		try {

			if (param1.equalsIgnoreCase("Assets")) {

				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[text()=\"" + param1 + "\"]/following::a[@role=\"button\"][1]")));

				WebElement waittext = driver
						.findElement(By.xpath("//*[text()=\"" + param1 + "\"]/following::a[@role=\"button\"][1]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittext).build().perform();

				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);

				waittext.click();

				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(30000);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "//*[text()=\"param1\"]/following::a[@role=\"button\"][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked Assets dropdownValues" + scripNumber);
				return;

			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Assets dropdownValues" + scripNumber);
			logger.error(e.getMessage());

		}
		try {
			if (param1.equalsIgnoreCase("Create Request") || param2.equalsIgnoreCase("CIP Budget Code")
					|| param1.equalsIgnoreCase("Demographic Info")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[contains(text(),\"" + param1
						+ "\")]/following::label[normalize-space(text())=\"" + param2 + "\"]/following::a)[1]")));
				WebElement waittext = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1
						+ "\")]/following::label[normalize-space(text())=\"" + param2 + "\"]/following::a)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "(//*[contains(text(),\"param1\")]/following::label[normalize-space(text())=\"param2\"]/following::a)[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked Create Request dropdownValues" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Create Request dropdownValues" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Payables to Ledger Reconciliation Report")) {

				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(10000);
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("(//span[contains(text(),\"" + param2 + "\")]/following::img)[1]")));
				WebElement waittext = driver
						.findElement(By.xpath("(//span[contains(text(),\"" + param2 + "\")]/following::img)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				Thread.sleep(4000);
				WebElement dropdown = driver.findElement(By.xpath("//span[text()=\"" + keysToSend + "\"]"));
				actions.moveToElement(dropdown).build().perform();
				dropdown.click();
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "(//span[contains(text(),\"param2\")]/following::img)[1]" + ";"
						+ "//span[text()=\"keysToSend\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked Payables to Ledger Reconciliation Report dropdownValues" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Payables to Ledger Reconciliation Report dropdownValues" + scripNumber);
			logger.error(e.getMessage());
		}
		try {

			if (param1.equalsIgnoreCase("P2P-3031-Spend Detail by Invoice Number")
					|| param1.equalsIgnoreCase("P2P-3026-Payment Terms by Supplier vs Actual Days Paid")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(10000);
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[normalize-space(text())=\"" + param2 + "\"]/following::input[1]")));
				WebElement waittext = driver
						.findElement(By.xpath("//*[normalize-space(text())=\"" + param2 + "\"]/following::input[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				String xpath = "//*[normalize-space(text())=\"param2\"]/following::input[1]";
				Thread.sleep(2000);
				if (param2.equalsIgnoreCase("Procurement BU") || param2.equalsIgnoreCase("Business Unit")) {
					WebElement search = driver
							.findElement(By.xpath("//div[@class=\"listbox\"]//span[contains(text(),\"Search\")]"));
					// clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO);
					search.click();
					Thread.sleep(1000);
					WebElement values = driver.findElement(By.xpath("(//span[text()=\"Name\"]/following::input)[1]"));
					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
					WebElement select = driver.findElement(By
							.xpath("//*[text()=\"Name\"]/following::div[normalize-space(text())=\"" + keysToSend + "\"]"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO, customerDetails);
					WebElement searchok = driver
							.findElement(By.xpath("//span[text()=\"Name\"]/following::button[text()=\"OK\"]"));
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO, customerDetails);
					screenshot(driver, fetchMetadataVO, customerDetails);
					String scripNumber = fetchMetadataVO.getScriptNumber();

					xpath = xpath + ";" + "//div[@class=\"listbox\"]//span[contains(text(),\"Search\")]" + ";"
							+ "(//span[text()=\"Name\"]/following::input)[1]" + ";"
							+ "//*[text()=\"Name\"]/following::div[normalize-space(text())=\"keysToSend\"]" + ";"
							+ "//span[text()=\"Name\"]/following::button[text()=\"OK\"]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
					logger.info("Sucessfully Clicked Procurement BU or Business Unit dropdownValues" + scripNumber);
					return;
				} else if (param2.equalsIgnoreCase("Requisition BU")) {
					Thread.sleep(2000);
					WebElement search = driver
							.findElement(By.xpath("(//div[@class=\"listbox\"]//span[contains(text(),\"Search\")])[1]"));
					// clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO);
					search.click();
					Thread.sleep(1000);
					WebElement values = driver
							.findElement(By.xpath("(//span[text()=\"Name\"]/following::input[@type=\"text\"])[2]"));
					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
					WebElement select = driver
							.findElement(By.xpath("//*[text()=\"Name\"]/following::div[text()=\"" + keysToSend + "\"]"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO, customerDetails);
					WebElement searchok = driver
							.findElement(By.xpath("//span[text()=\"Name\"]/following::button[text()=\"OK\"][2]"));
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO, customerDetails);
					screenshot(driver, fetchMetadataVO, customerDetails);
					String scripNumber = fetchMetadataVO.getScriptNumber();

					xpath = xpath + ";" + "(//div[@class=\"listbox\"]//span[contains(text(),\"Search\")])[1]" + ";"
							+ "(//span[text()=\"Name\"]/following::input[@type=\"text\"])[2]" + ";"
							+ "//*[text()=\"Name\"]/following::div[text()=\"keysToSend\"]" + ";"
							+ "//span[text()=\"Name\"]/following::button[text()=\"OK\"][2]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
					logger.info("Sucessfully Clicked Requisition BU dropdownValues" + scripNumber);
					return;
				} else if (param2.equalsIgnoreCase("Supplier Name")) {
					if (keysToSend.equalsIgnoreCase("All")) {
						WebElement select = driver.findElement(By.xpath("//span[text()=\"" + param2
								+ "\"]/following::div[normalize-space(text())=\"" + keysToSend + "\"][1]"));
						clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO, customerDetails);
						xpath = xpath + ";"
								+ "//span[text()=\"param2\"]/following::div[normalize-space(text())=\"keysToSend\"][1]";
						String scriptID = fetchMetadataVO.getScriptId();
						String lineNumber = fetchMetadataVO.getLineNumber();
						service.saveXpathParams(scriptID, lineNumber, xpath);
						return;
					} else {
						WebElement search = driver
								.findElement(By.xpath("(//div[@class=\"listbox\"]//span[contains(text(),\"Search\")])[3]"));
						clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO, customerDetails);
						Thread.sleep(1000);
						WebElement values = driver
								.findElement(By.xpath("(//span[text()=\"Name\"]/following::input[@type=\"text\"])[3]"));
						typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
						enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
						WebElement select = driver.findElement(By.xpath(
								"//*[text()=\"Name\"]/following::div[normalize-space(text())=\"" + keysToSend + "\"]"));
						clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO, customerDetails);
						WebElement searchok = driver
								.findElement(By.xpath("//span[text()=\"Name\"]/following::button[text()=\"OK\"][3]"));
						clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO, customerDetails);
						screenshot(driver, fetchMetadataVO, customerDetails);
						String scripNumber = fetchMetadataVO.getScriptNumber();

						xpath = xpath + ";" + "(//div[@class=\"listbox\"]//span[contains(text(),\"Search\")])[3]" + ";"
								+ "(//span[text()=\"Name\"]/following::input[@type=\"text\"])[3]" + ";"
								+ "//*[text()=\"Name\"]/following::div[normalize-space(text())=\"keysToSend\"]" + ";"
								+ "//span[text()=\"Name\"]/following::button[text()=\"OK\"][3]";
						String scriptID = fetchMetadataVO.getScriptId();
						String lineNumber = fetchMetadataVO.getLineNumber();
						service.saveXpathParams(scriptID, lineNumber, xpath);
						logger.info("Sucessfully  Supplier Name Clicked dropdownValues" + scripNumber);

						return;
					}
				}
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  dropdownValues" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("FIN-7073-UDG Cognos Extract")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(10000);
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[normalize-space(text())=\"" + param2 + "\"]/following::input[1]")));
				WebElement waittext = driver
						.findElement(By.xpath("//*[normalize-space(text())=\"" + param2 + "\"]/following::input[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				String xpath = "//*[normalize-space(text())=\"param2\"]/following::input[1]";
				Thread.sleep(2000);
				if (param2.equalsIgnoreCase("Period Name")) {
					WebElement search = driver
							.findElement(By.xpath("//div[@class=\"listbox\"]//span[contains(text(),\"Search\")]"));
					clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO, customerDetails);
					Thread.sleep(1000);
					WebElement values = driver.findElement(By.xpath("(//span[text()=\"Name\"]/following::input)[1]"));
					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
					WebElement select = driver.findElement(By
							.xpath("//*[text()=\"Name\"]/following::div[normalize-space(text())=\"" + keysToSend + "\"]"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO, customerDetails);
					WebElement searchok = driver
							.findElement(By.xpath("//span[text()=\"Name\"]/following::button[text()=\"OK\"]"));
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO, customerDetails);
					screenshot(driver, fetchMetadataVO, customerDetails);
					String scripNumber = fetchMetadataVO.getScriptNumber();

					xpath = xpath + ";" + "//div[@class=\"listbox\"]//span[contains(text(),\"Search\")]" + ";"
							+ "(//span[text()=\"Name\"]/following::input)[1]" + ";"
							+ "//*[text()=\"Name\"]/following::div[normalize-space(text())=\"keysToSend\"]" + ";"
							+ "//span[text()=\"Name\"]/following::button[text()=\"OK\"]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
					logger.info("Sucessfully Clicked Period Name dropdownValues" + scripNumber);
					return;
				} else if (param2.equalsIgnoreCase("Legal Entity")) {
					WebElement search = driver
							.findElement(By.xpath("//div[@class=\"listbox\"]//span[contains(text(),\"Search\")]"));
					clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO, customerDetails);
					Thread.sleep(1000);
					WebElement values = driver.findElement(By.xpath(
							"//div[@class=\"masterDialog modalDialog\"]/following::span[text()=\"Name\"]/following::input[1]"));
					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
					Thread.sleep(6000);
					WebElement select = driver.findElement(By
							.xpath("//*[text()=\"Name\"]/following::div[normalize-space(text())=\"" + keysToSend + "\"]"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO, customerDetails);
					WebElement searchok = driver.findElement(By.xpath(
							"//div[@class=\"masterDialog modalDialog\"]/following::span[text()=\"Name\"]/following::button[text()=\"OK\"]"));
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO, customerDetails);
					screenshot(driver, fetchMetadataVO, customerDetails);
					String scripNumber = fetchMetadataVO.getScriptNumber();

					xpath = xpath + ";" + "//div[@class=\"listbox\"]//span[contains(text(),\"Search\")]" + ";"
							+ "//div[@class=\"masterDialog modalDialog\"]/following::span[text()=\"Name\"]/following::input[1]"
							+ ";" + "//*[text()=\"Name\"]/following::div[normalize-space(text())=\"keysToSend\"]" + ";"
							+ "//div[@class=\"masterDialog modalDialog\"]/following::span[text()=\"Name\"]/following::button[text()=\"OK\"]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
					logger.info("Sucessfully Clicked Legal Entity dropdownValues" + scripNumber);
					return;
				}
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  dropdownValues" + scripNumber);
			logger.error(e.getMessage());
		}
		// DH 27
		try {
			if ((param1.equalsIgnoreCase("FIN-7056-Generate Customer Statements")
					|| param1.equalsIgnoreCase("FIN-7077-Customer Statement"))
					&& (param2.equalsIgnoreCase("Legal Entity") || param2.equalsIgnoreCase("Customer Name"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(10000);
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[normalize-space(text())=\"" + param2 + "\"]/following::input[1]")));
				WebElement waittext = driver
						.findElement(By.xpath("//*[normalize-space(text())=\"" + param2 + "\"]/following::input[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				String xpath = "//*[normalize-space(text())=\"param2\"]/following::input[1]";
				Thread.sleep(2000);
				if (param2.equalsIgnoreCase("Legal Entity")) {
					WebElement search = driver.findElement(
							By.xpath("//a[contains(@id,\"LEGAL_ENTITY\")][1]//span/span[contains(text(),\"Search\")]"));
					clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO, customerDetails);
					Thread.sleep(1000);
					WebElement values = driver
							.findElement(By.xpath("(//span[text()=\"Name\"]/following::input[@type=\"text\"])[1]"));
					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
					WebElement select = driver.findElement(By
							.xpath("//*[text()=\"Value\"]/following::div[normalize-space(text())=\"" + keysToSend + "\"]"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO, customerDetails);
					WebElement searchok = driver
							.findElement(By.xpath("//div[text()=\"Search\"]/following::button[text()=\"OK\"]"));
					actions.moveToElement(searchok).build().perform();
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO, customerDetails);
					screenshot(driver, fetchMetadataVO, customerDetails);
					String scripNumber = fetchMetadataVO.getScriptNumber();

					xpath = xpath + ";" + "//a[contains(@id,\"LEGAL_ENTITY\")][1]//span/span[contains(text(),\"Search\")]"
							+ ";" + "(//span[text()=\"Name\"]/following::input[@type=\"text\"])[1]" + ";"
							+ "//*[text()=\"Value\"]/following::div[normalize-space(text())=\"keysToSend\"]" + ";"
							+ "//div[text()=\"Search\"]/following::button[text()=\"OK\"]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
					logger.info("Sucessfully Clicked Legal Entity dropdownValues" + scripNumber);
					return;
				} else if (param2.equalsIgnoreCase("Customer Name")) {
					WebElement search1 = driver.findElement(
							By.xpath("//a[contains(@id,\"CUSTOMER_NAME\")][1]//span/span[contains(text(),\"Search\")]"));
					clickValidateXpath(driver, fetchMetadataVO, search1, fetchConfigVO, customerDetails);
					Thread.sleep(1000);
					WebElement values = driver.findElement(By.xpath(
							"//div[@class=\"masterDialog modalDialog\"]/following::span[text()=\"Name\"]/following::input[@type=\"text\"][1]"));
					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
					WebElement select = driver.findElement(
							By.xpath("//*[text()=\"Value\"]/following::div[contains(text(),\"" + keysToSend + "\")]"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO, customerDetails);
					Thread.sleep(1000);
					WebElement searchok = driver
							.findElement(By.xpath("//div[text()=\"Search\"]/following::button[text()=\"OK\"][2]"));
					actions.moveToElement(searchok).build().perform();
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO, customerDetails);
					screenshot(driver, fetchMetadataVO, customerDetails);
					String scripNumber = fetchMetadataVO.getScriptNumber();

					xpath = xpath + ";" + "//a[contains(@id,\"CUSTOMER_NAME\")][1]//span/span[contains(text(),\"Search\")]"
							+ ";"
							+ "//div[@class=\"masterDialog modalDialog\"]/following::span[text()=\"Name\"]/following::input[@type=\"text\"][1]"
							+ ";" + "//*[text()=\"Value\"]/following::div[contains(text(),\"keysToSend\")]" + ";"
							+ "//div[text()=\"Search\"]/following::button[text()=\"OK\"][2]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
					logger.info("Sucessfully Clicked Customer Name dropdownValues" + scripNumber);
					return;
				}

			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  dropdownValues" + scripNumber);
			logger.error(e.getMessage());
		}
		try {

			if (param1.equalsIgnoreCase("Applied Receipts Register")
					|| param1.equalsIgnoreCase("General Journals Report")
					|| param1.equalsIgnoreCase("Unapplied Receipt Register") || param2.equalsIgnoreCase("Request Name")
					|| (param1.equalsIgnoreCase("Receivables to Ledger Reconciliation")

							&& param2.equalsIgnoreCase("Ledger"))) {
				String xpath;

				if (param1.equalsIgnoreCase("General Journals Report") && (param2.equalsIgnoreCase("Ledger"))) {

					WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

					Thread.sleep(5000);

					wait.until(ExpectedConditions.presenceOfElementLocated(

							By.xpath("(//span[text()=\"" + param2 + "\"]/following::img)[1]")));

					WebElement waittext = driver

							.findElement(By.xpath("(//span[text()=\"" + param2 + "\"]/following::img)[1]"));

					Actions actions = new Actions(driver);

					actions.moveToElement(waittext).build().perform();

					// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);

					waittext.click();
					xpath = "(//span[text()=\"param2\"]/following::img)[1]";
					Thread.sleep(2000);

				}

				else {

					WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

					Thread.sleep(10000);

					wait.until(ExpectedConditions.presenceOfElementLocated(

							By.xpath("(//span[contains(text(),\"" + param2 + "\")]/following::img)[1]")));

					WebElement waittext = driver

							.findElement(By.xpath("(//span[contains(text(),\"" + param2 + "\")]/following::img)[1]"));

					Actions actions = new Actions(driver);

					actions.moveToElement(waittext).build().perform();

					// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);

					waittext.click();
					xpath = "(//span[contains(text(),\"param2\")]/following::img)[1]";
					Thread.sleep(2000);

				}

				if (param2.equalsIgnoreCase("Ledger") || param2.equalsIgnoreCase("Ledger Set")
						|| param2.equalsIgnoreCase("Entered Currency")

						|| param2.equalsIgnoreCase("Approval Status") || param2.equalsIgnoreCase("Batch Status")) {

					WebElement search = driver.findElement(

							By.xpath("//div[@class=\"floatingWindowDiv\"]//span[contains(text(),\"Search\")]"));

					// clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO);

					search.click();

					Thread.sleep(1000);

					WebElement dropdown = driver.findElement(By.xpath("//span[text()=\"Name\"]/following::select[1]"));

					dropdown.sendKeys("Starts");

					WebElement values = driver.findElement(By.xpath("(//span[text()=\"Name\"]/following::input)[1]"));

					// typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO,
					// fetchMetadataVO);

					values.sendKeys(keysToSend);
					Thread.sleep(1000);
					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);

					Thread.sleep(10000);
//					DH 27
					WebElement select = driver.findElement(By

							.xpath("//*[text()=\"Name\"]/following::*[normalize-space(text())=\"" + keysToSend + "\"]"));

//								clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO); 

					select.click();
//					DH 27
					Thread.sleep(1000);
					WebElement searchok = driver

							.findElement(By.xpath("//span[text()=\"Name\"]/following::a[text()=\"OK\"]"));

					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO, customerDetails);

					// searchok.click();

					screenshot(driver, fetchMetadataVO, customerDetails);

					String scripNumber = fetchMetadataVO.getScriptNumber();

					xpath = xpath + ";" + "//div[@class=\"floatingWindowDiv\"]//span[contains(text(),\"Search\")]" + ";"
							+ "//span[text()=\"Name\"]/following::select[1]" + ";"
							+ "(//span[text()=\"Name\"]/following::input)[1]" + ";"
							+ "//*[text()=\"Name\"]/following:*[normalize-space(text())=\"keysToSend\"]" + ";"
							+ "//span[text()=\"Name\"]/following::a[text()=\"OK\"]";

					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);

					logger.info("Sucessfully Clicked Ledger dropdownValues" + scripNumber);

					return;

				} else if (param2.equalsIgnoreCase("Request Name") || param2.equalsIgnoreCase("Source")
						|| param2.equalsIgnoreCase("Category")) {

					WebElement search = driver.findElement(

							By.xpath("//div[@class=\"floatingWindowDiv\"]//span[contains(text(),\"Search\")]"));

					// clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO);

					search.click();

					Thread.sleep(1000);

					WebElement values = driver.findElement(By.xpath("(//span[text()=\"Name\"]/following::input)[1]"));

					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);

					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);

					Thread.sleep(6000);
//					DH 27
					WebElement select = driver.findElement(By

							.xpath("//*[text()=\"Name\"]/following::*[normalize-space(text())=\"" + keysToSend + "\"]"));

					// clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);

					select.click();

					WebElement searchok = driver

							.findElement(By.xpath("//span[text()=\"Name\"]/following::a[text()=\"OK\"]"));

					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO, customerDetails);

					screenshot(driver, fetchMetadataVO, customerDetails);

					String scripNumber = fetchMetadataVO.getScriptNumber();

					xpath = xpath + ";" + "//div[@class=\"floatingWindowDiv\"]//span[contains(text(),\"Search\")]" + ";"
							+ "(//span[text()=\"Name\"]/following::input)[1]" + ";"
							+ "//*[text()=\"Name\"]/following::*[normalize-space(text())=\"keysToSend\"]" + ";"
							+ "//span[text()=\"Name\"]/following::a[text()=\"OK\"]";

					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);

					logger.info("Sucessfully Clicked Request Name dropdownValues" + scripNumber);

					return;

				}

				else if (param2.equalsIgnoreCase("Business Unit") || param2.equalsIgnoreCase("Accounting Period")) {

					WebElement search = driver.findElement(

							By.xpath("//div[@class=\"floatingWindowDiv\"]//span[contains(text(),\"Search\")]"));

					// clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO);

					search.click();

					Thread.sleep(1000);

					WebElement values = driver.findElement(By.xpath("(//span[text()=\"Name\"]/following::input)[1]"));

					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);

					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);

					WebElement select = driver.findElement(By

							.xpath("//*[text()=\"Name\"]/following::span[normalize-space(text())=\"" + keysToSend + "\"]"));

					// clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);

					select.click();

					WebElement move = driver.findElement(By

							.xpath("//td[@title=\"Move\"]"));

					// clickValidateXpath(driver, fetchMetadataVO, move, fetchConfigVO);

					move.click();

					WebElement searchok = driver

							.findElement(By.xpath("//span[text()=\"Name\"]/following::a[text()=\"OK\"]"));

					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO, customerDetails);

					// searchok.click();

					screenshot(driver, fetchMetadataVO, customerDetails);

					String scripNumber = fetchMetadataVO.getScriptNumber();

					xpath = xpath + ";" + "//div[@class=\"floatingWindowDiv\"]//span[contains(text(),\"Search\")]" + ";"
							+ "(//span[text()=\"Name\"]/following::input)[1]" + ";"
							+ "//*[text()=\"Name\"]/following::span[normalize-space(text())=\"keysToSend\"]" + ";"
							+ "//td[@title=\"Move\"]" + ";" + "//span[text()=\"Name\"]/following::a[text()=\"OK\"]";

					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);

					logger.info("Sucessfully Clicked Ledger dropdownValues" + scripNumber);

					return;

				}

			}

		} catch (Exception e) {

			String scripNumber = fetchMetadataVO.getScriptNumber();

			logger.error("Failed during  dropdownValues" + scripNumber);

			logger.error(e.getMessage());

		}
		try {
			if (param1.equalsIgnoreCase("FIN-7064-AP Invoice Summary")
					|| param1.equalsIgnoreCase("P2P-3000-AP Hold Detailed Report")
					|| param1.equalsIgnoreCase("FIN-7073-UDG Cognos Extract")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(15000);
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[normalize-space(text())=\"" + param2 + "\"]/following::input[1]")));
				WebElement waittext = driver
						.findElement(By.xpath("//*[normalize-space(text())=\"" + param2 + "\"]/following::input[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(2000);
				WebElement search = driver.findElement(
						By.xpath("(//a[contains(@id,\"legal\")or \"LE\"][1]//span/span[contains(text(),\"Search\")])[2]"));
				clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO, customerDetails);
				WebElement values = driver
						.findElement(By.xpath("(//span[text()=\"Name\"]/following::input[@type=\"text\"])"));
				typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				WebElement select = driver.findElement(
						By.xpath("//b[text()=\"Value\"]/following::div[normalize-space(text())=\"" + keysToSend + "\"]"));
				clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO, customerDetails);
				WebElement searchok = driver
						.findElement(By.xpath("//div[text()=\"Search\"]/following::button[text()=\"OK\"]"));
				clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO, customerDetails);
				Thread.sleep(10000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(15000);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "//*[normalize-space(text())=\"param2\"]/following::input[1]" + ";"
						+ "(//a[contains(@id,\"legal\")or \"LE\"][1]//span/span[contains(text(),\"Search\")])[2]" + ";"
						+ "(//span[text()=\"Name\"]/following::input[@type=\"text\"])" + ";"
						+ "//b[text()=\"Value\"]/following::div[normalize-space(text())=\"keysToSend\"]" + ";"
						+ "//div[text()=\"Search\"]/following::button[text()=\"OK\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked dropdownValues" + scripNumber);
				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  dropdownValues" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Report")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(5000);
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[normalize-space(text())=\"" + param2 + "\"]/following::input[1]")));
				WebElement waittext = driver
						.findElement(By.xpath("//*[normalize-space(text())=\"" + param2 + "\"]/following::input[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				String xpath = "//*[normalize-space(text())=\"param2\"]/following::input[1]";
				Thread.sleep(2000);
				if (param2.equalsIgnoreCase("Procurement Business Unit")) {
					WebElement search = driver.findElement(
							By.xpath("//a[contains(@id,\"PROCUREMENT\")][1]//span/span[contains(text(),\"Search\")]"));
					clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO, customerDetails);
					WebElement values = driver
							.findElement(By.xpath("(//span[text()=\"Name\"]/following::input[@type=\"text\"])[1]"));
					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
					WebElement select = driver.findElement(By
							.xpath("//*[text()=\"Value\"]/following::div[normalize-space(text())=\"" + keysToSend + "\"]"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO, customerDetails);
					WebElement searchok = driver
							.findElement(By.xpath("//div[text()=\"Search\"]/following::button[text()=\"OK\"]"));
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO, customerDetails);
					screenshot(driver, fetchMetadataVO, customerDetails);
					String scripNumber = fetchMetadataVO.getScriptNumber();

					xpath = xpath + ";" + "//a[contains(@id,\"PROCUREMENT\")][1]//span/span[contains(text(),\"Search\")]"
							+ ";" + "(//span[text()=\"Name\"]/following::input[@type=\"text\"])[1]" + ";"
							+ "//*[text()=\"Value\"]/following::div[normalize-space(text())=\"keysToSend\"]" + ";"
							+ "//div[text()=\"Search\"]/following::button[text()=\"OK\"]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
					logger.info("Sucessfully Clicked Report dropdownValues" + scripNumber);
					return;
				} else if (param2.equalsIgnoreCase("Ledger")) {
					Thread.sleep(1000);
					WebElement search = driver
							.findElement(By.xpath("//a[contains(@id,\"REQ\")][1]//span/span[contains(text(),\"Search\")]"));
					clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO, customerDetails);
					WebElement values = driver
							.findElement(By.xpath("(//span[text()=\"Name\"]/following::input[@type=\"text\"])[2]"));
					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
					WebElement select = driver.findElement(By.xpath(
							"//*[text()=\"Value\"]/following::div[normalize-space(text())=\"" + keysToSend + "\"][2]"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO, customerDetails);
					WebElement searchok = driver
							.findElement(By.xpath("//div[text()=\"Search\"]/following::button[text()=\"OK\"][2]"));
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO, customerDetails);
					screenshot(driver, fetchMetadataVO, customerDetails);
					String scripNumber = fetchMetadataVO.getScriptNumber();

					xpath = xpath + ";" + "//a[contains(@id,\"REQ\")][1]//span/span[contains(text(),\"Search\")]" + ";"
							+ "(//span[text()=\"Name\"]/following::input[@type=\"text\"])[2]" + ";"
							+ "//*[text()=\"Value\"]/following::div[normalize-space(text())=\"keysToSend\"][2]" + ";"
							+ "//div[text()=\"Search\"]/following::button[text()=\"OK\"][2]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
					logger.info("Sucessfully Clicked dropdownValues" + scripNumber);
					return;
				} else if (param2.equalsIgnoreCase("Request Name")) {
					Thread.sleep(1000);
					WebElement search = driver
							.findElement(By.xpath("//a[contains(@id,\"REQ\")][1]//span/span[contains(text(),\"Search\")]"));
					clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO, customerDetails);
					WebElement values = driver
							.findElement(By.xpath("(//span[text()=\"Name\"]/following::input[@type=\"text\"])[2]"));
					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
					WebElement select = driver.findElement(By.xpath(
							"//*[text()=\"Value\"]/following::div[normalize-space(text())=\"" + keysToSend + "\"][2]"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO, customerDetails);
					WebElement searchok = driver
							.findElement(By.xpath("//div[text()=\"Search\"]/following::button[text()=\"OK\"][2]"));
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO, customerDetails);
					screenshot(driver, fetchMetadataVO, customerDetails);
					String scripNumber = fetchMetadataVO.getScriptNumber();

					xpath = xpath + ";" + "//a[contains(@id,\"REQ\")][1]//span/span[contains(text(),\"Search\")]" + ";"
							+ "(//span[text()=\"Name\"]/following::input[@type=\"text\"])[2]" + ";"
							+ "//*[text()=\"Value\"]/following::div[normalize-space(text())=\"keysToSend\"][2]" + ";"
							+ "//div[text()=\"Search\"]/following::button[text()=\"OK\"][2]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
					logger.info("Sucessfully Clicked dropdownValues" + scripNumber);
					return;
				} else if (param2.equalsIgnoreCase("Requistion Business Unit")) {
					Thread.sleep(1000);
					WebElement search = driver
							.findElement(By.xpath("//a[contains(@id,\"REQ\")][1]//span/span[contains(text(),\"Search\")]"));
					clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO, customerDetails);
					WebElement values = driver
							.findElement(By.xpath("(//span[text()=\"Name\"]/following::input[@type=\"text\"])[2]"));
					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
					WebElement select = driver.findElement(By.xpath(
							"//*[text()=\"Value\"]/following::div[normalize-space(text())=\"" + keysToSend + "\"][2]"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO, customerDetails);
					WebElement searchok = driver
							.findElement(By.xpath("//div[text()=\"Search\"]/following::button[text()=\"OK\"][2]"));
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO, customerDetails);
					screenshot(driver, fetchMetadataVO, customerDetails);
					String scripNumber = fetchMetadataVO.getScriptNumber();

					xpath = xpath + ";" + "//a[contains(@id,\"REQ\")][1]//span/span[contains(text(),\"Search\")]" + ";"
							+ "(//span[text()=\"Name\"]/following::input[@type=\"text\"])[2]" + ";"
							+ "//*[text()=\"Value\"]/following::div[normalize-space(text())=\"keysToSend\"][2]" + ";"
							+ "//div[text()=\"Search\"]/following::button[text()=\"OK\"][2]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
					logger.info("Sucessfully Clicked Ledger dropdownValues" + scripNumber);
					return;
				} else if (param2.equalsIgnoreCase("Status")) {
					WebElement search = driver.findElement(
							By.xpath("//a[contains(@id,\"STATUS\")][1]//span/span[contains(text(),\"Search\")]"));
					clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO, customerDetails);
					WebElement values = driver
							.findElement(By.xpath("(//span[text()=\"Name\"]/following::input[@type=\"text\"])[3]"));
					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
					WebElement select = driver.findElement(By.xpath(
							"//*[text()=\"Value\"]/following::div[normalize-space(text())=\"" + keysToSend + "\"][1]"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO, customerDetails);
					WebElement searchok = driver
							.findElement(By.xpath("//div[text()=\"Search\"]/following::button[text()=\"OK\"][3]"));
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO, customerDetails);
					screenshot(driver, fetchMetadataVO, customerDetails);
					String scripNumber = fetchMetadataVO.getScriptNumber();

					xpath = xpath + ";" + "//a[contains(@id,\"STATUS\")][1]//span/span[contains(text(),\"Search\")]" + ";"
							+ "(//span[text()=\"Name\"]/following::input[@type=\"text\"])[3]" + ";"
							+ "//*[text()=\"Value\"]/following::div[normalize-space(text())=\"keysToSend\"][1]" + ";"
							+ "//div[text()=\"Search\"]/following::button[text()=\"OK\"][3]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
					logger.info("Sucessfully Clicked Status dropdownValues" + scripNumber);
					return;
				} else if (param2.equalsIgnoreCase("Req. Business Unit") || param2.equalsIgnoreCase("Client BU")) {
					WebElement search = driver.findElement(
							By.xpath("//a[contains(@id,\"paramsp\")][1]//span/span[contains(text(),\"Search\")]"));
					clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO, customerDetails);
					WebElement values = driver
							.findElement(By.xpath("(//span[text()=\"Name\"]/following::input[@type=\"text\"])[1]"));
					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
					WebElement select = driver.findElement(By
							.xpath("//*[text()=\"Value\"]/following::div[normalize-space(text())=\"" + keysToSend + "\"]"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO, customerDetails);
					WebElement searchok = driver
							.findElement(By.xpath("//div[text()=\"Search\"]/following::button[text()=\"OK\"]"));
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO, customerDetails);
					screenshot(driver, fetchMetadataVO, customerDetails);
					String scripNumber = fetchMetadataVO.getScriptNumber();

					xpath = xpath + ";" + "//a[contains(@id,\"paramsp\")][1]//span/span[contains(text(),\"Search\")]" + ";"
							+ "(//span[text()=\"Name\"]/following::input[@type=\"text\"])[1]" + ";"
							+ "//*[text()=\"Value\"]/following::div[normalize-space(text())=\"keysToSend\"]" + ";"
							+ "//div[text()=\"Search\"]/following::button[text()=\"OK\"]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
					logger.info("Sucessfully Clicked Req. Business Unit dropdownValues" + scripNumber);
					return;
				} else if (param2.equalsIgnoreCase("Legal Entity")) {
					Thread.sleep(2000);
					WebElement search = driver.findElement(
							By.xpath("(//a[contains(@id,\"paramsp\")][1]//span/span[contains(text(),\"Search\")])[1]"));
					clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO, customerDetails);
					WebElement values = driver
							.findElement(By.xpath("(//span[text()=\"Name\"]/following::input[@type=\"text\"])[1]"));
					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
					WebElement select = driver.findElement(By
							.xpath("//*[text()=\"Value\"]/following::div[normalize-space(text())=\"" + keysToSend + "\"]"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO, customerDetails);
					WebElement searchok = driver
							.findElement(By.xpath("//div[text()=\"Search\"]/following::button[text()=\"OK\"]"));
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO, customerDetails);
					screenshot(driver, fetchMetadataVO, customerDetails);
					String scripNumber = fetchMetadataVO.getScriptNumber();

					xpath = xpath + ";" + "(//a[contains(@id,\"paramsp\")][1]//span/span[contains(text(),\"Search\")])[1]"
							+ ";" + "(//span[text()=\"Name\"]/following::input[@type=\"text\"])[1]" + ";"
							+ "//*[text()=\"Value\"]/following::div[normalize-space(text())=\"keysToSend\"]" + ";"
							+ "//div[text()=\"Search\"]/following::button[text()=\"OK\"]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
					logger.info("Sucessfully Clicked Legal Entity dropdownValues" + scripNumber);
					return;
				} else if (param2.equalsIgnoreCase("Customer Name")) {
					Thread.sleep(1000);
					WebElement search = driver.findElement(
							By.xpath("(//a[contains(@id,\"CUSTOMER\")][1]//span/span[contains(text(),\"Search\")])[1]"));
					clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO, customerDetails);
					WebElement values = driver
							.findElement(By.xpath("(//span[text()=\"Name\"]/following::input[@type=\"text\"])[2]"));
					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
					WebElement select = driver.findElement(By.xpath(
							"//*[text()=\"Value\"]/following::div[normalize-space(text())=\"" + keysToSend + "\"][2]"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO, customerDetails);
					WebElement searchok = driver
							.findElement(By.xpath("//div[text()=\"Search\"]/following::button[text()=\"OK\"][2]"));
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO, customerDetails);
					screenshot(driver, fetchMetadataVO, customerDetails);
					String scripNumber = fetchMetadataVO.getScriptNumber();

					xpath = xpath + ";" + "(//a[contains(@id,\"CUSTOMER\")][1]//span/span[contains(text(),\"Search\")])[1]"
							+ ";" + "(//span[text()=\"Name\"]/following::input[@type=\"text\"])[2]" + ";"
							+ "//*[text()=\"Value\"]/following::div[normalize-space(text())=\"keysToSend\"][2]" + ";"
							+ "//div[text()=\"Search\"]/following::button[text()=\"OK\"][2]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
					logger.info("Sucessfully Clicked Customer Name dropdownValues" + scripNumber);
					return;
				} else if (param2.equalsIgnoreCase("Business Unit")) {
					if (keysToSend.equalsIgnoreCase("All")) {
						WebElement select = driver.findElement(By.xpath("//span[text()=\"" + param2
								+ "\"]/following::div[normalize-space(text())=\"" + keysToSend + "\"][1]"));
						clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO, customerDetails);
						String scripNumber = fetchMetadataVO.getScriptNumber();

						xpath = xpath + ";"
								+ "//span[text()=\"param2\"]/following::div[normalize-space(text())=\"keysToSend\"][1]";
						String scriptID = fetchMetadataVO.getScriptId();
						String lineNumber = fetchMetadataVO.getLineNumber();
						service.saveXpathParams(scriptID, lineNumber, xpath);
						logger.info("Sucessfully Clicked Business Unit dropdownValues" + scripNumber);
						return;
					} else {
						WebElement search = driver.findElement(
								By.xpath("//a[contains(@id,\"BU\")][1]//span/span[contains(text(),\"Search\")]"));
						clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO, customerDetails);
						WebElement values = driver
								.findElement(By.xpath("(//span[text()=\"Name\"]/following::input[@type=\"text\"])[1]"));
						typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
						enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
						WebElement select = driver.findElement(By.xpath(
								"//*[text()=\"Value\"]/following::div[normalize-space(text())=\"" + keysToSend + "\"]"));
						clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO, customerDetails);
						WebElement searchok = driver
								.findElement(By.xpath("//div[text()=\"Search\"]/following::button[text()=\"OK\"]"));
						clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO, customerDetails);
						Thread.sleep(10000);
						screenshot(driver, fetchMetadataVO, customerDetails);
						Thread.sleep(3000);
						String scripNumber = fetchMetadataVO.getScriptNumber();

						xpath = xpath + ";" + "//a[contains(@id,\"BU\")][1]//span/span[contains(text(),\"Search\")]" + ";"
								+ "(//span[text()=\"Name\"]/following::input[@type=\"text\"])[1]" + ";"
								+ "//*[text()=\"Value\"]/following::div[normalize-space(text())=\"keysToSend\"]" + ";"
								+ "//div[text()=\"Search\"]/following::button[text()=\"OK\"]";
						String scriptID = fetchMetadataVO.getScriptId();
						String lineNumber = fetchMetadataVO.getLineNumber();
						service.saveXpathParams(scriptID, lineNumber, xpath);
						logger.info("Sucessfully Clicked dropdownValues" + scripNumber);
						return;
					}
				} else if (param2.equalsIgnoreCase("Supplier Name")) {
					if (keysToSend.equalsIgnoreCase("All")) {
						WebElement select = driver.findElement(By.xpath("//span[text()=\"" + param2
								+ "\"]/following::div[normalize-space(text())=\"" + keysToSend + "\"][1]"));
						clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO, customerDetails);
						String scripNumber = fetchMetadataVO.getScriptNumber();

						xpath = xpath + ";"
								+ "//span[text()=\"param2\"]/following::div[normalize-space(text())=\"keysToSend\"][1]";
						String scriptID = fetchMetadataVO.getScriptId();
						String lineNumber = fetchMetadataVO.getLineNumber();
						service.saveXpathParams(scriptID, lineNumber, xpath);
						logger.info("Sucessfully Clicked Supplier Name dropdownValues" + scripNumber);
						return;
					} else {
						WebElement search = driver.findElement(
								By.xpath("//a[contains(@id,\"SUPPLIER\")][1]//span/span[contains(text(),\"Search\")]"));
						clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO, customerDetails);
						WebElement values = driver
								.findElement(By.xpath("(//span[text()=\"Name\"]/following::input[@type=\"text\"])[1]"));
						typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
						enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
						WebElement select = driver.findElement(
								By.xpath("//*[normalize-space(text())=\"Value\"]/following::div[normalize-space(text())=\""
										+ keysToSend + "\"]"));
						clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO, customerDetails);
						WebElement searchok = driver
								.findElement(By.xpath("//div[text()=\"Search\"]/following::button[text()=\"OK\"]"));
						clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO, customerDetails);
						Thread.sleep(10000);
						screenshot(driver, fetchMetadataVO, customerDetails);
						Thread.sleep(3000);
						String scripNumber = fetchMetadataVO.getScriptNumber();

						xpath = xpath + ";" + "//a[contains(@id,\"SUPPLIER\")][1]//span/span[contains(text(),\"Search\")]"
								+ ";" + "(//span[text()=\"Name\"]/following::input[@type=\"text\"])[1]" + ";"
								+ "//*[normalize-space(text())=\"Value\"]/following::div[normalize-space(text())=\"keysToSend\"]"
								+ ";" + "//div[text()=\"Search\"]/following::button[text()=\"OK\"]";
						String scriptID = fetchMetadataVO.getScriptId();
						String lineNumber = fetchMetadataVO.getLineNumber();
						service.saveXpathParams(scriptID, lineNumber, xpath);
						logger.info("Sucessfully Clicked dropdownValues" + scripNumber);
						return;
					}
				}
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  dropdownValues" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Basic Options") && param2.equalsIgnoreCase("Ledger")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"//*[normalize-space(text())=\"" + param1 + "\"]/following::label[normalize-space(text())=\""
								+ param2 + "\"]/following::a[contains(@title,\"" + param2 + "\")]")));
				WebElement waittext = driver
						.findElement(By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::label[text()=\""
								+ param2 + "\"]/following::a[contains(@title,\"" + param2 + "\")]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::label[text()=\"param2\"]/following::a[contains(@title,\"param2\")]";
				try {
					actions.click(waittext).build().perform();
					Thread.sleep(6000);
					WebElement popup1 = driver.findElement(By.xpath("//div[@class=\"AFDetectExpansion\"]"));
					WebElement search = driver.findElement(
							By.xpath("//div[@class=\"AFDetectExpansion\"]/following::a[contains(text(),\"Search\")][1]"));
					actions.moveToElement(search).build().perform();
					search.click();
					Thread.sleep(10000);
					wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
							"//div[contains(@id,\"PopupId::content\")]//*[text()=\"Search\"]/following::*[normalize-space(text())=\""
									+ param2 + "\"]/following::input[1]")));
					WebElement searchResult = driver.findElement(By.xpath(
							"//div[contains(@id,\"PopupId::content\")]//*[text()=\"Search\"]/following::*[normalize-space(text())=\""
									+ param2 + "\"]/following::input[1]"));
					typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
					Thread.sleep(5000);
					WebElement text = driver
							.findElement(By.xpath("(//span[contains(text(),\"" + keysToSend + "\")])[1]"));
					text.click();
					screenshot(driver, fetchMetadataVO, customerDetails);
					WebElement button = driver
							.findElement(By.xpath("//*[text()=\"Search\"]/following::*[normalize-space(text())=\"" + param2
									+ "\"]/following::*[text()=\"OK\"][1]"));
					button.click();
					String scripNumber = fetchMetadataVO.getScriptNumber();

					xpath = xpath + ";" + "//div[@class=\"AFDetectExpansion\"]" + ";"
							+ "//div[@class=\"AFDetectExpansion\"]/following::a[contains(text(),\"Search\")][1]" + ";"
							+ "//div[contains(@id,\"PopupId::content\")]//*[text()=\"Search\"]/following::*[normalize-space(text())=\"param2\"]/following::input[1]"
							+ ";" + "(//span[contains(text(),\"keysToSend\")])[1]" + ";"
							+ "//*[text()=\"Search\"]/following::*[normalize-space(text())=\"param2\"]/following::*[text()=\"OK\"][1]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
					logger.info("Sucessfully Clicked Basic Options or Ledger dropdownValues" + scripNumber);
				} catch (Exception e) {
					String scripNumber = fetchMetadataVO.getScriptNumber();
					logger.error("Failed during Basic Options or Ledger dropdownValues" + scripNumber);

					for (int i = 0; i <= 2; i++) {
						try {
							actions.click(waittext).build().perform();
							break;
						} finally {

						}
					}
				}
				try {
					Thread.sleep(6000);
					WebElement popup1 = driver.findElement(By.xpath("//div[@class=\"AFDetectExpansion\"]"));
					WebElement search = driver.findElement(
							By.xpath("//div[@class=\"AFDetectExpansion\"]/following::a[contains(text(),\"Search\")][1]"));
					actions.moveToElement(search).build().perform();
					search.click();
					Thread.sleep(10000);
					wait.until(ExpectedConditions.presenceOfElementLocated(
							By.xpath("//div[contains(@id,\"PopupId::content\")]//*[text()=\"Search\"]/following::*[text()=\""
									+ param2 + "\"]/following::input[1]")));
					WebElement searchResult = driver.findElement(
							By.xpath("//div[contains(@id,\"PopupId::content\")]//*[text()=\"Search\"]/following::*[text()=\""
									+ param2 + "\"]/following::input[1]"));
					typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
					Thread.sleep(5000);
					WebElement text = driver.findElement(By.xpath("//span[text()=\"" + param2
							+ "\"]/following::span[contains(text(),\"" + keysToSend + "\")][1]"));
					text.click();
					screenshot(driver, fetchMetadataVO, customerDetails);
					WebElement button = driver
							.findElement(By.xpath("//*[text()=\"Search\"]/following::*[normalize-space(text())=\"" + param2
									+ "\"]/following::*[text()=\"OK\"][1]"));
					button.click();
					String scripNumber = fetchMetadataVO.getScriptNumber();
					xpath = xpath + ";" + "//div[@class=\"AFDetectExpansion\"]" + ";"
							+ "//div[@class=\"AFDetectExpansion\"]/following::a[contains(text(),\"Search\")][1]" + ";"
							+ "//div[contains(@id,\"PopupId::content\")]//*[text()=\"Search\"]/following::*[normalize-space(text())=\"param2\"]/following::input[1]"
							+ ";" + "//span[text()=\"param2\"]/following::span[contains(text(),\"keysToSend\")][1]" + ";"
							+ "//*[text()=\"Search\"]/following::*[normalize-space(text())=\"param2\"]/following::*[text()=\"OK\"][1]";

					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
					logger.info("Sucessfully Clicked dropdownValues" + scripNumber);
				} catch (Exception e) {
					String scripNumber = fetchMetadataVO.getScriptNumber();
					logger.error("Failed during  dropdownValues" + scripNumber);

					for (int i = 0; i <= 2; i++) {
						try {
							actions.click(waittext).build().perform();
							break;
						} finally {
							Thread.sleep(4000);
							WebElement popup1 = driver.findElement(By.xpath("//div[@class=\"AFDetectExpansion\"]"));
							WebElement search = driver.findElement(By.xpath(
									"//div[@class=\"AFDetectExpansion\"]/following::a[contains(text(),\"Search\")][1]"));
							actions.moveToElement(search).build().perform();
							search.click();
							Thread.sleep(10000);
							wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
									"//div[contains(@id,\"PopupId::content\")]//*[text()=\"Search\"]/following::*[normalize-space(text())=\""
											+ param2 + "\"]/following::input[1]")));
							WebElement searchResult = driver.findElement(By.xpath(
									"//div[contains(@id,\"PopupId::content\")]//*[text()=\"Search\"]/following::*[normalize-space(text())=\""
											+ param2 + "\"]/following::input[1]"));
							typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
							enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
							Thread.sleep(5000);
							WebElement text = driver
									.findElement(By.xpath("(//span[contains(text(),\"" + keysToSend + "\")])[1]"));
							text.click();
							screenshot(driver, fetchMetadataVO, customerDetails);
							WebElement button = driver.findElement(By.xpath("//*[text()=\"Search\"]/following::*[text()=\""
									+ param2 + "\"]/following::*[text()=\"OK\"][1]"));
							button.click();

							xpath = xpath + ";" + "//div[@class=\"AFDetectExpansion\"]" + ";"
									+ "//div[@class=\"AFDetectExpansion\"]/following::a[contains(text(),\"Search\")][1]"
									+ ";"
									+ "//div[contains(@id,\"PopupId::content\")]//*[text()=\"Search\"]/following::*[normalize-space(text())=\"param2\"]/following::input[1]"
									+ ";" + "(//span[contains(text(),\"keysToSend\")])[1]" + ";"
									+ "//*[text()=\"Search\"]/following::*[text()=\"param2\"]/following::*[text()=\"OK\"][1]";
							String scriptID = fetchMetadataVO.getScriptId();
							String lineNumber = fetchMetadataVO.getLineNumber();
							service.saveXpathParams(scriptID, lineNumber, xpath);

						}
					}

				}
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  dropdownValues" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]/following::a[1]")));
			WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]/following::a[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			String xpath = "//*[normalize-space(text())=\"param1\"]/following::label[normalize-space(text())=\"param2\"]/following::a[1]";
			try {
				actions.click(waittext).build().perform();
				Thread.sleep(10000);
				WebElement popup1 = driver.findElement(By.xpath("//div[contains(@id,\"suggestions-popup\")]"));
				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				actions.release();
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				xpath = xpath + ";" + "//div[contains(@id,\"suggestions-popup\")]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked dropdownValues" + scripNumber);
			} catch (Exception ex) {
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.error("Failed during  dropdownValues" + scripNumber);

				try {
					try {
						WebElement popup1 = driver.findElement(By.xpath("//div[@class=\"AFDetectExpansion\"]"));
						dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO,
								customerDetails);
						actions.release();
						screenshot(driver, fetchMetadataVO, customerDetails);
						// String xpath="//div[@class=\"AFDetectExpansion\"]";
						xpath = xpath + ";" + "//div[@class=\"AFDetectExpansion\"]";
						String scriptID = fetchMetadataVO.getScriptId();
						String lineNumber = fetchMetadataVO.getLineNumber();
						service.saveXpathParams(scriptID, lineNumber, xpath);
					} catch (Exception ex1) {
						for (int i = 0; i <= 2; i++) {
							actions.click(waittext).build().perform();
							break;
						}
						Thread.sleep(3000);
						WebElement popup1 = driver.findElement(By.xpath("//div[contains(@id,\"suggestions-popup\")]"));
						dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO,
								customerDetails);
						actions.release();
						screenshot(driver, fetchMetadataVO, customerDetails);
						xpath = xpath + ";" + "//div[contains(@id,\"suggestions-popup\")]";
						String scriptID = fetchMetadataVO.getScriptId();
						String lineNumber = fetchMetadataVO.getLineNumber();
						service.saveXpathParams(scriptID, lineNumber, xpath);
					}
				} catch (Exception ex2) {
					WebElement popup1 = driver.findElement(By.xpath("//div[@class=\"AFDetectExpansion\"]"));
					dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
					actions.release();
					screenshot(driver, fetchMetadataVO, customerDetails);
					xpath = xpath + ";" + "//div[@class=\"AFDetectExpansion\"]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
				}
			}
			return;
		} catch (Exception exe) {
			logger.error(exe.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  dropdownValues" + scripNumber);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]/following::a[1]")));
			WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::label[text()=\"" + param2 + "\"]/following::a[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			String xpath = "//*[normalize-space(text())=\"param1\"]/following::label[text()=\"param2\"]/following::a[1]";
			try {
				actions.clickAndHold(waittext).build().perform();
				Thread.sleep(6000);
				WebElement popup1 = driver.findElement(By.xpath("//div[contains(@id,\"dropdownPopup::content\")]"));

				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				actions.release();
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				xpath = xpath + ";" + "//div[contains(@id,\"dropdownPopup::content\")]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked dropdownValues" + scripNumber);
			} catch (Exception e) {
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.error("Failed during  dropdownValues" + scripNumber);
				for (int i = 0; i <= 2; i++) {
					try {
						actions.click(waittext).build().perform();
						break;
					} finally {
						dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO,
								customerDetails);
						actions.release();
						screenshot(driver, fetchMetadataVO, customerDetails);
						String scriptID = fetchMetadataVO.getScriptId();
						String lineNumber = fetchMetadataVO.getLineNumber();
						service.saveXpathParams(scriptID, lineNumber, xpath);
					}
				}

			}
			try {
				actions.click(waittext).build().perform();
				Thread.sleep(6000);
				WebElement popup1 = driver.findElement(By.xpath("//div[contains(@id,\"dropdownPopup::content\")][1]"));
				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				actions.release();
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				xpath = xpath + ";" + "//div[contains(@id,\"dropdownPopup::content\")][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked dropdownValues" + scripNumber);
			} catch (Exception e) {
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.error("Failed during  dropdownValues" + scripNumber);
				for (int i = 0; i <= 2; i++) {
					try {
						actions.click(waittext).build().perform();
						break;
					} finally {
						dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO,
								customerDetails);
						screenshot(driver, fetchMetadataVO, customerDetails);
						String scriptID = fetchMetadataVO.getScriptId();
						String lineNumber = fetchMetadataVO.getLineNumber();
						service.saveXpathParams(scriptID, lineNumber, xpath);
					}
				}

			}
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  dropdownValues" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//label[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::a[1]")));
			WebElement waittext = driver.findElement(By.xpath("//label[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::a[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();

			String xpath = "//label[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]/following::a[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked dropdownValues" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  dropdownValues" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("(//div[contains(@id,\"popup-container\")]//*[normalize-space(text())=\"" + param1
							+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::a)[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//div[contains(@id,\"popup-container\")]//*[text()=\""
					+ param1 + "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::a)[1]"));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();

			String xpath = "(//div[contains(@id,\"popup-container\")]//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]/following::a)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked dropdownValues" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  dropdownValues" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::a)[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::a)[1]"));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();

			String xpath = "(//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]/following::a)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked dropdownValues" + scripNumber);
			return;
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked dropdownValues" + scripNumber);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(text(),\"Search\")]")));
			WebElement search = driver.findElement(By.xpath("//a[contains(text(),\"Search\")]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(search).build().perform();
			search.click();
			Thread.sleep(2000);
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\"" + param2
							+ "\"]/following::input[1]")));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(
					"//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\"" + param2 + "\"]"),
					param2));
			WebElement searchResult = driver
					.findElement(By.xpath("//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\""
							+ param2 + "\"]/following::input[1]"));
			typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
			String xpath1 = "//a[contains(text(),\"Search\")]";
			String xpath2 = "//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\"param2\"]/following::input[1]";
			String xpath = xpath1 + ";" + xpath2;
			if (keysToSend != null) {
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
				WebElement text = driver
						.findElement(By.xpath("(//span[normalize-space(text())=\"" + keysToSend + "\"])[1]"));
				text.click();
				xpath = xpath + ";" + "(//span[normalize-space(text())=\"keysToSend\"])[1]";
			}
			WebElement button = driver
					.findElement(By.xpath("//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\""
							+ param2 + "\"]/following::*[text()=\"K\"][1]"));
			button.click();
			String scripNumber = fetchMetadataVO.getScriptNumber();

			xpath = xpath + ";"
					+ "//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\"param2\"]/following::*[text()=\"K\"][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked dropdownValues" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  dropdownValues" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\"Name\"]/following::input[1]")));
			WebElement searchResult = driver.findElement(By.xpath(
					"//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\"Name\"]/following::input[1]"));
			typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
			enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(5000);
			WebElement text = driver.findElement(By.xpath("//span[normalize-space(text())=\"" + keysToSend + "\"]"));
			text.click();
			Thread.sleep(1000);
			WebElement button = driver.findElement(By.xpath(
					"//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\"Name\"]/following::*[text()=\"OK\"][1]"));
			button.click();
			String scripNumber = fetchMetadataVO.getScriptNumber();

			String xpath = "//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\"Name\"]/following::input[1]"
					+ ";" + "//span[normalize-space(text())=\"keysToSend\"]" + ";"
					+ "//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\"Name\"]/following::*[text()=\"OK\"][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked dropdownValues" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  dropdownValues" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebElement button = driver
					.findElement(By.xpath("//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\""
							+ param2 + "\"]/following::*[normalize-space(text())=\"OK\"][1]"));
			button.click();
			String scripNumber = fetchMetadataVO.getScriptNumber();

			String xpath = "//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\"param2\"]/following::*[normalize-space(text())=\"OK\"][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked dropdownValues" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();

			logger.error("Failed during  dropdownValues" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[contains(text(),\"" + param1
					+ "\")]/following::label[normalize-space(text())=\"" + keysToSend + "\"]/following::input)[1]")));
			Thread.sleep(1000);
			wait.until(
					ExpectedConditions.textToBePresentInElementLocated(
							By.xpath("//h1[contains(text(),\"" + param1
									+ "\")]/following::label[normalize-space(text())=\"" + keysToSend + "\"]"),
							keysToSend));
			WebElement waittill = driver.findElement(By.xpath("//h1[contains(text(),\"" + param1
					+ "\")]/following::label[normalize-space(text())=\"" + keysToSend + "\"]/following::input[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			Thread.sleep(500);
			String scripNumber = fetchMetadataVO.getScriptNumber();

			String xpath = "(//h1[contains(text(),\"param1\")]/following::label[normalize-space(text())=\"keysToSend\"]/following::input)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked dropdownValues" + scripNumber);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  dropdownValues" + scripNumber);
			logger.error(e.getMessage());
		}
		
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
			"//*[contains(@id,\"popup-container\")]//*[text()=\"" + param1 + "\"]/following::*[text()=\"" + param2 + "\"]/following::input[not (@type=\"hidden\")][1]")));
			WebElement waittext = driver.findElement(By.xpath(
			"//*[contains(@id,\"popup-container\")]//*[text()=\"" + param1 + "\"]/following::*[text()=\"" + param2 + "\"]/following::input[not (@type=\"hidden\")][1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			waittext.click();
			Thread.sleep(4000);
			WebElement selectvalue = driver.findElement(By.xpath("//*[text()=\"" + keysToSend + "\"][1]"));
			actions.moveToElement(selectvalue).build().perform();
			selectvalue.click();
			Thread.sleep(500);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[contains(@id,\"popup-container\")]//*[text()=\"param1\"]/following::*[text()=\"param2\"]/following::input[not (@type=\"hidden\")][1]"+";"+"//*[text()=\"keysToSend\"][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked Schedule New Process or Name dropdownValues" + scripNumber);
			return;
			} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Event Type dropdownValues" + scripNumber);
			logger.error(e.getMessage());
			}

		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(@id,\"" + param1 + "\")]")));
			Thread.sleep(1000);
//wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//a[contains(@id,\"drop\")]"), keysToSend));
			WebElement waittill = driver.findElement(By.xpath("//a[contains(@id,\"" + param1 + "\")]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittill, fetchConfigVO, customerDetails);
			dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(500);
			String scripNumber = fetchMetadataVO.getScriptNumber();

			String xpath = "//a[contains(@id,\"param1\")]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked dropdownValues" + scripNumber);
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  dropdownValues" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	private void clickValidateXpath(WebDriver driver, ScriptDetailsDto fetchMetadataVO, WebElement waittext,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click();", waittext);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked clickValidateXpath" + scripNumber);
			// waittext.click();
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  clickValidateXpath" + scripNumber);
			e.printStackTrace();
		}
	}

	public void clickFilter(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		// Dh changes 6
		try {
			if (param1.equalsIgnoreCase("Review Distributions")) {
				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1
						+ "\")]/following::*[text()=\"Account Class\"]//preceding::input[contains(@id,\"Filter\")])[3]"));

				waittill.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				String xpath = "(//*[contains(text(),\"param1\")]/following::*[text()=\"Account Class\"]//preceding::input[contains(@id,\"Filter\")])[3]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickFilter" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
		}

		try {
			WebElement waittill = driver.findElement(By.xpath("(//img[@title=\"" + param1 + "\"]/following::*[text()=\""
					+ param2 + "\"]/preceding::input[@type=\"text\"])[3]"));

			waittill.click();
			screenshot(driver, fetchMetadataVO, customerDetails);
			String xpath = "(//img[@title=\"param1\"]/following::*[text()=\"param2\"]/preceding::input[@type=\"text\"])[3]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  clickFilter" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("(//img[@title=\"" + param1 + "\"])[1]"));

			waittill.click();
			Thread.sleep(2000);
			WebElement waittill1 = driver.findElement(By.xpath("(//img[@title=\"" + param1 + "\"]/following::*[text()=\""
					+ param2 + "\"]/preceding::input[@type=\"text\"])[3]"));
			waittill1.click();
			Thread.sleep(2000);
			String xpath = "(//img[@title=\"param1\"])[1]" + ";"
					+ "(//img[@title=\"param1\"]/following::*[text()=\"param2\"]/preceding::input[@type=\"text\"])[3]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  clickFilter" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			// throw e;
		}
		// DH Changes 6
		try {
			WebElement waittill = driver.findElement(By.xpath("(//img[@title=\"" + param2 + "\"])[1]"));

			waittill.click();
			Thread.sleep(3000);
			// WebElement waittill1 = driver.findElement(By.xpath("(//img[@title=\"" + param1
			// + "\"]/following::*[text()=\""
			// + param2 + "\"]/preceding::input[@type=\"text\"])[3]"));
			// waittill1.click();
			// Thread.sleep(2000);
			String xpath = "(//img[@title=\"param2\"])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  clickFilter" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public String password(WebDriver driver, String inputParam, String keysToSend, FetchConfigVO fetchConfigVO,
			ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails) {
		try {
			WebElement waittill = driver.findElement(By.xpath("//*[contains(@placeholder,\"" + inputParam + "\")]"));
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked password" + scripNumber);
			String xpath = "//*[contains(@placeholder,\"inputParam\")]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return keysToSend;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  password" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			logger.error(e.getMessage());
			throw e;
		}
	}

	public void typeIntoValidxpath(WebDriver driver, String keysToSend, WebElement waittill,
			FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO) {
		try {
			waittill.clear();
			waittill.click();
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("arguments[0].value=\"" + keysToSend + "\";", waittill);
			logger.info("clear and typed the given Data");
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked typeIntoValidxpath" + scripNumber);

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  typeIntoValidxpath" + scripNumber);
			e.printStackTrace();
		}
	}

	public void clearMethod(WebDriver driver, WebElement waittill) {
//		WebDriverWait wait = new WebDriverWait(driver, 60);
		Duration timeoutDuration = Duration.ofSeconds(60);
		WebDriverWait wait = new WebDriverWait(driver, timeoutDuration);
		wait.until(ExpectedConditions.elementToBeClickable(waittill));
		waittill.click();
		waittill.clear();
		logger.info("clear and typed the given Data");
	}

	public void moveToElement(WebDriver driver, String inputParam, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO) {
		WebElement waittill = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + inputParam + "\"][1]"));
		Actions actions = new Actions(driver);
		actions.moveToElement(waittill).build().perform();
		String scripNumber = fetchMetadataVO.getScriptNumber();
		logger.info("Sucessfully Clicked moveToElement" + scripNumber);
		String xpath = "//*[normalize-space(text())=\"inputParam\"][1]";
		String scriptID = fetchMetadataVO.getScriptId();
		String lineNumber = fetchMetadataVO.getLineNumber();
		service.saveXpathParams(scriptID, lineNumber, xpath);
	}

	public void scrollUsingElement(WebDriver driver, String inputParam, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {
		try {
			Thread.sleep(2000);
			WebElement waittill = driver
					.findElement(By.xpath("//span[normalize-space(text())=\"" + inputParam + "\"][1]"));
			// ((JavascriptExecutor)driver).executeScript("document.body.style.zoom=\"50%\";");
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			// ((JavascriptExecutor)driver).executeScript("document.body.style.zoom=\"100%\";");
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//span[normalize-space(text())=\"inputParam\"][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  scrollUsingElement" + scripNumber);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("//a[normalize-space(text())=\"" + inputParam + "\"]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//a[normalize-space(text())=\"inputParam\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  scrollUsingElement" + scripNumber);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("//h1[normalize-space(text())=\"" + inputParam + "\"]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//h1[normalize-space(text())=\"inputParam\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Field during scrollUsingElement" + scripNumber);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("(//h2[normalize-space(text())=\"" + inputParam + "\"])"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//h2[normalize-space(text())=\"inputParam\"])";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  scrollUsingElement" + scripNumber);
		}
		try {
			WebElement waittill = driver
					.findElement(By.xpath("(//h3[normalize-space(text())=\"" + inputParam + "\"])[2]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//h3[normalize-space(text())=\"inputParam\"])[2]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
			return;
		} catch (Exception e) {
			logger.error("Failed during  scrollUsingElement" + e.getMessage());
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("//td[normalize-space(text())=\"" + inputParam + "\"]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//td[normalize-space(text())=\"inputParam\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
			return;
		} catch (Exception e) {
			logger.error("Failed during  scrollUsingElement" + e.getMessage());
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("//div[contains(text(),\"" + inputParam + "\")]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//div[contains(text(),\"inputParam\")]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  scrollUsingElement" + scripNumber);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("(//table[@summary=\"" + inputParam + "\"]//td//a)[1]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//table[@summary=\"inputParam\"]//td//a)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  scrollUsingElement" + scripNumber);
		}
		try {
			WebElement waittill = driver.findElement(
					By.xpath("(//label[normalize-space(text())=\"" + inputParam + "\"]/following::input)[1]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//label[normalize-space(text())=\"inputParam\"]/following::input)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
			return;
		} catch (Exception e) {
			logger.error("Failed during  scrollUsingElement" + e.getMessage());
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("//a[contains(@id,\"" + inputParam + "\")]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//a[contains(@id,\"inputParam\")]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  scrollUsingElement" + scripNumber);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("//li[normalize-space(text())=\"" + inputParam + "\"]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//li[normalize-space(text())=\"inputParam\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  scrollUsingElement" + scripNumber);
		}
		try {
			WebElement waittill = driver
					.findElement(By.xpath("//label[normalize-space(text())=\"" + inputParam + "\"]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//label[normalize-space(text())=\"inputParam\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  scrollUsingElement" + scripNumber);
		}
		try {
			WebElement waittill = driver
					.findElement(By.xpath("//button[normalize-space(text())=\"" + inputParam + "\"]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//button[normalize-space(text())=\"inputParam\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  scrollUsingElement" + scripNumber);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("//img[@title=\"" + inputParam + "\"]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//img[@title=\"inputParam\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  scrollUsingElement" + scripNumber);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("(//*[@title=\"" + inputParam + "\"])[1]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//*[@title=\"inputParam\"])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  scrollUsingElement" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			e.printStackTrace();
			throw e;
		}
	}

	private void scrollMethod(WebDriver driver, FetchConfigVO fetchConfigVO, WebElement waittill,
			ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails) {
		fetchConfigVO.getMEDIUM_WAIT();
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		// WebElement elements =
		// wait.until(ExpectedConditions.elementToBeClickable(waittill));
		WebElement element = waittill;
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
		screenshot(driver, fetchMetadataVO, customerDetails);
		String scripNumber = fetchMetadataVO.getScriptNumber();
		logger.info("Sucessfully Clicked scrollMethod" + scripNumber);
	}

	public void tab(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) throws Exception {
		try {
			Thread.sleep(4000);
			Actions action = new Actions(driver);
			action.sendKeys(Keys.TAB).build().perform();
			Thread.sleep(8000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked tab" + scripNumber);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  tab" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			e.printStackTrace();
			throw e;
		}
	}

	public void mousehover(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			Actions actions = new Actions(driver);
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("(//table[@summary=\"" + param1 + "\"]//tr[1]/following::a)[2]")));
			scrollUsingElement(driver, param1, fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(6000);
			WebElement waittext = driver
					.findElement(By.xpath("(//table[@summary=\"" + param1 + "\"]//tr[1]/following::a)[2]"));
			actions.moveToElement(waittext).build().perform();
			clickImage(driver, param2, param1, fetchMetadataVO, fetchConfigVO, customerDetails);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//table[@summary=\"param1\"]//tr[1]/following::a)[2]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked mousehover" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  mousehover" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			Actions actions = new Actions(driver);
			WebElement waittill = driver.findElement(By.xpath(
					"(//table[@role=\"presentation\"]/following::a[normalize-space(text())=\"" + param1 + "\"])[1]"));
			actions.moveToElement(waittill).build().perform();
			Thread.sleep(5000);
			System.out.print("Successfully executed Mousehover");
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//table[@role=\"presentation\"]/following::a[normalize-space(text())=\"param1\"])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked mousehover" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  mousehover" + scripNumber);
			logger.error(e.getMessage());
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void enter(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) throws Exception {
		try {
			Thread.sleep(2000);
			Actions actionObject = new Actions(driver);
			actionObject.sendKeys(Keys.ENTER).build().perform();
			Thread.sleep(8000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked enter" + scripNumber);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  enter" + scripNumber);
			logger.error(e.getMessage());
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void deleteAllCookies(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) {
		try {
			driver.manage().deleteAllCookies();
			logger.info("Successfully Deleted All The Cookies.");
		} catch (Exception e) {
			logger.error("Failed To Delete All The Cookies.");
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void selectCheckBox(WebDriver driver, String xpath, ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails) {
		try {
			WebElement element = driver.findElement(By.xpath(xpath));
			if (element.isSelected()) {
			} else {
				element.click();
			}
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked selectCheckBox" + scripNumber);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during selectCheckBox" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			e.printStackTrace();
			throw e;
		}
	}

	public void selectByText(WebDriver driver, String param1, String param2, String inputData,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {

		try {
			if (param1.equalsIgnoreCase("Basic Options") && param2.equalsIgnoreCase("Book")) {
				Thread.sleep(5000);
				WebElement waittext = driver.findElement(By.xpath(("//*[text()=\"" + param1 + "\"]//following::*[text()=\"" + param2 + "\"]//following::select[1]")));
				selectMethod(driver, inputData, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[text()='Basic Options']//following::*[text()='Book']//following::select[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked Release selectByText" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Release selectByText" + scripNumber);
		}
		
		try {
			if (param1.equalsIgnoreCase("App")) {
				Thread.sleep(5000);
				WebElement waittext = driver.findElement(By.xpath(("//*[text()=\"" + param1 + "\"]/following::input[1]")));
				selectMethod(driver, inputData, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Release selectByText" + scripNumber);
				String xpath = "//*[text()='App']/following::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Release selectByText" + scripNumber);
		}
		
		try {
			if (param1.equalsIgnoreCase("Account")) {
				Thread.sleep(5000);
				WebElement waittext = driver.findElement(By.xpath(("(//label[text()=\"" + param1 + "\"])[1]//preceding::input[1]")));
				selectMethod(driver, inputData, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Release selectByText" + scripNumber);
				String xpath = "(//label[text()='Account'])[1]//preceding::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Release selectByText" + scripNumber);
		}
    
		try {
			if (param1.equalsIgnoreCase("Address Purposes")) {
				Thread.sleep(2000);
				WebElement waittext = driver.findElement(
						By.xpath(("//*[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
								+ param2 + "\"]/following::select[not (@title)]")));
				selectMethod(driver, inputData, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]/following::select[not (@title)]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked selectByText" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during selectByText" + scripNumber);
		}
		try {
			if (param1.equalsIgnoreCase("Holds")) {
				Thread.sleep(2000);
				WebElement waittext = driver.findElement(
						By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
								+ param2 + "\"]/preceding-sibling::select[@title=\"\"]"));
				selectMethod(driver, inputData, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]/preceding-sibling::select[@title=\"\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked Holds selectByText" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Holds selectByText" + scripNumber);
		}
		try {
			if (param2.equalsIgnoreCase("Batch Status")) {
				Thread.sleep(2000);
				WebElement waittext = driver.findElement(By.xpath(
						("//*[normalize-space(text())=\"" + param1 + "\"]/following::label[normalize-space(text())=\""
								+ param2 + "\"]/preceding-sibling::select[1]")));
				selectMethod(driver, inputData, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::label[normalize-space(text())=\"param2\"]/preceding-sibling::select[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked Batch Status selectByText" + scripNumber);
				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Batch Status selectByText" + scripNumber);
		}
		try {
			if (param1.equalsIgnoreCase("Release") && param2.equalsIgnoreCase("Name")) {
				Thread.sleep(5000);
				WebElement waittext = driver.findElement(By.xpath(
						("(//*[normalize-space(text())=\"" + param1 + "\"]/following::label[normalize-space(text())=\""
								+ param2 + "\"]/preceding-sibling::select)[2]")));
				selectMethod(driver, inputData, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "(//*[normalize-space(text())=\"param1\"]/following::label[normalize-space(text())=\"param2\"]/preceding-sibling::select)[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked Release selectByText" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Release selectByText" + scripNumber);
		}
		try {
			Thread.sleep(2000);
			WebElement waittext = driver.findElement(By.xpath(("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]/following::select[1]")));
			selectMethod(driver, inputData, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked selectByText" + scripNumber);

			String xpath = "//*[normalize-space(text())=\"param1\"]/following::label[normalize-space(text())=\"param2\"]/following::select[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during selectByText" + scripNumber);
		}
		try {
			Thread.sleep(2000);
			WebElement waittext = driver.findElement(By.xpath(("//*[contains(text(),\"" + param1
					+ "\")]/following::label[normalize-space(text())=\"" + param2 + "\"]/preceding::select[1]")));
			selectMethod(driver, inputData, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();

			String xpath = "//*[contains(text(),\"param1\")]/following::label[normalize-space(text())=\"param2\"]/preceding::select[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked selectByText" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during selectByText" + scripNumber);
		}
		try {
			Thread.sleep(2000);
			WebElement waittext = driver.findElement(By.xpath(("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]/preceding::select[1]")));
			selectMethod(driver, inputData, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();

			String xpath = "//*[normalize-space(text())=\"param1\"]/following::label[normalize-space(text())=\"param2\"]/preceding::select[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked selectByText" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during selectByText" + scripNumber);
		}
		try {
			Thread.sleep(2000);
			WebElement waittext = driver.findElement(By.xpath(("//*[contains(text(),\"" + param1
					+ "\")]/following::label[normalize-space(text())=\"" + param2 + "\"]/following::select[1]")));
			selectMethod(driver, inputData, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();

			String xpath = "//*[contains(text(),\"param1\")]/following::label[normalize-space(text())=\"param2\"]/following::select[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked selectByText" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during selectByText" + scripNumber);
		}
		try {
			if (param2 == "") {
				WebElement waittext = driver
						.findElement(By.xpath(("//*[contains(text(),\"" + param1 + "\")]/following::select[1]")));
				selectMethod(driver, inputData, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "//*[contains(text(),\"param1\")]/following::select[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked selectCheckBox" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during selectByText" + scripNumber);
		}
		try {
			WebElement waittext = driver
					.findElement(By.xpath(("//*[contains(text(),\"" + param1 + "\")]/preceding-sibling::select[1]")));
			selectMethod(driver, inputData, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();

			String xpath = "//*[contains(text(),\"param1\")]/preceding-sibling::select[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Sucessfully Clicked selectByText" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during selectByText" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	private void selectMethod(WebDriver driver, String inputData, ScriptDetailsDto fetchMetadataVO, WebElement waittext,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {
		Actions actions = new Actions(driver);
		actions.moveToElement(waittext).build().perform();
		Select selectBox = new Select(waittext);
		selectBox.selectByVisibleText(inputData);
		String scripNumber = fetchMetadataVO.getScriptNumber();
		logger.info("Sucessfully Clicked selectMethod" + scripNumber);
		screenshot(driver, fetchMetadataVO, customerDetails);
		return;
	}

	public void selectByValue(WebDriver driver, String xpath, String inputData, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {
		try {
			WebElement webElement = driver.findElement(By.xpath(xpath));
			Select selectBox = new Select(webElement);
			selectBox.selectByValue(inputData);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked selectByValue" + scripNumber);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during selectByValue" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			e.printStackTrace();
			throw e;
		}
	}

	public void switchToDefaultFrame(WebDriver driver) {
		try {
			driver.switchTo().defaultContent();
			logger.info("Successfully switched to default Frame");
		} catch (Exception e) {
			logger.error("Failed During Switching to default Action");
			throw e;
		}
	}

	// DH 31
	private String copyNumbers(String value) {
		Pattern p = Pattern.compile("(\\b[Payment]+\\s[\\d]+)"); // the pattern to search for
		Matcher m = p.matcher(value);
		String theGroup = null;
		// if we find a match, get the group
		if (m.find()) {
			// we\"re only looking for one group, so get it
			theGroup = m.group(1);
			theGroup = theGroup.replaceAll("\\b\\w+(?<!\\w[\\d@]\\b)\\b", "");
			theGroup = theGroup.replaceAll(" ", "");
			// print the group out for verification
			System.out.format(theGroup);
		}
		return theGroup;
	}
	
	private String copyunderscorewithnumber(String value) {
		try {
			Pattern p = Pattern.compile("\\b[A-Z]+\\_[0-9]+\\d+"); // the pattern to search for \b[IBAA]+\-[A-Z]+\-\d+
																	// (\\b[Payment]+\\s[\\d]+)
			Matcher m = p.matcher(value);
			// if we find a match, get the group
			String theGroup = null;
			if (m.find()) {
				// we\"re only looking for one group, so get it
				theGroup = m.group(0);
				// theGroup = theGroup.replaceAll("\\b\\w+(?<!\\w[\\d@]\\b)\\b", "");
				// System.out.println(theGroup);
				theGroup = theGroup.replaceAll(" ", "");
				System.out.format(theGroup);
			}
			return theGroup;
		} catch (Exception e) {
			logger.error("Failed during  copy under score with number " + e.getMessage());
			throw e;
		}

	}

	private String copyunderscore(String value) {
		try {
			Pattern p = Pattern.compile("\\b[A-Z]+\\_[A-Z]+\\d+"); // the pattern to search for \b[IBAA]+\-[A-Z]+\-\d+
																	// (\\b[Payment]+\\s[\\d]+)
			Matcher m = p.matcher(value);
			// if we find a match, get the group
			String theGroup = null;
			if (m.find()) {
				// we\"re only looking for one group, so get it
				theGroup = m.group(0);
				// theGroup = theGroup.replaceAll("\\b\\w+(?<!\\w[\\d@]\\b)\\b", "");
				// System.out.println(theGroup);
				theGroup = theGroup.replaceAll(" ", "");
			}
			return theGroup;
		} catch (Exception e) {
			logger.error("Failed during  copy under score " + e.getMessage());
			throw e;
		}

	}

	public String copynumber(WebDriver driver, String inputParam1, String inputParam2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {

		String value = null;
		
		try {

			if (inputParam1.equalsIgnoreCase("Confirmation")||(inputParam2.equalsIgnoreCase("supply request"))) {

				Thread.sleep(5000);

				WebElement webElement = driver
						.findElement(By.xpath("//td[normalize-space(text())=\""+inputParam1+"\"]/following::*[contains(text(),\""+inputParam2+"\")][1]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(webElement).build().perform();
				String stringToSearch = webElement.getText();
				value = copyunderscorewithnumber(stringToSearch);

				// value = copyValuesWithSpc(webElement);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//td[normalize-space(text())=\"inputParam1\"]/following::*[contains(text(),\"inputParam2\")][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				String testParamId = fetchMetadataVO.getTestScriptParamId();
				String testSetId = fetchMetadataVO.getTestSetLineId();
				dynamicnumber.saveCopyNumber(value, testParamId, testSetId);
				logger.info("Sucessfully Clicked copynumber" + scripNumber);

				return value;

			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during copynumber" + scripNumber);

		}
		
		try {

			if (inputParam1.equalsIgnoreCase("Account Number")) {
				Thread.sleep(5000);
				WebElement webElement = driver.findElement(By.xpath("//*[text()=\"" + inputParam1 + "\"]/following::input[1]"));
				Actions actions = new Actions(driver);

				actions.moveToElement(webElement).build().perform();
				String stringToSearch = webElement.getAttribute("value");
//			value = copyValuesWithSpc(stringToSearch);
// value = copyValuesWithSpc(webElement);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[text()=\"inputParam1\"]/following::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				String testParamId = fetchMetadataVO.getTestScriptParamId();
				String testSetId = fetchMetadataVO.getTestSetLineId();
				dynamicnumber.saveCopyNumber(stringToSearch, testParamId, testSetId);
				logger.info("Sucessfully Clicked copynumber" + scripNumber);

				return value;

			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during copynumber" + scripNumber);
		}

		// Dh 611
		try {

			if (inputParam1.equalsIgnoreCase("Number")) {

				Thread.sleep(5000);

				WebElement webElement = driver
						.findElement(By.xpath("//label[text()=\"" + inputParam1 + "\"]/following::a[1]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(webElement).build().perform();
				String stringToSearch = webElement.getText();
				value = copyValuesWithSpc(stringToSearch);

				// value = copyValuesWithSpc(webElement);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//label[text()=\"inputParam1\"]/following::a[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				String testParamId = fetchMetadataVO.getTestScriptParamId();
				String testSetId = fetchMetadataVO.getTestSetLineId();
				dynamicnumber.saveCopyNumber(value, testParamId, testSetId);
				logger.info("Sucessfully Clicked copynumber" + scripNumber);

				return value;

			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during copynumber" + scripNumber);

		}
		try {

			if (inputParam1.equalsIgnoreCase("Number")) {

				Thread.sleep(5000);

				WebElement webElement = driver
						.findElement(By.xpath("//label[text()=\"" + inputParam1 + "\"]/following::a[1]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(webElement).build().perform();
				String stringToSearch = webElement.getText();
				value = copyValuesWithSpc(stringToSearch);

				// value = copyValuesWithSpc(webElement);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//label[text()=\"inputParam1\"]/following::a[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				String testParamId = fetchMetadataVO.getTestScriptParamId();
				String testSetId = fetchMetadataVO.getTestSetLineId();
				dynamicnumber.saveCopyNumber(value, testParamId, testSetId);
				logger.info("Sucessfully Clicked copynumber" + scripNumber);

				return value;

			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during copynumber" + scripNumber);

		}

		// DH 58
		try {

			if (inputParam1.equalsIgnoreCase("Expense Report")) {

				Thread.sleep(5000);

				WebElement webElement = driver
						.findElement(By.xpath("(//*[contains(text(),\"" + inputParam1 + "\")])[1]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(webElement).build().perform();
				String stringToSearch = webElement.getText();
				value = copyunderscore(stringToSearch);

				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//*[contains(text(),\"inputParam1\")])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				String testParamId = fetchMetadataVO.getTestScriptParamId();
				String testSetId = fetchMetadataVO.getTestSetLineId();
				dynamicnumber.saveCopyNumber(value, testParamId, testSetId);
				logger.info("Sucessfully Clicked Totals or Total copynumber" + scripNumber);
				return value;

			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Totals or Total copynumber" + scripNumber);

		}

		// DH 38

		try {

			if (inputParam1.equalsIgnoreCase("Confirmation") && (inputParam2.equalsIgnoreCase("document")
					|| inputParam2.equalsIgnoreCase("Requisition") || inputParam2.equalsIgnoreCase("initiative")
					|| inputParam2.equalsIgnoreCase("Negotiation"))) {

				Thread.sleep(5000);

				WebElement webElement = driver.findElement(By.xpath("//div[normalize-space(text())=\"" + inputParam1

						+ "\"]/following::*[contains(text(),\"" + inputParam2 + "\")][1]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(webElement).build().perform();
				String stringToSearch = webElement.getText();
				value = copyValuesWithSpc(stringToSearch);

				// value = copyValuesWithSpc(webElement);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//div[normalize-space(text())=\"inputParam1\"]/following::*[contains(text(),\"inputParam2\")][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				String testParamId = fetchMetadataVO.getTestScriptParamId();
				String testSetId = fetchMetadataVO.getTestSetLineId();
				dynamicnumber.saveCopyNumber(value, testParamId, testSetId);
				logger.info("Sucessfully Clicked copynumber" + scripNumber);

				return value;

			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during copynumber" + scripNumber);
		}

		try {
			if (inputParam1.equalsIgnoreCase("Item")) {
				Thread.sleep(5000);
				WebElement webElement = driver
						.findElement(By.xpath("//label[text()=\"" + inputParam1 + "\"]/following::td[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(webElement).build().perform();
				String stringToSearch = webElement.getText();
				value = copyValuesWithSpc(stringToSearch);
				// value = copyValuesWithSpc(webElement);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//label[text()=\"inputParam1\"]/following::td[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				String testParamId = fetchMetadataVO.getTestScriptParamId();
				String testSetId = fetchMetadataVO.getTestSetLineId();
				dynamicnumber.saveCopyNumber(value, testParamId, testSetId);
				logger.info("Sucessfully Clicked copynumber" + scripNumber);

				return value;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during copynumber" + scripNumber);
		}

		// DH 31
		try {
			if (inputParam1.equalsIgnoreCase("Confirmation") && inputParam2.equalsIgnoreCase("Payment")) {
				Thread.sleep(5000);

				WebElement webElement = driver.findElement(By.xpath("//div[normalize-space(text())=\"" + inputParam1
						+ "\"]/following::*[contains(text(),\"" + inputParam2 + "\")][1]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(webElement).build().perform();
				String stringToSearch = webElement.getText();
				value = copyNumbers(stringToSearch);

				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//div[normalize-space(text())=\"inputParam1\"]/following::*[contains(text(),\"inputParam2\")][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				String testParamId = fetchMetadataVO.getTestScriptParamId();
				String testSetId = fetchMetadataVO.getTestSetLineId();
				dynamicnumber.saveCopyNumber(value, testParamId, testSetId);
				logger.info("Sucessfully Clicked Totals or Total copynumber" + scripNumber);
				return value;

			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Totals or Total copynumber" + scripNumber);

		}

		try {

			if (inputParam1.equalsIgnoreCase("Totals") && inputParam2.equalsIgnoreCase("Total")) {

				Thread.sleep(5000);

				WebElement webElement = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + inputParam1

						+ "\"]/following::*[normalize-space(text())=\"" + inputParam2 + "\"]/following::span[1]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(webElement).build().perform();
				value = copyNegative(webElement);

				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[normalize-space(text())=\"inputParam1\"]/following::*[normalize-space(text())=\"inputParam2\"]/following::span[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				String testParamId = fetchMetadataVO.getTestScriptParamId();
				String testSetId = fetchMetadataVO.getTestSetLineId();
				dynamicnumber.saveCopyNumber(value, testParamId, testSetId);
				logger.info("Sucessfully Clicked Totals or Total copynumber" + scripNumber);
				return value;

			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Totals or Total copynumber" + scripNumber);

		}
		try {

			if (inputParam1.equalsIgnoreCase("Totals") || inputParam2.equalsIgnoreCase("Transaction Number")
					|| inputParam2.equalsIgnoreCase("Batch Number") || inputParam1.equalsIgnoreCase("Overview")) {

				Thread.sleep(5000);

				WebElement webElement = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + inputParam1

						+ "\"]/following::*[normalize-space(text())=\"" + inputParam2 + "\"]/following::span[1]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(webElement).build().perform();

				value = copyInt(webElement);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[normalize-space(text())=\"inputParam1\"]/following::*[normalize-space(text())=\"inputParam2\"]/following::span[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				String testParamId = fetchMetadataVO.getTestScriptParamId();
				String testSetId = fetchMetadataVO.getTestSetLineId();
				dynamicnumber.saveCopyNumber(value, testParamId, testSetId);
				logger.info("Sucessfully Clicked  copynumber" + scripNumber);

				return value;

			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  copynumber" + scripNumber);

		}

		// DH 34
		try {

			if (inputParam1.equalsIgnoreCase("Confirmation") || inputParam2.equalsIgnoreCase("adjustment")
					|| inputParam1.equalsIgnoreCase("Information")) {

				Thread.sleep(5000);

				// WebElement webElement =
				// driver.findElement(By.xpath("//div[normalize-space(text())=\"" + inputParam1
				//
//								+ "\"]/following::*[contains(text(),\"" + inputParam2 + "\")]"));

				WebElement webElement = driver.findElement(By.xpath("//div[normalize-space(text())=\"" + inputParam1

						+ "\"]/following::*[contains(text(),\"" + inputParam2 + "\")][1]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(webElement).build().perform();

				value = copyInt(webElement);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//div[normalize-space(text())=\"inputParam1\"]/following::*[contains(text(),\"inputParam2\")][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				String testParamId = fetchMetadataVO.getTestScriptParamId();
				String testSetId = fetchMetadataVO.getTestSetLineId();
				dynamicnumber.saveCopyNumber(value, testParamId, testSetId);
				logger.info("Sucessfully Clicked  copynumber" + scripNumber);

				return value;

			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during copynumber" + scripNumber);

		}

		try {

			WebElement webElement = driver.findElement(By.xpath("(//div[contains(@title,\"" + inputParam1 + "\")])[1]"));

			Actions actions = new Actions(driver);

			actions.moveToElement(webElement).build().perform();

			Thread.sleep(5000);

			if (webElement.isDisplayed() == true) {

				value = copyInt(webElement);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//div[contains(@title,\"inputParam1\")])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				String testParamId = fetchMetadataVO.getTestScriptParamId();
				String testSetId = fetchMetadataVO.getTestSetLineId();
				dynamicnumber.saveCopyNumber(value, testParamId, testSetId);
				logger.info("Sucessfully Clicked copynumber" + scripNumber);

				return value;

			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during copynumber" + scripNumber);

		}

		try {
			WebElement webElement = driver.findElement(By.xpath("(//div[contains(text(),\"" + inputParam1 + "\")])[1]"));

			Actions actions = new Actions(driver);

			actions.moveToElement(webElement).build().perform();

			Thread.sleep(5000);

			if (webElement.isDisplayed() == true) {

				value = copyMethod(webElement, value);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//div[contains(text(),\"inputParam1\")])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				String testParamId = fetchMetadataVO.getTestScriptParamId();
				String testSetId = fetchMetadataVO.getTestSetLineId();
				dynamicnumber.saveCopyNumber(value, testParamId, testSetId);
				logger.info("Sucessfully Clicked copynumber" + scripNumber);

				return value;

			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during copynumber" + scripNumber);

		}
		try {

			WebElement webElement = driver
					.findElement(By.xpath("//label[text()=\"" + inputParam1 + "\"]/following::td[1]"));

			Actions actions = new Actions(driver);

			actions.moveToElement(webElement).build().perform();

			Thread.sleep(5000);

			if (webElement.isDisplayed() == true) {

				value = copyMethod(webElement, value);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//label[text()=\"inputParam1\"]/following::td[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				String testParamId = fetchMetadataVO.getTestScriptParamId();
				String testSetId = fetchMetadataVO.getTestSetLineId();
				dynamicnumber.saveCopyNumber(value, testParamId, testSetId);
				logger.info("Sucessfully Clicked copynumber" + scripNumber);

				return value;

			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during copynumber" + scripNumber);

		}

		try {
			WebElement webElement = driver.findElement(By.xpath("//img[@title=\"In Balance \"]/following::td[1]"));

			Actions actions = new Actions(driver);

			actions.moveToElement(webElement).build().perform();

			if (webElement.isDisplayed() == true) {

				value = copyMethod(webElement, value);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//img[@title=\"In Balance \"]/following::td[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				String testParamId = fetchMetadataVO.getTestScriptParamId();
				String testSetId = fetchMetadataVO.getTestSetLineId();
				dynamicnumber.saveCopyNumber(value, testParamId, testSetId);
				logger.info("Sucessfully Clicked copynumber" + scripNumber);

				return value;

			}

		} catch (Exception e) {

			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during copynumber" + scripNumber);

			screenshotFail(driver, fetchMetadataVO, customerDetails);

			throw e;

		}

		return value;

	}

	private String copyMethod(WebElement webElement, String value) {

		String num = null;

		try {

			String number = webElement.getText();

			num = number.replaceAll("[^\\d.]+|\\.(?!\\d)", "");

			logger.info("Successfully Copied the Number");

		} catch (Exception e) {
			logger.error("Sucessfully Clicked copynumber");

			logger.error(e.getMessage());

		}

		return num;

	}

	/*
	 * private String copyInt(WebElement webElement) {
	 * 
	 * String num = null;
	 * 
	 * try {
	 * 
	 * // System.out.println(value);
	 * 
	 * String number = webElement.getText().toString();
	 * 
	 * System.out.println(number);
	 * 
	 * num = number.replaceAll("[^\\d.]+|\\.(?!\\d)", "");
	 * 
	 * System.out.println(num);
	 * 
	 * log.info("Successfully Copied the Number");
	 * 
	 * } catch (Exception e) {
	 * 
	 * logger.error(e.getMessage());
	 * 
	 * }
	 * 
	 * return num;
	 * 
	 * }
	 * 
	 */

	private String copyInt(WebElement webElement) {

		String num = null;
		String num1 = null;

		try {
			String number = webElement.getText().toString();
			num = number.replaceAll("\\b\\w+(?<!\\w[\\d@]\\b)\\b", "");
			num1 = num.replaceAll("[().$#@!*&^\\/\\\\]", "");
			// String num2= num1.trim();
			String num2 = num1.replaceAll("[^a-zA-Z0-9]", "").trim();
			Thread.sleep(2000);
			logger.info("Successfully Copied the Number");
			return num2;
		} catch (Exception e) {
			logger.error("Failed during  copy Integer value " + e.getMessage());
		}
		try {

			String number = webElement.getText().toString();

			num = number.replaceAll("[^\\d.]+|\\.(?!\\d)", "");

			logger.info("Successfully Copied the Number");

		} catch (Exception e) {

			logger.error("Failed during  copy Integer value " + e.getMessage());

		}

		return num;

	}

	private String copyNegative(WebElement webElement) {

		String num = null;

		try {

			String number = webElement.getText().toString();

			num = number.replaceAll("[^\\-\\,\\d.]+|\\.(?!\\d)", number);
			logger.info("Successfully Copied the Number");

		} catch (Exception e) {

			logger.error("Failed during  copy negative value " + e.getMessage());

		}

		return num;

	}

	public String copyy(WebDriver driver, String xpath, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {
		try {
			WebElement webElement = driver.findElement(By.xpath(xpath));
			String number = webElement.getText();
			String num = number.replaceAll("[^\\d.]+|\\.(?!\\d)", "");
			StringSelection stringSelection = new StringSelection(num);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked copyy" + scripNumber);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during copyy" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
		return xpath;
	}

	public String copytext(WebDriver driver, String xpath, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {
		try {
			java.util.List<WebElement> webElement = driver.findElements(By.xpath(xpath));
			ArrayList<String> texts = new ArrayList<String>();
			for (WebElement element : webElement) {
				String[] text = element.getText().split(":");
				if (text.length == 2) {
					texts.add(text[1].trim().toString());
					StringSelection stringSelection = new StringSelection(texts.get(0));
					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
				}
			}
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked copytext" + scripNumber);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during copytext" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
		return xpath;

	}

	public void maximize(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {
		try {
			driver.manage().window().maximize();
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked maximize" + scripNumber);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during maximize" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			e.printStackTrace();
			throw e;

		}
	}

	public void switchWindow(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {
		try {
			driver.switchTo().window(Main_Window);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked switchWindow" + scripNumber);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during switchWindow" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			e.printStackTrace();

			throw e;
		}
	}

	public void switchDefaultContent(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {
		try {
			Thread.sleep(8000);
			Set<String> set = driver.getWindowHandles();
			Iterator<String> itr = set.iterator();
			while (itr.hasNext()) {
				String childWindow = itr.next();
				driver.switchTo().window(childWindow);
				Thread.sleep(4000);
			}
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked switchDefaultContent" + scripNumber);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during switchDefaultContent" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			e.printStackTrace();
			throw e;
		}
	}

	public void switchParentWindow(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {
		try {
			Thread.sleep(8000);
			Set<String> set = driver.getWindowHandles();
			Iterator<String> itr = set.iterator();
			while (itr.hasNext()) {
				String childWindow = itr.next();
				driver.switchTo().window(childWindow);
			}
			driver.close();
			Thread.sleep(2000);
			Set<String> set1 = driver.getWindowHandles();
			Iterator<String> itr1 = set1.iterator();
			while (itr1.hasNext()) {
				String childWindow = itr1.next();
				driver.switchTo().window(childWindow);
			}
			driver.close();
			Set<String> set2 = driver.getWindowHandles();
			Iterator<String> itr2 = set2.iterator();
			while (itr2.hasNext()) {
				String childWindow = itr2.next();
				driver.switchTo().window(childWindow);
			}
			
			renameDownloadedFile(driver,fetchMetadataVO, fetchConfigVO, customerDetails);

		} catch (Exception e) {
			logger.error("Failed to Handle the window");
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			e.printStackTrace();
			throw e;
		}
	}

	public void switchToParentWindow(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {
		try {
			Thread.sleep(8000);
			Set<String> set = driver.getWindowHandles();
			Iterator<String> itr = set.iterator();
			while (itr.hasNext()) {
				String childWindow = itr.next();
				driver.switchTo().window(childWindow);
			}
			driver.close();
			Set<String> set1 = driver.getWindowHandles();
			Iterator<String> itr1 = set1.iterator();
			while (itr1.hasNext()) {
				String childWindow = itr1.next();
				driver.switchTo().window(childWindow);
			}
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked switchToParentWindow" + scripNumber);
			
			renameDownloadedFile(driver,fetchMetadataVO, fetchConfigVO, customerDetails);

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during switchToParentWindow" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			e.printStackTrace();
			throw e;
		}
	}

	public void dragAnddrop(WebDriver driver, String xpath, String xpath1, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {
		try {
			WebElement dragElement = driver.findElement(By.xpath(xpath));
			fromElement = dragElement;
			WebElement dropElement = driver.findElement(By.xpath(xpath1));
			toElement = dropElement;
			Actions action = new Actions(driver);
			// Action dragDrop = action.dragAndDrop(fromElement, webElement).build();
			action.dragAndDrop(fromElement, toElement).build().perform();
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Successfully Drag and drop the values" + scripNumber);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed During dragAnddrop Action." + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			e.printStackTrace();
			throw e;
		}
	}

	public void windowhandle(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {
		try {
			Thread.sleep(5000);
			WebElement waittext = driver
					.findElement(By.xpath("//iframe[@aria-describedby=\"cke_101\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			driver.switchTo().frame(waittext);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//iframe[@aria-describedby=\"cke_101\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			logger.error("Failed during  window handle " + e.getMessage());
		}
		try {
			Thread.sleep(5000);
			WebElement waittext = driver
					.findElement(By.xpath("//iframe[@aria-describedby=\"cke_39\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			driver.switchTo().frame(waittext);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//iframe[@aria-describedby=\"cke_39\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			logger.error("Failed during  window handle " + e.getMessage());
		}
		try {
			Thread.sleep(20000);
			String mainWindow = driver.getWindowHandle();
			Set<String> set = driver.getWindowHandles();
			Iterator<String> itr = set.iterator();
			while (itr.hasNext()) {
				String childWindow = itr.next();
				if (!mainWindow.equals(childWindow)) {
					driver.switchTo().window(childWindow);
					driver.manage().window().maximize();
					Thread.sleep(2000);
					screenshot(driver, fetchMetadataVO, customerDetails);
					driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
					driver.switchTo().window(childWindow);
				}
			}
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Successfully Handeled the window" + scripNumber);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed to Handle the window" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			e.printStackTrace();
			throw e;
		}
	}

	public void switchToFrame(WebDriver driver, String inputParam, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {

			if (inputParam.equalsIgnoreCase("Description for Internal Candidates")) {
				Thread.sleep(5000);
				WebElement waittext = driver
						.findElement(By.xpath("//iframe[@aria-describedby=\"cke_101\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				driver.switchTo().frame(waittext);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//iframe[@aria-describedby=\"cke_101\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
			} catch (Exception e) {
				logger.error("Failed during switch to frame " + e.getMessage());
			}
		
		try {
			if (inputParam.equalsIgnoreCase("Qualifications for Internal Candidates")) {
			Thread.sleep(5000);
			WebElement waittext = driver
					.findElement(By.xpath("//iframe[@aria-describedby=\"cke_39\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			driver.switchTo().frame(waittext);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//iframe[@aria-describedby=\"cke_39\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		}} catch (Exception e) {
			logger.error("Failed during switch to frame " + e.getMessage());
		}
		try {
			Thread.sleep(5000);
			WebElement waittext = driver
					.findElement(By.xpath("//h1[normalize-space(text())=\"" + inputParam + "\"]/following::iframe[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			driver.switchTo().frame(waittext);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//h1[normalize-space(text())=\"inputParam\"]/following::iframe[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			logger.error("Failed during switch to frame " + e.getMessage());
		}
		try {
			WebElement waittext = driver.findElement(By.xpath("//iframe[contains(@id,\"" + inputParam + "\")]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			driver.switchTo().frame(waittext);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//iframe[contains(@id,\"inputParam\")]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during switch to frame " + e.getMessage());
		}
		try {
			Thread.sleep(5000);
			WebElement waittext = driver.findElement(By.xpath("//iframe[@title=\"" + inputParam + "\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			driver.switchTo().frame(waittext);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//iframe[@title=\"inputParam\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during switch to frame " + e.getMessage());
		}

		try {
			Thread.sleep(10000);
			WebElement waittext = driver
					.findElement(By.xpath("//*[normalize-space(text())=\"" + inputParam + "\"]/following::iframe[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			driver.switchTo().frame(waittext);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[normalize-space(text())=\"inputParam\"]/following::iframe[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during switch to frame " + e.getMessage());
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void uploadFileAutoIT(WebDriver webDriver, String fileLocation, String param1, String param2, String param3, ScriptDetailsDto scriptDetailsDto, CustomerProjectDto customerProjectDto) throws Exception {
//		try {
//			String autoitscriptpath = System.getProperty("user.dir") + "/" + "File_upload_selenium_webdriver.au3";
//
//			Runtime.getRuntime().exec("cmd.exe /c Start AutoIt3.exe " + autoitscriptpath + " \"" + filelocation + "\"");
//			log.info("Successfully Uploaded The File");
//		} catch (Exception e) {
//			log.error("Failed During uploadFileAutoIT Action.");
////			screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, fetchConfigVO);
//			System.out.println(filelocation);
//			e.printStackTrace();
//			throw e;
//
//		}
		try {
			if(param1.equalsIgnoreCase("file")) {
				String uploadXPath = "//*[@type='"+param1+"']";
				WebElement uploadZip = webDriver.findElement(By.xpath(uploadXPath));
				Thread.sleep(5000);
				File file = new File(fileLocation+param3);
				logger.info("File Location" + file);
				uploadZip.sendKeys(file.getAbsolutePath());
				logger.info("Successfully Uploaded The File");
				screenshot(webDriver, scriptDetailsDto, customerProjectDto);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed During uploadFileAutoIT Action.");
			screenshotFail(webDriver, scriptDetailsDto, customerProjectDto);
			logger.error("File Location" + fileLocation);
			e.printStackTrace();
		}
		
		try {
			if(param1.equalsIgnoreCase("Add") && param2.equalsIgnoreCase("File")) {
			String uploadXPath = "//*[text()='"+param1+" "+param2+"']";
			WebElement uploadZip = webDriver.findElement(By.xpath(uploadXPath));
			Thread.sleep(5000);
			File file = new File(fileLocation+param3);
			logger.info("File Location" + file);
			uploadZip.sendKeys(file.getAbsolutePath());
			logger.info("Successfully Uploaded The File");
			screenshot(webDriver, scriptDetailsDto, customerProjectDto);
			return;
			}
		} catch (Exception e) {
			logger.error("Failed During uploadFileAutoIT Action.");
			screenshotFail(webDriver, scriptDetailsDto, customerProjectDto);
			logger.error("File Location" + fileLocation);
			e.printStackTrace();
			throw e;
		}
		try {
			if ((param2 == null && param3 == null) || (param2.equalsIgnoreCase("") && param3.equalsIgnoreCase(""))) {
				logger.info("Started Upload file");
				Thread.sleep(4000);
				webDriver.findElement(By.xpath("//*[@type='file']")).sendKeys(param1);
				Thread.sleep(3000);
				logger.info("Successfully Uploaded The File");
				return;
			}
		} catch (Exception e) {
			logger.error("Failed During uploadFileAutoIT Action.");
			logger.error("File Location" + fileLocation);
			e.printStackTrace();
		}
		
	}

	public void refreshPage(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {
		try {
			driver.navigate().refresh();
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked refreshPage" + scripNumber);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during refreshPage" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			e.printStackTrace();
			throw e;

		}
	}

	public String getErrorMessages(WebDriver driver) {
		try {
			String text = driver.findElement(By.xpath("//td[@class=\"AFNoteWindow\"]")).getText();
			return text;
		} catch (Exception e) {
			logger.error("Failed to get error message " +e.getMessage());
		}
		try {
			String text = driver.findElement(By.xpath("//div[contains(@class,\"Error\")]")).getText();
			return text;
		} catch (Exception e) {
			logger.error("Failed to get error message " +e.getMessage());
		}
//		try {
//			String text = driver.findElement(By.xpath("//div[contains(text(),\"Error\")]")).getText();
//			return text;
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//		}
		try {
			String text = driver.findElement(By.xpath("//div[contains(@id,\"ConfirmationDialogId\") and text()=\"Error\"]")).getText();
			return text;
		} catch (Exception e) {
			logger.error("Failed to get error message " +e.getMessage());
		}
		return null;
	}

	@Override
	public void multipleSendKeys(WebDriver driver, String param1, String param2, String value1, String value2,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			if (param1.equalsIgnoreCase("Requisition Lines")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittill = driver.findElement(By.xpath(
						"//*[text()=\"" + param1 + "\"]/following::*[text()=\"" + value1 + "\"]/following::input[1]"));
				Thread.sleep(1000);
				// wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[text()=\""+param1+"\"]/following::*[text()=\""+value1+"\"]/following::input[1]"),
				// value1));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, value2, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String xpath = "//*[text()=\"param1\"]/following::*[text()=\"value1\"]/following::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during multiple send keys " + e.getMessage());
			screenshotFail(driver, fetchMetadataVO, customerDetails);
		}
	}

	private String copyValuesWithSpc(String value) {
		try {
			Pattern p = Pattern.compile("\\b[A-Z]*\\-*[A-Z]*\\-*[a-z]*[A-Z]*[a-z]*\\d+[a-z]*[A-Z]*[a-z]*[0-9]*"); // the
																													// pattern
																													// to
																													// search
																													// for
																													// \b[IBAA]+\-[A-Z]+\-\d+
																													// (\\b[Payment]+\\s[\\d]+)
			Matcher m = p.matcher(value);
			// if we find a match, get the group
			String theGroup = null;
			if (m.find()) {
				// we\"re only looking for one group, so get it
				theGroup = m.group(0);
				// theGroup = theGroup.replaceAll("\\b\\w+(?<!\\w[\\d@]\\b)\\b", "");
				// System.out.println(theGroup);
				theGroup = theGroup.replaceAll(" ", "");
			}
			return theGroup;
		} catch (Exception e) {
			logger.error("Failed during copy value with special characters " + e.getMessage());
			throw e;
		}

	}

	public void oicLogout(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO, String type1,
			String type2, String type3, String param1, String param2, String param3, CustomerProjectDto customerDetails) throws Exception {

		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(
					ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@class,\"opaas-user__icon\")]")));
			WebElement waittext = driver.findElement(By.xpath("//*[contains(@class,\"opaas-user__icon\")]"));
			waittext.click();
			Thread.sleep(4000);
			screenshot(driver, fetchMetadataVO, customerDetails);
			WebElement signout = driver.findElement(By.xpath("//*[text()=\"Sign Out\"]"));
			signout.click();
			String xpath = "//*[contains(@class,\"opaas-user__icon\")]" + ";" + "//*[text()=\"Sign Out\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed to logout " + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}

	}

	public void loginOicApplication(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			String type1, String type2, String type3, String param1, String param2, String param3, String keysToSend,
			String value, CustomerProjectDto customerDetails) throws Exception {
		String param4 = "User name or email";
		String param5 = "password";
		// String param6 = "Sign In";
		navigateOICUrl(driver, fetchConfigVO, fetchMetadataVO, customerDetails);
		String xpath1 = oicLoginPage(driver, param4, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
		String xpath2 = oicLoginPage(driver, param5, value, fetchMetadataVO, fetchConfigVO, customerDetails);
		if (xpath2.equalsIgnoreCase(null)) {
			throw new IOException("Failed during login page");
		}
		String scripNumber = fetchMetadataVO.getScriptNumber();
		String xpath = xpath1 + ";" + xpath2;
		String scriptID = fetchMetadataVO.getScriptId();
		String lineNumber = fetchMetadataVO.getLineNumber();
		service.saveXpathParams(scriptID, lineNumber, xpath);
	}

	public void loginOicJob(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			String type1, String type2, String type3, String param1, String param2, String param3, String keysToSend,
			String value, CustomerProjectDto customerDetails) throws Exception {
		String param4 = "User name or email";
		String param5 = "password";
		// String param6 = "Sign In";
		navigateOICJobUrl(driver, fetchConfigVO, fetchMetadataVO, customerDetails);
		String xpath1 = oicLoginPage(driver, param4, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
		String xpath2 = oicLoginPage(driver, param5, value, fetchMetadataVO, fetchConfigVO, customerDetails);
		if (xpath2.equalsIgnoreCase(null)) {
			throw new IOException("Failed during login page");
		}
		String scripNumber = fetchMetadataVO.getScriptNumber();
		String xpath = xpath1 + ";" + xpath2;
		String scriptID = fetchMetadataVO.getScriptId();
		String lineNumber = fetchMetadataVO.getLineNumber();
		service.saveXpathParams(scriptID, lineNumber, xpath);
	}

	public String oicLoginPage(WebDriver driver, String param1, String keysToSend, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {
		String xpath = null;
		try {
			if (param1.equalsIgnoreCase("Password")) {
				String title1 = driver.getTitle();
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("//*[contains(@placeholder,\"" + param1 + "\")]")));
				JavascriptExecutor jse = (JavascriptExecutor) driver;
				jse.executeScript("document.getElementById(\"idcs-signin-basic-signin-form-password|input\").value = \""
						+ keysToSend + "\";");
				// if("password".equalsIgnoreCase(param1))
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
				String title2 = driver.getTitle();
				if (title1.equalsIgnoreCase(title2)) {
					screenshotFail(driver, fetchMetadataVO, customerDetails);
					throw new IOException("Failed during login page");
				}
				// screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Succesfully password is entered " + scripNumber);
				xpath = "//*[contains(@placeholder,\"param1\")]";
				return xpath;
			}
		} catch (Exception e) {
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed to enter password " + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("//*[contains(@placeholder,\"" + param1 + "\")]")));
			WebElement waittill = driver.findElement(By.xpath("//*[contains(@placeholder,\"" + param1 + "\")]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("arguments[0].value=\"" + keysToSend + "\";", waittill);
			// if("password".equalsIgnoreCase(param1))
			// screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			Thread.sleep(1000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			xpath = "//*[contains(@placeholder,\"param1\")]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Successfully entered User Name " + scripNumber);
			return xpath;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Failed during login page " + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
		}
		return xpath;
	}

	public synchronized void oicNavigate(WebDriver driver, FetchConfigVO fetchConfigVO,
			ScriptDetailsDto fetchMetadataVO, String type1, String type2, String param1, String param2, int count, CustomerProjectDto customerDetails)
			throws Exception {
		String param3 = "Show / Hide Navigation menu";

		String xpath = oicNavigator(driver, param3, fetchMetadataVO, fetchConfigVO, customerDetails);
		String xpath1 = oicMenuNavigation(driver, param1, fetchMetadataVO, fetchConfigVO, customerDetails);
		String xpath2 = oicMenuNavigationButton(driver, fetchMetadataVO, fetchConfigVO, type1, type2, param1, param2,
				count, customerDetails);
		String scripNumber = fetchMetadataVO.getScriptNumber();
		String xpaths = xpath + ">" + xpath1 + ">" + xpath2;
		String scriptID = fetchMetadataVO.getScriptId();
		String lineNumber = fetchMetadataVO.getLineNumber();
		service.saveXpathParams(scriptID, lineNumber, xpath);
	}

	public String oicNavigator(WebDriver driver, String param1, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			Thread.sleep(4000);
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//*[@title=\"" + param1 + "\"]//*[contains(@class,\"oj-start\")]")));
			wait.until(ExpectedConditions
					.elementToBeClickable(By.xpath("//*[@title=\"" + param1 + "\"]//*[contains(@class,\"oj-start\")]")));
			WebElement waittext = driver
					.findElement(By.xpath("//*[@title=\"" + param1 + "\"]//*[contains(@class,\"oj-start\")]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			actions.moveToElement(waittext).click().build().perform();
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Successfully navigator is done " + scripNumber);
			String xpath = "//*[@title=\"param1\"]//*[contains(@class,\"oj-start\")]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return xpath;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during navigator " + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public String oicMenuNavigation(WebDriver driver, String param1, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			Thread.sleep(5000);
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("(//div[@class=\"navlist-container\"]//span[text()=\"" + param1 + "\"])[1]")));
			wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("(//div[@class=\"navlist-container\"]//span[text()=\"" + param1 + "\"])[1]")));
			WebElement waittext = driver
					.findElement(By.xpath("(//div[@class=\"navlist-container\"]//span[text()=\"" + param1 + "\"])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			actions.moveToElement(waittext).click().build().perform();
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Successfully menunavigation is clicked " + scripNumber);
			String xpath = "(//div[@class=\"navlist-container\"]//span[text()=\"param1\"])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Successfully menunavigation is clicked " + scripNumber);
			return xpath;

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Menunavigation " + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	/*
	 * public String oicClickMenu(WebDriver driver, String param1, ScriptDetailsDto
	 * fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception { try {
	 * Thread.sleep(5000); WebDriverWait wait = new WebDriverWait(driver,
	 * fetchConfigVO.getWait_time());
	 * wait.until(ExpectedConditions.presenceOfElementLocated(
	 * By.xpath("(//div[@class=\"navlist-container\"]//span[text()=\""+ param1
	 * +"\"])[1]"))); wait.until(ExpectedConditions.elementToBeClickable(
	 * By.xpath("(//div[@class=\"navlist-container\"]//span[text()=\""+ param1
	 * +"\"])[1]"))); WebElement waittext = driver
	 * .findElement(By.xpath("(//div[@class=\"navlist-container\"]//span[text()=\""+
	 * param1 +"\"])[1]")); Actions actions = new Actions(driver);
	 * actions.moveToElement(waittext).build().perform();
	 * actions.moveToElement(waittext).click().build().perform(); screenshot(driver,
	 * "", fetchMetadataVO, fetchConfigVO); String scripNumber =
	 * fetchMetadataVO.getScriptNumber();
	 * log.info("Successfully menunavigation is clicked " + scripNumber); String
	 * xpath = "(//div[@class=\"navlist-container\"]//span[text()=\""+ param1
	 * +"\"])[1]"; log.info("Successfully menunavigation is clicked " + scripNumber);
	 * return xpath;
	 * 
	 * } catch (Exception e) { String scripNumber =
	 * fetchMetadataVO.getScriptNumber(); log.error("Failed during Menunavigation "
	 * + scripNumber);
	 * 
	 * screenshotFail(driver, "Failed during Navigation Method", fetchMetadataVO,
	 * fetchConfigVO); System.out.println("Not able to navitage to the :" + "" +
	 * param1); throw e; } }
	 */
	public String oicMenuNavigationButton(WebDriver driver, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, String type1, String type2, String param1, String param2, int count, CustomerProjectDto customerDetails)
			throws Exception {
		String xpath = null;
		try {
			Thread.sleep(5000);
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("(//div[@class=\"navlist-container\"]//span[text()=\"" + param2 + "\"])[2]")));
			wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("(//div[@class=\"navlist-container\"]//span[text()=\"" + param2 + "\"])[2]")));
			WebElement waittext = driver
					.findElement(By.xpath("(//div[@class=\"navlist-container\"]//span[text()=\"" + param2 + "\"])[2]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			actions.moveToElement(waittext).click().build().perform();
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Successfully menuNavigationButton is done " + scripNumber);
			xpath = "(//div[@class=\"navlist-container\"]//span[text()=\"param2\"])[2]";
			logger.info("Successfully menuNavigationButton is done " + scripNumber);
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return xpath;

		} catch (Exception e) {
			logger.error("Count value exceeds the limit " + count);
			logger.error("Failed During Navigation");
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}

	}

	public void oicClickButton(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		
		try {
			if (param1.equalsIgnoreCase("WATS Inbound Inv Transactions") && param2.equalsIgnoreCase("Run")) {
				Thread.sleep(3000);
				Actions action = new Actions(driver);
				WebElement we = driver.findElement(By.xpath(
						"(//*[text()=\"WATS Inbound Inv Transactions\"])[1]/following::*[text()=\"Scheduled Orchestration\"][1]"));
				action.moveToElement(we).perform();
				Thread.sleep(5000);
				WebElement run = driver.findElement(By.xpath(
						"(//*[text()=\"WATS Inbound Inv Transactions\"])[1]/following::*[text()=\"Scheduled Orchestration\"]/following::*[@title=\"Run\"][1]"));
				run.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath1 = "(//*[text()=\"WATS Inbound Inv Transactions\"])[1]/following::*[text()=\"Scheduled Orchestration\"][1]";
				String xpath2 = "(//*[text()=\"WATS Inbound Inv Transactions\"])[1]/following::*[text()=\"Scheduled Orchestration\"]/following::*[@title=\"Run\"][1]";
				String xpath = xpath1 + ";" + xpath2;
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Oracle ERP OPERA Trigger Synchronization") && param2.equalsIgnoreCase("Run")) {
				Thread.sleep(3000);
				Actions action = new Actions(driver);
				WebElement we = driver.findElement(By.xpath(
						"(//*[text()=\"Oracle ERP OPERA Trigger Synchronization\"])[1]/following::*[text()=\"Scheduled Orchestration\"][1]"));
				action.moveToElement(we).perform();
				Thread.sleep(5000);
				WebElement run = driver.findElement(By.xpath(
						"(//*[text()=\"Oracle ERP OPERA Trigger Synchronization\"])[1]/following::*[text()=\"Scheduled Orchestration\"]/following::*[@title=\"Run\"][1]"));
				run.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath1 = "(//*[text()=\"Oracle ERP OPERA Trigger Synchronization\"])[1]/following::*[text()=\"Scheduled Orchestration\"][1]";
				String xpath2 = "(//*[text()=\"Oracle ERP OPERA Trigger Synchronization\"])[1]/following::*[text()=\"Scheduled Orchestration\"]/following::*[@title=\"Run\"][1]";
				String xpath = xpath1 + ";" + xpath2;
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);
			logger.error(e.getMessage());
		}

		try {
			if (param1.equalsIgnoreCase("Oracle OPERA ERP Trigger Invoice Upload") && param2.equalsIgnoreCase("Run")) {
				Thread.sleep(3000);
				Actions action = new Actions(driver);
				WebElement we = driver.findElement(By.xpath(
						"(//*[text()=\"Oracle OPERA ERP Trigger Invoice Upload\"])[1]/following::*[text()=\"Scheduled Orchestration\"]"));
				action.moveToElement(we).perform();
				Thread.sleep(5000);
				WebElement run = driver.findElement(By.xpath(
						"(//*[text()=\"Oracle OPERA ERP Trigger Invoice Upload\"])[1]/following::*[text()=\"Scheduled Orchestration\"]/following::*[@title=\"Run\"][1]"));
				run.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);

				String xpath1 = "(//*[text()=\"Oracle OPERA ERP Trigger Invoice Upload\"])[1]/following::*[text()=\"Scheduled Orchestration\"]";
				String xpath2 = "(//*[text()=\"Oracle OPERA ERP Trigger Invoice Upload\"])[1]/following::*[text()=\"Scheduled Orchestration\"]/following::*[@title=\"Run\"][1]";
				String xpath = xpath1 + ";" + xpath2;
				String scriptID = fetchMetadataVO.getScriptId();

				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);
			logger.error(e.getMessage());
		}

		try {
			if (param1.equalsIgnoreCase("Oracle OPERA ERP Initiate Refresh Receivables")
					&& param2.equalsIgnoreCase("Run")) {
				Thread.sleep(3000);
				Actions action = new Actions(driver);
				WebElement we = driver.findElement(By.xpath(
						"(//*[text()=\"Oracle OPERA ERP Initiate Refresh Receivables\"])[1]/following::*[text()=\"App Driven Orchestration\"]"));
				action.moveToElement(we).perform();
				Thread.sleep(5000);
				WebElement run = driver.findElement(By.xpath(
						"(//*[text()=\"Oracle OPERA ERP Initiate Refresh Receivables\"])[1]/following::*[text()=\"App Driven Orchestration\"]/following::*[@title=\"Run\"][1]"));
				run.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath = "(//*[text()=\"Oracle OPERA ERP Initiate Refresh Receivables\"])[1]/following::*[text()=\"App Driven Orchestration\"]"
						+ ";"
						+ "(//*[text()=\"Oracle OPERA ERP Initiate Refresh Receivables\"])[1]/following::*[text()=\"App Driven Orchestration\"]/following::*[@title=\"Run\"][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);
			logger.error(e.getMessage());
		}

		try {
			if (param1.equalsIgnoreCase("OPERA ERP Initiate Process Receivables") && param2.equalsIgnoreCase("Run")) {
				Thread.sleep(3000);
				Actions action = new Actions(driver);
				WebElement we = driver.findElement(By.xpath(
						"(//*[text()=\"OPERA ERP Initiate Process Receivables\"])[1]/following::*[text()=\"Scheduled Orchestration\"]"));
				action.moveToElement(we).perform();
				Thread.sleep(5000);
				WebElement run = driver.findElement(By.xpath(
						"(//*[text()=\"OPERA ERP Initiate Process Receivables\"])[1]/following::*[text()=\"Scheduled Orchestration\"]/following::*[@title=\"Run\"][1]"));
				run.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath1 = "(//*[text()=\"OPERA ERP Initiate Process Receivables\"])[1]/following::*[text()=\"Scheduled Orchestration\"]";
				String xpath2 = "(//*[text()=\"OPERA ERP Initiate Process Receivables\"])[1]/following::*[text()=\"Scheduled Orchestration\"]/following::*[@title=\"Run\"][1]";
				String xpath = xpath1 + ";" + xpath2;
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);
			logger.error(e.getMessage());
		}

		try {
			if (param1.equalsIgnoreCase("Warning") && param2.equalsIgnoreCase("OK")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath(("//*[text()=\"" + param1 + "\"]/following::*[text()=\"K\"]"))));
				WebElement waittext = driver
						.findElement(By.xpath(("//*[text()=\"" + param1 + "\"]/following::*[text()=\"K\"]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				Thread.sleep(3000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Create Time Card clickButton" + scripNumber);
				String xpath = "//*[text()=\"param1\"]/following::*[text()=\"K\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Create Time Card clickButton" + scripNumber);
			logger.error(e.getMessage());
		}

		try {
			if (param1.equalsIgnoreCase("Job Set Details")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver.findElement(By.xpath(("//*[text()=\"" + param1
						+ "\"]/following::span[text()=\"" + param2 + "\"]/following::*[@class=\"vb-icon vb-icon-plug\"]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				screenshot(driver, fetchMetadataVO, customerDetails);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(15000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath = "//*[text()=\"param1\"]/following::span[text()=\"param2\"]/following::*[@class=\"vb-icon vb-icon-plug\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);
			logger.error(e.getMessage());
		}

		try {

			if (param1.equalsIgnoreCase("DH Projects to COA") && param2.equalsIgnoreCase("Run")) {
				Thread.sleep(3000);
				Actions action = new Actions(driver);
				// WebElement we = driver.findElement(By.xpath("(//*[text()=\"Scheduled
				// Orchestration\"]/following::*[@title=\"Run\"])[1]"));
				WebElement we = driver.findElement(By.xpath("//*[text()=\"Scheduled Orchestration\"]"));
				action.moveToElement(we).perform();
				Thread.sleep(5000);
				WebElement run = driver
						.findElement(By.xpath("(//*[text()=\"Scheduled Orchestration\"]/following::*[@title=\"Run\"])[1]"));
				run.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath1 = "//*[text()=\"Scheduled Orchestration\"]";
				String xpath2 = "(//*[text()=\"Scheduled Orchestration\"]/following::*[@title=\"Run\"])[1]";
				String xpath = xpath1 + ";" + xpath2;
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);
			logger.error(e.getMessage());
		}
		/*
		 * try { if(param1.equalsIgnoreCase("DH SCM Supplier Site Inactivation") &&
		 * param2.equalsIgnoreCase("Run")) { Thread.sleep(3000); Actions action = new
		 * Actions(driver); WebElement we = driver.findElement(By.
		 * xpath("(//*[text()=\"DH SCM Supplier Site Inactivation\"])[1]"));
		 * action.moveToElement(we).perform(); Thread.sleep(5000); WebElement run =
		 * driver.findElement(By.
		 * xpath("(//*[text()=\"DH SCM Supplier Site Inactivation\"])[1]/following::*[@title=\"Run\"][1]"
		 * )); run.click(); screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		 * Thread.sleep(5000); String scripNumber = fetchMetadataVO.getScriptNumber();
		 * log.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
		 * String xpath =
		 * "(//*[text()=\"DH SCM Supplier Site Inactivation\"])[1]/following::*[@title=\"Run\"][1]"
		 * ; String scriptID=fetchMetadataVO.getScriptId();String
		 * metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(
		 * scriptID,metadataID,xpath); return; } }catch (Exception e) { String
		 * scripNumber = fetchMetadataVO.getScriptNumber();
		 * log.error("Failed during clickButton" + scripNumber); logger.error(e.getMessage());
		 * }
		 */
		// testing
		try {
			if (param1.equalsIgnoreCase("DH SCM Supplier Site Inactivation") && param2.equalsIgnoreCase("Run")) {
				Thread.sleep(3000);
				Actions action = new Actions(driver);
				WebElement we = driver.findElement(By.xpath(
						"(//*[text()=\"DH SCM Supplier Site Inactivation\"])[1]/following::*[text()=\"Scheduled Orchestration\"][1]"));
				action.moveToElement(we).perform();
				Thread.sleep(5000);
				WebElement run = driver.findElement(By.xpath(
						"(//*[text()=\"DH SCM Supplier Site Inactivation\"])[1]/following::*[text()=\"Scheduled Orchestration\"]/following::*[@title=\"Run\"][1]"));
				run.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath1 = "(//*[text()=\"DH SCM Supplier Site Inactivation\"])[1]/following::*[text()=\"Scheduled Orchestration\"][1]";
				String xpath2 = "(//*[text()=\"DH SCM Supplier Site Inactivation\"])[1]/following::*[text()=\"Scheduled Orchestration\"]/following::*[@title=\"Run\"][1]";
				String xpath = xpath1 + ";" + xpath2;
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);
			logger.error(e.getMessage());
		}

		/*
		 * try {
		 * if(param1.equalsIgnoreCase("DH Food Ingredients Stock Depletion Integration")
		 * && param2.equalsIgnoreCase("Run")) { Thread.sleep(3000); Actions action = new
		 * Actions(driver); WebElement we = driver.findElement(By.
		 * xpath("(//*[text()=\"DH Food Ingredients Stock Depletion Integration\"])[1]"));
		 * action.moveToElement(we).perform(); Thread.sleep(5000); WebElement run =
		 * driver.findElement(By.
		 * xpath("(//*[text()=\"DH Food Ingredients Stock Depletion Integration\"])[1]/following::*[@title=\"Run\"][1]"
		 * )); run.click(); screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		 * Thread.sleep(5000); String scripNumber = fetchMetadataVO.getScriptNumber();
		 * log.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
		 * String xpath =
		 * "(//*[text()=\"DH Food Ingredients Stock Depletion Integration\"])[1]/following::*[@title=\"Run\"][1]"
		 * ; String scriptID=fetchMetadataVO.getScriptId();String
		 * metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(
		 * scriptID,metadataID,xpath); return; } }catch (Exception e) { String
		 * scripNumber = fetchMetadataVO.getScriptNumber();
		 * log.error("Failed during clickButton" + scripNumber); logger.error(e.getMessage());
		 * }
		 */
		try {
			if (param1.equalsIgnoreCase("DH Food Ingredients Stock Depletion Integration")
					&& param2.equalsIgnoreCase("Run")) {
				Thread.sleep(3000);
				Actions action = new Actions(driver);
				WebElement we = driver.findElement(By.xpath(
						"(//*[text()=\"DH Food Ingredients Stock Depletion Integration\"])[1]/following::*[text()=\"Scheduled Orchestration\"]"));
				action.moveToElement(we).perform();
				Thread.sleep(5000);
				WebElement run = driver.findElement(By.xpath(
						"(//*[text()=\"DH Food Ingredients Stock Depletion Integration\"])[1]/following::*[text()=\"Scheduled Orchestration\"]/following::*[@title=\"Run\"][1]"));
				run.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath1 = "(//*[text()=\"DH Food Ingredients Stock Depletion Integration\"])[1]/following::*[text()=\"Scheduled Orchestration\"]";
				String xpath2 = "(//*[text()=\"DH Food Ingredients Stock Depletion Integration\"])[1]/following::*[text()=\"Scheduled Orchestration\"]/following::*[@title=\"Run\"][1]";
				String xpath = xpath1 + ";" + xpath2;
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);
			logger.error(e.getMessage());
		}

		try {

			if (param1.equalsIgnoreCase("DH Projects Cost for Contingent Workers") && param2.equalsIgnoreCase("Run")) {
				Thread.sleep(3000);
				Actions action = new Actions(driver);
				WebElement we = driver.findElement(By.xpath(
						"//*[contains(text(),\"Integration imports the Purchase requisitions\")]/following::*[text()=\"App Driven Orchestration\"]"));
				action.moveToElement(we).perform();
				Thread.sleep(5000);
				WebElement run = driver.findElement(By.xpath(
						"//*[contains(text(),\"Integration imports the Purchase requisitions\")]/following::*[text()=\"App Driven Orchestration\"]/following::*[@title=\"Run\"]"));
				run.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath1 = "//*[contains(text(),\"Integration imports the Purchase requisitions\")]/following::*[text()=\"App Driven Orchestration\"]";
				String xpath2 = "//*[contains(text(),\"Integration imports the Purchase requisitions\")]/following::*[text()=\"App Driven Orchestration\"]/following::*[@title=\"Run\"]";
				String xpath = xpath1 + ";" + xpath2;
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);
			logger.error(e.getMessage());
		}

		try {
			if (param1.equalsIgnoreCase("Submit Now") && param2.equalsIgnoreCase("Submit Now")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver.findElement(
						By.xpath(("//*[text()=\"" + param1 + "\"]/following::span[text()=\"" + param2 + "\"]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				screenshot(driver, fetchMetadataVO, customerDetails);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(15000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath = "//*[text()=\"param1\"]/following::span[text()=\"param2\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Submit Now")
					|| param1.equalsIgnoreCase("Test") && param2.equalsIgnoreCase("")) {
				WebElement waittext = driver.findElement(By.xpath("//a[text()=\"" + param1 + "\"][1]"));// screenshot(driver,
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				screenshot(driver, fetchMetadataVO, customerDetails);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(15000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath = "//a[text()=\"param1\"][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			if (param1.equalsIgnoreCase("Back")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver
						.findElement(By.xpath(("//*[contains(@class,\"navigationlist-previous-icon\")][1]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				screenshot(driver, fetchMetadataVO, customerDetails);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(15000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath = "//*[contains(@class,\"navigationlist-previous-icon\")][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);
			logger.error(e.getMessage());
		}

		try {
			if (param1.equalsIgnoreCase("Close")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver.findElement(By.xpath(("//*[contains(@class,\"cross-icon\")][1]")));// screenshot(driver,
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				screenshot(driver, fetchMetadataVO, customerDetails);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(15000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath = "//*[contains(@class,\"cross-icon\")][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);
			logger.error(e.getMessage());
		}

		try {
			Thread.sleep(5000);
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			WebElement waittext = driver
					.findElement(By.xpath(("//*[text()=\"" + param1 + "\"]/following::*[text()=\"" + param2 + "\"][1]")));// screenshot(driver,
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			screenshot(driver, fetchMetadataVO, customerDetails);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			Thread.sleep(15000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
			String xpath = "//*[text()=\"param1+\"]/following::*[text()=\"param2\"][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			WebElement waittext = driver.findElement(By.xpath(("//*[@title=\"" + param1 + "\"][1]")));// screenshot(driver,
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			screenshot(driver, fetchMetadataVO, customerDetails);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			Thread.sleep(15000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
			String xpath = "//*[@title=\"param1\"][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			WebElement waittext = driver.findElement(By.xpath(("//*[@class=\"opaas-toolbar__search-icon\"][1]")));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			screenshot(driver, fetchMetadataVO, customerDetails);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			Thread.sleep(15000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
			String xpath = "//*[@class=\"opaas-toolbar__search-icon\"][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	/*
	 * public String oicSendValue(WebDriver driver, String param1, String param2,
	 * String keysToSend, ScriptDetailsDto fetchMetadataVO, FetchConfigVO
	 * fetchConfigVO) throws Exception {
	 * 
	 * try {
	 * 
	 * WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
	 * wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
	 * "//input[@placeholder=\""+param1+"\"]"))); WebElement waittill =
	 * driver.findElement(By.xpath("//input[@placeholder=\""+param1+"\"]")); Actions
	 * actions = new Actions(driver);
	 * actions.moveToElement(waittill).build().perform(); typeIntoValidxpath(driver,
	 * keysToSend, waittill, fetchConfigVO, fetchMetadataVO); screenshot(driver, "",
	 * fetchMetadataVO, fetchConfigVO); Thread.sleep(1000); String scripNumber =
	 * fetchMetadataVO.getScriptNumber();
	 * log.info("Sucessfully Clicked Close Date sendValue" + scripNumber); String
	 * xpath = "//input[@placeholder=\"param1\"]"; String
	 * scriptID=fetchMetadataVO.getScriptId();String
	 * metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(
	 * scriptID,metadataID,xpath); return keysToSend;
	 * 
	 * } catch (Exception e) { logger.error(e.getMessage()); String scripNumber =
	 * fetchMetadataVO.getScriptNumber(); log.error("Failed during sendValue" +
	 * scripNumber); screenshotFail(driver, "Failed during sendValue",
	 * fetchMetadataVO, fetchConfigVO); throw e; } }
	 * 
	 */

	public String oicSendValue(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("//input[@placeholder=\"" + param1 + "\"]")));
				WebElement waittill = driver.findElement(By.xpath("//input[@placeholder=\"" + param1 + "\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Close Date sendValue" + scripNumber);
				String xpath = "//input[@placeholder=\"param1\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return keysToSend;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during sendValue" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
		}
		try {
			if (param1.equals("Body") && param2.equalsIgnoreCase("Text")) {
				Thread.sleep(5000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(
						ExpectedConditions.presenceOfElementLocated(By.xpath("(//div[@class=\"CodeMirror-lines\"])[1]")));
				WebElement waittill = driver.findElement(By.xpath("//input[@placeholder=\"" + param1 + "\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Close Date sendValue" + scripNumber);
				String xpath = "//input[@placeholder=\"param1\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return keysToSend;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during sendValue" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
		}
		try {
			if (param1.equalsIgnoreCase("Request Submission")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By
						.xpath("//*[text()=\"" + param1 + "\"]/following::*[text()=\"" + param2 + "\"]/following::a[1]")));
				WebElement waittill = driver.findElement(
						By.xpath("//*[text()=\"" + param1 + "\"]/following::*[text()=\"" + param2 + "\"]/following::a[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				actions.click(waittill).perform();
				// clickValidateXpath(driver, fetchMetadataVO, waittill, fetchConfigVO);
				// waittill.click();
				Thread.sleep(3000);
				WebElement selectValue = driver.findElement(By.xpath("//span[text()=\"" + keysToSend + "\"]"));
				actions.moveToElement(selectValue).build().perform();
				actions.click(selectValue).perform();
				// clickValidateXpath(driver, fetchMetadataVO, selectValue, fetchConfigVO);
				// selectValue.click();
				/*
				 * JavascriptExecutor jse = (JavascriptExecutor) driver;
				 * jse.executeScript("arguments[0].value=\"" + keysToSend + "\";", waittill);
				 * tab(driver, fetchMetadataVO, fetchConfigVO);
				 */
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Close Date sendValue" + scripNumber);
				String xpath1 = "//*[text()=\"param1\"]/following::*[text()=\"param2\"]/following::a[1]";
				String xpath2 = "//span[text()=\"keysToSend\"]";
				String xpath = xpath1 + ";" + xpath2;
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return keysToSend;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during sendValue" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//*[text()=\"" + param1 + "\"]/following::*[text()=\"" + param2 + "\"]/following::a[1]")));
			WebElement waittill = driver.findElement(
					By.xpath("//*[text()=\"" + param1 + "\"]/following::*[text()=\"" + param2 + "\"]/following::a[1]"));
			Actions actions = new Actions(driver);
			actions.click(waittill).perform();
			Thread.sleep(3000);
			WebElement selectValue = driver.findElement(By.xpath("//span[text()=\"" + keysToSend + "\"]"));
			actions.click(selectValue).perform();
			screenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked Close Date sendValue" + scripNumber);
			String xpath1 = "//*[text()=\"param1\"]/following::*[text()=\"param2\"]/following::a[1]";
			String xpath2 = "//span[text()=\"keysToSend\"]";
			String xpath = xpath1 + ";" + xpath2;
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return keysToSend;
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during sendValue" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(),\"" + param1
					+ "\")]/following::*[text()=\"" + param2 + "\"]/following::input[1]")));
			WebElement waittill = driver.findElement(By.xpath("//*[contains(text(),\"" + param1
					+ "\")]/following::*[text()=\"" + param2 + "\"]/following::input[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			screenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked Close Date sendValue" + scripNumber);
			String xpath = "//*[contains(text(),\"param1\")]/following::*[text()=\"param2\"]/following::input[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return keysToSend;
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during sendValue" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void oicMouseHover(WebDriver driver, String param1, String param2,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {

		try {
			if (param1.equalsIgnoreCase("text")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Actions actions = new Actions(driver);
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[contains(text(), \"" + param1 + "\")]/following::*[text()=\"Add Item\"]")));
				scrollUsingElement(driver, param1, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(6000);
				WebElement waittext = driver.findElement(
						By.xpath("//*[contains(text(), \"" + param1 + "\")]/following::*[text()=\"Add Item\"]"));
				actions.moveToElement(waittext).build().perform();
				clickImage(driver, param2, param1, fetchMetadataVO, fetchConfigVO, customerDetails);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[contains(text(), \"param1\")]/following::*[text()=\"Add Item\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				logger.info("Sucessfully Clicked mousehover" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during mousehover" + scripNumber);
			logger.error(e.getMessage());
		}

		try {

			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()=\"" + param1 + "\"]")));

			WebElement waittill = driver.findElement(By.xpath("//*[text()=\"" + param1 + "\"]"));

			Actions actions = new Actions(driver);

			actions.moveToElement(waittill).perform();
			String xpath = "//*[text()=\"param1\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

		} catch (Exception e) {

			logger.error(e.getMessage());

			String scripNumber = fetchMetadataVO.getScriptNumber();

			logger.error("Failed during Mouse movement" + scripNumber);

			screenshotFail(driver, fetchMetadataVO, customerDetails);

			throw e;

		}

	}

	public void navigateOICUrl(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails) {
		try {
			driver.navigate().to(fetchConfigVO.getOIC_APPLICATION_URL());
			driver.manage().window().maximize();
			Thread.sleep(4000);
			WebElement iframe = driver.findElement(By.xpath("//iframe[@title=\"TrustArc Cookie Consent Manager\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(iframe).build().perform();
			driver.switchTo().frame(iframe);
			WebElement Acceptall = driver.findElement(By.xpath("//a[text()=\"Accept all\"]"));
			Acceptall.click();
			Thread.sleep(2000);
			// deleteAllCookies(driver, fetchMetadataVO, fetchConfigVO);
			// refreshPage(driver, fetchMetadataVO, fetchConfigVO);
			switchToActiveElement(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(10000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed to logout " + scripNumber);
			String xpath1 = "//iframe[@title=\"TrustArc Cookie Consent Manager\"]";
			String xpath2 = "//a[text()=\"Accept all\"]";
			String xpath = xpath1 + ";" + xpath2;
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("failed to do navigate URl " + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
		}
	}

	public void navigateOICJobUrl(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails) {
		try {
			driver.navigate().to(fetchConfigVO.getOIC_JOB_SCHEDULER());
			driver.manage().window().maximize();
			Thread.sleep(4000);
			WebElement iframe = driver.findElement(By.xpath("//iframe[@title=\"TrustArc Cookie Consent Manager\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(iframe).build().perform();
			driver.switchTo().frame(iframe);
			WebElement Acceptall = driver.findElement(By.xpath("//a[text()=\"Accept all\"]"));
			Acceptall.click();
			Thread.sleep(2000);
			// deleteAllCookies(driver, fetchMetadataVO, fetchConfigVO);
			// refreshPage(driver, fetchMetadataVO, fetchConfigVO);
			switchToActiveElement(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(10000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed to logout " + scripNumber);
			String xpath1 = "//iframe[@title=\"TrustArc Cookie Consent Manager\"]";
			String xpath2 = "//a[text()=\"Accept all\"]";
			String xpath = xpath1 + ";" + xpath2;
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("failed to do navigate URl " + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
		}
	}

	@Override
	public String loginToExcel(WebDriver driver, String param1, String param2, String username, String password,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
		// TODO Auto-generated method stub
		String s = "    Login To Excel    " + username + "    " + password;
		return s;
	}

	@Override
	public Integer addRow(Integer addrow) throws Exception {
		// TODO Auto-generated method stub
		int a = addrow.intValue();
		a++;
		return a;

	}

	@Override
	public String menuItemOfExcel(WebDriver driver, String param1, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO) throws Exception {
		// TODO Auto-generated method stub
		String s = "    Select Menu Item Of Excel    " + param1;
		return s;
	}

	@Override
	public String closeExcel() throws Exception {
		// TODO Auto-generated method stub
		String s = "    Close Excel";
		return s;
	}

	@Override
	public List<String> openExcelFileWithSheet(WebDriver driver, String param1, String param2, String fileName,
			String sheetName, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		// TODO Auto-generated method stub
		List<String> openExcelSteps = new ArrayList<String>();
		String s = "*** Settings ***";
		openExcelSteps.add(s);
		s = "Library    RPA.Desktop";
		openExcelSteps.add(s);
		s = "Library    RPA.Desktop.Windows";
		openExcelSteps.add(s);

		s = "Library    Screenshot";
		openExcelSteps.add(s);

		s = "Library    OperatingSystem";
		openExcelSteps.add(s);

		s = "Resource    C:\\\\EBS-Automation\\\\EBS Automation-POC\\\\robot files\\\\CustomKeywords.robot";
		openExcelSteps.add(s);

		s = "Variables    C:\\\\EBS-Automation\\\\EBS Automation-POC\\\\robot files\\\\excelinfo.yaml";
		openExcelSteps.add(s);

		s = "*** Tasks ***";
		openExcelSteps.add(s);
		s = "Create Journal Entry";
		openExcelSteps.add(s);
		s = "    [Setup]    Set Automation Speed    slow";
		openExcelSteps.add(s);
		s = "    [TearDown]    Capture And Upload Screenshot    C:\\\\EBS-Automation\\\\WATS_Files\\\\screenshot\\\\excel\\\\WATS\\\\"
				+ customerDetails.getTestSetName() + "    " + fetchMetadataVO.getSeqNum();
		openExcelSteps.add(s);

		s = "    OperatingSystem.Create Directory    C:\\\\EBS-Automation\\\\WATS_Files\\\\screenshot\\\\excel\\\\WATS\\\\"
				+ customerDetails.getTestSetName();
		openExcelSteps.add(s);

		s = "    Open Excel File With Sheet    " + fileName + "    " + sheetName;
		openExcelSteps.add(s);
		return openExcelSteps;

	}

	@Override
	public String typeIntoCell(WebDriver driver, String param1, String value1, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, Integer addrowCounter) throws Exception {
		// TODO Auto-generated method stub
		String s = "";
		if (addrowCounter > 1) {
			s = "    Type Into Cell    " + param1 + "    " + value1 + "    " + addrowCounter;
		} else {
			s = "    Type Into Cell    " + param1 + "    " + value1;
		}
		return s;
	}

	public void loginInformaticaApplication(WebDriver driver, FetchConfigVO fetchConfigVO,
			ScriptDetailsDto fetchMetadataVO, String type1, String type2, String type3, String param1, String param2,
			String param3, String keysToSend, String value, CustomerProjectDto customerDetails) throws Exception {
		String param4 = "username";
		String param5 = "password";
		// String param6 = "Sign In";
		navigateInformaticaUrl(driver, fetchConfigVO, fetchMetadataVO, customerDetails);
		String xpath1 = InformaticaLoginPage(driver, param4, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
		String xpath2 = this.InformaticaLoginPage(driver, param5, value, fetchMetadataVO, fetchConfigVO, customerDetails);
		if (xpath2.equalsIgnoreCase(null)) {
			throw new IOException("Failed during login page");
		}
		String scripNumber = fetchMetadataVO.getScriptNumber();
		String xpath = xpath1 + ";" + xpath2;
		String scriptID = fetchMetadataVO.getScriptId();
		String lineNumber = fetchMetadataVO.getLineNumber();
		service.saveXpathParams(scriptID, lineNumber, xpath);
	}

	public void navigateInformaticaUrl(WebDriver driver, FetchConfigVO fetchConfigVO,
			ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails) {
		try {
			driver.navigate().to(fetchConfigVO.getINFORMATICA_APPLICATION_URL());
			driver.manage().window().maximize();
			Thread.sleep(4000);

			// deleteAllCookies(driver, fetchMetadataVO, fetchConfigVO);
			// refreshPage(driver, fetchMetadataVO, fetchConfigVO);
			switchToActiveElement(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(10000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed to logout " + scripNumber);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("failed to do navigate URl " + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
		}
	}

	public String InformaticaLoginPage(WebDriver driver, String param1, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {
		String xpath = null;
		try {
			if (param1.equalsIgnoreCase("Password")) {
				String title1 = driver.getTitle();
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"" + param1 + "\"]/input")));

				WebElement waittill = driver.findElement(By.xpath("//*[@id=\"" + param1 + "\"]/input"));

				// JavascriptExecutor jse = (JavascriptExecutor) driver;
				// jse.executeScript("document.getElementById(\"idcs-signin-basic-signin-form-password|input\").value
				// = \"" + keysToSend + "\";");
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				waittill.sendKeys(keysToSend);
				// typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO,
				// fetchMetadataVO);
				// if("password".equalsIgnoreCase(param1))
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
				String title2 = driver.getTitle();
				/*
				 * if(title1.equalsIgnoreCase(title2)) { screenshotFail(driver,
				 * "Failed During Login page", fetchMetadataVO, fetchConfigVO); throw new
				 * IOException("Failed during login page"); }
				 */
				// screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Succesfully password is entered " + scripNumber);
				xpath = "//*[@id=\"param1\"]/input";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return xpath;
			}
		} catch (Exception e) {
			screenshotFail(driver, fetchMetadataVO, customerDetails);

			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed to enter password " + scripNumber);
			logger.error(e.getMessage());
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"" + param1 + "\"]/input")));
			WebElement waittill = driver.findElement(By.xpath("//*[@id=\"" + param1 + "\"]/input"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			waittill.sendKeys(keysToSend);

			// typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO,
			// fetchMetadataVO);
			// JavascriptExecutor jse = (JavascriptExecutor) driver;
			// jse.executeScript("arguments[0].value=\"" + keysToSend + "\";", waittill);
			// if("password".equalsIgnoreCase(param1))
			// screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			Thread.sleep(1000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			xpath = "//*[@id=\"param1\"]/input";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			logger.info("Successfully entered User Name " + scripNumber);
			return xpath;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Failed during login page " + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
		}
		return xpath;
	}

	public void InformaticaClickButton(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {

		try {

			if (param1.equalsIgnoreCase("Explore")) {
				Thread.sleep(3000);
				Actions action = new Actions(driver);
				// WebElement we = driver.findElement(By.xpath("(//*[text()=\"Scheduled
				// Orchestration\"]/following::*[@title=\"Run\"])[1]"));
				WebElement we = driver.findElement(By.xpath("(//*[text()=\"" + param1 + "\"])[1]"));
				action.moveToElement(we).perform();
				Thread.sleep(5000);
				WebElement run = driver.findElement(By.xpath("(//*[text()=\"" + param1 + "\"])[1]"));
				run.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath1 = "(//*[text()=\"param1\"])[1]";
				String xpath2 = "(//*[text()=\"param1\"])[1]";
				String xpath = xpath1 + ";" + xpath2;
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			// throw e;
		}

		try {

			if (param1.equalsIgnoreCase("My Jobs")) {
				Thread.sleep(3000);
				Actions action = new Actions(driver);
				// WebElement we = driver.findElement(By.xpath("(//*[text()=\"Scheduled
				// Orchestration\"]/following::*[@title=\"Run\"])[1]"));
				WebElement we = driver.findElement(By.xpath("(//*[text()=\"" + param1 + "\"])[1]"));
				action.moveToElement(we).perform();
				Thread.sleep(5000);
				WebElement run = driver.findElement(By.xpath("(//*[text()=\"" + param1 + "\"])[1]"));
				run.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath1 = "(//*[text()=\"param1\"])[1]";
				String xpath2 = "(//*[text()=\"param1\"])[1]";
				String xpath = xpath1 + ";" + xpath2;
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			// throw e;
		}

		try {

			if (param1.equalsIgnoreCase("Run")) {
				Thread.sleep(3000);
				Actions action = new Actions(driver);
				// WebElement we = driver.findElement(By.xpath("(//*[text()=\"Scheduled
				// Orchestration\"]/following::*[@title=\"Run\"])[1]"));
				WebElement we = driver.findElement(By.xpath("//span[text()=\"" + param1 + "\"]"));
				action.moveToElement(we).perform();
				Thread.sleep(5000);
				WebElement run = driver.findElement(By.xpath("//span[text()=\"" + param1 + "\"]"));
				run.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath1 = "//span[text()=\"param1\"]";
				String xpath2 = "//span[text()=\"param1\"]";
				String xpath = xpath1 + ";" + xpath2;
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			// throw e;
		}

		try {

			Thread.sleep(3000);
			Actions action = new Actions(driver);
			// WebElement we = driver.findElement(By.xpath("(//*[text()=\"Scheduled
			// Orchestration\"]/following::*[@title=\"Run\"])[1]"));
			WebElement we = driver.findElement(By.xpath("//*[text()=\"" + param1 + "\"]"));
			action.moveToElement(we).perform();
			Thread.sleep(5000);
			WebElement run = driver.findElement(By.xpath("//*[text()=\"" + param1 + "\"]"));
			run.click();
			screenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(5000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
			String xpath1 = "//*[text()=\"param1\"]";
			String xpath2 = "//*[text()=\"param1\"]";
			String xpath = xpath1 + ";" + xpath2;
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during clickButton" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public String InformaticaSendValue(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			if (param1.equalsIgnoreCase("Find")) {
				Thread.sleep(1000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("(//input[@placeholder=\"" + param1 + "\"])[2]")));
				WebElement waittill = driver.findElement(By.xpath("(//input[@placeholder=\"" + param1 + "\"])[2]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				waittill.sendKeys(keysToSend);
				// typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO,
				// fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked Close Date sendValue" + scripNumber);
				String xpath = "(//input[@placeholder=\"param1\"])[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return keysToSend;
			}
			return null;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Close Date sendValue" + scripNumber);
			logger.error(e.getMessage());
			throw e;
		}

	}

	public void InformaticaclickLink(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {

			if (param1.equalsIgnoreCase("")) {

				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				wait.until(ExpectedConditions

						.presenceOfElementLocated(By.xpath("//a[contains(@id,\"" + param1 + "\")]")));

				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittext).build().perform();

				waittext.click();

				screenshot(driver, fetchMetadataVO, customerDetails);

				refreshPage(driver, fetchMetadataVO, fetchConfigVO, customerDetails);

				Thread.sleep(5000);

				String scripNumber = fetchMetadataVO.getScriptNumber();

				logger.info("Sucessfully Clicked Approve clickLink" + scripNumber);

				String xpath = "//*[normalize-space(text())=\"param1\"]";

				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;

			}

		} catch (Exception e) {

			logger.error(e.getMessage());

			String scripNumber = fetchMetadataVO.getScriptNumber();

			logger.error("Failed during Approve clickLink" + scripNumber);

		}

	}

	public void InformaticaSelectAValue(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			if (param1.equalsIgnoreCase("All Projects")) {
				Thread.sleep(4000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("(//*[text()=\"" + param1 + "\"])[1]/following::*[text()=\"" + keysToSend + "\"]")));
				WebElement waittext = driver.findElement(
						By.xpath("(//*[text()=\"" + param1 + "\"])[1]/following::*[text()=\"" + keysToSend + "\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				Thread.sleep(2000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked selectAValue" + scripNumber);
				String xpath = "(//*[text()=\"param1\"])[1]/following::*[text()=\"keysToSend\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during selectAValue" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}

	}

	public void InformaticaClickImage(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			if (param1.equalsIgnoreCase("Refresh")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@title=\"" + param1 + "\"]")));
				WebElement waittext = driver.findElement(By.xpath("//button[@title=\"" + param1 + "\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// waittext.click();
				// highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(45000);
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				String xpath = "//button[@title=\"param1\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during Informatica Click Image " + e.getMessage());
			throw e;
		}

	}

	public void InformaticaLogout(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			String type1, String type2, String type3, String param1, String param2, String param3, CustomerProjectDto customerDetails) throws Exception {

		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@title=\"User\"]")));
			WebElement waittext = driver.findElement(By.xpath("//button[@title=\"User\"]"));
			waittext.click();
			Thread.sleep(4000);
			screenshot(driver, fetchMetadataVO, customerDetails);
			WebElement signout = driver.findElement(By.xpath("(//*[text()=\"Log Out\"])[3]"));
			signout.click();
			String xpath1 = "//button[@title=\"User\"]";
			String xpath2 = "(//*[text()=\"Log Out\"])[3]";
			String xpath = xpath1 + ";" + xpath2;
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error(e.getMessage());
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed to logout " + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}

	}

	@Override
	public void waitTillLoad(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO) {
		try {
			Thread.sleep(Integer.parseInt(fetchMetadataVO.getInputValue()));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public String uploadObjectToObjectStore(String sourceFilePath, String destinationFilePath) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void compareValue(WebDriver driver, String input_parameter, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, String globalValueForSteps2, CustomerProjectDto customerDetails) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void apiAccessToken(ScriptDetailsDto fetchMetadataVO, Map<String, String> accessTokenStorage, CustomerProjectDto customerDetails)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void navigateToBackPage(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void openPdf(WebDriver driver, String input_value, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void openFile(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionApprove(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createDriverFailedPdf(List<ScriptDetailsDto> fetchMetadataListVO, FetchConfigVO fetchConfigVO,
			String pdffileName, ApiValidationVO api, boolean validationFlag, CustomerProjectDto customerDetails)
			throws IOException, DocumentException, com.lowagie.text.DocumentException {
		// TODO Auto-generated method stub
		
	}
	public void loginSFApplication(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			String type1, String type2, String type3, String param1, String param2, String param3, String keysToSend,
			String value, CustomerProjectDto customerDetails) throws Exception {
		
	}

	@Override
	public void switchToParentWindowWithoutPdf(WebDriver driver, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
