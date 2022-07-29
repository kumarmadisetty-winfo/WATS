package com.winfo.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.winfo.services.DeletionOfScriptService;
import com.winfo.vo.DeleteScriptDto;
import com.winfo.vo.ResponseDto;

@RestController
public class DeletionOfScriptController {

	@Autowired
	DeletionOfScriptService deletionOfScriptService;

	@ResponseBody
	@RequestMapping(value = "/deleteScriptFromTestRun")
	public ResponseDto deleteScriptFromTestRun(@Valid @RequestBody DeleteScriptDto testScriptDto,
			BindingResult bindingResult) throws Exception {

		return deletionOfScriptService.deleteScriptFromTestRun(testScriptDto);
	}

}
