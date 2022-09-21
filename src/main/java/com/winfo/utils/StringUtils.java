package com.winfo.utils;

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

}
