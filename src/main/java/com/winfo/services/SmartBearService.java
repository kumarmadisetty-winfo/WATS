package com.winfo.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winfo.common.OkHttpService;
import com.winfo.common.WebClientService;
import com.winfo.exception.WatsEBSCustomException;
import com.winfo.utils.HttpMethodUtils;
import com.winfo.vo.ApiValidationVO;
import com.winfo.vo.ScriptDetailsDto;

import okhttp3.Credentials;

@Service
public class SmartBearService {
	private final Logger logger = LogManager.getLogger(SmartBearService.class);
	private static final String RESULTS = "results";
	private static final String PROJECTS_PATH = "/projects/";
	private static final String TEST_RUNS_PATH = "/testruns/";
	@Value("${smartbear.username:}")
	private String userName;

	@Value("${smartbear.password:}")
	private String password;

	@Value("${smartbear.version1.url:}")
	private String smartBearVersion1Url;

	@Value("${smartbear.version2.url:}")
	private String smartBearVersion2Url;

	@Autowired
	private DataBaseEntry databaseEntry;

	@Autowired
	private WebClientService webClientService;

	@Autowired
	private OkHttpService okHttpService;

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
		Map<String, Object> projects = callApi(smartBearVersion1Url + "/projects", HttpMethodUtils.GET, null);
		Optional<Map<String, Object>> optionalProject = ((List<Map<String, Object>>) projects.get(RESULTS)).stream()
				.filter(map -> map.get("proj_name").toString().equalsIgnoreCase(projectName)).findFirst();
		if (optionalProject.isPresent()) {
			return optionalProject.get().get("id").toString();
		} else {
			throw new WatsEBSCustomException(HttpStatus.NOT_FOUND.value(),
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
		Map<String, Object> scripts = callApi(smartBearVersion1Url + PROJECTS_PATH + projectId
				+ "/tests/?Filter=(active=true) and (folder_name = '" + folderName + "')", HttpMethodUtils.GET, null);

		Optional<Map<String, Object>> optionalScript = ((List<Map<String, Object>>) scripts.get(RESULTS)).stream()
				.filter(map -> ((List<Map<String, Object>>) map.get("custom_fields")).stream()
						.anyMatch(customMap -> customMap.get("name").equals(customColumnName)
								&& customMap.get("value").equals(fetchMetadataListVO.get(0).getScriptNumber())))
				.findFirst();
		if (optionalScript.isPresent()) {
			return optionalScript.get().get("id").toString();
		} else {
			throw new WatsEBSCustomException(HttpStatus.NOT_FOUND.value(),
					"Script with number " + fetchMetadataListVO.get(0).getScriptNumber() + " not found in SmartBear.");
		}
	}

	private String executeTestRun(String projectId, String scriptId) throws IOException {
		logger.info("Executing test run for script ID: {}", scriptId);
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("TestId", scriptId);
		Map<String, Object> execution = callApi(smartBearVersion2Url + PROJECTS_PATH + projectId + "/testruns",
				HttpMethodUtils.POST, requestBody);
		if (execution.containsKey("id")) {
			return execution.get("id").toString();
		} else {
			throw new WatsEBSCustomException(HttpStatus.NOT_FOUND.value(),
					"Failed to execute test run for script ID: " + scriptId);
		}
	}

	@SuppressWarnings("unchecked")
	private String getItemId(String projectId, String executionId) throws IOException {
		logger.info("Getting item ID for execution ID: {}", executionId);
		Map<String, Object> items = callApi(
				smartBearVersion2Url + PROJECTS_PATH + projectId + TEST_RUNS_PATH + executionId + "/items",
				HttpMethodUtils.GET, null);
		Optional<Map<String, Object>> optionalItem = ((List<Map<String, Object>>) items.get(RESULTS)).stream()
				.findFirst();
		if (optionalItem.isPresent()) {
			return optionalItem.get().get("id").toString();
		} else {
			throw new WatsEBSCustomException(HttpStatus.NOT_FOUND.value(),
					"No test result item found for execution ID " + executionId + " in SmartBear.");
		}
	}

	private void updateExecutionStatus(String projectId, String executionId, String status) throws IOException {
		logger.info("Updating test run status for execution ID: {} to {}", executionId, status);
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("StatusCode", status);
		callApi(smartBearVersion2Url + PROJECTS_PATH + projectId + TEST_RUNS_PATH + executionId, "PATCH", requestBody);
	}

	public void attachScriptLevelPdf(String projectId, String testSetId, String sourceFile) {
		String url = smartBearVersion1Url + PROJECTS_PATH + projectId + "/tests/" + testSetId + "/files";
		String authHeader = Credentials.basic(userName, password);
		okHttpService.attachPdf(url, sourceFile, authHeader);
	}

	public void attachExecutionLevelPdf(String projectId, String executionId, String itemId, String sourceFile) {
		String url = smartBearVersion2Url + PROJECTS_PATH + projectId + TEST_RUNS_PATH + executionId + "/items/"
				+ itemId + "/report";
		String authHeader = Credentials.basic(userName, password);
		okHttpService.attachPdf(url, sourceFile, authHeader);
	}

	private Map<String, Object> callApi(String url, String method, Map<String, Object> requestBody) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		ApiValidationVO apiValidationData = new ApiValidationVO();
		apiValidationData.setUrl(url);
		apiValidationData.setHttpType(method);
		apiValidationData.setRequestBody(requestBody);
		webClientService.executeWebClientApi(apiValidationData, userName, password);
		return mapper.readValue(apiValidationData.getResponse(), new TypeReference<Map<String, Object>>() {
		});
	}
}