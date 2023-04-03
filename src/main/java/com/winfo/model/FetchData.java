package com.winfo.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FetchData {
	
	@JsonProperty("script_number")
	private String scriptNumber;

	@JsonProperty("module")
	private String module;

	@JsonProperty("scenario_name")
	private String scenarioName;

	@JsonProperty("scenario_description")
	private String scenarioDescription;

	@JsonProperty("product_version")
	private String productVersion;

	@JsonProperty("priority")
	private Integer priority;

	@JsonProperty("process_area")
	private String processArea;

	@JsonProperty("role")
	private String role;

	@JsonProperty("subProcess_area")
	private String subProcessArea;

	@JsonProperty("standard_custom")
	private String standardCustom;

	@JsonProperty("test_script_status")
	private String testScriptStatus;

	@JsonProperty("customer_id")
	private Integer customerId;

	@JsonProperty("dependency")
	private Integer dependency;

	@JsonProperty("end2end_scenario")
	private String end2endScenario;

	@JsonProperty("expected_result")
	private String expectedResult;

	@JsonProperty("selenium_test_script_name")
	private String seleniumTestScriptName;

	@JsonProperty("selenium_test_method")
	private String seleniumTestMethod;

	@JsonProperty("author")
	private String author;
	@JsonProperty("created_by")
	private String createdBy;

	@JsonProperty("creation_date")
	private Date creationDate;

	@JsonProperty("updated_by")
	private String updatedBy;

	@JsonProperty("update_date")
	private Date updateDate;

	@JsonProperty("customisation_refrence")
	private String customisationRefrence;

	@JsonProperty("attribute1")
	private String attribute1;

	@JsonProperty("attribute2")
	private String attribute2;

	@JsonProperty("attribute3")
	private String attribute3;

	@JsonProperty("attribute4")
	private String attribute4;

	@JsonProperty("attribute5")
	private String attribute5;

	@JsonProperty("attribute6")
	private String attribute6;

	@JsonProperty("attribute7")
	private String attribute7;

	@JsonProperty("attribute8")
	private String attribute8;

	@JsonProperty("attribute9")
	private String attribute9;

	@JsonProperty("attribute10")
	private String attribute10;

	@JsonProperty("DEPENDENT_SCRIPT_NUM")
	private String dependentScriptNum;

	private String targetApplication;

}
