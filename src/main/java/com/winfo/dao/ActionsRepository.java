package com.winfo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.winfo.dao.EBSExecutionCustomRepository;
import com.winfo.model.Actions;
import com.winfo.model.CodeLines;
@Repository
public interface ActionsRepository extends JpaRepository<Actions, Long>{
	 List<Actions>findAll();
	 Actions findByActionName(String actionName);
	// String findByConfigurationId(int configId,String column);
	 List<Actions>findByActionTypeOrderById(String actionType);
	 
}

