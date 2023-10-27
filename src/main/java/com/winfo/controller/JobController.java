package com.winfo.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.winfo.exception.WatsEBSException;
import com.winfo.scripts.RunAutomation;
import com.winfo.serviceImpl.HealthCheck;
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
	HealthCheck healthCheck;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ResponseBody
	@PostMapping(value = {"/executeTestScript","/executeTestScript/{executedFrom}"})
	@ApiOperation(value = "Test Script Execution ", notes = " <B>TestScriptNo:</B> TestsetId is to pass to start the script execution")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Script execution completed"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Internal server error") })
	public ResponseEntity executeTestScript(@Valid @RequestBody TestScriptDto testScriptDto,@PathVariable Optional<String> executedFrom,
			BindingResult bindingResult) throws Exception {

		if (testScriptDto != null && testScriptDto.getTestScriptNo() != null && testScriptDto.getExecutedBy() != null) {
			logger.info(String.format("Test Script Run ID : %s ",  testScriptDto.getTestScriptNo()));
			ResponseDto responseDto = healthCheck.sanityCheckMethod(testScriptDto.getTestScriptNo());
			if (responseDto.getStatusCode() == HttpStatus.OK.value()) {
				runAutomation.run(testScriptDto,executedFrom);
			} else {
				return new ResponseEntity(
						new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Sanity check fail"),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return ResponseEntity.ok(responseDto);
		} else {
			return new ResponseEntity(
					new WatsEBSException(HttpStatus.BAD_REQUEST.value(), "Enter valid test set id"),
					HttpStatus.BAD_REQUEST);
		}

	}

}
