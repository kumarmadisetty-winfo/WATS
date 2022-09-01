package com.winfo.constants;

public enum DriverConstants {
	CHROME_DRIVER("webdriver.chrome.driver"),
	FIREFOX_DRIVER("webdriver.gecko.driver"),
	MS_EDGE("webdriver.edge.driver"),
	DEFAULT_DOWNLOAD_DIRECTORY("download.default_directory")
	;

	public String value;

	DriverConstants(String value) {
		this.value = value;
	}
}
