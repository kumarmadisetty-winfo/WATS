package com.winfo.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Table(name = "WIN_TA_EXECUTION_HISTORY")
@Data
public class ExecutionHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "executionId_generator")
	@SequenceGenerator(name = "executionId_generator", sequenceName = "WIN__TA_EXECUTION_ID_SEQ", allocationSize = 1)
	@Column(name = "EXECUTION_ID")
	private int executionId;
	
	@Column(name="TEST_SET_LINE_ID")
	private int testSetLineId;
	
	@Column(name = "EXECUTION_START_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date executionStartTime;

	@Column(name = "EXECUTION_END_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date executionEndTime;
	
	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "LAST_UPDATED_BY")
	private String lastUpdatedBy;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "LINE_ERROR_MESSAGE")
	private String lineErrorMessage;
	
}
