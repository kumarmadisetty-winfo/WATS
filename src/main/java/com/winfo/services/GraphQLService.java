package com.winfo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winfo.config.GraphqlSchemaReaderUtil;
import com.winfo.vo.CustomerProjectDto;
import com.winfo.vo.GraphqlRequestBody;
import com.winfo.vo.ScriptDetailsDto;
import com.winfo.vo.ScriptJiraXrayCloud;
import com.winfo.vo.ScriptStepsJiraXrayCloud;

@Service
public class GraphQLService {

	public String createTestRunInJiraXrayCloud(CustomerProjectDto customerDetails) throws Exception {

		final String query = GraphqlSchemaReaderUtil.getSchemaFromFileName("create-test-run/create-test-run-schema");
	    final String variables = GraphqlSchemaReaderUtil.getSchemaFromFileName("create-test-run\\create-test-run-variable");
//		graphQLRequestBody.setQuery(query);
//		graphQLRequestBody.setVariables(variables.replace("summary", customerDetails.getTestSetName()).replace("key", "XN"));
	    JSONObject newJson = executeCommand(query,variables.replace("summary", customerDetails.getTestSetName()).replace("key", "XN"));
	    return newJson.getJSONObject("data").getJSONObject("createTestExecution").getJSONObject("testExecution").get("issueId").toString();
	}
	
	public String createScriptInJiraXrayCloud(List<ScriptDetailsDto> fetchMetadataListVO) throws Exception {

		final String query = GraphqlSchemaReaderUtil.getSchemaFromFileName("create-script\\create-script-schema");
		ScriptJiraXrayCloud scriptJiraXrayCloud = new ScriptJiraXrayCloud();
		scriptJiraXrayCloud.setSummary(fetchMetadataListVO.get(0).getScriptNumber() +" : "+ fetchMetadataListVO.get(0).getScenarioName());
		scriptJiraXrayCloud.setProjectKey("XN");
		
		List<ScriptStepsJiraXrayCloud> listOfSteps = new ArrayList<>();
		for(ScriptDetailsDto scriptDetailsDto : fetchMetadataListVO) {
			ScriptStepsJiraXrayCloud scriptStepsJiraXrayCloud = new ScriptStepsJiraXrayCloud();
			scriptStepsJiraXrayCloud.setAction(scriptDetailsDto.getAction());
			scriptStepsJiraXrayCloud.setData(scriptDetailsDto.getInputParameter() +" : "+ scriptDetailsDto.getInputValue());
			scriptStepsJiraXrayCloud.setResult(scriptDetailsDto.getStepDescription());
			listOfSteps.add(scriptStepsJiraXrayCloud);
		}
		scriptJiraXrayCloud.setListOfStepsWithScript(listOfSteps);
		ObjectMapper mapper = new ObjectMapper();
		JSONObject newJson = executeCommand(query,mapper.writeValueAsString(scriptJiraXrayCloud));
	    return newJson.getJSONObject("data").getJSONObject("createTest").getJSONObject("test").get("issueId").toString();
	}
	
	public void associateScriptToTestRun(FetchConfigVO fetchConfigVO,String key) throws Exception {

		final String query = GraphqlSchemaReaderUtil.getSchemaFromFileName("associate-script-to-test-run\\associate-script-to-test-run-schema");
		ScriptJiraXrayCloud scriptJiraXrayCloud = new ScriptJiraXrayCloud();
		scriptJiraXrayCloud.setTestRunIssueId(fetchConfigVO.getTestRunIssueId());
		scriptJiraXrayCloud.setScriptIssueId(key);
		ObjectMapper mapper = new ObjectMapper();
		executeCommand(query,mapper.writeValueAsString(scriptJiraXrayCloud));
	}
	
	
	public void createScriptStepsInJiraXrayCloud() throws Exception {
		WebClient webClient = WebClient.builder().build();
		GraphqlRequestBody graphQLRequestBody = new GraphqlRequestBody();
		final String query = GraphqlSchemaReaderUtil.getSchemaFromFileName("update-test-steps-status/update-test-steps-status-schema");
	    final String variables = GraphqlSchemaReaderUtil.getSchemaFromFileName("update-test-steps-status/update-test-steps-variable");
		graphQLRequestBody.setQuery(query);
		graphQLRequestBody.setVariables(variables);
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0ZW5hbnQiOiIzYzM4NzA5Mi01NDg5LTM2MTEtYmFkNS1lNGNhNDIxNTZlODYiLCJhY2NvdW50SWQiOiI2MzlhYjMyNGIxMDgyYzMwYmI5MjI3NzMiLCJpc1hlYSI6ZmFsc2UsImlhdCI6MTY3MTM0NjU3NywiZXhwIjoxNjcxNDMyOTc3LCJhdWQiOiI4MTgzMjRFQkVBNzg0MDg3QjUwQkEzODAzNUU1NkM2MCIsImlzcyI6ImNvbS54cGFuZGl0LnBsdWdpbnMueHJheSIsInN1YiI6IjgxODMyNEVCRUE3ODQwODdCNTBCQTM4MDM1RTU2QzYwIn0.pxDFxugeUZXO-SfYxtpEN1dRRPuVKQiajCC56_p25Iw");
		String response = webClient.post().uri("http://xray.cloud.xpand-it.com/api/v1/graphql").headers(headersHttp -> headersHttp.addAll(headers)).syncBody(graphQLRequestBody).retrieve().bodyToMono(String.class).block();
		System.out.println(response);
	}
	
	public String getScriptId(FetchConfigVO fetchConfigVO,String scriptIssueId) throws Exception {
		final String query = GraphqlSchemaReaderUtil.getSchemaFromFileName("update-script-status\\get-script-id-schema");
		ScriptJiraXrayCloud scriptJiraXrayCloud = new ScriptJiraXrayCloud();
		scriptJiraXrayCloud.setTestRunIssueId(fetchConfigVO.getTestRunIssueId());
		scriptJiraXrayCloud.setScriptIssueId(scriptIssueId);
		ObjectMapper mapper = new ObjectMapper();
		JSONObject newJson = executeCommand(query,mapper.writeValueAsString(scriptJiraXrayCloud));
	    return newJson.getJSONObject("data").getJSONObject("getTestRuns").getJSONArray("results").getJSONObject(0).get("id").toString();
	}
	
	public void changeStatusOfScriptInJiraXrayCloud(String id, String status) throws Exception {
		final String query = GraphqlSchemaReaderUtil.getSchemaFromFileName("update-script-status\\update-script-status-schema");
		ScriptJiraXrayCloud scriptJiraXrayCloud = new ScriptJiraXrayCloud();
		scriptJiraXrayCloud.setStatus(status);
		scriptJiraXrayCloud.setScriptId(id);
		ObjectMapper mapper = new ObjectMapper();
		executeCommand(query,mapper.writeValueAsString(scriptJiraXrayCloud));
	}
	
	public void addAttachmentToScript(String scriptId, String is, String fileName) throws Exception {
		final String query = GraphqlSchemaReaderUtil.getSchemaFromFileName("update-script-status\\attachment-schema");
		ScriptJiraXrayCloud scriptJiraXrayCloud = new ScriptJiraXrayCloud();
		scriptJiraXrayCloud.setFileName(fileName);
		scriptJiraXrayCloud.setScriptId(scriptId);
		scriptJiraXrayCloud.setInputStream(is);
		ObjectMapper mapper = new ObjectMapper();
		executeCommand(query,mapper.writeValueAsString(scriptJiraXrayCloud));
	}
	
	public void changeStatusOfScriptStepsInJiraXrayCloud() throws Exception {
		WebClient webClient = WebClient.builder().build();
		GraphqlRequestBody graphQLRequestBody = new GraphqlRequestBody();
		final String query = GraphqlSchemaReaderUtil.getSchemaFromFileName("update-test-steps-status/update-test-steps-status-schema");
	    final String variables = GraphqlSchemaReaderUtil.getSchemaFromFileName("update-test-steps-status/update-test-steps-variable");
		graphQLRequestBody.setQuery(query);
		graphQLRequestBody.setVariables(variables);
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0ZW5hbnQiOiIzYzM4NzA5Mi01NDg5LTM2MTEtYmFkNS1lNGNhNDIxNTZlODYiLCJhY2NvdW50SWQiOiI2MzlhYjMyNGIxMDgyYzMwYmI5MjI3NzMiLCJpc1hlYSI6ZmFsc2UsImlhdCI6MTY3MTM0NjU3NywiZXhwIjoxNjcxNDMyOTc3LCJhdWQiOiI4MTgzMjRFQkVBNzg0MDg3QjUwQkEzODAzNUU1NkM2MCIsImlzcyI6ImNvbS54cGFuZGl0LnBsdWdpbnMueHJheSIsInN1YiI6IjgxODMyNEVCRUE3ODQwODdCNTBCQTM4MDM1RTU2QzYwIn0.pxDFxugeUZXO-SfYxtpEN1dRRPuVKQiajCC56_p25Iw");
		String response = webClient.post().uri("http://xray.cloud.xpand-it.com/api/v1/graphql").headers(headersHttp -> headersHttp.addAll(headers)).syncBody(graphQLRequestBody).retrieve().bodyToMono(String.class).block();
		System.out.println(response);
	}
	
	public void accessToken() {
		
	}
	
	public JSONObject executeCommand(String query, String variable) {
		WebClient webClient = WebClient.builder().build();
		GraphqlRequestBody graphQLRequestBody = new GraphqlRequestBody(query, variable);
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization",
				"Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0ZW5hbnQiOiIzYzM4NzA5Mi01NDg5LTM2MTEtYmFkNS1lNGNhNDIxNTZlODYiLCJhY2NvdW50SWQiOiI2MzlhYjMyNGIxMDgyYzMwYmI5MjI3NzMiLCJpc1hlYSI6ZmFsc2UsImlhdCI6MTY3MTUxNTgwMSwiZXhwIjoxNjcxNjAyMjAxLCJhdWQiOiI4MTgzMjRFQkVBNzg0MDg3QjUwQkEzODAzNUU1NkM2MCIsImlzcyI6ImNvbS54cGFuZGl0LnBsdWdpbnMueHJheSIsInN1YiI6IjgxODMyNEVCRUE3ODQwODdCNTBCQTM4MDM1RTU2QzYwIn0.nnu-Rns6PUY6Ph2Ch7-eOgpmpnHFtSeDlSILbWW7dj0");
		String response = webClient.post().uri("http://xray.cloud.xpand-it.com/api/v1/graphql")
				.headers(headersHttp -> headersHttp.addAll(headers)).syncBody(graphQLRequestBody).retrieve()
				.bodyToMono(String.class).block();
		System.out.println(response);
		return new JSONObject(response);
	}
}
