package com.winfo.serviceImpl;

import java.util.Date;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.winfo.exception.WatsEBSException;
import com.winfo.model.ExecutionHistory;
import com.winfo.repository.ExecutionHistoryRepository;
import com.winfo.service.ExecutionHistoryService;
import com.winfo.utils.Constants;

@Service
@Transactional
public class ExecutionHistoryServiceImpl implements ExecutionHistoryService {

    private final ExecutionHistoryRepository executionHistoryRepository;

    @Autowired
    public ExecutionHistoryServiceImpl(ExecutionHistoryRepository executionHistoryRepository) {
        this.executionHistoryRepository = executionHistoryRepository;
    }

    @Override
    public int saveExecutionHistory(int testSetLineId, Date startDate, String createdBy) {
        try {
            ExecutionHistory history = new ExecutionHistory();
            history.setTestSetLineId(testSetLineId);
            history.setExecutionStartTime(startDate);
            history.setCreatedBy(createdBy);
            executionHistoryRepository.save(history);
            return history.getExecutionId();
        } catch (Exception e) {
            throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occurred while inserting records", e);
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
            throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occurred while retrieving the maximum executionId", e);
        }
    }
    
    public void updateExecutionHistory(String errorMessage,Date endDate,String status,String executedBy,int executionId) {
    	 executionHistoryRepository.updateExecutionHistory(errorMessage, endDate,
    			 status,executedBy, executionId);
    }
}


