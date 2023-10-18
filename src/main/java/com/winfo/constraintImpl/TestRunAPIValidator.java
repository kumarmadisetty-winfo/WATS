package com.winfo.constraintImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.winfo.constraint.TestRunAPIValidation;
import com.winfo.model.LookUpCode;
import com.winfo.model.TestSet;
import com.winfo.repository.ConfigLinesRepository;
import com.winfo.repository.LookUpCodeRepository;
import com.winfo.repository.TestSetRepository;
import com.winfo.utils.Constants;
import com.winfo.utils.StringUtils;

import reactor.core.publisher.Mono;

@Component
public class TestRunAPIValidator implements ConstraintValidator<TestRunAPIValidation, Integer> {

	TestRunAPIValidation testRunAPIValidation;

	@Autowired
	TestSetRepository testSetRepository;

	@Autowired
	ConfigLinesRepository configLinesRepository;

	@Autowired
	LookUpCodeRepository lookUpCodeRepository;
	
	
	@Override
	public void initialize(TestRunAPIValidation constraintAnnotation) {
		this.testRunAPIValidation = constraintAnnotation;
	}

	@Override
	public boolean isValid(Integer testSetId, ConstraintValidatorContext context) {
		TestSet testSet = testSetRepository.findByTestRunId(testSetId);
		if (testSet != null) {
			return StringUtils.oracleAPIAuthorization(context,testSet,configLinesRepository,lookUpCodeRepository);
		}
		return true;
	}
}
