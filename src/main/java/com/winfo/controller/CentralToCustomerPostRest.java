package com.winfo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.winfo.services.CentralToCustomerPostService;
import com.winfo.vo.DomGenericResponseBean;
import com.winfo.vo.WatsMasterDataVOList;

@RestController
public class CentralToCustomerPostRest {

	@Autowired
	CentralToCustomerPostService service;
	
	@PostMapping("/centralToCustomerScriptMigrate")
	public List<DomGenericResponseBean> scriptMetaDataListFromCentral(@RequestBody WatsMasterDataVOList mastervolist) {
		return service.saveScriptMasterDtls(mastervolist);
		
	}

}
