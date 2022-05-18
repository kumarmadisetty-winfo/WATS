package com.winfo.dao;

import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Repository;

@Repository
@RefreshScope
public class TestRunExecuteStatusRepository {
	@PersistenceContext
	EntityManager em;
	
	public void updateExecStatusTable(String testSetId) {

		try {
			Session session = em.unwrap(Session.class);
			String execQry = "SELECT RESPONSE_COUNT FROM TEST_RUN_EXECUTE_STATUS WHERE TEST_RUN_ID =" + testSetId
					+ " AND EXECUTION_ID = (SELECT MAX(EXECUTION_ID) FROM TEST_RUN_EXECUTE_STATUS WHERE TEST_RUN_ID ="
					+ testSetId+" )";
			BigDecimal bigDecimal = (BigDecimal) session.createSQLQuery(execQry).getSingleResult();
			Integer id = Integer.parseInt(bigDecimal.toString());
			String updateQry = "UPDATE TEST_RUN_EXECUTE_STATUS SET RESPONSE_COUNT =" + (id + 1)
					+ " WHERE TEST_RUN_ID = " + testSetId
					+ " AND EXECUTION_ID = (SELECT MAX(EXECUTION_ID) FROM TEST_RUN_EXECUTE_STATUS WHERE TEST_RUN_ID = "+testSetId+" )";
			session.createSQLQuery(updateQry).executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
