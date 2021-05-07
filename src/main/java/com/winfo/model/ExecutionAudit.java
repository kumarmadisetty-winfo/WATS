package com.winfo.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WIN_TA_EXECUTION_AUDIT")
public class ExecutionAudit {
	@Id
	@Column(name = "EXECUTION_AUDIT_ID")
	private String executionAuditId;

	@Column(name = "TEST_SET_ID")
	private String testsetid;

	@Column(name = "SCRIPT_ID")
	private String scriptid;

	@Column(name = "SCRIPT_NUMBER")
	private String scriptnumber;

	@Column(name = "EXECUTION_Start_Time")
	private String executionstarttime;

	@Column(name = "EXECUTION_END_TIME")
	private Date executionendtime;

	@Column(name = "STATUS")
	private String status;

	
	public String getExecutionAuditId() {
		return executionAuditId;
	}

	public void setExecutionAuditId(String executionAuditId) {
		this.executionAuditId = executionAuditId;
	}

	public String getTestsetid() {
		return testsetid;
	}

	public void setTestsetid(String testsetid) {
		this.testsetid = testsetid;
	}

	public String getScriptid() {
		return scriptid;
	}

	public void setScriptid(String scriptid) {
		this.scriptid = scriptid;
	}

	public String getScriptnumber() {
		return scriptnumber;
	}

	public void setScriptnumber(String scriptnumber) {
		this.scriptnumber = scriptnumber;
	}



	public String getExecutionstarttime() {
		return executionstarttime;
	}

	public void setExecutionstarttime(String executionstarttime) {
		this.executionstarttime = executionstarttime;
	}

	

	public Date getExecutionendtime() {
		return executionendtime;
	}

	public void setExecutionendtime(Date executionendtime) {
		this.executionendtime = executionendtime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
