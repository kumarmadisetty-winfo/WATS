package com.winfo.vo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class ApiValidationDto {
private List<LookUpCodeVO> lookupCodes= new ArrayList<>();
	private boolean flag;
	public List<LookUpCodeVO> getLookupCodes() {
		return lookupCodes;
	}
	public void setLookupCodes(List<LookUpCodeVO> lookup_codes) {
		this.lookupCodes = lookup_codes;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
	public ApiValidationDto(List<LookUpCodeVO> lookupCodes, boolean flag) {
		super();
		this.lookupCodes = lookupCodes;
		this.flag = flag;
	}
	public ApiValidationDto() {
		
	}
	
	
	

}
