package com.winfo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
public class TestRunVO 
{

	
	
	@JsonProperty("test_set_id")
	private Integer test_set_id;
	
	@JsonProperty("test_set_line_id")
	private Integer test_set_line_id;
	
	@JsonProperty("script_id")
	private Integer script_id;
	
	@JsonProperty("seq_num")
	private Integer seq_num;

	@JsonProperty("issue_key")
	private String issue_key;

	@JsonProperty("test_set_name")
	private String  test_set_name;
	
	@JsonProperty("status")
	private String status;
	
	@JsonProperty("configuration_id")
	private Integer configuration_id;

	@JsonProperty("script_number")
	private String script_number;
	
	@JsonProperty("scenario_name")
	private String scenario_name;

	

	public String getScenario_name() {
		return scenario_name;
	}

	public void setScenario_name(String scenario_name) {
		this.scenario_name = scenario_name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getConfiguration_id() {
		return configuration_id;
	}

	public void setConfiguration_id(Integer configuration_id) {
		this.configuration_id = configuration_id;
	}

	public Integer getTest_set_id() {
		return test_set_id;
	}

	public void setTest_set_id(Integer test_set_id) {
		this.test_set_id = test_set_id;
	}

	public Integer getTest_set_line_id() {
		return test_set_line_id;
	}

	public void setTest_set_line_id(Integer test_set_line_id) {
		this.test_set_line_id = test_set_line_id;
	}

	public Integer getScript_id() {
		return script_id;
	}

	public void setScript_id(Integer script_id) {
		this.script_id = script_id;
	}

	public Integer getSeq_num() {
		return seq_num;
	}

	public void setSeq_num(Integer seq_num) {
		this.seq_num = seq_num;
	}



	public String getScript_number() {
		return script_number;
	}

	public void setScript_number(String script_number) {
		this.script_number = script_number;
	}

	public String getIssue_key() {
		return issue_key;
	}

	public void setIssue_key(String issue_key) {
		this.issue_key = issue_key;
	}

	public String getTest_set_name() {
		return test_set_name;
	}

	public void setTest_set_name(String test_set_name) {
		this.test_set_name = test_set_name;
	}

	
	
	
}
