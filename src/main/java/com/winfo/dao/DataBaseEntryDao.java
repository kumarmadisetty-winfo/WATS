package com.winfo.dao;

import java.sql.SQLException;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.winfo.services.FetchConfigVO;
import com.winfo.services.FetchMetadataVO;

public class DataBaseEntryDao {
	@PersistenceContext
	EntityManager em;
	public  void updatePassedScriptLineStatus(FetchMetadataVO fetchMetadataVO,FetchConfigVO fetchConfigVO,String test_script_param_id, String status) throws ClassNotFoundException, SQLException {
		Query query = em.createQuery("Update TestSetScriptParam set line_execution_status='Pass' where test_script_param_id="+"'"+test_script_param_id+"'");
		query.executeUpdate();
	}
	/*
	 * public void updateFailedScriptLineStatus(FetchMetadataVO
	 * fetchMetadataVO,FetchConfigVO fetchConfigVO,String
	 * test_script_param_id,String status,String error_message) throws
	 * ClassNotFoundException, SQLException { Query query = em.
	 * createQuery("Update TestSetScriptParam set line_execution_status='Fail',line_error_message="
	 * +"'"+error_message+"'"+",where test_script_param_id="+"'"+
	 * test_script_param_id+"'"); query.executeUpdate(); }
	 */	
}
