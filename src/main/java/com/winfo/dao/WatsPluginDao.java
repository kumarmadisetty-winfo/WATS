package com.winfo.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.winfo.model.PluginMaster;
import com.winfo.model.ScriptMaster;
import com.winfo.model.Testrundata;
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

	public DomGenericResponseBean pluginData(PluginMaster master, String scriptnumber) {
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

	@SuppressWarnings("deprecation")
	public List<String> getScriptNumber(String processArea, String module) {
		//select script_number from win_ta_script_master where process_area='RTR' and module='General Ledger' ORDER BY script_number DESC ;

		Session session = entityManager.unwrap(Session.class);
		String sql = " select script_number from  WIN_TA_SCRIPT_MASTER m WHERE m.process_area=:processArea and m.module=:module ORDER BY m.script_number DESC";
		
		SQLQuery query = session.createSQLQuery(sql);
//		query.addEntity(ScriptMaster.class);
		query.setParameter("processArea", processArea);
		query.setParameter("module", module);
		
		List<String> results = query.list();
		if(results.size()>0) {
//			return (String) results.get(0);
			return results;
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
	public List<String> getTestrunData() {
		Session session = entityManager.unwrap(Session.class);
		String sql="select TEST_SET_NAME from win_ta_test_set";
			SQLQuery query = session.createSQLQuery(sql);
			
			List results = query.list();
			if(results.size()>0) {
				
				return (List<String>) results;
			}
			else {
				return null;
			}
	}
	public int getTest_set_id() {
		Session session = entityManager.unwrap(Session.class);
		String sql="SELECT WATS_PROD.win_ta_test_set_id_seq.nextval FROM DUAL";
		SQLQuery query = session.createSQLQuery(sql);
		
		List results = query.list();
		if(results.size()>0) {
			System.out.println(results.get(0));
		
		BigDecimal bigDecimal= (BigDecimal)results.get(0);
		Integer id=Integer.parseInt(bigDecimal.toString());
		return id;
		}else {
		return 0;
		}
	}

	public int getTest_set_line_id() {
		Session session = entityManager.unwrap(Session.class);
		String sql="SELECT win_ta_test_set_line_seq.NEXTVAL FROM DUAL";
		SQLQuery query = session.createSQLQuery(sql);
		
		List results = query.list();
		if(results.size()>0) {
			System.out.println(results.get(0));
		
		BigDecimal bigDecimal= (BigDecimal)results.get(0);
		Integer id=Integer.parseInt(bigDecimal.toString());
		System.out.println("id"+id);
		return id;
		}else {
		return 0;
		}	
	}

	public int getParam_id() {
		Session session = entityManager.unwrap(Session.class);
		String sql="SELECT win_ta_param_id_seq.NEXTVAL FROM DUAL";
		SQLQuery query = session.createSQLQuery(sql);
		
		List results = query.list();
		if(results.size()>0) {
			System.out.println(results.get(0));
		
		BigDecimal bigDecimal= (BigDecimal)results.get(0);
		Integer id=Integer.parseInt(bigDecimal.toString());
		return id;
		}else {
		return 0;
		}	
	}
	public int getMasterScriptId() {
		Session session = entityManager.unwrap(Session.class);
		String sql="SELECT WIN_TA_SCRIPT_MASTER_SEQ.NEXTVAL FROM DUAL";
		SQLQuery query = session.createSQLQuery(sql);
		
		List results = query.list();
		if(results.size()>0) {
			System.out.println(results.get(0));
		
		BigDecimal bigDecimal= (BigDecimal)results.get(0);
		Integer id=Integer.parseInt(bigDecimal.toString());
		System.out.println("id"+id);
		return id;
		}else {
		return 0;
		}	
	}

	public int getMetaDataId() {
		Session session = entityManager.unwrap(Session.class);
		String sql="SELECT WIN_TA_SCRIPT_METADATA_SEQ.NEXTVAL FROM DUAL";
		SQLQuery query = session.createSQLQuery(sql);
		
		List results = query.list();
		if(results.size()>0) {
			System.out.println(results.get(0));
		
		BigDecimal bigDecimal= (BigDecimal)results.get(0);
		Integer id=Integer.parseInt(bigDecimal.toString());
		return id;
		}else {
		return 0;
		}	
	}
	public Testrundata getTestrunData(int testSetId) {
		Testrundata getTestrun=entityManager.find(Testrundata.class, testSetId);
		return getTestrun;
	}
	public int getTestsetIde(String testsetName) {
		Session session = entityManager.unwrap(Session.class);
		String sql="select TEST_SET_ID from win_ta_test_set where test_set_name=:testsetName";
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter("testsetName", testsetName);

		List results = query.list();
		if(results.size()>0) {
			System.out.println(results.get(0));
		
		BigDecimal bigDecimal= (BigDecimal)results.get(0);
		Integer id=Integer.parseInt(bigDecimal.toString());
		return id;
		}else {
		return 0;
		}	
	}

	public int getseqNum(int testSetId) {
		Session session = entityManager.unwrap(Session.class);
        String sql="select SEQ_NUM from win_ta_test_set_lines where TEST_SET_ID=:testSetId order by SEQ_NUM desc";
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter("testSetId", testSetId);

		List results = query.list();
		if(results.size()>0) {
			System.out.println(results.get(0));
		
		BigDecimal bigDecimal= (BigDecimal)results.get(0);
		Integer id=Integer.parseInt(bigDecimal.toString());
		return id;
		}else {
		return 0;
		}	
	}

	public void updateTestrun(Testrundata getTestrun) {
		Session session = entityManager.unwrap(Session.class);
		session.update(getTestrun);
	}

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

}
