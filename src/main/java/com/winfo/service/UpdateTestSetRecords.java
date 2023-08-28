package com.winfo.service;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.winfo.dao.DataBaseEntryDao;
import com.winfo.model.Scheduler;
import com.winfo.model.UserSchedulerJob;
import com.winfo.repository.ExecuteStatusRepository;
import com.winfo.repository.SchedulerRepository;
import com.winfo.repository.TestSetRepository;
import com.winfo.repository.UserRepository;
import com.winfo.repository.UserSchedulerJobRepository;
import com.winfo.serviceImpl.SendMailServiceImpl;
import com.winfo.vo.EmailParamDto;
import org.apache.commons.lang3.StringUtils;

@Service
public class UpdateTestSetRecords {

	
	
	
	
	
	
	
	
	
	
	@Autowired
	TestSetRepository testSetRepo;

	@Autowired
	ExecuteStatusRepository executeStatusRepo;

	@Autowired
	SendMailServiceImpl sendMailService;

	@Autowired
	UserRepository userRepository;
	@Autowired
	UserSchedulerJobRepository userSchedulerJobRepository;
	@Autowired
	DataBaseEntryDao dataBaseEntryDao;

	@Autowired
	SchedulerRepository schedulerRepository;

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void updateDependencyTestRunDetails(String testSetId, String executedBy,
			String testSetName, Integer jobId, String userEmail) {
		testSetRepo.updateTestRunExecution(executedBy, testSetId);
		executeStatusRepo.updateTestRunExecutionStatus(executedBy, testSetName, Integer.parseInt(testSetId));
		InitiationSendMail(testSetName, jobId, executedBy, testSetId);
	}

	public void InitiationSendMail(String testSetName, Integer jobId, String executedBy, String testSetId) {
		EmailParamDto emailParam = new EmailParamDto();
		emailParam.setTestSetName(testSetName);
		dataBaseEntryDao.getUserAndPrjManagerName(executedBy, testSetId, emailParam);
		String userEmail = userRepository.findByUserId(executedBy.toUpperCase());
		String testRunEmails = null;
		Optional<UserSchedulerJob> userSchedulerJob = userSchedulerJobRepository.findByJobIdAndComments(jobId,
				testSetName);
		if (userSchedulerJob.isPresent()) {
			testRunEmails = userSchedulerJob.get().getClientId();
			if (testRunEmails != null) {
				String listOfEmails = Arrays.asList(testRunEmails).stream()
						.filter(email -> !(userEmail.equalsIgnoreCase(email))).collect(Collectors.joining(","));
				if (StringUtils.isNotBlank(listOfEmails)) {
					emailParam.setReceiver(listOfEmails);
				}
			} else {
				Scheduler scheduler = schedulerRepository.findByJobId(jobId);
				if (!userEmail.equalsIgnoreCase(scheduler.getEmail())) {
					emailParam.setReceiver(scheduler.getEmail());
				}
			}
		}
		sendMailService.initiationSendMail(emailParam);
	}

}
