package com.winfo.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "WIN_TA_TEST_SET_ATTRIBUTE")
@Data
public class TestSetAttribute {

	@EmbeddedId
	private TestSetAttributePK id;

	@Column(name = "ATTRIBUTE_VALUE")
	private String attributeValue;

	@Column(name = "DATATYPE")
	private String datatype;

	@Column(name = "DATATYPE_FORMAT")
	private String datatypeFormat;

	@Column(name = "CREATED_DATE")
	private String createdDate;

	@Column(name = "UPDATED_DATE")
	private String updatedDate;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

}