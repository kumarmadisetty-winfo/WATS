package com.winfo.utils;

public class StringUtils {

	private static String projectPath = System.getProperty("user.dir");

	public static String getFilePath(String fileName) {
		return projectPath + fileName;
	}


	public static String convertToString(Object input) {
		String outputString = null;
		if (input != null) {
			outputString = String.valueOf(input).trim();
		}
		return outputString;
	}

	
	private StringUtils() {
		throw new IllegalStateException("Utility class");
	}

}
