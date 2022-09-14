package com.winfo.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TestSetAttributePK implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Column(name = "TEST_SET_ID")
	private Integer testSetId;

	@Column(name = "ATTRIBUTE_NAME")
	private String attributeName;

	public Integer getTestSetId() {
		return testSetId;
	}

	public void setTestSetId(Integer testSetId) {
		this.testSetId = testSetId;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	
}