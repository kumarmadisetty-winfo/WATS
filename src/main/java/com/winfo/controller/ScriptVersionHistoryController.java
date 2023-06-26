package com.winfo.controller;


import java.util.Map;
import java.util.Objects;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.winfo.exception.WatsEBSException;
import com.winfo.model.ScriptMaster;
import com.winfo.serviceImpl.ScriptVersionHistoryService;
import com.winfo.vo.ResponseDto;
import com.winfo.vo.ScriptMaterVO;
import com.winfo.vo.VersionHistoryDto;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin("*")
@RestController
public class ScriptVersionHistoryController {
	public static final Logger logger = Logger.getLogger(ScriptVersionHistoryController.class);

	@Autowired
	private ScriptVersionHistoryService versionHistoryService;

	@ResponseBody
	@PostMapping(value = "/saveVersionHistory")
	@ApiOperation( value="Save Version History ",notes = " <B>scriptId and Version Number</B> is to pass to save the version history for particular scriptId and version Number")
	@ApiResponses( value = { @ApiResponse( code=200,message="Saved version history successfully")})
	public ResponseDto saveVersionHistory(@Valid @RequestBody VersionHistoryDto versionHistoryDto) throws Exception {
		return versionHistoryService.saveVersionHistory(versionHistoryDto);
	}

	@ResponseBody
	@PostMapping(value = "/getScriptVersionHistoryDetails")
	@ApiOperation( value="Get ScriptVersion History Details ",notes = " <B>scriptId and Version Number</B> is to pass to get the script version history for particular scriptId and version Number")
	@ApiResponses( value = { @ApiResponse( code=200,message="Success")})
	public Map<String, String> getMapOfVersionHistory(@Valid @RequestBody VersionHistoryDto versionHistoryDto)
			throws Exception {
		return versionHistoryService.getMapOfVersionHistory(versionHistoryDto);
	}

	@ResponseBody
	@PostMapping(value = "/getVersionHistory")
	@ApiOperation( value="Get Version History ",notes = " <B>scriptId and Version Number</B> is to pass to get the version history")
	@ApiResponses( value = { @ApiResponse( code=200,message="Success")})
	public ScriptMaterVO getVersionHistory(@Valid @RequestBody VersionHistoryDto versionHistoryDto) throws Exception {
		if (!(Objects.isNull(versionHistoryDto.getVersionNumber()) || versionHistoryDto.getVersionNumber().isEmpty())) {
			return versionHistoryService.getVersionHistory(versionHistoryDto);
		} else {
			throw new WatsEBSException(500, "Version can not be null!", null);
		}
	}
}
