package com.winfo.vo;

import java.util.Date;

import com.winfo.model.LookUpCode;

public class LookUpCodeVO {

	private Integer LOOKUP_CODES_ID;
	private Integer LOOKUP_ID;
	private String LOOKUP_NAME;
	private String LOOKUP_CODE;
	private String TARGET_CODE;
	private String MEANING;
	private String DESCRIPTION;
	private Date EFFECTIVE_FROM;
	private Date EFFECTIVE_TO;
	private String CREATED_BY;
	private String LAST_UPDATED_BY;
	private Date CREATION_DATE;
	private Date UPDATE_DATE;
	private String DATA_VALIDATION;
	private String UNIQUE_MANDATORY;
	private String PROCESS_CODE;
	private String MODULE_CODE;
	
	public LookUpCodeVO() {};
	
	public LookUpCodeVO(LookUpCode lookUpCode) {
		this.LOOKUP_CODES_ID = lookUpCode.getLookUpCodeId();
		this.LOOKUP_ID = lookUpCode.getLookUpId();
		this.LOOKUP_NAME = lookUpCode.getLookUpName();
		this.LOOKUP_CODE = lookUpCode.getLookUpCode();
		this.MEANING = lookUpCode.getMeaning();
		this.DESCRIPTION = lookUpCode.getDescription();
		this.EFFECTIVE_FROM = lookUpCode.getEffectiveFrom();
		this.EFFECTIVE_TO = lookUpCode.getEffectiveTo();
		this.CREATED_BY = lookUpCode.getCreatedBy();
		this.LAST_UPDATED_BY = lookUpCode.getLastUpdatedBy();
		this.CREATION_DATE = lookUpCode.getCreationDate();
		this.UPDATE_DATE = lookUpCode.getUpdateDate();
		this.PROCESS_CODE = lookUpCode.getProcessCode();
		this.MODULE_CODE = lookUpCode.getModuleCode();
	}
	
	public Integer getLOOKUP_CODES_ID() {
		return LOOKUP_CODES_ID;
	}
	public void setLOOKUP_CODES_ID(Integer lOOKUP_CODES_ID) {
		LOOKUP_CODES_ID = lOOKUP_CODES_ID;
	}
	public Integer getLOOKUP_ID() {
		return LOOKUP_ID;
	}
	public void setLOOKUP_ID(Integer lOOKUP_ID) {
		LOOKUP_ID = lOOKUP_ID;
	}
	public String getLOOKUP_NAME() {
		return LOOKUP_NAME;
	}
	public void setLOOKUP_NAME(String lOOKUP_NAME) {
		LOOKUP_NAME = lOOKUP_NAME;
	}
	public String getLOOKUP_CODE() {
		return LOOKUP_CODE;
	}
	public void setLOOKUP_CODE(String lOOKUP_CODE) {
		LOOKUP_CODE = lOOKUP_CODE;
	}
	public String getTARGET_CODE() {
		return TARGET_CODE;
	}
	public void setTARGET_CODE(String tARGET_CODE) {
		TARGET_CODE = tARGET_CODE;
	}
	public String getMEANING() {
		return MEANING;
	}
	public void setMEANING(String mEANING) {
		MEANING = mEANING;
	}
	public String getDESCRIPTION() {
		return DESCRIPTION;
	}
	public void setDESCRIPTION(String dESCRIPTION) {
		DESCRIPTION = dESCRIPTION;
	}
	public Date getEFFECTIVE_FROM() {
		return EFFECTIVE_FROM;
	}
	public void setEFFECTIVE_FROM(Date eFFECTIVE_FROM) {
		EFFECTIVE_FROM = eFFECTIVE_FROM;
	}
	public Date getEFFECTIVE_TO() {
		return EFFECTIVE_TO;
	}
	public void setEFFECTIVE_TO(Date eFFECTIVE_TO) {
		EFFECTIVE_TO = eFFECTIVE_TO;
	}
	public String getCREATED_BY() {
		return CREATED_BY;
	}
	public void setCREATED_BY(String cREATED_BY) {
		CREATED_BY = cREATED_BY;
	}
	public String getLAST_UPDATED_BY() {
		return LAST_UPDATED_BY;
	}
	public void setLAST_UPDATED_BY(String lAST_UPDATED_BY) {
		LAST_UPDATED_BY = lAST_UPDATED_BY;
	}
	public Date getCREATION_DATE() {
		return CREATION_DATE;
	}
	public void setCREATION_DATE(Date cREATION_DATE) {
		CREATION_DATE = cREATION_DATE;
	}
	public Date getUPDATE_DATE() {
		return UPDATE_DATE;
	}
	public void setUPDATE_DATE(Date uPDATE_DATE) {
		UPDATE_DATE = uPDATE_DATE;
	}
	public String getDATA_VALIDATION() {
		return DATA_VALIDATION;
	}
	public void setDATA_VALIDATION(String dATA_VALIDATION) {
		DATA_VALIDATION = dATA_VALIDATION;
	}
	public String getUNIQUE_MANDATORY() {
		return UNIQUE_MANDATORY;
	}
	public void setUNIQUE_MANDATORY(String uNIQUE_MANDATORY) {
		UNIQUE_MANDATORY = uNIQUE_MANDATORY;
	}
	public String getPROCESS_CODE() {
		return PROCESS_CODE;
	}
	public void setPROCESS_CODE(String pROCESS_CODE) {
		PROCESS_CODE = pROCESS_CODE;
	}
	public String getMODULE_CODE() {
		return MODULE_CODE;
	}
	public void setMODULE_CODE(String mODULE_CODE) {
		MODULE_CODE = mODULE_CODE;
	}
	

}
