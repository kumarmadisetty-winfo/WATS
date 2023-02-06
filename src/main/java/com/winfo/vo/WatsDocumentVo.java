package com.winfo.vo;

import javax.validation.constraints.NotNull;


public class WatsDocumentVo {

	@NotNull
	private String fileName;
	
	@NotNull
	private String watsVersion;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getWatsVersion() {
		return watsVersion;
	}

	public void setWatsVersion(String watsVersion) {
		this.watsVersion = watsVersion;
	}
}
