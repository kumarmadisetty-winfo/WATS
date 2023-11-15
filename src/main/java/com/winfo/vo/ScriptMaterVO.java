package com.winfo.vo;

import java.sql.Date;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.winfo.serviceImpl.DataBaseEntry;
import com.winfo.utils.Constants;

import lombok.Data;
import lombok.EqualsAndHashCode;

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
	
	@EqualsAndHashCode.Exclude
	@JsonProperty("Created By")
	@JsonAlias("createdBy")
	private String createdBy;

	@EqualsAndHashCode.Exclude
	@JsonProperty("Creation Date")
	@JsonAlias("creationDate")
	private Date creationDate;

	@EqualsAndHashCode.Exclude
	@JsonProperty("Updated By")
	@JsonAlias("updatedBy")
	private String updatedBy;

	@EqualsAndHashCode.Exclude
	@JsonProperty("Updated Date")
	@JsonAlias("updateDate")
	private Date updateDate;

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

	@EqualsAndHashCode.Exclude
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
			standardCustom = dataBaseEntry.getMeaningByTargetCode(standardCustom, "TYPE_OF_SCRIPT");
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
		        	metaData.setAction(dataBaseEntry.getMeaningByTargetCodeBAndTargetApplication(metaData.getAction(), Constants.ACTION,targetApplication));
		            metaData.setValidationType(dataBaseEntry.getMeaningByTargetCode(metaData.getValidationType(), Constants.IP_VALIDATIONS));
		            if(Constants.REGULAR_EXPRESSION.equalsIgnoreCase(metaData.getValidationType())) {
		            	metaData.setValidationName(dataBaseEntry.getMeaningByTargetCode(metaData.getValidationName(), Constants.REGULAR_EXPRESSION));		            	
		            }
		            else if(Constants.API_VALIDATION.equalsIgnoreCase(metaData.getValidationType())){
		            	metaData.setValidationName(dataBaseEntry.getMeaningByTargetCode(metaData.getValidationName(), Constants.API_VALIDATION));		            			            	
		            }
		            metaData.setValidationName(dataBaseEntry.getMeaningByTargetCode(metaData.getValidationName(), Constants.API_VALIDATION));
		            metaData.setDatatypes(dataBaseEntry.getMeaningByTargetCode(metaData.getDatatypes(), Constants.DATATYPES));
		            metaData.setUniqueMandatory(dataBaseEntry.getMeaningByTargetCode(metaData.getUniqueMandatory(), Constants.UNIQUE_MANDATORY));
		        });
		    scriptMetaDatalist.sort(Comparator.comparing(ScriptMetaDataVO::getLineNumber));
		}
		if (customerId != null) {
			customerId = dataBaseEntry.getCustomerNameFromCustomerId(Integer.parseInt(customerId));
		}
		if (attribute1 != null) {
			attribute1 = dataBaseEntry.getScriptByScriptId(Integer.parseInt(attribute1)).getScriptNumber();
		}
	}
	
	//WATS-2793
	public void updateFieldIfNotNullForRequestBody(DataBaseEntry dataBaseEntry) {

		scriptMetaDatalist.stream().filter(Objects::nonNull)
				.forEach(metaData -> {
					metaData.setAction(dataBaseEntry.getMeaningByTargetCodeBAndTargetApplication(metaData.getAction(),  Constants.ACTION,targetApplication));
		            metaData.setValidationType(dataBaseEntry.getLookUpCodeByMeaning(metaData.getValidationType(), Constants.IP_VALIDATIONS));
		            if(Constants.REGULAR_EXPRESSION.equalsIgnoreCase(metaData.getValidationType())) {
		            	metaData.setValidationName(dataBaseEntry.getLookUpCodeByMeaning(metaData.getValidationName(), Constants.REGULAR_EXPRESSION));		            	
		            }
		            else if(Constants.API_VALIDATION.equalsIgnoreCase(metaData.getValidationType())){
		            	metaData.setValidationName(dataBaseEntry.getLookUpCodeByMeaning(metaData.getValidationName(), Constants.API_VALIDATION));		            			            	
		            }
		            metaData.setDatatypes(dataBaseEntry.getLookUpCodeByMeaning(metaData.getDatatypes(), Constants.DATATYPES));
		            metaData.setUniqueMandatory(dataBaseEntry.getLookUpCodeByMeaning(metaData.getUniqueMandatory(),Constants.UNIQUE_MANDATORY));
					//TODO- Get lookup code from meaning for validation type, datatype, unique mandatory.
				});
		if (StringUtils.isNotEmpty(role)&& StringUtils.isNotBlank(role)) {
			role = dataBaseEntry.getLookUpCodeByMeaning(role, "ROLE");
		}
		

		if (StringUtils.isNotBlank(subProcessArea)&&StringUtils.isNotEmpty(subProcessArea)) {
		  subProcessArea = dataBaseEntry.getLookUpCodeByMeaning(subProcessArea, "SUB_PROCESS_AREA");
		}

		
		if (StringUtils.isNotBlank(processArea)&&  StringUtils.isNotEmpty(processArea)) {
			processArea = dataBaseEntry.getLookUpCodeByMeaning(processArea, "PROCESS");
		}
		if (StringUtils.isNotBlank(module)&&  StringUtils.isNotEmpty(module)) {
		
			module = dataBaseEntry.getLookUpCodeByMeaning(module, "MODULE");
		}
		if (StringUtils.isNotBlank(targetApplication)&&  StringUtils.isNotEmpty(targetApplication)) {

			targetApplication = dataBaseEntry.getLookUpCodeByMeaning(targetApplication, "TARGET_APPLICATION");
		}
		if (StringUtils.isNotBlank(productVersion)&&  StringUtils.isNotEmpty(productVersion)) {

			productVersion = dataBaseEntry.getLookUpCodeByMeaning(productVersion, "PRODUCT_VERSION");
		}
		if (StringUtils.isNotBlank(attribute1)&&  StringUtils.isNotEmpty(attribute1)) {

			attribute1 = dataBaseEntry.getScriptByScriptNumberAndProductVersion(attribute1, productVersion).getScriptId().toString();
		}
	}
	public void changeNullToNA() {
		scriptMetaDatalist.stream()
        .filter(metaData -> metaData.getAction() != null)
        .forEach(metaData -> {
            if(metaData.getValidationType()==null) {
            	metaData.setValidationType("NA");            	
            }
            if(metaData.getValidationName()==null) {
            	metaData.setValidationName("NA");            	
            }
            if(metaData.getDatatypes()==null) {
            	metaData.setDatatypes("NA");            	
            }
            if(metaData.getUniqueMandatory()==null) {
            	metaData.setUniqueMandatory("NA");            	
            }
        });
		scriptMetaDatalist.sort(Comparator.comparing(ScriptMetaDataVO::getLineNumber));
	}
}
