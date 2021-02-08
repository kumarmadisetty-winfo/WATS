package com.winfo.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.winfo.services.CopyTestRunService;
import com.winfo.vo.CopyTestrunjson;
import com.winfo.vo.CopytestrunVo;
import com.winfo.vo.DomGenericResponseBean;
import com.winfo.vo.TestScriptDto;


@RestController
public class CopyTestrunController {
	@Autowired
	CopyTestRunService service;
//	@PostMapping("/copyTestrun")
	@RequestMapping(value = "copyTestrun", method = RequestMethod.POST,
    produces = MediaType.APPLICATION_JSON_VALUE)
	public CopyTestrunjson copyTestrun(@Valid @RequestBody(required = false) CopytestrunVo copyTestrunvo,
		BindingResult bindingResult) throws InterruptedException {
		System.out.println(copyTestrunvo.getCreation_date());
		int newtestrun= service.copyTestrun(copyTestrunvo);
		CopyTestrunjson jsondata=new CopyTestrunjson();
		System.out.println("newtestrun"+newtestrun);
		jsondata.setNew_test_run_id(newtestrun);
		return jsondata;
	}
}
