package com.winfo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class DomGenericResponseBean{
	
	private int status;
	private String statusMessage;
	private String description;
	
	@JsonInclude(Include.NON_NULL)
	private String failed_Script;
	
	@JsonInclude(Include.NON_NULL)
	private Integer scriptID;
	
	@JsonInclude(Include.NON_NULL)
	private String testRunName;
	
	public DomGenericResponseBean() {
		
	}
	
	public DomGenericResponseBean(int status , String statusMessage, String description) {
		this.status =status;
		this.statusMessage = statusMessage;
		this.description = description;
	}
	
	public String getTestRunName() {
		return testRunName;
	}
	public void setTestRunName(String testRunName) {
		this.testRunName = testRunName;
	}
	public Integer getScriptID() {
		return scriptID;
	}
	public void setScriptID(Integer scriptID) {
		this.scriptID = scriptID;
	}
	public String getFailed_Script() {
		return failed_Script;
	}
	public void setFailed_Script(String failed_Script) {
		this.failed_Script = failed_Script;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
		public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getStatusMessage() {
		return statusMessage;
	}
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
		
}
