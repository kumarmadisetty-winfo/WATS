package com.winfo.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.winfo.exception.WatsEBSCustomException;
import com.winfo.model.ExecutionAudit;
import com.winfo.services.TestCaseDataService;

@Repository
public class LimitScriptExecutionDao {
	Logger logger = LogManager.getLogger(TestCaseDataService.class);
	@Autowired
	private EntityManager entityManager;

	public int getLimitedCountForConfiguration(String testRunNo) {
		Session session = entityManager.unwrap(Session.class);
		String sql = "select count(STATUS) from win_ta_test_set_lines WHERE STATUS='IN-QUEUE' or STATUS='IN-PROGRESS'";
		NativeQuery<BigDecimal> query = session.createSQLQuery(sql);

		List<BigDecimal> results = query.list();
		Integer id = 0;
		if (results != null && !results.isEmpty()) {
			System.out.println(results.get(0));
			logger.info("result" + results.get(0));
			BigDecimal bigDecimal = results.get(0);
			id = Integer.parseInt(bigDecimal.toString());
		}

		return id;
	}

	public void insertTestrundata(ExecutionAudit executionAudit) {
		logger.info("executionAudit savaed");
		entityManager.persist(executionAudit);

	}

	public Long findCountOfExecAuditRecords(ExecutionAudit executionAudit) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<ExecutionAudit> from = cq.from(ExecutionAudit.class);
		Predicate condition1 = cb.equal(from.get("testSetId"), executionAudit.getTestsetid());
		Predicate condition2 = cb.equal(from.get("scriptId"), executionAudit.getScriptid());
		Predicate condition3 = cb.equal(from.get("scriptNumber"), executionAudit.getScriptnumber());
		Predicate condition4 = cb.equal(from.get("executionStartTime"), executionAudit.getExecutionstarttime());
		Predicate condition = cb.and(condition1, condition2, condition3, condition4);
		cq.select(cb.count(from)).where(condition);
		return entityManager.createQuery(cq).getSingleResult();

	}

	public int getPassedScriptsCount(String startDate, String endDate) {
		Session session = entityManager.unwrap(Session.class);
		String sql = "select count(STATUS) from WIN_TA_EXECUTION_AUDIT where status='pass'and execution_end_time BETWEEN '"
				+ startDate + "' AND '" + endDate + "'";
		NativeQuery<BigDecimal> query = session.createSQLQuery(sql);

		List<BigDecimal> results = query.list();
		Integer id = 0;
		if (results != null && !results.isEmpty()) {
			System.out.println(results.get(0));
			logger.info("result" + results.get(0));
			BigDecimal bigDecimal = results.get(0);
			id = Integer.parseInt(bigDecimal.toString());
		}

		return id;
	}

	public String getToMailId(String name) {
		Session session = entityManager.unwrap(Session.class);
		String sql = "select EMAIL from WIN_TA_USERS where USER_ID='" + name + "'";
		NativeQuery<String> query = session.createSQLQuery(sql);

		List<String> results = query.list();
		String mailId = null;
		if (results != null && !results.isEmpty()) {
			System.out.println(results.get(0));
			logger.info("result" + results.get(0));
			mailId = results.get(0);
		}
		return mailId;
	}

	public String getCCmailId(String testRunId) {
		Session session = entityManager.unwrap(Session.class);
		String sql = "SELECT PROJECT_MANAGER_EMAIL FROM WIN_TA_TEST_SET TS,WIN_TA_PROJECTS TP WHERE TS.PROJECT_ID = TP.PROJECT_ID AND TS.TEST_SET_ID = '"
				+ testRunId + "'";
		NativeQuery<String> query = session.createSQLQuery(sql);

		List<String> results = query.list();
		String mailId = null;
		if (results != null && !results.isEmpty()) {
			System.out.println(results.get(0));
			logger.info("result" + results.get(0));
			mailId = results.get(0);
		}
		return mailId;
	}

	public int getFailScriptRunCount(String testSetLineId, String testSetId) {
		Session session = entityManager.unwrap(Session.class);
		String sql = "SELECT RUN_COUNT from WIN_TA_TEST_SET_LINES where TEST_SET_LINE_ID=" + testSetLineId
				+ " AND TEST_SET_ID=" + testSetId + "";
		Integer id = 0;
		try {
			NativeQuery<BigDecimal> query = session.createSQLQuery(sql);

			List<BigDecimal> results = query.list();
			if (results != null && !results.isEmpty()) {
				logger.info("result" + results.get(0));
				BigDecimal bigDecimal = results.get(0);
				id = bigDecimal != null ? Integer.parseInt(bigDecimal.toString()) : 0;
			}
		} catch (Exception e) {
			throw new WatsEBSCustomException(500,
					"Exception occured while selecting the run count for Script level pdf", e);
		}
		return id;
	}

	public void updateFailScriptRunCount(int failedScriptRunCount, String testSetId, String testSetLineId) {
		try {
			Session session = entityManager.unwrap(Session.class);
			String sql1 = "UPDATE WIN_TA_TEST_SET_LINES SET RUN_COUNT=" + failedScriptRunCount
					+ " WHERE TEST_SET_LINE_ID=" + testSetLineId + " AND TEST_SET_ID=" + testSetId + "";
			Query query = session.createSQLQuery(sql1);
			query.executeUpdate();

		} catch (Exception e) {
			throw new WatsEBSCustomException(500,
					"Exception occured while updating the fail run count for script level pdf", e);
		}
	}

	public int getFailedScriptRunCount(String testSetLineId, String testSetId) {
		Session session = entityManager.unwrap(Session.class);
		String sql = "SELECT RUN_COUNT from WIN_TA_TEST_SET_LINES where TEST_SET_LINE_ID=" + testSetLineId
				+ " AND TEST_SET_ID=" + testSetId + "";
		Integer id = 0;
		try {
			NativeQuery<BigDecimal> query = session.createSQLQuery(sql);

			List<BigDecimal> results = query.list();
			if (results != null && !results.isEmpty()) {
				logger.info("result" + results.get(0));
				BigDecimal bigDecimal = results.get(0);
				id = Integer.parseInt(bigDecimal.toString());
			}
		} catch (Exception e) {
			throw new WatsEBSCustomException(500, "Exception occured while fetching the run count for script level pdf",
					e);
		}
		int failedScriptRunCount = id + 1;
		try {
			String sql1 = "UPDATE WIN_TA_TEST_SET_LINES SET RUN_COUNT=" + failedScriptRunCount
					+ " WHERE TEST_SET_LINE_ID=" + testSetLineId + " AND TEST_SET_ID=" + testSetId + "";
			Query query = session.createSQLQuery(sql1);
			query.executeUpdate();

		} catch (Exception e) {
			throw new WatsEBSCustomException(500, "Exception occured while updating run count for Script level pdf", e);
		}

		return failedScriptRunCount;
	}

	public void updateFaileScriptscount(String testSetLineId, String testSetId) {
		int failedScriptRunCount = 0;
		Session session = entityManager.unwrap(Session.class);
		try {
			String sql1 = "UPDATE WIN_TA_TEST_SET_LINES SET RUN_COUNT=" + failedScriptRunCount
					+ " WHERE TEST_SET_LINE_ID=" + testSetLineId + " AND TEST_SET_ID=" + testSetId + "";
			Query query = session.createSQLQuery(sql1);
			query.executeUpdate();

		} catch (Exception e) {
			throw new WatsEBSCustomException(500, "Exception occured while updating the run count for script level pdf",
					e);
		}

	}

}
