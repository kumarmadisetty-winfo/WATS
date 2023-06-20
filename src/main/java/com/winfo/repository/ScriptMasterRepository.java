package com.winfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.winfo.model.ScriptMaster;

@Repository
public interface ScriptMasterRepository extends JpaRepository<ScriptMaster, Integer> {
	
	@Query("Select max(scriptId) from ScriptMaster where scriptNumber like ?1% and productVersion=?2")
	public int getMaxScriptNumber(String newCustomScriptNumber,String productVersion);
	
	public ScriptMaster findByScriptId(Integer scriptId);
	
	public ScriptMaster findByScriptNumberAndProductVersion(String newCustomScriptNumber,String productVersion);

}
