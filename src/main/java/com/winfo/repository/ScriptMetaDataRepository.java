
package com.winfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.winfo.model.ScriptMetaData;
import com.winfo.model.ScriptMaster;

@Repository
public interface ScriptMetaDataRepository extends JpaRepository<ScriptMetaData, Integer> {

	ScriptMetaData findByLineNumberAndScriptMaster(Integer lineNumber,ScriptMaster scriptMaster);
  
	Integer deleteByLineNumberAndScriptMaster(Integer lineNumber,ScriptMaster scriptMaster);
	
}
