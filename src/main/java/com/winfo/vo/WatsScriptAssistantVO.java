package com.winfo.vo;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
public class WatsScriptAssistantVO {

	@JsonInclude(Include.NON_NULL)
	private String targetEnvironment;

	@JsonInclude(Include.NON_NULL)
	private String browser;

	private List<Map<String, String>> groups;

}