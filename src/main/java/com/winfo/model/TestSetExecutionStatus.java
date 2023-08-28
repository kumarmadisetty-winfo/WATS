package com.winfo.model;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Table(name = "test_run_execute_status")
@Data
public class TestSetExecutionStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EXECUTION_ID")
    private Integer executionId;

    @Column(name = "TEST_RUN_ID")
    private Integer testRunId;

    @Column(name = "SESSION_ID")
    private Integer sessionId;

    @Column(name = "REQUEST_COUNT")
    private Integer requestCount;

    @Column(name = "RESPONSE_COUNT")
    private Integer responseCount;

    @Column(name = "EXECUTE_BY_MAIL")
    private String executeByMail;

    @Column(name = "EXECUTED_BY")
    private String executedBy;

    @Column(name = "EXECUTION_DATE")
    @Temporal(TemporalType.DATE)
    private Date executionDate;
}
