package com.winfo.vo;

import java.util.List;

import lombok.Data;

@Data
public class ScheduleJobVO {
	

	private String schedulerName;
	
	private String schedulerEmail;
	
	private int configurationId;

	private int projectId;
	
	private List<ScheduleTestRunVO> testRuns;
	
	private int jobId;
	
	private String status;
}
