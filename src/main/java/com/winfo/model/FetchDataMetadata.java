package com.winfo.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FetchDataMetadata {

	@JsonProperty("line_number")
	private Integer lineNumber;

	@JsonProperty("action")
	private String action;

	@JsonProperty("input_parameter")
	private String inputParameter;

	@JsonProperty("xpath_location")
	private String xpathLocation;

	@JsonProperty("xpath_location1")
	private String xpathLocation1;

	@JsonProperty("created_by")
	private String createdBy;

	@JsonProperty("creation_date")
	private Date creationDate;

	@JsonProperty("updated_by")
	private String updatedBy;

	@JsonProperty("update_date")
	private Date updateDate;

	@JsonProperty("step_desc")
	private String stepDesc;

	@JsonProperty("field_type")
	private String fieldType;

	@JsonProperty("hint")
	private String hint;

	@JsonProperty("datatypes")
	private String datatypes;

	@JsonProperty("unique_mandatory")
	private String uniqueMandatory;

	@JsonProperty("validation_type")
	private String validationType;

	@JsonProperty("validation_name")
	private String validationName;

	@JsonProperty("script_number")
	private String scriptNumber;

	public Integer getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getInputParameter() {
		return inputParameter;
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

	public String getScriptNumber() {
		return scriptNumber;
	}

	public void setScriptNumber(String scriptNumber) {
		this.scriptNumber = scriptNumber;
	}

	public void setInputParameter(String inputParameter) {
		this.inputParameter = inputParameter;
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
