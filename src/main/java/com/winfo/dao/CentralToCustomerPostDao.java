package com.winfo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.winfo.model.LookUp;
import com.winfo.model.LookUpCode;
import com.winfo.model.ScriptMaster;
import com.winfo.vo.DomGenericResponseBean;

@Repository

@SuppressWarnings({ "unchecked", "rawtypes" })
public class CentralToCustomerPostDao {

	@Autowired
	private EntityManager entityManager;

	public DomGenericResponseBean centralRepoData(ScriptMaster master, String scriptnumber, String productversion) {
		Session session = entityManager.unwrap(Session.class);
		DomGenericResponseBean response = new DomGenericResponseBean();
		Query query = session
				.createQuery("select productVersion from ScriptMaster where scriptNumber='" + scriptnumber + "'");
		List<String> result2 = query.list();

		int i = 0;
		if (result2.isEmpty()) {
			session.save(master);
			response.setStatus(200);
			response.setStatusMessage("SUCCESS");
			response.setDescription("Script Copied Successfully");

		} else {
			for (i = 0; i < result2.size(); i++) {
				if (result2.get(i).equalsIgnoreCase(productversion)) {
					response.setStatus(400);
					response.setStatusMessage("ERROR");
					response.setDescription("Script Number Already exists");
					response.setFailed_Script(scriptnumber);
					break;
				} else {
					session.save(master);
					response.setStatus(200);
					response.setStatusMessage("SUCCESS");
					response.setDescription("Script Copied Successfully");
				}
			}
		}

		return response;
	}
	
	public void insertLookUpObj(LookUp lookUpObj) {
		entityManager.persist(lookUpObj);
	}
	
	public void insertLookUpCodeObj(LookUpCode lookUpCodeObj) {
		entityManager.persist(lookUpCodeObj);	
	}
	
	public int checkLookUpCountByLookUpName(String lookUpName) {
		Session session = entityManager.unwrap(Session.class);
		Query query = session
				.createNativeQuery("Select count(*) FROM WIN_TA_LOOKUPS WHERE LOOKUP_NAME='" + lookUpName + "'");

		return (int) query.getSingleResult();

	}

	public int checkLookUpCodeCountByLookUpCode(String lookUpName, String lookUpCode) {

		Session session = entityManager.unwrap(Session.class);
		Query query = session.createNativeQuery("Select count(*) FROM WIN_TA_LOOKUP_CODES WHERE LOOKUP_NAME='"
				+ lookUpName + "' AND LOOKUP_CODE='" + lookUpCode + "'");
		
		return (int) query.getSingleResult();
	}

}
