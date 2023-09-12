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
    public int insertExecHistoryTbl(int testSetLineId, Date startDate, String status, String createdBy) {
        try {
            int nextExecNo = dataBaseEntryDao.getNextExecutionNum();

            ExecutionHistory history = new ExecutionHistory();
            history.setExecutionId(nextExecNo);
            history.setTestSetLineId(testSetLineId);
            history.setExecutionStartTime(startDate);
            history.setCreatedBy(createdBy);
            history.setStatus(status);
            executionHistoryRepository.save(history);
            return nextExecNo;
        } catch (Exception e) {
            throw new WatsEBSException(500, "Exception occurred while inserting records", e);
        }
    }

    @Override
    public void updateExecHistoryTbl(int executionId,Date endDate, String status, String lastUpdatedBy) {
        try {
            ExecutionHistory history = executionHistoryRepository.findById((int) executionId).orElse(null);
            if (history != null) {
                history.setExecutionEndTime(endDate);
                history.setStatus(status);
                history.setLastUpdatedBy(lastUpdatedBy);
                executionHistoryRepository.save(history);
            }
        } catch (Exception e) {
            throw new WatsEBSException(500, "Exception occurred while updating records", e);
        }
    }
}


