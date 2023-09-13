package com.winfo.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "WIN_TA_CONFIG_LINES")
@Data
public class ConfigLines {

	@Id
	@Column(name = "CONF_LINE_ID")
	private Integer configLineId;

	@Column(name = "CONFIGURATION_ID")
	private Integer configurationId;

	@Column(name = "KEY_ID")
	private Integer keyId;

	@Column(name = "KEY_NAME")
	private String keyName;

	@Column(name = "VALUE_NAME")
	private String valueName;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATION_DATE")
	private Date creationDate;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Column(name = "UPDATE_DATE")
	private Date updateDate;

}
