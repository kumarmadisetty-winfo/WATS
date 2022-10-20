package com.winfo.vo;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PlugInVO {
	
	@JsonIgnore
	private String targetEnvironment;
	
	private List<Map<String,String>> groups;

	public String getTargetEnvironment() {
		return targetEnvironment;
	}

	public void setTargetEnvironment(String targetEnvironment) {
		this.targetEnvironment = targetEnvironment;
	}

	public List<Map<String, String>> getGroups() {
		return groups;
	}

	public void setGroups(List<Map<String, String>> groups) {
		this.groups = groups;
	}

}
