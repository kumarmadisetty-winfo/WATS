package com.winfo.vo;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class WatsDocumentVo {

	@NotNull
	private String fileName;

	@NotNull
	private String watsVersion;

}
