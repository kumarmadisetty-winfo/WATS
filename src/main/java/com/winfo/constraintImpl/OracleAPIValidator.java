package com.winfo.constraintImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.winfo.constraint.OracleAPIValidation;
import com.winfo.model.TestSet;
import com.winfo.repository.ConfigLinesRepository;
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
	
    @Override
    public void initialize(OracleAPIValidation constraintAnnotation) {
    }

    @Override
    public boolean isValid(Integer testSetId, ConstraintValidatorContext context) {
        if (testSetId == null) {
        	context.disableDefaultConstraintViolation();
        	context.buildConstraintViolationWithTemplate( "Invalid Test Run").addConstraintViolation();
            return false;
        }
        else {
        	TestSet testSet=testSetRepository.findByTestRunId(testSetId);
        	if(testSet!=null) {
        		String basePath = configLinesRepository.getValueFromKeyNameAndConfigurationId(Constants.API_BASE_URL,
    					testSet.getConfigurationId());
    			String username = configLinesRepository.getValueFromKeyNameAndConfigurationId(Constants.API_USERNAME,
    					testSet.getConfigurationId());
    			String password = configLinesRepository.getValueFromKeyNameAndConfigurationId(Constants.API_PASSWORD,
    					testSet.getConfigurationId());
    			WebClient webClient = WebClient.builder().baseUrl(basePath)
						.defaultHeader("Authorization", basicAuthHeader(username, password)).build();
				webClient.get()
						.uri("/hcmRestApi/resources/11.13.18.05/userAccounts?q=Username=").retrieve()
						.onStatus(httpStatus -> httpStatus.is4xxClientError() || httpStatus.is5xxServerError(),clientResponse -> {
							if(clientResponse.statusCode().value()==401) {
								logger.error("Unauthoriazed : " + clientResponse.statusCode());
								context.disableDefaultConstraintViolation();
				            	context.buildConstraintViolationWithTemplate("Not able to Validate. Please check the credentials in the configuration").addConstraintViolation();
							}
							else if(clientResponse.statusCode().value()==503) {
								logger.error("Unauthoriazed : " + clientResponse.statusCode());
								context.disableDefaultConstraintViolation();
								context.buildConstraintViolationWithTemplate("Not able to Validate. Oracle service is unavailable at this moment").addConstraintViolation();
							}
							return Mono.empty();
						})
						.bodyToMono(String.class).block();
        		return false;
        	}else {
        		context.disableDefaultConstraintViolation();
            	context.buildConstraintViolationWithTemplate( "Invalid Test Run").addConstraintViolation();
                return false;
        	}
        }        
    }
    
    private String basicAuthHeader(String username, String password) {
		String credentials = username + ":" + password;
		byte[] credentialsBytes = credentials.getBytes();
		String base64Credentials = java.util.Base64.getEncoder().encodeToString(credentialsBytes);
		return "Basic " + base64Credentials;
	}
}
