package com.winfo.services;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.persistence.Column;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.winfo.exception.WatsEBSCustomException;
import com.winfo.model.ScriptMaster;
import com.winfo.model.ScriptMetaData;

@Service
public class TemplateDownloadService {
	@Autowired
	private DataBaseEntry dataBaseEntry;

	private Object getValueByColumnName(Object object, String columnName)
			throws NoSuchFieldException, IllegalAccessException {
		Field field = Arrays.stream(object.getClass().getDeclaredFields())
				.filter(f -> f.isAnnotationPresent(Column.class))
				.filter(f -> f.getAnnotation(Column.class).name().equals(columnName)).findFirst()
				.orElseThrow(() -> new NoSuchFieldException("No such field found for column name: " + columnName));
		field.setAccessible(true);
		return field.get(object);
	}

	private void insertScriptMetaData(ScriptMaster scriptMaster, Sheet sheet) {
		int row = 11;
		List<ScriptMetaData> sortedList = scriptMaster.getScriptMetaDatalist().stream()
				.sorted(Comparator.comparingInt(ScriptMetaData::getLineNumber)).collect(Collectors.toList());
		for (ScriptMetaData scriptMetaData : sortedList) {
			String actionMeaning = dataBaseEntry.getActionMeaningScriptIdAndLineNumber(
					scriptMetaData.getScriptMaster().getScriptId(), scriptMetaData.getScriptMetaDataId());
			setCellValues(sheet, row, scriptMetaData.getLineNumber(), scriptMetaData.getStepDesc(),
					scriptMetaData.getInputParameter(), actionMeaning, scriptMetaData.getUniqueMandatory(),
					scriptMetaData.getDatatypes());
			row++;
		}
	}

	private void setCellValues(Sheet sheet, int row, Object... values) {
		for (int i = 0; i < values.length; i++) {
			Cell cell = sheet.getRow(row).getCell(i);
			if (values[i] != null) {
				cell.setCellValue(values[i].toString());
			}
		}
	}

	private List<String> getCodes(String codeType) {
		return dataBaseEntry.lookUpCodes(codeType);
	}

	private List<List<String>> getScriptDetailsColumns(String... codeTypes) {
		List<List<String>> columns = new ArrayList<>();
		for (String codeType : codeTypes) {
			columns.add(getCodes(codeType));
		}
		return columns;
	}

	private void setCellStyle(Cell cell, Font font, FillPatternType fillPatternType, IndexedColors fillColor,
			BorderStyle borderBottom, BorderStyle borderLeft, BorderStyle borderRight, BorderStyle borderTop) {
		Map<String, Object> properties = new HashMap<>();
		properties.put(CellUtil.FILL_PATTERN, fillPatternType);
		properties.put(CellUtil.FILL_FOREGROUND_COLOR, fillColor.getIndex());
		properties.put(CellUtil.BORDER_BOTTOM, borderBottom);
		properties.put(CellUtil.BORDER_LEFT, borderLeft);
		properties.put(CellUtil.BORDER_RIGHT, borderRight);
		properties.put(CellUtil.BORDER_TOP, borderTop);
		properties.put(CellUtil.FONT, font);
		CellUtil.setCellStyleProperties(cell, properties);
	}

	private CellStyle createCellStyle(Workbook workbook, Font font, BorderStyle borderStyle,
			IndexedColors foregroundColor, IndexedColors backgroundColor, FillPatternType fillPatternType,
			HorizontalAlignment alignment) {
		CellStyle style = workbook.createCellStyle();
		style.setFont(font);
		style.setBorderBottom(borderStyle);
		style.setBorderTop(borderStyle);
		style.setBorderRight(borderStyle);
		style.setBorderLeft(borderStyle);
		style.setAlignment(alignment != null ? alignment : HorizontalAlignment.LEFT);
		style.setFillForegroundColor(
				foregroundColor != null ? foregroundColor.getIndex() : IndexedColors.WHITE.getIndex());
		style.setFillBackgroundColor(
				backgroundColor != null ? backgroundColor.getIndex() : IndexedColors.WHITE.getIndex());
		style.setFillPattern(fillPatternType != null ? fillPatternType : FillPatternType.NO_FILL);
		return style;
	}

	private Sheet createListSheet(Workbook workbook, Map<String, String[]> targetApplicationActions) {
		Sheet sheet = workbook.createSheet("ListSheet");

		int columnIndex = 0;
		for (String key : targetApplicationActions.keySet()) {
			int rowIndex = 0;
			Row targetApplicationRow = sheet.getRow(rowIndex);
			if (targetApplicationRow == null) {
				targetApplicationRow = sheet.createRow(rowIndex);
			}
			rowIndex++;
			targetApplicationRow.createCell(columnIndex).setCellValue(key);
			String[] items = targetApplicationActions.get(key);
			for (String item : items) {
				Row row = sheet.getRow(rowIndex);
				if (row == null) {
					row = sheet.createRow(rowIndex);
				}
				rowIndex++;
				row.createCell(columnIndex).setCellValue(item);
			}
			String colLetter = CellReference.convertNumToColString(columnIndex);
			Name namedRange = workbook.createName();
			namedRange.setNameName(key.replace(" ", "_"));
			namedRange.setRefersToFormula("ListSheet!$" + colLetter + "$2:$" + colLetter + "$" + rowIndex);
			columnIndex++;
		}

		String colLetter = CellReference.convertNumToColString(columnIndex - 1);
		Name namedRange = workbook.createName();
		namedRange.setNameName("Categories1");
		namedRange.setRefersToFormula("ListSheet!$A$1:$" + colLetter + "$1");

		return sheet;
	}

	public ResponseEntity<StreamingResponseBody> generateTemplate(Optional<Integer> scriptId) {
		try {
			ScriptMaster scriptMasterData = null;
			if (scriptId.isPresent()) {
				scriptMasterData = dataBaseEntry.getScriptDetailsByScriptId(scriptId.get());
			}

			List<List<String>> listOfScriptDetailsColumn = getScriptDetailsColumns("PRODUCT_VERSION", "PROCESS",
					"MODULE", "ROLE", "STATUS", "PRIORITY", "STANDARD");

			List<String> listOfTargetApplication = getCodes("TARGET_APPLICATION");

			Map<String, String[]> mapOfTargetApplicationAndAction = listOfTargetApplication.stream()
					.collect(Collectors.toMap(targetApplication -> targetApplication,
							targetApplication -> dataBaseEntry.getActionByTargetApplication(targetApplication)
									.toArray(String[]::new),
							(existingValue, newValue) -> newValue, LinkedHashMap::new));

			Workbook workbook = new XSSFWorkbook();

			List<String[]> list = new ArrayList<>();
			list.add(new String[] { "SCRIPT NUMBER", "TEST CASE NAME", "TARGET APPLICATION", "PRODUCT VERSION",
					"PROCESS AREA", "MODULE", "SUB PROCESS AREA" });
			list.add(new String[] { "ROLE", "TEST SCRIPT STATUS", "TEST CASE DESCRIPTION", "EXPECTED RESULT",
					"PRIORITY", "DEPENDENCY", "STANDARD CUSTOM" });
			list.add(new String[] { "CUSTOMER ID", "CUSTOMISATION REFERENCE", "ATTRIBUTE1", "ATTRIBUTE2", "ATTRIBUTE3",
					"ATTRIBUTE4", "ATTRIBUTE5" });
			list.add(new String[] { "ATTRIBUTE6", "ATTRIBUTE7", "ATTRIBUTE8", "ATTRIBUTE9", "ATTRIBUTE10", "", "" });

			List<String> scriptLineHeaders = Arrays.asList("LINE NUMBER", "STEP DESCRIPTION", "INPUT PARAMETER",
					"ACTION", "UNIQUE/MANDATORY", "DATATYPES");

			List<String> listOfDropdownKeys = Arrays.asList("PRODUCT VERSION", "PROCESS AREA", "MODULE", "ROLE",
					"TEST SCRIPT STATUS", "PRIORITY", "STANDARD CUSTOM");

			Sheet sheet = createListSheet(workbook, mapOfTargetApplicationAndAction);

			List<Name> listOfFormula = new ArrayList<>();

			for (int i = 0; i < listOfScriptDetailsColumn.size(); i++) {
				List<String> listOfScriptDetails = listOfScriptDetailsColumn.get(i);
				String dropdownKey = listOfDropdownKeys.get(i).replace(" ", "_");
				Name newNameRange = workbook.createName();
				newNameRange.setNameName(dropdownKey);

				int rowIndex = 0;
				for (String scriptDetail : listOfScriptDetails) {
					Row row = sheet.getRow(rowIndex);
					if (row == null) {
						row = sheet.createRow(rowIndex);
					}
					row.createCell(i + mapOfTargetApplicationAndAction.size()).setCellValue(scriptDetail);
					rowIndex++;
				}

				String colLetter = CellReference.convertNumToColString(i + mapOfTargetApplicationAndAction.size());
				String reference = "ListSheet!$" + colLetter + "$1:$" + colLetter + "$" + rowIndex;
				newNameRange.setRefersToFormula(reference);

				listOfFormula.add(newNameRange);
			}

			sheet.protectSheet("Winfo@123");
			// unselect that sheet because we will hide it later
			sheet.setSelected(false);

			// visible data sheet
			sheet = workbook.createSheet("Winfo Test Automation Metadata");

			Font font = workbook.createFont();
			font.setFontHeightInPoints((short) 9);
			font.setFontName("Arial");
			font.setBold(true);

			CellStyle styleForScriptDetails = createCellStyle(workbook, font, BorderStyle.THIN,
					IndexedColors.SKY_BLUE, IndexedColors.SKY_BLUE, FillPatternType.FINE_DOTS,
					HorizontalAlignment.CENTER);

			CellStyle styleForScriptMetaData = createCellStyle(workbook, null, BorderStyle.THIN, null, null, null,
					null);

			CellStyle styleForHeaders = createCellStyle(workbook, font, BorderStyle.THIN, IndexedColors.SKY_BLUE,
					IndexedColors.SKY_BLUE, FillPatternType.FINE_DOTS, null);

			for (int index = 0; index < 511; index++) {
				sheet.createRow(index);
				for (int j = 0; j < scriptLineHeaders.size(); j++) {
					if (index >= 11) {
						Cell cell = sheet.getRow(index).createCell(j);
						cell.setCellStyle(styleForScriptMetaData);
					}
				}
			}

			if (scriptId.isPresent()) {
				insertScriptMetaData(scriptMasterData, sheet);
			}

			Map<String, String> map = new LinkedHashMap<>();

			int column = 0;
			for (String[] row : list) {
				for (int i = 1; i <= row.length; i++) {
					Cell keyCell = sheet.getRow(i).createCell(column);
					setCellStyle(keyCell, font, FillPatternType.SOLID_FOREGROUND, IndexedColors.LEMON_CHIFFON,
							BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
					keyCell.setCellValue(row[i - 1]);
					column++;

					Cell valueCell = sheet.getRow(i).createCell(column);
					valueCell.setCellStyle(styleForScriptMetaData);

					String value = "";
					String key = row[i - 1].replace("TEST CASE", "SCENARIO").replace(" ", "_");
					if (!row[i - 1].isEmpty() && scriptId.isPresent()) {
						Object result = getValueByColumnName(scriptMasterData, key);
						if ("PROCESS AREA".equalsIgnoreCase(row[i - 1]) || "MODULE".equalsIgnoreCase(row[i - 1])
								|| "PRIORITY".equalsIgnoreCase(row[i - 1])) {
							if ("PROCESS AREA".equalsIgnoreCase(row[i - 1])) {
								result = dataBaseEntry.getMeaningByTargetCode(result.toString(), "PROCESS");
							} else {
								result = dataBaseEntry.getMeaningByTargetCode(result.toString(), row[i - 1]);
							}
						}
						value = (result != null) ? result.toString() : "";
					}
					valueCell.setCellValue(value);

					if (listOfDropdownKeys.contains(row[i - 1])) {
						map.put(key, i + "," + column);
					}

					column--;
					sheet.autoSizeColumn(i);
					sheet.setColumnWidth(column, Math.max(sheet.getColumnWidth(column), 20));

				}
				column += 2;
			}
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 7));
			Cell cellKey1 = sheet.getRow(0).createCell(0);
			font.setFontHeightInPoints((short) 14);
			styleForScriptDetails.setFont(font);
			cellKey1.setCellStyle(styleForScriptDetails);
			cellKey1.setCellValue("Test Script Details");
			Cell cellEmpty1 = sheet.getRow(0).createCell(4);
			cellEmpty1.setCellStyle(styleForScriptDetails);
			cellEmpty1.setCellValue("Customization Details");

			RegionUtil.setBorderTop(BorderStyle.THIN, new CellRangeAddress(0, 0, 0, 3), sheet);
			RegionUtil.setBorderBottom(BorderStyle.THIN, new CellRangeAddress(0, 0, 0, 3), sheet);
			RegionUtil.setBorderLeft(BorderStyle.THIN, new CellRangeAddress(0, 0, 0, 3), sheet);
			RegionUtil.setBorderRight(BorderStyle.THIN, new CellRangeAddress(0, 0, 0, 3), sheet);

			RegionUtil.setBorderTop(BorderStyle.THIN, new CellRangeAddress(0, 0, 4, 7), sheet);
			RegionUtil.setBorderBottom(BorderStyle.THIN, new CellRangeAddress(0, 0, 4, 7), sheet);
			RegionUtil.setBorderLeft(BorderStyle.THIN, new CellRangeAddress(0, 0, 4, 7), sheet);
			RegionUtil.setBorderRight(BorderStyle.THIN, new CellRangeAddress(0, 0, 4, 7), sheet);
			int headingValue = 0;
			for (String heading : scriptLineHeaders) {
				Cell cellKey = sheet.getRow(10).createCell(headingValue++);
				cellKey.setCellStyle(styleForHeaders);
				cellKey.setCellValue(heading);
			}
			sheet.setColumnWidth(4, 100 * 256);

			for (Map.Entry<String, String> entry : map.entrySet()) {
				DataValidationHelper dvHelper = sheet.getDataValidationHelper();
				DataValidationConstraint dvConstraint = dvHelper.createFormulaListConstraint(entry.getKey());
				CellRangeAddressList addressList = new CellRangeAddressList();
				String[] arr = entry.getValue().split(",");
				int rowRange = Integer.parseInt(arr[0]);
				int columnRange = Integer.parseInt(arr[1]);
				addressList.addCellRangeAddress(rowRange, columnRange, rowRange, columnRange);
				DataValidation validation = dvHelper.createValidation(dvConstraint, addressList);
				validation.setShowErrorBox(true);
				validation.setEmptyCellAllowed(false);
				validation.createErrorBox("Invalid Value", "Please select a value from the dropdown list.");
				validation.setSuppressDropDownArrow(true);
				sheet.addValidationData(validation);
			}

			DataValidationHelper dvHelper1 = sheet.getDataValidationHelper();
			// data validation for categories in A2:
			DataValidationConstraint dvConstraint1 = dvHelper1.createFormulaListConstraint("Categories1");
			CellRangeAddressList addressList1 = new CellRangeAddressList(3, 3, 1, 1);
			DataValidation validation1 = dvHelper1.createValidation(dvConstraint1, addressList1);
			validation1.setShowErrorBox(true);
			validation1.setEmptyCellAllowed(false);
			validation1.createErrorBox("Invalid Value", "Please select a value from the dropdown list.");
			validation1.setSuppressDropDownArrow(true);
			sheet.addValidationData(validation1);

			// data validation for items of the selected category in B2:
			dvConstraint1 = dvHelper1.createFormulaListConstraint("INDIRECT(SUBSTITUTE($B$4,\" \",\"_\"))");
			addressList1 = new CellRangeAddressList(11, 511, 3, 3);
			validation1 = dvHelper1.createValidation(dvConstraint1, addressList1);
			validation1.setShowErrorBox(true);
			validation1.setEmptyCellAllowed(false);
			validation1.createErrorBox("Invalid Value", "Please select a value from the dropdown list.");
			validation1.setSuppressDropDownArrow(true);
			sheet.addValidationData(validation1);

			IntStream.range(0, scriptLineHeaders.size()).forEach(sheet::autoSizeColumn);
			// hide the ListSheet
			workbook.setSheetHidden(0, true);
			// set Winfo Test sheet active
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