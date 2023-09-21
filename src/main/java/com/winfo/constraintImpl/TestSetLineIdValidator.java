package com.winfo.constraintImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.winfo.constraint.TestSetLineIdValidation;
import com.winfo.repository.TestSetLinesRepository;
import com.winfo.utils.Constants;

@Component
public class TestSetLineIdValidator implements ConstraintValidator<TestSetLineIdValidation, Integer> {

	@Autowired
	TestSetLinesRepository testSetLinesRepository;
	
	TestSetLineIdValidation testSetLineIdValidation;

	@Override
	public void initialize(TestSetLineIdValidation constraintAnnotation) {
	    this.testSetLineIdValidation =constraintAnnotation;
	}

    @Override
    public boolean isValid(Integer testSetLineId, ConstraintValidatorContext context) {
        if (testSetLineId == null) {
        	context.disableDefaultConstraintViolation();
        	context.buildConstraintViolationWithTemplate( Constants.INVALID_TEST_SET_LINE_ID).addConstraintViolation();
            return false;
        }
        else {
        	long count=testSetLinesRepository.countByTestRunScriptId(testSetLineId);
        	if(count==1) {
        		return true;
        	}else {
        		context.disableDefaultConstraintViolation();
            	context.buildConstraintViolationWithTemplate( Constants.INVALID_TEST_SET_LINE_ID).addConstraintViolation();
                return false;
        	}
        }        
    }
}
