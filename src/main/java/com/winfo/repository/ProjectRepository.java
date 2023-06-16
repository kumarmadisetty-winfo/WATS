
package com.winfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.winfo.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Integer>{
	
	long countByProjectName(String projectName);
	
	Project findByProjectNameAndCustomerId(String projectName,int customerId);
}

