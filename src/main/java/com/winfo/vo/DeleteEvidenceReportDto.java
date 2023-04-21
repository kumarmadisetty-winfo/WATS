package com.winfo.vo;

import java.util.List;

import lombok.Data;

@Data
public class DeleteEvidenceReportDto {

	private String testSetId;
	private List<String> testSetLineId;
	private boolean isTestRunDelete;

}
