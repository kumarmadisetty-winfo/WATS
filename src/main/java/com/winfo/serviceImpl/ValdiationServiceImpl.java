package com.winfo.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.winfo.repository.ConfigLinesRepository;
import com.winfo.repository.LookUpCodeRepository;
import com.winfo.repository.SchedulerRepository;
import com.winfo.repository.TestSetLinesRepository;
import com.winfo.repository.TestSetRepository;
import com.winfo.repository.TestSetScriptParamRepository;
import com.winfo.repository.UserSchedulerJobRepository;
import com.winfo.repository.ConfigurationUsersRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winfo.exception.WatsEBSException;
import com.winfo.model.LookUpCode;
import com.winfo.model.Scheduler;
import com.winfo.model.TestSet;
import com.winfo.model.TestSetLine;
import com.winfo.model.TestSetScriptParam;
import com.winfo.model.UserSchedulerJob;
import com.winfo.service.ValidationService;
import com.winfo.utils.Constants;
import com.winfo.utils.StringUtils;
import com.winfo.vo.ResponseDto;

import reactor.core.publisher.Mono;

@Service
public class ValdiationServiceImpl implements ValidationService {

	public static final Logger logger = Logger.getLogger(ValdiationServiceImpl.class);

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

	@Autowired
	SchedulerRepository schedulerRepository;

	@Autowired
	UserSchedulerJobRepository userSchedulerJobRepository;

	@Override
	@Transactional
	public ResponseDto validateSchedule(Integer jobId) throws Exception {
		Scheduler scheduler = schedulerRepository.findByJobId(jobId);
		try {
			Optional<List<UserSchedulerJob>> listOfTestRuns = userSchedulerJobRepository.findByJobId(jobId);
			if (listOfTestRuns.isPresent()) {
				listOfTestRuns.get().stream().filter(Objects::nonNull).forEach((testRun) -> {
					TestSet testSet = testSetRepository.findByTestRunName(testRun.getComments());
					try {
						validateTestRun(testSet.getTestRunId(), true);
					} catch (Exception e) {
						logger.error(Constants.INTERNAL_SERVER_ERROR + ": " + e.getMessage());
						throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
								Constants.INTERNAL_SERVER_ERROR + ": " + e.getMessage());
					}
				});
			}
			return new ResponseDto(HttpStatus.OK.value(), Constants.SUCCESS,
					scheduler.getJobName() + " is " + Constants.VALIDATED_SUCCESSFULLY);

		} catch (Exception e) {
			logger.error(Constants.INTERNAL_SERVER_ERROR + ": " + e.getMessage());
			return new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.ERROR,
					scheduler.getJobName() + " is not " + Constants.VALIDATED_SUCCESSFULLY);
		}
	}

	@Override
	@Transactional
	public ResponseDto validateTestRun(Integer testSetId, boolean validateAll) throws Exception {
		TestSet testSet = testSetRepository.findByTestRunId(testSetId);
		try {
			List<String> apiDetails = StringUtils.getAPIValidationCredentials(configLinesRepository,testSet.getConfigurationId());
			testSet.getTestRunScriptDatalist().stream().filter(Objects::nonNull).filter(testSetLine -> {
				if (validateAll)
					return true;
				else
					return "Y".equalsIgnoreCase(testSetLine.getEnabled());
			}).forEach(testSetLine -> {
				testSetLine.setValidationStatus(Constants.VALIDATION_SUCCESS);
				List<TestSetScriptParam> validationAddedScriptSteps = testSetLine.getTestRunScriptParam()
						.stream().filter(Objects::nonNull).filter(testSetScriptParam -> {
							return (!Constants.NA.equalsIgnoreCase(testSetScriptParam.getValidationType())
									&& !"".equalsIgnoreCase(testSetScriptParam.getValidationType()))
									|| Constants.MANDETORY.equalsIgnoreCase(testSetScriptParam.getUniqueMandatory());
						}).collect(Collectors.toList());
				if (validationAddedScriptSteps.size() > 0) {
					validateScript(testSetLine, validationAddedScriptSteps, apiDetails.get(0), apiDetails.get(1),
							apiDetails.get(2));
				} else {
					logger.info(Constants.NO_VALIDATION_MESSAGE);
					testSetLine.setValidationStatus(Constants.No_VALIDATION);
				}
				testSetLinesRepository.updateValidationStatus(testSetLine.getTestRunScriptId(),
						testSetLine.getValidationStatus());
			});
			return new ResponseDto(HttpStatus.OK.value(), Constants.SUCCESS,
					testSet.getTestRunName() + " is " + Constants.VALIDATED_SUCCESSFULLY);

		} catch (Exception e) {
			logger.error("Internal server error. Please contact to the administrator: " + e.getMessage());
			return new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.ERROR,
					testSet.getTestRunName() + " is not " + Constants.VALIDATED_SUCCESSFULLY);
		}
	}

	@Override
	@Transactional
	public ResponseDto validateTestRunScript(Integer testSetId, Integer testSetLineId) throws Exception {
		try {
			TestSet testSet = testSetRepository.findByTestRunId(testSetId);
			List<TestSetLine> testSetLines = testSet.getTestRunScriptDatalist().parallelStream().filter(Objects::nonNull)
					.filter(testSetLine -> testSetLine.getTestRunScriptId().equals(testSetLineId))
					.collect(Collectors.toList());
			testSetLines.get(0).setValidationStatus(Constants.VALIDATION_SUCCESS);
			List<String> apiDetails = StringUtils.getAPIValidationCredentials(configLinesRepository,testSet.getConfigurationId());
			List<Integer> validationFailedScriptParam = new ArrayList<>();
			List<TestSetScriptParam> validationAddedScriptSteps = testSetLines.get(0).getTestRunScriptParam()
					.parallelStream().filter(Objects::nonNull).filter(testSetScriptParam -> {
						return ((!Constants.NA.equalsIgnoreCase(testSetScriptParam.getValidationType())
								&& !"".equalsIgnoreCase(testSetScriptParam.getValidationType()))
								|| Constants.MANDETORY.equalsIgnoreCase(testSetScriptParam.getUniqueMandatory()));
					}).collect(Collectors.toList());
			if (validationAddedScriptSteps.size() > 0) {
				validationFailedScriptParam = validateScript(testSetLines.get(0), validationAddedScriptSteps,
						apiDetails.get(0), apiDetails.get(1), apiDetails.get(2));
			} else {
				logger.info(Constants.NO_VALIDATION_MESSAGE);
				testSetLines.get(0).setValidationStatus(Constants.No_VALIDATION);
			}
			testSetLinesRepository.updateValidationStatus(testSetLines.get(0).getTestRunScriptId(),
					testSetLines.get(0).getValidationStatus());
			if (validationFailedScriptParam.size() > 0) {
				return new ResponseDto(HttpStatus.OK.value(), Constants.SUCCESS,
						Constants.VALIDATION_FAIL + " :" + validationFailedScriptParam.toString());
			}
			return new ResponseDto(HttpStatus.OK.value(), Constants.SUCCESS, Constants.VALIDATED_SUCCESSFULLY);

		} catch (Exception e) {
			logger.error(Constants.INTERNAL_SERVER_ERROR + ": " + e.getMessage());
			return new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.ERROR,
					Constants.INTERNAL_SERVER_ERROR + ": " + e.getMessage());
		}
	}

	private List<Integer> validateScript(TestSetLine testSetLine, List<TestSetScriptParam> validationAddedScriptSteps,
			String basePath, String username, String password) {
		return validationAddedScriptSteps.stream().filter(Objects::nonNull).filter(testSetScriptParam -> {
			testSetScriptParam.setValidationStatus(Constants.VALIDATION_SUCCESS);
			testSetScriptParam.setValidationErrorMessage(null);
			if (Constants.MANDETORY.equalsIgnoreCase(testSetScriptParam.getUniqueMandatory())
					&& "".equals(testSetScriptParam.getInputValue())) {
				updateLineAndParamValidationStatus(testSetLine, testSetScriptParam,
						"Input Value is mandetory for this step");
			} else if (Constants.API_VALIDATION.equalsIgnoreCase(testSetScriptParam.getValidationType())
					&& !Constants.NA.equalsIgnoreCase(testSetScriptParam.getValidationName())) {
				apiValidation(testSetLine, testSetScriptParam, basePath, username, password);
			} else if (Constants.REGULAR_EXPRESSION.equalsIgnoreCase(testSetScriptParam.getValidationType())
					&& !Constants.NA.equalsIgnoreCase(testSetScriptParam.getValidationName())) {
				regularExpressionValidation(testSetLine, testSetScriptParam);
			}
			testSetScriptParamRepository.updateValidationStatusAndValidationErrorMessage(
					testSetScriptParam.getTestRunScriptParamId(), testSetScriptParam.getValidationStatus(),
					testSetScriptParam.getValidationErrorMessage());
			return Constants.VALIDATION_FAIL.equalsIgnoreCase(testSetScriptParam.getValidationStatus());
		}).map(TestSetScriptParam::getTestRunScriptParamId).collect(Collectors.toList());
	}

	private void updateLineAndParamValidationStatus(TestSetLine testSetLine, TestSetScriptParam testSetScriptParam,String errorMessage) {
		testSetScriptParam.setValidationErrorMessage(errorMessage);
		testSetScriptParam.setValidationStatus(Constants.VALIDATION_FAIL);
		if(Constants.VALIDATION_SUCCESS.equalsIgnoreCase(testSetLine.getValidationStatus()))testSetLine.setValidationStatus(Constants.VALIDATION_FAIL);
	}

	@SuppressWarnings("unchecked")
	private void apiValidation(TestSetLine testSetLine, TestSetScriptParam testSetScriptParam, String basePath,
			String username, String password) {
		try {
			LookUpCode lookUpCode = lookUpCodeRepository.findByLookUpNameAndLookUpCode(
					testSetScriptParam.getValidationType(), testSetScriptParam.getValidationName());
			if ("Get UserId".equalsIgnoreCase(testSetScriptParam.getValidationName())) {
				long userCount = configurationUsersRepository.countByUserName(testSetScriptParam.getInputValue());
				if (userCount == 0) {
					updateLineAndParamValidationStatus(testSetLine, testSetScriptParam,
							testSetScriptParam.getInputParameter() + " is not added in the configuration");
					return;
				}
			}
			WebClient webClient = WebClient.builder().baseUrl(basePath)
					.defaultHeader("Authorization", StringUtils.basicAuthHeader(username, password)).build();
			String result = webClient.get().uri(lookUpCode.getTargetCode() + testSetScriptParam.getInputValue())
					.retrieve().onStatus(httpStatus -> httpStatus.is4xxClientError() || httpStatus.is5xxServerError(),
							clientResponse -> {
								if (clientResponse.statusCode().value() == HttpStatus.SERVICE_UNAVAILABLE.value()) {
									testSetScriptParam.setValidationErrorMessage(Constants.ORACLE_SERVICE_UNAVAILABLE);
									logger.warn(Constants.ORACLE_SERVICE_UNAVAILABLE);
								} else if (clientResponse.statusCode().is4xxClientError()) {
									testSetScriptParam.setValidationErrorMessage(
											Constants.ORACLE_CLIENT_ERROR + clientResponse.statusCode());
									logger.warn(Constants.ORACLE_CLIENT_ERROR + clientResponse.statusCode());
								} else {
									testSetScriptParam.setValidationErrorMessage(
											Constants.ORACLE_SERVER_ERROR + clientResponse.statusCode());
									logger.warn(Constants.ORACLE_SERVER_ERROR + clientResponse.statusCode());
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
					if (itemsList.isEmpty()
							|| !itemsList.get(0).containsValue(testSetScriptParam.getInputValue().toUpperCase())) {
						logger.warn(Constants.INVALID_INPUT_DATA);
						updateLineAndParamValidationStatus(testSetLine, testSetScriptParam,
								Constants.INVALID_INPUT_DATA);
					}
				}
			} else {
				logger.warn(Constants.NO_RESPOSNE_EXTERNAL_API);
				updateLineAndParamValidationStatus(testSetLine, testSetScriptParam, Constants.NO_RESPOSNE_EXTERNAL_API);
			}
		} catch (Exception e) {
			logger.error(Constants.INTERNAL_SERVER_ERROR + ": " + e.getMessage());
			updateLineAndParamValidationStatus(testSetLine, testSetScriptParam, Constants.INTERNAL_SERVER_ERROR);
		}
	}

	private void regularExpressionValidation(TestSetLine testSetLine, TestSetScriptParam testSetScriptParam) {
		LookUpCode lookUpCode = lookUpCodeRepository.findByMeaningAndLookUpName(testSetScriptParam.getValidationName(),
				testSetScriptParam.getValidationType());
		if (!StringUtils.isValidDate(testSetScriptParam.getInputValue(), lookUpCode.getMeaning())) {
			logger.warn(Constants.INVALID_INPUT_DATA);
			updateLineAndParamValidationStatus(testSetLine, testSetScriptParam, Constants.INVALID_INPUT_DATA);
		}
	}
}
