package com.winfo.vo;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ScriptJiraXrayCloud {

	private String summary;
	private String projectKey;
	private List<ScriptStepsJiraXrayCloud> listOfStepsWithScript;
	private String testRunIssueId;
	private String scriptIssueId;
	private String scriptId;
	private String status;
	private String inputStream;
	private String fileName;

	public ScriptJiraXrayCloud(String summary, String projectKey,
			List<ScriptStepsJiraXrayCloud> listOfStepsWithScript) {
		super();
		this.summary = summary;
		this.projectKey = projectKey;
		this.listOfStepsWithScript = listOfStepsWithScript;
	}
}
