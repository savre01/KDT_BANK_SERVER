package com.bank.server.controller.customer;

import com.bank.server.dto.customer.CustomerRequest;
import com.bank.server.dto.customer.CustomerResponse;
import com.bank.server.model.customer.Customer;
import com.bank.server.service.customer.CustomerService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        return ResponseEntity.ok(
            customerService.getAllCustomers().stream()
                .map(CustomerResponse::new)
                .toList()
        );
    }

    @GetMapping("/{customerIndex}")
    public ResponseEntity<?> getCustomerByIndex(@PathVariable Long customerIndex) {
        return customerService.getCustomerByIndex(customerIndex)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@RequestBody CustomerRequest request) {
        Customer customer = new Customer();
        customer.setCustomerId(request.getCustomerId());
        customer.setCustomerPassword(request.getCustomerPassword());
        customer.setCustomerName(request.getCustomerName());
        customer.setCustomerPhone(request.getCustomerPhone());
        customer.setCustomerBirth(request.getCustomerBirth());
        return ResponseEntity.ok(new CustomerResponse(customerService.createCustomer(customer)));
    }
}