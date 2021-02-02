package com.winfo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CopytestrunVo {
private int  testScriptNo;
	private int project ;
	private int configuration ;
	private String newtestrunname;
	

	

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
