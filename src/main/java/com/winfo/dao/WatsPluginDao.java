package com.winfo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.winfo.model.ScriptMaster;
import com.winfo.vo.DomGenericResponseBean;

@Repository

public class WatsPluginDao {

	@Autowired
	private EntityManager entityManager;

//	public void PluginData(EntityMaster master) {
//
//		Session session = entityManager.unwrap(Session.class);
//
//		session.save(master);
//
//	}

	public DomGenericResponseBean pluginData(ScriptMaster master, String scriptnumber) {
		DomGenericResponseBean response = new DomGenericResponseBean();
		Session session = entityManager.unwrap(Session.class);
	
//		Criteria criteria = session.createCriteria(ScriptMaster.class);
//		List<ScriptMaster> yourObject = (List<ScriptMaster>) criteria.add(Restrictions.eq("script_number", scriptnumber)).list();
		                             
		//System.out.println(yourObject.get(0).getScript_number());
//		if(yourObject.size()==0) {
//			session.save(master);
//			response.setStatus(200);
//			response.setStatusMessage("Success");
//		}
//		else {
//			response.setStatus(400);
//			response.setStatusMessage("scriptnumber already exists");
//		}

		session.save(master);
		response.setStatus(200);
		response.setStatusMessage("NewScriptNumber:"+scriptnumber);
         return response;
	}

	public String getScriptNumber(String processArea, String module) {
		//select script_number from win_ta_script_master where process_area='RTR' and module='General Ledger' ORDER BY script_number DESC ;

		Session session = entityManager.unwrap(Session.class);
		String sql = " select script_number from  WIN_TA_SCRIPT_MASTER_BKP4 m WHERE m.process_area=:processArea and m.module=:module ORDER BY m.script_number DESC";
		
		SQLQuery query = session.createSQLQuery(sql);
//		query.addEntity(ScriptMaster.class);
		query.setParameter("processArea", processArea);
		query.setParameter("module", module);
		
		List results = query.list();
		if(results.size()>0) {
			return (String) results.get(0);
		}
		else {
			return null;
		}
		
	}

	public void getdata(String testScriptNo) {
		Session session = entityManager.unwrap(Session.class);
		String sql = " SELECT test_set_desc, test_set_comments,enabled,description,effective_from,effective_to FROM win_ta_test_set WHERE test_set_id = :p5_test_run_name_copy;";
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter("p5_test_run_name_copy", testScriptNo);
		List results = query.list();
	}

}
