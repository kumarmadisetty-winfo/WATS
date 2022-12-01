package com.winfo.services;

import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.winfo.exception.WatsEBSCustomException;

@Service
public class TemplateDownloadService {
	@Autowired
	private DataBaseEntry dataBaseEntry;

	public ResponseEntity<StreamingResponseBody> generateTemplate() {

		try {
			List<String> listOfModuls = dataBaseEntry.getAllModules();

			Workbook workbook = new XSSFWorkbook();

			// hidden sheet for list values
			Sheet sheet = workbook.createSheet("ListSheet");

			String[] arrOfHeaders = { "SCRIPT ID", "SCRIPT NUMBER", "PROCESS AREA", "SUB PROCESS AREA", "MODULE",
					"ROLE", "TEST CASE NAME", "TEST CASE DESCRIPTION", "EXPECTED RESULT", "SELENIUM TEST SCRIPT_NAME",
					"PRIORITY", "DEPENDENCY", "PRODUCT VERSION", "STANDARD CUSTOM", "TEST SCRIPT STATUS", "AUTHOR",
					"CUSTOMER ID", "CUSTOMISATION REFERENCE", "ATTRIBUTE1", "ATTRIBUTE2", "ATTRIBUTE3", "ATTRIBUTE4",
					"ATTRIBUTE5", "ATTRIBUTE6", "ATTRIBUTE7", "ATTRIBUTE8", "ATTRIBUTE9", "ATTRIBUTE10",
					"SCRIPT METADATA ID", "SCRIPT ID", "SCRIPT NUMBER", "LINE NUMBER", "STEP DESCRIPTION",
					"INPUT PARAMETER", "ACTION", "VALIDATION TYPE", "VALIDATION NAME", "UNIQUE/MANDATORY", "DATATYPES",
					"TARGET APPLICATION" };

			Row row;
			int r = 0;
			Name namedRange;
			namedRange = workbook.createName();
			namedRange.setNameName("Categories");
			int max = 0;
			for (String referenceValue : listOfModuls) {
				max = Math.max(referenceValue.length(), max);
				row = sheet.getRow(r);
				if (row == null) {
					row = sheet.createRow(r);
				}
				r++;
				row.createCell(0).setCellValue(referenceValue);
				String colLetter = CellReference.convertNumToColString(0);
				String reference = "ListSheet!$" + colLetter + "$1:$" + colLetter + "$" + r;
				namedRange.setRefersToFormula(reference);
			}
			sheet.protectSheet("Winfo@123");
			// unselect that sheet because we will hide it later
			sheet.setSelected(false);
			
			// visible data sheet
			sheet = workbook.createSheet("Winfo Test Automation Metadata");
			CellStyle style2 = workbook.createCellStyle();
			style2.setFillForegroundColor(IndexedColors.WHITE.getIndex());
			style2.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
			style2.setFillPattern(FillPatternType.FINE_DOTS);
			style2.setBorderBottom(BorderStyle.NONE);
			style2.setBorderTop(BorderStyle.NONE);
			style2.setBorderRight(BorderStyle.NONE);
			style2.setBorderLeft(BorderStyle.NONE);
			Font font1 = workbook.createFont();

			font1.setFontHeightInPoints((short) 18);
			font1.setFontName("Arial");
			font1.setBold(true);
			style2.setFont(font1);
			for (int i = 0; i < 5; i++) {
				sheet.createRow(i);
				for (int j = 0; j < arrOfHeaders.length; j++) {
					Cell cell = sheet.getRow(i).createCell(j);
					cell.setCellStyle(style2);
				}
			}
			sheet.getRow(2).getCell(0).setCellValue("Winfo Test Automation Metadata");

			sheet.createRow(5);
			CellStyle style = workbook.createCellStyle();
			Font font = workbook.createFont();

			font.setFontHeightInPoints((short) 9);
			font.setFontName("Arial");
			font.setBold(true);
			style.setFont(font);

			style.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
			style.setFillBackgroundColor(IndexedColors.SKY_BLUE.getIndex());
			style.setFillPattern(FillPatternType.FINE_DOTS);

			style.setBorderBottom(BorderStyle.THICK);
			style.setBorderTop(BorderStyle.THICK);
			style.setBorderRight(BorderStyle.THICK);
			style.setBorderLeft(BorderStyle.THICK);

			CellStyle style1 = workbook.createCellStyle();
			style1.setBorderBottom(BorderStyle.MEDIUM);
			style1.setBorderTop(BorderStyle.MEDIUM);
			style1.setBorderRight(BorderStyle.MEDIUM);
			style1.setBorderLeft(BorderStyle.MEDIUM);

			for (int i = 6; i < 506; i++) {
				sheet.createRow(i);
				for (int j = 0; j < arrOfHeaders.length; j++) {
					Cell cell = sheet.getRow(i).createCell(j);
					cell.setCellStyle(style1);
				}
			}

			for (int i = 0; i < arrOfHeaders.length; i++) {
				Cell cell = sheet.getRow(5).createCell(i);
				cell.setCellStyle(style);
				cell.setCellValue(arrOfHeaders[i]);
				if (i != 0) {
					sheet.autoSizeColumn(i);
				}
			}
			sheet.setColumnWidth(4, max);
			// data validations
			DataValidationHelper dvHelper = sheet.getDataValidationHelper();
			DataValidationConstraint dvConstraint = dvHelper.createFormulaListConstraint("Categories");
			CellRangeAddressList addressList = new CellRangeAddressList();
			addressList.addCellRangeAddress(6, 4, 505, 4);
			DataValidation validation = dvHelper.createValidation(dvConstraint, addressList);
			sheet.addValidationData(validation);

			// hide the ListSheet
			workbook.setSheetHidden(0, true);
			// set Sheet1 active
			workbook.setActiveSheet(1);

			return ResponseEntity.ok()
					.header("Content-Disposition",
							"attachment; filename=\"Winfo Test Automation Metadata Template_" + new Date() + ".xlsx\"")
					.body(out -> {
						workbook.write(out);
						workbook.close();
					});
		} catch (Exception e) {
			e.printStackTrace();
			throw new WatsEBSCustomException(500, "Not able to generate excel templete", e);
		}
	}
}
