package com.winfo.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {

	private int statusCode;
	private String statusMessage;
	private String statusDescr;
	private List<ScriptValidationResponseVO> validation;
	
	public ResponseDto(int statusCode,String statusMessage,String statusDescr){
		this.statusCode=statusCode;
		this.statusMessage=statusMessage;
		this.statusDescr=statusDescr;
	}

}
