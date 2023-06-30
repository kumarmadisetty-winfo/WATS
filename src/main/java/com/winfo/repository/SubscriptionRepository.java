package com.winfo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.winfo.model.Subscription;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long>{
	
	@Query("SELECT balance FROM Subscription WHERE status = 'Active' and uom = 'Script' and to_date(sysdate) >= startDate and to_date(sysdate) <= endDate ORDER BY subscription_id")
	List<Long> findBalanceByStatusAndUomAndStartDateEndDateOrderBySubscriptionId();
		
	
}
