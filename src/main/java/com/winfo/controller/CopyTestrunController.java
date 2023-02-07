package com.winfo.controller;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
	@ApiOperation(value = "Copy Test Run", notes = " <B> NewTestRunName:</B> Test run name should be pass to create a new test run,<br> "
			+ "<B>TestSetId:</B> TestsetId should be pass to copy current test run to new test run, <br> "
			+ "<B>IncrementValue:</B> This field is used to increment param value, if user want to increment value, pass value as 'Y' or else pass value as 'N', <br>"
			+ "<B>RequestType:</B> We have to pass request type as 'copyTestRun' to copy new testrun because we are checking condition if request type is equal to copyTestRun then it will copy test run")
	@ApiResponses( value = { @ApiResponse( code=200,message="Created new test run and returned testSetId " )})
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
	@ApiOperation( value="Add Scripts on the testrun ", notes = " <B>TestSetId:</B> TestsetId should be required because at which testrun should add new scripts , <br> "
			+ " <B>ListOfLineIds:</B> listOfLineIds should be pass, for respective LineId, scripts will be added, <br>"
			+ "<B>IncementalValue:</B> IncrementValue is refer to sequenceNumber. If we add new scripts to the testRun then sequenceNumber should be increment for each script ")
	@ApiResponses( value = { @ApiResponse( code=200,message="Added script successfully" )})
	public ResponseDto addScriptOnTestRun(@RequestBody InsertScriptsVO scriptVO) {
		log.info("Test Set Id *** " + scriptVO.getTestSetId());
		return service.addScriptsOnTestRun(scriptVO);
	}
}
