package com.winfo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Entity
@Table(name = "WIN_TA_TEST_SET")
@Data
public class TestSet {
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "testRun_generator")
	@SequenceGenerator(name = "testRun_generator", sequenceName = "WIN_TA_TEST_SET_ID_SEQ", allocationSize = 1)
	@Id
	@Column(name = "TEST_SET_ID", nullable = false)
	private Integer testRunId;

	@NotEmpty(message = "Test_SET_Name can not be null")
	@Column(name = "TEST_SET_NAME")
	private String testRunName;

	@Column(name = "TEST_SET_DESC")
	private String testRunDesc;

	@Column(name = "TEST_SET_COMMENTS")
	private String testRunComments;

	@Column(name = "ENABLED")
	private String enabled;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "EFFECTIVE_FROM")
	@Temporal(TemporalType.DATE)
	private Date effectiveFrom;

	@Column(name = "EFFECTIVE_TO")
	@Temporal(TemporalType.DATE)
	private Date effectiveTo;

	@Column(name = "PROJECT_ID")
	private Integer projectId;

	@Column(name = "CONFIGURATION_ID")
	private Integer configurationId;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@Column(name = "CREATION_DATE")
	@Temporal(TemporalType.DATE)
	private Date creationDate;

	@Column(name = "UPDATE_DATE")
	@Temporal(TemporalType.DATE)
	private Date updateDate;

	@Column(name = "LAST_EXECUTED_BY")
	@Temporal(TemporalType.DATE)
	private Date lastExecutBy;

	@Column(name = "TS_COMPLETE_FLAG")
	private String tsCompleteFlag;

	@Column(name = "PASS_PATH")
	private String passPath;

	@Column(name = "FAIL_PATH")
	private String failPath;

	@Column(name = "EXCEPTION_PATH")
	private String exceptionPath;

	@Column(name = "TR_MODE")
	private String testRunMode;

	@Column(name = "START_TIME")
	private Date startTime;

	@Column(name = "END_TIME")
	private Date endTime;

	@Column(name = "PDF_GENERATION")
	private String pdfGenerationEnabled;
	
	@Column(name = "CPY_INCR_FLAG")
	private String copyIncreamentFlag;

	@Column(name = "REFERENCE_TEST_RUN")
	private Integer referenceTestRunId;
	
	@Column(name = "TEMPLATE")
	private String template;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "testRun")
	private List<TestSetLine> testRunScriptDatalist = new ArrayList<>();

	public void addTestRunScriptData(TestSetLine testRunScript) {
		testRunScriptDatalist.add(testRunScript);
		testRunScript.setTestRun(this);
	}
}
