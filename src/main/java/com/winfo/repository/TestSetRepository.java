package com.winfo.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.winfo.model.TestSet;

@Repository
public interface TestSetRepository extends JpaRepository<TestSet, Integer>{

	TestSet findByTestRunName(String testRunName);
	
	List<TestSet> findByTestRunNameIn(List<String> testRunNames);
	
	TestSet findByTestRunId(Integer testRunId);
	
	long countByTestRunId(Integer testRunId);
	
	@Modifying
	@Transactional
	@Query("UPDATE TestSet SET lastExecutBy =:date, lastUpdatedBy =:lastUpdatedBy, updateDate =:date, testRunMode = 'ACTIVE' WHERE testRunId =:testSetId")
	int updateTestRunExecution(String lastUpdatedBy,int testSetId, Date date);

	@Query("select testRunName from TestSet")
	List<String> getTestRun();

	
	@Query("select testRunId from TestSet where testRunName=:testsetName")
	List<?> findTestRunIdByTestRunName(String testsetName);
	
	@Modifying
	@Transactional
	@Query("Update TestSet set pdfGenerationEnabled=:enabled where testRunId=:testSetId")
	int updatePdfGenerationEnabledStatus(String enabled,int testSetId);
	
	
	
	
}
