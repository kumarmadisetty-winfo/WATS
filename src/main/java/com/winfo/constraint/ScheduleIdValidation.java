package com.winfo.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.winfo.constraintImpl.ScheduleIdValidator;
import com.winfo.utils.Constants;

import java.lang.annotation.*;

@Constraint(validatedBy = ScheduleIdValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE,ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ScheduleIdValidation {
	public abstract String message() default Constants.INVALID_JOB_ID;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}