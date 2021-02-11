package com.winfo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.winfo.vo.DomGenericResponseBean;
import com.winfo.model.ScriptMaster;

@Repository

public class CustomerToCentralPostDao {

	@Autowired
	private EntityManager entityManager;

	public DomGenericResponseBean centralRepoData(ScriptMaster master, String scriptnumber) {
		DomGenericResponseBean response = new DomGenericResponseBean();
		Session session = entityManager.unwrap(Session.class);
	
		Criteria criteria = session.createCriteria(ScriptMaster.class);
		List<ScriptMaster> yourObject = (List<ScriptMaster>) criteria.add(Restrictions.eq("script_number", scriptnumber)).list();
		                             
		
		if(yourObject.size()==0) {
            session.save(master);
            response.setStatus(200);
            response.setStatusMessage("SUCCESS");
            response.setDescription("Script Copied Successfully");
        }
      
        else {
            response.setStatus(400);
            response.setStatusMessage("ERROR");
            response.setDescription("Script Number Already exists");
            response.setFailed_Script(scriptnumber);
          
        }
         return response;
	}

}
