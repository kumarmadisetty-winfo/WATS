package com.winfo.vo;
import java.sql.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ScheduleTestRunVO {
	
	private String newTestRunName;
	
	private String templateTestRun;
	
	private String notification;

	private String autoIncrement;

	private List<String> dependencies;

//	@JsonFormat(pattern = "MM/dd/yyyy")
	private String startDate;

}
