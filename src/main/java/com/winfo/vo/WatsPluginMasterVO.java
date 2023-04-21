package com.winfo.vo;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WatsPluginMasterVO {

	@JsonProperty("SCRIPT ID")
	private Integer scriptId;

	@JsonProperty("SCRIPT NUMBER")
	private String scriptNumber;

	@JsonProperty("PROCESS AREA")
	private String processArea;

	@JsonProperty("SUB PROCESS AREA")
	private String subProcessArea;

	@JsonProperty("MODULE")
	private String module;

	@JsonProperty("ROLE")
	private String role;

	@JsonProperty("END2END SCENARIO")
	private String end2endScenario;

	@JsonProperty("SCENARIO NAME")
	private String scenarioName;

	@JsonProperty("SCENARIO DESCRIPTION")
	private String scenarioDescription;

	@JsonProperty("EXPECTED RESULT")
	private String expectedResult;

	@JsonProperty("SELENIUM TEST SCRIPT NAME")
	private String seleniumTestScriptName;

	@JsonProperty("SELENIUM_TEST_METHOD")
	private String seleniumTestMethod;

	@JsonProperty("DEPENDENCY")
	private String dependency;

	@JsonProperty("PRODUCT VERSION")
	private String productVersion;

	@JsonProperty("STANDARD CUSTOM")
	private String standardCustom;

	@JsonProperty("TEST SCRIPT STATUS")
	private String testScriptStatus;

	@JsonProperty("AUTHOR")
	private String author;

	@JsonProperty("CREATED_BY")
	private String createdBy;

	@JsonProperty("CREATION_DATE")
	private String creationDate;

	@JsonProperty("UPDATED_BY")
	private String updatedBy;

	@JsonProperty("UPDATE_DATE")
	private Date updateDate;

	@JsonProperty("CUSTOMER_ID")
	private Integer customerId;

	@JsonProperty("CUSTOMISATION_REFERENCE")
	private String customisationReference;

	@JsonProperty("ATTRIBUTE1")
	private String attribute1;

	@JsonProperty("ATTRIBUTE2")
	private String attribute2;

	@JsonProperty("ATTRIBUTE3")
	private String attribute3;

	@JsonProperty("ATTRIBUTE4")
	private String attribute4;

	@JsonProperty("ATTRIBUTE5")
	private String attribute5;

	@JsonProperty("ATTRIBUTE6")
	private String attribute6;

	@JsonProperty("ATTRIBUTE7")
	private String attribute7;

	@JsonProperty("ATTRIBUTE8")
	private String attribute8;

	@JsonProperty("ATTRIBUTE9")
	private String attribute9;

	@JsonProperty("ATTRIBUTE10")
	private String attribute10;

	@JsonProperty("PRIORITY")
	private Integer priority;

	@JsonProperty("MODULE_SRT")
	private String moduleSrt;

	@JsonProperty("TestrunName")
	private String testRunName;

	@JsonProperty("MetaDataList")
	private List<WatsPluginMetaDataVO> metaDataList = new ArrayList<>();

}
