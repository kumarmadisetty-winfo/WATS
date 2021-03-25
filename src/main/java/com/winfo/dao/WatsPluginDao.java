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
		String sql = " select script_number from  WIN_TA_SCRIPT_MASTER m WHERE m.process_area=:processArea and m.module=:module ORDER BY m.script_number DESC";
		
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
	
	public String getUserIdValidation(String username) {
		String userId = username.toUpperCase();
		Session session = entityManager.unwrap(Session.class);
		String sql="SELECT USER_ID FROM WIN_TA_USER_ROLE where upper(USER_ID) =:userId";
		
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter("userId", userId);
		
		List results = query.list();
		if(results.size()>0) {
			System.out.println((String) results.get(0));
			return (String) results.get(0);
		}
		else {
			return null;
		}
	}
	public String verifyEndDate(String username) {
		String userId = username.toUpperCase();
		Session session = entityManager.unwrap(Session.class);
	String sql=	"SELECT USER_ID FROM WIN_TA_USER_ROLE where upper(USER_ID) =:userId and NVL(END_DATE, SYSDATE) >= SYSDATE";
	//	String sql="SELECT USER_ID FROM WIN_TA_USER_ROLE where upper(USER_ID) =:userId";
		
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter("userId", userId);
		
		List results = query.list();
		if(results.size()>0) {
			System.out.println((String) results.get(0));
			return (String) results.get(0);
		}
		else {
			return null;
		}
	}
	public String verifyPasswordExpire(String username) {
		String userId = username.toUpperCase();
		Session session = entityManager.unwrap(Session.class);
		String sql="SELECT USER_ID FROM WIN_TA_USER_ROLE where upper(USER_ID) =:userId and NVL(PASSWORD_EXPIRY, SYSDATE) >= SYSDATE";
	//	String sql=	"SELECT USER_ID FROM WIN_TA_USER_ROLE where upper(USER_ID) =:userId and NVL(END_DATE, SYSDATE) >= SYSDATE";
			
			SQLQuery query = session.createSQLQuery(sql);
			query.setParameter("userId", userId);
			
			List results = query.list();
			if(results.size()>0) {
				System.out.println((String) results.get(0));
				return (String) results.get(0);
			}
			else {
				return null;
			}
	}
	public String verifyUserActive(String username) {
		String userId = username.toUpperCase();
		Session session = entityManager.unwrap(Session.class);
		String sql="SELECT USER_ID FROM WIN_TA_USER_ROLE where upper(USER_ID) =:userId and upper(STATUS) = 'ACTIVE'";
		String sql1="SELECT USER_ID FROM WIN_TA_USER_ROLE where upper(USER_ID) =:userId and NVL(PASSWORD_EXPIRY, SYSDATE) >= SYSDATE";
			
			SQLQuery query = session.createSQLQuery(sql);
			query.setParameter("userId", userId);
			
			List results = query.list();
			if(results.size()>0) {
				System.out.println((String) results.get(0));
				return (String) results.get(0);
			}
			else {
				return null;
			}
	}
	public String getEncriptPassword(String username) {
		String userId = username.toUpperCase();
		Session session = entityManager.unwrap(Session.class);
		String sql="SELECT TOOLKIT.DECRYPT(PASSWORD) FROM WIN_TA_USER_ROLE WHERE UPPER(USER_ID) =:userId ";
		//String sql="SELECT USER_ID FROM WIN_TA_USER_ROLE where upper(USER_ID) =:userId and NVL(PASSWORD_EXPIRY, SYSDATE) >= SYSDATE";
			
			SQLQuery query = session.createSQLQuery(sql);
			query.setParameter("userId", userId);
			
			List results = query.list();
			if(results.size()>0) {
				return (String) results.get(0);
			}
			else {
				return null;
			}
	}

}