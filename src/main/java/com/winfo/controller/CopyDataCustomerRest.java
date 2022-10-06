package com.winfo.controller;

import java.util.List;

import org.json.simple.parser.ParseException;
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

	@RequestMapping("/copydata_customer")
	public List<DomGenericResponseBean> copyData(@RequestBody CopyDataDetails copyDataDetails) throws ParseException {
		return service.copyData(copyDataDetails);

	}
}
