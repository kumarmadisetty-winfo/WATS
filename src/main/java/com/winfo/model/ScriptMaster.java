package com.winfo.model;

import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "WIN_TA_SCRIPT_MASTER")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "scriptId")
public class ScriptMaster {
	
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "master_generator")
	@SequenceGenerator(name = "master_generator", sequenceName = "WIN_TA_SCRIPT_MASTER_SEQ", allocationSize = 1)
	@Id
	@Column(name = "SCRIPT_ID")
	private Integer scriptId;

	@Column(name = "SCRIPT_NUMBER")
	private String scriptNumber;

	@Column(name = "PROCESS_AREA")
	private String processArea;

	@Column(name = "SUB_PROCESS_AREA")
	private String subProcessArea;

	@Column(name = "MODULE")
	private String module;

	@Column(name = "ROLE")
	private String role;

	@Column(name = "END2END_SCENARIO")
	private String end2endScenario;

	@Column(name = "SCENARIO_NAME")
	private String scenarioName;

	@Column(name = "SCENARIO_DESCRIPTION")
	private String scenarioDescription;

	@Column(name = "EXPECTED_RESULT")
	private String expectedResult;

	@Column(name = "SELENIUM_TEST_SCRIPT_NAME")
	private String seleniumTestScriptName;

	@Column(name = "SELENIUM_TEST_METHOD")
	private String seleniumTestMethod;

	@Column(name = "DEPENDENCY")
	private Integer dependency;

	@Column(name = "PRODUCT_VERSION")
	private String productVersion;

	@Column(name = "STANDARD_CUSTOM")
	private String standardCustom;

	@Column(name = "TEST_SCRIPT_STATUS")
	private String testScriptStatus;

	@Column(name = "AUTHOR")
	private String author;
	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATION_DATE")
	private Date creationDate;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Column(name = "UPDATE_DATE")
	private Date updateDate;

	@Column(name = "CUSTOMER_ID")
	private Integer customerId;

	@Column(name = "CUSTOMISATION_REFERENCE")
	private String customisationReference;

	@Column(name = "ATTRIBUTE2")
	private String attribute2;

	@Column(name = "ATTRIBUTE3")
	private String attribute3;

	@Column(name = "ATTRIBUTE4")
	private String attribute4;

	@Column(name = "ATTRIBUTE5")
	private String attribute5;

	@Column(name = "ATTRIBUTE6")
	private String attribute6;

	@Column(name = "ATTRIBUTE7")
	private String attribute7;

	@Column(name = "ATTRIBUTE8")
	private String attribute8;

	@Column(name = "ATTRIBUTE9")
	private String attribute9;

	@Column(name = "ATTRIBUTE10")
	private String attribute10;

	@Column(name = "PRIORITY")
	private Integer priority;

	@Column(name = "DEPENDENT_SCRIPT_NUM")
	private String dependentScriptNum;

	@Column(name = "APPR_FOR_MIGRATION")
	private String apprForMigration;

	@Column(name = "PLUGIN_FLAG")
	private String pluginFlag;

	@Column(name = "TARGET_APPLICATION")
	private String targetApplication;

	@OneToMany(cascade = CascadeType.MERGE, mappedBy = "scriptMaster", fetch = FetchType.LAZY)
	private List<ScriptMetaData> scriptMetaDatalist;
	

	@OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "DEPENDENCY", insertable = false, updatable = false)
    private ScriptMaster parent;   

	public ScriptMaster getParent() {
		return parent;
	}

	public void setParent(ScriptMaster parent) {
		this.parent = parent;
	}

	public String getPluginFlag() {
		return pluginFlag;
	}

	public void setPluginFlag(String pluginFlag) {
		this.pluginFlag = pluginFlag;
	}

	public String getTargetApplication() {
		return targetApplication;
	}

	public void setTargetApplication(String targetApplication) {
		this.targetApplication = targetApplication;
	}

	public void addMetadata(ScriptMetaData metadata) {
		scriptMetaDatalist.add(metadata);
		metadata.setScriptMaster(this);
	}

	public String getDependentScriptNum() {
		return dependentScriptNum;
	}

	public void setDependentScriptNum(String dependentScriptNum) {
		this.dependentScriptNum = dependentScriptNum;
	}

	public String getApprForMigration() {
		return apprForMigration;
	}

	public void setApprForMigration(String apprForMigration) {
		this.apprForMigration = apprForMigration;
	}

	public Integer getScriptId() {
		return scriptId;
	}

	public void setScriptId(Integer scriptId) {
		this.scriptId = scriptId;
	}

	public List<ScriptMetaData> getScriptMetaDatalist() {
		return scriptMetaDatalist;
	}

	public void setScriptMetaDatalist(List<ScriptMetaData> scriptMetaDatalist) {
		this.scriptMetaDatalist = scriptMetaDatalist;
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

}
