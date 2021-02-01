package com.winfo.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.winfo.services.CopyTestRunService;
import com.winfo.vo.CopytestrunVo;
import com.winfo.vo.DomGenericResponseBean;
import com.winfo.vo.TestScriptDto;
import com.winfo.vo.WatsMasterVO;

@RestController
public class CopyTestrunController {
	@Autowired
	CopyTestRunService service;
	@PostMapping("/copyTestrun")
	public DomGenericResponseBean copyTestrun(@Valid @RequestBody(required = false) CopytestrunVo copyTestrunvo,
			BindingResult bindingResult) throws InterruptedException {
		System.out.println(copyTestrunvo.getTestScriptNo());
		return service.copyTestrun(copyTestrunvo);
		
	}
}
