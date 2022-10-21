package com.winfo.controller;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.winfo.services.PluginTestrunService;
import com.winfo.services.WatsPluginService;
import com.winfo.vo.DomGenericResponseBean;
import com.winfo.vo.PlugInVO;
import com.winfo.vo.WatsLoginVO;
import com.winfo.vo.WatsPluginMasterVO;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class WatsPlugInRest {

	Logger log = Logger.getLogger("Logger");

	@Autowired
	WatsPluginService service;
	@Autowired
	PluginTestrunService testrunservice;

	@PostMapping("/pluginData")
	public DomGenericResponseBean pluginData(@RequestBody WatsPluginMasterVO mastervo) {
		return service.pluginData(mastervo);

	}

	@PostMapping("/login")
	public DomGenericResponseBean watsLogin(@RequestBody WatsLoginVO loginvo) {
		return service.watslogin(loginvo);
	}

	@GetMapping("/testrunNames/{productverson}")
	public List<String> getTestrunData(@PathVariable String productverson) {
		log.info(productverson);
		log.info(service.getTestrunDataPVerson(productverson));
		return service.getTestrunDataPVerson(productverson);
	}

	@PostMapping("/testrunData")
	public DomGenericResponseBean updateTestrun(@RequestBody WatsPluginMasterVO mastervo) {
		if (mastervo.getTestRunName().equals("")) {
			return service.pluginData(mastervo);
		} else {
			return testrunservice.updateTestrun(mastervo);

		}
	}
	
	@PostMapping(value="/getPluginZipFile",  produces="application/zip")
	public ResponseEntity<StreamingResponseBody> getPluginZip(@RequestBody PlugInVO plugInVO) throws IOException {
		return service.getPluginZipFile(plugInVO);
	}

}
