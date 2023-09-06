package com.winfo.reports;

import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.winfo.dao.DataBaseEntryDao;
import com.winfo.model.Scheduler;
import com.winfo.model.UserSchedulerJob;
import com.winfo.repository.ConfigurationRepository;
import com.winfo.repository.ProjectRepository;
import com.winfo.repository.SchedulerRepository;
import com.winfo.repository.TestSetRepository;
import com.winfo.repository.UserSchedulerJobRepository;
import com.winfo.utils.DateUtils;

@Service
public class PDFGenerator {


	private static SchedulerRepository schedulerRepository;


	private static ProjectRepository projectRepository;


	private static ConfigurationRepository configurationRepository;


	private static UserSchedulerJobRepository userSchedulerJobRepository;


	private static TestSetRepository testSetRepository;


	private static DataBaseEntryDao dataBaseEntryDao;

	public static final Logger logger = Logger.getLogger(PDFGenerator.class);

	public PDFGenerator(SchedulerRepository schedulerRepository, ProjectRepository projectRepository,
			ConfigurationRepository configurationRepository, UserSchedulerJobRepository userSchedulerJobRepository,
			TestSetRepository testSetRepository, DataBaseEntryDao dataBaseEntryDao) {
		this.schedulerRepository = schedulerRepository;
		this.projectRepository = projectRepository;
		this.configurationRepository = configurationRepository;
		this.userSchedulerJobRepository = userSchedulerJobRepository;
		this.testSetRepository = testSetRepository;
		this.dataBaseEntryDao = dataBaseEntryDao;

	}

	public static void createPDF(int jobId, String pdfpath, String cutomerName) throws Exception {
		logger.info("Schedule Report Started");
		try {
		Scheduler scheduler = schedulerRepository.findByJobId(jobId);
		String projectName = projectRepository.getProjectNameById(scheduler.getProjectId());
		String configurationName = configurationRepository.getConfigNameUsingId(scheduler.getConfigurationId());
		List<Object[]> startAndendTime = userSchedulerJobRepository.getMinandMaxTime(jobId);

		Document document = new Document(PageSize.A3);
		PdfWriter writer = PdfWriter.getInstance(document,
				new FileOutputStream(pdfpath + "//" + cutomerName + "//" + scheduler.getJobId() + ".pdf"));
		document.open();

		PdfPTable heading = new PdfPTable(1);
		Font customFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13);
		PdfPCell headcell = new PdfPCell(new Paragraph("Schedule Summary Report", customFont));
		headcell.setBorderWidth(0);
		headcell.setBackgroundColor(new BaseColor(167, 216, 241));
		headcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		headcell.setVerticalAlignment(Element.ALIGN_CENTER);
		headcell.setFixedHeight(20);
		heading.setWidthPercentage(100);
		heading.addCell(headcell);
		heading.setHorizontalAlignment(Element.ALIGN_TOP);
		document.add(heading);
	
		document.add(new Paragraph("\n"));

		startingDetails(document, scheduler.getJobName(), projectName, configurationName, scheduler.getEmail(),
				String.valueOf(startAndendTime.get(0)[0]), String.valueOf(startAndendTime.get(0)[1]));
		Font titleFont = FontFactory.getFont("Open Sans", BaseFont.IDENTITY_H, 12, Font.BOLD, BaseColor.BLACK);
		String[] headers = { "S.No", "Test Run Name", "Pass", "Fail", "Pass %", "Fail %", "Start Date", "End Date",
				"Total Duration" };

		PdfPTable table = createTable(9);
		addTableHeader(table, titleFont, headers);
		List<String> noOfRuns = userSchedulerJobRepository.getTestSetNames(jobId);
		int pass = 0;
		int fail = 0;
		Map<String, Map<String, Integer>> testRuns = new HashMap<>();
		
		for (int i = 0; i < noOfRuns.size(); i++) {
			int testSetId = testSetRepository.findByTestRunName(noOfRuns.get(i)).getTestRunId();
			Map<String, Integer> passAndFailCount = dataBaseEntryDao.getPassAndFailCount(String.valueOf(testSetId));
			UserSchedulerJob schedularRecords = userSchedulerJobRepository.findByCommentsAndJobId(noOfRuns.get(i), jobId);
            String startTime=schedularRecords.getStartDate();
            String endtime = getEndTime(schedularRecords.getEndDate());
			//LocalDateTime endTime =  LocalDateTime.parse("2023-08-22T11:14:45.747182"); 
			pass = pass + passAndFailCount.get("pass");
			fail = fail + passAndFailCount.get("fail");
			Font cellFont = FontFactory.getFont("Arial", 12);
			table.setWidths(new float[] { 0.50f, 1.0f, 0.45f, 0.40f, 0.60f, 0.60f, 1.60f, 1.60f, 1.20f });
			table.setWidthPercentage(100);
			table.addCell(createCell(new Paragraph(String.valueOf(i + 1)), Element.ALIGN_RIGHT, cellFont));
			table.addCell(createCell(new Paragraph(noOfRuns.get(i)), Element.ALIGN_LEFT, cellFont));
			table.addCell(createCell(new Paragraph(String.valueOf(passAndFailCount.get("pass"))), Element.ALIGN_RIGHT,
					cellFont));
			table.addCell(createCell(new Paragraph(String.valueOf(passAndFailCount.get("fail"))), Element.ALIGN_RIGHT,
					cellFont));
			table.addCell(
					createCell(new Paragraph(passPercent(passAndFailCount.get("pass"), passAndFailCount.get("fail"))),
							Element.ALIGN_RIGHT, cellFont));
			table.addCell(
					createCell(new Paragraph(failPercent(passAndFailCount.get("pass"), passAndFailCount.get("fail"))),
							Element.ALIGN_RIGHT, cellFont));
			table.addCell(createCell(new Paragraph(dateFormatConversion(String.valueOf(startTime.replace("+", "+0")).substring(0, 19)).toUpperCase()), Element.ALIGN_LEFT,
					cellFont));
			table.addCell(createCell(new Paragraph(dateFormatConversion(String.valueOf(endtime).substring(0, 19)).toUpperCase()), Element.ALIGN_LEFT, cellFont));
			if (endtime != null && startTime != null) {
				table.addCell(createCell(
						new Paragraph((String.valueOf(DateUtils.convertMiliSecToDayFormat(
								DateUtils.findTimeDifference(startTime.replace("+", "+0"), endtime))))),
						Element.ALIGN_RIGHT, cellFont));
			} else {
				table.addCell(createCell(new Paragraph(String.valueOf("0")), Element.ALIGN_RIGHT, cellFont));
			}
			testRuns.put(noOfRuns.get(i), passAndFailCount);
		}
		IntStream.range(0, 4).forEach(i-> {
			try {
				document.add(new Paragraph("\n"));
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		});
		document.add(table);
		RingChart ringChart = new RingChart();
		ringChart.createPDF(writer, pass, fail);
		GroupedStackedBarChart barChartToPDFExample2 = new GroupedStackedBarChart();
		barChartToPDFExample2.createBar(document, testRuns);
		logger.info("Schedule Report Created Successfully");
		document.close();
		}
		catch(Exception e)
		{
			logger.error("Exception occured while creating schedule report " +e.getMessage());
		}
	}

	public static String getEndTime(LocalDateTime endTime) {
		String endtime = null;
		try {
			DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
			LocalDateTime localDateTime = LocalDateTime.parse(endTime.toString(), inputFormatter);
			// Define the desired timezone offset (+05:30)
			ZoneOffset desiredOffset = ZoneOffset.ofHoursMinutes(5, 30);
			OffsetDateTime offsetDateTime = localDateTime.atOffset(desiredOffset);
			DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS ZZZ");
			endtime = offsetDateTime.format(outputFormatter);
			endtime = endtime.replace("+0530", "+05:30");
		} catch (DateTimeParseException  e) {
			logger.error("Failed to parse EndDate " + e.getMessage());
		}
		return endtime;

	}

	private static void startingDetails(Document document, String name, String projectName, String configurationName,
			String email, String startTime, String endTime) throws DocumentException, ParseException {
		
//		 String endtime = null;
		try {
			startTime = dateFormatConversion(startTime.substring(0,19)).toUpperCase();
			endTime = dateFormatConversion(getEndTime(LocalDateTime.parse(endTime)).substring(0,19)).toUpperCase();
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.error("Failed to convert Start and end date format " +e.getMessage());
		}
		
		PdfPTable firstLine = new PdfPTable(2);
		firstLine.setWidthPercentage(70);
		Font customFont = FontFactory.getFont("Arial", 10);
		PdfPCell cell1 = new PdfPCell(new Paragraph("Name : " + name, customFont));
		cell1.setBorderWidth(0);
		cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
		firstLine.addCell(cell1);
		PdfPCell cell2 = new PdfPCell(new Paragraph("Project : " + projectName, customFont));
		cell2.setBorderWidth(0);
		cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
		firstLine.addCell(cell2);
		PdfPCell cell3 = new PdfPCell(new Paragraph("Configuration : " + configurationName, customFont));
		cell3.setBorderWidth(0);
		cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
		firstLine.addCell(cell3);
		PdfPCell cell4 = new PdfPCell(new Paragraph("Email : " + email, FontFactory.getFont("Arial", 9)));
		cell4.setBorderWidth(0);
		cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
		firstLine.addCell(cell4);
		PdfPCell cell5 = new PdfPCell(new Paragraph("Start Time : " + startTime, customFont));
		cell5.setBorderWidth(0);
		cell5.setHorizontalAlignment(Element.ALIGN_LEFT);
		firstLine.addCell(cell5);
		PdfPCell cell6 = new PdfPCell(new Paragraph("End Time : " + endTime, customFont));
		cell6.setBorderWidth(0);
		cell6.setHorizontalAlignment(Element.ALIGN_LEFT);
		firstLine.addCell(cell6);
		firstLine.setHorizontalAlignment(Element.ALIGN_LEFT);
		document.add(firstLine);

	}
	
	private static String dateFormatConversion(String time) throws ParseException
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = dateFormat.parse(time);
        DateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aa");
        return simpleDateFormat.format(date);
	}

	private static PdfPTable createTable(int column) throws com.itextpdf.text.DocumentException {
		PdfPTable table = new PdfPTable(column);
		table.setWidthPercentage(100);
		return table;
	}

	private static void addTableHeader(PdfPTable table, Font font, String[] headers) {
		Arrays.stream(headers).map(header -> {
			PdfPCell headerCell = new PdfPCell(new Phrase(header, font));
			headerCell.setBackgroundColor(new BaseColor(167, 216, 241));
			headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			headerCell.setPadding(5);
			return headerCell;
		}).forEach(table::addCell);
	}

	private static PdfPCell createCell(Paragraph content, int horizontalAlignment, Font font) {
		PdfPCell cell = new PdfPCell(content);

		cell.setHorizontalAlignment(horizontalAlignment);
		cell.setVerticalAlignment(Element.ALIGN_CENTER);
		cell.setPadding(10);
		cell.setMinimumHeight(40f);

		return cell;
	}

	private static String failPercent(Integer pass, Integer fail) {
		try {
			return String.valueOf(((fail * 100) / (pass + fail)));
		} catch (Exception e) {
			return "0";
		}

	}

	private static String passPercent(Integer pass, Integer fail) {
		try {
			return String.valueOf(((pass * 100) / (pass + fail)));
		} catch (Exception e) {
			return "0";
		}
	}
}
