package com.winfo.model;

import java.util.ArrayList;
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
@Table(name = "WIN_TA_TEST_SET_LINES_BKP3")
public class ScriptsData {
	@Id
	@GeneratedValue
	@Column(name = "TEST_SET_LINE_ID")
	private int testsetlineid;

	@Column(name = "SCRIPT_ID")
	private int scriptid;
	
	@Column(name = "SCRIPT_NUMBER")
	private String scriptnumber;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "ENABLED")
	private String enabled;
	

	@Column(name = "SEQ_NUM")
	private String seqnum;
	
//	@Column(name = "TEST_SET_ID")
//	private int testsetid;
//	
	@Column(name = "CREATED_BY")
	private String createdby;
	
	@Column(name = "CREATION_DATE")
	private String creationdate;
	
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "TEST_SET_ID")
	private Testrundata Testrundata;
	
	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "Scriptsdata")

	private List<ScritplinesData> Scriptslinedata = new ArrayList<ScritplinesData>();
	

	public List<ScritplinesData> getScriptslinedata() {
		return Scriptslinedata;
	}

	public void setScriptslinedata(List<ScritplinesData> scriptslinedata) {
		Scriptslinedata = scriptslinedata;
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

	public String getCreationdate() {
		return creationdate;
	}

	public void setCreationdate(String creationdate) {
		this.creationdate = creationdate;
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

	public String getSeqnum() {
		return seqnum;
	}

	public void setSeqnum(String seqnum) {
		this.seqnum = seqnum;
	}


	
}
