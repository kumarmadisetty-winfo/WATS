package com.winfo.vo;

import java.util.Date;

import com.winfo.model.LookUpCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
	private String targetApplication;

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
		this.targetApplication = lookUpCode.getTargetApplication();
	}

}
