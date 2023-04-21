package com.winfo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "pyjab_actions")
@Data
public class PyJabActions {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "action_name")
	private String actionName;

	@Column(name = "param_values")
	private String paramValues;

	@Column(name = "method_name")
	private String methodName;

}
