package com.winfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.winfo.model.TestSet;

@Repository
public interface TestSetRepository extends JpaRepository<TestSet, Integer>{

	TestSet findByTestRunName(String testRunName);
	
	TestSet findByTestRunId(Integer testRunId);

}
