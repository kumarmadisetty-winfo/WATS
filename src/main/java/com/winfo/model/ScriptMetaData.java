package com.winfo.model;

import java.sql.Date;

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

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "WIN_TA_SCRIPT_METADATA")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "scriptMetaDataId")
public class ScriptMetaData {

	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "metadata_generator")
	@SequenceGenerator(name = "metadata_generator", sequenceName = "WIN_TA_SCRIPT_METADATA_SEQ", allocationSize = 1)
	@Id
	@Column(name = "SCRIPT_META_DATA_ID")
	private Integer scriptMetaDataId;

	@Column(name = "SCRIPT_NUMBER")
	private String scriptNumber;

	@Column(name = "LINE_NUMBER")
	private Integer lineNumber;

	@Column(name = "INPUT_PARAMETER")
	private String inputParameter;

	@Column(name = "ACTION")
	private String action;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATION_DATE")
	private Date creationDate;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Column(name = "UPDATE_DATE")
	private Date updateDate;

	@Column(name = "STEP_DESC")
	private String stepDesc;

	@Column(name = "FIELD_TYPE")
	private String fieldType;

	@Column(name = "HINT")
	private String hint;

	@Column(name = "DATATYPES")
	private String datatypes;

	@Column(name = "UNIQUE_MANDATORY")
	private String uniqueMandatory;

	@Column(name = "VALIDATION_TYPE")
	private String validationType;

	@Column(name = "VALIDATION_NAME")
	private String validationName;

	@Column(name = "METADATA_INPUT_VALUE")
	private String metadataInputvalue;

	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "SCRIPT_ID")
	private ScriptMaster scriptMaster;

	public ScriptMaster getScriptMaster() {
		return scriptMaster;
	}

	public void setScriptMaster(ScriptMaster scriptMaster) {
		this.scriptMaster = scriptMaster;
	}

	public String getMetadataInputvalue() {
		return metadataInputvalue;
	}

	public void setMetadataInputvalue(String metadataInputvalue) {
		this.metadataInputvalue = metadataInputvalue;
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
