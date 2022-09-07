package com.winfo.vo;

import java.util.List;

public class PyJabScriptDto {

	private String applicationName;
	private List<String> actions;
	private String scriptStatusUpdateUrl;
	private String copiedValueUrl;
	private String chromeDriverPath;
	private String ebsApplicationUrl;
	private String dllPath;
	private String ociConfigPath;
	private String ociConfigName;
	private String buckerName;
	private String ociNameSpace;
	private String scriptFileName;
	private String downloadPath;

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

	public String getCopiedValueUrl() {
		return copiedValueUrl;
	}

	public void setCopiedValueUrl(String copiedValueUrl) {
		this.copiedValueUrl = copiedValueUrl;
	}

	public String getChromeDriverPath() {
		return chromeDriverPath;
	}

	public void setChromeDriverPath(String chromeDriverPath) {
		this.chromeDriverPath = chromeDriverPath;
	}

	public String getEbsApplicationUrl() {
		return ebsApplicationUrl;
	}

	public void setEbsApplicationUrl(String ebsApplicationUrl) {
		this.ebsApplicationUrl = ebsApplicationUrl;
	}

	public String getDllPath() {
		return dllPath;
	}

	public void setDllPath(String dllPath) {
		this.dllPath = dllPath;
	}

	public String getOciConfigPath() {
		return ociConfigPath;
	}

	public void setOciConfigPath(String ociConfigPath) {
		this.ociConfigPath = ociConfigPath;
	}

	public String getOciConfigName() {
		return ociConfigName;
	}

	public void setOciConfigName(String ociConfigName) {
		this.ociConfigName = ociConfigName;
	}

	public String getBuckerName() {
		return buckerName;
	}

	public void setBuckerName(String buckerName) {
		this.buckerName = buckerName;
	}

	public String getOciNameSpace() {
		return ociNameSpace;
	}

	public void setOciNameSpace(String ociNameSpace) {
		this.ociNameSpace = ociNameSpace;
	}

	public String getScriptFileName() {
		return scriptFileName;
	}

	public void setScriptFileName(String scriptFileName) {
		this.scriptFileName = scriptFileName;
	}

	public String getDownloadPath() {
		return downloadPath;
	}

	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}

}
