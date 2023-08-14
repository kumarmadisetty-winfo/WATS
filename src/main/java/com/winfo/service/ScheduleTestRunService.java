package com.winfo.service;

import org.springframework.stereotype.Service;

import com.winfo.vo.ResponseDto;
import com.winfo.vo.ScheduleJobVO;

@Service
public interface ScheduleTestRunService {
	
	public  ResponseDto createNewScheduledJob(ScheduleJobVO scheduleJobVO);
	
	public  ResponseDto editScheduledJob(ScheduleJobVO scheduleJobVO);
}
