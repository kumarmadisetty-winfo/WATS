package com.winfo.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Table(name = "WIN_TA_LOOKUP_CODES")
@Data
public class LookUpCode {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lookUpCodeGenerator")
	@SequenceGenerator(name = "lookUpCodeGenerator", sequenceName = "LOOKUP_CODES_ID_S", allocationSize = 1)
	@Column(name = "LOOKUP_CODES_ID", nullable = false)
	private Integer lookUpCodeId;

	@Column(name = "LOOKUP_ID")
	private Integer lookUpId;

	@Column(name = "LOOKUP_NAME")
	private String lookUpName;

	@Column(name = "LOOKUP_CODE")
	private String lookUpCode;

	@Column(name = "TARGET_CODE")
	private String targetCode;

	@Column(name = "MEANING")
	private String meaning;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "EFFECTIVE_FROM")
	@Temporal(TemporalType.DATE)
	private Date effectiveFrom;

	@Column(name = "EFFECTIVE_TO")
	@Temporal(TemporalType.DATE)
	private Date effectiveTo;

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

	@Column(name = "PROCESS_CODE")
	private String processCode;

	@Column(name = "MODULE_CODE")
	private String moduleCode;

	@Column(name = "TARGET_APPLICATION")
	private String targetApplication;

}
