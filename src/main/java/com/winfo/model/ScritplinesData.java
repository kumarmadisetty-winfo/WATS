package com.winfo.model;

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
@Table(name = "WIN_TA_TEST_SET_SCRIPT_PARAM_BKP")
public class ScritplinesData {
	@Id
	@GeneratedValue
	@Column(name = "TEST_SCRIPT_PARAM_ID")
	private String testscriptperamid;
	
	@Column(name = "SCRIPT_ID")
	private String script_id;
	
	@Column(name = "SCRIPT_NUMBER")
	private String script_number;
	
	@Column(name = "LINE_NUMBER")
	private String line_number;
	
	@Column(name = "INPUT_PARAMETER")
	private String input_parameter;
	
	@Column(name = "ACTION")
	private String action;
	
	@Column(name = "TEST_RUN_PARAM_NAME")
	private String test_run_param_name;
	
	@Column(name = "TEST_RUN_PARAM_DESC")
	private String test_run_param_desc;
	
	@Column(name = "INPUT_VALUE")
	private String input_value;
	
	@Column(name = "METADATA_ID")
	private String metadata_id;
	
	@Column(name = "HINT")
	private String hint;
	
	@Column(name = "FIELD_TYPE")
	private String field_type;
	


	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "TEST_SET_LINE_ID")
	private ScriptsData Scriptsdata;

	public String getTestscriptperamid() {
		return testscriptperamid;
	}
	public ScriptsData getScriptsdata() {
		return Scriptsdata;
	}

	public void setScriptsdata(ScriptsData scriptsdata) {
		Scriptsdata = scriptsdata;
	}

	public void setTestscriptperamid(String testscriptperamid) {
		this.testscriptperamid = testscriptperamid;
	}

	public String getScript_id() {
		return script_id;
	}

	public void setScript_id(String script_id) {
		this.script_id = script_id;
	}

	
	public String getScript_number() {
		return script_number;
	}

	public void setScript_number(String script_number) {
		this.script_number = script_number;
	}

	public String getLine_number() {
		return line_number;
	}

	public void setLine_number(String line_number) {
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

	public String getInput_value() {
		return input_value;
	}

	public void setInput_value(String input_value) {
		this.input_value = input_value;
	}

	public String getMetadata_id() {
		return metadata_id;
	}

	public void setMetadata_id(String metadata_id) {
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
	@Override
	public String toString() {
		return "ScritplinesData [testscriptperamid=" + testscriptperamid + ", script_id=" + script_id
				+ ", script_number=" + script_number + ", line_number=" + line_number + ", input_parameter="
				+ input_parameter + ", action=" + action + ", test_run_param_name=" + test_run_param_name
				+ ", test_run_param_desc=" + test_run_param_desc + ", input_value=" + input_value + ", metadata_id="
				+ metadata_id + ", hint=" + hint + ", field_type=" + field_type + ", Scriptsdata=" + Scriptsdata + "]";
	}
}
