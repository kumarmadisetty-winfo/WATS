package com.winfo.serviceImpl;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.winfo.constants.HttpMethodConstants;
import com.winfo.exception.WatsEBSException;
import com.winfo.utils.Constants;
import com.winfo.vo.ApiValidationVO;
import com.winfo.vo.JiraUserManagement;
import com.winfo.vo.ResponseDto;

import reactor.core.publisher.Mono;

@Service
@RefreshScope
public class JiraUserServiceManagement {

	public final Logger logger = LogManager.getLogger(JiraUserServiceManagement.class);
	private static final String CONTENT_TYPE = "Content-Type";
	private static final String APPLICATION_JSON = "application/json";
	private static final String ACCEPT = "Accept";
	private static final String VALUES = "values";
	private static final String ACCOUNT_ID = "accountId";

	@Value("${jira.username}")
	private String userName;
	@Value("${jira.password}")
	private String password;
	@Value("${jira.url}")
	private String url;
	@Value("${jira.projectId}")
	private String projectId;
	@Value("${jira.url1}")
	private String userSearchUrl;

	@SuppressWarnings("unchecked")
	public ResponseDto userManegement(JiraUserManagement jiraUserManagementDTO) throws Exception {
		logger.info("Received details as input : Organization - {}, Mail - {}, UserName - {}",
				jiraUserManagementDTO.getOrganization(), jiraUserManagementDTO.getUserMail(),
				jiraUserManagementDTO.getUserName());
		try {

			Map<String, Object> mapOfBody = new HashMap<>();

			List<Map<String, Object>> listOfOrganization = null;
			Optional<Map<String, Object>> getOrganizationIfExists;
			String organizationId = null;

			List<Map<String, Object>> listOfUser = null;
			Optional<Map<String, Object>> getUserIfExists;
			String userId = null;

			/******************** ORGANIZATION RELATED OPERATION *************************/
			// Fetching all the organization.
			listOfOrganization = (List<Map<String, Object>>) getAllTheOrganization().get(VALUES);

			// Checking if organization exists or not.
			getOrganizationIfExists = listOfOrganization.stream()
					.filter(map -> map.get("name").toString().equalsIgnoreCase(jiraUserManagementDTO.getOrganization()))
					.findFirst();

			// Creating organization if not exists or else get the id of the organization.
			if (!getOrganizationIfExists.isPresent()) {
				mapOfBody.put("name", jiraUserManagementDTO.getOrganization());
				organizationId = createOrganization(mapOfBody).get("id").toString();
				mapOfBody.clear();
			} else {
				organizationId = getOrganizationIfExists.get().get("id").toString();
				getOrganizationIfExists.get().clear();
			}

			// Getting all the organization present in serviceDesk.
			listOfOrganization.clear();
			listOfOrganization = (List<Map<String, Object>>) getOrganizationPresentInServiceDesk().get(VALUES);

			// Checking if organization exists in service desk or not.
			getOrganizationIfExists = listOfOrganization.stream()
					.filter(map -> map.get("name").toString().equalsIgnoreCase(jiraUserManagementDTO.getOrganization()))
					.findFirst();

			// Adding organization to project if already not added.
			if (!getOrganizationIfExists.isPresent()) {
				mapOfBody.put("organizationId", organizationId);
				addOrganizationToProject(mapOfBody);
				mapOfBody.clear();
			}

			/******************** USER RELATED OPERATION *************************/
			// Get User Details if exists.
			listOfUser = getUserInfo(jiraUserManagementDTO.getUserMail());

			// Checking if user exists in JIRA or not.
			getUserIfExists = listOfUser.stream().filter(
					map -> map.get("emailAddress").toString().equalsIgnoreCase(jiraUserManagementDTO.getUserMail()))
					.findFirst();

			// Create user in JIRA if already not exists else get the id.
			if (!getUserIfExists.isPresent()) {
				mapOfBody.put("displayName", jiraUserManagementDTO.getUserName());
				mapOfBody.put("email", jiraUserManagementDTO.getUserMail());
				userId = createUser(mapOfBody).get(ACCOUNT_ID).toString();
				mapOfBody.clear();
			} else {
				userId = getUserIfExists.get().get(ACCOUNT_ID).toString();
			}

			// Adding user to the organization.
			mapOfBody.put("accountIds", Arrays.asList(userId));
			addUserToOrganization(mapOfBody, organizationId);
			mapOfBody.clear();

			return new ResponseDto(HttpStatus.OK.value(), Constants.SUCCESS, "User Creation Completed!");
		} catch (Exception e) {
			logger.error("Failed during user creation " + e.getMessage());
			return new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.ERROR, e.getMessage());
		}
	}

	/*************************
	 * ALL THE REQUIRED PRIVATE METHODS
	 * 
	 * @throws Exception
	 ****************************/

	private List<Map<String, Object>> getUserInfo(String userMail) throws Exception {
		logger.info("Received details as input in getUserInfo() : Mail - {}", userMail);
		try {
			ObjectMapper mapper = new ObjectMapper();
			ApiValidationVO apiValidationData = new ApiValidationVO();
			apiValidationData.setUrl(userSearchUrl + userMail);
			Map<String, String> map = new HashMap<>();
			map.put(ACCEPT, APPLICATION_JSON);
			apiValidationData.setRequestHeader(map);
			apiValidationData.setHttpType(HttpMethodConstants.GET);
			apiValidationResponse(apiValidationData);
			return mapper.readValue(apiValidationData.getResponse(), new TypeReference<List<Map<String, Object>>>() {
			});
		} catch (Exception e) {
			logger.error("Failed to get the user info " + e.getMessage());
			throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Not able to get the user info!");
		}
	}

	private Map<String, Object> getAllTheOrganization() throws Exception {
		logger.info("Received details as input in getAllTheOrganization()");
		try {
			ObjectMapper mapper = new ObjectMapper();
			ApiValidationVO apiValidationData = new ApiValidationVO();
			apiValidationData.setUrl(url + "/organization");
			Map<String, String> map = new HashMap<>();
			map.put(ACCEPT, APPLICATION_JSON);
			apiValidationData.setRequestHeader(map);
			apiValidationData.setHttpType(HttpMethodConstants.GET);
			apiValidationResponse(apiValidationData);
			return mapper.readValue(apiValidationData.getResponse(), new TypeReference<Map<String, Object>>() {
			});
		} catch (Exception e) {
			logger.error("Failed to get all the organization details " + e.getMessage());
			throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Not able to get all the organization details!");
		}
	}

	private Map<String, Object> createOrganization(Map<String, Object> mapOfBody) throws Exception {
		logger.info("Received details as input in createOrganization() : Body - {}", mapOfBody);
		try {
			ObjectMapper mapper = new ObjectMapper();
			ApiValidationVO apiValidationData = new ApiValidationVO();

			Object bodyObject = mapper.convertValue(mapOfBody, Object.class);
			apiValidationData.setRequestBody(bodyObject);

			apiValidationData.setUrl(url + "/organization");
			Map<String, String> mapOfHeaders = new HashMap<>();
			mapOfHeaders.put(ACCEPT, APPLICATION_JSON);
			mapOfHeaders.put(CONTENT_TYPE, APPLICATION_JSON);
			apiValidationData.setRequestHeader(mapOfHeaders);
			apiValidationData.setHttpType(HttpMethodConstants.POST);
			apiValidationResponse(apiValidationData);
			return mapper.readValue(apiValidationData.getResponse(), new TypeReference<Map<String, Object>>() {
			});
		} catch (Exception e) {
			logger.error("Failed to create the organization " +e.getMessage());
			throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Not able to create the organization!");
		}
	}

	private void addOrganizationToProject(Map<String, Object> mapOfBody) throws Exception {
		logger.info("Received details as input in addOrganizationToProject() : Body - {}", mapOfBody);
		try {
			ObjectMapper mapper = new ObjectMapper();
			ApiValidationVO apiValidationData = new ApiValidationVO();

			Object bodyObject = mapper.convertValue(mapOfBody, Object.class);
			apiValidationData.setRequestBody(bodyObject);

			apiValidationData.setUrl(url + "/servicedesk/"+projectId+"/organization");
			Map<String, String> mapOfHeaders = new HashMap<>();
			mapOfHeaders.put(ACCEPT, APPLICATION_JSON);
			mapOfHeaders.put(CONTENT_TYPE, APPLICATION_JSON);
			apiValidationData.setRequestHeader(mapOfHeaders);
			apiValidationData.setHttpType(HttpMethodConstants.POST);
			apiValidationResponse(apiValidationData);
		} catch (Exception e) {
			logger.error("Failed to add the organization to the project " +e.getMessage());
			throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Not able to add the organization to the project!");
		}
	}

	private Map<String, Object> getOrganizationPresentInServiceDesk() throws Exception {
		logger.info("Received details as input in getOrganizationPresentInServiceDesk()");
		try {
			ObjectMapper mapper = new ObjectMapper();
			ApiValidationVO apiValidationData = new ApiValidationVO();
			apiValidationData.setUrl(url + "/servicedesk/"+projectId+"/organization");
			Map<String, String> mapOfHeaders = new HashMap<>();
			mapOfHeaders.put(ACCEPT, APPLICATION_JSON);
			apiValidationData.setRequestHeader(mapOfHeaders);
			apiValidationData.setHttpType(HttpMethodConstants.GET);
			apiValidationResponse(apiValidationData);
			return mapper.readValue(apiValidationData.getResponse(), new TypeReference<Map<String, Object>>() {
			});
		} catch (Exception e) {
			logger.error("Failed to get all the organization present in the project " +e.getMessage());
			throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Not able to get all the organization present in the project!");
		}
	}

	private Map<String, Object> createUser(Map<String, Object> mapOfBody) throws Exception {
		logger.info("Received details as input in createUser() : Body - {}", mapOfBody);
		try {
			ObjectMapper mapper = new ObjectMapper();
			ApiValidationVO apiValidationData = new ApiValidationVO();

			Object bodyObject = mapper.convertValue(mapOfBody, Object.class);
			apiValidationData.setRequestBody(bodyObject);

			apiValidationData.setUrl(url + "/customer");
			Map<String, String> mapOfHeaders = new HashMap<>();
			mapOfHeaders.put(ACCEPT, APPLICATION_JSON);
			mapOfHeaders.put(CONTENT_TYPE, APPLICATION_JSON);
			apiValidationData.setRequestHeader(mapOfHeaders);
			apiValidationData.setHttpType(HttpMethodConstants.POST);
			apiValidationResponse(apiValidationData);
			return mapper.readValue(apiValidationData.getResponse(), new TypeReference<Map<String, Object>>() {
			});
		} catch (Exception e) {
			logger.error("Failed to create new user "+e.getMessage());
			throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Not able to create new user!");
		}
	}

	private void addUserToOrganization(Map<String, Object> mapOfBody, String organizationId) throws Exception {
		logger.info("Received details as input in addUserToOrganization() : Body - {}, Organization ID - {}", mapOfBody,
				organizationId);
		try {
			ObjectMapper mapper = new ObjectMapper();
			ApiValidationVO apiValidationData = new ApiValidationVO();

			Object bodyObject = mapper.convertValue(mapOfBody, Object.class);
			apiValidationData.setRequestBody(bodyObject);

			apiValidationData.setUrl(url + "/organization/" + organizationId + "/user");
			Map<String, String> mapOfHeaders = new HashMap<>();
			mapOfHeaders.put(CONTENT_TYPE, APPLICATION_JSON);
			apiValidationData.setRequestHeader(mapOfHeaders);
			apiValidationData.setHttpType(HttpMethodConstants.POST);
			apiValidationResponse(apiValidationData);
		} catch (Exception e) {
			logger.error("Failed to add the user to the organization!" +e.getMessage());
			throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Not able to add the user to the organization!");
		}
	}

	public void removeUserFromOrganization(Map<String, Object> mapOfBody, String organizationId) throws Exception {
		logger.info("Received details as input in removeUserFromOrganization() : Body - {}, Organization ID - {}",
				mapOfBody, organizationId);
		try {
			ObjectMapper mapper = new ObjectMapper();
			ApiValidationVO apiValidationData = new ApiValidationVO();

			Object bodyObject = mapper.convertValue(mapOfBody, Object.class);
			apiValidationData.setRequestBody(bodyObject);

			apiValidationData.setUrl(url + "/organization/" + organizationId + "/user");
			Map<String, String> mapOfHeaders = new HashMap<>();
			mapOfHeaders.put(CONTENT_TYPE, APPLICATION_JSON);
			apiValidationData.setRequestHeader(mapOfHeaders);
			apiValidationData.setHttpType(HttpMethodConstants.DELETE);
			apiValidationResponse(apiValidationData);
		} catch (Exception e) {
			logger.error("Failed to remove the user from the organization " +e.getMessage());
			throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Not able to remove the user from the organization!");
		}
	}

	public void removeUserFromProject(Map<String, Object> mapOfBody) throws Exception {
		logger.info("Received details as input in removeUserFromProject() : Body - {}", mapOfBody);
		try {
			ObjectMapper mapper = new ObjectMapper();
			ApiValidationVO apiValidationData = new ApiValidationVO();

			Object bodyObject = mapper.convertValue(mapOfBody, Object.class);
			apiValidationData.setRequestBody(bodyObject);

			apiValidationData.setUrl(url + "/servicedesk/"+projectId+"/customer");
			Map<String, String> mapOfHeaders = new HashMap<>();
			mapOfHeaders.put(CONTENT_TYPE, APPLICATION_JSON);
			mapOfHeaders.put("X-ExperimentalApi", "opt-in");
			apiValidationData.setRequestHeader(mapOfHeaders);
			apiValidationData.setHttpType(HttpMethodConstants.DELETE);
			apiValidationResponse(apiValidationData);
		} catch (Exception e) {
			logger.error("Failed to remove the user from the project "+e.getMessage());
			throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Not able to remove the user from the project!");
		}
	}

	public void removeOrganization(Map<String, Object> mapOfBody) throws Exception {
		logger.info("Received details as input in removeOrganization() : Body - {}", mapOfBody);
		try {
			ObjectMapper mapper = new ObjectMapper();
			ApiValidationVO apiValidationData = new ApiValidationVO();

			Object bodyObject = mapper.convertValue(mapOfBody, Object.class);
			apiValidationData.setRequestBody(bodyObject);

			apiValidationData.setUrl(url + "/servicedesk/"+projectId+"/organization");
			Map<String, String> mapOfHeaders = new HashMap<>();
			mapOfHeaders.put(CONTENT_TYPE, APPLICATION_JSON);
			apiValidationData.setRequestHeader(mapOfHeaders);
			apiValidationData.setHttpType(HttpMethodConstants.DELETE);
			apiValidationResponse(apiValidationData);
		} catch (Exception e) {
			logger.error("Failed to remove the organization from the project " +e.getMessage());
			throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Not able to remove the organization from the project!");
		}
	}

	/**************************
	 * API CALL METHODS WORKS ON DYNAMIC PARAMETER
	 * 
	 * @throws Exception
	 ************************/

	private void apiValidationResponse(ApiValidationVO apiValidationData) throws Exception {
		logger.info(
				"Received details as input in apiValidationResponse() : URL - {},HTTP Type - {},Request Header - {},Request Body - {},Response - {}",
				apiValidationData.getUrl(), apiValidationData.getHttpType(), apiValidationData.getRequestHeader(),
				apiValidationData.getRequestBody(), apiValidationData.getResponse());
		try {
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String strInput = ow.writeValueAsString(apiValidationData.getRequestBody());

			WebClient client = WebClient.create();

			// Setting headers
			HttpHeaders headers = new HttpHeaders();
			for (Entry<String, String> map : apiValidationData.getRequestHeader().entrySet()) {
				headers.set(map.getKey(), map.getValue());
			}
			headers.setBasicAuth(userName, password);

			// Fetching HttpMethod
			HttpMethod httpMethod = HttpMethod.valueOf(apiValidationData.getHttpType());

			Mono<String> bodyToMono = null;

			// Checking if body required or not for API.
			if (apiValidationData.getRequestBody() != null
					&& !ObjectUtils.isEmpty(apiValidationData.getRequestBody())) {
				bodyToMono = client.method(httpMethod).uri(new URI(apiValidationData.getUrl()))
						.headers(headersHttp -> headersHttp.addAll(headers)).accept(MediaType.APPLICATION_JSON)
						.body(BodyInserters.fromObject(strInput)).retrieve().bodyToMono(String.class);
			} else {
				bodyToMono = client.method(httpMethod).uri(new URI(apiValidationData.getUrl()))
						.headers(headersHttp -> headersHttp.addAll(headers)).accept(MediaType.APPLICATION_JSON)
						.retrieve().bodyToMono(String.class);
			}

			// Getting the response.
			String result = bodyToMono.block();
			apiValidationData.setResponse(result);
		} catch (Exception ex) {
			logger.error("Failed to hit the jira url " + ex.getMessage());
			throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Not able to hit the jira url!");
		}
	}

	/************************************
	 * REMOVE USER FROM ORGANIZATION AND PROJECT
	 * 
	 * @throws Exception
	 ***************************/

	@SuppressWarnings("unchecked")
	public ResponseDto removeUser(@Valid JiraUserManagement jiraUserManagementDTO) throws Exception {
		logger.info("Received details as input : Organization - {}, Mail - {}, UserName - {}",
				jiraUserManagementDTO.getOrganization(), jiraUserManagementDTO.getUserMail(),
				jiraUserManagementDTO.getUserName());
		try {
			Map<String, Object> mapOfBody = new HashMap<>();

			List<Map<String, Object>> listOfOrganization = null;
			Optional<Map<String, Object>> getOrganizationIfExists;

			List<Map<String, Object>> listOfUser = null;
			Optional<Map<String, Object>> getUserIfExists;

			// Fetching all the organization.
			listOfOrganization = (List<Map<String, Object>>) getAllTheOrganization().get(VALUES);

			// Checking if organization exists or not.
			getOrganizationIfExists = listOfOrganization.stream()
					.filter(map -> map.get("name").toString().equalsIgnoreCase(jiraUserManagementDTO.getOrganization()))
					.findFirst();

			// Get User Details if exists.
			listOfUser = getUserInfo(jiraUserManagementDTO.getUserMail());

			// Checking if user exists in JIRA or not.
			getUserIfExists = listOfUser.stream().filter(
					map -> map.get("emailAddress").toString().equalsIgnoreCase(jiraUserManagementDTO.getUserMail()))
					.findFirst();

			if (getUserIfExists.isPresent() && getOrganizationIfExists.isPresent()) {
				String organizationId = getOrganizationIfExists.get().get("id").toString();
				String userId = getUserIfExists.get().get(ACCOUNT_ID).toString();
				mapOfBody.put("accountIds", Arrays.asList(userId));
				removeUserFromOrganization(mapOfBody, organizationId);
				removeUserFromProject(mapOfBody);
				return new ResponseDto(HttpStatus.OK.value(), Constants.SUCCESS, "Successfully removed the user!");
			} else {
				return new ResponseDto(299, Constants.WARNING, "User does not exists!");
			}
		} catch (Exception e) {
			logger.error("Failed to remove the user "+e.getMessage());
			return new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.ERROR, e.getMessage());
		}
	}

	/************************************
	 * REMOVE ORGANIZATION FROM PROJECT
	 * 
	 * @throws Exception
	 ***************************/

	@SuppressWarnings("unchecked")
	public ResponseDto removeOrganization(@Valid String organizationName) throws Exception {
		try {
			Map<String, Object> mapOfBody = new HashMap<>();
			List<Map<String, Object>> listOfOrganization = null;
			Optional<Map<String, Object>> getOrganizationIfExists;
			String organizationId = null;

			// Fetching all the organization.
			listOfOrganization = (List<Map<String, Object>>) getAllTheOrganization().get(VALUES);

			// Checking if organization exists or not.
			getOrganizationIfExists = listOfOrganization.stream()
					.filter(map -> map.get("name").toString().equalsIgnoreCase(organizationName)).findFirst();

			// Remove if present.
			if (getOrganizationIfExists.isPresent()) {
				organizationId = getOrganizationIfExists.get().get("id").toString();
				mapOfBody.put("organizationId", organizationId);
				removeOrganization(mapOfBody);
				return new ResponseDto(HttpStatus.OK.value(), Constants.SUCCESS, "Successfully removed the organization!");
			} else {
				return new ResponseDto(299, Constants.WARNING, "Organization does not exists!");
			}
		} catch (Exception e) {
			logger.error("Successfully removed the organization " + e.getMessage());
			return new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.ERROR, e.getMessage());
		}

	}
}
