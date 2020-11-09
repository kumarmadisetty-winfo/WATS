package com.winfo.scripts;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.winfo.services.FetchConfigVO;
import com.winfo.services.TestCaseDataService;

public class ConnectToSQL {
	TestCaseDataService web = new TestCaseDataService();
	public String getPassword(String args, String userId, FetchConfigVO fetchConfigVO) throws SQLException, ClassNotFoundException,NullPointerException {

		String password = null;
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
		Class.forName("oracle.jdbc.driver.OracleDriver"); 
//		Connection conn = DriverManager.getConnection(fetchConfigVO.getDb_host(),fetchConfigVO.getDb_username(),fetchConfigVO.getDb_password());
		 conn = DriverManager.getConnection(fetchConfigVO.getDb_host(),fetchConfigVO.getDb_username(),"Winfo_123");
		 st = conn.createStatement();
//		String sqlStr = "SELECT TOOLKIT.DECRYPT(PASSWORD) PASSWORD FROM WIN_TA_CONFIG WHERE CONFIGURATION_ID = (SELECT CONFIGURATION_ID FROM WIN_TA_TEST_SET WHERE TEST_SET_ID = "+args+")";
		String sqlStr = "select WIN_DBMS_CRYPTO.DECRYPT(users.password , users.encrypt_key) PASSWORD from win_ta_test_set test_set,win_ta_config config,win_ta_config_users users where test_set.configuration_id = config.configuration_id and config.configuration_id = users.config_id and test_set.test_set_id = "+args+" and (upper(users.user_name) = upper('"+userId+"') or ('"+userId+"' is null and users.default_user = 'Y')) and rownum = 1";
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