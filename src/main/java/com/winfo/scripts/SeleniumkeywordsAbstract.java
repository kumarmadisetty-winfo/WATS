package com.winfo.scripts;

import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
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
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.FileImageOutputStream;

import org.apache.log4j.Logger;
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
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;

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
import com.winfo.services.FetchConfigVO;
import com.winfo.services.FetchMetadataVO;
import com.winfo.services.ScriptXpathService;
import com.winfo.utils.DateUtils;

public abstract class SeleniumkeywordsAbstract implements SeleniumKeyWordsInterface{
	@Autowired
	private DataBaseEntry  databaseentry;
	@Autowired
	ScriptXpathService service;
	Logger log = Logger.getLogger("Logger");

	public void createPdf(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO, String pdffileName,
			Date Starttime, Date endtime) throws IOException, DocumentException, com.itextpdf.text.DocumentException {
		try {
			String Date = DateUtils.getSysdate();
			String Folder = (fetchConfigVO.getPdf_path() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
					+ fetchMetadataListVO.get(0).getTest_run_name() + "/");
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
			Image img1 = Image.getInstance("/u01/oracle/selenium/apache-tomcat-8.5.60/webapps/wats/WEB-INF/classes/Images/wats_icon.png");
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
				String Folder = (fetchConfigVO.getPdf_path() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
						+ fetchMetadataListVO.get(0).getTest_run_name() + "/");
				String FILE = (Folder + pdffileName);
				System.out.println(FILE);
				List<String> fileNameList = null;
				if ("Passed_Report.pdf".equalsIgnoreCase(pdffileName)) {
//					fileNameList = getPassedPdfNew(fetchMetadataListVO, fetchConfigVO);
				} 
				else if ("Failed_Report.pdf".equalsIgnoreCase(pdffileName)) {
//					fileNameList = getFailedPdfNew(fetchMetadataListVO, fetchConfigVO);
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
					Image img1 = Image.getInstance("/u01/oracle/selenium/apache-tomcat-8.5.60/webapps/wats/WEB-INF/classes/Images/wats_icon.png");
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
	 public List<String> getPassedPdfNew(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO) throws Exception {

			File folder = new File(fetchConfigVO.getScreenshot_path() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
					+ fetchMetadataListVO.get(0).getTest_run_name() + "/");
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

				File file = new File("/u01/oracle/selenium/temp/VideoRecord/white.jpg");
				//File file = new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\white.jpg");
				File file1 = new File("/u01/oracle/selenium/temp/VideoRecord/WATS_LOGO.JPG");
				//File file1=new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\WATS_LOGO.JPG");

				 BufferedImage image = null;
			     image = ImageIO.read(file);
			     BufferedImage logo = null;
			     logo = ImageIO.read(file1);
			     Graphics g = image.getGraphics();
			     g.setColor(Color.black);
			     java.awt.Font font=new java.awt.Font("Calibri",  java.awt.Font.PLAIN, 36);
			     g.setFont(font);
			      
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
			     g.drawString("TEST SCRIPT DETAILS", 450, 50);
			     g.drawString("Test Run Name : " +  TName, 50, 125);
			     g.drawString("Script Number : " +  ScriptNumber, 50, 200);
			     g.drawString("Scenario Name :"+Scenario, 50, 275);
			     g.drawString("Status : "+status, 50, 350);
			     g.drawString("Executed By :"+ExeBy, 50, 425);
			     g.drawImage(logo,1012,15,null);
////			 g.drawString("Start Time :"+TStarttime1, 50, 425);
////			 g.drawString("End Time :"+endtime1, 50, 500);
////			 g.drawString("Execution Time : "+ExecutionTime, 50, 575);
			     g.dispose();
			     
			     BufferedImage image2 = null;
				 image2 = ImageIO.read(file);
				 Graphics g2 = image2.getGraphics();
				 g2.setColor(Color.black);
				 g2.setFont(font);
				 g2.drawString("TEST RUN SUMMARY", 450, 50);
			     g2.drawString("Test Run Name : " +  TName, 50, 125);
		         g2.drawString("Executed By :"+ExeBy, 50, 200);
		         g2.drawString("Start Time :"+TStarttime1, 50, 275);
		         g2.drawString("End Time :"+endtime1, 50, 350);
		         g2.drawString("Execution Time : "+ExecutionTime, 50,425);
		         g2.drawImage(logo,1012,15,null);
			     g2.dispose();
				 ImageIO.write(image2, "jpg", new File("/u01/oracle/selenium/temp/VideoRecord/first.jpg"));
				 //ImageIO.write(image2, "jpg", new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\first.jpg"));
				 String imgpath3 ="/u01/oracle/selenium/temp/VideoRecord/first.jpg";
				 String imgpath2 ="/u01/oracle/selenium/temp/VideoRecord/";
		         ImageIO.write(image, "jpg", new File("/u01/oracle/selenium/temp/VideoRecord/"+imagename+".jpg"));
				 //String imgpath3 ="C\\Users\\Winfo Solutions\\Desktop\\Add_On\\first.jpg";
				 //String imgpath2 ="C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\";
		         //ImageIO.write(image, "jpg", new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\"+imagename+".jpg"));
		        	        
		        File f11=new File(imgpath2);
		        File[] f22=f11.listFiles();
		        File f44=new File(imgpath3);
		        firstimagelink=f44.getAbsolutePath();

				
		        if (!seqList.get(0).getName().endsWith("Failed.jpg")) {
					passcount++;
					for(File f33:f22)
			        {
						if(f33.getAbsolutePath().contains(imagename)) {
							  linksall.add(f33.getAbsolutePath());
					        	}
			        }
					links1.add(seqList.get(0).getAbsolutePath());
					seqFileNameList.add(seqList.get(0).getName());

					for (int i = 1; i < seqList.size(); i++) {

						if (!seqList.get(i).getName().endsWith("Failed.jpg")) {
							links1.add(seqList.get(i).getAbsolutePath());
							seqFileNameList.add(seqList.get(i).getName());

						} else {

							

						}

					}

					links1.add(linksall.get(0));
					Collections.reverse(links1);
					Collections.reverse(seqFileNameList);
					links.addAll(links1);
					targetFileList.addAll(seqFileNameList);


				}

////	                    targetFileList.addAll(seqList);

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

		public List<String> getFailedPdfNew(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO) throws Exception {

			File folder = new File(fetchConfigVO.getScreenshot_path() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
					+ fetchMetadataListVO.get(0).getTest_run_name() + "/");
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

				File file = new File("/u01/oracle/selenium/temp/VideoRecord/white.jpg");
				//File file = new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\white.jpg");
				File file1 = new File("/u01/oracle/selenium/temp/VideoRecord/WATS_LOGO.JPG");
				//File file1=new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\WATS_LOGO.JPG");
				
		        BufferedImage image = null;
		        image = ImageIO.read(file);
		        BufferedImage logo = null;
		        logo = ImageIO.read(file1);
		        Graphics g = image.getGraphics();
		        g.setColor(Color.black);
		        java.awt.Font font=new java.awt.Font("Calibri",  java.awt.Font.PLAIN, 36);
		        g.setFont(font);
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

		        g.drawString("TEST SCRIPT DETAILS", 450, 50);
		        g.drawString("Test Run Name : " +  TName, 50, 125);
		        g.drawString("Script Number : " +  ScriptNumber, 50, 200);
		        g.drawString("Scenario Name :"+Scenario, 50, 275);
		        g.drawString("Status : "+status, 50, 350);
		        g.drawString("Executed By :"+ExeBy, 50, 425);
		        g.drawImage(logo,1150,15,null);
////		    g.drawString("Start Time :"+TStarttime1, 50, 425);
////		    g.drawString("End Time :"+endtime1, 50, 500);
////		    g.drawString("Execution Time : "+ExecutionTime, 50, 575);
		        g.dispose();
		        ImageIO.write(image, "jpg", new File("/u01/oracle/selenium/temp/VideoRecord/"+imagename+".jpg"));
		        //ImageIO.write(image, "jpg", new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\"+imagename+".jpg"));
		        
		        BufferedImage image1 = null;
		        image1 = ImageIO.read(file);
		        Graphics g1 = image1.getGraphics();
		        g1.setColor(Color.red);
		        java.awt.Font font1 = new java.awt.Font("Calibri", java.awt.Font.PLAIN, 36);
		        g1.setFont(font1);
		        g1.drawImage(logo,1012,14,null);
		        g1.drawString("FAILED IN THE NEXT STEP!!", 400, 300);
		        g1.dispose();
		        ImageIO.write(image1, "jpg", new File("/u01/oracle/selenium/temp/VideoRecord/last.jpg"));
		        //ImageIO.write(image1, "jpg", new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\last.jpg"));
		        
		        BufferedImage image2 = null;
		        image2 = ImageIO.read(file);
		        Graphics g2 = image2.getGraphics();
		        g2.setColor(Color.black);
		        g2.setFont(font);
		        g2.drawString("TEST RUN SUMMARY", 50, 50);
		        g2.drawString("Test Run Name : " +  TName, 50, 125);
	            g2.drawString("Executed By :"+ExeBy, 50, 200);
	            g2.drawString("Start Time :"+TStarttime1, 50, 275);
	            g2.drawString("End Time :"+endtime1, 50, 350);
	            g2.drawString("Execution Time : "+ExecutionTime, 50,425);
	            g2.drawImage(logo,1012,15,null);
		        g2.dispose();
		       	ImageIO.write(image2, "jpg", new File("/u01/oracle/selenium/temp/VideoRecord/first.jpg"));
		        String imgpath3 ="/u01/oracle/selenium/temp/VideoRecord/first.jpg";
		        String imgpath2 ="/u01/oracle/selenium/temp/VideoRecord/";
		        
		        //ImageIO.write(image2, "jpg", new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\first.jpg"));
			    //String imgpath3 ="C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\first.jpg";
		        //String imgpath2 ="C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\";
		        File f11=new File(imgpath2);
		        File[] f22=f11.listFiles();
		        File f44=new File(imgpath3);
		        firstimagelink=f44.getAbsolutePath();
		      
				if (seqList.get(0).getName().endsWith("Failed.jpg")) {
					failcount++;
					for(File f33:f22) {
			        	if(f33.getAbsolutePath().contains(imagename)) {
			        		linksall.add(f33.getAbsolutePath());
			        		linksall.set(0,f33.getAbsolutePath());
			        	}if(f33.getAbsolutePath().contains("last")) {
			        		linksall.add(f33.getAbsolutePath());
			        		linksall.add(f33.getAbsolutePath());  
			        		linksall.set(1,f33.getAbsolutePath());

		        	}
			        }
//	                                   System.out.println("SEQ : "+seqEntry.getKey());
					links1.add(seqList.get(0).getAbsolutePath());
					links1.add(linksall.get(1));
					seqFileNameList.add(seqList.get(0).getName());

					for (int i = 1; i < seqList.size(); i++) {

						if (!seqList.get(i).getName().endsWith("Failed.jpg")) {
							  links1.add(seqList.get(i).getAbsolutePath());
							seqFileNameList.add(seqList.get(i).getName());

						} else {

							

						}

					}
					links1.add(linksall.get(0));
					 Collections.reverse(links1);
					Collections.reverse(seqFileNameList);
					links.addAll(links1);
					targetFileList.addAll(seqFileNameList);

				}

//	                    targetFileList.addAll(seqList);

			}
	//
//			/*
//			 * for (String fileName : targetFileList) {
//			 * 
//			 * System.out.println("Target File : " + fileName);
//			 * 
//			 * }
//			 */
			fetchConfigVO.setPasscount(passcount);
			fetchConfigVO.setFailcount(failcount);
			if(video_rec.equalsIgnoreCase("yes")) {
				convertJPGtoMovie(firstimagelink,links,fetchMetadataListVO,fetchConfigVO,"Failed_Video.mp4");
		          }
			return targetFileList;


		}

		public List<String> getDetailPdfNew(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO) throws Exception {

			File folder = new File(fetchConfigVO.getScreenshot_path() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
					+ fetchMetadataListVO.get(0).getTest_run_name() + "/");
			//File folder=new File("C:\\Users\\Winfo Solutions\\Desktop\\test");
			File[] listOfFiles = folder.listFiles();
//			String video_rec=fetchConfigVO.getEnable_video();
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
							

							File file = new File("/u01/oracle/selenium/temp/VideoRecord/white.jpg");
							File file1 = new File("/u01/oracle/selenium/temp/VideoRecord/WATS_LOGO.JPG");
							//File file = new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\white.jpg");
							//File file1=new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\WATS_LOGO.JPG");
					        BufferedImage image = null;
					        image = ImageIO.read(file);
					        BufferedImage logo = null;
					        logo = ImageIO.read(file1);
					        Graphics g = image.getGraphics();
					        g.setColor(Color.black);
					        java.awt.Font font=new java.awt.Font("Calibri",  java.awt.Font.PLAIN, 36);
					        g.setFont(font);
					      
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
					        g.drawString("TEST SCRIPT DETAILS", 450, 50);
					        g.drawString("Test Run Name : " +  TName, 50, 125);
					        g.drawString("Script Number : " +  ScriptNumber, 50, 200);
					        g.drawString("Scenario Name :"+Scenario, 50, 275);
					        g.drawString("Status : "+status, 50, 350);
					        g.drawString("Executed By :"+ExeBy, 50, 425);
					        g.drawImage(logo,1012,15,null);
					        //g.drawString("Start Time :"+TStarttime1, 50, 425);
					        //g.drawString("End Time :"+endtime1, 50, 500);
					        //g.drawString("Execution Time : "+ExecutionTime, 50, 575);
					        g.dispose();
					        ImageIO.write(image, "jpg", new File("/u01/oracle/selenium/temp/VideoRecord/"+imagename+".jpg"));
					        //ImageIO.write(image, "jpg", new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\"+imagename+".jpg"));
					        
					        BufferedImage image1 = null;
					        image1 = ImageIO.read(file);
					        Graphics g1 = image1.getGraphics();
					        g1.setColor(Color.red);
					        java.awt.Font font1 = new java.awt.Font("Calibri", java.awt.Font.PLAIN, 36);
					        g1.setFont(font1);
					        g1.drawString("FAILED IN THE NEXT STEP!!", 400, 300);
					        g1.drawImage(logo,1012,14,null);
					        g1.dispose();
					        
					        BufferedImage image2 = null;
					        image2 = ImageIO.read(file);
					        Graphics g2 = image2.getGraphics();
					        g2.setColor(Color.black);
					        g2.setFont(font);
					        g2.drawString("TEST RUN SUMMARY", 450, 50);
					        g2.drawString("Test Run Name : " +  TName, 50, 125);
				            g2.drawString("Executed By :"+ExeBy, 50, 200);
				            g2.drawString("Start Time :"+TStarttime1, 50, 275);
				            g2.drawString("End Time :"+endtime1, 50, 350);
				            g2.drawString("Execution Time : "+ExecutionTime, 50,425);
				            g2.drawImage(logo,1012,15,null);
					        g2.dispose();
					       ImageIO.write(image2, "jpg", new File("/u01/oracle/selenium/temp/VideoRecord/first.jpg"));
					        //ImageIO.write(image2, "jpg", new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\first.jpg"));
					       String imgpath2 ="/u01/oracle/selenium/temp/VideoRecord/";
					        //String imgpath2 ="C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\";
					        String imgpath3 ="/u01/oracle/selenium/temp/VideoRecord/first.jpg";
					        //String imgpath3 ="C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\first.jpg";
					        File f11=new File(imgpath2);
					        File[] f22=f11.listFiles();
					        File f44=new File(imgpath3);
					        firstimagelink=f44.getAbsolutePath();

							if (!seqList.get(0).getName().endsWith("Failed.jpg")) {
								passcount++;
								for(File f33:f22)
						        {
						        	if(f33.getAbsolutePath().contains(imagename)) {
								  linksall.add(f33.getAbsolutePath());
						        	}
						        }
								links1.add(seqList.get(0).getAbsolutePath());

								seqFileNameList.add(seqList.get(0).getName());

//				             	                      System.out.println("FIRST S STEP: "+seqList.get(0).getName());

								for (int i = 1; i < seqList.size(); i++) {

									if (!seqList.get(i).getName().endsWith("Failed.jpg")) {
										links1.add(seqList.get(i).getAbsolutePath());

										seqFileNameList.add(seqList.get(i).getName());

//				                                                                 System.out.println("S STEP: "+seqList.get(i).getName());

									} else {

										

									}

								}
								links1.add(linksall.get(0));
								Collections.reverse(links1);
								Collections.reverse(seqFileNameList);
								links.addAll(links1);
								targetSuccessFileList.addAll(seqFileNameList);

							} else {
								failcount++;
								for(File f33:f22) {
						        	if(f33.getAbsolutePath().contains(imagename)) {
						        		linksall.add(f33.getAbsolutePath());
						        		linksall.set(0,f33.getAbsolutePath());
						        	}
						        	if(f33.getAbsolutePath().contains("last")) {
						        		linksall.add(f33.getAbsolutePath());
						        		linksall.add(f33.getAbsolutePath());  
						        		linksall.set(1,f33.getAbsolutePath());

						        	}
						        }
//				                                   System.out.println("SEQ : "+seqEntry.getKey());
								links1.add(seqList.get(0).getAbsolutePath());
								links1.add(linksall.get(1));
//				                                   System.out.println("SEQ : "+seqEntry.getKey());

								seqFileNameList.add(seqList.get(0).getName());

//				                                   System.out.println("FIRST F STEP: "+seqList.get(0).getName());

								for (int i = 1; i < seqList.size(); i++) {

									if (!seqList.get(i).getName().endsWith("Failed.jpg")) {
										links1.add(seqList.get(i).getAbsolutePath());

										seqFileNameList.add(seqList.get(i).getName());

//				                                                                 System.out.println("F STEP: "+seqList.get(i).getName());

									} else {

										

									}

								}

								links1.add(linksall.get(0));
								 Collections.reverse(links1);
								Collections.reverse(seqFileNameList);
								  links2.addAll(links1);
								targetFailedFileList.addAll(seqFileNameList);


							}

//				                    targetFileList.addAll(seqList);

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
			 String vidPath = (fetchConfigVO.getPdf_path() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
						+ fetchMetadataListVO.get(0).getTest_run_name() + "/"+name);
			// String vidPath="C:\\Testing\\ReportWinfo\\"+name;
			 String Folder = (fetchConfigVO.getPdf_path() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
						+ fetchMetadataListVO.get(0).getTest_run_name() + "/");
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
//	             for (int i=0;i<targetFileList.size();i++)
//	             {
//	            	 System.out.println(targetFileList.get(i));
//	             }
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
		 public List<String> getFileNameListNew(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO) throws IOException, Exception {

				File folder = new File(fetchConfigVO.getScreenshot_path() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
						+ fetchMetadataListVO.get(0).getTest_run_name() + "/");
				//File folder = new File("C:\\\\Users\\\\Winfo Solutions\\\\Desktop\\\\test");

				File[] listOfFiles = folder.listFiles();
//				String video_rec=fetchConfigVO.getEnable_video();
				String video_rec="no";
//				List<File> fileList = Arrays.asList(listOfFiles);
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
				File file = new File("/u01/oracle/selenium/temp/VideoRecord/white.jpg");
				//File file = new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\white.jpg");
				File file1 = new File("/u01/oracle/selenium/temp/VideoRecord/WATS_LOGO.JPG");
				//File file1=new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\WATS_LOGO.JPG");
				
		        BufferedImage image = null;
		        image = ImageIO.read(file);
		        BufferedImage logo = null;
		        logo = ImageIO.read(file1);
		        Graphics g = image.getGraphics();
		        g.setColor(Color.black);
		        java.awt.Font font=new java.awt.Font("Calibri",  java.awt.Font.PLAIN, 36);
		        g.setFont(font);
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
		        g.drawString("TEST SCRIPT DETAILS",450, 50);
		        g.drawString("Test Run Name : " +  TName, 50, 100);
		        g.drawString("Script Number : " +  ScriptNumber, 50, 150);
		        g.drawString("Scenario Name :"+Scenario, 50, 200);
		        g.drawString("Status : "+status, 50, 250);
		        g.drawString("Executed By :"+ExeBy, 50, 300);
		        g.drawString("Start Time :"+Starttime1, 50, 350);
		        g.drawString("End Time :"+endtime1, 50, 400);
		        g.drawString("Execution Time : "+ExecutionTime, 50, 450);
		        g.drawImage(logo,1012,15,null);
		        g.dispose();
		        ImageIO.write(image, "jpg", new File("/u01/oracle/selenium/temp/VideoRecord/first.jpg"));
		        //ImageIO.write(image, "jpg", new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\first.jpg"));
		        
		        BufferedImage image1 = null;
		        image1 = ImageIO.read(file);
		        Graphics g1 = image1.getGraphics();
		        g1.setColor(Color.red);
		        java.awt.Font font1 = new java.awt.Font("Calibir", java.awt.Font.PLAIN, 36);
		        g1.setFont(font1);
		        g1.drawString("FAILED IN THE NEXT STEP!!", 400, 300);
		        g1.drawImage(logo,1150,15,null);
		        g1.dispose();
		        ImageIO.write(image1, "jpg", new File("/u01/oracle/selenium/temp/VideoRecord/last.jpg"));
		        //ImageIO.write(image1, "jpg", new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\last.jpg"));
		        String imgpath2 ="/u01/oracle/selenium/temp/VideoRecord/";
		        //String imgpath2 ="C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\";
		        File f11=new File(imgpath2);
		        File[] f22=f11.listFiles();
		       

				if (!fileList.get(0).getName().endsWith("Failed.jpg")) {
					for(File f33:f22)
			        {
			        	if(f33.getAbsolutePath().contains("first")) {
					  linksall.add(f33.getAbsolutePath());
			        	}
			        }
					fetchConfigVO.setStatus1("Pass");
					fileNameList.add(fileList.get(0).getName());
					links1.add(fileList.get(0).getAbsolutePath());
					for (int i = 1; i < fileList.size(); i++) {

						if (!fileList.get(i).getName().endsWith("Failed.jpg")) {
							links1.add(fileList.get(i).getAbsolutePath());

							fileNameList.add(fileList.get(i).getName());

						} else {

							

						}

					}

					 links1.add(linksall.get(0));
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
		 public List<String> getFailFileNameListNew(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO) throws IOException, Exception {
			 System.out.println("entered to getFailFileNameListNew");
			 		File folder = new File(fetchConfigVO.getScreenshot_path() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
			 				+ fetchMetadataListVO.get(0).getTest_run_name() + "/");
			 		//File folder = new File("C:\\\\Users\\\\Winfo Solutions\\\\Desktop\\\\test");
			 		File[] listOfFiles = folder.listFiles();
			 		String video_rec="no";
			 		//String video_rec="yes";
//			 		List<File> fileList = Arrays.asList(listOfFiles);
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
			 		File file = new File("/u01/oracle/selenium/temp/VideoRecord/white.jpg");
			 		//File file = new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\white.jpg");
			 		File file1 = new File("/u01/oracle/selenium/temp/VideoRecord/WATS_LOGO.JPG");
			 		//File file1=new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\WATS_LOGO.JPG");

			         BufferedImage image = null;
			         image = ImageIO.read(file);
			         BufferedImage logo = null;
			         logo = ImageIO.read(file1);
			         Graphics g = image.getGraphics();
			         g.setColor(Color.black);
			         java.awt.Font font=new java.awt.Font("Calibri",  java.awt.Font.PLAIN, 36);
			         g.setFont(font);
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
			         g.drawString("TEST SCRIPT DETAILS",450, 50);
			         g.drawString("Test Run Name : " +  TName, 50, 100);
			         g.drawString("Script Number : " +  ScriptNumber, 50, 150);
			         g.drawString("Scenario Name :"+Scenario, 50, 200);
			         g.drawString("Status : "+status, 50, 250);
			         g.drawString("Executed By :"+ExeBy, 50, 300);
			         g.drawString("Start Time :"+Starttime1, 50, 350);
			         g.drawString("End Time :"+endtime1, 50, 400);
			         g.drawString("Execution Time : "+ExecutionTime, 50, 450);
			         g.drawImage(logo,1012,15,null);
			         g.dispose();
			         System.out.println("before ImageIO.write");
			         ImageIO.write(image, "jpg", new File("/u01/oracle/selenium/temp/VideoRecord/first.jpg"));
			         //ImageIO.write(image, "jpg", new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\first.jpg"));
			         
			         BufferedImage image1 = null;
			         image1 = ImageIO.read(file);
			         Graphics g1 = image1.getGraphics();
			         g1.setColor(Color.red);
			         java.awt.Font font1 = new java.awt.Font("Calibir", java.awt.Font.PLAIN, 36);
			         g1.setFont(font1);
			         g1.drawString("FAILED IN THE NEXT STEP!!", 400, 300);
			         g1.drawImage(logo,1012,15,null);
			         g1.dispose();
//			         ImageIO.write(image1, "jpg", new File("/u01/oracle/selenium/temp/VideoRecord/last.jpg"));
			         String imgpath2 ="/u01/oracle/selenium/temp/VideoRecord/";
			         //ImageIO.write(image1, "jpg", new File("C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\last.jpg"));
			         //String imgpath2 ="C:\\Users\\Winfo Solutions\\Desktop\\Add_On\\";
			         File f11=new File(imgpath2);
			         File[] f22=f11.listFiles();
			         System.out.println("before Failed.jpg");
			 		if (fileList.get(0).getName().endsWith("Failed.jpg")) {
			 			for(File f33:f22) {
			 	        	if(f33.getAbsolutePath().contains("first")) {
			 	        		linksall.add(f33.getAbsolutePath());
			 	        		linksall.set(0,f33.getAbsolutePath());
			 	        	}
			 	        	if(f33.getAbsolutePath().contains("last")) {
			 	        		linksall.add(f33.getAbsolutePath());
			 	        		linksall.add(f33.getAbsolutePath());  
			 	        		linksall.set(1,f33.getAbsolutePath());

			 	        	}}
			 			fetchConfigVO.setStatus1("Fail");
			 			fileNameList.add(fileList.get(0).getName());
			 			links1.add(fileList.get(0).getAbsolutePath());
			             links1.add(linksall.get(1));
			             System.out.println("added links1 list");
			 			for (int i = 1; i < fileList.size(); i++) {

			 				if (!fileList.get(i).getName().endsWith("Failed.jpg")) {
			 					 links1.add(fileList.get(i).getAbsolutePath());
			 					fileNameList.add(fileList.get(i).getName());

			 				} else {

			 					

			 				}

			 			}

			 			links1.add(linksall.get(0));
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
			public void DelatedScreenshoots(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO) throws IOException {
				File folder = new File(fetchConfigVO.getScreenshot_path() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
						+ fetchMetadataListVO.get(0).getTest_run_name() + "/");
				if (folder.exists()) {
				File[] listOfFiles = folder.listFiles();
				
//				String image=fetchConfigVO.getScreenshot_path() + fetchMetadataVO.getCustomer_name() + "/"
//						+ fetchMetadataVO.getTest_run_name() + "/" + fetchMetadataVO.getSeq_num() + "_"
//						+ fetchMetadataVO.getLine_number() + "_" + fetchMetadataVO.getScenario_name() + "_"
//						+ fetchMetadataVO.getScript_number() + "_" + fetchMetadataVO.getTest_run_name() + "_"
//						+ fetchMetadataVO.getLine_number();
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
			public void loginApplication(WebDriver driver, FetchConfigVO fetchConfigVO, FetchMetadataVO fetchMetadataVO,
					String type1, String type2, String type3, String param1, String param2, String param3, String keysToSend,
					String value) throws Exception {
				String param5 = "password";
				String param6 = "Sign In";
				navigateUrl(driver, fetchConfigVO, fetchMetadataVO);
				String xpath1=loginPage(driver, param1, keysToSend, fetchMetadataVO, fetchConfigVO);
				String xpath2=loginPage(driver, param5, value, fetchMetadataVO, fetchConfigVO);
				String scripNumber=fetchMetadataVO.getScript_number();
				String xpath=xpath1+";"+xpath2;
				service.saveXpathParams("User ID", "", scripNumber, xpath);
//				sendValue(driver, param1, param3, keysToSend, fetchMetadataVO, fetchConfigVO);
//				sendValue(driver, param5, param2, value, fetchMetadataVO, fetchConfigVO);
//				clickSignInSignOut(driver, param6, fetchMetadataVO, fetchConfigVO);
//				clickButton(driver, param6, param2, fetchMetadataVO, fetchConfigVO);
			}
			public void navigateUrl(WebDriver driver, FetchConfigVO fetchConfigVO, FetchMetadataVO fetchMetadataVO) {
				try {
					driver.navigate().to(fetchConfigVO.getApplication_url());
					driver.manage().window().maximize();
					deleteAllCookies(driver, fetchMetadataVO, fetchConfigVO);
					refreshPage(driver, fetchMetadataVO, fetchConfigVO);
					switchToActiveElement(driver, fetchMetadataVO, fetchConfigVO);
					String scripNumber = fetchMetadataVO.getScript_number();
		           	log.error("Failed to logout " +scripNumber);
				} catch (Exception e) {
		      	  String scripNumber = fetchMetadataVO.getScript_number();
			      log.error("failed to do navigate URl " +scripNumber);
					screenshotFail(driver, "Failed during navigateUrl Method", fetchMetadataVO, fetchConfigVO);
					System.out.println("Not able to navitage to the Url");
				}
			}
			public void switchToActiveElement(WebDriver driver, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) {
				try {
					driver.switchTo().activeElement();
					String scripNumber = fetchMetadataVO.getScript_number();
					log.info("Switched to Element Successfully" +scripNumber);
				} catch (Exception e) {
					String scripNumber = fetchMetadataVO.getScript_number();
					log.error("Failed During switchToActiveElement Action." +scripNumber);
					screenshotFail(driver, "Failed during switchToActiveElement Method", fetchMetadataVO, fetchConfigVO);
					System.out.println(e.getMessage());
					throw e;
				}
			}
			
			public void refreshPage(WebDriver driver, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) {
				try {
					driver.navigate().refresh();
					String scripNumber = fetchMetadataVO.getScript_number();
					log.info("Sucessfully Clicked refreshPage"+scripNumber);
				} catch (Exception e) {
					String scripNumber = fetchMetadataVO.getScript_number();
					log.error("Failed during refreshPage"+scripNumber);
					screenshotFail(driver, "Failed during refreshPage Method", fetchMetadataVO, fetchConfigVO);
					System.out.println("can not refresh the page");
					e.printStackTrace();
					throw e;

				}
			}
			public void deleteAllCookies(WebDriver driver, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) {
				try {
					driver.manage().deleteAllCookies();
					log.info("Successfully Deleted All The Cookies.");
				} catch (Exception e) {
					log.error("Failed To Delete All The Cookies.");
					screenshotFail(driver, "Failed during deleteAllCookies Method", fetchMetadataVO, fetchConfigVO);
					System.out.println("cookies not deleted");
					throw e;
				}
			}
			public String loginPage(WebDriver driver, String param1, String keysToSend, FetchMetadataVO fetchMetadataVO,
					FetchConfigVO fetchConfigVO) {
				String xpath = null;
				try {
					if (param1.equalsIgnoreCase("password")) {
						WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
						wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='" + param1 + "']")));
						JavascriptExecutor jse = (JavascriptExecutor) driver;
						jse.executeScript("document.getElementById('password').value = '" + keysToSend + "';");
						screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
						Thread.sleep(1000);
						enter(driver, fetchMetadataVO, fetchConfigVO);
						String scripNumber = fetchMetadataVO.getScript_number();
						log.info("Succesfully password is entered " +scripNumber);
						 xpath="//input[@type='param1']";
						return xpath;
					}
				} catch (Exception e) {
					String scripNumber = fetchMetadataVO.getScript_number();
					log.error("Failed to enter password " +scripNumber);
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
					String scripNumber = fetchMetadataVO.getScript_number();
					 xpath="//*[contains(@placeholder,'param1')]";
					log.info("Successfully entered data " +scripNumber);
					return xpath;
				} catch (Exception e) {
					String scripNumber = fetchMetadataVO.getScript_number();
					log.info("Failed during login page " +scripNumber);
					screenshotFail(driver, "Failed During Login page", fetchMetadataVO, fetchConfigVO);
					System.out.println("Failed During Login page");
				}
				return xpath;
			}
			public void typeIntoValidxpath(WebDriver driver, String keysToSend, WebElement waittill,
					FetchConfigVO fetchConfigVO, FetchMetadataVO fetchMetadataVO) {
				try {
					waittill.clear();
					waittill.click();
					JavascriptExecutor jse = (JavascriptExecutor) driver;
					jse.executeScript("arguments[0].value='" + keysToSend + "';", waittill);
					log.info("clear and typed the given Data");
			       String scripNumber = fetchMetadataVO.getScript_number();
					log.info("Sucessfully Clicked typeIntoValidxpath"+scripNumber);

				} catch (Exception e) {
					String scripNumber = fetchMetadataVO.getScript_number();
					log.error("Failed during  typeIntoValidxpath"+scripNumber);	
					e.printStackTrace();
				}
				
			}
			public synchronized void openTask(WebDriver driver, FetchConfigVO fetchConfigVO, FetchMetadataVO fetchMetadataVO, String type1,
					String type2, String param1, String param2,int count) throws Exception {
				String param3 = "Tasks";
//				clickImage(driver, param3, param2, fetchMetadataVO, fetchConfigVO);
//				clickLink(driver, param1, param2, fetchMetadataVO, fetchConfigVO);
				String xpath=task(driver, param3, fetchMetadataVO, fetchConfigVO);
				String xpath1=taskMenu(driver,fetchMetadataVO, fetchConfigVO,type1,type2,param1,param2,count);
				String xpaths=xpath+";"+xpath1;
				String scripNumber=fetchMetadataVO.getScript_number();
				service.saveXpathParams(param1,param2,scripNumber,xpaths);
			}
			public String task(WebDriver driver, String param1, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO)
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
					String scripNumber = fetchMetadataVO.getScript_number();
					log.info("Successfully task is open " +scripNumber);
					String xpath="//img[@title='param1']";
					log.info("Successfully task is open " + scripNumber);
					return xpath;
				
				} catch (Exception e) {
					String scripNumber = fetchMetadataVO.getScript_number();
					log.info("Failed During Task " +scripNumber);
					screenshotFail(driver, "Failed to Open Task Menu", fetchMetadataVO, fetchConfigVO);
					System.out.println("Failed to Open Task Menu");
					throw e;
				}
			}

			public String taskMenu(WebDriver driver, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO,String type1,String type2, String param1,String param2,int count)
					throws Exception {
				String xpath = null;
				try {
					Thread.sleep(2000);
					WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
					wait.until(ExpectedConditions.presenceOfElementLocated(
							By.xpath("//div[contains(@class,'AFVertical')]//a[normalize-space(text())='" + param1 + "']")));
					wait.until(ExpectedConditions.elementToBeClickable(
							By.xpath("//div[contains(@class,'AFVertical')]//a[normalize-space(text())='" + param1 + "']")));
					WebElement waittext = driver
							.findElement(By.xpath("//div[contains(@class,'AFVertical')]//a[normalize-space(text())='" + param1 + "']"));
					Actions actions = new Actions(driver);
					actions.moveToElement(waittext).build().perform();
					clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
					Thread.sleep(5000);
					screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
					String scripNumber = fetchMetadataVO.getScript_number();
					log.info("Successfully open Task " +scripNumber);
					 xpath="//div[contains(@class,'AFVertical')]//a[normalize-space(text())='param1']";

					log.info("Successfully open Task " + scripNumber);
					return xpath;
					
				} catch (Exception e) {
					 if(count==0) {
						  count = 1; 
						  System.out.println(" The Count Value is : "+count);
						  openTask(driver, fetchConfigVO, fetchMetadataVO, type1, type2, param1, param2,count);
					  }else if(count<=10) { 
						  count = count+1;
						  System.out.println(" The Count Value is : "+count); 
						  openTask(driver, fetchConfigVO, fetchMetadataVO, type1, type2, param1, param2,count);
					  }
					  else 
					  {
					  System.out.println("Count value exceeds the limit"); 
					  log.error("Failed to Open Task Menu");
					  screenshotFail(driver, "Failed to Open Task Menu", fetchMetadataVO, fetchConfigVO);
					  System.out.println("Failed to Open Task Menu");
					  throw e;
					
					  }
				}
				return xpath;
			}
			private void clickValidateXpath(WebDriver driver, FetchMetadataVO fetchMetadataVO, WebElement waittext,
					FetchConfigVO fetchConfigVO) {
				try {
					JavascriptExecutor js = (JavascriptExecutor)driver;
					js.executeScript("arguments[0].click();", waittext);
					String scripNumber = fetchMetadataVO.getScript_number();
					log.info("Sucessfully Clicked clickValidateXpath"+scripNumber);
					//waittext.click();
				} catch (Exception e) {
					String scripNumber = fetchMetadataVO.getScript_number();
					log.error("Failed during  clickValidateXpath"+scripNumber);	
					e.printStackTrace();
				}
			}
			public String screenshot(WebDriver driver, String screenshotName, FetchMetadataVO fetchMetadataVO,
					FetchConfigVO fetchConfigVO) {
					String image_dest = null;
					try {
						Shutterbug.shootPage(driver,ScrollStrategy.BOTH_DIRECTIONS,500,true).withName(fetchMetadataVO.getSeq_num() + "_"
								+ fetchMetadataVO.getLine_number() + "_" + fetchMetadataVO.getScenario_name() + "_"
								+ fetchMetadataVO.getScript_number() + "_" + fetchMetadataVO.getTest_run_name() + "_"
								+ fetchMetadataVO.getLine_number() + "_Passed").save(fetchConfigVO.getScreenshot_path()+"\\" + fetchMetadataVO.getCustomer_name() + "\\"
								+ fetchMetadataVO.getTest_run_name() + "\\");
						log.info("Successfully Screenshot is taken");
						return image_dest;
					} catch (Exception e) {
						log.error("Failed During Taking screenshot");
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
								+ fetchMetadataVO.getLine_number() + "_Failed").save(fetchConfigVO.getScreenshot_path()+"\\" + fetchMetadataVO.getCustomer_name() + "\\"
								+ fetchMetadataVO.getTest_run_name() + "\\");
						log.info("Successfully Screenshot is taken");
						return image_dest;
					} catch (Exception e) {
						log.error("Failed During Taking screenshot");
						System.out.println("Exception while taking Screenshot" + e.getMessage());
						return e.getMessage();
					}
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
			String scripNumber = fetchMetadataVO.getScript_number();
						log.info("Successfully Logout is done " +scripNumber);
					} catch (Exception e) {
						clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
						Thread.sleep(4000);
						WebElement signout = driver
								.findElement(By.xpath("//div[contains(@id,'popup-container')]//a[text()='Sign Out']"));
						signout.click();
						Thread.sleep(4000);
				String scripNumber = fetchMetadataVO.getScript_number();
						log.error("Successfully Logout is done " +scripNumber);
					}
					return;
				} catch (Exception e) {
					System.out.println(e);
			      String scripNumber = fetchMetadataVO.getScript_number();
					log.error("Failed to logout " +scripNumber);
					screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, fetchConfigVO);
					throw e;
				}
			}
			public void clickSignInSignOut(WebDriver driver, String param1, FetchMetadataVO fetchMetadataVO,
					FetchConfigVO fetchConfigVO) throws Exception {
				try {
					WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
					wait.until(ExpectedConditions
							.presenceOfElementLocated(By.xpath(("//button[normalize-space(normalize-space(text())='" + param1 + "')]"))));
					WebElement waittext = driver.findElement(By.xpath(("//button[normalize-space(normalize-space(text())='" + param1 + "')]")));
					screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
					clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
					String scripNumber = fetchMetadataVO.getScript_number();
					log.info("Sucessfully clicked SingnInSignOut" +scripNumber);
					return;
				} catch (Exception e) {
					String scripNumber = fetchMetadataVO.getScript_number();
					System.out.println(e);
					log.error("Failed during SingnInSignOut " +scripNumber);
					screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, fetchConfigVO);
					throw e;
				}
			}// *[text()='Action Required']/following::a[1]

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
					String scripNumber = fetchMetadataVO.getScript_number();
					log.info("Sucessfully Clicked textarea"+scripNumber);
					String xpath = "//*[normalize-space(text())='param1']/following::label[normalize-space(text())='param2']/following::textarea[1]";
					service.saveXpathParams(param1, param2, scripNumber, xpath);
					return keysToSend;
				} catch (Exception e) {
					String scripNumber = fetchMetadataVO.getScript_number();
					log.error("Failed during textarea"+scripNumber);	
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
					String scripNumber = fetchMetadataVO.getScript_number();
					log.info("Sucessfully Clicked textarea"+scripNumber);
					String xpath = "(//*[contains(text(),'param1')]/following::label[normalize-space(text())='param2']/following::textarea)[1]";
					service.saveXpathParams(param1, param2, scripNumber, xpath);
					return keysToSend;
				} catch (Exception e) {
					String scripNumber = fetchMetadataVO.getScript_number();
					log.error("Failed during textarea"+scripNumber);	
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
					String scripNumber = fetchMetadataVO.getScript_number();
					log.info("Sucessfully Clicked textarea"+scripNumber);
					String xpath = "//body[@dir='ltr']";
					service.saveXpathParams(param1, param2, scripNumber, xpath);
					return keysToSend;
				} catch (Exception e) {
					System.out.println(e);
					String scripNumber = fetchMetadataVO.getScript_number();
					log.error("Failed during textarea"+scripNumber);	
				}
				try { 
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
					log.info("Sucessfully Clicked textarea"+scripNumber);
					String xpath = "//body[contains(@class,'contents_ltr')][1]";
					service.saveXpathParams(param1, param2, scripNumber, xpath);
					return keysToSend;	
				} catch (Exception e) {
					System.out.println(e);
				}
				try { 
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
					log.info("Sucessfully Clicked textarea"+scripNumber);
					String xpath = "//*[normalize-space(text())='\"+param1+\"']/following::*[normalize-space(text())='\"+param2+\"']/following::textarea[1]";
					service.saveXpathParams(param1, param2, scripNumber, xpath);
					return keysToSend;	
				} catch (Exception e) {
					System.out.println(e);
					log.error("Failed during Click action.");
					screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, fetchConfigVO);
					throw e;
				}
			}
			public void clickRadiobutton(WebDriver driver, String param1, String param2, String keysToSend,
					FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception {
				try {
					WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
					wait.until(ExpectedConditions
							.presenceOfElementLocated(By.xpath(("(//*[normalize-space(text())='" + param1 + "']/following::label[text()='"
									+ param2 + "']/following::label[normalize-space(text())='" + keysToSend + "'])[1]"))));
					wait.until(ExpectedConditions.textToBePresentInElementLocated(
							By.xpath("//*[normalize-space(text())='" + param1 + "']/following::label[normalize-space(text()))='" + param2 + "']"), param2));
					WebElement waittext = driver.findElement(By.xpath("(//*[normalize-space(text())='" + param1 + "']/following::label[normalize-space(text())='"
							+ param2 + "']/following::label[normalize-space(text())='" + keysToSend + "'])[1]"));
					Thread.sleep(1000);
					clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
					tab(driver, fetchMetadataVO, fetchConfigVO);
					Thread.sleep(500);
					screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
					String scripNumber = fetchMetadataVO.getScript_number();
					log.info("Sucessfully Clicked clickRadiobutton"+scripNumber);			
					String xpath = "(//*[normalize-space(text())='param1']/following::label[text()='param2']/following::label[normalize-space(text())='keysToSend'])[1]";
					service.saveXpathParams(param1, param2, scripNumber, xpath);

					return;
				} catch (Exception e) {
					String scripNumber = fetchMetadataVO.getScript_number();
					log.error("Failed during clickRadiobutton"+scripNumber);	
					System.out.println(e);
				}
				try {
					WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
					wait.until(ExpectedConditions.presenceOfElementLocated(
							By.xpath(("(//*[normalize-space(text())='" + param1 + "']/following::label[normalize-space(text())='" + keysToSend + "'])[1]"))));
					WebElement waittext = driver.findElement(
							By.xpath(("(//*[normalize-space(text())='" + param1 + "']/following::label[normalize-space(text())='" + keysToSend + "'])[1]")));
					clickValidateXpath(driver, fetchMetadataVO, waittext, fetchConfigVO);
					tab(driver, fetchMetadataVO, fetchConfigVO);
					Thread.sleep(500);
					screenshot(driver, "", fetchMetadataVO, fetchConfigVO);
					String scripNumber = fetchMetadataVO.getScript_number();
					log.info("Sucessfully Clicked clickRadiobutton"+scripNumber);		
					String xpath = "(//*[normalize-space(text())='param1']/following::label[normalize-space(text())='keysToSend'])[1]";
					service.saveXpathParams(param1, param2, scripNumber, xpath);

					return;
				} catch (Exception e) {
					String scripNumber = fetchMetadataVO.getScript_number();
					log.error("Failed during clickRadiobutton"+scripNumber);	
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
					String scripNumber = fetchMetadataVO.getScript_number();
					log.info("Sucessfully Clicked clickRadiobutton"+scripNumber);	
					String xpath = "//*[contains(text(),'param1')]/following::*[normalize-space(text())='keysToSend']/preceding-sibling::input[1]";
					service.saveXpathParams(param1, param2, scripNumber, xpath);

					return;
				} catch (Exception e) {
					System.out.println(e);
					String scripNumber = fetchMetadataVO.getScript_number();
					log.error("Failed during clickRadiobutton"+scripNumber);	
					screenshotFail(driver, "Failed during Link Case", fetchMetadataVO, fetchConfigVO);
					throw e;
				}
			}

}
