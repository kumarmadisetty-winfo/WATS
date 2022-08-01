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

	public String getScript_number() {
		return scriptNumber;
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

	public String getLine_execution_status() {
		return lineExecutionStatus;
	}

	public void setLine_execution_status(String lineExecutionStatus) {
		this.lineExecutionStatus = lineExecutionStatus;
	}

	public String getInput_value() {
		return inputValue;
	}

	public void setInput_value(String inputValue) {
		this.inputValue = inputValue;
	}

	public String getTest_run_param_desc() {
		return testRunParamDesc;
	}

	public void setTest_run_param_desc(String testRunParamDesc) {
		this.testRunParamDesc = testRunParamDesc;
	}

	public String getLine_error_message() {
		return lineErrorMessage;
	}

	public void setLine_error_message(String lineErrorMessage) {
		this.lineErrorMessage = lineErrorMessage;
	}

	public String getTest_run_param_name() {
		return testRunParamName;
	}

	public void setTest_run_param_name(String testRunParamName) {
		this.testRunParamName = testRunParamName;
	}

	public String getLast_updated_by() {
		return lastUpdatedBy;
	}

	public void setLast_updated_by(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Integer getMetadata_id() {
		return metadataId;
	}

	public void setMetadata_id(Integer metadataId) {
		this.metadataId = metadataId;
	}

	public String getData_types() {
		return dataTypes;
	}

	public void setData_types(String dataTypes) {
		this.dataTypes = dataTypes;
	}

	public Integer getScript_meta_data_id() {
		return scriptMetaDataId;
	}

	public void setScript_meta_data_id(Integer scriptMetaDataId) {
		this.scriptMetaDataId = scriptMetaDataId;
	}

}
