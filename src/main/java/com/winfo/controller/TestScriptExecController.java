package com.winfo.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lowagie.text.DocumentException;
import com.winfo.services.TestScriptExecService;
import com.winfo.vo.ExecuteTestrunVo;
import com.winfo.vo.PyJabKafkaDto;
import com.winfo.vo.TestScriptDto;
import com.winfo.vo.UpdateScriptParamStatus;

@Controller
public class TestScriptExecController {

	@Autowired
	TestScriptExecService testScriptExecService;

	@ResponseBody
	@RequestMapping(value = "/executePyJabScript")
	public ExecuteTestrunVo executeTestScript(@Valid @RequestBody(required = false) TestScriptDto testScriptDto,
			BindingResult bindingResult) throws IOException, DocumentException, com.itextpdf.text.DocumentException {
		ExecuteTestrunVo status = null;
		if (testScriptDto != null && testScriptDto.getTestScriptNo() != null) {
			try {
				status = testScriptExecService.run(testScriptDto.getTestScriptNo());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}

		return status;
	}

	@ResponseBody
	@RequestMapping(value = "/updateStartScriptStatus")
	public void updateStartScriptStatus(@Valid @RequestBody(required = false) PyJabKafkaDto args,
			BindingResult bindingResult) throws ClassNotFoundException, SQLException {
		testScriptExecService.updateStartStatus(args);
	}

	@ResponseBody
	@RequestMapping(value = "/updateEndScriptStatus")
	public void updateEndScriptStatus(@Valid @RequestBody(required = false) PyJabKafkaDto args,
			BindingResult bindingResult) {
		testScriptExecService.generateTestScriptLineIdReports(args);
	}

	@ResponseBody
	@RequestMapping(value = "/updateScriptParamStatus")
	public void updateScriptParamStatus(UpdateScriptParamStatus args) throws ClassNotFoundException, SQLException {
		testScriptExecService.updateScriptParamStatus(args);
	}

}
