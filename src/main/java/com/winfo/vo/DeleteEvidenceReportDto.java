package com.winfo.vo;

import java.util.List;

public class DeleteEvidenceReportDto {

	private String testSetId;
	private List<String> testSetLineId;
	private boolean flag;

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

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

}
