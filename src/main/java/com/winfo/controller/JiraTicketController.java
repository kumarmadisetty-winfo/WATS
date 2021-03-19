package com.winfo.controller;

import java.util.List;

import javax.validation.Valid;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.winfo.services.JiraTicketBugService;

import com.winfo.vo.BugDetails;
import com.winfo.vo.DomGenericResponseBean;
import com.winfo.vo.DomGenericResponseBean1;



@RestController
public class JiraTicketController {
	
	@Autowired
	JiraTicketBugService service;

	
	
		
//	@PostMapping(value = "/issue", consumes = { MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE })
	@PostMapping(value = "/issue")
	public List<DomGenericResponseBean1> createJiraTicket( @RequestBody BugDetails bugdetails) throws ParseException {
		
		return service.createJiraTicket(bugdetails) ;
		
		
		
	}
	
	
	

}
