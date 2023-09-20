package com.winfo.serviceImpl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.winfo.repository.ConfigLinesRepository;
import com.winfo.repository.LookUpCodeRepository;
import com.winfo.repository.TestSetLinesRepository;
import com.winfo.repository.TestSetRepository;
import com.winfo.repository.TestSetScriptParamRepository;
import com.winfo.repository.ConfigurationUsersRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winfo.model.LookUpCode;
import com.winfo.model.TestSet;
import com.winfo.model.TestSetLine;
import com.winfo.model.TestSetScriptParam;
import com.winfo.service.TestRunValidationService;
import com.winfo.utils.Constants;
import com.winfo.vo.ResponseDto;

import reactor.core.publisher.Mono;

@Service
public class TestRunValdiationServiceImpl implements TestRunValidationService {

	public static final Logger logger = Logger.getLogger(TestRunValdiationServiceImpl.class);

	@Autowired
	TestSetRepository testSetRepository;

	@Autowired
	TestSetLinesRepository testSetLinesRepository;

	@Autowired
	TestSetScriptParamRepository testSetScriptParamRepository;

	@Autowired
	ConfigLinesRepository configLinesRepository;

	@Autowired
	LookUpCodeRepository lookUpCodeRepository;

	@Autowired
	ConfigurationUsersRepository configurationUsersRepository;

	@Override
	public ResponseDto validateTestRun(Integer testSetId) throws Exception {
		TestSet testSet = testSetRepository.findByTestRunId(testSetId);
		try {
			String basePath = configLinesRepository.getValueFromKeyNameAndConfigurationId(Constants.API_BASE_URL,
					testSet.getConfigurationId());
			String username = configLinesRepository.getValueFromKeyNameAndConfigurationId(Constants.API_USERNAME,
					testSet.getConfigurationId());
			String password = configLinesRepository.getValueFromKeyNameAndConfigurationId(Constants.API_PASSWORD,
					testSet.getConfigurationId());
			testSet.getTestRunScriptDatalist().parallelStream().forEach(testSetLine -> {
				testSetLine.setValidationStatus(Constants.VALIDATION_SUCCESS);
				List<TestSetScriptParam> validationAddedScriptSteps = testSetLine.getTestRunScriptParam()
						.parallelStream()
						.filter(testSetScriptParam -> (!Constants.NA
								.equalsIgnoreCase(testSetScriptParam.getValidationType())
								&& !"".equalsIgnoreCase(testSetScriptParam.getValidationType()))
								|| Constants.MANDETORY.equalsIgnoreCase(testSetScriptParam.getUniqueMandatory()))
						.collect(Collectors.toList());
				if (validationAddedScriptSteps.size() > 0) {
					validateScript(testSet, testSetLine, validationAddedScriptSteps, basePath, username, password);
				} else {
					logger.info("No Validation added to the script");
					testSetLine.setValidationStatus(Constants.No_VALIDATION);
				}
				testSetLinesRepository.updateValidationStatus(testSetLine.getTestRunScriptId(),testSetLine.getValidationStatus());
			});
			return new ResponseDto(HttpStatus.OK.value(), Constants.SUCCESS,
					testSet.getTestRunName() + " is validated successfully");

		} catch (Exception e) {
			logger.error("Internal server error. Please contact to the administrator: "+e.getMessage());
			return new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.ERROR,
					testSet.getTestRunName() + "is not validated successfully");
		}
	}

	private String basicAuthHeader(String username, String password) {
		String credentials = username + ":" + password;
		byte[] credentialsBytes = credentials.getBytes();
		String base64Credentials = java.util.Base64.getEncoder().encodeToString(credentialsBytes);
		return "Basic " + base64Credentials;
	}

	private boolean isValidDate(String date, String dateFormat) {
		DateFormat dateFormatChecker = new SimpleDateFormat(dateFormat);
		dateFormatChecker.setLenient(false);
		try {
			dateFormatChecker.parse(date);
			return true;
		} catch (ParseException e) {
			logger.error("Given date is not valid for expected format: "+e.getMessage());
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public void validateScript(TestSet testSet, TestSetLine testSetLine,
			List<TestSetScriptParam> validationAddedScriptSteps, String basePath, String username, String password) {
		validationAddedScriptSteps.parallelStream().forEach(testSetScriptParam -> {
			testSetScriptParam.setValidationStatus(Constants.VALIDATION_SUCCESS);
			testSetScriptParam.setValidationErrorMessage(null);
			if (Constants.MANDETORY.equalsIgnoreCase(testSetScriptParam.getUniqueMandatory())
					&& "".equals(testSetScriptParam.getInputValue())) {
				testSetScriptParam.setValidationErrorMessage("Input Value is mandetory for this step");
				testSetScriptParam.setValidationStatus(Constants.VALIDATION_FAIL);
				testSetLine.setValidationStatus(Constants.VALIDATION_FAIL);
			} else if (Constants.API_VALIDATION.equalsIgnoreCase(testSetScriptParam.getValidationType())
					&& !Constants.NA.equalsIgnoreCase(testSetScriptParam.getValidationName())) {
				try {
					LookUpCode lookUpCode = lookUpCodeRepository.findByLookUpNameAndLookUpCode(
							testSetScriptParam.getValidationType(), testSetScriptParam.getValidationName());
					if ("Get UserId".equalsIgnoreCase(testSetScriptParam.getValidationName())) {
						long userCount = configurationUsersRepository
								.countByUserName(testSetScriptParam.getInputValue());
						if (userCount == 0) {
							testSetScriptParam.setValidationStatus(Constants.VALIDATION_FAIL);
							testSetScriptParam.setValidationErrorMessage(
									testSetScriptParam.getInputParameter() + " is not added in the configuration");
							testSetLine.setValidationStatus(Constants.VALIDATION_FAIL);
							return;
						}
					}
					WebClient webClient = WebClient.builder().baseUrl(basePath)
							.defaultHeader("Authorization", basicAuthHeader(username, password)).build();
					String result = webClient.get().uri(lookUpCode.getTargetCode() + testSetScriptParam.getInputValue())
							.retrieve()
							.onStatus(httpStatus -> httpStatus.is4xxClientError() || httpStatus.is5xxServerError(),
									clientResponse -> {
										if (clientResponse.statusCode().value() == 503) {
											testSetScriptParam.setValidationErrorMessage(
													"Not able to Validate. Oracle service is unavailable at this moment");
											logger.warn(
													"Not able to Validate. Oracle service is unavailable at this moment");
										} else if (clientResponse.statusCode().is4xxClientError()) {
											testSetScriptParam.setValidationErrorMessage(
													"Server error: " + clientResponse.statusCode());
											logger.warn("Oracle server error: " + clientResponse.statusCode());
										} else {
											testSetScriptParam.setValidationErrorMessage(
													"Oracle server error: " + clientResponse.statusCode());
											logger.warn("Server side error: " + clientResponse.statusCode());
										}
										return Mono.empty();
									})
							.bodyToMono(String.class).block();
					if (result != null) {
						ObjectMapper objectMapper = new ObjectMapper();
						Map<String, Object> jsonMap = null;
						try {
							jsonMap = objectMapper.readValue(result, Map.class);
						} catch (JsonProcessingException e) {
							logger.error("Error occured while parsing external JSON: " + e.getMessage());
						}
						Object itemsObject = jsonMap.get("items");
						if (itemsObject instanceof List) {
							List<Map<String, Object>> itemsList = (List<Map<String, Object>>) itemsObject;
							if (itemsList.isEmpty() || !itemsList.get(0)
									.containsValue(testSetScriptParam.getInputValue().toUpperCase())) {
								logger.warn("Given Input Data is not valid");
								testSetScriptParam.setValidationErrorMessage("Given Input Data is not valid");
								testSetScriptParam.setValidationStatus(Constants.VALIDATION_FAIL);
								testSetLine.setValidationStatus(Constants.VALIDATION_FAIL);
							}
						}
					} else {
						logger.warn("Error occurred or no response received from the external API");
						testSetScriptParam.setValidationErrorMessage("Error occurred or no response received from the external API");
						testSetScriptParam.setValidationStatus(Constants.VALIDATION_FAIL);
						testSetLine.setValidationStatus(Constants.VALIDATION_FAIL);
					}
				} catch (Exception e) {
					logger.error("Internal server error. Please contact to the administrator: "+e.getMessage());
					testSetScriptParam.setValidationErrorMessage("Internal server error. Please contact to the administrator");
					testSetScriptParam.setValidationStatus(Constants.VALIDATION_FAIL);
					testSetLine.setValidationStatus(Constants.VALIDATION_FAIL);
				}
			} else if (Constants.REGULAR_EXPRESSION.equalsIgnoreCase(testSetScriptParam.getValidationType())
					&& !Constants.NA.equalsIgnoreCase(testSetScriptParam.getValidationName())) {
				LookUpCode lookUpCode = lookUpCodeRepository.findByMeaningAndLookUpName(
						testSetScriptParam.getValidationName(), testSetScriptParam.getValidationType());
				if (!isValidDate(testSetScriptParam.getInputValue(), lookUpCode.getMeaning())) {
					logger.warn("Given Input Data is not valid");
					testSetScriptParam.setValidationErrorMessage("Given Input Data is not valid");
					testSetScriptParam.setValidationStatus(Constants.VALIDATION_FAIL);
					testSetLine.setValidationStatus(Constants.VALIDATION_FAIL);
				}
			}
			testSetScriptParamRepository.updateValidationStatusAndValidationErrorMessage(testSetScriptParam.getTestRunScriptParamId(),testSetScriptParam.getValidationStatus(),testSetScriptParam.getValidationErrorMessage());
		});
	}
}
