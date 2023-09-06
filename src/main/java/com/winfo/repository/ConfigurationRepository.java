package com.winfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.winfo.model.ConfigTable;

public interface ConfigurationRepository extends JpaRepository<ConfigTable, Integer>{
	
	ConfigTable findFirstByCustomerId(int customerId);
	
	@Query("select configurationName from ConfigTable where configurationId = ?1")
	public String getConfigNameUsingId(int configId);
	
	@Query("select customerId from ConfigTable where configurationId = ?1")
	int getCustomerIdUsingconfigurationId(int configId);
	
	
}
