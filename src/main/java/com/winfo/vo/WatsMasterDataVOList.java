package com.winfo.vo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WatsMasterDataVOList {
	@JsonProperty("data")
	private List<WatsMasterVO> data= new ArrayList<WatsMasterVO>();

	public List<WatsMasterVO> getData() {
		return data;
	}

	public void setData(List<WatsMasterVO> data) {
		this.data = data;
	}

}
	
	
	
	
	
		
	
