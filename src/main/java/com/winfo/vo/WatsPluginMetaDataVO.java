package com.winfo.vo;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WatsPluginMetaDataVO {

	@JsonProperty("SCRIPT META DATA ID")
	private Integer scriptMetaDataId;

	@JsonProperty("SCRIPT NUMBER")
	private String scriptNumber;

	@JsonProperty("LINE NUMBER")
	private Integer lineNumber;

	@JsonProperty("INPUT PARAMETER")
	private String inputParameter;

	@JsonProperty("ACTION")
	private String action;

	@JsonProperty("XPATH_LOCATION")
	private String xpathLocation;

	@JsonProperty("XPATH_LOCATION1")
	private String xpathLocation1;

	@JsonProperty("CREATED_BY")
	private String createdBy;

	@JsonProperty("CREATION_DATE")
	private String creationDate;

	@JsonProperty("UPDATED_BY")
	private String updatedBy;

	@JsonProperty("UPDATE_DATE")
	private Date updateDate;

	@JsonProperty("STEP DESCRIPTION")
	private String stepDesc;

	@JsonProperty("FIELD_TYPE")
	private String fieldType;

	@JsonProperty("HINT")
	private String hint;

	@JsonProperty("INPUT VALUE")
	private String inputValue;

	public String getInputValue() {
		return inputValue;
	}

	public void setInputValue(String inputValue) {
		this.inputValue = inputValue;
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
