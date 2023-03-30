package com.winfo.vo;

import lombok.Data;

@Data
public class ExistTestRunDto {
	
	private Integer testSetId;
	private boolean forceMigrate = false;

}
