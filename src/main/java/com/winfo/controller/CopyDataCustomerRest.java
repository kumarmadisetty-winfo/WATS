package com.winfo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.winfo.services.CopyDataCustomerService;
import com.winfo.vo.CopyDataDetails;
import com.winfo.vo.DomGenericResponseBean;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class CopyDataCustomerRest {
	@Autowired
	CopyDataCustomerService service;

	@PostMapping("/copyScriptUsingProductVersion")
	@ApiOperation( value= "Copy Scripts Using ProductVersion ",notes = "<B>productVersionOld:</B> We have to pass current script version to copy data, <br>"
			+ "<B>productVersionNew:</B>  We have to pass new production version that will copy data from current product version to new product version")
	@ApiResponses( value = { @ApiResponse( code=200,message="Successfully copied scripts from one product version to another version")})
	public DomGenericResponseBean copyData(@RequestBody CopyDataDetails copyDataDetails) {
		return service.copyData(copyDataDetails);

	}
}
