package com.winfo.vo;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiValidationMigrationDto {
	
	private List<Integer> validationLookupCodes= new ArrayList<>();
	
	private String targetEnvironment;
	private boolean flag;

	

	
}
