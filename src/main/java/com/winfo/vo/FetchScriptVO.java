package com.winfo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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
	
	public FetchScriptVO(String p_test_set_id, String p_script_id, String p_test_set_line_id, String p_pass_path, String p_fail_path, String p_exception_path, String p_test_set_line_path) {
		
		this.p_test_set_id = p_test_set_id;
		this.p_script_id = p_script_id;
		this.p_test_set_line_id = p_test_set_line_id;
		this.p_pass_path = p_pass_path;
		this.p_fail_path = p_fail_path;
		this.p_exception_path = p_exception_path;
		this.p_test_set_line_path = p_test_set_line_path;	
	}
	
}
