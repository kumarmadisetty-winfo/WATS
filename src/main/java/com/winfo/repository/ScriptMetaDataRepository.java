
package com.winfo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.winfo.model.ScriptMetaData;
import com.winfo.model.ScriptMaster;

public interface ScriptMetaDataRepository extends JpaRepository<ScriptMetaData, Integer> {

	ScriptMetaData findByLineNumberAndScriptMaster(Integer lineNumber,ScriptMaster scriptMaster);
	
	Integer deleteByScriptMetaDataId(Integer metaDataId);
	
	@Modifying
	@Transactional
	@Query("delete from ScriptMetaData where SCRIPT_ID=:scriptId")
	Integer deleteByScriptMaster(int scriptId);
	
	List<ScriptMetaData> findByScriptMaster(ScriptMaster scriptMaster);
	
	long countByScriptMaster(ScriptMaster scriptMaster);

}
