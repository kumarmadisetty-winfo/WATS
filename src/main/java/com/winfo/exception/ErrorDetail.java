package com.winfo.exception;

import java.util.Date;

public class ErrorDetail {
//	private Date timestamp;
	private int code;
	private String errorDetails;

//	public Date getTimestamp() {
//		return timestamp;
//	}
//
//	public void setTimestamp(Date timestamp) {
//		this.timestamp = timestamp;
//	}

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
//		this.timestamp = timestamp;
		this.code = code;
		this.errorDetails = errorDetails;
	}

}