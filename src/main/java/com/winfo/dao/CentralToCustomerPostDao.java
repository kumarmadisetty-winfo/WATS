package com.winfo.dao;

import java.math.BigDecimal;
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
				.createQuery("select productVersion from ScriptMaster where scriptNumber='" + scriptnumber + "' and productVersion='" + productversion + "'");
		List<String> result2 = query.list();
		if (result2.isEmpty()) {
			session.merge(master);
			response.setStatus(200);
			response.setStatusMessage("SUCCESS");
			response.setDescription("Script Copied Successfully");

		} 
		else
		{
			response.setStatus(400);
			response.setStatusMessage("ERROR");
			response.setDescription("Script Number Already exists");
			response.setFailed_Script(scriptnumber);
		}

		return response;
	}

	public void insertLookUpObj(LookUp lookUpObj) {
		entityManager.persist(lookUpObj);
	}

	public void insertLookUpCodeObj(LookUpCode lookUpCodeObj) {
		entityManager.persist(lookUpCodeObj);
	}

	public boolean doesLookUpExist(String lookUpName) {
		Session session = entityManager.unwrap(Session.class);

		String qry = "SELECT count(1) FROM DUAL WHERE EXISTS (select 1 from  wats_prod.WIN_TA_LOOKUP_CODES where LOOKUP_CODE='"
				+ lookUpName + "')";
		Query query = session.createNativeQuery(qry);

		BigDecimal count = (BigDecimal) query.getSingleResult();
		return count.intValue() == 0;

	}

	public boolean doesLookUpCodeExist(String lookUpName, String lookUpCode) {

		Session session = entityManager.unwrap(Session.class);
		String qry = "SELECT count(1) FROM DUAL WHERE EXISTS (SELECT 1 FROM WIN_TA_LOOKUP_CODES WHERE LOOKUP_NAME='"
				+ lookUpName + "' AND LOOKUP_CODE='" + lookUpCode + "')";
		Query query = session.createNativeQuery(qry);
		BigDecimal count = (BigDecimal) query.getSingleResult();
		return count.intValue() == 0;
	}

}
