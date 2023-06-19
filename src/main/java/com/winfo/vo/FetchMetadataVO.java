package com.winfo.vo;

import java.io.Serializable;

import lombok.Data;

@Data
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
    private String executed_by;
	private String smtp_from_mail;
	private Integer dependencyScriptNumber;
	private String dependency_tr;
	private String targetApplicationName;
		
}
