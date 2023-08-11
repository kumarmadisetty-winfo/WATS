package com.winfo.vo;
import java.sql.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ScheduleTestRunVO {
	
	private String testRunName;
	
	private String templateTestRun;
	
	private String notification;

	private String autoIncrement;

	private List<String> dependencies;

	private String startDate;

}
