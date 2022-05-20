package com.winfo.exception;

public class ErrorDetail {
	private int code;
	private String errorDetails;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getErrorDetails() {
		return errorDetails;
	}

	public void setErrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}

	public ErrorDetail(int code, String errorDetails) {
		super();
		this.code = code;
		this.errorDetails = errorDetails;
	}

}