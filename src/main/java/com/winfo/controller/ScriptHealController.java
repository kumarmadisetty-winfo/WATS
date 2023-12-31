package com.winfo.controller;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.winfo.exception.WatsEBSException;
import com.winfo.repository.ScriptMasterRepository;
import com.winfo.serviceImpl.ScriptHealServiceImpl;
import com.winfo.vo.ScriptHealVo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@RestController
public class ScriptHealController {

	public static final Logger logger = Logger.getLogger(ScriptHealController.class);
	
	@Autowired
	ScriptMasterRepository scriptMasterRepository;
	@Autowired
	ScriptHealServiceImpl scriptHealServiceImpl;
	
	
	@ResponseBody
	@GetMapping(value = "/getNewInputParameter/{targetApplication}/{productVersion}/{module}")
	@ApiOperation( value="Get new label from new release note of target application",notes = "Script Id - Using for getting old input parameter from database")	
	@ApiResponses( value = { @ApiResponse( code=200,message="Succesfully found new Input Parameters")})
	public ResponseEntity<List<ScriptHealVo>> getUpdatedInputParameters(@PathVariable String targetApplication,@PathVariable String productVersion,@PathVariable String module) {
		try {
			List<ScriptHealVo> listOfOldNewInputParameters=scriptHealServiceImpl.getNewInputParameters(targetApplication,productVersion,module);
			return new ResponseEntity<List<ScriptHealVo>>(listOfOldNewInputParameters,HttpStatus.OK);
        } catch (IOException e) {
        	logger.error("Exception occurred while fetching the new Input Parameter");
        	throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occurred while fetching the new Input Parameter"); 
        }
	}

}
