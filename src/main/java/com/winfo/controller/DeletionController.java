package com.winfo.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.winfo.serviceImpl.DeletionService;
import com.winfo.vo.DeleteEvidenceReportDto;
import com.winfo.vo.ResponseDto;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class DeletionController {

	@Autowired
	DeletionService deletionOfScriptService;

	@ResponseBody
	@PostMapping(value = "/deleteScriptEvidenceReportData")
	@ApiOperation( value="Delete Script and Evidence Report in the TestRun ",notes = "<B> TestSetId and testSetLineId </B>is to pass to delete the script and Evidence Report(Screenshot and pdf in object Storage which are present in the test run<br>")
	@ApiResponses( value = { @ApiResponse( code=200,message="Script Evidence Report deleted successfully")})
	public ResponseDto deleteScriptFromTestRun(@Valid @RequestBody DeleteEvidenceReportDto testScriptDto,
			BindingResult bindingResult) throws Exception {

		return deletionOfScriptService.deleteScriptFromTestRun(testScriptDto);
	}

	@ResponseBody
	@PostMapping(value = "/deleteTestRunEvidenceReportData")
	@ApiOperation( value="Delete TestRun and EvidenceReportData",notes = "<B> TestSetId and testSetLineId </B>is to pass to delete the script and Evidence Report(Screenshot and pdf in object Storage which are present in the test run, <br>"
			+ "<B>IsTestRunDelete:</B> If we pass isTestRunDelete as 'true', it will delete testrun")
	@ApiResponses( value = { @ApiResponse( code=200,message="Testrun and Evidence report deleted successfully")})
	public ResponseDto deleteAllScriptFromTestRun(@Valid @RequestBody DeleteEvidenceReportDto testScriptDto,
			BindingResult bindingResult) throws Exception {

		return deletionOfScriptService.deleteAllScriptFromTestRun(testScriptDto);
	}

}
