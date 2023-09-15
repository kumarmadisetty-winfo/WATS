package com.winfo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(WatsEBSException.class)
	public ResponseEntity<Object> handleDefaultException(WatsEBSException exception) {
		ErrorDetail errorDetails = new ErrorDetail(exception.getErrorCode(), exception.getErrorMessage());
		exception.printStackTrace();
		return new ResponseEntity<>(errorDetails, HttpStatus.OK);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> exceptionHandler(Exception ex, WebRequest request) {
		List<String> errors = new ArrayList<>();
		if (ex instanceof ConstraintViolationException) {
			ConstraintViolationException cve = (ConstraintViolationException) ex;
			Set<ConstraintViolation<?>> constraintViolations = cve.getConstraintViolations();
			errors = new ArrayList<>(constraintViolations.size());
			errors = constraintViolations.stream().map(constraintViolation -> constraintViolation.getMessage())
					.collect(Collectors.toList());

		} else {
			errors.add(ex.getLocalizedMessage());
		}
		ErrorResponse responseErrors = new ErrorResponse(HttpStatus.PRECONDITION_FAILED.value(), errors);
		return new ResponseEntity<Object>(responseErrors, HttpStatus.PRECONDITION_FAILED);
	}

}