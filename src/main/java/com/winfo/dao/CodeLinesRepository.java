package com.winfo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.winfo.dao.EBSExecutionCustomRepository;
import com.winfo.model.Actions;
import com.winfo.model.CodeLines;

@Repository
public interface CodeLinesRepository extends JpaRepository<CodeLines, Long>,EBSExecutionCustomRepository{
	 List<CodeLines>findAll();
	// Actions findByActionName(String actionName);
	// String findByConfigurationId(int configId,String column);
	// List<Actions>findByActionTypeOrderById(String actionType);
	 List<CodeLines>findByActionIdInOrderByCodeLineId(List<Actions>listOfActions);
	 String findByConfigurationId(int testrunId,String column);
		String findByTestRunScriptId(int testRunScriptParamId,String inputParamName);
		 String findByTestRunScriptIdInputParam(int testRunScriptParamId,String inputParamName);
		 List<CodeLines> findByActionIdOrderByCodeLineId(Actions action);
		 List<CodeLines> findByActionIdInOrderByActionIdAscCodeLineIdAsc(List<Actions>listOfActions);
}

