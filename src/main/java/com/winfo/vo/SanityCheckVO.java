package com.winfo.vo;

public class SanityCheckVO {

	private String status;
	private HealthCheckVO services;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public HealthCheckVO getServices() {
		return services;
	}

	public void setServices(HealthCheckVO services) {
		this.services = services;
	}

	public SanityCheckVO(String status, HealthCheckVO services) {
		this.status = status;
		this.services = services;
	}

}
