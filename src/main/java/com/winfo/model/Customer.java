package com.winfo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WIN_TA_CUSTOMERS")
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

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerUri() {
		return customerUri;
	}

	public void setCustomerUri(String customerUri) {
		this.customerUri = customerUri;
	}

	public Integer getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(Integer customerNumber) {
		this.customerNumber = customerNumber;
	}

}
