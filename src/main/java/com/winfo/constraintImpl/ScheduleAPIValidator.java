package com.winfo.constraintImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.winfo.constraint.ScheduleAPIValidation;
import com.winfo.model.TestSet;
import com.winfo.repository.ConfigLinesRepository;
import com.winfo.repository.LookUpCodeRepository;
import com.winfo.repository.TestSetRepository;
import com.winfo.utils.StringUtils;

@Component
public class ScheduleAPIValidator implements ConstraintValidator<ScheduleAPIValidation, Integer> {

	public static final Logger logger = Logger.getLogger(TestRunAPIValidator.class);

	ScheduleAPIValidation scheduleAPIValidation;

	@Autowired
	TestSetRepository testSetRepository;

	@Autowired
	ConfigLinesRepository configLinesRepository;

	@Autowired
	LookUpCodeRepository lookUpCodeRepository;

	@Override
	public void initialize(ScheduleAPIValidation constraintAnnotation) {
		this.scheduleAPIValidation = constraintAnnotation;
	}

	@Override
	public boolean isValid(Integer jobId, ConstraintValidatorContext context) {
		//need to change below code
		TestSet testSet = testSetRepository.findByTestRunId(jobId);
		if (testSet != null) {
			return StringUtils.oracleAPIAuthorization(context,testSet,configLinesRepository,lookUpCodeRepository);
		} else {
			return true;
		}
	}
}
