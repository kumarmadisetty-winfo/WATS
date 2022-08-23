package com.winfo.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.winfo.services.PostApiValidationMigrationService;
import com.winfo.vo.ApiValidationMigrationDto;
import com.winfo.vo.ResponseDto;

@RestController
public class PostApiValidationMigration {
	
	public final Logger logger = LogManager.getLogger(PostApiValidationMigration.class);
	
	@Autowired
	PostApiValidationMigrationService apiValidationMigrationService;
	
	@PostMapping("/apiValidationMigrationSender")
	public ResponseDto apiValidationMigration(@RequestBody ApiValidationMigrationDto apiValidationMigration) {
		return apiValidationMigrationService.apiValidationMigration(apiValidationMigration);
	}

}
