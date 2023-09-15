package com.winfo.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.winfo.constraint.OracleAPIValidation;
import com.winfo.constraint.TestRunIdValidation;
import com.winfo.vo.ResponseDto;

@Service
public interface TestRunValidationService {
	
	public  ResponseDto validateTestRun(@Validated @TestRunIdValidation @OracleAPIValidation Integer testSetId) throws Exception;
}
