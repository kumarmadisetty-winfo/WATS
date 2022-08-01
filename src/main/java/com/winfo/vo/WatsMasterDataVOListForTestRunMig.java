package com.winfo.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.winfo.model.Testrundata;

public class WatsMasterDataVOListForTestRunMig {
	
	@JsonProperty("projectName")
	private String projectName;
	
	@JsonProperty("customer")
	private String customer;
	
	@JsonProperty("configurationName")
	private String configurationName;
	
	@JsonProperty("test_set_id")
	private int test_set_id;
	
	@JsonProperty("project_id")
	private int project_id;
	
	@JsonProperty("description")
	private String description;
	
	@JsonProperty("test_set_desc")
	private String test_set_desc;

	@JsonProperty("test_set_comments")
	private String test_set_comments;
	
	@JsonProperty("enabled")
	private String enabled;
	
	@JsonProperty("last_executed_by")
	private String last_executed_by;
	
	@JsonProperty("ts_complete_flag")
	private String ts_complete_flag;
	
	@JsonProperty("pass_path")
	private String pass_path;
	
	@JsonProperty("effective_to")
	private String effective_to;
	
	@JsonProperty("test_set_name")
	private String test_set_name;
	
	@JsonProperty("exeception_path")
	private String exeception_path;
	
	@JsonProperty("creation_date")
	private String creation_date;
	
	@JsonProperty("created_by")
	private String created_by;
	
	@JsonProperty("tr_mode")
	private String tr_mode;
	
	@JsonProperty("update_date")
	private String update_date;
	
	@JsonProperty("last_updated_by")
	private String last_updated_by;
	
	@JsonProperty("fail_path")
	private String fail_path;
	
	@JsonProperty("effective_from")
	private String effective_from;
	
	@JsonProperty("configuration_id")
	private int configuration_id;
	
	@JsonProperty("ScriptMasterData")
	private List<WatsMasterVO> ScriptMasterData= new ArrayList<WatsMasterVO>();
	
	@JsonProperty("TestSetLinesAndParaData")
	private List<WatsTestSetVO> TestSetLinesAndParaData= new ArrayList<WatsTestSetVO>();
	
	@JsonProperty("lookUpData")
	private Map<String,LookUpVO> lookUpData;
	
	@JsonProperty("testRunExists")
	private boolean testRunExists;
	
	

	public WatsMasterDataVOListForTestRunMig() {}
	
	public WatsMasterDataVOListForTestRunMig(Testrundata testRunData) {

		this.configuration_id = testRunData.getConfigurationid();
		this.created_by = testRunData.getCreatedby();
		this.project_id = testRunData.getProjectid();
		this.test_set_name = testRunData.getTestsetname();
		this.test_set_desc = testRunData.getTest_set_desc();
		this.test_set_comments = testRunData.getTest_set_comments();
		this.enabled = testRunData.getEnabled();
		this.description = testRunData.getDescription();
		this.effective_from = testRunData.getEffective_from();
		this.effective_to = testRunData.getEffective_to();
		this.ts_complete_flag = testRunData.getTscompleteflag();
		this.pass_path = testRunData.getPasspath();
		this.fail_path = testRunData.getFailpath();
		this.exeception_path =  testRunData.getExceptionpath();
		this.tr_mode = testRunData.getTrmode();
		this.last_updated_by = null;
		this.creation_date = null;
		this.update_date = null;
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

	public List<WatsTestSetVO> getTestSetLinesAndParaData() {
		return TestSetLinesAndParaData;
	}

	public void setTestSetLinesAndParaData(List<WatsTestSetVO> testSetLinesAndParaData) {
		TestSetLinesAndParaData = testSetLinesAndParaData;
	}

	public List<WatsMasterVO> getScriptMasterData() {
		return ScriptMasterData;
	}

	public void setScriptMasterData(List<WatsMasterVO> scriptMasterData) {
		ScriptMasterData = scriptMasterData;
	}

	public String getLast_executed_by() {
		return last_executed_by;
	}

	public void setLast_executed_by(String last_executed_by) {
		this.last_executed_by = last_executed_by;
	}

	public String getTs_complete_flag() {
		return ts_complete_flag;
	}

	public void setTs_complete_flag(String ts_complete_flag) {
		this.ts_complete_flag = ts_complete_flag;
	}

	public String getPass_path() {
		return pass_path;
	}

	public void setPass_path(String pass_path) {
		this.pass_path = pass_path;
	}

	public String getEffective_to() {
		return effective_to;
	}

	public void setEffective_to(String effective_to) {
		this.effective_to = effective_to;
	}

	public String getExeception_path() {
		return exeception_path;
	}

	public void setExeception_path(String exeception_path) {
		this.exeception_path = exeception_path;
	}

	public String getCreation_date() {
		return creation_date;
	}

	public void setCreation_date(String creation_date) {
		this.creation_date = creation_date;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public String getTr_mode() {
		return tr_mode;
	}

	public void setTr_mode(String tr_mode) {
		this.tr_mode = tr_mode;
	}

	public String getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
	}

	public String getLast_updated_by() {
		return last_updated_by;
	}

	public void setLast_updated_by(String last_updated_by) {
		this.last_updated_by = last_updated_by;
	}

	public String getFail_path() {
		return fail_path;
	}

	public void setFail_path(String fail_path) {
		this.fail_path = fail_path;
	}

	public String getEffective_from() {
		return effective_from;
	}

	public void setEffective_from(String effective_from) {
		this.effective_from = effective_from;
	}

	public int getConfiguration_id() {
		return configuration_id;
	}

	public void setConfiguration_id(int configuration_id) {
		this.configuration_id = configuration_id;
	}

	public int getProject_id() {
		return project_id;
	}

	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getTest_set_name() {
		return test_set_name;
	}

	public void setTest_set_name(String test_set_name) {
		this.test_set_name = test_set_name;
	}

	public int getTest_set_id() {
		return test_set_id;
	}

	public void setTest_set_id(int test_set_id) {
		this.test_set_id = test_set_id;
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
	
	
	
	
	
		
	
