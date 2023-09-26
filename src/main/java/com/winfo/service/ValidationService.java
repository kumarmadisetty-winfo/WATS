package com.winfo.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;

import com.winfo.constraint.TestRunAPIValidation;
import com.winfo.constraint.ScheduleIdValidation;
import com.winfo.constraint.TestRunValidation;
import com.winfo.constraint.TestSetLineValidation;
import com.winfo.vo.ResponseDto;

@Service
@Validated
public interface ValidationService {
	
	public  ResponseDto validateTestRun(@TestRunValidation @TestRunAPIValidation Integer testSetId,boolean validateAll) throws Exception;
	
	public  ResponseDto validateSchedule(@ScheduleIdValidation Integer jobId) throws Exception;
	
	public  ResponseDto validateTestRunScript(@TestRunValidation @TestRunAPIValidation Integer testSetId,@TestSetLineValidation Integer testSetLineId) throws Exception;
	
}
