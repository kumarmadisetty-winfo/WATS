package com.winfo.controller;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.winfo.services.TestRunMigrationService;
import com.winfo.vo.TestRunDetails;

@RestController
public class TestRunMigration {

	@Autowired
	TestRunMigrationService service;

	@PostMapping("/testRunMigration")
	public String testRunMigration(@RequestBody TestRunDetails testRunDetails)
			throws ParseException, JsonProcessingException {
		return service.testRunMigration(testRunDetails);

	}
}
