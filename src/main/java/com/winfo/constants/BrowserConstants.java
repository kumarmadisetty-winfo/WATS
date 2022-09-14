package com.winfo.constants;
@SuppressWarnings("unused")
public enum BrowserConstants{
	
	CHROME("chrome"),
	FIREFOX("firefox"),
	IE("ie"),
	EDGE("edge"),
	AWT_HEADLESS("java.awt.headless"),
	PROFILE_DEFAULT_CONTENT_SETTING("profile.default_content_settings.popups"),
	DOWNLOAD_DEFAULT_DIRECTORY("download.default_directory"),
	START_MAXIMIZED("start-maximized"),
	NO_SENDBOX("--no-sandbox"),
	ENABLE_AUTOMATION("--enable-automation"),
	TEST_TYPE("test-type=browser"),
	DISABLE_INFOBARS("disable-infobars"),
	MARIONETTE("marionette")
	;
	
	private final String value;
	
	BrowserConstants(String value) {
		this.value = value;
	}
	
	 public String getValue() {
			return value;
	    }
}



