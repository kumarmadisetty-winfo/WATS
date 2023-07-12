package com.winfo.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
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
	
	public static void deleteDir(String folderPath) {
		try {
			FileUtils.deleteDirectory(new File(folderPath));
			logger.info("Successfully deleted the directory");
		} catch (IOException e) {
			logger.error("Not able to delete the directory");
		}
	}

	public static void deleteScreenshotsForSpecificSeqFromDir(String folderPath, Integer seqNum) {
		try {
			File folder = new File(folderPath);
			File[] files = folder.listFiles();

			if (files != null) {
				for (File file : files) {
					if (file.isFile() && file.getName().startsWith(seqNum + "_")
							&& file.getName().toLowerCase().endsWith(".png")) {
						if (file.delete()) {
							logger.info("Deleted file: " + file.getName());
						} else {
							logger.warn("Failed to delete file: " + file.getName());
						}
					}
				}
			}
			logger.info("Successfully deleted the directory");
		} catch (NullPointerException e) {
			logger.error("Path is not correct");
		} catch (SecurityException e) {
			logger.error("Denies read access to the directory");
		} catch (Exception e) {
			logger.error("Not able to delete the screeshots");
		}

	}

	public static void deletePdfsForSpecificSeqFromDir(String folderPath, Integer seqNum) {
		try {
			File folder = new File(folderPath);
			File[] files = folder.listFiles();

			if (files != null) {
				for (File file : files) {
					if (file.isFile() && file.getName().startsWith(seqNum + "_")
							&& file.getName().toLowerCase().endsWith(".pdf")) {
						if (file.delete()) {
							logger.info("Deleted file: " + file.getName());
						} else {
							logger.warn("Failed to delete file: " + file.getName());
						}
					}
				}
			}
			logger.info("Successfully deleted the directory");
		} catch (NullPointerException e) {
			logger.error("Path is not correct");
		} catch (SecurityException e) {
			logger.error("Denies read access to the directory");
		} catch (Exception e) {
			logger.error("Not able to delete the pdfs");
		}

	}

}
