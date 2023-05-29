package com.winfo.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class VmInstanceDAO {
	public static final Logger logger = Logger.getLogger(VmInstanceDAO.class);

	@Autowired
	private EntityManager entityManager;

	public int getInprogressAndInqueueCount() {
		Session session = entityManager.unwrap(Session.class);
		String sql = "select count(STATUS) from win_ta_test_set_lines WHERE STATUS='IN-QUEUE' or STATUS='IN-PROGRESS'";
		NativeQuery<BigDecimal> query = session.createSQLQuery(sql);

		List<BigDecimal> results = query.list();
		Integer id = 0;
		if (results != null && !results.isEmpty()) {
			logger.info("Get Inprogress and Inqueue status count " + results.get(0));
			BigDecimal bigDecimal = results.get(0);
			id = Integer.parseInt(bigDecimal.toString());
		}

		return id;
	}

	public int getNumberOfTestscriptsforTestRunId(String testRunNo) {
		Session session = entityManager.unwrap(Session.class);
		String sql = "select count(TEST_SET_ID) from win_ta_test_set_lines WHERE TEST_SET_ID=" + testRunNo
				+ " and not STATUS='Pass' and ENABLED='Y'";
		NativeQuery<BigDecimal> query = session.createSQLQuery(sql);

		List<BigDecimal> results = query.list();
		Integer id = 0;
		if (results != null && !results.isEmpty()) {
			logger.info("Count of number of scripts in the test run " + results.get(0));

			BigDecimal bigDecimal = results.get(0);
			id = Integer.parseInt(bigDecimal.toString());
		}
		return id;
	}

	public Boolean isAnyScriptsInprogresOrInqueue() {
		Session session = entityManager.unwrap(Session.class);
		String sql = "select count(STATUS) from win_ta_test_set_lines WHERE STATUS='IN-QUEUE' or STATUS='IN-PROGRESS'";
		Query<?> query = session.createSQLQuery(sql);

		BigDecimal bigDecimal = (BigDecimal) query.getSingleResult();
		Integer id = Integer.parseInt(bigDecimal.toString());
		return id != 0;
	}

	public int getMaxNoOfBrowsersForConfiguration(String testRunId) {
		Session session = entityManager.unwrap(Session.class);
		String sql = "select PARALLEL_INDEPENDENT from win_ta_config conf,win_ta_test_set ts where conf.CONFIGURATION_ID(+)=ts.CONFIGURATION_ID and ts.test_set_id="
				+ testRunId;
		NativeQuery<BigDecimal> query = session.createSQLQuery(sql);

		List<BigDecimal> results = query.list();
		Integer id = 0;
		if (results != null && !results.isEmpty()) {
			logger.info("Maximum Number Of browser count " + results.get(0));
			BigDecimal bigDecimal = results.get(0);
			id = Integer.parseInt(bigDecimal.toString());
		}
		return id;
	}

	public Map<Date, Long> getStarttimeandExecutiontime(String testSetid) {
		Map<Date, Long> timeslist = new TreeMap<>();
		try {
			Session session = entityManager.unwrap(Session.class);
			String sql = "select e.START_TIME,e.DURATION from win_ta_test_set e where e.TEST_SET_ID=" + testSetid;
			Query<?> query = session.createSQLQuery(sql);
			List<Object[]> results = (List<Object[]>) query.list();
			for (Object[] user : results) {
				logger.info("Get start time and execution duration time " + results.get(0));
				BigDecimal bigDecimal = (BigDecimal) user[1];
				long id = Long.parseLong(bigDecimal.toString());
				timeslist.put((Date) user[0], id);
			}
		} catch (Exception e) {
			logger.info("Failed to get start time and execution duration time " + e.getMessage());

		}
		return timeslist;
	}

	public void updateTestrunTimes(Date tStarttime, Date tendtime, long tdiffMinutes, String testSetid) {
		Session session = entityManager.unwrap(Session.class);
		try {
			String sql1 = "UPDATE win_ta_test_set SET START_TIME=TO_TIMESTAMP('" + tStarttime
					+ "','YYYY-MM-DD HH24:MI:SS.FF'),END_TIME=TO_TIMESTAMP('" + tendtime
					+ "','YYYY-MM-DD HH24:MI:SS.FF'),DURATION=" + tdiffMinutes + " WHERE TEST_SET_ID=" + testSetid;
			Query<?> query = session.createSQLQuery(sql1);
			query.executeUpdate();

		} catch (Exception e) {
			logger.error("Failed during update test run start, end and duration time " +e.getMessage());

		}
	}

	public void updateTestrunTimes1(Date tendtime, long tdiffMinutes, String testSetid) {
		Session session = entityManager.unwrap(Session.class);
		try {
			String sql1 = "UPDATE win_ta_test_set SET END_TIME=TO_TIMESTAMP('" + tendtime
					+ "','YYYY-MM-DD HH24:MI:SS.FF'),DURATION=" + tdiffMinutes + " WHERE TEST_SET_ID=" + testSetid;
			Query<?> query = session.createSQLQuery(sql1);
			query.executeUpdate();

		} catch (Exception e) {
			logger.error("Failed during update test run end time and duration time " +e.getMessage());

		}
	}
}
