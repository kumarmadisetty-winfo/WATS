package com.winfo.vo;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class VersionHistoryDto {

	private String versionNumber;

	@NotNull
	private Integer scriptId;
	
	@JsonIgnore
	private boolean isSaveHistory;

}
