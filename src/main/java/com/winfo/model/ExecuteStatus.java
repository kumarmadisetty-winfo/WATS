package com.winfo.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "EXECUTE_STATUS")
public class ExecuteStatus {
	
	@EmbeddedId
	private ExecuteStatusPK executeStatusPK;	
	
	@Column(name = "TEST_RUN_NAME")
	private String testRunName;
	
	@Column(name = "STATUS_FLAG")
	private Character flag;
	
	@Column(name = "EXECUTION_DATE")
	private Date executionDate;
	
	
	@Column(name = "EXECUTE_STATUS")
	private Integer executionStatus;
	
	public ExecuteStatusPK getExecuteStatusPK() {
		return executeStatusPK;
	}

	public void setExecuteStatusPK(ExecuteStatusPK executeStatusPK) {
		this.executeStatusPK = executeStatusPK;
	}

	public String getTestRunName() {
		return testRunName;
	}

	public void setTestRunName(String testRunName) {
		this.testRunName = testRunName;
	}

	public Character getFlag() {
		return flag;
	}

	public void setFlag(Character flag) {
		this.flag = flag;
	}

	public Date getExecutionDate() {
		return executionDate;
	}

	public void setExecutionDate(Date executionDate) {
		this.executionDate = executionDate;
	}

	public Integer getExecutionStatus() {
		return executionStatus;
	}

	public void setExecutionStatus(Integer executionStatus) {
		this.executionStatus = executionStatus;
	}
	
}
