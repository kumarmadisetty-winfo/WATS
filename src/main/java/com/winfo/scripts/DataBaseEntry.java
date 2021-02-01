package com.winfo.scripts;
import java.sql.Connection;
import java.util.Date;

 

import org.springframework.stereotype.Service;

 

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;

 

import com.winfo.services.FetchConfigVO;
import com.winfo.services.FetchMetadataVO;

 

@Service
public class DataBaseEntry {

 


public  void updatePassedScriptLineStatus(FetchMetadataVO fetchMetadataVO,FetchConfigVO fetchConfigVO,String test_script_param_id, String status) throws ClassNotFoundException, SQLException {
    // Added try catch blocks
	Connection conn=null;
	Statement st=null;
	try {
    Class.forName("oracle.jdbc.driver.OracleDriver");
    conn = DriverManager.getConnection(fetchConfigVO.getDb_host(), fetchConfigVO.getDb_username(),
            "DB_PASSWORD");
    st = conn.createStatement();
    String sqlQuery="Update WATS_PROD.WIN_TA_TEST_SET_SCRIPT_PARAM  SET LINE_EXECUTION_STATUS='Pass' where TEST_SCRIPT_PARAM_ID='"+test_script_param_id+"'";
    st.executeQuery(sqlQuery);
    }
    catch (Exception e) {
		System.out.println(e);
	}finally {
		conn.close();
		 st.close();
    }
}
public  void updateFailedScriptLineStatus(FetchMetadataVO fetchMetadataVO,FetchConfigVO fetchConfigVO,String test_script_param_id,String status,String error_message) throws ClassNotFoundException, SQLException {
	// Added try catch blocks
	Connection conn=null;
	Statement st=null;
	try {
    Class.forName("oracle.jdbc.driver.OracleDriver");
    conn = DriverManager.getConnection(fetchConfigVO.getDb_host(), fetchConfigVO.getDb_username(),
            "DB_PASSWORD");
    st = conn.createStatement();
    String sqlQuery="Update WATS_PROD.WIN_TA_TEST_SET_SCRIPT_PARAM  SET LINE_EXECUTION_STATUS='Fail',LINE_ERROR_MESSAGE='"+error_message+"' where TEST_SCRIPT_PARAM_ID='"+test_script_param_id+"'";
    st.executeQuery(sqlQuery);
	}
	catch (Exception e) {
		System.out.println(e);
	}finally {
		conn.close();
		 st.close();
   }
}
//new change-database to get error message
public  String getErrorMessage(String sndo,String ScriptName,String testRunName,FetchConfigVO fetchConfigVO) throws ClassNotFoundException, SQLException {
	Connection conn=null;
	Statement st=null;
	String error=null;
	try {
    Class.forName("oracle.jdbc.driver.OracleDriver");
    conn = DriverManager.getConnection(fetchConfigVO.getDb_host(), fetchConfigVO.getDb_username(),
            "DB_PASSWORD");
    st = conn.createStatement();
    String sqlQuery="SELECT PARAM.LINE_ERROR_MESSAGE "
			+ "FROM WIN_TA_TEST_SET_SCRIPT_PARAM PARAM,WIN_TA_TEST_SET_LINES LINES,WIN_TA_TEST_SET TS "
			+ "WHERE TS.TEST_SET_ID = LINES.TEST_SET_ID "
			+ "AND LINES.TEST_SET_LINE_ID = PARAM.TEST_SET_LINE_ID "
			+ "AND TS.TEST_SET_ID =(SELECT TEST_SET_ID FROM WIN_TA_TEST_SET WHERE UPPER(TEST_SET_NAME)=UPPER('"+testRunName+"')) "
			+ "AND UPPER(LINES.SCRIPT_NUMBER) = UPPER('"+ScriptName+"') "
			+ "AND LINES.SEQ_NUM = "+sndo+" "
			+ "AND PARAM.LINE_ERROR_MESSAGE IS NOT NULL";
    ResultSet rs=st.executeQuery(sqlQuery);
    
    if(rs.next()){
    	error=rs.getString("LINE_ERROR_MESSAGE");
    	System.out.println(error);
    }
	}
	catch (Exception e) {
		System.out.println(e);
	}finally {
		conn.close();
		 st.close();
   }
	return error;
}

public  void updateInProgressScriptLineStatus(FetchMetadataVO fetchMetadataVO,FetchConfigVO fetchConfigVO,String test_script_param_id,String status) throws ClassNotFoundException, SQLException {
//Added try catch blocks	
	Connection conn=null;
	Statement st=null;
	try {
    Class.forName("oracle.jdbc.driver.OracleDriver");
    conn = DriverManager.getConnection(fetchConfigVO.getDb_host(), fetchConfigVO.getDb_username(),
            "DB_PASSWORD");
    st = conn.createStatement();
    String sqlQuery="Update WATS_PROD.WIN_TA_TEST_SET_SCRIPT_PARAM  SET LINE_EXECUTION_STATUS='In-Progress' where TEST_SCRIPT_PARAM_ID='"+test_script_param_id+"'";
    st.executeQuery(sqlQuery);
	}
    catch (Exception e) {
		System.out.println(e);
	}
	finally {
		conn.close();
		 st.close();
}
}
public  void updateInProgressScriptStatus(FetchConfigVO fetchConfigVO,String test_set_id,String test_set_line_id) throws ClassNotFoundException, SQLException {
	//Added try catch blocks	
		Connection conn=null;
		Statement st=null;
		try {
	    Class.forName("oracle.jdbc.driver.OracleDriver");
	    conn = DriverManager.getConnection(fetchConfigVO.getDb_host(), fetchConfigVO.getDb_username(),
	            "Winfo_123");
	    st = conn.createStatement();
	    String sqlQuery="Update WATS_PROD.WIN_TA_TEST_SET_LINES SET Status='IN-PROGRESS' where test_set_id="+test_set_id +"and test_set_line_id="+test_set_line_id;
	    st.executeQuery(sqlQuery);
		}
	    catch (Exception e) {
			System.out.println(e);
		}
		finally {
			conn.close();
			 st.close();
	}
	}

public void updateStartTime(FetchConfigVO fetchConfigVO,String line_id, String test_set_id) throws ClassNotFoundException, SQLException{
//Added try catch blocks    
//System.out.println("Start Method");
	Connection conn=null;
	Statement st=null;
	try {
    Class.forName("oracle.jdbc.driver.OracleDriver");
    conn = DriverManager.getConnection(fetchConfigVO.getDb_host(), fetchConfigVO.getDb_username(),
            "DB_PASSWORD");
    st = conn.createStatement();
    //DateFormat format = new SimpleDateFormat("MM/DD/YYYY HH24:MI:SS");
    Format startformat=new SimpleDateFormat("M/dd/yyyy HH:mm:ss");
    Date start_time1=fetchConfigVO.getStarttime();
    String start_time= startformat.format(start_time1);
    //System.out.println(start_time);
    String sqlQuery="Update WATS_PROD.WIN_TA_TEST_SET_LINES  SET EXECUTION_START_TIME=TO_TIMESTAMP('"+start_time+"','MM/DD/YYYY HH24:MI:SS') WHERE TEST_SET_ID="+test_set_id+" AND TEST_SET_LINE_ID = "+line_id;
    st.executeQuery(sqlQuery);
	}
    catch (Exception e) {
		System.out.println(e);
	}
	finally {
		conn.close();
		 st.close();
    }
    
}
public void updateEndTime(FetchConfigVO fetchConfigVO,String line_id,String test_set_id) throws ClassNotFoundException, SQLException{
	//Added try catch blocks
	Connection conn=null;
	Statement st=null;
	try {
	Class.forName("oracle.jdbc.driver.OracleDriver");
    conn = DriverManager.getConnection(fetchConfigVO.getDb_host(), fetchConfigVO.getDb_username(),
            "DB_PASSWORD");
    st = conn.createStatement();
    Format startformat=new SimpleDateFormat("M/dd/yyyy HH:mm:ss");
    Date end_time1=fetchConfigVO.getEndtime();
    String end_time= startformat.format(end_time1);
    String sqlQuery="Update WATS_PROD.WIN_TA_TEST_SET_LINES  SET EXECUTION_END_TIME=TO_TIMESTAMP('"+end_time+"','MM/DD/YYYY HH24:MI:SS') WHERE  TEST_SET_ID="+test_set_id+" AND TEST_SET_LINE_ID ="+line_id;
    st.executeQuery(sqlQuery);
	}
    catch (Exception e) {
		System.out.println(e);
	}
	finally {
		conn.close();
		 st.close();
    }
    
  }
public String getTrMode(String args,FetchConfigVO fetchConfigVO) throws SQLException {
	Connection conn=null;
	Statement st=null;
	String states = null;
	try {
	Class.forName("oracle.jdbc.driver.OracleDriver");
    conn = DriverManager.getConnection(fetchConfigVO.getDb_host(), fetchConfigVO.getDb_username(),
            "DB_PASSWORD");
    st = conn.createStatement();
    String sqlQuery="SELECT TR_MODE FROM WIN_TA_TEST_SET WHERE TEST_SET_ID ="+args;
ResultSet rs=st.executeQuery(sqlQuery);
    
    if(rs.next()){
    	states=rs.getString("TR_MODE");
    	System.out.println(states);
    }
	
	}
    catch (Exception e) {
		System.out.println(e);
	}
	finally {
		conn.close();
		 st.close();
    }
	return states;
}
}