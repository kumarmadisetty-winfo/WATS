package com.winfo.config;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.winfo.constants.BrowserConstants;
import com.winfo.constants.DriverConstants;
import com.winfo.services.FetchConfigVO;

public class DriverConfiguration {

	public static WebDriver getWebDriver(FetchConfigVO fetchConfigVO) throws MalformedURLException {
		WebDriver driver = null;
		if (BrowserConstants.CHROME.value.equalsIgnoreCase(fetchConfigVO.getBrowser())) {
			//System.setProperty(DriverConstants.CHROME_DRIVER.value, fetchConfigVO.getChrome_driver_path());
			System.setProperty(DriverConstants.CHROME_DRIVER.value, "C:\\Users\\watsadmin\\Documents\\Selenium Grid\\chromedriver.exe");
			Map<String, Object> prefs = new HashMap<String, Object>();
			prefs.put("profile.default_content_settings.popups", 0);
			prefs.put("download.default_directory", fetchConfigVO.getDownlod_file_path());
			ChromeOptions options = new ChromeOptions();
			options.setBinary("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");
			options.addArguments("start-maximized");
			options.addArguments("--enable-automation");
			options.addArguments("test-type=browser");
			options.addArguments("disable-infobars");
			options.setExperimentalOption("prefs", prefs);
			DesiredCapabilities cap = DesiredCapabilities.chrome();
			cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			cap.setCapability(ChromeOptions.CAPABILITY, options);
			//driver = new ChromeDriver(cap);
			driver=new RemoteWebDriver(new URL("http://watsudgs01.winfosolutions.com:4444/wd/hub"), cap); 

		} else if (BrowserConstants.FIREFOX.value.equalsIgnoreCase(fetchConfigVO.getBrowser())) {
			System.setProperty(DriverConstants.FIREFOX_DRIVER.value, fetchConfigVO.getFirefox_driver_path());
			DesiredCapabilities capabilities = DesiredCapabilities.firefox();
			capabilities.setCapability("marionette", true);
			driver = new FirefoxDriver(capabilities);
		} else if (BrowserConstants.IE.value.equalsIgnoreCase(fetchConfigVO.getBrowser())) {
			System.setProperty(DriverConstants.IE_DRIVER.value, fetchConfigVO.getIe_driver_path());
			driver = new InternetExplorerDriver();
		}
		if (driver != null) {
			System.out.println("Browser launched...");
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
		return driver;
	}
}
