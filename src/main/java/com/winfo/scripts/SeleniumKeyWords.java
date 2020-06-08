package com.winfo.scripts;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
//import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
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

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.lowagie.text.DocumentException;
import com.winfo.services.FetchConfigVO;
import com.winfo.services.FetchMetadataVO;
import com.winfo.utils.DateUtils;
import com.winfo.utils.StringUtils;

@Service
public class SeleniumKeyWords {

	 Logger logger = LogManager.getLogger(SeleniumKeyWords.class);
	/*
	 * private  Integer ElementWait = Integer
	 * .valueOf(PropertyReader.getPropertyValue(PropertyConstants.EXECUTION_TIME.
	 * value)); public  int WaitElementSeconds = new Integer(ElementWait);
	 */
	public String Main_Window = "";
	public  WebElement fromElement;
	public  WebElement toElement;
	
	public void loginApplication(WebDriver driver, FetchConfigVO fetchConfigVO,
			FetchMetadataVO fetchMetadataVO, String type1, String type2, String type3, String param1, String param2, String param3, String keysToSend, String value) throws Exception {
		String param5 = "Password";
		String param6 = "Sign In ";
		navigateUrl(driver, fetchConfigVO, fetchMetadataVO);
		sendValue(driver, param1, param3, keysToSend, fetchMetadataVO, fetchConfigVO);
		sendValue(driver, param5, param2, value, fetchMetadataVO, fetchConfigVO);
		clickSignInSignOut(driver, param6, fetchMetadataVO, fetchConfigVO);
//		clickButton(driver, param6, param2, fetchMetadataVO, fetchConfigVO);
	}
	
	public void navigate(WebDriver driver, FetchConfigVO fetchConfigVO,
			FetchMetadataVO fetchMetadataVO, String type1, String type2, String param1, String param2) throws Exception {
		String param3 = "Navigator";
		clickLink(driver, param3, param2, fetchMetadataVO, fetchConfigVO);
		clickMenu(driver, param1, param2, fetchMetadataVO, fetchConfigVO);
	}
	
	public void openTask(WebDriver driver, FetchConfigVO fetchConfigVO,
			FetchMetadataVO fetchMetadataVO, String type1, String type2, String param1, String param2) throws Exception {
		String param3 = "Tasks";
		clickImage(driver, param3, param2, fetchMetadataVO, fetchConfigVO);
		clickLink(driver, param1, param2, fetchMetadataVO, fetchConfigVO);
		
	}
	
	public void logout(WebDriver driver, FetchConfigVO fetchConfigVO,
			FetchMetadataVO fetchMetadataVO, String type1, String type2, String type3, String param1, String param2, String param3) throws Exception {
		String param4 = "UIScmil1u";
		String param5 = "Sign Out";
		String param6 = " Confirm";
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
		File folder = new File(System.getProperty("user.dir")+"\\"+"Screenshot\\" + fetchMetadataListVO.get(0).getCustomer_name() + "\\"+ fetchMetadataListVO.get(0).getTest_run_name() + "\\");
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
		File folder = new File(System.getProperty("user.dir")+"\\"+"Screenshot\\" + fetchMetadataListVO.get(0).getCustomer_name() + "\\"+ fetchMetadataListVO.get(0).getTest_run_name() + "\\");
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

	public void createPdf(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO, String pdffileName)
			throws IOException, DocumentException, com.itextpdf.text.DocumentException {
		try {
			String Date = DateUtils.getSysdate();
			String Folder =(fetchConfigVO.getPdf_path()+"\\"+ fetchMetadataListVO.get(0).getCustomer_name() +"\\" +fetchMetadataListVO.get(0).getTest_run_name()
					+ "\\");
			String FILE = (Folder + pdffileName);
			System.out.println(FILE);
			List<String> fileNameList = null;
			if("Passed_Report.pdf".equalsIgnoreCase(pdffileName)) {
				fileNameList = getPassedPdf(fetchMetadataListVO);
			}else if("Failed_Report.pdf".equalsIgnoreCase(pdffileName)) {
				fileNameList = getFailedPdf(fetchMetadataListVO);
			}else if("Detailed_Report.pdf".equalsIgnoreCase(pdffileName)) {
				fileNameList = getDetailPdf(fetchMetadataListVO);
			}else {
				fileNameList = getFileNameList(fetchMetadataListVO);
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
			PdfWriter.getInstance(document,
					new FileOutputStream(FILE));
			document.open();
			for (String image : fileNameList) {
				Image img = Image
						.getInstance(fetchConfigVO.getScreenshot_path()+"\\"+customer_Name + "\\" +test_Run_Name+ "\\" + image);
				String ScriptNumber = image.split("_")[2];
				String TestRun = image.split("_")[3];
				String Status = image.split("_")[6];
				String status = Status.split("\\.")[0];
				String Scenario = image.split("_")[1];
				document.setPageSize(img);
				document.newPage();
				Font fnt = FontFactory.getFont("Arial", 18);
				String TR = "Test Run Name:" + " " + TestRun;
				String SN = "Script Number:" + " " + ScriptNumber;
				String S  = "Status:" + " " + status;
				String Scenarios = "Scenario Name :" + "" + Scenario;
 				document.add(new Paragraph(TR, fnt)); 
				document.add(new Paragraph(SN, fnt));
				document.add(new Paragraph(S, fnt));
				document.add(new Paragraph(Scenarios, fnt));
				document.add(Chunk.NEWLINE);
				img.setAlignment(Image.ALIGN_CENTER);
				img.isScaleToFitHeight();
				img.scalePercent(65,65);
				document.add(img);
			}
			document.close();
		} catch (Exception e) {
			System.out.println("Not able to upload the pdf");
		}
	}
	public  void uploadPDF(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO){
		try {
			  String accessToken = getAccessTokenPdf();
			  List imageUrlList = new ArrayList();
			  File imageDir = new File(fetchConfigVO.getPdf_path()+"\\"+fetchMetadataListVO.get(0).getCustomer_name() +"\\" +fetchMetadataListVO.get(0).getTest_run_name()+"\\");
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
								  + fetchMetadataListVO.get(0).getCustomer_name() + "/" + fetchMetadataListVO.get(0).getTest_run_name()+"/" + imageFileName
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
	public void paste(WebDriver driver, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
		try {
			Actions action = new Actions(driver);
			action.keyDown(Keys.CONTROL).sendKeys("v").build().perform();
		} catch (Exception e) {
			screenshotFail(driver, "Failed during paste Method", fetchMetadataVO, fetchConfigVO);
			throw e;
		}
}
	public  void clear(WebDriver driver, String inputParam, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) {
		try { 
			WebElement waittill = driver.findElement(By.xpath("(//label[contains(text(),'"+inputParam+"')]/preceding-sibling::input)[1]"));
			clearMethod(driver, waittill);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}try { 
			WebElement waittill = driver.findElement(By.xpath("(//*[text()='"+inputParam+"']/following::input)[1]"));
			clearMethod(driver, waittill);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}try {
			WebElement waittill = driver.findElement(By.xpath("//*[contains(@placeholder,'"+inputParam+"')]"));
			clearMethod(driver, waittill);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			WebElement waittill = driver.findElement(By.xpath("//*[text()='"+inputParam+"']/following::textarea[1]"));
			clearMethod(driver, waittill);
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
	try { 
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[text()='Navigator']/following::*[text()='"+param1+"']/following::a[text()='"+param2+"']")));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h1[text()='Navigator']/following::*[text()='"+param1+"']/following::a[text()='"+param2+"']"), param2));
		WebElement waittext = driver.findElement(By.xpath("//h1[text()='Navigator']/following::*[text()='"+param1+"']/following::a[text()='"+param2+"']"));
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		return;
	} catch (Exception e) {
			System.out.println(e);
	}try { 
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//a[text()='" + param1 + "']"))));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(("//a[text()='" + param1 + "']")), param1));
		WebElement waittext = driver.findElement(By.xpath(("//a[text()='" + param1 + "']")));
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}try { 
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//div[contains(@style,'display: block')]//div[text()='" + param1 + "']"))));
		WebElement waittext = driver.findElement(By.xpath(("//div[contains(@style,'display: block')]//div[text()='" + param1 + "']")));
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}
	try { 
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//div[text()='" + param1 + "']"))));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(("//div[text()='" + param1 + "']")), param1));
		WebElement waittext = driver.findElement(By.xpath(("//div[text()='" + param1 + "']")));
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}
	try { 
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//div[contains(@id,'" + param1 + "')])[1]")));
		WebElement waittext = driver.findElement(By.xpath("(//div[contains(@id,'" + param1 + "')])[1]"));
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
	public void clickSignInSignOut(WebDriver driver, String param1, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
	try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//button[text()='" + param1 + "']"))));
		WebElement waittext = driver.findElement(By.xpath(("//button[text()='" + param1 + "']")));
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
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[text()='"+param1+"']/following::a[@title='"+param2+"'])[1]")));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(("//h1[text()='"+param1+"']")), param1));
		Thread.sleep(6000);
		WebElement waittext = driver.findElement(By.xpath("(//h1[text()='"+param1+"']/following::a[@title='"+param2+"'])[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		clickButtonDropdownText(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		return;
	}catch (Exception e) {
		System.out.println(e);
	}try {
		if(param2.equalsIgnoreCase("Publish to Managers")) {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[text()='"+param1+"']/following::a[text()='"+param2+"'])[2]")));
		WebElement waittext = driver.findElement(By.xpath("(//h1[text()='"+param1+"']/following::a[text()='"+param2+"'])[2]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		clickButtonDropdownText(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		return;
		}
	}catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[text()='"+param1+"']/following::a[text()='"+param2+"'])[1]")));
		WebElement waittext = driver.findElement(By.xpath("(//h1[text()='"+param1+"']/following::a[text()='"+param2+"'])[1]"));
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		clickButtonDropdownText(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
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
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		return;
	}catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[contains(text(),'"+param1+"')]/following::a[1]")));
		WebElement waittext = driver.findElement(By.xpath("//h1[contains(text(),'"+param1+"')]/following::a[1]"));
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		clickButtonDropdownText(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO);
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
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//li[text()='"+keysToSend+"']")));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(("//li[text()='"+keysToSend+"']")), keysToSend));
			Thread.sleep(5000);
			WebElement waittext = driver.findElement(By.xpath("//li[text()='"+keysToSend+"']"));
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
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			return;
		}catch (Exception e) {
			System.out.println(e);
		}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[text()='"+keysToSend+"']")));
		WebElement waittext = driver.findElement(By.xpath("//td[text()='"+keysToSend+"']"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
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
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h2[text()='" + param1 + "']/following::*[@title='" + param2 + "'])[1]")));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h2[text()='" + param1 + "']"), param1));
		WebElement waittext = driver.findElement(By.xpath("(//h2[text()='" + param1 + "']/following::*[@title='" + param2 + "'])[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		return;
	}catch (Exception e) {
		System.out.println(e);
	}
	try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()='"+ param1 +"']/following::*[text()='"+ param2 +"']/preceding::*[@title='Expand' and @href and not(@style='display:none')][1]")));
		WebElement waittext = driver.findElement(By.xpath("//*[text()='"+ param1 +"']/following::*[text()='"+ param2 +"']/preceding::*[@title='Expand' and @href and not(@style='display:none')][1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		return;
	}catch (Exception e) {
		System.out.println(e);
		logger.error("Failed during Click action.");
		screenshotFail(driver, "Failed during clickExpandorcollapse", fetchMetadataVO, fetchConfigVO);
		throw e;
	} 
}
	public void selectAValue(WebDriver driver, String param1,String param2, String keysToSend, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
		try {
			if(param1.equalsIgnoreCase("Worker Name")) {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			Thread.sleep(4000);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()='"+param1+"']/following::*[text()='"+keysToSend+"']/following::img[1]")));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[text()='"+param1+"']/following::*[text()='"+keysToSend+"']"), keysToSend));
			WebElement waittext = driver.findElement(By.xpath("//*[text()='"+param1+"']/following::*[text()='"+keysToSend+"']/following::img[1]"));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(1000);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
			}
		}catch (Exception e) {
			System.out.println(e);
		}try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(),'" + param1 + "')]/following::*[text()='"+keysToSend+"']")));
			WebElement waittext = driver.findElement(By.xpath("//*[contains(text(),'" + param1 + "')]/following::*[text()='"+keysToSend+"']"));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(2000);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		}catch (Exception e) {
			System.out.println(e);
		}
	try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()='" + param1 + "']/following::label[text()='"+param2+"']")));
		WebElement waittext = driver.findElement(By.xpath("//*[text()='" + param1 + "']/following::label[text()='"+param2+"']"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		Thread.sleep(2000);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
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
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()='"+param1+"']/following::*[text()='"+keysToSend+"']/following::img[contains(@id,'"+param2+"')][1]")));
			Thread.sleep(4000);
			WebElement waittill = driver.findElement(By.xpath("//*[text()='"+param1+"']/following::*[text()='"+keysToSend+"']/following::img[contains(@id,'"+param2+"')][1]"));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittill).build().perform();
			Thread.sleep(2000);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			clickValidateXpath(driver, fetchMetadataVO, waittill, fetchConfigVO);
			Thread.sleep(500);
			return keysToSend;	
		} catch (Exception e) {
			System.out.println(e);
		}try { 
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()='"+param1+"']/following::*[text()='"+keysToSend+"']/following::img[@title='"+param2+"'][1]")));
			WebElement waittill = driver.findElement(By.xpath("//*[text()='"+param1+"']/following::*[text()='"+keysToSend+"']/following::img[@title='"+param2+"'][1]"));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittill).build().perform();
			Thread.sleep(2000);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			clickValidateXpath(driver, fetchMetadataVO, waittill, fetchConfigVO);
			Thread.sleep(500);
			return keysToSend;	
		} catch (Exception e) {
			System.out.println(e);
		}try { 
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()='"+param1+"']/following::*[@value='"+keysToSend+"']/following::img[@title='"+param2+"'][1]")));
			WebElement waittill = driver.findElement(By.xpath("//*[text()='"+param1+"']/following::*[@value='"+keysToSend+"']/following::img[@title='"+param2+"'][1]"));
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
	
public void clickImage(WebDriver driver, String param1, String param2, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
	try {
		if(param2.equalsIgnoreCase("Back")) {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[text()='"+param1+"']/preceding::a[1]")));
		WebElement waittext = driver.findElement(By.xpath("//h1[text()='"+param1+"']/preceding::a[1]"));
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		return;
		}
	}catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[text()='" + param1 + "']/following::div[@role='button'])[1]")));
		Thread.sleep(2000);
		WebElement waittext = driver.findElement(By.xpath("(//h1[text()='" + param1 + "']/following::div[@role='button'])[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		Thread.sleep(3000);
		WebElement add = driver.findElement(By.xpath("//h1[text()='"+param1+"']/following::img[@title='"+param2+"']"));
		clickValidateXpath(driver, fetchMetadataVO, add, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		return;
	}catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[text()='"+param1+"']/following::img[@title='"+param2+"'])[1]")));
		WebElement waittext = driver.findElement(By.xpath("(//h1[text()='"+param1+"']/following::img[@title='"+param2+"'])[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		return;
	}catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[text()='" + param1 + "']/following::div[@role='button'])[1]")));
		WebElement waittext = driver.findElement(By.xpath("(//*[text()='" + param1 + "']/following::div[@role='button'])[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		Thread.sleep(3000);
		WebElement add = driver.findElement(By.xpath("//*[text()='"+param1+"']/following::img[@title='"+param2+"']"));
		clickValidateXpath(driver, fetchMetadataVO, add, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		return;
	}catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()='"+param1+"']/following::img[contains(@id,'"+param2+"')]")));
		WebElement waittext = driver.findElement(By.xpath("//*[text()='"+param1+"']/following::img[contains(@id,'"+param2+"')]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		Thread.sleep(2000);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		return;
	}catch (Exception e) {
		System.out.println(e);
	} try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()='"+param1+"']/following::img[@title='"+param2+"'][1]")));
		WebElement waittext = driver.findElement(By.xpath("//*[text()='"+param1+"']/following::img[@title='"+param2+"'][1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		return;
	}catch (Exception e) {
		System.out.println(e);
	} 
	try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@title='"+param1+"']")));
		WebElement waittext = driver.findElement(By.xpath("//img[@title='"+param1+"']"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		return;
	}catch (Exception e) {
		System.out.println(e);
	} try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[contains(@id,'"+param1+"')]")));
		WebElement waittext = driver.findElement(By.xpath("//img[contains(@id,'"+param1+"')]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
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
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}
	try {
		 WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		 wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()='"+param1+"']/following::*[text()='"+param2+"']/following::img[1]")));
		 WebElement waittext = driver.findElement(By.xpath("//*[text()='"+param1+"']/following::*[text()='"+param2+"']/following::img[1]"));
		 Actions actions = new Actions(driver); 
		 actions.moveToElement(waittext).build().perform();
		 clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		return;
	}catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@aria-label,'" +param1+ "')]")));
		WebElement waittext = driver.findElement(By.xpath("//*[contains(@aria-label,'" +param1+ "')]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
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
                  clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
                  return;
            }else if(param2.equalsIgnoreCase("Done")) {
                WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
                Thread.sleep(2000);
                wait.until(ExpectedConditions
                            .presenceOfElementLocated(By.xpath(("//span[text()='o']"))));
                wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()='o']"), "o"));
                WebElement waittext = driver.findElement(By.xpath(("//span[text()='o']")));
                screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
                clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
                return;
          }else if(param1.equalsIgnoreCase("Submit")) {
                  WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
                  wait.until(ExpectedConditions
                              .presenceOfElementLocated(By.xpath(("//span[text()='m']"))));
                  wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()='m']"), "m"));
                  Thread.sleep(10000);
                  WebElement waittext = driver.findElement(By.xpath(("//span[text()='m']")));
                  screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
                  Actions actions = new Actions(driver); 
                  actions.moveToElement(waittext).build().perform();
                  clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
                  Thread.sleep(3000);
                  return;
            }else if(param2.equalsIgnoreCase("Submit")) {
                WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
                wait.until(ExpectedConditions
                            .presenceOfElementLocated(By.xpath(("//*[text()='"+param1+"']/following::span[text()='m']"))));
                wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[text()='"+param1+"']/following::span[text()='m']"), "m"));
                Thread.sleep(4000);
                WebElement waittext = driver.findElement(By.xpath(("//*[text()='"+param1+"']/following::span[text()='m']")));
                screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
                clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
                Thread.sleep(3000);
                return;
          }else if(param1.equalsIgnoreCase("Next")) {
                  WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
                  wait.until(ExpectedConditions
                              .presenceOfElementLocated(By.xpath(("//span[text()='x']"))));
                  wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()='x']"), "x"));
                  Thread.sleep(4000);
                  WebElement waittext = driver.findElement(By.xpath(("//span[text()='x']")));
                  screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
                  Actions actions = new Actions(driver); 
                  actions.moveToElement(waittext).build().perform();
                  clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
                  Thread.sleep(8000);
                  return;
            }else if(param2.equalsIgnoreCase("Next")) {
                WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
                wait.until(ExpectedConditions
                            .presenceOfElementLocated(By.xpath("//*[text()='"+param1+"']/following::*[text()='"+param2+"']")));
                wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[text()='"+param1+"']/following::*[text()='"+param2+"']"), "x"));
                WebElement waittext = driver.findElement(By.xpath("//*[text()='"+param1+"']/following::*[text()='"+param2+"']"));
                screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
                clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
                Thread.sleep(8000);
                return;
          }else if(param2.equalsIgnoreCase("Yes")) {
                  WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
                  wait.until(ExpectedConditions
                              .presenceOfElementLocated(By.xpath(("//*[text()='"+param1+"']/following::span[text()='Y']"))));
                  wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[text()='"+param1+"']/following::span[text()='Y']"), "Y"));
                  Thread.sleep(4000);
                  WebElement waittext = driver.findElement(By.xpath(("//*[text()='"+param1+"']/following::span[text()='Y']")));
                  screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
                  clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
                  Thread.sleep(2000);
                  return;
            }else if(param2.equalsIgnoreCase("OK")) {
                WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
                wait.until(ExpectedConditions
                            .presenceOfElementLocated(By.xpath("//button[@_afrpdo='ok' and @accesskey='K']")));
                wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//button[@_afrpdo='ok' and @accesskey='K']"), "K"));
                Thread.sleep(4000);
                WebElement waittext = driver.findElement(By.xpath("//button[@_afrpdo='ok' and @accesskey='K']"));
                screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
                clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
                Thread.sleep(4000);
                return;
          }else if(param1.equalsIgnoreCase("Save and Close")) {
                WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
                wait.until(ExpectedConditions
                            .presenceOfElementLocated(By.xpath(("//span[text()='S']"))));
                wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()='S']"), "S"));
                Thread.sleep(4000);
                WebElement waittext = driver.findElement(By.xpath(("//span[text()='S']")));
                screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
                clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
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
              clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
              return;
          }else if(param2.equalsIgnoreCase("Continue")) {
              WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
              wait.until(ExpectedConditions
                          .presenceOfElementLocated(By.xpath(("//button[text()='Contin']"))));
              wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//button[text()='Contin']"), "Contin"));
              WebElement waittext = driver.findElement(By.xpath(("//button[text()='Contin']")));
              screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
              clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
              return;
          }else if(param1.equalsIgnoreCase("Close")) {
                  WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
                  wait.until(ExpectedConditions
                              .presenceOfElementLocated(By.xpath(("//button[text()='Cl']"))));
                  Thread.sleep(5000);
                  WebElement waittext = driver.findElement(By.xpath(("//button[text()='Cl']")));
                  Actions actions = new Actions(driver); 
                  actions.moveToElement(waittext).build().perform();
                  clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
                  return;
            }else if(param1.equalsIgnoreCase("Cancel")) {
                WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
                wait.until(ExpectedConditions
                            .presenceOfElementLocated(By.xpath(("//span[text()='C']"))));
                Thread.sleep(2000);
                WebElement waittext = driver.findElement(By.xpath(("//span[text()='C']")));
                Actions actions = new Actions(driver); 
                actions.moveToElement(waittext).build().perform();
                clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
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
                clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
                return;
          }
        } catch (Exception e) {
            System.out.println(e);
		}try {
			if(param2.equalsIgnoreCase("OK")) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				wait.until(ExpectedConditions
                            .presenceOfElementLocated(By.xpath(("//*[text()=\""+param1+"\"]/following::span[text()='K']"))));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[text()=\""+param1+"\"]/following::span[text()='K']"), "K"));
				WebElement waittext = driver.findElement(By.xpath(("//*[text()=\""+param1+"\"]/following::span[text()='K']")));
				screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
				clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
				Thread.sleep(4000);
				return;
          }
		} catch (Exception e) {
			System.out.println(e);
		}try {
			if(param2.equalsIgnoreCase("Submit")) {
                WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
                wait.until(ExpectedConditions
                            .presenceOfElementLocated(By.xpath(("//*[text()='"+param1+"']/following::span[text()='"+param2+"']"))));
                WebElement waittext = driver.findElement(By.xpath(("//*[text()='"+param1+"']/following::span[text()='"+param2+"']")));
                screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
                clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
                Thread.sleep(3000);
                return;
          }
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//span[text()='" + param1 + "']"))));
			WebElement waittext = driver.findElement(By.xpath(("//span[text()='" + param1 + "']")));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(5000);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}
		try { 
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//td[(text()='" +param1+ "')]"))));
			WebElement waittext = driver.findElement(By.xpath(("//td[(text()='" +param1+ "')]")));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(2000);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}try { 
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//button[text()='" +param1+ "'and not(@style='display:none')]"))));
			WebElement waittext = driver.findElement(By.xpath(("//button[text()='" +param1+ "'and not(@style='display:none')]")));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(2000);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			Thread.sleep(5000);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}try { 
			if(param1.equalsIgnoreCase("Warning")) {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()='Warning']/following::*[contains(text(),'Do you want to continue?') and not(text()=\"Your changes aren't saved. If you leave this page, then your changes will be lost. Do you want to continue?\")]/following::*[text()='Yes']")));
			WebElement waittext = driver.findElement(By.xpath("//*[text()='Warning']/following::*[contains(text(),'Do you want to continue?') and not(text()=\"Your changes aren't saved. If you leave this page, then your changes will be lost. Do you want to continue?\")]/following::*[text()='Yes']"));
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(1000);
			return;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		try { 
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[text()='"+param1+"']/following::*[text()='"+param2+"' and not(@_afrpdo)])[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//*[text()='"+param1+"']/following::*[text()='"+param2+"' and not(@_afrpdo)])[1]"));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(1000);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}
		try { 
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[text()='"+param1+"']/following::*[text()='"+param2+"'])[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//*[text()='"+param1+"']/following::*[text()='"+param2+"'])[1]"));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			Thread.sleep(1000);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}
		try { 
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[text()=\""+param1+"\"]/following::*[text()='"+param2+"'])[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//*[text()=\""+param1+"\"]/following::*[text()='"+param2+"'])[1]"));
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			Thread.sleep(3000);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
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
	public void tableRowSelect(WebDriver driver, String param1, String param2, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//table[@summary='"+param1 +"']//td)[1]")));
			WebElement waittext = driver.findElement(By.xpath("(//table[@summary='"+param1 +"']//td)[1]"));
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
	public void clickLink(WebDriver driver, String param1, String param2, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
	try {
		if(param1.equalsIgnoreCase("Viewing plan")){
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='" + param1 + "']/following::a[1]")));
			Thread.sleep(4000);
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[text()='" + param1 + "']"), param1));
			WebElement waittext = driver.findElement(By.xpath("//span[text()='" + param1 + "']/following::a[1]"));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		}
	} catch (Exception e) {
		System.out.println(e);
	}try {
		if(param2!=null){
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[text()='"+param1+"']/following::label[text()='"+param2+"']/following::a[1]")));
			Thread.sleep(4000);
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h1[text()='"+param1+"']/following::label[text()='"+param2+"']"), param2));
			WebElement waittext = driver.findElement(By.xpath("//h1[text()='"+param1+"']/following::label[text()='"+param2+"']/following::a[1]"));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		}
	} catch (Exception e) {
		System.out.println(e);
	}try {
		if(param1.equalsIgnoreCase("Select Template")){
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()='"+param1+"']/following::a[@title='"+param2+"'][1]")));
			WebElement waittext = driver.findElement(By.xpath("//*[text()='"+param1+"']/following::a[@title='"+param2+"'][1]"));
			Actions actions = new Actions(driver); 
			actions.moveToElement(waittext).build().perform();
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		}
	} catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//a[(text()='" + param1 + "')][1]"))));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//a[(text()='" + param1 + "')and @onclick]"), param1));
		WebElement waittext = driver.findElement(By.xpath("//a[(text()='" + param1 + "')][1]"));
		Thread.sleep(5000);
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		Thread.sleep(2000);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
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
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
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
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
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
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
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
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
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
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
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
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[text()='"+param1+"']/following::a[text()='"+param2+"']"))));
		WebElement waittext = driver.findElement(By.xpath(("//*[text()='"+param1+"']/following::a[text()='"+param2+"']")));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		return;
	} catch (Exception e) {
		System.out.println(e);
		logger.error("Failed during Click action.");
		screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, fetchConfigVO);
		throw e;
	}
}
	public void clickRadiobutton(WebDriver driver, String param1,String param2, String keysToSend, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath(("(//*[text()='" + param1 + "']/following::label[text()='"
							+ param2 + "']/following::label[text()='" + keysToSend + "'])[1]"))));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath("//*[text()='" + param1 + "']/following::label[text()='" + param2 + "']"), param2));
			WebElement waittext = driver.findElement(By.xpath("(//*[text()='" + param1 + "']/following::label[text()='"
					+ param2 + "']/following::label[text()='" + keysToSend + "'])[1]"));
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
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath(("(//*[text()='" + param1 + "']/following::label[text()='" + keysToSend + "'])[1]"))));
			WebElement waittext = driver.findElement(By.xpath(("(//*[text()='" + param1 + "']/following::label[text()='" + keysToSend + "'])[1]")));
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
	public void clickCheckbox(WebDriver driver, String param1, String keysToSend, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//label[text()='"+param1+"']/following::span[text()='"+keysToSend+"']/preceding::label[1]"
					))));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//label[text()='"+param1+"']/following::span[text()='"+keysToSend+"']"), keysToSend));
			WebElement waittext = driver.findElement(By.xpath("//label[text()='"+param1+"']/following::span[text()='"+keysToSend+"']/preceding::label[1]"));
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			tab(driver, fetchMetadataVO, fetchConfigVO);
			Thread.sleep(500);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[text()='"+param1+"']/following::span[text()='"+keysToSend+"']/preceding::label[1]"))));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[text()='"+param1+"']/following::span[text()='"+keysToSend+"']"), keysToSend));
			WebElement waittext = driver.findElement(By.xpath("//*[text()='"+param1+"']/following::span[text()='"+keysToSend+"']/preceding::label[1]"));
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			tab(driver, fetchMetadataVO, fetchConfigVO);
			Thread.sleep(500);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//label[text()='"+param1+"']/following::label[text()='"+keysToSend+"']"))));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//label[text()='"+param1+"']/following::label[text()='"+keysToSend+"']"), keysToSend));
			WebElement waittext = driver.findElement(By.xpath("//label[text()='"+param1+"']/following::label[text()='"+keysToSend+"']"));
			Thread.sleep(1000);
			clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
			tab(driver, fetchMetadataVO, fetchConfigVO);
			Thread.sleep(500);
			screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
			return;
		} catch (Exception e) {
			System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[text()='"+param1+"']/following::*[text()='"+keysToSend+"']"))));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[text()='"+param1+"']/following::*[text()='"+keysToSend+"']"), keysToSend));
		WebElement waittext = driver.findElement(By.xpath("//*[text()='"+param1+"']/following::*[text()='"+keysToSend+"']"));
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
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//label[text()='"+keysToSend+"']"))));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//label[text()='"+keysToSend+"']"), keysToSend));
		WebElement waittext = driver.findElement(By.xpath("//label[text()='"+keysToSend+"']"));
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
public void clickLinkAction(WebDriver driver, String param1,String param2, String keysToSend, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
	

	try {
		WebDriverWait wait = new WebDriverWait(driver,fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(("//*[text()='"+param1+"']/following::a[text()='"+keysToSend+"']/following::img[contains(@title,'"+param2+"')][1]"))));
		Thread.sleep(2000);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[text()='"+param1+"']/following::a[text()='"+keysToSend+"']"),keysToSend)); 
		WebElement waittext =driver.findElement(By.xpath("//*[text()='"+param1+"']/following::a[text()='"+keysToSend+"']/following::img[contains(@title,'"+param2+"')][1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickValidateXpath(driver, fetchMetadataVO, waittext,fetchConfigVO); Thread.sleep(2000); 
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO); 
		return; 
	}catch(Exception e) {
		System.out.println(e);
	}
	try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()='"+param1+"']/following::a[text()='"+keysToSend+"']")));
		WebElement waittext = driver.findElement(By.xpath("//*[text()='"+param1+"']/following::a[text()='"+keysToSend+"']"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}
	try {//*[text()='Worker Name']/following::*[text()='Kaushik (Kaushik) Sekaran']/following::img[1]
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[text()='"+keysToSend+"']/following::td[text()='"+param1+"']/following::table[1]//div)[1]")));
		WebElement waittext = driver.findElement(By.xpath("(//*[text()='"+keysToSend+"']/following::td[text()='"+param1+"']/following::table[1]//div)[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		Thread.sleep(1000);
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
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[text()='"+param1+"']/following::label[text()='"+param2+"']/following::textarea)[1]")));
		Thread.sleep(1000);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[text()='"+param1+"']/following::label[text()='"+param2+"']"), param2));
		WebElement waittill = driver.findElement(By.xpath("//*[text()='"+param1+"']/following::label[text()='"+param2+"']/following::textarea[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittill).build().perform();
		typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
		Thread.sleep(500);
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
                          .presenceOfElementLocated(By.xpath(("//div[@role='"+param1+"' and not(@style)]//label[text()='"+param2+"']/following::input[1]"))));
              wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//div[@role='"+param1+"' and not(@style)]//label[text()='"+param2+"']"), param2));
              WebElement waittill = driver.findElement(By.xpath(("//div[@role='"+param1+"' and not(@style)]//label[text()='"+param2+"']/following::input[1]")));
              screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
              typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
              return keysToSend;
        }
        else {
        	System.out.println("Not Enteed Name");
        }
	}catch (Exception e) {
    		System.out.println(e);
    	}
	try { 
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[contains(text(),'"+param1+"')]/following::label[text()='"+param2+"']/following::input)[1]")));
		Thread.sleep(1000);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h1[contains(text(),'"+param1+"')]/following::label[text()='"+param2+"']"), param2));
		WebElement waittill = driver.findElement(By.xpath("//h1[contains(text(),'"+param1+"')]/following::label[text()='"+param2+"']/following::input[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittill).build().perform();
		typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
		Thread.sleep(500);
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
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//label[text()='"+param1+"']/following::label[text()='"+param2+"']/following::input)[1]")));
		Thread.sleep(1000);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//label[text()='"+param1+"']/following::label[text()='"+param2+"']"), param2));
		WebElement waittill = driver.findElement(By.xpath("//label[text()='"+param1+"']/following::label[text()='"+param2+"']/following::input[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittill).build().perform();
		typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
		Thread.sleep(500);
		return keysToSend;
	} catch (Exception e) {
		System.out.println(e);
	}try { 
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[text()=\""+param1+"\"]/following::label[text()='"+param2+"']/following::input)[1]")));
		Thread.sleep(1000);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[text()=\""+param1+"\"]/following::label[text()='"+param2+"']"), param2));
		WebElement waittill = driver.findElement(By.xpath("//*[text()=\""+param1+"\"]/following::label[text()='"+param2+"']/following::input[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittill).build().perform();
		typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
		Thread.sleep(500);
		return keysToSend;	
	} catch (Exception e) {
		System.out.println(e);
	}try { 
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[text()='"+param1+"']/following::*[text()='"+param2+"']/following::input)[1]")));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[text()='"+param1+"']/following::*[text()='"+param2+"']"), param2));
		WebElement waittill = driver.findElement(By.xpath("//*[text()='"+param1+"']/following::*[text()='"+param2+"']/following::input[1]"));
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
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[text()='"+param1+"']/following::*[text()='"+param2+"']/following::span[text()='"+keysToSend+"']")));
		Thread.sleep(4000);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h1[text()='"+param1+"']/following::*[text()='"+param2+"']/following::span[text()='"+keysToSend+"']"), keysToSend));
		WebElement waittext = driver.findElement(By.xpath("//h1[text()='"+param1+"']/following::*[text()='"+param2+"']/following::span[text()='"+keysToSend+"']"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}
	try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[text()='"+param1+"']/following::*[text()=\""+param2+"\"]/following::*[text()='"+keysToSend+"']")));
		WebElement waittext = driver.findElement(By.xpath("//h1[text()='"+param1+"']/following::*[text()=\""+param2+"\"]/following::*[text()='"+keysToSend+"']"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}
	try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//label[text()='"+param1+"']/following::*[text()='"+param2+"']/following::*[text()='"+keysToSend+"']")));
		WebElement waittext = driver.findElement(By.xpath("//label[text()='"+param1+"']/following::*[text()='"+param2+"']/following::*[text()='"+keysToSend+"']"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h3[text()='"+param1+"']/following::label[text()='"+param2+"'][1]/following::div[contains(@style,'position')]//li[text()='"+keysToSend+"']")));
		WebElement waittext = driver.findElement(By.xpath("//label[text()='"+param1+"']/following::*[text()='"+param2+"']/following::*[text()='"+keysToSend+"']"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}
	try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h3[text()='"+param1+"']/following::label[text()='"+param2+"'][1]/following::div[contains(@style,'position')]//li[text()='"+keysToSend+"']")));
		WebElement waittext = driver.findElement(By.xpath("//h3[text()='"+param1+"']/following::label[text()='"+param2+"'][1]/following::div[contains(@style,'position')]//li[text()='"+keysToSend+"']"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()='"+param1+"']/following::*[text()=\""+param2+"\"]/following::*[@type='checkbox']/following::*[text()='"+keysToSend+"']")));
		WebElement waittext = driver.findElement(By.xpath("//*[text()='"+param1+"']/following::*[text()=\""+param2+"\"]/following::*[@type='checkbox']/following::*[text()='"+keysToSend+"']"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		Thread.sleep(500);
		tab(driver, fetchMetadataVO, fetchConfigVO);
		Thread.sleep(500);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()='"+param1+"']/following::*[text()=\""+param2+"\"]/following::*[text()='"+keysToSend+"']")));
		WebElement waittext = driver.findElement(By.xpath("//*[text()='"+param1+"']/following::*[text()=\""+param2+"\"]/following::*[text()='"+keysToSend+"']"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
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
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()='Search']/following::*[text()='"+param2+"']/following::input[1]")));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[text()='Search']/following::*[text()='"+param2+"']"), param2));
		WebElement searchResult = driver.findElement(By.xpath("//*[text()='Search']/following::*[text()='"+param2+"']/following::input[1]"));
		typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
		if(keysToSend!=null) {
			enter(driver, fetchMetadataVO, fetchConfigVO);
			Thread.sleep(5000);
			WebElement text = driver.findElement(By.xpath("(//span[text()='"+keysToSend+"'])[1]"));
			text.click();
		}
		WebElement button = driver.findElement(By.xpath("//*[text()='Search']/following::*[text()='"+param2+"']/following::*[text()='K'][1]"));
		button.click();	
		return;
	} catch (Exception e) {
		System.out.println(e);
		}
	try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()='Search']/following::*[text()='Name']/following::input[1]")));
		WebElement searchResult = driver.findElement(By.xpath("//*[text()='Search']/following::*[text()='Name']/following::input[1]"));
		typeIntoValidxpath(driver, keysToSend, searchResult, fetchConfigVO, fetchMetadataVO);
		enter(driver, fetchMetadataVO, fetchConfigVO);
		Thread.sleep(5000);
		WebElement text = driver.findElement(By.xpath("//span[text()='"+keysToSend+"']"));
		text.click();
		Thread.sleep(1000);
		WebElement button = driver.findElement(By.xpath("//*[text()='Search']/following::*[text()='Name']/following::*[text()='OK'][1]"));
		button.click();	
		return;
	} catch (Exception e) {
		System.out.println(e);
	}
	try {
		WebElement button = driver.findElement(By.xpath("//*[text()='Search']/following::*[text()='"+param2+"']/following::*[text()='OK'][1]"));
		button.click();	
		return;
	} catch (Exception e) {
		System.out.println(e);
	}
	try { 
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[contains(text(),'"+param1+"')]/following::label[text()='"+keysToSend+"']/following::input)[1]")));
		Thread.sleep(1000);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h1[contains(text(),'"+param1+"')]/following::label[text()='"+keysToSend+"']"), keysToSend));
		WebElement waittill = driver.findElement(By.xpath("//h1[contains(text(),'"+param1+"')]/following::label[text()='"+keysToSend+"']/following::input[1]"));
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
	public void tableSendKeys(WebDriver driver, String param1, String param2, String param3,String keysToSend,
		FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
	try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		WebElement waittill = driver.findElement(By.xpath("//h1[text()='"+param1+"']/following::label[text()='"+param2+"']/preceding-sibling::input[not(@type='hidden')]"));
		Thread.sleep(1000);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h1[text()='"+param1+"']/following::label[text()='"+param2+"']"), param2));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittill).build().perform();
		typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}try {
		WebElement waittill = driver.findElement(By.xpath("//h1[text()='"+param1+"']/following::*[text()='"+param2+"']/preceding-sibling::input[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittill).build().perform();
		typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}try {
		WebElement waittill = driver.findElement(By.xpath("//h1[text()='"+param1+"']/following::label[text()='"+param2+"']/preceding::input[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittill).build().perform();
		typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}try {
		WebElement waittill = driver.findElement(By.xpath("(//*[text()='"+param1+"']/following::label[text()='"+param2+"']/preceding-sibling::input)[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittill).build().perform();
		typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}
	try {
		WebElement waittill = driver.findElement(By.xpath("//*[text()='"+param1+"']/following::table[@summary='"+param2+"']//*[text()='"+param3+"']/following::input[contains(@id,'NewBdgtPctLst')][1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittill).build().perform();
		typeIntoValidxpath(driver, keysToSend, waittill, fetchConfigVO, fetchMetadataVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}try {
	//	tab(driver, fetchMetadataVO, fetchConfigVO);
	//	Thread.sleep(1000);
	//	enter(driver, fetchMetadataVO, fetchConfigVO);
		WebElement waittill = driver.findElement(By.xpath("(//table[@summary='"+param1+"']//label[text()='"+param2+"']/preceding-sibling::input)[1]"));
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
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table[@summary='"+param1+"']/following::li[text()='"+keysToSend+"']")));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//table[@summary='"+param1+"']/following::li[text()='"+keysToSend+"']"), keysToSend));
		WebElement waittext = driver.findElement(By.xpath("//table[@summary='"+param1+"']/following::li[text()='"+keysToSend+"']"));
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()='"+param1+"']/following::li[text()='"+keysToSend+"']")));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[text()='"+param1+"']/following::li[text()='"+keysToSend+"']"), keysToSend));
		WebElement waittext = driver.findElement(By.xpath("//*[text()='"+param1+"']/following::li[text()='"+keysToSend+"']"));
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
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
	try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()='"+param1+"']/following::label[text()='"+param2+"']/preceding::a[1]")));
		WebElement waittext = driver.findElement(By.xpath("//*[text()='"+param1+"']/following::label[text()='"+param2+"']/preceding::a[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		tableDropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
		Thread.sleep(3000);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table[@summary='"+param1+"']//input/following-sibling::a[1]")));
		WebElement waittext = driver.findElement(By.xpath("//table[@summary='"+param1+"']//input/following-sibling::a[1]"));
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

public void dropdownValues(WebDriver driver, String param1, String param2,String param3, String keysToSend,
		FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
	try {
		if(!param3.isEmpty()) {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[(text()='"+param1+"')]/following::label[text()='"+param2+"']/following::a[@title='"+param3+"']")));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h1[(text()='"+param1+"')]/following::label[text()='"+param2+"']"), param2));
			WebElement waittext = driver.findElement(By.xpath("//h1[(text()='"+param1+"')]/following::label[text()='"+param2+"']/following::a[@title='"+param3+"']"));
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
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1[text()=\""+param1+"\"]/following::*[text()=\""+param2+"\"]/following::a)[1]")));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("(//h1[text()=\""+param1+"\"]/following::*[text()=\""+param2+"\"]"), param2));
		WebElement waittext = driver.findElement(By.xpath("(//h1[text()=\""+param1+"\"]/following::*[text()=\""+param2+"\"]/following::a)[1]"));
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
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//label[text()='"+param1+"']/following::*[text()='"+param2+"']/following::a[1]")));
		WebElement waittext = driver.findElement(By.xpath("//label[text()='"+param1+"']/following::*[text()='"+param2+"']/following::a[1]"));
		Actions actions = new Actions(driver); 
		actions.moveToElement(waittext).build().perform();
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//div[contains(@id,'popup-container')]//*[text()='"+param1+"']/following::*[text()=\""+param2+"\"]/following::a)[1]")));
		WebElement waittext = driver.findElement(By.xpath("(//div[contains(@id,'popup-container')]//*[text()='"+param1+"']/following::*[text()=\""+param2+"\"]/following::a)[1]"));
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		return;
	} catch (Exception e) {
		System.out.println(e);
	}try {
		WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[text()='"+param1+"']/following::*[text()=\""+param2+"\"]/following::a)[1]")));
		WebElement waittext = driver.findElement(By.xpath("(//*[text()='"+param1+"']/following::*[text()=\""+param2+"\"]/following::a)[1]"));
		clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
		dropdownTexts(driver, param1, param2, keysToSend, fetchMetadataVO, fetchConfigVO);
		screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
		return;
	} catch (Exception e) {
		System.out.println(e);
		logger.error("Failed during Click action.");
		screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, fetchConfigVO);
		throw e;
	}
}
	private  void clickValidateXpath(WebDriver driver, FetchMetadataVO fetchMetadataVO, WebElement waittext, FetchConfigVO fetchConfigVO) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			driver.manage().timeouts().setScriptTimeout(fetchConfigVO.getWait_time(), TimeUnit.SECONDS);
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
				WebElement waittill = driver.findElement(By.xpath("//span[text()='"+inputParam+"'][1]"));
			//	((JavascriptExecutor)driver).executeScript("document.body.style.zoom='50%';");
					scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
				//	((JavascriptExecutor)driver).executeScript("document.body.style.zoom='100%';");
					logger.info("ScrollUsingElement Successfully Done!");
					return;
			}catch(Exception e) {
				System.out.println(inputParam);
			}
			try {
				WebElement waittill = driver.findElement(By.xpath("//a[text()='"+inputParam+"']"));
					scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
					logger.info("ScrollUsingElement Successfully Done!");
					return;
			}catch(Exception e) {
				System.out.println(inputParam);
			}
			try {
				WebElement waittill = driver.findElement(By.xpath("//h1[text()='"+inputParam+"']"));
					scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
					logger.info("ScrollUsingElement Successfully Done!");
					return;
			}catch(Exception e) {
				System.out.println(inputParam);
			}
			try {
				WebElement waittill = driver.findElement(By.xpath("(//h2[text()='"+inputParam+"'])"));
					scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
					logger.info("ScrollUsingElement Successfully Done!");
					return;
			}catch(Exception e) {
				System.out.println(inputParam);
			}try {
				WebElement waittill = driver.findElement(By.xpath("(//h3[text()='"+inputParam+"'])[2]"));
				scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
				logger.info("ScrollUsingElement Successfully Done!");
				return;
		}catch(Exception e) {
			System.out.println(inputParam);
		}try {
				WebElement waittill = driver.findElement(By.xpath("//td[text()='"+inputParam+"']"));
				scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
				logger.info("ScrollUsingElement Successfully Done!");
				return;
		}catch(Exception e) {
			System.out.println(inputParam);
		}
			try {
				WebElement waittill = driver.findElement(By.xpath("//div[contains(text(),'"+inputParam+"')]"));
					scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
					logger.info("ScrollUsingElement Successfully Done!");
					return;
			}catch(Exception e) {
				System.out.println(inputParam);
			}
			try {
				WebElement waittill = driver.findElement(By.xpath("(//table[@summary='"+ inputParam +"']//td//a)[1]"));
					scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
					logger.info("ScrollUsingElement Successfully Done!");
					return;
			}catch(Exception e) {
				System.out.println(inputParam);
			}
			try {
				WebElement waittill = driver.findElement(By.xpath("(//label[text()=\""+inputParam+"\"]/following::input)[1]"));
					scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
					logger.info("ScrollUsingElement Successfully Done!");
					return;
			}catch(Exception e) {
				System.out.println(inputParam);
			}
			try {
				WebElement waittill = driver.findElement(By.xpath("//a[contains(@id,'"+inputParam+"')]"));
					scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
					logger.info("ScrollUsingElement Successfully Done!");
					return;
			}catch(Exception e) {
				System.out.println(inputParam);
			}
			try {
				WebElement waittill = driver.findElement(By.xpath("//li[text()='"+inputParam+"']"));
					scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
					logger.info("ScrollUsingElement Successfully Done!");
					return;
			}catch(Exception e) {
				System.out.println(inputParam);
			}
			try {
				WebElement waittill = driver.findElement(By.xpath("//label[text()=\""+inputParam+"\"]"));
					scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
					logger.info("ScrollUsingElement Successfully Done!");
					return;
			}catch(Exception e) {
				System.out.println(inputParam);
			}
			try {
				WebElement waittill = driver.findElement(By.xpath("//button[text()='" + inputParam + "']"));
					scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
					logger.info("ScrollUsingElement Successfully Done!");
					return;
			}catch(Exception e) {
				System.out.println(inputParam);
			}
			try {
				WebElement waittill = driver.findElement(By.xpath("//img[@title='"+inputParam+"']"));
					scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
					logger.info("ScrollUsingElement Successfully Done!");
					return;
			}catch(Exception e) {
				System.out.println(inputParam);
			}try {
				WebElement waittill = driver.findElement(By.xpath("(//*[@title='" + inputParam + "'])[1]"));
				scrollMethod(driver, fetchConfigVO, waittill, fetchMetadataVO);
				logger.info("ScrollUsingElement Successfully Done!");
				return;
		}
			catch (Exception e) {
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
			return;
		}catch (Exception e) {
			System.out.println(e);
		} try {
				Actions actions = new Actions(driver); 
				WebElement waittill = driver.findElement(By.xpath("(//table[@role='presentation']/following::a[text()='"+param1+"'])[1]"));
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
			Actions actionObject = new Actions(driver);
			actionObject.sendKeys(Keys.ENTER).build().perform();
			Thread.sleep(3000);
		} catch (Exception e) {
			System.out.println(e);
			screenshotFail(driver, "Failed during Enter Method", fetchMetadataVO, fetchConfigVO);
			throw e;
		}
	}
	public  String screenshot(WebDriver driver, String screenshotName, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) {
		String image_dest = null;
		try {
			TakesScreenshot ts = (TakesScreenshot) driver;
			File source = ts.getScreenshotAs(OutputType.FILE);
			image_dest = (fetchConfigVO.getScreenshot_path()+"\\" + fetchMetadataVO.getCustomer_name()
				+ "\\" + fetchMetadataVO.getTest_run_name()+ "\\" + fetchMetadataVO.getLine_number()+ "_" + fetchMetadataVO.getScenario_name() 
				+ "_" + fetchMetadataVO.getScript_number() + "_" + fetchMetadataVO.getTest_run_name()
				+ "_" + fetchMetadataVO.getScript_id() + "_" + fetchMetadataVO.getLine_number() + "_Passed").concat(".png");
			System.out.println(image_dest);
			File destination = new File(image_dest);
			FileUtils.copyFile(source, destination);
			logger.info("Successfully Screenshot is taken");
			return image_dest;
		} catch (Exception e) {
			logger.error("Failed During Taking screenshot");
			System.out.println("Exception while taking Screenshot" + e.getMessage());
			return e.getMessage();
		}
	}
	public  String screenshotFail(WebDriver driver, String screenshotName, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) {
		String image_dest = null;
		try {
			TakesScreenshot ts = (TakesScreenshot) driver;
			File source = ts.getScreenshotAs(OutputType.FILE);
			String currenttime = new SimpleDateFormat("MM-dd-yyyy HH-mm-ss").format(Calendar.getInstance().getTime());
			image_dest =(fetchConfigVO.getScreenshot_path()+"\\" + fetchMetadataVO.getCustomer_name()
			+ "\\" + fetchMetadataVO.getTest_run_name()+ "\\" + fetchMetadataVO.getLine_number()+ "_" + fetchMetadataVO.getScenario_name() + "_" + fetchMetadataVO.getScript_number() + "_" + fetchMetadataVO.getTest_run_name()
			+ "_" + fetchMetadataVO.getScript_id() + "_" + fetchMetadataVO.getInput_parameter() + "_Failed").concat(".png");
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
	public  String screenshotException(WebDriver driver, String screenshotName, List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO) {
		String image_dest = null;
		try {
			TakesScreenshot ts = (TakesScreenshot) driver;
			File source = ts.getScreenshotAs(OutputType.FILE);
			image_dest =(fetchConfigVO.getScreenshot_path()+"\\" + fetchMetadataListVO.get(0).getCustomer_name()
			+ "\\" + fetchMetadataListVO.get(0).getTest_run_name()+ "\\" + fetchMetadataListVO.get(0).getLine_number()+ "_" + fetchMetadataListVO.get(0).getScenario_name() + "_" + fetchMetadataListVO.get(0).getScript_number() + "_" + fetchMetadataListVO.get(0).getTest_run_name()
			+ "_" + fetchMetadataListVO.get(0).getScript_id() + "_" + "NoSuchElementException" + "_Failed").concat(".png");
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
	public  void selectByText(WebDriver driver, String inputParam, String inputData, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) {
		try {
			WebElement waittext = driver.findElement(By.xpath(("//*[contains(text(),'"+inputParam+"')]/following::select[1]")));
			selectMethod(driver, inputData, fetchMetadataVO, waittext, fetchConfigVO);
			return;
		} catch (Exception e) {
			System.out.println(inputParam);
		}try {
			WebElement waittext = driver.findElement(By.xpath(("//*[contains(text(),'"+inputParam+"')]/preceding-sibling::select[1]")));
			selectMethod(driver, inputData, fetchMetadataVO, waittext, fetchConfigVO);
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
	public  String copynumber(WebDriver driver, String inputParam, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) {
		try {
			WebElement webElement = driver.findElement(By.xpath("(//div[contains(text(),'"+inputParam+"')])[1]"));
			if(webElement.isDisplayed()==true) {
			copyMethod(webElement);
			return inputParam;
			}
		} catch (Exception e) {
			System.out.println(inputParam);
		}
		try {
			WebElement webElement = driver.findElement(By.xpath("//img[@title='In Balance ']/following::td[1]"));
			if(webElement.isDisplayed()==true) {
			copyMethod(webElement);
			return inputParam;
			}
		} catch (Exception e) {
			logger.error("Failed During copynumber Action");
			screenshotFail(driver, "Failed during CopyNumber Method", fetchMetadataVO, fetchConfigVO);
			System.out.println(inputParam);
			throw e;
		}
		return inputParam;
	}
	private void copyMethod(WebElement webElement) {
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

	public void switchToFrame(WebDriver driver, String inputParam, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) {
        try {
              WebElement waittext = driver.findElement(By.xpath("//iframe[contains(@id,'"+inputParam+"')]"));
              driver.switchTo().frame(waittext);
              return;
        } catch (Exception e) {
              System.out.println(e);
              logger.error("Failed During switchToFrame Action");
              screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, fetchConfigVO);
              throw e;
        }
  }
	public  void uploadFileAutoIT(String filelocation, FetchMetadataVO fetchMetadataVO) throws Exception {
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

}
