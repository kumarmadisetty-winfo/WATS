package com.winfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.winfo.model.Scheduler;

@Repository
public interface SchedulerRepository extends JpaRepository<Scheduler, Integer>{

	Scheduler findByJobName(String jobName);
	Scheduler findByJobId(int jobId);
	
	@Modifying
	@Transactional
	@Query("update Scheduler set status=:status where jobId=:jobId")
	int updateSchedulerStatus(String status,int jobId);

}
