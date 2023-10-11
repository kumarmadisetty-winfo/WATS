package com.winfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.winfo.model.ConfigUsers;

public interface ConfigurationUsersRepository extends JpaRepository<ConfigUsers, Integer>{
		
	@Query("SELECT count(userName) from ConfigUsers where lower(userName) = lower(:userName)")
	long countByUserName(String userName);
}
