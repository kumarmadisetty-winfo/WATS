package com.winfo.controller;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.winfo.services.DeleteDataService;
import com.winfo.vo.DeleteScriptsData;


@RestController
public class DeleteDataRest {
	@Autowired
	DeleteDataService service;

	@RequestMapping("/delete_data")
	public String deleteData(@RequestBody DeleteScriptsData deletescriptsdata) throws ParseException {
		return service.deleteData(deletescriptsdata);
		
	}
}



