package com.winfo.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "audit_script_execution_trail")
public class AuditScriptExecTrail {

	@Id
	private String correlationId;
	private Integer testSetLineId;
	private Integer stageId;
	private Date eventTime;
	private String triggeredBy;
	
	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	public Integer getTestSetLineId() {
		return testSetLineId;
	}

	public void setTestSetLineId(Integer testSetLineId) {
		this.testSetLineId = testSetLineId;
	}

	public Integer getStageId() {
		return stageId;
	}

	public void setStageId(Integer stageId) {
		this.stageId = stageId;
	}

	public Date getEventTime() {
		return eventTime;
	}

	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}

	public String getTriggeredBy() {
		return triggeredBy;
	}

	public void setTriggeredBy(String triggeredBy) {
		this.triggeredBy = triggeredBy;
	}

	public static class Builder {

		private String correlationId;
		private Integer testSetLineId;
		private Integer stageId;
		private Date eventTime;
		private String triggeredBy;

		public Builder() {
		}

		Builder(String correlationId, Integer testSetLineId, Integer stageId, Date eventTime, String triggeredBy) {
			this.correlationId = correlationId;
			this.testSetLineId = testSetLineId;
			this.stageId = stageId;
			this.eventTime = eventTime;
			this.triggeredBy = triggeredBy;
		}

		public Builder correlationId(String correlationId) {
			this.correlationId = correlationId;
			return Builder.this;
		}

		public Builder testSetLineId(Integer testSetLineId) {
			this.testSetLineId = testSetLineId;
			return Builder.this;
		}

		public Builder stageId(Integer stageId) {
			this.stageId = stageId;
			return Builder.this;
		}

		public Builder eventTime(Date eventTime) {
			this.eventTime = eventTime;
			return Builder.this;
		}

		public Builder triggeredBy(String triggeredBy) {
			this.triggeredBy = triggeredBy;
			return Builder.this;
		}

		public AuditScriptExecTrail build() {

			return new AuditScriptExecTrail(this);
		}
	}

	private AuditScriptExecTrail(Builder builder) {
		this.correlationId = builder.correlationId;
		this.testSetLineId = builder.testSetLineId;
		this.stageId = builder.stageId;
		this.eventTime = builder.eventTime;
		this.triggeredBy = builder.triggeredBy;
	}

	public static Builder builder() {
		return new Builder();
	}

}
