package com.winfo.serviceImpl;

import java.util.Date;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.winfo.exception.WatsEBSException;
import com.winfo.model.ExecutionHistory;
import com.winfo.repository.ExecutionHistoryRepository;
import com.winfo.service.WinTaExecutionHistoryService;

@Service
@Transactional
public class WinTaExecutionHistoryServiceImpl implements WinTaExecutionHistoryService {

    private final ExecutionHistoryRepository executionHistoryRepository;

    @Autowired
    public WinTaExecutionHistoryServiceImpl(ExecutionHistoryRepository executionHistoryRepository) {
        this.executionHistoryRepository = executionHistoryRepository;
    }

    @Override
    public int insertExecHistoryTbl(int testSetLineId, Date startDate, String status, String createdBy) {
        try {
            ExecutionHistory history = new ExecutionHistory();
            history.setTestSetLineId(testSetLineId);
            history.setExecutionStartTime(startDate);
            history.setCreatedBy(createdBy);
            history.setStatus(status);
            executionHistoryRepository.save(history);
            return history.getExecutionId();
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
    public int getMaxExecutionIdForTestSetLine(int testSetLineId) {
        try {
            Integer maxExecutionId = executionHistoryRepository.findMaxExecutionIdByTestSetLineId(testSetLineId);
    
            if (maxExecutionId != null) {
                return maxExecutionId;
            } else {
                throw new NoSuchElementException("No maximum executionId found for testSetLineId: " + testSetLineId);
            }
        } catch (Exception e) {
            throw new WatsEBSException(500, "Exception occurred while retrieving the maximum executionId", e);
        }
    }
}


