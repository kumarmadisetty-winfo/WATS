package com.winfo.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
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

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getDirectoryName() {
		return directoryName;
	}

	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}

	public String getDirectoryPath() {
		return directoryPath;
	}

	public void setDirectoryPath(String directoryPath) {
		this.directoryPath = directoryPath;
	}

	public int getOriginConId() {
		return originConId;
	}

	public void setOriginConId(int originConId) {
		this.originConId = originConId;
	}

}
