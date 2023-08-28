package com.winfo.serviceImpl;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.winfo.dao.DataBaseEntryDao;
import com.winfo.model.Scheduler;
import com.winfo.model.TestSetExecutionStatus;
import com.winfo.model.UserSchedulerJob;
import com.winfo.repository.ExecuteStatusRepository;
import com.winfo.repository.SchedulerRepository;
import com.winfo.repository.TestSetExecutionStatusRepository;
import com.winfo.repository.TestSetLinesRepository;
import com.winfo.repository.TestSetRepository;
import com.winfo.repository.TestSetScriptParamRepository;
import com.winfo.repository.UserRepository;
import com.winfo.repository.UserSchedulerJobRepository;
import com.winfo.vo.EmailParamDto;

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
	
	@Autowired
	private TestSetLinesRepository testSetLinesRepository;

	@Autowired
	private TestSetScriptParamRepository testSetScriptParamRepository;

	@Autowired
	private TestSetExecutionStatusRepository testSetExecutionStatusRepository;

	public void updateDependencyTestRunDetails(int testSetId, String executedBy, String testSetName, Integer jobId,
			 TestSetExecutionStatus testSetExecutionStatus) {
		testSetRepo.updateTestRunExecution(executedBy, testSetId,new Date());
		executeStatusRepo.updateTestRunExecutionStatus(executedBy, testSetName, testSetId);
		testSetLinesRepository.updateStatusStartTimeEndTimeTetSetLines(testSetId);
		testSetScriptParamRepository.updateLineExecutiStatusAndLineErrorMsg(testSetId);
		testSetLinesRepository.updateTestRunScriptEnable(testSetId);
		insertTestSetExecutionStatusRecord(testSetExecutionStatus);
		InitiationSendMail(testSetName, jobId, executedBy, testSetId);
		
	}

	@Transactional
	public void InitiationSendMail(String testSetName, Integer jobId, String executedBy, int testSetId) {
		EmailParamDto emailParam = new EmailParamDto();
		emailParam.setTestSetName(testSetName);
		dataBaseEntryDao.getUserAndPrjManagerName(executedBy, String.valueOf(testSetId), emailParam);
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

	@Transactional
	public void insertTestSetExecutionStatusRecord(TestSetExecutionStatus testSetExecutionStatus) {
		testSetExecutionStatusRepository.save(testSetExecutionStatus);
	}

}
