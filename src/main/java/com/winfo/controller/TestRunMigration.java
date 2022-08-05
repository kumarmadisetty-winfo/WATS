package com.winfo.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.winfo.services.TestRunMigrationGetService;
import com.winfo.services.TestRunMigrationService;
import com.winfo.vo.DomGenericResponseBean;
import com.winfo.vo.TestRunDetails;
import com.winfo.vo.TestRunMigrationDto;

@RestController
public class TestRunMigration {

	public final Logger logger = LogManager.getLogger(TestRunMigration.class);

	@Autowired
	TestRunMigrationService service;

	@Autowired
	TestRunMigrationGetService testRunMigrateGetService;

	@PostMapping("/testRunMigration")
	public String testRunMigration(@RequestBody TestRunDetails testRunDetails)
			throws ParseException, JsonProcessingException {
		logger.info("TestRunId**" + testRunDetails.getListOfTestRun().get(0));
		return service.testRunMigration(testRunDetails);

	}

	@PostMapping("/testRunMigrationToCustomer")
	public List<DomGenericResponseBean> pluginData(@RequestBody List<TestRunMigrationDto> listOfTestRunDto) {
		return testRunMigrateGetService.centralRepoData(listOfTestRunDto);

	}
}
