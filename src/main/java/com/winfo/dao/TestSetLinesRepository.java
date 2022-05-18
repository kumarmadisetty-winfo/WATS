package com.winfo.dao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Repository;

import com.winfo.model.TestSetLines;
import com.winfo.services.FetchConfigVO;
import com.winfo.utils.Constants.BOOLEAN_STATUS;

@Repository
@RefreshScope
public class TestSetLinesRepository {
	@PersistenceContext
	EntityManager em;
	
	public TestSetLines getScript(long testSetId, long testSetLineId) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TestSetLines> cq = cb.createQuery(TestSetLines.class);
		Root<TestSetLines> from = cq.from(TestSetLines.class);

		Predicate condition1 = cb.equal(from.get("test_set_line_id"), testSetLineId);
		Predicate condition2 = cb.equal(from.get("testSet").get("test_set_id"), testSetId);
		Predicate condition = cb.and(condition1, condition2);
		cq.where(condition);
		Query query = em.createQuery(cq.select(from));
		return (TestSetLines) query.getSingleResult();

	}
	
	public void updateFaileScriptscount(String testSetLineId, String testSetId) {
		int failedScriptRunCount=0;
		Session session = em.unwrap(Session.class);
		try {
			String sql1 = "UPDATE WIN_TA_TEST_SET_LINES SET RUN_COUNT=" + failedScriptRunCount
					+ " WHERE TEST_SET_LINE_ID=" + testSetLineId + " AND TEST_SET_ID=" + testSetId + "";
			Query query = session.createSQLQuery(sql1);
			query.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public int getFailedScriptRunCount(String testSetLineId, String testSetId) {
		Session session = em.unwrap(Session.class);
		String sql = "SELECT RUN_COUNT from WIN_TA_TEST_SET_LINES where TEST_SET_LINE_ID=" + testSetLineId
				+ " AND TEST_SET_ID=" + testSetId + "";
		Integer id = 0;
		try {
			NativeQuery<BigDecimal> query = session.createSQLQuery(sql);

			List<BigDecimal> results = query.list();
			if (results != null && !results.isEmpty()) {
				System.out.println(results.get(0));
				BigDecimal bigDecimal = results.get(0);
				id = Integer.parseInt(bigDecimal.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int failedScriptRunCount=id + 1;
		try {
			String sql1 = "UPDATE WIN_TA_TEST_SET_LINES SET RUN_COUNT=" + failedScriptRunCount
					+ " WHERE TEST_SET_LINE_ID=" + testSetLineId + " AND TEST_SET_ID=" + testSetId + "";
			Query query = session.createSQLQuery(sql1);
			query.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return failedScriptRunCount;
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
			e.printStackTrace();
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getStatusAndSeqNum(String testSetId) {
		List<Object[]> listObj = null;
		try {
			Session session = em.unwrap(Session.class);
			String execQry = "SELECT SEQ_NUM, STATUS FROM WIN_TA_TEST_SET_LINES WHERE TEST_SET_ID="+testSetId;
			listObj = session.createSQLQuery(execQry).getResultList();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return listObj;
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
			System.out.println("cannot update Status");
			System.out.println(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getTestSetLinesStatusByTestSetId(long testSetId, Boolean enable) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TestSetLines> cq = cb.createQuery(TestSetLines.class);
		Root<TestSetLines> from = cq.from(TestSetLines.class);

		Predicate condition1 = cb.equal(from.get("testSet").get("test_set_id"), testSetId);
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
		return query.getResultList();
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
			e.printStackTrace();
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
			e.printStackTrace();
		}

	}


}
