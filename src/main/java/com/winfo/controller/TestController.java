package com.winfo.controller;

import java.io.File;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

	@ResponseBody
	@RequestMapping(value = "/test")
	public void test() {
		System.out.println(File.pathSeparator+"Char");
	}
	
}
