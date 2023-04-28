package com.winfo.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Table(name = "WIN_TA_EXECUTION_AUDIT")
@Data
public class ExecutionAudit {
	@Id
	@Column(name = "EXECUTION_AUDIT_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int executionAuditId;

	@Column(name = "TEST_SET_ID")
	private String testSetId;

	@Column(name = "SCRIPT_ID")
	private String scriptId;

	@Column(name = "SCRIPT_NUMBER")
	private String scriptNumber;

	@Column(name = "EXECUTION_Start_Time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date executionStartTime;

	@Column(name = "EXECUTION_END_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date executionEndTime;

	@Column(name = "STATUS")
	private String status;


}
