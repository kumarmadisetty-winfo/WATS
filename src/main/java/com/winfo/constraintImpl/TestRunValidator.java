package com.winfo.constraintImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.winfo.constraint.TestRunValidation;
import com.winfo.repository.TestSetRepository;
import com.winfo.utils.Constants;

@Component
public class TestRunValidator implements ConstraintValidator<TestRunValidation, Integer> {

	@Autowired
	TestSetRepository testSetRepository;
	
	TestRunValidation testRunIdValidation;

	@Override
	public void initialize(TestRunValidation constraintAnnotation) {
	    this.testRunIdValidation =constraintAnnotation;
	}

    @Override
    public boolean isValid(Integer testSetId, ConstraintValidatorContext context) {
        if (testSetId == null) {
        	context.disableDefaultConstraintViolation();
        	context.buildConstraintViolationWithTemplate( Constants.INVALID_TEST_SET_ID).addConstraintViolation();
            return false;
        }
        else {
        	long count=testSetRepository.countByTestRunId(testSetId);
        	if(count==1) {
        		return true;
        	}else {
        		context.disableDefaultConstraintViolation();
            	context.buildConstraintViolationWithTemplate( Constants.INVALID_TEST_SET_ID).addConstraintViolation();
                return false;
        	}
        }        
    }
}
