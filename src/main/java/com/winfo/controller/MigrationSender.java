package com.winfo.controller;

import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.winfo.serviceImpl.CustomerToCentralGetService;
import com.winfo.serviceImpl.PostApiValidationMigrationService;
import com.winfo.serviceImpl.TestRunMigrationService;
import com.winfo.vo.ApiValidationMigrationDto;
import com.winfo.vo.ResponseDto;
import com.winfo.vo.ScriptDtlsDto;
import com.winfo.vo.TestRunDetails;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class MigrationSender {

	public static final Logger logger = Logger.getLogger(MigrationSender.class);
	@Autowired
	PostApiValidationMigrationService apiValidationMigrationService;
	@Autowired
	CustomerToCentralGetService service;
	@Autowired
	TestRunMigrationService testRunMigrationService;

	@PostMapping("/apiValidationMigrationSender")
	@ApiOperation( value="Api Validation Migration ",notes = "<B>LookUpCodes:</B> LookUpCodes should be provided to migrate the lookUpCode to the customer<br>"
			+ "<B>TargetEnvironment:</B>TargetEnvironment should be provided to identify which customer apivalidationMigration can be done<br>")
	@ApiResponses( value = { @ApiResponse( code=200,message="ApiValidation Lookupcodes Migrated Successfully")})
	public ResponseDto apiValidationMigration(@RequestBody ApiValidationMigrationDto apiValidationMigration) {
		return apiValidationMigrationService.apiValidationMigration(apiValidationMigration);
	}

	@PostMapping("/scriptMigrateFromCustomerToCentral")
	@ApiOperation( value="Script Migration ",notes = "<B>ScriptId:</B> ScriptId should be provided to migrate the script to the customer<br>"
			+ "<B>CustomerName:</B>CustomerName should be provided to which customer to migrate the script<br>"
			+ "<B>ProductVersion:</B>productVersion should be provided because if script multple productVersion then based upon productVersion will migrate the script")
	@ApiResponses( value = { @ApiResponse( code=200,message="Script Migrated Successfully")})
	public String customerRepoData(@RequestBody ScriptDtlsDto scriptDtls) {
		logger.info("SCRIPT IDS**" + scriptDtls.getScriptId());
		return service.scriptMetaData(scriptDtls);

	}
	
	@PostMapping("/testRunMigration")
	@ApiOperation( value="Test Run Migration ",notes = "<B>TestSetId:</B> testSetId should be provided to migrate the test run<br>"
			+ "<B>CustomerName:</B>CustomerName should be provided to which customer to migrate the test run<br>"
			+ "<B>forceMigrate:</B>Initially we should pass forceMigrate as 'false'.If testRun is already existing in the customer Environment then we should pass forceMigrate as 'true', then it will migrate the current test run ")
	@ApiResponses( value = { @ApiResponse( code=200,message="TestRun Migrated Successfully")})
	public String testRunMigration(@RequestBody TestRunDetails testRunDetails)
			throws ParseException, JsonProcessingException {
		logger.info("TestRunId**" + testRunDetails.getListOfTestRun().get(0));
		return testRunMigrationService.testRunMigration(testRunDetails);
	}

}
