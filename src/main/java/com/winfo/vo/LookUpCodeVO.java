package com.winfo.vo;

import java.util.Date;

import com.winfo.model.LookUpCode;

public class LookUpCodeVO {

	private Integer lookUpCodeId;
	private Integer lookUpId;
	private String lookUpName;
	private String lookUpCode;
	private String targetCode;
	private String meaning;
	private String description;
	private Date effectiveFrom;
	private Date effectiveTo;
	private String createdBy;
	private String lastUpdatedBy;
	private Date creationDate;
	private Date updateDate;
	private String dataValidation;
	private String uniqueMendatory;
	private String processCode;
	private String moduleCode;
	
	public LookUpCodeVO() {};
	
	public LookUpCodeVO(LookUpCode lookUpCode) {
		this.lookUpCodeId = lookUpCode.getLookUpCodeId();
		this.lookUpId = lookUpCode.getLookUpId();
		this.lookUpName = lookUpCode.getLookUpName();
		this.lookUpCode = lookUpCode.getLookUpCode();
		this.meaning = lookUpCode.getMeaning();
		this.description = lookUpCode.getDescription();
		this.effectiveFrom = lookUpCode.getEffectiveFrom();
		this.effectiveTo = lookUpCode.getEffectiveTo();
		this.createdBy = lookUpCode.getCreatedBy();
		this.lastUpdatedBy = lookUpCode.getLastUpdatedBy();
		this.creationDate = lookUpCode.getCreationDate();
		this.updateDate = lookUpCode.getUpdateDate();
		this.processCode = lookUpCode.getProcessCode();
		this.moduleCode = lookUpCode.getModuleCode();
	}

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

	public String getDataValidation() {
		return dataValidation;
	}

	public void setDataValidation(String dataValidation) {
		this.dataValidation = dataValidation;
	}

	public String getUniqueMendatory() {
		return uniqueMendatory;
	}

	public void setUniqueMendatory(String uniqueMendatory) {
		this.uniqueMendatory = uniqueMendatory;
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


}
