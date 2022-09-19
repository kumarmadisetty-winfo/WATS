package com.winfo.utils;

public class StringUtils {

	private static String projectPath = System.getProperty("user.dir");

	public static String getFilePath(String fileName) {
		return projectPath + fileName;
	}

	public static boolean isNotNullorBlank(String input) {
		
		return input.isEmpty();
	}

	public static final boolean isNullOrBlank(String input) {
		return input == null || "".equals(input.trim()) || "null".equals(input.trim());
	}

	public static final boolean isNullOrBlank(Object o) {
		return !(o != null && !"".equals(o.toString()));
	}

	public static String convertToString(Object input) {
		String outputString = null;
		if (input != null) {
			outputString = String.valueOf(input).trim();
		}
		return outputString;
	}

	public static final int convertStringToInteger(final String input) {
		return convertStringToInteger(input, 0);
	}

	public static final int convertStringToInteger(final String input, int defaultValue) {
		return StringUtils.isNullOrBlank(input) ? defaultValue : Integer.parseInt(input);
	}

	public static String replaceString(String input, String oldChar, String newChar) {
		if (isNotNullorBlank(input)) {
			input = input.replace(oldChar, newChar);
		}
		return convertToString(input);
	}
	
	private StringUtils() {
		throw new IllegalStateException("Utility class");
	}

}
