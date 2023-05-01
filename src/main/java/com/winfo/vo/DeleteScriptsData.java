package com.winfo.vo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class DeleteScriptsData {

	@JsonProperty("script_id")
	private List<Integer> script_id = new ArrayList<Integer>();

	private String prod_ver;
	
	@JsonProperty("deleteAll")
	private boolean deleteAll;

}
