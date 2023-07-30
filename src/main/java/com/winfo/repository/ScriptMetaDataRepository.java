
package com.winfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.winfo.model.ScriptMetaData;
import com.winfo.model.ScriptMaster;

public interface ScriptMetaDataRepository extends JpaRepository<ScriptMetaData, Integer> {

	ScriptMetaData findByLineNumberAndScriptMaster(Integer lineNumber,ScriptMaster scriptMaster);
	
	Integer deleteByScriptMetaDataId(Integer metaDataId);
}
