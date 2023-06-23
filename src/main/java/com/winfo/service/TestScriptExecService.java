package com.winfo.service;

import org.springframework.stereotype.Service;

import com.winfo.vo.ResponseDto;

@Service
public interface TestScriptExecService {

	public ResponseDto generateTestRunPdf(String testSetId);
	
}
