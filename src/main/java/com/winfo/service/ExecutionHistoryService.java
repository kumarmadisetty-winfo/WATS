package com.winfo.service;

import java.util.Date;

public interface ExecutionHistoryService {
    int saveExecutionHistory(int testSetLineId, Date startDate, String createdBy) throws Exception;
    int getMaxExecutionIdForTestSetLine(int testSetLineId) throws Exception;
    void updateExecutionHistory(String errorMessage,Date endDate,String status,String executedBy,int executionId);
}

