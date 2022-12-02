package com.winfo.vo;

public class ExistTestRunDto {
	private Integer testSetId;
	private boolean forceMigrate = false;
	
	public Integer getTestSetId() {
		return testSetId;
	}
	public void setTestSetId(Integer testSetId) {
		this.testSetId = testSetId;
	}
	public boolean isForceMigrate() {
		return forceMigrate;
	}
	public void setForceMigrate(boolean forceMigrate) {
		this.forceMigrate = forceMigrate;
	}
	
	
}
