package com.winfo.dao;

import java.sql.Timestamp;
import java.text.DateFormat;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.winfo.model.AuditLogs;
@Repository
public class AuditLogsDao {
	@Autowired
	private EntityManager entityManager;
	public void updateTimeAndDate(AuditLogs auditLogs) {
		Session session = entityManager.unwrap(Session.class);
		entityManager.merge(auditLogs);
	}

}
