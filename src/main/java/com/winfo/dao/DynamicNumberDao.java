package com.winfo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DynamicNumberDao {

	@Autowired
	private EntityManager entityManager;

	public void saveCopyNumber(String value, String testParamId, String testSetId) {
		// TODO Auto-generated method stub
		Session session = entityManager.unwrap(Session.class);
		
		String sql= "UPDATE WIN_TA_TEST_SET_SCRIPT_PARAM SET input_value=:input where TEST_SCRIPT_PARAM_ID=:testParamId and TEST_SET_LINE_ID=:testSetId";
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter("input", value);
		query.setParameter("testParamId", testParamId);
		query.setParameter("testSetId", testSetId);
		int i =query.executeUpdate();
		System.out.println("DynamicValueupdate:::::"+i);
	}

	public String getCopynumber(String testrun_name, String seq, String line_number, String testParamId, String testSetId) {

		Session session = entityManager.unwrap(Session.class);
		String sql="select input_value from win_ta_test_set_script_param where line_number=:line_number and test_set_line_id=(select test_set_line_id from win_ta_test_set_lines where test_set_id=(select test_set_id from win_ta_test_set where test_set_name=:testrun_name) and seq_num=:seq)";
		//String sql="select xpath_location from  win_ta_script_metadata where input_parameter=:params and script_number=:scripNumber";
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter("testrun_name", testrun_name);
		//query.setParameter("script_num", script_num);
		query.setParameter("seq", seq);
		query.setParameter("line_number", line_number);
		List results = query.list();
		if(results.size()>0) {
			System.out.println("getCopyNumber::::::"+(String) results.get(0));
			String copynumberValue=(String) results.get(0);
			//saveCopyNumber(copynumberValue,testParamId,testSetId);
			return copynumberValue;
		}
		else {
			return null;
		}
	}
	
	public void  getTestSetParamIdWithCopyAction(String key,String value, String testSetLineId, String testSetId) {
		Session session = entityManager.unwrap(Session.class);
		
		String sql= "UPDATE WIN_TA_TEST_SET_SCRIPT_PARAM SET input_value=:input where  TEST_SET_LINE_ID=:testSetLineId and action='ebsCopyValue' and input_parameter=:key";
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter("input", value);
		query.setParameter("testSetLineId", testSetLineId);
		query.setParameter("key", key);
		int i =query.executeUpdate();
		System.out.println("DynamicValueupdate:::::"+i);
	}
	
	public String getCopynumberInputParameter(String testrun_name, String seq, String line_number, String testParamId, String testSetId) {

		Session session = entityManager.unwrap(Session.class);
		String sql="select input_parameter from win_ta_test_set_script_param where line_number=:line_number and test_set_line_id=(select test_set_line_id from win_ta_test_set_lines where test_set_id=(select test_set_id from win_ta_test_set where test_set_name=:testrun_name) and seq_num=:seq)";
		//String sql="select xpath_location from  win_ta_script_metadata where input_parameter=:params and script_number=:scripNumber";
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter("testrun_name", testrun_name);
		//query.setParameter("script_num", script_num);
		query.setParameter("seq", seq);
		query.setParameter("line_number", line_number);
		List results = query.list();
		if(results.size()>0) {
			System.out.println("getCopyNumber::::::"+(String) results.get(0));
			String copynumberValue=(String) results.get(0);
			//saveCopyNumber(copynumberValue,testParamId,testSetId);
			return copynumberValue;
		}
		else {
			return null;
		}
	}
	

}
