package com.winfo.vo;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UpdateScriptParamStatus {
	
	@NotNull
	private String scriptParamId;
	private String message = "";
	
	@JsonProperty("success")
	private boolean success;
	private String result;

}
