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
		this.setCreated_by(scriptMetaData.getCreated_by());
		this.setCreation_date(scriptMetaData.getCreation_date());
		this.setDatatypes(scriptMetaData.getDatatypes());
		this.setField_type(scriptMetaData.getField_type());
		this.setHint(scriptMetaData.getHint());
		this.setInput_parameter(scriptMetaData.getInput_parameter());
		this.setLine_number(scriptMetaData.getLine_number());
		this.setMetadataInputValue(scriptMetaData.getMetadata_inputvalue());
		this.setScript_meta_data_id(scriptMetaData.getScript_meta_data_id());
		this.setScript_number(scriptMetaData.getScript_number());
		this.setStep_desc(scriptMetaData.getStep_desc());
		this.setUnique_mandatory(scriptMetaData.getUnique_mandatory());
		this.setUpdate_date(scriptMetaData.getUpdate_date());
		this.setUpdated_by(scriptMetaData.getUpdated_by());
		this.setValidation_name(scriptMetaData.getValidation_name());
		this.setValidation_type(scriptMetaData.getValidation_type());
		this.setXpath_location(scriptMetaData.getXpath_location());
		this.setXpath_location1(scriptMetaData.getXpath_location1());
	}

	public String getMetadataInputValue() {
		return metadataInputValue;
	}

	public void setMetadataInputValue(String metadataInputValue) {
		this.metadataInputValue = metadataInputValue;
	}

	public Integer getScript_meta_data_id() {
		return scriptMetaDataId;
	}

	public void setScript_meta_data_id(Integer scriptMetaDataId) {
		this.scriptMetaDataId = scriptMetaDataId;
	}

	public String getScript_number() {
		return scriptNumber;
	}

	public String getDatatypes() {
		return datatypes;
	}

	public void setDatatypes(String datatypes) {
		this.datatypes = datatypes;
	}

	public String getUnique_mandatory() {
		return uniqueMandatory;
	}

	public void setUnique_mandatory(String uniqueMandatory) {
		this.uniqueMandatory = uniqueMandatory;
	}

	public String getValidation_type() {
		return validationType;
	}

	public void setValidation_type(String validationType) {
		this.validationType = validationType;
	}

	public String getValidation_name() {
		return validationName;
	}

	public void setValidation_name(String validationName) {
		this.validationName = validationName;
	}

	public void setScript_number(String scriptNumber) {
		this.scriptNumber = scriptNumber;
	}

	public Integer getLine_number() {
		return lineNumber;
	}

	public void setLine_number(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getInput_parameter() {
		return inputParameter;
	}

	public void setInput_parameter(String inputParameter) {
		this.inputParameter = inputParameter;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getXpath_location() {
		return xpathLocation;
	}

	public void setXpath_location(String xpathLocation) {
		this.xpathLocation = xpathLocation;
	}

	public String getXpath_location1() {
		return xpathLocation1;
	}

	public void setXpath_location1(String xpathLocation1) {
		this.xpathLocation1 = xpathLocation1;
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

	public String getStep_desc() {
		return stepDesc;
	}

	public void setStep_desc(String stepDesc) {
		this.stepDesc = stepDesc;
	}

	public String getField_type() {
		return fieldType;
	}

	public void setField_type(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

}
