package com.winfo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.winfo.model.UserRole;
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
	@Query("SELECT userId FROM UserRole where upper(userId) =:userId")
	List<String> getUserId(String userId);
	
	
	@Query("SELECT userId FROM UserRole where upper(userId) =:userId and NVL(endDate, SYSDATE) >= SYSDATE")
	List<String> getEndDate(String userId);
	
	
	@Query("SELECT userId FROM UserRole where upper(userId) =:userId and NVL(passwordExpiry, SYSDATE) >= SYSDATE")
	List<String> getPasswordExpiry(String userId);
	
	
	@Query("SELECT userId FROM UserRole where upper(userId) =:userId and upper(status) = 'ACTIVE'")
	List<String> getUserActive(String userId);
	
}
