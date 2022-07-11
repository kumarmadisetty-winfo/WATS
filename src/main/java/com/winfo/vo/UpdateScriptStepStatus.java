package com.winfo.vo;

import javax.validation.constraints.NotNull;

public class UpdateScriptStepStatus {
	@NotNull
	private String scriptParamId;
	private String message="";
	private String result;
	private String status;

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

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
