package com.winfo.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.winfo.service.TestRunValidationService;
import com.winfo.vo.ResponseDto;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin("*")
@RestController
public class TestRunValidationController {
	public static final Logger logger = Logger.getLogger(TestRunValidationController.class);

	@Autowired
	private TestRunValidationService testRunValisationService;

	@ResponseBody
	@PutMapping(value = "/validateTestRun/{testSetId}")
	@ApiOperation( value="Validate Test Run ",notes = "Test Run Id need to pass for validating a Test Run")
	@ApiResponses( value = { @ApiResponse( code=200,message="Test Run validated successfully")})
	public ResponseDto validateTestRun(@PathVariable Integer testSetId) throws Exception {
		return testRunValisationService.validateTestRun(testSetId);
	}

}
