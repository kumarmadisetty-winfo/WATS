package com.winfo.vo;

import lombok.Data;

@Data
public class ScriptValidationResponseVO {

	private String errorMessage;

	private Integer paramId;
	
	public ScriptValidationResponseVO(Integer paramId,String errorMessage) {
		this.errorMessage=errorMessage;
		this.paramId=paramId;
	}
}
