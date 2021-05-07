package com.winfo.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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

	public int getPassedScriptsCount() {
		Session session = entityManager.unwrap(Session.class);
		String sql = "select count(STATUS) from WIN_TA_EXECUTION_AUDIT where status='pass'and execution_end_time BETWEEN '06-02-2021 10:04:02:AM' AND '28-04-2021 11:04:02:AM'";
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


	public String getMailId(String name) {
		Session session = entityManager.unwrap(Session.class);
		String sql = "select EMAIL from WIN_TA_USERS where USER_ID='"+name+"'";
		NativeQuery<String> query = session.createSQLQuery(sql);

		List<String> results = query.list();
		String mailId =null;
		if (results != null && !results.isEmpty()) {
			System.out.println(results.get(0));
			logger.info("result" + results.get(0));
			mailId = results.get(0);
		}
		return mailId;
	}


	public String getCCmailId(String testRunId) {
		Session session = entityManager.unwrap(Session.class);
		String sql = "	SELECT PROJECT_MANAGER_EMAIL FROM WIN_TA_TEST_SET TS,WIN_TA_PROJECTS TP WHERE TS.PROJECT_ID = TP.PROJECT_ID AND TS.TEST_SET_ID = :"+testRunId+"";
		NativeQuery<String> query = session.createSQLQuery(sql);

		List<String> results = query.list();
		String mailId =null;
		if (results != null && !results.isEmpty()) {
			System.out.println(results.get(0));
			logger.info("result" + results.get(0));
			mailId = results.get(0);
		}
		return mailId;		
	}

}
