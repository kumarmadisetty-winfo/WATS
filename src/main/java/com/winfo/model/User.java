package com.winfo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "WIN_TA_USERS")
@Data
public class User {

	@Column(name = "USER_ID")
	@Id
	private String userId;

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "CUSTOMER_ID")
	private Integer customerId;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "CONTACT_NUMBER")
	private long contactNumber;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "OTP")
	private String otp;

}
