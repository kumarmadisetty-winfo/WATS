package com.winfo.controller;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.winfo.services.CopyTestRunService;
import com.winfo.vo.CopyTestrunjson;
import com.winfo.vo.CopytestrunVo;

@RestController
public class CopyTestrunController {
	Logger log = Logger.getLogger("Logger");

	@Autowired
	CopyTestRunService service;

	@RequestMapping(value = "copyTestrun", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public CopyTestrunjson copyTestrun(@Valid @RequestBody(required = false) CopytestrunVo copyTestrunvo,
			BindingResult bindingResult) throws InterruptedException {
		log.info("copyTestrunvo.getCreation_date()" + copyTestrunvo.getCreationDate());
		int newtestrun = 0;
		if (copyTestrunvo.getRequesttype().equalsIgnoreCase("copyTestRun")) {
			newtestrun = service.copyTestrun(copyTestrunvo);
		} else if (copyTestrunvo.getRequesttype().equalsIgnoreCase("reRun")) {
			newtestrun = service.reRun(copyTestrunvo);
		}
		CopyTestrunjson jsondata = new CopyTestrunjson();
		log.info("newtestrun" + newtestrun);
		jsondata.setNew_test_run_id(newtestrun);
		jsondata.setStatusMessage("SUCCESS");
		log.info(jsondata.toString());
		return jsondata;
	}
}
