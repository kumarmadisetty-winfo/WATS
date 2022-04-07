package com.winfo.controller;

import java.io.IOException;
import java.net.MalformedURLException;

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
import com.winfo.vo.TestScriptDto;


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

}
