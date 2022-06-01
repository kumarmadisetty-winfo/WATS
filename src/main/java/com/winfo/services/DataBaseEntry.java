package com.winfo.services;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.NoResultException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.winfo.dao.CopyTestRunDao;
import com.winfo.dao.DataBaseEntryDao;
import com.winfo.model.AuditScriptExecTrail;
import com.winfo.model.ScriptMaster;
import com.winfo.model.TestSetLine;
import com.winfo.model.TestSetScriptParam;
import com.winfo.utils.Constants.AUDIT_TRAIL_STAGES;
import com.winfo.utils.Constants.SCRIPT_PARAM_STATUS;
import com.winfo.utils.Constants.TEST_SET_LINE_ID_STATUS;
import com.winfo.vo.EmailParamDto;

@Service
@RefreshScope
@Transactional
public class DataBaseEntry {
	public final Logger logger = LogManager.getLogger(DataBaseEntry.class);
	private static final String COMPLETED = "Completed";

	@Autowired
	DataBaseEntryDao dao;

	@Autowired
	CopyTestRunDao copyTestrunDao;

	@Autowired
	SendMailServiceImpl sendMailServiceImpl;

	@Autowired
	LimitScriptExecutionService limitScriptExecutionService;

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

	public String getErrorMessage(String sndo, String ScriptName, String testRunName)
			throws ClassNotFoundException, SQLException {
		return dao.getErrorMessage(sndo, ScriptName, testRunName);
	}

	public void updateInProgressScriptStatus(FetchConfigVO fetchConfigVO, String test_set_id, String test_set_line_id)
			throws ClassNotFoundException, SQLException {
		dao.updateInProgressScriptStatus(fetchConfigVO, test_set_id, test_set_line_id);
	}

	public List<Object[]> getStatusAndSeqNum(String testSetId) {
		return dao.getStatusAndSeqNum(testSetId);
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

	public void updateEndTime(FetchConfigVO fetchConfigVO, String lineId, String testSetId, Date endTime)
			throws ClassNotFoundException, SQLException {
		dao.updateEndTime(fetchConfigVO, lineId, testSetId, endTime);
	}

	public void updateSubscription() {
		List<Object[]> noOfHits = dao.getSumDetailsFromSubscription();
		List<Object[]> subscriptionDtls = dao.getSubscriptionDetails();

		BigDecimal sumQuantity = (BigDecimal) noOfHits.get(0)[0];
		BigDecimal sumExecuted = (BigDecimal) noOfHits.get(0)[1];
//		BigDecimal sumBalance = (BigDecimal) noOfHits.get(0)[2];

		BigDecimal subsId = (BigDecimal) subscriptionDtls.get(0)[0];
		BigDecimal executed = (BigDecimal) subscriptionDtls.get(0)[1];
		BigDecimal balance = (BigDecimal) subscriptionDtls.get(0)[2];

		Integer sum = dao.findGraceAllowance(subsId);

		Integer graceValue = (sum == null) ? 0 : sum;

		if (sumQuantity.intValue() + graceValue.intValue() - sumExecuted.intValue() > 0) {
			dao.updateSubscriptionExecuteAndBalance(executed, balance, subsId);
		}

		if (Math.abs(balance.intValue() - 1) >= graceValue && (balance.intValue() - 1) <= 0) {
			dao.updateSubscriptionStatus(COMPLETED, subsId);
		}

	}

	public void updateTestCaseEndDate(FetchScriptVO fetchScriptVO, Date endDate) {
		dao.updateTestSetPaths(fetchScriptVO.getP_pass_path(), fetchScriptVO.getP_fail_path(),
				fetchScriptVO.getP_exception_path(), fetchScriptVO.getP_test_set_id());
		dao.updateTestSetLineStatus(fetchScriptVO.getP_status(), fetchScriptVO.getP_test_set_line_path(),
				fetchScriptVO.getP_test_set_id(), fetchScriptVO.getP_test_set_line_id(), fetchScriptVO.getP_script_id(),
				endDate);
	}

	@Transactional
	public void updateTestCaseStatus(FetchScriptVO fetchScriptVO, FetchConfigVO fetchConfigVO,
			List<FetchMetadataVO> fetchMetadataListVO, Date startDate) {
		EmailParamDto emailParam = new EmailParamDto();
		emailParam.setTestSetName(fetchMetadataListVO.get(0).getTest_run_name());
		emailParam.setExecutedBy(fetchMetadataListVO.get(0).getExecuted_by());
		updateSubscription();
		dao.insertExecHistoryTbl(fetchScriptVO.getP_test_set_line_id(), fetchConfigVO.getStarttime(),
				fetchConfigVO.getEndtime(), fetchScriptVO.getP_status());

		Integer responseCount = dao.updateExecStatusTable(fetchScriptVO.getP_test_set_id());

		BigDecimal requestCount = (BigDecimal) dao.getRequestCountFromExecStatus(fetchScriptVO.getP_test_set_id());
		emailParam.setRequestCount(requestCount.intValue());
		if (requestCount.intValue() <= responseCount) {
			dao.updateExecStatusFlag(fetchScriptVO.getP_test_set_id());
			dao.getPassAndFailCount(fetchScriptVO.getP_test_set_id(), emailParam);
			dao.getUserAndPrjManagerName(emailParam.getExecutedBy(), fetchScriptVO.getP_test_set_id(), emailParam);
			sendMailServiceImpl.sendMail(emailParam);
		} else {
			Integer inProgressCount = dao.getCountOfInProgressScript(fetchScriptVO.getP_test_set_id());
			if (inProgressCount.equals(0)) {
				dao.updateExecStatusFlag(fetchScriptVO.getP_test_set_id());
			}
		}
		limitScriptExecutionService.insertTestRunScriptData(fetchConfigVO, fetchMetadataListVO,
				fetchMetadataListVO.get(0).getScript_id(), fetchMetadataListVO.get(0).getScript_number(),
				fetchConfigVO.getStatus1(), startDate, fetchConfigVO.getEndtime());
	}

	public void getPassAndFailCount(String testSetId, EmailParamDto emailParam) {
		dao.getPassAndFailCount(testSetId, emailParam);

	}

	public void getUserAndPrjManagerName(String userName, String testSetId, EmailParamDto emailParam) {
		dao.getUserAndPrjManagerName(userName, testSetId, emailParam);
	}

	public void updateFailedImages(FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO,
			String test_script_param_id) throws SQLException {
		dao.updateFailedImages(fetchMetadataVO, fetchConfigVO, test_script_param_id);
	}

	@Transactional
	public Map<String, Map<String, TestSetScriptParam>> getTestRunMap(String testRunId) {
		try {
			Map<String, Map<String, TestSetScriptParam>> map = dao.getTestRunMap(testRunId);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Transactional
	public List<String> getStatusByTestSetId(String testSetId) {
		return dao.getStatusByTestSetId(testSetId);
	}

	@Transactional
	public Map<String, TestSetScriptParam> getTestScriptMap(String testSetLineId) {
		TestSetLine testSetLine = dao.getTestSetLine(testSetLineId);
		return dao.getTestScriptMap(testSetLine);
	}

	@Transactional
	public boolean checkRunStatusOfDependantScript(String testSetId, String scriptId) {
		ScriptMaster scriptMaster = dao.findScriptMasterByScriptId(Integer.valueOf(scriptId));
		TestSetLine testLines = dao.checkTestSetLinesByScriptId(Integer.valueOf(testSetId),
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
			boolean executeApi) {
		return dao.getMetaDataVOList(testRunId, testSetLineId, finalPdf, executeApi);
	}

	@Transactional
	public void setPassAndFailScriptCount(String testRunId, FetchConfigVO fetchConfigVO) {
		dao.getPassAndFailScriptCount(testRunId, fetchConfigVO);
	}

	public boolean checkIfAllTestSetLinesCompleted(long testSetId, Boolean enable) {
		List<String> result = dao.getTestSetLinesStatusByTestSetId(testSetId, enable);
		return !(result.stream().anyMatch(TEST_SET_LINE_ID_STATUS.IN_QUEUE.getLabel()::equalsIgnoreCase)
				|| result.stream().anyMatch(TEST_SET_LINE_ID_STATUS.IN_PROGRESS.getLabel()::equalsIgnoreCase));

	}

	public String pdfGenerationEnabled(long testSetId) {
		return dao.getTestSetPdfGenerationEnableStatus(testSetId);
	}

	public TestSetLine getTestSetLinesRecord(String testSetId, String testSetLineId) {
		return dao.getScript(Long.valueOf(testSetId), Long.valueOf(testSetLineId));
	}

	public String getTestSetMode(Long testSetId) {
		return dao.getTestSetMode(testSetId);

	}

	public Boolean checkAllStepsStatusForAScript(String testSetLineId) throws ClassNotFoundException, SQLException {
		List<String> result = dao.getStepsStatusByScriptId(Integer.valueOf(testSetLineId));
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

	public List<Object[]> getSeqNumAndStatus(String testSetId) {
		return dao.getStatusAndSeqNum(testSetId);
	}

	public List<Object[]> getConfigurationDetails(String testSetId) {
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

	public AuditScriptExecTrail insertScriptExecAuditRecord(AuditScriptExecTrail auditTrial, AUDIT_TRAIL_STAGES stage) {
		try {
			logger.info("Audit Inserting stage {}", stage.getLabel());
			AuditScriptExecTrail auditTrialNew = AuditScriptExecTrail.builder()
					.correlationId(auditTrial.getCorrelationId()).testSetLineId(auditTrial.getTestSetLineId())
					.triggeredBy(auditTrial.getTriggeredBy()).build();
			auditTrialNew.setStageId(dao.findAuditStageIdByName(stage.getLabel()));
			auditTrialNew.setEventTime(new Date());
			dao.insertAuditScriptExecTrail(auditTrialNew);
		} catch (Exception e) {
			// no need of throwing exception, just print
			logger.error(
					"Exception occured while loggin audit trial for test set line id - {} with correlation id - {}",
					auditTrial.getTestSetLineId(), auditTrial.getCorrelationId());
			e.printStackTrace();
		}
		return auditTrial;
	}

	public Date findStepMaxUpdatedDate(String testSetLineId, Date startDate) {
		Date endDate = null;
		try {
			endDate = dao.findStepMaxUpdatedDate(testSetLineId);
		} catch (NoResultException e) {
			endDate = startDate;
		}
		return endDate != null ? endDate : startDate;
	}
	
	public Integer getCountOfInProgressScript(String testSetId) {
		return dao.getCountOfInProgressScript(testSetId);
	}
	
	public void updateExecStatusFlag(String testSetId) {
		dao.updateExecStatusFlag(testSetId);
	}
	
}
