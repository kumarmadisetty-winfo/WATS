package com.winfo.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;

import com.winfo.constraint.TestRunAPIValidation;
import com.winfo.constraint.ScheduleIdValidation;
import com.winfo.constraint.TestRunIdValidation;
import com.winfo.constraint.TestSetLineIdValidation;
import com.winfo.vo.ResponseDto;

@Service
@Validated
public interface ValidationService {
	
	public  ResponseDto validateTestRun(@TestRunIdValidation @TestRunAPIValidation Integer testSetId,boolean validateAll) throws Exception;
	
	public  ResponseDto validateSchedule(@ScheduleIdValidation Integer jobId) throws Exception;
	
	public  ResponseDto validateTestRunScript(@TestRunIdValidation @TestRunAPIValidation Integer testSetId,@TestSetLineIdValidation Integer testSetLineId) throws Exception;
	
}
