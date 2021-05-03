package com.winfo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.winfo.services.WatsPluginService;
import com.winfo.vo.DomGenericResponseBean;
import com.winfo.vo.WatsLoginVO;
import com.winfo.vo.WatsPluginMasterVO;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class WatsPlugInRest {

	@Autowired
	WatsPluginService service;

	@PostMapping("/pluginData")
	public DomGenericResponseBean pluginData(@RequestBody WatsPluginMasterVO mastervo) {
		return service.pluginData(mastervo);
		
	}
	@PostMapping("/login")
	public DomGenericResponseBean watsLogin(@RequestBody WatsLoginVO loginvo){
		return service.watslogin(loginvo);
	}

}
