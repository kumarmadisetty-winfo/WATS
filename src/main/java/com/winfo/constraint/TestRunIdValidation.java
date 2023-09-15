package com.winfo.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.winfo.constraintImpl.TestRunIdValidator;

import java.lang.annotation.*;

@Constraint(validatedBy = {TestRunIdValidator.class})
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TestRunIdValidation {
	public abstract String message() default "Invalid Test Set Id";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}