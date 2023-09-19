
package com.winfo.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.winfo.model.TestSetScriptParam;

public interface TestSetScriptParamRepository extends JpaRepository<TestSetScriptParam, Integer>{

	@Modifying
	@Query("UPDATE TestSetScriptParam SET lineNumber = :lineNumber, inputParameter = :inputParameter,"
			+ " action = :action, testRunParamDesc = :testRunParamDesc ,validationType = :validationType,"
			+ "validationName = :validationName, dataTypes = :dataTypes, uniqueMandatory = :uniqueMandatory,"
			+ " lastUpdatedBy =:updatedBy, updateDate=sysdate "
			+ "WHERE  metadataId = :metadataId AND scriptId = :scriptId")
	Integer updateScriptParam(String dataTypes, String uniqueMandatory, String validationType,String validationName, 
			Integer lineNumber, String inputParameter,String action ,String testRunParamDesc, String updatedBy,Integer scriptId,Integer metadataId);
	
	Integer deleteByScriptIdAndMetadataId(Integer scriptId,Integer metadataId);
	
	List<TestSetScriptParam> findByScriptIdAndMetadataId(Integer scriptId,Integer metadataId);
	
	long countByScriptIdAndMetadataId(Integer scriptId,Integer metadataId);
	
	@Modifying
	@Transactional
	@Query("UPDATE TestSetScriptParam s SET s.lineExecutionStatus = 'New', s.lineErrorMessage = NULL WHERE s.testSetLine.testRunScriptId IN (SELECT l.testRunScriptId FROM TestSetLine l WHERE l.testRun.testRunId = :testSetId AND l.enabled = 'Y' AND UPPER(l.status) != 'PASS')")
	int updateLineExecutiStatusAndLineErrorMsg(int testSetId);
	
	@Modifying
	@Transactional
	@Query("UPDATE TestSetScriptParam  SET startTime=:startTime  WHERE testRunScriptParamId=:testRunScriptParamId")
	int updateTestSetScriptParamStartTime(Date startTime,int testRunScriptParamId);
	
	@Modifying
	@Transactional
	@Query("UPDATE TestSetScriptParam  SET endTime=:endTime  WHERE testRunScriptParamId=:testRunScriptParamId")
	int updateTestSetScriptParamEndTime(Date endTime,int testRunScriptParamId);
	
	
	@Modifying
	@Transactional
	@Query("UPDATE TestSetScriptParam  SET startTime=:startTime,endTime=:endTime WHERE testRunScriptParamId=:testRunScriptParamId")
	int updateTestSetScriptParamStartAndEndTime(Date startTime,Date endTime,int testRunScriptParamId);
	

}
