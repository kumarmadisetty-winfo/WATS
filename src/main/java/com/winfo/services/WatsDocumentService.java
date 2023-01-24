package com.winfo.services;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.transaction.Transactional;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.ByteStreams;
import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.model.BmcException;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.GetObjectRequest;
import com.oracle.bmc.objectstorage.responses.GetObjectResponse;
import com.winfo.dao.WatsPluginDao;
import com.winfo.exception.WatsEBSCustomException;
import com.winfo.model.ScriptMaster;
import com.winfo.model.ScriptMetaData;
import com.winfo.vo.DomGenericResponseBean;
import com.winfo.vo.WatsDocumentVo;
import com.winfo.vo.WatsLoginVO;
import com.winfo.vo.WatsPluginMasterVO;
import com.winfo.vo.WatsPluginMetaDataVO;
import com.winfo.vo.WatsScriptAssistantVO;

@Service
public class WatsDocumentService {

Logger log = Logger.getLogger("Logger");
	
	@Value("${oci.config.name.common}")
	private String ociConfigName;
	@Value("${oci.bucket.name.common}")
	private String ociBucketName;
	@Value("${oci.namespace.common}")
	private String ociNamespace;
	@Value("${oci.config.path.common}")
	private String ociConfigPath;
	public static final String FORWARD_SLASH = "/";
	@Autowired
	WatsPluginDao dao;
	
	public ResponseEntity<StreamingResponseBody> getWatsDocumentPDFFile(WatsDocumentVo documentVo) throws IOException {

		GetObjectResponse response = null;
		final ConfigFileReader.ConfigFile configFile = ConfigFileReader
				.parse(new FileInputStream(new File(ociConfigPath)), ociConfigName);
		final AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);
		try (ObjectStorageClient client = new ObjectStorageClient(provider);) {
				GetObjectRequest getObjectRequest = GetObjectRequest.builder().namespaceName(ociNamespace)
						.bucketName(ociBucketName).objectName("watsDocuments/"+documentVo.getWatsVersion()+"/"+documentVo.getFileName()+".pdf").build();
				response = client.getObject(getObjectRequest);
				InputStream fis= response.getInputStream();
				long len=response.getContentLength();
		        byte[] targetArray = IOUtils.toByteArray(fis);
				return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).header("Content-Disposition", "attachment; filename=\""+documentVo.getFileName()+".pdf\"").header("Content-Length",""+response.getContentLength())
						.body(out -> {
							out.write(targetArray);
							out.flush();
							});
		    } catch (WatsEBSCustomException e) {
				throw e;
			} catch (BmcException e) {
		    	throw new WatsEBSCustomException(500,documentVo.getFileName()+".pdf"+ " is not exist for "+documentVo.getWatsVersion()+" WATS Version",e);
		    }catch (IOException e) {
				throw new WatsEBSCustomException(500, "Exception occured while returning file from service", e);
			} catch (Exception e) {
				System.out.println(e);
		    	throw new WatsEBSCustomException(500, "Exception occured while downloading pdf from Object Store", e);
		    }
	}
}
