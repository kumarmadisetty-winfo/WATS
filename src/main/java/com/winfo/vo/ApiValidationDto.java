package com.winfo.vo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class ApiValidationDto {
private List<LookUpCodeVO> lookup_codes= new ArrayList<>();
	private boolean flag;
	public List<LookUpCodeVO> getLookup_codes() {
		return lookup_codes;
	}
	public void setLookup_codes(List<LookUpCodeVO> lookup_codes) {
		this.lookup_codes = lookup_codes;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public ApiValidationDto(List<LookUpCodeVO> lookup_codes, boolean flag) {
		super();
		this.lookup_codes = lookup_codes;
		this.flag = flag;
	}
	
	public ApiValidationDto() {
		// TODO Auto-generated constructor stub
	}
	
	
	

}
