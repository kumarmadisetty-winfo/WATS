package com.winfo.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.hibernate.QueryException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.winfo.exception.WatsEBSException;
import com.winfo.model.ApplicationProperties;
import com.winfo.model.AuditScriptExecTrail;
import com.winfo.model.AuditStageLookup;
import com.winfo.model.Customer;
import com.winfo.model.LogDetailsTable;
import com.winfo.model.LookUp;
import com.winfo.model.LookUpCode;
import com.winfo.model.Project;
import com.winfo.model.ScriptMaster;
import com.winfo.model.ScriptMetaData;
import com.winfo.model.TestSet;
import com.winfo.model.TestSetAttribute;
import com.winfo.model.TestSetLine;
import com.winfo.model.TestSetScriptParam;
import com.winfo.repository.LogDetailsRepository;
import com.winfo.utils.Constants.BOOLEAN_STATUS;
import com.winfo.vo.CustomerProjectDto;
import com.winfo.vo.EmailParamDto;
import com.winfo.vo.FetchConfigVO;
import com.winfo.vo.FetchMetadataVO;
import com.winfo.vo.LookUpCodeVO;
import com.winfo.vo.LookUpVO;
import com.winfo.vo.ScriptDetailsDto;
import com.winfo.vo.Status;

@SuppressWarnings({ "deprecation", "unchecked" })
@Repository
@RefreshScope
public class DataBaseEntryDao {
	@PersistenceContext
	EntityManager em;
	
	public static final Logger logger = Logger.getLogger(DataBaseEntryDao.class);

	private static final String NULL_STRING = "null";

	private static final String CUSTOMER_DLT_QRY = "SELECT DISTINCT wtp.customer_id,\r\n" + " wtc.customer_number,\r\n"
			+ "  wtc.customer_name,\r\n" + " wtts.project_id,\r\n" + " wtp.project_name,\r\n"
			+ "  wtts.test_set_id,\r\n" + " wtts.TEST_SET_NAME test_run_name\r\n" + " from\r\n"
			+ " win_ta_test_set wtts,\r\n" + " win_ta_test_set_lines  wttsl,\r\n" + " win_ta_projects wtp,\r\n"
			+ " win_ta_customers wtc\r\n" + " WHERE 1=1\r\n" //+ " AND wttsl.test_set_id = wtts.test_set_id\r\n"
			+ " AND wtts.project_id = wtp.project_id\r\n" + " AND wtp.customer_id = wtc.customer_id\r\n"
			+ " AND wtts.test_set_id=";

	private static final String EXCEPTION_MSG = "Exception occured while fetching request count for test run script.";
	private static final String TEST_SET_LINE = "testSetLine";
	private static final String TEST_RUN_SCRIPT_ID = "testRunScriptId";
	private static final String TEST_SET_ID = "testRunId";
	private static final String TEST_RUN = "testRun";
	private static final String TR_MODE = "testRunMode";
	private static final String SIMPLE_DATE = "M/dd/yyyy HH:mm:ss";
	private static final String IN_PROGRESS = "In-Progress";
	private static final String[] SPECIAL_CHAR = { "_", ".", "'" };
	private static final String PASSED = "Passed";
	private static final String JPG = "jpg";
	private static final String PNG = "png";
	private static final String PASS = "Pass";
	private static final String FAIL = "Fail";
	private static final String NEW = "New";
	private static final String STATUS = "status";
	private static final String IN_QUEUE = "In-Queue";
	@Autowired
	private LogDetailsRepository logDetailsRepository;

	public TestSet getTestSetObjByTestSetId(Integer testSetId) {
		Session session = em.unwrap(Session.class);
		return session.find(TestSet.class, testSetId);
	}
	
	public void updateStartAndEndTimeForTestSetTable(String testSetId, Date startTime, Date endTime) {
		
		try {
			Query query = em.createQuery("Update TestSet set startTime= :tStartTime, endTime=:tEndTime where testRunId=:testSetId");
			query.setParameter("tStartTime", startTime);
			query.setParameter("tEndTime", endTime);
			query.setParameter("testSetId", Integer.parseInt(testSetId));
			query.executeUpdate();
		} catch (Exception e) {
			logger.error("Failed to update start time and end time in TestSet " + e.getMessage());
		}
		
	}

	public TestSetLine getScriptDataByLineID(int lineId) {
		Session session = em.unwrap(Session.class);
		return session.find(TestSetLine.class, lineId);
	}

	public LookUpCodeVO getLookupCode(String lookUpName, String lookupCode) {
		Session session = em.unwrap(Session.class);
		List<LookUpCode> listOfLookUpCode = session
				.createQuery(
						"from LookUpCode where lookUpName = '" + lookUpName + "' and lookUpCode = '" + lookupCode + "'")
				.getResultList();
		return listOfLookUpCode.isEmpty() ? null : new LookUpCodeVO(listOfLookUpCode.get(0));
	}

	public LookUpVO getLookUp(String lookUpName, Map<String, LookUpCodeVO> mapOfData) {
		Session session = em.unwrap(Session.class);
		List<LookUp> listOfLookUp = session.createQuery("from LookUp where lookUpName = '" + lookUpName + "'")
				.getResultList();
		LookUp lookUpObj = listOfLookUp.isEmpty() ? null : listOfLookUp.get(0);
		return new LookUpVO(lookUpObj, mapOfData);
	}

	public List<Integer> getListOfLineIdByTestSetId(int testSetId) {
		Session session = em.unwrap(Session.class);
		return session.createQuery("select testRunScriptId from TestSetLine where testRun.testRunId = " + testSetId)
				.getResultList();
	}

	public List<ScriptMaster> getScriptMasterListByScriptId(int scriptId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ScriptMaster> cq = cb.createQuery(ScriptMaster.class);
		Root<ScriptMaster> from = cq.from(ScriptMaster.class);
		Predicate condition = cb.equal(from.get("scriptId"), scriptId);
		cq.where(condition);
		Query query = em.createQuery(cq);
		return query.getResultList();
	}

	public String getConfiNameByConfigId(Integer configId) {
		Session session = em.unwrap(Session.class);
		return (String) session
				.createNativeQuery("select config_name from win_ta_config where configuration_id ='" + configId + "'")
				.getSingleResult();
	}

	public List<Object[]> getProjectNameById(int projectId) {
		Session session = em.unwrap(Session.class);
		return session
				.createNativeQuery(
						"select project_name, wats_package from win_ta_projects where project_id =" + projectId)
				.getResultList();
	}

	public List<ScriptMetaData> getScriptMetaDataList(Integer scriptId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ScriptMetaData> cq = cb.createQuery(ScriptMetaData.class);
		Root<ScriptMetaData> from = cq.from(ScriptMetaData.class);
		Predicate condition = cb.equal(from.get("scriptMaster").get("scriptId"), scriptId);
		cq.where(condition);
		Query query = em.createQuery(cq);
		return query.getResultList();
	}

	public List<TestSetScriptParam> getScriptParamList(Integer testSetLineId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TestSetScriptParam> cq = cb.createQuery(TestSetScriptParam.class);
		Root<TestSetScriptParam> from = cq.from(TestSetScriptParam.class);
		Predicate condition = cb.equal(from.get(TEST_SET_LINE).get(TEST_RUN_SCRIPT_ID), testSetLineId);
		cq.where(condition);
		Query query = em.createQuery(cq);
		return query.getResultList();
	}

	public void updatePassedScriptLineStatus(String testScriptParamId, String status) {
		try {
			TestSetScriptParam testSetScriptParam = em.find(TestSetScriptParam.class, Integer.parseInt(testScriptParamId));
			testSetScriptParam.setUpdateDate(new Date());
			testSetScriptParam.setLineExecutionStatus(status);
			em.merge(testSetScriptParam);
		} catch (Exception e) {
			logger.error("Failed to update passed script line status " + e.getMessage());
		}
	}

	public String getProductVersionByScriptId(Integer scriptId) {
		String productVersion = "";
		String sqlQuery = "select product_version from WIN_TA_SCRIPT_MASTER where script_id=" + scriptId;

		try {
			Session session = em.unwrap(Session.class);
			Query query = session.createSQLQuery(sqlQuery);
			productVersion = (String) query.getResultList().get(0);
		} catch (Exception e) {
			logger.error("Failed to get product version " + e.getMessage());
		}

		return productVersion;
	}

	public String getErrorMessage(String sndo, String scriptName, String testRunName) {
		String errorMessage = "";
		String sqlQuery = "SELECT PARAM.LINE_ERROR_MESSAGE "
				+ "FROM WIN_TA_TEST_SET_SCRIPT_PARAM PARAM,WIN_TA_TEST_SET_LINES LINES,WIN_TA_TEST_SET TS "
				+ "WHERE TS.TEST_SET_ID = LINES.TEST_SET_ID " + "AND LINES.TEST_SET_LINE_ID = PARAM.TEST_SET_LINE_ID "
				+ "AND TS.TEST_SET_ID = (SELECT TEST_SET_ID FROM WIN_TA_TEST_SET WHERE UPPER(TEST_SET_NAME)=UPPER('"
				+ testRunName + "'))" + "AND UPPER(LINES.SCRIPT_NUMBER) = UPPER('" + scriptName + "') "
				+ "AND LINES.SEQ_NUM = " + sndo + " " + "AND PARAM.LINE_ERROR_MESSAGE IS NOT NULL";

		try {
			Session session = em.unwrap(Session.class);
			Query query = session.createSQLQuery(sqlQuery);
			errorMessage = (String) query.getResultList().get(0);
		} catch (Exception e) {
			logger.error("Failed to get error message " + e.getMessage());
		}

		return errorMessage;
	}

	public void updateInProgressScriptStatus(String testSetLineId) {
		try {
			TestSetLine testLines = em.find(TestSetLine.class, Integer.parseInt(testSetLineId));
			if (testLines != null) {
				testLines.setStatus(IN_PROGRESS.toUpperCase());
				em.merge(testLines);
			}
		} catch (Exception e) {
			logger.error(" Failed to update in progress script status " + e.getMessage());
		}
	}

	public void updateStartTime(String lineId, String testSetId, Date startTime1) {
		Format startformat = new SimpleDateFormat(SIMPLE_DATE);
		String startTime = startformat.format(startTime1);
		try {
			Session session = em.unwrap(Session.class);
			Query query = session
					.createSQLQuery("Update WATS_PROD.WIN_TA_TEST_SET_LINES  SET EXECUTION_START_TIME=TO_TIMESTAMP('"
							+ startTime + "','MM/DD/YYYY HH24:MI:SS') WHERE TEST_SET_ID=" + testSetId
							+ " AND TEST_SET_LINE_ID = " + lineId);
			query.executeUpdate();
		} catch (Exception e) {
			logger.error("Failed to update start time " + e.getMessage());
	}
	}

	public int getNextExecutionNum() {
		Session session = em.unwrap(Session.class);
		String sql = "SELECT WIN__TA_EXECUTION_ID_SEQ.NEXTVAL FROM DUAL";
		SQLQuery<?> query = session.createSQLQuery(sql);

		List<?> results = query.list();
		if (!results.isEmpty()) {
			logger.info("Execution Id " +results.get(0));
			BigDecimal bigDecimal = (BigDecimal) results.get(0);
			return Integer.parseInt(bigDecimal.toString());
		} else {
			return 0;
		}
	}

	public void updateFailedImages(ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			String testScriptParamId, CustomerProjectDto customerDetails) throws IOException {
		String folder = (fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + customerDetails.getCustomerName()
				+ File.separator

				+ customerDetails.getTestSetName() + File.separator + fetchMetadataVO.getSeqNum() + SPECIAL_CHAR[0]

				+ fetchMetadataVO.getLineNumber() + SPECIAL_CHAR[0] + fetchMetadataVO.getScenarioName()
				+ SPECIAL_CHAR[0]

				+ fetchMetadataVO.getScriptNumber() + SPECIAL_CHAR[0] + customerDetails.getTestSetName()
				+ SPECIAL_CHAR[0]

				+ fetchMetadataVO.getLineNumber() + SPECIAL_CHAR[0] + PASSED);

		String jpgFile = folder.concat(SPECIAL_CHAR[1] + JPG);
		String pngFile = folder.concat(SPECIAL_CHAR[1] + PNG);

		File file = new File(jpgFile).exists() ? new File(jpgFile) : new File(pngFile);
		byte[] screenshotArray = new byte[(int) file.length()];
		try (FileInputStream fileInputStream = new FileInputStream(file);) {
			logger.debug("File Input Stream " + fileInputStream.read(screenshotArray));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String sql = "Update WIN_TA_TEST_SET_SCRIPT_PARAM  SET SCREENSHOT= :screenshot where TEST_SCRIPT_PARAM_ID='"
				+ testScriptParamId + "'";
		Query query = em.unwrap(Session.class).createSQLQuery(sql);
		query.setParameter("screenshot", screenshotArray);
		query.executeUpdate();
	}

	public Map<String, Map<String, TestSetScriptParam>> getTestRunMap(String testRunId) {
		Map<String, Map<String, TestSetScriptParam>> map = new HashMap<>();
		String sql = "from TestSetLine where testRun=:testSet";
		Integer testRunId2 = Integer.parseInt(testRunId);
		Query query = em.createQuery(sql);
		query.setParameter("testSet", em.find(TestSet.class, testRunId2));
		List<TestSetLine> testSetLinesList = query.getResultList();
		for (TestSetLine test_set_line : testSetLinesList) {
			Map<String, TestSetScriptParam> map2 = getTestScriptMap(test_set_line);
			map.put(String.valueOf(test_set_line.getSeqNum()), map2);

		}
		return map;

	}

	public Map<String, TestSetScriptParam> getTestScriptMap(TestSetLine testSetLine) {
		String sql = "from TestSetScriptParam where testSetLine=:testSetLines";
		Query query = em.createQuery(sql);
		query.setParameter("testSetLines", testSetLine);
		List<TestSetScriptParam> testScriptParamList = query.getResultList();
		Map<String, TestSetScriptParam> map2 = new HashMap<>();
		for (TestSetScriptParam scriptParam : testScriptParamList) {
			map2.put(String.valueOf(scriptParam.getLineNumber()), scriptParam);
		}
		return map2;
	}

	public TestSetLine getTestSetLine(String testSetLineId) {
		return em.find(TestSetLine.class, Integer.parseInt(testSetLineId));
	}

	public void getDependentScriptNumbers(Map<String, List<ScriptDetailsDto>> dependentScriptMap,
			List<Integer> dependentList) {
		String sql = "Select script_id,dependency from ScriptMaster where script_id in (:dependentList)";
		Query query = em.unwrap(Session.class).createQuery(sql).setParameterList("dependentList", dependentList);

		List<Object[]> scriptList = query.getResultList();
		Map<Integer, Integer> map = new HashMap<>();

		for (Object[] obj : scriptList) {
			map.put((Integer) obj[0], (Integer) obj[1]);
		}

		for (Entry<String, List<ScriptDetailsDto>> element : dependentScriptMap.entrySet()) {
			element.getValue().get(0)
					.setDependencyScriptNumber(map.get(Integer.parseInt(element.getValue().get(0).getScriptId())));

		}

	}

	public void getTestRunLevelDependentScriptNumbers(Map<String, List<ScriptDetailsDto>> dependentScriptMap,
			List<Integer> dependentList, String testSetId) {
		String sql = "Select test_set_line_id,dependency_tr from win_ta_test_set_lines where test_set_line_id in (:dependentList) and test_set_id = :test_set_id  and dependency_tr is not null";
		Query query = em.unwrap(Session.class).createSQLQuery(sql).setParameterList("dependentList", dependentList)
				.setParameter("test_set_id", testSetId);

		List<Object[]> scriptList = query.getResultList();
		Map<Integer, Integer> map = new HashMap<>();

		for (Object[] obj : scriptList) {
			map.put(Integer.parseInt(obj[0].toString()), Integer.parseInt(obj[1].toString()));
		}

		for (Entry<String, List<ScriptDetailsDto>> element : dependentScriptMap.entrySet()) {
			element.getValue().get(0)
					.setDependencyScriptNumber(map.get(Integer.parseInt(element.getValue().get(0).getTestSetLineId())));
		}

	}

	public void getStatus(Integer dependentScriptNo, Integer testSetId, Map<Integer, Status> scriptStatus,
			int testRunDependencyCount) {
		String sq1;
		if (testRunDependencyCount > 0) {
			sq1 = "select status from win_ta_test_set_lines where test_set_id=:test_set_id and test_set_line_id=:dependentScriptNo";
		} else {
			sq1 = "select status from win_ta_test_set_lines where test_set_id=:test_set_id and script_id=:dependentScriptNo";
		}

		Query query = em.unwrap(Session.class).createSQLQuery(sq1);
		query.setParameter("test_set_id", testSetId);
		query.setParameter("dependentScriptNo", dependentScriptNo);

		List<String> list = query.getResultList();

		Status status = new Status();
		int awaitCount = 0;
		if (list != null) {
			if ((!list.isEmpty()) && (!(list.contains(FAIL) || list.contains(FAIL.toUpperCase())))
					&& (!(list.contains(NEW) || list.contains(NEW.toUpperCase())))) {
				if ((list.contains(IN_PROGRESS) || list.contains(IN_PROGRESS.toUpperCase()))
						|| (list.contains(IN_QUEUE) || list.contains(IN_QUEUE.toUpperCase()))) {
					status.setStatusMsg("Wait");
					for (String stat : list) {
						if (!stat.equalsIgnoreCase(PASS)) {
							awaitCount++;
						}
					}
					status.setInExecutionCount(awaitCount);
					scriptStatus.put(dependentScriptNo, status);
				} else {
					status.setStatusMsg(PASS);
					scriptStatus.put(dependentScriptNo, status);
				}
			} else {
				status.setStatusMsg(FAIL);
				scriptStatus.put(dependentScriptNo, status);
			}
		} else {
			status.setStatusMsg(FAIL);
			scriptStatus.put(dependentScriptNo, status);
		}

	}

	public int getTestRunDependentCount(String testSetId) {
		String sq1 = "select count(*) from win_ta_test_set_lines where dependency_tr is not null and test_set_id = :test_set_id";
		Query query = em.unwrap(Session.class).createSQLQuery(sq1);
		query.setParameter("test_set_id", testSetId);
		return Integer.parseInt(query.getSingleResult().toString());
	}

	public String getPackage(String args) {
		TestSet testSet = em.unwrap(Session.class).find(TestSet.class, Integer.parseInt(args));
		Project project = em.unwrap(Session.class).find(Project.class, testSet.getProjectId());
		return project.getWatsPackage();
	}

	public Customer getCustomer(String args) {
		TestSet testSet = em.unwrap(Session.class).find(TestSet.class, Integer.parseInt(args));
		Project project = em.unwrap(Session.class).find(Project.class, testSet.getProjectId());
		return em.unwrap(Session.class).find(Customer.class, project.getCustomerId());
	}

	public String getTestSetMode(Long testSetId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> query = cb.createQuery(String.class);
		Root<TestSet> root = query.from(TestSet.class);
		Predicate condition = cb.equal(root.get(TEST_SET_ID), testSetId);
		query.select(root.get(TR_MODE)).where(condition);
		return em.createQuery(query).getSingleResult();

	}

	public Integer findAuditStageIdByName(String stageName) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
		Root<AuditStageLookup> from = cq.from(AuditStageLookup.class);
		Predicate condition = cb.equal(from.get("stageName"), stageName);
		cq.select(from.get("stageId")).where(condition);
		return em.createQuery(cq).getSingleResult();

	}

	public AuditScriptExecTrail insertAuditScriptExecTrail(AuditScriptExecTrail auditScriptExecTrail) {
		em.persist(auditScriptExecTrail);
		return auditScriptExecTrail;

	}

	public String getTestSetPdfGenerationEnableStatus(Long testSetId) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<String> query = cb.createQuery(String.class);
			Root<TestSet> root = query.from(TestSet.class);
			Predicate condition = cb.equal(root.get(TEST_SET_ID), testSetId);
			query.select(root.get("pdfGenerationEnabled")).where(condition);
			return em.createQuery(query).getSingleResult();
		} catch (Exception e) {
			throw new WatsEBSException(500,
					"Exception occured while getting status of PDF Generation for the Test Run", e);
		}

	}

	public List<String> getStepsStatusByScriptId(int testSetLineId) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<TestSetScriptParam> from = cq.from(TestSetScriptParam.class);

		Predicate condition = cb.equal(from.get(TEST_SET_LINE).get(TEST_RUN_SCRIPT_ID), testSetLineId);
		cq.where(condition);
		Query query = em.createQuery(cq.select(from.get("lineExecutionStatus")));

		return query.getResultList();
	}

	public List<String> getStepsStatusForSteps(List<Integer> statusList) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<TestSetScriptParam> from = cq.from(TestSetScriptParam.class);
		Expression<String> inExpression = from.get("testRunScriptParamId");
		Predicate inPredicate = inExpression.in(statusList);
		cq.where(inPredicate);
		Query query = em.createQuery(cq.select(from.get("lineExecutionStatus")));
		return query.getResultList();
	}

	public TestSetLine getScript(long testSetId, long testSetLineId) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TestSetLine> cq = cb.createQuery(TestSetLine.class);
		Root<TestSetLine> from = cq.from(TestSetLine.class);

		Predicate condition1 = cb.equal(from.get(TEST_RUN_SCRIPT_ID), testSetLineId);
		Predicate condition2 = cb.equal(from.get(TEST_RUN).get(TEST_SET_ID), testSetId);
		Predicate condition = cb.and(condition1, condition2);
		cq.where(condition);
		Query query = em.createQuery(cq.select(from));
		return (TestSetLine) query.getSingleResult();

	}

	public List<Object[]> getConfigurationDetails(String testSetId) {
		try {
			Query query = em
					.createNativeQuery("select cm.KEY_NAME,cl.VALUE_NAME,cm.DEFAULT_VALUE from WIN_TA_CONFIG_LINES cl "
							+ "join WIN_TA_CONFIG_MASTER cm on cl.KEY_ID=cm.KEY_ID "
							+ "join win_ta_test_set ts on ts.CONFIGURATION_ID=cl.CONFIGURATION_ID where ts.TEST_SET_ID=:testSetId");
			query.setParameter("testSetId", testSetId);
			return query.getResultList();
		} catch (Exception e) {
			throw new WatsEBSException(500, "Exception occured while fetching configuration details", e);
		}
	}

	public void updatePdfGenerationEnableStatus(String testSetId, String enabled) {
		try {
			Query query = em.createQuery(
					"Update TestSet set pdfGenerationEnabled='" + enabled + "' where testRunId='" + testSetId + "'");
			query.executeUpdate();
		} catch (Exception e) {
			logger.error("Error Updation PDF Generation Status " + e.getMessage());
		}
	}

	public Date findMaxExecutionEndDate(long testSetId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Date> cq = cb.createQuery(Date.class);
		Root<TestSetLine> from = cq.from(TestSetLine.class);
		Predicate condition = cb.equal(from.get(TEST_RUN).get(TEST_SET_ID), testSetId);

		cq.select(cb.greatest(from.<Date>get("executionEndTime"))).where(condition);
		return em.createQuery(cq).getSingleResult();

	}

	public Date findMinExecutionStartDate(long testSetId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Date> cq = cb.createQuery(Date.class);
		Root<TestSetLine> from = cq.from(TestSetLine.class);
		Predicate condition = cb.equal(from.get(TEST_RUN).get(TEST_SET_ID), testSetId);

		cq.select(cb.least(from.<Date>get("executionStartTime"))).where(condition);
		return em.createQuery(cq).getSingleResult();

	}

	public Date findStepMaxUpdatedDate(String testSetLineId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Date> cq = cb.createQuery(Date.class);
		Root<TestSetScriptParam> from = cq.from(TestSetScriptParam.class);
		Predicate condition = cb.equal(from.get(TEST_SET_LINE).get(TEST_RUN_SCRIPT_ID), testSetLineId);

		cq.select(cb.greatest(from.<Date>get("updateDate"))).where(condition);
		return em.createQuery(cq).getSingleResult();

	}

	public Integer findFirstStepIdInScript(String testSetLineId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
		Root<TestSetScriptParam> from = cq.from(TestSetScriptParam.class);
		Predicate condition = cb.equal(from.get(TEST_SET_LINE).get(TEST_RUN_SCRIPT_ID), testSetLineId);
		cq.select(from.get("testRunScriptParamId")).where(condition);
		cq.orderBy(cb.asc(from.get("lineNumber")));
		return em.createQuery(cq).setMaxResults(1).getSingleResult();

	}

	public ScriptMaster findScriptMasterByScriptId(int scriptId) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ScriptMaster> cq = cb.createQuery(ScriptMaster.class);
		Root<ScriptMaster> from = cq.from(ScriptMaster.class);

		Predicate condition1 = cb.equal(from.get("scriptId"), scriptId);

		cq.where(condition1);
		Query query = em.createQuery(cq);
		return (ScriptMaster) query.getSingleResult();
	}

	public TestSetLine checkTestSetLinesByScriptId(int testSetId, int scriptId) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TestSetLine> cq = cb.createQuery(TestSetLine.class);
		Root<TestSetLine> from = cq.from(TestSetLine.class);

		Predicate condition1 = cb.equal(from.get("scriptId"), scriptId);
		Predicate condition2 = cb.equal(from.get(TEST_RUN).get(TEST_SET_ID), testSetId);
		Predicate condition = cb.and(condition1, condition2);
		cq.where(condition);
		Query query = em.createQuery(cq);
		TestSetLine result = (TestSetLine) query.getSingleResult();
		em.refresh(result);

		return result;
	}

	public TestSetLine checkTestSetLinesByTestSetLineId(int testSetLineId) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TestSetLine> cq = cb.createQuery(TestSetLine.class);
		Root<TestSetLine> from = cq.from(TestSetLine.class);

		Predicate condition = cb.equal(from.get(TEST_RUN_SCRIPT_ID), testSetLineId);
		cq.where(condition);
		Query query = em.createQuery(cq);
		TestSetLine result = (TestSetLine) query.getSingleResult();
		em.refresh(result);

		return result;
	}

	public List<String> getTestSetLinesStatusByTestSetId(long testSetId, Boolean enable) {
		List<String> result = null;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TestSetLine> cq = cb.createQuery(TestSetLine.class);
			Root<TestSetLine> from = cq.from(TestSetLine.class);

			Predicate condition1 = cb.equal(from.get(TEST_RUN).get(TEST_SET_ID), testSetId);
			Predicate condition2 = null;
			Predicate condition = null;
			if (enable != null) {
				condition2 = cb.equal(from.get("enabled"),
						enable ? BOOLEAN_STATUS.TRUE.getLabel() : BOOLEAN_STATUS.FALSE.getLabel());
				condition = cb.and(condition1, condition2);
			} else {
				condition = condition1;
			}
			cq.where(condition);
			Query query = em.createQuery(cq.select(from.get(STATUS)));
			result = query.getResultList();
		} catch (Exception e) {
			throw new WatsEBSException(500, "Exception occured while fetching the status for test run pdfs", e);
		}

		return result;
	}

	public List<String> getStatusByTestSetId(String testSetId) {

		String sqlQry = "select STATUS FROM win_ta_test_set_lines where test_set_id=" + testSetId;
		List<String> result = null;
		Session session = em.unwrap(Session.class);
		try {
			Query query = session.createSQLQuery(sqlQry);
			result = query.getResultList();
		} catch (Exception e) {
			throw new WatsEBSException(604, "Unable to read status from win_ta_test_set_lines");
		}
		return result;
	}

	public void getPassAndFailScriptCount(String testRunId, FetchConfigVO fetchConfigVO) {
		String sqlQuery = "select count(status) from win_ta_test_set_lines where test_set_id=" + testRunId
				+ "and status='Fail'";
		String sqlPassQuery = "select count(status) from win_ta_test_set_lines where test_set_id=" + testRunId
				+ "and status='Pass'";

		Session session = em.unwrap(Session.class);

		Integer failCount = 0;
		Integer passCount = 0;
		try {
			NativeQuery<BigDecimal> query = session.createSQLQuery(sqlQuery);

			List<BigDecimal> results = query.list();
			if (results != null && !results.isEmpty()) {

				BigDecimal bigDecimal = results.get(0);
				failCount = Integer.parseInt(bigDecimal.toString());
			}
		} catch (Exception e) {
			throw new WatsEBSException(600, "Unable to read the fail count");
		}
		try {
			NativeQuery<BigDecimal> query1 = session.createSQLQuery(sqlPassQuery);

			List<BigDecimal> results1 = query1.list();
			if (results1 != null && !results1.isEmpty()) {

				BigDecimal bigDecimal1 = results1.get(0);
				passCount = Integer.parseInt(bigDecimal1.toString());
			}
			fetchConfigVO.setFailcount(failCount);
			fetchConfigVO.setPasscount(passCount);
		} catch (Exception e) {
			throw new WatsEBSException(600, "Unable to read the pass count");
		}

	}

	public CustomerProjectDto getCustomerDetails(String testSetId) {
		CustomerProjectDto customerDetails = null;
		String qry = CUSTOMER_DLT_QRY + testSetId;
		try {
			Session session = em.unwrap(Session.class);
			Query query = session.createSQLQuery(qry);
			Object[] result = (Object[]) query.getSingleResult();
			customerDetails = new CustomerProjectDto();
			customerDetails
					.setCustomerId(NULL_STRING.equals(String.valueOf(result[0])) ? null : String.valueOf(result[0]));
			customerDetails.setCustomerNumber(
					NULL_STRING.equals(String.valueOf(result[1])) ? null : String.valueOf(result[1]));
			customerDetails
					.setCustomerName(NULL_STRING.equals(String.valueOf(result[2])) ? null : String.valueOf(result[2]));
			customerDetails
					.setProjectId(NULL_STRING.equals(String.valueOf(result[3])) ? null : String.valueOf(result[3]));
			customerDetails
					.setProjectName(NULL_STRING.equals(String.valueOf(result[4])) ? null : String.valueOf(result[4]));
			customerDetails
					.setTestSetId(NULL_STRING.equals(String.valueOf(result[5])) ? null : String.valueOf(result[5]));
			customerDetails
					.setTestSetName(NULL_STRING.equals(String.valueOf(result[6])) ? null : String.valueOf(result[6]));
		} catch (Exception e) {
			throw new WatsEBSException(500, "Exception occured while fetching all steps details for test run.",
					e);
		}
		return customerDetails;

	}

	public List<ScriptDetailsDto> getScriptDetails(String testRunId, String testSetLineId, boolean finalPdf,
			boolean executeApi) {

		List<ScriptDetailsDto> listOfTestRunExecutionVo = new ArrayList<>();
		String whereClause = "";
		if (testSetLineId != null) {
			whereClause = whereClause + "       AND wttsl.test_set_line_id=" + testSetLineId + "\r\n";
		}
		if (finalPdf) {
			whereClause = whereClause + "      and  (upper(status) in ('PASS','FAIL'))\r\n";
		}

		if (executeApi) {
			whereClause = whereClause + "      and wttsl.enabled = 'Y'\r\n";
			whereClause = whereClause + "      and  (upper(status) in ('NEW','FAIL','IN-QUEUE'))\r\n";
		}

		String sqlQuery = "SELECT wttsl.test_set_line_id,\r\n" + "wtsmdata.script_id,\r\n"
				+ "           wtsmdata.script_number,\r\n" + "           wtsmdata.line_number,\r\n"
				+ "           CASE WHEN INSTR(wtsmdata.input_parameter,')',1,1) >4\r\n"
				+ "               THEN wtsmdata.input_parameter\r\n"
				+ "               ELSE regexp_replace( wtsmdata.input_parameter, '[*#()]', '') END input_parameter,\r\n"
				+ "           wtsmdata.input_value,\r\n" + "           wtsmdata.action,\r\n"
				+ "           wtsmdata.xpath_location,\r\n" + "           wtsmdata.xpath_location1,\r\n"
				+ "           wtsmdata.field_type,\r\n" + "           wtsmdata.hint,\r\n"
				+ "           ma.SCENARIO_NAME,\r\n" + "    decode(ma.dependency, null, 'N', 'Y') dependency\r\n"
				+ "          , wttsl.SEQ_NUM\r\n"
				+ ",wtsmdata.LINE_EXECUTION_STATUS\r\n, wtsmdata.TEST_SCRIPT_PARAM_ID\r\n"
				+ ", wtsmdata.Line_ERROR_MESSAGE\r\n,  wtsmdata.test_run_param_desc\r\n"
				+ "          ,ex_st.EXECUTED_BY    EXECUTED_BY\r\n" + "          ,ma.TARGET_APPLICATION\r\n"
				+ " ,wttsl.dependency_tr\r\n" 
				+ "          , wttsl.ISSUE_KEY\r\n"
				+ " ,mam.UNIQUE_MANDATORY\r\n"
				+", wtp.ORACLE_RELEASE_YEAR\r\n"
				+ "      from\r\n" + "      execute_status ex_st,\r\n"
				+ "      win_ta_test_set        wtts,\r\n" + "    win_ta_script_master ma,\r\n"
				+ "           win_ta_test_set_lines  wttsl,\r\n"
				+ "           win_ta_test_set_script_param wtsmdata,\r\n" + "           win_ta_projects        wtp,\r\n"
				+ "           win_ta_customers       wtc\r\n" + "     WHERE 1=1\r\n"
				+ "     AND wtts.TEST_SET_ID = EX_ST.TEST_RUN_ID(+)\r\n" + "    and ma.script_id = wttsl.script_id\r\n"
				+ "    and ma.script_number = wttsl.script_number\r\n"
				+ "      -- AND wtts.test_set_id = :p_test_set_id\r\n"
				+ "       AND wttsl.test_set_id = wtts.test_set_id\r\n"
				+ "       AND wttsl.script_id = wtsmdata.script_id\r\n"
				+ " 	  AND mam.script_id = wtsmdata.script_id\r\n"
			    + " 	  AND mam.line_number = wtsmdata.line_number\r\n"
				+ "       AND wtsmdata.test_set_line_id =wttsl.test_set_line_id\r\n"
				+ "       AND wtts.project_id = wtp.project_id\r\n" + "       AND wtp.customer_id = wtc.customer_id\r\n"
				+ "       AND wtts.test_set_id=" + testRunId + "\r\n" + whereClause + "       order by\r\n"
				+ "       wttsl.SEQ_NUM,\r\n" + "         -- wtsmdata.script_number,\r\n"
				+ "          wttsl.script_id,\r\n" + "          wtsmdata.line_number asc";

		try {
			Session session = em.unwrap(Session.class);
			Query query = session.createSQLQuery(sqlQuery);
			List<Object[]> resultList = query.getResultList();
			Iterator<Object[]> itr = resultList.iterator();
			while (itr.hasNext()) {
				Object[] obj = itr.next();
				ScriptDetailsDto scriptDetailsDto = new ScriptDetailsDto();

				scriptDetailsDto
						.setTestSetLineId(NULL_STRING.equals(String.valueOf(obj[0])) ? null : String.valueOf(obj[0]));
				scriptDetailsDto
						.setScriptId(NULL_STRING.equals(String.valueOf(obj[1])) ? null : String.valueOf(obj[1]));
				scriptDetailsDto
						.setScriptNumber(NULL_STRING.equals(String.valueOf(obj[2])) ? null : String.valueOf(obj[2]));
				scriptDetailsDto
						.setLineNumber(NULL_STRING.equals(String.valueOf(obj[3])) ? null : String.valueOf(obj[3]));
				scriptDetailsDto
						.setInputParameter(NULL_STRING.equals(String.valueOf(obj[4])) ? null : String.valueOf(obj[4]));
				scriptDetailsDto
						.setInputValue(NULL_STRING.equals(String.valueOf(obj[5])) ? null : String.valueOf(obj[5]));

				scriptDetailsDto.setAction(NULL_STRING.equals(String.valueOf(obj[6])) ? null : String.valueOf(obj[6]));

				scriptDetailsDto
						.setXpathLocation(NULL_STRING.equals(String.valueOf(obj[7])) ? null : String.valueOf(obj[7]));
				scriptDetailsDto
						.setXpathLocation1(NULL_STRING.equals(String.valueOf(obj[8])) ? null : String.valueOf(obj[8]));

				scriptDetailsDto
						.setFieldType(NULL_STRING.equals(String.valueOf(obj[9])) ? null : String.valueOf(obj[9]));

				scriptDetailsDto
						.setScenarioName(NULL_STRING.equals(String.valueOf(obj[11])) ? null : String.valueOf(obj[11]));

				scriptDetailsDto
						.setDependency(NULL_STRING.equals(String.valueOf(obj[12])) ? null : String.valueOf(obj[12]));

				scriptDetailsDto
						.setSeqNum(NULL_STRING.equals(String.valueOf(obj[13])) ? null : String.valueOf(obj[13]));

				scriptDetailsDto
						.setStatus(NULL_STRING.equals(String.valueOf(obj[14])) ? null : String.valueOf(obj[14]));

				scriptDetailsDto.setTestScriptParamId(
						NULL_STRING.equals(String.valueOf(obj[15])) ? null : String.valueOf(obj[15]));

				scriptDetailsDto
						.setLineErrorMsg(NULL_STRING.equals(String.valueOf(obj[16])) ? null : String.valueOf(obj[16]));

				scriptDetailsDto.setTestRunParamDesc(
						NULL_STRING.equals(String.valueOf(obj[17])) ? null : String.valueOf(obj[17]));

				scriptDetailsDto
						.setExecutedBy(NULL_STRING.equals(String.valueOf(obj[18])) ? null : String.valueOf(obj[18]));

				scriptDetailsDto.setTargetApplicationName(
						NULL_STRING.equals(String.valueOf(obj[19])) ? null : String.valueOf(obj[19]));

				scriptDetailsDto.setDependencyScriptNumber(
						NULL_STRING.equals(String.valueOf(obj[20])) ? null : Integer.valueOf((String) obj[20]));
				
				scriptDetailsDto.setIssueKey(
						NULL_STRING.equals(String.valueOf(obj[21])) ? null : String.valueOf(obj[21]));
				
				scriptDetailsDto.setOracleReleaseYear(
						NULL_STRING.equals(String.valueOf(obj[22])) ? null : String.valueOf(obj[22]));
				
				scriptDetailsDto.setUniqueMandatory(
						NULL_STRING.equals(String.valueOf(obj[23])) ? null : String.valueOf(obj[23]));
				
				listOfTestRunExecutionVo.add(scriptDetailsDto);
			}
		} catch (QueryException e) {
			e.printStackTrace();
			throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Error executing the query: " + e.getMessage(), e);
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
			throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Error accessing array element: " + e.getMessage(), e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Exception occured while fetching all steps details for test run", e);
		}
		return listOfTestRunExecutionVo;
	}

	public List<FetchMetadataVO> getMetaDataVOList(String testRunId, String testSetLineId, boolean finalPdf,
			boolean executeApi) {

		List<FetchMetadataVO> listOfTestRunExecutionVo = new ArrayList<>();
		String whereClause = "";
		if (testSetLineId != null) {
			whereClause = whereClause + "       AND wttsl.test_set_line_id=" + testSetLineId + "\r\n";
		}
		if (finalPdf) {
			whereClause = whereClause + "      and  (upper(status) in ('PASS','FAIL'))\r\n";
		}

		if (executeApi) {
			whereClause = whereClause + "      and wttsl.enabled = 'Y'\r\n";
			whereClause = whereClause + "      and  (upper(status) in ('NEW','FAIL','IN-QUEUE'))\r\n";
		}

		String sqlQuery = "SELECT wtp.customer_id,\r\n" + "           wtc.customer_number,\r\n"
				+ "           wtc.customer_name,\r\n" + "           wtts.project_id,\r\n"
				+ "           wtp.project_name,\r\n" + "           wttsl.test_set_id,\r\n"
				+ "           wttsl.test_set_line_id,\r\n" + "           wtsmdata.script_id,\r\n"
				+ "           wtsmdata.script_number,\r\n" + "           wtsmdata.line_number,\r\n"
				+ "           CASE WHEN INSTR(wtsmdata.input_parameter,')',1,1) >4\r\n"
				+ "               THEN wtsmdata.input_parameter\r\n"
				+ "               ELSE regexp_replace( wtsmdata.input_parameter, '[*#()]', '') END input_parameter,\r\n"
				+ "           wtsmdata.input_value,\r\n" + "           wtsmdata.action,\r\n"
				+ "           wtsmdata.xpath_location,\r\n" + "           wtsmdata.xpath_location1,\r\n"
				+ "           wtsmdata.field_type,\r\n" + "           wtsmdata.hint,\r\n"
				+ "           ma.SCENARIO_NAME,\r\n" + "    wttsl.dependency_tr\r\n"
				+ "          ,wtts.TEST_SET_NAME test_run_name, wttsl.SEQ_NUM\r\n"
				+ ",wtsmdata.LINE_EXECUTION_STATUS\r\n, wtsmdata.TEST_SCRIPT_PARAM_ID\r\n"
				+ ", wtsmdata.Line_ERROR_MESSAGE\r\n,  wtsmdata.test_run_param_desc\r\n"
				+ "          ,ex_st.EXECUTED_BY    EXECUTED_BY\r\n" + "          ,ma.TARGET_APPLICATION\r\n"
				+ "      from\r\n" + "      execute_status ex_st,\r\n" + "      win_ta_test_set        wtts,\r\n"
				+ "    win_ta_script_master ma,\r\n" + "           win_ta_test_set_lines  wttsl,\r\n"
				+ "           win_ta_test_set_script_param wtsmdata,\r\n" + "           win_ta_projects        wtp,\r\n"
				+ "           win_ta_customers       wtc\r\n" + "     WHERE 1=1\r\n"
				+ "     AND wtts.TEST_SET_ID = EX_ST.TEST_RUN_ID(+)\r\n" + "    and ma.script_id = wttsl.script_id\r\n"
				+ "    and ma.script_number = wttsl.script_number\r\n"
				+ "      -- AND wtts.test_set_id = :p_test_set_id\r\n"
				+ "       AND wttsl.test_set_id = wtts.test_set_id\r\n"
				+ "       AND wttsl.script_id = wtsmdata.script_id\r\n"
				+ "       AND wtsmdata.test_set_line_id =wttsl.test_set_line_id\r\n"
				+ "       AND wtts.project_id = wtp.project_id\r\n" + "       AND wtp.customer_id = wtc.customer_id\r\n"
				+ "       AND wtts.test_set_id=" + testRunId + "\r\n" + whereClause + "       order by\r\n"
				+ "       wttsl.SEQ_NUM,\r\n" + "         -- wtsmdata.script_number,\r\n"
				+ "          wttsl.script_id,\r\n" + "          wtsmdata.line_number asc";

		try {
			Session session = em.unwrap(Session.class);
			Query query = session.createSQLQuery(sqlQuery);
			List<Object[]> resultList = query.getResultList();
			Iterator<Object[]> itr = resultList.iterator();
			while (itr.hasNext()) {
				Object[] obj = itr.next();
				FetchMetadataVO testRunExecutionVO = new FetchMetadataVO();

				testRunExecutionVO
						.setCustomer_id(NULL_STRING.equals(String.valueOf(obj[0])) ? null : String.valueOf(obj[0]));
				testRunExecutionVO
						.setCustomer_number(NULL_STRING.equals(String.valueOf(obj[1])) ? null : String.valueOf(obj[1]));
				testRunExecutionVO
						.setCustomer_name(NULL_STRING.equals(String.valueOf(obj[2])) ? null : String.valueOf(obj[2]));
				testRunExecutionVO
						.setProject_id(NULL_STRING.equals(String.valueOf(obj[3])) ? null : String.valueOf(obj[3]));
				testRunExecutionVO
						.setProject_name(NULL_STRING.equals(String.valueOf(obj[4])) ? null : String.valueOf(obj[4]));
				testRunExecutionVO
						.setTest_set_id(NULL_STRING.equals(String.valueOf(obj[5])) ? null : String.valueOf(obj[5]));
				testRunExecutionVO.setTest_set_line_id(
						NULL_STRING.equals(String.valueOf(obj[6])) ? null : String.valueOf(obj[6]));
				testRunExecutionVO
						.setScript_id(NULL_STRING.equals(String.valueOf(obj[7])) ? null : String.valueOf(obj[7]));
				testRunExecutionVO
						.setScript_number(NULL_STRING.equals(String.valueOf(obj[8])) ? null : String.valueOf(obj[8]));
				testRunExecutionVO
						.setLine_number(NULL_STRING.equals(String.valueOf(obj[9])) ? null : String.valueOf(obj[9]));
				testRunExecutionVO.setInput_parameter(
						NULL_STRING.equals(String.valueOf(obj[10])) ? null : String.valueOf(obj[10]));
				testRunExecutionVO
						.setInput_value(NULL_STRING.equals(String.valueOf(obj[11])) ? null : String.valueOf(obj[11]));
				testRunExecutionVO
						.setAction(NULL_STRING.equals(String.valueOf(obj[12])) ? null : String.valueOf(obj[12]));
				testRunExecutionVO.setXpath_location(
						NULL_STRING.equals(String.valueOf(obj[13])) ? null : String.valueOf(obj[13]));
				testRunExecutionVO.setXpath_location1(
						NULL_STRING.equals(String.valueOf(obj[14])) ? null : String.valueOf(obj[14]));
				testRunExecutionVO
						.setField_type(NULL_STRING.equals(String.valueOf(obj[15])) ? null : String.valueOf(obj[15]));

				testRunExecutionVO
						.setScenario_name(NULL_STRING.equals(String.valueOf(obj[17])) ? null : String.valueOf(obj[17]));
				testRunExecutionVO.setDependencyScriptNumber(
						NULL_STRING.equals(String.valueOf(obj[18])) ? null : Integer.valueOf((String) obj[18]));
				testRunExecutionVO
						.setTest_run_name(NULL_STRING.equals(String.valueOf(obj[19])) ? null : String.valueOf(obj[19]));
				testRunExecutionVO
						.setSeq_num(NULL_STRING.equals(String.valueOf(obj[20])) ? null : String.valueOf(obj[20]));
				testRunExecutionVO
						.setStatus(NULL_STRING.equals(String.valueOf(obj[21])) ? null : String.valueOf(obj[21]));
				testRunExecutionVO.setTest_script_param_id(
						NULL_STRING.equals(String.valueOf(obj[22])) ? null : String.valueOf(obj[22]));
				testRunExecutionVO
						.setLineErrorMsg(NULL_STRING.equals(String.valueOf(obj[23])) ? null : String.valueOf(obj[23]));
				testRunExecutionVO.setTestRunParamDesc(
						NULL_STRING.equals(String.valueOf(obj[24])) ? null : String.valueOf(obj[24]));
				testRunExecutionVO
						.setExecuted_by(NULL_STRING.equals(String.valueOf(obj[25])) ? null : String.valueOf(obj[25]));
				testRunExecutionVO.setTargetApplicationName(
						NULL_STRING.equals(String.valueOf(obj[26])) ? null : String.valueOf(obj[26]));
				listOfTestRunExecutionVo.add(testRunExecutionVO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new WatsEBSException(500, "Exception occured while fetching all steps details for test run", e);
		}
		return listOfTestRunExecutionVo;
	}

	public void updatePassedScriptLineStatus(String testScriptParamId, String status, String message) {
		Format updateDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String updateDateFormatStr = updateDateFormat.format(new Date());
		try {
			Query query = em.createQuery("Update TestSetScriptParam set updateDate=TO_TIMESTAMP('" + updateDateFormatStr
					+ "','YYYY-MM-DD HH24:MI:SS'), lineExecutionStatus='" + status + "',line_error_message='"
					+ message.replace("'", "''") + "' where testRunScriptParamId=" + "'" + testScriptParamId + "'");
			query.executeUpdate();
		} catch (Exception e) {
			throw new WatsEBSException(500,
					"Exception occured while updating the status for testScriptId " + testScriptParamId, e);
		}
	}

	public void updatePassedScriptLineStatus(String testScriptParamId, String status, String value, String message) {
		Format updateDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String updateDateFormatStr = updateDateFormat.format(new Date());
		try {
			Query query = em.createQuery("Update TestSetScriptParam set updateDate=TO_TIMESTAMP('" + updateDateFormatStr
					+ "','YYYY-MM-DD HH24:MI:SS'),line_execution_status='" + status + "',input_value='" + value
					+ "',line_error_message='" + message.replace("'", "''") + "' where test_script_param_id='"
					+ testScriptParamId + "'");
			query.executeUpdate();
		} catch (Exception e) {
			logger.error("Failed to update passed script line status " + e.getMessage());
		}
	}

	public void updateFailedScriptLineStatus(String testScriptParamId, String errorMessage) {

		String sql = "Update WIN_TA_TEST_SET_SCRIPT_PARAM  SET LINE_EXECUTION_STATUS='Fail',LINE_ERROR_MESSAGE= :error_message where TEST_SCRIPT_PARAM_ID='"
				+ testScriptParamId + "'";
		Session session = em.unwrap(Session.class);
		Query query = session.createSQLQuery(sql);
		query.setParameter("error_message", errorMessage);

		query.executeUpdate();

	}

	public void updateInProgressScriptLineStatus(String testScriptParamId, String status) {
		try {
			TestSetScriptParam scriptParam = em.find(TestSetScriptParam.class, Integer.parseInt(testScriptParamId));
			if (scriptParam != null) {
				scriptParam.setLineExecutionStatus(status);
				em.merge(scriptParam);
			}
		} catch (Exception e) {
			logger.error(" Failed update inprogress scriptLine status " + e.getMessage());
		}
	}

	public void updateInProgressScriptStatus(String testSetLineId, Date startDate) {
		try {
			TestSetLine testLines = em.find(TestSetLine.class, Integer.parseInt(testSetLineId));

			if (testLines != null) {
				testLines.setStatus(IN_PROGRESS.toUpperCase());
				testLines.setExecutionStartTime(startDate);
				em.merge(testLines);
			}
		} catch (Exception e) {
			logger.error("Failed to update script status to In-progress " + e.getMessage());
		}
	}

	public void updateEnabledStatusForTestSetLine(String testSetId, String isEnable) {
		try {
			Query query = em.createQuery("from TestSetLine where testRun.testRunId=" + testSetId + " and enabled='"
					+ isEnable + "' AND STATUS IN ('IN-PROGRESS','IN-QUEUE','New')");

			List<TestSetLine> listOfLineDetails = query.getResultList();

			for (TestSetLine linesObj : listOfLineDetails) {
				linesObj.setStatus(FAIL);
				em.merge(linesObj);
			}

		} catch (Exception e) {
			logger.error("Failed to update script status to fail " + e.getMessage());
		}
	}

	public void updateStatusOfScript(String testSetLineId, String status) {
		try {
			TestSetLine testLines = em.find(TestSetLine.class, Integer.parseInt(testSetLineId));
			if (testLines != null) {
				testLines.setStatus(status);
				em.merge(testLines);
			}
		} catch (Exception e) {
			logger.error(String.format("Failed to update script status to - %s", status));
			logger.error(e.getMessage());
		}
	}

	public String getTrMode(String testSetId) throws SQLException {
		TestSet testSet = em.find(TestSet.class, Integer.parseInt(testSetId));
		if (testSet == null) {
			throw new SQLException();
		}
		return testSet.getTestRunMode();
	}

	public String getPassword(String testSetId, String userId) {
		Session session = em.unwrap(Session.class);
		String password = null;
		String sqlStr = "select WIN_DBMS_CRYPTO.DECRYPT(users.password , users.encrypt_key) PASSWORD from win_ta_test_set test_set,win_ta_config config,win_ta_config_users users where test_set.configuration_id = config.configuration_id and config.configuration_id = users.config_id and test_set.test_set_id = "
				+ testSetId + " and (upper(users.user_name) = upper('" + userId + "') or ('" + userId
				+ "' is null and users.default_user = 'Y')) and rownum = 1";

		logger.info(sqlStr);
		try {
			Query query = session.createSQLQuery(sqlStr);
			password = (String) query.getSingleResult();
		} catch (Exception e) {
			logger.error("Failed to get Password from DB");
		}
		return password;

	}

	public void updateEndTime(String lineId, String testSetId, Date endTime1) {
		Format startformat = new SimpleDateFormat(SIMPLE_DATE);
		String endTime = startformat.format(endTime1);

		try {
			Session session = em.unwrap(Session.class);
			String sqlQuery = "Update WIN_TA_TEST_SET_LINES  SET EXECUTION_END_TIME=TO_TIMESTAMP('" + endTime
					+ "','MM/DD/YYYY HH24:MI:SS') WHERE  TEST_SET_ID=" + testSetId + " AND TEST_SET_LINE_ID =" + lineId;
			Query query = session.createSQLQuery(sqlQuery);
			query.executeUpdate();
		} catch (Exception e) {
			logger.error("Failed to update endTime " + e.getMessage());
		}
	}

	public List<Object[]> getSumDetailsFromSubscription() {
		List<Object[]> result = null;
		String qry = "select sum(quantity),sum(executed),sum(balance) from wats_subscription\r\n"
				+ "where uom = 'Script' and status='Active'  and to_date(sysdate) >= start_date and to_date(sysdate) <= end_date";
		try {
			Session session = em.unwrap(Session.class);
			Query query = session.createSQLQuery(qry);
			result = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
  
	public List<Object[]> getSubscriptionDetails() {
		String qry = "select subscription_id, executed,balance from (SELECT subscription_id,executed,balance\r\n"
				+ "         FROM wats_subscription\r\n"
				+ "         WHERE status = 'Active' and uom = 'Script' and to_date(sysdate) >= start_date and to_date(sysdate) <= end_date\r\n"
				+ "        ORDER BY subscription_id) where ROWNUM = 1";
		List<Object[]> result = null;
		try {
			Session session = em.unwrap(Session.class);
			Query query = session.createSQLQuery(qry);
			result = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public Integer findGraceAllowance(BigDecimal subscriptionId) {
		BigDecimal sum = null;
		String qry = "select sum(round(WTC.GRACE_ALLOWANCE*WS.quantity/100)) \r\n"
				+ "        from wats_subscription WS,WIN_TA_CUSTOMERS WTC\r\n"
				+ "        where uom = 'Script' and status = 'Active'\r\n"
				+ "        and to_date(sysdate ,'dd-mm-yyyy') >= start_date and to_date(sysdate ,'dd-mm-yyyy') <= end_date\r\n"
				+ "        and UPPER(WTC.CUSTOMER_NAME)=UPPER(WS.CUSTOMER_NAME) and WS.subscription_id = " + subscriptionId;

		try {
			Session session = em.unwrap(Session.class);
			Query query = session.createSQLQuery(qry);
			sum = (BigDecimal) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sum.intValue();
	}

	public void updateSubscriptionExecuteAndBalance(BigDecimal executedCount, BigDecimal updatedBalanceCount,
			BigDecimal subscriptionId) {
		String qry = " UPDATE wats_subscription\r\n" + "    SET executed = " + (executedCount.intValue() + 1)
				+ ", balance = " + (updatedBalanceCount.intValue() - 1) + "\r\n" + "    WHERE subscription_id = "
				+ subscriptionId;
		try {
			Session session = em.unwrap(Session.class);
			Query query = session.createSQLQuery(qry);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateSubscriptionStatus(String status, BigDecimal subscriptionId) {

		String qry = "UPDATE WATS_SUBSCRIPTION SET STATUS='" + status + "' WHERE subscription_id = " + subscriptionId;

		try {
			Session session = em.unwrap(Session.class);
			Query query = session.createSQLQuery(qry);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void updateTestSetLineStatus(String status, String testSetLineScriptPath, String testSetId,
			String testSetLineId, String scriptId, Date endDate) {
		Format startformat = new SimpleDateFormat(SIMPLE_DATE);
		String endTime = startformat.format(endDate);

		try {
			Session session = em.unwrap(Session.class);
			String sqlQuery = "Update WIN_TA_TEST_SET_LINES SET STATUS='" + status
					+ "', TEST_SET_LINE_SCRIPT_PATH=REPLACE('" + testSetLineScriptPath
					+ "','AAA','&'), EXECUTION_END_TIME=TO_TIMESTAMP('" + endTime
					+ "','MM/DD/YYYY HH24:MI:SS') WHERE  TEST_SET_ID=" + testSetId + " AND TEST_SET_LINE_ID="
					+ testSetLineId + " AND SCRIPT_ID=" + scriptId;
			Query query = session.createSQLQuery(sqlQuery);
			query.executeUpdate();
		} catch (Exception e) {
			throw new WatsEBSException(500,
					"Exception occured while updating status, end date and path for script level pdf", e);
		}
	}

	public void updateTestSetPaths(String passPath, String failPath, String executionPath, String testSetId) {

		try {
			Session session = em.unwrap(Session.class);
			String sqlQuery = "Update WIN_TA_TEST_SET SET PASS_PATH =REPLACE('" + passPath
					+ "','AAA','&') , FAIL_PATH =REPLACE('" + failPath + "','AAA','&') , EXCEPTION_PATH =REPLACE('"
					+ executionPath + "','AAA','&') WHERE TEST_SET_ID=" + testSetId;
			Query query = session.createSQLQuery(sqlQuery);
			query.executeUpdate();
		} catch (Exception e) {
			throw new WatsEBSException(500, "Exception occured while updating the Paths for test run pdfs", e);
		}
	}

	public void insertExecHistoryTbl(String testSetLineId, Date startDate, Date endDate, String status) {
		Format dateFormat = new SimpleDateFormat(SIMPLE_DATE);
		String startTime = dateFormat.format(startDate);
		String endTime = dateFormat.format(endDate);
		try {
			Session session = em.unwrap(Session.class);
			int nextExecNo = getNextExecutionNum();
			String instQry = "INSERT INTO WIN_TA_EXECUTION_HISTORY (EXECUTION_ID, TEST_SET_LINE_ID, EXECUTION_START_TIME, EXECUTION_END_TIME, CREATED_BY, STATUS) VALUES ('"
					+ (nextExecNo) + "','" + testSetLineId + "'," + "TO_TIMESTAMP('" + startTime
					+ "','MM/DD/YYYY HH24:MI:SS')" + "," + "TO_TIMESTAMP('" + endTime + "','MM/DD/YYYY HH24:MI:SS')"
					+ ",'APP_USER','" + status + "')";
			Query instQuery = session.createSQLQuery(instQry);
			instQuery.executeUpdate();

		} catch (Exception e) {
			throw new WatsEBSException(500,
					"Exception occured while inserting records for start date, end date and status", e);
		}
	}

	public List<Object[]> getStatusAndSeqNum(String testSetId) {
		List<Object[]> listObj = null;
		try {
			Session session = em.unwrap(Session.class);
			String execQry = "SELECT SEQ_NUM, STATUS FROM WIN_TA_TEST_SET_LINES WHERE TEST_SET_ID=" + testSetId;
			listObj = session.createSQLQuery(execQry).getResultList();
		} catch (Exception e) {
			throw new WatsEBSException(605, "Unable to read records from WIN_TA_TEST_SET_LINES");
		}
		return listObj;
	}

	public void getPassAndFailCount(String testSetId, EmailParamDto emailParam) {
		String passQry = "SELECT COUNT(1) FROM WIN_TA_TEST_SET_LINES WHERE TEST_SET_ID = " + testSetId
				+ " AND UPPER(STATUS) = 'PASS'\r\n" + " AND ENABLED = 'Y'";
		String failQry = "SELECT COUNT(1) FROM WIN_TA_TEST_SET_LINES WHERE TEST_SET_ID = " + testSetId
				+ " AND UPPER(STATUS) = 'FAIL' AND ENABLED = 'Y'";

		try {
			Session session = em.unwrap(Session.class);

			BigDecimal passCount = (BigDecimal) session.createSQLQuery(passQry).getSingleResult();
			BigDecimal failCount = (BigDecimal) session.createSQLQuery(failQry).getSingleResult();

			emailParam.setPassCount(passCount.intValue());
			emailParam.setFailCount(failCount.intValue());

		} catch (Exception e) {
			throw new WatsEBSException(500,
					"Exception occured while fetching total pass and fail count for test run script.", e);
		}

	}

	public void getUserAndPrjManagerName(String userName, String testSetId, EmailParamDto emailParam) {

		String fetchUserName = "SELECT EMAIL\r\n" + "FROM WIN_TA_USERS\r\n" + "WHERE UPPER(USER_ID) = UPPER('"
				+ userName + "')";

		String fetchManagerName = "SELECT PROJ.PROJECT_MANAGER_EMAIL\r\n"
				+ "FROM WIN_TA_TEST_SET TS,WIN_TA_PROJECTS PROJ\r\n" + "WHERE TS.PROJECT_ID=PROJ.PROJECT_ID\r\n"
				+ "AND TS.TEST_SET_ID = " + testSetId + "\r\n" + "AND ROWNUM=1";

		try {
			Session session = em.unwrap(Session.class);
			String user = (String) session.createSQLQuery(fetchUserName).getSingleResult();
			String manager = (String) session.createSQLQuery(fetchManagerName).getSingleResult();
			emailParam.setReceiver(user);
			emailParam.setCcPerson(manager);
		} catch (Exception e) {
			throw new WatsEBSException(500, "Exception occured while fetching email for user.", e);
		}
	}

	public Integer getCountOfInProgressScriptForStoppedTestRun(String testSetId) {
		Integer count = null;
		String qry = " SELECT NVL(TR_MODE,'ACTIVE')\r\n" + "FROM WIN_TA_TEST_SET\r\n" + "WHERE TEST_SET_ID = "
				+ testSetId;

		try {
			Session session = em.unwrap(Session.class);
			String trMode = (String) session.createSQLQuery(qry).getSingleResult();
			if (trMode.equalsIgnoreCase("STOPPED")) {
				count = getCountOfInProgressScript(testSetId);
			}
		} catch (Exception e) {
			throw new WatsEBSException(500, "Exception occured while fetching the running process count.", e);
		}
		return count;
	}

	public Integer getCountOfInProgressScript(String testSetId) {
		Integer count = null;

		String selectQry = "SELECT COUNT(1) FROM WIN_TA_TEST_SET_LINES WHERE TEST_SET_ID =" + testSetId + "\r\n"
				+ "AND UPPER(STATUS) in ('IN-PROGRESS','IN-QUEUE')";
		try {
			Session session = em.unwrap(Session.class);
			BigDecimal inProgressCount = (BigDecimal) session.createSQLQuery(selectQry).getSingleResult();
			count = inProgressCount.intValue();
		} catch (Exception e) {
			throw new WatsEBSException(500, "Exception occured while fetching the running process count.", e);
		}
		return count;
	}

	public Integer updateExecStatusTable(String testSetId) {
		Integer id = null;
		try {
			Session session = em.unwrap(Session.class);
			String execQry = "SELECT RESPONSE_COUNT FROM TEST_RUN_EXECUTE_STATUS WHERE TEST_RUN_ID =" + testSetId
					+ " AND EXECUTION_ID = (SELECT MAX(EXECUTION_ID) FROM TEST_RUN_EXECUTE_STATUS WHERE TEST_RUN_ID ="
					+ testSetId + " )";
			BigDecimal bigDecimal = (BigDecimal) session.createSQLQuery(execQry).getSingleResult();
			id = Integer.parseInt(bigDecimal.toString());
			String updateQry = "UPDATE TEST_RUN_EXECUTE_STATUS SET RESPONSE_COUNT =" + (id + 1)
					+ " WHERE TEST_RUN_ID = " + testSetId
					+ " AND EXECUTION_ID = (SELECT MAX(EXECUTION_ID) FROM TEST_RUN_EXECUTE_STATUS WHERE TEST_RUN_ID = "
					+ testSetId + " )";
			session.createSQLQuery(updateQry).executeUpdate();
		} catch (Exception e) {
			throw new WatsEBSException(500, "Exception occured while updating the response count", e);
		}
		return id + 1;

	}

	public void updateExecStatusFlag(String testSetId) {
		String updateQry = "UPDATE EXECUTE_STATUS SET STATUS_FLAG = 'I' WHERE TEST_RUN_ID = " + testSetId;

		try {
			Session session = em.unwrap(Session.class);
			session.createSQLQuery(updateQry).executeUpdate();
		} catch (Exception e) {
			throw new WatsEBSException(500, "Exception occured while Updating status flag for test run script.",
					e);
		}
	}

	public Object getRequestCountFromExecStatus(String testSetId) {

		Object requestCount = null;
		try {
			Session session = em.unwrap(Session.class);

			String execQry = " SELECT\r\n" + "REQUEST_COUNT\r\n" + "FROM\r\n" + "TEST_RUN_EXECUTE_STATUS\r\n"
					+ "WHERE\r\n" + "TEST_RUN_ID =" + testSetId + "\r\n" + "AND EXECUTION_ID = (\r\n" + "SELECT\r\n"
					+ "MAX(EXECUTION_ID)\r\n" + "FROM\r\n" + "TEST_RUN_EXECUTE_STATUS\r\n" + "WHERE\r\n"
					+ "TEST_RUN_ID = " + testSetId + ")";
			requestCount = session.createSQLQuery(execQry).getSingleResult();
		} catch (Exception e) {
			throw new WatsEBSException(500, EXCEPTION_MSG, e);
		}
		return requestCount;
	}

	public List<Object[]> findStartAndEndTimeForTestRun(String testRunId, String scriptStatus) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
		Root<TestSetLine> from = cq.from(TestSetLine.class);
		Predicate condition1 = cb.equal(from.get(TEST_RUN).get(TEST_SET_ID), testRunId);
		Predicate condition2 = cb.equal(from.get(STATUS), scriptStatus);
		List<String> statusList = Arrays.asList(PASS, FAIL);
		Expression<String> inExpression = from.get(STATUS);
		Predicate inPredicate = inExpression.in(statusList);

		Predicate condition3 = (scriptStatus != null) ? cb.and(condition1, condition2)
				: cb.and(condition1, inPredicate);
		cq.multiselect(from.get("executionStartTime"), from.get("executionEndTime")).where(condition3);
		return em.createQuery(cq).getResultList();

	}

	public int dbAccessibilityCheck() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = cb.createQuery(Object.class);
		cq.multiselect(cb.count(cq.from(TestSet.class)));
		Object result = em.createQuery(cq).getSingleResult();
		return Integer.parseInt(result.toString());
	}

	public String getCentralRepoUrl(String name) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<ApplicationProperties> from = cq.from(ApplicationProperties.class);
		Predicate condition = cb.equal(from.get("keyName"), name);
		cq.multiselect(from.get("valueName")).where(condition);
		List<String> result = em.createQuery(cq).getResultList();
		return result.get(0);
	}

	public void updateTestSetLineStatusForSanity(String testSetId) {
		String updateQry = "UPDATE win_ta_test_set_lines SET status = 'Fail' where test_set_id = " + testSetId
				+ " and enabled = 'Y' and status in ('New','Fail','IN-QUEUE','IN-PROGRESS','NEW','FAIL')";
		try {
			Session session = em.unwrap(Session.class);
			session.createSQLQuery(updateQry).executeUpdate();
		} catch (Exception e) {
			throw new WatsEBSException(500, "Exception occured while Updating status for scripts.", e);
		}
	}

	public List<TestSetLine> getAllTestSetLineRecord(String testSetId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TestSetLine> cq = cb.createQuery(TestSetLine.class);
		Root<TestSetLine> from = cq.from(TestSetLine.class);
		Predicate condition = cb.equal(from.get(TEST_RUN).get(TEST_SET_ID), testSetId);
		cq.where(condition);
		Query query = em.createQuery(cq.select(from));
		return query.getResultList();
	}

	public void updateEnableFlagForSanity(String testSetId) {
		String updateQry = "UPDATE EXECUTE_STATUS SET STATUS_FLAG = 'I' WHERE TEST_RUN_ID = :P_TEST_SET_ID";
		try {
			Session session = em.unwrap(Session.class);
			session.createSQLQuery(updateQry).setParameter("P_TEST_SET_ID", testSetId).executeUpdate();
		} catch (Exception e) {
			throw new WatsEBSException(500, "Exception occured while Updating status for status flag.", e);
		}
	}

	public TestSet getTestRunDetails(String testSetId) {
		return em.unwrap(Session.class).find(TestSet.class, Integer.parseInt(testSetId));
	}


	public boolean doesActionContainsExcel(String scriptId) {
		Object count = null;
		String updateQry = "select count(*) from WATS_PROD.win_ta_test_set_script_param where script_id = :script_id and action like '%excel%'";
		try {
			Session session = em.unwrap(Session.class);
			count = session.createSQLQuery(updateQry).setParameter("script_id", scriptId).getSingleResult();
		} catch (Exception e) {
			throw new WatsEBSException(500, "Exception occured while Checking if actions contains excel or not.",
					e);
		}
		return Integer.parseInt(count.toString()) > 0;
	}

	public int getApiValidationIdActionId() {
		Object requestCount = null;
		try {
			Session session = em.unwrap(Session.class);

			String execQry = "select lookup_id from win_ta_lookups where lookup_name = 'API_VALIDATION'";
			requestCount = session.createSQLQuery(execQry).getSingleResult();
		} catch (Exception e) {
			throw new WatsEBSException(500, EXCEPTION_MSG, e);
		}
		return Integer.parseInt(requestCount.toString());
	}

	public List<Object> getApiValidationDataFromLookupsCode(int apiValidationId, List<Integer> list) {
		List<Object> listOfLookUpCodesData = null;
		try {
			Session session = em.unwrap(Session.class);
			listOfLookUpCodesData = session.createQuery(
					"from LookUpCode lu where lu.lookUpId in :lookupId and lu.lookUpCodeId in (:listOfLookUpId)")
					.setParameter("lookupId", apiValidationId).setParameter("listOfLookUpId", list).getResultList();
		} catch (Exception e) {
			throw new WatsEBSException(500, EXCEPTION_MSG, e);
		}
		return listOfLookUpCodesData;
	}

	public List<String> getExistingLookupCodeByValidationId(int apiValidationId, String lookUpCode) {
		List<String> listOfLookUpCode = null;
		try {
			Session session = em.unwrap(Session.class);
			String query = "select lookup_code from win_ta_lookup_codes where lookup_id = " + apiValidationId
					+ " and lookup_code in ('" + lookUpCode + "')";
			listOfLookUpCode = session.createSQLQuery(query).getResultList();
		} catch (Exception e) {
			throw new WatsEBSException(500, EXCEPTION_MSG, e);
		}
		return listOfLookUpCode;
	}

	@Transactional
	public void insertApiValidation(LookUpCode lookUpCodes) {
		Session session = em.unwrap(Session.class);
		session.persist(lookUpCodes);
	}

	public TestSetAttribute getApiValueBySetIdAndAPIKey(String testSetId, String apiKey) {

		Session session = em.unwrap(Session.class);

		List<TestSetAttribute> listOfSetAttr = session.createQuery(
				"from TestSetAttribute where id.testSetId =" + testSetId + " and id.attributeName = '" + apiKey + "'")
				.getResultList();
		return listOfSetAttr.isEmpty() ? null : listOfSetAttr.get(0);
	}

	public void insertRecordInTestSetAttribute(String testSetLineId, String string, String token, String executedBy) {
		Session session = em.unwrap(Session.class);
		Date date = new Date();
		int listOfSetAttr = session.createNativeQuery(
				"INSERT INTO win_ta_test_set_attribute (TEST_SET_ID,ATTRIBUTE_NAME,ATTRIBUTE_VALUE,CREATED_DATE,UPDATED_DATE,CREATED_BY,UPDATED_BY) VALUES (:P5_TEST_RUN_ID,:P5_TEST_Attribute_Name,:P5_ACCESS_TOKEN_EDIT,:SYSDATE1,:SYSDATE2,:APP_USER1,:APP_USER2)")
				.setParameter("P5_TEST_RUN_ID", testSetLineId).setParameter("P5_TEST_Attribute_Name", string)
				.setParameter("P5_ACCESS_TOKEN_EDIT", token).setParameter("SYSDATE1", date)
				.setParameter("SYSDATE2", date).setParameter("APP_USER1", executedBy)
				.setParameter("APP_USER2", executedBy).executeUpdate();
		if(listOfSetAttr > 0) {
			logger.info("TestSet Attribute Records Updated Successfully ");
		} else {
			logger.info("Some issue occured while inserting TestSet Attribute Records ");
		}
	}

	public List<LookUpCode> getExistingLookupListByValidationId(int apiValidationId, String lookUpCode) throws Exception {
		  TypedQuery<LookUpCode> query ;
		  try {
			Session session = em.unwrap(Session.class); 
			query = em.createQuery("from LookUpCode where lookup_id = :apiValidationId and lookup_code in :lookUpCode", LookUpCode.class);
			  
		} catch (Exception e) {
			logger.error("Not able to fetch LookUpCode data from database");
			throw new WatsEBSException(500, "Exception occured while fetching LookUpCode data",e);
		}
		 return query.setParameter("apiValidationId", apiValidationId).setParameter("lookUpCode", lookUpCode).getResultList();
		}
	
	public void updateApiValidation(LookUpCode listOfLookUpCodes) {
		Session session = em.unwrap(Session.class);
		session.merge(listOfLookUpCodes);
	}
	public String getEnabledStatusByTestSetLineID(String testSetLineId) {
		Session session = em.unwrap(Session.class);
		String query = "select enabled from WIN_TA_TEST_SET_LINES where test_set_line_id=:testSetLineId ";
		String result = session.createSQLQuery(query).setParameter("testSetLineId", testSetLineId).getSingleResult().toString();
		return result;
	}

	public ScriptMaster getScriptDetailsByScriptId(Integer scriptId) {
		Session session = em.unwrap(Session.class);
		return session.find(ScriptMaster.class, scriptId);
	}

	public String getDirectoryPath() {
		try {
			Session session = em.unwrap(Session.class);
			String sql = "select directory_path from all_directories where directory_name = 'WATS_OBJ_DIR'";
			@SuppressWarnings("rawtypes")
			SQLQuery query = session.createSQLQuery(sql);
			return query.getSingleResult().toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new WatsEBSException(500, "Directory path is not present", e);
		}
	}
	
	public List<String> getAllModules() {
		try {
			Session session = em.unwrap(Session.class);
			Query query = session.createQuery("select meaning from LookUpCode where lookUpName = 'MODULE'");
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			throw new WatsEBSException(500, "Not able to fetch the module", e);
		}
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void deleteTestSetLinesRecordsByTestSetLineId(TestSetLine testSetLine, String deletedBy) {
		try {
			LogDetailsTable logDetailsTable = createLogDetailsTable(testSetLine,deletedBy);
	        logDetailsRepository.save(logDetailsTable);
			int data = em.createQuery("delete from TestSetLine where testRunScriptId = :testSetLineId")
					.setParameter("testSetLineId", testSetLine.getTestRunScriptId()).executeUpdate();
			
			logger.info("deleted count {}" + data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void deleteTestSetScriptParamRecordsByTestSetLineId(TestSetLine testSetLine) {
		try {
			Integer testRunScriptId = testSetLine.getTestRunScriptId();
			int data = em
					.createQuery("delete from TestSetScriptParam where testSetLine.testRunScriptId = :testSetLineId")
					.setParameter("testSetLineId", testRunScriptId).executeUpdate();
			logger.info("deleted count {}" + data);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateStatusOfPdfGeneration(String testSetId, String status) {
		Session session = em.unwrap(Session.class);
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaUpdate<TestSet> cq = cb.createCriteriaUpdate(TestSet.class);
		Root<TestSet> from = cq.from(TestSet.class);
		cq.set("pdfGenerationEnabled", status);
		Predicate condition = cb.equal(from.get(TEST_SET_ID), testSetId);
		cq.where(condition);
		Query query = session.createQuery(cq);
		query.executeUpdate();
	}
	
	public List<TestSetScriptParam> getTestSetScriptParamContainsExcel(Integer testsetlineid) {
		List<TestSetScriptParam> testscriptparam = null;
		String qry = "SELECT e FROM TestSetScriptParam e WHERE e.testSetLine.testRunScriptId=:testsetlineid AND e.action LIKE '%excel%' ORDER BY e.lineNumber";
		try {
			TypedQuery<TestSetScriptParam > query = em.createQuery(qry, TestSetScriptParam .class);
			query.setParameter("testsetlineid", testsetlineid);
			testscriptparam = query.getResultList();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return testscriptparam;
	}
	
	public void UpdateTestSetScriptParamContainsExcel(Integer testscriptparamid) {
		String updateQry = "UPDATE WATS_PROD.win_ta_test_set_script_param SET LINE_EXECUTION_STATUS = 'Fail' where TEST_SCRIPT_PARAM_ID = " + testscriptparamid;
		try {
			Session session = em.unwrap(Session.class);
			session.createSQLQuery(updateQry).executeUpdate();
		} catch (Exception e) {
			throw new WatsEBSException(500, "Exception occured while Updating status for scripts.", e);
		}
	}
	
	private LogDetailsTable createLogDetailsTable(TestSetLine testSetLine, String deletedBy) {
		LogDetailsTable logDetailsTable = new LogDetailsTable();
		logDetailsTable.setLogLevel("Info");
		logDetailsTable.setLogTable("WIN_TA_TEST_SET_LINES");
		logDetailsTable.setLogAction("Delete");
		logDetailsTable.setLogTime(new Date());
		logDetailsTable.setExecutedBy(deletedBy);
		String logDescription = String.format(
				"Test Set Id: %d, Script Number: %s, Test Set Line Id: %d, Last Updated By: %s, Sequence Number: %d is Deleted",
				testSetLine.getTestRun().getTestRunId(), testSetLine.getScriptNumber(),
				testSetLine.getTestRunScriptId(), deletedBy, testSetLine.getSeqNum());
		logger.info(logDescription);
		logDetailsTable.setLogDescription(logDescription);
		return logDetailsTable;
	}
	
	public void updateTestSetLinesWarningMessage(String testScriptParamId, String errorMessage) {

		String sql = "Update WIN_TA_TEST_SET_SCRIPT_PARAM SET LINE_ERROR_MESSAGE= :error_message where TEST_SCRIPT_PARAM_ID='"
				+ testScriptParamId + "'";
		Session session = em.unwrap(Session.class);
		Query query = session.createSQLQuery(sql);
		query.setParameter("error_message", errorMessage);

		query.executeUpdate();

	}

}
	
	


