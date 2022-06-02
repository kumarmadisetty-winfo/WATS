package com.winfo.vo;

public class EmailParamDto {
	
	private String receiver;
	
	private String ccPerson;
	
	private Integer requestCount;
	
	private Integer passCount;
	
	private Integer failCount;
	
	private String testSetName;
	
	private String executedBy;

	public String getExecutedBy() {
		return executedBy;
	}

	public void setExecutedBy(String executedBy) {
		this.executedBy = executedBy;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getCcPerson() {
		return ccPerson;
	}

	public void setCcPerson(String ccPerson) {
		this.ccPerson = ccPerson;
	}

	public Integer getRequestCount() {
		return requestCount;
	}

	public void setRequestCount(Integer requestCount) {
		this.requestCount = requestCount;
	}

	public Integer getPassCount() {
		return passCount;
	}

	public void setPassCount(Integer passCount) {
		this.passCount = passCount;
	}

	public Integer getFailCount() {
		return failCount;
	}

	public void setFailCount(Integer failCount) {
		this.failCount = failCount;
	}

	public String getTestSetName() {
		return testSetName;
	}

	public void setTestSetName(String testSetName) {
		this.testSetName = testSetName;
	}
	

}
