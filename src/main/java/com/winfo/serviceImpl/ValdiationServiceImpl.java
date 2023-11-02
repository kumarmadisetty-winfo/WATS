package com.winfo.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.winfo.model.Scheduler;
import com.winfo.model.TestSet;
import com.winfo.model.TestSetLine;
import com.winfo.model.TestSetScriptParam;
import com.winfo.model.UserSchedulerJob;
import com.winfo.service.ValidationService;
import com.winfo.utils.Constants;
import com.winfo.utils.StringUtils;
import com.winfo.vo.ResponseDto;
import com.winfo.vo.ScriptValidationResponseVO;

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
	public ResponseEntity<ResponseDto> validateSchedule(Integer jobId) throws Exception {
		Scheduler scheduler = schedulerRepository.findByJobId(jobId);
		try {
			Optional<List<UserSchedulerJob>> listOfTestRuns = userSchedulerJobRepository.findByJobId(jobId);
			if (listOfTestRuns.isPresent()) {
				List<ResponseEntity<ResponseDto>> result=new ArrayList<>();
				AtomicReference<Exception> exceptionReference = new AtomicReference<>(null);
				listOfTestRuns.get().parallelStream().filter(Objects::nonNull).forEach((testRun) -> {
					if (exceptionReference.get() != null) {
				        return;
				    }
					TestSet testSet = testSetRepository.findByTestRunName(testRun.getComments());
					if(testSet!=null) {
						try {
							result.add(validateTestRun(testSet.getTestRunId(), true));
						} catch (Exception e) {
							logger.error(Constants.INTERNAL_SERVER_ERROR + " - " + e.getMessage()+" - "+testSet.getTestRunId()+" - "+testSet.getTestRunName());
							exceptionReference.set(new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
									Constants.INTERNAL_SERVER_ERROR + " - " + e.getMessage()));
						}
					}else {
						logger.error(Constants.INTERNAL_SERVER_ERROR + " - " + Constants.INVALID_TEST_SET_ID +" - "+testRun.getComments());
						exceptionReference.set(new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
								Constants.INTERNAL_SERVER_ERROR + " - " + Constants.INVALID_TEST_SET_ID+" - "+testRun.getComments()));
					}
				});
				Exception exception = exceptionReference.get();
				if (exception != null) {
				    throw exception;
				}
				Map<Boolean, List<ResponseEntity<ResponseDto>>>  partitionedMap = result.parallelStream()
				        .collect(Collectors.partitioningBy(responseEntity -> responseEntity.getStatusCode().value() == Constants.SUCCESS_STATUS));
				result.removeAll(partitionedMap.get(true));
				logger.info(result.toString());
				if (result.size()>0) {
					logger.error(Constants.INTERNAL_SERVER_ERROR +" - "+scheduler.getJobName()+" - "+scheduler.getJobId());
					return new ResponseEntity<ResponseDto>(new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.ERROR,
							scheduler.getJobName() + " is not " + Constants.VALIDATED_SUCCESSFULLY),HttpStatus.INTERNAL_SERVER_ERROR);
				}
				logger.info(scheduler.getJobName() + " is " + Constants.VALIDATED_SUCCESSFULLY);
				return new ResponseEntity<ResponseDto>(new ResponseDto(HttpStatus.OK.value(), Constants.SUCCESS,
						scheduler.getJobName() + " is " + Constants.VALIDATED_SUCCESSFULLY),HttpStatus.OK);
			}
			logger.error(Constants.INTERNAL_SERVER_ERROR + " - " + scheduler.getJobName()+" - "+scheduler.getJobId());
			return new ResponseEntity<ResponseDto>(new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.ERROR,
					scheduler.getJobName() + " is not " + Constants.VALIDATED_SUCCESSFULLY),HttpStatus.INTERNAL_SERVER_ERROR);

		} catch (WatsEBSException e) {
			logger.error(Constants.INTERNAL_SERVER_ERROR + " - " + e.getMessage()+" - "+Constants.TEST_RUN_FETCH_ERROR+" - " +scheduler.getJobName()+" - "+scheduler.getJobId());
			return new ResponseEntity<ResponseDto>(new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.ERROR,
					scheduler.getJobName() + " is not " + Constants.VALIDATED_SUCCESSFULLY +" - " +Constants.TEST_RUN_FETCH_ERROR),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch (Exception e) {
			logger.error(Constants.INTERNAL_SERVER_ERROR + " - " + e.getMessage()+" - "+scheduler.getJobName()+" - "+scheduler.getJobId());
			return new ResponseEntity<ResponseDto>(new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.ERROR,
					scheduler.getJobName() + " is not " + Constants.VALIDATED_SUCCESSFULLY),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<ResponseDto> validateTestRun(Integer testSetId, boolean validateAll) throws Exception {
		TestSet testSet = testSetRepository.findByTestRunId(testSetId);
		try {
			List<String> apiDetails = configLinesRepository.getListOfValueFromKeyNameAndConfigurationId(List.of(Constants.API_BASE_URL,Constants.API_USERNAME,Constants.API_PASSWORD),testSet.getConfigurationId());
			if(apiDetails!=null && apiDetails.size()==3) {
				List<ScriptValidationResponseVO> validationFailedScriptParam = new ArrayList<>();
				testSet.getTestRunScriptDatalist().parallelStream().filter(Objects::nonNull).filter(testSetLine -> {
					if (validateAll)
						return true;
					else
						return "Y".equalsIgnoreCase(testSetLine.getEnabled());
				}).filter(testSetLine->{
					return testSetLine.getTestRunScriptParam()!=null && !"".equals(testSetLine.getValidationStatus());
				}).forEach(testSetLine -> {
					testSetLine.setValidationStatus(Constants.VALIDATION_SUCCESS);
					List<TestSetScriptParam> validationAddedScriptSteps = testSetLine.getTestRunScriptParam()
							.parallelStream().filter(Objects::nonNull).filter(testSetScriptParam -> {
								return (!Constants.NA.equalsIgnoreCase(testSetScriptParam.getValidationType())
										&& !"".equalsIgnoreCase(testSetScriptParam.getValidationType()))
										|| Constants.MANDATORY.equalsIgnoreCase(testSetScriptParam.getUniqueMandatory()) 
										|| Constants.BOTH.equalsIgnoreCase(testSetScriptParam.getUniqueMandatory());
							}).collect(Collectors.toList());
					if (validationAddedScriptSteps.size() > 0) {
						validationFailedScriptParam.addAll(validateScript(testSetLine, validationAddedScriptSteps, apiDetails.get(0), apiDetails.get(2),
								apiDetails.get(1)));
					} else {
						logger.info(Constants.NO_VALIDATION_MESSAGE);
						testSetLine.setValidationStatus(Constants.No_VALIDATION);
					}
					testSetLinesRepository.updateValidationStatus(testSetLine.getTestRunScriptId(),
							testSetLine.getValidationStatus());
				});
				if (validationFailedScriptParam.size() > 0) {
					logger.error(Constants.INTERNAL_SERVER_ERROR+" - "+testSet.getTestRunId()+" - "+testSet.getTestRunName());
					return new ResponseEntity<ResponseDto>(new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.ERROR,
							Constants.VALIDATION_FAIL),HttpStatus.INTERNAL_SERVER_ERROR);
				}
				logger.info(testSet.getTestRunName() + " is " + Constants.VALIDATED_SUCCESSFULLY);
				return new ResponseEntity<ResponseDto>(new ResponseDto(HttpStatus.OK.value(), Constants.SUCCESS,
						testSet.getTestRunName() + " is " + Constants.VALIDATED_SUCCESSFULLY),HttpStatus.OK);	
			}else {
				logger.error(Constants.INTERNAL_SERVER_ERROR + " - " + Constants.COFIG_CREDENTIALS_FETCH_ERROR +" - Configuration Id - "+testSet.getConfigurationId()+" - Test Run - "+testSet.getTestRunName());
				throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
						Constants.INTERNAL_SERVER_ERROR + " - " + Constants.COFIG_CREDENTIALS_FETCH_ERROR+" - Test Run - "+testSet.getTestRunName());
			}

		}catch (WatsEBSException e) {
			logger.error(Constants.INTERNAL_SERVER_ERROR + " - " + e.getMessage()+" - "+Constants.COFIG_CREDENTIALS_FETCH_ERROR+" - Configuration Id - " +testSet.getConfigurationId()+" - Test Run - "+testSet.getTestRunName());
			return new ResponseEntity<ResponseDto>(new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.ERROR,
					testSet.getTestRunName() + " is not " + Constants.VALIDATED_SUCCESSFULLY +" - "+Constants.COFIG_CREDENTIALS_FETCH_ERROR+" - Configuration Id - " +testSet.getConfigurationId()+" - Test Run - "+testSet.getTestRunName()),HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			logger.error(Constants.INTERNAL_SERVER_ERROR+": " + e.getMessage()+" - "+testSet.getTestRunId()+" - "+testSet.getTestRunName());
			return new ResponseEntity<ResponseDto>(new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.ERROR,
					testSet.getTestRunName() + " is not " + Constants.VALIDATED_SUCCESSFULLY),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<ResponseDto> validateTestRunScript(Integer testSetId, Integer testSetLineId) throws Exception {
		TestSet testSet = testSetRepository.findByTestRunId(testSetId);
		try {
			Optional<TestSetLine> testSetLine = testSet.getTestRunScriptDatalist().parallelStream().filter(Objects::nonNull)
					.filter(testSetLineObject -> testSetLineObject.getTestRunScriptId().equals(testSetLineId))
					.findFirst();
			//check whether Test Set Line is exist of not
			if(testSetLine.isPresent()) {
				testSetLine.get().setValidationStatus(Constants.VALIDATION_SUCCESS);
				//get the API_BASE_PATH, API_USERNAME and API_PASSWORD from the test run configuration
				List<String> apiDetails = configLinesRepository.getListOfValueFromKeyNameAndConfigurationId(List.of(Constants.API_BASE_URL,Constants.API_USERNAME,Constants.API_PASSWORD),testSet.getConfigurationId());
				logger.info("API details of validation - API_BASE_PATH - "+apiDetails.get(0)+" - API_USERNAME"+apiDetails.get(2));
				List<ScriptValidationResponseVO> validationFailedScriptParam = new ArrayList<>();
				//Get all Validation added script steps
				List<TestSetScriptParam> validationAddedScriptSteps = testSetLine.get().getTestRunScriptParam()
						.parallelStream().filter(Objects::nonNull).filter(testSetScriptParam -> {
							return ((!Constants.NA.equalsIgnoreCase(testSetScriptParam.getValidationType())
									&& !"".equalsIgnoreCase(testSetScriptParam.getValidationType()))
									|| Constants.MANDATORY.equalsIgnoreCase(testSetScriptParam.getUniqueMandatory())
									|| Constants.BOTH.equalsIgnoreCase(testSetScriptParam.getUniqueMandatory()));
						}).collect(Collectors.toList());
				//If any one validation added to the script then do validation
				if (validationAddedScriptSteps.size() > 0) {
					validationFailedScriptParam = validateScript(testSetLine.get(), validationAddedScriptSteps,
							apiDetails.get(0), apiDetails.get(2), apiDetails.get(1));
				}
				//If no validation added then update the status
				else {
					logger.info(Constants.NO_VALIDATION_MESSAGE+" - "+testSetLine.get().getTestRunScriptId()+" - "+testSetLine.get().getScriptNumber());
					testSetLine.get().setValidationStatus(Constants.No_VALIDATION);
				}
				//update the script level validation status
				testSetLinesRepository.updateValidationStatus(testSetLine.get().getTestRunScriptId(),
						testSetLine.get().getValidationStatus());
				//If any validation got fail then return the particular script step and the error message
				if (validationFailedScriptParam.size() > 0) {
					logger.error(Constants.INTERNAL_SERVER_ERROR+" - "+testSet.getTestRunId()+" - "+testSet.getTestRunName());
					return new ResponseEntity<ResponseDto>(new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.ERROR,
							Constants.VALIDATION_FAIL,validationFailedScriptParam),HttpStatus.INTERNAL_SERVER_ERROR);
				}
				logger.info( Constants.VALIDATED_SUCCESSFULLY+" - "+testSetLine.get().getTestRunScriptId()+" - "+testSetLine.get().getScriptNumber());
				return new ResponseEntity<ResponseDto>(new ResponseDto(HttpStatus.OK.value(), Constants.SUCCESS, Constants.VALIDATED_SUCCESSFULLY),HttpStatus.OK);
			}else {
				return new ResponseEntity<ResponseDto>(new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.ERROR,
						Constants.VALIDATION_FAIL + " - invalid TestSetLineId - "+testSetLineId),HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (Exception e) {
			logger.error(Constants.INTERNAL_SERVER_ERROR + " - " + e.getMessage()+" - "+testSet.getTestRunId()+" - "+testSet.getTestRunName());
			return new ResponseEntity<ResponseDto>(new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.ERROR,
					Constants.INTERNAL_SERVER_ERROR + " - " + e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private List<ScriptValidationResponseVO> validateScript(TestSetLine testSetLine, List<TestSetScriptParam> validationAddedScriptSteps,
			String basePath, String username, String password) {
		return validationAddedScriptSteps.parallelStream().filter(Objects::nonNull).filter(testSetScriptParam -> {
			testSetScriptParam.setValidationStatus(Constants.VALIDATION_SUCCESS);
			testSetScriptParam.setValidationErrorMessage(null);
			//for checking if script step is mandatory or not
			if ((Constants.MANDATORY.equalsIgnoreCase(testSetScriptParam.getUniqueMandatory())
					|| Constants.BOTH.equalsIgnoreCase(testSetScriptParam.getUniqueMandatory()))
					&& "".equals(testSetScriptParam.getInputValue())) {
				updateLineAndParamValidationStatus(testSetLine, testSetScriptParam,
						Constants.INPUT_VALUE_MANDATORY);
			}
			//for Api validation and User Id validation
			else if (Constants.API_VALIDATION.equalsIgnoreCase(testSetScriptParam.getValidationType())
					&& !Constants.NA.equalsIgnoreCase(testSetScriptParam.getValidationName())) {
				apiValidation(testSetLine, testSetScriptParam, basePath, username, password);
			}
			//for Regular Expression validation
			else if (Constants.REGULAR_EXPRESSION.equalsIgnoreCase(testSetScriptParam.getValidationType())
					&& !Constants.NA.equalsIgnoreCase(testSetScriptParam.getValidationName())) {
				regularExpressionValidation(testSetLine, testSetScriptParam);
			}
			//update the script step level validation status
			testSetScriptParamRepository.updateValidationStatusAndValidationErrorMessage(
					testSetScriptParam.getTestRunScriptParamId(), testSetScriptParam.getValidationStatus(),
					testSetScriptParam.getValidationErrorMessage());
			return Constants.VALIDATION_FAIL.equalsIgnoreCase(testSetScriptParam.getValidationStatus());
		}).map(testSetScriptParam->new ScriptValidationResponseVO(testSetScriptParam.getTestRunScriptParamId(),testSetScriptParam.getValidationErrorMessage()))
				.collect(Collectors.toList());
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
			String tagretCode = lookUpCodeRepository.getTargetCodeFromLookUpNameAndLookUpCode(
					testSetScriptParam.getValidationType(), testSetScriptParam.getValidationName());
			if("".equals(tagretCode)) {
				logger.error(Constants.LOOKUPCODE_NOT_FOUND+" - "+testSetScriptParam.getValidationName()+" - " + testSetScriptParam.getTestRunScriptParamId()+" - "+testSetScriptParam.getInputParameter());
				updateLineAndParamValidationStatus(testSetLine, testSetScriptParam, Constants.LOOKUPCODE_NOT_FOUND+" - "+testSetScriptParam.getValidationName());
				return;
			}
			if (Constants.GET_USER_ID.equalsIgnoreCase(testSetScriptParam.getValidationName())) {
				long userCount = configurationUsersRepository.countByUserName(testSetScriptParam.getInputValue());
				if (userCount == 0) {
					logger.warn(testSetScriptParam.getInputValue() + " is not added in the configuration - "+ testSetScriptParam.getTestRunScriptParamId()+" - "+testSetScriptParam.getInputParameter());
					updateLineAndParamValidationStatus(testSetLine, testSetScriptParam,
							testSetScriptParam.getInputValue() + " is not added in the configuration");
					return;
				}
			}
			WebClient webClient = WebClient.builder().baseUrl(basePath)
					.defaultHeader("Authorization", StringUtils.basicAuthHeader(username, password)).build();
			String result = webClient.get().uri(tagretCode + testSetScriptParam.getInputValue())
					.retrieve().onStatus(httpStatus -> httpStatus.is4xxClientError() || httpStatus.is5xxServerError(),
							clientResponse -> {
								if (clientResponse.statusCode().value() == HttpStatus.SERVICE_UNAVAILABLE.value()) {
									testSetScriptParam.setValidationErrorMessage(Constants.ORACLE_SERVICE_UNAVAILABLE);
									logger.warn(Constants.ORACLE_SERVICE_UNAVAILABLE+" - "+ testSetScriptParam.getTestRunScriptParamId()+" - "+testSetScriptParam.getInputParameter());
								} else if (clientResponse.statusCode().is4xxClientError()) {
									testSetScriptParam.setValidationErrorMessage(
											Constants.ORACLE_CLIENT_ERROR + clientResponse.statusCode());
									logger.warn(Constants.ORACLE_CLIENT_ERROR + clientResponse.statusCode()+" - "+ testSetScriptParam.getTestRunScriptParamId()+" - "+testSetScriptParam.getInputParameter());
								} else {
									testSetScriptParam.setValidationErrorMessage(
											Constants.ORACLE_SERVER_ERROR + clientResponse.statusCode());
									logger.warn(Constants.ORACLE_SERVER_ERROR + clientResponse.statusCode()+" - "+ testSetScriptParam.getTestRunScriptParamId()+" - "+testSetScriptParam.getInputParameter());
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
					logger.error("Error occurred while parsing external JSON: " + e.getMessage()+" - "+ testSetScriptParam.getTestRunScriptParamId()+" - "+testSetScriptParam.getInputParameter());
				}
				Object itemsObject = jsonMap.get("items");
				if (itemsObject instanceof List) {
					List<Map<String, Object>> itemsList = (List<Map<String, Object>>) itemsObject;
					if (itemsList.isEmpty()
							|| !itemsList.get(0).containsValue(testSetScriptParam.getInputValue().toUpperCase())) {
						logger.warn(Constants.INVALID_INPUT_DATA+" - "+ testSetScriptParam.getTestRunScriptParamId()+" - "+testSetScriptParam.getInputParameter());
						updateLineAndParamValidationStatus(testSetLine, testSetScriptParam,
								Constants.INVALID_INPUT_DATA);
					}
				}else {
					logger.warn(Constants.INVALID_RESPOSNE_EXTERNAL_API+" - "+ testSetScriptParam.getTestRunScriptParamId()+" - "+testSetScriptParam.getInputParameter());
					updateLineAndParamValidationStatus(testSetLine, testSetScriptParam, Constants.INVALID_RESPOSNE_EXTERNAL_API);
				}
			} else {
				logger.warn(Constants.NO_RESPOSNE_EXTERNAL_API+" - "+ testSetScriptParam.getTestRunScriptParamId()+" - "+testSetScriptParam.getInputParameter());
				updateLineAndParamValidationStatus(testSetLine, testSetScriptParam, Constants.NO_RESPOSNE_EXTERNAL_API);
			}
		} catch (Exception e) {
			logger.error(Constants.INTERNAL_SERVER_ERROR + " - " + e.getMessage()+" - "+ testSetScriptParam.getTestRunScriptParamId()+" - "+testSetScriptParam.getInputParameter());
			updateLineAndParamValidationStatus(testSetLine, testSetScriptParam, Constants.INTERNAL_SERVER_ERROR);
		}
	}

	private void regularExpressionValidation(TestSetLine testSetLine, TestSetScriptParam testSetScriptParam) {
		String targetCode = lookUpCodeRepository.getTargetCodeFromLookUpNameAndLookUpCode(testSetScriptParam.getValidationName(),
				testSetScriptParam.getValidationType());
		if (!StringUtils.isValidDate(testSetScriptParam.getInputValue(), targetCode)) {
			logger.warn(Constants.INVALID_INPUT_DATA+" - "+ testSetScriptParam.getTestRunScriptParamId()+" - "+testSetScriptParam.getInputParameter());
			updateLineAndParamValidationStatus(testSetLine, testSetScriptParam, Constants.INVALID_INPUT_DATA);
		}
	}
}
