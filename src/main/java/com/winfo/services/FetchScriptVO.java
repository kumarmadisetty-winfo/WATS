package com.winfo.services;

public class FetchScriptVO {

	private String p_script_id;
	private String p_status;
	private String p_test_set_id;
	private String p_test_set_line_id;
	private String p_script_number;
	private String p_input_parameter;
	private String p_pass_path;
	private String p_fail_path;
	private String p_exception_path;
	private String p_test_set_line_path;
	
	public FetchScriptVO() {};
	
	public FetchScriptVO(String p_test_set_id, String p_script_id, String p_test_set_line_id, String p_pass_path, String p_fail_path, String p_exception_path, String p_test_set_line_path) {
		
		this.p_test_set_id = p_test_set_id;
		this.p_script_id = p_script_id;
		this.p_test_set_line_id = p_test_set_line_id;
		this.p_pass_path = p_pass_path;
		this.p_fail_path = p_fail_path;
		this.p_exception_path = p_exception_path;
		this.p_test_set_line_path = p_test_set_line_path;	
	}
	
	
	public String getP_test_set_line_path() {
		return p_test_set_line_path;
	}
	public void setP_test_set_line_path(String p_test_set_line_path) {
		this.p_test_set_line_path = p_test_set_line_path;
	}
	public String getP_pass_path() {
		return p_pass_path;
	}
	public void setP_pass_path(String p_pass_path) {
		this.p_pass_path = p_pass_path;
	}
	public String getP_fail_path() {
		return p_fail_path;
	}
	public void setP_fail_path(String p_fail_path) {
		this.p_fail_path = p_fail_path;
	}
	public String getP_exception_path() {
		return p_exception_path;
	}
	public void setP_exception_path(String p_exception_path) {
		this.p_exception_path = p_exception_path;
	}
	public String getP_script_id() {
		return p_script_id;
	}
	public void setP_script_id(String p_script_id) {
		this.p_script_id = p_script_id;
	}
	public String getP_status() {
		return p_status;
	}
	public void setP_status(String p_status) {
		this.p_status = p_status;
	}
	public String getP_test_set_id() {
		return p_test_set_id;
	}
	public void setP_test_set_id(String p_test_set_id) {
		this.p_test_set_id = p_test_set_id;
	}
	public String getP_script_number() {
		return p_script_number;
	}
	public void setP_script_number(String p_script_number) {
		this.p_script_number = p_script_number;
	}
	public String getP_test_set_line_id() {
		return p_test_set_line_id;
	}
	public void setP_test_set_line_id(String p_test_set_line_id) {
		this.p_test_set_line_id = p_test_set_line_id;
	}
	public String getP_input_parameter() {
		return p_input_parameter;
	}
	public void setP_input_parameter(String p_input_parameter) {
		this.p_input_parameter = p_input_parameter;
	}
	

	
}
