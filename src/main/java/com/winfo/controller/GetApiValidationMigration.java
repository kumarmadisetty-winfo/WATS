package com.winfo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.winfo.services.GetApiValidationMigrationService;
import com.winfo.vo.LookUpCodeVO;
import com.winfo.vo.ResponseDto;

@RestController
public class GetApiValidationMigration {
	@Autowired
	GetApiValidationMigrationService apiValidationMigrationService;

	@PostMapping("/apiValidationMigrationReceiver")
	public ResponseDto apiValidationMigration(@RequestBody List<LookUpCodeVO> lookUpCodeVOData) {
		return apiValidationMigrationService.apiValidationMigration(lookUpCodeVOData);
	}

}
