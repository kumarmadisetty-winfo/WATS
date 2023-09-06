package com.winfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.winfo.model.ConfigLines;

public interface ConfigLinesRepository extends JpaRepository<ConfigLines, Integer>{
	
	@Query("Select valueName from ConfigLines where configurationId=:configId and keyName=:keyName")
	String getPdfPathusingConfigurationIdAndkeyName(int configId,String keyName);

}
