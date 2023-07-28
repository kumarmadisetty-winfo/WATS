
package com.winfo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.winfo.model.TestSetScriptParam;

public interface TestSetScriptParamRepository extends JpaRepository<TestSetScriptParam, Integer>{

	@Modifying
	@Query("UPDATE TestSetScriptParam SET lineNumber = :lineNumber, inputParameter = :inputParameter,"
			+ " action = :action, testRunParamDesc = :testRunParamDesc ,validationType = :validationType,"
			+ "validationName = :validationName, dataTypes = :dataTypes, uniqueMandatory = :uniqueMandatory "
			+ "WHERE  metadataId = :metadataId AND scriptId = :scriptId")
	Integer updateScriptParam(String dataTypes, String uniqueMandatory, String validationType,String validationName, 
			Integer lineNumber, String inputParameter,String action ,String testRunParamDesc,Integer scriptId,Integer metadataId);
	
	@Modifying
	@Query("insert into TestSetScriptParam(lineNumber,inputParameter,action,testRunParamDesc,validationType,validationName,dataTypes,"
			+ "uniqueMandatory,metadataId,scriptId) values() = :lineNumber,  = :inputParameter,"
			+ "  = :action,  = :testRunParamDesc , = :validationType,"
			+ " = :validationName, = :dataTypes,  = :uniqueMandatory "
			+ "WHERE  = :metadataId AND scriptId = :scriptId")
	Integer insertScriptParam(String dataTypes, String uniqueMandatory, String validationType,String validationName, 
			Integer lineNumber, String inputParameter,String action ,String testRunParamDesc,Integer scriptId,Integer metadataId);
	
	Integer deleteByScriptIdAndMetadataId(Integer scriptId,Integer metadataId);
	
	List<TestSetScriptParam> findByScriptIdAndMetadataId(Integer scriptId,Integer metadataId);
	
	long countByScriptIdAndMetadataId(Integer scriptId,Integer metadataId);

}
