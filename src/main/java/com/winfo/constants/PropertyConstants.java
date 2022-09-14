package com.winfo.constants;

public enum PropertyConstants {

	CHROME_DRIVER_PATH("chromeDriverPath"), IE_DRIVER_PATH("ieDriverPath"), FIREFOX_DRIVER_PATH("firefoxDriverPath"),
	DOWNLOAD_FILE_PATH("downlodFilePath"), TEST_DATA_WORKBOOK("testDataWorkBook"),
	TEST_DATA_SHEETNAME("testDataSheetName"), TEST_EXECUTION_WORKBOOK("testExecutionWorkBook"),
	TEST_EXECUTION_SHEETNAME("testExecutionSheetName"), OUTPUT_WORKBOOK("outputWorkBook"),
	OUTPUT_SHEETNAME1("outputSheetName1"), OUTPUT_SHEETNAME2("outputSheetName2"), BROWSER("browser"),
	EXECUTION_TIME("executionTime"), PROJECT_NAME("projectName"), URL("url"), PASSWORD("password"), LEDGER("ledger"),
	CATEGORY("category"), ACCOUNT("account"), IS_SKIP("IS_SKIP");

	private final String value;

	PropertyConstants(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
