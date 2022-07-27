package com.winfo.vo;

public class HealthCheckVO {
	private String database;
	private String seleniumGrid;
	private String objectStoreAccess;
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
		return objectStoreAccess;
	}

	public void setObjectStoreAccess(String objectStoreAccess) {
		this.objectStoreAccess = objectStoreAccess;
	}

	public String getCentralRepo() {
		return centralRepo;
	}

	public void setCentralRepo(String centralRepo) {
		this.centralRepo = centralRepo;
	}

}