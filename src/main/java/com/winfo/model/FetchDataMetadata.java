package com.winfo.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FetchDataMetadata {

	@JsonProperty("line_number")
	private Integer lineNumber;

	@JsonProperty("action")
	private String action;

	@JsonProperty("input_parameter")
	private String inputParameter;

	@JsonProperty("xpath_location")
	private String xpathLocation;

	@JsonProperty("xpath_location1")
	private String xpathLocation1;

	@JsonProperty("created_by")
	private String createdBy;

	@JsonProperty("creation_date")
	private Date creationDate;

	@JsonProperty("updated_by")
	private String updatedBy;

	@JsonProperty("update_date")
	private Date updateDate;

	@JsonProperty("step_desc")
	private String stepDesc;

	@JsonProperty("field_type")
	private String fieldType;

	@JsonProperty("hint")
	private String hint;

	@JsonProperty("datatypes")
	private String datatypes;

	@JsonProperty("unique_mandatory")
	private String uniqueMandatory;

	@JsonProperty("validation_type")
	private String validationType;

	@JsonProperty("validation_name")
	private String validationName;

	@JsonProperty("script_number")
	private String scriptNumber;

}
