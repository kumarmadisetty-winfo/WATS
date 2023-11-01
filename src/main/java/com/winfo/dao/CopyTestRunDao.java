package com.winfo.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.winfo.model.ExecuteStatus;
import com.winfo.model.ScriptMaster;
import com.winfo.model.ScriptMetaData;
import com.winfo.model.TestSet;
import com.winfo.model.TestSetLine;

@SuppressWarnings({ "deprecation", "unchecked" })
@Repository
public class CopyTestRunDao {

 public static final Logger logger = Logger.getLogger(CopyTestRunDao.class);;

	@PersistenceContext
	private EntityManager entityManager;

	public TestSet getdata(int testScriptNo) {
		return entityManager.find(TestSet.class, testScriptNo);
	}
	
	public TestSet saveTestrun(TestSet testSetObj) {
		entityManager.persist(testSetObj);
		logger.info("Test Run Id " + testSetObj.getTestRunId());
		return testSetObj;
	}

	public int getIds() {
		Session session = entityManager.unwrap(Session.class);
		String sql = "SELECT WATS_PROD.win_ta_test_set_id_seq.nextval FROM DUAL";
		SQLQuery<?> query = session.createSQLQuery(sql);

		List<?> results = query.list();
		if (!results.isEmpty()) {
			logger.info("TestSet Id " +results.get(0));
			BigDecimal bigDecimal = (BigDecimal) results.get(0);
			return Integer.parseInt(bigDecimal.toString());
		} else {
			return 0;
		}
	}

	public int getscrtiptIds() {
		Session session = entityManager.unwrap(Session.class);
		String sql = "SELECT win_ta_test_set_line_seq.NEXTVAL FROM DUAL";
		SQLQuery<?> query = session.createSQLQuery(sql);

		List<?> results = query.list();
		if (!results.isEmpty()) {
			logger.info("TestSet line id " + results.get(0));
			BigDecimal bigDecimal = (BigDecimal) results.get(0);
			Integer id = Integer.parseInt(bigDecimal.toString());
			logger.info("TestSet line id " + id);
			return id;
		} else {
			return 0;
		}
	}

	public int getscrtiptlineIds() {
		Session session = entityManager.unwrap(Session.class);
		String sql = "SELECT win_ta_param_id_seq.NEXTVAL FROM DUAL";
		SQLQuery<?> query = session.createSQLQuery(sql);

		List<?> results = query.list();
		if (!results.isEmpty()) {
			logger.info("Script Line Id " +results.get(0));
			BigDecimal bigDecimal = (BigDecimal) results.get(0);
			return Integer.parseInt(bigDecimal.toString());
		} else {
			return 0;
		}
	}

	public int updateTestSetRecord(TestSet testSetObj) {
		entityManager.merge(testSetObj);
		logger.info("Test Run Id " + testSetObj.getTestRunId());
		return testSetObj.getTestRunId();

	}

	public void updatelinesRecord(TestSetLine testSetLines) {
		entityManager.persist(testSetLines);
	}

	public String getProductVersion(Integer projectId) {
		Session session = entityManager.unwrap(Session.class);
		String sql = "Select product_version from win_ta_projects where project_id=" + projectId;
		SQLQuery<?> query = session.createSQLQuery(sql);
		List<?> results = query.list();
		if (!results.isEmpty()) {
			logger.info("Product version " +results.get(0));
			return (String) results.get(0);
		}
		return null;
	}

	public Object[] getScriptMasterInfoByProjectId(String scriptNumber, Integer projectId) {
		Session session = entityManager.unwrap(Session.class);
		String productVersion = getProductVersion(projectId);
		String sql = "Select * from win_ta_script_master where script_number='" + scriptNumber
				+ "' and product_version='" + productVersion + "'";
		Query query = session.createSQLQuery(sql);
		List<Object[]> rows = query.getResultList();
		if (!rows.isEmpty()) {
			return rows.get(0);
		}
		return new Object[0];
	}

	public ScriptMaster getScriptMasterInfo(String scriptNumber, String productVersion) {
		String sql = "from ScriptMaster where script_number='" + scriptNumber + "' and product_version='"
				+ productVersion + "'";
		Query query = entityManager.createQuery(sql);
		List<ScriptMaster> rows = query.getResultList();
		if (!rows.isEmpty()) {
			return rows.get(0);
		}
		return null;

	}

	public List<Object[]> getScriptMetadataInfoByScriptId(int scriptId) {
		Session session = entityManager.unwrap(Session.class);
		String sql = "Select * from win_ta_script_metadata where script_id =" + scriptId;
		Query query = session.createSQLQuery(sql);
		return query.getResultList();

	}

	public List<ScriptMetaData> getScriptMetadataInfo(int scriptId) {
		String sql = "from ScriptMetaData where scriptMaster.scriptId = :scriptId order by lineNumber";
		Query query = entityManager.createQuery(sql);
		query.setParameter("scriptId", scriptId);
		return query.getResultList();

	}

	public Integer findMaxSeqNumOfTestRun(Integer testSetId) {
		Session session = entityManager.unwrap(Session.class);
		String sql = "select max(seq_num) from WIN_TA_TEST_SET_LINES where test_set_id = " + testSetId;
		Query query = session.createSQLQuery(sql);
		List<BigDecimal> maxSeqnumList = query.getResultList();

		if(maxSeqnumList == null || maxSeqnumList.isEmpty() || maxSeqnumList.get(0) == null) {
			return 0;
		}
		return maxSeqnumList.get(0).intValue();
	}

	public TestSetLine getLineDtlByTestSetId(Integer testSetLineId) {
		logger.info("TestSet Line ID  " + testSetLineId);
		return entityManager.find(TestSetLine.class, testSetLineId);
	}
	
	public String findProductVersionByTestSetId(Integer testSetId) {
		Session session = entityManager.unwrap(Session.class);
		String sql = "select productVersion from Project where projectId = (select projectId from TestSet where testRunId = :testSetId )";
		Query query = session.createQuery(sql);
		query.setParameter("testSetId", testSetId);
		List<String> listOfProductVersion = query.getResultList();
		return listOfProductVersion == null ? "" : listOfProductVersion.get(0);
	}
	
	public Integer getScriptIdFromMaster(String scriptNumber, String productVersion) {
		Session session = entityManager.unwrap(Session.class);
		String sql = "select scriptId from ScriptMaster where scriptNumber = :scriptNumber AND productVersion = :productVersion";
		Query query = session.createQuery(sql);
		query.setParameter("scriptNumber", scriptNumber);
		query.setParameter("productVersion", productVersion);
		List<Integer> listOfProductVersion = query.getResultList();
		return CollectionUtils.isEmpty(listOfProductVersion) ? null : listOfProductVersion.get(0);
	}
	
	public void updateExecuteStatusDtls(ExecuteStatus executeStatus) {
		entityManager.persist(executeStatus);
		logger.info("Execute Status Test Run Name " + executeStatus.getTestRunName());
	}

	public String getMeaningUsingValidationName(String validationName) {
		Session session = entityManager.unwrap(Session.class);
		String sql = "select meaning from LookUpCode where LOOKUP_CODE=:validationName";
		Query query = session.createQuery(sql);
		query.setParameter("validationName", validationName);
		return (String) query.getSingleResult();
	}


}