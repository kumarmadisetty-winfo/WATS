package com.winfo.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WIN_TA_EXECUTION_AUDIT")
public class ExecutionAudit {
	@Id
	@Column(name = "EXECUTION_AUDIT_ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private int executionAuditId;

	@Column(name = "TEST_SET_ID")
	private String testSetId;

	@Column(name = "SCRIPT_ID")
	private String scriptId;

	@Column(name = "SCRIPT_NUMBER")
	private String scriptNumber;

	@Column(name = "EXECUTION_Start_Time")
	private Date executionStartTime;

	@Column(name = "EXECUTION_END_TIME")
	private Date executionEndTime;

	@Column(name = "STATUS")
	private String status;

	
	public int getExecutionAuditId() {
		return executionAuditId;
	}

	public void setExecutionAuditId(int executionAuditId) {
		this.executionAuditId = executionAuditId;
	}

	public String getTestsetid() {
		return testSetId;
	}

	public void setTestsetid(String testsetid) {
		this.testSetId = testsetid;
	}

	public String getScriptid() {
		return scriptId;
	}

	public void setScriptid(String scriptid) {
		this.scriptId = scriptid;
	}

	public String getScriptnumber() {
		return scriptNumber;
	}

	public void setScriptnumber(String scriptnumber) {
		this.scriptNumber = scriptnumber;
	}



	public Date getExecutionstarttime() {
		return executionStartTime;
	}

	public void setExecutionstarttime(Date executionstarttime) {
		this.executionStartTime = executionstarttime;
	}

	

	public Date getExecutionendtime() {
		return executionEndTime;
	}

	public void setExecutionendtime(Date executionendtime) {
		this.executionEndTime = executionendtime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
