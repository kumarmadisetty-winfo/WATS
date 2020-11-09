package com.winfo.constants;

public enum DriverConstants {
	CHROME_DRIVER("webdriver.chrome.driver"),
	FIREFOX_DRIVER("webdriver.gecko.driver"),
	IE_DRIVER("webdriver.ie.driver"),
	DEFAULT_DOWNLOAD_DIRECTORY("download.default_directory")
	;
//	change public to private
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	DriverConstants(String value) {
		this.value = value;
	}
}
