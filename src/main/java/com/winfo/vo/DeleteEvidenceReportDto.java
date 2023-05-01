package com.winfo.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DeleteEvidenceReportDto {

	private String testSetId;
	private List<String> testSetLineId;
	
	@JsonProperty("isTestRunDelete")
	private boolean isTestRunDelete;

}
