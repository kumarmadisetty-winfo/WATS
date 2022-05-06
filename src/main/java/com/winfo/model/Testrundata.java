package com.winfo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="WIN_TA_TEST_SET")
public class Testrundata {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "testRun_generator")
	@SequenceGenerator(name = "testRun_generator", sequenceName = "WIN_TA_TEST_SET_ID_SEQ", allocationSize = 1)
	@Column(name = "TEST_SET_ID")
	private int testsetid;
	

	@Column(name = "TEST_SET_DESC")
	private String  test_set_desc;
	
	@Column(name = "TEST_SET_COMMENTS")
	private String  test_set_comments;
	
	@Column(name = "ENABLED")
	private String  enabled;
	
	@Column(name = "DESCRIPTION")
	private String  description;
	
	@Column(name = "EFFECTIVE_FROM")
	private String  effective_from;
	
	@Column(name = "EFFECTIVE_TO")
	private String  effective_to;
	
	@Column(name = "PROJECT_ID")
	private int  projectid;
	
	@Column(name = "TEST_SET_NAME")
	private String  testsetname;
	
	@Column(name = "CONFIGURATION_ID")
	private int  configurationid;
	
	@Column(name = "CREATED_BY")
	private String  createdby;

	@Column(name = "LAST_UPDATED_BY")
	private String  lastupdatedby;
	
	@Column(name = "CREATION_DATE")
	private Date  creationdate;
	
	@Column(name = "UPDATE_DATE")
	private Date  updatedate;
	
	@Column(name = "LAST_EXECUTED_BY")
	private Date  lastexecuteby;
	
	@Column(name = "TS_COMPLETE_FLAG")
	private String  tscompleteflag;
	
	@Column(name = "PASS_PATH")
	private String  passpath;
	
	@Column(name = "FAIL_PATH")
	private String  failpath;
	
	@Column(name = "EXCEPTION_PATH")
	private String  exceptionpath;
	
	@Column(name = "TR_MODE")
	private String  trmode;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "Testrundata")

	private List<ScriptsData> Scriptsdata = new ArrayList<ScriptsData>();

	public void addScriptsdata(ScriptsData scriptsdata) {
		Scriptsdata.add(scriptsdata);
		scriptsdata.setTestrundata(this);
	}
	

	public String getTestsetname() {
		return testsetname;
	}

	public void setTestsetname(String testsetname) {
		this.testsetname = testsetname;
	}

	

	public int getProjectid() {
		return projectid;
	}


	public void setProjectid(int projectid) {
		this.projectid = projectid;
	}


	public int getConfigurationid() {
		return configurationid;
	}


	public void setConfigurationid(int configurationid) {
		this.configurationid = configurationid;
	}


	public String getCreatedby() {
		return createdby;
	}


	public void setCreatedby(String createdby) {
		this.createdby = createdby;
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


	public Date getUpdatedate() {
		return updatedate;
	}


	public void setUpdatedate(Date updatedate) {
		this.updatedate = updatedate;
	}


	public Date getLastexecuteby() {
		return lastexecuteby;
	}


	public void setLastexecuteby(Date lastexecuteby) {
		this.lastexecuteby = lastexecuteby;
	}


	public String getTscompleteflag() {
		return tscompleteflag;
	}


	public void setTscompleteflag(String tscompleteflag) {
		this.tscompleteflag = tscompleteflag;
	}


	public String getPasspath() {
		return passpath;
	}


	public void setPasspath(String passpath) {
		this.passpath = passpath;
	}


	public String getFailpath() {
		return failpath;
	}


	public void setFailpath(String failpath) {
		this.failpath = failpath;
	}


	public String getExceptionpath() {
		return exceptionpath;
	}


	public void setExceptionpath(String exceptionpath) {
		this.exceptionpath = exceptionpath;
	}


	public String getTrmode() {
		return trmode;
	}


	public void setTrmode(String trmode) {
		this.trmode = trmode;
	}


	public List<ScriptsData> getScriptsdata() {
		return Scriptsdata;
	}

	public void setScriptsdata(List<ScriptsData> scriptsdata) {
		Scriptsdata = scriptsdata;
	}

	public int getTestsetid() {
		return testsetid;
	}

	public void setTestsetid(int testsetid) {
		this.testsetid = testsetid;
	}

	public String getTest_set_desc() {
		return test_set_desc;
	}

	public void setTest_set_desc(String test_set_desc) {
		this.test_set_desc = test_set_desc;
	}

	public String getTest_set_comments() {
		return test_set_comments;
	}

	public void setTest_set_comments(String test_set_comments) {
		this.test_set_comments = test_set_comments;
	}

	public String getEnabled() {
		return enabled;
	}
	
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEffective_from() {
		return effective_from;
	}

	public void setEffective_from(String effective_from) {
		this.effective_from = effective_from;
	}

	public String getEffective_to() {
		return effective_to;
	}

	public void setEffective_to(String effective_to) {
		this.effective_to = effective_to;
	}


}
