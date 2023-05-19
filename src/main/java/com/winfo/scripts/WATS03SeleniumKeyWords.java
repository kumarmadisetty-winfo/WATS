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
import org.openqa.selenium.By;
//import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
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

import com.lowagie.text.DocumentException;
import com.winfo.service.SeleniumKeyWordsInterface;
import com.winfo.serviceImpl.AbstractSeleniumKeywords;
import com.winfo.serviceImpl.DataBaseEntry;
import com.winfo.serviceImpl.DynamicRequisitionNumber;
import com.winfo.serviceImpl.FetchConfigVO;
import com.winfo.serviceImpl.LimitScriptExecutionService;
import com.winfo.serviceImpl.ScriptXpathService;
import com.winfo.utils.StringUtils;
import com.winfo.vo.ApiValidationVO;
import com.winfo.vo.CustomerProjectDto;
import com.winfo.vo.ScriptDetailsDto;

@Service("WATS03")
//@Service("WATS")
@RefreshScope
public class WATS03SeleniumKeyWords extends AbstractSeleniumKeywords implements SeleniumKeyWordsInterface {
//New-changes - added annotation for DatabaseEntry
	@Autowired
	private DataBaseEntry databaseentry;
	@Autowired
	ScriptXpathService service;
	@Autowired
	DynamicRequisitionNumber dynamicnumber;
	@Autowired
	LimitScriptExecutionService limitScriptExecutionService;

	@Value("${configvO.watslogo}")
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
	Logger log = Logger.getLogger("Logger");

	public String Main_Window = "";
	public WebElement fromElement;
	public WebElement toElement;

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
		String xpath = xpath1 + ";" + xpath2;
		String scriptID = fetchMetadataVO.getScriptId();
		String lineNumber = fetchMetadataVO.getLineNumber();
		service.saveXpathParams(scriptID, lineNumber, xpath);
//		sendValue(driver, param1, param3, keysToSend, fetchMetadataVO, fetchConfigVO);
//		sendValue(driver, param5, param2, value, fetchMetadataVO, fetchConfigVO);
//		clickSignInSignOut(driver, param6, fetchMetadataVO, fetchConfigVO, customerDetails);
//		clickButton(driver, param6, param2, fetchMetadataVO, fetchConfigVO);
	}

	public synchronized void navigate(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			String type1, String type2, String param1, String param2, String param3, int count, CustomerProjectDto customerDetails)
			throws Exception {
		String param = "Navigator";
		String xpath = navigator(driver, param, fetchMetadataVO, fetchConfigVO, customerDetails);
		String xpath1 = menuNavigation(driver, param1, fetchMetadataVO, fetchConfigVO, customerDetails);

		String xpath2 = menuNavigationButton(driver, fetchMetadataVO, fetchConfigVO, type1, type2, param1, param2,
				count, customerDetails);
		String scripNumber = fetchMetadataVO.getScriptNumber();
		String xpaths = xpath + ">" + xpath1 + ">" + xpath2;
		String scriptID = fetchMetadataVO.getScriptId();
		String lineNumber = fetchMetadataVO.getLineNumber();
		service.saveXpathParams(scriptID, lineNumber, xpath);
//		clickLink(driver, param3, param2, fetchMetadataVO, fetchConfigVO);
//		clickMenu(driver, param1, param2, fetchMetadataVO, fetchConfigVO);
//		clickButton(driver, param2, param2, fetchMetadataVO, fetchConfigVO);
	}

	public synchronized void openTask(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			String type1, String type2, String param1, String param2, int count, CustomerProjectDto customerDetails)
			throws Exception {
		String param3 = "Tasks";
//		clickImage(driver, param3, param2, fetchMetadataVO, fetchConfigVO);
//		clickLink(driver, param1, param2, fetchMetadataVO, fetchConfigVO, customerDetails);
		String xpath = task(driver, param3, fetchMetadataVO, fetchConfigVO, customerDetails);
		String xpath1 = taskMenu(driver, fetchMetadataVO, fetchConfigVO, type1, type2, param1, param2, count,
				customerDetails);
		String xpaths = xpath + ";" + xpath1;
		String scripNumber = fetchMetadataVO.getScriptNumber();
		String scriptID = fetchMetadataVO.getScriptId();
		String lineNumber = fetchMetadataVO.getLineNumber();
		service.saveXpathParams(scriptID, lineNumber, xpath);

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

	public void logoutDropdown(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			String param1, CustomerProjectDto customerDetails) throws Exception {

		try {
			Thread.sleep(4000);
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@id,'UISpb2')]")));
			WebElement waittext = driver.findElement(By.xpath("//div[contains(@id,'UISpb2')]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(4000);
			try {
				if (driver.findElement(By.xpath("//div[contains(@id,'popup-container')]//a[text()='Sign Out']"))
						.isDisplayed()) {
					WebElement signout = driver
							.findElement(By.xpath("//div[contains(@id,'popup-container')]//a[text()='Sign Out']"));
					signout.click();
					Thread.sleep(4000);
				}
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Successfully Logout is done " + scripNumber);
			} catch (Exception e) {
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(4000);
				WebElement signout = driver
						.findElement(By.xpath("//div[contains(@id,'popup-container')]//a[text()='Sign Out']"));
				signout.click();
				Thread.sleep(4000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.error("Successfully Logout is done " + scripNumber);
			}
			return;
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed to logout " + scripNumber);
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
					.presenceOfElementLocated(By.xpath("//td[@data-afr-adfday='cm' and text()='" + param1 + "']")));
			wait.until(ExpectedConditions
					.elementToBeClickable(By.xpath("//td[@data-afr-adfday='cm' and text()='" + param1 + "']")));
			WebElement waittext = driver
					.findElement(By.xpath("//td[@data-afr-adfday='cm' and text()='" + param1 + "']"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			actions.moveToElement(waittext).click().build().perform();
			screenshot(driver, fetchMetadataVO, customerDetails);
			return;
		} catch (Exception e) {
			log.error("Failed During Navigation");
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			System.out.println("Not able to navitage to the Url");
			throw e;
		}
	}

	public void selectMonth(WebDriver driver, String param1, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			WebElement waittext = driver.findElement(By.xpath("//*[text()='Create Time Card']/following::select[1]"));
			selectMethod(driver, param1, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			screenshot(driver, fetchMetadataVO, customerDetails);
			return;
		} catch (Exception e) {
			log.error("Failed During Navigation");
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			System.out.println("Not able to navitage to the :" + "" + param1);
			throw e;
		}
	}

	public void selectYear(WebDriver driver, String param1, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			WebElement wait = driver.findElement(By.xpath("//*[text()='Select Year']/preceding-sibling::input[1]"));
			String yearValue = wait.getAttribute("title");
			int year = Integer.parseInt(yearValue);
			System.out.println("The value of the year is: " + year);
			if (year < Integer.parseInt(param1)) {
				while (year != Integer.parseInt(param1)) {
					WebElement increment = driver.findElement(By.xpath("//a[@title='increment']"));
					increment.click();
					year = year + 1;
					System.out.println(year);
				}
			} else if (year > Integer.parseInt(param1)) {
				while (year != Integer.parseInt(param1)) {
					WebElement decrement = driver.findElement(By.xpath("//a[@title='decrement']"));
					decrement.click();
					year = year - 1;
					System.out.println(year);
				}
			} else {
				System.out.println("The given year is matched with the Oracle year");
			}
			screenshot(driver, fetchMetadataVO, customerDetails);
			return;
		} catch (Exception e) {
			log.error("Failed During Navigation");
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			System.out.println("Not able to navitage to the :" + "" + param1);
			throw e;
		}
	}

	public void openSettings(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			String type1, String type2, String type3, String param1, String param2, String param3,
			CustomerProjectDto customerDetails) throws Exception {
		String param4 = "UIScmil1u";
		clickLink(driver, param4, param3, fetchMetadataVO, fetchConfigVO, customerDetails);
		clickLink(driver, param1, param2, fetchMetadataVO, fetchConfigVO, customerDetails);
		// clickSignInSignOut(driver, param6, fetchMetadataVO, fetchConfigVO,
		// customerDetails);
//		clickButton(driver, param6, param2, fetchMetadataVO, fetchConfigVO);
	}

	public void navigateUrl(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			CustomerProjectDto customerDetails) {
		try {
			driver.navigate().to(fetchConfigVO.getApplication_url());
			driver.manage().window().maximize();
			deleteAllCookies(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			refreshPage(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			switchToActiveElement(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed to logout " + scripNumber);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("failed to do navigate URl " + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			System.out.println("Not able to navitage to the Url");
		}
	}

	public String loginPage(WebDriver driver, String param1, String keysToSend, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {
		String xpath = null;
		try {
			if (param1.equalsIgnoreCase("password")) {
				String title1 = driver.getTitle();
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='" + param1 + "']")));
				JavascriptExecutor jse = (JavascriptExecutor) driver;
				jse.executeScript("document.getElementById('password').value = '" + keysToSend + "';");
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
				// screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Succesfully password is entered " + scripNumber);
				xpath = "//input[@type='param1']";
				return xpath;
			}
		} catch (Exception e) {
			screenshotFail(driver, fetchMetadataVO, customerDetails);

			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed to enter password " + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("//*[contains(@placeholder,'" + param1 + "')]")));
			WebElement waittill = driver.findElement(By.xpath("//*[contains(@placeholder,'" + param1 + "')]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("arguments[0].value='" + keysToSend + "';", waittill);
			// if("password".equalsIgnoreCase(param1))
			// screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(1000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			xpath = "//*[contains(@placeholder,'param1')]";
			log.info("Successfully entered data " + scripNumber);
			return xpath;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Failed during login page " + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			System.out.println("Failed During Login page");
		}
		return xpath;
	}

	public String navigator(WebDriver driver, String param1, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			Thread.sleep(4000);
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@title='" + param1 + "']")));
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@title='" + param1 + "']")));
			WebElement waittext = driver.findElement(By.xpath("//a[@title='" + param1 + "']"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			actions.moveToElement(waittext).click().build().perform();
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Successfully navigator is done " + scripNumber);
			String xpath = "//a[@title='param1']";
			return xpath;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during navigator " + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			System.out.println("Not able to navitage to the Url");
			throw e;
		}
	}

	public String menuNavigation(WebDriver driver, String param1, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			if (param1.equalsIgnoreCase("Expenses")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("(//*[contains(@id,'popup-container')]//*[@title='" + param1 + "'])[2]")));
				wait.until(ExpectedConditions.elementToBeClickable(
						By.xpath("(//*[contains(@id,'popup-container')]//*[@title='" + param1 + "'])[2]")));
				WebElement waittext = driver
						.findElement(By.xpath("(//*[contains(@id,'popup-container')]//*[@title='" + param1 + "'])[2]"));
				Actions actions = new Actions(driver);
				Thread.sleep(3000);
				actions.moveToElement(waittext).build().perform();
				actions.moveToElement(waittext).click().build().perform();
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Successfully MenuNavigation is done " + scripNumber);
				String xpath = "(//*[contains(@id,'popup-container')]//*[@title='param1'])[2]";

				return xpath;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during MenuNavigation " + scripNumber);
			// TODO: handle exception
		}

		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//*[contains(@id,'popup-container')]//*[@title='" + param1 + "']")));
			wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("//*[contains(@id,'popup-container')]//*[@title='" + param1 + "']")));
			/*
			 * WebElement waittext = driver
			 * .findElement(By.xpath("//*[contains(@id,'popup-container')]//*[@title='" +
			 * param1 + "']"));
			 */

			// ------------------------(New Change)-----------------------

			WebElement waittext = driver.findElement(By.xpath(
					"//*[contains(@id,'popup-container')]//*[@title='" + param1 + "']//div[2]/a/*[name()='svg'][1]"));

			WebElement showmore = driver
					.findElement(By.xpath("//*[contains(@id,'popup-container')]//a[text()='Show More']"));
			Actions actions = new Actions(driver);
			actions.moveToElement(showmore).build().perform();
			actions.moveToElement(showmore).click().build().perform();
			Thread.sleep(15000);
			WebElement showless = driver
					.findElement(By.xpath("//*[contains(@id,'popup-container')]//a[text()='Show Less']"));
			actions.moveToElement(showless).build().perform();
			actions.moveToElement(showless).click().build().perform();
			Thread.sleep(15000);
			actions.moveToElement(waittext).build().perform();
			actions.moveToElement(waittext).click().build().perform();
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Successfully menunavigation is clicked " + scripNumber);
			String xpath = "//*[contains(@id,'popup-container')]//a[text()='Show More']" + ">"
					+ "//*[contains(@id,'popup-container')]//a[text()='Show Less']";
			log.info("Successfully menunavigation is clicked " + scripNumber);
			return xpath;

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Menunavigation " + scripNumber);

			screenshotFail(driver, fetchMetadataVO, customerDetails);
			System.out.println("Not able to navitage to the :" + "" + param1);
			throw e;
		}
	}

	public String menuNavigationButton(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			String type1, String type2, String param1, String param2, int count, CustomerProjectDto customerDetails)
			throws Exception {
		String xpath = null;
		try {
			Thread.sleep(5000);
			if (param2.equalsIgnoreCase("Assets")) {
				WebElement asset = driver.findElement(By.xpath(
						"//span[normalize-space(text())='Fixed Assets']/following::span[normalize-space(text())='"
								+ param2 + "']"));
				Actions actions = new Actions(driver);
				actions.moveToElement(asset).build().perform();
				actions.moveToElement(asset).click().build().perform();
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Successfully menuNavigationButton is done " + scripNumber);
				xpath = "//span[normalize-space(text())='Fixed Assets']/following::span[normalize-space(text())='param2']";
				log.info("Successfully menuNavigationButton is done " + scripNumber);
				return xpath;

			} else {
				// try {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"//div[@style='visibility: visible;']//span[normalize-space(text())='" + param2 + "']")));
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
						"//div[@style='visibility: visible;']//span[normalize-space(text())='" + param2 + "']")));
				WebElement waittext = driver.findElement(By
						.xpath("//div[@style='visibility: visible;']//span[normalize-space(text())='" + param2 + "']"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				actions.moveToElement(waittext).click().build().perform();
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Successfully menuNavigationButton is done " + scripNumber);
				xpath = "//div[@style='visibility: visible;']//span[normalize-space(text())='" + param1 + "']";
				log.info("Successfully menuNavigationButton is done " + scripNumber);
				return xpath;
			}
		} catch (Exception e) {
			if (count == 0) {
				count = 1;
				System.out.println(" The Count Value is : " + count);
				navigate(driver, fetchConfigVO, fetchMetadataVO, type1, type2, param1, param2, null, count, customerDetails);
			} else if (count <= 10) {
				count = count + 1;
				System.out.println(" The Count Value is : " + count);
				navigate(driver, fetchConfigVO, fetchMetadataVO, type1, type2, param1, param2, null, count, customerDetails);
			} else {
				System.out.println("Count value exceeds the limit");
				log.error("Failed During Navigation");
				screenshotFail(driver, fetchMetadataVO, customerDetails);
				System.out.println("Not able to navitage to the :" + "" + param1);
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
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@title='" + param1 + "']")));
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@title='" + param1 + "']")));
			WebElement waittext = driver.findElement(By.xpath("//img[@title='" + param1 + "']"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Successfully task is open " + scripNumber);
			String xpath = "//img[@title='param1']";
			log.info("Successfully task is open " + scripNumber);
			return xpath;

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Failed During Task " + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			System.out.println("Failed to Open Task Menu");
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
					By.xpath("//div[contains(@class,'AFVertical')]//a[normalize-space(text())='" + param1 + "']")));
			wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("//div[contains(@class,'AFVertical')]//a[normalize-space(text())='" + param1 + "']")));
			WebElement waittext = driver.findElement(
					By.xpath("//div[contains(@class,'AFVertical')]//a[normalize-space(text())='" + param1 + "']"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(5000);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Successfully open Task " + scripNumber);
			xpath = "//div[contains(@class,'AFVertical')]//a[normalize-space(text())='param1']";

			log.info("Successfully open Task " + scripNumber);
			return xpath;

		} catch (Exception e) {
			if (count == 0) {
				count = 1;
				System.out.println(" The Count Value is : " + count);
				openTask(driver, fetchConfigVO, fetchMetadataVO, type1, type2, param1, param2, count, customerDetails);
			} else if (count <= 10) {
				count = count + 1;
				System.out.println(" The Count Value is : " + count);
				openTask(driver, fetchConfigVO, fetchMetadataVO, type1, type2, param1, param2, count, customerDetails);
			} else {
				System.out.println("Count value exceeds the limit");
				log.error("Failed to Open Task Menu");
				screenshotFail(driver, fetchMetadataVO, customerDetails);
				System.out.println("Failed to Open Task Menu");
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
			log.info("Successfully waited for 4 seconds " + scripNumber);
		} catch (InterruptedException e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed During meduim wait" + scripNumber);
			e.printStackTrace();
			// Restore interrupted state...
			Thread.currentThread().interrupt();
		}
	}

	public void shortwait(WebDriver driver, String inputData) {
		try {
			int time = StringUtils.convertStringToInteger(inputData, 2);
			int seconds = time * 1000;
			log.info("Successfully shortwait");
			Thread.sleep(seconds);
		} catch (InterruptedException e) {
			log.error("Failed During shortwait");
			e.printStackTrace();
			// Restore interrupted state...
			Thread.currentThread().interrupt();
		}
	}

	public void wait(WebDriver driver, String inputData) {
		try {
			int time = StringUtils.convertStringToInteger(inputData, 8);
			int seconds = time * 1000;
			log.info("Successfully wait");
			Thread.sleep(seconds);
		} catch (InterruptedException e) {
			log.error("Failed During wait");
			e.printStackTrace();
			// Restore interrupted state...
			Thread.currentThread().interrupt();
		}
	}

	public void convertJPGtoMovie(String targetFile1, List<String> targetFileList,
			List<ScriptDetailsDto> fetchMetadataListVO, FetchConfigVO fetchConfigVO, String name,
			CustomerProjectDto customerDetails) {
		String vidPath = (fetchConfigVO.getPdf_path() + customerDetails.getCustomerName() + "/"
				+ customerDetails.getTestSetName() + "/" + name);
		// String vidPath="C:\\Testing\\ReportWinfo\\"+name;
		String Folder = (fetchConfigVO.getPdf_path() + customerDetails.getCustomerName() + "/"
				+ customerDetails.getTestSetName() + "/");
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
				System.out.println(targetFile1);
				str = targetFile1;
				ipl = cvLoadImage(str);
				recorder.record(grabberConverter.convert(ipl));
			}
			for (String image : targetFileList) {
				System.out.println(image);
				str = image;
				ipl = cvLoadImage(str);
				recorder.record(grabberConverter.convert(ipl));
			}
			recorder.stop();
			System.out.println("ok");
		} catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
			e.printStackTrace();
		}
	}

	public List<String> getImages(List<ScriptDetailsDto> fetchMetadataListVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) {
		List<String> fileNameList = new ArrayList<String>();
		File folder = new File(fetchConfigVO.getScreenshot_path() + "\\" + customerDetails.getCustomerName() + "\\"
				+ customerDetails.getTestSetName() + "\\");
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				System.out.println("File " + listOfFiles[i].getName());
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
		String Folder = (fetchConfigVO.getScreenshot_path() + "\\" + customerDetails.getCustomerName() + "\\"
				+ customerDetails.getTestSetName() + "\\");
		List<String> fileNameList = null;
		String customer_Name = customerDetails.getCustomerName();
		String test_Run_Name = customerDetails.getTestSetName();
		fileNameList = getImages(fetchMetadataListVO, fetchConfigVO, customerDetails);

		for (String image : fileNameList) {

			FileInputStream inputStream = new FileInputStream(
					fetchConfigVO.getScreenshot_path() + "\\" + customer_Name + "\\" + test_Run_Name + "\\" + image);
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

//		BufferedImage originalImage = ImageIO.read(new File(Folder+image));
//		int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

//		BufferedImage resizeImageGif = resizeImage(originalImage, type);
//		ImageIO.write(resizeImageGif, "jpg", new File("C:\\Kaushik"+"\\"+image));

			/*
			 * BufferedImage resizeImagePng = resizeImage(originalImage, type);
			 * ImageIO.write(resizeImagePng, "png", new File("c:\\image\\mkyong_png.jpg"));
			 * 
			 * BufferedImage resizeImageHintJpg = resizeImageWithHint(originalImage, type);
			 * ImageIO.write(resizeImageHintJpg, "jpg", new
			 * File("c:\\image\\mkyong_hint_jpg.jpg"));
			 * 
			 * BufferedImage resizeImageHintPng = resizeImageWithHint(originalImage, type);
			 * ImageIO.write(resizeImageHintPng, "png", new
			 * File("c:\\image\\mkyong_hint_png.jpg"));
			 */
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
			log.info("Successfully Copy is done " + scripNumber);

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Copy " + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			e.printStackTrace();
			throw e;
		}
	}// input[@placeholder='Enter search terms']

	public void paste(WebDriver driver, String inputParam, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, String globalValueForSteps, CustomerProjectDto customerDetails)

			throws Exception {
		// DH 39
		try {
			if (inputParam.equalsIgnoreCase("Query By Example")) {
				// WebElement waittill = driver.findElement(
				// By.xpath("//h1[text()='" + inputParam +
				// "']/following::input[@placeholder='Search']"));
				// to get Dynamic copynumber
				WebElement waittill = driver
						.findElement(By.xpath("//*[@title='" + inputParam + "']/following::input[1]"));

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
				System.out.println("copynumberValue:::" + copynumberValue);

				String value = globalValueForSteps;

				Thread.sleep(2000);
				waittill.click();
				JavascriptExecutor jse = (JavascriptExecutor) driver;
				jse.executeScript("arguments[0].value='" + copynumberValue + "';", waittill);
				Thread.sleep(3000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Successfully paste is done " + scripNumber);
				String xpath = "//*[@title='inputParam']/following::input[1]";

				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				// service.saveXpathParams(inputParam,"",scripNumber,xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Paste Method");
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}

		try {
			if (inputParam.equalsIgnoreCase("Notifications")) {
				WebElement waittill = driver.findElement(
						By.xpath("//h1[text()='" + inputParam + "']/following::input[@placeholder='Search']"));
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
				System.out.println("copynumberValue:::" + copynumberValue);

				String value = globalValueForSteps;

				// Thread.sleep(2000);
				waittill.click();
				JavascriptExecutor jse = (JavascriptExecutor) driver;
				jse.executeScript("arguments[0].value='" + copynumberValue + "';", waittill);
				Thread.sleep(3000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Successfully paste is done " + scripNumber);
				String xpath = "//input[@placeholder='inputParam']";

				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				// service.saveXpathParams(inputParam,"",scripNumber,xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Paste Method");
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
		try {

			WebElement waittill = driver

					.findElement(By.xpath("//label[text()='" + inputParam + "']/following::input[1]"));

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

			System.out.println("copynumberValue:::" + copynumberValue);

			String value = globalValueForSteps;

//	          String value = copynumber(driver, inputParam1, inputParam2, fetchMetadataVO, fetchConfigVO)

			waittill.click();

			JavascriptExecutor jse = (JavascriptExecutor) driver;

			jse.executeScript("arguments[0].value='" + copynumberValue + "';", waittill);

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
			String xpath = "//label[text()='inputParam']/following::input[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

//				service.saveXpathParams(inputParam,"",scripNumber,xpath);

			return;

		} catch (Exception e) {

			System.out.println(e);

		}

		try {

			WebElement waittill = driver.findElement(By.xpath("//input[@placeholder='" + inputParam + "']"));

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
			System.out.println("copynumberValue:::" + copynumberValue);
			waittill.click();

			JavascriptExecutor jse = (JavascriptExecutor) driver;

			jse.executeScript("arguments[0].value='" + copynumberValue + "';", waittill);

			Thread.sleep(3000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Successfully paste is done " + scripNumber);
			String xpath = "//input[@placeholder='inputParam']";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

//		service.saveXpathParams(inputParam,"",scripNumber,xpath);

			return;

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Paste Method");
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;

		}

	}

	public void clear(WebDriver driver, String inputParam1, String inputParam2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {

		try {
			if (inputParam1.equalsIgnoreCase("Lines")) {
				WebElement waittill = driver.findElement(By.xpath("(//*[normalize-space(text())=\"" + inputParam1
						+ "\"]/following::label[normalize-space(text())='" + inputParam2
						+ "']/preceding-sibling::input)[1]"));
				clearMethod(driver, waittill);
				Thread.sleep(4000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//*[normalize-space(text())='inputParam1']/following::label[normalize-space(text())='inputParam2']/preceding-sibling::input)[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			if (inputParam2.equals("Accounting Period")) {
				Thread.sleep(4000);
				WebElement waittill = driver.findElement(
						By.xpath("//label[normalize-space(text())='" + inputParam2 + "']/preceding-sibling::input[1]"));
				clearMethod(driver, waittill);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Successfully Accounting Period Cleared" + scripNumber);
				String xpath = "//label[normalize-space(text())='inputParam2']/preceding-sibling::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed During Accounting Period Clear" + scripNumber);
			System.out.println(e);
		}
		try {
			WebElement waittill = driver.findElement(
					By.xpath("(//label[contains(text(),'" + inputParam1 + "')]/preceding-sibling::input)[1]"));
			clearMethod(driver, waittill);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Successfully Cleared" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed During Clear" + scripNumber);
			System.out.println(e);
		}
		try {
			Thread.sleep(4000);
			WebElement waittill = driver
					.findElement(By.xpath("(//*[normalize-space(text())='" + inputParam1 + "']/following::input)[1]"));
			clearMethod(driver, waittill);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Successfully Cleared" + scripNumber);
			String xpath = "(//*[normalize-space(text())='inputParam1']/following::input)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed During Clear" + scripNumber);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("//*[contains(@placeholder,'" + inputParam1 + "')]"));
			clearMethod(driver, waittill);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Successfully Cleared" + scripNumber);
			String xpath = "//*[contains(@placeholder,'inputParam1')]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed During Clear" + scripNumber);
			System.out.println(e);
		}
		try {
			WebElement waittill = driver
					.findElement(By.xpath("//*[normalize-space(text())='" + inputParam1 + "']/following::textarea[1]"));
			clearMethod(driver, waittill);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Successfully Cleared" + scripNumber);
			String xpath = "//*[normalize-space(text())='inputParam1']/following::textarea[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed During Clear" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			System.out.println(e);
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
					System.out.println(driver.switchTo().window(childWindow).getTitle());
					driver.manage().window().maximize();
					driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
					driver.switchTo().window(childWindow);
					driver.close();
					driver.switchTo().window(mainWindow);
					String scripNumber = fetchMetadataVO.getScriptNumber();
					log.info("Successfully Windowclosed" + scripNumber);
				}
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed During WindowClose Acion." + scripNumber);
			screenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void switchToActiveElement(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) {
		try {
			driver.switchTo().activeElement();
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Switched to Element Successfully" + scripNumber);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed During switchToActiveElement Action." + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			System.out.println(e.getMessage());
			throw e;
		}
	}

	public void clickMenu(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			if (param1.equalsIgnoreCase("PDF")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath(("(//div[normalize-space(text())='" + param1 + "'])[2]"))));
				// wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(("(//div[text()='"
				// + param1 + "'])[1]")), param1));
				WebElement waittext = driver
						.findElement(By.xpath(("(//div[normalize-space(text())='" + param1 + "'])[2]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(80000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String params = param1;
				String xpath = "(//div[normalize-space(text())='param1'])[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				log.info("Sucessfully clicked Element in clickmenu " + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("failed during ClickMenu " + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@title='" + param1 + "']")));
			Thread.sleep(4000);
			WebElement waittext = driver.findElement(By.xpath("//div[@title='" + param1 + "']"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String params = param1;
			String xpath = "//div[@title='param1']";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully clicked Element in clickmenu " + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("failed during ClickMenu " + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath(("//a[normalize-space(text())='" + param1 + "']"))));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath(("//a[normalize-space(text())='" + param1 + "']")), param1));
			WebElement waittext = driver.findElement(By.xpath(("//a[normalize-space(text())='" + param1 + "']")));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();

			String xpath = "//a[normalize-space(text())='param1']";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully clicked Element in clickmenu " + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("failed during ClickMenu " + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					("//div[contains(@style,'display: block')]//div[normalize-space(text())='" + param1 + "']"))));
			WebElement waittext = driver.findElement(By.xpath(
					("//div[contains(@style,'display: block')]//div[normalize-space(text())='" + param1 + "']")));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();

			String xpath = "//div[contains(@style,'display: block')]//div[normalize-space(text())='param1']";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully clicked Element in clickmenu " + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();

			log.error("failed during ClickMenu " + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath(("//div[normalize-space(text())='" + param1 + "']"))));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath(("//div[normalize-space(text())='" + param1 + "']")), param1));
			WebElement waittext = driver.findElement(By.xpath(("//div[normalize-space(text())='" + param1 + "']")));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//div[normalize-space(text())='param1']";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully clicked Element in clickmenu " + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("failed during ClickMenu " + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("(//div[contains(@id,'" + param1 + "')])[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//div[contains(@id,'" + param1 + "')])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String params = param1;
			String xpath = "(//div[contains(@id,'param1')])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully clicked Element in clickmenu " + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			System.out.println(e);
			log.error("failed during ClickMenu " + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void clickSignInSignOut(WebDriver driver, String param1, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath(("//button[normalize-space(normalize-space(text())='" + param1 + "')]"))));
			WebElement waittext = driver
					.findElement(By.xpath(("//button[normalize-space(normalize-space(text())='" + param1 + "')]")));
			screenshot(driver, fetchMetadataVO, customerDetails);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully clicked SingnInSignOut" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			System.out.println(e);
			log.error("Failed during SingnInSignOut " + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}// *[text()='Action Required']/following::a[1]

	public void clickNotificationLink(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {

		// placeholder changed from 'Enter Search Terms to Search' in Fusion Instance
		try {
			if (param1.equalsIgnoreCase("Notifications") && param2.equalsIgnoreCase("Search")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1
						+ "']/following::input[@placeholder='" + param2 + "']/following::a[1]")));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
						+ "']/following::input[@placeholder='" + param2 + "']/following::a[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(2000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Successfully Clicked NotificationLink" + scripNumber);
				String params = param1;
				String xpath = "//*[normalize-space(text())='param1']/following::a[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed During NotificationLink" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//*[normalize-space(text())='" + param1 + "']/following::a[1]")));
			Thread.sleep(4000);
			WebElement waittext = driver
					.findElement(By.xpath("//*[normalize-space(text())='" + param1 + "']/following::a[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(2000);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Successfully Clicked NotificationLink" + scripNumber);
			String params = param1;
			String xpath = "//*[normalize-space(text())='param1']/following::a[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed During NotificationLink" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath(("//*[@placeholder='" + param1 + "']/following::a[1]"))));
			Thread.sleep(4000);
			WebElement waittext = driver.findElement(By.xpath("//*[@placeholder='" + param1 + "']/following::a[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(2000);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Successfully Clicked NotificationLink" + scripNumber);
			String params = param1;
			String xpath = "//*[@placeholder='param1']/following::a[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			System.out.println(e);
			log.error("Failed during NotificationLink" + scripNumber);
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
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@title='" + param1 + "']")));
				WebElement waittext = driver.findElement(By.xpath("//a[@title='" + param1 + "']"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				clickButtonDropdownText(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//a[@title='" + param1 + "']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				log.info("Successfully Clicked ClickButtonDropdown" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed During ClickButtonDropdown " + scripNumber);
			System.out.println(e);
		}
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("//h1[contains(text(),'" + param1 + "')]/following::a[1]")));
				WebElement waittext = driver
						.findElement(By.xpath("//h1[contains(text(),'" + param1 + "')]/following::a[1]"));
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				clickButtonDropdownText(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//h1[contains(text(),'" + param1 + "')]/following::a[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				log.info("Successfully Clicked ClickButtonDropdown" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed During clickButtonDropdown " + scripNumber);
			System.out.println(e);

		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"(//h1[normalize-space(text())='" + param1 + "']/following::a[@title='" + param2 + "'])[1]")));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath(("//h1[normalize-space(text())='" + param1 + "']")), param1));
			Thread.sleep(6000);
			WebElement waittext = driver.findElement(By.xpath(
					"(//h1[normalize-space(text())='" + param1 + "']/following::a[@title='" + param2 + "'])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			clickButtonDropdownText(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//h1[normalize-space(text())='" + param1 + "']/following::a[@title='" + param2 + "'])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Successfully Clicked ClickButtonDropdown" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed During clickButtonDropdown " + scripNumber);
			System.out.println(e);
		}
		try {
			if (param2.equalsIgnoreCase("Publish to Managers")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[normalize-space(text())='"
						+ param1 + "']/following::a[normalize-space(text())='" + param2 + "'])[2]")));
				WebElement waittext = driver.findElement(By.xpath("(//h1[normalize-space(text())='" + param1
						+ "']/following::a[normalize-space(text())='" + param2 + "'])[2]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				clickButtonDropdownText(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//h1[normalize-space(text())='param1']/following::a[normalize-space(text())='param2'])[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				log.info("Successfully Clicked ClickButtonDropdown" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed During clickButtonDropdown " + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[normalize-space(text())='" + param1
					+ "']/following::a[normalize-space(text())='" + param2 + "'])[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//h1[normalize-space(text())='" + param1
					+ "']/following::a[normalize-space(text())='" + param2 + "'])[1]"));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			clickButtonDropdownText(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//h1[normalize-space(text())='param1']/following::a[normalize-space(text())='param2'])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Successfully Clicked ClickButtonDropdown" + scripNumber);
			return;
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed During clickButtonDropdown " + scripNumber);
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
					.presenceOfElementLocated(By.xpath("//li[normalize-space(text())='" + keysToSend + "']")));
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("//li[normalize-space(text())='" + keysToSend + "']")));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath(("//li[normalize-space(text())='" + keysToSend + "']")), keysToSend));
			Thread.sleep(5000);
			WebElement waittext = driver.findElement(By.xpath("//li[normalize-space(text())='" + keysToSend + "']"));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Successfully Clicked ClickButtonDropdownText" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed During clickButtonDropdownText " + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"//div[contains(@class,'PopupMenuContent')]//td[normalize-space(text())='" + keysToSend + "']")));
			WebElement waittext = driver.findElement(By.xpath(
					"//div[contains(@class,'PopupMenuContent')]//td[normalize-space(text())='" + keysToSend + "']"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Successfully Clicked ClickButtonDropdownText" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed During clickButtonDropdownText " + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("//td[normalize-space(text())='" + keysToSend + "']")));
			WebElement waittext = driver.findElement(By.xpath("//td[normalize-space(text())='" + keysToSend + "']"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Successfully Clicked ClickButtonDropdownText" + scripNumber);
			return;
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed During clickButtonDropdownText " + scripNumber);
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
						"(//h2[normalize-space(text())='" + param1 + "']/preceding::*[@title='" + param2 + "'])[1]")));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(
						By.xpath("//h2[normalize-space(text())='" + param1 + "']"), param1));
				WebElement waittext = driver.findElement(By.xpath(
						"(//h2[normalize-space(text())='" + param1 + "']/preceding::*[@title='" + param2 + "'])[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(4000);
				try {
					WebElement Expand = driver.findElement(By.xpath("(//h2[normalize-space(text())='" + param1
							+ "']/following::*[@title='" + param2 + "'])[1]"));
					Expand.click();
					String scripNumber = fetchMetadataVO.getScriptNumber();
					log.info("Sucessfully Clicked Expanded or Collapsed" + scripNumber);

				} catch (Exception e) {

					String scripNumber = fetchMetadataVO.getScriptNumber();
					log.error("Failed During ClickExpand or Collapse" + scripNumber);
				}

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//h1[normalize-space(text())='param1']/preceding::*[@title='param2'])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				log.info("Sucessfully Clicked Process Monitor ClickExpand or Collapse" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed During Process Monitor ClickExpand or Collapse" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"(//h2[normalize-space(text())='" + param1 + "']/following::*[@title='" + param2 + "'])[1]")));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath("//h2[normalize-space(text())='" + param1 + "']"), param1));
			WebElement waittext = driver.findElement(By.xpath(
					"(//h2[normalize-space(text())='" + param1 + "']/following::*[@title='" + param2 + "'])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(4000);
			try {
				WebElement Expand = driver.findElement(By.xpath(
						"(//h2[normalize-space(text())='" + param1 + "']/following::*[@title='" + param2 + "'])[1]"));
				Expand.click();
			} catch (Exception e) {

			}
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked ClickExpand or Collapse" + scripNumber);
			String xpath = "(//h2[normalize-space(text())='param1']/following::*[@title='param2'])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			screenshot(driver, fetchMetadataVO, customerDetails);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed During ClickExpand or Collapse" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"(//h1[normalize-space(text())='" + param1 + "']/preceding::*[@title='" + param2 + "'])[1]")));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath("//h1[normalize-space(text())='" + param1 + "']"), param1));
			WebElement waittext = driver.findElement(By.xpath(
					"(//h1[normalize-space(text())='" + param1 + "']/preceding::*[@title='" + param2 + "'])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(4000);
			try {
				WebElement Expand = driver.findElement(By.xpath(
						"(//h1[normalize-space(text())='" + param1 + "']/preceding::*[@title='" + param2 + "'])[1]"));
				Expand.click();
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked ClickExpand or Collapse" + scripNumber);

			} catch (Exception e) {
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.error("Failed During ClickExpand or Collapse" + scripNumber);

			}
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked ClickExpand or Collapse" + scripNumber);
			String xpath = "(//h1[normalize-space(text())='param1']/preceding::*[@title='param2'])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed During ClickExpand or Collapse" + scripNumber);
			System.out.println(e);
		}
		try {
			Thread.sleep(4000);
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"(//span[contains(text(),'" + param1 + "')])[1]/preceding::a[3][@title='" + param2 + "'][1]")));
			WebElement waittext = driver.findElement(By.xpath(
					"(//span[contains(text(),'" + param1 + "')])[1]/preceding::a[3][@title='" + param2 + "'][1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(1000);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked ClickExpand or Collapse" + scripNumber);
			String xpath = "(//span[contains(text(),'param1')])[1]/preceding::a[3][@title='param2'][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed During ClickExpand or Collapse" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"(//*[normalize-space(text())='" + param1 + "']/following::*[@title='" + param2 + "'])[1]")));
			WebElement waittext = driver.findElement(By
					.xpath("(//*[normalize-space(text())='" + param1 + "']/following::*[@title='" + param2 + "'])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(4000);
			try {
				WebElement Expand = driver.findElement(By.xpath(
						"(//*[normalize-space(text())='" + param1 + "']/following::*[@title='" + param2 + "'])[1]"));
				Expand.click();
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked ClickExpand or Collapse" + scripNumber);
			} catch (Exception e) {
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.error("Failed During ClickExpand or Collapse" + scripNumber);
			}
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked ClickExpand or Collapse" + scripNumber);
			screenshot(driver, fetchMetadataVO, customerDetails);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed During ClickExpand or Collapse" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"(//*[normalize-space(text())='" + param1 + "']/preceding::*[@title='" + param2 + "'])[1]")));
			WebElement waittext = driver.findElement(By
					.xpath("(//*[normalize-space(text())='" + param1 + "']/preceding::*[@title='" + param2 + "'])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(4000);
			try {
				WebElement Expand = driver.findElement(By.xpath(
						"(//*[normalize-space(text())='" + param1 + "']/preceding::*[@title='" + param2 + "'])[1]"));
				Expand.click();
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked ClickExpand or Collapse" + scripNumber);
			} catch (Exception e) {
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.error("Failed During ClickExpand or Collapse" + scripNumber);

			}
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked ClickExpand or Collapse" + scripNumber);
			String xpath = "(//*[normalize-space(text())='param1']/preceding::*[@title='param2'])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			screenshot(driver, fetchMetadataVO, customerDetails);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed During ClickExpand or Collapse" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"//*[normalize-space(text())='" + param1 + "']/following::*[normalize-space(text())='" + param2
							+ "']/preceding::*[@title='Expand' and @href and not(@style='display:none')][1]")));
			WebElement waittext = driver.findElement(
					By.xpath("//*[normalize-space(text())='" + param1 + "']/following::*[normalize-space(text())='"
							+ param2 + "']/preceding::*[@title='Expand' and @href and not(@style='display:none')][1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(4000);
			try {
				WebElement Expand = driver.findElement(By.xpath(
						"//*[normalize-space(text())='" + param1 + "']/following::*[normalize-space(text())='" + param2
								+ "']/preceding::*[@title='Expand' and @href and not(@style='display:none')][1]"));
				Expand.click();
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked ClickExpand or Collapse" + scripNumber);
			} catch (Exception e) {
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.error("Failed During ClickExpand or Collapse" + scripNumber);

			}
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked ClickExpand or Collapse" + scripNumber);
			String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']/preceding::*[@title='Expand' and @href and not(@style='display:none')][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			screenshot(driver, fetchMetadataVO, customerDetails);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed During ClickExpand or Collapse" + scripNumber);
			System.out.println(e);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void selectAValue(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {

		// DH 39
		try {
			if (param1.equalsIgnoreCase("Assignment Number")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(),'" + param1
						+ "')]/following::a[text()='" + keysToSend + "' and not(@style)]")));
				WebElement waittext = driver.findElement(By.xpath("//*[contains(text(),'" + param1
						+ "')]/following::a[text()='" + keysToSend + "' and not(@style)]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(2000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Review installments selectAValue" + scripNumber);
				String xpath = "//*[contains(text(),'param1')]/following::a[text()='keysToSend' and not(@style)]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Review installments selectAValue" + scripNumber);
			System.out.println(e);
		}

		// DH 32
		try {
			if (param1.equalsIgnoreCase("Plan")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(),'" + param1
						+ "')]/following::*[normalize-space(text())='" + keysToSend + "']//following::span[1]")));
				WebElement waittext = driver.findElement(By.xpath("//*[contains(text(),'" + param1
						+ "')]/following::*[normalize-space(text())='" + keysToSend + "']//following::span[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(2000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Review installments selectAValue" + scripNumber);
				String xpath = "//*[contains(text(),'param1')]/following::*[normalize-space(text())='keysToSend']//following::span[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Review installments selectAValue" + scripNumber);
			System.out.println(e);
		}

		try {
			if (param1.equalsIgnoreCase("Review installments") || param1.equalsIgnoreCase("Review proposed payments")
					|| param1.equalsIgnoreCase("Record print status")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"//*[normalize-space(text())='" + keysToSend + "']/following::img[@title='" + param1 + "']")));
				WebElement waittext = driver.findElement(By.xpath(
						"//*[normalize-space(text())='" + keysToSend + "']/following::img[@title='" + param1 + "']"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(2000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Review installments selectAValue" + scripNumber);
				String xpath = "//*[normalize-space(text())='keysToSend']/following::img[@title='param1']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Review installments selectAValue" + scripNumber);
			System.out.println(e);
		}
		// DH 15
		try {
			if (param1.equalsIgnoreCase("Absences") && param2.equalsIgnoreCase("Absence Type")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[normalize-space(text())='" + param1 + "']/following::span[text()='" + keysToSend
								+ "']/following::img[contains(@title,'" + param2 + "')][1]")));
				WebElement waittext = driver
						.findElement(By.xpath("//*[normalize-space(text())='" + param1 + "']/following::span[text()='"
								+ keysToSend + "']/following::img[contains(@title,'" + param2 + "')][1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(2000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked selectAValue" + scripNumber);
				String xpath = "//*[normalize-space(text())='keysToSend']/following::*[normalize-space(text())='param1']";
				// service.saveXpathParams(param1, param2, scripNumber, xpath);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during selectAValue" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			// throw e;
		}
		try {
			if (param1.equalsIgnoreCase("Payment Process Requests") && param1.equalsIgnoreCase("Name")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"(//*[contains(text(),'" + param2 + "')]/following::*[normalize-space(text())='" + keysToSend
								+ "'][1]/following::img/following::a[contains(@id,'RecentlyCompletedPpr')])[2]")));
				WebElement waittext = driver.findElement(By.xpath(
						"(//*[contains(text(),'\" + param2 + \"')]/following::*[normalize-space(text())='\" + keysToSend + \"'][1]/following::img/following::a[contains(@id,'RecentlyCompletedPpr')])[2]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(2000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Review installments selectAValue" + scripNumber);
				String xpath = "//*[normalize-space(text())='keysToSend']/following::img[@title='param1']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Review installments selectAValue" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Existing Absences") && param2.equalsIgnoreCase("Action")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By
						.xpath("//*[contains(text(),'" + keysToSend + "')]/following::*[@title='" + param2 + "'][1]")));
				WebElement waittext = driver.findElement(By
						.xpath("//*[contains(text(),'" + keysToSend + "')]/following::*[@title='" + param2 + "'][1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(2000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked selectAValue" + scripNumber);
				String xpath = "//*[normalize-space(text())='keysToSend']/following::*[normalize-space(text())='param1']";
				// service.saveXpathParams(param1, param2, scripNumber, xpath);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during selectAValue" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			// throw e;
		}
		try {
			Thread.sleep(5000);
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(),'" + param1
					+ "')]/following::*[normalize-space(text())='" + keysToSend + "'][1]")));
			WebElement waittext = driver.findElement(By.xpath("//*[contains(text(),'" + param1
					+ "')]/following::*[normalize-space(text())='" + keysToSend + "'][1]"));
			Thread.sleep(2000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(5000);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked selectAValue" + scripNumber);
			String xpath = "//*[contains(text(),'param1')]/following::*[normalize-space(text())='keysToSend'][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during selectAValue" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1
					+ "']/following::label[normalize-space(text())='" + param2 + "']")));
			WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
					+ "']/following::label[normalize-space(text())='" + param2 + "']"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(2000);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked selectAValue" + scripNumber);
			String xpath = "//*[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during selectAValue" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + keysToSend
					+ "']/following::*[normalize-space(text())='" + param1 + "']")));
			WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + keysToSend
					+ "']/following::*[normalize-space(text())='" + param1 + "']"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(2000);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked selectAValue" + scripNumber);
			String xpath = "//*[normalize-space(text())='keysToSend']/following::*[normalize-space(text())='param1']";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during selectAValue" + scripNumber);
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
					By.xpath("//*[normalize-space(text())='" + param1 + "']/following::*[normalize-space(text())='"
							+ keysToSend + "']/following::img[contains(@id,'" + param2 + "')][1]")));
			Thread.sleep(4000);
			WebElement waittill = driver.findElement(
					By.xpath("//*[normalize-space(text())='" + param1 + "']/following::*[normalize-space(text())='"
							+ keysToSend + "']/following::img[contains(@id,'" + param2 + "')][1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			Thread.sleep(2000);
			screenshot(driver, fetchMetadataVO, customerDetails);
			clickValidateXpath(driver, fetchMetadataVO, waittill, fetchConfigVO);
			Thread.sleep(500);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='keysToSend']/following::img[contains(@id,'param2')][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully Clicked clickTableImage" + scripNumber);
			return keysToSend;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickTableImage" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//*[normalize-space(text())='" + param1 + "']/following::*[normalize-space(text())='"
							+ keysToSend + "']/following::img[@title='" + param2 + "'][1]")));
			WebElement waittill = driver.findElement(
					By.xpath("//*[normalize-space(text())='" + param1 + "']/following::*[normalize-space(text())='"
							+ keysToSend + "']/following::img[@title='" + param2 + "'][1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			Thread.sleep(2000);
			screenshot(driver, fetchMetadataVO, customerDetails);
			clickValidateXpath(driver, fetchMetadataVO, waittill, fetchConfigVO);
			Thread.sleep(500);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='keysToSend']/following::img[@title='param2'][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully Clicked clickTableImage" + scripNumber);
			return keysToSend;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickTableImage" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1
					+ "']/following::*[@value='" + keysToSend + "']/following::img[@title='" + param2 + "'][1]")));
			WebElement waittill = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
					+ "']/following::*[@value='" + keysToSend + "']/following::img[@title='" + param2 + "'][1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			Thread.sleep(2000);
			screenshot(driver, fetchMetadataVO, customerDetails);
			clickValidateXpath(driver, fetchMetadataVO, waittill, fetchConfigVO);
			Thread.sleep(500);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[normalize-space(text())='param1']/following::*[@value='keysToSend']/following::img[@title='param2'][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully Clicked clickTableImage" + scripNumber);
			return keysToSend;
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickTableImage" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void clickImage(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {

		// DH 43
		try {
			if (param1.equalsIgnoreCase("Plan Balances")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[text()='" + param1 + "']/following::div[@role='button'][1]")));
				WebElement waittext = driver
						.findElement(By.xpath("//*[text()='" + param1 + "']/following::div[@role='button'][1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		// DH 15
		try {
			if (param2.equalsIgnoreCase("Back")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//h1[normalize-space(text())='" + param1 + "']/preceding::a[1]")));
				WebElement waittext = driver
						.findElement(By.xpath("//h1[normalize-space(text())='" + param1 + "']/preceding::a[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		// Dh 9
		try {

			if (param1.equalsIgnoreCase("move")) {

				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				wait.until(ExpectedConditions.presenceOfElementLocated(

						By.xpath("(//a[contains(@id,'" + param1 + "')])[1]")));

				WebElement waittext = driver.findElement(

						By.xpath("(//a[contains(@id,'" + param1 + "')])[1]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittext).build().perform();

				waittext.click();

				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);

				String scripNumber = fetchMetadataVO.getScriptNumber();

				log.info("Sucessfully Clicked clickImage" + scripNumber);

				String xpath = "(//a[contains(@id,'param1')])[1]";

				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;

			}

		} catch (Exception e) {

			String scripNumber = fetchMetadataVO.getScriptNumber();

			log.error("Failed during clickImag" + scripNumber);

			System.out.println(e);

		}

		// DH 19
		try {
			if (param1.equalsIgnoreCase("Existing Absences") && param2.equalsIgnoreCase("Add")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By
						.xpath("(//h1[normalize-space(text())='" + param1 + "']/following::div[@role='button'])[1]")));
				Thread.sleep(2000);
				WebElement waittext = driver.findElement(
						By.xpath("(//h1[normalize-space(text())='" + param1 + "']/following::div[@role='button'])[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(3000);
				WebElement add = driver.findElement(By.xpath(
						"//h1[normalize-space(text())='" + param1 + "']/following::span[text()='" + param2 + "']"));
				clickValidateXpath(driver, fetchMetadataVO, add, fetchConfigVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked clickImage" + scripNumber);
				String xpath = "(//h1[normalize-space(text())='param1']/following::div[@role='button'])[1]";
				// service.saveXpathParams(param1, param2, scripNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickImag" + scripNumber);
			System.out.println(e);
		}

		try {
			if (param1.equals("Republish")) {
				Thread.sleep(3000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver.findElement(By.xpath("//img[contains(@title,'" + param1 + "')]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked clickImage" + scripNumber);
				String params = param1;
				String xpath = "//img[contains(@title,'param1')]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickImag" + scripNumber);
			System.out.println(e);
		}

		try {
			if (param1.equalsIgnoreCase("Provider") || param1.equalsIgnoreCase("Receiver")) {
				Thread.sleep(4000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"//*[normalize-space(text())='" + param1 + "']/following::img[@title='" + param2 + "'][2]")));
				WebElement waittext = driver.findElement(By.xpath(
						"//*[normalize-space(text())='" + param1 + "']/following::img[@title='" + param2 + "'][2]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Provider or Receiver clickImage" + scripNumber);
				String xpath = "//*[normalize-space(text())='param1']/following::img[@title='param2'][2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Provider or Receiver clickImag" + scripNumber);
			System.out.println(e);
		}

		try {
			if (param2.equalsIgnoreCase("Add Row")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[contains(text(),'" + param1 + "')]/following::img[@title='" + param2 + "'][1]")));
				WebElement waittext = driver.findElement(
						By.xpath("//*[contains(text(),'" + param1 + "')]/following::img[@title='" + param2 + "'][1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked clickImage" + scripNumber);
				String xpath = "//*[contains(text(),'param1')]/following::img[@title='param2'][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickImag" + scripNumber);
			System.out.println(e);
		}
		// label[contains(text(),'Enter Cost Centre')]/following::input[1]
		try {
			if (param1.equalsIgnoreCase("Report")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[contains(text(),'" + param2 + "')/following::input[1]]")));
				WebElement waittext = driver
						.findElement(By.xpath("//*[contains(text(),'" + param2 + "')/following::input[1]]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Report clickImage" + scripNumber);
				String xpath = "//*[contains(text(),'param2')/following::input[1]]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Report clickImag" + scripNumber);
			System.out.println(e);
		}
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@title='" + param1 + "']")));
				WebElement waittext = driver.findElement(By.xpath("//img[@title='" + param1 + "']"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(8000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked clickImage" + scripNumber);
				String params = param1;
				String xpath = "//img[@title='param1']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickImag" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Customer")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By
						.xpath("//*[normalize-space(text())='" + param1 + "']/following::a[@title='" + param2 + "']")));
				WebElement waittext = driver.findElement(By
						.xpath("//*[normalize-space(text())='" + param1 + "']/following::a[@title='" + param2 + "']"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Customer clickImage" + scripNumber);
				String xpath = "//*[normalize-space(text())='param1']/following::a[@title='param2']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Clicked clickImag" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Add to Selected") || param1.equalsIgnoreCase("Remove from Selected")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(
						ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@title='" + param1 + "']//img[1]")));
				WebElement waittext = driver.findElement(By.xpath("//a[@title='" + param1 + "']//img[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Add to Selected clickImage" + scripNumber);
				String xpath = "//a[@title='param1']//img[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Add to Selected clickImag" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param2.equalsIgnoreCase("Go to Member Selection")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By
						.xpath("//*[contains(text(),'" + param1 + "')]/following::input[@title='" + param2 + "'][1]")));
				WebElement waittext = driver.findElement(By
						.xpath("//*[contains(text(),'" + param1 + "')]/following::input[@title='" + param2 + "'][1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Go to Member Selection clickImage" + scripNumber);
				String xpath = "//*[contains(text(),'param1')]/following::input[@title='param2'][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Go to Member Selection clickImag" + scripNumber);
			System.out.println(e);
		}
		/*
		 * try { if (param1.equalsIgnoreCase("Provider") ||
		 * param1.equalsIgnoreCase("Receiver")) { Thread.sleep(4000); WebDriverWait wait
		 * = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		 * wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
		 * "//*[normalize-space(text())='" + param1 + "']/following::img[@title='" +
		 * param2 + "'][2]"))); WebElement waittext = driver.findElement(By.xpath(
		 * "//*[normalize-space(text())='" + param1 + "']/following::img[@title='" +
		 * param2 + "'][2]")); Actions actions = new Actions(driver);
		 * actions.moveToElement(waittext).build().perform(); clickValidateXpath(driver,
		 * fetchMetadataVO, waittext, fetchConfigVO); screenshot(driver, "",
		 * fetchMetadataVO, fetchConfigVO); String scripNumber =
		 * fetchMetadataVO.getScriptNumber();
		 * log.info("Sucessfully Clicked Provider or Receiver clickImage" +
		 * scripNumber); String xpath =
		 * "//*[normalize-space(text())='param1']/following::img[@title='param2'][2]";
		 * String scriptID=fetchMetadataVO.getScriptId();String
		 * metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(
		 * scriptID,metadataID,xpath); return; } } catch (Exception e) { String
		 * scripNumber = fetchMetadataVO.getScriptNumber();
		 * log.error("Failed during Provider or Receiver clickImag" + scripNumber);
		 * System.out.println(e); }
		 */
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(
						ExpectedConditions.presenceOfElementLocated(By.xpath("//img[contains(@id,'" + param1 + "')]")));
				WebElement waittext = driver.findElement(By.xpath("//img[contains(@id,'" + param1 + "')]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked clickImage" + scripNumber);
				String params = param1;
				String xpath = "//img[contains(@id,'param1')]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickImag" + scripNumber);
			System.out.println(e);
		}
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				Thread.sleep(3000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@title='" + param1 + "']")));
				WebElement waittext = driver.findElement(By.xpath("//a[@title='" + param1 + "']"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked clickImage" + scripNumber);
				String params = param1;
				String xpath = "//a[@title='param1']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickImag" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param2.equalsIgnoreCase("Back")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//h1[normalize-space(text())='" + param1 + "']/preceding::a[1]")));
				WebElement waittext = driver
						.findElement(By.xpath("//h1[normalize-space(text())='" + param1 + "']/preceding::a[1]"));
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Back clickImage" + scripNumber);
				String xpath = "//h1[normalize-space(text())='param1']/preceding::a[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Back clickImag" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("(//h1[normalize-space(text())='" + param1 + "']/following::div[@role='button'])[1]")));
			Thread.sleep(2000);
			WebElement waittext = driver.findElement(
					By.xpath("(//h1[normalize-space(text())='" + param1 + "']/following::div[@role='button'])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(3000);
			WebElement add = driver.findElement(
					By.xpath("//h1[normalize-space(text())='" + param1 + "']/following::img[@title='" + param2 + "']"));
			clickValidateXpath(driver, fetchMetadataVO, add, fetchConfigVO);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked clickImage" + scripNumber);
			String xpath = "(//h1[normalize-space(text())='param1']/following::div[@role='button'])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickImag" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"(//h1[normalize-space(text())='" + param1 + "']/following::img[@title='" + param2 + "'])[1]")));
			WebElement waittext = driver.findElement(By.xpath(
					"(//h1[normalize-space(text())='" + param1 + "']/following::img[@title='" + param2 + "'])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked clickImage" + scripNumber);
			String xpath = "(//h1[normalize-space(text())='param1']/following::img[@title='param2'])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickImag" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"//*[normalize-space(text())='" + param1 + "']/following::img[@title='" + param2 + "'][1]")));
			WebElement waittext = driver.findElement(By
					.xpath("//*[normalize-space(text())='" + param1 + "']/following::img[@title='" + param2 + "'][1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			Thread.sleep(8000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked clickImage" + scripNumber);
			String xpath = "//*[normalize-space(text())='param1']/following::img[@title='param2'][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickImag" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("(//*[normalize-space(text())='" + param1 + "']/following::div[@role='button'])[1]")));
			WebElement waittext = driver.findElement(
					By.xpath("(//*[normalize-space(text())='" + param1 + "']/following::div[@role='button'])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(3000);
			WebElement add = driver.findElement(
					By.xpath("//*[normalize-space(text())='" + param1 + "']/following::img[@title='" + param2 + "']"));
			clickValidateXpath(driver, fetchMetadataVO, add, fetchConfigVO);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked clickImage" + scripNumber);
			String xpath = "(//*[normalize-space(text())='param1']/following::div[@role='button'])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickImag" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"//*[normalize-space(text())='" + param1 + "']/following::img[contains(@id,'" + param2 + "')]")));
			WebElement waittext = driver.findElement(By.xpath(
					"//*[normalize-space(text())='" + param1 + "']/following::img[contains(@id,'" + param2 + "')]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			waittext.click();
//			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(2000);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked clickImage" + scripNumber);
			String xpath = "//*[normalize-space(text())='param1']/following::img[contains(@id,'param2')]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickImag" + scripNumber);
			System.out.println(e);
		}

		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1
					+ "']/following::*[normalize-space(text())='" + param2 + "']/following::img[1]")));
			WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
					+ "']/following::*[normalize-space(text())='" + param2 + "']/following::img[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			waittext.click();
			// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked clickImage" + scripNumber);
			String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']/following::img[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickImag" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("//*[contains(@aria-label,'" + param1 + "')]")));
			WebElement waittext = driver.findElement(By.xpath("//*[contains(@aria-label,'" + param1 + "')]"));
			Actions actions = new Actions(driver);
			waittext.click();
			actions.moveToElement(waittext).build().perform();
			// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked clickImage" + scripNumber);
			String xpath = "//*[contains(@aria-label,'param1')]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickImag" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void clickButtonCheckPopup(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			if ("yes".equalsIgnoreCase(fetchMetadataVO.getConditionalPopup())) {
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				log.info("alertText clicked ");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clickButton(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {

		// Dh 39
		try {
			if (param1.equalsIgnoreCase("Generate Schedules")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver.findElement(By.xpath(("//span[text()='G']")));// screenshot(driver,
				// "",
				// fetchMetadataVO,
				// fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				Thread.sleep(15000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath = "//span[text()='G']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickButton" + scripNumber);

			System.out.println(e);
		}

		// Dh 39
		try {
			if (param1.equalsIgnoreCase("Addresses") && param2.equalsIgnoreCase("Edit")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver.findElement(
						By.xpath(("//h2[text()='" + param1 + "']/following::*[text()='" + param2 + "'][1]")));// screenshot(driver,
				// "",
				// fetchMetadataVO,
				// fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				Thread.sleep(15000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath = "//h2[text()='param1']/following::*[text()='param2'][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickButton" + scripNumber);

			System.out.println(e);
		}

		// Dh 39
		try {
			if (param1.equalsIgnoreCase("Warning") && param2.equalsIgnoreCase("Yes")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver.findElement(By.xpath(("//*[normalize-space(text())='" + param1
						+ "']/following::*[normalize-space(text())='" + param2 + "' and @type='button'][1]")));// screenshot(driver,
				// "",
				// fetchMetadataVO,
				// fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				Thread.sleep(15000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2' and @type='button'][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickButton" + scripNumber);

			System.out.println(e);
		}

		// DH 31
		try {

			if (param1.equalsIgnoreCase("Create Baseline")) {

				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				WebElement waittext = driver.findElement(By.xpath(("//*[text()='aseline']")));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittext).build().perform();

				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);

				Thread.sleep(15000);

				String scripNumber = fetchMetadataVO.getScriptNumber();

				log.info("Sucessfully Clicked Create Baseline clickButton" + scripNumber);

				String xpath = "//*[text()='aseline']";

				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;

			}

		} catch (Exception e) {

			String scripNumber = fetchMetadataVO.getScriptNumber();

			log.error("Failed during clickButton" + scripNumber);

			System.out.println(e);

		}

		// DH 31

		try {

			if (param1.equalsIgnoreCase("Search")) {

				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				WebElement waittext = driver.findElement(By.xpath(("//*[text()='Sea']")));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittext).build().perform();

				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);

				Thread.sleep(15000);

				String scripNumber = fetchMetadataVO.getScriptNumber();

				log.info("Sucessfully Clicked Search clickButton" + scripNumber);

				String xpath = "//*[text()='Sea']";

				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;

			}

		} catch (Exception e) {

			String scripNumber = fetchMetadataVO.getScriptNumber();

			log.error("Failed during clickButton" + scripNumber);

			System.out.println(e);

		}

		// New code for PTP.PO.511
		// DH 29
		try {
			if (param1.equalsIgnoreCase("Submit")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver.findElement(By.xpath(("//a[@accessKey='m']")));// screenshot(driver,
				// "",
				// fetchMetadataVO,
				// fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				Thread.sleep(15000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath = "//a[@accessKey='m']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickButton" + scripNumber);
			System.out.println(e);
		}

		// DH 29
		try {
			if (param1.equalsIgnoreCase("Approval") || param1.equalsIgnoreCase("Respond to Questionnaire")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver.findElement(By.xpath(("//div[contains(@id,'popup-container')]//td[text()='"
						+ param1 + "']//preceding-sibling::td[1]")));// screenshot(driver,
				// "",
				// fetchMetadataVO,
				// fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				Thread.sleep(15000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath = "//div[contains(@id,'popup-container')]//td[text()='param1']//preceding-sibling::td[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickButton" + scripNumber);

			System.out.println(e);
		}

		// DH 29
		try {
			if (param1.equalsIgnoreCase("Initiate") || param1.equalsIgnoreCase("Supplier")
					|| param1.equalsIgnoreCase("Internal")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver.findElement(
						By.xpath(("(//div[contains(@class,'PopupMenuContent')])[2]//td[text()='" + param1 + "']")));// screenshot(driver,
				// "",
				// fetchMetadataVO,
				// fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(15000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath = "(//div[contains(@class,'PopupMenuContent')])[2]//td[text()='param1']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickButton" + scripNumber);

			System.out.println(e);
		}

		// DH 10
		try {

			if (param2.equalsIgnoreCase("Done")) {

				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				WebElement waittext = driver

						.findElement(By.xpath("//div[contains(@id,'popup-container')]//*[text()='" + param1
								+ "']/following::button[text()='" + param2 + "'])[1]"));

				// //screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);

				Actions actions = new Actions(driver);

				actions.moveToElement(waittext).build().perform();

				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);

				Thread.sleep(1000);

				String scripNumber = fetchMetadataVO.getScriptNumber();

				log.info("Sucessfully Clicked Done Button" + scripNumber);

				String xpath = "//div[contains(@id,'popup-container')]//*[text()='param1']/following::button[text()='param2'])[1]";

				// service.saveXpathParams(param1, param2, scripNumber, xpath);

				return;

			}

		} catch (Exception e) {

			String scripNumber = fetchMetadataVO.getScriptNumber();

			log.error("Failed during clicking Done Button" + scripNumber);

			System.out.println(e);

		}

		// DH 9
		try {

			if (param1.equalsIgnoreCase("Save and Close")) {

				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				WebElement waittext = driver.findElement(By.xpath(("//button[text()='ave and Close']")));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittext).build().perform();

				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);

				Thread.sleep(15000);

				String scripNumber = fetchMetadataVO.getScriptNumber();

				log.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);

				String xpath = "//button[text()='ave and Close']";

				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;

			}

		} catch (Exception e) {

			String scripNumber = fetchMetadataVO.getScriptNumber();

			log.error("Failed during clickButton" + scripNumber);

			System.out.println(e);

		}
		// DH 22
		try {

			if (param1.equalsIgnoreCase("Update Address") && param2.equalsIgnoreCase("OK")) {

				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				WebElement waittext = driver
						.findElement(By.xpath(("(//div[contains(@id,'popup-container')]//button[@accesskey='K'])[2]")));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittext).build().perform();

				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);

				Thread.sleep(15000);

				String scripNumber = fetchMetadataVO.getScriptNumber();

				log.info("Sucessfully Clicked OK clickButton" + scripNumber);

				String xpath = "(//div[contains(@id,'popup-container')]//button[@accesskey='K'])[1]";

				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;

			}

		} catch (Exception e) {

			String scripNumber = fetchMetadataVO.getScriptNumber();

			log.error("Failed during clickButton" + scripNumber);

			System.out.println(e);

		}
		// Action: Click Button

		try {

			if (param2.equalsIgnoreCase("OK")) {

				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				WebElement waittext = driver
						.findElement(By.xpath(("(//div[contains(@id,'popup-container')]//button[@accesskey='O'])[1]")));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittext).build().perform();

				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);

				Thread.sleep(15000);

				String scripNumber = fetchMetadataVO.getScriptNumber();

				log.info("Sucessfully Clicked OK clickButton" + scripNumber);

				String xpath = "(//div[contains(@id,'popup-container')]//button[@accesskey='O'])[1]";

				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;

			}

		} catch (Exception e) {

			String scripNumber = fetchMetadataVO.getScriptNumber();

			log.error("Failed during clickButton" + scripNumber);

			System.out.println(e);

		}

		// Action: Click Button

		try {

			if (param2.equalsIgnoreCase("OK")) {

				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				WebElement waittext = driver
						.findElement(By.xpath(("(//div[contains(@id,'popup-container')]//button[@accesskey='K'])[1]")));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittext).build().perform();

				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);

				Thread.sleep(15000);

				String scripNumber = fetchMetadataVO.getScriptNumber();

				log.info("Sucessfully Clicked OK clickButton" + scripNumber);

				String xpath = "(//div[contains(@id,'popup-container')]//button[@accesskey='K'])[1]";

				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;

			}

		} catch (Exception e) {

			String scripNumber = fetchMetadataVO.getScriptNumber();

			log.error("Failed during clickButton" + scripNumber);

			System.out.println(e);

		}

		// Action: Click Button

		try {

			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

			WebElement waittext = driver
					.findElement(By.xpath(("//div[contains(@class,'PopupMenu')]//*[text()='" + param1 + "']")));

			Actions actions = new Actions(driver);

			actions.moveToElement(waittext).build().perform();

			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);

			Thread.sleep(15000);

			String scripNumber = fetchMetadataVO.getScriptNumber();

			log.info("Sucessfully Clicked OK clickButton" + scripNumber);

			String xpath = "//div[contains(@class,'PopupMenu')]//*[text()='param1']";

			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;

		} catch (Exception e) {

			String scripNumber = fetchMetadataVO.getScriptNumber();

			log.error("Failed during clickButton" + scripNumber);

			System.out.println(e);

		}

		// Dh changes 7

		try {
			if (param1.equalsIgnoreCase("Finish")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver.findElement(By.xpath(("//span[text()='i'][1]")));
				// screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(3000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Create Time Card clickButton" + scripNumber);
				String xpath = "//span[text()='i'][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Create Time Card clickButton" + scripNumber);
		}

		try {

			if (param1.equalsIgnoreCase("Applied Receipts Register") && param2.equalsIgnoreCase("Apply")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver.findElement(By.xpath("//input[@value='Apply']"));
//                screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(15000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Create Time Card clickButton" + scripNumber);
				String xpath = "//input[@value='Apply']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed clickButton" + scripNumber);

			System.out.println(e);
		}
		// DH changes 6
		try {
			if (param1.equalsIgnoreCase("Issue Refund")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver
						.findElement(By.xpath("//div[text()='" + param1 + "']/following::*[text()='K']"));
				// //screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Create Time Card clickButton" + scripNumber);
				String xpath = "//div[contains(@class,'Overflow')]//div[@role='button']";
				// service.saveXpathParams(param1, param2, scripNumber, xpath);

				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Issue Refund ok clickButton" + scripNumber);

			System.out.println(e);
		}
		// DH changes 6
		try {
			if (param1.equalsIgnoreCase("Create Bank Account")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver
						.findElement(By.xpath("//*[text()='" + param1 + "']/following::*[text()='S']"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Create Time Card clickButton" + scripNumber);
				String xpath = "//div[contains(@class,'Overflow')]//div[@role='button']";
				// service.saveXpathParams(param1, param2, scripNumber, xpath);

				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Issue Refund ok clickButton" + scripNumber);

			System.out.println(e);
		}
		try {
			if ((param2.equalsIgnoreCase("Save and Close") && param1.equalsIgnoreCase("Manage Expenditure Types"))
					|| param1.equalsIgnoreCase("Manage Rate Schedules") || param2.equalsIgnoreCase("Save and Close")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver.findElement(By.xpath(("(//span[text()='S'])[2]")));
//                screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(15000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Create Time Card clickButton" + scripNumber);
				String xpath = "(//span[text()='S'])[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Create Time Card clickButton" + scripNumber);

			System.out.println(e);
		}
		// DH fix 4
		try {
			// Changed == to equals method
			if (param2.equalsIgnoreCase("OK")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(3000);
				// wait.until(ExpectedConditions
				// .presenceOfElementLocated(By.xpath(("//div[text()='" + param1 +
				// "']/following::button[text()='" + param2 + "'][1]"))));
				WebElement waittext = driver.findElement(
						By.xpath(("//div[text()='" + param1 + "']/following::button[text()='" + param2 + "'][1]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(5000);
				// screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked clickButton" + scripNumber);
				String xpath = "//div[normalize-space(text())='param1']";
				// service.saveXpathParams(param1, param2, scripNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickButton" + scripNumber);
			System.out.println(e);
		}

		// DH fix 4
		try {
			if (param1.equalsIgnoreCase("Transactions")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver
						.findElement(By.xpath(("//*[text()='" + param1 + "']/following::span[text()='o']")));
				// //screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Create Time Card clickButton" + scripNumber);
				String xpath = "//div[contains(@class,'Overflow')]//div[@role='button']";
				// service.saveXpathParams(param1, param2, scripNumber, xpath); return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Create Time Card clickButton" + scripNumber);
			System.out.println(e);
		}

		// DH fix4
		try {
			if (param1.equalsIgnoreCase("Accounts")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver.findElement(
						By.xpath("//*[contains(text(),'" + param1 + "')]/following::td[text()='" + param2 + "'][2]"));
				// //screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Create Time Card clickButton" + scripNumber);
				String xpath = "//div[contains(@class,'Overflow')]//div[@role='button']";
				// service.saveXpathParams(param1, param2, scripNumber, xpath);

				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Create Time Card clickButton" + scripNumber);

			System.out.println(e);
		}
		// for "PTP.PO.212 Split requisition lines" when exectuing in Fusion instance
		try {
			if (param1.equalsIgnoreCase("Create Address") && param2.equalsIgnoreCase("OK")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(8000);
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//*[normalize-space(text())='Create Address']/following::*[text()='K'][1]"))));
				WebElement waittext = driver.findElement(
						By.xpath(("//*[normalize-space(text())='Create Address']/following::*[text()='K'][1]")));
//                screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				Thread.sleep(4000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(4000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Ok clickButton" + scripNumber);
				String xpath = "//*[normalize-space(text())='Create Address']/following::*[text()='K'][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Ok clickButton" + scripNumber);
		}

		try {
			if (param1.equalsIgnoreCase("Submit")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver.findElement(By.xpath(("//*[text()='Submit' or text()='S']")));// screenshot(driver,
																											// "",
																											// fetchMetadataVO,
																											// fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(15000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Create Time Card clickButton" + scripNumber);
				String xpath = "//*[text()='Submit or text()='S']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickButton" + scripNumber);

			System.out.println(e);
		}
//for "PTP.AP.327 Applying a prepayment to Invoice" when exectuing in Fusion instance
		try {
			if (param2.equalsIgnoreCase("Save and Close") && !param1.equalsIgnoreCase("Unapply Application")
					&& !param1.equalsIgnoreCase("Manage Distributions") && !param1.equalsIgnoreCase("Manage Holds")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver.findElement(By.xpath(("//span[text()='S']")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(15000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Create Time Card clickButton" + scripNumber);
				String xpath = "//span[text()='S']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Create Time Card clickButton" + scripNumber);
		}
		try {
			if ((param1.equalsIgnoreCase("Manage Organization Trees")
					|| param1.equalsIgnoreCase("Edit Project Template")
					|| param1.equalsIgnoreCase("Manage Project Templates")) && param2.equalsIgnoreCase("Done")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("(//span[text()='o'])[2]"))));

				WebElement waittext = driver.findElement(By.xpath(("(//span[text()='o'])[2]")));
//                screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);

				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Done clickButton" + scripNumber);
				log.info("Sucessfully Clicked Done clickButton" + scripNumber);
				String xpath = "(//span[text()='o'])[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickButton Done" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Done")) {
				Thread.sleep(3000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(5000);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[text()='o']"))));
				WebElement waittext = driver.findElement(By.xpath(("//span[text()='o']")));
//                screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Done clickButton" + scripNumber);
				log.info("Sucessfully Clicked Done clickButton" + scripNumber);
				String xpath = "//span[text()='o']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickButton Done" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Notifications")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//h1[normalize-space(text())='Notifications']/following::button[text()='" + param2
								+ "'][1]"))));
				WebElement waittext = driver.findElement(
						By.xpath(("//h1[normalize-space(text())='Notifications']/following::button[text()='" + param2
								+ "'][1]")));
				// screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(15000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Members clickButton" + scripNumber);
				String xpath = "//button[@title='param2']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Members clickButton" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Expend")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver
						.findElement(By.xpath(("//div[contains(@class,'Overflow')]//div[@role='button']")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(15000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Create Time Card clickButton" + scripNumber);
				String xpath = "//div[contains(@class,'Overflow')]//div[@role='button']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Create Time Card clickButton" + scripNumber);

			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Create Time Card") && param2.equalsIgnoreCase("OK")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//*[text()='Create Time Card']/following::span[text()='K']"))));
				WebElement waittext = driver
						.findElement(By.xpath(("//*[text()='Create Time Card']/following::span[text()='K']")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(15000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Create Time Card clickButton" + scripNumber);
				String xpath = "//*[text()='Create Time Card']/following::span[text()='K']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Create Time Card clickButton" + scripNumber);

			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Edit Line")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(8000);
				System.out.println("here1234");
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						("//*[contains(text(),'" + param1 + "')]/following::span[normalize-space(text())='K']"))));
				WebElement waittext = driver.findElement(By.xpath(
						("//*[contains(text(),'" + param1 + "')]/following::span[normalize-space(text())='K']")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				Thread.sleep(4000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(4000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Edit Line clickButton" + scripNumber);
				String xpath = "//*[contains(text(),'param1')]/following::span[normalize-space(text())='K']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Edit Line clickButton" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Members") || param1.equalsIgnoreCase("Complete Report")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(
						ExpectedConditions.presenceOfElementLocated(By.xpath(("//button[@title='" + param2 + "']"))));
				WebElement waittext = driver.findElement(By.xpath(("//button[@title='" + param2 + "']")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(15000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Members clickButton" + scripNumber);
				String xpath = "//button[@title='param2']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Members clickButton" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Address Contacts") && param2.equalsIgnoreCase("OK")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"//*[normalize-space(text())='" + param1 + "']/following::button[@title='" + param2 + "']")));
				WebElement waittext = driver.findElement(By.xpath(
						"//*[normalize-space(text())='" + param1 + "']/following::button[@title='" + param2 + "']"));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Address Contacts clickButton" + scripNumber);
				String xpath = "//*[normalize-space(text())='" + param1 + "']/following::button[@title='param2']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Address Contacts clickButton" + scripNumber);
			System.out.println(e);
		}
		try {
			Thread.sleep(2000);
			if (param2.equalsIgnoreCase("OK")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(8000);
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//div[contains(@id,'RejectPopup::content')]//span[text()='K']"))));
				// wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[text()='ne']"),
				// "ne"));
				WebElement waittext = driver
						.findElement(By.xpath(("//div[contains(@id,'RejectPopup::content')]//span[text()='K']")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(4000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Ok clickButton" + scripNumber);
				String xpath = "//div[contains(@id,'RejectPopup::content')]//span[text()='K']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Ok clickButton" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param2.equalsIgnoreCase("OK")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(8000);
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//*[normalize-space(text())='Search']/following::*[normalize-space(text())='"
								+ param1 + "']/following::*[not (@aria-disabled) and text()='OK'][1]"))));
				WebElement waittext = driver.findElement(
						By.xpath(("//*[normalize-space(text())='Search']/following::*[normalize-space(text())='"
								+ param1 + "']/following::*[not (@aria-disabled) and text()='OK'][1]")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				Thread.sleep(4000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(4000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Ok clickButton" + scripNumber);
				String xpath = "//*[normalize-space(text())='Search']/following::*[normalize-space(text())='param1']/following::*[not (@aria-disabled) and text()='OK'][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Ok clickButton" + scripNumber);
		}
		try {
			if (param2.equalsIgnoreCase("OK")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(8000);
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//[contains(text(),'" + param1 + "')]/following::span[text()='K']"))));
				WebElement waittext = driver
						.findElement(By.xpath(("//[contains(text(),'" + param1 + "')]/following::span[text()='K']")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(4000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Ok clickButton" + scripNumber);
				String xpath = "//[contains(text(),'param1')]/following::span[text()='K']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Ok clickButton" + scripNumber);
		}
		try {

			if (param2.equalsIgnoreCase("Select")) {
				Thread.sleep(2000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("(//input[contains(@value,'" + param1
						+ "') and (@type)]/following::button[contains(text(),'" + param2 + "')])[1]"))));
				WebElement waittext = driver.findElement(By.xpath(("(//input[contains(@value,'" + param1
						+ "') and (@type)]/following::button[contains(text(),'" + param2 + "')])[1]")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(4000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Select clickButton" + scripNumber);
				String xpath = "(//input[contains(@value,'param1') and (@type)]/following::button[contains(text(),'param2')])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Select clickButton" + scripNumber);
		}
		try {
			if (param2.equalsIgnoreCase("Done")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(8000);
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//*[contains(@id,'tAccountPopup::content')]//*[text()='o']"))));
				WebElement waittext = driver
						.findElement(By.xpath(("//*[contains(@id,'tAccountPopup::content')]//*[text()='o']")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Done clickButton" + scripNumber);
				String xpath = "//*[contains(@id,'tAccountPopup::content')]//*[text()='o']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Done clickButton" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Apply")) {
				Thread.sleep(8000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@value='" + param1 + "']")));
				// wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()='l']"),
				// "l"));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath("//input[@value='" + param1 + "']"));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Apply clickButton" + scripNumber);
				String xpath = "//input[@value='param1']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Apply clickButton" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Done")) {
				Thread.sleep(3000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(5000);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[text()='ne']"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[text()='ne']"), "ne"));
				WebElement waittext = driver.findElement(By.xpath(("//*[text()='ne']")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Done clickButton" + scripNumber);
				log.info("Sucessfully Clicked Done clickButton" + scripNumber);
				String xpath = "//*[text()='ne']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param1.equalsIgnoreCase("Approval and Notification History")
					&& param2.equalsIgnoreCase("Done")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(5000);
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//div[contains(text(),'" + param1 + "')]/following::span[text()='o']"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(
						By.xpath("//div[contains(text(),'" + param1 + "')]/following::span[text()='o']"), "o"));
				WebElement waittext = driver.findElement(
						By.xpath(("//div[contains(text(),'" + param1 + "')]/following::span[text()='o']")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Approval and Notification History or Done clickButton" + scripNumber);
				String xpath = "//div[contains(text(),'param1')]/following::span[text()='o']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param2.equalsIgnoreCase("Done")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(5000);
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//*[contains(text(),'" + param1 + "')]/following::span[text()='o']"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(
						By.xpath("//*[contains(text(),'" + param1 + "')]/following::span[text()='o']"), "o"));
				WebElement waittext = driver
						.findElement(By.xpath(("//*[contains(text(),'" + param1 + "')]/following::span[text()='o']")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Done clickButton" + scripNumber);
				String xpath = "//*[contains(text(),'param1')]/following::span[text()='o']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param1.equalsIgnoreCase("Submit")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[text()='m']"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()='m']"), "m"));
				Thread.sleep(20000);
				WebElement waittext = driver.findElement(By.xpath(("//span[text()='m']")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(3000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Submit clickButton" + scripNumber);
				String xpath = "//span[text()='m']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param2.equalsIgnoreCase("Submit")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//*[normalize-space(text())='" + param1 + "']/following::span[text()='m']"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(
						By.xpath("//*[normalize-space(text())='" + param1 + "']/following::span[text()='m']"), "m"));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(
						By.xpath(("//*[normalize-space(text())='" + param1 + "']/following::span[text()='m']")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(3000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Submit clickButton" + scripNumber);
				String xpath = "//*[normalize-space(text())='param1']/following::span[text()='m']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param1.equalsIgnoreCase("Distributions")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='istributions']")));
				WebElement waittext = driver.findElement(By.xpath("//span[text()='istributions']"));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(3000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Distributions clickButton" + scripNumber);
				String xpath = "//span[text()='istributions']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if ((param1.equalsIgnoreCase("Manage Distributions") || param1.equalsIgnoreCase("Manage Holds"))
					&& param2.equalsIgnoreCase("Save and Close")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						("//*[normalize-space(text())='" + param1 + "']/following::button[text()='Save and Close']"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(
						"//*[normalize-space(text())='" + param1 + "']/following::button[text()='Save and Close']"),
						"Save and Close"));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath(
						("//*[normalize-space(text())='" + param1 + "']/following::button[text()='Save and Close']")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(3000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Manage Holds or Save and Close clickButton" + scripNumber);
				String xpath = "//*[normalize-space(text())='param1']/following::button[text()='Save and Close']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param2.equalsIgnoreCase("Save and Close")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//*[normalize-space(text())='" + param1 + "']/following::span[text()='S']"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(
						By.xpath("//*[normalize-space(text())='" + param1 + "']/following::span[text()='S']"), "S"));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(
						By.xpath(("//*[normalize-space(text())='" + param1 + "']/following::span[text()='S']")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(3000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath = "//*[normalize-space(text())='param1']/following::span[text()='S']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param1.equalsIgnoreCase("Next")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[text()='x']"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()='x']"), "x"));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath(("//span[text()='x']")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(8000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Next clickButton" + scripNumber);
				String xpath = "//span[text()='x']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param2.equalsIgnoreCase("Next")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1
						+ "']/following::*[normalize-space(text())='" + param2 + "']")));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[normalize-space(text())='"
						+ param1 + "']/following::*[normalize-space(text())='" + param2 + "']"), "x"));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
						+ "']/following::*[normalize-space(text())='" + param2 + "']"));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(8000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Next clickButton" + scripNumber);
				String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param2.equalsIgnoreCase("Yes")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath(("//div[@class='AFDetectExpansion']/following::*[text()='"
								+ param1 + "']/following::span[text()='Y']"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(
						By.xpath("//div[@class='AFDetectExpansion']/following::*[text()='" + param1
								+ "']/following::span[text()='Y']"),
						"Y"));
				Thread.sleep(6000);
				WebElement waittext = driver
						.findElement(By.xpath(("//div[@class='AFDetectExpansion']/following::*[text()='" + param1
								+ "']/following::span[text()='Y']")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(6000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Yes clickButton" + scripNumber);
				String xpath = "//div[@class='AFDetectExpansion']/following::*[text()='param1']/following::span[text()='Y']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param2.equalsIgnoreCase("OK")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("//button[@_afrpdo='ok' and @accesskey='K']")));
				wait.until(ExpectedConditions
						.textToBePresentInElementLocated(By.xpath("//button[@_afrpdo='ok' and @accesskey='K']"), "K"));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath("//button[@_afrpdo='ok' and @accesskey='K']"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(4000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked OK clickButton" + scripNumber);
				String xpath = "//button[@_afrpdo='ok' and @accesskey='K']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} // Here adding code for Advanced Button AP.452

			else if (param1.equalsIgnoreCase("Advanced")) {

				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				wait.until(ExpectedConditions

						.presenceOfElementLocated(By.xpath("//button[@accesskey='d']")));

				Thread.sleep(4000);

				WebElement waittext = driver.findElement(By.xpath("//button[@accesskey='d']"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittext).build().perform();

//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails); 

				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);

				Thread.sleep(4000);

				String scripNumber = fetchMetadataVO.getScriptNumber();

				log.info("Sucessfully Clicked OK clickButton" + scripNumber);

				String xpath = "//button[@accesskey='d']";

				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			} else if (param1.equalsIgnoreCase("Save and Close")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[text()='S']"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()='S']"), "S"));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath(("//span[text()='S']")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath = "//span[text()='S']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param1.equalsIgnoreCase("Continue")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[text()='u']"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()='u']"), "u"));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath(("//span[text()='u']")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Continue clickButton" + scripNumber);
				String xpath = "//span[text()='u']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param2.equalsIgnoreCase("Continue")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//button[text()='Contin']"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//button[text()='Contin']"),
						"Contin"));
				WebElement waittext = driver.findElement(By.xpath(("//button[text()='Contin']")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Continue clickButton" + scripNumber);
				String xpath = "//button[text()='Contin']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param1.equalsIgnoreCase("Close")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//button[text()='Cl']"))));
				Thread.sleep(5000);
				WebElement waittext = driver.findElement(By.xpath(("//button[text()='Cl']")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Close clickButton" + scripNumber);
				String xpath = "//button[text()='Cl']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param1.equalsIgnoreCase("Adjustment")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(3000);
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath(("(//span[text()='" + param1 + "'])[1]"))));
				WebElement waittext = driver.findElement(By.xpath(("(//span[text()='" + param1 + "'])[1]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(5000);
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Adjustment clickButton" + scripNumber);
				String xpath = "(//span[text()='param1'])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param1.equalsIgnoreCase("Cancel")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='C']")));
				Thread.sleep(5000);
				WebElement waittext = driver.findElement(By.xpath("//span[text()='C']"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Cancel clickButton" + scripNumber);
				String xpath = "//span[text()='C']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param1.equalsIgnoreCase("Save")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[text()='ave']"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()='ave']"), "ave"));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath(("//span[text()='ave']")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Save clickButton" + scripNumber);
				String xpath = "//span[text()='ave']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param1.equalsIgnoreCase("Apply")) {
				Thread.sleep(8000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[text()='l']"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()='l']"), "l"));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath(("//span[text()='l']")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Apply clickButton" + scripNumber);
				String xpath = "//span[text()='l']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param2.equalsIgnoreCase("Apply")) {
				Thread.sleep(4000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//*[normalize-space(text())='" + param1 + "']/following::span[text()='l']"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(
						By.xpath("//*[normalize-space(text())='" + param1 + "']/following::span[text()='l']"), "l"));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(
						By.xpath(("//*[normalize-space(text())='" + param1 + "']/following::span[text()='l']")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				Thread.sleep(2000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Apply clickButton" + scripNumber);
				String xpath = "//*[normalize-space(text())='param1']/following::span[text()='l']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param2.equalsIgnoreCase("Accept")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//*[normalize-space(text())='" + param1 + "']/following::span[text()='p']"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(
						By.xpath("//*[normalize-space(text())='" + param1 + "']/following::span[text()='p']"), "p"));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(
						By.xpath(("//*[normalize-space(text())='" + param1 + "']/following::span[text()='p']")));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Accept clickButton" + scripNumber);
				String xpath = "//*[normalize-space(text())='param1']/following::span[text()='p']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Apply clickButton" + scripNumber);
			System.out.println(e);

		}
		try {
			if (param2.equalsIgnoreCase("OK")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//*[normalize-space(text())=\"" + param1 + "\"]/following::span[text()='K']"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(
						By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::span[text()='K']"), "K"));
				WebElement waittext = driver.findElement(
						By.xpath(("//*[normalize-space(text())=\"" + param1 + "\"]/following::span[text()='K']")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(4000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked OK clickButton" + scripNumber);
				String xpath = "//*[normalize-space(text())='param1']/following::span[text()='K']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during OK clickButton" + scripNumber);
		}
		try {
			if (param1.equalsIgnoreCase("Add Application")) {
				try {
					WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
					wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[text()='A']"))));
					wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()='A']"), "A"));
					Thread.sleep(4000);
					WebElement waittext = driver.findElement(By.xpath(("//span[text()='A']")));
//					screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
					Actions actions = new Actions(driver);
					actions.moveToElement(waittext).build().perform();
					clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
					String scripNumber = fetchMetadataVO.getScriptNumber();
					log.info("Sucessfully Clicked add Application clickButton" + scripNumber);
					String xpath = "//span[text()='A']";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
				} catch (Exception e) {
					WebElement expand = driver
							.findElement(By.xpath(("//a[text()='Application']/following::div[@role='button'][2]")));
					expand.click();
					Thread.sleep(2000);
					WebElement waittext = driver.findElement(By.xpath(("//span[text()='A']")));
//					screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
					Actions actions = new Actions(driver);
					actions.moveToElement(waittext).build().perform();
					clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
					String scripNumber = fetchMetadataVO.getScriptNumber();
					log.error("Failed during add Application clickButton" + scripNumber);
					String xpath = "//a[text()='Application']/following::div[@role='button'][2]" + ">"
							+ "//span[text()='A']";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
				}
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during add Application clickButton" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Unapply Application")) {
				try {
					WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
					wait.until(ExpectedConditions
							.presenceOfElementLocated(By.xpath(("//button[text()='" + param1 + "']"))));
					wait.until(ExpectedConditions
							.textToBePresentInElementLocated(By.xpath("//button[text()='" + param1 + "']"), "param1"));
					Thread.sleep(4000);
					WebElement waittext = driver.findElement(By.xpath(("//button[text()='" + param1 + "']")));
//					screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
					Actions actions = new Actions(driver);
					actions.moveToElement(waittext).build().perform();
					clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
					Thread.sleep(4000);
					String scripNumber = fetchMetadataVO.getScriptNumber();
					log.info("Sucessfully Clicked Unapply Application clickButton" + scripNumber);
					String xpath = "//button[text()='param1']";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
				} catch (Exception e) {
					WebElement expand = driver
							.findElement(By.xpath(("//a[text()='Application']/following::div[@role='button'][2]")));
					expand.click();
					Thread.sleep(2000);
					WebElement waittext = driver.findElement(By.xpath(("//button[text()='" + param1 + "']")));
//					screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
					Actions actions = new Actions(driver);
					actions.moveToElement(waittext).build().perform();
					clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
					String scripNumber = fetchMetadataVO.getScriptNumber();
					log.error("Failed during Unapply Application clickButton" + scripNumber);
					String xpath = "//a[text()='Application']/following::div[@role='button'][2]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
				}
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Unapply Application clickButton" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param2.equalsIgnoreCase("Submit")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(3000);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[normalize-space(text())='"
						+ param1 + "']/following::span[normalize-space(text())='" + param2 + "']"))));
				WebElement waittext = driver.findElement(By.xpath(("//*[normalize-space(text())='" + param1
						+ "']/following::span[normalize-space(text())='" + param2 + "']")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(3000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Submit clickButton" + scripNumber);
				return;
			}
		} catch (Exception e) {
			// TODO: handle exception
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Submit clickButton" + scripNumber);
		}
		try {
			// Changed == to equals method
			if (!param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())='"
						+ param1 + "']/following::*[normalize-space(text())='" + param2 + "'])[1]")));
				WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())='" + param1
						+ "']/following::*[normalize-space(text())='" + param2 + "'])[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked clickButton" + scripNumber);
				String xpath = "(//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2'])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickButton" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Columns") || param1.equalsIgnoreCase("Show All")) {
				Thread.sleep(4000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("(//td[normalize-space(text())='" + param1 + "'])[2]")));
				// wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()='l']"),
				// "l"));
				Thread.sleep(4000);
				WebElement waittext = driver
						.findElement(By.xpath("(//td[normalize-space(text())='" + param1 + "'])[2]"));
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked  Columns or Show All clickButton" + scripNumber);
				String xpath = "(//td[normalize-space(text())='param1'])[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Columns or Show All clickButton" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Add to Document Builder")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(3000);
				wait.until(
						ExpectedConditions.presenceOfElementLocated(By.xpath(("//button[text()='" + param1 + "']"))));
				WebElement waittext = driver.findElement(By.xpath(("//button[text()='" + param1 + "']")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(3000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked  Add to Document Builder clickButton" + scripNumber);
				String xpath = "//button[text()='param1']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Add to Document Builder clickButton" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Freeze")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(3000);
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//tr[contains(@id,'HEADER_FREEZE')]//td[text()='" + param1 + "']"))));
				WebElement waittext = driver
						.findElement(By.xpath(("//tr[contains(@id,'HEADER_FREEZE')]//td[text()='" + param1 + "']")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(3000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked  Freeze clickButton" + scripNumber);
				String xpath = "//tr[contains(@id,'HEADER_FREEZE')]//td[text()='param1']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param1.equalsIgnoreCase("Unfreeze")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(3000);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						("//tr[contains(@id,'HEADER_UNFREEZE')]//td[normalize-space(text())='" + param1 + "']"))));
				WebElement waittext = driver.findElement(By.xpath(
						("//tr[contains(@id,'HEADER_UNFREEZE')]//td[normalize-space(text())='" + param1 + "']")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(3000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked  Unfreeze clickButton" + scripNumber);
				String xpath = "//tr[contains(@id,'HEADER_UNFREEZE')]//td[normalize-space(text())='param1']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param1.equalsIgnoreCase("Close")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(3000);
				wait.until(ExpectedConditions.presenceOfElementLocated(By
						.xpath(("//tr[contains(@id,'HEADER_CLOSE')]//td[normalize-space(text())='" + param1 + "']"))));
				WebElement waittext = driver.findElement(
						By.xpath(("//tr[contains(@id,'HEADER_CLOSE')]//td[normalize-space(text())='" + param1 + "']")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(3000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Close clickButton" + scripNumber);
				String xpath = "//tr[contains(@id,'HEADER_CLOSE')]//td[normalize-space(text())='param1']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param1.equalsIgnoreCase("Reopen")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(3000);
				wait.until(ExpectedConditions.presenceOfElementLocated(By
						.xpath(("//tr[contains(@id,'HEADER_REOPEN')]//td[normalize-space(text())='" + param1 + "']"))));
				WebElement waittext = driver.findElement(By
						.xpath(("//tr[contains(@id,'HEADER_REOPEN')]//td[normalize-space(text())='" + param1 + "']")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(3000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Reopen clickButton" + scripNumber);
				String xpath = "//tr[contains(@id,'HEADER_REOPEN')]//td[normalize-space(text())='param1']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param1.equalsIgnoreCase("Edit")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(3000);
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//tr[contains(@id,'HEADER_EDIT')]//td[normalize-space(text())='" + param1 + "']"))));
				WebElement waittext = driver.findElement(
						By.xpath(("//tr[contains(@id,'HEADER_EDIT')]//td[normalize-space(text())='" + param1 + "']")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(3000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Edit clickButton" + scripNumber);
				String xpath = "//tr[contains(@id,'HEADER_EDIT')]//td[normalize-space(text())='param1']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickButton" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Edit")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(3000);
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//tr[contains(@id,'commandMenuItem')]//td[text()='" + param1 + "']"))));
				WebElement waittext = driver
						.findElement(By.xpath(("//tr[contains(@id,'commandMenuItem')]//td[text()='" + param1 + "']")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(3000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Edit clickButton" + scripNumber);
				String xpath = "//tr[contains(@id,'commandMenuItem')]//td[text()='param1']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Edit clickButton" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Reverse")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By
						.xpath(("//div[@class='AFPopupMenuPopup']//td[(normalize-space(text())='" + param1 + "')]"))));
				WebElement waittext = driver.findElement(
						By.xpath(("//div[@class='AFPopupMenuPopup']//td[(normalize-space(text())='" + param1 + "')]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(2000);
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Reverse clickButton" + scripNumber);
				String xpath = "//div[@class='AFPopupMenuPopup']//td[(normalize-space(text())='param1')]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Reverse clickButton" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("PDF")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("//td[normalize-space(text())='" + param1 + "']")));
				WebElement waittext = driver.findElement(By.xpath("//td[normalize-space(text())='" + param1 + "']"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(60000);
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked  Columns or Show All clickButton" + scripNumber);
				String xpath = "//td[normalize-space(text())='param1']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Apply clickButton" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Republish")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("//button[normalize-space(text())='" + param1 + "']")));
				WebElement waittext = driver
						.findElement(By.xpath("//button[normalize-space(text())='" + param1 + "']"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(6000);
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Republish clickButton" + scripNumber);
				String xpath = "//button[normalize-space(text())='param1']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Republish clickButton" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Match Invoice Lines") && param2.equalsIgnoreCase("OK")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[text()='" + param1 + "']/following::*[text()='K'][2]")));
				WebElement waittext = driver
						.findElement(By.xpath("//*[text()='" + param1 + "']/following::*[text()='K'][2]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(6000);
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Republish clickButton" + scripNumber);
				String xpath = "//*[text()='" + param1 + "']/following::*[text()='K'][2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Republish clickButton" + scripNumber);
			System.out.println(e);
		}
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(3000);
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath(("//span[normalize-space(text())='" + param1 + "']"))));
				WebElement waittext = driver
						.findElement(By.xpath(("//span[normalize-space(text())='" + param1 + "']")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(5000);
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked  clickButton" + scripNumber);
				String xpath = "//span[normalize-space(text())='param1']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickButton" + scripNumber);
			System.out.println(e);
		}
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath(("//td[(normalize-space(text())='" + param1 + "')]"))));
				WebElement waittext = driver
						.findElement(By.xpath(("//td[(normalize-space(text())='" + param1 + "')]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(2000);
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked  clickButton" + scripNumber);
				String xpath = "//td[(normalize-space(text())='param1')]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickButton" + scripNumber);
			System.out.println(e);
		}
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//button[text()='" + param1 + "'and not(@style='display:none')]"))));
				WebElement waittext = driver
						.findElement(By.xpath(("//button[text()='" + param1 + "'and not(@style='display:none')]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(2000);
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked clickButton" + scripNumber);
				String xpath = "//button[text()='param1'and not(@style='display:none')]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickButton" + scripNumber);
			System.out.println(e);
		}
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(3000);
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath(("//div[normalize-space(text())='" + param1 + "']"))));
				WebElement waittext = driver.findElement(By.xpath(("//div[normalize-space(text())='" + param1 + "']")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(5000);
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked clickButton" + scripNumber);
				String xpath = "//div[normalize-space(text())='param1']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickButton" + scripNumber);
			System.out.println(e);
		}
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//div[contains(@class,'PopupMenu')]/following::*[text()='" + param1 + "']"))));
				WebElement waittext = driver.findElement(
						By.xpath(("//div[contains(@class,'PopupMenu')]/following::*[text()='" + param1 + "']")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(2000);
//				screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked  clickButton" + scripNumber);
				String xpath = "//div[contains(@class,'PopupMenu')]/following::*[text()='" + param1 + "']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickButton" + scripNumber);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())='" + param1
					+ "']/following::*[normalize-space(text())='" + param2 + "' and not(@_afrpdo)])[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())='" + param1
					+ "']/following::*[normalize-space(text())='" + param2 + "' and not(@_afrpdo)])[1]"));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(1000);
//			screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked clickButton" + scripNumber);
			String xpath = "(//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2' and not(@_afrpdo)])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickButton" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())='" + param2 + "'])[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())='" + param2 + "'])[1]"));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(3000);
//			screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked clickButton" + scripNumber);
			String xpath = "(//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2'])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickButton" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"(//*[normalize-space(text())=\"" + param1 + "\"]/following::*[@title='" + param2 + "'])[1]")));
			WebElement waittext = driver.findElement(By.xpath(
					"(//*[normalize-space(text())=\"" + param1 + "\"]/following::*[@title='" + param2 + "'])[1]"));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(3000);
//			screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked clickButton" + scripNumber);
			String xpath = "(//*[normalize-space(text())='param1']/following::*[@title='param2'])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickButton" + scripNumber);
			System.out.println(e);
		}

		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			Thread.sleep(3000);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[contains(text(),'" + param1
					+ "')]/following::*[normalize-space(text())='" + param2 + "'][1]"))));
			WebElement waittext = driver.findElement(By.xpath(("//*[contains(text(),'" + param1
					+ "')]/following::*[normalize-space(text())='" + param2 + "'][1]")));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(5000);
//			screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked clickButton" + scripNumber);
			String xpath = "//*[contains(text(),'param1')]/following::*[normalize-space(text())='param2'][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickButton" + scripNumber);
		}
//		   try {
//	              String text = driver.findElement(By.xpath("//td[@class='AFNoteWindow']")).getText();
//	              fetchConfigVO.setErrormessage(text);
//	  			return;
//	        } catch (Exception e) {
//	            System.out.println(e);
//	        }try {
//	              String text = driver.findElement(By.xpath("//div[contains(@class,'Error')]")).getText();
//	              fetchConfigVO.setErrormessage(text);
//	  			return;
//	        } catch (Exception e) {
//	            System.out.println(e);
//	        }
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())='" + param1
					+ "']/following::*[normalize-space(text())='" + param2 + "'])[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())='" + param1
					+ "']/following::*[normalize-space(text())='" + param2 + "'])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
//			screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(1000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked clickButton" + scripNumber);
			String xpath = "(//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2'])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickButton" + scripNumber);
			System.out.println(e);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}

	}

	public void clickTableLink(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			if (param1.equalsIgnoreCase("Manage Receipts") || param1.equalsIgnoreCase("Manage Customers")) {
				Thread.sleep(3000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[normalize-space(text())='"
						+ param1 + "']/following::table[@summary='" + param2 + "']//a)[2]/parent::span")));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath("(//h1[normalize-space(text())='" + param1
						+ "']/following::table[@summary='" + param2 + "']//a)[2]/parent::span"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				actions.click(waittext).build().perform();
				Thread.sleep(8000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked clickTableLink" + scripNumber);
				String xpath = "(//h1[normalize-space(text())='param1']/following::table[@summary='param2']//a)[2]/parent::span";
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
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(),'" + param1
						+ "')]/following::table[@summary='" + param2 + "']//span[@title]")));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath("//*[contains(text(),'" + param1
						+ "')]/following::table[@summary='" + param2 + "']//span[@title]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(2000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked List of Processes Meeting Search Criteria clickTableLink" + scripNumber);
				String xpath = "(//table[@summary='param1']//td[2]//span)[1]";
				// service.saveXpathParams(param1, param2, scripNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during List of Processes Meeting Search Criteria clickTableLink" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Manage Journals") || param1.equalsIgnoreCase("Journal Lines")
					|| param1.equalsIgnoreCase("Manage Transactions") || param1.equalsIgnoreCase("Prepare Source lines")
					|| param1.equalsIgnoreCase("Contracts")) {
				Thread.sleep(3000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[normalize-space(text())='"
						+ param1 + "']/following::table[@summary='" + param2 + "']//a)[2]")));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath("(//h1[normalize-space(text())='" + param1
						+ "']/following::table[@summary='" + param2 + "']//a)[2]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				actions.click(waittext).build().perform();
				Thread.sleep(8000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked clickTableLink" + scripNumber);
				String xpath = "(//h1[normalize-space(text())='param1']/following::table[@summary='param2']//a)[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickTableLink" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Addresses")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("(//table[@summary='" + param1 + "']//a)[2]")));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath("(//table[@summary='" + param1 + "']//a)[2]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				actions.click(waittext).build().perform();
				Thread.sleep(8000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Addresses clickTableLink" + scripNumber);
				String xpath = "(//table[@summary='param1']//a)[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Addresses clickTableLink" + scripNumber);
			System.out.println(e);
		}

		try {
			if (param1.equalsIgnoreCase("Source Lines")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"(//table[@summary='" + param1 + "']//a[not (contains(@title,'Required information'))])[1]")));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath(
						"(//table[@summary='" + param1 + "']//a[not (contains(@title,'Required information'))])[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				actions.click(waittext).build().perform();
				Thread.sleep(8000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Addresses clickTableLink" + scripNumber);
				String xpath = "(//table[@summary='\" +param1+ \"']//a[not (contains(@title,'Required information'))])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Addresses clickTableLink" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param2.equalsIgnoreCase("Approved")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//table[@summary='" + param1
						+ "']//*[normalize-space(text())='" + param2 + "']/following::a)[1]")));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath("(//table[@summary='" + param1
						+ "']//*[normalize-space(text())='" + param2 + "']/following::a)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				actions.click(waittext).build().perform();
				Thread.sleep(8000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Approved clickTableLink" + scripNumber);
				String xpath = "(//table[@summary='param1']//*[normalize-space(text())='param2']/following::a)[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Approved clickTableLink" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Manage Orders")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//h1[normalize-space(text())='" + param1 + "']/following::table[@summary='" + param2
								+ "']//a[contains(@title,'Purchase Order')]")));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath("//h1[normalize-space(text())='" + param1
						+ "']/following::table[@summary='" + param2 + "']//a[contains(@title,'Purchase Order')]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(2000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Manage Orders clickTableLink" + scripNumber);
				String xpath = "//h1[normalize-space(text())='param1']/following::table[@summary='param2']//a[contains(@title,'Purchase Order')]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Manage Orders clickTableLink" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Manage Receipts")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("(//table[@summary='" + param2 + "']//td)[1]")));
				WebElement waittext = driver.findElement(By.xpath("(//table[@summary='" + param2 + "']//td)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(2000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Manage Receipts clickTableLink" + scripNumber);
				String xpath = "(//table[@summary='param2']//td)[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Manage Receipts clickTableLink" + scripNumber);
			System.out.println(e);
		}
		// Adding Xpath for 'Checking the dashboard for unposted & journals in error for
		// all journals(GL.125)’
		try {

			if (param1.equalsIgnoreCase("Journals") && param2.equalsIgnoreCase("Requiring Attention")) {

				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				wait.until(ExpectedConditions

						.presenceOfElementLocated(By.xpath(
								"//h2[text()='" + param1 + "']//following::table[@summary='" + param2 + "']//a[1]")));

				WebElement waittext = driver.findElement(
						By.xpath("//h2[text()='" + param1 + "']//following::table[@summary='" + param2 + "']//a[1]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittext).build().perform();

				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);

				tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);

				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);

				Thread.sleep(2000);

				String scripNumber = fetchMetadataVO.getScriptNumber();

				log.info("Sucessfully Clicked Journals in Requiring Attention clickTableLink" + scripNumber);

				String xpath = "//h2[text()='param1']//following::table[@summary='param2']//a[1]";

				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;

			}

		} catch (Exception e) {

			String scripNumber = fetchMetadataVO.getScriptNumber();

			log.error("Failed during Journals in Requiring Attention clickTableLink" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("List of Processes Meeting Search Criteria")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("(//table[@summary='" + param1 + "']//td[2]//span)[1]")));
				Thread.sleep(4000);
				WebElement waittext = driver
						.findElement(By.xpath("(//table[@summary='" + param1 + "']//td[2]//span)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(2000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked List of Processes Meeting Search Criteria clickTableLink" + scripNumber);
				String xpath = "(//table[@summary='param1']//td[2]//span)[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during List of Processes Meeting Search Criteria clickTableLink" + scripNumber);
			System.out.println(e);
		}
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("(//table[@summary='" + param1 + "']//a[not (@title)])[1]")));
				Thread.sleep(4000);
				WebElement waittext = driver
						.findElement(By.xpath("(//table[@summary='" + param1 + "']//a[not (@title)])[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(2000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked  clickTableLink" + scripNumber);
				String params = param1;
				String xpath = "(//table[@summary='param1']//a[not (@title)])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickTableLink" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[normalize-space(text())='" + param1
					+ "']/following::img[@title='" + param2 + "']/following-sibling::a[1]")));
			Thread.sleep(4000);
			WebElement waittext = driver.findElement(By.xpath("//h1[normalize-space(text())='" + param1
					+ "']/following::img[@title='" + param2 + "']/following-sibling::a[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(2000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked clickTableLink" + scripNumber);
			String xpath = "//h1[normalize-space(text())='param1']/following::img[@title='param2']/following-sibling::a[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickTableLink" + scripNumber);
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
						By.xpath("(//div[@class='AFDetectExpansion']/following::span[normalize-space(text())='" + param1
								+ "']/following::table//span[text()])[1]")));
				WebElement waittext = driver.findElement(
						By.xpath("(//div[@class='AFDetectExpansion']/following::span[normalize-space(text())='" + param1
								+ "']/following::table//span[text()])[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(4000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked tableRowSelect" + scripNumber);
				String xpath = "(//div[@class='AFDetectExpansion']/following::span[normalize-space(text())='param1']/following::table//span[text()])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during tableRowSelect" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("SecondLine")) {
				Thread.sleep(4000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("(//table[@summary='" + param2 + "']//tr[2]//td)[1]")));
				WebElement waittext = driver
						.findElement(By.xpath("(//table[@summary='" + param2 + "']//tr[2]//td)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(10000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked SecondLine tableRowSelect" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during SecondLine tableRowSelect" + scripNumber);
			System.out.println(e);
		}
		try {
			Thread.sleep(6000);
			// System.out.println("Here1 came");
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("(//table[@summary='" + param1 + "']//td)[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//table[@summary='" + param1 + "']//td)[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			waittext.click();
			// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(10000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked Here1 came tableRowSelect" + scripNumber);
			String xpath = "(//table[@summary='param1']//td)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Here1 came tableRowSelect" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"//h1[normalize-space(text())='" + param1 + "']/following::table[@summary='" + param1 + "'][1]")));
			WebElement waittext = driver.findElement(By.xpath(
					"//h1[normalize-space(text())='" + param1 + "']/following::table[@summary='" + param1 + "'][1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(4000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked tableRowSelect" + scripNumber);
			String xpath = "//h1[normalize-space(text())='param1']/following::table[@summary='param1'][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during tableRowSelect" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1
					+ "']/following::*[normalize-space(text())='" + param2 + "']/following::tr[1]/td[1]")));
			WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
					+ "']/following::*[normalize-space(text())='" + param2 + "']/following::tr[1]/td[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(2000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked tableRowSelect" + scripNumber);
			String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']/following::tr[1]/td[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during tableRowSelect" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("")));
			WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
					+ "']/following::*[normalize-space(text())='" + param2 + "']/following::tr[1]/td[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(2000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked tableRowSelect" + scripNumber);
			String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']/following::tr[1]/td[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during tableRowSelect" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			// throw e;
		}
		// New Code for NTA.SO.4
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(
					ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@summary,'" + param1 + "')]")));
			WebElement waittext = driver.findElement(By.xpath("//*[contains(@summary,'" + param1 + "')]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(2000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked tableRowSelect" + scripNumber);
			String xpath = "//*[contains(@summary,'param1')]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during tableRowSelect" + scripNumber);
			System.out.println(e);
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

							.presenceOfElementLocated(By.xpath("//a[contains(@id,'" + param1 + "')]")));

					WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1 + "']"));

					Actions actions = new Actions(driver);

					actions.moveToElement(waittext).build().perform();

					waittext.click();

					screenshot(driver, fetchMetadataVO, customerDetails);

					refreshPage(driver, fetchMetadataVO, fetchConfigVO, customerDetails);

					Thread.sleep(5000);

					String scripNumber = fetchMetadataVO.getScriptNumber();

					log.info("Sucessfully Clicked Approve clickLink" + scripNumber);

					String xpath = "//a[contains(@id,'param1')]";

					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);

					return;

				}

			} catch (Exception e) {

				System.out.println(e);

				String scripNumber = fetchMetadataVO.getScriptNumber();

				log.error("Failed during Approve clickLink" + scripNumber);

			}
			// Here adding code for Scanned invoices in AP.453

			try {

				if (param1.equalsIgnoreCase("Invoices")) {

					WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

					wait.until(ExpectedConditions.presenceOfElementLocated(
							By.xpath("//span[text()='" + param2 + "']/following::span[text() > '0'][1]")));

					WebElement waittext = driver
							.findElement(By.xpath("//span[text()='" + param2 + "']/following::span[text() > '0'][1]"));

					Actions actions = new Actions(driver);

					actions.moveToElement(waittext).build().perform();

					waittext.click();

					Thread.sleep(6000);

					screenshot(driver, fetchMetadataVO, customerDetails);

					String scripNumber = fetchMetadataVO.getScriptNumber();

					log.info("Sucessfully Clicked Scanned clickLink" + scripNumber);

					String xpath = "//span[text()='param2']/following::span[text() > '0'][1]";

					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
					return;

				}

			} catch (Exception e) {

				String scripNumber = fetchMetadataVO.getScriptNumber();

				log.error("Failed during Scanned clickLink" + scripNumber);

				System.out.println(e);

			}
			// DH fix
			try {
				if (param1.equalsIgnoreCase("Accounts")) {
					Thread.sleep(70000);
					WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
					// wait.until(ExpectedConditions
					// .presenceOfElementLocated(By.xpath("//a[normalize-space(text())='" + param1 +
					// "']")));
					WebElement waittext = driver.findElement(By
							.xpath("//*[contains(text(),'" + param1 + "')]/following::*[text()='" + param2 + "'][1]"));
					Actions actions = new Actions(driver);
					actions.moveToElement(waittext).build().perform();
					// actions.moveToElement(Keys.PAGE_DOWN).build().perform();
					waittext.click();
					screenshot(driver, fetchMetadataVO, customerDetails);
					String scripNumber = fetchMetadataVO.getScriptNumber();
					log.info("Sucessfully Clicked Export clickLink" + scripNumber);
					String xpath = "//a[normalize-space(text())='param1']";
					// service.saveXpathParams(param1, param2, scripNumber, xpath);

					Thread.sleep(5000);
					return;
				}
			} catch (Exception e) {
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.error("Failed during Export clickLink" + scripNumber);
				System.out.println(e);
			}
			if (param1.equalsIgnoreCase("Report") && param2.equalsIgnoreCase("Apply")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@value='" + param2 + "']")));
				WebElement waittext = driver.findElement(By.xpath("//input[@value='" + param2 + "']"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				Thread.sleep(6000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked clickLink" + scripNumber);
				String xpath = "//input[@value='param2']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickLink" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Home")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//a[contains(@id,'UIShome')])[1]")));
				WebElement waittext = driver.findElement(By.xpath("(//a[contains(@id,'UIShome')])[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				refreshPage(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Home clickLink" + scripNumber);
				String xpath = "(//a[contains(@id,'UIShome')])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Home clickLink" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Financials Details") && param2.equalsIgnoreCase("Invoices")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[contains(text(),'" + param1 + "')]/following::*[text()='" + param1 + "')[1]")));
				WebElement waittext = driver.findElement(
						By.xpath("//*[contains(text(),'" + param1 + "')]/following::*[text()='" + param1 + "')[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				refreshPage(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Financials Details or Invoices clickLink" + scripNumber);
				String xpath = "//*[contains(text(),'param1')]/following::*[text()='param1')[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Financials Details or Invoices  clickLink" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Approve")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1 + "']")));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1 + "']"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				refreshPage(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Approve clickLink" + scripNumber);
				String xpath = "//*[normalize-space(text())='param1']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Approve clickLink" + scripNumber);
		}
		try {
			if (param1.equalsIgnoreCase("Payables to Ledger Reconciliation Summary")
					&& param2.equalsIgnoreCase("Export")) {
				Thread.sleep(8000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//label[normalize-space(text())='"
						+ param1 + "']/following::a[normalize-space(text())='" + param2 + "']")));
				WebElement waittext = driver.findElement(By.xpath("//label[normalize-space(text())='" + param1
						+ "']/following::a[normalize-space(text())='" + param2 + "']"));
				Actions actions = new Actions(driver);
				// actions.moveToElement(waittext).build().perform();
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
				js.executeScript("window.scrollBy(0,1000)");
				waittext.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Payables to Ledger Reconciliation Summary clickLink" + scripNumber);
				String xpath = "//label[normalize-space(text())='param1']/following::a[normalize-space(text())='param2']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Payables to Ledger Reconciliation Summary clickLink" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Export")) {
				Thread.sleep(70000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("//a[normalize-space(text())='" + param1 + "']")));
				WebElement waittext = driver.findElement(By.xpath("//a[normalize-space(text())='" + param1 + "']"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				refreshPage(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Export clickLink" + scripNumber);
				String xpath = "//a[normalize-space(text())='param1']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Export clickLink" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Export")) {
				Thread.sleep(70000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("//a[normalize-space(text())='" + param1 + "']")));
				WebElement waittext = driver.findElement(By.xpath("//a[normalize-space(text())='" + param1 + "']"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// actions.moveToElement(Keys.PAGE_DOWN).build().perform();
				waittext.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Export clickLink" + scripNumber);
				String xpath = "//a[normalize-space(text())='param1']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				Thread.sleep(5000);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Export clickLink" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Project")) {
				try {
					Thread.sleep(70000);
					WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
					wait.until(ExpectedConditions
							.presenceOfElementLocated(By.xpath("//a[normalize-space(text())='" + param1 + "']")));
					WebElement waittext = driver.findElement(By.xpath("//a[normalize-space(text())='" + param1 + "']"));
					Actions actions = new Actions(driver);
					actions.moveToElement(waittext).build().perform();
					waittext.click();
					screenshot(driver, fetchMetadataVO, customerDetails);
					// refreshPage(driver, fetchMetadataVO, fetchConfigVO);
					String scripNumber = fetchMetadataVO.getScriptNumber();
					log.info("Sucessfully Clicked Project clickLink" + scripNumber);
					String xpath = "//a[normalize-space(text())='param1']";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);

					Thread.sleep(5000);
				} catch (Exception e) {
					WebElement expand = driver
							.findElement(By.xpath(("//span[text()='Allocate']/following::div[@role='button'][2]")));
					expand.click();
					Thread.sleep(2000);
					WebElement waittext = driver
							.findElement(By.xpath(("//a[normalize-space(text())='" + param1 + "']")));
					screenshot(driver, fetchMetadataVO, customerDetails);
					Actions actions = new Actions(driver);
					actions.moveToElement(waittext).build().perform();
					waittext.click();
					String scripNumber = fetchMetadataVO.getScriptNumber();
					log.error("Failed during Project clickLink" + scripNumber);
				}
				return;
			}

		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickLink" + scripNumber);
		}
		try {
			if (param1.equalsIgnoreCase("Financial Reporting Center")) {
				Thread.sleep(2000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath(("//a[normalize-space(text())='" + param2 + "'][1]"))));
				WebElement waittext = driver
						.findElement(By.xpath(("//a[normalize-space(text())='" + param2 + "'][1]")));
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
				log.info("Sucessfully Clicked Financial Reporting Center clickLink" + scripNumber);
				String xpath = "//a[normalize-space(text())='param2'][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Financial Reporting Center clickLink" + scripNumber);
		}
		try {
			if (param1.equalsIgnoreCase("Receivables")) {
				Thread.sleep(2000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath(("//a[normalize-space(text())='" + param1 + "'][1]"))));
				WebElement waittext = driver
						.findElement(By.xpath(("//a[normalize-space(text())='" + param1 + "'][1]")));
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
				log.info("Sucessfully Clicked Receivables clickLink" + scripNumber);
				String xpath = "//a[normalize-space(text())='param1'][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Receivables clickLink" + scripNumber);
			System.out.println(e);
		}
		// for "PTP.PO.212 Split requisition lines" when exectuing in Fusion instance
		try {
			if (param1.equalsIgnoreCase("Requisition Lines") && param2.equalsIgnoreCase("Actions")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//h2[text()='" + param1 + "']/following::a[text()='Actions']")));
				WebElement waittext = driver
						.findElement(By.xpath("//h2[text()='" + param1 + "']/following::a[text()='Actions']"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Requisition Lines clickLink" + scripNumber);
				String xpath = "//table[@summary='param1']//span[text()='Approved']/following::a[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Requisition Lines clickLink" + scripNumber);
			System.out.println(e);
		}

		try {
			if (param1.equalsIgnoreCase("Requisition Lines")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//table[@summary='" + param1 + "']//span[text()='Approved']/following::a[1]")));
				WebElement waittext = driver.findElement(
						By.xpath("//table[@summary='" + param1 + "']//span[text()='Approved']/following::a[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Requisition Lines clickLink" + scripNumber);
				String xpath = "//table[@summary='param1']//span[text()='Approved']/following::a[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Requisition Lines clickLink" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Details")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("(//*[contains(text(),'" + param1 + "')]/following::*[text()='" + param2 + "'])[1]")));
				WebElement waittext = driver.findElement(
						By.xpath("(//*[contains(text(),'" + param1 + "')]/following::*[text()='" + param2 + "'])[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Details" + scripNumber);

				String xpath = "(//*[contains(text(),'param1')]/following::*[text()='param2'])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Details" + scripNumber);
			System.out.println(e);
		}
		try {
			// Changed == to equals method
			if (!param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[normalize-space(text())='"
						+ param1 + "']/following::label[normalize-space(text())='" + param2 + "']/following::a[1]")));
				Thread.sleep(4000);
				wait.until(
						ExpectedConditions
								.textToBePresentInElementLocated(
										By.xpath("//h1[normalize-space(text())='" + param1
												+ "']/following::label[normalize-space(text())='" + param2 + "']"),
										param2));
				WebElement waittext = driver.findElement(By.xpath("//h1[normalize-space(text())='" + param1
						+ "']/following::label[normalize-space(text())='" + param2 + "']/following::a[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked clickLink" + scripNumber);
				String xpath = "//h1[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/following::a[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickLink" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Journal")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[normalize-space(text())='"
						+ param1 + "']/following::a[normalize-space(text())='" + param2 + "']")));
				Thread.sleep(4000);
				wait.until(
						ExpectedConditions
								.textToBePresentInElementLocated(
										By.xpath("//h1[normalize-space(text())='" + param1
												+ "']/following::a[normalize-space(text())='" + param2 + "']"),
										param2));
				WebElement waittext = driver.findElement(By.xpath("//h1[normalize-space(text())='" + param1
						+ "']/following::a[normalize-space(text())='" + param2 + "']"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Journal clickLink" + scripNumber);
				String xpath = "//h1[normalize-space(text())='param1']/following::a[normalize-space(text())='param2']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Journal clickLink" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Receipt Details") || param1.equalsIgnoreCase("General Information")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1
						+ "']/following::*[normalize-space(text())='" + param2 + "'][1]")));
				Thread.sleep(4000);
				wait.until(
						ExpectedConditions
								.textToBePresentInElementLocated(
										By.xpath("//*[normalize-space(text())='" + param1
												+ "']/following::*[normalize-space(text())='" + param2 + "']"),
										param2));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
						+ "']/following::*[normalize-space(text())='" + param2 + "'][1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Receipt Details clickLink" + scripNumber);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Receipt Details clickLink" + scripNumber);
		}
		try {
			if (param1.equalsIgnoreCase("View")) {
				Thread.sleep(3000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//a[text()='" + param1 + "'][1]"))));
				WebElement waittext = driver.findElement(By.xpath("//a[text()='" + param1 + "'][1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				Thread.sleep(5000);
				waittext.click();
				Thread.sleep(2000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked View clickLink" + scripNumber);
				String xpath = "//a[text()='param1'][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during View clickLink" + scripNumber);
		}
		try {
			if (param1.equalsIgnoreCase("Invoice Actions")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(4000);
				WebElement Continue = driver
						.findElement(By.xpath("//div[text()='Warning']/following::button[text()='Continue']"));
				Continue.click();
				Thread.sleep(10000);
				WebElement waittext = driver.findElement(By.xpath("//a[normalize-space(text())='" + param1 + "'][1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				Thread.sleep(2000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Invoice Actions clickLink" + scripNumber);
				String xpath = "//div[text()='Warning']/following::button[text()='Continue']" + ";"
						+ "//a[normalize-space(text())='param1'][1]";
				return;
			}
			try {
				// Changed == to equals method
				if (param2.equals("")) {
					Thread.sleep(3000);
					WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
					wait.until(
							ExpectedConditions.presenceOfElementLocated(By.xpath(("//a[text()='" + param1 + "'][1]"))));
					WebElement waittext = driver.findElement(By.xpath("//a[text()='" + param1 + "'][1]"));
					Actions actions = new Actions(driver);
					actions.moveToElement(waittext).build().perform();
					Thread.sleep(5000);
					waittext.click();
					Thread.sleep(2000);
					screenshot(driver, fetchMetadataVO, customerDetails);
					String scripNumber = fetchMetadataVO.getScriptNumber();
					log.info("Sucessfully Clicked clickLink" + scripNumber);
					String params = param1;
					String xpath = "//a[text()='param1'][1]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
					return;
				}
			} catch (Exception e) {
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.error("Failed during clickLink" + scripNumber);
				System.out.println(e);
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Invoice Actions clickLink" + scripNumber);
			System.out.println(e);
		}
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				Thread.sleep(3000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath(("//a[normalize-space(text())='" + param1 + "'][1]"))));
				WebElement waittext = driver.findElement(By.xpath("//a[normalize-space(text())='" + param1 + "'][1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				Thread.sleep(5000);
				waittext.click();
				Thread.sleep(2000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked clickLink" + scripNumber);
				String params = param1;
				String xpath = "//a[normalize-space(text())='param1'][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickLink" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Reports and Analytics")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//h1[normalize-space(text())='"
						+ param1 + "']/following::*[normalize-space(text())='" + param2 + "']"))));
				WebElement waittext = driver.findElement(By.xpath(("//h1[normalize-space(text())='" + param1
						+ "']/following::*[normalize-space(text())='" + param2 + "']")));
				Thread.sleep(5000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				Thread.sleep(2000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Reports and Analytics clickLink" + scripNumber);
				String xpath = "//h1[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Reports and Analytics clickLink" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Attachment") || param1.equalsIgnoreCase("Invoice Summary")
					|| param1.equalsIgnoreCase("Attachments")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//*[normalize-space(text())='" + param1 + "']/following::a[1]"))));
				WebElement waittext = driver
						.findElement(By.xpath(("//*[normalize-space(text())='" + param1 + "']/following::a[1]")));
				Thread.sleep(5000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				Thread.sleep(2000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Attachment or Invoice Summary clickLink" + scripNumber);
				String xpath = "//*[normalize-space(text())='param1']/following::a[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Attachment or Invoice Summary clickLink" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[normalize-space(text())='" + param1
					+ "']/following::*[normalize-space(text())='" + param2 + "']/following::span)[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//h1[normalize-space(text())='" + param1
					+ "']/following::*[normalize-space(text())='" + param2 + "']/following::span)[1]"));
			Thread.sleep(5000);
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			waittext.click();
			Thread.sleep(2000);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked clickLink" + scripNumber);
			String xpath = "(//h1[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']/following::span)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickLink" + scripNumber);
			System.out.println(e);
		}
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("//a[contains(text(),'" + param1 + "')]")));
				WebElement waittext = driver.findElement(By.xpath("//a[contains(text(),'" + param1 + "')]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked clickLink" + scripNumber);
				String params = param1;
				String xpath = "//a[contains(text(),'param1')]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickLink" + scripNumber);
			System.out.println(e);
		}
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("(//a[contains(@id,'" + param1 + "')])[1]")));
				WebElement waittext = driver.findElement(By.xpath("(//a[contains(@id,'" + param1 + "')])[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked clickLink" + scripNumber);
				String params = param1;
				String xpath = "(//a[contains(@id,'param1')])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickLink" + scripNumber);
			System.out.println(e);
		}
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@title='" + param1 + "']")));
				WebElement waittext = driver.findElement(By.xpath("//div[@title='" + param1 + "']"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked clickLink" + scripNumber);
				String params = param1;
				String xpath = "//div[@title='param1']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickLink" + scripNumber);
			System.out.println(e);
		}
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@title='" + param1 + "']")));
				WebElement waittext = driver.findElement(By.xpath("//a[@title='" + param1 + "']"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked clickLink" + scripNumber);

				String params = param1;
				String xpath = "//a[@title='param1']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickLink" + scripNumber);
			System.out.println(e);
		}
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("//*[contains(@title,'" + param1 + "')]")));
				WebElement waittext = driver.findElement(By.xpath("//*[contains(@title,'" + param1 + "')]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked clickLink" + scripNumber);
				String params = param1;
				String xpath = "//*[contains(@title,'param1')]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickLink" + scripNumber);
			System.out.println(e);
		}
		// Need to check for what purpose
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("(//div[contains(text(),'" + param1 + "')])[2]")));
				WebElement waittext = driver.findElement(By.xpath("(//div[contains(text(),'" + param1 + "')])[2]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked clickLink" + scripNumber);
				String params = param1;
				String xpath = "(//div[contains(text(),'param1')])[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickLink" + scripNumber);
			System.out.println(e);
		}
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//a[@role='" + param1 + "']"))));
				WebElement waittext = driver.findElement(By.xpath(("//a[@role='" + param1 + "']")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked clickLink" + scripNumber);
				String params = param1;
				String xpath = "//a[@role='param1']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickLink" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[normalize-space(text())='" + param1
					+ "']/following::a[normalize-space(text())='" + param2 + "'][1]"))));
			WebElement waittext = driver.findElement(By.xpath(("//*[normalize-space(text())='" + param1
					+ "']/following::a[normalize-space(text())='" + param2 + "'][1]")));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			waittext.click();
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked clickLink" + scripNumber);
			String xpath = "//*[normalize-space(text())='param1']/following::a[normalize-space(text())='param2'][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickLink" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
//			throw e;
		}
		// DH 29
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath(("//h1[contains(text(),'" + param1 + "')]/following::*[text()='" + param2 + "'][1]"))));
			WebElement waittext = driver.findElement(
					By.xpath(("//h1[contains(text(),'" + param1 + "')]/following::*[text()='" + param2 + "'][1]")));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			waittext.click();
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked clickLink" + scripNumber);
			String xpath = "//h1[contains(text(),'param1')]/following::*[text()='param2'][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickLink" + scripNumber);
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
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//*[normalize-space(text())='" + param1 + "']/following::*[normalize-space(text())='"
								+ keysToSend + "']/preceding::input[@type='radio']"))));
				WebElement waittext = driver.findElement(
						By.xpath(("//*[normalize-space(text())='" + param1 + "']/following::*[normalize-space(text())='"
								+ keysToSend + "']/preceding::input[@type='radio']")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(500);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked clickRadiobutton" + scripNumber);
				String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='keysToSend']/preceding::input[@type='radio']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickRadiobutton" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
		}

		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath(("(//*[normalize-space(text())='" + param1 + "']/following::label[text()='" + param2
							+ "']/following::label[normalize-space(text())='" + keysToSend + "'])[1]"))));
			wait.until(
					ExpectedConditions
							.textToBePresentInElementLocated(
									By.xpath("//*[normalize-space(text())='" + param1
											+ "']/following::label[normalize-space(text()))='" + param2 + "']"),
									param2));
			WebElement waittext = driver.findElement(
					By.xpath("(//*[normalize-space(text())='" + param1 + "']/following::label[normalize-space(text())='"
							+ param2 + "']/following::label[normalize-space(text())='" + keysToSend + "'])[1]"));
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(500);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked clickRadiobutton" + scripNumber);
			String xpath = "(//*[normalize-space(text())='param1']/following::label[text()='param2']/following::label[normalize-space(text())='keysToSend'])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickRadiobutton" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("(//*[normalize-space(text())='" + param1
					+ "']/following::label[normalize-space(text())='" + keysToSend + "'])[1]"))));
			WebElement waittext = driver.findElement(By.xpath(("(//*[normalize-space(text())='" + param1
					+ "']/following::label[normalize-space(text())='" + keysToSend + "'])[1]")));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(500);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked clickRadiobutton" + scripNumber);
			String xpath = "(//*[normalize-space(text())='param1']/following::label[normalize-space(text())='keysToSend'])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickRadiobutton" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			WebElement waittext = driver.findElement(By.xpath(("//*[contains(text(),'" + param1
					+ "')]/following::*[normalize-space(text())='" + keysToSend + "']/preceding-sibling::input[1]")));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(500);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked clickRadiobutton" + scripNumber);
			String xpath = "//*[contains(text(),'param1')]/following::*[normalize-space(text())='keysToSend']/preceding-sibling::input[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickRadiobutton" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void clickCheckbox(WebDriver driver, String param1, String keysToSend, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		// DH 31
		try {
			if (param1.equalsIgnoreCase("Bank Statement Lines")) {
				Thread.sleep(2000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[text()='" + param1
						+ "']/following::span[text()='" + keysToSend + "'])[1]/preceding::label[1]")));

				WebElement waittext = driver.findElement(By.xpath("(//*[text()='" + param1
						+ "']/following::span[text()='" + keysToSend + "'])[1]/preceding::label[1]"));
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(500);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Address Purpose clickCheckbox" + scripNumber);
				String params = param1;
				String xpath = "(//*[text()='param1']/following::span[text()='keysToSend'])[1]/preceding::label[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Address Purpose clickCheckbox" + scripNumber);
			System.out.println(e);
		}

		// DH 31
		try {
			if (param1.equalsIgnoreCase("System Transactions")) {
				Thread.sleep(2000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[text()='" + param1
						+ "']/following::span[text()='" + keysToSend + "'])[2]/preceding::label[1]")));

				WebElement waittext = driver.findElement(By.xpath("(//*[text()='" + param1
						+ "']/following::span[text()='" + keysToSend + "'])[2]/preceding::label[1]"));
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(500);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Address Purpose clickCheckbox" + scripNumber);
				String params = param1;
				String xpath = "(//*[text()='param1']/following::span[text()='keysToSend'])[2]/preceding::label[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Address Purpose clickCheckbox" + scripNumber);
			System.out.println(e);
		}

		// DH 25
		try {
			if (param1.equalsIgnoreCase("Supplier Contact")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						("(//*[normalize-space(text())='" + param1 + "']/following::input[@type='checkbox'])[1]"))));
				WebElement waittext = driver.findElement(By.xpath(
						"(//*[normalize-space(text())='" + param1 + "']/following::input[@type='checkbox'])[1]"));
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(500);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked clickCheckbox" + scripNumber);
				String params = param1;
				String xpath = "(//*[normalize-space(text())='param1']/following::input[@type='checkbox'])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickCheckbox" + scripNumber);
			System.out.println(e);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
		}

		// po.511
		try {
			if (param1.equalsIgnoreCase("Internal Responder")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						("(//*[normalize-space(text())='" + param1 + "']/following::input[@type='checkbox'])[2]"))));
				WebElement waittext = driver.findElement(By.xpath(
						"(//*[normalize-space(text())='" + param1 + "']/following::input[@type='checkbox'])[2]"));
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(500);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked clickCheckbox" + scripNumber);
				String params = param1;
				String xpath = "(//*[normalize-space(text())='param1']/following::input[@type='checkbox'])[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickCheckbox" + scripNumber);
			System.out.println(e);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
		}

		// DH 23
		try {
			if (param1.equalsIgnoreCase("Correct Unmatched Invoices")) {
				Thread.sleep(2000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()='" + param1
						+ "']/following::span[text()='" + keysToSend + "']/following::label[1]")));

				WebElement waittext = driver.findElement(By.xpath("//*[text()='" + param1
						+ "']/following::span[text()='" + keysToSend + "']/following::label[1]"));
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(500);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Address Purpose clickCheckbox" + scripNumber);
				String params = param1;
				String xpath = "//*[text()='param1']/following::span[text()='keysToSend']/following::label[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Address Purpose clickCheckbox" + scripNumber);
			System.out.println(e);
		}

		try {
			if (param1.equalsIgnoreCase("Item Description")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[normalize-space(text())='" + param1 + "']/following::*[normalize-space(text())='"
								+ keysToSend + "']/preceding::label[contains(@id,'Label')][1]")));
				wait.until(
						ExpectedConditions.textToBePresentInElementLocated(
								By.xpath("//*[normalize-space(text())='" + param1
										+ "']/following::*[normalize-space(text())='" + keysToSend + "']"),
								keysToSend));
				WebElement waittext = driver.findElement(
						By.xpath("//*[normalize-space(text())='" + param1 + "']/following::*[normalize-space(text())='"
								+ keysToSend + "']/preceding::label[contains(@id,'Label')][1]"));
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(500);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Item Description clickCheckbox" + scripNumber);
				String params = param1;
				String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='keysToSend']/preceding::label[contains(@id,'Label')][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Item Description clickCheckbox" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Address Purpose")) {
				Thread.sleep(2000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1
						+ "']/following::*[normalize-space(text())='" + keysToSend + "']")));
				wait.until(
						ExpectedConditions.textToBePresentInElementLocated(
								By.xpath("//*[normalize-space(text())='" + param1
										+ "']/following::*[normalize-space(text())='" + keysToSend + "']"),
								keysToSend));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
						+ "']/following::*[normalize-space(text())='" + keysToSend + "']"));
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(500);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Address Purpose clickCheckbox" + scripNumber);
				String params = param1;
				String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='keysToSend']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Address Purpose clickCheckbox" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Scenario")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1
						+ "']/following::span[normalize-space(text())='" + keysToSend + "']/preceding::input[1]")));
				wait.until(
						ExpectedConditions.textToBePresentInElementLocated(
								By.xpath("//*[normalize-space(text())='" + param1
										+ "']/following::span[normalize-space(text())='" + keysToSend + "']"),
								keysToSend));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
						+ "']/following::span[normalize-space(text())='" + keysToSend + "']/preceding::input[1]"));
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				// tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(500);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Scenario clickCheckbox" + scripNumber);
				String params = param1;
				String xpath = "//*[normalize-space(text())='param1']/following::span[normalize-space(text())='keysToSend']/preceding::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Scenario clickCheckbox" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Address Purpose")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1
						+ "']/following::label[normalize-space(text())='" + keysToSend + "']/preceding::input[1]")));
				wait.until(
						ExpectedConditions.textToBePresentInElementLocated(
								By.xpath("//*[normalize-space(text())='" + param1
										+ "']/following::label[normalize-space(text())='" + keysToSend + "']"),
								keysToSend));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
						+ "']/following::label[normalize-space(text())='" + keysToSend + "']/preceding::input[1]"));
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				// tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(500);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Address Purpose clickCheckbox" + scripNumber);
				String params = param1;
				String xpath = "//*[normalize-space(text())='param1']/following::label[normalize-space(text())='keysToSend']/preceding::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Address Purpose clickCheckbox" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Name")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//a[normalize-space(text())='" + param1 + "']/following::input[1]")));
				WebElement waittext = driver
						.findElement(By.xpath("//a[normalize-space(text())='" + param1 + "']/following::input[1]"));
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				// tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(500);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Name clickCheckbox" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Name clickCheckbox" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Match Invoice Lines")
					|| param1.equalsIgnoreCase("Correct Unmatched Invoices")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[normalize-space(text())='" + param1 + "']/following::*[normalize-space(text())='"
								+ keysToSend + "']/following::label[contains(@id,'Label')][1]")));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[text()='" + param1 + "']"),
						param1));
				WebElement waittext = driver.findElement(By.xpath(
						"//*[normalize-space(text())='Match Invoice Lines']/following::*[normalize-space(text())='Match']/following::label[contains(@id,'Label')][1]"));
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(500);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Match Invoice Lines clickCheckbox" + scripNumber);
				String params = param1;
				String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='keysToSend']/following::label[contains(@id,'Label')][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Match Invoice Lines clickCheckbox" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//label[text()='" + param1
					+ "']/following::span[text()='" + keysToSend + "']/preceding::label[1]"))));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//label[text()='" + param1 + "']"),
					param1));
			WebElement waittext = driver.findElement(By.xpath("//label[normalize-space(text())='" + param1
					+ "']/following::span[normalize-space(text())='" + keysToSend + "']/preceding::label[1]"));
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(500);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked clickCheckbox" + scripNumber);
			String params = param1;
			String xpath = "//label[text()='param1']/following::span[text()='keysToSend']/preceding::label[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickCheckbox" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[normalize-space(text())='" + param1
					+ "']/following::span[normalize-space(text())='" + keysToSend + "']/preceding::label[1]"))));
			wait.until(
					ExpectedConditions
							.textToBePresentInElementLocated(
									By.xpath("//*[normalize-space(text())='" + param1
											+ "']/following::span[normalize-space(text())='" + keysToSend + "']"),
									keysToSend));
			WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
					+ "']/following::span[normalize-space(text())='" + keysToSend + "']/preceding::label[1]"));
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(500);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked clickCheckbox" + scripNumber);
			String params = param1;
			String xpath = "//*[normalize-space(text())='param1']/following::span[normalize-space(text())='keysToSend']/preceding::label[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickCheckbox" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//label[normalize-space(text())='"
					+ param1 + "']/following::label[normalize-space(text())='" + keysToSend + "']"))));
			wait.until(
					ExpectedConditions.textToBePresentInElementLocated(
							By.xpath("//label[normalize-space(text())='" + param1
									+ "']/following::label[normalize-space(text())='" + keysToSend + "']"),
							keysToSend));
			WebElement waittext = driver.findElement(By.xpath("//label[normalize-space(text())='" + param1
					+ "']/following::label[normalize-space(text())='" + keysToSend + "']"));
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(500);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked clickCheckbox" + scripNumber);
			String params = param1;
			String xpath = "//label[normalize-space(text())='param1']/following::label[normalize-space(text())='keysToSend']";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickCheckbox" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[normalize-space(text())='" + param1
					+ "']/following::*[normalize-space(text())='" + keysToSend + "']"))));
			wait.until(
					ExpectedConditions
							.textToBePresentInElementLocated(
									By.xpath("//*[normalize-space(text())='" + param1
											+ "']/following::*[normalize-space(text())='" + keysToSend + "']"),
									keysToSend));
			WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
					+ "']/following::*[normalize-space(text())='" + keysToSend + "']"));
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(3000);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked clickCheckbox" + scripNumber);
			String params = param1;
			String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='keysToSend']";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickCheckbox" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath(("//label[normalize-space(text())='" + keysToSend + "']"))));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath("//label[normalize-space(text())='" + keysToSend + "']"), keysToSend));
			WebElement waittext = driver.findElement(By.xpath("//label[normalize-space(text())='" + keysToSend + "']"));
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(500);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked clickCheckbox" + scripNumber);
			String params = param1;
			String xpath = "//label[normalize-space(text())='keysToSend']";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickCheckbox" + scripNumber);
			System.out.println(e);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
//			throw e;
		}

		// New code for PTP.Ex.111
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath(("(//*[contains(text(),'" + param1 + "')]/following::input[@type='checkbox'])[1]"))));

			WebElement waittext = driver.findElement(
					By.xpath("(//*[contains(text(),'" + param1 + "')]/following::input[@type='checkbox'])[1]"));
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(500);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked clickCheckbox" + scripNumber);
			String params = param1;
			String xpath = "(//*[contains(text(),'param1')]/following::input[@type='checkbox'])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickCheckbox" + scripNumber);
			System.out.println(e);
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
					By.xpath(("//*[normalize-space(text())='" + param1 + "']/following::a[normalize-space(text())='"
							+ keysToSend + "']/following::img[contains(@title,'" + param2 + "')][1]"))));
			Thread.sleep(2000);
			wait.until(
					ExpectedConditions
							.textToBePresentInElementLocated(
									By.xpath("//*[normalize-space(text())='" + param1
											+ "']/following::a[normalize-space(text())='" + keysToSend + "']"),
									keysToSend));
			WebElement waittext = driver.findElement(
					By.xpath("//*[normalize-space(text())='" + param1 + "']/following::a[normalize-space(text())='"
							+ keysToSend + "']/following::img[contains(@title,'" + param2 + "')][1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(2000);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[normalize-space(text())='param1']/following::a[normalize-space(text())='keysToSend']/following::img[contains(@title,'param2')][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully Clicked clickLinkAction" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickLinkAction" + scripNumber);
			System.out.println(e);
		}
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1
						+ "']/following::a[normalize-space(text())='" + keysToSend + "']")));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
						+ "']/following::a[normalize-space(text())='" + keysToSend + "']"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[normalize-space(text())='param1']/following::a[normalize-space(text())='keysToSend']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				log.info("Sucessfully Clicked clickLinkAction" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickLinkAction" + scripNumber);
			System.out.println(e);
		}
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"(//*[normalize-space(text())='" + keysToSend + "']/following::td[normalize-space(text())='"
								+ param1 + "']/following::table[1]//div)[1]")));
				WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())='" + keysToSend
						+ "']/following::td[normalize-space(text())='" + param1 + "']/following::table[1]//div)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(1000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//*[normalize-space(text())='keysToSend']/following::td[normalize-space(text())='param1']/following::table[1]//div)[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				log.info("Sucessfully Clicked clickLinkAction" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickLinkAction" + scripNumber);
			System.out.println(e);
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
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(2000);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//*[normalize-space(text())='param1']/following::*[normalize-space(text())='keysToSend']/following::img[contains(@title,'param2')])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully Clicked clickLinkAction" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickLinkAction" + scripNumber);
			System.out.println(e);
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
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(2000);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//*[normalize-space(text())='param1']/following::*[normalize-space(text())='keysToSend']/following::img[contains(@title,'param2')])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully Clicked clickLinkAction" + scripNumber);
			return;
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickLinkAction" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public String textarea(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {
		// DH 20
		try {
			if (param1.equalsIgnoreCase("Create Note")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//body[@dir='ltr']")));
				Thread.sleep(1000);
				WebElement waittill = driver.findElement(By.xpath("//body[@dir='ltr']"));
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
			System.out.println(e);
		}

		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())='" + param1
					+ "']/following::label[normalize-space(text())='" + param2 + "']/following::textarea)[1]")));
			Thread.sleep(1000);
			wait.until(
					ExpectedConditions
							.textToBePresentInElementLocated(
									By.xpath("//*[normalize-space(text())='" + param1
											+ "']/following::label[normalize-space(text())='" + param2 + "']"),
									param2));
			WebElement waittill = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
					+ "']/following::label[normalize-space(text())='" + param2 + "']/following::textarea[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			Thread.sleep(500);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked textarea" + scripNumber);
			String xpath = "//*[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/following::textarea[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return keysToSend;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during textarea" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[contains(text(),'" + param1
					+ "')]/following::label[normalize-space(text())='" + param2 + "']/following::textarea)[1]")));
			Thread.sleep(1000);
			wait.until(
					ExpectedConditions
							.textToBePresentInElementLocated(
									By.xpath("//*[contains(text(),'" + param1
											+ "')]/following::label[normalize-space(text())='" + param2 + "']"),
									param2));
			WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),'" + param1
					+ "')]/following::label[normalize-space(text())='" + param2 + "']/following::textarea)[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			Thread.sleep(500);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked textarea" + scripNumber);
			String xpath = "(//*[contains(text(),'param1')]/following::label[normalize-space(text())='param2']/following::textarea)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return keysToSend;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during textarea" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//body[@dir='ltr']")));
			Thread.sleep(1000);
			WebElement waittill = driver.findElement(By.xpath("//body[@dir='ltr']"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			screenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(500);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked textarea" + scripNumber);
			String xpath = "//body[@dir='ltr']";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return keysToSend;
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during textarea" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public String sendValue(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {
		// DH 32
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("//*[contains(@id,'popup-container')]//*[text()='" + param1
							+ "']/following::*[text()='" + param2 + "']/following::input[1]")));
			WebElement waittill = driver.findElement(By.xpath("//*[contains(@id,'popup-container')]//*[text()='"
					+ param1 + "']/following::*[text()='" + param2 + "']/following::input[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			screenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked Close Date sendValue" + scripNumber);
			String xpath = "//*[contains(@id,'popup-container')]//*[text()='param1']/following::*[text()='param2']/following::input[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return keysToSend;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Close Date sendValue" + scripNumber);
			System.out.println(e);
		}

		// DH 39 SCM.PM.509

		try {
			if (param1.equalsIgnoreCase("Security")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"//*[text()='" + param1 + "']/following::span[text()='" + param2 + "']/following::input[1]")));
				WebElement waittill = driver.findElement(By.xpath(
						"//*[text()='" + param1 + "']/following::span[text()='" + param2 + "']/following::input[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Close Date sendValue" + scripNumber);
				String xpath = "//*[text()='param1']/following::span[text()='param2']/following::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Close Date sendValue" + scripNumber);
			System.out.println(e);
		}

//DH 39 OTC.AR.236
		try {
			if (param1.equalsIgnoreCase("Create Contact Point") && param2.equalsIgnoreCase("Phone")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[text()='" + param1
						+ "']/following::label[text()='" + param2 + "']/following::input[1]")));
				WebElement waittill = driver.findElement(By.xpath("//div[text()='" + param1
						+ "']/following::label[text()='" + param2 + "']/following::input[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Close Date sendValue" + scripNumber);
				String xpath = "//div[text()='param1']/following::label[text()='param2']/following::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Close Date sendValue" + scripNumber);
			System.out.println(e);
		}

		// DH 12
		try {
			if (param2.equalsIgnoreCase("Close Date: Fixed")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("(//*[text()='" + param1 + "']/following::label[text()='"
								+ param2 + "']/preceding-sibling::input[not(@type='hidden')])[1]")));
				WebElement waittill = driver
						.findElement(By.xpath("(//*[text()='" + param1 + "']/following::label[text()='" + param2
								+ "']/preceding-sibling::input[not(@type='hidden')])[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Close Date sendValue" + scripNumber);
				String xpath = "(//*[text()='param1']/following::label[text()='param2']/preceding-sibling::input[not(@type='hidden')])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Close Date sendValue" + scripNumber);
			System.out.println(e);
		}

		// DH 15
		try {
			if (param1.equalsIgnoreCase("Manage Divisions")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("(//*[text()='" + param1 + "']/following::label[text()='"
								+ param2 + "']/following::input[contains(@id,'qry') and not (@role)])[1]")));
				Thread.sleep(1000);
				WebElement waittill = driver
						.findElement(By.xpath("(//*[text()='" + param1 + "']/following::label[text()='" + param2
								+ "']/following::input[contains(@id,'qry') and not (@role)])[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				Thread.sleep(500);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked sendValue" + scripNumber);
				String xpath = "(//label[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/following::input)[1]";
				// service.saveXpathParams(param1, param2, scripNumber, xpath);
				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during sendValue" + scripNumber);
			System.out.println(e);
		}
		// DH 18
		try {
			if (param2.equalsIgnoreCase("Start Date and Time") || param2.equalsIgnoreCase("End Date and Time")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("(//*[text()='" + param1 + "']/following::label[text()='"
								+ param2 + "']/following::input[@placeholder='dd/mm/yy h:mm a'])[1]")));
				Thread.sleep(1000);
				WebElement waittill = driver
						.findElement(By.xpath("(//*[text()='" + param1 + "']/following::label[text()='" + param2
								+ "']/following::input[@placeholder='dd/mm/yy h:mm a'])[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				Thread.sleep(500);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked sendValue" + scripNumber);
				String xpath = "(//label[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/following::input)[1]";
				// service.saveXpathParams(param1, param2, scripNumber, xpath);
				return keysToSend;
			}

			// return keysToSend;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during sendValue" + scripNumber);
			System.out.println(e);
			// throw e;
		}
		try {
			if (param1.equalsIgnoreCase("Password")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='" + param1 + "']")));
				WebElement waittill = driver.findElement(By.xpath("//input[@type='" + param1 + "']"));
				JavascriptExecutor jse = (JavascriptExecutor) driver;
				jse.executeScript("document.getElementById('password').value = '" + keysToSend + "';");
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Password sendValue" + scripNumber);
				String xpath = "//input[@type='param1']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
//                                       String scriptID=fetchMetadataVO.getScriptId();String lineNumber = fetchMetadataVO.getLineNumber();service.saveXpathParams(scriptID, lineNumber, xpath);
//                String lineNumber = fetchMetadataVO.getLineNumber();
//                service.saveXpathParams(scriptID,metadataID);
				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Password sendValue" + scripNumber);
			System.out.println(e);
		}
		// Here Adding xpath for invoice dates AP.452

		try {

			if (param1.equalsIgnoreCase("Invoice Date")) {

				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()='" + param1
						+ "']//following::*[contains(text(),'" + param2 + "')]/preceding::input[2]")));

				WebElement waittill = driver.findElement(By.xpath("//*[text()='" + param1
						+ "']//following::*[contains(text(),'" + param2 + "')]/preceding::input[2]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);

				Thread.sleep(1000);

				String scripNumber = fetchMetadataVO.getScriptNumber();

				log.info("Sucessfully Clicked Invoice Dates sendValue" + scripNumber);

				String xpath = "//*[text()='" + param1 + "']//following::*[contains(text(),'" + param2
						+ "')]/preceding::input[2]";

				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return keysToSend;

			}

		} catch (Exception e) {

			String scripNumber = fetchMetadataVO.getScriptNumber();

			log.error("Failed during Invoice Dates  sendValue" + scripNumber);

			System.out.println(e);

		}
		try {

			if ((param1.equalsIgnoreCase("Application Accounting Date") || param1.equalsIgnoreCase("Accounting Date"))
					&& param2.equalsIgnoreCase("Start Date")) {

				WebElement waittill = driver
						.findElement(By.xpath(" //label[@title='" + param1 + "']/following::input[1]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				Thread.sleep(500);

				String scripNumber = fetchMetadataVO.getScriptNumber();

				log.info("Sucessfully Clicked Payables to Ledger Reconciliation Report sendValue" + scripNumber);

				String xpath = " //label[@title='param1']/following::input[1]";

				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return keysToSend;

			}

		} catch (Exception e) {

			String scripNumber = fetchMetadataVO.getScriptNumber();

			log.error("Failed during Payables to Ledger Reconciliation Report sendValue" + scripNumber);

			System.out.println(e);

		}

		try {
			if (param2.equalsIgnoreCase("Delegate to: ") || param1.equalsIgnoreCase("Search: Invoice")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[text()='" + param1
						+ "']//following::label[text()='" + param2 + "']//following::input[1]")));
				WebElement waittill = driver.findElement(By.xpath("//h1[text()='" + param1
						+ "']//following::label[text()='" + param2 + "']//following::input[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Delegate to sendValue" + scripNumber);
				String xpath = "//h1[text()='param1']//following::label[text()='param2']//following::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Delegate to  sendValue" + scripNumber);
			System.out.println(e);
		}

		try {

			if ((param1.equalsIgnoreCase("Application Accounting Date") || param1.equalsIgnoreCase("Accounting Date"))
					&& param2.equalsIgnoreCase("End Date")) {

				WebElement waittill = driver
						.findElement(By.xpath(" //label[@title='" + param1 + "']/following::input[2]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				Thread.sleep(500);

				String scripNumber = fetchMetadataVO.getScriptNumber();

				log.info("Sucessfully Clicked Payables to Ledger Reconciliation Report sendValue" + scripNumber);

				String xpath = " //label[@title='param1']/following::input[2]";

				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return keysToSend;

			}

		} catch (Exception e) {

			String scripNumber = fetchMetadataVO.getScriptNumber();

			log.error("Failed during Payables to Ledger Reconciliation Report sendValue" + scripNumber);

			System.out.println(e);

		}
		try {
			if (param1.equalsIgnoreCase("Reports and Analytics")
					|| param1.equalsIgnoreCase("Notifications") && param2.equalsIgnoreCase("Search")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1
						+ "']/following::input[@placeholder='" + param2 + "'][1]")));
				WebElement waittill = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
						+ "']/following::input[@placeholder='" + param2 + "'][1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Reports and Analytics or Search sendValue" + scripNumber);
				String xpath = "//*[normalize-space(text())=' param1 ']/following::input[@placeholder=' param2 '][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Reports and Analytics or Search  sendValue" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Report")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[contains(text(),'" + param2 + "')]/following::input[1]")));
				WebElement waittill = driver
						.findElement(By.xpath("//*[contains(text(),'" + param2 + "')]/following::input[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				Thread.sleep(8000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Report sendValue" + scripNumber);
				String xpath = "//*[contains(text(),'param2')]/following::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Report sendValue" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Create Bank Account")
					&& (param2.equalsIgnoreCase("Account Number") || param2.equalsIgnoreCase("IBAN"))
					|| param2.equalsIgnoreCase("Delegate to")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[contains(text(),'" + param1
						+ "')]/following::label[contains(text(),'" + param2 + "')]/following::input)[1]")));
				Thread.sleep(1000);
				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),'" + param1
						+ "')]/following::label[contains(text(),'" + param2 + "')]/following::input)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				Thread.sleep(500);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Create Expense Item sendValue" + scripNumber);
				String xpath = "(//*[contains(text(),'" + param1 + "')]/following::label[contains(text(),'" + param2
						+ "')]/following::input)[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				log.info("Sucessfully Clicked Create Expense Item sendValue" + scripNumber);

				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Create Expense Item sendValue" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Create Bank Account")
					&& (param2.equalsIgnoreCase("Bank") || param2.equalsIgnoreCase("Bank Branch"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[text()='Create Bank Account']/following::label[text()='" + param2 + "'][2]")));
				Thread.sleep(1000);
				WebElement waittill = driver.findElement(
						By.xpath("//*[text()='Create Bank Account']/following::label[text()='" + param2 + "'][2]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				Thread.sleep(500);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Create Expense Item sendValue" + scripNumber);
				String xpath = "//*[text()='Create Bank Account']/following::label[text()='" + param2 + "'][2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				log.info("Sucessfully Clicked Create Expense Item sendValue" + scripNumber);

				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Create Expense Item sendValue" + scripNumber);
			System.out.println(e);
		}

		try {
			if (param2.equalsIgnoreCase("Phone") || param2.equalsIgnoreCase("Mobile")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())='"
						+ param1 + "']/following::*[normalize-space(text())='" + param2 + "']/following::input)[3]")));
				WebElement waittill = driver.findElement(By.xpath("(//*[normalize-space(text())='" + param1
						+ "']/following::*[normalize-space(text())='" + param2 + "']/following::input)[3]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				Thread.sleep(8000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Phone or Mobile sendValue" + scripNumber);
				String xpath = "(//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']/following::input)[3]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Clicked Phone or Mobile sendValue" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Create Line") && param2.equalsIgnoreCase("Name")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"(//div[normalize-space(text())='" + param1 + "']/following::label[normalize-space(text())='"
								+ param2 + "']/following::input)[2]")));
				WebElement waittill = driver.findElement(By.xpath("(//div[normalize-space(text())='" + param1
						+ "']/following::label[normalize-space(text())='" + param2 + "']/following::input)[2]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				Thread.sleep(8000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Create Line or Name sendValue" + scripNumber);
				String xpath = "(//div[normalize-space(text())=' param1 ']/following::label[normalize-space(text())=' param2 ']/following::input)[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Create Line or Name  sendValue" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Create Time Card") && param2.equalsIgnoreCase("Person Name")) {
				Thread.sleep(4000);

				WebElement waittill = driver.findElement(By.xpath("//div[text()=\"" + param1

						+ "\"]/following::span[text()='" + param2 + "']//input[1]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Create Time Card or Person Name sendValue" + scripNumber);
				String xpath = "//div[text()='param1']/following::span[text()='param2']//input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return keysToSend;

			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Create Time Card or Person Name sendValue" + scripNumber);
			System.out.println(e);

		}
//DH 22

		try {
			if (param1.equalsIgnoreCase("Lines") && param2.equalsIgnoreCase("Query By Example")) {
				Thread.sleep(8000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[normalize-space(text())='"
						+ param1 + "']/following::*[@title='" + param2 + "']/following::input)[1]")));
				WebElement waittill = driver.findElement(By.xpath("(//h1[normalize-space(text())='" + param1
						+ "']/following::*[@title='" + param2 + "']/following::input)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				Thread.sleep(3000);
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(10000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Lines or Query By Example sendValue" + scripNumber);
				String xpath = "(//h1[normalize-space(text())='param1']/following::*[@title='param2']/following::input)[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Lines or Query By Example  sendValue" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param2.equalsIgnoreCase("Unapply Accounting Date")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"(//*[normalize-space(text())=\"" + param1 + "\"]/following::label[normalize-space(text())='"
								+ param2 + "']/following::input)[1]")));
				Thread.sleep(5000);
				wait.until(
						ExpectedConditions
								.textToBePresentInElementLocated(
										By.xpath("//*[normalize-space(text())=\"" + param1
												+ "\"]/following::label[normalize-space(text())='" + param2 + "']"),
										param2));
				WebElement waittill = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::label[normalize-space(text())='" + param2 + "']/following::input[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				Thread.sleep(8000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Unapply Accounting Date sendValue" + scripNumber);
				String xpath = "//*[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Unapply Accounting Date sendValue" + scripNumber);
			System.out.println(e);
		}
		try {

			if (param1.equalsIgnoreCase("Accounting Period-Filter")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				try {
					WebElement waittill = driver.findElement(By.xpath("//*[contains(@id,'PeriodName::content')]"));
					Actions actions = new Actions(driver);
					actions.moveToElement(waittill).build().perform();
					typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
					String scripNumber = fetchMetadataVO.getScriptNumber();

					String xpath = "//*[contains(@id,'PeriodName::content')]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);

					Thread.sleep(2000);
				} catch (Exception e) {
					WebElement filter = driver.findElement(By.xpath("//img[@title='Query By Example']"));
					Actions actions = new Actions(driver);
					actions.moveToElement(filter).build().perform();
					filter.click();
					Thread.sleep(5000);
					WebElement waittill = driver.findElement(By.xpath("//*[contains(@id,'PeriodName::content')]"));
					actions.moveToElement(waittill).build().perform();
					typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
					Thread.sleep(2000);
					String scripNumber = fetchMetadataVO.getScriptNumber();
					log.error("Failed during Accounting Period-Filter sendValue" + scripNumber);
				}

				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Accounting Period-Filter sendValue" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Manage Accounting Periods")
					|| param1.equalsIgnoreCase("Edit Accounting Period Statuses")
					|| param2.equalsIgnoreCase("Query By Example")) {

				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[contains(text(),'" + param1
						+ "')]/following::*[@title='" + param2 + "']/following::input)[1]")));
				// wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[contains(text(),'"+param1+"')]/following::*[@title='"+param2+"']"),
				// param2));
				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),'" + param1
						+ "')]/following::*[@title='" + param2 + "']/following::input)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				Thread.sleep(2000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Manage Accounting Periods sendValue" + scripNumber);
				String xpath = "(//*[contains(text(),'param1')]/following::*[@title='param2']/following::input)[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Manage Accounting Periods sendValue" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Reports and Analytics") && param2.equalsIgnoreCase("Search")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1
						+ "']/following::input[@placeholder='" + param2 + "'][1]")));
				WebElement waittill = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
						+ "']/following::input[@placeholder='" + param2 + "'][1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Reports and Analytics or Search sendValue" + scripNumber);
				String xpath = "//*[normalize-space(text())=' param1 ']/following::input[@placeholder=' param2 '][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Reports and Analytics or Search  sendValue" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Payables to Ledger Reconciliation Report")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("(//*[contains(text(),'" + param2 + "')]/following::input)[2]")));
				Thread.sleep(1000);
				WebElement waittill = driver
						.findElement(By.xpath("(//*[contains(text(),'" + param2 + "')]/following::input)[2]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				Thread.sleep(500);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				log.info("Sucessfully Clicked Payables to Ledger Reconciliation Report sendValue" + scripNumber);

				String xpath = "(//*[contains(text(),'param2')]/following::input)[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Payables to Ledger Reconciliation Report sendValue" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Daily Rates")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"//a[normalize-space(text())='" + param1 + "']/following::label[normalize-space(text())='"
								+ param2 + "']/preceding::input[not (@type='hidden')][1]")));
				WebElement waittill = driver.findElement(By.xpath(
						"//a[normalize-space(text())='" + param1 + "']/following::label[normalize-space(text())='"
								+ param2 + "']/preceding::input[not (@type='hidden')][1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Daily Rates  sendValue" + scripNumber);
				String xpath = "//a[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/preceding::input[not (@type='hidden')][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  Daily Rates sendValue" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Edit Line") && param2.equalsIgnoreCase("Category Name")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[contains(text(),'" + param1
						+ "')]/following::label[normalize-space(text())='" + param2 + "']/following::input)[1]")));
				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),'" + param1
						+ "')]/following::label[normalize-space(text())='" + param2 + "']/following::input)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked sendValue" + scripNumber);
				String xpath = "(//*[contains(text(),'param1')]/following::label[normalize-space(text())='param2']/following::input)[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return keysToSend;
			}
		} catch (Exception e) {
			// TODO: handle exception
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during sendValue" + scripNumber);
		}
		try {
			if (param1.equalsIgnoreCase("Create Expense Item") && param2.equalsIgnoreCase("Amount")) {
				Thread.sleep(10000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("(//h1[contains(text(),'" + param1 + "')]/following::label[normalize-space(text())='"
								+ param2 + "']/following::input[@type='text'])[2]")));
				Thread.sleep(1000);
				WebElement waittill = driver.findElement(
						By.xpath("(//h1[contains(text(),'" + param1 + "')]/following::label[normalize-space(text())='"
								+ param2 + "']/following::input[@type='text'])[2]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				Thread.sleep(500);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Create Expense Item sendValue" + scripNumber);
				String xpath = "(//h1[contains(text(),'param1')]/following::label[normalize-space(text())='param2']/following::input[@type='text'])[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				log.info("Sucessfully Clicked Create Expense Item sendValue" + scripNumber);

				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Create Expense Item sendValue" + scripNumber);
			System.out.println(e);
		}
		try {
			Thread.sleep(10000);
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h2[contains(text(),'" + param1
					+ "')]/following::label[normalize-space(text())='" + param2 + "']/following::input)[1]")));
			Thread.sleep(1000);
			wait.until(
					ExpectedConditions
							.textToBePresentInElementLocated(
									By.xpath("//h2[contains(text(),'" + param1
											+ "')]/following::label[normalize-space(text())='" + param2 + "']"),
									param2));
			WebElement waittill = driver.findElement(By.xpath("//h2[contains(text(),'" + param1
					+ "')]/following::label[normalize-space(text())='" + param2 + "']/following::input[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			Thread.sleep(500);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked sendValue" + scripNumber);
			String xpath = "(//h2[contains(text(),'param1')]/following::label[normalize-space(text())='param2']/following::input)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return keysToSend;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during sendValue" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[contains(text(),'" + param1
					+ "')]/following::label[normalize-space(text())='" + param2 + "']/following::input)[1]")));
			Thread.sleep(1000);
			wait.until(
					ExpectedConditions
							.textToBePresentInElementLocated(
									By.xpath("//*[contains(text(),'" + param1
											+ "')]/following::label[normalize-space(text())='" + param2 + "']"),
									param2));

			WebElement waittill = driver.findElement(By.xpath("//*[contains(text(),'" + param1
					+ "')]/following::label[normalize-space(text())='" + param2 + "']/following::input[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			Thread.sleep(500);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//*[contains(text(),'param1')]/following::label[normalize-space(text())='param2']/following::input)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully Clicked sendValue" + scripNumber);
			return keysToSend;
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during sendValue" + scripNumber);
		}
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("//*[contains(@placeholder,'" + param1 + "')]")));
				WebElement waittill = driver.findElement(By.xpath("//*[contains(@placeholder,'" + param1 + "')]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked sendValue" + scripNumber);
				String xpath = "//*[contains(@placeholder,'param1')]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return keysToSend;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during sendValue" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[contains(text(),'" + param1
					+ "')]/following::label[normalize-space(text())='" + param2 + "']/following::input)[1]")));
			Thread.sleep(1000);
			wait.until(
					ExpectedConditions
							.textToBePresentInElementLocated(
									By.xpath("//h1[contains(text(),'" + param1
											+ "')]/following::label[normalize-space(text())='" + param2 + "']"),
									param2));
			WebElement waittill = driver.findElement(By.xpath("//h1[contains(text(),'" + param1
					+ "')]/following::label[normalize-space(text())='" + param2 + "']/following::input[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			String scripNumber = fetchMetadataVO.getScriptNumber();

			String xpath = "(//h1[contains(text(),'param1')]/following::label[normalize-space(text())='param2']/following::input)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			Thread.sleep(5000);
			return keysToSend;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during sendValue" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//label[normalize-space(text())='"
					+ param1 + "']/following::label[normalize-space(text())='" + param2 + "']/following::input)[1]")));
			Thread.sleep(1000);
			wait.until(
					ExpectedConditions
							.textToBePresentInElementLocated(
									By.xpath("//label[normalize-space(text())='" + param1
											+ "']/following::label[normalize-space(text())='" + param2 + "']"),
									param2));
			WebElement waittill = driver.findElement(By.xpath("//label[normalize-space(text())='" + param1
					+ "']/following::label[normalize-space(text())='" + param2 + "']/following::input[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			Thread.sleep(500);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked sendValue" + scripNumber);
			String xpath = "(//label[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/following::input)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return keysToSend;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during sendValue" + scripNumber);
			System.out.println(e);
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
			log.info("Sucessfully Clicked sendValue" + scripNumber);
			String xpath = "(//*[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/following::input)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return keysToSend;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during sendValue" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())='" + param1
					+ "']/following::*[normalize-space(text())='" + param2 + "']/following::input)[1]")));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[normalize-space(text())='"
					+ param1 + "']/following::*[normalize-space(text())='" + param2 + "']"), param2));
			WebElement waittill = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
					+ "']/following::*[normalize-space(text())='" + param2 + "']/following::input[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			Thread.sleep(2000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked sendValue" + scripNumber);
			String xpath = "(//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']/following::input)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return keysToSend;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during sendValue" + scripNumber);
			System.out.println(e);
		}

		try {
			Thread.sleep(5000);
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())='" + param1
					+ "']/following::img[@title='" + param2 + "']/following::input)[1]")));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath("//*[normalize-space(text())='" + param1 + "']/following::img[@title='" + param2 + "']"),
					param2));
			WebElement waittill = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
					+ "']/following::img[@title='" + param2 + "']/following::input[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			Thread.sleep(2000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked sendValue" + scripNumber);
			String xpath = "(//*[normalize-space(text())='param1']/following::img[@title='param2']/following::input)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return keysToSend;
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during sendValue" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
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
						"//div[contains(@id,'dropdownPopup::popup-container')]//a[contains(text(),'Search')][1]")));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath(
						"//div[contains(@id,'dropdownPopup::popup-container')]//a[contains(text(),'Search')][1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(5000);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"//div[contains(@id,'PopupId::content')]//*[text()='Search']/following::*[normalize-space(text())='"
								+ param2 + "']/following::input[1]")));
				WebElement searchResult = driver.findElement(By.xpath(
						"//div[contains(@id,'PopupId::content')]//*[text()='Search']/following::*[normalize-space(text())='"
								+ param2 + "']/following::input[1]"));
				typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
				if (keysToSend != null) {

					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);

					Thread.sleep(5000);

					WebElement text = driver.findElement(
							By.xpath("(//div[@class='AFDetectExpansion']/following::span[normalize-space(text())='"
									+ param2 + "']/following::table//span[text()])[1]"));

					text.click();

					Thread.sleep(1000);

					WebElement button = driver
							.findElement(By.xpath("//*[text()='Search']/following::*[normalize-space(text())='" + param2
									+ "']/following::*[text()='OK'][1]"));

					button.click();
					String scripNumber = fetchMetadataVO.getScriptNumber();
					log.info("Sucessfully Clicked Postal Code Legal Entity dropdownTexts" + scripNumber);

				}

				return;

			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Postal Code Legal Entity  dropdownTexts" + scripNumber);

			System.out.println(e);

		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"(//div[contains(@id,'popup-container')]//*[normalize-space(text())='" + keysToSend + "'])[1]")));
			Thread.sleep(4000);
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(
					"(//div[contains(@id,'popup-container')]//*[normalize-space(text())='" + keysToSend + "'])[1]"),
					keysToSend));
			WebElement waittext = driver.findElement(By.xpath(
					"(//div[contains(@id,'popup-container')]//*[normalize-space(text())='" + keysToSend + "'])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(2000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked dropdownTexts" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during dropdownTexts" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("(//div[contains(@id,'dropdownPopup::dropDownContent')]//*[normalize-space(text())='"
							+ keysToSend + "'])[1]")));
			Thread.sleep(4000);
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath("(//div[contains(@id,'dropdownPopup::dropDownContent')]//*[normalize-space(text())='"
							+ keysToSend + "'])[1]"),
					keysToSend));
			WebElement waittext = driver.findElement(
					By.xpath("(//div[contains(@id,'dropdownPopup::dropDownContent')]//*[normalize-space(text())='"
							+ keysToSend + "'])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(2000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked dropdownTexts" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during dropdownTexts" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"//div[contains(@id,'dropdownPopup::dropDownContent')]/following::a[contains(text(),'Search')][1]")));
//		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//a[contains(text(),'Search')]"), "Search"));
			WebElement search = driver.findElement(By.xpath(
					"//div[contains(@id,'dropdownPopup::dropDownContent')]/following::a[contains(text(),'Search')][1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(search).build().perform();
			search.click();
			Thread.sleep(10000);
			// wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@id,'PopupId::content')]//*[text()='Search']/following::*[text()='"+param2+"']/following::input[1]")));
			WebElement searchResult = driver.findElement(By.xpath(
					"//div[contains(@id,'PopupId::content')]//*[text()='Search']/following::*[normalize-space(text())='"
							+ param2 + "']/following::input[1]"));
			typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
			if (keysToSend != null) {
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
				try {
					WebElement text = driver.findElement(By.xpath(
							"//div[@class='AFDetectExpansion']/following::span[text()='Name']/following::span[normalize-space(text())='"
									+ keysToSend + "']"));
					String scripNumber = fetchMetadataVO.getScriptNumber();
					log.info("Sucessfully Clicked dropdownTexts" + scripNumber);
					text.click();
				} catch (Exception e) {
					String scripNumber = fetchMetadataVO.getScriptNumber();
					log.error("Failed during dropdownTexts" + scripNumber);
					WebElement text = driver
							.findElement(By.xpath("(//span[contains(text(),'" + keysToSend + "')])[1]"));
					text.click();
				}
			}
			try {
				WebElement button = driver
						.findElement(By.xpath("//*[text()='Search']/following::*[normalize-space(text())='" + param2
								+ "']/following::*[not (@aria-disabled) and text()='K'][1]"));
				button.click();
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked dropdownTexts" + scripNumber);
			} catch (Exception e) {
				WebElement button = driver
						.findElement(By.xpath("//*[text()='Search']/following::*[normalize-space(text())='" + param2
								+ "']/following::*[not (@aria-disabled) and text()='OK'][1]"));
				button.click();
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.error("Failed during dropdownTexts" + scripNumber);
			}

			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during dropdownTexts" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"//div[contains(@id,'PopupId::content')]/following::*[normalize-space(text())='Search']/following::*[text()='Name']/following::input[@type='text'][1]")));
			WebElement searchResult = driver.findElement(By.xpath(
					"//div[contains(@id,'PopupId::content')]/following::*[normalize-space(text())='Search']/following::*[text()='Name']/following::input[@type='text'][1]"));
			typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
			enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(5000);
			WebElement text = driver.findElement(By.xpath("(//span[contains(text(),'" + keysToSend + "')])[1]"));
			text.click();
			Thread.sleep(1000);
			WebElement button = driver.findElement(
					By.xpath("//*[text()='Search']/following::*[text()='Name']/following::*[text()='OK'][1]"));
			button.click();
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked dropdownTexts" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during dropdownTexts" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[contains(text(),'" + param1
					+ "')]/following::label[text()='" + keysToSend + "']/following::input)[1]")));
			Thread.sleep(1000);
			wait.until(
					ExpectedConditions.textToBePresentInElementLocated(
							By.xpath("//h1[contains(text(),'" + param1
									+ "')]/following::label[normalize-space(text())='" + keysToSend + "']"),
							keysToSend));
			WebElement waittill = driver.findElement(By.xpath("//h1[contains(text(),'" + param1
					+ "')]/following::label[normalize-space(text())='" + keysToSend + "']/following::input[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			Thread.sleep(500);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked dropdownTexts" + scripNumber);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during dropdownTexts" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//div[@class='AFDetectExpansion']/following::a[contains(text(),'Search')][1]")));
//			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//a[contains(text(),'Search')]"), "Search"));
			WebElement search = driver.findElement(
					By.xpath("//div[@class='AFDetectExpansion']/following::a[contains(text(),'Search')][1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(search).build().perform();
			search.click();
			Thread.sleep(10000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked dropdownTexts" + scripNumber);
			try {
				WebElement searchResult = driver.findElement(
						By.xpath("//div[contains(@id,'PopupId::content')]//*[text()='Search']/following::*[text()='"
								+ param2 + "']/following::input[1]"));
				typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
				String scripNumber1 = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked dropdownTexts" + scripNumber1);
			} catch (Exception e) {
				WebElement searchResult = driver.findElement(By.xpath(
						"//div[contains(@id,'PopupId::content')]//*[text()='Search']/following::*[text()='Name']/following::input[1]"));
				typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
				String scripNumber1 = fetchMetadataVO.getScriptNumber();
				log.error("Failed during dropdownTexts" + scripNumber1);
			}
			if (keysToSend != null) {
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
				try {
					WebElement text = driver.findElement(By.xpath(
							"//div[@class='AFDetectExpansion']/following::span[text()='Name']/following::span[text()='"
									+ keysToSend + "']"));
					text.click();
					String scripNumber1 = fetchMetadataVO.getScriptNumber();
					log.info("Sucessfully Clicked dropdownTexts" + scripNumber1);
				} catch (Exception e) {
					WebElement text = driver
							.findElement(By.xpath("(//span[contains(text(),'" + keysToSend + "')])[1]"));
					text.click();
					String scripNumber1 = fetchMetadataVO.getScriptNumber();
					log.error("Failed during dropdownTexts" + scripNumber1);
				}
			}
			try {
				WebElement button = driver
						.findElement(By.xpath("//*[text()='Search']/following::*[normalize-space(text())='" + param2
								+ "']/following::*[not (@aria-disabled) and text()='K'][1]"));
				String scripNumber1 = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked dropdownTexts" + scripNumber1);
				button.click();
			} catch (Exception e) {
				WebElement button = driver
						.findElement(By.xpath("//*[text()='Search']/following::*[normalize-space(text())='" + param2
								+ "']/following::*[not (@aria-disabled) and text()='OK'][1]"));
				button.click();
				String scripNumber1 = fetchMetadataVO.getScriptNumber();
				log.error("Failed during dropdownTexts" + scripNumber1);
			}

			return;
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during dropdownTexts" + scripNumber);
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

						+ "\"]/following::label[text()='" + param2 + "']/preceding-sibling::input)[2]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//*[text()=\"" + param1

						+ "\"]/following::label[text()='" + param2 + "']/preceding-sibling::input)[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				log.info("Sucessfully Clicked Time Entry multiplelinestableSendKeys" + scripNumber);

				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Time Entry multiplelinestableSendKeys" + scripNumber);
			System.out.println(e);

		}
		try {

			if (param1.equalsIgnoreCase("Mon")) {
				Thread.sleep(1000);
				WebElement saturday = driver.findElement(By.xpath("(//td[contains(text(),'Saturday')])[1]"));

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[8]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[8]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				screenshot(driver, fetchMetadataVO, customerDetails);

				return;
			}
			if (param1.equalsIgnoreCase("Tue")) {

				Thread.sleep(1000);
				WebElement saturday = driver.findElement(By.xpath("(//td[contains(text(),'Saturday')])[1]"));
				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[9]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				String xpath = "(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[9]";
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				screenshot(driver, fetchMetadataVO, customerDetails);

				return;
			}
			if (param1.equalsIgnoreCase("Wed")) {
				WebElement saturday = driver.findElement(By.xpath("(//td[contains(text(),'Saturday')])[1]"));

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[10]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[10]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				screenshot(driver, fetchMetadataVO, customerDetails);

				return;
			}
			if (param1.equalsIgnoreCase("Thu")) {

				WebElement saturday = driver.findElement(By.xpath("(//td[contains(text(),'Saturday')])[1]"));
				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[11]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[11]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				screenshot(driver, fetchMetadataVO, customerDetails);

				return;
			}
			if (param1.equalsIgnoreCase("Fri")) {
				WebElement saturday = driver.findElement(By.xpath("(//td[contains(text(),'Saturday')])[1]"));

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[12]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[12]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				screenshot(driver, fetchMetadataVO, customerDetails);

				return;
			}
			if (param1.equalsIgnoreCase("Sat")) {

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[13]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[13]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				screenshot(driver, fetchMetadataVO, customerDetails);

				return;
			}
			if (param1.equalsIgnoreCase("Sunday")) {

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[14]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[14]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				screenshot(driver, fetchMetadataVO, customerDetails);

				return;
			}
		} catch (Exception e) {

			System.out.println(e);

		}

		try {

			if (param1.equalsIgnoreCase("Mon")) {
				Thread.sleep(1000);
				WebElement saturday = driver.findElement(By.xpath("(//td[contains(text(),'Sat')])[3]"));

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[8]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[8]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				log.info("Sucessfully Clicked Mon multiplelinestableSendKeys" + scripNumber);

				return;
			}
			if (param1.equalsIgnoreCase("Tue")) {

				Thread.sleep(1000);
				WebElement saturday = driver.findElement(By.xpath("(//td[contains(text(),'Sat')])[3]"));
				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[9]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[9]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				log.info("Sucessfully Clicked Mon multiplelinestableSendKeys" + scripNumber);

				return;
			}
			if (param1.equalsIgnoreCase("Wed")) {
				WebElement saturday = driver.findElement(By.xpath("(//td[contains(text(),'Sat')])[3]"));

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[10]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[10]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				screenshot(driver, fetchMetadataVO, customerDetails);

				return;
			}
			if (param1.equalsIgnoreCase("Thu")) {

				WebElement saturday = driver.findElement(By.xpath("(//td[contains(text(),'Sat')])[3]"));
				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[11]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();
				String scripNumber = fetchMetadataVO.getScriptNumber();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				String xpath = "(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[11]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				screenshot(driver, fetchMetadataVO, customerDetails);

				return;
			}
			if (param1.equalsIgnoreCase("Fri")) {
				WebElement saturday = driver.findElement(By.xpath("(//td[contains(text(),'Sat')])[3]"));

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[12]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[12]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				screenshot(driver, fetchMetadataVO, customerDetails);

				return;
			}
			if (param1.equalsIgnoreCase("Sat")) {

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[13]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[13]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				screenshot(driver, fetchMetadataVO, customerDetails);

				return;
			}
			if (param1.equalsIgnoreCase("Sunday")) {

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[14]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();
				String scripNumber = fetchMetadataVO.getScriptNumber();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				String xpath = "(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[14]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				screenshot(driver, fetchMetadataVO, customerDetails);

				return;
			}
		} catch (Exception e) {

			System.out.println(e);

		}

		try {

			if (param1.equalsIgnoreCase("Mon")) {
				Thread.sleep(1000);

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[6]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[6]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				screenshot(driver, fetchMetadataVO, customerDetails);

				return;
			}
			if (param1.equalsIgnoreCase("Tue")) {

				Thread.sleep(1000);
				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[7]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[7]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				screenshot(driver, fetchMetadataVO, customerDetails);

				return;
			}
			if (param1.equalsIgnoreCase("Wed")) {
				Thread.sleep(1000);

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[8]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Wed multiplelinestableSendKeys" + scripNumber);
				return;
			}
			if (param1.equalsIgnoreCase("Thu")) {

				Thread.sleep(1000);
				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[9]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[9]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				log.info("Sucessfully Clicked Thu multiplelinestableSendKeys" + scripNumber);
				return;
			}
			if (param1.equalsIgnoreCase("Fri")) {
				Thread.sleep(1000);

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[10]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[10]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				log.info("Sucessfully Clicked Fri multiplelinestableSendKeys" + scripNumber);
				return;
			}
			if (param1.equalsIgnoreCase("Sat")) {

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[13]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[13]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				log.info("Sucessfully Clicked Sat multiplelinestableSendKeys" + scripNumber);
				return;
			}
			if (param1.equalsIgnoreCase("Sunday")) {

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[14]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[14]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				log.info("Sucessfully Clicked Sunday multiplelinestableSendKeys" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  multiplelinestableSendKeys" + scripNumber);
			System.out.println(e);

		}

	}

	public void tableSendKeys(WebDriver driver, String param1, String param2, String param3, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {
		// DH 34
		try {
			if (param1.equalsIgnoreCase("Budget Lines")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				// WebElement waittill = driver.findElement(By.xpath("(//*[text()='" + param1 +
				// "'])[1]/following::label[text()='" + param2 + "']/preceding::input[1]"));

				WebElement waittill = driver.findElement(By.xpath(
						"(//table[@summary='Budget Lines']//label[text()='Total']/preceding-sibling::input[contains(@id,'tRCIN')][1]"));

				Thread.sleep(1000);
				// wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h1[normalize-space(text())='"+param1+"']/following::label[text()='"+param2+"']"),
				// param2));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Provider or Receiver tableSendKeys" + scripNumber);
				String xpath = "(//*[text()='param1'])[1]/following::label[text()='param2']/preceding::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during tableSendKeys" + scripNumber);
			System.out.println(e);
		}

		// DH 29
		try {
			if (param1.equalsIgnoreCase("Associated Projects")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittill = driver.findElement(By.xpath("(//*[text()='" + param1
						+ "'])[1]/following::label[text()='" + param2 + "']/preceding::input[1]"));
				Thread.sleep(1000);
				// wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h1[normalize-space(text())='"+param1+"']/following::label[text()='"+param2+"']"),
				// param2));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Provider or Receiver tableSendKeys" + scripNumber);
				String xpath = "(//*[text()='param1'])[1]/following::label[text()='param2']/preceding::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during tableSendKeys" + scripNumber);
			System.out.println(e);
		}
		// New Code for PTP.PO.511
		try {
			if (param1.equalsIgnoreCase("Suppliers") && param2.equalsIgnoreCase("Internal Responder")) {
				WebElement waittill = driver.findElement(By
						.xpath("//*[text()='" + param1 + "']/following::input[contains(@id,'internalResponder')][1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				// waittill.sendKeys(keysToSend);
				Thread.sleep(1000);
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked tableSendKeys" + scripNumber);
				String xpath = "//*[text()='param1']/following::input[contains(@id,'internalResponder')][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during tableSendKeys" + scripNumber);
			System.out.println(e);
		}

		try {
			if (param1.equalsIgnoreCase("Assigned Sets")) {
				WebElement waittill = driver
						.findElement(By.xpath("//span[text()='" + param2 + "']/following::input[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
//        waittill.sendKeys(keysToSend);
				Thread.sleep(1000);
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked tableSendKeys" + scripNumber);
				String xpath = "//span[text()='" + param2 + "']/following::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  tableSendKeys" + scripNumber);
			System.out.println(e);
		}
		// Dh changes 8
		try {
			if (param1.equalsIgnoreCase("Journals") && param2.equalsIgnoreCase("journalBatch")) {
				WebElement waittill = driver.findElement(
						By.xpath("//*[text()='" + param1 + "']/following::input[contains(@id,'" + param2 + "')][1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
//		        waittill.sendKeys(keysToSend);
				Thread.sleep(1000);
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked tableSendKeys" + scripNumber);
				String xpath = "//span[text()='" + param2 + "']/following::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  tableSendKeys" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Add Project Customer")) {
				WebElement waittill = driver
						.findElement(By.xpath("//*[text()='" + param1 + "']/following::label[text()='" + param2
								+ "']/preceding-sibling::input[not(@type='hidden')]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
//        waittill.sendKeys(keysToSend);
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked tableSendKeys" + scripNumber);
				String xpath = "//*[text()='" + param1 + "']/following::label[text()='" + param2
						+ "']/preceding-sibling::input[not(@type='hidden')]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  tableSendKeys" + scripNumber);
			System.out.println(e);
		}
		// DH changes
		try {
			if (param1.equalsIgnoreCase("Review Distributions") && param2.equalsIgnoreCase("Account Class")) {
				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),'" + param1
						+ "')]/following::*[text()='" + param2 + "']//preceding::input[contains(@id,'Filter')])[3]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				// waittill.sendKeys(keysToSend);
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked tableSendKeys" + scripNumber);
				String xpath = "//*[text()='" + param1 + "']/following::label[text()='" + param2
						+ "']/preceding-sibling::input[not(@type='hidden')]";
				// service.saveXpathParams(param1, param2, scripNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during tableSendKeys" + scripNumber);
			System.out.println(e);
		}
		// DH changes
		try {

			if ((param1.equalsIgnoreCase("Receipt Details") && param2.equalsIgnoreCase("Application Reference"))) {
				WebElement waittill = driver.findElement(By.xpath("//h1[text()='" + param1
						+ "']/following::input[contains(@id,'trxNumberList')]/following::a[@title='Search']/preceding-sibling::input[1]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Associated Projects or Funded Amount tableSendKeys" + scripNumber);
				String xpath = "//*[text()='param1']/following::label[text()='param2']/preceding-sibling::input[1]";
				// service.saveXpathParams(param1, param2, scripNumber, xpath);

				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Associated Projects or Funded Amount tableSendKeys" + scripNumber);
			System.out.println(e);

		}
		try {

			if ((param1.equalsIgnoreCase("Associated Projects") && param2.equalsIgnoreCase("Funded Amount"))
					|| param2.equalsIgnoreCase("Amount")
					|| param1.equalsIgnoreCase("Review Distributions") && param2.equalsIgnoreCase("Distribution")) {
				WebElement waittill = driver.findElement(By.xpath("//*[text()=\"" + param1

						+ "\"]/following::label[text()='" + param2 + "']/preceding-sibling::input[1]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Associated Projects or Funded Amount tableSendKeys" + scripNumber);
				String xpath = "//*[text()='param1']/following::label[text()='param2']/preceding-sibling::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  Associated Projects or Funded Amount tableSendKeys" + scripNumber);
			System.out.println(e);

		}

		try {

			if (param1.equalsIgnoreCase("Associated Projects") && param2.equalsIgnoreCase("Project Number")) {

				WebElement waittill = driver.findElement(By.xpath("//*[text()=\"" + param1

						+ "\"]/following::label[text()='" + param2 + "']/preceding::span[1]/input"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Associated Projects or FProject Number tableSendKeys" + scripNumber);
				String xpath = "//*[text()='param1']/following::label[text()='param2']/preceding::span[1]/input";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Associated Projects or FProject Number tableSendKeys" + scripNumber);
			System.out.println(e);

		}

		try {

			if (param1.equalsIgnoreCase("Associated Projects") && param2.equalsIgnoreCase("Task Number")) {

				WebElement waittill = driver.findElement(By.xpath("(//*[text()=\"" + param1

						+ "\"]/following::div[text()='Autocompletes on TAB']/preceding::input[1])[4]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Associated Projects or Task Number tableSendKeys" + scripNumber);
				String xpath = "(//*[text()='param1']/following::div[text()='Autocompletes on TAB']/preceding::input[1])[4]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Associated Projects or Task Number tableSendKeys" + scripNumber);
			System.out.println(e);

		}
		try {

			if (param1.equalsIgnoreCase("Time Entry") || param2.equalsIgnoreCase("Unit Price")) {

				WebElement waittill = driver.findElement(By.xpath("(//*[text()=\"" + param1

						+ "\"]/following::label[text()='" + param2 + "']/preceding-sibling::input)[1]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Time Entry tableSendKeys" + scripNumber);
				String xpath = "(//*[text()='param1']/following::label[text()='param2']/preceding-sibling::input)[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Time Entry tableSendKeys" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Mon")) {

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[1]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Mon tableSendKeys" + scripNumber);
				String xpath = "(//*[contains(text(),'param1')]/following::label[text()='param2']/preceding-sibling::input[1])[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
			if (param1.equalsIgnoreCase("Tue")) {

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[2]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Tue tableSendKeys" + scripNumber);
				String xpath = "(//*[contains(text(),'param1')]/following::label[text()='param2']/preceding-sibling::input[1])[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
			if (param1.equalsIgnoreCase("Wed")) {
				Thread.sleep(2000);

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[3]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Wed tableSendKeys" + scripNumber);
				String xpath = "(//*[contains(text(),'param1')]/following::label[text()='param2']/preceding-sibling::input[1])[3]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
			if (param1.equalsIgnoreCase("Thu")) {
				Thread.sleep(2000);

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[4]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Thu tableSendKeys" + scripNumber);
				String xpath = "(//*[contains(text(),'param1')]/following::label[text()='param2']/preceding-sibling::input[1])[4]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
			if (param1.equalsIgnoreCase("Fri")) {
				Thread.sleep(2000);

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[5]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Fri tableSendKeys" + scripNumber);
				String xpath = "(//*[contains(text(),'param1')]/following::label[text()='param2']/preceding-sibling::input[1])[5]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
			if (param1.equalsIgnoreCase("Sat")) {

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[6]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Sat tableSendKeys" + scripNumber);
				String xpath = "(//*[contains(text(),'param1')]/following::label[text()='param2']/preceding-sibling::input[1])[6]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
			if (param1.equalsIgnoreCase("Sunday")) {

				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1

						+ "\")]/following::label[text()='" + param2 + "']/preceding-sibling::input[1])[7]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Sunday tableSendKeys" + scripNumber);
				String xpath = "(//*[contains(text(),'param1')]/following::label[text()='param2']/preceding-sibling::input[1])[7]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  tableSendKeys" + scripNumber);
			System.out.println(e);

		}

		try {
			if (param1.equalsIgnoreCase("Quantity")) {
				Thread.sleep(5000);
				try {
					WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
					WebElement waittill = driver.findElement(
							By.xpath("(//text()='" + param1 + "']/preceding-sibling::input[ not (@value)])[1]"));
					Actions actions = new Actions(driver);
					actions.moveToElement(waittill).build().perform();
					typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
					screenshot(driver, fetchMetadataVO, customerDetails);
					String scripNumber = fetchMetadataVO.getScriptNumber();
					log.info("Sucessfully Clicked Quantity tableSendKeys" + scripNumber);
					String xpath = "(//text()='param1']/preceding-sibling::input[ not (@value)])[1]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);

				} catch (Exception e) {
					WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
					WebElement waittill = driver
							.findElement(By.xpath("//label[text()='" + param1 + "']/preceding-sibling::input[1]"));
					Actions actions = new Actions(driver);
					actions.moveToElement(waittill).build().perform();
					typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
					screenshot(driver, fetchMetadataVO, customerDetails);
					String scripNumber = fetchMetadataVO.getScriptNumber();
					log.error("Failed during Quantity tableSendKeys" + scripNumber);

				}
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  tableSendKeys" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Lines") && param2.equalsIgnoreCase("Price")) {
				Thread.sleep(10000);
				WebElement waittill = driver
						.findElement(By.xpath("//*[text()=\"" + param1 + "\"]/following::label[text()='" + param2
								+ "']/preceding-sibling::input[contains(@name,'AmountAsPrice')]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				Thread.sleep(4000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Lines or Price tableSendKeys" + scripNumber);
				String xpath = "//*[text()='param1']/following::label[text()='param2']/preceding-sibling::input[contains(@name,'AmountAsPrice')]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Clicked Lines or Price  tableSendKeys" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Lines") && param2.equalsIgnoreCase("Expenditure Item Date")) {
				Thread.sleep(10000);
				WebElement waittill = driver.findElement(By.xpath("(//*[text()=\"" + param1
						+ "\"]/following::label[text()='" + param2 + "']/preceding-sibling::input)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				Thread.sleep(4000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Lines or Expenditure Item Date tableSendKeys" + scripNumber);
				String xpath = "(//*[text()='param1']/following::label[text()='param2']/preceding-sibling::input)[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Lines or Expenditure Item Date tableSendKeys" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Lines") || param2.equalsIgnoreCase("Item")) {
				Thread.sleep(10000);
				WebElement waittill = driver.findElement(By.xpath("//*[text()=\"" + param1
						+ "\"]/following::label[text()='" + param2 + "']/preceding::input[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				Thread.sleep(4000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Lines or Item tableSendKeys" + scripNumber);
				String xpath = "//*[text()='param1']/following::label[text()='param2']/preceding::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Clicked Lines or Item  tableSendKeys" + scripNumber);
			System.out.println(e);
		}

		// DH 12
		try {

			if (param1.equalsIgnoreCase("Query By Example")) {
				Thread.sleep(4000);
				WebElement waittill = driver.findElement(By.xpath("//*[contains(text(),'" + param1
						+ "')]/following::th[@_d_index='" + param2 + "'][1]//input[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				Thread.sleep(4000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Lines or Item tableSendKeys" + scripNumber);
				String xpath = "//*[contains(text(),'param1')]/following::th[@_d_index='param2'][1]//input[1]";
				// service.saveXpathParams(param1, param2, scripNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Clicked Lines or Item tableSendKeys" + scripNumber);
			System.out.println(e);
		}

		try {
			if (param2.equalsIgnoreCase("Application Reference")) {
				Thread.sleep(4000);
				WebElement waittill = driver.findElement(By.xpath(
						"(//h1[text()=\"" + param1 + "\"]/following::*[text()='" + param2 + "']/following::input)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Application Reference tableSendKeys" + scripNumber);
				String xpath = "(//h1[text()='param1']/following::*[text()='param2']/following::input)[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Application Reference tableSendKeys" + scripNumber);
			System.out.println(e);
		}
		// Add Xpath for "Register a mass promise to pay(AR.249)

		try {

			if (param1.equalsIgnoreCase("Transaction")) {

				WebElement waittill = driver.findElement(By.xpath(

						"(//span[text()='" + param1 + "']/preceding::input[@type='text'])[2]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittill).build().perform();

				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);

				screenshot(driver, fetchMetadataVO, customerDetails);

				String scripNumber = fetchMetadataVO.getScriptNumber();

				log.info("Sucessfully Clicked tableSendKeys" + scripNumber);

				String xpath = "(//span[text()='param1']/preceding::input[@type='text'])[2]";

				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;

			}

		} catch (Exception e) {

			String scripNumber = fetchMetadataVO.getScriptNumber();

			log.error("Failed during tableSendKeys" + scripNumber);

			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Provider") || param1.equalsIgnoreCase("Receiver")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittill = driver.findElement(By.xpath("(//*[text()='" + param1 + "']/following::*[text()='"
						+ param2 + "']/preceding-sibling::input)[2]"));
				Thread.sleep(1000);
				// wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h1[normalize-space(text())='"+param1+"']/following::label[text()='"+param2+"']"),
				// param2));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Provider or Receiver tableSendKeys" + scripNumber);
				String xpath = "(//*[text()='param1']/following::*[text()='param2']/preceding-sibling::input)[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  tableSendKeys" + scripNumber);
			System.out.println(e);
		}
		try {
			Thread.sleep(6000);
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			WebElement waittill = driver.findElement(By.xpath("(//h1[text()='" + param1 + "']/following::label[text()='"
					+ param2 + "']/preceding-sibling::input[not(@type='hidden')])[1]"));
			Thread.sleep(1000);
			// wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h1[normalize-space(text())='"+param1+"']/following::label[text()='"+param2+"']"),
			// param2));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			Thread.sleep(6000);
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked tableSendKeys" + scripNumber);
			String xpath = "(//h1[text()='param1']/following::label[text()='param2']/preceding-sibling::input[not(@type='hidden')])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  tableSendKeys" + scripNumber);
			System.out.println(e);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath(
					"//h1[text()='" + param1 + "']/following::*[text()='" + param2 + "']/preceding-sibling::input[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked tableSendKeys" + scripNumber);
			String xpath = "//h1[text()='param1']/following::*[text()='param2']/preceding-sibling::input[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  tableSendKeys" + scripNumber);
			System.out.println(e);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("//h1[text()='" + param1 + "']/following::span[text()='"
					+ param2 + "']/preceding::input[contains(@id,'descColumn::content')]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked tableSendKeys" + scripNumber);
			String xpath = "//h1[text()='Assets']/following::span[text()='Description']/preceding::input[contains(@id,'descColumn::content')]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  tableSendKeys" + scripNumber);
			System.out.println(e);
		}

		// Add Xpath for "Journal submitted but Approver on vacation(RTR.GL.115)"

		try {
			WebElement waittill = driver.findElement(By.xpath("//*[text()='" + param1 + "']/following::span[text()='"
					+ param2 + "']/preceding::input[contains(@id,'journalBatch::content')]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked tableSendKeys" + scripNumber);
			String xpath = "//*[text()='param1']/following::span[text()='param2']/preceding::input[contains(@id,'journalBatch::content')]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during tableSendKeys" + scripNumber);
			System.out.println(e);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath(
					"//h1[text()='" + param1 + "']/following::label[text()='" + param2 + "']/preceding::input[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			screenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(5000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked tableSendKeys" + scripNumber);
			String xpath = "//h1[text()='param1']/following::label[text()='param2']/preceding::input[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  tableSendKeys" + scripNumber);
			System.out.println(e);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("(//*[text()=\"" + param1
					+ "\"]/following::label[text()='" + param2 + "']/preceding-sibling::input)[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked tableSendKeys" + scripNumber);
			String xpath = "(//*[text()='param1']/following::label[text()='param2']/preceding-sibling::input)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  tableSendKeys" + scripNumber);
			System.out.println(e);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("(//*[text()=\"" + param1
					+ "\"]/following::label[text()='" + param2 + "']/preceding-sibling::textarea)[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked tableSendKeys" + scripNumber);
			String xpath = "(//*[text()='param1']/following::label[text()='param2']/preceding-sibling::textarea)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  tableSendKeys" + scripNumber);
			System.out.println(e);
		}
		try {
			WebElement waittill = driver
					.findElement(By.xpath("//*[text()=\"" + param1 + "\"]/following::table[@summary='" + param2
							+ "']//*[text()='" + param3 + "']/following::input[contains(@id,'NewBdgtPctLst')][1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked tableSendKeys" + scripNumber);
			String xpath = "//*[text()='param1']/following::table[@summary='param2']//*[text()='param3']/following::input[contains(@id,'NewBdgtPctLst')][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  tableSendKeys" + scripNumber);
			System.out.println(e);
		}
		try {
			// tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			// Thread.sleep(1000);
			// enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			WebElement waittill = driver.findElement(By.xpath("(//table[@summary='" + param1 + "']//label[text()='"
					+ param2 + "']/preceding-sibling::input)[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked tableSendKeys" + scripNumber);
			String xpath = "(//table[@summary='param1']//label[text()='param2']/preceding-sibling::input)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  tableSendKeys" + scripNumber);
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
					By.xpath("//div[@class='AFDetectExpansion']/following::*[text()='" + keysToSend + "']"),
					keysToSend));
			WebElement waittext = driver.findElement(
					By.xpath("//div[@class='AFDetectExpansion']/following::*[text()='" + keysToSend + "']"));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked tableDropdownTexts" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  tableDropdownTexts" + scripNumber);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//table[@summary='" + param1 + "']/following::li[text()='" + keysToSend + "']")));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath("//table[@summary='" + param1 + "']/following::li[text()='" + keysToSend + "']"),
					keysToSend));
			WebElement waittext = driver.findElement(
					By.xpath("//table[@summary='" + param1 + "']/following::li[text()='" + keysToSend + "']"));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked tableDropdownTexts" + scripNumber);
			return;
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  tableDropdownTexts" + scripNumber);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//*[text()='" + param1 + "']/following::li[text()='" + keysToSend + "']")));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath("//*[text()='" + param1 + "']/following::li[text()='" + keysToSend + "']"), keysToSend));
			WebElement waittext = driver
					.findElement(By.xpath("//*[text()='" + param1 + "']/following::li[text()='" + keysToSend + "']"));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked tableDropdownTexts" + scripNumber);
			return;
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  tableDropdownTexts" + scripNumber);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//*[text()='" + param1 + "']/following::td[text()='" + keysToSend + "']")));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath("//*[text()='" + param1 + "']/following::td[text()='" + keysToSend + "']"), keysToSend));
			WebElement waittext = driver
					.findElement(By.xpath("//*[text()='" + param1 + "']/following::td[text()='" + keysToSend + "']"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked tableDropdownTexts" + scripNumber);
			return;
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  tableDropdownTexts" + scripNumber);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"//div[contains(@id,'dropdownPopup::content')]/following::a[contains(text(),'Search')][1]")));
//		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//a[contains(text(),'Search')]"), "Search"));
			WebElement search = driver.findElement(By
					.xpath("//div[contains(@id,'dropdownPopup::content')]/following::a[contains(text(),'Search')][1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(search).build().perform();
			search.click();
			Thread.sleep(10000);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"//div[contains(@id,'PopupId::content')]//*[text()='Search']/following::*[normalize-space(text())='"
							+ param2 + "']/following::input[1]")));
			WebElement searchResult = driver.findElement(By.xpath(
					"//div[contains(@id,'PopupId::content')]//*[text()='Search']/following::*[normalize-space(text())='"
							+ param2 + "']/following::input[1]"));
			typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
			if (keysToSend != null) {
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
				WebElement text = driver.findElement(By.xpath("(//span[contains(text(),'" + keysToSend + "')])[1]"));
				text.click();
			}
			try {
				WebElement button = driver.findElement(By.xpath(
						"//*[text()='Search']/following::*[text()='" + param2 + "']/following::*[text()='K'][1]"));
				button.click();
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked tableDropdownTexts" + scripNumber);
			} catch (Exception e) {
				WebElement button = driver.findElement(By.xpath(
						"//*[text()='Search']/following::*[text()='" + param2 + "']/following::*[text()='OK'][1]"));
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.error("Failed during  tableDropdownTexts" + scripNumber);
				button.click();
			}
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  tableDropdownTexts" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			try {
				WebElement searchResult = driver
						.findElement(By.xpath("//*[text()='Search']/following::*[text()='Name']/following::input[1]"));
				typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked tableDropdownTexts" + scripNumber);
			} catch (Exception e) {
				WebElement searchResult = driver
						.findElement(By.xpath("//*[text()='Search']/following::*[text()='Value']/following::input[1]"));
				typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.error("Failed during  tableDropdownTexts" + scripNumber);
			}

			WebElement text = driver.findElement(By.xpath("(//span[contains(text(),'" + keysToSend + "')])[1]"));
			text.click();
			Thread.sleep(1000);
			try {
				WebElement button = driver.findElement(
						By.xpath("//*[text()='Search']/following::*[text()='Name']/following::*[text()='OK'][1]"));
				button.click();
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked tableDropdownTexts" + scripNumber);
			} catch (Exception e) {
				WebElement button = driver.findElement(
						By.xpath("//*[text()='Search']/following::*[text()='Value']/following::*[text()='OK'][1]"));
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.error("Failed during  tableDropdownTexts" + scripNumber);
				button.click();
			}

			return;
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  tableDropdownTexts" + scripNumber);
		}
		try {
			WebElement button = driver
					.findElement(By.xpath("//*[text()='Search']/following::*[normalize-space(text())='" + param2
							+ "']/following::*[text()='OK'][1]"));
			button.click();
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked tableDropdownTexts" + scripNumber);
			return;
		} catch (Exception e) {
			System.out.println(e);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  tableDropdownTexts" + scripNumber);
			throw e;
		}
	}

	public void tableDropdownValues(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {
		// dh 8
		try {
			if (param2.equalsIgnoreCase("Type")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1
						+ "']/following::label[normalize-space(text())='" + param2 + "']/preceding::input[2]")));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
						+ "']/following::label[normalize-space(text())='" + param2 + "']/preceding::input[2]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// clickTableDropdown(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				tableDropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(3000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		try {
			if (param1.equalsIgnoreCase("Add Project Customer")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(@id,'projectRole')]")));
				WebElement waittext = driver.findElement(By.xpath("//a[contains(@id,'projectRole')]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				tableDropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "(//a[contains(@id,'projectRole')]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				log.info("Sucessfully Clicked tableDropdownValues" + scripNumber);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  tableDropdownValues" + scripNumber);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("(//*[text()='" + param1 + "']/following::a[contains(@id,'" + param2 + "')])[1]")));
			WebElement waittext = driver.findElement(
					By.xpath("(//*[text()='" + param1 + "']/following::a[contains(@id,'" + param2 + "')])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			tableDropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();

			String xpath = "(//*[text()='param1']/following::a[contains(@id,'param2')])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully Clicked tableDropdownValues" + scripNumber);
			return;
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  tableDropdownValues" + scripNumber);
		}
		try {
			if (param1.equalsIgnoreCase("Billing")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"//*[text()='" + param1 + "']/following::label[text()='" + param2 + "']/following::a[1]")));
				WebElement waittext = driver.findElement(By.xpath(
						"//*[text()='" + param1 + "']/following::label[text()='" + param2 + "']/following::a[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				Thread.sleep(2000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				tableDropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(3000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "//*[text()='param1']/following::label[text()='param2']/following::a[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				log.info("Sucessfully Clicked Billing tableDropdownValues" + scripNumber);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Billing tableDropdownValues" + scripNumber);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By
					.xpath("//*[text()='" + param1 + "']/following::label[text()='" + param2 + "']/preceding::a[1]")));
			WebElement waittext = driver.findElement(
					By.xpath("//*[text()='" + param1 + "']/following::label[text()='" + param2 + "']/preceding::a[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			tableDropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(3000);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();

			String xpath = "//*[text()='param1']/following::label[text()='param2']/preceding::a[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully Clicked tableDropdownValues" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during tableDropdownValues" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//table[@summary='" + param1 + "']//input/following-sibling::a[1]")));
			WebElement waittext = driver
					.findElement(By.xpath("//table[@summary='" + param1 + "']//input/following-sibling::a[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			tableDropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();

			String xpath = "//table[@summary='param1']//input/following-sibling::a[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully Clicked tableDropdownValues" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  tableDropdownValues" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()='" + param1
					+ "']/following::input[contains(@id,'" + param2 + "')][1]/following::a[1]")));
			WebElement waittext = driver.findElement(By.xpath("//*[text()='" + param1
					+ "']/following::input[contains(@id,'" + param2 + "')][1]/following::a[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			tableDropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();

			String xpath = "//*[text()='param1']/following::input[contains(@id,'param2')][1]/following::a[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully Clicked tableDropdownValues" + scripNumber);
			return;
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  tableDropdownValues" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void dropdownValues(WebDriver driver, String param1, String param2, String param3, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {
		// DH 32
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("//*[contains(@id,'popup-container')]//*[text()='" + param1
							+ "']/following::*[text()='" + param2 + "']/following::input[not (@type='hidden')][1]")));
			WebElement waittext = driver.findElement(By.xpath("//*[contains(@id,'popup-container')]//*[text()='"
					+ param1 + "']/following::*[text()='" + param2 + "']/following::input[not (@type='hidden')][1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			waittext.click();
			Thread.sleep(4000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[contains(@id,'popup-container')]//*[text()='param1']/following::*[text()='param2']/following::input[not (@type='hidden')][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully Clicked Schedule New Process or Name dropdownValues" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Event Type dropdownValues" + scripNumber);
			System.out.println(e);
		}

		// DH 29
		try {
			if (param1.equalsIgnoreCase("Create Event") && (param2.equalsIgnoreCase("Event Type"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//h1[normalize-space(text())='" + param1 + "']/following::label[text()='" + param2
								+ "']/following::a[contains(@id,'eventType')]")));
				WebElement waittext = driver.findElement(By.xpath("//h1[normalize-space(text())='" + param1
						+ "']/following::label[text()='" + param2 + "']/following::a[contains(@id,'eventType')]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				WebElement text = driver.findElement(
						By.xpath("//div[contains(@id,'popup-container')]//td[text()='" + keysToSend + "'][1]"));
				text.click();
				Thread.sleep(4000);

				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//h1[normalize-space(text())='param1']/following::label[text()='param2']/following::a[contains(@id,'eventType')]"
						+ ";" + "//div[contains(@id,'popup-container')]//td[text()='" + keysToSend + "'][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				log.info("Sucessfully Clicked Schedule New Process or Name dropdownValues" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Event Type dropdownValues" + scripNumber);
			System.out.println(e);
		}

		// DH 14 SCP.512
		try {
			if (param1.equalsIgnoreCase("Search") && param2.equalsIgnoreCase("Order Type")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())='"
						+ param1 + "']/following::label[text()='" + param2 + "']/following::input)[1]")));

				WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())='" + param1
						+ "']/following::label[text()='" + param2 + "']/following::input)[1]"));

				Actions actions = new Actions(driver);

				waittext.click();

				Thread.sleep(10000);

				WebElement search = driver
						.findElement(By.xpath("//div[contains(@id,'popup-container')]//label[text()='All']/input[1]"));

				search.click();

				Thread.sleep(4000);

				search.click();

				WebElement Value = driver.findElement(By
						.xpath("//div[contains(@id,'popup-container')]//label[text()='" + keysToSend + "']/input[1]"));

				Value.click();
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//div[@class='AFDetectExpansion']/following::*[text()='param1']/following::*[normalize-space(text())='param2 ']/following::a[1]"
						+ ";" + "//a[contains(text(),'Search')]" + ";"
						+ "//div[@class='AFDetectExpansion']/following::*[text()='Search']/following::*[normalize-space(text())=' param2 ']/following::input[1]"
						+ ";"
						+ "//div[@class='AFDetectExpansion']/following::span[contains(text(),'keysToSend ')][1]/following::button[text()='OK'][1]"
						+ ";"
						+ "//div[@class='AFDetectExpansion']/following::*[text()=' param1 ']/following::*[normalize-space(text())='param2']/following::a[1]/following::button[text()='OK']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				log.info("Sucessfully Clicked Schedule New Process or Name dropdownValues" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Schedule New Process or Name dropdownValues" + scripNumber);
			System.out.println(e);
		}

		// DH 24
		try {
			if (param1.equalsIgnoreCase("Pay Groups") || param1.equalsIgnoreCase("Sources")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[text()='" + param1 + "']/following::*[@title='" + param2 + "']")));
				WebElement waittext = driver
						.findElement(By.xpath("//*[text()='" + param1 + "']/following::*[@title='" + param2 + "']"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(2000);
				WebElement search = driver.findElement(By.xpath("//a[contains(text(),'Search')]"));
				clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO);
				Thread.sleep(5000);
				WebElement values = driver.findElement(By.xpath(
						"//div[@class='AFDetectExpansion']/following::*[text()='Search']/following::*[normalize-space(text())='"
								+ param2 + "']/following::input[1]"));
				typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				WebElement select = driver
						.findElement(By.xpath("//div[@class='AFDetectExpansion']/following::span[starts-with(text(),'"
								+ keysToSend + "')][1]"));
				clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
				WebElement searchok = driver
						.findElement(By.xpath("//div[@class='AFDetectExpansion']/following::span[contains(text(),'"
								+ keysToSend + "')][1]/following::button[text()='OK'][1]"));
				clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[text()='param1']/following::*[@title='param2']" + ";"
						+ "//a[contains(text(),'Search')]" + ";"
						+ "//div[@class='AFDetectExpansion']/following::*[text()='Search']/following::*[normalize-space(text())=' param2 ']/following::input[1]"
						+ ";"
						+ "//div[@class='AFDetectExpansion']/following::span[contains(text(),'keysToSend ')][1]/following::button[text()='OK'][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				log.info("Sucessfully Clicked Schedule New Process or Name dropdownValues" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Schedule New Process or Name dropdownValues" + scripNumber);
			System.out.println(e);
		}

		/*
		 * //dh 12 try { if (param1.equalsIgnoreCase("Search") &&
		 * param2.equalsIgnoreCase("Order Type")) {
		 * 
		 * WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		 * 
		 * wait.until(ExpectedConditions.presenceOfElementLocated(By.
		 * xpath("(//*[normalize-space(text())='Search']/following::label[text()='Order Type']/following::a[1])[1]"
		 * )));
		 * 
		 * WebElement waittext = driver.findElement(By.
		 * xpath("(//*[normalize-space(text())='Search']/following::label[text()='Order Type']/following::a[1])[1]"
		 * ));
		 * 
		 * Actions actions = new Actions(driver);
		 * 
		 * actions.click(waittext).build().perform();
		 * 
		 * Thread.sleep(4000);
		 * 
		 * WebElement search = driver.findElement(By.xpath(
		 * "//div[contains(@id,'popup-container')]//label[text()='All']/input[1]"));
		 * 
		 * search.click();
		 * 
		 * Thread.sleep(4000);
		 * 
		 * search.click();
		 * 
		 * WebElement Value = driver.findElement(By.xpath(
		 * "//div[contains(@id,'popup-container')]//label[text()='"+ keysToSend
		 * +"']/input[1]"));
		 * 
		 * Value.click();
		 * 
		 * Thread.sleep(1000);
		 * 
		 * String scripNumber = fetchMetadataVO.getScriptNumber();
		 * 
		 * String xpath =
		 * "//div[@class='AFDetectExpansion']/following::*[text()='param1']/following::*[normalize-space(text())='param2 ']/following::a[1]"
		 * + ";" + "//a[contains(text(),'Search')]" + ";" +
		 * "//div[@class='AFDetectExpansion']/following::*[text()='Search']/following::*[normalize-space(text())=' param2 ']/following::input[1]"
		 * + ";" +
		 * "//div[@class='AFDetectExpansion']/following::span[contains(text(),'keysToSend ')][1]/following::button[text()='OK'][1]"
		 * + ";" +
		 * "//div[@class='AFDetectExpansion']/following::*[text()=' param1 ']/following::*[normalize-space(text())='param2']/following::a[1]/following::button[text()='OK']"
		 * ; String scriptID=fetchMetadataVO.getScriptId();String
		 * metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(
		 * scriptID,metadataID,xpath);
		 * log.info("Sucessfully Clicked Schedule New Process or Name dropdownValues" +
		 * scripNumber); return; } } catch (Exception e) { String scripNumber =
		 * fetchMetadataVO.getScriptNumber();
		 * log.error("Failed during Schedule New Process or Name dropdownValues" +
		 * scripNumber); System.out.println(e); }
		 */

		try {
			if (param1.equalsIgnoreCase("Schedule New Process") && param2.equalsIgnoreCase("Name")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//div[@class='AFDetectExpansion']/following::*[text()='" + param1
								+ "']/following::*[normalize-space(text())='" + param2 + "']/following::a[1]")));
				WebElement waittext = driver
						.findElement(By.xpath("//div[@class='AFDetectExpansion']/following::*[text()='" + param1
								+ "']/following::*[normalize-space(text())='" + param2 + "']/following::a[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(2000);
				WebElement search = driver.findElement(By.xpath("//a[contains(text(),'Search')]"));
				clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO);
				Thread.sleep(5000);
				WebElement values = driver.findElement(By.xpath(
						"//div[@class='AFDetectExpansion']/following::*[text()='Search']/following::*[normalize-space(text())='"
								+ param2 + "']/following::input[1]"));
				typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				WebElement select = driver
						.findElement(By.xpath("//div[@class='AFDetectExpansion']/following::span[starts-with(text(),'"
								+ keysToSend + "')][1]"));
				clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
				WebElement searchok = driver
						.findElement(By.xpath("//div[@class='AFDetectExpansion']/following::span[contains(text(),'"
								+ keysToSend + "')][1]/following::button[text()='OK'][1]"));
				clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(5000);
				WebElement ok = driver.findElement(By.xpath("//div[@class='AFDetectExpansion']/following::*[text()='"
						+ param1 + "']/following::*[normalize-space(text())='" + param2
						+ "']/following::a[1]/following::button[text()='OK']"));
				ok.click();
				Thread.sleep(6000);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "//div[@class='AFDetectExpansion']/following::*[text()='param1']/following::*[normalize-space(text())='param2 ']/following::a[1]"
						+ ";" + "//a[contains(text(),'Search')]" + ";"
						+ "//div[@class='AFDetectExpansion']/following::*[text()='Search']/following::*[normalize-space(text())=' param2 ']/following::input[1]"
						+ ";"
						+ "//div[@class='AFDetectExpansion']/following::span[contains(text(),'keysToSend ')][1]/following::button[text()='OK'][1]"
						+ ";"
						+ "//div[@class='AFDetectExpansion']/following::*[text()=' param1 ']/following::*[normalize-space(text())='param2']/following::a[1]/following::button[text()='OK']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				log.info("Sucessfully Clicked Schedule New Process or Name dropdownValues" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Schedule New Process or Name dropdownValues" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Invoice Header") && param2.equalsIgnoreCase("Business Unit")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1
						+ "']/following::label[text()='" + param2 + "']/following::a[1]")));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
						+ "']/following::label[text()='" + param2 + "']/following::a[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "//*[normalize-space(text())='param1']/following::label[text()='param2']/following::a[1]"
						+ ";" + "//a[contains(text(),'Search')][1]" + ";"
						+ "//*[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::input[1]"
						+ ";" + "//span[text()='Name']/following::span[normalize-space(text())='keysToSend']" + ";"
						+ "//*[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::*[text()='OK'][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				try {
					actions.click(waittext).build().perform();
					Thread.sleep(10000);
					// WebElement popup1 =
					// driver.findElement(By.xpath("//div[contains(@id,'suggestions-popup')]"));
					WebElement search = driver.findElement(By.xpath("//a[contains(text(),'Search')][1]"));
					search.click();
					Thread.sleep(3000);
					wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
							"//*[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::input[1]")));
					WebElement searchResult = driver.findElement(By.xpath(
							"//*[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::input[1]"));
					typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
					Thread.sleep(5000);
					WebElement text = driver.findElement(By.xpath(
							"//span[text()='Name']/following::span[normalize-space(text())='" + keysToSend + "']"));
					text.click();
					Thread.sleep(1000);
					WebElement button = driver.findElement(By.xpath(
							"//*[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::*[text()='OK'][1]"));
					button.click();

					log.info("Sucessfully Clicked Invoice Header or Business Unit dropdownValues" + scripNumber);

				} catch (Exception e) {
					// String scripNumber = fetchMetadataVO.getScriptNumber();
					log.error("Failed during Invoice Header or Business Unit  dropdownValues" + scripNumber);
					System.out.println(e);
				}
				return;
			}
		} catch (Exception ex) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  dropdownValues" + scripNumber);
			System.out.println(ex);
		}
		// This is to select the dropdown and select 'All' and deselect All then
		// Selecting Draft
		try {
			if (param2.equalsIgnoreCase("Project Status") && keysToSend.equalsIgnoreCase("Draft")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1
						+ "']/following::label[text()='" + param2 + "']/following::a[1]")));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
						+ "']/following::label[text()='" + param2 + "']/following::a[1]"));
				Actions actions = new Actions(driver);
				actions.click(waittext).build().perform();
				Thread.sleep(4000);
				WebElement checkbox = driver.findElement(By.xpath("//label[text()='All']"));
				checkbox.click();
				Thread.sleep(3000);
				checkbox.click();
				WebElement text = driver.findElement(By.xpath("//label[text()='" + param2
						+ "']/following::label[normalize-space(text())='" + keysToSend + "']"));
				text.click();
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "//*[normalize-space(text())='param1']/following::label[text()='param2']/following::a[1]"
						+ ";" + "//label[text()='All']" + ";"
						+ "//label[text()='param2']/following::label[normalize-space(text())='keysToSend']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				log.info("Sucessfully Clicked Project Status or Draft dropdownValues" + scripNumber);
				return;
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
		// --------------------------(including new change
		// here)<------------------------------
		try {
			if (param1.equalsIgnoreCase("Create Order") && param2.equalsIgnoreCase("Search: Bill-to Account")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[contains(text(),'" + param1 + "')]/following::a[@title='" + param2 + "'][1]")));
				WebElement waittext = driver.findElement(
						By.xpath("//*[contains(text(),'" + param1 + "')]/following::a[@title='" + param2 + "'][1]"));
				Actions actions = new Actions(driver);
				actions.click(waittext).build().perform();
				Thread.sleep(4000);
				WebElement search = driver.findElement(By.xpath("//a[text()='Search...']"));
				search.click();
				Thread.sleep(2000);
				WebElement Value = driver.findElement(By.xpath(
						"//div[text()='Search and Select: Bill-to Account']/following::label[text()='Account Number']/following::input[1]"));
				typeIntoValidxpath(driver, keysToSend, Value, fetchConfigVO, fetchMetadataVO);
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(1000);
				WebElement clickok = driver
						.findElement(By.xpath("//h1[text()='Search']/following::button[text()='OK'][1]"));
				clickok.click();
				Thread.sleep(2000);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "//*[normalize-space(text())='param1']/following::label[text()='param2']/following::a[1]"
						+ ";" + "//label[text()='All']" + ";"
						+ "//label[text()='param2']/following::label[normalize-space(text())='keysToSend']";
				// service.saveXpathParams(param1, param2, scripNumber, xpath);
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				log.info("Sucessfully Clicked Project Status or Draft dropdownValues" + scripNumber);
				return;
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}

		// --------------------------------------------(ends
		// here)-------------------------------------

		try {
			if (param1.equalsIgnoreCase("Create Contract in Wizard") && param2.equalsIgnoreCase("Primary Party")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1
						+ "']/following::label[text()='" + param2 + "']/following::a[1]")));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
						+ "']/following::label[text()='" + param2 + "']/following::a[1]"));
				Actions actions = new Actions(driver);
				actions.click(waittext).build().perform();
				Thread.sleep(4000);
				WebElement Search = driver.findElement(By.xpath("(//a[text()='Search...'])[3]"));
				Search.click();
				WebElement Name = driver.findElement(By.xpath(
						"//h2[normalize-space(text())='Search']/following::label[text()='Name']/following::input[1]"));
				typeIntoValidxpath(driver, keysToSend, Name, fetchConfigVO, fetchMetadataVO);
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
				WebElement text = driver.findElement(By
						.xpath("//span[text()='Name']/following::span[normalize-space(text())='" + keysToSend + "']"));
				text.click();
				Thread.sleep(1000);
				WebElement button = driver.findElement(By.xpath(
						"//*[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::*[text()='OK'][1]"));
				button.click();
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "//*[normalize-space(text())='param1']/following::label[text()='param2']/following::a[1]"
						+ ";" + "(//a[text()='Search...'])[3]" + ";"
						+ "//h2[normalize-space(text())='Search']/following::label[text()='Name']/following::input[1]"
						+ ";" + "//span[text()='Name']/following::span[normalize-space(text())='keysToSend']" + ";"
						+ "//*[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::*[text()='OK'][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
		try {
			if (param1.equalsIgnoreCase("Create Bank Account") && param2.equalsIgnoreCase("Country")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1
						+ "']/following::label[text()='" + param2 + "']/following::a[1]")));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
						+ "']/following::label[text()='" + param2 + "']/following::a[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				actions.click(waittext).build().perform();
				Thread.sleep(10000);
				WebElement search = driver.findElement(By.xpath(
						"//*[contains(@id,'territoryShortNameId')]/following-sibling::a[contains(text(),'Search')]"));
				search.click();
				Thread.sleep(3000);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"(//h2[normalize-space(text())='Search']/following::label[normalize-space(text())='Name']/following::input)[1]")));
				WebElement searchResult = driver.findElement(By.xpath(
						"(//h2[normalize-space(text())='Search']/following::label[normalize-space(text())='Name']/following::input)[1]"));
				typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
				WebElement text = driver.findElement(By
						.xpath("//span[text()='Name']/following::span[normalize-space(text())='" + keysToSend + "']"));
				text.click();
				Thread.sleep(1000);
				WebElement button = driver.findElement(By.xpath(
						"//*[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::*[text()='OK'][1]"));
				button.click();
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "//*[normalize-space(text())='param1']/following::label[text()='param2']/following::a[1]"
						+ ";"
						+ "//*[contains(@id,'territoryShortNameId')]/following-sibling::a[contains(text(),'Search')]"
						+ ";"
						+ "(//h2[normalize-space(text())='Search']/following::label[normalize-space(text())='Name']/following::input)[1]"
						+ ";" + "//span[text()='Name']/following::span[normalize-space(text())='keysToSend']" + ";"
						+ "//*[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::*[text()='OK'][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				log.info("Sucessfully Clicked Create Bank Account or Country dropdownValues" + scripNumber);
				return;
			}
		} catch (Exception ex) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Create Bank Account or Country dropdownValues" + scripNumber);
			System.out.println(ex);
		}
		// for "PTP.PO.301 Request New Supplier" when exectuing in Fusion instance
		try {
			if (param1.equalsIgnoreCase("Company Details") && param2.equalsIgnoreCase("Tax Country")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1
						+ "']/following::label[text()='" + param2 + "']/following::a[@title='Search: Tax Country']")));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
						+ "']/following::label[text()='" + param2 + "']/following::a[@title='Search: Tax Country']"));
				Actions actions = new Actions(driver);
				actions.click(waittext).build().perform();
				Thread.sleep(4000);
				WebElement Search = driver.findElement(By.xpath("(//a[text()='Search...'])"));
				Search.click();
				WebElement Name = driver.findElement(By.xpath(
						"//div[normalize-space(text())='Search and Select: Tax Country']/following::label[text()='Name']/following::input[1]"));
				typeIntoValidxpath(driver, keysToSend, Name, fetchConfigVO, fetchMetadataVO);
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
				WebElement text = driver.findElement(By
						.xpath("//span[text()='Name']/following::span[normalize-space(text())='" + keysToSend + "']"));
				text.click();
				Thread.sleep(1000);
				WebElement button = driver.findElement(By.xpath(
						"//*[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::*[text()='OK'][1]"));
				button.click();
				return;
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
		try {
			if (param1.equalsIgnoreCase("Create Address") && param2.equalsIgnoreCase("Country")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1
						+ "']/following::label[text()='" + param2 + "']/following::a[@title='Search: Country']")));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
						+ "']/following::label[text()='" + param2 + "']/following::a[@title='Search: Country']"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				actions.click(waittext).build().perform();
				Thread.sleep(10000);
				WebElement search = driver.findElement(By.xpath(
						"//*[contains(@id,'inputComboboxListOfValues1')]/following-sibling::a[contains(text(),'Search')]"));
				search.click();
				Thread.sleep(3000);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"(//h2[normalize-space(text())='Search']/following::label[normalize-space(text())='Name']/following::input)[1]")));
				WebElement searchResult = driver.findElement(By.xpath(
						"(//h2[normalize-space(text())='Search']/following::label[normalize-space(text())='Name']/following::input)[1]"));
				typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
				WebElement text = driver.findElement(By
						.xpath("//span[text()='Name']/following::span[normalize-space(text())='" + keysToSend + "']"));
				text.click();
				Thread.sleep(5000);
				WebElement button = driver.findElement(By.xpath(
						"//button[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::button[text()='OK'][1]"));
				button.click();
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "//*[normalize-space(text())='param1']/following::label[text()='param2']/following::a[@title='Search: Country']"
						+ ";"
						+ "//*[contains(@id,'inputComboboxListOfValues1')]/following-sibling::a[contains(text(),'Search')]"
						+ ";"
						+ "(//h2[normalize-space(text())='Search']/following::label[normalize-space(text())='Name']/following::input)[1]"
						+ ";" + "//span[text()='Name']/following::span[normalize-space(text())='keysToSend']" + ";"
						+ "//button[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::button[text()='OK'][1]";

				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				log.info("Sucessfully Clicked Create Address or Country dropdownValues" + scripNumber);
				return;
			}
		} catch (Exception ex) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Create Address or Country  dropdownValues" + scripNumber);
			System.out.println(ex);
		}
		try {

			if (param1.equalsIgnoreCase("Assets")) {

				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[text()='" + param1 + "']/following::a[@role='button'][1]")));

				WebElement waittext = driver
						.findElement(By.xpath("//*[text()='" + param1 + "']/following::a[@role='button'][1]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittext).build().perform();

				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);

				waittext.click();

				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(30000);

				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "//*[text()='param1']/following::a[@role='button'][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				log.info("Sucessfully Clicked Assets dropdownValues" + scripNumber);
				return;

			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Assets dropdownValues" + scripNumber);
			System.out.println(e);

		}
		try {
			if (param1.equalsIgnoreCase("Create Request") || param2.equalsIgnoreCase("CIP Budget Code")
					|| param1.equalsIgnoreCase("Demographic Info")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[contains(text(),'" + param1
						+ "')]/following::label[normalize-space(text())='" + param2 + "']/following::a)[1]")));
				WebElement waittext = driver.findElement(By.xpath("(//*[contains(text(),'" + param1
						+ "')]/following::label[normalize-space(text())='" + param2 + "']/following::a)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "(//*[contains(text(),'param1')]/following::label[normalize-space(text())='param2']/following::a)[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				log.info("Sucessfully Clicked Create Request dropdownValues" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Create Request dropdownValues" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Payables to Ledger Reconciliation Report")) {

				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(10000);
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("(//span[contains(text(),'" + param2 + "')]/following::img)[1]")));
				WebElement waittext = driver
						.findElement(By.xpath("(//span[contains(text(),'" + param2 + "')]/following::img)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				Thread.sleep(4000);
				WebElement dropdown = driver.findElement(By.xpath("//span[text()='" + keysToSend + "']"));
				actions.moveToElement(dropdown).build().perform();
				dropdown.click();
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "(//span[contains(text(),'param2')]/following::img)[1]" + ";"
						+ "//span[text()='keysToSend']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				log.info("Sucessfully Clicked Payables to Ledger Reconciliation Report dropdownValues" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Payables to Ledger Reconciliation Report dropdownValues" + scripNumber);
			System.out.println(e);
		}
		try {

			if (param1.equalsIgnoreCase("P2P-3031-Spend Detail by Invoice Number")
					|| param1.equalsIgnoreCase("P2P-3026-Payment Terms by Supplier vs Actual Days Paid")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(10000);
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[normalize-space(text())='" + param2 + "']/following::input[1]")));
				WebElement waittext = driver
						.findElement(By.xpath("//*[normalize-space(text())='" + param2 + "']/following::input[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);

				Thread.sleep(2000);
				if (param2.equalsIgnoreCase("Procurement BU") || param2.equalsIgnoreCase("Business Unit")) {
					WebElement search = driver
							.findElement(By.xpath("//div[@class='listbox']//span[contains(text(),'Search')]"));
					// clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO);
					search.click();
					Thread.sleep(1000);
					WebElement values = driver.findElement(By.xpath("(//span[text()='Name']/following::input)[1]"));
					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
					WebElement select = driver.findElement(By
							.xpath("//*[text()='Name']/following::div[normalize-space(text())='" + keysToSend + "']"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
					WebElement searchok = driver
							.findElement(By.xpath("//span[text()='Name']/following::button[text()='OK']"));
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);
					screenshot(driver, fetchMetadataVO, customerDetails);
					String scripNumber = fetchMetadataVO.getScriptNumber();

					String xpath = "//*[normalize-space(text())='param2']/following::input[1]" + ";"
							+ "//div[@class='listbox']//span[contains(text(),'Search')]" + ";"
							+ "(//span[text()='Name']/following::input)[1]" + ";"
							+ "//span[text()='Name']/following::button[text()='OK']";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
					log.info("Sucessfully Clicked Procurement BU or Business Unit dropdownValues" + scripNumber);
					return;
				} else if (param2.equalsIgnoreCase("Requisition BU")) {
					Thread.sleep(2000);
					WebElement search = driver
							.findElement(By.xpath("(//div[@class='listbox']//span[contains(text(),'Search')])[1]"));
					// clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO);
					search.click();
					Thread.sleep(1000);
					WebElement values = driver
							.findElement(By.xpath("(//span[text()='Name']/following::input[@type='text'])[2]"));
					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
					WebElement select = driver
							.findElement(By.xpath("//*[text()='Name']/following::div[text()='" + keysToSend + "']"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
					WebElement searchok = driver
							.findElement(By.xpath("//span[text()='Name']/following::button[text()='OK'][2]"));
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);
					screenshot(driver, fetchMetadataVO, customerDetails);
					String scripNumber = fetchMetadataVO.getScriptNumber();

					String xpath = "//*[normalize-space(text())='param2']/following::input[1]" + ";"
							+ "(//div[@class='listbox']//span[contains(text(),'Search')])[1]" + ";"
							+ "(//span[text()='Name']/following::input[@type='text'])[2]" + ";"
							+ "//*[text()='Name']/following::div[text()='" + keysToSend + "']" + ";"
							+ "//span[text()='Name']/following::button[text()='OK'][2]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
					log.info("Sucessfully Clicked Requisition BU dropdownValues" + scripNumber);
					return;
				} else if (param2.equalsIgnoreCase("Supplier Name")) {
					if (keysToSend.equalsIgnoreCase("All")) {
						WebElement select = driver.findElement(By.xpath("//span[text()='" + param2
								+ "']/following::div[normalize-space(text())='" + keysToSend + "'][1]"));
						clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
						return;
					} else {
						WebElement search = driver
								.findElement(By.xpath("(//div[@class='listbox']//span[contains(text(),'Search')])[3]"));
						clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO);
						Thread.sleep(1000);
						WebElement values = driver
								.findElement(By.xpath("(//span[text()='Name']/following::input[@type='text'])[3]"));
						typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
						enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
						WebElement select = driver.findElement(By.xpath(
								"//*[text()='Name']/following::div[normalize-space(text())='" + keysToSend + "']"));
						clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
						WebElement searchok = driver
								.findElement(By.xpath("//span[text()='Name']/following::button[text()='OK'][3]"));
						clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);
						screenshot(driver, fetchMetadataVO, customerDetails);
						String scripNumber = fetchMetadataVO.getScriptNumber();

						String xpath = "//*[normalize-space(text())='param2']/following::input[1]" + ";"
								+ "(//div[@class='listbox']//span[contains(text(),'Search')])[3]" + ";"
								+ "(//span[text()='Name']/following::input[@type='text'])[3]" + ";"
								+ "//*[text()='Name']/following::div[normalize-space(text())='" + keysToSend + "']"
								+ ";" + "//span[text()='Name']/following::button[text()='OK'][3]";
						String scriptID = fetchMetadataVO.getScriptId();
						String lineNumber = fetchMetadataVO.getLineNumber();
						service.saveXpathParams(scriptID, lineNumber, xpath);
						log.info("Sucessfully  Supplier Name Clicked dropdownValues" + scripNumber);

						return;
					}
				}
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  dropdownValues" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("FIN-7073-UDG Cognos Extract")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(10000);
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[normalize-space(text())='" + param2 + "']/following::input[1]")));
				WebElement waittext = driver
						.findElement(By.xpath("//*[normalize-space(text())='" + param2 + "']/following::input[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(2000);
				if (param2.equalsIgnoreCase("Period Name")) {
					WebElement search = driver
							.findElement(By.xpath("//div[@class='listbox']//span[contains(text(),'Search')]"));
					clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO);
					Thread.sleep(1000);
					WebElement values = driver.findElement(By.xpath("(//span[text()='Name']/following::input)[1]"));
					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
					WebElement select = driver.findElement(By
							.xpath("//*[text()='Name']/following::div[normalize-space(text())='" + keysToSend + "']"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
					WebElement searchok = driver
							.findElement(By.xpath("//span[text()='Name']/following::button[text()='OK']"));
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);
					screenshot(driver, fetchMetadataVO, customerDetails);
					String scripNumber = fetchMetadataVO.getScriptNumber();

					String xpath = "//*[normalize-space(text())='param2']/following::input[1]" + ";"
							+ "//div[@class='listbox']//span[contains(text(),'Search')]" + ";"
							+ "(//span[text()='Name']/following::input)[1]" + ";"
							+ "//*[text()='Name']/following::div[normalize-space(text())='keysToSend']" + ";"
							+ "//span[text()='Name']/following::button[text()='OK']";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
					log.info("Sucessfully Clicked Period Name dropdownValues" + scripNumber);
					return;
				} else if (param2.equalsIgnoreCase("Legal Entity")) {
					WebElement search = driver
							.findElement(By.xpath("//div[@class='listbox']//span[contains(text(),'Search')]"));
					clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO);
					Thread.sleep(1000);
					WebElement values = driver.findElement(By.xpath(
							"//div[@class='masterDialog modalDialog']/following::span[text()='Name']/following::input[1]"));
					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
					Thread.sleep(6000);
					WebElement select = driver.findElement(By
							.xpath("//*[text()='Name']/following::div[normalize-space(text())='" + keysToSend + "']"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
					WebElement searchok = driver.findElement(By.xpath(
							"//div[@class='masterDialog modalDialog']/following::span[text()='Name']/following::button[text()='OK']"));
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);
					screenshot(driver, fetchMetadataVO, customerDetails);
					String scripNumber = fetchMetadataVO.getScriptNumber();

					String xpath = "//*[normalize-space(text())='param2']/following::input[1]" + ";"
							+ "//div[@class='listbox']//span[contains(text(),'Search')]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
					log.info("Sucessfully Clicked Legal Entity dropdownValues" + scripNumber);
					return;
				}
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  dropdownValues" + scripNumber);
			System.out.println(e);
		}
		try {
			if ((param1.equalsIgnoreCase("FIN-7056-Generate Customer Statements")
					|| param1.equalsIgnoreCase("FIN-7077-Customer Statement"))
					&& (param2.equalsIgnoreCase("Legal Entity") || param2.equalsIgnoreCase("Customer Name"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(10000);
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[normalize-space(text())='" + param2 + "']/following::input[1]")));
				WebElement waittext = driver
						.findElement(By.xpath("//*[normalize-space(text())='" + param2 + "']/following::input[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(2000);
				if (param2.equalsIgnoreCase("Legal Entity")) {
					WebElement search = driver.findElement(
							By.xpath("//a[contains(@id,'LEGAL_ENTITY')][1]//span/span[contains(text(),'Search')]"));
					clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO);
					Thread.sleep(1000);
					WebElement values = driver
							.findElement(By.xpath("(//span[text()='Name']/following::input[@type='text'])[1]"));
					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
					WebElement select = driver.findElement(By
							.xpath("//*[text()='Value']/following::div[normalize-space(text())='" + keysToSend + "']"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
					WebElement searchok = driver
							.findElement(By.xpath("//div[text()='Search']/following::button[text()='OK']"));
					actions.moveToElement(searchok).build().perform();
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);
					screenshot(driver, fetchMetadataVO, customerDetails);
					String scripNumber = fetchMetadataVO.getScriptNumber();

					String xpath = "//*[normalize-space(text())='param2']/following::input[1]" + ";"
							+ "//a[contains(@id,'LEGAL_ENTITY')][1]//span/span[contains(text(),'Search')]" + ";"
							+ "(//span[text()='Name']/following::input[@type='text'])[1]" + ";"
							+ "//*[text()='Value']/following::div[normalize-space(text())='keysToSend']" + ";"
							+ "//div[text()='Search']/following::button[text()='OK']";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
					log.info("Sucessfully Clicked Legal Entity dropdownValues" + scripNumber);
					return;
				} else if (param2.equalsIgnoreCase("Customer Name")) {
					WebElement search1 = driver.findElement(
							By.xpath("//a[contains(@id,'CUSTOMER_NAME')][1]//span/span[contains(text(),'Search')]"));
					clickValidateXpath(driver, fetchMetadataVO, search1, fetchConfigVO);
					Thread.sleep(1000);
					WebElement values = driver.findElement(By.xpath(
							"//div[@class='masterDialog modalDialog']/following::span[text()='Name']/following::input[@type='text'][1]"));
					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
					WebElement select = driver.findElement(
							By.xpath("//*[text()='Value']/following::div[contains(text(),'" + keysToSend + "')]"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
					Thread.sleep(1000);
					WebElement searchok = driver
							.findElement(By.xpath("//div[text()='Search']/following::button[text()='OK'][2]"));
					actions.moveToElement(searchok).build().perform();
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);
					screenshot(driver, fetchMetadataVO, customerDetails);
					String scripNumber = fetchMetadataVO.getScriptNumber();

					String xpath = "//*[normalize-space(text())='param2']/following::input[1]" + ";"
							+ "//a[contains(@id,'CUSTOMER_NAME')][1]//span/span[contains(text(),'Search')]" + ";"
							+ "//div[@class='masterDialog modalDialog']/following::span[text()='Name']/following::input[@type='text'][1]"
							+ ";" + "//*[text()='Value']/following::div[contains(text(),'" + keysToSend + "')]" + ";"
							+ "//div[text()='Search']/following::button[text()='OK'][2]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
					log.info("Sucessfully Clicked Customer Name dropdownValues" + scripNumber);
					return;
				}

			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  dropdownValues" + scripNumber);
			System.out.println(e);
		}
		// DH 27
		try {

			if (param1.equalsIgnoreCase("Applied Receipts Register")
					|| param1.equalsIgnoreCase("General Journals Report")
					|| param1.equalsIgnoreCase("Unapplied Receipt Register") || param2.equalsIgnoreCase("Request Name")
					|| (param1.equalsIgnoreCase("Receivables to Ledger Reconciliation")

							&& param2.equalsIgnoreCase("Ledger"))) {

				if (param1.equalsIgnoreCase("General Journals Report") && (param2.equalsIgnoreCase("Ledger"))) {

					WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

					Thread.sleep(5000);

					wait.until(ExpectedConditions.presenceOfElementLocated(

							By.xpath("(//span[text()='" + param2 + "']/following::img)[1]")));

					WebElement waittext = driver

							.findElement(By.xpath("(//span[text()='" + param2 + "']/following::img)[1]"));

					Actions actions = new Actions(driver);

					actions.moveToElement(waittext).build().perform();

					// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);

					waittext.click();

					Thread.sleep(2000);

				}

				else {

					WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

					Thread.sleep(10000);

					wait.until(ExpectedConditions.presenceOfElementLocated(

							By.xpath("(//span[contains(text(),'" + param2 + "')]/following::img)[1]")));

					WebElement waittext = driver

							.findElement(By.xpath("(//span[contains(text(),'" + param2 + "')]/following::img)[1]"));

					Actions actions = new Actions(driver);

					actions.moveToElement(waittext).build().perform();

					// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);

					waittext.click();

					Thread.sleep(2000);

				}

				if (param2.equalsIgnoreCase("Ledger") || param2.equalsIgnoreCase("Ledger Set")
						|| param2.equalsIgnoreCase("Entered Currency")

						|| param2.equalsIgnoreCase("Approval Status") || param2.equalsIgnoreCase("Batch Status")) {

					WebElement search = driver.findElement(

							By.xpath("//div[@class='floatingWindowDiv']//span[contains(text(),'Search')]"));

					// clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO);

					search.click();

					Thread.sleep(1000);

					WebElement dropdown = driver.findElement(By.xpath("//span[text()='Name']/following::select[1]"));

					dropdown.sendKeys("Starts");

					WebElement values = driver.findElement(By.xpath("(//span[text()='Name']/following::input)[1]"));

					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);

					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);

					Thread.sleep(10000);
					// DH 27
					WebElement select = driver.findElement(By

							.xpath("//*[text()='Name']/following::*[normalize-space(text())='" + keysToSend + "']"));

//								clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO); 

					select.click();
					//// DH 27
					Thread.sleep(1000);

					WebElement searchok = driver

							.findElement(By.xpath("//span[text()='Name']/following::a[text()='OK']"));

					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);

					// searchok.click();

					screenshot(driver, fetchMetadataVO, customerDetails);

					String scripNumber = fetchMetadataVO.getScriptNumber();

					String xpath = "(//span[contains(text(),'param2')]/following::img)[1]" + ";"

							+ "//div[@class='floatingWindowDiv']//span[contains(text(),'Search')]" + ";"

							+ "(//span[text()='Name']/following::input)[1]" + ";"

							+ "//*[text()='Name']/following::span[normalize-space(text())='keysToSend']" + ";"

							+ "//span[text()='Name']/following::a[text()='OK']";

					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);

					log.info("Sucessfully Clicked Ledger dropdownValues" + scripNumber);

					return;

				} else if (param2.equalsIgnoreCase("Request Name") || param2.equalsIgnoreCase("Source")
						|| param2.equalsIgnoreCase("Category")) {

					WebElement search = driver.findElement(

							By.xpath("//div[@class='floatingWindowDiv']//span[contains(text(),'Search')]"));

					// clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO);

					search.click();

					Thread.sleep(1000);

					WebElement values = driver.findElement(By.xpath("(//span[text()='Name']/following::input)[1]"));

					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);

					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);

					Thread.sleep(6000);
//               DH 27
					WebElement select = driver.findElement(By

							.xpath("//*[text()='Name']/following::*[normalize-space(text())='" + keysToSend + "']"));

					// clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);

					select.click();

					WebElement searchok = driver

							.findElement(By.xpath("//span[text()='Name']/following::a[text()='OK']"));

					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);

					screenshot(driver, fetchMetadataVO, customerDetails);

					String scripNumber = fetchMetadataVO.getScriptNumber();

					String xpath = "(//span[contains(text(),'param2')]/following::img)[1]" + ";"

							+ "//div[@class='floatingWindowDiv']//span[contains(text(),'Search')]" + ""

							+ "(//span[text()='Name']/following::input)[1]" + ";"

							+ "//*[text()='Name']/following::span[normalize-space(text())='keysToSend']" + ";"

							+ "//span[text()='Name']/following::a[text()='OK']";

					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);

					log.info("Sucessfully Clicked Request Name dropdownValues" + scripNumber);

					return;

				}

				else if (param2.equalsIgnoreCase("Business Unit") || param2.equalsIgnoreCase("Accounting Period")) {

					WebElement search = driver.findElement(

							By.xpath("//div[@class='floatingWindowDiv']//span[contains(text(),'Search')]"));

					// clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO);

					search.click();

					Thread.sleep(1000);

					WebElement values = driver.findElement(By.xpath("(//span[text()='Name']/following::input)[1]"));

					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);

					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);

					WebElement select = driver.findElement(By

							.xpath("//*[text()='Name']/following::span[normalize-space(text())='" + keysToSend + "']"));

					// clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);

					select.click();

					WebElement move = driver.findElement(By

							.xpath("//td[@title='Move']"));

					// clickValidateXpath(driver, fetchMetadataVO, move, fetchConfigVO);

					move.click();

					WebElement searchok = driver

							.findElement(By.xpath("//span[text()='Name']/following::a[text()='OK']"));

					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);

					// searchok.click();

					screenshot(driver, fetchMetadataVO, customerDetails);

					String scripNumber = fetchMetadataVO.getScriptNumber();

					String xpath = "(//span[contains(text(),'param2')]/following::img)[1]" + ";"

							+ "//div[@class='floatingWindowDiv']//span[contains(text(),'Search')]" + ";"

							+ "(//span[text()='Name']/following::input)[1]" + ";"

							+ "//*[text()='Name']/following::span[normalize-space(text())='keysToSend']" + ";"

							+ "//span[text()='Name']/following::a[text()='OK']";

					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);

					log.info("Sucessfully Clicked Ledger dropdownValues" + scripNumber);

					return;

				}

			}

		} catch (Exception e) {

			String scripNumber = fetchMetadataVO.getScriptNumber();

			log.error("Failed during  dropdownValues" + scripNumber);

			System.out.println(e);

		}
		try {
			if (param1.equalsIgnoreCase("FIN-7064-AP Invoice Summary")
					|| param1.equalsIgnoreCase("P2P-3000-AP Hold Detailed Report")
					|| param1.equalsIgnoreCase("FIN-7073-UDG Cognos Extract")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(15000);
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[normalize-space(text())='" + param2 + "']/following::input[1]")));
				WebElement waittext = driver
						.findElement(By.xpath("//*[normalize-space(text())='" + param2 + "']/following::input[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(2000);
				WebElement search = driver.findElement(
						By.xpath("(//a[contains(@id,'legal')or 'LE'][1]//span/span[contains(text(),'Search')])[2]"));
				clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO);
				WebElement values = driver
						.findElement(By.xpath("(//span[text()='Name']/following::input[@type='text'])"));
				typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				WebElement select = driver.findElement(
						By.xpath("//b[text()='Value']/following::div[normalize-space(text())='" + keysToSend + "']"));
				clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
				WebElement searchok = driver
						.findElement(By.xpath("//div[text()='Search']/following::button[text()='OK']"));
				clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);
				Thread.sleep(10000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(15000);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "//*[normalize-space(text())='param2']/following::input[1]" + ";"
						+ "(//a[contains(@id,'legal')or 'LE'][1]//span/span[contains(text(),'Search')])[2]" + ";"
						+ "(//span[text()='Name']/following::input[@type='text'])" + ";"
						+ "//b[text()='Value']/following::div[normalize-space(text())='keysToSend']" + ";"
						+ "//div[text()='Search']/following::button[text()='OK']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				log.info("Sucessfully Clicked dropdownValues" + scripNumber);
				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  dropdownValues" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Report")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(5000);
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[normalize-space(text())='" + param2 + "']/following::input[1]")));
				WebElement waittext = driver
						.findElement(By.xpath("//*[normalize-space(text())='" + param2 + "']/following::input[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(2000);
				if (param2.equalsIgnoreCase("Procurement Business Unit")) {
					WebElement search = driver.findElement(
							By.xpath("//a[contains(@id,'PROCUREMENT')][1]//span/span[contains(text(),'Search')]"));
					clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO);
					WebElement values = driver
							.findElement(By.xpath("(//span[text()='Name']/following::input[@type='text'])[1]"));
					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
					WebElement select = driver.findElement(By
							.xpath("//*[text()='Value']/following::div[normalize-space(text())='" + keysToSend + "']"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
					WebElement searchok = driver
							.findElement(By.xpath("//div[text()='Search']/following::button[text()='OK']"));
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);
					screenshot(driver, fetchMetadataVO, customerDetails);
					String scripNumber = fetchMetadataVO.getScriptNumber();

					String xpath = "//*[normalize-space(text())='param2']/following::input[1]" + ";"
							+ "//a[contains(@id,'PROCUREMENT')][1]//span/span[contains(text(),'Search')]" + ";"
							+ "(//span[text()='Name']/following::input[@type='text'])[1]" + ";"
							+ "//*[text()='Value']/following::div[normalize-space(text())='keysToSend']" + ";"
							+ "//div[text()='Search']/following::button[text()='OK']";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
					log.info("Sucessfully Clicked Report dropdownValues" + scripNumber);
					return;
				} else if (param2.equalsIgnoreCase("Ledger")) {
					Thread.sleep(1000);
					WebElement search = driver
							.findElement(By.xpath("//a[contains(@id,'REQ')][1]//span/span[contains(text(),'Search')]"));
					clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO);
					WebElement values = driver
							.findElement(By.xpath("(//span[text()='Name']/following::input[@type='text'])[2]"));
					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
					WebElement select = driver.findElement(By.xpath(
							"//*[text()='Value']/following::div[normalize-space(text())='" + keysToSend + "'][2]"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
					WebElement searchok = driver
							.findElement(By.xpath("//div[text()='Search']/following::button[text()='OK'][2]"));
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);
					screenshot(driver, fetchMetadataVO, customerDetails);
					String scripNumber = fetchMetadataVO.getScriptNumber();

					String xpath = "//*[normalize-space(text())='param2']/following::input[1]" + ";"
							+ "//a[contains(@id,'REQ')][1]//span/span[contains(text(),'Search')]" + ";"
							+ "(//span[text()='Name']/following::input[@type='text'])[2]" + ";"
							+ "//*[text()='Value']/following::div[normalize-space(text())='keysToSend'][2]" + ";"
							+ "//div[text()='Search']/following::button[text()='OK'][2]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
					log.info("Sucessfully Clicked dropdownValues" + scripNumber);
					return;
				} else if (param2.equalsIgnoreCase("Request Name")) {
					Thread.sleep(1000);
					WebElement search = driver
							.findElement(By.xpath("//a[contains(@id,'REQ')][1]//span/span[contains(text(),'Search')]"));
					clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO);
					WebElement values = driver
							.findElement(By.xpath("(//span[text()='Name']/following::input[@type='text'])[2]"));
					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
					WebElement select = driver.findElement(By.xpath(
							"//*[text()='Value']/following::div[normalize-space(text())='" + keysToSend + "'][2]"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
					WebElement searchok = driver
							.findElement(By.xpath("//div[text()='Search']/following::button[text()='OK'][2]"));
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);
					screenshot(driver, fetchMetadataVO, customerDetails);
					String scripNumber = fetchMetadataVO.getScriptNumber();

					String xpath = "//*[normalize-space(text())='param2']/following::input[1]" + ";"
							+ "//a[contains(@id,'REQ')][1]//span/span[contains(text(),'Search')]" + ";"
							+ "(//span[text()='Name']/following::input[@type='text'])[2]" + ";"
							+ "//*[text()='Value']/following::div[normalize-space(text())='keysToSend'][2]" + ";"
							+ "//div[text()='Search']/following::button[text()='OK'][2]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
					log.info("Sucessfully Clicked dropdownValues" + scripNumber);
					return;
				} else if (param2.equalsIgnoreCase("Requistion Business Unit")) {
					Thread.sleep(1000);
					WebElement search = driver
							.findElement(By.xpath("//a[contains(@id,'REQ')][1]//span/span[contains(text(),'Search')]"));
					clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO);
					WebElement values = driver
							.findElement(By.xpath("(//span[text()='Name']/following::input[@type='text'])[2]"));
					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
					WebElement select = driver.findElement(By.xpath(
							"//*[text()='Value']/following::div[normalize-space(text())='" + keysToSend + "'][2]"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
					WebElement searchok = driver
							.findElement(By.xpath("//div[text()='Search']/following::button[text()='OK'][2]"));
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);
					screenshot(driver, fetchMetadataVO, customerDetails);
					String scripNumber = fetchMetadataVO.getScriptNumber();

					String xpath = "//*[normalize-space(text())='param2']/following::input[1]" + ";"
							+ "//a[contains(@id,'REQ')][1]//span/span[contains(text(),'Search')]" + ";"
							+ "(//span[text()='Name']/following::input[@type='text'])[2]" + ";"
							+ "//*[text()='Value']/following::div[normalize-space(text())='keysToSend'][2]" + ";"
							+ "//div[text()='Search']/following::button[text()='OK'][2]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
					log.info("Sucessfully Clicked Ledger dropdownValues" + scripNumber);
					return;
				} else if (param2.equalsIgnoreCase("Status")) {
					WebElement search = driver.findElement(
							By.xpath("//a[contains(@id,'STATUS')][1]//span/span[contains(text(),'Search')]"));
					clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO);
					WebElement values = driver
							.findElement(By.xpath("(//span[text()='Name']/following::input[@type='text'])[3]"));
					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
					WebElement select = driver.findElement(By.xpath(
							"//*[text()='Value']/following::div[normalize-space(text())='" + keysToSend + "'][1]"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
					WebElement searchok = driver
							.findElement(By.xpath("//div[text()='Search']/following::button[text()='OK'][3]"));
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);
					screenshot(driver, fetchMetadataVO, customerDetails);
					String scripNumber = fetchMetadataVO.getScriptNumber();

					String xpath = "//*[normalize-space(text())='param2']/following::input[1]" + ";"
							+ "//a[contains(@id,'STATUS')][1]//span/span[contains(text(),'Search')]" + ";"
							+ "(//span[text()='Name']/following::input[@type='text'])[3]" + ";"
							+ "//*[text()='Value']/following::div[normalize-space(text())='keysToSend'][1]" + ";"
							+ "//div[text()='Search']/following::button[text()='OK'][3]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
					log.info("Sucessfully Clicked Status dropdownValues" + scripNumber);
					return;
				} else if (param2.equalsIgnoreCase("Req. Business Unit") || param2.equalsIgnoreCase("Client BU")) {
					WebElement search = driver.findElement(
							By.xpath("//a[contains(@id,'paramsp')][1]//span/span[contains(text(),'Search')]"));
					clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO);
					WebElement values = driver
							.findElement(By.xpath("(//span[text()='Name']/following::input[@type='text'])[1]"));
					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
					WebElement select = driver.findElement(By
							.xpath("//*[text()='Value']/following::div[normalize-space(text())='" + keysToSend + "']"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
					WebElement searchok = driver
							.findElement(By.xpath("//div[text()='Search']/following::button[text()='OK']"));
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);
					screenshot(driver, fetchMetadataVO, customerDetails);
					String scripNumber = fetchMetadataVO.getScriptNumber();

					String xpath = "//*[normalize-space(text())='param2']/following::input[1]" + ";"
							+ "//a[contains(@id,'paramsp')][1]//span/span[contains(text(),'Search')]" + ";"
							+ "(//span[text()='Name']/following::input[@type='text'])[1]" + ";"
							+ "//*[text()='Value']/following::div[normalize-space(text())='keysToSend']" + ";"
							+ "//div[text()='Search']/following::button[text()='OK']";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
					log.info("Sucessfully Clicked Req. Business Unit dropdownValues" + scripNumber);
					return;
				} else if (param2.equalsIgnoreCase("Legal Entity")) {
					Thread.sleep(2000);
					WebElement search = driver.findElement(
							By.xpath("(//a[contains(@id,'paramsp')][1]//span/span[contains(text(),'Search')])[1]"));
					clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO);
					WebElement values = driver
							.findElement(By.xpath("(//span[text()='Name']/following::input[@type='text'])[1]"));
					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
					WebElement select = driver.findElement(By
							.xpath("//*[text()='Value']/following::div[normalize-space(text())='" + keysToSend + "']"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
					WebElement searchok = driver
							.findElement(By.xpath("//div[text()='Search']/following::button[text()='OK']"));
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);
					screenshot(driver, fetchMetadataVO, customerDetails);
					String scripNumber = fetchMetadataVO.getScriptNumber();

					String xpath = "//*[normalize-space(text())='param2']/following::input[1]" + ";"
							+ "(//a[contains(@id,'paramsp')][1]//span/span[contains(text(),'Search')])[1]" + ";"
							+ "(//span[text()='Name']/following::input[@type='text'])[1]" + ";"
							+ "//*[text()='Value']/following::div[normalize-space(text())='keysToSend']" + ";"
							+ "//div[text()='Search']/following::button[text()='OK']";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
					log.info("Sucessfully Clicked Legal Entity dropdownValues" + scripNumber);
					return;
				} else if (param2.equalsIgnoreCase("Customer Name")) {
					Thread.sleep(1000);
					WebElement search = driver.findElement(
							By.xpath("(//a[contains(@id,'CUSTOMER')][1]//span/span[contains(text(),'Search')])[1]"));
					clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO);
					WebElement values = driver
							.findElement(By.xpath("(//span[text()='Name']/following::input[@type='text'])[2]"));
					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
					WebElement select = driver.findElement(By.xpath(
							"//*[text()='Value']/following::div[normalize-space(text())='" + keysToSend + "'][2]"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
					WebElement searchok = driver
							.findElement(By.xpath("//div[text()='Search']/following::button[text()='OK'][2]"));
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);
					screenshot(driver, fetchMetadataVO, customerDetails);
					String scripNumber = fetchMetadataVO.getScriptNumber();

					String xpath = "//*[normalize-space(text())='param2']/following::input[1]" + ";"
							+ "(//a[contains(@id,'CUSTOMER')][1]//span/span[contains(text(),'Search')])[1]" + ";"
							+ "(//span[text()='Name']/following::input[@type='text'])[2]" + ";"
							+ "//*[text()='Value']/following::div[normalize-space(text())='keysToSend'][2]" + ";"
							+ "//div[text()='Search']/following::button[text()='OK'][2]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
					log.info("Sucessfully Clicked Customer Name dropdownValues" + scripNumber);
					return;
				} else if (param2.equalsIgnoreCase("Business Unit")) {
					if (keysToSend.equalsIgnoreCase("All")) {
						WebElement select = driver.findElement(By.xpath("//span[text()='" + param2
								+ "']/following::div[normalize-space(text())='" + keysToSend + "'][1]"));
						clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
						String scripNumber = fetchMetadataVO.getScriptNumber();

						String xpath = "//*[normalize-space(text())='param2']/following::input[1]" + ";"
								+ "//span[text()='" + param2
								+ "']/following::div[normalize-space(text())='keysToSend'][1]";
						String scriptID = fetchMetadataVO.getScriptId();
						String lineNumber = fetchMetadataVO.getLineNumber();
						service.saveXpathParams(scriptID, lineNumber, xpath);
						log.info("Sucessfully Clicked Business Unit dropdownValues" + scripNumber);
						return;
					} else {
						WebElement search = driver.findElement(
								By.xpath("//a[contains(@id,'BU')][1]//span/span[contains(text(),'Search')]"));
						clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO);
						WebElement values = driver
								.findElement(By.xpath("(//span[text()='Name']/following::input[@type='text'])[1]"));
						typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
						enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
						WebElement select = driver.findElement(By.xpath(
								"//*[text()='Value']/following::div[normalize-space(text())='" + keysToSend + "']"));
						clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
						WebElement searchok = driver
								.findElement(By.xpath("//div[text()='Search']/following::button[text()='OK']"));
						clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);
						Thread.sleep(10000);
						screenshot(driver, fetchMetadataVO, customerDetails);
						Thread.sleep(3000);
						String scripNumber = fetchMetadataVO.getScriptNumber();

						String xpath = "//*[normalize-space(text())='param2']/following::input[1]" + ";"
								+ "//a[contains(@id,'BU')][1]//span/span[contains(text(),'Search')]" + ";"
								+ "(//span[text()='Name']/following::input[@type='text'])[1]" + ";"
								+ "//*[text()='Value']/following::div[normalize-space(text())='keysToSend']" + ";"
								+ "//div[text()='Search']/following::button[text()='OK']";
						String scriptID = fetchMetadataVO.getScriptId();
						String lineNumber = fetchMetadataVO.getLineNumber();
						service.saveXpathParams(scriptID, lineNumber, xpath);
						log.info("Sucessfully Clicked dropdownValues" + scripNumber);
						return;
					}
				} else if (param2.equalsIgnoreCase("Supplier Name")) {
					if (keysToSend.equalsIgnoreCase("All")) {
						WebElement select = driver.findElement(By.xpath("//span[text()='" + param2
								+ "']/following::div[normalize-space(text())='" + keysToSend + "'][1]"));
						clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
						String scripNumber = fetchMetadataVO.getScriptNumber();

						String xpath = "//*[normalize-space(text())='param2']/following::input[1]" + ";"
								+ "//span[text()='param2']/following::div[normalize-space(text())='keysToSend'][1]";
						String scriptID = fetchMetadataVO.getScriptId();
						String lineNumber = fetchMetadataVO.getLineNumber();
						service.saveXpathParams(scriptID, lineNumber, xpath);
						log.info("Sucessfully Clicked Supplier Name dropdownValues" + scripNumber);
						return;
					} else {
						WebElement search = driver.findElement(
								By.xpath("//a[contains(@id,'SUPPLIER')][1]//span/span[contains(text(),'Search')]"));
						clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO);
						WebElement values = driver
								.findElement(By.xpath("(//span[text()='Name']/following::input[@type='text'])[1]"));
						typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
						enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
						WebElement select = driver.findElement(
								By.xpath("//*[normalize-space(text())='Value']/following::div[normalize-space(text())='"
										+ keysToSend + "']"));
						clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
						WebElement searchok = driver
								.findElement(By.xpath("//div[text()='Search']/following::button[text()='OK']"));
						clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);
						Thread.sleep(10000);
						screenshot(driver, fetchMetadataVO, customerDetails);
						Thread.sleep(3000);
						String scripNumber = fetchMetadataVO.getScriptNumber();

						String xpath = "//*[normalize-space(text())='param2']/following::input[1]" + ";"
								+ "//a[contains(@id,'SUPPLIER')][1]//span/span[contains(text(),'Search')]" + ";"
								+ "(//span[text()='Name']/following::input[@type='text'])[1]" + ";"
								+ "//*[normalize-space(text())='Value']/following::div[normalize-space(text())='keysToSend']"
								+ ";" + "//div[text()='Search']/following::button[text()='OK']";
						String scriptID = fetchMetadataVO.getScriptId();
						String lineNumber = fetchMetadataVO.getLineNumber();
						service.saveXpathParams(scriptID, lineNumber, xpath);
						log.info("Sucessfully Clicked dropdownValues" + scripNumber);
						return;
					}
				}
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  dropdownValues" + scripNumber);
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Basic Options") && param2.equalsIgnoreCase("Ledger")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"//*[normalize-space(text())='" + param1 + "']/following::label[normalize-space(text())='"
								+ param2 + "']/following::a[contains(@title,'" + param2 + "')]")));
				WebElement waittext = driver
						.findElement(By.xpath("//*[normalize-space(text())='" + param1 + "']/following::label[text()='"
								+ param2 + "']/following::a[contains(@title,'" + param2 + "')]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				try {
					actions.click(waittext).build().perform();
					Thread.sleep(6000);
					WebElement popup1 = driver.findElement(By.xpath("//div[@class='AFDetectExpansion']"));
					WebElement search = driver.findElement(
							By.xpath("//div[@class='AFDetectExpansion']/following::a[contains(text(),'Search')][1]"));
					actions.moveToElement(search).build().perform();
					search.click();
					Thread.sleep(10000);
					wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
							"//div[contains(@id,'PopupId::content')]//*[text()='Search']/following::*[normalize-space(text())='"
									+ param2 + "']/following::input[1]")));
					WebElement searchResult = driver.findElement(By.xpath(
							"//div[contains(@id,'PopupId::content')]//*[text()='Search']/following::*[normalize-space(text())='"
									+ param2 + "']/following::input[1]"));
					typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
					Thread.sleep(5000);
					WebElement text = driver
							.findElement(By.xpath("(//span[contains(text(),'" + keysToSend + "')])[1]"));
					text.click();
					screenshot(driver, fetchMetadataVO, customerDetails);
					WebElement button = driver
							.findElement(By.xpath("//*[text()='Search']/following::*[normalize-space(text())='" + param2
									+ "']/following::*[text()='OK'][1]"));
					button.click();
					String scripNumber = fetchMetadataVO.getScriptNumber();

					String xpath = "//*[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/following::a[contains(@title,'param2')]"
							+ ";" + "//div[@class='AFDetectExpansion']" + ";"
							+ "//div[@class='AFDetectExpansion']/following::a[contains(text(),'Search')][1]" + ";"
							+ "//div[contains(@id,'PopupId::content')]//*[text()='Search']/following::*[normalize-space(text())='param2']/following::input[1]"
							+ ";" + "(//span[contains(text(),'keysToSend')])[1]" + ";"
							+ "//*[text()='Search']/following::*[normalize-space(text())='param2']/following::*[text()='OK'][1]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
					log.info("Sucessfully Clicked Basic Options or Ledger dropdownValues" + scripNumber);
				} catch (Exception e) {
					String scripNumber = fetchMetadataVO.getScriptNumber();
					log.error("Failed during Basic Options or Ledger dropdownValues" + scripNumber);

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
					WebElement popup1 = driver.findElement(By.xpath("//div[@class='AFDetectExpansion']"));
					WebElement search = driver.findElement(
							By.xpath("//div[@class='AFDetectExpansion']/following::a[contains(text(),'Search')][1]"));
					actions.moveToElement(search).build().perform();
					search.click();
					Thread.sleep(10000);
					wait.until(ExpectedConditions.presenceOfElementLocated(
							By.xpath("//div[contains(@id,'PopupId::content')]//*[text()='Search']/following::*[text()='"
									+ param2 + "']/following::input[1]")));
					WebElement searchResult = driver.findElement(
							By.xpath("//div[contains(@id,'PopupId::content')]//*[text()='Search']/following::*[text()='"
									+ param2 + "']/following::input[1]"));
					typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
					Thread.sleep(5000);
					WebElement text = driver.findElement(By.xpath("//span[text()='" + param2
							+ "']/following::span[contains(text(),'" + keysToSend + "')][1]"));
					text.click();
					screenshot(driver, fetchMetadataVO, customerDetails);
					WebElement button = driver
							.findElement(By.xpath("//*[text()='Search']/following::*[normalize-space(text())='" + param2
									+ "']/following::*[text()='OK'][1]"));
					button.click();
					String scripNumber = fetchMetadataVO.getScriptNumber();
					String xpath = "//*[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/following::a[contains(@title,'param2')]"
							+ ";" + "//div[@class='AFDetectExpansion']/following::a[contains(text(),'Search')][1]" + ";"
							+ "//div[contains(@id,'PopupId::content')]//*[text()='Search']/following::*[text()='param2']/following::input[1]"
							+ ";" + "//span[text()='param2']/following::span[contains(text(),'keysToSend')][1]" + ";"
							+ "//*[text()='Search']/following::*[normalize-space(text())='param2']/following::*[text()='OK'][1]";

					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
					log.info("Sucessfully Clicked dropdownValues" + scripNumber);
				} catch (Exception e) {
					String scripNumber = fetchMetadataVO.getScriptNumber();
					log.error("Failed during  dropdownValues" + scripNumber);

					for (int i = 0; i <= 2; i++) {
						try {
							actions.click(waittext).build().perform();
							break;
						} finally {
							Thread.sleep(4000);
							WebElement popup1 = driver.findElement(By.xpath("//div[@class='AFDetectExpansion']"));
							WebElement search = driver.findElement(By.xpath(
									"//div[@class='AFDetectExpansion']/following::a[contains(text(),'Search')][1]"));
							actions.moveToElement(search).build().perform();
							search.click();
							Thread.sleep(10000);
							wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
									"//div[contains(@id,'PopupId::content')]//*[text()='Search']/following::*[normalize-space(text())='"
											+ param2 + "']/following::input[1]")));
							WebElement searchResult = driver.findElement(By.xpath(
									"//div[contains(@id,'PopupId::content')]//*[text()='Search']/following::*[normalize-space(text())='"
											+ param2 + "']/following::input[1]"));
							typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
							enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
							Thread.sleep(5000);
							WebElement text = driver
									.findElement(By.xpath("(//span[contains(text(),'" + keysToSend + "')])[1]"));
							text.click();
							screenshot(driver, fetchMetadataVO, customerDetails);
							WebElement button = driver.findElement(By.xpath("//*[text()='Search']/following::*[text()='"
									+ param2 + "']/following::*[text()='OK'][1]"));
							button.click();

							String xpath = "//div[@class='AFDetectExpansion']/following::a[contains(text(),'Search')][1]"
									+ ";" + "(//span[contains(text(),'keysToSend')])[1]" + ";"
									+ "//*[text()='Search']/following::*[text()='param2']/following::*[text()='OK'][1]";
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
			log.error("Failed during  dropdownValues" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1
					+ "']/following::label[normalize-space(text())='" + param2 + "']/following::a[1]")));
			WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
					+ "']/following::label[normalize-space(text())='" + param2 + "']/following::a[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			try {
				actions.click(waittext).build().perform();
				Thread.sleep(10000);
				WebElement popup1 = driver.findElement(By.xpath("//div[contains(@id,'suggestions-popup')]"));
				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				actions.release();
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "//*[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/following::a[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				log.info("Sucessfully Clicked dropdownValues" + scripNumber);
			} catch (Exception ex) {
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.error("Failed during  dropdownValues" + scripNumber);

				try {
					try {
						WebElement popup1 = driver.findElement(By.xpath("//div[@class='AFDetectExpansion']"));
						dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO,
								customerDetails);
						actions.release();
						screenshot(driver, fetchMetadataVO, customerDetails);
						// String xpath="//div[@class='AFDetectExpansion']";
						String xpath = "//*[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/following::a[1]";
						String scriptID = fetchMetadataVO.getScriptId();
						String lineNumber = fetchMetadataVO.getLineNumber();
						service.saveXpathParams(scriptID, lineNumber, xpath);
					} catch (Exception ex1) {
						for (int i = 0; i <= 2; i++) {
							actions.click(waittext).build().perform();
							break;
						}
						Thread.sleep(3000);
						WebElement popup1 = driver.findElement(By.xpath("//div[contains(@id,'suggestions-popup')]"));
						dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO,
								customerDetails);
						actions.release();
						screenshot(driver, fetchMetadataVO, customerDetails);
						String xpath = "//*[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/following::a[1]";
						String scriptID = fetchMetadataVO.getScriptId();
						String lineNumber = fetchMetadataVO.getLineNumber();
						service.saveXpathParams(scriptID, lineNumber, xpath);
					}
				} catch (Exception ex2) {
					WebElement popup1 = driver.findElement(By.xpath("//div[@class='AFDetectExpansion']"));
					dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
					actions.release();
					screenshot(driver, fetchMetadataVO, customerDetails);
					String xpath = "//*[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/following::a[1]";
					String scriptID = fetchMetadataVO.getScriptId();
					String lineNumber = fetchMetadataVO.getLineNumber();
					service.saveXpathParams(scriptID, lineNumber, xpath);
				}
			}
			return;
		} catch (Exception exe) {
			System.out.println(exe);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  dropdownValues" + scripNumber);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1
					+ "']/following::label[normalize-space(text())='" + param2 + "']/following::a[1]")));
			WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
					+ "']/following::label[text()='" + param2 + "']/following::a[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			try {
				actions.clickAndHold(waittext).build().perform();
				Thread.sleep(6000);
				WebElement popup1 = driver.findElement(By.xpath("//div[contains(@id,'dropdownPopup::content')]"));

				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				actions.release();
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "//*[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/following::a[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				log.info("Sucessfully Clicked dropdownValues" + scripNumber);
			} catch (Exception e) {
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.error("Failed during  dropdownValues" + scripNumber);
				for (int i = 0; i <= 2; i++) {
					try {
						actions.click(waittext).build().perform();
						break;
					} finally {
						dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO,
								customerDetails);
						actions.release();
						screenshot(driver, fetchMetadataVO, customerDetails);
						String xpath = "//*[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/following::a[1]";
						String scriptID = fetchMetadataVO.getScriptId();
						String lineNumber = fetchMetadataVO.getLineNumber();
						service.saveXpathParams(scriptID, lineNumber, xpath);
					}
				}

			}
			try {
				actions.click(waittext).build().perform();
				Thread.sleep(6000);
				WebElement popup1 = driver.findElement(By.xpath("//div[contains(@id,'dropdownPopup::content')][1]"));
				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				actions.release();
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "//div[contains(@id,'dropdownPopup::content')][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				log.info("Sucessfully Clicked dropdownValues" + scripNumber);
			} catch (Exception e) {
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.error("Failed during  dropdownValues" + scripNumber);
				for (int i = 0; i <= 2; i++) {
					try {
						actions.click(waittext).build().perform();
						break;
					} finally {
						dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO,
								customerDetails);
						screenshot(driver, fetchMetadataVO, customerDetails);
						String xpath = "//div[contains(@id,'dropdownPopup::content')][1]";
						String scriptID = fetchMetadataVO.getScriptId();
						String lineNumber = fetchMetadataVO.getLineNumber();
						service.saveXpathParams(scriptID, lineNumber, xpath);
					}
				}

			}
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  dropdownValues" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//label[normalize-space(text())='" + param1
					+ "']/following::*[normalize-space(text())='" + param2 + "']/following::a[1]")));
			WebElement waittext = driver.findElement(By.xpath("//label[normalize-space(text())='" + param1
					+ "']/following::*[normalize-space(text())='" + param2 + "']/following::a[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();

			String xpath = "//label[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']/following::a[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully Clicked dropdownValues" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  dropdownValues" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("(//div[contains(@id,'popup-container')]//*[normalize-space(text())='" + param1
							+ "']/following::*[normalize-space(text())=\"" + param2 + "\"]/following::a)[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//div[contains(@id,'popup-container')]//*[text()='"
					+ param1 + "']/following::*[normalize-space(text())=\"" + param2 + "\"]/following::a)[1]"));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();

			String xpath = "(//div[contains(@id,'popup-container')]//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']/following::a)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully Clicked dropdownValues" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  dropdownValues" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())='" + param1
					+ "']/following::*[normalize-space(text())=\"" + param2 + "\"]/following::a)[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())='" + param1
					+ "']/following::*[normalize-space(text())=\"" + param2 + "\"]/following::a)[1]"));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();

			String xpath = "(//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']/following::a)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully Clicked dropdownValues" + scripNumber);
			return;
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked dropdownValues" + scripNumber);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(text(),'Search')]")));
			WebElement search = driver.findElement(By.xpath("//a[contains(text(),'Search')]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(search).build().perform();
			search.click();
			Thread.sleep(2000);
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//*[normalize-space(text())='Search']/following::*[normalize-space(text())='" + param2
							+ "']/following::input[1]")));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(
					"//*[normalize-space(text())='Search']/following::*[normalize-space(text())='" + param2 + "']"),
					param2));
			WebElement searchResult = driver
					.findElement(By.xpath("//*[normalize-space(text())='Search']/following::*[normalize-space(text())='"
							+ param2 + "']/following::input[1]"));
			typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
			if (keysToSend != null) {
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
				WebElement text = driver
						.findElement(By.xpath("(//span[normalize-space(text())='" + keysToSend + "'])[1]"));
				text.click();
			}
			WebElement button = driver
					.findElement(By.xpath("//*[normalize-space(text())='Search']/following::*[normalize-space(text())='"
							+ param2 + "']/following::*[text()='K'][1]"));
			button.click();
			String scripNumber = fetchMetadataVO.getScriptNumber();

			String xpath = "//a[contains(text(),'Search')]" + ";"
					+ "//*[normalize-space(text())='Search']/following::*[normalize-space(text())='param2']/following::input[1]"
					+ ";" + "(//span[normalize-space(text())='keysToSend'])[1]" + ";"
					+ "//*[normalize-space(text())='Search']/following::*[normalize-space(text())='param2']/following::*[text()='K'][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully Clicked dropdownValues" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  dropdownValues" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"//*[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::input[1]")));
			WebElement searchResult = driver.findElement(By.xpath(
					"//*[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::input[1]"));
			typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
			enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(5000);
			WebElement text = driver.findElement(By.xpath("//span[normalize-space(text())='" + keysToSend + "']"));
			text.click();
			Thread.sleep(1000);
			WebElement button = driver.findElement(By.xpath(
					"//*[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::*[text()='OK'][1]"));
			button.click();
			String scripNumber = fetchMetadataVO.getScriptNumber();

			String xpath = "//*[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::input[1]"
					+ ";" + "//span[normalize-space(text())='keysToSend']" + ";"
					+ "//*[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::*[text()='OK'][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully Clicked dropdownValues" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  dropdownValues" + scripNumber);
			System.out.println(e);
		}
		try {
			WebElement button = driver
					.findElement(By.xpath("//*[normalize-space(text())='Search']/following::*[normalize-space(text())='"
							+ param2 + "']/following::*[normalize-space(text())='OK'][1]"));
			button.click();
			String scripNumber = fetchMetadataVO.getScriptNumber();

			String xpath = "//*[normalize-space(text())='Search']/following::*[normalize-space(text())='param2']/following::*[normalize-space(text())='OK'][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully Clicked dropdownValues" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();

			log.error("Failed during  dropdownValues" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[contains(text(),'" + param1
					+ "')]/following::label[normalize-space(text())='" + keysToSend + "']/following::input)[1]")));
			Thread.sleep(1000);
			wait.until(
					ExpectedConditions.textToBePresentInElementLocated(
							By.xpath("//h1[contains(text(),'" + param1
									+ "')]/following::label[normalize-space(text())='" + keysToSend + "']"),
							keysToSend));
			WebElement waittill = driver.findElement(By.xpath("//h1[contains(text(),'" + param1
					+ "')]/following::label[normalize-space(text())='" + keysToSend + "']/following::input[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			Thread.sleep(500);
			String scripNumber = fetchMetadataVO.getScriptNumber();

			String xpath = "(//h1[contains(text(),'param1')]/following::label[normalize-space(text())='keysToSend']/following::input)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully Clicked dropdownValues" + scripNumber);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  dropdownValues" + scripNumber);
			System.out.println(e);
		}

		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(@id,'" + param1 + "')]")));
			Thread.sleep(1000);
//wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//a[contains(@id,'drop')]"), keysToSend));
			WebElement waittill = driver.findElement(By.xpath("//a[contains(@id,'" + param1 + "')]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittill, fetchConfigVO);
			dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(500);
			String scripNumber = fetchMetadataVO.getScriptNumber();

			String xpath = "//a[contains(@id,'param1')]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully Clicked dropdownValues" + scripNumber);
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  dropdownValues" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	private void clickValidateXpath(WebDriver driver, ScriptDetailsDto fetchMetadataVO, WebElement waittext,
			FetchConfigVO fetchConfigVO) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click();", waittext);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked clickValidateXpath" + scripNumber);
			// waittext.click();
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  clickValidateXpath" + scripNumber);
			e.printStackTrace();
		}
	}

	public void clickFilter(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		// Dh changes 6
		try {
			if (param1.equalsIgnoreCase("Review Distributions")) {
				WebElement waittill = driver.findElement(By.xpath("(//*[contains(text(),'" + param1
						+ "')]/following::*[text()='Account Class']//preceding::input[contains(@id,'Filter')])[3]"));

				waittill.click();
				screenshot(driver, fetchMetadataVO, customerDetails);

				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickFilter" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			System.out.println("(//img[@title='" + param1 + "']/following::*[text()='" + param2
					+ "']/preceding::input[@type='text'])[3]");
		}

		try {
			WebElement waittill = driver.findElement(By.xpath("(//img[@title='" + param1 + "']/following::*[text()='"
					+ param2 + "']/preceding::input[@type='text'])[3]"));

			waittill.click();
			screenshot(driver, fetchMetadataVO, customerDetails);

			return;

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  clickFilter" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			System.out.println("(//img[@title='" + param1 + "']/following::*[text()='" + param2
					+ "']/preceding::input[@type='text'])[3]");
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("(//img[@title='" + param1 + "'])[1]"));

			waittill.click();
			Thread.sleep(2000);
			WebElement waittill1 = driver.findElement(By.xpath("(//img[@title='" + param1 + "']/following::*[text()='"
					+ param2 + "']/preceding::input[@type='text'])[3]"));
			waittill1.click();
			Thread.sleep(2000);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  clickFilter" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			System.out.println("//img[@title='" + param1 + "'][1]");
//			throw e;
		}
		// DH Changes 6
		try {
			WebElement waittill = driver.findElement(By.xpath("(//img[@title='" + param2 + "'])[1]"));

			waittill.click();
			Thread.sleep(3000);
			// WebElement waittill1 = driver.findElement(By.xpath("(//img[@title='" + param1
			// + "']/following::*[text()='"
			// + param2 + "']/preceding::input[@type='text'])[3]"));
			// waittill1.click();
			// Thread.sleep(2000);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  clickFilter" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			System.out.println("//img[@title='" + param1 + "'][1]");
			throw e;
		}
	}

	public String password(WebDriver driver, String inputParam, String keysToSend, FetchConfigVO fetchConfigVO,
			ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails) {
		try {
			WebElement waittill = driver.findElement(By.xpath("//*[contains(@placeholder,'" + inputParam + "')]"));
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked password" + scripNumber);
			return keysToSend;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  password" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			System.out.println(e);
			throw e;
		}
	}

	public void typeIntoValidxpath(WebDriver driver, String keysToSend, WebElement waittill,
			FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO) {
		try {
			waittill.clear();
			waittill.click();
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("arguments[0].value='" + keysToSend + "';", waittill);
			log.info("clear and typed the given Data");
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked typeIntoValidxpath" + scripNumber);

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  typeIntoValidxpath" + scripNumber);
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
		log.info("clear and typed the given Data");
	}

	public void moveToElement(WebDriver driver, String inputParam, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO) {
		WebElement waittill = driver.findElement(By.xpath("//*[normalize-space(text())='" + inputParam + "'][1]"));
		Actions actions = new Actions(driver);
		actions.moveToElement(waittill).build().perform();
		String scripNumber = fetchMetadataVO.getScriptNumber();
		log.info("Sucessfully Clicked moveToElement" + scripNumber);
	}

	public void scrollUsingElement(WebDriver driver, String inputParam, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {
		try {
			Thread.sleep(2000);
			WebElement waittill = driver
					.findElement(By.xpath("//span[normalize-space(text())='" + inputParam + "'][1]"));
			// ((JavascriptExecutor)driver).executeScript("document.body.style.zoom='50%';");
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			// ((JavascriptExecutor)driver).executeScript("document.body.style.zoom='100%';");
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//span[normalize-space(text())='inputParam'][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();

			log.error("Failed during  scrollUsingElement" + scripNumber);
			System.out.println(inputParam);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("//a[normalize-space(text())='" + inputParam + "']"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//a[normalize-space(text())='inputParam']";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  scrollUsingElement" + scripNumber);
			System.out.println(inputParam);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("//h1[normalize-space(text())='" + inputParam + "']"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//h1[normalize-space(text())='inputParam']";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Field during scrollUsingElement" + scripNumber);
			System.out.println(inputParam);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("(//h2[normalize-space(text())='" + inputParam + "'])"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//h2[normalize-space(text())='inputParam'])";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  scrollUsingElement" + scripNumber);
			System.out.println(inputParam);
		}
		try {
			WebElement waittill = driver
					.findElement(By.xpath("(//h3[normalize-space(text())='" + inputParam + "'])[2]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//h3[normalize-space(text())='inputParam'])[2]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
			return;
		} catch (Exception e) {
			System.out.println(inputParam);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("//td[normalize-space(text())='" + inputParam + "']"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//td[normalize-space(text())='inputParam']";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
			return;
		} catch (Exception e) {
			System.out.println(inputParam);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("//div[contains(text(),'" + inputParam + "')]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//div[contains(text(),'inputParam')]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  scrollUsingElement" + scripNumber);
			System.out.println(inputParam);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("(//table[@summary='" + inputParam + "']//td//a)[1]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//table[@summary='inputParam']//td//a)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  scrollUsingElement" + scripNumber);
			System.out.println(inputParam);
		}
		try {
			WebElement waittill = driver.findElement(
					By.xpath("(//label[normalize-space(text())=\"" + inputParam + "\"]/following::input)[1]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//label[normalize-space(text())='inputParam']/following::input)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
			return;
		} catch (Exception e) {
			System.out.println(inputParam);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("//a[contains(@id,'" + inputParam + "')]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//a[contains(@id,'inputParam')]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  scrollUsingElement" + scripNumber);
			System.out.println(inputParam);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("//li[normalize-space(text())='" + inputParam + "']"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//li[normalize-space(text())='inputParam']";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  scrollUsingElement" + scripNumber);
			System.out.println(inputParam);
		}
		try {
			WebElement waittill = driver
					.findElement(By.xpath("//label[normalize-space(text())=\"" + inputParam + "\"]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//label[normalize-space(text())='inputParam']";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  scrollUsingElement" + scripNumber);
			System.out.println(inputParam);
		}
		try {
			WebElement waittill = driver
					.findElement(By.xpath("//button[normalize-space(text())='" + inputParam + "']"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//button[normalize-space(text())='inputParam']";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  scrollUsingElement" + scripNumber);
			System.out.println(inputParam);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("//img[@title='" + inputParam + "']"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//img[@title='inputParam']";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  scrollUsingElement" + scripNumber);
			System.out.println(inputParam);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("(//*[@title='" + inputParam + "'])[1]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//*[@title='inputParam'])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  scrollUsingElement" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			System.out.println(inputParam);
			e.printStackTrace();
			throw e;
		}
	}

	private void scrollMethod(WebDriver driver, FetchConfigVO fetchConfigVO, WebElement waittill,
			ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails) {
		fetchConfigVO.getMedium_wait();
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		// WebElement elements =
		// wait.until(ExpectedConditions.elementToBeClickable(waittill));
		WebElement element = waittill;
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
		screenshot(driver, fetchMetadataVO, customerDetails);
		String scripNumber = fetchMetadataVO.getScriptNumber();
		log.info("Sucessfully Clicked scrollMethod" + scripNumber);
	}

	public void tab(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) throws Exception {
		try {
			Thread.sleep(4000);
			Actions action = new Actions(driver);
			action.sendKeys(Keys.TAB).build().perform();
			Thread.sleep(8000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked tab" + scripNumber);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  tab" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			System.out.println("Failed to do TAB Action");
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
					.presenceOfElementLocated(By.xpath("(//table[@summary='" + param1 + "']//tr[1]/following::a)[2]")));
			scrollUsingElement(driver, param1, fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(6000);
			WebElement waittext = driver
					.findElement(By.xpath("(//table[@summary='" + param1 + "']//tr[1]/following::a)[2]"));
			actions.moveToElement(waittext).build().perform();
			clickImage(driver, param2, param1, fetchMetadataVO, fetchConfigVO, customerDetails);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//table[@summary='" + param1 + "']//tr[1]/following::a)[2]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully Clicked mousehover" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  mousehover" + scripNumber);
			System.out.println(e);
		}
		try {
			Actions actions = new Actions(driver);
			WebElement waittill = driver.findElement(By.xpath(
					"(//table[@role='presentation']/following::a[normalize-space(text())='" + param1 + "'])[1]"));
			actions.moveToElement(waittill).build().perform();
			Thread.sleep(5000);
			System.out.print("Successfully executed Mousehover");
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//table[@role='presentation']/following::a[normalize-space(text())='" + param1 + "'])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully Clicked mousehover" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  mousehover" + scripNumber);
			System.out.println(e);
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
			log.info("Sucessfully Clicked enter" + scripNumber);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  enter" + scripNumber);
			System.out.println(e);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void deleteAllCookies(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) {
		try {
			driver.manage().deleteAllCookies();
			log.info("Successfully Deleted All The Cookies.");
		} catch (Exception e) {
			log.error("Failed To Delete All The Cookies.");
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			System.out.println("cookies not deleted");
			throw e;
		}
	}

	public void selectCheckBox(WebDriver driver, String xpath, ScriptDetailsDto fetchMetadataVO,
			CustomerProjectDto customerDetails) {
		try {
			WebElement element = driver.findElement(By.xpath(xpath));
			if (element.isSelected()) {
			} else {
				element.click();
			}
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked selectCheckBox" + scripNumber);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during selectCheckBox" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			e.printStackTrace();
			System.out.println(xpath);
			throw e;
		}
	}

	public void selectByText(WebDriver driver, String param1, String param2, String inputData,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {
		try {
			if (param1.equalsIgnoreCase("Address Purposes")) {
				Thread.sleep(2000);
				WebElement waittext = driver.findElement(
						By.xpath(("//*[normalize-space(text())='" + param1 + "']/following::*[normalize-space(text())='"
								+ param2 + "']/following::select[not (@title)]")));
				selectMethod(driver, inputData, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']/following::select[not (@title)]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				log.info("Sucessfully Clicked selectByText" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during selectByText" + scripNumber);
			System.out.println(param2);
		}
		try {
			if (param1.equalsIgnoreCase("Holds")) {
				Thread.sleep(2000);
				WebElement waittext = driver.findElement(
						By.xpath("//*[normalize-space(text())='" + param1 + "']/following::*[normalize-space(text())='"
								+ param2 + "']/preceding-sibling::select[@title='']"));
				selectMethod(driver, inputData, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']/preceding-sibling::select[@title='']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				log.info("Sucessfully Clicked Holds selectByText" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Holds selectByText" + scripNumber);
			System.out.println(param2);
		}
		try {
			if (param2.equalsIgnoreCase("Batch Status")) {
				Thread.sleep(2000);
				WebElement waittext = driver.findElement(By.xpath(
						("//*[normalize-space(text())='" + param1 + "']/following::label[normalize-space(text())='"
								+ param2 + "']/preceding-sibling::select[1]")));
				selectMethod(driver, inputData, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/preceding-sibling::select[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				log.info("Sucessfully Clicked Batch Status selectByText" + scripNumber);
				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Batch Status selectByText" + scripNumber);
			System.out.println(param2);
		}
		try {
			if (param1.equalsIgnoreCase("Release") && param2.equalsIgnoreCase("Name")) {
				Thread.sleep(5000);
				WebElement waittext = driver.findElement(By.xpath(
						("(//*[normalize-space(text())='" + param1 + "']/following::label[normalize-space(text())='"
								+ param2 + "']/preceding-sibling::select)[2]")));
				selectMethod(driver, inputData, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "(//*[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/preceding-sibling::select)[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				log.info("Sucessfully Clicked Release selectByText" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Release selectByText" + scripNumber);
			System.out.println(param2);
		}
		try {
			Thread.sleep(2000);
			WebElement waittext = driver.findElement(By.xpath(("//*[normalize-space(text())='" + param1
					+ "']/following::label[normalize-space(text())='" + param2 + "']/following::select[1]")));
			selectMethod(driver, inputData, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked selectByText" + scripNumber);

			String xpath = "//*[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/following::select[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during selectByText" + scripNumber);
			System.out.println(param2);
		}
		try {
			Thread.sleep(2000);
			WebElement waittext = driver.findElement(By.xpath(("//*[contains(text(),'" + param1
					+ "')]/following::label[normalize-space(text())='" + param2 + "']/preceding::select[1]")));
			selectMethod(driver, inputData, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();

			String xpath = "//*[contains(text(),'param1')]/following::label[normalize-space(text())='param2']/preceding::select[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully Clicked selectByText" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during selectByText" + scripNumber);
			System.out.println(param2);
		}
		try {
			Thread.sleep(2000);
			WebElement waittext = driver.findElement(By.xpath(("//*[normalize-space(text())='" + param1
					+ "']/following::label[normalize-space(text())='" + param2 + "']/preceding::select[1]")));
			selectMethod(driver, inputData, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();

			String xpath = "//*[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/preceding::select[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully Clicked selectByText" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during selectByText" + scripNumber);
			System.out.println(param2);
		}
		try {
			Thread.sleep(2000);
			WebElement waittext = driver.findElement(By.xpath(("//*[contains(text(),'" + param1
					+ "')]/following::label[normalize-space(text())='" + param2 + "']/following::select[1]")));
			selectMethod(driver, inputData, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();

			String xpath = "//*[contains(text(),'param1')]/following::label[normalize-space(text())='param2']/following::select[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully Clicked selectByText" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during selectByText" + scripNumber);
			System.out.println(param2);
		}
		try {
			if (param2 == "") {
				WebElement waittext = driver
						.findElement(By.xpath(("//*[contains(text(),'" + param1 + "')]/following::select[1]")));
				selectMethod(driver, inputData, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String xpath = "//*[contains(text(),'param1')]/following::select[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				log.info("Sucessfully Clicked selectCheckBox" + scripNumber);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during selectByText" + scripNumber);
			System.out.println(param2);
		}
		try {
			WebElement waittext = driver
					.findElement(By.xpath(("//*[contains(text(),'" + param1 + "')]/preceding-sibling::select[1]")));
			selectMethod(driver, inputData, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();

			String xpath = "//*[contains(text(),'param1')]/preceding-sibling::select[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			log.info("Sucessfully Clicked selectByText" + scripNumber);
			return;
		} catch (Exception e) {
			System.out.println(param2);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during selectByText" + scripNumber);
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
		log.info("Sucessfully Clicked selectMethod" + scripNumber);
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
			log.info("Sucessfully Clicked selectByValue" + scripNumber);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during selectByValue" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			System.out.println(xpath);
			e.printStackTrace();
			throw e;
		}
	}

	public void switchToDefaultFrame(WebDriver driver) {
		try {
			driver.switchTo().defaultContent();
			log.info("Successfully switched to default Frame");
		} catch (Exception e) {
			log.error("Failed During Switching to default Action");
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
			// we're only looking for one group, so get it
			theGroup = m.group(1);
			theGroup = theGroup.replaceAll("\\b\\w+(?<!\\w[\\d@]\\b)\\b", "");
			theGroup = theGroup.replaceAll(" ", "");
			// print the group out for verification
			System.out.format(theGroup);
		}
		return theGroup;
	}

	public String copynumber(WebDriver driver, String inputParam1, String inputParam2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {

		String value = null;
		// DH 38

		try {

			if (inputParam1.equalsIgnoreCase("Confirmation") && inputParam2.equalsIgnoreCase("document")) {

				Thread.sleep(5000);

				WebElement webElement = driver.findElement(By.xpath("//div[normalize-space(text())='" + inputParam1

						+ "']/following::*[contains(text(),'" + inputParam2 + "')][1]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(webElement).build().perform();
				String stringToSearch = webElement.getText();
				System.out.println(stringToSearch);
				value = copyValuesWithSpc(stringToSearch);

				// value = copyValuesWithSpc(webElement);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//div[normalize-space(text())='inputParam1']/following::*[contains(text(),'inputParam2')]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				String testParamId = fetchMetadataVO.getTestScriptParamId();
				String testSetId = fetchMetadataVO.getTestSetLineId();
				dynamicnumber.saveCopyNumber(value, testParamId, testSetId);
				log.info("Sucessfully Clicked copynumber" + scripNumber);

				return value;

			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during copynumber" + scripNumber);
			System.out.println(inputParam2);

		}

		// DH 31
		try {
			if (inputParam1.equalsIgnoreCase("Confirmation") && inputParam2.equalsIgnoreCase("Payment")) {
				Thread.sleep(5000);

				WebElement webElement = driver.findElement(By.xpath("//div[normalize-space(text())='" + inputParam1
						+ "']/following::*[contains(text(),'" + inputParam2 + "')][1]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(webElement).build().perform();
				String stringToSearch = webElement.getText();
				System.out.println(stringToSearch);
				value = copyNumbers(stringToSearch);

				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//div[normalize-space(text())='inputParam1']/following::*[contains(text(),'inputParam2')][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				String testParamId = fetchMetadataVO.getTestScriptParamId();
				String testSetId = fetchMetadataVO.getTestSetLineId();
				dynamicnumber.saveCopyNumber(value, testParamId, testSetId);
				log.info("Sucessfully Clicked Totals or Total copynumber" + scripNumber);
				return value;

			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Totals or Total copynumber" + scripNumber);

			System.out.println(inputParam2);

		}

		try {

			if (inputParam1.equalsIgnoreCase("Totals") && inputParam2.equalsIgnoreCase("Total")) {

				Thread.sleep(5000);

				WebElement webElement = driver.findElement(By.xpath("//*[normalize-space(text())='" + inputParam1

						+ "']/following::*[normalize-space(text())='" + inputParam2 + "']/following::span[1]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(webElement).build().perform();
				value = copyNegative(webElement);

				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[normalize-space(text())='inputParam1']/following::*[normalize-space(text())='inputParam2']/following::span[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				String testParamId = fetchMetadataVO.getTestScriptParamId();
				String testSetId = fetchMetadataVO.getTestSetLineId();
				dynamicnumber.saveCopyNumber(value, testParamId, testSetId);
				log.info("Sucessfully Clicked Totals or Total copynumber" + scripNumber);
				return value;

			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Totals or Total copynumber" + scripNumber);

			System.out.println(inputParam2);

		}
		try {

			if (inputParam1.equalsIgnoreCase("Totals") || inputParam2.equalsIgnoreCase("Transaction Number")
					|| inputParam2.equalsIgnoreCase("Batch Number") || inputParam1.equalsIgnoreCase("Overview")) {

				Thread.sleep(5000);

				WebElement webElement = driver.findElement(By.xpath("//*[normalize-space(text())='" + inputParam1

						+ "']/following::*[normalize-space(text())='" + inputParam2 + "']/following::span[1]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(webElement).build().perform();

				value = copyInt(webElement);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[normalize-space(text())='inputParam1']/following::*[normalize-space(text())='inputParam2']/following::span[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				String testParamId = fetchMetadataVO.getTestScriptParamId();
				String testSetId = fetchMetadataVO.getTestSetLineId();
				dynamicnumber.saveCopyNumber(value, testParamId, testSetId);
				log.info("Sucessfully Clicked  copynumber" + scripNumber);

				return value;

			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  copynumber" + scripNumber);

			System.out.println(inputParam2);

		}

		// DH 34
		try {

			if (inputParam1.equalsIgnoreCase("Confirmation") || inputParam2.equalsIgnoreCase("adjustment")
					|| inputParam1.equalsIgnoreCase("Information")) {

				Thread.sleep(5000);

				// WebElement webElement =
				// driver.findElement(By.xpath("//div[normalize-space(text())='" + inputParam1
				//
//								+ "']/following::*[contains(text(),'" + inputParam2 + "')]"));

				WebElement webElement = driver.findElement(By.xpath("//div[normalize-space(text())='" + inputParam1

						+ "']/following::*[contains(text(),'" + inputParam2 + "')][1]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(webElement).build().perform();

				value = copyInt(webElement);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//div[normalize-space(text())='inputParam1']/following::*[contains(text(),'inputParam2')]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				String testParamId = fetchMetadataVO.getTestScriptParamId();
				String testSetId = fetchMetadataVO.getTestSetLineId();
				dynamicnumber.saveCopyNumber(value, testParamId, testSetId);
				log.info("Sucessfully Clicked  copynumber" + scripNumber);

				return value;

			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during copynumber" + scripNumber);
			System.out.println(inputParam2);

		}

		try {

			WebElement webElement = driver.findElement(By.xpath("(//div[contains(@title,'" + inputParam1 + "')])[1]"));

			Actions actions = new Actions(driver);

			actions.moveToElement(webElement).build().perform();

			Thread.sleep(5000);

			if (webElement.isDisplayed() == true) {

				value = copyInt(webElement);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//div[contains(@title,'inputParam1')])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				String testParamId = fetchMetadataVO.getTestScriptParamId();
				String testSetId = fetchMetadataVO.getTestSetLineId();
				dynamicnumber.saveCopyNumber(value, testParamId, testSetId);
				log.info("Sucessfully Clicked copynumber" + scripNumber);

				return value;

			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during copynumber" + scripNumber);

			System.out.println(inputParam1);

		}

		try {
			WebElement webElement = driver.findElement(By.xpath("(//div[contains(text(),'" + inputParam1 + "')])[1]"));

			Actions actions = new Actions(driver);

			actions.moveToElement(webElement).build().perform();

			Thread.sleep(5000);

			if (webElement.isDisplayed() == true) {

				value = copyMethod(webElement, value);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//div[contains(text(),'inputParam1')])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				String testParamId = fetchMetadataVO.getTestScriptParamId();
				String testSetId = fetchMetadataVO.getTestSetLineId();
				dynamicnumber.saveCopyNumber(value, testParamId, testSetId);
				log.info("Sucessfully Clicked copynumber" + scripNumber);

				return value;

			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during copynumber" + scripNumber);

			System.out.println(inputParam1);

		}
		try {

			WebElement webElement = driver
					.findElement(By.xpath("//label[text()='" + inputParam1 + "']/following::td[1]"));

			Actions actions = new Actions(driver);

			actions.moveToElement(webElement).build().perform();

			Thread.sleep(5000);

			if (webElement.isDisplayed() == true) {

				value = copyMethod(webElement, value);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//label[text()='inputParam1']/following::td[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				String testParamId = fetchMetadataVO.getTestScriptParamId();
				String testSetId = fetchMetadataVO.getTestSetLineId();
				dynamicnumber.saveCopyNumber(value, testParamId, testSetId);
				log.info("Sucessfully Clicked copynumber" + scripNumber);

				return value;

			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during copynumber" + scripNumber);

			System.out.println(inputParam1);

		}

		try {
			WebElement webElement = driver.findElement(By.xpath("//img[@title='In Balance ']/following::td[1]"));

			Actions actions = new Actions(driver);

			actions.moveToElement(webElement).build().perform();

			if (webElement.isDisplayed() == true) {

				value = copyMethod(webElement, value);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//img[@title='In Balance ']/following::td[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				String testParamId = fetchMetadataVO.getTestScriptParamId();
				String testSetId = fetchMetadataVO.getTestSetLineId();
				dynamicnumber.saveCopyNumber(value, testParamId, testSetId);
				log.info("Sucessfully Clicked copynumber" + scripNumber);

				return value;

			}

		} catch (Exception e) {

			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during copynumber" + scripNumber);

			screenshotFail(driver, fetchMetadataVO, customerDetails);

			System.out.println(inputParam1);

			throw e;

		}

		return value;

	}

	private String copyMethod(WebElement webElement, String value) {

		String num = null;

		try {

//          System.out.println(value);

			String number = webElement.getText();

			System.out.println(number);

			num = number.replaceAll("[^\\d.]+|\\.(?!\\d)", "");

			System.out.println(num);

			log.info("Successfully Copied the Number");

		} catch (Exception e) {
			log.error("Sucessfully Clicked copynumber");

			System.out.println(e);

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
	 * System.out.println(e);
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
			System.out.println(number);
			num = number.replaceAll("\\b\\w+(?<!\\w[\\d@]\\b)\\b", "");
			num1 = num.replaceAll("[().$#@!*&^\\/\\\\]", "");
			// String num2= num1.trim();
			String num2 = num1.replaceAll("[^a-zA-Z0-9]", "").trim();
			System.out.println(num2);
			Thread.sleep(2000);
			log.info("Successfully Copied the Number");
			return num2;
		} catch (Exception e) {
			System.out.println(e);
		}
		try {

			String number = webElement.getText().toString();

			System.out.println(number);

			num = number.replaceAll("[^\\d.]+|\\.(?!\\d)", "");

			System.out.println(num);

			log.info("Successfully Copied the Number");

		} catch (Exception e) {

			System.out.println(e);

		}

		return num;

	}

	private String copyNegative(WebElement webElement) {

		String num = null;

		try {

//          System.out.println(value);

			String number = webElement.getText().toString();

			System.out.println(number);

			num = number.replaceAll("[^\\-\\,\\d.]+|\\.(?!\\d)", number);
			System.out.println(num);
			log.info("Successfully Copied the Number");

		} catch (Exception e) {

			System.out.println(e);

		}

		return num;

	}

	public String copyy(WebDriver driver, String xpath, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) {
		try {
			WebElement webElement = driver.findElement(By.xpath(xpath));
			String number = webElement.getText();
			System.out.println(number);
			String num = number.replaceAll("[^\\d.]+|\\.(?!\\d)", "");
			StringSelection stringSelection = new StringSelection(num);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked copyy" + scripNumber);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during copyy" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			System.out.println(xpath);
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
					System.out.println(texts.get(0));
					StringSelection stringSelection = new StringSelection(texts.get(0));
					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
				}
			}
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked copytext" + scripNumber);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during copytext" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			System.out.println(xpath);
			throw e;
		}
		return xpath;

	}

	public void maximize(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) {
		try {
			driver.manage().window().maximize();
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked maximize" + scripNumber);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during maximize" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			System.out.println("can not maximize");
			e.printStackTrace();
			throw e;

		}
	}

	public void switchWindow(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) {
		try {
			driver.switchTo().window(Main_Window);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked switchWindow" + scripNumber);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during switchWindow" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			System.out.println("can not switch to window");
			e.printStackTrace();

			throw e;
		}
	}

	public void switchDefaultContent(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) throws Exception {
		try {
			Thread.sleep(8000);
			Set<String> set = driver.getWindowHandles();
			Iterator<String> itr = set.iterator();
			while (itr.hasNext()) {
				String childWindow = itr.next();
				driver.switchTo().window(childWindow);
				System.out.println(driver.switchTo().window(childWindow).getTitle());
				Thread.sleep(4000);
			}
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked switchDefaultContent" + scripNumber);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during switchDefaultContent" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			System.out.println("failed while hadling window");
			e.printStackTrace();
			throw e;
		}
	}

	public void switchParentWindow(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) throws Exception {
		try {
			Thread.sleep(8000);
			Set<String> set = driver.getWindowHandles();
			Iterator<String> itr = set.iterator();
			while (itr.hasNext()) {
				String childWindow = itr.next();
				driver.switchTo().window(childWindow);
				System.out.println(driver.switchTo().window(childWindow).getTitle());
			}
			driver.close();
			Thread.sleep(2000);
			Set<String> set1 = driver.getWindowHandles();
			Iterator<String> itr1 = set1.iterator();
			while (itr1.hasNext()) {
				String childWindow = itr1.next();
				driver.switchTo().window(childWindow);
				System.out.println(driver.switchTo().window(childWindow).getTitle());
			}
			driver.close();
			Set<String> set2 = driver.getWindowHandles();
			Iterator<String> itr2 = set2.iterator();
			while (itr2.hasNext()) {
				String childWindow = itr2.next();
				driver.switchTo().window(childWindow);
				System.out.println(driver.switchTo().window(childWindow).getTitle());
			}
			
			renameDownloadedFile(driver,fetchMetadataVO, fetchConfigVO, customerDetails);

		} catch (Exception e) {
			log.error("Failed to Handle the window");
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			System.out.println("failed while hadling window");
			e.printStackTrace();
			throw e;
		}
	}

	public void switchToParentWindow(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) throws Exception {
		try {
			Thread.sleep(8000);
			Set<String> set = driver.getWindowHandles();
			Iterator<String> itr = set.iterator();
			while (itr.hasNext()) {
				String childWindow = itr.next();
				driver.switchTo().window(childWindow);
				System.out.println(driver.switchTo().window(childWindow).getTitle());
			}
			driver.close();
			Set<String> set1 = driver.getWindowHandles();
			Iterator<String> itr1 = set1.iterator();
			while (itr1.hasNext()) {
				String childWindow = itr1.next();
				driver.switchTo().window(childWindow);
				System.out.println(driver.switchTo().window(childWindow).getTitle());
			}
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked switchToParentWindow" + scripNumber);
			
			renameDownloadedFile(driver,fetchMetadataVO, fetchConfigVO, customerDetails);

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during switchToParentWindow" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			System.out.println("failed while hadling window");
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
			log.info("Successfully Drag and drop the values" + scripNumber);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed During dragAnddrop Action." + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			System.out.println(xpath);
			e.printStackTrace();
			throw e;
		}
	}

	public void windowhandle(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) throws Exception {
		try {
			Thread.sleep(20000);
			String mainWindow = driver.getWindowHandle();
			Set<String> set = driver.getWindowHandles();
			Iterator<String> itr = set.iterator();
			while (itr.hasNext()) {
				String childWindow = itr.next();
				if (!mainWindow.equals(childWindow)) {
					driver.switchTo().window(childWindow);
					System.out.println(driver.switchTo().window(childWindow).getTitle());
					driver.manage().window().maximize();
					Thread.sleep(2000);
					screenshot(driver, fetchMetadataVO, customerDetails);
					driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
					driver.switchTo().window(childWindow);
				}
			}
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Successfully Handeled the window" + scripNumber);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed to Handle the window" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			System.out.println("failed while hadling window");
			e.printStackTrace();
			throw e;
		}
	}

	public void switchToFrame(WebDriver driver, String inputParam, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			Thread.sleep(5000);
			WebElement waittext = driver
					.findElement(By.xpath("//h1[normalize-space(text())='" + inputParam + "']/following::iframe[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			driver.switchTo().frame(waittext);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//h1[normalize-space(text())='inputParam']/following::iframe[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			WebElement waittext = driver.findElement(By.xpath("//iframe[contains(@id,'" + inputParam + "')]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			driver.switchTo().frame(waittext);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//iframe[contains(@id,'inputParam')]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			Thread.sleep(5000);
			WebElement waittext = driver.findElement(By.xpath("//iframe[@title='" + inputParam + "']"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			driver.switchTo().frame(waittext);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//iframe[@title='inputParam']";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			Thread.sleep(10000);
			WebElement waittext = driver
					.findElement(By.xpath("//*[normalize-space(text())='" + inputParam + "']/following::iframe[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			driver.switchTo().frame(waittext);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[normalize-space(text())='inputParam']/following::iframe[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			System.out.println(e);
			log.error("Failed During switchToFrame Action");
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
				System.out.println("Is file Present*** "+file.isFile());
				uploadZip.sendKeys(file.getAbsolutePath());
				log.info("Successfully Uploaded The File");
				screenshot(webDriver, scriptDetailsDto, customerProjectDto);
				return;
			}
		} catch (Exception e) {
			log.error("Failed During uploadFileAutoIT Action.");
			screenshotFail(webDriver, scriptDetailsDto, customerProjectDto);
			log.error(fileLocation);
			e.printStackTrace();
		}
		
		try {
			if(param1.equalsIgnoreCase("Add") && param2.equalsIgnoreCase("File")) {
			String uploadXPath = "//*[text()='"+param1+" "+param2+"']";
			WebElement uploadZip = webDriver.findElement(By.xpath(uploadXPath));
			Thread.sleep(5000);
			File file = new File(fileLocation+param3);
			System.out.println("Is file Present*** "+file.isFile());
			uploadZip.sendKeys(file.getAbsolutePath());
			log.info("Successfully Uploaded The File");
			screenshot(webDriver, scriptDetailsDto, customerProjectDto);
			return;
			}
		} catch (Exception e) {
			log.error("Failed During uploadFileAutoIT Action.");
			screenshotFail(webDriver, scriptDetailsDto, customerProjectDto);
			log.error(fileLocation);
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
			logger.error(fileLocation);
			e.printStackTrace();
		}
		
	}

	public void refreshPage(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) {
		try {
			driver.navigate().refresh();
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked refreshPage" + scripNumber);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during refreshPage" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			System.out.println("can not refresh the page");
			e.printStackTrace();
			throw e;

		}
	}

	public String getErrorMessages(WebDriver driver) {
		try {
			String text = driver.findElement(By.xpath("//td[@class='AFNoteWindow']")).getText();
			return text;
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			String text = driver.findElement(By.xpath("//div[contains(@class,'Error')]")).getText();
			return text;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	@Override
	public void actionApprove(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void multipleSendKeys(WebDriver driver, String param1, String param2, String value1, String value2,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {
		try {
			if (param1.equalsIgnoreCase("Requisition Lines")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittill = driver.findElement(By.xpath(
						"//*[text()='" + param1 + "']/following::*[text()='" + value1 + "']/following::input[1]"));
				Thread.sleep(1000);
				// wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[text()='"+param1+"']/following::*[text()='"+value1+"']/following::input[1]"),
				// value1));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, value2, waittill, fetchConfigVO, fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
			log.error("Failed during Click action.");
			screenshotFail(driver, fetchMetadataVO, customerDetails);
		}
	}

	private String copyValuesWithSpc(String value) {
		try {
			Pattern p = Pattern.compile("\\b[A-Z]+\\-[A-Z]+\\-\\d+"); // the pattern to search for
																		// \b[IBAA]+\-[A-Z]+\-\d+
																		// (\\b[Payment]+\\s[\\d]+)
			Matcher m = p.matcher(value);
			// if we find a match, get the group
			String theGroup = null;
			if (m.find()) {
				// we're only looking for one group, so get it
				theGroup = m.group(0);
				System.out.println(theGroup);
				// theGroup = theGroup.replaceAll("\\b\\w+(?<!\\w[\\d@]\\b)\\b", "");
				// System.out.println(theGroup);
				theGroup = theGroup.replaceAll(" ", "");
				System.out.format(theGroup);
			}
			return theGroup;
		} catch (Exception e) {
			System.out.println(e);
			throw e;
		}

	}

	public void oicLogout(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO, String type1,
			String type2, String type3, String param1, String param2, String param3, CustomerProjectDto customerDetails)
			throws Exception {

		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(
					ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@class,'opaas-user__icon')]")));
			WebElement waittext = driver.findElement(By.xpath("//*[contains(@class,'opaas-user__icon')]"));
			waittext.click();
			Thread.sleep(4000);
			screenshot(driver, fetchMetadataVO, customerDetails);
			WebElement signout = driver.findElement(By.xpath("//*[text()='Sign Out']"));
			signout.click();
			return;
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed to logout " + scripNumber);
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

	public String oicLoginPage(WebDriver driver, String param1, String keysToSend, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {
		String xpath = null;
		try {
			if (param1.equalsIgnoreCase("Password")) {
				String title1 = driver.getTitle();
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("//*[contains(@placeholder,'" + param1 + "')]")));
				JavascriptExecutor jse = (JavascriptExecutor) driver;
				jse.executeScript("document.getElementById('password').value = '" + keysToSend + "';");
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
				// screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Succesfully password is entered " + scripNumber);
				xpath = "//*[contains(@placeholder,'param1')]";
				return xpath;
			}
		} catch (Exception e) {
			screenshotFail(driver, fetchMetadataVO, customerDetails);

			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed to enter password " + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("//*[contains(@placeholder,'" + param1 + "')]")));
			WebElement waittill = driver.findElement(By.xpath("//*[contains(@placeholder,'" + param1 + "')]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("arguments[0].value='" + keysToSend + "';", waittill);
			// if("password".equalsIgnoreCase(param1))
			// screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(1000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			xpath = "//*[contains(@placeholder,'param1')]";
			log.info("Successfully entered User Name " + scripNumber);
			return xpath;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Failed during login page " + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			System.out.println("Failed During Login page");
		}
		return xpath;
	}

	public synchronized void oicNavigate(WebDriver driver, FetchConfigVO fetchConfigVO,
			ScriptDetailsDto fetchMetadataVO, String type1, String type2, String param1, String param2, int count,
			CustomerProjectDto customerDetails) throws Exception {
		String param3 = "Show / Hide Navigation menu";

		String xpath = oicNavigator(driver, param3, fetchMetadataVO, fetchConfigVO, customerDetails);
		String xpath1 = oicMenuNavigation(driver, param1, fetchMetadataVO, fetchConfigVO, customerDetails);
		String xpath2 = menuNavigationButton(driver, fetchMetadataVO, fetchConfigVO, type1, type2, param1, param2,
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
					By.xpath("//*[@title='" + param1 + "']//*[contains(@class,'oj-start')]")));
			wait.until(ExpectedConditions
					.elementToBeClickable(By.xpath("//*[@title='" + param1 + "']//*[contains(@class,'oj-start')]")));
			WebElement waittext = driver
					.findElement(By.xpath("//*[@title='" + param1 + "']//*[contains(@class,'oj-start')]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			actions.moveToElement(waittext).click().build().perform();
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Successfully navigator is done " + scripNumber);
			String xpath = "//*[@title='Show / Hide Navigation menu']//*[contains(@class,'oj-start')]";
			return xpath;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during navigator " + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			System.out.println("Not able to navitage to the Url");
			throw e;
		}
	}

	public String oicMenuNavigation(WebDriver driver, String param1, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			Thread.sleep(5000);
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("(//div[@class='navlist-container']//span[text()='" + param1 + "'])[1]")));
			wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("(//div[@class='navlist-container']//span[text()='" + param1 + "'])[1]")));
			WebElement waittext = driver
					.findElement(By.xpath("(//div[@class='navlist-container']//span[text()='" + param1 + "'])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			actions.moveToElement(waittext).click().build().perform();
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Successfully menunavigation is clicked " + scripNumber);
			String xpath = "(//div[@class='navlist-container']//span[text()='" + param1 + "'])[1]";
			log.info("Successfully menunavigation is clicked " + scripNumber);
			return xpath;

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Menunavigation " + scripNumber);

			screenshotFail(driver, fetchMetadataVO, customerDetails);
			System.out.println("Not able to navitage to the :" + "" + param1);
			throw e;
		}
	}

	public String oicMenuNavigationButton(WebDriver driver, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, String type1, String type2, String param1, String param2, int count,
			CustomerProjectDto customerDetails) throws Exception {
		String xpath = null;
		try {
			Thread.sleep(5000);
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("(//div[@class='navlist-container']//span[text()='" + param2 + "'])[2]")));
			wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("(//div[@class='navlist-container']//span[text()='" + param2 + "'])[2]")));
			WebElement waittext = driver
					.findElement(By.xpath("(//div[@class='navlist-container']//span[text()='" + param2 + "'])[2]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			actions.moveToElement(waittext).click().build().perform();
			screenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Successfully menuNavigationButton is done " + scripNumber);
			xpath = "(//div[@class='navlist-container']//span[text()='" + param2 + "'])[2]";
			log.info("Successfully menuNavigationButton is done " + scripNumber);
			return xpath;

		} catch (Exception e) {
			System.out.println("Count value exceeds the limit");
			log.error("Failed During Navigation");
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			System.out.println("Not able to navitage to the :" + "" + param1);
			throw e;
		}

	}

	public void oicClickButton(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			if (param1.equalsIgnoreCase("Run")) {
				WebElement waittext = driver.findElement(By.xpath("(//*[@title='Run'])[2]"));// screenshot(driver,
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				screenshot(driver, fetchMetadataVO, customerDetails);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(15000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath = "(//*[@title='Run'])[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param1.equalsIgnoreCase("Submit Now")) {
				WebElement waittext = driver.findElement(By.xpath("//a[text()='Submit Now'][1]"));// screenshot(driver,
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				screenshot(driver, fetchMetadataVO, customerDetails);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(15000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath = "//a[text()='Submit Now'][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickButton" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			WebElement waittext = driver.findElement(By.xpath(("//*[contains(@class,'cross-icon')][1]")));// screenshot(driver,
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			screenshot(driver, fetchMetadataVO, customerDetails);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(15000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
			String xpath = "//*[contains(@class,'cross-icon')][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickButton" + scripNumber);
			System.out.println(e);
		}
		try {
			if (!param2.equalsIgnoreCase("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittext = driver.findElement(
						By.xpath(("//*[text()='" + param1 + "']/following::span[text()='" + param2 + "']")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				screenshot(driver, fetchMetadataVO, customerDetails);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(15000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath = "//*[text()='param1']/following::span[text()='param2']";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickButton" + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			WebElement waittext = driver.findElement(By.xpath(("//*[@class='opaas-toolbar__search-icon'][1]")));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			screenshot(driver, fetchMetadataVO, customerDetails);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(15000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
			String xpath = "//*[@class='opaas-toolbar__search-icon'][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickButton" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public String oicSendValue(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {

		try {

			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@placeholder='" + param1 + "']")));
			WebElement waittill = driver.findElement(By.xpath("//input[@placeholder='" + param1 + "']"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			screenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked Close Date sendValue" + scripNumber);
			String xpath = "//input[@placeholder='param1']";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return keysToSend;

		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during sendValue" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void oicMouseHover(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {

		try {

			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()='" + param1 + "']")));

			WebElement waittill = driver.findElement(By.xpath("//*[text()='" + param1 + "']"));

			Actions actions = new Actions(driver);

			actions.moveToElement(waittill).perform();

		} catch (Exception e) {

			System.out.println(e);

			String scripNumber = fetchMetadataVO.getScriptNumber();

			log.error("Failed during Mouse movement" + scripNumber);

			screenshotFail(driver, fetchMetadataVO, customerDetails);

			throw e;

		}

	}

	public void navigateOICUrl(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			CustomerProjectDto customerDetails) {
		try {
			driver.navigate().to(fetchConfigVO.getOIC_APPLICATION_URL());
			driver.manage().window().maximize();
			deleteAllCookies(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			refreshPage(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			switchToActiveElement(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed to logout " + scripNumber);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("failed to do navigate URl " + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			System.out.println("Not able to navitage to the Url");
		}
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
			String sheetName, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) throws Exception {
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

	@Override
	public void oicClickMenu(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void loginOicJob(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			String type1, String type2, String type3, String param1, String param2, String param3, String keysToSend,
			String value, CustomerProjectDto customerDetails) throws Exception {
		// TODO Auto-generated method stub

	}

	public void loginInformaticaApplication(WebDriver driver, FetchConfigVO fetchConfigVO,
			ScriptDetailsDto fetchMetadataVO, String type1, String type2, String type3, String param1, String param2,
			String param3, String keysToSend, String value, CustomerProjectDto customerDetails) throws Exception {
		String param4 = "User name or email";
		String param5 = "password";
		// String param6 = "Sign In";
		navigateInformaticaUrl(driver, fetchConfigVO, fetchMetadataVO, customerDetails);
		String xpath1 = InformaticaLoginPage(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO,
				customerDetails);
		String xpath2 = this.InformaticaLoginPage(driver, param5, value, fetchMetadataVO, fetchConfigVO,
				customerDetails);
		if (xpath2.equalsIgnoreCase(null)) {
			throw new IOException("Failed during login page");
		}
		String scripNumber = fetchMetadataVO.getScriptNumber();
		String xpath = xpath1 + ";" + xpath2;
		String scriptID = fetchMetadataVO.getScriptId();
		String lineNumber = fetchMetadataVO.getLineNumber();
		service.saveXpathParams(scriptID, lineNumber, xpath);
	}

	public void navigateInformaticaUrl(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			CustomerProjectDto customerDetails) {
		try {
			driver.navigate().to(fetchConfigVO.getINFORMATICA_APPLICATION_URL());
			driver.manage().window().maximize();
			Thread.sleep(4000);

			// deleteAllCookies(driver, fetchMetadataVO, fetchConfigVO);
			// refreshPage(driver, fetchMetadataVO, fetchConfigVO);
			switchToActiveElement(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(10000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed to logout " + scripNumber);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("failed to do navigate URl " + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			System.out.println("Not able to navitage to the Url");
		}
	}

	public String InformaticaLoginPage(WebDriver driver, String param1, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {
		String xpath = null;
		try {
			if (param1.equalsIgnoreCase("Password")) {
				String title1 = driver.getTitle();
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='" + param1 + "']/input")));

				WebElement waittill = driver.findElement(By.xpath("//*[@id='" + param1 + "']/input"));

				// JavascriptExecutor jse = (JavascriptExecutor) driver;
				// jse.executeScript("document.getElementById('idcs-signin-basic-signin-form-password|input').value
				// = '" + keysToSend + "';");
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
				// screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Succesfully password is entered " + scripNumber);
				xpath = "//*[contains(@placeholder,'param1')]";
				return xpath;
			}
		} catch (Exception e) {
			screenshotFail(driver, fetchMetadataVO, customerDetails);

			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed to enter password " + scripNumber);
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='" + param1 + "']/input")));
			WebElement waittill = driver.findElement(By.xpath("//*[@id='" + param1 + "']/input"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			waittill.sendKeys(keysToSend);

			// typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO,
			// fetchMetadataVO);
			// JavascriptExecutor jse = (JavascriptExecutor) driver;
			// jse.executeScript("arguments[0].value='" + keysToSend + "';", waittill);
			// if("password".equalsIgnoreCase(param1))
			// screenshot(driver, "", fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(1000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			xpath = "//*[contains(@placeholder,'param1')]";
			log.info("Successfully entered User Name " + scripNumber);
			return xpath;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Failed during login page " + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			System.out.println("Failed During Login page");
		}
		return xpath;
	}

	public void InformaticaClickButton(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {

		try {

			if (param1.equalsIgnoreCase("Explore")) {
				Thread.sleep(3000);
				Actions action = new Actions(driver);
				// WebElement we = driver.findElement(By.xpath("(//*[text()='Scheduled
				// Orchestration']/following::*[@title='Run'])[1]"));
				WebElement we = driver.findElement(By.xpath("(//*[text()='" + param1 + "'])[1]"));
				action.moveToElement(we).perform();
				Thread.sleep(5000);
				WebElement run = driver.findElement(By.xpath("(//*[text()='" + param1 + "'])[1]"));
				run.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath = "(//*[text()='Scheduled Orchestration']/following::*[@title='Run'])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickButton" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			// throw e;
		}

		try {

			if (param1.equalsIgnoreCase("My Jobs")) {
				Thread.sleep(3000);
				Actions action = new Actions(driver);
				// WebElement we = driver.findElement(By.xpath("(//*[text()='Scheduled
				// Orchestration']/following::*[@title='Run'])[1]"));
				WebElement we = driver.findElement(By.xpath("(//*[text()='" + param1 + "'])[1]"));
				action.moveToElement(we).perform();
				Thread.sleep(5000);
				WebElement run = driver.findElement(By.xpath("(//*[text()='" + param1 + "'])[1]"));
				run.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath = "(//*[text()='Scheduled Orchestration']/following::*[@title='Run'])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickButton" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			// throw e;
		}

		try {

			if (param1.equalsIgnoreCase("Run")) {
				Thread.sleep(3000);
				Actions action = new Actions(driver);
				// WebElement we = driver.findElement(By.xpath("(//*[text()='Scheduled
				// Orchestration']/following::*[@title='Run'])[1]"));
				WebElement we = driver.findElement(By.xpath("//span[text()='" + param1 + "']"));
				action.moveToElement(we).perform();
				Thread.sleep(5000);
				WebElement run = driver.findElement(By.xpath("//span[text()='" + param1 + "']"));
				run.click();
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
				String xpath = "(//*[text()='Scheduled Orchestration']/following::*[@title='Run'])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickButton" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			// throw e;
		}

		try {

			Thread.sleep(3000);
			Actions action = new Actions(driver);
			// WebElement we = driver.findElement(By.xpath("(//*[text()='Scheduled
			// Orchestration']/following::*[@title='Run'])[1]"));
			WebElement we = driver.findElement(By.xpath("//*[text()='" + param1 + "']"));
			action.moveToElement(we).perform();
			Thread.sleep(5000);
			WebElement run = driver.findElement(By.xpath("//*[text()='" + param1 + "']"));
			run.click();
			screenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(5000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Sucessfully Clicked Save and Close clickButton" + scripNumber);
			String xpath = "(//*[text()='Scheduled Orchestration']/following::*[@title='Run'])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during clickButton" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public String InformaticaSendValue(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {
		try {
			if (param1.equalsIgnoreCase("Find")) {
				Thread.sleep(1000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("(//input[@placeholder='" + param1 + "'])[2]")));
				WebElement waittill = driver.findElement(By.xpath("(//input[@placeholder='" + param1 + "'])[2]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				waittill.sendKeys(keysToSend);
				// typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO,
				// fetchMetadataVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked Close Date sendValue" + scripNumber);
				String xpath = "//*[text()='param1']//following::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return keysToSend;
			}
			return null;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during Close Date sendValue" + scripNumber);
			System.out.println(e);
			throw e;
		}

	}

	public void InformaticaclickLink(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {

			if (param1.equalsIgnoreCase("")) {

				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				wait.until(ExpectedConditions

						.presenceOfElementLocated(By.xpath("//a[contains(@id,'" + param1 + "')]")));

				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1 + "']"));

				Actions actions = new Actions(driver);

				actions.moveToElement(waittext).build().perform();

				waittext.click();

				screenshot(driver, fetchMetadataVO, customerDetails);

				refreshPage(driver, fetchMetadataVO, fetchConfigVO, customerDetails);

				Thread.sleep(5000);

				String scripNumber = fetchMetadataVO.getScriptNumber();

				log.info("Sucessfully Clicked Approve clickLink" + scripNumber);

				String xpath = "//a[contains(@id,'param1')]";

				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;

			}

		} catch (Exception e) {

			System.out.println(e);

			String scripNumber = fetchMetadataVO.getScriptNumber();

			log.error("Failed during Approve clickLink" + scripNumber);

		}

	}

	public void InformaticaSelectAValue(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {
		try {
			if (param1.equalsIgnoreCase("All Projects")) {
				Thread.sleep(4000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("(//*[text()='" + param1 + "'])[1]/following::*[text()='" + keysToSend + "']")));
				WebElement waittext = driver.findElement(
						By.xpath("(//*[text()='" + param1 + "'])[1]/following::*[text()='" + keysToSend + "']"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				Thread.sleep(2000);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Sucessfully Clicked selectAValue" + scripNumber);
				String xpath = "//*[contains(@data-afr-popupid,'param1')]//*[contains(normalize-space(text()),'keysToSend')][1]";

				return;
			}
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during selectAValue" + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}

	}

	public void InformaticaClickImage(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			if (param1.equalsIgnoreCase("Refresh")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@title='" + param1 + "']")));
				WebElement waittext = driver.findElement(By.xpath("//button[@title='" + param1 + "']"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// waittext.click();
				// highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(45000);
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
			throw e;
		}

	}

	public void InformaticaLogout(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			String type1, String type2, String type3, String param1, String param2, String param3,
			CustomerProjectDto customerDetails) throws Exception {

		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@title='User']")));
			WebElement waittext = driver.findElement(By.xpath("//button[@title='User']"));
			waittext.click();
			Thread.sleep(4000);
			screenshot(driver, fetchMetadataVO, customerDetails);
			WebElement signout = driver.findElement(By.xpath("(//*[text()='Log Out'])[3]"));
			signout.click();
			return;
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed to logout " + scripNumber);
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}

	}

	@Override
	public void loginSSOApplication(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			String type1, String type2, String type3, String param1, String param2, String param3, String keysToSend,
			String value, CustomerProjectDto customerDetails) throws Exception {
	}

	@Override
	public void waitTillLoad(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO) {
		try {
			Thread.sleep(Integer.parseInt(fetchMetadataVO.getInputValue()));
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.error(e);
			Thread.currentThread().interrupt();
		}

	}

	@Override
	public String uploadObjectToObjectStore(String sourceFilePath, String destinationFilePath) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void compareValue(WebDriver driver, String input_parameter, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, String globalValueForSteps2, CustomerProjectDto customerDetails)
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
	public String loginToExcel(WebDriver driver, String param1, String param2, String username, String password,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void apiAccessToken(ScriptDetailsDto fetchMetadataVO, Map<String, String> accessTokenStorage,
			CustomerProjectDto customerDetails) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void createDriverFailedPdf(List<ScriptDetailsDto> fetchMetadataListVO, FetchConfigVO fetchConfigVO,
			String pdffileName, ApiValidationVO api, boolean validationFlag, CustomerProjectDto customerDetails)
			throws IOException, com.itextpdf.text.DocumentException, DocumentException {
		// TODO Auto-generated method stub
		
	}
	public void loginSFApplication(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			String type1, String type2, String type3, String param1, String param2, String param3, String keysToSend,
			String value, CustomerProjectDto customerDetails) throws Exception {
		
	}

}
