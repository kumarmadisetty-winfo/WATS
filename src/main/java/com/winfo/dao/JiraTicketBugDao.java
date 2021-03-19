package com.winfo.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.reactive.function.BodyInserters;
//import org.springframework.web.multipart.MultipartRequest;
//import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winfo.model.FetchData;
import com.winfo.model.FetchDataMetadata;
import com.winfo.model.TestSet;
import com.winfo.services.JiraTicketBugService;
import com.winfo.vo.BugDetails;
import com.winfo.vo.DomGenericResponseBean;
import com.winfo.vo.DomGenericResponseBean1;

import com.winfo.vo.TestRunVO;

import reactor.core.publisher.Mono;


@Repository
public class JiraTicketBugDao {
	
		
	@Autowired
	private EntityManager entityManager;
	
	
	public   List<Object> createJiraTicket(Integer testsetid,List<Integer> scriptIds) throws ParseException
	
	{
		Session session = entityManager.unwrap(Session.class);
		Query fetchsummary;
		if(scriptIds.size()>0)
		{
			fetchsummary=session.createQuery("select b.test_set_id,a.script_id,a.seq_num,a.issue_key,b.test_set_name,a.test_set_line_id,a.status,b.configuration_id,a.script_number from TestSetLines a join a.testSet b where b.test_set_id=(:testsetId) and a.script_id in (:scriptidlist)");
			fetchsummary.setParameter("testsetId", testsetid);
			fetchsummary.setParameterList("scriptidlist", scriptIds);
		}
		else
		{
			fetchsummary=session.createQuery("select b.test_set_id,a.script_id,a.seq_num,a.issue_key,b.test_set_name,a.test_set_line_id,a.status,b.configuration_id,a.script_number from TestSetLines a join a.testSet b where b.test_set_id="+testsetid);
		}	 
	
		
		List<Object> summaryresult = (List<Object>) fetchsummary.list(); 
		return summaryresult;
	}
				
				public  List<String> createDescription(TestRunVO slist) {
					Session session = entityManager.unwrap(Session.class);
					Query fetchdescription=session.createQuery("select a.line_error_message from TestSetScriptParam a join  a.testSetLines b  on a.script_id=b.script_id  join b.testSet c on c.test_set_id="
			 		+slist.getTest_set_id()+ " and b.script_id="+slist.getScript_id()+" and upper(a.line_execution_status)='FAIL'");

				  
				  List<String> descriptionresult=(List<String>)fetchdescription.list();
				  return descriptionresult;
				}
					
				public List<String> getJiraIssueUrl(Integer configuration_id){
					Session session = entityManager.unwrap(Session.class);
					Query fetchjiraissueurl=session.createQuery("select jira_issue_url from ConfigTable where configuration_id="+configuration_id);
				
				   
					List<String> jiraissueurlresult = (List<String>) fetchjiraissueurl.list();
				    return jiraissueurlresult;
				}
				
				
				public int updateIssueKey(String issue_key,TestRunVO slist,int count) {
					Session session = entityManager.unwrap(Session.class);
						 Query updateissuekey=session.createQuery("update TestSetLines a set a.issue_key='"+issue_key+"'  where a.test_set_line_id="+slist.getTest_set_line_id());
						 count+= updateissuekey.executeUpdate();
						 return count;
				}
		
	}
	

	 

	
