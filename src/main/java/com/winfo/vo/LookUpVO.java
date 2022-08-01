package com.winfo.vo;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.winfo.model.LookUp;

public class LookUpVO {

	@JsonProperty("lookupId")
	private Integer lookupId;
	@JsonProperty("lastUpdatedBy")
	private String lastUpdatedBy;
	@JsonProperty("lookupName")
	private String lookupName;
	@JsonProperty("mapOfData")
	private Map<String, LookUpCodeVO> mapOfData;

	@JsonProperty("lookupDesc")
	private String lookupDesc;
	@JsonProperty("creationDate")
	private Date creationDate;
	@JsonProperty("createdBy")
	private String createdBy;
	@JsonProperty("updateDate")
	private Date updateDate;
	
	public LookUpVO() {};
	
	public LookUpVO(LookUp lookUp, Map<String, LookUpCodeVO> mapOfData) {
		this.lookupId = lookUp.getLookUpId();
		this.lookupName = lookUp.getLookUpName();
		this.lookupDesc = lookUp.getLookUpName();
		this.createdBy = lookUp.getCreatedBy();
		this.lastUpdatedBy = lookUp.getLastUpdatedBy();
		this.creationDate = lookUp.getCreationDate();
		this.updateDate = lookUp.getUpdatedDate();
		this.mapOfData = mapOfData;
		
	}
	
	public Integer getLookup_id() {
		return lookupId;
	}

	public void setLookup_id(Integer lookup_id) {
		this.lookupId = lookup_id;
	}

	public String getLookup_name() {
		return lookupName;
	}

	public void setLookup_name(String lookupName) {
		this.lookupName = lookupName;
	}

	public String getLookup_desc() {
		return lookupDesc;
	}

	public void setLookup_desc(String lookupDesc) {
		this.lookupDesc = lookupDesc;
	}

	public String getCreated_by() {
		return createdBy;
	}

	public void setCreated_by(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLast_updated_by() {
		return lastUpdatedBy;
	}

	public void setLast_updated_by(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getCreation_date() {
		return creationDate;
	}

	public void setCreation_date(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getUpdate_date() {
		return updateDate;
	}

	public void setUpdate_date(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Map<String, LookUpCodeVO> getMapOfData() {
		return mapOfData;
	}

	public void setMapOfData(Map<String, LookUpCodeVO> mapOfData) {
		this.mapOfData = mapOfData;
	}

}
