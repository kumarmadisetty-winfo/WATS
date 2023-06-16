package com.winfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.winfo.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{
	
	Customer findByCustomerName(String customerName);
	
	Customer findByCustomerId(int customerId);
}

