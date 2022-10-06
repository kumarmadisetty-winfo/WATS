package com.winfo.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "WIN_TA_TEST_SET_ATTRIBUTE")
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

	public TestSetAttributePK getId() {
		return id;
	}

	public void setId(TestSetAttributePK id) {
		this.id = id;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public String getDatatypeFormat() {
		return datatypeFormat;
	}

	public void setDatatypeFormat(String datatypeFormat) {
		this.datatypeFormat = datatypeFormat;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	

}