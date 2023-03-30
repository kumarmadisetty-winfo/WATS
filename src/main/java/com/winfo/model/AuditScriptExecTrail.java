package com.winfo.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "audit_script_execution_trail")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditScriptExecTrail implements Serializable {

	private static final long serialVersionUID = 6915558297579903655L;
	@Id
	@GeneratedValue
	private int id;
	private String correlationId;
	private Integer testSetLineId;
	private Integer stageId;
	private Date eventTime;
	private String triggeredBy;
	private String message;

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
