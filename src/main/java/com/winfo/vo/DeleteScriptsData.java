package com.winfo.vo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeleteScriptsData {
	@JsonProperty("script_id")
	private List<Integer> script_id= new ArrayList<Integer>();
	
	private String prod_ver;
	
	private boolean deleteAll;
	
	public List<Integer> getScript_id() {
		return script_id;
	}
	public void setScript_id(List<Integer> script_id) {
		this.script_id = script_id;
	}
	
	public String getProd_ver() {
		return prod_ver;
	}
	public void setProd_ver(String prod_ver) {
		this.prod_ver = prod_ver;
	}
	public boolean isDeleteAll() {
		return deleteAll;
	}
	public void setDeleteAll(boolean deleteAll) {
		this.deleteAll = deleteAll;
	}

}
