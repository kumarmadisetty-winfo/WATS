package com.winfo.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import com.winfo.dao.WatsPluginDao;
import com.winfo.utils.CommonObjectStoreUtils;
import com.winfo.vo.WatsDocumentVo;

@Service
public class WatsDocumentService {

	public final Logger log = LogManager.getLogger(WatsDocumentService.class);
	
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
	CommonObjectStoreUtils commonObjectStoreUtils;
	
	public ResponseEntity<StreamingResponseBody> getWatsDocumentPDFFile(WatsDocumentVo documentVo) throws IOException {

		return commonObjectStoreUtils.getFileFromCommonObjectStore("watsDocuments/"+documentVo.getWatsVersion()+"/",documentVo.getFileName(),MediaType.APPLICATION_PDF);
		
	}
}
