package com.winfo.exception;

public class PopupException extends RuntimeException {

	/**
	
	 *
	
	 */

	private static final long serialVersionUID = 1L;

	private int code;

	private String msg;

	public PopupException(int code, String msg) {

		this.code = code;

		this.msg = msg;

	}

}