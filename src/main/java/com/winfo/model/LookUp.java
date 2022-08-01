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
@Table(name = "WIN_TA_LOOKUPS")
public class LookUp {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lookUpGenerator")
	@SequenceGenerator(name = "lookUpGenerator", sequenceName = "LOOKUP_ID_S", allocationSize = 1)
	@Column(name = "LOOKUP_ID", nullable = false)
	private Integer lookUpId;

	@Column(name = "LOOKUP_NAME")
	private String lookUpName;

	@Column(name = "LOOKUP_DESC")
	private String lookUpDesc;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@Column(name = "CREATION_DATE")
	@Temporal(TemporalType.DATE)
	private Date creationDate;

	@Column(name = "UPDATE_DATE")
	@Temporal(TemporalType.DATE)
	private Date updatedDate;

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

	public String getLookUpDesc() {
		return lookUpDesc;
	}

	public void setLookUpDesc(String lookUpDesc) {
		this.lookUpDesc = lookUpDesc;
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

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
}
