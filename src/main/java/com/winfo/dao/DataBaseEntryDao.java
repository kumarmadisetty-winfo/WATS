package com.winfo.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import com.winfo.vo.Status;

@Repository
@RefreshScope
public class DataBaseEntryDao {
	@PersistenceContext
	EntityManager em;
	public  void updatePassedScriptLineStatus(FetchMetadataVO fetchMetadataVO,FetchConfigVO fetchConfigVO,String test_script_param_id, String status) throws ClassNotFoundException, SQLException {
		try {
		Query query = em.createQuery("Update TestSetScriptParam set line_execution_status='Pass' where test_script_param_id="+"'"+test_script_param_id+"'");
		query.executeUpdate();
		}catch(Exception e) {
			System.out.println("cant update passed script line status");
			System.out.println(e);
		}
	}
	public  void updateFailedScriptLineStatus(FetchMetadataVO fetchMetadataVO,FetchConfigVO fetchConfigVO,String test_script_param_id,String status,String error_message) throws ClassNotFoundException, SQLException {
		/*
		 * String folder = (fetchConfigVO.getScreenshot_path() +
		 * fetchMetadataVO.getCustomer_name() + "/" + fetchMetadataVO.getTest_run_name()
		 * + "/" + fetchMetadataVO.getSeq_num() + "_" + fetchMetadataVO.getLine_number()
		 * + "_" + fetchMetadataVO.getScenario_name() + "_" +
		 * fetchMetadataVO.getScript_number() + "_" + fetchMetadataVO.getTest_run_name()
		 * + "_" + fetchMetadataVO.getLine_number() + "_Failed").concat(".jpg");
		 * 
		 * File file=new File(folder); byte[] screenshotArray=new
		 * byte[(int)file.length()]; try { FileInputStream fileInputStream = new
		 * FileInputStream(file); fileInputStream.read(screenshotArray);
		 * fileInputStream.close(); } catch (FileNotFoundException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } catch (IOException e) { //
		 * TODO Auto-generated catch block e.printStackTrace(); }
		 */
		//scriptParam.setLine_execution_statues(status);
		//scriptParam.setLine_error_message(error_message);
		String sql = "Update WATS_PROD.WIN_TA_TEST_SET_SCRIPT_PARAM  SET LINE_EXECUTION_STATUS='Fail',LINE_ERROR_MESSAGE= :error_message where TEST_SCRIPT_PARAM_ID='"+test_script_param_id+"'";
		Session session= em.unwrap(Session.class);
		Query query=session.createSQLQuery(sql);
		query.setParameter("error_message",error_message);
		//query.setParameter("screenshot",screenshotArray);
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
	    			
	    try {
	    			Session session = em.unwrap(Session.class);
	    			Query query = session.createSQLQuery(sqlQuery);
	    			errorMessage  = (String) query.getResultList().get(0);
					/*
					 * if(errorMessage==null) { throw new RuntimeException(); }
					 */
	    }catch(Exception e) {
	    	System.out.println("cant get error message");
	    	System.out.println(e);
	    }
		
		return errorMessage;
	}
	public  void updateInProgressScriptLineStatus(FetchMetadataVO fetchMetadataVO,FetchConfigVO fetchConfigVO,String test_script_param_id,String status) throws ClassNotFoundException, SQLException {
		try {
		TestSetScriptParam scriptParam=em.find(TestSetScriptParam.class,Integer.parseInt(test_script_param_id));
		/*
		 * if(scriptParam==null) { throw new RuntimeException(); }
		 */
		if(scriptParam!=null) {
		scriptParam.setLine_execution_statues(status);
		em.merge(scriptParam);
		}
		}catch(Exception e) {
			System.out.println("cant update inprogress scriptLine status");
			System.out.println(e);
		}
	}
	public  void updateInProgressScriptStatus(FetchConfigVO fetchConfigVO,String test_set_id,String test_set_line_id) throws ClassNotFoundException, SQLException {
		try {
		TestSetLines testLines=em.find(TestSetLines.class, Integer.parseInt(test_set_line_id));
		
		/* if(testLines==null) { throw new RuntimeException(); } */
		if(testLines!=null) { 
		testLines.setStatus("IN-PROGRESS");
		em.merge(testLines);
		}
		}catch(Exception e) {
			System.out.println("cant update in progress script status");
			System.out.println(e);
		}
	}
	public void updateStartTime(FetchConfigVO fetchConfigVO,String line_id, String test_set_id,Date start_time1) throws ClassNotFoundException, SQLException{
		Format startformat=new SimpleDateFormat("M/dd/yyyy HH:mm:ss");
		String start_time= startformat.format(start_time1);
		try {
		Session session = em.unwrap(Session.class);
		Query query=session.createSQLQuery("Update WATS_PROD.WIN_TA_TEST_SET_LINES  SET EXECUTION_START_TIME=TO_TIMESTAMP('"+start_time+"','MM/DD/YYYY HH24:MI:SS') WHERE TEST_SET_ID="+test_set_id+" AND TEST_SET_LINE_ID = "+line_id);
		query.executeUpdate();
		}catch(Exception e) {
			System.out.println("cant update starttime");
			System.out.println(e);
		}
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
		String password=null;
		String sqlStr = "select WIN_DBMS_CRYPTO.DECRYPT(users.password , users.encrypt_key) PASSWORD from win_ta_test_set test_set,win_ta_config config,win_ta_config_users users where test_set.configuration_id = config.configuration_id and config.configuration_id = users.config_id and test_set.test_set_id = "
				+ args + " and (upper(users.user_name) = upper('" + userId + "') or ('" + userId
				+ "' is null and users.default_user = 'Y')) and rownum = 1";
		 
		try {
		 Query query=session.createSQLQuery(sqlStr);
		 password= (String) query.getSingleResult();
		}catch(Exception e) {
			System.out.println("________NO_Password________");
		}
		return password;
		
	}
	public void updateEndTime(FetchConfigVO fetchConfigVO,String line_id,String test_set_id,Date end_time1) throws ClassNotFoundException, SQLException{
		Format startformat=new SimpleDateFormat("M/dd/yyyy HH:mm:ss");
		String end_time= startformat.format(end_time1);
		
		try {
		Session session=em.unwrap(Session.class);
		String sqlQuery="Update WATS_PROD.WIN_TA_TEST_SET_LINES  SET EXECUTION_END_TIME=TO_TIMESTAMP('"+end_time+"','MM/DD/YYYY HH24:MI:SS') WHERE  TEST_SET_ID="+test_set_id+" AND TEST_SET_LINE_ID ="+line_id;
		Query query=session.createSQLQuery(sqlQuery);
		query.executeUpdate();
		}catch(Exception e) {
			System.out.println("cannot update endTime");
			System.out.println(e);
		}
	}
	public void updateFailedImages(FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO,
			String test_script_param_id) throws SQLException {
		try {
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
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	public Map<String, Map<String, TestSetScriptParam>> getTestRunMap(String test_run_id) {
		// TODO Auto-generated method stub
		//FetchMetadataVO metadataVO=new FetchMetadataVO();
		Map<String,Map<String,TestSetScriptParam>> map=new HashMap<String,Map<String,TestSetScriptParam>> ();
		String sql="from TestSetLines where testSet=:testSet";
		Integer test_run_id2=Integer.parseInt(test_run_id);
		Query query = em.createQuery(sql);
		query.setParameter("testSet",em.find(TestSet.class,test_run_id2) );
		List<TestSetLines> test_set_lines_list = query.getResultList();
		for(TestSetLines test_set_line:test_set_lines_list) {
			Map<String,TestSetScriptParam> map2	= getTestScriptMap(test_set_line);
			map.put(String.valueOf(test_set_line.getSeq_num()),map2);
				
		}
		return map;
		
	}
	
	
	public Map<String,TestSetScriptParam> getTestScriptMap(TestSetLines test_set_line) {
		String sql="from TestSetScriptParam where testSetLines=:testSetLines";
		Query query = em.createQuery(sql);
		query.setParameter("testSetLines",test_set_line);
		List<TestSetScriptParam> testScriptParamList=query.getResultList();
		Map<String,TestSetScriptParam> map2 = new HashMap<String,TestSetScriptParam>();
		for(TestSetScriptParam scriptParam:testScriptParamList) {
			map2.put(String.valueOf(scriptParam.getLine_number()), scriptParam);
		}
		//map.put(String.valueOf(test_set_line.getSeq_num()),map2);
		return map2;
	}
	public TestSetLines getTestSetLine(String test_set_line_id) {
		// TODO Auto-generated method stub
		return em.find(TestSetLines.class, Integer.parseInt(test_set_line_id));
	}

	public void getDependentScriptNumbers(LinkedHashMap<String, List<FetchMetadataVO>> dependentScriptMap, List<Integer> dependentList) {
		// TODO Auto-generated method stub
		String sql = "Select script_id,dependency from ScriptMaster where script_id in (:dependentList)";
		Query query = em.unwrap(Session.class).createQuery(sql).setParameterList("dependentList",dependentList);
		
		List<Object[]> scriptList = query.getResultList();
		//Object[] objectArray = scriptList.toArray();
		Map<Integer,Integer> map =new HashMap<Integer,Integer>();
		
		for(Object[] obj:scriptList) {
			map.put((Integer)obj[0],(Integer)obj[1]);
		}
		
		for(Entry<String, List<FetchMetadataVO>> element:dependentScriptMap.entrySet()) {
			element.getValue().get(0).setDependencyScriptNumber(map.get(Integer.parseInt(element.getValue().get(0).getScript_id())));
			
		}
		
	}
	

public void getStatus(Integer dependentScriptNo,Integer test_set_id, Map<Integer, Status> scriptStatus) {
		// TODO Auto-generated method stub
		String sq1 = "select status from win_ta_test_set_lines where test_set_id=:test_set_id and script_id=:dependentScriptNo";
		
		
		Query query = em.unwrap(Session.class).createSQLQuery(sq1);
		query.setParameter("test_set_id",test_set_id);
		query.setParameter("dependentScriptNo",dependentScriptNo);
		//query.setPara
		
		List<String>list =	query.getResultList();
		
		
		Status status=new Status();
		int awaitCount=0;
		if(list!=null) {
		if((list.size()>0)&&(!(list.contains("Fail") || list.contains("FAIL"))) && (!(list.contains("New")||list.contains("NEW")))) {
		if((list.contains("In-Progress") || list.contains("IN-PROGRESS")) || (list.contains("In-Queue")||list.contains("IN-QUEUE"))) {
			status.setStatus("Wait");
			for(String stat:list) {
				if(!stat.equalsIgnoreCase("Pass")) {
					awaitCount++;
				}
			}
			status.setInExecutionCount(awaitCount);
			scriptStatus.put(dependentScriptNo, status);
		}else {
		status.setStatus("Pass");
		scriptStatus.put(dependentScriptNo, status);
		}
		}else {
			status.setStatus("Fail");
			scriptStatus.put(dependentScriptNo, status);
		}
		}else {
			status.setStatus("Fail");
			scriptStatus.put(dependentScriptNo, status);
		}
		
	}

	public String getNodeOs(Integer script_id) {
		// TODO Auto-generated method stub
		String os = null;
		try {
			String sql = "select operating_system from win_ta_script_master_os WHERE script_id = :scriptId";
			Query query = em.unwrap(Session.class).createSQLQuery(sql);
			query.setParameter("scriptId", script_id);
			os = query.getSingleResult().toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return os;
		}
	}
	
	
}



















































