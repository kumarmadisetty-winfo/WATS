package com.winfo.vo;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.winfo.model.ScriptMetaData;

public class WatsMetaDataVO {

	// SCRIPTMETADATA TABLE

	@JsonProperty("script_meta_data_id")
	private Integer script_meta_data_id;
	@JsonProperty("script_number")
	private String script_number;
	@JsonProperty("line_number")
	private Integer line_number;
	@JsonProperty("input_parameter")
	private String input_parameter;
	@JsonProperty("action")
	private String action;
	@JsonProperty("xpath_location")
	private String xpath_location;
	@JsonProperty("xpath_location1")
	private String xpath_location1;
	@JsonProperty("created_by")
	private String created_by;
	@JsonProperty("creation_date")
	private Date creation_date;
	@JsonProperty("updated_by")
	private String updated_by;
	@JsonProperty("update_date")
	private Date update_date;
	@JsonProperty("step_desc")
	private String step_desc;
	@JsonProperty("field_type")
	private String field_type;
	@JsonProperty("hint")
	private String hint;
	@JsonProperty("datatypes")
	private String datatypes;
	@JsonProperty("unique_mandatory")
	private String unique_mandatory;
	@JsonProperty("validation_type")
	private String validation_type;
	@JsonProperty("validation_name")
	private String validation_name;
	@JsonProperty("metadata_input_value")
	private String metadataInputValue;

	public WatsMetaDataVO() {
	}

	public WatsMetaDataVO(ScriptMetaData scriptMetaData) {
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
		return script_meta_data_id;
	}

	public void setScript_meta_data_id(Integer script_meta_data_id) {
		this.script_meta_data_id = script_meta_data_id;
	}

	public String getScript_number() {
		return script_number;
	}

	public String getDatatypes() {
		return datatypes;
	}

	public void setDatatypes(String datatypes) {
		this.datatypes = datatypes;
	}

	public String getUnique_mandatory() {
		return unique_mandatory;
	}

	public void setUnique_mandatory(String unique_mandatory) {
		this.unique_mandatory = unique_mandatory;
	}

	public String getValidation_type() {
		return validation_type;
	}

	public void setValidation_type(String validation_type) {
		this.validation_type = validation_type;
	}

	public String getValidation_name() {
		return validation_name;
	}

	public void setValidation_name(String validation_name) {
		this.validation_name = validation_name;
	}

	public void setScript_number(String script_number) {
		this.script_number = script_number;
	}

	public Integer getLine_number() {
		return line_number;
	}

	public void setLine_number(Integer line_number) {
		this.line_number = line_number;
	}

	public String getInput_parameter() {
		return input_parameter;
	}

	public void setInput_parameter(String input_parameter) {
		this.input_parameter = input_parameter;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getXpath_location() {
		return xpath_location;
	}

	public void setXpath_location(String xpath_location) {
		this.xpath_location = xpath_location;
	}

	public String getXpath_location1() {
		return xpath_location1;
	}

	public void setXpath_location1(String xpath_location1) {
		this.xpath_location1 = xpath_location1;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public Date getCreation_date() {
		return creation_date;
	}

	public void setCreation_date(Date creation_date) {
		this.creation_date = creation_date;
	}

	public String getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(String updated_by) {
		this.updated_by = updated_by;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	public String getStep_desc() {
		return step_desc;
	}

	public void setStep_desc(String step_desc) {
		this.step_desc = step_desc;
	}

	public String getField_type() {
		return field_type;
	}

	public void setField_type(String field_type) {
		this.field_type = field_type;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	@Override
	public String toString() {
		return this.step_desc + " " + this.unique_mandatory + " " + this.created_by + " " + this.xpath_location1 + " "
				+ this.datatypes + " " + this.validation_type + " " + this.xpath_location + " " + this.line_number + " "
				+ this.hint + " " + this.script_meta_data_id + " " + this.updated_by + " " + this.action + " "
				+ this.script_number + " " + this.input_parameter + " " + this.field_type + " " + this.validation_name;
	}

}
