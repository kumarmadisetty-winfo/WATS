package com.winfo.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.winfo.exception.WatsEBSCustomException;
import com.winfo.repository.ScriptMasterRepository;
import com.winfo.serviceImpl.ScriptHealService;
import com.winfo.vo.ScriptHealVo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@Controller
public class ScriptHealController {

	public static final Logger logger = Logger.getLogger(ScriptHealController.class);
	
	@Autowired
	ScriptMasterRepository scriptMasterRepository;
	@Autowired
	ScriptHealService scriptHealService;
	
	
	@ResponseBody
	@GetMapping(value = "/getNewInputParameter/{targetApplication}/{productVersion}/{module}")
	@ApiOperation( value="Get new label from new release note of target application",notes = "Script Id - Using for getting old input parameter from database")	
	@ApiResponses( value = { @ApiResponse( code=200,message="Succesfully found new Input Parameters")})
	public List<ScriptHealVo> readPdf(@PathVariable String targetApplication,@PathVariable String productVersion,@PathVariable String module) {
		try {
			return scriptHealService.getNewInputParameters(targetApplication,productVersion,module);
        } catch (IOException e) {
        	logger.error("Exception occured while fetching the new Input Parameter");
        	throw new WatsEBSCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occured while fetching the new Input Parameter"); 
        }
	}

}
