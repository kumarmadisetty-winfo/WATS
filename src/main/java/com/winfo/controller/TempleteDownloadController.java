package com.winfo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.winfo.services.TempleteDownloadService;

@RestController
public class TempleteDownloadController {
	
	@Autowired
	private TempleteDownloadService templeteDownloaderService;
	
	@GetMapping(value="/downloadTemplete")
	public ResponseEntity<StreamingResponseBody> test() throws Exception {
		return templeteDownloaderService.generateTemplete();
	}
	
}
