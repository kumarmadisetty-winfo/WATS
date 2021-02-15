package com.winfo.controller;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.winfo.services.CopyDataService;
import com.winfo.vo.CopyDataDetails;
//import com.winfo.vo.ScriptAndCustomeridDetails;
@RestController
public class CopyDataRest {
	@Autowired
	CopyDataService service;

	@RequestMapping("/copydata")
	public String copyData(@RequestBody CopyDataDetails copyDataDetails ) throws ParseException {
		return service.copyData(copyDataDetails);
		
	}
}
