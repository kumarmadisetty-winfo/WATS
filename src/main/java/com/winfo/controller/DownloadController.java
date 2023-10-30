package com.winfo.controller;


import javax.validation.Valid;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.winfo.serviceImpl.DownloadDocumnetsService;
import com.winfo.vo.DocumentsVo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DownloadController {
	
	public static final Logger logger = Logger.getLogger(DownloadController.class);

	final DownloadDocumnetsService downloadDocumnetsService;
	
	@PostMapping(value = "/downloadDocument" , produces= MediaType.APPLICATION_PDF_VALUE)
	@ApiOperation( value="Download Test Run/Script/Release Note PDF",notes = "<B> FileName and FilePath </B> should be provided to download PDF <br>"
			+ "will get file from object store and download PDF")
	@ApiResponses( value = { @ApiResponse( code=200,message="PDF downloaded successfully")})
	public ResponseEntity<StreamingResponseBody>  downloadDocument(@Valid @RequestBody DocumentsVo documentsVo) throws Exception {

			return downloadDocumnetsService.retrieveDocumentsFromObjectStore(documentsVo);
	}
}
