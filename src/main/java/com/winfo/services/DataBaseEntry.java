package com.winfo.services;

import java.sql.SQLException;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.winfo.dao.DataBaseEntryDao;

@Service
@RefreshScope
@Transactional
public class DataBaseEntry {
	@Autowired
	DataBaseEntryDao dao;
	
	public  void updatePassedScriptLineStatus(FetchMetadataVO fetchMetadataVO,FetchConfigVO fetchConfigVO,String test_script_param_id, String status) throws ClassNotFoundException, SQLException {
		dao.updatePassedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, status);
	}
	public  void updateFailedScriptLineStatus(FetchMetadataVO fetchMetadataVO,FetchConfigVO fetchConfigVO,String test_script_param_id,String status,String error_message) throws ClassNotFoundException, SQLException {
		dao.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, status, error_message);
	}
	public  void updateInProgressScriptLineStatus(FetchMetadataVO fetchMetadataVO,FetchConfigVO fetchConfigVO,String test_script_param_id,String status) throws ClassNotFoundException, SQLException {
		dao.updateInProgressScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, status);
	}
	public  String getErrorMessage(String sndo,String ScriptName,String testRunName,FetchConfigVO fetchConfigVO) throws ClassNotFoundException, SQLException {
		return dao.getErrorMessage(sndo, ScriptName, testRunName, fetchConfigVO);
	}
	
	public  void updateInProgressScriptStatus(FetchConfigVO fetchConfigVO,String test_set_id,String test_set_line_id) throws ClassNotFoundException, SQLException {
		dao.updateInProgressScriptStatus(fetchConfigVO, test_set_id, test_set_line_id);
	}
	public void updateStartTime(FetchConfigVO fetchConfigVO,String line_id, String test_set_id,Date start_time1) throws ClassNotFoundException, SQLException{
		dao.updateStartTime(fetchConfigVO, line_id, test_set_id, start_time1);
	}
	public String getTrMode(String args,FetchConfigVO fetchConfigVO) throws SQLException {
		return dao.getTrMode(args, fetchConfigVO);
	}
	public String getPassword(String args, String userId, FetchConfigVO fetchConfigVO)
			throws SQLException, ClassNotFoundException {
		return dao.getPassword(args, userId, fetchConfigVO);
	}
	public void updateEndTime(FetchConfigVO fetchConfigVO,String line_id,String test_set_id,Date end_time1) throws ClassNotFoundException, SQLException{
		dao.updateEndTime(fetchConfigVO, line_id, test_set_id, end_time1);
	}
	public void updateFailedImages(FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO,
			String test_script_param_id) throws SQLException {
			dao.updateFailedImages(fetchMetadataVO, fetchConfigVO, test_script_param_id);
	}
}

































