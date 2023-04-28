package com.winfo.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Embeddable
@Data
public class AllDirectoriesEmbeddedPK implements Serializable {

	private static final long serialVersionUID = 1L;
	@Column(name = "OWNER")
	protected String owner;
	@Column(name = "DIRECTORY_NAME")
	protected String directoryName;
	@Column(name = "DIRECTORY_PATH")
	protected String directoryPath;
	@Column(name = "ORIGIN_CON_ID")
	protected int originConId;



}
