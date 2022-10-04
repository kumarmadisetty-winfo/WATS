package com.winfo.vo;

import java.util.ArrayList;
import java.util.List;

public class ApiValidationMigrationDto {
	
	private List<Integer> validation_lookup_codes= new ArrayList<>();
	
	private String targetEnvironment;
	private boolean flag;

	public List<Integer> getValidation_lookup_codes() {
		return validation_lookup_codes;
	}

	public void setValidation_lookup_codes(List<Integer> lookUpCodeIds) {
		this.validation_lookup_codes = lookUpCodeIds;
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

	public ApiValidationMigrationDto(List<Integer> validation_lookup_codes, String targetEnvironment, boolean flag) {
		super();
		this.validation_lookup_codes = validation_lookup_codes;
		this.targetEnvironment = targetEnvironment;
		this.flag = flag;
	}

	public ApiValidationMigrationDto() {
	}

	
}
