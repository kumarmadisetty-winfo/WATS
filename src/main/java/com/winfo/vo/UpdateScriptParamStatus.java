package com.winfo.vo;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UpdateScriptParamStatus {
	
	@NotNull
	private String scriptParamId;
	private String message = "";
	private boolean success;
	private String result;

}
