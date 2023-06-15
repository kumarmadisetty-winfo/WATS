package com.winfo.exception;

import org.springframework.stereotype.Component;

@Component
public class WatsEBSCustomException extends RuntimeException {
	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;
	private final int code;
	private final String errorDetails;

	public WatsEBSCustomException(int code, String errorDetails) {
		super(errorDetails);
		this.code = code;
		this.errorDetails = errorDetails;
	}

	public WatsEBSCustomException(int code, String errorDetails, Throwable e) {
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
	
	public WatsEBSCustomException() {
		this.code = 400;
		this.errorDetails = "Error";
	}

}
