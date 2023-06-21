package com.winfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.winfo.model.LogDetailsTable;
import com.winfo.model.TestSetLine;

public interface LogDetailsRepository extends JpaRepository<LogDetailsTable, Integer>{
}
