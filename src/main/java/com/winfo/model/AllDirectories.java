package com.winfo.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "all_directories")
@Data
public class AllDirectories {

	@EmbeddedId
	protected AllDirectoriesEmbeddedPK allDirectoriesEmbeddedPK;


}
