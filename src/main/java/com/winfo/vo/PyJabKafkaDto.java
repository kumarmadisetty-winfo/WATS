package com.winfo.vo;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

public class PyJabKafkaDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6396022101714194229L;
	@NotNull
	private String testSetId;
	@NotNull
	private String testSetLineId;
	private String scriptPath;
	private boolean pass;
	private Date startTime;
	private String localScreenshotPath;
	private String obJectStoreScreenshotPath;
	

	public PyJabKafkaDto(@NotNull String testSetId, @NotNull String testSetLineId, String scriptPath,
			String localScreenshotPath, String obJectStoreScreenshotPath) {
		super();
		this.testSetId = testSetId;
		this.testSetLineId = testSetLineId;
		this.scriptPath = scriptPath;
		this.localScreenshotPath = localScreenshotPath;
		this.obJectStoreScreenshotPath = obJectStoreScreenshotPath;
	}

	public String getTestSetId() {
		return testSetId;
	}

	public void setTestSetId(String testSetId) {
		this.testSetId = testSetId;
	}

	public String getTestSetLineId() {
		return testSetLineId;
	}

	public void setTestSetLineId(String testSetLineId) {
		this.testSetLineId = testSetLineId;
	}

	public String getScriptPath() {
		return scriptPath;
	}

	public void setScriptPath(String scriptPath) {
		this.scriptPath = scriptPath;
	}

	public boolean isPass() {
		return pass;
	}

	public void setPass(boolean pass) {
		this.pass = pass;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getLocalScreenshotPath() {
		return localScreenshotPath;
	}

	public void setLocalScreenshotPath(String localScreenshotPath) {
		this.localScreenshotPath = localScreenshotPath;
	}

	public String getObJectStoreScreenshotPath() {
		return obJectStoreScreenshotPath;
	}

	public void setObJectStoreScreenshotPath(String obJectStoreScreenshotPath) {
		this.obJectStoreScreenshotPath = obJectStoreScreenshotPath;
	}


}
