package com.winfo.controller;

import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.winfo.exception.WatsEBSException;
import com.winfo.service.ScheduleTestRunService;
import com.winfo.utils.Constants;
import com.winfo.vo.ResponseDto;
import com.winfo.vo.ScheduleJobVO;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@RestController
public class ScheduleTestRunController {
	@Autowired
	ScheduleTestRunService scheduleTestRunService;
	
	
	public static final Logger logger = Logger.getLogger(ScheduleTestRunController.class);

	@PostMapping("/schedule")
	@ApiOperation( value="Create new scheduled job for group of test run",notes = "")
	@ApiResponses( value = { @ApiResponse( code=200,message="Successfully created the scheduled job")})
	public  ResponseEntity<ResponseDto> createScheduledJob(@RequestBody ScheduleJobVO scheduleJobVO) throws ParseException {
		ResponseDto responseDto =scheduleTestRunService.createNewScheduledJob(scheduleJobVO); 
		
		if (responseDto.getStatusCode() == HttpStatus.OK.value()) {
			return ResponseEntity.ok(responseDto);
		} else {
			return new ResponseEntity<ResponseDto>(responseDto,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping("/schedule")
	@ApiOperation( value="edit scheduled job for group of test run",notes = "")
	@ApiResponses( value = { @ApiResponse( code=200,message="Successfully updated the scheduled job")})
	public  ResponseEntity<ResponseDto> editScheduledJob(@RequestBody ScheduleJobVO scheduleJobVO) throws ParseException {
		ResponseDto responseDto =scheduleTestRunService.editScheduledJob(scheduleJobVO); 
		if (responseDto.getStatusCode() == HttpStatus.OK.value()) {
			return ResponseEntity.ok(responseDto);
		} else {
			return new ResponseEntity<ResponseDto>(responseDto,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/scheduleReport/{jobId}")
	@ApiOperation( value="Generate schedule summary testrun report",notes = "To generate schedule summary testrun report, we should pass jobId")	
	@ApiResponses( value = { @ApiResponse( code=200,message="Generated schedule summary testrun report succesfully")})
	public ResponseDto generateScheduleSummaryTestRunReport(@PathVariable int jobId){
		return scheduleTestRunService.generateScheduleSummaryTestRunReport(jobId);

	}
}



