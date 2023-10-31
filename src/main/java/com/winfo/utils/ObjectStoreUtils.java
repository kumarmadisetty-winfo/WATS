package com.winfo.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.model.BmcException;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.GetObjectRequest;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.objectstorage.responses.GetObjectResponse;
import com.winfo.config.MessageUtil;
import com.winfo.exception.WatsEBSException;
import com.winfo.vo.ResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ObjectStoreUtils { 

	public final Logger log = LogManager.getLogger(ObjectStoreUtils.class);
	
	@Value("${oci.config.path}")
	private String ociConfigPath;

	final MessageUtil messageUtil;
	
	public ResponseEntity<StreamingResponseBody> getFileFromObjectStore(String filePath,String fileName,MediaType mediaType,String ociConfigName,
			String ociBucketName,String ociNamespace) throws IOException {

		GetObjectResponse response = null;
		try {
			final ConfigFileReader.ConfigFile configFile = ConfigFileReader
					.parse(new FileInputStream(new File(ociConfigPath)), ociConfigName);
			log.info("Successfully read the config file");
			final AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);
			log.info("Successfully authenticated to object store");
			try (ObjectStorageClient client = new ObjectStorageClient(provider);) {
				GetObjectRequest getObjectRequest = GetObjectRequest.builder().namespaceName(ociNamespace)
						.bucketName(ociBucketName).objectName(filePath + fileName).build();
				response = client.getObject(getObjectRequest);
				log.info(fileName+" file found successfully");
				InputStream fis = response.getInputStream();
				byte[] targetArray = IOUtils.toByteArray(fis);
				return ResponseEntity.ok().contentType(mediaType).header("Content-Disposition", "attachment; filename=\"" + fileName + "\"").header("Content-Length", "" + response.getContentLength())
						.body(out -> {
							out.write(targetArray);
							out.flush();
						});
			}
		} catch (WatsEBSException e) {
			log.error(e.getErrorMessage());
			throw e;
		} catch (BmcException e) {
			log.error(fileName + " is not exist in Object Store");
			throw new WatsEBSException(e.getStatusCode(),
					MessageUtil.getMessage(messageUtil.getObjectStoreUtils().getError().getFileNotPresent(), fileName), e);
		} catch (IOException e) {
			log.error(messageUtil.getObjectStoreUtils().getError().getFailedToReturnTheFile()+" : "+e.getMessage());
			throw new WatsEBSException(403, messageUtil.getObjectStoreUtils().getError().getFailedToReturnTheFile(), e);
		} catch (Exception e) {
			log.error("Exception occurred while downloading " + fileName + " from Object Store");
			throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), MessageUtil.getMessage(messageUtil.getObjectStoreUtils().getError().getDownloadFailed(), fileName), e);
		}
	}
	
	public PDDocument readFileFromObjectStore(String folderName,String fileName,String ociConfigName,
			String ociBucketName,String ociNamespace) {
		GetObjectResponse response = null;
		try {
			final ConfigFileReader.ConfigFile configFile = ConfigFileReader
					.parse(new FileInputStream(new File(ociConfigPath)), ociConfigName);
			log.info("Successfully read the config file");
			final AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);
			log.info("Successfully authenticated to object store");
			String destinationFilePath = folderName + Constants.FORWARD_SLASH + fileName;
			/* Create a service client */
			try (ObjectStorageClient client = new ObjectStorageClient(provider);) {
				/* Create a request and dependent object(s). */
				GetObjectRequest getObjectRequest = GetObjectRequest.builder().namespaceName(ociNamespace)
						.bucketName(ociBucketName).objectName(destinationFilePath).build();
				response = client.getObject(getObjectRequest);
				PDDocument document = PDDocument.load(response.getInputStream());
				return document;
			}
		} catch (BmcException e) {
			log.error(fileName + " is not exist in Object Store");
			throw new WatsEBSException(e.getStatusCode(),
					MessageUtil.getMessage(messageUtil.getObjectStoreUtils().getError().getFileNotPresent(), fileName), e);
		} catch (IOException e) {
			log.error("Exception occurred while fetching file from service");
			throw new WatsEBSException(403, messageUtil.getObjectStoreUtils().getError().getFailedToReturnTheFile(), e);
		} catch (Exception e) {
			log.error("Exception occurred while reading pdf from Object Storage");
			throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occurred while reading pdf from Object Storage", e);

		}
	}
	
	public  ResponseDto putFileInObjectStore(String filePath,String fileName,MediaType mediaType,String ociConfigName,
			String ociBucketName,String ociNamespace,byte[] fileBytes) throws IOException {

		try {
			final ConfigFileReader.ConfigFile configFile = ConfigFileReader
					.parse(new FileInputStream(new File(ociConfigPath)), ociConfigName);
			log.info("Successfully read the config file");
			final AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);
			log.info("Successfully authenticated to object store");
			try (ObjectStorageClient client = new ObjectStorageClient(provider);) {
				PutObjectRequest request = PutObjectRequest.builder()
	                    .bucketName(ociBucketName)
	                    .namespaceName(ociNamespace)
	                    .objectName(filePath + fileName)
	                    .contentType("application/octet-stream")
	                    .contentLength((long) fileBytes.length)
	                    .putObjectBody(new ByteArrayInputStream(fileBytes))
	                    .build();
				client.putObject(request);
				return new ResponseDto(HttpStatus.OK.value(), Constants.SUCCESS,
						fileName+ " successfully uploaded");
			}
		} catch (BmcException e) {
			log.error("Oracle Cloud service error: " + e.getServiceCode() + " - " + e.getMessage());
			throw new WatsEBSException(e.getStatusCode(),"Oracle Cloud service error: " + e.getServiceCode() + " - " + e.getMessage(), e);
        } catch (IOException e) {
            log.error("Input/Output error: " + e.getMessage());
            throw new WatsEBSException(403, "Input/Output error: " + e.getMessage(), e);
        } catch (Exception e) {
        	log.error("Failed to upload the file: " + e.getMessage());
        	throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Failed to upload the file: " + e.getMessage(), e);
        }
	}
}
