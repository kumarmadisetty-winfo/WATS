package com.winfo.dao;

import java.math.BigDecimal;
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

	public int saveTestrun(Testrundata setTestrundata) {
		// TODO Auto-generated method stub
		
		entityManager.persist(setTestrundata);
		System.out.println("setTestrundata.getTestsetid() 1:"+setTestrundata.getTestsetid());
		return setTestrundata.getTestsetid();
	}

	public int getIds() {
		Session session = entityManager.unwrap(Session.class);
		String sql="SELECT win_ta_test_set_id_seq.nextval FROM DUAL";
		SQLQuery query = session.createSQLQuery(sql);
		
		List results = query.list();
		if(results.size()>0) {
			System.out.println(results.get(0));
		
		BigDecimal bigDecimal= (BigDecimal)results.get(0);
		Integer id=Integer.parseInt(bigDecimal.toString());
		return id;
		}else {
		return 0;
		}
	}

	public int getscrtiptIds() {
		Session session = entityManager.unwrap(Session.class);
		String sql="SELECT win_ta_test_set_line_seq.NEXTVAL FROM DUAL";
		SQLQuery query = session.createSQLQuery(sql);
		
		List results = query.list();
		if(results.size()>0) {
			System.out.println(results.get(0));
		
		BigDecimal bigDecimal= (BigDecimal)results.get(0);
		Integer id=Integer.parseInt(bigDecimal.toString());
		System.out.println("id"+id);
		return id;
		}else {
		return 0;
		}	
	}

	public int getscrtiptlineIds() {
		Session session = entityManager.unwrap(Session.class);
		String sql="SELECT win_ta_param_id_seq.NEXTVAL FROM DUAL";
		SQLQuery query = session.createSQLQuery(sql);
		
		List results = query.list();
		if(results.size()>0) {
			System.out.println(results.get(0));
		
		BigDecimal bigDecimal= (BigDecimal)results.get(0);
		Integer id=Integer.parseInt(bigDecimal.toString());
		return id;
		}else {
		return 0;
		}	
	}
	public int update(Testrundata getTestrun) {
		entityManager.merge(getTestrun);
		System.out.println("getTestrun.getTestsetid() 2:"+getTestrun.getTestsetid());
		return getTestrun.getTestsetid();
	}
}