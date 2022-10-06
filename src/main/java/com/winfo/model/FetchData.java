package com.winfo.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FetchData {
	@JsonProperty("script_number")
	private String scriptNumber;

	@JsonProperty("module")
	private String module;

	@JsonProperty("scenario_name")
	private String scenarioName;

	@JsonProperty("scenario_description")
	private String scenarioDescription;

	@JsonProperty("product_version")
	private String productVersion;

	@JsonProperty("priority")
	private Integer priority;

	@JsonProperty("process_area")
	private String processArea;

	@JsonProperty("role")
	private String role;

	@JsonProperty("subProcess_area")
	private String subProcessArea;

	@JsonProperty("standard_custom")
	private String standardCustom;

	@JsonProperty("test_script_status")
	private String testScriptStatus;

	@JsonProperty("customer_id")
	private Integer customerId;

	@JsonProperty("dependency")
	private Integer dependency;

	@JsonProperty("end2end_scenario")
	private String end2endScenario;

	@JsonProperty("expected_result")
	private String expectedResult;

	@JsonProperty("selenium_test_script_name")
	private String seleniumTestScriptName;

	@JsonProperty("selenium_test_method")
	private String seleniumTestMethod;

	@JsonProperty("author")
	private String author;
	@JsonProperty("created_by")
	private String createdBy;

	@JsonProperty("creation_date")
	private Date creationDate;

	@JsonProperty("updated_by")
	private String updatedBy;

	@JsonProperty("update_date")
	private Date updateDate;

	@JsonProperty("customisation_refrence")
	private String customisationRefrence;

	@JsonProperty("attribute1")
	private String attribute1;

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

	@JsonProperty("DEPENDENT_SCRIPT_NUM")
	private String dependentScriptNum;

	private String targetApplication;

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

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
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

	public String getProductVersion() {
		return productVersion;
	}

	public void setProductVersion(String productVersion) {
		this.productVersion = productVersion;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getProcessArea() {
		return processArea;
	}

	public void setProcessArea(String processArea) {
		this.processArea = processArea;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getSubProcessArea() {
		return subProcessArea;
	}

	public void setSubProcessArea(String subProcessArea) {
		this.subProcessArea = subProcessArea;
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

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getDependency() {
		return dependency;
	}

	public void setDependency(Integer dependency) {
		this.dependency = dependency;
	}

	public String getEnd2endScenario() {
		return end2endScenario;
	}

	public void setEnd2endScenario(String end2endScenario) {
		this.end2endScenario = end2endScenario;
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

	public String getCustomisationRefrence() {
		return customisationRefrence;
	}

	public void setCustomisationRefrence(String customisationRefrence) {
		this.customisationRefrence = customisationRefrence;
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

	public String getTargetApplication() {
		return targetApplication;
	}

	public void setTargetApplication(String targetApplication) {
		this.targetApplication = targetApplication;
	}

}
