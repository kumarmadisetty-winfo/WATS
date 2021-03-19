package com.winfo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WIN_TA_CONFIG")

public class ConfigTable {
	@Id
	@Column(name = "CONFIGURATION_ID")
	private Integer configuration_id;
	
	
	
	@Column(name = "JIRA_ISSUE_URL")
	private String jira_issue_url;

	public Integer getConfiguration_id() {
		return configuration_id;
	}

	public void setConfiguration_id(Integer configuration_id) {
		this.configuration_id = configuration_id;
	}

	public String getJira_issue_url() {
		return jira_issue_url;
	}

	public void setJira_issue_url(String jira_issue_url) {
		this.jira_issue_url = jira_issue_url;
	}
	
	
	
}
