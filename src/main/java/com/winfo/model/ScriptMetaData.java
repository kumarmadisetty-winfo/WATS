package com.winfo.model;

import java.sql.Date;

import javax.persistence.CascadeType;
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

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;

@Entity
@Table(name = "WIN_TA_SCRIPT_METADATA")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "scriptMetaDataId")
@Data
public class ScriptMetaData {

	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "metadata_generator")
	@SequenceGenerator(name = "metadata_generator", sequenceName = "WIN_TA_SCRIPT_METADATA_SEQ", allocationSize = 1)
	@Id
	@Column(name = "SCRIPT_META_DATA_ID")
	private Integer scriptMetaDataId;

	@Column(name = "SCRIPT_NUMBER")
	private String scriptNumber;

	@Column(name = "LINE_NUMBER")
	private Integer lineNumber;

	@Column(name = "INPUT_PARAMETER")
	private String inputParameter;

	@Column(name = "ACTION")
	private String action;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATION_DATE")
	private Date creationDate;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Column(name = "UPDATE_DATE")
	private Date updateDate;

	@Column(name = "STEP_DESC")
	private String stepDesc;

	@Column(name = "FIELD_TYPE")
	private String fieldType;

	@Column(name = "HINT")
	private String hint;

	@Column(name = "DATATYPES")
	private String datatypes;

	@Column(name = "UNIQUE_MANDATORY")
	private String uniqueMandatory;

	@Column(name = "VALIDATION_TYPE")
	private String validationType;

	@Column(name = "VALIDATION_NAME")
	private String validationName;

	@Column(name = "METADATA_INPUT_VALUE")
	private String metadataInputvalue;

	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "SCRIPT_ID")
	private ScriptMaster scriptMaster;

}
