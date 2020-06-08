package com.winfo.vo;

public class ExecutionVO {
	
	private String testCaseNo;
	private String moduleName;
	private String automationMethod;
	private String testCasePackage;
	private String priority;
	private String flag;
	private String tests__commands__targets__001;
	
	private String Script_Id;
	private String Actions;
	private String Xpath;
	private String Values;
	
	public String getScript_Id() {
		return Script_Id;
	}
	public void setScript_Id(String script_Id) {
		Script_Id = script_Id;
	}
	public String getActions() {
		return Actions;
	}
	public void setActions(String actions) {
		Actions = actions;
	}
	public String getXpath() {
		return Xpath;
	}
	public void setXpath(String xpath) {
		Xpath = xpath;
	}
	public String getValues() {
		return Values;
	}
	public void setValues(String values) {
		Values = values;
	}
	public String getTests__commands__targets__001() {
		return tests__commands__targets__001;
	}
	public void setTests__commands__targets__001(String tests__commands__targets__001) {
		this.tests__commands__targets__001 = tests__commands__targets__001;
	}
	public String getTestcasemethod() {
		return testCasePackage;
	}
	public void setTestcasemethod(String testcasemethod) {
		this.testCasePackage = testcasemethod;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getModuleDesc() { 
		return priority;
	}
	public void setModuleDesc(String priority) {
		this.priority = priority;
	}
	public String getTestCaseNo() {
		return testCaseNo;
	}
	public void setTestCaseNo(String testCaseNo) {
		this.testCaseNo = testCaseNo;
	}
	public String getAutomationMethod() {
		return automationMethod;
	}
	public void setAutomationMethod(String automationMethod) {
		this.automationMethod = automationMethod;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
}
