package com.winfo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.winfo.services.HealthCheck;
import com.winfo.vo.SanityCheckVO;

@Controller
public class HealthCheckController {

	@Autowired
	HealthCheck healthCheck;

	@ResponseBody
	@RequestMapping(value = "/sanityCheckForAdminApi")
	public SanityCheckVO healthCheckStatusForAdmin() {
		return healthCheck.sanityCheckForAdminMethod();
	}
}
