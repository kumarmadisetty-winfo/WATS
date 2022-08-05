package com.winfo.controller;

import java.sql.SQLException;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.winfo.services.TestScriptExecService;
import com.winfo.utils.Constants.AUDIT_TRAIL_STAGES;
import com.winfo.vo.MessageQueueDto;
import com.winfo.vo.ResponseDto;
import com.winfo.vo.UpdateScriptParamStatus;
import com.winfo.vo.UpdateScriptStepStatus;

@Controller
public class TestScriptExecController {

	public final Logger logger = LogManager.getLogger(TestScriptExecController.class);

	@Autowired
	TestScriptExecService testScriptExecService;

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
	@RequestMapping(value = "/updateStartScriptStatus")
	public void updateStartScriptStatus(@Valid @RequestBody MessageQueueDto msgQueueDto, BindingResult bindingResult)
			throws ClassNotFoundException, SQLException {
		logger.info("TestRunId **" + msgQueueDto.getTestSetId());
		testScriptExecService.updateStartStatus(msgQueueDto);
	}

	@ResponseBody
	@RequestMapping(value = "/updateEndScriptStatus")
	public void updateEndScriptStatus(@Valid @RequestBody MessageQueueDto msgQueueDto, BindingResult bindingResult)
			throws Exception {
		logger.info("TestRunId **" + msgQueueDto.getTestSetId());
		testScriptExecService.generateTestScriptLineIdReports(msgQueueDto);
	}

	@ResponseBody
	@RequestMapping(value = "/generateScriptPdf")
	public ResponseDto updateEndScriptStatus2(@Valid @RequestBody MessageQueueDto msgQueueDto,
			BindingResult bindingResult) throws Exception {
		logger.info("TestSetId**" + msgQueueDto.getTestSetId());
		return testScriptExecService.generateTestScriptLineIdReports(msgQueueDto);
	}

	@KafkaListener(topics = "#{'${kafka.topic.name.wats.not.reachable}'.split(',')}", groupId = "wats-group")
	public void updateStatusForTestRunWhenWatsIsNotReachable(MessageQueueDto event) throws Exception {
		testScriptExecService.generateTestScriptLineIdReports(event);
		event.setStage(AUDIT_TRAIL_STAGES.SU);
		testScriptExecService.updateAuditLogs(event);
	}

	@ResponseBody
	@RequestMapping(value = "/updateScriptParamStatus")
	public void updateScriptParamStatus(@Valid @RequestBody UpdateScriptParamStatus args)
			throws ClassNotFoundException, SQLException {
		testScriptExecService.updateScriptParamStatus(args);
	}

	@ResponseBody
	@RequestMapping(value = "/updateScriptStepStatus")
	public void updateScriptStepStatus(@Valid @RequestBody UpdateScriptStepStatus args)
			throws ClassNotFoundException, SQLException {
		testScriptExecService.updateScriptStepStatus(args);
	}

	@ResponseBody
	@RequestMapping(value = "/getTestSetMode/{testSetId}")
	public String getTestSetMode(@PathVariable Long testSetId) {
		return testScriptExecService.getTestSetMode(testSetId);
	}

	@ResponseBody
	@RequestMapping(value = "/getCopiedValue/{copyPath}")
	public String getTestSetMode(@PathVariable String copyPath) {
		return testScriptExecService.getCopiedValue(copyPath);
	}

	@ResponseBody
	@RequestMapping(value = "/generateTestRunPdfs/{testSetId}")
	public ResponseDto generateTestRunPdfs(@PathVariable String testSetId) {
		logger.info("TestSetId**" + testSetId);
		return testScriptExecService.generateTestRunPdf(testSetId);
	}

}
