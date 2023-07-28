package com.winfo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "WIN_TA_CONFIG")
@Data
public class ConfigTable {
	
	@Id
	@Column(name = "CONFIGURATION_ID")
	private Integer configurationId;
	
//	@Column(name = "JIRA_ISSUE_URL")
//	private String jiraIssueUrl;

	@Column(name = "CUSTOMER_ID")
	private Integer customerId;
	
}
