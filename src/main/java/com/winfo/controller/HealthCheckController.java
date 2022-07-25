package com.winfo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.winfo.services.HealthCheck;
import com.winfo.vo.ResponseDto;
import com.winfo.vo.TestScriptDto;

@Controller
public class HealthCheckController {
	
	@Autowired
	HealthCheck healthCheck;
	
	@ResponseBody
	@RequestMapping(value = "/sanityCheckForExecuteApi")
	public ResponseDto healthCheckStatus(@RequestBody TestScriptDto testSetId){
		return healthCheck.sanityCheckMethod(testSetId.getTestScriptNo());
	}

}
