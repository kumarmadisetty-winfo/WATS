
package com.winfo.serviceImpl;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import com.winfo.utils.Constants;
import com.winfo.utils.ObjectStoreUtils;
import com.winfo.vo.PDFVo;

@Service
public class DownloadPDFService {

	public static final Logger logger = Logger.getLogger(DownloadPDFService.class);
	
	@Value("${oci.config.name.common}")
	private String ociConfigNameCommonObjStore;
	@Value("${oci.bucket.name.common}")
	private String ociBucketNameCommonObjStore;;
	@Value("${oci.namespace.common}")
	private String ociNamespaceCommonObjStore;
	@Value("${oci.config.name}")
	private String ociConfigName;
	@Value("${oci.bucket.name}")
	private String ociBucketName;
	@Value("${oci.namespace}")
	private String ociNamespace;
	@Autowired
	ObjectStoreUtils objectStoreUtils;
	
	public ResponseEntity<StreamingResponseBody> getPDFFromObjectStore(PDFVo pdfVO) throws IOException {
		if(pdfVO.isCommonObjectStore())
			return objectStoreUtils.getFileFromObjectStore(pdfVO.getFilePath()+Constants.FORWARD_SLASH,pdfVO.getFileName(),MediaType.APPLICATION_PDF,
					ociConfigNameCommonObjStore, ociBucketNameCommonObjStore, ociNamespaceCommonObjStore);
		else
			return objectStoreUtils.getFileFromObjectStore(pdfVO.getFilePath()+Constants.FORWARD_SLASH,pdfVO.getFileName(),MediaType.APPLICATION_PDF,
					ociConfigName, ociBucketName, ociNamespace);
		
	}
}
