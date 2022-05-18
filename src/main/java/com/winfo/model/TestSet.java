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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;


@Entity
@Table(name = "WIN_TA_TEST_SET")

public class TestSet {
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "testRun_generator")
	@SequenceGenerator(name = "testRun_generator", sequenceName = "WIN_TA_TEST_SET_ID_SEQ", allocationSize = 1)
	@Id
	@Column(name = "TEST_SET_ID", nullable = false)
	private Integer testRunId;
	
	@NotEmpty(message="Test_SET_Name can not be null")
	@Column(name = "TEST_SET_NAME")
	private String  testRunName;

	@Column(name = "TEST_SET_DESC")
	private String  testRunDesc;
	
	@Column(name = "TEST_SET_COMMENTS")
	private String  testRunComments;
	
	@Column(name = "ENABLED")
	private String  enabled;
	
	@Column(name = "DESCRIPTION")
	private String  description;
	
	@Column(name = "EFFECTIVE_FROM")
	@Temporal(TemporalType.DATE)
	private Date  effectiveFrom;
	
	@Column(name = "EFFECTIVE_TO")
	@Temporal(TemporalType.DATE)
	private Date  effectiveTo;
	
	@Column(name = "PROJECT_ID")
	private Integer  projectId;
	
	@Column(name = "CONFIGURATION_ID")
	private Integer  configurationId;
	
	@Column(name = "CREATED_BY")
	private String  createdBy;

	@Column(name = "LAST_UPDATED_BY")
	private String  lastUpdatedBy;
	
	@Column(name = "CREATION_DATE")
	@Temporal(TemporalType.DATE)
	private Date  creationDate;
	
	@Column(name = "UPDATE_DATE")
	@Temporal(TemporalType.DATE)
	private Date  updateDate;
	
	@Column(name = "LAST_EXECUTED_BY")
	@Temporal(TemporalType.DATE)
	private Date  lastExecutBy;
	
	@Column(name = "TS_COMPLETE_FLAG")
	private String  tsCompleteFlag;
	
	@Column(name = "PASS_PATH")
	private String  passPath;
	
	@Column(name = "FAIL_PATH")
	private String  failPath;
	
	@Column(name = "EXCEPTION_PATH")
	private String  exceptionPath;
	
	@Column(name = "TR_MODE")
	private String  testRunMode;
	
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "testRun")

	private List<TestSetLines> testRunScriptDatalist = new ArrayList<TestSetLines>();
	
	public List<TestSetLines> getTestRunScriptDatalist() {
		return testRunScriptDatalist;
	}

	public void setTestRunScriptDatalist(List<TestSetLines> testRunScriptDatalist) {
		this.testRunScriptDatalist = testRunScriptDatalist;
	}

	public void addTestRunScriptData(TestSetLines testRunScript) {
		testRunScriptDatalist.add(testRunScript);
		testRunScript.setTestRun(this);
	}

	public Integer getTestRunId() {
		return testRunId;
	}

	public void setTestRunId(Integer testRunId) {
		this.testRunId = testRunId;
	}

	public String getTestRunName() {
		return testRunName;
	}

	public void setTestRunName(String testRunName) {
		this.testRunName = testRunName;
	}

	public String getTestRunDesc() {
		return testRunDesc;
	}

	public void setTestRunDesc(String testRunDesc) {
		this.testRunDesc = testRunDesc;
	}

	public String getTestRunComments() {
		return testRunComments;
	}

	public void setTestRunComments(String testRunComments) {
		this.testRunComments = testRunComments;
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

	public Date getEffectiveFrom() {
		return effectiveFrom;
	}

	public void setEffectiveFrom(Date effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}

	public Date getEffectiveTo() {
		return effectiveTo;
	}

	public void setEffectiveTo(Date effectiveTo) {
		this.effectiveTo = effectiveTo;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getConfigurationId() {
		return configurationId;
	}

	public void setConfigurationId(Integer configurationId) {
		this.configurationId = configurationId;
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

	public Date getLastExecutBy() {
		return lastExecutBy;
	}

	public void setLastExecutBy(Date lastExecutBy) {
		this.lastExecutBy = lastExecutBy;
	}

	public String getTsCompleteFlag() {
		return tsCompleteFlag;
	}

	public void setTsCompleteFlag(String tsCompleteFlag) {
		this.tsCompleteFlag = tsCompleteFlag;
	}

	public String getPassPath() {
		return passPath;
	}

	public void setPassPath(String passPath) {
		this.passPath = passPath;
	}

	public String getFailPath() {
		return failPath;
	}

	public void setFailPath(String failPath) {
		this.failPath = failPath;
	}

	public String getExceptionPath() {
		return exceptionPath;
	}

	public void setExceptionPath(String exceptionPath) {
		this.exceptionPath = exceptionPath;
	}

	public String getTestRunMode() {
		return testRunMode;
	}

	public void setTestRunMode(String testRunMode) {
		this.testRunMode = testRunMode;
	}

}
