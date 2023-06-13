package com.winfo.scripts;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.assertthat.selenium_shutterbug.core.Capture;
import com.assertthat.selenium_shutterbug.core.Shutterbug;
import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.objectstorage.responses.PutObjectResponse;
import com.winfo.exception.WatsEBSException;
import com.winfo.serviceImpl.DynamicRequisitionNumber;
import com.winfo.serviceImpl.ScriptXpathService;
import com.winfo.vo.CustomerProjectDto;
import com.winfo.vo.FetchConfigVO;
import com.winfo.vo.ScriptDetailsDto;

@Service
public class XpathPerformance {

	public static final Logger logger = Logger.getLogger(XpathPerformance.class);
	
	private static final String SCREENSHOT = "Screenshot";
	public static final String FORWARD_SLASH = "/";
	private static final String PNG_EXTENSION = ".png";
	@Value("${oci.config.path}")
	private String ociConfigPath;
	@Value("${oci.config.name}")
	private String ociConfigName;
	@Value("${oci.bucket.name}")
	private String ociBucketName;
	@Value("${oci.namespace}")
	private String ociNamespace;
	@Autowired
	ScriptXpathService service;
	@Autowired
	DynamicRequisitionNumber dynamicnumber;

		public void sendValue(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, int count,
			CustomerProjectDto customerDetails) throws Exception {
//         int count=0;
		String action = fetchMetadataVO.getAction();
		String scriptID = fetchMetadataVO.getScriptId();
		String testSetLine=fetchMetadataVO.getTestSetLineId();

		String lineNumber = fetchMetadataVO.getLineNumber();
//		String xpathlocation = service.getXpathParams(scriptID, lineNumber);
		String xpathlocation = service.getXpathParams(scriptID, lineNumber,testSetLine);

		if (xpathlocation != null) {
			String param1r = xpathlocation.replace("param1", param1);
			String paramsr = param1r.replace("param2", param2);
			try {
//				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
//				wait.until(ExpectedConditions.presenceOfElementLocated(
//						By.xpath(paramsr)));
				WebElement waittill = driver.findElement(By.xpath(paramsr));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				logger.info("Successfully Executed send Value "  + fetchMetadataVO.getScriptNumber());

				return;
			} catch (Exception e) {

				if (count == 0) {
					Thread.sleep(2000);
					count = 1;
					logger.error(" The Count Value is : " + count);
					sendValue(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, count,
							customerDetails);

				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					logger.error(" The Count Value is : " + count);
					sendValue(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, count,
							customerDetails);
				} else {
					logger.error("Count value exceeds the limit " + count);
					logger.error("Failed During SendValue " + fetchMetadataVO.getScriptNumber());
					fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
					throw e;
				}
			}

		} else {
			throw new Exception("XpathLocation is null");
		}
	}

	public void typeIntoValidxpath(WebDriver driver, String keysToSend, WebElement waittill ,
			FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO) {
		try {
			waittill.clear();
			waittill.click();
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("arguments[0].value='" + keysToSend + "';", waittill);
			logger.info("clear and typed the given Data");
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked typeIntoValidxpath" + scripNumber);

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  typeIntoValidxpath" + scripNumber);
			e.printStackTrace();
		}
	}

	public void clickButton(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, int count, CustomerProjectDto customerDetails) throws Exception {
//		int count=0;
		String scriptID = fetchMetadataVO.getScriptId();
		String action = fetchMetadataVO.getAction();
		String lineNumber = fetchMetadataVO.getLineNumber();
		String testSetLine=fetchMetadataVO.getTestSetLineId();
//		String xpathlocation = service.getXpathParams(scriptID, lineNumber);
		String xpathlocation = service.getXpathParams(scriptID, lineNumber,testSetLine);
		if (xpathlocation != null) {
			String param1r = xpathlocation.replace("param1", param1);
			String paramsr = param1r.replace("param2", param2);
			try {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath((paramsr))));
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				WebElement waittext = driver.findElement(By.xpath((paramsr)));
				Thread.sleep(6000);
				if (!waittext.isEnabled()) {
					Thread.sleep(15000);
				}
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				if (param1.equalsIgnoreCase("Approval")) {
					waittext.click();
				} else {
					clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				}
//			if(ExpectedConditions.elementToBeClickable(By.xpath((paramsr))) != null) {
//				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
//			}
				Thread.sleep(2000);
				logger.info("Successfully clicked clickButton " + fetchMetadataVO.getScriptNumber());

			} catch (Exception e) {

				if (count == 0) {
					count = 1;
					logger.error(" The Count Value is : " + count);
					clickButton(driver, param1, param2, fetchMetadataVO, fetchConfigVO, count, customerDetails);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					logger.error(" The Count Value is : " + count);
					clickButton(driver, param1, param2, fetchMetadataVO, fetchConfigVO, count, customerDetails);
				} else {
					logger.error("Count value exceeds the limit " + count);
					logger.error("Failed During Click Button " + fetchMetadataVO.getScriptNumber());
					fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
					throw e;
				}
			}

		} else {
			throw new Exception("XpathLocation is null");
		}
	}

	
	public String textarea(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, int count,CustomerProjectDto customerDetails) throws Exception {

//		int count=0;
		String scriptID = fetchMetadataVO.getScriptId();
		String action = fetchMetadataVO.getAction();
		String lineNumber = fetchMetadataVO.getLineNumber();
		String testSetLine=fetchMetadataVO.getTestSetLineId();
//		String xpathlocation = service.getXpathParams(scriptID, lineNumber);
		String xpathlocation = service.getXpathParams(scriptID, lineNumber,testSetLine);
		if (xpathlocation != null) {
			String param1r = xpathlocation.replace("param1", param1);
			String paramsr = param1r.replace("param2", param2);
			try {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(paramsr)));
				WebElement waittill = driver.findElement(By.xpath(paramsr));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				Thread.sleep(1000);
				logger.info("Successfully Entered value in the  Text Area " + fetchMetadataVO.getScriptNumber());
				return keysToSend;
			} catch (Exception e) {
				if (count == 0) {
					count = 1;
					logger.error(" The Count Value is : " + count);
					textarea(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, count,customerDetails);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					logger.error(" The Count Value is : " + count);
					textarea(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, count,customerDetails);
				} else {
					logger.error("Count value exceeds the limit " + count);
					logger.error("Failed During textarea " + fetchMetadataVO.getScriptNumber() );
					fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
					throw e;
				}
			}
		} else {
			throw new Exception("XpathLocation is null");
		}
		return keysToSend;
	}

	public void tableSendKeys(WebDriver driver, String param1, String param2, String param3, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, int count,CustomerProjectDto customerDetails) throws Exception {
//	         int count=0;
		String scriptID = fetchMetadataVO.getScriptId();
		String action = fetchMetadataVO.getAction();
		String lineNumber = fetchMetadataVO.getLineNumber();
		String testSetLine=fetchMetadataVO.getTestSetLineId();
//		String xpathlocation = service.getXpathParams(scriptID, lineNumber);
		String xpathlocation = service.getXpathParams(scriptID, lineNumber,testSetLine);
		if (xpathlocation != null) {
			String param1r = xpathlocation.replace("param1", param1);
			String paramsr = param1r.replace("param2", param2);
			try {
//					WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
//					wait.until(ExpectedConditions.presenceOfElementLocated(
//							By.xpath(paramsr)));
				WebElement waittill = driver.findElement(By.xpath(paramsr));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				logger.info("Succesfully Entered TableSendkeys " + fetchMetadataVO.getScriptNumber());

				return;
			} catch (Exception e) {
				if (count == 0) {
					count = 1;
					logger.error(" The Count Value is : " + count);
					tableSendKeys(driver, param1, param2, param3, keysToSend, fetchMetadataVO, fetchConfigVO, count,customerDetails);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					logger.error(" The Count Value is : " + count);
					tableSendKeys(driver, param1, param2, param3, keysToSend, fetchMetadataVO, fetchConfigVO, count,customerDetails);
				} else {
					logger.error("Count value exceeds the limit " + count);
					logger.error("Failed During Table Send Keys " + fetchMetadataVO.getScriptNumber());
					fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
					throw e;
				}
			}
		} else {
			throw new Exception("XpathLocation is null");
		}
		return;
	}

	public void multiplelinestableSendKeys(WebDriver driver, String param1, String param2, String param3,
			String keysToSend, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, int count,
			CustomerProjectDto customerDetails) throws Exception {
//        int count=0;
		String scriptID = fetchMetadataVO.getScriptId();
		String action = fetchMetadataVO.getAction();
		String lineNumber = fetchMetadataVO.getLineNumber();
		String testSetLine=fetchMetadataVO.getTestSetLineId();
//		String xpathlocation = service.getXpathParams(scriptID, lineNumber);
		String xpathlocation = service.getXpathParams(scriptID, lineNumber,testSetLine);
		if (xpathlocation != null) {
			String param1r = xpathlocation.replace("param1", param1);
			String paramsr = param1r.replace("param2", param2);
			try {
//				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
//				wait.until(ExpectedConditions.presenceOfElementLocated(
//						By.xpath(paramsr)));
				WebElement waittill = driver.findElement(By.xpath(paramsr));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				logger.info("Successfully sent multiTableSendkeys " + fetchMetadataVO.getScriptNumber());

				return;
			} catch (Exception e) {
				if (count == 0) {
					count = 1;
					logger.error(" The Count Value is : " + count);
					multiplelinestableSendKeys(driver, param1, param2, param3, keysToSend, fetchMetadataVO,
							fetchConfigVO, count, customerDetails);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					logger.error(" The Count Value is : " + count);
					multiplelinestableSendKeys(driver, param1, param2, param3, keysToSend, fetchMetadataVO,
							fetchConfigVO, count, customerDetails);
				} else {
					logger.error("Count value exceeds the limit " + count);
					logger.error("Failed During multiTableSendkeys " + fetchMetadataVO.getScriptNumber());
					fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
					throw e;
				}
			}
		} else {
			throw new Exception("XpathLocation is null");
		}
		return;
	}

	public void clickLinkAction(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, int count,
			CustomerProjectDto customerDetails) throws Exception {
//		int count=0;
		String scriptID = fetchMetadataVO.getScriptId();
		String action = fetchMetadataVO.getAction();
		String lineNumber = fetchMetadataVO.getLineNumber();
		String testSetLine=fetchMetadataVO.getTestSetLineId();
//		String xpathlocation = service.getXpathParams(scriptID, lineNumber);
		String xpathlocation = service.getXpathParams(scriptID, lineNumber,testSetLine);
		if (xpathlocation != null) {
			String param1r = xpathlocation.replace("param1", param1);
			String paramsr = param1r.replace("param2", param2);
			try {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath((paramsr))));
				WebElement waittext = driver.findElement(By.xpath((paramsr)));
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				logger.info("Successfully clicked clickLinkAction " + fetchMetadataVO.getScriptNumber());

			} catch (Exception e) {

				if (count == 0) {
					count = 1;
					logger.error(" The Count Value is : " + count);
					clickLinkAction(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, count,
							customerDetails);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					logger.error(" The Count Value is : " + count);
					clickLinkAction(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, count,
							customerDetails);
				} else {
					logger.error("Count value exceeds the limit " + count);
					logger.error("Failed During clickLinkAction " + fetchMetadataVO.getScriptNumber());
					fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
					throw e;
				}
			}

		} else {
			throw new Exception("XpathLocation is null");
		}
	}

	public void clickTableLink(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, int count, CustomerProjectDto customerDetails) throws Exception {

//		int count=0;
		String scriptID = fetchMetadataVO.getScriptId();
		String action = fetchMetadataVO.getAction();
		String lineNumber = fetchMetadataVO.getLineNumber();
//		String xpathlocation = service.getXpathParams(scriptID, lineNumber);
		String testSetLine=fetchMetadataVO.getTestSetLineId();
		String xpathlocation = service.getXpathParams(scriptID, lineNumber,testSetLine);
		if (xpathlocation != null) {
			String param1r = xpathlocation.replace("param1", param1);
			String paramsr = param1r.replace("param2", param2);
			try {
//			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
//			wait.until(ExpectedConditions
//					.presenceOfElementLocated(By.xpath((paramsr))));
				WebElement waittext = driver.findElement(By.xpath((paramsr)));
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				actions.click(waittext).build().perform();
				logger.info("Successfully clicked clickTablelink " + fetchMetadataVO.getScriptNumber());

			} catch (Exception e) {

				if (count == 0) {
					count = 1;
					logger.error(" The Count Value is : " + count);
					clickTableLink(driver, param1, param2, fetchMetadataVO, fetchConfigVO, count, customerDetails);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					logger.error(" The Count Value is : " + count);
					clickTableLink(driver, param1, param2, fetchMetadataVO, fetchConfigVO, count, customerDetails);
				} else {
					logger.error("Count value exceeds the limit " + count);
					logger.error("Failed During SendValue " + fetchMetadataVO.getScriptNumber());
					fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
					throw e;
				}
			}

		} else {
			throw new Exception("XpathLocation is null");
		}
	}

	public void clickLink(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, int count, CustomerProjectDto customerDetails) throws Exception {
//		int count=0;
		String scriptID = fetchMetadataVO.getScriptId();
		String action = fetchMetadataVO.getAction();
		String lineNumber = fetchMetadataVO.getLineNumber();
		String testSetLine=fetchMetadataVO.getTestSetLineId();
		String xpathlocation = service.getXpathParams(scriptID, lineNumber,testSetLine);
		logger.info("xpathlocation is" + xpathlocation);
		if (xpathlocation != null) {
			String param1r = xpathlocation.replace("param1", param1);
			String paramsr = param1r.replace("param2", param2);
			try {
//			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
//			wait.until(ExpectedConditions
//					.presenceOfElementLocated(By.xpath((paramsr))));
				WebElement waittext = driver.findElement(By.xpath((paramsr)));
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
//			refreshPage(driver, fetchMetadataVO, fetchConfigVO);
				logger.info("Successfully clicked  ClickLink " + fetchMetadataVO.getScriptNumber());

			} catch (Exception e) {
				if (count == 0) {
					count = 1;
					logger.error(" The Count Value is : " + count);
					clickLink(driver, param1, param2, fetchMetadataVO, fetchConfigVO, count, customerDetails);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					logger.error(" The Count Value is : " + count);
					clickLink(driver, param1, param2, fetchMetadataVO, fetchConfigVO, count, customerDetails);
				} else {
					logger.error("Count value exceeds the limit " + count);
					logger.error("Failed During Click Link " + fetchMetadataVO.getScriptNumber());
					fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
					throw e;
				}
			}

		} else {
			throw new Exception("XpathLocation is null");
		}

	}

	public void refreshPage(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) {
		try {
			driver.navigate().refresh();
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked refreshPage" + scripNumber);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during refreshPage" + scripNumber);
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			e.printStackTrace();
			throw e;

		}
	}

	public void clickImage(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, int count, CustomerProjectDto customerDetails) throws Exception {
//		int count=0;
		String scriptID = fetchMetadataVO.getScriptId();
		String action = fetchMetadataVO.getAction();
		String lineNumber = fetchMetadataVO.getLineNumber();
//		String xpathlocation = service.getXpathParams(scriptID, lineNumber);
		String testSetLine=fetchMetadataVO.getTestSetLineId();
		String xpathlocation = service.getXpathParams(scriptID, lineNumber,testSetLine);
		if (xpathlocation != null) {
			String param1r = xpathlocation.replace("param1", param1);
			String paramsr = param1r.replace("param2", param2);
			try {
//			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
//			wait.until(ExpectedConditions
//					.presenceOfElementLocated(By.xpath((paramsr))));
				WebElement waittext = driver.findElement(By.xpath((paramsr)));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
//				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				logger.info("Successfully clicked  clickImage " + fetchMetadataVO.getScriptNumber());

			} catch (Exception e) {

				if (count == 0) {
					count = 1;
					logger.error(" The Count Value is : " + count);
					clickImage(driver, param1, param2, fetchMetadataVO, fetchConfigVO, count, customerDetails);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					logger.error(" The Count Value is : " + count);
					clickImage(driver, param1, param2, fetchMetadataVO, fetchConfigVO, count, customerDetails);
				} else {
					logger.error("Count value exceeds the limit " + count);
					logger.error("Failed During click Image " + fetchMetadataVO.getScriptNumber());
					fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
					throw e;
				}
			}

		} else {
			throw new Exception("XpathLocation is null");
		}
	}

	public void clickTableImage(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, int count,
			CustomerProjectDto customerDetails) throws Exception {
//		int count=0;
		String scriptID = fetchMetadataVO.getScriptId();
		String action = fetchMetadataVO.getAction();
		String lineNumber = fetchMetadataVO.getLineNumber();
//		String xpathlocation = service.getXpathParams(scriptID, lineNumber);
		String testSetLine=fetchMetadataVO.getTestSetLineId();
		String xpathlocation = service.getXpathParams(scriptID, lineNumber,testSetLine);
		if (xpathlocation != null) {
			String param1r = xpathlocation.replace("param1", param1);
			String paramsr = param1r.replace("param2", param2);
			try {
//			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
//			wait.until(ExpectedConditions
//					.presenceOfElementLocated(By.xpath((paramsr))));
				WebElement waittext = driver.findElement(By.xpath((paramsr)));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				logger.info("Successfully clicked click Table Image " + fetchMetadataVO.getScriptNumber());

			} catch (Exception e) {

				if (count == 0) {
					count = 1;
					logger.error(" The Count Value is : " + count);
					clickTableImage(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, count,
							customerDetails);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					logger.error(" The Count Value is : " + count);
					clickTableImage(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, count,
							customerDetails);
				} else {
					logger.error("Count value exceeds the limit " + count);
					logger.error("Failed During Click Table Image " + fetchMetadataVO.getScriptNumber());
					fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
					throw e;
				}
			}

		} else {
			throw new Exception("XpathLocation is null");
		}
	}

	public void tableRowSelect(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, int count, CustomerProjectDto customerDetails) throws Exception {
//		int count=0;
		String scriptID = fetchMetadataVO.getScriptId();
		String action = fetchMetadataVO.getAction();
		String lineNumber = fetchMetadataVO.getLineNumber();
//		String xpathlocation = service.getXpathParams(scriptID, lineNumber);
		String testSetLine=fetchMetadataVO.getTestSetLineId();
		String xpathlocation = service.getXpathParams(scriptID, lineNumber,testSetLine);
		if (xpathlocation != null) {
			String param1r = xpathlocation.replace("param1", param1);
			String paramsr = param1r.replace("param2", param2);
			try {
//			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
//			wait.until(ExpectedConditions
//					.presenceOfElementLocated(By.xpath((paramsr))));
				WebElement waittext = driver.findElement(By.xpath((paramsr)));
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				logger.info("Successs selected tableRowSelect " + fetchMetadataVO.getScriptNumber());
			} catch (Exception e) {

				if (count == 0) {
					count = 1;
					logger.error(" The Count Value is : " + count);
					tableRowSelect(driver, param1, param2, fetchMetadataVO, fetchConfigVO, count, customerDetails);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					logger.error(" The Count Value is : " + count);
				} else {
					logger.error("Count value exceeds the limit " + count);
					logger.error("Failed During TableRowSelect " + fetchMetadataVO.getScriptNumber());
					fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
					throw e;
				}
			}

		} else {
			throw new Exception("XpathLocation is null");
		}
	}

	public void clickButtonDropdownText(WebDriver driver, String param1, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("//li[normalize-space(text())='" + keysToSend + "']")));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath(("//li[normalize-space(text())='" + keysToSend + "']")), keysToSend));
			Thread.sleep(5000);
			WebElement waittext = driver.findElement(By.xpath("//li[normalize-space(text())='" + keysToSend + "']"));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Successfully Clicked ClickButtonDropdownText" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed During clickButtonDropdownText " + scripNumber);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"//div[contains(@class,'PopupMenuContent')]//td[normalize-space(text())='" + keysToSend + "']")));
			WebElement waittext = driver.findElement(By.xpath(
					"//div[contains(@class,'PopupMenuContent')]//td[normalize-space(text())='" + keysToSend + "']"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Successfully Clicked ClickButtonDropdownText" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed During clickButtonDropdownText " + scripNumber);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("//td[normalize-space(text())='" + keysToSend + "']")));
			WebElement waittext = driver.findElement(By.xpath("//td[normalize-space(text())='" + keysToSend + "']"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Successfully Clicked ClickButtonDropdownText" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed During clickButtonDropdownText " + scripNumber);
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void clickButtonDropdown(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, int count,
			CustomerProjectDto customerDetails) throws Exception {

//		int count=0;
		String scriptID = fetchMetadataVO.getScriptId();
		String action = fetchMetadataVO.getAction();
		String lineNumber = fetchMetadataVO.getLineNumber();
//		String xpathlocation = service.getXpathParams(scriptID, lineNumber);
		String testSetLine=fetchMetadataVO.getTestSetLineId();
		String xpathlocation = service.getXpathParams(scriptID, lineNumber,testSetLine);
		if (xpathlocation != null) {
			String param1r = xpathlocation.replace("param1", param1);
			String paramsr = param1r.replace("param2", param2);
			try {
//			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
//			wait.until(ExpectedConditions
//					.presenceOfElementLocated(By.xpath((paramsr))));
				WebElement waittext = driver.findElement(By.xpath(paramsr));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				clickButtonDropdownText(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				logger.info("Successfully clicked clickButtonDropdown " + fetchMetadataVO.getScriptNumber());
			} catch (Exception e) {

				if (count == 0) {
					count = 1;
					logger.error(" The Count Value is : " + count);
					clickButtonDropdown(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, count,
							customerDetails);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					logger.error(" The Count Value is : " + count);
					clickButtonDropdown(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, count,
							customerDetails);
				} else {
					logger.error("Count value exceeds the limit " + count);
					logger.error("Failed During clickButton Dropdown " + fetchMetadataVO.getScriptNumber());
					fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
					throw e;
				}
			}

		} else {
			throw new Exception("XpathLocation is null");
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
		fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
		return;
	}

	public void selectByText(WebDriver driver, String param1, String param2, String inputData,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, int count,
			CustomerProjectDto customerDetails) throws Exception {
//		int count=0;
		String scriptID = fetchMetadataVO.getScriptId();
		String action = fetchMetadataVO.getAction();
		String lineNumber = fetchMetadataVO.getLineNumber();
//		String xpathlocation = service.getXpathParams(scriptID, lineNumber);
		String testSetLine=fetchMetadataVO.getTestSetLineId();
		String xpathlocation = service.getXpathParams(scriptID, lineNumber,testSetLine);
		if (xpathlocation != null) {
			String param1r = xpathlocation.replace("param1", param1);
			String paramsr = param1r.replace("param2", param2);
			try {
//			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
//			wait.until(ExpectedConditions
//					.presenceOfElementLocated(By.xpath((paramsr))));
				WebElement waittext = driver.findElement(By.xpath((paramsr)));
				selectMethod(driver, inputData, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				logger.info("Successfully selected selectBytext " + fetchMetadataVO.getScriptNumber());
			} catch (Exception e) {

				if (count == 0) {
					count = 1;
					Thread.sleep(2000);
					logger.error(" The Count Value is : " + count);
					selectByText(driver, param1, param2, inputData, fetchMetadataVO, fetchConfigVO, count,
							customerDetails);

				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					logger.error(" The Count Value is : " + count);
					selectByText(driver, param1, param2, inputData, fetchMetadataVO, fetchConfigVO, count,
							customerDetails);
				} else {
					logger.error("Count value exceeds the limit " + count);
					logger.error("Failed During Select By TExt " + fetchMetadataVO.getScriptNumber());
					fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
					throw e;
				}
			}

		} else {
			throw new Exception("XpathLocation is null");
		}
	}

	public void tableDropdownValues(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, int count,
			CustomerProjectDto customerDetails) throws Exception {
//		int count=0;
		String scriptID = fetchMetadataVO.getScriptId();
		String action = fetchMetadataVO.getAction();
		String lineNumber = fetchMetadataVO.getLineNumber();
//		String xpathlocation = service.getXpathParams(scriptID, lineNumber);
		String testSetLine=fetchMetadataVO.getTestSetLineId();
		String xpathlocation = service.getXpathParams(scriptID, lineNumber,testSetLine);
		if (xpathlocation != null) {
			String param1r = xpathlocation.replace("param1", param1);
			String paramsr = param1r.replace("param2", param2);
			try {
//			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
//			wait.until(ExpectedConditions
//					.presenceOfElementLocated(By.xpath((paramsr))));
				WebElement waittext = driver.findElement(By.xpath((paramsr)));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				tableDropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				logger.info("Successfully clicked  tableDropdownValues " + fetchMetadataVO.getScriptNumber());
			} catch (Exception e) {

				if (count == 0) {
					count = 1;
					logger.error(" The Count Value is : " + count);
					tableDropdownValues(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, count,
							customerDetails);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					logger.error(" The Count Value is : " + count);
					tableDropdownValues(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, count,
							customerDetails);
				} else {
					logger.error("Count value exceeds the limit " + count);
					logger.error("Failed During tableDropdownValues " + fetchMetadataVO.getScriptNumber());
					fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
					throw e;
				}
			}

		} else {
			throw new Exception("XpathLocation is null");
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
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked tableDropdownTexts" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  tableDropdownTexts" + scripNumber);
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
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked tableDropdownTexts" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  tableDropdownTexts" + scripNumber);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//*[text()='" + param1 + "']/following::li[text()='" + keysToSend + "']")));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath("//*[text()='" + param1 + "']/following::li[text()='" + keysToSend + "']"), keysToSend));
			WebElement waittext = driver
					.findElement(By.xpath("//*[text()='" + param1 + "']/following::li[text()='" + keysToSend + "']"));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked tableDropdownTexts" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  tableDropdownTexts" + scripNumber);
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
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked tableDropdownTexts" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  tableDropdownTexts" + scripNumber);
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
				logger.info("Sucessfully Clicked tableDropdownTexts" + scripNumber);
			} catch (Exception e) {
				WebElement button = driver.findElement(By.xpath(
						"//*[text()='Search']/following::*[text()='" + param2 + "']/following::*[text()='OK'][1]"));
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.error("Failed during  tableDropdownTexts" + scripNumber);
				button.click();
			}
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  tableDropdownTexts" + scripNumber);
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
				logger.info("Sucessfully Clicked tableDropdownTexts" + scripNumber);
			} catch (Exception e) {
				WebElement searchResult = driver
						.findElement(By.xpath("//*[text()='Search']/following::*[text()='Value']/following::input[1]"));
				typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.error("Failed during  tableDropdownTexts" + scripNumber);
			}

			WebElement text = driver.findElement(By.xpath("(//span[contains(text(),'" + keysToSend + "')])[1]"));
			text.click();
			Thread.sleep(1000);
			try {
				WebElement button = driver.findElement(
						By.xpath("//*[text()='Search']/following::*[text()='Name']/following::*[text()='OK'][1]"));
				button.click();
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked tableDropdownTexts" + scripNumber);
			} catch (Exception e) {
				WebElement button = driver.findElement(
						By.xpath("//*[text()='Search']/following::*[text()='Value']/following::*[text()='OK'][1]"));
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.error("Failed during  tableDropdownTexts" + scripNumber);
				button.click();
			}

			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  tableDropdownTexts" + scripNumber);
		}
		try {
			WebElement button = driver
					.findElement(By.xpath("//*[text()='Search']/following::*[normalize-space(text())='" + param2
							+ "']/following::*[text()='OK'][1]"));
			button.click();
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked tableDropdownTexts" + scripNumber);
			return;
		} catch (Exception e) {
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during  tableDropdownTexts" + scripNumber);
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
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
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
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
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
					logger.info("Sucessfully Clicked Postal Code Legal Entity dropdownTexts" + scripNumber);

				}

				return;

			}

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during Postal Code Legal Entity  dropdownTexts" + scripNumber);

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
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			Thread.sleep(2000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked dropdownTexts" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during dropdownTexts" + scripNumber);
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
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			Thread.sleep(2000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Sucessfully Clicked dropdownTexts" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during dropdownTexts" + scripNumber);
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
					logger.info("Sucessfully Clicked dropdownTexts" + scripNumber);
					text.click();
				} catch (Exception e) {
					String scripNumber = fetchMetadataVO.getScriptNumber();
					logger.error("Failed during dropdownTexts" + scripNumber);
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
				logger.info("Sucessfully Clicked dropdownTexts" + scripNumber);
			} catch (Exception e) {
				WebElement button = driver
						.findElement(By.xpath("//*[text()='Search']/following::*[normalize-space(text())='" + param2
								+ "']/following::*[not (@aria-disabled) and text()='OK'][1]"));
				button.click();
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.error("Failed during dropdownTexts" + scripNumber);
			}

			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during dropdownTexts" + scripNumber);
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
			logger.info("Sucessfully Clicked dropdownTexts" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during dropdownTexts" + scripNumber);
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
			logger.info("Sucessfully Clicked dropdownTexts" + scripNumber);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during dropdownTexts" + scripNumber);
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
			logger.info("Sucessfully Clicked dropdownTexts" + scripNumber);
			try {
				WebElement searchResult = driver.findElement(
						By.xpath("//div[contains(@id,'PopupId::content')]//*[text()='Search']/following::*[text()='"
								+ param2 + "']/following::input[1]"));
				typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
				String scriptID = fetchMetadataVO.getScriptId();
				logger.info("Sucessfully Clicked dropdownTexts" + scriptID);
			} catch (Exception e) {
				WebElement searchResult = driver.findElement(By.xpath(
						"//div[contains(@id,'PopupId::content')]//*[text()='Search']/following::*[text()='Name']/following::input[1]"));
				typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
				String scriptID = fetchMetadataVO.getScriptId();
				logger.error("Failed during dropdownTexts" + scriptID);
			}
			if (keysToSend != null) {
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
				try {
					WebElement text = driver.findElement(By.xpath(
							"//div[@class='AFDetectExpansion']/following::span[text()='Name']/following::span[text()='"
									+ keysToSend + "']"));
					text.click();
					String scriptID = fetchMetadataVO.getScriptId();
					logger.info("Sucessfully Clicked dropdownTexts" + scriptID);
				} catch (Exception e) {
					WebElement text = driver
							.findElement(By.xpath("(//span[contains(text(),'" + keysToSend + "')])[1]"));
					text.click();
					String scriptID = fetchMetadataVO.getScriptId();
					logger.error("Failed during dropdownTexts" + scriptID);
				}
			}
			try {
				WebElement button = driver
						.findElement(By.xpath("//*[text()='Search']/following::*[normalize-space(text())='" + param2
								+ "']/following::*[not (@aria-disabled) and text()='K'][1]"));
				String scriptID = fetchMetadataVO.getScriptId();
				logger.info("Sucessfully Clicked dropdownTexts" + scriptID);
				button.click();
			} catch (Exception e) {
				WebElement button = driver
						.findElement(By.xpath("//*[text()='Search']/following::*[normalize-space(text())='" + param2
								+ "']/following::*[not (@aria-disabled) and text()='OK'][1]"));
				button.click();
				String scriptID = fetchMetadataVO.getScriptId();
				logger.error("Failed during dropdownTexts" + scriptID);
			}

			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during dropdownTexts" + scripNumber);
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void dropdownValues(WebDriver driver, String param1, String param2, String param3, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, int count,
			CustomerProjectDto customerDetails,String xpathlocation) throws Exception {

		String scriptID = fetchMetadataVO.getScriptId();
		String lineNumber = fetchMetadataVO.getLineNumber();
		String testSetLine=fetchMetadataVO.getTestSetLineId();
		String[] mainparams=null;
		if(xpathlocation==null) {
			xpathlocation = service.getXpathParams(scriptID, lineNumber,testSetLine);
		}
		if (xpathlocation != null) {
			String param1r = xpathlocation.replace("param1", param1).replace("param2", param2).replace("param3", param3).replace("keysToSend", keysToSend);
			mainparams = param1r.split(";");
			try {
				for(int i=0;i<mainparams.length;i++) {
							String mainparam = mainparams[i];
							WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
							wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(mainparam)));
							wait.until(ExpectedConditions.elementToBeClickable(By.xpath(mainparam)));
							WebElement waittext = driver.findElement(By.xpath(mainparam));
							Actions actions = new Actions(driver);
							Thread.sleep(3000);
							actions.moveToElement(waittext).build().perform();
							if(mainparam.contains("input")) {
								typeIntoValidxpath(driver, keysToSend, waittext, fetchConfigVO, fetchMetadataVO);
							}
							else {
								actions.moveToElement(waittext).click().build().perform();
							}
							fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
							String scripNumber = fetchMetadataVO.getScriptNumber();
							logger.info("XpathPerformance=> Successfully dropdownValues step-"+i+" is done " + scripNumber);
							mainparams=ArrayUtils.removeElement(mainparams, mainparams[i]);
							i--;
				}
				return;
			} catch (Exception e) {
				if (count == 0) {
					count = 1;
					xpathlocation=String.join(";", mainparams);
					dropdownValues(driver, param1, param2, param3, keysToSend, fetchMetadataVO, fetchConfigVO, count,customerDetails,xpathlocation);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					xpathlocation=String.join(";", mainparams);
					dropdownValues(driver, param1, param2, param3, keysToSend, fetchMetadataVO, fetchConfigVO, count,customerDetails,xpathlocation);
				} else {
					logger.error("XpathPerformance=> Failed During dropdownValues");
					fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
					throw e;
				}
			}
		}

		else {
			throw new Exception("XpathLocation is null");
		}
	}

	public void clickCheckbox(WebDriver driver, String param1, String keysToSend, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, int count, CustomerProjectDto customerDetails) throws Exception {
//		int count=0;
		String scriptID = fetchMetadataVO.getScriptId();
		String action = fetchMetadataVO.getAction();
		String lineNumber = fetchMetadataVO.getLineNumber();
//		String xpathlocation = service.getXpathParams(scriptID, lineNumber);
		String testSetLine=fetchMetadataVO.getTestSetLineId();
		String xpathlocation = service.getXpathParams(scriptID, lineNumber,testSetLine);
		if (xpathlocation != null) {
			String param1r = xpathlocation.replace("param1", param1);
			String paramsr = param1r.replace("keysToSend", keysToSend);
			try {
//			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
//			wait.until(ExpectedConditions
//					.presenceOfElementLocated(By.xpath((paramsr))));
				WebElement waittext = driver.findElement(By.xpath((paramsr)));
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				logger.info("Successfully Clicked clickCheckox  " + fetchMetadataVO.getScriptNumber());

			} catch (Exception e) {

				if (count == 0) {
					count = 1;
					logger.error(" The Count Value is : " + count);
					clickCheckbox(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO, count, customerDetails);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					logger.error(" The Count Value is : " + count);
					clickCheckbox(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO, count, customerDetails);
				} else {
					logger.error("Count value exceeds the limit " + count);
					logger.error("Failed During click checkbox " + fetchMetadataVO.getScriptNumber());
					fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
					throw e;
				}
			}

		} else {
			throw new Exception("XpathLocation is null");
		}
	}

	public void clickRadiobutton(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, int count,
			CustomerProjectDto customerDetails) throws Exception {
//		int count=0;
		String scriptID = fetchMetadataVO.getScriptId();
		String action = fetchMetadataVO.getAction();

		String lineNumber = fetchMetadataVO.getLineNumber();
//		String xpathlocation = service.getXpathParams(scriptID, lineNumber);
		String testSetLine=fetchMetadataVO.getTestSetLineId();
		String xpathlocation = service.getXpathParams(scriptID, lineNumber,testSetLine);
		if (xpathlocation != null) {
			String param1r = xpathlocation.replace("param1", param1);
			String param2r = param1r.replace("param2", param2);
			String paramsr = param2r.replace("keysToSend", keysToSend);
			logger.info("XpathPerformance=> successfully clicked  Radiobutton " + fetchMetadataVO.getScriptNumber());
			try {
//			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
//			wait.until(ExpectedConditions
//					.presenceOfElementLocated(By.xpath((paramsr))));
				WebElement waittext = driver.findElement(By.xpath((paramsr)));
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(500);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			} catch (Exception e) {

				if (count == 0) {
					count = 1;
					logger.error(" The Count Value is : " + count);
					clickRadiobutton(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, count,
							customerDetails);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					logger.error(" The Count Value is : " + count);
					clickCheckbox(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO, count, customerDetails);
				} else {
					logger.error("XpathPerformance=> Failed During clickRadiobutton " + fetchMetadataVO.getScriptNumber());
					fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
					throw e;
				}
			}

		} else {
			throw new Exception("XpathLocation is null");
		}
	}

	public void tab(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) throws Exception {
		try {
			Thread.sleep(4000);
			Actions action = new Actions(driver);
			action.sendKeys(Keys.TAB).build().perform();
			Thread.sleep(8000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("XpathPerformance=> Sucessfully Clicked tab" + scripNumber);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("XpathPerformance=> Failed during  tab" + scripNumber);
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			e.printStackTrace();
			throw e;
		}
	}

	public void selectAValue(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, int count,
			CustomerProjectDto customerDetails) throws Exception {
//		int count=0;
		String scriptID = fetchMetadataVO.getScriptId();
		String action = fetchMetadataVO.getAction();

		String lineNumber = fetchMetadataVO.getLineNumber();
//		String xpathlocation = service.getXpathParams(scriptID, lineNumber);
		String testSetLine=fetchMetadataVO.getTestSetLineId();
		String xpathlocation = service.getXpathParams(scriptID, lineNumber,testSetLine);
		if (xpathlocation != null) {
			String param1r = xpathlocation.replace("param1", param1);
			String param2r = param1r.replace("param2", param2);
			String paramsr = param2r.replace("keysToSend", keysToSend);
			try {
//			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
//			wait.until(ExpectedConditions
//					.presenceOfElementLocated(By.xpath((paramsr))));
				WebElement waittext = driver.findElement(By.xpath((paramsr)));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(2000);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				logger.info("XpathPerformance=> Successfully clicked SelectAvalue " + fetchMetadataVO.getScriptNumber());
			} catch (Exception e) {

				if (count == 0) {
					count = 1;
					logger.error(" The Count Value is : " + count);
					selectAValue(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, count,
							customerDetails);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					logger.error(" The Count Value is : " + count);
					selectAValue(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, count,
							customerDetails);
				} else {
					logger.error("XpathPerformance=> Failed During SelectAValue " + fetchMetadataVO.getScriptNumber());
					fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
					throw e;
				}
			}

		} else {
			throw new Exception("XpathLocation is null");
		}
	}

	public void navigate(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			String type1, String type2, String param1, String param2, int count, CustomerProjectDto customerDetails,String xpathlocation,int totalXpaths)
			throws Exception {
		String scriptID = fetchMetadataVO.getScriptId();
		String lineNumber = fetchMetadataVO.getLineNumber();
		String testSetLine=fetchMetadataVO.getTestSetLineId();
		String[] mainparams=null;
		String param3 = "Navigator";
		boolean clickNavigator=false;
		if(xpathlocation==null) {
			clickNavigator=true;
			xpathlocation = service.getXpathParams(scriptID, lineNumber,testSetLine);
			if (xpathlocation != null) {
				mainparams = xpathlocation.split(";");
				totalXpaths=mainparams.length;
			}
		}
		if (xpathlocation != null) {
			String param1r = xpathlocation.replace("param1", param1);
			String paramsr = param1r.replace("param2", param2);
			mainparams = paramsr.split(";");
			try {
				for(int i=0;i<mainparams.length;i++) {
					String mainparam = mainparams[i];
							if(clickNavigator==true && totalXpaths==2 && mainparams.length==2 && i==0) {
								navigator(driver, param3, fetchMetadataVO, fetchConfigVO, customerDetails);
							}
							WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
							wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(mainparam)));
							wait.until(ExpectedConditions.elementToBeClickable(By.xpath(mainparam)));
							WebElement waittext = driver.findElement(By.xpath(mainparam));
							Actions actions = new Actions(driver);
							Thread.sleep(3000);
							actions.moveToElement(waittext).build().perform();
							actions.moveToElement(waittext).click().build().perform();
							fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
							String scripNumber = fetchMetadataVO.getScriptNumber();
							logger.info("XpathPerformance=> Successfully Navigation step-"+i+" is done " + scripNumber);
							mainparams=ArrayUtils.removeElement(mainparams, mainparams[i]);
							i--;
				}
				return;
			} catch (Exception e) {
				if (count == 0) {
					count = 1;
					xpathlocation=String.join(";", mainparams);
					navigate(driver, fetchConfigVO,
							fetchMetadataVO, type1, type2, param1, param2, count, customerDetails,xpathlocation,totalXpaths);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					xpathlocation=String.join(";", mainparams);
					navigate(driver, fetchConfigVO,
							fetchMetadataVO, type1, type2, param1, param2, count, customerDetails,xpathlocation,totalXpaths);
				} else {
					logger.error("XpathPerformance=> Failed During Navigate " + fetchMetadataVO.getScriptNumber());
					fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
					throw e;
				}
				}
			}

		else {
			throw new Exception("XpathLocation is null");
		}
	}
	
	private void clickValidateXpath(WebDriver driver, ScriptDetailsDto fetchMetadataVO, WebElement waittext,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click();", waittext);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("XpathPerformance=> Sucessfully Clicked clickValidateXpath" + scripNumber);
			// waittext.click();
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("XpathPerformance=> Failed during  clickValidateXpath" + scripNumber);
			e.printStackTrace();
		}
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
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Successfully navigator is done " + scripNumber);
			String xpath = "//a[@title='param1']";
			return xpath;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("XpathPerformance=> Failed during navigator " + scripNumber);
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void clickMenu(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, int count, CustomerProjectDto customerDetails) throws Exception {
		String scriptID = fetchMetadataVO.getScriptId();
		String action = fetchMetadataVO.getAction();

		String lineNumber = fetchMetadataVO.getLineNumber();
//		String xpathlocation = service.getXpathParams(scriptID, lineNumber);
		String testSetLine=fetchMetadataVO.getTestSetLineId();
		String xpathlocation = service.getXpathParams(scriptID, lineNumber,testSetLine);
		if (xpathlocation != null) {
			String param1r = xpathlocation.replace("param1", param1);
			String paramsr = param1r.replace("param2", param2);
			try {

				WebElement waittext = driver.findElement(By.xpath((paramsr)));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(8000);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				logger.info("XpathPerformance=> Sucessfully clicked Element in clickmenu " + scripNumber);
				return;

			} catch (Exception e) {
				if (count == 0) {
					count = 1;
					logger.error(" The Count Value is : " + count);
					clickMenu(driver, param1, param2, fetchMetadataVO, fetchConfigVO, count, customerDetails);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					logger.error(" The Count Value is : " + count);
					clickMenu(driver, param1, param2, fetchMetadataVO, fetchConfigVO, count, customerDetails);
				} else {
					logger.error("XpathPerformance=> Failed During clickMenu " + fetchMetadataVO.getScriptNumber());
					fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
					throw e;
				}
			}

		} else {
			throw new Exception("XpathLocation is null");
		}

	}

	public void mousehover(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			int count = 0;
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			Actions actions = new Actions(driver);
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("(//table[@summary='" + param1 + "']//tr[1]/following::a)[2]")));
			scrollUsingElement(driver, param1, fetchMetadataVO, fetchConfigVO, count, customerDetails);
			Thread.sleep(6000);
			WebElement waittext = driver
					.findElement(By.xpath("(//table[@summary='" + param1 + "']//tr[1]/following::a)[2]"));
			actions.moveToElement(waittext).build().perform();
			clickImage(driver, param2, param1, fetchMetadataVO, fetchConfigVO, count, customerDetails);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
//			String xpath="(//table[@summary='" + param1 + "']//tr[1]/following::a)[2]";
//					service.saveXpathParams(param1,param2,scripNumber,xpath);
			logger.info("XpathPerformance=> Sucessfully Clicked mousehover" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("XpathPerformance=> Failed during  mousehover" + scripNumber);
		}
		try {
			Actions actions = new Actions(driver);
			WebElement waittill = driver.findElement(By.xpath(
					"(//table[@role='presentation']/following::a[normalize-space(text())='" + param1 + "'])[1]"));
			actions.moveToElement(waittill).build().perform();
			Thread.sleep(5000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
//			String xpath="(//table[@role='presentation']/following::a[normalize-space(text())='" + param1 + "'])[1]";
//					service.saveXpathParams(param1,param2,scripNumber,xpath);
			logger.info("XpathPerformance=> Sucessfully Clicked mousehover" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("XpathPerformance=> Failed during  mousehover" + scripNumber);
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void scrollUsingElement(WebDriver driver, String inputParam, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, int count, CustomerProjectDto customerDetails) throws Exception {
		String scriptID = fetchMetadataVO.getScriptId();
		String action = fetchMetadataVO.getAction();
		String lineNumber = fetchMetadataVO.getLineNumber();
//		String xpathlocation = service.getXpathParams(scriptID, lineNumber);
		String testSetLine=fetchMetadataVO.getTestSetLineId();
		String xpathlocation = service.getXpathParams(scriptID, lineNumber,testSetLine);
		if (xpathlocation != null) {
			String param1r = xpathlocation.replace("inputParam", inputParam);
			try {
				WebElement waittill = driver.findElement(By.xpath(param1r));
				scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("XpathPerformance=> Sucessfully Clicked scrollUsingElement" + scripNumber);
				return;
			} catch (Exception e) {
				if (count == 0) {
					count = 1;
					scrollUsingElement(driver, fetchMetadataVO.getInputParameter(), fetchMetadataVO, fetchConfigVO,
							count, customerDetails);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					scrollUsingElement(driver, fetchMetadataVO.getInputParameter(), fetchMetadataVO, fetchConfigVO,
							count, customerDetails);
				} else {
					logger.error("XpathPerformance=> Failed During scrollUsingElement " + fetchMetadataVO.getScriptNumber());
					fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
					throw e;
				}

			}

		} else {
			throw new Exception("XpathLocation is null");
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
		fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
		String scripNumber = fetchMetadataVO.getScriptNumber();
		logger.info("XpathPerformance=> Sucessfully Clicked scrollMethod" + scripNumber);
	}

	public void switchToFrame(WebDriver driver, String inputParam, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, int count,CustomerProjectDto customerDetails) throws Exception {
		String scriptID = fetchMetadataVO.getScriptId();
		String action = fetchMetadataVO.getAction();
		String lineNumber = fetchMetadataVO.getLineNumber();
//		String xpathlocation = service.getXpathParams(scriptID, lineNumber);
		String testSetLine=fetchMetadataVO.getTestSetLineId();
		String xpathlocation = service.getXpathParams(scriptID, lineNumber,testSetLine);
		if (xpathlocation != null) {
			String param1r = xpathlocation.replace("inputParam", inputParam);
			try {
				WebElement waittext = driver.findElement(By.xpath(param1r));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				driver.switchTo().frame(waittext);

				return;
			} catch (Exception e) {
				if (count == 0) {
					count = 1;
					switchToFrame(driver, fetchMetadataVO.getInputParameter(), fetchMetadataVO, fetchConfigVO, count,customerDetails);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					switchToFrame(driver, fetchMetadataVO.getInputParameter(), fetchMetadataVO, fetchConfigVO, count,customerDetails);
				} else {
					logger.error("XpathPerformance=> Failed During SwitchToFrame " + fetchMetadataVO.getScriptNumber());
					fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
					throw e;
				}
			}

		} else {
			throw new Exception("XpathLocation is null");
		}
	}

	public void clear(WebDriver driver, String inputParam1, String inputParam2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, int count,CustomerProjectDto customerDetails) throws Exception {

		String scriptID = fetchMetadataVO.getScriptId();
		String action = fetchMetadataVO.getAction();
		String lineNumber = fetchMetadataVO.getLineNumber();
//		String xpathlocation = service.getXpathParams(scriptID, lineNumber);
		String testSetLine=fetchMetadataVO.getTestSetLineId();
		String xpathlocation = service.getXpathParams(scriptID, lineNumber,testSetLine);
		if (xpathlocation != null) {
			String param1r = xpathlocation.replace("inputParam1", inputParam1);
			String paramsr = param1r.replace("inputParam2", inputParam2);
			try {

				WebElement waittill = driver.findElement(By.xpath(paramsr));
				clearMethod(driver, waittill);
				Thread.sleep(4000);
				return;

			} catch (Exception e) {
				if (count == 0) {
					count = 1;
					clear(driver, inputParam1, inputParam2, fetchMetadataVO, fetchConfigVO, count,customerDetails);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					clear(driver, inputParam1, inputParam2, fetchMetadataVO, fetchConfigVO, count,customerDetails);
				} else {
					logger.error("XpathPerformance=> Failed During clear Action " + fetchMetadataVO.getScriptNumber());
					fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
					throw e;
				}
			}

		} else {
			throw new Exception("XpathLocation is null");
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
	public String fullPagePassedScreenshot(WebDriver driver, ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails) {
		String imageName = null;
		String folderName = null;
		try {
			folderName = SCREENSHOT + FORWARD_SLASH + customerDetails.getCustomerName() + FORWARD_SLASH
					+ customerDetails.getTestSetName();
			
			imageName = (fetchMetadataVO.getSeqNum() + "_" + fetchMetadataVO.getLineNumber() + "_"
					+ fetchMetadataVO.getScenarioName() + "_" + fetchMetadataVO.getScriptNumber() + "_"
					+ customerDetails.getTestSetName() + "_" + fetchMetadataVO.getLineNumber() + "_Passed").concat(PNG_EXTENSION);
			
			BufferedImage bufferedImage = Shutterbug.shootPage(driver, Capture.FULL).getImage();
//			Screenshot s=new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(driver);
	        File file = new File(System.getProperty("java.io.tmpdir")+File.separator+imageName+PNG_EXTENSION);
	        ImageIO.write(bufferedImage,"PNG",file);
	        
	        uploadObjectToObjectStore(file.getCanonicalPath(), folderName, imageName);

			logger.info("Successfully Screenshot is taken " + imageName);
			return folderName + FORWARD_SLASH + imageName;

		} catch (Exception e) {
			logger.error("Failed During Taking screenshot " + e.getMessage());
			return e.getMessage();
		}
	}
	
	public String fullPageFailedScreenshot(WebDriver driver, ScriptDetailsDto fetchMetadataVO,
			CustomerProjectDto customerDetails) {
		String imageName = null;
		String folderName = null;
		try {
			folderName = SCREENSHOT + FORWARD_SLASH + customerDetails.getCustomerName() + FORWARD_SLASH
					+ customerDetails.getTestSetName();
			imageName = (fetchMetadataVO.getSeqNum() + "_" + fetchMetadataVO.getLineNumber() + "_"
					+ fetchMetadataVO.getScenarioName() + "_" + fetchMetadataVO.getScriptNumber() + "_"
					+ customerDetails.getTestSetName() + "_" + fetchMetadataVO.getLineNumber() + "_Failed").concat(PNG_EXTENSION);
			BufferedImage bufferedImage = Shutterbug.shootPage(driver, Capture.FULL).getImage();
//			Screenshot s=new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(driver);
	        File file = new File(System.getProperty("java.io.tmpdir")+File.separator+imageName);
	        ImageIO.write(bufferedImage,"PNG",file);
			uploadObjectToObjectStore(file.getCanonicalPath(), folderName, imageName);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Successfully Failed Screenshot is Taken " + scripNumber);
			return folderName + FORWARD_SLASH + imageName;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during screenshotFail Action. " + scripNumber);
			logger.error("Exception while taking Screenshot" + e.getMessage());
			return e.getMessage();
		}
	}

	public String uploadObjectToObjectStore(String localFilePath, String folderName, String fileName) {

		PutObjectResponse response = null;
		try {
			/**
			 * Create a default authentication provider that uses the DEFAULT profile in the
			 * configuration file. Refer to <see
			 * href="https://docs.cloud.oracle.com/en-us/iaas/Content/API/Concepts/sdkconfig.htm#SDK_and_CLI_Configuration_File>the
			 * public documentation</see> on how to prepare a configuration file.
			 */
			final ConfigFileReader.ConfigFile configFile = ConfigFileReader
					.parse(new FileInputStream(new File(ociConfigPath)), ociConfigName);
			final AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);
			final String FILE_NAME = localFilePath;
			File file = new File(FILE_NAME);
			long fileSize = FileUtils.sizeOf(file);
			InputStream is = new FileInputStream(file);
			String destinationFilePath = folderName + FORWARD_SLASH + fileName;
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
		} catch (WatsEBSException e) {
			throw e;
		} catch (Exception e) {
			throw new WatsEBSException(500, "Exception occured while uploading pdf in Object Storage..", e);
		}
	}
	
	public void clickExpandorcollapse(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails,int count) throws Exception {
		String scriptID = fetchMetadataVO.getScriptId();
		String lineNumber = fetchMetadataVO.getLineNumber();
		String testSetLine=fetchMetadataVO.getTestSetLineId();
		String	xpathlocation = service.getXpathParams(scriptID, lineNumber,testSetLine);
		if (xpathlocation != null) {
			String param1r = xpathlocation.replace("param1", param1).replace("param2", param2);
			try {
							WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
							wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(param1r)));
							wait.until(ExpectedConditions.elementToBeClickable(By.xpath(param1r)));
							WebElement waittext = driver.findElement(By.xpath(param1r));
							Actions actions = new Actions(driver);
							Thread.sleep(3000);
							actions.moveToElement(waittext).build().perform();
							actions.moveToElement(waittext).click().build().perform();
							fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
							String scripNumber = fetchMetadataVO.getScriptNumber();
							logger.info("XpathPerformance=> Successfully click ExpandOrCollapse " + scripNumber);			
				return;
			} catch (Exception e) {
				try {
					WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
					wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(param1r)));
					wait.until(ExpectedConditions.elementToBeClickable(By.xpath(param1r)));
					WebElement waittext = driver.findElement(By.xpath(param1r));
					Actions actions = new Actions(driver);
					Thread.sleep(3000);
					actions.moveToElement(waittext).build().perform();
					actions.moveToElement(waittext).click().build().perform();
					fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
					String scripNumber = fetchMetadataVO.getScriptNumber();
					logger.info("XpathPerformance=> Successfully click ExpandOrCollapse" + scripNumber);
					return;
				} catch (Exception ex) {
					if (count == 0) {
						count = 1;
						clickExpandorcollapse(driver, param1, param2, fetchMetadataVO, fetchConfigVO, customerDetails,
								count);
						Thread.sleep(2000);
					} else if (count <= 2) {
						count = count + 1;
						clickExpandorcollapse(driver, param1, param2, fetchMetadataVO, fetchConfigVO, customerDetails,
								count);
						Thread.sleep(2000);
					} else {
						logger.error("XpathPerformance=> Failed During click ExpandOrCollapse ");
						fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
						throw ex;
					}
				}
			}
		}

		else {
			throw new Exception("XpathLocation is null");
		}
	}
	
	public void clickNotificationLink(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails,int count) throws Exception {
		String scriptID = fetchMetadataVO.getScriptId();
		String lineNumber = fetchMetadataVO.getLineNumber();
		String testSetLine=fetchMetadataVO.getTestSetLineId();
		String	xpathlocation = service.getXpathParams(scriptID, lineNumber,testSetLine);
		if (xpathlocation != null) {
			String param1r = xpathlocation.replace("param1", param1).replace("param2", param2);
			try {
							WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
							wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(param1r)));
							wait.until(ExpectedConditions.elementToBeClickable(By.xpath(param1r)));
							WebElement waittext = driver.findElement(By.xpath(param1r));
							Actions actions = new Actions(driver);
							Thread.sleep(3000);
							actions.moveToElement(waittext).build().perform();
							actions.moveToElement(waittext).click().build().perform();
//							fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
							String scripNumber = fetchMetadataVO.getScriptNumber();
							logger.info("XpathPerformance=> Successfully clicked NotificationLink " + scripNumber);			
				return;
			} catch (Exception e) {
				if (count == 0) {
					count = 1;
					clickNotificationLink(driver, param1, param2, fetchMetadataVO, fetchConfigVO,
							customerDetails,count);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					clickNotificationLink(driver, param1, param2, fetchMetadataVO, fetchConfigVO,
							customerDetails,count);
					Thread.sleep(2000);
				} else {
					logger.error("XpathPerformance=> Failed During clickNotificationLink " + fetchMetadataVO.getScriptNumber());
					fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
					throw e;
				}
			}
		}

		else {
			throw new Exception("XpathLocation is null");
		}
	}
	
	public void clickNotification(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails,int count) throws Exception {
		String scriptID = fetchMetadataVO.getScriptId();
		String lineNumber = fetchMetadataVO.getLineNumber();
		String testSetLine=fetchMetadataVO.getTestSetLineId();
		String	xpathlocation = service.getXpathParams(scriptID, lineNumber,testSetLine);
		if (xpathlocation != null) {
			String param1r = xpathlocation.replace("param1", param1).replace("param2", param2);
			try {
							WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
							wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(param1r)));
							wait.until(ExpectedConditions.elementToBeClickable(By.xpath(param1r)));
							WebElement waittext = driver.findElement(By.xpath(param1r));
							Actions actions = new Actions(driver);
							Thread.sleep(3000);
							actions.moveToElement(waittext).build().perform();
							actions.moveToElement(waittext).click().build().perform();
							fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
							String scripNumber = fetchMetadataVO.getScriptNumber();
							logger.info("XpathPerformance=> Successfully done clickNotification" + scripNumber);			
				return;
			} catch (Exception e) {
				if (count == 0) {
					count = 1;
					clickNotificationLink(driver, param1, param2, fetchMetadataVO, fetchConfigVO,
							customerDetails,count);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					clickNotificationLink(driver, param1, param2, fetchMetadataVO, fetchConfigVO,
							customerDetails,count);
					Thread.sleep(2000);
				} else {
					logger.error("XpathPerformance=> Failed During clickNotification " + fetchMetadataVO.getScriptNumber());
					fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
					throw e;
				}
			}
		}

		else {
			throw new Exception("XpathLocation is null");
		}
	}
	
	public void openTask(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			String type1, String type2, String param1, String param2, int count, CustomerProjectDto customerDetails,String xpathlocation)
			throws Exception {
		String scriptID = fetchMetadataVO.getScriptId();
		String lineNumber = fetchMetadataVO.getLineNumber();
		String testSetLine=fetchMetadataVO.getTestSetLineId();
		String[] mainparams=null;
		if(xpathlocation==null) {
			xpathlocation = service.getXpathParams(scriptID, lineNumber,testSetLine);
		}
		if (xpathlocation != null) {
			String param1r = xpathlocation.replace("param1", param1);
			String paramsr = param1r.replace("param2", param2);
			mainparams = paramsr.split(";");
			try {
				for(int i=0;i<mainparams.length;i++) {
					String mainparam = mainparams[i];
							WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
							wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(mainparam)));
							wait.until(ExpectedConditions.elementToBeClickable(By.xpath(mainparam)));
							WebElement waittext = driver.findElement(By.xpath(mainparam));
							Actions actions = new Actions(driver);
							Thread.sleep(3000);
							actions.moveToElement(waittext).build().perform();
							clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
							Thread.sleep(1000);
							fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
							String scripNumber = fetchMetadataVO.getScriptNumber();
							logger.info("XpathPerformance=> Successfully clicked openTask " + scripNumber);
							mainparams=ArrayUtils.removeElement(mainparams, mainparams[i]);
							i--;
				}
				return;
			} catch (Exception e) {
				if (count == 0) {
					count = 1;
					xpathlocation=String.join(";", mainparams);
					openTask(driver, fetchConfigVO,
							fetchMetadataVO, type1, type2, param1, param2, count, customerDetails,xpathlocation);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					xpathlocation=String.join(";", mainparams);
					openTask(driver, fetchConfigVO,
							fetchMetadataVO, type1, type2, param1, param2, count, customerDetails,xpathlocation);
				} else {
					logger.error("XpathPerformance=> Failed During openTask " + fetchMetadataVO.getScriptNumber());
					fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
					throw e;
				}
			}
		}

		else {
			throw new Exception("XpathLocation is null");
		}
	}
	
	public void moveToElement(WebDriver driver, String inputParam, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO,CustomerProjectDto customerDetails,int count) throws Exception {
		String scriptID = fetchMetadataVO.getScriptId();
		String lineNumber = fetchMetadataVO.getLineNumber();
		String testSetLine=fetchMetadataVO.getTestSetLineId();
		String	xpathlocation = service.getXpathParams(scriptID, lineNumber,testSetLine);
		if (xpathlocation != null) {
			String param1r = xpathlocation.replace("param1", inputParam);
			try {
							WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
							wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(param1r)));
							wait.until(ExpectedConditions.elementToBeClickable(By.xpath(param1r)));
							WebElement waittext = driver.findElement(By.xpath(param1r));
							Actions actions = new Actions(driver);
							actions.moveToElement(waittext).build().perform();
							fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
							String scripNumber = fetchMetadataVO.getScriptNumber();
							logger.info("XpathPerformance=> Successfully  moveToElement" + scripNumber);			
				return;
			} catch (Exception e) {
				if (count == 0) {
					count = 1;
					moveToElement(driver,
							fetchMetadataVO.getInputParameter(), fetchMetadataVO, fetchConfigVO,customerDetails,count);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					moveToElement(driver,
							fetchMetadataVO.getInputParameter(), fetchMetadataVO, fetchConfigVO,customerDetails,count);
					Thread.sleep(2000);
				} else {
					logger.error("XpathPerformance=> Failed During moveToElement " + fetchMetadataVO.getScriptNumber());
					fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
					throw e;
				}
			}
		}

		else {
			throw new Exception("XpathLocation is null");
		}
	}
	
	public void datePicker(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails,int count)
			throws Exception {

		String[] fullDate = keysToSend.split(">");
		String value1 = fullDate[0];
		String value2 = fullDate[1];
		String value3 = fullDate[2];
		String scriptID = fetchMetadataVO.getScriptId();
		String lineNumber = fetchMetadataVO.getLineNumber();
		String testSetLine=fetchMetadataVO.getTestSetLineId();
		String	xpathlocation = service.getXpathParams(scriptID, lineNumber,testSetLine);
		if (xpathlocation != null) {
			String param1r = xpathlocation.replace("value1", value1).replace("value2", value2).replace("value3", value3);
			String[] mainparams = param1r.split(";");
			try {
				//click date icon
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Actions actions = new Actions(driver);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(mainparams[0])));
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(mainparams[0])));
				WebElement waittill = driver.findElement(By.xpath(mainparams[0]));
				actions.moveToElement(waittill).build().perform();
				actions.moveToElement(waittill).click().build().perform();
				Thread.sleep(1000);
				//Enter year
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(mainparams[1])));
				waittill = driver.findElement(By.xpath(mainparams[1]));
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, value3, waittill, fetchConfigVO, fetchMetadataVO);
				Thread.sleep(2000);
				//click month
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(mainparams[2])));
				waittill = driver.findElement(By.xpath(mainparams[2]));
				selectMethod(driver, value2, fetchMetadataVO, waittill, fetchConfigVO, customerDetails);
				Thread.sleep(2000);
				//click date
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(mainparams[3])));
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(mainparams[3])));
				waittill = driver.findElement(By.xpath(mainparams[3]));
				actions.moveToElement(waittill).build().perform();
				actions.moveToElement(waittill).click().build().perform();
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				logger.info("XpathPerformance=> Successfully done datePicker action " + fetchMetadataVO.getScriptNumber());		
			} catch (Exception e) {
				if (count == 0) {
					count = 1;
					datePicker(driver, param1, param2,fetchMetadataVO.getInputValue(), fetchMetadataVO, fetchConfigVO,customerDetails,count);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					datePicker(driver, param1, param2,fetchMetadataVO.getInputValue(), fetchMetadataVO, fetchConfigVO,customerDetails,count);
					Thread.sleep(2000);
				} else {
					logger.error("XpathPerformance=> Failed During datePicker " + fetchMetadataVO.getScriptNumber());
					fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
					throw e;
				}
			}
		}

	else {
		throw new Exception("XpathLocation is null");
		}	
	}
	
	public void multipleSendKeys(WebDriver driver, String param1, String param2, String value1, String value2,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails, int count) throws Exception {
		String scriptID = fetchMetadataVO.getScriptId();
		String lineNumber = fetchMetadataVO.getLineNumber();
		String testSetLine=fetchMetadataVO.getTestSetLineId();
		String	xpathlocation = service.getXpathParams(scriptID, lineNumber,testSetLine);
		if (xpathlocation != null) {
			String param1r = xpathlocation.replace("value1", value1);
			String[] mainparams = param1r.split(";");
			try {	
							WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
//							wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(mainparams[0])));
//							wait.until(ExpectedConditions.elementToBeClickable(By.xpath(mainparams[0])));
//							WebElement waittext=driver.findElement(By.xpath(mainparams[0]));
//							JavascriptExecutor js=(JavascriptExecutor) driver;
//							js.executeScript("document.querySelector(waittext).scrollLeft=1000");
							
//							wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(mainparams[0])));
//							wait.until(ExpectedConditions.elementToBeClickable(By.xpath(mainparams[0])));
							WebElement waittext = driver.findElement(By.xpath(mainparams[0]));
							Actions actions = new Actions(driver);
							actions.moveToElement(waittext).build().perform();
							waittext.clear();
//							waittext.sendKeys(value2);
							JavascriptExecutor jse = (JavascriptExecutor) driver;
							jse.executeScript("arguments[0].value='" + value2 + "';", waittext);
//							typeIntoValidxpath(driver, value2, waittext, fetchConfigVO, fetchMetadataVO);
							fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
							String scripNumber = fetchMetadataVO.getScriptNumber();
							logger.info("XpathPerformance=> Successfully sent multipleSendKeys  " + scripNumber);			
				return;
			} catch (Exception e) {
				if (count == 0) {
					count = 1;
					multipleSendKeys(driver, param1, param2,value1, value2, fetchMetadataVO, fetchConfigVO, customerDetails,count);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					multipleSendKeys(driver, param1, param2,value1, value2, fetchMetadataVO, fetchConfigVO, customerDetails,count);
					Thread.sleep(2000);
				} else {
					logger.error("XpathPerformance=> Failed During multipleSendKeys " + fetchMetadataVO.getScriptNumber());
					fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
					throw e;
				}
			}
		}

		else {
			throw new Exception("XpathLocation is null");
		} 
	}
	
	public void actionApprove(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails,int count) throws Exception {
		String scriptID = fetchMetadataVO.getScriptId();
		String lineNumber = fetchMetadataVO.getLineNumber();
		String testSetLine=fetchMetadataVO.getTestSetLineId();
		String	xpathlocation = service.getXpathParams(scriptID, lineNumber,testSetLine);
		if (xpathlocation != null) {
			String param1r = xpathlocation.replace("param1", param1).replace("param2", param2);
			String[] mainparams = param1r.split(";");
			try {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(mainparams[0])));
				WebElement waittill = driver.findElement(By.xpath(mainparams[0]));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				actions.moveToElement(waittill).click().build().perform();
				Thread.sleep(3000);
				
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(mainparams[1])));
				waittill = driver.findElement(By.xpath(mainparams[1]));
				actions.moveToElement(waittill).build().perform();
				actions.moveToElement(waittill).click().build().perform();
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				logger.info("XpathPerformance=> Sucessfully clicked ActionApprove " + fetchMetadataVO.getScriptNumber());
			} catch (Exception e) {
				if (count == 0) {
					count = 1;
					actionApprove(driver, param1, param2, fetchMetadataVO,fetchConfigVO, customerDetails,count);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					datePicker(driver, param1, param2,fetchMetadataVO.getInputValue(), fetchMetadataVO, fetchConfigVO,customerDetails,count);
					Thread.sleep(2000);
				} else {
					logger.error("XpathPerformance=> Failed During ActionApprove " + fetchMetadataVO.getScriptNumber());
					fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
					throw e;
				}
			}
		}

	else {
		throw new Exception("XpathLocation is null");
		}
	}
	
	public void clickFilter(WebDriver driver, String xpath1, String xpath2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails,int count) throws Exception {
		String scriptID = fetchMetadataVO.getScriptId();
		String lineNumber = fetchMetadataVO.getLineNumber();
		String testSetLine=fetchMetadataVO.getTestSetLineId();
		String	xpathlocation = service.getXpathParams(scriptID, lineNumber,testSetLine);
		if (xpathlocation != null) {
			String param1r = xpathlocation.replace("xpath1", xpath1).replace("xpath2", xpath2);
			String[] mainparams = param1r.split(";");
				try {
					WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
					wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(mainparams[1])));
					wait.until(ExpectedConditions.elementToBeClickable(By.xpath(mainparams[1])));
					fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
					return;
				} catch (Exception e) {
					try {
						WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
						wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(mainparams[0])));
						wait.until(ExpectedConditions.elementToBeClickable(By.xpath(mainparams[0])));
						WebElement waittill = driver.findElement(By.xpath(mainparams[0]));
						waittill.click();
						Thread.sleep(500);
						fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
						logger.info("XpathPerformance=> Sucessfully Clicked clickFilter " + fetchMetadataVO.getScriptNumber());
					}catch(Exception ele) {
						if (count == 0) {
							count = 1;
							clickFilter(driver, xpath1, xpath2,fetchMetadataVO, fetchConfigVO, customerDetails,count);
							Thread.sleep(2000);
						} else if (count <= 2) {
							count = count + 1;
							clickFilter(driver, xpath1, xpath2,fetchMetadataVO, fetchConfigVO, customerDetails,count);
							Thread.sleep(2000);
						} else {
							logger.error("XpathPerformance=> Failed During clickFilter " + fetchMetadataVO.getScriptNumber());
							fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
							throw e;
						}
					}
				}
		}

		else {
			throw new Exception("XpathLocation is null");
			}
		
	}
	
	public String copynumber(WebDriver driver, String inputParam1, String inputParam2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails,int count) throws Exception {

		String value = null;
		String scriptID = fetchMetadataVO.getScriptId();
		String lineNumber = fetchMetadataVO.getLineNumber();
		String testSetLine=fetchMetadataVO.getTestSetLineId();
		String	xpathlocation = service.getXpathParams(scriptID, lineNumber,testSetLine);
		if (xpathlocation != null) {
		try {
				String param1r = xpathlocation.replace("inputParam1", inputParam1).replace("inputParam2", inputParam2);
					WebElement webElement = driver.findElement(By.xpath(param1r));
					Actions actions = new Actions(driver);
					actions.moveToElement(webElement).build().perform();
					value = webElement.getAttribute("value");
					if(!"".equals(value)) {
						value = webElement.getText();
					}
					Thread.sleep(5000);
					String scripNumber = fetchMetadataVO.getScriptNumber();
					String testParamId = fetchMetadataVO.getTestScriptParamId();
					String testSetId = fetchMetadataVO.getTestSetLineId();
					dynamicnumber.saveCopyNumber(value, testParamId, testSetId);
					logger.info("XpathPerformance=> Sucessfully Clicked copynumber" + scripNumber);
					return value;
			} catch (Exception e) {
				if (count == 0) {
					count = 1;
					copynumber(driver,inputParam1, inputParam2, fetchMetadataVO, fetchConfigVO, customerDetails,count);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					copynumber(driver,inputParam1, inputParam2, fetchMetadataVO, fetchConfigVO, customerDetails,count);
					Thread.sleep(2000);
				} else {
					logger.error("XpathPerformance=> Failed During copynumber " + fetchMetadataVO.getScriptNumber());
					fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
					throw e;
				}
		    }
		}

		else {
			throw new Exception("XpathLocation is null");
			}
		return value;
	}
	
	public void loginApplication(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			String type1, String type2, String type3, String param1, String param2, String param3, String keysToSend,
			String value, CustomerProjectDto customerDetails,int count) throws Exception {
		String scriptID = fetchMetadataVO.getScriptId();
		String lineNumber = fetchMetadataVO.getLineNumber();
		String testSetLine=fetchMetadataVO.getTestSetLineId();
		String	xpathlocation = service.getXpathParams(scriptID, lineNumber,testSetLine);
		logger.info(" Xpath Location " + xpathlocation);
		if (xpathlocation != null) {
		try {
			String param1r = xpathlocation.replace("param1", param1).replace("param2", param2);
			String[] mainparams = param1r.split(";");
			String title1 = driver.getTitle();
			String param5 = "password";
			navigateUrl(driver, fetchConfigVO, fetchMetadataVO, customerDetails);
			loginPage(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails,mainparams[0]);
			Thread.sleep(1000);
			loginPage(driver, param5, value, fetchMetadataVO, fetchConfigVO, customerDetails,mainparams[1]);
			Thread.sleep(5000);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			String title2 = driver.getTitle();
			if (title1.equalsIgnoreCase(title2)) {
				fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
				throw new IOException("Failed during login page");
			}
			logger.info("XpathPerformance=> Succesfully login into application " + fetchMetadataVO.getScriptNumber());
		} catch (Exception e) {
			if (count == 0) {
				count = 1;
				loginApplication(driver, fetchConfigVO,fetchMetadataVO, type1, type2, type3, param1, param2, param3,keysToSend,
						value, customerDetails,count);
				Thread.sleep(2000);
			} else if (count <= 2) {
				count = count + 1;
				loginApplication(driver, fetchConfigVO,fetchMetadataVO, type1, type2, type3, param1, param2, param3,keysToSend,
						value, customerDetails,count);
				Thread.sleep(2000);
			} else {
				logger.error("XpathPerformance=> Failed During LoginIntoApplication " + fetchMetadataVO.getScriptNumber());
				fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
				throw e;
			}
		}
	 }

		else {
			throw new Exception("XpathLocation is null");
			}
	}
	public void navigateUrl(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails) {
		try {
			driver.navigate().to(fetchConfigVO.getApplication_url());
			driver.manage().window().maximize();
			deleteAllCookies(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			refreshPage(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			switchToActiveElement(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("XpathPerformance=> Successfully Navigated to the URL " + scripNumber);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("XpathPerformance=> failed to do navigate URl " + scripNumber);
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
		}
	}
	public void deleteAllCookies(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {
		try {
			driver.manage().deleteAllCookies();
			logger.info("XpathPerformance=> Successfully Deleted All The Cookies.");
		} catch (Exception e) {
			logger.error("XpathPerformance=> Failed To Delete All The Cookies.");
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}
	public void switchToActiveElement(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {
		try {
			driver.switchTo().activeElement();
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("XpathPerformance=> Switched to Element Successfully" + scripNumber);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("XpathPerformance=> Failed During switchToActiveElement Action." + scripNumber);
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}
	public void loginPage(WebDriver driver, String param1, String keysToSend, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails,String xpath) {
		try {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
				WebElement waittill = driver.findElement(By.xpath(xpath));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				logger.info("Succesfully "+param1+" is entered " );
		} catch (Exception e) {
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("XpathPerformance=> Failed to enter "+param1+" "+ scripNumber);
		}
	}
	
	public void logout(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO, String type1,
			String type2, String type3, String param1, String param2, String param3, CustomerProjectDto customerDetails,int count,String xpathlocation,int totalXpaths)
			throws Exception {
		String scriptID = fetchMetadataVO.getScriptId();
		String lineNumber = fetchMetadataVO.getLineNumber();
		String testSetLine=fetchMetadataVO.getTestSetLineId();
		String[] mainparams=null;
		if(xpathlocation==null) {
			xpathlocation = service.getXpathParams(scriptID, lineNumber,testSetLine);
			if (xpathlocation != null) {
				mainparams = xpathlocation.split(";");
				totalXpaths=mainparams.length;
			}
		}
		if (xpathlocation != null) {
		try {
			mainparams = xpathlocation.split(";");
			for(int i=0;i<mainparams.length;i++) {
					WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
					wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(mainparams[0])));
					WebElement waittext = driver.findElement(By.xpath(mainparams[0]));
					Actions actions = new Actions(driver);
					actions.moveToElement(waittext).build().perform();
					clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO,customerDetails);
					Thread.sleep(4000);
					mainparams=ArrayUtils.removeElement(mainparams, mainparams[i]);
					i--;
					if(totalXpaths==2 && (mainparams.length-1)==i) {
						String param6 = " Confirm";
						clickSignInSignOut(driver, param6, fetchMetadataVO, fetchConfigVO, customerDetails);
					}
			}
		} catch (Exception e) {
			if (count == 0) {
				count = 1;
				xpathlocation=String.join(";", mainparams);
				logout(driver, fetchConfigVO, fetchMetadataVO,type1, type2, type3, param1, param2, param3, customerDetails,count,xpathlocation,totalXpaths);
				Thread.sleep(2000);
			} else if (count <= 2) {
				count = count + 1;
				xpathlocation=String.join(";", mainparams);
				logout(driver, fetchConfigVO, fetchMetadataVO,type1, type2, type3, param1, param2, param3, customerDetails,count,xpathlocation,totalXpaths);
				Thread.sleep(2000);
			} else {
				logger.error("XpathPerformance=> Failed During logout " + fetchMetadataVO.getScriptNumber());
				fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
				throw e;
			}
		 }
		}
		else {
			throw new Exception("XpathLocation is null");
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
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO,customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("XpathPerformance=> Sucessfully clicked SingnInSignOut" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("XpathPerformance=> Failed during SingnInSignOut " + scripNumber);
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}
}