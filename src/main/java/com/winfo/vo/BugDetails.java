package com.winfo.vo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BugDetails {
	
	@JsonProperty("test_set_id")
	private Integer test_set_id;
	
	
	@JsonProperty("script_id")
	private List<Integer> script_id= new ArrayList<Integer>();
	


	
	public Integer getTest_set_id() {
		return test_set_id;
	}



	public List<Integer> getScript_id() {
		return script_id;
	}



	public void setScript_id(List<Integer> script_id) {
		this.script_id = script_id;
	}



	public void setTest_set_id(Integer test_set_id) {
		this.test_set_id = test_set_id;
	}




	
}
