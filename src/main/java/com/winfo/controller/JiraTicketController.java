package com.winfo.controller;

import java.util.List;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.winfo.services.JiraTicketBugService;
import com.winfo.vo.BugDetails;
import com.winfo.vo.DomGenericResponseBean;



@RestController
public class JiraTicketController {
	
	@Autowired
	JiraTicketBugService service;

	
	
		
//	@PostMapping(value = "/issue", consumes = { MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE })
	@PostMapping(value = "/issue")
	public List<DomGenericResponseBean> createJiraTicket( @RequestBody BugDetails bugdetails) throws ParseException {
		
		return service.createJiraTicket(bugdetails) ;
		
		
		
	}
	
	
	

}
