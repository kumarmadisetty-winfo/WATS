package com.winfo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.winfo.serviceImpl.HealthCheck;
import com.winfo.vo.SanityCheckVO;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller
public class HealthCheckController {

	@Autowired
	HealthCheck healthCheck;

	@ResponseBody
	@GetMapping(value = "/sanityCheckForAdmin")
	@ApiOperation( value="Sanity Check For Admin ",notes = "Sanity is used for checking status of database,seleniumGrid,objectStoreAccess and centralRepo")
	public SanityCheckVO healthCheckStatusForAdmin() {
		return healthCheck.sanityCheckForAdminMethod();
	}
}
