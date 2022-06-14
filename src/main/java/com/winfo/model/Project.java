package com.winfo.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WIN_TA_PROJECTS")
public class Project {

	@Column(name = "PROJECT_ID")
	@Id
	private Integer projectId;

	@Column(name = "PROJECT_NUMBER")
	private String projectNumber;

	@Column(name = "PROJECT_NAME")
	private String projectName;

	@Column(name = "START_DATE")
	private Date startDate;

	@Column(name = "END_DATE")
	private Date endDate;

	@Column(name = "PROJECT_STATUS")
	private String projectStatus;

	@Column(name = "PROJECT_MANAGER")
	private String projectManager;

	@Column(name = "PROJECT_MANAGER_EMAIL")
	private String projectManagerEmail;

	@Column(name = "CUSTOMER_ID")
	private Integer customerId;

	@Column(name = "CREATION_DATE")
	private Date creationDate;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@Column(name = "LAST_UPDATED_DATE")
	private Date lastUpdatedDate;

	@Column(name = "PRODUCT_VERSION")
	private String productVersion;

}
