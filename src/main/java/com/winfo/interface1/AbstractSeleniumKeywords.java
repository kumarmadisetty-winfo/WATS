package com.winfo.interface1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.assertthat.selenium_shutterbug.core.Capture;
import com.assertthat.selenium_shutterbug.core.Shutterbug;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.itextpdf.awt.PdfGraphics2D;
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
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.lowagie.text.DocumentException;
import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.DeleteObjectRequest;
import com.oracle.bmc.objectstorage.requests.GetObjectRequest;
import com.oracle.bmc.objectstorage.requests.ListObjectsRequest;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.objectstorage.responses.DeleteObjectResponse;
import com.oracle.bmc.objectstorage.responses.GetObjectResponse;
import com.oracle.bmc.objectstorage.responses.ListObjectsResponse;
import com.oracle.bmc.objectstorage.responses.PutObjectResponse;
import com.winfo.exception.WatsEBSCustomException;
import com.winfo.model.TestSetAttribute;
import com.winfo.services.DataBaseEntry;
import com.winfo.services.DynamicRequisitionNumber;
import com.winfo.services.FetchConfigVO;
import com.winfo.utils.Constants.TEST_SET_LINE_ID_STATUS;
import com.winfo.utils.DateUtils;
import com.winfo.utils.StringUtils;
import com.winfo.vo.ApiValidationVO;
import com.winfo.vo.CustomerProjectDto;
import com.winfo.vo.ScriptDetailsDto;

import reactor.core.publisher.Mono;

@Service
public abstract class AbstractSeleniumKeywords {

	public static final Logger logger = Logger.getLogger(AbstractSeleniumKeywords.class);
	
	@Value("${oci.config.path}")
	private String ociConfigPath;
	@Value("${oci.config.name}")
	private String ociConfigName;
	@Value("${oci.bucket.name}")
	private String ociBucketName;
	@Value("${oci.namespace}")
	private String ociNamespace;
	@Value("${configvO.watslogo}")
	private String watslogo;
	@Value("${configvO.whiteimage}")
	private String whiteimage;
	@Autowired
	private DataBaseEntry databaseentry;

	private static final String PASSED_PDF = "Passed_Report.pdf";
	private static final String FAILED_PDF = "Failed_Report.pdf";
	private static final String DETAILED_PDF = "Detailed_Report.pdf";
	private static final String API_TESTING = "API_TESTING";
	private static final String PNG_EXTENSION = ".png";
	private static final String JPG_EXTENSION = ".jpg";
	public static final String FORWARD_SLASH = "/";
	private static final String STATUS = "Status";
	private static final String TOTAL = "Total";
	private static final String PERCENTAGE = "Percentage";
	private static final String PASSED = "Passed";
	private static final String FAILED = "Failed";
	private static final String ARIAL = "Arial";
	private static final String PASS = "Pass";
	private static final String FAIL = "Fail";
	private static final String IN_COMPLETE = "In Complete";
	private static final String EXECUTION_REPORT = "Execution Report";
	private static final String TEST_RUN_NAME = "Test Run Name";
	private static final String EXECUTED_BY = "Executed By";
	private static final String START_TIME = "Start Time";
	private static final String END_TIME = "End Time";
	private static final String EXECUTION_TIME = "Execution Time";
	private static final String EXECUTION_SUMMARY = "Execution Summary";
	private static final String SCRIPT_NUMBER = "Script Number";
	private static final String STEP_DESC = "Step Description : ";
	private static final String TEST_PARAM = "Test Parameter : ";
	private static final String TEST_VALUE = "Test Value : ";
	private static final String SCENARIO_NAME = "Test Case Name";
	private static final String LINE_NUMBER = "Line Number : ";
	private static final String SCREENSHOT = "Screenshot";
	private static final String ELAPSED_TIME = "Elapsed Time";

	@Autowired
	DataBaseEntry dataBaseEntry;

	@Autowired
	DynamicRequisitionNumber dynamicnumber;
	
	public String takeScreenshot(WebDriver driver, ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails) {
		String imageName = null;
		String folderName = null;
		try {
			TakesScreenshot ts = (TakesScreenshot) driver;
			File source = ts.getScreenshotAs(OutputType.FILE);
			String fileExtension = source.getName();
			fileExtension = fileExtension.substring(fileExtension.indexOf("."));
			folderName = SCREENSHOT + FORWARD_SLASH + customerDetails.getCustomerName() + FORWARD_SLASH
					+ customerDetails.getTestSetName();
			imageName = (fetchMetadataVO.getSeqNum() + "_" + fetchMetadataVO.getLineNumber() + "_"
					+ fetchMetadataVO.getScenarioName() + "_" + fetchMetadataVO.getScriptNumber() + "_"
					+ customerDetails.getTestSetName() + "_" + fetchMetadataVO.getLineNumber() + "_Passed")
							.concat(fileExtension);
			uploadObjectToObjectStore(source.getCanonicalPath(), folderName, imageName);
			logger.info("Successfully Screenshot is taken " + imageName);
			return folderName + FORWARD_SLASH + imageName;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception while taking Screenshot" + e.getMessage());
			return "Failed during taking Screenshot";
		}
	}
	public String screenshot(WebDriver driver, ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails) {
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
			e.printStackTrace();
			logger.error("Failed During Taking screenshot");
			logger.error("Exception while taking Screenshot" + e.getMessage());
			return e.getMessage();
		}
	}
	

	public String screenshotFail(WebDriver driver, ScriptDetailsDto fetchMetadataVO,
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
			e.printStackTrace();
			logger.error("Failed during screenshotFail Action. " + scripNumber);
			logger.error("Exception while taking Screenshot" + e.getMessage());
			return e.getMessage();
//			throw e;
		}
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
			e.printStackTrace();
			logger.error("Failed During Taking screenshot");
			logger.error("Exception while taking Screenshot" + e.getMessage());
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
			e.printStackTrace();
			logger.error("Failed during screenshotFail Action. " + scripNumber);
			logger.error("Exception while taking Screenshot" + e.getMessage());
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

	public void downloadScreenshotsFromObjectStore(String screenshotPath, String customerName, String testSetName,
			String seqNum) {
		ConfigFileReader.ConfigFile configFile = null;
		List<String> objNames = null;
		try {
			configFile = ConfigFileReader.parse(new FileInputStream(new File(ociConfigPath)), ociConfigName);
		} catch (IOException e) {
			throw new WatsEBSCustomException(500, "Exception occured while connecting to oci/config path", e);
		}
		try {
			final AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);

			try (ObjectStorage client = new ObjectStorageClient(provider);) {

				String seqnum = (seqNum == null) ? "" : seqNum;

				String objectStoreScreenShotPath = SCREENSHOT + FORWARD_SLASH + customerName + FORWARD_SLASH
						+ testSetName + FORWARD_SLASH + seqnum;

				ListObjectsRequest listObjectsRequest = ListObjectsRequest.builder().namespaceName(ociNamespace)
						.bucketName(ociBucketName).prefix(objectStoreScreenShotPath).delimiter(FORWARD_SLASH).build();

				/* Send request to the Client */
				ListObjectsResponse response = client.listObjects(listObjectsRequest);

				objNames = response.getListObjects().getObjects().stream().map(objSummary -> objSummary.getName())
						.collect(Collectors.toList());
				logger.info(objNames.size());
				ListIterator<String> listIt = objNames.listIterator();
				createDir(screenshotPath);
				while (listIt.hasNext()) {
					String objectName = listIt.next();
					GetObjectResponse getResponse = client.getObject(GetObjectRequest.builder()
							.namespaceName(ociNamespace).bucketName(ociBucketName).objectName(objectName).build());

					String imageName = objectName.substring(objectName.lastIndexOf(FORWARD_SLASH) + 1,
							objectName.length());
					File file = new File(screenshotPath + File.separator + imageName);
					logger.info("Image Name ****** " + imageName);
					logger.info(file.exists() + "FileExist or not ******" + file.getPath());
					try (final InputStream stream = getResponse.getInputStream();
							final OutputStream outputStream = new FileOutputStream(file.getPath())) {

						// final OutputStream outputStream = Files.newOutputStream(file.toPath(),
						// CREATE_NEW)) {
						// use fileStream
						byte[] buf = new byte[8192];
						int bytesRead;
						while ((bytesRead = stream.read(buf)) > 0) {
							outputStream.write(buf, 0, bytesRead);
						}
					} catch (IOException e1) {
						e1.printStackTrace();
						throw new WatsEBSCustomException(500,
								"Exception occured while read or write screenshot from Object Storage", e1);
					}
				}
			}
		} catch (WatsEBSCustomException e) {
			throw e;
		} catch (Exception e) {
			throw new WatsEBSCustomException(500,
					"Exception occured while downloading screenshots from object path location.", e);
		}

	}

	public void createDir(String path) {
		File folder1 = new File(path);
		if (!folder1.exists()) {
			logger.info("creating directory: " + folder1.getName());
			try {
				folder1.mkdirs();
			} catch (SecurityException se) {
				se.printStackTrace();
			}
		} else {
			logger.info("Folder exist");
		}
	}

	public void findPassAndFailCount(FetchConfigVO fetchConfigVO, String testSetId) {

		List<String> testLineStatusList = dataBaseEntry.getStatusByTestSetId(testSetId);
		fetchConfigVO.setSeqNumAndStatus(dataBaseEntry.getStatusAndSeqNum(testSetId));
		int passCount = 0;
		int failCount = 0;
		int other = 0;
		for (String testLinesStatus : testLineStatusList) {
			if (testLinesStatus.equalsIgnoreCase(TEST_SET_LINE_ID_STATUS.PASS.getLabel())) {
				passCount++;
			} else if (testLinesStatus.equalsIgnoreCase(TEST_SET_LINE_ID_STATUS.FAIL.getLabel())) {
				failCount++;
			} else {
				other++;
			}
		}
		fetchConfigVO.setPasscount(passCount);
		fetchConfigVO.setFailcount(failCount);
		fetchConfigVO.setOtherCount(other);
	}

	public List<String> getPassedPdfNew(List<ScriptDetailsDto> fetchMetadataListVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) {

		String folder = fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + customerDetails.getCustomerName()
				+ File.separator + customerDetails.getTestSetName() + File.separator;

		Map<Integer, List<File>> filesMap = new TreeMap<>();
		List<String> targetPassedPdf = new ArrayList<>();
		Map<String, String> seqNumMap = new HashMap<>();
		for (Object[] obj : fetchConfigVO.getSeqNumAndStatus()) {
			seqNumMap.put(obj[0].toString(), obj[1].toString());
		}
		List<String> fileSeqList = fileSeqContainer(fetchMetadataListVO, customerDetails.getTestSetName());
		for (String fileNames : fileSeqList) {
			if (fileNames.endsWith(PASSED)) {
				fileNames = new File(folder + fileNames + PNG_EXTENSION).exists() ? fileNames + PNG_EXTENSION
						: fileNames;
				fileNames = (!(fileNames.endsWith(PNG_EXTENSION))
						&& (new File(folder + fileNames + JPG_EXTENSION).exists())) ? fileNames + JPG_EXTENSION
								: fileNames;
				File newFile = new File(folder + fileNames);
				if (newFile.exists()) {
					Integer seqNum = Integer.valueOf(newFile.getName().substring(0, newFile.getName().indexOf('_')));
					if (seqNumMap.get(seqNum.toString()).equals(PASS)) {
						filesMap.putIfAbsent(seqNum, new ArrayList<>());
						filesMap.get(seqNum).add(newFile);
						targetPassedPdf.add(newFile.getName());
					}
				}
			}
		}

		return targetPassedPdf;
	}

	public List<String> getFailedPdfNew(List<ScriptDetailsDto> fetchMetadataListVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) {

		String folder = fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + customerDetails.getCustomerName()
				+ File.separator + customerDetails.getTestSetName() + File.separator;
		Map<String, String> seqNumMap = new HashMap<>();
		for (Object[] obj : fetchConfigVO.getSeqNumAndStatus()) {
			seqNumMap.put(obj[0].toString(), obj[1].toString());
		}
		List<String> targetFailedPdf = new ArrayList<>();
		List<String> fileSeqList = fileSeqContainer(fetchMetadataListVO, customerDetails.getTestSetName());
		Map<Integer, List<File>> filesMap = new TreeMap<>();
		for (String fileNames : fileSeqList) {
			fileNames = new File(folder + fileNames + PNG_EXTENSION).exists() ? fileNames + PNG_EXTENSION : fileNames;
			fileNames = (!(fileNames.endsWith(PNG_EXTENSION))
					&& (new File(folder + fileNames + JPG_EXTENSION).exists())) ? fileNames + JPG_EXTENSION : fileNames;
			File newFile = new File(folder + fileNames);
			if (newFile.exists()) {
				Integer seqNum = Integer.valueOf(newFile.getName().substring(0, newFile.getName().indexOf('_')));
				if (seqNumMap.get(seqNum.toString()).equals(FAIL)) {
					if (!filesMap.containsKey(seqNum)) {
						filesMap.put(seqNum, new ArrayList<File>());
					}
					filesMap.get(seqNum).add(newFile);
					targetFailedPdf.add(newFile.getName());
				}
			}
		}

		return targetFailedPdf;
	}

	public List<String> getDetailPdfNew(List<ScriptDetailsDto> fetchMetadataListVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) {

		String folder = fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + customerDetails.getCustomerName()
				+ File.separator + customerDetails.getTestSetName() + File.separator;
		Map<Integer, List<File>> filesMap = new TreeMap<>();
		List<String> fileSeqList = fileSeqContainer(fetchMetadataListVO, customerDetails.getTestSetName());
		List<String> detailsFileName = new ArrayList<>();
		for (String fileNames : fileSeqList) {
			fileNames = new File(folder + fileNames + PNG_EXTENSION).exists() ? fileNames + PNG_EXTENSION : fileNames;
			fileNames = (!(fileNames.endsWith(PNG_EXTENSION))
					&& (new File(folder + fileNames + JPG_EXTENSION).exists())) ? fileNames + JPG_EXTENSION : fileNames;
			File file = new File(folder + fileNames);
			if (file.exists()) {
				Integer seqNum = Integer.valueOf(file.getName().substring(0, file.getName().indexOf('_')));
				if (!filesMap.containsKey(seqNum)) {
					filesMap.put(seqNum, new ArrayList<>());
				}
				filesMap.get(seqNum).add(file);
				detailsFileName.add(fileNames);
			}
		}
		return detailsFileName;
	}

	public List<String> fileSeqContainer(List<ScriptDetailsDto> fetchMetadataListVO, String testRunName) {
		List<String> fetchConfigVODtl = new ArrayList<>();
		for (ScriptDetailsDto fetchMetaData : fetchMetadataListVO) {
			if (fetchMetaData.getStatus().equals(PASS)) {
				fetchConfigVODtl.add(fetchMetaData.getSeqNum() + "_"

						+ fetchMetaData.getLineNumber() + "_" + fetchMetaData.getScenarioName() + "_"

						+ fetchMetaData.getScriptNumber() + "_" + testRunName + "_"

						+ fetchMetaData.getLineNumber() + "_Passed");
			} else if (fetchMetaData.getStatus().equals(FAIL)) {
				fetchConfigVODtl.add(fetchMetaData.getSeqNum() + "_"

						+ fetchMetaData.getLineNumber() + "_" + fetchMetaData.getScenarioName() + "_"

						+ fetchMetaData.getScriptNumber() + "_" + testRunName + "_"

						+ fetchMetaData.getLineNumber() + "_Failed");
			}
		}
		return fetchConfigVODtl;
	}

	public List<String> getFileNameListNew(List<ScriptDetailsDto> fetchMetadataListVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) {

		String folder = fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + customerDetails.getCustomerName()
				+ File.separator + customerDetails.getTestSetName() + File.separator;
		List<File> fileList = new ArrayList<>();
		List<String> fileSeqList = fileSeqContainer(fetchMetadataListVO, customerDetails.getTestSetName());
		for (String newFile : fileSeqList) {
			newFile = new File(folder + newFile + PNG_EXTENSION).exists() ? newFile + PNG_EXTENSION : newFile;
			newFile = (!(newFile.endsWith(PNG_EXTENSION)) && (new File(folder + newFile + JPG_EXTENSION).exists()))
					? newFile + JPG_EXTENSION
					: newFile;
			File file = new File(folder + newFile);
			if (file.exists()) {
				fileList.add(file);
			}
		}
		List<String> fileNameList = new ArrayList<>();
		for (int i = 0; i < fileList.size(); i++) {
			fileNameList.add(fileList.get(i).getName());
		}
		return fileNameList;
	}

	public Map<String, String> findExecutionTimeForTestRun(String testSetId, String pdffileName, FetchConfigVO fetchConfigVO) {

		String scriptStatus = null;
		Map<String, String> totalExecutedTime = new HashMap<>();
		if (pdffileName.equalsIgnoreCase(PASSED_PDF)) {
			scriptStatus = PASS;
		} else if (pdffileName.equalsIgnoreCase(FAILED_PDF)) {
			scriptStatus = FAIL;
		} else {
			scriptStatus = null;
		}

		List<Object[]> startAndEndDates = dataBaseEntry.findStartAndEndTimeForTestRun(testSetId, scriptStatus);
		List<Date> listOfStartTime = new ArrayList<>();
		List<Date> listOfEndTime = new ArrayList<>();
		long totalDiff = 0;
		Date startDate = null;
		Date finishDate = null;
		for (Object[] date : startAndEndDates) {
			if (date[0] != null && date[1] != null) {
				if (startDate == null || startDate.after((Date) date[0])) {
					startDate = (Date) date[0];
				}
				if (finishDate == null || finishDate.before((Date) date[1])) {
					finishDate = (Date) date[1];
				}
				totalDiff += DateUtils.findTimeDifference(date[0].toString(), date[1].toString());

				listOfStartTime.add((Date) date[0]);
				listOfEndTime.add((Date) date[1]);
			}
		}
		if(!listOfStartTime.isEmpty() && !listOfEndTime.isEmpty()) {
			fetchConfigVO.setStarttime(DateUtils.findMinStartTimeAndMaxEndTime(listOfStartTime, "MIN"));
			fetchConfigVO.setEndtime(DateUtils.findMinStartTimeAndMaxEndTime(listOfEndTime, "MAX"));
		}
		
		if (startDate != null && finishDate != null) {
			totalExecutedTime.put("totalElapsedTime", DateUtils.convertMiliSecToDayFormat(
					DateUtils.findTimeDifference(startDate.toString(), finishDate.toString())));
		} else {
			totalExecutedTime.put("totalElapsedTime", "Not Available");
		}
		totalExecutedTime.put("totalExecutedTime", DateUtils.convertMiliSecToDayFormat(totalDiff));

		return totalExecutedTime;
	}

	public void createPdf(List<ScriptDetailsDto> fetchMetadataListVO, FetchConfigVO fetchConfigVO, String pdffileName,
			CustomerProjectDto customerDetails) {
		try {
			String folder = (fetchConfigVO.getWINDOWS_PDF_LOCATION() + customerDetails.getCustomerName()
					+ File.separator + customerDetails.getTestSetName() + File.separator);
			String file = (folder + pdffileName);
			findPassAndFailCount(fetchConfigVO, customerDetails.getTestSetId());

			List<String> fileNameList = null;
			if (PASSED_PDF.equalsIgnoreCase(pdffileName)) {
				fileNameList = getPassedPdfNew(fetchMetadataListVO, fetchConfigVO, customerDetails);
			} else if (FAILED_PDF.equalsIgnoreCase(pdffileName)) {
				fileNameList = getFailedPdfNew(fetchMetadataListVO, fetchConfigVO, customerDetails);
			} else if (DETAILED_PDF.equalsIgnoreCase(pdffileName)) {
				fileNameList = getDetailPdfNew(fetchMetadataListVO, fetchConfigVO, customerDetails);
			} else {
				fileNameList = getFileNameListNew(fetchMetadataListVO, fetchConfigVO, customerDetails);
			}
			String executedBy = fetchMetadataListVO.get(0).getExecutedBy();
			createDir(folder);
			Document document = new Document();
			String report = EXECUTION_REPORT;
			Font font23 = FontFactory.getFont(ARIAL, 23);
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
			Rectangle pageSize = new Rectangle(1360, 800);
			document.setPageSize(pageSize);
			document.open();
			logger.info("before enter Images/wats_icon.png1");
			Image watsLogo = Image.getInstance(watslogo);
			logger.info("after enter Images/wats_icon.png1");
			watsLogo.scalePercent(13, 13);
			watsLogo.setAlignment(Image.ALIGN_RIGHT);
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss aa");
			String testRunName1 = customerDetails.getTestSetName();

			if ((!fileNameList.isEmpty()) && (PASSED_PDF.equalsIgnoreCase(pdffileName)
					|| FAILED_PDF.equalsIgnoreCase(pdffileName) || DETAILED_PDF.equalsIgnoreCase(pdffileName))) {
				int passcount = fetchConfigVO.getPasscount();
				int failcount = fetchConfigVO.getFailcount();
				int others = fetchConfigVO.getOtherCount();
				
				Map<String,String> totalTimeTaken = findExecutionTimeForTestRun(customerDetails.getTestSetId(), pdffileName, fetchConfigVO);
				String totalExecutedTime = totalTimeTaken.get("totalExecutedTime");
				String totalElapsedTime = totalTimeTaken.get("totalElapsedTime");
				Date tendTime = fetchConfigVO.getEndtime();
				Date tStarttime = fetchConfigVO.getStarttime();
				String startTime = dateFormat.format(tStarttime);
				String endTime = dateFormat.format(tendTime);
				String[] testArr = { TEST_RUN_NAME, testRunName1, EXECUTED_BY, executedBy, START_TIME, startTime, END_TIME, endTime, EXECUTION_TIME,
						totalExecutedTime, ELAPSED_TIME, totalElapsedTime };
				document.add(watsLogo);
				document.add(new Paragraph(report, font23));
				document.add(Chunk.NEWLINE);
				PdfPTable table1 = new PdfPTable(2);
				table1.setWidths(new int[] { 1, 1 });
				table1.setWidthPercentage(100f);
				for (String text : testArr) {
					insertCell(table1, text, Element.ALIGN_LEFT, 1, font23);
				}
				document.add(table1);

				if (DETAILED_PDF.equalsIgnoreCase(pdffileName)) {
					generateDetailsPDF(document, watsLogo, passcount, failcount, others, writer);
					String checkPackage = dataBaseEntry.getPackage(customerDetails.getTestSetId());
					if (API_TESTING.equalsIgnoreCase(checkPackage)) {
						Map<Integer, Integer> mapOfResponseCodeAndCount = new HashMap<Integer,Integer>();
						ObjectMapper objectMapper = new ObjectMapper();
						objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
						List<String> responseCodeList = dynamicnumber.getResponseCode(customerDetails.getTestSetId());
						for (String responseCode : responseCodeList) {
							String inputValue = responseCode.replaceAll("(\")(?=[\\{])|(?<=[\\}])(\")|(\\\\)(?=[\\\"])",
									"");
							ApiValidationVO apiValidationData = objectMapper.readValue(inputValue,
									ApiValidationVO.class);
							if (apiValidationData.getResponseCode() != null) {
								if (mapOfResponseCodeAndCount.containsKey(apiValidationData.getResponseCode())) {
									mapOfResponseCodeAndCount.put(apiValidationData.getResponseCode(),
											mapOfResponseCodeAndCount.get(apiValidationData.getResponseCode()) + 1);
								} else {
									mapOfResponseCodeAndCount.put(apiValidationData.getResponseCode(), 1);
								}
							}
						}
						inserStatusCodeInCell(document, mapOfResponseCodeAndCount);
					}
				} else if (PASSED_PDF.equalsIgnoreCase(pdffileName)) {
					generatePassPDF(document, passcount, failcount);
				} else {
					generateFailedPDF(document, passcount, failcount);
				}
				addRestOfPagesToPDF(document, fileNameList, watsLogo, fetchConfigVO, fetchMetadataListVO,
						customerDetails,writer);
			} else if (!(PASSED_PDF.equalsIgnoreCase(pdffileName) || FAILED_PDF.equalsIgnoreCase(pdffileName)
					|| DETAILED_PDF.equalsIgnoreCase(pdffileName))) {
				generateScriptLvlPDF(document, fetchConfigVO.getStarttime(), fetchConfigVO.getEndtime(), watsLogo,
						fetchMetadataListVO, fetchConfigVO, fileNameList, customerDetails,writer);
			}
			document.close();

		} catch (Exception e) {
			logger.info("Not able to Create pdf {}", e);
		}
		try {
			String destinationFilePath = (customerDetails.getCustomerName() + FORWARD_SLASH
					+ customerDetails.getTestSetName() + FORWARD_SLASH) + pdffileName;

			String sourceFilePath = (fetchConfigVO.getWINDOWS_PDF_LOCATION() + customerDetails.getCustomerName()
					+ File.separator + customerDetails.getTestSetName() + File.separator) + pdffileName;
			uploadPDF(sourceFilePath, destinationFilePath);
		} catch (Exception e) {
			logger.info(e);
		}
	}

	public void generateScriptLvlPDF(Document document, Date startTime, Date endTime, Image watsLogo,
			List<ScriptDetailsDto> fetchMetadataListVO, FetchConfigVO fetchConfigVO, List<String> fileNameList,
			CustomerProjectDto customerDetails,PdfWriter writer) throws IOException, com.itextpdf.text.DocumentException {

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a");
		Font font23 = FontFactory.getFont(ARIAL, 23);
		Font fnt12 = FontFactory.getFont(ARIAL, 12);
		String report = EXECUTION_REPORT;
		String starttime1 = dateFormat.format(startTime);
		String endtime1 = dateFormat.format(endTime);
		long diff = DateUtils.findTimeDifference(starttime1.toString(), endtime1.toString());
		String scriptNumber2 = fetchMetadataListVO.get(0).getScenarioName();
		String scenario1 = fetchConfigVO.getStatus1();
		String executionTime = DateUtils.convertMiliSecToDayFormat(diff);
		String tr = TEST_RUN_NAME;
		String sn = SCRIPT_NUMBER;
		String sn1 = SCENARIO_NAME;
		String scenarios1 = "Status ";
		String errorMsg = "ErrorMessage";
		String eb = EXECUTED_BY;
		String st = START_TIME;
		String et = END_TIME;
		String ex = EXECUTION_TIME;
		String testRunName1 = customerDetails.getTestSetName();
		String scriptNumber = fetchMetadataListVO.get(0).getScriptNumber();
		String executedBy = fetchMetadataListVO.get(0).getExecutedBy();
		String customerName = customerDetails.getCustomerName();
		String errorMsgs = fetchMetadataListVO.get(0).getLineErrorMsg();
		document.add(watsLogo);

		document.add(new Paragraph(report, font23));
		document.add(Chunk.NEWLINE);
		PdfPTable table1 = new PdfPTable(2);
		table1.setWidths(new int[] { 1, 1 });
		table1.setWidthPercentage(100f);
		String[] strArr1 = { tr, testRunName1, sn, scriptNumber, sn1, scriptNumber2, scenarios1, scenario1 };
		String[] strArr2 = { eb, executedBy, st, starttime1, et, endtime1, ex, executionTime };
		for (String str : strArr1) {
			insertCell(table1, str, Element.ALIGN_LEFT, 1, font23);
		}
		if (errorMsgs != null) {
			insertCell(table1, errorMsg, Element.ALIGN_LEFT, 1, font23);
			insertCell(table1, errorMsgs, Element.ALIGN_LEFT, 1, font23);
		}
		for (String str : strArr2) {
			insertCell(table1, str, Element.ALIGN_LEFT, 1, font23);
		}

		document.add(table1);
		document.newPage();

		int i = 0;
		int increment = 0;
		for (ScriptDetailsDto metaDataVO : fetchMetadataListVO) {
			String checkPackage = dataBaseEntry.getPackage(customerDetails.getTestSetId());
			String fileName = metaDataVO.getSeqNum() + "_" + metaDataVO.getLineNumber() + "_"
					+ metaDataVO.getScenarioName() + "_" + metaDataVO.getScriptNumber() + "_"
					+ customerDetails.getTestSetName() + "_" + metaDataVO.getLineNumber();
			String nextSeqNumber = "0";
			String currentSeqNumber = "0";
			increment++;
			if(increment < fetchMetadataListVO.size()) {
				currentSeqNumber = metaDataVO.getSeqNum();
				nextSeqNumber = fetchMetadataListVO.get(increment).getSeqNum();
			}
			String image = null;
			if (fileNameList.contains(fileName + "_" + PASSED + PNG_EXTENSION)) {
				image = fileName + "_" + PASSED + PNG_EXTENSION;
			} else if (fileNameList.contains(fileName + "_" + PASSED + JPG_EXTENSION)) {
				image = fileName + "_" + PASSED + JPG_EXTENSION;
			} else if (fileNameList.contains(fileName + "_" + FAILED + PNG_EXTENSION)) {
				image = fileName + "_" + FAILED + PNG_EXTENSION;
			} else if (fileNameList.contains(fileName + "_" + FAILED + JPG_EXTENSION)) {
				image = fileName + "_" + FAILED + JPG_EXTENSION;
			}

			if (image != null) {
				i++;
				Image img = Image.getInstance(fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + customerName
						+ FORWARD_SLASH + testRunName1 + FORWARD_SLASH + image);

				Rectangle pageSize = new Rectangle(img.getPlainWidth(), img.getPlainHeight() + 100);
				String status = image.split("_")[6].split("\\.")[0];
				String scenario = image.split("_")[2];
				String steps = image.split("_")[5];

				String stepDescription = metaDataVO.getTestRunParamDesc();
				String inputParam = metaDataVO.getInputParameter();
				String inputValue = metaDataVO.getInputValue();
				if (API_TESTING.equalsIgnoreCase(checkPackage)) {
					inputValue = "";
				}
				document.setPageSize(pageSize);
				document.newPage();
				String s = "Status: " + status;
				String scenarios = SCENARIO_NAME + " :" + scenario;
				watsLogo.scalePercent(13, 13);
				watsLogo.setAlignment(Image.ALIGN_RIGHT);
				document.add(watsLogo);
				document.add(new Paragraph(s, fnt12));
				document.add(new Paragraph(scenarios, fnt12));
				String step = status.equals(FAILED) ? "Failed at Line Number:" + "" + steps : "Line Number :" + "" + steps;
				String failMsg = status.equals(FAILED) ? "Failed Message:" + "" + metaDataVO.getLineErrorMsg() : null;
				document.add(new Paragraph(step, fnt12));
				if (failMsg != null) {
					document.add(new Paragraph(failMsg, fnt12));
				}
				if (stepDescription != null) {
					document.add(new Paragraph(STEP_DESC + stepDescription, fnt12));
				}
				if (inputParam != null && inputValue != null) {
					document.add(new Paragraph(TEST_PARAM + inputParam, fnt12));
					document.add(new Paragraph(TEST_VALUE + inputValue, fnt12));
				}
				document.add(Chunk.NEWLINE);

				Paragraph p = new Paragraph(String.format("page %s of %s", i, fileNameList.size()));
				p.setAlignment(Element.ALIGN_RIGHT);
				img.setAlignment(Image.ALIGN_CENTER);
				img.isScaleToFitHeight();
				img.scalePercent(60, 62);
				document.add(img);
				document.add(p);

				if (API_TESTING.equalsIgnoreCase(checkPackage)) {
					addingResponseIntoReport(fileName, document, customerDetails, fetchConfigVO);
				}
				
				//Adding the downloaded pdf after that particular script
				
				if(!currentSeqNumber.equalsIgnoreCase(nextSeqNumber) || fetchMetadataListVO.size() == increment) {
					String docName = (metaDataVO.getSeqNum() + "_"
		                    + metaDataVO.getScenarioName() + "_"
		                    + metaDataVO.getScriptNumber() + "_" 
		                    + customerDetails.getTestSetName() + "_Passed");
					File file = new File(fetchConfigVO.getDownlod_file_path() + docName + ".pdf");
					if(file.exists()) {
					PdfContentByte contentByte = writer.getDirectContent();
					PdfReader pdfReader = new PdfReader(fetchConfigVO.getDownlod_file_path() + docName + ".pdf");
					for(int page=1; page<=pdfReader.getNumberOfPages(); page++) { 
						PdfImportedPage pages = writer.getImportedPage(pdfReader, page);
						document.newPage();
						contentByte.addTemplate(pages, 1f, 0, 0, 1, 130, 0);
						
					      }
					}
				}
			}
		}

	}

	public void generatePassPDF(Document document, int passCount, int failCount)
			throws DocumentException, com.itextpdf.text.DocumentException {
		Font font23 = FontFactory.getFont(ARIAL, 23);
		String start = EXECUTION_SUMMARY;
		document.add(Chunk.NEWLINE);
		Paragraph executionSummery = new Paragraph(start, font23);
		document.add(executionSummery);
		document.add(Chunk.NEWLINE);
		DecimalFormat df1 = new DecimalFormat("0");
		DecimalFormat df2 = new DecimalFormat("0");
		double pass = Math.round((passCount * 100.0) / (passCount + failCount));
		PdfPTable table = new PdfPTable(3);
		table.setWidths(new int[] { 1, 1, 1 });
		table.setWidthPercentage(100f);
		insertCell(table, STATUS, Element.ALIGN_CENTER, 1, font23);
		insertCell(table, TOTAL, Element.ALIGN_CENTER, 1, font23);
		insertCell(table, PERCENTAGE, Element.ALIGN_CENTER, 1, font23);
		PdfPCell[] cells1 = table.getRow(0).getCells();
		for (int k = 0; k < cells1.length; k++) {
			cells1[k].setBackgroundColor(new BaseColor(161, 190, 212));
		}
		String[] strArr = { PASSED, df1.format(passCount), df2.format(pass) + "%" };
		for (String str : strArr) {
			insertCell(table, str, Element.ALIGN_CENTER, 1, font23);
		}
		document.setMargins(20, 20, 20, 20);
		document.add(table);

	}

	public void generateFailedPDF(Document document, int passcount, int failcount)
			throws DocumentException, com.itextpdf.text.DocumentException {
		Font font23 = FontFactory.getFont(ARIAL, 23);
		String start = EXECUTION_SUMMARY;
		document.add(Chunk.NEWLINE);
		Paragraph executionSummery = new Paragraph(start, font23);
		document.add(executionSummery);
		document.add(Chunk.NEWLINE);
		DecimalFormat df1 = new DecimalFormat("0");
		DecimalFormat df2 = new DecimalFormat("0");
		double fail = Math.round((failcount * 100.0) / (passcount + failcount));
		PdfPTable table = new PdfPTable(3);
		table.setWidths(new int[] { 1, 1, 1 });
		table.setWidthPercentage(100f);
		insertCell(table, STATUS, Element.ALIGN_CENTER, 1, font23);
		insertCell(table, TOTAL, Element.ALIGN_CENTER, 1, font23);
		insertCell(table, PERCENTAGE, Element.ALIGN_CENTER, 1, font23);
		PdfPCell[] cells1 = table.getRow(0).getCells();
		for (int k = 0; k < cells1.length; k++) {
			cells1[k].setBackgroundColor(new BaseColor(161, 190, 212));
		}
		String[] strArr = { FAILED, df1.format(failcount), df2.format(fail) + "%" };
		for (String str : strArr) {
			insertCell(table, str, Element.ALIGN_CENTER, 1, font23);
		}
		document.setMargins(20, 20, 20, 20);
		document.add(table);
	}

	public void generateDetailsPDF(Document document, Image watsLogo, int passCount, int failCount, int others,
			PdfWriter writer) throws DocumentException, com.itextpdf.text.DocumentException {
		String start = EXECUTION_SUMMARY;
		String pichart = "Pie-Chart";
		Font font23 = FontFactory.getFont(ARIAL, 23);
		Font fontWhite23 = FontFactory.getFont(ARIAL, 23, BaseColor.WHITE);
		document.add(Chunk.NEWLINE);
		Paragraph executionSummery = new Paragraph(start, font23);
		document.add(executionSummery);
		document.add(Chunk.NEWLINE);
		DecimalFormat df1 = new DecimalFormat("0");
		DecimalFormat df2 = new DecimalFormat("0");
		double pass = Math.round((passCount * 100.0) / (passCount + failCount + others));
		double fail = Math.round((failCount * 100.0) / (passCount + failCount + others));
		double other = Math.round(others * 100.0) / (passCount + failCount + others);
		DefaultPieDataset dataSet = new DefaultPieDataset();
		if (passCount == 0 && others == 0) {
			dataSet.setValue(FAIL, fail);
		} else if (failCount == 0 && others == 0) {
			dataSet.setValue(PASS, pass);
		} else if (passCount == 0 && failCount == 0) {
			dataSet.setValue(IN_COMPLETE, other);
		} else if (passCount != 0 && others != 0 && failCount == 0) {
			dataSet.setValue(PASS, pass);
			dataSet.setValue(IN_COMPLETE, other);
		} else if (passCount == 0 && others != 0 && failCount != 0) {
			dataSet.setValue(FAIL, fail);
			dataSet.setValue(IN_COMPLETE, other);
		} else if (passCount != 0 && others == 0 && failCount != 0) {
			dataSet.setValue(PASS, pass);
			dataSet.setValue(FAIL, fail);
		} else if (passCount != 0 && others != 0 && failCount != 0) {
			dataSet.setValue(PASS, pass);
			dataSet.setValue(FAIL, fail);
			dataSet.setValue(IN_COMPLETE, other);
		}
		PdfPTable table = new PdfPTable(3);
		table.setWidths(new int[] { 1, 1, 1 });
		table.setWidthPercentage(100f);
		insertCell(table, STATUS, Element.ALIGN_CENTER, 1, font23);
		insertCell(table, TOTAL, Element.ALIGN_CENTER, 1, font23);
		insertCell(table, PERCENTAGE, Element.ALIGN_CENTER, 1, font23);
		PdfPCell[] cells1 = table.getRow(0).getCells();
		for (int k = 0; k < cells1.length; k++) {
			cells1[k].setBackgroundColor(new BaseColor(161, 190, 212));
		}
		String[] strArr = { PASSED, df1.format(passCount), df2.format(pass) + "%", FAILED, df1.format(failCount),
				df2.format(fail) + "%" };

		for (String str : strArr) {
			insertCell(table, str, Element.ALIGN_CENTER, 1, font23);
		}
		document.setMargins(20, 20, 20, 20);
		document.add(table);

		Chunk ch = new Chunk(pichart, fontWhite23);
		ch.setTextRise(-18);
		ch.setBackground(new BaseColor(38, 99, 175), 0f, 10f, 1730f, 15f);
		document.newPage();
		document.add(watsLogo);
		Paragraph p1 = new Paragraph(ch);
		p1.setSpacingBefore(50);
		document.add(p1);
		
		try {
			chartAddition(writer,passCount,failCount,others);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void chartAddition(PdfWriter writer, int passCount, int failCount, int others) throws Exception {
		try {
			PdfContentByte contentByte = writer.getDirectContent();
			JFreeChart chart = ChartFactory.createPieChart(null, createDataset(passCount, failCount, others), true,
					true, false);
			contentByte.saveState();
			contentByte.stroke();
			contentByte.restoreState();
			PiePlot plot = (PiePlot) chart.getPlot();
			plot.setLabelGenerator(
					new StandardPieSectionLabelGenerator("{0} ({2})", new DecimalFormat("0"), new DecimalFormat("0%")));
			plot.setLabelFont(new java.awt.Font("SansSerif", Font.BOLD, 12));
			plot.setLabelBackgroundPaint(Color.WHITE);
			plot.setLabelGap(0.02);
			plot.setShadowPaint(null);
			plot.setBackgroundPaint(Color.WHITE);
			plot.setBaseSectionOutlinePaint(Color.WHITE);
			Color passColor = new Color(50, 205, 50);
			plot.setSectionPaint("Pass", passColor);
			Color failColor = new Color(255, 0, 0);
			plot.setSectionPaint("Fail", failColor);
			plot.setSectionPaint("In Complete", Color.GRAY);
			plot.setOutlinePaint(null);

			LegendTitle legend = chart.getLegend();
			legend.setFrame(BlockBorder.NONE);
			Graphics2D g2 = new PdfGraphics2D(contentByte, 1000, 600);
			Rectangle2D r2D = new Rectangle2D.Double(400, -40, 600, 600);
			chart.draw(g2, r2D);
			chart.setBorderVisible(false);
			g2.dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static PieDataset createDataset(int passCount, int failCount, int others) {
		List<Integer> values = new ArrayList<>();
		values.add(passCount);
		values.add(failCount);
		values.add(others);
		DefaultPieDataset dataset = new DefaultPieDataset();
		dataset.setValue("Pass", values.get(0));
		dataset.setValue("Fail", values.get(1));
		dataset.setValue("In Complete", values.get(2));
		return dataset;
	}

	public void addRestOfPagesToPDF(Document document, List<String> fileNameList, Image watsLogo,
			FetchConfigVO fetchConfigVO, List<ScriptDetailsDto> fetchMetadataListVO, CustomerProjectDto customerDetails,PdfWriter writer)
			throws IOException, com.itextpdf.text.DocumentException {
		int k = 0;
		int l = 0;
		String sno1 = "";
		Map<Integer, Map<String, String>> toc = new TreeMap<>();
		String customerName = customerDetails.getCustomerName();
		String testRunName1 = customerDetails.getTestSetName();
		Font font23 = FontFactory.getFont(ARIAL, 23);
		Font fnt12 = FontFactory.getFont(ARIAL, 12);
		Font bf15 = FontFactory.getFont(ARIAL, 23, Font.UNDERLINE);
		Font bf16 = FontFactory.getFont(ARIAL, 12, Font.UNDERLINE, new BaseColor(66, 245, 236));
		Font bf13 = FontFactory.getFont(ARIAL, 23, Font.UNDERLINE, BaseColor.GREEN);
		Font bf14 = FontFactory.getFont(ARIAL, 23, Font.UNDERLINE, BaseColor.RED);
		Font bfBold = FontFactory.getFont(ARIAL, 23, BaseColor.WHITE);
		Map<String, String> seqNumMap = new HashMap<>();
		for (Object[] obj : fetchConfigVO.getSeqNumAndStatus()) {
			seqNumMap.put(obj[0].toString(), obj[1].toString());
		}
		for (String image : fileNameList) {
			k++;
			String sndo = image.split("_")[0]; // SEQ NUM
			String name = image.split("_")[3];// SCRIPT NUM
			Map<String, String> toc2 = new TreeMap<>();

			/* JUST CHECK THE CODE IS IT WORKJIng OR NOT */
			if (!sndo.equalsIgnoreCase(sno1)) {
				Map<String, String> toc1 = new TreeMap<>();
				for (String image1 : fileNameList) {
					if (image1.startsWith(sndo + "_") && seqNumMap.get(sndo).equals(FAIL)) {
						toc2.put(sndo, FAILED + l);
						l++;
					}
				}
				String str = String.valueOf(toc2.get(sndo));
				toc1.put(sndo + "_" + name, str);
				toc.put(k, toc1);

			}
			if (sndo != null) {
				sno1 = sndo;
			}
		}
		sno1 = "";
		document.newPage();
		document.add(watsLogo);
		Anchor target2 = new Anchor(String.valueOf("Page Numbers"), bfBold);
		target2.setName(String.valueOf("details"));
		Chunk ch1 = new Chunk("Script Numbers", bfBold);
		ch1.setBackground(new BaseColor(38, 99, 175), 0f, 10f, 1730f, 15f);
		Paragraph p2 = new Paragraph();
		p2.add(ch1);
		p2.add(new Chunk(new VerticalPositionMark()));
		p2.add(target2);
		document.add(p2);
		document.add(Chunk.NEWLINE);

		Chunk dottedLine = new Chunk(new DottedLineSeparator());
		for (Entry<Integer, Map<String, String>> entry : toc.entrySet()) {
			Map<String, String> str1 = entry.getValue();
			for (Entry<String, String> entry1 : str1.entrySet()) {
				Anchor click = new Anchor(String.valueOf(entry.getKey()), bf15);
				click.setReference("#" + entry1.getKey());
				Paragraph pr = new Paragraph();
				Anchor ca1 = new Anchor(entry1.getKey(), bf15);
				ca1.setReference("#" + entry1.getKey());
				String compare = entry1.getValue();
				if (!compare.equals("null")) {
					Anchor click1 = new Anchor(String.valueOf("(Failed)"), bf14);
					click1.setReference("#" + entry1.getKey());
					pr.add(ca1);
					pr.add(click1);
					pr.add(dottedLine);
					pr.add(click);
					document.add(Chunk.NEWLINE);
					document.add(pr);
				} else {
					Anchor click2 = new Anchor(String.valueOf("(Passed)"), bf13);
					click2.setReference("#" + entry1.getKey());
					pr.add(ca1);
					pr.add(click2);
					pr.add(dottedLine);
					pr.add(click);
					document.add(Chunk.NEWLINE);
					document.add(pr);
				}
			}
		}

		int i = 0;
		int j = 0;
		int increment = 0;
		for (ScriptDetailsDto metaDataVO : fetchMetadataListVO) {
			String checkPackage = dataBaseEntry.getPackage(customerDetails.getTestSetId());
			String fileName = metaDataVO.getSeqNum() + "_" + metaDataVO.getLineNumber() + "_"
					+ metaDataVO.getScenarioName() + "_" + metaDataVO.getScriptNumber() + "_"
					+ customerDetails.getTestSetName() + "_" + metaDataVO.getLineNumber();
			String nextSeqNumber = "0";
			String currentSeqNumber = "0";
			increment++;
			if(increment < fetchMetadataListVO.size()) {
				currentSeqNumber = metaDataVO.getSeqNum();
				nextSeqNumber = fetchMetadataListVO.get(increment).getSeqNum();
			}
			String image = null;
			if (fileNameList.contains(fileName + "_Passed.png")) {
				image = fileName + "_Passed.png";
			} else if (fileNameList.contains(fileName + "_Passed.jpg")) {
				image = fileName + "_Passed.jpg";
			} else if (fileNameList.contains(fileName + "_Failed.png")) {
				image = fileName + "_Failed.png";
			} else if (fileNameList.contains(fileName + "_Failed.jpg")) {
				image = fileName + "_Failed.jpg";
			}

			if (image != null) {
				i++;
				Image img = Image.getInstance(fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + customerName
						+ FORWARD_SLASH + testRunName1 + FORWARD_SLASH + image);
				Rectangle pageSize = new Rectangle(img.getPlainWidth(), img.getPlainHeight() + 100);
				String sno = image.split("_")[0];
				String sNo = SCRIPT_NUMBER;
				String scriptNumber1 = image.split("_")[3];
				String snm = SCENARIO_NAME;
				String scriptName = image.split("_")[2];

				if (!sno.equalsIgnoreCase(sno1)) {
					document.setPageSize(pageSize);
					document.newPage();
					document.add(watsLogo);
					Anchor target3 = new Anchor("Script Details", font23);
					target3.setName(sno + "_" + scriptNumber1);
					Paragraph pa = new Paragraph();
					pa.add(target3);
					document.add(pa);
					document.add(Chunk.NEWLINE);
					PdfPTable table2 = new PdfPTable(2);
					table2.setWidths(new int[] { 1, 1 });
					table2.setWidthPercentage(100f);
					String[] strArr = { sNo, scriptNumber1, snm, scriptName };
					for (String str : strArr) {
						insertCell(table2, str, Element.ALIGN_LEFT, 1, font23);
					}

					for (Entry<String, String> entry1 : toc.get(i).entrySet()) {
						String str = entry1.getValue();
						if (!str.equals("null")) {
							insertCell(table2, STATUS, Element.ALIGN_LEFT, 1, font23);
							insertCell(table2, FAILED, Element.ALIGN_LEFT, 1, font23);
						} else {
							insertCell(table2, STATUS, Element.ALIGN_LEFT, 1, font23);
							insertCell(table2, PASSED, Element.ALIGN_LEFT, 1, font23);
						}
					}

					document.add(table2);
				}
				if (sno != null) {
					sno1 = sno;
				}
				String status = image.split("_")[6].split("\\.")[0];
				String scenario = image.split("_")[2];

				String scenarios = SCENARIO_NAME + " :" + scenario;

				String sndo = image.split("_")[0];
				watsLogo.scalePercent(13, 13);
				Rectangle one1 = new Rectangle(1360, 1000);
				watsLogo.setAlignment(Image.ALIGN_RIGHT);
				if (image.startsWith(sndo + "_") && image.contains(FAILED)) {
					document.setPageSize(one1);
					document.newPage();
				} else {

					document.setPageSize(pageSize);
					document.newPage();
				}
				document.add(watsLogo);
				document.add(new Paragraph(scenarios, fnt12));
				String reason = image.split("_")[5];
				String step = LINE_NUMBER + "" + reason;

				String stepDescription = metaDataVO.getTestRunParamDesc();

				String inputParam = metaDataVO.getInputParameter();

				String inputValue = metaDataVO.getInputValue();

				if (API_TESTING.equalsIgnoreCase(checkPackage)) {
					inputValue = "";
				}

				Paragraph pr1 = new Paragraph();
				pr1.add("Status:");

				if (image.startsWith(sndo + "_") && image.contains(FAILED)) {
					String message = "Failed at Line Number:" + "" + reason;
					String error = metaDataVO.getLineErrorMsg();
					String errorMessage = "Failed Message:" + "" + error;
					Anchor target1 = new Anchor(status);
					target1.setName(String.valueOf(status + j));
					j++;
					pr1.add(target1);
					document.add(pr1);
					document.add(new Paragraph(message, fnt12));
					if (error != null) {
						document.add(new Paragraph(errorMessage, fnt12));
					}
					if (stepDescription != null) {
						document.add(new Paragraph(STEP_DESC + stepDescription, fnt12));
					}
					if (inputParam != null) {
						document.add(new Paragraph(TEST_PARAM + inputParam, fnt12));
						if (inputValue != null) {
							document.add(new Paragraph(TEST_VALUE + inputValue, fnt12));
						}
					}
					document.add(Chunk.NEWLINE);
					img.setAlignment(Image.ALIGN_CENTER);
					img.isScaleToFitHeight();
					// new change-change page size
					img.scalePercent(60, 60);
					document.add(img);

				} else {
					document.add(new Paragraph(step, fnt12));
					Anchor target1 = new Anchor(status);
					target1.setName(String.valueOf(status));
					pr1.add(target1);
					document.add(pr1);

					if (stepDescription != null) {
						document.add(new Paragraph(STEP_DESC + stepDescription, fnt12));
					}
					if (inputParam != null) {
						document.add(new Paragraph(TEST_PARAM + inputParam, fnt12));
						if (inputValue != null) {
							document.add(new Paragraph(TEST_VALUE + inputValue, fnt12));
						}
					}
					img.setAlignment(Image.ALIGN_CENTER);
					img.isScaleToFitHeight();
					img.scalePercent(60, 68);
					document.add(img);
				}

				Anchor target = new Anchor(String.valueOf(i));
				target.setName(String.valueOf(i));
				Anchor target1 = new Anchor(String.valueOf("Back to Index"), bf16);
				target1.setReference("#" + "details");
				Paragraph p = new Paragraph();
				p.add(target1);
				p.add(new Chunk(new VerticalPositionMark()));
				p.add(" page ");
				p.add(target);
				p.add(" of " + fileNameList.size());
				document.add(p);

				if (API_TESTING.equalsIgnoreCase(checkPackage)) {
					addingResponseIntoReport(fileName, document, customerDetails, fetchConfigVO);
				}
				
				//Adding the downloaded pdf after that particular script

				if (!currentSeqNumber.equalsIgnoreCase(nextSeqNumber) || fetchMetadataListVO.size() == increment) {
					String docName = (metaDataVO.getSeqNum() + "_" + metaDataVO.getScenarioName() + "_"
							+ metaDataVO.getScriptNumber() + "_" + customerDetails.getTestSetName() + "_Passed");
					File file = new File(fetchConfigVO.getDownlod_file_path() + docName + ".pdf");
					if (file.exists()) {
						PdfContentByte contentByte = writer.getDirectContent();
						PdfReader pdfReader = new PdfReader(fetchConfigVO.getDownlod_file_path() + docName + ".pdf");
						for (int page = 1; page <= pdfReader.getNumberOfPages(); page++) {
							PdfImportedPage pages = writer.getImportedPage(pdfReader, page);
							document.newPage();
							contentByte.addTemplate(pages, 1f, 0, 0, 1, 130, 0);

						}
					}
				}
			}

		}

	}

	public void insertCell(PdfPTable table, String text, int align, int colspan, Font font) {

		// create a new cell with the specified Text and Font
		PdfPCell cell = new PdfPCell(new Paragraph(text.trim(), font));
		cell.setBorder(PdfPCell.NO_BORDER);
		// set the cell alignment

		cell.setUseVariableBorders(true);
		if (STATUS.equalsIgnoreCase(text)) {
			cell.setBorderWidthLeft(0.3f);
			cell.setBorderColorLeft(new BaseColor(230, 225, 225));
			cell.setBorderWidthTop(0.3f);
			cell.setBorderColorTop(new BaseColor(230, 225, 225));
			cell.setBorderWidthRight(0.3f);
			cell.setBorderColorRight(new BaseColor(230, 225, 225));
			cell.setBorderWidthBottom(0.3f);
			cell.setBorderColorBottom(new BaseColor(230, 225, 225));
		} else if (TOTAL.equalsIgnoreCase(text) || PERCENTAGE.equalsIgnoreCase(text)) {
			cell.setBorderWidthTop(0.3f);
			cell.setBorderColorTop(new BaseColor(230, 225, 225));
			cell.setBorderWidthRight(0.3f);
			cell.setBorderColorRight(new BaseColor(230, 225, 225));
			cell.setBorderWidthBottom(0.3f);
			cell.setBorderColorBottom(new BaseColor(230, 225, 225));
		} else if (PASSED.equalsIgnoreCase(text) || FAILED.equalsIgnoreCase(text)) {
			cell.setBorderWidthLeft(0.3f);
			cell.setBorderColorLeft(new BaseColor(230, 225, 225));
			cell.setBorderWidthRight(0.3f);
			cell.setBorderColorRight(new BaseColor(230, 225, 225));
			cell.setBorderWidthBottom(0.3f);
			cell.setBorderColorBottom(new BaseColor(230, 225, 225));
		} else if (text.contains("%")) {
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
		// in case there is no text and you wan to create an empty row
		if (text.trim().equalsIgnoreCase("")) {
			cell.setMinimumHeight(20f);
		}
		if (text.length() > 103) {
			cell.setFixedHeight(80f);
		} else if (text.length() > 53) {
			cell.setFixedHeight(60f);
		} else {
			cell.setFixedHeight(40f);
		}
		// add the call to the table
		table.addCell(cell);

	}

	public void deleteScreenShotFromTempLocation(String screenshotFolder, CustomerProjectDto customerDetails) {
		String path = customerDetails.getCustomerName() + File.separator + customerDetails.getTestSetName()
				+ File.separator;
		try {
			if (screenshotFolder != null) {
				File folder = new File(screenshotFolder + path);
				if (folder.exists()) {
					FileUtils.deleteDirectory(folder);
				}
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	public void deletePdfsFromTempLocation(String pdfFolder, CustomerProjectDto customerDetails) {
		String path = customerDetails.getCustomerName() + File.separator + customerDetails.getTestSetName()
				+ File.separator;
		try {
			if (pdfFolder != null) {
				File folder = new File(pdfFolder + path);
				if (folder.exists()) {
					FileUtils.deleteDirectory(folder);
				}
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	public void deleteOldScreenshotForScriptFrmObjStore(ScriptDetailsDto testSetLine,
			CustomerProjectDto customerDetails) {
		ConfigFileReader.ConfigFile configFile = null;
		try {
			configFile = ConfigFileReader.parse(new FileInputStream(new File(ociConfigPath)), ociConfigName);
		} catch (IOException e) {
			throw new WatsEBSCustomException(500, "Not able to read object store config");
		}

		try {
			final AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);
			List<String> objNames = null;
			try (ObjectStorage client = new ObjectStorageClient(provider);) {

				String objectStoreScreenShotPath = SCREENSHOT + FORWARD_SLASH + customerDetails.getCustomerName()
						+ FORWARD_SLASH + customerDetails.getTestSetName() + FORWARD_SLASH + testSetLine.getSeqNum();
				ListObjectsRequest listScreenShotObjectsRequest = ListObjectsRequest.builder()
						.namespaceName(ociNamespace).bucketName(ociBucketName).prefix(objectStoreScreenShotPath)
						.delimiter(FORWARD_SLASH).build();

				ListObjectsResponse responseScreenShot = client.listObjects(listScreenShotObjectsRequest);

				objNames = responseScreenShot.getListObjects().getObjects().stream()
						.map((objSummary) -> objSummary.getName()).collect(Collectors.toList());

				logger.info(objNames.size());
				if (objNames.size() > 0) {
					ListIterator<String> listIt = objNames.listIterator();

					while (listIt.hasNext()) {
						String objectName = listIt.next();
						DeleteObjectResponse getResponse = client.deleteObject(DeleteObjectRequest.builder()
								.namespaceName(ociNamespace).bucketName(ociBucketName).objectName(objectName).build());
						if(getResponse != null) {
							logger.info("DELETED MARKER " + getResponse.getIsDeleteMarker());
						}
					}
				} else {
					logger.info("Screenshot is not present");
				}
			}
		} catch (Exception e1) {
			logger.error("Not able to connect with object store");
		}
		logger.info("Script screenshot deleted successfully!!");
	}

	public String uploadPDF(String sourceFile, String destinationFilePath) {

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
			final String FILE_NAME = sourceFile;
			File file = new File(FILE_NAME);
			long fileSize = FileUtils.sizeOf(file);
			InputStream is = new FileInputStream(file);

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
			throw new WatsEBSCustomException(500, "Exception occured while uploading pdf in Object Storage", e);
		}
	}

	public void createScreenShot(ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, String message,
			CustomerProjectDto customerDetails) throws Exception {
		try {
			File file = new ClassPathResource(whiteimage).getFile();

			File file1 = new ClassPathResource(watslogo.substring(10)).getFile();
			BufferedImage logo = null;
			logo = ImageIO.read(file1);

			BufferedImage image1 = null;
			image1 = ImageIO.read(file);
			Graphics g1 = image1.getGraphics();
			g1.setColor(Color.red);
			java.awt.Font font1 = new java.awt.Font("Calibir", java.awt.Font.PLAIN, 36);
			g1.setFont(font1);
			g1.drawString(message, 500, 360);
			g1.drawImage(logo, 1100, 100, null);
			g1.dispose();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image1, "png", baos);

			String imageName = (fetchMetadataVO.getSeqNum() + "_" + fetchMetadataVO.getLineNumber() + "_"
					+ fetchMetadataVO.getScenarioName() + "_" + fetchMetadataVO.getScriptNumber() + "_"
					+ customerDetails.getTestSetName() + "_" + fetchMetadataVO.getLineNumber() + "_Passed")
					.concat(PNG_EXTENSION);

			File imagePath = new File(fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + customerDetails.getCustomerName()
					+ FORWARD_SLASH + customerDetails.getTestSetName());

			if (!imagePath.exists()) {
				logger.error("creating directory: " + imagePath.getName());
				try {
					imagePath.mkdirs();
				} catch (SecurityException se) {
					logger.error(se);
				}
			} else {
				logger.error("Folder exist");
			}
			logger.error(imagePath.getName().toString());
			FileOutputStream fileOutputStream = new FileOutputStream(imagePath + File.separator + imageName);
			baos.writeTo(fileOutputStream);
			baos.close();
			fileOutputStream.close();
			File source = new File(imagePath + File.separator + imageName);
			String fileExtension = source.getName();

			fileExtension = fileExtension.substring(fileExtension.indexOf("."));

			String folderName = SCREENSHOT + FORWARD_SLASH + customerDetails.getCustomerName() + FORWARD_SLASH
					+ customerDetails.getTestSetName();

			uploadObjectToObjectStore(source.getCanonicalPath(), folderName, imageName);
			Files.delete(Paths.get(source.getPath()));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void downloadObjectFromObjectStore(String localFilePath, String folderName, String fileName) {
		GetObjectResponse response = null;
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
//			long fileSize = FileUtils.sizeOf(file);
//			InputStream is = new FileInputStream(file);
			String destinationFilePath = folderName + FORWARD_SLASH + fileName;
			/* Create a service client */
			try (ObjectStorageClient client = new ObjectStorageClient(provider);) {

				/* Create a request and dependent object(s). */

				GetObjectRequest getObjectRequest = GetObjectRequest.builder().namespaceName(ociNamespace)
						.bucketName(ociBucketName).objectName(destinationFilePath).build();

				response = client.getObject(getObjectRequest);
				try (final InputStream stream = response.getInputStream();
						final OutputStream outputStream = new FileOutputStream(file.getPath())) {

					byte[] buf = new byte[8192];
					int bytesRead;
					while ((bytesRead = stream.read(buf)) > 0) {
						outputStream.write(buf, 0, bytesRead);
					}
				}

			}
		} catch (WatsEBSCustomException e) {
			throw e;
		} catch (Exception e) {
			throw new WatsEBSCustomException(500, "Exception occured while uploading pdf in Object Storage", e);
		}

	}

	public void addingResponseIntoReport(String fileName, Document document, CustomerProjectDto customerDetails,
			FetchConfigVO fetchConfigVO) throws IOException, com.itextpdf.text.DocumentException {

		document.newPage();
		String folderName = fetchConfigVO.getWINDOWS_PDF_LOCATION() + customerDetails.getCustomerName() + FORWARD_SLASH
				+ customerDetails.getTestSetName();
		fileName = fileName + "_Passed.txt";
		String localPath = (fetchConfigVO.getWINDOWS_PDF_LOCATION() + customerDetails.getCustomerName() + File.separator
				+ customerDetails.getTestSetName() + File.separator) + fileName;
		downloadObjectFromObjectStore(localPath, folderName, fileName);
		Path path = Paths.get(localPath);
		byte[] bytes = Files.readAllBytes(path);
		String text = new String(bytes);
		ObjectMapper mapper = new ObjectMapper();
		Object json = mapper.readValue(text, Object.class);
		String prettyString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
		Paragraph paragraphResponse = new Paragraph("Response Body :- \n" + prettyString);
		paragraphResponse.setAlignment(Element.CCITTG3_2D);
		document.add(paragraphResponse);
	}

	public void inserStatusCodeInCell(Document document, Map<Integer, Integer> mapOfResponseCodeAndCount)
			throws com.itextpdf.text.DocumentException {
		document.newPage();
		Font font23 = FontFactory.getFont(ARIAL, 23);
		PdfPTable table1 = new PdfPTable(2);
		table1.setWidths(new int[] { 1, 1 });
		table1.setWidthPercentage(100f);
		String[] statusCodeArray = { "Response Code", "200", "201", "400", "401", "403", "404", "405", "408", "500",
				"503", "504", "Total" };
		String[] statusCount = { "Count", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0" };
		Integer total = 0;
		for (int i = 0; i < statusCodeArray.length; i++) {
			String str = statusCodeArray[i];
			Integer checkInteger = null;

			try {
				checkInteger = Integer.parseInt(statusCodeArray[i]);
			} catch (NumberFormatException e) {
				logger.error("Input String cannot be parsed to Integer.");
			}
			if (checkInteger != null && mapOfResponseCodeAndCount.containsKey(checkInteger)) {
				statusCount[i] = mapOfResponseCodeAndCount.get(checkInteger).toString();
				total += mapOfResponseCodeAndCount.get(checkInteger);
			}
			if (str.contains("Total")) {
				statusCount[i] = total.toString();
			}
			String str1 = statusCount[i];
			insertCell(table1, str, Element.ALIGN_LEFT, 1, font23);
			insertCell(table1, str1, Element.ALIGN_LEFT, 1, font23);
		}
		document.add(table1);
		document.newPage();
	}
	
	public void apiAccessTokenCreation(FetchConfigVO fetchConfigVO,ScriptDetailsDto fetchMetadataVO,CustomerProjectDto customerDetails)
			throws Exception {

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		String str = "{\n"
				+ "  \"HTTP Type\": \"POST\",\n"
				+ "  \"Request Header\": {\n"
				+ "    \"Content-Type\": \"application/x-www-form-urlencoded\"\n"
				+ "  },\n"
				+ "  \"Request Body\": {\n"
				+ "    \"grant_type\": \"client_credentials\"\n"
				+ "  }\n"
				+ "}";
		ApiValidationVO apiValidationData = objectMapper.readValue(str,ApiValidationVO.class);
		apiValidationData.setUrl(fetchConfigVO.getAPI_AUTHENTICATION_URL());
		String token = null;
		try {

			HttpHeaders headers = new HttpHeaders();
			for (Entry<String, String> map : apiValidationData.getRequestHeader().entrySet()) {
				headers.set(map.getKey(), map.getValue());
			}
			headers.set("Authorization", "Basic "+fetchConfigVO.getAPI_AUTHENTICATION_CODE());		// Converting object to string
//			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//			String json = ow.writeValueAsString(apiValidationData.getRequestBody());

			// Converting Request body object into map
			Gson gson = new Gson();
			Map<String, String> attributes = gson.fromJson(gson.toJson(apiValidationData.getRequestBody()), Map.class);

			// Setting Map value to MultiValueMap
			MultiValueMap<String, String> bodyValues = new LinkedMultiValueMap<>();
			for (Entry<String, String> map : attributes.entrySet()) {
				bodyValues.set(map.getKey(), map.getValue());
			}

			// Fetching HttpMethod
			HttpMethod httpMethod = HttpMethod.valueOf(apiValidationData.getHttpType());

			// Creating WebClient
			WebClient client = WebClient.create();
			Map<String, String> response = client.method(httpMethod).uri(new URI(apiValidationData.getUrl()))
					.headers(headersHttp -> headersHttp.addAll(headers))
					.contentType(MediaType.APPLICATION_FORM_URLENCODED).body(BodyInserters.fromFormData(bodyValues))
					.retrieve().bodyToMono(Map.class).block();

			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(response);
			apiValidationData.setResponse(json);


			// Getting the token from the response
			token = response.get("access_token");
			
			databaseentry.insertRecordInTestSetAttribute(customerDetails.getTestSetId(),"access_token",token,fetchMetadataVO.getExecutedBy());
			Date date = new Date(System.currentTimeMillis() - 3600 * 1000);
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			TimeZone timeZone = TimeZone.getTimeZone("GMT");
			formatter.setTimeZone(timeZone);
			String expiresTime = formatter.format(date);
			databaseentry.insertRecordInTestSetAttribute(customerDetails.getTestSetId(),"expires_in",expiresTime,fetchMetadataVO.getExecutedBy());
		} catch (Exception ex) {
			throw ex;
		}
//		return token;
	}

	public void apiAccessToken(ScriptDetailsDto fetchMetadataVO, Map<String, String> accessTokenStorage, CustomerProjectDto customerDetails)
			throws Exception {

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		ApiValidationVO apiValidationData = objectMapper.readValue(fetchMetadataVO.getInputValue(),
				ApiValidationVO.class);

		String token = null;
		try {

			HttpHeaders headers = new HttpHeaders();
			for (Entry<String, String> map : apiValidationData.getRequestHeader().entrySet()) {
				headers.set(map.getKey(), map.getValue());
			}

			// Converting object to string
//			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//			String json = ow.writeValueAsString(apiValidationData.getRequestBody());

			// Converting Request body object into map
			Gson gson = new Gson();
			Map<String, String> attributes = gson.fromJson(gson.toJson(apiValidationData.getRequestBody()), Map.class);

			// Setting Map value to MultiValueMap
			MultiValueMap<String, String> bodyValues = new LinkedMultiValueMap<>();
			for (Entry<String, String> map : attributes.entrySet()) {
				bodyValues.set(map.getKey(), map.getValue());
			}

			// Fetching HttpMethod
			HttpMethod httpMethod = HttpMethod.valueOf(apiValidationData.getHttpType());

			// Creating WebClient
			WebClient client = WebClient.create();
			Map<String, String> response = client.method(httpMethod).uri(new URI(apiValidationData.getUrl()))
					.headers(headersHttp -> headersHttp.addAll(headers))
					.contentType(MediaType.APPLICATION_FORM_URLENCODED).body(BodyInserters.fromFormData(bodyValues))
					.retrieve().bodyToMono(Map.class).block();

			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(response);
			apiValidationData.setResponse(json);

			String value = ow.writeValueAsString(apiValidationData);
			String testParamId = fetchMetadataVO.getTestScriptParamId();
			String testSetId = fetchMetadataVO.getTestSetLineId();
			dynamicnumber.saveCopyNumber(value, testParamId, testSetId);

			// Getting the token from the response
			token = response.get("access_token");
//			String key = "Access Token";
			String key = customerDetails.getTestSetName() + ">" + fetchMetadataVO.getSeqNum() + ">"
					+ fetchMetadataVO.getLineNumber();
			accessTokenStorage.put(key, token);
		} catch (Exception ex) {
			throw ex;
		}
//		return token;
	}

	public void apiValidationResponse(ScriptDetailsDto fetchMetadataVO, Map<String, String> accessTokenStorage,
			ApiValidationVO api, CustomerProjectDto customerDetails,FetchConfigVO fetchConfigVO) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		String inputValue = fetchMetadataVO.getInputValue().replaceAll("(\")(?=[\\{])|(?<=[\\}])(\")|(\\\\)(?=[\\\"])","");
		ApiValidationVO apiValidationData = objectMapper.readValue(inputValue,
				ApiValidationVO.class);

		try {
			
			TestSetAttribute testSetAttributeAccessToken = databaseentry.getApiValueBySetIdAndAPIKey(customerDetails.getTestSetId(), "access_token");
			if(testSetAttributeAccessToken!=null) {
				TestSetAttribute testSetAttributeExpiresIn = databaseentry.getApiValueBySetIdAndAPIKey(customerDetails.getTestSetId(), "expires_in");
				boolean expireIsPresent = testSetAttributeExpiresIn != null;
				boolean authenticationValues = (fetchConfigVO.getAPI_AUTHENTICATION_URL()!=null && fetchConfigVO.getAPI_AUTHENTICATION_CODE() !=null);
				if (expireIsPresent && authenticationValues) {
					if(expireIsPresent) {
						
					}else{
						apiAccessTokenCreation(fetchConfigVO,fetchMetadataVO,customerDetails);
					}
				}else {
					apiValidationData.setAccessToken(testSetAttributeAccessToken.getAttributeValue());
				}
				
			}
			else {
				apiAccessTokenCreation(fetchConfigVO,fetchMetadataVO,customerDetails);
			}

			WebClient client = WebClient.create();
//            String accessType="Bearer";
			HttpHeaders headers = new HttpHeaders();
			for (Entry<String, String> map : apiValidationData.getRequestHeader().entrySet()) {
				headers.set(map.getKey(), map.getValue());
			}

			if (apiValidationData.getAccessToken() != null) {
//				String[] str = apiValidationData.getAccessToken().split(">");
//				System.out.println(str);
//				String data = dynamicnumber.getCopynumber(str[0], str[1], str[2]);
//				ApiValidationVO token = objectMapper.readValue(data, ApiValidationVO.class);
//				Map<String, String> map = objectMapper.readValue(token.getResponse(), Map.class);
				if("Bearer".equalsIgnoreCase(apiValidationData.getAuthenticationType()) || "Basic".equalsIgnoreCase(apiValidationData.getAuthenticationType()))
                {
                    
                    headers.set("Authorization", apiValidationData.getAuthenticationType() +apiValidationData.getAccessToken());
                }
                else
                {
                	headers.set("Authorization", "Bearer" +apiValidationData.getAccessToken());
                }
			}
			apiValidationData.setAccessToken(null);
			// Converting object to string
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String strInput = ow.writeValueAsString(apiValidationData.getRequestBody());

			// Fetching HttpMethod
			HttpMethod httpMethod = HttpMethod.valueOf(apiValidationData.getHttpType());
			ClientResponse response;

			if (apiValidationData.getRequestBody() != null && !ObjectUtils.isEmpty(apiValidationData.getRequestBody())) {
				response = client.method(httpMethod).uri(new URI(apiValidationData.getUrl()))
						.headers(headersHttp -> headersHttp.addAll(headers)).accept(MediaType.APPLICATION_JSON)
						.body(BodyInserters.fromObject(strInput)).exchange().block();
			} else {
				response = client.method(httpMethod).uri(new URI(apiValidationData.getUrl()))
						.headers(headersHttp -> headersHttp.addAll(headers)).accept(MediaType.APPLICATION_JSON)
						.exchange().block();
			}

			Mono<String> bodyToMono = response.bodyToMono(String.class);
			bodyToMono.subscribe((body) -> {
				api.setResponse(body);
			}, (ex) -> {
			});
			api.setResponseCode(response.statusCode().value());
			apiValidationData.setResponseCode(response.statusCode().value());
			apiValidationData.setAccessToken("");
			ObjectWriter ow1 = new ObjectMapper().writer();
			String value = ow1.writeValueAsString(apiValidationData);
			String testParamId = fetchMetadataVO.getTestScriptParamId();
			String testSetId = fetchMetadataVO.getTestSetLineId();
			dynamicnumber.saveCopyNumber(value, testParamId, testSetId);
//			return response.statusCode();
			createScreenShot(fetchMetadataVO,fetchConfigVO,"Response : "+api.getResponseCode(),customerDetails);
			
			String fileName = (fetchConfigVO.getWINDOWS_PDF_LOCATION()+customerDetails.getTestSetName()+"/"+fetchMetadataVO.getSeqNum() + "_"
					+ fetchMetadataVO.getLineNumber() + "_" + fetchMetadataVO.getScenarioName() + "_"
					+ fetchMetadataVO.getScriptNumber() + "_" + customerDetails.getTestSetName() + "_"
					+ fetchMetadataVO.getLineNumber() + "_Passed").concat(".txt");
			String name = (fetchMetadataVO.getSeqNum() + "_"
					+ fetchMetadataVO.getLineNumber() + "_" + fetchMetadataVO.getScenarioName() + "_"
					+ fetchMetadataVO.getScriptNumber() + "_" + customerDetails.getTestSetName() + "_"
					+ fetchMetadataVO.getLineNumber() + "_Passed").concat(".txt");
			createDir(fetchConfigVO.getWINDOWS_PDF_LOCATION()+customerDetails.getTestSetName());
			
			try (PrintWriter out = new PrintWriter(fileName)) {
			    out.println(api.getResponse());
			}
//			String folderName = "API" + "/" + customerDetails.getCustomerName() + "/"
//					+ customerDetails.getTestSetName();
			File source = new File(fileName);
			uploadObjectToObjectStore(source.getCanonicalPath(), fetchConfigVO.getWINDOWS_PDF_LOCATION()+customerDetails.getCustomerName()+"/"+customerDetails.getTestSetName(), name);
			Files.delete( Paths.get(fileName));
		} catch (Exception ex) {
			throw ex;
		}
	}

	public boolean validation(ScriptDetailsDto fetchMetadataVO, ApiValidationVO api) throws Exception {
		String[] values = fetchMetadataVO.getInputValue().split("/");
		for (String str : values) {
			if (api.getResponseCode().toString().contains(str)) {
				return true;
			}
		}
		throw new Exception("Validation Failed.");
	}

	public void renameDownloadedFile(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,CustomerProjectDto customerDetails) throws InterruptedException {
		// For getting the name of the downloaded file name

		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("window.open()");
		ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
		String fileName = null;
		if(fetchConfigVO.getBrowser().equalsIgnoreCase("chrome")) {
			driver.switchTo().window(tabs.get(1)).get("chrome://downloads");
			/* Download Window Open */
			Thread.sleep(3000);
			 fileName = (String) jse.executeScript(
					"return document.querySelector('downloads-manager').shadowRoot.querySelector('#downloadsList downloads-item').shadowRoot.querySelector('div#content #file-link').text");
			driver.close();
			driver.switchTo().window(tabs.get(0));
			logger.info("File Name*** " + fileName);
			
		}else if(fetchConfigVO.getBrowser().equalsIgnoreCase("firefox")) {
			driver.switchTo().window(tabs.get(1)).get("about:downloads");
			/* Download Window Open */
			Thread.sleep(3000);
			 fileName = (String) jse.executeScript(
					"return document.querySelector('#contentAreaDownloadsView .downloadMainArea .downloadContainer description:nth-of-type(1)').value");
			driver.close();
			driver.switchTo().window(tabs.get(0));
			logger.info("File Name*** " + fileName);
		}
		
		if (fileName != null) {
			File oldFile = new File(fetchConfigVO.getDownlod_file_path() + fileName);

			String newName = (fetchMetadataVO.getSeqNum() + "_" + fetchMetadataVO.getScenarioName() + "_"
					+ fetchMetadataVO.getScriptNumber() + "_" + customerDetails.getTestSetName() + "_Passed");
			if (new File(fetchConfigVO.getDownlod_file_path() + newName + ".pdf").exists())
				new File(fetchConfigVO.getDownlod_file_path() + newName + ".pdf").delete();

			if (oldFile.exists()) {
				if (oldFile.renameTo(new File(fetchConfigVO.getDownlod_file_path() + newName + ".pdf"))) {
					logger.info("File name changed succesful");
				} else {
					logger.info("Rename failed");
				}
			}
		}
	}
	
	public List<String> getListOfFileNamesPresentInObjectStore(String objectStorePdfPath) throws Exception {

		List<String> objNames = null;
		ConfigFileReader.ConfigFile configFile = null;
		try {
			configFile = ConfigFileReader.parse(new FileInputStream(new File(ociConfigPath)), ociConfigName);
		} catch (IOException e) {
			throw new WatsEBSCustomException(500, "Exception occured while connecting to oci/config path", e);
		}
		try {
			final AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);
			try (ObjectStorage client = new ObjectStorageClient(provider);) {

				ListObjectsRequest listPdfObjectsRequest = ListObjectsRequest.builder().namespaceName(ociNamespace)
						.bucketName(ociBucketName).prefix(objectStorePdfPath).delimiter("/").build();

				ListObjectsResponse responsePdf = client.listObjects(listPdfObjectsRequest);

				objNames = responsePdf.getListObjects().getObjects().stream().map((objSummary) -> objSummary.getName())
						.collect(Collectors.toList());

				logger.info(objNames.size());
				return objNames;
			} catch (Exception e1) {

				throw new WatsEBSCustomException(500, "Not able to connect with object store");
			}
		} catch (Exception e) {
			throw new WatsEBSCustomException(500,
					"Exception occured while getting files from object path location.", e);
		}

	}

	public String createDirInCloud(String folderName) {

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
			String destinationFilePath = folderName + FORWARD_SLASH;
			/* Create a service client */
			try (ObjectStorageClient client = new ObjectStorageClient(provider);) {

				// create empty content
				InputStream emptyContent = new ByteArrayInputStream(new byte[0]);

				/* Create a request and dependent object(s). */
				PutObjectRequest putObjectRequest = PutObjectRequest.builder().namespaceName(ociNamespace)
						.bucketName(ociBucketName).objectName(destinationFilePath).contentLength(0L)
						.putObjectBody(emptyContent).build();

				/* Send request to the Client */
				response = client.putObject(putObjectRequest);
			}
			return response.toString();
		} catch (WatsEBSCustomException e) {
			throw e;
		} catch (Exception e) {
			throw new WatsEBSCustomException(500, "Exception occured while creating folder in Object Storage..", e);
		}
	}
	
	public void uploadFileAutoIT(WebDriver webDriver, String fileLocation, String param1, String param2, String param3)
			throws Exception {

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
		try {
			String autoitscriptpath = System.getProperty("user.dir") + "/" + "File_upload_selenium_webdriver.au3";
			Runtime.getRuntime().exec("cmd.exe /c Start AutoIt3.exe " + autoitscriptpath + " \"" + fileLocation + "\"");
			logger.info("Successfully Uploaded The File");
			return;
		} catch (Exception e) {
			logger.error("Failed During uploadFileAutoIT Action.");
			System.out.println(fileLocation);
			e.printStackTrace();
			throw e;

		}

	}

	public void uploadPdfToSharepoint(List<ScriptDetailsDto> fetchMetadataListVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) {
		try {
			String accessToken = getSharepointAccessTokenPdf(fetchConfigVO);
			List imageUrlList = new ArrayList();
			File imageDir = new File(fetchConfigVO.getPdf_path() + customerDetails.getCustomerName() + "/"
					+ customerDetails.getTestSetName() + "/");
			System.out.println(imageDir);

			RestTemplate restTemplate = new RestTemplate();

			// Outer header
			HttpHeaders uploadSessionHeader = new HttpHeaders();
			// uploadSessionHeader.setContentType(MediaType.APPLICATION_JSON);
			uploadSessionHeader.add("Authorization", "Bearer " + accessToken);
			HttpEntity<byte[]> uploadSessionRequest = new HttpEntity<>(null, uploadSessionHeader);

			// SITE-ID
			ResponseEntity<Object> siteDetailsResponse = restTemplate.exchange("https://graph.microsoft.com/v1.0/sites/"
					+ fetchConfigVO.getSharePoint_URL() + ":/sites/" + fetchConfigVO.getSite_Name(), HttpMethod.GET,
					uploadSessionRequest, Object.class);

			Map<String, Object> siteDetailsMap = siteDetailsResponse.getBody() != null
					? (LinkedHashMap<String, Object>) siteDetailsResponse.getBody()
					: null;
			String siteId = siteDetailsMap != null
					? StringUtils.convertToString(siteDetailsMap.get("id").toString().split(",")[1])
					: null;

			// DRIVE-ID
			ResponseEntity<Object> driveDetailsResponse = restTemplate.exchange(
					"https://graph.microsoft.com/v1.0/sites/" + siteId + "/drives", HttpMethod.GET,
					uploadSessionRequest, Object.class);

			Map<String, Object> driveDetailsMap = driveDetailsResponse.getBody() != null
					? (LinkedHashMap<String, Object>) driveDetailsResponse.getBody()
					: null;

			List<Map<String, String>> list = (List<Map<String, String>>) driveDetailsMap.get("value");

			String driveId = null;
			for (Map<String, String> map : list) {
				if (fetchConfigVO.getSharePoint_Library_Name() != null) {
					if (fetchConfigVO.getSharePoint_Library_Name().equalsIgnoreCase(map.get("name"))) {
						driveId = map.get("id");
						break;
					}
				} else {
					if ("Documents".equalsIgnoreCase(map.get("name"))) {
						driveId = map.get("id");
						break;
					}
				}
			}

//			System.out.println("https://graph.microsoft.com/v1.0/drives/"+driveId+"/root:/test");

			// SITE-ID
			ResponseEntity<Object> itemDetailsResponse = restTemplate
					.exchange(
							"https://graph.microsoft.com/v1.0/drives/" + driveId + "/root:/"
									+ fetchConfigVO.getDirectory_Name(),
							HttpMethod.GET, uploadSessionRequest, Object.class);

			Map<String, Object> itemDetailsMap = itemDetailsResponse.getBody() != null
					? (LinkedHashMap<String, Object>) itemDetailsResponse.getBody()
					: null;

			String itemId = itemDetailsMap != null ? StringUtils.convertToString(itemDetailsMap.get("id")) : null;
			for (File imageFile : imageDir.listFiles()) {
				String imageFileName = imageFile.getName();
				System.out.println(imageFileName);
				imageUrlList.add(imageFileName);
				File pdfFile = new File(imageDir + "/" + imageFileName);
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
				MultiValueMap<String, byte[]> bodyMap = new LinkedMultiValueMap<>();
				bodyMap.add("user-file", data);

				ResponseEntity<Object> response = restTemplate.exchange(
						"https://graph.microsoft.com/v1.0/drives/" + driveId + "/items/" + itemId + ":/"
								+ customerDetails.getCustomerName() + "/" + customerDetails.getProjectName() + "/"
								+ customerDetails.getTestSetName() + "/" + imageFileName + ":/createUploadSession",
						HttpMethod.POST, uploadSessionRequest, Object.class);

//				ResponseEntity<Object> response = restTemplate.exchange("https://graph.microsoft.com/v1.0/drives/"
//						+ fetchConfigVO.getSharepoint_drive_id() + "/items/" + fetchConfigVO.getSharepoint_item_id()
//						+ ":/Screenshot/" + fetchMetadataListVO.get(0).getCustomer_name() + "/"
//						+ fetchMetadataListVO.get(0).getTest_run_name() + "/" + imageFileName + ":/createUploadSession",
//						HttpMethod.POST, uploadSessionRequest, Object.class);

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

	public String getSharepointAccessTokenPdf(FetchConfigVO fetchConfigVO) {
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
}
