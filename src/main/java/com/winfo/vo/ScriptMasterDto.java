package com.winfo.vo;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.winfo.model.AuditScriptExecTrail;
import com.winfo.model.ScriptMaster;
import com.winfo.utils.Constants.AUDIT_TRAIL_STAGES;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScriptMasterDto {

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

	@JsonProperty("lookUpVO")
	private LookUpVO lookUpVO;

	@JsonProperty("metaDataList")
	private List<ScriptMetaDataDto> metaDataList = new ArrayList<>();
	
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

}
