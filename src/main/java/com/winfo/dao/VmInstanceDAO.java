package com.winfo.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.winfo.model.ScriptsData;
@Transactional
@Repository
public class VmInstanceDAO {
	Logger log = Logger.getLogger("Logger");
	
	@Autowired
	private EntityManager entityManager;


	public int getInprogressAndInqueueCount() {
		Session session = entityManager.unwrap(Session.class);
		String sql = "select count(STATUS) from win_ta_test_set_lines WHERE STATUS='IN-QUEUE' or STATUS='IN-PROGRESS'";
		NativeQuery<BigDecimal> query = session.createSQLQuery(sql);

		List<BigDecimal> results = query.list();
		Integer id = 0;
		if (results != null && !results.isEmpty()){
			System.out.println(results.get(0));
			log.info("result"+results.get(0));
			BigDecimal bigDecimal = results.get(0);
			id = Integer.parseInt(bigDecimal.toString());
		} 
		
		return id;
	}
	public int getNumberOfTestscriptsforTestRunId(String testRunNo) {
		Session session = entityManager.unwrap(Session.class);
		String sql = "select count(TEST_SET_ID) from win_ta_test_set_lines WHERE TEST_SET_ID=" + testRunNo+" and not STATUS='Pass' and ENABLED='Y'";
		NativeQuery<BigDecimal> query = session.createSQLQuery(sql);

		List<BigDecimal> results = query.list();
		Integer id = 0;
		if (results != null && !results.isEmpty()){
			System.out.println(results.get(0));
			log.info("result"+results.get(0));

			BigDecimal bigDecimal = results.get(0);
			id= Integer.parseInt(bigDecimal.toString());
		}
		return id;
	}
	public Boolean isAnyScriptsInprogresOrInqueue() {
		Session session = entityManager.unwrap(Session.class);
		String sql = "select count(STATUS) from win_ta_test_set_lines WHERE STATUS='IN-QUEUE' or STATUS='IN-PROGRESS'";
		NativeQuery<BigDecimal> query = session.createSQLQuery(sql);

		List<BigDecimal> results = query.list();
		Integer id = 0;
		if (results != null && !results.isEmpty()){
			System.out.println(results.get(0));
			log.info("result"+results.get(0));

			BigDecimal bigDecimal = results.get(0);
			 id = Integer.parseInt(bigDecimal.toString());
			return true;
		} else {
			return false;
		}
	}

	public int getMaxNoOfBrowsersForConfiguration(String testRunId) {
		Session session = entityManager.unwrap(Session.class);
		String sql = "select PARALLEL_INDEPENDENT from win_ta_config conf,win_ta_test_set ts where conf.CONFIGURATION_ID(+)=ts.CONFIGURATION_ID and ts.test_set_id="+testRunId;
		NativeQuery<BigDecimal> query = session.createSQLQuery(sql);

		List<BigDecimal> results = query.list();
		Integer id = 0;
		if (results != null && !results.isEmpty()){
			System.out.println(results.get(0));
			log.info("result"+results.get(0));

			BigDecimal bigDecimal = results.get(0);
			id= Integer.parseInt(bigDecimal.toString());
		}
		return id;
	}
}
