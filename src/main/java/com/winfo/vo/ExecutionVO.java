package com.winfo.vo;

public class ExecutionVO {
	
	private String testCaseNo;
	private String moduleName;
	private String automationMethod;
	private String testCasePackage;
	private String priority;
	private String flag;
	private String testsCommandsTargets001;
	
	private String scriptId;
	private String actions;
	private String xPath;
	private String values;
	
	public String getScriptId() {
		return scriptId;
	}
	public void setScriptId(String scriptId) {
		this.scriptId = scriptId;
	}
	public String getActions() {
		return actions;
	}
	public void setActions(String actions) {
		this.actions = actions;
	}
	public String getXpath() {
		return xPath;
	}
	public void setXpath(String xPath) {
		this.xPath = xPath;
	}
	public String getValues() {
		return values;
	}
	public void setValues(String values) {
		this.values = values;
	}
	public String getTestsCommandsTargets001() {
		return testsCommandsTargets001;
	}
	public void setTestsCommandsTargets001(String testsCommandsTargets001) {
		this.testsCommandsTargets001 = testsCommandsTargets001;
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
