package com.winfo.config;

public class DataBaseConfiguration {

	private String dbConnection = "jdbc:oracle:thin:@";
	private String dbhost = "192.168.1.243";
	private String dbport = "1522";
	private String dbName = "apex19c";
	private String dbschemaName = "APEX_AUTOMATION";
	private String dbschemaPsw = "APEX_AUTOMATION";
	private String configport = "8080";
	private String configschemaName = "apex";
	private String configschemaPsw = "wats_tst";
	
	
	public String getConfigport() {
		return configport;
	}
	public void setConfigport(String configport) {
		this.configport = configport;
	}
	public String getConfigschemaName() {
		return configschemaName;
	}
	public void setConfigschemaName(String configschemaName) {
		this.configschemaName = configschemaName;
	}
	public String getConfigschemaPsw() {
		return configschemaPsw;
	}
	public void setConfigschemaPsw(String configschemaPsw) {
		this.configschemaPsw = configschemaPsw;
	}
	public String getDbConnection() {
		return dbConnection;
	}
	public void setDbConnection(String dbConnection) {
		this.dbConnection = dbConnection;
	}
	public String getDbhost() {
		return dbhost;
	}
	public void setDbhost(String dbhost) {
		this.dbhost = dbhost;
	}
	public String getDbport() {
		return dbport;
	}
	public void setDbport(String dbport) {
		this.dbport = dbport;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getDbschemaName() {
		return dbschemaName;
	}
	public void setDbschemaName(String dbschemaName) {
		this.dbschemaName = dbschemaName;
	}
	public String getDbschemaPsw() {
		return dbschemaPsw;
	}
	public void setDbschemaPsw(String dbschemaPsw) {
		this.dbschemaPsw = dbschemaPsw;
	}
	
	
	
	
	
}
