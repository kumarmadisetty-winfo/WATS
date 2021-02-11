package com.winfo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WIN_TA_CUSTOMERS")
public class CustomerTable {
	@Id
	
	@Column(name = "CUSTOMER_ID")
	private Integer customer_id;
	@Column(name = "CUSTOMER_NAME")
	private String customer_name;
	@Column(name = "CUSTOMER_URI")
	private String customer_uri;
	public String getCustomer_uri() {
		return customer_uri;
	}
	public void setCustomer_uri(String customer_uri) {
		this.customer_uri = customer_uri;
	}
	public Integer getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(Integer customer_id) {
		this.customer_id = customer_id;
	}
	public String getCustomer_name() {
		return customer_name;
	}
	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

}
