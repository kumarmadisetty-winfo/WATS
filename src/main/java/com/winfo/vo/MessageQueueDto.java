package com.winfo.vo;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.winfo.model.AuditScriptExecTrail;
import com.winfo.utils.Constants.AUDIT_TRAIL_STAGES;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@JsonInclude(Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageQueueDto implements Serializable {

	private static final long serialVersionUID = 63960221017141942L;
	
	@NotNull
	private String testSetId;
	
	@NotNull
	private String testSetLineId;
	
	private String scriptPath;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	
	private Date startDate;
	
	@JsonProperty("manualTrigger")
	private boolean manualTrigger;
	
	private AuditScriptExecTrail autditTrial;
	private AUDIT_TRAIL_STAGES stage;
	
	public MessageQueueDto(@NotNull String testSetId, @NotNull String testSetLineId, String scriptPath,
			AuditScriptExecTrail autditTrial) {
		super();
		this.testSetId = testSetId;
		this.testSetLineId = testSetLineId;
		this.scriptPath = scriptPath;
		this.autditTrial = autditTrial;
	}
	
}
