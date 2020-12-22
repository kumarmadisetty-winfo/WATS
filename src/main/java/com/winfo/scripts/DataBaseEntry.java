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
    
    Class.forName("oracle.jdbc.driver.OracleDriver");
    Connection conn = DriverManager.getConnection(fetchConfigVO.getDb_host(), fetchConfigVO.getDb_username(),
            "Winfo_123");
    Statement st = conn.createStatement();
    
    String sqlQuery="Update WATS_PROD.WIN_TA_TEST_SET_SCRIPT_PARAM  SET LINE_EXECUTION_STATUS='Pass' where TEST_SCRIPT_PARAM_ID='"+test_script_param_id+"'";
    st.executeQuery(sqlQuery);
    
    conn.close();
    
            
}
public  void updateFailedScriptLineStatus(FetchMetadataVO fetchMetadataVO,FetchConfigVO fetchConfigVO,String test_script_param_id,String status,String error_message) throws ClassNotFoundException, SQLException {
    
    Class.forName("oracle.jdbc.driver.OracleDriver");
    Connection conn = DriverManager.getConnection(fetchConfigVO.getDb_host(), fetchConfigVO.getDb_username(),
            "Winfo_123");
    Statement st = conn.createStatement();
    
    String sqlQuery="Update WATS_PROD.WIN_TA_TEST_SET_SCRIPT_PARAM  SET LINE_EXECUTION_STATUS='Fail',LINE_ERROR_MESSAGE='"+error_message+"' where TEST_SCRIPT_PARAM_ID='"+test_script_param_id+"'";
    st.executeQuery(sqlQuery);
    
    conn.close();
    
            
}
public  void updateInProgressScriptLineStatus(FetchMetadataVO fetchMetadataVO,FetchConfigVO fetchConfigVO,String test_script_param_id,String status) throws ClassNotFoundException, SQLException {
    
    Class.forName("oracle.jdbc.driver.OracleDriver");
    Connection conn = DriverManager.getConnection(fetchConfigVO.getDb_host(), fetchConfigVO.getDb_username(),
            "Winfo_123");
    Statement st = conn.createStatement();
    
    String sqlQuery="Update WATS_PROD.WIN_TA_TEST_SET_SCRIPT_PARAM  SET LINE_EXECUTION_STATUS='In-Progress' where TEST_SCRIPT_PARAM_ID='"+test_script_param_id+"'";
    st.executeQuery(sqlQuery);
    
    conn.close();
    
            
}
public void updateStartTime(FetchConfigVO fetchConfigVO,String line_id, String test_set_id) throws ClassNotFoundException, SQLException{
    //System.out.println("Start Method");
    Class.forName("oracle.jdbc.driver.OracleDriver");
    Connection conn = DriverManager.getConnection(fetchConfigVO.getDb_host(), fetchConfigVO.getDb_username(),
            "Winfo_123");
    Statement st = conn.createStatement();
    //DateFormat format = new SimpleDateFormat("MM/DD/YYYY HH24:MI:SS");
    Format startformat=new SimpleDateFormat("M/dd/yyyy HH:mm:ss");
    Date start_time1=fetchConfigVO.getStarttime();
    String start_time= startformat.format(start_time1);
    //System.out.println(start_time);
    String sqlQuery="Update WATS_PROD.WIN_TA_TEST_SET_LINES  SET EXECUTION_START_TIME=TO_TIMESTAMP('"+start_time+"','MM/DD/YYYY HH24:MI:SS') WHERE TEST_SET_ID="+test_set_id+" AND TEST_SET_LINE_ID = "+line_id;
    
    st.executeQuery(sqlQuery);
    
    conn.close();
}

 

public void updateEndTime(FetchConfigVO fetchConfigVO,String line_id,String test_set_id) throws ClassNotFoundException, SQLException{
    Class.forName("oracle.jdbc.driver.OracleDriver");
    Connection conn = DriverManager.getConnection(fetchConfigVO.getDb_host(), fetchConfigVO.getDb_username(),
            "Winfo_123");
    Statement st = conn.createStatement();
    Format startformat=new SimpleDateFormat("M/dd/yyyy HH:mm:ss");
    Date end_time1=fetchConfigVO.getEndtime();
    String end_time= startformat.format(end_time1);
    String sqlQuery="Update WATS_PROD.WIN_TA_TEST_SET_LINES  SET EXECUTION_END_TIME=TO_TIMESTAMP('"+end_time+"','MM/DD/YYYY HH24:MI:SS') WHERE  TEST_SET_ID="+test_set_id+" AND TEST_SET_LINE_ID ="+line_id;
    st.executeQuery(sqlQuery);
    
    conn.close();

 

}

 

}