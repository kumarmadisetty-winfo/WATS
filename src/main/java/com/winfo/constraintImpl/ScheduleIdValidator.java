package com.winfo.constraintImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.winfo.constraint.ScheduleIdValidation;
import com.winfo.repository.SchedulerRepository;
import com.winfo.utils.Constants;

@Component
public class ScheduleIdValidator implements ConstraintValidator<ScheduleIdValidation, Integer> {

	@Autowired
	SchedulerRepository schedulerRepository;
	
	ScheduleIdValidation scheduleIdValidation;

	@Override
	public void initialize(ScheduleIdValidation constraintAnnotation) {
	    this.scheduleIdValidation =constraintAnnotation;
	}

    @Override
    public boolean isValid(Integer jobId, ConstraintValidatorContext context) {
        if (jobId == null) {
        	context.disableDefaultConstraintViolation();
        	context.buildConstraintViolationWithTemplate( Constants.INVALID_JOB_ID).addConstraintViolation();
            return false;
        }
        else {
        	long count=schedulerRepository.countByJobId(jobId);
        	if(count==1) {
        		return true;
        	}else {
        		context.disableDefaultConstraintViolation();
            	context.buildConstraintViolationWithTemplate( Constants.INVALID_JOB_ID).addConstraintViolation();
                return false;
        	}
        }        
    }
}
