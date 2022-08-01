package com.winfo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.winfo.services.TestRunMigrationGetService;
import com.winfo.vo.DomGenericResponseBean3;
import com.winfo.vo.TestRunMigrationDto;

@RestController
public class CentralToCustomerTestRunMigration {
	
	@Autowired
	TestRunMigrationGetService service;
	
	@PostMapping("/testRunMigrationToCustomer")
	public DomGenericResponseBean3 pluginData(@RequestBody List<TestRunMigrationDto> mastervolist) {
		return service.centralRepoData(mastervolist);
		
	}

}
