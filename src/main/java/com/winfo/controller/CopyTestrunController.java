package com.winfo.controller;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.winfo.services.CopyTestRunService;
import com.winfo.vo.CopyTestrunjson;
import com.winfo.vo.CopytestrunVo;
import com.winfo.vo.InsertScriptsVO;
import com.winfo.vo.ResponseDto;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class CopyTestrunController {
	Logger log = Logger.getLogger("Logger");

	@Autowired
	CopyTestRunService service;

	@PostMapping(value = "copyTestrun", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Copy Test Run", notes = " <B> NewTestRunName:</B> Test run name is a required parameter when creating a new test run<br> "
			+ "<B>TestSetId:</B> TestsetId should be pass to identify the current test run to copy new test run<br> "
			+ "<B>IncrementValue:</B> This field is used to increment param value, if user want to increment value, pass value as 'Y' or else pass value as 'N'<br>"
			+ "<B>RequestType:</B> the request type must be set to 'copyTestRun' in order to copy a new test run. The code checks if the request type is equal to 'copyTestRun' and if so, it will proceed to copy the test run.")
	@ApiResponses( value = { @ApiResponse( code=200,message="Created new test run and returned testSetId")})
	public CopyTestrunjson copyTestrun(@Valid @RequestBody(required = false) CopytestrunVo copyTestrunvo,
			BindingResult bindingResult) throws InterruptedException, JsonProcessingException {
		log.info("Test Run Name**" + copyTestrunvo.getNewtestrunname());
		log.info("copyTestrunvo.getCreation_date()" + copyTestrunvo.getCreationDate());
		int newtestrun = 0;
		if (copyTestrunvo.getRequestType().equalsIgnoreCase("copyTestRun")) {
			newtestrun = service.copyTestrun(copyTestrunvo);
		} else if (copyTestrunvo.getRequestType().equalsIgnoreCase("reRun")) {
			newtestrun = service.reRun(copyTestrunvo);
		}
		CopyTestrunjson jsondata = new CopyTestrunjson();
		log.info("newtestrun" + newtestrun);
		jsondata.setNewTestRunId(newtestrun);
		jsondata.setStatusMessage("SUCCESS");
		log.info(jsondata.toString());
		return jsondata;
	}

	@ResponseBody
	@PostMapping(value = "/addScriptsOnTestRun")
	@ApiOperation( value="Add Scripts on the testrun ", notes = " <B>TestSetId:</B> The TestSetId is a unique identifier used to identify and organize a specific set of test scripts in a testing environment. It is typically required in order to keep track of which testrun the new scripts should be added<br> "
			+ " <B>ListOfLineIds:</B> listOfLineIds should be pass, for respective LineId, scripts will be added <br>"
			+ "<B>IncementalValue:</B> IncrementValue is refer to sequenceNumber. If we add new scripts to the testRun then sequenceNumber should be increment for each script ")
	@ApiResponses( value = { @ApiResponse( code=200,message="Added script successfully" )})
	public ResponseDto addScriptOnTestRun(@RequestBody InsertScriptsVO scriptVO) {
		log.info("Test Set Id *** " + scriptVO.getTestSetId());
		return service.addScriptsOnTestRun(scriptVO);
	}
}
