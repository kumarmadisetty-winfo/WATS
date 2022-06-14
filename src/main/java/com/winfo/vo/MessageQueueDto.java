package com.winfo.vo;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.winfo.model.AuditScriptExecTrail;
import com.winfo.utils.Constants.AUDIT_TRAIL_STAGES;

@JsonInclude(Include.NON_NULL)
public class MessageQueueDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 63960221017141942L;
	@NotNull
	private String testSetId;
	@NotNull
	private String testSetLineId;
	private String scriptPath;
	private boolean success;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	private Date startDate;
	private boolean manualTrigger;
	private AuditScriptExecTrail autditTrial;
	private AUDIT_TRAIL_STAGES stage;

	public MessageQueueDto() {
	}

	public MessageQueueDto(@NotNull String testSetId, @NotNull String testSetLineId, String scriptPath,
			AuditScriptExecTrail autditTrial) {
		super();
		this.testSetId = testSetId;
		this.testSetLineId = testSetLineId;
		this.scriptPath = scriptPath;
		this.autditTrial = autditTrial;
	}

	public String getTestSetId() {
		return testSetId;
	}

	public void setTestSetId(String testSetId) {
		this.testSetId = testSetId;
	}

	public String getTestSetLineId() {
		return testSetLineId;
	}

	public void setTestSetLineId(String testSetLineId) {
		this.testSetLineId = testSetLineId;
	}

	public String getScriptPath() {
		return scriptPath;
	}

	public void setScriptPath(String scriptPath) {
		this.scriptPath = scriptPath;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public boolean isManualTrigger() {
		return manualTrigger;
	}

	public void setManualTrigger(boolean manualTrigger) {
		this.manualTrigger = manualTrigger;
	}

	public AuditScriptExecTrail getAutditTrial() {
		return autditTrial;
	}

	public void setAutditTrial(AuditScriptExecTrail autditTrial) {
		this.autditTrial = autditTrial;
	}

	public AUDIT_TRAIL_STAGES getStage() {
		return stage;
	}

	public void setStage(AUDIT_TRAIL_STAGES stage) {
		this.stage = stage;
	}

}
