package com.winfo.model;

import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;

@Entity
@Table(name = "WIN_TA_SCRIPT_MASTER")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "scriptId")
@Data
public class ScriptMaster {

	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "master_generator")
	@SequenceGenerator(name = "master_generator", sequenceName = "WIN_TA_SCRIPT_MASTER_SEQ", allocationSize = 1)
	@Id
	@Column(name = "SCRIPT_ID")
	private Integer scriptId;

	@Column(name = "SCRIPT_NUMBER")
	private String scriptNumber;

	@Column(name = "PROCESS_AREA")
	private String processArea;

	@Column(name = "SUB_PROCESS_AREA")
	private String subProcessArea;

	@Column(name = "MODULE")
	private String module;

	@Column(name = "ROLE")
	private String role;

	@Column(name = "END2END_SCENARIO")
	private String end2endScenario;

	@Column(name = "SCENARIO_NAME")
	private String scenarioName;

	@Column(name = "SCENARIO_DESCRIPTION")
	private String scenarioDescription;

	@Column(name = "EXPECTED_RESULT")
	private String expectedResult;

	@Column(name = "SELENIUM_TEST_SCRIPT_NAME")
	private String seleniumTestScriptName;

	@Column(name = "SELENIUM_TEST_METHOD")
	private String seleniumTestMethod;

	@Column(name = "DEPENDENCY")
	private Integer dependency;

	@Column(name = "PRODUCT_VERSION")
	private String productVersion;

	@Column(name = "STANDARD_CUSTOM")
	private String standardCustom;

	@Column(name = "TEST_SCRIPT_STATUS")
	private String testScriptStatus;

	@Column(name = "AUTHOR")
	private String author;
	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATION_DATE")
	private Date creationDate;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Column(name = "UPDATE_DATE")
	private Date updateDate;

	@Column(name = "CUSTOMER_ID")
	private Integer customerId;

	@Column(name = "CUSTOMISATION_REFERENCE")
	private String customisationReference;

	@Column(name = "ATTRIBUTE2")
	private String attribute2;

	@Column(name = "ATTRIBUTE3")
	private String attribute3;

	@Column(name = "ATTRIBUTE4")
	private String attribute4;

	@Column(name = "ATTRIBUTE5")
	private String attribute5;

	@Column(name = "ATTRIBUTE6")
	private String attribute6;

	@Column(name = "ATTRIBUTE7")
	private String attribute7;

	@Column(name = "ATTRIBUTE8")
	private String attribute8;

	@Column(name = "ATTRIBUTE9")
	private String attribute9;

	@Column(name = "ATTRIBUTE10")
	private String attribute10;

	@Column(name = "PRIORITY")
	private Integer priority;

	@Column(name = "DEPENDENT_SCRIPT_NUM")
	private String dependentScriptNum;

	@Column(name = "APPR_FOR_MIGRATION")
	private String apprForMigration;

	@Column(name = "PLUGIN_FLAG")
	private String pluginFlag;

	@Column(name = "TARGET_APPLICATION")
	private String targetApplication;

	@OneToMany(cascade = CascadeType.MERGE, mappedBy = "scriptMaster", fetch = FetchType.LAZY)
	private List<ScriptMetaData> scriptMetaDatalist;

	@OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "DEPENDENCY", insertable = false, updatable = false)
	private ScriptMaster parent;

	public void addMetadata(ScriptMetaData metadata) {
		scriptMetaDatalist.add(metadata);
		metadata.setScriptMaster(this);
	}

}
