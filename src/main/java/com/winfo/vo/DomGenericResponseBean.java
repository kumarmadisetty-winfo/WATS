package com.winfo.vo;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class DomGenericResponseBean {

	private int status;
	private String statusMessage;
	private String description;

	@JsonInclude(Include.NON_NULL)
	private String failed_Script;

	@JsonInclude(Include.NON_NULL)
	private Integer scriptID;

	@JsonInclude(Include.NON_NULL)
	private String testRunName;

	public DomGenericResponseBean(int status, String statusMessage, String description) {
		this.status = status;
		this.statusMessage = statusMessage;
		this.description = description;
	}
}
