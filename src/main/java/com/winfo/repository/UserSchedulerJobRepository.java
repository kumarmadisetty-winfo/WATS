
package com.winfo.repository;

import java.time.LocalDateTime;
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
    int updateEndDateInUserSchedulerJob(LocalDateTime endDate, String testRunName,int jobId);
	
	Optional<List<UserSchedulerJob>> findByJobId(int jobId);
	
	Optional<UserSchedulerJob> findByJobIdAndDependency(int jobId,int dependency);
	
	@Query("select t1 from UserSchedulerJob uj, TestSet t1 where uj.comments=t1.testRunName and uj.jobId=:jobId")
	List<TestSet> findByTestRuns(int jobId);

	Optional<UserSchedulerJob> findByJobIdAndComments(int jobId,String testRunName);
}
