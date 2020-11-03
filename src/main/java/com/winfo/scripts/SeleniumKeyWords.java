package com.winfo.scripts;


//import static org.bytedeco.javacpp.opencv_imgcodecs.*;  
import java.io.File;  
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;  
import java.util.Scanner;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.block.LineBorder;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.VerticalAlignment;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;

import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

//import org.bytedeco.javacpp.avcodec;
//import org.bytedeco.javacv.FFmpegFrameRecorder;  
//import org.bytedeco.javacv.OpenCVFrameConverter;
//import org.bytedeco.javacv.OpenCVFrameConverter.ToIplImage;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.block.LineBorder;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.VerticalAlignment;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.FileImageOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//import org.bytedeco.javacv.FFmpegFrameRecorder;
//import org.bytedeco.javacv.OpenCVFrameConverter;
import org.openqa.selenium.By;
//import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
//import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Font.FontFamily;

import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.winfo.services.FetchConfigVO;
import com.winfo.services.FetchMetadataVO;
import com.winfo.utils.DateUtils;
import com.winfo.utils.StringUtils;

@Service
public class SeleniumKeyWords {

	public static Logger logger = LogManager.getLogger(SeleniumKeyWords.class);
	/*
	 * private Integer ElementWait = Integer
	 * .valueOf(PropertyReader.getPropertyValue(PropertyConstants.EXECUTION_TIME.
	 * value)); public int WaitElementSeconds = new Integer(ElementWait);
	 */
	public String Main_Window = "";
	public WebElement fromElement;
	public WebElement toElement;

	public void loginApplication(WebDriver driver, FetchConfigVO fetchConfigVO, FetchMetadataVO fetchMetadataVO,
			String type1, String type2, String type3, String param1, String param2, String param3, String keysToSend,
			String value) throws Exception {
		String param5 = "password";
		String param6 = "Sign In";
		navigateUrl(driver, fetchConfigVO, fetchMetadataVO);
		loginPage(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO);
		loginPage(driver, param5, value, fetchMetadataVO, fetchConfigVO);
//		sendValue(driver, param1, param3, keysToSend, fetchMetadataVO, fetchConfigVO);
//		sendValue(driver, param5, param2, value, fetchMetadataVO, fetchConfigVO);
//		clickSignInSignOut(driver, param6, fetchMetadataVO, fetchConfigVO);
//		clickButton(driver, param6, param2, fetchMetadataVO, fetchConfigVO);
	}

	public void navigate(WebDriver driver, FetchConfigVO fetchConfigVO, FetchMetadataVO fetchMetadataVO, String type1,
			String type2, String param1, String param2) throws Exception {
		String param3 = "Navigator";
		navigator(driver, param3, fetchMetadataVO, fetchConfigVO);
		menuNavigation(driver, param1, fetchMetadataVO, fetchConfigVO);
		menuNavigationButton(driver, param2, fetchMetadataVO, fetchConfigVO);
//		clickLink(driver, param3, param2, fetchMetadataVO, fetchConfigVO);
//		clickMenu(driver, param1, param2, fetchMetadataVO, fetchConfigVO);
//		clickButton(driver, param2, param2, fetchMetadataVO, fetchConfigVO);
	}

	public void openTask(WebDriver driver, FetchConfigVO fetchConfigVO, FetchMetadataVO fetchMetadataVO, String type1,
			String type2, String param1, String param2) throws Exception {
		String param3 = "Tasks";
//		clickImage(driver, param3, param2, fetchMetadataVO, fetchConfigVO);
//		clickLink(driver, param1, param2, fetchMetadataVO, fetchConfigVO);
		task(driver, param3, fetchMetadataVO, fetchConfigVO);
		taskMenu(driver, param1, fetchMetadataVO, fetchConfigVO);
	}

	public void logout(WebDriver driver, FetchConfigVO fetchConfigVO, FetchMetadataVO fetchMetadataVO, String type1,
			String type2, String type3, String param1, String param2, String param3) throws Exception {

		String param4 = "UIScmil1u";
		String param5 = "Sign Out";
		String param6 = " Confirm";
		logoutDropdown(driver, fetchConfigVO, fetchMetadataVO, param1);
		clickSignInSignOut(driver, param6, fetchMetadataVO, fetchConfigVO);
	}

	public void logoutDropdown(WebDriver driver, FetchConfigVO fetchConfigVO, FetchMetadataVO fetchMetadataVO,
			String param1) throws Exception {

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
			} catch (Exception e) {
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(4000);
				WebElement signout = driver
						.findElement(By.xpath("//div[contains(@id,'popup-container')]//a[text()='Sign Out']"));
				signout.click();
				Thread.sleep(4000);
			}
			return;
		} catch (Exception e) {
			System.out.println(e);
			logger.error("Failed during Click action.");
			screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, fetchConfigVO);
			throw e;
		}
	}

	public void openSettings(WebDriver driver, FetchConfigVO fetchConfigVO, FetchMetadataVO fetchMetadataVO,
			String type1, String type2, String type3, String param1, String param2, String param3) throws Exception {
		String param4 = "UIScmil1u";
		clickLink(driver, param4, param3, fetchMetadataVO, fetchConfigVO);
		clickLink(driver, param1, param2, fetchMetadataVO, fetchConfigVO);
		// clickSignInSignOut(driver, param6, fetchMetadataVO, fetchConfigVO);
//		clickButton(driver, param6, param2, fetchMetadataVO, fetchConfigVO);
	}

	public void navigateUrl(WebDriver driver, FetchConfigVO fetchConfigVO, FetchMetadataVO fetchMetadataVO) {
		try {
			driver.navigate().to(fetchConfigVO.getApplication_url());
			driver.manage().window().maximize();
			deleteAllCookies(driver, fetchMetadataVO, fetchConfigVO);
			refreshPage(driver, fetchMetadataVO, fetchConfigVO);
			switchToActiveElement(driver, fetchMetadataVO, fetchConfigVO);
			logger.info("Navigated to given Url");
		} catch (Exception e) {
			logger.error("Failed During Navigation");
			screenshotFail(driver, "Failed during navigateUrl Method", fetchMetadataVO, fetchConfigVO);
			System.out.println("Not able to navitage to the Url");
		}
	}

	public String loginPage(WebDriver driver, String param1, String keysToSend, FetchMetadataVO fetchMetadataVO,
			FetchConfigVO fetchConfigVO) {
		try {
			if (param1.equalsIgnoreCase("password")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='" + param1 + "']")));
				JavascriptExecutor jse = (JavascriptExecutor) driver;
				jse.executeScript("document.getElementById('password').value = '" + keysToSend + "';");
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Thread.sleep(1000);
				enter(driver, fetchMetadataVO, fetchConfigVO);
				return keysToSend;
			}
		} catch (Exception e) {
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
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			Thread.sleep(1000);
			return keysToSend;
		} catch (Exception e) {
			logger.error("Failed During Login page");
			screenshotFail(driver, "Failed During Login page", fetchMetadataVO, fetchConfigVO);
			System.out.println("Failed During Login page");
		}
		return keysToSend;
	}

	public void navigator(WebDriver driver, String param1, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO)
			throws Exception {
		try {
			Thread.sleep(4000);
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@title='" + param1 + "']")));
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@title='" + param1 + "']")));
			WebElement waittext = driver.findElement(By.xpath("//a[@title='" + param1 + "']"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			actions.moveToElement(waittext).click().build().perform();
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
			logger.error("Failed During Navigation");
			screenshotFail(driver, "Failed during navigateUrl Method", fetchMetadataVO, fetchConfigVO);
			System.out.println("Not able to navitage to the Url");
			throw e;
		}
	}

	public void menuNavigation(WebDriver driver, String param1, FetchMetadataVO fetchMetadataVO,
			FetchConfigVO fetchConfigVO) throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//*[contains(@id,'popup-container')]//*[@title='" + param1 + "']")));
			wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("//*[contains(@id,'popup-container')]//*[@title='" + param1 + "']")));
			WebElement waittext = driver
					.findElement(By.xpath("//*[contains(@id,'popup-container')]//*[@title='" + param1 + "']"));
			WebElement showmore = driver
					.findElement(By.xpath("//*[contains(@id,'popup-container')]//a[text()='Show More']"));
			Actions actions = new Actions(driver);
			actions.moveToElement(showmore).build().perform();
			actions.moveToElement(showmore).click().build().perform();
			Thread.sleep(3000);
			WebElement showless = driver
					.findElement(By.xpath("//*[contains(@id,'popup-container')]//a[text()='Show Less']"));
			actions.moveToElement(showless).build().perform();
			actions.moveToElement(showless).click().build().perform();
			Thread.sleep(3000);
			actions.moveToElement(waittext).build().perform();
			actions.moveToElement(waittext).click().build().perform();
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
			logger.error("Failed During Navigation");
			screenshotFail(driver, "Failed during Navigation Method", fetchMetadataVO, fetchConfigVO);
			System.out.println("Not able to navitage to the :" + "" + param1);
			throw e;
		}
	}

	public void menuNavigationButton(WebDriver driver, String param1, FetchMetadataVO fetchMetadataVO,
			FetchConfigVO fetchConfigVO) throws Exception {
		try {
			Thread.sleep(3000);
			if (param1.equalsIgnoreCase("Assets")) {
				WebElement asset = driver.findElement(By.xpath(
						"//span[normalize-space(text())='Fixed Assets']/following::span[normalize-space(text())='"
								+ param1 + "']"));
				Actions actions = new Actions(driver);
				actions.moveToElement(asset).build().perform();
				actions.moveToElement(asset).click().build().perform();
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			} else {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("//span[normalize-space(text())='" + param1 + "']")));
				wait.until(ExpectedConditions
						.elementToBeClickable(By.xpath("//span[normalize-space(text())='" + param1 + "']")));
				WebElement waittext = driver.findElement(By.xpath("//span[normalize-space(text())='" + param1 + "']"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				actions.moveToElement(waittext).click().build().perform();
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
			logger.error("Failed During Navigation");
			screenshotFail(driver, "Failed during Navigation Method", fetchMetadataVO, fetchConfigVO);
			System.out.println("Not able to navitage to the :" + "" + param1);
			throw e;
		}
	}

	public void task(WebDriver driver, String param1, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO)
			throws Exception {
		try {
			Thread.sleep(7000);
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@title='" + param1 + "']")));
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@title='" + param1 + "']")));
			WebElement waittext = driver.findElement(By.xpath("//img[@title='" + param1 + "']"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
			logger.error("Failed to Open Task Menu");
			screenshotFail(driver, "Failed to Open Task Menu", fetchMetadataVO, fetchConfigVO);
			System.out.println("Failed to Open Task Menu");
			throw e;
		}
	}

	public void taskMenu(WebDriver driver, String param1, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO)
			throws Exception {
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
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
			logger.error("Failed to Open Task Menu");
			screenshotFail(driver, "Failed to Open Task Menu", fetchMetadataVO, fetchConfigVO);
			System.out.println("Failed to Open Task Menu");
			throw e;
		}
	}

	public void mediumWait(WebDriver driver, String inputData, FetchMetadataVO fetchMetadataVO,
			FetchConfigVO fetchConfigVO) {
		try {
			int time = StringUtils.convertStringToInteger(inputData, 4);
			int seconds = time * 1000;
			Thread.sleep(seconds);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		} catch (InterruptedException e) {
			e.printStackTrace(); // Restore interrupted state...
			Thread.currentThread().interrupt();
		}
	}

	public void shortwait(WebDriver driver, String inputData) {
		try {
			int time = StringUtils.convertStringToInteger(inputData, 2);
			int seconds = time * 1000;
			Thread.sleep(seconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
			// Restore interrupted state...
			Thread.currentThread().interrupt();
		}
	}

	public void wait(WebDriver driver, String inputData) {
		try {
			int time = StringUtils.convertStringToInteger(inputData, 8);
			int seconds = time * 1000;
			Thread.sleep(seconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
			// Restore interrupted state...
			Thread.currentThread().interrupt();
		}
	}

	public List<String> getFailFileNameListNew(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO) {

//		 File folder = new File(fetchConfigVO.getScreenshot_path() + fetchMetadataListVO.get(0).getCustomer_name() + "\\"
//		 + fetchMetadataListVO.get(0).getTest_run_name() + "\\");

		File folder = new File("C:\\Users\\winfo83\\Documents\\wats\\passedpdfs\\PPM Dry Run 1\\");

		File[] listOfFiles = folder.listFiles();

		// List<File> fileList = Arrays.asList(listOfFiles);
		List<File> allFileList = Arrays.asList(listOfFiles);
		        List<File> fileList = new ArrayList<>();
//		        String seqNumber = fetchMetadataListVO.get(0).getSeq_num();
		        String seqNumber = "5";

		        for (File file : allFileList) {
		            if(file.getName().startsWith(seqNumber+"_")) {
		                fileList.add(file);
		            }
		        }

		Collections.sort(fileList, new Comparator<File>() {

		public int compare(File f1, File f2) {

			return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified()) * -1;

		}

		});

		List<String> fileNameList = new ArrayList<String>();

		 if (fileList.get(0).getName().endsWith("Failed.jpg")) {
		
		 fileNameList.add(fileList.get(0).getName());
		
		 for (int i = 1; i < fileList.size(); i++) {
		
		 if (!fileList.get(i).getName().endsWith("Failed.jpg")) {
		
		 fileNameList.add(fileList.get(i).getName());
		
		 } else {
		break;
	
		
		 }
		
		 }
		
		 Collections.reverse(fileNameList);
		
		 }
//		if (!fileList.get(0).getName().endsWith("Failed.jpg")) {
//
//		fileNameList.add(fileList.get(0).getName());
//
//		for (int i = 1; i < fileList.size(); i++) {
//
//		if (!fileList.get(i).getName().endsWith("Failed.jpg")) {
//
//		fileNameList.add(fileList.get(i).getName());
//
//		} else {
//		fileNameList.add(fileList.get(i).getName());
//
//		break;
//
//		}
//
//		}
//
//
//		}
//		else {
//		fileNameList.add(fileList.get(0).getName());
//
//		}
		System.out.println(fileNameList);
		//Collections.reverse(fileNameList);

		// targetFileList.addAll(seqList);

		/*
		* for (String fileName : fileNameList) {
		*
		* System.out.println("Target File : " + fileName);
		*
		* }
		*/


		return fileNameList;

		}
	public List<String> getFileNameListNew(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO) {

//		 File folder = new File(fetchConfigVO.getScreenshot_path() + fetchMetadataListVO.get(0).getCustomer_name() + "\\"
//		 + fetchMetadataListVO.get(0).getTest_run_name() + "\\");
		File folder = new File("C:\\Users\\winfo83\\Documents\\wats\\passedpdfs\\PPM Dry Run 1\\");

		File[] listOfFiles = folder.listFiles();

		// List<File> fileList = Arrays.asList(listOfFiles);
		List<File> allFileList = Arrays.asList(listOfFiles);
		        List<File> fileList = new ArrayList<>();
//		        String seqNumber = fetchMetadataListVO.get(0).getSeq_num();
		      String seqNumber = "3";

		        for (File file : allFileList) {
		            if(file.getName().startsWith(seqNumber+"_")) {
		                fileList.add(file);
		            }
		        }

		Collections.sort(fileList, new Comparator<File>() {

		public int compare(File f1, File f2) {

			return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified()) * -1;
		}

		});

		List<String> fileNameList = new ArrayList<String>();

		if (!fileList.get(0).getName().endsWith("Failed.jpg")) {

		fileNameList.add(fileList.get(0).getName());

		for (int i = 1; i < fileList.size(); i++) {

		if (!fileList.get(i).getName().endsWith("Failed.jpg")) {

		fileNameList.add(fileList.get(i).getName());

		} else {


		}

		}

//		Collections.reverse(fileNameList);

		}
		Collections.reverse(fileNameList);

		// targetFileList.addAll(seqList);

		/*
		* for (String fileName : fileNameList) {
		*
		* System.out.println("Target File : " + fileName);
		*
		* }
		*/

		return fileNameList;

		}

	public List<String> getPassedPdfNew(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO) {
         
//		 File folder = new File(fetchConfigVO.getScreenshot_path() + fetchMetadataListVO.get(0).getCustomer_name() + "\\"
//		 + fetchMetadataListVO.get(0).getTest_run_name() + "\\");
		File folder = new File("C:\\Users\\winfo83\\Documents\\wats\\passedpdfs\\PPM Dry Run 1\\");
		 String VedioGen="on";
		File[] listOfFiles = folder.listFiles();

		Map<Integer, List<File>> filesMap = new TreeMap<>();

		for (File file : Arrays.asList(listOfFiles)) {

		Integer seqNum = Integer.valueOf(file.getName().substring(0, file.getName().indexOf('_')));

		if (!filesMap.containsKey(seqNum)) {

		filesMap.put(seqNum, new ArrayList<File>());
		 
		}

		filesMap.get(seqNum).add(file);

		}

		List<String> targetFileList = new ArrayList<>();
		ArrayList<String> links = new ArrayList<String>();
		for (Entry<Integer, List<File>> seqEntry : filesMap.entrySet()) {

		List<File> seqList = seqEntry.getValue();
		// TreeMap<Integer, File> map = new TreeMap<Integer, File>();
		// for(File f: seqList) {
		// String[] arr = f.getName().split("_", 0);
		// Integer seqNumf = Integer.valueOf(arr[1]);
		// map.put(seqNumf, f);
		//
		// }
		// List<File> fileValues = new ArrayList<>(map.values());
		Collections.sort(seqList, new Comparator<File>() {

		public int compare(File f1, File f2) {
			return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified()) * -1;
		}

		});
		List<String> seqFileNameList = new ArrayList<String>();
		ArrayList<String> links1 = new ArrayList<String>();
		if (!seqList.get(0).getName().endsWith("Failed.jpg")) {
			links1.add(seqList.get(0).getPath());
		seqFileNameList.add(seqList.get(0).getName());

		for (int i = 1; i < seqList.size(); i++) {

		if (!seqList.get(i).getName().endsWith("Failed.jpg")) {
			links1.add(seqList.get(i).getAbsolutePath());
		seqFileNameList.add(seqList.get(i).getName());

		} else {


		}

		}
		Collections.reverse(links1);
		 Collections.reverse(seqFileNameList);
		 links.addAll(links1);
		targetFileList.addAll(seqFileNameList);

		}

//		                    targetFileList.addAll(seqList);

		}

		/*
		* for (String fileName : targetFileList) {
		*
		* System.out.println("Target File : " + fileName);
		*
		* }
		*/
          String vidPath="C:\\Users\\winfo83\\Documents\\wats\\passedpdfs\\Passed_vedio.mp4";
          if(VedioGen.equalsIgnoreCase("ON")) {
//		convertJPGtoMovie(targetFileList,vidPath);
          }
		System.out.println(targetFileList.size());
		return targetFileList;

		}
	public List<String> getFailedPdfNew(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO) {

//		 File folder = new File(fetchConfigVO.getScreenshot_path() + fetchMetadataListVO.get(0).getCustomer_name() + "\\"
//		 + fetchMetadataListVO.get(0).getTest_run_name() + "\\");
		File folder = new File("C:\\Users\\winfo83\\Documents\\wats\\passedpdfs\\PPM Dry Run 1\\");
		File[] listOfFiles = folder.listFiles();
  
	    String VedioGen="";	
		Map<Integer, List<File>> filesMap = new TreeMap<>();

		for (File file : Arrays.asList(listOfFiles)) {

		Integer seqNum = Integer.valueOf(file.getName().substring(0, file.getName().indexOf('_')));

		if (!filesMap.containsKey(seqNum)) {

		filesMap.put(seqNum, new ArrayList<File>());

		}

		filesMap.get(seqNum).add(file);

		}

		List<String> targetFileList = new ArrayList<>();
		ArrayList<String> links = new ArrayList<String>();
		for (Entry<Integer, List<File>> seqEntry : filesMap.entrySet()) {

		List<File> seqList = seqEntry.getValue();

		Collections.sort(seqList, new Comparator<File>() {

		public int compare(File f1, File f2) {

			return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified()) * -1;

		}

		});

		List<String> seqFileNameList = new ArrayList<String>();
		ArrayList<String> links1 = new ArrayList<String>();
//
//		                    for (int i = 0; i < seqList.size(); i++) {
//
//		                            if (seqList.get(i).getName().endsWith("Failed.jpg")) {
//
//		                                   seqFileNameList.add(seqList.get(i).getName());
//
//		                             } else {
//
//		                             
//
//		                                       }
//		 
//		                            }
//
////		                            Collections.reverse(seqFileNameList);
//
//		                            targetFileList.addAll(seqFileNameList);

		                     

		 if (seqList.get(0).getName().endsWith("Failed.jpg")) {
		
         links1.add(seqList.get(0).getAbsolutePath());
		 seqFileNameList.add(seqList.get(0).getName());
		
		 for (int i = 1; i < seqList.size(); i++) {
		
		 if (!seqList.get(i).getName().endsWith("Failed.jpg")) {
	         links1.add(seqList.get(i).getAbsolutePath());
		 seqFileNameList.add(seqList.get(i).getName());
		
		 } else {
		
		 
		
		 }
		
		 }
		 Collections.reverse(links1);
		 Collections.reverse(seqFileNameList);
		 
		 links.addAll(links1);
		 targetFileList.addAll(seqFileNameList);
		
		 }

//		                    targetFileList.addAll(seqList);

		}

		/*
		* for (String fileName : targetFileList) {
		*
		* System.out.println("Target File : " + fileName);
		*
		* }
		*/
		 String vidPath="C:\\Users\\winfo83\\Documents\\wats\\passedpdfs\\field_vedio.mp4";
			
		 if(VedioGen.equalsIgnoreCase("ON")) {
//				convertJPGtoMovie(links,vidPath);
		          }
		return targetFileList;

		}

	public List<String> getDetailPdfNew(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO) {

//		 File folder = new File(fetchConfigVO.getScreenshot_path() + fetchMetadataListVO.get(0).getCustomer_name() + "\\"
//		 + fetchMetadataListVO.get(0).getTest_run_name() + "\\");
		File folder = new File("C:\\Users\\winfo83\\Documents\\wats\\passedpdfs\\PPM Dry Run 1\\");

		String VedioGen="";
		File[] listOfFiles = folder.listFiles();

		Map<Integer, List<File>> filesMap = new TreeMap<>();
		for (File file : Arrays.asList(listOfFiles)) {

		Integer seqNum = Integer.valueOf(file.getName().substring(0, file.getName().indexOf('_')));

		if (!filesMap.containsKey(seqNum)) {

		filesMap.put(seqNum, new ArrayList<File>());

		}

		filesMap.get(seqNum).add(file);

		}

		List<String> targetFileList = new ArrayList<>();

		List<String> targetSuccessFileList = new ArrayList<>();

		List<String> targetFailedFileList = new ArrayList<>();
		ArrayList<String> links = new ArrayList<String>();

		for (Entry<Integer, List<File>> seqEntry : filesMap.entrySet()) {

		List<File> seqList = seqEntry.getValue();

		Collections.sort(seqList, new Comparator<File>() {

		public int compare(File f1, File f2) {

			return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified())*-1;

		}

		});

		List<String> seqFileNameList = new ArrayList<String>();
		ArrayList<String> links1 = new ArrayList<String>();
		if (!seqList.get(0).getName().endsWith("Failed.jpg")) {
			links1.add(seqList.get(0).getAbsolutePath());
		seqFileNameList.add(seqList.get(0).getName());

//		                                  System.out.println("FIRST S STEP: "+seqList.get(0).getName());

		for (int i = 1; i < seqList.size(); i++) {

		if (!seqList.get(i).getName().endsWith("Failed.jpg")) {
			links1.add(seqList.get(i).getAbsolutePath());
		seqFileNameList.add(seqList.get(i).getName());

//		                                                                 System.out.println("S STEP: "+seqList.get(i).getName());

		}else {
			
		}

		}
		Collections.reverse(links1);
		Collections.reverse(seqFileNameList);
        links.addAll(links1);
		targetSuccessFileList.addAll(seqFileNameList);

		} else {


		             if (seqList.get(0).getName().endsWith("Failed.jpg")) {
			links1.add(seqList.get(0).getAbsolutePath());
		seqFileNameList.add(seqList.get(0).getName());

//		                                  System.out.println("FIRST S STEP: "+seqList.get(0).getName());

		for (int i = 1; i < seqList.size(); i++) {

		if (!seqList.get(i).getName().endsWith("Failed.jpg")) {
			links1.add(seqList.get(i).getAbsolutePath());
		seqFileNameList.add(seqList.get(i).getName());

//		                                                                 System.out.println("S STEP: "+seqList.get(i).getName());

		}else {
			
		}

		                       }
		                  Collections.reverse(links1);
		          		Collections.reverse(seqFileNameList);
		                  links.addAll(links1);
		                  targetFailedFileList.addAll(seqFileNameList);

		             }

//		                                   System.out.println("SEQ : "+seqEntry.getKey());

		// seqFileNameList.add(seqList.get(0).getName());
		//
////		                                   System.out.println("FIRST F STEP: "+seqList.get(0).getName());
		//
		// for (int i = 1; i < seqList.size(); i++) {
		//
		// if (!seqList.get(i).getName().endsWith("Failed.jpg")) {
		//
		// seqFileNameList.add(seqList.get(i).getName());
		//
////		                                                                 System.out.println("F STEP: "+seqList.get(i).getName());
		//
		// } else {
		//
		// break;
		//
		// }
		//
		// }

		// Collections.reverse(seqFileNameList);
		//
		// targetFailedFileList.addAll(seqFileNameList);
		//
		// }

//		                    targetFileList.addAll(seqList);

		}
		}

		targetFileList.addAll(targetSuccessFileList);

		targetFileList.addAll(targetFailedFileList);

		/*
		* for (String fileName : targetFileList) {
		*
		* System.out.println("Target File : " + fileName);
		*
		* }
		*/
		 String vidPath="C:\\Users\\winfo83\\Documents\\wats\\passedpdfs\\detailed_vedio.mp4";
		 if(VedioGen.equalsIgnoreCase("ON")) {
//				convertJPGtoMovie(links,vidPath);
		          }
		return targetFileList;

		}






//	private void convertJPGtoMovie(List<String> links, String vidPath) {
//		
//		OpenCVFrameConverter.ToIplImage grabberConverter = new OpenCVFrameConverter.ToIplImage();
//		 FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(vidPath,1366,613);
//		 try {
//			 recorder.setFrameRate(0.33);
//            recorder.setVideoCodec(avcodec.AV_CODEC_ID_MPEG4);
//            recorder.setVideoBitrate(9000);
//            recorder.setFormat("mp4");
//            recorder.setVideoQuality(0); // maximum quality
//            recorder.start();
//            
//            for (String image : links) {
//				
//		        recorder.record(grabberConverter.convert(cvLoadImage(image)));
//		     }
//		     recorder.stop();
//		    }
//		    catch (org.bytedeco.javacv.FrameRecorder.Exception e){
//		       e.printStackTrace();
//		    }
//	 }
//		
	

	public List<String> getFailFileNameList(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO) {
		List<String> fileNameList = new ArrayList<String>();
		File folder = new File(fetchConfigVO.getScreenshot_path() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
				+ fetchMetadataListVO.get(0).getTest_run_name() + "/");
		File[] listOfFiles = folder.listFiles();
		Arrays.sort(listOfFiles, new Comparator<File>() {
			public int compare(File f1, File f2) {
				return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());

			}
		});
		String seqNumber = fetchMetadataListVO.get(0).getSeq_num();
		String scripNumber = fetchMetadataListVO.get(0).getScript_number();
		String Number = fetchMetadataListVO.get(0).getLine_number();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				System.out.println("File " + listOfFiles[i].getName());
				String fileName = listOfFiles[i].getName();
				String[] fileNameArr = fileName.split("\\.");
				String fileExt = fileNameArr[fileNameArr.length - 1];
				String[] _arr = fileName.split("_");
				String currentScriptNumber = _arr[3];
				String seq = _arr[0];
				String Status = _arr[6];
				String status = Status.split("\\.")[0];
				if ("jpg".equalsIgnoreCase(fileExt) && scripNumber.equalsIgnoreCase(currentScriptNumber)
						&& seqNumber.equalsIgnoreCase(seq)) {
					fileNameList.add(fileName);
				}
			}
		}
		return fileNameList;
	}

	public List<String> getFileNameList(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO) {
		List<String> fileNameList = new ArrayList<String>();
		File folder = new File(fetchConfigVO.getScreenshot_path() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
				+ fetchMetadataListVO.get(0).getTest_run_name() + "/");
		File[] listOfFiles = folder.listFiles();
		Arrays.sort(listOfFiles, new Comparator<File>() {
			public int compare(File f1, File f2) {
				return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());

			}
		});
		String seqNumber = fetchMetadataListVO.get(0).getSeq_num();
		String scripNumber = fetchMetadataListVO.get(0).getScript_number();
		String Number = fetchMetadataListVO.get(0).getLine_number();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				System.out.println("File " + listOfFiles[i].getName());
				String fileName = listOfFiles[i].getName();
				String[] fileNameArr = fileName.split("\\.");
				String fileExt = fileNameArr[fileNameArr.length - 1];
				String[] _arr = fileName.split("_");
				String currentScriptNumber = _arr[3];
				String seq = _arr[0];
				String Status = _arr[6];
				String status = Status.split("\\.")[0];
				if ("jpg".equalsIgnoreCase(fileExt) && scripNumber.equalsIgnoreCase(currentScriptNumber)
						&& "Passed".equalsIgnoreCase(status) && seqNumber.equalsIgnoreCase(seq)) {
					fileNameList.add(fileName);
				}
			}
		}
		return fileNameList;
	}

	public List<String> getPassedPdf(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO) {
		List<String> fileNameList = new ArrayList<String>();
		File folder = new File(fetchConfigVO.getScreenshot_path() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
				+ fetchMetadataListVO.get(0).getTest_run_name() + "/");
		File[] listOfFiles = folder.listFiles();
		Arrays.sort(listOfFiles, new Comparator<File>() {
			public int compare(File f1, File f2) {
				return Long.valueOf(f1.getName().split("_", 0)[1])
						.compareTo(Long.valueOf(f2.getName().split("_", 0)[1]));

			}
		});
		String seqNumber = fetchMetadataListVO.get(0).getSeq_num();
		String scripNumber = fetchMetadataListVO.get(0).getScript_number();
		String STATUS = fetchMetadataListVO.get(0).getStatus();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				System.out.println("File " + listOfFiles[i].getName());
				String fileName = listOfFiles[i].getName();
				String[] fileNameArr = fileName.split("\\.");
				String fileExt = fileNameArr[fileNameArr.length - 1];
				String[] _arr = fileName.split("_");
				String currentScriptNumber = _arr[3];
				String seq = _arr[0];
				String Status = _arr[6];
				String status = Status.split("\\.")[0];
				if ("jpg".equalsIgnoreCase(fileExt) && "Passed".equalsIgnoreCase(status)) {
					fileNameList.add(fileName);
				}
			}
		}
		return fileNameList;
	}

	public List<String> getFailedPdf(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO) {

		List<String> fileNameList = new ArrayList<String>();
		File folder = new File(fetchConfigVO.getScreenshot_path() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
				+ fetchMetadataListVO.get(0).getTest_run_name() + "/");
		File[] listOfFiles = folder.listFiles();
		Arrays.sort(listOfFiles, new Comparator<File>() {
			public int compare(File f1, File f2) {
				return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());

			}
		});
		String scripNumber = fetchMetadataListVO.get(0).getScript_number();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				System.out.println("File " + listOfFiles[i].getName());
				String fileName = listOfFiles[i].getName();
				String[] fileNameArr = fileName.split("\\.");
				String fileExt = fileNameArr[fileNameArr.length - 1];
				String[] _arr = fileName.split("_");
				String currentScriptNumber = _arr[3];
				String Status = _arr[6];
				String status = Status.split("\\.")[0];
				if ("jpg".equalsIgnoreCase(fileExt) && ("Failed".equalsIgnoreCase(status))) {
					fileNameList.add(fileName);
				}
			}
		}
		return fileNameList;
	}

	public List<String> getDetailPdf(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO) {
		List<String> fileNameList = new ArrayList<String>();
		File folder = new File(fetchConfigVO.getScreenshot_path() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
				+ fetchMetadataListVO.get(0).getTest_run_name() + "/");
		File[] listOfFiles = folder.listFiles();
		Arrays.sort(listOfFiles, new Comparator<File>() {
			public int compare(File f1, File f2) {
				return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());

			}
		});
		String scripNumber = fetchMetadataListVO.get(0).getScript_number();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				System.out.println("File " + listOfFiles[i].getName());
				String fileName = listOfFiles[i].getName();
				String[] fileNameArr = fileName.split("\\.");
				String fileExt = fileNameArr[fileNameArr.length - 1];
				String[] _arr = fileName.split("_");
				String currentScriptNumber = _arr[3];
				String Status = _arr[6];
				String status = Status.split("\\.")[0];
				if ("jpg".equalsIgnoreCase(fileExt)) {
					fileNameList.add(fileName);
				}
			}
		}
		return fileNameList;
	}

	public void createPdf(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO, String pdffileName,
			int passcount, int failcount) throws IOException, DocumentException, com.itextpdf.text.DocumentException {
		try {
			String Date = DateUtils.getSysdate();
//			 String Folder = (fetchConfigVO.getPdf_path() +
//			 fetchMetadataListVO.get(0).getCustomer_name() + "\\"
//			 + fetchMetadataListVO.get(0).getTest_run_name() + "\\");
			String Folder = "C:\\Users\\winfo83\\Documents\\wats\\passedpdfs\\";
			String FILE = (Folder + pdffileName);
			System.out.println(FILE);
			List<String> fileNameList = null;
			if ("Passed_Report.pdf".equalsIgnoreCase(pdffileName)) {
				fileNameList = getPassedPdfNew(fetchMetadataListVO, fetchConfigVO);
			} else if ("Failed_Report.pdf".equalsIgnoreCase(pdffileName)) {
				fileNameList = getFailedPdfNew(fetchMetadataListVO, fetchConfigVO);
			} else if ("Detailed_Report.pdf".equalsIgnoreCase(pdffileName)) {
				fileNameList = getDetailPdfNew(fetchMetadataListVO, fetchConfigVO);
			} else {
				fileNameList = getFileNameListNew(fetchMetadataListVO, fetchConfigVO);
			}
			String Script_Number = fetchMetadataListVO.get(0).getScript_number();
			String customer_Name = fetchMetadataListVO.get(0).getCustomer_name();
			String test_Run_Name = fetchMetadataListVO.get(0).getTest_run_name();
			String Scenario_Name = fetchMetadataListVO.get(0).getScenario_name();
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
			
//			start piechart and table code
			Document document = new Document();
			String start = "Execution Summary";
			String pichart = "Pie-Chart";
			 Font bfBold12 = FontFactory.getFont("Arial", 25); 
			 Font fnt = FontFactory.getFont("Arial", 12);
			 DefaultPieDataset dataSet = new DefaultPieDataset();
			PdfWriter writer = null;
			writer = PdfWriter.getInstance(document, new FileOutputStream(FILE));
			Rectangle one = new Rectangle(1900,1000);
//			Image one = Image.getInstance(
//					"C:\\Users\\winfo83\\Documents\\wats\\passedpdfs\\PPM Dry Run 1\\"
//							+ fileNameList.get(1));
	        document.setPageSize(one);
			document.open();
			
			
			if(passcount==0) {
				
				dataSet.setValue("Fail", failcount);
			}else if(failcount==0) {
				dataSet.setValue("Pass", passcount);
			}
			else {
				dataSet.setValue("Pass", passcount);
				dataSet.setValue("Fail", failcount);
			}
			double pass=Math.round((passcount * 100.0) /(passcount + failcount));
			double fail=Math.round((failcount * 100.0) /(passcount + failcount));
		
	        document.add(new Paragraph(start, bfBold12));
	        document.add(Chunk.NEWLINE);
	   	 DecimalFormat df1 = new DecimalFormat("0");
	   	 DecimalFormat df2 = new DecimalFormat("0");
	   	 Font bf12 = new Font(FontFamily.TIMES_ROMAN, 23);
	   	PdfPTable table = new PdfPTable(3); 
		 table.setWidths(new int[]{1, 1, 1});
		 table.setWidthPercentage(100f);
		 insertCell(table, "Status", Element.ALIGN_CENTER, 1, bfBold12);
	     insertCell(table, "Total", Element.ALIGN_CENTER, 1, bfBold12);
	     insertCell(table, "Percentage", Element.ALIGN_CENTER, 1, bfBold12);
//	     table.setHeaderRows(1);
	     PdfPCell[] cells1 = table.getRow(0).getCells(); 
		  for (int k=0;k<cells1.length;k++){
		     cells1[k].setBackgroundColor(new BaseColor(161, 190, 212));
		  }
	     insertCell(table, "Passed", Element.ALIGN_CENTER, 1, bf12);
	     insertCell(table, df1.format(passcount),  Element.ALIGN_CENTER, 1, bf12);
	     insertCell(table,df2.format(pass)+"%",  Element.ALIGN_CENTER, 1, bf12);
	     
	     insertCell(table, "Failed", Element.ALIGN_CENTER, 1, bf12);
	     insertCell(table, df1.format(failcount),  Element.ALIGN_CENTER, 1, bf12);
	     insertCell(table, df2.format(fail)+"%",  Element.ALIGN_CENTER, 1, bf12);
	     document.setMargins(20, 20, 20, 20);
	     document.add(table);
	     Font bfBold = FontFactory.getFont("Arial", 25,BaseColor.WHITE);
	     Chunk ch = new Chunk(pichart, bfBold);
	     ch.setTextRise(-18);
	     ch.setBackground(new BaseColor(38, 99, 175), 0f, 10f, 1730f, 15f);
	     
	     Paragraph p1 = new Paragraph(ch);
	     p1.setSpacingBefore(50);
	     document.add(p1);    
	        
	     JFreeChart chart = ChartFactory.createPieChart(
				 " ", dataSet, true, true, false);
			Color c1=new Color(102, 255, 102);
			Color c=new Color(253, 32, 32);
			
			LegendTitle legend = chart.getLegend();
			 PiePlot piePlot = (PiePlot) chart.getPlot();
			 piePlot.setSectionPaint("Pass",c1);
			 piePlot.setSectionPaint("Fail", c);
			 piePlot.setBackgroundPaint(Color.WHITE);
			 piePlot.setOutlinePaint(null);
			 piePlot.setLabelBackgroundPaint(null);
			 piePlot.setLabelOutlinePaint(null);
			 piePlot.setLabelGenerator(new StandardPieSectionLabelGenerator());
			 piePlot.setInsets(new RectangleInsets(10, 5.0, 5.0, 5.0));
			 piePlot.setLabelShadowPaint(null);
			 piePlot.setShadowXOffset(0.0D);
			 piePlot.setShadowYOffset(0.0D); 
			 piePlot.setLabelGenerator(null);
			 piePlot.setBackgroundAlpha(0.4f);
			 piePlot.setExplodePercent("Pass", 0.05);
			 piePlot.setSimpleLabels(true);
		   piePlot.setSectionOutlinesVisible(false);
		   java.awt.Font f2=new java.awt.Font("", java.awt.Font.PLAIN, 22);
		   piePlot.setLabelFont(f2);
		   
		   if(passcount==0||failcount==0) {
			    PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator(
			    		  "{2}", new DecimalFormat("0"), new DecimalFormat("0%")) ;
			  piePlot.setLegendLabelGenerator(gen);
			  legend.setPosition(RectangleEdge.BOTTOM);
			   legend.setVerticalAlignment(VerticalAlignment.CENTER);
		   }else {
			   PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator(
			    		  "{2}", new DecimalFormat("0"), new DecimalFormat("0%")) ;
			  piePlot.setLabelGenerator(gen);
			  legend.setPosition(RectangleEdge.RIGHT);
			   legend.setVerticalAlignment(VerticalAlignment.CENTER);
		   }
		   piePlot.setInsets(new RectangleInsets(0.0, 5.0, 5.0, 5.0));
		   legend.setFrame(BlockBorder.NONE);
		   legend.setFrame(new LineBorder(Color.white, new BasicStroke(20f),
				    new RectangleInsets(1.0, 1.0, 1.0, 1.0)));
		   
		   java.awt.Font pass1=new java.awt.Font("", Font.NORMAL, 22);
				  legend.setItemFont(pass1);
				  PdfContentByte contentByte = writer.getDirectContent();
					PdfTemplate template = contentByte.createTemplate(1000, 900);
					Graphics2D graphics2d = template.createGraphics(900, 500,
							new DefaultFontMapper());
					Rectangle2D rectangle2d = new Rectangle2D.Double(0, 0, 750,
							450);
					chart.draw(graphics2d, rectangle2d);
					graphics2d.dispose();
					contentByte.addTemplate(template, 500, 100);
			
			
			
			int i=0;
			for (String image : fileNameList) {
//				 Image img = Image.getInstance(
//				 fetchConfigVO.getScreenshot_path() + customer_Name + "\\" + test_Run_Name +
//				 "\\" + image);
				i++;
				Image img = Image.getInstance(
						"C:\\Users\\winfo83\\Documents\\wats\\passedpdfs\\PPM Dry Run 1\\"
								+ image);

				String ScriptNumber = image.split("_")[3];
				String TestRun = image.split("_")[4];
				String Status = image.split("_")[6];
				String status = Status.split("\\.")[0];
				String Scenario = image.split("_")[2];
				document.setPageSize(img);
				document.newPage();
				
				String TR = "Test Run Name:" + " " + TestRun;
				String SN = "Script Number:" + " " + ScriptNumber;
				String S = "Status:" + " " + status;
				String Scenarios = "Scenario Name :" + "" + Scenario;
				Image img1 = Image.getInstance("C:\\Users\\winfo83\\Documents\\wats\\passedpdfs\\wats_icon.png");
				img1.scalePercent(65, 65);
		         img1.setAlignment(Image.ALIGN_RIGHT);
		        document.add(img1);
				document.add(new Paragraph(TR, fnt));
				document.add(new Paragraph(SN, fnt));
				document.add(new Paragraph(S, fnt));
				document.add(new Paragraph(Scenarios, fnt));
				document.add(Chunk.NEWLINE);
				
				Paragraph p=new Paragraph(String.format("page %s of %s", i, fileNameList.size()));
				p.setAlignment(Element.ALIGN_RIGHT);
				img.setAlignment(Image.ALIGN_CENTER);
				img.isScaleToFitHeight();
				img.scalePercent(60, 60);
				document.add(img);
				document.add(p);
				System.out.println("This Image " + "" + image + "" + "was added to the report");
			}
			document.close();
//			compress(fetchMetadataListVO, fetchConfigVO, pdffileName);
		} catch (Exception e) {
			System.out.println("Not able to Create pdf");
		}
	}
	 private static void insertCell(PdfPTable table, String text, int align, int colspan, Font font){
  	   
   	  //create a new cell with the specified Text and Font
   	  PdfPCell cell = new PdfPCell(new Paragraph(text.trim(), font));
   	  cell.setBorder(PdfPCell.NO_BORDER);
   	  //set the cell alignment
   	  
   	  cell.setUseVariableBorders(true);
  	  if(text.equalsIgnoreCase("Status")) {
  		cell.setBorderWidthLeft(0.3f);
  		cell.setBorderColorLeft(new BaseColor(230, 225, 225));
  		cell.setBorderWidthTop(0.3f); 
  		cell.setBorderColorTop(new BaseColor(230, 225, 225));
  	    cell.setBorderWidthRight(0.3f);
  	 cell.setBorderColorRight(new BaseColor(230, 225, 225));
  	    cell.setBorderWidthBottom(0.3f);
  	 cell.setBorderColorBottom(new BaseColor(230, 225, 225));
   	  }
  	  else if(text.equalsIgnoreCase("Total")) {
  		 cell.setBorderWidthTop(0.3f); 
  		cell.setBorderColorTop(new BaseColor(230, 225, 225));
  		 cell.setBorderWidthRight(0.3f);
  		cell.setBorderColorRight(new BaseColor(230, 225, 225));
  	  cell.setBorderWidthBottom(0.3f);
  	cell.setBorderColorBottom(new BaseColor(230, 225, 225));
  	  }else if(text.equalsIgnoreCase("Percentage")) {
  		 cell.setBorderWidthTop(0.3f); 
  		cell.setBorderColorTop(new BaseColor(230, 225, 225));
  		 cell.setBorderWidthRight(0.3f);
  		cell.setBorderColorRight(new BaseColor(230, 225, 225));
  	  cell.setBorderWidthBottom(0.3f);
  	cell.setBorderColorBottom(new BaseColor(230, 225, 225));
  	  }
  	else if(text.equalsIgnoreCase("Passed")||text.equalsIgnoreCase("Failed")) {
  		cell.setBorderWidthLeft(0.3f);
  		cell.setBorderColorLeft(new BaseColor(230, 225, 225));
		 cell.setBorderWidthRight(0.3f);
		cell.setBorderColorRight(new BaseColor(230, 225, 225));
	  cell.setBorderWidthBottom(0.3f);
	 cell.setBorderColorBottom(new BaseColor(230, 225, 225));
	  }
  	else if(text.contains("%")) {
  	 cell.setBorderWidthRight(0.3f);
  	cell.setBorderColorRight(new BaseColor(230, 225, 225));
  	    cell.setBorderWidthBottom(0.3f);
  	cell.setBorderColorBottom(new BaseColor(230, 225, 225));
  	}
  	else {
  	 cell.setBorderWidthRight(0.3f);
  	cell.setBorderColorRight(new BaseColor(230, 225, 225));
  		cell.setBorderWidthBottom(0.3f);
  		cell.setBorderColorBottom(new BaseColor(230, 225, 225));
  	}
  	  
  	      cell.setHorizontalAlignment(align);
   	  cell.setColspan(colspan);
   	  //in case there is no text and you wan to create an empty row
   	  if(text.trim().equalsIgnoreCase("")){
   	   cell.setMinimumHeight(20f);
   	  }
   	  cell.setFixedHeight(40f);
   	  //add the call to the table
   	  table.addCell(cell);
   	   
    
}
	public List<String> getImages(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO) {
		List<String> fileNameList = new ArrayList<String>();
		File folder = new File(fetchConfigVO.getScreenshot_path() + "\\" + fetchMetadataListVO.get(0).getCustomer_name()
				+ "\\" + fetchMetadataListVO.get(0).getTest_run_name() + "\\");
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

	public void compress(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO, String pdffileName)
			throws IOException {
		String Folder = (fetchConfigVO.getScreenshot_path() + "\\" + fetchMetadataListVO.get(0).getCustomer_name()
				+ "\\" + fetchMetadataListVO.get(0).getTest_run_name() + "\\");
		List<String> fileNameList = null;
		String customer_Name = fetchMetadataListVO.get(0).getCustomer_name();
		String test_Run_Name = fetchMetadataListVO.get(0).getTest_run_name();
		fileNameList = getImages(fetchMetadataListVO, fetchConfigVO);

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

	public List<String> getLowFileNameList(List<FetchMetadataVO> fetchMetadataListVO) {
		List<String> fileNameList = new ArrayList<String>();
		File folder = new File("C:\\Kaushik\\");
		File[] listOfFiles = folder.listFiles();
		Arrays.sort(listOfFiles, new Comparator<File>() {
			public int compare(File f1, File f2) {
				return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());

			}
		});
		String scripNumber = fetchMetadataListVO.get(0).getScript_number();
		String Number = fetchMetadataListVO.get(0).getLine_number();
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
				if ("jpg".equalsIgnoreCase(fileExt) && scripNumber.equalsIgnoreCase(currentScriptNumber)
						&& "Passed".equalsIgnoreCase(status)) {
					fileNameList.add(fileName);
				}
			}
		}
		return fileNameList;
	}

	public List<String> getLowPassedPdf(List<FetchMetadataVO> fetchMetadataListVO) {
		List<String> fileNameList = new ArrayList<String>();
		File folder = new File("C:\\Kaushik\\");
		File[] listOfFiles = folder.listFiles();
		Arrays.sort(listOfFiles, new Comparator<File>() {
			public int compare(File f1, File f2) {
				return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());

			}
		});
		String scripNumber = fetchMetadataListVO.get(0).getScript_number();
		String STATUS = fetchMetadataListVO.get(0).getStatus();
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

	public List<String> getLowFailedPdf(List<FetchMetadataVO> fetchMetadataListVO) {

		List<String> fileNameList = new ArrayList<String>();
		File folder = new File("C:\\Kaushik\\");
		File[] listOfFiles = folder.listFiles();
		Arrays.sort(listOfFiles, new Comparator<File>() {
			public int compare(File f1, File f2) {
				return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());

			}
		});
		String scripNumber = fetchMetadataListVO.get(0).getScript_number();
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
				if ("jpg".equalsIgnoreCase(fileExt) && ("Failed".equalsIgnoreCase(status))) {
					fileNameList.add(fileName);
				}
			}
		}
		return fileNameList;
	}

	public List<String> getLowDetailPdf(List<FetchMetadataVO> fetchMetadataListVO) {
		List<String> fileNameList = new ArrayList<String>();
		File folder = new File("C:\\Kaushik\\");
		File[] listOfFiles = folder.listFiles();
		Arrays.sort(listOfFiles, new Comparator<File>() {
			public int compare(File f1, File f2) {
				return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());

			}
		});
		String scripNumber = fetchMetadataListVO.get(0).getScript_number();
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
				if ("jpg".equalsIgnoreCase(fileExt)) {
					fileNameList.add(fileName);
				}
			}
		}
		return fileNameList;
	}

	public void createLowPdf(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO, String pdffileName)
			throws IOException, DocumentException, com.itextpdf.text.DocumentException {
		try {
			String Date = DateUtils.getSysdate();
			String Folder = ("C:\\Kaushik\\PDF\\");
			String FILE = (Folder + pdffileName);
			System.out.println(FILE);
			List<String> fileNameList = null;
			if ("Passed_Report.pdf".equalsIgnoreCase(pdffileName)) {
				fileNameList = getLowPassedPdf(fetchMetadataListVO);
			} else if ("Failed_Report.pdf".equalsIgnoreCase(pdffileName)) {
				fileNameList = getLowFailedPdf(fetchMetadataListVO);
			} else if ("Detailed_Report.pdf".equalsIgnoreCase(pdffileName)) {
				fileNameList = getLowDetailPdf(fetchMetadataListVO);
			} else {
				fileNameList = getLowFileNameList(fetchMetadataListVO);
			}
			String Script_Number = fetchMetadataListVO.get(0).getScript_number();
			String customer_Name = fetchMetadataListVO.get(0).getCustomer_name();
			String test_Run_Name = fetchMetadataListVO.get(0).getTest_run_name();
			String Scenario_Name = fetchMetadataListVO.get(0).getScenario_name();
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
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(FILE));
			document.open();
			for (String image : fileNameList) {
				Image img = Image.getInstance("C:\\Kaushik\\" + image);
				String ScriptNumber = image.split("_")[2];
				String TestRun = image.split("_")[3];
				String Status = image.split("_")[6];
				String status = Status.split("\\.")[0];
				String Scenario = image.split("_")[1];
				document.setPageSize(img);
				document.newPage();
				Font fnt = FontFactory.getFont("Arial", 12);
				String TR = "Test Run Name:" + " " + TestRun;
				String SN = "Script Number:" + " " + ScriptNumber;
				String S = "Status:" + " " + status;
				String Scenarios = "Scenario Name :" + "" + Scenario;
				document.add(new Paragraph(TR, fnt));
				document.add(new Paragraph(SN, fnt));
				document.add(new Paragraph(S, fnt));
				document.add(new Paragraph(Scenarios, fnt));
				document.add(Chunk.NEWLINE);
				img.setAlignment(Image.ALIGN_CENTER);
				img.isScaleToFitHeight();
				img.scalePercent(60, 60);
				document.add(img);
			}
			document.close();
		} catch (Exception e) {
			System.out.println("Not able to upload the pdf");
		}
	}

	public void createFailedPdf(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO,
			String pdffileName) throws IOException, DocumentException, com.itextpdf.text.DocumentException {
		try {
			String Date = DateUtils.getSysdate();
//			String Folder = (fetchConfigVO.getPdf_path() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
//					+ fetchMetadataListVO.get(0).getTest_run_name() + "/");
			String Folder = "C:\\Users\\winfo83\\Documents\\wats\\passedpdfs\\";

			String FILE = (Folder + pdffileName);
			System.out.println(FILE);
			List<String> fileNameList = null;
			if ("Passed_Report.pdf".equalsIgnoreCase(pdffileName)) {
				fileNameList = getPassedPdfNew(fetchMetadataListVO, fetchConfigVO);
			} else if ("Failed_Report.pdf".equalsIgnoreCase(pdffileName)) {
				fileNameList = getFailedPdfNew(fetchMetadataListVO, fetchConfigVO);
			} else if ("Detailed_Report.pdf".equalsIgnoreCase(pdffileName)) {
				fileNameList = getDetailPdfNew(fetchMetadataListVO, fetchConfigVO);
			} else {
				fileNameList = getFailFileNameListNew(fetchMetadataListVO, fetchConfigVO);
			}
			String Script_Number = fetchMetadataListVO.get(0).getScript_number();
			String customer_Name = fetchMetadataListVO.get(0).getCustomer_name();
			String test_Run_Name = fetchMetadataListVO.get(0).getTest_run_name();
			String Scenario_Name = fetchMetadataListVO.get(0).getScenario_name();
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
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(FILE));
			document.open();
			int i=0;
			for (String image : fileNameList) {
//				Image img = Image.getInstance(
//						fetchConfigVO.getScreenshot_path() + customer_Name + "/" + test_Run_Name + "/" + image);
				Image img = Image.getInstance(
						"C:\\Users\\winfo83\\Documents\\wats\\passedpdfs\\UDG - PPM\\"
								+ image);
                i++;
				String ScriptNumber = image.split("_")[3];
				String TestRun = image.split("_")[4];
				String Status = image.split("_")[6];
				String status = Status.split("\\.")[0];
				String Scenario = image.split("_")[2];
				String Reason = image.split("_")[5];
				document.setPageSize(img);
				document.newPage();
				Font fnt = FontFactory.getFont("Arial", 12);
				String TR = "Test Run Name:" + " " + TestRun;
				String SN = "Script Number:" + " " + ScriptNumber;
				String S = "Status:" + " " + status;
				String Scenarios = "Scenario Name :" + "" + Scenario;
				String Message = "Failed at Line Number:" + "" + Reason;
				// String message = "Failed at
				// :"+fetchMetadataListVO.get(0).getInput_parameter();
				Image img1 = Image.getInstance("C:\\Users\\winfo83\\Documents\\wats\\passedpdfs\\wats_icon.png");
				img1.scalePercent(65, 65);
		         img1.setAlignment(Image.ALIGN_RIGHT);
		        document.add(img1);
				document.add(new Paragraph(TR, fnt));
				document.add(new Paragraph(SN, fnt));
				document.add(new Paragraph(S, fnt));
				document.add(new Paragraph(Scenarios, fnt));
				if (status.equalsIgnoreCase("Failed")) {
					document.add(new Paragraph(Message, fnt));
				}
				document.add(Chunk.NEWLINE);
				Paragraph p=new Paragraph(String.format("page %s of %s", i, fileNameList.size()));
				p.setAlignment(Element.ALIGN_RIGHT);
				img.setAlignment(Image.ALIGN_CENTER);
				img.isScaleToFitHeight();
				img.scalePercent(45, 45);
				document.add(img);
				document.add(p);
			}
			document.close();
			compress(fetchMetadataListVO, fetchConfigVO, pdffileName);
		} catch (Exception e) {
			System.out.println("Not able to upload the pdf");
		}
	}

	public void uploadPDF(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO) {
		try {
			String accessToken = getAccessTokenPdf(fetchConfigVO);
			List imageUrlList = new ArrayList();
			File imageDir = new File(fetchConfigVO.getPdf_path() + "\\" + fetchMetadataListVO.get(0).getCustomer_name()
					+ "\\" + fetchMetadataListVO.get(0).getTest_run_name() + "\\");
			System.out.println(imageDir);
			for (File imageFile : imageDir.listFiles()) {
				String imageFileName = imageFile.getName();
				System.out.println(imageFileName);
				imageUrlList.add(imageFileName);
				File pdfFile = new File(imageDir + "\\" + imageFileName);
				System.out.println(pdfFile);
				FileInputStream input = new FileInputStream(pdfFile);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				byte[] buffer = new byte[99999999];
				int l;
				while ((l = input.read(buffer)) > 0) {
					bos.write(buffer, 0, l);
				}
				input.close();
				byte[] data = bos.toByteArray();
				RestTemplate restTemplate = new RestTemplate();
				MultiValueMap<String, byte[]> bodyMap = new LinkedMultiValueMap<>();
				bodyMap.add("user-file", data);
				// Outer header
				HttpHeaders uploadSessionHeader = new HttpHeaders();
				// uploadSessionHeader.setContentType(MediaType.APPLICATION_JSON);
				uploadSessionHeader.add("Authorization", "Bearer " + accessToken);
				System.out.println(fetchConfigVO.getSharepoint_drive_id());
				System.out.println(fetchConfigVO.getSharepoint_item_id());
				HttpEntity<byte[]> uploadSessionRequest = new HttpEntity<>(null, uploadSessionHeader);
				ResponseEntity<Object> response = restTemplate.exchange("https://graph.microsoft.com/v1.0/drives/"
						+ fetchConfigVO.getSharepoint_drive_id() + "/items/" + fetchConfigVO.getSharepoint_item_id()
						+ ":/Screenshot/" + fetchMetadataListVO.get(0).getCustomer_name() + "/"
						+ fetchMetadataListVO.get(0).getTest_run_name() + "/" + imageFileName + ":/createUploadSession",
						HttpMethod.POST, uploadSessionRequest, Object.class);
				System.out.println(response);
				Map<String, Object> linkedMap = response.getBody() != null
						? (LinkedHashMap<String, Object>) response.getBody()
						: null;
				String uploadUrl = linkedMap != null ? StringUtils.convertToString(linkedMap.get("uploadUrl")) : null;

				HttpHeaders uploadingFileHeader = new HttpHeaders();
				uploadingFileHeader.setContentLength(data.length);
				uploadingFileHeader.add("Content-Range", "bytes " + 0 + "-" + (data.length - 1) + "/" + data.length);
				uploadingFileHeader.setContentType(MediaType.parseMediaType("application/pdf"));

				HttpEntity<byte[]> uploadingFileRequest = new HttpEntity<>(data, uploadingFileHeader);
				ResponseEntity<byte[]> putResponse = restTemplate.exchange(uploadUrl, HttpMethod.PUT,
						uploadingFileRequest, byte[].class);

				System.out.println(putResponse);
				System.out.println("response status: " + response.getStatusCode());
				System.out.println("response body: " + response.getBody());
				System.out.println("response : " + response);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public String getAccessTokenPdf(FetchConfigVO fetchConfigVO) {
		String acessToken = null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
			map.add("grant_type", "client_credentials");
			map.add("client_id", fetchConfigVO.getClient_id());
			map.add("client_secret", fetchConfigVO.getClient_secret());
			map.add("scope", "https://graph.microsoft.com/.default");

			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
			ResponseEntity<Object> response = restTemplate.exchange(
					"https://login.microsoftonline.com/" + fetchConfigVO.getTenant_id() + "/oauth2/v2.0/token",
					HttpMethod.POST, entity, Object.class);
			System.out.println(response);

			@SuppressWarnings("unchecked")
			Map<String, Object> linkedMap = response.getBody() != null ? (Map<String, Object>) response.getBody()
					: null;
			acessToken = linkedMap != null ? StringUtils.convertToString(linkedMap.get("access_token")) : null;
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println(acessToken);
		return acessToken;
	}

	public void copy(WebDriver driver, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
		try {
			Robot r = new Robot();
			r.keyPress(KeyEvent.VK_CONTROL);
			r.keyPress(KeyEvent.VK_C);
			r.keyRelease(KeyEvent.VK_C);
			r.keyRelease(KeyEvent.VK_CONTROL);

		} catch (Exception e) {
			screenshotFail(driver, "Failed during Copy Method", fetchMetadataVO, fetchConfigVO);
			e.printStackTrace();
			throw e;
		}
	}// input[@placeholder='Enter search terms']

	public void paste(WebDriver driver, String inputParam, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO,
			String globalValueForSteps)

			throws Exception {

		try {

			WebElement waittill = driver

					.findElement(By.xpath("//label[text()='" + inputParam + "']/following::input[1]"));

			String value = globalValueForSteps;

//          String value = copynumber(driver, inputParam1, inputParam2, fetchMetadataVO, fetchConfigVO)

			waittill.click();

			JavascriptExecutor jse = (JavascriptExecutor) driver;

			jse.executeScript("arguments[0].value='" + value + "';", waittill);

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

			return;

		} catch (Exception e) {

			System.out.println(e);

		}

		try {

			WebElement waittill = driver.findElement(By.xpath("//input[@placeholder='" + inputParam + "']"));

			String value = globalValueForSteps;

			waittill.click();

			JavascriptExecutor jse = (JavascriptExecutor) driver;

			jse.executeScript("arguments[0].value='" + value + "';", waittill);

			Thread.sleep(3000);

			return;

		} catch (Exception e) {

			screenshotFail(driver, "Failed during paste Method", fetchMetadataVO, fetchConfigVO);

			throw e;

		}

	}

	public void clear(WebDriver driver, String inputParam1, String inputParam2, FetchMetadataVO fetchMetadataVO,
			FetchConfigVO fetchConfigVO) {

		try {
			if (inputParam1.equalsIgnoreCase("Lines")) {
				WebElement waittill = driver.findElement(By.xpath("(//*[normalize-space(text())=\"" + inputParam1
						+ "\"]/following::label[normalize-space(text())='" + inputParam2
						+ "']/preceding-sibling::input)[1]"));
				clearMethod(driver, waittill);
				Thread.sleep(4000);
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
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			WebElement waittill = driver.findElement(
					By.xpath("(//label[contains(text(),'" + inputParam1 + "')]/preceding-sibling::input)[1]"));
			clearMethod(driver, waittill);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			Thread.sleep(4000);
			WebElement waittill = driver
					.findElement(By.xpath("(//*[normalize-space(text())='" + inputParam1 + "']/following::input)[1]"));
			clearMethod(driver, waittill);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("//*[contains(@placeholder,'" + inputParam1 + "')]"));
			clearMethod(driver, waittill);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			WebElement waittill = driver
					.findElement(By.xpath("//*[normalize-space(text())='" + inputParam1 + "']/following::textarea[1]"));
			clearMethod(driver, waittill);
			return;
		} catch (Exception e) {
			screenshotFail(driver, "Failed during clearAndType Method", fetchMetadataVO, fetchConfigVO);
			System.out.println(e);
			throw e;
		}
	}

	public void windowclose(WebDriver driver, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) {
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
					logger.info("Window closed Successfully");
				}
			}
		} catch (Exception e) {
			logger.error("Failed During WindowClose Acion.");
			screenshot(driver, "Failed during windowhandle Method", fetchMetadataVO, fetchConfigVO);
			throw e;
		}
	}

	public void switchToActiveElement(WebDriver driver, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) {
		try {
			driver.switchTo().activeElement();
			logger.info("Switched to Element Successfully");
		} catch (Exception e) {
			logger.error("Failed During switchToActiveElement Action.");
			screenshotFail(driver, "Failed during switchToActiveElement Method", fetchMetadataVO, fetchConfigVO);
			System.out.println(e.getMessage());
			throw e;
		}
	}

	public void clickMenu(WebDriver driver, String param1, String param2, FetchMetadataVO fetchMetadataVO,
			FetchConfigVO fetchConfigVO) throws Exception {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
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
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
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
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
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
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
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
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
			System.out.println(e);
			logger.error("Failed during Click action.");
			screenshotFail(driver, "Failed during clickLink Method", fetchMetadataVO, fetchConfigVO);
			throw e;
		}
	}

	public void clickSignInSignOut(WebDriver driver, String param1, FetchMetadataVO fetchMetadataVO,
			FetchConfigVO fetchConfigVO) throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath(("//button[normalize-space(normalize-space(text())='" + param1 + "')]"))));
			WebElement waittext = driver
					.findElement(By.xpath(("//button[normalize-space(normalize-space(text())='" + param1 + "')]")));
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			return;
		} catch (Exception e) {
			System.out.println(e);
			logger.error("Failed during Click action.");
			screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, fetchConfigVO);
			throw e;
		}
	}// *[text()='Action Required']/following::a[1]

	public void clickNotificationLink(WebDriver driver, String param1, FetchMetadataVO fetchMetadataVO,
			FetchConfigVO fetchConfigVO) throws Exception {

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
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
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
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
			System.out.println(e);
			logger.error("Failed during Click action.");
			screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, fetchConfigVO);
			throw e;
		}
	}

	public void clickButtonDropdown(WebDriver driver, String param1, String param2, String keysToSend,
			FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
		try {
//Changed == to equals method
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@title='" + param1 + "']")));
				WebElement waittext = driver.findElement(By.xpath("//a[@title='" + param1 + "']"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				clickButtonDropdownText(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO);
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
//Changed == to equals method			
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("//h1[contains(text(),'" + param1 + "')]/following::a[1]")));
				WebElement waittext = driver
						.findElement(By.xpath("//h1[contains(text(),'" + param1 + "')]/following::a[1]"));
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				clickButtonDropdownText(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
			clickButtonDropdownText(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
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
				clickButtonDropdownText(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO);
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[normalize-space(text())='" + param1
					+ "']/following::a[normalize-space(text())='" + param2 + "'])[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//h1[normalize-space(text())='" + param1
					+ "']/following::a[normalize-space(text())='" + param2 + "'])[1]"));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			clickButtonDropdownText(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
			System.out.println(e);
			logger.error("Failed during Click action.");
			screenshotFail(driver, "Failed during clickLink Method", fetchMetadataVO, fetchConfigVO);
			throw e;
		}
	}

	public void clickButtonDropdownText(WebDriver driver, String param1, String keysToSend,
			FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("//li[normalize-space(text())='" + keysToSend + "']")));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath(("//li[normalize-space(text())='" + keysToSend + "']")), keysToSend));
			Thread.sleep(5000);
			WebElement waittext = driver.findElement(By.xpath("//li[normalize-space(text())='" + keysToSend + "']"));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			return;
		} catch (Exception e) {
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
			return;
		} catch (Exception e) {
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
			return;
		} catch (Exception e) {
			System.out.println(e);
			logger.error("Failed during Click action.");
			screenshotFail(driver, "Failed during clickLink Method", fetchMetadataVO, fetchConfigVO);
			throw e;
		}
	}

	public void clickExpandorcollapse(WebDriver driver, String param1, String param2, FetchMetadataVO fetchMetadataVO,
			FetchConfigVO fetchConfigVO) throws Exception {
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
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
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
			} catch (Exception e) {

			}
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			Thread.sleep(4000);
			try {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"(//span[contains(text(),'" + param1 + "')])[1]/preceding::a[3][@title='" + param2 + "'][1]")));
				WebElement waittext = driver.findElement(By.xpath(
						"(//span[contains(text(),'" + param1 + "')])[1]/preceding::a[3][@title='" + param2 + "'][1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(1000);
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			} catch (Exception e) {
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			}
			return;
		} catch (Exception e) {
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
			} catch (Exception e) {

			}
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
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
			} catch (Exception e) {

			}
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
			System.out.println(e);
			logger.error("Failed during Click action.");
			screenshotFail(driver, "Failed during clickExpandorcollapse", fetchMetadataVO, fetchConfigVO);
			throw e;
		}
	}

	public void selectAValue(WebDriver driver, String param1, String param2, String keysToSend,
			FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
		try {
			if (param1.equalsIgnoreCase("Review installments")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"//*[normalize-space(text())='" + keysToSend + "']/following::img[@title='" + param1 + "']")));
				WebElement waittext = driver.findElement(By.xpath(
						"//*[normalize-space(text())='" + keysToSend + "']/following::img[@title='" + param1 + "']"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(2000);
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(),'" + param1
					+ "')]/following::*[normalize-space(text())='" + keysToSend + "'][1]")));
			WebElement waittext = driver.findElement(By.xpath("//*[contains(text(),'" + param1
					+ "')]/following::*[normalize-space(text())='" + keysToSend + "'][1]"));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(2000);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
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
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
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
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
			System.out.println(e);
			logger.error("Failed during Click action.");
			screenshotFail(driver, "Failed during clickExpandorcollapse", fetchMetadataVO, fetchConfigVO);
			throw e;
		}
	}

	public String clickTableImage(WebDriver driver, String param1, String param2, String keysToSend,
			FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
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
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			clickValidateXpath(driver, fetchMetadataVO, waittill, fetchConfigVO);
			Thread.sleep(500);
			return keysToSend;
		} catch (Exception e) {
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
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			clickValidateXpath(driver, fetchMetadataVO, waittill, fetchConfigVO);
			Thread.sleep(500);
			return keysToSend;
		} catch (Exception e) {
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
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			clickValidateXpath(driver, fetchMetadataVO, waittill, fetchConfigVO);
			Thread.sleep(500);
			return keysToSend;
		} catch (Exception e) {
			System.out.println(e);
			logger.error("Failed during Click action.");
			screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, fetchConfigVO);
			throw e;
		}
	}

	public void clickImage(WebDriver driver, String param1, String param2, FetchMetadataVO fetchMetadataVO,
			FetchConfigVO fetchConfigVO) throws Exception {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
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
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
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
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
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
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
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
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
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
			return;
		} catch (Exception e) {
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
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
			System.out.println(e);
			logger.error("Failed during Click action.");
			screenshotFail(driver, "Failed during click Image Method", fetchMetadataVO, fetchConfigVO);
			throw e;
		}
	}

	public void clickButton(WebDriver driver, String param1, String param2, FetchMetadataVO fetchMetadataVO,
			FetchConfigVO fetchConfigVO) throws Exception {
		try {
			if (param1.equalsIgnoreCase("Edit Line")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(8000);
				System.out.println("here1234");
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						("//*[contains(text(),'" + param1 + "')]/following::span[normalize-space(text())='K']"))));
				WebElement waittext = driver.findElement(By.xpath(
						("//*[contains(text(),'" + param1 + "')]/following::span[normalize-space(text())='K']")));
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				Thread.sleep(4000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(4000);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Members") || param1.equalsIgnoreCase("Complete Report")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(
						ExpectedConditions.presenceOfElementLocated(By.xpath(("//button[@title='" + param2 + "']"))));
				WebElement waittext = driver.findElement(By.xpath(("//button[@title='" + param2 + "']")));
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(15000);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Address Contacts") && param2.equalsIgnoreCase("OK")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"//*[normalize-space(text())='" + param1 + "']/following::button[@title='" + param2 + "']")));
				WebElement waittext = driver.findElement(By.xpath(
						"//*[normalize-space(text())='" + param1 + "']/following::button[@title='" + param2 + "']"));
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(5000);
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(4000);
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				Thread.sleep(4000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(4000);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			if (param2.equalsIgnoreCase("OK")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(8000);
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//[contains(text(),'" + param1 + "')]/following::span[text()='K']"))));
				WebElement waittext = driver
						.findElement(By.xpath(("//[contains(text(),'" + param1 + "')]/following::span[text()='K']")));
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(4000);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		try {

			if (param2.equalsIgnoreCase("Select")) {
				Thread.sleep(2000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("(//input[contains(@value,'" + param1
						+ "') and (@type)]/following::button[contains(text(),'" + param2 + "')])[1]"))));
				WebElement waittext = driver.findElement(By.xpath(("(//input[contains(@value,'" + param1
						+ "') and (@type)]/following::button[contains(text(),'" + param2 + "')])[1]")));
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(4000);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			if (param2.equalsIgnoreCase("Done")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(8000);
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath(("//*[contains(@id,'tAccountPopup::content')]//*[text()='o']"))));
				WebElement waittext = driver
						.findElement(By.xpath(("//*[contains(@id,'tAccountPopup::content')]//*[text()='o']")));
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				return;
			} else if (param1.equalsIgnoreCase("Submit")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[text()='m']"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()='m']"), "m"));
				Thread.sleep(20000);
				WebElement waittext = driver.findElement(By.xpath(("//span[text()='m']")));
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(3000);
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(3000);
				return;
			} else if (param1.equalsIgnoreCase("Distributions")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='istributions']")));
				WebElement waittext = driver.findElement(By.xpath("//span[text()='istributions']"));
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(3000);
				return;
			} else if (param1.equalsIgnoreCase("Manage Holds") && param2.equalsIgnoreCase("Save and Close")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						("//*[normalize-space(text())='" + param1 + "']/following::button[text()='Save and Close']"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(
						"//*[normalize-space(text())='" + param1 + "']/following::button[text()='Save and Close']"),
						"Save and Close"));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath(
						("//*[normalize-space(text())='" + param1 + "']/following::button[text()='Save and Close']")));
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(3000);
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(3000);
				return;
			} else if (param1.equalsIgnoreCase("Next")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[text()='x']"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()='x']"), "x"));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath(("//span[text()='x']")));
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(8000);
				return;
			} else if (param2.equalsIgnoreCase("Next")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1
						+ "']/following::*[normalize-space(text())='" + param2 + "']")));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[normalize-space(text())='"
						+ param1 + "']/following::*[normalize-space(text())='" + param2 + "']"), "x"));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1
						+ "']/following::*[normalize-space(text())='" + param2 + "']"));
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(8000);
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(6000);
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(4000);
				return;
			} else if (param1.equalsIgnoreCase("Save and Close")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[text()='S']"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()='S']"), "S"));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath(("//span[text()='S']")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				return;
			} else if (param1.equalsIgnoreCase("Continue")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[text()='u']"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()='u']"), "u"));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath(("//span[text()='u']")));
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				return;
			} else if (param2.equalsIgnoreCase("Continue")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//button[text()='Contin']"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//button[text()='Contin']"),
						"Contin"));
				WebElement waittext = driver.findElement(By.xpath(("//button[text()='Contin']")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				return;
			} else if (param1.equalsIgnoreCase("Close")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//button[text()='Cl']"))));
				Thread.sleep(5000);
				WebElement waittext = driver.findElement(By.xpath(("//button[text()='Cl']")));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			} else if (param1.equalsIgnoreCase("Cancel")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='C']")));
				Thread.sleep(5000);
				WebElement waittext = driver.findElement(By.xpath("//span[text()='C']"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				return;
			} else if (param1.equalsIgnoreCase("Save")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[text()='ave']"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()='ave']"), "ave"));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath(("//span[text()='ave']")));
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				return;
			} else if (param1.equalsIgnoreCase("Apply")) {
				Thread.sleep(8000);
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[text()='l']"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()='l']"), "l"));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath(("//span[text()='l']")));
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				Thread.sleep(2000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(4000);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Add Application")) {
				try {
					WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
					wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[text()='A']"))));
					wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()='A']"), "A"));
					Thread.sleep(4000);
					WebElement waittext = driver.findElement(By.xpath(("//span[text()='A']")));
					screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
					Actions actions = new Actions(driver);
					actions.moveToElement(waittext).build().perform();
					clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				} catch (Exception e) {
					WebElement expand = driver
							.findElement(By.xpath(("//a[text()='Application']/following::div[@role='button'][2]")));
					expand.click();
					Thread.sleep(2000);
					WebElement waittext = driver.findElement(By.xpath(("//span[text()='A']")));
					screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
					Actions actions = new Actions(driver);
					actions.moveToElement(waittext).build().perform();
					clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				}
				return;
			}
		} catch (Exception e) {
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
					screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
					Actions actions = new Actions(driver);
					actions.moveToElement(waittext).build().perform();
					clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
					Thread.sleep(4000);
				} catch (Exception e) {
					WebElement expand = driver
							.findElement(By.xpath(("//a[text()='Application']/following::div[@role='button'][2]")));
					expand.click();
					Thread.sleep(2000);
					WebElement waittext = driver.findElement(By.xpath(("//button[text()='" + param1 + "']")));
					screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
					Actions actions = new Actions(driver);
					actions.moveToElement(waittext).build().perform();
					clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				}
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(3000);
				return;
			}
		} catch (Exception e) {
			// TODO: handle exception
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Thread.sleep(1000);
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				// clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				waittext.click();
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(3000);
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(3000);
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(3000);
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(3000);
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(3000);
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(3000);
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(3000);
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Thread.sleep(5000);
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())='" + param1
					+ "']/following::*[normalize-space(text())='" + param2 + "' and not(@_afrpdo)])[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())='" + param1
					+ "']/following::*[normalize-space(text())='" + param2 + "' and not(@_afrpdo)])[1]"));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(1000);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
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
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
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
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
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
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
			System.out.println(e);
			logger.error("Failed during Click action.");
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())='" + param1
					+ "']/following::*[normalize-space(text())='" + param2 + "'])[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())='" + param1
					+ "']/following::*[normalize-space(text())='" + param2 + "'])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			Thread.sleep(1000);
			return;
		} catch (Exception e) {
			System.out.println(e);
			screenshotFail(driver, "Failed during clickLink Method", fetchMetadataVO, fetchConfigVO);
			throw e;
		}
	}

	public void clickTableLink(WebDriver driver, String param1, String param2, FetchMetadataVO fetchMetadataVO,
			FetchConfigVO fetchConfigVO) throws Exception {

		try {
			if (param1.equalsIgnoreCase("Manage Journals") || param1.equalsIgnoreCase("Journal Lines")
					|| param1.equalsIgnoreCase("Manage Transactions") || param1.equalsIgnoreCase("Manage Receipts")
					|| param1.equalsIgnoreCase("Prepare Source lines")) {
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
				return;
			}
		} catch (Exception e) {
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
				return;
			}
		} catch (Exception e) {
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
				return;
			}
		} catch (Exception e) {
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
				return;
			}
		} catch (Exception e) {
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
				tab(driver, fetchMetadataVO, fetchConfigVO);
				enter(driver, fetchMetadataVO, fetchConfigVO);
				Thread.sleep(2000);
				return;
			}
		} catch (Exception e) {
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
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			// Changed == to equals method
			if (param2.equals("")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("(//table[@summary='" + param1 + "']//a)[1]")));
				Thread.sleep(4000);
				WebElement waittext = driver.findElement(By.xpath("(//table[@summary='" + param1 + "']//a)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(2000);
				return;
			}
		} catch (Exception e) {
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
			return;
		} catch (Exception e) {
			System.out.println(e);
			logger.error("Failed during Click action.");
			screenshotFail(driver, "Failed during clickLink Method", fetchMetadataVO, fetchConfigVO);
			throw e;
		}

	}

	public void tableRowSelect(WebDriver driver, String param1, String param2, FetchMetadataVO fetchMetadataVO,
			FetchConfigVO fetchConfigVO) throws Exception {
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
				return;
			}
		} catch (Exception e) {
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
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			Thread.sleep(6000);
			System.out.println("Here1 came");
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("(//table[@summary='" + param1 + "']//td)[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//table[@summary='" + param1 + "']//td)[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			waittext.click();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(10000);
			return;
		} catch (Exception e) {
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
			return;
		} catch (Exception e) {
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
			return;
		} catch (Exception e) {
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
			return;
		} catch (Exception e) {
			System.out.println(e);
			logger.error("Failed during Click action.");
			screenshotFail(driver, "Failed during clickLink Method", fetchMetadataVO, fetchConfigVO);
			throw e;
		}
	}

	public void clickLink(WebDriver driver, String param1, String param2, FetchMetadataVO fetchMetadataVO,
			FetchConfigVO fetchConfigVO) throws Exception {
		try {
			if (param1.equalsIgnoreCase("Report") && param2.equalsIgnoreCase("Apply")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@value='" + param2 + "']")));
				WebElement waittext = driver.findElement(By.xpath("//input[@value='" + param2 + "']"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				Thread.sleep(6000);
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				refreshPage(driver, fetchMetadataVO, fetchConfigVO);
				Thread.sleep(5000);
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				refreshPage(driver, fetchMetadataVO, fetchConfigVO);
				Thread.sleep(5000);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Thread.sleep(5000);
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				refreshPage(driver, fetchMetadataVO, fetchConfigVO);
				Thread.sleep(5000);
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Thread.sleep(5000);
				return;
			}
		} catch (Exception e) {
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
					screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
					// refreshPage(driver, fetchMetadataVO, fetchConfigVO);
					Thread.sleep(5000);
				} catch (Exception e) {
					WebElement expand = driver
							.findElement(By.xpath(("//span[text()='Allocate']/following::div[@role='button'][2]")));
					expand.click();
					Thread.sleep(2000);
					WebElement waittext = driver
							.findElement(By.xpath(("//a[normalize-space(text())='" + param1 + "']")));
					screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
					Actions actions = new Actions(driver);
					actions.moveToElement(waittext).build().perform();
					waittext.click();
				}
				return;
			}

		} catch (Exception e) {
			System.out.println(e);
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Thread.sleep(30000);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Thread.sleep(30000);
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Thread.sleep(5000);
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
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
					screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
					return;
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Attachment") || param1.equalsIgnoreCase("Invoice Summary")) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
			System.out.println(e);
			logger.error("Failed during Click action.");
			screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, fetchConfigVO);
			throw e;
		}
	}

	public void clickRadiobutton(WebDriver driver, String param1, String param2, String keysToSend,
			FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
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
			tab(driver, fetchMetadataVO, fetchConfigVO);
			Thread.sleep(500);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("(//*[normalize-space(text())='" + param1
					+ "']/following::label[normalize-space(text())='" + keysToSend + "'])[1]"))));
			WebElement waittext = driver.findElement(By.xpath(("(//*[normalize-space(text())='" + param1
					+ "']/following::label[normalize-space(text())='" + keysToSend + "'])[1]")));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			tab(driver, fetchMetadataVO, fetchConfigVO);
			Thread.sleep(500);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
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
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
			System.out.println(e);
			logger.error("Failed during Click action.");
			screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, fetchConfigVO);
			throw e;
		}
	}

	public void clickCheckbox(WebDriver driver, String param1, String keysToSend, FetchMetadataVO fetchMetadataVO,
			FetchConfigVO fetchConfigVO) throws Exception {
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
				tab(driver, fetchMetadataVO, fetchConfigVO);
				Thread.sleep(500);
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
				tab(driver, fetchMetadataVO, fetchConfigVO);
				Thread.sleep(500);
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
				// tab(driver, fetchMetadataVO, fetchConfigVO);
				Thread.sleep(500);
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
				// tab(driver, fetchMetadataVO, fetchConfigVO);
				Thread.sleep(500);
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
				// tab(driver, fetchMetadataVO, fetchConfigVO);
				Thread.sleep(500);
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Match Invoice Lines")) {
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
				tab(driver, fetchMetadataVO, fetchConfigVO);
				Thread.sleep(500);
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
			tab(driver, fetchMetadataVO, fetchConfigVO);
			Thread.sleep(500);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
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
			tab(driver, fetchMetadataVO, fetchConfigVO);
			Thread.sleep(500);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
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
			tab(driver, fetchMetadataVO, fetchConfigVO);
			Thread.sleep(500);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
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
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
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
			tab(driver, fetchMetadataVO, fetchConfigVO);
			Thread.sleep(500);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
			System.out.println(e);
			logger.error("Failed during Click action.");
			screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, fetchConfigVO);
			throw e;
		}
	}

	public void clickLinkAction(WebDriver driver, String param1, String param2, String keysToSend,
			FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {

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
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
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
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
			System.out.println(e);
			logger.error("Failed during Click action.");
			screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, fetchConfigVO);
			throw e;
		}
	}

	public String textarea(WebDriver driver, String param1, String param2, String keysToSend,
			FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
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
			return keysToSend;
		} catch (Exception e) {
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
			return keysToSend;
		} catch (Exception e) {
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
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			Thread.sleep(500);
			return keysToSend;
		} catch (Exception e) {
			System.out.println(e);
			logger.error("Failed during Click action.");
			screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, fetchConfigVO);
			throw e;
		}
	}

	public String sendValue(WebDriver driver, String param1, String param2, String keysToSend,
			FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
		try {
			if (param1.equalsIgnoreCase("Password")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='" + param1 + "']")));
				WebElement waittill = driver.findElement(By.xpath("//input[@type='" + param1 + "']"));
				JavascriptExecutor jse = (JavascriptExecutor) driver;
				jse.executeScript("document.getElementById('password').value = '" + keysToSend + "';");
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Thread.sleep(1000);
				return keysToSend;
			}
		} catch (Exception e) {
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
				return keysToSend;
			}
		} catch (Exception e) {
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
				return keysToSend;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
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
				enter(driver, fetchMetadataVO, fetchConfigVO);
				Thread.sleep(10000);
				return keysToSend;
			}
		} catch (Exception e) {
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
				return keysToSend;
			}
		} catch (Exception e) {
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
				}

				return keysToSend;
			}
		} catch (Exception e) {
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
				return keysToSend;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Thread.sleep(1000);
				return keysToSend;
			}
		} catch (Exception e) {
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
				return keysToSend;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Thread.sleep(1000);
				return keysToSend;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Thread.sleep(1000);
				return keysToSend;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
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
			return keysToSend;
		} catch (Exception e) {
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
			return keysToSend;
		} catch (Exception e) {
			System.out.println(e);
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Thread.sleep(1000);
				return keysToSend;
			}
		} catch (Exception e) {
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
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			Thread.sleep(5000);
			return keysToSend;
		} catch (Exception e) {
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
			return keysToSend;
		} catch (Exception e) {
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
			return keysToSend;
		} catch (Exception e) {
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
			return keysToSend;
		} catch (Exception e) {
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
			return keysToSend;
		} catch (Exception e) {
			System.out.println(e);
			logger.error("Failed during Click action.");
			screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, fetchConfigVO);
			throw e;
		}
	}

	public void dropdownTexts(WebDriver driver, String param1, String param2, String keysToSend,
			FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
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

					enter(driver, fetchMetadataVO, fetchConfigVO);

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

				}

				return;

			}

		} catch (Exception e) {

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
			return;
		} catch (Exception e) {
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
			return;
		} catch (Exception e) {
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
				enter(driver, fetchMetadataVO, fetchConfigVO);
				Thread.sleep(5000);
				try {
					WebElement text = driver.findElement(By.xpath(
							"//div[@class='AFDetectExpansion']/following::span[text()='Name']/following::span[normalize-space(text())='"
									+ keysToSend + "']"));
					text.click();
				} catch (Exception e) {
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
			} catch (Exception e) {
				WebElement button = driver
						.findElement(By.xpath("//*[text()='Search']/following::*[normalize-space(text())='" + param2
								+ "']/following::*[not (@aria-disabled) and text()='OK'][1]"));
				button.click();
			}

			return;
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"//div[contains(@id,'PopupId::content')]/following::*[normalize-space(text())='Search']/following::*[text()='Name']/following::input[@type='text'][1]")));
			WebElement searchResult = driver.findElement(By.xpath(
					"//div[contains(@id,'PopupId::content')]/following::*[normalize-space(text())='Search']/following::*[text()='Name']/following::input[@type='text'][1]"));
			typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
			enter(driver, fetchMetadataVO, fetchConfigVO);
			Thread.sleep(5000);
			WebElement text = driver.findElement(By.xpath("(//span[contains(text(),'" + keysToSend + "')])[1]"));
			text.click();
			Thread.sleep(1000);
			WebElement button = driver.findElement(
					By.xpath("//*[text()='Search']/following::*[text()='Name']/following::*[text()='OK'][1]"));
			button.click();
			return;
		} catch (Exception e) {
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
		} catch (Exception e) {
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
			try {
				WebElement searchResult = driver.findElement(
						By.xpath("//div[contains(@id,'PopupId::content')]//*[text()='Search']/following::*[text()='"
								+ param2 + "']/following::input[1]"));
				typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
			} catch (Exception e) {
				WebElement searchResult = driver.findElement(By.xpath(
						"//div[contains(@id,'PopupId::content')]//*[text()='Search']/following::*[text()='Name']/following::input[1]"));
				typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
			}
			if (keysToSend != null) {
				enter(driver, fetchMetadataVO, fetchConfigVO);
				Thread.sleep(5000);
				try {
					WebElement text = driver.findElement(By.xpath(
							"//div[@class='AFDetectExpansion']/following::span[text()='Name']/following::span[text()='"
									+ keysToSend + "']"));
					text.click();
				} catch (Exception e) {
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
			} catch (Exception e) {
				WebElement button = driver
						.findElement(By.xpath("//*[text()='Search']/following::*[normalize-space(text())='" + param2
								+ "']/following::*[not (@aria-disabled) and text()='OK'][1]"));
				button.click();
			}

			return;
		} catch (Exception e) {
			System.out.println(e);
			logger.error("Failed during Click action.");
			screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, fetchConfigVO);
			throw e;
		}
	}

	public void tableSendKeys(WebDriver driver, String param1, String param2, String param3, String keysToSend,
			FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
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
					screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				} catch (Exception e) {
					WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
					WebElement waittill = driver
							.findElement(By.xpath("//label[text()='" + param1 + "']/preceding-sibling::input[1]"));
					Actions actions = new Actions(driver);
					actions.moveToElement(waittill).build().perform();
					typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
					screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				}
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath(
					"//h1[text()='" + param1 + "']/following::*[text()='" + param2 + "']/preceding-sibling::input[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath(
					"//h1[text()='" + param1 + "']/following::label[text()='" + param2 + "']/preceding::input[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			Thread.sleep(5000);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("(//*[text()=\"" + param1
					+ "\"]/following::label[text()='" + param2 + "']/preceding-sibling::input)[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("(//*[text()=\"" + param1
					+ "\"]/following::label[text()='" + param2 + "']/preceding-sibling::textarea)[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			WebElement waittill = driver
					.findElement(By.xpath("//*[text()=\"" + param1 + "\"]/following::table[@summary='" + param2
							+ "']//*[text()='" + param3 + "']/following::input[contains(@id,'NewBdgtPctLst')][1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			// tab(driver, fetchMetadataVO, fetchConfigVO);
			// Thread.sleep(1000);
			// enter(driver, fetchMetadataVO, fetchConfigVO);
			WebElement waittill = driver.findElement(By.xpath("(//table[@summary='" + param1 + "']//label[text()='"
					+ param2 + "']/preceding-sibling::input)[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
			System.out.println(e);
			logger.error("Failed during Click action.");
			screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, fetchConfigVO);
			throw e;
		}
	}

	public void tableDropdownTexts(WebDriver driver, String param1, String param2, String keysToSend,
			FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath("//div[@class='AFDetectExpansion']/following::*[text()='" + keysToSend + "']"),
					keysToSend));
			WebElement waittext = driver.findElement(
					By.xpath("//div[@class='AFDetectExpansion']/following::*[text()='" + keysToSend + "']"));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			return;
		} catch (Exception e) {
			// TODO: handle exception
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
			return;
		} catch (Exception e) {
			System.out.println(e);
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
			return;
		} catch (Exception e) {
			System.out.println(e);
			logger.error("Failed during Click action.");
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
			return;
		} catch (Exception e) {
			System.out.println(e);
			logger.error("Failed during Click action.");
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
				enter(driver, fetchMetadataVO, fetchConfigVO);
				Thread.sleep(5000);
				WebElement text = driver.findElement(By.xpath("(//span[contains(text(),'" + keysToSend + "')])[1]"));
				text.click();
			}
			try {
				WebElement button = driver.findElement(By.xpath(
						"//*[text()='Search']/following::*[text()='" + param2 + "']/following::*[text()='K'][1]"));
				button.click();
			} catch (Exception e) {
				WebElement button = driver.findElement(By.xpath(
						"//*[text()='Search']/following::*[text()='" + param2 + "']/following::*[text()='OK'][1]"));
				button.click();
			}
			return;
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			try {
				WebElement searchResult = driver
						.findElement(By.xpath("//*[text()='Search']/following::*[text()='Name']/following::input[1]"));
				typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
				enter(driver, fetchMetadataVO, fetchConfigVO);
				Thread.sleep(5000);
			} catch (Exception e) {
				WebElement searchResult = driver
						.findElement(By.xpath("//*[text()='Search']/following::*[text()='Value']/following::input[1]"));
				typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
				enter(driver, fetchMetadataVO, fetchConfigVO);
				Thread.sleep(5000);
			}

			WebElement text = driver.findElement(By.xpath("(//span[contains(text(),'" + keysToSend + "')])[1]"));
			text.click();
			Thread.sleep(1000);
			try {
				WebElement button = driver.findElement(
						By.xpath("//*[text()='Search']/following::*[text()='Name']/following::*[text()='OK'][1]"));
				button.click();
			} catch (Exception e) {
				WebElement button = driver.findElement(
						By.xpath("//*[text()='Search']/following::*[text()='Value']/following::*[text()='OK'][1]"));
				button.click();
			}

			return;
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			WebElement button = driver
					.findElement(By.xpath("//*[text()='Search']/following::*[normalize-space(text())='" + param2
							+ "']/following::*[text()='OK'][1]"));
			button.click();
			return;
		} catch (Exception e) {
			System.out.println(e);
			screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, fetchConfigVO);
			throw e;
		}
	}

	public void tableDropdownValues(WebDriver driver, String param1, String param2, String keysToSend,
			FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("(//*[text()='" + param1 + "']/following::a[contains(@id,'" + param2 + "')])[1]")));
			WebElement waittext = driver.findElement(
					By.xpath("(//*[text()='" + param1 + "']/following::a[contains(@id,'" + param2 + "')])[1]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			tableDropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
			System.out.println(e);
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
				tableDropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
				Thread.sleep(3000);
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
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
			tableDropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
			Thread.sleep(3000);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
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
			tableDropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
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
			tableDropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
			System.out.println(e);
			logger.error("Failed during Click action.");
			screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, fetchConfigVO);
			throw e;
		}
	}

	public void dropdownValues(WebDriver driver, String param1, String param2, String param3, String keysToSend,
			FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
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
				enter(driver, fetchMetadataVO, fetchConfigVO);
				WebElement select = driver
						.findElement(By.xpath("//div[@class='AFDetectExpansion']/following::span[starts-with(text(),'"
								+ keysToSend + "')][1]"));
				clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
				WebElement searchok = driver
						.findElement(By.xpath("//div[@class='AFDetectExpansion']/following::span[contains(text(),'"
								+ keysToSend + "')][1]/following::button[text()='OK'][1]"));
				clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Thread.sleep(5000);
				WebElement ok = driver.findElement(By.xpath("//div[@class='AFDetectExpansion']/following::*[text()='"
						+ param1 + "']/following::*[normalize-space(text())='" + param2
						+ "']/following::a[1]/following::button[text()='OK']"));
				ok.click();
				Thread.sleep(6000);
				return;
			}
		} catch (Exception e) {
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
					enter(driver, fetchMetadataVO, fetchConfigVO);
					Thread.sleep(5000);
					WebElement text = driver.findElement(By.xpath(
							"//span[text()='Name']/following::span[normalize-space(text())='" + keysToSend + "']"));
					text.click();
					Thread.sleep(1000);
					WebElement button = driver.findElement(By.xpath(
							"//*[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::*[text()='OK'][1]"));
					button.click();

				} catch (Exception e) {
					System.out.println(e);
				}
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
				enter(driver, fetchMetadataVO, fetchConfigVO);
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
				enter(driver, fetchMetadataVO, fetchConfigVO);
				Thread.sleep(5000);
				WebElement text = driver.findElement(By
						.xpath("//span[text()='Name']/following::span[normalize-space(text())='" + keysToSend + "']"));
				text.click();
				Thread.sleep(5000);
				WebElement button = driver.findElement(By.xpath(
						"//button[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::button[text()='OK'][1]"));
				button.click();
				return;
			}
		} catch (Exception ex) {
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
				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);

				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);

				return;

			}

		} catch (Exception e) {

			System.out.println(e);

		}
		try {
			if (param1.equalsIgnoreCase("Create Request") || param2.equalsIgnoreCase("CIP Budget Code")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[contains(text(),'" + param1
						+ "')]/following::label[normalize-space(text())='" + param2 + "']/following::a)[1]")));
				WebElement waittext = driver.findElement(By.xpath("(//*[contains(text(),'" + param1
						+ "')]/following::label[normalize-space(text())='" + param2 + "']/following::a)[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
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
				return;
			}
		} catch (Exception e) {
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
					enter(driver, fetchMetadataVO, fetchConfigVO);
					WebElement select = driver.findElement(By
							.xpath("//*[text()='Name']/following::div[normalize-space(text())='" + keysToSend + "']"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
					WebElement searchok = driver
							.findElement(By.xpath("//span[text()='Name']/following::button[text()='OK']"));
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);
					screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
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
					enter(driver, fetchMetadataVO, fetchConfigVO);
					WebElement select = driver
							.findElement(By.xpath("//*[text()='Name']/following::div[text()='" + keysToSend + "']"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
					WebElement searchok = driver
							.findElement(By.xpath("//span[text()='Name']/following::button[text()='OK'][2]"));
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);
					screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
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
						enter(driver, fetchMetadataVO, fetchConfigVO);
						WebElement select = driver.findElement(By.xpath(
								"//*[text()='Name']/following::div[normalize-space(text())='" + keysToSend + "']"));
						clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
						WebElement searchok = driver
								.findElement(By.xpath("//span[text()='Name']/following::button[text()='OK'][3]"));
						clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);
						screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
						return;
					}
				}
			}
		} catch (Exception e) {
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
					enter(driver, fetchMetadataVO, fetchConfigVO);
					WebElement select = driver.findElement(By
							.xpath("//*[text()='Name']/following::div[normalize-space(text())='" + keysToSend + "']"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
					WebElement searchok = driver
							.findElement(By.xpath("//span[text()='Name']/following::button[text()='OK']"));
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);
					screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
					return;
				} else if (param2.equalsIgnoreCase("Legal Entity")) {
					WebElement search = driver
							.findElement(By.xpath("//div[@class='listbox']//span[contains(text(),'Search')]"));
					clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO);
					Thread.sleep(1000);
					WebElement values = driver.findElement(By.xpath(
							"//div[@class='masterDialog modalDialog']/following::span[text()='Name']/following::input[1]"));
					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO);
					Thread.sleep(6000);
					WebElement select = driver.findElement(By
							.xpath("//*[text()='Name']/following::div[normalize-space(text())='" + keysToSend + "']"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
					WebElement searchok = driver.findElement(By.xpath(
							"//div[@class='masterDialog modalDialog']/following::span[text()='Name']/following::button[text()='OK']"));
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);
					screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
					return;
				}
			}

		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("FIN-7056-Generate Customer Statements")
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
					enter(driver, fetchMetadataVO, fetchConfigVO);
					WebElement select = driver.findElement(By
							.xpath("//*[text()='Value']/following::div[normalize-space(text())='" + keysToSend + "']"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
					WebElement searchok = driver
							.findElement(By.xpath("//div[text()='Search']/following::button[text()='OK']"));
					actions.moveToElement(searchok).build().perform();
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);
					screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
					return;
				} else if (param2.equalsIgnoreCase("Customer Name")) {
					WebElement search1 = driver.findElement(
							By.xpath("//a[contains(@id,'CUSTOMER_NAME')][1]//span/span[contains(text(),'Search')]"));
					clickValidateXpath(driver, fetchMetadataVO, search1, fetchConfigVO);
					Thread.sleep(1000);
					WebElement values = driver.findElement(By.xpath(
							"//div[@class='masterDialog modalDialog']/following::span[text()='Name']/following::input[@type='text'][1]"));
					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO);
					WebElement select = driver.findElement(
							By.xpath("//*[text()='Value']/following::div[contains(text(),'" + keysToSend + "')]"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
					Thread.sleep(1000);
					WebElement searchok = driver
							.findElement(By.xpath("//div[text()='Search']/following::button[text()='OK'][2]"));
					actions.moveToElement(searchok).build().perform();
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);
					screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
					return;
				}

			}

		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			if (param1.equalsIgnoreCase("Receivables to Ledger Reconciliation")
					&& (param2.equalsIgnoreCase("Ledger") || param2.equalsIgnoreCase("Request Name"))) {
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
				if (param2.equalsIgnoreCase("Ledger")) {
					WebElement search = driver.findElement(
							By.xpath("//div[@class='floatingWindowDiv']//span[contains(text(),'Search')]"));
					// clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO);
					search.click();
					Thread.sleep(1000);
					WebElement values = driver.findElement(By.xpath("(//span[text()='Name']/following::input)[1]"));
					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO);
					WebElement select = driver.findElement(By
							.xpath("//*[text()='Name']/following::span[normalize-space(text())='" + keysToSend + "']"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
					WebElement searchok = driver
							.findElement(By.xpath("//span[text()='Name']/following::a[text()='OK']"));
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);
					// searchok.click();
					screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
					return;
				} else if (param2.equalsIgnoreCase("Request Name")) {
					WebElement search = driver.findElement(
							By.xpath("//div[@class='floatingWindowDiv']//span[contains(text(),'Search')]"));
					// clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO);
					search.click();
					Thread.sleep(1000);
					WebElement values = driver.findElement(By.xpath("(//span[text()='Name']/following::input)[1]"));
					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO);
					Thread.sleep(6000);
					WebElement select = driver.findElement(By
							.xpath("//*[text()='Name']/following::span[normalize-space(text())='" + keysToSend + "']"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
					WebElement searchok = driver
							.findElement(By.xpath("//span[text()='Name']/following::a[text()='OK']"));
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);
					screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
					return;
				}

			}

		} catch (Exception e) {
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
				enter(driver, fetchMetadataVO, fetchConfigVO);
				WebElement select = driver.findElement(
						By.xpath("//b[text()='Value']/following::div[normalize-space(text())='" + keysToSend + "']"));
				clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
				WebElement searchok = driver
						.findElement(By.xpath("//div[text()='Search']/following::button[text()='OK']"));
				clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);
				Thread.sleep(10000);
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Thread.sleep(15000);
				return;
			}

		} catch (Exception e) {
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
					enter(driver, fetchMetadataVO, fetchConfigVO);
					WebElement select = driver.findElement(By
							.xpath("//*[text()='Value']/following::div[normalize-space(text())='" + keysToSend + "']"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
					WebElement searchok = driver
							.findElement(By.xpath("//div[text()='Search']/following::button[text()='OK']"));
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);
					screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
					return;
				} else if (param2.equalsIgnoreCase("Ledger")) {
					Thread.sleep(1000);
					WebElement search = driver
							.findElement(By.xpath("//a[contains(@id,'REQ')][1]//span/span[contains(text(),'Search')]"));
					clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO);
					WebElement values = driver
							.findElement(By.xpath("(//span[text()='Name']/following::input[@type='text'])[2]"));
					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO);
					WebElement select = driver.findElement(By.xpath(
							"//*[text()='Value']/following::div[normalize-space(text())='" + keysToSend + "'][2]"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
					WebElement searchok = driver
							.findElement(By.xpath("//div[text()='Search']/following::button[text()='OK'][2]"));
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);
					screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
					return;
				} else if (param2.equalsIgnoreCase("Request Name")) {
					Thread.sleep(1000);
					WebElement search = driver
							.findElement(By.xpath("//a[contains(@id,'REQ')][1]//span/span[contains(text(),'Search')]"));
					clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO);
					WebElement values = driver
							.findElement(By.xpath("(//span[text()='Name']/following::input[@type='text'])[2]"));
					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO);
					WebElement select = driver.findElement(By.xpath(
							"//*[text()='Value']/following::div[normalize-space(text())='" + keysToSend + "'][2]"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
					WebElement searchok = driver
							.findElement(By.xpath("//div[text()='Search']/following::button[text()='OK'][2]"));
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);
					screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
					return;
				} else if (param2.equalsIgnoreCase("Requistion Business Unit")) {
					Thread.sleep(1000);
					WebElement search = driver
							.findElement(By.xpath("//a[contains(@id,'REQ')][1]//span/span[contains(text(),'Search')]"));
					clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO);
					WebElement values = driver
							.findElement(By.xpath("(//span[text()='Name']/following::input[@type='text'])[2]"));
					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO);
					WebElement select = driver.findElement(By.xpath(
							"//*[text()='Value']/following::div[normalize-space(text())='" + keysToSend + "'][2]"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
					WebElement searchok = driver
							.findElement(By.xpath("//div[text()='Search']/following::button[text()='OK'][2]"));
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);
					screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
					return;
				} else if (param2.equalsIgnoreCase("Status")) {
					WebElement search = driver.findElement(
							By.xpath("//a[contains(@id,'STATUS')][1]//span/span[contains(text(),'Search')]"));
					clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO);
					WebElement values = driver
							.findElement(By.xpath("(//span[text()='Name']/following::input[@type='text'])[3]"));
					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO);
					WebElement select = driver.findElement(By.xpath(
							"//*[text()='Value']/following::div[normalize-space(text())='" + keysToSend + "'][1]"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
					WebElement searchok = driver
							.findElement(By.xpath("//div[text()='Search']/following::button[text()='OK'][3]"));
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);
					screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
					return;
				} else if (param2.equalsIgnoreCase("Req. Business Unit") || param2.equalsIgnoreCase("Client BU")) {
					WebElement search = driver.findElement(
							By.xpath("//a[contains(@id,'paramsp')][1]//span/span[contains(text(),'Search')]"));
					clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO);
					WebElement values = driver
							.findElement(By.xpath("(//span[text()='Name']/following::input[@type='text'])[1]"));
					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO);
					WebElement select = driver.findElement(By
							.xpath("//*[text()='Value']/following::div[normalize-space(text())='" + keysToSend + "']"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
					WebElement searchok = driver
							.findElement(By.xpath("//div[text()='Search']/following::button[text()='OK']"));
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);
					screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
					return;
				} else if (param2.equalsIgnoreCase("Legal Entity")) {
					Thread.sleep(2000);
					WebElement search = driver.findElement(
							By.xpath("(//a[contains(@id,'paramsp')][1]//span/span[contains(text(),'Search')])[1]"));
					clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO);
					WebElement values = driver
							.findElement(By.xpath("(//span[text()='Name']/following::input[@type='text'])[1]"));
					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO);
					WebElement select = driver.findElement(By
							.xpath("//*[text()='Value']/following::div[normalize-space(text())='" + keysToSend + "']"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
					WebElement searchok = driver
							.findElement(By.xpath("//div[text()='Search']/following::button[text()='OK']"));
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);
					screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
					return;
				} else if (param2.equalsIgnoreCase("Customer Name")) {
					Thread.sleep(1000);
					WebElement search = driver.findElement(
							By.xpath("(//a[contains(@id,'CUSTOMER')][1]//span/span[contains(text(),'Search')])[1]"));
					clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO);
					WebElement values = driver
							.findElement(By.xpath("(//span[text()='Name']/following::input[@type='text'])[2]"));
					typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
					enter(driver, fetchMetadataVO, fetchConfigVO);
					WebElement select = driver.findElement(By.xpath(
							"//*[text()='Value']/following::div[normalize-space(text())='" + keysToSend + "'][2]"));
					clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
					WebElement searchok = driver
							.findElement(By.xpath("//div[text()='Search']/following::button[text()='OK'][2]"));
					clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);
					screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
					return;
				} else if (param2.equalsIgnoreCase("Business Unit")) {
					if (keysToSend.equalsIgnoreCase("All")) {
						WebElement select = driver.findElement(By.xpath("//span[text()='" + param2
								+ "']/following::div[normalize-space(text())='" + keysToSend + "'][1]"));
						clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
						return;
					} else {
						WebElement search = driver.findElement(
								By.xpath("//a[contains(@id,'BU')][1]//span/span[contains(text(),'Search')]"));
						clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO);
						WebElement values = driver
								.findElement(By.xpath("(//span[text()='Name']/following::input[@type='text'])[1]"));
						typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
						enter(driver, fetchMetadataVO, fetchConfigVO);
						WebElement select = driver.findElement(By.xpath(
								"//*[text()='Value']/following::div[normalize-space(text())='" + keysToSend + "']"));
						clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
						WebElement searchok = driver
								.findElement(By.xpath("//div[text()='Search']/following::button[text()='OK']"));
						clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);
						Thread.sleep(10000);
						screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
						Thread.sleep(3000);
						return;
					}
				} else if (param2.equalsIgnoreCase("Supplier Name")) {
					if (keysToSend.equalsIgnoreCase("All")) {
						WebElement select = driver.findElement(By.xpath("//span[text()='" + param2
								+ "']/following::div[normalize-space(text())='" + keysToSend + "'][1]"));
						clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
						return;
					} else {
						WebElement search = driver.findElement(
								By.xpath("//a[contains(@id,'SUPPLIER')][1]//span/span[contains(text(),'Search')]"));
						clickValidateXpath(driver, fetchMetadataVO, search, fetchConfigVO);
						WebElement values = driver
								.findElement(By.xpath("(//span[text()='Name']/following::input[@type='text'])[1]"));
						typeIntoValidxpath(driver, keysToSend, values, fetchConfigVO, fetchMetadataVO);
						enter(driver, fetchMetadataVO, fetchConfigVO);
						WebElement select = driver.findElement(
								By.xpath("//*[normalize-space(text())='Value']/following::div[normalize-space(text())='"
										+ keysToSend + "']"));
						clickValidateXpath(driver, fetchMetadataVO, select, fetchConfigVO);
						WebElement searchok = driver
								.findElement(By.xpath("//div[text()='Search']/following::button[text()='OK']"));
						clickValidateXpath(driver, fetchMetadataVO, searchok, fetchConfigVO);
						Thread.sleep(10000);
						screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
						Thread.sleep(3000);
						return;
					}
				}
			}
		} catch (Exception e) {
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
					enter(driver, fetchMetadataVO, fetchConfigVO);
					Thread.sleep(5000);
					WebElement text = driver
							.findElement(By.xpath("(//span[contains(text(),'" + keysToSend + "')])[1]"));
					text.click();
					screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
					WebElement button = driver
							.findElement(By.xpath("//*[text()='Search']/following::*[normalize-space(text())='" + param2
									+ "']/following::*[text()='OK'][1]"));
					button.click();
				} catch (Exception e) {
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
					enter(driver, fetchMetadataVO, fetchConfigVO);
					Thread.sleep(5000);
					WebElement text = driver.findElement(By.xpath("//span[text()='" + param2
							+ "']/following::span[contains(text(),'" + keysToSend + "')][1]"));
					text.click();
					screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
					WebElement button = driver
							.findElement(By.xpath("//*[text()='Search']/following::*[normalize-space(text())='" + param2
									+ "']/following::*[text()='OK'][1]"));
					button.click();
				} catch (Exception e) {
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
							enter(driver, fetchMetadataVO, fetchConfigVO);
							Thread.sleep(5000);
							WebElement text = driver
									.findElement(By.xpath("(//span[contains(text(),'" + keysToSend + "')])[1]"));
							text.click();
							screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
							WebElement button = driver.findElement(By.xpath("//*[text()='Search']/following::*[text()='"
									+ param2 + "']/following::*[text()='OK'][1]"));
							button.click();
						}
					}

				}
				return;
			}
		} catch (Exception e) {
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
				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
				actions.release();
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			} catch (Exception ex) {
				try {
					try {
						WebElement popup1 = driver.findElement(By.xpath("//div[@class='AFDetectExpansion']"));
						dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
						actions.release();
						screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
					} catch (Exception ex1) {
						for (int i = 0; i <= 2; i++) {
//if condition added							
							if (i <= 2) {
								actions.click(waittext).build().perform();
								break;
							}
						}
						Thread.sleep(3000);
						WebElement popup1 = driver.findElement(By.xpath("//div[contains(@id,'suggestions-popup')]"));
						dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
						actions.release();
						screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
					}
				} catch (Exception ex2) {
					WebElement popup1 = driver.findElement(By.xpath("//div[@class='AFDetectExpansion']"));
					dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
					actions.release();
					screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				}
			}
			return;
		} catch (Exception exe) {
			System.out.println(exe);
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

				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
				actions.release();
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);

			} catch (Exception e) {
				for (int i = 0; i <= 2; i++) {
					try {
						actions.click(waittext).build().perform();
						break;
					} finally {
						dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
						actions.release();
						screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
					}
				}

			}
			try {
				actions.click(waittext).build().perform();
				Thread.sleep(6000);
				WebElement popup1 = driver.findElement(By.xpath("//div[contains(@id,'dropdownPopup::content')][1]"));
				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
				actions.release();
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			} catch (Exception e) {
				for (int i = 0; i <= 2; i++) {
					try {
						actions.click(waittext).build().perform();
						break;
					} finally {
						dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
						screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
					}
				}

			}
			return;
		} catch (Exception e) {
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
			dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
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
			dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())='" + param1
					+ "']/following::*[normalize-space(text())=\"" + param2 + "\"]/following::a)[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())='" + param1
					+ "']/following::*[normalize-space(text())=\"" + param2 + "\"]/following::a)[1]"));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
			System.out.println(e);
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
				enter(driver, fetchMetadataVO, fetchConfigVO);
				Thread.sleep(5000);
				WebElement text = driver
						.findElement(By.xpath("(//span[normalize-space(text())='" + keysToSend + "'])[1]"));
				text.click();
			}
			WebElement button = driver
					.findElement(By.xpath("//*[normalize-space(text())='Search']/following::*[normalize-space(text())='"
							+ param2 + "']/following::*[text()='K'][1]"));
			button.click();
			return;
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"//*[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::input[1]")));
			WebElement searchResult = driver.findElement(By.xpath(
					"//*[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::input[1]"));
			typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
			enter(driver, fetchMetadataVO, fetchConfigVO);
			Thread.sleep(5000);
			WebElement text = driver.findElement(By.xpath("//span[normalize-space(text())='" + keysToSend + "']"));
			text.click();
			Thread.sleep(1000);
			WebElement button = driver.findElement(By.xpath(
					"//*[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::*[text()='OK'][1]"));
			button.click();
			return;
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			WebElement button = driver
					.findElement(By.xpath("//*[normalize-space(text())='Search']/following::*[normalize-space(text())='"
							+ param2 + "']/following::*[normalize-space(text())='OK'][1]"));
			button.click();
			return;
		} catch (Exception e) {
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
		} catch (Exception e) {
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
			dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
			Thread.sleep(500);
		} catch (Exception e) {
			System.out.println(e);
			logger.error("Failed during Click action.");
			screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, fetchConfigVO);
			throw e;
		}
	}

	private void clickValidateXpath(WebDriver driver, FetchMetadataVO fetchMetadataVO, WebElement waittext,
			FetchConfigVO fetchConfigVO) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click();", waittext);
			// waittext.click();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clickFilter(WebDriver driver, String xpath1, String xpath2, FetchMetadataVO fetchMetadataVO,
			FetchConfigVO fetchConfigVO) throws InterruptedException {
		try {
			WebElement waittill = driver.findElement(By.xpath(xpath1));
			WebElement waittill1 = driver.findElement(By.xpath(xpath2));
			if (waittill1.isDisplayed()) {
				WebDriverWait wait = new WebDriverWait(driver, 10);
				WebElement element = wait.until(ExpectedConditions.elementToBeClickable(waittill));
				element.click();
				logger.info("Clicked Expand Succesfully.");
				return;
			} else {
				waittill.click();
				System.out.println("");
				logger.info("Clicked Expand Succesfully.");
				return;
			}
		} catch (StaleElementReferenceException e) {
			logger.error("Falied During ClickExpand Action.");
			WebElement waittill = driver.findElement(By.xpath(xpath1));
			WebDriverWait wait = new WebDriverWait(driver, 60);
			wait.until(ExpectedConditions.elementToBeClickable(waittill));
			waittill.click();
		} catch (Exception e) {
			logger.error("Falied During ClickExpand Action.");
			screenshotFail(driver, "Failed during clickExpand Method", fetchMetadataVO, fetchConfigVO);
			System.out.println(xpath1);
			throw e;
		}
	}

	public String password(WebDriver driver, String inputParam, String keysToSend, FetchConfigVO fetchConfigVO,
			FetchMetadataVO fetchMetadataVO) {
		try {
			WebElement waittill = driver.findElement(By.xpath("//*[contains(@placeholder,'" + inputParam + "')]"));
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			return keysToSend;
		} catch (Exception e) {
			screenshotFail(driver, "Failed during clearAndType Method", fetchMetadataVO, fetchConfigVO);
			System.out.println(e);
			throw e;
		}
	}

	public void typeIntoValidxpath(WebDriver driver, String keysToSend, WebElement waittill,
			FetchConfigVO fetchConfigVO, FetchMetadataVO fetchMetadataVO) {
		try {
			waittill.clear();
			waittill.click();
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("arguments[0].value='" + keysToSend + "';", waittill);
			logger.info("clear and typed the given Data");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clearMethod(WebDriver driver, WebElement waittill) {
		WebDriverWait wait = new WebDriverWait(driver, 60);
		wait.until(ExpectedConditions.elementToBeClickable(waittill));
		waittill.click();
		waittill.clear();
		logger.info("clear and typed the given Data");
	}

	public void moveToElement(WebDriver driver, String inputParam, FetchMetadataVO fetchMetadataVO,
			FetchConfigVO fetchConfigVO) {
		WebElement waittill = driver.findElement(By.xpath("//*[normalize-space(text())='" + inputParam + "'][1]"));
		Actions actions = new Actions(driver);
		actions.moveToElement(waittill).build().perform();
	}

	public void scrollUsingElement(WebDriver driver, String inputParam, FetchMetadataVO fetchMetadataVO,
			FetchConfigVO fetchConfigVO) {
		try {
			Thread.sleep(2000);
			WebElement waittill = driver
					.findElement(By.xpath("//span[normalize-space(text())='" + inputParam + "'][1]"));
			// ((JavascriptExecutor)driver).executeScript("document.body.style.zoom='50%';");
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
			// ((JavascriptExecutor)driver).executeScript("document.body.style.zoom='100%';");
			logger.info("ScrollUsingElement Successfully Done!");
			return;
		} catch (Exception e) {
			System.out.println(inputParam);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("//a[normalize-space(text())='" + inputParam + "']"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
			logger.info("ScrollUsingElement Successfully Done!");
			return;
		} catch (Exception e) {
			System.out.println(inputParam);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("//h1[normalize-space(text())='" + inputParam + "']"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
			logger.info("ScrollUsingElement Successfully Done!");
			return;
		} catch (Exception e) {
			System.out.println(inputParam);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("(//h2[normalize-space(text())='" + inputParam + "'])"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
			logger.info("ScrollUsingElement Successfully Done!");
			return;
		} catch (Exception e) {
			System.out.println(inputParam);
		}
		try {
			WebElement waittill = driver
					.findElement(By.xpath("(//h3[normalize-space(text())='" + inputParam + "'])[2]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
			logger.info("ScrollUsingElement Successfully Done!");
			return;
		} catch (Exception e) {
			System.out.println(inputParam);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("//td[normalize-space(text())='" + inputParam + "']"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
			logger.info("ScrollUsingElement Successfully Done!");
			return;
		} catch (Exception e) {
			System.out.println(inputParam);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("//div[contains(text(),'" + inputParam + "')]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
			logger.info("ScrollUsingElement Successfully Done!");
			return;
		} catch (Exception e) {
			System.out.println(inputParam);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("(//table[@summary='" + inputParam + "']//td//a)[1]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
			logger.info("ScrollUsingElement Successfully Done!");
			return;
		} catch (Exception e) {
			System.out.println(inputParam);
		}
		try {
			WebElement waittill = driver.findElement(
					By.xpath("(//label[normalize-space(text())=\"" + inputParam + "\"]/following::input)[1]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
			logger.info("ScrollUsingElement Successfully Done!");
			return;
		} catch (Exception e) {
			System.out.println(inputParam);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("//a[contains(@id,'" + inputParam + "')]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
			logger.info("ScrollUsingElement Successfully Done!");
			return;
		} catch (Exception e) {
			System.out.println(inputParam);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("//li[normalize-space(text())='" + inputParam + "']"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
			logger.info("ScrollUsingElement Successfully Done!");
			return;
		} catch (Exception e) {
			System.out.println(inputParam);
		}
		try {
			WebElement waittill = driver
					.findElement(By.xpath("//label[normalize-space(text())=\"" + inputParam + "\"]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
			logger.info("ScrollUsingElement Successfully Done!");
			return;
		} catch (Exception e) {
			System.out.println(inputParam);
		}
		try {
			WebElement waittill = driver
					.findElement(By.xpath("//button[normalize-space(text())='" + inputParam + "']"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
			logger.info("ScrollUsingElement Successfully Done!");
			return;
		} catch (Exception e) {
			System.out.println(inputParam);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("//img[@title='" + inputParam + "']"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
			logger.info("ScrollUsingElement Successfully Done!");
			return;
		} catch (Exception e) {
			System.out.println(inputParam);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("(//*[@title='" + inputParam + "'])[1]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
			logger.info("ScrollUsingElement Successfully Done!");
			return;
		} catch (Exception e) {
			logger.error("Failed During scrollUsingElement");
			screenshotFail(driver, "Failed during scrollUsingElement Method", fetchMetadataVO, fetchConfigVO);
			System.out.println(inputParam);
			e.printStackTrace();
			throw e;
		}
	}

	private void scrollMethod(WebDriver driver, FetchConfigVO fetchConfigVO, WebElement waittill,
			FetchMetadataVO fetchMetadataVO) {
		fetchConfigVO.getMedium_wait();
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		// WebElement elements =
		// wait.until(ExpectedConditions.elementToBeClickable(waittill));
		WebElement element = waittill;
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
	}

	public void tab(WebDriver driver, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
		try {
			Thread.sleep(4000);
			Actions action = new Actions(driver);
			action.sendKeys(Keys.TAB).build().perform();
			Thread.sleep(8000);
			logger.info("Successfully Clicked the tab.");
		} catch (Exception e) {
			logger.error("Failed During clicking the tab");
			screenshotFail(driver, "Failed during tab Method", fetchMetadataVO, fetchConfigVO);
			System.out.println("Failed to do TAB Action");
			e.printStackTrace();
			throw e;
		}
	}

	public void mousehover(WebDriver driver, String param1, String param2, FetchMetadataVO fetchMetadataVO,
			FetchConfigVO fetchConfigVO) throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			Actions actions = new Actions(driver);
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("(//table[@summary='" + param1 + "']//tr[1]/following::a)[2]")));
			scrollUsingElement(driver, param1, fetchMetadataVO, fetchConfigVO);
			Thread.sleep(6000);
			WebElement waittext = driver
					.findElement(By.xpath("(//table[@summary='" + param1 + "']//tr[1]/following::a)[2]"));
			actions.moveToElement(waittext).build().perform();
			clickImage(driver, param2, param1, fetchMetadataVO, fetchConfigVO);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			Actions actions = new Actions(driver);
			WebElement waittill = driver.findElement(By.xpath(
					"(//table[@role='presentation']/following::a[normalize-space(text())='" + param1 + "'])[1]"));
			actions.moveToElement(waittill).build().perform();
			Thread.sleep(5000);
			System.out.print("Successfully executed Mousehover");
			return;
		} catch (Exception e) {
			System.out.println(e);
			screenshotFail(driver, "Failed during MouseHover Method", fetchMetadataVO, fetchConfigVO);
			throw e;
		}
	}

	public void enter(WebDriver driver, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
		try {
			Thread.sleep(2000);
			Actions actionObject = new Actions(driver);
			actionObject.sendKeys(Keys.ENTER).build().perform();
			Thread.sleep(8000);
		} catch (Exception e) {
			System.out.println(e);
			screenshotFail(driver, "Failed during Enter Method", fetchMetadataVO, fetchConfigVO);
			throw e;
		}
	}

	public String screenshot(WebDriver driver, String screenshotName, FetchMetadataVO fetchMetadataVO,
			FetchConfigVO fetchConfigVO) {
		String image_dest = null;
		try {
			TakesScreenshot ts = (TakesScreenshot) driver;
			File source = ts.getScreenshotAs(OutputType.FILE);
			image_dest = (fetchConfigVO.getScreenshot_path() + fetchMetadataVO.getCustomer_name() + "/"
					+ fetchMetadataVO.getTest_run_name() + "/" + fetchMetadataVO.getSeq_num() + "_"
					+ fetchMetadataVO.getLine_number() + "_" + fetchMetadataVO.getScenario_name() + "_"
					+ fetchMetadataVO.getScript_number() + "_" + fetchMetadataVO.getTest_run_name() + "_"
					+ fetchMetadataVO.getLine_number() + "_Passed").concat(".jpg");
			System.out.println(image_dest);
			File destination = new File(image_dest);
//			FileUtils.copyFile(source, destination);
//			Files.copy(FileSystems.getDefault().getPath(source.getPath()),
//					FileSystems.getDefault().getPath(destination.getPath()), StandardCopyOption.COPY_ATTRIBUTES,
//					StandardCopyOption.REPLACE_EXISTING);
			Files.copy(source.toPath(),
					destination.toPath(), StandardCopyOption.COPY_ATTRIBUTES,
					StandardCopyOption.REPLACE_EXISTING);
			
			logger.info("Successfully Screenshot is taken");
			return image_dest;
		} catch (Exception e) {
			logger.error("Failed During Taking screenshot");
			System.out.println("Exception while taking Screenshot" + e.getMessage());
			return e.getMessage();
		}
	}

	public String screenshotFail(WebDriver driver, String screenshotName, FetchMetadataVO fetchMetadataVO,
			FetchConfigVO fetchConfigVO) {
		String image_dest = null;
		try {
			TakesScreenshot ts = (TakesScreenshot) driver;
			File source = ts.getScreenshotAs(OutputType.FILE);
			String currenttime = new SimpleDateFormat("MM-dd-yyyy HH-mm-ss").format(Calendar.getInstance().getTime());
			image_dest = (fetchConfigVO.getScreenshot_path() + fetchMetadataVO.getCustomer_name() + "/"
					+ fetchMetadataVO.getTest_run_name() + "/" + fetchMetadataVO.getSeq_num() + "_"
					+ fetchMetadataVO.getLine_number() + "_" + fetchMetadataVO.getScenario_name() + "_"
					+ fetchMetadataVO.getScript_number() + "_" + fetchMetadataVO.getTest_run_name() + "_"
					+ fetchMetadataVO.getLine_number() + "_Failed").concat(".jpg");
			File destination = new File(image_dest);
			FileUtils.copyFile(source, destination);
			logger.info("Successfully Failed Screenshot is Taken ");
			return image_dest;
		} catch (Exception e) {
			logger.error("Failed during screenshotFail Action. ");
			System.out.println("Exception while taking Screenshot" + e.getMessage());
			return e.getMessage();
		}
	}

	public String screenshotException(WebDriver driver, String screenshotName,
			List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO, String line_number, String param) {
		String image_dest = null;
		try {
			TakesScreenshot ts = (TakesScreenshot) driver;
			File source = ts.getScreenshotAs(OutputType.FILE);
			image_dest = (fetchConfigVO.getScreenshot_path() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
					+ fetchMetadataListVO.get(0).getTest_run_name() + "/" + line_number + "_"
					+ fetchMetadataListVO.get(0).getScenario_name() + "_"
					+ fetchMetadataListVO.get(0).getScript_number() + "_"
					+ fetchMetadataListVO.get(0).getTest_run_name() + "_" + fetchMetadataListVO.get(0).getScript_id()
					+ "_" + param + "_Failed").concat(".jpg");
			File destination = new File(image_dest);
			FileUtils.copyFile(source, destination);
			logger.info("Successfully Failed Screenshot is Taken ");
			return image_dest;
		} catch (Exception e) {
			logger.error("Failed during screenshotFail Action. ");
			System.out.println("Exception while taking Screenshot" + e.getMessage());
			return e.getMessage();
		}
	}

	public void deleteAllCookies(WebDriver driver, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) {
		try {
			driver.manage().deleteAllCookies();
			logger.info("Successfully Deleted All The Cookies.");
		} catch (Exception e) {
			logger.error("Failed To Delete All The Cookies.");
			screenshotFail(driver, "Failed during deleteAllCookies Method", fetchMetadataVO, fetchConfigVO);
			System.out.println("cookies not deleted");
			throw e;
		}
	}

	public void selectCheckBox(WebDriver driver, String xpath, FetchMetadataVO fetchMetadataVO) {
		try {
			WebElement element = driver.findElement(By.xpath(xpath));
			if (element.isSelected()) {
			} else {
				element.click();
			}
			logger.info("selected Checkbox Successfully");
		} catch (Exception e) {
			logger.error("Failed While Selecting Checkbox.");
			screenshotFail(driver, "Failed during selectCheckBox Method", fetchMetadataVO, null);
			e.printStackTrace();
			System.out.println(xpath);
			throw e;
		}
	}

	public void selectByText(WebDriver driver, String param1, String param2, String inputData,
			FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) {
		try {
			if (param1.equalsIgnoreCase("Address Purposes")) {
				Thread.sleep(2000);
				WebElement waittext = driver.findElement(
						By.xpath(("//*[normalize-space(text())='" + param1 + "']/following::*[normalize-space(text())='"
								+ param2 + "']/following::select[not (@title)]")));
				selectMethod(driver, inputData, fetchMetadataVO, waittext, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
			System.out.println(param2);
		}
		try {
			if (param1.equalsIgnoreCase("Holds")) {
				Thread.sleep(2000);
				WebElement waittext = driver.findElement(
						By.xpath("//*[normalize-space(text())='" + param1 + "']/following::*[normalize-space(text())='"
								+ param2 + "']/preceding-sibling::select[@title='']"));
				selectMethod(driver, inputData, fetchMetadataVO, waittext, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
			System.out.println(param2);
		}
		try {
			if (param2.equalsIgnoreCase("Batch Status")) {
				Thread.sleep(2000);
				WebElement waittext = driver.findElement(By.xpath(
						("//*[normalize-space(text())='" + param1 + "']/following::label[normalize-space(text())='"
								+ param2 + "']/preceding-sibling::select[1]")));
				selectMethod(driver, inputData, fetchMetadataVO, waittext, fetchConfigVO);
				return;
			}

		} catch (Exception e) {
			System.out.println(param2);
		}
		try {
			if (param1.equalsIgnoreCase("Release") && param2.equalsIgnoreCase("Name")) {
				Thread.sleep(5000);
				WebElement waittext = driver.findElement(By.xpath(
						("(//*[normalize-space(text())='" + param1 + "']/following::label[normalize-space(text())='"
								+ param2 + "']/preceding-sibling::select)[2]")));
				selectMethod(driver, inputData, fetchMetadataVO, waittext, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
			System.out.println(param2);
		}
		try {
			Thread.sleep(2000);
			WebElement waittext = driver.findElement(By.xpath(("//*[normalize-space(text())='" + param1
					+ "']/following::label[normalize-space(text())='" + param2 + "']/following::select[1]")));
			selectMethod(driver, inputData, fetchMetadataVO, waittext, fetchConfigVO);
			return;
		} catch (Exception e) {
			System.out.println(param2);
		}
		try {
			Thread.sleep(2000);
			WebElement waittext = driver.findElement(By.xpath(("//*[normalize-space(text())='" + param1
					+ "']/following::label[normalize-space(text())='" + param2 + "']/preceding::select[1]")));
			selectMethod(driver, inputData, fetchMetadataVO, waittext, fetchConfigVO);
			return;
		} catch (Exception e) {
			System.out.println(param2);
		}
		try {
			Thread.sleep(2000);
			WebElement waittext = driver.findElement(By.xpath(("//*[contains(text(),'" + param1
					+ "')]/following::label[normalize-space(text())='" + param2 + "']/following::select[1]")));
			selectMethod(driver, inputData, fetchMetadataVO, waittext, fetchConfigVO);
			return;
		} catch (Exception e) {
			System.out.println(param2);
		}
		try {
//Changed == to equals method
			if (param2.equals("")) {
				WebElement waittext = driver
						.findElement(By.xpath(("//*[contains(text(),'" + param1 + "')]/following::select[1]")));
				selectMethod(driver, inputData, fetchMetadataVO, waittext, fetchConfigVO);
				return;
			}
		} catch (Exception e) {
			System.out.println(param2);
		}
		try {
			WebElement waittext = driver
					.findElement(By.xpath(("//*[contains(text(),'" + param1 + "')]/preceding-sibling::select[1]")));
			selectMethod(driver, inputData, fetchMetadataVO, waittext, fetchConfigVO);
			return;
		} catch (Exception e) {
			System.out.println(param2);
			screenshotFail(driver, "Failed during selectByValue Method", fetchMetadataVO, fetchConfigVO);
			throw e;
		}
	}

	private void selectMethod(WebDriver driver, String inputData, FetchMetadataVO fetchMetadataVO, WebElement waittext,
			FetchConfigVO fetchConfigVO) {
		Actions actions = new Actions(driver);
		actions.moveToElement(waittext).build().perform();
		Select selectBox = new Select(waittext);
		selectBox.selectByVisibleText(inputData);
		logger.info("selectedBYText Successfully");
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		return;
	}

	public void selectByValue(WebDriver driver, String xpath, String inputData, FetchMetadataVO fetchMetadataVO,
			FetchConfigVO fetchConfigVO) {
		try {
			WebElement webElement = driver.findElement(By.xpath(xpath));
			Select selectBox = new Select(webElement);
			selectBox.selectByValue(inputData);
			logger.info("selectedBYValue Successfully");
		} catch (Exception e) {
			logger.error("Failed During selectByValue Action.");
			screenshotFail(driver, "Failed during selectByValue Method", fetchMetadataVO, fetchConfigVO);
			System.out.println(xpath);
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

	public String copynumber(WebDriver driver, String inputParam1, String inputParam2, FetchMetadataVO fetchMetadataVO,

			FetchConfigVO fetchConfigVO) {

		String value = null;

		try {

			if (inputParam1.equalsIgnoreCase("Totals") && inputParam2.equalsIgnoreCase("Total")) {

				Thread.sleep(5000);

				WebElement webElement = driver.findElement(By.xpath("//*[normalize-space(text())='" + inputParam1

						+ "']/following::*[normalize-space(text())='" + inputParam2 + "']/following::span[1]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(webElement).build().perform();
				value = copyNegative(webElement);
				return value;

			}

		} catch (Exception e) {

			System.out.println(inputParam2);

		}
		try {

			if (inputParam1.equalsIgnoreCase("Totals") || inputParam2.equalsIgnoreCase("Transaction Number")
					|| inputParam2.equalsIgnoreCase("Batch Number")) {

				Thread.sleep(5000);

				WebElement webElement = driver.findElement(By.xpath("//*[normalize-space(text())='" + inputParam1

						+ "']/following::*[normalize-space(text())='" + inputParam2 + "']/following::span[1]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(webElement).build().perform();

				value = copyInt(webElement);

				return value;

			}

		} catch (Exception e) {

			System.out.println(inputParam2);

		}

		try {

			if (inputParam1.equalsIgnoreCase("Confirmation") || inputParam2.equalsIgnoreCase("adjustment")
					|| inputParam1.equalsIgnoreCase("Information")) {

				Thread.sleep(5000);

				WebElement webElement = driver.findElement(By.xpath("//div[normalize-space(text())='" + inputParam1

						+ "']/following::label[contains(text(),'" + inputParam2 + "')]"));

				Actions actions = new Actions(driver);

				actions.moveToElement(webElement).build().perform();

				value = copyInt(webElement);

				return value;

			}

		} catch (Exception e) {

			System.out.println(inputParam2);

		}

		try {

			WebElement webElement = driver.findElement(By.xpath("(//div[contains(@title,'" + inputParam1 + "')])[1]"));

			Actions actions = new Actions(driver);

			actions.moveToElement(webElement).build().perform();

			Thread.sleep(5000);

			if (webElement.isDisplayed() == true) {

				value = copyMethod(webElement, value);

				return value;

			}

		} catch (Exception e) {

			System.out.println(inputParam1);

		}

		try {

			WebElement webElement = driver.findElement(By.xpath("//img[@title='In Balance ']/following::td[1]"));

			Actions actions = new Actions(driver);

			actions.moveToElement(webElement).build().perform();

			if (webElement.isDisplayed() == true) {

				value = copyMethod(webElement, value);

				return value;

			}

		} catch (Exception e) {

			logger.error("Failed During copynumber Action");

			screenshotFail(driver, "Failed during CopyNumber Method", fetchMetadataVO, fetchConfigVO);

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

			logger.info("Successfully Copied the Number");

		} catch (Exception e) {

			System.out.println(e);

		}

		return num;

	}

	private String copyInt(WebElement webElement) {

		String num = null;

		try {

//         System.out.println(value);

			String number = webElement.getText().toString();

			System.out.println(number);

			num = number.replaceAll("[^\\d.]+|\\.(?!\\d)", "");

			System.out.println(num);

			logger.info("Successfully Copied the Number");

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
			logger.info("Successfully Copied the Number");

		} catch (Exception e) {

			System.out.println(e);

		}

		return num;

	}

	public String copyy(WebDriver driver, String xpath, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) {
		try {
			WebElement webElement = driver.findElement(By.xpath(xpath));
			String number = webElement.getText();
			System.out.println(number);
			String num = number.replaceAll("[^\\d.]+|\\.(?!\\d)", "");
			StringSelection stringSelection = new StringSelection(num);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
			logger.info("Successfully Copied");
		} catch (Exception e) {
			logger.error("Failed During copyy Action.");
			screenshotFail(driver, "Failed during copyy Method", fetchMetadataVO, fetchConfigVO);
			System.out.println(xpath);
			throw e;
		}
		return xpath;
	}

	public String copytext(WebDriver driver, String xpath, FetchMetadataVO fetchMetadataVO,
			FetchConfigVO fetchConfigVO) {
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
			logger.info("Successfully Copied");
		} catch (Exception e) {
			logger.error("Failed During copytext Action.");
			screenshotFail(driver, "Failed during copytext Method", fetchMetadataVO, fetchConfigVO);
			System.out.println(xpath);
			throw e;
		}
		return xpath;

	}

	public void maximize(WebDriver driver, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) {
		try {
			driver.manage().window().maximize();
			logger.info("Successfully Maximized");
		} catch (Exception e) {
			logger.error("Failed During Maximize Action.");
			screenshotFail(driver, "Failed during maximize Method", fetchMetadataVO, fetchConfigVO);
			System.out.println("can not maximize");
			e.printStackTrace();
			throw e;

		}
	}

	public void switchWindow(WebDriver driver, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) {
		try {
			driver.switchTo().window(Main_Window);
			logger.info("Successfully Switched to Another Window");
		} catch (Exception e) {
			logger.error("Failed During Switching to Window");
			screenshotFail(driver, "Failed during switchWindow Method", fetchMetadataVO, fetchConfigVO);
			System.out.println("can not switch to window");
			e.printStackTrace();

			throw e;
		}
	}

	public void switchDefaultContent(WebDriver driver, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO)
			throws Exception {
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
			logger.info("Successfully Handeled the window");
		} catch (Exception e) {
			logger.error("Failed to Handle the window");
			screenshotFail(driver, "Failed during windowhandle Method", fetchMetadataVO, fetchConfigVO);
			System.out.println("failed while hadling window");
			e.printStackTrace();
			throw e;
		}
	}

	public void switchParentWindow(WebDriver driver, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO)
			throws Exception {
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
		} catch (Exception e) {
			logger.error("Failed to Handle the window");
			screenshotFail(driver, "Failed during windowhandle Method", fetchMetadataVO, fetchConfigVO);
			System.out.println("failed while hadling window");
			e.printStackTrace();
			throw e;
		}
	}

	public void switchToParentWindow(WebDriver driver, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO)
			throws Exception {
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
		} catch (Exception e) {
			logger.error("Failed to Handle the window");
			screenshotFail(driver, "Failed during windowhandle Method", fetchMetadataVO, fetchConfigVO);
			System.out.println("failed while hadling window");
			e.printStackTrace();
			throw e;
		}
	}

	public void dragAnddrop(WebDriver driver, String xpath, String xpath1, FetchMetadataVO fetchMetadataVO,
			FetchConfigVO fetchConfigVO) {
		try {
			WebElement dragElement = driver.findElement(By.xpath(xpath));
			fromElement = dragElement;
			WebElement dropElement = driver.findElement(By.xpath(xpath1));
			toElement = dropElement;
			Actions action = new Actions(driver);
			// Action dragDrop = action.dragAndDrop(fromElement, webElement).build();
			action.dragAndDrop(fromElement, toElement).build().perform();
			logger.info("Successfully Drag and drop the values");
		} catch (Exception e) {
			logger.error("Failed During dragAnddrop Action.");
			screenshotFail(driver, "Failed during dragAnddrop Method", fetchMetadataVO, fetchConfigVO);
			System.out.println(xpath);
			e.printStackTrace();
			throw e;
		}
	}

	public void windowhandle(WebDriver driver, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO)
			throws Exception {
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
					screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
					driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
					driver.switchTo().window(childWindow);
				}
			}
			logger.info("Successfully Handeled the window");
		} catch (Exception e) {
			logger.error("Failed to Handle the window");
			screenshotFail(driver, "Failed during windowhandle Method", fetchMetadataVO, fetchConfigVO);
			System.out.println("failed while hadling window");
			e.printStackTrace();
			throw e;
		}
	}

	public void switchToFrame(WebDriver driver, String inputParam, FetchMetadataVO fetchMetadataVO,
			FetchConfigVO fetchConfigVO) throws Exception {
		try {
			Thread.sleep(5000);
			WebElement waittext = driver
					.findElement(By.xpath("//h1[normalize-space(text())='" + inputParam + "']/following::iframe[1]"));
			driver.switchTo().frame(waittext);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			WebElement waittext = driver.findElement(By.xpath("//iframe[contains(@id,'" + inputParam + "')]"));
			driver.switchTo().frame(waittext);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			Thread.sleep(5000);
			WebElement waittext = driver.findElement(By.xpath("//iframe[@title='" + inputParam + "']"));
			driver.switchTo().frame(waittext);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			Thread.sleep(5000);
			WebElement waittext = driver
					.findElement(By.xpath("//h1[normalize-space(text())='" + inputParam + "']/following::iframe[1]"));
			driver.switchTo().frame(waittext);
			return;
		} catch (Exception e) {
			System.out.println(e);
			logger.error("Failed During switchToFrame Action");
			screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, fetchConfigVO);
			throw e;
		}
	}

	public void uploadFileAutoIT(String filelocation, FetchMetadataVO fetchMetadataVO) throws Exception {
		try {
			String autoitscriptpath = System.getProperty("user.dir") + "\\" + "File_upload_selenium_webdriver.au3";

			Runtime.getRuntime().exec("cmd.exe /c Start AutoIt3.exe " + autoitscriptpath + " \"" + filelocation + "\"");
			logger.info("Successfully Uploaded The File");
		} catch (Exception e) {
			logger.error("Failed During uploadFileAutoIT Action.");
			System.out.println(filelocation);
			e.printStackTrace();
			throw e;

		}
	}

	public void refreshPage(WebDriver driver, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) {
		try {
			driver.navigate().refresh();
			logger.info("Successfully refreshed the Page");
		} catch (Exception e) {
			logger.error("Failed During refreshPage Action");
			screenshotFail(driver, "Failed during refreshPage Method", fetchMetadataVO, fetchConfigVO);
			System.out.println("can not refresh the page");
			e.printStackTrace();
			throw e;

		}
	}

}
