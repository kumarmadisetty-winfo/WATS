package com.winfo.vo;
import java.util.List;
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
