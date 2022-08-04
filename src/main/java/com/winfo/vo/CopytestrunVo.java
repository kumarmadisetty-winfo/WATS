package com.winfo.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CopytestrunVo {
	private int testScriptNo;
	private int project;
	private int configuration;
	private String newtestrunname;
	private String createdBy;
	private String incrementValue;

	@JsonFormat(pattern = "MM/dd/yyyy")
	private Date creationDate;
	@JsonProperty("requestType")
	private String requestType;

	public String getRequestType() {
		return requestType;
	}

	public void setRequesttype(String requestType) {
		this.requestType = requestType;
	}

	public String getIncrementValue() {
		return incrementValue;
	}

	public void setIncrementValue(String incrementValue) {
		this.incrementValue = incrementValue;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreation_date(Date creationDate) {
		this.creationDate = creationDate;
	}

	public int getProject() {
		return project;
	}

	public void setProject(int project) {
		this.project = project;
	}

	public int getConfiguration() {
		return configuration;
	}

	public void setConfiguration(int configuration) {
		this.configuration = configuration;
	}

	public String getNewtestrunname() {
		return newtestrunname;
	}

	public void setNewtestrunname(String newtestrunname) {
		this.newtestrunname = newtestrunname;
	}

	public int getTestScriptNo() {
		return testScriptNo;
	}

	public void setTestScriptNo(int testScriptNo) {
		this.testScriptNo = testScriptNo;
	}

}
