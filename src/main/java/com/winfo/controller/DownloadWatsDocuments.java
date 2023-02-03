package com.winfo.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.winfo.exception.WatsEBSCustomException;
import com.winfo.services.WatsDocumentService;
import com.winfo.services.WatsPluginService;
import com.winfo.vo.WatsDocumentVo;
import com.winfo.vo.WatsPluginMasterVO;
import com.winfo.vo.WatsScriptAssistantVO;
@RestController
public class DownloadWatsDocuments {
	
	public final Logger log = LogManager.getLogger(DownloadWatsDocuments.class);

	@Autowired
	WatsDocumentService service;
	
	@PostMapping(value = "/downloadWatsDoc" , produces= MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<StreamingResponseBody>  getWatsDocument(@Valid @RequestBody WatsDocumentVo documentVo) throws Exception {

			return service.getWatsDocumentPDFFile(documentVo);
	}
}
