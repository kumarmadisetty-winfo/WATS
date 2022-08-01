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

@Entity
@Table(name = "WIN_TA_LOOKUP_CODES")
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

	public Integer getLookUpCodeId() {
		return lookUpCodeId;
	}

	public void setLookUpCodeId(Integer lookUpCodeId) {
		this.lookUpCodeId = lookUpCodeId;
	}
	
	public Integer getLookUpId() {
		return lookUpId;
	}

	public void setLookUpId(Integer lookUpId) {
		this.lookUpId = lookUpId;
	}

	public String getLookUpName() {
		return lookUpName;
	}

	public void setLookUpName(String lookUpName) {
		this.lookUpName = lookUpName;
	}

	public String getLookUpCode() {
		return lookUpCode;
	}

	public void setLookUpCode(String lookUpCode) {
		this.lookUpCode = lookUpCode;
	}

	public String getTargetCode() {
		return targetCode;
	}

	public void setTargetCode(String targetCode) {
		this.targetCode = targetCode;
	}

	public String getMeaning() {
		return meaning;
	}

	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getEffectiveFrom() {
		return effectiveFrom;
	}

	public void setEffectiveFrom(Date effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}

	public Date getEffectiveTo() {
		return effectiveTo;
	}

	public void setEffectiveTo(Date effectiveTo) {
		this.effectiveTo = effectiveTo;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getProcessCode() {
		return processCode;
	}

	public void setProcessCode(String processCode) {
		this.processCode = processCode;
	}

	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public String getTargetApplication() {
		return targetApplication;
	}

	public void setTargetApplication(String targetApplication) {
		this.targetApplication = targetApplication;
	}

}
