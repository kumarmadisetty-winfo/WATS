package com.winfo.vo;
import java.sql.Date;
import java.util.List;

import lombok.Data;

@Data
public class ScheduleTestRunVO {
	
	private String newTestRunName;
	
	private String templateTestRun;
	
	private String notification;

	private String autoIncrement;

	private List<String> dependencies;

	private Date startDate;

}
