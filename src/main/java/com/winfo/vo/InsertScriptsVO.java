package com.winfo.vo;

import java.util.List;

public class InsertScriptsVO {

	private int testSetId;
	
	private List<Integer> listOfLineIds;

	public Integer getTestSetId() {
		return testSetId;
	}

	public void setTestSetId(Integer testSetId) {
		this.testSetId = testSetId;
	}

	public List<Integer> getListOfLineIds() {
		return listOfLineIds;
	}

	public void setListOfLineIds(List<Integer> listOfLineIds) {
		this.listOfLineIds = listOfLineIds;
	}
	
}
