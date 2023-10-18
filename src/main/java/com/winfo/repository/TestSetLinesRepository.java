package com.winfo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.winfo.model.TestSetLine;

public interface TestSetLinesRepository extends JpaRepository<TestSetLine, Integer>{
	@Query("select tl from TestSetLine tl where tl.testRun.testRunId = :testSetId and tl.seqNum = :seqNum")
	TestSetLine findBySeqNum(@Param("testSetId") Integer testSetId ,@Param("seqNum") Integer seqNum);
	
	@Query("select count(*) from TestSetLine where test_set_id=:testSetId and UPPER(status)=UPPER(:scriptStatus)")
	int getScriptCountOfTestRun(String testSetId ,String scriptStatus);
	
	@Query("select count(*) from TestSetLine where test_set_id=:testSetId and upper(status)!='PASS' ")
	Integer checkIsTestRunPassed(String testSetId);
	
	long countByScriptId(Integer scriptId);
	
	long countByTestRunScriptId(Integer testSetLineId);
	
	TestSetLine findByTestRunScriptId(Integer testSetLineId);
	
	List<TestSetLine> findByScriptId(Integer scriptId);
	
	@Modifying
	@Transactional
	@Query("update TestSetLine set enabled='Y' where test_set_id=:testSetId")
	int updateTestRunScriptEnable(int testSetId);
	
	@Modifying
	@Transactional
	@Query("update TestSetLine set status='IN-QUEUE', executionStartTime=null, executionEndTime=null where enabled = 'Y' and upper(status)!='PASS' and testRun.testRunId=:testSetId")
	int updateStatusStartTimeEndTimeTetSetLines(int testSetId);
	
	@Modifying
	@Transactional
	@Query("UPDATE TestSetLine SET validationStatus =:validationStatus WHERE testRunScriptId=:testRunScriptId")
	int updateValidationStatus(int testRunScriptId, String validationStatus);
	
	
	@Query("select seqNum from TestSetLine where testRun=:testSetId order by seqNum desc")
	List<?> getSeqNumByTestSetId(int testSetId);
	
	
	
	
	
	
}
