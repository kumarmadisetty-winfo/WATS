package com.winfo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import lombok.Data;


@Entity
@Table(name = "WIN_TA_TEST_SET_LINES")
@Data
public class TestSetLine {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "testRunScript_generator")
	@SequenceGenerator(name = "testRunScript_generator", sequenceName = "WIN_TA_TEST_SET_LINE_SEQ", allocationSize = 1)
	@Column(name = "TEST_SET_LINE_ID", nullable = false)
	private Integer testRunScriptId;

	@Column(name ="SCRIPT_ID")

	private Integer scriptId;

	@Column(name = "SCRIPT_NUMBER")
	@NotEmpty(message="Script Number can not null")
	private String scriptNumber;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "ENABLED")
	private String enabled;

	@Column(name = "SEQ_NUM")
	private Integer seqNum;

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

	@Column(name = "TEST_SET_LINE_SCRIPT_PATH")
	private String testRunScriptPath;

	@Column(name = "EXECUTED_BY")
	private String executedBy;

	@Column(name = "EXECUTION_START_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date executionStartTime;

	@Column(name = "EXECUTION_END_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date executionEndTime;

	@Column(name = "ISSUE_KEY")
	private String issueKey;
	
	@Column(name = "RUN_COUNT")
	private Integer runCount;
	
	@Column(name = "SCRIPT_UPDATED")
	private String scriptUpadated;
	
	@Column(name = "DEPENDENCY_TR")
	private Integer dependencyTr;
	
	@Column(name = "VALIDATION_STATUS")
	private String validationStatus;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TEST_SET_ID" ,nullable = false)
	private TestSet testRun;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "testSetLine", fetch = FetchType.LAZY)
	private List<TestSetScriptParam> testRunScriptParam = new ArrayList<>();
	
	
	public void addTestScriptParam(TestSetScriptParam scriptParam) {
		testRunScriptParam.add(scriptParam);
		scriptParam.setTestSetLine(this);
	}


	
}
