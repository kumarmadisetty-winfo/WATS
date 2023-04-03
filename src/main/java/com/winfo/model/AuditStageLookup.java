package com.winfo.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "audit_stage_lookup")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditStageLookup {

	@Id
	private Integer stageId;
	private String stageName;

}
