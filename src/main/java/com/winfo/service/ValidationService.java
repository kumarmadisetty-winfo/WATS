package com.winfo.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;

import com.winfo.constraint.TestRunAPIValidation;
import com.winfo.constraint.ScheduleAPIValidation;
import com.winfo.constraint.ScheduleValidation;
import com.winfo.constraint.TestRunValidation;
import com.winfo.constraint.TestSetLineValidation;
import com.winfo.vo.ResponseDto;

@Service
@Validated
public interface ValidationService {
	
	public  ResponseEntity<ResponseDto> validateTestRun(@TestRunValidation @TestRunAPIValidation Integer testSetId,boolean validateAll) throws Exception;
	
	public  ResponseEntity<ResponseDto> validateSchedule(@ScheduleValidation @ScheduleAPIValidation Integer jobId) throws Exception;
	
	public  ResponseEntity<ResponseDto> validateTestRunScript(@TestRunValidation @TestRunAPIValidation Integer testSetId,@TestSetLineValidation Integer testSetLineId) throws Exception;
	
}
