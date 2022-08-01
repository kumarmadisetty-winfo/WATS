package com.winfo.vo;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.winfo.model.TestSetScriptParam;

public class WatsTestSetParamVO {

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
	@JsonProperty("lineExecutionStatus")
	private String lineExecutionStatus;
	@JsonProperty("inputValue")
	private String inputValue;
	@JsonProperty("testRunParamDesc")
	private String testRunParamDesc;
	@JsonProperty("lineErrorMessage")
	private String lineErrorMessage;
	@JsonProperty("testRunParamName")
	private String testRunParamName;
	@JsonProperty("lastUpdatedBy")
	private String lastUpdatedBy;
	@JsonProperty("metadataId")
	private Integer metadataId;
	@JsonProperty("dataTypes")
	private String dataTypes;

	public WatsTestSetParamVO() {
	}

	public WatsTestSetParamVO(TestSetScriptParam testSetScriptParam) {
		this.scriptNumber = testSetScriptParam.getScriptNumber();
		this.lineErrorMessage = testSetScriptParam.getLineErrorMessage();
		this.lineNumber = testSetScriptParam.getLineNumber();
		this.inputParameter = testSetScriptParam.getInputParameter();
		this.action = testSetScriptParam.getAction();
		this.xpathLocation = testSetScriptParam.getXpathLocation();
		this.xpathLocation1 = testSetScriptParam.getXpathLocation1();
		this.testRunParamName = testSetScriptParam.getTestRunParamName();
		this.testRunParamDesc = testSetScriptParam.getTestRunParamDesc();
		this.createdBy = testSetScriptParam.getCreatedBy();
		this.lastUpdatedBy = null;
		this.updateDate = null;
		this.inputValue = testSetScriptParam.getInputValue();
		this.metadataId = testSetScriptParam.getMetadataId();
		this.hint = testSetScriptParam.getHint();
		this.fieldType = testSetScriptParam.getFieldType();
		this.dataTypes = testSetScriptParam.getDataTypes();
		this.lineExecutionStatus = testSetScriptParam.getLineExecutionStatus();
		this.uniqueMandatory = testSetScriptParam.getUniqueMandatory();
		this.validationType = testSetScriptParam.getValidationType();
		this.validationName = testSetScriptParam.getValidationName();

	}

	public WatsTestSetParamVO(TestSetScriptParam testSetScriptParam, Integer testRunScriptParamId) {
		this.scriptMetaDataId = testRunScriptParamId;
		this.scriptNumber = testSetScriptParam.getScriptNumber();
		this.lineErrorMessage = testSetScriptParam.getLineErrorMessage();
		this.lineNumber = testSetScriptParam.getLineNumber();
		this.inputParameter = testSetScriptParam.getInputParameter();
		this.action = testSetScriptParam.getAction();
		this.xpathLocation = testSetScriptParam.getXpathLocation();
		this.xpathLocation1 = testSetScriptParam.getXpathLocation1();
		this.testRunParamName = testSetScriptParam.getTestRunParamName();
		this.testRunParamDesc = testSetScriptParam.getTestRunParamDesc();
		this.createdBy = testSetScriptParam.getCreatedBy();
		this.lastUpdatedBy = null;
		this.creationDate = null;
		this.updateDate = null;
		this.inputValue = testSetScriptParam.getInputValue();
		this.metadataId = testSetScriptParam.getMetadataId();
		this.hint = testSetScriptParam.getHint();
		this.fieldType = testSetScriptParam.getFieldType();
		this.dataTypes = testSetScriptParam.getDataTypes();
		this.lineExecutionStatus = testSetScriptParam.getLineExecutionStatus();
		this.uniqueMandatory = testSetScriptParam.getUniqueMandatory();
		this.validationType = testSetScriptParam.getValidationType();
		this.validationName = testSetScriptParam.getValidationName();

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

	public String getLineExecutionStatus() {
		return lineExecutionStatus;
	}

	public void setLineExecutionStatus(String lineExecutionStatus) {
		this.lineExecutionStatus = lineExecutionStatus;
	}

	public String getInputValue() {
		return inputValue;
	}

	public void setInputValue(String inputValue) {
		this.inputValue = inputValue;
	}

	public String getTestRunParamDesc() {
		return testRunParamDesc;
	}

	public void setTestRunParamDesc(String testRunParamDesc) {
		this.testRunParamDesc = testRunParamDesc;
	}

	public String getLineErrorMessage() {
		return lineErrorMessage;
	}

	public void setLineErrorMessage(String lineErrorMessage) {
		this.lineErrorMessage = lineErrorMessage;
	}

	public String getTestRunParamName() {
		return testRunParamName;
	}

	public void setTestRunParamName(String testRunParamName) {
		this.testRunParamName = testRunParamName;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Integer getMetadataId() {
		return metadataId;
	}

	public void setMetadataId(Integer metadataId) {
		this.metadataId = metadataId;
	}

	public String getDataTypes() {
		return dataTypes;
	}

	public void setDataTypes(String dataTypes) {
		this.dataTypes = dataTypes;
	}

	public Integer getScriptMetaDataId() {
		return scriptMetaDataId;
	}

	public void setScriptMetaDataId(Integer scriptMetaDataId) {
		this.scriptMetaDataId = scriptMetaDataId;
	}

}
