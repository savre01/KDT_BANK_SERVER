// CustomerRepository.java
package com.bank.server.repository.customer;

import com.bank.server.model.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByCustomerIndex(Long customerIndex);
}

