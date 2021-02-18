package com.winfo.dao;

import javax.persistence.EntityManager;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class WatsXpathDao {

	@Autowired
	private EntityManager entityManager;

	public int saveXpathParams(String params, String scripNumber, String xpath) {
		Session session = entityManager.unwrap(Session.class);

	//	String sql = " select script_number from  WIN_TA_SCRIPT_MASTER_BKP4 m WHERE m.process_area=:processArea and m.module=:module ORDER BY m.script_number DESC";
  String sql= "UPDATE win_ta_script_metadata m SET m.xpath_location =:xpath WHERE m.script_number=:scripNumber and m.input_parameter=:params";
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter("xpath", xpath);
		query.setParameter("scripNumber", scripNumber);
		query.setParameter("params", params);
		int i =query.executeUpdate();
		return i;

	}

	public void saveXpathParam1(String param1, String scripNumber, String xpath) {
		Session session = entityManager.unwrap(Session.class);

	//	String sql = " select script_number from  WIN_TA_SCRIPT_MASTER_BKP4 m WHERE m.process_area=:processArea and m.module=:module ORDER BY m.script_number DESC";
  String sql= "UPDATE win_ta_script_metadata m SET m.xpath_location =:xpath WHERE m.script_number=:scripNumber and m.input_parameter=:params";
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter("xpath", xpath);
		query.setParameter("scripNumber", scripNumber);
		query.setParameter("params", param1);
		int i =query.executeUpdate();
		System.out.println("update:::::"+i);
	}
}
