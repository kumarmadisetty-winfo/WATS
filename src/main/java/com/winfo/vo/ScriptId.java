package com.winfo.vo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ScriptId {
	@JsonProperty("script_id")
	private List<Integer> script_id= new ArrayList<Integer>();
	@JsonProperty("product_version")
	private String product_version;
	
	public List<Integer> getScript_id() {
		return script_id;
	}
	public void setScript_id(List<Integer> script_id) {
		this.script_id = script_id;
	}
	public String getProduct_version() {
		return product_version;
	}
	public void setProduct_version(String product_version) {
		this.product_version = product_version;
	}
	
	
		
}
