package com.winfo.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.winfo.exception.WatsEBSException;
import com.winfo.reports.PDFGenerator;
import com.winfo.repository.ConfigLinesRepository;
import com.winfo.repository.ConfigurationRepository;
import com.winfo.repository.CustomerRepository;
import com.winfo.repository.SchedulerRepository;
import com.winfo.repository.TestSetRepository;
import com.winfo.repository.UserSchedulerJobRepository;
import com.winfo.service.ScheduleTestRunService;
import com.winfo.serviceImpl.DataBaseEntry;
import com.winfo.serviceImpl.TestScriptExecService;
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
	@Autowired
	UserSchedulerJobRepository userSchedulerJobRepository;
	@Autowired
	TestSetRepository testSetRepository;
	@Autowired
	DataBaseEntry dataBaseEntry;
	@Autowired
	TestScriptExecService testScriptExecService;
	@Autowired
	SchedulerRepository schedulerRepository;
	@Autowired
	ConfigurationRepository configurationRepository;
	@Autowired
	ConfigLinesRepository configLinesRepository;
	@Autowired
	CustomerRepository customerRepository;
	
	public static final Logger logger = Logger.getLogger(ScheduleTestRunController.class);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/schedule")
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
	@PutMapping("/schedule")
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
	
	@PostMapping(value = "/generateScheduleTestRunReport/{jobId}")
	@ApiOperation( value="Generate Schedule TestRun Report",notes = "To generate Schedule TestRun Report(Detailed Report), we should pass jobId")	
	@ApiResponses( value = { @ApiResponse( code=200,message="Generated Schedule TestRun Report Succesfully")})
	public ResponseDto generateScheduleTestRunReport(@PathVariable int jobId){
		ResponseDto response = null;
		try {
			logger.info("Started schedule testrun report regeneration : " + jobId);
			int configId = schedulerRepository.findByJobId(jobId).getConfigurationId();
			String pdfPath=configLinesRepository.getPdfPathusingConfigurationIdAndkeyName(configId);
			int customerId = configurationRepository.getCustomerIdUsingconfigurationId(configId);
			String customerName = customerRepository.findByCustomerId(customerId).getCustomerName();
			logger.info("PdfPath : " + pdfPath + "," + " customerName : " +customerName);
			if (StringUtils.isNotBlank(pdfPath) && StringUtils.isNotBlank(customerName)) {
				response=PDFGenerator.createPDF(jobId, pdfPath, customerName);
				if(response.getStatusCode()==200) {
				logger.info("Schedule summary report regeneration has done successfully : " + jobId);
				}
			} else {
				logger.info("Pdf path and customerName should not be null");
				response = new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.ERROR, "Exception occured while regenerating the schedule summary report");
			}
		} catch (Exception e) {
			logger.error("Exception occured while regenerating the schedule summary report : " + jobId);
		}
		return response;

	}
}



