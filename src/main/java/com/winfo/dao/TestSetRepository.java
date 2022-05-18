package com.winfo.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Session;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Repository;

@Repository
@RefreshScope
public class TestSetRepository {
	
	@PersistenceContext
	EntityManager em;
	
	public void updateTestSetPaths(String passPath, String failPath, String executionPath, String testSetId) {

		try {
			Session session = em.unwrap(Session.class);
			String sqlQuery = "Update WIN_TA_TEST_SET SET PASS_PATH ='" + passPath + "', FAIL_PATH ='" + failPath
					+ "', EXCEPTION_PATH ='" + executionPath + "'WHERE TEST_SET_ID=" + testSetId;
			Query query = session.createSQLQuery(sqlQuery);
			query.executeUpdate();
		} catch (Exception e) {
			System.out.println("cannot update TestSetPath");
			System.out.println(e);
		}
	}

}
