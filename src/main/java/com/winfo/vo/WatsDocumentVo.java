package com.winfo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class WatsDocumentVo {

	@JsonInclude(Include.NON_NULL)
	private String fileName;
	
	@JsonInclude(Include.NON_NULL)
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
