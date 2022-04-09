package com.winfo.vo;

import java.util.List;

public class PyJabScriptDto {

	private String applicationName;
	private List<String> actions;
	private String scriptStatusUpdateUrl;

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public List<String> getActions() {
		return actions;
	}

	public void setActions(List<String> actions) {
		this.actions = actions;
	}

	public String getScriptStatusUpdateUrl() {
		return scriptStatusUpdateUrl;
	}

	public void setScriptStatusUpdateUrl(String scriptStatusUpdateUrl) {
		this.scriptStatusUpdateUrl = scriptStatusUpdateUrl;
	}

}
