package com.winfo.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class WatsXpathDao {

	@Autowired
	private EntityManager entityManager;

	public int saveXpathParams(String scriptID, String metadataID, String xpath) {

		Session session = entityManager.unwrap(Session.class);
  String sql= "UPDATE win_ta_script_metadata m SET m.xpath_location =:xpath WHERE m.script_id=:scriptID and m.script_meta_data_id=:metadataID;";
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter("xpath", xpath);
		query.setParameter("scriptID", scriptID);
		query.setParameter("metadataID", metadataID);
		int i =query.executeUpdate();
		//System.out.println("update:::::"+i);
		return i;
	}

	public String getXpathParams(String scriptID, String metadataID) {
		
		Session session = entityManager.unwrap(Session.class);
		String sql="select m.xpath_location from  win_ta_script_metadata m WHERE m.script_id=:scriptID and m.script_meta_data_id=:metadataID;";
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter("scriptID", scriptID);
		query.setParameter("metadataID", metadataID);
		List results = query.list();
		if(results.size()>0) {
			//System.out.println("xpath::::::"+(String) results.get(0));
			return (String) results.get(0);
		}
		else {
			return null;
		}
	}
	public Timestamp executionDate(String scriptID) {
		Session session = entityManager.unwrap(Session.class);
		String sql="select CAST(EXECUTION_START_TIME AS DATE)  from WIN_TA_TEST_SET_LINES where script_id=:scriptID and status='Pass' order by EXECUTION_START_TIME desc fetch first 1 rows only;";
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter("scriptID", scriptID);
		List results = query.list();
		System.out.println("results1:;:"+results);
		if(!results.isEmpty()) {
			java.sql.Timestamp  obj= (java.sql.Timestamp)results.get(0);
//		    java.sql.Timestamp ts2 = java.sql.Timestamp.valueOf("2005-04-06 09:01:10");

//			  Timestamp ts=new Timestamp(obj);  
           //   Date date=new Date(obj.getTime()); 
			System.out.println("executionDate::::::"+obj);
			return obj;
		}
		else {
			return null;
		}
	}

	public Timestamp scriptUpdateDate(String scriptID) {
		Session session = entityManager.unwrap(Session.class);
		String sql="select update_date from win_ta_script_metadata where script_id=:scriptID order by update_date desc fetch first 1 rows only";
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter("scriptID", scriptID);
		List results = query.list();
		System.out.println("results1:;:"+results);

		if(!results.isEmpty()) {
			java.sql.Timestamp  obj= (java.sql.Timestamp)results.get(0);

			System.out.println("UpdateDate::::::"+obj);
			return obj;
		} 
		else {
			return null;
		}
	}

//	public int saveXpathParams(String params, String scripNumber, String xpath, String action, String lineNumber) {
//		Session session = entityManager.unwrap(Session.class);
//		String params1="%"+params;
//  String sql= "UPDATE win_ta_script_metadata m SET m.xpath_location =:xpath WHERE m.script_number=:scripNumber and m.action=:action and m.line_number=:lineNumber and m.input_parameter like :params";
//		SQLQuery query = session.createSQLQuery(sql);
//		query.setParameter("xpath", xpath);
//		query.setParameter("scripNumber", scripNumber);
//		query.setParameter("params", params1);
//		query.setParameter("action", action);
//		query.setParameter("lineNumber", lineNumber);
//		int i =query.executeUpdate();
//		//System.out.println("update:::::"+i);
//		return i;
//
//	}
//
//	public void saveXpathParam1(String param1, String scripNumber, String xpath) {
//		Session session = entityManager.unwrap(Session.class);
//		String params1="%"+param1+"%";
//		String sql= "UPDATE win_ta_script_metadata m SET m.xpath_location =:xpath WHERE m.script_number=:scripNumber and m.input_parameter like :params";
//		SQLQuery query = session.createSQLQuery(sql);
//		query.setParameter("xpath", xpath);
//		query.setParameter("scripNumber", scripNumber);
//		query.setParameter("params", params1);
//		int i =query.executeUpdate();
//		//System.out.println("update:::::"+i);
//	}
//
//	public String checkXpathlocation(String params, String scripNumber) {
//		Session session = entityManager.unwrap(Session.class);
//		String params1="%"+params;
//		String sql="select xpath_location from  win_ta_script_metadata where  script_number=:scripNumber and input_parameter like :params";
//		SQLQuery query = session.createSQLQuery(sql);
//		query.setParameter("scripNumber", scripNumber);
//		query.setParameter("params", params1);
//		List results = query.list();
//		if(results.size()>0) {
//			//System.out.println("xpath::::::"+(String) results.get(0));
//			return (String) results.get(0);
//		}
//		else {
//			return null;
//		}
//	}
//	public String checkXpathlocation1(String params, String scriptID, String action) {
//		Session session = entityManager.unwrap(Session.class);
//		String params1="%"+params;
//		String sql="select xpath_location from  win_ta_script_metadata where  SCRIPT_ID=:scriptID and action=:action  and input_parameter like :params";
//		SQLQuery query = session.createSQLQuery(sql);
//		query.setParameter("scriptID", scriptID);
//		query.setParameter("params", params1);
//		query.setParameter("action", action);
//		List results = query.list();
//		if(results.size()>0) {
//			//System.out.println("xpath::::::"+(String) results.get(0));
//			return (String) results.get(0);
//		}
//		else {
//			return null;
//		}
//	}





}
//package com.winfo.dao;
//
//import java.util.List;
//
//import javax.persistence.EntityManager;
//
//import org.hibernate.SQLQuery;
//import org.hibernate.Session;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public class WatsXpathDao {
//
//	@Autowired
//	private EntityManager entityManager;
//
//	public int saveXpathParams(String params, String scripNumber, String xpath) {
//		Session session = entityManager.unwrap(Session.class);
//		String params1="%"+params+"%";
//  String sql= "UPDATE win_ta_script_metadata m SET m.xpath_location =:xpath WHERE m.script_number=:scripNumber and m.input_parameter like :params";
//		SQLQuery query = session.createSQLQuery(sql);
//		query.setParameter("xpath", xpath);
//		query.setParameter("scripNumber", scripNumber);
//		query.setParameter("params", params1);
//		int i =query.executeUpdate();
//		//System.out.println("update:::::"+i);
//		return i;
//
//	}
//
//	public void saveXpathParam1(String param1, String scripNumber, String xpath) {
//		Session session = entityManager.unwrap(Session.class);
//		String params1="%"+param1+"%";
//		String sql= "UPDATE win_ta_script_metadata m SET m.xpath_location =:xpath WHERE m.script_number=:scripNumber and m.input_parameter like :params";
//		SQLQuery query = session.createSQLQuery(sql);
//		query.setParameter("xpath", xpath);
//		query.setParameter("scripNumber", scripNumber);
//		query.setParameter("params", params1);
//		int i =query.executeUpdate();
//		//System.out.println("update:::::"+i);
//	}
//
//	public String checkXpathlocation(String params, String scripNumber) {
//		Session session = entityManager.unwrap(Session.class);
//
//		String sql="select xpath_location from  win_ta_script_metadata where input_parameter=:params and script_number=:scripNumber";
//		SQLQuery query = session.createSQLQuery(sql);
//		query.setParameter("scripNumber", scripNumber);
//		query.setParameter("params", params);
//		List results = query.list();
//		if(results.size()>0) {
//			//System.out.println("xpath::::::"+(String) results.get(0));
//			return (String) results.get(0);
//		}
//		else {
//			return null;
//		}
//	}
//
//
//}