package com.winfo.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
import com.oracle.bmc.objectstorage.responses.GetObjectResponse;
import com.winfo.config.MessageUtil;
import com.winfo.dao.WatsPluginDao;
import com.winfo.exception.WatsEBSException;

@Service
public class CommonObjectStoreUtils { 

	public final Logger log = LogManager.getLogger(CommonObjectStoreUtils.class);
	
	@Value("${oci.config.name.common}")
	private String ociConfigName;
	@Value("${oci.bucket.name.common}")
	private String ociBucketName;
	@Value("${oci.namespace.common}")
	private String ociNamespace;
	@Value("${oci.config.path}")
	private String ociConfigPath;
	public static final String FORWARD_SLASH = "/";
	@Autowired
	WatsPluginDao dao;
	@Autowired
	MessageUtil messageUtil;
	
	public ResponseEntity<StreamingResponseBody> getFileFromCommonObjectStore(String filePath,String fileName,MediaType mediaType) throws IOException {

		GetObjectResponse response = null;
		final ConfigFileReader.ConfigFile configFile = ConfigFileReader
				.parse(new FileInputStream(new File(ociConfigPath)), ociConfigName);
		final AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);
		try (ObjectStorageClient client = new ObjectStorageClient(provider);) {
				GetObjectRequest getObjectRequest = GetObjectRequest.builder().namespaceName(ociNamespace)
						.bucketName(ociBucketName).objectName(filePath+fileName).build();
				response = client.getObject(getObjectRequest);
				InputStream fis= response.getInputStream();
		        byte[] targetArray = IOUtils.toByteArray(fis);
				return ResponseEntity.ok().contentType(mediaType).header("Content-Disposition", "attachment; filename=\""+fileName+"\"").header("Content-Length",""+response.getContentLength())
						.body(out -> {
							out.write(targetArray);
							out.flush();
							});
		    } catch (WatsEBSException e) {
		    	log.error(e.getErrorMessage());
				throw e;
			} catch (BmcException e) {
				log.error(fileName+ " is not exist in Object Store");
				throw new WatsEBSException(e.getStatusCode(),
						MessageUtil.getMessage(messageUtil.getCommonObjectStoreUtils().getError().getFileNotPresent(), fileName), e);
		    }catch (IOException e) {
		    	log.error("Exception occurred while returning file from service");

			} catch (Exception e) {
				log.error("Exception occurred while downloading "+fileName+" from Object Store");
		    

				throw new WatsEBSException(HttpStatus.FORBIDDEN.value(), messageUtil.getCommonObjectStoreUtils().getError().getFailedToReturnTheFile(), e);
			} catch (Exception e) {
				log.error("Exception occurred while downloading "+fileName+" from Object Store");
		    	throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(),MessageUtil.getMessage(messageUtil.getCommonObjectStoreUtils().getError().getDownloadFailed(),fileName), e);

		    }
	}
	
	public PDDocument readFileFromCommonObjectStore(String folderName,String fileName) {
		GetObjectResponse response = null;
		try {
			final ConfigFileReader.ConfigFile configFile = ConfigFileReader
					.parse(new FileInputStream(new File(ociConfigPath)), ociConfigName);
			final AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);
			String destinationFilePath = folderName + FORWARD_SLASH + fileName;
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
			log.error(fileName+ " is not exist in Object Store");
			throw new WatsEBSException(e.getStatusCode(),
					MessageUtil.getMessage(messageUtil.getCommonObjectStoreUtils().getError().getFileNotPresent(), fileName), e);
	    }catch (IOException e) {
	    	log.error("Exception occurred while fetching file from service");

		}  catch (Exception e) {
		
			throw new WatsEBSException(HttpStatus.FORBIDDEN.value(), messageUtil.getCommonObjectStoreUtils().getError().getFailedToReturnTheFile(), e);
		}  catch (Exception e) {
			throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occurred while reading pdf in Object Storage", e);

		}
	}
}
