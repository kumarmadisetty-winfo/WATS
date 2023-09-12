package com.winfo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.winfo.model.ExecutionHistory;

@Repository
public interface ExecutionHistoryRepository extends JpaRepository<ExecutionHistory, Integer> {
}

