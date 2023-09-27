package com.winfo.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.winfo.constraintImpl.TestRunScriptValidator;
import com.winfo.utils.Constants;

import java.lang.annotation.*;

@Constraint(validatedBy = TestRunScriptValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE,ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TestRunScriptValidation {
	public abstract String message() default Constants.INVALID_TEST_SET_LINE_ID;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}