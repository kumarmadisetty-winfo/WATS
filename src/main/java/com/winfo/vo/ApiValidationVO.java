package com.winfo.vo;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiValidationVO {

	@JsonProperty("URL")
	private String url;
	@JsonProperty("HTTP Type")
	private String httpType;
	@JsonProperty("Request Header")
	private Map<String, String> requestHeader;
	@JsonProperty("Request Body")
	private Object requestBody;
	@JsonProperty("Response")
	private String response;
	@JsonProperty("Response Code")
	private Integer responseCode;
	private String accessToken;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHttpType() {
		return httpType;
	}

	public void setHttpType(String httpType) {
		this.httpType = httpType;
	}

	public Map<String, String> getRequestHeader() {
		return requestHeader;
	}

	public void setRequestHeader(Map<String, String> requestHeader) {
		this.requestHeader = requestHeader;
	}


	public Object getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(Object requestBody) {
		this.requestBody = requestBody;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public Integer getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(Integer responseCode) {
		this.responseCode = responseCode;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	

	public ApiValidationVO(String url, String httpType, Map<String, String> requestHeader, Object requestBody,
			String response, Integer responseCode, String accessToken) {
		this.url = url;
		this.httpType = httpType;
		this.requestHeader = requestHeader;
		this.requestBody = requestBody;
		this.response = response;
		this.responseCode = responseCode;
		this.accessToken = accessToken;
	}

	public ApiValidationVO() {
	}

}