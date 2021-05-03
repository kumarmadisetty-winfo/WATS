package com.winfo.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.query.Query;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winfo.dao.JiraTicketBugDao;
import com.winfo.model.TestSet;
import com.winfo.model.TestSetLines;
import com.winfo.scripts.SeleniumKeyWords;
import com.winfo.vo.BugDetails;
import com.winfo.vo.DeleteScriptsData;
import com.winfo.vo.DomGenericResponseBean;
import com.winfo.vo.DomGenericResponseBean1;
import com.winfo.vo.TestRunVO;
import com.winfo.vo.WatsMasterVO;
import com.winfo.vo.WatsMetaDataVO;

import reactor.core.publisher.Mono;

@Service
@RefreshScope
public class JiraTicketBugService {
	@Autowired
private TestCaseDataService testRunService;
 
	@Autowired
	JiraTicketBugDao dao;
	@Autowired
    private WebClient.Builder webClientBuilder;
	@Value("${jira.username}")
	private String userName;
	@Value("${jira.password}")
	private String password;
	

	public String webClient(String jiraissueurl,JSONObject jsonobject ) {
		
		WebClient webClient =WebClient.builder().baseUrl(jiraissueurl)
                .defaultHeaders(httpHeaders -> httpHeaders.setBasicAuth(userName,password )).build();
		
		Mono<String> result = webClient.post().syncBody(jsonobject).retrieve().bodyToMono(String.class); 
			String response = result.block();
		return response;
				 	}
	
					
					
					  public String webClient1(String jiraattachmenturl,String testrunname,Integer seqnum,String scriptnumber,Integer testsetid) {
						
	//public String webClient1(String jiraattachmenturl) {
					  final WebClient webClient = webClientBuilder.defaultHeaders(httpHeaders ->
					  httpHeaders.setBasicAuth(userName, password)).defaultHeader("X-Atlassian-Token", "no-check").build();
					  Mono<String> result=webClient.post() .uri(jiraattachmenturl)
					  .contentType(MediaType.MULTIPART_FORM_DATA) 
					  .body(BodyInserters.fromMultipartData(fromFile(testrunname, seqnum, scriptnumber,testsetid))) .retrieve()
					
				//	  .body(BodyInserters.fromMultipartData(fromFile ()))
				//	  .retrieve()
					  .bodyToMono(String.class); 
					  String response = result.block(); 
					  return response;
					  
					  
					  } 
					  public MultiValueMap<String, HttpEntity<?>> fromFile(String testrunname,Integer seqnum,String scriptnumber,Integer testsetid) {
						
				//	  public MultiValueMap<String, HttpEntity<?>> fromFile() {
						  MultipartBodyBuilder builder = new MultipartBodyBuilder(); 
					 
						  FetchConfigVO fetchConfigVO = testRunService.getFetchConfigVO(testsetid.toString());
				
				//	  	  File	  filenew=new File("C:\\temptesting\\1_RTR.GL.116.pdf");
			
					  	  	  File filenew=new File(fetchConfigVO.getPdf_path()+"/"+testrunname+"/"+seqnum.toString()+"_"+scriptnumber+".pdf");
					
				  
					 
					  builder.part("file", new  FileSystemResource(filenew));
						  return builder.build();
						  }
					 				  
					 	
	@Transactional
	public   List<DomGenericResponseBean1> createJiraTicket( BugDetails bugdetails) throws ParseException {
	
		List<DomGenericResponseBean1> bean = new ArrayList<DomGenericResponseBean1>();
		Integer testsetid=bugdetails.getTest_set_id();
		
		List<Integer> scriptIds = bugdetails.getScript_id();
		List<Integer> scriptId = new ArrayList<Integer>();
     	int count=0;
     	
		List<Object> result= dao.createJiraTicket(testsetid,scriptIds);
		List<TestRunVO> finalresult=new ArrayList<TestRunVO>();
		Iterator itr = result.iterator();
		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
			TestRunVO testrunvo = new TestRunVO();
			if (String.valueOf(obj[0]).equals("null")) {
				testrunvo.setTest_set_id(null);
			} else {
				testrunvo.setTest_set_id(Integer.parseInt(String.valueOf(obj[0])));
			}
			if (String.valueOf(obj[1]).equals("null")) {
				testrunvo.setScript_id(null);
			} else {
				testrunvo.setScript_id(Integer.parseInt(String.valueOf(obj[1])));
			}
			if (String.valueOf(obj[2]).equals("null")) {
				testrunvo.setSeq_num(null);
			} else {
				testrunvo.setSeq_num(Integer.parseInt(String.valueOf(obj[2])));
			}
			if (String.valueOf(obj[3]).equals("null")) {
				testrunvo.setIssue_key(null);
			} else {
				testrunvo.setIssue_key(String.valueOf(obj[3]));
			}

			if (String.valueOf(obj[4]).equals("null")) {
				testrunvo.setTest_set_name(null);
			} else {
				testrunvo.setTest_set_name(String.valueOf(obj[4]));
			}
			if (String.valueOf(obj[5]).equals("null")) {
				testrunvo.setTest_set_line_id(null);
			} else {
				testrunvo.setTest_set_line_id(Integer.parseInt(String.valueOf(obj[5])));
			}
			if (String.valueOf(obj[6]).equals("null")) {
				testrunvo.setStatus(null);
			} else {
				testrunvo.setStatus(String.valueOf(obj[6]));
			}
			if (String.valueOf(obj[7]).equals("null")) {
				testrunvo.setConfiguration_id(null);
			} else {
				testrunvo.setConfiguration_id(Integer.parseInt(String.valueOf(obj[7])));
			}
			if (String.valueOf(obj[8]).equals("null")) {
				testrunvo.setScript_number(null);
			} else {
				testrunvo.setScript_number(String.valueOf(obj[8]));
			}

			finalresult.add(testrunvo);
		}
		  
		  for(TestRunVO slist:finalresult) { 
			  
		  if(slist.getIssue_key()==null && slist.getStatus().equalsIgnoreCase("FAIL"))
			  {
			  
			  	List fields=new ArrayList();
			  	String summary="TestSetID="+slist.getTest_set_id().toString()+" ScriptID="+slist.getScript_id().toString()+" Seqnumber="+slist.getSeq_num().toString()+" Testsetname="+slist.getTest_set_name();
				
			  	JSONArray jsonarray = new JSONArray();
				  JSONObject jsonobject = new JSONObject();
				  JSONObject requestjson = new JSONObject();
			  	
			  	jsonobject.put("summary",summary.toString());
			  	
				Map<String,String> projects=new HashMap<String,String>(); 
				  projects.put("key", "WATS");
				 
				 jsonobject.put("project",projects);
				  
					Map<String,String> issuetype=new HashMap<String,String>(); 
					issuetype.put("name","Bug");
				
					jsonobject.put("issuetype",issuetype);
					 List<String> descriptionResult=dao.createDescription(slist);
					 String description=descriptionResult.get(0);
						
						jsonobject.put("description", description);
					
						jsonarray.put(jsonobject.toString());
						 requestjson.put("fields", jsonarray);
						String JSONString1= requestjson.toString().replace("\\","");
						String str1="[";
						String str2="\"";
						String str4="]";
						String str3=str1+str2;
						String str5=str2+str4;
						String JSONString2=JSONString1.replace(str3,"").replace(str5,"");
//						String finalJSONString=JSONString2.replace(str5,"");
								
								

								JSONParser parser = new JSONParser(); 
								JSONObject finalJSONObject = (JSONObject) parser.parse(JSONString2);
								   List<String> result3 = dao.getJiraIssueUrl(slist.getConfiguration_id());
								   String jiraissueurl=result3.get(0);
								   
									       String jiraticketresponse= webClient(jiraissueurl,finalJSONObject);
								
									       
									       
									       String issuekey=null;
									        Map<String, Object> map;
											try {
												map = new ObjectMapper().readValue(jiraticketresponse, Map.class);
												
										        for (Map.Entry<String, Object> entry : map.entrySet()) {
										        	if(entry.getKey().equals("key"))
										        	{
										        		issuekey=entry.getValue().toString();
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
											
											
											 if(issuekey!=null){
												 String jiraattachmenturl=jiraissueurl+issuekey+"/attachments";
									
												 		 String jiraattachemtresponse= webClient1(jiraattachmenturl,slist.getTest_set_name(),slist.getSeq_num(),slist.getScript_number(),slist.getTest_set_id());
											 
										//	 String jiraattachemtresponse= webClient1(jiraissueurlattachment);
												
											 String issue_key=String.valueOf(issuekey);
												 
                                        count= dao.updateIssueKey(issue_key,slist,count);
                                        scriptId.add(slist.getScript_id());
									           }
									  }
									 
								  
								 
							
								}
									
								System.out.println(count + " Record(s) Updated.");
								  
								DomGenericResponseBean1 response = new DomGenericResponseBean1();
										if(count>0) {
										  response.setStatus(200);
										  response.setStatusMessage("SUCCESS");
										  response.setDescription("Issue Created Successfully for script ID "+scriptId.toString());
							             bean.add(response);
									  }
									else
									{
							              response.setStatus(400);
							              response.setStatusMessage("ERROR");
							              response.setDescription("Issue already exists for script ID");
							              bean.add(response);

									}
										return bean;
											         

	}
	
}