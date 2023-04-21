package com.winfo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SanityCheckVO {

	private String status;
	private HealthCheckVO services;

}
