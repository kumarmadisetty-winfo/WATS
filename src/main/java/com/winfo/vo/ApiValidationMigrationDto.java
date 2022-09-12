package com.winfo.vo;

import java.util.ArrayList;
import java.util.List;

public class ApiValidationMigrationDto {
	
	private List<Integer> lookUpCodeIds= new ArrayList<>();
	
	private String customerName;

	public List<Integer> getLookUpCodeIds() {
		return lookUpCodeIds;
	}

	public void setLookUpCodeIds(List<Integer> lookUpCodeIds) {
		this.lookUpCodeIds = lookUpCodeIds;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public ApiValidationMigrationDto(List<Integer> lookUpCodeIds, String customerName) {
		this.lookUpCodeIds = lookUpCodeIds;
		this.customerName = customerName;
	}

	public ApiValidationMigrationDto() {
	}

	
}
