package com.winfo.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Table(name = "EXECUTE_STATUS")
@Data
public class ExecuteStatus {

	@EmbeddedId
	private ExecuteStatusPK executeStatusPK;

	@Column(name = "STATUS_FLAG")
	private Character flag;

	@Column(name = "EXECUTION_DATE")
	private Date executionDate;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	@Temporal(TemporalType.DATE)
	private Date createdDate;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Column(name = "UPDATED_DATE")
	@Temporal(TemporalType.DATE)
	private Date updatedDate;
}
