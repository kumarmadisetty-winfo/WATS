package com.winfo.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;

import com.winfo.model.LookUpCode;
import com.winfo.model.TestSet;
import com.winfo.repository.ConfigLinesRepository;
import com.winfo.repository.LookUpCodeRepository;

import reactor.core.publisher.Mono;

public class StringUtils {

	private static String projectPath = System.getProperty("user.dir");

	public static String getFilePath(String fileName) {
		return projectPath + fileName;
	}

	public static final boolean isNullOrBlank(String input) {
		return input == null || "".equals(input.trim()) || "null".equals(input.trim());
	}

	public static String convertToString(Object input) {
		String outputString = null;
		if (input != null) {
			outputString = String.valueOf(input).trim();
		}
		return outputString;
	}


	public static final int convertStringToInteger(final String input, int defaultValue) {
		return StringUtils.isNullOrBlank(input) ? defaultValue : Integer.parseInt(input);
	}
	
	private StringUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static String basicAuthHeader(String username, String password) {
		String credentials = username + ":" + password;
		byte[] credentialsBytes = credentials.getBytes();
		String base64Credentials = java.util.Base64.getEncoder().encodeToString(credentialsBytes);
		return "Basic " + base64Credentials;
	}
	
	public static boolean isValidDate(String date, String dateFormat) {
		DateFormat dateFormatChecker = new SimpleDateFormat(dateFormat);
		dateFormatChecker.setLenient(false);
		try {
			dateFormatChecker.parse(date);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}
	
	public static boolean oracleAPIAuthorization(ConstraintValidatorContext context,TestSet testSet,ConfigLinesRepository configLinesRepository,LookUpCodeRepository lookUpCodeRepository) {

		try {
			List<String> apiDetails = StringUtils.getAPIValidationCredentials(configLinesRepository,testSet.getConfigurationId());
			LookUpCode lookUpCode = lookUpCodeRepository.findByLookUpNameAndLookUpCode(Constants.API_VALIDATION,
					Constants.GET_USER_ID);
			try {
				WebClient webClient = WebClient.builder().baseUrl(apiDetails.get(0))
						.defaultHeader("Authorization", StringUtils.basicAuthHeader(apiDetails.get(1), apiDetails.get(2))).build();
				String result = webClient.get().uri(lookUpCode.getTargetCode()).retrieve()
						.onStatus(httpStatus -> httpStatus.is4xxClientError() || httpStatus.is5xxServerError(),
								clientResponse -> {
									if (clientResponse.statusCode().value() == HttpStatus.UNAUTHORIZED.value()) {
										context.disableDefaultConstraintViolation();
										context.buildConstraintViolationWithTemplate(Constants.INVALID_CREDENTIALS_CONFIG_MESSAGE)
												.addConstraintViolation();
									}
									return Mono.empty();
								})
						.bodyToMono(String.class).block();
				if (result != null) {
					return true;
				}
			} catch (Exception e) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate(
						Constants.INVALID_API_BASE_URL_CONFIG_MESSAGE)
						.addConstraintViolation();
				return false;
			}
			return false;
		} catch (Exception e) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(
					Constants.INVALID_CREDENTIALS_AND_API_BASE_URL_CONFIG_MESSAGE)
					.addConstraintViolation();
			return false;
		}
	
	}
	
	public static List<String> getAPIValidationCredentials(ConfigLinesRepository configLinesRepository,Integer configId) {
		List<String> apiDetails = new ArrayList<>();
		apiDetails.add(configLinesRepository.getValueFromKeyNameAndConfigurationId(Constants.API_BASE_URL, configId));
		apiDetails.add(configLinesRepository.getValueFromKeyNameAndConfigurationId(Constants.API_USERNAME, configId));
		apiDetails.add(configLinesRepository.getValueFromKeyNameAndConfigurationId(Constants.API_PASSWORD, configId));
		return apiDetails;
	}
}
