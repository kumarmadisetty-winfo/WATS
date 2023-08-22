
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
	
//	List<UserSchedulerJob> findByJobId(Integer jobId);
	
	@Query("select min(startDate), max(endDate) from UserSchedulerJob where jobId = ?1")
	public List<Object[]> getMinandMaxTime(int jobId);
	
	@Query("select comments from UserSchedulerJob where jobId = ?1")
	public List<String> getTestSetNames(int jobId);

	@Query("select startDate from UserSchedulerJob where comments = ?1 and jobId = ?2")
	String getStartTime(String jobName, Integer jobId);

	@Query("select endDate from UserSchedulerJob where comments = ?1 and jobId = ?2")
	LocalDateTime getEndTime(String jobName, Integer jobId);
}
