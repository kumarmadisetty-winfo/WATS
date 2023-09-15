package com.winfo.controller;

import java.sql.SQLException;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.winfo.exception.WatsEBSException;
import com.winfo.serviceImpl.TestScriptExecService;
import com.winfo.utils.Constants.AUDIT_TRAIL_STAGES;
import com.winfo.vo.MessageQueueDto;
import com.winfo.vo.ResponseDto;
import com.winfo.vo.UpdateScriptParamStatus;
import com.winfo.vo.UpdateScriptStepStatus;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller
public class TestScriptExecController {

	@Autowired
	TestScriptExecService testScriptExecService;

	public static final Logger logger = Logger.getLogger(TestScriptExecController.class);
	
//	@ResponseBody
//	@RequestMapping(value = "/executeTestScript")
//	public ResponseDto executeTestScript(@Valid @RequestBody(required = false) TestScriptDto testScriptDto,
//			BindingResult bindingResult) throws IOException, DocumentException, com.itextpdf.text.DocumentException {
//		ResponseDto status = null;
//		if (testScriptDto != null && testScriptDto.getTestScriptNo() != null) {
////			status = testScriptExecService.run(testScriptDto.getTestScriptNo());
//		}
//
//		return status;
//	}

	@ResponseBody
	@PostMapping(value = "/updateStartScriptStatus")
	@ApiOperation( value="Update Start Script Status",notes = "To Update  StartScript Status, we should pass <B>testSetId, testSetLineId , manualTrigger: true and startDate</B><br>"
			+ "<B>ManualTrigger</B>It is by default set to true")	
	@ApiResponses( value = { @ApiResponse( code=200,message="Updated StartScript Status Succesfully")})
	public void updateStartScriptStatus(@Valid @RequestBody MessageQueueDto args, BindingResult bindingResult)
			throws ClassNotFoundException, SQLException {
		testScriptExecService.updateStartStatus(args);
	}

	@ResponseBody
	@PostMapping(value = "/updateEndScriptStatus")
	@ApiOperation( value="Update End Script Status",notes = "To Update  EndScript Status, we should pass <B>testSetId, testSetLineId , manualTrigger: true and startDate</B><br>"
			+ "<B>ManualTrigger</B>It is by default set to true")	
	@ApiResponses( value = { @ApiResponse( code=200,message="Updated EndScript Status Succesfully")})
	public void updateEndScriptStatus(@Valid @RequestBody MessageQueueDto msgQueueDto, BindingResult bindingResult)
			throws Exception {
		testScriptExecService.generateTestScriptLineIdReports(msgQueueDto);
	}

	@ResponseBody
	@GetMapping(value = "/generateScriptPdf")
	@ApiOperation( value="Generate Script PDF",notes = "To generate Script pdf, we should pass <B>testSetId, testSetLineId and manualTrigger: true</B><br>"
					+ "<B>ManualTrigger</B>It is by default set to true")	
	@ApiResponses( value = { @ApiResponse( code=200,message="Generated TestRunPdfs Succesfully")})
	public ResponseDto updateEndScriptStatus2(@Valid @RequestBody MessageQueueDto args, BindingResult bindingResult)
			throws Exception {
		return testScriptExecService.generateTestScriptLineIdReports(args);
	}

	@KafkaListener(topics = "#{'${kafka.topic.name.wats.not.reachable}'.split(',')}", groupId = "wats-group")
	public void updateStatusForTestRunWhenWatsIsNotReachable(MessageQueueDto event) throws Exception {
		testScriptExecService.generateTestScriptLineIdReports(event);
		event.setStage(AUDIT_TRAIL_STAGES.SU);
		testScriptExecService.updateAuditLogs(event);
	}

	@ResponseBody
	@PostMapping(value = "/updateScriptParamStatus")
	@ApiOperation( value="Update ScriptParam Status",notes = "To update Scriptparam status, we should pass <B>ScriptParamId, Success</B><br>"
			+ "<B>Success:</B> Success is a boolean type.If success is true, script status is passed else failed")	
	@ApiResponses( value = { @ApiResponse( code=200,message="Updated ScriptParam Status")})
	public void updateScriptParamStatus(@Valid @RequestBody UpdateScriptParamStatus args)
			throws ClassNotFoundException, SQLException {
		testScriptExecService.updateScriptParamStatus(args);
	}

	@ResponseBody
	@PostMapping(value = "/updateScriptStepStatus")
	@ApiOperation( value="Update ScriptStep Status",notes = "To update ScriptStep status, we should pass <B>ScriptParamId, Status</B><br>"
			+ "<B>Status:</B> Status is to update the status of the script")	
	@ApiResponses( value = { @ApiResponse( code=200,message="Updated ScriptStep Status")})
	public void updateScriptStepStatus(@Valid @RequestBody UpdateScriptStepStatus args)
			throws ClassNotFoundException, SQLException {
		testScriptExecService.updateScriptStepStatus(args);
	}

	@ResponseBody
	@GetMapping(value = "/getTestSetMode/{testSetId}")
	@ApiOperation( value="Get Copied Value",notes = "We should pass copyPath to get copied value present in the testrun input value")	
	@ApiResponses( value = { @ApiResponse( code=200,message="Copied Succesfully")})
	public String getTestSetMode(@PathVariable Long testSetId) {
		return testScriptExecService.getTestSetMode(testSetId);
	}

	@ResponseBody
	@PostMapping(value = "/getCopiedValue/{copyPath}")
	@ApiOperation( value="Get Copied Value",notes = "We should pass copyPath to get copied value in the test run input value")	
	@ApiResponses( value = { @ApiResponse( code=200,message="Copied Succesfully")})
	public String getTestSetMode(@PathVariable String copyPath) {
		return testScriptExecService.getCopiedValue(copyPath);
	}

	@ResponseBody
	@GetMapping(value = "/generateTestRunPdfs/{testSetId}")
	@ApiOperation( value="Generate Test Run PDF",notes = "To generate TestRun pdf(Passed, Failed and Detailed), we should pass testSetId")	
	@ApiResponses( value = { @ApiResponse( code=200,message="Generated TestRunPdfs Succesfully")})
	public ResponseDto generateTestRunPdfs(@PathVariable String testSetId) {
		try {
			return testScriptExecService.generateTestRunPdf(testSetId);			
		}
		catch(Exception e) {
			logger.error("Exception occurred while generating PDFs");
        	throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occurred while generating PDFs"); 
		}
	}
	
	@ResponseBody
	@PostMapping(value = "/excelRunStatusUpdation/{testsetlineid}")
	@ApiOperation( value="ExcelRun Status Updation",notes = "To update ExcelRun Status Updation, we should pass testsetlineid")	
	@ApiResponses( value = { @ApiResponse( code=200,message="Excel updated Succesfully")})
	public ResponseDto excelStatus(@PathVariable("testsetlineid") Integer testsetlineid) {
		return testScriptExecService.excelStatusCheck(testsetlineid);
	}

}
