package com.winfo.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ExecuteStatusPK implements Serializable{
	
	private static final long serialVersionUID = 23L;
	
	@Column(name = "EXECUTED_BY")
	private String executedBy;

	@Column(name = "TEST_RUN_ID")
	private Integer testSetId;

	public String getExecutedBy() {
		return executedBy;
	}

	public void setExecutedBy(String executedBy) {
		this.executedBy = executedBy;
	}

	public Integer getTestSetId() {
		return testSetId;
	}

	public void setTestSetId(Integer testSetId) {
		this.testSetId = testSetId;
	}

}
