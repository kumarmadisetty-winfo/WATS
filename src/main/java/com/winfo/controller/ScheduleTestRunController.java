package com.winfo.controller;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.winfo.vo.ResponseDto;
import com.winfo.vo.ScheduleJobVO;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import com.winfo.exception.WatsEBSException;
import com.winfo.service.ScheduleTestRunService;


@RestController
public class ScheduleTestRunController {
	@Autowired
	ScheduleTestRunService scheduleTestRunService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/schedule ")
	@ApiOperation( value="Create new scheduled job for group of test run",notes = "")
	@ApiResponses( value = { @ApiResponse( code=200,message="Successfully created the scheduled job")})
	public  ResponseEntity createScheduledJob(@RequestBody ScheduleJobVO scheduleJobVO) throws ParseException {
		ResponseDto responseDto =scheduleTestRunService.createNewScheduledJob(scheduleJobVO); 
		if (responseDto.getStatusCode() == HttpStatus.OK.value()) {
			return ResponseEntity.ok(responseDto);
		} else {
			return new ResponseEntity(
					new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error occured while scheduling a job"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping("/schedule ")
	@ApiOperation( value="edit scheduled job for group of test run",notes = "")
	@ApiResponses( value = { @ApiResponse( code=200,message="Successfully updated the scheduled job")})
	public  ResponseEntity editScheduledJob(@RequestBody ScheduleJobVO scheduleJobVO) throws ParseException {
		ResponseDto responseDto =scheduleTestRunService.editScheduledJob(scheduleJobVO); 
		if (responseDto.getStatusCode() == HttpStatus.OK.value()) {
			return ResponseEntity.ok(responseDto);
		} else {
			return new ResponseEntity(
					new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error occured while scheduling a job"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}



