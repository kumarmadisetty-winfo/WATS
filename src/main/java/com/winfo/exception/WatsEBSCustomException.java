package com.winfo.exception;

import org.springframework.stereotype.Component;

@Component
public class WatsEBSCustomException extends RuntimeException {
	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;
	private int errorCode;
	private String errorMessage;
	
	public WatsEBSCustomException(int errorCode, String errorMessage) {
		super(errorMessage);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		
	}

	public WatsEBSCustomException(int errorCode, String errorMessage, Exception cause) {
		super(errorMessage);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.initCause(cause);
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public WatsEBSCustomException() {

	}

}
