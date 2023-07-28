package com.winfo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.winfo.model.TestSetLine;

public interface TestSetLinesRepository extends JpaRepository<TestSetLine, Integer>{
	@Query("select tl from TestSetLine tl where tl.testRun.testRunId = :testSetId and tl.seqNum = :seqNum")
	TestSetLine findBySeqNum(@Param("testSetId") Integer testSetId ,@Param("seqNum") Integer seqNum);
	
	@Query("select count(*) from TestSetLine where test_set_id=:testSetId and status=:scriptStatus")
	int getScriptCountOfTestRun(String testSetId ,String scriptStatus);
	
	long countByScriptId(Integer scriptId);
	
	List<TestSetLine> findByScriptId(Integer scriptId);
}
