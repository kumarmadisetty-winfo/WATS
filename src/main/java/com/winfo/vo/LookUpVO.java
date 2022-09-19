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
	
	public LookUpVO() {	
	}
	
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
	
	public Integer getLookupId() {
		return lookupId;
	}

	public void setLookupId(Integer lookupId) {
		this.lookupId = lookupId;
	}

	public String getLookupName() {
		return lookupName;
	}

	public void setLookupName(String lookupName) {
		this.lookupName = lookupName;
	}

	public String getLookupDesc() {
		return lookupDesc;
	}

	public void setLookupDesc(String lookupDesc) {
		this.lookupDesc = lookupDesc;
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

	public Map<String, LookUpCodeVO> getMapOfData() {
		return mapOfData;
	}

	public void setMapOfData(Map<String, LookUpCodeVO> mapOfData) {
		this.mapOfData = mapOfData;
	}

}
