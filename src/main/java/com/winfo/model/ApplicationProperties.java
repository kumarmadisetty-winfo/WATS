package com.winfo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "APPLICATION_PROPERTIES")
@Data
public class ApplicationProperties {

	@Id 
	@Column(name = "KEY_NAME")
	private String keyName;

	@Column(name = "VALUE_NAME")
	private String valueName;


}
