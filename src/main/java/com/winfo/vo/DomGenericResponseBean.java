package com.winfo.vo;

public class DomGenericResponseBean{
	
	private int status;
	private String statusMessage;
	private String description;
	private String failed_Script;
	private int scriptID;
	
	
	
	
	
	public int getScriptID() {
		return scriptID;
	}
	public void setScriptID(int scriptID) {
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
