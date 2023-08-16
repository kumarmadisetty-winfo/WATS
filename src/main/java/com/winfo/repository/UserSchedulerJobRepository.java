
package com.winfo.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.winfo.model.TestSet;
import com.winfo.model.UserSchedulerJob;

@Repository
public interface UserSchedulerJobRepository extends JpaRepository<UserSchedulerJob, Integer>{

	@Modifying
    @Transactional
    @Query("update UserSchedulerJob set endDate=:endDate where comments=:testRunName and jobId=:jobId")
    int updateEndDateInUserSchedulerJob(Date endDate, String testRunName,String jobId);
	
	Optional<List<UserSchedulerJob>> findByJobId(String jobId);
	Optional<UserSchedulerJob> findByJobIdAndDependency(String jobId,String dependency);
	@Query("select TestSet from UserSchedulerJob, TestSet where comments=testRunName and jobId=:jobId")
	List<TestSet> findByTestRuns(String jobId);
}
