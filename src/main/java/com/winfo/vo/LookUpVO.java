package com.winfo.vo;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.winfo.model.LookUp;

public class LookUpVO {

	@JsonProperty("lookup_id")
	private Integer lookup_id;
	@JsonProperty("last_updated_by")
	private String last_updated_by;
	@JsonProperty("lookup_name")
	private String lookup_name;
	@JsonProperty("mapOfData")
	private Map<String, LookUpCodeVO> mapOfData;

	@JsonProperty("lookup_desc")
	private String lookup_desc;
	@JsonProperty("creation_date")
	private Date creation_date;
	@JsonProperty("created_by")
	private String created_by;
	@JsonProperty("update_date")
	private Date update_date;
	
	public LookUpVO() {};
	
	public LookUpVO(LookUp lookUp, Map<String, LookUpCodeVO> mapOfData) {
		this.lookup_id = lookUp.getLookUpId();
		this.lookup_name = lookUp.getLookUpName();
		this.lookup_desc = lookUp.getLookUpName();
		this.created_by = lookUp.getCreatedBy();
		this.last_updated_by = lookUp.getLastUpdatedBy();
		this.creation_date = lookUp.getCreationDate();
		this.update_date = lookUp.getUpdatedDate();
		this.mapOfData = mapOfData;
		
	}
	
	public Integer getLookup_id() {
		return lookup_id;
	}

	public void setLookup_id(Integer lookup_id) {
		this.lookup_id = lookup_id;
	}

	public String getLookup_name() {
		return lookup_name;
	}

	public void setLookup_name(String lookup_name) {
		this.lookup_name = lookup_name;
	}

	public String getLookup_desc() {
		return lookup_desc;
	}

	public void setLookup_desc(String lookup_desc) {
		this.lookup_desc = lookup_desc;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public String getLast_updated_by() {
		return last_updated_by;
	}

	public void setLast_updated_by(String last_updated_by) {
		this.last_updated_by = last_updated_by;
	}

	public Date getCreation_date() {
		return creation_date;
	}

	public void setCreation_date(Date creation_date) {
		this.creation_date = creation_date;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	public Map<String, LookUpCodeVO> getMapOfData() {
		return mapOfData;
	}

	public void setMapOfData(Map<String, LookUpCodeVO> mapOfData) {
		this.mapOfData = mapOfData;
	}

}
