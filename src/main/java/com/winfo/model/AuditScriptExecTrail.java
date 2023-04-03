package com.winfo.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "audit_script_execution_trail")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

}
