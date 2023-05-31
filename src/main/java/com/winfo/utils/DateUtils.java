package com.winfo.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
		String days = (time = TimeUnit.MILLISECONDS.toDays(diffInTime)) > 0 ? time + "days " : "";
		String hr = (time = TimeUnit.MILLISECONDS.toHours(diffInTime) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(diffInTime))) > 0 || !days.equals("") ? time + "hr " : "";
		String min = (time = TimeUnit.MILLISECONDS.toMinutes(diffInTime) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(diffInTime))) > 0 || !hr.equals("") ? time + "min " : "";
		String sec = (time = TimeUnit.MILLISECONDS.toSeconds(diffInTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diffInTime))) > 0 ? time + "sec" : "";

		return days + hr + min + sec;
	}
	
	public static Date findMinStartTimeAndMaxEndTime(List<Date> listOfDates, String maxOrMin) {
		if("MAX".equalsIgnoreCase(maxOrMin)) {
			return listOfDates.stream().max(Date::compareTo).get();
		} else {
			return listOfDates.stream().min(Comparator.naturalOrder()).get();
		}
	}

	private DateUtils() {
		throw new IllegalStateException("Utility class");
	}
}
