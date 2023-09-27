package com.winfo.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.winfo.service.ValidationService;
import com.winfo.vo.ResponseDto;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin("*")
@RestController
public class ValidationController {
	public static final Logger logger = Logger.getLogger(ValidationController.class);

	@Autowired
	private ValidationService testRunValisationService;

	@PutMapping(value = "/validateTestRun/{testSetId}/{validateAll}")
	@ApiOperation( value="Validate Test Run ",notes = "Test Run Id need to pass for validating a Test Run")
	@ApiResponses( value = { @ApiResponse( code=200,message="Test Run validated successfully")})
	public ResponseEntity<ResponseDto> validateTestRun(@PathVariable Integer testSetId, @PathVariable boolean validateAll) throws Exception {
		return testRunValisationService.validateTestRun(testSetId,validateAll);
	}
	
	@PutMapping(value = "/validateSchedule/{jobId}")
	@ApiOperation( value="Validate Schedule",notes = "Schedule Id need to pass for validating a Test Run")
	@ApiResponses( value = { @ApiResponse( code=200,message="Schedule validated successfully")})
	public ResponseEntity<ResponseDto> validateSchedule(@PathVariable Integer jobId) throws Exception {
		return testRunValisationService.validateSchedule(jobId);
	}
	
	@PutMapping(value = "/validateScript/{testSetId}/{testSetLineId}")
	@ApiOperation( value="Validate Script",notes = "Test Set Line Id need to pass for validating a Test Run")
	@ApiResponses( value = { @ApiResponse( code=200,message="Script validated successfully")})
	public ResponseEntity<ResponseDto> validateSript(@PathVariable Integer testSetId,@PathVariable Integer testSetLineId) throws Exception {
		return testRunValisationService.validateTestRunScript(testSetId,testSetLineId);
	}


}
