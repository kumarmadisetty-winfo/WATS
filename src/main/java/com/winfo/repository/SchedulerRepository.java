package com.winfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.winfo.model.Scheduler;

@Repository
public interface SchedulerRepository extends JpaRepository<Scheduler, Integer>{

	Scheduler findByJobName(String jobName);
	Scheduler findByJobId(int jobId);

}
