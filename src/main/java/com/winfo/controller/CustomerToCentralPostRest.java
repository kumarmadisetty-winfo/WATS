package com.winfo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.winfo.vo.DomGenericResponseBean;
import com.winfo.vo.WatsMasterDataVOList;
import com.winfo.services.CustomerToCentralPostService;
import com.winfo.vo.WatsMasterVO;

@RestController
public class CustomerToCentralPostRest {

	@Autowired
	CustomerToCentralPostService service;

	@PostMapping("/customerTocentral_central")
	public List<DomGenericResponseBean> pluginData(@RequestBody WatsMasterDataVOList mastervolist) {
		return service.centralRepoData(mastervolist);
		
	}

}
