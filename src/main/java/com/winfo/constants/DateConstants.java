package com.winfo.constants;

public enum DateConstants {
	
	DATE_FORMAT_MM_DD_YYYY_HH_MM_SS("MM/dd/yyyy HH:mm:ss"),
	DATE_FORMAT_TWO_MM_DD_YYYY_HH_MM_SS("dd-MMM-yyyy")
//	DATE_FORMAT_TWO_MM_DD_YYYY_HH_MM_SS("dd-MMM-yyyy HH-mm-ss")
	;
	public String value;
	
	DateConstants(String value) {
		this.value = value;
	}

}