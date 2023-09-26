package com.winfo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.winfo.model.ConfigLines;

public interface ConfigLinesRepository extends JpaRepository<ConfigLines, Integer>{
	
	@Query("Select valueName from ConfigLines where configurationId=:configId and keyName=:keyName")
	String getValueFromKeyNameAndConfigurationId(String keyName,int configId);
	
	@Query("Select valueName from ConfigLines where configurationId=:configId and keyName in :keyNames")
	List<String> getListOfValueFromKeyNameAndConfigurationId(List<String> keyNames,int configId);

}
