package com.winfo.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "EXECUTE_STATUS")
@Data
public class ExecuteStatus {
	
	@EmbeddedId
	private ExecuteStatusPK executeStatusPK;	
	
	@Column(name = "TEST_RUN_NAME")
	private String testRunName;
	
	@Column(name = "STATUS_FLAG")
	private Character flag;
	
	@Column(name = "EXECUTION_DATE")
	private Date executionDate;
	
	
	@Column(name = "EXECUTE_STATUS")
	private Integer executionStatus;
	
	
	@Column(name = "EXECUTED_BY")
	private String executedBy;
	
	@Column(name = "TEST_RUN_ID")
	private Integer testRunId;
	
	
}
