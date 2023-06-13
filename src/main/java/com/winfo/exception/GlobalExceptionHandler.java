package com.winfo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(WatsEBSException.class)
	public ResponseEntity<Object> handleDefaultException(WatsEBSException exception) {
		ErrorDetail errorDetails = new ErrorDetail(exception.getErrorCode(), exception.getErrorMessage());
		exception.printStackTrace();
		return new ResponseEntity<>(errorDetails, HttpStatus.OK);
	}

}