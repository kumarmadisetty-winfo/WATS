package com.winfo.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.winfo.model.TestSetLine;

public class TestSetLineDto {
	// #################TestSetLinesAndParaData

	@JsonProperty("testSetLineId")
	private String testSetLineId;

	@JsonProperty("testSetId")
	private String testSetId;

	@JsonProperty("scriptId")
	private int scriptId;

	@JsonProperty("executedby")
	private String executedby;
	@JsonProperty("creationdate")
	private Date creationdate;
	@JsonProperty("executionendtime")
	private Date executionendtime;
	@JsonProperty("seqnum")
	private int seqnum;
	@JsonProperty("scriptnumber")
	private String scriptnumber;
	@JsonProperty("testsstlinescriptpath")
	private String testsstlinescriptpath;
	@JsonProperty("enabled")
	private String enabled;
	@JsonProperty("createdby")
	private String createdby;
	@JsonProperty("scriptUpadated")
	private String scriptUpadated;
	@JsonProperty("lastupdatedby")
	private String lastupdatedby;
	@JsonProperty("updateddate")
	private Date updateddate;
	@JsonProperty("executionstarttime")
	private Date executionstarttime;
	@JsonProperty("status")
	private String status;
	@JsonProperty("scriptParam")
	private List<WatsTestSetParamVO> scriptParam = new ArrayList<WatsTestSetParamVO>();

	public TestSetLineDto() {
	}

	public TestSetLineDto(TestSetLine scriptData) {
		this.scriptId = scriptData.getScriptId();
		this.scriptnumber = scriptData.getScriptNumber();
		this.status = "New";
		this.enabled = scriptData.getEnabled();
		this.seqnum = scriptData.getSeqNum();
		this.createdby = scriptData.getCreatedBy();
		this.testsstlinescriptpath = scriptData.getTestRunScriptPath();
		this.executedby = scriptData.getExecutedBy();
		this.scriptUpadated = null;
		this.lastupdatedby = null;
		this.creationdate = null;
		this.updateddate = null;
		this.executionendtime = null;
		this.executionstarttime = null;
	}

	public String getExecutedby() {
		return executedby;
	}

	public void setExecutedby(String executedby) {
		this.executedby = executedby;
	}

	public Date getCreationdate() {
		return creationdate;
	}

	public void setCreationdate(Date creationdate) {
		this.creationdate = creationdate;
	}

	public Date getExecutionendtime() {
		return executionendtime;
	}

	public void setExecutionendtime(Date executionendtime) {
		this.executionendtime = executionendtime;
	}

	public int getSeqnum() {
		return seqnum;
	}

	public void setSeqnum(int seqnum) {
		this.seqnum = seqnum;
	}

	public String getScriptnumber() {
		return scriptnumber;
	}

	public void setScriptnumber(String scriptnumber) {
		this.scriptnumber = scriptnumber;
	}

	public String getTestsstlinescriptpath() {
		return testsstlinescriptpath;
	}

	public void setTestsstlinescriptpath(String testsstlinescriptpath) {
		this.testsstlinescriptpath = testsstlinescriptpath;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getCreatedby() {
		return createdby;
	}

	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}

	public String getScriptUpadated() {
		return scriptUpadated;
	}

	public void setScriptUpadated(String scriptUpadated) {
		this.scriptUpadated = scriptUpadated;
	}

	public String getLastupdatedby() {
		return lastupdatedby;
	}

	public void setLastupdatedby(String lastupdatedby) {
		this.lastupdatedby = lastupdatedby;
	}

	public Date getUpdateddate() {
		return updateddate;
	}

	public void setUpdateddate(Date updateddate) {
		this.updateddate = updateddate;
	}

	public Date getExecutionstarttime() {
		return executionstarttime;
	}

	public void setExecutionstarttime(Date executionstarttime) {
		this.executionstarttime = executionstarttime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<WatsTestSetParamVO> getScriptParam() {
		return scriptParam;
	}

	public void setScriptParam(List<WatsTestSetParamVO> scriptParam) {
		this.scriptParam = scriptParam;
	}

	public String getTestSetLineId() {
		return testSetLineId;
	}

	public void setTestSetLineId(String testSetLineId) {
		this.testSetLineId = testSetLineId;
	}

	public String getTestSetId() {
		return testSetId;
	}

	public void setTestSetId(String testSetId) {
		this.testSetId = testSetId;
	}

	public int getScriptId() {
		return scriptId;
	}

	public void setScriptId(int scriptId) {
		this.scriptId = scriptId;
	}

}
