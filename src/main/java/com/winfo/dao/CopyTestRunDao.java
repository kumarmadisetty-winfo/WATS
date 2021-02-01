package com.winfo.dao;

import javax.persistence.EntityManager;

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
}
