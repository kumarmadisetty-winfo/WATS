package com.winfo.exception;

import org.springframework.stereotype.Component;

@Component
public class WatsEBSException extends RuntimeException {
	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;
	private final int code;
	private final String errorDetails;

	public WatsEBSException(int code, String errorDetails) {
		super(errorDetails);
		this.code = code;
		this.errorDetails = errorDetails;
	}

	public WatsEBSException(int code, String errorDetails, Throwable e) {
		super(e);
		this.code = code;
		this.errorDetails = errorDetails;
	}

	public int getErrorCode() {
		return code;
	}


	public String getErrorMessage() {
		return errorDetails;
	}
	
	public WatsEBSException() {
		this.code = 400;
		this.errorDetails = "Error";
	}

}
