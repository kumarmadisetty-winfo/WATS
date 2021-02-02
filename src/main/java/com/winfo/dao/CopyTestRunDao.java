package com.winfo.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.winfo.model.Testrundata;
@Repository
public class CopyTestRunDao {
	@Autowired
	private EntityManager entityManager;
	public Testrundata getdata(int testScriptNo) {
		Testrundata getTestrun=entityManager.find(Testrundata.class, testScriptNo);
		return getTestrun;
	}

	public void saveTestrun(Testrundata setTestrundata) {
		// TODO Auto-generated method stub
		entityManager.persist(setTestrundata);
//		Session session = entityManager.unwrap(Session.class);
//		session.save(setTestrundata);
	}

	public List<Integer> getIds() {
		List<Integer> addIds=new ArrayList<Integer>();
		Session session = entityManager.unwrap(Session.class);
		String sql1 = " select script_number from  WIN_TA_SCRIPT_MASTER_BKP4 m WHERE m.process_area=:processArea and m.module=:module ORDER BY m.script_number DESC";
		
		SQLQuery query = session.createSQLQuery(sql1);
		
		List results = query.list();
		if(results.size()>0) {
			
		return null;
	}
}
