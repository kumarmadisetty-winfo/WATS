package com.winfo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "code_lines")
@Data
public class CodeLines {
	@Column(name = "code_line_id")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long codeLineId;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "action_id")
	private Actions actionId;
	@Column(name = "robot_line")
	private String robotLine;
	@Column(name = "param_values")
	private String paramValues;

}
