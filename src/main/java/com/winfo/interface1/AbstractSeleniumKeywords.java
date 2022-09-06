package com.winfo.interface1;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import org.jfree.util.Log;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

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
import com.winfo.services.DataBaseEntry;
import com.winfo.services.FetchConfigVO;
import com.winfo.utils.Constants.TEST_SET_LINE_ID_STATUS;
import com.winfo.utils.DateUtils;
import com.winfo.vo.CustomerProjectDto;
import com.winfo.vo.ScriptDetailsDto;

@Service
public abstract class AbstractSeleniumKeywords {

	public final Logger logger = LogManager.getLogger(AbstractSeleniumKeywords.class);
	@Value("${oci.config.name}")
	private String ociConfigName;
	@Value("${oci.bucket.name}")
	private String ociBucketName;
	@Value("${oci.namespace}")
	private String ociNamespace;
	@Value("${configvO.watslogo}")
	private String watslogo;

	private static final String PNG_EXTENSION = ".png";
	private static final String JPG_EXTENSION = ".jpg";
	public static final String FORWARD_SLASH = "/";
	private static final String[] CONST = { "Status", "Total", "Percentage" };
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
	private static final String STEP_NO = "Step No : ";
	private static final String SCREENSHOT = "Screenshot";

	@Autowired
	DataBaseEntry dataBaseEntry;

	public String screenshot(WebDriver driver, String screenshotName, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {
		String imageName = null;
		String folderName = null;
		try {
			TakesScreenshot ts = (TakesScreenshot) driver;
			File source = ts.getScreenshotAs(OutputType.FILE);
			String fileExtension = source.getName();

			fileExtension = fileExtension.substring(fileExtension.indexOf("."));

			folderName = "Screenshot" + "/" + customerDetails.getCustomerName() + "/"
					+ customerDetails.getTestSetName();
			imageName = (fetchMetadataVO.getSeqNum() + "_" + fetchMetadataVO.getLineNumber() + "_"
					+ fetchMetadataVO.getScenarioName() + "_" + fetchMetadataVO.getScriptNumber() + "_"
					+ customerDetails.getTestSetName() + "_" + fetchMetadataVO.getLineNumber() + "_Passed")
					.concat(fileExtension);

			uploadObjectToObjectStore(source.getCanonicalPath(), folderName, imageName);

			logger.info("Successfully Screenshot is taken " + imageName);
			return folderName + "/" + imageName;

		} catch (Exception e) {
			logger.error("Failed During Taking screenshot");
			logger.info("Exception while taking Screenshot" + e.getMessage());
			return e.getMessage();
		}
	}

	public String screenshotFail(WebDriver driver, String screenshotName, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {
		String imageName = null;
		String folderName = null;
		try {
			TakesScreenshot ts = (TakesScreenshot) driver;
			File source = ts.getScreenshotAs(OutputType.FILE);

			String fileExtension = source.getName();

			fileExtension = fileExtension.substring(fileExtension.indexOf("."));
			folderName = "Screenshot" + "/" + customerDetails.getCustomerName() + "/"
					+ customerDetails.getTestSetName();
			imageName = (fetchMetadataVO.getSeqNum() + "_" + fetchMetadataVO.getLineNumber() + "_"
					+ fetchMetadataVO.getScenarioName() + "_" + fetchMetadataVO.getScriptNumber() + "_"
					+ customerDetails.getTestSetName() + "_" + fetchMetadataVO.getLineNumber() + "_Failed")
					.concat(fileExtension);
			uploadObjectToObjectStore(source.getCanonicalPath(), folderName, imageName);
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.info("Successfully Failed Screenshot is Taken " + scripNumber);
			return folderName + "/" + imageName;
		} catch (Exception e) {
			String scripNumber = fetchMetadataVO.getScriptNumber();
			logger.error("Failed during screenshotFail Action. " + scripNumber);
			System.out.println("Exception while taking Screenshot" + e.getMessage());
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
					.parse(new ClassPathResource("oci/config").getInputStream(), ociConfigName);
			final AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);
			final String FILE_NAME = localFilePath;
			File file = new File(FILE_NAME);
			long fileSize = FileUtils.sizeOf(file);
			InputStream is = new FileInputStream(file);
			String destinationFilePath = folderName + "/" + fileName;
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

	public void downloadScreenshotsFromObjectStore(String screenshotPath, String customerName, String testSetName,
			String seqNum) {
		ConfigFileReader.ConfigFile configFile = null;
		List<String> objNames = null;
		try {
			configFile = ConfigFileReader.parse(new ClassPathResource("oci/config").getInputStream(), ociConfigName);
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
						.bucketName(ociBucketName).prefix(objectStoreScreenShotPath).delimiter("/").build();

				/* Send request to the Client */
				ListObjectsResponse response = client.listObjects(listObjectsRequest);

				objNames = response.getListObjects().getObjects().stream().map((objSummary) -> objSummary.getName())
						.collect(Collectors.toList());
				logger.info(objNames.size());
				ListIterator<String> listIt = objNames.listIterator();
				createDir(screenshotPath);
				while (listIt.hasNext()) {
					String objectName = listIt.next();
					GetObjectResponse getResponse = client.getObject(GetObjectRequest.builder()
							.namespaceName(ociNamespace).bucketName(ociBucketName).objectName(objectName).build());

					String imageName = objectName.substring(objectName.lastIndexOf("/") + 1, objectName.length());
					File file = new File(screenshotPath + File.separator + imageName);
					logger.info("Image Name ****** " + imageName);
					logger.info(file.exists() + "FileExist or not ******" + file.getPath());
					if (!file.exists()) {
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
							throw new WatsEBSCustomException(500,
									"Exception occured while read or write screenshot from Object Storage", e1);
						}
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

	private void createDir(String path) {
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
			if (testLinesStatus.equalsIgnoreCase(TEST_SET_LINE_ID_STATUS.Pass.getLabel())) {
				passCount++;
			} else if (testLinesStatus.equalsIgnoreCase(TEST_SET_LINE_ID_STATUS.Fail.getLabel())) {
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
			CustomerProjectDto customerDetails) throws IOException {

		String folder = fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + customerDetails.getCustomerName()
				+ File.separator + customerDetails.getTestSetName() + File.separator;

		/* FOR MP4 FUNCTIONALITY */
		String videoRec = "no";

		Map<Integer, List<File>> filesMap = new TreeMap<>();
		List<String> targetPassedPdf = new ArrayList<>();
		Map<String, String> seqNumMap = new HashMap<>();
		for (Object[] obj : fetchConfigVO.getSeqNumAndStatus()) {
			seqNumMap.put(obj[0].toString(), obj[1].toString());
		}
		List<String> fileSeqList = fileSeqContainer(fetchMetadataListVO, customerDetails.getTestSetName());
		for (String fileNames : fileSeqList) {
			if (fileNames.endsWith(PASSED)) {
				fileNames = new File(folder + fileNames + ".png").exists() ? fileNames + ".png" : fileNames;
				fileNames = (!(fileNames.endsWith(".png")) && (new File(folder + fileNames + ".jpg").exists()))
						? fileNames + ".jpg"
						: fileNames;
				File newFile = new File(folder + fileNames);
				if (newFile.exists()) {
					Integer seqNum = Integer.valueOf(newFile.getName().substring(0, newFile.getName().indexOf('_')));
					if (seqNumMap.get(seqNum.toString()).equals("Pass")) {
						filesMap.putIfAbsent(seqNum, new ArrayList<File>());
						filesMap.get(seqNum).add(newFile);
						targetPassedPdf.add(newFile.getName());
					}
				}
			}
		}

		return targetPassedPdf;
	}

	public List<String> getFailedPdfNew(List<ScriptDetailsDto> fetchMetadataListVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) throws IOException {

		String folder = fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + customerDetails.getCustomerName()
				+ File.separator + customerDetails.getTestSetName() + File.separator;
		String videoRec = "no";
		Map<String, String> seqNumMap = new HashMap<>();
		for (Object[] obj : fetchConfigVO.getSeqNumAndStatus()) {
			seqNumMap.put(obj[0].toString(), obj[1].toString());
		}
		List<String> targetFailedPdf = new ArrayList<>();
		List<String> fileSeqList = fileSeqContainer(fetchMetadataListVO, customerDetails.getTestSetName());
		Map<Integer, List<File>> filesMap = new TreeMap<>();
		for (String fileNames : fileSeqList) {
			fileNames = new File(folder + fileNames + ".png").exists() ? fileNames + ".png" : fileNames;
			fileNames = (!(fileNames.endsWith(".png")) && (new File(folder + fileNames + ".jpg").exists()))
					? fileNames + ".jpg"
					: fileNames;
			File newFile = new File(folder + fileNames);
			if (newFile.exists()) {
				Integer seqNum = Integer.valueOf(newFile.getName().substring(0, newFile.getName().indexOf('_')));
				if (seqNumMap.get(seqNum.toString()).equals("Fail")) {
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
			CustomerProjectDto customerDetails) throws IOException {

		String folder = fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + customerDetails.getCustomerName()
				+ File.separator + customerDetails.getTestSetName() + File.separator;
		Map<Integer, List<File>> filesMap = new TreeMap<>();
		List<String> fileSeqList = fileSeqContainer(fetchMetadataListVO, customerDetails.getTestSetName());
		List<String> detailsFileName = new ArrayList<>();
		for (String fileNames : fileSeqList) {
			fileNames = new File(folder + fileNames + ".png").exists() ? fileNames + ".png" : fileNames;
			fileNames = (!(fileNames.endsWith(".png")) && (new File(folder + fileNames + ".jpg").exists()))
					? fileNames + ".jpg"
					: fileNames;
			File file = new File(folder + fileNames);
			if (file.exists()) {
				Integer seqNum = Integer.valueOf(file.getName().substring(0, file.getName().indexOf('_')));
				if (!filesMap.containsKey(seqNum)) {
					filesMap.put(seqNum, new ArrayList<File>());
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
			if (fetchMetaData.getStatus().equals("Pass")) {
				fetchConfigVODtl.add(fetchMetaData.getSeqNum() + "_"

						+ fetchMetaData.getLineNumber() + "_" + fetchMetaData.getScenarioName() + "_"

						+ fetchMetaData.getScriptNumber() + "_" + testRunName + "_"

						+ fetchMetaData.getLineNumber() + "_Passed");
			} else if (fetchMetaData.getStatus().equals("Fail")) {
				fetchConfigVODtl.add(fetchMetaData.getSeqNum() + "_"

						+ fetchMetaData.getLineNumber() + "_" + fetchMetaData.getScenarioName() + "_"

						+ fetchMetaData.getScriptNumber() + "_" + testRunName + "_"

						+ fetchMetaData.getLineNumber() + "_Failed");
			}
		}
		return fetchConfigVODtl;
	}

	public List<String> getFileNameListNew(List<ScriptDetailsDto> fetchMetadataListVO, FetchConfigVO fetchConfigVO,
			CustomerProjectDto customerDetails) throws IOException {

		String folder = fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + customerDetails.getCustomerName()
				+ File.separator + customerDetails.getTestSetName() + File.separator;
		String videoRec = "no";
		List<File> fileList = new ArrayList<>();
		List<String> fileSeqList = fileSeqContainer(fetchMetadataListVO, customerDetails.getTestSetName());
		for (String newFile : fileSeqList) {
			newFile = new File(folder + newFile + ".png").exists() ? newFile + ".png" : newFile;
			newFile = (!(newFile.endsWith(".png")) && (new File(folder + newFile + ".jpg").exists())) ? newFile + ".jpg"
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

	public String findExecutionTimeForScript(String testSetId, String pdffileName) {

		String scriptStatus = null;

		if (pdffileName.equalsIgnoreCase("Passed_Report.pdf")) {
			scriptStatus = "Pass";
		} else if (pdffileName.equalsIgnoreCase("Failed_Report.pdf")) {
			scriptStatus = "Fail";
		} else {
			scriptStatus = null;
		}

		List<Object[]> startAndEndDates = dataBaseEntry.findStartAndEndTimeForTestRun(testSetId, scriptStatus);
		long totalDiff = 0;
		for (Object[] date : startAndEndDates) {
			if (date[0] != null && date[1] != null) {
				totalDiff += DateUtils.findTimeDifference(date[0].toString(), date[1].toString());
			}
		}

		return DateUtils.convertMiliSecToDayFormat(totalDiff);
	}

	public void createPdf(List<ScriptDetailsDto> fetchMetadataListVO, FetchConfigVO fetchConfigVO, String pdffileName,
			CustomerProjectDto customerDetails) {
		try {
			String folder = (fetchConfigVO.getWINDOWS_PDF_LOCATION() + customerDetails.getCustomerName()
					+ File.separator + customerDetails.getTestSetName() + File.separator);
			String file = (folder + pdffileName);
			findPassAndFailCount(fetchConfigVO, customerDetails.getTestSetId());

			List<String> fileNameList = null;
			if ("Passed_Report.pdf".equalsIgnoreCase(pdffileName)) {
				fileNameList = getPassedPdfNew(fetchMetadataListVO, fetchConfigVO, customerDetails);
			} else if ("Failed_Report.pdf".equalsIgnoreCase(pdffileName)) {
				fileNameList = getFailedPdfNew(fetchMetadataListVO, fetchConfigVO, customerDetails);
			} else if ("Detailed_Report.pdf".equalsIgnoreCase(pdffileName)) {
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
			watsLogo.scalePercent(65, 68);
			watsLogo.setAlignment(Image.ALIGN_RIGHT);
			Date tendTime = fetchConfigVO.getEndtime();
			Date tStarttime = fetchConfigVO.getStarttime();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss aa");
			String testRunName1 = customerDetails.getTestSetName();

			if ((!fileNameList.isEmpty()) && ("Passed_Report.pdf".equalsIgnoreCase(pdffileName)
					|| "Failed_Report.pdf".equalsIgnoreCase(pdffileName)
					|| "Detailed_Report.pdf".equalsIgnoreCase(pdffileName))) {
				int passcount = fetchConfigVO.getPasscount();
				int failcount = fetchConfigVO.getFailcount();
				int others = fetchConfigVO.getOtherCount();

				String executedTime = findExecutionTimeForScript(customerDetails.getTestSetId(), pdffileName);
				String startTime = dateFormat.format(tStarttime);
				String endTime = dateFormat.format(tendTime);
				String executionTime = executedTime;
				String tr = TEST_RUN_NAME;
				String sn = EXECUTED_BY;
				String sn1 = START_TIME;
				String s1 = END_TIME;
				String scenarios1 = EXECUTION_TIME;
				String[] testArr = { tr, testRunName1, sn, executedBy, sn1, startTime, s1, endTime, scenarios1,
						executionTime };
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

				if ("Detailed_Report.pdf".equalsIgnoreCase(pdffileName)) {
					generateDetailsPDF(document, watsLogo, passcount, failcount, others, writer);
				} else if ("Passed_Report.pdf".equalsIgnoreCase(pdffileName)) {
					generatePassPDF(document, passcount, failcount);
				} else {
					generateFailedPDF(document, passcount, failcount);
				}
				addRestOfPagesToPDF(document, fileNameList, watsLogo, fetchConfigVO, fetchMetadataListVO,
						customerDetails);
			} else if (!("Passed_Report.pdf".equalsIgnoreCase(pdffileName)
					|| "Failed_Report.pdf".equalsIgnoreCase(pdffileName)
					|| "Detailed_Report.pdf".equalsIgnoreCase(pdffileName))) {
				generateScriptLvlPDF(document, fetchConfigVO.getStarttime(), fetchConfigVO.getEndtime(), watsLogo,
						fetchMetadataListVO, fetchConfigVO, fileNameList, customerDetails);
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
			CustomerProjectDto customerDetails) throws IOException, com.itextpdf.text.DocumentException {

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a");
		Font font23 = FontFactory.getFont(ARIAL, 23);
		Font fnt12 = FontFactory.getFont(ARIAL, 12);
		String report = EXECUTION_REPORT;
		String starttime1 = dateFormat.format(startTime);
		String endtime1 = dateFormat.format(endTime);
		long diff = DateUtils.findTimeDifference(startTime.toString(), endTime.toString());
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
		String errorMsgs = fetchConfigVO.getErrormessage();
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
		for (ScriptDetailsDto metaDataVO : fetchMetadataListVO) {
			String fileName = metaDataVO.getSeqNum() + "_" + metaDataVO.getLineNumber() + "_"
					+ metaDataVO.getScenarioName() + "_" + metaDataVO.getScriptNumber() + "_"
					+ customerDetails.getTestSetName() + "_" + metaDataVO.getLineNumber();
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
				Image img = Image.getInstance(fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + customerName + "/"
						+ testRunName1 + "/" + image);

				Rectangle pageSize = new Rectangle(img.getPlainWidth(), img.getPlainHeight() + 100);
				String status = image.split("_")[6].split("\\.")[0];
				String scenario = image.split("_")[2];
				String steps = image.split("_")[5];

				String stepDescription = metaDataVO.getTestRunParamDesc();
				String inputParam = metaDataVO.getInputParameter();
				String inputValue = metaDataVO.getInputValue();
				document.setPageSize(pageSize);
				document.newPage();
				String s = "Status: " + status;
				String scenarios = SCENARIO_NAME + " :" + scenario;
				watsLogo.scalePercent(65, 65);
				watsLogo.setAlignment(Image.ALIGN_RIGHT);
				document.add(watsLogo);
				document.add(new Paragraph(s, fnt12));
				document.add(new Paragraph(scenarios, fnt12));
				String step = status.equals(FAILED) ? "Failed at Line Number:" + "" + steps : "Step No :" + "" + steps;
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
		for (String consts : CONST) {
			insertCell(table, consts, Element.ALIGN_CENTER, 1, font23);
		}
		PdfPCell[] cells1 = table.getRow(0).getCells();
		for (int k = 0; k < cells1.length; k++) {
			cells1[k].setBackgroundColor(new BaseColor(161, 190, 212));
		}
		String[] strArr = { "Status", df1.format(passCount), df2.format(pass) + "%" };
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
		for (String str : CONST) {
			insertCell(table, str, Element.ALIGN_CENTER, 1, font23);
		}
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
		for (String consts : CONST) {
			insertCell(table, consts, Element.ALIGN_CENTER, 1, font23);
		}
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

		JFreeChart chart = ChartFactory.createPieChart("", dataSet, true, true, false);
		Color c1 = Color.GREEN;
		Color c = Color.RED;
		Color gray = Color.GRAY;

		LegendTitle legend = chart.getLegend();
		PiePlot piePlot = (PiePlot) chart.getPlot();
		piePlot.setSectionPaint("Pass", c1);
		piePlot.setSectionPaint("Fail", c);
		piePlot.setSectionPaint("In Complete", gray);

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
		java.awt.Font f2 = new java.awt.Font("", java.awt.Font.PLAIN, 22);
		piePlot.setLabelFont(f2);
		PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator("{2}", new DecimalFormat("0"),
				new DecimalFormat("0%"));
		piePlot.setLabelGenerator(gen);
		legend.setPosition(RectangleEdge.RIGHT);
		legend.setVerticalAlignment(VerticalAlignment.CENTER);
		piePlot.setInsets(new RectangleInsets(0.0, 5.0, 5.0, 5.0));
		legend.setFrame(BlockBorder.NONE);
		legend.setFrame(new LineBorder(Color.white, new BasicStroke(20f), new RectangleInsets(1.0, 1.0, 1.0, 1.0)));

		java.awt.Font pass1 = new java.awt.Font("", Font.NORMAL, 22);
		legend.setItemFont(pass1);
		PdfContentByte contentByte = writer.getDirectContent();
		PdfTemplate template = contentByte.createTemplate(1000, 900);
		@SuppressWarnings("deprecation")
		Graphics2D graphics2d = template.createGraphics(700, 400, new DefaultFontMapper());
		Rectangle2D rectangle2d = new Rectangle2D.Double(0, 0, 600, 400);
		chart.draw(graphics2d, rectangle2d);
		graphics2d.dispose();
		contentByte.addTemplate(template, 400, 100);
	}

	public void addRestOfPagesToPDF(Document document, List<String> fileNameList, Image watsLogo,
			FetchConfigVO fetchConfigVO, List<ScriptDetailsDto> fetchMetadataListVO, CustomerProjectDto customerDetails)
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
					if (image1.startsWith(sndo + "_") && seqNumMap.get(sndo).equals("Fail")) {
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
				Anchor click1 = new Anchor(String.valueOf("(Failed)"), bf14);
				click1.setReference("#" + entry1.getValue());
				Paragraph pr = new Paragraph();
				Anchor ca1 = new Anchor(entry1.getKey(), bf15);
				ca1.setReference("#" + entry1.getKey());
				String compare = entry1.getValue();
				if (!compare.equals("null")) {
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
		for (ScriptDetailsDto metaDataVO : fetchMetadataListVO) {
			String fileName = metaDataVO.getSeqNum() + "_" + metaDataVO.getLineNumber() + "_"
					+ metaDataVO.getScenarioName() + "_" + metaDataVO.getScriptNumber() + "_"
					+ customerDetails.getTestSetName() + "_" + metaDataVO.getLineNumber();
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
				Image img = Image.getInstance(fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + customerName + "/"
						+ testRunName1 + "/" + image);
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
							insertCell(table2, CONST[0], Element.ALIGN_LEFT, 1, font23);
							insertCell(table2, FAILED, Element.ALIGN_LEFT, 1, font23);
						} else {
							insertCell(table2, CONST[0], Element.ALIGN_LEFT, 1, font23);
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
				watsLogo.scalePercent(65, 68);
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
				String step = STEP_NO + "" + reason;

				String stepDescription = metaDataVO.getTestRunParamDesc();

				String inputParam = metaDataVO.getInputParameter();

				String inputValue = metaDataVO.getInputValue();

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
			}

		}

	}

	public void insertCell(PdfPTable table, String text, int align, int colspan, Font font) {

		// create a new cell with the specified Text and Font
		PdfPCell cell = new PdfPCell(new Paragraph(text.trim(), font));
		cell.setBorder(PdfPCell.NO_BORDER);
		// set the cell alignment

		cell.setUseVariableBorders(true);
		if (text.equalsIgnoreCase("Status")) {
			cell.setBorderWidthLeft(0.3f);
			cell.setBorderColorLeft(new BaseColor(230, 225, 225));
			cell.setBorderWidthTop(0.3f);
			cell.setBorderColorTop(new BaseColor(230, 225, 225));
			cell.setBorderWidthRight(0.3f);
			cell.setBorderColorRight(new BaseColor(230, 225, 225));
			cell.setBorderWidthBottom(0.3f);
			cell.setBorderColorBottom(new BaseColor(230, 225, 225));
		} else if (text.equalsIgnoreCase("Total")) {
			cell.setBorderWidthTop(0.3f);
			cell.setBorderColorTop(new BaseColor(230, 225, 225));
			cell.setBorderWidthRight(0.3f);
			cell.setBorderColorRight(new BaseColor(230, 225, 225));
			cell.setBorderWidthBottom(0.3f);
			cell.setBorderColorBottom(new BaseColor(230, 225, 225));
		} else if (text.equalsIgnoreCase("Percentage")) {
			cell.setBorderWidthTop(0.3f);
			cell.setBorderColorTop(new BaseColor(230, 225, 225));
			cell.setBorderWidthRight(0.3f);
			cell.setBorderColorRight(new BaseColor(230, 225, 225));
			cell.setBorderWidthBottom(0.3f);
			cell.setBorderColorBottom(new BaseColor(230, 225, 225));
		} else if (text.equalsIgnoreCase("Passed") || text.equalsIgnoreCase("Failed")) {
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

	public void deleteOldScreenshotForScriptFrmObjStore(ScriptDetailsDto testSetLine, CustomerProjectDto customerDetails) {
		ConfigFileReader.ConfigFile configFile = null;
		try {
			configFile = ConfigFileReader.parse(new ClassPathResource("oci/config").getInputStream(), ociConfigName);
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
						.delimiter("/").build();

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
					}
				} else {
					logger.info("Screenshot is not present");
				}
			}
		} catch (Exception e1) {
			Log.error("Not able to connect with object store");
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
					.parse(new ClassPathResource("oci/config").getInputStream(), ociConfigName);
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

}
