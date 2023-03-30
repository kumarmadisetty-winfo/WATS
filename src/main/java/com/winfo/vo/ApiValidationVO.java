package com.winfo.vo;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
	@JsonProperty("Authentication Type")
	private String authenticationType;

}