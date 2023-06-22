package com.winfo.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.winfo.repository.ScriptMasterRepository;
import com.winfo.serviceImpl.ScriptHealService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@Controller
public class ScriptHealController {

	@Autowired
	ScriptMasterRepository scriptMasterRepository;
	@Autowired
	ScriptHealService scriptHealService;
	
	
	@ResponseBody
	@GetMapping(value = "/getNewInputParameter/{scriptId}")
	@ApiOperation( value="Get new label from new release note of target application",notes = "Script Id - Using for getting old input parameter from database")	
	@ApiResponses( value = { @ApiResponse( code=200,message="Succesfully found new Input Parameters")})
	public Map<Integer, Map<String,String>> readPdf(@PathVariable Integer scriptId) {
		try {
			return scriptHealService.getNewInputParameters(scriptId);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error");
            return null;  
        }
	}

}
