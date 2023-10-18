package com.winfo.constraintImpl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.winfo.constraint.ScheduleAPIValidation;
import com.winfo.model.Scheduler;
import com.winfo.model.TestSet;
import com.winfo.model.UserSchedulerJob;
import com.winfo.repository.ConfigLinesRepository;
import com.winfo.repository.LookUpCodeRepository;
import com.winfo.repository.ProjectRepository;
import com.winfo.repository.SchedulerRepository;
import com.winfo.repository.TestSetRepository;
import com.winfo.repository.UserSchedulerJobRepository;
import com.winfo.utils.Constants;
import com.winfo.utils.StringUtils;

@Component
public class ScheduleAPIValidator implements ConstraintValidator<ScheduleAPIValidation, Integer> {

	ScheduleAPIValidation scheduleAPIValidation;

	@Autowired
	TestSetRepository testSetRepository;

	@Autowired
	ConfigLinesRepository configLinesRepository;

	@Autowired
	LookUpCodeRepository lookUpCodeRepository;
	
	@Autowired
	UserSchedulerJobRepository userSchedulerJobRepository;
	
	@Autowired
	SchedulerRepository schedulerRepository;
	
	@Autowired
	ProjectRepository projectRepository;

	@Override
	public void initialize(ScheduleAPIValidation constraintAnnotation) {
		this.scheduleAPIValidation = constraintAnnotation;
	}

	@Override
	public boolean isValid(Integer jobId, ConstraintValidatorContext context) {
		Scheduler scheduler=schedulerRepository.findByJobId(jobId);
		if(scheduler!=null) {
			Optional<List<UserSchedulerJob>> listOfTestRuns=userSchedulerJobRepository.findByJobId(scheduler.getJobId());
			if(listOfTestRuns.isPresent() && listOfTestRuns.get().size()>0) {
				return listOfTestRuns.get().parallelStream().filter(Objects::nonNull).allMatch((testRun)->{
					TestSet testSet = testSetRepository.findByTestRunName(testRun.getComments());
					if (testSet != null) {
						return StringUtils.oracleAPIAuthorization(context,testSet,configLinesRepository,lookUpCodeRepository,projectRepository);						
					}
					context.disableDefaultConstraintViolation();
					context.buildConstraintViolationWithTemplate(Constants.SCHEDULE_TEST_RUN_NAME_RESPONSE_STRING+testRun.getComments()
					+Constants.SCHEDULE_TEST_RUN_ERROR_RESPONSE_STRING+Constants.INVALID_TEST_SET_LINE_ID+Constants.SINGLE_QUOTE+Constants.CLOSE_CURLY_BRACES)
					.addConstraintViolation();
					return false;
				});
			}
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(Constants.NO_TEST_RUN_IN_SCHEDULE)
			.addConstraintViolation();
			return false;	
		}
		return true;
	}
}
