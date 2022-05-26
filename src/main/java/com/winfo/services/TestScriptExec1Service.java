package com.winfo.services;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

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
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
import org.apache.commons.lang3.StringUtils;
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
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
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
import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.GetObjectRequest;
import com.oracle.bmc.objectstorage.requests.ListObjectsRequest;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.objectstorage.responses.GetObjectResponse;
import com.oracle.bmc.objectstorage.responses.ListObjectsResponse;
import com.oracle.bmc.objectstorage.responses.PutObjectResponse;
import com.winfo.exception.WatsEBSCustomException;
import com.winfo.model.TestSetLine;
import com.winfo.model.TestSetScriptParam;
import com.winfo.scripts.DHSeleniumKeyWords;
import com.winfo.utils.Constants;
import com.winfo.utils.Constants.BOOLEAN_STATUS;
import com.winfo.utils.Constants.TEST_SET_LINE_ID_STATUS;
import com.winfo.vo.MessageQueueDto;
import com.winfo.vo.ResponseDto;

@Service
public class TestScriptExec1Service {

	public final Logger logger = LogManager.getLogger(TestScriptExec1Service.class);
	public static final String topic = "test-script-run";
	public static final String FORWARD_SLASH = "/";
	public static final String BACK_SLASH = "\\";
	public static final String SPLIT = "@";
	private static final String[] CONST = { "Status", "Total", "Percentage" };
	private static final String PASSED = "Passed";
	private static final String FAILED = "Failed";
	private static final String ARIAL = "Arial";
	@Value("${configvO.watslogo}")
	private String watslogo;
	@Value("${chrome.driver.path}")
	private String chromeDriverPath;
	@Value("${dll.path}")
	private String dllPath;
	@Value("${oci.config.path}")
	private String ociConfigPath;
	@Value("${oci.config.name}")
	private String ociConfigName;
	@Value("${oci.bucket.name}")
	private String ociBucketName;
	@Value("${oci.namespace}")
	private String ociNamespace;
	@Autowired
	ErrorMessagesHandler errorMessagesHandler;
	@Value("${configvO.watsvediologo}")
	private String watsvediologo;
	@Value("${configvO.whiteimage}")
	private String whiteimage;
	@Value("${url.update.script.param}")
	private String scriptParamStatusUpdateUrl;
	@Value("${url.get.copied.value}")
	private String copiedValueUrl;
	// @Value("${pyjab.template.name}")
	private String templateName;

	@Autowired
	DataBaseEntry dataBaseEntry;
	@Autowired
	TestCaseDataService dataService;
	@Autowired
	DHSeleniumKeyWords eBSSeleniumKeyWords;
	@Autowired
	LimitScriptExecutionService limitScriptExecutionService;

	public ResponseDto generateTestScriptLineIdReports(MessageQueueDto msgQueueDto) {
		try {
			Boolean scriptStatus = dataBaseEntry.checkAllStepsStatusForAScript(msgQueueDto.getTestSetLineId());
			if (scriptStatus == null) {
				if (msgQueueDto.isManualTrigger()) {
					return new ResponseDto(200, Constants.WARNING, "Script Run In Progress");
				} else {
					scriptStatus = false;
				}
			}
			msgQueueDto.setSuccess(scriptStatus);
			TestSetLine testSetLine = dataBaseEntry.getTestSetLinesRecord(msgQueueDto.getTestSetId(),
					msgQueueDto.getTestSetLineId());
			msgQueueDto.setStartDate(testSetLine.getExecutionStartTime());
			FetchConfigVO fetchConfigVO = fetchConfigVODetails(msgQueueDto.getTestSetId());

			fetchConfigVO.setWINDOWS_SCREENSHOT_LOCATION(
					System.getProperty(Constants.SYS_USER_HOME_PATH) + Constants.SCREENSHOT);
			fetchConfigVO.setWINDOWS_PDF_LOCATION(System.getProperty(Constants.SYS_USER_HOME_PATH) + Constants.PDF);

			List<FetchMetadataVO> fetchMetadataListVO = dataBaseEntry.getMetaDataVOList(msgQueueDto.getTestSetId(),
					msgQueueDto.getTestSetLineId(), false, false);
			msgQueueDto.setSuccess(scriptStatus);

			String screenShotFolderPath = (fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION()
					+ fetchMetadataListVO.get(0).getCustomer_name() + BACK_SLASH
					+ fetchMetadataListVO.get(0).getTest_run_name() + BACK_SLASH);
			String objectStore = fetchConfigVO.getScreenshot_path();
			String[] arrOfStr = objectStore.split(FORWARD_SLASH, 5);
			StringBuilder objectStoreScreenShotPath = new StringBuilder(arrOfStr[3]);
			for (int i = 4; i < arrOfStr.length; i++) {
				objectStoreScreenShotPath.append(FORWARD_SLASH + arrOfStr[i]);
			}

			String scriptId = fetchMetadataListVO.get(0).getScript_id();
			String passurl = fetchConfigVO.getImg_url() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
					+ fetchMetadataListVO.get(0).getTest_run_name() + "/" + "Passed_Report.pdf" + "AAAparent="
					+ fetchConfigVO.getImg_url();
			String failurl = fetchConfigVO.getImg_url() + fetchMetadataListVO.get(0).getCustomer_name() + "b/"
					+ fetchMetadataListVO.get(0).getTest_run_name() + "/" + "Failed_Report.pdf" + "AAAparent="
					+ fetchConfigVO.getImg_url();
			String detailurl = fetchConfigVO.getImg_url() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
					+ fetchMetadataListVO.get(0).getTest_run_name() + "/" + "Detailed_Report.pdf" + "AAAparent="
					+ fetchConfigVO.getImg_url();
			String scripturl = fetchConfigVO.getImg_url() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
					+ fetchMetadataListVO.get(0).getTest_run_name() + "/" + fetchMetadataListVO.get(0).getSeq_num()
					+ "_" + fetchMetadataListVO.get(0).getScript_number() + ".pdf" + "AAAparent="
					+ fetchConfigVO.getImg_url();

			fetchConfigVO.setStarttime(msgQueueDto.getStartDate());
			fetchConfigVO.setStarttime1(msgQueueDto.getStartDate());
			deleteScreenshotsFromWindows(fetchConfigVO, fetchMetadataListVO);
			downloadScreenshotsFromObjectStore(screenShotFolderPath, fetchMetadataListVO.get(0).getCustomer_name(),
					fetchMetadataListVO.get(0).getTest_run_name(), objectStoreScreenShotPath.toString(),
					fetchMetadataListVO.get(0).getSeq_num() + "_");
			FetchScriptVO post = new FetchScriptVO(msgQueueDto.getTestSetId(), scriptId, msgQueueDto.getTestSetLineId(),
					passurl, failurl, detailurl, scripturl);
			Date enddate = testSetLine.getExecutionEndTime() != null ? testSetLine.getExecutionEndTime() : new Date();
			String pdfName = null;
			if (msgQueueDto.isSuccess()) {
				pdfName = fetchMetadataListVO.get(0).getSeq_num() + "_" + fetchMetadataListVO.get(0).getScript_number()
						+ ".pdf";
				post.setP_status("Pass");
				fetchConfigVO.setEndtime(enddate);
				limitScriptExecutionService.updateFaileScriptscount(msgQueueDto.getTestSetLineId(),
						msgQueueDto.getTestSetId());
			} else {
				fetchConfigVO.setErrormessage("EBS Execution Failed");
				post.setP_status("Fail");
				fetchConfigVO.setEndtime(enddate);
//				int failedScriptRunCount = limitScriptExecutionService
//						.getFailedScriptRunCount(msgQueueDto.getTestSetLineId(), msgQueueDto.getTestSetId());
				int failedScriptRunCount = limitScriptExecutionService
						.getFailScriptRunCount(msgQueueDto.getTestSetLineId(), msgQueueDto.getTestSetId());
				fetchConfigVO.setStatus1("Fail");
				if (!msgQueueDto.isManualTrigger()) {
					failedScriptRunCount = failedScriptRunCount + 1;
					limitScriptExecutionService.updateFailScriptRunCount(failedScriptRunCount,
							msgQueueDto.getTestSetLineId(), msgQueueDto.getTestSetId());
				}
				pdfName = fetchMetadataListVO.get(0).getSeq_num() + "_" + fetchMetadataListVO.get(0).getScript_number()
						+ "_RUN" + failedScriptRunCount + ".pdf";

			}
			createPdf(fetchMetadataListVO, fetchConfigVO, pdfName, msgQueueDto.getStartDate(), enddate);
//			dataBaseEntry.updateSetLinesStatusAndTestSetPath(post, fetchConfigVO);
			dataService.updateTestCaseStatus(post, msgQueueDto.getTestSetId(), fetchConfigVO);
			limitScriptExecutionService.insertTestRunScriptData(fetchConfigVO, fetchMetadataListVO,
					fetchMetadataListVO.get(0).getScript_id(), fetchMetadataListVO.get(0).getScript_number(),
					fetchConfigVO.getStatus1(), new Date(), enddate);

			// final reports generation
			if (!msgQueueDto.isManualTrigger()) {
				String pdfGenerationEnabled = dataBaseEntry
						.pdfGenerationEnabled(Long.valueOf(msgQueueDto.getTestSetId()));
				if (BOOLEAN_STATUS.TRUE.getLabel().equalsIgnoreCase(pdfGenerationEnabled)) {
					boolean runFinalPdf = dataBaseEntry
							.checkIfAllTestSetLinesCompleted(Long.valueOf(msgQueueDto.getTestSetId()), true);
					if (runFinalPdf) {
						Date date1 = new Date();
						fetchConfigVO.setEndtime(date1);
						dataBaseEntry.updatePdfGenerationEnableStatus(msgQueueDto.getTestSetId(),
								BOOLEAN_STATUS.FALSE.getLabel());
						testRunPdfGeneration(msgQueueDto.getTestSetId(), fetchConfigVO, date1);
					}
				}
			}

		} catch (Exception e) {
			throw new WatsEBSCustomException(500, "Exception occured while generating the pdf", e);
		}
		return new ResponseDto(200, Constants.ERROR, "Fail");
	}

	private void testRunPdfGeneration(String testSetId, FetchConfigVO fetchConfigVO, Date endDate) {
		List<FetchMetadataVO> fetchMetadataListVOFinal = dataBaseEntry.getMetaDataVOList(testSetId, null, true, false);
		dataBaseEntry.setPassAndFailScriptCount(testSetId, fetchConfigVO);
		fetchConfigVO.setEndtime(endDate);
		try {
			createPdf(fetchMetadataListVOFinal, fetchConfigVO, "Passed_Report.pdf", null, null);
			createPdf(fetchMetadataListVOFinal, fetchConfigVO, "Failed_Report.pdf", null, null);
			createPdf(fetchMetadataListVOFinal, fetchConfigVO, "Detailed_Report.pdf", null, null);
		} catch (com.itextpdf.text.DocumentException e) {
			logger.error("Unable to create TestLvlPDF" + e);
		}
	}

	public ResponseDto generateTestRunPdf(String testSetId) {
		boolean runFinalPdf = dataBaseEntry.checkIfAllTestSetLinesCompleted(Long.valueOf(testSetId), null);
		if (runFinalPdf) {
			try {
				FetchConfigVO fetchConfigVO = fetchConfigVO(testSetId);
				Date startDate = dataBaseEntry.findMinExecutionStartDate(Long.valueOf(testSetId));
				Date endDate = dataBaseEntry.findMaxExecutionEndDate(Long.valueOf(testSetId));
//				fetchConfigVO.setWINDOWS_SCREENSHOT_LOCATION(
//						System.getProperty(Constants.SYS_USER_HOME_PATH) + Constants.SCREENSHOT);
//				fetchConfigVO.setWINDOWS_PDF_LOCATION(System.getProperty(Constants.SYS_USER_HOME_PATH) + Constants.PDF);
				fetchConfigVO.setStarttime(startDate);
				fetchConfigVO.setStarttime1(startDate);
				fetchConfigVO.setEndtime(endDate);
				testRunPdfGeneration(testSetId, fetchConfigVO, endDate);
				return new ResponseDto(200, Constants.SUCCESS, null);
			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseDto(500, Constants.ERROR, e.getMessage());
			}
		} else {
			return new ResponseDto(200, Constants.WARNING, "Cannot generate PDF. Scripts are In-Progress or In-Queue");
		}
	}

	public FetchConfigVO fetchConfigVODetails(String testSetId) {
		List<Object[]> configurations = dataBaseEntry.getConfigurationDetails(testSetId);
		Map<String, String> mapConfig = new HashMap<>();
		String value = null;
		for (Object[] e : configurations) {
			if (e[1] != null && StringUtils.isNotBlank(e[1].toString())) {
				value = e[1].toString();
			} else if (e[2] != null && StringUtils.isNotBlank(e[2].toString())) {
				value = e[2].toString();
			} else {
				value = null;
			}
			mapConfig.put(e[0].toString(), value);
		}

		JSONObject jsno = new JSONObject(mapConfig);
		Gson g = new Gson();
		return g.fromJson(jsno.toString(), FetchConfigVO.class);
	}

	public void deleteScreenshotsFromWindows(FetchConfigVO fetchConfigVO, List<FetchMetadataVO> fetchMetadataListVO) {
		File folder1 = new File(
				fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + fetchMetadataListVO.get(0).getCustomer_name()
						+ BACK_SLASH + fetchMetadataListVO.get(0).getTest_run_name());
		if (!folder1.exists()) {
			try {
				folder1.mkdirs();
			} catch (SecurityException se) {
				throw new WatsEBSCustomException(500, "Exception occured while creating directory", se);
			}
		} else {

			File folder = new File(
					fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + fetchMetadataListVO.get(0).getCustomer_name()
							+ BACK_SLASH + fetchMetadataListVO.get(0).getTest_run_name() + BACK_SLASH);
			if (folder.exists()) {
				File[] listOfFiles = folder.listFiles();

				for (File file : Arrays.asList(listOfFiles)) {

//					String seqNum = String.valueOf(file.getName().substring(0, file.getName().indexOf('_')));
//
//					String seqnum1 = fetchMetadataListVO.get(0).getSeq_num();
//					if (seqNum.equalsIgnoreCase(seqnum1)) {
					Path imagesPath = Paths.get(file.getPath());
					try {
						Files.delete(imagesPath);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void downloadScreenshotsFromObjectStore(String screenshotPath, String customerName, String TestRunName,
			String objectStoreScreenShotPath, String seqNum) {
		ConfigFileReader.ConfigFile configFile = null;
		List<String> objNames = null;

		System.out.println(objectStoreScreenShotPath);
		try {
			configFile = ConfigFileReader.parse(new ClassPathResource("oci/config").getInputStream(), ociConfigName);
		} catch (IOException e) {
			throw new WatsEBSCustomException(500, "Exception occured while connecting to oci/config path", e);
		}

		final AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);

		try (ObjectStorage client = new ObjectStorageClient(provider);) {

//		String objectStoreScreenshotPath = objectStoreScreenShotPath + customerName + FORWARD_SLASH + TestRunName
//				+ FORWARD_SLASH + seqNum;

			String objectStoreScreenshotPath = objectStoreScreenShotPath + customerName + FORWARD_SLASH + TestRunName
					+ FORWARD_SLASH;

			ListObjectsRequest listObjectsRequest = ListObjectsRequest.builder().namespaceName(ociNamespace)
					.bucketName(ociBucketName).prefix(objectStoreScreenshotPath).delimiter("/").build();

			/* Send request to the Client */
			ListObjectsResponse response = client.listObjects(listObjectsRequest);

			objNames = response.getListObjects().getObjects().stream().map((objSummary) -> objSummary.getName())
					.collect(Collectors.toList());
			logger.info(objNames.size());
			ListIterator<String> listIt = objNames.listIterator();
			String imagePath = screenshotPath;
			while (listIt.hasNext()) {
				String objectName = listIt.next();
				GetObjectResponse getResponse = client.getObject(GetObjectRequest.builder().namespaceName(ociNamespace)
						.bucketName(ociBucketName).objectName(objectName).build());

				String imageName = objectName.substring(objectName.lastIndexOf("/") + 1, objectName.length());
				File file = new File(imagePath + imageName);
				if (!file.exists()) {
					try (final InputStream stream = getResponse.getInputStream();
							// final OutputStream outputStream = new FileOutputStream(imagePath + imageName)

							final OutputStream outputStream = Files.newOutputStream(file.toPath(), CREATE_NEW)) {
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
		} catch (Exception e) {
			throw new WatsEBSCustomException(500, "Exception occured while closing Object stroage path", e);
		}

	}

	public void generateDetailsPDF(Document document, Image watsLogo, int passCount, int failCount, int others,
			PdfWriter writer) throws DocumentException {
		String start = "Execution Summary";
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
			dataSet.setValue("Fail", fail);
		} else if (failCount == 0 && others == 0) {
			dataSet.setValue("Pass", pass);
		} else if (passCount == 0 && failCount == 0) {
			dataSet.setValue("In Complete", other);
		} else if (passCount != 0 && others != 0 && failCount == 0) {
			dataSet.setValue("Pass", pass);
			dataSet.setValue("In Complete", other);
		} else if (passCount == 0 && others != 0 && failCount != 0) {
			dataSet.setValue("Fail", fail);
			dataSet.setValue("In Complete", other);
		} else if (passCount != 0 && others == 0 && failCount != 0) {
			dataSet.setValue("Pass", pass);
			dataSet.setValue("Fail", fail);
		} else if (passCount != 0 && others != 0 && failCount != 0) {
			dataSet.setValue("Pass", pass);
			dataSet.setValue("Fail", fail);
			dataSet.setValue("In Complete", other);
		}
		PdfPTable table = new PdfPTable(3);
		table.setWidths(new int[] { 1, 1, 1 });
		table.setWidthPercentage(100f);
		for (String consts : CONST) {
			eBSSeleniumKeyWords.insertCell(table, consts, Element.ALIGN_CENTER, 1, font23);
		}
		PdfPCell[] cells1 = table.getRow(0).getCells();
		for (int k = 0; k < cells1.length; k++) {
			cells1[k].setBackgroundColor(new BaseColor(161, 190, 212));
		}
		String[] strArr = { PASSED, df1.format(passCount), df2.format(pass) + "%", FAILED, df1.format(failCount),
				df2.format(fail) + "%" };

		for (String str : strArr) {
			eBSSeleniumKeyWords.insertCell(table, str, Element.ALIGN_CENTER, 1, font23);
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

	public void generateScriptLvlPDF(Document document, Date startTime, Date endTime, Image watsLogo,
			List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO, List<String> fileNameList)
			throws DocumentException, IOException {

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:aa");
		Font font23 = FontFactory.getFont(ARIAL, 23);
		Font fnt12 = FontFactory.getFont(ARIAL, 12);
		String report = "Execution Report";
		String starttime1 = dateFormat.format(startTime);
		String endtime1 = dateFormat.format(endTime);
		long diff = endTime.getTime() - startTime.getTime();
		long diffSeconds = diff / 1000 % 60;
		long diffMinutes = diff / (60 * 1000) % 60;
		long diffHours = diff / (60 * 60 * 1000);
		String scriptNumber2 = fetchMetadataListVO.get(0).getScenario_name();
		String scenario1 = fetchConfigVO.getStatus1();
		String executionTime = diffHours + ":" + diffMinutes + ":" + diffSeconds;
		String tr = "Test Run Name";
		String sn = "Script Number";
		String sn1 = "Scenario name";
		String scenarios1 = "Status ";
		String errorMsg = "ErrorMessage";
		String eb = "Executed By";
		String st = "Start Time";
		String et = "End Time";
		String ex = "Execution Time";
		String testRunName1 = fetchMetadataListVO.get(0).getTest_run_name();
		String scriptNumber = fetchMetadataListVO.get(0).getScript_number();
		String executedBy = fetchMetadataListVO.get(0).getExecuted_by();
		String customerName = fetchMetadataListVO.get(0).getCustomer_name();
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
			eBSSeleniumKeyWords.insertCell(table1, str, Element.ALIGN_LEFT, 1, font23);
		}
		if (errorMsgs != null) {
			eBSSeleniumKeyWords.insertCell(table1, errorMsg, Element.ALIGN_LEFT, 1, font23);
			eBSSeleniumKeyWords.insertCell(table1, errorMsgs, Element.ALIGN_LEFT, 1, font23);
		}
		for (String str : strArr2) {
			eBSSeleniumKeyWords.insertCell(table1, str, Element.ALIGN_LEFT, 1, font23);
		}

		document.add(table1);
		document.newPage();
		// added step DEsc, Input PAram ,Input val in pdf
		Map<String, TestSetScriptParam> map = dataBaseEntry
				.getTestScriptMap(fetchMetadataListVO.get(0).getTest_set_line_id());
		int i = 0;
		for (String image : fileNameList) {
			i++;
			Image img = Image.getInstance(
					fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + customerName + "/" + testRunName1 + "/" + image);
			
			Rectangle pageSize = new Rectangle(img.getPlainWidth(), img.getPlainHeight() + 100);

			String status = image.split("_")[6].split("\\.")[0];
			String scenario = image.split("_")[2];
			String steps = image.split("_")[5];

			String stepDescription = map.get(steps).getTestRunParamDesc();
			String inputParam = map.get(steps).getInputParameter();
			String inputValue = map.get(steps).getInputValue();
			document.setPageSize(pageSize);
			document.newPage();

			String s = "Status:" + " " + status;
			String scenarios = "Scenario Name :" + "" + scenario;
			watsLogo.scalePercent(65, 65);
			watsLogo.setAlignment(Image.ALIGN_RIGHT);
			document.add(watsLogo);
			document.add(new Paragraph(s, fnt12));
			document.add(new Paragraph(scenarios, fnt12));
			String step = status.equals("Failed") ? "Failed at Line Number:" + "" + steps : "Step No :" + "" + steps;
			String failMsg = status.equals("Failed") ? "Failed Message:" + "" + fetchConfigVO.getErrormessage() : null;
			document.add(new Paragraph(step, fnt12));
			if (failMsg != null) {
				document.add(new Paragraph(failMsg, fnt12));
			}
			if (stepDescription != null) {
				document.add(new Paragraph("Step Description: " + stepDescription, fnt12));
			}
			if (inputParam != null && inputValue != null) {
				document.add(new Paragraph("Test Parameter: " + inputParam, fnt12));
				document.add(new Paragraph("Test Value: " + inputValue, fnt12));
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

	public String findExecutionTimeForScript(String testSetId, String pdffileName, Date tStarttime, Date tendTime,
			long tdiff) {

		Map<Date, Long> timeslist = limitScriptExecutionService.getStarttimeandExecutiontime(testSetId);
		String startTime = null;
		String executionTime = null;
		Timestamp startTimestamp = new Timestamp(tStarttime.getTime());
		Timestamp endTimestamp = new Timestamp(tendTime.getTime());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:aa");
		if (timeslist.size() == 0) {
			startTime = dateFormat.format(tStarttime);
			long tDiffSeconds = tdiff / 1000 % 60;
			long tDiffMinutes = tdiff / (60 * 1000) % 60;
			long tDiffHours = tdiff / (60 * 60 * 1000);
			executionTime = tDiffHours + ":" + tDiffMinutes + ":" + tDiffSeconds;
			if ("Detailed_Report.pdf".equalsIgnoreCase(pdffileName)) {
				limitScriptExecutionService.updateTestrunTimes(startTimestamp, endTimestamp, tdiff, testSetId);
			}
		} else {
			for (Entry<Date, Long> entryMap : timeslist.entrySet()) {
				startTime = dateFormat.format(entryMap.getKey());
				long totalTime = tdiff + entryMap.getValue();
				long tDiffSeconds = totalTime / 1000 % 60;
				long tDiffMinutes = totalTime / (60 * 1000) % 60;
				long tDiffHours = totalTime / (60 * 60 * 1000);
				executionTime = tDiffHours + ":" + tDiffMinutes + ":" + tDiffSeconds;
				if ("Detailed_Report.pdf".equalsIgnoreCase(pdffileName)) {
					limitScriptExecutionService.updateTestrunTimes1(endTimestamp, totalTime, testSetId);
				}
			}
		}
		return startTime + "_" + executionTime;
	}

	private void createDir(String path) {
		File folder1 = new File(path);
		if (!folder1.exists()) {
			System.out.println("creating directory: " + folder1.getName());
			try {
				folder1.mkdirs();
			} catch (SecurityException se) {
				se.printStackTrace();
			}
		} else {
			System.out.println("Folder exist");
		}
	}

	public void generatePassPDF(Document document, int passCount, int failCount) throws DocumentException {
		Font font23 = FontFactory.getFont(ARIAL, 23);
		String start = "Execution Summary";
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
			eBSSeleniumKeyWords.insertCell(table, consts, Element.ALIGN_CENTER, 1, font23);
		}
		PdfPCell[] cells1 = table.getRow(0).getCells();
		for (int k = 0; k < cells1.length; k++) {
			cells1[k].setBackgroundColor(new BaseColor(161, 190, 212));
		}
		String[] strArr = { "Status", df1.format(passCount), df2.format(pass) + "%" };
		for (String str : strArr) {
			eBSSeleniumKeyWords.insertCell(table, str, Element.ALIGN_CENTER, 1, font23);
		}
		document.setMargins(20, 20, 20, 20);
		document.add(table);

	}

	public void generateFailedPDF(Document document, int passcount, int failcount) throws DocumentException {
		Font font23 = FontFactory.getFont(ARIAL, 23);
		String start = "Execution Summary";
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
			eBSSeleniumKeyWords.insertCell(table, str, Element.ALIGN_CENTER, 1, font23);
		}
		PdfPCell[] cells1 = table.getRow(0).getCells();
		for (int k = 0; k < cells1.length; k++) {
			cells1[k].setBackgroundColor(new BaseColor(161, 190, 212));
		}
		String[] strArr = { FAILED, df1.format(failcount), df2.format(fail) + "%" };
		for (String str : strArr) {
			eBSSeleniumKeyWords.insertCell(table, str, Element.ALIGN_CENTER, 1, font23);
		}
		document.setMargins(20, 20, 20, 20);
		document.add(table);
	}

	public void addRestOfPagesToPDF(Document document, List<String> fileNameList, Image watsLogo,
			FetchConfigVO fetchConfigVO, List<FetchMetadataVO> fetchMetadataListVO)
			throws DocumentException, IOException, ClassNotFoundException, SQLException {
		int k = 0;
		int l = 0;
		String sno1 = "";
		Map<Integer, Map<String, String>> toc = new TreeMap<>();
		String customerName = fetchMetadataListVO.get(0).getCustomer_name();
		String testRunName1 = fetchMetadataListVO.get(0).getTest_run_name();
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
		for (String image : fileNameList) {
			i++;
			Image img = Image.getInstance(
					fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + customerName + "/" + testRunName1 + "/" + image);
			Rectangle pageSize = new Rectangle(img.getPlainWidth(), img.getPlainHeight() + 100);
			String sno = image.split("_")[0];
			String sNo = "Script Number";
			String scriptNumber1 = image.split("_")[3];
			String snm = "Scenario Name";
			String scriptName = image.split("_")[2];
			String testRunName = image.split("_")[4];
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
					eBSSeleniumKeyWords.insertCell(table2, str, Element.ALIGN_LEFT, 1, font23);
				}

				for (Entry<String, String> entry1 : toc.get(i).entrySet()) {
					String str = entry1.getValue();
					if (!str.equals("null")) {
						eBSSeleniumKeyWords.insertCell(table2, CONST[0], Element.ALIGN_LEFT, 1, font23);
						eBSSeleniumKeyWords.insertCell(table2, FAILED, Element.ALIGN_LEFT, 1, font23);
					} else {
						eBSSeleniumKeyWords.insertCell(table2, CONST[0], Element.ALIGN_LEFT, 1, font23);
						eBSSeleniumKeyWords.insertCell(table2, PASSED, Element.ALIGN_LEFT, 1, font23);
					}
				}

				document.add(table2);

			}
			if (sno != null) {
				sno1 = sno;
			}
			String status = image.split("_")[6].split("\\.")[0];
			String scenario = image.split("_")[2];

			String scenarios = "Scenario Name :" + "" + scenario;

			String sndo = image.split("_")[0];
			watsLogo.scalePercent(65, 68);
			Rectangle one1 = new Rectangle(1360, 1000);
			watsLogo.setAlignment(Image.ALIGN_RIGHT);
			if (image.startsWith(sndo + "_") && image.contains("Failed")) {
				document.setPageSize(one1);
				document.newPage();
			} else {

				document.setPageSize(pageSize);
				document.newPage();
			}
			document.add(watsLogo);
			document.add(new Paragraph(scenarios, fnt12));
			String reason = image.split("_")[5];
			String step = "Step No :" + "" + reason;
			String message = "Failed at Line Number:" + "" + reason;
			// new change-database to get error message
			String error = dataBaseEntry.getErrorMessage(sndo, scriptNumber1, testRunName);
			String errorMessage = "Failed Message:" + "" + error;

			Map<String, Map<String, TestSetScriptParam>> descriptionList = dataBaseEntry
					.getTestRunMap(fetchMetadataListVO.get(0).getTest_set_id());
			String stepDescription = descriptionList.get(sno).get(reason).getTestRunParamDesc();

			String inputParam = descriptionList.get(sno).get(reason).getInputParameter();

			String inputValue = descriptionList.get(sno).get(reason).getInputValue();

			Paragraph pr1 = new Paragraph();
			pr1.add("Status:");

			if (image.startsWith(sndo + "_") && image.contains("Failed")) {
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
					document.add(new Paragraph("Step Description :" + stepDescription, fnt12));
				}
				if (inputParam != null) {
					document.add(new Paragraph("Test Parameter :" + inputParam, fnt12));
					if (inputValue != null) {
						document.add(new Paragraph("Test Value :" + inputValue, fnt12));
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
					document.add(new Paragraph("Step Description: " + stepDescription, fnt12));
				}
				if (inputParam != null) {
					document.add(new Paragraph("Test Parameter: " + inputParam, fnt12));
					if (inputValue != null) {
						document.add(new Paragraph("Test Value: " + inputValue, fnt12));
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

	private void createPdf(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO, String pdffileName,
			Date starttime, Date endtime) throws com.itextpdf.text.DocumentException {
		try {
			String folder = (fetchConfigVO.getWINDOWS_PDF_LOCATION() + fetchMetadataListVO.get(0).getCustomer_name()
					+ BACK_SLASH + fetchMetadataListVO.get(0).getTest_run_name() + BACK_SLASH);
			String file = (folder + pdffileName);
			findPassAndFailCount(fetchConfigVO, fetchMetadataListVO.get(0).getTest_set_id());

			List<String> fileNameList = null;
			if ("Passed_Report.pdf".equalsIgnoreCase(pdffileName)) {
				fileNameList = eBSSeleniumKeyWords.getPassedPdfNew(fetchMetadataListVO, fetchConfigVO);
			} else if ("Failed_Report.pdf".equalsIgnoreCase(pdffileName)) {
				fileNameList = eBSSeleniumKeyWords.getFailedPdfNew(fetchMetadataListVO, fetchConfigVO);
			} else if ("Detailed_Report.pdf".equalsIgnoreCase(pdffileName)) {
				fileNameList = eBSSeleniumKeyWords.getDetailPdfNew(fetchMetadataListVO, fetchConfigVO);
			} else {
				fileNameList = eBSSeleniumKeyWords.getFileNameListNew(fetchMetadataListVO, fetchConfigVO);
			}
			String executedBy = fetchMetadataListVO.get(0).getExecuted_by();
			createDir(folder);
			Document document = new Document();
			String report = "Execution Report";
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
			fetchConfigVO.setStarttime1(new Date());
			Date tStarttime = fetchConfigVO.getStarttime1();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:aa");
			String tendtime1 = dateFormat.format(tendTime);
			long tdiff = tendTime.getTime() - tStarttime.getTime();
			String testRunName1 = fetchMetadataListVO.get(0).getTest_run_name();

			if ((!fileNameList.isEmpty()) && ("Passed_Report.pdf".equalsIgnoreCase(pdffileName)
					|| "Failed_Report.pdf".equalsIgnoreCase(pdffileName)
					|| "Detailed_Report.pdf".equalsIgnoreCase(pdffileName))) {
				int passcount = fetchConfigVO.getPasscount();
				int failcount = fetchConfigVO.getFailcount();
				int others = fetchConfigVO.getOtherCount();

				String[] startAndExecTime = findExecutionTimeForScript(fetchMetadataListVO.get(0).getTest_set_id(),
						pdffileName, tStarttime, tendTime, tdiff).split("_");
				String startTime = startAndExecTime[0];
				String executionTime = startAndExecTime[1];
				String endTime = tendtime1;
				String tr = "Test Run Name";
				String sn = "Executed By";
				String sn1 = "Start Time";
				String s1 = "End Time";
				String scenarios1 = "Execution Time";
				String[] testArr = { tr, testRunName1, sn, executedBy, sn1, startTime, s1, endTime, scenarios1,
						executionTime };
				document.add(watsLogo);
				document.add(new Paragraph(report, font23));
				document.add(Chunk.NEWLINE);
				PdfPTable table1 = new PdfPTable(2);
				table1.setWidths(new int[] { 1, 1 });
				table1.setWidthPercentage(100f);
				for (String text : testArr) {
					eBSSeleniumKeyWords.insertCell(table1, text, Element.ALIGN_LEFT, 1, font23);
				}
				document.add(table1);

				if ("Detailed_Report.pdf".equalsIgnoreCase(pdffileName)) {
					generateDetailsPDF(document, watsLogo, passcount, failcount, others, writer);
				} else if ("Passed_Report.pdf".equalsIgnoreCase(pdffileName)) {
					generatePassPDF(document, passcount, failcount);
				} else {
					generateFailedPDF(document, passcount, failcount);
				}
				addRestOfPagesToPDF(document, fileNameList, watsLogo, fetchConfigVO, fetchMetadataListVO);
			} else if (!("Passed_Report.pdf".equalsIgnoreCase(pdffileName)
					|| "Failed_Report.pdf".equalsIgnoreCase(pdffileName)
					|| "Detailed_Report.pdf".equalsIgnoreCase(pdffileName))) {
				generateScriptLvlPDF(document, starttime, endtime, watsLogo, fetchMetadataListVO, fetchConfigVO,
						fileNameList);
			}
			document.close();

		} catch (Exception e) {
			logger.info("Not able to Create pdf {}", e);
		}
		try {
			String destinationFilePath = (fetchMetadataListVO.get(0).getCustomer_name() + FORWARD_SLASH
					+ fetchMetadataListVO.get(0).getTest_run_name() + FORWARD_SLASH) + pdffileName;

			String sourceFilePath = (fetchConfigVO.getWINDOWS_PDF_LOCATION()
					+ fetchMetadataListVO.get(0).getCustomer_name() + BACK_SLASH
					+ fetchMetadataListVO.get(0).getTest_run_name() + BACK_SLASH) + pdffileName;
			uploadObjectToObjectStore(sourceFilePath, destinationFilePath);
		} catch (Exception e) {
			logger.info(e);
		}
	}

	public String uploadObjectToObjectStore(String sourceFile, String destinationFilePath) {

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
		} catch (Exception e) {
			throw new WatsEBSCustomException(500, "Exception occured while uploading pdf in Object Storage", e);
		}
	}

	public FetchConfigVO fetchConfigVO(String testSetId) {
		List<Object[]> configurations = dataBaseEntry.getConfigurationDetails(testSetId);
		Map<String, String> mapConfig = new HashMap<>();
		String value = null;
		for (Object[] e : configurations) {
			if (e[1] != null && StringUtils.isNotBlank(e[1].toString())) {
				value = e[1].toString();
			} else if (e[2] != null && StringUtils.isNotBlank(e[2].toString())) {
				value = e[2].toString();
			} else {
				value = null;
			}
			mapConfig.put(e[0].toString(), value);
		}

		JSONObject jsno = new JSONObject(mapConfig);
		Gson g = new Gson();
		return g.fromJson(jsno.toString(), FetchConfigVO.class);
	}
}
