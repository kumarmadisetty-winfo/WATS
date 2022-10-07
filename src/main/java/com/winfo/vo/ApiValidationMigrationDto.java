package com.winfo.vo;

import java.util.ArrayList;
import java.util.List;

public class ApiValidationMigrationDto {
	
	private List<Integer> validationLookupCodes= new ArrayList<>();
	
	private String targetEnvironment;
	private boolean flag;

	public List<Integer> getValidationLookupCodes() {
		return validationLookupCodes;
	}

	public void setValidationLookupCodes(List<Integer> lookUpCodeIds) {
		this.validationLookupCodes = lookUpCodeIds;
	}

	public String getTargetEnvironment() {
		return targetEnvironment;
	}

	public void setTargetEnvironment(String customerName) {
		this.targetEnvironment = customerName;
	}

	

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public ApiValidationMigrationDto(List<Integer> validationLookupCodes, String targetEnvironment, boolean flag) {
		super();
		this.validationLookupCodes = validationLookupCodes;
		this.targetEnvironment = targetEnvironment;
		this.flag = flag;
	}

	public ApiValidationMigrationDto() {
	}

	
}
