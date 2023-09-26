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
import com.winfo.model.TestSet;
import com.winfo.model.UserSchedulerJob;
import com.winfo.repository.ConfigLinesRepository;
import com.winfo.repository.LookUpCodeRepository;
import com.winfo.repository.TestSetRepository;
import com.winfo.repository.UserSchedulerJobRepository;
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

	@Override
	public void initialize(ScheduleAPIValidation constraintAnnotation) {
		this.scheduleAPIValidation = constraintAnnotation;
	}

	@Override
	public boolean isValid(Integer jobId, ConstraintValidatorContext context) {
			Optional<List<UserSchedulerJob>> listOfTestRuns=userSchedulerJobRepository.findByJobId(jobId);
			if(listOfTestRuns.isPresent()) {
				return listOfTestRuns.get().parallelStream().filter(Objects::nonNull).allMatch((testRun)->{
					TestSet testSet = testSetRepository.findByTestRunName(testRun.getComments());
					return StringUtils.oracleAPIAuthorization(context,testSet,configLinesRepository,lookUpCodeRepository);
				});
			}
			else return false;
	}
}
