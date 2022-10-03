package com.winfo.config;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import com.winfo.constants.BrowserConstants;
import com.winfo.constants.DriverConstants;
import com.winfo.services.FetchConfigVO;

@Component
@RefreshScope
public class DriverConfiguration {

	public final Logger logger = LogManager.getLogger(DriverConfiguration.class);

	@Value("${configvO.config_url}")
	private String configUrl;
	
	
	/*
	 * Edge Browser is not working due to Incompatilility issue Providing related
	 * Jira Ticket Number : https://winfosolutions.atlassian.net/browse/WATS-1566
	 */

	public WebDriver getWebDriver(FetchConfigVO fetchConfigVO, String operatingSystem) throws MalformedURLException {
		logger.info("Start of get web driver method");
		WebDriver driver = null;
		String os = System.getProperty("os.name").toLowerCase();
		os = operatingSystem == null ? os : operatingSystem;
		if (BrowserConstants.CHROME.getValue().equalsIgnoreCase(fetchConfigVO.getBrowser())) {
			System.setProperty(DriverConstants.CHROME_DRIVER.getValue(), fetchConfigVO.getChrome_driver_path());
			System.setProperty(BrowserConstants.AWT_HEADLESS.getValue(), "false");
			Map<String, Object> prefs = new HashMap<>();
			prefs.put(BrowserConstants.PROFILE_DEFAULT_CONTENT_SETTING.getValue(), 0);
			prefs.put(BrowserConstants.DOWNLOAD_DEFAULT_DIRECTORY.getValue(), fetchConfigVO.getDownlod_file_path());
			ChromeOptions options = new ChromeOptions();
			DesiredCapabilities cap = DesiredCapabilities.chrome();
			if (os.contains("win")) {
				logger.info("windows location");
				options.setBinary("/Program Files/Google/Chrome/Application/chrome.exe");
				cap.setPlatform(Platform.WINDOWS);
			} else {
				logger.info("linux location");
				options.setBinary("/usr/bin/google-chrome");
				cap.setPlatform(Platform.LINUX);
			}
			options.addArguments(BrowserConstants.START_MAXIMIZED.getValue());
//			options.addArguments("--headless");
			options.addArguments(BrowserConstants.NO_SENDBOX.getValue());
			options.addArguments(BrowserConstants.ENABLE_AUTOMATION.getValue());
			options.addArguments(BrowserConstants.TEST_TYPE.getValue());
			options.addArguments(BrowserConstants.DISABLE_INFOBARS.getValue());
			options.setExperimentalOption("prefs", prefs);
			cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			cap.setCapability(ChromeOptions.CAPABILITY, options);
			

//			 driver = new ChromeDriver(cap);
			driver = new RemoteWebDriver(new URL(configUrl), cap);
		} else if (BrowserConstants.FIREFOX.getValue().equalsIgnoreCase(fetchConfigVO.getBrowser())) {
			System.setProperty(DriverConstants.FIREFOX_DRIVER.getValue(),
					"/Github/EBS-Automation-POC/Driver/geckodriver.exe");
//			System.setProperty(DriverConstants.FIREFOX_DRIVER.value, fetchConfigVO.getFirefox_driver_path());

			System.setProperty(BrowserConstants.AWT_HEADLESS.getValue(), "false");
			FirefoxOptions options = new FirefoxOptions();
			if (os.contains("win")) {
				options.setBinary("/Program Files/Mozilla Firefox/firefox.exe");
			} else {
				logger.info("linux location");
				options.setBinary("/usr/bin/firefox");
			}
//			options.addArguments("--headless");
			options.addArguments(BrowserConstants.NO_SENDBOX.getValue());
			options.addArguments(BrowserConstants.ENABLE_AUTOMATION.getValue());
			options.addArguments(BrowserConstants.DISABLE_INFOBARS.getValue());
			options.setCapability(BrowserConstants.MARIONETTE.getValue(), true);
			// driver = new FirefoxDriver(options);
			driver = new RemoteWebDriver(new URL(configUrl), options);
		}
		if (driver != null) {
			logger.info("Browser launched...");
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
		return driver;
	}
}