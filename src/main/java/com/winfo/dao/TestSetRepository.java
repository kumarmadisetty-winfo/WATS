package com.winfo.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Repository;

import com.winfo.model.TestSet;

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
	
	public String getTestSetPdfGenerationEnableStatus(Long testSetId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> query = cb.createQuery(String.class);
		Root<TestSet> root = query.from(TestSet.class);
		Predicate condition = cb.equal(root.get("testRunId"), testSetId);
		query.select(root.get("pdfGenerationEnabled")).where(condition);
		return em.createQuery(query).getSingleResult();

	}
	
	public void updatePdfGenerationEnableStatus(String testSetId, String enabled) {
		try {
			Query query = em.createQuery(
					"Update TestSet set pdfGenerationEnabled='" + enabled + "' where test_set_id='" + testSetId + "'");
			query.executeUpdate();
		} catch (Exception e) {
			System.out.println("Error Updation PDF Generation Status");
			System.out.println(e);
		}
	}

}
