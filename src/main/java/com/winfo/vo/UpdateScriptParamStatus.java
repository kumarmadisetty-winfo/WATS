package com.winfo.vo;

import javax.validation.constraints.NotNull;

public class UpdateScriptParamStatus {
	@NotNull
	private String scriptParamId;
	@NotNull
	private String message;
	private boolean success;

	public String getScriptParamId() {
		return scriptParamId;
	}

	public void setScriptParamId(String scriptParamId) {
		this.scriptParamId = scriptParamId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

}
