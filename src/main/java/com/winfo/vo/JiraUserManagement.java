package com.winfo.vo;

import javax.validation.constraints.NotBlank;

public class JiraUserManagement {
	
	@NotBlank(message = "Organization can not be blank")
	private String organization;
	@NotBlank(message = "User name can not be blank")
	private String userName;
	@NotBlank(message = "User mail can not be blank")
	private String userMail;
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserMail() {
		return userMail;
	}
	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}
	
	

}
