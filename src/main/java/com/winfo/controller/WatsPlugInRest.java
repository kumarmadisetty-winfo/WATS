package com.winfo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.winfo.services.PluginTestrunService;
import com.winfo.services.WatsPluginService;
import com.winfo.vo.DomGenericResponseBean;
import com.winfo.vo.WatsLoginVO;
import com.winfo.vo.WatsPluginMasterVO;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class WatsPlugInRest {

	@Autowired
	WatsPluginService service;
	@Autowired
	PluginTestrunService testrunservice;

	@PostMapping("/pluginData")
	public DomGenericResponseBean pluginData(@RequestBody WatsPluginMasterVO mastervo) {
		return service.pluginData(mastervo);
		
	}
	
	
	@PostMapping("/login")
	public DomGenericResponseBean watsLogin(@RequestBody WatsLoginVO loginvo){
		return service.watslogin(loginvo);
	}
	
	
	@GetMapping("/testrunNames/{productverson}")
	public List<String> getTestrunData(@PathVariable String productverson){
	      System.out.println(productverson);
	      System.out.println(service.getTestrunDataPVerson(productverson));
		return service.getTestrunDataPVerson(productverson);
	}
	
	
	@PostMapping("/testrunData")
	public DomGenericResponseBean updateTestrun(@RequestBody WatsPluginMasterVO mastervo){
		if(mastervo.getTestrunName().equals("")) {
			return service.pluginData(mastervo);
		}
		else {
			return testrunservice.updateTestrun(mastervo);

		}
	}

}
