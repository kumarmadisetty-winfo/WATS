package com.winfo.serviceImpl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.winfo.dao.DataBaseEntryDao;
import com.winfo.exception.WatsEBSException;
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
	
	public static final Logger logger = Logger.getLogger(UpdateTestSetRecords.class);

	public void updateDependencyTestRunDetails(int testSetId, String executedBy, String testSetName, Integer jobId,
			 TestSetExecutionStatus testSetExecutionStatus) {
		try {
			testSetRepo.updateTestRunExecution(executedBy, testSetId, new Date());
			executeStatusRepo.updateTestRunExecutionStatus(executedBy, testSetName, testSetId);
			testSetLinesRepository.updateStatusStartTimeEndTimeTetSetLines(testSetId);
			testSetScriptParamRepository.updateLineExecutiStatusAndLineErrorMsg(testSetId);
			testSetLinesRepository.updateTestRunScriptEnable(testSetId);
			List<TestSetExecutionStatus> testSetExcecutionStatus = testSetExecutionStatusRepository
					.findByTestRunId(testSetId);
			if (testSetExcecutionStatus.size() == 0) {
				insertTestSetExecutionStatusRecord(testSetExecutionStatus);
			} else {
				dataBaseEntryDao.updateExecStatusTable(String.valueOf(testSetId));
			}
//			initiationSendMail(testSetName, jobId, executedBy, testSetId);
		} catch (Exception e) {
			logger.error("Exception occurred while updating dependency testRun detailes");
		}
	}

	@Transactional
	public void initiationSendMail(String testSetName, Integer jobId, String executedBy, int testSetId) {
		try {
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
		}catch(WatsEBSException e) {
			logger.error("Failed during sending schedule testrun initiation mail ");
		}
		
	}

	@Transactional
	public void insertTestSetExecutionStatusRecord(TestSetExecutionStatus testSetExecutionStatus) {
		testSetExecutionStatusRepository.save(testSetExecutionStatus);
	}

}
