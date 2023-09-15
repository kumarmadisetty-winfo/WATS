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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winfo.model.LookUpCode;
import com.winfo.model.TestSet;
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

	@SuppressWarnings("unchecked")
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
				List<TestSetScriptParam> validationAddedScriptSteps = testSetLine.getTestRunScriptParam().parallelStream()
						.filter(testSetScriptParam -> !"NA".equalsIgnoreCase(testSetScriptParam.getValidationType())
								&& !"".equalsIgnoreCase(testSetScriptParam.getValidationType())).collect(Collectors.toList());
				if (validationAddedScriptSteps.size() > 0) {
					validationAddedScriptSteps.parallelStream().forEach(testSetLineParam -> {
						if (Constants.API_VALIDATION.equalsIgnoreCase(testSetLineParam.getValidationType())
								&& !"NA".equalsIgnoreCase(testSetLineParam.getValidationName())) {
							try {
								LookUpCode lookUpCode = lookUpCodeRepository.findByLookUpNameAndLookUpCode(
										testSetLineParam.getValidationType(), testSetLineParam.getValidationName());
								WebClient webClient = WebClient.builder().baseUrl(basePath)
										.defaultHeader("Authorization", basicAuthHeader(username, password)).build();
								String result = webClient.get()
										.uri(lookUpCode.getTargetCode() + testSetLineParam.getInputValue()).retrieve()
										.onStatus(httpStatus -> httpStatus.is4xxClientError() || httpStatus.is5xxServerError(),clientResponse -> {
											if(clientResponse.statusCode().value()==401) {
												logger.error("Unauthoriazed : " + clientResponse.statusCode());
											}
											else if (clientResponse.statusCode().is4xxClientError()) {
												logger.error("Client side error: " + clientResponse.statusCode());
											} else {
												logger.error("Server side error: " + clientResponse.statusCode());
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
										logger.error("Error occured while parsing external JSON: "+ e.getMessage());
									}
									Object itemsObject = jsonMap.get("items");
									if (itemsObject instanceof List) {
										List<Map<String, Object>> itemsList = (List<Map<String, Object>>) itemsObject;
										if (!itemsList.isEmpty() && itemsList.get(0).containsValue(testSetLineParam.getInputValue().toUpperCase())) {
											logger.info("Given Input Data is valid");
											testSetLineParam.setValidationStatus(Constants.VALIDATION_SUCCESS);
										} else {
											logger.error("Given Input Data is not valid");
											testSetLineParam.setValidationStatus(Constants.VALIDATION_FAIL);
											testSetLine.setValidationStatus(Constants.VALIDATION_FAIL);
										}
									}
								} else {
									logger.error("Error occurred or no response received from the external API");
									testSetLineParam.setValidationStatus(Constants.VALIDATION_FAIL);
									testSetLine.setValidationStatus(Constants.VALIDATION_FAIL);
								}
							} catch (Exception e) {
								logger.error("Internal server error. Please contact to the administrator");
								testSetLineParam.setValidationStatus(Constants.VALIDATION_FAIL);
								testSetLine.setValidationStatus(Constants.VALIDATION_FAIL);
							}
						}
						else if(Constants.REGULAR_EXPRESSION.equalsIgnoreCase(testSetLineParam.getValidationType())
								&& !"NA".equalsIgnoreCase(testSetLineParam.getValidationName())) {
							LookUpCode lookUpCode = lookUpCodeRepository.findByMeaningAndLookUpName(
									testSetLineParam.getValidationName(),testSetLineParam.getValidationType());
							if(isValidDate(testSetLineParam.getInputValue(),lookUpCode.getMeaning())) {
								logger.info("Given Input Data is valid");
								testSetLineParam.setValidationStatus(Constants.VALIDATION_SUCCESS);
							}
							else {
								logger.error("Given Input Data is not valid");
								testSetLineParam.setValidationStatus(Constants.VALIDATION_FAIL);
								testSetLine.setValidationStatus(Constants.VALIDATION_FAIL);
							}
						}
					});
				} else {
					logger.info("No Validation added to the script");
					testSetLine.setValidationStatus(Constants.No_VALIDATION);
				}
				testSetLinesRepository.save(testSetLine);
			});
			return new ResponseDto(HttpStatus.OK.value(), Constants.SUCCESS, testSet.getTestRunName()+" is validated successfully");
			
		}catch (Exception e) {
			logger.error("Internal server error. Please contact to the administrator");
			return new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.ERROR, testSet.getTestRunName()+ "No changes found in ");
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
	        	logger.error("Given date is not valid for expected format");
	            return false;
	        }
	    }
}
