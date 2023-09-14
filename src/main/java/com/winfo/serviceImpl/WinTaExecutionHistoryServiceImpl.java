package com.winfo.serviceImpl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.winfo.dao.DataBaseEntryDao;
import com.winfo.exception.WatsEBSException;
import com.winfo.model.ExecutionHistory;
import com.winfo.repository.ExecutionHistoryRepository;
import com.winfo.service.WinTaExecutionHistoryService;

@Service
@Transactional
public class WinTaExecutionHistoryServiceImpl extends WinTaExecutionHistoryService {

    private final ExecutionHistoryRepository executionHistoryRepository;

    @Autowired
    DataBaseEntryDao dataBaseEntryDao;
    
    public WinTaExecutionHistoryServiceImpl(ExecutionHistoryRepository executionHistoryRepository) {
        this.executionHistoryRepository = executionHistoryRepository;
    }

    @Override
    public void insertExecHistoryTbl(int testSetLineId, Date startDate, Date endDate,String status, String createdBy, String lastUpdatedBy) {
        try {

            ExecutionHistory history = new ExecutionHistory();
            history.setTestSetLineId(testSetLineId);
            history.setExecutionStartTime(startDate);
            history.setExecutionEndTime(endDate);
            history.setCreatedBy(createdBy);
            history.setStatus(status);
            history.setLastUpdatedBy(lastUpdatedBy);
            executionHistoryRepository.save(history);
            int generatedExecutionId = history.getExecutionId();
            System.out.println(generatedExecutionId);
        } catch (Exception e) {
            throw new WatsEBSException(500, "Exception occurred while inserting records", e);
        }
    }
}


