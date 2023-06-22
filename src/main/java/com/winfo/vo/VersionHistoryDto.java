package com.winfo.vo;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class VersionHistoryDto {

	private String versionNumber;

	@NotNull
	private Integer scriptId;
	
	private boolean isSaveHistory;

}
