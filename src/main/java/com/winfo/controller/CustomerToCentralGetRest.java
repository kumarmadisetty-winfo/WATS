package com.winfo.controller;

import java.util.List;
import org.json.simple.JSONObject;

//import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.winfo.vo.DomGenericResponseBean;
import com.winfo.vo.ScriptId;
import com.winfo.vo.WatsMasterDataVOList;
import com.winfo.services.CustomerToCentralGetService;
import com.winfo.vo.WatsMasterVO;

@RestController
public class CustomerToCentralGetRest {

	@Autowired
	CustomerToCentralGetService service;

	@RequestMapping("/customerTocentral_customer")
	public String customerRepoData(@RequestBody ScriptId scriptID) throws ParseException {
		System.out.print("service");
		return service.customerRepoData(scriptID);
		
	}

}
