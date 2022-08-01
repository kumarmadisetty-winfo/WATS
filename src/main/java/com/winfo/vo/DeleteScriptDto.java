package com.winfo.vo;

import java.util.List;

public class DeleteScriptDto {

	private String testSetId = "false";
	private List<String> testSetLineId;

	public String getTestSetId() {
		return testSetId;
	}

	public void setTestSetId(String testSetId) {
		this.testSetId = testSetId;
	}

	public List<String> getTestSetLineId() {
		return testSetLineId;
	}

	public void setTestSetLineId(List<String> testSetLineId) {
		this.testSetLineId = testSetLineId;
	}

	

}
