package com.winfo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.winfo.model.Actions;
@Repository
public interface ActionsRepository extends JpaRepository<Actions, Long>{
	 List<Actions>findAll();
	 Actions findByActionName(String actionName);
	 List<Actions>findByActionTypeOrderById(String actionType);
	 
}

