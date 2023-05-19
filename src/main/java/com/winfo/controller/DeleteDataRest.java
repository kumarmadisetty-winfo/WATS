package com.winfo.controller;

import java.util.List;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.winfo.vo.DomGenericResponseBean;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import com.winfo.serviceImpl.DeleteDataService;
import com.winfo.vo.DeleteScriptsData;


@RestController
public class DeleteDataRest {
	@Autowired
	DeleteDataService service;

	@PostMapping("/delete_data")
	@ApiOperation( value="Delete Script from Library",notes = " <B>ScriptId:</B> ScriptId should be pass, it will identify particular scriptId and it will delete the script,<br>"
			+ "ProductVersion:</B> ProductVersion must be pass because if one script contains multiple product version, with the help of product version we will delete the script,<br>"
			+ "<B>DeleteAll:</B> If we pass deleteAll as 'true', it will delete all the scripts.")
	@ApiResponses( value = { @ApiResponse( code=200,message="Successfully deleted the scripts")})
	public  List<DomGenericResponseBean> deleteData(@RequestBody DeleteScriptsData deletescriptsdata) throws ParseException {
		return service.deleteData(deletescriptsdata);
		
	}
}



