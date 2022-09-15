package com.winfo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
public class TestRunVO 
{

	
	
	@JsonProperty("test_set_id")
	private Integer testSetId;
	
	@JsonProperty("test_set_line_id")
	private Integer testSetLineId;
	
	@JsonProperty("script_id")
	private Integer scriptId;
	
	@JsonProperty("seq_num")
	private Integer seqNum;

	@JsonProperty("issue_key")
	private String issueKey;

	@JsonProperty("test_set_name")
	private String  testSetName;
	
	@JsonProperty("status")
	private String status;
	
	@JsonProperty("configuration_id")
	private Integer configurationId;

	@JsonProperty("script_number")
	private String scriptNumber;
	
	@JsonProperty("scenario_name")
	private String scenarioName;

	

	public String getScenarioName() {
		return scenarioName;
	}

	public void setScenarioName(String scenarioName) {
		this.scenarioName = scenarioName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getConfigurationId() {
		return configurationId;
	}

	public void setConfigurationId(Integer configurationId) {
		this.configurationId = configurationId;
	}

	public Integer getTestSetId() {
		return testSetId;
	}

	public void setTestSetId(Integer testSetId) {
		this.testSetId = testSetId;
	}

	public Integer getTestSetLineId() {
		return testSetLineId;
	}

	public void setTestSetLineId(Integer testSetLineId) {
		this.testSetLineId = testSetLineId;
	}

	public Integer getScriptId() {
		return scriptId;
	}

	public void setScriptId(Integer scriptId) {
		this.scriptId = scriptId;
	}

	public Integer getSeqNum() {
		return seqNum;
	}

	public void setSeqNum(Integer seqNum) {
		this.seqNum = seqNum;
	}



	public String getScriptNumber() {
		return scriptNumber;
	}

	public void setScriptNumber(String scriptNumber) {
		this.scriptNumber = scriptNumber;
	}

	public String getIssueKey() {
		return issueKey;
	}

	public void setIssueKey(String issueKey) {
		this.issueKey = issueKey;
	}

	public String getTestSetName() {
		return testSetName;
	}

	public void setTestSetName(String testSetName) {
		this.testSetName = testSetName;
	}

	
	
	
}
