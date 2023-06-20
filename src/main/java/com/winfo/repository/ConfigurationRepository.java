package com.winfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.winfo.model.ConfigTable;

public interface ConfigurationRepository extends JpaRepository<ConfigTable, Integer>{
	
	ConfigTable findFirstByCustomerId(int customerId);
}
