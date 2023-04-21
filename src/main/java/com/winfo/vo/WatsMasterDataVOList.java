package com.winfo.vo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class WatsMasterDataVOList {

	@JsonProperty("data")
	private List<ScriptMasterDto> data = new ArrayList<>();

}
