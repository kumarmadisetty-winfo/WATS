package com.winfo.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class StringUtils {

	private static String projectPath = System.getProperty("user.dir");

	public static String getFilePath(String fileName) {
		return projectPath + fileName;
	}

	public static final boolean isNullOrBlank(String input) {
		return input == null || "".equals(input.trim()) || "null".equals(input.trim());
	}

	public static String convertToString(Object input) {
		String outputString = null;
		if (input != null) {
			outputString = String.valueOf(input).trim();
		}
		return outputString;
	}


	public static final int convertStringToInteger(final String input, int defaultValue) {
		return StringUtils.isNullOrBlank(input) ? defaultValue : Integer.parseInt(input);
	}
	
	private StringUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static String basicAuthHeader(String username, String password) {
		String credentials = username + ":" + password;
		byte[] credentialsBytes = credentials.getBytes();
		String base64Credentials = java.util.Base64.getEncoder().encodeToString(credentialsBytes);
		return "Basic " + base64Credentials;
	}
	
	public static boolean isValidDate(String date, String dateFormat) {
		DateFormat dateFormatChecker = new SimpleDateFormat(dateFormat);
		dateFormatChecker.setLenient(false);
		try {
			dateFormatChecker.parse(date);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}
}
