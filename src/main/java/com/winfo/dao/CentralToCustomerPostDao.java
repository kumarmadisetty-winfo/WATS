package com.winfo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.winfo.model.ScriptMaster;
import com.winfo.vo.DomGenericResponseBean;

@Repository

public class CentralToCustomerPostDao {

	@Autowired
	private EntityManager entityManager;

	@SuppressWarnings({"unchecked","rawtypes"})
	public DomGenericResponseBean centralRepoData(ScriptMaster master, String scriptnumber, String productversion) {
		Session session = entityManager.unwrap(Session.class);
		DomGenericResponseBean response = new DomGenericResponseBean();
		Query query = session
				.createQuery("select product_version from ScriptMaster where script_number='" + scriptnumber + "'");
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

}
