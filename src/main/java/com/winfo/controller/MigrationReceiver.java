package com.winfo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.winfo.serviceImpl.CentralToCustomerPostService;
import com.winfo.serviceImpl.GetApiValidationMigrationService;
import com.winfo.serviceImpl.TestRunMigrationGetService;
import com.winfo.vo.ApiValidationDto;
import com.winfo.vo.DomGenericResponseBean;
import com.winfo.vo.ResponseDto;
import com.winfo.vo.TestRunMigrationDto;
import com.winfo.vo.WatsMasterDataVOList;

@RestController
public class MigrationReceiver {

	public static final Logger logger = Logger.getLogger(MigrationReceiver.class);

	@Autowired
	CentralToCustomerPostService service;

	@Autowired
	GetApiValidationMigrationService apiValidationMigrationService;

	@Autowired
	TestRunMigrationGetService testRunMigrateGetService;

	@PostMapping("/centralToCustomerScriptMigrate")
	public List<DomGenericResponseBean> scriptMetaDataListFromCentral(@RequestBody WatsMasterDataVOList mastervolist) {
		return service.saveScriptMasterDtls(mastervolist);

	}

	@PostMapping("/apiValidationMigrationReceiver")
	public ResponseDto apiValidationMigration(@RequestBody ApiValidationDto lookUpCodeVOData) {
		return apiValidationMigrationService.apiValidationMigration(lookUpCodeVOData);
	}

	@PostMapping("/testRunMigrationToCustomer")
	public Map<String, List<DomGenericResponseBean>> pluginData(
			@RequestBody List<TestRunMigrationDto> listOfTestRunDto) {
		Map<String, List<DomGenericResponseBean>> response = new HashMap<>();
		response.put("response", testRunMigrateGetService.centralRepoData(listOfTestRunDto));
		return response;
	}

}
