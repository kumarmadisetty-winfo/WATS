package com.winfo.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.winfo.services.CustomerToCentralGetService;
import com.winfo.vo.ScriptDtlsDto;

@RestController
public class CustomerToCentralGetRest {

	public final Logger logger = LogManager.getLogger(CustomerToCentralGetRest.class);

	@Autowired
	CustomerToCentralGetService service;

	@RequestMapping("/scriptMigrateFromCustomerToCentral")
	public String customerRepoData(@RequestBody ScriptDtlsDto scriptDtls) {
		logger.info("SCRIPT IDS**" + scriptDtls.getScriptId());
		return service.scriptMetaData(scriptDtls);

	}

}
