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
		this.setCreatedBy(scriptMaster.getCreatedBy());
		this.setCreationDate(scriptMaster.getCreationDate());
		this.setCustomerId(scriptMaster.getCustomerId());
		this.setCustomisationReference(scriptMaster.getCustomisationReference());
		this.setDependency(scriptMaster.getDependency());
		this.setDependentScriptNum(scriptMaster.getDependentScriptNum());
		this.setEnd2endScenario(scriptMaster.getEnd2endScenario());
		this.setExpectedResult(scriptMaster.getExpectedResult());
		this.setModule(scriptMaster.getModule());
		this.setPriority(scriptMaster.getPriority());
		this.setProcessArea(scriptMaster.getProcessArea());
		this.setProductVersion(scriptMaster.getProductVersion());
		this.setRole(scriptMaster.getRole());
		this.setScenarioDescription(scriptMaster.getScenarioDescription());
		this.setScenarioName(scriptMaster.getScenarioName());
		this.setScriptId(scriptMaster.getScriptId());
		this.setScriptNumber(scriptMaster.getScriptNumber());
		this.setSeleniumTestMethod(scriptMaster.getSeleniumTestMethod());
		this.setSeleniumTestScriptName(scriptMaster.getSeleniumTestScriptName());
		this.setStandardCustom(scriptMaster.getStandardCustom());
		this.setSubProcessArea(scriptMaster.getSubProcessArea());
		this.setTargetApplication(scriptMaster.getTargetApplication());
		this.setTestScriptStatus(scriptMaster.getTestScriptStatus());
		this.setUpdateDate(scriptMaster.getUpdateDate());
		this.setUpdatedBy(scriptMaster.getUpdatedBy());
	}

	public String getTargetApplication() {
		return targetApplication;
	}

	public void setTargetApplication(String targetApplication) {
		this.targetApplication = targetApplication;
	}

	public Integer getScriptId() {
		return scriptId;
	}

	public void setScriptId(Integer scriptId) {
		this.scriptId = scriptId;
	}

	public String getDependentScriptNum() {
		return dependentScriptNum;
	}

	public void setDependentScriptNum(String dependentScriptNum) {
		this.dependentScriptNum = dependentScriptNum;
	}

	public String getScriptNumber() {
		return scriptNumber;
	}

	public void setScriptNumber(String scriptNumber) {
		this.scriptNumber = scriptNumber;
	}

	public String getProcessArea() {
		return processArea;
	}

	public void setProcessArea(String processArea) {
		this.processArea = processArea;
	}

	public String getSubProcessArea() {
		return subProcessArea;
	}

	public void setSubProcessArea(String subProcessArea) {
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

	public String getEnd2endScenario() {
		return end2endScenario;
	}

	public void setEnd2endScenario(String end2endScenario) {
		this.end2endScenario = end2endScenario;
	}

	public String getScenarioName() {
		return scenarioName;
	}

	public void setScenarioName(String scenarioName) {
		this.scenarioName = scenarioName;
	}

	public String getScenarioDescription() {
		return scenarioDescription;
	}

	public void setScenarioDescription(String scenarioDescription) {
		this.scenarioDescription = scenarioDescription;
	}

	public String getExpectedResult() {
		return expectedResult;
	}

	public void setExpectedResult(String expectedResult) {
		this.expectedResult = expectedResult;
	}

	public String getSeleniumTestScriptName() {
		return seleniumTestScriptName;
	}

	public void setSeleniumTestScriptName(String seleniumTestScriptName) {
		this.seleniumTestScriptName = seleniumTestScriptName;
	}

	public String getSeleniumTestMethod() {
		return seleniumTestMethod;
	}

	public void setSeleniumTestMethod(String seleniumTestMethod) {
		this.seleniumTestMethod = seleniumTestMethod;
	}

	public Integer getDependency() {
		return dependency;
	}

	public void setDependency(Integer dependency) {
		this.dependency = dependency;
	}

	public String getProductVersion() {
		return productVersion;
	}

	public void setProductVersion(String productVersion) {
		this.productVersion = productVersion;
	}

	public String getStandardCustom() {
		return standardCustom;
	}

	public void setStandardCustom(String standardCustom) {
		this.standardCustom = standardCustom;
	}

	public String getTestScriptStatus() {
		return testScriptStatus;
	}

	public void setTestScriptStatus(String testScriptStatus) {
		this.testScriptStatus = testScriptStatus;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getCustomisationReference() {
		return customisationReference;
	}

	public void setCustomisationReference(String customisationReference) {
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
