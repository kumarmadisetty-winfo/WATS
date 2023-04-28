package com.winfo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class WatsLoginVO {

	@JsonProperty("username")
	private String username;

	@JsonProperty("password")
	private String password;

}
