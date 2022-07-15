//package com.winfo.interceptor;
//
//import java.awt.BasicStroke;
//import java.awt.Color;
//import java.awt.Graphics;
//import java.awt.Graphics2D;
//import java.awt.geom.Rectangle2D;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.sql.Timestamp;
//import java.text.DateFormat;
//import java.text.DecimalFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Date;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.TreeMap;
//import java.util.Map.Entry;
//
//import javax.imageio.ImageIO;
//
//import org.jfree.chart.ChartFactory;
//import org.jfree.chart.JFreeChart;
//import org.jfree.chart.block.BlockBorder;
//import org.jfree.chart.block.LineBorder;
//import org.jfree.chart.labels.PieSectionLabelGenerator;
//import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
//import org.jfree.chart.plot.PiePlot;
//import org.jfree.chart.title.LegendTitle;
//import org.jfree.data.general.DefaultPieDataset;
//import org.jfree.ui.RectangleEdge;
//import org.jfree.ui.RectangleInsets;
//import org.jfree.ui.VerticalAlignment;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.web.client.RestTemplate;
//
//import com.google.gson.Gson;
//import com.itextpdf.awt.DefaultFontMapper;
//import com.itextpdf.text.Anchor;
//import com.itextpdf.text.BaseColor;
//import com.itextpdf.text.Chunk;
//import com.itextpdf.text.Document;
//import com.itextpdf.text.Element;
//import com.itextpdf.text.Font;
//import com.itextpdf.text.FontFactory;
//import com.itextpdf.text.Image;
//import com.itextpdf.text.Paragraph;
//import com.itextpdf.text.Rectangle;
//import com.itextpdf.text.pdf.PdfContentByte;
//import com.itextpdf.text.pdf.PdfPCell;
//import com.itextpdf.text.pdf.PdfPTable;
//import com.itextpdf.text.pdf.PdfTemplate;
//import com.itextpdf.text.pdf.PdfWriter;
//import com.itextpdf.text.pdf.draw.DottedLineSeparator;
//import com.itextpdf.text.pdf.draw.VerticalPositionMark;
//import com.lowagie.text.DocumentException;
//import com.winfo.services.FetchConfigVO;
//import com.winfo.services.FetchMetadataVO;
//import com.winfo.utils.DateUtils;
//
//public class PdfGeneration {
//
//	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException, DocumentException, com.itextpdf.text.DocumentException {
//		// TODO Auto-generated method stub
//
//		String testrunId="";
//		String testRunName="";
//		String customerName="";
//		String pdffileName="";
//      PdfGeneration pdfGen= new PdfGeneration();
//      
//      FetchConfigVO fetchConfigVO =pdfGen.getFetchConfigVO(testrunId);
//      
//      List<FetchMetadataVO> fetchMetadataListVO=  pdfGen.getMetaDataVOList(testrunId);
//    //  pdfGen.getListOfImages(fetchConfigVO);
//      LinkedHashMap<String, List<FetchMetadataVO>> dependentScriptMap=new LinkedHashMap<String, List<FetchMetadataVO>>();
//		LinkedHashMap<String, List<FetchMetadataVO>> metaDataMap = pdfGen.prepareTestcasedata(fetchMetadataListVO,dependentScriptMap);
//		
//		for (Entry<String, List<FetchMetadataVO>> metaData : metaDataMap.entrySet()) {
//			Date startdate = new Date();
//			fetchConfigVO.setStarttime(startdate);
//			Date enddate = new Date();
//			fetchConfigVO.setEndtime(enddate);
//			if(metaData.getValue().get(0).getStatus().equalsIgnoreCase("Fail"))
//			{
//				int failedScriptRunCount = limitScriptExecutionService.getFailedScriptRunCount(metaData.getValue().get(0).getTest_set_line_id(),
//						testrunId);					if(failedScriptRunCount==1) {
//							pdfGen.createFailedPdf(fetchMetadataListVO, fetchConfigVO,
//									metaData.getValue().get(0).getSeq_num()  + "_" + metaData.getValue().get(0).getScript_number() +".pdf", startdate, enddate);
//
//						}else if(failedScriptRunCount==2) {
//							limitScriptExecutionService.renameFailedFile(fetchMetadataListVO, fetchConfigVO,
//									metaData.getValue().get(0).getSeq_num() + "_" + metaData.getValue().get(0).getScript_number()  +".pdf",failedScriptRunCount);
//							pdfGen.createFailedPdf(fetchMetadataListVO, fetchConfigVO,
//									metaData.getValue().get(0).getSeq_num()  + "_" + metaData.getValue().get(0).getScript_number()  + "_RUN" + failedScriptRunCount + ".pdf", startdate, enddate);
//
//						}else {
//							pdfGen.createFailedPdf(fetchMetadataListVO, fetchConfigVO,
//									metaData.getValue().get(0).getSeq_num()  + "_" + metaData.getValue().get(0).getScript_number()  + "_RUN" + failedScriptRunCount + ".pdf", startdate, enddate);
//						}
//			}
//			pdfGen.createPdf( metaData.getValue(),  fetchConfigVO,  pdffileName,
//					 startdate,  enddate);
//		}
//		for (Entry<String, List<FetchMetadataVO>> metaData : dependentScriptMap.entrySet()) {
//			Date startdate = new Date();
//			fetchConfigVO.setStarttime(startdate);
//			Date enddate = new Date();
//			fetchConfigVO.setEndtime(enddate);
//			if(metaData.getValue().get(0).getStatus().equalsIgnoreCase("Fail"))
//			{
//				int failedScriptRunCount = limitScriptExecutionService.getFailedScriptRunCount(metaData.getValue().get(0).getTest_set_line_id(),
//						testrunId);					if(failedScriptRunCount==1) {
//							pdfGen.createFailedPdf(fetchMetadataListVO, fetchConfigVO,
//									metaData.getValue().get(0).getSeq_num()  + "_" + metaData.getValue().get(0).getScript_number() +".pdf", startdate, enddate);
//
//						}else if(failedScriptRunCount==2) {
//							limitScriptExecutionService.renameFailedFile(fetchMetadataListVO, fetchConfigVO,
//									metaData.getValue().get(0).getSeq_num() + "_" + metaData.getValue().get(0).getScript_number()  +".pdf",failedScriptRunCount);
//							pdfGen.createFailedPdf(fetchMetadataListVO, fetchConfigVO,
//									metaData.getValue().get(0).getSeq_num()  + "_" + metaData.getValue().get(0).getScript_number()  + "_RUN" + failedScriptRunCount + ".pdf", startdate, enddate);
//
//						}else {
//							pdfGen.createFailedPdf(fetchMetadataListVO, fetchConfigVO,
//									metaData.getValue().get(0).getSeq_num()  + "_" + metaData.getValue().get(0).getScript_number()  + "_RUN" + failedScriptRunCount + ".pdf", startdate, enddate);
//						}
//			}
//			pdfGen.createPdf( metaData.getValue(),  fetchConfigVO,  pdffileName,
//					 startdate,  enddate);
//		}
//		pdfGen.createPdf(fetchMetadataListVO,
//				fetchConfigVO, "Passed_Report.pdf", null, null);
//		pdfGen.createPdf(fetchMetadataListVO,
//				fetchConfigVO, "Failed_Report.pdf", null, null);
//		pdfGen.createPdf(fetchMetadataListVO,
//				fetchConfigVO, "Detailed_Report.pdf", null, null);
//
//	}
//
//public void createPdf(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO, String pdffileName,
//		Date Starttime, Date endtime) throws IOException, DocumentException, com.itextpdf.text.DocumentException {
//	try {
//		String Date = DateUtils.getSysdate();
//		String Folder = (fetchConfigVO.getWINDOWS_PDF_LOCATION()  + fetchMetadataListVO.get(0).getCustomer_name() + "/"
//				+ fetchMetadataListVO.get(0).getTest_run_name() + "/");
//		// String Folder="C:\\Users\\Winfo Solutions\\Desktop\\new\\";
////		String Folder = "/objstore/udgsup/UDG SUPPORT/UDG - PPM  (copy)/";
//		String FILE = (Folder + pdffileName);
//		System.out.println(FILE);
//		List<String> fileNameList = null;
//		if ("Passed_Report.pdf".equalsIgnoreCase(pdffileName)) {
//			fileNameList = getPassedPdfNew(fetchMetadataListVO, fetchConfigVO);
//		} else if ("Failed_Report.pdf".equalsIgnoreCase(pdffileName)) {
//			fileNameList = getFailedPdfNew(fetchMetadataListVO, fetchConfigVO);
//		} else if ("Detailed_Report.pdf".equalsIgnoreCase(pdffileName)) {
//			fileNameList = getDetailPdfNew(fetchMetadataListVO, fetchConfigVO);
//		} else {
//			fileNameList = getFileNameListNew(fetchMetadataListVO, fetchConfigVO);
//		}
//		String Script_Number = fetchMetadataListVO.get(0).getScript_number();
//		String customer_Name = fetchMetadataListVO.get(0).getCustomer_name();
//		String test_Run_Name = fetchMetadataListVO.get(0).getTest_run_name();
//		String Scenario_Name = fetchMetadataListVO.get(0).getScenario_name();
//		// new change add ExecutedBy field
//		String ExecutedBy = fetchMetadataListVO.get(0).getExecuted_by();
//		String ScriptDescription1 = fetchMetadataListVO.get(0).getScenario_name();
//		File theDir = new File(Folder);
//		if (!theDir.exists()) {
//			System.out.println("creating directory: " + theDir.getName());
//			boolean result = false;
//			try {
//				theDir.mkdirs();
//				result = true;
//			} catch (SecurityException se) {
//				// handle it
//				System.out.println(se.getMessage());
//			}
//		} else {
//			System.out.println("Folder exist");
//		}
//		int passcount = fetchConfigVO.getPasscount();
//		int failcount = fetchConfigVO.getFailcount();
////		Date Starttime = fetchConfigVO.getStarttime();
//		Date Tendtime = fetchConfigVO.getEndtime();
//		Date TStarttime = fetchConfigVO.getStarttime1();
//		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:aa");
//
//		String TStarttime1 = dateFormat.format(TStarttime);
//		String Tendtime1 = dateFormat.format(Tendtime);
//		long Tdiff = Tendtime.getTime() - TStarttime.getTime();
//
//		Document document = new Document();
//		String start = "Execution Summary";
//		String pichart = "Pie-Chart";
//		String Report = "Execution Report";
//		Font bfBold12 = FontFactory.getFont("Arial", 23);
//		Font fnt = FontFactory.getFont("Arial", 12);
//		Font bf12 = FontFactory.getFont("Arial", 23);
//		Font bf15 = FontFactory.getFont("Arial", 23, Font.UNDERLINE);
//		Font bf16 = FontFactory.getFont("Arial", 12,Font.UNDERLINE,new BaseColor(66, 245, 236));
//		Font bf13 = FontFactory.getFont("Arial", 23, Font.UNDERLINE, BaseColor.GREEN);
//		Font bf14 = FontFactory.getFont("Arial", 23, Font.UNDERLINE, BaseColor.RED);
//		Font bfBold = FontFactory.getFont("Arial", 23, BaseColor.WHITE);
//		DefaultPieDataset dataSet = new DefaultPieDataset();
//		PdfWriter writer = null;
//		writer = PdfWriter.getInstance(document, new FileOutputStream(FILE));
//		Rectangle one = new Rectangle(1360, 800);
//		document.setPageSize(one);
//		document.open();
//		System.out.println("before enter Images/wats_icon.png1");
//		Image img1 = Image.getInstance(watslogo);
//		System.out.println("after enter Images/wats_icon.png1");
//
//		img1.scalePercent(65, 68);
//		img1.setAlignment(Image.ALIGN_RIGHT);
////	start to create testrun level reports	
//		if ((passcount != 0 || failcount != 0) & ("Passed_Report.pdf".equalsIgnoreCase(pdffileName)
//				|| "Failed_Report.pdf".equalsIgnoreCase(pdffileName)
//				|| "Detailed_Report.pdf".equalsIgnoreCase(pdffileName))) {
////     Start testrun to add details like start and end time,testrun name
//			String TestRun = TestRun = test_Run_Name;
//			;
//			String StartTime = null;
//			String EndTime = Tendtime1;
//			String ExecutionTime = null;
//			Date date = new Date();
//			Timestamp startTimestamp = new Timestamp(TStarttime.getTime());
//			Timestamp endTimestamp = new Timestamp(Tendtime.getTime());
//
//			Map<Date, Long> timeslist = limitScriptExecutionService
//					.getStarttimeandExecutiontime(fetchMetadataListVO.get(0).getTest_set_id());
//			if (timeslist.size() == 0) {
//				StartTime = TStarttime1;
//				long TdiffSeconds = Tdiff / 1000 % 60;
//				long TdiffMinutes = Tdiff / (60 * 1000) % 60;
//				long TdiffHours = Tdiff / (60 * 60 * 1000);
//				ExecutionTime = TdiffHours + ":" + TdiffMinutes + ":" + TdiffSeconds;
//				if ("Detailed_Report.pdf".equalsIgnoreCase(pdffileName)) {
//					limitScriptExecutionService.updateTestrunTimes(startTimestamp, endTimestamp, Tdiff,
//							fetchMetadataListVO.get(0).getTest_set_id());
//				}
//			} else {
//				for (Entry<Date, Long> entryMap : timeslist.entrySet()) {
//					StartTime = dateFormat.format(entryMap.getKey());
//					long totalTime = Tdiff + entryMap.getValue();
//					long TdiffSeconds = totalTime / 1000 % 60;
//					long TdiffMinutes = totalTime / (60 * 1000) % 60;
//					long TdiffHours = totalTime / (60 * 60 * 1000);
//					ExecutionTime = TdiffHours + ":" + TdiffMinutes + ":" + TdiffSeconds;
//					if ("Detailed_Report.pdf".equalsIgnoreCase(pdffileName)) {
//
//						limitScriptExecutionService.updateTestrunTimes1(endTimestamp, totalTime,
//								fetchMetadataListVO.get(0).getTest_set_id());
//					}
//				}
//			}
//			String TR = "Test Run Name";
//			String SN = "Executed By";
//			String SN1 = "Start Time";
//			String S1 = "End Time";
//			String Scenarios1 = "Execution Time";
//
//			document.add(img1);
//			document.add(new Paragraph(Report, bfBold12));
//			document.add(Chunk.NEWLINE);
//			PdfPTable table1 = new PdfPTable(2);
//			table1.setWidths(new int[] { 1, 1 });
//			table1.setWidthPercentage(100f);
//			insertCell(table1, TR, Element.ALIGN_LEFT, 1, bf12);
//			insertCell(table1, TestRun, Element.ALIGN_LEFT, 1, bf12);
//			insertCell(table1, SN, Element.ALIGN_LEFT, 1, bf12);
//			insertCell(table1, ExecutedBy, Element.ALIGN_LEFT, 1, bf12);
//			insertCell(table1, SN1, Element.ALIGN_LEFT, 1, bf12);
//			insertCell(table1, StartTime, Element.ALIGN_LEFT, 1, bf12);
//			insertCell(table1, S1, Element.ALIGN_LEFT, 1, bf12);
//			insertCell(table1, EndTime, Element.ALIGN_LEFT, 1, bf12);
//			insertCell(table1, Scenarios1, Element.ALIGN_LEFT, 1, bf12);
//			insertCell(table1, ExecutionTime, Element.ALIGN_LEFT, 1, bf12);
//			document.add(table1);
////   End testrun to add details like start and end time,testrun name 	
//
////				Start Testrun to add Table and piechart 		 
//			if (passcount == 0) {
//
//				dataSet.setValue("Fail", failcount);
//			} else if (failcount == 0) {
//				dataSet.setValue("Pass", passcount);
//			} else {
//				dataSet.setValue("Pass", passcount);
//				dataSet.setValue("Fail", failcount);
//			}
//			double pass = Math.round((passcount * 100.0) / (passcount + failcount));
//			double fail = Math.round((failcount * 100.0) / (passcount + failcount));
//			Rectangle one1 = new Rectangle(1360, 1000);
//			if ("Detailed_Report.pdf".equalsIgnoreCase(pdffileName)) {
//				
//				document.setPageSize(one1);
//
//				document.newPage();
//				document.add(img1);
//				Paragraph executionSummery=new Paragraph(start, bfBold12);
////				executionSummery.setAlignment(Element.ALIGN_CENTER);
//				document.add(executionSummery);
//				document.add(Chunk.NEWLINE);
//				DecimalFormat df1 = new DecimalFormat("0");
//				DecimalFormat df2 = new DecimalFormat("0");
////		Start Testrun to add Table   	 
//				PdfPTable table = new PdfPTable(3);
//				table.setWidths(new int[] { 1, 1, 1 });
//				table.setWidthPercentage(100f);
//				insertCell(table, "Status", Element.ALIGN_CENTER, 1, bfBold12);
//				insertCell(table, "Total", Element.ALIGN_CENTER, 1, bfBold12);
//				insertCell(table, "Percentage", Element.ALIGN_CENTER, 1, bfBold12);
//				PdfPCell[] cells1 = table.getRow(0).getCells();
//				for (int k = 0; k < cells1.length; k++) {
//					cells1[k].setBackgroundColor(new BaseColor(161, 190, 212));
//				}
//				insertCell(table, "Passed", Element.ALIGN_CENTER, 1, bf12);
//				insertCell(table, df1.format(passcount), Element.ALIGN_CENTER, 1, bf12);
//				insertCell(table, df2.format(pass) + "%", Element.ALIGN_CENTER, 1, bf12);
//				insertCell(table, "Failed", Element.ALIGN_CENTER, 1, bf12);
//				insertCell(table, df1.format(failcount), Element.ALIGN_CENTER, 1, bf12);
//				insertCell(table, df2.format(fail) + "%", Element.ALIGN_CENTER, 1, bf12);
//				document.setMargins(20, 20, 20, 20);
//				document.add(table);
//			} else if ("Passed_Report.pdf".equalsIgnoreCase(pdffileName)) {
//				document.add(Chunk.NEWLINE);
//				Paragraph executionSummery=new Paragraph(start, bfBold12);
////				executionSummery.setAlignment(Element.ALIGN_CENTER);
//				document.add(executionSummery);
//				document.add(Chunk.NEWLINE);
//				DecimalFormat df1 = new DecimalFormat("0");
//				DecimalFormat df2 = new DecimalFormat("0");
////				Start Testrun to add Table   	 
//				PdfPTable table = new PdfPTable(3);
//				table.setWidths(new int[] { 1, 1, 1 });
//				table.setWidthPercentage(100f);
//				insertCell(table, "Status", Element.ALIGN_CENTER, 1, bfBold12);
//				insertCell(table, "Total", Element.ALIGN_CENTER, 1, bfBold12);
//				insertCell(table, "Percentage", Element.ALIGN_CENTER, 1, bfBold12);
//				PdfPCell[] cells1 = table.getRow(0).getCells();
//				for (int k = 0; k < cells1.length; k++) {
//					cells1[k].setBackgroundColor(new BaseColor(161, 190, 212));
//				}
//
//				insertCell(table, "Passed", Element.ALIGN_CENTER, 1, bf12);
//				insertCell(table, df1.format(passcount), Element.ALIGN_CENTER, 1, bf12);
//				insertCell(table, df2.format(pass) + "%", Element.ALIGN_CENTER, 1, bf12);
//				document.setMargins(20, 20, 20, 20);
//				document.add(table);
//
//			} else {
//				document.add(Chunk.NEWLINE);
//				Paragraph executionSummery=new Paragraph(start, bfBold12);
////				executionSummery.setAlignment(Element.ALIGN_CENTER);
//				document.add(executionSummery);
//				document.add(Chunk.NEWLINE);
//				DecimalFormat df1 = new DecimalFormat("0");
//				DecimalFormat df2 = new DecimalFormat("0");
////						Start Testrun to add Table   	 
//				PdfPTable table = new PdfPTable(3);
//				table.setWidths(new int[] { 1, 1, 1 });
//				table.setWidthPercentage(100f);
//				insertCell(table, "Status", Element.ALIGN_CENTER, 1, bfBold12);
//				insertCell(table, "Total", Element.ALIGN_CENTER, 1, bfBold12);
//				insertCell(table, "Percentage", Element.ALIGN_CENTER, 1, bfBold12);
//				PdfPCell[] cells1 = table.getRow(0).getCells();
//				for (int k = 0; k < cells1.length; k++) {
//					cells1[k].setBackgroundColor(new BaseColor(161, 190, 212));
//				}
//
//				insertCell(table, "Failed", Element.ALIGN_CENTER, 1, bf12);
//				insertCell(table, df1.format(failcount), Element.ALIGN_CENTER, 1, bf12);
//				insertCell(table, df2.format(fail) + "%", Element.ALIGN_CENTER, 1, bf12);
//				document.setMargins(20, 20, 20, 20);
//				document.add(table);
//			}
////		End Testrun to add Table
////		Start Testrun to add piechart 
//			if ("Detailed_Report.pdf".equalsIgnoreCase(pdffileName)) {
//				Chunk ch = new Chunk(pichart, bfBold);
//				ch.setTextRise(-18);
//				ch.setBackground(new BaseColor(38, 99, 175), 0f, 10f, 1730f, 15f);
//
//				Paragraph p1 = new Paragraph(ch);
//				p1.setSpacingBefore(50);
//				document.add(p1);
//
//				JFreeChart chart = ChartFactory.createPieChart(" ", dataSet, true, true, false);
//				Color c1 = new Color(102, 255, 102);
//				Color c = new Color(253, 32, 32);
//
//				LegendTitle legend = chart.getLegend();
//				PiePlot piePlot = (PiePlot) chart.getPlot();
//				piePlot.setSectionPaint("Pass", c1);
//				piePlot.setSectionPaint("Fail", c);
//				piePlot.setBackgroundPaint(Color.WHITE);
//				piePlot.setOutlinePaint(null);
//				piePlot.setLabelBackgroundPaint(null);
//				piePlot.setLabelOutlinePaint(null);
//				piePlot.setLabelGenerator(new StandardPieSectionLabelGenerator());
//				piePlot.setInsets(new RectangleInsets(10, 5.0, 5.0, 5.0));
//				piePlot.setLabelShadowPaint(null);
//				piePlot.setShadowXOffset(0.0D);
//				piePlot.setShadowYOffset(0.0D);
//				piePlot.setLabelGenerator(null);
//				piePlot.setBackgroundAlpha(0.4f);
//				piePlot.setExplodePercent("Pass", 0.05);
//				piePlot.setSimpleLabels(true);
//				piePlot.setSectionOutlinesVisible(false);
//				java.awt.Font f2 = new java.awt.Font("", java.awt.Font.PLAIN, 22);
//				piePlot.setLabelFont(f2);
//
//				PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator("{2}", new DecimalFormat("0"),
//						new DecimalFormat("0%"));
//				piePlot.setLabelGenerator(gen);
//				legend.setPosition(RectangleEdge.RIGHT);
//				legend.setVerticalAlignment(VerticalAlignment.CENTER);
//				piePlot.setInsets(new RectangleInsets(0.0, 5.0, 5.0, 5.0));
//				legend.setFrame(BlockBorder.NONE);
//				legend.setFrame(
//						new LineBorder(Color.white, new BasicStroke(20f), new RectangleInsets(1.0, 1.0, 1.0, 1.0)));
//
//				java.awt.Font pass1 = new java.awt.Font("", Font.NORMAL, 22);
//				legend.setItemFont(pass1);
//				PdfContentByte contentByte = writer.getDirectContent();
//				PdfTemplate template = contentByte.createTemplate(1000, 900);
//				Graphics2D graphics2d = template.createGraphics(700, 400, new DefaultFontMapper());
//				Rectangle2D rectangle2d = new Rectangle2D.Double(0, 0, 600, 400);
//				chart.draw(graphics2d, rectangle2d);
//				graphics2d.dispose();
//				contentByte.addTemplate(template, 400, 100);
//			}
////		 End Testrun to add piechart 
////End Testrun to add Table and piechart 
////				 		Start to add page heading,all testrun names and states and page numbers	 		
//			int k = 0, l = 0;
//			String sno1 = "";
//			Map<Integer, Map<String, String>> toc = new TreeMap<>();
//
//			Map<String, String> toc2 = new TreeMap<>();
//			for (String image : fileNameList) {
//				k++;
//				String sndo = image.split("_")[0];
//				String name = image.split("_")[3];
//
//				if (!sndo.equalsIgnoreCase(sno1)) {
//					Map<String, String> toc1 = new TreeMap<>();
////				 				l=0;
//					for (String image1 : fileNameList) {
//						String Status = image1.split("_")[6];
//						String status = Status.split("\\.")[0];
//
////				 					l++;
//						if (image1.startsWith(sndo + "_") && image1.contains("Failed")) {
//
////				 						toc2.put(sndo,String.valueOf(l-2));	
//							toc2.put(sndo, "Failed" + l);
//							l++;
//						}
//					}
//
//					String str = String.valueOf(toc2.get(sndo));
//					toc1.put(sndo+"_"+name, str);
//					toc.put(k, toc1);
//
//				}
//				if (sndo != null) {
//					sno1 = sndo;
//				}
//			}
//			sno1 = "";
//			document.newPage();
//			document.add(img1);
////			Start to add page heading 
//			Anchor target2 = new Anchor(String.valueOf("Page Numbers"), bfBold);
//			target2.setName(String.valueOf("details"));
//			Chunk ch1 = new Chunk(String.format("Script Numbers"), bfBold);
//			ch1.setBackground(new BaseColor(38, 99, 175), 0f, 10f, 1730f, 15f);
//			Paragraph p2 = new Paragraph();
//			p2.add(ch1);
//			p2.add(new Chunk(new VerticalPositionMark()));
//			p2.add(target2);
//			document.add(p2);
//			document.add(Chunk.NEWLINE);
////			End to add page heading 
//
////		 Start to add all testrun names and states and page numbers	
//			Chunk dottedLine = new Chunk(new DottedLineSeparator());
//			for (Entry<Integer, Map<String, String>> entry : toc.entrySet()) {
//				Map<String, String> str1 = entry.getValue();
//				for (Entry<String, String> entry1 : str1.entrySet()) {
//					Anchor click = new Anchor(String.valueOf(entry.getKey()), bf15);
//					click.setReference("#" + String.valueOf(entry1.getKey()));
//					Anchor click1 = new Anchor(String.valueOf("(Failed)"), bf14);
//					click1.setReference("#" + String.valueOf(entry1.getValue()));
//					Paragraph pr = new Paragraph();
//					int value = entry.getKey();
//					Anchor ca1 = new Anchor(String.valueOf(entry1.getKey()), bf15);
//					ca1.setReference("#" + String.valueOf(entry1.getKey()));
//					String compare = entry1.getValue();
//					if (!compare.equals("null")) {
//						pr.add(ca1);
//
//						pr.add(click1);
//						pr.add(dottedLine);
//						pr.add(click);
//						document.add(Chunk.NEWLINE);
//						document.add(pr);
//					} else {
//						Anchor click2 = new Anchor(String.valueOf("(Passed)"), bf13);
//						click2.setReference("#" + String.valueOf(entry1.getKey()));
//						pr.add(ca1);
//						pr.add(click2);
//						pr.add(dottedLine);
//						pr.add(click);
//						document.add(Chunk.NEWLINE);
//						document.add(pr);
//					}
//				}
//			}
////		 End to add all testrun names and states and page numbers
////		 End to add page heading,add all testrun names and states and page numbers	
//
////Start to add script details, screenshoots and pagenumbers and wats icon	
//			int i = 0, j = 0;
//			for (String image : fileNameList) {
//				i++;
//				Image img = Image.getInstance(
//						fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + customer_Name + "/" + test_Run_Name + "/" + image);
////Start to add script details 
//				String sno = image.split("_")[0];
//				String SNO = "Script Number";
//				String ScriptNumber = image.split("_")[3];
//				String SNM = "Test Case Name";
//				String ScriptName = image.split("_")[2];
//				String testRunName = image.split("_")[4];
////			String scrtipt=;
//				if (!sno.equalsIgnoreCase(sno1)) {
//					document.setPageSize(img);
//					document.newPage();
//					document.add(img1);
//					Anchor target3 = new Anchor("Script Details", bf12);
//					target3.setName(ScriptNumber);
//					Paragraph pa = new Paragraph();
//					pa.add(target3);
////					pa.setAlignment(Element.ALIGN_CENTER);
//					document.add(pa);
//					document.add(Chunk.NEWLINE);
//					PdfPTable table2 = new PdfPTable(2);
//					table2.setWidths(new int[] { 1, 1 });
//					table2.setWidthPercentage(100f);
//					insertCell(table2, SNO, Element.ALIGN_LEFT, 1, bf12);
//					insertCell(table2, ScriptNumber, Element.ALIGN_LEFT, 1, bf12);
//					insertCell(table2, SNM, Element.ALIGN_LEFT, 1, bf12);
//					insertCell(table2, ScriptName, Element.ALIGN_LEFT, 1, bf12);
//
//					for (Entry<String, String> entry1 : toc.get(i).entrySet()) {
//						String str = entry1.getValue();
//						if (!str.equals("null")) {
//							insertCell(table2, "Status", Element.ALIGN_LEFT, 1, bf12);
//							insertCell(table2, "Failed", Element.ALIGN_LEFT, 1, bf12);
//						} else {
//							insertCell(table2, "Status", Element.ALIGN_LEFT, 1, bf12);
//							insertCell(table2, "Passed", Element.ALIGN_LEFT, 1, bf12);
//						}
//					}
//
//					document.add(table2);
//
//				}
//				if (sno != null) {
//					sno1 = sno;
//				}
////End to add script details 
//
////Start to add  screenshoots and pagenumbers and wats icon		 		
////			String TestRun = image.split("_")[4];
//				String Status = image.split("_")[6];
//				String status = Status.split("\\.")[0];
//				String Scenario = image.split("_")[2];
//
////			String TR = "Test Run Name:" + " " + TestRun;
////			String SN = "Script Number:" + " " + ScriptNumber;
//
//				String Scenarios = "Test Case Name :" + "" + Scenario;
//
//				String sndo = image.split("_")[0];
//				img1.scalePercent(65, 68);
//
//				img1.setAlignment(Image.ALIGN_RIGHT);
//				// new change-failed pdf to set pagesize
//				if (image.startsWith(sndo + "_") && image.contains("Failed")) {
////				Rectangle one2 = new Rectangle(1360,1000);
//					document.setPageSize(one1);
//					document.newPage();
//				} else {
//
//					document.setPageSize(img);
//					document.newPage();
//				}
//				document.add(img1);
//				document.add(new Paragraph(Scenarios, fnt));
//				String Reason = image.split("_")[5];
//				String step = "Step No :" + "" + Reason;
//				String Message = "Failed at Line Number:" + "" + Reason;
//				// new change-database to get error message
//				String error = databaseentry.getErrorMessage(sndo, ScriptNumber, testRunName, fetchConfigVO);
//				String errorMessage = "Failed Message:" + "" + error;
//				Paragraph pr1 = new Paragraph();
//				pr1.add("Status:");
//
//				if (image.startsWith(sndo + "_") && image.contains("Failed")) {
//					Anchor target1 = new Anchor(status);
//					target1.setName(String.valueOf(status + j));
//					j++;
//					pr1.add(target1);
//					document.add(pr1);
//					document.add(new Paragraph(Message, fnt));
//					if (error != null) {
//						document.add(new Paragraph(errorMessage, fnt));
//					}
//					document.add(Chunk.NEWLINE);
//					img.setAlignment(Image.ALIGN_CENTER);
//					img.isScaleToFitHeight();
//					// new change-change page size
//					img.scalePercent(60, 60);
//					document.add(img);
//
//				} else {
//					document.add(new Paragraph(step, fnt));
//					Anchor target1 = new Anchor(status);
//					target1.setName(String.valueOf(status));
//					pr1.add(target1);
//					document.add(pr1);
//					img.setAlignment(Image.ALIGN_CENTER);
//					img.isScaleToFitHeight();
//					// new change-change page size
//					img.scalePercent(60, 68);
//					document.add(img);
//				}
//
//				Anchor target = new Anchor(String.valueOf(i));
//				target.setName(String.valueOf(i));
//				Anchor target1 = new Anchor(String.valueOf("Back to Index"), bf16);
//				target1.setReference("#" + String.valueOf("details"));
//				Paragraph p = new Paragraph();
//				p.add(target1);
//				p.add(new Chunk(new VerticalPositionMark()));
//				p.add(" page ");
//				p.add(target);
//				p.add(" of " + fileNameList.size());
////			img.setAlignment(Image.ALIGN_CENTER);
////			img.isScaleToFitHeight();
////			img.scalePercent(60, 71);
////			document.add(img);
//				document.add(p);
//				System.out.println("This Image " + "" + image + "" + "was added to the report");
////End to add  screenshots and pagenumbers and wats icon		 		
////End to add script details, screenshoots and pagenumbers and wats icon		 		
////End to create testrun level reports	
//			}
//		} else {
////Start to create Script level passed reports		
////Start to add Script level details		
//			if (!("Passed_Report.pdf".equalsIgnoreCase(pdffileName)
//					|| "Failed_Report.pdf".equalsIgnoreCase(pdffileName)
//					|| "Detailed_Report.pdf".equalsIgnoreCase(pdffileName))) {
//				String Starttime1 = dateFormat.format(Starttime);
//				String endtime1 = dateFormat.format(endtime);
//				long diff = endtime.getTime() - Starttime.getTime();
//				long diffSeconds = diff / 1000 % 60;
//				long diffMinutes = diff / (60 * 1000) % 60;
//				long diffHours = diff / (60 * 60 * 1000);
//				String TestRun = test_Run_Name;
//				String ScriptNumber = Script_Number;
//				String ScriptNumber1 = Scenario_Name;
//				String Scenario1 = fetchConfigVO.getStatus1();
////				String ExecutedBy=fetchConfigVO.getApplication_user_name();
//				String StartTime = Starttime1;
//				String EndTime = endtime1;
//				String ExecutionTime = diffHours + ":" + diffMinutes + ":" + diffSeconds;
//
//				String TR = "Test Run Name";
//				String SN = "Script Number";
//				String SN1 = "Scenario name";
//				String Scenarios1 = "Status ";
//				String EB = "Executed By";
//				String ST = "Start Time";
//				String ET = "End Time";
//				String EX = "Execution Time";
//				document.add(img1);
//
//				document.add(new Paragraph(Report, bfBold12));
//				document.add(Chunk.NEWLINE);
//				PdfPTable table1 = new PdfPTable(2);
//				table1.setWidths(new int[] { 1, 1 });
//				table1.setWidthPercentage(100f);
//
//				insertCell(table1, TR, Element.ALIGN_LEFT, 1, bf12);
//				insertCell(table1, TestRun, Element.ALIGN_LEFT, 1, bf12);
//				insertCell(table1, SN, Element.ALIGN_LEFT, 1, bf12);
//				insertCell(table1, ScriptNumber, Element.ALIGN_LEFT, 1, bf12);
//				insertCell(table1, SN1, Element.ALIGN_LEFT, 1, bf12);
//				insertCell(table1, ScriptNumber1, Element.ALIGN_LEFT, 1, bf12);
//				insertCell(table1, Scenarios1, Element.ALIGN_LEFT, 1, bf12);
//				insertCell(table1, Scenario1, Element.ALIGN_LEFT, 1, bf12);
//				insertCell(table1, EB, Element.ALIGN_LEFT, 1, bf12);
//				insertCell(table1, ExecutedBy, Element.ALIGN_LEFT, 1, bf12);
//				insertCell(table1, ST, Element.ALIGN_LEFT, 1, bf12);
//				insertCell(table1, StartTime, Element.ALIGN_LEFT, 1, bf12);
//				insertCell(table1, ET, Element.ALIGN_LEFT, 1, bf12);
//				insertCell(table1, EndTime, Element.ALIGN_LEFT, 1, bf12);
//				insertCell(table1, EX, Element.ALIGN_LEFT, 1, bf12);
//				insertCell(table1, ExecutionTime, Element.ALIGN_LEFT, 1, bf12);
//				document.add(table1);
//				document.newPage();
////End to add Script level details
//
////Start to add screenshoots and pagenumbers and wats icon		 		
//				int i = 0;
//				for (String image : fileNameList) {
////			 Image img = Image.getInstance(
////			 fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + customer_Name + "\\" + test_Run_Name +
////			 "\\" + image);
//					i++;
//					Image img = Image.getInstance(
//							fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + customer_Name + "/" + test_Run_Name + "/" + image);
//
//					String Status = image.split("_")[6];
//					String status = Status.split("\\.")[0];
//					String Scenario = image.split("_")[2];
//					String steps = image.split("_")[5];
//					document.setPageSize(img);
//					document.newPage();
//
//					String S = "Status:" + " " + status;
//					String Scenarios = "Test Case Name :" + "" + Scenario;
//					String step = "Step No :" + "" + steps;
//					img1.scalePercent(65, 65);
//					img1.setAlignment(Image.ALIGN_RIGHT);
//					document.add(img1);
//					document.add(new Paragraph(S, fnt));
//					document.add(new Paragraph(Scenarios, fnt));
//					document.add(new Paragraph(step, fnt));
//					document.add(Chunk.NEWLINE);
//
//					Paragraph p = new Paragraph(String.format("page %s of %s", i, fileNameList.size()));
//					p.setAlignment(Element.ALIGN_RIGHT);
//					img.setAlignment(Image.ALIGN_CENTER);
//					img.isScaleToFitHeight();
//					// new change-change page size
//					img.scalePercent(60, 62);
//					document.add(img);
//					document.add(p);
//					System.out.println("This Image " + "" + image + "" + "was added to the report");
////	End to add screenshoots and pagenumbers and wats icon
////End to create Script level passed reports		
//
//				}
//			}
//		}
//		document.close();
////		compress(fetchMetadataListVO, fetchConfigVO, pdffileName);
//
//	} catch (Exception e) {
//		System.out.println("Not able to Create pdf" + e);
//	}
//}
//public void insertCell(PdfPTable table, String text, int align, int colspan, Font font) {
//
//	// create a new cell with the specified Text and Font
//	PdfPCell cell = new PdfPCell(new Paragraph(text.trim(), font));
//	cell.setBorder(PdfPCell.NO_BORDER);
//	// set the cell alignment
//
//	cell.setUseVariableBorders(true);
//	if (text.equalsIgnoreCase("Status")) {
//		cell.setBorderWidthLeft(0.3f);
//		cell.setBorderColorLeft(new BaseColor(230, 225, 225));
//		cell.setBorderWidthTop(0.3f);
//		cell.setBorderColorTop(new BaseColor(230, 225, 225));
//		cell.setBorderWidthRight(0.3f);
//		cell.setBorderColorRight(new BaseColor(230, 225, 225));
//		cell.setBorderWidthBottom(0.3f);
//		cell.setBorderColorBottom(new BaseColor(230, 225, 225));
//	} else if (text.equalsIgnoreCase("Total")) {
//		cell.setBorderWidthTop(0.3f);
//		cell.setBorderColorTop(new BaseColor(230, 225, 225));
//		cell.setBorderWidthRight(0.3f);
//		cell.setBorderColorRight(new BaseColor(230, 225, 225));
//		cell.setBorderWidthBottom(0.3f);
//		cell.setBorderColorBottom(new BaseColor(230, 225, 225));
//	} else if (text.equalsIgnoreCase("Percentage")) {
//		cell.setBorderWidthTop(0.3f);
//		cell.setBorderColorTop(new BaseColor(230, 225, 225));
//		cell.setBorderWidthRight(0.3f);
//		cell.setBorderColorRight(new BaseColor(230, 225, 225));
//		cell.setBorderWidthBottom(0.3f);
//		cell.setBorderColorBottom(new BaseColor(230, 225, 225));
//	} else if (text.equalsIgnoreCase("Passed") || text.equalsIgnoreCase("Failed")) {
//		cell.setBorderWidthLeft(0.3f);
//		cell.setBorderColorLeft(new BaseColor(230, 225, 225));
//		cell.setBorderWidthRight(0.3f);
//		cell.setBorderColorRight(new BaseColor(230, 225, 225));
//		cell.setBorderWidthBottom(0.3f);
//		cell.setBorderColorBottom(new BaseColor(230, 225, 225));
//	} else if (text.contains("%")) {
//		cell.setBorderWidthRight(0.3f);
//		cell.setBorderColorRight(new BaseColor(230, 225, 225));
//		cell.setBorderWidthBottom(0.3f);
//		cell.setBorderColorBottom(new BaseColor(230, 225, 225));
//	}
////  	else if() {
////  	 cell.setBorderWidthRight(0.3f);
////  	cell.setBorderColorRight(new BaseColor(230, 225, 225));
////  		cell.setBorderWidthBottom(0.3f);
////  		cell.setBorderColorBottom(new BaseColor(230, 225, 225));
////  	}
//	else {
//		cell.setBorderWidthLeft(0.3f);
//		cell.setBorderColorLeft(new BaseColor(230, 225, 225));
//		cell.setBorderWidthTop(0.3f);
//		cell.setBorderColorTop(new BaseColor(230, 225, 225));
//		cell.setBorderWidthRight(0.3f);
//		cell.setBorderColorRight(new BaseColor(230, 225, 225));
//		cell.setBorderWidthBottom(0.3f);
//		cell.setBorderColorBottom(new BaseColor(230, 225, 225));
//	}
//
//	cell.setHorizontalAlignment(align);
//
//	cell.setColspan(colspan);
//	// in case there is no text and you wan to create an empty row
//	if (text.trim().equalsIgnoreCase("")) {
//		cell.setMinimumHeight(20f);
//	}
//	if (text.length() > 103) {
//		cell.setFixedHeight(80f);
//	} else if (text.length() > 53) {
//		cell.setFixedHeight(60f);
//	} else {
//		cell.setFixedHeight(40f);
//	}
//	// add the call to the table
//	table.addCell(cell);
//
//}
//public List<String> getPassedPdfNew(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO)
//		throws IOException {
//
//	File folder = new File(fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
//			+ fetchMetadataListVO.get(0).getTest_run_name() + "/");
//	// File folder=new File("C:\\Users\\Winfo Solutions\\Desktop\\test");
//	File[] listOfFiles = folder.listFiles();
//	// String video_rec=fetchConfigVO.getVideo_rec();
//	String video_rec = "no";
//	Map<Integer, List<File>> filesMap = new TreeMap<>();
//	int passcount = 0;
//	int failcount = 0;
//	for (File file : Arrays.asList(listOfFiles)) {
//
//		Integer seqNum = Integer.valueOf(file.getName().substring(0, file.getName().indexOf('_')));
//
//		if (!filesMap.containsKey(seqNum)) {
//
//			filesMap.put(seqNum, new ArrayList<File>());
//
//		}
//
//		filesMap.get(seqNum).add(file);
//
//	}
//
//	List<String> targetFileList = new ArrayList<>();
//	ArrayList<String> links = new ArrayList<String>();
//	String firstimagelink = null;
//	for (Entry<Integer, List<File>> seqEntry : filesMap.entrySet()) {
//
//		List<File> seqList = seqEntry.getValue();
//
//		Collections.sort(seqList, new Comparator<File>() {
//
//			public int compare(File f1, File f2) {
//
//				return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified()) * -1;
//
//			}
//
//		});
//		List<String> seqFileNameList = new ArrayList<String>();
//		ArrayList<String> links1 = new ArrayList<String>();
//		ArrayList<String> linksall = new ArrayList<String>();
//
//		File file = new ClassPathResource(whiteimage).getFile();
//		// File file = new File("C:\\Users\\Winfo
//		// Solutions\\Desktop\\Add_On\\white.jpg");
//		File file1 = new ClassPathResource(watsvediologo).getFile();
//		// File file1=new File("C:\\Users\\Winfo
//		// Solutions\\Desktop\\Add_On\\WATS_LOGO.JPG");
//
//		BufferedImage image = null;
//		image = ImageIO.read(file);
//		BufferedImage logo = null;
//		logo = ImageIO.read(file1);
//		Graphics g = image.getGraphics();
//		g.setColor(Color.black);
//		java.awt.Font font = new java.awt.Font("Calibri", java.awt.Font.PLAIN, 36);
//		g.setFont(font);
//
//		String details = seqList.get(0).getName();
//		String ScriptNumber = details.split("_")[3];
//		String TestRun = details.split("_")[4];
//		String Status = details.split("_")[6];
//		String status = Status.split("\\.")[0];
//		String Scenario = details.split("_")[2];
//		String imagename = TestRun + ScriptNumber;
//		String TName = fetchMetadataListVO.get(0).getTest_run_name();
//		Date endtime = fetchConfigVO.getEndtime();
//		Date TStarttime = fetchConfigVO.getStarttime1();
//		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//		String TStarttime1 = dateFormat.format(TStarttime);
////Changed the executed by variable
//		String ExeBy = fetchMetadataListVO.get(0).getExecuted_by();
//		String endtime1 = dateFormat.format(endtime);
//		long Tdiff = endtime.getTime() - TStarttime.getTime();
//		long TdiffSeconds = Tdiff / 1000 % 60;
//		long TdiffMinutes = Tdiff / (60 * 1000) % 60;
//		long TdiffHours = Tdiff / (60 * 60 * 1000);
//		String ExecutionTime = TdiffHours + ":" + TdiffMinutes + ":" + TdiffSeconds;
//		g.drawString("TEST SCRIPT DETAILS", 450, 50);
//		g.drawString("Test Run Name : " + TName, 50, 125);
//		g.drawString("Script Number : " + ScriptNumber, 50, 200);
//		g.drawString("Test Case Name :" + Scenario, 50, 275);
//		g.drawString("Status : " + status, 50, 350);
//		g.drawString("Executed By :" + ExeBy, 50, 425);
//		g.drawImage(logo, 1012, 15, null);
//////	 g.drawString("Start Time :"+TStarttime1, 50, 425);
//////	 g.drawString("End Time :"+endtime1, 50, 500);
//////	 g.drawString("Execution Time : "+ExecutionTime, 50, 575);
//		g.dispose();
//
//		BufferedImage image2 = null;
//		image2 = ImageIO.read(file);
//		Graphics g2 = image2.getGraphics();
//		g2.setColor(Color.black);
//		g2.setFont(font);
//		g2.drawString("TEST RUN SUMMARY", 450, 50);
//		g2.drawString("Test Run Name : " + TName, 50, 125);
//		g2.drawString("Executed By :" + ExeBy, 50, 200);
//		g2.drawString("Start Time :" + TStarttime1, 50, 275);
//		g2.drawString("End Time :" + endtime1, 50, 350);
//		g2.drawString("Execution Time : " + ExecutionTime, 50, 425);
//		g2.drawImage(logo, 1012, 15, null);
//		g2.dispose();
//		File folder1 = new File(
//				fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + fetchMetadataListVO.get(0).getCustomer_name() + "/Images");
//		if (!folder1.exists()) {
//			System.out.println("creating directory: " + folder1.getName());
//			boolean result = false;
//			try {
//				folder1.mkdirs();
//				result = true;
//			} catch (SecurityException se) {
//				// handle it
//				System.out.println(se.getMessage());
//			}
//		} else {
//			System.out.println("Folder exist");
//		}
//
//		ImageIO.write(image2, "jpg", new File(folder1 + "/first.jpg"));
//		// ImageIO.write(image2, "jpg", new File("C:\\Users\\Winfo
//		// Solutions\\Desktop\\Add_On\\first.jpg"));
//		String imgpath3 = folder1 + "/first.jpg";
//		String imgpath2 = folder1 + "/";
//		ImageIO.write(image, "jpg", new File(folder1 + "/" + imagename + ".jpg"));
//		// String imgpath3 ="C\\Users\\Winfo Solutions\\Desktop\\Add_On\\first.jpg";
//		// String imgpath2 ="C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\";
//		// ImageIO.write(image, "jpg", new File("C:\\Users\\Winfo
//		// Solutions\\Desktop\\Add_On\\"+imagename+".jpg"));
//
//		File f11 = new File(imgpath2);
//		File[] f22 = f11.listFiles();
//		File f44 = new File(imgpath3);
//		firstimagelink = f44.getAbsolutePath();
//
//		if (!seqList.get(0).getName().endsWith("Failed.jpg")) {
//			passcount++;
//			for (File f33 : f22) {
//				if (f33.getAbsolutePath().contains(imagename)) {
//					linksall.add(f33.getAbsolutePath());
//				}
//			}
//			links1.add(seqList.get(0).getAbsolutePath());
//			seqFileNameList.add(seqList.get(0).getName());
//
//			for (int i = 1; i < seqList.size(); i++) {
//
//				if (!seqList.get(i).getName().endsWith("Failed.jpg")) {
//					links1.add(seqList.get(i).getAbsolutePath());
//					seqFileNameList.add(seqList.get(i).getName());
//
//				} else {
//
//				}
//
//			}
//
//			links1.add(linksall.get(0));
//			Collections.reverse(links1);
//			Collections.reverse(seqFileNameList);
//			links.addAll(links1);
//			targetFileList.addAll(seqFileNameList);
//
//		}
//
//////                targetFileList.addAll(seqList);
//
//	}
//
//	/*
//	 * for (String fileName : targetFileList) {
//	 * 
//	 * System.out.println("Target File : " + fileName);
//	 * 
//	 * }
//	 */
//
//	fetchConfigVO.setPasscount(passcount);
//	fetchConfigVO.setFailcount(failcount);
//
//	System.out.println(targetFileList.size());
//	return targetFileList;
//}
//
//public List<String> getFailedPdfNew(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO)
//		throws IOException {
//
//	File folder = new File(fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
//			+ fetchMetadataListVO.get(0).getTest_run_name() + "/");
//	// File folder=new File("C:\\Users\\Winfo Solutions\\Desktop\\test");
//	File[] listOfFiles = folder.listFiles();
//	// String video_rec=fetchConfigVO.getVideo_rec();
//	String video_rec = "no";
//	Map<Integer, List<File>> filesMap = new TreeMap<>();
//	int failcount = 0;
//	int passcount = 0;
//	for (File file : Arrays.asList(listOfFiles)) {
//
//		Integer seqNum = Integer.valueOf(file.getName().substring(0, file.getName().indexOf('_')));
//
//		if (!filesMap.containsKey(seqNum)) {
//
//			filesMap.put(seqNum, new ArrayList<File>());
//
//		}
//
//		filesMap.get(seqNum).add(file);
//
//	}
//
//	List<String> targetFileList = new ArrayList<>();
//	ArrayList<String> links = new ArrayList<String>();
//	String firstimagelink = null;
//	for (Entry<Integer, List<File>> seqEntry : filesMap.entrySet()) {
//
//		List<File> seqList = seqEntry.getValue();
//
//		Collections.sort(seqList, new Comparator<File>() {
//
//			public int compare(File f1, File f2) {
//
//				return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified()) * -1;
//
//			}
//
//		});
//
//		List<String> seqFileNameList = new ArrayList<String>();
//		ArrayList<String> links1 = new ArrayList<String>();
//		ArrayList<String> linksall = new ArrayList<String>();
//
//		File file = new ClassPathResource(whiteimage).getFile();
//		// File file = new File("C:\\Users\\Winfo
//		// Solutions\\Desktop\\Add_On\\white.jpg");
//		File file1 = new ClassPathResource(watsvediologo).getFile();
//		// File file1=new File("C:\\Users\\Winfo
//		// Solutions\\Desktop\\Add_On\\WATS_LOGO.JPG");
//
//		BufferedImage image = null;
//		image = ImageIO.read(file);
//		BufferedImage logo = null;
//		logo = ImageIO.read(file1);
//		Graphics g = image.getGraphics();
//		g.setColor(Color.black);
//		java.awt.Font font = new java.awt.Font("Calibri", java.awt.Font.PLAIN, 36);
//		g.setFont(font);
//		String details = seqList.get(0).getName();
//		String ScriptNumber = details.split("_")[3];
//		String TestRun = details.split("_")[4];
//		String Status = details.split("_")[6];
//		String status = Status.split("\\.")[0];
//		String Scenario = details.split("_")[2];
//		String imagename = TestRun + ScriptNumber;
//		String TName = fetchMetadataListVO.get(0).getTest_run_name();
//		Date endtime = fetchConfigVO.getEndtime();
//		Date TStarttime = fetchConfigVO.getStarttime1();
//		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//		String TStarttime1 = dateFormat.format(TStarttime);
////Changed the executed by variable
//		String ExeBy = fetchMetadataListVO.get(0).getExecuted_by();
//		String endtime1 = dateFormat.format(endtime);
//		long Tdiff = endtime.getTime() - TStarttime.getTime();
//		long TdiffSeconds = Tdiff / 1000 % 60;
//		long TdiffMinutes = Tdiff / (60 * 1000) % 60;
//		long TdiffHours = Tdiff / (60 * 60 * 1000);
//		String ExecutionTime = TdiffHours + ":" + TdiffMinutes + ":" + TdiffSeconds;
//
//		g.drawString("TEST SCRIPT DETAILS", 450, 50);
//		g.drawString("Test Run Name : " + TName, 50, 125);
//		g.drawString("Script Number : " + ScriptNumber, 50, 200);
//		g.drawString("Test Case Name :" + Scenario, 50, 275);
//		g.drawString("Status : " + status, 50, 350);
//		g.drawString("Executed By :" + ExeBy, 50, 425);
//		g.drawImage(logo, 1150, 15, null);
//////    g.drawString("Start Time :"+TStarttime1, 50, 425);
//////    g.drawString("End Time :"+endtime1, 50, 500);
//////    g.drawString("Execution Time : "+ExecutionTime, 50, 575);
//		g.dispose();
//		File folder1 = new File(
//				fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + fetchMetadataListVO.get(0).getCustomer_name() + "/Images");
//		if (!folder1.exists()) {
//			System.out.println("creating directory: " + folder1.getName());
//			boolean result = false;
//			try {
//				folder1.mkdirs();
//				result = true;
//			} catch (SecurityException se) {
//				// handle it
//				System.out.println(se.getMessage());
//			}
//		} else {
//			System.out.println("Folder exist");
//		}
//
//		ImageIO.write(image, "jpg", new File(folder1 + "/" + imagename + ".jpg"));
//		// ImageIO.write(image, "jpg", new File("C:\\Users\\Winfo
//		// Solutions\\Desktop\\Add_On\\"+imagename+".jpg"));
//
//		BufferedImage image1 = null;
//		image1 = ImageIO.read(file);
//		Graphics g1 = image1.getGraphics();
//		g1.setColor(Color.red);
//		java.awt.Font font1 = new java.awt.Font("Calibri", java.awt.Font.PLAIN, 36);
//		g1.setFont(font1);
//		g1.drawImage(logo, 1012, 14, null);
//		g1.drawString("FAILED IN THE NEXT STEP!!", 400, 300);
//		g1.dispose();
//		ImageIO.write(image1, "jpg", new File(folder1 + "/last.jpg"));
//		// ImageIO.write(image1, "jpg", new File("C:\\Users\\Winfo
//		// Solutions\\Desktop\\Add_On\\last.jpg"));
//
//		BufferedImage image2 = null;
//		image2 = ImageIO.read(file);
//		Graphics g2 = image2.getGraphics();
//		g2.setColor(Color.black);
//		g2.setFont(font);
//		g2.drawString("TEST RUN SUMMARY", 50, 50);
//		g2.drawString("Test Run Name : " + TName, 50, 125);
//		g2.drawString("Executed By :" + ExeBy, 50, 200);
//		g2.drawString("Start Time :" + TStarttime1, 50, 275);
//		g2.drawString("End Time :" + endtime1, 50, 350);
//		g2.drawString("Execution Time : " + ExecutionTime, 50, 425);
//		g2.drawImage(logo, 1012, 15, null);
//		g2.dispose();
//		ImageIO.write(image2, "jpg", new File(folder1 + "/first.jpg"));
//		String imgpath3 = folder1 + "/first.jpg";
//		String imgpath2 = folder1 + "/";
//
//		// ImageIO.write(image2, "jpg", new File("C:\\Users\\Winfo
//		// Solutions\\Desktop\\Add_On\\first.jpg"));
//		// String imgpath3 ="C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\first.jpg";
//		// String imgpath2 ="C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\";
//		File f11 = new File(imgpath2);
//		File[] f22 = f11.listFiles();
//		File f44 = new File(imgpath3);
//		firstimagelink = f44.getAbsolutePath();
//
//		if (seqList.get(0).getName().endsWith("Failed.jpg")) {
//			failcount++;
//			for (File f33 : f22) {
//				if (f33.getAbsolutePath().contains(imagename)) {
//					linksall.add(f33.getAbsolutePath());
//					linksall.set(0, f33.getAbsolutePath());
//				}
//				if (f33.getAbsolutePath().contains("last")) {
//					linksall.add(f33.getAbsolutePath());
//					linksall.add(f33.getAbsolutePath());
//					linksall.set(1, f33.getAbsolutePath());
//
//				}
//			}
////                               System.out.println("SEQ : "+seqEntry.getKey());
//			links1.add(seqList.get(0).getAbsolutePath());
//			links1.add(linksall.get(1));
//			seqFileNameList.add(seqList.get(0).getName());
//
//			for (int i = 1; i < seqList.size(); i++) {
//
//				if (!seqList.get(i).getName().endsWith("Failed.jpg")) {
//					links1.add(seqList.get(i).getAbsolutePath());
//					seqFileNameList.add(seqList.get(i).getName());
//
//				} else {
//
//				}
//
//			}
//			links1.add(linksall.get(0));
//			Collections.reverse(links1);
//			Collections.reverse(seqFileNameList);
//			links.addAll(links1);
//			targetFileList.addAll(seqFileNameList);
//
//		}
//
////                targetFileList.addAll(seqList);
//
//	}
////
////	/*
////	 * for (String fileName : targetFileList) {
////	 * 
////	 * System.out.println("Target File : " + fileName);
////	 * 
////	 * }
////	 */
//	fetchConfigVO.setPasscount(passcount);
//	fetchConfigVO.setFailcount(failcount);
//	
//	return targetFileList;
//
//}
//
//public List<String> getDetailPdfNew(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO)
//		throws IOException {
//
//	File folder = new File(fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
//			+ fetchMetadataListVO.get(0).getTest_run_name() + "/");
//	// File folder=new File("C:\\Users\\Winfo Solutions\\Desktop\\test");
//	File[] listOfFiles = folder.listFiles();
////	String video_rec=fetchConfigVO.getEnable_video();
//	String video_rec = "no";
//	Map<Integer, List<File>> filesMap = new TreeMap<>();
//	int failcount = 0;
//	int passcount = 0;
//	for (File file : Arrays.asList(listOfFiles)) {
//
//		Integer seqNum = Integer.valueOf(file.getName().substring(0, file.getName().indexOf('_')));
//
//		if (!filesMap.containsKey(seqNum)) {
//
//			filesMap.put(seqNum, new ArrayList<File>());
//
//		}
//
//		filesMap.get(seqNum).add(file);
//
//	}
//
//	List<String> targetFileList = new ArrayList<>();
//	ArrayList<String> finalLinks = new ArrayList<String>();
//	List<String> targetSuccessFileList = new ArrayList<>();
//	ArrayList<String> links = new ArrayList<String>();
//	ArrayList<String> links2 = new ArrayList<String>();
//	List<String> targetFailedFileList = new ArrayList<>();
//	String firstimagelink = null;
//	String TName = fetchMetadataListVO.get(0).getTest_run_name();
//	for (Entry<Integer, List<File>> seqEntry : filesMap.entrySet()) {
//
//		List<File> seqList = seqEntry.getValue();
//
//		Collections.sort(seqList, new Comparator<File>() {
//
//			public int compare(File f1, File f2) {
//
//				return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified()) * -1;
//
//			}
//
//		});
//
//		List<String> seqFileNameList = new ArrayList<String>();
//		ArrayList<String> links1 = new ArrayList<String>();
//
//		ArrayList<String> linksall = new ArrayList<String>();
//
//		File file = new ClassPathResource(whiteimage).getFile();
//		File file1 = new ClassPathResource(watsvediologo).getFile();
//		// File file = new File("C:\\Users\\Winfo
//		// Solutions\\Desktop\\Add_On\\white.jpg");
//		// File file1=new File("C:\\Users\\Winfo
//		// Solutions\\Desktop\\Add_On\\WATS_LOGO.JPG");
//		BufferedImage image = null;
//		image = ImageIO.read(file);
//		BufferedImage logo = null;
//		logo = ImageIO.read(file1);
//		Graphics g = image.getGraphics();
//		g.setColor(Color.black);
//		java.awt.Font font = new java.awt.Font("Calibri", java.awt.Font.PLAIN, 36);
//		g.setFont(font);
//
//		String details = seqList.get(0).getName();
//		String ScriptNumber = details.split("_")[3];
//		String TestRun = details.split("_")[4];
//		String Status = details.split("_")[6];
//		String status = Status.split("\\.")[0];
//		String Scenario = details.split("_")[2];
//		String imagename = TestRun + ScriptNumber;
//		// String TName = fetchMetadataListVO.get(0).getTest_run_name();
//		Date endtime = fetchConfigVO.getEndtime();
//		Date TStarttime = fetchConfigVO.getStarttime1();
//		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//		String TStarttime1 = dateFormat.format(TStarttime);
////Changed the executed by variable
//		String ExeBy = fetchMetadataListVO.get(0).getExecuted_by();
//		String endtime1 = dateFormat.format(endtime);
//		long Tdiff = endtime.getTime() - TStarttime.getTime();
//		long TdiffSeconds = Tdiff / 1000 % 60;
//		long TdiffMinutes = Tdiff / (60 * 1000) % 60;
//		long TdiffHours = Tdiff / (60 * 60 * 1000);
//		String ExecutionTime = TdiffHours + ":" + TdiffMinutes + ":" + TdiffSeconds;
//		g.drawString("TEST SCRIPT DETAILS", 450, 50);
//		g.drawString("Test Run Name : " + TName, 50, 125);
//		g.drawString("Script Number : " + ScriptNumber, 50, 200);
//		g.drawString("Test Case Name :" + Scenario, 50, 275);
//		g.drawString("Status : " + status, 50, 350);
//		g.drawString("Executed By :" + ExeBy, 50, 425);
//		g.drawImage(logo, 1012, 15, null);
//		// g.drawString("Start Time :"+TStarttime1, 50, 425);
//		// g.drawString("End Time :"+endtime1, 50, 500);
//		// g.drawString("Execution Time : "+ExecutionTime, 50, 575);
//		g.dispose();
//		File folder1 = new File(
//				fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + fetchMetadataListVO.get(0).getCustomer_name() + "/Images");
//		if (!folder1.exists()) {
//			System.out.println("creating directory: " + folder1.getName());
//			boolean result = false;
//			try {
//				folder1.mkdirs();
//				result = true;
//			} catch (SecurityException se) {
//				// handle it
//				System.out.println(se.getMessage());
//			}
//		} else {
//			System.out.println("Folder exist");
//		}
//
//		ImageIO.write(image, "jpg", new File(folder1 + "/" + imagename + ".jpg"));
//		// ImageIO.write(image, "jpg", new File("C:\\Users\\Winfo
//		// Solutions\\Desktop\\Add_On\\"+imagename+".jpg"));
//
//		BufferedImage image1 = null;
//		image1 = ImageIO.read(file);
//		Graphics g1 = image1.getGraphics();
//		g1.setColor(Color.red);
//		java.awt.Font font1 = new java.awt.Font("Calibri", java.awt.Font.PLAIN, 36);
//		g1.setFont(font1);
//		g1.drawString("FAILED IN THE NEXT STEP!!", 400, 300);
//		g1.drawImage(logo, 1012, 14, null);
//		g1.dispose();
//
//		BufferedImage image2 = null;
//		image2 = ImageIO.read(file);
//		Graphics g2 = image2.getGraphics();
//		g2.setColor(Color.black);
//		g2.setFont(font);
//		g2.drawString("TEST RUN SUMMARY", 450, 50);
//		g2.drawString("Test Run Name : " + TName, 50, 125);
//		g2.drawString("Executed By :" + ExeBy, 50, 200);
//		g2.drawString("Start Time :" + TStarttime1, 50, 275);
//		g2.drawString("End Time :" + endtime1, 50, 350);
//		g2.drawString("Execution Time : " + ExecutionTime, 50, 425);
//		g2.drawImage(logo, 1012, 15, null);
//		g2.dispose();
//		ImageIO.write(image2, "jpg", new File(folder1 + "/first.jpg"));
//		// ImageIO.write(image2, "jpg", new File("C:\\Users\\Winfo
//		// Solutions\\Desktop\\Add_On\\first.jpg"));
//		String imgpath2 = folder1 + "/";
//		// String imgpath2 ="C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\";
//		String imgpath3 = folder1 + "/first.jpg";
//		// String imgpath3 ="C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\first.jpg";
//		File f11 = new File(imgpath2);
//		File[] f22 = f11.listFiles();
//		File f44 = new File(imgpath3);
//		firstimagelink = f44.getAbsolutePath();
//
//		if (!seqList.get(0).getName().endsWith("Failed.jpg")) {
//			passcount++;
//			for (File f33 : f22) {
//				if (f33.getAbsolutePath().contains(imagename)) {
//					linksall.add(f33.getAbsolutePath());
//				}
//			}
//			links1.add(seqList.get(0).getAbsolutePath());
//
//			seqFileNameList.add(seqList.get(0).getName());
//
////		             	                      System.out.println("FIRST S STEP: "+seqList.get(0).getName());
//
//			for (int i = 1; i < seqList.size(); i++) {
//
//				if (!seqList.get(i).getName().endsWith("Failed.jpg")) {
//					links1.add(seqList.get(i).getAbsolutePath());
//
//					seqFileNameList.add(seqList.get(i).getName());
//
////		                                                                 System.out.println("S STEP: "+seqList.get(i).getName());
//
//				} else {
//
//				}
//
//			}
//			links1.add(linksall.get(0));
//			Collections.reverse(links1);
//			Collections.reverse(seqFileNameList);
//			links.addAll(links1);
//			targetSuccessFileList.addAll(seqFileNameList);
//
//		} else {
//			failcount++;
//			for (File f33 : f22) {
//				if (f33.getAbsolutePath().contains(imagename)) {
//					linksall.add(f33.getAbsolutePath());
//					linksall.set(0, f33.getAbsolutePath());
//				}
//				if (f33.getAbsolutePath().contains("last")) {
//					linksall.add(f33.getAbsolutePath());
//					linksall.add(f33.getAbsolutePath());
//					linksall.set(1, f33.getAbsolutePath());
//
//				}
//			}
////		                                   System.out.println("SEQ : "+seqEntry.getKey());
//			links1.add(seqList.get(0).getAbsolutePath());
//			links1.add(linksall.get(1));
////		                                   System.out.println("SEQ : "+seqEntry.getKey());
//
//			seqFileNameList.add(seqList.get(0).getName());
//
////		                                   System.out.println("FIRST F STEP: "+seqList.get(0).getName());
//
//			for (int i = 1; i < seqList.size(); i++) {
//
//				if (!seqList.get(i).getName().endsWith("Failed.jpg")) {
//					links1.add(seqList.get(i).getAbsolutePath());
//
//					seqFileNameList.add(seqList.get(i).getName());
//
////		                                                                 System.out.println("F STEP: "+seqList.get(i).getName());
//
//				} else {
//
//				}
//
//			}
//
//			links1.add(linksall.get(0));
//			Collections.reverse(links1);
//			Collections.reverse(seqFileNameList);
//			links2.addAll(links1);
//			targetFailedFileList.addAll(seqFileNameList);
//
//		}
//
////		                    targetFileList.addAll(seqList);
//
//	}
//
//	finalLinks.addAll(links);
//	finalLinks.addAll(links2);
//	targetFileList.addAll(targetSuccessFileList);
//
//	targetFileList.addAll(targetFailedFileList);
//
//	/*
//	 * for (String fileName : targetFileList) {
//	 * 
//	 * System.out.println("Target File : " + fileName);
//	 * 
//	 * }
//	 */
//	fetchConfigVO.setPasscount(passcount);
//	fetchConfigVO.setFailcount(failcount);
//	
//	return targetFileList;
//}
//public List<String> getFileNameListNew(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO)
//		throws IOException {
//
//	File folder = new File(fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
//			+ fetchMetadataListVO.get(0).getTest_run_name() + "/");
//	// File folder = new File("C:\\\\Users\\\\Winfo Solutions\\\\Desktop\\\\test");
//
//	File[] listOfFiles = folder.listFiles();
////	String video_rec=fetchConfigVO.getEnable_video();
//	String video_rec = "no";
////	List<File> fileList = Arrays.asList(listOfFiles);
//	List<File> allFileList = Arrays.asList(listOfFiles);
//	List<File> fileList = new ArrayList<>();
//	String seqNumber = fetchMetadataListVO.get(0).getSeq_num();
//	// String seqNumber = "1";
//	for (File file : allFileList) {
//		if (file.getName().startsWith(seqNumber + "_")) {
//			fileList.add(file);
//		}
//	}
//
//	Collections.sort(fileList, new Comparator<File>() {
//
//		public int compare(File f1, File f2) {
//
//			return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified()) * -1;
//
//		}
//
//	});
//
//	List<String> fileNameList = new ArrayList<String>();
//	ArrayList<String> linksall = new ArrayList<String>();
//	ArrayList<String> links1 = new ArrayList<String>();
//	File file = new ClassPathResource(whiteimage).getFile();
//	// File file = new File("C:\\Users\\Winfo
//	// Solutions\\Desktop\\Add_On\\white.jpg");
//	File file1 = new ClassPathResource(watsvediologo).getFile();
//	// File file1=new File("C:\\Users\\Winfo
//	// Solutions\\Desktop\\Add_On\\WATS_LOGO.JPG");
//
//	BufferedImage image = null;
//	image = ImageIO.read(file);
//	BufferedImage logo = null;
//	logo = ImageIO.read(file1);
//	Graphics g = image.getGraphics();
//	g.setColor(Color.black);
//	java.awt.Font font = new java.awt.Font("Calibri", java.awt.Font.PLAIN, 36);
//	g.setFont(font);
//	String details = fileList.get(0).getName();
//	// String details= seqList.get(0).getName();
//	String ScriptNumber = details.split("_")[3];
//	String TestRun = details.split("_")[4];
//	String Status = details.split("_")[6];
//	String status = Status.split("\\.")[0];
//	String Scenario = details.split("_")[2];
//	String imagename = TestRun + ScriptNumber;
//	String TName = fetchMetadataListVO.get(0).getTest_run_name();
//	String no = details.split("_")[0];
//	Date Starttime = fetchConfigVO.getStarttime();
//	Date endtime = fetchConfigVO.getEndtime();
//	DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//	String Starttime1 = dateFormat.format(Starttime);
////Changed the executed by variable
//	String ExeBy = fetchMetadataListVO.get(0).getExecuted_by();
//	String endtime1 = dateFormat.format(endtime);
//	long diff = endtime.getTime() - Starttime.getTime();
//	long diffSeconds = diff / 1000 % 60;
//	long diffMinutes = diff / (60 * 1000) % 60;
//	long diffHours = diff / (60 * 60 * 1000);
//	String ExecutionTime = diffHours + ":" + diffMinutes + ":" + diffSeconds;
//	g.drawString("TEST SCRIPT DETAILS", 450, 50);
//	g.drawString("Test Run Name : " + TName, 50, 100);
//	g.drawString("Script Number : " + ScriptNumber, 50, 150);
//	g.drawString("Test Case Name :" + Scenario, 50, 200);
//	g.drawString("Status : " + status, 50, 250);
//	g.drawString("Executed By :" + ExeBy, 50, 300);
//	g.drawString("Start Time :" + Starttime1, 50, 350);
//	g.drawString("End Time :" + endtime1, 50, 400);
//	g.drawString("Execution Time : " + ExecutionTime, 50, 450);
//	g.drawImage(logo, 1012, 15, null);
//	g.dispose();
//	File folder1 = new File(
//			fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + fetchMetadataListVO.get(0).getCustomer_name() + "/Images");
//	if (!folder1.exists()) {
//		System.out.println("creating directory: " + folder1.getName());
//		boolean result = false;
//		try {
//			folder1.mkdirs();
//			result = true;
//		} catch (SecurityException se) {
//			// handle it
//			System.out.println(se.getMessage());
//		}
//	} else {
//		System.out.println("Folder exist");
//	}
//
//	ImageIO.write(image, "jpg", new File(folder1 + "/first.jpg"));
//	// ImageIO.write(image, "jpg", new File("C:\\Users\\Winfo
//	// Solutions\\Desktop\\Add_On\\first.jpg"));
//
//	BufferedImage image1 = null;
//	image1 = ImageIO.read(file);
//	Graphics g1 = image1.getGraphics();
//	g1.setColor(Color.red);
//	java.awt.Font font1 = new java.awt.Font("Calibir", java.awt.Font.PLAIN, 36);
//	g1.setFont(font1);
//	g1.drawString("FAILED IN THE NEXT STEP!!", 400, 300);
//	g1.drawImage(logo, 1150, 15, null);
//	g1.dispose();
//	ImageIO.write(image1, "jpg", new File(folder1 + "/last.jpg"));
//	// ImageIO.write(image1, "jpg", new File("C:\\Users\\Winfo
//	// Solutions\\Desktop\\Add_On\\last.jpg"));
//	String imgpath2 = folder1 + "/";
//	// String imgpath2 ="C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\";
//	File f11 = new File(imgpath2);
//	File[] f22 = f11.listFiles();
//
//	if (!fileList.get(0).getName().endsWith("Failed.jpg")) {
//		for (File f33 : f22) {
//			if (f33.getAbsolutePath().contains("first")) {
//				linksall.add(f33.getAbsolutePath());
//			}
//		}
//		fetchConfigVO.setStatus1("Pass");
//		fileNameList.add(fileList.get(0).getName());
//		links1.add(fileList.get(0).getAbsolutePath());
//		for (int i = 1; i < fileList.size(); i++) {
//
//			if (!fileList.get(i).getName().endsWith("Failed.jpg")) {
//				links1.add(fileList.get(i).getAbsolutePath());
//
//				fileNameList.add(fileList.get(i).getName());
//
//			} else {
//
//			}
//
//		}
//
//		links1.add(linksall.get(0));
//		Collections.reverse(links1);
//		Collections.reverse(fileNameList);
//
//	}
//
//	// targetFileList.addAll(seqList);
//
//	/*
//	 * for (String fileName : fileNameList) {
//	 * 
//	 * System.out.println("Target File : " + fileName);
//	 * 
//	 * }
//	 */
//	
//	return fileNameList;
//
//}
//
//public FetchConfigVO getFetchConfigVO(String parameter) {
//	JSONParser jsonParser = new JSONParser();
////	 String uri = "https://watshubd01.winfosolutions.com:4443/wats/wats_workspace_prod/CONFIG_GET/data";
//
//	final String uri = config_url + parameter;
//
////	                          final String uri = "https://watshubd01.winfosolutions.com:4443/wats/wats_workspace_prod/taconfig/data/" + parameter;
//	try {
//	//	System.out.println(uri);
//
//		RestTemplate restTemplate = new RestTemplate();
//
//		//System.out.println(restTemplate);
//
//		String result = restTemplate.getForObject(uri, String.class);
//
//	//	System.out.println(result);
//
//		JSONObject obj = (JSONObject) jsonParser.parse(result);
//
//		//System.out.println(restTemplate);
//
//		JSONArray employeeList = (JSONArray) obj.get("items");
//		//System.out.println(employeeList);
//		Map<String, String> map = new TreeMap<>();
//		// Iterate over employee array
//		employeeList.forEach(emp -> parseEmployeeObject((JSONObject) emp, map));
//		JSONObject jsno = new JSONObject(map);
//		Gson g = new Gson();
//		FetchConfigVO vo = g.fromJson(jsno.toString(), FetchConfigVO.class);
//	//	System.out.println(jsno.toString());
//		return vo;
//	} catch (Exception e) {
//		e.printStackTrace();
//	}
//	return null;
//}
//
//public void parseEmployeeObject(JSONObject employee, Map<String, String> map) {
//	// Get employee object within list
////        JSONObject employeeObject = (JSONObject) employee.get("items");
//
//	// Get employee first name
//	String firstName = (String) employee.get("key_name");
////	System.out.println(firstName);
//
//	// Get employee last name
//	String lastName = (String) employee.get("value_name");
//	//System.out.println(lastName);
//	map.put(firstName, lastName);
//}
//public   List<FetchMetadataVO> getMetaDataVOList( String testRunId) throws ClassNotFoundException, SQLException {
//    // Added try catch blocks
//	Connection conn=null;
//	Statement st=null;
//	 List<FetchMetadataVO> listOfFetchMetadatavO= new ArrayList<>();
//	try {
//    Class.forName("oracle.jdbc.driver.OracleDriver");
//    conn = DriverManager.getConnection(fetchConfigVO.getDb_host(), fetchConfigVO.getDb_username(),
//    		dbPassword);
//    st = conn.createStatement();
//   // String sqlQuery="Update WATS_PROD.WIN_TA_TEST_SET_SCRIPT_PARAM  SET LINE_EXECUTION_STATUS='Pass' where TEST_SCRIPT_PARAM_ID='"+test_script_param_id+"'";
//    
//    String sqlQuery = "SELECT wtp.customer_id,\r\n"
//			+ "           wtc.customer_number,\r\n"
//			+ "           wtc.customer_name,\r\n"
//			+ "           wtts.project_id,\r\n"
//			+ "           wtp.project_name,\r\n"
//			+ "           wttsl.test_set_id,\r\n"
//			+ "           wttsl.test_set_line_id,\r\n"
//			+ "           wtsmdata.script_id,\r\n"
//			+ "           wtsmdata.script_number,\r\n"
//			+ "           wtsmdata.line_number,\r\n"
//			+ "           CASE WHEN INSTR(wtsmdata.input_parameter,')',1,1) >4\r\n"
//			+ "               THEN wtsmdata.input_parameter\r\n"
//			+ "               ELSE regexp_replace( wtsmdata.input_parameter, '[*#()]', '') END input_parameter,\r\n"
//			+ "           wtsmdata.input_value,\r\n"
//			+ "           wtsmdata.action,\r\n"
//			+ "           wtsmdata.xpath_location,\r\n"
//			+ "           wtsmdata.xpath_location1,\r\n"
//			+ "           wtsmdata.field_type,\r\n"
//			+ "           wtsmdata.hint,\r\n"
//			+ "           ma.SCENARIO_NAME,\r\n"
//			+ "    decode(ma.dependency, null, 'N', 'Y') dependency\r\n"
//			+ "          ,wtts.TEST_SET_NAME test_run_name, wttsl.SEQ_NUM\r\n"
//			+ "          ,wtsmdata.TEST_SCRIPT_PARAM_ID\r\n"
//			+ "          ,ex_st.EXECUTED_BY    EXECUTED_BY\r\n"
//			+ "      from\r\n"
//			+ "      execute_status ex_st,\r\n"
//			+ "      win_ta_test_set        wtts,\r\n"
//			+ "    win_ta_script_master ma,\r\n"
//			+ "           win_ta_test_set_lines  wttsl,\r\n"
//			+ "           win_ta_test_set_script_param wtsmdata,\r\n"
//			+ "           win_ta_projects        wtp,\r\n"
//			+ "           win_ta_customers       wtc\r\n"
//			+ "     WHERE 1=1\r\n"
//			+ "     AND wtts.TEST_SET_ID = EX_ST.TEST_RUN_ID(+)\r\n"
//			+ "    and ma.script_id = wttsl.script_id\r\n"
//			+ "    and ma.script_number = wttsl.script_number\r\n"
//			+ "      -- AND wtts.test_set_id = :p_test_set_id\r\n"
//			+ "       AND wttsl.test_set_id = wtts.test_set_id\r\n"
//			+ "       AND wttsl.script_id = wtsmdata.script_id\r\n"
//			+ "       AND wtsmdata.test_set_line_id =wttsl.test_set_line_id\r\n"
//			+ "       AND wtts.project_id = wtp.project_id\r\n"
//			+ "       AND wtp.customer_id = wtc.customer_id\r\n"
//			+ "       AND wtts.test_set_id="+testRunId+"\r\n"
//			+ "      and  (upper(status) in ('IN-PROGRESS', 'NEW','FAIL','IN-QUEUE','STOPPED'))\r\n"
//			+ "      and wttsl.enabled = 'Y'\r\n"
//			+ "       order by\r\n"
//			+ "       wttsl.SEQ_NUM,\r\n"
//			+ "         -- wtsmdata.script_number,\r\n"
//			+ "          wttsl.script_id,\r\n"
//			+ "          wtsmdata.line_number asc";
//    
//    st.executeQuery(sqlQuery);
//ResultSet rs=st.executeQuery(sqlQuery);
//  String NULL_STRING="null";
// 
//    while(rs.next()){
//    	FetchMetadataVO testRunExecutionVO= new FetchMetadataVO();
//		
//		testRunExecutionVO.setCustomer_id(NULL_STRING.equals(String.valueOf(rs.getObject(0)))? null:String.valueOf(rs.getObject(0)));
//		testRunExecutionVO.setCustomer_number(NULL_STRING.equals(String.valueOf(rs.getObject(1)))? null:String.valueOf(rs.getObject(1)));
//		testRunExecutionVO.setCustomer_name( NULL_STRING.equals(String.valueOf(rs.getObject(2)))? null:String.valueOf(rs.getObject(2)));
//		testRunExecutionVO.setProject_id(NULL_STRING.equals(String.valueOf(rs.getObject(3)))? null:String.valueOf(rs.getObject(3)));
//		testRunExecutionVO.setProject_name(  NULL_STRING.equals(String.valueOf(rs.getObject(4)))? null:String.valueOf(rs.getObject(4)));
//	//	testRunExecutionVO.setTestRunId(NULL_STRING.equals(String.valueOf(rs.getObject(5)))? null:String.valueOf(rs.getObject(5)));
//		testRunExecutionVO.setTest_set_line_id(NULL_STRING.equals(String.valueOf(rs.getObject(6)))? null:String.valueOf(rs.getObject(6)));
//		testRunExecutionVO.setScript_id(NULL_STRING.equals(String.valueOf(rs.getObject(7)))? null:String.valueOf(rs.getObject(7)));
//		testRunExecutionVO.setScript_number( NULL_STRING.equals(String.valueOf(rs.getObject(8)))? null:String.valueOf(rs.getObject(8)));
//		testRunExecutionVO.setLine_number(NULL_STRING.equals(String.valueOf(rs.getObject(9)))? null:String.valueOf(rs.getObject(9)));
//		testRunExecutionVO.setInput_parameter( NULL_STRING.equals(String.valueOf(rs.getObject(10)))? null:String.valueOf(rs.getObject(10)));
//		testRunExecutionVO.setInput_value( NULL_STRING.equals(String.valueOf(rs.getObject(11)))? null:String.valueOf(rs.getObject(11)));
//		testRunExecutionVO.setAction( NULL_STRING.equals(String.valueOf(rs.getObject(12)))? null:String.valueOf(rs.getObject(12)));
//		testRunExecutionVO.setXpath_location( NULL_STRING.equals(String.valueOf(rs.getObject(13)))? null:String.valueOf(rs.getObject(13)));
//		testRunExecutionVO.setXpath_location1( NULL_STRING.equals(String.valueOf(rs.getObject(14)))? null:String.valueOf(rs.getObject(14)));
//		testRunExecutionVO.setField_type( NULL_STRING.equals(String.valueOf(rs.getObject(15)))? null:String.valueOf(rs.getObject(15)));
//		//testRunExecutionVO.setHint( NULL_STRING.equals(String.valueOf(rs.getObject(16)))? null:String.valueOf(rs.getObject(16)));
//		testRunExecutionVO.setScenario_name(  NULL_STRING.equals(String.valueOf(rs.getObject(17)))? null:String.valueOf(rs.getObject(17)));
//		testRunExecutionVO.setDependency( NULL_STRING.equals(String.valueOf(rs.getObject(18)))? null:String.valueOf(rs.getObject(18)));
//		testRunExecutionVO.setTest_run_name( NULL_STRING.equals(String.valueOf(rs.getObject(19)))? null:String.valueOf(rs.getObject(19)));
//		testRunExecutionVO.setSeq_num(NULL_STRING.equals(String.valueOf(rs.getObject(20)))? null:String.valueOf(rs.getObject(20)));
//		testRunExecutionVO.setTest_script_param_id(NULL_STRING.equals(String.valueOf(rs.getObject(21)))? null:String.valueOf(rs.getObject(21)));
//		testRunExecutionVO.setExecuted_by( NULL_STRING.equals(String.valueOf(rs.getObject(22)))? null:String.valueOf(rs.getObject(22)));
//		listOfFetchMetadatavO.add(testRunExecutionVO);
//    }
//    }
//    catch (Exception e) {
//		System.out.println(e);
//	}finally {
//		conn.close();
//		 st.close();
//    }
//	return  listOfFetchMetadatavO;
//}
//public LinkedHashMap<String, List<FetchMetadataVO>> prepareTestcasedata(List<FetchMetadataVO> list,LinkedHashMap<String, List<FetchMetadataVO>> dependentScriptMap) {
//
//	LinkedHashMap<String, List<FetchMetadataVO>> testCaseMap = new LinkedHashMap<String, List<FetchMetadataVO>>();
//
//	//dependentScriptMap = new LinkedHashMap<String, List<FetchMetadataVO>>();
//
//	if (list != null) {
//
//		for (FetchMetadataVO testcase : list) {
//
//			String test_line_id = testcase.getTest_set_line_id();
//
//			String dependency = testcase.getDependency();
//
//			if (test_line_id != null && "N".equalsIgnoreCase(dependency)) {
//
//				prepareTestData(testCaseMap, testcase, test_line_id);
//
//			} else {
//
//				prepareTestData(dependentScriptMap, testcase, test_line_id);
//
//			}
//
//		}
//
//	}
//
//	System.out.println(testCaseMap);
//
//	return testCaseMap;
//
//}
//
///*
// * 
// * public LinkedHashMap<String, List<FetchMetadataVO>> getDependentScriptMap() {
// * 
// * return dependentScriptMap;
// * 
// * }
// */ 
//private void prepareTestData(LinkedHashMap<String, List<FetchMetadataVO>> testCaseMap, FetchMetadataVO testcase,
//		String test_line_id) {
//
//	if (testCaseMap.containsKey(test_line_id)) {
//
//		List<FetchMetadataVO> testcasedata = testCaseMap.get(test_line_id);
//
//		testcasedata.add(testcase);
//
//		testCaseMap.put(test_line_id, testcasedata);
//
//	} else {
//
//		List<FetchMetadataVO> testcasedata = new ArrayList<FetchMetadataVO>();
//
//		testcasedata.add(testcase);
//
//		testCaseMap.put(test_line_id, testcasedata);
//
//	}
//
//}
//public void createFailedPdf(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO,
//		String pdffileName, Date Starttime, Date endtime)
//		throws IOException, DocumentException, com.itextpdf.text.DocumentException {
//	try {
//		String Date = DateUtils.getSysdate();
//		String Folder = (fetchConfigVO.getWINDOWS_PDF_LOCATION()  + fetchMetadataListVO.get(0).getCustomer_name() + "/"
//				+ fetchMetadataListVO.get(0).getTest_run_name() + "/");
//		String FILE = (Folder + pdffileName);
//		System.out.println(FILE);
//		List<String> fileNameList = null;
//		if ("Passed_Report.pdf".equalsIgnoreCase(pdffileName)) {
////			fileNameList = getPassedPdfNew(fetchMetadataListVO, fetchConfigVO);
//		} else if ("Failed_Report.pdf".equalsIgnoreCase(pdffileName)) {
////			fileNameList = getFailedPdfNew(fetchMetadataListVO, fetchConfigVO);
//		}
//		if ("Detailed_Report.pdf".equalsIgnoreCase(pdffileName)) {
//			fileNameList = getDetailPdfNew(fetchMetadataListVO, fetchConfigVO);
//		} else {
//			fileNameList = getFailFileNameListNew(fetchMetadataListVO, fetchConfigVO);
//		}
//
//		String Script_Number = fetchMetadataListVO.get(0).getScript_number();
//		String customer_Name = fetchMetadataListVO.get(0).getCustomer_name();
//		String test_Run_Name = fetchMetadataListVO.get(0).getTest_run_name();
//		String Scenario_Name = fetchMetadataListVO.get(0).getScenario_name();
//		// new change add ExecutedBy field
//		String ExecutedBy = fetchMetadataListVO.get(0).getExecuted_by();
//		String ScriptDescription1 = fetchMetadataListVO.get(0).getScenario_name();
//		File theDir = new File(Folder);
//		if (!theDir.exists()) {
//			System.out.println("creating directory: " + theDir.getName());
//			boolean result = false;
//			try {
//				theDir.mkdirs();
//				result = true;
//			} catch (SecurityException se) {
//				// handle it
//				System.out.println(se.getMessage());
//			}
//		} else {
//			System.out.println("Folder exist");
//		}
//		Font bf12 = FontFactory.getFont("Arial", 23);
//		System.out.println("before enter Images/wats_icon.png");
//		Image img1 = Image.getInstance(watslogo);
//		System.out.println("after enter Images/wats_icon.png");
//		img1.scalePercent(65, 68);
//		img1.setAlignment(Image.ALIGN_RIGHT);
//		Font bfBold12 = FontFactory.getFont("Arial", 23);
//		String Report = "Execution Report";
//		Font fnt = FontFactory.getFont("Arial", 12);
//		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:aa");
//		String Starttime1 = dateFormat.format(Starttime);
//		String endtime1 = dateFormat.format(endtime);
//		long diff = endtime.getTime() - Starttime.getTime();
//		long diffSeconds = diff / 1000 % 60;
//		long diffMinutes = diff / (60 * 1000) % 60;
//		long diffHours = diff / (60 * 60 * 1000);
//		Document document = new Document();
//		PdfWriter.getInstance(document, new FileOutputStream(FILE));
//		Rectangle one = new Rectangle(1360, 800);
//		document.setPageSize(one);
//		document.open();
//		String TestRun = test_Run_Name;
//		String ScriptNumber = Script_Number;
//		String error = fetchConfigVO.getErrormessage();
//		String ScriptNumber1 = Scenario_Name;
//		String Scenario1 = fetchConfigVO.getStatus1();
////		String ExecutedBy=fetchConfigVO.getApplication_user_name();
//		String StartTime = Starttime1;
//		String EndTime = endtime1;
//		String ExecutionTime = diffHours + ":" + diffMinutes + ":" + diffSeconds;
//
//		String TR = "Test Run Name";
//		String SN = "Script Number";
//		String SN1 = "Test Case Name";
//		String Scenarios1 = "Status ";
//		String showErrorMessage = "	ErrorMessage ";
//		String EB = "Executed By";
//		String ST = "Start Time";
//		String ET = "End Time";
//		String EX = "Execution Time";
//
//		document.add(img1);
//
//		document.add(new Paragraph(Report, bfBold12));
//		document.add(Chunk.NEWLINE);
//		PdfPTable table1 = new PdfPTable(2);
//		table1.setWidths(new int[] { 1, 1 });
//		table1.setWidthPercentage(100f);
//
//		insertCell(table1, TR, Element.ALIGN_LEFT, 1, bf12);
//		insertCell(table1, TestRun, Element.ALIGN_LEFT, 1, bf12);
//		insertCell(table1, SN, Element.ALIGN_LEFT, 1, bf12);
//		insertCell(table1, ScriptNumber, Element.ALIGN_LEFT, 1, bf12);
//		insertCell(table1, SN1, Element.ALIGN_LEFT, 1, bf12);
//		insertCell(table1, ScriptNumber1, Element.ALIGN_LEFT, 1, bf12);
//		insertCell(table1, Scenarios1, Element.ALIGN_LEFT, 1, bf12);
//		insertCell(table1, Scenario1, Element.ALIGN_LEFT, 1, bf12);
//		insertCell(table1, showErrorMessage, Element.ALIGN_LEFT, 1, bf12);
//		insertCell(table1, error, Element.ALIGN_LEFT, 1, bf12);
//		insertCell(table1, EB, Element.ALIGN_LEFT, 1, bf12);
//		insertCell(table1, ExecutedBy, Element.ALIGN_LEFT, 1, bf12);
//		insertCell(table1, ST, Element.ALIGN_LEFT, 1, bf12);
//		insertCell(table1, StartTime, Element.ALIGN_LEFT, 1, bf12);
//		insertCell(table1, ET, Element.ALIGN_LEFT, 1, bf12);
//		insertCell(table1, EndTime, Element.ALIGN_LEFT, 1, bf12);
//		insertCell(table1, EX, Element.ALIGN_LEFT, 1, bf12);
//		insertCell(table1, ExecutionTime, Element.ALIGN_LEFT, 1, bf12);
//		document.add(table1);
//		document.newPage();
////End to add Script level details
////			Start to add screenshoots and pagenumbers and wats icon		 		
//		int i = 0;
//		for (String image : fileNameList) {
//			i++;
//			Image img = Image.getInstance(
//					fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + customer_Name + "/" + test_Run_Name + "/" + image);
//
////					String ScriptNumber = image.split("_")[3];
////					String TestRun = image.split("_")[4];
//			String Status = image.split("_")[6];
//			String status = Status.split("\\.")[0];
//			String Scenario = image.split("_")[2];
//
//			if (status.equalsIgnoreCase("Failed")) {// Rectangle one2 = new Rectangle(1360,1000);
//				document.setPageSize(one);
//				document.newPage();
//			} else {
//
//				document.setPageSize(img);
//				document.newPage();
//			}
//
//			document.add(img1);
//			String Reason = image.split("_")[5];
//			// String TR = "Test Run Name:" + " " + TestRun;
////					String SN = "Script Number:" + " " + ScriptNumber;
//			String S = "Status:" + " " + status;
//			String step = "Step No :" + "" + Reason;
//			String Scenarios = "Test Case Name :" + "" + Scenario;
//			String Message = "Failed at Line Number:" + "" + Reason;
//			String errorMessage = "Failed Message:" + "" + fetchConfigVO.getErrormessage();
//			// String message = "Failed at
//			// :"+fetchMetadataListVO.get(0).getInput_parameter();
////					document.add(new Paragraph(TR, fnt));
////					document.add(new Paragraph(SN, fnt));
//			document.add(new Paragraph(S, fnt));
//			document.add(new Paragraph(Scenarios, fnt));
////new change-failed pdf to add pagesize
//			if (status.equalsIgnoreCase("Failed")) {
//				document.add(new Paragraph(Message, fnt));
//				if (fetchConfigVO.getErrormessage() != null) {
//					document.add(new Paragraph(errorMessage, fnt));
//				}
//				document.add(Chunk.NEWLINE);
//				img.setAlignment(Image.ALIGN_CENTER);
//				img.isScaleToFitHeight();
//				// new change-change page size
//				img.scalePercent(60, 58);
//				document.add(img);
//			} else {
//				document.add(new Paragraph(step, fnt));
//				document.add(Chunk.NEWLINE);
//				img.setAlignment(Image.ALIGN_CENTER);
//				img.isScaleToFitHeight();
//				// new change-change page size
//				img.scalePercent(60, 62);
//				document.add(img);
//			}
//
//			Paragraph p = new Paragraph(String.format("page %s of %s", i, fileNameList.size()));
//			p.setAlignment(Element.ALIGN_RIGHT);
//
//			document.add(p);
//			System.out.println("This Image " + "" + image + "" + "was added to the report");
////			End to add screenshoots and pagenumbers and wats icon
//			// End to create Script level passed reports
//
//		}
//		document.close();
//
//	} catch (Exception e) {
//		System.out.println("Not able to upload the pdf");
//		e.printStackTrace();
//	}
//}
//
////public void getListOfImages(FetchConfigVO fetchConfigVO)
////{
////	List<String> fileNameList = new ArrayList<String>();
////	  File folder = new File(fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + fetchMetadataListVO.get(0).getCustomer_name() + "\\"
////				+ fetchMetadataListVO.get(0).getTest_run_name() + "\\");
////
////		File[] listOfFiles = folder.listFiles();
////		List<File> allFileList = Arrays.asList(listOfFiles);
////		List<File> fileList = new ArrayList<>();
////		String seqNumber = fetchMetadataListVO.get(0).getSeq_num();
////		// String seqNumber = "1";
////		for (File file : allFileList) {
////			if (file.getName().startsWith(seqNumber + "_")) {
////				fileList.add(file);
////			}
////		}
////
////		Collections.sort(fileList, new Comparator<File>() {
////
////			public int compare(File f1, File f2) {
////
////				return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified()) * -1;
////
////			}
////
////		});
////		for(File f:fileList) {
////		fileNameList.add(f.getName());
////		}
////
////}
//}
