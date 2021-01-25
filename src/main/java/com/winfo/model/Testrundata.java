package com.winfo.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="WIN_TA_TEST_SET_BKP")
public class Testrundata {
	@Id
	@GeneratedValue
	@Column(name = "TEST_SET_ID")
	private String testsetid;
	

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

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "Testrundata")

	private List<ScriptsData> Scriptsdata = new ArrayList<ScriptsData>();


	public List<ScriptsData> getScriptsdata() {
		return Scriptsdata;
	}

	public void setScriptsdata(List<ScriptsData> scriptsdata) {
		Scriptsdata = scriptsdata;
	}

	public String getTestsetid() {
		return testsetid;
	}

	public void setTestsetid(String testsetid) {
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
	@Override
	public String toString() {
		return "Testrundata [testsetid=" + testsetid + ", test_set_desc=" + test_set_desc + ", test_set_comments="
				+ test_set_comments + ", enabled=" + enabled + ", description=" + description + ", effective_from="
				+ effective_from + ", effective_to=" + effective_to + ", Scriptsdata=" + Scriptsdata + "]";
	}

}
