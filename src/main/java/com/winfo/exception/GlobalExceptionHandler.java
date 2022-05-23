package com.winfo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(WatsEBSCustomException.class)
	public ResponseEntity<Object> handleDefaultException(WatsEBSCustomException exception) {
		ErrorDetail errorDetails = new ErrorDetail(exception.getErrorCode(), exception.getErrorMessage());
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}

}