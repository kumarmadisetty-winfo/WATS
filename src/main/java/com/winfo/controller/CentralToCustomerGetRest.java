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
import com.winfo.vo.ScriptAndCustomeridDetails;
import com.winfo.vo.WatsMasterDataVOList;
import com.winfo.services.CentralToCustomerGetService;
import com.winfo.vo.WatsMasterVO;

@RestController
public class CentralToCustomerGetRest {

	@Autowired
	CentralToCustomerGetService service;

	@RequestMapping("/centralTocustomer")
	public String customerRepoData(@RequestBody ScriptAndCustomeridDetails scriptandcustomeriddetails ) throws ParseException {
		return service.customerRepoData( scriptandcustomeriddetails);
		
	}

}
