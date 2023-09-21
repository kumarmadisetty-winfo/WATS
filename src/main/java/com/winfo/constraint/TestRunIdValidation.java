package com.winfo.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.winfo.constraintImpl.TestRunIdValidator;
import com.winfo.utils.Constants;

import java.lang.annotation.*;

@Constraint(validatedBy = {TestRunIdValidator.class})
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE,ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TestRunIdValidation {
	public abstract String message() default Constants.INVALID_TEST_SET_ID;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}