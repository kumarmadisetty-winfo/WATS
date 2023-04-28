package com.winfo.vo;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ScriptDtlsDto {

	private List<Integer> scriptId= new ArrayList<>();

	private String productVersion;
	
	private String customerName;
	
	
		
}
