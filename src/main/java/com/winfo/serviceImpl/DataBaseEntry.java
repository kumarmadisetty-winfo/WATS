package com.winfo.serviceImpl;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.winfo.dao.DataBaseEntryDao;
import com.winfo.model.AuditScriptExecTrail;
import com.winfo.model.Customer;
import com.winfo.model.LookUpCode;
import com.winfo.model.ScriptMaster;
import com.winfo.model.TestSet;
import com.winfo.model.TestSetAttribute;
import com.winfo.model.TestSetLine;
import com.winfo.model.TestSetScriptParam;
import com.winfo.repository.CustomerRepository;
import com.winfo.repository.LookUpCodeRepository;
import com.winfo.repository.ScriptMasterRepository;
import com.winfo.repository.SubscriptionRepository;
import com.winfo.repository.TestSetLinesRepository;
import com.winfo.utils.Constants;
import com.winfo.utils.Constants.AUDIT_TRAIL_STAGES;
import com.winfo.utils.Constants.SCRIPT_PARAM_STATUS;
import com.winfo.utils.Constants.TEST_SET_LINE_ID_STATUS;
import com.winfo.utils.Constants.UPDATE_STATUS;
import com.winfo.vo.CustomerProjectDto;
import com.winfo.vo.EmailParamDto;
import com.winfo.vo.FetchConfigVO;
import com.winfo.vo.FetchMetadataVO;
import com.winfo.vo.FetchScriptVO;
import com.winfo.vo.ScriptDetailsDto;
import com.winfo.vo.Status;

@Service
@RefreshScope
@Transactional
public class DataBaseEntry {
	@Autowired
	DataBaseEntryDao dao;
	@Autowired
	SendMailServiceImpl sendMailServiceImpl;

	@Autowired
	LimitScriptExecutionService limitScriptExecutionService;
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	SubscriptionRepository subscriptionRepository;
	
	@Autowired
	ApplicationContext appContext;
	
	public final Logger logger = LogManager.getLogger(DataBaseEntry.class);
	private static final String COMPLETED = "Completed";
	@Autowired
	private LookUpCodeRepository lookUpCodeJpaRepository;
	
	@Autowired
	private ScriptMasterRepository scriptMasterRepository;
	
	@Autowired
	private TestSetLinesRepository testSetLinesRepository;
	
	public void updateStartAndEndTimeForTestSetTable(String testSetId, Date startTime, Date endTime) {
		dao.updateStartAndEndTimeForTestSetTable(testSetId, startTime, endTime);
		
	}
	
	public void updatePassedScriptLineStatus(ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			String test_script_param_id, String status) throws ClassNotFoundException, SQLException {
		dao.updatePassedScriptLineStatus(test_script_param_id, status);
	}

	public void updateFailedScriptLineStatus(ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			String test_script_param_id, String status, String error_message)
			throws ClassNotFoundException, SQLException {
		dao.updateFailedScriptLineStatus(test_script_param_id, error_message);
	}

	public void updateInProgressScriptLineStatus(String test_script_param_id, String status)
			throws ClassNotFoundException, SQLException {
		dao.updateInProgressScriptLineStatus(test_script_param_id, status);
	}

	public String getErrorMessage(String sndo, String ScriptName, String testRunName, FetchConfigVO fetchConfigVO)
			throws ClassNotFoundException, SQLException {
		return dao.getErrorMessage(sndo, ScriptName, testRunName);
	}

	public String getErrorMessage(String sndo, String ScriptName, String testRunName)
			throws ClassNotFoundException, SQLException {
		return dao.getErrorMessage(sndo, ScriptName, testRunName);
	}

	public void updateInProgressScriptStatus(FetchConfigVO fetchConfigVO, String test_set_id, String test_set_line_id)
			throws ClassNotFoundException, SQLException {
		dao.updateInProgressScriptStatus(test_set_id);
	}

	public void updateStartTime(FetchConfigVO fetchConfigVO, String line_id, String test_set_id, Date start_time1)
			throws ClassNotFoundException, SQLException {
		dao.updateStartTime(line_id, test_set_id, start_time1);
	}

	public String getTrMode(String args, FetchConfigVO fetchConfigVO) throws SQLException {
		return dao.getTrMode(args);
	}

	public String getPassword(String args, String userId, FetchConfigVO fetchConfigVO)
			throws SQLException, ClassNotFoundException {
		return dao.getPassword(args, userId);
	}

	public void updateEndTime(FetchConfigVO fetchConfigVO, String line_id, String test_set_id, Date end_time1)
			throws ClassNotFoundException, SQLException {
		dao.updateEndTime(line_id, test_set_id, end_time1);
	}

	public void updateFailedImages(ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			String test_script_param_id, CustomerProjectDto customerDetails) throws SQLException, IOException {
		dao.updateFailedImages(fetchMetadataVO, fetchConfigVO, test_script_param_id, customerDetails);
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
		TestSetLine testSetLine = dao.getTestSetLine(test_set_line_id);
		return dao.getTestScriptMap(testSetLine);
	}

	@Transactional
	public void getDependentScriptNumbers(LinkedHashMap<String, List<ScriptDetailsDto>> dependentScriptMap) {
		List<Integer> dependentList = new ArrayList();
		for (Entry<String, List<ScriptDetailsDto>> element : dependentScriptMap.entrySet()) {
			dependentList.add(Integer.parseInt(element.getValue().get(0).getScriptId()));

		}

		dao.getDependentScriptNumbers(dependentScriptMap, dependentList);
		/*
		 * for(Entry<String,List<ScriptDetailsDto>>
		 * element:dependentScriptMap.entrySet()) {
		 * //dependentList.add(Integer.parseInt(element.getValue().get(0).getScript_id()
		 * ));
		 * status.put(Integer.parseInt(element.getValue().get(0).getScript_id()),"New");
		 * 
		 * if(element.getValue().get(0).getDependencyScriptNumber()!=null) {
		 * status.put(element.getValue().get(0).getDependencyScriptNumber(),"New"); }
		 * 
		 * }
		 */

	}

	@Transactional
	public void getTestRunLevelDependentScriptNumbers(LinkedHashMap<String, List<ScriptDetailsDto>> dependentScriptMap,
			String testSetId) {
		List<Integer> dependentList = new ArrayList();
		for (Entry<String, List<ScriptDetailsDto>> element : dependentScriptMap.entrySet()) {
			dependentList.add(Integer.parseInt(element.getValue().get(0).getTestSetLineId()));

		}
		dao.getTestRunLevelDependentScriptNumbers(dependentScriptMap, dependentList, testSetId);
	}

	public void getStatus(Integer dependentScriptNo, Integer test_set_id, Map<Integer, Status> scriptStatus,
			int testRunDependencyCount) {
		// TODO Auto-generated method stub
		dao.getStatus(dependentScriptNo, test_set_id, scriptStatus, testRunDependencyCount);
	}

	@Transactional
	public int getTestRunDependentCount(String testSetId) {
		return dao.getTestRunDependentCount(testSetId);
	}

	public String getPackage(String args) {
		return dao.getPackage(args);
	}

	public Customer getCustomer(String args) {
		return dao.getCustomer(args);
	}

	public String getTestSetMode(Long testSetId) {
		return dao.getTestSetMode(testSetId);

	}

	public AuditScriptExecTrail insertScriptExecAuditRecord(AuditScriptExecTrail auditTrial, AUDIT_TRAIL_STAGES stage,
			String errorMessage) {
		try {
			logger.info("Audit Inserting stage {}", stage.getLabel());
			AuditScriptExecTrail auditTrialNew = AuditScriptExecTrail.builder()
					.correlationId(auditTrial.getCorrelationId()).testSetLineId(auditTrial.getTestSetLineId())
					.triggeredBy(auditTrial.getTriggeredBy()).build();
			auditTrialNew.setStageId(dao.findAuditStageIdByName(stage.getLabel()));
			auditTrialNew.setEventTime(new Date());
			auditTrialNew.setMessage(errorMessage);
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

	public void updateEnabledStatusForTestSetLine(String testSetId, String isEnable) {
		dao.updateEnabledStatusForTestSetLine(testSetId, isEnable);
	}

	public void updateStatusOfScript(String test_set_line_id, String status) {
		dao.updateStatusOfScript(test_set_line_id, status);
	}

	public void updateDefaultMessageForFailedScriptInFirstStep(String testSetLineId, String errMessage) {
		int firstStepScriptParamId = dao.findFirstStepIdInScript(testSetLineId);
		dao.updatePassedScriptLineStatus(null, null, firstStepScriptParamId + "", SCRIPT_PARAM_STATUS.FAIL.getLabel());
	}

	public void updateExecStatusIfTestRunIsCompleted(String testSetId) {
		Integer inProgressCount = dao.getCountOfInProgressScript(testSetId);
		if (inProgressCount.equals(0)) {
			dao.updateExecStatusFlag(testSetId);
		}
	}

	public TestSetLine getTestSetLinesRecord(String testSetId, String testSetLineId) {
		return dao.getScript(Long.valueOf(testSetId), Long.valueOf(testSetLineId));
	}

	public String getScriptStatus(String testSetLineId) {
		List<String> result = dao.getStepsStatusByScriptId(Integer.valueOf(testSetLineId));
		TestSetLine testSetLine =dao.getScriptDataByLineID(Integer.valueOf(testSetLineId));
		if(testSetLine.getStatus() != null && (!"".equalsIgnoreCase(testSetLine.getStatus())) 
				&& testSetLine.getStatus().equalsIgnoreCase(UPDATE_STATUS.PASS.getLabel())) {
			return UPDATE_STATUS.PASS.getLabel();
		}
		if (result.stream().allMatch(SCRIPT_PARAM_STATUS.NEW.getLabel()::equalsIgnoreCase)) {
			appContext.getBean(this.getClass()).updateDefaultMessageForFailedScriptInFirstStep(testSetLineId,
					Constants.ERR_MSG_FOR_SCRIPT_RUN);
			return UPDATE_STATUS.FAIL.getLabel();
		}

		if (result.stream().anyMatch(SCRIPT_PARAM_STATUS.NEW.getLabel()::equalsIgnoreCase)
				|| result.stream().anyMatch(SCRIPT_PARAM_STATUS.FAIL.getLabel()::equalsIgnoreCase)
				|| result.stream().anyMatch(SCRIPT_PARAM_STATUS.IN_PROGRESS.getLabel()::equalsIgnoreCase)) {
			return UPDATE_STATUS.FAIL.getLabel();
		} else {
			return UPDATE_STATUS.PASS.getLabel();
		}
	}

	public boolean checkScriptStatusForSteps(List<Integer> stepIdList) {
		Boolean status = null;

		List<String> result = dao.getStepsStatusForSteps(stepIdList);
		if (result.stream().anyMatch(SCRIPT_PARAM_STATUS.FAIL.getLabel()::equalsIgnoreCase)) {
			status = false;
		}
		if (result.stream().allMatch(SCRIPT_PARAM_STATUS.PASS.getLabel()::equalsIgnoreCase)) {
			status = true;
		}
		while (status == null) {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			result = dao.getStepsStatusForSteps(stepIdList);
			if (result.stream().anyMatch(SCRIPT_PARAM_STATUS.FAIL.getLabel()::equalsIgnoreCase)) {
				status = false;
			}
			if (result.stream().allMatch(SCRIPT_PARAM_STATUS.PASS.getLabel()::equalsIgnoreCase)) {
				status = true;
			}
		}

		return status;
	}

	public CustomerProjectDto getCustomerDetails(String testSetId) {

		return dao.getCustomerDetails(testSetId);
	}

	public List<ScriptDetailsDto> getScriptDetailsListVO(String testRunId, String testSetLineId, boolean finalPdf,
			boolean executeApi) {
		return dao.getScriptDetails(testRunId, testSetLineId, finalPdf, executeApi);
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

	public void updateTestCaseEndDate(FetchScriptVO fetchScriptVO, Date endDate, String status) {
		dao.updateTestSetPaths(fetchScriptVO.getP_pass_path(), fetchScriptVO.getP_fail_path(),
				fetchScriptVO.getP_exception_path(), fetchScriptVO.getP_test_set_id());
		dao.updateTestSetLineStatus(status, fetchScriptVO.getP_test_set_line_path(), fetchScriptVO.getP_test_set_id(),
				fetchScriptVO.getP_test_set_line_id(), fetchScriptVO.getP_script_id(), endDate);
	}

	@Transactional
	public void updateTestCaseStatus(FetchScriptVO fetchScriptVO, FetchConfigVO fetchConfigVO,
			List<ScriptDetailsDto> fetchMetadataListVO, Date startDate, String testRunName, boolean isDependentFailBecauseOfIndependent) {
		EmailParamDto emailParam = new EmailParamDto();
		emailParam.setTestSetName(testRunName);
		emailParam.setExecutedBy(fetchMetadataListVO.get(0).getExecutedBy());
		if (!isDependentFailBecauseOfIndependent) {
			appContext.getBean(this.getClass()).updateSubscription();
		}
		dao.insertExecHistoryTbl(fetchScriptVO.getP_test_set_line_id(), fetchConfigVO.getStarttime(),
				fetchConfigVO.getEndtime(), fetchConfigVO.getStatus1());

		Integer responseCount = dao.updateExecStatusTable(fetchScriptVO.getP_test_set_id());

		BigDecimal requestCount = (BigDecimal) dao.getRequestCountFromExecStatus(fetchScriptVO.getP_test_set_id());
		emailParam.setRequestCount(requestCount.intValue());
		if (requestCount.intValue() <= responseCount) {
			dao.updateExecStatusFlag(fetchScriptVO.getP_test_set_id());
			dao.getPassAndFailCount(fetchScriptVO.getP_test_set_id(), emailParam);
			dao.getUserAndPrjManagerName(emailParam.getExecutedBy(), fetchScriptVO.getP_test_set_id(), emailParam);
			boolean sendMail = appContext.getBean(this.getClass())
					.checkIfAllTestSetLinesCompleted(Long.valueOf(fetchScriptVO.getP_test_set_id()), true);
			if (sendMail) {
				sendMailServiceImpl.sendMail(emailParam);
			}
		} else {
			Integer inProgressCount = dao.getCountOfInProgressScript(fetchScriptVO.getP_test_set_id());
			if (inProgressCount.equals(0)) {
				dao.updateExecStatusFlag(fetchScriptVO.getP_test_set_id());
			}
		}
		limitScriptExecutionService.insertTestRunScriptData(fetchMetadataListVO.get(0).getScriptId(),
				fetchMetadataListVO.get(0).getScriptNumber(), fetchConfigVO.getStatus1(), fetchConfigVO.getStarttime(),
				fetchConfigVO.getEndtime(), fetchScriptVO.getP_test_set_id());
	}

	public String pdfGenerationEnabled(long testSetId) {
		return dao.getTestSetPdfGenerationEnableStatus(testSetId);
	}

	public boolean checkIfAllTestSetLinesCompleted(long testSetId, Boolean enable) {
		List<String> result = dao.getTestSetLinesStatusByTestSetId(testSetId, enable);
		return !(result.stream().anyMatch(TEST_SET_LINE_ID_STATUS.IN_QUEUE.getLabel()::equalsIgnoreCase)
				|| result.stream().anyMatch(TEST_SET_LINE_ID_STATUS.IN_PROGRESS.getLabel()::equalsIgnoreCase));

	}

	public Date findMaxExecutionEndDate(long testSetId) {
		return dao.findMaxExecutionEndDate(testSetId);
	}

	public void updatePdfGenerationEnableStatus(String testSetId, String enabled) {
		dao.updatePdfGenerationEnableStatus(testSetId, enabled);
	}

	@Transactional
	public List<String> getStatusByTestSetId(String testSetId) {
		return dao.getStatusByTestSetId(testSetId);
	}

	public List<Object[]> getStatusAndSeqNum(String testSetId) {
		return dao.getStatusAndSeqNum(testSetId);
	}

	@Transactional
	public void setPassAndFailScriptCount(String testRunId, FetchConfigVO fetchConfigVO) {
		dao.getPassAndFailScriptCount(testRunId, fetchConfigVO);
	}

	public void updateInProgressScriptStatus(String testSetLineId, Date startDate) {
		dao.updateInProgressScriptStatus(testSetLineId, startDate);
	}

	public Date findMinExecutionStartDate(long testSetId) {
		return dao.findMinExecutionStartDate(testSetId);
	}

	public List<Object[]> getConfigurationDetails(String testSetId) {
		return dao.getConfigurationDetails(testSetId);
	}

	public void updatePassedScriptLineStatus(ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			String test_script_param_id, String status, String message) throws ClassNotFoundException, SQLException {
		dao.updatePassedScriptLineStatus(test_script_param_id, status);
	}

	public void updatePassedScriptLineStatus(ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			String test_script_param_id, String status, String value, String message)
			throws ClassNotFoundException, SQLException {
		dao.updatePassedScriptLineStatus(test_script_param_id, status);
	}

	@Transactional
	public List<FetchMetadataVO> getMetaDataVOList(String testRunId, String testSetLineId, boolean finalPdf,
			boolean executeApi) {
		return dao.getMetaDataVOList(testRunId, testSetLineId, finalPdf, executeApi);
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

		if (testLines.getStatus().equalsIgnoreCase(TEST_SET_LINE_ID_STATUS.PASS.getLabel())) {
			return true;
		} else {
			return false;
		}
	}

	@Transactional
	public boolean checkRunStatusOfTestRunLevelDependantScript(Integer testSetLineId) {
		TestSetLine testLines = dao.checkTestSetLinesByTestSetLineId(testSetLineId);

		while (testLines.getStatus().equalsIgnoreCase(TEST_SET_LINE_ID_STATUS.IN_QUEUE.getLabel())
				|| testLines.getStatus().equalsIgnoreCase(TEST_SET_LINE_ID_STATUS.IN_PROGRESS.getLabel())) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			testLines = dao.checkTestSetLinesByTestSetLineId(testSetLineId);

		}

		if (testLines.getStatus().equalsIgnoreCase(TEST_SET_LINE_ID_STATUS.PASS.getLabel())) {
			return true;
		} else {
			return false;
		}
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

		if (sumQuantity.intValue() - sumExecuted.intValue() > 0) {
			dao.updateSubscriptionExecuteAndBalance(executed, balance, subsId);
		}
		List<Long> updatedBalance=subscriptionRepository.findBalanceByStatusAndUomAndStartDateEndDateOrderBySubscriptionId();
		if(updatedBalance.stream().findFirst().get()== 0) {
			dao.updateSubscriptionStatus(COMPLETED, subsId);
		}

	}

	public List<Object[]> findStartAndEndTimeForTestRun(String testRunId, String scriptStatus) {
		return dao.findStartAndEndTimeForTestRun(testRunId, scriptStatus);
	}

	public String getCentralRepoUrl(String customerName) {
		return dao.getCentralRepoUrl(customerName);
	}

	public void updateTestSetLineStatusForSanity(String testSetId) {
		dao.updateTestSetLineStatusForSanity(testSetId);
	}

	public List<TestSetLine> getAllTestSetLineRecord(String testSetId) {
		return dao.getAllTestSetLineRecord(testSetId);
	}

	public void updateEnableFlagForSanity(String testSetId) {
		dao.updateEnableFlagForSanity(testSetId);
	}

	public TestSet getTestRunDetails(String testSetId) {
		return dao.getTestRunDetails(testSetId);
	}

	public boolean doesActionContainsExcel(String scriptId) {
		return dao.doesActionContainsExcel(scriptId);
	}
	
	public TestSetAttribute getApiValueBySetIdAndAPIKey(String testSetId, String apiKey) {
		return dao.getApiValueBySetIdAndAPIKey(testSetId, apiKey);
	}

	public void insertRecordInTestSetAttribute(String testSetLineId, String string, String token, String executedBy) {
		dao.insertRecordInTestSetAttribute(testSetLineId, string, token, executedBy);
	}
	public int getApiValidationIdActionId() {
		return dao.getApiValidationIdActionId();
	}

	public List<Object> getApiValidationDataFromLookupsCode(int apiValidationId, List<Integer> list) {
		return dao.getApiValidationDataFromLookupsCode(apiValidationId, list);
	}

	public List<String> getExistingLookupCodeByValidationId(int apiValidationId, String lookUpCode) {
		return dao.getExistingLookupCodeByValidationId(apiValidationId, lookUpCode);
	}

	public void insertApiValidation(LookUpCode lookUpCodes) {
		dao.insertApiValidation(lookUpCodes);
	}

	public void updateApiValidation(LookUpCode listOfLookUpCodes) {
		dao.updateApiValidation(listOfLookUpCodes);
		
	}

	public List<LookUpCode> getExistingLookupListByValidationId(int apiValidationId, String lookUpCode) throws Exception {
		return dao.getExistingLookupListByValidationId(apiValidationId,lookUpCode);
	}
	public String getEnabledStatusByTestSetLineID(String testSetLineId) {
		return dao.getEnabledStatusByTestSetLineID(testSetLineId);
	}

	public ScriptMaster getScriptDetailsByScriptId(Integer scriptId) {
		return scriptMasterRepository.findById(scriptId).get();
	}
	
	public String getDirectoryPath() {
		return dao.getDirectoryPath();
	}

	public List<String> getAllModules() {
		return dao.getAllModules();
	}
	
	@Transactional
	public void getTestRunLinesDataByTestSetLineId(TestSetLine testSetLineObj, String deletedBy) {
	
		TestSetLine newTestSetLineObj = dao.getTestSetLine(testSetLineObj.getTestRunScriptId().toString());
		appContext.getBean(this.getClass()).deleteScriptFromTestRun(newTestSetLineObj,deletedBy);
	}
	
	public void deleteScriptFromTestRun(TestSetLine testSetLineObj, String deletedBy) {
		dao.deleteTestSetScriptParamRecordsByTestSetLineId(testSetLineObj);
		dao.deleteTestSetLinesRecordsByTestSetLineId(testSetLineObj,deletedBy);
	}
	
	public TestSetLine getTestSetLineRecordsByTestSetLineId(String testSetLineId) {
		return dao.getTestSetLine(testSetLineId);
	}

	public void updateStatusOfPdfGeneration(String testSetId, String status) {
		dao.updateStatusOfPdfGeneration(testSetId,status);
	}
	
	public List<TestSetScriptParam> getTestSetScriptParamContainsExcel(Integer testsetlineid) {
		return dao.getTestSetScriptParamContainsExcel(testsetlineid);
	}
	
	public void UpdateTestSetScriptParamContainsExcel(Integer testscriptparamid) {
		dao.UpdateTestSetScriptParamContainsExcel(testscriptparamid);
	}
	
	public List<String> findLookUpCodesUsingLookUpName(String lookUpName) {
		return lookUpCodeJpaRepository.findLookUpCodesUsingLookUpName(lookUpName);
	}
	
	public List<String> getActionByTargetApplication(String targetApplication) {
		return lookUpCodeJpaRepository.getActionByTargetApplication(targetApplication);
	}
	
	public String getActionMeaningScriptIdAndLineNumber(Integer scriptId, Integer scriptMetaDataId) {
		return lookUpCodeJpaRepository.getActionMeaningScriptIdAndLineNumber(scriptId,scriptMetaDataId);
	}
	
	public String getMeaningByTargetCode(String lookUpCode, String lookUpName) {
		return lookUpCodeJpaRepository.getMeaningByTargetCode(lookUpCode,lookUpName);
	}
	
	public String getLookUpCodeByMeaning(String meaning, String lookUpName) {
		try {
			return lookUpCodeJpaRepository.findByMeaningAndLookUpName(meaning,lookUpName).getLookUpCode();			
		}catch(Exception e) {
			return meaning;
		}
	}
	
	public TestSetLine getTestSetLineBySequenceNumber(String testSetId, String seqNumber) {
		return testSetLinesRepository.findBySeqNum(Integer.parseInt(testSetId), Integer.parseInt(seqNumber));
	}
	public String getCustomerNameFromCustomerId(int customerId) {
		return customerRepository.findByCustomerId(customerId).getCustomerName();
	}
	public List<String> getListOfCustomers(String userName) {
		return customerRepository.findListOfCustomers(userName);
	}
	public void updateTestSetLinesWarningMessage(String test_script_param_id, String error_message)
			throws ClassNotFoundException, SQLException {
		dao.updateTestSetLinesWarningMessage(test_script_param_id, error_message);
	}
}
