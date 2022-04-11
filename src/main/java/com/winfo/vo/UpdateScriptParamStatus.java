package com.winfo.vo;

import javax.validation.constraints.NotNull;

public class UpdateScriptParamStatus {
	@NotNull
	private String scriptParamId;
	@NotNull
	private String message;
	private boolean success;
	private String result;

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

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
}
