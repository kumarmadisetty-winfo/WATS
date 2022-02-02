package com.winfo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "wats_selenium_ebs_action_mapping")
public class SeleniumEbsMapping {
private long id; 
private String seleniumActionName;
private String ebsActionName;
@Id
@Column(name = "id")
@GeneratedValue(strategy = GenerationType.AUTO)
public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
}
@Column(name = "selenium_action_name")
public String getSeleniumActionName() {
	return seleniumActionName;
}
public void setSeleniumActionName(String seleniumActionName) {
	this.seleniumActionName = seleniumActionName;
}
@Column(name = "ebs_action_name")
public String getEbsActionName() {
	return ebsActionName;
}
public void setEbsActionName(String ebsActionName) {
	this.ebsActionName = ebsActionName;
}

}
