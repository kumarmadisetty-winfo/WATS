package com.winfo.utils;

import java.io.File;

import org.apache.log4j.Logger;

public class FileUtil {
	public static final Logger logger = Logger.getLogger(FileUtil.class);
	
	public static void createDir(String path) {
		File folder = new File(path);
		if (!folder.exists()) {
			logger.info("creating directory: " + folder.getName());
			try {
				folder.mkdirs();
			} catch (SecurityException se) {
				se.printStackTrace();
				logger.error("Failed to create directory " + se.getMessage());
			}
		} else {
			logger.info("Folder exist " + folder);
		}
	}

}
