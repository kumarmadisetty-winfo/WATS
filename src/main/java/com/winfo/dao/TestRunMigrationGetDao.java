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
import com.winfo.model.ScriptsData;
import com.winfo.model.ScritplinesData;
import com.winfo.model.Testrundata;

@Repository
public class TestRunMigrationGetDao {

	@Autowired
	private EntityManager entityManager;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int insertScriptMetaData(ScriptMetaData scriptMetaData) {
		Session session = entityManager.unwrap(Session.class);
		session.save(scriptMetaData);
		return scriptMetaData.getScript_meta_data_id();
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int insertScriptMaster(ScriptMaster master) {
		Session session = entityManager.unwrap(Session.class);
		session.save(master);
		return master.getScript_id();
	}

	@Transactional
	public int insertTestRun(Testrundata testrundata) {
		Session session = entityManager.unwrap(Session.class);
		session.merge(testrundata);
		return testrundata.getTestsetid();
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int insertScriptLine(ScriptsData scriptsData) {
		Session session = entityManager.unwrap(Session.class);
		session.merge(scriptsData);
		session.close();
		return scriptsData.getTestsetlineid();
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int insertScriptParam(ScritplinesData scritplinesData) {
		Session session = entityManager.unwrap(Session.class);
		session.merge(scritplinesData);
		session.close();
		return scritplinesData.getTestscriptperamid();
	}

//	public void addTestRunData(Testrundata testrundata) {
//		insertTestRun(testrundata);
//	}

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

}
