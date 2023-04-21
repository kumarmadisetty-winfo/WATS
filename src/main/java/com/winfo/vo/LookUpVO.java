package com.winfo.vo;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.winfo.model.LookUp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
