package com.winfo.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.winfo.dao.WatsPluginDao;
import com.winfo.exception.WatsEBSCustomException;

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
		    } catch (WatsEBSCustomException e) {
		    	log.error(e.getErrorMessage());
				throw e;
			} catch (BmcException e) {
				log.error(fileName+ " is not exist in Object Store");
		    	throw new WatsEBSCustomException(e.getStatusCode(),fileName+ " is not exist in Object Store",e);
		    }catch (IOException e) {
		    	log.error("Exception occured while returning file from service");
				throw new WatsEBSCustomException(403, "Exception occured while returning file from service", e);
			} catch (Exception e) {
				log.error("Exception occured while downloading "+fileName+" from Object Store");
		    	throw new WatsEBSCustomException(500, "Exception occured while downloading "+fileName+" from Object Store", e);
		    }
	}
}
