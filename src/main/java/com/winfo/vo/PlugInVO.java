package com.winfo.vo;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class PlugInVO {
	
	@JsonInclude(Include.NON_NULL)
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