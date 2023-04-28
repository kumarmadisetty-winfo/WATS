package com.winfo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TestRunVO {

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
	private String testSetName;

	@JsonProperty("status")
	private String status;

	@JsonProperty("configuration_id")
	private Integer configurationId;

	@JsonProperty("script_number")
	private String scriptNumber;

	@JsonProperty("scenario_name")
	private String scenarioName;

}
