package com.winfo.model;



import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FetchData {
	@JsonProperty("script_number")
	private String script_number;
	@JsonProperty("module")
	private String  module;
	@JsonProperty("scenario_name")
	private String scenario_name;
	@JsonProperty("scenario_description")
	private String scenario_description;
	@JsonProperty("product_version")
	private String product_version;
	@JsonProperty("priority")
	private Integer priority;
	@JsonProperty("process_area")
	private String process_area;
	@JsonProperty("role")
	private String role;
	@JsonProperty("sub_process_area")
	private String sub_process_area;
	@JsonProperty("standard_custom")
	private String standard_custom;
	@JsonProperty("test_script_status")
	private String test_script_status;
	@JsonProperty("customer_id")
	private Integer customer_id;
	@JsonProperty("dependency")
	private Integer dependency;
	@JsonProperty("end2end_scenario")
	private String end2end_scenario;
	@JsonProperty("expected_result")
	private String expected_result;
	@JsonProperty("selenium_test_script_name")
	private String selenium_test_script_name;
	@JsonProperty("selenium_test_method")
	private String selenium_test_method;
	@JsonProperty("author")
	private String author;
	@JsonProperty("created_by")
	private String created_by;
	@JsonProperty("creation_date")
	private Date creation_date;
	@JsonProperty("updated_by")
	private String updated_by;
	@JsonProperty("update_date")
	private Date update_date;
	@JsonProperty("customisation_refrence")
	private String customisation_refrence;
	@JsonProperty("attribute1")
	private String attribute1;
	@JsonProperty("attribute2")
	private String attribute2;
	@JsonProperty("attribute3")
	private String attribute3;
	@JsonProperty("attribute4")
	private String attribute4;
	@JsonProperty("attribute5")
	private String attribute5;
	@JsonProperty("attribute6")
	private String attribute6;
	@JsonProperty("attribute7")
	private String attribute7;
	@JsonProperty("attribute8")
	private String attribute8;
	@JsonProperty("attribute9")
	private String attribute9;
	@JsonProperty("attribute10")
	private String attribute10;
	@JsonProperty("DEPENDENT_SCRIPT_NUM")
    private String dependent_script_num;
	
	
	public String getDependent_script_num() {
		return dependent_script_num;
	}
	public void setDependent_script_num(String dependent_script_num) {
		this.dependent_script_num = dependent_script_num;
	}
	public String getScript_number() {
		return script_number;
	}
	public void setScript_number(String script_number) {
		this.script_number = script_number;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getScenario_name() {
		return scenario_name;
	}
	public void setScenario_name(String scenario_name) {
		this.scenario_name = scenario_name;
	}
	public String getScenario_description() {
		return scenario_description;
	}
	public void setScenario_description(String scenario_description) {
		this.scenario_description = scenario_description;
	}
	public String getProduct_version() {
		return product_version;
	}
	public void setProduct_version(String product_version) {
		this.product_version = product_version;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public String getProcess_area() {
		return process_area;
	}
	public void setProcess_area(String process_area) {
		this.process_area = process_area;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getSub_process_area() {
		return sub_process_area;
	}
	public void setSub_process_area(String sub_process_area) {
		this.sub_process_area = sub_process_area;
	}
	public String getStandard_custom() {
		return standard_custom;
	}
	public void setStandard_custom(String standard_custom) {
		this.standard_custom = standard_custom;
	}
	public String getTest_script_status() {
		return test_script_status;
	}
	public void setTest_script_status(String test_script_status) {
		this.test_script_status = test_script_status;
	}
	public Integer getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(Integer customer_id) {
		this.customer_id = customer_id;
	}
	public Integer getDependency() {
		return dependency;
	}
	public void setDependency(Integer dependency) {
		this.dependency = dependency;
	}
	public String getEnd2end_scenario() {
		return end2end_scenario;
	}
	public void setEnd2end_scenario(String end2end_scenario) {
		this.end2end_scenario = end2end_scenario;
	}
	public String getExpected_result() {
		return expected_result;
	}
	public void setExpected_result(String expected_result) {
		this.expected_result = expected_result;
	}
	public String getSelenium_test_script_name() {
		return selenium_test_script_name;
	}
	public void setSelenium_test_script_name(String selenium_test_script_name) {
		this.selenium_test_script_name = selenium_test_script_name;
	}
	public String getSelenium_test_method() {
		return selenium_test_method;
	}
	public void setSelenium_test_method(String selenium_test_method) {
		this.selenium_test_method = selenium_test_method;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getCreated_by() {
		return created_by;
	}
	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}
	public Date getCreation_date() {
		return creation_date;
	}
	public void setCreation_date(Date creation_date) {
		this.creation_date = creation_date;
	}
	public String getUpdated_by() {
		return updated_by;
	}
	public void setUpdated_by(String updated_by) {
		this.updated_by = updated_by;
	}
	public Date getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}
	public String getCustomisation_refrence() {
		return customisation_refrence;
	}
	public void setCustomisation_refrence(String customisation_refrence) {
		this.customisation_refrence = customisation_refrence;
	}
	public String getAttribute1() {
		return attribute1;
	}
	public void setAttribute1(String attribute1) {
		this.attribute1 = attribute1;
	}
	public String getAttribute2() {
		return attribute2;
	}
	public void setAttribute2(String attribute2) {
		this.attribute2 = attribute2;
	}
	public String getAttribute3() {

		return attribute3;
	}
	public void setAttribute3(String attribute3) {
		this.attribute3 = attribute3;
	}
	public String getAttribute4() {
		return attribute4;
	}
	public void setAttribute4(String attribute4) {
		this.attribute4 = attribute4;
	}
	public String getAttribute5() {
		return attribute5;
	}
	public void setAttribute5(String attribute5) {
		this.attribute5 = attribute5;
	}
	public String getAttribute6() {
		return attribute6;
	}
	public void setAttribute6(String attribute6) {
		this.attribute6 = attribute6;
	}
	public String getAttribute7() {
		return attribute7;
	}
	public void setAttribute7(String attribute7) {
		this.attribute7 = attribute7;
	}
	public String getAttribute8() {
		return attribute8;
	}
	public void setAttribute8(String attribute8) {
		this.attribute8 = attribute8;
	}
	public String getAttribute9() {
		return attribute9;
	}
	public void setAttribute9(String attribute9) {
		this.attribute9 = attribute9;
	}
	public String getAttribute10() {
		return attribute10;
	}
	public void setAttribute10(String attribute10) {
		this.attribute10 = attribute10;
	}
	
	
	
	

	
		}
