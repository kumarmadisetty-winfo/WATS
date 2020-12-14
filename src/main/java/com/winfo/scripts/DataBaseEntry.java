package com.winfo.scripts;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.winfo.services.FetchConfigVO;
import com.winfo.services.FetchMetadataVO;

public class DataBaseEntry {


public  void updatePassedScriptLineStatus(FetchMetadataVO fetchMetadataVO,FetchConfigVO fetchConfigVO,String test_script_param_id, String status) throws ClassNotFoundException, SQLException {
	
	Class.forName("oracle.jdbc.driver.OracleDriver");
	Connection conn = DriverManager.getConnection(fetchConfigVO.getDb_host(), fetchConfigVO.getDb_username(),
			"Winfo_123");
	Statement st = conn.createStatement();
	
	String sqlQuery="Update WATS_PROD.WIN_TA_TEST_SET_SCRIPT_PARAM  set LINE_EXECUTION_STATUS='Pass' where TEST_SCRIPT_PARAM_ID=test_script_param_id";
	st.executeQuery(sqlQuery);
	
	conn.close();
	
			
}
public  void updateFailedScriptLineStatus(FetchMetadataVO fetchMetadataVO,FetchConfigVO fetchConfigVO,String test_script_param_id,String status,String error_message) throws ClassNotFoundException, SQLException {
	
	Class.forName("oracle.jdbc.driver.OracleDriver");
	Connection conn = DriverManager.getConnection(fetchConfigVO.getDb_host(), fetchConfigVO.getDb_username(),
			"Winfo_123");
	Statement st = conn.createStatement();
	
	String sqlQuery="Update WATS_PROD.WIN_TA_TEST_SET_SCRIPT_PARAM  set LINE_EXECUTION_STATUS='Fail',LINE_ERROR_MESSAGE="+error_message+" where TEST_SCRIPT_PARAM_ID=test_script_param_id";
	st.executeQuery(sqlQuery);
	
	conn.close();
	
			
}

}
