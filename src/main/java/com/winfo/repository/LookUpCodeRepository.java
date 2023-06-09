package com.winfo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.winfo.model.LookUpCode;

@Repository
public interface LookUpCodeRepository extends JpaRepository<LookUpCode, Integer> {

	@Query("select lc.meaning from LookUpCode lc where lc.lookUpName = :lookUpCodeName")
	List<String> findLookUpCodesUsingLookUpName(@Param("lookUpCodeName") String lookUpCodeName);

	@Query("select lc.meaning from LookUpCode lc where lc.lookUpName = 'ACTION' and lc.targetApplication = :targetApplication")
	List<String> getActionByTargetApplication(@Param("targetApplication") String targetApplication);

	@Query("SELECT t2.meaning FROM ScriptMetaData t1 INNER JOIN LookUpCode t2 ON t1.action = t2.lookUpCode where t1.scriptMaster.scriptId = :scriptId and t1.scriptMetaDataId = :scriptMetaDataId")
	String getActionMeaningScriptIdAndLineNumber(@Param("scriptId") Integer scriptId,
			@Param("scriptMetaDataId") Integer scriptMetaDataId);

	@Query("SELECT lc.meaning from LookUpCode lc where lower(lc.lookUpCode) = lower(:lookUpCode) and lower(lc.lookUpName) = lower(:lookUpName)")
	String getMeaningByTargetCode(@Param("lookUpCode") String lookUpCode, @Param("lookUpName") String lookUpName);

	@Query("Select targetCode FROM LookUpCode where lookUpName='TARGET CLIENT' and lookUpCode=:customerName")
	String getCustomerURLByCustomerName(String customerName);


}
