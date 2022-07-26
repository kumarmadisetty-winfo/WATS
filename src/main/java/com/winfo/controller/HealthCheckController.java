package com.winfo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.winfo.exception.WatsEBSCustomException;
import com.winfo.services.HealthCheck;
import com.winfo.vo.ResponseDto;
import com.winfo.vo.SanityCheckVO;
import com.winfo.vo.TestScriptDto;

@Controller
public class HealthCheckController {
	
	@Autowired
	HealthCheck healthCheck;
	
	@ResponseBody()
	@RequestMapping(value = "/sanityCheckForExecuteApi")
	public ResponseDto healthCheckStatus(@RequestBody Optional<TestScriptDto> testSetId) throws Exception{
		if(testSetId.isPresent() && !(testSetId.get().getTestScriptNo().isEmpty())) {
			return healthCheck.sanityCheckMethod(testSetId);
		}
		else {
			throw new WatsEBSCustomException(500, "Test set id can not be null or empty");
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/sanityCheckForAdminApi")
	public SanityCheckVO healthCheckStatusForAdmin(){
		return healthCheck.sanityCheckForAdminMethod();
	}
}
