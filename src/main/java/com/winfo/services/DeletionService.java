package com.winfo.services;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.DeleteObjectRequest;
import com.oracle.bmc.objectstorage.requests.ListObjectsRequest;
import com.oracle.bmc.objectstorage.responses.DeleteObjectResponse;
import com.oracle.bmc.objectstorage.responses.ListObjectsResponse;
import com.winfo.exception.WatsEBSCustomException;
import com.winfo.model.TestSetLine;
import com.winfo.utils.Constants;
import com.winfo.utils.StringUtils;
import com.winfo.vo.CustomerProjectDto;
import com.winfo.vo.DeleteEvidenceReportDto;
import com.winfo.vo.ResponseDto;

@Service
public class DeletionService {

	public final Logger logger = LogManager.getLogger(DeletionService.class);

	@Value("${oci.config.name}")
	private String ociConfigName;

	@Value("${oci.bucket.name}")
	private String ociBucketName;

	@Value("${oci.namespace}")
	private String ociNamespace;

	@Autowired
	DataBaseEntry dataBaseEntry;

	@Autowired
	HealthCheck healthCheck;

	@Autowired
	TestScriptExecService testScriptExecService;

	private static final String SCREENSHOT = "Screenshot";
	public static final String FORWARD_SLASH = "/";

	public ResponseDto deleteAllScriptFromTestRun(DeleteEvidenceReportDto testScriptDto) throws Exception {
		String testSetId = testScriptDto.getTestSetId();
		CustomerProjectDto customerDetails = dataBaseEntry.getCustomerDetails(testSetId);
		ConfigFileReader.ConfigFile configFile = null;
		try {
			configFile = ConfigFileReader.parse(new ClassPathResource("oci/config").getInputStream(), ociConfigName);
		} catch (IOException e) {
			throw new WatsEBSCustomException(500, "Not able to connect with object store");
		}
		try {
			final AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);
			FetchConfigVO fetchConfigVO = testScriptExecService.fetchConfigVO(testSetId);
			List<TestSetLine> listOfTestSetLine = dataBaseEntry.getAllTestSetLineRecord(testSetId);
			TestSetLine testSetLine = listOfTestSetLine.get(0);
			deleteScreenShot(testSetLine, customerDetails, provider, testScriptDto.isFlag());
			deletePdf(testSetLine, customerDetails, provider, testScriptDto.isFlag());
			if ("SHAREPOINT".equalsIgnoreCase(fetchConfigVO.getPDF_LOCATION())) {
				String access = healthCheck.getSharePointAccess(fetchConfigVO);
				deletePdfFromSharePoint(fetchConfigVO, access, customerDetails, testSetLine, testScriptDto.isFlag());
			}
		} catch (Exception e) {
			if (e instanceof WatsEBSCustomException) {
				throw e;
			} else {
				return new ResponseDto(500, Constants.ERROR, "Not able to delete screenshot & pdf");
			}
		}
		return new ResponseDto(200, Constants.SUCCESS, "Screenshot & Pdf deleted successfully");
	}

	public ResponseDto deleteScriptFromTestRun(DeleteEvidenceReportDto testScriptDto) throws Exception {
		String testSetId = testScriptDto.getTestSetId();
		CustomerProjectDto customerDetails = dataBaseEntry.getCustomerDetails(testSetId);
		ConfigFileReader.ConfigFile configFile = null;

		try {
			configFile = ConfigFileReader.parse(new ClassPathResource("oci/config").getInputStream(), ociConfigName);
		} catch (IOException e) {
			throw new WatsEBSCustomException(500, "Not able to connect with object store");
		}
		try {
			final AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);
			FetchConfigVO fetchConfigVO = testScriptExecService.fetchConfigVO(testSetId);
			for (String testSetLineId : testScriptDto.getTestSetLineId()) {
				TestSetLine testSetLine = dataBaseEntry.getTestSetLinesRecord(testSetId, testSetLineId);
				deleteScreenShot(testSetLine, customerDetails, provider, testScriptDto.isFlag());
				deletePdf(testSetLine, customerDetails, provider, testScriptDto.isFlag());
				if ("SHAREPOINT".equalsIgnoreCase(fetchConfigVO.getPDF_LOCATION())) {
					String access = healthCheck.getSharePointAccess(fetchConfigVO);
					deletePdfFromSharePoint(fetchConfigVO, access, customerDetails, testSetLine,
							testScriptDto.isFlag());
				}
			}
		} catch (Exception e) {
			if (e instanceof WatsEBSCustomException) {
				throw e;
			} else {
				return new ResponseDto(500, Constants.ERROR, "Not able to delete screenshot & pdf");
			}
		}
		return new ResponseDto(200, Constants.SUCCESS, "Screenshot & Pdf deleted successfully");
	}

	private ResponseDto deleteScreenShot(TestSetLine testSetLine, CustomerProjectDto customerDetails,
			AuthenticationDetailsProvider provider, boolean flag) throws Exception {
		List<String> objNames = null;
		try (ObjectStorage client = new ObjectStorageClient(provider);) {

			String objectStoreScreenShotPath;

			if (flag) {
				objectStoreScreenShotPath = SCREENSHOT + FORWARD_SLASH + customerDetails.getCustomerName()
						+ FORWARD_SLASH + testSetLine.getTestRun().getTestRunName() + FORWARD_SLASH;
			} else {
				objectStoreScreenShotPath = SCREENSHOT + FORWARD_SLASH + customerDetails.getCustomerName()
						+ FORWARD_SLASH + testSetLine.getTestRun().getTestRunName() + FORWARD_SLASH
						+ testSetLine.getSeqNum();
			}

			ListObjectsRequest listScreenShotObjectsRequest = ListObjectsRequest.builder().namespaceName(ociNamespace)
					.bucketName(ociBucketName).prefix(objectStoreScreenShotPath).delimiter("/").build();

			ListObjectsResponse responseScreenShot = client.listObjects(listScreenShotObjectsRequest);

			objNames = responseScreenShot.getListObjects().getObjects().stream()
					.map((objSummary) -> objSummary.getName()).collect(Collectors.toList());

			logger.info(objNames.size());
			if (objNames.size() > 0) {
				ListIterator<String> listIt = objNames.listIterator();

				while (listIt.hasNext()) {
					String objectName = listIt.next();
					if (objectStoreScreenShotPath.equalsIgnoreCase(objectName) && flag) {
						DeleteObjectResponse getResponse = client.deleteObject(DeleteObjectRequest.builder()
								.namespaceName(ociNamespace).bucketName(ociBucketName).objectName(objectName).build());
						break;
					} else if(!flag){
						DeleteObjectResponse getResponse = client.deleteObject(DeleteObjectRequest.builder()
								.namespaceName(ociNamespace).bucketName(ociBucketName).objectName(objectName).build());
					}
				}
			} else {
				logger.info("Screenshot is not present");
				return new ResponseDto(200, Constants.SUCCESS, "Screenshot is not present");
			}

		} catch (Exception e1) {
			throw new WatsEBSCustomException(500, "Not able to connect with object store");
		}
		return new ResponseDto(200, Constants.SUCCESS, "Screenshot deleted successfully");
	}

	private ResponseDto deletePdf(TestSetLine testSetLine, CustomerProjectDto customerDetails,
			AuthenticationDetailsProvider provider, boolean flag) throws Exception {
		List<String> objNames = null;
		try (ObjectStorage client = new ObjectStorageClient(provider);) {

			String objectStorePdfPath;

			if (flag) {
				objectStorePdfPath = customerDetails.getCustomerName() + FORWARD_SLASH
						+ testSetLine.getTestRun().getTestRunName() + FORWARD_SLASH;
			} else {
				objectStorePdfPath = customerDetails.getCustomerName() + FORWARD_SLASH
						+ testSetLine.getTestRun().getTestRunName() + FORWARD_SLASH + testSetLine.getSeqNum();
			}

			ListObjectsRequest listPdfObjectsRequest = ListObjectsRequest.builder().namespaceName(ociNamespace)
					.bucketName(ociBucketName).prefix(objectStorePdfPath).delimiter("/").build();

			ListObjectsResponse responsePdf = client.listObjects(listPdfObjectsRequest);

			objNames = responsePdf.getListObjects().getObjects().stream().map((objSummary) -> objSummary.getName())
					.collect(Collectors.toList());

			logger.info(objNames.size());

			if (objNames.size() > 0) {
				ListIterator<String> listIt = objNames.listIterator();
				while (listIt.hasNext()) {
					String objectName = listIt.next();
					if (objectStorePdfPath.equalsIgnoreCase(objectName) && flag) {
						DeleteObjectResponse getResponse = client.deleteObject(DeleteObjectRequest.builder()
								.namespaceName(ociNamespace).bucketName(ociBucketName).objectName(objectName).build());
						break;
					} else if(!flag){
						DeleteObjectResponse getResponse = client.deleteObject(DeleteObjectRequest.builder()
								.namespaceName(ociNamespace).bucketName(ociBucketName).objectName(objectName).build());
					}
				}
			} else {
				logger.info("Pdf is not present");
				return new ResponseDto(200, Constants.SUCCESS, "Pdf is not present");
			}
		} catch (Exception e1) {

			throw new WatsEBSCustomException(500, "Not able to connect with object store");
		}
		return new ResponseDto(200, Constants.SUCCESS, "Pdf deleted successfully");
	}

	public void deletePdfFromSharePoint(FetchConfigVO fetchConfigVO, String accessToken,
			CustomerProjectDto customerDetails, TestSetLine testSetLine, boolean flag) {
		try {
			RestTemplate restTemplate = new RestTemplate();

			// Outer header
			HttpHeaders deleteSessionHeader = new HttpHeaders();
			deleteSessionHeader.add("Authorization", "Bearer " + accessToken);
			HttpEntity<byte[]> deleteSessionRequest = new HttpEntity<>(null, deleteSessionHeader);

			// SITE-ID
			ResponseEntity<Object> siteDetailsResponse = restTemplate.exchange("https://graph.microsoft.com/v1.0/sites/"
					+ fetchConfigVO.getSharePoint_URL() + ":/sites/" + fetchConfigVO.getSite_Name(), HttpMethod.GET,
					deleteSessionRequest, Object.class);

			Map<String, Object> siteDetailsMap = siteDetailsResponse.getBody() != null
					? (LinkedHashMap<String, Object>) siteDetailsResponse.getBody()
					: null;
			String siteId = siteDetailsMap != null
					? StringUtils.convertToString(siteDetailsMap.get("id").toString().split(",")[1])
					: null;

			// DRIVE-ID
			ResponseEntity<Object> driveDetailsResponse = restTemplate.exchange(
					"https://graph.microsoft.com/v1.0/sites/" + siteId + "/drives", HttpMethod.GET,
					deleteSessionRequest, Object.class);

			Map<String, Object> driveDetailsMap = driveDetailsResponse.getBody() != null
					? (LinkedHashMap<String, Object>) driveDetailsResponse.getBody()
					: null;

			List<Map<String, String>> list = (List<Map<String, String>>) driveDetailsMap.get("value");

			String driveId = null;
			for (Map<String, String> map : list) {
				if (fetchConfigVO.getSharePoint_Library_Name() != null) {
					if (fetchConfigVO.getSharePoint_Library_Name().equalsIgnoreCase(map.get("name"))) {
						driveId = map.get("id");
						break;
					}
				} else {
					if ("Documents".equalsIgnoreCase(map.get("name"))) {
						driveId = map.get("id");
						break;
					}
				}
			}

			// SITE-ID
			ResponseEntity<Object> itemDetailsResponse = restTemplate.exchange(
					"https://graph.microsoft.com/v1.0/drives/" + driveId + "/root:/" + fetchConfigVO.getDirectory_Name()
							+ "/" + customerDetails.getCustomerName() + "/" + customerDetails.getProjectName() + "/"
							+ testSetLine.getTestRun().getTestRunName(),
					HttpMethod.GET, deleteSessionRequest, Object.class);

			Map<String, Object> itemDetailsMap = itemDetailsResponse.getBody() != null
					? (LinkedHashMap<String, Object>) itemDetailsResponse.getBody()
					: null;

			String itemId = itemDetailsMap != null ? StringUtils.convertToString(itemDetailsMap.get("id")) : null;

			// Child-Name
			ResponseEntity<Object> listOfItemDetailsResponse = restTemplate.exchange(
					"https://graph.microsoft.com/v1.0/drives/" + driveId + "/items/" + itemId + "/children",
					HttpMethod.GET, deleteSessionRequest, Object.class);

			Map<String, Object> listOfItemDetailsMap = listOfItemDetailsResponse.getBody() != null
					? (LinkedHashMap<String, Object>) listOfItemDetailsResponse.getBody()
					: null;

			List<Map<String, Object>> listOfMaps = (List<Map<String, Object>>) listOfItemDetailsMap.get("value");
			if (flag) {
				ResponseEntity<Object> deletionOfPdf = restTemplate.exchange(
						"https://graph.microsoft.com/v1.0/drives/" + driveId + "/root:/"
								+ fetchConfigVO.getDirectory_Name() + "/" + customerDetails.getCustomerName() + "/"
								+ customerDetails.getProjectName() + "/" + testSetLine.getTestRun().getTestRunName(),
						HttpMethod.DELETE, deleteSessionRequest, Object.class);
			} else if(!flag){
				for (Map<String, Object> listOfName : listOfMaps) {
					String pdfName = listOfName.get("name").toString();
					String pdfNameToFind = testSetLine.getSeqNum() + "_" + testSetLine.getScriptNumber();
					if (pdfName.contains(pdfNameToFind)) {
						ResponseEntity<Object> deletionOfPdf = restTemplate.exchange(
								"https://graph.microsoft.com/v1.0/drives/" + driveId + "/root:/"
										+ fetchConfigVO.getDirectory_Name() + "/" + customerDetails.getCustomerName()
										+ "/" + customerDetails.getProjectName() + "/"
										+ testSetLine.getTestRun().getTestRunName() + "/" + pdfName,
								HttpMethod.DELETE, deleteSessionRequest, Object.class);
					}
				}
			}

		} catch (Exception e) {
			System.out.println("Pdf is not present in sharepoint");
		}
	}

}
