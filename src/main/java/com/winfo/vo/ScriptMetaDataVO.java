package com.winfo.vo;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ScriptMetaDataVO {

	@JsonProperty("Line Number")
	@JsonAlias("lineNumber")
	private Integer lineNumber;

	@JsonProperty("Step Description")
	@JsonAlias("stepDesc")
	private String stepDesc;

	@JsonProperty("Input Parameter")
	@JsonAlias("inputParameter")
	private String inputParameter;

	@JsonProperty("Action")
	@JsonAlias("action")
	private String action;

	@JsonProperty("Validation Type")
	@JsonAlias("validationType")
	private String validationType;

	@JsonProperty("Validation Name")
	@JsonAlias("validationName")
	private String validationName;

	@JsonProperty("Unique/Mandatory")
	@JsonAlias("uniqueMandatory")
	private String uniqueMandatory;

	@JsonProperty("Data Types")
	@JsonAlias("datatypes")
	private String datatypes;

}
