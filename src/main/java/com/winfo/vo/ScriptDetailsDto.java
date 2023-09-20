package com.winfo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScriptDetailsDto {

	private String scriptId;
	private String scriptNumber;
	private String testResultLineId;
	private String scriptMetaDataId;
	private String lineNumber;
	private String inputParameter;
	private String action;
	private String xpathLocation;
	private String xpathLocation1;
	private String inputValue;
	private String status;
	private int rowNumber;
	private String testSetLineId;
	private String dependency;
	private String fieldType;
	private String scenarioName;
	private String module;
	private String seqNum;
	private String stepDescription;
	private String scriptDescription;
	private String testScriptParamId;
	private String conditionalPopup;
	private String lineErrorMsg;
	private String testRunParamDesc;
	private Integer dependencyScriptNumber;
	private String issueKey;
	private String executedBy;
	private String targetApplicationName;
	private String oracleReleaseYear;
	private String uniqueMandatory;
	private String executionStartTime;
	private String executionEndTime;
}
