package com.winfo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailParamDto {

	private String receiver;

	private String ccPerson;

	private Integer requestCount;

	private Integer passCount;

	private Integer failCount;

	private String testSetName;

	private String executedBy;
	
	private String jobName;

}
