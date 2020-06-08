package com.winfo.controller;

import java.net.MalformedURLException;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.winfo.scripts.RunAutomation;
import com.winfo.vo.TestScriptDto;

@CrossOrigin("*")
@RestController
public class JobController {
	private static final Logger logger = Logger.getLogger(JobController.class);
	
	/*
	 * @Autowired RunAutomation runAutomation;
	 */
	@Autowired
	RunAutomation runAutomation;
	
	@ResponseBody
	@RequestMapping(value = "/executeTestScript")
	public String executeTestScript(@Valid @RequestBody(required = false) TestScriptDto testScriptDto,
			BindingResult bindingResult) {

		if(testScriptDto !=null && testScriptDto.getTestScriptNo() != null) {
			System.out.println("Parameter test script # : "+testScriptDto.getTestScriptNo());
			try {
				runAutomation.run(testScriptDto.getTestScriptNo());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
//		runAutomation.run(parameters.getTestScriptNo());
		System.out.println("Test script # : ");

		return "{status : 20}";
	}
}
