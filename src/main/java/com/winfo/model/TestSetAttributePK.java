package com.winfo.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Embeddable
@Data
public class TestSetAttributePK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "TEST_SET_ID")
	private Integer testSetId;

	@Column(name = "ATTRIBUTE_NAME")
	private String attributeName;

}