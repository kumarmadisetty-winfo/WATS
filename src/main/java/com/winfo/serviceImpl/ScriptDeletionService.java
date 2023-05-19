package com.winfo.serviceImpl;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.winfo.model.TestSetLine;

@Service
public class ScriptDeletionService {

	@Autowired
	private EntityManager em;

	@Autowired
	private PlatformTransactionManager transactionManager;

	private final ReadWriteLock lock = new ReentrantReadWriteLock();

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void deleteScriptFromTestRun(Integer integer) {
		lock.writeLock().lock();
		try {
			TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
			em.remove(em.find(TestSetLine.class, integer));
			transactionManager.commit(status);
		} finally {
			lock.writeLock().unlock();
		}
	}
}
