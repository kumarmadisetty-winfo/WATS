package com.winfo.vo;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class WatsPluginMetaDataVO {

	@JsonProperty("SCRIPT META DATA ID")
	private Integer scriptMetaDataId;

	@JsonProperty("SCRIPT NUMBER")
	private String scriptNumber;

	@JsonProperty("LINE NUMBER")
	private Integer lineNumber;

	@JsonProperty("INPUT PARAMETER")
	private String inputParameter;

	@JsonProperty("ACTION")
	private String action;

	@JsonProperty("XPATH_LOCATION")
	private String xpathLocation;

	@JsonProperty("XPATH_LOCATION1")
	private String xpathLocation1;

	@JsonProperty("CREATED_BY")
	private String createdBy;

	@JsonProperty("CREATION_DATE")
	private String creationDate;

	@JsonProperty("UPDATED_BY")
	private String updatedBy;

	@JsonProperty("UPDATE_DATE")
	private Date updateDate;

	@JsonProperty("STEP DESCRIPTION")
	private String stepDesc;

	@JsonProperty("FIELD_TYPE")
	private String fieldType;

	@JsonProperty("HINT")
	private String hint;

	@JsonProperty("INPUT VALUE")
	private String inputValue;

}
