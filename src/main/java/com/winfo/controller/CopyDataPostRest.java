package com.winfo.controller;

import java.util.List;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.winfo.services.CopyDataPostService;
import com.winfo.services.CustomerToCentralPostService;

import com.winfo.vo.DomGenericResponseBean;
import com.winfo.vo.WatsMasterDataVOList;
@RestController
public class CopyDataPostRest {
	@Autowired
	CopyDataPostService service;
	
	@PostMapping("/copyData_post")
	public List<DomGenericResponseBean> copyDataPost(@RequestBody WatsMasterDataVOList mastervolist ) throws ParseException {
		return service.copyDataPost(mastervolist);
		
	}
}
