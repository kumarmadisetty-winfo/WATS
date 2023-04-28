package com.winfo.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Embeddable
@Data
public class ExecuteStatusPK implements Serializable {

	private static final long serialVersionUID = 23L;

	@Column(name = "EXECUTED_BY")
	private String executedBy;

	@Column(name = "TEST_RUN_ID")
	private Integer testSetId;

}
