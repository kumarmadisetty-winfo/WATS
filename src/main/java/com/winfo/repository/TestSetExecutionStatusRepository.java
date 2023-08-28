package com.winfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.winfo.model.TestSetExecutionStatus;

public interface TestSetExecutionStatusRepository extends JpaRepository<TestSetExecutionStatus, Integer>{
	
}
