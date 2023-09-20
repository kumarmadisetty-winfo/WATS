package com.winfo.service;

import java.util.Date;

public interface WinTaExecutionHistoryService {
    int insertExecHistoryTbl(int testSetLineId, Date startDate, String createdBy) throws Exception;
    void updateExecHistoryTbl(int executionId, Date endDate, String status, String lastUpdatedBy) throws Exception;
    int getMaxExecutionIdForTestSetLine(int testSetLineId) throws Exception;
}

