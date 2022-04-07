package com.winfo.vo;

public class PyJabKafkaDto {
	private String testSetId;
	private String testSetLineId;
	private String scriptPath;
	
	public PyJabKafkaDto(String testSetId, String testSetLineId, String scriptPath) {
		super();
		this.testSetId = testSetId;
		this.testSetLineId = testSetLineId;
		this.scriptPath = scriptPath;
	}

	public String getTestSetId() {
		return testSetId;
	}

	public void setTestSetId(String testSetId) {
		this.testSetId = testSetId;
	}

	public String getTestSetLineId() {
		return testSetLineId;
	}

	public void setTestSetLineId(String testSetLineId) {
		this.testSetLineId = testSetLineId;
	}

	public String getScriptPath() {
		return scriptPath;
	}

	public void setScriptPath(String scriptPath) {
		this.scriptPath = scriptPath;
	}
}
