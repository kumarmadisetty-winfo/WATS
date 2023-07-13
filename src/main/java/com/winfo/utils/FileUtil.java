package com.winfo.utils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.winfo.vo.CustomerProjectDto;
import com.winfo.vo.FetchConfigVO;

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
			File file = new File(folderPath);
			FileUtils.deleteDirectory(file);
			logger.info("Successfully deleted the directory : "+file.getName());
		} catch (IOException e) {
			logger.error("Not able to delete the directory");
		}
	}

	public static void deleteScreenshotsForSpecificSeqFromDir(String folderPath, Integer seqNum) {
		try {
			File folder = new File(folderPath);
			File[] files = folder.listFiles();
			if (files != null) {
				Arrays.stream(files).parallel().filter(file -> file.isFile() && file.getName().startsWith(seqNum + "_")
						&& file.getName().toLowerCase().endsWith(".png")).forEach(file -> {
							if (file.delete()) {
								logger.info("Deleted file: " + file.getName());
							} else {
								logger.warn("Failed to delete file: " + file.getName());
							}
						});
			}
			logger.info("Successfully deleted the screenshots");
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
				Arrays.stream(files).parallel().filter(file -> file.isFile() && file.getName().startsWith(seqNum + "_")
						&& file.getName().toLowerCase().endsWith(".pdf")).forEach(file -> {
							if (file.delete()) {
								logger.info("Deleted file: " + file.getName());
							} else {
								logger.warn("Failed to delete file: " + file.getName());
							}
						});
			}
			logger.info("Successfully deleted the pdfs");
		} catch (NullPointerException e) {
			logger.error("Path is not correct");
		} catch (SecurityException e) {
			logger.error("Denies read access to the directory");
		} catch (Exception e) {
			logger.error("Not able to delete the pdfs");
		}
	}
	
	public static void deleteScreenshotAndPdfDirectoryFromTemp(FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) {
		deleteDir(
				fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + customerDetails.getCustomerName()
						+ File.separator + customerDetails.getTestSetName() + File.separator);
		deleteDir(
				fetchConfigVO.getWINDOWS_PDF_LOCATION() + customerDetails.getCustomerName()
						+ File.separator + customerDetails.getTestSetName() + File.separator);
	}

}
