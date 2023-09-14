package com.winfo.service;

import java.util.Date;

public abstract class WinTaExecutionHistoryService {
	public abstract void insertExecHistoryTbl(int testSetLineId, Date startDate,Date endDate,String status, String createdBy, String lastupdatedBy) throws Exception ;
}
