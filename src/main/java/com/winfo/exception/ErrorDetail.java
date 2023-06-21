package com.winfo.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorDetail {
	private int code;
	private String errorDetails;
}