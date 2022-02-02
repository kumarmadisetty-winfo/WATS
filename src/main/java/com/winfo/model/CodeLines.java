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

@Entity
@Table(name = "code_lines")
public class CodeLines {
private long codeLineId;
private Actions actionId;
private String robot_line;
private String param_values;

@Column(name="code_line_id")
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
public long getCodeLineId() {
	return codeLineId;
}
public void setCodeLineId(long codeLineId) {
	this.codeLineId = codeLineId;
}


@Column(name = "robot_line")
public String getRobot_line() {
	return robot_line;
}

//@ManyToOne(fetch = FetchType.LAZY)
//@JoinColumn(name = "action_id")
//public Actions getAction_id() {
//	return actionId;
//}
//public void setAction_id(Actions action_id) {
//	this.actionId = action_id;
//}
public void setRobot_line(String robot_line) {
	this.robot_line = robot_line;
}
@Column(name = "param_values")
public String getParam_values() {
	return param_values;
}
public void setParam_values(String param_values) {
	this.param_values = param_values;
}
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "action_id")
public Actions getActionId() {
	return actionId;
}
public void setActionId(Actions actionId) {
	this.actionId = actionId;
}

}
