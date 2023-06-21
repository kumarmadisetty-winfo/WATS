package com.winfo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import com.winfo.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	Customer findByCustomerName(String customerName);

	Customer findByCustomerId(int customerId);

	@Query("select customerName from Customer")
	List<String> findListOfCustomers();

	@Query("select c.customerName from Customer c, UserRole r, User u where upper(u.userId)=upper(?1) and "
			+ "upper(r.userId)=upper(u.userId) and (upper(r.userType) in ('SUPER_ADMIN','SUPPORT') or c.customerId=u.customerId)")
	List<String> findListOfCustomers(String userName);
}
