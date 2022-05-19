package com.winfo.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.winfo.dao.DataBaseEntryDao;
import com.winfo.model.ScriptMaster;
import com.winfo.model.TestSetLines;
import com.winfo.model.TestSetScriptParam;
import com.winfo.utils.Constants.SCRIPT_PARAM_STATUS;
import com.winfo.utils.Constants.TEST_SET_LINE_ID_STATUS;

@Service
@RefreshScope
@Transactional
public class DataBaseEntry {
	@Autowired
	DataBaseEntryDao dao;

	public void updatePassedScriptLineStatus(FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO,
			String test_script_param_id, String status, String message) throws ClassNotFoundException, SQLException {
		dao.updatePassedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, status, message);
	}

	public void updatePassedScriptLineStatus(FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO,
			String test_script_param_id, String status, String value, String message)
			throws ClassNotFoundException, SQLException {
		dao.updatePassedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, status, value, message);
	}

	public void updateFailedScriptLineStatus(FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO,
			String test_script_param_id, String status, String error_message)
			throws ClassNotFoundException, SQLException {
		dao.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, status, error_message);
	}

	public void updateInProgressScriptLineStatus(FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO,
			String test_script_param_id, String status) throws ClassNotFoundException, SQLException {
		dao.updateInProgressScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, status);
	}

	public String getErrorMessage(String sndo, String ScriptName, String testRunName, FetchConfigVO fetchConfigVO)
			throws ClassNotFoundException, SQLException {
		return dao.getErrorMessage(sndo, ScriptName, testRunName, fetchConfigVO);
	}

	public void updateInProgressScriptStatus(FetchConfigVO fetchConfigVO, String test_set_id, String test_set_line_id)
			throws ClassNotFoundException, SQLException {
		dao.updateInProgressScriptStatus(fetchConfigVO, test_set_id, test_set_line_id);
	}

	public void updateStatusOfScript(String test_set_id, String test_set_line_id, String status)
			throws ClassNotFoundException, SQLException {
		dao.updateStatusOfScript(test_set_id, test_set_line_id, status);
	}

	public void updateStartTime(FetchConfigVO fetchConfigVO, String line_id, String test_set_id, Date start_time1)
			throws ClassNotFoundException, SQLException {
		dao.updateStartTime(fetchConfigVO, line_id, test_set_id, start_time1);
	}

	public String getTrMode(String args, FetchConfigVO fetchConfigVO) throws SQLException {
		return dao.getTrMode(args, fetchConfigVO);
	}

	public String getPassword(String args, String userId, FetchConfigVO fetchConfigVO)
			throws SQLException, ClassNotFoundException {
		return dao.getPassword(args, userId, fetchConfigVO);
	}

	public void updateEndTime(FetchConfigVO fetchConfigVO, String line_id, String test_set_id, Date end_time1)
			throws ClassNotFoundException, SQLException {
		dao.updateEndTime(fetchConfigVO, line_id, test_set_id, end_time1);
	}

	public void updateFailedImages(FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO,
			String test_script_param_id) throws SQLException {
		dao.updateFailedImages(fetchMetadataVO, fetchConfigVO, test_script_param_id);
	}

	@Transactional
	public Map<String, Map<String, TestSetScriptParam>> getTestRunMap(String test_run_id) {
		try {
			Map<String, Map<String, TestSetScriptParam>> map = dao.getTestRunMap(test_run_id);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Transactional
	public Map<String, TestSetScriptParam> getTestScriptMap(String test_set_line_id) {
		TestSetLines testSetLine = dao.getTestSetLine(test_set_line_id);
		return dao.getTestScriptMap(testSetLine);
	}

	@Transactional
	public boolean checkRunStatusOfDependantScript(String testSetId, String scriptId) {
		ScriptMaster scriptMaster = dao.findScriptMasterByScriptId(Integer.valueOf(scriptId));
		TestSetLines testLines = dao.checkTestSetLinesByScriptId(Integer.valueOf(testSetId),
				scriptMaster.getDependency());

		while (testLines.getStatus().equalsIgnoreCase(TEST_SET_LINE_ID_STATUS.IN_QUEUE.getLabel())
				|| testLines.getStatus().equalsIgnoreCase(TEST_SET_LINE_ID_STATUS.IN_PROGRESS.getLabel())) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			testLines = dao.checkTestSetLinesByScriptId(Integer.valueOf(testSetId), scriptMaster.getDependency());

		}

		if (testLines.getStatus().equalsIgnoreCase(TEST_SET_LINE_ID_STATUS.Pass.getLabel())) {
			return true;
		} else {
			return false;
		}
	}

	@Transactional
	public List<FetchMetadataVO> getMetaDataVOList(String testRunId, String testSetLineId, boolean finalPdf,
			boolean isManualTrigger) {
		return dao.getMetaDataVOList(testRunId, testSetLineId, finalPdf, isManualTrigger);
	}

	@Transactional
	public void setPassAndFailScriptCount(String testRunId, FetchConfigVO fetchConfigVO) {
		dao.getPassAndFailScriptCount(testRunId, fetchConfigVO);
	}

	public boolean checkIfAllTestSetLinesCompleted(long testSetId, Boolean enable) {
		ArrayList<String> result = dao.getTestSetLinesStatusByTestSetId(testSetId, enable);
		return !(result.stream().anyMatch(TEST_SET_LINE_ID_STATUS.IN_QUEUE.getLabel()::equalsIgnoreCase)
				|| result.stream().anyMatch(TEST_SET_LINE_ID_STATUS.IN_PROGRESS.getLabel()::equalsIgnoreCase));

	}

	public String pdfGenerationEnabled(long testSetId) {
		return dao.getTestSetPdfGenerationEnableStatus(testSetId);
	}

	public String getTestSetMode(Long testSetId) {
		return dao.getTestSetMode(testSetId);

	}

	public Boolean checkAllStepsStatusForAScript(String testSetLineId) throws ClassNotFoundException, SQLException {
		ArrayList<String> result = dao.getStepsStatusByScriptId(Integer.valueOf(testSetLineId));
		if (result.stream().allMatch(SCRIPT_PARAM_STATUS.NEW.getLabel()::equalsIgnoreCase)) {
			int firstStepScriptParamId = dao.findFirstStepIdInScript(testSetLineId);
			dao.updatePassedScriptLineStatus(null, null, firstStepScriptParamId + "",
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
		return dao.getScript(Long.valueOf(testSetId), Long.valueOf(testSetLineId)).getExecution_start_time();
	}
	
	public List<Object[]> getSeqNumAndStatus(String testSetId) {
		return dao.getStatusAndSeqNum(testSetId);
	}

	public ArrayList<Object[]> getConfigurationDetails(String testSetId) {
		return dao.getConfigurationDetails(testSetId);
	}

	public void updatePdfGenerationEnableStatus(String testSetId, String enabled) {
		dao.updatePdfGenerationEnableStatus(testSetId, enabled);
	}

	public Date findMaxExecutionEndDate(long testSetId) {
		return dao.findMaxExecutionEndDate(testSetId);
	}

	public Date findMinExecutionStartDate(long testSetId) {
		return dao.findMinExecutionStartDate(testSetId);
	}
}
