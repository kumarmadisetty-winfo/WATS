package com.winfo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelRecordsVO {

	private Integer contractNumber;
	private Integer contractLineNumber;
	private Double invoicedAmount;

	
}
