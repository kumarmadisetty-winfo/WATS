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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import com.gargoylesoftware.htmlunit.javascript.host.Console;
import com.winfo.model.ScriptMaster;
import com.winfo.model.TestSet;
import com.winfo.model.TestSetLines;
import com.winfo.model.TestSetScriptParam;
import com.winfo.services.FetchConfigVO;
import com.winfo.services.FetchMetadataVO;
import com.winfo.utils.Constants.BOOLEAN_STATUS;

@Repository
@RefreshScope
public class DataBaseEntryDao {
	@PersistenceContext
	EntityManager em;
	private static final String TEST_SET_ID = "testRun";
	private static final String TR_MODE = "tr_mode";

	public void updatePassedScriptLineStatus(FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO,
			String test_script_param_id, String status, String message) throws ClassNotFoundException, SQLException {
		try {
			Query query = em.createQuery("Update TestSetScriptParam set line_execution_status='" + status
					+ "',line_error_message='" + message.replace("'", "''") + "' where test_script_param_id=" + "'"
					+ test_script_param_id + "'");
			query.executeUpdate();
		} catch (Exception e) {
			System.out.println("cant update passed script line status");
			System.out.println(e);
		}
	}

	public void updatePassedScriptLineStatus(FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO,
			String test_script_param_id, String status, String value, String message)
			throws ClassNotFoundException, SQLException {
		try {
			Query query = em.createQuery("Update TestSetScriptParam set line_execution_status='" + status
					+ "',input_value='" + value + "',line_error_message='" + message.replace("'", "''")
					+ "' where test_script_param_id='" + test_script_param_id + "'");
			query.executeUpdate();
		} catch (Exception e) {
			System.out.println("cant update passed script line status");
			System.out.println(e);
		}
	}

	public void updateFailedScriptLineStatus(FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO,
			String test_script_param_id, String status, String error_message)
			throws ClassNotFoundException, SQLException {

		String sql = "Update WIN_TA_TEST_SET_SCRIPT_PARAM  SET LINE_EXECUTION_STATUS='Fail',LINE_ERROR_MESSAGE= :error_message where TEST_SCRIPT_PARAM_ID='"
				+ test_script_param_id + "'";
		Session session = em.unwrap(Session.class);
		Query query = session.createSQLQuery(sql);
		query.setParameter("error_message", error_message);

		query.executeUpdate();

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

	public void updateInProgressScriptLineStatus(FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO,
			String test_script_param_id, String status) throws ClassNotFoundException, SQLException {
		try {
			TestSetScriptParam scriptParam = em.find(TestSetScriptParam.class, Integer.parseInt(test_script_param_id));
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

	public void updateInProgressScriptStatus(FetchConfigVO fetchConfigVO, String test_set_id, String test_set_line_id)
			throws ClassNotFoundException, SQLException {
		try {
			TestSetLines testLines = em.find(TestSetLines.class, Integer.parseInt(test_set_line_id));

			/* if(testLines==null) { throw new RuntimeException(); } */
			if (testLines != null) {
				testLines.setStatus("IN-PROGRESS");
				em.merge(testLines);
			}
		} catch (Exception e) {
			System.out.println("cant update in progress script status");
			System.out.println(e);
		}
	}

	public void updateStatusOfScript(String test_set_id, String test_set_line_id, String status)
			throws ClassNotFoundException, SQLException {
		try {
			TestSetLines testLines = em.find(TestSetLines.class, Integer.parseInt(test_set_line_id));

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

	public void updateStartTime(FetchConfigVO fetchConfigVO, String line_id, String test_set_id, Date start_time1)
			throws ClassNotFoundException, SQLException {
		Format startformat = new SimpleDateFormat("M/dd/yyyy HH:mm:ss");
		String start_time = startformat.format(start_time1);
		try {
			Session session = em.unwrap(Session.class);
			Query query = session.createSQLQuery("Update WIN_TA_TEST_SET_LINES  SET EXECUTION_START_TIME=TO_TIMESTAMP('"
					+ start_time + "','MM/DD/YYYY HH24:MI:SS') WHERE TEST_SET_ID=" + test_set_id
					+ " AND TEST_SET_LINE_ID = " + line_id);
			query.executeUpdate();
		} catch (Exception e) {
			System.out.println("cant update starttime");
			System.out.println(e);
		}
	}

	public String getTrMode(String args, FetchConfigVO fetchConfigVO) throws SQLException {
		TestSet testSet = em.find(TestSet.class, Integer.parseInt(args));
		if (testSet == null) {
			throw new RuntimeException();
		}
		return testSet.getTestRunMode();
	}

	public String getPassword(String args, String userId, FetchConfigVO fetchConfigVO)
			throws SQLException, ClassNotFoundException {
		Session session = em.unwrap(Session.class);
		String password = null;
		String sqlStr = "select WIN_DBMS_CRYPTO.DECRYPT(users.password , users.encrypt_key) PASSWORD from win_ta_test_set test_set,win_ta_config config,win_ta_config_users users where test_set.configuration_id = config.configuration_id and config.configuration_id = users.config_id and test_set.test_set_id = "
				+ args + " and (upper(users.user_name) = upper('" + userId + "') or ('" + userId
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

	public void updateEndTime(FetchConfigVO fetchConfigVO, String line_id, String test_set_id, Date end_time1)
			throws ClassNotFoundException, SQLException {
		Format startformat = new SimpleDateFormat("M/dd/yyyy HH:mm:ss");
		String end_time = startformat.format(end_time1);

		try {
			Session session = em.unwrap(Session.class);
			String sqlQuery = "Update WIN_TA_TEST_SET_LINES  SET EXECUTION_END_TIME=TO_TIMESTAMP('" + end_time
					+ "','MM/DD/YYYY HH24:MI:SS') WHERE  TEST_SET_ID=" + test_set_id + " AND TEST_SET_LINE_ID ="
					+ line_id;
			Query query = session.createSQLQuery(sqlQuery);
			query.executeUpdate();
		} catch (Exception e) {
			System.out.println("cannot update endTime");
			System.out.println(e);
		}
	}

	public void updateTestSetLineStatus(String status, String testSetLineScriptPath, String testSetId,
			String testSetLineId, String scriptId, Date endDate) {
		String endTime = new SimpleDateFormat("M/dd/yyyy HH:mm:ss").format(endDate);

		try {
			Session session = em.unwrap(Session.class);
			String sqlQuery = "Update WIN_TA_TEST_SET_LINES SET STATUS='" + status + "', TEST_SET_LINE_SCRIPT_PATH='"
					+ testSetLineScriptPath + "', EXECUTION_END_TIME=TO_TIMESTAMP('" + endTime
					+ "','MM/DD/YYYY HH24:MI:SS') WHERE  TEST_SET_ID=" + testSetId + " AND TEST_SET_LINE_ID="
					+ testSetLineId + " AND SCRIPT_ID=" + scriptId;
			Query query = session.createSQLQuery(sqlQuery);
			query.executeUpdate();
		} catch (Exception e) {
			throw new WatsEBSCustomException(606,"Unable to update records on  WIN_TA_TEST_SET_LINES");
		}
	}

	public void updateTestSetPaths(String passPath, String failPath, String executionPath, String testSetId) {

		try {
			Session session = em.unwrap(Session.class);
			String sqlQuery = "Update WIN_TA_TEST_SET SET PASS_PATH ='" + passPath + "', FAIL_PATH ='" + failPath
					+ "', EXCEPTION_PATH ='" + executionPath + "'WHERE TEST_SET_ID=" + testSetId;
			Query query = session.createSQLQuery(sqlQuery);
			query.executeUpdate();
		} catch (Exception e) {
			throw new WatsEBSCustomException(607,"Unable to update the records for WIN_TA_TEST_SET table");
		}
	}

	public void updateExecHistoryTbl(String testSetLineId, Date startDate, Date endDate, String status) {
		Format dateFormat = new SimpleDateFormat("M/dd/yyyy HH:mm:ss");
		String startTime = dateFormat.format(startDate);
		String endTime = dateFormat.format(endDate);
		try {
			Session session = em.unwrap(Session.class);
			int nextExecNo = getNextExecutionNum();
			String instQry = "INSERT INTO WIN_TA_EXECUTION_HISTORY (EXECUTION_ID, TEST_SET_LINE_ID, EXECUTION_START_TIME, EXECUTION_END_TIME, STATUS) VALUES ('"
					+ (nextExecNo) + "','" + testSetLineId + "'," + "TO_TIMESTAMP('" + startTime
					+ "','MM/DD/YYYY HH24:MI:SS')" + "," + "TO_TIMESTAMP('" + endTime + "','MM/DD/YYYY HH24:MI:SS')"
					+ ",'" + status + "')";
			Query instQuery = session.createSQLQuery(instQry);
			instQuery.executeUpdate();

		} catch (Exception e) {
			throw new WatsEBSCustomException(608, "Unable to insert the records in WIN_TA_EXECUTION_HISTORY table");
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

	public void updateExecStatusTable(String testSetId) {

		try {
			Session session = em.unwrap(Session.class);
			String execQry = "SELECT RESPONSE_COUNT FROM TEST_RUN_EXECUTE_STATUS WHERE TEST_RUN_ID =" + testSetId
					+ " AND EXECUTION_ID = (SELECT MAX(EXECUTION_ID) FROM TEST_RUN_EXECUTE_STATUS WHERE TEST_RUN_ID ="
					+ testSetId + " )";
			BigDecimal bigDecimal = (BigDecimal) session.createSQLQuery(execQry).getSingleResult();
			Integer id = Integer.parseInt(bigDecimal.toString());
			String updateQry = "UPDATE TEST_RUN_EXECUTE_STATUS SET RESPONSE_COUNT =" + (id + 1)
					+ " WHERE TEST_RUN_ID = " + testSetId
					+ " AND EXECUTION_ID = (SELECT MAX(EXECUTION_ID) FROM TEST_RUN_EXECUTE_STATUS WHERE TEST_RUN_ID = "
					+ testSetId + " )";
			session.createSQLQuery(updateQry).executeUpdate();
		} catch (Exception e) {
			throw new WatsEBSCustomException(608, "Unable to update the records");
		}

	}

	public int getNextExecutionNum() {
		Session session = em.unwrap(Session.class);
		String sql = "SELECT WIN__TA_EXECUTION_ID_SEQ.NEXTVAL FROM DUAL";
		SQLQuery query = session.createSQLQuery(sql);

		List results = query.list();
		if (results.size() > 0) {
			System.out.println(results.get(0));

			BigDecimal bigDecimal = (BigDecimal) results.get(0);
			Integer id = Integer.parseInt(bigDecimal.toString());
			return id;
		} else {
			return 0;
		}
	}

	public void updateFailedImages(FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO,
			String test_script_param_id) throws SQLException {
		try {
			String folder = (fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION() + fetchMetadataVO.getCustomer_name() + "\\"

					+ fetchMetadataVO.getTest_run_name() + "\\" + fetchMetadataVO.getSeq_num() + "_"

					+ fetchMetadataVO.getLine_number() + "_" + fetchMetadataVO.getScenario_name() + "_"

					+ fetchMetadataVO.getScript_number() + "_" + fetchMetadataVO.getTest_run_name() + "_"

					+ fetchMetadataVO.getLine_number() + "_Passed").concat(".jpg");

			File file = new File(folder);
			byte[] screenshotArray = new byte[(int) file.length()];
			try {
				FileInputStream fileInputStream = new FileInputStream(file);
				fileInputStream.read(screenshotArray);
				fileInputStream.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String sql = "Update WIN_TA_TEST_SET_SCRIPT_PARAM  SET SCREENSHOT= :screenshot where TEST_SCRIPT_PARAM_ID='"
					+ test_script_param_id + "'";
			Query query = em.unwrap(Session.class).createSQLQuery(sql);
			query.setParameter("screenshot", screenshotArray);
			query.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public Map<String, Map<String, TestSetScriptParam>> getTestRunMap(String testRunId) {
		Map<String, Map<String, TestSetScriptParam>> map = new HashMap<String, Map<String, TestSetScriptParam>>();
		String sql = "from TestSetLines where TEST_SET_ID=:testRunId";
		Integer test_run_id2 = Integer.parseInt(testRunId);
		Query query = em.createQuery(sql);
		query.setParameter("testRunId", em.find(TestSet.class, test_run_id2));
		List<TestSetLines> test_set_lines_list = query.getResultList();
		for (TestSetLines test_set_line : test_set_lines_list) {
			Map<String, TestSetScriptParam> map2 = getTestScriptMap(test_set_line);
			map.put(String.valueOf(test_set_line.getSeqNum()), map2);

		}
		return map;

	}

	public Map<String, TestSetScriptParam> getTestScriptMap(TestSetLines testSetLine) {
		String sql = "from TestSetScriptParam where testSetLines=:testSetLines";
		Query query = em.createQuery(sql);
		query.setParameter("testSetLines", testSetLine);
		List<TestSetScriptParam> testScriptParamList = query.getResultList();
		Map<String, TestSetScriptParam> map2 = new HashMap<String, TestSetScriptParam>();
		for (TestSetScriptParam scriptParam : testScriptParamList) {
			map2.put(String.valueOf(scriptParam.getLineNumber()), scriptParam);
		}
		// map.put(String.valueOf(test_set_line.getSeq_num()),map2);
		return map2;
	}

	public TestSetLines getTestSetLine(String test_set_line_id) {
		return em.find(TestSetLines.class, Integer.parseInt(test_set_line_id));
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

	public TestSetLines checkTestSetLinesByScriptId(int testSetId, int scriptId) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TestSetLines> cq = cb.createQuery(TestSetLines.class);
		Root<TestSetLines> from = cq.from(TestSetLines.class);

		Predicate condition1 = cb.equal(from.get("script_id"), scriptId);
		Predicate condition2 = cb.equal(from.get("testSet").get("test_set_id"), testSetId);
		Predicate condition = cb.and(condition1, condition2);
		cq.where(condition);
		Query query = em.createQuery(cq);
		TestSetLines result = (TestSetLines) query.getSingleResult();
		em.refresh(result);

		return result;
	}

	@SuppressWarnings("unchecked")
	public List<String> getTestSetLinesStatusByTestSetId(long testSetId, Boolean enable) {
		List<String> result = null;
		try {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TestSetLines> cq = cb.createQuery(TestSetLines.class);
		Root<TestSetLines> from = cq.from(TestSetLines.class);

		Predicate condition1 = cb.equal(from.get("testRun").get("testRunId"), testSetId);
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
		Query query = em.createQuery(cq.select(from.get("status")));
		result = query.getResultList();
		} catch(Exception e) {
			throw new WatsEBSCustomException(600, "Unable to read the records");
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
			throw new WatsEBSCustomException(604,"Unable to read status from win_ta_test_set_lines");
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
				+ "          ,ex_st.EXECUTED_BY    EXECUTED_BY\r\n" + "      from\r\n"
				+ "      execute_status ex_st,\r\n" + "      win_ta_test_set        wtts,\r\n"
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
						.setExecuted_by(NULL_STRING.equals(String.valueOf(obj[23])) ? null : String.valueOf(obj[23]));

				listOfTestRunExecutionVo.add(testRunExecutionVO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new WatsEBSCustomException(600, "Unable to read the records from Tables",e);
		}
		return listOfTestRunExecutionVo;
	}

	public String getTestSetMode(Long testSetId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> query = cb.createQuery(String.class);
		Root<TestSet> root = query.from(TestSet.class);
		Predicate condition = cb.equal(root.get(TEST_SET_ID), testSetId);
		query.select(root.get(TR_MODE)).where(condition);
		return em.createQuery(query).getSingleResult();

	}

	public String getTestSetPdfGenerationEnableStatus(Long testSetId) {
		try {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> query = cb.createQuery(String.class);
		Root<TestSet> root = query.from(TestSet.class);
		Predicate condition = cb.equal(root.get("testRunId"), testSetId);
		query.select(root.get("pdfGenerationEnabled")).where(condition);
		return em.createQuery(query).getSingleResult();
		} catch(Exception e) {
			throw new WatsEBSCustomException(600, "Unable to read the records");
		}

	}

	public ArrayList<String> getStepsStatusByScriptId(int testSetLineId) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<TestSetScriptParam> from = cq.from(TestSetScriptParam.class);

		Predicate condition = cb.equal(from.get("testSetLines").get("testRunScriptId"), testSetLineId);
		cq.where(condition);
		Query query = em.createQuery(cq.select(from.get("lineExecutionStatus")));
		ArrayList<String> result = (ArrayList<String>) query.getResultList();

		return result;
	}

	public TestSetLines getScript(long testSetId, long testSetLineId) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TestSetLines> cq = cb.createQuery(TestSetLines.class);
		Root<TestSetLines> from = cq.from(TestSetLines.class);

		Predicate condition1 = cb.equal(from.get("testRunScriptId"), testSetLineId);
		Predicate condition2 = cb.equal(from.get("testRun").get("testRunId"), testSetId);
		Predicate condition = cb.and(condition1, condition2);
		cq.where(condition);
		Query query = em.createQuery(cq.select(from));
		return (TestSetLines) query.getSingleResult();

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
			throw new WatsEBSCustomException(601, "Records are not available");
		}
	}

	public void updatePdfGenerationEnableStatus(String testSetId, String enabled) {
		try {
			Query query = em.createQuery(
					"Update TestSet set pdfGenerationEnabled='" + enabled + "' where test_set_id='" + testSetId + "'");
			query.executeUpdate();
		} catch (Exception e) {
			System.out.println("Error Updation PDF Generation Status");
			System.out.println(e);
		}
	}

	public Date findMaxExecutionEndDate(long testSetId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Date> cq = cb.createQuery(Date.class);
		Root<TestSetLines> from = cq.from(TestSetLines.class);
		Predicate condition = cb.equal(from.get("testRun").get("testRunId"), testSetId);

		cq.select(cb.greatest(from.<Date>get("executionEndTime"))).where(condition);
		Date query = em.createQuery(cq).getSingleResult();
		return query;

	}

	public Date findMinExecutionStartDate(long testSetId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Date> cq = cb.createQuery(Date.class);
		Root<TestSetLines> from = cq.from(TestSetLines.class);
		Predicate condition = cb.equal(from.get("testRun").get("testRunId"), testSetId);

		cq.select(cb.least(from.<Date>get("executionStartTime"))).where(condition);
		Date query = em.createQuery(cq).getSingleResult();
		return query;

	}

	public Integer findFirstStepIdInScript(String testSetLineId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
		Root<TestSetScriptParam> from = cq.from(TestSetScriptParam.class);
		Predicate condition = cb.equal(from.get("testSetLines").get("testRunScriptId"), testSetLineId);
		cq.select(from.get("testRunScriptParamId")).where(condition);
		cq.orderBy(cb.asc(from.get("lineNumber")));
		Integer result = em.createQuery(cq).setMaxResults(1).getSingleResult();
		return result;

	}

}
