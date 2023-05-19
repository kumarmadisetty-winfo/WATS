package com.winfo.serviceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.simple.JSONArray;
//import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.ListObjectsRequest;
import com.oracle.bmc.objectstorage.responses.ListObjectsResponse;
import com.winfo.config.MessageUtil;
import com.winfo.dao.DataBaseEntryDao;
import com.winfo.exception.WatsEBSCustomException;
import com.winfo.utils.Constants;
import com.winfo.utils.StringUtils;
import com.winfo.vo.CustomerProjectDto;
import com.winfo.vo.FetchConfigVO;
import com.winfo.vo.HealthCheckVO;
import com.winfo.vo.ResponseDto;
import com.winfo.vo.SanityCheckVO;

@Service
public class HealthCheck {

	@Autowired
	DataBaseEntryDao dao;

	@Value("${hubUrl}")
	private String watsHubUrl;

	@Value("${oci.config.name}")
	private String ociConfigName;

	@Value("${oci.bucket.name}")
	private String ociBucketName;

	@Value("${oci.namespace}")
	private String ociNamespace;

	@Value("${oci.config.path}")
	private String ociConfigPath;

	@Autowired
	DataBaseEntry dataBaseEntry;

	@Autowired
	CentralRepoStatusCheckService centralRepoStatusCheckService;

	@Autowired
	TestScriptExecService testScriptExecService;
	
	@Autowired
	MessageUtil messageUtil;

	public static final String FORWARD_SLASH = "/";
	private static final String SCREENSHOT = "Screenshot";
	private static final String GREEN = "GREEN";
	private static final String RED = "RED";

	public ResponseDto sanityCheckMethod(String testSetId) throws Exception {
		try {
			dbAccessibilityCheck();
			String checkPackage = dataBaseEntry.getPackage(testSetId);
			if (checkPackage != null && !(checkPackage.toLowerCase().contains(Constants.EBS))) {
				seleniumGridCheck();
			}
			storageAccessChecks(testSetId);
		} catch (Exception e) {
			dataBaseEntry.updateTestSetLineStatusForSanity(testSetId);
			dataBaseEntry.updateEnableFlagForSanity(testSetId);
			return new ResponseDto(500, Constants.ERROR, e.getMessage());
		}
		return new ResponseDto(200, Constants.SUCCESS, messageUtil.getHealthCheck().getSuccess().getSanityCheckMessage());
	}

	public SanityCheckVO sanityCheckForAdminMethod() {
		HealthCheckVO healthCheckVO = new HealthCheckVO();
		int count = 0;
		String flag = GREEN;
		try {
			dbAccessibilityCheck();
			healthCheckVO.setDatabase(GREEN);
		} catch (Exception e) {
			healthCheckVO.setDatabase(RED);
			count++;
		}
		try {
			seleniumGridCheck();
			healthCheckVO.setSeleniumGrid(GREEN);
		} catch (Exception e) {
			healthCheckVO.setSeleniumGrid(RED);
			count++;
		}
		try {
			objectStoreAccessChecks(null);
			healthCheckVO.setObjectStoreAccess(GREEN);
		} catch (Exception e) {
			e.printStackTrace();
			healthCheckVO.setObjectStoreAccess(RED);
			count++;
		}
		try {
			centralRepoStatusCheckService.centralRepoStatus();
			healthCheckVO.setCentralRepo(GREEN);
		} catch (Exception e) {
			healthCheckVO.setCentralRepo(RED);
			count++;
		}
		if (count == 4) {
			flag = RED;
		} else if (count > 0 && count < 4) {
			flag = "AMBER";
		}
		return new SanityCheckVO(flag, healthCheckVO);
	}

	public ResponseDto dbAccessibilityCheck() {
		try {
			dao.dbAccessibilityCheck();
		} catch (Exception e) {
			throw new WatsEBSCustomException(500, messageUtil.getHealthCheck().getError().getDbAccessibilityMessage());
		}
		return new ResponseDto(200, Constants.SUCCESS, null);
	}

	public ResponseDto seleniumGridCheck() {
		try {
			RestTemplate restTemplate = new RestTemplate();
			String url = watsHubUrl.concat("status");
			String result = restTemplate.getForObject(url, String.class);
			JSONParser parser = new JSONParser();
			JSONObject obj = (JSONObject) parser.parse(result);
			JSONObject obj1 = (JSONObject) obj.get("value");
			JSONArray jsonarray = (JSONArray) obj1.get("nodes");
			long total = (long) jsonarray.size();
			if (total > 0) {
				return new ResponseDto(200, Constants.SUCCESS, null);
			} else {
				throw new WatsEBSCustomException(500, messageUtil.getHealthCheck().getError().getSeleniumGridMessage());
			}
		} catch (Exception e) {
			throw new WatsEBSCustomException(500, messageUtil.getHealthCheck().getError().getSeleniumGridMessage());
		}
	}

	public void storageAccessChecks(String testSetId) throws Exception {
		FetchConfigVO fetchConfigVO = testScriptExecService.fetchConfigVO(testSetId);
		objectStoreAccessChecks(Optional.of(testSetId));
		if ("SHAREPOINT".equalsIgnoreCase(fetchConfigVO.getPDF_LOCATION())) {
			getSharePointAccess(fetchConfigVO);
		}
	}

	public ResponseDto objectStoreAccessChecks(Optional<String> testSetId) throws Exception {
		ConfigFileReader.ConfigFile configFile = null;
		try {
			File file = new File(ociConfigPath);
			configFile = ConfigFileReader.parse(new FileInputStream(file), ociConfigName);
		} catch (IOException e) {
			throw new WatsEBSCustomException(500, messageUtil.getObjectStore().getConfigFileIOException());
		}
		final AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);
		try (ObjectStorage client = new ObjectStorageClient(provider);) {
			String objectStoreScreenShotPath = SCREENSHOT;

			String objectStorePdfPath = null;
			List<String> pdfResponseList = null;
			if (testSetId != null) {
				CustomerProjectDto customerDetails = dataBaseEntry.getCustomerDetails(testSetId.get());

				objectStorePdfPath = customerDetails.getCustomerName();

				ListObjectsRequest listPdfObjectsRequest = ListObjectsRequest.builder().namespaceName(ociNamespace)
						.bucketName(ociBucketName).prefix(objectStorePdfPath).delimiter("/").build();
				ListObjectsResponse responsePdf = client.listObjects(listPdfObjectsRequest);
				pdfResponseList = responsePdf.getListObjects().getPrefixes();
			}
			ListObjectsRequest listScreenShotObjectsRequest = ListObjectsRequest.builder().namespaceName(ociNamespace)
					.bucketName(ociBucketName).prefix(objectStoreScreenShotPath).delimiter("/").build();
			ListObjectsResponse responseScreenShot = client.listObjects(listScreenShotObjectsRequest);

			List<String> screenShotResponseList = responseScreenShot.getListObjects().getPrefixes();
			if (testSetId != null) {
				if (!(screenShotResponseList.contains(objectStoreScreenShotPath + FORWARD_SLASH)
						&& pdfResponseList.contains(objectStorePdfPath + FORWARD_SLASH))) {
					throw new WatsEBSCustomException(500, messageUtil.getHealthCheck().getError().getObjectStoreAccess());
				}
			} else {
				if (!(screenShotResponseList.contains(objectStoreScreenShotPath + FORWARD_SLASH))) {
					throw new WatsEBSCustomException(500, messageUtil.getHealthCheck().getError().getObjectStoreAccess());
				}
			}
		} catch (Exception e1) {
			if (e1 instanceof WatsEBSCustomException) {
				throw e1;
			} else {
				throw new WatsEBSCustomException(500, messageUtil.getObjectStore().getAccessDeniedException());
			}
		}
		return new ResponseDto(200, Constants.SUCCESS, null);
	}

	public String getSharePointAccess(FetchConfigVO fetchConfigVO) throws WatsEBSCustomException {
		String acessToken = null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
			map.add("grant_type", "client_credentials");
			map.add("client_id", fetchConfigVO.getClient_id());
			map.add("client_secret", fetchConfigVO.getClient_secret());
			map.add("scope", "https://graph.microsoft.com/.default");

			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
			ResponseEntity<Object> response = restTemplate.exchange(
					"https://login.microsoftonline.com/" + fetchConfigVO.getTenant_id() + "/oauth2/v2.0/token",
					HttpMethod.POST, entity, Object.class);
			@SuppressWarnings("unchecked")
			Map<String, Object> linkedMap = response.getBody() != null ? (Map<String, Object>) response.getBody()
					: null;
			acessToken = linkedMap != null ? StringUtils.convertToString(linkedMap.get("access_token")) : null;
		} catch (Exception e) {
			throw new WatsEBSCustomException(500, messageUtil.getHealthCheck().getError().getSharePointAccess());
		}
		return acessToken;
	}

}
