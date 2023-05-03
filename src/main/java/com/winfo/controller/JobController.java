package com.winfo.controller;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.winfo.config.MessageUtil;
import com.winfo.scripts.RunAutomation;
import com.winfo.services.HealthCheck;
import com.winfo.vo.ResponseDto;
import com.winfo.vo.TestScriptDto;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin("*")
@RestController
public class JobController {

	public static final Logger logger = Logger.getLogger(JobController.class);

	@Autowired
	RunAutomation runAutomation;
	
	@Autowired
	private MessageUtil messageUtil;

	@Autowired
	HealthCheck healthCheck;

	@ResponseBody
	@PostMapping(value = "/executeTestScript")
	@ApiOperation( value="Test Script Execution ",notes = " <B>TestScriptNo:</B> TestsetId is to pass to start the script execution")
	@ApiResponses( value = { @ApiResponse( code=200,message="Script execution completed")})
	public ResponseDto executeTestScript(@Valid @RequestBody TestScriptDto testScriptDto, BindingResult bindingResult)
			throws Exception {

		ResponseDto status = null;
		if (testScriptDto != null && testScriptDto.getTestScriptNo() != null) {
			logger.info("Start of Test Script Run # : " + testScriptDto.getTestScriptNo());
			status = healthCheck.sanityCheckMethod(testScriptDto.getTestScriptNo());
			runAutomation.run(testScriptDto.getTestScriptNo());

		}
		return status;
	}

}
