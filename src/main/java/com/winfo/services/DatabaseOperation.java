package com.winfo.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.winfo.dao.DataBaseEntryDao;
import com.winfo.dao.ExecutionAuditRepository;
import com.winfo.dao.ExecutionHistoryRepository;
import com.winfo.dao.ScriptParamRepository;
import com.winfo.dao.TestRunExecuteStatusRepository;
import com.winfo.dao.TestSetLinesRepository;
import com.winfo.dao.TestSetRepository;
import com.winfo.model.ExecutionAudit;
import com.winfo.utils.Constants.SCRIPT_PARAM_STATUS;
import com.winfo.utils.Constants.TEST_SET_LINE_ID_STATUS;

@Service
@RefreshScope
@Transactional
public class DatabaseOperation {

	@Autowired
	ScriptParamRepository scriptDao;
	
	@Autowired
	TestSetLinesRepository testSetLinesRepo;
	
	@Autowired
	DataBaseEntryDao dao;
	
	@Autowired
	TestSetRepository testSetRepo;
	
	@Autowired
	ExecutionHistoryRepository execHistoryRepo;
	
	@Autowired
	TestRunExecuteStatusRepository testRunExecStatusRepo;
	
	@Autowired
	ExecutionAuditRepository exeAuditRepo;
	

	public Boolean checkAllStepsStatusForAScript(String testSetLineId) {
		List<String> result = scriptDao.getStepsStatusByScriptId(Integer.valueOf(testSetLineId));
		if (result.stream().allMatch(SCRIPT_PARAM_STATUS.NEW.getLabel()::equalsIgnoreCase)) {
			int firstStepScriptParamId = scriptDao.findFirstStepIdInScript(testSetLineId);
			scriptDao.updatePassedScriptLineStatus(firstStepScriptParamId + "",
					SCRIPT_PARAM_STATUS.FAIL.getLabel(),
					"System could not launch the script. Try to re-execute. If it continues to fail, please contact WATS Support Team");
			return false;
		}
		if (result.stream().anyMatch(SCRIPT_PARAM_STATUS.NEW.getLabel()::equalsIgnoreCase)
				|| result.stream().anyMatch(SCRIPT_PARAM_STATUS.FAIL.getLabel()::equalsIgnoreCase)) {
			return false;
		} else if (result.stream().anyMatch(SCRIPT_PARAM_STATUS.IN_PROGRESS.getLabel()::equalsIgnoreCase)) {
			return null;
		} else {
			return true;
		}
	}
	
	public Date getExecStartDateOfScript(String testSetId, String testSetLineId) {
		 return testSetLinesRepo.getScript(Long.valueOf(testSetId), Long.valueOf(testSetLineId)).getExecutionStartTime();
	}
	
	public List<FetchMetadataVO> getMetaDataVOList(String testRunId, String testSetLineId, boolean finalPdf,
			boolean isManualTrigger) {
		return dao.getMetaDataVOList(testRunId, testSetLineId, finalPdf, isManualTrigger);
	}
	
	public void updateFaileScriptscount(String testSetLineId, String testSetId) {
		testSetLinesRepo.updateFaileScriptscount(testSetLineId,testSetId);
		
	}
	
	public int getFailedScriptRunCount(String testSetLineId, String testSetId) {
		return testSetLinesRepo.getFailedScriptRunCount(testSetLineId,testSetId);
	}
	
	public List<String> getStatusByTestSetId(String testSetId) {
		return testSetLinesRepo.getStatusByTestSetId(testSetId);
	}
	
	public List<Object[]> getStatusAndSeqNum(String testSetId) {
		return testSetLinesRepo.getStatusAndSeqNum(testSetId);	
	}
	
	@Transactional
	public void updateSetLinesStatusAndTestSetPath(FetchScriptVO fetchScriptVO, FetchConfigVO fetchConfigVO) {
		testSetLinesRepo.updateTestSetLineStatus(fetchScriptVO.getP_status(), fetchScriptVO.getP_test_set_line_path(),
				fetchScriptVO.getP_test_set_id(), fetchScriptVO.getP_test_set_line_id(), fetchScriptVO.getP_script_id(),
				fetchConfigVO.getEndtime());
		testSetRepo.updateTestSetPaths(fetchScriptVO.getP_pass_path(), fetchScriptVO.getP_fail_path(),
				fetchScriptVO.getP_exception_path(), fetchScriptVO.getP_test_set_id());
		execHistoryRepo.updateExecHistoryTbl(fetchScriptVO.getP_test_set_line_id(), fetchConfigVO.getStarttime(),
				fetchConfigVO.getEndtime(), fetchScriptVO.getP_status());
		
		testRunExecStatusRepo.updateExecStatusTable(fetchScriptVO.getP_test_set_id());
	}

	public void insertTestRunScriptData(FetchConfigVO fetchConfigVO, List<FetchMetadataVO> fetchMetadataListVO,
			String scriptId, String scriptNumber, String status, Date startDate, Date endDate) {
		try {
			ExecutionAudit executionAudit = new ExecutionAudit();
			String testSetId = fetchMetadataListVO.get(0).getTest_set_id();
			executionAudit.setTestsetid(testSetId);
			executionAudit.setScriptid(scriptId);
			executionAudit.setScriptnumber(scriptNumber);
			executionAudit.setExecutionstarttime(startDate);
			executionAudit.setExecutionendtime(endDate);
			executionAudit.setStatus(status);
			exeAuditRepo.insertTestrundata(executionAudit);
			System.out.println("data added successfully");
			System.out.println("data added successfully");
		} catch (Exception e) {
			System.out.println("testrun data not added " + e);
			e.printStackTrace();
		}
	}
	
	public String pdfGenerationEnabled(long testSetId) {
		return testSetRepo.getTestSetPdfGenerationEnableStatus(testSetId);
	}
	
	public boolean checkIfAllTestSetLinesCompleted(long testSetId, Boolean enable) {
		List<String> result = testSetLinesRepo.getTestSetLinesStatusByTestSetId(testSetId, enable);
		return !(result.stream().anyMatch(TEST_SET_LINE_ID_STATUS.IN_QUEUE.getLabel()::equalsIgnoreCase)
				|| result.stream().anyMatch(TEST_SET_LINE_ID_STATUS.IN_PROGRESS.getLabel()::equalsIgnoreCase));

	}
	
	public void updatePdfGenerationEnableStatus(String testSetId, String enabled) {
		testSetRepo.updatePdfGenerationEnableStatus(testSetId, enabled);
	}
	
	public void setPassAndFailScriptCount(String testRunId, FetchConfigVO fetchConfigVO) {
		testSetLinesRepo.getPassAndFailScriptCount(testRunId, fetchConfigVO);
	}

}
