package com.winfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.winfo.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("select email from User where UPPER(userId)=:userId")
    String findByUserId(String userId);
}
