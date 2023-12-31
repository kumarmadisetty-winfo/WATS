package com.winfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.winfo.model.ExecuteStatus;

public interface ExecuteStatusRepository extends JpaRepository<ExecuteStatus, Integer> {
	
	@Modifying
	@Transactional
	@Query("UPDATE ExecuteStatus e SET e.executeStatusPK.executedBy =:executedBy, flag = 'E',executionDate = TO_DATE(SYSDATE, 'DD/MM/YYYY HH:MI AM') WHERE e.executeStatusPK.testSetId =:testSetId")
	int updateTestRunExecutionStatus(String executedBy,int testSetId);

}
