package com.winfo.model;



import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;

import lombok.Data;



@Entity
@Table(name = "WIN_TA_TEST_SET_SCRIPT_PARAM")
@Data
public class TestSetScriptParam {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "testRunScriptParam_generator")
	@SequenceGenerator(name = "testRunScriptParam_generator", sequenceName = "WIN_TA_PARAM_ID_SEQ", allocationSize = 1)
	@Column(name = "TEST_SCRIPT_PARAM_ID", nullable = false,unique = true)
	private Integer testRunScriptParamId;

	@Column(name = "SCRIPT_ID")
	private Integer scriptId;

	@Column(name = "SCRIPT_NUMBER")
	@NotEmpty(message="Script Number can not be null")
	private String scriptNumber;

	@Column(name = "LINE_ERROR_MESSAGE")
	private String lineErrorMessage;

	@Column(name = "LINE_NUMBER")
	private Integer lineNumber;

	@Column(name = "INPUT_PARAMETER")
	private String inputParameter;

	@Column(name = "ACTION")
	private String action;

	@Column(name = "XPATH_LOCATION")
	private String xpathLocation;

	@Column(name = "XPATH_LOCATION1")
	private String xpathLocation1;
	
	@Column(name = "XPATH_LOCATION_UPDATED_BY")
	private String XpathLocationUpdatedBy;
    
	@Column(name = "XPATH_LOCATION_UPDATED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date XpathLocationUpdatedDate;
	
	@Column(name = "TEST_RUN_PARAM_NAME")
	private String testRunParamName;

	@Column(name = "TEST_RUN_PARAM_DESC")
	private String testRunParamDesc;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@Column(name = "CREATION_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	@Column(name = "UPDATE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;

	@Column(name = "INPUT_VALUE")
	private String inputValue;

	@Column(name = "METADATA_ID")
	private Integer metadataId;

	@Column(name = "HINT")
	private String hint;

	@Column(name = "FIELD_TYPE")
	private String fieldType;

	@Column(name = "DATATYPES")
	private String dataTypes;

	@Column(name = "LINE_EXECUTION_STATUS")
	private String lineExecutionStatus;

	@Column(name = "UNIQUE_MANDATORY")
	private String uniqueMandatory;

	@Column(name = "VALIDATION_TYPE")
	private String validationType;

	@Column(name = "VALIDATION_NAME")
	private String validationName;
	
	@Column(name = "VALIDATION_STATUS")
	private String validationStatus;

	@Column(name = "SCREENSHOT")
    private byte[] screenshot;
	
	@Column(name = "VALIDATION_ERROR_MESSAGE")
	private String validationErrorMessage;
	
	@ManyToOne( fetch = FetchType.EAGER)
	@JoinColumn(name = "TEST_SET_LINE_ID" ,nullable = false)
	private TestSetLine testSetLine;
	
	@Column(name = "START_TIME")
    private Date startTime;
	
	@Column(name = "END_TIME")
	private Date endTime;


	
}
