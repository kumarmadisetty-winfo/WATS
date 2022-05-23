package com.winfo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;


@Entity
@Table(name = "WIN_TA_TEST_SET_LINES")

public class TestSetLine {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "testRunScript_generator")
	@SequenceGenerator(name = "testRunScript_generator", sequenceName = "WIN_TA_TEST_SET_LINE_SEQ", allocationSize = 1)
	@Column(name = "TEST_SET_LINE_ID", nullable = false)
	private Integer testRunScriptId;

	@Column(name ="SCRIPT_ID")

	private Integer scriptId;

	@Column(name = "SCRIPT_NUMBER")
	@NotEmpty(message="Script Number can not null")
	private String scriptNumber;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "ENABLED")
	private String enabled;

	@Column(name = "SEQ_NUM")
	private Integer seqNum;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@Column(name = "CREATION_DATE")
	@Temporal(TemporalType.DATE)
	private Date creationDate;

	@Column(name = "UPDATE_DATE")
	@Temporal(TemporalType.DATE)
	private Date updateDate;

	@Column(name = "TEST_SET_LINE_SCRIPT_PATH")
	private String testRunScriptPath;

	@Column(name = "EXECUTED_BY")
	private String executedBy;

	@Column(name = "EXECUTION_START_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date executionStartTime;

	@Column(name = "EXECUTION_END_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date executionEndTime;

	@Column(name = "ISSUE_KEY")
	private String issueKey;
	
	@Column(name = "RUN_COUNT")
	private Integer runCount;
	
	@Column(name = "SCRIPT_UPDATED")
	private String scriptUpadated;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)

	@JoinColumn(name = "TEST_SET_ID" ,nullable = false)
	private TestSet testRun;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "testSetLines")
	private List<TestSetScriptParam> testRunScriptParam = new ArrayList<TestSetScriptParam>();

	public void addTestScriptParam(TestSetScriptParam scriptParam) {
		testRunScriptParam.add(scriptParam);
		scriptParam.setTestRunScripts(this);
	}

	public Integer getTestRunScriptId() {
		return testRunScriptId;
	}

	public void setTestRunScriptId(Integer testRunScriptId) {
		this.testRunScriptId = testRunScriptId;
	}

	public Integer getScriptId() {
		return scriptId;
	}

	public void setScriptId(Integer scriptId) {
		this.scriptId = scriptId;
	}

	public String getScriptNumber() {
		return scriptNumber;
	}

	public void setScriptNumber(String scriptNumber) {
		this.scriptNumber = scriptNumber;
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

	public Integer getSeqNum() {
		return seqNum;
	}

	public void setSeqNum(Integer seqNum) {
		this.seqNum = seqNum;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getTestRunScriptPath() {
		return testRunScriptPath;
	}

	public void setTestRunScriptPath(String testRunScriptPath) {
		this.testRunScriptPath = testRunScriptPath;
	}

	public String getExecutedBy() {
		return executedBy;
	}

	public void setExecutedBy(String executedBy) {
		this.executedBy = executedBy;
	}

	public Date getExecutionStartTime() {
		return executionStartTime;
	}

	public void setExecutionStartTime(Date executionStartTime) {
		this.executionStartTime = executionStartTime;
	}

	public Date getExecutionEndTime() {
		return executionEndTime;
	}

	public void setExecutionEndTime(Date executionEndTime) {
		this.executionEndTime = executionEndTime;
	}

	public String getIssueKey() {
		return issueKey;
	}

	public void setIssueKey(String issueKey) {
		this.issueKey = issueKey;
	}

	public TestSet getTestRun() {
		return testRun;
	}

	public void setTestRun(TestSet testRun) {
		this.testRun = testRun;
	}

	public List<TestSetScriptParam> getTestRunScriptParam() {
		return testRunScriptParam;
	}

	public void setTestRunScriptParam(List<TestSetScriptParam> testRunScriptParam) {
		this.testRunScriptParam = testRunScriptParam;
	}

	public Integer getRunCount() {
		return runCount;
	}

	public void setRunCount(Integer runCount) {
		this.runCount = runCount;
	}

	public String getScriptUpadated() {
		return scriptUpadated;
	}
	public void setScriptUpadated(String scriptUpadated) {
		this.scriptUpadated = scriptUpadated;
	}
		

}
