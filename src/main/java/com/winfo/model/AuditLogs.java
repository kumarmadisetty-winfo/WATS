package com.winfo.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "WIN_AUDIT_LOGS")
@Data
public class AuditLogs {
	@Id
	@Column(name = "ID")
	private int id;
	@Column(name="TIME")
	private Timestamp date;
	
}
