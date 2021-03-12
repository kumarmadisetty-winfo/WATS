package com.winfo.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CopytestrunVo {
private int  testScriptNo;
	private int project ;
	private int configuration ;
	private String newtestrunname;
	private String created_by;
	private String increment_value;

	@JsonFormat(pattern = "MM/dd/yyyy")
	private Date creation_date;
	 @JsonProperty("request_type")
	 private String requesttype;
	

	public String getRequesttype() {
		return requesttype;
	}

	public void setRequesttype(String requesttype) {
		this.requesttype = requesttype;
	}
	 public String getIncrement_value() {
		return increment_value;
	}

	public void setIncrement_value(String increment_value) {
		this.increment_value = increment_value;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}



	public Date getCreation_date() {
		return creation_date;
	}

	public void setCreation_date(Date creation_date) {
		this.creation_date = creation_date;
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
