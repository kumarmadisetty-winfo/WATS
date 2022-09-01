package com.winfo.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.winfo.model.ScriptMaster;
import com.winfo.model.ScriptMetaData;
import com.winfo.model.TestSet;
import com.winfo.model.TestSetLine;

@Repository
public class CopyTestRunDao {
	Logger log = Logger.getLogger("Logger");

	@PersistenceContext
	private EntityManager entityManager;

	public TestSet getdata(int testScriptNo) {
		TestSet getTestrun = entityManager.find(TestSet.class, testScriptNo);
		return getTestrun;
	}

	public int saveTestrun(TestSet testSetObj) {
		entityManager.persist(testSetObj);
		System.out.println("setTestrundata.getTestsetid() 1:" + testSetObj.getTestRunId());
		return testSetObj.getTestRunId();
	}

	public int getIds() {
		Session session = entityManager.unwrap(Session.class);
		String sql = "SELECT WATS_PROD.win_ta_test_set_id_seq.nextval FROM DUAL";
		SQLQuery query = session.createSQLQuery(sql);

		List results = query.list();
		if (results.size() > 0) {
			System.out.println(results.get(0));

			BigDecimal bigDecimal = (BigDecimal) results.get(0);
			Integer id = Integer.parseInt(bigDecimal.toString());
			return id;
		} else {
			return 0;
		}
	}

	public int getscrtiptIds() {
		Session session = entityManager.unwrap(Session.class);
		String sql = "SELECT win_ta_test_set_line_seq.NEXTVAL FROM DUAL";
		SQLQuery query = session.createSQLQuery(sql);

		List results = query.list();
		if (results.size() > 0) {
			System.out.println(results.get(0));

			BigDecimal bigDecimal = (BigDecimal) results.get(0);
			Integer id = Integer.parseInt(bigDecimal.toString());
			System.out.println("id" + id);
			return id;
		} else {
			return 0;
		}
	}

	public int getscrtiptlineIds() {
		Session session = entityManager.unwrap(Session.class);
		String sql = "SELECT win_ta_param_id_seq.NEXTVAL FROM DUAL";
		SQLQuery query = session.createSQLQuery(sql);

		List results = query.list();
		if (results.size() > 0) {
			System.out.println(results.get(0));

			BigDecimal bigDecimal = (BigDecimal) results.get(0);
			Integer id = Integer.parseInt(bigDecimal.toString());
			return id;
		} else {
			return 0;
		}
	}

	public int updateTestSetRecord(TestSet testSetObj) {
		entityManager.merge(testSetObj);
		log.info("getTestrun.getTestsetid() 2:" + testSetObj.getTestRunId());
		return testSetObj.getTestRunId();

	}
	
	public void updatelinesRecord(TestSetLine testSetLines) {
		entityManager.persist(testSetLines);
	}

	public String getProductVersion(Integer project_id) {
		Session session = entityManager.unwrap(Session.class);
		String sql = "Select product_version from win_ta_projects where project_id=" + project_id;
		SQLQuery query = session.createSQLQuery(sql);
		List results = query.list();
		if (results.size() > 0) {
			System.out.println(results.get(0));

			String product_version = (String) results.get(0);
			return product_version;

		}
		return null;
	}

	public Object[] getScriptMasterInfoByProjectId(String scriptNumber, Integer projectId) {
		Session session = entityManager.unwrap(Session.class);
		String product_version = getProductVersion(projectId);
		String sql = "Select * from win_ta_script_master where script_number='" + scriptNumber
				+ "' and product_version='" + product_version + "'";
		Query query = session.createSQLQuery(sql);
		List<Object[]> rows = query.getResultList();
		// ScriptMaster master=new ScriptMaster();
		if (rows != null) {
			if (rows.size() > 0) {
				return rows.get(0);
			}
		}
		return null;

	}

	public ScriptMaster getScriptMasterInfo(String scriptNumber, String productVersion) {
		Session session = entityManager.unwrap(Session.class);
		String sql = "from ScriptMaster where script_number='" + scriptNumber + "' and product_version='"
				+ productVersion + "'";
		Query query = entityManager.createQuery(sql);
		List<ScriptMaster> rows = query.getResultList();
		if (rows != null) {
			if (rows.size() > 0) {
				return rows.get(0);
			}
		}
		return null;

	}

	public List<Object[]> getScriptMetadataInfoByScriptId(int script_id) {
		Session session = entityManager.unwrap(Session.class);
		String sql = "Select * from win_ta_script_metadata where script_id =" + script_id;
		Query query = session.createSQLQuery(sql);
		List<Object[]> metadata = query.getResultList();
		if (metadata != null) {
			return metadata;
		}
		return null;

	}

	public List<ScriptMetaData> getScriptMetadataInfo(int script_id) {
		// Session session = entityManager.unwrap(Session.class);
		String sql = "from ScriptMetaData S where script_id =" + script_id + " order by S.line_number";
		Query query = entityManager.createQuery(sql);
		List<ScriptMetaData> metadata = query.getResultList();
		if (metadata != null) {
			return metadata;
		}
		return null;

	}
	
	public Integer findMaxSeqNumOfTestRun(Integer testSetId) {
		Session session = entityManager.unwrap(Session.class);
		String sql = "select max(seq_num) from WIN_TA_TEST_SET_LINES where test_set_id = "+testSetId;
		Query query = session.createSQLQuery(sql);
		List<BigDecimal> maxSeqnumList = query.getResultList();
		if(maxSeqnumList.isEmpty()) {
			return 1;
		}
		return maxSeqnumList.get(0).intValue() + 1;
	}
	
	
	public TestSetLine getLineDtlByTestSetId(Integer testSetLineId) {
		log.info("TestSetLineID *** "+testSetLineId);
		TestSetLine testSetLineObj = entityManager.find(TestSetLine.class, testSetLineId);
		return testSetLineObj;
	}
	

}