
package com.winfo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.winfo.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Integer>{
	
	long countByProjectName(String projectName);
	
	Project findByProjectNameAndCustomerId(String projectName,int customerId);
	
	@Query("select max(projectName) from Project where projectName like ?1%")
	public String getMaxProjectName(String oldProjectName);
	
	@Query("select projectName from Project where projectId = ?1")
	public String getProjectNameById(int projectId);
	
	@Query("select testRunName from TestSet where projectId in (select projectId from Project where productVersion=:productVersion)")
	List<String> getTestRunData(String productVersion);
	

}


