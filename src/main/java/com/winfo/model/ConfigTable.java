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
	private Integer configurationId;
	
	
	
	@Column(name = "JIRA_ISSUE_URL")
	private String jiraIssueUrl;

	public Integer getConfigurationId() {
		return configurationId;
	}

	public void setConfigurationId(Integer configurationId) {
		this.configurationId = configurationId;
	}

	public String getJiraIssueUrl() {
		return jiraIssueUrl;
	}

	public void setJiraIssueUrl(String jiraIssueUrl) {
		this.jiraIssueUrl = jiraIssueUrl;
	}
	
	
	
}
