package com.winfo.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.winfo.constants.DateConstants;

public class DateUtils {

	public static String getSysdate() {
		DateFormat dateFormat = new SimpleDateFormat(DateConstants.DATE_FORMAT_TWO_MM_DD_YYYY_HH_MM_SS.getValue());
		Date date = new Date();
		return dateFormat.format(date);
	}

	public static String getSysdate(String dateFormat) {
		DateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		Date date = new Date();
		return simpleDateFormat.format(date);
	}

	public static long findTimeDifference(String startTime, String endTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long diff = 0;
		try {
			Date d1 = sdf.parse(startTime);
			Date d2 = sdf.parse(endTime);

			diff = d2.getTime() - d1.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return diff;
	}

	public static String convertMiliSecToDayFormat(long diffInTime) {
		long time = 0;
		String days = (time = diffInTime / (1000 * 60 * 60 * 24) % 365) > 0 ? time + "days " : "";
		String hr = (time = (diffInTime / (1000 * 60 * 60)) % 24) > 0 || !days.equals("") ? time + "hr " : "";
		String min = (time = (diffInTime / (1000 * 60)) % 60) > 0 || !hr.equals("") ? time + "min " : "";
		String sec = (time = (diffInTime / 1000) % 60) > 0 ? time + "sec" : "";

		return days + hr + min + sec;
	}
	
	public static Date findMinStartTimeAndMaxEndTime(List<Date> listOfDates, String maxOrMin) {
		List<Date> sortedDatesList = listOfDates.stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
		if("MAX".equalsIgnoreCase(maxOrMin)) {
			return sortedDatesList.stream().max(Date::compareTo).get();
		} else {
			return sortedDatesList.stream().min(Comparator.naturalOrder()).get();
		}
	}

	private DateUtils() {
		throw new IllegalStateException("Utility class");
	}
}
