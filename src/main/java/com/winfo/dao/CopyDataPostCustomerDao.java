package com.winfo.dao;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.winfo.model.ScriptMaster;

@Repository
public class CopyDataPostCustomerDao {
	@Autowired
	private EntityManager entityManager;

	public void copyDataPost(ScriptMaster master) {

		Session session1 = entityManager.unwrap(Session.class);
		session1.save(master);

	}
}
