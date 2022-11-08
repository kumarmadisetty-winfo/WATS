package com.winfo.controller;

import java.util.List;

import javax.validation.Valid;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.winfo.services.JiraTicketBugService;
import com.winfo.services.JiraUserServiceManagement;
import com.winfo.vo.BugDetails;
import com.winfo.vo.DomGenericResponseBean;
import com.winfo.vo.JiraUserManagement;
import com.winfo.vo.ResponseDto;

@RestController
public class JiraTicketController {

	@Autowired
	JiraTicketBugService service;
	@Autowired
	JiraUserServiceManagement jiraUserServiceManagement;

//	@PostMapping(value = "/issue", consumes = { MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE })
	@PostMapping(value = "/issue")
	public List<DomGenericResponseBean> createJiraTicket(@RequestBody BugDetails bugdetails) throws ParseException {

		return service.createJiraTicket(bugdetails);

	}

	@PostMapping(value = "/userManagement")
	public ResponseDto userManagement(@Valid @RequestBody JiraUserManagement jiraUserManagementDTO) throws Exception {
		return jiraUserServiceManagement.userManegement(jiraUserManagementDTO);
	}

	@PostMapping(value = "/removeUserAccess")
	public ResponseDto removeUser(@Valid @RequestBody JiraUserManagement jiraUserManagementDTO) throws Exception {
		return jiraUserServiceManagement.removeUser(jiraUserManagementDTO);
	}

	@PostMapping(value = "/removeOrganization/{organizationName}")
	public ResponseDto removeOrganization(@PathVariable String organizationName) throws Exception {
		return jiraUserServiceManagement.removeOrganization(organizationName);
	}
}
