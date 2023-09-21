package com.winfo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.winfo.model.ScriptMaster;

@Repository
public interface ScriptMasterRepository extends JpaRepository<ScriptMaster, Integer> {

	List<ScriptMaster> findByTargetApplicationAndProductVersionAndModule(String targetApplication,String productVersion, String module);
	
	@Query("Select nvl(max(scriptId),0) from ScriptMaster where scriptNumber like ?1% and productVersion=?2")
	public Integer getMaxScriptNumber(String newCustomScriptNumber,String productVersion);
	
	public ScriptMaster findByScriptId(Integer scriptId);
	
	public ScriptMaster findByScriptNumberAndProductVersion(String newCustomScriptNumber,String productVersion);
	
	public List<ScriptMaster> findByProductVersion(String productVersion);
	
	@Query("from ScriptMaster where scriptId in :listOfScriptIds" )
	List<ScriptMaster> findByScriptIds(List<Integer> listOfScriptIds);
		
	@Modifying
	@Transactional
	@Query("delete from ScriptMaster where scriptId=:scriptId")
	Integer deleteByScriptId(Integer scriptId);
	
	long countByScriptId(Integer scriptId);
  
}
