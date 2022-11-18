package com.winfo.vo;

import javax.validation.constraints.NotBlank;

public class VersionHistoryDto {
	
	private String versionNumber;
	@NotBlank
	private Integer scriptId;
	public String getVersionNumber() {
		return versionNumber;
	}
	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}
	public Integer getScriptId() {
		return scriptId;
	}
	public void setScriptId(Integer scriptId) {
		this.scriptId = scriptId;
	}
	
	

}
