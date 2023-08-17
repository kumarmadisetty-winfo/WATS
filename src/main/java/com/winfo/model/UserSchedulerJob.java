package com.winfo.model;

import java.sql.Date;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "WIN_TA_USER_SCHEDULER_JOBS")
@Data
public class UserSchedulerJob {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "schedulerGenerator")
	@SequenceGenerator(name = "schedulerGenerator", sequenceName = "WIN_TA_SCHEDULED_ID_SEQ", allocationSize = 1)
	@Column(name = "SCHEDULED_ID", nullable = false)
	private Integer scheduledId;

	@Column(name = "JOB_ID")
	private Integer jobId;
	
	@Column(name = "DEPENDENCY")
	private Integer dependency;

	@Column(name = "COMMENTS")
	private String comments;

	@Column(name = "JOB_NAME")
	private String jobName;

	@Column(name = "CLIENT_ID")
	private String clientId;

	@Column(name = "START_DATE")
	private String startDate;
	
	@Column(name = "END_DATE")
	private LocalDateTime endDate;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	private Date creationDate;

	@Column(name = "UPDATED_BY")
	private String updatedBy;
	
	@Column(name = "UPDATED_DATE")
	private Date updatedDate;

}
