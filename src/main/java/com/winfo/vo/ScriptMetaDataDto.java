package com.winfo.vo;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.winfo.model.AuditScriptExecTrail;
import com.winfo.model.ScriptMetaData;
import com.winfo.utils.Constants.AUDIT_TRAIL_STAGES;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScriptMetaDataDto {

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
	
	public ScriptMetaDataDto(ScriptMetaData scriptMetaData) {
		this.setAction(scriptMetaData.getAction());
		this.setCreatedBy(scriptMetaData.getCreatedBy());
		this.setCreationDate(scriptMetaData.getCreationDate());
		this.setDatatypes(scriptMetaData.getDatatypes());
		this.setFieldType(scriptMetaData.getFieldType());
		this.setHint(scriptMetaData.getHint());
		this.setInputParameter(scriptMetaData.getInputParameter());
		this.setLineNumber(scriptMetaData.getLineNumber());
		this.setMetadataInputValue(scriptMetaData.getMetadataInputvalue());
		this.setScriptMetaDataId(scriptMetaData.getScriptMetaDataId());
		this.setScriptNumber(scriptMetaData.getScriptNumber());
		this.setStepDesc(scriptMetaData.getStepDesc());
		this.setUniqueMandatory(scriptMetaData.getUniqueMandatory());
		this.setUpdateDate(scriptMetaData.getUpdateDate());
		this.setUpdatedBy(scriptMetaData.getUpdatedBy());
		this.setValidationName(scriptMetaData.getValidationName());
		this.setValidationType(scriptMetaData.getValidationType());
	}

}
