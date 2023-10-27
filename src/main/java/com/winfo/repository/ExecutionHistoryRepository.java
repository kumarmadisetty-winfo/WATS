package com.winfo.repository;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.winfo.model.ExecutionHistory;

@Repository
public interface ExecutionHistoryRepository extends JpaRepository<ExecutionHistory, Integer> {
    @Query("SELECT MAX(e.executionId) FROM ExecutionHistory e WHERE e.testSetLineId = :testSetLineId")
    Integer findMaxExecutionIdByTestSetLineId(Integer testSetLineId);
    
    @Modifying
    @Transactional
    @Query("update ExecutionHistory set lineErrorMessage=:lineErrorMessage, executionEndTime=:endDate,status=:status,lastUpdatedBy=:lastUpdatedBy where executionId=:executionId")
    int updateExecutionHistory(String lineErrorMessage,Date endDate, String status, String lastUpdatedBy,int executionId);
    
    
    @Modifying
    @Transactional
    @Query("delete from ExecutionHistory where testSetLineId=:testSetLineId")
    int deleteExecutionHistory(int testSetLineId);
    
}

