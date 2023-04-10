package com.winfo.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.ConfigFileReader.ConfigFile;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.core.ComputeManagementClient;
import com.oracle.bmc.core.model.InstancePoolSummary;
import com.oracle.bmc.core.model.InstanceSummary;
import com.oracle.bmc.core.model.UpdateInstancePoolDetails;
import com.oracle.bmc.core.requests.ListInstancePoolInstancesRequest;
import com.oracle.bmc.core.requests.ListInstancePoolsRequest;
import com.oracle.bmc.core.requests.UpdateInstancePoolRequest;
import com.oracle.bmc.core.responses.ListInstancePoolInstancesResponse;
import com.oracle.bmc.core.responses.ListInstancePoolsResponse;
import com.oracle.bmc.core.responses.UpdateInstancePoolResponse;
import com.winfo.constants.BrowserConstants;
import com.winfo.constants.DriverConstants;
import com.winfo.dao.VmInstanceDAO;

@Service
@RefreshScope
public class VMDetailesService {
	Logger log = Logger.getLogger("Logger");

	@Autowired
	private VmInstanceDAO vmInstanceDao;

	@Value("${configvO.config_url}")
	private String config_url;

	@Autowired
	TestCaseDataService dataService;

	@Value("${instancePool.compementId}")
	private String compementId;

	@Value("${instancePool.configLocation}")
	private String configLocation;

	@Value("${instance.displayName}")
	private String displayName;

	@Value("${instance.maxNoOfBrowsersForVm}")
	private int maxNoOfBrowsersForVm;

	@Value("${instance.instanceConfigProfileName}")
	private String configProfileName;

	@Transactional
	public void startInstance(String testRunId) throws IOException {

		ListInstancePoolInstancesResponse listresponse = null;
		ConfigFileAuthenticationDetailsProvider provider = null;
		try {

			ConfigFile configFile = ConfigFileReader.parse(new ClassPathResource(configLocation).getInputStream(),
					configProfileName);

			provider = new ConfigFileAuthenticationDetailsProvider(configFile);
			ComputeManagementClient client = new ComputeManagementClient(provider);
			ListInstancePoolsRequest listInstancePoolsRequest = ListInstancePoolsRequest.builder()
					.compartmentId(compementId).displayName(displayName).limit(719)
//		    		.page("EXAMPLE-page-Value")
					.sortBy(ListInstancePoolsRequest.SortBy.Displayname)
					.sortOrder(ListInstancePoolsRequest.SortOrder.Desc)
					.lifecycleState(InstancePoolSummary.LifecycleState.Running).build();
			ListInstancePoolsResponse response = client.listInstancePools(listInstancePoolsRequest);

			listresponse = getListOfInstancesResponse(response, client, compementId);
			int numberOfVms = 0;

			for (InstanceSummary instanceSummary : listresponse.getItems()) {
				if ("Running".equalsIgnoreCase(instanceSummary.getState())) {
					numberOfVms++;
				}
			}
			int inprogressandInqueueCount = vmInstanceDao.getInprogressAndInqueueCount();
			int testrunScriptsCount = vmInstanceDao.getNumberOfTestscriptsforTestRunId(testRunId);
			int total = inprogressandInqueueCount + testrunScriptsCount - maxNoOfBrowsersForVm * numberOfVms;
			int maxNoOfBrowsersForConfig = vmInstanceDao.getMaxNoOfBrowsersForConfiguration(testRunId);
			int increageBrowserscount = 0 + maxNoOfBrowsersForVm * numberOfVms;
			while (total > 0 && maxNoOfBrowsersForConfig > increageBrowserscount) {
				numberOfVms++;
				total = total - maxNoOfBrowsersForVm;
				increageBrowserscount = increageBrowserscount + maxNoOfBrowsersForVm;
			}
			UpdateInstancePoolDetails updateInstancePoolDetails = UpdateInstancePoolDetails.builder().size(numberOfVms)
					.build();

			UpdateInstancePoolRequest updateRequest = UpdateInstancePoolRequest.builder()
					.instancePoolId(response.getItems().get(0).getId())
					.updateInstancePoolDetails(updateInstancePoolDetails).build();

			UpdateInstancePoolResponse updateResponse = client.updateInstancePool(updateRequest);
			System.out.println("no of vms lanched and wait 2mits" + updateResponse.getInstancePool().getSize());
			String fistVmStatus = null;

			while (!"Running".equalsIgnoreCase(fistVmStatus)) {
				Thread.sleep(5000);
				listresponse = getListOfInstancesResponse(response, client, compementId);
				for (InstanceSummary instanceSummary : listresponse.getItems()) {
					if ("Running".equalsIgnoreCase(instanceSummary.getState())) {
						fistVmStatus = "Running";
					}
				}
			}
			FetchConfigVO fetchConfigVO = dataService.getFetchConfigVO(testRunId);
			getWebDriver(fetchConfigVO);

			log.info("no of vms lanched" + updateResponse.getInstancePool().getSize());

		} catch (Exception e) {
			log.error("failed at sart the vms" + e);
			e.printStackTrace();
		}

	}

	private void getWebDriver(FetchConfigVO fetchConfigVO) throws MalformedURLException, InterruptedException {
		WebDriver driver = null;
		String os = System.getProperty("os.name").toLowerCase();
		if (BrowserConstants.CHROME.getValue().equalsIgnoreCase(fetchConfigVO.getBrowser())) {
			System.setProperty(DriverConstants.CHROME_DRIVER.getValue(), fetchConfigVO.getChrome_driver_path());
			System.setProperty("java.awt.headless", "false");
			// System.setProperty(DriverConstants.CHROME_DRIVER.value,
			// "C:\\Users\\watsadmin\\Documents\\Selenium Grid\\chromedriver.exe");
			Map<String, Object> prefs = new HashMap<String, Object>();
			prefs.put("profile.default_content_settings.popups", 0);
			prefs.put("download.default_directory", fetchConfigVO.getDownlod_file_path());
			ChromeOptions options = new ChromeOptions();
			
			if (os.contains("win")) {
				System.out.println("windows location");
				options.setBinary("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe");// cap.setCapability("chrome.binary",
																										// "C:\\Program
																										// Files
																										// (x86)\\Google\\Chrome\\Application\\chrome.exe");
			} else {
				System.out.println("linex location");
				options.setBinary("/usr/bin/google-chrome");
			}

//			options.setBinary("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");
//			options.setBinary("/usr/bin/google-chrome");
//			options.addArguments("headless");
			options.addArguments("start-maximized");
			options.addArguments("--enable-automation");
			options.addArguments("test-type=browser");
			options.addArguments("disable-infobars");
			options.setExperimentalOption("prefs", prefs);
			options.setAcceptInsecureCerts(true);
			MutableCapabilities cap = new MutableCapabilities().merge(options);
			

			try {
				driver = new RemoteWebDriver(new URL(config_url), cap);

				driver.quit();
			} catch (Exception e) {
				Thread.sleep(3 * 60 * 1000);
				getWebDriver(fetchConfigVO);

				log.error("failed at stop the vms" + e);
				e.printStackTrace();
			}
		}

	}

	@Transactional
	public void stopInstance() throws Exception {
		ConfigFileAuthenticationDetailsProvider provider = null;
		ListInstancePoolInstancesResponse listresponse = null;
		try {
			ConfigFile configFile = ConfigFileReader.parse(new ClassPathResource(configLocation).getInputStream(),
					configProfileName);
			provider = new ConfigFileAuthenticationDetailsProvider(configFile);
			ComputeManagementClient client = new ComputeManagementClient(provider);
			ListInstancePoolsRequest listInstancePoolsRequest = ListInstancePoolsRequest.builder()
					.compartmentId(compementId).displayName(displayName).limit(719)
					.sortBy(ListInstancePoolsRequest.SortBy.Displayname)
					.sortOrder(ListInstancePoolsRequest.SortOrder.Desc)
					.lifecycleState(InstancePoolSummary.LifecycleState.Running).build();
			ListInstancePoolsResponse response = client.listInstancePools(listInstancePoolsRequest);

			listresponse = getListOfInstancesResponse(response, client, compementId);

			for (InstanceSummary instanceSummary : listresponse.getItems()) {
				if ("Running".equalsIgnoreCase(instanceSummary.getState())) {
					UpdateInstancePoolDetails updateInstancePoolDetails = UpdateInstancePoolDetails.builder().size(0)
							.build();

					UpdateInstancePoolRequest updateRequest = UpdateInstancePoolRequest.builder()
							.instancePoolId(response.getItems().get(0).getId())
							.updateInstancePoolDetails(updateInstancePoolDetails).build();
					UpdateInstancePoolResponse updateResponse = client.updateInstancePool(updateRequest);

					listresponse = getListOfInstancesResponse(response, client, compementId);
					while (!"Terminated".equalsIgnoreCase(listresponse.getItems().get(0).getState())) {
						Thread.sleep(7000);
						listresponse = getListOfInstancesResponse(response, client, compementId);
					}

					System.out
							.println("all vms are stoped and wait 2mits" + updateResponse.getInstancePool().getSize());
					log.info("all vms are stoped" + updateResponse.getInstancePool().getSize());
				}
			}
		} catch (Exception e) {
			log.error("failed at stop the vms" + e);
			System.out.println("Exception" + e);
		}
	}

	public ListInstancePoolInstancesResponse getListOfInstancesResponse(ListInstancePoolsResponse response,
			ComputeManagementClient client, String instancePoolId) {
		try {

			System.out.println("instanceSummary");
			ListInstancePoolInstancesRequest listInstancePoolInstancesRequest = ListInstancePoolInstancesRequest
					.builder().compartmentId(response.getItems().get(0).getCompartmentId())
					.instancePoolId(response.getItems().get(0).getId())
//					.displayName(response.getItems().get(0).getDisplayName())
					.limit(122).sortBy(ListInstancePoolInstancesRequest.SortBy.Timecreated)
					.sortOrder(ListInstancePoolInstancesRequest.SortOrder.Desc).build();

//			client.setRegion(Region.AP_MUMBAI_1);
//			GetInstancePoolRequest getInstancePoolRequest = GetInstancePoolRequest.builder().instancePoolId(instancePoolId)
//					.build();
//			GetInstancePoolResponse response = client.getInstancePool(getInstancePoolRequest);
//			ListInstancePoolInstancesRequest listInstancePoolInstancesRequest = ListInstancePoolInstancesRequest
//					.builder().compartmentId(response.getInstancePool().getCompartmentId())
//					.instancePoolId(response.getInstancePool().getId())
//					.displayName(response.getInstancePool().getDisplayName()).limit(122)
//					.sortBy(ListInstancePoolInstancesRequest.SortBy.Timecreated)
//					.sortOrder(ListInstancePoolInstancesRequest.SortOrder.Desc).build();
			return client.listInstancePoolInstances(listInstancePoolInstancesRequest);
		} catch (Exception e) {
			System.out.println("Exception" + e);
			log.error("failed at configuration setup" + e);
		}
		return null;
	}

}
