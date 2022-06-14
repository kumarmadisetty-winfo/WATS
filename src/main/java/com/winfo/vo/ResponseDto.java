package com.winfo.vo;

public class ResponseDto {
	private int statusCode;
	private String statusMessage;
	private String statusDescr;
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getStatusMessage() {
		return statusMessage;
	}
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	public String getStatusDescr() {
		return statusDescr;
	}
	public void setStatusDescr(String statusDescr) {
		this.statusDescr = statusDescr;
	}
	public ResponseDto(int statusCode, String statusMessage, String statusDescr) {
		super();
		this.statusCode = statusCode;
		this.statusMessage = statusMessage;
		this.statusDescr = statusDescr;
	}
	public ResponseDto() {
		super();
	}
	

}
