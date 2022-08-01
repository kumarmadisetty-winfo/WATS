package com.winfo.vo;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.winfo.model.ScriptMetaData;

public class ScriptMetaDataDto {

	// SCRIPTMETADATA TABLE

	@JsonProperty("scriptMetaDataId")
	private Integer scriptMetaDataId;
	@JsonProperty("scriptNumber")
	private String scriptNumber;
	@JsonProperty("lineNumber")
	private Integer lineNumber;
	@JsonProperty("inputParameter")
	private String inputParameter;
	@JsonProperty("action")
	private String action;
	@JsonProperty("xpathLocation")
	private String xpathLocation;
	@JsonProperty("xpathLocation1")
	private String xpathLocation1;
	@JsonProperty("createdBy")
	private String createdBy;
	@JsonProperty("creationDate")
	private Date creationDate;
	@JsonProperty("updatedBy")
	private String updatedBy;
	@JsonProperty("updateDate")
	private Date updateDate;
	@JsonProperty("stepDesc")
	private String stepDesc;
	@JsonProperty("fieldType")
	private String fieldType;
	@JsonProperty("hint")
	private String hint;
	@JsonProperty("datatypes")
	private String datatypes;
	@JsonProperty("uniqueMandatory")
	private String uniqueMandatory;
	@JsonProperty("validationType")
	private String validationType;
	@JsonProperty("validationName")
	private String validationName;
	@JsonProperty("metadataInputValue")
	private String metadataInputValue;

	public ScriptMetaDataDto() {
	}

	public ScriptMetaDataDto(ScriptMetaData scriptMetaData) {
		this.setAction(scriptMetaData.getAction());
		this.setCreatedBy(scriptMetaData.getCreated_by());
		this.setCreationDate(scriptMetaData.getCreation_date());
		this.setDatatypes(scriptMetaData.getDatatypes());
		this.setFieldType(scriptMetaData.getField_type());
		this.setHint(scriptMetaData.getHint());
		this.setInputParameter(scriptMetaData.getInput_parameter());
		this.setLineNumber(scriptMetaData.getLine_number());
		this.setMetadataInputValue(scriptMetaData.getMetadata_inputvalue());
		this.setScriptMetaDataId(scriptMetaData.getScript_meta_data_id());
		this.setScriptNumber(scriptMetaData.getScript_number());
		this.setStepDesc(scriptMetaData.getStep_desc());
		this.setUniqueMandatory(scriptMetaData.getUnique_mandatory());
		this.setUpdateDate(scriptMetaData.getUpdate_date());
		this.setUpdatedBy(scriptMetaData.getUpdated_by());
		this.setValidationName(scriptMetaData.getValidation_name());
		this.setValidationType(scriptMetaData.getValidation_type());
		this.setXpathLocation(scriptMetaData.getXpath_location());
		this.setXpathLocation1(scriptMetaData.getXpath_location1());
	}

	public String getMetadataInputValue() {
		return metadataInputValue;
	}

	public void setMetadataInputValue(String metadataInputValue) {
		this.metadataInputValue = metadataInputValue;
	}

	public Integer getScriptMetaDataId() {
		return scriptMetaDataId;
	}

	public void setScriptMetaDataId(Integer scriptMetaDataId) {
		this.scriptMetaDataId = scriptMetaDataId;
	}

	public String getScriptNumber() {
		return scriptNumber;
	}

	public String getDatatypes() {
		return datatypes;
	}

	public void setDatatypes(String datatypes) {
		this.datatypes = datatypes;
	}

	public String getUniqueMandatory() {
		return uniqueMandatory;
	}

	public void setUniqueMandatory(String uniqueMandatory) {
		this.uniqueMandatory = uniqueMandatory;
	}

	public String getValidationType() {
		return validationType;
	}

	public void setValidationType(String validationType) {
		this.validationType = validationType;
	}

	public String getValidationName() {
		return validationName;
	}

	public void setValidationName(String validationName) {
		this.validationName = validationName;
	}

	public void setScriptNumber(String scriptNumber) {
		this.scriptNumber = scriptNumber;
	}

	public Integer getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getInputParameter() {
		return inputParameter;
	}

	public void setInputParameter(String inputParameter) {
		this.inputParameter = inputParameter;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getXpathLocation() {
		return xpathLocation;
	}

	public void setXpathLocation(String xpathLocation) {
		this.xpathLocation = xpathLocation;
	}

	public String getXpathLocation1() {
		return xpathLocation1;
	}

	public void setXpathLocation1(String xpathLocation1) {
		this.xpathLocation1 = xpathLocation1;
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

	public String getStepDesc() {
		return stepDesc;
	}

	public void setStepDesc(String stepDesc) {
		this.stepDesc = stepDesc;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

}
