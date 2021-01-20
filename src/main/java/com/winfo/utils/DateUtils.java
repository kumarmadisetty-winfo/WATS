package com.winfo.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.winfo.constants.DateConstants;

public class DateUtils {

	public static String getSysdate() {
		DateFormat dateFormat = new SimpleDateFormat(DateConstants.DATE_FORMAT_TWO_MM_DD_YYYY_HH_MM_SS.value);
		Date date = new Date();
   	 	String sysdate= dateFormat.format(date);
		return sysdate;
	}
	
	public static String getSysdate(String dateFormat) {
		DateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		Date date = new Date();
   	 	String sysdate= simpleDateFormat.format(date);
		return sysdate;
	}
	
}
