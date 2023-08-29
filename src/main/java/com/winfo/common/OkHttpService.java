package com.winfo.common;

import java.io.File;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.winfo.exception.WatsEBSException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Component
public class OkHttpService {
	private final Logger logger = LogManager.getLogger(OkHttpService.class);
	
	@Autowired
	private OkHttpClient okHttpClient;
	
	private static final String SERVICE_NAME = "application";
	
	@CircuitBreaker(name = SERVICE_NAME, fallbackMethod = "getDefaultException")
	public void attachPdf(String url, String filePath, String authHeader) {
		logger.info("Received details as input in attachPdf method");
		try {
			File file = new File(filePath);
			RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
					.addFormDataPart(file.getName(), file.getName(),
							RequestBody.create(file, okhttp3.MediaType.parse("application/octet-stream")))
					.addFormDataPart("Title", file.getName()).build();
			Request request = new Request.Builder().url(url).method("POST", body).addHeader("Authorization", authHeader)
					.addHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"")
					.addHeader("X-Atlassian-Token", "no-check").build();
			try (Response response = okHttpClient.newCall(request).execute()) {
				if (response.isSuccessful()) {
					logger.info("Pdf attached succefully!");
				} else if (response.code() == 404) {
					throw new NotFoundException("Endpoint not found");
				} else if (response.code() == 401) {
					throw new WatsEBSException(401, "Authentication Error");
				} else if (response.code() == 500) {
					throw new InternalServerErrorException("Internal server error");
				} else if (response.code() == 400) {
					throw new WatsEBSException(400, "Bad Request");
				} else if (response.code() == 503) {
					throw new WatsEBSException(503, "Service Unavailable");
				}
			}

		} catch (NotFoundException ex) {
			logger.error("{}", ex.getMessage());
			throw new WatsEBSException(HttpStatus.NOT_FOUND.value(), ex.getMessage());
		} catch (WatsEBSException ex) {
			logger.error("{}", ex.getMessage());
			throw new WatsEBSException(ex.getErrorCode(), ex.getMessage());
		} catch (InternalServerErrorException ex) {
			logger.error("{}", ex.getMessage());
			throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
		} catch (Exception ex) {
			logger.error("Error while calling API: {}", ex.getMessage());
			throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
		}
	}
	
	public void getDefaultException(Exception ex) {
		if (ex instanceof NotFoundException) {
			logger.error("Endpoint not found");
		} else if (ex instanceof WatsEBSException) {
			logger.error("Authentication Error");
		} else if (ex instanceof InternalServerErrorException) {
			logger.error("Internal server error");
		} else {
			logger.error("Error while calling API: Internal server error");
		}
	}
}
