package com.winfo.vo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class DeleteScriptsVO {

	@JsonProperty("scriptId")
	private List<Integer> scriptId = new ArrayList<Integer>();

	@JsonProperty("productVersion")
	private String productVersion;

	@JsonProperty("deleteAll")
	private boolean deleteAll;

}
