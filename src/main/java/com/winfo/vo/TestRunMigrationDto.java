package com.winfo.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.winfo.model.TestSet;

public class TestRunMigrationDto {

	@JsonProperty("projectName")
	private String projectName;

	@JsonProperty("watsPackage")
	private String watsPackage;

	@JsonProperty("customer")
	private String customer;

	@JsonProperty("configurationName")
	private String configurationName;

	@JsonProperty("testSetId")
	private int testSetId;

	@JsonProperty("projectId")
	private int projectId;

	@JsonProperty("description")
	private String description;

	@JsonProperty("testSetDesc")
	private String testSetDesc;

	@JsonProperty("testSetComments")
	private String testSetComments;

	@JsonProperty("enabled")
	private String enabled;

	@JsonProperty("lastExecutedBy")
	private String lastExecutedBy;

	@JsonProperty("tsCompleteFlag")
	private String tsCompleteFlag;

	@JsonProperty("passPath")
	private String passPath;

	@JsonProperty("effectiveTo")
	private Date effectiveTo;

	@JsonProperty("testSetName")
	private String testSetName;

	@JsonProperty("execeptionPath")
	private String execeptionPath;

	@JsonProperty("creationDate")
	private String creationDate;

	@JsonProperty("createdBy")
	private String createdBy;

	@JsonProperty("trMode")
	private String trMode;

	@JsonProperty("updateDate")
	private String updateDate;

	@JsonProperty("lastUpdatedBy")
	private String lastUpdatedBy;

	@JsonProperty("failPath")
	private String failPath;

	@JsonProperty("effectiveFrom")
	private Date effectiveFrom;

	@JsonProperty("configurationId")
	private int configurationId;

	@JsonProperty("scriptMasterData")
	private List<ScriptMasterDto> scriptMasterData = new ArrayList<ScriptMasterDto>();

	@JsonProperty("testSetLinesAndParaData")
	private List<TestSetLineDto> testSetLinesAndParaData = new ArrayList<TestSetLineDto>();

	@JsonProperty("lookUpData")
	private Map<String, LookUpVO> lookUpData;

	@JsonProperty("testRunExists")
	private boolean testRunExists;

	public TestRunMigrationDto() {
	}

	public TestRunMigrationDto(TestSet testRunData) {

		this.configurationId = testRunData.getConfigurationId();
		this.createdBy = testRunData.getCreatedBy();
		this.projectId = testRunData.getProjectId();
		this.testSetName = testRunData.getTestRunName();
		this.testSetDesc = testRunData.getTestRunDesc();
		this.testSetComments = testRunData.getTestRunComments();
		this.enabled = testRunData.getEnabled();
		this.description = testRunData.getDescription();
		this.effectiveFrom = testRunData.getEffectiveFrom();
		this.effectiveTo = testRunData.getEffectiveTo();
		this.tsCompleteFlag = testRunData.getTsCompleteFlag();
		this.passPath = testRunData.getPassPath();
		this.failPath = testRunData.getFailPath();
		this.execeptionPath = testRunData.getExceptionPath();
		this.trMode = testRunData.getTestRunMode();
		this.lastUpdatedBy = null;
		this.creationDate = null;
		this.updateDate = null;
	}

	public String getWatsPackage() {
		return watsPackage;
	}

	public void setWatsPackage(String watsPackage) {
		this.watsPackage = watsPackage;
	}

	public boolean isTestRunExists() {
		return testRunExists;
	}

	public void setTestRunExists(boolean testRunExists) {
		this.testRunExists = testRunExists;
	}

	public Map<String, LookUpVO> getLookUpData() {
		return lookUpData;
	}

	public void setLookUpData(Map<String, LookUpVO> lookUpData) {
		this.lookUpData = lookUpData;
	}

	public List<TestSetLineDto> getTestSetLinesAndParaData() {
		return testSetLinesAndParaData;
	}

	public void setTestSetLinesAndParaData(List<TestSetLineDto> testSetLinesAndParaData) {
		this.testSetLinesAndParaData = testSetLinesAndParaData;
	}

	public List<ScriptMasterDto> getScriptMasterData() {
		return scriptMasterData;
	}

	public void setScriptMasterData(List<ScriptMasterDto> scriptMasterData) {
		this.scriptMasterData = scriptMasterData;
	}

	public String getLastExecuted_by() {
		return lastExecutedBy;
	}

	public void setLastExecutedBy(String lastExecutedBy) {
		this.lastExecutedBy = lastExecutedBy;
	}

	public String getTsComplete_flag() {
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

	public String getExeceptionPath() {
		return execeptionPath;
	}

	public void setExeceptionPath(String execeptionPath) {
		this.execeptionPath = execeptionPath;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getTrMode() {
		return trMode;
	}

	public void setTrMode(String trMode) {
		this.trMode = trMode;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getFailPath() {
		return failPath;
	}

	public void setFailPath(String failPath) {
		this.failPath = failPath;
	}

	public Date getEffectiveTo() {
		return effectiveTo;
	}

	public void setEffectiveTo(Date effectiveTo) {
		this.effectiveTo = effectiveTo;
	}

	public Date getEffectiveFrom() {
		return effectiveFrom;
	}

	public void setEffectiveFrom(Date effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}

	public int getConfigurationId() {
		return configurationId;
	}

	public void setConfigurationId(int configurationId) {
		this.configurationId = configurationId;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTestSetDesc() {
		return testSetDesc;
	}

	public void setTestSetDesc(String testSetDesc) {
		this.testSetDesc = testSetDesc;
	}

	public String getTestSetComments() {
		return testSetComments;
	}

	public void setTestSetComments(String testSetComments) {
		this.testSetComments = testSetComments;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getTestSetName() {
		return testSetName;
	}

	public void setTestSetName(String testSetName) {
		this.testSetName = testSetName;
	}

	public int getTestSetId() {
		return testSetId;
	}

	public void setTestSetId(int testSetId) {
		this.testSetId = testSetId;
	}

	public String getConfigurationName() {
		return configurationName;
	}

	public void setConfigurationName(String configurationName) {
		this.configurationName = configurationName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

}
