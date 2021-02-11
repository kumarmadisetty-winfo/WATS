package com.winfo.vo;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WatsMetaDataVO {

	@JsonProperty("script_meta_data_id")
	private Integer script_meta_data_id;
	@JsonProperty("script_number")
	private String script_number;
	@JsonProperty("line_number")
	private Integer line_number;
	@JsonProperty("input_parameter")
	private String input_parameter;
	@JsonProperty("action")
	private String action;
	@JsonProperty("xpath_location")
	private String xpath_location;
	@JsonProperty("xpath_location1")
	private String xpath_location1;
	@JsonProperty("created_by")
	private String created_by;
	@JsonProperty("creation_date")
	private Date creation_date;
	@JsonProperty("updated_by")
	private String updated_by;
	@JsonProperty("update_date")
	private Date update_date;
	@JsonProperty("step_desc")
	private String step_desc;
	@JsonProperty("field_type")
	private String field_type;
	@JsonProperty("hint")
	private String hint;
	public Integer getScript_meta_data_id() {
		return script_meta_data_id;
	}
	public void setScript_meta_data_id(Integer script_meta_data_id) {
		this.script_meta_data_id = script_meta_data_id;
	}
	public String getScript_number() {
		return script_number;
	}
	public void setScript_number(String script_number) {
		this.script_number = script_number;
	}
	public Integer getLine_number() {
		return line_number;
	}
	public void setLine_number(Integer line_number) {
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
	public String getXpath_location1() {
		return xpath_location1;
	}
	public void setXpath_location1(String xpath_location1) {
		this.xpath_location1 = xpath_location1;
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
	public String getStep_desc() {
		return step_desc;
	}
	public void setStep_desc(String step_desc) {
		this.step_desc = step_desc;
	}
	public String getField_type() {
		return field_type;
	}
	public void setField_type(String field_type) {
		this.field_type = field_type;
	}
	public String getHint() {
		return hint;
	}
	public void setHint(String hint) {
		this.hint = hint;
	}

	
	
}
