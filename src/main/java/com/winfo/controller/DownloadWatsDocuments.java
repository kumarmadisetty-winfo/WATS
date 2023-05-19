package com.winfo.controller;


import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.winfo.serviceImpl.WatsDocumentService;
import com.winfo.vo.WatsDocumentVo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
@RestController
public class DownloadWatsDocuments {
	
	public final Logger log = LogManager.getLogger(DownloadWatsDocuments.class);

	@Autowired
	WatsDocumentService service;
	
	@PostMapping(value = "/downloadWatsDoc" , produces= MediaType.APPLICATION_PDF_VALUE)
	@ApiOperation( value="Download Wats Document ",notes = "<B> FileName and WatsVersion </B> should be provided to download Wats Document <br>"
			+ "will get file from object store and download wats document")
	@ApiResponses( value = { @ApiResponse( code=200,message="Wats Document downloaded successfully")})
	public ResponseEntity<StreamingResponseBody>  getWatsDocument(@Valid @RequestBody WatsDocumentVo documentVo) throws Exception {

			return service.getWatsDocumentPDFFile(documentVo);
	}
}
