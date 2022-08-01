package com.winfo.vo;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.winfo.model.TestSetScriptParam;

public class WatsTestSetParamVO {

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
	@JsonProperty("line_execution_status")
	private String line_execution_status;
	@JsonProperty("input_value")
	private String input_value;
	@JsonProperty("test_run_param_desc")
	private String test_run_param_desc;
	@JsonProperty("line_error_message")
	private String line_error_message;
	@JsonProperty("test_run_param_name")
	private String test_run_param_name;
	@JsonProperty("last_updated_by")
	private String last_updated_by;
	@JsonProperty("metadata_id")
	private Integer metadata_id;
	@JsonProperty("data_types")
	private String data_types;

	public WatsTestSetParamVO() {
	}

	public WatsTestSetParamVO(TestSetScriptParam testSetScriptParam) {
		this.script_number = testSetScriptParam.getScriptNumber();
		this.line_error_message = testSetScriptParam.getLineErrorMessage();
		this.line_number = testSetScriptParam.getLineNumber();
		this.input_parameter = testSetScriptParam.getInputParameter();
		this.action = testSetScriptParam.getAction();
		this.xpath_location = testSetScriptParam.getXpathLocation();
		this.xpath_location1 = testSetScriptParam.getXpathLocation1();
		this.test_run_param_name = testSetScriptParam.getTestRunParamName();
		this.test_run_param_desc = testSetScriptParam.getTestRunParamDesc();
		this.created_by = testSetScriptParam.getCreatedBy();
		this.last_updated_by = null;
		this.update_date = null;
		this.input_value = testSetScriptParam.getInputValue();
		this.metadata_id = testSetScriptParam.getMetadataId();
		this.hint = testSetScriptParam.getHint();
		this.field_type = testSetScriptParam.getFieldType();
		this.data_types = testSetScriptParam.getDataTypes();
		this.line_execution_status = testSetScriptParam.getLineExecutionStatus();
		this.unique_mandatory = testSetScriptParam.getUniqueMandatory();
		this.validation_type = testSetScriptParam.getValidationType();
		this.validation_name = testSetScriptParam.getValidationName();

	}

	public WatsTestSetParamVO(TestSetScriptParam testSetScriptParam, Integer testRunScriptParamId) {
		this.script_meta_data_id = testRunScriptParamId;
		this.script_number = testSetScriptParam.getScriptNumber();
		this.line_error_message = testSetScriptParam.getLineErrorMessage();
		this.line_number = testSetScriptParam.getLineNumber();
		this.input_parameter = testSetScriptParam.getInputParameter();
		this.action = testSetScriptParam.getAction();
		this.xpath_location = testSetScriptParam.getXpathLocation();
		this.xpath_location1 = testSetScriptParam.getXpathLocation1();
		this.test_run_param_name = testSetScriptParam.getTestRunParamName();
		this.test_run_param_desc = testSetScriptParam.getTestRunParamDesc();
		this.created_by = testSetScriptParam.getCreatedBy();
		this.last_updated_by = null;
		this.creation_date = null;
		this.update_date = null;
		this.input_value = testSetScriptParam.getInputValue();
		this.metadata_id = testSetScriptParam.getMetadataId();
		this.hint = testSetScriptParam.getHint();
		this.field_type = testSetScriptParam.getFieldType();
		this.data_types = testSetScriptParam.getDataTypes();
		this.line_execution_status = testSetScriptParam.getLineExecutionStatus();
		this.unique_mandatory = testSetScriptParam.getUniqueMandatory();
		this.validation_type = testSetScriptParam.getValidationType();
		this.validation_name = testSetScriptParam.getValidationName();

	}

	public String getScript_number() {
		return script_number;
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

	public String getLine_execution_status() {
		return line_execution_status;
	}

	public void setLine_execution_status(String line_execution_status) {
		this.line_execution_status = line_execution_status;
	}

	public String getInput_value() {
		return input_value;
	}

	public void setInput_value(String input_value) {
		this.input_value = input_value;
	}

	public String getTest_run_param_desc() {
		return test_run_param_desc;
	}

	public void setTest_run_param_desc(String test_run_param_desc) {
		this.test_run_param_desc = test_run_param_desc;
	}

	public String getLine_error_message() {
		return line_error_message;
	}

	public void setLine_error_message(String line_error_message) {
		this.line_error_message = line_error_message;
	}

	public String getTest_run_param_name() {
		return test_run_param_name;
	}

	public void setTest_run_param_name(String test_run_param_name) {
		this.test_run_param_name = test_run_param_name;
	}

	public String getLast_updated_by() {
		return last_updated_by;
	}

	public void setLast_updated_by(String last_updated_by) {
		this.last_updated_by = last_updated_by;
	}

	public Integer getMetadata_id() {
		return metadata_id;
	}

	public void setMetadata_id(Integer metadata_id) {
		this.metadata_id = metadata_id;
	}

	public String getData_types() {
		return data_types;
	}

	public void setData_types(String data_types) {
		this.data_types = data_types;
	}

	public Integer getScript_meta_data_id() {
		return script_meta_data_id;
	}

	public void setScript_meta_data_id(Integer script_meta_data_id) {
		this.script_meta_data_id = script_meta_data_id;
	}

	@Override
	public String toString() {
		return this.line_execution_status + " " + this.creation_date + " " + this.unique_mandatory + " "
				+ this.created_by + " " + this.input_value + " " + this.xpath_location1 + " " + this.test_run_param_desc
				+ " " + this.line_error_message + " " + this.test_run_param_name + " " + this.validation_type + " "
				+ this.xpath_location1 + " " + this.line_number+" "+this.metadata_id
				+" "+this.action+" "+this.script_number+" "+this.input_parameter+ " "+this.script_meta_data_id;
	}

}
