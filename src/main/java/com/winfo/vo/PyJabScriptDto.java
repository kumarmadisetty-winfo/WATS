package com.winfo.vo;

import java.util.List;

import lombok.Data;

@Data
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
	private String excelDownloadFilePath;

}
