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
@Table(name = "WIN_TA_TEST_SET_LINES_BKP")
public class ScriptsData {
	@Id
	@GeneratedValue
	@Column(name = "TEST_SET_LINE_ID")
	private String testsetlineid;
	
	public List<ScritplinesData> getScriptslinedata() {
		return Scriptslinedata;
	}

	public void setScriptslinedata(List<ScritplinesData> scriptslinedata) {
		Scriptslinedata = scriptslinedata;
	}


	@Column(name = "SCRIPT_ID")
	private String scriptid;
	
	@Column(name = "SCRIPT_NUMBER")
	private String scriptnumber;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "ENABLED")
	private String enabled;
	

	@Column(name = "SEQ_NUM")
	private String seqnum;

	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "TEST_SET_ID")
	private Testrundata Testrundata;
	
	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "Scriptsdata")

	private List<ScritplinesData> Scriptslinedata = new ArrayList<ScritplinesData>();
	
	public Testrundata getTestrundata() {
		return Testrundata;
	}

	public void setTestrundata(Testrundata testrundata) {
		Testrundata = testrundata;
	}

	public String getTestsetlineid() {
		return testsetlineid;
	}

	public void setTestsetlineid(String testsetlineid) {
		this.testsetlineid = testsetlineid;
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
	
	
	@Override
	public String toString() {
		return "ScriptsData [testsetlineid=" + testsetlineid + ", scriptid=" + scriptid + ", scriptnumber="
				+ scriptnumber + ", status=" + status + ", enabled=" + enabled + ", seqnum=" + seqnum + ", Testrundata="
				+ Testrundata + ", Scriptslinedata=" + Scriptslinedata + "]";
	}

	
}
