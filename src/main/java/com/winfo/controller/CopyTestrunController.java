package com.winfo.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.winfo.services.WatsPluginService;
import com.winfo.vo.DomGenericResponseBean;
import com.winfo.vo.TestScriptDto;
import com.winfo.vo.WatsMasterVO;

@RestController
public class CopyTestrunController {
	@Autowired
	WatsPluginService service;
	@PostMapping("/copyTestrun")
	public DomGenericResponseBean copyTestrun(@Valid @RequestBody(required = false) TestScriptDto testScriptDto,
			BindingResult bindingResult) {
		return service.copyTestrun(testScriptDto.getTestScriptNo());
		
	}
}
