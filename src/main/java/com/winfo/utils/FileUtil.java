package com.winfo.utils;

import java.io.File;

import org.apache.log4j.Logger;

public class FileUtil {
	public static final Logger logger = Logger.getLogger(FileUtil.class);
	
	public static void createDir(String path) {
		File folder1 = new File(path);
		if (!folder1.exists()) {
			logger.info("creating directory: " + folder1.getName());
			try {
				folder1.mkdirs();
			} catch (SecurityException se) {
				se.printStackTrace();
				logger.error("Failed to create directory " + se.getMessage());
			}
		} else {
			logger.info("Folder exist " + folder1);
		}
	}

}
