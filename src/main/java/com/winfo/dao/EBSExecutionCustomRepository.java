package com.winfo.dao;

import org.springframework.stereotype.Repository;


@Repository
public interface EBSExecutionCustomRepository{
	String findByConfigurationId(int testrunId,String column);
	String findByTestRunScriptId(int testRunScriptParamId,String inputParamName);
	 String findByTestRunScriptIdInputParam(int testRunScriptParamId,String inputParamName);
}