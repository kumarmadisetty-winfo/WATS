package com.winfo.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.winfo.model.Testrundata;

public class TestRunMigrationDto {

	@JsonProperty("projectName")
	private String projectName;

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
	private String effectiveTo;

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
	private String effectiveFrom;

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

	public TestRunMigrationDto(Testrundata testRunData) {

		this.configurationId = testRunData.getConfigurationid();
		this.createdBy = testRunData.getCreatedby();
		this.projectId = testRunData.getProjectid();
		this.testSetName = testRunData.getTestsetname();
		this.testSetDesc = testRunData.getTest_set_desc();
		this.testSetComments = testRunData.getTest_set_comments();
		this.enabled = testRunData.getEnabled();
		this.description = testRunData.getDescription();
		this.effectiveFrom = testRunData.getEffective_from();
		this.effectiveTo = testRunData.getEffective_to();
		this.tsCompleteFlag = testRunData.getTscompleteflag();
		this.passPath = testRunData.getPasspath();
		this.failPath = testRunData.getFailpath();
		this.execeptionPath = testRunData.getExceptionpath();
		this.trMode = testRunData.getTrmode();
		this.lastUpdatedBy = null;
		this.creationDate = null;
		this.updateDate = null;
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

	public String getLast_executed_by() {
		return lastExecutedBy;
	}

	public void setLast_executed_by(String lastExecutedBy) {
		this.lastExecutedBy = lastExecutedBy;
	}

	public String getTs_complete_flag() {
		return tsCompleteFlag;
	}

	public void setTs_complete_flag(String tsCompleteFlag) {
		this.tsCompleteFlag = tsCompleteFlag;
	}

	public String getPass_path() {
		return passPath;
	}

	public void setPass_path(String passPath) {
		this.passPath = passPath;
	}

	public String getEffective_to() {
		return effectiveTo;
	}

	public void setEffective_to(String effectiveTo) {
		this.effectiveTo = effectiveTo;
	}

	public String getExeception_path() {
		return execeptionPath;
	}

	public void setExeception_path(String execeptionPath) {
		this.execeptionPath = execeptionPath;
	}

	public String getCreation_date() {
		return creationDate;
	}

	public void setCreation_date(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreated_by() {
		return createdBy;
	}

	public void setCreated_by(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getTr_mode() {
		return trMode;
	}

	public void setTr_mode(String tr_mode) {
		this.trMode = tr_mode;
	}

	public String getUpdate_date() {
		return updateDate;
	}

	public void setUpdate_date(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getLast_updated_by() {
		return lastUpdatedBy;
	}

	public void setLast_updated_by(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getFail_path() {
		return failPath;
	}

	public void setFail_path(String fail_path) {
		this.failPath = fail_path;
	}

	public String getEffective_from() {
		return effectiveFrom;
	}

	public void setEffective_from(String effective_from) {
		this.effectiveFrom = effective_from;
	}

	public int getConfiguration_id() {
		return configurationId;
	}

	public void setConfiguration_id(int configuration_id) {
		this.configurationId = configuration_id;
	}

	public int getProject_id() {
		return projectId;
	}

	public void setProject_id(int project_id) {
		this.projectId = project_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTest_set_desc() {
		return testSetDesc;
	}

	public void setTest_set_desc(String test_set_desc) {
		this.testSetDesc = test_set_desc;
	}

	public String getTest_set_comments() {
		return testSetComments;
	}

	public void setTest_set_comments(String test_set_comments) {
		this.testSetComments = test_set_comments;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getTest_set_name() {
		return testSetName;
	}

	public void setTest_set_name(String test_set_name) {
		this.testSetName = test_set_name;
	}

	public int getTest_set_id() {
		return testSetId;
	}

	public void setTest_set_id(int testSetId) {
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
