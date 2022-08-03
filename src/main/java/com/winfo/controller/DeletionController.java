package com.winfo.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.winfo.services.DeletionService;
import com.winfo.vo.DeleteEvidenceReportDto;
import com.winfo.vo.ResponseDto;

@RestController
public class DeletionController {

	@Autowired
	DeletionService deletionOfScriptService;

	@ResponseBody
	@RequestMapping(value = "/deleteEvidenceReportData")
	public ResponseDto deleteScriptFromTestRun(@Valid @RequestBody DeleteEvidenceReportDto testScriptDto,
			BindingResult bindingResult) throws Exception {

		return deletionOfScriptService.deleteScriptFromTestRun(testScriptDto);
	}

	@ResponseBody
	@RequestMapping(value = "/deleteTestRunEvidenceReportData")
	public ResponseDto deleteAllScriptFromTestRun(@Valid @RequestBody DeleteEvidenceReportDto testScriptDto,
			BindingResult bindingResult) throws Exception {

		return deletionOfScriptService.deleteAllScriptFromTestRun(testScriptDto);
	}

}
