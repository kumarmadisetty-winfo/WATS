package com.winfo.model;



import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "WIN_TA_TEST_SET_SCRIPT_PARAM")

public class TestSetScriptParam {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "testRunScriptParam_generator")
	@SequenceGenerator(name = "testRunScriptParam_generator", sequenceName = "WIN_TA_PARAM_ID_SEQ", allocationSize = 1)
	@Column(name = "TEST_SCRIPT_PARAM_ID", nullable = false,unique = true)
	private Integer testRunScriptParamId;

	@Column(name = "SCRIPT_ID")
	
	private Integer scriptId;

	@Column(name = "SCRIPT_NUMBER")
	@NotEmpty(message="Script Number can not be null")
	private String scriptNumber;

	@Column(name = "LINE_ERROR_MESSAGE")
	private String lineErrorMessage;

	@Column(name = "LINE_NUMBER")
	private Integer lineNumber;

	@Column(name = "INPUT_PARAMETER")
	private String inputParameter;

	@Column(name = "ACTION")
	private String action;

	@Column(name = "XPATH_LOCATION")
	private String xpathLocation;

	@Column(name = "XPATH_LOCATION1")
	private String xpathLocation1;

	@Column(name = "TEST_RUN_PARAM_NAME")
	private String testRunParamName;

	@Column(name = "TEST_RUN_PARAM_DESC")
	private String testRunParamDesc;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@Column(name = "CREATION_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	@Column(name = "UPDATE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;

	@Column(name = "INPUT_VALUE")
	private String inputValue;

	@Column(name = "METADATA_ID")
	private Integer metadataId;

	@Column(name = "HINT")
	private String hint;

	@Column(name = "FIELD_TYPE")
	private String fieldType;

	@Column(name = "DATATYPES")
	private String dataTypes;

	@Column(name = "LINE_EXECUTION_STATUS")
	private String lineExecutionStatus;

	@Column(name = "UNIQUE_MANDATORY")
	private String uniqueMandatory;

	@Column(name = "VALIDATION_TYPE")
	private String validationType;

	@Column(name = "VALIDATION_NAME")
	private String validationName;

	@Column(name = "SCREENSHOT")
    private byte[] screenshot;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)

	@JoinColumn(name = "TEST_SET_LINE_ID" ,nullable = false)
	private TestSetLine testSetLine;

	public Integer getTestRunScriptParamId() {
		return testRunScriptParamId;
	}

	public void setTestRunScriptParamId(Integer testRunScriptParamId) {
		this.testRunScriptParamId = testRunScriptParamId;
	}

	public Integer getScriptId() {
		return scriptId;
	}

	public void setScriptId(Integer scriptId) {
		this.scriptId = scriptId;
	}

	public String getScriptNumber() {
		return scriptNumber;
	}

	public void setScriptNumber(String scriptNumber) {
		this.scriptNumber = scriptNumber;
	}

	public String getLineErrorMessage() {
		return lineErrorMessage;
	}

	public void setLineErrorMessage(String lineErrorMessage) {
		this.lineErrorMessage = lineErrorMessage;
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

	public String getTestRunParamName() {
		return testRunParamName;
	}

	public void setTestRunParamName(String testRunParamName) {
		this.testRunParamName = testRunParamName;
	}

	public String getTestRunParamDesc() {
		return testRunParamDesc;
	}

	public void setTestRunParamDesc(String testRunParamDesc) {
		this.testRunParamDesc = testRunParamDesc;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getInputValue() {
		return inputValue;
	}

	public void setInputValue(String inputValue) {
		this.inputValue = inputValue;
	}

	public Integer getMetadataId() {
		return metadataId;
	}

	public void setMetadataId(Integer metadataId) {
		this.metadataId = metadataId;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getDataTypes() {
		return dataTypes;
	}

	public void setDataTypes(String dataTypes) {
		this.dataTypes = dataTypes;
	}

	public String getLineExecutionStatus() {
		return lineExecutionStatus;
	}

	public void setLineExecutionStatus(String lineExecutionStatus) {
		this.lineExecutionStatus = lineExecutionStatus;
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

	public TestSetLine getTestRunScripts() {
		return testSetLine;
	}

	public void setTestRunScripts(TestSetLine testSetLine) {
		this.testSetLine = testSetLine;
	}

	public byte[] getScreenshot() {
		return screenshot;
	}

	public void setScreenshot(byte[] screenshot) {
		this.screenshot = screenshot;
	}	
	
}
