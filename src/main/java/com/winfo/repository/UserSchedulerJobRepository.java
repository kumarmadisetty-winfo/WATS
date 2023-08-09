
package com.winfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.winfo.model.UserSchedulerJob;

@Repository
public interface UserSchedulerJobRepository extends JpaRepository<UserSchedulerJob, Integer>{

	@Query(value = "SELECT WIN_TA_JOB_ID_SEQ.NEXTVAL from dual",nativeQuery = true) 
	Integer getNewJobIdFromSequence();

}
