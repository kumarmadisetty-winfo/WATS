package com.winfo.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "wats_subscription")
@Data
public class Subscription {
	
	@Id
	@Column(name = "SUBSCRIPTION_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long subscriptionId;
	
	@Column(name = "SUBSCRIPTION_DETAILS")
	private String subscriptionDetails;

	@Column(name = "QUANTITY")
	private long quantity;
	
	@Column(name = "UOM")
	private String uom;
	
	@Column(name = "CURRENCY")
	private String currency;
	
	@Column(name = "TOTAL")
	private long total;
	
	@Column(name = "START_DATE")
	private Date startDate;
	
	@Column(name = "END_DATE")
	private Date endDate;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "EXECUTED")
	private long executed;
	
	@Column(name = "BALANCE")
	private long balance;
	
	@Column(name = "CREATION_DATE")
	private Date creationDate;
	
	@Column(name = "CUSTOMER_NAME")
	private String customerName;
}
