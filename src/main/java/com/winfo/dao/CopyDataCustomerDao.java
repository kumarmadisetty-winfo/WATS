package com.winfo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.winfo.model.ScriptMaster;

@Repository
@SuppressWarnings("unchecked")
public class CopyDataCustomerDao {

	@Autowired
	private EntityManager entityManager;
	
	public List<ScriptMaster> findDependentScriptByDependencyAndProductVersion(Integer scriptId, String productVersion) {
		
		Session session = entityManager.unwrap(Session.class);
		Query<?> query = session.createQuery("from ScriptMaster where dependency=:scriptDependency and productVersion =:productVersionName");
		query.setParameter("scriptDependency", scriptId);
		query.setParameter("productVersionName", productVersion);
		List<ScriptMaster> dependentScriptMasterList = (List<ScriptMaster>)query.getResultList();
		return dependentScriptMasterList;
	}

	public void updateScriptDtlsInMasterTable(ScriptMaster masterObj) {

		Session session1 = entityManager.unwrap(Session.class);
		session1.save(masterObj);
	}

	public List<ScriptMaster> getScriptMasterDtlByProductVersion(String productVersion) {

		Session session = entityManager.unwrap(Session.class);

		Query<?> query = session.createQuery("from ScriptMaster where productVersion =:productVersionName");
		query.setParameter("productVersionName", productVersion);
		List<ScriptMaster> scriptMasterList = (List<ScriptMaster>) query.getResultList();

		return scriptMasterList;
	}

}
