package com.winfo.vo;



import com.fasterxml.jackson.annotation.JsonProperty;

public class CopyDataDetails {

	@JsonProperty("product_version_new")
	private String product_version_new;
	
	@JsonProperty("product_version_old")
    private String product_version_old;
	public String getProduct_version_new() {
		return product_version_new;
	}
	public void setProduct_version_new(String product_version_new) {
		this.product_version_new = product_version_new;
	}

	public String getProduct_version_old() {
		return product_version_old;
	}
	public void setProduct_version_old(String product_version_old) {
		this.product_version_old = product_version_old;
	}
	
	
	
}
