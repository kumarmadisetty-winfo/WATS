package com.winfo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.winfo.services.CustomerToCentralGetService;
import com.winfo.vo.ScriptDtlsDto;

@RestController
public class CustomerToCentralGetRest {

	@Autowired
	CustomerToCentralGetService service;

	@RequestMapping("/scriptMigrateFromCustomerToCentral")
	public String customerRepoData(@RequestBody ScriptDtlsDto scriptDtls) {
		return service.scriptMetaData(scriptDtls);

	}

}
