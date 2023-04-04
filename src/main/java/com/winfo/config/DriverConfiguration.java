package com.winfo.config;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
//import org.openqa.selenium.remote.DesiredCapabilities;
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
//		logger.info("Start of get web driver method");
		RemoteWebDriver driver = null;
//		String os = System.getProperty("os.name").toLowerCase();
//		os = operatingSystem == null ? os : operatingSystem;
//			System.setProperty(DriverConstants.CHROME_DRIVER.getValue(), fetchConfigVO.getChrome_driver_path());
//			System.setProperty("headless", "false");
			Map<String, Object> prefs = new HashMap<>();
//			prefs.put(BrowserConstants.PROFILE_DEFAULT_CONTENT_SETTING.getValue(), 0);
			ChromeOptions options = new ChromeOptions();
//			DesiredCapabilities cap = new DesiredCapabilities();
//			cap.setCapability("browserName", "chrome");
			MutableCapabilities cap = new MutableCapabilities();
//			MutableCapabilities cap = new MutableCapabilities().merge(options);
//			MutableCapabilities cap = new MutableCapabilities();
//			cap.setCapability(os, null);
//			cap.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
			cap.setCapability(CapabilityType.UNHANDLED_PROMPT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
			
//				prefs.put(BrowserConstants.DOWNLOAD_DEFAULT_DIRECTORY.getValue(), fetchConfigVO.getDownlod_file_path());
//				options.setBinary("/usr/bin/google-chrome");
//				cap.setPlatform(Platform.LINUX);
				cap.setCapability(CapabilityType.PLATFORM_NAME, Platform.WINDOWS);
				cap.merge(options);
			
//			options.addArguments(BrowserConstants.START_MAXIMIZED.getValue());
//			options.addArguments("--headless");
//			options.addArguments(BrowserConstants.NO_SENDBOX.getValue());
//			options.addArguments(BrowserConstants.ENABLE_AUTOMATION.getValue());
//			options.addArguments(BrowserConstants.TEST_TYPE.getValue());
//			options.addArguments(BrowserConstants.DISABLE_INFOBARS.getValue());
//			options.addArguments("--disable-popup-blocking");
//			options.addArguments("chrome.switches","--disable-extensions");
			options.setExperimentalOption("prefs", prefs);
//			cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			cap.setCapability(ChromeOptions.CAPABILITY, options);
			options.setAcceptInsecureCerts(true);
//			cap.setCapability(ChromeOptions.CAPABILITY, options);
			
			cap.merge(options);
//			 driver = new ChromeDriver(cap);
			try {
				String url = "http://watsdev01.winfosolutions.com:7777";
				driver = new RemoteWebDriver(new URL(url), cap);
				driver.get("https://www.google.com");
			} catch (Exception e) {
				System.out.println(e);
			}
		if (driver != null) {
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		}
		return driver;
	}
}