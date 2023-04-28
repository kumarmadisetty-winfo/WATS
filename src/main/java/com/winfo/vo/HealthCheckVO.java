package com.winfo.vo;

import lombok.Data;

@Data
public class HealthCheckVO {

	private String database;
	private String seleniumGrid;
	private String objectStoreAccess;
	private String centralRepo;

}