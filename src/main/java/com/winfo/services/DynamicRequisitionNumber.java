package com.winfo.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winfo.dao.DynamicNumberDao;

@Service
public class DynamicRequisitionNumber {

	@Autowired
	DynamicNumberDao dao;
	
	@Transactional
	public void saveCopyNumber(String value, String testParamId, String testSetId) {
	dao.saveCopyNumber(value,testParamId,testSetId);
		
	}

	@Transactional
	public String getCopynumber(String testrun_name, String seq, String line_number, String testParamId, String testSetId) {
		return dao.getCopynumber(testrun_name,seq,line_number,testParamId,testSetId);
		
	}
	
	@Transactional
	public String getCopynumber(String testrun_name, String seq, String line_number) {
		return dao.getCopynumber(testrun_name,seq,line_number);
		
	}
	
	@Transactional
	public String getCopynumberInputParameter(String testrun_name, String seq, String line_number, String testParamId, String testSetId) {
		return dao.getCopynumberInputParameter(testrun_name,seq,line_number,testParamId,testSetId);
		
	}
	
	@Transactional
	public void getTestSetParamIdWithCopyAction( String key,String value, String testSetLineId, String testSetId) {
	dao.getTestSetParamIdWithCopyAction(key,value,testSetLineId,testSetId);
		
	}

}
