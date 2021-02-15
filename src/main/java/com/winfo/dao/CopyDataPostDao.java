package com.winfo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.winfo.model.ScriptMaster;
import com.winfo.vo.DomGenericResponseBean;

@Repository
public class CopyDataPostDao {
	@Autowired
	private EntityManager entityManager;
	public void copyDataPost(ScriptMaster master) {
	
		Session session1 = entityManager.unwrap(Session.class);
	
	
	
		    session1.save(master);
		    session1.close();
		    
			
			//return response;
	}
}
