package com.winfo.vo;

import java.util.ArrayList;
import java.util.List;

public class ApiValidationMigrationDto {
	
	private List<Integer> validation_lookup_codes= new ArrayList<>();
	
	private String targetEnvironment;

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

	public ApiValidationMigrationDto(List<Integer> lookUpCodeIds, String customerName) {
		this.validation_lookup_codes = lookUpCodeIds;
		this.targetEnvironment = customerName;
	}

	public ApiValidationMigrationDto() {
	}

	
}
