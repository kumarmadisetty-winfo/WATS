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

import lombok.Data;

@Entity
@Table(name = "WIN_TA_LOOKUPS")
@Data
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

}
