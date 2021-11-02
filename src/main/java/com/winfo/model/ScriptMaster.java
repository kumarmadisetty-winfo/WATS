package com.winfo.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "WIN_TA_SCRIPT_MASTER")
public class ScriptMaster {

	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "master_generator")
	@SequenceGenerator(name = "master_generator", sequenceName = "WIN_TA_SCRIPT_MASTER_SEQ", allocationSize = 1)
	@Id
	//@GeneratedValue
	@Column(name = "SCRIPT_ID")
	private Integer script_id;
	@Column(name = "SCRIPT_NUMBER")
	private String script_number;
	@Column(name = "PROCESS_AREA")
	private String process_area;
	@Column(name = "SUB_PROCESS_AREA")
	private String sub_process_area;
	@Column(name = "MODULE")
	private String module;
	@Column(name = "ROLE")
	private String role;
	@Column(name = "END2END_SCENARIO")
	private String end2end_scenario;
	@Column(name = "SCENARIO_NAME")
	private String scenario_name;
	@Column(name = "SCENARIO_DESCRIPTION")
	private String scenario_description;
	@Column(name = "EXPECTED_RESULT")
	private String expected_result;
	@Column(name = "SELENIUM_TEST_SCRIPT_NAME")
	private String selenium_test_script_name;
	@Column(name = "SELENIUM_TEST_METHOD")
	private String selenium_test_method;
	@Column(name = "DEPENDENCY")
	private Integer dependency;
	@Column(name = "PRODUCT_VERSION")
	private String product_version;
	@Column(name = "STANDARD_CUSTOM")
	private String standard_custom;
	@Column(name = "TEST_SCRIPT_STATUS")
	private String test_script_status;
	@Column(name = "AUTHOR")
	private String author;
	@Column(name = "CREATED_BY")
	private String created_by;
	@Column(name = "CREATION_DATE")
	private Date creation_date;
	@Column(name = "UPDATED_BY")
	private String updated_by;
	@Column(name = "UPDATE_DATE")
	private Date update_date;
	@Column(name = "CUSTOMER_ID")
	private Integer customer_id;
	@Column(name = "CUSTOMISATION_REFERENCE")
	private String customisation_reference;
	@Column(name = "ATTRIBUTE1")
	private String attribute1;
	@Column(name = "ATTRIBUTE2")
	private String attribute2;
	@Column(name = "ATTRIBUTE3")
	private String attribute3;
	@Column(name = "ATTRIBUTE4")
	private String attribute4;
	@Column(name = "ATTRIBUTE5")
	private String attribute5;
	@Column(name = "ATTRIBUTE6")
	private String attribute6;
	@Column(name = "ATTRIBUTE7")
	private String attribute7;
	@Column(name = "ATTRIBUTE8")
	private String attribute8;
	@Column(name = "ATTRIBUTE9")
	private String attribute9;
	@Column(name = "ATTRIBUTE10")
	private String attribute10;
	@Column(name = "PRIORITY")
	private Integer priority;
	@Column(name = "DEPENDENT_SCRIPT_NUM")
    private String dependent_script_num;
    @Column(name = "APPR_FOR_MIGRATION")
    private String appr_for_migration;
    @Column(name = "PLUGIN_FLAG")
    private boolean plugin_flag;
    

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "scriptMaster")

	private List<ScriptMetaData> scriptMetaDatalist = new ArrayList<ScriptMetaData>();

	public void addMetadata(ScriptMetaData metadata) {
		scriptMetaDatalist.add(metadata);
		metadata.setScriptMaster(this);
	}

	public boolean isPlugin_flag() {
		return plugin_flag;
	}

	public void setPlugin_flag(boolean plugin_flag) {
		this.plugin_flag = plugin_flag;
	}

	public String getDependent_script_num() {
		return dependent_script_num;
	}

	public void setDependent_script_num(String dependent_script_num) {
		this.dependent_script_num = dependent_script_num;
	}

	public String getAppr_for_migration() {
		return appr_for_migration;
	}

	public void setAppr_for_migration(String appr_for_migration) {
		this.appr_for_migration = appr_for_migration;
	}

	public Integer getScript_id() {
		return script_id;
	}

	public void setScript_id(Integer script_id) {
		this.script_id = script_id;
	}

	public List<ScriptMetaData> getScriptMetaDatalist() {
		return scriptMetaDatalist;
	}

	public void setScriptMetaDatalist(List<ScriptMetaData> scriptMetaDatalist) {
		this.scriptMetaDatalist = scriptMetaDatalist;
	}

	public String getScript_number() {
		return script_number;
	}

	public void setScript_number(String script_number) {
		this.script_number = script_number;
	}

	public String getProcess_area() {
		return process_area;
	}

	public void setProcess_area(String process_area) {
		this.process_area = process_area;
	}

	public String getSub_process_area() {
		return sub_process_area;
	}

	public void setSub_process_area(String sub_process_area) {
		this.sub_process_area = sub_process_area;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getEnd2end_scenario() {
		return end2end_scenario;
	}

	public void setEnd2end_scenario(String end2end_scenario) {
		this.end2end_scenario = end2end_scenario;
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

	public Integer getDependency() {
		return dependency;
	}

	public void setDependency(Integer dependency) {
		this.dependency = dependency;
	}

	public String getProduct_version() {
		return product_version;
	}

	public void setProduct_version(String product_version) {
		this.product_version = product_version;
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

	public Integer getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(Integer customer_id) {
		this.customer_id = customer_id;
	}

	public String getCustomisation_reference() {
		return customisation_reference;
	}

	public void setCustomisation_reference(String customisation_reference) {
		this.customisation_reference = customisation_reference;
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

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

}
