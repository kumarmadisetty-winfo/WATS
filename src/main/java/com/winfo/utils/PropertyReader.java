package com.winfo.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.winfo.constants.PropertyConstants;
import com.winfo.vo.ConfigurationVO;

public class PropertyReader {

	private static String fileName = File.separator+"Property"+File.separator+"config.properties";

	public static ConfigurationVO getConfigurationData() {
		ConfigurationVO config = null;
		try (InputStream input = new FileInputStream(StringUtils.getFilePath(fileName))) {
			config = new ConfigurationVO();
			Properties prop = new Properties();
			prop.load(input);
			config.setBrowser(prop.getProperty(PropertyConstants.BROWSER.getValue()));
			config.setExecutionTime(prop.getProperty(PropertyConstants.EXECUTION_TIME.getValue()));
			config.setProjectName(prop.getProperty(PropertyConstants.PROJECT_NAME.getValue()));
			config.setUrl(prop.getProperty(PropertyConstants.URL.getValue()));
			config.setPassword(prop.getProperty(PropertyConstants.PASSWORD.getValue()));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return config;
	}

	public static String getPropertyValue(String key) {
		String propertyValue = null;
		try (InputStream input = new FileInputStream(StringUtils.getFilePath(fileName))) {
			Properties prop = new Properties();
			prop.load(input);
			propertyValue = prop.getProperty(key);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return propertyValue;
	}
	
	private PropertyReader() {
		throw new IllegalStateException("Utility class");
	}
	
}
