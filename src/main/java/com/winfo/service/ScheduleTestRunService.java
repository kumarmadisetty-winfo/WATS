package com.winfo.service;

import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.winfo.vo.ResponseDto;
import com.winfo.vo.ScheduleJobVO;

@Service
public interface ScheduleTestRunService {
	
	public  ResponseDto createNewScheduledJob(ScheduleJobVO scheduleJobVO);
}
