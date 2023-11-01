package com.winfo.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.winfo.model.Project;
import javax.validation.ConstraintValidatorContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.winfo.exception.WatsEBSException;
import com.winfo.model.LookUpCode;
import com.winfo.model.TestSet;
import com.winfo.repository.ConfigLinesRepository;
import com.winfo.repository.LookUpCodeRepository;
import com.winfo.repository.ProjectRepository;
import com.winfo.serviceImpl.ValdiationServiceImpl;

import reactor.core.publisher.Mono;

@Component
public class StringUtils {

	public static final Logger logger = Logger.getLogger(StringUtils.class);
	
	private static String projectPath = System.getProperty("user.dir");
	
	private static ProjectRepository projectRepository;
	
	@Autowired
	private StringUtils(ProjectRepository projectRepository) {
		StringUtils.projectRepository = projectRepository;
	}

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
			logger.error("Error occured while parsing "+dateFormat+ " into "+date+" - "+e.getMessage());
			return false;
		}
	}
	
	public static boolean oracleAPIAuthorization(ConstraintValidatorContext context,TestSet testSet,ConfigLinesRepository configLinesRepository,LookUpCodeRepository lookUpCodeRepository) {

		try {
			Project project=projectRepository.findByProjectId(testSet.getProjectId());
			if(Constants.ORACLE_FUSION.equalsIgnoreCase(project.getWatsPackage())) {
				boolean isApiValidationAddedInScripts=testSet.getTestRunScriptDatalist().size()>0 ? 
						testSet.getTestRunScriptDatalist().parallelStream().anyMatch(testSetLine->{
								if(testSetLine.getTestRunScriptParam().size()>0) {
									return testSetLine.getTestRunScriptParam().parallelStream().anyMatch(testSetScriptParam->{
										return Constants.API_VALIDATION.equalsIgnoreCase(testSetScriptParam.getValidationType());
									})?true:false;
								}
								else return false;
						}):false;
				if(isApiValidationAddedInScripts) {
					List<String> apiDetails = configLinesRepository.getListOfValueFromKeyNameAndConfigurationId(List.of(Constants.API_BASE_URL,Constants.API_USERNAME,Constants.API_PASSWORD),testSet.getConfigurationId());
					String targetCode= lookUpCodeRepository.getTargetCodeFromLookUpNameAndLookUpCode(Constants.API_VALIDATION,
							Constants.GET_USER_ID);
					WebClient webClient = WebClient.builder().baseUrl(apiDetails.get(0))
							.defaultHeader("Authorization", StringUtils.basicAuthHeader(apiDetails.get(2), apiDetails.get(1))).build();
					String result = webClient.get().uri(targetCode).retrieve()
							.onStatus(httpStatus -> httpStatus.is4xxClientError() || httpStatus.is5xxServerError(),
									clientResponse -> {
										if (clientResponse.statusCode().value() == HttpStatus.UNAUTHORIZED.value()) {
											throw new WatsEBSException(HttpStatus.NOT_FOUND.value(),Constants.SCHEDULE_TEST_RUN_NAME_RESPONSE_STRING+testSet.getTestRunName()
											+Constants.SCHEDULE_TEST_RUN_ERROR_RESPONSE_STRING+Constants.INVALID_CREDENTIALS_CONFIG_MESSAGE+Constants.SINGLE_QUOTE+Constants.CLOSE_CURLY_BRACES);
										}
										return Mono.empty();
									})
							.bodyToMono(String.class).block();
					if (result != null) {
						return true;
					} else {
						return false;
					}
				}
			}
			return true;
		} catch (WatsEBSException e) {
			logger.error(e.getMessage());
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(Constants.SCHEDULE_TEST_RUN_NAME_RESPONSE_STRING+testSet.getTestRunName()
			+Constants.SCHEDULE_TEST_RUN_ERROR_RESPONSE_STRING+Constants.INVALID_CREDENTIALS_CONFIG_MESSAGE+Constants.SINGLE_QUOTE+Constants.CLOSE_CURLY_BRACES)
					.addConstraintViolation();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(
					Constants.SCHEDULE_TEST_RUN_NAME_RESPONSE_STRING+testSet.getTestRunName()
					+Constants.SCHEDULE_TEST_RUN_ERROR_RESPONSE_STRING+Constants.INVALID_CREDENTIALS_AND_API_BASE_URL_CONFIG_MESSAGE+Constants.SINGLE_QUOTE+Constants.CLOSE_CURLY_BRACES)
					.addConstraintViolation();
			return false;
		}
	
	}
}
