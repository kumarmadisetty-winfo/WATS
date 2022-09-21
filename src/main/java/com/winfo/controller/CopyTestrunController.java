package com.winfo.controller;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.winfo.services.CopyTestRunService;
import com.winfo.vo.CopyTestrunjson;
import com.winfo.vo.CopytestrunVo;
import com.winfo.vo.InsertScriptsVO;
import com.winfo.vo.ResponseDto;

@RestController
public class CopyTestrunController {
	Logger log = Logger.getLogger("Logger");

	@Autowired
	CopyTestRunService service;

	@PostMapping(value = "copyTestrun", produces = MediaType.APPLICATION_JSON_VALUE)
	public CopyTestrunjson copyTestrun(@Valid @RequestBody(required = false) CopytestrunVo copyTestrunvo,
			BindingResult bindingResult) throws InterruptedException, JsonMappingException, JsonProcessingException {
		log.info("Test Run Name**" + copyTestrunvo.getNewtestrunname());
		log.info("copyTestrunvo.getCreation_date()" + copyTestrunvo.getCreationDate());
		int newtestrun = 0;
		if (copyTestrunvo.getRequestType().equalsIgnoreCase("copyTestRun")) {
			newtestrun = service.copyTestrun(copyTestrunvo);
		} else if (copyTestrunvo.getRequestType().equalsIgnoreCase("reRun")) {
			newtestrun = service.reRun(copyTestrunvo);
		}
		CopyTestrunjson jsondata = new CopyTestrunjson();
		log.info("newtestrun" + newtestrun);
		jsondata.setNewTestRunId(newtestrun);
		jsondata.setStatusMessage("SUCCESS");
		log.info(jsondata.toString());
		return jsondata;
	}

	@ResponseBody
	@RequestMapping(value = "/addScriptsOnTestRun")
	public ResponseDto addScriptOnTestRun(@RequestBody InsertScriptsVO scriptVO) {
		log.info("Test Set Id *** " + scriptVO.getTestSetId());
		return service.addScriptsOnTestRun(scriptVO);
	}
}
