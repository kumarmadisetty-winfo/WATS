package com.winfo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.winfo.services.CentralRepoStatusCheckService;
import com.winfo.vo.ResponseDto;

@Controller
public class CentralRepoAccessCheckController {
	
	@Autowired
	CentralRepoStatusCheckService centralRepoStatusCheckService;
	
	@ResponseBody
	@RequestMapping(value = "/centralRepoAccessCheckApi")
	public ResponseDto centralRepoStatus(){
		return centralRepoStatusCheckService.centralRepoStatus();
	}
}
