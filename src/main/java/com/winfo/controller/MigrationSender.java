package com.winfo.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.winfo.services.CustomerToCentralGetService;
import com.winfo.services.PostApiValidationMigrationService;
import com.winfo.services.TestRunMigrationService;
import com.winfo.vo.ApiValidationMigrationDto;
import com.winfo.vo.ResponseDto;
import com.winfo.vo.ScriptDtlsDto;
import com.winfo.vo.TestRunDetails;

@RestController
public class MigrationSender {
	public final Logger logger = LogManager.getLogger(MigrationSender.class);
	@Autowired
	PostApiValidationMigrationService apiValidationMigrationService;
	@Autowired
	CustomerToCentralGetService service;
	@Autowired
	TestRunMigrationService testRunMigrationService;
	
	@PostMapping("/apiValidationMigrationSender")
	public ResponseDto apiValidationMigration(@RequestBody ApiValidationMigrationDto apiValidationMigration) {
		return apiValidationMigrationService.apiValidationMigration(apiValidationMigration);
	}
	
	@RequestMapping("/scriptMigrateFromCustomerToCentral")
	public String customerRepoData(@RequestBody ScriptDtlsDto scriptDtls) {
		logger.info("SCRIPT IDS**" + scriptDtls.getScriptId());
		return service.scriptMetaData(scriptDtls);

	}
	
	@PostMapping("/testRunMigration")
	public String testRunMigration(@RequestBody TestRunDetails testRunDetails)
			throws ParseException, JsonProcessingException {
		logger.info("TestRunId**" + testRunDetails.getListOfTestRun().get(0));
		return testRunMigrationService.testRunMigration(testRunDetails);

	}
	


}
