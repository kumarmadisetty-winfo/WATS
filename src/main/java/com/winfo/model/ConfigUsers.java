package com.winfo.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "WIN_TA_CONFIG_USERS")
@Data
public class ConfigUsers {

	@Id
	@Column(name = "USER_ID")
	private Integer userId;

	@Column(name = "CONFIG_ID")
	private Integer configIs;

	@Column(name = "USER_NAME")
	private String userName;

}
