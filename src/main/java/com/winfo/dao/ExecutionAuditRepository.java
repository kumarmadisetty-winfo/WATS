package com.winfo.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Repository;

import com.winfo.model.ExecutionAudit;

@Repository
@RefreshScope
public class ExecutionAuditRepository {
	
	@PersistenceContext
	EntityManager em;
	
	public void insertTestrundata(ExecutionAudit executionAudit) {
		System.out.println("executionAudit savaed");
		em.persist(executionAudit);

	}

	
	

}
