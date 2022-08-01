package com.winfo.vo;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.winfo.model.ScriptMaster;

public class ScriptMasterDto {

	// SCRIPTMASTER TABLE

	@JsonProperty("scriptId")
	private Integer scriptId;
	@JsonProperty("scriptNumber")
	private String scriptNumber;
	@JsonProperty("processArea")
	private String processArea;
	@JsonProperty("subProcessArea")
	private String subProcessArea;
	@JsonProperty("module")
	private String module;
	@JsonProperty("role")
	private String role;
	@JsonProperty("end2endScenario")
	private String end2endScenario;
	@JsonProperty("scenarioName")
	private String scenarioName;
	@JsonProperty("scenarioDescription")
	private String scenarioDescription;
	@JsonProperty("expectedResult")
	private String expectedResult;
	@JsonProperty("seleniumTestScriptName")
	private String seleniumTestScriptName;
	@JsonProperty("seleniumTestMethod")
	private String seleniumTestMethod;
	@JsonProperty("dependency")
	private Integer dependency;
	@JsonProperty("productVersion")
	private String productVersion;
	@JsonProperty("standardCustom")
	private String standardCustom;
	@JsonProperty("testScriptStatus")
	private String testScriptStatus;
	@JsonProperty("author")
	private String author;
	@JsonProperty("createdBy")
	private String createdBy;
	@JsonProperty("creationDate")
	private Date creationDate;
	@JsonProperty("updatedBy")
	private String updatedBy;
	@JsonProperty("updateDate")
	private Date updateDate;
	@JsonProperty("customerId")
	private Integer customerId;
	@JsonProperty("customisationReference")
	private String customisationReference;
	@JsonProperty("attribute2")
	private String attribute2;
	@JsonProperty("attribute3")
	private String attribute3;
	@JsonProperty("attribute4")
	private String attribute4;
	@JsonProperty("attribute5")
	private String attribute5;
	@JsonProperty("attribute6")
	private String attribute6;
	@JsonProperty("attribute7")
	private String attribute7;
	@JsonProperty("attribute8")
	private String attribute8;
	@JsonProperty("attribute9")
	private String attribute9;
	@JsonProperty("attribute10")
	private String attribute10;
	@JsonProperty("priority")
	private Integer priority;
	@JsonProperty("dependentScriptNum")
	private String dependentScriptNum;
	@JsonProperty("targetApplication")
	private String targetApplication;

	@JsonProperty("metaDataList")
	private List<ScriptMetaDataDto> metaDataList = new ArrayList<>();

	public ScriptMasterDto() {
	}

	public ScriptMasterDto(ScriptMaster scriptMaster) {
		this.setAttribute2(scriptMaster.getAttribute2());
		this.setAttribute10(scriptMaster.getAttribute10());
		this.setAttribute3(scriptMaster.getAttribute3());
		this.setAttribute4(scriptMaster.getAttribute4());
		this.setAttribute5(scriptMaster.getAttribute5());
		this.setAttribute6(scriptMaster.getAttribute6());
		this.setAttribute7(scriptMaster.getAttribute7());
		this.setAttribute8(scriptMaster.getAttribute8());
		this.setAttribute9(scriptMaster.getAttribute9());
		this.setAuthor(scriptMaster.getAuthor());
		this.setCreated_by(scriptMaster.getCreated_by());
		this.setCreation_date(scriptMaster.getCreation_date());
		this.setCustomer_id(scriptMaster.getCustomer_id());
		this.setCustomisation_reference(scriptMaster.getCustomisation_reference());
		this.setDependency(scriptMaster.getDependency());
		this.setDependent_script_num(scriptMaster.getDependent_script_num());
		this.setEnd2end_scenario(scriptMaster.getEnd2end_scenario());
		this.setExpected_result(scriptMaster.getExpected_result());
		this.setModule(scriptMaster.getModule());
		this.setPriority(scriptMaster.getPriority());
		this.setProcess_area(scriptMaster.getProcess_area());
		this.setProduct_version(scriptMaster.getProduct_version());
		this.setRole(scriptMaster.getRole());
		this.setScenario_description(scriptMaster.getScenario_description());
		this.setScenario_name(scriptMaster.getScenario_name());
		this.setScript_id(scriptMaster.getScript_id());
		this.setScript_number(scriptMaster.getScript_number());
		this.setSelenium_test_method(scriptMaster.getSelenium_test_method());
		this.setSelenium_test_script_name(scriptMaster.getSelenium_test_script_name());
		this.setStandard_custom(scriptMaster.getStandard_custom());
		this.setSub_process_area(scriptMaster.getSub_process_area());
		this.setTargetApplication(scriptMaster.getTargetApplication());
		this.setTest_script_status(scriptMaster.getTest_script_status());
		this.setUpdate_date(scriptMaster.getUpdate_date());
		this.setUpdated_by(scriptMaster.getUpdated_by());
	}

	public String getTargetApplication() {
		return targetApplication;
	}

	public void setTargetApplication(String targetApplication) {
		this.targetApplication = targetApplication;
	}

	public Integer getScript_id() {
		return scriptId;
	}

	public void setScript_id(Integer scriptId) {
		this.scriptId = scriptId;
	}

	public String getDependent_script_num() {
		return dependentScriptNum;
	}

	public void setDependent_script_num(String dependentScriptNum) {
		this.dependentScriptNum = dependentScriptNum;
	}

	public String getScript_number() {
		return scriptNumber;
	}

	public void setScript_number(String scriptNumber) {
		this.scriptNumber = scriptNumber;
	}

	public String getProcess_area() {
		return processArea;
	}

	public void setProcess_area(String processArea) {
		this.processArea = processArea;
	}

	public String getSub_process_area() {
		return subProcessArea;
	}

	public void setSub_process_area(String subProcessArea) {
		this.subProcessArea = subProcessArea;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getEnd2end_scenario() {
		return end2endScenario;
	}

	public void setEnd2end_scenario(String end2endScenario) {
		this.end2endScenario = end2endScenario;
	}

	public String getScenario_name() {
		return scenarioName;
	}

	public void setScenario_name(String scenarioName) {
		this.scenarioName = scenarioName;
	}

	public String getScenario_description() {
		return scenarioDescription;
	}

	public void setScenario_description(String scenarioDescription) {
		this.scenarioDescription = scenarioDescription;
	}

	public String getExpected_result() {
		return expectedResult;
	}

	public void setExpected_result(String expectedResult) {
		this.expectedResult = expectedResult;
	}

	public String getSelenium_test_script_name() {
		return seleniumTestScriptName;
	}

	public void setSelenium_test_script_name(String seleniumTestScriptName) {
		this.seleniumTestScriptName = seleniumTestScriptName;
	}

	public String getSelenium_test_method() {
		return seleniumTestMethod;
	}

	public void setSelenium_test_method(String seleniumTestMethod) {
		this.seleniumTestMethod = seleniumTestMethod;
	}

	public Integer getDependency() {
		return dependency;
	}

	public void setDependency(Integer dependency) {
		this.dependency = dependency;
	}

	public String getProduct_version() {
		return productVersion;
	}

	public void setProduct_version(String productVersion) {
		this.productVersion = productVersion;
	}

	public String getStandard_custom() {
		return standardCustom;
	}

	public void setStandard_custom(String standardCustom) {
		this.standardCustom = standardCustom;
	}

	public String getTest_script_status() {
		return testScriptStatus;
	}

	public void setTest_script_status(String testScriptStatus) {
		this.testScriptStatus = testScriptStatus;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCreated_by() {
		return createdBy;
	}

	public void setCreated_by(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreation_date() {
		return creationDate;
	}

	public void setCreation_date(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getUpdated_by() {
		return updatedBy;
	}

	public void setUpdated_by(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdate_date() {
		return updateDate;
	}

	public void setUpdate_date(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Integer getCustomer_id() {
		return customerId;
	}

	public void setCustomer_id(Integer customerId) {
		this.customerId = customerId;
	}

	public String getCustomisation_reference() {
		return customisationReference;
	}

	public void setCustomisation_reference(String customisationReference) {
		this.customisationReference = customisationReference;
	}

	public String getAttribute2() {
		return attribute2;
	}

	public void setAttribute2(String attribute2) {
		this.attribute2 = attribute2;
	}

	public String getAttribute3() {
		return attribute3;
	}

	public void setAttribute3(String attribute3) {
		this.attribute3 = attribute3;
	}

	public String getAttribute4() {
		return attribute4;
	}

	public void setAttribute4(String attribute4) {
		this.attribute4 = attribute4;
	}

	public String getAttribute5() {
		return attribute5;
	}

	public void setAttribute5(String attribute5) {
		this.attribute5 = attribute5;
	}

	public String getAttribute6() {
		return attribute6;
	}

	public void setAttribute6(String attribute6) {
		this.attribute6 = attribute6;
	}

	public String getAttribute7() {
		return attribute7;
	}

	public void setAttribute7(String attribute7) {
		this.attribute7 = attribute7;
	}

	public String getAttribute8() {
		return attribute8;
	}

	public void setAttribute8(String attribute8) {
		this.attribute8 = attribute8;
	}

	public String getAttribute9() {
		return attribute9;
	}

	public void setAttribute9(String attribute9) {
		this.attribute9 = attribute9;
	}

	public String getAttribute10() {
		return attribute10;
	}

	public void setAttribute10(String attribute10) {
		this.attribute10 = attribute10;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public List<ScriptMetaDataDto> getMetaDataList() {
		return metaDataList;
	}

	public void setMetaDataList(List<ScriptMetaDataDto> metaDataList) {
		this.metaDataList = metaDataList;
	}

}
