package com.winfo.vo;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class JiraUserManagement {

	@NotBlank(message = "Organization can not be blank")
	private String organization;

	@NotBlank(message = "User name can not be blank")
	private String userName;

	@NotBlank(message = "User mail can not be blank")
	private String userMail;

}
