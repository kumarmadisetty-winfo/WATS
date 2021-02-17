package com.winfo.vo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeleteScriptsData {
	@JsonProperty("script_id")
	private List<Integer> script_id= new ArrayList<Integer>();
	
	public List<Integer> getScript_id() {
		return script_id;
	}
	public void setScript_id(List<Integer> script_id) {
		this.script_id = script_id;
	}

}
