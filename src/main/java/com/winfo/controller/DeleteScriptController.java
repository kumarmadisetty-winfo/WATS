package com.winfo.controller;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.winfo.vo.ResponseDto;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import com.winfo.exception.WatsEBSException;
import com.winfo.serviceImpl.DeleteScriptsServiceImpl;
import com.winfo.vo.DeleteScriptsVO;


@RestController
public class DeleteScriptController {
	@Autowired
	DeleteScriptsServiceImpl deleteDataServiceImpl;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@DeleteMapping("/deleteScriptsFromLibrary")
	@ApiOperation( value="Delete Script from Library",notes = " <B>ScriptId:</B> ScriptId should be pass, it will identify particular scriptId and it will delete the script,<br>"
			+ "ProductVersion:</B> ProductVersion must be pass because if one script contains multiple product version, with the help of product version we will delete the script,<br>"
			+ "<B>DeleteAll:</B> If we pass deleteAll as 'true', it will delete all the scripts.")
	@ApiResponses( value = { @ApiResponse( code=200,message="Successfully deleted the scripts")})
	public  ResponseEntity deleteScriptFromScriptLibrary(@RequestBody DeleteScriptsVO deleteScriptsVO) throws ParseException {
		ResponseDto responseDto =deleteDataServiceImpl.deleteData(deleteScriptsVO); 
		if (responseDto.getStatusCode() == HttpStatus.OK.value()) {
			return ResponseEntity.ok(responseDto);
		} else {
			return new ResponseEntity(
					new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error occured while deleting scripts"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}



