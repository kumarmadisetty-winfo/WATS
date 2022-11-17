package com.winfo.controller;

import java.io.IOException;
import java.util.Map;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.winfo.model.ScriptMaster;
import com.winfo.services.VersionHistoryService;
import com.winfo.vo.ResponseDto;
import com.winfo.vo.VersionHistoryDto;

@CrossOrigin("*")
@RestController
public class VersionHistoryController {
	public static final Logger logger = Logger.getLogger(VersionHistoryController.class);

	@Autowired
	private VersionHistoryService versionHistoryService;

	@ResponseBody
	@PostMapping(value = "/saveVersionHistory")
	public ResponseDto saveVersionHistory(@Valid @RequestBody VersionHistoryDto versionHistoryDto) throws IOException {
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
	public ScriptMaster getVersionHistory(@Valid @RequestBody VersionHistoryDto versionHistoryDto) throws IOException {
		return versionHistoryService.getVersionHistory(versionHistoryDto);
	}
}
