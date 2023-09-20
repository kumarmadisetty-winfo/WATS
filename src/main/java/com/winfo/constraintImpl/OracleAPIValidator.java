package com.winfo.constraintImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.winfo.constraint.OracleAPIValidation;
import com.winfo.model.LookUpCode;
import com.winfo.model.TestSet;
import com.winfo.repository.ConfigLinesRepository;
import com.winfo.repository.LookUpCodeRepository;
import com.winfo.repository.TestSetRepository;
import com.winfo.utils.Constants;

import reactor.core.publisher.Mono;

@Component
public class OracleAPIValidator implements ConstraintValidator<OracleAPIValidation, Integer> {

	public static final Logger logger = Logger.getLogger(OracleAPIValidator.class);

	@Autowired
	TestSetRepository testSetRepository;

	@Autowired
	ConfigLinesRepository configLinesRepository;

	@Autowired
	LookUpCodeRepository lookUpCodeRepository;

	@Override
	public void initialize(OracleAPIValidation constraintAnnotation) {
	}

	@Override
	public boolean isValid(Integer testSetId, ConstraintValidatorContext context) {
		TestSet testSet = testSetRepository.findByTestRunId(testSetId);
		if (testSet != null) {
			String basePath = configLinesRepository.getValueFromKeyNameAndConfigurationId(Constants.API_BASE_URL,
					testSet.getConfigurationId());
			String username = configLinesRepository.getValueFromKeyNameAndConfigurationId(Constants.API_USERNAME,
					testSet.getConfigurationId());
			String password = configLinesRepository.getValueFromKeyNameAndConfigurationId(Constants.API_PASSWORD,
					testSet.getConfigurationId());
			LookUpCode lookUpCode = lookUpCodeRepository.findByLookUpNameAndLookUpCode(Constants.API_VALIDATION,
					"Get UserId");
			try {
				WebClient webClient = WebClient.builder().baseUrl(basePath)
						.defaultHeader("Authorization", basicAuthHeader(username, password)).build();
				String result = webClient.get().uri(lookUpCode.getTargetCode()).retrieve()
						.onStatus(httpStatus -> httpStatus.is4xxClientError() || httpStatus.is5xxServerError(),
								clientResponse -> {
									if (clientResponse.statusCode().value() == 401) {
										logger.error("Unauthoriazed : " + clientResponse.statusCode());
										context.disableDefaultConstraintViolation();
										context.buildConstraintViolationWithTemplate(
												"Not able to Validate. Please check the credentials in the configuration")
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
						"Not able to Validate. Please check the API_BASE_URL in the configuration")
						.addConstraintViolation();
				return false;
			}
			return false;
		} else {
			return true;
		}
	}

	private String basicAuthHeader(String username, String password) {
		String credentials = username + ":" + password;
		byte[] credentialsBytes = credentials.getBytes();
		String base64Credentials = java.util.Base64.getEncoder().encodeToString(credentialsBytes);
		return "Basic " + base64Credentials;
	}
}
