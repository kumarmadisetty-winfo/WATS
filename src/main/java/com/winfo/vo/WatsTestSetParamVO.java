package com.winfo.vo;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.winfo.model.TestSetScriptParam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
	private String dataType;
	
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
		this.dataType = testSetScriptParam.getDataTypes();
		this.lineExecutionStatus = testSetScriptParam.getLineExecutionStatus();
		this.uniqueMandatory = testSetScriptParam.getUniqueMandatory();
		this.validationType = testSetScriptParam.getValidationType();
		this.validationName = testSetScriptParam.getValidationName();

	}


}
