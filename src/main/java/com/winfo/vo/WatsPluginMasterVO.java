package com.winfo.vo;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WatsPluginMasterVO {

	@JsonProperty("SCRIPT ID")
	private Integer scriptId;

	@JsonProperty("SCRIPT NUMBER")
	private String scriptNumber;

	@JsonProperty("PROCESS AREA")
	private String processArea;

	@JsonProperty("SUB PROCESS AREA")
	private String subProcessArea;

	@JsonProperty("MODULE")
	private String module;

	@JsonProperty("ROLE")
	private String role;

	@JsonProperty("END2END SCENARIO")
	private String end2endScenario;

	@JsonProperty("SCENARIO NAME")
	private String scenarioName;

	@JsonProperty("SCENARIO DESCRIPTION")
	private String scenarioDescription;

	@JsonProperty("EXPECTED RESULT")
	private String expectedResult;

	@JsonProperty("SELENIUM TEST SCRIPT NAME")
	private String seleniumTestScriptName;

	@JsonProperty("SELENIUM_TEST_METHOD")
	private String seleniumTestMethod;

	@JsonProperty("DEPENDENCY")
	private String dependency;

	@JsonProperty("PRODUCT VERSION")
	private String productVersion;

	@JsonProperty("STANDARD CUSTOM")
	private String standardCustom;

	@JsonProperty("TEST SCRIPT STATUS")
	private String testScriptStatus;

	@JsonProperty("AUTHOR")
	private String author;

	@JsonProperty("CREATED_BY")
	private String createdBy;

	@JsonProperty("CREATION_DATE")
	private String creationDate;

	@JsonProperty("UPDATED_BY")
	private String updatedBy;

	@JsonProperty("UPDATE_DATE")
	private Date updateDate;

	@JsonProperty("CUSTOMER_ID")
	private Integer customerId;

	@JsonProperty("CUSTOMISATION_REFERENCE")
	private String customisationReference;

	@JsonProperty("ATTRIBUTE1")
	private String attribute1;

	@JsonProperty("ATTRIBUTE2")
	private String attribute2;

	@JsonProperty("ATTRIBUTE3")
	private String attribute3;

	@JsonProperty("ATTRIBUTE4")
	private String attribute4;

	@JsonProperty("ATTRIBUTE5")
	private String attribute5;

	@JsonProperty("ATTRIBUTE6")
	private String attribute6;

	@JsonProperty("ATTRIBUTE7")
	private String attribute7;

	@JsonProperty("ATTRIBUTE8")
	private String attribute8;

	@JsonProperty("ATTRIBUTE9")
	private String attribute9;

	@JsonProperty("ATTRIBUTE10")
	private String attribute10;

	@JsonProperty("PRIORITY")
	private Integer priority;

	@JsonProperty("MODULE_SRT")
	private String moduleSrt;

	@JsonProperty("TestrunName")
	private String testRunName;

	@JsonProperty("MetaDataList")
	private List<WatsPluginMetaDataVO> metaDataList = new ArrayList<>();

	public String getTestRunName() {
		return testRunName;
	}

	public void setTestRunName(String testRunName) {
		this.testRunName = testRunName;
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

	public String getDependency() {
		return dependency;
	}

	public void setDependency(String dependency) {
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

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
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

	public String getAttribute1() {
		return attribute1;
	}

	public void setAttribute1(String attribute1) {
		this.attribute1 = attribute1;
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

	public List<WatsPluginMetaDataVO> getMetaDataList() {
		return metaDataList;
	}

	public void setMetaDataList(List<WatsPluginMetaDataVO> metaDataList) {
		this.metaDataList = metaDataList;
	}

	public String getModuleSrt() {
		return moduleSrt;
	}

	public void setModuleSrt(String moduleSrt) {
		this.moduleSrt = moduleSrt;
	}

}
