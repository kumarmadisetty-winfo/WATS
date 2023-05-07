package com.winfo.services;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winfo.exception.WatsEBSCustomException;
import com.winfo.utils.HttpMethodUtils;
import com.winfo.vo.ApiValidationVO;
import com.winfo.vo.ScriptDetailsDto;

import okhttp3.Credentials;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import reactor.core.publisher.Mono;

@Service
public class SmartBearService {
	private final Logger logger = LogManager.getLogger(SmartBearService.class);
	private static final String URL_VERSION_1 = "/v1";
	private static final String URL_VERSION_2 = "/v2";
	private static final String RESULTS = "results";
	private static final String PROJECTS_PATH = "/projects/";
	private static final String TEST_RUNS_PATH = "/testruns/";
	@Value("${smartbear.username:#{null}}")
	private String userName;

	@Value("${smartbear.password:#{null}}")
	private String password;

	@Value("${smartbear.baseUrl:#{null}}")
	private String smartBearBaseUrl;

	@Autowired
	private DataBaseEntry databaseEntry;

	public void smartBearIntegrate(List<ScriptDetailsDto> fetchMetadataListVO, String status, String sourceFile,
			String projectName, String customColumnName) {
		try {
			/** Retrieve the project ID for the specified project name **/
			String projectId = getProjectId(projectName);

			/** Retrieve the script ID for the specified metadata and project ID **/
			String scriptId = getScriptId(fetchMetadataListVO, projectId, customColumnName);

			/**
			 * Execute a test run for the specified project and script, and retrieve the
			 * execution ID
			 **/
			String executionId = executeTestRun(projectId, scriptId);

			/** Retrieve the item ID for the specified project and execution **/
			String itemId = getItemId(projectId, executionId);

			/** Update the execution status for the specified project and execution **/
			updateExecutionStatus(projectId, executionId, status);

			/**
			 * Attach a PDF file at the script level for the specified project and execution
			 **/
			attachExecutionLevelPdf(projectId, executionId, itemId, sourceFile);

			/**
			 * Attach a PDF file at the test run level for the specified project and script
			 **/
			attachScriptLevelPdf(projectId, scriptId, sourceFile);

		} catch (Exception e) {
			logger.error("Error occurred while executing SmartBear API: %s", e.getMessage());
			throw new WatsEBSCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Not able to perform SmartBear Integration.");
		}
	}

	public void smartBearRegenerateAttachment(List<ScriptDetailsDto> fetchMetadataListVO, String sourceFile,
			String projectName, String customColumnName) {
		try {
			/** Retrieve the project ID for the specified project name **/
			String projectId = getProjectId(projectName);

			/** Retrieve the script ID for the specified metadata and project ID **/
			String scriptId = getScriptId(fetchMetadataListVO, projectId, customColumnName);

			/**
			 * Attach a PDF file at the test run level for the specified project and script
			 **/
			attachScriptLevelPdf(projectId, scriptId, sourceFile);

		} catch (Exception e) {
			logger.error("Error occurred while re-attaching the report in SmartBear: %s", e.getMessage());
			throw new WatsEBSCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Not able to perform SmartBear Re-Attachment.");
		}
	}

	@SuppressWarnings("unchecked")
	private String getProjectId(String projectName) throws IOException {
		logger.info("Getting project ID for project name: {}", projectName);
		Map<String, Object> projects = callApi(smartBearBaseUrl + URL_VERSION_1 + "/projects", HttpMethodUtils.GET,
				null);
		List<Map<String, Object>> listOfResults = (List<Map<String, Object>>) projects.get(RESULTS);
		Optional<Map<String, Object>> optionalProject = listOfResults.stream()
				.filter(map -> map.get("proj_name").toString().equalsIgnoreCase(projectName)).findFirst();
		if (optionalProject.isPresent()) {
			return optionalProject.get().get("id").toString();
		} else {
			throw new WatsEBSCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Project with name " + projectName + " not found in SmartBear.");
		}
	}

	@SuppressWarnings("unchecked")
	private String getScriptId(List<ScriptDetailsDto> fetchMetadataListVO, String projectId, String customColumnName)
			throws IOException {
		logger.info("Getting script ID for script number: {}", fetchMetadataListVO.get(0).getScriptNumber());
		String folderName = fetchMetadataListVO.get(0).getOracleReleaseYear() + "/"
				+ databaseEntry.getScriptDetailsByScriptId(Integer.parseInt(fetchMetadataListVO.get(0).getScriptId()))
						.getProductVersion().replace("R13", "").trim()
				+ "/"
				+ databaseEntry.getScriptDetailsByScriptId(Integer.parseInt(fetchMetadataListVO.get(0).getScriptId()))
						.getProcessArea();
		Map<String, Object> scripts = callApi(
				smartBearBaseUrl + URL_VERSION_1 + PROJECTS_PATH + projectId
						+ "/tests/?Filter=(active=true) and (folder_name = '" + folderName + "')",
				HttpMethodUtils.GET, null);
		List<Map<String, Object>> listOfResults = (List<Map<String, Object>>) scripts.get(RESULTS);
		Optional<Map<String, Object>> optionalScript = listOfResults.stream()
				.filter(map -> ((List<Map<String, Object>>) map.get("custom_fields")).stream()
						.anyMatch(customMap -> customMap.get("name").equals(customColumnName)
								&& customMap.get("value").equals(fetchMetadataListVO.get(0).getScriptNumber())))
				.findFirst();
		if (optionalScript.isPresent()) {
			return optionalScript.get().get("id").toString();
		} else {
			throw new WatsEBSCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Script with number " + fetchMetadataListVO.get(0).getScriptNumber() + " not found in SmartBear.");
		}
	}

	private String executeTestRun(String projectId, String scriptId) throws IOException {
		logger.info("Executing test run for script ID: {}", scriptId);
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("TestId", scriptId);
		Map<String, Object> execution = callApi(
				smartBearBaseUrl + URL_VERSION_2 + PROJECTS_PATH + projectId + "/testruns", HttpMethodUtils.POST,
				requestBody);
		if (execution.containsKey("id")) {
			return execution.get("id").toString();
		} else {
			throw new WatsEBSCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Failed to execute test run for script ID: " + scriptId);
		}
	}

	@SuppressWarnings("unchecked")
	private String getItemId(String projectId, String executionId) throws IOException {
		logger.info("Getting item ID for execution ID: {}", executionId);
		Map<String, Object> items = callApi(
				smartBearBaseUrl + URL_VERSION_2 + PROJECTS_PATH + projectId + TEST_RUNS_PATH + executionId + "/items",
				HttpMethodUtils.GET, null);
		List<Map<String, Object>> listOfResults = (List<Map<String, Object>>) items.get(RESULTS);
		Optional<Map<String, Object>> optionalItem = listOfResults.stream().findFirst();
		if (optionalItem.isPresent()) {
			return optionalItem.get().get("id").toString();
		} else {
			throw new WatsEBSCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"No test result item found for execution ID " + executionId + " in SmartBear.");
		}
	}

	private void updateExecutionStatus(String projectId, String executionId, String status) throws IOException {
		logger.info("Updating test run status for execution ID: {} to {}", executionId, status);
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("StatusCode", status);
		callApi(smartBearBaseUrl + URL_VERSION_2 + PROJECTS_PATH + projectId + TEST_RUNS_PATH + executionId, "PATCH",
				requestBody);
	}

	private void attachPdf(String url, String filePath, String authHeader) {
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
			OkHttpClient client = new OkHttpClient.Builder().build();
			Response response = client.newCall(request).execute();
			if (!response.isSuccessful()) {
				throw new WatsEBSCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
						"Failed to attach pdf file: " + response.code());
			}
			logger.info("PDF attached successfully.");
		} catch (IOException e) {
			e.printStackTrace();
			throw new WatsEBSCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Failed to attach pdf file: " + e.getMessage());
		}
	}

	public void attachScriptLevelPdf(String projectId, String testSetId, String sourceFile) {
		String url = smartBearBaseUrl + URL_VERSION_1 + PROJECTS_PATH + projectId + "/tests/" + testSetId + "/files";
		String authHeader = Credentials.basic(userName, password);
		attachPdf(url, sourceFile, authHeader);
	}

	public void attachExecutionLevelPdf(String projectId, String executionId, String itemId, String sourceFile) {
		String url = smartBearBaseUrl + URL_VERSION_2 + PROJECTS_PATH + projectId + TEST_RUNS_PATH + executionId
				+ "/items/" + itemId + "/report";
		String authHeader = Credentials.basic(userName, password);
		attachPdf(url, sourceFile, authHeader);
	}

	private Map<String, Object> callApi(String url, String method, Map<String, Object> requestBody) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		ApiValidationVO apiValidationData = new ApiValidationVO();
		apiValidationData.setUrl(url);
		apiValidationData.setHttpType(method);
		apiValidationData.setRequestBody(requestBody);
		executeSmartBearApi(apiValidationData);
		return mapper.readValue(apiValidationData.getResponse(), new TypeReference<Map<String, Object>>() {
		});
	}

	private void executeSmartBearApi(ApiValidationVO apiValidationData) {
		logger.info(
				"Received details as input in executeSmartBearApi: URL - {}, HTTP Type - {}, Request Header - {}, Request Body - {}, Response - {}",
				apiValidationData.getUrl(), apiValidationData.getHttpType(), apiValidationData.getRequestHeader(),
				apiValidationData.getRequestBody(), apiValidationData.getResponse());

		try {
			WebClient client = WebClient.create();

			// Setting headers
			HttpHeaders headers = new HttpHeaders();
			MultiValueMap<String, String> requestHeaders = new LinkedMultiValueMap<>();
			if (apiValidationData.getRequestHeader() != null) {
				requestHeaders.setAll(apiValidationData.getRequestHeader());
				headers.addAll(requestHeaders);
			}
			headers.setBasicAuth(userName, password);

			// Fetching HttpMethod
			HttpMethod httpMethod = HttpMethod.valueOf(apiValidationData.getHttpType());

			Mono<String> bodyToMono;

			// Checking if body required or not for API.
			if (apiValidationData.getRequestBody() != null
					&& !StringUtils.isEmpty(apiValidationData.getRequestBody())) {
				bodyToMono = client.method(httpMethod).uri(apiValidationData.getUrl())
						.headers(httpHeaders -> httpHeaders.addAll(headers)).contentType(MediaType.APPLICATION_JSON)
						.body(BodyInserters.fromObject(apiValidationData.getRequestBody())).retrieve()
						.bodyToMono(String.class);
			} else {
				bodyToMono = client.method(httpMethod).uri(apiValidationData.getUrl())
						.headers(httpHeaders -> httpHeaders.addAll(headers)).accept(MediaType.APPLICATION_JSON)
						.retrieve().bodyToMono(String.class);
			}

			// Getting the response.
			String result = bodyToMono.block();
			apiValidationData.setResponse(result);
		} catch (Exception ex) {
			logger.error("Error while calling API: {}", ex.getMessage());
			throw new WatsEBSCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Not able to hit the smartbear url!");
		}
	}
}