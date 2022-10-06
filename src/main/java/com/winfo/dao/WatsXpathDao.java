package com.winfo.dao;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@SuppressWarnings("deprecation")
@Repository
public class WatsXpathDao {

	Logger log = Logger.getLogger(WatsXpathDao.class);

	private static final String SCRIPT_ID = "scriptID";

	@Autowired
	private EntityManager entityManager;

	public int saveXpathParams(String scriptID, String lineNumber, String xpath) {
		return 0;
//		Session session = entityManager.unwrap(Session.class);
//		String sql = "UPDATE win_ta_script_metadata m SET m.xpath_location =:xpath WHERE m.script_id=:scriptID and m.line_number=:line_number";
//		SQLQuery<?> query = session.createSQLQuery(sql);
//		query.setParameter("xpath", xpath);
//		query.setParameter(SCRIPT_ID, scriptID);
//		query.setParameter("line_number", lineNumber);
//		return query.executeUpdate();
	}

//	public String getXpathParams(String scriptID, String lineNumber) {
//
//		Session session = entityManager.unwrap(Session.class);
//		String sql = "select m.xpath_location from  win_ta_script_metadata m WHERE m.script_id=:scriptID and m.line_number=:line_number";
//		SQLQuery<?> query = session.createSQLQuery(sql);
//		query.setParameter(SCRIPT_ID, scriptID);
//		query.setParameter("line_number", lineNumber);
//		List<?> results = query.list();
//		if (!results.isEmpty()) {
//			return (String) results.get(0);
//		} else {
//			return null;
//		}
//	}
	
	public String getXpathParams(String scriptID, String lineNumber,String testSetLine) {

		Session session = entityManager.unwrap(Session.class);
		String sql = "select m.xpath_location from  WIN_TA_TEST_SET_SCRIPT_PARAM m WHERE m.script_id=:scriptID and m.line_number=:line_number and m.test_set_line_id=:testSetLine";
		SQLQuery<?> query = session.createSQLQuery(sql);
		query.setParameter(SCRIPT_ID, scriptID);
		query.setParameter("line_number", lineNumber);
		query.setParameter("testSetLine", testSetLine);
		List<?> results = query.list();
		if (!results.isEmpty()) {
			return (String) results.get(0);
		} else {
			return null;
		}
	}

	public Timestamp executionDate(String scriptID) {
		Session session = entityManager.unwrap(Session.class);
		String sql = "select CAST(EXECUTION_START_TIME AS DATE)  from WIN_TA_TEST_SET_LINES where script_id=:scriptID and status='Pass' order by EXECUTION_START_TIME desc fetch first 1 rows only";
		SQLQuery<?> query = session.createSQLQuery(sql);
		query.setParameter(SCRIPT_ID, scriptID);
		List<?> results = query.list();
		log.info("results1:::" + results);
		if (!results.isEmpty()) {
			java.sql.Timestamp obj = (java.sql.Timestamp) results.get(0);
			log.info("executionDate::::::" + obj);
			return obj;
		} else {
			return null;
		}
	}

	public Timestamp executionEndDate(String scriptID) {
		Session session = entityManager.unwrap(Session.class);
		String sql = "select CAST(EXECUTION_END_TIME AS DATE)  from WIN_TA_TEST_SET_LINES where script_id=:scriptID and status='Pass' order by EXECUTION_END_TIME desc fetch first 1 rows only";
		SQLQuery<?> query = session.createSQLQuery(sql);
		query.setParameter(SCRIPT_ID, scriptID);
		List<?> results = query.list();
		log.info("results1:;:" + results);
		if (!results.isEmpty()) {
			java.sql.Timestamp obj = (java.sql.Timestamp) results.get(0);
			log.info("executionDate::::::" + obj);
			return obj;
		} else {
			return null;
		}
	}

	public Timestamp scriptUpdateDate(String scriptID) {
		Session session = entityManager.unwrap(Session.class);
		String sql = "select update_date from win_ta_script_metadata where script_id=:scriptID order by update_date desc fetch first 1 rows only";
		SQLQuery<?> query = session.createSQLQuery(sql);
		query.setParameter(SCRIPT_ID, scriptID);
		List<?> results = query.list();
		log.info("results1:;:" + results);
		if (!results.isEmpty()) {
			java.sql.Timestamp obj = (java.sql.Timestamp) results.get(0);
			log.info("UpdateDate::::::" + obj);
			return obj;
		} else {
			return null;
		}
	}

	/*
	 * public int saveXpathParams(String params, String scripNumber, String xpath,
	 * String action, String lineNumber) { Session session =
	 * entityManager.unwrap(Session.class); String params1="%"+params; String sql=
	 * "UPDATE win_ta_script_metadata m SET m.xpath_location =:xpath WHERE m.script_number=:scripNumber and m.action=:action and m.line_number=:lineNumber and m.input_parameter like :params"
	 * ; SQLQuery query = session.createSQLQuery(sql); query.setParameter("xpath",
	 * xpath); query.setParameter("scripNumber", scripNumber);
	 * query.setParameter("params", params1); query.setParameter("action", action);
	 * query.setParameter("lineNumber", lineNumber); int i =query.executeUpdate();
	 * //System.out.println("update:::::"+i); return i;
	 * 
	 * }
	 * 
	 * public void saveXpathParam1(String param1, String scripNumber, String xpath)
	 * { Session session = entityManager.unwrap(Session.class); String
	 * params1="%"+param1+"%"; String sql=
	 * "UPDATE win_ta_script_metadata m SET m.xpath_location =:xpath WHERE m.script_number=:scripNumber and m.input_parameter like :params"
	 * ; SQLQuery query = session.createSQLQuery(sql); query.setParameter("xpath",
	 * xpath); query.setParameter("scripNumber", scripNumber);
	 * query.setParameter("params", params1); int i =query.executeUpdate();
	 * //System.out.println("update:::::"+i); }
	 * 
	 * public String checkXpathlocation(String params, String scripNumber) { Session
	 * session = entityManager.unwrap(Session.class); String params1="%"+params;
	 * String
	 * sql="select xpath_location from  win_ta_script_metadata where  script_number=:scripNumber and input_parameter like :params"
	 * ; SQLQuery query = session.createSQLQuery(sql);
	 * query.setParameter("scripNumber", scripNumber); query.setParameter("params",
	 * params1); List results = query.list(); if(results.size()>0) {
	 * //System.out.println("xpath::::::"+(String) results.get(0)); return (String)
	 * results.get(0); } else { return null; } } public String
	 * checkXpathlocation1(String params, String scriptID, String action) { Session
	 * session = entityManager.unwrap(Session.class); String params1="%"+params;
	 * String
	 * sql="select xpath_location from  win_ta_script_metadata where  SCRIPT_ID=:scriptID and action=:action  and input_parameter like :params"
	 * ; SQLQuery query = session.createSQLQuery(sql);
	 * query.setParameter("scriptID", scriptID); query.setParameter("params",
	 * params1); query.setParameter("action", action); List results = query.list();
	 * if(results.size()>0) { //System.out.println("xpath::::::"+(String)
	 * results.get(0)); return (String) results.get(0); } else { return null; } }
	 */

}