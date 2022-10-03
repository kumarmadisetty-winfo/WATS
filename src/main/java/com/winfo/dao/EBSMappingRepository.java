package com.winfo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.winfo.model.SeleniumEbsMapping;

@Repository
public interface EBSMappingRepository extends JpaRepository<SeleniumEbsMapping, Long> {
	List<SeleniumEbsMapping> findAll();

	SeleniumEbsMapping findBySeleniumActionName(String actionName);
}
