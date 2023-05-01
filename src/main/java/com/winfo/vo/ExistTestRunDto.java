package com.winfo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ExistTestRunDto {
	
	private Integer testSetId;
	
	@JsonProperty("forceMigrate")
	private boolean forceMigrate = false;

}
