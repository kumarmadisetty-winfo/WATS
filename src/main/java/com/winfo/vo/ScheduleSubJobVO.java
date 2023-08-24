package com.winfo.vo;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ScheduleSubJobVO {
	
	@JsonProperty("Sub-Schedular Name")
	@JsonAlias("subJobName")
	private String subJobName;
	
	@JsonProperty("Test Run Name")
	@JsonAlias("testRunName")
	private String testRunName;
	
	@JsonProperty("Test Run ID")
	@JsonAlias("testSetId")
	private int testSetId;
	
	@JsonProperty("Job ID")
	@JsonAlias("jobId")
	private int jobId;
	
	@JsonProperty("Start Date")
	@JsonAlias("startDate")
	private String startDate;
	
	@JsonProperty("Email")
	@JsonAlias("email")
	private String email;	
	
	@JsonProperty("User Name")
	@JsonAlias("userName")
	private String userName;	
	
	@JsonProperty("Type")
	@JsonAlias("type")
	private String type;	
}
