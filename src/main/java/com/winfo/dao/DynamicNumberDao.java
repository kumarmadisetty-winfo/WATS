package com.winfo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@SuppressWarnings("deprecation")
@Repository
public class DynamicNumberDao {

	@Autowired
	private EntityManager entityManager;

	private final Logger logger = LogManager.getLogger(DynamicNumberDao.class);

	public void saveCopyNumber(String value, String testParamId, String testSetId) {
		Session session = entityManager.unwrap(Session.class);
		String sql = "UPDATE WIN_TA_TEST_SET_SCRIPT_PARAM SET input_value=:input where TEST_SCRIPT_PARAM_ID=:testParamId and TEST_SET_LINE_ID=:testSetId";
		SQLQuery<?> query = session.createSQLQuery(sql);
		query.setParameter("input", value);
		query.setParameter("testParamId", testParamId);
		query.setParameter("testSetId", testSetId);
		int i = query.executeUpdate();
		logger.info("DynamicValueUpdate:::::{}", i);
	}

	public String getCopynumber(String testRunName, String seq, String lineNumber) {

		Session session = entityManager.unwrap(Session.class);
		String sql = "select input_value from win_ta_test_set_script_param where line_number=:line_number and test_set_line_id=(select test_set_line_id from win_ta_test_set_lines where test_set_id=(select test_set_id from win_ta_test_set where test_set_name=:testrun_name) and seq_num=:seq)";
		SQLQuery<?> query = session.createSQLQuery(sql);
		query.setParameter("testrun_name", testRunName);
		query.setParameter("seq", seq);
		query.setParameter("line_number", lineNumber);
		List<?> results = query.list();
		if (!results.isEmpty()) {
			logger.info("getCopyNumber:::::: {}", results.get(0));
			return (String) results.get(0);
		} else {
			return null;
		}
	}

	public void getTestSetParamIdWithCopyAction(String key, String value, String testSetLineId) {
		Session session = entityManager.unwrap(Session.class);

		String sql = "UPDATE WIN_TA_TEST_SET_SCRIPT_PARAM SET input_value=:input where  TEST_SET_LINE_ID=:testSetLineId and action='ebsCopyValue' and input_parameter=:key";
		SQLQuery<?> query = session.createSQLQuery(sql);
		query.setParameter("input", value);
		query.setParameter("testSetLineId", testSetLineId);
		query.setParameter("key", key);
		int i = query.executeUpdate();
		logger.info("DynamicValueupdate::::: {}", i);
	}

	public String getCopynumberInputParameter(String testrunName, String seq, String lineNumber) {

		Session session = entityManager.unwrap(Session.class);
		String sql = "select input_parameter from win_ta_test_set_script_param where line_number=:line_number and test_set_line_id=(select test_set_line_id from win_ta_test_set_lines where test_set_id=(select test_set_id from win_ta_test_set where test_set_name=:testrun_name) and seq_num=:seq)";
		SQLQuery<?> query = session.createSQLQuery(sql);
		query.setParameter("testrun_name", testrunName);
		query.setParameter("seq", seq);
		query.setParameter("line_number", lineNumber);
		List<?> results = query.list();
		if (!results.isEmpty()) {
			logger.info("getCopyNumber:::::: {}", results.get(0));
			return (String) results.get(0);
		} else {
			return null;
		}
	}
	
	public List<String> getResponseCode(String test_set_id) {

		Session session = entityManager.unwrap(Session.class);
		String sql="select input_value from win_ta_test_set_script_param where action = 'apiValidationResponse' and LINE_EXECUTION_STATUS in ('Pass','PASS') and test_set_line_id in (select test_set_line_id from win_ta_test_set_lines where test_set_id = :test_set_id)";
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter("test_set_id", test_set_id);
		List results = query.list();
		if(results.size()>0) {
			System.out.println("getCopyNumber::::::"+(String) results.get(0));
			String copynumberValue=(String) results.get(0);
			//saveCopyNumber(copynumberValue,testParamId,testSetId);
			return results;
		}
		else {
			return null;
		}
	}

}
