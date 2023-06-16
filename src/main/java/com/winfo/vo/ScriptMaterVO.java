package com.winfo.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.winfo.serviceImpl.DataBaseEntry;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ScriptMaterVO {

	@JsonProperty("Script Number")
	@JsonAlias("scriptNumber")
	private String scriptNumber;

	@JsonProperty("Test Case Name")
	@JsonAlias("scenarioName")
	private String scenarioName;

	@JsonProperty("Target Application")
	@JsonAlias("targetApplication")
	private String targetApplication;

	@JsonProperty("Process Area")
	@JsonAlias("processArea")
	private String processArea;

	@JsonProperty("Module")
	@JsonAlias("module")
	private String module;

	@JsonProperty("Product Version")
	@JsonAlias("productVersion")
	private String productVersion;

	@JsonProperty("Dependency")
	@JsonAlias("dependency")
	private String dependency;

	@JsonProperty("Sub Process Area")
	@JsonAlias("subProcessArea")
	private String subProcessArea;

	@JsonProperty("Role")
	@JsonAlias("role")
	private String role;

	@JsonProperty("Type Of Script")
	@JsonAlias("standardCustom")
	private String standardCustom;

	@JsonProperty("Priority")
	@JsonAlias("priority")
	private String priority;

	@JsonProperty("Test Script Status")
	@JsonAlias("testScriptStatus")
	private String testScriptStatus;

	@JsonProperty("Test Case Description")
	@JsonAlias("scenarioDescription")
	private String scenarioDescription;

	@JsonProperty("Expected Result")
	@JsonAlias("expectedResult")
	private String expectedResult;

	@JsonProperty("Customer Id")
	@JsonAlias("customerId")
	private String customerId;

	@JsonProperty("Customization Reference")
	@JsonAlias("customisationReference")
	private String customisationReference;

	@JsonProperty("Reference Script")
	@JsonAlias("attribute1")
	private String attribute1;

	@JsonProperty("Request increment fields")
	@JsonAlias("attribute2")
	private String attribute2;

	@JsonProperty("Attribute3")
	@JsonAlias("attribute3")
	private String attribute3;

	@JsonProperty("Attribute4")
	@JsonAlias("attribute4")
	private String attribute4;

	@JsonProperty("Attribute5")
	@JsonAlias("attribute5")
	private String attribute5;

	@JsonProperty("Attribute6")
	@JsonAlias("attribute6")
	private String attribute6;

	@JsonProperty("Attribute7")
	@JsonAlias("attribute7")
	private String attribute7;

	@JsonProperty("Attribute8")
	@JsonAlias("attribute8")
	private String attribute8;

	@JsonProperty("Attribute9")
	@JsonAlias("attribute9")
	private String attribute9;

	@JsonProperty("Attribute10")
	@JsonAlias("attribute10")
	private String attribute10;

	@JsonProperty("Script Steps")
	@JsonAlias("scriptMetaDatalist")
	private List<ScriptMetaDataVO> scriptMetaDatalist;

	public void updateFieldIfNotNull(DataBaseEntry dataBaseEntry) {
		if (dependency != null) {
			dependency = dataBaseEntry.getScriptDetailsByScriptId(Integer.parseInt(dependency)).getScriptNumber();
		}
		if (attribute1 != null) {
			attribute1 = dataBaseEntry.getScriptDetailsByScriptId(Integer.parseInt(attribute1)).getScriptNumber();
		}
		if (processArea != null) {
			processArea = dataBaseEntry.getMeaningByTargetCode(processArea, "PROCESS");
		}
		if (module != null) {
			module = dataBaseEntry.getMeaningByTargetCode(module, "MODULE");
		}
		if (standardCustom != null) {
			standardCustom = dataBaseEntry.getMeaningByTargetCode(standardCustom, "STANDARD");
		}
		if (testScriptStatus != null) {
			testScriptStatus = dataBaseEntry.getMeaningByTargetCode(testScriptStatus, "STATUS");
		}
		if (priority != null) {
			priority = dataBaseEntry.getMeaningByTargetCode(priority, "PRIORITY");
		}
		if (role != null) {
			role = dataBaseEntry.getMeaningByTargetCode(role, "ROLE");
		}
		if (scriptMetaDatalist != null) {
		    scriptMetaDatalist.stream()
		        .filter(metaData -> metaData.getAction() != null)
		        .forEach(metaData -> {
		            String updatedAction = dataBaseEntry.getMeaningByTargetCode(metaData.getAction(), "ACTION");
		            metaData.setAction(updatedAction);
		        });
		}
		if (customerId != null) {
			customerId = dataBaseEntry.getCustomerNameFromCustomerId(Integer.parseInt(customerId)).getCustomerName();
		}

	}
}
