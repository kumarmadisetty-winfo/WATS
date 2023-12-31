package com.winfo.scripts;

import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.jfree.util.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.itextpdf.text.DocumentException;
import com.winfo.service.SeleniumKeyWordsInterface;
import com.winfo.serviceImpl.AbstractSeleniumKeywords;
import com.winfo.serviceImpl.ScriptXpathService;
import com.winfo.utils.FileUtil;
import com.winfo.utils.StringUtils;
import com.winfo.vo.ApiValidationVO;
import com.winfo.vo.CustomerProjectDto;
import com.winfo.vo.FetchConfigVO;
import com.winfo.vo.ScriptDetailsDto;

@Service("ARLO")
public class ARLOSeleniumKeywords extends AbstractSeleniumKeywords implements SeleniumKeyWordsInterface {

	public static final Logger logger = Logger.getLogger(ARLOSeleniumKeywords.class);

	@Autowired
	ScriptXpathService service;

	@Autowired
	private RestTemplate restTemplate;

	public void loginApplication(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			String type1, String type2, String type3, String param1, String param2, String param3, String keysToSend,
			String value, CustomerProjectDto customerDetails) throws Exception {
		String param5 = "Password";
		String param6 = "submit";
		navigateUrl(driver, fetchConfigVO, fetchMetadataVO, customerDetails);
		sendValue(driver, param1, param3, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
		sendValue(driver, param5, param2, value, fetchMetadataVO, fetchConfigVO, customerDetails);
		clickSignIn(driver, param6, fetchMetadataVO, fetchConfigVO, customerDetails);
//		clickButton(driver, param6, param2, fetchMetadataVO, fetchConfigVO);
	}

	public void navigate(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO, String type1,
			String type2, String param1, String param2, String param3, int count, CustomerProjectDto customerDetails)
			throws Exception {
		//Text
		String param = "Navigator";
		clickNavigate(driver, param, fetchMetadataVO, fetchConfigVO, customerDetails);
		clickNavigateMenu(driver, param1, param2, fetchMetadataVO, fetchConfigVO, customerDetails);
		// text
	//	clickLink(driver, param, param2, fetchMetadataVO, fetchConfigVO, customerDetails);
	//	clickMenu(driver, param1, param2, fetchMetadataVO, fetchConfigVO, customerDetails);
	}
	
	public void clickNavigate(WebDriver driver, String param1, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//a[@title=\"" + param1 + "\"]"))));
			WebElement waittext = driver.findElement(By.xpath(("//a[@title=\"" + param1 + "\"]")));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(5000);
			String xpath = "//a[@title=\"param1\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
		} catch (Exception e) {
			logger.error(e.getMessage());
			Thread.currentThread().interrupt();
			logger.error("Failed during Navigate action " + fetchMetadataVO.getScriptNumber());
			takeScreenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}
	public void clickNavigateMenu(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		if (param1.equalsIgnoreCase("Setup and Maintenance")) {
			try {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//h1[normalize-space(text())=\"Navigator\"]/following::*[normalize-space(text())=\""
								+ param1 + "\"]")));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(
						By.xpath("//h1[normalize-space(text())=\"Navigator\"]/following::*[normalize-space(text())=\""
								+ param1 + "\"]"),
						param1));
				WebElement waittext = driver.findElement(
						By.xpath("//h1[normalize-space(text())=\"Navigator\"]/following::*[normalize-space(text())=\""
								+ param1 + "\"]"));
				highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				takeScreenshot(driver, fetchMetadataVO, customerDetails);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//h1[normalize-space(text())=\"Navigator\"]/following::*[normalize-space(text())=\"param1\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} catch (Exception e) {
				logger.error("Failed During Menu Navigation " +  e.getMessage());
				Thread.currentThread().interrupt();
			}
		}try {
			if(param2.equalsIgnoreCase("Annual Awards Merit,Bonus,St...")) {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//h1[normalize-space(text())=\"Navigator\"]/following::a[normalize-space(text())=\"Annual Awards (Merit,Bonus,St...\"]")));
//		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h1[normalize-space(text())=\"Navigator\"]/following::*[normalize-space(text())=\""+param1+"\"]/following::a[normalize-space(text())=\""+param2+"\"]"), param2));
			WebElement waittext = driver.findElement(
					By.xpath("//h1[normalize-space(text())=\"Navigator\"]/following::a[normalize-space(text())=\"Annual Awards (Merit,Bonus,St...\"]"));
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			takeScreenshot(driver, fetchMetadataVO, customerDetails);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//h1[normalize-space(text())=\"Navigator\"]/following::a[normalize-space(text())=\"Annual Awards (Merit,Bonus,St...\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
			}
		} catch (Exception e) {
			logger.error("Failed During Menu Navigation " +  e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//h1[normalize-space(text())=\"Navigator\"]/following::a[normalize-space(text())=\"" + param2 + "\"]")));
//		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h1[normalize-space(text())=\"Navigator\"]/following::*[normalize-space(text())=\""+param1+"\"]/following::a[normalize-space(text())=\""+param2+"\"]"), param2));
			WebElement waittext = driver.findElement(
					By.xpath("//h1[normalize-space(text())=\"Navigator\"]/following::a[normalize-space(text())=\"" + param2 + "\"]"));
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			takeScreenshot(driver, fetchMetadataVO, customerDetails);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//h1[normalize-space(text())=\"Navigator\"]/following::a[normalize-space(text())=\"param2\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed During Menu Navigation " +  e.getMessage());
			Thread.currentThread().interrupt();
		}try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//a[text()=\"" + param2 + "\"]"))));
			WebElement waittext = driver.findElement(By.xpath(("//a[text()=\"" + param2 + "\"]")));
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			takeScreenshot(driver, fetchMetadataVO, customerDetails);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(5000);
			String xpath = "//a[text()=\"param2\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
		} catch (Exception e) {
			logger.error(e.getMessage());
			Thread.currentThread().interrupt();
			logger.error("Failed during Navigate action " +fetchMetadataVO.getScriptNumber());
			takeScreenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void openTask(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO, String type1,
			String type2, String param1, String param2, int count, CustomerProjectDto customerDetails)
			throws Exception {
		String param3 = "Tasks";
		clickImage(driver, param3, param2, fetchMetadataVO, fetchConfigVO, customerDetails);
		clickTaskLink(driver, param1, fetchMetadataVO, fetchConfigVO, customerDetails);

	}

	public void logout(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO, String type1,
			String type2, String type3, String param1, String param2, String param3, CustomerProjectDto customerDetails)
			throws Exception {
		String param4 = "Settings and Actions";
		String param5 = "Sign Out";
		String param6 = "Confirm";
		// clickLink(driver, param4, param3, fetchMetadataVO, fetchConfigVO,
		// customerDetails);
		// clickLink(driver, param5, param3, fetchMetadataVO, fetchConfigVO,
		// customerDetails);
		clickLogoutDropdown(driver, param4, fetchMetadataVO, fetchConfigVO, customerDetails);
		clickSignout(driver, param5, fetchMetadataVO, fetchConfigVO, customerDetails);
		clickSignInSignOut(driver, param6, fetchMetadataVO, fetchConfigVO, customerDetails);
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
			logger.info("Successfully Navigate to the Navigate URL " +fetchMetadataVO.getScriptNumber());
		} catch (Exception e) {
			logger.error("Failed During Navigation " +fetchMetadataVO.getScriptNumber());
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
		}
	}

	public void clickLogoutDropdown(WebDriver driver, String param4, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//img[@title=\"" + param4 + "\"]"))));
			WebElement waittext = driver.findElement(By.xpath(("//img[@title=\"" + param4 + "\"]")));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		} catch (Exception e) {
			logger.error("Failed During LogOut Dropdown "  +fetchMetadataVO.getScriptNumber());
			screenshotFail(driver, fetchMetadataVO, customerDetails);
		}
	}

	public void clickSignout(WebDriver driver, String param5, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {
		try {

			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//a[text()=\"" + param5 + "\"]"))));
			WebElement waittext = driver.findElement(By.xpath(("//a[text()=\"" + param5 + "\"]")));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		} catch (Exception e) {
			logger.error("Failed During SignOut "  +fetchMetadataVO.getScriptNumber());
			screenshotFail(driver, fetchMetadataVO, customerDetails);
		}
	}

	public void mediumWait(WebDriver driver, String inputData, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {
		try {
			int time = StringUtils.convertStringToInteger(inputData, 4);
			int seconds = time * 1000;
			Thread.sleep(seconds);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
		} catch (InterruptedException e) {
			logger.error("Failed during Medium wait " + e.getMessage());
			Thread.currentThread().interrupt();
		}
	}

	public void shortwait(WebDriver driver, String inputData) {
		try {
			int time = StringUtils.convertStringToInteger(inputData, 2);
			int seconds = time * 1000;
			Thread.sleep(seconds);
		} catch (InterruptedException e) {
			logger.error("Failed during  short wait " + e.getMessage());
			Thread.currentThread().interrupt();
		}
	}

	public void wait(WebDriver driver, String inputData) {
		try {
			int time = StringUtils.convertStringToInteger(inputData, 8);
			int seconds = time * 1000;
			Thread.sleep(seconds);
		} catch (InterruptedException e) {
			logger.error("Failed during wait " + e.getMessage());
			Thread.currentThread().interrupt();
		}
	}

	public void uploadImage(List<ScriptDetailsDto> fetchMetadataListVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) {
		try {
			String sharepoint = fetchConfigVO.getSHAREPOINT_RESP();
			logger.info("Share point " + sharepoint);
			String accessToken = getAccessToken();
			List<String> imageUrlList = new ArrayList<>();
			File imageDir = new File(System.getProperty("user.dir") + File.separator + "Screenshot" + File.separator
					+ customerDetails.getCustomerName() + File.separator + customerDetails.getTestSetName());
			for (File imageFile : imageDir.listFiles()) {
				String imageFileName = imageFile.getName();
				logger.info("Image File Name " + imageFileName);
				imageUrlList.add(imageFileName);
				BufferedImage bImage = ImageIO.read(new File(imageDir + File.separator + imageFileName));
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ImageIO.write(bImage, "png", bos);
				byte[] data = bos.toByteArray();

				MultiValueMap<String, byte[]> bodyMap = new LinkedMultiValueMap<>();
				bodyMap.add("user-file", data);
				// Outer header
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.IMAGE_PNG);
				headers.add("userName", "kaushik.sekaran@winfosolutions.com");
				headers.add("password", "WatsSelenium@1");
				headers.set("Authorization", "Bearer " + accessToken);
				headers.add("scope",
						"https://graph.microsoft.com/Sites.ReadWrite.All https://graph.microsoft.com/Files.ReadWrite");
				HttpEntity<byte[]> requestEntity = new HttpEntity<>(data, headers);

				ResponseEntity<byte[]> response = restTemplate.exchange(
						"https://graph.microsoft.com/v1.0/me/drive/root:/Screenshot/"
								+ customerDetails.getCustomerName() + "/" + imageFileName + ":/content",
						HttpMethod.PUT, requestEntity, byte[].class);
				logger.info(String.format("Response Status : %s, Response Body : %s, Response : %s ", response.getStatusCode(), response.getBody(), response ));
			}
		} catch (Exception e) {
			logger.error("Failed during upload Image " + e.getMessage());
			Thread.currentThread().interrupt();
		}
	}

	public String getAccessToken() {
		String acessToken = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

			map.add("grant_type", "password");
			map.add("client_id", "874400db-4a61-4f17-a697-8b1a3e9228ea");

			map.add("client_secret", "GBLpmY0UP04JOHsixinT9j]B_UKjTM@=");
			map.add("scope", "https://graph.microsoft.com/Sites.ReadWrite.All");

			map.add("userName", "kaushik.sekaran@winfosolutions.com");
			map.add("password", "WatsSelenium@1");

			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
			ResponseEntity<Object> response = restTemplate.exchange(
					"https://login.microsoftonline.com/7c51d221-e93b-4397-b4c7-775faf9f6d10/oauth2/v2.0/token",
					HttpMethod.POST, entity, Object.class);

			logger.info("Rest Template " + response);

			@SuppressWarnings("unchecked")
			Map<String, Object> linkedMap = response.getBody() != null ? (Map<String, Object>) response.getBody()
					: null;
			acessToken = linkedMap != null ? StringUtils.convertToString(linkedMap.get("access_token")) : null;
		} catch (Exception e) {
			logger.error("Failed during getting Access Token " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		logger.info("Acess Token " + acessToken);
		return acessToken;
	}

	public void convertJPGtoMovie(String targetFile1, List<String> targetFileList,
			List<ScriptDetailsDto> fetchMetadataListVO, FetchConfigVO fetchConfigVO, String name,
			CustomerProjectDto customerDetails) {
		String vidPath = (fetchConfigVO.getPDF_PATH() + customerDetails.getCustomerName() + File.separator
				+ customerDetails.getTestSetName() + File.separator + name);
		String folder = (fetchConfigVO.getPDF_PATH() + customerDetails.getCustomerName() + File.separator
				+ customerDetails.getTestSetName() + File.separator);
		FileUtil.createDir(folder);
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
			if (targetFile1 != null) {
				logger.info("Target File: " + targetFile1);
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
		List<String> fileNameList = new ArrayList<>();
		File folder = new File(fetchConfigVO.getSCREENSHOT_PATH() + File.separator + customerDetails.getCustomerName()
				+ File.separator + customerDetails.getTestSetName() + File.separator);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				logger.debug("File " + listOfFiles[i].getName());
				String fileName = listOfFiles[i].getName();
				String[] fileNameArr = fileName.split("\\.");
				String fileExt = fileNameArr[fileNameArr.length - 1];
				String[] arr = fileName.split("_");
				String status = arr[6];
				status = status.split("\\.")[0];
				if ("jpg".equalsIgnoreCase(fileExt) && "Passed".equalsIgnoreCase(status)) {
					fileNameList.add(fileName);
				}
			}
		}
		return fileNameList;
	}

	public void openFile(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) {
		try {
			WebElement copy = driver.findElement(By.xpath("(//*[text()=\"Succeeded\"])[1]/preceding::span[text()][1]"));
			String number = copy.getText();
			// String num = number.replaceAll("[^\\d.]+|\\.(?!\\d)", "");
			// logger.info(num);
			driver.get(fetchConfigVO.getDOWNLOD_FILE_PATH() + "/" + number + ".log");
			Thread.sleep(2000);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			driver.navigate().back();
			Thread.sleep(8000);
			String xpath = "(//*[text()=\"Succeeded\"])[1]/preceding::span[text()][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
		} catch (Exception e) {
			logger.error("Failed during open file " + e.getMessage());
			Thread.currentThread().interrupt();
		}
	}

	public static File getLastModified(String directoryFilePath, FetchConfigVO fetchConfigVO) {
		File directory = new File(fetchConfigVO.getDOWNLOD_FILE_PATH());
		File[] files = directory.listFiles(File::isFile);
		long lastModifiedTime = Long.MIN_VALUE;
		File chosenFile = null;

		if (files != null) {
			for (File file : files) {
				if (file.lastModified() > lastModifiedTime) {
					chosenFile = file;
					lastModifiedTime = file.lastModified();
				}
			}
		}

		return chosenFile;
	}

	public void openPdf(WebDriver driver, String path, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) {
		try {

			Thread.sleep(5000);
			driver.get("chrome://downloads/");
			JavascriptExecutor js = (JavascriptExecutor) driver;
			String fileName = (String) js.executeScript(
					"return document.querySelector('downloads-manager').shadowRoot.querySelector('downloads-item').shadowRoot.querySelector('div#content #file-link').textContent");
			String filePath = fetchConfigVO.getDOWNLOD_FILE_PATH() + fileName;
			String fileUrl = "file://" + filePath;
			driver.get(fileUrl);
			Thread.sleep(5000);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			driver.navigate().back();
			Thread.sleep(2000);
			driver.navigate().back();
		} catch (Exception e) {
			logger.error("Failed during open PDF " + e.getMessage());
		}
	}

	public void navigateToBackPage(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) {
		try {
			Thread.sleep(5000);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			driver.navigate().back();
			Thread.sleep(8000);
		} catch (Exception e) {
			logger.error("Failed during navigate To BackPage " +e.getMessage());
			Thread.currentThread().interrupt();
		}
	}

	public void copy(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) throws Exception {
		try {
			Robot r = new Robot();
			r.keyPress(KeyEvent.VK_CONTROL);
			r.keyPress(KeyEvent.VK_C);
			r.keyRelease(KeyEvent.VK_C);
			r.keyRelease(KeyEvent.VK_CONTROL);

		} catch (Exception e) {
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			e.printStackTrace();
			throw e;
		}
	}

	public void paste(WebDriver driver, String inputParam, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, String globalValueForSteps, CustomerProjectDto customerDetails)
			throws Exception {
		try {
			Actions action = new Actions(driver);
			action.keyDown(Keys.CONTROL).sendKeys("v").build().perform();
		} catch (Exception e) {
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void clear(WebDriver driver, String inputParam, String inputParam2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {
		try {
			WebElement waittill = driver.findElement(
					By.xpath("(//label[contains(text(),\"" + inputParam + "\")]/preceding-sibling::input)[1]"));
			clearMethod(driver, waittill);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//label[contains(text(),\"inputParam\")]/preceding-sibling::input)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			logger.error("Failed during clear " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			// Thread.sleep(2000);
			WebElement waittill = driver.findElement(
					By.xpath("(//label[normalize-space(text())=\"" + inputParam + "\"]/following::input)[1]"));
			clearMethod(driver, waittill);
			Thread.sleep(2000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//label[normalize-space(text())=\"inputParam\"]/following::input)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during clear " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebElement waittill = driver
					.findElement(By.xpath("(//*[normalize-space(text())=\"" + inputParam + "\"]/following::input)[1]"));
			clearMethod(driver, waittill);
			Thread.sleep(2000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//*[normalize-space(text())=\"inputParam\"]/following::input)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during clear " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("//*[contains(@placeholder,\"" + inputParam + "\")]"));
			clearMethod(driver, waittill);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[contains(@placeholder,\"inputParam\")]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during clear " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebElement waittill = driver.findElement(
					By.xpath("//*[normalize-space(text())=\"" + inputParam + "\"]/following::textarea[1]"));
			clearMethod(driver, waittill);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[normalize-space(text())=\"inputParam\"]/following::textarea[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			logger.error("Failed during clear " + e.getMessage());
			Thread.currentThread().interrupt();
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
					logger.info("Window closed Successfully");
				}
			}
		} catch (Exception e) {
			logger.error("Failed During WindowClose Acion.");
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void switchToActiveElement(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) {
		try {
			driver.switchTo().activeElement();
			logger.info("Switched to Element Successfully");
		} catch (Exception e) {
			logger.error("Failed During switchToActiveElement Action " + e.getMessage());
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void clickMenu(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		if (param1.equalsIgnoreCase("Setup and Maintenance")) {
			try {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//h1[normalize-space(text())=\"Navigator\"]/following::*[normalize-space(text())=\""
								+ param1 + "\"]")));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(
						By.xpath("//h1[normalize-space(text())=\"Navigator\"]/following::*[normalize-space(text())=\""
								+ param1 + "\"]"),
						param1));
				WebElement waittext = driver.findElement(
						By.xpath("//h1[normalize-space(text())=\"Navigator\"]/following::*[normalize-space(text())=\""
								+ param1 + "\"]"));
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//h1[normalize-space(text())=\"Navigator\"]/following::*[normalize-space(text())=\"param1\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} catch (Exception e) {
				logger.error("Failed during click Menu " +e.getMessage());
				Thread.currentThread().interrupt();
			}
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//h1[normalize-space(text())=\"Navigator\"]/following::*[normalize-space(text())=\""
							+ param1 + "\"]/following::a[normalize-space(text())=\"" + param2 + "\"]")));
//		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h1[normalize-space(text())=\"Navigator\"]/following::*[normalize-space(text())=\""+param1+"\"]/following::a[normalize-space(text())=\""+param2+"\"]"), param2));
			WebElement waittext = driver.findElement(
					By.xpath("//h1[normalize-space(text())=\"Navigator\"]/following::*[normalize-space(text())=\""
							+ param1 + "\"]/following::a[normalize-space(text())=\"" + param2 + "\"]"));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//h1[normalize-space(text())=\"Navigator\"]/following::*[normalize-space(text())=\"param1\"]/following::a[normalize-space(text())=\"param2\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during click Menu " +e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath(("//a[normalize-space(text())=\"" + param1 + "\"]"))));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath(("//a[normalize-space(text())=\"" + param1 + "\"]")), param1));
			WebElement waittext = driver.findElement(By.xpath(("//a[normalize-space(text())=\"" + param1 + "\"]")));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//a[normalize-space(text())=\"param1\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during click Menu " +e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath(("//div[contains(@style,\"display: block\")]//div[text()=\"" + param1 + "\"]"))));
			WebElement waittext = driver.findElement(
					By.xpath(("//div[contains(@style,\"display: block\")]//div[text()=\"" + param1 + "\"]")));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//div[contains(@style,\"display: block\")]//div[text()=\"param1 \"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during click Menu " +e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath(("//div[normalize-space(text())=\"" + param1 + "\"]"))));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath(("//div[normalize-space(text())=\"" + param1 + "\"]")), param1));
			WebElement waittext = driver.findElement(By.xpath(("//div[normalize-space(text())=\"" + param1 + "\"]")));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//div[normalize-space(text())=\"param1 \"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during click Menu " +e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("(//div[contains(@id,\"" + param1 + "\")])[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//div[contains(@id,\"" + param1 + "\")])[1]"));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//div[contains(@id,\"param1\")])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error(e.getMessage());
			Thread.currentThread().interrupt();
			logger.error("Failed during Click action.");
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void clickSignIn(WebDriver driver, String param6, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			String title1 = driver.getTitle();
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//button[@type=\"" + param6 + "\"]"))));
			WebElement waittext = driver.findElement(By.xpath(("//button[@type=\"" + param6 + "\"]")));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(5000);
			String title2 = driver.getTitle();
			if (title1.equalsIgnoreCase(title2)) {
				screenshotFail(driver, fetchMetadataVO, customerDetails);
				throw new IOException("Failed during login page");
			}
			String xpath = "//button[@type=\"param1\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
		} catch (Exception e) {
			logger.error(e.getMessage());
			Thread.currentThread().interrupt();
			logger.error("Failed during Click action.");
			screenshotFail(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void clickSignInSignOut(WebDriver driver, String param6, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			String title1 = driver.getTitle();
			screenshot(driver, fetchMetadataVO, customerDetails);
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//button[@name=\"" + param6 + "\"]"))));
			WebElement waittext = driver.findElement(By.xpath(("//button[@name=\"" + param6 + "\"]")));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(5000);
			String title2 = driver.getTitle();
			if (title1.equalsIgnoreCase(title2)) {
				screenshotFail(driver, fetchMetadataVO, customerDetails);
				throw new IOException("Failed during login page");
			}
			String xpath = "//button[@name=\"param1\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
		} catch (Exception e) {
			logger.error(e.getMessage());
			Thread.currentThread().interrupt();
			logger.error("Failed during Click action.");
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void clickTaskLink(WebDriver driver, String param1, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			Thread.sleep(2000);
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath(("//a[normalize-space(text())=\"" + param1 + "\"][1]"))));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath(("//a[normalize-space(text())=\"" + param1 + "\"][1]")), param1));
			WebElement waittext = driver.findElement(By.xpath(("//a[normalize-space(text())=\"" + param1 + "\"][1]")));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String xpath = "//a[normalize-space(text())=\"param1\"][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
		} catch (Exception e) {
			logger.error(e.getMessage());
			Thread.currentThread().interrupt();
			logger.error("Failed during Click action.");
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void clickButtonDropdown(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {
		try {
			if (param1.equalsIgnoreCase("Approvals") && param2.equalsIgnoreCase("Actions")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[normalize-space(text())=\""
						+ param1 + "\"]/following::a[normalize-space(text())=\"" + param2 + "\"])[1]")));
				WebElement waittext = driver.findElement(By.xpath("(//h1[normalize-space(text())=\"" + param1
						+ "\"]/following::a[normalize-space(text())=\"" + param2 + "\"])[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				clickButtonDropdownText(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
//		screenshot1(driver, "", fetchMetadataVO, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//h1[normalize-space(text())=\"param1\"]/following::a[normalize-space(text())=\"param2\"])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during clickButton Dropdown" + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param2.equalsIgnoreCase("Publish to Managers")) {
				// updating [2] to [1] after 21A Execution
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())=\""
						+ param1 + "\"]/following::a[normalize-space(text())=\"" + param2 + "\"])[2]")));
				WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())=\"" + param1
						+ "\"]/following::a[normalize-space(text())=\"" + param2 + "\"])[2]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				Thread.sleep(3000);
				waittext.click();
//		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				clickButtonDropdownText(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
//		screenshot1(driver, "", fetchMetadataVO, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//*[normalize-space(text())=\"param1\"]/following::a[normalize-space(text())=\"param2\"])[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during clickButton Dropdown" + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			// updating budget method to budget pool after 21A Execution and [2] to [1]
			if (param1.equalsIgnoreCase("Budget Pool") && param2.equalsIgnoreCase("Actions")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())=\""
						+ param1 + "\"]/following::a[normalize-space(text())=\"" + param2 + "\"])[2]")));
				WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())=\"" + param1
						+ "\"]/following::a[normalize-space(text())=\"" + param2 + "\"])[2]"));
				waittext.click();
				clickButtonDropdownText(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//*[normalize-space(text())=\"param1\"]/following::a[normalize-space(text())=\"param2\"])[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during clickButton Dropdown" + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Workforce Compensation") && param2.equalsIgnoreCase("Search")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//h1[contains(text(),\"" + param1 + "\")]/following::a[1]")));
				WebElement waittext = driver
						.findElement(By.xpath("//h1[contains(text(),\"" + param1 + "\")]/following::a[1]"));
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
//		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				WebElement search = driver.findElement(By.xpath("//li[text()=\"Search...\"]"));
				search.click();
				WebElement name = driver.findElement(
						By.xpath("//*[text()=\"Search\"]/following::*[text()=\"Name\"]/following::input[1]"));
				name.sendKeys(keysToSend);
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				WebElement text = driver.findElement(
						By.xpath("//*[normalize-space(text())=\"Search\"]/following::a[normalize-space(text())=\""
								+ keysToSend + "\"]"));
				text.click();
				WebElement button = driver.findElement(By.xpath(
						"//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\"o\"][1]"));
				button.click();
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//h1[contains(text(),\"param1\")]/following::a[1]" + ";" + "//li[text()=\"Search...\"]"
						+ ";" + "//*[text()=\"Search\"]/following::*[text()=\"Name\"]/following::input[1]" + ";"
						+ "//*[normalize-space(text())=\"Search\"]/following::a[normalize-space(text())=\"keysToSend\"]"
						+ ";"
						+ "//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\"o\"][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during clickButton Dropdown" + e.getMessage());
			Thread.currentThread().interrupt();
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
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			clickButtonDropdownText(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
//		screenshot1(driver, "", fetchMetadataVO, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//h1[normalize-space(text())=\"param1\"]/following::a[@title=\"param2\"])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during clickButton Dropdown" + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[normalize-space(text())=\"" + param1
					+ "\"]/following::a[normalize-space(text())=\"" + param2 + "\"])[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//h1[normalize-space(text())=\"" + param1
					+ "\"]/following::a[normalize-space(text())=\"" + param2 + "\"])[1]"));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(4000);
			WebElement values = driver
					.findElement(By.xpath("(//td[normalize-space(text())=\"" + keysToSend + "\"])[2]"));
			clickValidateXpath(driver, fetchMetadataVO, values, fetchConfigVO);
//		screenshot1(driver, "", fetchMetadataVO, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//h1[normalize-space(text())=\"param1\"]/following::a[normalize-space(text())=\"param2\"])[1]"
					+ ";" + "(//td[normalize-space(text())=\"keysToSend\"])[2]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during clickButton Dropdown" + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@title=\"" + param1 + "\"]")));
			WebElement waittext = driver.findElement(By.xpath("//a[@title=\"" + param1 + "\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			clickButtonDropdownText(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
//		screenshot1(driver, "", fetchMetadataVO, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//a[@title=\"param1\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during clickButton Dropdown" + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("//h1[contains(text(),\"" + param1 + "\")]/following::a[1]")));
			WebElement waittext = driver
					.findElement(By.xpath("//h1[contains(text(),\"" + param1 + "\")]/following::a[1]"));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
//		screenshot1(driver, "", fetchMetadataVO, fetchConfigVO);
			clickButtonDropdownText(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//h1[contains(text(),\"param1\")]/following::a[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
		} catch (Exception e) {
			logger.error(e.getMessage());
			Thread.currentThread().interrupt();
			logger.error("Failed during Click action.");
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
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
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath(("//li[normalize-space(text())=\"" + keysToSend + "\"]")), keysToSend));
			Thread.sleep(5000);
			WebElement waittext = driver.findElement(By.xpath("//li[normalize-space(text())=\"" + keysToSend + "\"]"));
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String xpath = "//li[normalize-space(text())=\"keysToSend\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during clickButton Dropdown" + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//div[contains(@class,\"PopupMenuContent\")]//td[text()=\"" + keysToSend + "\"]")));
			WebElement waittext = driver.findElement(
					By.xpath("//div[contains(@class,\"PopupMenuContent\")]//td[text()=\"" + keysToSend + "\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String xpath = "//div[contains(@class,\"PopupMenuContent\")]//td[text()=\"keysToSend\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during clickButton Dropdown" + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("//td[normalize-space(text())=\"" + keysToSend + "\"]")));
			WebElement waittext = driver.findElement(By.xpath("//td[normalize-space(text())=\"" + keysToSend + "\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String xpath = "//td[normalize-space(text())=\"keysToSend\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
		} catch (Exception e) {
			logger.error(e.getMessage());
			Thread.currentThread().interrupt();
			logger.error("Failed during Click action.");
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void clickExpandorcollapse(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"(//h2[normalize-space(text())=\"" + param1 + "\"]/following::*[@title=\"" + param2 + "\"])[1]")));
			Thread.sleep(8000);
			WebElement waittext = driver.findElement(By.xpath(
					"(//h2[normalize-space(text())=\"" + param1 + "\"]/following::*[@title=\"" + param2 + "\"])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//h2[normalize-space(text())=\" param1\"]/following::*[@title=\" param2 \"])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during clickButton Dropdown" + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"//*[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\"" + param2
							+ "\"]/preceding::*[@title=\"Expand\" and @href and not(@style=\"display:none\")][1]")));
			WebElement waittext = driver.findElement(By.xpath(
					"//*[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\"" + param2
							+ "\"]/preceding::*[@title=\"Expand\" and @href and not(@style=\"display:none\")][1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[normalize-space(text())=\" param1 \"]/following::*[normalize-space(text())=\" param2 \"]/preceding::*[@title=\"Expand\" and @href and not(@style=\"display:none\")][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during clickButton Dropdown" + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"(//*[normalize-space(text())=\"" + param1 + "\"]/following::*[@title=\"" + param2 + "\"])[1]")));
			Thread.sleep(4000);
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]"), param1));
			WebElement waittext = driver.findElement(By.xpath(
					"(//*[normalize-space(text())=\"" + param1 + "\"]/following::*[@title=\"" + param2 + "\"])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(2000);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//*[normalize-space(text())=\" param1 \"]/following::*[@title=\" param2 \"])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during clickButton Dropdown" + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"(//*[normalize-space(text())=\"" + param1 + "\"]/preceding::*[@title=\"" + param2 + "\"])[1]")));

			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]"), param1));
			WebElement waittext = driver.findElement(By.xpath(
					"(//*[normalize-space(text())=\"" + param1 + "\"]/preceding::*[@title=\"" + param2 + "\"])[1]"));
			Thread.sleep(4000);
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(8000);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//*[normalize-space(text())=\"param1 \"]/preceding::*[@title=\" param2\"])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
		} catch (Exception e) {
			logger.error(e.getMessage());
			Thread.currentThread().interrupt();
			logger.error("Failed during Click action.");
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void selectAValue(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {
		try {
			if (param1.equalsIgnoreCase("Manager")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"//*[contains(text(),\"" + param1 + "\")]/following::*[text()=\"" + keysToSend + "\"][2]")));
				WebElement waittext = driver.findElement(By.xpath(
						"//*[contains(text(),\"" + param1 + "\")]/following::*[text()=\"" + keysToSend + "\"][2]"));
				Thread.sleep(6000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[contains(text(),\"param1\")]/following::*[text()=\"keysToSend\"][2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during clickButton Dropdown " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Worker Name") || param1.equalsIgnoreCase("Manager")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				wait.until(ExpectedConditions.presenceOfElementLocated(By
						.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
								+ keysToSend + "\"]/following::img[1]")));
				wait.until(
						ExpectedConditions.textToBePresentInElementLocated(
								By.xpath("//*[normalize-space(text())=\"" + param1
										+ "\"]/following::*[normalize-space(text())=\"" + keysToSend + "\"]"),
								keysToSend));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::*[normalize-space(text())=\"" + keysToSend + "\"]/following::img[1]"));
				Thread.sleep(4000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"keysToSend\"]/following::img[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during clickButton Dropdown " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Worker")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())=\""
						+ param1 + "\"]/following::div[contains(@class,\"PopupMenuPopup\")]//span[contains(text(),\""
						+ keysToSend + "\")][1]")));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(
						By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]"), param1));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::div[contains(@class,\"PopupMenuPopup\")]//span[contains(text(),\""
						+ keysToSend + "\")][1]"));
				Thread.sleep(4000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::div[contains(@class,\"PopupMenuPopup\")]//span[contains(text(),\"keysToSend\")][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during clickButton Dropdown " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Performance Document Eligibility")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"//*[contains(text(),\"" + param1 + "\")]/following::a[text()=\"" + keysToSend + "\"]")));
				WebElement waittext = driver.findElement(By
						.xpath("//*[contains(text(),\"" + param1 + "\")]/following::a[text()=\"" + keysToSend + "\"]"));
				Thread.sleep(6000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[contains(text(),\"param1\")]/following::a[text()=\"keysToSend\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during clickButton Dropdown " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("performance documents")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
	
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[contains(text(),\"" + param1 + "\")]/following::*[text()=\"" + keysToSend + "\"]")));
				WebElement waittext = driver.findElement(
						By.xpath("//*[contains(text(),\"" + param1 + "\")]/following::*[text()=\"" + keysToSend + "\"]"));
				Thread.sleep(6000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[contains(text(),\"param1\")]/following::*[text()=\"keysToSend\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
			} catch (Exception e) {
				logger.error("Failed during clickButton Dropdown " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
	
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[contains(text(),\"" + param1 + "\")]/following::*[text()=\"" + keysToSend + "\"]")));
				WebElement waittext = driver.findElement(
						By.xpath("//*[contains(text(),\"" + param1 + "\")]/following::*[text()=\"" + keysToSend + "\"]"));
				Thread.sleep(6000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[contains(text(),\"param1\")]/following::*[text()=\"keysToSend\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} catch (Exception e) {
				logger.error("Failed during clickButton Dropdown " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]")));
			WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[normalize-space(text())=\"param1 \"]/following::label[normalize-space(text())=\"param2\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during clickButton Dropdown " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"//*[normalize-space(text())=\"" + param1 + "\"]/following::*[@title=\"" + keysToSend + "\"][1]")));
			WebElement waittext = driver.findElement(By.xpath(
					"//*[normalize-space(text())=\"" + param1 + "\"]/following::*[@title=\"" + keysToSend + "\"][1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[normalize-space(text())=\"param1\"]/following::*[@title=\"keysToSend\"][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
		} catch (Exception e) {
			logger.error(e.getMessage());
			Thread.currentThread().interrupt();
			logger.error("Failed during Click action.");
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
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

			WebElement waittill = driver.findElement(
					By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
							+ keysToSend + "\"]/following::img[contains(@id,\"" + param2 + "\")][1]"));
			Thread.sleep(4000);
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			Thread.sleep(2000);
			highlightElement(driver, fetchMetadataVO, waittill, fetchConfigVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittill, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"keysToSend\"]/following::img[contains(@id,\"param2\")][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return keysToSend;
		} catch (Exception e) {
			logger.error("Failed during clickButton Dropdown " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("(//*[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
							+ keysToSend + "\"]/following::img[@title=\"" + param2 + "\"])[1]")));
			WebElement waittill = driver.findElement(
					By.xpath("(//*[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
							+ keysToSend + "\"]/following::img[@title=\"" + param2 + "\"])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			Thread.sleep(2000);
			highlightElement(driver, fetchMetadataVO, waittill, fetchConfigVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittill, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"keysToSend\"]/following::img[@title=\"param2\"])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return keysToSend;
		} catch (Exception e) {
			logger.error("Failed during clickButton Dropdown " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::*[@value=\"" + keysToSend
							+ "\"]/following::img[normalize-space(@title)=\"" + param2 + "\"][1]")));
			WebElement waittill = driver
					.findElement(By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::*[@value=\""
							+ keysToSend + "\"]/following::img[normalize-space(@title)=\"" + param2 + "\"][1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			Thread.sleep(2000);
			highlightElement(driver, fetchMetadataVO, waittill, fetchConfigVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittill, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[normalize-space(text())=\"param1\"]/following::*[@value=\"keysToSend\"]/following::img[normalize-space(@title)=\"param2\"][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return keysToSend;
		} catch (Exception e) {
			logger.error(e.getMessage());
			Thread.currentThread().interrupt();
			logger.error("Failed during Click action.");
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void clickImage(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			if (param2.equalsIgnoreCase("Back")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//h1[normalize-space(text())=\"" + param1 + "\"]/preceding::a[1]")));
				WebElement waittext = driver
						.findElement(By.xpath("//h1[normalize-space(text())=\"" + param1 + "\"]/preceding::a[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//h1[normalize-space(text())=\"param1\"]/preceding::a[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during click Image " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Back")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(3000);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@title=\"Done\"]")));
				WebElement waittext = driver.findElement(By.xpath("//a[@title=\"Done\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//a[@title=\"Done\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during click Image " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {

			if (param1.equalsIgnoreCase("Notes") && (param2.equalsIgnoreCase("Create"))) {
				WebElement add = driver.findElement(By.xpath(
						"//div[normalize-space(text())=\"" + param1 + "\"]/following::img[@title=\"" + param2 + "\"]"));
				highlightElement(driver, fetchMetadataVO, add, fetchConfigVO);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, add, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//div[normalize-space(text())=\"param1\"]/following::img[@title=\"param2\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during click Image " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {

			if (param1.equalsIgnoreCase("Notes")) {
				WebElement add = driver.findElement(By.xpath(
						"//div[normalize-space(text())=\"" + param1 + "\"]/following::div[@role=\"button\"][1]"));
				highlightElement(driver, fetchMetadataVO, add, fetchConfigVO);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, add, fetchConfigVO);
				String xpath = "//div[normalize-space(text())=\"param1\"]/following::div[@role=\"button\"][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during click Image " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By
					.xpath("(//h1[normalize-space(text())=\"" + param1 + "\"]/following::div[@role=\"button\"])[1]")));

			WebElement waittext = driver.findElement(
					By.xpath("(//h1[normalize-space(text())=\"" + param1 + "\"]/following::div[@role=\"button\"])[1]"));
			Thread.sleep(2000);
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(3000);
			WebElement add = driver.findElement(By.xpath(
					"//h1[normalize-space(text())=\"" + param1 + "\"]/following::img[@title=\"" + param2 + "\"]"));
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, add, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//h1[normalize-space(text())=\"param1\"]/following::div[@role=\"button\"])[1]" + ";"
					+ "//h1[normalize-space(text())=\"param1\"]/following::img[@title=\"param2\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during click Image " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {

			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[normalize-space(text())=\"" + param1
					+ "\"]/following::img[@title=\"" + param2 + "\"])[1]")));
			WebElement waittext = driver.findElement(By.xpath(
					"(//h1[normalize-space(text())=\"" + param1 + "\"]/following::img[@title=\"" + param2 + "\"])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(4000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//h1[normalize-space(text())=\"param1\"]/following::img[@title=\"param2\"])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during click Image " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"//*[normalize-space(text())=\"" + param1 + "\"]/following::img[@title=\"" + param2 + "\"][1]")));
			WebElement waittext = driver.findElement(By.xpath(
					"//*[normalize-space(text())=\"" + param1 + "\"]/following::img[@title=\"" + param2 + "\"][1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[normalize-space(text())=\"param1\"]/following::img[@title=\"param2\"][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during click Image " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("(//*[normalize-space(text())=\"" + param1 + "\"]/following::div[@role=\"button\"])[1]")));
			WebElement waittext = driver.findElement(
					By.xpath("(//*[normalize-space(text())=\"" + param1 + "\"]/following::div[@role=\"button\"])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(3000);
			WebElement add = driver.findElement(By.xpath(
					"//*[normalize-space(text())=\"" + param1 + "\"]/following::img[@title=\"" + param2 + "\"]"));
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//*[normalize-space(text())=\"param1 \"]/following::div[@role=\"button\"])[1]" + ";"
					+ "//*[normalize-space(text())=\"param1\"]/following::img[@title=\"param2\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during click Image " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::img[contains(@id,\"" + param2 + "\")]")));
			WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::img[contains(@id,\"" + param2 + "\")]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[normalize-space(text())=\"param1\"]/following::img[contains(@id,\"param2\")]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during click Image " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"//*[normalize-space(text())=\"" + param1 + "\"]/following::img[@title=\"" + param2 + "\"][1]")));
			WebElement waittext = driver.findElement(By.xpath(
					"//*[normalize-space(text())=\"" + param1 + "\"]/following::img[@title=\"" + param2 + "\"][1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[normalize-space(text())=\"param1\"]/following::img[@title=\"param2\"][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during click Image " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {

			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@title=\"" + param1 + "\"]")));
			WebElement waittext = driver.findElement(By.xpath("//img[@title=\"" + param1 + "\"]"));
			Thread.sleep(3000);
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(3000);
			waittext.click();
			// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//img[@title=\"param1\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during click Image " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(
					ExpectedConditions.presenceOfElementLocated(By.xpath("//img[contains(@id,\"" + param1 + "\")]")));
			WebElement waittext = driver.findElement(By.xpath("//img[contains(@id,\"" + param1 + "\")]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//img[contains(@id,\"param1\")]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during click Image" + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@title=\"" + param1 + "\"]")));
			WebElement waittext = driver.findElement(By.xpath("//a[@title=\"" + param1 + "\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//a[@title=\"param1\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during click Image " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::img[1]")));
			WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::img[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]/following::img[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			logger.error("Failed during click Image " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("//*[contains(@aria-label,\"" + param1 + "\")]")));
			WebElement waittext = driver.findElement(By.xpath("//*[contains(@aria-label,\"" + param1 + "\")]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[contains(@aria-label,\"param1\")]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
		} catch (Exception e) {
			logger.error(e.getMessage());
			Thread.currentThread().interrupt();
			logger.error("Failed during Click Image ");
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void clickButton(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			if (param1.equalsIgnoreCase("Done")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[text()=\"ne\"]"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()=\"ne\"]"), "ne"));
				WebElement waittext = driver.findElement(By.xpath(("//span[text()=\"ne\"]")));
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(2000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//span[text()=\"ne\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param1.equalsIgnoreCase("Warning") && param2.equalsIgnoreCase("Yes")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(2000);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//div[@class='AFPopupSelector']//*[contains(text(),\"Warning\")]/following::*[normalize-space(text())=\"Yes\"]"))));
				WebElement waittext = driver.findElement(By.xpath(("//div[@class='AFPopupSelector']//*[contains(text(),\"Warning\")]/following::*[normalize-space(text())=\"Yes\"]")));
				screenshot(driver, fetchMetadataVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//div[@class='AFPopupSelector']//*[contains(text(),\"Warning\")]/following::*[normalize-space(text())=\"Yes\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
	//			service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param2.equalsIgnoreCase("Done")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(2000);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[text()=\"o\"]"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()=\"o\"]"), "o"));
				WebElement waittext = driver.findElement(By.xpath(("//span[text()=\"o\"]")));
				screenshot(driver, fetchMetadataVO, customerDetails);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//span[text()=\"o\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param1.equalsIgnoreCase("Submit")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(40000);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[text()=\"m\"]"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()=\"m\"]"), "m"));

				WebElement waittext = driver.findElement(By.xpath(("//span[text()=\"m\"]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// screenshot1(driver, "", fetchMetadataVO, fetchConfigVO);
				Thread.sleep(2000);
				highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(3000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//span[text()=\"m\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param2.equalsIgnoreCase("Submit")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(20000);
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//*[normalize-space(text())=\"" + param1 + "\"]/following::span[text()=\"m\"]"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(
						By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::span[text()=\"m\"]"),
						"m"));

				WebElement waittext = driver.findElement(
						By.xpath(("//*[normalize-space(text())=\"" + param1 + "\"]/following::span[text()=\"m\"]")));
				highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(3000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::span[text()=\"m\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param1.equalsIgnoreCase("Edit Employment: Review") && param2.equalsIgnoreCase("Submit")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//h1[text()=\"Edit Employment: Review\"]/following::*[@title=\"Submit\"]"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(
						By.xpath("//h1[text()=\"Edit Employment: Review\"]"), "Edit Employment: Review"));
				Thread.sleep(20000);
				WebElement waittext = driver.findElement(
						By.xpath(("//h1[text()=\"Edit Employment: Review\"]/following::*[@title=\"Submit\"]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(3000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//h1[text()=\"Edit Employment: Review\"]/following::*[@title=\"Submit\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param1.equalsIgnoreCase("Next")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[text()=\"x\"]"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()=\"x\"]"), "x"));

				WebElement waittext = driver.findElement(By.xpath(("//span[text()=\"x\"]")));
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(10000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(20000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//span[text()=\"x\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param2.equalsIgnoreCase("Next")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())=\""
						+ param1 + "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]")));
				wait.until(
						ExpectedConditions
								.textToBePresentInElementLocated(
										By.xpath("//*[normalize-space(text())=\"" + param1
												+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]"),
										"x"));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(8000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param2.equalsIgnoreCase("Yes")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//*[normalize-space(text())=\"" + param1 + "\"]/following::span[text()=\"Y\"]"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(
						By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::span[text()=\"Y\"]"),
						"Y"));

				WebElement waittext = driver.findElement(
						By.xpath(("//*[normalize-space(text())=\"" + param1 + "\"]/following::span[text()=\"Y\"]")));
				Thread.sleep(10000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(2000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::span[text()=\"Y\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			} else if (param1.equalsIgnoreCase("Yes")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[text()=\"Y\"]"))));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath(("//span[text()=\"Y\"]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(2000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//span[text()=\"Y\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param2.equalsIgnoreCase("OK")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("//button[@_afrpdo=\"ok\" and @accesskey=\"K\"]")));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(
						By.xpath("//button[@_afrpdo=\"ok\" and @accesskey=\"K\"]"), "K"));
				Thread.sleep(15000);
				WebElement waittext = driver.findElement(By.xpath("//button[@_afrpdo=\"ok\" and @accesskey=\"K\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(4000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//button[@_afrpdo=\"ok\" and @accesskey=\"K\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param1.equalsIgnoreCase("Save and Close")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[text()=\"S\"]"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()=\"S\"]"), "S"));

				WebElement waittext = driver.findElement(By.xpath(("//span[text()=\"S\"]")));
				Thread.sleep(4000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//span[text()=\"S\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param1.equalsIgnoreCase("Continue")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[text()=\"u\"]"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()=\"u\"]"), "u"));

				WebElement waittext = driver.findElement(By.xpath(("//span[text()=\"u\"]")));
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(4000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
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
				highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//button[text()=\"Contin\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param1.equalsIgnoreCase("Close")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//button[text()=\"Cl\"]"))));

				WebElement waittext = driver.findElement(By.xpath(("//button[text()=\"Cl\"]")));
				Thread.sleep(5000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//button[text()=\"Cl\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param1.equalsIgnoreCase("Cancel")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[text()=\"C\"]"))));

				WebElement waittext = driver.findElement(By.xpath(("//span[text()=\"C\"]")));
				Thread.sleep(2000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//span[text()=\"C\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			} else if (param2.equalsIgnoreCase("Cancel")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//*[normalize-space(text())=\"" + param1 + "\"]/following::span[text()=\"C\"]"))));

				WebElement waittext = driver.findElement(
						By.xpath(("//*[normalize-space(text())=\"" + param1 + "\"]/following::span[text()=\"C\"]")));
				Thread.sleep(2000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::span[text()=\"C\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			} else if (param1.equalsIgnoreCase("Save")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[text()=\"ave\"]"))));
				wait.until(
						ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()=\"ave\"]"), "ave"));

				WebElement waittext = driver.findElement(By.xpath(("//span[text()=\"ave\"]")));
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(4000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//span[text()=\"ave\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during click Button " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Subject Areas") && param2.equalsIgnoreCase("Name")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"(//*[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
								+ param2 + "\" and not(@_afrpdo)])[1]")));

				WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())=\"" + param1
						+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\" and not(@_afrpdo)])[1]"));
				Thread.sleep(10000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				actions.doubleClick(waittext).build().perform();
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\" and not(@_afrpdo)])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during click Button " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Edit Plan Cycle")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By
						.xpath(("//*[normalize-space(text())=\"" + param1 + "\"]/following::span[text()=\"K\"][2]"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(
						By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::span[text()=\"K\"][2]"),
						"K"));
				WebElement waittext = driver.findElement(
						By.xpath(("//*[normalize-space(text())=\"" + param1 + "\"]/following::span[text()=\"K\"][2]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(4000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::span[text()=\"K\"][2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during click Image " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param2.equalsIgnoreCase("OK")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//*[normalize-space(text())=\"" + param1 + "\"]/following::span[text()=\"K\"]"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(
						By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::span[text()=\"K\"]"),
						"K"));
				WebElement waittext = driver.findElement(
						By.xpath(("//*[normalize-space(text())=\"" + param1 + "\"]/following::span[text()=\"K\"]")));
				Thread.sleep(5000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(4000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::span[text()=\"K\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during click Image " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param2.equalsIgnoreCase("Submit")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[normalize-space(text())=\""
						+ param1 + "\"]/following::span[normalize-space(text())=\"" + param2 + "\"]"))));
				WebElement waittext = driver.findElement(By.xpath(("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::span[normalize-space(text())=\"" + param2 + "\"]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(3000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::span[normalize-space(text())=\"param2\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			logger.error("Failed during click Button " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param2.equalsIgnoreCase("Save and Close")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[normalize-space(text())=\""
						+ param1 + "\"]/following::span[normalize-space(text())=\"S\"]"))));
				WebElement waittext = driver.findElement(By.xpath(("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::span[normalize-space(text())=\"S\"]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(3000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::span[normalize-space(text())=\"S\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			logger.error("Failed during click Button " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Goals") && param2.equals("Actions")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@id,\"actBtn\")]")));
				WebElement waittext = driver.findElement(By.xpath("//div[contains(@id,\"actBtn\")]"));
				Thread.sleep(4000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//div[contains(@id,\"actBtn\")]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			logger.error("Failed during click Button " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath(("//span[normalize-space(text())=\"" + param1 + "\"]"))));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath("//span[normalize-space(text())=\"" + param1 + "\"]"), param1));

			WebElement waittext = driver.findElement(By.xpath(("//span[normalize-space(text())=\"" + param1 + "\"]")));
			Thread.sleep(4000);
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(5000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//span[normalize-space(text())=\"param1 \"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during click Image " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath(("//td[normalize-space(text())=\"" + param1 + "\"]"))));
			WebElement waittext = driver.findElement(By.xpath(("//td[normalize-space(text())=\"" + param1 + "\"]")));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			// screenshot1(driver, "", fetchMetadataVO, fetchConfigVO);
			Thread.sleep(5000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(2000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//td[normalize-space(text())=\"param1\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during click Image " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By
					.xpath(("//button[normalize-space(text())=\"" + param1 + "\"and not(@style=\"display:none\")]"))));
			WebElement waittext = driver.findElement(
					By.xpath(("//button[normalize-space(text())=\"" + param1 + "\"and not(@style=\"display:none\")]")));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(5000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//button[normalize-space(text())=\"param1\"and not(@style=\"display:none\")]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during click Button " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Warning")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"//*[normalize-space(text())=\"Warning\"]/following::*[contains(text(),\"Do you want to continue?\") and not(text()=\"Your changes aren\"t saved. If you leave this page, then your changes will be lost. Do you want to continue?\")]/following::*[text()=\"Yes\"]")));
				WebElement waittext = driver.findElement(By.xpath(
						"//*[normalize-space(text())=\"Warning\"]/following::*[contains(text(),\"Do you want to continue?\") and not(text()=\"Your changes aren\"t saved. If you leave this page, then your changes will be lost. Do you want to continue?\")]/following::*[text()=\"Yes\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(1000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[normalize-space(text())=\"Warning\"]/following::*[contains(text(),\"Do you want to continue?\") and not(text()=\"Your changes aren\"t saved. If you leave this page, then your changes will be lost. Do you want to continue?\")]/following::*[text()=\"Yes\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during click Button " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\" and not(@_afrpdo)])[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\" and not(@_afrpdo)])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\" and not(@_afrpdo)])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during click Button " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"])[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			screenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(1000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during click Button " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"])[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);

			return;
		} catch (Exception e) {
			logger.error("Failed during click Image " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[contains(text(),\"" + param1
					+ "\")]/following::*[normalize-space(text())=\"" + param2 + "\"])[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//h1[contains(text(),\"" + param1
					+ "\")]/following::*[normalize-space(text())=\"" + param2 + "\"])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(1000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//h1[contains(text(),\"param1\")]/following::*[normalize-space(text())=\"param2\"])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during click Image " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[contains(text(),\"" + param1
					+ "\")]/following::*[normalize-space(text())=\"" + param2 + "\"])[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//*[contains(text(),\"" + param1
					+ "\")]/following::*[normalize-space(text())=\"" + param2 + "\"])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(1000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//*[contains(text(),\"param1\")]/following::*[normalize-space(text())=\"param2\"])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error(e.getMessage());
			Thread.currentThread().interrupt();
			logger.error("Failed during Click Button.");
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void clickTableLink(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("(//table[@summary=\"" + param1 + "\"]//a)[1]")));

			WebElement waittext = driver.findElement(By.xpath("(//table[@summary=\"" + param1 + "\"]//a)[1]"));
			Thread.sleep(4000);
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(2000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//table[@summary=\"param1\"]//a)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during click Button " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())=\"" + param1
					+ "\"]/following::table[@summary=\"Main Task List\"]//a)[2]")));

			WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())=\"" + param1
					+ "\"]/following::table[@summary=\"Main Task List\"]//a)[2]"));
			Thread.sleep(4000);
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(2000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//*[normalize-space(text())=\"param1\"]/following::table[@summary=\"Main Task List\"]//a)[2]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
		} catch (Exception e) {
			logger.error(e.getMessage());
			Thread.currentThread().interrupt();
			logger.error("Failed during Click action.");
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void tableRowSelect(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("(//table[@summary=\"" + param1 + "\"]//td)[1]")));

			WebElement waittext = driver.findElement(By.xpath("(//table[@summary=\"" + param1 + "\"]//td)[1]"));
			Thread.sleep(5000);
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(2000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//table[@summary=\"param1\"]//td)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
		} catch (Exception e) {
			logger.error(e.getMessage());
			Thread.currentThread().interrupt();
			logger.error("Failed during Table row select ");
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void actionApprove(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			Thread.sleep(2000);
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath(("//a[normalize-space(text())=\"" + param1 + "\"][1]"))));
			WebElement waittext = driver.findElement(By.xpath(("//a[normalize-space(text())=\"" + param1 + "\"][1]")));
			waittext.click();
			Thread.sleep(3000);
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath(("(//div[@class='AFPopupMenuPopup']//td[normalize-space(text())=\"" + param2 + "\"])[1]"))));
			WebElement approve = driver.findElement(By.xpath(("(//div[@class='AFPopupMenuPopup']//td[normalize-space(text())=\"" + param2 + "\"])[1]")));
			highlightElement(driver, fetchMetadataVO, approve, fetchConfigVO);
			clickValidateXpath(driver, fetchMetadataVO, approve, fetchConfigVO);
			screenshot(driver, fetchMetadataVO, customerDetails);
			String xpath = "(//div[@class='AFPopupMenuPopup']//td[normalize-space(text())=\"" + param2 + "\"])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
		} catch (Exception e) {
			logger.error(e.getMessage());
			Thread.currentThread().interrupt();
			logger.error("Failed during Click Approve Button.");
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void clickLink(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			if (param1.equalsIgnoreCase("Performance Goals")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath(("//*[text()=\"Performance Goals\"]/following::a[1]"))));
				WebElement waittext = driver.findElement(By.xpath("//*[text()=\"Performance Goals\"]/following::a[1]"));
				Thread.sleep(5000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[text()=\"Performance Goals\"]/following::a[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			logger.error("Failed during click Link " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Goals") && param2.equalsIgnoreCase("Performance Goals")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath(("//*[text()=\"Performance Goals\"]/following::a[1]"))));
				WebElement waittext = driver.findElement(By.xpath("//*[text()=\"Performance Goals\"]/following::a[1]"));
				Thread.sleep(5000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[text()=\"Performance Goals\"]/following::a[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			logger.error("Failed during click Link " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Viewing plan")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//span[normalize-space(text())=\"" + param1 + "\"]/following::a[1]")));

				wait.until(ExpectedConditions.textToBePresentInElementLocated(
						By.xpath("//span[normalize-space(text())=\"" + param1 + "\"]"), param1));
				WebElement waittext = driver
						.findElement(By.xpath("//span[normalize-space(text())=\"" + param1 + "\"]/following::a[1]"));
				Thread.sleep(4000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//span[normalize-space(text())=\" param1 \"]/following::a[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return;
			}
		} catch (Exception e) {
			logger.error("Failed during click Link " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param2 != null) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(
						ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[normalize-space(text())=\"" + param1
								+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]/following::a[1]")));

				wait.until(
						ExpectedConditions
								.textToBePresentInElementLocated(
										By.xpath("//h1[normalize-space(text())=\"" + param1
												+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]"),
										param2));
				WebElement waittext = driver.findElement(By.xpath("//h1[normalize-space(text())=\"" + param1
						+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]/following::a[1]"));
				Thread.sleep(4000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//h1[normalize-space(text())=\"param1\"]/following::label[normalize-space(text())=\"param2\"]/following::a[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;

			}
		} catch (Exception e) {
			logger.error("Failed during click Link " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Select Template")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"//*[normalize-space(text())=\"" + param1 + "\"]/following::a[@title=\"" + param2 + "\"][1]")));
				WebElement waittext = driver.findElement(By.xpath(
						"//*[normalize-space(text())=\"" + param1 + "\"]/following::a[@title=\"" + param2 + "\"][1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::a[@title=\"param2\"][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during click Link " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
//		JavascriptExecutor js = (JavascriptExecutor) driver;

			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath(("//a[@title=\"" + param1 + "\" and not(@aria-disabled)]"))));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath("//a[@title=\"" + param1 + "\" and not(@aria-disabled)]"), param1));
			WebElement waittext = driver
					.findElement(By.xpath(("//a[@title=\"" + param1 + "\" and not(@aria-disabled)]")));
//	     	js.executeScript("document.body.style.zoom=\"90%\"");
			Thread.sleep(4000);
			Actions actions = new Actions(driver);
			actions.click(waittext).build().perform();
//			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			// js.executeScript("document.body.style.zoom=\"100%\"");
			takeScreenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//a[@title= 'param1' and not(@aria-disabled)]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during click Link " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			// Thread.sleep(2000);
//		JavascriptExecutor js = (JavascriptExecutor) driver;

			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath(("//a[normalize-space(text())=\"" + param1 + "\"][1]"))));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath("//a[normalize-space(text())=\"" + param1 + "\"][1]"), param1));
			WebElement waittext = driver.findElement(By.xpath(("//a[normalize-space(text())=\"" + param1 + "\"][1]")));
//		js.executeScript("document.body.style.zoom=\"90%\"");
			Thread.sleep(5000);
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			// js.executeScript("document.body.style.zoom='100%'");
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//a[normalize-space(text())=\"param1\"][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during click Link " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("(//a[contains(@id,\"" + param1 + "\")])[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//a[contains(@id,\"" + param1 + "\")])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//a[contains(@id,\" param1 \")])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during click Link " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@title=\"" + param1 + "\"]")));
			WebElement waittext = driver.findElement(By.xpath("//div[@title=\"" + param1 + "\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//div[@title=\" param1\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during click Link " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@title=\"" + param1 + "\"]")));
			WebElement waittext = driver.findElement(By.xpath("//a[@title=\"" + param1 + "\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//a[@title=\" param1 \"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during click Link " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(
					ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(@title,\"" + param1 + "\")]")));
			WebElement waittext = driver.findElement(By.xpath("//a[contains(@title,\"" + param1 + "\")]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//a[contains(@title,\"param1\")]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during click Link " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(
					ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@title,\"" + param1 + "\")]")));
			WebElement waittext = driver.findElement(By.xpath("//*[contains(@title,\"" + param1 + "\")]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[contains(@title,\" param1 \")]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during click Link " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		// Need to check for what purpose
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("(//div[contains(text(),\"" + param1 + "\")])[2]")));
			WebElement waittext = driver.findElement(By.xpath("(//div[contains(text(),\"" + param1 + "\")])[2]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//div[contains(text(),\" param1 \")])[2]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during click Link " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//a[@role=\"" + param1 + "\"]"))));
			WebElement waittext = driver.findElement(By.xpath(("//a[@role=\"" + param1 + "\"]")));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//a[@role=\" param1 \"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during click Link " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::a[normalize-space(text())=\"" + param2 + "\"]"))));
			WebElement waittext = driver.findElement(By.xpath(("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::a[normalize-space(text())=\"" + param2 + "\"]")));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[normalize-space(text())=\"param1\"]/following::a[normalize-space(text())=\"param2\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
		} catch (Exception e) {
			logger.error(e.getMessage());
			Thread.currentThread().interrupt();
			logger.error("Failed during Click Link");
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void clickRadiobutton(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {
		try {
			Thread.sleep(3000);
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					("(//*[normalize-space(text())=\"" + param1 + "\"]/following::label[normalize-space(text())=\""
							+ param2 + "\"]/following::label[normalize-space(text())=\"" + keysToSend + "\"])[1]"))));
			wait.until(
					ExpectedConditions
							.textToBePresentInElementLocated(
									By.xpath("//*[normalize-space(text())=\"" + param1
											+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]"),
									param2));
			WebElement waittext = driver.findElement(By.xpath(
					"(//*[normalize-space(text())=\"" + param1 + "\"]/following::label[normalize-space(text())=\""
							+ param2 + "\"]/following::label[normalize-space(text())=\"" + keysToSend + "\"])[1]"));
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(500);

			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//*[normalize-space(text())=\"param1\"]/following::label[normalize-space(text())=\"param2\"]/following::label[normalize-space(text())=\" keysToSend\"])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during click Link " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("(//*[normalize-space(text())=\"" + param1
					+ "\"]/following::label[normalize-space(text())=\"" + keysToSend + "\"])[1]"))));
			WebElement waittext = driver.findElement(By.xpath(("(//*[normalize-space(text())=\"" + param1
					+ "\"]/following::label[normalize-space(text())=\"" + keysToSend + "\"])[1]")));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(500);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//*[normalize-space(text())=\" param1\"]/following::label[normalize-space(text())=\"keysToSend\"])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
		} catch (Exception e) {
			logger.error("Failed during click Link " + e.getMessage());
			Thread.currentThread().interrupt();
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void clickCheckbox(WebDriver driver, String param1, String keysToSend, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			if ((param1.equalsIgnoreCase("Create Performance Documents"))
					|| (param1.equalsIgnoreCase("Employee Final Feedback"))
					|| (param1.equalsIgnoreCase("Performance Documents"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						("//*[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
								+ keysToSend + "\"]/preceding::label[@id][1]"))));
				WebElement waittext = driver.findElement(By.xpath(
						("//*[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
								+ keysToSend + "\"]/preceding::label[@id][1]")));
				Thread.sleep(4000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(1000);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"keysToSend\"]/preceding::label[@id][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during click check box " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		
				try {
					if (param1.equalsIgnoreCase("Goals")) {
						WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

						wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("(//*[normalize-space(text())=\"Goals\"]/following::*[normalize-space(text())=\""+ keysToSend + "\"]/preceding::input[@type='checkbox'])[1]"))));
						WebElement waittext = driver.findElement(By.xpath(("(//*[normalize-space(text())=\"Goals\"]/following::*[normalize-space(text())=\""+ keysToSend + "\"]/preceding::input[@type='checkbox'])[1]")));
						Thread.sleep(4000);
						Actions actions = new Actions(driver);
						actions.moveToElement(waittext).build().perform();
						clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
						Thread.sleep(1000);
						screenshot(driver, fetchMetadataVO, customerDetails);
						String scripNumber = fetchMetadataVO.getScriptNumber();
						String xpath = "(//*[normalize-space(text())=\"Goals\"]/following::*[normalize-space(text())=\""+ keysToSend + "\"]/preceding::input[@type='checkbox'])[1]";
						String scriptID = fetchMetadataVO.getScriptId();
						String lineNumber = fetchMetadataVO.getLineNumber();
						service.saveXpathParams(scriptID, lineNumber, xpath);
						return;
					}
				} catch (Exception e) {
					logger.error("Failed during click check box " + e.getMessage());
					Thread.currentThread().interrupt();
				}

				try {
			if (param1.equalsIgnoreCase("Participant Feedback")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[normalize-space(text())=\""
						+ param1 + "\"]/following::*[normalize-space(text())=\"" + keysToSend
						+ "\"]/preceding::label[normalize-space(text())=\"Participant\"]/preceding::label[1]"))));
				WebElement waittext = driver.findElement(By.xpath(("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::*[normalize-space(text())=\"" + keysToSend
						+ "\"]/preceding::label[normalize-space(text())=\"Participant\"]/preceding::label[1]")));
				Thread.sleep(4000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(1000);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"keysToSend\"]/preceding::label[normalize-space(text())=\"Participant\"]/preceding::label[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during click check box " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (keysToSend.equalsIgnoreCase("Include terminated work relationships")
					|| keysToSend.equalsIgnoreCase("Managers cannot update due dates")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[normalize-space(text())=\""
						+ param1 + "\"]/following::*[normalize-space(text())=\"" + keysToSend + "\"]"))));
				wait.until(
						ExpectedConditions.textToBePresentInElementLocated(
								By.xpath("//*[normalize-space(text())=\"" + param1
										+ "\"]/following::*[normalize-space(text())=\"" + keysToSend + "\"]"),
								keysToSend));
				WebElement waittext = driver.findElement(By.xpath(("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::*[normalize-space(text())=\"" + keysToSend + "\"]")));
				Thread.sleep(4000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(1000);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"keysToSend\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during click check box " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Goals") && (keysToSend.equalsIgnoreCase("Private"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(4000);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[normalize-space(text())=\""
						+ param1 + "\"]/following::*[normalize-space(text())=\"" + keysToSend + "\"]"))));
				wait.until(
						ExpectedConditions.textToBePresentInElementLocated(
								By.xpath("//*[normalize-space(text())=\"" + param1
										+ "\"]/following::*[normalize-space(text())=\"" + keysToSend + "\"]"),
								keysToSend));
				WebElement waittext = driver.findElement(By.xpath(("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::*[normalize-space(text())=\"" + keysToSend + "\"]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(1000);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"keysToSend\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during click check box " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Goal Weights") || param1.equalsIgnoreCase("Refresh Options")
					|| param1.equalsIgnoreCase("Disability Info")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[normalize-space(text())=\""
						+ param1 + "\"]/following::*[normalize-space(text())=\"" + keysToSend + "\"]"))));
				wait.until(
						ExpectedConditions.textToBePresentInElementLocated(
								By.xpath("//*[normalize-space(text())=\"" + param1
										+ "\"]/following::*[normalize-space(text())=\"" + keysToSend + "\"]"),
								keysToSend));
				WebElement waittext = driver.findElement(By.xpath(("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::*[normalize-space(text())=\"" + keysToSend + "\"]")));
				Thread.sleep(4000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(1000);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"keysToSend\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during click check box " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					("//label[normalize-space(text())=\"" + param1 + "\"]/following::span[normalize-space(text())=\""
							+ keysToSend + "\"]/preceding::label[1]"))));
			wait.until(
					ExpectedConditions.textToBePresentInElementLocated(
							By.xpath("//label[normalize-space(text())=\"" + param1
									+ "\"]/following::span[normalize-space(text())=\"" + keysToSend + "\"]"),
							keysToSend));
			WebElement waittext = driver.findElement(By.xpath("//label[normalize-space(text())=\"" + param1
					+ "\"]/following::span[normalize-space(text())=\"" + keysToSend + "\"]/preceding::label[1]"));
			Thread.sleep(1000);
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(500);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//label[normalize-space(text())=\"param1\"]/following::span[normalize-space(text())=\"keysToSend\"]/preceding::label[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during click check box " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::span[text()=\"" + keysToSend + "\"]/preceding::label[1]"))));
			// wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[normalize-space(text())=\""+param1+"\"]/following::span[text()=\""+keysToSend+"\"]"),
			// keysToSend));
			WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::span[text()=\"" + keysToSend + "\"]/preceding::label[1]"));
			Thread.sleep(1000);
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			waittext.click();
			// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(500);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[normalize-space(text())=\"param1\"]/following::span[text()=\"keysToSend\"]/preceding::label[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during click check box " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + keysToSend + "\"]/preceding::label[1]")));
			wait.until(
					ExpectedConditions
							.textToBePresentInElementLocated(
									By.xpath("//*[normalize-space(text())=\"" + param1
											+ "\"]/following::*[normalize-space(text())=\"" + keysToSend + "\"]"),
									keysToSend));
			WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + keysToSend + "\"]/preceding::label[1]"));
			Thread.sleep(1000);
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(500);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"keysToSend\"]/preceding::label[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during click check box " + e.getMessage());
			Thread.currentThread().interrupt();
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
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(500);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//label[normalize-space(text())=\"param1\"]/following::label[normalize-space(text())=\"keysToSend\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during click check box " + e.getMessage());
			Thread.currentThread().interrupt();
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
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(3000);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"keysToSend\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during click check box " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath(("//label[normalize-space(text())=\"" + keysToSend + "\"]"))));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath("//label[normalize-space(text())=\"" + keysToSend + "\"]"), keysToSend));
			WebElement waittext = driver
					.findElement(By.xpath("//label[normalize-space(text())=\"" + keysToSend + "\"]"));
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(500);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//label[normalize-space(text())=\"keysToSend\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
		} catch (Exception e) {
			logger.error(e.getMessage());
			Thread.currentThread().interrupt();
			logger.error("Failed during Click action.");
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void clickNotificationLink(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {

		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath(("//*[@placeholder=\"" + param1 + "\"]/following::a[1]"))));

			WebElement waittext = driver.findElement(By.xpath("//*[@placeholder=\"" + param1 + "\"]/following::a[1]"));
			Thread.sleep(4000);
			Actions actions = new Actions(driver);
			// actions.moveToElement(waittext).build().perform();
			// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			waittext.click();
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[@placeholder=\"param1\"]/following::a[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
		} catch (Exception e) {
			logger.error(e.getMessage());
			Thread.currentThread().interrupt();
			logger.error("Failed during Click action.");
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void clickLinkAction(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {

		try {
			if (param1.equalsIgnoreCase("Goals") && param2.equalsIgnoreCase("Edit")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						("//*[normalize-space(text())=\"" + param1 + "\"]/following::span[normalize-space(text())=\""
								+ keysToSend + "\"]/following::img[contains(@title,\"" + param2 + "\")][1]"))));

				wait.until(
						ExpectedConditions.textToBePresentInElementLocated(
								By.xpath("//*[normalize-space(text())=\"" + param1
										+ "\"]/following::span[normalize-space(text())=\"" + keysToSend + "\"]"),
								keysToSend));
				WebElement waittext = driver.findElement(By.xpath(
						"//*[normalize-space(text())=\"" + param1 + "\"]/following::span[normalize-space(text())=\""
								+ keysToSend + "\"]/following::img[contains(@title,\"" + param2 + "\")][1]"));
				Thread.sleep(2000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::span[normalize-space(text())=\"keysToSend\"]/following::img[contains(@title,\"param2\")][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during click Link Action " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath(("//*[normalize-space(text())=\"" + param1 + "\"]/following::a[normalize-space(text())=\""
							+ keysToSend + "\"]/following::img[contains(@title,\"" + param2 + "\")][1]"))));

			wait.until(
					ExpectedConditions
							.textToBePresentInElementLocated(
									By.xpath("//*[normalize-space(text())=\"" + param1
											+ "\"]/following::a[normalize-space(text())=\"" + keysToSend + "\"]"),
									keysToSend));
			WebElement waittext = driver.findElement(
					By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::a[normalize-space(text())=\""
							+ keysToSend + "\"]/following::img[contains(@title,\"" + param2 + "\")][1]"));
			Thread.sleep(2000);
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[normalize-space(text())=\"param1\"]/following::a[normalize-space(text())=\"keysToSend\"]/following::img[contains(@title,\"param2\")][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during click Link Action " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::a[normalize-space(text())=\"" + keysToSend + "\"]")));
			WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::a[normalize-space(text())=\"" + keysToSend + "\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[normalize-space(text())=\"param1\"]/following::a[normalize-space(text())=\"keysToSend\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during click Link Action " + e.getMessage());
			Thread.currentThread().interrupt();

		}
		try {// *[text()=\"Worker Name\"]/following::*[text()=\"Kaushik (Kaushik)
				// Sekaran\"]/following::img[1]
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"(//*[normalize-space(text())=\"" + keysToSend + "\"]/following::td[normalize-space(text())=\""
							+ param1 + "\"]/following::table[1]//div)[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())=\"" + keysToSend
					+ "\"]/following::td[normalize-space(text())=\"" + param1 + "\"]/following::table[1]//div)[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//*[normalize-space(text())=\"keysToSend\"]/following::td[normalize-space(text())=\"param1\"]/following::table[1]//div)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during click check box " + e.getMessage());
			Thread.currentThread().interrupt();
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
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"keysToSend\"]/following::img[contains(@title,\"param2\")])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
		} catch (Exception e) {
			Thread.currentThread().interrupt();
			logger.error("Failed during click Link Action " + e.getMessage());
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public String textarea(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {
		try {
			if(param1.equalsIgnoreCase("Employee Comments"))
			{
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()=\"Employee Comments\"]/following::p[1]")));
			Thread.sleep(1000);
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
			By.xpath("//*[text()=\"Employee Comments\"]/following::p[1]"),param2));
			WebElement waittill = driver.findElement(By.xpath("//*[text()=\"Employee Comments\"]/following::p[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			Thread.sleep(500);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[text()=\"Employee Comments\"]/following::p[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return keysToSend;
			}
	} catch (Exception e) {
			 logger.error("Failed during text area " + e.getMessage());
			Thread.currentThread().interrupt();
			 }
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[text()=\"" + param1 + "\"]/following::*[text()=\"" + param2 + "\"]/following::p)[1]")));
			
			WebElement waittill = driver.findElement(By.xpath("(//*[text()=\"" + param1 + "\"]/following::*[text()=\"" + param2 + "\"]/following::p)[1]"));
			Thread.sleep(1000);
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			screenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(500);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//*[text()=\"" + param1 + "\"]/following::*[text()=\"" + param2 + "\"]/following::p)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return keysToSend;
		} catch (Exception e) {
			logger.error("Failed during text area " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()=\"" + param1 + "\"]/following::p[1]")));
			Thread.sleep(1000);
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
			By.xpath("//*[text()=\"" + param1 + "\"]/following::p[1]"),param2));
			WebElement waittill = driver.findElement(By.xpath("//*[text()=\"" + param1 + "\"]/following::p[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			Thread.sleep(500);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[text()=\"" + param1 + "\"]/following::p[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return keysToSend;
		} catch (Exception e) {
			logger.error("Failed during text area " + e.getMessage());
			Thread.currentThread().interrupt();
			 }
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())=\"" + param1
					+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]/following::textarea)[1]")));
			
			wait.until(
					ExpectedConditions
							.textToBePresentInElementLocated(
									By.xpath("//*[normalize-space(text())=\"" + param1
											+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]"),
									param2));
			WebElement waittill = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]/following::textarea[1]"));
			Thread.sleep(1000);
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			Thread.sleep(500);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[normalize-space(text())=\"param1\"]/following::label[normalize-space(text())=\"param2\"]/following::textarea[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return keysToSend;
		} catch (Exception e) {
			logger.error("Failed during text area " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//body[@dir=\"ltr\"]")));
			
			WebElement waittill = driver.findElement(By.xpath("//body[@dir=\"ltr\"]"));
			Thread.sleep(1000);
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(500);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//body[@dir=\"ltr\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return keysToSend;
		} catch (Exception e) {
			logger.error("Failed during text area " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("//body[contains(@class,\"contents_ltr\")][1]")));
			
			WebElement waittill = driver.findElement(By.xpath("//body[contains(@class,\"contents_ltr\")][1]"));
			Thread.sleep(1000);
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(500);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//body[contains(@class,\"contents_ltr\")][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return keysToSend;
		} catch (Exception e) {
			logger.error("Failed during text area " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::textarea)[1]")));
			
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[normalize-space(text())=\""
					+ param1 + "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]"), param2));
			WebElement waittill = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::textarea[1]"));
			Thread.sleep(1000);
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			Thread.sleep(500);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]/following::textarea[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return keysToSend;
		} catch (Exception e) {
			logger.error(e.getMessage());
			Thread.currentThread().interrupt();
			logger.error("Failed during Click textArea ");
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public String sendValue(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {
		try {
			if (param1.equalsIgnoreCase("Search")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//div[@role=\"" + param1 + "\" and not(@style)]//label[normalize-space(text())=\""
								+ param2 + "\"]/following::input[1]"))));
				wait.until(
						ExpectedConditions.textToBePresentInElementLocated(
								By.xpath("//div[@role=\"" + param1
										+ "\" and not(@style)]//label[normalize-space(text())=\"" + param2 + "\"]"),
								param2));
				WebElement waittill = driver.findElement(
						By.xpath(("//div[@role=\"" + param1 + "\" and not(@style)]//label[normalize-space(text())=\""
								+ param2 + "\"]/following::input[1]")));
				screenshot(driver, fetchMetadataVO, customerDetails);
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//div[@role=\"param1\" and not(@style)]//label[normalize-space(text())=\"param2\"]/following::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return keysToSend;
			} else {
				logger.info("Not Entered Name");
			}
		} catch (Exception e) {
			logger.error("Failed during send value " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Search") && (param2.equalsIgnoreCase("Match With"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"//*[normalize-space(text())=\"" + param1 + "\"]/following::label[normalize-space(text())=\""
								+ param2 + "\"]/preceding::input[@type=\"text\"][1]")));
				WebElement waittill = driver.findElement(By.xpath(
						"//*[normalize-space(text())=\"" + param1 + "\"]/following::label[normalize-space(text())=\""
								+ param2 + "\"]/preceding::input[@type=\"text\"][1]"));
				Thread.sleep(6000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				Thread.sleep(2000);
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::label[normalize-space(text())=\"param2\"]/preceding::input[@type=\"text\"][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return keysToSend;
			}
		} catch (Exception e) {
			logger.error("Failed during send value " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Performance Documents") && (param2.equalsIgnoreCase("Review Period"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[normalize-space(text())=\""
						+ param1
						+ "\"]/following::*[normalize-space(text())=\"Review Period *\"]/following::input[contains(@id,\"HCMPERFORMANCE_FUSE_PERFORMANCE\") and not(@style)])[1]")));
				WebElement waittill = driver.findElement(By.xpath("(//h1[normalize-space(text())=\"" + param1
						+ "\"]/following::*[normalize-space(text())=\"Review Period *\"]/following::input[contains(@id,\"HCMPERFORMANCE_FUSE_PERFORMANCE\") and not(@style)])[1]"));
				Thread.sleep(6000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				Thread.sleep(2000);
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//h1[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"Review Period *\"]/following::input[contains(@id,\"HCMPERFORMANCE_FUSE_PERFORMANCE\") and not(@style)])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return keysToSend;
			}
		} catch (Exception e) {
			logger.error("Failed during send value " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Participant Feedback") && (param2.equalsIgnoreCase("Participant"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"//*[normalize-space(text())=\"" + param1 + "\"]/following::td[normalize-space(text())=\""
								+ param2 + "\"]/following::input[1]")));
				WebElement waittill = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::td[normalize-space(text())=\"" + param2 + "\"]/following::input[1]"));
				Thread.sleep(6000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				Thread.sleep(2000);
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::td[normalize-space(text())=\"param2\"]/following::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return keysToSend;
			}
		} catch (Exception e) {
			logger.error("Failed during send value " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Transfer")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"//*[normalize-space(text())=\"" + param1 + "\"]/following::label[normalize-space(text())=\""
								+ param2 + "\"]/following::input[not(@type=\"hidden\")]")));
				WebElement waittill = driver.findElement(By.xpath(
						"//*[normalize-space(text())=\"" + param1 + "\"]/following::label[normalize-space(text())=\""
								+ param2 + "\"]/following::input[not(@type=\"hidden\")]"));
				Thread.sleep(6000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				Thread.sleep(2000);
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::label[normalize-space(text())=\"param2\"]/following::input[not(@type=\"hidden\")]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return keysToSend;
			}
		} catch (Exception e) {
			logger.error("Failed during send value " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Performance Documents")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[normalize-space(text())=\""
						+ param1 + "\"]/following::*[normalize-space(text())=\"" + param2
						+ "\"]/following::input[contains(@id,\"HCMPERFORMANCE_FUSE_PERFORMANCE\") and not(@style)])[1]")));
				WebElement waittill = driver.findElement(By.xpath("(//h1[normalize-space(text())=\"" + param1
						+ "\"]/following::*[normalize-space(text())=\"" + param2
						+ "\"]/following::input[contains(@id,\"HCMPERFORMANCE_FUSE_PERFORMANCE\") and not(@style)])[1]"));
				Thread.sleep(6000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				Thread.sleep(2000);
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//h1[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]/following::input[contains(@id,\"HCMPERFORMANCE_FUSE_PERFORMANCE\") and not(@style)])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return keysToSend;
			}
		} catch (Exception e) {
			logger.error("Failed during send value " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Duplicate Plan Cycle")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(),\"" + param1
						+ "\")]/following::label[text()=\"" + param2 + "\"]/following::input[1]")));
				WebElement waittill = driver.findElement(By.xpath("//div[contains(text(),\"" + param1
						+ "\")]/following::label[text()=\"" + param2 + "\"]/following::input[1]"));
				Thread.sleep(6000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				Thread.sleep(2000);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//div[contains(text(),\"param1\")]/following::label[text()=\"param2\"]/following::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return keysToSend;
			}
		} catch (Exception e) {
			logger.error("Failed during send value " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Send Email Notification")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"//h1[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
								+ param2 + "\"]/preceding-sibling::input[1]")));
				WebElement waittill = driver.findElement(By.xpath("//h1[normalize-space(text())=\"" + param1
						+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/preceding-sibling::input[1]"));
				Thread.sleep(6000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
				Thread.sleep(2000);
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//h1[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]/preceding-sibling::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);

				return keysToSend;
			}
		} catch (Exception e) {
			logger.error("Failed during send value " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Location") && (param2.equalsIgnoreCase("Location"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By
						.xpath("(//h1[contains(text(),\"" + param1 + "\")]/following::label[normalize-space(text())=\""
								+ param2 + "\"]/following::input[not(@type=\"hidden\")])[1]")));

				wait.until(
						ExpectedConditions.textToBePresentInElementLocated(
								By.xpath("//h1[contains(text(),\"" + param1
										+ "\")]/following::label[normalize-space(text())=\"" + param2 + "\"]"),
								param2));
				WebElement waittill = driver.findElement(By
						.xpath("(//h1[contains(text(),\"" + param1 + "\")]/following::label[normalize-space(text())=\""
								+ param2 + "\"]/following::input[not(@type=\"hidden\")])[1]"));
				Thread.sleep(1000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				for (int i = 0; i <= 2; i++) {
					try {
						typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
						break;
					} finally {

					}
				}
				Thread.sleep(500);
				String xpath = "(//h1[contains(text(),\"param1\")]/following::label[normalize-space(text())=\"param2\"]/following::input[not(@type=\"hidden\")])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return keysToSend;
			}
		} catch (Exception e) {
			logger.error("Failed during send value " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[contains(text(),\"" + param1
					+ "\")]/following::label[normalize-space(text())=\"" + param2 + "\"]/following::input)[1]")));

			wait.until(
					ExpectedConditions
							.textToBePresentInElementLocated(
									By.xpath("//h1[contains(text(),\"" + param1
											+ "\")]/following::label[normalize-space(text())=\"" + param2 + "\"]"),
									param2));
			WebElement waittill = driver.findElement(By.xpath("//h1[contains(text(),\"" + param1
					+ "\")]/following::label[normalize-space(text())=\"" + param2 + "\"]/following::input[1]"));
			Thread.sleep(1000);
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			Thread.sleep(500);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//h1[contains(text(),\"param1\")]/following::label[normalize-space(text())=\"param2\"]/following::input[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return keysToSend;
		} catch (Exception e) {
			logger.error("Failed during send value " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("//*[contains(@placeholder,\"" + param1 + "\")]")));
			WebElement waittill = driver.findElement(By.xpath("//*[contains(@placeholder,\"" + param1 + "\")]"));
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			Thread.sleep(1000);
			String xpath = "//*[contains(@placeholder,\"param1\")]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return keysToSend;
		} catch (Exception e) {
			logger.error("Failed during send value " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"(//label[normalize-space(text())=\"" + param1 + "\"]/following::label[normalize-space(text())=\""
							+ param2 + "\"]/following::input)[1]")));

			wait.until(
					ExpectedConditions
							.textToBePresentInElementLocated(
									By.xpath("//label[normalize-space(text())=\"" + param1
											+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]"),
									param2));
			WebElement waittill = driver.findElement(By.xpath("//label[normalize-space(text())=\"" + param1
					+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]/following::input[1]"));
			Thread.sleep(1000);
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			Thread.sleep(500);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//label[normalize-space(text())=\"param1\"]/following::label[normalize-space(text())=\"param2\"]/following::input[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return keysToSend;
		} catch (Exception e) {
			logger.error("Failed during send value " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())=\"" + param1
					+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]/following::input)[1]")));

			wait.until(
					ExpectedConditions
							.textToBePresentInElementLocated(
									By.xpath("//*[normalize-space(text())=\"" + param1
											+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]"),
									param2));
			WebElement waittill = driver.findElement(By.xpath("(//*[normalize-space(text())=\"" + param1
					+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]/following::input)[1]"));
			Thread.sleep(1000);
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			Thread.sleep(500);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//*[normalize-space(text())=\"param1\"]/following::label[normalize-space(text())=\"param2\"]/following::input)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return keysToSend;
		} catch (Exception e) {
			logger.error("Failed during send value " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::input)[1]")));
			WebElement waittill = driver.findElement(By.xpath("(//*[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::input)[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			if (keysToSend.equalsIgnoreCase("Annual Performance Evaluation 2019")
					|| (keysToSend.equalsIgnoreCase("Varshitha (Varshitha) Reddy Karna"))) {
				Thread.sleep(2000);
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
			}
			Thread.sleep(2000);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]/following::input)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return keysToSend;
		} catch (Exception e) {
			logger.error(e.getMessage());
			Thread.currentThread().interrupt();
			logger.error("Failed during send value " + fetchMetadataVO.getScriptNumber());
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void dropdownTexts(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {
		try {
			if (param2.equalsIgnoreCase("Plan") || param2.equalsIgnoreCase("Option")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//div[@class=\"AFPopupMenuPopup\"]//li[text()=\"" + keysToSend + "\"]")));
				WebElement waittext = driver
						.findElement(By.xpath("//div[@class=\"AFPopupMenuPopup\"]//li[text()=\"" + keysToSend + "\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String xpath = "//div[@class=\"AFPopupMenuPopup\"]//li[text()=\"keysToSend\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during Dropdown text " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("(//div[contains(@id,\"popup-container\")]//*[normalize-space(text())=\"" + keysToSend
							+ "\"])[1]")));

			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(
					"(//div[contains(@id,\"popup-container\")]//*[normalize-space(text())=\"" + keysToSend + "\"])[1]"),
					keysToSend));
			WebElement waittext = driver
					.findElement(By.xpath("(//div[contains(@id,\"popup-container\")]//*[normalize-space(text())=\""
							+ keysToSend + "\"])[1]"));
			Thread.sleep(4000);
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(2000);
			String xpath = "(//div[contains(@id,\"popup-container\")]//*[normalize-space(text())=\"keysToSend\"])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
	//		service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during Dropdown text " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//h1[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
							+ param2 + "\"]/following::span[normalize-space(text())=\"" + keysToSend + "\"]")));

			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath("//h1[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
							+ param2 + "\"]/following::span[normalize-space(text())=\"" + keysToSend + "\"]"),
					keysToSend));
			WebElement waittext = driver.findElement(
					By.xpath("//h1[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
							+ param2 + "\"]/following::span[normalize-space(text())=\"" + keysToSend + "\"]"));
			Thread.sleep(4000);
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String xpath = "//h1[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]/following::span[normalize-space(text())=\"keysToSend\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during Dropdown text " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param2.equalsIgnoreCase("Performance Document Types")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())=\""
						+ param1 + "\"]/following::*[normalize-space(text())=\"" + param2
						+ "\"]/following::[@type=\"checkbox\"]/parent::label[normalize-space(text())=\"" + keysToSend
						+ "\"]")));
				wait.until(
						ExpectedConditions
								.textToBePresentInElementLocated(
										By.xpath("//h1[normalize-space(text())=\"" + param1
												+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]"),
										param2));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::*[normalize-space(text())=\"" + param2
						+ "\"]/following::[@type=\"checkbox\"]/parent::label[normalize-space(text())=\"" + keysToSend
						+ "\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]/following::[@type=\"checkbox\"]/parent::label[normalize-space(text())=\"keysToSend\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during Dropdown text " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Send Email Notification")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//div[@class=\"AFPopupMenuPopup\"]//li[text()=\"" + keysToSend + "\"]")));
				WebElement waittext = driver
						.findElement(By.xpath("//div[@class=\"AFPopupMenuPopup\"]//li[text()=\"" + keysToSend + "\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String xpath = "//div[@class=\"AFPopupMenuPopup\"]//li[text()=\"keysToSend\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during Dropdown text " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Create Performance Documents") && (param2.equalsIgnoreCase("Review Period"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("//div[normalize-space(text())=\"" + keysToSend + "\"]")));
				WebElement waittext = driver
						.findElement(By.xpath("//div[normalize-space(text())=\"" + keysToSend + "\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String xpath = "//div[normalize-space(text())=\"keysToSend\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during Dropdown text " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Performance Documents") && (param2.equalsIgnoreCase("Review Period"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("//div[normalize-space(text())=\"" + keysToSend + "\"]")));
				WebElement waittext = driver
						.findElement(By.xpath("//div[normalize-space(text())=\"" + keysToSend + "\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String xpath = "//div[normalize-space(text())=\"keysToSend\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during Dropdown text " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Participant Feedback") && (param2.equalsIgnoreCase("Review Period"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("//div[normalize-space(text())=\"" + keysToSend + "\"]")));
				WebElement waittext = driver
						.findElement(By.xpath("//div[normalize-space(text())=\"" + keysToSend + "\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				String xpath = "//div[normalize-space(text())=\"keysToSend\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during Dropdown text " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Change Access for All Managers")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())=\""
						+ param1 + "\"]/following::*[normalize-space(text())=\"" + param2
						+ "\"]/following::*[@type=\"checkbox\"]/following::*[normalize-space(text())=\"" + keysToSend
						+ "\"]")));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::*[normalize-space(text())=\"" + param2
						+ "\"]/following::*[@type=\"checkbox\"]/following::*[normalize-space(text())=\"" + keysToSend
						+ "\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(1000);
				tab(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]/following::*[@type=\"checkbox\"]/following::*[normalize-space(text())=\"keysToSend\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during Dropdown text " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//h1[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
							+ param2 + "\"]/following::*[normalize-space(text())=\"" + keysToSend + "\"]")));
			WebElement waittext = driver.findElement(
					By.xpath("//h1[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
							+ param2 + "\"]/following::*[normalize-space(text())=\"" + keysToSend + "\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String xpath = "//h1[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]/following::*[normalize-space(text())=\"keysToSend\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during Dropdown text " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By
					.xpath("//label[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
							+ param2 + "\"]/following::*[normalize-space(text())=\"" + keysToSend + "\"]")));
			WebElement waittext = driver.findElement(By
					.xpath("//label[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
							+ param2 + "\"]/following::*[normalize-space(text())=\"" + keysToSend + "\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String xpath = "//label[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]/following::*[normalize-space(text())=\"keysToSend\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during Dropdown text " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h3[normalize-space(text())=\"" + param1
					+ "\"]/following::label[normalize-space(text())=\"" + param2
					+ "\"][1]/following::div[contains(@style,\"position\")]//li[normalize-space(text())=\"" + keysToSend
					+ "\"]")));
			WebElement waittext = driver.findElement(By
					.xpath("//label[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
							+ param2 + "\"]/following::*[normalize-space(text())=\"" + keysToSend + "\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String xpath = "//label[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]/following::*[normalize-space(text())=\"keysToSend\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during Dropdown text " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h3[normalize-space(text())=\"" + param1
					+ "\"]/following::label[normalize-space(text())=\"" + param2
					+ "\"][1]/following::div[contains(@style,\"position\")]//li[normalize-space(text())=\"" + keysToSend
					+ "\"]")));
			WebElement waittext = driver.findElement(By.xpath("//h3[normalize-space(text())=\"" + param1
					+ "\"]/following::label[normalize-space(text())=\"" + param2
					+ "\"][1]/following::div[contains(@style,\"position\")]//li[normalize-space(text())=\"" + keysToSend
					+ "\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String xpath = "//h3[normalize-space(text())=\"param1\"]/following::label[normalize-space(text())=\"param2\"][1]/following::div[contains(@style,\"position\")]//li[normalize-space(text())=\"keysToSend\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during Dropdown text " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
							+ param2 + "\"]/following::*[@type=\"checkbox\"]/following::*[normalize-space(text())=\""
							+ keysToSend + "\"]")));
			WebElement waittext = driver.findElement(
					By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
							+ param2 + "\"]/following::*[@type=\"checkbox\"]/following::*[normalize-space(text())=\""
							+ keysToSend + "\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(500);
			String xpath = "//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]/following::*[@type=\"checkbox\"]/following::*[normalize-space(text())=\"keysToSend\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during Dropdown text " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
							+ param2 + "\"]/following::*[normalize-space(text())=\"" + keysToSend + "\"][1]")));
			WebElement waittext = driver.findElement(
					By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
							+ param2 + "\"]/following::*[normalize-space(text())=\"" + keysToSend + "\"][1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(1000);
			String xpath = "//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]/following::*[normalize-space(text())=\"keysToSend\"][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during Dropdown text " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(text(),\"Search\")]")));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//a[contains(text(),\"Search\")]"),
					"Search"));
			WebElement search = driver.findElement(By.xpath("//a[contains(text(),\"Search\")]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(search).build().perform();
			search.click();
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\"" + param2
							+ "\"]/following::input[1]")));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(
					"//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\"" + param2 + "\"]"),
					param2));
			WebElement searchResult = driver.findElement(
					By.xpath("//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\"" + param2
							+ "\"]/following::input[1]"));
			typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
			String xpath = "//a[contains(text(),\"Search\")]" + ";"
					+ "//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\"param2\"]/following::input[1]";
			if (keysToSend != null) {
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(5000);
				WebElement text = driver.findElement(
						By.xpath("//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\""
								+ param2 + "\"]/following::span[normalize-space(text())=\"" + keysToSend + "\"]"));
				text.click();
				xpath = xpath + ";"
						+ "//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\"param2\"]/following::span[normalize-space(text())=\"keysToSend\"]";
			}
			WebElement button = driver.findElement(
					By.xpath("//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\"" + param2
							+ "\"]/following::*[normalize-space(text())=\"K\"][1]"));
			button.click();
			xpath = xpath + ";"
					+ "//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\"param2\"]/following::*[normalize-space(text())=\"K\"][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during Dropdown text " + e.getMessage());
			Thread.currentThread().interrupt();
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
					"//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\"Name\"]/following::*[normalize-space(text())=\"OK\"][1]"));
			button.click();
			String xpath = "//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\"Name\"]/following::input[1]"
					+ ";" + "//span[normalize-space(text())=\"keysToSend\"]"
					+ "//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\"Name\"]/following::*[normalize-space(text())=\"OK\"][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during Dropdown text " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebElement button = driver.findElement(
					By.xpath("//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\"" + param2
							+ "\"]/following::*[normalize-space(text())=\"OK\"][1]"));
			button.click();
			String xpath = "//*[normalize-space(text())=\"Search\"]/following::*[normalize-space(text())=\"param2\"]/following::*[normalize-space(text())=\"OK\"][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during Dropdown text " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//*[contains(text(),\"" + param1 + "\")]/following::*[normalize-space(text())=\"" + param2
							+ "\"]/following::*[normalize-space(text())=\"" + keysToSend + "\"][1]")));
			WebElement waittext = driver.findElement(
					By.xpath("//*[contains(text(),\"" + param1 + "\")]/following::*[normalize-space(text())=\"" + param2
							+ "\"]/following::*[normalize-space(text())=\"" + keysToSend + "\"][1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String xpath = "//*[contains(text(),\"param1\")]/following::*[normalize-space(text())=\"param2\"]/following::*[normalize-space(text())=\"keysToSend\"][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during Dropdown text " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[contains(text(),\"" + param1
					+ "\")]/following::label[normalize-space(text())=\"" + keysToSend + "\"]/following::input)[1]")));

			wait.until(
					ExpectedConditions.textToBePresentInElementLocated(
							By.xpath("//h1[contains(text(),\"" + param1
									+ "\")]/following::label[normalize-space(text())=\"" + keysToSend + "\"]"),
							keysToSend));
			WebElement waittill = driver.findElement(By.xpath("//h1[contains(text(),\"" + param1
					+ "\")]/following::label[normalize-space(text())=\"" + keysToSend + "\"]/following::input[1]"));
			Thread.sleep(1000);
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			Thread.sleep(500);
			String xpath = "//h1[contains(text(),\"param1\")]/following::label[normalize-space(text())=\"keysToSend\"]/following::input[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
		} catch (Exception e) {
			logger.error(e.getMessage());
			Thread.currentThread().interrupt();
			logger.error("Failed during Dropdown text ");
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void multipleSendKeys(WebDriver driver, String param1, String param2, String value1, String value2,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {
		try {
			if (param1.equalsIgnoreCase("Element Details")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				WebElement waittill = driver.findElement(By.xpath("//*[text()=\"" + param1
						+ "\"]/following::*[text()=\"" + value1 + "\"]/following::input[@placeholder=\"m/d/yy\"][1]"));
				Thread.sleep(1000);
				// wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[text()=\""+param1+"\"]/following::*[text()=\""+value1+"\"]/following::input[1]"),
				// value1));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittill).build().perform();
				typeIntoValidxpath(driver, value2, waittill, fetchConfigVO, fetchMetadataVO);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[text()=\"param1\"]/following::*[text()=\"value1\"]/following::input[@placeholder=\"m/d/yy\"][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			Thread.currentThread().interrupt();
			logger.error("Failed during Click mulitiple sendkeys " + fetchMetadataVO.getScriptNumber());
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
		}
		try {

			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			WebElement waittill = driver.findElement(By.xpath(
					"//*[text()=\"" + param1 + "\"]/following::*[text()=\"" + value1 + "\"]/following::input[1]"));
			Thread.sleep(1000);
			// wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[text()=\""+param1+"\"]/following::*[text()=\""+value1+"\"]/following::input[1]"),
			// value1));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, value2, waittill, fetchConfigVO, fetchMetadataVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[text()=\"param1\"]/following::*[text()=\"value1\"]/following::input[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;

		} catch (Exception e) {
			logger.error(e.getMessage());
			Thread.currentThread().interrupt();
			logger.error("Failed during Click mulitiple sendkeys " + fetchMetadataVO.getScriptNumber());
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			WebElement waittill = driver.findElement(By.xpath("(//*[text()=\"" + param1 + "\"]/following::*[text()=\""
					+ value1 + "\"]/following::input[contains(@id,\"NewBdgtPctLst\")])[" + param2 + "]"));
			Thread.sleep(1000);
			// wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[text()=\""+param1+"\"]/following::*[text()=\""+value1+"\"]/following::input[1]"),
			// value1));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, value2, waittill, fetchConfigVO, fetchMetadataVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//*[text()=\"param1\"]/following::*[text()=\"value1\"]/following::input[contains(@id,\"NewBdgtPctLst\")])[param2]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
		} catch (Exception e) {
			logger.error(e.getMessage());
			Thread.currentThread().interrupt();
			logger.error("Failed during Click mulitiple sendkeys " + fetchMetadataVO.getScriptNumber());
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void tableSendKeys(WebDriver driver, String param1, String param2, String param3, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			WebElement waittill = driver
					.findElement(By.xpath("//h1[text()=\"" + param1 + "\"]/following::label[normalize-space(text())=\""
							+ param2 + "\"]/preceding-sibling::input[not(@type=\"hidden\")]"));

			wait.until(
					ExpectedConditions
							.textToBePresentInElementLocated(
									By.xpath("//h1[normalize-space(text())=\"" + param1
											+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]"),
									param2));
			Thread.sleep(1000);
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//h1[text()=\"param1\"]/following::label[normalize-space(text())=\"param2\"]/preceding-sibling::input[not(@type=\"hidden\")]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during Click mulitiple sendkeys " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("//h1[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/preceding-sibling::input[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//h1[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]/preceding-sibling::input[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during Click mulitiple sendkeys " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("//h1[normalize-space(text())=\"" + param1
					+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]/preceding::input[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//h1[normalize-space(text())=\"param1\"]/following::label[normalize-space(text())=\"param2\"]/preceding::input[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during Click mulitiple sendkeys " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("(//*[normalize-space(text())=\"" + param1
					+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]/preceding-sibling::input)[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//*[normalize-space(text())=\"param1\"]/following::label[normalize-space(text())=\"param2\"]/preceding-sibling::input)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during Click mulitiple sendkeys " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::table[@summary=\"" + param2 + "\"]//*[normalize-space(text())=\"" + param3
					+ "\"]/following::input[contains(@id,\"NewBdgtPctLst\")][1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[normalize-space(text())=\"param1\"]/following::table[@summary=\"param2\"]//*[normalize-space(text())=\"param3\"]/following::input[contains(@id,\"NewBdgtPctLst\")][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during Click mulitiple sendkeys " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("(//table[@summary=\"" + param1
					+ "\"]//label[normalize-space(text())=\"" + param2 + "\"]/preceding-sibling::input)[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//table[@summary=\"param1\"]//label[normalize-space(text())=\"param2\"]/preceding-sibling::input)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during Click mulitiple sendkeys " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("//h1[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::input[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//h1[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]/following::input[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
		} catch (Exception e) {
			Thread.currentThread().interrupt();
			logger.error("Failed during Click mulitiple sendkeys " + e.getMessage());
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void tableDropdownTexts(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table[@summary=\"" + param1
					+ "\"]/following::li[normalize-space(text())=\"" + keysToSend + "\"]")));
			wait.until(
					ExpectedConditions
							.textToBePresentInElementLocated(
									By.xpath("//table[@summary=\"" + param1
											+ "\"]/following::li[normalize-space(text())=\"" + keysToSend + "\"]"),
									keysToSend));
			WebElement waittext = driver.findElement(By.xpath("//table[@summary=\"" + param1
					+ "\"]/following::li[normalize-space(text())=\"" + keysToSend + "\"]"));
			clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String xpath = "//table[@summary=\"param1\"]/following::li[normalize-space(text())=\"keysToSend\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during table dropdown text " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::li[normalize-space(text())=\"" + keysToSend + "\"]")));
			wait.until(
					ExpectedConditions.textToBePresentInElementLocated(
							By.xpath("//*[normalize-space(text())=\"" + param1
									+ "\"]/following::li[normalize-space(text())=\"" + keysToSend + "\"]"),
							keysToSend));
			WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::li[normalize-space(text())=\"" + keysToSend + "\"]"));
			clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			String xpath = "//*[normalize-space(text())=\"param1\"]/following::li[normalize-space(text())=\"keysToSend\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
		} catch (Exception e) {
			Thread.currentThread().interrupt();
			logger.error("Failed during table dropdown text " + e.getMessage());
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void tableDropdownValues(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {
		try {
			if (param2.equalsIgnoreCase("Type")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"//*[normalize-space(text())=\"" + param1 + "\"]/following::label[normalize-space(text())=\""
								+ param2 + "\"]/preceding::input[2]")));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]/preceding::input[2]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickTableDropdown(driver, fetchMetadataVO, waittext, fetchConfigVO);
				tableDropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(3000);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::label[normalize-space(text())=\"param2\"]/preceding::input[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during table dropdown values " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]/preceding::a[1]")));
			WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
					+ "\"]/following::label[normalize-space(text())=\"" + param2 + "\"]/preceding::a[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickTableDropdown(driver, fetchMetadataVO, waittext, fetchConfigVO);
			tableDropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(3000);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[normalize-space(text())=\"param1\"]/following::label[normalize-space(text())=\"param2\"]/preceding::a[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during table dropdown values " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//table[@summary=\"" + param1 + "\"]//input/following-sibling::a[1]")));
			WebElement waittext = driver
					.findElement(By.xpath("//table[@summary=\"" + param1 + "\"]//input/following-sibling::a[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickTableDropdown(driver, fetchMetadataVO, waittext, fetchConfigVO);
			tableDropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//table[@summary=\"param1\"]//input/following-sibling::a[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
		} catch (Exception e) {
			Thread.currentThread().interrupt();
			logger.error("Failed during table dropdown values " + e.getMessage());
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void dropdownValues(WebDriver driver, String param1, String param2, String param3, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {
		try {
			if (!param3.isEmpty()) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"//h1[normalize-space(text())=\"" + param1 + "\"]/following::label[normalize-space(text())=\""
								+ param2 + "\"]/following::a[@title=\"" + param3 + "\"]")));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(
						"//h1[normalize-space(text())=\"" + param1 + "\"]/following::label[text()=\"" + param2 + "\"]"),
						param2));
				WebElement waittext = driver.findElement(By.xpath(
						"//h1[normalize-space(text())=\"" + param1 + "\"]/following::label[normalize-space(text())=\""
								+ param2 + "\"]/following::a[@title=\"" + param3 + "\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//h1[normalize-space(text())=\"param1\"]/following::label[normalize-space(text())=\"param2\"]/following::a[@title=\"param3\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during table dropdown values " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Performance") && param2.equalsIgnoreCase("Performance Documents")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				wait.until(ExpectedConditions.presenceOfElementLocated(By
						.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
								+ param2 + "\"]/following::input[@role=\"combobox\"]")));
				WebElement waittext = driver.findElement(By
						.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
								+ param2 + "\"]/following::input[@role=\"combobox\"]"));
				Thread.sleep(6000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]/following::input[@role=\"combobox\"]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during table dropdown values " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Performance Documents") || (param1.equalsIgnoreCase("Performance Goals")  && (param2.equalsIgnoreCase("Review Period")))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[normalize-space(text())=\"Performance Goals\"]/following::label[normalize-space(text())=\"Review Period\"]/preceding-sibling::input[not(@type='hidden')]/following::a[1]")));
				WebElement waittext = driver.findElement(By.xpath("//h1[normalize-space(text())=\"Performance Goals\"]/following::label[normalize-space(text())=\"Review Period\"]/preceding-sibling::input[not(@type='hidden')]/following::a[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(2000);
				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//h1[normalize-space(text())=\"Performance Goals\"]/following::label[normalize-space(text())=\"Review Period\"]/preceding-sibling::input[not(@type='hidden')]/following::a[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during table dropdown values " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Send Email Notification")
					&& (param2.equalsIgnoreCase("Hire Date Start Range - Days"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())=\""
						+ param1
						+ "\"]/following::*[normalize-space(text())=\"Hire Date Start Range\"]/following::input[@role=\"combobox\"][2]")));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::*[normalize-space(text())=\"Hire Date Start Range\"]/following::input[@role=\"combobox\"][2]"));
				Thread.sleep(6000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				// clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"Hire Date Start Range\"]/following::input[@role=\"combobox\"][2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during table dropdown values " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Other Plans")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())=\""
						+ param1 + "\"]/following::td[normalize-space(text())=\"Review Period *\"]/following::a[3])")));
				WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())=\"" + param1
						+ "\"]/following::td[normalize-space(text())=\"Review Period *\"]/following::a[3])"));
				Thread.sleep(6000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("arguments[0].click();", waittext);
				// clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//*[normalize-space(text())=\"param1\"]/following::td[normalize-space(text())=\"Review Period *\"]/following::a[3])";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during table dropdown values " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Send Email Notification")
					&& (param2.equalsIgnoreCase("Hire Date End Range - Days"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())=\""
						+ param1
						+ "\"]/following::*[normalize-space(text())=\"Hire Date End Range\"]/following::input[@role=\"combobox\"][2]")));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::*[normalize-space(text())=\"Hire Date End Range\"]/following::input[@role=\"combobox\"][2]"));
				Thread.sleep(6000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"Hire Date End Range\"]/following::input[@role=\"combobox\"][2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during table dropdown values " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Goal Name") && (param2.equalsIgnoreCase("Status"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())=\""
						+ param1 + "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::a)[1]")));
				WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())=\"" + param1
						+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::a)[1]"));
				Thread.sleep(6000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]/following::a)[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during table dropdown values " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param2.equalsIgnoreCase("Plan") || param2.equalsIgnoreCase("Option")) {

				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())=\""
						+ param1 + "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::a)[1]")));
				wait.until(
						ExpectedConditions
								.textToBePresentInElementLocated(
										By.xpath("//*[normalize-space(text())=\"" + param1
												+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]"),
										param2));
				WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())=\"" + param1
						+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::a)[1]"));
				Thread.sleep(4000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				Thread.sleep(2000);
				waittext.click();
//			clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(10000);
				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]/following::a)[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during table dropdown values " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Create Performance Documents") && (param2.equalsIgnoreCase("Review Period"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[normalize-space(text())=\""
						+ param1
						+ "\"]/following::*[normalize-space(text())=\"Review Period *\"][1]/following::a[contains(@id,\"HCMPERFORMANCE_FUSE_PERFORMANCE\") and not(@style)][1]")));
				WebElement waittext = driver.findElement(By.xpath("//h1[normalize-space(text())=\"" + param1
						+ "\"]/following::*[normalize-space(text())=\"Review Period *\"][1]/following::a[contains(@id,\"HCMPERFORMANCE_FUSE_PERFORMANCE\") and not(@style)][1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("arguments[0].click();", waittext);
				// clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				WebElement waittext1 = driver
						.findElement(By.xpath("(//div[contains(@id,\"popup-container\")]//*[normalize-space(text())=\""
								+ keysToSend + "\"])[1]"));
				actions.moveToElement(waittext1).build().perform();
				js.executeScript("arguments[0].click();", waittext1);
				// dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO,
				// fetchConfigVO, customerDetails);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//h1[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"Review Period *\"][1]/following::a[contains(@id,\"HCMPERFORMANCE_FUSE_PERFORMANCE\") and not(@style)][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during table dropdown values " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Performance Documents") && (param2.equalsIgnoreCase("Review Period"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[normalize-space(text())=\""
						+ param1
						+ "\"]/following::*[normalize-space(text())=\"Review Period *\"][1]/following::a[contains(@id,\"HCMPERFORMANCE_FUSE_PERFORMANCE\") and not(@style)][1]")));
				WebElement waittext = driver.findElement(By.xpath("//h1[normalize-space(text())=\"" + param1
						+ "\"]/following::*[normalize-space(text())=\"Review Period *\"][1]/following::a[contains(@id,\"HCMPERFORMANCE_FUSE_PERFORMANCE\") and not(@style)][1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(1000);
				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//h1[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"Review Period *\"][1]/following::a[contains(@id,\"HCMPERFORMANCE_FUSE_PERFORMANCE\") and not(@style)][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during table dropdown values " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Participant Feedback") && (param2.equalsIgnoreCase("Review Period"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[normalize-space(text())=\""
						+ param1
						+ "\"]/following::*[normalize-space(text())=\"Review Period *\"][1]/following::a[contains(@id,\"HCMPERFORMANCE_FUSE_PERFORMANCE\") and not(@style)][1]")));
				WebElement waittext = driver.findElement(By.xpath("//h1[normalize-space(text())=\"" + param1
						+ "\"]/following::*[normalize-space(text())=\"Review Period *\"][1]/following::a[contains(@id,\"HCMPERFORMANCE_FUSE_PERFORMANCE\") and not(@style)][1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(1000);
				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//h1[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"Review Period *\"][1]/following::a[contains(@id,\"HCMPERFORMANCE_FUSE_PERFORMANCE\") and not(@style)][1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during table dropdown values " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Checklist") && (param2.equalsIgnoreCase("Status"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[contains(text(),\"" + param1 +"\")]/following::label[text()=\"" + param2 +"\"]/following::a)[1]")));
				WebElement waittext = driver.findElement(By.xpath("(//h1[contains(text(),\"" + param1 +"\")]/following::label[text()=\"" + param2 + "\"]/following::a)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
	//			clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(1000);
				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//h1[contains(text(),\"" + param1 +"\")]/following::label[text()=\"" + param2 +"\"]/following::a)[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
//				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during table dropdown values " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Worker List") && (param2.equalsIgnoreCase("Currency"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(),\"" + param1 +"\")]/following::a[contains(@id,'CurrTypeList')]")));
				WebElement waittext = driver.findElement(By.xpath("//*[contains(text(),\"" + param1 +"\")]/following::a[contains(@id,'CurrTypeList')]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
	//			clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(1000);
				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[contains(text(),\"" + param1 + "\")]/following::a[contains(@id,'CurrTypeList')]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
//				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during table dropdown values " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("When and Why")
					&& (param2.equalsIgnoreCase("Why are you changing the location?"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())=\""
						+ param1 + "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::a)[1]")));
				WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())=\"" + param1
						+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::a)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				String xpath = "(//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())= \"param2\"]/following::a)[1]";
				for (int i = 0; i <= 2; i++) {
					try {
						actions.click(waittext).build().perform();
						break;
					} finally {

					}
				}
				try {
					Thread.sleep(6000);
					WebElement popup1 = driver.findElement(By.xpath("//div[contains(@id,\"popup-container\")]"));
					if (popup1.isDisplayed()) {
						dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO,
								customerDetails);
						actions.release();
						fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
					}
					xpath = xpath + ";" + "//div[contains(@id,\"popup-container\")]";
				} catch (Exception e) {
					for (int i = 0; i <= 2; i++) {
						try {
							actions.click(waittext).build().perform();
							break;
						} finally {

						}
					}
					logger.error("Failed during table dropdown values " + e.getMessage());
					Thread.currentThread().interrupt();
				}
				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				actions.release();
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();

				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during table dropdown values " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())=\""
						+ param1 + "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::a)[1]")));
				WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())=\"" + param1
						+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::a)[1]"));
				Thread.sleep(6000);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
		//		clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				screenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]/following::a)[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
		} catch (Exception e) {
			logger.error("Failed during table dropdown values " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::a)[1]")));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h1[normalize-space(text())=\""
					+ param1 + "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]"), param2));
			WebElement waittext = driver.findElement(By.xpath("(//h1[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::a)[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			Thread.sleep(5000);
			waittext.click();
			// clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(10000);
			dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//h1[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]/following::a)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during table dropdown values " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//label[normalize-space(text())=\""
					+ param1 + "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::a[1]")));
			WebElement waittext = driver.findElement(By.xpath("//label[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::a[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(1000);
			dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//label[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]/following::a[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during table dropdown values " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("(//div[contains(@id,\"popup-container\")]//*[normalize-space(text())=\"" + param1
							+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::a)[1]")));
			WebElement waittext = driver.findElement(
					By.xpath("(//div[contains(@id,\"popup-container\")]//*[normalize-space(text())=\"" + param1
							+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::a)[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			String xpath = "(//div[contains(@id,\"popup-container\")]//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]/following::a)[1]";
			for (int i = 0; i <= 2; i++) {
				try {
					clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
					Thread.sleep(1000);
					break;
				} finally {
					logger.info("Clicked 1st time");
				}
			}
			try {
				Thread.sleep(4000);
				WebElement popup1 = driver.findElement(By.xpath("//div[contains(@id,\"popup-container\")]"));
				if (popup1.isDisplayed()) {
					dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
					fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				}
				xpath = xpath + ";" + "//div[contains(@id,\"popup-container\")]";
			} catch (Exception e) {
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(2000);
				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			}
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during table dropdown values " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Basic Information") || param1.equalsIgnoreCase("Communication")
					|| param1.equalsIgnoreCase("Eligibility")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(
						ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())=\"" + param1
								+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::input[1]")));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::input[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				Thread.sleep(1000);
				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]/following::input[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during table dropdown values " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Selected Eligibility Profiles")
					|| param1.equalsIgnoreCase("Overall Ratings")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"(//*[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
								+ param2 + "\"]/following::input[ not(@type=\"hidden\")])[1]")));
				WebElement waittext = driver.findElement(By.xpath(
						"(//*[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
								+ param2 + "\"]/following::input[ not(@type=\"hidden\")])[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(1000);
				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]/following::input[ not(@type=\"hidden\")])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during table dropdown values " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Details")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())=\""
						+ param1 + "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::a[1]")));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + param1
						+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::a[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(1000);
				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]/following::a[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during table dropdown values " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
							+ param2 + "\"]/following::input[@placeholder=\"Select a value\"]")));
			WebElement waittext = driver.findElement(
					By.xpath("//*[normalize-space(text())=\"" + param1 + "\"]/following::*[normalize-space(text())=\""
							+ param2 + "\"]/following::input[@placeholder=\"Select a value\"]"));
			clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]/following::input[@placeholder=\"Select a value\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during table dropdown values " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::a)[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())=\"" + param1
					+ "\"]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::a)[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			String xpath = "(//*[normalize-space(text())=\"param1\"]/following::*[normalize-space(text())=\"param2\"]/following::a)[1]";
			for (int i = 0; i <= 2; i++) {
				try {
					clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
					Thread.sleep(1000);
					break;
				} finally {
					logger.info("Clicked 1st time");
				}
			}
			try {
				Thread.sleep(4000);
				WebElement popup1 = driver.findElement(By.xpath("//div[contains(@id,\"popup-container\")]"));
				if (popup1.isDisplayed()) {
					dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
					fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				}
				xpath = xpath + ";" + "//div[contains(@id,\"popup-container\")]";
			} catch (Exception e) {
				enter(driver, fetchMetadataVO, fetchConfigVO, customerDetails);
				Thread.sleep(2000);
				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
				fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			}
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during table dropdown values " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[contains(text(),\"" + param1
					+ "\")]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::a)[1]")));
			wait.until(
					ExpectedConditions
							.textToBePresentInElementLocated(
									By.xpath("//h1[contains(text(),\"" + param1
											+ "\")]/following::*[normalize-space(text())=\"" + param2 + "\"]"),
									param2));
			WebElement waittext = driver.findElement(By.xpath("(//h1[contains(text(),\"" + param1
					+ "\")]/following::*[normalize-space(text())=\"" + param2 + "\"]/following::a)[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			waittext.click();
	//		clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
			takeScreenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//h1[contains(text(),\"param1\")]/following::*[normalize-space(text())=\"param2\"]/following::a)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during table dropdown values " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("(//label[contains(text(),\"" + param1 + "\")]/following::a)[1]")));
			// wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h1[contains(text(),\""+param1+"\")]/following::*[normalize-space(text())=\""+param2+"\"]"),
			// param2));
			WebElement waittext = driver
					.findElement(By.xpath("(//label[contains(text(),\"" + param1 + "\")]/following::a)[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO, customerDetails);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//label[contains(text(),\"param1\")]/following::a)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
//			service.saveXpathParams(scriptID, lineNumber, xpath);
		} catch (Exception e) {
			Thread.currentThread().interrupt();
			logger.error("Failed during table dropdown values " + e.getMessage());
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	private void clickDropdownXpath(WebDriver driver, ScriptDetailsDto fetchMetadataVO, WebElement waittext,
			FetchConfigVO fetchConfigVO) {
		try {
			// WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			// driver.manage().timeouts().setScriptTimeout(fetchConfigVO.getWait_time(),
			// TimeUnit.SECONDS);
			 JavascriptExecutor js = (JavascriptExecutor)driver;
			 js.executeScript("arguments[0].click();", waittext);
	//		waittext.click();
		} catch (Exception e) {
			logger.error("Failed during click Dropdown Xpath " + fetchMetadataVO.getScriptNumber());
			e.printStackTrace();
		}
	}

	private void clickTableDropdown(WebDriver driver, ScriptDetailsDto fetchMetadataVO, WebElement waittext,
			FetchConfigVO fetchConfigVO) {
		try {
			waittext.click();
		} catch (Exception e) {
			logger.error("Failed during click Table Dropdown Xpath " + fetchMetadataVO.getScriptNumber());
			e.printStackTrace();
		}
	}

	public void highlightElement(WebDriver driver, ScriptDetailsDto fetchMetadataVO, WebElement waittext,
			FetchConfigVO fetchConfigVO) {
		try {
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("arguments[0].style.border=\"6px solid yellow\"", waittext);
		} catch (Exception e) {
			logger.error("Failed during highlight Element " + fetchMetadataVO.getScriptNumber());
			e.printStackTrace();
		}
	}

	private void clickValidateXpath(WebDriver driver, ScriptDetailsDto fetchMetadataVO, WebElement waittext,
			FetchConfigVO fetchConfigVO) {
		try {
			// WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			// driver.manage().timeouts().setScriptTimeout(fetchConfigVO.getWait_time(),
			// TimeUnit.SECONDS);
	//		 JavascriptExecutor js = (JavascriptExecutor)driver;
	//		 js.executeScript("arguments[0].click();", waittext);
			waittext.click();
		} catch (Exception e) {
			logger.error("Failed during click validate xpath " + fetchMetadataVO.getScriptNumber());
			e.printStackTrace();
		}
	}

	public void clickFilter(WebDriver driver, String xpath1, String xpath2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws InterruptedException {
		try {
			WebElement waittill = driver.findElement(By.xpath(xpath1));
			WebElement waittill1 = driver.findElement(By.xpath(xpath2));
			if (waittill1.isDisplayed()) {
				Duration timeoutDuration = Duration.ofSeconds(10);
				WebDriverWait wait = new WebDriverWait(driver, timeoutDuration);
				WebElement element = wait.until(ExpectedConditions.elementToBeClickable(waittill));
				element.click();
				logger.info("Clicked Expand Succesfully.");
			} else {
				waittill.click();
				logger.info("Clicked Expand Succesfully.");
			}
		} catch (StaleElementReferenceException e) {
			logger.error("Falied During Click Expand Action.");
			WebElement waittill = driver.findElement(By.xpath(xpath1));
			Duration timeoutDuration = Duration.ofSeconds(60);
			WebDriverWait wait = new WebDriverWait(driver, timeoutDuration);
//			WebDriverWait wait = new WebDriverWait(driver, 60);
			wait.until(ExpectedConditions.elementToBeClickable(waittill));
			waittill.click();
		} catch (Exception e) {
			logger.error("Falied During ClickExpand Action.");
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public String password(WebDriver driver, String inputParam, String keysToSend, FetchConfigVO fetchConfigVO,
			ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails) {
		try {
			WebElement waittill = driver.findElement(By.xpath("//*[contains(@placeholder,\"" + inputParam + "\")]"));
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			String xpath = "//*[contains(@placeholder,\"inputParam\")]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return keysToSend;
		} catch (Exception e) {
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			logger.error("Failed during enter password " + e.getMessage());
			Thread.currentThread().interrupt();
			throw e;
		}
	}

	public void typeIntoValidxpath(WebDriver driver, String keysToSend, WebElement waittill,
			FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO) {
		try {
			driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(10));
			waittill.clear();
			waittill.sendKeys(keysToSend);
			logger.info("clear and typed the given Data");

		} catch (Exception e) {
			logger.error("Failed during type into valid xpath " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void clearMethod(WebDriver driver, WebElement waittill) {
		Duration timeoutDuration = Duration.ofSeconds(60);
		WebDriverWait wait = new WebDriverWait(driver, timeoutDuration);
		wait.until(ExpectedConditions.elementToBeClickable(waittill));
		waittill.clear();
		logger.info("clear and typed the given Data");
	}

	public void scrollUsingElement(WebDriver driver, String inputParam, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {
		try {
			if (inputParam.equalsIgnoreCase("Work Email")) {
				Thread.sleep(3000);
				WebElement waittill = driver.findElement(By.xpath("(//b[text()=\"" + inputParam + "\"])[1]"));
				scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
				logger.info("ScrollUsingElement Successfully Done!");
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//b[text()=\"inputParam\"])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during Scroll using element " + e.getMessage());
		}
		try {
			Thread.sleep(2000);
			WebElement waittill = driver
					.findElement(By.xpath("//span[normalize-space(text())=\"" + inputParam + "\"][1]"));
			// ((JavascriptExecutor)driver).executeScript("document.body.style.zoom=\"50%\";");
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			// ((JavascriptExecutor)driver).executeScript("document.body.style.zoom=\"100%\";");
			logger.info("ScrollUsingElement Successfully Done!");
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//span[normalize-space(text())=\"inputParam\"][1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during Scroll using element " + e.getMessage());
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("//a[normalize-space(text())=\"" + inputParam + "\"]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			logger.info("ScrollUsingElement Successfully Done!");
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//a[normalize-space(text())=\"inputParam\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during Scroll using element " + e.getMessage());
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("//h1[normalize-space(text())=\"" + inputParam + "\"]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			logger.info("ScrollUsingElement Successfully Done!");
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//h1[normalize-space(text())=\"inputParam\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during Scroll using element " + e.getMessage());
		}
		try {
			WebElement waittill = driver
					.findElement(By.xpath("(//h2[normalize-space(text())=\"" + inputParam + "\"])"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			logger.info("ScrollUsingElement Successfully Done!");
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//h2[normalize-space(text())=\"inputParam\"])";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during Scroll using element " + e.getMessage());
		}
		try {
			WebElement waittill = driver
					.findElement(By.xpath("(//h3[normalize-space(text())=\"" + inputParam + "\"])[2]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			logger.info("ScrollUsingElement Successfully Done!");
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//h3[normalize-space(text())=\"inputParam\"])[2]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during Scroll using element " + e.getMessage());
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("//td[normalize-space(text())=\"" + inputParam + "\"]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			logger.info("ScrollUsingElement Successfully Done!");
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//td[normalize-space(text())=\"inputParam\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during Scroll using element " + e.getMessage());
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("//div[contains(text(),\"" + inputParam + "\")]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			logger.info("ScrollUsingElement Successfully Done!");
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//div[contains(text(),\"inputParam\")]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during Scroll using element " + e.getMessage());
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("(//table[@summary=\"" + inputParam + "\"]//td//a)[1]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			logger.info("ScrollUsingElement Successfully Done!");
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//table[@summary=\" inputParam\"]//td//a)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during Scroll using element " + e.getMessage());
		}
		try {
			WebElement waittill = driver.findElement(
					By.xpath("(//label[normalize-space(text())=\"" + inputParam + "\"]/following::input)[1]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			logger.info("ScrollUsingElement Successfully Done!");
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//label[normalize-space(text())=\"inputParam\"]/following::input)[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during Scroll using element " + e.getMessage());
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("//a[contains(@id,\"" + inputParam + "\")]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			logger.info("ScrollUsingElement Successfully Done!");
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//a[contains(@id,\"inputParam\")]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during Scroll using element " + e.getMessage());
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("//li[normalize-space(text())=\"" + inputParam + "\"]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			logger.info("ScrollUsingElement Successfully Done!");
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//li[normalize-space(text())=\"inputParam\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during Scroll using element " + e.getMessage());
		}
		try {
			WebElement waittill = driver
					.findElement(By.xpath("//label[normalize-space(text())=\"" + inputParam + "\"]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			logger.info("ScrollUsingElement Successfully Done!");
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//label[normalize-space(text())=\"inputParam\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during Scroll using element " + e.getMessage());
		}
		try {
			WebElement waittill = driver
					.findElement(By.xpath("//button[normalize-space(text())=\"" + inputParam + "\"]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			logger.info("ScrollUsingElement Successfully Done!");
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//button[normalize-space(text())=\" inputParam \"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during Scroll using element " + e.getMessage());
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("//img[@title=\"" + inputParam + "\"]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			logger.info("ScrollUsingElement Successfully Done!");
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//img[@title=\"inputParam\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during Scroll using element " + e.getMessage());
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("//*[normalize-space(text())=\"" + inputParam + "\"]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			logger.info("ScrollUsingElement Successfully Done!");
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[normalize-space(text())=\"inputParam\"]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during Scroll using element " + e.getMessage());
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("(//*[@title=\"" + inputParam + "\"])[1]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO, customerDetails);
			logger.info("ScrollUsingElement Successfully Done!");
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//*[@title=\" inputParam \"])[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
		} catch (Exception e) {
			logger.error("Failed during Scroll using element " + e.getMessage());
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
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
		fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
	}

	public void tab(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) throws Exception {
		try {
			Actions action = new Actions(driver);
			action.sendKeys(Keys.TAB).build().perform();
			logger.info("Successfully Clicked the tab.");
		} catch (Exception e) {
			logger.error("Failed During clicking the tab");
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			e.printStackTrace();
			throw e;
		}
	}

	public void mousehover(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			Actions actions = new Actions(driver);
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("(//table[@summary=\"" + param1 + "\"]//tr[1]/following::a)[2]")));
			scrollUsingElement(driver, param1, fetchMetadataVO, fetchConfigVO, customerDetails);
			Thread.sleep(6000);
			WebElement waittext = driver
					.findElement(By.xpath("(//table[@summary=\"" + param1 + "\"]//tr[1]/following::a)[2]"));
			actions.moveToElement(waittext).build().perform();
			clickImage(driver, param2, param1, fetchMetadataVO, fetchConfigVO, customerDetails);
			fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "(//table[@summary=\"param1\"]//tr[1]/following::a)[2]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during mouse hover " + e.getMessage());
			Thread.currentThread().interrupt();
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
		} catch (Exception e) {
			logger.error("Failed during mouse hover " + e.getMessage());
			Thread.currentThread().interrupt();
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void enter(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) throws Exception {
		try {
			Thread.sleep(4000);
			Actions actionObject = new Actions(driver);
			actionObject.sendKeys(Keys.ENTER).build().perform();
			Thread.sleep(3000);
		} catch (Exception e) {
			logger.error("Failed during enter action " + e.getMessage());
			Thread.currentThread().interrupt();
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
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
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
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
			logger.info("selected Checkbox Successfully");
		} catch (Exception e) {
			logger.error("Failed While Selecting Checkbox.");
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			e.printStackTrace();
			throw e;
		}
	}

	public void selectByText(WebDriver driver, String inputParam, String param2, String inputData,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {
		try {
			WebElement waittext = driver
					.findElement(By.xpath(("//*[contains(text(),\"" + inputParam + "\")]/following::select[1]")));
			selectMethod(driver, inputData, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[contains(text(),\"inputParam\")]/following::select[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during selectByText " + e.getMessage());
		}
		try {
			WebElement waittext = driver.findElement(
					By.xpath(("//*[contains(text(),\"" + inputParam + "\")]/preceding-sibling::select[1]")));
			selectMethod(driver, inputData, fetchMetadataVO, waittext, fetchConfigVO, customerDetails);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//*[contains(text(),\"inputParam\")]/preceding-sibling::select[1]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
		} catch (Exception e) {
			logger.error("Failed during selectByText " + e.getMessage());
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	private void selectMethod(WebDriver driver, String inputData, ScriptDetailsDto fetchMetadataVO, WebElement waittext,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {
		Select selectBox = new Select(waittext);
		selectBox.selectByVisibleText(inputData);
		logger.info("selectedBYText Successfully");
		fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
	}

	public void selectByValue(WebDriver driver, String xpath, String inputData, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {
		try {
			WebElement webElement = driver.findElement(By.xpath(xpath));
			Select selectBox = new Select(webElement);
			selectBox.selectByValue(inputData);
			logger.info("selectedBYValue Successfully");
		} catch (Exception e) {
			logger.error("Failed During selectByValue Action.");
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			logger.info(xpath);
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

	public String copynumber(WebDriver driver, String inputParam, String inputParam2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {
		try {
			WebElement webElement = driver.findElement(By.xpath("(//div[contains(text(),\"" + inputParam + "\")])[1]"));
			if (webElement.isDisplayed() == true) {
				copyMethod(webElement);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//div[contains(text(),\"inputParam\")])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return inputParam;
			}
		} catch (Exception e) {
			logger.error("Failed during copy number  " + e.getMessage());
		}
		try {
			WebElement webElement = driver.findElement(By.xpath("//img[@title=\"In Balance \"]/following::td[1]"));
			if (webElement.isDisplayed() == true) {
				copyMethod(webElement);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "//img[@title=\"In Balance \"]/following::td[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return inputParam;
			}
		} catch (Exception e) {
			logger.error("Failed During copynumber Action " + e.getMessage());
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
		return inputParam;
	}

	private void copyMethod(WebElement webElement) {
		String number = webElement.getText();
		String num = number.replaceAll("[^\\d.]+|\\.(?!\\d)", "");
		StringSelection stringSelection = new StringSelection(num);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
		logger.info("Successfully Copied the Number");
	}

	public String copyy(WebDriver driver, String xpath, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) {
		try {
			WebElement webElement = driver.findElement(By.xpath(xpath));
			String number = webElement.getText();
			String num = number.replaceAll("[^\\d.]+|\\.(?!\\d)", "");
			StringSelection stringSelection = new StringSelection(num);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
			logger.info("Successfully Copied");
		} catch (Exception e) {
			logger.error("Failed During copyy Action.");
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
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
			logger.info("Successfully Copied");
		} catch (Exception e) {
			logger.error("Failed During copytext Action.");
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
		return xpath;

	}

	public void maximize(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) {
		try {
			driver.manage().window().maximize();
			logger.info("Successfully Maximized");
		} catch (Exception e) {
			logger.error("Failed During Maximize Action.");
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			e.printStackTrace();
			throw e;

		}
	}

	public void switchWindow(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) {
		try {
			driver.switchTo().window("");
			logger.info("Successfully Switched to Another Window");
		} catch (Exception e) {
			logger.error("Failed During Switching to Window");
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			e.printStackTrace();

			throw e;
		}
	}

	public void switchDefaultContent(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) {
		try {
			driver.switchTo().defaultContent();
			logger.info("Successfully Switched to Default Content");
		} catch (Exception e) {
			logger.error("Failed During switching to Default Action.");
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void dragAnddrop(WebDriver driver, String xpath, String xpath1, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {
		try {
			WebElement dragElement = driver.findElement(By.xpath(xpath));
			WebElement dropElement = driver.findElement(By.xpath(xpath1));
			Actions action = new Actions(driver);
			// Action dragDrop = action.dragAndDrop(fromElement, webElement).build();
			action.dragAndDrop(dragElement, dropElement).build().perform();
			logger.info("Successfully Drag and drop the values");
		} catch (Exception e) {
			logger.error("Failed During dragAnddrop Action.");
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			e.printStackTrace();
			throw e;
		}
	}

	public void windowhandle(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) throws Exception {
		try {
			String mainWindow = driver.getWindowHandle();
			Set<String> set = driver.getWindowHandles();
			Iterator<String> itr = set.iterator();
			while (itr.hasNext()) {
				String childWindow = itr.next();
				if (!mainWindow.equals(childWindow)) {
					driver.switchTo().window(childWindow);
					driver.manage().window().maximize();
					Thread.sleep(2000);
					fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
					driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
					driver.switchTo().window(childWindow);
				}
			}
			logger.info("Successfully Handeled the window");
		} catch (Exception e) {
			logger.error("Failed to Handle the window");
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			e.printStackTrace();
			throw e;
		}
	}

	public void switchToFrame(WebDriver driver, String param1, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws InterruptedException {
		Thread.sleep(5000);
		try {
			WebElement waittext = driver.findElement(By.xpath("//iframe[contains(@id,\"" + param1 + "\")]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			driver.switchTo().frame(waittext);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//iframe[contains(@id,\"param1\")]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
			return;
		} catch (Exception e) {
			logger.error("Failed during Switch to Frame " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase("Overall Comments/Manager Rating")
					|| param1.equalsIgnoreCase("Manager Comments") || param1.equalsIgnoreCase("Comments")) {
				Thread.sleep(5000);
				WebElement waittext = driver
						.findElement(By.xpath("(//iframe[@class=\"cke_wysiwyg_frame cke_reset\"])[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				driver.switchTo().frame(waittext);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//iframe[@class=\"cke_wysiwyg_frame cke_reset\"])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during Switch to Frame " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if ((param1.equalsIgnoreCase("Goal 1,2,3")
					|| (param1.equalsIgnoreCase("1. What was your overall impact on Arlo success"))
					|| (param1.equalsIgnoreCase("1. Describe your overall goal achievement for the year"))
					|| (param1.equalsIgnoreCase("Success Criteria")))) {
				WebElement waittext = driver
						.findElement(By.xpath("(//iframe[@class=\"cke_wysiwyg_frame cke_reset\"])[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				driver.switchTo().frame(waittext);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//iframe[@class=\"cke_wysiwyg_frame cke_reset\"])[1]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during Switch to Frame " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if ((param1.equalsIgnoreCase("Goal 4 5 6") || (param1.equalsIgnoreCase(
					"2. What are you going to stop, start and continue in order to have an impact on Arlo success"))
					|| (param1.equalsIgnoreCase("2. What were your additional accomplishments in the year"))
					|| (param1.equalsIgnoreCase("Comments")) || (param1.equalsIgnoreCase("Employee Comments")))) {
				WebElement waittext = driver
						.findElement(By.xpath("(//iframe[@class=\"cke_wysiwyg_frame cke_reset\"])[2]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				driver.switchTo().frame(waittext);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//iframe[@class=\"cke_wysiwyg_frame cke_reset\"])[2]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during Switch to Frame " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase(
					"3. Describe two things you do well in line with the Arlo Values: Results, Customer Focus, Integrity")) {
				WebElement waittext = driver
						.findElement(By.xpath("(//iframe[@class=\"cke_wysiwyg_frame cke_reset\"])[3]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				driver.switchTo().frame(waittext);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//iframe[@class=\"cke_wysiwyg_frame cke_reset\"])[3]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during Switch to Frame " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			if (param1.equalsIgnoreCase(
					"4. What are two things you could do more of in line with the Arlo Values: Results, Customer Focus")) {
				WebElement waittext = driver
						.findElement(By.xpath("(//iframe[@class=\"cke_wysiwyg_frame cke_reset\"])[4]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				driver.switchTo().frame(waittext);
				String scripNumber = fetchMetadataVO.getScriptNumber();
				String xpath = "(//iframe[@class=\"cke_wysiwyg_frame cke_reset\"])[4]";
				String scriptID = fetchMetadataVO.getScriptId();
				String lineNumber = fetchMetadataVO.getLineNumber();
				service.saveXpathParams(scriptID, lineNumber, xpath);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed during Switch to Frame " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		try {
			WebElement waittext = driver.findElement(By.xpath("//iframe[contains(@title,\"" + param1 + "\")]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			driver.switchTo().frame(waittext);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			String xpath = "//iframe[contains(@title,\"param1\")]";
			String scriptID = fetchMetadataVO.getScriptId();
			String lineNumber = fetchMetadataVO.getLineNumber();
			service.saveXpathParams(scriptID, lineNumber, xpath);
		} catch (Exception e) {
			Thread.currentThread().interrupt();
			logger.error("Failed during Switch to Frame " + e.getMessage());
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
	}

	public void uploadFileAutoIT(WebDriver webDriver, String fileLocation, String param1, String param2, String param3, ScriptDetailsDto scriptDetailsDto, CustomerProjectDto customerProjectDto) throws Exception {
//		try {
//			// String autoitscriptpath = System.getProperty("user.dir") + File.separator +
//			// "File_upload_selenium_webdriver.au3";
////			Runtime.getRuntime().exec("cmd.exe /c Start AutoIt3.exe " + autoitscriptpath + " \"" + fetchConfigVO.getUpload_file_path()+File.separator+filelocation + "\"");
//			Runtime.getRuntime()
//					.exec("C:\\Selenium\\Spring\\WinfoAutomation_MultiThread\\File_upload_selenium_webdriver.exe");
//			// Runtime.getRuntime().exec("E:\\AutoIT\\FileUpload.exe");
//			Thread.sleep(5000);
//			logger.info("Successfully Uploaded The File");
//		} catch (Exception e) {
//			logger.error("Failed During uploadFileAutoIT Action.");
//			logger.info(filelocation);
//			e.printStackTrace();
//			throw e;
//		}
		try {
			if(param1.equalsIgnoreCase("file")) {
				String uploadXPath = "//*[@type='"+param1+"']";
				WebElement uploadZip = webDriver.findElement(By.xpath(uploadXPath));
				Thread.sleep(5000);
				File file = new File(fileLocation+param3);
				logger.info(" File Location " + file);
				uploadZip.sendKeys(file.getAbsolutePath());
				logger.info("Successfully Uploaded The File");
				screenshot(webDriver, scriptDetailsDto, customerProjectDto);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed During uploadFileAutoIT Action " + fileLocation);
			screenshotFail(webDriver, scriptDetailsDto, customerProjectDto);
			e.printStackTrace();
		}
		try {
			if(param1.equalsIgnoreCase("Add") && param2.equalsIgnoreCase("File")) {
			String uploadXPath = "//*[text()='"+param1+" "+param2+"']";
			WebElement uploadZip = webDriver.findElement(By.xpath(uploadXPath));
			Thread.sleep(5000);
			File file = new File(fileLocation+param3);
			logger.info(" File Location " + file);
			uploadZip.sendKeys(file.getAbsolutePath());
			logger.info("Successfully Uploaded The File");
			screenshot(webDriver, scriptDetailsDto, customerProjectDto);
			return;
			}
		} catch (Exception e) {
			logger.error("Failed During uploadFileAutoIT Action " +fileLocation);
			screenshotFail(webDriver, scriptDetailsDto, customerProjectDto);
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
			logger.error("Failed During uploadFileAutoIT Action " + fileLocation);
			e.printStackTrace();
		}
	}

	public void refreshPage(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) {
		try {
			driver.navigate().refresh();
			logger.info("Successfully refreshed the Page");
		} catch (Exception e) {
			logger.error("Failed During refreshPage Action");
			fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			e.printStackTrace();
			throw e;

		}
	}

	public void delatedScreenshoots(List<ScriptDetailsDto> fetchMetadataListVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) throws IOException {
		File folder = new File(fetchConfigVO.getSCREENSHOT_PATH() + customerDetails.getCustomerName() + "/"
				+ customerDetails.getTestSetName() + "/");
		if (folder.exists()) {
			File[] listOfFiles = folder.listFiles();

//		String image=fetchConfigVO.getScreenshot_path() + fetchMetadataVO.getCustomer_name() + "/"
//				+ fetchMetadataVO.getTest_run_name() + "/" + fetchMetadataVO.getSeq_num() + "_"
//				+ fetchMetadataVO.getLineNumber() + "_" + fetchMetadataVO.getScenario_name() + "_"
//				+ fetchMetadataVO.getScriptNumber() + "_" + fetchMetadataVO.getTest_run_name() + "_"
//				+ fetchMetadataVO.getLineNumber();
			for (File file : Arrays.asList(listOfFiles)) {

				String seqNum = String.valueOf(file.getName().substring(0, file.getName().indexOf('_')));

				String seqnum1 = fetchMetadataListVO.get(0).getSeqNum();
				if (seqNum.equalsIgnoreCase(seqnum1)) {
					Path imagesPath = Paths.get(file.getPath());
					Files.delete(imagesPath);
				}
			}
		}
	}

	public void loginSFApplication(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			String type1, String type2, String type3, String param1, String param2, String param3, String keysToSend,
			String value, CustomerProjectDto customerDetails) throws Exception {

	}

	@Override
	public String getErrorMessages(WebDriver driver) throws Exception {
		return null;
	}

	@Override
	public void moveToElement(WebDriver driver, String inputParameter, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO) throws Exception {
		// EMPTY METHOD

	}

	@Override
	public String closeExcel() throws Exception {
		return null;
	}

	@Override
	public String typeIntoCell(WebDriver driver, String param1, String value1, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, Integer addrowCounter) throws Exception {
		return null;
	}

	@Override
	public Integer addRow(Integer addrow) throws Exception {
		return null;
	}

	@Override
	public String menuItemOfExcel(WebDriver driver, String param1, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO) throws Exception {
		return null;
	}

	@Override
	public String loginToExcel(WebDriver driver, String param1, String param2, String username, String password,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
		return null;
	}

	@Override
	public void waitTillLoad(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO) {
		try {
			Thread.sleep(Integer.parseInt(fetchMetadataVO.getInputValue()));
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.error("Failed during waitTillLoad " + e.getMessage());
			Thread.currentThread().interrupt();
		}

	}

	@Override
	public String uploadObjectToObjectStore(String sourceFilePath, String destinationFilePath) {
		return null;
	}

	@Override
	public void compareValue(WebDriver driver, String inputParameter, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, String globalValueForSteps2, CustomerProjectDto customerDetails)
			throws Exception {
		// EMPTY METHOD

	}

	@Override
	public void apiAccessToken(ScriptDetailsDto fetchMetadataVO, Map<String, String> accessTokenStorage,
			CustomerProjectDto customerDetails) throws Exception {
		// EMPTY METHOD

	}

	@Override
	public void multiplelinestableSendKeys(WebDriver driver, String param1, String param2, String param3,
			String inputValue, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) throws Exception {
		// EMPTY METHOD

	}

	@Override
	public void switchToParentWindow(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) throws Exception {
		// EMPTY METHOD

	}

	@Override
	public void datePicker(WebDriver driver, String param1, String param2, String inputValue,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {
		// EMPTY METHOD

	}

	@Override
	public void switchParentWindow(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) throws Exception {
		// EMPTY METHOD

	}

	@Override
	public void clickButtonCheckPopup(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		// EMPTY METHOD

	}

	@Override
	public void oicLogout(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO, String type1,
			String type2, String type3, String param1, String param2, String param3, CustomerProjectDto customerDetails)
			throws Exception {
		// EMPTY METHOD

	}

	@Override
	public String oicLoginPage(WebDriver driver, String param1, String keysToSend, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {
		return null;
	}

	@Override
	public void oicNavigate(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			String type1, String type2, String param1, String param2, int count, CustomerProjectDto customerDetails)
			throws Exception {
		// EMPTY METHOD

	}

	@Override
	public String oicNavigator(WebDriver driver, String param1, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		return null;
	}

	@Override
	public String oicMenuNavigation(WebDriver driver, String param1, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		return null;
	}

	@Override
	public String oicMenuNavigationButton(WebDriver driver, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, String type1, String type2, String param1, String param2, int count,
			CustomerProjectDto customerDetails) throws Exception {
		return null;
	}

	@Override
	public void oicClickButton(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		// EMPTY METHOD

	}

	@Override
	public String oicSendValue(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {
		return null;
	}

	@Override
	public void oicMouseHover(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		// EMPTY METHOD

	}

	@Override
	public List<String> openExcelFileWithSheet(WebDriver driver, String param1, String param2, String fileName,
			String sheetName, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) throws Exception {
		return null;
	}

	@Override
	public void navigateOICUrl(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			CustomerProjectDto customerDetails) throws Exception {
		// EMPTY METHOD

	}

	@Override
	public void loginOicApplication(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			String type1, String type2, String type3, String param1, String param2, String param3, String keysToSend,
			String value, CustomerProjectDto customerDetails) throws Exception {
		// EMPTY METHOD

	}

	@Override
	public void loginInformaticaApplication(WebDriver driver, FetchConfigVO fetchConfigVO,
			ScriptDetailsDto fetchMetadataVO, String type1, String type2, String type3, String param1, String param2,
			String param3, String keysToSend, String value, CustomerProjectDto customerDetails) throws Exception {
		// EMPTY METHOD

	}

	@Override
	public void navigateInformaticaUrl(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			CustomerProjectDto customerDetails) {
		// EMPTY METHOD

	}

	@Override
	public String InformaticaLoginPage(WebDriver driver, String param1, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {
		return null;
	}

	@Override
	public void InformaticaClickButton(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		// EMPTY METHOD

	}

	@Override
	public String InformaticaSendValue(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {
		return null;
	}

	@Override
	public void InformaticaclickLink(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		// EMPTY METHOD

	}

	@Override
	public void InformaticaSelectAValue(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception {
		// EMPTY METHOD

	}

	@Override
	public void InformaticaClickImage(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		// EMPTY METHOD

	}

	@Override
	public void InformaticaLogout(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			String type1, String type2, String type3, String param1, String param2, String param3,
			CustomerProjectDto customerDetails) throws Exception {
		// EMPTY METHOD

	}

	@Override
	public void oicClickMenu(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		// EMPTY METHOD

	}

	@Override
	public void loginOicJob(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			String type1, String type2, String type3, String param1, String param2, String param3, String keysToSend,
			String value, CustomerProjectDto customerDetails) throws Exception {
		// EMPTY METHOD

	}

	@Override
	public void loginSSOApplication(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			String type1, String type2, String type3, String param1, String param2, String param3, String inputValue,
			String password, CustomerProjectDto customerDetails) throws Exception {
		// EMPTY METHOD

	}

	@Override
	public void createDriverFailedPdf(List<ScriptDetailsDto> fetchMetadataListVO, FetchConfigVO fetchConfigVO,
			String pdffileName, ApiValidationVO api, boolean validationFlag, CustomerProjectDto customerDetails)
			throws IOException, DocumentException, com.lowagie.text.DocumentException {
		// EMPTY METHOD

	}

	@Override
	public void switchToParentWindowWithoutPdf(WebDriver driver, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
