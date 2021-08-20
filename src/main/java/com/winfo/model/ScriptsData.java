package com.winfo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "WIN_TA_TEST_SET_LINES")
public class ScriptsData {
	@Id
	@Column(name = "TEST_SET_LINE_ID")
	private int testsetlineid;

	@Column(name = "SCRIPT_ID")
	private int scriptid;
	
	@Column(name = "SCRIPT_NUMBER")
	private String scriptnumber;
	
	@Column(name = "SCRIPT_UPDATED")
	private String scriptUpadated;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "ENABLED")
	private String enabled;
	

	@Column(name = "SEQ_NUM")
	private int seqnum;
	
//	@Column(name = "TEST_SET_ID")
//	private int testsetid;
//	
	@Column(name = "CREATED_BY")
	private String createdby;
	
	@Column(name = "LAST_UPDATED_BY")
	private String lastupdatedby;
	
	@Column(name = "CREATION_DATE")
	private Date creationdate;
	
	@Column(name = "UPDATE_DATE")
	private Date updateddate;
	
	@Column(name = "TEST_SET_LINE_SCRIPT_PATH")
	private String testsstlinescriptpath;
	
	@Column(name = "EXECUTED_BY")
	private String executedby;
	
	@Column(name = "EXECUTION_START_TIME")
	private Date executionstarttime;
	
	@Column(name = "EXECUTION_END_TIME")
	private Date executionendtime;	
	
	@ManyToOne(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "TEST_SET_ID")
	private Testrundata Testrundata;
	
	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "Scriptsdata")

	private List<ScritplinesData> Scriptslinedata = new ArrayList<ScritplinesData>();
	
    public void addScriptlines(ScritplinesData scriptslinedata) {
    	Scriptslinedata.add(scriptslinedata);
    	scriptslinedata.setScriptsdata(this);
    }
	public List<ScritplinesData> getScriptslinedata() {
		return Scriptslinedata;
	}

	public void setScriptslinedata(List<ScritplinesData> scriptslinedata) {
		Scriptslinedata = scriptslinedata;
	}



	public String getScriptUpadated() {
		return scriptUpadated;
	}
	public void setScriptUpadated(String scriptUpadated) {
		this.scriptUpadated = scriptUpadated;
	}
	public String getScriptnumber() {
		return scriptnumber;
	}

	public int getTestsetlineid() {
		return testsetlineid;
	}

	public void setTestsetlineid(int testsetlineid) {
		this.testsetlineid = testsetlineid;
	}

	public int getScriptid() {
		return scriptid;
	}

	public void setScriptid(int scriptid) {
		this.scriptid = scriptid;
	}

//	public int getTestsetid() {
//		return testsetid;
//	}
//
//	public void setTestsetid(int testsetid) {
//		this.testsetid = testsetid;
//	}

	public String getCreatedby() {
		return createdby;
	}

	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}



	public Testrundata getTestrundata() {
		return Testrundata;
	}

	public void setTestrundata(Testrundata testrundata) {
		Testrundata = testrundata;
	}

	public void setScriptnumber(String scriptnumber) {
		this.scriptnumber = scriptnumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	public int getSeqnum() {
		return seqnum;
	}
	public void setSeqnum(int seqnum) {
		this.seqnum = seqnum;
	}
	public String getLastupdatedby() {
		return lastupdatedby;
	}
	public void setLastupdatedby(String lastupdatedby) {
		this.lastupdatedby = lastupdatedby;
	}
	public Date getCreationdate() {
		return creationdate;
	}
	public void setCreationdate(Date creationdate) {
		this.creationdate = creationdate;
	}
	public Date getUpdateddate() {
		return updateddate;
	}
	public void setUpdateddate(Date updateddate) {
		this.updateddate = updateddate;
	}
	public String getTestsstlinescriptpath() {
		return testsstlinescriptpath;
	}
	public void setTestsstlinescriptpath(String testsstlinescriptpath) {
		this.testsstlinescriptpath = testsstlinescriptpath;
	}
	public String getExecutedby() {
		return executedby;
	}
	public void setExecutedby(String executedby) {
		this.executedby = executedby;
	}
	public Date getExecutionstarttime() {
		return executionstarttime;
	}
	public void setExecutionstarttime(Date executionstarttime) {
		this.executionstarttime = executionstarttime;
	}
	public Date getExecutionendtime() {
		return executionendtime;
	}
	public void setExecutionendtime(Date executionendtime) {
		this.executionendtime = executionendtime;
	}




	
}
