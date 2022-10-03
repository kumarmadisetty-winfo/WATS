package com.winfo.dao;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.winfo.model.AuditLogs;
@Repository
public class AuditLogsDao {
	@Autowired
	private EntityManager entityManager;
	public void updateTimeAndDate(AuditLogs auditLogs) {
		entityManager.merge(auditLogs);
	}

}
