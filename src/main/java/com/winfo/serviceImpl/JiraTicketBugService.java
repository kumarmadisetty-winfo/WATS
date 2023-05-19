package com.winfo.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winfo.config.MessageUtil;
import com.winfo.dao.JiraTicketBugDao;
import com.winfo.scripts.RunAutomation;
import com.winfo.vo.BugDetails;
import com.winfo.vo.DomGenericResponseBean;
import com.winfo.vo.TestRunVO;

import reactor.core.publisher.Mono;

@Service
@RefreshScope
public class JiraTicketBugService {
	
	public final Logger log = LogManager.getLogger(RunAutomation.class);
	private static final String TRANSITIONS = "transitions";
	@Autowired
	private TestCaseDataService testRunService;

	@Autowired
	JiraTicketBugDao dao;
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	@Autowired
	MessageUtil messageUtil;
	
	@Value("${jira.username}")
	private String userName;
	@Value("${jira.password}")
	private String password;

	@Autowired
	TestCaseDataService dataService;

	public String webClient(String jiraissueurl, JSONObject jsonobject) {

		WebClient webClient = WebClient.builder().baseUrl(jiraissueurl)
				.defaultHeaders(httpHeaders -> httpHeaders.setBasicAuth(userName, password)).build();

		Mono<String> result = webClient.post().syncBody(jsonobject).retrieve().bodyToMono(String.class);
		String response = result.block();
		return response;
	}

	public String webClient1(String jiraattachmenturl, String testrunname, Integer seqnum, String scriptnumber,
			Integer testsetid) {
		String response = null;
		try {
			// public String webClient1(String jiraattachmenturl) {
			final WebClient webClient = webClientBuilder
					.defaultHeaders(httpHeaders -> httpHeaders.setBasicAuth(userName, password))
					.defaultHeader("X-Atlassian-Token", "no-check").build();
			Mono<String> result = webClient.post().uri(jiraattachmenturl).contentType(MediaType.MULTIPART_FORM_DATA)
					.body(BodyInserters.fromMultipartData(fromFile(testrunname, seqnum, scriptnumber, testsetid)))
					.retrieve()

					// .body(BodyInserters.fromMultipartData(fromFile ()))
					// .retrieve()
					.bodyToMono(String.class);
			response = result.block();
		} catch (Exception e) {
			System.out.println(e);
		}
		return response;

	}

	public MultiValueMap<String, HttpEntity<?>> fromFile(String testrunname, Integer seqnum, String scriptnumber,
			Integer testsetid) {

		// public MultiValueMap<String, HttpEntity<?>> fromFile() {
		MultipartBodyBuilder builder = new MultipartBodyBuilder();
		try {
			String args = testsetid.toString();
			FetchConfigVO fetchConfigVO = testRunService.getFetchConfigVO(args);
			final String uri = fetchConfigVO.getUri_test_scripts() + args;
			List<FetchMetadataVO> fetchMetadataListVO = testRunService.getFetchMetaData(args, uri);

			// File filenew=new File("C:\\temptesting\\1_RTR.GL.116.pdf");
			// File filenew=new File("C:\\temptesting\\1_RTR.GL.116.pdf");

			// File filenew=new
			// File(fetchConfigVO.getPdf_path()+"/"+testrunname+"/"+seqnum.toString()+"_"+scriptnumber+".pdf");
			File filenew = new File(fetchConfigVO.getPdf_path() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
					+ testrunname + "/" + seqnum.toString() + "_" + scriptnumber + ".pdf");

			System.out.println("jira pdf path= " + filenew);
			builder.part("file", new FileSystemResource(filenew));
		} catch (Exception e) {
			System.out.println(e);
		}
		return builder.build();
	}

	@Transactional
	public List<DomGenericResponseBean> createJiraTicket(BugDetails bugdetails) throws ParseException {
		List<DomGenericResponseBean> bean = new ArrayList<DomGenericResponseBean>();
		try {
		Integer testsetid = bugdetails.getTest_set_id();
		int testSetLineId = bugdetails.getTestSetLineId();
		List<Integer> scriptIds = bugdetails.getScript_id();
		List<Integer> scriptId = new ArrayList<Integer>();
		List<String> scriptNumber = new ArrayList<String>();
		int count = 0;
		String issue_key=null;
		List<String> issueKeyList=new ArrayList<>();

		List<Object> result = dao.createJiraTicket(testsetid, scriptIds, testSetLineId);
		List<TestRunVO> finalresult = new ArrayList<TestRunVO>();
		Iterator itr = result.iterator();
		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
			TestRunVO testrunvo = new TestRunVO();
			if (String.valueOf(obj[0]).equals("null")) {
				testrunvo.setTestSetId(null);
			} else {
				testrunvo.setTestSetId(Integer.parseInt(String.valueOf(obj[0])));
			}
			if (String.valueOf(obj[1]).equals("null")) {
				testrunvo.setScriptId(null);
			} else {
				testrunvo.setScriptId(Integer.parseInt(String.valueOf(obj[1])));
			}
			if (String.valueOf(obj[2]).equals("null")) {
				testrunvo.setSeqNum(null);
			} else {
				testrunvo.setSeqNum(Integer.parseInt(String.valueOf(obj[2])));
			}
			if (String.valueOf(obj[3]).equals("null")) {
				testrunvo.setIssueKey(null);
			} else {
				testrunvo.setIssueKey(String.valueOf(obj[3]));
			}

			if (String.valueOf(obj[4]).equals("null")) {
				testrunvo.setTestSetName(null);
			} else {
				testrunvo.setTestSetName(String.valueOf(obj[4]));
			}
			if (String.valueOf(obj[5]).equals("null")) {
				testrunvo.setTestSetLineId(null);
			} else {
				testrunvo.setTestSetLineId(Integer.parseInt(String.valueOf(obj[5])));
			}
			if (String.valueOf(obj[6]).equals("null")) {
				testrunvo.setStatus(null);
			} else {
				testrunvo.setStatus(String.valueOf(obj[6]));
			}
			if (String.valueOf(obj[7]).equals("null")) {
				testrunvo.setConfigurationId(null);
			} else {
				testrunvo.setConfigurationId(Integer.parseInt(String.valueOf(obj[7])));
			}
			if (String.valueOf(obj[8]).equals("null")) {
				testrunvo.setScriptNumber(null);
			} else {
				testrunvo.setScriptNumber(String.valueOf(obj[8]));
			}
			if (String.valueOf(obj[9]).equals("null")) {
				testrunvo.setScenarioName(null);
			} else {
				testrunvo.setScenarioName(String.valueOf(obj[9]));
			}

			finalresult.add(testrunvo);
		}

		for (TestRunVO slist : finalresult) {

			if (slist.getIssueKey() == null && slist.getStatus().equalsIgnoreCase("FAIL")) {

				List fields = new ArrayList();
				String summary = "Test run name=" + slist.getTestSetName().toString() + " Seqnumber="
						+ slist.getSeqNum().toString() + " Script Number=" + slist.getScriptNumber().toString()
						+ " Test Case Name=" + slist.getScenarioName().toString();

				JSONArray jsonarray = new JSONArray();
				JSONObject jsonobject = new JSONObject();
				JSONObject requestjson = new JSONObject();

				jsonobject.put("summary", summary.toString());

				Map<String, String> projects = new HashMap<String, String>();
				projects.put("key", "WATS");

				jsonobject.put("project", projects);

				Map<String, String> issuetype = new HashMap<String, String>();
				issuetype.put("name", "Bug");

				jsonobject.put("issuetype", issuetype);
				List<String> descriptionResult = dao.createDescription(slist);
				String description = descriptionResult.get(0);

				jsonobject.put("description", description);
				
				Map<String, String> envName = new HashMap<String, String>();
				envName.put("id", "10125");
				
				jsonobject.put("customfield_10063", envName);

				jsonarray.put(jsonobject.toString());
				requestjson.put("fields", jsonarray);
				String JSONString1 = requestjson.toString().replace("\\", "");
				String str1 = "[";
				String str2 = "\"";
				String str4 = "]";
				String str3 = str1 + str2;
				String str5 = str2 + str4;
				String JSONString2 = JSONString1.replace(str3, "").replace(str5, "");
//						String finalJSONString=JSONString2.replace(str5,"");

				JSONParser parser = new JSONParser();
				JSONObject finalJSONObject = (JSONObject) parser.parse(JSONString2);
//				List<String> result3 = dao.getJiraIssueUrl(slist.getConfiguration_id());
//				String jiraissueurl = result3.get(0);
				FetchConfigVO fetchConfigVO = dataService.getFetchConfigVO(testsetid.toString());
				String jiraissueurl = fetchConfigVO.getJIRA_ISSUE_URL();
				String jiraticketresponse = webClient(jiraissueurl, finalJSONObject);

				String issuekey = null;
				Map<String, Object> map;
				try {
					map = new ObjectMapper().readValue(jiraticketresponse, Map.class);

					for (Map.Entry<String, Object> entry : map.entrySet()) {
						if (entry.getKey().equals("key")) {
							issuekey = entry.getValue().toString();
						}
						System.out.println(entry.getKey() + "=" + entry.getValue());

					}

				} catch (JsonParseException e) {

					e.printStackTrace();
				} catch (JsonMappingException e) {

					e.printStackTrace();
				} catch (IOException e) {

					e.printStackTrace();
				}
              
				if (issuekey != null) {
					String jiraattachmenturl = jiraissueurl + issuekey + "/attachments";

					String jiraattachemtresponse = webClient1(jiraattachmenturl, slist.getTestSetName(),
							slist.getSeqNum(), slist.getScriptNumber(), slist.getTestSetId());

					// String jiraattachemtresponse= webClient1(jiraissueurlattachment);

					issue_key = String.valueOf(issuekey);
					issueKeyList.add(issue_key);
					count = dao.updateIssueKey(issue_key, slist, count);
					scriptId.add(slist.getScriptId());
					scriptNumber.add(slist.getScriptNumber());
				}
			}else {
				scriptNumber.add(slist.getScriptNumber());
			}

		}

		System.out.println(count + " Record(s) Updated.");
		String finalIssuekey=String.join("','", issueKeyList);
		DomGenericResponseBean response = new DomGenericResponseBean();
		if (count > 0) {
			response.setStatus(200);
			response.setStatusMessage("SUCCESS");
			response.setDescription("Issue Created Successfully for script number " + scriptNumber.toString() + "," + "Jira Ticket Number " + "[" + finalIssuekey + "]");
			bean.add(response);
		} else {
			response.setStatus(400);
			response.setStatusMessage("ERROR");
			response.setDescription("Issue already exists for script number " + scriptNumber.toString());
			bean.add(response);

		}
		} catch (Exception e) {
			DomGenericResponseBean response = new DomGenericResponseBean();
			response.setStatus(400);
			response.setStatusMessage("ERROR");
			response.setDescription(messageUtil.getJiraTicketBugService().getError().getNotAbleToCreateIssue());
			bean.add(response);
			log.error(e);
//			throw new WatsEBSCustomException(500, MessageUtil.getMessage("JiraTicketBugService.Error.NotAbleToCreateIssue"));
		}
		return bean;

	}
	
	@SuppressWarnings("serial")
	public void jiraIssueFixed(String jiraIssueKey,String jiraIssueUrl,String jiraIssueTransitions) throws JsonMappingException, JsonProcessingException {
		try{
			log.info("changing status of Passed script in jira");
			String[] jiraIssueTransitionsArray = jiraIssueTransitions.split(",");
			List <String> jiraIssueTransitionsList = Arrays.asList(jiraIssueTransitionsArray);
			for(int j = 0 ; j < jiraIssueTransitionsList.size() ; j++){
				WebClient webClient = WebClient.builder().baseUrl(jiraIssueUrl+jiraIssueKey+"/transitions")
						.defaultHeaders(httpHeaders -> httpHeaders.setBasicAuth(userName, password)).build();
				Mono<String> result = webClient.get().retrieve().bodyToMono(String.class);
				String response = result.block();
				ObjectMapper mapper = new ObjectMapper();
				Map jsonMap=mapper.readValue(response, Map.class);
				org.json.JSONObject jsonObject = new org.json.JSONObject(jsonMap);
				org.json.JSONArray transitionArray = jsonObject.getJSONArray(TRANSITIONS);
				for(int i = 0 ; i < transitionArray.length() ; i++){
					String value = transitionArray.optJSONObject(i).getString("name");
					if(jiraIssueTransitionsList.get(j).equalsIgnoreCase(value)) {
						String id=transitionArray.optJSONObject(i).getString("id");
						Map<String,Map<String,String>> statusId = new HashMap<>();
						statusId.put("transition", new HashMap<String, String>() {{ put("id",id); }});
						JSONObject body = new JSONObject(statusId);
						webClient(jiraIssueUrl+jiraIssueKey+"/transitions", body);
					}
				}
			}
			log.info("Status of Passed script in jira Successfully changed.");
		}catch (Exception e) {
			log.error("Error occured while updating status of "+jiraIssueKey+" issue in jira");
		}
	}

}