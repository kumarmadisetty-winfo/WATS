package com.winfo.vo;

import java.util.List;

public class DeleteEvidenceReportDto {

	private String testSetId;
	private List<String> testSetLineId;
	private boolean isTestRunDelete;
	
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
	public boolean getIsTestRunDelete() {
		return isTestRunDelete;
	}
	public void setIsTestRunDelete(boolean isTestRunDelete) {
		this.isTestRunDelete = isTestRunDelete;
	}
}
