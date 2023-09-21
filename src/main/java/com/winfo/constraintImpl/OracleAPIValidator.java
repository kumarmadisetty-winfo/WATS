package com.winfo.constraintImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.winfo.constraint.OracleAPIValidation;
import com.winfo.model.LookUpCode;
import com.winfo.model.TestSet;
import com.winfo.repository.ConfigLinesRepository;
import com.winfo.repository.LookUpCodeRepository;
import com.winfo.repository.TestSetRepository;
import com.winfo.utils.Constants;
import com.winfo.utils.StringUtils;

import reactor.core.publisher.Mono;

@Component
public class OracleAPIValidator implements ConstraintValidator<OracleAPIValidation, Integer> {

	public static final Logger logger = Logger.getLogger(OracleAPIValidator.class);

	OracleAPIValidation oracleAPIValidation;

	@Autowired
	TestSetRepository testSetRepository;

	@Autowired
	ConfigLinesRepository configLinesRepository;

	@Autowired
	LookUpCodeRepository lookUpCodeRepository;

	@Override
	public void initialize(OracleAPIValidation constraintAnnotation) {
		this.oracleAPIValidation = constraintAnnotation;
	}

	@Override
	public boolean isValid(Integer testSetId, ConstraintValidatorContext context) {
		TestSet testSet = testSetRepository.findByTestRunId(testSetId);
		if (testSet != null) {
			try {
				String basePath = configLinesRepository.getValueFromKeyNameAndConfigurationId(Constants.API_BASE_URL,
						testSet.getConfigurationId());
				String username = configLinesRepository.getValueFromKeyNameAndConfigurationId(Constants.API_USERNAME,
						testSet.getConfigurationId());
				String password = configLinesRepository.getValueFromKeyNameAndConfigurationId(Constants.API_PASSWORD,
						testSet.getConfigurationId());
				LookUpCode lookUpCode = lookUpCodeRepository.findByLookUpNameAndLookUpCode(Constants.API_VALIDATION,
						Constants.GET_USER_ID);
				try {
					WebClient webClient = WebClient.builder().baseUrl(basePath)
							.defaultHeader("Authorization", StringUtils.basicAuthHeader(username, password)).build();
					String result = webClient.get().uri(lookUpCode.getTargetCode()).retrieve()
							.onStatus(httpStatus -> httpStatus.is4xxClientError() || httpStatus.is5xxServerError(),
									clientResponse -> {
										if (clientResponse.statusCode().value() == HttpStatus.UNAUTHORIZED.value()) {
											logger.error("Unauthoriazed : " + clientResponse.statusCode());
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
		} else {
			return true;
		}
	}
}
