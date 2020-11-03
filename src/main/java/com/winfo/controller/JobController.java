package com.winfo.controller;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.validation.Valid;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.lowagie.text.DocumentException;
import com.winfo.scripts.RunAutomation;
import com.winfo.vo.TestScriptDto;

@CrossOrigin("*")
@RestController
public class JobController {
	
	
	/*
	 * @Autowired RunAutomation runAutomation;
	 */
	@Autowired
	RunAutomation runAutomation;
	
	@ResponseBody
	@RequestMapping(value = "/executeTestScript")
	public String executeTestScript(@Valid @RequestBody(required = false) TestScriptDto testScriptDto,
			BindingResult bindingResult) throws IOException, DocumentException, com.itextpdf.text.DocumentException {

		if(testScriptDto !=null && testScriptDto.getTestScriptNo() != null) {
			System.out.println("Parameter test script # : "+testScriptDto.getTestScriptNo());
			try {
//				runAutomation.run(testScriptDto.getTestScriptNo());
				runAutomation.report();
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
