package com.winfo.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Session;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Repository;

import com.winfo.model.TestSet;
import com.winfo.model.TestSetLines;
import com.winfo.model.TestSetScriptParam;
import com.winfo.services.FetchConfigVO;
import com.winfo.services.FetchMetadataVO;

@Repository
@RefreshScope
public class DataBaseEntryDao {
	@PersistenceContext
	EntityManager em;
	public  void updatePassedScriptLineStatus(FetchMetadataVO fetchMetadataVO,FetchConfigVO fetchConfigVO,String test_script_param_id, String status) throws ClassNotFoundException, SQLException {
		Query query = em.createQuery("Update TestSetScriptParam set line_execution_status='Pass' where test_script_param_id="+"'"+test_script_param_id+"'");
		query.executeUpdate();
	}
	public  void updateFailedScriptLineStatus(FetchMetadataVO fetchMetadataVO,FetchConfigVO fetchConfigVO,String test_script_param_id,String status,String error_message) throws ClassNotFoundException, SQLException {
		String folder = (fetchConfigVO.getScreenshot_path() + fetchMetadataVO.getCustomer_name() + "/"
				+ fetchMetadataVO.getTest_run_name() + "/" + fetchMetadataVO.getSeq_num() + "_"
				+ fetchMetadataVO.getLine_number() + "_" + fetchMetadataVO.getScenario_name() + "_"
				+ fetchMetadataVO.getScript_number() + "_" + fetchMetadataVO.getTest_run_name() + "_"
				+ fetchMetadataVO.getLine_number() + "_Failed").concat(".jpg");
		TestSetScriptParam scriptParam=em.find(TestSetScriptParam.class,test_script_param_id);
		
		if(scriptParam==null) {
			throw new RuntimeException();
		}
		File file=new File(folder);
		byte[] screenshotArray=new byte[(int)file.length()];
		 try {
			FileInputStream fileInputStream = new FileInputStream(file);
			fileInputStream.read(screenshotArray);
			fileInputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//scriptParam.setLine_execution_statues(status);
		//scriptParam.setLine_error_message(error_message);
		String sql = "Update WATS_PROD.WIN_TA_TEST_SET_SCRIPT_PARAM  SET LINE_EXECUTION_STATUS='Fail',LINE_ERROR_MESSAGE= :error_message,SCREENSHOT= :screenshot where TEST_SCRIPT_PARAM_ID='"+test_script_param_id+"'";
		Session session= em.unwrap(Session.class);
		Query query=session.createSQLQuery(sql);
		query.setParameter("error",error_message);
		query.setParameter("screenshot",screenshotArray);
		query.executeUpdate();
	}
	public  String getErrorMessage(String sndo,String ScriptName,String testRunName,FetchConfigVO fetchConfigVO) throws ClassNotFoundException, SQLException {	
		String errorMessage="";
	    String sqlQuery="SELECT PARAM.LINE_ERROR_MESSAGE "
				+ "FROM WIN_TA_TEST_SET_SCRIPT_PARAM PARAM,WIN_TA_TEST_SET_LINES LINES,WIN_TA_TEST_SET TS "
				+ "WHERE TS.TEST_SET_ID = LINES.TEST_SET_ID "
				+ "AND LINES.TEST_SET_LINE_ID = PARAM.TEST_SET_LINE_ID "
				+ "AND TS.TEST_SET_ID = (SELECT TEST_SET_ID FROM WIN_TA_TEST_SET WHERE UPPER(TEST_SET_NAME)=UPPER('"+testRunName+"'))"
				+ "AND UPPER(LINES.SCRIPT_NUMBER) = UPPER('"+ScriptName+"') "
				+ "AND LINES.SEQ_NUM = "+sndo+" "
				+ "AND PARAM.LINE_ERROR_MESSAGE IS NOT NULL";
	    Session session = em.unwrap(Session.class);
	    	
	    			Query query = session.createSQLQuery(sqlQuery);
	    			errorMessage  = (String) query.getResultList().get(0);
	    			if(errorMessage==null) {
	    				throw new RuntimeException();
	    			}
	    	
		
		return errorMessage;
	}
	public  void updateInProgressScriptLineStatus(FetchMetadataVO fetchMetadataVO,FetchConfigVO fetchConfigVO,String test_script_param_id,String status) throws ClassNotFoundException, SQLException {
		TestSetScriptParam scriptParam=em.find(TestSetScriptParam.class,Integer.parseInt(test_script_param_id));
		if(scriptParam==null) {
			throw new RuntimeException();
		}
		scriptParam.setLine_execution_statues(status);
		em.merge(scriptParam);
	}
	public  void updateInProgressScriptStatus(FetchConfigVO fetchConfigVO,String test_set_id,String test_set_line_id) throws ClassNotFoundException, SQLException {
		TestSetLines testLines=em.find(TestSetLines.class, Integer.parseInt(test_set_line_id));
		if(testLines==null) {
			throw new RuntimeException();
		}
		testLines.setStatus("IN-PROGRESS");
		em.merge(testLines);
	}
	public void updateStartTime(FetchConfigVO fetchConfigVO,String line_id, String test_set_id,Date start_time1) throws ClassNotFoundException, SQLException{
		Format startformat=new SimpleDateFormat("M/dd/yyyy HH:mm:ss");
		String start_time= startformat.format(start_time1);
		Session session = em.unwrap(Session.class);
		Query query=session.createSQLQuery("Update WATS_PROD.WIN_TA_TEST_SET_LINES  SET EXECUTION_START_TIME=TO_TIMESTAMP('"+start_time+"','MM/DD/YYYY HH24:MI:SS') WHERE TEST_SET_ID="+test_set_id+" AND TEST_SET_LINE_ID = "+line_id);
		query.executeUpdate();
		
	}
	public String getTrMode(String args,FetchConfigVO fetchConfigVO) throws SQLException {
		TestSet testSet=em.find(TestSet.class,Integer.parseInt(args));
		if(testSet==null) {
			throw new RuntimeException();
		}
		return testSet.getTr_mode();
	}
	public String getPassword(String args, String userId, FetchConfigVO fetchConfigVO)
			throws SQLException, ClassNotFoundException {
		Session session=em.unwrap(Session.class);
		String sqlStr = "select WIN_DBMS_CRYPTO.DECRYPT(users.password , users.encrypt_key) PASSWORD from win_ta_test_set test_set,win_ta_config config,win_ta_config_users users where test_set.configuration_id = config.configuration_id and config.configuration_id = users.config_id and test_set.test_set_id = "
				+ args + " and (upper(users.user_name) = upper('" + userId + "') or ('" + userId
				+ "' is null and users.default_user = 'Y')) and rownum = 1";
		Query query=session.createSQLQuery(sqlStr);
		String password=password  = (String) query.getSingleResult();
		return password;
		
	}
	public void updateEndTime(FetchConfigVO fetchConfigVO,String line_id,String test_set_id,Date end_time1) throws ClassNotFoundException, SQLException{
		Format startformat=new SimpleDateFormat("M/dd/yyyy HH:mm:ss");
		String end_time= startformat.format(end_time1);
		Session session=em.unwrap(Session.class);
		String sqlQuery="Update WATS_PROD.WIN_TA_TEST_SET_LINES  SET EXECUTION_END_TIME=TO_TIMESTAMP('"+end_time+"','MM/DD/YYYY HH24:MI:SS') WHERE  TEST_SET_ID="+test_set_id+" AND TEST_SET_LINE_ID ="+line_id;
		Query query=session.createSQLQuery(sqlQuery);
		query.executeUpdate();
	}
	public void updateFailedImages(FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO,
			String test_script_param_id) throws SQLException {
		String folder = (fetchConfigVO.getScreenshot_path() + fetchMetadataVO.getCustomer_name() + "/"

				+ fetchMetadataVO.getTest_run_name() + "/" + fetchMetadataVO.getSeq_num() + "_"

				+ fetchMetadataVO.getLine_number() + "_" + fetchMetadataVO.getScenario_name() + "_"

				+ fetchMetadataVO.getScript_number() + "_" + fetchMetadataVO.getTest_run_name() + "_"

				+ fetchMetadataVO.getLine_number() + "_Passed").concat(".jpg");
		
		 File file=new File(folder);
		 byte[] screenshotArray=new byte[(int)file.length()];
		 try {
			FileInputStream fileInputStream = new FileInputStream(file);
			fileInputStream.read(screenshotArray);
			fileInputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String sql="Update WATS_PROD.WIN_TA_TEST_SET_SCRIPT_PARAM  SET SCREENSHOT= :screenshot where TEST_SCRIPT_PARAM_ID='"+test_script_param_id+"'";
		Query query=em.unwrap(Session.class).createSQLQuery(sql);
		query.setParameter("screenshot",screenshotArray);
		query.executeUpdate();
	}
}



















































