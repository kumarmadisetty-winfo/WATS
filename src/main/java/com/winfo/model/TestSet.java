package com.winfo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "WIN_TA_TEST_SET")

public class TestSet {
	@Id
	@GeneratedValue
	@Column(name = "TEST_SET_ID")
	private Integer test_set_id;

	@Column(name = "TEST_SET_DESC")
	private String test_set_desc;

	@Column(name = "TEST_SET_COMMENTS")
	private String test_set_comments;

	@Column(name = "ENABLED")
	private String enabled;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "EFFECTIVE_FROM")
	private Date effective_from;

	@Column(name = "EFFECTIVE_TO")
	private Date effective_to;

	@Column(name = "PROJECT_ID")
	private Integer project_id;

	@Column(name = "TEST_SET_NAME")
	private String test_set_name;

	@Column(name = "CONFIGURATION_ID")
	private Integer configuration_id;

	@Column(name = "CREATED_BY")
	private String created_by;

	@Column(name = "LAST_UPDATED_BY")
	private String last_updated_by;

	@Column(name = "CREATION_DATE")
	private Date creation_date;

	@Column(name = "UPDATE_DATE")
	private Date update_date;

	@Column(name = "LAST_EXECUTED_BY")
	private Date last_execute_by;

	@Column(name = "TS_COMPLETE_FLAG")
	private String ts_complete_flag;

	@Column(name = "PASS_PATH")
	private String pass_path;

	@Column(name = "FAIL_PATH")
	private String fail_path;

	@Column(name = "EXCEPTION_PATH")
	private String exception_path;

	@Column(name = "TR_MODE")
	private String tr_mode;

	@Column(name = "PDF_GENERATION")
	private String pdfGenerationEnabled;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "testSet")

	private List<TestSetLines> testSetLinesDatalist = new ArrayList<TestSetLines>();

	public void addTestSetLinesdata(TestSetLines setlines) {
		testSetLinesDatalist.add(setlines);
		setlines.setTestSet(this);
	}

	public Integer getTest_set_id() {
		return test_set_id;
	}

	public void setTest_set_id(Integer test_set_id) {
		this.test_set_id = test_set_id;
	}

	public String getTest_set_desc() {
		return test_set_desc;
	}

	public void setTest_set_desc(String test_set_desc) {
		this.test_set_desc = test_set_desc;
	}

	public String getTest_set_comments() {
		return test_set_comments;
	}

	public void setTest_set_comments(String test_set_comments) {
		this.test_set_comments = test_set_comments;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getEffective_from() {
		return effective_from;
	}

	public void setEffective_from(Date effective_from) {
		this.effective_from = effective_from;
	}

	public Date getEffective_to() {
		return effective_to;
	}

	public void setEffective_to(Date effective_to) {
		this.effective_to = effective_to;
	}

	public Integer getProject_id() {
		return project_id;
	}

	public void setProject_id(Integer project_id) {
		this.project_id = project_id;
	}

	public String getTest_set_name() {
		return test_set_name;
	}

	public void setTest_set_name(String test_set_name) {
		this.test_set_name = test_set_name;
	}

	public Integer getConfiguration_id() {
		return configuration_id;
	}

	public void setConfiguration_id(Integer configuration_id) {
		this.configuration_id = configuration_id;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public String getLast_updated_by() {
		return last_updated_by;
	}

	public void setLast_updated_by(String last_updated_by) {
		this.last_updated_by = last_updated_by;
	}

	public Date getCreation_date() {
		return creation_date;
	}

	public void setCreation_date(Date creation_date) {
		this.creation_date = creation_date;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	public Date getLast_execute_by() {
		return last_execute_by;
	}

	public void setLast_execute_by(Date last_execute_by) {
		this.last_execute_by = last_execute_by;
	}

	public String getTs_complete_flag() {
		return ts_complete_flag;
	}

	public void setTs_complete_flag(String ts_complete_flag) {
		this.ts_complete_flag = ts_complete_flag;
	}

	public String getPass_path() {
		return pass_path;
	}

	public void setPass_path(String pass_path) {
		this.pass_path = pass_path;
	}

	public String getFail_path() {
		return fail_path;
	}

	public void setFail_path(String fail_path) {
		this.fail_path = fail_path;
	}

	public String getException_path() {
		return exception_path;
	}

	public void setException_path(String exception_path) {
		this.exception_path = exception_path;
	}

	public String getTr_mode() {
		return tr_mode;
	}

	public void setTr_mode(String tr_mode) {
		this.tr_mode = tr_mode;
	}

	public List<TestSetLines> getTestSetLinesDatalist() {
		return testSetLinesDatalist;
	}

	public void setTestSetLinesDatalist(List<TestSetLines> testSetLinesDatalist) {
		this.testSetLinesDatalist = testSetLinesDatalist;
	}

	public String getPdfGenerationEnabled() {
		return pdfGenerationEnabled;
	}

	public void setPdfGenerationEnabled(String pdfGenerationEnabled) {
		this.pdfGenerationEnabled = pdfGenerationEnabled;
	}

}
