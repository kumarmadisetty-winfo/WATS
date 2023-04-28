package com.winfo.vo;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UpdateScriptStepStatus {

	@NotNull
	private String scriptParamId;
	private String message = "";
	private String result;
	private String status;

}
