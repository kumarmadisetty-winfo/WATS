package com.winfo.vo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BugDetails {

	@JsonProperty("test_set_id")
	private Integer test_set_id;

	@JsonProperty("test_set_line_id")
	private Integer testSetLineId;

	@JsonProperty("script_id")
	private List<Integer> script_id = new ArrayList<Integer>();
	
	@JsonProperty("jobId")
	private int jobId;

}
