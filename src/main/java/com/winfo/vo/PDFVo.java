package com.winfo.vo;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class PDFVo {

	@NotNull
	private String fileName;

	@NotNull
	private String filePath;
	
	private boolean commonObjectStore;

}
