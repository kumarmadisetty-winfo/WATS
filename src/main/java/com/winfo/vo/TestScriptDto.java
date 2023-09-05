package com.winfo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TestScriptDto {

	private String testScriptNo = "false";
	
	private String jobId;

	private String executedBy;

}
