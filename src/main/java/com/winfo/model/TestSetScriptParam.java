package com.winfo.model;



import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "WIN_TA_TEST_SET_SCRIPT_PARAM")

public class TestSetScriptParam {

	@Id
	@GeneratedValue
	@Column(name = "TEST_SCRIPT_PARAM_ID")
	private Integer test_script_param_id;
	
	@Column(name = "LINE_ERROR_MESSAGE")
	private String line_error_message;

	@Column(name = "SCRIPT_ID")
	private Integer script_id;
	
	@Column(name = "SCRIPT_NUMBER")
	private String script_number;
	
	@Column(name = "LINE_NUMBER")
	private Integer line_number;
	
	@Column(name = "INPUT_PARAMETER")
	private String input_parameter;
	
	@Column(name = "ACTION")
	private String action;
	
	@Column(name = "XPATH_LOCATION")
	private String xpath_location;
	
	@Column(name = "XPATH_LOCATION1")
	private String xpath_location1;
	
	@Column(name = "TEST_RUN_PARAM_NAME")
	private String test_run_param_name;
	
	@Column(name = "TEST_RUN_PARAM_DESC")
	private String test_run_param_desc;
	
	@Column(name = "CREATED_BY")
	private String created_by;
	
	@Column(name = "LAST_UPDATED_BY")
	private String last_updated_by;
	
	@Column(name = "CREATION_DATE")
	private Date creation_date;
	
	@Column(name = "UPDATE_DATE")
	private Date update_date;
	
	@Column(name = "INPUT_VALUE")
	private String input_value;
	
	@Column(name = "METADATA_ID")
	private Integer metadata_id;
	
	@Column(name = "HINT")
	private String hint;
	
	@Column(name = "FIELD_TYPE")
	private String field_type;
	
	@Column(name = "DATATYPES")
	private String data_types;
	
	
	@Column(name = "LINE_EXECUTION_STATUS")
	private String line_execution_status;
	
	
	@Column(name = "UNIQUE_MANDATORY")
	private String unique_mandatory;

	@Column(name = "VALIDATION_TYPE")
	private String validation_type;
	
	@Column(name = "VALIDATION_NAME")
	private String validation_name;
	
	@ManyToOne(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "TEST_SET_LINE_ID")
	
	
	private TestSetLines testSetLines;

	
	public TestSetLines getTestSetLines() {
		return testSetLines;
	}

	public void setTestSetLines(TestSetLines testSetLines) {
		this.testSetLines = testSetLines;
	}

	public Integer getTest_script_param_id() {
		return test_script_param_id;
	}

	public void setTest_script_param_id(Integer test_script_param_id) {
		this.test_script_param_id = test_script_param_id;
	}

	public String getLine_error_message() {
		return line_error_message;
	}

	public void setLine_error_message(String line_error_message) {
		this.line_error_message = line_error_message;
	}

	public Integer getScript_id() {
		return script_id;
	}

	public void setScript_id(Integer script_id) {
		this.script_id = script_id;
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

	public String getTest_run_param_name() {
		return test_run_param_name;
	}

	public void setTest_run_param_name(String test_run_param_name) {
		this.test_run_param_name = test_run_param_name;
	}

	public String getTest_run_param_desc() {
		return test_run_param_desc;
	}

	public void setTest_run_param_desc(String test_run_param_desc) {
		this.test_run_param_desc = test_run_param_desc;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public String getLast_updated_by() {
		return last_updated_by;
	}

	public void setLast_updated_by(String last_updated_by) {
		this.last_updated_by = last_updated_by;
	}

	public Date getCreation_date() {
		return creation_date;
	}

	public void setCreation_date(Date creation_date) {
		this.creation_date = creation_date;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	public String getInput_value() {
		return input_value;
	}

	public void setInput_value(String input_value) {
		this.input_value = input_value;
	}

	public Integer getMetadata_id() {
		return metadata_id;
	}

	public void setMetadata_id(Integer metadata_id) {
		this.metadata_id = metadata_id;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public String getField_type() {
		return field_type;
	}

	public void setField_type(String field_type) {
		this.field_type = field_type;
	}

	public String getData_types() {
		return data_types;
	}

	public void setData_types(String data_types) {
		this.data_types = data_types;
	}

	public String getLine_execution_statues() {
		return line_execution_status;
	}

	public void setLine_execution_statues(String line_execution_status) {
		this.line_execution_status = line_execution_status;
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

		
	
}
