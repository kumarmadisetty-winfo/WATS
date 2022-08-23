package com.winfo.controller;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.winfo.scripts.RunAutomation;
import com.winfo.services.HealthCheck;
import com.winfo.vo.ResponseDto;
import com.winfo.vo.TestScriptDto;

@CrossOrigin("*")
@RestController
public class JobController {

	public final Logger logger = LogManager.getLogger(JobController.class);

	/*
	 * @Autowired RunAutomation runAutomation;
	 */
	@Autowired
	RunAutomation runAutomation;

	@Autowired
	HealthCheck healthCheck;

	@ResponseBody
	@RequestMapping(value = "/executeTestScript")
	public ResponseDto executeTestScript(@Valid @RequestBody TestScriptDto testScriptDto, BindingResult bindingResult)
			throws Exception {

		logger.info("TestRunId ***" + testScriptDto.getTestScriptNo());
		ResponseDto status = null;
		if (testScriptDto != null && testScriptDto.getTestScriptNo() != null) {
			logger.info("Start of Test Script Run # : " + testScriptDto.getTestScriptNo());
			status = healthCheck.sanityCheckMethod(testScriptDto.getTestScriptNo());
			runAutomation.run(testScriptDto.getTestScriptNo());

		}
		logger.info("End of Test Script Run # : " + testScriptDto.getTestScriptNo());
		return status;
	}
}
