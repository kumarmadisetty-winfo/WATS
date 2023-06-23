package com.winfo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.winfo.model.ScriptMaster;

@Repository
public interface ScriptMasterRepository extends JpaRepository<ScriptMaster, Integer> {
	
	List<ScriptMaster> findByProductVersionAndModule(String productVersion, String module);
	
}
