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

public class TestSetLines {
	

	@Id
	@GeneratedValue
	@Column(name = "TEST_SET_LINE_ID")
	private Integer test_set_line_id;

	@Column(name = "SCRIPT_ID")
	private Integer script_id;
	
//	@Column(name = "TEST_SET_ID")
//	private Integer test_set_id;
	
	@Column(name = "SCRIPT_NUMBER")
	private String script_number;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "ENABLED")
	private String enabled;
	

	@Column(name = "SEQ_NUM")
	private Integer seq_num;
	
	@Column(name = "CREATED_BY")
	private String created_by;
	
	@Column(name = "LAST_UPDATED_BY")
	private String last_updated_by;
	
	@Column(name = "CREATION_DATE")
	private Date creation_date;
	
	@Column(name = "UPDATE_DATE")
	private Date update_date;
	
	@Column(name = "TEST_SET_LINE_SCRIPT_PATH")
	private String test_set_line_script_path;
	
	@Column(name = "EXECUTED_BY")
	private String executed_by;
	
	@Column(name = "EXECUTION_START_TIME")
	private Date execution_start_time;
	
	@Column(name = "EXECUTION_END_TIME")
	private Date execution_end_time;
	
	@Column(name = "ISSUE_KEY")
	private String issue_key;
	
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "TEST_SET_ID")

	private TestSet testSet;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "testSetLines")
	
	private List<TestSetScriptParam> testSetScriptParam = new ArrayList<TestSetScriptParam>();
	
	
	 public void addTestScriptParam(TestSetScriptParam setScriptParam) {
		 testSetScriptParam.add(setScriptParam);
		 setScriptParam.setTestSetLines(this);
	    }


	public Integer getTest_set_line_id() {
		return test_set_line_id;
	}

	public void setTest_set_line_id(Integer test_set_line_id) {
		this.test_set_line_id = test_set_line_id;
	}

	public Integer getScript_id() {
		return script_id;
	}

	public void setScript_id(Integer script_id) {
		this.script_id = script_id;
	}

	
	public String getScript_number() {
		return script_number;
	}

	public void setScript_number(String script_number) {
		this.script_number = script_number;
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

	public Integer getSeq_num() {
		return seq_num;
	}

	public void setSeq_num(Integer seq_num) {
		this.seq_num = seq_num;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public String getLast_updated_by() {
		return last_updated_by;
	}

	public void setLast_updated_by(String last_updated_by) {
		this.last_updated_by = last_updated_by;
	}

	public Date getCreation_date() {
		return creation_date;
	}

	public void setCreation_date(Date creation_date) {
		this.creation_date = creation_date;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	public String getTest_set_line_script_path() {
		return test_set_line_script_path;
	}

	public void setTest_set_line_script_path(String test_set_line_script_path) {
		this.test_set_line_script_path = test_set_line_script_path;
	}

	public String getExecuted_by() {
		return executed_by;
	}

	public void setExecuted_by(String executed_by) {
		this.executed_by = executed_by;
	}

	public Date getExecution_start_time() {
		return execution_start_time;
	}

	public void setExecution_start_time(Date execution_start_time) {
		this.execution_start_time = execution_start_time;
	}

	public Date getExecution_end_time() {
		return execution_end_time;
	}

	public void setExecution_end_time(Date execution_end_time) {
		this.execution_end_time = execution_end_time;
	}


	public TestSet getTestSet() {
		return testSet;
	}

	public void setTestSet(TestSet testSet) {
		this.testSet = testSet;
	}

	
	

	
	

	public String getIssue_key() {
		return issue_key;
	}


	public void setIssue_key(String issue_key) {
		this.issue_key = issue_key;
	}


	public List<TestSetScriptParam> getTestSetScriptParam() {
		return testSetScriptParam;
	}


	public void setTestSetScriptParam(List<TestSetScriptParam> testSetScriptParam) {
		this.testSetScriptParam = testSetScriptParam;
	}

		

}
