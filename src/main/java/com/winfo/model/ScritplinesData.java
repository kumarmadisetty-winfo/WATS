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
public class ScritplinesData {
	@Id
	@Column(name = "TEST_SCRIPT_PARAM_ID")
	private int testscriptperamid;
	
	@Column(name = "SCRIPT_ID")
	private int script_id;
	
	@Column(name = "SCRIPT_NUMBER")
	private String script_number;
	
	@Column(name = "LINE_NUMBER")
	private int line_number;
	
	@Column(name = "INPUT_PARAMETER")
	private String input_parameter;
	
	@Column(name = "ACTION")
	private String action;
	
	@Column(name = "XPATH_LOCATION")
	private String xpathlocation;
	
	@Column(name = "XPATH_LOCATION1")
	private String xpathlocation1;
	
	@Column(name = "TEST_RUN_PARAM_NAME")
	private String test_run_param_name;
	
	@Column(name = "TEST_RUN_PARAM_DESC")
	private String test_run_param_desc;
	
	@Column(name = "CREATED_BY")
	private String createdby;
	
	@Column(name = "LAST_UPDATED_BY")
	private String lastupdatedby;
	
	@Column(name = "CREATION_DATE")
	private Date creationdate;
	
	@Column(name = "UPDATE_DATE")
	private Date updateddate;
	
	@Column(name = "INPUT_VALUE")
	private String input_value;
	
	@Column(name = "METADATA_ID")
	private int metadata_id;
	
	@Column(name = "HINT")
	private String hint;
	
	@Column(name = "FIELD_TYPE")
	private String field_type;
	
	@Column(name = "DATATYPES")
	private String datatypes;
	
	
	@Column(name = "LINE_EXECUTION_STATUS")
	private String lineexecutionstatues;
	
	@Column(name = "LINE_ERROR_MESSAGE")
	private String lineerrormessage;
	
	@Column(name = "UNIQUE_MANDATORY")
	private String uniquemandatory;

	public String getDatatypes() {
		return datatypes;
	}
	public void setDatatypes(String datatypes) {
		this.datatypes = datatypes;
	}
	public String getUniquemandatory() {
		return uniquemandatory;
	}
	public void setUniquemandatory(String uniquemandatory) {
		this.uniquemandatory = uniquemandatory;
	}

	@ManyToOne(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "TEST_SET_LINE_ID")
	private ScriptsData Scriptsdata;

	public int getTestscriptperamid() {
		return testscriptperamid;
	}
	public ScriptsData getScriptsdata() {
		return Scriptsdata;
	}

	public void setScriptsdata(ScriptsData scriptsdata) {
		Scriptsdata = scriptsdata;
	}

	public void setTestscriptperamid(int testscriptperamid) {
		this.testscriptperamid = testscriptperamid;
	}

	public int getScript_id() {
		return script_id;
	}

	public void setScript_id(int script_id) {
		this.script_id = script_id;
	}

	
	public String getScript_number() {
		return script_number;
	}

	public void setScript_number(String script_number) {
		this.script_number = script_number;
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


	public int getLine_number() {
		return line_number;
	}
	public void setLine_number(int line_number) {
		this.line_number = line_number;
	}
	public String getXpathlocation() {
		return xpathlocation;
	}
	public void setXpathlocation(String xpathlocation) {
		this.xpathlocation = xpathlocation;
	}
	public String getXpathlocation1() {
		return xpathlocation1;
	}
	public void setXpathlocation1(String xpathlocation1) {
		this.xpathlocation1 = xpathlocation1;
	}
	public String getCreatedby() {
		return createdby;
	}
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}
	public String getLastupdatedby() {
		return lastupdatedby;
	}
	public void setLastupdatedby(String lastupdatedby) {
		this.lastupdatedby = lastupdatedby;
	}
	public Date getCreationdate() {
		return creationdate;
	}
	public void setCreationdate(Date creationdate) {
		this.creationdate = creationdate;
	}
	public Date getUpdateddate() {
		return updateddate;
	}
	public void setUpdateddate(Date updateddate) {
		this.updateddate = updateddate;
	}
	public int getMetadata_id() {
		return metadata_id;
	}
	public void setMetadata_id(int metadata_id) {
		this.metadata_id = metadata_id;
	}
	public String getLineexecutionstatues() {
		return lineexecutionstatues;
	}
	public void setLineexecutionstatues(String lineexecutionstatues) {
		this.lineexecutionstatues = lineexecutionstatues;
	}
	public String getLineerrormessage() {
		return lineerrormessage;
	}
	public void setLineerrormessage(String lineerrormessage) {
		this.lineerrormessage = lineerrormessage;
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
	
}