package com.winfo.controller;

import java.util.List;
import java.util.Optional;

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

import com.winfo.exception.WatsEBSCustomException;
import com.winfo.serviceImpl.PluginTestrunService;
import com.winfo.serviceImpl.WatsPluginService;
import com.winfo.vo.DomGenericResponseBean;
import com.winfo.vo.WatsScriptAssistantVO;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

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
	@ApiOperation( value="Plugin Data",notes = " We should pass all the WatsPluginMasterVO fields to create the script using plugin")
	@ApiResponses( value = { @ApiResponse( code=200,message="Script created successfully")})
	public DomGenericResponseBean pluginData(@RequestBody WatsPluginMasterVO mastervo) {
		return service.pluginData(mastervo);

	}

	@PostMapping("/login")
	@ApiOperation( value="Wats Login",notes = " We should pass username and password to wats login")
	@ApiResponses( value = { @ApiResponse( code=200,message="Login Successfully")})
	public DomGenericResponseBean watsLogin(@RequestBody WatsLoginVO loginvo) {
		return service.watslogin(loginvo);
	}

	@GetMapping("/testrunNames/{productverson}")
	@ApiOperation( value="Wats Login",notes = " We should pass productVersion to get testRun names")
	@ApiResponses( value = { @ApiResponse( code=200,message="Success")})
	public List<String> getTestrunData(@PathVariable String productverson) {
		log.info(productverson);
		log.info(service.getTestrunDataPVerson(productverson));
		return service.getTestrunDataPVerson(productverson);
	}

	@PostMapping("/testrunData")
	@ApiOperation( value="Update test run Data",notes = "  We should pass all the WatsPluginMasterVO fields to update the test run data")
	@ApiResponses( value = { @ApiResponse( code=200,message="Success")})
	public DomGenericResponseBean updateTestrun(@RequestBody WatsPluginMasterVO mastervo) {
		if (mastervo.getTestRunName().equals("")) {
			return service.pluginData(mastervo);
		} else {
			return testrunservice.updateTestrun(mastervo);

		}
	}
	
	
	@PostMapping(value = {"/getPluginZipFile/{targetEnvironment}/{browser}","/getPluginZipFile/{targetEnvironment}"} , produces = "application/zip")
	@ApiOperation( value="Get WinfoTest Script Assistance ZipFile",notes = "  We should pass target Environment and browser to download WinfoTest script assistance")
	@ApiResponses( value = { @ApiResponse( code=200,message="Success")})
	public ResponseEntity<StreamingResponseBody> getWatsScriptAssistant(@PathVariable String targetEnvironment,@PathVariable Optional<String> browser) throws Exception {

		if (targetEnvironment != null && (!"".equalsIgnoreCase(targetEnvironment))) {
			WatsScriptAssistantVO plugInVO = new WatsScriptAssistantVO();
			plugInVO.setTargetEnvironment(targetEnvironment);
			if(browser.isPresent()) {
				plugInVO.setBrowser(browser.get());
			}
			return service.getWatsScriptAssistantFile(plugInVO);
		} else {
			throw new WatsEBSCustomException(500, "Customer can not be null or empty");
		}

	}

}
