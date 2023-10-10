package com.winfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.winfo.model.LookUp;

@Repository
public interface LookUpRepository extends JpaRepository<LookUp, Integer>{
	
	 LookUp findByLookUpName(String lookUpName);

}
