package com.winfo.services;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

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
import com.winfo.vo.CustomerProjectDto;
import com.winfo.vo.DeleteScriptDto;
import com.winfo.vo.ResponseDto;

@Service
public class DeletionOfScriptService {

	public final Logger logger = LogManager.getLogger(DeletionOfScriptService.class);

	@Value("${oci.config.name}")
	private String ociConfigName;

	@Value("${oci.bucket.name}")
	private String ociBucketName;

	@Value("${oci.namespace}")
	private String ociNamespace;

	@Autowired
	DataBaseEntry dataBaseEntry;

	private static final String SCREENSHOT = "Screenshot";
	public static final String FORWARD_SLASH = "/";

	public ResponseDto deleteScriptFromTestRun(DeleteScriptDto testScriptDto) throws Exception {
		String testSetId = testScriptDto.getTestSetId();
		String testSetLineId = testScriptDto.getTestSetLineId();
		TestSetLine testSetLine = dataBaseEntry.getTestSetLinesRecord(testSetId, testSetLineId);
		CustomerProjectDto customerDetails = dataBaseEntry.getCustomerDetails(testSetId);
		ConfigFileReader.ConfigFile configFile = null;

		try {
			configFile = ConfigFileReader.parse(new ClassPathResource("oci/config").getInputStream(), ociConfigName);
		} catch (IOException e) {
			throw new WatsEBSCustomException(500, "Not able to connect with object store");
		}
		try {
			final AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);
			deleteScreenShot(testSetLine, customerDetails, provider);
			deletePdf(testSetLine, customerDetails, provider);
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
			AuthenticationDetailsProvider provider) throws Exception {
		List<String> objNames = null;
		try (ObjectStorage client = new ObjectStorageClient(provider);) {

			String objectStoreScreenShotPath = SCREENSHOT + FORWARD_SLASH + customerDetails.getCustomerName()
					+ FORWARD_SLASH + testSetLine.getTestRun().getTestRunName() + FORWARD_SLASH
					+ testSetLine.getSeqNum();

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
					DeleteObjectResponse getResponse = client.deleteObject(DeleteObjectRequest.builder()
							.namespaceName(ociNamespace).bucketName(ociBucketName).objectName(objectName).build());
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
			AuthenticationDetailsProvider provider) throws Exception {
		List<String> objNames = null;
		try (ObjectStorage client = new ObjectStorageClient(provider);) {

			String objectStorePdfPath = customerDetails.getCustomerName() + FORWARD_SLASH
					+ testSetLine.getTestRun().getTestRunName() + FORWARD_SLASH + testSetLine.getSeqNum();

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
					DeleteObjectResponse getResponse = client.deleteObject(DeleteObjectRequest.builder()
							.namespaceName(ociNamespace).bucketName(ociBucketName).objectName(objectName).build());
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

}
