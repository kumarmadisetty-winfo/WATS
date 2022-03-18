package com.winfo.scripts;


import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.OpenCVFrameConverter;
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
import org.openqa.selenium.By;
//import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.FluentWait;
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

import com.assertthat.selenium_shutterbug.core.Shutterbug;
import com.assertthat.selenium_shutterbug.utils.web.ScrollStrategy;
import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.lowagie.text.DocumentException;
import com.winfo.interface1.SeleniumKeyWordsInterface;
import com.winfo.services.DataBaseEntry;
import com.winfo.services.FetchConfigVO;
import com.winfo.services.FetchMetadataVO;
import com.winfo.services.ScriptXpathService;
import com.winfo.utils.DateUtils;
import com.winfo.utils.StringUtils;

@Service("ARLO")
public  class ARLOSeleniumKeywords implements SeleniumKeyWordsInterface {

	@Autowired
	private DataBaseEntry  databaseentry;
	Logger logger = LogManager.getLogger(ARLOSeleniumKeywords.class);
	/*
	 * private  Integer ElementWait = Integer
	 * .valueOf(PropertyReader.getPropertyValue(PropertyConstants.EXECUTION_TIME.
	 * value)); public  int WaitElementSeconds = new Integer(ElementWait);
	 */
	public String Main_Window = "";
	public  WebElement fromElement;
	public  WebElement toElement;
	
	@Autowired
	ScriptXpathService service;
	public void loginApplication(WebDriver driver, FetchConfigVO fetchConfigVO,
			FetchMetadataVO fetchMetadataVO, String type1, String type2, String type3, String param1, String param2, String param3, String keysToSend, String value) throws Exception {
		String param5 = "Password";
		String param6 = "Sign In";
		navigateUrl(driver, fetchConfigVO, fetchMetadataVO);
		sendValue(driver, param1, param3, keysToSend, fetchMetadataVO, fetchConfigVO);
		sendValue(driver, param5, param2, value, fetchMetadataVO, fetchConfigVO);
		clickSignInSignOut(driver, param6, fetchMetadataVO, fetchConfigVO);
//		clickButton(driver, param6, param2, fetchMetadataVO, fetchConfigVO);
	}
	
	public void navigate(WebDriver driver, FetchConfigVO fetchConfigVO,
			FetchMetadataVO fetchMetadataVO, String type1, String type2, String param1, String param2,int count) throws Exception {
		String param3 = "Navigator";
		clickLink(driver, param3, param2, fetchMetadataVO, fetchConfigVO);
		clickMenu(driver, param1, param2, fetchMetadataVO, fetchConfigVO);
	}
	
	public void openTask(WebDriver driver, FetchConfigVO fetchConfigVO,
			FetchMetadataVO fetchMetadataVO, String type1, String type2, String param1, String param2,int count) throws Exception {
		String param3 = "Tasks";
		clickImage(driver, param3, param2, fetchMetadataVO, fetchConfigVO);
		clickTaskLink(driver, param1,  fetchMetadataVO, fetchConfigVO);
		
	}
	
	public void logout(WebDriver driver, FetchConfigVO fetchConfigVO,
			FetchMetadataVO fetchMetadataVO, String type1, String type2, String type3, String param1, String param2, String param3) throws Exception {
		String param4 = "UIScmil1u";
		String param5 = "Sign Out";
		String param6 = "Confirm";
		clickLink(driver, param4, param3, fetchMetadataVO, fetchConfigVO);
		clickLink(driver, param5, param3, fetchMetadataVO, fetchConfigVO);
		clickSignInSignOut(driver, param6, fetchMetadataVO, fetchConfigVO);
//		clickButton(driver, param6, param2, fetchMetadataVO, fetchConfigVO);
	}

	public  void navigateUrl(WebDriver driver, FetchConfigVO fetchConfigVO,
			FetchMetadataVO fetchMetadataVO) {
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

	public  void mediumWait(WebDriver driver, String inputData, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) {
		try {
			int time = StringUtils.convertStringToInteger(inputData, 4);
			int seconds = time * 1000;
			Thread.sleep(seconds);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public  void shortwait(WebDriver driver, String inputData) {
		try {
			int time = StringUtils.convertStringToInteger(inputData, 2);
			int seconds = time * 1000;
			Thread.sleep(seconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public  void wait(WebDriver driver, String inputData) {
		try {
			int time = StringUtils.convertStringToInteger(inputData, 8);
			int seconds = time * 1000;
			Thread.sleep(seconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public  void uploadImage(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO){
		try {
			String sharepoint = fetchConfigVO.getSharepoint_resp();
			System.out.println(sharepoint);
			String accessToken = getAccessToken();
			List imageUrlList = new ArrayList();
			File imageDir = new File(System.getProperty("user.dir") + "\\" + "Screenshot\\"
					+ fetchMetadataListVO.get(0).getCustomer_name() + "\\" + fetchMetadataListVO.get(0).getTest_run_name());
			for (File imageFile : imageDir.listFiles()) {
				String imageFileName = imageFile.getName();
				System.out.println(imageFileName);
				imageUrlList.add(imageFileName);
				BufferedImage bImage = ImageIO.read(new File(imageDir + "\\" + imageFileName));
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

				RestTemplate restTemplate = new RestTemplate();
				ResponseEntity<byte[]> response = restTemplate.exchange("https://graph.microsoft.com/v1.0/me/drive/root:/Screenshot/" + fetchMetadataListVO.get(0).getCustomer_name()
						+ "/" + imageFileName + ":/content",
						HttpMethod.PUT, requestEntity, byte[].class);

				System.out.println("response status: " + response.getStatusCode());
				System.out.println("response body: " + response.getBody());
				System.out.println("response : " + response);
			}
		} catch (Exception e) {
			  System.out.println(e);
		  }
	}
	
	public  String getAccessToken(){
		  String acessToken = null;
		  try {
			  RestTemplate restTemplate = new RestTemplate();

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
				
			  System.out.println(response);

			  @SuppressWarnings("unchecked")
			  Map<String, Object> linkedMap = response.getBody() != null ? (Map<String, Object>) response.getBody() : null;
			  acessToken = linkedMap != null ? StringUtils.convertToString(linkedMap.get("access_token")) : null;
		} catch (Exception e) {
			System.out.println(e);
		}
		  System.out.println(acessToken);
		  return acessToken;
	  }
	
	public List<String> getFailFileNameList(List<FetchMetadataVO> fetchMetadataListVO,FetchConfigVO fetchConfigVO)throws IOException {
		List<String> fileNameList = new ArrayList<String>();
		File folder = new File(System.getProperty("user.dir")+"/"+"Screenshot/" + fetchMetadataListVO.get(0).getCustomer_name() + "/" + fetchMetadataListVO.get(0).getTest_run_name() + "/");
		File[] listOfFiles = folder.listFiles();
		Arrays.sort(listOfFiles, new Comparator<File>(){
            public int compare(File f1, File f2)
            {
                return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
                
            } });
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
				if ("PNG".equalsIgnoreCase(fileExt) && scripNumber.equalsIgnoreCase(currentScriptNumber)) {
					fileNameList.add(fileName);
				}
			}
		}
		return fileNameList;
	}
	
	public List<String> getFileNameList(List<FetchMetadataVO> fetchMetadataListVO) {
		List<String> fileNameList = new ArrayList<String>();
		File folder = new File(System.getProperty("user.dir")+"\\"+"Screenshot\\" + fetchMetadataListVO.get(0).getCustomer_name() + "\\" + fetchMetadataListVO.get(0).getTest_run_name() + "\\");
		File[] listOfFiles = folder.listFiles();
		Arrays.sort(listOfFiles, new Comparator<File>(){
            public int compare(File f1, File f2)
            {
                return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
                
            } });
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
				if ("PNG".equalsIgnoreCase(fileExt) && scripNumber.equalsIgnoreCase(currentScriptNumber) && "Passed".equalsIgnoreCase(status)) {
					fileNameList.add(fileName);
				}
			}
		}
		return fileNameList;
	}
	
	public List<String> getPassedPdf(List<FetchMetadataVO> fetchMetadataListVO) {
		List<String> fileNameList = new ArrayList<String>();
		File folder = new File(System.getProperty("user.dir")+"\\"+"Screenshot\\" + fetchMetadataListVO.get(0).getCustomer_name() + "\\" + fetchMetadataListVO.get(0).getTest_run_name() + "\\");
		File[] listOfFiles = folder.listFiles();
		Arrays.sort(listOfFiles, new Comparator<File>(){
            public int compare(File f1, File f2)
            {
                return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
                
            } });
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
				if ("PNG".equalsIgnoreCase(fileExt) && "Passed".equalsIgnoreCase(status)) {
					fileNameList.add(fileName);
				}
			}
		}
		return fileNameList;
	}
	
	public  List<String> getFailedPdf(List<FetchMetadataVO> fetchMetadataListVO) {

		List<String> fileNameList = new ArrayList<String>();
		File folder = new File(System.getProperty("user.dir")+"/"+"Screenshot/" + fetchMetadataListVO.get(0).getCustomer_name() + "/"+ fetchMetadataListVO.get(0).getTest_run_name() + "/");
		File[] listOfFiles = folder.listFiles();
		Arrays.sort(listOfFiles, new Comparator<File>(){
            public int compare(File f1, File f2)
            {
                return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
                
            } });
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
				if ("PNG".equalsIgnoreCase(fileExt) && ("Failed".equalsIgnoreCase(status))) {
					fileNameList.add(fileName);
				}
			}
		}
		return fileNameList;
	}
	
	public  List<String> getDetailPdf(List<FetchMetadataVO> fetchMetadataListVO) {
		List<String> fileNameList = new ArrayList<String>();
		File folder = new File(System.getProperty("user.dir")+"/"+"Screenshot/" + fetchMetadataListVO.get(0).getCustomer_name() + "/"+ fetchMetadataListVO.get(0).getTest_run_name() + "/");
		File[] listOfFiles = folder.listFiles();
		Arrays.sort(listOfFiles, new Comparator<File>(){
            public int compare(File f1, File f2)
            {
                return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
                
            } });
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
				if ("PNG".equalsIgnoreCase(fileExt)) {
					fileNameList.add(fileName);
				}
			}
		}
		return fileNameList;
	}
	
	public List<String> getFailFileNameListNew(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO) throws Exception {
		System.out.println("entered to getFailFileNameListNew");
				File folder = new File(fetchConfigVO.getScreenshot_path() + fetchMetadataListVO.get(0).getCustomer_name() + "\\"
						+ fetchMetadataListVO.get(0).getTest_run_name() + "\\");
				//File folder = new File("C:\\\\Users\\\\Winfo Solutions\\\\Desktop\\\\test");
				File[] listOfFiles = folder.listFiles();
				String video_rec="no";
				//String video_rec="yes";
//				List<File> fileList = Arrays.asList(listOfFiles);
				List<File> allFileList = Arrays.asList(listOfFiles);
		        List<File> fileList = new ArrayList<>();
		        String seqNumber = fetchMetadataListVO.get(0).getSeq_num();
		       // String seqNumber = "1";
		        for (File file : allFileList) {
		            if(file.getName().startsWith(seqNumber+"_")) {
		                fileList.add(file);
		            }
		        }
		        System.out.println("before Collections.sort completed");
				Collections.sort(fileList, new Comparator<File>() {

					public int compare(File f1, File f2) {

						return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified()) * -1;

					}

				});
				System.out.println("after Collections.sort completed");
				List<String> fileNameList = new ArrayList<String>();
				ArrayList<String> linksall = new ArrayList<String>();
				ArrayList<String> links1 = new ArrayList<String>();
//				File file = new File("/u01/oracle/selenium/apache-tomcat-8.5.60/webapps/wats/WEB-INF/classes/Images/white.jpg");
				//File file = new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\white.jpg");
//				File file1 = new File("/u01/oracle/selenium/apache-tomcat-8.5.60/webapps/wats/WEB-INF/classes/Images/WATS_LOGO.JPG");
				//File file1=new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\WATS_LOGO.JPG");

		        BufferedImage image = null;
//		        image = ImageIO.read(file);
		        BufferedImage logo = null;
//		        logo = ImageIO.read(file1);
//		        Graphics g = image.getGraphics();
//		        g.setColor(Color.black);
		        java.awt.Font font=new java.awt.Font("Calibri",  java.awt.Font.PLAIN, 36);
//		        g.setFont(font);
		        String details= fileList.get(0).getName();
		       //String details= seqList.get(0).getName();
		       	String ScriptNumber = details.split("_")[3];
				String TestRun = details.split("_")[4];
				String Status = details.split("_")[6];
				String status = Status.split("\\.")[0];
				String Scenario = details.split("_")[2];
				String imagename=TestRun+ScriptNumber;
		        String TName = fetchMetadataListVO.get(0).getTest_run_name();
		        String no = details.split("_")[0];
		        Date Starttime = fetchConfigVO.getStarttime();
		        Date endtime=fetchConfigVO.getEndtime();
		        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		        String Starttime1=dateFormat.format(Starttime);
		//Changed the executed by variable
		        String ExeBy=fetchMetadataListVO.get(0).getExecuted_by();
		        String endtime1=dateFormat.format(endtime);
		        long diff=endtime.getTime() - Starttime.getTime();
		        long diffSeconds = diff / 1000 % 60;
		        long diffMinutes = diff / (60 * 1000) % 60;
		        long diffHours = diff / (60 * 60 * 1000);
		        String ExecutionTime=diffHours+":"+diffMinutes+":"+diffSeconds;
//		        g.drawString("TEST SCRIPT DETAILS",450, 50);
//		        g.drawString("Test Run Name : " +  TName, 50, 100);
//		        g.drawString("Script Number : " +  ScriptNumber, 50, 150);
//		        g.drawString("Scenario Name :"+Scenario, 50, 200);
//		        g.drawString("Status : "+status, 50, 250);
//		        g.drawString("Executed By :"+ExeBy, 50, 300);
//		        g.drawString("Start Time :"+Starttime1, 50, 350);
//		        g.drawString("End Time :"+endtime1, 50, 400);
//		        g.drawString("Execution Time : "+ExecutionTime, 50, 450);
//		        g.drawImage(logo,1012,15,null);
//		        g.dispose();
		        System.out.println("before ImageIO.write");
//		        ImageIO.write(image, "jpg", new File("/u01/oracle/selenium/apache-tomcat-8.5.60/webapps/wats/WEB-INF/classes/Images/first.jpg"));
		        //ImageIO.write(image, "jpg", new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\first.jpg"));
		        
		        BufferedImage image1 = null;
//		        image1 = ImageIO.read(file);
//		        Graphics g1 = image1.getGraphics();
//		        g1.setColor(Color.red);
//		        java.awt.Font font1 = new java.awt.Font("Calibir", java.awt.Font.PLAIN, 36);
//		        g1.setFont(font1);
//		        g1.drawString("FAILED IN THE NEXT STEP!!", 400, 300);
//		        g1.drawImage(logo,1012,15,null);
//		        g1.dispose();
//		        ImageIO.write(image1, "jpg", new File("/u01/oracle/selenium/temp/VideoRecord/last.jpg"));
//		        String imgpath2 ="/u01/oracle/selenium/apache-tomcat-8.5.60/webapps/wats/WEB-INF/classes/Images/";
		        //ImageIO.write(image1, "jpg", new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\last.jpg"));
		        //String imgpath2 ="C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\";
//		        File f11=new File(imgpath2);
//		        File[] f22=f11.listFiles();
		        System.out.println("before Failed.jpg");
				if (fileList.get(0).getName().endsWith("Failed.png")) {
//					for(File f33:f22) {
//			        	if(f33.getAbsolutePath().contains("first")) {
//			        		linksall.add(f33.getAbsolutePath());
//			        		linksall.set(0,f33.getAbsolutePath());
//			        	}
//			        	if(f33.getAbsolutePath().contains("last")) {
//			        		linksall.add(f33.getAbsolutePath());
//			        		linksall.add(f33.getAbsolutePath());  
//			        		linksall.set(1,f33.getAbsolutePath());
		//
//			        	}}
					fetchConfigVO.setStatus1("Fail");
					fileNameList.add(fileList.get(0).getName());
//					links1.add(fileList.get(0).getAbsolutePath());
//		            links1.add(linksall.get(1));
		            System.out.println("added links1 list");
					for (int i = 1; i < fileList.size(); i++) {

						if (!fileList.get(i).getName().endsWith("Failed.png")) {
//							 links1.add(fileList.get(i).getAbsolutePath());
							fileNameList.add(fileList.get(i).getName());

						} else {

							

						}

					}

					//links1.add(linksall.get(0));
					Collections.reverse(links1);
					Collections.reverse(fileNameList);
					 System.out.println("Collections.reverse");
				}

				// targetFileList.addAll(seqList);

				/*
				 * for (String fileName : fileNameList) {
				 * 
				 * System.out.println("Target File : " + fileName);
				 * 
				 * }
				 */
				if(video_rec.equalsIgnoreCase("Y")) {
					String name=no+"_"+ScriptNumber+".mp4";
					convertJPGtoMovie(null,links1,fetchMetadataListVO,fetchConfigVO,name);
			          }
				return fileNameList;
			}



	public List<String> getFileNameListNew(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO) throws Exception {

		File folder = new File(fetchConfigVO.getScreenshot_path() + fetchMetadataListVO.get(0).getCustomer_name() + "\\"
				+ fetchMetadataListVO.get(0).getTest_run_name() + "\\");
		//File folder = new File("C:\\\\Users\\\\Winfo Solutions\\\\Desktop\\\\test");

		File[] listOfFiles = folder.listFiles();
//		String video_rec=fetchConfigVO.getEnable_video();
		String video_rec="no";
//		List<File> fileList = Arrays.asList(listOfFiles);
		List<File> allFileList = Arrays.asList(listOfFiles);
        List<File> fileList = new ArrayList<>();
        String seqNumber = fetchMetadataListVO.get(0).getSeq_num();
        //String seqNumber = "1";
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
		ArrayList<String> linksall = new ArrayList<String>();
		ArrayList<String> links1 = new ArrayList<String>();
//		File file = new File("/u01/oracle/selenium/apache-tomcat-8.5.60/webapps/wats/WEB-INF/classes/Images/white.jpg");
		//File file = new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\white.jpg");
//		File file1 = new File("/u01/oracle/selenium/apache-tomcat-8.5.60/webapps/wats/WEB-INF/classes/Images/WATS_LOGO.JPG");
		//File file1=new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\WATS_LOGO.JPG");
		
        BufferedImage image = null;
//        image = ImageIO.read(file);
        BufferedImage logo = null;
//        logo = ImageIO.read(file1);
//        Graphics g = image.getGraphics();
//        g.setColor(Color.black);
        java.awt.Font font=new java.awt.Font("Calibri",  java.awt.Font.PLAIN, 36);
//        g.setFont(font);
        String details= fileList.get(0).getName();
        //String details= seqList.get(0).getName();
        String ScriptNumber = details.split("_")[3];
 		String TestRun = details.split("_")[4];
 		String Status = details.split("_")[6];
 		String status = Status.split("\\.")[0];
 		String Scenario = details.split("_")[2];
 		String imagename=TestRun+ScriptNumber;
        String TName = fetchMetadataListVO.get(0).getTest_run_name();
        String no = details.split("_")[0];
        Date Starttime = fetchConfigVO.getStarttime();
        Date endtime=fetchConfigVO.getEndtime();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String Starttime1=dateFormat.format(Starttime);
//Changed the executed by variable
        String ExeBy=fetchMetadataListVO.get(0).getExecuted_by();
        String endtime1=dateFormat.format(endtime);
        long diff=endtime.getTime() - Starttime.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000);
        String ExecutionTime=diffHours+":"+diffMinutes+":"+diffSeconds;
//        g.drawString("TEST SCRIPT DETAILS",450, 50);
//        g.drawString("Test Run Name : " +  TName, 50, 100);
//        g.drawString("Script Number : " +  ScriptNumber, 50, 150);
//        g.drawString("Scenario Name :"+Scenario, 50, 200);
//        g.drawString("Status : "+status, 50, 250);
//        g.drawString("Executed By :"+ExeBy, 50, 300);
//        g.drawString("Start Time :"+Starttime1, 50, 350);
//        g.drawString("End Time :"+endtime1, 50, 400);
//        g.drawString("Execution Time : "+ExecutionTime, 50, 450);
//        g.drawImage(logo,1012,15,null);
//        g.dispose();
//        ImageIO.write(image, "jpg", new File("/u01/oracle/selenium/apache-tomcat-8.5.60/webapps/wats/WEB-INF/classes/Images/first.jpg"));
        //ImageIO.write(image, "jpg", new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\first.jpg"));
        
        BufferedImage image1 = null;
//        image1 = ImageIO.read(file);
//        Graphics g1 = image1.getGraphics();
//        g1.setColor(Color.red);
//        java.awt.Font font1 = new java.awt.Font("Calibir", java.awt.Font.PLAIN, 36);
//        g1.setFont(font1);
//        g1.drawString("FAILED IN THE NEXT STEP!!", 400, 300);
//        g1.drawImage(logo,1150,15,null);
//        g1.dispose();
//        ImageIO.write(image1, "jpg", new File("/u01/oracle/selenium/apache-tomcat-8.5.60/webapps/wats/WEB-INF/classes/Images/last.jpg"));
        //ImageIO.write(image1, "jpg", new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\last.jpg"));
//        String imgpath2 ="/u01/oracle/selenium/apache-tomcat-8.5.60/webapps/wats/WEB-INF/classes/Images/";
        //String imgpath2 ="C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\";
//        File f11=new File(imgpath2);
//        File[] f22=f11.listFiles();
       

		if (!fileList.get(0).getName().endsWith("Failed.png")) {
//			for(File f33:f22)
//	        {
//	        	if(f33.getAbsolutePath().contains("first")) {
//			  linksall.add(f33.getAbsolutePath());
//	        	}
//	        }
			fetchConfigVO.setStatus1("Pass");
			fileNameList.add(fileList.get(0).getName());
//			links1.add(fileList.get(0).getAbsolutePath());
			for (int i = 1; i < fileList.size(); i++) {

				if (!fileList.get(i).getName().endsWith("Failed.png")) {
//					links1.add(fileList.get(i).getAbsolutePath());

					fileNameList.add(fileList.get(i).getName());

				} else {

					

				}

			}

//			 links1.add(linksall.get(0));
			 Collections.reverse(links1);
			Collections.reverse(fileNameList);

		}

		// targetFileList.addAll(seqList);

		/*
		 * for (String fileName : fileNameList) {
		 * 
		 * System.out.println("Target File : " + fileName);
		 * 
		 * }
		 */
		if(video_rec.equalsIgnoreCase("Y")) {
			String name=no+"_"+ScriptNumber+".mp4";
			convertJPGtoMovie(null,links1,fetchMetadataListVO,fetchConfigVO,name);
	          }
		return fileNameList;

	}

	public List<String> getPassedPdfNew(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO) throws Exception {

		File folder = new File(fetchConfigVO.getScreenshot_path() + fetchMetadataListVO.get(0).getCustomer_name() + "\\"
				+ fetchMetadataListVO.get(0).getTest_run_name() + "\\");
		//File folder=new File("C:\\Users\\Winfo Solutions\\Desktop\\test");
		File[] listOfFiles = folder.listFiles();
		//String video_rec=fetchConfigVO.getVideo_rec();
		String video_rec="no";
		Map<Integer, List<File>> filesMap = new TreeMap<>();
		int passcount=0;
		int failcount=0;
		for (File file : Arrays.asList(listOfFiles)) {

			Integer seqNum = Integer.valueOf(file.getName().substring(0, file.getName().indexOf('_')));

			if (!filesMap.containsKey(seqNum)) {

				filesMap.put(seqNum, new ArrayList<File>());

			}

			filesMap.get(seqNum).add(file);

		}

		List<String> targetFileList = new ArrayList<>();
		ArrayList<String> links = new ArrayList<String>();
		String firstimagelink=null;
		for (Entry<Integer, List<File>> seqEntry : filesMap.entrySet()) {

			List<File> seqList = seqEntry.getValue();

			Collections.sort(seqList, new Comparator<File>() {

				public int compare(File f1, File f2) {

					return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified()) * -1;

				}

			});
			List<String> seqFileNameList = new ArrayList<String>();
			ArrayList<String> links1 = new ArrayList<String>();
			ArrayList<String> linksall = new ArrayList<String>();

//			File file = new File("/u01/oracle/selenium/apache-tomcat-8.5.60/webapps/wats/WEB-INF/classes/Images/white.jpg");
			//File file = new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\white.jpg");
//			File file1 = new File("/u01/oracle/selenium/apache-tomcat-8.5.60/webapps/wats/WEB-INF/classes/Images/WATS_LOGO.JPG");
			//File file1=new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\WATS_LOGO.JPG");

			 BufferedImage image = null;
//		     image = ImageIO.read(file);
		     BufferedImage logo = null;
//		     logo = ImageIO.read(file1);
//		     Graphics g = image.getGraphics();
//		     g.setColor(Color.black);
		     java.awt.Font font=new java.awt.Font("Calibri",  java.awt.Font.PLAIN, 36);
//		     g.setFont(font);
		      
		     String details= seqList.get(0).getName();
		     String ScriptNumber = details.split("_")[3];
		     String TestRun = details.split("_")[4];
		     String Status = details.split("_")[6];
		     String status = Status.split("\\.")[0];
		     String Scenario = details.split("_")[2];
		     String imagename=TestRun+ScriptNumber;
		     String TName = fetchMetadataListVO.get(0).getTest_run_name();
		     Date endtime=fetchConfigVO.getEndtime();
		     Date TStarttime=fetchConfigVO.getStarttime1();
		     DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		     String TStarttime1=dateFormat.format(TStarttime);
//Changed the executed by variable
		     String ExeBy=fetchMetadataListVO.get(0).getExecuted_by();
		     String endtime1=dateFormat.format(endtime);
		     long Tdiff=endtime.getTime() - TStarttime.getTime();
		     long TdiffSeconds = Tdiff / 1000 % 60;
		     long TdiffMinutes = Tdiff / (60 * 1000) % 60;
		     long TdiffHours = Tdiff / (60 * 60 * 1000);
		     String ExecutionTime=TdiffHours+":"+TdiffMinutes+":"+TdiffSeconds;
//		     g.drawString("TEST SCRIPT DETAILS", 450, 50);
//		     g.drawString("Test Run Name : " +  TName, 50, 125);
//		     g.drawString("Script Number : " +  ScriptNumber, 50, 200);
//		     g.drawString("Scenario Name :"+Scenario, 50, 275);
//		     g.drawString("Status : "+status, 50, 350);
//		     g.drawString("Executed By :"+ExeBy, 50, 425);
//		     g.drawImage(logo,1012,15,null);
//////		 g.drawString("Start Time :"+TStarttime1, 50, 425);
//////		 g.drawString("End Time :"+endtime1, 50, 500);
//////		 g.drawString("Execution Time : "+ExecutionTime, 50, 575);
//		     g.dispose();
		     
		     BufferedImage image2 = null;
//			 image2 = ImageIO.read(file);
//			 Graphics g2 = image2.getGraphics();
//			 g2.setColor(Color.black);
//			 g2.setFont(font);
//			 g2.drawString("TEST RUN SUMMARY", 450, 50);
//		     g2.drawString("Test Run Name : " +  TName, 50, 125);
//	         g2.drawString("Executed By :"+ExeBy, 50, 200);
//	         g2.drawString("Start Time :"+TStarttime1, 50, 275);
//	         g2.drawString("End Time :"+endtime1, 50, 350);
//	         g2.drawString("Execution Time : "+ExecutionTime, 50,425);
//	         g2.drawImage(logo,1012,15,null);
//		     g2.dispose();
//			 ImageIO.write(image2, "jpg", new File("/u01/oracle/selenium/apache-tomcat-8.5.60/webapps/wats/WEB-INF/classes/Images/first.jpg"));
			 //ImageIO.write(image2, "jpg", new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\first.jpg"));
//			 String imgpath3 ="/u01/oracle/selenium/apache-tomcat-8.5.60/webapps/wats/WEB-INF/classes/Images/first.jpg";
//			 String imgpath2 ="/u01/oracle/selenium/apache-tomcat-8.5.60/webapps/wats/WEB-INF/classes/Images/";
//	         ImageIO.write(image, "jpg", new File("/u01/oracle/selenium/apache-tomcat-8.5.60/webapps/wats/WEB-INF/classes/Images/"+imagename+".jpg"));
			 //String imgpath3 ="C\\Users\\Winfo Solutions\\Desktop\\Add_On\\first.jpg";
			 //String imgpath2 ="C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\";
	         //ImageIO.write(image, "jpg", new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\"+imagename+".jpg"));
	        	        
//	        File f11=new File(imgpath2);
//	        File[] f22=f11.listFiles();
//	        File f44=new File(imgpath3);
//	        firstimagelink=f44.getAbsolutePath();

			
	        if (!seqList.get(0).getName().endsWith("Failed.png")) {
				passcount++;
//				for(File f33:f22)
//		        {
//					if(f33.getAbsolutePath().contains(imagename)) {
//						  linksall.add(f33.getAbsolutePath());
//				        	}
//		        }
//				links1.add(seqList.get(0).getAbsolutePath());
				seqFileNameList.add(seqList.get(0).getName());

				for (int i = 1; i < seqList.size(); i++) {

					if (!seqList.get(i).getName().endsWith("Failed.png")) {
//						links1.add(seqList.get(i).getAbsolutePath());
						seqFileNameList.add(seqList.get(i).getName());

					} else {

						

					}

				}

//				links1.add(linksall.get(0));
				Collections.reverse(links1);
				Collections.reverse(seqFileNameList);
				links.addAll(links1);
				targetFileList.addAll(seqFileNameList);


			}

////                    targetFileList.addAll(seqList);

		}

		/*
		 * for (String fileName : targetFileList) {
		 * 
		 * System.out.println("Target File : " + fileName);
		 * 
		 * }
		 */

		fetchConfigVO.setPasscount(passcount);
		fetchConfigVO.setFailcount(failcount);
		if(video_rec.equalsIgnoreCase("yes")) {
			convertJPGtoMovie(firstimagelink,links,fetchMetadataListVO,fetchConfigVO,"Passed_Video.mp4");
	          }
		System.out.println(targetFileList.size());
		return targetFileList;
	}

	public List<String> getFailedPdfNew(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO) throws IOException{

		File folder = new File(fetchConfigVO.getScreenshot_path() + fetchMetadataListVO.get(0).getCustomer_name() + "\\"
				+ fetchMetadataListVO.get(0).getTest_run_name() + "\\");
		//File folder=new File("C:\\Users\\Winfo Solutions\\Desktop\\test");
		File[] listOfFiles = folder.listFiles();
		//String video_rec=fetchConfigVO.getVideo_rec();
		String video_rec="no";
		Map<Integer, List<File>> filesMap = new TreeMap<>();
		int failcount=0;
		int passcount=0;
		for (File file : Arrays.asList(listOfFiles)) {

			Integer seqNum = Integer.valueOf(file.getName().substring(0, file.getName().indexOf('_')));

			if (!filesMap.containsKey(seqNum)) {

				filesMap.put(seqNum, new ArrayList<File>());

			}

			filesMap.get(seqNum).add(file);

		}

		List<String> targetFileList = new ArrayList<>();
		ArrayList<String> links = new ArrayList<String>();
		String firstimagelink=null;
		for (Entry<Integer, List<File>> seqEntry : filesMap.entrySet()) {

			List<File> seqList = seqEntry.getValue();

			Collections.sort(seqList, new Comparator<File>() {

				public int compare(File f1, File f2) {

					return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified()) * -1;

				}

			});
			

			List<String> seqFileNameList = new ArrayList<String>();
			ArrayList<String> links1 = new ArrayList<String>();
			ArrayList<String> linksall = new ArrayList<String>();

//			File file = new File("/u01/oracle/selenium/apache-tomcat-8.5.60/webapps/wats/WEB-INF/classes/Images/white.jpg");
			//File file = new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\white.jpg");
//			File file1 = new File("/u01/oracle/selenium/apache-tomcat-8.5.60/webapps/wats/WEB-INF/classes/Images/WATS_LOGO.JPG");
			//File file1=new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\WATS_LOGO.JPG");
			
	        BufferedImage image = null;
//	        image = ImageIO.read(file);
	        BufferedImage logo = null;
//	        logo = ImageIO.read(file1);
//	        Graphics g = image.getGraphics();
//	        g.setColor(Color.black);
	        java.awt.Font font=new java.awt.Font("Calibri",  java.awt.Font.PLAIN, 36);
//	        g.setFont(font);
	        String details= seqList.get(0).getName();
	        String ScriptNumber = details.split("_")[3];
			String TestRun = details.split("_")[4];
			String Status = details.split("_")[6];
			String status = Status.split("\\.")[0];
			String Scenario = details.split("_")[2];
			String imagename=TestRun+ScriptNumber;
	        String TName = fetchMetadataListVO.get(0).getTest_run_name();
	        Date endtime=fetchConfigVO.getEndtime();
	        Date TStarttime=fetchConfigVO.getStarttime1();
	        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	        String TStarttime1=dateFormat.format(TStarttime);
//Changed the executed by variable
	        String ExeBy=fetchMetadataListVO.get(0).getExecuted_by();
	        String endtime1=dateFormat.format(endtime);
	        long Tdiff=endtime.getTime() - TStarttime.getTime();
	        long TdiffSeconds = Tdiff / 1000 % 60;
	        long TdiffMinutes = Tdiff / (60 * 1000) % 60;
	        long TdiffHours = Tdiff / (60 * 60 * 1000);
	        String ExecutionTime=TdiffHours+":"+TdiffMinutes+":"+TdiffSeconds;

//	        g.drawString("TEST SCRIPT DETAILS", 450, 50);
//	        g.drawString("Test Run Name : " +  TName, 50, 125);
//	        g.drawString("Script Number : " +  ScriptNumber, 50, 200);
//	        g.drawString("Scenario Name :"+Scenario, 50, 275);
//	        g.drawString("Status : "+status, 50, 350);
//	        g.drawString("Executed By :"+ExeBy, 50, 425);
//	        g.drawImage(logo,1150,15,null);
//////	    g.drawString("Start Time :"+TStarttime1, 50, 425);
//////	    g.drawString("End Time :"+endtime1, 50, 500);
//////	    g.drawString("Execution Time : "+ExecutionTime, 50, 575);
//	        g.dispose();
//	        ImageIO.write(image, "jpg", new File("/u01/oracle/selenium/apache-tomcat-8.5.60/webapps/wats/WEB-INF/classes/Images/"+imagename+".jpg"));
	        //ImageIO.write(image, "jpg", new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\"+imagename+".jpg"));
	        
	        BufferedImage image1 = null;
//	        image1 = ImageIO.read(file);
//	        Graphics g1 = image1.getGraphics();
//	        g1.setColor(Color.red);
	        java.awt.Font font1 = new java.awt.Font("Calibri", java.awt.Font.PLAIN, 36);
//	        g1.setFont(font1);
//	        g1.drawImage(logo,1012,14,null);
//	        g1.drawString("FAILED IN THE NEXT STEP!!", 400, 300);
//	        g1.dispose();
//	        ImageIO.write(image1, "jpg", new File("/u01/oracle/selenium/apache-tomcat-8.5.60/webapps/wats/WEB-INF/classes/Images/last.jpg"));
	        //ImageIO.write(image1, "jpg", new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\last.jpg"));
	        
	        BufferedImage image2 = null;
//	        image2 = ImageIO.read(file);
//	        Graphics g2 = image2.getGraphics();
//	        g2.setColor(Color.black);
//	        g2.setFont(font);
//	        g2.drawString("TEST RUN SUMMARY", 50, 50);
//	        g2.drawString("Test Run Name : " +  TName, 50, 125);
//            g2.drawString("Executed By :"+ExeBy, 50, 200);
//            g2.drawString("Start Time :"+TStarttime1, 50, 275);
//            g2.drawString("End Time :"+endtime1, 50, 350);
//            g2.drawString("Execution Time : "+ExecutionTime, 50,425);
//            g2.drawImage(logo,1012,15,null);
//	        g2.dispose();
//	       	ImageIO.write(image2, "jpg", new File("/u01/oracle/selenium/apache-tomcat-8.5.60/webapps/wats/WEB-INF/classes/Images/first.jpg"));
//	        String imgpath3 ="/u01/oracle/selenium/apache-tomcat-8.5.60/webapps/wats/WEB-INF/classes/Images/first.jpg";
//	        String imgpath2 ="/u01/oracle/selenium/apache-tomcat-8.5.60/webapps/wats/WEB-INF/classes/Images/";
	        
	        //ImageIO.write(image2, "jpg", new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\first.jpg"));
		    //String imgpath3 ="C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\first.jpg";
	        //String imgpath2 ="C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\";
//	        File f11=new File(imgpath2);
//	        File[] f22=f11.listFiles();
//	        File f44=new File(imgpath3);
//	        firstimagelink=f44.getAbsolutePath();
	      
			if (seqList.get(0).getName().endsWith("Failed.png")) {
				failcount++;
//				for(File f33:f22) {
//		        	if(f33.getAbsolutePath().contains(imagename)) {
//		        		linksall.add(f33.getAbsolutePath());
//		        		linksall.set(0,f33.getAbsolutePath());
//		        	}if(f33.getAbsolutePath().contains("last")) {
//		        		linksall.add(f33.getAbsolutePath());
//		        		linksall.add(f33.getAbsolutePath());  
//		        		linksall.set(1,f33.getAbsolutePath());
//
//	        	}
//		        }
//                                   System.out.println("SEQ : "+seqEntry.getKey());
//				links1.add(seqList.get(0).getAbsolutePath());
//				links1.add(linksall.get(1));
				seqFileNameList.add(seqList.get(0).getName());

				for (int i = 1; i < seqList.size(); i++) {

					if (!seqList.get(i).getName().endsWith("Failed.png")) {
//						  links1.add(seqList.get(i).getAbsolutePath());
						seqFileNameList.add(seqList.get(i).getName());

					} else {

						

					}

				}
//				links1.add(linksall.get(0));
				 Collections.reverse(links1);
				Collections.reverse(seqFileNameList);
				links.addAll(links1);
				targetFileList.addAll(seqFileNameList);

			}

//                    targetFileList.addAll(seqList);

		}
//
//		/*
//		 * for (String fileName : targetFileList) {
//		 * 
//		 * System.out.println("Target File : " + fileName);
//		 * 
//		 * }
//		 */
		fetchConfigVO.setPasscount(passcount);
		fetchConfigVO.setFailcount(failcount);
		if(video_rec.equalsIgnoreCase("yes")) {
			convertJPGtoMovie(firstimagelink,links,fetchMetadataListVO,fetchConfigVO,"Failed_Video.mp4");
	          }
		return targetFileList;


	}

	public List<String> getDetailPdfNew(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO) throws Exception {

		File folder = new File(fetchConfigVO.getScreenshot_path() + fetchMetadataListVO.get(0).getCustomer_name() + "\\"
				+ fetchMetadataListVO.get(0).getTest_run_name() + "\\");
		//File folder=new File("C:\\Users\\Winfo Solutions\\Desktop\\test");
		File[] listOfFiles = folder.listFiles();
//		String video_rec=fetchConfigVO.getEnable_video();
		 String video_rec="no";
		Map<Integer, List<File>> filesMap = new TreeMap<>();
		int failcount=0;
		int passcount=0;
		for (File file : Arrays.asList(listOfFiles)) {

			Integer seqNum = Integer.valueOf(file.getName().substring(0, file.getName().indexOf('_')));

			if (!filesMap.containsKey(seqNum)) {

				filesMap.put(seqNum, new ArrayList<File>());

			}

			filesMap.get(seqNum).add(file);

		}

		List<String> targetFileList = new ArrayList<>();
		ArrayList<String> finalLinks = new ArrayList<String>();
		List<String> targetSuccessFileList = new ArrayList<>();
		ArrayList<String> links = new ArrayList<String>();
		ArrayList<String> links2 = new ArrayList<String>();
		List<String> targetFailedFileList = new ArrayList<>();
		String firstimagelink = null;
		String TName = fetchMetadataListVO.get(0).getTest_run_name();
		for (Entry<Integer, List<File>> seqEntry : filesMap.entrySet()) {

			List<File> seqList = seqEntry.getValue();

			Collections.sort(seqList, new Comparator<File>() {

				public int compare(File f1, File f2) {

					return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified()) * -1;

				}

			});

			List<String> seqFileNameList = new ArrayList<String>();
			ArrayList<String> links1 = new ArrayList<String>();
						
						ArrayList<String> linksall = new ArrayList<String>();
						

//						File file = new File("/u01/oracle/selenium/apache-tomcat-8.5.60/webapps/wats/WEB-INF/classes/Images/white.jpg");
//						File file1 = new File("/u01/oracle/selenium/apache-tomcat-8.5.60/webapps/wats/WEB-INF/classes/Images/WATS_LOGO.JPG");
						//File file = new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\white.jpg");
						//File file1=new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\WATS_LOGO.JPG");
				        BufferedImage image = null;
//				        image = ImageIO.read(file);
				        BufferedImage logo = null;
//				        logo = ImageIO.read(file1);
//				        Graphics g = image.getGraphics();
//				        g.setColor(Color.black);
				        java.awt.Font font=new java.awt.Font("Calibri",  java.awt.Font.PLAIN, 36);
//				        g.setFont(font);
				      
				        String details= seqList.get(0).getName();
				        String ScriptNumber = details.split("_")[3];
						String TestRun = details.split("_")[4];
						String Status = details.split("_")[6];
						String status = Status.split("\\.")[0];
						String Scenario = details.split("_")[2];
						String imagename=TestRun+ScriptNumber;
				      //String TName = fetchMetadataListVO.get(0).getTest_run_name();
				        Date endtime=fetchConfigVO.getEndtime();
				        Date TStarttime=fetchConfigVO.getStarttime1();
				        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
				        String TStarttime1=dateFormat.format(TStarttime);
//Changed the executed by variable
				        String ExeBy=fetchMetadataListVO.get(0).getExecuted_by();
				        String endtime1=dateFormat.format(endtime);
				        long Tdiff=endtime.getTime() - TStarttime.getTime();
				        long TdiffSeconds = Tdiff / 1000 % 60;
				        long TdiffMinutes = Tdiff / (60 * 1000) % 60;
				        long TdiffHours = Tdiff / (60 * 60 * 1000);
				        String ExecutionTime=TdiffHours+":"+TdiffMinutes+":"+TdiffSeconds;
//				        g.drawString("TEST SCRIPT DETAILS", 450, 50);
//				        g.drawString("Test Run Name : " +  TName, 50, 125);
//				        g.drawString("Script Number : " +  ScriptNumber, 50, 200);
//				        g.drawString("Scenario Name :"+Scenario, 50, 275);
//				        g.drawString("Status : "+status, 50, 350);
//				        g.drawString("Executed By :"+ExeBy, 50, 425);
//				        g.drawImage(logo,1012,15,null);
//				        //g.drawString("Start Time :"+TStarttime1, 50, 425);
//				        //g.drawString("End Time :"+endtime1, 50, 500);
//				        //g.drawString("Execution Time : "+ExecutionTime, 50, 575);
//				        g.dispose();
//				        ImageIO.write(image, "jpg", new File("/u01/oracle/selenium/apache-tomcat-8.5.60/webapps/wats/WEB-INF/classes/Images/"+imagename+".jpg"));
				        //ImageIO.write(image, "jpg", new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\"+imagename+".jpg"));
				        
				        BufferedImage image1 = null;
//				        image1 = ImageIO.read(file);
//				        Graphics g1 = image1.getGraphics();
//				        g1.setColor(Color.red);
//				        java.awt.Font font1 = new java.awt.Font("Calibri", java.awt.Font.PLAIN, 36);
//				        g1.setFont(font1);
//				        g1.drawString("FAILED IN THE NEXT STEP!!", 400, 300);
//				        g1.drawImage(logo,1012,14,null);
//				        g1.dispose();
				        
				        BufferedImage image2 = null;
//				        image2 = ImageIO.read(file);
//				        Graphics g2 = image2.getGraphics();
//				        g2.setColor(Color.black);
//				        g2.setFont(font);
//				        g2.drawString("TEST RUN SUMMARY", 450, 50);
//				        g2.drawString("Test Run Name : " +  TName, 50, 125);
//			            g2.drawString("Executed By :"+ExeBy, 50, 200);
//			            g2.drawString("Start Time :"+TStarttime1, 50, 275);
//			            g2.drawString("End Time :"+endtime1, 50, 350);
//			            g2.drawString("Execution Time : "+ExecutionTime, 50,425);
//			            g2.drawImage(logo,1012,15,null);
//				        g2.dispose();
//				       ImageIO.write(image2, "jpg", new File("/u01/oracle/selenium/apache-tomcat-8.5.60/webapps/wats/WEB-INF/classes/Images/first.jpg"));
				        //ImageIO.write(image2, "jpg", new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\first.jpg"));
//				       String imgpath2 ="/u01/oracle/selenium/apache-tomcat-8.5.60/webapps/wats/WEB-INF/classes/Images/";
				        //String imgpath2 ="C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\";
//				        String imgpath3 ="/u01/oracle/selenium/apache-tomcat-8.5.60/webapps/wats/WEB-INF/classes/Images/first.jpg";
				        //String imgpath3 ="C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\first.jpg";
//				        File f11=new File(imgpath2);
//				        File[] f22=f11.listFiles();
//				        File f44=new File(imgpath3);
//				        firstimagelink=f44.getAbsolutePath();

						if (!seqList.get(0).getName().endsWith("Failed.png")) {
							passcount++;
//							for(File f33:f22)
//					        {
//					        	if(f33.getAbsolutePath().contains(imagename)) {
//							  linksall.add(f33.getAbsolutePath());
//					        	}
//					        }
//							links1.add(seqList.get(0).getAbsolutePath());

							seqFileNameList.add(seqList.get(0).getName());

//			             	                      System.out.println("FIRST S STEP: "+seqList.get(0).getName());

							for (int i = 1; i < seqList.size(); i++) {

								if (!seqList.get(i).getName().endsWith("Failed.jpg")) {
//									links1.add(seqList.get(i).getAbsolutePath());

									seqFileNameList.add(seqList.get(i).getName());

//			                                                                 System.out.println("S STEP: "+seqList.get(i).getName());

								} else {

									

								}

							}
//							links1.add(linksall.get(0));
							Collections.reverse(links1);
							Collections.reverse(seqFileNameList);
							links.addAll(links1);
							targetSuccessFileList.addAll(seqFileNameList);

						} else {
							failcount++;
//							for(File f33:f22) {
//					        	if(f33.getAbsolutePath().contains(imagename)) {
//					        		linksall.add(f33.getAbsolutePath());
//					        		linksall.set(0,f33.getAbsolutePath());
//					        	}
//					        	if(f33.getAbsolutePath().contains("last")) {
//					        		linksall.add(f33.getAbsolutePath());
//					        		linksall.add(f33.getAbsolutePath());  
//					        		linksall.set(1,f33.getAbsolutePath());
//
//					        	}
//					        }
//			                                   System.out.println("SEQ : "+seqEntry.getKey());
//							links1.add(seqList.get(0).getAbsolutePath());
//							links1.add(linksall.get(1));
//			                                   System.out.println("SEQ : "+seqEntry.getKey());

							seqFileNameList.add(seqList.get(0).getName());

//			                                   System.out.println("FIRST F STEP: "+seqList.get(0).getName());

							for (int i = 1; i < seqList.size(); i++) {

								if (!seqList.get(i).getName().endsWith("Failed.png")) {
//									links1.add(seqList.get(i).getAbsolutePath());

									seqFileNameList.add(seqList.get(i).getName());

//			                                                                 System.out.println("F STEP: "+seqList.get(i).getName());

								} else {

									

								}

							}

//							links1.add(linksall.get(0));
							 Collections.reverse(links1);
							Collections.reverse(seqFileNameList);
//							  links2.addAll(links1);
							targetFailedFileList.addAll(seqFileNameList);


						}

//			                    targetFileList.addAll(seqList);

					}

					finalLinks.addAll(links);
					finalLinks.addAll(links2);
					targetFileList.addAll(targetSuccessFileList);

					targetFileList.addAll(targetFailedFileList);

					/*
					 * for (String fileName : targetFileList) {
					 * 
					 * System.out.println("Target File : " + fileName);
					 * 
					 * }
					 */
					fetchConfigVO.setPasscount(passcount);
					fetchConfigVO.setFailcount(failcount);
					if(video_rec.equalsIgnoreCase("Y")) {
						
						convertJPGtoMovie(firstimagelink,finalLinks,fetchMetadataListVO,fetchConfigVO,TName+".mp4");
				          }
					return targetFileList;
	}
	
	public  void convertJPGtoMovie(String targetFile1,List<String> targetFileList, List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO,String name)
    {
		 String vidPath = (fetchConfigVO.getPdf_path() + fetchMetadataListVO.get(0).getCustomer_name() + "\\"
					+ fetchMetadataListVO.get(0).getTest_run_name() + "\\"+name);
		// String vidPath="C:\\Testing\\ReportWinfo\\"+name;
		 String Folder = (fetchConfigVO.getPdf_path() + fetchMetadataListVO.get(0).getCustomer_name() + "\\"
					+ fetchMetadataListVO.get(0).getTest_run_name() + "\\");
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
		//String vidPath = "C:\\Users\\Winfo Solutions\\Desktop\\"+name;
        OpenCVFrameConverter.ToIplImage grabberConverter = new OpenCVFrameConverter.ToIplImage();
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(vidPath,1366,614);
        String str=null;
   	 IplImage ipl = cvLoadImage(str);
        try {
            recorder.setFrameRate(0.33);
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            recorder.setVideoBitrate(9000);
            recorder.setFormat("mp4");
            recorder.setVideoQuality(0); // maximum quality
            recorder.start();
//            for (int i=0;i<targetFileList.size();i++)
//            {
//           	 System.out.println(targetFileList.get(i));
//            }
            if(targetFile1 != null)
            {
           	 System.out.println(targetFile1);
           	 str=targetFile1;
           	 ipl = cvLoadImage(str);
           	 recorder.record(grabberConverter.convert(ipl));
            }
            for (String image : targetFileList) {
           	 System.out.println(image);
           	 str=image;
           	 ipl = cvLoadImage(str);
           	 recorder.record(grabberConverter.convert(ipl));             }
            recorder.stop();
            System.out.println("ok");
           }
           catch (org.bytedeco.javacv.FrameRecorder.Exception e){
              e.printStackTrace();
           }
    }

	public void createPdf(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO, String pdffileName,
			Date Starttime, Date endtime) throws IOException, DocumentException, com.itextpdf.text.DocumentException {
		try {
			String Date = DateUtils.getSysdate();
			String Folder = (fetchConfigVO.getPdf_path() + fetchMetadataListVO.get(0).getCustomer_name() + "\\"
					+ fetchMetadataListVO.get(0).getTest_run_name() + "\\");
			//String Folder="C:\\Users\\Winfo Solutions\\Desktop\\new\\";
//			String Folder = "/objstore/udgsup/UDG SUPPORT/UDG - PPM  (copy)/";
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
			//new change add ExecutedBy field
			String ExecutedBy = fetchMetadataListVO.get(0).getExecuted_by();
			String ScriptDescription1 = fetchMetadataListVO.get(0).getScenario_name();
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
			int passcount=fetchConfigVO.getPasscount();
			int failcount=fetchConfigVO.getFailcount();
//			Date Starttime = fetchConfigVO.getStarttime();
			Date Tendtime=fetchConfigVO.getEndtime();
			Date TStarttime=fetchConfigVO.getStarttime1();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:aa");
			
			String TStarttime1=dateFormat.format(TStarttime);
			String Tendtime1=dateFormat.format(Tendtime);
			  long Tdiff=Tendtime.getTime() - TStarttime.getTime();
			    long TdiffSeconds = Tdiff / 1000 % 60;
			    long TdiffMinutes = Tdiff / (60 * 1000) % 60;
			    long TdiffHours = Tdiff / (60 * 60 * 1000);

			Document document = new Document();
			String start = "Execution Summary";
			String pichart = "Pie-Chart";
			String Report="Execution Report";
			 Font bfBold12 = FontFactory.getFont("Arial", 23); 
			 Font fnt = FontFactory.getFont("Arial", 12);
			 Font bf12 = FontFactory.getFont("Arial", 23);
			 Font bf15 = FontFactory.getFont("Arial", 23, Font.UNDERLINE);
			 Font bf16 = FontFactory.getFont("Arial", 12, Font.UNDERLINE);
			 Font bf13 = FontFactory.getFont("Arial", 23, Font.UNDERLINE,BaseColor.GREEN);
			 Font bf14 = FontFactory.getFont("Arial", 23, Font.UNDERLINE,BaseColor.RED);
			 Font bfBold = FontFactory.getFont("Arial", 23,BaseColor.WHITE);
			 DefaultPieDataset dataSet = new DefaultPieDataset();
			PdfWriter writer = null;
			writer = PdfWriter.getInstance(document, new FileOutputStream(FILE));
			Rectangle one = new Rectangle(1360,800);
	        document.setPageSize(one);
			document.open();
			System.out.println("before enter Images/wats_icon.png1");
			Image img1 = Image.getInstance("C:\\Users\\opc\\Documents\\Images\\wats_icon.png");
				System.out.println("after enter Images/wats_icon.png1");

				img1.scalePercent(65, 68);
		         img1.setAlignment(Image.ALIGN_RIGHT);
//		start to create testrun level reports	
			if((passcount!=0||failcount!=0) &("Passed_Report.pdf".equalsIgnoreCase(pdffileName)||"Failed_Report.pdf".equalsIgnoreCase(pdffileName)||"Detailed_Report.pdf".equalsIgnoreCase(pdffileName))) {			
//	     Start testrun to add details like start and end time,testrun name 			
				String TestRun=test_Run_Name;
//				String ExecutedBy=fetchConfigVO.getApplication_user_name();
				String StartTime=TStarttime1;
				String EndTime=Tendtime1;
				String ExecutionTime=TdiffHours+":"+TdiffMinutes+":"+TdiffSeconds;

				String TR = "Test Run Name";
				String SN = "Executed By" ;
				String SN1 = "Start Time";
				String S1 = "End Time";
				String Scenarios1 = "Execution Time";

				document.add(img1);
				document.add(new Paragraph(Report,bfBold12));
				document.add(Chunk.NEWLINE);
				PdfPTable table1 = new PdfPTable(2); 
				 table1.setWidths(new int[]{1, 1});
				 table1.setWidthPercentage(100f);			 
				 insertCell(table1, TR, Element.ALIGN_LEFT, 1, bf12);
				 insertCell(table1, TestRun, Element.ALIGN_LEFT, 1, bf12);
				 insertCell(table1, SN, Element.ALIGN_LEFT, 1, bf12);
				 insertCell(table1, ExecutedBy, Element.ALIGN_LEFT, 1, bf12);
				 insertCell(table1, SN1, Element.ALIGN_LEFT, 1, bf12);
				 insertCell(table1, StartTime, Element.ALIGN_LEFT, 1, bf12);
				 insertCell(table1, S1, Element.ALIGN_LEFT, 1, bf12);
				 insertCell(table1, EndTime, Element.ALIGN_LEFT, 1, bf12);
				 insertCell(table1, Scenarios1, Element.ALIGN_LEFT, 1, bf12);
				 insertCell(table1, ExecutionTime, Element.ALIGN_LEFT, 1, bf12);
				 document.add(table1);
//	   End testrun to add details like start and end time,testrun name 	
 
//					Start Testrun to add Table and piechart 		 
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
				
					
					 Rectangle one1 = new Rectangle(1360,1000);
				        document.setPageSize(one1);
				       
					 document.newPage();
					 document.add(img1);
			        document.add(new Paragraph(start, bfBold12));
			        document.add(Chunk.NEWLINE);
			   	 DecimalFormat df1 = new DecimalFormat("0");
			   	 DecimalFormat df2 = new DecimalFormat("0");
//			Start Testrun to add Table   	 
			   	PdfPTable table = new PdfPTable(3); 
				 table.setWidths(new int[]{1, 1, 1});
				 table.setWidthPercentage(100f);
				 insertCell(table, "Status", Element.ALIGN_CENTER, 1, bfBold12);
			     insertCell(table, "Total", Element.ALIGN_CENTER, 1, bfBold12);
			     insertCell(table, "Percentage", Element.ALIGN_CENTER, 1, bfBold12);
			     PdfPCell[] cells1 = table.getRow(0).getCells(); 
				  for (int k=0;k<cells1.length;k++){
				     cells1[k].setBackgroundColor(new BaseColor(161, 190, 212));
				  }
					if("Passed_Report.pdf".equalsIgnoreCase(pdffileName)) {			

					     insertCell(table, "Passed", Element.ALIGN_CENTER, 1, bf12);
					     insertCell(table, df1.format(passcount),  Element.ALIGN_CENTER, 1, bf12);
					     insertCell(table,df2.format(pass)+"%",  Element.ALIGN_CENTER, 1, bf12);
							}else if("Failed_Report.pdf".equalsIgnoreCase(pdffileName)) {
					     insertCell(table, "Failed", Element.ALIGN_CENTER, 1, bf12);
					     insertCell(table, df1.format(failcount),  Element.ALIGN_CENTER, 1, bf12);
					     insertCell(table, df2.format(fail)+"%",  Element.ALIGN_CENTER, 1, bf12);
							}else {
								insertCell(table, "Passed", Element.ALIGN_CENTER, 1, bf12);
							     insertCell(table, df1.format(passcount),  Element.ALIGN_CENTER, 1, bf12);
							     insertCell(table,df2.format(pass)+"%",  Element.ALIGN_CENTER, 1, bf12);
							     insertCell(table, "Failed", Element.ALIGN_CENTER, 1, bf12);
							     insertCell(table, df1.format(failcount),  Element.ALIGN_CENTER, 1, bf12);
							     insertCell(table, df2.format(fail)+"%",  Element.ALIGN_CENTER, 1, bf12);
							}
			     document.setMargins(20, 20, 20, 20);
			     document.add(table);
//			End Testrun to add Table
//			Start Testrun to add piechart 
			     if("Detailed_Report.pdf".equalsIgnoreCase(pdffileName)) {
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
				   
					   PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator(
					    		  "{2}", new DecimalFormat("0"), new DecimalFormat("0%")) ;
					  piePlot.setLabelGenerator(gen);
					  legend.setPosition(RectangleEdge.RIGHT);
					   legend.setVerticalAlignment(VerticalAlignment.CENTER);
				   piePlot.setInsets(new RectangleInsets(0.0, 5.0, 5.0, 5.0));
				   legend.setFrame(BlockBorder.NONE);
				   legend.setFrame(new LineBorder(Color.white, new BasicStroke(20f),
						    new RectangleInsets(1.0, 1.0, 1.0, 1.0)));
				   
				   java.awt.Font pass1=new java.awt.Font("", Font.NORMAL, 22);
						  legend.setItemFont(pass1);
						  PdfContentByte contentByte = writer.getDirectContent();
							PdfTemplate template = contentByte.createTemplate(1000, 900);
							Graphics2D graphics2d = template.createGraphics(700, 400,
									new DefaultFontMapper());
							Rectangle2D rectangle2d = new Rectangle2D.Double(0, 0, 600,
									400);
							chart.draw(graphics2d, rectangle2d);
							graphics2d.dispose();
					 		contentByte.addTemplate(template, 400, 100);
			     }
//			 End Testrun to add piechart 
// End Testrun to add Table and piechart 
//					 		Start to add page heading,all testrun names and states and page numbers	 		
					 		int k=0,l=0;
							String sno1 = "";
							Map<Integer, Map<String,String>> toc = new TreeMap<>();
							
							Map<String, String> toc2 = new TreeMap<>();
					 		for (String image : fileNameList) {
					 			k++;
					 			String sndo = image.split("_")[0];
					 			String name = image.split("_")[3];
					 			if(!sndo.equalsIgnoreCase(sno1)) {
					 				Map<String, String> toc1 = new TreeMap<>();
//					 				l=0;
					 				for (String image1 : fileNameList) {
					 					String Status = image1.split("_")[6];
					 					String status = Status.split("\\.")[0];
//					 					l++;
					 					if(image1.startsWith(sndo+"_")&&image1.contains("Failed")) {
					 						
//					 						toc2.put(sndo,String.valueOf(l-2));	
					 						toc2.put(sndo,"Failed"+l);	
					 						l++;
					 						}
					 					}
					 				
					 				String str=String.valueOf(toc2.get(sndo));
					 				toc1.put(name, str);
					 				 toc.put(k,toc1);
					 				
					 			}
					 			if (sndo!=null){
									sno1=sndo;
								}	
					 		}	
					 		sno1 = "";
					 		 document.newPage();
							 document.add(img1);
//				Start to add page heading 
					 		Anchor target2 = new Anchor(String.valueOf("Page Numbers"),bfBold);
						    target2.setName(String.valueOf("details"));
						    Chunk ch1 = new Chunk(String.format("Script Numbers"), bfBold);
						    ch1.setBackground(new BaseColor(38, 99, 175), 0f, 10f, 1730f, 15f);  
						    Paragraph p2 = new Paragraph();
						    p2.add(ch1);
						    p2.add(new Chunk(new VerticalPositionMark()));
						    p2.add(target2);
						    document.add(p2);
						    document.add(Chunk.NEWLINE);
//				End to add page heading 
						    
//			 Start to add all testrun names and states and page numbers	
					 	   Chunk dottedLine = new Chunk(new DottedLineSeparator());
					 		for (Entry<Integer, Map<String,String>> entry : toc.entrySet()) {
					 			Map<String,String> str1=entry.getValue();
					 			for(Entry<String, String> entry1:str1.entrySet()) {
					 	      Anchor click = new Anchor(String.valueOf(entry.getKey()),bf15);
					 		    click.setReference("#"+String.valueOf(entry1.getKey()));
					 		   Anchor click1 = new Anchor(String.valueOf("(Failed)"),bf14);
					 		   click1.setReference("#"+String.valueOf(entry1.getValue()));
					 		    Paragraph pr = new Paragraph();
					 		    int value=entry.getKey();
					 		   Anchor ca1 = new Anchor(String.valueOf(entry1.getKey()), bf15);
					 		  ca1.setReference("#"+String.valueOf(entry1.getKey()));
					 		  String compare=entry1.getValue();
		                     if(!compare.equals("null")) {
					 		   pr.add(ca1);
					 		  
					 		  pr.add(click1);
					 		    pr.add(dottedLine);
					 		    pr.add(click);
					 		   document.add(Chunk.NEWLINE);
					 		    document.add(pr);
		                     }else {
		                    	 Anchor click2 = new Anchor(String.valueOf("(Passed)"),bf13);
		                    	 click2.setReference("#"+String.valueOf(entry1.getKey()));
		                    	 pr.add(ca1);
		  			 		   pr.add(click2);
		  			 		    pr.add(dottedLine);
		  			 		    pr.add(click);
		  			 		   document.add(Chunk.NEWLINE);
		  			 		    document.add(pr);
		                     }
					 			}  
					 		}
//			 End to add all testrun names and states and page numbers
//			 End to add page heading,add all testrun names and states and page numbers	


//	Start to add script details, screenshoots and pagenumbers and wats icon	
			int i=0,j=0; 
			for (String image : fileNameList) {
						i++;
				Image img = Image.getInstance(
						fetchConfigVO.getScreenshot_path() + customer_Name + "/" + test_Run_Name + "/" + image);
//	Start to add script details 
				String sno = image.split("_")[0];
				String SNO = "Script Number";
				String ScriptNumber = image.split("_")[3];
				String SNM = "Scenario Name";
				String ScriptName = image.split("_")[2];
				String testRunName=image.split("_")[4];
//				String scrtipt=;
				if(!sno.equalsIgnoreCase(sno1)) {
					document.setPageSize(img);
					document.newPage();
					 document.add(img1);
					 Anchor target3 = new Anchor("Script Details",bf12);
					    target3.setName(ScriptNumber);
					    Paragraph pa=new Paragraph();
					    pa.add(target3);
					document.add(pa);
					document.add(Chunk.NEWLINE);
					PdfPTable table2 = new PdfPTable(2); 
					 table2.setWidths(new int[]{1, 1});
					 table2.setWidthPercentage(100f);
					 insertCell(table2, SNO, Element.ALIGN_LEFT, 1, bf12);
					 insertCell(table2, ScriptNumber, Element.ALIGN_LEFT, 1, bf12);
					 insertCell(table2, SNM, Element.ALIGN_LEFT, 1, bf12);
					 insertCell(table2, ScriptName, Element.ALIGN_LEFT, 1, bf12);
					 
					 for(Entry<String, String> entry1:toc.get(i).entrySet()) {
						 String str=entry1.getValue();
						 if(!str.equals("null")) {
								 insertCell(table2, "Status", Element.ALIGN_LEFT, 1, bf12);
						         insertCell(table2, "Failed", Element.ALIGN_LEFT, 1, bf12);
					        }else {
								 insertCell(table2, "Status", Element.ALIGN_LEFT, 1, bf12);
								 insertCell(table2, "Passed", Element.ALIGN_LEFT, 1, bf12);
					        }
					 }
					
					 document.add(table2);
					 
				}
				if (sno!=null){
					sno1=sno;
				}
//	End to add script details 
				
//	Start to add  screenshoots and pagenumbers and wats icon		 		
//				String TestRun = image.split("_")[4];
				String Status = image.split("_")[6];
				String status = Status.split("\\.")[0];
				String Scenario = image.split("_")[2];

//				String TR = "Test Run Name:" + " " + TestRun;
//				String SN = "Script Number:" + " " + ScriptNumber;
				String S = "Status:" + " " + status;
				String Scenarios = "Scenario Name :" + "" + Scenario;
				 String sndo = image.split("_")[0];
				 img1.scalePercent(65, 68);
					
		         img1.setAlignment(Image.ALIGN_RIGHT);
				 //new change-failed pdf to set pagesize 
				if(image.startsWith(sndo+"_")&&image.contains("Failed")) {
//					Rectangle one2 = new Rectangle(1360,1000);
			        document.setPageSize(one1); 
				 document.newPage();
				}	else {
					
		         document.setPageSize(img);
		         document.newPage();
				}
		         document.add(img1);
		         document.add(new Paragraph(Scenarios, fnt));
		         String Reason = image.split("_")[5];

		         String Message = "Failed at Line Number:" + ""+ Reason;
				 //new change-database to get error message
		         String error=databaseentry.getErrorMessage(sndo,ScriptNumber,testRunName,fetchConfigVO);
					String errorMessage = "Failed Message:" + ""+error; 
		         Paragraph pr1=new Paragraph();
		         pr1.add("Status:");
		       
			if(image.startsWith(sndo+"_")&&image.contains("Failed")) {
		        Anchor target1 = new Anchor(status);
			    target1.setName(String.valueOf(status+j));
			    j++; 
		        pr1.add(target1);
		        document.add(pr1);
		        document.add(new Paragraph(Message, fnt));
		        if(error!=null) {
					document.add(new Paragraph(errorMessage, fnt));
					}
					document.add(Chunk.NEWLINE);
					img.setAlignment(Image.ALIGN_CENTER);
					img.isScaleToFitHeight();
					//new change-change page size
					img.scalePercent(60,60);
					document.add(img);
	
			}else {
					 Anchor target1 = new Anchor(status);
					    target1.setName(String.valueOf(status));
				        pr1.add(target1);
				        document.add(pr1);
						img.setAlignment(Image.ALIGN_CENTER);
						img.isScaleToFitHeight();
						//new change-change page size
						img.scalePercent(60,68);
						document.add(img);
				}   
		
               
				Anchor target = new Anchor(String.valueOf(i));
			    target.setName(String.valueOf(i));
				Anchor target1 = new Anchor(String.valueOf("Back to Index"),bf16);
				target1.setReference("#"+String.valueOf("details"));
				Paragraph p=new Paragraph();
				p.add(target1);
				p.add(new Chunk(new VerticalPositionMark()));
				p.add(" page ");
				p.add(target);
				p.add(" of "+fileNameList.size());
//				img.setAlignment(Image.ALIGN_CENTER);
//				img.isScaleToFitHeight();
//				img.scalePercent(60, 71);
//				document.add(img);
				document.add(p);
				System.out.println("This Image " + "" + image + "" + "was added to the report");
//	End to add  screenshots and pagenumbers and wats icon		 		
//	End to add script details, screenshoots and pagenumbers and wats icon		 		
//  End to create testrun level reports	
			}
			}else {	
//  Start to create Script level passed reports		
//  Start to add Script level details		
				if(!("Passed_Report.pdf".equalsIgnoreCase(pdffileName)||"Failed_Report.pdf".equalsIgnoreCase(pdffileName)||"Detailed_Report.pdf".equalsIgnoreCase(pdffileName))){
					String Starttime1=dateFormat.format(Starttime);
					String endtime1=dateFormat.format(endtime);
					long  diff=endtime.getTime() - Starttime.getTime();
					 long diffSeconds = diff / 1000 % 60;
					    long diffMinutes = diff / (60 * 1000) % 60;
					    long diffHours = diff / (60 * 60 * 1000);
					String TestRun=test_Run_Name;
					String ScriptNumber=Script_Number;
					String ScriptNumber1=Scenario_Name;
					String Scenario1=fetchConfigVO.getStatus1();
//					String ExecutedBy=fetchConfigVO.getApplication_user_name();
					String StartTime=Starttime1;
					String EndTime=endtime1;
					String ExecutionTime=diffHours+":"+diffMinutes+":"+diffSeconds;
				
				String TR = "Test Run Name";
				String SN = "Script Number";
				String SN1 = "Scenario name";
				String Scenarios1 = "Status ";
				String EB = "Executed By" ;
				String ST = "Start Time";
				String ET = "End Time" ;
				String EX = "Execution Time";
				 document.add(img1);

				document.add(new Paragraph(Report,bfBold12));
				document.add(Chunk.NEWLINE);
				PdfPTable table1 = new PdfPTable(2); 
				 table1.setWidths(new int[]{1, 1});
				 table1.setWidthPercentage(100f);
			 
				 insertCell(table1, TR, Element.ALIGN_LEFT, 1, bf12);
				 insertCell(table1, TestRun, Element.ALIGN_LEFT, 1, bf12);
				 insertCell(table1, SN, Element.ALIGN_LEFT, 1, bf12);
				 insertCell(table1, ScriptNumber, Element.ALIGN_LEFT, 1, bf12);
				 insertCell(table1, SN1, Element.ALIGN_LEFT, 1, bf12);
				 insertCell(table1, ScriptNumber1, Element.ALIGN_LEFT, 1, bf12);
				 insertCell(table1, Scenarios1, Element.ALIGN_LEFT, 1, bf12);
				 insertCell(table1, Scenario1, Element.ALIGN_LEFT, 1, bf12);
				 insertCell(table1, EB, Element.ALIGN_LEFT, 1, bf12);
				 insertCell(table1, ExecutedBy, Element.ALIGN_LEFT, 1, bf12);
				 insertCell(table1, ST, Element.ALIGN_LEFT, 1, bf12);
				 insertCell(table1, StartTime, Element.ALIGN_LEFT, 1, bf12);
				 insertCell(table1, ET, Element.ALIGN_LEFT, 1, bf12);
				 insertCell(table1, EndTime, Element.ALIGN_LEFT, 1, bf12);
				 insertCell(table1, EX, Element.ALIGN_LEFT, 1, bf12);
				 insertCell(table1, ExecutionTime, Element.ALIGN_LEFT, 1, bf12);
				 document.add(table1);
				 document.newPage();	
//  End to add Script level details
				 
//	Start to add screenshoots and pagenumbers and wats icon		 		
		int i=0;
			for (String image : fileNameList) {
//				 Image img = Image.getInstance(
//				 fetchConfigVO.getScreenshot_path() + customer_Name + "\\" + test_Run_Name +
//				 "\\" + image);
				i++;
				Image img = Image.getInstance(
						fetchConfigVO.getScreenshot_path() + customer_Name + "/" + test_Run_Name + "/" + image);

				String Status = image.split("_")[6];
				String status = Status.split("\\.")[0];
				String Scenario = image.split("_")[2];

				document.setPageSize(img);
				document.newPage();

				String S = "Status:" + " " + status;
				String Scenarios = "Scenario Name :" + "" + Scenario;
				img1.scalePercent(65, 65);
		         img1.setAlignment(Image.ALIGN_RIGHT);
		        document.add(img1);
				document.add(new Paragraph(S, fnt));
				document.add(new Paragraph(Scenarios, fnt));
				document.add(Chunk.NEWLINE);
				
				Paragraph p=new Paragraph(String.format("page %s of %s", i, fileNameList.size()));
				p.setAlignment(Element.ALIGN_RIGHT);
				img.setAlignment(Image.ALIGN_CENTER);
				img.isScaleToFitHeight();
				//new change-change page size
				img.scalePercent(60, 64);
				document.add(img);
				document.add(p);
				System.out.println("This Image " + "" + image + "" + "was added to the report");
//		End to add screenshoots and pagenumbers and wats icon
//  End to create Script level passed reports		

			}
			}
			}
			document.close();
//			compress(fetchMetadataListVO, fetchConfigVO, pdffileName);
			
			} catch (Exception e) {
			System.out.println("Not able to Create pdf"+e);
		}
	}
	 public  void insertCell(PdfPTable table, String text, int align, int colspan, Font font){
	  	   
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
//	  	else if() {
//	  	 cell.setBorderWidthRight(0.3f);
//	  	cell.setBorderColorRight(new BaseColor(230, 225, 225));
//	  		cell.setBorderWidthBottom(0.3f);
//	  		cell.setBorderColorBottom(new BaseColor(230, 225, 225));
//	  	}
	  	else {
	  		cell.setBorderWidthLeft(0.3f);
	  		cell.setBorderColorLeft(new BaseColor(230, 225, 225));
	  		cell.setBorderWidthTop(0.3f); 
	  		cell.setBorderColorTop(new BaseColor(230, 225, 225));
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

	 public void createFailedPdf(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO,
				String pdffileName,Date Starttime,Date endtime) throws IOException, DocumentException, com.itextpdf.text.DocumentException {
			try {
				String Date = DateUtils.getSysdate();
				String Folder = (fetchConfigVO.getPdf_path() + fetchMetadataListVO.get(0).getCustomer_name() + "\\"
						+ fetchMetadataListVO.get(0).getTest_run_name() + "\\");
				String FILE = (Folder + pdffileName);
				System.out.println(FILE);
				List<String> fileNameList = null;
				if ("Passed_Report.pdf".equalsIgnoreCase(pdffileName)) {
					fileNameList = getPassedPdfNew(fetchMetadataListVO, fetchConfigVO);
				} 
				else if ("Failed_Report.pdf".equalsIgnoreCase(pdffileName)) {
					fileNameList = getFailedPdfNew(fetchMetadataListVO, fetchConfigVO);
				}
				if ("Detailed_Report.pdf".equalsIgnoreCase(pdffileName)) {
					fileNameList = getDetailPdfNew(fetchMetadataListVO, fetchConfigVO);
				} else {
					fileNameList = getFailFileNameListNew(fetchMetadataListVO, fetchConfigVO);
				}
				
				String Script_Number = fetchMetadataListVO.get(0).getScript_number();
				String customer_Name = fetchMetadataListVO.get(0).getCustomer_name();
				String test_Run_Name = fetchMetadataListVO.get(0).getTest_run_name();
				String Scenario_Name = fetchMetadataListVO.get(0).getScenario_name();
				//new change add ExecutedBy field
				String ExecutedBy = fetchMetadataListVO.get(0).getExecuted_by();	
				String ScriptDescription1 = fetchMetadataListVO.get(0).getScenario_name();
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
				 Font bf12 = FontFactory.getFont("Arial", 23);
					System.out.println("before enter Images/wats_icon.png");
					Image img1 = Image.getInstance("C:\\Users\\opc\\Documents\\Images\\wats_icon.png");
						System.out.println("after enter Images/wats_icon.png");
				 img1.scalePercent(65, 68);
		         img1.setAlignment(Image.ALIGN_RIGHT);
				 Font bfBold12 = FontFactory.getFont("Arial", 23); 
				 String Report="Execution Report";
				 Font fnt = FontFactory.getFont("Arial", 12);
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:aa");				String Starttime1=dateFormat.format(Starttime);
					String endtime1=dateFormat.format(endtime);
					long diff=endtime.getTime() - Starttime.getTime();
					 long diffSeconds = diff / 1000 % 60;
					    long diffMinutes = diff / (60 * 1000) % 60;
					    long diffHours = diff / (60 * 60 * 1000);
				Document document = new Document();
				PdfWriter.getInstance(document, new FileOutputStream(FILE));
				Rectangle one = new Rectangle(1360,800);
		        document.setPageSize(one);
				document.open();
				String TestRun=test_Run_Name;
				String ScriptNumber=Script_Number;
				String ScriptNumber1=Scenario_Name;
				String Scenario1=fetchConfigVO.getStatus1();
//				String ExecutedBy=fetchConfigVO.getApplication_user_name();
				String StartTime=Starttime1;
				String EndTime=endtime1;
				String ExecutionTime=diffHours+":"+diffMinutes+":"+diffSeconds;
			
			String TR = "Test Run Name";
			String SN = "Script Number";
			String SN1 = "Scenario Name";
			String Scenarios1 = "Status ";
			String EB = "Executed By" ;
			String ST = "Start Time";
			String ET = "End Time" ;
			String EX = "Execution Time";
		
			 document.add(img1);

			document.add(new Paragraph(Report,bfBold12));
			document.add(Chunk.NEWLINE);
			PdfPTable table1 = new PdfPTable(2); 
			 table1.setWidths(new int[]{1, 1});
			 table1.setWidthPercentage(100f);
		 
			 insertCell(table1, TR, Element.ALIGN_LEFT, 1, bf12);
			 insertCell(table1, TestRun, Element.ALIGN_LEFT, 1, bf12);
			 insertCell(table1, SN, Element.ALIGN_LEFT, 1, bf12);
			 insertCell(table1, ScriptNumber, Element.ALIGN_LEFT, 1, bf12);
			 insertCell(table1, SN1, Element.ALIGN_LEFT, 1, bf12);
			 insertCell(table1, ScriptNumber1, Element.ALIGN_LEFT, 1, bf12);
			 insertCell(table1, Scenarios1, Element.ALIGN_LEFT, 1, bf12);
			 insertCell(table1, Scenario1, Element.ALIGN_LEFT, 1, bf12);
			 insertCell(table1, EB, Element.ALIGN_LEFT, 1, bf12);
			 insertCell(table1, ExecutedBy, Element.ALIGN_LEFT, 1, bf12);
			 insertCell(table1, ST, Element.ALIGN_LEFT, 1, bf12);
			 insertCell(table1, StartTime, Element.ALIGN_LEFT, 1, bf12);
			 insertCell(table1, ET, Element.ALIGN_LEFT, 1, bf12);
			 insertCell(table1, EndTime, Element.ALIGN_LEFT, 1, bf12);
			 insertCell(table1, EX, Element.ALIGN_LEFT, 1, bf12);
			 insertCell(table1, ExecutionTime, Element.ALIGN_LEFT, 1, bf12);
			 document.add(table1);
			 document.newPage();	
	//End to add Script level details
//					Start to add screenshoots and pagenumbers and wats icon		 		
					int i=0;
						for (String image : fileNameList) {
							i++;
							Image img = Image.getInstance(
									fetchConfigVO.getScreenshot_path() + customer_Name + "/" + test_Run_Name + "/" + image);

//							String ScriptNumber = image.split("_")[3];
//							String TestRun = image.split("_")[4];
							String Status = image.split("_")[6];
							String status = Status.split("\\.")[0];
							String Scenario = image.split("_")[2];
							
							if (status.equalsIgnoreCase("Failed")) {//							Rectangle one2 = new Rectangle(1360,1000);
						        document.setPageSize(one); 
							 document.newPage();
							}	else {
								
					         document.setPageSize(img);
					         document.newPage();
							}
						
							document.add(img1);
							String Reason = image.split("_")[5];
						//						String TR = "Test Run Name:" + " " + TestRun;
//							String SN = "Script Number:" + " " + ScriptNumber;
							String S = "Status:" + " " + status;
//							String Scenarios = "Scenario Name :" + "" + Scenario;
							String Message = "Failed at Line Number:" + ""+ Reason;
							String errorMessage = "Failed Message:" + ""+ fetchConfigVO.getErrormessage();
							// String message = "Failed at
							// :"+fetchMetadataListVO.get(0).getInput_parameter();
//							document.add(new Paragraph(TR, fnt));
//							document.add(new Paragraph(SN, fnt));
							document.add(new Paragraph(S, fnt));
//							document.add(new Paragraph(Scenarios, fnt));
	//new change-failed pdf to add pagesize
							if (status.equalsIgnoreCase("Failed")) {
								document.add(new Paragraph(Message, fnt));
								if(fetchConfigVO.getErrormessage()!=null) {
								document.add(new Paragraph(errorMessage, fnt));
								}
								document.add(Chunk.NEWLINE);
								img.setAlignment(Image.ALIGN_CENTER);
								img.isScaleToFitHeight();
								//new change-change page size
								img.scalePercent(60,60);
								document.add(img);
							}else {
								document.add(Chunk.NEWLINE);
								img.setAlignment(Image.ALIGN_CENTER);
								img.isScaleToFitHeight();
								//new change-change page size
								img.scalePercent(60,68);
								document.add(img);
							}
							
											
							Paragraph p=new Paragraph(String.format("page %s of %s", i, fileNameList.size()));
							p.setAlignment(Element.ALIGN_RIGHT);
							
							
							document.add(p);
							System.out.println("This Image " + "" + image + "" + "was added to the report");
//					End to add screenshoots and pagenumbers and wats icon
			//  End to create Script level passed reports		

						}
				document.close();
				compress(fetchMetadataListVO, fetchConfigVO, pdffileName);
			} catch (Exception e) {
				System.out.println("Not able to upload the pdf"+e);
			}
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

//			BufferedImage originalImage = ImageIO.read(new File(Folder+image));
//			int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

//			BufferedImage resizeImageGif = resizeImage(originalImage, type);
//			ImageIO.write(resizeImageGif, "jpg", new File("C:\\Kaushik"+"\\"+image));

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
	public  void uploadPDF(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO){
		try {
			  String accessToken = getAccessTokenPdf();
			  List imageUrlList = new ArrayList();
			  File imageDir = new File(fetchConfigVO.getPdf_path()+fetchMetadataListVO.get(0).getCustomer_name() +"\\" +fetchMetadataListVO.get(0).getTest_run_name()+"\\");
			  System.out.println(imageDir);
			  for(File imageFile : imageDir.listFiles()){
				  String imageFileName = imageFile.getName();
				  System.out.println(imageFileName);
				  imageUrlList.add(imageFileName);
				  File pdfFile = new File(imageDir+"\\"+imageFileName);
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
			      //Outer header 
			      HttpHeaders uploadSessionHeader = new HttpHeaders();
	//			  uploadSessionHeader.setContentType(MediaType.APPLICATION_JSON);
				  uploadSessionHeader.add("Authorization", "Bearer " + accessToken);
				  
				  HttpEntity<byte[]> uploadSessionRequest = new HttpEntity<>(null, uploadSessionHeader);
				  ResponseEntity<Object> response = restTemplate
						  .exchange("https://graph.microsoft.com/v1.0/drives/b!KcGTxB8fRUOsVkFTx3_XQI27VIClhktAidGIE0ZEKfowr1GL3k-zRrQ5i52Xg3Jv/items/01NZEJ6GV6Y2GOVW7725BZO354PWSELRRZ:/ArloSelenium/"
								  + fetchMetadataListVO.get(0).getCustomer_name() + "\\" + fetchMetadataListVO.get(0).getTest_run_name()+"\\" + imageFileName
								  + ":/createUploadSession", HttpMethod.POST, uploadSessionRequest, Object.class);
				  System.out.println(response);
				  Map<String, Object> linkedMap = response.getBody() != null
						  ? (LinkedHashMap<String, Object>) response.getBody()
								  : null;
						  String uploadUrl = linkedMap != null ? StringUtils.convertToString(linkedMap.get("uploadUrl")) : null;

						  HttpHeaders uploadingFileHeader = new HttpHeaders();
						  uploadingFileHeader.setContentLength(data.length);
						  uploadingFileHeader.add("Content-Range","bytes " + 0 + "-" + (data.length - 1) + "/" + data.length);
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
	public  String getAccessTokenPdf(){
		  String acessToken = null;
		  try {
			  RestTemplate restTemplate = new RestTemplate();
			  HttpHeaders headers = new HttpHeaders();
			  headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			  MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
			  map.add("grant_type", "client_credentials");
			  map.add("client_id", "e3cac627-b21f-4c30-8de4-7765bc1618da");
			  map.add("client_secret", "XF.Xr78H26?rKNcvXdL=:[q.sni-oyFG");
			  map.add("scope", "https://graph.microsoft.com/.default");


			  HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
			  ResponseEntity<Object> response = restTemplate.exchange(
					  "https://login.microsoftonline.com/0e295999-ec13-4f09-9407-28050f7372cc/oauth2/v2.0/token",
					  HttpMethod.POST, entity, Object.class);
			  System.out.println(response);

			  @SuppressWarnings("unchecked")
			  Map<String, Object> linkedMap = response.getBody() != null ? (Map<String, Object>) response.getBody() : null;
			  acessToken = linkedMap != null ? StringUtils.convertToString(linkedMap.get("access_token")) : null;
		} catch (Exception e) {
			System.out.println(e);
		}
		  System.out.println(acessToken);
		  return acessToken;
	  }
	
	public void openFile(WebDriver driver, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) { 
		  try  
          {  
			  WebElement copy = driver.findElement(By.xpath("(//*[text()='Succeeded'])[1]/preceding::span[text()][1]"));
			  String number = copy.getText();
			  System.out.println(number);
	//		  String num = number.replaceAll("[^\\d.]+|\\.(?!\\d)", "");
	//		  System.out.println(num);
			  driver.get(fetchConfigVO.getDownlod_file_path()+"/"+number+".log");
			  Thread.sleep(2000);
			  screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			  driver.navigate().back();
			  Thread.sleep(8000);
          }catch(Exception e)  
		  {  
        	  System.out.println(e);
		  }  
	}
	
	public static File getLastModified(String directoryFilePath, FetchConfigVO fetchConfigVO)
	{
	    File directory = new File(fetchConfigVO.getDownlod_file_path());
	    File[] files = directory.listFiles(File::isFile);
	    long lastModifiedTime = Long.MIN_VALUE;
	    File chosenFile = null;

	    if (files != null)
	    {
	        for (File file : files)
	        {
	            if (file.lastModified() > lastModifiedTime)
	            {
	                chosenFile = file;
	                lastModifiedTime = file.lastModified();
	            }
	        }
	    }

	    return chosenFile;
	}
	
	public void openPdf(WebDriver driver, String path, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) { 
		  try  
        {  
			  File path1 = getLastModified(path, fetchConfigVO);
			  driver.get(""+path1);
			  Thread.sleep(2000);
			  screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			  driver.navigate().back();
			  Thread.sleep(8000);
        }catch(Exception e)  
		  {  
        	System.out.println(e);
		  }  
	}
	
	public void navigateToBackPage(WebDriver driver, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) { 
		try  
		{  
			Thread.sleep(5000);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			driver.navigate().back();
			Thread.sleep(8000);
		}catch(Exception e)  
		{  
			System.out.println(e);
		}  
	}
	
	public  void copy(WebDriver driver, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
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
	}
	public void paste(WebDriver driver, String inputParam, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO,
			String globalValueForSteps) throws Exception {
		try {
			Actions action = new Actions(driver);
			action.keyDown(Keys.CONTROL).sendKeys("v").build().perform();
		} catch (Exception e) {
			screenshotFail(driver, "Failed during paste Method", fetchMetadataVO, fetchConfigVO);
			throw e;
		}
}
	public  void clear(WebDriver driver, String inputParam, String inputParam2, FetchMetadataVO fetchMetadataVO,
			FetchConfigVO fetchConfigVO) {
		try { 
			WebElement waittill = driver.findElement(By.xpath("(//label[contains(text(),'"+inputParam+"')]/preceding-sibling::input)[1]"));
			clearMethod(driver, waittill);
    String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "(//label[contains(text(),'inputParam')]/preceding-sibling::input)[1]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		
			return;
		} catch (Exception e) {
			System.out.println(e);
		}try { 
			Thread.sleep(2000);
			WebElement waittill = driver.findElement(By.xpath("(//label[normalize-space(text())='"+inputParam+"']/following::input)[1]"));
			clearMethod(driver, waittill);
			Thread.sleep(2000);
            String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "(//label[normalize-space(text())='inputParam']/following::input)[1]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}try { 
			WebElement waittill = driver.findElement(By.xpath("(//*[normalize-space(text())='"+inputParam+"']/following::input)[1]"));
			clearMethod(driver, waittill);
			Thread.sleep(2000);
            String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "(//*[normalize-space(text())='inputParam']/following::input)[1]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}try {
			WebElement waittill = driver.findElement(By.xpath("//*[contains(@placeholder,'"+inputParam+"')]"));
			clearMethod(driver, waittill);
            String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "//*[contains(@placeholder,'inputParam')]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("//*[normalize-space(text())='"+inputParam+"']/following::textarea[1]"));
			clearMethod(driver, waittill);
            String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "//*[normalize-space(text())='inputParam']/following::textarea[1]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return;
		} catch (Exception e) {
			screenshotFail(driver, "Failed during clearAndType Method", fetchMetadataVO, fetchConfigVO);
			System.out.println(e);
			throw e;
		}
	}
	public  void windowclose(WebDriver driver, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) {
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

	public  void switchToActiveElement(WebDriver driver, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) {
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
	public void clickMenu(WebDriver driver, String param1, String param2, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
	if(param1.equalsIgnoreCase("Setup and Maintenance")){
		try { 
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[normalize-space(text())='Navigator']/following::*[normalize-space(text())='"+param1+"']")));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h1[normalize-space(text())='Navigator']/following::*[normalize-space(text())='"+param1+"']"), param1));
		WebElement waittext = driver.findElement(By.xpath("//h1[normalize-space(text())='Navigator']/following::*[normalize-space(text())='"+param1+"']"));
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//h1[normalize-space(text())='Navigator']/following::*[normalize-space(text())='param1']";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	} catch (Exception e) {
			System.out.println(e);
	}
	}
		try { 
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[normalize-space(text())='Navigator']/following::*[normalize-space(text())='"+param1+"']/following::a[normalize-space(text())='"+param2+"']")));
//		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h1[normalize-space(text())='Navigator']/following::*[normalize-space(text())='"+param1+"']/following::a[normalize-space(text())='"+param2+"']"), param2));
		WebElement waittext = driver.findElement(By.xpath("//h1[normalize-space(text())='Navigator']/following::*[normalize-space(text())='"+param1+"']/following::a[normalize-space(text())='"+param2+"']"));
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//h1[normalize-space(text())='Navigator']/following::*[normalize-space(text())='param1']/following::a[normalize-space(text())='param2']";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	} catch (Exception e) {
			System.out.println(e);
	}try { 
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//a[normalize-space(text())='" + param1 + "']"))));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(("//a[normalize-space(text())='" + param1 + "']")), param1));
		WebElement waittext = driver.findElement(By.xpath(("//a[normalize-space(text())='" + param1 + "']")));
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//a[normalize-space(text())='param1']";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}try { 
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//div[contains(@style,'display: block')]//div[text()='" + param1 + "']"))));
		WebElement waittext = driver.findElement(By.xpath(("//div[contains(@style,'display: block')]//div[text()='" + param1 + "']")));
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//div[contains(@style,'display: block')]//div[text()='param1 ']";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}
	try { 
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//div[normalize-space(text())='" + param1 + "']"))));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(("//div[normalize-space(text())='" + param1 + "']")), param1));
		WebElement waittext = driver.findElement(By.xpath(("//div[normalize-space(text())='" + param1 + "']")));
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//div[normalize-space(text())='param1 ']";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}
	try { 
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//div[contains(@id,'" + param1 + "')])[1]")));
		WebElement waittext = driver.findElement(By.xpath("(//div[contains(@id,'" + param1 + "')])[1]"));
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "(//div[contains(@id,'param1')])[1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	} catch (Exception e) {
		System.out.println(e);
		logger.error("Failed during Click action.");
		screenshotFail(driver, "Failed during clickLink Method", fetchMetadataVO, fetchConfigVO);
		throw e;
	}
}
	public void clickSignInSignOut(WebDriver driver, String param1, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
	try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//button[normalize-space(text())='" + param1 + "']"))));
		WebElement waittext = driver.findElement(By.xpath(("//button[normalize-space(text())='" + param1 + "']")));
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		return;
	} catch (Exception e) {
		System.out.println(e);
		logger.error("Failed during Click action.");
		screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, fetchConfigVO);
		throw e;
	}
}

	public void clickTaskLink(WebDriver driver, String param1, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
		try {
			Thread.sleep(2000);
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//a[normalize-space(text())='" + param1 + "'][1]"))));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(("//a[normalize-space(text())='" + param1 + "'][1]")), param1));
			WebElement waittext = driver.findElement(By.xpath(("//a[normalize-space(text())='" + param1 + "'][1]")));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			return;
		} catch (Exception e) {
			System.out.println(e);
			logger.error("Failed during Click action.");
			screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, fetchConfigVO);
			throw e;
		}
	}
public void clickButtonDropdown(WebDriver driver, String param1, String param2,String keysToSend, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
	try {
		if(param1.equalsIgnoreCase("Approvals") && param2.equalsIgnoreCase("Actions")) {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[normalize-space(text())='"+param1+"']/following::a[normalize-space(text())='"+param2+"'])[1]")));
		WebElement waittext = driver.findElement(By.xpath("(//h1[normalize-space(text())='"+param1+"']/following::a[normalize-space(text())='"+param2+"'])[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		clickButtonDropdownText(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO);
//		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "(//h1[normalize-space(text())='param1']/following::a[normalize-space(text())='param2'])[1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
		}
	}catch (Exception e) {
		System.out.println(e);
	}try {
		if(param2.equalsIgnoreCase("Publish to Managers")) {
			//updating [2] to [1] after 21A Execution
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())='"+param1+"']/following::a[normalize-space(text())='"+param2+"'])[2]")));
		WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())='"+param1+"']/following::a[normalize-space(text())='"+param2+"'])[2]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		Thread.sleep(3000);
		waittext.click();
//		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		clickButtonDropdownText(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO);
//		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "(//*[normalize-space(text())='param1']/following::a[normalize-space(text())='param2'])";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
		}
	}catch (Exception e) {
		System.out.println(e);
	}try {
		//updating budget method to budget pool after 21A Execution and [2] to [1]
		if(param1.equalsIgnoreCase("Budget Pool") && param2.equalsIgnoreCase("Actions")) {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())='"+param1+"']/following::a[normalize-space(text())='"+param2+"'])[2]")));
		WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())='"+param1+"']/following::a[normalize-space(text())='"+param2+"'])[2]"));
		waittext.click();
		clickButtonDropdownText(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
       String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "(//*[normalize-space(text())='param1']/following::a[normalize-space(text())='param2'])[1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
		}
	}catch (Exception e) {
		System.out.println(e);
	}try {if(param1.equalsIgnoreCase("Workforce Compensation") && param2.equalsIgnoreCase("Search")) {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[contains(text(),'"+param1+"')]/following::a[1]")));
		WebElement waittext = driver.findElement(By.xpath("//h1[contains(text(),'"+param1+"')]/following::a[1]"));
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
//		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		WebElement search = driver.findElement(By.xpath("//li[text()='Search...']"));
		search.click();
		WebElement name = driver.findElement(By.xpath("//*[text()='Search']/following::*[text()='Name']/following::input[1]"));
		name.sendKeys(keysToSend);
		enter(driver, fetchMetadataVO, fetchConfigVO);
		WebElement text = driver.findElement(By.xpath("//*[normalize-space(text())='Search']/following::a[normalize-space(text())='"+keysToSend+"']"));
		text.click();
		WebElement button = driver.findElement(By.xpath("//*[normalize-space(text())='Search']/following::*[normalize-space(text())='o'][1]"));
		button.click();	
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//h1[contains(text(),'param1')]/following::a[1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	}
	}catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[normalize-space(text())='"+param1+"']/following::a[@title='"+param2+"'])[1]")));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(("//h1[normalize-space(text())='"+param1+"']")), param1));
		Thread.sleep(6000);
		WebElement waittext = driver.findElement(By.xpath("(//h1[normalize-space(text())='"+param1+"']/following::a[@title='"+param2+"'])[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		clickButtonDropdownText(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO);
//		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "(//h1[normalize-space(text())='param1']/following::a[@title='param2'])[1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	}catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[normalize-space(text())='"+param1+"']/following::a[normalize-space(text())='"+param2+"'])[1]")));
		WebElement waittext = driver.findElement(By.xpath("(//h1[normalize-space(text())='"+param1+"']/following::a[normalize-space(text())='"+param2+"'])[1]"));
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		Thread.sleep(4000);
		WebElement values = driver.findElement(By.xpath("(//td[normalize-space(text())='"+keysToSend+"'])[2]"));
		clickValidateXpath(driver, fetchMetadataVO, values, fetchConfigVO);
//		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "(//h1[normalize-space(text())='param1']/following::a[normalize-space(text())='param2'])[1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	}catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@title='"+param1+"']")));
		WebElement waittext = driver.findElement(By.xpath("//a[@title='"+param1+"']"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		clickButtonDropdownText(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO);
//		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//a[@title='param1']";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	}catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[contains(text(),'"+param1+"')]/following::a[1]")));
		WebElement waittext = driver.findElement(By.xpath("//h1[contains(text(),'"+param1+"')]/following::a[1]"));
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
//		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		clickButtonDropdownText(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO);
       String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//h1[contains(text(),'param1')]/following::a[1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	}catch (Exception e) {
		System.out.println(e);
		logger.error("Failed during Click action.");
		screenshotFail(driver, "Failed during clickLink Method", fetchMetadataVO, fetchConfigVO);
		throw e;
	}
}
	public void clickButtonDropdownText(WebDriver driver, String param1, String keysToSend, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//li[normalize-space(text())='"+keysToSend+"']")));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(("//li[normalize-space(text())='"+keysToSend+"']")), keysToSend));
			Thread.sleep(5000);
			WebElement waittext = driver.findElement(By.xpath("//li[normalize-space(text())='"+keysToSend+"']"));
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			return;
		}catch (Exception e) {
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class,'PopupMenuContent')]//td[text()='"+keysToSend+"']")));
			WebElement waittext = driver.findElement(By.xpath("//div[contains(@class,'PopupMenuContent')]//td[text()='"+keysToSend+"']"));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			return;
		}catch (Exception e) {
			System.out.println(e);
		}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[normalize-space(text())='"+keysToSend+"']")));
		WebElement waittext = driver.findElement(By.xpath("//td[normalize-space(text())='"+keysToSend+"']"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		Thread.sleep(1000);
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		return;
	}catch (Exception e) {
		System.out.println(e);
		logger.error("Failed during Click action.");
		screenshotFail(driver, "Failed during clickLink Method", fetchMetadataVO, fetchConfigVO);
		throw e;
	}
}
	public void clickExpandorcollapse(WebDriver driver, String param1, String param2, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
	try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h2[normalize-space(text())='" + param1 + "']/following::*[@title='" + param2 + "'])[1]")));
		Thread.sleep(8000);
		WebElement waittext = driver.findElement(By.xpath("(//h2[normalize-space(text())='" + param1 + "']/following::*[@title='" + param2 + "'])[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "(//h2[normalize-space(text())=' param1']/following::*[@title=' param2 '])[1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	}catch (Exception e) {
		System.out.println(e);
	}
	try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='"+ param1 +"']/following::*[normalize-space(text())='"+ param2 +"']/preceding::*[@title='Expand' and @href and not(@style='display:none')][1]")));
		WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='"+ param1 +"']/following::*[normalize-space(text())='"+ param2 +"']/preceding::*[@title='Expand' and @href and not(@style='display:none')][1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//*[normalize-space(text())=' param1 ']/following::*[normalize-space(text())=' param2 ']/preceding::*[@title='Expand' and @href and not(@style='display:none')][1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	}catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())='" + param1 + "']/following::*[@title='" + param2 + "'])[1]")));
		Thread.sleep(4000);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[normalize-space(text())='" + param1 + "']"), param1));
		WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())='" + param1 + "']/following::*[@title='" + param2 + "'])[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		Thread.sleep(2000);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "(//*[normalize-space(text())=' param1 ']/following::*[@title=' param2 '])[1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	}catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())='" + param1 + "']/preceding::*[@title='" + param2 + "'])[1]")));
		Thread.sleep(4000);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[normalize-space(text())='" + param1 + "']"), param1));
		WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())='" + param1 + "']/preceding::*[@title='" + param2 + "'])[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		Thread.sleep(8000);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "(//*[normalize-space(text())='param1 ']/preceding::*[@title=' param2'])[1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	}catch (Exception e) {
		System.out.println(e);
		logger.error("Failed during Click action.");
		screenshotFail(driver, "Failed during clickExpandorcollapse", fetchMetadataVO, fetchConfigVO);
		throw e;
	} 
}
	public void selectAValue(WebDriver driver, String param1,String param2, String keysToSend, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
		try {if(param1.equalsIgnoreCase("Manager")){
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			Thread.sleep(6000);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(),'" + param1 + "')]/following::*[text()='"+keysToSend+"'][2]")));
			WebElement waittext = driver.findElement(By.xpath("//*[contains(text(),'" + param1 + "')]/following::*[text()='"+keysToSend+"'][2]"));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
            String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "//*[contains(text(),'param1')]/following::*[text()='keysToSend'][1]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return;
		}
		}catch (Exception e) {
			System.out.println(e);
		}try { 
			if(param1.equalsIgnoreCase("Worker Name") || param1.equalsIgnoreCase("Manager")) {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			Thread.sleep(4000);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+keysToSend+"']/following::img[1]")));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+keysToSend+"']"), keysToSend));
			WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+keysToSend+"']/following::img[1]"));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
            String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='keysToSend']/following::img[1]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return;
			}
		}catch (Exception e) {
			System.out.println(e);
		}try {
			if(param1.equalsIgnoreCase("Worker")){
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(4000);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::div[contains(@class,'PopupMenuPopup')]//span[contains(text(),'"+keysToSend+"')][1]")));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']"), param1));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='"+param1+"']/following::div[contains(@class,'PopupMenuPopup')]//span[contains(text(),'"+keysToSend+"')][1]"));
				Actions actions = new Actions(driver); 
				actions.moveToElement(waittext).build().perform();
				highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				Thread.sleep(1000);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
	            String scripNumber = fetchMetadataVO.getScript_number();
				String xpath = "//*[normalize-space(text())='param1']/following::div[contains(@class,'PopupMenuPopup')]//span[contains(text(),'keysToSend')][1]";
			    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
				return;
				}
		} catch (Exception e) {
			System.out.println(e);
		}try {if(param1.equalsIgnoreCase("Performance Document Eligibility")){
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			Thread.sleep(6000);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(),'" + param1 + "')]/following::a[text()='"+keysToSend+"']")));
			WebElement waittext = driver.findElement(By.xpath("//*[contains(text(),'" + param1 + "')]/following::a[text()='"+keysToSend+"']"));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
            String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "//*[contains(text(),'param1')]/following::a[text()='keysToSend']";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return;
		}
		}catch (Exception e) {
			System.out.println(e);
		}try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			Thread.sleep(6000);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(),'" + param1 + "')]/following::*[text()='"+keysToSend+"']")));
			WebElement waittext = driver.findElement(By.xpath("//*[contains(text(),'" + param1 + "')]/following::*[text()='"+keysToSend+"']"));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
           String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "//*[contains(text(),'param1')]/following::*[text()='keysToSend']";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return;
		}catch (Exception e) {
			System.out.println(e);
		}
	try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1 + "']/following::label[normalize-space(text())='"+param2+"']")));
		WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1 + "']/following::label[normalize-space(text())='"+param2+"']"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		Thread.sleep(1000);
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
       String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//*[normalize-space(text())='param1 ']/following::label[normalize-space(text())='param2']";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	}catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + param1 + "']/following::*[@title='"+keysToSend+"'][1]")));
		WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='" + param1 + "']/following::*[@title='"+keysToSend+"'][1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		Thread.sleep(1000);
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//*[normalize-space(text())='param1']/following::*[@title='keysToSend'][1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	}catch (Exception e) {
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
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+keysToSend+"']/following::img[contains(@id,'"+param2+"')][1]")));
			Thread.sleep(4000);
			WebElement waittill = driver.findElement(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+keysToSend+"']/following::img[contains(@id,'"+param2+"')][1]"));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittill).build().perform();
			Thread.sleep(2000);
			highlightElement(driver, fetchMetadataVO, waittill, fetchConfigVO);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittill, fetchConfigVO);
           String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='keysToSend']/following::img[contains(@id,'param2')][1]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return keysToSend;	
		} catch (Exception e) {
			System.out.println(e);
		}try { 
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+keysToSend+"']/following::img[@title='"+param2+"'])[1]")));
			WebElement waittill = driver.findElement(By.xpath("(//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+keysToSend+"']/following::img[@title='"+param2+"'])[1]"));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittill).build().perform();
			Thread.sleep(2000);
			highlightElement(driver, fetchMetadataVO, waittill, fetchConfigVO);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittill, fetchConfigVO);
           String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "(//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='keysToSend']/following::img[@title='param2'])[1]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return keysToSend;	
		} catch (Exception e) {
			System.out.println(e);
		}try { 
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[@value='"+keysToSend+"']/following::img[normalize-space(@title)='"+param2+"'][1]")));
			WebElement waittill = driver.findElement(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[@value='"+keysToSend+"']/following::img[normalize-space(@title)='"+param2+"'][1]"));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittill).build().perform();
			Thread.sleep(2000);
			highlightElement(driver, fetchMetadataVO, waittill, fetchConfigVO);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittill, fetchConfigVO);
          String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "//*[normalize-space(text())='"+param1+"']/following::*[@value='keysToSend']/following::img[normalize-space(@title)='param2'][1]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return keysToSend;	
		} catch (Exception e) {
			System.out.println(e);
			logger.error("Failed during Click action.");
			screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, fetchConfigVO);
			throw e;
		}
	}
	
public void clickImage(WebDriver driver, String param1, String param2, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
	try {
		if(param2.equalsIgnoreCase("Back")) {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[normalize-space(text())='"+param1+"']/preceding::a[1]")));
		WebElement waittext = driver.findElement(By.xpath("//h1[normalize-space(text())='"+param1+"']/preceding::a[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		Thread.sleep(1000);
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//h1[normalize-space(text())='param1']/preceding::a[1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
		}
	}catch (Exception e) {
		System.out.println(e);
	}try {
		if(param1.equalsIgnoreCase("Back")) {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		Thread.sleep(3000);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@title='Done']")));
		WebElement waittext = driver.findElement(By.xpath("//a[@title='Done']"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		Thread.sleep(1000);
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//a[@title='Done']";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
		}
	}catch (Exception e) {
		System.out.println(e);
	}try {
		
		if(param1.equalsIgnoreCase("Notes") && (param2.equalsIgnoreCase("Create"))) {
			WebElement add = driver.findElement(By.xpath("//div[normalize-space(text())='"+param1+"']/following::img[@title='"+param2+"']"));
			highlightElement(driver, fetchMetadataVO, add, fetchConfigVO);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, add, fetchConfigVO);
			 String scripNumber = fetchMetadataVO.getScript_number();
				String xpath = "//div[normalize-space(text())='param1']/following::img[@title='param2']";
			    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return;
		}
	}catch (Exception e) {
		System.out.println(e);
	}
	try {
		
		if(param1.equalsIgnoreCase("Notes")) {
			WebElement add = driver.findElement(By.xpath("//div[normalize-space(text())='"+param1+"']/following::div[@role='button'][1]"));
			highlightElement(driver, fetchMetadataVO, add, fetchConfigVO);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, add, fetchConfigVO);
			return;
		}
	}catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[normalize-space(text())='" + param1 + "']/following::div[@role='button'])[1]")));
		Thread.sleep(2000);
		WebElement waittext = driver.findElement(By.xpath("(//h1[normalize-space(text())='" + param1 + "']/following::div[@role='button'])[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		Thread.sleep(3000);
		WebElement add = driver.findElement(By.xpath("//h1[normalize-space(text())='"+param1+"']/following::img[@title='"+param2+"']"));
		highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		Thread.sleep(1000);
		clickValidateXpath(driver, fetchMetadataVO, add, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//h1[normalize-space(text())='param1']/following::img[@title='param2']";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	}catch (Exception e) {
		System.out.println(e);
	}try {
		
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[normalize-space(text())='"+param1+"']/following::img[@title='"+param2+"'])[1]")));
		WebElement waittext = driver.findElement(By.xpath("(//h1[normalize-space(text())='"+param1+"']/following::img[@title='"+param2+"'])[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		Thread.sleep(4000);
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "(//h1[normalize-space(text())='param1']/following::img[@title='param2'])[1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	}catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::img[@title='"+param2+"'][1]")));
		WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='"+param1+"']/following::img[@title='"+param2+"'][1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		Thread.sleep(1000);
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
       String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//*[normalize-space(text())='param1']/following::img[@title='param2'][1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	}catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())='" + param1 + "']/following::div[@role='button'])[1]")));
		WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())='" + param1 + "']/following::div[@role='button'])[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		Thread.sleep(3000);
		WebElement add = driver.findElement(By.xpath("//*[normalize-space(text())='"+param1+"']/following::img[@title='"+param2+"']"));
		highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		Thread.sleep(1000);
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "(//*[normalize-space(text())='param1 ']/following::div[@role='button'])[1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	}catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::img[contains(@id,'"+param2+"')]")));
		WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='"+param1+"']/following::img[contains(@id,'"+param2+"')]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		Thread.sleep(1000);
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//*[normalize-space(text())='param1']/following::img[contains(@id,'param2')]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	}catch (Exception e) {
		System.out.println(e);
	} try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::img[@title='"+param2+"'][1]")));
		WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='"+param1+"']/following::img[@title='"+param2+"'][1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		Thread.sleep(1000);
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//*[normalize-space(text())='param1']/following::img[@title='param2'][1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	}catch (Exception e) {
		System.out.println(e);
	} 
	try {
		Thread.sleep(3000);
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@title='"+param1+"']")));
		WebElement waittext = driver.findElement(By.xpath("//img[@title='"+param1+"']"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		Thread.sleep(3000);
		waittext.click();
		//clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
       String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//img[@title='param1']";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	}catch (Exception e) {
		System.out.println(e);
	} try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[contains(@id,'"+param1+"')]")));
		WebElement waittext = driver.findElement(By.xpath("//img[contains(@id,'"+param1+"')]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		Thread.sleep(1000);
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
       String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//img[contains(@id,'param1')]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	}catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@title='"+param1+"']")));
		WebElement waittext = driver.findElement(By.xpath("//a[@title='"+param1+"']"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		Thread.sleep(1000);
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//a[@title='param1']";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}
	try {
		 WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		 wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+param2+"']/following::img[1]")));
		 WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+param2+"']/following::img[1]"));
		 Actions actions = new Actions(driver); 
		 actions.moveToElement(waittext).build().perform();
		 highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
		 screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		 Thread.sleep(1000);
		 clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
	         String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']/following::img[1]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);

		return;
	}catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@aria-label,'" +param1+ "')]")));
		WebElement waittext = driver.findElement(By.xpath("//*[contains(@aria-label,'" +param1+ "')]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		Thread.sleep(1000);
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
       String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//*[contains(@aria-label,'param1')]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	}catch (Exception e) {
		System.out.println(e);
		logger.error("Failed during Click action.");
		screenshotFail(driver, "Failed during click Image Method", fetchMetadataVO, fetchConfigVO);
		throw e;
	}
}
	public void clickButton(WebDriver driver, String param1, String param2, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
		try {
            if(param1.equalsIgnoreCase("Done")) {
                  WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
                  Thread.sleep(2000);
                  wait.until(ExpectedConditions
                              .presenceOfElementLocated(By.xpath(("//span[text()='ne']"))));
                  wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()='ne']"), "ne"));
                  WebElement waittext = driver.findElement(By.xpath(("//span[text()='ne']")));
                  screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
                  Actions actions = new Actions(driver); 
                  actions.moveToElement(waittext).build().perform();
                  highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
                  screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
                  Thread.sleep(1000);
                  clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
                  String scripNumber = fetchMetadataVO.getScript_number();
      			String xpath = "//span[text()='ne']";
      		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
                  return;
            }else if(param2.equalsIgnoreCase("Done")) {
                WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
                Thread.sleep(2000);
                wait.until(ExpectedConditions
                            .presenceOfElementLocated(By.xpath(("//span[text()='o']"))));
                wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()='o']"), "o"));
                WebElement waittext = driver.findElement(By.xpath(("//span[text()='o']")));
                screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
                Actions actions = new Actions(driver); 
                actions.moveToElement(waittext).build().perform();
                highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
                screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
                Thread.sleep(1000);
                clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
                String scripNumber = fetchMetadataVO.getScript_number();
    			String xpath = "//span[text()='o']";
    		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
                return;
          }else if(param1.equalsIgnoreCase("Submit")) {
                  WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
                  Thread.sleep(40000);
                  wait.until(ExpectedConditions
                              .presenceOfElementLocated(By.xpath(("//span[text()='m']"))));
                  wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()='m']"), "m"));
                  
                  WebElement waittext = driver.findElement(By.xpath(("//span[text()='m']")));
                  Actions actions = new Actions(driver); 
                  actions.moveToElement(waittext).build().perform();
  //                screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
                  Thread.sleep(2000);
                  highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
                  screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
                  Thread.sleep(1000);
                  clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
                  Thread.sleep(3000);
                  String scripNumber = fetchMetadataVO.getScript_number();
      			String xpath = "//span[text()='m']";
      		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
                  return;
            }else if(param2.equalsIgnoreCase("Submit")) {
                WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
                Thread.sleep(20000);
                wait.until(ExpectedConditions
                            .presenceOfElementLocated(By.xpath(("//*[normalize-space(text())='"+param1+"']/following::span[text()='m']"))));
                wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::span[text()='m']"), "m"));
                
                WebElement waittext = driver.findElement(By.xpath(("//*[normalize-space(text())='"+param1+"']/following::span[text()='m']")));
                highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
                screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
                Thread.sleep(1000);
                clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
                Thread.sleep(3000);
                String scripNumber = fetchMetadataVO.getScript_number();
    			String xpath = "//*[normalize-space(text())='param1']/following::span[text()='m']";
    		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
                return;
          }else if(param1.equalsIgnoreCase("Edit Employment: Review") && param2.equalsIgnoreCase("Submit")) {
              WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
              wait.until(ExpectedConditions
                          .presenceOfElementLocated(By.xpath(("//h1[text()='Edit Employment: Review']/following::*[@title='Submit']"))));
              wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h1[text()='Edit Employment: Review']"), "Edit Employment: Review"));
              Thread.sleep(20000);
              WebElement waittext = driver.findElement(By.xpath(("//h1[text()='Edit Employment: Review']/following::*[@title='Submit']")));
              Actions actions = new Actions(driver); 
              actions.moveToElement(waittext).build().perform();
              highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
              screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
              Thread.sleep(1000);
              clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
              Thread.sleep(3000);
              String scripNumber = fetchMetadataVO.getScript_number();
  			String xpath = "//h1[text()='Edit Employment: Review']/following::*[@title='Submit']";
  		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
              return;
        }else if(param1.equalsIgnoreCase("Next")) {
                  WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
                  wait.until(ExpectedConditions
                              .presenceOfElementLocated(By.xpath(("//span[text()='x']"))));
                  wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()='x']"), "x"));
                  Thread.sleep(10000);
                  WebElement waittext = driver.findElement(By.xpath(("//span[text()='x']")));
                  screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
                  Actions actions = new Actions(driver); 
                  actions.moveToElement(waittext).build().perform();
                  highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
                  screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
                  Thread.sleep(1000);
                  clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
                  Thread.sleep(20000);
                  String scripNumber = fetchMetadataVO.getScript_number();
      			String xpath = "//span[text()='x']";
      		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
                  return;
            }else if(param2.equalsIgnoreCase("Next")) {
                WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
                wait.until(ExpectedConditions
                            .presenceOfElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+param2+"']")));
                wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+param2+"']"), "x"));
                WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+param2+"']"));
                Actions actions = new Actions(driver); 
                actions.moveToElement(waittext).build().perform();
                highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
                screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
                Thread.sleep(1000);
                clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
                Thread.sleep(8000);
                String scripNumber = fetchMetadataVO.getScript_number();
    			String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']";
    		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
                return;
          }else if(param2.equalsIgnoreCase("Yes")) {
                  WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
                  wait.until(ExpectedConditions
                              .presenceOfElementLocated(By.xpath(("//*[normalize-space(text())='"+param1+"']/following::span[text()='Y']"))));
                  wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::span[text()='Y']"), "Y"));
                  Thread.sleep(10000);
                  WebElement waittext = driver.findElement(By.xpath(("//*[normalize-space(text())='"+param1+"']/following::span[text()='Y']")));
                  Actions actions = new Actions(driver); 
                  actions.moveToElement(waittext).build().perform();
                  highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
                  screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
                  Thread.sleep(1000);
                  clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
                  Thread.sleep(2000);
                 String scripNumber = fetchMetadataVO.getScript_number();
      			String xpath = "//*[normalize-space(text())='param1']/following::span[text()='Y']";
      		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
 
                  return;
            }else if(param1.equalsIgnoreCase("Yes")) {
                WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
                wait.until(ExpectedConditions
                            .presenceOfElementLocated(By.xpath(("//span[text()='Y']"))));
                Thread.sleep(4000);
                WebElement waittext = driver.findElement(By.xpath(("//span[text()='Y']")));
                Actions actions = new Actions(driver); 
                actions.moveToElement(waittext).build().perform();
                highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
                screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
                Thread.sleep(1000);
                clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
                Thread.sleep(2000);
                String scripNumber = fetchMetadataVO.getScript_number();
    			String xpath = "//span[text()='Y']";
    		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
                return;
          }else if(param2.equalsIgnoreCase("OK")) {
                WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
                wait.until(ExpectedConditions
                            .presenceOfElementLocated(By.xpath("//button[@_afrpdo='ok' and @accesskey='K']")));
                wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//button[@_afrpdo='ok' and @accesskey='K']"), "K"));
                Thread.sleep(15000);
                WebElement waittext = driver.findElement(By.xpath("//button[@_afrpdo='ok' and @accesskey='K']"));
                Actions actions = new Actions(driver); 
                actions.moveToElement(waittext).build().perform();
                highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
                screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
                Thread.sleep(1000);
                clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
                Thread.sleep(4000);
                String scripNumber = fetchMetadataVO.getScript_number();
    			String xpath = "//button[@_afrpdo='ok' and @accesskey='K']";
    		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
                return;
          }else if(param1.equalsIgnoreCase("Save and Close")) {
                WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
                wait.until(ExpectedConditions
                            .presenceOfElementLocated(By.xpath(("//span[text()='S']"))));
                wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()='S']"), "S"));
                Thread.sleep(4000);
                WebElement waittext = driver.findElement(By.xpath(("//span[text()='S']")));
                Actions actions = new Actions(driver); 
                actions.moveToElement(waittext).build().perform();
                highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
                screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
                Thread.sleep(1000);
                clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
                String scripNumber = fetchMetadataVO.getScript_number();
    			String xpath = "//span[text()='S']";
    		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
                return;
          }else if(param1.equalsIgnoreCase("Continue")) {
              WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
              wait.until(ExpectedConditions
                          .presenceOfElementLocated(By.xpath(("//span[text()='u']"))));
              wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()='u']"), "u"));
              Thread.sleep(4000);
              WebElement waittext = driver.findElement(By.xpath(("//span[text()='u']")));
              screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
              Actions actions = new Actions(driver); 
              actions.moveToElement(waittext).build().perform();
              highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
              screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
              Thread.sleep(1000);
              clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
              String scripNumber = fetchMetadataVO.getScript_number();
  			String xpath = "//span[text()='u']";
  		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
              return;
          }else if(param2.equalsIgnoreCase("Continue")) {
              WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
              wait.until(ExpectedConditions
                          .presenceOfElementLocated(By.xpath(("//button[text()='Contin']"))));
              wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//button[text()='Contin']"), "Contin"));
              WebElement waittext = driver.findElement(By.xpath(("//button[text()='Contin']")));
              Actions actions = new Actions(driver); 
              actions.moveToElement(waittext).build().perform();
              highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
              screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
              Thread.sleep(1000);
              clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
              String scripNumber = fetchMetadataVO.getScript_number();
  			String xpath = "//button[text()='Contin']";
  		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
              return;
          }else if(param1.equalsIgnoreCase("Close")) {
                  WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
                  wait.until(ExpectedConditions
                              .presenceOfElementLocated(By.xpath(("//button[text()='Cl']"))));
                  Thread.sleep(5000);
                  WebElement waittext = driver.findElement(By.xpath(("//button[text()='Cl']")));
                  Actions actions = new Actions(driver); 
                  actions.moveToElement(waittext).build().perform();
                  highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
                  screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
                  Thread.sleep(1000);
                  clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
                  String scripNumber = fetchMetadataVO.getScript_number();
      			String xpath = "//button[text()='Cl']";
      		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
                  return;
            }else if(param1.equalsIgnoreCase("Cancel")) {
                WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
                wait.until(ExpectedConditions
                            .presenceOfElementLocated(By.xpath(("//span[text()='C']"))));
                Thread.sleep(2000);
                WebElement waittext = driver.findElement(By.xpath(("//span[text()='C']")));
                Actions actions = new Actions(driver); 
                actions.moveToElement(waittext).build().perform();
                highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
                screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
                Thread.sleep(1000);
                clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
                String scripNumber = fetchMetadataVO.getScript_number();
    			String xpath = "//span[text()='C']";
    		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
                return;
          }else if(param2.equalsIgnoreCase("Cancel")) {
              WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
              wait.until(ExpectedConditions
                          .presenceOfElementLocated(By.xpath(("//*[normalize-space(text())='"+param1+"']/following::span[text()='C']"))));
              Thread.sleep(2000);
              WebElement waittext = driver.findElement(By.xpath(("//*[normalize-space(text())='"+param1+"']/following::span[text()='C']")));
              Actions actions = new Actions(driver); 
              actions.moveToElement(waittext).build().perform();
              highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
              screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
              Thread.sleep(1000);
              clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
            String scripNumber = fetchMetadataVO.getScript_number();
  			String xpath = "//*[normalize-space(text())='param1']/following::span[text()='C']";
  		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
  
              return;
        }else if(param1.equalsIgnoreCase("Save")) {
                WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
                wait.until(ExpectedConditions
                            .presenceOfElementLocated(By.xpath(("//span[text()='ave']"))));
                wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()='ave']"), "ave"));
                Thread.sleep(4000);
                WebElement waittext = driver.findElement(By.xpath(("//span[text()='ave']")));
                screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
                Actions actions = new Actions(driver); 
                actions.moveToElement(waittext).build().perform();
                highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
                screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
                Thread.sleep(1000);
                clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
                String scripNumber = fetchMetadataVO.getScript_number();
    			String xpath = "//span[text()='ave']";
    		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
                return;
          }
        } catch (Exception e) {
            System.out.println(e);
		}
		try {
			if(param1.equalsIgnoreCase("Subject Areas") && param2.equalsIgnoreCase("Name")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+param2+"' and not(@_afrpdo)])[1]")));
				Thread.sleep(10000);
				WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+param2+"' and not(@_afrpdo)])[1]"));
				Actions actions = new Actions(driver); 
				actions.moveToElement(waittext).build().perform();
				highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
                screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
                Thread.sleep(1000);
                actions.doubleClick(waittext).build().perform();
				Thread.sleep(1000);
	            String scripNumber = fetchMetadataVO.getScript_number();
				String xpath = "(//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='param2' and not(@_afrpdo)])[1]";
			    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			if(param1.equalsIgnoreCase("Edit Plan Cycle")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
                            .presenceOfElementLocated(By.xpath(("//*[normalize-space(text())=\""+param1+"\"]/following::span[text()='K'][2]"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[normalize-space(text())=\""+param1+"\"]/following::span[text()='K'][2]"), "K"));
				WebElement waittext = driver.findElement(By.xpath(("//*[normalize-space(text())=\""+param1+"\"]/following::span[text()='K'][2]")));
				Actions actions = new Actions(driver); 
				actions.moveToElement(waittext).build().perform();
				highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
                screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
                Thread.sleep(1000);
                clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(4000);
	            String scripNumber = fetchMetadataVO.getScript_number();
				String xpath = "//*[normalize-space(text())='param1']/following::span[text()='K'][2]";
			    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
				return;
          }
		} catch (Exception e) {
			System.out.println(e);
		}try {
			if(param2.equalsIgnoreCase("OK")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(5000);
				wait.until(ExpectedConditions
                            .presenceOfElementLocated(By.xpath(("//*[normalize-space(text())=\""+param1+"\"]/following::span[text()='K']"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[normalize-space(text())=\""+param1+"\"]/following::span[text()='K']"), "K"));
				WebElement waittext = driver.findElement(By.xpath(("//*[normalize-space(text())=\""+param1+"\"]/following::span[text()='K']")));
				Actions actions = new Actions(driver); 
				actions.moveToElement(waittext).build().perform();
				highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
                screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
                Thread.sleep(1000);
                clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(4000);
	            String scripNumber = fetchMetadataVO.getScript_number();
				String xpath = "//*[normalize-space(text())='param1']/following::span[text()='K']";
			    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
				return;
          }
		} catch (Exception e) {
			System.out.println(e);
		}try {
			if(param2.equalsIgnoreCase("Submit")) {
                WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
                wait.until(ExpectedConditions
                            .presenceOfElementLocated(By.xpath(("//*[normalize-space(text())='"+param1+"']/following::span[normalize-space(text())='"+param2+"']"))));
                WebElement waittext = driver.findElement(By.xpath(("//*[normalize-space(text())='"+param1+"']/following::span[normalize-space(text())='"+param2+"']")));
                Actions actions = new Actions(driver); 
                actions.moveToElement(waittext).build().perform();
                highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
                screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
                Thread.sleep(1000);
                clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
                Thread.sleep(3000);
               String scripNumber = fetchMetadataVO.getScript_number();
    			String xpath = "//*[normalize-space(text())='param1']/following::span[normalize-space(text())='param2']";
    		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
 
                return;
          }
		} catch (Exception e) {
			// TODO: handle exception
		}try {
			if(param2.equalsIgnoreCase("Save and Close")) {
                WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
                wait.until(ExpectedConditions
                            .presenceOfElementLocated(By.xpath(("//*[normalize-space(text())='"+param1+"']/following::span[normalize-space(text())='S']"))));
                WebElement waittext = driver.findElement(By.xpath(("//*[normalize-space(text())='"+param1+"']/following::span[normalize-space(text())='S']")));
                Actions actions = new Actions(driver); 
                actions.moveToElement(waittext).build().perform();
                highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
                screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
                Thread.sleep(1000);
                clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
                Thread.sleep(3000);
               String scripNumber = fetchMetadataVO.getScript_number();
    			String xpath = "//*[normalize-space(text())='param1']/following::span[normalize-space(text())='S']";
    		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
 
                return;
          }
		} catch (Exception e) {
			// TODO: handle exception
		}try {
			if(param1.equalsIgnoreCase("Goals") && param2.equals("Actions")) {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			Thread.sleep(4000);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@id,'actBtn')]")));
			WebElement waittext = driver.findElement(By.xpath("//div[contains(@id,'actBtn')]"));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
            screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
            Thread.sleep(1000);
            clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
            String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "//div[contains(@id,'actBtn')]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);

			return;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[normalize-space(text())='" + param1 + "']"))));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[normalize-space(text())='" + param1 + "']"), param1));
			Thread.sleep(4000);
			WebElement waittext = driver.findElement(By.xpath(("//span[normalize-space(text())='" + param1 + "']")));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
            screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
            Thread.sleep(1000);
            clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(5000);
            String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "//span[normalize-space(text())='param1 ']";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}
		try { 
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//td[normalize-space(text())='" +param1+ "']"))));
			WebElement waittext = driver.findElement(By.xpath(("//td[normalize-space(text())='" +param1+ "']")));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
         //   screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
            Thread.sleep(5000);
            clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
            Thread.sleep(2000);
            String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "//td[normalize-space(text())='param1']";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}try { 
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//button[normalize-space(text())='" +param1+ "'and not(@style='display:none')]"))));
			WebElement waittext = driver.findElement(By.xpath(("//button[normalize-space(text())='" +param1+ "'and not(@style='display:none')]")));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
            screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
            Thread.sleep(1000);
            clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(5000);
            String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "//button[normalize-space(text())='param1'and not(@style='display:none')]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}try { 
			if(param1.equalsIgnoreCase("Warning")) {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='Warning']/following::*[contains(text(),'Do you want to continue?') and not(text()=\"Your changes aren't saved. If you leave this page, then your changes will be lost. Do you want to continue?\")]/following::*[text()='Yes']")));
			WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='Warning']/following::*[contains(text(),'Do you want to continue?') and not(text()=\"Your changes aren't saved. If you leave this page, then your changes will be lost. Do you want to continue?\")]/following::*[text()='Yes']"));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittext).build().perform();	
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
            screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
            Thread.sleep(1000);
            clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(1000);
            String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "//*[normalize-space(text())='Warning']/following::*[contains(text(),'Do you want to continue?') and not(text()=\"Your changes aren't saved. If you leave this page, then your changes will be lost. Do you want to continue?\")]/following::*[text()='Yes']";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		try { 
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+param2+"' and not(@_afrpdo)])[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+param2+"' and not(@_afrpdo)])[1]"));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
            screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
            Thread.sleep(1000);
            clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
           String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "(//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2' and not(@_afrpdo)])[1]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}
		try { 
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+param2+"'])[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+param2+"'])[1]"));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
            screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
            Thread.sleep(1000);
            clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(1000);
            String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "(//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2'])[1]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}
		try { 
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())=\""+param1+"\"]/following::*[normalize-space(text())='"+param2+"'])[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())=\""+param1+"\"]/following::*[normalize-space(text())='"+param2+"'])[1]"));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
            screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
            Thread.sleep(1000);
            clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
            String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "(//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2'])[1]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);

			return;
		} catch (Exception e) {
			System.out.println(e);
		}try { 
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[contains(text(),'"+param1+"')]/following::*[normalize-space(text())='"+param2+"'])[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//h1[contains(text(),'"+param1+"')]/following::*[normalize-space(text())='"+param2+"'])[1]"));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
            screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
            Thread.sleep(1000);
            clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(1000);
           String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "(//h1[contains(text(),'param1')]/following::*[normalize-space(text())='param2'])[1]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}try { 
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[contains(text(),'"+param1+"')]/following::*[normalize-space(text())='"+param2+"'])[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//*[contains(text(),'"+param1+"')]/following::*[normalize-space(text())='"+param2+"'])[1]"));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
            screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
            Thread.sleep(1000);
            clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(1000);
           String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "(//*[contains(text(),'param1')]/following::*[normalize-space(text())='param2'])[1]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return;
		} catch (Exception e) {
			System.out.println(e);
			logger.error("Failed during Click action.");
			screenshotFail(driver, "Failed during clickLink Method", fetchMetadataVO, fetchConfigVO);
			throw e;
		}
	}
	public void clickTableLink(WebDriver driver, String param1, String param2, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//table[@summary='"+param1 +"']//a)[1]")));
			Thread.sleep(4000);
			WebElement waittext = driver.findElement(By.xpath("(//table[@summary='"+param1 +"']//a)[1]"));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
            screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
            Thread.sleep(1000);
            clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(2000);
            String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "(//table[@summary='param1']//a)[1]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())='"+param1 +"']/following::table[@summary='Main Task List']//a)[2]")));
			Thread.sleep(4000);
			WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())='"+param1 +"']/following::table[@summary='Main Task List']//a)[2]"));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
            screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
            Thread.sleep(1000);
            clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(2000);
           String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "(//*[normalize-space(text())='param1']/following::table[@summary='Main Task List']//a)[2]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return;
		} catch (Exception e) {
			System.out.println(e);
			logger.error("Failed during Click action.");
			screenshotFail(driver, "Failed during clickLink Method", fetchMetadataVO, fetchConfigVO);
			throw e;
		}
	}
	public void tableRowSelect(WebDriver driver, String param1, String param2, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//table[@summary='"+param1 +"']//td)[1]")));
			Thread.sleep(5000);
			WebElement waittext = driver.findElement(By.xpath("(//table[@summary='"+param1 +"']//td)[1]"));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
            screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
            Thread.sleep(1000);
            clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(2000);
           String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "(//table[@summary='param1']//td)[1]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return;
		} catch (Exception e) {
			System.out.println(e);
			logger.error("Failed during Click action.");
			screenshotFail(driver, "Failed during clickLink Method", fetchMetadataVO, fetchConfigVO);
			throw e;
		}
	}
	
	public void actionApprove(WebDriver driver, String param1, String param2, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception  {
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			Thread.sleep(2000);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//a[normalize-space(text())=\""+param1+"\"][1]"))));
			WebElement waittext = driver.findElement(By.xpath(("//a[normalize-space(text())=\""+param1+"\"][1]")));
			waittext.click();
			Thread.sleep(3000);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//td[normalize-space(text())=\""+param2+"\"][1]"))));
			WebElement approve = driver.findElement(By.xpath(("//td[normalize-space(text())=\""+param2+"\"][1]")));
			highlightElement(driver, fetchMetadataVO, approve, fetchConfigVO);
	        clickValidateXpath(driver, fetchMetadataVO, approve, fetchConfigVO);
	        screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
			System.out.println(e);
			logger.error("Failed during Click Approve Button.");
			screenshotFail(driver, "Failed during Click Approve Button.", fetchMetadataVO, fetchConfigVO);
			throw e;
		}
	}
	
	public void clickLink(WebDriver driver, String param1, String param2, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
		try { if(param1.equalsIgnoreCase("Performance Goals")){
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[text()='Performance Goals']/following::a[1]"))));
			WebElement waittext = driver.findElement(By.xpath("//*[text()='Performance Goals']/following::a[1]"));
			Thread.sleep(5000);
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
	        clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
	        screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
            String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "//*[text()='Performance Goals']/following::a[1]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);

			return;
		}
		} catch (Exception e) {
			System.out.println(e);
		}
		try { if(param1.equalsIgnoreCase("Goals") && param2.equalsIgnoreCase("Performance Goals")){
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[text()='Performance Goals']/following::a[1]"))));
			WebElement waittext = driver.findElement(By.xpath("//*[text()='Performance Goals']/following::a[1]"));
			Thread.sleep(5000);
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
	        clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
	        screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
	        String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "//*[text()='Performance Goals']/following::a[1]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);

	        return;
		}
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
		if(param1.equalsIgnoreCase("Viewing plan")){
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[normalize-space(text())='" + param1 + "']/following::a[1]")));
			Thread.sleep(4000);
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[normalize-space(text())='" + param1 + "']"), param1));
			WebElement waittext = driver.findElement(By.xpath("//span[normalize-space(text())='" + param1 + "']/following::a[1]"));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
            screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
            Thread.sleep(1000);
            clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
            String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "//span[normalize-space(text())=' param1 ']/following::a[1]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);

			return;		}
	} catch (Exception e) {
		System.out.println(e);
	}try {
		if(param2!=null){
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[normalize-space(text())='"+param1+"']/following::label[normalize-space(text())='"+param2+"']/following::a[1]")));
			Thread.sleep(4000);
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h1[normalize-space(text())='"+param1+"']/following::label[normalize-space(text())='"+param2+"']"), param2));
			WebElement waittext = driver.findElement(By.xpath("//h1[normalize-space(text())='"+param1+"']/following::label[normalize-space(text())='"+param2+"']/following::a[1]"));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
            screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
            Thread.sleep(1000);
            clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
            String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "//h1[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/following::a[1]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
				return;

		}
	} catch (Exception e) {
		System.out.println(e);
	}try {
		if(param1.equalsIgnoreCase("Select Template")){
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::a[@title='"+param2+"'][1]")));
			WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='"+param1+"']/following::a[@title='"+param2+"'][1]"));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittext).build().perform();
			highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
            screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
            Thread.sleep(1000);
            clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
            String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "//*[normalize-space(text())='param1']/following::a[@title='param2'][1]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
            return;
		}
	} catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		Thread.sleep(2000);
//		JavascriptExecutor js = (JavascriptExecutor) driver;
		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//a[normalize-space(text())=\""+param1+"\"][1]"))));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//a[normalize-space(text())=\""+param1+"\"][1]"), param1));
		WebElement waittext = driver.findElement(By.xpath(("//a[normalize-space(text())=\""+param1+"\"][1]")));
//		js.executeScript("document.body.style.zoom='90%'");
		Thread.sleep(5000);
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
        clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
  //      js.executeScript("document.body.style.zoom='100%'");
        screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
       String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//a[normalize-space(text())='param1'][1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
        return;
	} catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//a[contains(@id,'" + param1 + "')])[1]")));
		WebElement waittext = driver.findElement(By.xpath("(//a[contains(@id,'" + param1 + "')])[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
       String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "(//a[contains(@id,' param1 ')])[1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}
	try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@title='" + param1 + "']")));
		WebElement waittext = driver.findElement(By.xpath("//div[@title='" + param1 + "']"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
        screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
        Thread.sleep(1000);
        clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
       String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//div[@title=' param1']";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
        return;
	} catch (Exception e) {
		System.out.println(e);
	}
	try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@title='" + param1 + "']")));
		WebElement waittext = driver.findElement(By.xpath("//a[@title='" + param1 + "']"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
        screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
        Thread.sleep(1000);
        clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//a[@title=' param1 ']";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
        return;
	} catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(@title,'"+param1+"')]")));
		WebElement waittext = driver.findElement(By.xpath("//a[contains(@title,'"+param1+"')]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
        screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
        Thread.sleep(1000);
        clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
       String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//a[contains(@title,'param1')]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
        return;
	} catch (Exception e) {
		System.out.println(e);
	}
	try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@title,'" + param1 + "')]")));
		WebElement waittext = driver.findElement(By.xpath("//*[contains(@title,'" + param1 + "')]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
        screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
        Thread.sleep(1000);
        clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
       String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//*[contains(@title,' param1 ')]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
        return;
	} catch (Exception e) {
		System.out.println(e);
	}
	// Need to check for what purpose
	try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//div[contains(text(),'" + param1 + "')])[2]")));
		WebElement waittext = driver.findElement(By.xpath("(//div[contains(text(),'" + param1 + "')])[2]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
        screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
        Thread.sleep(1000);
        clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "(//div[contains(text(),' param1 ')])[2]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
        return;
	} catch (Exception e) {
		System.out.println(e);
	}
	try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//a[@role='" + param1 + "']"))));
		WebElement waittext = driver.findElement(By.xpath(("//a[@role='" + param1 + "']")));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
        screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
        Thread.sleep(1000);
        clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//a[@role=' param1 ']";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
        return;
	} catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[normalize-space(text())='"+param1+"']/following::a[normalize-space(text())='"+param2+"']"))));
		WebElement waittext = driver.findElement(By.xpath(("//*[normalize-space(text())='"+param1+"']/following::a[normalize-space(text())='"+param2+"']")));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
        screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
        Thread.sleep(1000);
        clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//*[normalize-space(text())='param1']/following::a[normalize-space(text())='param2']";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
        return;	} catch (Exception e) {
		System.out.println(e);
		logger.error("Failed during Click action.");
		screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, fetchConfigVO);
		throw e;
	}
}
	public void clickRadiobutton(WebDriver driver, String param1,String param2, String keysToSend, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
		try {
			Thread.sleep(3000);
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath(("(//*[normalize-space(text())='" + param1 + "']/following::label[normalize-space(text())='"
							+ param2 + "']/following::label[normalize-space(text())='" + keysToSend + "'])[1]"))));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath("//*[normalize-space(text())='" + param1 + "']/following::label[normalize-space(text())='" + param2 + "']"), param2));
			WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())='" + param1 + "']/following::label[normalize-space(text())='"
					+ param2 + "']/following::label[normalize-space(text())='" + keysToSend + "'])[1]"));
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			tab(driver, fetchMetadataVO, fetchConfigVO);
			Thread.sleep(500);

			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
            String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "(//*[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/following::label[normalize-space(text())=' keysToSend'])[1]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath(("(//*[normalize-space(text())='" + param1 + "']/following::label[normalize-space(text())='" + keysToSend + "'])[1]"))));
			WebElement waittext = driver.findElement(By.xpath(("(//*[normalize-space(text())='" + param1 + "']/following::label[normalize-space(text())='" + keysToSend + "'])[1]")));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			tab(driver, fetchMetadataVO, fetchConfigVO);
			Thread.sleep(500);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
            String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "(//*[normalize-space(text())=' param1']/following::label[normalize-space(text())='keysToSend'])[1]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return;
		} catch (Exception e) {
				System.out.println(e);	
			
			logger.error("Failed during Click action.");
			screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, fetchConfigVO);
			throw e;
		}
	}
	public void clickCheckbox(WebDriver driver, String param1, String keysToSend, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
		try{
			if((param1.equalsIgnoreCase("Create Performance Documents")) || (param1.equalsIgnoreCase("Employee Final Feedback")) || (param1.equalsIgnoreCase("Performance Documents"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			Thread.sleep(4000);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+keysToSend+"']/preceding::label[@id][1]"
					))));
			WebElement waittext = driver.findElement(By.xpath(("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+keysToSend+"']/preceding::label[@id][1]"
					)));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(1000);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
           String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='keysToSend']/preceding::label[@id][1]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return;
			}
			}catch (Exception e) {
				System.out.println(e);
			}try{
				if(param1.equalsIgnoreCase("Participant Feedback")){
					WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(4000);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+keysToSend+"']/preceding::label[normalize-space(text())='Participant']/preceding::label[1]"
						))));
				WebElement waittext = driver.findElement(By.xpath(("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+keysToSend+"']/preceding::label[normalize-space(text())='Participant']/preceding::label[1]"
						)));
				Actions actions = new Actions(driver); 
				actions.moveToElement(waittext).build().perform();
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(1000);
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
	            String scripNumber = fetchMetadataVO.getScript_number();
				String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='keysToSend']/preceding::label[normalize-space(text())='Participant']/preceding::label[1]";
			    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
				return;
				}
				}catch (Exception e) {
					System.out.println(e);
				}try{
					if(keysToSend.equalsIgnoreCase("Include terminated work relationships") || keysToSend.equalsIgnoreCase("Managers cannot update due dates")){
						WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
					Thread.sleep(4000);
					wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+keysToSend+"']"))));
					wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+keysToSend+"']"), keysToSend));
					WebElement waittext = driver.findElement(By.xpath(("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+keysToSend+"']")));
					Actions actions = new Actions(driver); 
					actions.moveToElement(waittext).build().perform();
					clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
					Thread.sleep(1000);
					screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		            String scripNumber = fetchMetadataVO.getScript_number();
					String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='keysToSend']";
				    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
					return;
					}
					}catch (Exception e) {
						System.out.println(e);
					}try{
					if(param1.equalsIgnoreCase("Goals") && (keysToSend.equalsIgnoreCase("Private"))){
						WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
					Thread.sleep(4000);
					wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+keysToSend+"']"))));
					wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+keysToSend+"']"), keysToSend));
					WebElement waittext = driver.findElement(By.xpath(("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+keysToSend+"']")));
					Actions actions = new Actions(driver); 
					actions.moveToElement(waittext).build().perform();
					clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
					Thread.sleep(1000);
					screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		            String scripNumber = fetchMetadataVO.getScript_number();
					String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='keysToSend']";
				    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
					return;
					}
					}catch (Exception e) {
						System.out.println(e);
					}try{
						if(param1.equalsIgnoreCase("Goal Weights")|| param1.equalsIgnoreCase("Refresh Options") || param1.equalsIgnoreCase("Disability Info")){
							WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
						Thread.sleep(4000);
						wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+keysToSend+"']"))));
						wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+keysToSend+"']"), keysToSend));
						WebElement waittext = driver.findElement(By.xpath(("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+keysToSend+"']")));
						Actions actions = new Actions(driver); 
						actions.moveToElement(waittext).build().perform();
						clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
						Thread.sleep(1000);
						screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			            String scripNumber = fetchMetadataVO.getScript_number();
						String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='keysToSend']";
					    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
						return;
						}
						}catch (Exception e) {
							System.out.println(e);
						}try {
					WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
					wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//label[normalize-space(text())='"+param1+"']/following::span[normalize-space(text())='"+keysToSend+"']/preceding::label[1]"
							))));
					wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//label[normalize-space(text())='"+param1+"']/following::span[normalize-space(text())='"+keysToSend+"']"), keysToSend));
					WebElement waittext = driver.findElement(By.xpath("//label[normalize-space(text())='"+param1+"']/following::span[normalize-space(text())='"+keysToSend+"']/preceding::label[1]"));
					Thread.sleep(1000);
					Actions actions = new Actions(driver); 
					actions.moveToElement(waittext).build().perform();
					clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
					tab(driver, fetchMetadataVO, fetchConfigVO);
					Thread.sleep(500);
					screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		            String scripNumber = fetchMetadataVO.getScript_number();
					String xpath = "//label[normalize-space(text())='param1']/following::span[normalize-space(text())='keysToSend']/preceding::label[1]";
				    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
					return;
				} catch (Exception e) {
					System.out.println(e);
		}try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[normalize-space(text())='"+param1+"']/following::span[text()='"+keysToSend+"']/preceding::label[1]"))));
	//		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::span[text()='"+keysToSend+"']"), keysToSend));
			WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='"+param1+"']/following::span[text()='"+keysToSend+"']/preceding::label[1]"));
			Thread.sleep(1000);
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittext).build().perform();
			waittext.click();
		//	clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			tab(driver, fetchMetadataVO, fetchConfigVO);
			Thread.sleep(500);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
            String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "//*[normalize-space(text())='param1']/following::span[text()='keysToSend']/preceding::label[1]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+keysToSend+"']/preceding::label[1]")));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+keysToSend+"']"), keysToSend));
			WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+keysToSend+"']/preceding::label[1]"));
			Thread.sleep(1000);
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			tab(driver, fetchMetadataVO, fetchConfigVO);
			Thread.sleep(500);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
            String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='keysToSend']/preceding::label[1]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return;
		} catch (Exception e) {
			System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//label[normalize-space(text())='"+param1+"']/following::label[normalize-space(text())='"+keysToSend+"']"))));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//label[normalize-space(text())='"+param1+"']/following::label[normalize-space(text())='"+keysToSend+"']"), keysToSend));
		WebElement waittext = driver.findElement(By.xpath("//label[normalize-space(text())='"+param1+"']/following::label[normalize-space(text())='"+keysToSend+"']"));
		Thread.sleep(1000);
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		tab(driver, fetchMetadataVO, fetchConfigVO);
		Thread.sleep(500);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
       String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//label[normalize-space(text())='param1']/following::label[normalize-space(text())='keysToSend']";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	} catch (Exception e) {
		System.out.println(e);
}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+keysToSend+"']"))));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+keysToSend+"']"), keysToSend));
		WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+keysToSend+"']"));
		Thread.sleep(1000);
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		Thread.sleep(3000);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
       String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='keysToSend']";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}
	try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//label[normalize-space(text())='"+keysToSend+"']"))));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//label[normalize-space(text())='"+keysToSend+"']"), keysToSend));
		WebElement waittext = driver.findElement(By.xpath("//label[normalize-space(text())='"+keysToSend+"']"));
		Thread.sleep(1000);
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		tab(driver, fetchMetadataVO, fetchConfigVO);
		Thread.sleep(500);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
       String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//label[normalize-space(text())='keysToSend']";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	} catch (Exception e) {
		System.out.println(e);
		logger.error("Failed during Click action.");
		screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, fetchConfigVO);
		throw e;
	}
}public void clickNotificationLink(WebDriver driver, String param1, String param2, FetchMetadataVO fetchMetadataVO,
		FetchConfigVO fetchConfigVO) throws Exception {
	

	try {
		WebDriverWait wait = new WebDriverWait(driver,fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[@placeholder='"+param1+"']/following::a[1]"))));
		Thread.sleep(4000);
		WebElement waittext =driver.findElement(By.xpath("//*[@placeholder='"+param1+"']/following::a[1]"));
		Actions actions = new Actions(driver); 
	//	actions.moveToElement(waittext).build().perform();
		//clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
        waittext.click();
        screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
  String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//*[@placeholder='param1']/following::a[1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
        return;	}catch(Exception e) {
		System.out.println(e);
		logger.error("Failed during Click action.");
		screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, fetchConfigVO);
		throw e;
	}
}
public void clickLinkAction(WebDriver driver, String param1,String param2, String keysToSend, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
	
	try {
		if(param1.equalsIgnoreCase("Goals") && param2.equalsIgnoreCase("Edit")) {
		WebDriverWait wait = new WebDriverWait(driver,fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[normalize-space(text())='"+param1+"']/following::span[normalize-space(text())='"+keysToSend+"']/following::img[contains(@title,'"+param2+"')][1]"))));
		Thread.sleep(2000);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::span[normalize-space(text())='"+keysToSend+"']"),keysToSend)); 
		WebElement waittext =driver.findElement(By.xpath("//*[normalize-space(text())='"+param1+"']/following::span[normalize-space(text())='"+keysToSend+"']/following::img[contains(@title,'"+param2+"')][1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
        screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
        Thread.sleep(1000);
        clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
       String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//*[normalize-space(text())='param1']/following::span[normalize-space(text())='keysToSend']/following::img[contains(@title,'param2')][1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
        return; 
		}
	}catch(Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver,fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[normalize-space(text())='"+param1+"']/following::a[normalize-space(text())='"+keysToSend+"']/following::img[contains(@title,'"+param2+"')][1]"))));
		Thread.sleep(2000);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::a[normalize-space(text())='"+keysToSend+"']"),keysToSend)); 
		WebElement waittext =driver.findElement(By.xpath("//*[normalize-space(text())='"+param1+"']/following::a[normalize-space(text())='"+keysToSend+"']/following::img[contains(@title,'"+param2+"')][1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
        screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
        Thread.sleep(1000);
        clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
       String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//*[normalize-space(text())='param1']/following::a[normalize-space(text())='keysToSend']/following::img[contains(@title,'param2')][1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
        return; 	}catch(Exception e) {
		System.out.println(e);
	}
	try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::a[normalize-space(text())='"+keysToSend+"']")));
		WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='"+param1+"']/following::a[normalize-space(text())='"+keysToSend+"']"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
        screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
        Thread.sleep(1000);
        clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
    String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//*[normalize-space(text())='param1']/following::a[normalize-space(text())='keysToSend']";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
        return;
		} catch (Exception e) {
		System.out.println(e);
		
	}
	try {//*[text()='Worker Name']/following::*[text()='Kaushik (Kaushik) Sekaran']/following::img[1]
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())='"+keysToSend+"']/following::td[normalize-space(text())='"+param1+"']/following::table[1]//div)[1]")));
		WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())='"+keysToSend+"']/following::td[normalize-space(text())='"+param1+"']/following::table[1]//div)[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
        screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
        Thread.sleep(1000);
        clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "(//*[normalize-space(text())='keysToSend']/following::td[normalize-space(text())='param1']/following::table[1]//div)[1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
        return;
        	} catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver,fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("(//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+keysToSend+"']/following::img[contains(@title,'"+param2+"')])[1]"))));
		WebElement waittext =driver.findElement(By.xpath("(//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+keysToSend+"']/following::img[contains(@title,'"+param2+"')])[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
        screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
        Thread.sleep(1000);
        clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "(//*[normalize-space(text())='param1']/following::*[normalize-space(text())='keysToSend']/following::img[contains(@title,'param2')])[1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
        return; 
	}catch(Exception e) {
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
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())='"+param1+"']/following::label[normalize-space(text())='"+param2+"']/following::textarea)[1]")));
		Thread.sleep(1000);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::label[normalize-space(text())='"+param2+"']"), param2));
		WebElement waittill = driver.findElement(By.xpath("//*[normalize-space(text())='"+param1+"']/following::label[normalize-space(text())='"+param2+"']/following::textarea[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittill).build().perform();
		typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
		Thread.sleep(500);
       String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//*[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/following::textarea[1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return keysToSend;	
	} catch (Exception e) {
		System.out.println(e);
	}try { 
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//body[@dir='ltr']")));
		Thread.sleep(1000);
		WebElement waittill = driver.findElement(By.xpath("//body[@dir='ltr']"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittill).build().perform();
		typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		Thread.sleep(500);
    String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//body[@dir='ltr']";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return keysToSend;	
	} catch (Exception e) {
		System.out.println(e);
	}try { 
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//body[contains(@class,'contents_ltr')][1]")));
		Thread.sleep(1000);
		WebElement waittill = driver.findElement(By.xpath("//body[contains(@class,'contents_ltr')][1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittill).build().perform();
		typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		Thread.sleep(500);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//body[contains(@class,'contents_ltr')][1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return keysToSend;	
	} catch (Exception e) {
		System.out.println(e);
	}try { 
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+param2+"']/following::textarea)[1]")));
		Thread.sleep(1000);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+param2+"']"), param2));
		WebElement waittill = driver.findElement(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+param2+"']/following::textarea[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittill).build().perform();
		typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
		Thread.sleep(500);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']/following::textarea[1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
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
        if(param1.equalsIgnoreCase("Search")) {
              WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
              wait.until(ExpectedConditions
                          .presenceOfElementLocated(By.xpath(("//div[@role='"+param1+"' and not(@style)]//label[normalize-space(text())='"+param2+"']/following::input[1]"))));
              wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//div[@role='"+param1+"' and not(@style)]//label[normalize-space(text())='"+param2+"']"), param2));
              WebElement waittill = driver.findElement(By.xpath(("//div[@role='"+param1+"' and not(@style)]//label[normalize-space(text())='"+param2+"']/following::input[1]")));
              screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
              typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
              String scripNumber = fetchMetadataVO.getScript_number();
  			String xpath = "//div[@role='param1' and not(@style)]//label[normalize-space(text())='param2']/following::input[1]";
  		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
              return keysToSend;
        }
        else {
        	System.out.println("Not Entered Name");
        }
	}catch (Exception e) {
    		System.out.println(e);
		}
	try { 
		if(param1.equalsIgnoreCase("Search") && (param2.equalsIgnoreCase("Match With"))) {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			Thread.sleep(6000);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::label[normalize-space(text())='"+param2+"']/preceding::input[@type='text'][1]")));
			WebElement waittill = driver.findElement(By.xpath("//*[normalize-space(text())='"+param1+"']/following::label[normalize-space(text())='"+param2+"']/preceding::input[@type='text'][1]"));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			Thread.sleep(2000);
			enter(driver, fetchMetadataVO, fetchConfigVO);
            String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "//*[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/preceding::input[@type='text'][1]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return keysToSend;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	try { 
    		if(param1.equalsIgnoreCase("Performance Documents") && (param2.equalsIgnoreCase("Review Period"))) {
    			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
    			Thread.sleep(6000);
    			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='Review Period *']/following::input[contains(@id,'HCMPERFORMANCE_FUSE_PERFORMANCE') and not(@style)])[1]")));
    			WebElement waittill = driver.findElement(By.xpath("(//h1[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='Review Period *']/following::input[contains(@id,'HCMPERFORMANCE_FUSE_PERFORMANCE') and not(@style)])[1]"));
    			Actions actions = new Actions(driver); 
    			actions.moveToElement(waittill).build().perform();
    			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
    			Thread.sleep(2000);
    			enter(driver, fetchMetadataVO, fetchConfigVO);
                String scripNumber = fetchMetadataVO.getScript_number();
    			String xpath = "(//h1[normalize-space(text())='param1']/following::*[normalize-space(text())='Review Period *']/following::input[contains(@id,'HCMPERFORMANCE_FUSE_PERFORMANCE') and not(@style)])[1]";
    		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
    			return keysToSend;
    			}
    		} catch (Exception e) {
    			System.out.println(e);
    		}try { 
        		if(param1.equalsIgnoreCase("Participant Feedback") && (param2.equalsIgnoreCase("Participant"))) {
        			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
        			Thread.sleep(6000);
        			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::td[normalize-space(text())='"+param2+"']/following::input[1]")));
        			WebElement waittill = driver.findElement(By.xpath("//*[normalize-space(text())='"+param1+"']/following::td[normalize-space(text())='"+param2+"']/following::input[1]"));
        			Actions actions = new Actions(driver); 
        			actions.moveToElement(waittill).build().perform();
        			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
        			Thread.sleep(2000);
        			enter(driver, fetchMetadataVO, fetchConfigVO);
                    String scripNumber = fetchMetadataVO.getScript_number();
        			String xpath = "//*[normalize-space(text())='param1']/following::td[normalize-space(text())='param2']/following::input[1]";
        		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
        			return keysToSend;
        			}
        		} catch (Exception e) {
        			System.out.println(e);
        		}try { 
        		if(param1.equalsIgnoreCase("Transfer")) {
        			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
        			Thread.sleep(6000);
        			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::label[normalize-space(text())='"+param2+"']/following::input[not(@type='hidden')]")));
        			WebElement waittill = driver.findElement(By.xpath("//*[normalize-space(text())='"+param1+"']/following::label[normalize-space(text())='"+param2+"']/following::input[not(@type='hidden')]"));
        			Actions actions = new Actions(driver); 
        			actions.moveToElement(waittill).build().perform();
        			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
        			Thread.sleep(2000);
        			enter(driver, fetchMetadataVO, fetchConfigVO);
                    String scripNumber = fetchMetadataVO.getScript_number();
        			String xpath = "//*[normalize-space(text())='param1']/following::label[normalize-space(text())='param']/following::input[not(@type='hidden')]";
        		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
        			return keysToSend;
        			}
        		} catch (Exception e) {
        			System.out.println(e);
        		}try { 
    			if(param1.equalsIgnoreCase("Performance Documents")) {
        			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
        			Thread.sleep(6000);
        			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+param2+"']/following::input[contains(@id,'HCMPERFORMANCE_FUSE_PERFORMANCE') and not(@style)])[1]")));
        			WebElement waittill = driver.findElement(By.xpath("(//h1[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+param2+"']/following::input[contains(@id,'HCMPERFORMANCE_FUSE_PERFORMANCE') and not(@style)])[1]"));
        			Actions actions = new Actions(driver); 
        			actions.moveToElement(waittill).build().perform();
        			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
        			Thread.sleep(2000);
        			enter(driver, fetchMetadataVO, fetchConfigVO);
                    String scripNumber = fetchMetadataVO.getScript_number();
        			String xpath = "(//h1[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']/following::input[contains(@id,'HCMPERFORMANCE_FUSE_PERFORMANCE') and not(@style)])[1]";
        		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
        			return keysToSend;
        			}
        		} catch (Exception e) {
        			System.out.println(e);
        		}try { 
        			if(param1.equalsIgnoreCase("Duplicate Plan Cycle")) {
            			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
            			Thread.sleep(6000);
            			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(),'"+param1+"')]/following::label[text()='"+param2+"']/following::input[1]")));
            			WebElement waittill = driver.findElement(By.xpath("//div[contains(text(),'"+param1+"')]/following::label[text()='"+param2+"']/following::input[1]"));
            			Actions actions = new Actions(driver); 
            			actions.moveToElement(waittill).build().perform();
            			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
            			Thread.sleep(2000);
                       String scripNumber = fetchMetadataVO.getScript_number();
            			String xpath = "//div[contains(text(),'param1')]/following::label[text()='param2']/following::input[1]";
            		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
 
            			return keysToSend;
            			}
	            		} catch (Exception e) {
	            			System.out.println(e);
	            		}try { 
        			if(param1.equalsIgnoreCase("Send Email Notification")) {
            			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
            			Thread.sleep(6000);
            			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+param2+"']/preceding-sibling::input[1]")));
            			WebElement waittill = driver.findElement(By.xpath("//h1[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+param2+"']/preceding-sibling::input[1]"));
            			Actions actions = new Actions(driver); 
            			actions.moveToElement(waittill).build().perform();
            			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
            			Thread.sleep(2000);
            			enter(driver, fetchMetadataVO, fetchConfigVO);
                      String scripNumber = fetchMetadataVO.getScript_number();
            			String xpath = "//h1[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']/preceding-sibling::input[1]";
            		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
  
            			return keysToSend;
            			}
	            		} catch (Exception e) {
	            			System.out.println(e);
	            		}
        		try { 
        			if(param1.equalsIgnoreCase("Location") && (param2.equalsIgnoreCase("Location"))) {
        			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
        			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[contains(text(),'"+param1+"')]/following::label[normalize-space(text())='"+param2+"']/following::input[not(@type='hidden')])[1]")));
        			Thread.sleep(1000);
        			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h1[contains(text(),'"+param1+"')]/following::label[normalize-space(text())='"+param2+"']"), param2));
        			WebElement waittill = driver.findElement(By.xpath("(//h1[contains(text(),'"+param1+"')]/following::label[normalize-space(text())='"+param2+"']/following::input[not(@type='hidden')])[1]"));
        			Actions actions = new Actions(driver); 
        			actions.moveToElement(waittill).build().perform();
        			for(int i=0; i<=2;i++){
        				try{
        					typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
        				break;
        				}finally {
        					
        				}
        			}
        			Thread.sleep(500);
        			return keysToSend;
        			}
        		} catch (Exception e) {
        			System.out.println(e);
        		}try { 
        			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
        			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[contains(text(),'"+param1+"')]/following::label[normalize-space(text())='"+param2+"']/following::input)[1]")));
        			Thread.sleep(1000);
        			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h1[contains(text(),'"+param1+"')]/following::label[normalize-space(text())='"+param2+"']"), param2));
        			WebElement waittill = driver.findElement(By.xpath("//h1[contains(text(),'"+param1+"')]/following::label[normalize-space(text())='"+param2+"']/following::input[1]"));
        			Actions actions = new Actions(driver); 
        			actions.moveToElement(waittill).build().perform();
        			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
        			Thread.sleep(500);
                    String scripNumber = fetchMetadataVO.getScript_number();
        			String xpath = "//h1[contains(text(),'param1')]/following::label[normalize-space(text())='param2']/following::input[1]";
        		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
        			return keysToSend;
        		} catch (Exception e) {
        			System.out.println(e);
        		}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@placeholder,'"+param1+"')]")));
		WebElement waittill = driver.findElement(By.xpath("//*[contains(@placeholder,'"+param1+"')]"));
		typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		Thread.sleep(1000);
		return keysToSend;
	} catch (Exception e) {
		System.out.println(e);	
	}try { 
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//label[normalize-space(text())='"+param1+"']/following::label[normalize-space(text())='"+param2+"']/following::input)[1]")));
		Thread.sleep(1000);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//label[normalize-space(text())='"+param1+"']/following::label[normalize-space(text())='"+param2+"']"), param2));
		WebElement waittill = driver.findElement(By.xpath("//label[normalize-space(text())='"+param1+"']/following::label[normalize-space(text())='"+param2+"']/following::input[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittill).build().perform();
		typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
		Thread.sleep(500);
       String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//label[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/following::input[1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return keysToSend;
	} catch (Exception e) {
		System.out.println(e);
	}try { 
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())=\""+param1+"\"]/following::label[normalize-space(text())=\""+param2+"\"]/following::input)[1]")));
			Thread.sleep(1000);
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[normalize-space(text())=\""+param1+"\"]/following::label[normalize-space(text())=\""+param2+"\"]"), param2));
			WebElement waittill = driver.findElement(By.xpath("(//*[normalize-space(text())=\""+param1+"\"]/following::label[normalize-space(text())=\""+param2+"\"]/following::input)[1]"));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			Thread.sleep(500);
            String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "(//*[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/following::input)[1]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return keysToSend;	
		} catch (Exception e) {
			System.out.println(e);
		}try { 
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())=\""+param2+"\"]/following::input)[1]")));
		WebElement waittill = driver.findElement(By.xpath("(//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())=\""+param2+"\"]/following::input)[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittill).build().perform();
		typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
		if(keysToSend.equalsIgnoreCase("Annual Performance Evaluation 2019")|| (keysToSend.equalsIgnoreCase("Varshitha (Varshitha) Reddy Karna"))) {
			Thread.sleep(2000);
			enter(driver, fetchMetadataVO, fetchConfigVO);
		}
		Thread.sleep(2000);
       String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "(//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']/following::input)[1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
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
			if(param2.equalsIgnoreCase("Plan") || param2.equalsIgnoreCase("Option")) {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='AFPopupMenuPopup']//li[text()='"+keysToSend+"']")));
			WebElement waittext = driver.findElement(By.xpath("//div[@class='AFPopupMenuPopup']//li[text()='"+keysToSend+"']"));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittext).build().perform();
			clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			return;
			}
		} catch (Exception e) {
			System.out.println(e);
		}try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//div[contains(@id,'popup-container')]//*[normalize-space(text())='"+keysToSend+"'])[1]")));
			Thread.sleep(4000);
	 	wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("(//div[contains(@id,'popup-container')]//*[normalize-space(text())='"+keysToSend+"'])[1]"), keysToSend));
			WebElement waittext = driver.findElement(By.xpath("(//div[contains(@id,'popup-container')]//*[normalize-space(text())='"+keysToSend+"'])[1]"));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittext).build().perform();
			clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(2000);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+param2+"']/following::span[normalize-space(text())='"+keysToSend+"']")));
		Thread.sleep(4000);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h1[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+param2+"']/following::span[normalize-space(text())='"+keysToSend+"']"), keysToSend));
		WebElement waittext = driver.findElement(By.xpath("//h1[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+param2+"']/following::span[normalize-space(text())='"+keysToSend+"']"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}
	try {
		if(param2.equalsIgnoreCase("Performance Document Types")) {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+param2+"']/following::[@type='checkbox']/parent::label[normalize-space(text())='"+keysToSend+"']")));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h1[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+param2+"']"), param2));
		WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+param2+"']/following::[@type='checkbox']/parent::label[normalize-space(text())='"+keysToSend+"']"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		return;
		}
	} catch (Exception e) {
		System.out.println(e);
	}try {
		if(param1.equalsIgnoreCase("Send Email Notification")) {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='AFPopupMenuPopup']//li[text()='"+keysToSend+"']")));
		WebElement waittext = driver.findElement(By.xpath("//div[@class='AFPopupMenuPopup']//li[text()='"+keysToSend+"']"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		return;
		}
	} catch (Exception e) {
		System.out.println(e);
	}try {
		if(param1.equalsIgnoreCase("Create Performance Documents") && (param2.equalsIgnoreCase("Review Period"))) {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[normalize-space(text())='"+keysToSend+"']")));
		WebElement waittext = driver.findElement(By.xpath("//div[normalize-space(text())='"+keysToSend+"']"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		return;
		}
	} catch (Exception e) {
		System.out.println(e);
	}try {
		if(param1.equalsIgnoreCase("Performance Documents") && (param2.equalsIgnoreCase("Review Period"))) {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[normalize-space(text())='"+keysToSend+"']")));
		WebElement waittext = driver.findElement(By.xpath("//div[normalize-space(text())='"+keysToSend+"']"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		return;
		}
	} catch (Exception e) {
		System.out.println(e);
	}try {
		if(param1.equalsIgnoreCase("Participant Feedback") && (param2.equalsIgnoreCase("Review Period"))) {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[normalize-space(text())='"+keysToSend+"']")));
		WebElement waittext = driver.findElement(By.xpath("//div[normalize-space(text())='"+keysToSend+"']"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		return;
		}
	} catch (Exception e) {
		System.out.println(e);
	}try {
		if(param1.equalsIgnoreCase("Change Access for All Managers")) {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+param2+"']/following::*[@type='checkbox']/following::*[normalize-space(text())='"+keysToSend+"']")));
		WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+param2+"']/following::*[@type='checkbox']/following::*[normalize-space(text())='"+keysToSend+"']"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		Thread.sleep(1000);
		tab(driver, fetchMetadataVO, fetchConfigVO);
		return;
		}
	} catch (Exception e) {
		System.out.println(e);
	}
	try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())=\""+param2+"\"]/following::*[normalize-space(text())='"+keysToSend+"']")));
		WebElement waittext = driver.findElement(By.xpath("//h1[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())=\""+param2+"\"]/following::*[normalize-space(text())='"+keysToSend+"']"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}
	try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//label[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+param2+"']/following::*[normalize-space(text())='"+keysToSend+"']")));
		WebElement waittext = driver.findElement(By.xpath("//label[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+param2+"']/following::*[normalize-space(text())='"+keysToSend+"']"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h3[normalize-space(text())='"+param1+"']/following::label[normalize-space(text())='"+param2+"'][1]/following::div[contains(@style,'position')]//li[normalize-space(text())='"+keysToSend+"']")));
		WebElement waittext = driver.findElement(By.xpath("//label[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+param2+"']/following::*[normalize-space(text())='"+keysToSend+"']"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}
	try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h3[normalize-space(text())='"+param1+"']/following::label[normalize-space(text())='"+param2+"'][1]/following::div[contains(@style,'position')]//li[normalize-space(text())='"+keysToSend+"']")));
		WebElement waittext = driver.findElement(By.xpath("//h3[normalize-space(text())='"+param1+"']/following::label[normalize-space(text())='"+param2+"'][1]/following::div[contains(@style,'position')]//li[normalize-space(text())='"+keysToSend+"']"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())=\""+param2+"\"]/following::*[@type='checkbox']/following::*[normalize-space(text())='"+keysToSend+"']")));
		WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())=\""+param2+"\"]/following::*[@type='checkbox']/following::*[normalize-space(text())='"+keysToSend+"']"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		Thread.sleep(500);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())=\""+param2+"\"]/following::*[normalize-space(text())='"+keysToSend+"'][1]")));
		WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())=\""+param2+"\"]/following::*[normalize-space(text())='"+keysToSend+"'][1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		Thread.sleep(1000);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}
	try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(text(),'Search')]")));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//a[contains(text(),'Search')]"), "Search"));
		WebElement search = driver.findElement(By.xpath("//a[contains(text(),'Search')]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(search).build().perform();
		search.click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='Search']/following::*[normalize-space(text())='"+param2+"']/following::input[1]")));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[normalize-space(text())='Search']/following::*[normalize-space(text())='"+param2+"']"), param2));
		WebElement searchResult = driver.findElement(By.xpath("//*[normalize-space(text())='Search']/following::*[normalize-space(text())='"+param2+"']/following::input[1]"));
		typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
		if(keysToSend!=null) {
			enter(driver, fetchMetadataVO, fetchConfigVO);
			Thread.sleep(5000);
			WebElement text = driver.findElement(By.xpath("//*[normalize-space(text())='Search']/following::*[normalize-space(text())='"+param2+"']/following::span[normalize-space(text())='"+keysToSend+"']"));
			text.click();
		}
		WebElement button = driver.findElement(By.xpath("//*[normalize-space(text())='Search']/following::*[normalize-space(text())='"+param2+"']/following::*[normalize-space(text())='K'][1]"));
		button.click();	
		return;
	} catch (Exception e) {
		System.out.println(e);
		}
	try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::input[1]")));
		WebElement searchResult = driver.findElement(By.xpath("//*[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::input[1]"));
		typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
		enter(driver, fetchMetadataVO, fetchConfigVO);
		Thread.sleep(5000);
		WebElement text = driver.findElement(By.xpath("//span[normalize-space(text())='"+keysToSend+"']"));
		text.click();
		Thread.sleep(1000);
		WebElement button = driver.findElement(By.xpath("//*[normalize-space(text())='Search']/following::*[normalize-space(text())='Name']/following::*[normalize-space(text())='OK'][1]"));
		button.click();	
		return;
	} catch (Exception e) {
		System.out.println(e);
	}
	try {
		WebElement button = driver.findElement(By.xpath("//*[normalize-space(text())='Search']/following::*[normalize-space(text())='"+param2+"']/following::*[normalize-space(text())='OK'][1]"));
		button.click();	
		return;
	} catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(),'"+param1+"')]/following::*[normalize-space(text())='"+param2+"']/following::*[normalize-space(text())='"+keysToSend+"'][1]")));
		WebElement waittext = driver.findElement(By.xpath("//*[contains(text(),'"+param1+"')]/following::*[normalize-space(text())='"+param2+"']/following::*[normalize-space(text())='"+keysToSend+"'][1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}
	try { 
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[contains(text(),'"+param1+"')]/following::label[normalize-space(text())='"+keysToSend+"']/following::input)[1]")));
		Thread.sleep(1000);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h1[contains(text(),'"+param1+"')]/following::label[normalize-space(text())='"+keysToSend+"']"), keysToSend));
		WebElement waittill = driver.findElement(By.xpath("//h1[contains(text(),'"+param1+"')]/following::label[normalize-space(text())='"+keysToSend+"']/following::input[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittill).build().perform();
		typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
		Thread.sleep(500);
	} catch (Exception e) {
		System.out.println(e);
		logger.error("Failed during Click action.");
		screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, fetchConfigVO);
		throw e;
	}
	}
	public void multipleSendKeys(WebDriver driver, String param1, String param2, String value1,String value2,
			FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
		try {
			if(param1.equalsIgnoreCase("Element Details")) {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			WebElement waittill = driver.findElement(By.xpath("//*[text()='"+param1+"']/following::*[text()='"+value1+"']/following::input[@placeholder='m/d/yy'][1]"));
			Thread.sleep(1000);
		//	wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[text()='"+param1+"']/following::*[text()='"+value1+"']/following::input[1]"), value1));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, value2, waittill, fetchConfigVO, fetchMetadataVO);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "//*[text()='"+param1+"']/following::*[text()='"+value1+"']/following::input[@placeholder='m/d/yy'][1]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return;
			}
		} catch (Exception e) {
			System.out.println(e);
			logger.error("Failed during Click action.");
			screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, fetchConfigVO);
		}
		try {
			
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			WebElement waittill = driver.findElement(By.xpath("//*[text()='"+param1+"']/following::*[text()='"+value1+"']/following::input[1]"));
			Thread.sleep(1000);
		//	wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[text()='"+param1+"']/following::*[text()='"+value1+"']/following::input[1]"), value1));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, value2, waittill, fetchConfigVO, fetchMetadataVO);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "(//table[@role='presentation']/following::a[normalize-space(text())='param1'])[1]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return;

		} catch (Exception e) {
			System.out.println(e);
			logger.error("Failed during Click action.");
			screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, fetchConfigVO);
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			WebElement waittill = driver.findElement(By.xpath("(//*[text()='"+param1+"']/following::*[text()='"+value1+"']/following::input[contains(@id,'NewBdgtPctLst')])["+param2+"]"));
			Thread.sleep(1000);
		//	wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[text()='"+param1+"']/following::*[text()='"+value1+"']/following::input[1]"), value1));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittill).build().perform();
			typeIntoValidxpath(driver, value2, waittill, fetchConfigVO, fetchMetadataVO);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "(//*[text()='"+param1+"']/following::*[text()='"+value1+"']/following::input[contains(@id,'NewBdgtPctLst')])["+param2+"]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return;
		} catch (Exception e) {
			System.out.println(e);
			logger.error("Failed during Click action.");
			screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, fetchConfigVO);
			throw e;
		}
}
	public void tableSendKeys(WebDriver driver, String param1, String param2, String param3,String keysToSend,
		FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
	try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		WebElement waittill = driver.findElement(By.xpath("//h1[text()='"+param1+"']/following::label[normalize-space(text())='"+param2+"']/preceding-sibling::input[not(@type='hidden')]"));
		Thread.sleep(1000);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h1[normalize-space(text())='"+param1+"']/following::label[normalize-space(text())='"+param2+"']"), param2));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittill).build().perform();
		typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//h1[text()='param1']/following::label[normalize-space(text())='param2']/preceding-sibling::input[not(@type='hidden')]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}try {
		WebElement waittill = driver.findElement(By.xpath("//h1[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+param2+"']/preceding-sibling::input[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittill).build().perform();
		typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
       String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//h1[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']/preceding-sibling::input[1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}try {
		WebElement waittill = driver.findElement(By.xpath("//h1[normalize-space(text())='"+param1+"']/following::label[normalize-space(text())='"+param2+"']/preceding::input[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittill).build().perform();
		typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
       String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//h1[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/preceding::input[1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}try {
		WebElement waittill = driver.findElement(By.xpath("(//*[normalize-space(text())='"+param1+"']/following::label[normalize-space(text())='"+param2+"']/preceding-sibling::input)[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittill).build().perform();
		typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "(//*[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/preceding-sibling::input)[1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}
	try {
		WebElement waittill = driver.findElement(By.xpath("//*[normalize-space(text())='"+param1+"']/following::table[@summary='"+param2+"']//*[normalize-space(text())='"+param3+"']/following::input[contains(@id,'NewBdgtPctLst')][1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittill).build().perform();
		typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
       String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//*[normalize-space(text())='param1']/following::table[@summary='param2']//*[normalize-space(text())='param3']/following::input[contains(@id,'NewBdgtPctLst')][1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}try {
		WebElement waittill = driver.findElement(By.xpath("(//table[@summary='"+param1+"']//label[normalize-space(text())='"+param2+"']/preceding-sibling::input)[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittill).build().perform();
		typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "(//table[@summary='param1']//label[normalize-space(text())='param2']/preceding-sibling::input)[1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}try {
		WebElement waittill = driver.findElement(By.xpath("//h1[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+param2+"']/following::input[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittill).build().perform();
		typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
       String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//h1[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']/following::input[1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
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
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table[@summary='"+param1+"']/following::li[normalize-space(text())='"+keysToSend+"']")));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//table[@summary='"+param1+"']/following::li[normalize-space(text())='"+keysToSend+"']"), keysToSend));
		WebElement waittext = driver.findElement(By.xpath("//table[@summary='"+param1+"']/following::li[normalize-space(text())='"+keysToSend+"']"));
		clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::li[normalize-space(text())='"+keysToSend+"']")));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::li[normalize-space(text())='"+keysToSend+"']"), keysToSend));
		WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='"+param1+"']/following::li[normalize-space(text())='"+keysToSend+"']"));
		clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		return;
	} catch (Exception e) {
		System.out.println(e);
		logger.error("Failed during Click action.");
		screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, fetchConfigVO);
		throw e;
	}
}

public void tableDropdownValues(WebDriver driver, String param1, String param2, String keysToSend,
		FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
	try {if (param2.equalsIgnoreCase("Type")) {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::label[normalize-space(text())='"+param2+"']/preceding::input[2]")));
		WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='"+param1+"']/following::label[normalize-space(text())='"+param2+"']/preceding::input[2]"));
		Actions actions = new Actions(driver);
		actions.moveToElement(waittext).build().perform();
		clickTableDropdown(driver, fetchMetadataVO, waittext, fetchConfigVO);
		tableDropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
		Thread.sleep(3000);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//*[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/preceding::input[2]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	}
	} catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::label[normalize-space(text())='"+param2+"']/preceding::a[1]")));
		WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='"+param1+"']/following::label[normalize-space(text())='"+param2+"']/preceding::a[1]"));
		Actions actions = new Actions(driver);
		actions.moveToElement(waittext).build().perform();
		clickTableDropdown(driver, fetchMetadataVO, waittext, fetchConfigVO);
		tableDropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
		Thread.sleep(3000);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
       String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//*[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/preceding::a[1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table[@summary='"+param1+"']//input/following-sibling::a[1]")));
		WebElement waittext = driver.findElement(By.xpath("//table[@summary='"+param1+"']//input/following-sibling::a[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickTableDropdown(driver, fetchMetadataVO, waittext, fetchConfigVO);
		tableDropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//table[@summary='param1']//input/following-sibling::a[1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	} catch (Exception e) {
		System.out.println(e);
	logger.error("Failed during Click action.");
		screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, fetchConfigVO);
		throw e;
	}
}
public void dropdownValues(WebDriver driver, String param1, String param2,String param3, String keysToSend,
		FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
	try {
		if(!param3.isEmpty()) {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[normalize-space(text())='"+param1+"']/following::label[normalize-space(text())='"+param2+"']/following::a[@title='"+param3+"']")));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h1[normalize-space(text())='"+param1+"']/following::label[text()='"+param2+"']"), param2));
			WebElement waittext = driver.findElement(By.xpath("//h1[normalize-space(text())='"+param1+"']/following::label[normalize-space(text())='"+param2+"']/following::a[@title='"+param3+"']"));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittext).build().perform();
	//		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
            String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "//h1[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/following::a[@title='param3']";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return;
		}
	} catch (Exception e) {
		System.out.println(e);
	}try {
		if(param1.equalsIgnoreCase("Performance") && param2.equalsIgnoreCase("Performance Documents")) {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			Thread.sleep(6000);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+param2+"']/following::input[@role='combobox']")));
			WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+param2+"']/following::input[@role='combobox']"));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittext).build().perform();
			clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
            String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']/following::input[@role='combobox']";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return;
		}
	} catch (Exception e) {
			System.out.println(e);
		}try {
			if(param1.equalsIgnoreCase("Send Email Notification") && (param2.equalsIgnoreCase("Hire Date Start Range - Days"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(6000);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='Hire Date Start Range']/following::input[@role='combobox'][2]")));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='Hire Date Start Range']/following::input[@role='combobox'][2]"));
				Actions actions = new Actions(driver); 
				actions.moveToElement(waittext).build().perform();
				waittext.click();
	//			clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
	            String scripNumber = fetchMetadataVO.getScript_number();
				String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='Hire Date Start Range']/following::input[@role='combobox'][2]";
			    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			if(param1.equalsIgnoreCase("Other Plans")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(6000);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())='"+param1+"']/following::td[normalize-space(text())='"+param2+"']/following::a[3])")));
				WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())='"+param1+"']/following::td[normalize-space(text())='"+param2+"']/following::a[3])"));
				Actions actions = new Actions(driver); 
				actions.moveToElement(waittext).build().perform();
				clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
	            String scripNumber = fetchMetadataVO.getScript_number();
				String xpath = "(//*[normalize-space(text())='param1']/following::td[normalize-space(text())='param2']/following::a[3])";
			    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
		}try {
			if(param1.equalsIgnoreCase("Send Email Notification") && (param2.equalsIgnoreCase("Hire Date End Range - Days"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(6000);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='Hire Date End Range']/following::input[@role='combobox'][2]")));
				WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='Hire Date End Range']/following::input[@role='combobox'][2]"));
				Actions actions = new Actions(driver); 
				actions.moveToElement(waittext).build().perform();
				waittext.click();
				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
	            String scripNumber = fetchMetadataVO.getScript_number();
				String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='Hire Date End Range']/following::input[@role='combobox'][2]";
			    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
		}try {
			if(param1.equalsIgnoreCase("Goal Name") && (param2.equalsIgnoreCase("Status"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				Thread.sleep(6000);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())=\""+param1+"\"]/following::*[normalize-space(text())=\""+param2+"\"]/following::a)[1]")));
				WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())=\""+param1+"\"]/following::*[normalize-space(text())=\""+param2+"\"]/following::a)[1]"));
				Actions actions = new Actions(driver); 
				actions.moveToElement(waittext).build().perform();
				clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
	            String scripNumber = fetchMetadataVO.getScript_number();
				String xpath = "(//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']/following::a)[1]";
			    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		try {if(param2.equalsIgnoreCase("Plan") || param2.equalsIgnoreCase("Option")) {
			Thread.sleep(4000);
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())=\""+param1+"\"]/following::*[normalize-space(text())=\""+param2+"\"]/following::a)[1]")));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[normalize-space(text())=\""+param1+"\"]/following::*[normalize-space(text())=\""+param2+"\"]"), param2));
			WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())=\""+param1+"\"]/following::*[normalize-space(text())=\""+param2+"\"]/following::a)[1]"));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittext).build().perform();
			Thread.sleep(2000);
			waittext.click();
//			clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(10000);
			dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
            String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "(//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']/following::a)[1]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return;
		}
		} catch (Exception e) {
			System.out.println(e);
		}try {
			if(param1.equalsIgnoreCase("Create Performance Documents") && (param2.equalsIgnoreCase("Review Period"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='Review Period *'][1]/following::a[contains(@id,'HCMPERFORMANCE_FUSE_PERFORMANCE') and not(@style)][1]")));
				WebElement waittext = driver.findElement(By.xpath("//h1[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='Review Period *'][1]/following::a[contains(@id,'HCMPERFORMANCE_FUSE_PERFORMANCE') and not(@style)][1]"));
				Actions actions = new Actions(driver); 
				actions.moveToElement(waittext).build().perform();
				clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
	            String scripNumber = fetchMetadataVO.getScript_number();
				String xpath = "//h1[normalize-space(text())='param1']/following::*[normalize-space(text())='Review Period *'][1]/following::a[contains(@id,'HCMPERFORMANCE_FUSE_PERFORMANCE') and not(@style)][1]";
			    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
		}try {
			if(param1.equalsIgnoreCase("Performance Documents") && (param2.equalsIgnoreCase("Review Period"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='Review Period *'][1]/following::a[contains(@id,'HCMPERFORMANCE_FUSE_PERFORMANCE') and not(@style)][1]")));
				WebElement waittext = driver.findElement(By.xpath("//h1[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='Review Period *'][1]/following::a[contains(@id,'HCMPERFORMANCE_FUSE_PERFORMANCE') and not(@style)][1]"));
				Actions actions = new Actions(driver); 
				actions.moveToElement(waittext).build().perform();
				clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
	            String scripNumber = fetchMetadataVO.getScript_number();
				String xpath = "//h1[normalize-space(text())='param1']/following::*[normalize-space(text())='Review Period *'][1]/following::a[contains(@id,'HCMPERFORMANCE_FUSE_PERFORMANCE') and not(@style)][1]";
			    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
		}try {
			if(param1.equalsIgnoreCase("Participant Feedback") && (param2.equalsIgnoreCase("Review Period"))) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='Review Period *'][1]/following::a[contains(@id,'HCMPERFORMANCE_FUSE_PERFORMANCE') and not(@style)][1]")));
				WebElement waittext = driver.findElement(By.xpath("//h1[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='Review Period *'][1]/following::a[contains(@id,'HCMPERFORMANCE_FUSE_PERFORMANCE') and not(@style)][1]"));
				Actions actions = new Actions(driver); 
				actions.moveToElement(waittext).build().perform();
				clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(1000);
				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
	            String scripNumber = fetchMetadataVO.getScript_number();
				String xpath = "//h1[normalize-space(text())='param1']/following::*[normalize-space(text())='Review Period *'][1]/following::a[contains(@id,'HCMPERFORMANCE_FUSE_PERFORMANCE') and not(@style)][1]";
			    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
				return;
			}
		} catch (Exception e) {
			System.out.println(e);
		}try {
			if(param1.equalsIgnoreCase("When and Why") && (param2.equalsIgnoreCase("Why are you changing the location?"))) {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())=\""+param2+"\"]/following::a)[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())=\""+param2+"\"]/following::a)[1]"));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittext).build().perform();
			for(int i=0; i<=2;i++){
				try{
					actions.click(waittext).build().perform();
				break;
				}finally {
					
				}
			}try {
				Thread.sleep(6000);
				WebElement popup1 = driver.findElement(By.xpath("//div[contains(@id,'popup-container')]"));
				if(popup1.isDisplayed()) {
					dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
					actions.release();
					screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				}
			} catch (Exception e) {
				for(int i=0; i<=2;i++){
					try{
						actions.click(waittext).build().perform();
						break;
					}finally {
						
					}
				}
				
			}
			dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
			actions.release();
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
            String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "//div[contains(@id,'popup-container')]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return;
		}
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[normalize-space(text())=\""+param1+"\"]/following::*[normalize-space(text())=\""+param2+"\"]/following::a)[1]")));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h1[normalize-space(text())=\""+param1+"\"]/following::*[normalize-space(text())=\""+param2+"\"]"), param2));
		WebElement waittext = driver.findElement(By.xpath("(//h1[normalize-space(text())=\""+param1+"\"]/following::*[normalize-space(text())=\""+param2+"\"]/following::a)[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		Thread.sleep(5000);
		waittext.click();
	//	clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		Thread.sleep(10000);
		dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "(//h1[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']/following::a)[1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//label[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+param2+"']/following::a[1]")));
		WebElement waittext = driver.findElement(By.xpath("//label[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+param2+"']/following::a[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		Thread.sleep(1000);
		dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//label[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']/following::a[1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//div[contains(@id,'popup-container')]//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())=\""+param2+"\"]/following::a)[1]")));
		WebElement waittext = driver.findElement(By.xpath("(//div[contains(@id,'popup-container')]//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())=\""+param2+"\"]/following::a)[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		for(int i=0; i<=2;i++){
			try{
				clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(1000);
			break;
			}finally {
				System.out.println("Clicked 1st time");
			}
		}try {
			Thread.sleep(4000);
			WebElement popup1 = driver.findElement(By.xpath("//div[contains(@id,'popup-container')]"));
			if(popup1.isDisplayed()) {
				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			}
		} catch (Exception e) {
			enter(driver, fetchMetadataVO, fetchConfigVO);
			Thread.sleep(2000);
			dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		}
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "(//div[contains(@id,'popup-container')]//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']/following::a)[1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}try {
		if(param1.equalsIgnoreCase("Basic Information") || param1.equalsIgnoreCase("Communication") || param1.equalsIgnoreCase("Eligibility")) {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())=\""+param2+"\"]/following::input[1]")));
		WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())=\""+param2+"\"]/following::input[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
	//	clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		waittext.click();
		Thread.sleep(1000);
		dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']/following::input[1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
		}
	} catch (Exception e) {
		System.out.println(e);
	}try {
		if(param1.equalsIgnoreCase("Selected Eligibility Profiles") || param1.equalsIgnoreCase("Overall Ratings")) {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+param2+"']/following::input[ not(@type='hidden')])[1]")));
		WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+param2+"']/following::input[ not(@type='hidden')])[1]"));
		Actions actions = new Actions(driver);
		actions.moveToElement(waittext).build().perform();
		clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		Thread.sleep(1000);
		dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "(//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']/following::input[ not(@type='hidden')])[1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
		}
	} catch (Exception e) {
		System.out.println(e);
	}try {
		if(param1.equalsIgnoreCase("Details")) {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+param2+"']/following::a[1]")));
		WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())='"+param2+"']/following::a[1]"));
		Actions actions = new Actions(driver);
		actions.moveToElement(waittext).build().perform();
		clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		Thread.sleep(1000);
		dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']/following::a[1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
		}
	} catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())=\""+param2+"\"]/following::input[@placeholder='Select a value']")));
		WebElement waittext = driver.findElement(By.xpath("//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())=\""+param2+"\"]/following::input[@placeholder='Select a value']"));
		clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//*[normalize-space(text())='param1']/following::*[normalize-space(text())='param2']/following::input[@placeholder='Select a value']";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())=\""+param2+"\"]/following::a)[1]")));
		WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())='"+param1+"']/following::*[normalize-space(text())=\""+param2+"\"]/following::a)[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		for(int i=0; i<=2;i++){
			try{
				clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(1000);
			break;
			}finally {
				System.out.println("Clicked 1st time");
			}
		}try {
			Thread.sleep(4000);
			WebElement popup1 = driver.findElement(By.xpath("//div[contains(@id,'popup-container')]"));
			if(popup1.isDisplayed()) {
				dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			}
		} catch (Exception e) {
			enter(driver, fetchMetadataVO, fetchConfigVO);
			Thread.sleep(2000);
			dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		}
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "//div[contains(@id,'popup-container')]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}
	try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[contains(text(),'"+param1+"')]/following::*[normalize-space(text())='"+param2+"']/following::a)[1]")));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h1[contains(text(),'"+param1+"')]/following::*[normalize-space(text())='"+param2+"']"), param2));
		WebElement waittext = driver.findElement(By.xpath("(//h1[contains(text(),'"+param1+"')]/following::*[normalize-space(text())='"+param2+"']/following::a)[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickDropdownXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "(//h1[contains(text(),'param1')]/following::*[normalize-space(text())='param2']/following::a)[1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
 		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//label[contains(text(),'"+param1+"')]/following::a)[1]")));
	//	wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h1[contains(text(),'"+param1+"')]/following::*[normalize-space(text())='"+param2+"']"), param2));
		WebElement waittext = driver.findElement(By.xpath("(//label[contains(text(),'"+param1+"')]/following::a)[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
        String scripNumber = fetchMetadataVO.getScript_number();
		String xpath = "(//label[contains(text(),'param1')]/following::a)[1]";
	    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
		return;
	}catch (Exception e) {
		System.out.println(e);
		logger.error("Failed during Click action.");
		screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, fetchConfigVO);
		throw e;
		}
	}
private  void clickDropdownXpath(WebDriver driver, FetchMetadataVO fetchMetadataVO, WebElement waittext, FetchConfigVO fetchConfigVO) {
		try {
		//	WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		//	driver.manage().timeouts().setScriptTimeout(fetchConfigVO.getWait_time(), TimeUnit.SECONDS);
	//		JavascriptExecutor js = (JavascriptExecutor)driver;
	//		js.executeScript("arguments[0].click();", waittext);
			waittext.click();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private  void clickTableDropdown(WebDriver driver, FetchMetadataVO fetchMetadataVO, WebElement waittext, FetchConfigVO fetchConfigVO) {
		try {
			waittext.click();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void highlightElement(WebDriver driver, FetchMetadataVO fetchMetadataVO, WebElement waittext, FetchConfigVO fetchConfigVO) {
		try {
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("arguments[0].style.border='6px solid yellow'", waittext);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private  void clickValidateXpath(WebDriver driver, FetchMetadataVO fetchMetadataVO, WebElement waittext, FetchConfigVO fetchConfigVO) {
		try {
		//	WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		//	driver.manage().timeouts().setScriptTimeout(fetchConfigVO.getWait_time(), TimeUnit.SECONDS);
	//		JavascriptExecutor js = (JavascriptExecutor)driver;
	//		js.executeScript("arguments[0].click();", waittext);
			waittext.click();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void clickFilter(WebDriver driver, String xpath1, String xpath2, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO)
			throws InterruptedException {
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
	public  String password(WebDriver driver, String inputParam, String keysToSend,
			FetchConfigVO fetchConfigVO, FetchMetadataVO fetchMetadataVO) {
		try { 
			WebElement waittill = driver.findElement(By.xpath("//*[contains(@placeholder,'"+inputParam+"')]"));
			typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
			return keysToSend;
		} catch (Exception e) {
			screenshotFail(driver, "Failed during clearAndType Method", fetchMetadataVO, fetchConfigVO);
			System.out.println(e);
			throw e;
		}
	}
	public void typeIntoValidxpath(WebDriver driver, String keysToSend, WebElement waittill, FetchConfigVO fetchConfigVO, FetchMetadataVO fetchMetadataVO) {
		try {
			driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
			waittill.clear();
			waittill.sendKeys(keysToSend);
			logger.info("clear and typed the given Data");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void clearMethod(WebDriver driver, WebElement waittill) {
		WebDriverWait wait = new WebDriverWait(driver, 60);
		wait.until(ExpectedConditions.elementToBeClickable(waittill));
		waittill.clear();
		logger.info("clear and typed the given Data");
	}
	public void scrollUsingElement(WebDriver driver, String inputParam, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) {
		try {
			if(inputParam.equalsIgnoreCase("Work Email")) {
				Thread.sleep(3000);
			WebElement waittill = driver.findElement(By.xpath("(//b[text()='"+inputParam+"'])[1]"));
			scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
			logger.info("ScrollUsingElement Successfully Done!");
            String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "(//b[text()='inputParam'])[1]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return;
			}
		}catch(Exception e) {
			System.out.println(inputParam);
		}	
		try {
				Thread.sleep(2000);
				WebElement waittill = driver.findElement(By.xpath("//span[normalize-space(text())='"+inputParam+"'][1]"));
			//	((JavascriptExecutor)driver).executeScript("document.body.style.zoom='50%';");
					scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
				//	((JavascriptExecutor)driver).executeScript("document.body.style.zoom='100%';");
					logger.info("ScrollUsingElement Successfully Done!");
		            String scripNumber = fetchMetadataVO.getScript_number();
					String xpath = "//span[normalize-space(text())='inputParam'][1]";
				    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
					return;
			}catch(Exception e) {
				System.out.println(inputParam);
			}
			try {
				WebElement waittill = driver.findElement(By.xpath("//a[normalize-space(text())='"+inputParam+"']"));
					scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
					logger.info("ScrollUsingElement Successfully Done!");
		            String scripNumber = fetchMetadataVO.getScript_number();
					String xpath = "//a[normalize-space(text())='inputParam']";
				    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
					return;
			}catch(Exception e) {
				System.out.println(inputParam);
			}
			try {
				WebElement waittill = driver.findElement(By.xpath("//h1[normalize-space(text())='"+inputParam+"']"));
					scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
					logger.info("ScrollUsingElement Successfully Done!");
		            String scripNumber = fetchMetadataVO.getScript_number();
					String xpath = "//h1[normalize-space(text())='inputParam']";
				    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
					return;
			}catch(Exception e) {
				System.out.println(inputParam);
			}
			try {
				WebElement waittill = driver.findElement(By.xpath("(//h2[normalize-space(text())='"+inputParam+"'])"));
					scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
					logger.info("ScrollUsingElement Successfully Done!");
		            String scripNumber = fetchMetadataVO.getScript_number();
					String xpath = "(//h2[normalize-space(text())='inputParam'])";
				    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
					return;
			}catch(Exception e) {
				System.out.println(inputParam);
			}try {
				WebElement waittill = driver.findElement(By.xpath("(//h3[normalize-space(text())='"+inputParam+"'])[2]"));
				scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
				logger.info("ScrollUsingElement Successfully Done!");
	            String scripNumber = fetchMetadataVO.getScript_number();
				String xpath = "(//h3[normalize-space(text())='inputParam'])[2]";
			    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
				return;
		}catch(Exception e) {
			System.out.println(inputParam);
		}try {
				WebElement waittill = driver.findElement(By.xpath("//td[normalize-space(text())='"+inputParam+"']"));
				scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
				logger.info("ScrollUsingElement Successfully Done!");
	            String scripNumber = fetchMetadataVO.getScript_number();
				String xpath = "//td[normalize-space(text())='inputParam']";
			    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
				return;
		}catch(Exception e) {
			System.out.println(inputParam);
		}
			try {
				WebElement waittill = driver.findElement(By.xpath("//div[contains(text(),'"+inputParam+"')]"));
					scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
					logger.info("ScrollUsingElement Successfully Done!");
		            String scripNumber = fetchMetadataVO.getScript_number();
					String xpath = "//div[contains(text(),'inputParam')]";
				    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
					return;
			}catch(Exception e) {
				System.out.println(inputParam);
			}
			try {
				WebElement waittill = driver.findElement(By.xpath("(//table[@summary='"+ inputParam +"']//td//a)[1]"));
					scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
					logger.info("ScrollUsingElement Successfully Done!");
		            String scripNumber = fetchMetadataVO.getScript_number();
					String xpath = "(//table[@summary=' inputParam']//td//a)[1]";
				    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
					return;
			}catch(Exception e) {
				System.out.println(inputParam);
			}
			try {
				WebElement waittill = driver.findElement(By.xpath("(//label[normalize-space(text())=\""+inputParam+"\"]/following::input)[1]"));
					scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
					logger.info("ScrollUsingElement Successfully Done!");
		            String scripNumber = fetchMetadataVO.getScript_number();
					String xpath = "(//label[normalize-space(text())='inputParam']/following::input)[1]";
				    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
					return;
			}catch(Exception e) {
				System.out.println(inputParam);
			}
			try {
				WebElement waittill = driver.findElement(By.xpath("//a[contains(@id,'"+inputParam+"')]"));
					scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
					logger.info("ScrollUsingElement Successfully Done!");
		            String scripNumber = fetchMetadataVO.getScript_number();
					String xpath = "//a[contains(@id,'inputParam')]";
				    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
					return;
			}catch(Exception e) {
				System.out.println(inputParam);
			}
			try {
				WebElement waittill = driver.findElement(By.xpath("//li[normalize-space(text())='"+inputParam+"']"));
					scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
					logger.info("ScrollUsingElement Successfully Done!");
		            String scripNumber = fetchMetadataVO.getScript_number();
					String xpath = "//li[normalize-space(text())='inputParam']";
				    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
					return;
			}catch(Exception e) {
				System.out.println(inputParam);
			}
			try {
				WebElement waittill = driver.findElement(By.xpath("//label[normalize-space(text())=\""+inputParam+"\"]"));
					scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
					logger.info("ScrollUsingElement Successfully Done!");
		            String scripNumber = fetchMetadataVO.getScript_number();
					String xpath = "//label[normalize-space(text())='inputParam']";
				    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
					return;
			}catch(Exception e) {
				System.out.println(inputParam);
			}
			try {
				WebElement waittill = driver.findElement(By.xpath("//button[normalize-space(text())='" + inputParam + "']"));
					scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
					logger.info("ScrollUsingElement Successfully Done!");
		            String scripNumber = fetchMetadataVO.getScript_number();
					String xpath = "//button[normalize-space(text())=' inputParam ']";
				    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
					return;
			}catch(Exception e) {
				System.out.println(inputParam);
			}
			try {
				WebElement waittill = driver.findElement(By.xpath("//img[@title='"+inputParam+"']"));
					scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
					logger.info("ScrollUsingElement Successfully Done!");
		            String scripNumber = fetchMetadataVO.getScript_number();
					String xpath = "//img[@title='inputParam']";
				    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
					return;
			}catch(Exception e) {
				System.out.println(inputParam);
			}try {
				WebElement waittill = driver.findElement(By.xpath("//*[normalize-space(text())='"+inputParam+"']"));
				scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
				logger.info("ScrollUsingElement Successfully Done!");
	            String scripNumber = fetchMetadataVO.getScript_number();
				String xpath = "//*[normalize-space(text())='inputParam']";
			    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
				return;
			}catch(Exception e) {
				System.out.println(inputParam);
			}try {
				WebElement waittill = driver.findElement(By.xpath("(//*[@title='" + inputParam + "'])[1]"));
				scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
				logger.info("ScrollUsingElement Successfully Done!");
	            String scripNumber = fetchMetadataVO.getScript_number();
				String xpath = "(//*[@title=' inputParam '])[1]";
			    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
				return;
		}catch (Exception e) {
				logger.error("Failed During scrollUsingElement");
				screenshotFail(driver, "Failed during scrollUsingElement Method", fetchMetadataVO, fetchConfigVO);
				System.out.println(inputParam);
				e.printStackTrace();
				throw e;
			}
		}
	private void scrollMethod(WebDriver driver, FetchConfigVO fetchConfigVO, WebElement waittill, FetchMetadataVO fetchMetadataVO) {
		fetchConfigVO.getMedium_wait();
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
	//	WebElement elements = wait.until(ExpectedConditions.elementToBeClickable(waittill));
		WebElement element = waittill;
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
	}
	public  void tab(WebDriver driver, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
		try {
			Actions action = new Actions(driver);
			action.sendKeys(Keys.TAB).build().perform();
			logger.info("Successfully Clicked the tab.");
		} catch (Exception e) {
			logger.error("Failed During clicking the tab");
			screenshotFail(driver, "Failed during tab Method", fetchMetadataVO, fetchConfigVO);
			System.out.println("Failed to do TAB Action");
			e.printStackTrace();
			throw e;
		}
	}
	public void mousehover(WebDriver driver, String param1, String param2, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			Actions actions = new Actions(driver);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//table[@summary='"+param1+"']//tr[1]/following::a)[2]")));
			scrollUsingElement(driver, param1, fetchMetadataVO, fetchConfigVO);
			Thread.sleep(6000);
			WebElement waittext = driver.findElement(By.xpath("(//table[@summary='"+param1+"']//tr[1]/following::a)[2]"));
			actions.moveToElement(waittext).build().perform();
			clickImage(driver,param2,param1, fetchMetadataVO, fetchConfigVO);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
            String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "(//table[@summary='param1']//tr[1]/following::a)[2]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return;
		}catch (Exception e) {
			System.out.println(e);
		} try {
				Actions actions = new Actions(driver); 
				WebElement waittill = driver.findElement(By.xpath("(//table[@role='presentation']/following::a[normalize-space(text())='"+param1+"'])[1]"));
				actions.moveToElement(waittill).build().perform();
				Thread.sleep(5000);
				System.out.print("Successfully executed Mousehover");
	            String scripNumber = fetchMetadataVO.getScript_number();
				String xpath = "(//table[@role='presentation']/following::a[normalize-space(text())='param1'])[1]";
			    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
				return;
				} catch (Exception e) {
				System.out.println(e);
				screenshotFail(driver, "Failed during MouseHover Method", fetchMetadataVO, fetchConfigVO);
				throw e;
				}
		}
	public void enter(WebDriver driver, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
		try {
			Thread.sleep(4000);
			Actions actionObject = new Actions(driver);
			actionObject.sendKeys(Keys.ENTER).build().perform();
			Thread.sleep(3000);
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
				Shutterbug.shootPage(driver,ScrollStrategy.BOTH_DIRECTIONS,500,true).withName(fetchMetadataVO.getSeq_num() + "_"
						+ fetchMetadataVO.getLine_number() + "_" + fetchMetadataVO.getScenario_name() + "_"
						+ fetchMetadataVO.getScript_number() + "_" + fetchMetadataVO.getTest_run_name() + "_"
						+ fetchMetadataVO.getLine_number() + "_Passed").save(fetchConfigVO.getScreenshot_path() + fetchMetadataVO.getCustomer_name() + "\\"
						+ fetchMetadataVO.getTest_run_name() + "\\");
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
				Shutterbug.shootPage(driver,ScrollStrategy.BOTH_DIRECTIONS,500,true).withName(fetchMetadataVO.getSeq_num() + "_"
						+ fetchMetadataVO.getLine_number() + "_" + fetchMetadataVO.getScenario_name() + "_"
						+ fetchMetadataVO.getScript_number() + "_" + fetchMetadataVO.getTest_run_name() + "_"
						+ fetchMetadataVO.getLine_number() + "_Failed").save(fetchConfigVO.getScreenshot_path() + fetchMetadataVO.getCustomer_name() + "\\"
						+ fetchMetadataVO.getTest_run_name() + "\\");
				logger.info("Successfully Screenshot is taken");
				return image_dest;
			} catch (Exception e) {
				logger.error("Failed During Taking screenshot");
				System.out.println("Exception while taking Screenshot" + e.getMessage());
				return e.getMessage();
			}
	}

	/*
	 * public String screenshot(WebDriver driver, String screenshotName,
	 * FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) { String
	 * image_dest = null; try { TakesScreenshot ts = (TakesScreenshot) driver; File
	 * source = ts.getScreenshotAs(OutputType.FILE); image_dest =
	 * (fetchConfigVO.getScreenshot_path()+"\\" + fetchMetadataVO.getCustomer_name()
	 * + "\\" + fetchMetadataVO.getTest_run_name()+ "
	 * \\" + fetchMetadataVO.getLine_number()+ "_" +
	 * fetchMetadataVO.getScenario_name() + "_" + fetchMetadataVO.getScript_number()
	 * + "_" + fetchMetadataVO.getTest_run_name() + "_" +
	 * fetchMetadataVO.getScript_id() + "_" + fetchMetadataVO.getLine_number() +
	 * "_Passed").concat(".png"); System.out.println(image_dest); File destination =
	 * new File(image_dest); FileUtils.copyFile(source, destination);
	 * logger.info("Successfully Screenshot is taken"); return image_dest; } catch
	 * (Exception e) { logger.error("Failed During Taking screenshot");
	 * System.out.println("Exception while taking Screenshot" + e.getMessage());
	 * return e.getMessage(); } } public String screenshotFail(WebDriver driver,
	 * String screenshotName, FetchMetadataVO fetchMetadataVO, FetchConfigVO
	 * fetchConfigVO) { String image_dest = null; try { TakesScreenshot ts =
	 * (TakesScreenshot) driver; File source = ts.getScreenshotAs(OutputType.FILE);
	 * String currenttime = new
	 * SimpleDateFormat("MM-dd-yyyy HH-mm-ss").format(Calendar.getInstance().getTime
	 * ()); image_dest =(fetchConfigVO.getScreenshot_path()+"\\" +
	 * fetchMetadataVO.getCustomer_name() +
	 * "\\" + fetchMetadataVO.getTest_run_name()+ "
	 * \\" + fetchMetadataVO.getLine_number()+ "
	 * _" + fetchMetadataVO.getScenario_name() + "
	 * _" + fetchMetadataVO.getScript_number() + "_" +
	 * fetchMetadataVO.getTest_run_name() + "_" + fetchMetadataVO.getScript_id() +
	 * "_" + fetchMetadataVO.getInput_parameter() + "_Failed").concat(".png"); File
	 * destination = new File(image_dest); FileUtils.copyFile(source, destination);
	 * logger.info("Successfully Failed Screenshot is Taken "); return image_dest; }
	 * catch (Exception e) { logger.error("Failed during screenshotFail Action. ");
	 * System.out.println("Exception while taking Screenshot" + e.getMessage());
	 * return e.getMessage(); } } public String screenshotException(WebDriver
	 * driver, String screenshotName, List<FetchMetadataVO> fetchMetadataListVO,
	 * FetchConfigVO fetchConfigVO) { String image_dest = null; try {
	 * TakesScreenshot ts = (TakesScreenshot) driver; File source =
	 * ts.getScreenshotAs(OutputType.FILE); image_dest
	 * =(fetchConfigVO.getScreenshot_path()+"\\" +
	 * fetchMetadataListVO.get(0).getCustomer_name() +
	 * "\\" + fetchMetadataListVO.get(0).getTest_run_name()+ "
	 * \\" + fetchMetadataListVO.get(0).getLine_number()+ "
	 * _" + fetchMetadataListVO.get(0).getScenario_name() + "
	 * _" + fetchMetadataListVO.get(0).getScript_number() + "_" +
	 * fetchMetadataListVO.get(0).getTest_run_name() + "_" +
	 * fetchMetadataListVO.get(0).getScript_id() + "_" + "NoSuchElementException" +
	 * "_Failed").concat(".png"); File destination = new File(image_dest);
	 * FileUtils.copyFile(source, destination);
	 * logger.info("Successfully Failed Screenshot is Taken "); return image_dest; }
	 * catch (Exception e) { logger.error("Failed during screenshotFail Action. ");
	 * System.out.println("Exception while taking Screenshot" + e.getMessage());
	 * return e.getMessage(); } }
	 */
	public  void deleteAllCookies(WebDriver driver, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) {
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
	public  void selectByText(WebDriver driver, String inputParam, String param2, String inputData,
			FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) {
		try {
			WebElement waittext = driver.findElement(By.xpath(("//*[contains(text(),'"+inputParam+"')]/following::select[1]")));
			selectMethod(driver, inputData, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "//*[contains(text(),'"+inputParam+"')]/following::select[1]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return;
		} catch (Exception e) {
			System.out.println(inputParam);
		}try {
			WebElement waittext = driver.findElement(By.xpath(("//*[contains(text(),'"+inputParam+"')]/preceding-sibling::select[1]")));
			selectMethod(driver, inputData, fetchMetadataVO, waittext, fetchConfigVO);
			String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "//*[contains(text(),'"+inputParam+"')]/preceding-sibling::select[1]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return;
		} catch (Exception e) {
			System.out.println(inputParam);
			screenshotFail(driver, "Failed during selectByValue Method", fetchMetadataVO, fetchConfigVO);
			throw e;
		}
	}
	private void selectMethod(WebDriver driver, String inputData, FetchMetadataVO fetchMetadataVO,
			WebElement waittext, FetchConfigVO fetchConfigVO) {
		Select selectBox = new Select(waittext);
		selectBox.selectByVisibleText(inputData);
		logger.info("selectedBYText Successfully");
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		return;
	}
	public void selectByValue(WebDriver driver, String xpath, String inputData, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) {
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
	public  void switchToDefaultFrame(WebDriver driver) {
		try {
			driver.switchTo().defaultContent();
			logger.info("Successfully switched to default Frame");
		} catch (Exception e) {
			logger.error("Failed During Switching to default Action");
			throw e;
		}
	}
	public  String copynumber(WebDriver driver, String inputParam, String inputParam2, FetchMetadataVO fetchMetadataVO,

			FetchConfigVO fetchConfigVO) {
		try {
			WebElement webElement = driver.findElement(By.xpath("(//div[contains(text(),'"+inputParam+"')])[1]"));
			if(webElement.isDisplayed()==true) {
			copyMethod(webElement);
            String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "(//div[contains(text(),'inputParam')])[1]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return inputParam;
			}
		} catch (Exception e) {
			System.out.println(inputParam);
		}
		try {
			WebElement webElement = driver.findElement(By.xpath("//img[@title='In Balance ']/following::td[1]"));
			if(webElement.isDisplayed()==true) {
			copyMethod(webElement);
            String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "//img[@title='In Balance ']/following::td[1]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
			return inputParam;
			}
		} catch (Exception e) {
			logger.error("Failed During copynumber Action");
			screenshotFail(driver, "Failed during CopyNumber Method", fetchMetadataVO, fetchConfigVO);
			System.out.println(inputParam);
			throw e;
		}
		return inputParam;
	}	private void copyMethod(WebElement webElement) {
		String number = webElement.getText();
		System.out.println(number);
		String num = number.replaceAll("[^\\d.]+|\\.(?!\\d)", "");
		System.out.println(num);
		StringSelection stringSelection = new StringSelection(num);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
		logger.info("Successfully Copied the Number");
	}

	public  String copyy(WebDriver driver, String xpath, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) {
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
	public  String copytext(WebDriver driver, String xpath, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) {
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
	public void switchDefaultContent(WebDriver driver, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) {
		try {
			driver.switchTo().defaultContent();
			logger.info("Successfully Switched to Default Content");
		} catch (Exception e) {
			logger.error("Failed During switching to Default Action.");
			screenshotFail(driver, "Failed during switchDefaultContent Method", fetchMetadataVO, fetchConfigVO);
			System.out.println("can not switch");
			throw e;
		}
	}
	public  void dragAnddrop(WebDriver driver, String xpath, String xpath1, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) {
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
	public  void windowhandle(WebDriver driver, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
		try {
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

	public void switchToFrame(WebDriver driver, String param1, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws InterruptedException {
		Thread.sleep(5000);
        try {
              WebElement waittext = driver.findElement(By.xpath("//iframe[contains(@id,'"+param1+"')]"));
              Actions actions = new Actions(driver); 
              actions.moveToElement(waittext).build().perform();
              driver.switchTo().frame(waittext);
              String scripNumber = fetchMetadataVO.getScript_number();
  			String xpath = "//iframe[contains(@id,'param1')]";
  		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
              return;
        } catch (Exception e) {
              System.out.println(e);
        }try {
        	if(param1.equalsIgnoreCase("Overall Comments/Manager Rating") || param1.equalsIgnoreCase("Manager Comments") || param1.equalsIgnoreCase("Comments")) {
        		Thread.sleep(5000);
                WebElement waittext = driver.findElement(By.xpath("(//iframe[@class='cke_wysiwyg_frame cke_reset'])[1]"));
                Actions actions = new Actions(driver); 
    			actions.moveToElement(waittext).build().perform();
                driver.switchTo().frame(waittext);
                String scripNumber = fetchMetadataVO.getScript_number();
    			String xpath = "(//iframe[@class='cke_wysiwyg_frame cke_reset'])[1]";
    		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
                return;
            	}
        } catch (Exception e) {
                System.out.println(e);
        }try {
        	if((param1.equalsIgnoreCase("Goal 1,2,3") || (param1.equalsIgnoreCase("1. What was your overall impact on Arlo success")) || (param1.equalsIgnoreCase("1. Describe your overall goal achievement for the year")) || (param1.equalsIgnoreCase("Success Criteria")))) {
        		WebElement waittext = driver.findElement(By.xpath("(//iframe[@class='cke_wysiwyg_frame cke_reset'])[1]"));
        		Actions actions = new Actions(driver); 
    			actions.moveToElement(waittext).build().perform();
        		driver.switchTo().frame(waittext);
                String scripNumber = fetchMetadataVO.getScript_number();
    			String xpath = "(//iframe[@class='cke_wysiwyg_frame cke_reset'])[1]";
    		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
        		return;
        	}
        } catch (Exception e) {
        	System.out.println(e);
        }try {
        	if((param1.equalsIgnoreCase("Goal 4 5 6") || (param1.equalsIgnoreCase("2. What are you going to stop, start and continue in order to have an impact on Arlo success")) || (param1.equalsIgnoreCase("2. What were your additional accomplishments in the year")) || (param1.equalsIgnoreCase("Comments")) || (param1.equalsIgnoreCase("Employee Comments")))) {
        		WebElement waittext = driver.findElement(By.xpath("(//iframe[@class='cke_wysiwyg_frame cke_reset'])[2]"));
        		Actions actions = new Actions(driver); 
    			actions.moveToElement(waittext).build().perform();
        		driver.switchTo().frame(waittext);
                String scripNumber = fetchMetadataVO.getScript_number();
    			String xpath = "(//iframe[@class='cke_wysiwyg_frame cke_reset'])[2]";
    		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
        		return;	}
        } catch (Exception e) {
        	System.out.println(e);
        }try {
        	if(param1.equalsIgnoreCase("3. Describe two things you do well in line with the Arlo Values: Results, Customer Focus, Integrity")) {
        		WebElement waittext = driver.findElement(By.xpath("(//iframe[@class='cke_wysiwyg_frame cke_reset'])[3]"));
        		Actions actions = new Actions(driver); 
    			actions.moveToElement(waittext).build().perform();
        		driver.switchTo().frame(waittext);
                String scripNumber = fetchMetadataVO.getScript_number();
    			String xpath = "(//iframe[@class='cke_wysiwyg_frame cke_reset'])[3]";
    		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
        		return;
        	}
        } catch (Exception e) {
        	System.out.println(e);
        }try {
        	if(param1.equalsIgnoreCase("4. What are two things you could do more of in line with the Arlo Values: Results, Customer Focus")) {
        		WebElement waittext = driver.findElement(By.xpath("(//iframe[@class='cke_wysiwyg_frame cke_reset'])[4]"));
        		Actions actions = new Actions(driver); 
    			actions.moveToElement(waittext).build().perform();
        		driver.switchTo().frame(waittext);
                String scripNumber = fetchMetadataVO.getScript_number();
    			String xpath = "(//iframe[@class='cke_wysiwyg_frame cke_reset'])[4]";
    		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
        		return;
        	}
        } catch (Exception e) {
        	System.out.println(e);
        }try {
        	WebElement waittext = driver.findElement(By.xpath("//iframe[contains(@title,'"+param1+"')]"));
        	Actions actions = new Actions(driver); 
			actions.moveToElement(waittext).build().perform();
        	driver.switchTo().frame(waittext);
            String scripNumber = fetchMetadataVO.getScript_number();
			String xpath = "//iframe[contains(@title,'"+param1+"')]";
		    String scriptID=fetchMetadataVO.getScript_id();String metadataID=fetchMetadataVO.getScript_meta_data_id();service.saveXpathParams(scriptID,metadataID,xpath);
        	return;
      } catch (Exception e) {
            System.out.println(e);
            logger.error("Failed During switchToFrame Action");
            screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, fetchConfigVO);
            throw e;
      }
  }	public  void uploadFileAutoIT(String filelocation, FetchMetadataVO fetchMetadataVO) throws Exception {
		try {
	//		String autoitscriptpath = System.getProperty("user.dir") + "\\" + "File_upload_selenium_webdriver.au3";
//			Runtime.getRuntime().exec("cmd.exe /c Start AutoIt3.exe " + autoitscriptpath + " \"" + fetchConfigVO.getUpload_file_path()+"\\"+filelocation + "\"");
			Runtime.getRuntime().exec("C:\\Selenium\\Spring\\WinfoAutomation_MultiThread\\File_upload_selenium_webdriver.exe");
		//	Runtime.getRuntime().exec("E:\\AutoIT\\FileUpload.exe");
			Thread.sleep(5000);
			logger.info("Successfully Uploaded The File");
			} catch (Exception e) {
			logger.error("Failed During uploadFileAutoIT Action.");
			System.out.println(filelocation);
			e.printStackTrace();
			throw e;
		}
}
	public  void refreshPage(WebDriver driver, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) {
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


	public void DelatedScreenshoots(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO) throws IOException {
		File folder = new File(fetchConfigVO.getScreenshot_path() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
				+ fetchMetadataListVO.get(0).getTest_run_name() + "/");
		if (folder.exists()) {
		File[] listOfFiles = folder.listFiles();
		
//		String image=fetchConfigVO.getScreenshot_path() + fetchMetadataVO.getCustomer_name() + "/"
//				+ fetchMetadataVO.getTest_run_name() + "/" + fetchMetadataVO.getSeq_num() + "_"
//				+ fetchMetadataVO.getLine_number() + "_" + fetchMetadataVO.getScenario_name() + "_"
//				+ fetchMetadataVO.getScript_number() + "_" + fetchMetadataVO.getTest_run_name() + "_"
//				+ fetchMetadataVO.getLine_number();
			for (File file : Arrays.asList(listOfFiles)) {

				String seqNum = String.valueOf(file.getName().substring(0, file.getName().indexOf('_')));

			
			String seqnum1=fetchMetadataListVO.get(0).getSeq_num();
			if(seqNum.equalsIgnoreCase(seqnum1)) {
				Path imagesPath = Paths.get(file.getPath());
				 Files.delete(imagesPath);
			}
		}
		}
	}

	@Override
	public void multiplelinestableSendKeys(WebDriver driver, String param1, String param2, String param3,
			String input_value, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getErrorMessages(WebDriver driver) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void moveToElement(WebDriver driver, String input_parameter, FetchMetadataVO fetchMetadataVO,
			FetchConfigVO fetchConfigVO) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void switchToParentWindow(WebDriver driver, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void datePicker(WebDriver driver, String param1, String param2, String input_value,
			FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void switchParentWindow(WebDriver driver, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clickButtonCheckPopup(WebDriver driver, String param1, String param2, FetchMetadataVO fetchMetadataVO,
			FetchConfigVO fetchConfigVO) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void oicLogout(WebDriver driver, FetchConfigVO fetchConfigVO, FetchMetadataVO fetchMetadataVO, String type1,
			String type2, String type3, String param1, String param2, String param3) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String oicLoginPage(WebDriver driver, String param1, String keysToSend, FetchMetadataVO fetchMetadataVO,
			FetchConfigVO fetchConfigVO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void oicNavigate(WebDriver driver, FetchConfigVO fetchConfigVO, FetchMetadataVO fetchMetadataVO,
			String type1, String type2, String param1, String param2, int count) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String oicNavigator(WebDriver driver, String param1, FetchMetadataVO fetchMetadataVO,
			FetchConfigVO fetchConfigVO) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String oicMenuNavigation(WebDriver driver, String param1, FetchMetadataVO fetchMetadataVO,
			FetchConfigVO fetchConfigVO) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String oicMenuNavigationButton(WebDriver driver, FetchMetadataVO fetchMetadataVO,
			FetchConfigVO fetchConfigVO, String type1, String type2, String param1, String param2, int count)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void oicClickButton(WebDriver driver, String param1, String param2, FetchMetadataVO fetchMetadataVO,
			FetchConfigVO fetchConfigVO) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String oicSendValue(WebDriver driver, String param1, String param2, String keysToSend,
			FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void oicMouseHover(WebDriver driver, String param1, String param2, FetchMetadataVO fetchMetadataVO,
			FetchConfigVO fetchConfigVO) throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public List<String> openExcelFileWithSheet(WebDriver driver, String param1, String param2, String fileName,
			String sheetName, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String closeExcel() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String typeIntoCell(WebDriver driver, String param1, String value1, FetchMetadataVO fetchMetadataVO,
			FetchConfigVO fetchConfigVO, Integer addrowCounter) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer addRow(Integer addrow) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String menuItemOfExcel(WebDriver driver, String param1, FetchMetadataVO fetchMetadataVO,
			FetchConfigVO fetchConfigVO) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String loginToExcel(WebDriver driver, String param1, String param2, String username, String password,
			FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}





}
