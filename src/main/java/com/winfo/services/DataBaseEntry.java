package com.winfo.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.winfo.dao.DataBaseEntryDao;

@Service
@RefreshScope
public class DataBaseEntry {
	@Autowired
	DataBaseEntryDao dao;
	@Transactional
	public  void updatePassedScriptLineStatus(FetchMetadataVO fetchMetadataVO,FetchConfigVO fetchConfigVO,String test_script_param_id, String status) throws ClassNotFoundException, SQLException {
		dao.updatePassedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, status);
	}
	@Transactional
	public  void updateFailedScriptLineStatus(FetchMetadataVO fetchMetadataVO,FetchConfigVO fetchConfigVO,String test_script_param_id,String status,String error_message) throws ClassNotFoundException, SQLException {
		dao.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, status, error_message);
	}
	@Transactional
	public  void updateInProgressScriptLineStatus(FetchMetadataVO fetchMetadataVO,FetchConfigVO fetchConfigVO,String test_script_param_id,String status) throws ClassNotFoundException, SQLException {
		dao.updateInProgressScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, status);
	}
	@Transactional
	public  String getErrorMessage(String sndo,String ScriptName,String testRunName,FetchConfigVO fetchConfigVO) throws ClassNotFoundException, SQLException {
		return dao.getErrorMessage(sndo, ScriptName, testRunName, fetchConfigVO);
	}
	@Transactional
	public  void updateInProgressScriptStatus(FetchConfigVO fetchConfigVO,String test_set_id,String test_set_line_id) throws ClassNotFoundException, SQLException {
		dao.updateInProgressScriptStatus(fetchConfigVO, test_set_id, test_set_line_id);
	}
	@Transactional
	public void updateStartTime(FetchConfigVO fetchConfigVO,String line_id, String test_set_id,Date start_time1) throws ClassNotFoundException, SQLException{
		dao.updateStartTime(fetchConfigVO, line_id, test_set_id, start_time1);
	}
	@Transactional
	public String getTrMode(String args,FetchConfigVO fetchConfigVO) throws SQLException {
		return dao.getTrMode(args, fetchConfigVO);
	}
	@Transactional
	public String getPassword(String args, String userId, FetchConfigVO fetchConfigVO)
			throws SQLException, ClassNotFoundException {
		return dao.getPassword(args, userId, fetchConfigVO);
	}
	@Transactional
	public void updateEndTime(FetchConfigVO fetchConfigVO,String line_id,String test_set_id,Date end_time1) throws ClassNotFoundException, SQLException{
		dao.updateEndTime(fetchConfigVO, line_id, test_set_id, end_time1);
	}
	@Transactional
	public void updateFailedImages(FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO,
			String test_script_param_id) throws SQLException {
			dao.updateFailedImages(fetchMetadataVO, fetchConfigVO, test_script_param_id);
	}
	@Transactional
	public String getNodeOs(Integer test_set_line_id) {
		
		return dao.getNodeOs(test_set_line_id);
		
	}
	
	@Transactional
	public void getDependentScriptNumbers(LinkedHashMap<String, List<FetchMetadataVO>> dependentScriptMap) {
		List<Integer> dependentList = new ArrayList();
		for(Entry<String,List<FetchMetadataVO>> element:dependentScriptMap.entrySet()) {
			dependentList.add(Integer.parseInt(element.getValue().get(0).getScript_id()));
		}
		
		
		dao.getDependentScriptNumbers(dependentScriptMap,dependentList);
	}
}


































