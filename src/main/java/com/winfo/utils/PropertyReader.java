package com.winfo.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.winfo.constants.PropertyConstants;
import com.winfo.vo.ConfigurationVO;

public class PropertyReader {

	private static String fileName =  "/Property/config.properties";

	public static ConfigurationVO getConfigurationData() {
		ConfigurationVO config = null;
		try (InputStream input = new FileInputStream(StringUtils.getFilePath(fileName))) {
			config = new ConfigurationVO();
			Properties prop = new Properties();
			prop.load(input);
			config.setBrowser(prop.getProperty(PropertyConstants.BROWSER.value));
			config.setExecutionTime(prop.getProperty(PropertyConstants.EXECUTION_TIME.value));
			config.setProjectName(prop.getProperty(PropertyConstants.PROJECT_NAME.value));
			config.setUrl(prop.getProperty(PropertyConstants.URL.value));
			config.setPassword(prop.getProperty(PropertyConstants.PASSWORD.value));
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
}
