package com.winfo.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "all_directories")
public class AllDirectories {

	@EmbeddedId
	protected AllDirectoriesEmbeddedPK allDirectoriesEmbeddedPK;

	public AllDirectoriesEmbeddedPK getAllDirectoriesEmbeddedPK() {
		return allDirectoriesEmbeddedPK;
	}

	public void setAllDirectoriesEmbeddedPK(AllDirectoriesEmbeddedPK allDirectoriesEmbeddedPK) {
		this.allDirectoriesEmbeddedPK = allDirectoriesEmbeddedPK;
	}

}
