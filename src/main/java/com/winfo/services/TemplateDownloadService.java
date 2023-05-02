package com.winfo.services;

import java.io.IOException;
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
import java.util.concurrent.atomic.AtomicInteger;
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
import org.springframework.http.HttpStatus;
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
		List<ScriptMetaData> sortedList = scriptMaster.getScriptMetaDatalist().stream()
				.sorted(Comparator.comparingInt(ScriptMetaData::getLineNumber)).collect(Collectors.toList());
		IntStream.range(0, sortedList.size()).forEach(index -> {
			ScriptMetaData scriptMetaData = sortedList.get(index);
			String actionMeaning = dataBaseEntry.getActionMeaningScriptIdAndLineNumber(
					scriptMetaData.getScriptMaster().getScriptId(), scriptMetaData.getScriptMetaDataId());
			setCellValues(sheet, 11 + index, scriptMetaData.getLineNumber(), scriptMetaData.getStepDesc(),
					scriptMetaData.getInputParameter(), actionMeaning, scriptMetaData.getUniqueMandatory(),
					scriptMetaData.getDatatypes());
		});
	}

	private void setCellValues(Sheet sheet, int row, Object... values) {
		IntStream.range(0, values.length).forEach(index -> {
			Cell cell = sheet.getRow(row).getCell(index);
			Optional.ofNullable(values[index]).map(Object::toString).ifPresent(cell::setCellValue);
		});
	}

	private List<String> getCodes(String codeType) {
		return dataBaseEntry.lookUpCodes(codeType);
	}

	private List<List<String>> getScriptDetailsColumns(String... codeTypes) {
		return Arrays.stream(codeTypes).map(this::getCodes).collect(Collectors.toList());
	}

	private void setCellStyle(Cell cell, Font font, FillPatternType fillPatternType, IndexedColors fillColor, BorderStyle border) {
		Map<String, Object> properties = new HashMap<>();
		properties.put(CellUtil.FILL_PATTERN, fillPatternType);
		properties.put(CellUtil.FILL_FOREGROUND_COLOR, fillColor.getIndex());
		properties.put(CellUtil.BORDER_BOTTOM, border);
		properties.put(CellUtil.BORDER_LEFT, border);
		properties.put(CellUtil.BORDER_RIGHT, border);
		properties.put(CellUtil.BORDER_TOP, border);
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

	private Sheet createListSheet(Workbook workbook, Map<String, String[]> targetApplicationActions) throws IOException {
		Sheet sheet = workbook.createSheet("ListSheet");

		AtomicInteger columnIndex = new AtomicInteger(0);
		targetApplicationActions.forEach((key, items) -> {
			AtomicInteger rowIndex = new AtomicInteger(0);
			Row targetApplicationRow = sheet.getRow(rowIndex.get()) == null ? sheet.createRow(rowIndex.getAndIncrement()) : sheet.getRow(rowIndex.getAndIncrement());
			targetApplicationRow.createCell(columnIndex.get()).setCellValue(key);
			Arrays.stream(items).forEach(item -> {
				Row row = sheet.getRow(rowIndex.get()) == null ? sheet.createRow(rowIndex.getAndIncrement()) : sheet.getRow(rowIndex.getAndIncrement());
				row.createCell(columnIndex.get()).setCellValue(item);
			});
			String colLetter = CellReference.convertNumToColString(columnIndex.get());
			Name namedRange = workbook.createName();
			namedRange.setNameName(key.replace(" ", "_"));
			namedRange.setRefersToFormula("ListSheet!$" + colLetter + "$2:$" + colLetter + "$" + rowIndex.get());
			columnIndex.getAndIncrement();
		});

		String colLetter = CellReference.convertNumToColString(columnIndex.get() - 1);
		Name namedRange = workbook.createName();
		namedRange.setNameName("Categories1");
		namedRange.setRefersToFormula("ListSheet!$A$1:$" + colLetter + "$1");
		return sheet;
	}

	private void setBorder(BorderStyle borderStyle, int firstRow, int lastRow, int firstCol, int lastCol, Sheet sheet) {
		RegionUtil.setBorderTop(borderStyle, new CellRangeAddress(firstRow, lastRow, firstCol, lastCol), sheet);
		RegionUtil.setBorderBottom(borderStyle, new CellRangeAddress(firstRow, lastRow, firstCol, lastCol), sheet);
		RegionUtil.setBorderLeft(borderStyle, new CellRangeAddress(firstRow, lastRow, firstCol, lastCol), sheet);
		RegionUtil.setBorderRight(borderStyle, new CellRangeAddress(firstRow, lastRow, firstCol, lastCol), sheet);
	}

	public ResponseEntity<StreamingResponseBody> generateTemplate(Optional<Integer> scriptId) {
		try {
			ScriptMaster scriptMasterData = scriptId.isPresent() ? dataBaseEntry.getScriptDetailsByScriptId(scriptId.get()) : null;

			List<List<String>> listOfScriptDetailsColumn = getScriptDetailsColumns("PRODUCT_VERSION", "PROCESS",
					"MODULE", "ROLE", "STATUS", "PRIORITY", "STANDARD");

			List<String> listOfTargetApplication = getCodes("TARGET_APPLICATION");

			Map<String, String[]> mapOfTargetApplicationAndAction = listOfTargetApplication.stream()
					.collect(Collectors.toMap(targetApplication -> targetApplication,
							targetApplication -> dataBaseEntry.getActionByTargetApplication(targetApplication)
									.toArray(String[]::new),
							(existingValue, newValue) -> newValue, LinkedHashMap::new));

			Workbook workbook = new XSSFWorkbook();

			List<String[]> listOfScriptColumnName = new ArrayList<>();
			listOfScriptColumnName.add(new String[] { "SCRIPT NUMBER", "TEST CASE NAME", "TARGET APPLICATION", "PRODUCT VERSION",
					"PROCESS AREA", "MODULE", "SUB PROCESS AREA" });
			listOfScriptColumnName.add(new String[] { "ROLE", "TEST SCRIPT STATUS", "TEST CASE DESCRIPTION", "EXPECTED RESULT",
					"PRIORITY", "DEPENDENCY", "STANDARD CUSTOM" });
			listOfScriptColumnName.add(new String[] { "CUSTOMER ID", "CUSTOMISATION REFERENCE", "ATTRIBUTE1", "ATTRIBUTE2", "ATTRIBUTE3",
					"ATTRIBUTE4", "ATTRIBUTE5" });
			listOfScriptColumnName.add(new String[] { "ATTRIBUTE6", "ATTRIBUTE7", "ATTRIBUTE8", "ATTRIBUTE9", "ATTRIBUTE10", "", "" });

			List<String> scriptLineHeaders = Arrays.asList("LINE NUMBER", "STEP DESCRIPTION", "INPUT PARAMETER",
					"ACTION", "UNIQUE/MANDATORY", "DATATYPES");

			List<String> listOfDropdownKeys = Arrays.asList("PRODUCT VERSION", "PROCESS AREA", "MODULE", "ROLE",
					"TEST SCRIPT STATUS", "PRIORITY", "STANDARD CUSTOM");

			Sheet valueSheet = createListSheet(workbook, mapOfTargetApplicationAndAction);

			IntStream.range(0, listOfScriptDetailsColumn.size()).mapToObj(i -> {
				List<String> listOfScriptDetails = listOfScriptDetailsColumn.get(i);
				String dropdownKey = listOfDropdownKeys.get(i).replace(" ", "_");
				Name newNameRange = workbook.createName();
				newNameRange.setNameName(dropdownKey);

				IntStream.range(0, listOfScriptDetails.size()).forEach(rowIndex -> {
					Row row = valueSheet.getRow(rowIndex);
					if (row == null) {
						row = valueSheet.createRow(rowIndex);
					}
					int cellIndex = i + mapOfTargetApplicationAndAction.size();
					row.createCell(cellIndex).setCellValue(listOfScriptDetails.get(rowIndex));
				});

				String colLetter = CellReference.convertNumToColString(i + mapOfTargetApplicationAndAction.size());
				String reference = "ListSheet!$" + colLetter + "$1:$" + colLetter + "$" + listOfScriptDetails.size();
				newNameRange.setRefersToFormula(reference);

				return newNameRange;
			}).collect(Collectors.toList());

			valueSheet.protectSheet("Winfo@123");
			// unselect that sheet because we will hide it later
			valueSheet.setSelected(false);

			// visible data sheet
			Sheet automationSheet = workbook.createSheet("Winfo Test Automation Metadata");

			Font font = workbook.createFont();
			font.setFontHeightInPoints((short) 9);
			font.setFontName("Arial");
			font.setBold(true);

			CellStyle styleForScriptDetails = createCellStyle(workbook, font, BorderStyle.THIN, IndexedColors.SKY_BLUE,
					IndexedColors.SKY_BLUE, FillPatternType.FINE_DOTS, HorizontalAlignment.CENTER);

			CellStyle styleForScriptMetaData = createCellStyle(workbook, null, BorderStyle.THIN, null, null, null,
					null);

			CellStyle styleForHeaders = createCellStyle(workbook, font, BorderStyle.THIN, IndexedColors.SKY_BLUE,
					IndexedColors.SKY_BLUE, FillPatternType.FINE_DOTS, null);

			IntStream.range(0, 511).forEach(index -> {
				automationSheet.createRow(index);
				if (index >= 11) {
					IntStream.range(0, scriptLineHeaders.size())
							.mapToObj(j -> automationSheet.getRow(index).createCell(j))
							.forEach(cell -> cell.setCellStyle(styleForScriptMetaData));
				}
			});

			scriptId.ifPresent(id -> insertScriptMetaData(scriptMasterData, automationSheet));

			Map<String, String> map = new LinkedHashMap<>();

			int column = 0;
			for (String[] row : listOfScriptColumnName) {
				for (int i = 1; i <= row.length; i++) {
					Cell keyCell = automationSheet.getRow(i).createCell(column);
					setCellStyle(keyCell, font, FillPatternType.SOLID_FOREGROUND, IndexedColors.LEMON_CHIFFON, BorderStyle.THIN);
					keyCell.setCellValue(row[i - 1]);
					column++;

					Cell valueCell = automationSheet.getRow(i).createCell(column);
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
					automationSheet.autoSizeColumn(i);
					automationSheet.setColumnWidth(column, Math.max(automationSheet.getColumnWidth(column), 20));

				}
				column += 2;
			}
			automationSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
			automationSheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 7));

			IntStream.of(0, 4).forEach(index -> {
				Cell cell = automationSheet.getRow(0).createCell(index);
				font.setFontHeightInPoints((short) 14);
				styleForScriptDetails.setFont(font);
				cell.setCellStyle(styleForScriptDetails);
				cell.setCellValue(index == 0 ? "Test Script Details" : "Customization Details");
			});

			setBorder(BorderStyle.THIN, 0, 0, 0, 3, automationSheet);
			setBorder(BorderStyle.THIN, 0, 0, 4, 7, automationSheet);

			IntStream.range(0, scriptLineHeaders.size()).forEach(index -> {
				Cell cellKey = automationSheet.getRow(10).createCell(index);
				cellKey.setCellStyle(styleForHeaders);
				cellKey.setCellValue(scriptLineHeaders.get(index));
			});

			automationSheet.setColumnWidth(4, 100 * 256);

			map.forEach((key, value) -> {
				DataValidationHelper dvHelper = automationSheet.getDataValidationHelper();
				DataValidationConstraint dvConstraint = dvHelper.createFormulaListConstraint(key);
				CellRangeAddressList addressList = new CellRangeAddressList();
				String[] arr = value.split(",");
				int rowRange = Integer.parseInt(arr[0]);
				int columnRange = Integer.parseInt(arr[1]);
				addressList.addCellRangeAddress(rowRange, columnRange, rowRange, columnRange);
				DataValidation validation = dvHelper.createValidation(dvConstraint, addressList);
				validation.setShowErrorBox(true);
				validation.setEmptyCellAllowed(false);
				validation.createErrorBox("Invalid Value", "Please select a value from the dropdown list.");
				validation.setSuppressDropDownArrow(true);
				automationSheet.addValidationData(validation);
			});

			DataValidationHelper dvHelper = automationSheet.getDataValidationHelper();

			// data validation for categories in A2:
			DataValidationConstraint dvConstraint = dvHelper.createFormulaListConstraint("Categories1");
			CellRangeAddressList addressList = new CellRangeAddressList(3, 3, 1, 1);
			DataValidation validation = dvHelper.createValidation(dvConstraint, addressList);
			validation.setShowErrorBox(true);
			validation.setEmptyCellAllowed(false);
			validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
			validation.createErrorBox("Invalid Value", "Please select a value from the dropdown list.");
			validation.setSuppressDropDownArrow(true);
			automationSheet.addValidationData(validation);

			// data validation for items of the selected category in B2:
			dvConstraint = dvHelper.createFormulaListConstraint("INDIRECT(SUBSTITUTE($B$4,\" \",\"_\"))");
			addressList = new CellRangeAddressList(11, 511, 3, 3);
			validation = dvHelper.createValidation(dvConstraint, addressList);
			validation.setShowErrorBox(true);
			validation.setEmptyCellAllowed(false);
			validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
			validation.createErrorBox("Invalid Value", "Please select a value from the dropdown list.");
			validation.setSuppressDropDownArrow(true);
			automationSheet.addValidationData(validation);

			automationSheet.getDataValidations().forEach(automationSheet::addValidationData);

			IntStream.range(0, scriptLineHeaders.size()).forEach(automationSheet::autoSizeColumn);
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
			throw new WatsEBSCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Not able to generate excel templete", e);
		}
	}
}