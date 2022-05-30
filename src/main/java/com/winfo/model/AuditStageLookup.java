package com.winfo.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "audit_script_execution_trail")
public class AuditStageLookup {

	@Id
	private Integer stageId;
	private String stageName;
	
	public Integer getStageId() {
		return stageId;
	}

	public void setStageId(Integer stageId) {
		this.stageId = stageId;
	}

	public String getStageName() {
		return stageName;
	}

	public void setStageName(String stageName) {
		this.stageName = stageName;
	}

	public static class Builder {

		private Integer stageId;
		private String stageName;

		public Builder() {
		}

		Builder(Integer stageId, String stageName) {
			this.stageId = stageId;
			this.stageName = stageName;
		}

		public Builder stageId(Integer stageId) {
			this.stageId = stageId;
			return Builder.this;
		}

		public Builder stageName(String stageName) {
			this.stageName = stageName;
			return Builder.this;
		}

		public AuditStageLookup build() {

			return new AuditStageLookup(this);
		}
	}

	private AuditStageLookup(Builder builder) {
		this.stageId = builder.stageId;
		this.stageName = builder.stageName;
	}

}
