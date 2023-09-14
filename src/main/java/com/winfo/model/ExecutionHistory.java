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
	@Column(name = "EXECUTION_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "execution_id_generator")
	@SequenceGenerator(name = "execution_id_generator", sequenceName = "WIN__TA_EXECUTION_ID_SEQ", allocationSize = 1)
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
	
}
