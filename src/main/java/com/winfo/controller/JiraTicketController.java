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

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class JiraTicketController {

	@Autowired
	JiraTicketBugService service;
	@Autowired
	JiraUserServiceManagement jiraUserServiceManagement;

//	@PostMapping(value = "/issue", consumes = { MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE })
	@PostMapping(value = "/issue")
	@ApiOperation( value="Create Jira Ticket ",notes = " <B>TestsetId, TestsetLineId and ScriptId</B> is to pass to create the Issue in the Jira ticket for the particular test run")
	@ApiResponses( value = { @ApiResponse( code=200,message="Created Jira Ticket successfully")})
	public List<DomGenericResponseBean> createJiraTicket(@RequestBody BugDetails bugdetails) throws ParseException {

		return service.createJiraTicket(bugdetails);

	}

	@PostMapping(value = "/userManagement")
	@ApiOperation( value="User Creation ",notes = " <B>UserName , UserMail and Organization name required to create a user </B>")
	@ApiResponses( value = { @ApiResponse( code=200,message="Created user")})
	public ResponseDto userManagement(@Valid @RequestBody JiraUserManagement jiraUserManagementDTO) throws Exception {
		return jiraUserServiceManagement.userManegement(jiraUserManagementDTO);
	}

	@PostMapping(value = "/removeUserAccess")
	@ApiOperation( value="Remove User Access ",notes = " <B>Organization:</B> Organization name is required to delete the user account <br>"
			+ "<B>UserMail and UserName</B> is required to delete the particular user account ")
	@ApiResponses( value = { @ApiResponse( code=200,message="Removed User Successfully")})
	public ResponseDto removeUser(@Valid @RequestBody JiraUserManagement jiraUserManagementDTO) throws Exception {
		return jiraUserServiceManagement.removeUser(jiraUserManagementDTO);
	}

	@PostMapping(value = "/removeOrganization/{organizationName}")
	@ApiOperation( value="Remove Oraganization ",notes = " <B>Organization:</B> Organization name is required to delete the Oraganization ")
	@ApiResponses( value = { @ApiResponse( code=200,message="Organization removed Successfully")})
	public ResponseDto removeOrganization(@PathVariable String organizationName) throws Exception {
		return jiraUserServiceManagement.removeOrganization(organizationName);
	}
}
