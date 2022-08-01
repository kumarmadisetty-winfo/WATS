package com.winfo.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.winfo.model.ScriptsData;

public class WatsTestSetVO {
	// #################TestSetLinesAndParaData

	@JsonProperty("test_set_line_id")
	private String test_set_line_id;

	@JsonProperty("test_set_id")
	private String test_set_id;

	@JsonProperty("script_id")
	private int script_id;

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
	@JsonProperty("ScriptParam")
	private List<WatsTestSetParamVO> ScriptParam = new ArrayList<WatsTestSetParamVO>();

	public WatsTestSetVO() {
	}

	public WatsTestSetVO(ScriptsData scriptData) {
		this.script_id = scriptData.getScriptid();
		this.scriptnumber = scriptData.getScriptnumber();
		this.status = scriptData.getScriptnumber();
		this.enabled = scriptData.getEnabled();
		this.seqnum = scriptData.getSeqnum();
		this.createdby = scriptData.getCreatedby();
		this.testsstlinescriptpath = scriptData.getTestsstlinescriptpath();
		this.executedby = scriptData.getExecutedby();
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
		return ScriptParam;
	}

	public void setScriptParam(List<WatsTestSetParamVO> scriptParam) {
		ScriptParam = scriptParam;
	}

	public String getTest_set_line_id() {
		return test_set_line_id;
	}

	public void setTest_set_line_id(String test_set_line_id) {
		this.test_set_line_id = test_set_line_id;
	}

	public String getTest_set_id() {
		return test_set_id;
	}

	public void setTest_set_id(String test_set_id) {
		this.test_set_id = test_set_id;
	}

	public int getScript_id() {
		return script_id;
	}

	public void setScript_id(int script_id) {
		this.script_id = script_id;
	}

	@Override
	public String toString() {
		return this.executedby + " " + this.creationdate + " " + this.executionendtime + " " + this.seqnum + " "
				+ this.scriptnumber + " " + this.script_id + " " + this.testsstlinescriptpath + " " + this.enabled + " "
				+ this.createdby + " " + this.status;
	}

}
