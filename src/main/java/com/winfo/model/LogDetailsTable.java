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
@Table(name = "LOG_DETAILS")
@Data
public class LogDetailsTable {

	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "logDetails_generator")
	@SequenceGenerator(name = "logDetails_generator", sequenceName = "LOG_DETAILS_ID_SEQ", allocationSize = 1)
	@Id
	@Column(name = "LOG_ID", nullable = false)
	private Integer logId;

	@Column(name = "LOG_LEVEL", nullable = false)
	private String logLevel;

	@Column(name = "LOG_TABLE", nullable = false)
	private String logTable;

	@Column(name = "LOG_ACTION", nullable = false)
	private String logAction;

	@Column(name = "LOG_TIME", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date logTime;

	@Column(name = "EXECUTED_BY", nullable = false)
	private String executedBy;

	@Column(name = "LOG_DESCRIPTION", nullable = false)
	private String logDescription;

}
