package com.winfo.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.winfo.model.TestSetLine;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestSetLineDto {

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
	private List<WatsTestSetParamVO> scriptParam = new ArrayList<>();
	
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

}
