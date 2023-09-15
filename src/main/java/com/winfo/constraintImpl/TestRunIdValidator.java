package com.winfo.constraintImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.winfo.constraint.TestRunIdValidation;
import com.winfo.repository.TestSetRepository;

@Component
public class TestRunIdValidator implements ConstraintValidator<TestRunIdValidation, Integer> {

	@Autowired
	TestSetRepository testSetRepository;
	
    @Override
    public void initialize(TestRunIdValidation constraintAnnotation) {
    }

    @Override
    public boolean isValid(Integer testSetId, ConstraintValidatorContext context) {
        if (testSetId == null) {
        	context.disableDefaultConstraintViolation();
        	context.buildConstraintViolationWithTemplate( "Invalid Test Run").addConstraintViolation();
            return false;
        }
        else {
        	long count=testSetRepository.countByTestRunId(testSetId);
        	if(count==1) {
        		return true;
        	}else {
        		context.disableDefaultConstraintViolation();
            	context.buildConstraintViolationWithTemplate( "Invalid Test Run").addConstraintViolation();
                return false;
        	}
        }        
    }
}
