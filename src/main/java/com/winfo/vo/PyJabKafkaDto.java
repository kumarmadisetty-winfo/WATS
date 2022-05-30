//package com.winfo.vo;
//
//import java.io.Serializable;
//import java.util.Date;
//
//import javax.validation.constraints.NotNull;
//
//import com.fasterxml.jackson.annotation.JsonFormat;
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.annotation.JsonInclude.Include;
//
//@JsonInclude(Include.NON_NULL)
//public class PyJabKafkaDto implements Serializable {
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 6396022101714194229L;
//	@NotNull
//	private String testSetId;
//	@NotNull
//	private String testSetLineId;
//	private String scriptPath;
//	private boolean success;
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
//	private Date startDate;
//	private String localScreenshotPath;
//	private String obJectStoreScreenshotPath;
//	private boolean manualTrigger;
//	
//	public PyJabKafkaDto() {
//	}
//	
//
//	public PyJabKafkaDto(@NotNull String testSetId, @NotNull String testSetLineId, String scriptPath,
//			String localScreenshotPath, String obJectStoreScreenshotPath) {
//		super();
//		this.testSetId = testSetId;
//		this.testSetLineId = testSetLineId;
//		this.scriptPath = scriptPath;
//		this.localScreenshotPath = localScreenshotPath;
//		this.obJectStoreScreenshotPath = obJectStoreScreenshotPath;
//	}
//
//	public String getTestSetId() {
//		return testSetId;
//	}
//
//	public void setTestSetId(String testSetId) {
//		this.testSetId = testSetId;
//	}
//
//	public String getTestSetLineId() {
//		return testSetLineId;
//	}
//
//	public void setTestSetLineId(String testSetLineId) {
//		this.testSetLineId = testSetLineId;
//	}
//
//	public String getScriptPath() {
//		return scriptPath;
//	}
//
//	public void setScriptPath(String scriptPath) {
//		this.scriptPath = scriptPath;
//	}
//
//
//	public boolean isSuccess() {
//		return success;
//	}
//
//	public void setSuccess(boolean success) {
//		this.success = success;
//	}
//
//	public Date getStartDate() {
//		return startDate;
//	}
//
//	public void setStartDate(Date startDate) {
//		this.startDate = startDate;
//	}
//
//	public String getLocalScreenshotPath() {
//		return localScreenshotPath;
//	}
//
//	public void setLocalScreenshotPath(String localScreenshotPath) {
//		this.localScreenshotPath = localScreenshotPath;
//	}
//
//	public String getObJectStoreScreenshotPath() {
//		return obJectStoreScreenshotPath;
//	}
//
//	public void setObJectStoreScreenshotPath(String obJectStoreScreenshotPath) {
//		this.obJectStoreScreenshotPath = obJectStoreScreenshotPath;
//	}
//
//
//	public boolean isManualTrigger() {
//		return manualTrigger;
//	}
//
//
//	public void setManualTrigger(boolean manualTrigger) {
//		this.manualTrigger = manualTrigger;
//	}
//
//}
