package com.winfo.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "WIN_TA_USER_ROLE")
@Data
public class UserRole {

	@Column(name = "USER_ID")
	@Id
	private String userId;

	@Column(name = "PASSWORD")
	private String password;
	
	@Column(name = "START_DATE")
	private Date startDate;

	@Column(name = "END_DATE")
	private Date endDate;

	@Column(name = "PASSWORD_EXPIRY")
	private Date passwordExpiry;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "USER_TYPE")
	private String userType;

	@Column(name = "NO_OF_ATTEMPTS")
	private Integer noOfAttempts;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@Column(name = "LAST_UPDATED_DATE")
	private Date lastUpdatedDate;

	@Column(name = "PASSWORD_CHANGE")
	private String passwordChange;

}
