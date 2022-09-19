package com.winfo.vo;

import java.util.ArrayList;
import java.util.List;

public class ScriptDtlsDto {

	private List<Integer> scriptId= new ArrayList<>();

	private String productVersion;
	
	private String customerName;
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public List<Integer> getScriptId() {
		return scriptId;
	}
	public void setScriptId(List<Integer> scriptId) {
		this.scriptId = scriptId;
	}
	public String getProductVersion() {
		return productVersion;
	}
	public void setProductVersion(String productVersion) {
		this.productVersion = productVersion;
	}
	
	
		
}
