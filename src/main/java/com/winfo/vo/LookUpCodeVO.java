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
	
	public Integer getLOOKUP_CODES_ID() {
		return lookUpCodeId;
	}
	public void setLOOKUP_CODES_ID(Integer lookUpCodeId) {
		this.lookUpCodeId = lookUpCodeId;
	}
	public Integer getLOOKUP_ID() {
		return lookUpId;
	}
	public void setLOOKUP_ID(Integer lookUpId) {
		this.lookUpId = lookUpId;
	}
	public String getLOOKUP_NAME() {
		return lookUpName;
	}
	public void setLOOKUP_NAME(String lookUpName) {
		this.lookUpName = lookUpName;
	}
	public String getLOOKUP_CODE() {
		return lookUpCode;
	}
	public void setLOOKUP_CODE(String lookUpCode) {
		this.lookUpCode = lookUpCode;
	}
	public String getTARGET_CODE() {
		return targetCode;
	}
	public void setTARGET_CODE(String targetCode) {
		this.targetCode = targetCode;
	}
	public String getMEANING() {
		return meaning;
	}
	public void setMEANING(String meaning) {
		this.meaning = meaning;
	}
	public String getDESCRIPTION() {
		return description;
	}
	public void setDESCRIPTION(String description) {
		this.description = description;
	}
	public Date getEFFECTIVE_FROM() {
		return effectiveFrom;
	}
	public void setEFFECTIVE_FROM(Date effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}
	public Date getEFFECTIVE_TO() {
		return effectiveTo;
	}
	public void setEFFECTIVE_TO(Date effectiveTo) {
		this.effectiveTo = effectiveTo;
	}
	public String getCREATED_BY() {
		return createdBy;
	}
	public void setCREATED_BY(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getLAST_UPDATED_BY() {
		return lastUpdatedBy;
	}
	public void setLAST_UPDATED_BY(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public Date getCREATION_DATE() {
		return creationDate;
	}
	public void setCREATION_DATE(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Date getUPDATE_DATE() {
		return updateDate;
	}
	public void setUPDATE_DATE(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getDATA_VALIDATION() {
		return dataValidation;
	}
	public void setDATA_VALIDATION(String dataValidation) {
		this.dataValidation = dataValidation;
	}
	public String getUNIQUE_MANDATORY() {
		return uniqueMendatory;
	}
	public void setUNIQUE_MANDATORY(String uniqueMendatory) {
		this.uniqueMendatory = uniqueMendatory;
	}
	public String getPROCESS_CODE() {
		return processCode;
	}
	public void setPROCESS_CODE(String processCode) {
		this.processCode = processCode;
	}
	public String getMODULE_CODE() {
		return moduleCode;
	}
	public void setMODULE_CODE(String moduleCode) {
		this.moduleCode = moduleCode;
	}
	

}
