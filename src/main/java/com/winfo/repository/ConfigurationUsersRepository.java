package com.winfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.winfo.model.ConfigUsers;

public interface ConfigurationUsersRepository extends JpaRepository<ConfigUsers, Integer>{
		
	long countByUserName(String userName);
}
