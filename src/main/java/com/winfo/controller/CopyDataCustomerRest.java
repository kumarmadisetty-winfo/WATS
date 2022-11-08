package com.winfo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.winfo.services.CopyDataCustomerService;
import com.winfo.vo.CopyDataDetails;
import com.winfo.vo.DomGenericResponseBean;

@RestController
public class CopyDataCustomerRest {
	@Autowired
	CopyDataCustomerService service;

	@RequestMapping("/copyScriptUsingProductVersion")
	public DomGenericResponseBean copyData(@RequestBody CopyDataDetails copyDataDetails) {
		return service.copyData(copyDataDetails);

	}
}
