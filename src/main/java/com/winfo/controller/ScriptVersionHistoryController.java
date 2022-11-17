package com.winfo.controller;

import java.util.Map;
import java.util.Objects;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.winfo.exception.WatsEBSCustomException;
import com.winfo.model.ScriptMaster;
import com.winfo.services.ScriptVersionHistoryService;
import com.winfo.vo.ResponseDto;
import com.winfo.vo.VersionHistoryDto;

@CrossOrigin("*")
@RestController
public class ScriptVersionHistoryController {
	public static final Logger logger = Logger.getLogger(ScriptVersionHistoryController.class);

	@Autowired
	private ScriptVersionHistoryService versionHistoryService;

	@ResponseBody
	@PostMapping(value = "/saveVersionHistory")
	public ResponseDto saveVersionHistory(@Valid @RequestBody VersionHistoryDto versionHistoryDto) throws Exception {
		return versionHistoryService.saveVersionHistory(versionHistoryDto);
	}

	@ResponseBody
	@GetMapping(value = "/getVersionHistoryDetails")
	public Map<String, String> getMapOfVersionHistory(@Valid @RequestBody VersionHistoryDto versionHistoryDto)
			throws Exception {
		return versionHistoryService.getMapOfVersionHistory(versionHistoryDto);
	}

	@ResponseBody
	@GetMapping(value = "/getVersionHistory")
	public ScriptMaster getVersionHistory(@Valid @RequestBody VersionHistoryDto versionHistoryDto) throws Exception {
		if (!(Objects.isNull(versionHistoryDto.getVersionNumber()) || versionHistoryDto.getVersionNumber().isEmpty())) {
			return versionHistoryService.getVersionHistory(versionHistoryDto);
		} else {
			throw new WatsEBSCustomException(500, "Version can not be null!", null);
		}
	}
}
