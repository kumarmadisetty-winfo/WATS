package com.winfo.vo;

import java.util.List;

public class TestRunDetails {
	
	private List<ExistTestRunDto> listOfTestRun;
	private String customerName;

	public List<ExistTestRunDto> getListOfTestRun() {
		return listOfTestRun;
	}

	public void setListOfTestRun(List<ExistTestRunDto> listOfTestRun) {
		this.listOfTestRun = listOfTestRun;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

}
