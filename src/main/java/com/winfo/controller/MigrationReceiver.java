package com.winfo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.winfo.exception.WatsEBSException;
import com.winfo.serviceImpl.CentralToCustomerPostService;
import com.winfo.serviceImpl.GetApiValidationMigrationService;
import com.winfo.serviceImpl.TestRunMigrationGetService;
import com.winfo.utils.Constants;
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
	
	@Autowired
	DomGenericResponseBean domGenericResponseBean;

	@PostMapping("/centralToCustomerScriptMigrate/{customerName}")
	public List<DomGenericResponseBean> scriptMetaDataListFromCentral(@RequestBody WatsMasterDataVOList mastervolist,@PathVariable String customerName) {
		if(!"".equals(customerName) && customerName!=null) {
			return service.saveScriptMasterDtls(mastervolist,customerName);			
		}else{
			logger.error(Constants.CUSTOMER_ERROR);
			throw new WatsEBSException(HttpStatus.NOT_FOUND.value(),Constants.CUSTOMER_ERROR);
		}

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
