package com.winfo.scripts;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.objectstorage.responses.PutObjectResponse;
import com.winfo.exception.WatsEBSCustomException;
import com.winfo.interface1.AbstractSeleniumKeywords;
import com.winfo.services.DynamicRequisitionNumber;
import com.winfo.services.FetchConfigVO;
import com.winfo.services.FetchMetadataVO;
import com.winfo.services.ScriptXpathService;
import com.winfo.vo.CustomerProjectDto;
import com.winfo.vo.ScriptDetailsDto;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

@Service
public class XpathPerformance {

	Logger log = Logger.getLogger("Logger");
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

	public String screenshot(WebDriver driver, String screenshotName, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {

		String image_dest = null;

		try {

			TakesScreenshot ts = (TakesScreenshot) driver;

			File source = ts.getScreenshotAs(OutputType.FILE);

			image_dest = (fetchConfigVO.getScreenshot_path() + customerDetails.getCustomerName() + "/"

					+ customerDetails.getTestSetName() + "/" + fetchMetadataVO.getSeqNum() + "_"

					+ fetchMetadataVO.getLineNumber() + "_" + fetchMetadataVO.getScenarioName() + "_"

					+ fetchMetadataVO.getScriptNumber() + "_" + customerDetails.getTestSetName() + "_"

					+ fetchMetadataVO.getLineNumber() + "_Passed").concat(".jpg");

			System.out.println(image_dest);

			File destination = new File(image_dest);

			if (!destination.exists()) {

				System.out.println("creating directory: " + destination.getName());

				boolean result = false;

				try {

					destination.mkdirs();

					result = true;

				} catch (SecurityException se) {

					// handle it

					System.out.println(se.getMessage());

				}

			} else {

				System.out.println("Folder exist");

			}

			// FileUtils.copyFile(source, destination);

//			Files.copy(FileSystems.getDefault().getPath(source.getPath()), FileSystems.getDefault().getPath(destination.getPath()), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);

			Files.copy(source.toPath(),

					destination.toPath(), StandardCopyOption.COPY_ATTRIBUTES,

					StandardCopyOption.REPLACE_EXISTING);

			log.info("Successfully Screenshot is taken");

			return image_dest;

		} catch (Exception e) {

			log.error("Failed During Taking screenshot");

			System.out.println("Exception while taking Screenshot" + e.getMessage());

			return e.getMessage();

		}

	}

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
				System.out.println("it's working sendValue.......");

				return;
			} catch (Exception e) {

				if (count == 0) {
					Thread.sleep(2000);
					count = 1;
					System.out.println(" The Count Value is : " + count);
					sendValue(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, count,
							customerDetails);

				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					System.out.println(" The Count Value is : " + count);
					sendValue(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, count,
							customerDetails);
				} else {
					System.out.println("Count value exceeds the limit");
					log.error("Failed During SendValue");
//					screenshotFail(driver, "Failed during SendValue Method", fetchMetadataVO, fetchConfigVO);
					System.out.println("Not able to SendValue to the :" + "" + param1);
					throw e;
				}
			}

		} else {
			throw new Exception("XpathLocation is null");
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
				System.out.println("it's working clickButton.......");

			} catch (Exception e) {

				if (count == 0) {
					count = 1;
					System.out.println(" The Count Value is : " + count);
					clickButton(driver, param1, param2, fetchMetadataVO, fetchConfigVO, count, customerDetails);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					System.out.println(" The Count Value is : " + count);
					clickButton(driver, param1, param2, fetchMetadataVO, fetchConfigVO, count, customerDetails);
				} else {
					System.out.println("Count value exceeds the limit");
					log.error("Failed During SendValue");
//					screenshotFail(driver, "Failed during SendValue Method", fetchMetadataVO, fetchConfigVO);
					System.out.println("Not able to SendValue to the :" + "" + param1);
					throw e;
				}
			}

		} else {
			throw new Exception("XpathLocation is null");
		}
	}

	private void clickValidateXpath(WebDriver driver, ScriptDetailsDto fetchMetadataVO, WebElement waittext,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {
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

	public String textarea(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, int count) throws Exception {

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
				System.out.println("it's working textarea.......");

				return keysToSend;
			} catch (Exception e) {
				if (count == 0) {
					count = 1;
					System.out.println(" The Count Value is : " + count);
					textarea(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, count);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					System.out.println(" The Count Value is : " + count);
					textarea(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, count);
				} else {
					System.out.println("Count value exceeds the limit");
					log.error("Failed During SendValue");
//					screenshotFail(driver, "Failed during SendValue Method", fetchMetadataVO, fetchConfigVO);
					System.out.println("Not able to SendValue to the :" + "" + param1);
					throw e;
				}
			}
		} else {
			throw new Exception("XpathLocation is null");
		}
		return keysToSend;
	}

	public void tableSendKeys(WebDriver driver, String param1, String param2, String param3, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, int count) throws Exception {
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
				System.out.println("it's working TableSendkeys.......");

				return;
			} catch (Exception e) {
				if (count == 0) {
					count = 1;
					System.out.println(" The Count Value is : " + count);
					tableSendKeys(driver, param1, param2, param3, keysToSend, fetchMetadataVO, fetchConfigVO, count);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					System.out.println(" The Count Value is : " + count);
					tableSendKeys(driver, param1, param2, param3, keysToSend, fetchMetadataVO, fetchConfigVO, count);
				} else {
					System.out.println("Count value exceeds the limit");
					log.error("Failed During SendValue");
//						screenshotFail(driver, "Failed during SendValue Method", fetchMetadataVO, fetchConfigVO);
					System.out.println("Not able to SendValue to the :" + "" + param1);
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
				System.out.println("it's working multiTableSendkeys.......");

				return;
			} catch (Exception e) {
				if (count == 0) {
					count = 1;
					System.out.println(" The Count Value is : " + count);
					multiplelinestableSendKeys(driver, param1, param2, param3, keysToSend, fetchMetadataVO,
							fetchConfigVO, count, customerDetails);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					System.out.println(" The Count Value is : " + count);
					multiplelinestableSendKeys(driver, param1, param2, param3, keysToSend, fetchMetadataVO,
							fetchConfigVO, count, customerDetails);
				} else {
					System.out.println("Count value exceeds the limit");
					log.error("Failed During SendValue");
//					screenshotFail(driver, "Failed during SendValue Method", fetchMetadataVO, fetchConfigVO);
					System.out.println("Not able to SendValue to the :" + "" + param1);
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
				System.out.println("it's working clickLinkAction.......");

			} catch (Exception e) {

				if (count == 0) {
					count = 1;
					System.out.println(" The Count Value is : " + count);
					clickLinkAction(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, count,
							customerDetails);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					System.out.println(" The Count Value is : " + count);
					clickLinkAction(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, count,
							customerDetails);
				} else {
					System.out.println("Count value exceeds the limit");
					log.error("Failed During SendValue");
//					screenshotFail(driver, "Failed during SendValue Method", fetchMetadataVO, fetchConfigVO);
					System.out.println("Not able to SendValue to the :" + "" + param1);
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
				System.out.println("it's working clickTablelink.......");

			} catch (Exception e) {

				if (count == 0) {
					count = 1;
					System.out.println(" The Count Value is : " + count);
					clickTableLink(driver, param1, param2, fetchMetadataVO, fetchConfigVO, count, customerDetails);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					System.out.println(" The Count Value is : " + count);
					clickTableLink(driver, param1, param2, fetchMetadataVO, fetchConfigVO, count, customerDetails);
				} else {
					System.out.println("Count value exceeds the limit");
					log.error("Failed During SendValue");
//					screenshotFail(driver, "Failed during SendValue Method", fetchMetadataVO, fetchConfigVO);
					System.out.println("Not able to SendValue to the :" + "" + param1);
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
		log.info("xpathlocation is" +xpathlocation);
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
				System.out.println("it's working ClickLink.......");

			} catch (Exception e) {
				if (count == 0) {
					count = 1;
					System.out.println(" The Count Value is : " + count);
					clickLink(driver, param1, param2, fetchMetadataVO, fetchConfigVO, count, customerDetails);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					System.out.println(" The Count Value is : " + count);
					clickLink(driver, param1, param2, fetchMetadataVO, fetchConfigVO, count, customerDetails);
				} else {
					System.out.println("Count value exceeds the limit");
					log.error("Failed During SendValue");
//					screenshotFail(driver, "Failed during SendValue Method", fetchMetadataVO, fetchConfigVO);
					System.out.println("Not able to SendValue to the :" + "" + param1);
					throw e;
				}
			}

		} else {
			throw new Exception("XpathLocation is null");
		}

	}

	public String screenshotFail(WebDriver driver, String screenshotName, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {
		String image_dest = null;
		try {
			TakesScreenshot ts = (TakesScreenshot) driver;
			File source = ts.getScreenshotAs(OutputType.FILE);
			String fileExtension = source.getName();
			fileExtension = fileExtension.substring(fileExtension.indexOf("."));

			String currenttime = new SimpleDateFormat("MM-dd-yyyy HH-mm-ss").format(Calendar.getInstance().getTime());
			image_dest = (fetchConfigVO.getScreenshot_path() + customerDetails.getCustomerName() + "/"
					+ customerDetails.getTestSetName() + "/" + fetchMetadataVO.getSeqNum() + "_"
					+ fetchMetadataVO.getLineNumber() + "_" + fetchMetadataVO.getScenarioName() + "_"
					+ fetchMetadataVO.getScriptNumber() + "_" + customerDetails.getTestSetName() + "_"
					+ fetchMetadataVO.getLineNumber() + "_Failed").concat(fileExtension);
			File destination = new File(image_dest);

			if (!destination.exists()) {

				System.out.println("creating directory: " + destination.getName());

				boolean result = false;

				try {

					destination.mkdirs();

					result = true;

				} catch (SecurityException se) {

					// handle it

					System.out.println(se.getMessage());

				}

			} else {

				System.out.println("Folder exist");

			}

			// FileUtils.copyFile(source, destination);

//			Files.copy(FileSystems.getDefault().getPath(source.getPath()), FileSystems.getDefault().getPath(destination.getPath()), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);

			Files.copy(source.toPath(),

					destination.toPath(), StandardCopyOption.COPY_ATTRIBUTES,

					StandardCopyOption.REPLACE_EXISTING);

			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Successfully Failed Screenshot is Taken " + scripNumber);
			return image_dest;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during screenshotFail Action. " + scripNumber);
			System.out.println("Exception while taking Screenshot" + e.getMessage());
			return e.getMessage();
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
			screenshotFail(driver, "Failed during refreshPage Method", fetchMetadataVO, fetchConfigVO, customerDetails);
			System.out.println("can not refresh the page");
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
				System.out.println("it's working clickImage.......");

			} catch (Exception e) {

				if (count == 0) {
					count = 1;
					System.out.println(" The Count Value is : " + count);
					clickImage(driver, param1, param2, fetchMetadataVO, fetchConfigVO, count, customerDetails);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					System.out.println(" The Count Value is : " + count);
					clickImage(driver, param1, param2, fetchMetadataVO, fetchConfigVO, count, customerDetails);
				} else {
					System.out.println("Count value exceeds the limit");
					log.error("Failed During SendValue");
//					screenshotFail(driver, "Failed during SendValue Method", fetchMetadataVO, fetchConfigVO);
					System.out.println("Not able to SendValue to the :" + "" + param1);
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
				System.out.println("it's working clicktableImage.......");

			} catch (Exception e) {

				if (count == 0) {
					count = 1;
					System.out.println(" The Count Value is : " + count);
					clickTableImage(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, count,
							customerDetails);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					System.out.println(" The Count Value is : " + count);
					clickTableImage(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, count,
							customerDetails);
				} else {
					System.out.println("Count value exceeds the limit");
					log.error("Failed During SendValue");
//					screenshotFail(driver, "Failed during SendValue Method", fetchMetadataVO, fetchConfigVO);
					System.out.println("Not able to SendValue to the :" + "" + param1);
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
				System.out.println("It's working in tableRowSelect..... ");
			} catch (Exception e) {

				if (count == 0) {
					count = 1;
					System.out.println(" The Count Value is : " + count);
					tableRowSelect(driver, param1, param2, fetchMetadataVO, fetchConfigVO, count, customerDetails);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					System.out.println(" The Count Value is : " + count);
				} else {
					System.out.println("Count value exceeds the limit");
					log.error("Failed During SendValue");
//					screenshotFail(driver, "Failed during SendValue Method", fetchMetadataVO, fetchConfigVO);
					System.out.println("Not able to SendValue to the :" + "" + param1);
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
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
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
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Successfully Clicked ClickButtonDropdownText" + scripNumber);
			return;
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed During clickButtonDropdownText " + scripNumber);
			screenshotFail(driver, "Failed during clickLink Method", fetchMetadataVO, fetchConfigVO, customerDetails);
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
				System.out.println("It's working in clickButtonDropdown..... ");
			} catch (Exception e) {

				if (count == 0) {
					count = 1;
					System.out.println(" The Count Value is : " + count);
					clickButtonDropdown(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, count,
							customerDetails);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					System.out.println(" The Count Value is : " + count);
					clickButtonDropdown(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, count,
							customerDetails);
				} else {
					System.out.println("Count value exceeds the limit");
					log.error("Failed During SendValue");
//					screenshotFail(driver, "Failed during SendValue Method", fetchMetadataVO, fetchConfigVO);
					System.out.println("Not able to SendValue to the :" + "" + param1);
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
		log.info("Sucessfully Clicked selectMethod" + scripNumber);
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
				System.out.println("It's working in selectBytext..... ");
			} catch (Exception e) {

				if (count == 0) {
					count = 1;
					Thread.sleep(2000);
					System.out.println(" The Count Value is : " + count);
					selectByText(driver, param1, param2, inputData, fetchMetadataVO, fetchConfigVO, count,
							customerDetails);

				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					System.out.println(" The Count Value is : " + count);
					selectByText(driver, param1, param2, inputData, fetchMetadataVO, fetchConfigVO, count,
							customerDetails);
				} else {
					System.out.println("Count value exceeds the limit");
					log.error("Failed During SendValue");
//					screenshotFail(driver, "Failed during SendValue Method", fetchMetadataVO, fetchConfigVO);
					System.out.println("Not able to SendValue to the :" + "" + param1);
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
				System.out.println("It's working in tableDropdownValues..... ");
			} catch (Exception e) {

				if (count == 0) {
					count = 1;
					System.out.println(" The Count Value is : " + count);
					tableDropdownValues(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, count,
							customerDetails);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					System.out.println(" The Count Value is : " + count);
					tableDropdownValues(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, count,
							customerDetails);
				} else {
					System.out.println("Count value exceeds the limit");
					log.error("Failed During SendValue");
//					screenshotFail(driver, "Failed during SendValue Method", fetchMetadataVO, fetchConfigVO);
					System.out.println("Not able to SendValue to the :" + "" + param1);
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
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
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
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
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
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
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
			screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, fetchConfigVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  tableDropdownTexts" + scripNumber);
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
			screenshotFail(driver, "Failed during Enter Method", fetchMetadataVO, fetchConfigVO, customerDetails);
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
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
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
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
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
				String scriptID = fetchMetadataVO.getScriptId();
				log.info("Sucessfully Clicked dropdownTexts" + scriptID);
			} catch (Exception e) {
				WebElement searchResult = driver.findElement(By.xpath(
						"//div[contains(@id,'PopupId::content')]//*[text()='Search']/following::*[text()='Name']/following::input[1]"));
				typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
				String scriptID = fetchMetadataVO.getScriptId();
				log.error("Failed during dropdownTexts" + scriptID);
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
					log.info("Sucessfully Clicked dropdownTexts" + scriptID);
				} catch (Exception e) {
					WebElement text = driver
							.findElement(By.xpath("(//span[contains(text(),'" + keysToSend + "')])[1]"));
					text.click();
					String scriptID = fetchMetadataVO.getScriptId();
					log.error("Failed during dropdownTexts" + scriptID);
				}
			}
			try {
				WebElement button = driver
						.findElement(By.xpath("//*[text()='Search']/following::*[normalize-space(text())='" + param2
								+ "']/following::*[not (@aria-disabled) and text()='K'][1]"));
				String scriptID = fetchMetadataVO.getScriptId();
				log.info("Sucessfully Clicked dropdownTexts" + scriptID);
				button.click();
			} catch (Exception e) {
				WebElement button = driver
						.findElement(By.xpath("//*[text()='Search']/following::*[normalize-space(text())='" + param2
								+ "']/following::*[not (@aria-disabled) and text()='OK'][1]"));
				button.click();
				String scriptID = fetchMetadataVO.getScriptId();
				log.error("Failed during dropdownTexts" + scriptID);
			}

			return;
		} catch (Exception e) {
			System.out.println(e);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during dropdownTexts" + scripNumber);
			screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, fetchConfigVO, customerDetails);
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
							actions.moveToElement(waittext).click().build().perform();
							fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
							String scripNumber = fetchMetadataVO.getScriptNumber();
							log.info("Successfully dropdownValues step-"+i+" is done " + scripNumber);
							mainparams=ArrayUtils.removeElement(mainparams, mainparams[i]);
							i--;
				}
				return;
			} catch (Exception e) {

				if (count == 0) {
					count = 1;
					System.out.println(" The Count Value is : " + count);
					xpathlocation=String.join(";", mainparams);
					dropdownValues(driver, param1, param2, param3, keysToSend, fetchMetadataVO, fetchConfigVO, count,
							customerDetails,xpathlocation);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					System.out.println(" The Count Value is : " + count);
					xpathlocation=String.join(";", mainparams);
					dropdownValues(driver, param1, param2, param3, keysToSend, fetchMetadataVO, fetchConfigVO, count,
							customerDetails,xpathlocation);
				} else {
					System.out.println("Count value exceeds the limit");
					log.error("Failed During dropdownValues");
					fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
					System.out.println("Not able to dropdownValues to the :" + "" + param1);
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
				System.out.println("It's working in clickCheckox..... ");

			} catch (Exception e) {

				if (count == 0) {
					count = 1;
					System.out.println(" The Count Value is : " + count);
					clickCheckbox(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO, count, customerDetails);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					System.out.println(" The Count Value is : " + count);
					clickCheckbox(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO, count, customerDetails);
				} else {
					System.out.println("Count value exceeds the limit");
					log.error("Failed During SendValue");
//					screenshotFail(driver, "Failed during SendValue Method", fetchMetadataVO, fetchConfigVO);
					System.out.println("Not able to SendValue to the :" + "" + param1);
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
			System.out.println("It's working in Radiobutton..... ");
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
					System.out.println(" The Count Value is : " + count);
					clickRadiobutton(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, count,
							customerDetails);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					System.out.println(" The Count Value is : " + count);
					clickCheckbox(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO, count, customerDetails);
				} else {
					System.out.println("Count value exceeds the limit");
					log.error("Failed During SendValue");
//					screenshotFail(driver, "Failed during SendValue Method", fetchMetadataVO, fetchConfigVO);
					System.out.println("Not able to SendValue to the :" + "" + param1);
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
			log.info("Sucessfully Clicked tab" + scripNumber);
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  tab" + scripNumber);
			screenshotFail(driver, "Failed during tab Method", fetchMetadataVO, fetchConfigVO, customerDetails);
			System.out.println("Failed to do TAB Action");
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
				System.out.println("It's working in SelectAvalue..... ");
			} catch (Exception e) {

				if (count == 0) {
					count = 1;
					System.out.println(" The Count Value is : " + count);
					selectAValue(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, count,
							customerDetails);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					System.out.println(" The Count Value is : " + count);
					selectAValue(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, count,
							customerDetails);
				} else {
					System.out.println("Count value exceeds the limit");
					log.error("Failed During SendValue");
//					screenshotFail(driver, "Failed during SendValue Method", fetchMetadataVO, fetchConfigVO);
					System.out.println("Not able to SendValue to the :" + "" + param1);
					throw e;
				}
			}

		} else {
			throw new Exception("XpathLocation is null");
		}
	}

	public synchronized void navigate(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			String type1, String type2, String param1, String param2, int count, CustomerProjectDto customerDetails,String xpathlocation)
			throws Exception {
		String scriptID = fetchMetadataVO.getScriptId();
		String lineNumber = fetchMetadataVO.getLineNumber();
		String testSetLine=fetchMetadataVO.getTestSetLineId();
		String[] mainparams=null;
		if(xpathlocation==null) {
			String param3 = "Navigator";
			navigator(driver, param3, fetchMetadataVO, fetchConfigVO, customerDetails);
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
							actions.moveToElement(waittext).click().build().perform();
							fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
							String scripNumber = fetchMetadataVO.getScriptNumber();
							log.info("Successfully Navigation step-"+i+" is done " + scripNumber);
//							mainparams=Arrays.stream(mainparams).filter(s -> !s.equals(mainparam)).toArray(String[]::new);
							mainparams=ArrayUtils.removeElement(mainparams, mainparams[i]);
							i--;
				}
				return;
			} catch (Exception e) {
				if (count == 0) {
					count = 1;
					xpathlocation=String.join(";", mainparams);
					navigate(driver, fetchConfigVO,
							fetchMetadataVO, type1, type2, param1, param2, count, customerDetails,xpathlocation);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					xpathlocation=String.join(";", mainparams);
					navigate(driver, fetchConfigVO,
							fetchMetadataVO, type1, type2, param1, param2, count, customerDetails,xpathlocation);
				} else {
					log.error("Failed During Navigate");
					fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
					throw e;
				}
			}
		}

		else {
			throw new Exception("XpathLocation is null");
		}
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
//		String xpaths=xpath+";"+xpath1;
//		String scripNumber=fetchMetadataVO.getScript_number();
//		service.saveXpathParams(param1,param2,scripNumber,xpaths);
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
			log.info("Successfully navigator is done " + scripNumber);
			String xpath = "//a[@title='param1']";
			return xpath;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during navigator " + scripNumber);
			screenshotFail(driver, "Failed during navigateUrl Method", fetchMetadataVO, fetchConfigVO, customerDetails);
			System.out.println("Not able to navitage to the Url");
			throw e;
		}
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
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Successfully task is open " + scripNumber);
			String xpath = "//img[@title='param1']";
			log.info("Successfully task is open " + scripNumber);
			return xpath;

		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Failed During Task " + scripNumber);
			screenshotFail(driver, "Failed to Open Task Menu", fetchMetadataVO, fetchConfigVO, customerDetails);
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
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			Thread.sleep(5000);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
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
				screenshotFail(driver, "Failed to Open Task Menu", fetchMetadataVO, fetchConfigVO, customerDetails);
				System.out.println("Failed to Open Task Menu");
				throw e;

			}
		}
		return xpath;
	}

	public void clickNotificationLink(WebDriver driver, String param1, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, int count, CustomerProjectDto customerDetails) throws Exception {
		String scriptID = fetchMetadataVO.getScriptId();
		String action = fetchMetadataVO.getAction();

		String lineNumber = fetchMetadataVO.getLineNumber();
//		String xpathlocation = service.getXpathParams(scriptID, lineNumber);
		String testSetLine=fetchMetadataVO.getTestSetLineId();
		String xpathlocation = service.getXpathParams(scriptID, lineNumber,testSetLine);
		if (xpathlocation != null) {
			String param1r = xpathlocation.replace("param1", param1);

			try {

				WebElement waittext = driver.findElement(By.xpath(param1r));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
				Thread.sleep(2000);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				log.info("Successfully Clicked NotificationLink" + scripNumber);
				return;
			} catch (Exception e) {
				if (count == 0) {
					count = 1;
					System.out.println(" The Count Value is : " + count);
					clickNotificationLink(driver, param1, fetchMetadataVO, fetchConfigVO, count, customerDetails);

					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					System.out.println(" The Count Value is : " + count);
					clickNotificationLink(driver, param1, fetchMetadataVO, fetchConfigVO, count, customerDetails);

				} else {
					System.out.println("Count value exceeds the limit");
					log.error("Failed During SendValue");
//				screenshotFail(driver, "Failed during SendValue Method", fetchMetadataVO, fetchConfigVO);
					System.out.println("Not able to SendValue to the :" + "" + param1);
					throw e;
				}
			}

		} else {
			throw new Exception("XpathLocation is null");
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

				log.info("Sucessfully clicked Element in clickmenu " + scripNumber);
				return;

			} catch (Exception e) {
				if (count == 0) {
					count = 1;
					System.out.println(" The Count Value is : " + count);
					clickMenu(driver, param1, param2, fetchMetadataVO, fetchConfigVO, count, customerDetails);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					System.out.println(" The Count Value is : " + count);
					clickMenu(driver, param1, param2, fetchMetadataVO, fetchConfigVO, count, customerDetails);
				} else {
					System.out.println("Count value exceeds the limit");
					log.error("Failed During SendValue");
//				screenshotFail(driver, "Failed during SendValue Method", fetchMetadataVO, fetchConfigVO);
					System.out.println("Not able to SendValue to the :" + "" + param1);
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
//			String xpath="(//table[@role='presentation']/following::a[normalize-space(text())='" + param1 + "'])[1]";
//					service.saveXpathParams(param1,param2,scripNumber,xpath);
			log.info("Sucessfully Clicked mousehover" + scripNumber);
			return;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.error("Failed during  mousehover" + scripNumber);
			System.out.println(e);
			screenshotFail(driver, "Failed during MouseHover Method", fetchMetadataVO, fetchConfigVO, customerDetails);
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
				log.info("Sucessfully Clicked scrollUsingElement" + scripNumber);
				return;
			} catch (Exception e) {
				if (count == 0) {
					count = 1;
					System.out.println(" The Count Value is : " + count);
					scrollUsingElement(driver, fetchMetadataVO.getInputParameter(), fetchMetadataVO, fetchConfigVO,
							count, customerDetails);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					System.out.println(" The Count Value is : " + count);
					scrollUsingElement(driver, fetchMetadataVO.getInputParameter(), fetchMetadataVO, fetchConfigVO,
							count, customerDetails);
				} else {
					System.out.println("Count value exceeds the limit");
					log.error("Failed During SendValue");
//				screenshotFail(driver, "Failed during SendValue Method", fetchMetadataVO, fetchConfigVO);
					System.out.println("Not able to SendValue to the :" + "" + inputParam);
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
		log.info("Sucessfully Clicked scrollMethod" + scripNumber);
	}

	public void switchToFrame(WebDriver driver, String inputParam, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, int count) throws Exception {
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
					System.out.println(" The Count Value is : " + count);
					switchToFrame(driver, fetchMetadataVO.getInputParameter(), fetchMetadataVO, fetchConfigVO, count);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					System.out.println(" The Count Value is : " + count);
					switchToFrame(driver, fetchMetadataVO.getInputParameter(), fetchMetadataVO, fetchConfigVO, count);
				} else {
					System.out.println("Count value exceeds the limit");
					log.error("Failed During SendValue");
//				screenshotFail(driver, "Failed during SendValue Method", fetchMetadataVO, fetchConfigVO);
					System.out.println("Not able to SendValue to the :" + "" + inputParam);
					throw e;
				}
			}

		} else {
			throw new Exception("XpathLocation is null");
		}
	}

	public void clear(WebDriver driver, String inputParam1, String inputParam2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, int count) throws Exception {

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
					System.out.println(" The Count Value is : " + count);
					clear(driver, inputParam1, inputParam2, fetchMetadataVO, fetchConfigVO, count);
					Thread.sleep(2000);
				} else if (count <= 2) {
					count = count + 1;
					Thread.sleep(2000);
					System.out.println(" The Count Value is : " + count);
					clear(driver, inputParam1, inputParam2, fetchMetadataVO, fetchConfigVO, count);
				} else {
					System.out.println("Count value exceeds the limit");
					log.error("Failed During SendValue");
//				screenshotFail(driver, "Failed during SendValue Method", fetchMetadataVO, fetchConfigVO);
					System.out.println("Not able to SendValue to the :" + "" + inputParam1);
					throw e;
				}
			}

		} else {
			throw new Exception("XpathLocation is null");
		}
	}

	public void clearMethod(WebDriver driver, WebElement waittill) {
		WebDriverWait wait = new WebDriverWait(driver, 60);
		wait.until(ExpectedConditions.elementToBeClickable(waittill));
		waittill.click();
		waittill.clear();
		log.info("clear and typed the given Data");
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
			
			
			Screenshot s=new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(driver);
	        File file = new File(System.getProperty("java.io.tmpdir")+File.separator+imageName+PNG_EXTENSION);
	        ImageIO.write(s.getImage(),"PNG",file);
	        
	        uploadObjectToObjectStore(file.getCanonicalPath(), folderName, imageName);

	        log.info("Successfully Screenshot is taken " + imageName);
			return folderName + FORWARD_SLASH + imageName;

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Failed During Taking screenshot");
			log.error("Exception while taking Screenshot" + e.getMessage());
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
			Screenshot s=new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(driver);
	        File file = new File(System.getProperty("java.io.tmpdir")+File.separator+imageName);
	        ImageIO.write(s.getImage(),"PNG",file);
			uploadObjectToObjectStore(file.getCanonicalPath(), folderName, imageName);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			log.info("Successfully Failed Screenshot is Taken " + scripNumber);
			return folderName + FORWARD_SLASH + imageName;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			e.printStackTrace();
			log.error("Failed during screenshotFail Action. " + scripNumber);
			log.error("Exception while taking Screenshot" + e.getMessage());
			return e.getMessage();
//			throw e;
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
		} catch (WatsEBSCustomException e) {
			throw e;
		} catch (Exception e) {
			throw new WatsEBSCustomException(500, "Exception occured while uploading pdf in Object Storage..", e);
		}
	}
}