package com.winfo.vo;

import java.util.List;

import lombok.Data;

@Data
public class TestRunDetails {

	private List<ExistTestRunDto> listOfTestRun;
	private String customerName;
	private String createdBy;

}
