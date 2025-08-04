package com.bank.server.service.customer;

import com.bank.server.model.customer.Customer;
import com.bank.server.repository.customer.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<List<Customer>> getCustomerByIndex(Long index) {
        List<Customer> result = customerRepository.findByCustomerIndex(index);
        return result.isEmpty() ? Optional.empty() : Optional.of(result);
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
}
