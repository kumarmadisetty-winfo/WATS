package com.winfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.winfo.model.TestSet;

@Repository
public interface TestSetRepository extends JpaRepository<TestSet, Integer>{

	TestSet findByTestRunName(String testRunName);
	
	TestSet findByTestRunId(Integer testRunId);
	
	@Modifying
	@Transactional
	@Query("UPDATE TestSet SET lastExecutBy = SYSDATE, lastUpdatedBy = lastUpdatedBy, updateDate = SYSDATE, testRunMode = 'ACTIVE' WHERE testRunId =: testSetId")
	int updateTestRunExecution(String testSetId,String LastUpdatedBy);
	

}
