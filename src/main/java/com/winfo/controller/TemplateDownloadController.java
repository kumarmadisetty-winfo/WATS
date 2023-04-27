package com.winfo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.winfo.services.TemplateDownloadService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class TemplateDownloadController {
	
	@Autowired
	private TemplateDownloadService templateDownloaderService;
	
	@GetMapping(value={"/downloadTemplate","/downloadTemplate/{scriptId}"})
	@ApiOperation( value="Download Template ",notes = " Download Template ")
	@ApiResponses( value = { @ApiResponse( code=200,message="Success")})
	public ResponseEntity<StreamingResponseBody> generateTemplete(@PathVariable Optional<Integer> scriptId) throws Exception {
		return templateDownloaderService.generateTemplate(scriptId);
	}
	
}
