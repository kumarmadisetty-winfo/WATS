package com.winfo.service;

import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.winfo.model.TestSetExecutionStatus;
import com.winfo.repository.TestSetExecutionStatusRepository;
import com.winfo.repository.TestSetLinesRepository;
import com.winfo.repository.TestSetScriptParamRepository;

@Service
public class UpdateTestSetRecords {

	@Autowired
	private TestSetLinesRepository testSetLinesRepository;

	@Autowired
	private TestSetScriptParamRepository testSetScriptParamRepository;

	@Autowired
	private TestSetExecutionStatusRepository testSetExecutionStatusRepository;

	public void updateStatusStartTimeEndTimeTetSetLines(String testSetId) {
		testSetLinesRepository.updateStatusStartTimeEndTimeTetSetLines(testSetId);
	}

	public void updateLineExecutiStatusAndLineErrorMsg(String testSetId) {
		testSetScriptParamRepository.updateLineExecutiStatusAndLineErrorMsg(testSetId);
	}

	@Transactional
	public void insertTestSetExecutionStatusRecord(TestSetExecutionStatus testSetExecutionStatus) {
		testSetExecutionStatusRepository.save(testSetExecutionStatus);
	}

	public void updateTestRunScriptEnable(String testSetId) {
		testSetLinesRepository.updateTestRunScriptEnable(testSetId);
	}

}
