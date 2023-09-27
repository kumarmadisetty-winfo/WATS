package com.winfo.constraintImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.winfo.constraint.ScheduleValidation;
import com.winfo.repository.SchedulerRepository;
import com.winfo.utils.Constants;

@Component
public class ScheduleValidator implements ConstraintValidator<ScheduleValidation, Integer> {

	@Autowired
	SchedulerRepository schedulerRepository;
	
	ScheduleValidation scheduleIdValidation;

	@Override
	public void initialize(ScheduleValidation constraintAnnotation) {
	    this.scheduleIdValidation =constraintAnnotation;
	}

    @Override
	public boolean isValid(Integer jobId, ConstraintValidatorContext context) {
		if (jobId == null) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(Constants.INVALID_JOB_ID).addConstraintViolation();
			return false;
		}
		long count = schedulerRepository.countByJobId(jobId);
		if (count != 1) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(Constants.INVALID_JOB_ID).addConstraintViolation();
			return false;
		}
		return true;

	}
}
