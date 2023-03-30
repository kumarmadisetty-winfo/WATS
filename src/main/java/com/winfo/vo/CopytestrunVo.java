package com.winfo.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CopytestrunVo {
	private int testScriptNo;
	private int project;
	private int configuration;
	private String newtestrunname;
	private String createdBy;
	private String incrementValue;

	@JsonFormat(pattern = "MM/dd/yyyy")
	private Date creationDate;
	@JsonProperty("requestType")
	private String requestType;
}
