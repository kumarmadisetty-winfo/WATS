package com.winfo.scripts;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import com.winfo.utils.DateUtils;
import com.winfo.utils.StringUtils;

public class Test {

	public static void main(String[] args) throws IOException, DocumentException, com.itextpdf.text.DocumentException {
		
		createPdf();
	}
	
	public static void createPdf()
			throws IOException, DocumentException, com.itextpdf.text.DocumentException {
		String Date = DateUtils.getSysdate();
		String FILE = ("C:\\Selenium\\RMI_Selenium\\WinfoAutomation\\Report\\Winfo\\Winfo Testing 2"+ "_"+Date+".pdf");
		System.out.println(FILE);
		String fileNameList = "C:\\Selenium\\RMI_Selenium\\WinfoAutomation\\Screenshot\\";
//		String Script_Number = fetchMetadataListVO.get(0).getScript_number();
		Document document = new Document();
		PdfWriter.getInstance(document,
				new FileOutputStream(FILE));
		document.open();
//		for (String image : fileNameList) {
			Image img = Image
					.getInstance("C:\\Selenium\\RMI_Selenium\\WinfoAutomation\\Screenshot\\Winfo\\" + fileNameList);
			String ScriptNumber = fileNameList.split("_")[1];
			String TestRun = fileNameList.split("_")[2];
			String Status = fileNameList.split("_")[5];
			String status = Status.split("\\.")[0];
			document.setPageSize(img);
			document.newPage();
			Font fnt = FontFactory.getFont("Arial", 18);
			String TR = "Test Run Name:" + " " + TestRun;
			String SN = "Script Number:" + " " + ScriptNumber;
			String S  = "Status:" + " " + status;
			document.add(new Paragraph(TR, fnt));
			document.add(new Paragraph(SN, fnt));
			document.add(new Paragraph(S, fnt));
			document.add(Chunk.NEWLINE);
			img.setAlignment(Image.ALIGN_CENTER);
			img.isScaleToFitHeight();
			img.scalePercent(65,65);
			document.add(img);
//		}
		document.close();
	//	uploadPDF(fetchMetadataListVO, fetchConfigVO);
	}
	public static void uploadPDF(){
		try {
			  String accessToken = getAccessTokenPdf();
			  List imageUrlList = new ArrayList();
			  File imageDir = new File("C:\\Selenium\\RMI_Selenium\\WinfoAutomation\\Report\\Winfo\\");
			  System.out.println(imageDir);
			  for(File imageFile : imageDir.listFiles()){
				  String imageFileName = imageFile.getName();
				  System.out.println(imageFileName);
				  imageUrlList.add(imageFileName);
				  File pdfFile = new File(imageDir+"\\"+imageFileName);
				  FileInputStream input = new FileInputStream(pdfFile);
				  ByteArrayOutputStream bos = new ByteArrayOutputStream();
				  byte[] buffer = new byte[65536];
				  int l;
				  while ((l = input.read(buffer)) > 0) {
					  bos.write(buffer, 0, l);
				  }
				  input.close();
				  byte[] data = bos.toByteArray();
			   
				  MultiValueMap<String, byte[]> bodyMap = new LinkedMultiValueMap<>();
			      bodyMap.add("user-file", data);
			      //Outer header 
			      HttpHeaders headers = new HttpHeaders();
			      headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			      headers.add("maxRequestLength", "1048576");
			      headers.add("userName","kaushik.sekaran@winfosolutions.com");
			      headers.add("password","WatsSelenium@1");
			      headers.set("Authorization", "Bearer " +accessToken);
			      headers.add("scope","https://graph.microsoft.com/Sites.ReadWrite.All https://graph.microsoft.com/Files.ReadWrite");
			      HttpEntity< byte[]> requestEntity = new HttpEntity<>(data, headers);
			      
			      RestTemplate restTemplate = new RestTemplate();
			      System.out.println("https://graph.microsoft.com/v1.0/me/drive/root:/Screenshot/Winfo_Report/"+imageFileName+":/content");
			      ResponseEntity<byte[]> response = restTemplate.exchange("https://graph.microsoft.com/v1.0/me/drive/root:/Screenshot/Winfo_Report/"+imageFileName+":/content",
			    		  HttpMethod.PUT, requestEntity, byte[].class);
	
			      System.out.println("response status: " + response.getStatusCode());
			      System.out.println("response body: " + response.getBody());
			      System.out.println("response : " + response);
			  }
		  } catch (Exception e) {
			  System.out.println(e);
		  }
	}
	
	public static String getAccessTokenPdf(){
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
}
