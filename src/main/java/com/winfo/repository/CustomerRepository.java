package com.winfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.winfo.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{
	
	Customer findByCustomerName(String customerName);
}

