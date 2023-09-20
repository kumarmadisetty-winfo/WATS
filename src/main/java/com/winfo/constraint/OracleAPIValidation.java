package com.winfo.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.winfo.constraintImpl.OracleAPIValidator;

import java.lang.annotation.*;

@Constraint(validatedBy = OracleAPIValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE,ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OracleAPIValidation {
	public abstract String message() default "Invalid credentails in the configuration";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}