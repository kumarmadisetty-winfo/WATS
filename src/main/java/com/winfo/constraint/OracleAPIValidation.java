package com.winfo.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.winfo.constraintImpl.OracleAPIValidator;
import com.winfo.utils.Constants;

import java.lang.annotation.*;

@Constraint(validatedBy = OracleAPIValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE,ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OracleAPIValidation {
	public abstract String message() default Constants.INVALID_CREDENTIALS_CONFIG;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}