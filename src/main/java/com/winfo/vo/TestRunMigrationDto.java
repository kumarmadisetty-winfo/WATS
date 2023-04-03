package com.winfo.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.winfo.model.TestSet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestRunMigrationDto {

	@JsonProperty("projectName")
	private String projectName;

	@JsonProperty("watsPackage")
	private String watsPackage;

	@JsonProperty("customer")
	private String customer;

	@JsonProperty("configurationName")
	private String configurationName;

	@JsonProperty("testSetId")
	private int testSetId;

	@JsonProperty("projectId")
	private int projectId;

	@JsonProperty("description")
	private String description;

	@JsonProperty("testSetDesc")
	private String testSetDesc;

	@JsonProperty("testSetComments")
	private String testSetComments;

	@JsonProperty("enabled")
	private String enabled;

	@JsonProperty("lastExecutedBy")
	private String lastExecutedBy;

	@JsonProperty("tsCompleteFlag")
	private String tsCompleteFlag;

	@JsonProperty("passPath")
	private String passPath;

	@JsonProperty("effectiveTo")
	private Date effectiveTo;

	@JsonProperty("testSetName")
	private String testSetName;

	@JsonProperty("execeptionPath")
	private String execeptionPath;

	@JsonProperty("creationDate")
	private String creationDate;

	@JsonProperty("createdBy")
	private String createdBy;

	@JsonProperty("trMode")
	private String trMode;

	@JsonProperty("updateDate")
	private String updateDate;

	@JsonProperty("lastUpdatedBy")
	private String lastUpdatedBy;

	@JsonProperty("failPath")
	private String failPath;

	@JsonProperty("effectiveFrom")
	private Date effectiveFrom;

	@JsonProperty("configurationId")
	private int configurationId;

	@JsonProperty("scriptMasterData")
	private List<ScriptMasterDto> scriptMasterData = new ArrayList<>();

	@JsonProperty("testSetLinesAndParaData")
	private List<TestSetLineDto> testSetLinesAndParaData = new ArrayList<>();

	@JsonProperty("lookUpData")
	private Map<String, LookUpVO> lookUpData;

	@JsonProperty("testRunExists")
	private boolean testRunExists;
	
	public TestRunMigrationDto(TestSet testRunData) {

		this.configurationId = testRunData.getConfigurationId();
		this.createdBy = testRunData.getCreatedBy();
		this.projectId = testRunData.getProjectId();
		this.testSetName = testRunData.getTestRunName();
		this.testSetDesc = testRunData.getTestRunDesc();
		this.testSetComments = testRunData.getTestRunComments();
		this.enabled = testRunData.getEnabled();
		this.description = testRunData.getDescription();
		this.effectiveFrom = testRunData.getEffectiveFrom();
		this.effectiveTo = testRunData.getEffectiveTo();
		this.tsCompleteFlag = testRunData.getTsCompleteFlag();
		this.passPath = testRunData.getPassPath();
		this.failPath = testRunData.getFailPath();
		this.execeptionPath = testRunData.getExceptionPath();
		this.trMode = testRunData.getTestRunMode();
		this.lastUpdatedBy = null;
		this.creationDate = null;
		this.updateDate = null;
	}


}
