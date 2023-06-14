package com.winfo.dao;

import java.math.BigDecimal;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.winfo.model.ScriptMaster;
import com.winfo.model.ScriptMetaData;
import com.winfo.model.TestSet;
import com.winfo.model.TestSetLine;
import com.winfo.model.TestSetScriptParam;

@Repository
public class TestRunMigrationGetDao {

	@Autowired
	private EntityManager entityManager;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int insertScriptMetaData(ScriptMetaData scriptMetaData) {
		Session session = entityManager.unwrap(Session.class);
		session.save(scriptMetaData);
		return scriptMetaData.getScriptMetaDataId();
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int insertScriptMaster(ScriptMaster master) {
		Session session = entityManager.unwrap(Session.class);
		session.persist(master);
		return master.getScriptId();
	}

	@Transactional
	public int insertTestRun(TestSet testrundata) {
		Session session = entityManager.unwrap(Session.class);
		session.persist(testrundata);
		return testrundata.getTestRunId();
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int insertScriptLine(TestSetLine scriptsData) {
		Session session = entityManager.unwrap(Session.class);
		session.merge(scriptsData);
		session.close();
		return scriptsData.getTestRunScriptId();
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int insertScriptParam(TestSetScriptParam scritplinesData) {
		Session session = entityManager.unwrap(Session.class);
		session.merge(scritplinesData);
		session.close();
		return scritplinesData.getTestRunScriptParamId();
	}

	public int checkScriptPresent(String productVersion, String scriptNumber) {
		Session session = entityManager.unwrap(Session.class);

		BigDecimal checkifPresent = (BigDecimal) session
				.createNativeQuery("select count(*) from WIN_TA_SCRIPT_MASTER where script_number='" + scriptNumber
						+ "' and product_version='" + productVersion + "'")
				.getSingleResult();
		int data = Integer.parseInt(checkifPresent.toString());
		if (data != 0) {
			BigDecimal data1 = (BigDecimal) session
					.createNativeQuery("select script_id from WIN_TA_SCRIPT_MASTER where script_number='" + scriptNumber
							+ "' and product_version='" + productVersion + "'")
					.getSingleResult();
			data = Integer.parseInt(data1.toString());
		}
		return data;
	}
	public int getOldScriptCustomerId(String productVersion, String scriptNumber) {
		Session session = entityManager.unwrap(Session.class);

		BigDecimal oldCustomerId = (BigDecimal) session
				.createNativeQuery("select customer_id from WIN_TA_SCRIPT_MASTER where script_number='" + scriptNumber
						+ "' and product_version='" + productVersion + "'")
				.getSingleResult();
		return Integer.parseInt(oldCustomerId.toString());
	}
	public String getMaxScriptNumber(String newCustomScriptNumber,String productVersion) {
		Session session = entityManager.unwrap(Session.class);
		
		String maxSciptNumber = (String) session
				.createNativeQuery("select max(script_number) from WIN_TA_SCRIPT_MASTER where script_number like '" + newCustomScriptNumber
						+ "%' and product_version='" + productVersion + "'")
				.getSingleResult();
		return maxSciptNumber;
	}

}
