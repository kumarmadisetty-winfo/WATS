package com.winfo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WIN_TA_CUSTOMERS")
public class CustomerTable {
	
	@Id
	@Column(name = "CUSTOMER_ID")
	private Integer customerId;
	
	@Column(name = "CUSTOMER_NAME")
	private String customerName;
	
	@Column(name = "CUSTOMER_URI")
	private String customerUri;


}
