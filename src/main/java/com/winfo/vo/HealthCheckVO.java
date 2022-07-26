package com.winfo.vo;
public class HealthCheckVO {
	private String database;
	private String seleniumGrid;
	private String ObjectStoreAccess;
	private String centralRepo;

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getSeleniumGrid() {
		return seleniumGrid;
	}

	public void setSeleniumGrid(String seleniumGrid) {
		this.seleniumGrid = seleniumGrid;
	}

	public String getObjectStoreAccess() {
		return ObjectStoreAccess;
	}

	public void setObjectStoreAccess(String objectStoreAccess) {
		ObjectStoreAccess = objectStoreAccess;
	}

	public String getCentralRepo() {
		return centralRepo;
	}

	public void setCentralRepo(String centralRepo) {
		this.centralRepo = centralRepo;
	}

}