package com.winfo.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Repository;

import com.winfo.exception.WatsEBSCustomException;
import com.winfo.model.AuditScriptExecTrail;
import com.winfo.model.AuditStageLookup;
import com.winfo.model.Project;
import com.winfo.model.ScriptMaster;
import com.winfo.model.ScriptMetaData;
import com.winfo.model.TestSet;
import com.winfo.model.TestSetLine;
import com.winfo.model.TestSetScriptParam;
import com.winfo.services.FetchConfigVO;
import com.winfo.services.FetchMetadataVO;
import com.winfo.utils.Constants.BOOLEAN_STATUS;
import com.winfo.vo.CustomerProjectDto;
import com.winfo.vo.EmailParamDto;
import com.winfo.vo.ScriptDetailsDto;
import com.winfo.vo.Status;

@Repository
@RefreshScope
public class DataBaseEntryDao {
	@PersistenceContext
	EntityManager em;

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
	private static final String PASS = "Pass";
	private static final String FAIL = "Fail";
	private static final String NEW  = "New";
	private static final String STATUS = "status";
	private static final String IN_QUEUE = "In-Queue";

	
	@SuppressWarnings("unchecked")
	public List<ScriptMetaData> getScriptMetaDataList(Integer scriptId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ScriptMetaData> cq = cb.createQuery(ScriptMetaData.class);
		Root<ScriptMetaData> from = cq.from(ScriptMetaData.class);
		Predicate condition = cb.equal(from.get("scriptMaster").get("script_id"), scriptId);
		cq.where(condition);
		Query query = em.createQuery(cq);
		return query.getResultList();
	}
	
	
	public void updatePassedScriptLineStatus(FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO,
			String testScriptParamId, String status) throws ClassNotFoundException, SQLException {
		try {
			Query query = em.createQuery(
					"Update TestSetScriptParam set line_execution_status='Pass' where test_script_param_id=" + "'"
							+ testScriptParamId + "'");
			query.executeUpdate();
		} catch (Exception e) {
			System.out.println("cant update passed script line status");
			System.out.println(e);
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
			System.out.println("cant get product version.");
			System.out.println(e);
		}

		return productVersion;
	}

	public String getErrorMessage(String sndo, String ScriptName, String testRunName, FetchConfigVO fetchConfigVO)
			throws ClassNotFoundException, SQLException {
		String errorMessage = "";
		String sqlQuery = "SELECT PARAM.LINE_ERROR_MESSAGE "
				+ "FROM WIN_TA_TEST_SET_SCRIPT_PARAM PARAM,WIN_TA_TEST_SET_LINES LINES,WIN_TA_TEST_SET TS "
				+ "WHERE TS.TEST_SET_ID = LINES.TEST_SET_ID " + "AND LINES.TEST_SET_LINE_ID = PARAM.TEST_SET_LINE_ID "
				+ "AND TS.TEST_SET_ID = (SELECT TEST_SET_ID FROM WIN_TA_TEST_SET WHERE UPPER(TEST_SET_NAME)=UPPER('"
				+ testRunName + "'))" + "AND UPPER(LINES.SCRIPT_NUMBER) = UPPER('" + ScriptName + "') "
				+ "AND LINES.SEQ_NUM = " + sndo + " " + "AND PARAM.LINE_ERROR_MESSAGE IS NOT NULL";

		try {
			Session session = em.unwrap(Session.class);
			Query query = session.createSQLQuery(sqlQuery);
			errorMessage = (String) query.getResultList().get(0);
			/*
			 * if(errorMessage==null) { throw new RuntimeException(); }
			 */
		} catch (Exception e) {
			System.out.println("cant get error message");
			System.out.println(e);
		}

		return errorMessage;
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
			/*
			 * if(errorMessage==null) { throw new RuntimeException(); }
			 */
		} catch (Exception e) {
			System.out.println("cant get error message");
			System.out.println(e);
		}

		return errorMessage;
	}

	public void updateInProgressScriptStatus(FetchConfigVO fetchConfigVO, String testSetId, String testSetLineId)
			throws ClassNotFoundException, SQLException {
		try {
			TestSetLine testLines = em.find(TestSetLine.class, Integer.parseInt(testSetLineId));

			/* if(testLines==null) { throw new RuntimeException(); } */
			if (testLines != null) {
				testLines.setStatus(IN_PROGRESS.toUpperCase());
				em.merge(testLines);
			}
		} catch (Exception e) {
			System.out.println("cant update in progress script status");
			System.out.println(e);
		}
	}

	public void updateStartTime(FetchConfigVO fetchConfigVO, String lineId, String testSetId, Date startTime1)
			throws ClassNotFoundException, SQLException {
		Format startformat = new SimpleDateFormat(SIMPLE_DATE);
		String start_time = startformat.format(startTime1);
		try {
			Session session = em.unwrap(Session.class);
			Query query = session
					.createSQLQuery("Update WATS_PROD.WIN_TA_TEST_SET_LINES  SET EXECUTION_START_TIME=TO_TIMESTAMP('"
							+ start_time + "','MM/DD/YYYY HH24:MI:SS') WHERE TEST_SET_ID=" + testSetId
							+ " AND TEST_SET_LINE_ID = " + lineId);
			query.executeUpdate();
		} catch (Exception e) {
			System.out.println("cant update starttime");
			System.out.println(e);
		}
	}

	public int getNextExecutionNum() {
		Session session = em.unwrap(Session.class);
		String sql = "SELECT WIN__TA_EXECUTION_ID_SEQ.NEXTVAL FROM DUAL";
		SQLQuery query = session.createSQLQuery(sql);

		List results = query.list();
		if (!results.isEmpty()) {
			System.out.println(results.get(0));

			BigDecimal bigDecimal = (BigDecimal) results.get(0);
			Integer id = Integer.parseInt(bigDecimal.toString());
			return id;
		} else {
			return 0;
		}
	}

	public void updateFailedImages(FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO,
			String testScriptParamId) throws SQLException {
		try {
			String folder = (fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + fetchMetadataVO.getCustomer_name()
					+ File.separator

					+ fetchMetadataVO.getTest_run_name() + File.separator + fetchMetadataVO.getSeq_num()
					+ SPECIAL_CHAR[0]

					+ fetchMetadataVO.getLine_number() + SPECIAL_CHAR[0] + fetchMetadataVO.getScenario_name()
					+ SPECIAL_CHAR[0]

					+ fetchMetadataVO.getScript_number() + SPECIAL_CHAR[0] + fetchMetadataVO.getTest_run_name()
					+ SPECIAL_CHAR[0]

					+ fetchMetadataVO.getLine_number() + SPECIAL_CHAR[0] + PASSED).concat(SPECIAL_CHAR[1] +JPG);

			File file = new File(folder);
			byte[] screenshotArray = new byte[(int) file.length()];
			try (FileInputStream fileInputStream = new FileInputStream(file);) {
				fileInputStream.read(screenshotArray);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			String sql = "Update WIN_TA_TEST_SET_SCRIPT_PARAM  SET SCREENSHOT= :screenshot where TEST_SCRIPT_PARAM_ID='"
					+ testScriptParamId + "'";
			Query query = em.unwrap(Session.class).createSQLQuery(sql);
			query.setParameter("screenshot", screenshotArray);
			query.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public Map<String, Map<String, TestSetScriptParam>> getTestRunMap(String test_run_id) {
		// TODO Auto-generated method stub
		// FetchMetadataVO metadataVO=new FetchMetadataVO();
		Map<String, Map<String, TestSetScriptParam>> map = new HashMap<String, Map<String, TestSetScriptParam>>();
		String sql = "from TestSetLine where testRun=:testSet";
		Integer testRunId2 = Integer.parseInt(test_run_id);
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
		// map.put(String.valueOf(test_set_line.getSeq_num()),map2);
		return map2;
	}

	public TestSetLine getTestSetLine(String test_set_line_id) {
		// TODO Auto-generated method stub
		return em.find(TestSetLine.class, Integer.parseInt(test_set_line_id));
	}

	public void getDependentScriptNumbers(LinkedHashMap<String, List<FetchMetadataVO>> dependentScriptMap,
			List<Integer> dependentList) {
		// TODO Auto-generated method stub
		String sql = "Select script_id,dependency from ScriptMaster where script_id in (:dependentList)";
		Query query = em.unwrap(Session.class).createQuery(sql).setParameterList("dependentList", dependentList);

		List<Object[]> scriptList = query.getResultList();
		// Object[] objectArray = scriptList.toArray();
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();

		for (Object[] obj : scriptList) {
			map.put((Integer) obj[0], (Integer) obj[1]);
		}

		for (Entry<String, List<FetchMetadataVO>> element : dependentScriptMap.entrySet()) {
			element.getValue().get(0)
					.setDependencyScriptNumber(map.get(Integer.parseInt(element.getValue().get(0).getScript_id())));

		}

	}

	public void getTestRunLevelDependentScriptNumbers(LinkedHashMap<String, List<FetchMetadataVO>> dependentScriptMap,
			List<Integer> dependentList, String test_set_id) {
		// TODO Auto-generated method stub
		String sql = "Select script_id,dependency_tr from win_ta_test_set_lines where script_id in (:dependentList) and test_set_id = :test_set_id  and dependency_tr is not null";
		Query query = em.unwrap(Session.class).createSQLQuery(sql).setParameterList("dependentList", dependentList)
				.setParameter("test_set_id", test_set_id);

		List<Object[]> scriptList = query.getResultList();
		// Object[] objectArray = scriptList.toArray();
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();

		for (Object[] obj : scriptList) {
			map.put(Integer.parseInt(obj[0].toString()), Integer.parseInt(obj[1].toString()));
		}

		for (Entry<String, List<FetchMetadataVO>> element : dependentScriptMap.entrySet()) {
			element.getValue().get(0)
					.setDependencyScriptNumber(map.get(Integer.parseInt(element.getValue().get(0).getScript_id())));

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
		// query.setPara

		List<String> list = query.getResultList();

		Status status = new Status();
		int awaitCount = 0;
		if (list != null) {
			if ((list.size() > 0) && (!(list.contains(FAIL) || list.contains(FAIL.toUpperCase())))
					&& (!(list.contains(NEW) || list.contains(NEW.toUpperCase())))) {
				if ((list.contains(IN_PROGRESS) || list.contains(IN_PROGRESS.toUpperCase()))
						|| (list.contains(IN_QUEUE) || list.contains(IN_QUEUE.toUpperCase()))) {
					status.setStatus("Wait");
					for (String stat : list) {
						if (!stat.equalsIgnoreCase(PASS)) {
							awaitCount++;
						}
					}
					status.setInExecutionCount(awaitCount);
					scriptStatus.put(dependentScriptNo, status);
				} else {
					status.setStatus(PASS);
					scriptStatus.put(dependentScriptNo, status);
				}
			} else {
				status.setStatus(FAIL);
				scriptStatus.put(dependentScriptNo, status);
			}
		} else {
			status.setStatus(FAIL);
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
		Integer result = em.createQuery(cq).getSingleResult();
		return result;

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
			throw new WatsEBSCustomException(500,
					"Exception occured while getting status of PDF Generation for the Test Run", e);
		}

	}

	public ArrayList<String> getStepsStatusByScriptId(int testSetLineId) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<TestSetScriptParam> from = cq.from(TestSetScriptParam.class);

		Predicate condition = cb.equal(from.get(TEST_SET_LINE).get(TEST_RUN_SCRIPT_ID), testSetLineId);
		cq.where(condition);
		Query query = em.createQuery(cq.select(from.get("lineExecutionStatus")));
		ArrayList<String> result = (ArrayList<String>) query.getResultList();

		return result;
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
			return (List<Object[]>) query.getResultList();
		} catch (Exception e) {
			throw new WatsEBSCustomException(500, "Exception occured while fetching configuration details", e);
		}
	}

	public void updatePdfGenerationEnableStatus(String testSetId, String enabled) {
		try {
			Query query = em.createQuery(
					"Update TestSet set pdfGenerationEnabled='" + enabled + "' where testRunId='" + testSetId + "'");
			query.executeUpdate();
		} catch (Exception e) {
			System.out.println("Error Updation PDF Generation Status");
			System.out.println(e);
		}
	}

	public Date findMaxExecutionEndDate(long testSetId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Date> cq = cb.createQuery(Date.class);
		Root<TestSetLine> from = cq.from(TestSetLine.class);
		Predicate condition = cb.equal(from.get(TEST_RUN).get(TEST_SET_ID), testSetId);

		cq.select(cb.greatest(from.<Date>get("executionEndTime"))).where(condition);
		Date query = em.createQuery(cq).getSingleResult();
		return query;

	}

	public Date findMinExecutionStartDate(long testSetId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Date> cq = cb.createQuery(Date.class);
		Root<TestSetLine> from = cq.from(TestSetLine.class);
		Predicate condition = cb.equal(from.get(TEST_RUN).get(TEST_SET_ID), testSetId);

		cq.select(cb.least(from.<Date>get("executionStartTime"))).where(condition);
		Date query = em.createQuery(cq).getSingleResult();
		return query;

	}

	public Date findStepMaxUpdatedDate(String testSetLineId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Date> cq = cb.createQuery(Date.class);
		Root<TestSetScriptParam> from = cq.from(TestSetScriptParam.class);
		Predicate condition = cb.equal(from.get("testSetLine").get(TEST_RUN_SCRIPT_ID), testSetLineId);

		cq.select(cb.greatest(from.<Date>get("updateDate"))).where(condition);
		return em.createQuery(cq).getSingleResult();

	}

	public Integer findFirstStepIdInScript(String testSetLineId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
		Root<TestSetScriptParam> from = cq.from(TestSetScriptParam.class);
		Predicate condition = cb.equal(from.get("testSetLine").get(TEST_RUN_SCRIPT_ID), testSetLineId);
		cq.select(from.get("testRunScriptParamId")).where(condition);
		cq.orderBy(cb.asc(from.get("lineNumber")));
		Integer result = em.createQuery(cq).setMaxResults(1).getSingleResult();
		return result;

	}

	public ScriptMaster findScriptMasterByScriptId(int scriptId) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ScriptMaster> cq = cb.createQuery(ScriptMaster.class);
		Root<ScriptMaster> from = cq.from(ScriptMaster.class);

		Predicate condition1 = cb.equal(from.get("script_id"), scriptId);

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
	
	public TestSetLine checkTestSetLinesByTestSetLineId(int testSetId, int testSetLineId) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TestSetLine> cq = cb.createQuery(TestSetLine.class);
		Root<TestSetLine> from = cq.from(TestSetLine.class);

		Predicate condition1 = cb.equal(from.get("testRunScriptId"), testSetLineId);
		Predicate condition2 = cb.equal(from.get(TEST_RUN).get(TEST_SET_ID), testSetId);
		Predicate condition = cb.and(condition1, condition2);
		cq.where(condition);
		Query query = em.createQuery(cq);
		TestSetLine result = (TestSetLine) query.getSingleResult();
		em.refresh(result);

		return result;
	}

	@SuppressWarnings("unchecked")
	public List<String> getTestSetLinesStatusByTestSetId(long testSetId, Boolean enable) {
		List<String> result = null;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TestSetLine> cq = cb.createQuery(TestSetLine.class);
			Root<TestSetLine> from = cq.from(TestSetLine.class);

			Predicate condition1 = cb.equal(from.get(TEST_RUN).get(TEST_SET_ID), testSetId);
			Predicate condition2 = null;
			Predicate condition = cb.and(condition1, condition2);
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
			throw new WatsEBSCustomException(500, "Exception occured while fetching the status for test run pdfs", e);
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	public List<String> getStatusByTestSetId(String testSetId) {

		String sqlQry = "select STATUS FROM win_ta_test_set_lines where test_set_id=" + testSetId;
		List<String> result = null;
		Session session = em.unwrap(Session.class);
		try {
			Query query = session.createSQLQuery(sqlQry);
			result = query.getResultList();
		} catch (Exception e) {
			throw new WatsEBSCustomException(604, "Unable to read status from win_ta_test_set_lines");
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
			throw new WatsEBSCustomException(600, "Unable to read the fail count");
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
			throw new WatsEBSCustomException(600, "Unable to read the pass count");
		}

	}

	public CustomerProjectDto getCustomerDetails(String testSetId) {
		CustomerProjectDto customerDetails = null;
		String qry = "SELECT DISTINCT wtp.customer_id,\r\n" + "           wtc.customer_number,\r\n"
				+ "          wtc.customer_name,\r\n" + "           wtts.project_id,\r\n"
				+ "           wtp.project_name,\r\n" + "           wttsl.test_set_id,\r\n"
				+ "         wtts.TEST_SET_NAME test_run_name\r\n" + "      from\r\n"
				+ "      win_ta_test_set        wtts,\r\n" + "           win_ta_test_set_lines  wttsl,\r\n"
				+ "           win_ta_projects        wtp,\r\n" + "           win_ta_customers       wtc\r\n"
				+ "     WHERE 1=1\r\n" + "       AND wttsl.test_set_id = wtts.test_set_id\r\n"
				+ "       AND wtts.project_id = wtp.project_id\r\n" + "       AND wtp.customer_id = wtc.customer_id\r\n"
				+ "       AND wtts.test_set_id=" + testSetId;
		try {
			String NULL_STRING = "null";
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
			throw new WatsEBSCustomException(500, "Exception occured while fetching all steps details for test run.",
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
			String NULL_STRING = "null";
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

				// testRunExecutionVO.setHint( NULL_STRING.equals(String.valueOf(obj[10]))?
				// null:String.valueOf(obj[10]));
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

				listOfTestRunExecutionVo.add(scriptDetailsDto);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new WatsEBSCustomException(500, "Exception occured while fetching all steps details for test run", e);
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
				+ "           ma.SCENARIO_NAME,\r\n" + "    decode(ma.dependency, null, 'N', 'Y') dependency\r\n"
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
			String NULL_STRING = "null";
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
				// testRunExecutionVO.setHint( NULL_STRING.equals(String.valueOf(obj[16]))?
				// null:String.valueOf(obj[16]));
				testRunExecutionVO
						.setScenario_name(NULL_STRING.equals(String.valueOf(obj[17])) ? null : String.valueOf(obj[17]));
				testRunExecutionVO
						.setDependency(NULL_STRING.equals(String.valueOf(obj[18])) ? null : String.valueOf(obj[18]));
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
			throw new WatsEBSCustomException(500, "Exception occured while fetching all steps details for test run", e);
		}
		return listOfTestRunExecutionVo;
	}

	public void updatePassedScriptLineStatus(FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO,
			String testScriptParamId, String status, String message) {
		Format updateDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String updateDateFormatStr = updateDateFormat.format(new Date());
		try {
			Query query = em.createQuery("Update TestSetScriptParam set updateDate=TO_TIMESTAMP('" + updateDateFormatStr
					+ "','YYYY-MM-DD HH24:MI:SS'), lineExecutionStatus='" + status + "',line_error_message='"
					+ message.replace("'", "''") + "' where testRunScriptParamId=" + "'" + testScriptParamId + "'");
			query.executeUpdate();
		} catch (Exception e) {
			throw new WatsEBSCustomException(500,
					"Exception occured while updating the status for testScriptId " + testScriptParamId, e);
		}
	}

	public void updatePassedScriptLineStatus(FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO,
			String testScriptParamId, String status, String value, String message) {
		Format updateDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String updateDateFormatStr = updateDateFormat.format(new Date());
		try {
			Query query = em.createQuery("Update TestSetScriptParam set updateDate=TO_TIMESTAMP('" + updateDateFormatStr
					+ "','YYYY-MM-DD HH24:MI:SS'),line_execution_status='" + status + "',input_value='" + value
					+ "',line_error_message='" + message.replace("'", "''") + "' where test_script_param_id='"
					+ testScriptParamId + "'");
			query.executeUpdate();
		} catch (Exception e) {
			System.out.println("cant update passed script line status");
			System.out.println(e);
		}
	}

	public void updateFailedScriptLineStatus(FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO,
			String testScriptParamId, String status, String errorMessage) {

		String sql = "Update WIN_TA_TEST_SET_SCRIPT_PARAM  SET LINE_EXECUTION_STATUS='Fail',LINE_ERROR_MESSAGE= :error_message where TEST_SCRIPT_PARAM_ID='"
				+ testScriptParamId + "'";
		Session session = em.unwrap(Session.class);
		Query query = session.createSQLQuery(sql);
		query.setParameter("error_message", errorMessage);

		query.executeUpdate();

	}

	public void updateInProgressScriptLineStatus(FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO,
			String testScriptParamId, String status) {
		try {
			TestSetScriptParam scriptParam = em.find(TestSetScriptParam.class, Integer.parseInt(testScriptParamId));
			/*
			 * if(scriptParam==null) { throw new RuntimeException(); }
			 */
			if (scriptParam != null) {
				scriptParam.setLineExecutionStatus(status);
				em.merge(scriptParam);
			}
		} catch (Exception e) {
			System.out.println("cant update inprogress scriptLine status");
			System.out.println(e);
		}
	}

	public void updateInProgressScriptStatus(String testSetId, String testSetLineId, Date startDate) {
		try {
			TestSetLine testLines = em.find(TestSetLine.class, Integer.parseInt(testSetLineId));

			if (testLines != null) {
				testLines.setStatus(IN_PROGRESS.toUpperCase());
				testLines.setExecutionStartTime(startDate);
				em.merge(testLines);
			}
		} catch (Exception e) {
			System.out.println("cant update in progress script status");
			System.out.println(e);
		}
	}

	public void updateStatusOfScript(String testSetLineId, String status) {
		try {
			TestSetLine testLines = em.find(TestSetLine.class, Integer.parseInt(testSetLineId));

			/* if(testLines==null) { throw new RuntimeException(); } */
			if (testLines != null) {
				testLines.setStatus(status);
				em.merge(testLines);
			}
		} catch (Exception e) {
			System.out.println("cant update script status to - " + status);
			System.out.println(e);
		}
	}

	public String getTrMode(String testSetId, FetchConfigVO fetchConfigVO) throws SQLException {
		TestSet testSet = em.find(TestSet.class, Integer.parseInt(testSetId));
		if (testSet == null) {
			throw new SQLException();
		}
		return testSet.getTestRunMode();
	}

	public String getPassword(String testSetId, String userId, FetchConfigVO fetchConfigVO) {
		Session session = em.unwrap(Session.class);
		String password = null;
		String sqlStr = "select WIN_DBMS_CRYPTO.DECRYPT(users.password , users.encrypt_key) PASSWORD from win_ta_test_set test_set,win_ta_config config,win_ta_config_users users where test_set.configuration_id = config.configuration_id and config.configuration_id = users.config_id and test_set.test_set_id = "
				+ testSetId + " and (upper(users.user_name) = upper('" + userId + "') or ('" + userId
				+ "' is null and users.default_user = 'Y')) and rownum = 1";

		System.out.println(sqlStr);
		try {
			Query query = session.createSQLQuery(sqlStr);
			password = (String) query.getSingleResult();
		} catch (Exception e) {
			System.out.println("________NO_Password________");
		}
		return password;

	}

	public void updateEndTime(FetchConfigVO fetchConfigVO, String lineId, String testSetId, Date endTime1) {
		Format startformat = new SimpleDateFormat(SIMPLE_DATE);
		String endTime = startformat.format(endTime1);

		try {
			Session session = em.unwrap(Session.class);
			String sqlQuery = "Update WIN_TA_TEST_SET_LINES  SET EXECUTION_END_TIME=TO_TIMESTAMP('" + endTime
					+ "','MM/DD/YYYY HH24:MI:SS') WHERE  TEST_SET_ID=" + testSetId + " AND TEST_SET_LINE_ID =" + lineId;
			Query query = session.createSQLQuery(sqlQuery);
			query.executeUpdate();
		} catch (Exception e) {
			System.out.println("cannot update endTime");
			System.out.println(e);
		}
	}

	public List<Object[]> getSumDetailsFromSubscription() {
		List<Object[]> result = null;
		String qry = "select sum(quantity),sum(executed),sum(balance) from wats_subscription\r\n"
				+ "where uom = 'Script' and status='Active'  and to_date(sysdate ,'dd-mm-yyyy') >= start_date and to_date(sysdate ,'dd-mm-yyyy') <= end_date";
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
				+ "         WHERE status = 'Active' and uom = 'Script' and to_date(sysdate ,'dd-mm-yyyy') >= start_date and to_date(sysdate ,'dd-mm-yyyy') <= end_date\r\n"
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
		Integer sum = null;
		String qry = "select sum(round(WTC.GRACE_ALLOWANCE*WS.quantity/100)) \r\n"
				+ "        from wats_subscription WS,WIN_TA_CUSTOMERS WTC\r\n"
				+ "        where uom = 'Script' and status = 'Active'\r\n"
				+ "        and to_date(sysdate ,'dd-mm-yyyy') >= start_date and to_date(sysdate ,'dd-mm-yyyy') <= end_date\r\n"
				+ "        and WTC.CUSTOMER_NAME= WS.CUSTOMER_NAME and WS.subscription_id = " + subscriptionId;

		try {
			Session session = em.unwrap(Session.class);
			Query query = session.createSQLQuery(qry);
			sum = (Integer) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sum;
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
			throw new WatsEBSCustomException(500,
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
			throw new WatsEBSCustomException(500, "Exception occured while updating the Paths for test run pdfs", e);
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
			throw new WatsEBSCustomException(500,
					"Exception occured while inserting records for start date, end date and status", e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getStatusAndSeqNum(String testSetId) {
		List<Object[]> listObj = null;
		try {
			Session session = em.unwrap(Session.class);
			String execQry = "SELECT SEQ_NUM, STATUS FROM WIN_TA_TEST_SET_LINES WHERE TEST_SET_ID=" + testSetId;
			listObj = session.createSQLQuery(execQry).getResultList();
		} catch (Exception e) {
			throw new WatsEBSCustomException(605, "Unable to read records from WIN_TA_TEST_SET_LINES");
		}
		return listObj;
	}

	public void getPassAndFailCount(String testSetId, EmailParamDto emailParam) {
		String passQry = "SELECT\r\n" + "COUNT(1)\r\n" + "FROM\r\n" + "WIN_TA_TEST_SET_LINES\r\n" + "WHERE\r\n"
				+ "TEST_SET_ID = " + testSetId + "\r\n" + " AND UPPER(STATUS) = 'PASS'\r\n" + " AND ENABLED = 'Y'";
		String failQry = "SELECT\r\n" + "COUNT(1)\r\n" + "FROM\r\n" + "WIN_TA_TEST_SET_LINES\r\n" + "WHERE\r\n"
				+ "TEST_SET_ID = " + testSetId + "\r\n" + "AND UPPER(STATUS) = 'FAIL'\r\n" + "AND ENABLED = 'Y'";

		try {
			Session session = em.unwrap(Session.class);

			BigDecimal passCount = (BigDecimal) session.createSQLQuery(passQry).getSingleResult();
			BigDecimal failCount = (BigDecimal) session.createSQLQuery(failQry).getSingleResult();

			emailParam.setPassCount(passCount.intValue());
			emailParam.setFailCount(failCount.intValue());

		} catch (Exception e) {
			throw new WatsEBSCustomException(500,
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
			throw new WatsEBSCustomException(500, "Exception occured while fetching email for user.", e);
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
			throw new WatsEBSCustomException(500, "Exception occured while fetching the running process count.", e);
		}
		return count;
	}

	public Integer getCountOfInProgressScript(String testSetId) {
		Integer count = null;

		String selectQry = "SELECT COUNT(1)\r\n" + "FROM WIN_TA_TEST_SET_LINES\r\n" + "	WHERE TEST_SET_ID = "
				+ testSetId + "\r\n" + "AND UPPER(STATUS) in ('IN-PROGRESS','IN-QUEUE')";
		try {
			Session session = em.unwrap(Session.class);
			BigDecimal inProgressCount = (BigDecimal) session.createSQLQuery(selectQry).getSingleResult();
			count = inProgressCount.intValue();
		} catch (Exception e) {
			throw new WatsEBSCustomException(500, "Exception occured while fetching the running process count.", e);
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
			throw new WatsEBSCustomException(500, "Exception occured while updating the response count", e);
		}
		return id + 1;

	}

	public void updateExecStatusFlag(String testSetId) {
		String updateQry = "UPDATE EXECUTE_STATUS\r\n" + "SET\r\n" + "STATUS_FLAG = 'I'\r\n" + "WHERE\r\n"
				+ "TEST_RUN_ID = " + testSetId;

		try {
			Session session = em.unwrap(Session.class);
			session.createSQLQuery(updateQry).executeUpdate();
		} catch (Exception e) {
			throw new WatsEBSCustomException(500, "Exception occured while Updating status flag for test run script.",
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
			throw new WatsEBSCustomException(500, "Exception occured while fetching request count for test run script.",
					e);
		}
		return requestCount;
	}

	public List<Object[]> findStartAndEndTimeForTestRun(String testRunId, String scriptStatus) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
		Root<TestSetLine> from = cq.from(TestSetLine.class);
		Predicate condition1 = cb.equal(from.get(TEST_RUN).get(TEST_SET_ID), testRunId);
		Predicate condition2 = cb.equal(from.get(STATUS), scriptStatus);
		Predicate passCondtion = cb.equal(from.get(STATUS), PASS);
		Predicate failCondition = cb.equal(from.get(STATUS), FAIL);
		Predicate condition3 = (scriptStatus != null) ? cb.and(condition1, condition2)
				: cb.and(condition1, passCondtion, failCondition);

		cq.multiselect(from.get("executionStartTime"), from.get("executionEndTime")).where(condition3);
		List<Object[]> query = em.createQuery(cq).getResultList();
		return query;

	}

}
