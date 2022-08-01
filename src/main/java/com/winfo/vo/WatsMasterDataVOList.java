package com.winfo.vo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.winfo.model.ScriptMaster;

public class WatsMasterDataVOList {
	
	@JsonProperty("data")
	private List<ScriptMasterDto> data= new ArrayList<>();

	public List<ScriptMasterDto> getData() {
		return data;
	}

	public void setData(List<ScriptMasterDto> data) {
		this.data = data;
	}
	

}
	
	
	
	
	
		
	
