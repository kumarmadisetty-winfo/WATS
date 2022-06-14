package com.winfo.services;

import java.io.Serializable;

public class FetchMetadataVO implements Serializable {

	private static final long serialVersionUID = -1844541395681694931L;

	private String script_id;
	private String script_number;
	private String test_result_line_id;
	private String script_meta_data_id;
	private String line_number;
	private String input_parameter;
	private String action;
	private String xpath_location;
	private String xpath_location1;
	private String input_value;
	private String status;
	private int RowNumber;
	private String test_set_id;
	private String customer_id;
	private String customer_number;
	private String customer_name;
	private String project_id;
	private String project_name;
	private String test_set_line_id;
	private String dependency;
	private String test_run_name;
	private String field_type;
	private String scenario_name;
	private String module;
	private String seq_num;
	private String step_description;
	private String script_description;
	private String test_script_param_id;
	private String conditional_popup;
	private String lineErrorMsg;
	private String testRunParamDesc;
//added executed_by
    private String executed_by;
	private String smtp_from_mail;
	private Integer dependencyScriptNumber;
	private String dependency_tr;
	private String targetApplicationName;
	
	
	
 public String getTargetApplicationName() {
		return targetApplicationName;
	}

	public void setTargetApplicationName(String targetApplicationName) {
		this.targetApplicationName = targetApplicationName;
	}

public String getLineErrorMsg() {
		return lineErrorMsg;
	}

	public void setLineErrorMsg(String lineErrorMsg) {
		this.lineErrorMsg = lineErrorMsg;
	}

	public String getTestRunParamDesc() {
		return testRunParamDesc;
	}

	public void setTestRunParamDesc(String testRunParamDesc) {
		this.testRunParamDesc = testRunParamDesc;
	}

public String getDependency_tr() {
		return dependency_tr;
	}

	public void setDependency_tr(String dependency_tr) {
		this.dependency_tr = dependency_tr;
	}

public String getSmtp_from_mail() {
		return smtp_from_mail;
	}

	public String getConditional_popup() {
	return conditional_popup;
}

public void setConditional_popup(String conditional_popup) {
	this.conditional_popup = conditional_popup;
}

	public void setSmtp_from_mail(String smtp_from_mail) {
		this.smtp_from_mail = smtp_from_mail;
	}

	//added set and get method   
	public String getExecuted_by() {
		return executed_by;
	}

	public void setExecuted_by(String executed_by) {
		this.executed_by = executed_by;
	}

	public String getTest_script_param_id() {
		return test_script_param_id;
	}

	public void setTest_script_param_id(String test_script_param_id) {
		this.test_script_param_id = test_script_param_id;
	}

	public String getScript_description() {
		return script_description;
	}

	public void setScript_description(String script_description) {
		this.script_description = script_description;
	}

	public String getStep_description() {
		return step_description;
	}

	public void setStep_description(String step_description) {
		this.step_description = step_description;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getSeq_num() {
		return seq_num;
	}

	public void setSeq_num(String seq_num) {
		this.seq_num = seq_num;
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

	public String getField_type() {
		return field_type;
	}

	public void setField_type(String field_type) {
		this.field_type = field_type;
	}

	public String getTest_run_name() {
		return test_run_name;
	}

	public void setTest_run_name(String test_run_name) {
		this.test_run_name = test_run_name;
	}

	@Override
	public String toString() {
		System.out.println(dependency);
		return super.toString();
	}
	
	public String getDependency() {
		return dependency;
	}
	public void setDependency(String dependency) {
		this.dependency = dependency;
	}
	public String getScript_id() {
		return script_id;
	}
	public void setScript_id(String script_id) {
		this.script_id = script_id;
	}
	public String getScript_number() {
		return script_number;
	}
	public void setScript_number(String script_number) {
		this.script_number = script_number;
	}
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}
	public String getCustomer_number() {
		return customer_number;
	}
	public void setCustomer_number(String customer_number) {
		this.customer_number = customer_number;
	}
	public String getCustomer_name() {
		return customer_name;
	}
	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}
	public String getProject_id() {
		return project_id;
	}
	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}
	public String getProject_name() {
		return project_name;
	}
	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}
	public String getTest_set_id() {
		return test_set_id;
	}
	public void setTest_set_id(String test_set_id) {
		this.test_set_id = test_set_id;
	}
	public String getTest_set_line_id() {
		return test_set_line_id;
	}
	public void setTest_set_line_id(String test_set_line_id) {
		this.test_set_line_id = test_set_line_id;
	}
	public int getRowNumber() {
		return RowNumber;
	}
	public void setRowNumber(int rowIndex) {
		RowNumber = rowIndex;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getXpath_location1() {
		return xpath_location1;
	}
	public void setXpath_location1(String xpath_location1) {
		this.xpath_location1 = xpath_location1;
	}
	
	public String getTest_result_line_id() {
		return test_result_line_id;
	}
	public void setTest_result_line_id(String test_result_line_id) {
		this.test_result_line_id = test_result_line_id;
	}
	public String getScript_meta_data_id() {
		return script_meta_data_id;
	}
	public void setScript_meta_data_id(String script_meta_data_id) {
		this.script_meta_data_id = script_meta_data_id;
	}
	public String getLine_number() {
		return line_number;
	}
	public void setLine_number(String line_number) {
		this.line_number = line_number;
	}
	public String getInput_parameter() {
		return input_parameter;
	}
	public void setInput_parameter(String input_parameter) {
		this.input_parameter = input_parameter;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getXpath_location() {
		return xpath_location;
	}
	public void setXpath_location(String xpath_location) {
		this.xpath_location = xpath_location;
	}
	public String getInput_value() {
		return input_value;
	}
	public void setInput_value(String input_value) {
		this.input_value = input_value;
	}

	public Integer getDependencyScriptNumber() {
		return dependencyScriptNumber;
	}

	public void setDependencyScriptNumber(Integer dependencyScriptNumber) {
		this.dependencyScriptNumber = dependencyScriptNumber;
	}
	
	
	
}
