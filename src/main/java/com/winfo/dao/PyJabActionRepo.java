package com.winfo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.winfo.model.PyJabActions;

public interface PyJabActionRepo extends JpaRepository<PyJabActions, Long> {

	PyJabActions findByActionName(String actionName);

}
