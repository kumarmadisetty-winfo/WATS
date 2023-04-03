package com.winfo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "WIN_TA_CUSTOMERS")
@Data
public class Customer {

	@Id
	@Column(name = "CUSTOMER_ID", unique = true, nullable = false)
	private Integer customerId;

	@Column(name = "CUSTOMER_NAME")
	private String customerName;

	@Column(name = "CUSTOMER_URI")
	private String customerUri;

	@Column(name = "CUSTOMER_NUMBER")
	private Integer customerNumber;

}
