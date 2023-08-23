package com.winfo.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "WIN_TA_SCHEDULER")
@Data
public class Scheduler {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jobGenerator")
	@SequenceGenerator(name = "jobGenerator", sequenceName = "WIN_TA_JOB_ID_SEQ", allocationSize = 1)
	@Column(name = "JOB_ID", nullable = false)
	private Integer jobId;

	@Column(name = "PROJECT_ID")
	private Integer projectId;

	@Column(name = "CONFIGURATION_ID")
	private Integer configurationId;
	
	@Column(name = "TEMPLATE")
	private String template;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "JOB_NAME")
	private String jobName;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATION_DATE")
	private Date creationDate;

	@Column(name = "UPDATED_BY")
	private String updatedBy;
	
	@Column(name = "UPDATE_DATE")
	private Date updatedDate;
	
	@Column(name="STATUS")
	private String status;

}
