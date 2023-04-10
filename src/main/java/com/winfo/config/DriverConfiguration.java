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
import com.google.common.collect.ImmutableMap;

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
//			System.setProperty("headless", "false");
			Map<String, Object> prefs = new HashMap<>();
//			prefs.put(BrowserConstants.PROFILE_DEFAULT_CONTENT_SETTING.getValue(), 0);
			
			ChromeOptions options = new ChromeOptions();
			MutableCapabilities cap = new MutableCapabilities();
//			cap.setCapability(CapabilityType.UNHANDLED_PROMPT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
			if (!os.contains("win")) {
				prefs.put(BrowserConstants.DOWNLOAD_DEFAULT_DIRECTORY.getValue(), fetchConfigVO.getDownlod_file_path());
				options.setBinary("/Program Files/Google/Chrome/Application/chrome.exe");
				cap.setCapability(CapabilityType.PLATFORM_NAME, Platform.WINDOWS);
				cap.merge(options);
			} else  {
				prefs.put(BrowserConstants.DOWNLOAD_DEFAULT_DIRECTORY.getValue(), fetchConfigVO.getDownlod_file_path());
				logger.info("linux location");
				options.setBinary("/usr/bin/google-chrome");
				cap.setCapability(CapabilityType.PLATFORM_NAME, Platform.LINUX);
				cap.merge(options);
			}
			options.addArguments(BrowserConstants.START_MAXIMIZED.getValue());
//			options.addArguments("--headless");
			options.addArguments(BrowserConstants.NO_SENDBOX.getValue());
			options.addArguments(BrowserConstants.ENABLE_AUTOMATION.getValue());
			options.addArguments(BrowserConstants.TEST_TYPE.getValue());
			options.addArguments(BrowserConstants.DISABLE_INFOBARS.getValue());
			options.addArguments("--disable-popup-blocking");
			options.addArguments("chrome.switches","--disable-extensions");
//			options.addArguments("--acceptInsecureCerts");
			options.setExperimentalOption("prefs", prefs);
			options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);
			options.setAcceptInsecureCerts(true);
//			options.setHeadless(false);
			options.setExperimentalOption("prefs", 
			         ImmutableMap.of("profile.default_content_setting_values.notifications", 0));
			cap.setCapability(ChromeOptions.CAPABILITY, options);
			try {
//				String url = "http://watsdev01.winfosolutions.com:4444";
				String url = "http://localhost:4444";
				driver = new RemoteWebDriver(new URL(url), cap);
				logger.info("driver init success");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e);
			}
		}else if (BrowserConstants.FIREFOX.getValue().equalsIgnoreCase(fetchConfigVO.getBrowser())) {
//			System.setProperty(DriverConstants.FIREFOX_DRIVER.getValue(),
//			"/Github/EBS-Automation-POC/Driver/geckodriver.exe");
			System.setProperty(DriverConstants.FIREFOX_DRIVER.getValue(), fetchConfigVO.getFirefox_driver_path());
			FirefoxProfile profile = new FirefoxProfile();
			
		//	profile.setPreference("browser.download.folderList", 2);
		//	profile.setPreference("browser.download.dir", fetchConfigVO.getDownlod_file_path());
		//	profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/pdf;text/plain;application/text;text/xml;application/xml");
		//	profile.setPreference("pdfjs.disabled", true);
		
			System.setProperty(BrowserConstants.AWT_HEADLESS.getValue(), "false");
			FirefoxOptions options = new FirefoxOptions();
			if (os.contains("win")) {
				options.setBinary("/Program Files/Mozilla Firefox/firefox.exe");
			} else {
				logger.info("linux location");
				options.setBinary("/usr/bin/firefox");
			}
		//	options.addArguments("--headless");
			options.addArguments(BrowserConstants.NO_SENDBOX.getValue());
			options.addArguments(BrowserConstants.ENABLE_AUTOMATION.getValue());
			options.addArguments(BrowserConstants.DISABLE_INFOBARS.getValue());
			options.setCapability(BrowserConstants.MARIONETTE.getValue(), true);
			options.setProfile(profile);
			// driver = new FirefoxDriver(options);
			driver = new RemoteWebDriver(new URL(configUrl), options);
		}
		if (driver != null) {
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		}
		return driver;
	}
}