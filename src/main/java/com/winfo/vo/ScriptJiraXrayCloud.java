package com.winfo.vo;

import java.util.List;

public class ScriptJiraXrayCloud {

	private String summary;
	private String projectKey;
	private List<ScriptStepsJiraXrayCloud> listOfStepsWithScript;
	private String testRunIssueId;
	private String scriptIssueId;
	private String scriptId;
	private String status;
	private String inputStream;
	private String fileName;
	
	
	public String getInputStream() {
		return inputStream;
	}

	public void setInputStream(String inputStream) {
		this.inputStream = inputStream;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	public String getScriptId() {
		return scriptId;
	}

	public void setScriptId(String scriptId) {
		this.scriptId = scriptId;
	}


	public String getTestRunIssueId() {
		return testRunIssueId;
	}

	public void setTestRunIssueId(String testRunIssueId) {
		this.testRunIssueId = testRunIssueId;
	}

	public String getScriptIssueId() {
		return scriptIssueId;
	}

	public void setScriptIssueId(String scriptIssueId) {
		this.scriptIssueId = scriptIssueId;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getProjectKey() {
		return projectKey;
	}

	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}

	public List<ScriptStepsJiraXrayCloud> getListOfStepsWithScript() {
		return listOfStepsWithScript;
	}

	public void setListOfStepsWithScript(List<ScriptStepsJiraXrayCloud> listOfStepsWithScript) {
		this.listOfStepsWithScript = listOfStepsWithScript;
	}

	public ScriptJiraXrayCloud(String summary, String projectKey,
			List<ScriptStepsJiraXrayCloud> listOfStepsWithScript) {
		super();
		this.summary = summary;
		this.projectKey = projectKey;
		this.listOfStepsWithScript = listOfStepsWithScript;
	}

	public ScriptJiraXrayCloud() {
		super();
		// TODO Auto-generated constructor stub
	}

}
