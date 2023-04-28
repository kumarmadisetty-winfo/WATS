package com.winfo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "wats_selenium_ebs_action_mapping")
@Data
public class SeleniumEbsMapping {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "selenium_action_name")
	private String seleniumActionName;
	
	@Column(name = "ebs_action_name")
	private String ebsActionName;

}
