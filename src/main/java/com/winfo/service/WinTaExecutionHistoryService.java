package com.winfo.service;

import java.util.Date;

public abstract class WinTaExecutionHistoryService {
	public abstract int insertExecHistoryTbl(int testSetLineId, Date startDate,String status, String createdBy) throws Exception ;
    public abstract void updateExecHistoryTbl(int executionId,Date endDate, String status, String lastUpdatedBy) throws Exception;
}
