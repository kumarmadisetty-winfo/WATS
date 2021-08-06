package com.winfo.scripts;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

 

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;

 

import com.winfo.services.FetchConfigVO;
import com.winfo.services.FetchMetadataVO;

 

@Service
@RefreshScope
public class DataBaseEntry {

	@Value("${database.dbpassword}")
	private  String dbPassword;


public  void updatePassedScriptLineStatus(FetchMetadataVO fetchMetadataVO,FetchConfigVO fetchConfigVO,String test_script_param_id, String status) throws ClassNotFoundException, SQLException {
    // Added try catch blocks
	Connection conn=null;
	PreparedStatement st=null; 
	try {
    Class.forName("oracle.jdbc.driver.OracleDriver");
    conn = DriverManager.getConnection(fetchConfigVO.getDb_host(), fetchConfigVO.getDb_username(),
    		dbPassword);
    String folder = (fetchConfigVO.getScreenshot_path() + fetchMetadataVO.getCustomer_name() + "/"

			+ fetchMetadataVO.getTest_run_name() + "/" + fetchMetadataVO.getSeq_num() + "_"

			+ fetchMetadataVO.getLine_number() + "_" + fetchMetadataVO.getScenario_name() + "_"

			+ fetchMetadataVO.getScript_number() + "_" + fetchMetadataVO.getTest_run_name() + "_"

			+ fetchMetadataVO.getLine_number() + "_Passed").concat(".jpg");
    File file=new File(folder);
    InputStream in = new FileInputStream(file);
    st= conn.prepareStatement("Update WATS_PROD.WIN_TA_TEST_SET_SCRIPT_PARAM  SET LINE_EXECUTION_STATUS='Pass',SCREENSHOT=? where TEST_SCRIPT_PARAM_ID='"+test_script_param_id+"'");
    st.setBinaryStream(1,in,(int)file.length());   
    st.executeUpdate();
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
	PreparedStatement st=null; 
	try {
    Class.forName("oracle.jdbc.driver.OracleDriver");
    conn = DriverManager.getConnection(fetchConfigVO.getDb_host(), fetchConfigVO.getDb_username(),
    		dbPassword);
    String folder = (fetchConfigVO.getScreenshot_path() + fetchMetadataVO.getCustomer_name() + "/"
			+ fetchMetadataVO.getTest_run_name() + "/" + fetchMetadataVO.getSeq_num() + "_"
			+ fetchMetadataVO.getLine_number() + "_" + fetchMetadataVO.getScenario_name() + "_"
			+ fetchMetadataVO.getScript_number() + "_" + fetchMetadataVO.getTest_run_name() + "_"
			+ fetchMetadataVO.getLine_number() + "_Failed").concat(".jpg");
    File file=new File(folder);
    InputStream in = new FileInputStream(file);
    st= conn.prepareStatement("Update WATS_PROD.WIN_TA_TEST_SET_SCRIPT_PARAM  SET LINE_EXECUTION_STATUS='Fail',LINE_ERROR_MESSAGE=?,SCREENSHOT=? where TEST_SCRIPT_PARAM_ID='"+test_script_param_id+"'");
    st.setString(1, error_message);
    st.setBinaryStream(2,in,(int)file.length());   
    st.executeUpdate();
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
    		dbPassword);
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
    		dbPassword);
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
	    		dbPassword);
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

public void updateStartTime(FetchConfigVO fetchConfigVO,String line_id, String test_set_id,Date start_time1) throws ClassNotFoundException, SQLException{
//Added try catch blocks    
//System.out.println("Start Method");
	Connection conn=null;
	Statement st=null;
	try {
    Class.forName("oracle.jdbc.driver.OracleDriver");
    conn = DriverManager.getConnection(fetchConfigVO.getDb_host(), fetchConfigVO.getDb_username(),
    		dbPassword);
    st = conn.createStatement();
    //DateFormat format = new SimpleDateFormat("MM/DD/YYYY HH24:MI:SS");
    Format startformat=new SimpleDateFormat("M/dd/yyyy HH:mm:ss");
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
public void updateEndTime(FetchConfigVO fetchConfigVO,String line_id,String test_set_id,Date end_time1) throws ClassNotFoundException, SQLException{
	//Added try catch blocks
	Connection conn=null;
	Statement st=null;
	try {
	Class.forName("oracle.jdbc.driver.OracleDriver");
    conn = DriverManager.getConnection(fetchConfigVO.getDb_host(), fetchConfigVO.getDb_username(),
    		dbPassword);
    st = conn.createStatement();
    Format startformat=new SimpleDateFormat("M/dd/yyyy HH:mm:ss");
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
    		dbPassword);
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
public String getPassword(String args, String userId, FetchConfigVO fetchConfigVO)
		throws SQLException, ClassNotFoundException {

	String password = null;
	Connection conn=null;
	Statement st=null;
	ResultSet rs=null;
	try {
	Class.forName("oracle.jdbc.driver.OracleDriver");

//                       Connection conn = DriverManager.getConnection(fetchConfigVO.getDb_host(),fetchConfigVO.getDb_username(),fetchConfigVO.getDb_password());

	 conn = DriverManager.getConnection(fetchConfigVO.getDb_host(), fetchConfigVO.getDb_username(),
			 dbPassword);

	 st = conn.createStatement();

//                       String sqlStr = "SELECT TOOLKIT.DECRYPT(PASSWORD) PASSWORD FROM WIN_TA_CONFIG WHERE CONFIGURATION_ID = (SELECT CONFIGURATION_ID FROM WIN_TA_TEST_SET WHERE TEST_SET_ID = "+args+")";

	String sqlStr = "select WIN_DBMS_CRYPTO.DECRYPT(users.password , users.encrypt_key) PASSWORD from win_ta_test_set test_set,win_ta_config config,win_ta_config_users users where test_set.configuration_id = config.configuration_id and config.configuration_id = users.config_id and test_set.test_set_id = "
			+ args + " and (upper(users.user_name) = upper('" + userId + "') or ('" + userId
			+ "' is null and users.default_user = 'Y')) and rownum = 1";

	 rs = st.executeQuery(sqlStr);

	System.out.println(rs);

	while (rs.next()) {

		password = rs.getString("PASSWORD");

		System.out.println(password);

	}
	}catch (Exception e) {
		System.out.println(e);
	}finally {
		conn.close();
		 st.close();
		rs.close();

	}


	return password;

}

}