package com.winfo.constants;
public enum BrowserConstants{
	
	CHROME("chrome"),
	FIREFOX("firefox"),
	IE("ie")
	;
//	change public to private
	private String value;

	BrowserConstants(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}


}



